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
 * ProductionTradingSystemV2
 *
 * A single Java 17+ file that demonstrates a production-shaped trading LLD with
 * interview-friendly standard-library data structures and explicit domain types.
 *
 * HOT PATH
 * --------
 * OrderRepository -> OpenOrderRiskLedger -> OrderBook -> MatchingEngine
 *                                          |              |
 *                                          |              +-> Fill
 *                                          +-> resting liquidity
 *
 * FILL PATH
 * ---------
 * Fill -> release open-order risk -> update position -> update order lifecycle
 *
 * SUPPORTING INFRASTRUCTURE
 * -------------------------
 * LRU cache, sequence reorder buffer, bounded dedup, resilient fan-out aggregator.
 * These are deliberately kept OUTSIDE the deterministic matching hot path.
 *
 * FIXED-POINT DECIMALS
 * --------------------
 * Price and Money store four decimal places in scaled long values:
 *
 *     101.2500 -> 1_012_500 scaled units
 *
 * Decimal parsing/formatting happens only at boundaries. Internal comparisons,
 * TreeMap ordering and notional arithmetic stay integer-based—no double money math.
 *
 * Compile:
 *   javac --release 17 ProductionTradingSystemV2.java
 *
 * Run:
 *   java ProductionTradingSystemV2
 */
public class ProductionTradingSystemV2 {

    // ======================================================================
    // 1. FIXED-POINT VALUE OBJECTS
    // ======================================================================

    /**
     * Shared fixed-point convention for this demo.
     * Four decimal places is illustrative; real venues derive scale/tick size
     * from instrument metadata.
     */
    static final class FixedPoint {
        static final int DECIMAL_PLACES = 4;
        static final long SCALE = 10_000L;

        private FixedPoint() {}

        static long parseScaled(String decimal) {
            BigDecimal value = new BigDecimal(decimal)
                    .setScale(DECIMAL_PLACES, RoundingMode.UNNECESSARY);

            return value
                    .movePointRight(DECIMAL_PLACES)
                    .longValueExact();
        }

        static String format(long scaledValue) {
            return BigDecimal.valueOf(scaledValue, DECIMAL_PLACES)
                    .setScale(DECIMAL_PLACES, RoundingMode.UNNECESSARY)
                    .toPlainString();
        }

        /** Round a positive rational number to the nearest integer, HALF_UP. */
        static long divideRoundedHalfUp(BigInteger numerator, long denominator) {
            if (denominator <= 0) {
                throw new IllegalArgumentException("denominator must be > 0");
            }

            BigInteger divisor = BigInteger.valueOf(denominator);
            BigInteger[] quotientAndRemainder = numerator.divideAndRemainder(divisor);
            BigInteger quotient = quotientAndRemainder[0];
            BigInteger remainder = quotientAndRemainder[1].abs();

            if (remainder.shiftLeft(1).compareTo(divisor.abs()) >= 0) {
                quotient = quotient.add(BigInteger.ONE);
            }

            return quotient.longValueExact();
        }
    }

    /**
     * Price stored as scaled integer units. Comparable, immutable and type-safe.
     * This prevents accidental mixing of price with quantity/timestamps/notional.
     */
    record Price(long scaledValue) implements Comparable<Price> {

        static final Price ZERO = new Price(0L);

        Price {
            if (scaledValue < 0) {
                throw new IllegalArgumentException("price cannot be negative");
            }
        }

        static Price parse(String decimal) {
            return new Price(FixedPoint.parseScaled(decimal));
        }

        static Price ofScaled(long scaledValue) {
            return new Price(scaledValue);
        }

        Money multiply(long quantity) {
            if (quantity < 0) {
                throw new IllegalArgumentException("quantity cannot be negative");
            }
            return Money.ofScaled(Math.multiplyExact(scaledValue, quantity));
        }

        static Price weightedAverage(
                Price previousAverage,
                long previousQuantity,
                Price fillPrice,
                long fillQuantity) {

            if (previousQuantity < 0 || fillQuantity <= 0) {
                throw new IllegalArgumentException("invalid weighted-average quantities");
            }

            long totalQuantity = Math.addExact(previousQuantity, fillQuantity);

            BigInteger previousContribution = BigInteger.valueOf(previousAverage.scaledValue)
                    .multiply(BigInteger.valueOf(previousQuantity));

            BigInteger fillContribution = BigInteger.valueOf(fillPrice.scaledValue)
                    .multiply(BigInteger.valueOf(fillQuantity));

            long averageScaled = FixedPoint.divideRoundedHalfUp(
                    previousContribution.add(fillContribution),
                    totalQuantity);

            return Price.ofScaled(averageScaled);
        }

        @Override
        public int compareTo(Price other) {
            return Long.compare(scaledValue, other.scaledValue);
        }

        @Override
        public String toString() {
            return FixedPoint.format(scaledValue);
        }
    }

    /** Quote-currency notional represented with the same four-decimal scale. */
    record Money(long scaledValue) implements Comparable<Money> {

        static final Money ZERO = new Money(0L);

        Money {
            if (scaledValue < 0) {
                throw new IllegalArgumentException("money cannot be negative in this model");
            }
        }

        static Money parse(String decimal) {
            return new Money(FixedPoint.parseScaled(decimal));
        }

        static Money ofScaled(long scaledValue) {
            return new Money(scaledValue);
        }

        Money add(Money other) {
            return new Money(Math.addExact(scaledValue, other.scaledValue));
        }

        Money subtractExact(Money other) {
            long result = Math.subtractExact(scaledValue, other.scaledValue);
            if (result < 0) {
                throw new IllegalStateException("money state would become negative");
            }
            return new Money(result);
        }

        @Override
        public int compareTo(Money other) {
            return Long.compare(scaledValue, other.scaledValue);
        }

        @Override
        public String toString() {
            return FixedPoint.format(scaledValue);
        }
    }

    // ======================================================================
    // 2. DOMAIN ENUMS + PRIORITY
    // ======================================================================

    enum Side {
        BUY, SELL
    }

    enum OrderType {
        LIMIT, MARKET
    }

    enum TimeInForce {
        IOC, FOK, GTC, DAY
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

        boolean isTerminal() {
            return this == FILLED || this == CANCELLED || this == REJECTED;
        }
    }

    enum RiskDecision {
        PASS,
        LIMIT_BREACH,
        KILL_SWITCH
    }

    enum SubmissionDecision {
        ACCEPTED,
        DUPLICATE_CLIENT_ORDER_ID,
        RISK_REJECTED
    }

    /** Deterministic time priority: timestamp first, sequence breaks ties. */
    record OrderPriority(long arrivalTimeNanos, long arrivalSequence)
            implements Comparable<OrderPriority> {

        @Override
        public int compareTo(OrderPriority other) {
            int byTime = Long.compare(arrivalTimeNanos, other.arrivalTimeNanos);
            return byTime != 0
                    ? byTime
                    : Long.compare(arrivalSequence, other.arrivalSequence);
        }
    }

    // ======================================================================
    // 3. ORDER AGGREGATE
    // ======================================================================

    static final class Order {

        // Identity / routing
        final String clientOrderId;
        String exchangeOrderId;
        final String instrumentId;
        final String firmId;

        // Order economics
        final Side side;
        final OrderType orderType;
        final Price limitPrice;          // non-null only for LIMIT
        final Price riskReferencePrice;  // required for MARKET risk reservation
        final long originalQuantity;
        final TimeInForce timeInForce;
        final OrderPriority priority;

        // Mutable lifecycle / fill state
        long remainingQuantity;
        long filledQuantity;
        Price averageFillPrice = Price.ZERO;
        OrderState state = OrderState.PENDING_ACK;

        // Back-pointer only while resting. It identifies the level in O(1),
        // but ArrayDeque.remove(order) is still O(orders at that level).
        PriceLevel restingLevel;

        private Order(
                String clientOrderId,
                String instrumentId,
                String firmId,
                Side side,
                OrderType orderType,
                Price limitPrice,
                Price riskReferencePrice,
                long originalQuantity,
                TimeInForce timeInForce,
                OrderPriority priority) {

            this.clientOrderId = requireText(clientOrderId, "clientOrderId");
            this.instrumentId = requireText(instrumentId, "instrumentId");
            this.firmId = requireText(firmId, "firmId");
            this.side = Objects.requireNonNull(side);
            this.orderType = Objects.requireNonNull(orderType);
            this.timeInForce = Objects.requireNonNull(timeInForce);
            this.priority = Objects.requireNonNull(priority);

            if (originalQuantity <= 0) {
                throw new IllegalArgumentException("originalQuantity must be > 0");
            }

            if (orderType == OrderType.LIMIT) {
                this.limitPrice = Objects.requireNonNull(limitPrice, "limitPrice");
                if (limitPrice.scaledValue() <= 0) {
                    throw new IllegalArgumentException("limitPrice must be > 0");
                }
                this.riskReferencePrice = limitPrice;
            } else {
                this.limitPrice = null;
                this.riskReferencePrice = Objects.requireNonNull(
                        riskReferencePrice,
                        "market order requires riskReferencePrice");
            }

            this.originalQuantity = originalQuantity;
            this.remainingQuantity = originalQuantity;
        }

