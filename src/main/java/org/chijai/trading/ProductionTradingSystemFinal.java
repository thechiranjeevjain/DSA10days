package org.chijai.trading;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * ProductionTradingSystemFinal
 *
 * Canonical full-reference implementation of the final trading LLD model.
 *
 * The hot path is intentionally simple:
 *
 *   decimal boundary
 *       -> OrderStore
 *       -> RiskEngine
 *       -> OrderBook
 *       -> MatchingEngine
 *       -> Fill
 *       -> Position + risk release
 *
 * Supporting modules from the final model are included afterwards:
 *
 *   - LRU cache
 *   - out-of-order sequence reconstruction
 *   - bounded dedup
 *   - resilient remote fan-out with bulkheads, timeout, retry,
 *     partial failure and in-process single-flight idempotency
 *
 * IMPORTANT:
 * The remote/backend modules are NOT part of the deterministic matching hot path.
 *
 * Java: 17+
 *
 * Compile:
 *   javac --release 17 ProductionTradingSystemFinal.java
 *
 * Run:
 *   java ProductionTradingSystemFinal
 */
public class ProductionTradingSystemFinal {

    // =====================================================================
    // 1. FIXED-POINT DECIMAL BOUNDARY
    // =====================================================================

    /**
     * Fixed-point rule:
     *
     * NORMALIZE only at ingress:
     *      "101.2500" -> 1_012_500L
     *
     * Internal book/risk code:
     *      scaled long only
     *
     * DENORMALIZE only at egress:
     *      1_012_500L -> "101.2500"
     *
     * No double is used for monetary values.
     *
     * Real exchanges normally derive scale/tick-size from instrument metadata.
     */
    static final class FixedPoint {

        static final int DECIMAL_PLACES = 4;

        private FixedPoint() {}

        static long normalize(String decimal) {
            return new BigDecimal(decimal)
                    .setScale(DECIMAL_PLACES, RoundingMode.UNNECESSARY)
                    .movePointRight(DECIMAL_PLACES)
                    .longValueExact();
        }

        static String denormalize(long scaledValue) {
            return BigDecimal
                    .valueOf(scaledValue, DECIMAL_PLACES)
                    .setScale(DECIMAL_PLACES, RoundingMode.UNNECESSARY)
                    .toPlainString();
        }

        static long notional(long normalizedPrice, long quantity) {
            if (normalizedPrice < 0 || quantity < 0) {
                throw new IllegalArgumentException("price/quantity must be >= 0");
            }
            return Math.multiplyExact(normalizedPrice, quantity);
        }

        /**
         * Weighted average with wide intermediate arithmetic.
         */
        static long weightedAverage(
                long currentAverage,
                long currentQuantity,
                long newPrice,
                long newQuantity) {

            long totalQuantity =
                    Math.addExact(currentQuantity, newQuantity);

            if (totalQuantity == 0) {
                return 0;
            }

            BigInteger oldValue =
                    BigInteger.valueOf(currentAverage)
                            .multiply(BigInteger.valueOf(currentQuantity));

            BigInteger newValue =
                    BigInteger.valueOf(newPrice)
                            .multiply(BigInteger.valueOf(newQuantity));

            return oldValue
                    .add(newValue)
                    .divide(BigInteger.valueOf(totalQuantity))
                    .longValueExact();
        }
    }

    // =====================================================================
    // 2. DOMAIN MODEL
    // =====================================================================

    enum Side {
        BUY, SELL
    }

    enum OrderType {
        LIMIT, MARKET
    }

    enum TimeInForce {
        GTC, DAY, IOC, FOK
    }

    enum OrderState {
        NEW,
        ACKNOWLEDGED,
        PARTIALLY_FILLED,
        FILLED,
        CANCELLED,
        REJECTED;

        boolean terminal() {
            return this == FILLED
                    || this == CANCELLED
                    || this == REJECTED;
        }
    }

    enum RiskDecision {
        PASS, BREACH, KILL
    }

    /**
     * Sequence is the deterministic tie-break.
     * Timestamp is useful operational context but is not assumed unique.
     */
    record OrderPriority(
            long arrivalTimeNanos,
            long sequence)
            implements Comparable<OrderPriority> {

        @Override
        public int compareTo(OrderPriority other) {
            int time =
                    Long.compare(
                            arrivalTimeNanos,
                            other.arrivalTimeNanos);

            return time != 0
                    ? time
                    : Long.compare(sequence, other.sequence);
        }
    }

    /**
     * Canonical order.
     *
     * Quantity invariant:
     *
     *      originalQuantity
     *          = filledQuantity
     *          + remainingQuantity
     *
     * riskPrice:
     * - LIMIT  -> usually the limit price
     * - MARKET -> caller supplies a trusted normalized reference price
     *             for pre-trade risk estimation
     */
    static final class Order {

        // Identity
        final String clientOrderId;
        String exchangeOrderId;

        // Routing / ownership
        final String instrumentId;
        final String firmId;

        // Economics
        final Side side;
        final OrderType orderType;
        final TimeInForce timeInForce;

        final long limitPrice;
        final long riskPrice;

        final long originalQuantity;

        // Priority
        final OrderPriority priority;

        // Mutable execution state
        long remainingQuantity;
        long filledQuantity;
        long averageFillPrice;

        OrderState state =
                OrderState.NEW;

        // Non-null only while resting.
        PriceLevel priceLevel;

        Order(
                String clientOrderId,
                String instrumentId,
                String firmId,
                Side side,
                OrderType orderType,
                long limitPrice,
                long riskPrice,
                long originalQuantity,
                OrderPriority priority,
                TimeInForce timeInForce) {

            this.clientOrderId =
                    Objects.requireNonNull(clientOrderId);

            this.instrumentId =
                    Objects.requireNonNull(instrumentId);

            this.firmId =
                    Objects.requireNonNull(firmId);

            this.side =
                    Objects.requireNonNull(side);

            this.orderType =
                    Objects.requireNonNull(orderType);

            this.timeInForce =
                    Objects.requireNonNull(timeInForce);

            this.priority =
                    Objects.requireNonNull(priority);

            if (originalQuantity <= 0) {
                throw new IllegalArgumentException("quantity must be > 0");
            }

            if (orderType == OrderType.LIMIT && limitPrice <= 0) {
                throw new IllegalArgumentException("limit price must be > 0");
            }

            if (riskPrice <= 0) {
                throw new IllegalArgumentException("risk price must be > 0");
            }

            this.limitPrice =
                    limitPrice;

            this.riskPrice =
                    riskPrice;

            this.originalQuantity =
                    originalQuantity;

            this.remainingQuantity =
                    originalQuantity;
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

        void acknowledge(String exchangeOrderId) {
            this.exchangeOrderId =
                    Objects.requireNonNull(exchangeOrderId);

            state =
                    OrderState.ACKNOWLEDGED;
        }

        /**
         * Apply one execution and preserve the quantity invariant.
         */
        void applyFill(
                long executionQuantity,
                long executionPrice) {

            if (executionQuantity <= 0
                    || executionQuantity > remainingQuantity) {

                throw new IllegalArgumentException(
                        "invalid execution quantity");
            }

            averageFillPrice =
                    FixedPoint.weightedAverage(
                            averageFillPrice,
                            filledQuantity,
                            executionPrice,
                            executionQuantity);

            filledQuantity =
                    Math.addExact(
                            filledQuantity,
                            executionQuantity);

            remainingQuantity -=
                    executionQuantity;

            state =
                    isDone()
                            ? OrderState.FILLED
                            : OrderState.PARTIALLY_FILLED;

            verifyQuantityInvariant();
        }

        void cancelRemainder() {
            if (!isDone()) {
                state =
                        OrderState.CANCELLED;
            }
        }

        void reject() {
            state =
                    OrderState.REJECTED;
        }

        private void verifyQuantityInvariant() {
            if (Math.addExact(
                    filledQuantity,
                    remainingQuantity)
                    != originalQuantity) {

                throw new IllegalStateException(
                        "quantity invariant broken for "
                                + clientOrderId);
            }
        }

        @Override
        public String toString() {
            String price =
                    isMarketOrder()
                            ? "MARKET"
                            : FixedPoint.denormalize(limitPrice);

            return "Order{id='%s', instrument='%s', side=%s, price=%s, original=%d, remaining=%d, filled=%d, avgPx=%s, state=%s}"
                    .formatted(
                            clientOrderId,
                            instrumentId,
                            side,
                            price,
                            originalQuantity,
                            remainingQuantity,
                            filledQuantity,
                            FixedPoint.denormalize(averageFillPrice),
                            state);
        }
    }

    // =====================================================================
    // 3. PRICE LEVEL
    // =====================================================================

