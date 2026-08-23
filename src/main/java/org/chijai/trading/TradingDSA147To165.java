package org.chijai.trading;

import java.util.*;

/**
 * TradingDSA147To165
 *
 * VERSION: 6.0 — Interview Recall / Independent Solutions
 * Java 17+ interview-practice file for DSA-147 through DSA-165.
 *
 * LEARNING DESIGN RULE
 * --------------------
 * 1. Every DSA problem is self-contained.
 * 2. No DSA problem calls helpers/classes from another DSA problem.
 * 3. Intentional duplication is preferred over shared abstractions.
 * 4. Same concept => same name and same visual code grammar.
 * 5. Keep only the algorithmic state required by the problem.
 * 6. Production concerns stay in FOLLOW-UP comments unless the problem requires them.
 * 7. Prefer familiar JDK idioms over custom abstractions/helpers.
 *
 * CONSISTENT VOCABULARY
 * ---------------------
 * orderId, executionId, instrument
 * price, quantity, remainingQuantity
 * sequence, timestampNs
 * event, order, trade, fill, result
 * bids, asks, buyOrders, sellOrders
 * window, buffer, nextExpected
 *
 * All monetary prices are represented as long (think fixed-point scaled integer).
 */
public class TradingDSA147To165 {

    // =========================================================================
    // DSA-147 — Best Bid / Best Ask
    // =========================================================================
    /**
     * PROBLEM
     * Maintain an aggregated order book from NEW, CANCEL and FILL events.
     * Return best bid, best ask and their quantities.
     *
     * EXAMPLE
     * BUY 100x10, BUY 101x5, SELL 103x7, SELL 102x8
     * -> bestBid=101x5, bestAsk=102x8
     *
     * IDEA
     * TreeMap<price, quantity> per side.
     * BUY/BID = highest price wins. SELL/ASK = lowest price wins.
     *
     * COMPLEXITY
     * update O(log P), best price O(log P) conservatively, space O(P)
     *
     * FOLLOW-UP
     * Need per-order cancel/FIFO? Change value from quantity to PriceLevel containing orders.
     */
    static final class DSA147_BestBidAsk {

        enum Side { BUY, SELL }
        enum EventType { NEW, CANCEL, FILL }

        record OrderEvent(EventType type, Side side, long price, long quantity) {}

        private final TreeMap<Long, Long> bids =
                new TreeMap<>(Comparator.reverseOrder());

        private final TreeMap<Long, Long> asks =
                new TreeMap<>();

        void process(OrderEvent event) {
            TreeMap<Long, Long> levels =
                    event.side() == Side.BUY ? bids : asks;

            switch (event.type()) {
                case NEW -> levels.merge(
                        event.price(), event.quantity(), Long::sum);

                case CANCEL, FILL -> {
                    long remainingQuantity =
                            levels.get(event.price()) - event.quantity();

                    if (remainingQuantity == 0) {
                        levels.remove(event.price());
                    } else {
                        levels.put(event.price(), remainingQuantity);
                    }
                }
            }
        }

        long bestBid() {
            return bids.isEmpty() ? -1 : bids.firstKey();
        }

        long bestAsk() {
            return asks.isEmpty() ? -1 : asks.firstKey();
        }

        long bestBidQuantity() {
            return bids.isEmpty() ? 0 : bids.firstEntry().getValue();
        }

        long bestAskQuantity() {
            return asks.isEmpty() ? 0 : asks.firstEntry().getValue();
        }
    }

    // =========================================================================
    // DSA-148 — Top N Price Levels
    // =========================================================================
    /**
     * PROBLEM
     * Maintain aggregated bid/ask levels and return the top N levels best-to-worst.
     *
     * EXAMPLE
     * bids = 101x20, 100x10, 99x30
     * topBids(2) -> [101x20, 100x10]
     *
     * IDEA
     * Best-first TreeMaps; iterate only the first N entries.
     *
     * COMPLEXITY
     * update O(log P), top N O(N), space O(P)
     *
     * FOLLOW-UP
     * Need individual orders at a level? Store PriceLevel instead of aggregate quantity.
     */
    static final class DSA148_TopNPriceLevels {

        enum Side { BUY, SELL }
        enum EventType { NEW, CANCEL, FILL }

        record OrderEvent(EventType type, Side side, long price, long quantity) {}
        record PriceLevel(long price, long quantity) {}

