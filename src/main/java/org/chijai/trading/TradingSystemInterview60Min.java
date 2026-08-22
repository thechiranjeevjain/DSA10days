package org.chijai.trading;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * TradingSystemInterview60Min
 *
 * Senior Java / electronic-trading LLD scoped for a 40–60 minute interview.
 *
 * Core flow:
 *   normalize price -> risk -> price-time book -> match -> fill / rest / cancel
 *
 * Intentionally excluded:
 *   LRU, sequence buffers, remote retries, Redis, distributed idempotency,
 *   exposure DAGs, persistence, threading and zero-GC optimizations.
 *
 * Compile:
 *   javac --release 17 TradingSystemInterview60Min.java
 *
 * Run:
 *   java TradingSystemInterview60Min
 */
public class TradingSystemInterview60Min {

    // =====================================================================
    // 1. FIXED-POINT BOUNDARY
    // =====================================================================

    /**
     * Normalize decimal price only at ingress:
     *   "101.2500" -> 1_012_500
     *
     * Use scaled long internally for compare/sort/match.
     *
     * Denormalize only at egress:
     *   1_012_500 -> "101.2500"
     *
     * Real systems usually derive scale/tick size from instrument metadata.
     */
    static final class FixedPointPrice {
        static final int DECIMAL_PLACES = 4;

        private FixedPointPrice() {}

        static long normalize(String decimalPrice) {
            return new BigDecimal(decimalPrice)
                    .setScale(DECIMAL_PLACES, RoundingMode.UNNECESSARY)
                    .movePointRight(DECIMAL_PLACES)
                    .longValueExact();
        }

        static String denormalize(long scaledPrice) {
            return BigDecimal.valueOf(scaledPrice, DECIMAL_PLACES)
                    .setScale(DECIMAL_PLACES, RoundingMode.UNNECESSARY)
                    .toPlainString();
        }
    }

    // =====================================================================
    // 2. DOMAIN
    // =====================================================================

    enum Side { BUY, SELL }
    enum OrderType { LIMIT, MARKET }
    enum TimeInForce { GTC, DAY, IOC, FOK }

    enum OrderState {
        NEW, ACKNOWLEDGED, PARTIALLY_FILLED, FILLED, CANCELLED, REJECTED;

        boolean terminal() {
            return this == FILLED || this == CANCELLED || this == REJECTED;
        }
    }

    static final class Order {
        final String clientOrderId;
        final String instrumentId;
        final String firmId;
        final Side side;
        final OrderType orderType;
        final TimeInForce timeInForce;

        // Normalized fixed-point price. Ignored for MARKET orders.
        final long limitPrice;

        final long originalQuantity;
        final long arrivalSequence;

        long remainingQuantity;
        long filledQuantity;
        long averageFillPrice;
        OrderState state = OrderState.NEW;

        // Back-pointer to resting level; null when not resting.
        PriceLevel priceLevel;

        Order(
                String clientOrderId,
                String instrumentId,
                String firmId,
                Side side,
                OrderType orderType,
                long limitPrice,
                long originalQuantity,
                long arrivalSequence,
                TimeInForce timeInForce) {

            this.clientOrderId = Objects.requireNonNull(clientOrderId);
            this.instrumentId = Objects.requireNonNull(instrumentId);
            this.firmId = firmId;
            this.side = Objects.requireNonNull(side);
            this.orderType = Objects.requireNonNull(orderType);
            this.timeInForce = Objects.requireNonNull(timeInForce);

            if (originalQuantity <= 0) {
                throw new IllegalArgumentException("quantity must be > 0");
            }
            if (orderType == OrderType.LIMIT && limitPrice <= 0) {
                throw new IllegalArgumentException("limit price must be > 0");
            }

            this.limitPrice = limitPrice;
            this.originalQuantity = originalQuantity;
            this.remainingQuantity = originalQuantity;
            this.arrivalSequence = arrivalSequence;
        }

        boolean isMarketOrder() {
            return orderType == OrderType.MARKET;
        }

        boolean isDone() {
            return remainingQuantity == 0;
        }

        boolean isTerminal() {
            return state.terminal();
        }