    /**
     * Price priority is handled by OrderBook TreeMaps.
     * Time priority inside one price is FIFO.
     *
     * Interview implementation:
     * ArrayDeque gives O(1) head/tail operations.
     *
     * Production follow-up:
     * arbitrary ArrayDeque.remove(order) is O(orders-at-level);
     * an intrusive doubly linked list + ID->node index gives O(1) unlink.
     */
    static final class PriceLevel {

        final long price;

        final ArrayDeque<Order> orders =
                new ArrayDeque<>();

        long totalRemainingQuantity;

        PriceLevel(long price) {
            this.price =
                    price;
        }

        void addLast(Order order) {
            if (order.priceLevel != null) {
                throw new IllegalStateException(
                        "order already belongs to a price level");
            }

            orders.addLast(order);

            totalRemainingQuantity =
                    Math.addExact(
                            totalRemainingQuantity,
                            order.remainingQuantity);

            order.priceLevel =
                    this;
        }

        Order bestOrder() {
            return orders.peekFirst();
        }

        /**
         * Remove a fully consumed head BEFORE applyFill(),
         * because this subtracts pre-fill remaining quantity.
         */
        Order removeBestOrder() {
            Order order =
                    orders.pollFirst();

            if (order != null) {
                totalRemainingQuantity =
                        Math.subtractExact(
                                totalRemainingQuantity,
                                order.remainingQuantity);

                order.priceLevel =
                        null;
            }

            return order;
        }

        /**
         * Partial fill keeps the order at the head and preserves priority.
         */
        void reduceQuantity(
                long executionQuantity) {

            if (executionQuantity <= 0
                    || executionQuantity > totalRemainingQuantity) {

                throw new IllegalArgumentException(
                        "invalid level quantity reduction");
            }

            totalRemainingQuantity -=
                    executionQuantity;
        }

        boolean remove(
                Order order) {

            if (!orders.remove(order)) {
                return false;
            }

            totalRemainingQuantity =
                    Math.subtractExact(
                            totalRemainingQuantity,
                            order.remainingQuantity);

            order.priceLevel =
                    null;

            return true;
        }

        boolean isEmpty() {
            return orders.isEmpty();
        }

        int orderCount() {
            return orders.size();
        }

        @Override
        public String toString() {
            return "Level{price=%s, qty=%d, orders=%d}"
                    .formatted(
                            FixedPoint.denormalize(price),
                            totalRemainingQuantity,
                            orderCount());
        }
    }

    // =====================================================================
    // 4. ORDER BOOK
    // =====================================================================

    /**
     * One instrument's active resting liquidity.
     *
     * BIDS:
     * reverse TreeMap => firstEntry() is highest bid.
     *
     * ASKS:
     * natural TreeMap => firstEntry() is lowest ask.
     *
     * restingOrdersById is intentionally NOT the lifetime OrderStore.
     */
    static final class OrderBook {

        final String instrumentId;

        private final TreeMap<Long, PriceLevel> bidLevels =
                new TreeMap<>(
                        Comparator.reverseOrder());

        private final TreeMap<Long, PriceLevel> askLevels =
                new TreeMap<>();

        private final Map<String, Order> restingOrdersById =
                new HashMap<>();

        OrderBook(String instrumentId) {
            this.instrumentId =
                    Objects.requireNonNull(instrumentId);
        }

        void addRestingOrder(
                Order order) {

            validateInstrument(order);

            if (order.isMarketOrder()) {
                throw new IllegalArgumentException(
                        "market order cannot rest");
            }

            if (order.isDone()
                    || order.isTerminal()) {

                throw new IllegalArgumentException(
                        "done/terminal order cannot rest");
            }

            if (restingOrdersById.containsKey(
                    order.clientOrderId)) {

                throw new IllegalArgumentException(
                        "order already resting: "
                                + order.clientOrderId);
            }

            TreeMap<Long, PriceLevel> side =
                    levelsFor(order.side);

            PriceLevel level =
                    side.computeIfAbsent(
                            order.limitPrice,
                            PriceLevel::new);

            level.addLast(order);

            restingOrdersById.put(
                    order.clientOrderId,
                    order);
        }

        /**
         * Cancel one resting order.
         *
         * With ArrayDeque this is O(orders at that price level).
         */
        boolean cancel(
                String clientOrderId) {

            Order order =
                    restingOrdersById.get(
                            clientOrderId);

            if (order == null
                    || order.priceLevel == null) {

                return false;
            }

            PriceLevel level =
                    order.priceLevel;

            if (!level.remove(order)) {
                return false;
            }

            restingOrdersById.remove(
                    clientOrderId);

            removeLevelIfEmpty(
                    order.side,
                    level);

            order.cancelRemainder();
            return true;
        }

        Long bestBid() {
            return bidLevels.isEmpty()
                    ? null
                    : bidLevels.firstKey();
        }

        Long bestAsk() {
            return askLevels.isEmpty()
                    ? null
                    : askLevels.firstKey();
        }

        PriceLevel bestOppositeLevel(
                Side incomingSide) {

            NavigableMap<Long, PriceLevel> opposite =
                    oppositeLevels(incomingSide);

            return opposite.isEmpty()
                    ? null
                    : opposite.firstEntry()
                            .getValue();
        }

        NavigableMap<Long, PriceLevel> oppositeLevels(
                Side incomingSide) {

            return incomingSide == Side.BUY
                    ? askLevels
                    : bidLevels;
        }

        Order findRestingOrder(
                String clientOrderId) {

            return restingOrdersById.get(
                    clientOrderId);
        }

        /**
         * MatchingEngine already removed this order from PriceLevel.
         * Update only the resting-order index here.
         */
        void forgetFullyFilledRestingOrder(
                Order order,
                PriceLevel oldLevel) {

            restingOrdersById.remove(
                    order.clientOrderId);

            removeLevelIfEmpty(
                    order.side,
                    oldLevel);
        }

        int restingOrderCount() {
            return restingOrdersById.size();
        }

        private TreeMap<Long, PriceLevel>
        levelsFor(Side side) {

            return side == Side.BUY
                    ? bidLevels
                    : askLevels;
        }

        private void removeLevelIfEmpty(
                Side side,
                PriceLevel level) {

            if (level.isEmpty()) {
                levelsFor(side).remove(
                        level.price);
            }
        }

        private void validateInstrument(
                Order order) {

            if (!instrumentId.equals(
                    order.instrumentId)) {

                throw new IllegalArgumentException(
                        "order belongs to "
                                + order.instrumentId
                                + ", book is "
                                + instrumentId);
            }
        }

        String snapshot() {
            return "OrderBook{%s, bestBid=%s, bestAsk=%s, resting=%d}"
                    .formatted(
                            instrumentId,
                            bestBid() == null
                                    ? "-"
                                    : FixedPoint.denormalize(bestBid()),
                            bestAsk() == null
                                    ? "-"
                                    : FixedPoint.denormalize(bestAsk()),
                            restingOrderCount());
        }
    }

    static final class OrderBookRegistry {

        private final Map<String, OrderBook> books =
                new HashMap<>();

        OrderBook getOrCreate(
                String instrumentId) {

            return books.computeIfAbsent(
                    instrumentId,
                    OrderBook::new);
        }

        OrderBook get(
                String instrumentId) {

            return books.get(
                    instrumentId);
        }
    }

    // =====================================================================
    // 5. AUTHORITATIVE ORDER STORE
    // =====================================================================

    /**
     * Lifetime identity/lifecycle lookup.
     *
     * This is separate from OrderBook.restingOrdersById.
     *
     * A FILLED/CANCELLED order can remain in OrderStore
     * while it no longer exists in the book.
     */
    static final class OrderStore {

        private final Map<String, Order> byClientOrderId =
                new HashMap<>();

        private final Map<String, Order> byExchangeOrderId =
                new HashMap<>();

        void add(
                Order order) {

            if (byClientOrderId.putIfAbsent(
                    order.clientOrderId,
                    order) != null) {

                throw new IllegalArgumentException(
                        "duplicate clientOrderId: "
                                + order.clientOrderId);
            }
        }

        void linkExchangeOrderId(
                Order order,
                String exchangeOrderId) {

            Objects.requireNonNull(exchangeOrderId);

            Order previous =
                    byExchangeOrderId.putIfAbsent(
                            exchangeOrderId,
                            order);

            if (previous != null
                    && previous != order) {

                throw new IllegalArgumentException(
                        "duplicate exchangeOrderId: "
                                + exchangeOrderId);
            }

            order.acknowledge(
                    exchangeOrderId);
        }

        Order byClientOrderId(
                String clientOrderId) {

            return byClientOrderId.get(
                    clientOrderId);
        }

        Order byExchangeOrderId(
                String exchangeOrderId) {

            return byExchangeOrderId.get(
                    exchangeOrderId);
        }

        boolean containsClientOrderId(
                String clientOrderId) {

            return byClientOrderId.containsKey(
                    clientOrderId);
        }