        static Order limit(
                String clientOrderId,
                String instrumentId,
                String firmId,
                Side side,
                Price limitPrice,
                long quantity,
                TimeInForce timeInForce,
                OrderPriority priority) {

            return new Order(
                    clientOrderId,
                    instrumentId,
                    firmId,
                    side,
                    OrderType.LIMIT,
                    limitPrice,
                    null,
                    quantity,
                    timeInForce,
                    priority);
        }

        static Order market(
                String clientOrderId,
                String instrumentId,
                String firmId,
                Side side,
                Price riskReferencePrice,
                long quantity,
                TimeInForce timeInForce,
                OrderPriority priority) {

            return new Order(
                    clientOrderId,
                    instrumentId,
                    firmId,
                    side,
                    OrderType.MARKET,
                    null,
                    riskReferencePrice,
                    quantity,
                    timeInForce,
                    priority);
        }

        void acknowledge(String exchangeOrderId) {
            this.exchangeOrderId = requireText(exchangeOrderId, "exchangeOrderId");
            this.state = OrderState.ACKNOWLEDGED;
        }

        void applyFill(long executionQuantity, Price executionPrice) {
            Objects.requireNonNull(executionPrice);

            if (executionQuantity <= 0) {
                throw new IllegalArgumentException("executionQuantity must be > 0");
            }

            if (executionQuantity > remainingQuantity) {
                throw new IllegalArgumentException(
                        "overfill: execution=" + executionQuantity
                                + ", remaining=" + remainingQuantity);
            }

            averageFillPrice = Price.weightedAverage(
                    averageFillPrice,
                    filledQuantity,
                    executionPrice,
                    executionQuantity);

            filledQuantity = Math.addExact(filledQuantity, executionQuantity);
            remainingQuantity -= executionQuantity;

            state = remainingQuantity == 0
                    ? OrderState.FILLED
                    : OrderState.PARTIALLY_FILLED;

            assertQuantityInvariant();
        }

        void reject() {
            state = OrderState.REJECTED;
        }

        void cancelRemainingQuantity() {
            if (!isFilled()) {
                state = OrderState.CANCELLED;
            }
        }

        boolean isMarketOrder() {
            return orderType == OrderType.MARKET;
        }

        boolean isFilled() {
            return remainingQuantity == 0;
        }

        boolean isTerminal() {
            return state.isTerminal();
        }

        Price priceUsedForRisk() {
            return riskReferencePrice;
        }

        private void assertQuantityInvariant() {
            if (remainingQuantity < 0
                    || filledQuantity < 0
                    || Math.addExact(remainingQuantity, filledQuantity) != originalQuantity) {

                throw new IllegalStateException(
                        "quantity invariant violated for " + clientOrderId);
            }
        }

        @Override
        public String toString() {
            return "Order{" +
                    "clientOrderId='" + clientOrderId + '\'' +
                    ", instrument='" + instrumentId + '\'' +
                    ", firm='" + firmId + '\'' +
                    ", side=" + side +
                    ", type=" + orderType +
                    ", limitPrice=" + limitPrice +
                    ", originalQty=" + originalQuantity +
                    ", remainingQty=" + remainingQuantity +
                    ", filledQty=" + filledQuantity +
                    ", averageFillPrice=" + averageFillPrice +
                    ", tif=" + timeInForce +
                    ", state=" + state +
                    '}';
        }
    }

    // ======================================================================
    // 4. PRICE LEVEL + ORDER BOOK
    // ======================================================================

    static final class PriceLevel {

        final Side side;
        final Price price;

        /** Oldest order at the head = highest time priority at this price. */
        private final ArrayDeque<Order> restingOrders = new ArrayDeque<>();

        private long totalRemainingQuantity;

        PriceLevel(Side side, Price price) {
            this.side = Objects.requireNonNull(side);
            this.price = Objects.requireNonNull(price);
        }

        void addLast(Order order) {
            if (order.side != side || !Objects.equals(order.limitPrice, price)) {
                throw new IllegalArgumentException("order does not belong to this level");
            }
            if (order.restingLevel != null) {
                throw new IllegalStateException("order already rests on a level");
            }

            restingOrders.addLast(order);
            totalRemainingQuantity = Math.addExact(
                    totalRemainingQuantity,
                    order.remainingQuantity);
            order.restingLevel = this;
        }

        Order highestPriorityOrder() {
            return restingOrders.peekFirst();
        }

        /**
         * Remove the head completely.
         * Call BEFORE applyFill when the passive order will be fully filled,
         * because this subtracts its pre-fill remaining quantity.
         */
        Order removeHighestPriorityOrder() {
            Order removed = restingOrders.pollFirst();
            if (removed != null) {
                totalRemainingQuantity = Math.subtractExact(
                        totalRemainingQuantity,
                        removed.remainingQuantity);
                removed.restingLevel = null;
            }
            return removed;
        }

        /** Arbitrary cancellation. ArrayDeque makes this O(orders at this level). */
        boolean remove(Order order) {
            if (!restingOrders.remove(order)) {
                return false;
            }
            totalRemainingQuantity = Math.subtractExact(
                    totalRemainingQuantity,
                    order.remainingQuantity);
            order.restingLevel = null;
            return true;
        }

        /** Partial fill: order remains at the head and keeps time priority. */
        void reduceAggregateQuantity(long executedQuantity) {
            if (executedQuantity <= 0 || executedQuantity > totalRemainingQuantity) {
                throw new IllegalArgumentException("invalid aggregate quantity reduction");
            }
            totalRemainingQuantity -= executedQuantity;
        }

        long totalRemainingQuantity() {
            return totalRemainingQuantity;
        }

        int orderCount() {
            return restingOrders.size();
        }

        boolean isEmpty() {
            return restingOrders.isEmpty();
        }

        Iterable<Order> ordersInTimePriority() {
            return Collections.unmodifiableCollection(restingOrders);
        }

        @Override
        public String toString() {
            return "PriceLevel{" +
                    "side=" + side +
                    ", price=" + price +
                    ", totalRemainingQty=" + totalRemainingQuantity +
                    ", orders=" + orderCount() +
                    '}';
        }
    }

    static final class OrderBook {

        final String instrumentId;

        /** firstEntry() is the best bid because bids are descending. */
        private final NavigableMap<Price, PriceLevel> bidLevels =
                new TreeMap<>(Comparator.reverseOrder());

        /** firstEntry() is the best ask because asks are ascending. */
        private final NavigableMap<Price, PriceLevel> askLevels =
                new TreeMap<>();

        /** Resting-liquidity index only—not the authoritative OMS repository. */
        private final Map<String, Order> restingOrdersByClientId = new HashMap<>();

        OrderBook(String instrumentId) {
            this.instrumentId = requireText(instrumentId, "instrumentId");
        }

        void addRestingOrder(Order order) {
            validateBelongsToBook(order);

            if (order.isMarketOrder()) {
                throw new IllegalArgumentException("market order cannot rest");
            }
            if (order.isFilled() || order.isTerminal()) {
                throw new IllegalArgumentException("filled/terminal order cannot rest");
            }
            if (restingOrdersByClientId.containsKey(order.clientOrderId)) {
                throw new IllegalArgumentException(
                        "order already resting: " + order.clientOrderId);
            }

            NavigableMap<Price, PriceLevel> levels = levelsForSide(order.side);
            PriceLevel level = levels.computeIfAbsent(
                    order.limitPrice,
                    price -> new PriceLevel(order.side, price));

            level.addLast(order);
            restingOrdersByClientId.put(order.clientOrderId, order);
        }

        boolean cancelRestingOrder(String clientOrderId) {
            Order order = restingOrdersByClientId.get(clientOrderId);
            if (order == null || order.restingLevel == null) {
                return false;
            }

            PriceLevel level = order.restingLevel;
            if (!level.remove(order)) {
                return false;
            }

            restingOrdersByClientId.remove(clientOrderId);
            removeLevelIfEmpty(level);
            order.cancelRemainingQuantity();
            return true;
        }

        Order findRestingOrder(String clientOrderId) {
            return restingOrdersByClientId.get(clientOrderId);
        }

        Price bestBidPrice() {
            return bidLevels.isEmpty() ? null : bidLevels.firstKey();
        }

        Price bestAskPrice() {
            return askLevels.isEmpty() ? null : askLevels.firstKey();
        }

        PriceLevel bestOppositeLevel(Side incomingSide) {
            NavigableMap<Price, PriceLevel> opposite = oppositeLevels(incomingSide);
            return opposite.isEmpty() ? null : opposite.firstEntry().getValue();
        }

        Iterable<PriceLevel> oppositeLevelsInMatchPriority(Side incomingSide) {
            return Collections.unmodifiableCollection(oppositeLevels(incomingSide).values());
        }

        /**
         * MatchingEngine has already removed the head from PriceLevel.
         * This method removes only the resting-ID index and, if needed, the empty level.
         */
        void onOrderDequeuedForExecution(Order order, PriceLevel formerLevel) {
            restingOrdersByClientId.remove(order.clientOrderId);
            removeLevelIfEmpty(formerLevel);
        }

        int restingOrderCount() {
            return restingOrdersByClientId.size();
        }

        private void removeLevelIfEmpty(PriceLevel level) {
            if (level.isEmpty()) {
                levelsForSide(level.side).remove(level.price);
            }
        }

        private NavigableMap<Price, PriceLevel> levelsForSide(Side side) {
            return side == Side.BUY ? bidLevels : askLevels;
        }