        /**
         * Invariant:
         *   originalQuantity = filledQuantity + remainingQuantity
         *
         * Checked long arithmetic is enough for interview code.
         * If constraints allow price*qty overflow, use a wider intermediate.
         */
        void applyFill(long executionQuantity, long executionPrice) {
            if (executionQuantity <= 0 || executionQuantity > remainingQuantity) {
                throw new IllegalArgumentException("invalid execution quantity");
            }

            long newFilled = Math.addExact(filledQuantity, executionQuantity);
            long oldValue = Math.multiplyExact(averageFillPrice, filledQuantity);
            long newValue = Math.multiplyExact(executionPrice, executionQuantity);

            averageFillPrice = Math.addExact(oldValue, newValue) / newFilled;
            filledQuantity = newFilled;
            remainingQuantity -= executionQuantity;
            state = isDone() ? OrderState.FILLED : OrderState.PARTIALLY_FILLED;

            if (filledQuantity + remainingQuantity != originalQuantity) {
                throw new IllegalStateException("quantity invariant broken");
            }
        }

        void cancelRemainder() {
            if (!isDone()) {
                state = OrderState.CANCELLED;
            }
        }

        @Override
        public String toString() {
            String price = isMarketOrder()
                    ? "MARKET"
                    : FixedPointPrice.denormalize(limitPrice);

            return "Order{id=%s, side=%s, price=%s, remaining=%d, filled=%d, avgPx=%s, state=%s}"
                    .formatted(
                            clientOrderId,
                            side,
                            price,
                            remainingQuantity,
                            filledQuantity,
                            FixedPointPrice.denormalize(averageFillPrice),
                            state);
        }
    }

    // =====================================================================
    // 3. PRICE LEVEL
    // =====================================================================

    /**
     * TreeMap resolves price priority.
     * FIFO deque resolves time priority inside one price.
     *
     * Interview trade-off:
     * arbitrary ArrayDeque.remove(order) is O(orders at this level).
     *
     * Production follow-up:
     * intrusive doubly-linked list + ID->node index for O(1) unlink.
     */
    static final class PriceLevel {
        final long price;
        final ArrayDeque<Order> fifo = new ArrayDeque<>();
        long totalRemainingQuantity;

        PriceLevel(long price) {
            this.price = price;
        }

        void addLast(Order order) {
            fifo.addLast(order);
            totalRemainingQuantity =
                    Math.addExact(totalRemainingQuantity, order.remainingQuantity);
            order.priceLevel = this;
        }

        Order bestOrder() {
            return fifo.peekFirst();
        }

        /**
         * Remove a fully consumed head BEFORE fill mutates remainingQuantity.
         */
        Order removeBestOrder() {
            Order order = fifo.pollFirst();
            if (order != null) {
                totalRemainingQuantity -= order.remainingQuantity;
                order.priceLevel = null;
            }
            return order;
        }

        /**
         * Partial fill: order stays at head and keeps time priority.
         */
        void reduceQuantity(long executionQuantity) {
            totalRemainingQuantity -= executionQuantity;
        }

        boolean remove(Order order) {
            if (!fifo.remove(order)) {
                return false;
            }
            totalRemainingQuantity -= order.remainingQuantity;
            order.priceLevel = null;
            return true;
        }

        boolean isEmpty() {
            return fifo.isEmpty();
        }
    }

    // =====================================================================
    // 4. ORDER BOOK
    // =====================================================================

    /**
     * One instrument's book.
     *
     * bids: reverse order -> firstKey() = highest bid
     * asks: natural order -> firstKey() = lowest ask
     */
    static final class OrderBook {
        final String instrumentId;

        private final TreeMap<Long, PriceLevel> bids =
                new TreeMap<>(Comparator.reverseOrder());
        private final TreeMap<Long, PriceLevel> asks = new TreeMap<>();
        private final Map<String, Order> restingOrdersById = new HashMap<>();

        OrderBook(String instrumentId) {
            this.instrumentId = Objects.requireNonNull(instrumentId);
        }

        void addRestingOrder(Order order) {
            validateInstrument(order);

            if (order.isMarketOrder()) {
                throw new IllegalArgumentException("market order cannot rest");
            }
            if (order.isDone() || order.isTerminal()) {
                throw new IllegalArgumentException("terminal order cannot rest");
            }
            if (restingOrdersById.containsKey(order.clientOrderId)) {
                throw new IllegalArgumentException("order already resting");
            }

            TreeMap<Long, PriceLevel> side = levelsFor(order.side);
            PriceLevel level =
                    side.computeIfAbsent(order.limitPrice, PriceLevel::new);

            level.addLast(order);
            restingOrdersById.put(order.clientOrderId, order);
        }

