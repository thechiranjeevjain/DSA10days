package org.chijai.trading;

import java.math.BigInteger;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * ProductionTradingSystem
 *
 * One Java 17+ file tying together the canonical trading LLD model:
 *
 *   OrderStore -> RiskEngine -> OrderBook -> MatchingEngine -> Fill/Position
 *
 * plus supporting infrastructure:
 *
 *   - Order lifecycle + time-in-force
 *   - Price levels + price-time priority
 *   - Resting-order cancellation
 *   - Self-match prevention (cancel-passive policy for this demo)
 *   - Transactional risk reservation / rollback / release
 *   - Position aggregation
 *   - LRU cache
 *   - Out-of-order sequence reconstruction
 *   - Bounded dedup
 *   - Resilient fan-out aggregator with:
 *       * in-process idempotent single-flight
 *       * bounded bulkhead executors
 *       * per-service timeout
 *       * overall deadline
 *       * transient retry with backoff + jitter
 *       * partial failure for optional services
 *
 * IMPORTANT DESIGN BOUNDARY:
 * The remote fan-out aggregator is intentionally separate from the deterministic
 * matching hot path. Do not put Redis/network retries/remote calls in the
 * low-latency matching loop.
 *
 * Compile:
 *   javac ProductionTradingSystem.java
 *
 * Run:
 *   java ProductionTradingSystem
 */
public class ProductionTradingSystem {

    // ========================================================================
    // 1. CANONICAL DOMAIN ENUMS
    // ========================================================================

    enum Side {
        BUY, SELL
    }

    enum TimeInForce {
        IOC, FOK, GTC, DAY
    }

    enum Result {
        PASS, BREACH, KILL
    }

    enum OrderState {
        PENDING_ACK,
        ACKNOWLEDGED,
        PARTIALLY_FILLED,

        FILLED,
        CANCELLED,
        REJECTED,

        PENDING_CANCEL,
        PENDING_REPLACE;

        boolean terminal() {
            return this == FILLED
                    || this == CANCELLED
                    || this == REJECTED;
        }
    }

    /**
     * Market order sentinel used by the canonical model.
     * Prices otherwise use integer ticks, never double.
     */
    static final long MARKET_PRICE = Long.MIN_VALUE;

    // ========================================================================
    // 2. ORDER
    // ========================================================================

    static final class Order {

        // Identity
        final String clOrdId;
        String exchOrdId;

        // Economics
        final Side side;
        final long price;
        final long origQty;

        // Fill state
        long leavesQty;
        long cumQty;
        long avgPx;

        // Book / ranking context
        PriceLevel level;

        /**
         * Interview shorthand for FIFO priority.
         * Production systems normally need a deterministic tie-break too,
         * e.g. (transactionTimeNanos, sequenceNumber).
         */
        final long rankNanos;

        // Matching / OMS context
        final String firmId;
        final TimeInForce tif;

        OrderState state;

        Order(
                String clOrdId,
                Side side,
                long price,
                long origQty,
                long rankNanos,
                String firmId,
                TimeInForce tif) {

            this.clOrdId = Objects.requireNonNull(clOrdId);
            this.side = Objects.requireNonNull(side);

            if (origQty <= 0) {
                throw new IllegalArgumentException("origQty must be > 0");
            }

            this.price = price;
            this.origQty = origQty;
            this.leavesQty = origQty;
            this.rankNanos = rankNanos;
            this.firmId = firmId;
            this.tif = Objects.requireNonNull(tif);
            this.state = OrderState.PENDING_ACK;
        }

        void fill(long qty, long px) {

            if (qty <= 0) {
                throw new IllegalArgumentException("fill qty must be > 0");
            }

            if (qty > leavesQty) {
                throw new IllegalArgumentException(
                        "overfill: qty=" + qty + ", leaves=" + leavesQty);
            }

            long newCumQty = Math.addExact(cumQty, qty);

            /*
             * BigInteger keeps the demo safe from price*quantity overflow while
             * preserving the canonical long-tick external representation.
             */
            BigInteger weightedOld =
                    BigInteger.valueOf(avgPx)
                            .multiply(BigInteger.valueOf(cumQty));

            BigInteger weightedNew =
                    BigInteger.valueOf(px)
                            .multiply(BigInteger.valueOf(qty));

            avgPx = weightedOld
                    .add(weightedNew)
                    .divide(BigInteger.valueOf(newCumQty))
                    .longValueExact();

            cumQty = newCumQty;
            leavesQty -= qty;

            state = leavesQty == 0
                    ? OrderState.FILLED
                    : OrderState.PARTIALLY_FILLED;

            assertInvariant();
        }

        boolean isMarket() {
            return price == MARKET_PRICE;
        }

        boolean isDone() {
            return leavesQty == 0;
        }

        boolean isTerminal() {
            return state != null && state.terminal();
        }

        void acknowledge(String exchangeOrderId) {
            this.exchOrdId = Objects.requireNonNull(exchangeOrderId);
            this.state = OrderState.ACKNOWLEDGED;
        }

        void reject() {
            this.state = OrderState.REJECTED;
        }

        void cancelRemainder() {
            if (!isDone()) {
                this.state = OrderState.CANCELLED;
            }
        }

        void assertInvariant() {
            if (leavesQty < 0
                    || cumQty < 0
                    || Math.addExact(leavesQty, cumQty) != origQty) {

                throw new IllegalStateException(
                        "order quantity invariant violated for " + clOrdId);
            }
        }

        @Override
        public String toString() {
            return "Order{" +
                    "clOrdId='" + clOrdId + '\'' +
                    ", exchOrdId='" + exchOrdId + '\'' +
                    ", side=" + side +
                    ", price=" + (isMarket() ? "MKT" : price) +
                    ", origQty=" + origQty +
                    ", leavesQty=" + leavesQty +
                    ", cumQty=" + cumQty +
                    ", avgPx=" + avgPx +
                    ", state=" + state +
                    ", firmId='" + firmId + '\'' +
                    ", tif=" + tif +
                    '}';
        }
    }

    // ========================================================================
    // 3. PRICE LEVEL
    // ========================================================================

    static final class PriceLevel {

        final long price;

        /**
         * Interview implementation.
         *
         * Head = oldest/highest time priority at this price.
         *
         * NOTE:
         * ArrayDeque.remove(order) is O(number of orders at this level).
         * A production low-latency book can replace this with an intrusive
         * doubly-linked OrderList plus an ID -> node index for O(1) unlink.
         */
        final ArrayDeque<Order> queue =
                new ArrayDeque<>();

        long totalQty;

        PriceLevel(long price) {
            this.price = price;
        }

        void add(Order order) {

            if (order.price != price) {
                throw new IllegalArgumentException(
                        "order price does not match level");
            }

            if (order.level != null) {
                throw new IllegalStateException(
                        "order already belongs to a level");
            }

            queue.addLast(order);
            totalQty = Math.addExact(totalQty, order.leavesQty);
            order.level = this;
        }

        Order peek() {
            return queue.peekFirst();
        }

        /**
         * Physical unlink for a completely removed head order.
         *
         * IMPORTANT:
         * This subtracts the order's PRE-FILL leavesQty.
         * Therefore matching code polls BEFORE calling order.fill().
         */
        Order poll() {

            Order order = queue.pollFirst();

            if (order != null) {
                totalQty = Math.subtractExact(
                        totalQty,
                        order.leavesQty);

                order.level = null;
            }

            return order;
        }