        void remove(
                Order order) {

            byClientOrderId.remove(
                    order.clientOrderId,
                    order);

            if (order.exchangeOrderId != null) {
                byExchangeOrderId.remove(
                        order.exchangeOrderId,
                        order);
            }
        }

        int size() {
            return byClientOrderId.size();
        }
    }

    // =====================================================================
    // 6. MATCHING ENGINE
    // =====================================================================

    record Fill(
            String aggressiveOrderId,
            String passiveOrderId,
            String instrumentId,
            long executionPrice,
            long executionQuantity) {}

    record MatchResult(
            List<Fill> fills,
            List<String> selfMatchCancelledPassiveIds) {

        MatchResult {
            fills =
                    List.copyOf(fills);

            selfMatchCancelledPassiveIds =
                    List.copyOf(selfMatchCancelledPassiveIds);
        }
    }

    /**
     * Matching rules in this reference:
     *
     * BUY crosses when:
     *      market OR buy.limit >= bestAsk
     *
     * SELL crosses when:
     *      market OR sell.limit <= bestBid
     *
     * Execution price:
     *      passive/resting order price.
     *
     * Self-match policy:
     *      cancel passive self-order.
     */
    static final class MatchingEngine {

        MatchResult match(
                Order incoming,
                OrderBook book) {

            List<Fill> fills =
                    new ArrayList<>();

            List<String> selfMatchCancelled =
                    new ArrayList<>();

            /*
             * FOK = all or none.
             * Validate before mutating the book.
             */
            if (incoming.timeInForce
                    == TimeInForce.FOK
                    && !canFullyFill(
                            incoming,
                            book)) {

                incoming.cancelRemainder();

                return new MatchResult(
                        fills,
                        selfMatchCancelled);
            }

            while (incoming.remainingQuantity > 0) {

                PriceLevel bestLevel =
                        book.bestOppositeLevel(
                                incoming.side);

                if (bestLevel == null
                        || !crosses(
                                incoming,
                                bestLevel.price)) {

                    break;
                }

                Order passive =
                        bestLevel.bestOrder();

                if (sameFirm(
                        incoming,
                        passive)) {

                    if (!book.cancel(
                            passive.clientOrderId)) {

                        throw new IllegalStateException(
                                "failed SMP cancellation");
                    }

                    selfMatchCancelled.add(
                            passive.clientOrderId);

                    continue;
                }

                long executionQuantity =
                        Math.min(
                                incoming.remainingQuantity,
                                passive.remainingQuantity);

                long executionPrice =
                        bestLevel.price;

                boolean passiveFullyFilled =
                        executionQuantity
                                == passive.remainingQuantity;

                if (passiveFullyFilled) {

                    /*
                     * Physical removal happens exactly once.
                     *
                     * Remove BEFORE fill because PriceLevel subtracts
                     * current pre-fill remainingQuantity.
                     */
                    Order removed =
                            bestLevel.removeBestOrder();

                    if (removed != passive) {
                        throw new IllegalStateException(
                                "price-time queue corrupted");
                    }

                    book.forgetFullyFilledRestingOrder(
                            passive,
                            bestLevel);

                } else {

                    /*
                     * Passive stays at the head:
                     * same price-time priority.
                     */
                    bestLevel.reduceQuantity(
                            executionQuantity);
                }

                passive.applyFill(
                        executionQuantity,
                        executionPrice);

                incoming.applyFill(
                        executionQuantity,
                        executionPrice);

                fills.add(
                        new Fill(
                                incoming.clientOrderId,
                                passive.clientOrderId,
                                incoming.instrumentId,
                                executionPrice,
                                executionQuantity));
            }

            applyRemainderPolicy(
                    incoming,
                    book);

            return new MatchResult(
                    fills,
                    selfMatchCancelled);
        }

        private boolean crosses(
                Order incoming,
                long passivePrice) {

            if (incoming.isMarketOrder()) {
                return true;
            }

            return incoming.side == Side.BUY
                    ? incoming.limitPrice >= passivePrice
                    : incoming.limitPrice <= passivePrice;
        }

        private boolean sameFirm(
                Order first,
                Order second) {

            return Objects.equals(
                    first.firmId,
                    second.firmId);
        }

        private void applyRemainderPolicy(
                Order incoming,
                OrderBook book) {

            if (incoming.isDone()) {
                return;
            }

            if (incoming.isMarketOrder()
                    || incoming.timeInForce
                    == TimeInForce.IOC) {

                incoming.cancelRemainder();
                return;
            }

            /*
             * If this happens, the FOK pre-check and matching
             * eligibility rules disagree.
             */
            if (incoming.timeInForce
                    == TimeInForce.FOK) {

                throw new IllegalStateException(
                        "FOK partially executed");
            }

            /*
             * GTC / DAY limit remainder rests.
             */
            book.addRestingOrder(
                    incoming);
        }