        private final TreeMap<Long, Long> bids =
                new TreeMap<>(Comparator.reverseOrder());

        private final TreeMap<Long, Long> asks =
                new TreeMap<>();

        void process(OrderEvent event) {
            TreeMap<Long, Long> levels =
                    event.side() == Side.BUY ? bids : asks;

            switch (event.type()) {
                case NEW -> levels.merge(
                        event.price(), event.quantity(), Long::sum);

                case CANCEL, FILL -> {
                    long remainingQuantity =
                            levels.get(event.price()) - event.quantity();

                    if (remainingQuantity == 0) {
                        levels.remove(event.price());
                    } else {
                        levels.put(event.price(), remainingQuantity);
                    }
                }
            }
        }

        List<PriceLevel> topBids(int n) {
            return topLevels(bids, n);
        }

        List<PriceLevel> topAsks(int n) {
            return topLevels(asks, n);
        }

        private List<PriceLevel> topLevels(
                TreeMap<Long, Long> levels,
                int n) {

            List<PriceLevel> result = new ArrayList<>();

            for (Map.Entry<Long, Long> entry : levels.entrySet()) {
                if (result.size() == n) {
                    break;
                }

                result.add(new PriceLevel(
                        entry.getKey(), entry.getValue()));
            }

            return result;
        }
    }

    // =========================================================================
    // DSA-149 — Price-Time Priority Queue
    // =========================================================================
    /**
     * PROBLEM
     * Return the next order using price-time priority.
     * BUY: higher price first, then earlier sequence.
     * SELL: lower price first, then earlier sequence.
     *
     * EXAMPLE
     * B1=100 seq2, B2=101 seq3, B3=101 seq1 -> B3 wins.
     *
     * IDEA
     * Two PriorityQueues with side-specific comparators.
     *
     * COMPLEXITY
     * add/poll O(log N), peek O(1), space O(N)
     *
     * FOLLOW-UP
     * Need fast cancel/depth? Use price levels + FIFO order lists instead of only heaps.
     */
    static final class DSA149_PriceTimePriorityQueue {

        enum Side { BUY, SELL }

        record Order(
                String orderId,
                Side side,
                long price,
                long sequence,
                long quantity) {}

        private final PriorityQueue<Order> buyOrders =
                new PriorityQueue<>(
                        Comparator.comparingLong(Order::price)
                                .reversed()
                                .thenComparingLong(Order::sequence)
                );

        private final PriorityQueue<Order> sellOrders =
                new PriorityQueue<>(
                        Comparator.comparingLong(Order::price)
                                .thenComparingLong(Order::sequence)
                );

        void add(Order order) {
            if (order.side() == Side.BUY) {
                buyOrders.add(order);
            } else {
                sellOrders.add(order);
            }
        }

        Order peekBuy() {
            return buyOrders.peek();
        }

        Order peekSell() {
            return sellOrders.peek();
        }

        Order pollBuy() {
            return buyOrders.poll();
        }

        Order pollSell() {
            return sellOrders.poll();
        }
    }

    // =========================================================================
    // DSA-150 — Deduplicate Execution Events
    // =========================================================================
    /**
     * PROBLEM
     * Execution events have unique execution IDs. A replayed execution must affect
     * position exactly once.
     *
     * EXAMPLE
     * E1 BUY 10 -> +10; replay E1 BUY 10 -> ignored; final position = +10.
     *
     * IDEA
     * HashSet<executionId>. Add ID before mutating position.
     *
     * COMPLEXITY
     * expected O(1) per event, space O(unique executions)
     *
     * FOLLOW-UP
     * Bounded/durable dedup is a persistence requirement; do not add it unless asked.
     */
    static final class DSA150_DeduplicateExecutionEvents {

        enum Side { BUY, SELL }

        record ExecutionEvent(
                long executionId,
                String account,
                String instrument,
                Side side,
                long quantity) {}

        record PositionKey(String account, String instrument) {}

        private final Set<Long> seenExecutionIds = new HashSet<>();
        private final Map<PositionKey, Long> positionByKey = new HashMap<>();

        boolean process(ExecutionEvent event) {
            if (!seenExecutionIds.add(event.executionId())) {
                return false;
            }

            long signedQuantity =
                    event.side() == Side.BUY
                            ? event.quantity()
                            : -event.quantity();

            PositionKey key =
                    new PositionKey(event.account(), event.instrument());

            positionByKey.merge(key, signedQuantity, Long::sum);
            return true;
        }