        boolean remove(Order order) {

            if (!queue.remove(order)) {
                return false;
            }

            totalQty = Math.subtractExact(
                    totalQty,
                    order.leavesQty);

            order.level = null;
            return true;
        }

        /**
         * Partial fill: order stays at head and retains priority,
         * but aggregate resting quantity falls.
         */
        void reduceTotalQty(long qty) {

            if (qty <= 0 || qty > totalQty) {
                throw new IllegalArgumentException(
                        "invalid level quantity reduction");
            }

            totalQty -= qty;
        }

        boolean empty() {
            return queue.isEmpty();
        }

        int orderCount() {
            return queue.size();
        }

        @Override
        public String toString() {
            return "PriceLevel{" +
                    "price=" + price +
                    ", totalQty=" + totalQty +
                    ", orders=" + queue.size() +
                    '}';
        }
    }

    // ========================================================================
    // 4. ORDER BOOK
    // ========================================================================

    static final class OrderBook {

        /**
         * firstEntry() is best on BOTH sides:
         *
         * bids -> reverse sorted -> first = highest
         * asks -> natural sorted -> first = lowest
         */
        final TreeMap<Long, PriceLevel> bids =
                new TreeMap<>(Comparator.reverseOrder());

        final TreeMap<Long, PriceLevel> asks =
                new TreeMap<>();

        /**
         * RESTING orders only.
         *
         * This is intentionally different from OrderStore, which owns the
         * authoritative identity/lifecycle lookup.
         */
        final HashMap<String, Order> restingByClOrd =
                new HashMap<>();

        void add(Order order) {

            if (order.isMarket()) {
                throw new IllegalArgumentException(
                        "market order cannot rest on the book");
            }

            if (order.isDone() || order.isTerminal()) {
                throw new IllegalArgumentException(
                        "done/terminal order cannot rest");
            }

            if (restingByClOrd.containsKey(order.clOrdId)) {
                throw new IllegalArgumentException(
                        "order already resting: " + order.clOrdId);
            }

            TreeMap<Long, PriceLevel> side =
                    sideMap(order.side);

            PriceLevel level =
                    side.computeIfAbsent(
                            order.price,
                            PriceLevel::new);

            level.add(order);
            restingByClOrd.put(order.clOrdId, order);
        }

        boolean cancel(String clOrdId) {

            Order order =
                    restingByClOrd.get(clOrdId);

            if (order == null || order.level == null) {
                return false;
            }

            PriceLevel level =
                    order.level;

            if (!level.remove(order)) {
                return false;
            }

            restingByClOrd.remove(clOrdId);

            if (level.empty()) {
                sideMap(order.side)
                        .remove(level.price);
            }

            order.cancelRemainder();
            return true;
        }

        Order findResting(String id) {
            return restingByClOrd.get(id);
        }

        Long bestBid() {
            return bids.isEmpty()
                    ? null
                    : bids.firstKey();
        }

        Long bestAsk() {
            return asks.isEmpty()
                    ? null
                    : asks.firstKey();
        }

        PriceLevel bestBidLevel() {
            return bids.isEmpty()
                    ? null
                    : bids.firstEntry().getValue();
        }

        PriceLevel bestAskLevel() {
            return asks.isEmpty()
                    ? null
                    : asks.firstEntry().getValue();
        }

        /**
         * MatchingEngine already physically removed the order from PriceLevel.
         * This method ONLY updates the resting-order index.
         */
        void forgetDequeued(Order order) {
            restingByClOrd.remove(order.clOrdId);
        }

        void removeLevelIfEmpty(
                Side side,
                PriceLevel level) {

            if (level.empty()) {
                sideMap(side).remove(level.price);
            }
        }

        int restingOrderCount() {
            return restingByClOrd.size();
        }

        private TreeMap<Long, PriceLevel>
        sideMap(Side side) {

            return side == Side.BUY
                    ? bids
                    : asks;
        }

        String snapshot() {

            return "OrderBook{" +
                    "bestBid=" + bestBid() +
                    ", bestAsk=" + bestAsk() +
                    ", resting=" + restingOrderCount() +
                    ", bids=" + bids.values() +
                    ", asks=" + asks.values() +
                    '}';
        }
    }

    // ========================================================================
    // 5. AUTHORITATIVE ORDER STORE
    // ========================================================================

    static final class OrderStore {

        private final Map<String, Order> byClOrd =
                new HashMap<>();

        private final Map<String, Order> byExchId =
                new HashMap<>();

        void put(Order order) {

            if (byClOrd.putIfAbsent(
                    order.clOrdId,
                    order) != null) {

                throw new IllegalArgumentException(
                        "duplicate clOrdId: " + order.clOrdId);
            }
        }

        void link(
                Order order,
                String exchangeOrderId) {

            Objects.requireNonNull(exchangeOrderId);

            Order previous =
                    byExchId.putIfAbsent(
                            exchangeOrderId,
                            order);

            if (previous != null
                    && previous != order) {

                throw new IllegalArgumentException(
                        "duplicate exchOrdId: " + exchangeOrderId);
            }

            order.acknowledge(exchangeOrderId);
        }

        Order byClOrd(String id) {
            return byClOrd.get(id);
        }

        Order byExchId(String id) {
            return byExchId.get(id);
        }

        boolean isDuplicate(String clientOrderId) {
            return byClOrd.containsKey(clientOrderId);
        }

        void remove(Order order) {

            byClOrd.remove(
                    order.clOrdId,
                    order);

            if (order.exchOrdId != null) {
                byExchId.remove(
                        order.exchOrdId,
                        order);
            }
        }

        int size() {
            return byClOrd.size();
        }
    }

    // ========================================================================
    // 6. FILL + MATCH OUTCOME
    // ========================================================================

    record Fill(
            String aggressorClOrdId,
            String passiveClOrdId,
            long price,
            long qty) {

        Fill {
            if (qty <= 0) {
                throw new IllegalArgumentException(
                        "fill qty must be > 0");
            }
        }
    }

    record MatchOutcome(
            List<Fill> fills,
            List<String> selfMatchCancelledPassiveIds) {

        MatchOutcome {
            fills = List.copyOf(fills);
            selfMatchCancelledPassiveIds =
                    List.copyOf(selfMatchCancelledPassiveIds);
        }
    }

    // ========================================================================
    // 7. MATCHING ENGINE
    // ========================================================================

    static final class MatchingEngine {

        /**
         * Convenience API matching the whiteboard model.
         */
        List<Fill> match(
                Order incoming,
                OrderBook book) {

            return matchDetailed(
                    incoming,
                    book).fills();
        }