        boolean cancel(String clientOrderId) {
            Order order = restingOrdersById.get(clientOrderId);
            if (order == null || order.priceLevel == null) {
                return false;
            }

            PriceLevel level = order.priceLevel;
            if (!level.remove(order)) {
                return false;
            }

            restingOrdersById.remove(clientOrderId);
            removeLevelIfEmpty(order.side, level);
            order.cancelRemainder();
            return true;
        }

        Long bestBid() {
            return bids.isEmpty() ? null : bids.firstKey();
        }

        Long bestAsk() {
            return asks.isEmpty() ? null : asks.firstKey();
        }

        PriceLevel bestOppositeLevel(Side incomingSide) {
            NavigableMap<Long, PriceLevel> opposite = oppositeLevels(incomingSide);
            return opposite.isEmpty() ? null : opposite.firstEntry().getValue();
        }

        NavigableMap<Long, PriceLevel> oppositeLevels(Side incomingSide) {
            return incomingSide == Side.BUY ? asks : bids;
        }

        Order findRestingOrder(String clientOrderId) {
            return restingOrdersById.get(clientOrderId);
        }

        /**
         * MatchingEngine has already physically removed this order from PriceLevel.
         * Only remove it from the resting-order index here.
         */
        void forgetFullyFilledRestingOrder(Order order, PriceLevel oldLevel) {
            restingOrdersById.remove(order.clientOrderId);
            removeLevelIfEmpty(order.side, oldLevel);
        }

        private TreeMap<Long, PriceLevel> levelsFor(Side side) {
            return side == Side.BUY ? bids : asks;
        }

        private void removeLevelIfEmpty(Side side, PriceLevel level) {
            if (level.isEmpty()) {
                levelsFor(side).remove(level.price);
            }
        }

        private void validateInstrument(Order order) {
            if (!instrumentId.equals(order.instrumentId)) {
                throw new IllegalArgumentException("wrong instrument for book");
            }
        }
    }

    // =====================================================================
    // 5. MATCHING ENGINE
    // =====================================================================

    record Fill(
            String aggressiveOrderId,
            String passiveOrderId,
            long price,
            long quantity) {}

    static final class MatchingEngine {

        /**
         * Rules:
         * - BUY crosses if market OR buy.limit >= bestAsk
         * - SELL crosses if market OR sell.limit <= bestBid
         * - execute at passive/resting price
         * - same-firm policy here: cancel passive
         */
        List<Fill> match(Order incoming, OrderBook book) {
            List<Fill> fills = new ArrayList<>();

            // FOK = all or none, so pre-check before any mutation.
            if (incoming.timeInForce == TimeInForce.FOK
                    && !canFullyFill(incoming, book)) {
                incoming.cancelRemainder();
                return fills;
            }

            while (incoming.remainingQuantity > 0) {
                PriceLevel bestLevel = book.bestOppositeLevel(incoming.side);

                if (bestLevel == null || !crosses(incoming, bestLevel.price)) {
                    break;
                }

                Order passive = bestLevel.bestOrder();

                // Simplified self-match prevention policy.
                if (sameFirm(incoming, passive)) {
                    book.cancel(passive.clientOrderId);
                    continue;
                }

                long executionQuantity =
                        Math.min(incoming.remainingQuantity, passive.remainingQuantity);
                long executionPrice = bestLevel.price; // passive price wins

                boolean passiveDone =
                        executionQuantity == passive.remainingQuantity;

                if (passiveDone) {
                    // Physical removal happens exactly once.
                    Order removed = bestLevel.removeBestOrder();
                    if (removed != passive) {
                        throw new IllegalStateException("price-time queue corrupted");
                    }
                    book.forgetFullyFilledRestingOrder(passive, bestLevel);
                } else {
                    // Resting order remains at head; priority is preserved.
                    bestLevel.reduceQuantity(executionQuantity);
                }

                passive.applyFill(executionQuantity, executionPrice);
                incoming.applyFill(executionQuantity, executionPrice);

                fills.add(new Fill(
                        incoming.clientOrderId,
                        passive.clientOrderId,
                        executionPrice,
                        executionQuantity));
            }

            applyRemainderPolicy(incoming, book);
            return fills;
        }

        private boolean crosses(Order incoming, long passivePrice) {
            if (incoming.isMarketOrder()) {
                return true;
            }
            return incoming.side == Side.BUY
                    ? incoming.limitPrice >= passivePrice
                    : incoming.limitPrice <= passivePrice;
        }