        /**
         * Interview-friendly FOK pre-check:
         * scan only crossing price levels and count eligible quantity.
         */
        private boolean canFullyFill(
                Order incoming,
                OrderBook book) {

            long needed =
                    incoming.remainingQuantity;

            for (PriceLevel level
                    : book.oppositeLevels(
                            incoming.side)
                    .values()) {

                if (!crosses(
                        incoming,
                        level.price)) {

                    break;
                }

                for (Order passive
                        : level.orders) {

                    if (sameFirm(
                            incoming,
                            passive)) {

                        continue;
                    }

                    needed -=
                            passive.remainingQuantity;

                    if (needed <= 0) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    // =====================================================================
    // 7. TRANSACTIONAL PRE-TRADE RISK
    // =====================================================================

    /**
     * Open-order exposure reservation.
     *
     * notional uses the same fixed-point scale as price:
     * normalizedPrice * quantity.
     */
    record Exposure(
            String riskGroupId,
            long quantity,
            long notional) {

        Exposure {
            Objects.requireNonNull(
                    riskGroupId);

            if (quantity < 0
                    || notional < 0) {

                throw new IllegalArgumentException(
                        "exposure must be >= 0");
            }
        }

        static Exposure forOrder(
                Order order,
                long quantity) {

            return new Exposure(
                    order.firmId,
                    quantity,
                    FixedPoint.notional(
                            order.riskPrice,
                            quantity));
        }
    }

    record RiskLimit(
            long maxOpenQuantity,
            long maxOpenNotional) {

        RiskLimit {
            if (maxOpenQuantity < 0
                    || maxOpenNotional < 0) {

                throw new IllegalArgumentException(
                        "risk limits must be >= 0");
            }
        }
    }

    /**
     * Mutable group state.
     *
     * begin/commit/rollback makes the transactional invariant explicit:
     *
     * reject/exception
     * -> exact pre-attempt state restored.
     */
    static final class ExposureState {

        long openQuantity;
        long openNotional;

        private long savedOpenQuantity;
        private long savedOpenNotional;

        void begin() {
            savedOpenQuantity =
                    openQuantity;

            savedOpenNotional =
                    openNotional;
        }

        void add(
                Exposure exposure) {

            openQuantity =
                    Math.addExact(
                            openQuantity,
                            exposure.quantity());

            openNotional =
                    Math.addExact(
                            openNotional,
                            exposure.notional());
        }

        void commit() {
            // Saved snapshot is logically discarded.
        }

        void rollback() {
            openQuantity =
                    savedOpenQuantity;

            openNotional =
                    savedOpenNotional;
        }

        void release(
                Exposure exposure) {

            if (exposure.quantity()
                    > openQuantity
                    || exposure.notional()
                    > openNotional) {

                throw new IllegalStateException(
                        "risk release exceeds reserved exposure");
            }

            openQuantity -=
                    exposure.quantity();

            openNotional -=
                    exposure.notional();
        }

        ExposureState copy() {
            ExposureState copy =
                    new ExposureState();

            copy.openQuantity =
                    openQuantity;

            copy.openNotional =
                    openNotional;

            return copy;
        }

        @Override
        public String toString() {
            return "ExposureState{qty=%d, notional=%s}"
                    .formatted(
                            openQuantity,
                            FixedPoint.denormalize(
                                    openNotional));
        }
    }

    /**
     * Checks inspect the PROVISIONAL state after the new exposure was applied.
     * They do not mutate exposure themselves.
     *
     * That avoids double-accounting across multiple checks.
     */
    interface RiskCheck {
        RiskDecision check(
                ExposureState provisionalState,
                RiskLimit limit);
    }

    static final class MaxOpenQuantityCheck
            implements RiskCheck {

        @Override
        public RiskDecision check(
                ExposureState state,
                RiskLimit limit) {

            return state.openQuantity
                    <= limit.maxOpenQuantity()
                    ? RiskDecision.PASS
                    : RiskDecision.BREACH;
        }
    }

    static final class MaxOpenNotionalCheck
            implements RiskCheck {

        @Override
        public RiskDecision check(
                ExposureState state,
                RiskLimit limit) {

            return state.openNotional
                    <= limit.maxOpenNotional()
                    ? RiskDecision.PASS
                    : RiskDecision.BREACH;
        }
    }

    static final class KillSwitchCheck
            implements RiskCheck {

        private volatile boolean active;

        void activate() {
            active =
                    true;
        }

        void deactivate() {
            active =
                    false;
        }

        @Override
        public RiskDecision check(
                ExposureState state,
                RiskLimit limit) {

            return active
                    ? RiskDecision.KILL
                    : RiskDecision.PASS;
        }
    }

    static final class ExposureGroup {

        private final List<RiskCheck> checks;
        private final RiskLimit limit;
        private final ExposureState state =
                new ExposureState();

        ExposureGroup(
                List<RiskCheck> checks,
                RiskLimit limit) {

            this.checks =
                    List.copyOf(checks);

            this.limit =
                    Objects.requireNonNull(limit);
        }

        synchronized RiskDecision reserve(
                Exposure exposure) {

            state.begin();

            try {
                state.add(
                        exposure);

                for (RiskCheck check
                        : checks) {

                    RiskDecision decision =
                            check.check(
                                    state,
                                    limit);

                    if (decision
                            != RiskDecision.PASS) {

                        state.rollback();
                        return decision;
                    }
                }

                state.commit();
                return RiskDecision.PASS;

            } catch (RuntimeException error) {

                state.rollback();
                throw error;
            }
        }

        synchronized void release(
                Exposure exposure) {

            state.release(
                    exposure);
        }

        synchronized ExposureState snapshot() {
            return state.copy();
        }
    }

    static final class RiskEngine {

        private final Map<String, ExposureGroup> groups =
                new HashMap<>();

        void registerGroup(
                String riskGroupId,
                ExposureGroup group) {

            groups.put(
                    Objects.requireNonNull(riskGroupId),
                    Objects.requireNonNull(group));
        }

        RiskDecision reserve(
                Exposure exposure) {

            return group(
                    exposure.riskGroupId())
                    .reserve(exposure);
        }

        void release(
                Exposure exposure) {

            group(
                    exposure.riskGroupId())
                    .release(exposure);
        }

        ExposureState snapshot(
                String riskGroupId) {

            return group(
                    riskGroupId)
                    .snapshot();
        }

        private ExposureGroup group(
                String riskGroupId) {

            ExposureGroup group =
                    groups.get(
                            riskGroupId);

            if (group == null) {
                throw new IllegalStateException(
                        "no risk group registered: "
                                + riskGroupId);
            }

            return group;
        }
    }

    // =====================================================================
    // 8. POSITION TRACKER
    // =====================================================================

    record PositionKey(
            String firmId,
            String instrumentId) {}

    /**
     * Minimal signed execution position:
     *
     * BUY  -> +qty
     * SELL -> -qty
     */
    static final class PositionTracker {

        private final Map<PositionKey, Long> netPosition =
                new HashMap<>();

        void applyExecution(
                Order order,
                long executionQuantity) {

            long signedQuantity =
                    order.side == Side.BUY
                            ? executionQuantity
                            : -executionQuantity;

            netPosition.merge(
                    new PositionKey(
                            order.firmId,
                            order.instrumentId),
                    signedQuantity,
                    Long::sum);
        }

        long position(
                String firmId,
                String instrumentId) {

            return netPosition.getOrDefault(
                    new PositionKey(
                            firmId,
                            instrumentId),
                    0L);
        }
    }

    // =====================================================================
    // 9. TRADING APPLICATION SERVICE
    // =====================================================================

    record SubmissionResult(
            RiskDecision riskDecision,
            Order order,
            List<Fill> fills,
            List<String> selfMatchCancelledPassiveIds) {

        SubmissionResult {
            fills =
                    List.copyOf(fills);

            selfMatchCancelledPassiveIds =
                    List.copyOf(
                            selfMatchCancelledPassiveIds);
        }

        boolean accepted() {
            return riskDecision
                    == RiskDecision.PASS;
        }
    }

    /**
     * Thin orchestration layer:
     *
     * identity
     * -> risk reserve
     * -> ACK
     * -> match/rest
     * -> risk release on fills/cancels
     * -> position updates
     */
    static final class TradingService {

        private final OrderStore orderStore =
                new OrderStore();

        private final OrderBookRegistry orderBooks =
                new OrderBookRegistry();

        private final RiskEngine riskEngine;
        private final PositionTracker positions =
                new PositionTracker();

        private final MatchingEngine matchingEngine =
                new MatchingEngine();

        private final AtomicLong nextArrivalSequence =
                new AtomicLong(1L);

        private final AtomicLong nextExchangeOrderId =
                new AtomicLong(1_000_000L);

        TradingService(
                RiskEngine riskEngine) {

            this.riskEngine =
                    Objects.requireNonNull(riskEngine);
        }

        Order newLimitOrder(
                String clientOrderId,
                String instrumentId,
                String firmId,
                Side side,
                String decimalLimitPrice,
                long quantity,
                TimeInForce timeInForce) {

            long normalizedPrice =
                    FixedPoint.normalize(
                            decimalLimitPrice);

            return new Order(
                    clientOrderId,
                    instrumentId,
                    firmId,
                    side,
                    OrderType.LIMIT,
                    normalizedPrice,
                    normalizedPrice,
                    quantity,
                    newPriority(),
                    timeInForce);
        }

        Order newMarketOrder(
                String clientOrderId,
                String instrumentId,
                String firmId,
                Side side,
                long quantity,
                String decimalRiskReferencePrice,
                TimeInForce timeInForce) {

            return new Order(
                    clientOrderId,
                    instrumentId,
                    firmId,
                    side,
                    OrderType.MARKET,
                    0L,
                    FixedPoint.normalize(
                            decimalRiskReferencePrice),
                    quantity,
                    newPriority(),
                    timeInForce);
        }

        SubmissionResult submit(
                Order order) {

            if (orderStore.containsClientOrderId(
                    order.clientOrderId)) {

                order.reject();

                return new SubmissionResult(
                        RiskDecision.BREACH,
                        order,
                        List.of(),
                        List.of());
            }

            /*
             * Store first so lifecycle/rejected orders remain queryable.
             */
            orderStore.add(
                    order);

            Exposure fullReservation =
                    Exposure.forOrder(
                            order,
                            order.originalQuantity);

            RiskDecision riskDecision =
                    riskEngine.reserve(
                            fullReservation);

            if (riskDecision
                    != RiskDecision.PASS) {

                order.reject();

                return new SubmissionResult(
                        riskDecision,
                        order,
                        List.of(),
                        List.of());
            }

            orderStore.linkExchangeOrderId(
                    order,
                    String.valueOf(
                            nextExchangeOrderId
                                    .getAndIncrement()));

            OrderBook book =
                    orderBooks.getOrCreate(
                            order.instrumentId);

            MatchResult matchResult =
                    matchingEngine.match(
                            order,
                            book);

            /*
             * A fill removes that executed quantity from BOTH orders'
             * open-order exposure and updates BOTH positions.
             */
            for (Fill fill
                    : matchResult.fills()) {

                Order aggressor =
                        requireOrder(
                                fill.aggressiveOrderId());

                Order passive =
                        requireOrder(
                                fill.passiveOrderId());

                riskEngine.release(
                        Exposure.forOrder(
                                aggressor,
                                fill.executionQuantity()));

                riskEngine.release(
                        Exposure.forOrder(
                                passive,
                                fill.executionQuantity()));

                positions.applyExecution(
                        aggressor,
                        fill.executionQuantity());

                positions.applyExecution(
                        passive,
                        fill.executionQuantity());
            }

            /*
             * SMP cancelled passive orders leave the book with their
             * remaining open exposure released.
             */
            for (String passiveId
                    : matchResult
                    .selfMatchCancelledPassiveIds()) {

                Order passive =
                        requireOrder(
                                passiveId);

                if (passive.remainingQuantity > 0) {
                    riskEngine.release(
                            Exposure.forOrder(
                                    passive,
                                    passive.remainingQuantity));
                }
            }

            /*
             * IOC / MARKET / failed FOK remainder does not rest.
             * Release whatever reserved quantity remains unexecuted.
             */
            if (order.state
                    == OrderState.CANCELLED
                    && order.remainingQuantity > 0
                    && order.priceLevel == null) {

                riskEngine.release(
                        Exposure.forOrder(
                                order,
                                order.remainingQuantity));
            }

            return new SubmissionResult(
                    RiskDecision.PASS,
                    order,
                    matchResult.fills(),
                    matchResult
                            .selfMatchCancelledPassiveIds());
        }

        boolean cancel(
                String clientOrderId) {

            Order order =
                    orderStore.byClientOrderId(
                            clientOrderId);

            if (order == null
                    || order.priceLevel == null) {

                return false;
            }

            long quantityToRelease =
                    order.remainingQuantity;

            OrderBook book =
                    orderBooks.get(
                            order.instrumentId);

            if (book == null
                    || !book.cancel(
                            clientOrderId)) {

                return false;
            }

            riskEngine.release(
                    Exposure.forOrder(
                            order,
                            quantityToRelease));

            return true;
        }

        Order order(
                String clientOrderId) {

            return orderStore.byClientOrderId(
                    clientOrderId);
        }

        OrderBook book(
                String instrumentId) {

            return orderBooks.get(
                    instrumentId);
        }

        long position(
                String firmId,
                String instrumentId) {

            return positions.position(
                    firmId,
                    instrumentId);
        }

        private Order requireOrder(
                String clientOrderId) {

            Order order =
                    orderStore.byClientOrderId(
                            clientOrderId);

            if (order == null) {
                throw new IllegalStateException(
                        "unknown order: "
                                + clientOrderId);
            }

            return order;
        }

        private OrderPriority newPriority() {
            return new OrderPriority(
                    System.nanoTime(),
                    nextArrivalSequence
                            .getAndIncrement());
        }
    }

    // =====================================================================
    // 10. GENERIC LRU CACHE
    // =====================================================================

    static final class LruNode<K, V> {

        K key;
        V value;

        LruNode<K, V> previous;
        LruNode<K, V> next;

        LruNode() {}

        LruNode(
                K key,
                V value) {

            this.key =
                    key;

            this.value =
                    value;
        }
    }

    /**
     * HashMap + doubly linked list.
     *
     * head.next = MRU
     * tail.previous = LRU
     */
    static final class LruCache<K, V> {

        private final int capacity;

        private final Map<K, LruNode<K, V>> nodes =
                new HashMap<>();

        private final LruNode<K, V> head =
                new LruNode<>();

        private final LruNode<K, V> tail =
                new LruNode<>();

        LruCache(int capacity) {

            if (capacity <= 0) {
                throw new IllegalArgumentException(
                        "capacity must be > 0");
            }

            this.capacity =
                    capacity;

            head.next =
                    tail;

            tail.previous =
                    head;
        }

        V get(K key) {

            LruNode<K, V> node =
                    nodes.get(
                            key);

            if (node == null) {
                return null;
            }

            moveToMostRecent(
                    node);

            return node.value;
        }

        void put(
                K key,
                V value) {

            LruNode<K, V> existing =
                    nodes.get(
                            key);

            if (existing != null) {
                existing.value =
                        value;

                moveToMostRecent(
                        existing);

                return;
            }

            LruNode<K, V> node =
                    new LruNode<>(
                            key,
                            value);

            nodes.put(
                    key,
                    node);

            addAfterHead(
                    node);

            if (nodes.size()
                    > capacity) {

                LruNode<K, V> leastRecent =
                        removeLeastRecent();

                nodes.remove(
                        leastRecent.key);
            }
        }

        boolean remove(
                K key) {

            LruNode<K, V> node =
                    nodes.remove(
                            key);

            if (node == null) {
                return false;
            }

            unlink(node);
            return true;
        }

        V computeIfAbsent(
                K key,
                Function<K, V> factory) {

            V existing =
                    get(key);

            if (existing != null) {
                return existing;
            }

            V value =
                    factory.apply(
                            key);

            put(
                    key,
                    value);

            return value;
        }

        int size() {
            return nodes.size();
        }

        private void addAfterHead(
                LruNode<K, V> node) {

            node.previous =
                    head;

            node.next =
                    head.next;

            head.next.previous =
                    node;

            head.next =
                    node;
        }

        private void unlink(
                LruNode<K, V> node) {

            node.previous.next =
                    node.next;

            node.next.previous =
                    node.previous;
        }

        private void moveToMostRecent(
                LruNode<K, V> node) {

            unlink(node);
            addAfterHead(node);
        }

        private LruNode<K, V>
        removeLeastRecent() {

            LruNode<K, V> leastRecent =
                    tail.previous;

            if (leastRecent == head) {
                throw new IllegalStateException(
                        "cache is empty");
            }

            unlink(
                    leastRecent);

            return leastRecent;
        }
    }

    // =====================================================================
    // 11. OUT-OF-ORDER SEQUENCE RECONSTRUCTION
    // =====================================================================

    /**
     * Invariant:
     *
     * everything < nextExpected has already been emitted exactly once.
     *
     * Future messages are parked until the hole closes.
     */
    static final class SequenceReorderBuffer<T> {

        private long nextExpected;

        private final TreeMap<Long, T> pending =
                new TreeMap<>();

        SequenceReorderBuffer(
                long firstExpectedSequence) {

            nextExpected =
                    firstExpectedSequence;
        }

        List<T> receive(
                long sequence,
                T message) {

            List<T> ready =
                    new ArrayList<>();

            if (sequence < nextExpected) {
                return ready;
            }

            pending.putIfAbsent(
                    sequence,
                    message);

            while (true) {

                T next =
                        pending.remove(
                                nextExpected);

                if (next == null) {
                    break;
                }

                ready.add(
                        next);

                nextExpected++;
            }

            return ready;
        }

        long nextExpected() {
            return nextExpected;
        }

        int bufferedCount() {
            return pending.size();
        }
    }

    // =====================================================================
    // 12. BOUNDED DEDUP
    // =====================================================================

    /**
     * Bounded insertion-order remembered set.
     *
     * It is exact only while an ID remains retained.
     * After eviction, an old ID can be accepted again.
     */
    static final class BoundedDedupSet<K> {

        private final Map<K, Boolean> remembered;

        BoundedDedupSet(
                int capacity) {

            if (capacity <= 0) {
                throw new IllegalArgumentException(
                        "capacity must be > 0");
            }

            remembered =
                    new LinkedHashMap<>(
                            capacity,
                            0.75f,
                            false) {

                        @Override
                        protected boolean removeEldestEntry(
                                Map.Entry<K, Boolean> eldest) {

                            return size()
                                    > capacity;
                        }
                    };
        }

        /**
         * true  -> first time while retained
         * false -> duplicate still remembered
         */
        boolean firstTime(
                K key) {

            if (remembered.containsKey(
                    key)) {

                return false;
            }

            remembered.put(
                    key,
                    Boolean.TRUE);

            return true;
        }

        int size() {
            return remembered.size();
        }
    }

    // =====================================================================
    // 13. RESILIENT REMOTE FAN-OUT
    //     (SEPARATE BACKEND MODULE, NOT MATCHING HOT PATH)
    // =====================================================================

    record AggregationRequest(
            String idempotencyKey,
            String payload) {}

    record ServiceAResult(
            String value) {}

    record ServiceBResult(
            String value) {

        static final ServiceBResult EMPTY =
                new ServiceBResult("");
    }

    record ServiceCResult(
            String value) {

        static final ServiceCResult EMPTY =
                new ServiceCResult("");
    }

    record AggregatedResult(
            String idempotencyKey,
            String mergedValue,
            boolean partial) {}

    @FunctionalInterface
    interface RemoteService<T> {
        T call(
                AggregationRequest request)
                throws Exception;
    }

    static final class TransientRemoteException
            extends RuntimeException {

        TransientRemoteException(
                String message) {

            super(message);
        }
    }

    record RetryPolicy(
            int maxAttempts,
            long initialBackoffMillis,
            long maxBackoffMillis) {

        RetryPolicy {
            if (maxAttempts <= 0
                    || initialBackoffMillis < 0
                    || maxBackoffMillis < initialBackoffMillis) {

                throw new IllegalArgumentException(
                        "invalid retry policy");
            }
        }
    }

    /**
     * In-process single-flight:
     * concurrent duplicate keys join one CompletableFuture.
     *
     * Production distributed idempotency requires a durable/shared
     * atomic reservation or transaction.
     */
    static final class SingleFlightRegistry<V> {

        private final ConcurrentHashMap<
                String,
                CompletableFuture<V>>
                inFlightOrCompleted =
                new ConcurrentHashMap<>();

        Reservation<V> reserve(
                String key) {

            CompletableFuture<V> mine =
                    new CompletableFuture<>();

            CompletableFuture<V> existing =
                    inFlightOrCompleted.putIfAbsent(
                            key,
                            mine);

            return existing == null
                    ? new Reservation<>(
                            true,
                            mine)
                    : new Reservation<>(
                            false,
                            existing);
        }

        void removeFailed(
                String key,
                CompletableFuture<V> future) {

            inFlightOrCompleted.remove(
                    key,
                    future);
        }

        record Reservation<V>(
                boolean owner,
                CompletableFuture<V> future) {}
    }

    /**
     * Demonstrates:
     *
     * - per-dependency bulkheads
     * - concurrent fan-out
     * - per-service timeout
     * - total deadline
     * - retry only transient failures
     * - exponential backoff + jitter
     * - optional-service degradation
     * - in-process idempotent single-flight
     *
     * completeOnTimeout/orTimeout does not guarantee the underlying
     * network call itself stops; real clients should use native deadlines.
     */
    static final class ResilientAggregator
            implements AutoCloseable {

        private static final long PER_SERVICE_TIMEOUT_MS =
                200L;

        private static final long TOTAL_TIMEOUT_MS =
                500L;

        private final ExecutorService serviceAPool =
                boundedPool(
                        "service-a",
                        4,
                        32);

        private final ExecutorService serviceBPool =
                boundedPool(
                        "service-b",
                        4,
                        32);

        private final ExecutorService serviceCPool =
                boundedPool(
                        "service-c",
                        2,
                        16);

        private final RemoteService<ServiceAResult> serviceA;
        private final RemoteService<ServiceBResult> serviceB;
        private final RemoteService<ServiceCResult> serviceC;

        private final RetryPolicy retryPolicy =
                new RetryPolicy(
                        3,
                        50L,
                        200L);

        private final SingleFlightRegistry<AggregatedResult>
                idempotency =
                new SingleFlightRegistry<>();

        /**
         * Stand-in for durable persistence in this runnable demo.
         */
        private final ConcurrentHashMap<
                String,
                AggregatedResult>
                persisted =
                new ConcurrentHashMap<>();

        private final AtomicInteger persistCount =
                new AtomicInteger();

        ResilientAggregator(
                RemoteService<ServiceAResult> serviceA,
                RemoteService<ServiceBResult> serviceB,
                RemoteService<ServiceCResult> serviceC) {

            this.serviceA =
                    Objects.requireNonNull(serviceA);

            this.serviceB =
                    Objects.requireNonNull(serviceB);

            this.serviceC =
                    Objects.requireNonNull(serviceC);
        }

        AggregatedResult aggregate(
                AggregationRequest request) {

            SingleFlightRegistry.Reservation<AggregatedResult>
                    reservation =
                    idempotency.reserve(
                            request.idempotencyKey());

            if (!reservation.owner()) {
                return reservation.future()
                        .join();
            }

            CompletableFuture<AggregatedResult> shared =
                    reservation.future();

            try {
                AggregatedResult result =
                        compute(
                                request);

                persist(
                        result);

                shared.complete(
                        result);

                return result;

            } catch (RuntimeException error) {

                shared.completeExceptionally(
                        error);

                idempotency.removeFailed(
                        request.idempotencyKey(),
                        shared);

                throw error;
            }
        }

        private AggregatedResult compute(
                AggregationRequest request) {

            long overallDeadlineNanos =
                    System.nanoTime()
                            + TimeUnit.MILLISECONDS
                            .toNanos(
                                    TOTAL_TIMEOUT_MS);

            CompletableFuture<ServiceAResult> a =
                    callAsync(
                            serviceA,
                            request,
                            serviceAPool,
                            overallDeadlineNanos);

            CompletableFuture<ServiceBResult> b =
                    callAsync(
                            serviceB,
                            request,
                            serviceBPool,
                            overallDeadlineNanos);

            CompletableFuture<ServiceCResult> c =
                    callAsync(
                            serviceC,
                            request,
                            serviceCPool,
                            overallDeadlineNanos);

            try {
                CompletableFuture
                        .allOf(a, b, c)
                        .get(
                                Math.max(
                                        1L,
                                        remainingMillis(
                                                overallDeadlineNanos)),
                                TimeUnit.MILLISECONDS);

            } catch (TimeoutException ignored) {
                // Continue with whatever completed.

            } catch (InterruptedException interrupted) {

                Thread.currentThread()
                        .interrupt();

                throw new RuntimeException(
                        "aggregation interrupted",
                        interrupted);

            } catch (ExecutionException unexpected) {

                throw new RuntimeException(
                        "unexpected aggregation failure",
                        unexpected.getCause());
            }

            ServiceAResult requiredA =
                    completedValueOrNull(
                            a);

            ServiceBResult optionalB =
                    completedValueOrNull(
                            b);

            ServiceCResult optionalC =
                    completedValueOrNull(
                            c);

            if (requiredA == null) {
                throw new RuntimeException(
                        "required Service A unavailable");
            }

            boolean partial =
                    optionalB == null
                            || optionalC == null;

            ServiceBResult safeB =
                    optionalB == null
                            ? ServiceBResult.EMPTY
                            : optionalB;

            ServiceCResult safeC =
                    optionalC == null
                            ? ServiceCResult.EMPTY
                            : optionalC;

            return new AggregatedResult(
                    request.idempotencyKey(),
                    requiredA.value()
                            + "|"
                            + safeB.value()
                            + "|"
                            + safeC.value(),
                    partial);
        }

        private <T> CompletableFuture<T> callAsync(
                RemoteService<T> service,
                AggregationRequest request,
                Executor executor,
                long overallDeadlineNanos) {

            long serviceDeadlineNanos =
                    Math.min(
                            overallDeadlineNanos,
                            System.nanoTime()
                                    + TimeUnit.MILLISECONDS
                                    .toNanos(
                                            PER_SERVICE_TIMEOUT_MS));

            return CompletableFuture
                    .supplyAsync(
                            () -> callWithRetry(
                                    service,
                                    request,
                                    serviceDeadlineNanos),
                            executor)
                    .orTimeout(
                            PER_SERVICE_TIMEOUT_MS,
                            TimeUnit.MILLISECONDS)
                    .exceptionally(
                            ignored -> null);
        }

        private <T> T callWithRetry(
                RemoteService<T> service,
                AggregationRequest request,
                long deadlineNanos) {

            TransientRemoteException lastTransient =
                    null;

            for (int attempt = 1;
                 attempt <= retryPolicy.maxAttempts();
                 attempt++) {

                if (System.nanoTime()
                        >= deadlineNanos) {

                    break;
                }

                try {
                    return service.call(
                            request);

                } catch (InterruptedException interrupted) {

                    Thread.currentThread()
                            .interrupt();

                    throw new RuntimeException(
                            "remote call interrupted",
                            interrupted);

                } catch (TransientRemoteException transientFailure) {

                    lastTransient =
                            transientFailure;

                    if (attempt
                            == retryPolicy.maxAttempts()) {

                        break;
                    }

                    long backoff =
                            Math.min(
                                    retryPolicy.initialBackoffMillis()
                                            * (1L << (attempt - 1)),
                                    retryPolicy.maxBackoffMillis());

                    long jitter =
                            ThreadLocalRandom
                                    .current()
                                    .nextLong(
                                            0L,
                                            21L);

                    long sleepMillis =
                            backoff
                                    + jitter;

                    if (remainingMillis(
                            deadlineNanos)
                            <= sleepMillis) {

                        break;
                    }

                    sleep(
                            sleepMillis);

                } catch (Exception permanentFailure) {

                    /*
                     * Non-transient failure:
                     * retrying is not useful.
                     */
                    throw new CompletionException(
                            permanentFailure);
                }
            }

            if (lastTransient != null) {
                throw lastTransient;
            }

            throw new TransientRemoteException(
                    "remote deadline exhausted");
        }

        private void persist(
                AggregatedResult result) {

            persisted.put(
                    result.idempotencyKey(),
                    result);

            persistCount.incrementAndGet();
        }

        int persistCount() {
            return persistCount.get();
        }

        AggregatedResult persisted(
                String idempotencyKey) {

            return persisted.get(
                    idempotencyKey);
        }

        @Override
        public void close() {
            shutdown(
                    serviceAPool);

            shutdown(
                    serviceBPool);

            shutdown(
                    serviceCPool);
        }

        private static <T> T completedValueOrNull(
                CompletableFuture<T> future) {

            if (!future.isDone()) {
                return null;
            }

            try {
                return future.getNow(
                        null);

            } catch (CompletionException ignored) {
                return null;
            }
        }

        private static ExecutorService boundedPool(
                String threadPrefix,
                int threads,
                int queueCapacity) {

            AtomicInteger number =
                    new AtomicInteger();

            ThreadFactory factory =
                    runnable -> {

                        Thread thread =
                                new Thread(
                                        runnable,
                                        threadPrefix
                                                + "-"
                                                + number.incrementAndGet());

                        thread.setDaemon(
                                true);

                        return thread;
                    };

            return new ThreadPoolExecutor(
                    threads,
                    threads,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(
                            queueCapacity),
                    factory,
                    new ThreadPoolExecutor.AbortPolicy());
        }
    }

    // =====================================================================
    // 14. GENERAL HELPERS
    // =====================================================================

    private static long remainingMillis(
            long deadlineNanos) {

        long remainingNanos =
                deadlineNanos
                        - System.nanoTime();

        if (remainingNanos <= 0) {
            return 0L;
        }

        return Math.max(
                1L,
                TimeUnit.NANOSECONDS
                        .toMillis(
                                remainingNanos));
    }

    private static void sleep(
            long millis) {

        try {
            Thread.sleep(
                    millis);

        } catch (InterruptedException interrupted) {

            Thread.currentThread()
                    .interrupt();

            throw new RuntimeException(
                    "sleep interrupted",
                    interrupted);
        }
    }

    private static void shutdown(
            ExecutorService executor) {

        executor.shutdown();

        try {
            if (!executor.awaitTermination(
                    1L,
                    TimeUnit.SECONDS)) {

                executor.shutdownNow();
            }

        } catch (InterruptedException interrupted) {

            Thread.currentThread()
                    .interrupt();

            executor.shutdownNow();
        }
    }

    private static void require(
            boolean condition,
            String message) {

        if (!condition) {
            throw new AssertionError(
                    "FAILED: " + message);
        }
    }

    private static void section(
            String title) {

        System.out.println();
        System.out.println(
                "============================================================");
        System.out.println(
                title);
        System.out.println(
                "============================================================");
    }

    // =====================================================================
    // 15. RUNNABLE INTEGRATION TESTS
    // =====================================================================

    public static void main(String[] args)
            throws Exception {

        section(
                "1. FIXED-POINT NORMALIZATION / DENORMALIZATION");

        testFixedPoint();

        section(
                "2. ORDER BOOK + MATCHING + RISK + POSITION");

        testIntegratedTradingFlow();

        section(
                "3. IOC + FOK + SMP + KILL SWITCH");

        testTradingPolicies();

        section(
                "4. EXPLICIT CANCEL + RISK RELEASE");

        testExplicitCancel();

        section(
                "5. LRU CACHE");

        testLru();

        section(
                "6. OUT-OF-ORDER SEQUENCE RECONSTRUCTION");

        testSequenceReorder();

        section(
                "7. BOUNDED DEDUP");

        testBoundedDedup();

        section(
                "8. RESILIENT FAN-OUT / RETRY / IDEMPOTENCY");

        testResilientAggregator();

        section(
                "9. FINAL RESULT");

        System.out.println(
                "ALL FINAL INTEGRATION TESTS PASSED.");
    }

    private static void testFixedPoint() {

        long normalized =
                FixedPoint.normalize(
                        "101.2500");

        require(
                normalized
                        == 1_012_500L,
                "normalize 101.2500");

        require(
                FixedPoint.denormalize(
                        normalized)
                        .equals(
                                "101.2500"),
                "denormalize 101.2500");

        long notional =
                FixedPoint.notional(
                        normalized,
                        50L);

        require(
                FixedPoint.denormalize(
                        notional)
                        .equals(
                                "5062.5000"),
                "fixed-point notional");

        System.out.println(
                "101.2500 -> "
                        + normalized
                        + " -> "
                        + FixedPoint.denormalize(
                                normalized));

        System.out.println(
                "101.2500 x 50 = "
                        + FixedPoint.denormalize(
                                notional));
    }

    private static RiskEngine newRiskEngine(
            KillSwitchCheck killSwitch,
            long maxOpenQuantity,
            String maxOpenNotionalDecimal,
            String... firms) {

        RiskEngine riskEngine =
                new RiskEngine();

        RiskLimit limit =
                new RiskLimit(
                        maxOpenQuantity,
                        FixedPoint.normalize(
                                maxOpenNotionalDecimal));

        for (String firm
                : firms) {

            riskEngine.registerGroup(
                    firm,
                    new ExposureGroup(
                            List.of(
                                    killSwitch,
                                    new MaxOpenQuantityCheck(),
                                    new MaxOpenNotionalCheck()),
                            limit));
        }

        return riskEngine;
    }

    private static void testIntegratedTradingFlow() {

        KillSwitchCheck killSwitch =
                new KillSwitchCheck();

        RiskEngine riskEngine =
                newRiskEngine(
                        killSwitch,
                        10_000L,
                        "5000000.0000",
                        "FIRM-A",
                        "FIRM-B");

        TradingService trading =
                new TradingService(
                        riskEngine);

        trading.submit(
                trading.newLimitOrder(
                        "S1",
                        "AAPL",
                        "FIRM-B",
                        Side.SELL,
                        "101.0000",
                        50L,
                        TimeInForce.GTC));

        trading.submit(
                trading.newLimitOrder(
                        "S2",
                        "AAPL",
                        "FIRM-B",
                        Side.SELL,
                        "102.0000",
                        40L,
                        TimeInForce.GTC));

        OrderBook book =
                trading.book(
                        "AAPL");

        require(
                book != null,
                "AAPL book exists");

        require(
                Objects.equals(
                        book.bestAsk(),
                        FixedPoint.normalize(
                                "101.0000")),
                "best ask is 101");

        /*
         * BUY 70 @ 102:
         * - 50 @ passive 101
         * - 20 @ passive 102
         */
        SubmissionResult buy =
                trading.submit(
                        trading.newLimitOrder(
                                "B1",
                                "AAPL",
                                "FIRM-A",
                                Side.BUY,
                                "102.0000",
                                70L,
                                TimeInForce.GTC));

        require(
                buy.fills().size()
                        == 2,
                "B1 creates two fills");

        require(
                buy.fills().get(0)
                        .executionQuantity()
                        == 50L,
                "first fill quantity");

        require(
                buy.fills().get(0)
                        .executionPrice()
                        == FixedPoint.normalize(
                                "101.0000"),
                "passive price wins");

        Order remainingAsk =
                book.findRestingOrder(
                        "S2");

        require(
                remainingAsk != null
                        && remainingAsk.remainingQuantity
                        == 20L,
                "S2 keeps 20");

        require(
                Objects.equals(
                        book.bestAsk(),
                        FixedPoint.normalize(
                                "102.0000")),
                "best ask becomes 102");

        require(
                trading.position(
                        "FIRM-A",
                        "AAPL")
                        == 70L,
                "FIRM-A +70");

        require(
                trading.position(
                        "FIRM-B",
                        "AAPL")
                        == -70L,
                "FIRM-B -70");

        ExposureState firmA =
                riskEngine.snapshot(
                        "FIRM-A");

        ExposureState firmB =
                riskEngine.snapshot(
                        "FIRM-B");

        require(
                firmA.openQuantity
                        == 0L,
                "fully executed aggressor has zero open qty");

        require(
                firmB.openQuantity
                        == 20L,
                "S2 remaining open qty is 20");

        require(
                firmB.openNotional
                        == FixedPoint.notional(
                                FixedPoint.normalize(
                                        "102.0000"),
                                20L),
                "S2 remaining notional");

        System.out.println(
                book.snapshot());

        System.out.println(
                "Fills: "
                        + buy.fills());

        System.out.println(
                "Risk A: "
                        + firmA);

        System.out.println(
                "Risk B: "
                        + firmB);
    }

    private static void testTradingPolicies() {

        KillSwitchCheck killSwitch =
                new KillSwitchCheck();

        RiskEngine riskEngine =
                newRiskEngine(
                        killSwitch,
                        100_000L,
                        "100000000.0000",
                        "A",
                        "B");

        TradingService trading =
                new TradingService(
                        riskEngine);

        /*
         * 10 available.
         */
        trading.submit(
                trading.newLimitOrder(
                        "ASK-1",
                        "MSFT",
                        "B",
                        Side.SELL,
                        "100.0000",
                        10L,
                        TimeInForce.GTC));

        /*
         * FOK requests 20:
         * zero execution because full quantity unavailable.
         */
        SubmissionResult fok =
                trading.submit(
                        trading.newLimitOrder(
                                "FOK-BUY",
                                "MSFT",
                                "A",
                                Side.BUY,
                                "100.0000",
                                20L,
                                TimeInForce.FOK));

        require(
                fok.fills().isEmpty(),
                "FOK all-or-none");

        require(
                fok.order().state
                        == OrderState.CANCELLED,
                "unfillable FOK cancelled");

        require(
                trading.book(
                        "MSFT")
                        .findRestingOrder(
                                "ASK-1")
                        != null,
                "failed FOK did not mutate book");

        /*
         * IOC takes 10 then cancels 10 remainder.
         */
        SubmissionResult ioc =
                trading.submit(
                        trading.newLimitOrder(
                                "IOC-BUY",
                                "MSFT",
                                "A",
                                Side.BUY,
                                "100.0000",
                                20L,
                                TimeInForce.IOC));

        require(
                ioc.order().filledQuantity
                        == 10L,
                "IOC takes available 10");

        require(
                ioc.order().remainingQuantity
                        == 10L,
                "IOC has 10 cancelled remainder");

        require(
                ioc.order().state
                        == OrderState.CANCELLED,
                "IOC remainder cancelled");

        /*
         * SMP cancel-passive:
         * same-firm ask at 101 is cancelled,
         * then incoming buy continues to different-firm ask at 102.
         */
        trading.submit(
                trading.newLimitOrder(
                        "SELF-ASK",
                        "MSFT",
                        "A",
                        Side.SELL,
                        "101.0000",
                        5L,
                        TimeInForce.GTC));

        trading.submit(
                trading.newLimitOrder(
                        "OTHER-ASK",
                        "MSFT",
                        "B",
                        Side.SELL,
                        "102.0000",
                        5L,
                        TimeInForce.GTC));

        SubmissionResult smp =
                trading.submit(
                        trading.newLimitOrder(
                                "SMP-BUY",
                                "MSFT",
                                "A",
                                Side.BUY,
                                "102.0000",
                                5L,
                                TimeInForce.GTC));

        require(
                smp.selfMatchCancelledPassiveIds()
                        .equals(
                                List.of(
                                        "SELF-ASK")),
                "SMP cancels passive self-order");

        require(
                smp.fills().size()
                        == 1
                        && smp.fills().get(0)
                        .passiveOrderId()
                        .equals(
                                "OTHER-ASK"),
                "SMP continues to eligible liquidity");

        /*
         * Market order with explicit reference price used only for risk.
         */
        trading.submit(
                trading.newLimitOrder(
                        "MARKET-ASK",
                        "MSFT",
                        "B",
                        Side.SELL,
                        "103.0000",
                        3L,
                        TimeInForce.GTC));

        SubmissionResult market =
                trading.submit(
                        trading.newMarketOrder(
                                "MARKET-BUY",
                                "MSFT",
                                "A",
                                Side.BUY,
                                3L,
                                "103.0000",
                                TimeInForce.IOC));

        require(
                market.fills().size()
                        == 1,
                "market order fills");

        require(
                market.fills().get(0)
                        .executionPrice()
                        == FixedPoint.normalize(
                                "103.0000"),
                "market executes at passive price");

        killSwitch.activate();

        SubmissionResult killed =
                trading.submit(
                        trading.newLimitOrder(
                                "KILLED",
                                "MSFT",
                                "A",
                                Side.BUY,
                                "99.0000",
                                1L,
                                TimeInForce.GTC));

        require(
                killed.riskDecision()
                        == RiskDecision.KILL,
                "kill switch blocks order");

        require(
                killed.order().state
                        == OrderState.REJECTED,
                "killed order rejected");

        killSwitch.deactivate();

        System.out.println(
                "IOC / FOK / SMP / market / kill-switch passed.");
    }

    private static void testExplicitCancel() {

        KillSwitchCheck killSwitch =
                new KillSwitchCheck();

        RiskEngine riskEngine =
                newRiskEngine(
                        killSwitch,
                        1_000L,
                        "1000000.0000",
                        "FIRM-1");

        TradingService trading =
                new TradingService(
                        riskEngine);

        trading.submit(
                trading.newLimitOrder(
                        "CANCEL-ME",
                        "IBM",
                        "FIRM-1",
                        Side.BUY,
                        "10.0000",
                        25L,
                        TimeInForce.GTC));

        require(
                riskEngine.snapshot(
                        "FIRM-1")
                        .openQuantity
                        == 25L,
                "resting order reserves risk");

        require(
                trading.cancel(
                        "CANCEL-ME"),
                "explicit cancel succeeds");

        require(
                trading.order(
                        "CANCEL-ME")
                        .state
                        == OrderState.CANCELLED,
                "store retains cancelled lifecycle");

        ExposureState after =
                riskEngine.snapshot(
                        "FIRM-1");

        require(
                after.openQuantity
                        == 0L
                        && after.openNotional
                        == 0L,
                "cancel releases all remaining risk");

        System.out.println(
                "Explicit cancel released book liquidity and risk.");
    }

    private static void testLru() {

        LruCache<String, Integer> cache =
                new LruCache<>(2);

        cache.put(
                "A",
                1);

        cache.put(
                "B",
                2);

        require(
                Objects.equals(
                        cache.get(
                                "A"),
                        1),
                "A exists");

        /*
         * A is now MRU; B becomes LRU.
         */
        cache.put(
                "C",
                3);

        require(
                cache.get(
                        "B")
                        == null,
                "B evicted");

        require(
                Objects.equals(
                        cache.get(
                                "A"),
                        1),
                "A retained");

        require(
                Objects.equals(
                        cache.get(
                                "C"),
                        3),
                "C retained");

        require(
                cache.size()
                        == 2,
                "capacity respected");

        System.out.println(
                "LRU passed.");
    }

    private static void testSequenceReorder() {

        SequenceReorderBuffer<String> buffer =
                new SequenceReorderBuffer<>(
                        1L);

        require(
                buffer.receive(
                        3L,
                        "C")
                        .isEmpty(),
                "3 buffers");

        require(
                buffer.receive(
                        2L,
                        "B")
                        .isEmpty(),
                "2 buffers");

        List<String> ready =
                buffer.receive(
                        1L,
                        "A");

        require(
                ready.equals(
                        List.of(
                                "A",
                                "B",
                                "C")),
                "1 closes hole and drains");

        require(
                buffer.nextExpected()
                        == 4L,
                "next expected is 4");

        require(
                buffer.bufferedCount()
                        == 0,
                "buffer empty");

        System.out.println(
                "Reordered: "
                        + ready);
    }

    private static void testBoundedDedup() {

        BoundedDedupSet<String> dedup =
                new BoundedDedupSet<>(
                        2);

        require(
                dedup.firstTime(
                        "E1"),
                "E1 first");

        require(
                !dedup.firstTime(
                        "E1"),
                "E1 duplicate");

        require(
                dedup.firstTime(
                        "E2"),
                "E2 first");

        require(
                dedup.firstTime(
                        "E3"),
                "E3 first");

        require(
                dedup.size()
                        == 2,
                "bounded size");

        /*
         * E1 was evicted; bounded dedup is not eternal.
         */
        require(
                dedup.firstTime(
                        "E1"),
                "evicted E1 accepted again");

        System.out.println(
                "Bounded dedup semantics passed.");
    }

    private static void testResilientAggregator()
            throws Exception {

        AtomicInteger serviceBAttempts =
                new AtomicInteger();

        RemoteService<ServiceAResult> serviceA =
                request ->
                        new ServiceAResult(
                                "A:"
                                        + request.payload());

        RemoteService<ServiceBResult> serviceB =
                request -> {

                    if (serviceBAttempts
                            .incrementAndGet()
                            == 1) {

                        throw new TransientRemoteException(
                                "temporary B failure");
                    }

                    return new ServiceBResult(
                            "B:"
                                    + request.payload());
                };

        RemoteService<ServiceCResult> serviceC =
                request -> {

                    /*
                     * Optional service exceeds per-service timeout.
                     */
                    Thread.sleep(
                            300L);

                    return new ServiceCResult(
                            "C:"
                                    + request.payload());
                };

        try (ResilientAggregator aggregator =
                     new ResilientAggregator(
                             serviceA,
                             serviceB,
                             serviceC)) {

            AggregationRequest request =
                    new AggregationRequest(
                            "idem-1",
                            "payload");

            ExecutorService callers =
                    Executors.newFixedThreadPool(
                            2);

            try {
                Future<AggregatedResult> first =
                        callers.submit(
                                () ->
                                        aggregator.aggregate(
                                                request));

                Future<AggregatedResult> duplicate =
                        callers.submit(
                                () ->
                                        aggregator.aggregate(
                                                request));

                AggregatedResult r1 =
                        first.get(
                                2L,
                                TimeUnit.SECONDS);

                AggregatedResult r2 =
                        duplicate.get(
                                2L,
                                TimeUnit.SECONDS);

                require(
                        r1.equals(
                                r2),
                        "same idempotency key shares result");

                require(
                        r1.partial(),
                        "slow optional C creates partial result");

                require(
                        r1.mergedValue()
                                .startsWith(
                                        "A:payload|B:payload|"),
                        "A and retried B contribute");

                require(
                        aggregator.persistCount()
                                == 1,
                        "in-process durable side effect once");

                require(
                        Objects.equals(
                                aggregator.persisted(
                                        "idem-1"),
                                r1),
                        "persisted result matches");

                require(
                        serviceBAttempts.get()
                                >= 2,
                        "B retried transient failure");

                System.out.println(
                        "Aggregator: "
                                + r1);

                System.out.println(
                        "Persist count="
                                + aggregator.persistCount()
                                + ", B attempts="
                                + serviceBAttempts.get());

            } finally {
                callers.shutdownNow();
            }
        }
    }
}