        MatchOutcome matchDetailed(
                Order incoming,
                OrderBook book) {

            Objects.requireNonNull(incoming);
            Objects.requireNonNull(book);

            if (incoming.isDone()
                    || incoming.isTerminal()) {

                throw new IllegalArgumentException(
                        "incoming order already terminal/done");
            }

            List<Fill> fills =
                    new ArrayList<>();

            List<String> selfMatchCancellations =
                    new ArrayList<>();

            /*
             * FOK = all-or-none.
             * Pre-check uses the SAME crossing and SMP eligibility rules.
             */
            if (incoming.tif == TimeInForce.FOK
                    && !canFullyFill(incoming, book)) {

                incoming.cancelRemainder();

                return new MatchOutcome(
                        fills,
                        selfMatchCancellations);
            }

            TreeMap<Long, PriceLevel> opposite =
                    incoming.side == Side.BUY
                            ? book.asks
                            : book.bids;

            while (incoming.leavesQty > 0
                    && !opposite.isEmpty()) {

                Map.Entry<Long, PriceLevel> best =
                        opposite.firstEntry();

                long passivePrice =
                        best.getKey();

                if (!crosses(
                        incoming,
                        passivePrice)) {

                    break;
                }

                PriceLevel level =
                        best.getValue();

                while (incoming.leavesQty > 0
                        && !level.empty()) {

                    Order passive =
                            level.peek();

                    /*
                     * Demo SMP policy:
                     * self-match => cancel passive order.
                     *
                     * Real venues can have different SMP actions.
                     */
                    if (sameFirm(
                            passive,
                            incoming)) {

                        boolean cancelled =
                                book.cancel(
                                        passive.clOrdId);

                        if (!cancelled) {
                            throw new IllegalStateException(
                                    "failed to cancel SMP passive");
                        }

                        selfMatchCancellations.add(
                                passive.clOrdId);

                        continue;
                    }

                    long qty =
                            Math.min(
                                    incoming.leavesQty,
                                    passive.leavesQty);

                    /*
                     * This exercise executes at resting/passive price.
                     */
                    long execPrice =
                            passivePrice;

                    boolean passiveCompletes =
                            qty == passive.leavesQty;

                    if (passiveCompletes) {

                        /*
                         * Physical unlink happens exactly once.
                         * Poll BEFORE fill because poll subtracts the
                         * current pre-fill leavesQty.
                         */
                        Order removed =
                                level.poll();

                        if (removed != passive) {
                            throw new IllegalStateException(
                                    "price-time queue corrupted");
                        }

                        /*
                         * DO NOT call book.cancel(passive) here:
                         * PriceLevel unlink already happened.
                         */
                        book.forgetDequeued(
                                passive);

                    } else {

                        /*
                         * Partial resting order stays at head,
                         * preserving original time priority.
                         */
                        level.reduceTotalQty(qty);
                    }

                    passive.fill(
                            qty,
                            execPrice);

                    incoming.fill(
                            qty,
                            execPrice);

                    fills.add(
                            new Fill(
                                    incoming.clOrdId,
                                    passive.clOrdId,
                                    execPrice,
                                    qty));
                }

                Side passiveSide =
                        incoming.side == Side.BUY
                                ? Side.SELL
                                : Side.BUY;

                book.removeLevelIfEmpty(
                        passiveSide,
                        level);
            }

            afterMatch(
                    incoming,
                    book);

            return new MatchOutcome(
                    fills,
                    selfMatchCancellations);
        }

        private boolean sameFirm(
                Order passive,
                Order incoming) {

            return passive.firmId != null
                    && incoming.firmId != null
                    && Objects.equals(
                            passive.firmId,
                            incoming.firmId);
        }

        private void afterMatch(
                Order order,
                OrderBook book) {

            if (order.isDone()) {
                return;
            }

            /*
             * IOC: take available liquidity now, cancel remainder.
             * Market remainder cannot rest.
             */
            if (order.tif == TimeInForce.IOC
                    || order.isMarket()) {

                order.cancelRemainder();
                return;
            }

            /*
             * FOK should have been completely fillable before
             * any matching mutation.
             */
            if (order.tif == TimeInForce.FOK) {
                throw new IllegalStateException(
                        "FOK partially executed");
            }

            /*
             * GTC / DAY limit remainder rests.
             */
            book.add(order);
        }

        private boolean crosses(
                Order order,
                long bestOppositePrice) {

            if (order.isMarket()) {
                return true;
            }

            return order.side == Side.BUY
                    ? order.price >= bestOppositePrice
                    : order.price <= bestOppositePrice;
        }