        private NavigableMap<Price, PriceLevel> oppositeLevels(Side incomingSide) {
            return incomingSide == Side.BUY ? askLevels : bidLevels;
        }

        private void validateBelongsToBook(Order order) {
            if (!instrumentId.equals(order.instrumentId)) {
                throw new IllegalArgumentException(
                        "order instrument " + order.instrumentId
                                + " does not belong to book " + instrumentId);
            }
        }

        String snapshot() {
            return "OrderBook{" +
                    "instrument='" + instrumentId + '\'' +
                    ", bestBid=" + bestBidPrice() +
                    ", bestAsk=" + bestAskPrice() +
                    ", restingOrders=" + restingOrderCount() +
                    ", bids=" + bidLevels.values() +
                    ", asks=" + askLevels.values() +
                    '}';
        }
    }

    /** Reusable registry: one independent order book per instrument. */
    static final class OrderBookRegistry {
        private final Map<String, OrderBook> booksByInstrument = new HashMap<>();

        OrderBook getOrCreate(String instrumentId) {
            return booksByInstrument.computeIfAbsent(instrumentId, OrderBook::new);
        }

        OrderBook get(String instrumentId) {
            return booksByInstrument.get(instrumentId);
        }
    }

    // ======================================================================
    // 5. ORDER REPOSITORY
    // ======================================================================

    interface OrderRepository {
        void add(Order order);
        void linkExchangeOrderId(Order order, String exchangeOrderId);
        Order findByClientOrderId(String clientOrderId);
        Order findByExchangeOrderId(String exchangeOrderId);
        boolean containsClientOrderId(String clientOrderId);
        void remove(Order order);
        int size();
    }

    static final class InMemoryOrderRepository implements OrderRepository {

        private final Map<String, Order> ordersByClientId = new HashMap<>();
        private final Map<String, Order> ordersByExchangeId = new HashMap<>();