        long position(String account, String instrument) {
            return positionByKey.getOrDefault(
                    new PositionKey(account, instrument), 0L);
        }
    }

    // =========================================================================
    // DSA-151 — Detect Sequence Gap
    // =========================================================================
    /**
     * PROBLEM
     * Given strictly increasing sequence numbers, return the missing ranges.
     *
     * EXAMPLE
     * [1,2,5,6,10] -> [3..4, 7..9]
     *
     * IDEA
     * Compare current sequence with previous + 1.
     *
     * COMPLEXITY
     * O(N) time, O(1) extra space excluding output
     *
     * FOLLOW-UP
     * If input is unordered, sort first before detecting gaps.
     */
    static final class DSA151_DetectSequenceGap {

        record Gap(long from, long to) {}

        static List<Gap> detect(long[] sequences) {
            List<Gap> result = new ArrayList<>();

            for (int index = 1; index < sequences.length; index++) {
                long previous = sequences[index - 1];
                long current = sequences[index];

                if (current > previous + 1) {
                    result.add(new Gap(previous + 1, current - 1));
                }
            }

            return result;
        }
    }

    // =========================================================================
    // DSA-152 — Reorder Out-of-Order Messages
    // =========================================================================
    /**
     * PROBLEM
     * Messages can arrive out of order. Emit them only in exact sequence order.
     *
     * EXAMPLE
     * receive 3 -> []
     * receive 2 -> []
     * receive 1 -> [1,2,3]
     *
     * IDEA
     * nextExpected + HashMap<sequence, message> + drain contiguous messages.
     *
     * COMPLEXITY
     * expected O(1) per message, space O(buffered messages)
     *
     * FOLLOW-UP
     * Gap timeout/snapshot recovery is separate from in-memory reordering.
     */
    static final class DSA152_ReorderOutOfOrderMessages {

        record Message(long sequence, String payload) {}

        private long nextExpected;
        private final Map<Long, Message> buffer = new HashMap<>();

        DSA152_ReorderOutOfOrderMessages(long firstExpected) {
            nextExpected = firstExpected;
        }

        List<Message> receive(Message message) {
            List<Message> result = new ArrayList<>();

            if (message.sequence() < nextExpected) {
                return result;
            }

            buffer.putIfAbsent(message.sequence(), message);

            while (buffer.containsKey(nextExpected)) {
                result.add(buffer.remove(nextExpected));
                nextExpected++;
            }

            return result;
        }
    }

    // =========================================================================
    // DSA-153 — Rolling Exposure
    // =========================================================================
    /**
     * PROBLEM
     * Every open order contributes price * remainingQuantity to exposure.
     * Process NEW, UPDATE and CANCEL without rescanning all open orders.
     *
     * EXAMPLE
     * NEW O1 100x100 -> 10,000
     * UPDATE O1 100x120 -> 12,000
     * CANCEL O1 -> 0
     *
     * IDEA
     * orderId -> current contribution + one running total.
     * UPDATE = total - oldContribution + newContribution.
     *
     * COMPLEXITY
     * expected O(1) per event, space O(open orders)
     *
     * FOLLOW-UP
     * Real risk may add FX, hierarchy and multiple exposure metrics.
     */
    static final class DSA153_RollingExposure {

        enum EventType { NEW, UPDATE, CANCEL }

        private final Map<String, Long> exposureByOrderId = new HashMap<>();
        private long totalExposure;

        long process(
                EventType type,
                String orderId,
                long price,
                long remainingQuantity) {

            switch (type) {
                case NEW -> {
                    long newExposure = price * remainingQuantity;
                    exposureByOrderId.put(orderId, newExposure);
                    totalExposure += newExposure;
                }

                case UPDATE -> {
                    long oldExposure = exposureByOrderId.get(orderId);
                    long newExposure = price * remainingQuantity;

                    exposureByOrderId.put(orderId, newExposure);
                    totalExposure += newExposure - oldExposure;
                }

                case CANCEL -> {
                    totalExposure -= exposureByOrderId.remove(orderId);
                }
            }

            return totalExposure;
        }
    }