        private boolean sameFirm(Order a, Order b) {
            return a.firmId != null
                    && b.firmId != null
                    && a.firmId.equals(b.firmId);
        }

        private void applyRemainderPolicy(Order incoming, OrderBook book) {
            if (incoming.isDone()) {
                return;
            }

            // Market and IOC remainders never rest.
            if (incoming.isMarketOrder() || incoming.timeInForce == TimeInForce.IOC) {
                incoming.cancelRemainder();
                return;
            }

            // A FOK reaching this point indicates an invariant bug.
            if (incoming.timeInForce == TimeInForce.FOK) {
                throw new IllegalStateException("FOK partially executed");
            }

            // GTC / DAY unmatched LIMIT remainder rests.
            book.addRestingOrder(incoming);
        }

        /**
         * Simple interview FOK pre-check:
         * scan only crossing levels and count eligible quantity.
         */
        private boolean canFullyFill(Order incoming, OrderBook book) {
            long needed = incoming.remainingQuantity;

            for (PriceLevel level : book.oppositeLevels(incoming.side).values()) {
                if (!crosses(incoming, level.price)) {
                    break;
                }

                for (Order passive : level.fifo) {
                    if (sameFirm(incoming, passive)) {
                        continue;
                    }

                    needed -= passive.remainingQuantity;
                    if (needed <= 0) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    // =====================================================================
    // 6. MINIMAL PRE-TRADE RISK
    // =====================================================================

    enum RiskDecision { PASS, REJECT, KILL }

    interface RiskCheck {
        RiskDecision check(Order order);
    }

    static final class MaxOrderQuantityCheck implements RiskCheck {
        private final long maxQuantity;

        MaxOrderQuantityCheck(long maxQuantity) {
            this.maxQuantity = maxQuantity;
        }

        @Override
        public RiskDecision check(Order order) {
            return order.originalQuantity <= maxQuantity
                    ? RiskDecision.PASS
                    : RiskDecision.REJECT;
        }
    }

    static final class KillSwitchCheck implements RiskCheck {
        private volatile boolean active;

        void activate() {
            active = true;
        }

        void deactivate() {
            active = false;
        }

        @Override
        public RiskDecision check(Order order) {
            return active ? RiskDecision.KILL : RiskDecision.PASS;
        }
    }

    /**
     * Strategy/pipeline extension point.
     *
     * Production follow-up:
     * exposure state, hierarchical limits, begin/commit/rollback.
     */
    static final class RiskEngine {
        private final List<RiskCheck> checks;

        RiskEngine(List<RiskCheck> checks) {
            this.checks = List.copyOf(checks);
        }

        RiskDecision check(Order order) {
            for (RiskCheck check : checks) {
                RiskDecision decision = check.check(order);
                if (decision != RiskDecision.PASS) {
                    return decision;
                }
            }
            return RiskDecision.PASS;
        }
    }

    // =====================================================================
    // 7. THIN APPLICATION SERVICE
    // =====================================================================

    record SubmissionResult(
            RiskDecision riskDecision,
            List<Fill> fills,
            Order order) {

        SubmissionResult {
            fills = List.copyOf(fills);
        }
    }

    static final class TradingService {
        private final OrderBook orderBook;
        private final RiskEngine riskEngine;
        private final MatchingEngine matchingEngine = new MatchingEngine();

        private long nextArrivalSequence = 1;

        TradingService(OrderBook orderBook, RiskEngine riskEngine) {
            this.orderBook = Objects.requireNonNull(orderBook);
            this.riskEngine = Objects.requireNonNull(riskEngine);
        }

        Order newLimitOrder(
                String id,
                String firmId,
                Side side,
                String decimalPrice,
                long quantity,
                TimeInForce tif) {

            return new Order(
                    id,
                    orderBook.instrumentId,
                    firmId,
                    side,
                    OrderType.LIMIT,
                    FixedPointPrice.normalize(decimalPrice),
                    quantity,
                    nextArrivalSequence++,
                    tif);
        }

        Order newMarketOrder(
                String id,
                String firmId,
                Side side,
                long quantity,
                TimeInForce tif) {

            return new Order(
                    id,
                    orderBook.instrumentId,
                    firmId,
                    side,
                    OrderType.MARKET,
                    0,
                    quantity,
                    nextArrivalSequence++,
                    tif);
        }

        SubmissionResult submit(Order order) {
            RiskDecision riskDecision = riskEngine.check(order);

            if (riskDecision != RiskDecision.PASS) {
                order.state = OrderState.REJECTED;
                return new SubmissionResult(riskDecision, List.of(), order);
            }

            order.state = OrderState.ACKNOWLEDGED;
            List<Fill> fills = matchingEngine.match(order, orderBook);

            return new SubmissionResult(RiskDecision.PASS, fills, order);
        }

        boolean cancel(String clientOrderId) {
            return orderBook.cancel(clientOrderId);
        }
    }

    // =====================================================================
    // 8. SMALL RUNNABLE DEMO
    // =====================================================================

    public static void main(String[] args) {
        testFixedPoint();

        KillSwitchCheck killSwitch = new KillSwitchCheck();
        RiskEngine risk = new RiskEngine(List.of(
                killSwitch,
                new MaxOrderQuantityCheck(1_000)));

        OrderBook book = new OrderBook("AAPL");
        TradingService trading = new TradingService(book, risk);

        // Rest two asks.
        trading.submit(trading.newLimitOrder(
                "S1", "FIRM-B", Side.SELL, "101.0000", 50, TimeInForce.GTC));

        trading.submit(trading.newLimitOrder(
                "S2", "FIRM-B", Side.SELL, "102.0000", 40, TimeInForce.GTC));

        require(
                book.bestAsk() == FixedPointPrice.normalize("101.0000"),
                "best ask");

        // BUY 70 @ 102 => 50 @ 101 + 20 @ 102.
        SubmissionResult buy = trading.submit(trading.newLimitOrder(
                "B1", "FIRM-A", Side.BUY, "102.0000", 70, TimeInForce.GTC));

        require(buy.fills().size() == 2, "two fills");
        require(buy.fills().get(0).quantity() == 50, "first fill qty");
        require(
                buy.fills().get(0).price() == FixedPointPrice.normalize("101.0000"),
                "passive price");

        Order s2 = book.findRestingOrder("S2");
        require(s2 != null && s2.remainingQuantity == 20, "partial fill keeps remainder");
        require(
                book.bestAsk() == FixedPointPrice.normalize("102.0000"),
                "best ask after fill");

        // IOC takes available 20 then cancels remaining 10.
        SubmissionResult ioc = trading.submit(trading.newLimitOrder(
                "B2", "FIRM-A", Side.BUY, "102.0000", 30, TimeInForce.IOC));

        require(ioc.order().filledQuantity == 20, "IOC fill");
        require(ioc.order().state == OrderState.CANCELLED, "IOC cancel remainder");
        require(book.bestAsk() == null, "ask book empty");

        // FOK: only 10 available, request 20 => zero fills.
        trading.submit(trading.newLimitOrder(
                "S3", "FIRM-B", Side.SELL, "103.0000", 10, TimeInForce.GTC));

        SubmissionResult fok = trading.submit(trading.newLimitOrder(
                "B3", "FIRM-A", Side.BUY, "103.0000", 20, TimeInForce.FOK));

        require(fok.fills().isEmpty(), "FOK all or none");
        require(fok.order().state == OrderState.CANCELLED, "FOK cancelled");
        require(book.findRestingOrder("S3") != null, "FOK did not mutate book");

        // Explicit cancel.
        require(trading.cancel("S3"), "cancel");
        require(book.findRestingOrder("S3") == null, "cancel removed resting order");

        // Risk examples.
        SubmissionResult tooBig = trading.submit(trading.newLimitOrder(
                "BIG", "FIRM-A", Side.BUY, "99.0000", 2_000, TimeInForce.GTC));

        require(tooBig.riskDecision() == RiskDecision.REJECT, "max qty risk");

        killSwitch.activate();

        SubmissionResult killed = trading.submit(trading.newLimitOrder(
                "KILL", "FIRM-A", Side.BUY, "99.0000", 1, TimeInForce.GTC));

        require(killed.riskDecision() == RiskDecision.KILL, "kill switch");

        System.out.println();
        System.out.println("ALL 40-60 MIN INTERVIEW TESTS PASSED.");
    }

    private static void testFixedPoint() {
        long internal = FixedPointPrice.normalize("101.2500");

        require(internal == 1_012_500, "normalize");
        require(
                FixedPointPrice.denormalize(internal).equals("101.2500"),
                "denormalize");

        System.out.println(
                "Fixed point: 101.2500 -> "
                        + internal
                        + " -> "
                        + FixedPointPrice.denormalize(internal));
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }
}