        private boolean canFullyFill(
                Order incoming,
                OrderBook book) {

            long required =
                    incoming.leavesQty;

            NavigableMap<Long, PriceLevel> opposite =
                    incoming.side == Side.BUY
                            ? book.asks
                            : book.bids;

            for (Map.Entry<Long, PriceLevel> entry
                    : opposite.entrySet()) {

                if (!crosses(
                        incoming,
                        entry.getKey())) {

                    break;
                }

                for (Order passive
                        : entry.getValue().queue) {

                    if (sameFirm(
                            passive,
                            incoming)) {

                        continue;
                    }

                    required -=
                            passive.leavesQty;

                    if (required <= 0) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    // ========================================================================
    // 8. RISK MODEL
    // ========================================================================

    /**
     * Simplified open-order exposure for this runnable LLD.
     *
     * Notional is priceTicks * qty.
     *
     * For market orders this demo uses notional=0 because the canonical Order
     * model does not carry instrument/reference-price data. In a real system,
     * market-order risk should use a trusted reference/limit price.
     */
    record Exposure(
            String groupId,
            long qty,
            long notional) {

        Exposure {
            Objects.requireNonNull(groupId);

            if (qty < 0 || notional < 0) {
                throw new IllegalArgumentException(
                        "exposure values must be >= 0");
            }
        }

        static Exposure forOrder(
                Order order,
                long qty) {

            long notional = order.isMarket()
                    ? 0L
                    : multiplyNonNegative(
                            order.price,
                            qty);

            return new Exposure(
                    groupId(order),
                    qty,
                    notional);
        }

        private static String groupId(Order order) {
            return order.firmId == null
                    ? "<NO_FIRM>"
                    : order.firmId;
        }
    }

    record RiskLimit(
            long maxOpenQty,
            long maxOpenNotional) {

        RiskLimit {
            if (maxOpenQty < 0
                    || maxOpenNotional < 0) {

                throw new IllegalArgumentException(
                        "risk limits must be >= 0");
            }
        }
    }

    interface RiskCheck {

        Result check(
                Exposure exposure,
                ExposureState state,
                RiskLimit limit);

        void release(
                Exposure exposure,
                ExposureState state);
    }

    static final class ExposureState {

        long openQty;
        long openNotional;

        private long savedQty;
        private long savedNotional;

        void begin() {
            savedQty = openQty;
            savedNotional = openNotional;
        }

        void commit() {
            // saved snapshot is logically discarded
        }

        void rollback() {
            openQty = savedQty;
            openNotional = savedNotional;
        }

        @Override
        public String toString() {
            return "ExposureState{" +
                    "openQty=" + openQty +
                    ", openNotional=" + openNotional +
                    '}';
        }
    }

    /**
     * Owns ONLY openQty so checks do not double-apply the same field.
     */
    static final class MaxQtyCheck
            implements RiskCheck {

        @Override
        public Result check(
                Exposure exposure,
                ExposureState state,
                RiskLimit limit) {

            long prospective =
                    Math.addExact(
                            state.openQty,
                            exposure.qty());

            if (prospective
                    > limit.maxOpenQty()) {

                return Result.BREACH;
            }

            state.openQty =
                    prospective;

            return Result.PASS;
        }

        @Override
        public void release(
                Exposure exposure,
                ExposureState state) {

            state.openQty =
                    Math.max(
                            0L,
                            state.openQty
                                    - exposure.qty());
        }
    }

    /**
     * Owns ONLY openNotional.
     */
    static final class MaxNotionalCheck
            implements RiskCheck {

        @Override
        public Result check(
                Exposure exposure,
                ExposureState state,
                RiskLimit limit) {

            long prospective =
                    Math.addExact(
                            state.openNotional,
                            exposure.notional());

            if (prospective
                    > limit.maxOpenNotional()) {

                return Result.BREACH;
            }

            state.openNotional =
                    prospective;

            return Result.PASS;
        }

        @Override
        public void release(
                Exposure exposure,
                ExposureState state) {

            state.openNotional =
                    Math.max(
                            0L,
                            state.openNotional
                                    - exposure.notional());
        }
    }

    static final class KillSwitchCheck
            implements RiskCheck {

        private volatile boolean active;

        @Override
        public Result check(
                Exposure exposure,
                ExposureState state,
                RiskLimit limit) {

            return active
                    ? Result.KILL
                    : Result.PASS;
        }

        @Override
        public void release(
                Exposure exposure,
                ExposureState state) {

            // Kill switch owns no exposure metric.
        }

        void activate() {
            active = true;
        }

        void deactivate() {
            active = false;
        }

        boolean active() {
            return active;
        }
    }

    static final class ExposureGroup {

        final List<RiskCheck> checks;
        final ExposureState state =
                new ExposureState();
        final RiskLimit limit;

        ExposureGroup(
                List<RiskCheck> checks,
                RiskLimit limit) {

            this.checks =
                    List.copyOf(checks);

            this.limit =
                    Objects.requireNonNull(limit);
        }

        /**
         * Transactional reservation:
         * reject/exception => rollback to exact pre-attempt state.
         */
        synchronized Result evaluate(
                Exposure exposure) {

            state.begin();

            try {

                for (RiskCheck check : checks) {

                    Result result =
                            check.check(
                                    exposure,
                                    state,
                                    limit);

                    if (result != Result.PASS) {

                        state.rollback();
                        return result;
                    }
                }

                state.commit();
                return Result.PASS;

            } catch (RuntimeException e) {

                state.rollback();
                throw e;
            }
        }

        synchronized void release(
                Exposure exposure) {

            for (RiskCheck check
                    : checks) {

                check.release(
                        exposure,
                        state);
            }
        }

        synchronized ExposureState snapshot() {

            ExposureState copy =
                    new ExposureState();

            copy.openQty =
                    state.openQty;

            copy.openNotional =
                    state.openNotional;

            return copy;
        }
    }

    static final class RiskEngine {

        private final Map<String, ExposureGroup> groups =
                new HashMap<>();

        void register(
                String groupId,
                ExposureGroup group) {

            groups.put(
                    Objects.requireNonNull(groupId),
                    Objects.requireNonNull(group));
        }

        Result check(
                Exposure exposure) {

            return group(exposure.groupId())
                    .evaluate(exposure);
        }

        void release(
                Exposure exposure) {

            group(exposure.groupId())
                    .release(exposure);
        }

        ExposureState snapshot(
                String groupId) {

            return group(groupId)
                    .snapshot();
        }

        private ExposureGroup group(
                String groupId) {

            ExposureGroup group =
                    groups.get(groupId);

            if (group == null) {
                throw new IllegalStateException(
                        "no risk group: " + groupId);
            }

            return group;
        }
    }

    // ========================================================================
    // 9. POSITION SERVICE
    // ========================================================================

    /**
     * Minimal downstream position aggregation using firm as the key because the
     * canonical Order model in the handbook intentionally does not include
     * account/instrument fields.
     *
     * BUY  -> +qty
     * SELL -> -qty
     */
    static final class PositionService {

        private final Map<String, Long> netByFirm =
                new HashMap<>();

        void applyFill(
                Order order,
                long qty) {

            long signed =
                    order.side == Side.BUY
                            ? qty
                            : -qty;

            String key =
                    order.firmId == null
                            ? "<NO_FIRM>"
                            : order.firmId;

            netByFirm.merge(
                    key,
                    signed,
                    Long::sum);
        }

        long netPosition(
                String firmId) {

            return netByFirm.getOrDefault(
                    firmId,
                    0L);
        }
    }

    // ========================================================================
    // 10. TRADING SYSTEM ORCHESTRATOR
    // ========================================================================

    record SubmitResult(
            Result riskResult,
            Order order,
            List<Fill> fills,
            List<String> selfMatchCancelledPassiveIds) {

        SubmitResult {
            fills = List.copyOf(fills);
            selfMatchCancelledPassiveIds =
                    List.copyOf(selfMatchCancelledPassiveIds);
        }

        boolean accepted() {
            return riskResult == Result.PASS;
        }
    }

    static final class TradingSystem {

        final OrderStore orderStore =
                new OrderStore();

        final OrderBook orderBook =
                new OrderBook();

        final RiskEngine riskEngine;
        final MatchingEngine matchingEngine =
                new MatchingEngine();

        final PositionService positions =
                new PositionService();

        private final AtomicLong nextExchangeId =
                new AtomicLong(1_000_000L);

        TradingSystem(
                RiskEngine riskEngine) {

            this.riskEngine =
                    Objects.requireNonNull(riskEngine);
        }

        /**
         * End-to-end:
         *
         * identity -> risk reservation -> ACK -> match/rest -> fills ->
         * release open-order risk -> update position.
         */
        SubmitResult submit(
                Order order) {

            Objects.requireNonNull(order);

            if (orderStore.isDuplicate(
                    order.clOrdId)) {

                order.reject();

                return new SubmitResult(
                        Result.BREACH,
                        order,
                        List.of(),
                        List.of());
            }

            /*
             * Authoritative lifecycle store retains even rejected orders
             * after they have been accepted into this process.
             */
            orderStore.put(order);

            Exposure reservation =
                    Exposure.forOrder(
                            order,
                            order.origQty);

            Result risk =
                    riskEngine.check(
                            reservation);

            if (risk != Result.PASS) {

                order.reject();

                return new SubmitResult(
                        risk,
                        order,
                        List.of(),
                        List.of());
            }

            orderStore.link(
                    order,
                    String.valueOf(
                            nextExchangeId
                                    .getAndIncrement()));

            MatchOutcome outcome =
                    matchingEngine.matchDetailed(
                            order,
                            orderBook);

            /*
             * Every fill converts open-order risk into execution/position.
             * Release BOTH aggressor and passive open-order reservations
             * by the executed quantity.
             */
            for (Fill fill
                    : outcome.fills()) {

                Order aggressor =
                        orderStore.byClOrd(
                                fill.aggressorClOrdId());

                Order passive =
                        orderStore.byClOrd(
                                fill.passiveClOrdId());

                if (aggressor == null
                        || passive == null) {

                    throw new IllegalStateException(
                            "fill references unknown order");
                }

                riskEngine.release(
                        Exposure.forOrder(
                                aggressor,
                                fill.qty()));

                riskEngine.release(
                        Exposure.forOrder(
                                passive,
                                fill.qty()));

                positions.applyFill(
                        aggressor,
                        fill.qty());

                positions.applyFill(
                        passive,
                        fill.qty());
            }

            /*
             * SMP cancellations remove passive resting orders.
             * Release their remaining reserved open-order exposure.
             */
            for (String passiveId
                    : outcome
                    .selfMatchCancelledPassiveIds()) {

                Order passive =
                        orderStore.byClOrd(
                                passiveId);

                if (passive != null
                        && passive.leavesQty > 0) {

                    riskEngine.release(
                            Exposure.forOrder(
                                    passive,
                                    passive.leavesQty));
                }
            }

            /*
             * If incoming did not rest (IOC/market/FOK cancellation),
             * release the unexecuted reserved remainder.
             */
            if (order.state == OrderState.CANCELLED
                    && order.leavesQty > 0
                    && order.level == null) {

                riskEngine.release(
                        Exposure.forOrder(
                                order,
                                order.leavesQty));
            }

            return new SubmitResult(
                    Result.PASS,
                    order,
                    outcome.fills(),
                    outcome
                            .selfMatchCancelledPassiveIds());
        }

        boolean cancel(
                String clOrdId) {

            Order order =
                    orderStore.byClOrd(
                            clOrdId);

            if (order == null
                    || order.level == null) {

                return false;
            }

            long qtyToRelease =
                    order.leavesQty;

            boolean cancelled =
                    orderBook.cancel(
                            clOrdId);

            if (!cancelled) {
                return false;
            }

            riskEngine.release(
                    Exposure.forOrder(
                            order,
                            qtyToRelease));

            return true;
        }
    }

    // ========================================================================
    // 11. GENERIC LRU CACHE
    // ========================================================================

    static final class Node<K, V> {

        K key;
        V value;

        Node<K, V> prev;
        Node<K, V> next;

        Node() {}

        Node(
                K key,
                V value) {

            this.key = key;
            this.value = value;
        }
    }

    static final class LRUCache<K, V> {

        private final int capacity;

        private final Map<K, Node<K, V>> map =
                new HashMap<>();

        /*
         * head.next = MRU
         * tail.prev = LRU
         */
        private final Node<K, V> head =
                new Node<>();

        private final Node<K, V> tail =
                new Node<>();

        LRUCache(int capacity) {

            if (capacity <= 0) {
                throw new IllegalArgumentException(
                        "capacity must be > 0");
            }

            this.capacity = capacity;

            head.next = tail;
            tail.prev = head;
        }

        V get(K key) {

            Node<K, V> node =
                    map.get(key);

            if (node == null) {
                return null;
            }

            moveToHead(node);
            return node.value;
        }

        void put(
                K key,
                V value) {

            Node<K, V> node =
                    map.get(key);

            if (node != null) {

                node.value = value;
                moveToHead(node);
                return;
            }

            node =
                    new Node<>(
                            key,
                            value);

            map.put(
                    key,
                    node);

            addToHead(
                    node);

            if (map.size()
                    > capacity) {

                Node<K, V> lru =
                        removeTail();

                map.remove(
                        lru.key);
            }
        }

        boolean remove(K key) {

            Node<K, V> node =
                    map.remove(key);

            if (node == null) {
                return false;
            }

            removeNode(node);
            return true;
        }

        V computeIfAbsent(
                K key,
                Function<K, V> factory) {

            Node<K, V> existing =
                    map.get(key);

            if (existing != null) {

                moveToHead(existing);
                return existing.value;
            }

            V value =
                    factory.apply(key);

            put(key, value);
            return value;
        }

        int size() {
            return map.size();
        }

        private void addToHead(
                Node<K, V> node) {

            node.prev = head;
            node.next = head.next;

            head.next.prev = node;
            head.next = node;
        }

        private void removeNode(
                Node<K, V> node) {

            node.prev.next =
                    node.next;

            node.next.prev =
                    node.prev;
        }

        private void moveToHead(
                Node<K, V> node) {

            removeNode(node);
            addToHead(node);
        }

        private Node<K, V> removeTail() {

            Node<K, V> lru =
                    tail.prev;

            if (lru == head) {
                throw new IllegalStateException(
                        "cannot remove sentinel");
            }

            removeNode(lru);
            return lru;
        }
    }

    // ========================================================================
    // 12. OUT-OF-ORDER SEQUENCE RECONSTRUCTION
    // ========================================================================

    static final class ReorderBuffer<T> {

        private long nextExpected;

        private final TreeMap<Long, T> pending =
                new TreeMap<>();

        ReorderBuffer(
                long firstExpected) {

            this.nextExpected =
                    firstExpected;
        }

        List<T> receive(
                long seq,
                T message) {

            List<T> ready =
                    new ArrayList<>();

            if (seq < nextExpected) {
                return ready;
            }

            pending.putIfAbsent(
                    seq,
                    message);

            while (true) {

                T next =
                        pending.remove(
                                nextExpected);

                if (next == null) {
                    break;
                }

                ready.add(next);
                nextExpected++;
            }

            return ready;
        }

        long nextExpected() {
            return nextExpected;
        }

        int pendingCount() {
            return pending.size();
        }
    }

    // ========================================================================
    // 13. BOUNDED DEDUP
    // ========================================================================

    static final class BoundedDedupSet<K> {

        private final Map<K, Boolean> seen;

        BoundedDedupSet(
                int capacity) {

            if (capacity <= 0) {
                throw new IllegalArgumentException(
                        "capacity must be > 0");
            }

            /*
             * insertion-order bounded remembered set.
             *
             * This is NOT exact forever:
             * after eviction, an old ID can be accepted again.
             */
            seen =
                    new LinkedHashMap<>(
                            capacity,
                            0.75f,
                            false) {

                        @Override
                        protected boolean removeEldestEntry(
                                Map.Entry<K, Boolean> eldest) {

                            return size() > capacity;
                        }
                    };
        }

        boolean firstTime(K key) {

            if (seen.containsKey(key)) {
                return false;
            }

            seen.put(
                    key,
                    Boolean.TRUE);

            return true;
        }

        int size() {
            return seen.size();
        }
    }

    // ========================================================================
    // 14. RESILIENT FAN-OUT AGGREGATOR (SEPARATE FROM MATCHING HOT PATH)
    // ========================================================================

    record Request(
            String key,
            String payload) {}

    record ResultA(
            String value) {

        static final ResultA EMPTY =
                new ResultA("");
    }

    record ResultB(
            String value) {

        static final ResultB EMPTY =
                new ResultB("");
    }

    record ResultC(
            String value) {

        static final ResultC EMPTY =
                new ResultC("");
    }

    record AggregatedResult(
            String key,
            String merged,
            boolean partial) {}

    @FunctionalInterface
    interface BackendService<T> {
        T call(Request request)
                throws Exception;
    }

    static final class TransientServiceException
            extends RuntimeException {

        TransientServiceException(
                String message) {

            super(message);
        }
    }

    /**
     * In-process single-flight:
     *
     * one key -> one CompletableFuture
     *
     * Concurrent duplicates join the same computation, so the durable
     * persistence callback is invoked once per successful key in this JVM.
     *
     * Production distributed idempotency would require a durable/shared
     * atomic reservation/transaction.
     */
    static final class IdempotencyRegistry<V> {

        private final ConcurrentHashMap<
                String,
                CompletableFuture<V>>
                slots =
                new ConcurrentHashMap<>();

        Reservation<V> reserve(
                String key) {

            CompletableFuture<V> mine =
                    new CompletableFuture<>();

            CompletableFuture<V> existing =
                    slots.putIfAbsent(
                            key,
                            mine);

            if (existing == null) {
                return new Reservation<>(
                        true,
                        mine);
            }

            return new Reservation<>(
                    false,
                    existing);
        }

        void removeOnFailure(
                String key,
                CompletableFuture<V> future) {

            slots.remove(
                    key,
                    future);
        }

        record Reservation<V>(
                boolean owner,
                CompletableFuture<V> future) {}
    }

    static final class AggregatorService
            implements AutoCloseable {

        private static final long PER_SERVICE_MS =
                200L;

        private static final long TOTAL_MS =
                500L;

        private final ExecutorService poolA =
                boundedPool(
                        "svc-A",
                        4,
                        32);

        private final ExecutorService poolB =
                boundedPool(
                        "svc-B",
                        4,
                        32);

        private final ExecutorService poolC =
                boundedPool(
                        "svc-C",
                        2,
                        16);

        private final BackendService<ResultA> serviceA;
        private final BackendService<ResultB> serviceB;
        private final BackendService<ResultC> serviceC;

        private final IdempotencyRegistry<AggregatedResult>
                idempotency =
                new IdempotencyRegistry<>();

        private final ConcurrentHashMap<
                String,
                AggregatedResult>
                persisted =
                new ConcurrentHashMap<>();

        private final AtomicInteger persistCount =
                new AtomicInteger();

        AggregatorService(
                BackendService<ResultA> serviceA,
                BackendService<ResultB> serviceB,
                BackendService<ResultC> serviceC) {

            this.serviceA =
                    Objects.requireNonNull(serviceA);

            this.serviceB =
                    Objects.requireNonNull(serviceB);

            this.serviceC =
                    Objects.requireNonNull(serviceC);
        }

        AggregatedResult aggregate(
                Request request) {

            IdempotencyRegistry.Reservation<AggregatedResult>
                    reservation =
                    idempotency.reserve(
                            request.key());

            /*
             * Duplicate request:
             * join the original single-flight result.
             */
            if (!reservation.owner()) {
                return reservation.future().join();
            }

            CompletableFuture<AggregatedResult> slot =
                    reservation.future();

            try {

                AggregatedResult result =
                        compute(request);

                /*
                 * Side effect occurs before completing the idempotency future.
                 * Only the owner reaches this point.
                 */
                persist(result);

                slot.complete(result);
                return result;

            } catch (RuntimeException e) {

                slot.completeExceptionally(e);

                /*
                 * Failed attempts are removable so a later request may retry.
                 */
                idempotency.removeOnFailure(
                        request.key(),
                        slot);

                throw e;
            }
        }

        private AggregatedResult compute(
                Request request) {

            long overallDeadline =
                    System.nanoTime()
                            + TimeUnit.MILLISECONDS
                            .toNanos(TOTAL_MS);

            CompletableFuture<ResultA> futureA =
                    timedCall(
                            serviceA,
                            request,
                            poolA,
                            overallDeadline);

            CompletableFuture<ResultB> futureB =
                    timedCall(
                            serviceB,
                            request,
                            poolB,
                            overallDeadline);

            CompletableFuture<ResultC> futureC =
                    timedCall(
                            serviceC,
                            request,
                            poolC,
                            overallDeadline);

            CompletableFuture<Void> all =
                    CompletableFuture.allOf(
                            futureA,
                            futureB,
                            futureC);

            long remainingMs =
                    remainingMillis(
                            overallDeadline);

            try {

                all.get(
                        Math.max(
                                1L,
                                remainingMs),
                        TimeUnit.MILLISECONDS);

            } catch (TimeoutException e) {

                /*
                 * Overall deadline reached.
                 * Continue and inspect completed values.
                 */

            } catch (InterruptedException e) {

                Thread.currentThread()
                        .interrupt();

                throw new RuntimeException(
                        "aggregator interrupted",
                        e);

            } catch (ExecutionException e) {

                /*
                 * timedCall converts dependency failures to null,
                 * so reaching here is unexpected.
                 */
                throw new RuntimeException(
                        "unexpected aggregate failure",
                        e.getCause());
            }

            ResultA a =
                    getNowOrNull(futureA);

            ResultB b =
                    getNowOrNull(futureB);

            ResultC c =
                    getNowOrNull(futureC);

            if (a == null) {
                throw new RuntimeException(
                        "Service A is required");
            }

            ResultB safeB =
                    b == null
                            ? ResultB.EMPTY
                            : b;

            ResultC safeC =
                    c == null
                            ? ResultC.EMPTY
                            : c;

            boolean partial =
                    b == null
                            || c == null;

            String merged =
                    a.value()
                            + "|"
                            + safeB.value()
                            + "|"
                            + safeC.value();

            return new AggregatedResult(
                    request.key(),
                    merged,
                    partial);
        }

        private <T> CompletableFuture<T> timedCall(
                BackendService<T> service,
                Request request,
                Executor executor,
                long overallDeadline) {

            long dependencyDeadline =
                    Math.min(
                            overallDeadline,
                            System.nanoTime()
                                    + TimeUnit.MILLISECONDS
                                    .toNanos(PER_SERVICE_MS));

            return CompletableFuture
                    .supplyAsync(
                            () -> callWithRetry(
                                    service,
                                    request,
                                    dependencyDeadline,
                                    3),
                            executor)
                    /*
                     * IMPORTANT:
                     * orTimeout bounds what callers wait for, but does not
                     * guarantee the underlying remote call is physically stopped.
                     * Real clients should use native deadlines/cancellation too.
                     */
                    .orTimeout(
                            PER_SERVICE_MS,
                            TimeUnit.MILLISECONDS)
                    .exceptionally(
                            ignored -> null);
        }

        private <T> T callWithRetry(
                BackendService<T> service,
                Request request,
                long deadlineNanos,
                int maxAttempts) {

            int attempt = 0;
            RuntimeException last =
                    null;

            while (attempt < maxAttempts) {

                attempt++;

                if (System.nanoTime()
                        >= deadlineNanos) {

                    break;
                }

                try {
                    return service.call(
                            request);

                } catch (InterruptedException e) {

                    Thread.currentThread()
                            .interrupt();

                    throw new RuntimeException(
                            "dependency call interrupted",
                            e);

                } catch (TransientServiceException e) {

                    last = e;

                    if (attempt
                            >= maxAttempts) {

                        break;
                    }

                    long sleepMs =
                            Math.min(
                                    50L
                                            * (1L << (attempt - 1)),
                                    200L);

                    /*
                     * Small jitter avoids synchronized retry waves.
                     */
                    sleepMs +=
                            ThreadLocalRandom
                                    .current()
                                    .nextLong(0L, 21L);

                    long remaining =
                            remainingMillis(
                                    deadlineNanos);

                    if (remaining
                            <= sleepMs) {

                        break;
                    }

                    try {
                        Thread.sleep(
                                sleepMs);

                    } catch (InterruptedException ie) {

                        Thread.currentThread()
                                .interrupt();

                        throw new RuntimeException(
                                "retry backoff interrupted",
                                ie);
                    }

                } catch (Exception permanent) {

                    /*
                     * Non-transient exception:
                     * do not retry.
                     */
                    throw new CompletionException(
                            permanent);
                }
            }

            if (last != null) {
                throw last;
            }

            throw new TransientServiceException(
                    "dependency deadline exhausted");
        }

        private static <T> T getNowOrNull(
                CompletableFuture<T> future) {

            if (!future.isDone()) {
                return null;
            }

            try {
                return future.getNow(null);

            } catch (CompletionException e) {
                return null;
            }
        }

        private void persist(
                AggregatedResult result) {

            persisted.put(
                    result.key(),
                    result);

            persistCount.incrementAndGet();
        }

        int persistCount() {
            return persistCount.get();
        }

        AggregatedResult persisted(
                String key) {

            return persisted.get(key);
        }

        @Override
        public void close() {

            shutdown(poolA);
            shutdown(poolB);
            shutdown(poolC);
        }

        private static ExecutorService boundedPool(
                String prefix,
                int threads,
                int queueCapacity) {

            AtomicInteger n =
                    new AtomicInteger();

            ThreadFactory factory =
                    runnable -> {

                        Thread t =
                                new Thread(
                                        runnable,
                                        prefix
                                                + "-"
                                                + n.incrementAndGet());

                        t.setDaemon(true);
                        return t;
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

        private static void shutdown(
                ExecutorService executor) {

            executor.shutdown();

            try {

                if (!executor.awaitTermination(
                        1,
                        TimeUnit.SECONDS)) {

                    executor.shutdownNow();
                }

            } catch (InterruptedException e) {

                Thread.currentThread()
                        .interrupt();

                executor.shutdownNow();
            }
        }
    }

    // ========================================================================
    // 15. HELPERS
    // ========================================================================

    private static long multiplyNonNegative(
            long a,
            long b) {

        if (a < 0 || b < 0) {
            throw new IllegalArgumentException(
                    "values must be non-negative");
        }

        return Math.multiplyExact(
                a,
                b);
    }

    private static long remainingMillis(
            long deadlineNanos) {

        long remaining =
                deadlineNanos
                        - System.nanoTime();

        if (remaining <= 0) {
            return 0L;
        }

        return Math.max(
                1L,
                TimeUnit.NANOSECONDS
                        .toMillis(remaining));
    }

    private static void check(
            boolean condition,
            String message) {

        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void section(
            String title) {

        System.out.println();
        System.out.println(
                "============================================================");
        System.out.println(title);
        System.out.println(
                "============================================================");
    }

    // ========================================================================
    // 16. RUNNABLE TEST / DEMO SUITE
    // ========================================================================

    public static void main(String[] args)
            throws Exception {

        section("1. ORDER BOOK + RISK + MATCHING + POSITION");

        testIntegratedTradingFlow();

        section("2. FOK + IOC + SELF-MATCH PREVENTION");

        testTimeInForceAndSmp();

        section("3. EXPLICIT CANCEL + RISK RELEASE");

        testCancel();

        section("4. LRU CACHE");

        testLru();

        section("5. OUT-OF-ORDER RECONSTRUCTION");

        testReorderBuffer();

        section("6. BOUNDED DEDUP");

        testBoundedDedup();

        section("7. RESILIENT FAN-OUT + IDEMPOTENCY");

        testAggregator();

        section("8. ALL TESTS");

        System.out.println(
                "ALL INTEGRATED SMOKE TESTS PASSED.");
    }

    private static RiskEngine newRiskEngine(
            KillSwitchCheck killSwitch,
            long maxQty,
            long maxNotional,
            String... firms) {

        RiskEngine engine =
                new RiskEngine();

        for (String firm : firms) {

            engine.register(
                    firm,
                    new ExposureGroup(
                            List.of(
                                    killSwitch,
                                    new MaxQtyCheck(),
                                    new MaxNotionalCheck()),
                            new RiskLimit(
                                    maxQty,
                                    maxNotional)));
        }

        return engine;
    }

    private static Order order(
            String id,
            Side side,
            long price,
            long qty,
            long rank,
            String firm,
            TimeInForce tif) {

        return new Order(
                id,
                side,
                price,
                qty,
                rank,
                firm,
                tif);
    }

    private static void testIntegratedTradingFlow() {

        KillSwitchCheck kill =
                new KillSwitchCheck();

        RiskEngine risk =
                newRiskEngine(
                        kill,
                        10_000,
                        5_000_000,
                        "FIRM-A",
                        "FIRM-B");

        TradingSystem system =
                new TradingSystem(risk);

        /*
         * Rest two SELL orders.
         */
        SubmitResult s1 =
                system.submit(
                        order(
                                "S1",
                                Side.SELL,
                                101,
                                50,
                                1,
                                "FIRM-B",
                                TimeInForce.GTC));

        SubmitResult s2 =
                system.submit(
                        order(
                                "S2",
                                Side.SELL,
                                102,
                                40,
                                2,
                                "FIRM-B",
                                TimeInForce.GTC));

        check(s1.accepted(), "S1 must pass risk");
        check(s2.accepted(), "S2 must pass risk");
        check(system.orderBook.bestAsk() == 101L,
                "best ask should be 101");

        /*
         * BUY 70 @ 102:
         * 50 trades at passive 101
         * 20 trades at passive 102
         *
         * S2 remains with 20 at the SAME priority.
         */
        SubmitResult buy =
                system.submit(
                        order(
                                "B1",
                                Side.BUY,
                                102,
                                70,
                                3,
                                "FIRM-A",
                                TimeInForce.GTC));

        check(buy.fills().size() == 2,
                "B1 should create two fills");

        check(buy.fills().get(0).price() == 101L,
                "first fill must execute at passive price 101");

        check(buy.fills().get(0).qty() == 50L,
                "first fill qty should be 50");

        check(buy.fills().get(1).price() == 102L,
                "second fill must execute at passive price 102");

        check(buy.fills().get(1).qty() == 20L,
                "second fill qty should be 20");

        Order s2Order =
                system.orderStore.byClOrd("S2");

        check(s2Order.leavesQty == 20L,
                "S2 should have 20 leaves");

        check(system.orderBook.bestAsk() == 102L,
                "best ask should now be 102");

        check(system.orderBook.bestAskLevel().totalQty == 20L,
                "ask 102 aggregate should be 20");

        check(system.positions.netPosition("FIRM-A") == 70L,
                "FIRM-A position should be +70");

        check(system.positions.netPosition("FIRM-B") == -70L,
                "FIRM-B position should be -70");

        /*
         * Only S2's unfilled 20 remains as open order exposure.
         */
        ExposureState firmB =
                risk.snapshot("FIRM-B");

        check(firmB.openQty == 20L,
                "FIRM-B open risk qty should be 20");

        check(firmB.openNotional == 2_040L,
                "FIRM-B open notional should be 20*102");

        ExposureState firmA =
                risk.snapshot("FIRM-A");

        check(firmA.openQty == 0L,
                "fully executed B1 should leave no open-order risk");

        System.out.println(
                system.orderBook.snapshot());

        System.out.println(
                "Fills: " + buy.fills());

        System.out.println(
                "FIRM-A position="
                        + system.positions.netPosition("FIRM-A")
                        + ", FIRM-B position="
                        + system.positions.netPosition("FIRM-B"));
    }

    private static void testTimeInForceAndSmp() {

        KillSwitchCheck kill =
                new KillSwitchCheck();

        RiskEngine risk =
                newRiskEngine(
                        kill,
                        100_000,
                        100_000_000,
                        "A",
                        "B");

        TradingSystem system =
                new TradingSystem(risk);

        system.submit(
                order(
                        "ASK-B-1",
                        Side.SELL,
                        100,
                        10,
                        1,
                        "B",
                        TimeInForce.GTC));

        /*
         * FOK requests 20 but only 10 available:
         * ZERO execution.
         */
        SubmitResult fok =
                system.submit(
                        order(
                                "BUY-A-FOK",
                                Side.BUY,
                                100,
                                20,
                                2,
                                "A",
                                TimeInForce.FOK));

        check(fok.fills().isEmpty(),
                "FOK must execute zero if full qty unavailable");

        check(fok.order().state == OrderState.CANCELLED,
                "unfillable FOK should cancel");

        check(system.orderBook.findResting("ASK-B-1") != null,
                "FOK failure must not mutate resting liquidity");

        /*
         * IOC takes 10 and cancels the remaining 10.
         */
        SubmitResult ioc =
                system.submit(
                        order(
                                "BUY-A-IOC",
                                Side.BUY,
                                100,
                                20,
                                3,
                                "A",
                                TimeInForce.IOC));

        check(ioc.fills().size() == 1,
                "IOC should fill available liquidity");

        check(ioc.fills().get(0).qty() == 10L,
                "IOC should fill 10");

        check(ioc.order().leavesQty == 10L,
                "IOC should retain cancelled leaves quantity");

        check(ioc.order().state == OrderState.CANCELLED,
                "IOC remainder must cancel");

        check(system.orderBook.bestAsk() == null,
                "ask book should now be empty");

        /*
         * SMP cancel-passive:
         * same-firm passive is cancelled, then matching continues to next level.
         */
        system.submit(
                order(
                        "SELF-ASK",
                        Side.SELL,
                        101,
                        5,
                        4,
                        "A",
                        TimeInForce.GTC));

        system.submit(
                order(
                        "OTHER-ASK",
                        Side.SELL,
                        102,
                        5,
                        5,
                        "B",
                        TimeInForce.GTC));

        SubmitResult smp =
                system.submit(
                        order(
                                "A-BUY",
                                Side.BUY,
                                102,
                                5,
                                6,
                                "A",
                                TimeInForce.GTC));

        check(
                smp.selfMatchCancelledPassiveIds()
                        .equals(List.of("SELF-ASK")),
                "SMP should cancel passive self order");

        check(smp.fills().size() == 1,
                "after SMP cancel, order should match next eligible liquidity");

        check(
                smp.fills().get(0)
                        .passiveClOrdId()
                        .equals("OTHER-ASK"),
                "fill should be against OTHER-ASK");

        /*
         * Kill switch demonstration.
         */
        kill.activate();

        SubmitResult killed =
                system.submit(
                        order(
                                "KILLED",
                                Side.BUY,
                                99,
                                1,
                                7,
                                "A",
                                TimeInForce.GTC));

        check(killed.riskResult() == Result.KILL,
                "kill switch must reject new risk");

        check(killed.order().state == OrderState.REJECTED,
                "killed order must be rejected");

        kill.deactivate();

        System.out.println(
                "FOK/IOC/SMP/kill-switch checks passed.");
    }

    private static void testCancel() {

        KillSwitchCheck kill =
                new KillSwitchCheck();

        RiskEngine risk =
                newRiskEngine(
                        kill,
                        1_000,
                        1_000_000,
                        "F1");

        TradingSystem system =
                new TradingSystem(risk);

        system.submit(
                order(
                        "CANCEL-ME",
                        Side.BUY,
                        10,
                        25,
                        1,
                        "F1",
                        TimeInForce.GTC));

        ExposureState before =
                risk.snapshot("F1");

        check(before.openQty == 25L,
                "resting order must reserve qty");

        check(system.cancel("CANCEL-ME"),
                "cancel should succeed");

        check(system.orderBook.findResting("CANCEL-ME") == null,
                "cancelled order must leave the book");

        check(
                system.orderStore
                        .byClOrd("CANCEL-ME")
                        .state
                        == OrderState.CANCELLED,
                "OMS store keeps cancelled lifecycle object");

        ExposureState after =
                risk.snapshot("F1");

        check(after.openQty == 0L,
                "cancel must release open qty");

        check(after.openNotional == 0L,
                "cancel must release open notional");

        System.out.println(
                "Cancel released book liquidity and risk.");
    }

    private static void testLru() {

        LRUCache<String, Integer> cache =
                new LRUCache<>(2);

        cache.put("A", 1);
        cache.put("B", 2);

        check(cache.get("A") == 1,
                "A should exist");

        /*
         * A became MRU, so B is LRU.
         */
        cache.put("C", 3);

        check(cache.get("B") == null,
                "B should have been evicted");

        check(cache.get("A") == 1,
                "A should remain");

        check(cache.get("C") == 3,
                "C should remain");

        check(cache.size() == 2,
                "LRU size must respect capacity");

        System.out.println(
                "LRU get/put/eviction passed.");
    }

    private static void testReorderBuffer() {

        ReorderBuffer<String> buffer =
                new ReorderBuffer<>(1L);

        check(
                buffer.receive(3L, "C").isEmpty(),
                "3 must buffer while expecting 1");

        check(
                buffer.receive(2L, "B").isEmpty(),
                "2 must buffer while expecting 1");

        List<String> emitted =
                buffer.receive(1L, "A");

        check(
                emitted.equals(
                        List.of("A", "B", "C")),
                "1 should close hole and drain 1,2,3");

        check(buffer.nextExpected() == 4L,
                "next expected should be 4");

        check(buffer.pendingCount() == 0,
                "buffer should be empty");

        System.out.println(
                "Reorder output: " + emitted);
    }

    private static void testBoundedDedup() {

        BoundedDedupSet<String> dedup =
                new BoundedDedupSet<>(2);

        check(dedup.firstTime("E1"),
                "E1 first arrival");

        check(!dedup.firstTime("E1"),
                "E1 duplicate while retained");

        check(dedup.firstTime("E2"),
                "E2 first arrival");

        check(dedup.firstTime("E3"),
                "E3 first arrival");

        check(dedup.size() == 2,
                "bounded set must remain at capacity");

        /*
         * E1 was eldest and has been evicted.
         * This demonstrates bounded—not eternal—dedup.
         */
        check(dedup.firstTime("E1"),
                "evicted E1 can be accepted again");

        System.out.println(
                "Bounded dedup retention semantics passed.");
    }

    private static void testAggregator()
            throws Exception {

        AtomicInteger bAttempts =
                new AtomicInteger();

        BackendService<ResultA> serviceA =
                req -> new ResultA(
                        "A:" + req.payload());

        BackendService<ResultB> serviceB =
                req -> {

                    /*
                     * One transient failure, then success.
                     */
                    if (bAttempts.incrementAndGet() == 1) {
                        throw new TransientServiceException(
                                "temporary B failure");
                    }

                    return new ResultB(
                            "B:" + req.payload());
                };

        BackendService<ResultC> serviceC =
                req -> {

                    /*
                     * Optional dependency exceeds 200ms service budget.
                     * It demonstrates partial degradation.
                     */
                    Thread.sleep(300L);

                    return new ResultC(
                            "C:" + req.payload());
                };

        try (AggregatorService service =
                     new AggregatorService(
                             serviceA,
                             serviceB,
                             serviceC)) {

            Request request =
                    new Request(
                            "idem-1",
                            "payload");

            /*
             * Call twice concurrently with the same key.
             * Single-flight idempotency should persist only once.
             */
            ExecutorService callers =
                    Executors.newFixedThreadPool(2);

            try {

                Future<AggregatedResult> r1 =
                        callers.submit(
                                () -> service.aggregate(request));

                Future<AggregatedResult> r2 =
                        callers.submit(
                                () -> service.aggregate(request));

                AggregatedResult a =
                        r1.get(
                                2,
                                TimeUnit.SECONDS);

                AggregatedResult b =
                        r2.get(
                                2,
                                TimeUnit.SECONDS);

                check(a.equals(b),
                        "same idempotency key must return same result");

                check(a.partial(),
                        "slow optional C should produce partial result");

                check(
                        a.merged()
                                .startsWith(
                                        "A:payload|B:payload|"),
                        "critical A and retried B should contribute");

                check(service.persistCount() == 1,
                        "durable side effect should occur once in-process");

                check(
                        service.persisted("idem-1")
                                .equals(a),
                        "persisted result should match");

                System.out.println(
                        "Aggregator result: " + a);

                System.out.println(
                        "Persist count="
                                + service.persistCount()
                                + ", B attempts="
                                + bAttempts.get());

            } finally {

                callers.shutdownNow();
            }
        }
    }
}