    // =========================================================================
    // DSA-154 — Price Deviation Check
    // =========================================================================
    /**
     * PROBLEM
     * Maintain a reference price per instrument and reject order prices outside
     * an allowed deviation expressed in basis points (100 bps = 1%).
     *
     * EXAMPLE
     * reference=10000, threshold=500 bps -> allowed [9500,10500].
     *
     * IDEA
     * lower = reference * (10000-bps) / 10000
     * upper = reference * (10000+bps) / 10000
     *
     * COMPLEXITY
     * expected O(1) update/check, space O(instruments)
     *
     * FOLLOW-UP
     * Production: define stale-reference behavior and checked fixed-point arithmetic.
     */
    static final class DSA154_PriceDeviationCheck {

        private final long thresholdBps;
        private final Map<String, Long> referencePriceByInstrument = new HashMap<>();

        DSA154_PriceDeviationCheck(long thresholdBps) {
            this.thresholdBps = thresholdBps;
        }

        void updateReferencePrice(String instrument, long price) {
            referencePriceByInstrument.put(instrument, price);
        }

        boolean isAllowed(String instrument, long price) {
            Long referencePrice = referencePriceByInstrument.get(instrument);

            if (referencePrice == null) {
                return false;
            }

            long lower =
                    referencePrice * (10_000 - thresholdBps) / 10_000;

            long upper =
                    referencePrice * (10_000 + thresholdBps) / 10_000;

            return price >= lower && price <= upper;
        }
    }

    // =========================================================================
    // DSA-155 — Exchange Throttle
    // =========================================================================
    /**
     * PROBLEM
     * Allow at most limit requests during the last windowNs nanoseconds.
     *
     * EXAMPLE
     * limit=2, window=1000
     * t=1000 ACCEPT, 1100 ACCEPT, 1200 REJECT, 2001 ACCEPT
     *
     * IDEA
     * Deque of accepted timestamps. Evict expired timestamps from the front.
     *
     * COMPLEXITY
     * amortized O(1) per request, space O(requests in current window)
     *
     * FOLLOW-UP
     * Token bucket is a different policy; discuss it only if requested.
     */
    static final class DSA155_ExchangeThrottle {

        private final int limit;
        private final long windowNs;
        private final Deque<Long> window = new ArrayDeque<>();

        DSA155_ExchangeThrottle(int limit, long windowNs) {
            this.limit = limit;
            this.windowNs = windowNs;
        }

        boolean allow(long timestampNs) {
            long cutoff = timestampNs - windowNs;

            while (!window.isEmpty()
                    && window.peekFirst() <= cutoff) {
                window.pollFirst();
            }

            if (window.size() >= limit) {
                return false;
            }

            window.addLast(timestampNs);
            return true;
        }
    }

    // =========================================================================
    // DSA-156 — Rolling VWAP
    // =========================================================================
    /**
     * PROBLEM
     * Maintain VWAP over the last N trades.
     * VWAP = sum(price * quantity) / sum(quantity).
     *
     * EXAMPLE
     * 100x10, 110x20 -> VWAP = 3200/30 = 106.666...
     *
     * IDEA
     * Deque of last N trades + running totalValue + running totalQuantity.
     *
     * COMPLEXITY
     * O(1) per trade, space O(N)
     *
     * FOLLOW-UP
     * Time-window VWAP uses the same deque pattern but expires by timestamp.
     */
    static final class DSA156_RollingVWAP {

        record Trade(long price, long quantity) {}

        private final int maxTrades;
        private final Deque<Trade> window = new ArrayDeque<>();

        private long totalValue;
        private long totalQuantity;

        DSA156_RollingVWAP(int maxTrades) {
            this.maxTrades = maxTrades;
        }

        void add(Trade trade) {
            window.addLast(trade);

            totalValue += trade.price() * trade.quantity();
            totalQuantity += trade.quantity();

            if (window.size() > maxTrades) {
                Trade expired = window.pollFirst();

                totalValue -= expired.price() * expired.quantity();
                totalQuantity -= expired.quantity();
            }
        }

        double vwap() {
            return totalQuantity == 0
                    ? 0.0
                    : (double) totalValue / totalQuantity;
        }
    }

    // =========================================================================
    // DSA-157 — Market Data Sliding Maximum
    // =========================================================================
    /**
     * PROBLEM
     * Given market prices, return the maximum in every window of size windowSize.
     *
     * EXAMPLE
     * [1,3,-1,-3,5,3,6,7], windowSize=3 -> [3,3,5,5,6,7]
     *
     * IDEA
     * Monotonic decreasing deque of indices.
     * Front always points to the maximum of the current window.
     *
     * COMPLEXITY
     * O(N) time, O(windowSize) space
     *
     * FOLLOW-UP
     * For a live stream, keep the same deque and a running index as object state.
     */
    static final class DSA157_MarketDataSlidingMaximum {