        @Override
        public void add(Order order) {
            Order previous = ordersByClientId.putIfAbsent(order.clientOrderId, order);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "duplicate clientOrderId: " + order.clientOrderId);
            }
        }

        @Override
        public void linkExchangeOrderId(Order order, String exchangeOrderId) {
            exchangeOrderId = requireText(exchangeOrderId, "exchangeOrderId");

            Order previous = ordersByExchangeId.putIfAbsent(exchangeOrderId, order);
            if (previous != null && previous != order) {
                throw new IllegalArgumentException(
                        "duplicate exchangeOrderId: " + exchangeOrderId);
            }

            order.acknowledge(exchangeOrderId);
        }

        @Override
        public Order findByClientOrderId(String clientOrderId) {
            return ordersByClientId.get(clientOrderId);
        }

        @Override
        public Order findByExchangeOrderId(String exchangeOrderId) {
            return ordersByExchangeId.get(exchangeOrderId);
        }

        @Override
        public boolean containsClientOrderId(String clientOrderId) {
            return ordersByClientId.containsKey(clientOrderId);
        }

        @Override
        public void remove(Order order) {
            ordersByClientId.remove(order.clientOrderId, order);
            if (order.exchangeOrderId != null) {
                ordersByExchangeId.remove(order.exchangeOrderId, order);
            }
        }

        @Override
        public int size() {
            return ordersByClientId.size();
        }
    }

    // ======================================================================
    // 6. MATCHING
    // ======================================================================

    record Fill(
            String aggressorClientOrderId,
            String passiveClientOrderId,
            String instrumentId,
            Price executionPrice,
            long executionQuantity) {

        Fill {
            requireText(aggressorClientOrderId, "aggressorClientOrderId");
            requireText(passiveClientOrderId, "passiveClientOrderId");
            requireText(instrumentId, "instrumentId");
            Objects.requireNonNull(executionPrice);
            if (executionQuantity <= 0) {
                throw new IllegalArgumentException("executionQuantity must be > 0");
            }
        }
    }

    enum SelfMatchAction {
        CANCEL_PASSIVE
    }

    interface SelfMatchPolicy {
        boolean isSelfMatch(Order aggressor, Order passive);
        SelfMatchAction action();
    }

    static final class SameFirmCancelPassivePolicy implements SelfMatchPolicy {
        @Override
        public boolean isSelfMatch(Order aggressor, Order passive) {
            return Objects.equals(aggressor.firmId, passive.firmId);
        }

        @Override
        public SelfMatchAction action() {
            return SelfMatchAction.CANCEL_PASSIVE;
        }
    }

    record MatchResult(
            List<Fill> fills,
            List<String> selfMatchCancelledPassiveOrderIds) {

        MatchResult {
            fills = List.copyOf(fills);
            selfMatchCancelledPassiveOrderIds =
                    List.copyOf(selfMatchCancelledPassiveOrderIds);
        }
    }

    static final class MatchingEngine {

        private final SelfMatchPolicy selfMatchPolicy;

        MatchingEngine(SelfMatchPolicy selfMatchPolicy) {
            this.selfMatchPolicy = Objects.requireNonNull(selfMatchPolicy);
        }

        MatchResult matchIncomingOrder(Order incomingOrder, OrderBook orderBook) {
            Objects.requireNonNull(incomingOrder);
            Objects.requireNonNull(orderBook);

            if (!incomingOrder.instrumentId.equals(orderBook.instrumentId)) {
                throw new IllegalArgumentException("order/book instrument mismatch");
            }
            if (incomingOrder.isFilled() || incomingOrder.isTerminal()) {
                throw new IllegalArgumentException("incoming order already terminal");
            }

            List<Fill> fills = new ArrayList<>();
            List<String> selfMatchCancellations = new ArrayList<>();

            // FOK is all-or-none. Pre-check before mutating the book.
            if (incomingOrder.timeInForce == TimeInForce.FOK
                    && !hasEnoughEligibleLiquidity(incomingOrder, orderBook)) {
                incomingOrder.cancelRemainingQuantity();
                return new MatchResult(fills, selfMatchCancellations);
            }

            while (incomingOrder.remainingQuantity > 0) {
                PriceLevel bestPassiveLevel = orderBook.bestOppositeLevel(incomingOrder.side);

                if (bestPassiveLevel == null
                        || !crosses(incomingOrder, bestPassiveLevel.price)) {
                    break;
                }

                while (incomingOrder.remainingQuantity > 0
                        && !bestPassiveLevel.isEmpty()) {

                    Order passiveOrder = bestPassiveLevel.highestPriorityOrder();

                    if (selfMatchPolicy.isSelfMatch(incomingOrder, passiveOrder)) {
                        handleSelfMatch(
                                passiveOrder,
                                orderBook,
                                selfMatchCancellations);
                        continue;
                    }

                    long executionQuantity = Math.min(
                            incomingOrder.remainingQuantity,
                            passiveOrder.remainingQuantity);

                    // This exercise executes at the resting/passive price.
                    Price executionPrice = bestPassiveLevel.price;

                    boolean passiveWillBeFilled =
                            executionQuantity == passiveOrder.remainingQuantity;

                    if (passiveWillBeFilled) {
                        // Remove BEFORE fill because level removal subtracts pre-fill leaves.
                        PriceLevel formerLevel = passiveOrder.restingLevel;
                        Order removed = formerLevel.removeHighestPriorityOrder();

                        if (removed != passiveOrder) {
                            throw new IllegalStateException("price-time queue corrupted");
                        }

                        orderBook.onOrderDequeuedForExecution(passiveOrder, formerLevel);
                    } else {
                        // Partial passive fill: same order remains at head, keeping priority.
                        bestPassiveLevel.reduceAggregateQuantity(executionQuantity);
                    }

                    passiveOrder.applyFill(executionQuantity, executionPrice);
                    incomingOrder.applyFill(executionQuantity, executionPrice);

                    fills.add(new Fill(
                            incomingOrder.clientOrderId,
                            passiveOrder.clientOrderId,
                            incomingOrder.instrumentId,
                            executionPrice,
                            executionQuantity));
                }
            }

            applyRemainderPolicy(incomingOrder, orderBook);
            return new MatchResult(fills, selfMatchCancellations);
        }

        private void handleSelfMatch(
                Order passiveOrder,
                OrderBook orderBook,
                List<String> cancellations) {

            if (selfMatchPolicy.action() != SelfMatchAction.CANCEL_PASSIVE) {
                throw new UnsupportedOperationException("unsupported self-match action");
            }

            if (!orderBook.cancelRestingOrder(passiveOrder.clientOrderId)) {
                throw new IllegalStateException("failed to cancel passive self-match order");
            }

            cancellations.add(passiveOrder.clientOrderId);
        }

        private void applyRemainderPolicy(Order order, OrderBook orderBook) {
            if (order.isFilled()) {
                return;
            }

            if (order.isMarketOrder() || order.timeInForce == TimeInForce.IOC) {
                order.cancelRemainingQuantity();
                return;
            }

            if (order.timeInForce == TimeInForce.FOK) {
                throw new IllegalStateException("FOK partially executed");
            }

            // GTC / DAY limit remainder rests.
            orderBook.addRestingOrder(order);
        }

        private boolean crosses(Order incomingOrder, Price passivePrice) {
            if (incomingOrder.isMarketOrder()) {
                return true;
            }

            int comparison = incomingOrder.limitPrice.compareTo(passivePrice);
            return incomingOrder.side == Side.BUY
                    ? comparison >= 0
                    : comparison <= 0;
        }

        private boolean hasEnoughEligibleLiquidity(Order incomingOrder, OrderBook orderBook) {
            long quantityStillRequired = incomingOrder.remainingQuantity;

            for (PriceLevel level : orderBook.oppositeLevelsInMatchPriority(incomingOrder.side)) {
                if (!crosses(incomingOrder, level.price)) {
                    break;
                }

                for (Order passiveOrder : level.ordersInTimePriority()) {
                    if (selfMatchPolicy.isSelfMatch(incomingOrder, passiveOrder)) {
                        continue;
                    }

                    quantityStillRequired -= passiveOrder.remainingQuantity;
                    if (quantityStillRequired <= 0) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    // ======================================================================
    // 7. RISK MODEL + OPEN-ORDER RISK LEDGER
    // ======================================================================

    record RiskExposure(
            String riskGroupId,
            long openQuantity,
            Money openNotional) {

        RiskExposure {
            requireText(riskGroupId, "riskGroupId");
            if (openQuantity < 0) {
                throw new IllegalArgumentException("openQuantity cannot be negative");
            }
            Objects.requireNonNull(openNotional);
        }

        static RiskExposure forOrderQuantity(Order order, long quantity) {
            if (quantity < 0 || quantity > order.originalQuantity) {
                throw new IllegalArgumentException("invalid risk quantity");
            }

            return new RiskExposure(
                    order.firmId,
                    quantity,
                    order.priceUsedForRisk().multiply(quantity));
        }
    }

    record RiskLimit(long maxOpenQuantity, Money maxOpenNotional) {
        RiskLimit {
            if (maxOpenQuantity < 0) {
                throw new IllegalArgumentException("maxOpenQuantity cannot be negative");
            }
            Objects.requireNonNull(maxOpenNotional);
        }
    }

    interface RiskCheck {
        RiskDecision reserve(RiskExposure exposure, RiskExposureState state, RiskLimit limit);
        void release(RiskExposure exposure, RiskExposureState state);
    }

    static final class RiskExposureState {
        private long openQuantity;
        private Money openNotional = Money.ZERO;

        private long savedOpenQuantity;
        private Money savedOpenNotional = Money.ZERO;

        void beginTransaction() {
            savedOpenQuantity = openQuantity;
            savedOpenNotional = openNotional;
        }

        void commitTransaction() {
            // Snapshot is logically discarded. No external side effect is needed.
        }

        void rollbackTransaction() {
            openQuantity = savedOpenQuantity;
            openNotional = savedOpenNotional;
        }

        long openQuantity() {
            return openQuantity;
        }

        Money openNotional() {
            return openNotional;
        }

        void addOpenQuantity(long quantity) {
            openQuantity = Math.addExact(openQuantity, quantity);
        }

        void releaseOpenQuantity(long quantity) {
            if (quantity < 0 || quantity > openQuantity) {
                throw new IllegalStateException("open quantity release would underflow");
            }
            openQuantity -= quantity;
        }

        void addOpenNotional(Money notional) {
            openNotional = openNotional.add(notional);
        }

        void releaseOpenNotional(Money notional) {
            openNotional = openNotional.subtractExact(notional);
        }

        @Override
        public String toString() {
            return "RiskExposureState{" +
                    "openQuantity=" + openQuantity +
                    ", openNotional=" + openNotional +
                    '}';
        }
    }

    static final class KillSwitchRiskCheck implements RiskCheck {
        private volatile boolean active;

        @Override
        public RiskDecision reserve(
                RiskExposure exposure,
                RiskExposureState state,
                RiskLimit limit) {
            return active ? RiskDecision.KILL_SWITCH : RiskDecision.PASS;
        }

        @Override
        public void release(RiskExposure exposure, RiskExposureState state) {
            // Owns no exposure field.
        }

        void activate() {
            active = true;
        }

        void deactivate() {
            active = false;
        }
    }

    /** Owns only open-quantity mutation. */
    static final class MaxOpenQuantityRiskCheck implements RiskCheck {
        @Override
        public RiskDecision reserve(
                RiskExposure exposure,
                RiskExposureState state,
                RiskLimit limit) {

            long prospective = Math.addExact(
                    state.openQuantity(),
                    exposure.openQuantity());

            if (prospective > limit.maxOpenQuantity()) {
                return RiskDecision.LIMIT_BREACH;
            }

            state.addOpenQuantity(exposure.openQuantity());
            return RiskDecision.PASS;
        }

        @Override
        public void release(RiskExposure exposure, RiskExposureState state) {
            state.releaseOpenQuantity(exposure.openQuantity());
        }
    }

    /** Owns only open-notional mutation. */
    static final class MaxOpenNotionalRiskCheck implements RiskCheck {
        @Override
        public RiskDecision reserve(
                RiskExposure exposure,
                RiskExposureState state,
                RiskLimit limit) {

            Money prospective = state.openNotional().add(exposure.openNotional());

            if (prospective.compareTo(limit.maxOpenNotional()) > 0) {
                return RiskDecision.LIMIT_BREACH;
            }

            state.addOpenNotional(exposure.openNotional());
            return RiskDecision.PASS;
        }

        @Override
        public void release(RiskExposure exposure, RiskExposureState state) {
            state.releaseOpenNotional(exposure.openNotional());
        }
    }

    static final class RiskGroup {
        private final List<RiskCheck> checks;
        private final RiskExposureState exposureState = new RiskExposureState();
        private final RiskLimit limit;

        RiskGroup(List<RiskCheck> checks, RiskLimit limit) {
            this.checks = List.copyOf(checks);
            this.limit = Objects.requireNonNull(limit);
        }

        /**
         * Transactional reservation: if any check rejects or throws, all prior
         * provisional mutations are rolled back to the exact pre-attempt state.
         */
        synchronized RiskDecision reserve(RiskExposure exposure) {
            exposureState.beginTransaction();

            try {
                for (RiskCheck check : checks) {
                    RiskDecision decision = check.reserve(exposure, exposureState, limit);
                    if (decision != RiskDecision.PASS) {
                        exposureState.rollbackTransaction();
                        return decision;
                    }
                }

                exposureState.commitTransaction();
                return RiskDecision.PASS;
            } catch (RuntimeException failure) {
                exposureState.rollbackTransaction();
                throw failure;
            }
        }

        synchronized void release(RiskExposure exposure) {
            for (RiskCheck check : checks) {
                check.release(exposure, exposureState);
            }
        }

        synchronized RiskExposureState snapshot() {
            RiskExposureState copy = new RiskExposureState();
            copy.openQuantity = exposureState.openQuantity;
            copy.openNotional = exposureState.openNotional;
            return copy;
        }
    }

    static final class RiskEngine {
        private final Map<String, RiskGroup> groupsById = new HashMap<>();

        void registerGroup(String riskGroupId, RiskGroup group) {
            groupsById.put(requireText(riskGroupId, "riskGroupId"), Objects.requireNonNull(group));
        }

        RiskDecision reserve(RiskExposure exposure) {
            return group(exposure.riskGroupId()).reserve(exposure);
        }

        void release(RiskExposure exposure) {
            group(exposure.riskGroupId()).release(exposure);
        }

        RiskExposureState snapshot(String riskGroupId) {
            return group(riskGroupId).snapshot();
        }

        private RiskGroup group(String riskGroupId) {
            RiskGroup group = groupsById.get(riskGroupId);
            if (group == null) {
                throw new IllegalStateException("unknown risk group: " + riskGroupId);
            }
            return group;
        }
    }

    /**
     * Centralizes ownership of per-order open-risk reservations.
     *
     * The matching engine knows nothing about risk. The orchestration layer tells
     * this ledger when quantity executes/cancels; the ledger releases exactly the
     * corresponding reservation. This prevents scattered/double risk releases.
     */
    static final class OpenOrderRiskLedger {
        private final RiskEngine riskEngine;
        private final Map<String, Long> reservedQuantityByClientOrderId = new HashMap<>();

        OpenOrderRiskLedger(RiskEngine riskEngine) {
            this.riskEngine = Objects.requireNonNull(riskEngine);
        }

        RiskDecision reserveForNewOrder(Order order) {
            if (reservedQuantityByClientOrderId.containsKey(order.clientOrderId)) {
                throw new IllegalStateException("risk already reserved for order");
            }

            RiskExposure fullExposure = RiskExposure.forOrderQuantity(
                    order,
                    order.originalQuantity);

            RiskDecision decision = riskEngine.reserve(fullExposure);
            if (decision == RiskDecision.PASS) {
                reservedQuantityByClientOrderId.put(
                        order.clientOrderId,
                        order.originalQuantity);
            }
            return decision;
        }

        void releaseExecutedQuantity(Order order, long executedQuantity) {
            releaseQuantity(order, executedQuantity);
        }

        void releaseAllRemaining(Order order) {
            long reserved = reservedQuantity(order.clientOrderId);
            if (reserved > 0) {
                releaseQuantity(order, reserved);
            }
        }

        long reservedQuantity(String clientOrderId) {
            return reservedQuantityByClientOrderId.getOrDefault(clientOrderId, 0L);
        }

        private void releaseQuantity(Order order, long quantity) {
            if (quantity <= 0) {
                throw new IllegalArgumentException("release quantity must be > 0");
            }

            long reserved = reservedQuantity(order.clientOrderId);
            if (quantity > reserved) {
                throw new IllegalStateException(
                        "risk release exceeds reservation for " + order.clientOrderId);
            }

            riskEngine.release(RiskExposure.forOrderQuantity(order, quantity));

            long remainingReservation = reserved - quantity;
            if (remainingReservation == 0) {
                reservedQuantityByClientOrderId.remove(order.clientOrderId);
            } else {
                reservedQuantityByClientOrderId.put(
                        order.clientOrderId,
                        remainingReservation);
            }
        }
    }

    // ======================================================================
    // 8. POSITIONS
    // ======================================================================

    record PositionKey(String firmId, String instrumentId) {
        PositionKey {
            requireText(firmId, "firmId");
            requireText(instrumentId, "instrumentId");
        }
    }

    static final class PositionTracker {
        private final Map<PositionKey, Long> netPositions = new HashMap<>();

        void applyExecution(Order order, long executedQuantity) {
            long signedQuantity = order.side == Side.BUY
                    ? executedQuantity
                    : -executedQuantity;

            netPositions.merge(
                    new PositionKey(order.firmId, order.instrumentId),
                    signedQuantity,
                    Long::sum);
        }

        long netPosition(String firmId, String instrumentId) {
            return netPositions.getOrDefault(new PositionKey(firmId, instrumentId), 0L);
        }
    }

    // ======================================================================
    // 9. END-TO-END TRADING ORCHESTRATOR
    // ======================================================================

    record OrderSubmissionResult(
            SubmissionDecision submissionDecision,
            RiskDecision riskDecision,
            Order order,
            List<Fill> fills,
            List<String> selfMatchCancelledPassiveOrderIds) {

        OrderSubmissionResult {
            fills = List.copyOf(fills);
            selfMatchCancelledPassiveOrderIds =
                    List.copyOf(selfMatchCancelledPassiveOrderIds);
        }

        boolean acceptedByRisk() {
            return submissionDecision == SubmissionDecision.ACCEPTED;
        }
    }

    static final class TradingSystem {
        private final OrderRepository orderRepository;
        private final OrderBookRegistry orderBooks;
        private final MatchingEngine matchingEngine;
        private final OpenOrderRiskLedger openOrderRiskLedger;
        private final PositionTracker positions;
        private final AtomicLong nextExchangeOrderId = new AtomicLong(1_000_000L);

        TradingSystem(
                OrderRepository orderRepository,
                OrderBookRegistry orderBooks,
                MatchingEngine matchingEngine,
                OpenOrderRiskLedger openOrderRiskLedger,
                PositionTracker positions) {

            this.orderRepository = Objects.requireNonNull(orderRepository);
            this.orderBooks = Objects.requireNonNull(orderBooks);
            this.matchingEngine = Objects.requireNonNull(matchingEngine);
            this.openOrderRiskLedger = Objects.requireNonNull(openOrderRiskLedger);
            this.positions = Objects.requireNonNull(positions);
        }

        OrderSubmissionResult submit(Order order) {
            Objects.requireNonNull(order);

            if (orderRepository.containsClientOrderId(order.clientOrderId)) {
                order.reject();
                return new OrderSubmissionResult(
                        SubmissionDecision.DUPLICATE_CLIENT_ORDER_ID,
                        null,
                        order,
                        List.of(),
                        List.of());
            }

            // The OMS repository owns lifecycle identity regardless of later risk rejection.
            orderRepository.add(order);

            RiskDecision riskDecision = openOrderRiskLedger.reserveForNewOrder(order);
            if (riskDecision != RiskDecision.PASS) {
                order.reject();
                return new OrderSubmissionResult(
                        SubmissionDecision.RISK_REJECTED,
                        riskDecision,
                        order,
                        List.of(),
                        List.of());
            }

            orderRepository.linkExchangeOrderId(
                    order,
                    String.valueOf(nextExchangeOrderId.getAndIncrement()));

            OrderBook book = orderBooks.getOrCreate(order.instrumentId);
            MatchResult matchResult = matchingEngine.matchIncomingOrder(order, book);

            for (Fill fill : matchResult.fills()) {
                Order aggressor = requireOrder(fill.aggressorClientOrderId());
                Order passive = requireOrder(fill.passiveClientOrderId());

                openOrderRiskLedger.releaseExecutedQuantity(
                        aggressor,
                        fill.executionQuantity());
                openOrderRiskLedger.releaseExecutedQuantity(
                        passive,
                        fill.executionQuantity());

                positions.applyExecution(aggressor, fill.executionQuantity());
                positions.applyExecution(passive, fill.executionQuantity());
            }

            // SMP cancel-passive removes the entire passive remainder from open risk.
            for (String passiveId : matchResult.selfMatchCancelledPassiveOrderIds()) {
                Order cancelledPassive = requireOrder(passiveId);
                openOrderRiskLedger.releaseAllRemaining(cancelledPassive);
            }

            // IOC / market / failed-FOK remainder never rests; release its reservation.
            if (order.state == OrderState.CANCELLED && order.restingLevel == null) {
                openOrderRiskLedger.releaseAllRemaining(order);
            }

            return new OrderSubmissionResult(
                    SubmissionDecision.ACCEPTED,
                    RiskDecision.PASS,
                    order,
                    matchResult.fills(),
                    matchResult.selfMatchCancelledPassiveOrderIds());
        }

        boolean cancel(String clientOrderId) {
            Order order = orderRepository.findByClientOrderId(clientOrderId);
            if (order == null || order.restingLevel == null) {
                return false;
            }

            OrderBook book = orderBooks.get(order.instrumentId);
            if (book == null || !book.cancelRestingOrder(clientOrderId)) {
                return false;
            }

            openOrderRiskLedger.releaseAllRemaining(order);
            return true;
        }

        OrderBook orderBook(String instrumentId) {
            return orderBooks.get(instrumentId);
        }

        PositionTracker positions() {
            return positions;
        }

        private Order requireOrder(String clientOrderId) {
            Order order = orderRepository.findByClientOrderId(clientOrderId);
            if (order == null) {
                throw new IllegalStateException("fill references unknown order " + clientOrderId);
            }
            return order;
        }
    }

    // ======================================================================
    // 10. REUSABLE LRU CACHE
    // ======================================================================

    static final class LruNode<K, V> {
        K key;
        V value;
        LruNode<K, V> previous;
        LruNode<K, V> next;

        LruNode() {}

        LruNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    static final class LruCache<K, V> {
        private final int capacity;
        private final Map<K, LruNode<K, V>> nodesByKey = new HashMap<>();

        // Sentinels: most-recent is after head; least-recent is before tail.
        private final LruNode<K, V> head = new LruNode<>();
        private final LruNode<K, V> tail = new LruNode<>();

        LruCache(int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("capacity must be > 0");
            }
            this.capacity = capacity;
            head.next = tail;
            tail.previous = head;
        }

        V get(K key) {
            LruNode<K, V> node = nodesByKey.get(key);
            if (node == null) {
                return null;
            }
            moveToMostRecent(node);
            return node.value;
        }

        void put(K key, V value) {
            LruNode<K, V> existing = nodesByKey.get(key);
            if (existing != null) {
                existing.value = value;
                moveToMostRecent(existing);
                return;
            }

            LruNode<K, V> node = new LruNode<>(key, value);
            nodesByKey.put(key, node);
            addAsMostRecent(node);

            if (nodesByKey.size() > capacity) {
                LruNode<K, V> leastRecent = removeLeastRecent();
                nodesByKey.remove(leastRecent.key);
            }
        }

        boolean remove(K key) {
            LruNode<K, V> node = nodesByKey.remove(key);
            if (node == null) {
                return false;
            }
            unlink(node);
            return true;
        }

        V computeIfAbsent(K key, Function<K, V> factory) {
            V existing = get(key);
            if (existing != null) {
                return existing;
            }
            V value = factory.apply(key);
            put(key, value);
            return value;
        }

        int size() {
            return nodesByKey.size();
        }

        private void addAsMostRecent(LruNode<K, V> node) {
            node.previous = head;
            node.next = head.next;
            head.next.previous = node;
            head.next = node;
        }

        private void unlink(LruNode<K, V> node) {
            node.previous.next = node.next;
            node.next.previous = node.previous;
        }

        private void moveToMostRecent(LruNode<K, V> node) {
            unlink(node);
            addAsMostRecent(node);
        }

        private LruNode<K, V> removeLeastRecent() {
            LruNode<K, V> leastRecent = tail.previous;
            if (leastRecent == head) {
                throw new IllegalStateException("LRU is empty");
            }
            unlink(leastRecent);
            return leastRecent;
        }
    }

    // ======================================================================
    // 11. SEQUENCE REORDERING + BOUNDED DEDUP
    // ======================================================================

    static final class SequenceReorderBuffer<T> {
        private long nextExpectedSequence;
        private final NavigableMap<Long, T> bufferedMessages = new TreeMap<>();

        SequenceReorderBuffer(long firstExpectedSequence) {
            this.nextExpectedSequence = firstExpectedSequence;
        }

        List<T> onMessage(long sequenceNumber, T message) {
            List<T> readyInOrder = new ArrayList<>();

            if (sequenceNumber < nextExpectedSequence) {
                return readyInOrder; // stale / duplicate
            }

            bufferedMessages.putIfAbsent(sequenceNumber, message);

            while (true) {
                T next = bufferedMessages.remove(nextExpectedSequence);
                if (next == null) {
                    break;
                }
                readyInOrder.add(next);
                nextExpectedSequence++;
            }

            return readyInOrder;
        }

        long nextExpectedSequence() {
            return nextExpectedSequence;
        }

        int bufferedMessageCount() {
            return bufferedMessages.size();
        }
    }

    /**
     * Insertion-order bounded remembered set.
     * This is intentionally NOT eternal/exact dedup after an ID is evicted.
     */
    static final class BoundedDedupSet<K> {
        private final Map<K, Boolean> rememberedKeys;

        BoundedDedupSet(int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("capacity must be > 0");
            }

            rememberedKeys = new LinkedHashMap<>(capacity, 0.75f, false) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<K, Boolean> eldest) {
                    return size() > capacity;
                }
            };
        }

        boolean firstTime(K key) {
            if (rememberedKeys.containsKey(key)) {
                return false;
            }
            rememberedKeys.put(key, Boolean.TRUE);
            return true;
        }

        int size() {
            return rememberedKeys.size();
        }
    }

    // ======================================================================
    // 12. RESILIENT FAN-OUT AGGREGATOR (NOT MATCHING HOT PATH)
    // ======================================================================

    record Request(String idempotencyKey, String payload) {}
    record ServiceAResult(String value) {}
    record ServiceBResult(String value) { static final ServiceBResult EMPTY = new ServiceBResult(""); }
    record ServiceCResult(String value) { static final ServiceCResult EMPTY = new ServiceCResult(""); }
    record AggregatedResult(String idempotencyKey, String mergedValue, boolean partial) {}

    @FunctionalInterface
    interface BackendService<T> {
        T call(Request request) throws Exception;
    }

    static final class TransientDependencyException extends RuntimeException {
        TransientDependencyException(String message) {
            super(message);
        }
    }

    record RetryPolicy(
            int maxAttempts,
            long initialBackoffMillis,
            long maxBackoffMillis,
            long maxJitterMillis) {

        RetryPolicy {
            if (maxAttempts <= 0
                    || initialBackoffMillis < 0
                    || maxBackoffMillis < initialBackoffMillis
                    || maxJitterMillis < 0) {
                throw new IllegalArgumentException("invalid retry policy");
            }
        }

        long delayBeforeAttempt(int nextAttemptNumber) {
            if (nextAttemptNumber <= 1) {
                return 0L;
            }

            int exponent = Math.max(0, nextAttemptNumber - 2);
            long exponential;
            try {
                exponential = Math.multiplyExact(initialBackoffMillis, 1L << Math.min(exponent, 30));
            } catch (ArithmeticException overflow) {
                exponential = maxBackoffMillis;
            }

            long capped = Math.min(exponential, maxBackoffMillis);
            long jitter = maxJitterMillis == 0
                    ? 0L
                    : ThreadLocalRandom.current().nextLong(maxJitterMillis + 1);
            return Math.addExact(capped, jitter);
        }
    }

    /** One JVM: concurrent same-key callers join the same computation. */
    static final class SingleFlightRegistry<V> {
        private final ConcurrentHashMap<String, CompletableFuture<V>> computations =
                new ConcurrentHashMap<>();

        Reservation<V> reserve(String key) {
            CompletableFuture<V> candidate = new CompletableFuture<>();
            CompletableFuture<V> existing = computations.putIfAbsent(key, candidate);
            return existing == null
                    ? new Reservation<>(true, candidate)
                    : new Reservation<>(false, existing);
        }

        void removeFailed(String key, CompletableFuture<V> computation) {
            computations.remove(key, computation);
        }

        record Reservation<V>(boolean owner, CompletableFuture<V> future) {}
    }

    static final class ResilientAggregatorService implements AutoCloseable {
        private final long perDependencyTimeoutMillis;
        private final long overallDeadlineMillis;
        private final RetryPolicy retryPolicy;

        private final ExecutorService serviceAExecutor;
        private final ExecutorService serviceBExecutor;
        private final ExecutorService serviceCExecutor;

        private final BackendService<ServiceAResult> serviceA;
        private final BackendService<ServiceBResult> serviceB;
        private final BackendService<ServiceCResult> serviceC;

        private final SingleFlightRegistry<AggregatedResult> singleFlight =
                new SingleFlightRegistry<>();

        // Demo persistence. Production would use a durable store/transaction.
        private final ConcurrentHashMap<String, AggregatedResult> persistedResults =
                new ConcurrentHashMap<>();
        private final AtomicInteger persistenceCount = new AtomicInteger();

        ResilientAggregatorService(
                BackendService<ServiceAResult> serviceA,
                BackendService<ServiceBResult> serviceB,
                BackendService<ServiceCResult> serviceC,
                long perDependencyTimeoutMillis,
                long overallDeadlineMillis,
                RetryPolicy retryPolicy) {

            this.serviceA = Objects.requireNonNull(serviceA);
            this.serviceB = Objects.requireNonNull(serviceB);
            this.serviceC = Objects.requireNonNull(serviceC);
            this.perDependencyTimeoutMillis = perDependencyTimeoutMillis;
            this.overallDeadlineMillis = overallDeadlineMillis;
            this.retryPolicy = Objects.requireNonNull(retryPolicy);

            serviceAExecutor = newBoundedExecutor("service-a", 4, 32);
            serviceBExecutor = newBoundedExecutor("service-b", 4, 32);
            serviceCExecutor = newBoundedExecutor("service-c", 2, 16);
        }

        AggregatedResult aggregate(Request request) {
            SingleFlightRegistry.Reservation<AggregatedResult> reservation =
                    singleFlight.reserve(request.idempotencyKey());

            if (!reservation.owner()) {
                return reservation.future().join();
            }

            CompletableFuture<AggregatedResult> ownerFuture = reservation.future();

            try {
                AggregatedResult result = compute(request);
                persistExactlyOnceInThisJvm(result);
                ownerFuture.complete(result);
                return result;
            } catch (RuntimeException failure) {
                ownerFuture.completeExceptionally(failure);
                singleFlight.removeFailed(request.idempotencyKey(), ownerFuture);
                throw failure;
            }
        }

        private AggregatedResult compute(Request request) {
            long overallDeadlineNanos = System.nanoTime()
                    + TimeUnit.MILLISECONDS.toNanos(overallDeadlineMillis);

            CompletableFuture<ServiceAResult> futureA = invokeDependency(
                    serviceA,
                    request,
                    serviceAExecutor,
                    overallDeadlineNanos);

            CompletableFuture<ServiceBResult> futureB = invokeDependency(
                    serviceB,
                    request,
                    serviceBExecutor,
                    overallDeadlineNanos);

            CompletableFuture<ServiceCResult> futureC = invokeDependency(
                    serviceC,
                    request,
                    serviceCExecutor,
                    overallDeadlineNanos);

            try {
                CompletableFuture.allOf(futureA, futureB, futureC).get(
                        Math.max(1L, remainingMillis(overallDeadlineNanos)),
                        TimeUnit.MILLISECONDS);
            } catch (TimeoutException ignored) {
                // Overall deadline reached: inspect whichever futures completed.
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("aggregator interrupted", interrupted);
            } catch (ExecutionException unexpected) {
                throw new RuntimeException("unexpected fan-out failure", unexpected.getCause());
            }

            ServiceAResult a = getCompletedValueOrNull(futureA);
            ServiceBResult b = getCompletedValueOrNull(futureB);
            ServiceCResult c = getCompletedValueOrNull(futureC);

            if (a == null) {
                throw new RuntimeException("Service A is required");
            }

            ServiceBResult safeB = b == null ? ServiceBResult.EMPTY : b;
            ServiceCResult safeC = c == null ? ServiceCResult.EMPTY : c;
            boolean partial = b == null || c == null;

            return new AggregatedResult(
                    request.idempotencyKey(),
                    a.value() + "|" + safeB.value() + "|" + safeC.value(),
                    partial);
        }

        private <T> CompletableFuture<T> invokeDependency(
                BackendService<T> dependency,
                Request request,
                Executor executor,
                long overallDeadlineNanos) {

            long dependencyDeadlineNanos = Math.min(
                    overallDeadlineNanos,
                    System.nanoTime()
                            + TimeUnit.MILLISECONDS.toNanos(perDependencyTimeoutMillis));

            return CompletableFuture.supplyAsync(
                            () -> callWithRetry(
                                    dependency,
                                    request,
                                    dependencyDeadlineNanos),
                            executor)
                    // Bounds caller wait, but does not guarantee the underlying remote call is stopped.
                    .orTimeout(perDependencyTimeoutMillis, TimeUnit.MILLISECONDS)
                    .exceptionally(ignored -> null);
        }

        private <T> T callWithRetry(
                BackendService<T> dependency,
                Request request,
                long deadlineNanos) {

            RuntimeException lastTransientFailure = null;

            for (int attempt = 1; attempt <= retryPolicy.maxAttempts(); attempt++) {
                if (System.nanoTime() >= deadlineNanos) {
                    break;
                }

                try {
                    return dependency.call(request);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("dependency call interrupted", interrupted);
                } catch (TransientDependencyException transientFailure) {
                    lastTransientFailure = transientFailure;

                    if (attempt == retryPolicy.maxAttempts()) {
                        break;
                    }

                    long delayMillis = retryPolicy.delayBeforeAttempt(attempt + 1);
                    if (remainingMillis(deadlineNanos) <= delayMillis) {
                        break;
                    }

                    sleepPreservingInterrupt(delayMillis);
                } catch (Exception permanentFailure) {
                    // Permanent/application failures are not retried.
                    throw new CompletionException(permanentFailure);
                }
            }

            if (lastTransientFailure != null) {
                throw lastTransientFailure;
            }

            throw new TransientDependencyException("dependency deadline exhausted");
        }

        private void persistExactlyOnceInThisJvm(AggregatedResult result) {
            persistedResults.put(result.idempotencyKey(), result);
            persistenceCount.incrementAndGet();
        }

        int persistenceCount() {
            return persistenceCount.get();
        }

        AggregatedResult persistedResult(String idempotencyKey) {
            return persistedResults.get(idempotencyKey);
        }

        @Override
        public void close() {
            shutdown(serviceAExecutor);
            shutdown(serviceBExecutor);
            shutdown(serviceCExecutor);
        }
    }

    // ======================================================================
    // 13. SHARED HELPERS
    // ======================================================================

    private static String requireText(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }

    private static long remainingMillis(long deadlineNanos) {
        long remainingNanos = deadlineNanos - System.nanoTime();
        if (remainingNanos <= 0) {
            return 0L;
        }
        return Math.max(1L, TimeUnit.NANOSECONDS.toMillis(remainingNanos));
    }

    private static void sleepPreservingInterrupt(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("sleep interrupted", interrupted);
        }
    }

    private static <T> T getCompletedValueOrNull(CompletableFuture<T> future) {
        if (!future.isDone()) {
            return null;
        }
        try {
            return future.getNow(null);
        } catch (CompletionException ignored) {
            return null;
        }
    }

    private static ExecutorService newBoundedExecutor(
            String threadNamePrefix,
            int threadCount,
            int queueCapacity) {

        AtomicInteger threadNumber = new AtomicInteger();
        ThreadFactory threadFactory = runnable -> {
            Thread thread = new Thread(
                    runnable,
                    threadNamePrefix + "-" + threadNumber.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        };

        return new ThreadPoolExecutor(
                threadCount,
                threadCount,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueCapacity),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    private static void shutdown(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("============================================================");
        System.out.println(title);
        System.out.println("============================================================");
    }

    // ======================================================================
    // 14. TEST FIXTURE
    // ======================================================================

    static final class TestFixture {
        final KillSwitchRiskCheck killSwitch = new KillSwitchRiskCheck();
        final RiskEngine riskEngine = new RiskEngine();
        final OpenOrderRiskLedger riskLedger;
        final InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
        final OrderBookRegistry orderBooks = new OrderBookRegistry();
        final PositionTracker positions = new PositionTracker();
        final MatchingEngine matchingEngine =
                new MatchingEngine(new SameFirmCancelPassivePolicy());
        final TradingSystem tradingSystem;

        TestFixture(long maxOpenQuantity, Money maxOpenNotional, String... firms) {
            for (String firm : firms) {
                riskEngine.registerGroup(
                        firm,
                        new RiskGroup(
                                List.of(
                                        killSwitch,
                                        new MaxOpenQuantityRiskCheck(),
                                        new MaxOpenNotionalRiskCheck()),
                                new RiskLimit(maxOpenQuantity, maxOpenNotional)));
            }

            riskLedger = new OpenOrderRiskLedger(riskEngine);
            tradingSystem = new TradingSystem(
                    orderRepository,
                    orderBooks,
                    matchingEngine,
                    riskLedger,
                    positions);
        }
    }

    private static OrderPriority priority(long sequence) {
        // Tests use equal timestamps intentionally to prove sequence tie-breaking exists.
        return new OrderPriority(1_000_000L, sequence);
    }

    // ======================================================================
    // 15. RUNNABLE DEMONSTRATION / SMOKE TESTS
    // ======================================================================

    public static void main(String[] args) throws Exception {
        section("1. FIXED-POINT PRICE / MONEY");
        testFixedPointTypes();

        section("2. END-TO-END LIMIT MATCHING");
        testIntegratedTradingFlow();

        section("3. IOC / FOK / MARKET / SELF-MATCH / KILL SWITCH");
        testAdvancedOrderSemantics();

        section("4. EXPLICIT CANCEL + RISK RELEASE");
        testCancellation();

        section("5. MULTI-INSTRUMENT BOOK REGISTRY");
        testMultipleInstruments();

        section("6. LRU CACHE");
        testLruCache();

        section("7. SEQUENCE REORDERING");
        testSequenceReordering();

        section("8. BOUNDED DEDUP");
        testBoundedDedup();

        section("9. RESILIENT FAN-OUT AGGREGATOR");
        testResilientAggregator();

        section("10. RESULT");
        System.out.println("ALL REFACTORED INTEGRATION TESTS PASSED.");
    }

    private static void testFixedPointTypes() {
        Price price = Price.parse("101.2500");
        Money notional = price.multiply(50);

        check(price.scaledValue() == 1_012_500L, "price scale should be 1e4");
        check(price.toString().equals("101.2500"), "price formatting");
        check(notional.toString().equals("5062.5000"), "notional = price * quantity");

        Price average = Price.weightedAverage(
                Price.parse("101.2500"),
                50,
                Price.parse("102.0000"),
                20);

        check(average.toString().equals("101.4643"), "weighted average should round half-up");

        System.out.println("Price=" + price + ", 50-unit notional=" + notional);
        System.out.println("Weighted avg 50@101.2500 + 20@102.0000 = " + average);
    }

    private static void testIntegratedTradingFlow() {
        TestFixture fixture = new TestFixture(
                10_000,
                Money.parse("5000000.0000"),
                "FIRM-A",
                "FIRM-B");

        Order sell1 = Order.limit(
                "S1", "AAPL", "FIRM-B", Side.SELL,
                Price.parse("101.2500"), 50, TimeInForce.GTC, priority(1));

        Order sell2 = Order.limit(
                "S2", "AAPL", "FIRM-B", Side.SELL,
                Price.parse("102.0000"), 40, TimeInForce.GTC, priority(2));

        fixture.tradingSystem.submit(sell1);
        fixture.tradingSystem.submit(sell2);

        Order buy = Order.limit(
                "B1", "AAPL", "FIRM-A", Side.BUY,
                Price.parse("102.0000"), 70, TimeInForce.GTC, priority(3));

        OrderSubmissionResult result = fixture.tradingSystem.submit(buy);

        check(result.fills().size() == 2, "B1 should create two fills");
        check(result.fills().get(0).executionPrice().equals(Price.parse("101.2500")),
                "first fill at passive 101.2500");
        check(result.fills().get(0).executionQuantity() == 50, "first qty 50");
        check(result.fills().get(1).executionPrice().equals(Price.parse("102.0000")),
                "second fill at passive 102.0000");
        check(result.fills().get(1).executionQuantity() == 20, "second qty 20");

        check(buy.averageFillPrice.equals(Price.parse("101.4643")),
                "aggressor weighted average price");
        check(sell2.remainingQuantity == 20, "S2 should have 20 remaining");

        OrderBook aapl = fixture.tradingSystem.orderBook("AAPL");
        check(aapl.bestAskPrice().equals(Price.parse("102.0000")), "best ask should be 102");
        check(aapl.bestOppositeLevel(Side.BUY).totalRemainingQuantity() == 20,
                "remaining ask aggregate should be 20");

        check(fixture.positions.netPosition("FIRM-A", "AAPL") == 70,
                "FIRM-A AAPL position +70");
        check(fixture.positions.netPosition("FIRM-B", "AAPL") == -70,
                "FIRM-B AAPL position -70");

        RiskExposureState firmB = fixture.riskEngine.snapshot("FIRM-B");
        check(firmB.openQuantity() == 20, "only S2 remainder should stay reserved");
        check(firmB.openNotional().equals(Money.parse("2040.0000")),
                "20 * 102.0000 should remain open");

        check(fixture.riskEngine.snapshot("FIRM-A").openQuantity() == 0,
                "fully filled B1 leaves no open-order risk");

        System.out.println(aapl.snapshot());
        System.out.println("Fills=" + result.fills());
        System.out.println("B1 averageFillPrice=" + buy.averageFillPrice);
    }

    private static void testAdvancedOrderSemantics() {
        TestFixture fixture = new TestFixture(
                100_000,
                Money.parse("100000000.0000"),
                "A",
                "B");

        fixture.tradingSystem.submit(Order.limit(
                "ASK-B-1", "MSFT", "B", Side.SELL,
                Price.parse("100.0000"), 10, TimeInForce.GTC, priority(1)));

        // FOK 20 cannot fully fill against only 10 -> zero fills, no book mutation.
        Order fok = Order.limit(
                "BUY-A-FOK", "MSFT", "A", Side.BUY,
                Price.parse("100.0000"), 20, TimeInForce.FOK, priority(2));

        OrderSubmissionResult fokResult = fixture.tradingSystem.submit(fok);
        check(fokResult.fills().isEmpty(), "unfillable FOK must execute zero");
        check(fok.state == OrderState.CANCELLED, "unfillable FOK cancels");
        check(fixture.tradingSystem.orderBook("MSFT").findRestingOrder("ASK-B-1") != null,
                "failed FOK must not consume liquidity");

        // IOC takes 10 now, then cancels the unfilled 10.
        Order ioc = Order.limit(
                "BUY-A-IOC", "MSFT", "A", Side.BUY,
                Price.parse("100.0000"), 20, TimeInForce.IOC, priority(3));

        OrderSubmissionResult iocResult = fixture.tradingSystem.submit(ioc);
        check(iocResult.fills().size() == 1, "IOC should fill available 10");
        check(ioc.remainingQuantity == 10, "IOC retains cancelled remainder quantity");
        check(ioc.state == OrderState.CANCELLED, "IOC remainder cancelled");
        check(fixture.riskLedger.reservedQuantity(ioc.clientOrderId) == 0,
                "IOC risk reservation fully released");

        // SMP cancel-passive, then continue to next eligible passive firm.
        fixture.tradingSystem.submit(Order.limit(
                "SELF-ASK", "MSFT", "A", Side.SELL,
                Price.parse("101.0000"), 5, TimeInForce.GTC, priority(4)));

        fixture.tradingSystem.submit(Order.limit(
                "OTHER-ASK", "MSFT", "B", Side.SELL,
                Price.parse("102.0000"), 5, TimeInForce.GTC, priority(5)));

        Order smpBuyer = Order.limit(
                "A-BUY", "MSFT", "A", Side.BUY,
                Price.parse("102.0000"), 5, TimeInForce.GTC, priority(6));

        OrderSubmissionResult smpResult = fixture.tradingSystem.submit(smpBuyer);
        check(smpResult.selfMatchCancelledPassiveOrderIds().equals(List.of("SELF-ASK")),
                "SMP should cancel passive self order");
        check(smpResult.fills().size() == 1, "should continue to OTHER-ASK");
        check(smpResult.fills().get(0).passiveClientOrderId().equals("OTHER-ASK"),
                "must fill against other firm");

        // Market order uses explicit reference price only for risk reservation.
        fixture.tradingSystem.submit(Order.limit(
                "MARKET-LIQ", "MSFT", "B", Side.SELL,
                Price.parse("103.0000"), 3, TimeInForce.GTC, priority(7)));

        Order marketBuy = Order.market(
                "MARKET-BUY", "MSFT", "A", Side.BUY,
                Price.parse("103.5000"), 5, TimeInForce.IOC, priority(8));

        OrderSubmissionResult marketResult = fixture.tradingSystem.submit(marketBuy);
        check(marketResult.fills().size() == 1, "market should consume available liquidity");
        check(marketResult.fills().get(0).executionPrice().equals(Price.parse("103.0000")),
                "market executes at passive price");
        check(marketBuy.state == OrderState.CANCELLED && marketBuy.remainingQuantity == 2,
                "market remainder cannot rest");

        // Kill switch rejects new risk without modifying exposure state.
        fixture.killSwitch.activate();
        RiskExposureState before = fixture.riskEngine.snapshot("A");

        Order killed = Order.limit(
                "KILLED", "MSFT", "A", Side.BUY,
                Price.parse("99.0000"), 1, TimeInForce.GTC, priority(9));

        OrderSubmissionResult killedResult = fixture.tradingSystem.submit(killed);
        check(killedResult.submissionDecision() == SubmissionDecision.RISK_REJECTED,
                "kill-switch order should be risk rejected");
        check(killedResult.riskDecision() == RiskDecision.KILL_SWITCH,
                "risk decision should identify kill switch");
        check(killed.state == OrderState.REJECTED, "killed order state rejected");

        RiskExposureState after = fixture.riskEngine.snapshot("A");
        check(before.openQuantity() == after.openQuantity()
                        && before.openNotional().equals(after.openNotional()),
                "risk rejection must roll back exactly");

        fixture.killSwitch.deactivate();
        System.out.println("FOK / IOC / MARKET / SMP / KILL checks passed.");
    }

    private static void testCancellation() {
        TestFixture fixture = new TestFixture(
                1_000,
                Money.parse("1000000.0000"),
                "F1");

        Order order = Order.limit(
                "CANCEL-ME", "IBM", "F1", Side.BUY,
                Price.parse("10.5000"), 25, TimeInForce.GTC, priority(1));

        fixture.tradingSystem.submit(order);
        check(fixture.riskLedger.reservedQuantity("CANCEL-ME") == 25,
                "resting order should reserve 25");

        check(fixture.tradingSystem.cancel("CANCEL-ME"), "cancel should succeed");
        check(order.state == OrderState.CANCELLED, "order lifecycle should be cancelled");
        check(fixture.riskLedger.reservedQuantity("CANCEL-ME") == 0,
                "cancel should release all remaining risk");
        check(fixture.riskEngine.snapshot("F1").openNotional().equals(Money.ZERO),
                "cancel should release notional");

        System.out.println("Explicit cancel released liquidity + risk.");
    }

    private static void testMultipleInstruments() {
        TestFixture fixture = new TestFixture(
                10_000,
                Money.parse("10000000.0000"),
                "F1");

        fixture.tradingSystem.submit(Order.limit(
                "AAPL-BID", "AAPL", "F1", Side.BUY,
                Price.parse("200.0000"), 10, TimeInForce.GTC, priority(1)));

        fixture.tradingSystem.submit(Order.limit(
                "IBM-BID", "IBM", "F1", Side.BUY,
                Price.parse("150.0000"), 20, TimeInForce.GTC, priority(2)));

        check(fixture.tradingSystem.orderBook("AAPL").bestBidPrice().equals(Price.parse("200.0000")),
                "AAPL independent book");
        check(fixture.tradingSystem.orderBook("IBM").bestBidPrice().equals(Price.parse("150.0000")),
                "IBM independent book");

        System.out.println("AAPL book=" + fixture.tradingSystem.orderBook("AAPL").snapshot());
        System.out.println("IBM book=" + fixture.tradingSystem.orderBook("IBM").snapshot());
    }

    private static void testLruCache() {
        LruCache<String, Integer> cache = new LruCache<>(2);
        cache.put("A", 1);
        cache.put("B", 2);
        check(cache.get("A") == 1, "A must exist and become MRU");
        cache.put("C", 3);
        check(cache.get("B") == null, "B should be evicted as LRU");
        check(cache.get("A") == 1 && cache.get("C") == 3, "A/C should remain");
        System.out.println("LRU operations passed.");
    }

    private static void testSequenceReordering() {
        SequenceReorderBuffer<String> reorderBuffer = new SequenceReorderBuffer<>(1);
        check(reorderBuffer.onMessage(3, "C").isEmpty(), "3 buffers");
        check(reorderBuffer.onMessage(2, "B").isEmpty(), "2 buffers");

        List<String> emitted = reorderBuffer.onMessage(1, "A");
        check(emitted.equals(List.of("A", "B", "C")), "1 closes hole and drains");
        check(reorderBuffer.nextExpectedSequence() == 4, "next expected 4");
        System.out.println("Reordered=" + emitted);
    }

    private static void testBoundedDedup() {
        BoundedDedupSet<String> dedup = new BoundedDedupSet<>(2);
        check(dedup.firstTime("E1"), "E1 first");
        check(!dedup.firstTime("E1"), "E1 duplicate");
        check(dedup.firstTime("E2"), "E2 first");
        check(dedup.firstTime("E3"), "E3 first; evicts E1");
        check(dedup.size() == 2, "bounded capacity");
        check(dedup.firstTime("E1"), "evicted E1 can be accepted again");
        System.out.println("Bounded dedup semantics passed.");
    }

    private static void testResilientAggregator() throws Exception {
        AtomicInteger serviceBAttempts = new AtomicInteger();

        BackendService<ServiceAResult> serviceA =
                request -> new ServiceAResult("A:" + request.payload());

        BackendService<ServiceBResult> serviceB = request -> {
            if (serviceBAttempts.incrementAndGet() == 1) {
                throw new TransientDependencyException("temporary B failure");
            }
            return new ServiceBResult("B:" + request.payload());
        };

        BackendService<ServiceCResult> serviceC = request -> {
            Thread.sleep(300); // intentionally exceeds 200ms per-dependency timeout
            return new ServiceCResult("C:" + request.payload());
        };

        RetryPolicy retryPolicy = new RetryPolicy(3, 50, 200, 20);

        try (ResilientAggregatorService aggregator = new ResilientAggregatorService(
                serviceA,
                serviceB,
                serviceC,
                200,
                500,
                retryPolicy)) {

            Request request = new Request("idem-1", "payload");
            ExecutorService callers = Executors.newFixedThreadPool(2);

            try {
                Future<AggregatedResult> first = callers.submit(() -> aggregator.aggregate(request));
                Future<AggregatedResult> duplicate = callers.submit(() -> aggregator.aggregate(request));

                AggregatedResult result1 = first.get(2, TimeUnit.SECONDS);
                AggregatedResult result2 = duplicate.get(2, TimeUnit.SECONDS);

                check(result1.equals(result2), "same key joins same computation");
                check(result1.partial(), "slow optional C should degrade partially");
                check(result1.mergedValue().startsWith("A:payload|B:payload|"),
                        "A + retried B should contribute");
                check(aggregator.persistenceCount() == 1,
                        "single-flight owner should persist once in this JVM");
                check(aggregator.persistedResult("idem-1").equals(result1),
                        "persisted result should match");

                System.out.println("Aggregator result=" + result1);
                System.out.println("Persistence count=" + aggregator.persistenceCount()
                        + ", B attempts=" + serviceBAttempts.get());
            } finally {
                callers.shutdownNow();
            }
        }
    }
}