        static long[] maxInWindows(long[] prices, int windowSize) {
            if (prices.length == 0 || windowSize <= 0 || windowSize > prices.length) {
                return new long[0];
            }

            Deque<Integer> indices = new ArrayDeque<>();
            long[] result = new long[prices.length - windowSize + 1];

            for (int index = 0; index < prices.length; index++) {
                while (!indices.isEmpty()
                        && indices.peekFirst() <= index - windowSize) {
                    indices.pollFirst();
                }

                while (!indices.isEmpty()
                        && prices[indices.peekLast()] <= prices[index]) {
                    indices.pollLast();
                }

                indices.addLast(index);

                if (index >= windowSize - 1) {
                    result[index - windowSize + 1] =
                            prices[indices.peekFirst()];
                }
            }

            return result;
        }
    }

    // =========================================================================
    // DSA-158 — Order State Aggregation
    // =========================================================================
    /**
     * PROBLEM
     * Maintain order lifecycle and quantities from NEW, ACK, FILL, CANCEL, REJECT.
     * A FILL may be partial or complete depending on remaining quantity.
     *
     * EXAMPLE
     * NEW O1 qty100 -> ACK -> FILL 40 -> FILL 60
     * -> FILLED, filledQuantity=100, remainingQuantity=0
     *
     * IDEA
     * HashMap<orderId, mutable Order>. Each event mutates exactly one order.
     *
     * INVARIANT
     * originalQuantity = filledQuantity + remainingQuantity
     *
     * COMPLEXITY
     * expected O(1) per event, space O(orders)
     *
     * FOLLOW-UP
     * Production OMS usually validates a richer transition matrix.
     */
    static final class DSA158_OrderStateAggregation {

        enum State {
            PENDING_NEW,
            ACTIVE,
            PARTIALLY_FILLED,
            FILLED,
            CANCELLED,
            REJECTED
        }

        enum EventType { NEW, ACK, FILL, CANCEL, REJECT }

        static final class Order {
            final String orderId;
            final long originalQuantity;

            long filledQuantity;
            long remainingQuantity;
            State state;

            Order(String orderId, long quantity) {
                this.orderId = orderId;
                this.originalQuantity = quantity;
                this.remainingQuantity = quantity;
                this.state = State.PENDING_NEW;
            }
        }

        private final Map<String, Order> ordersById = new HashMap<>();

        void process(String orderId, EventType type, long quantity) {
            switch (type) {
                case NEW -> ordersById.put(
                        orderId, new Order(orderId, quantity));

                case ACK -> ordersById.get(orderId).state = State.ACTIVE;

                case FILL -> {
                    Order order = ordersById.get(orderId);

                    order.filledQuantity += quantity;
                    order.remainingQuantity -= quantity;

                    order.state = order.remainingQuantity == 0
                            ? State.FILLED
                            : State.PARTIALLY_FILLED;
                }

                case CANCEL -> ordersById.get(orderId).state = State.CANCELLED;

                case REJECT -> ordersById.get(orderId).state = State.REJECTED;
            }
        }

        Order get(String orderId) {
            return ordersById.get(orderId);
        }
    }

    // =========================================================================
    // DSA-159 — Match Buy and Sell Orders
    // =========================================================================
    /**
     * PROBLEM
     * Match limit orders using price-time priority.
     * BUY crosses when buy.price >= best sell.price.
     * SELL crosses when sell.price <= best buy.price.
     * Execute at the resting order's price.
     *
     * EXAMPLE
     * S1 SELL 101x50, S2 SELL 102x50, B1 BUY 102x70
     * -> 50@101 with S1, then 20@102 with S2; S2 keeps 30.
     *
     * IDEA
     * Two price-time PriorityQueues + mutable remainingQuantity.
     *
     * COMPLEXITY
     * each heap add/poll O(log N); consuming M resting orders O(M log N)
     *
     * FOLLOW-UP
     * Need cancel/depth/amend? Move to TreeMap<price, PriceLevel> + FIFO orders.
     */
    static final class DSA159_MatchBuyAndSellOrders {

        enum Side { BUY, SELL }

        static final class Order {
            final String orderId;
            final Side side;
            final long price;
            final long sequence;
            long remainingQuantity;

            Order(
                    String orderId,
                    Side side,
                    long price,
                    long sequence,
                    long quantity) {

                this.orderId = orderId;
                this.side = side;
                this.price = price;
                this.sequence = sequence;
                this.remainingQuantity = quantity;
            }

            long price() { return price; }
            long sequence() { return sequence; }
        }

        record Trade(
                String buyOrderId,
                String sellOrderId,
                long price,
                long quantity) {}

        private final PriorityQueue<Order> buyOrders =
                new PriorityQueue<>(
                        Comparator.comparingLong(Order::price)
                                .reversed()
                                .thenComparingLong(Order::sequence)
                );

        private final PriorityQueue<Order> sellOrders =
                new PriorityQueue<>(
                        Comparator.comparingLong(Order::price)
                                .thenComparingLong(Order::sequence)
                );

        private long nextSequence;

        List<Trade> submit(
                String orderId,
                Side side,
                long price,
                long quantity) {

            Order incoming = new Order(
                    orderId, side, price, nextSequence++, quantity);

            return side == Side.BUY
                    ? matchBuy(incoming)
                    : matchSell(incoming);
        }

        private List<Trade> matchBuy(Order buyOrder) {
            List<Trade> result = new ArrayList<>();

            while (buyOrder.remainingQuantity > 0
                    && !sellOrders.isEmpty()
                    && buyOrder.price >= sellOrders.peek().price) {

                Order sellOrder = sellOrders.poll();

                long executionQuantity = Math.min(
                        buyOrder.remainingQuantity,
                        sellOrder.remainingQuantity);

                result.add(new Trade(
                        buyOrder.orderId,
                        sellOrder.orderId,
                        sellOrder.price,
                        executionQuantity));

                buyOrder.remainingQuantity -= executionQuantity;
                sellOrder.remainingQuantity -= executionQuantity;

                if (sellOrder.remainingQuantity > 0) {
                    sellOrders.add(sellOrder);
                }
            }

            if (buyOrder.remainingQuantity > 0) {
                buyOrders.add(buyOrder);
            }

            return result;
        }

        private List<Trade> matchSell(Order sellOrder) {
            List<Trade> result = new ArrayList<>();

            while (sellOrder.remainingQuantity > 0
                    && !buyOrders.isEmpty()
                    && sellOrder.price <= buyOrders.peek().price) {

                Order buyOrder = buyOrders.poll();

                long executionQuantity = Math.min(
                        sellOrder.remainingQuantity,
                        buyOrder.remainingQuantity);

                result.add(new Trade(
                        buyOrder.orderId,
                        sellOrder.orderId,
                        buyOrder.price,
                        executionQuantity));

                sellOrder.remainingQuantity -= executionQuantity;
                buyOrder.remainingQuantity -= executionQuantity;

                if (buyOrder.remainingQuantity > 0) {
                    buyOrders.add(buyOrder);
                }
            }

            if (sellOrder.remainingQuantity > 0) {
                sellOrders.add(sellOrder);
            }

            return result;
        }
    }

    // =========================================================================
    // DSA-160 — Position From Executions
    // =========================================================================
    /**
     * PROBLEM
     * Maintain net position per (account, instrument) from fills.
     * BUY adds quantity; SELL subtracts quantity.
     *
     * EXAMPLE
     * BUY 100 AAPL, SELL 35 AAPL -> net position = +65.
     *
     * IDEA
     * HashMap<PositionKey, netQuantity> + signed delta.
     *
     * COMPLEXITY
     * expected O(1) per fill, space O(account-instrument keys)
     *
     * FOLLOW-UP
     * Average cost / realized P&L needs an explicit accounting model; do not invent one.
     */
    static final class DSA160_PositionFromExecutions {

        enum Side { BUY, SELL }

        record Fill(
                String account,
                String instrument,
                Side side,
                long quantity) {}

        record PositionKey(String account, String instrument) {}

        private final Map<PositionKey, Long> positionByKey = new HashMap<>();

        void process(Fill fill) {
            long signedQuantity =
                    fill.side() == Side.BUY
                            ? fill.quantity()
                            : -fill.quantity();

            PositionKey key =
                    new PositionKey(fill.account(), fill.instrument());

            positionByKey.merge(key, signedQuantity, Long::sum);
        }

        long position(String account, String instrument) {
            return positionByKey.getOrDefault(
                    new PositionKey(account, instrument), 0L);
        }
    }

    // =========================================================================
    // DSA-161 — Detect Duplicate Orders
    // =========================================================================
    /**
     * PROBLEM
     * Every order has a unique orderId. Detect whether an incoming orderId
     * has already been seen.
     *
     * EXAMPLE
     * O1 first time -> not duplicate
     * O1 again -> duplicate
     *
     * IDEA
     * HashSet.add(orderId) returns false if the ID already exists.
     *
     * COMPLEXITY
     * expected O(1) per order, space O(unique order IDs)
     *
     * FOLLOW-UP
     * If identity is composite, use a record such as
     * (account, clientOrderId, instrument) as the HashSet key.
     */
    static final class DSA161_DetectDuplicateOrders {

        private final Set<String> seenOrderIds = new HashSet<>();

        boolean isDuplicate(String orderId) {
            return !seenOrderIds.add(orderId);
        }
    }

    // =========================================================================
    // DSA-162 — Most Active Instruments
    // =========================================================================
    /**
     * PROBLEM
     * Events arrive in timestamp order. Maintain activity per instrument over the
     * last windowNs and return the top K instruments by activity quantity.
     *
     * EXAMPLE
     * AAPL +100, MSFT +300, AAPL +250 -> AAPL=350, MSFT=300.
     *
     * IDEA
     * Deque for expiry + HashMap for running scores + min-heap size K for query.
     *
     * COMPLEXITY
     * add amortized O(1); topK O(M log K), M=active instruments
     *
     * FOLLOW-UP
     * If top-K is queried extremely often, maintain ranking incrementally.
     */
    static final class DSA162_MostActiveInstruments {

        record Event(
                String instrument,
                long quantity,
                long timestampNs) {}

        record RankedInstrument(
                String instrument,
                long quantity) {}

        private final long windowNs;
        private final Deque<Event> window = new ArrayDeque<>();
        private final Map<String, Long> activityByInstrument = new HashMap<>();

        DSA162_MostActiveInstruments(long windowNs) {
            this.windowNs = windowNs;
        }

        void add(Event event) {
            evictExpired(event.timestampNs());

            window.addLast(event);
            activityByInstrument.merge(
                    event.instrument(), event.quantity(), Long::sum);
        }

        List<RankedInstrument> topK(int k, long nowNs) {
            evictExpired(nowNs);

            PriorityQueue<RankedInstrument> minHeap =
                    new PriorityQueue<>(
                            Comparator.comparingLong(RankedInstrument::quantity)
                    );

            for (Map.Entry<String, Long> entry : activityByInstrument.entrySet()) {
                RankedInstrument instrument =
                        new RankedInstrument(entry.getKey(), entry.getValue());

                minHeap.add(instrument);

                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }

            List<RankedInstrument> result = new ArrayList<>(minHeap);

            result.sort(
                    Comparator.comparingLong(RankedInstrument::quantity)
                            .reversed()
                            .thenComparing(RankedInstrument::instrument)
            );

            return result;
        }

        private void evictExpired(long nowNs) {
            long cutoff = nowNs - windowNs;

            while (!window.isEmpty()
                    && window.peekFirst().timestampNs() <= cutoff) {

                Event expired = window.pollFirst();
                long remainingQuantity =
                        activityByInstrument.get(expired.instrument()) - expired.quantity();

                if (remainingQuantity == 0) {
                    activityByInstrument.remove(expired.instrument());
                } else {
                    activityByInstrument.put(expired.instrument(), remainingQuantity);
                }
            }
        }
    }

    // =========================================================================
    // DSA-163 — Merge Multiple Ordered Market Feeds
    // =========================================================================
    /**
     * PROBLEM
     * K feeds are individually sorted by (timestamp, sequence).
     * Merge them into one globally sorted stream.
     *
     * EXAMPLE
     * F1: 1000,1300; F2: 1100,1400 -> 1000,1100,1300,1400.
     *
     * IDEA
     * Min-heap containing one current head from each feed.
     * Poll -> emit -> advance only that feed -> push its next event.
     *
     * COMPLEXITY
     * O(N log K) time, O(K) heap space
     *
     * FOLLOW-UP
     * Feed index is the final tie-break so output is deterministic.
     */
    static final class DSA163_MergeMultipleOrderedMarketFeeds {

        record Event(
                long timestampNs,
                long sequence,
                String feed,
                String payload) {}

        record HeapEntry(
                Event event,
                int feedIndex,
                int position) {}

        static List<Event> merge(List<List<Event>> feeds) {
            PriorityQueue<HeapEntry> minHeap =
                    new PriorityQueue<>(
                            Comparator
                                    .comparingLong(
                                            (HeapEntry entry) ->
                                                    entry.event().timestampNs())
                                    .thenComparingLong(
                                            entry -> entry.event().sequence())
                                    .thenComparingInt(HeapEntry::feedIndex)
                    );

            for (int feedIndex = 0;
                 feedIndex < feeds.size();
                 feedIndex++) {

                if (!feeds.get(feedIndex).isEmpty()) {
                    minHeap.add(new HeapEntry(
                            feeds.get(feedIndex).get(0),
                            feedIndex,
                            0));
                }
            }

            List<Event> result = new ArrayList<>();

            while (!minHeap.isEmpty()) {
                HeapEntry smallest = minHeap.poll();
                result.add(smallest.event());

                int nextPosition = smallest.position() + 1;
                List<Event> feed = feeds.get(smallest.feedIndex());

                if (nextPosition < feed.size()) {
                    minHeap.add(new HeapEntry(
                            feed.get(nextPosition),
                            smallest.feedIndex(),
                            nextPosition));
                }
            }

            return result;
        }
    }

    // =========================================================================
    // DSA-164 — Snapshot + Incremental Merge
    // =========================================================================
    /**
     * PROBLEM
     * Incremental updates can arrive before or after a snapshot at sequence S.
     * After loading snapshot S, apply updates strictly from S+1 onward.
     *
     * EXAMPLE
     * update 12 arrives -> buffer
     * snapshot 10 loads -> expect 11
     * update 11 arrives -> apply 11, then drain 12
     *
     * IDEA
     * nextExpected + HashMap<sequence, update> + drain contiguous updates.
     *
     * COMPLEXITY
     * expected O(1) per incremental; snapshot install scans buffered updates once
     *
     * FOLLOW-UP
     * Ignore every sequence < nextExpected to prevent replay/double-apply.
     */
    static final class DSA164_SnapshotIncrementalMerge {

        enum Operation { UPSERT, DELETE }

        record Update(
                long sequence,
                Operation operation,
                String key,
                String value) {}

        private Map<String, String> state = new HashMap<>();
        private final Map<Long, Update> buffer = new HashMap<>();

        private boolean snapshotLoaded;
        private long nextExpected;

        void apply(Update update) {
            if (!snapshotLoaded) {
                buffer.putIfAbsent(update.sequence(), update);
                return;
            }

            if (update.sequence() < nextExpected) {
                return;
            }

            buffer.putIfAbsent(update.sequence(), update);
            drain();
        }

        void loadSnapshot(
                Map<String, String> snapshot,
                long snapshotSequence) {

            state = new HashMap<>(snapshot);
            nextExpected = snapshotSequence + 1;
            snapshotLoaded = true;

            buffer.entrySet().removeIf(
                    entry -> entry.getKey() < nextExpected);

            drain();
        }

        private void drain() {
            while (buffer.containsKey(nextExpected)) {
                Update update = buffer.remove(nextExpected);

                if (update.operation() == Operation.UPSERT) {
                    state.put(update.key(), update.value());
                } else {
                    state.remove(update.key());
                }

                nextExpected++;
            }
        }

        String get(String key) {
            return state.get(key);
        }
    }

    // =========================================================================
    // DSA-165 — Order Latency Percentile
    // =========================================================================
    /**
     * PROBLEM
     * Given order latencies, return an exact percentile using nearest-rank.
     * Assume all samples fit in memory.
     *
     * EXAMPLE
     * Sort samples, then index = ceil(percentile * N) - 1.
     *
     * IDEA
     * Copy the input, sort it, then select index = ceil(percentile * N) - 1.
     *
     * COMPLEXITY
     * O(N log N) time, O(N) space for the sorted copy
     *
     * FOLLOW-UP
     * Streaming/bounded-memory production telemetry -> HdrHistogram/t-digest/KLL.
     */
    static final class DSA165_OrderLatencyPercentile {

        static long percentile(long[] latencies, double percentile) {
            long[] sorted = latencies.clone();
            Arrays.sort(sorted);

            int index =
                    (int) Math.ceil(percentile * sorted.length) - 1;

            return sorted[index];
        }
    }
}
