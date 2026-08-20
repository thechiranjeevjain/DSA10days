package org.chijai.trading;

import java.util.*;

public class TradingDSA147To165 {

    // ============================================================
    // DSA-147 — Best Bid / Best Ask
    // Update: O(log P), best bid/ask: O(1), Space: O(P)
    // ============================================================
    static class BestBidAskBook {
        private final TreeMap<Integer, Long> bids = new TreeMap<>();
        private final TreeMap<Integer, Long> asks = new TreeMap<>();

        public void updateBid(int price, long quantityDelta) {
            updateLevel(bids, price, quantityDelta);
        }

        public void updateAsk(int price, long quantityDelta) {
            updateLevel(asks, price, quantityDelta);
        }

        private void updateLevel(TreeMap<Integer, Long> side, int price, long delta) {
            long newQty = side.getOrDefault(price, 0L) + delta;

            if (newQty < 0) {
                throw new IllegalArgumentException("Quantity cannot become negative");
            }

            if (newQty == 0) {
                side.remove(price);
            } else {
                side.put(price, newQty);
            }
        }

        public Integer bestBid() {
            return bids.isEmpty() ? null : bids.lastKey();
        }

        public Integer bestAsk() {
            return asks.isEmpty() ? null : asks.firstKey();
        }
    }


    // ============================================================
    // DSA-148 — Top N Price Levels
    // Update: O(log P)
    // Query: O(N)
    // Space: O(P)
    // ============================================================
    static class TopNPriceLevels {

        enum Side {
            BUY, SELL
        }

        static class Level {
            final int price;
            final long quantity;

            Level(int price, long quantity) {
                this.price = price;
                this.quantity = quantity;
            }

            @Override
            public String toString() {
                return "(" + price + ", qty=" + quantity + ")";
            }
        }

        private final TreeMap<Integer, Long> bids = new TreeMap<>();
        private final TreeMap<Integer, Long> asks = new TreeMap<>();

        public void update(Side side, int price, long quantityDelta) {

            TreeMap<Integer, Long> book =
                    side == Side.BUY ? bids : asks;

            long newQty =
                    book.getOrDefault(price, 0L) + quantityDelta;

            if (newQty < 0) {
                throw new IllegalArgumentException(
                        "Quantity cannot become negative");
            }

            if (newQty == 0) {
                book.remove(price);
            } else {
                book.put(price, newQty);
            }
        }

        public List<Level> topN(Side side, int n) {

            if (n <= 0) {
                return Collections.emptyList();
            }

            TreeMap<Integer, Long> book =
                    side == Side.BUY ? bids : asks;

            NavigableMap<Integer, Long> ordered =
                    side == Side.BUY
                            ? book.descendingMap()
                            : book;

            List<Level> result = new ArrayList<>();

            for (Map.Entry<Integer, Long> entry
                    : ordered.entrySet()) {

                if (result.size() == n) {
                    break;
                }

                result.add(
                        new Level(
                                entry.getKey(),
                                entry.getValue()
                        )
                );
            }

            return result;
        }
    }


    // ============================================================
    // DSA-149 — Price-Time Priority Queue
    //
    // BUY:
    // higher price first
    // earlier sequence first
    //
    // SELL:
    // lower price first
    // earlier sequence first
    //
    // Insert: O(log N)
    // Peek: O(1)
    // Poll: O(log N)
    // ============================================================
    static class PriceTimePriorityQueue {

        enum Side {
            BUY, SELL
        }

        static class Order {

            final long orderId;
            final Side side;
            final int price;
            final long arrivalSequence;

            Order(long orderId,
                  Side side,
                  int price,
                  long arrivalSequence) {

                this.orderId = orderId;
                this.side = side;
                this.price = price;
                this.arrivalSequence = arrivalSequence;
            }

            @Override
            public String toString() {
                return "Order{" +
                        "id=" + orderId +
                        ", side=" + side +
                        ", price=" + price +
                        ", seq=" + arrivalSequence +
                        '}';
            }
        }

        private final PriorityQueue<Order> buys =
                new PriorityQueue<>(
                        Comparator
                                .comparingInt((Order o) -> o.price)
                                .reversed()
                                .thenComparingLong(
                                        o -> o.arrivalSequence)
                );

        private final PriorityQueue<Order> sells =
                new PriorityQueue<>(
                        Comparator
                                .comparingInt((Order o) -> o.price)
                                .thenComparingLong(
                                        o -> o.arrivalSequence)
                );

        public void add(Order order) {

            if (order.side == Side.BUY) {
                buys.offer(order);
            } else {
                sells.offer(order);
            }
        }

        public Order peekNext(Side side) {

            return side == Side.BUY
                    ? buys.peek()
                    : sells.peek();
        }

        public Order pollNext(Side side) {

            return side == Side.BUY
                    ? buys.poll()
                    : sells.poll();
        }
    }


    // ============================================================
    // DSA-150 — Deduplicate Execution Events
    //
    // Expected:
    // O(1) per event
    // O(U) space
    //
    // Invariant:
    // execution ID changes state at most once.
    // ============================================================
    static class ExecutionDeduplicator {

        static class Execution {

            final String executionId;
            final long quantity;

            Execution(String executionId,
                      long quantity) {

                this.executionId = executionId;
                this.quantity = quantity;
            }
        }

        private final Set<String> processed =
                new HashSet<>();

        private long totalQuantity;

        public boolean process(
                Execution execution) {

            if (!processed.add(
                    execution.executionId)) {

                return false;
            }

            totalQuantity +=
                    execution.quantity;

            return true;
        }

        public long totalQuantity() {
            return totalQuantity;
        }
    }


    // ============================================================
    // DSA-151 — Detect Sequence Gap
    //
    // Example:
    // 1 2 5 6 10
    //
    // Missing:
    // 3-4
    // 7-9
    //
    // O(n)
    // ============================================================
    static class SequenceGapDetector {

        static class Range {

            final long from;
            final long to;

            Range(long from,
                  long to) {

                this.from = from;
                this.to = to;
            }

            @Override
            public String toString() {

                if (from == to) {
                    return String.valueOf(from);
                }

                return from + "-" + to;
            }
        }

        public static List<Range>
        findMissingRanges(
                long[] sequenceNumbers) {

            List<Range> gaps =
                    new ArrayList<>();

            if (sequenceNumbers == null
                    || sequenceNumbers.length < 2) {

                return gaps;
            }

            long previous =
                    sequenceNumbers[0];

            for (int i = 1;
                 i < sequenceNumbers.length;
                 i++) {

                long current =
                        sequenceNumbers[i];

                if (current <= previous) {

                    throw new IllegalArgumentException(
                            "Input must be strictly increasing"
                    );
                }

                if (current > previous + 1) {

                    gaps.add(
                            new Range(
                                    previous + 1,
                                    current - 1
                            )
                    );
                }

                previous = current;
            }

            return gaps;
        }
    }


    // ============================================================
    // DSA-152 — Reorder Out-of-Order Messages
    //
    // TreeMap buffers messages until
    // expected sequence appears.
    //
    // O(n log W)
    // Space O(W)
    // ============================================================
    static class BoundedMessageReorderer<T> {

        static class Message<T> {

            final long sequence;
            final T payload;

            Message(long sequence,
                    T payload) {

                this.sequence = sequence;
                this.payload = payload;
            }

            @Override
            public String toString() {
                return sequence + ":" + payload;
            }
        }

        private long nextExpectedSequence;

        private final int maxWindow;

        private final TreeMap<
                Long,
                Message<T>> buffer =
                new TreeMap<>();

        BoundedMessageReorderer(
                long firstExpectedSequence,
                int maxWindow) {

            if (maxWindow <= 0) {

                throw new IllegalArgumentException(
                        "maxWindow must be positive"
                );
            }

            this.nextExpectedSequence =
                    firstExpectedSequence;

            this.maxWindow =
                    maxWindow;
        }

        public List<Message<T>>
        onMessage(Message<T> message) {

            List<Message<T>> emitted =
                    new ArrayList<>();

            // Message already processed.
            if (message.sequence
                    < nextExpectedSequence) {

                return emitted;
            }

            if (message.sequence
                    - nextExpectedSequence
                    >= maxWindow) {

                throw new IllegalStateException(
                        "Message exceeds bounded disorder window"
                );
            }

            buffer.putIfAbsent(
                    message.sequence,
                    message
            );

            while (buffer.containsKey(
                    nextExpectedSequence)) {

                emitted.add(
                        buffer.remove(
                                nextExpectedSequence)
                );

                nextExpectedSequence++;
            }

            return emitted;
        }
    }


    // ============================================================
    // DSA-153 — Rolling Exposure
    //
    // account + instrument -> exposure
    //
    // Expected O(1) per event.
    // ============================================================
    static class RollingExposure {

        static class Key {

            final String account;
            final String instrument;

            Key(String account,
                String instrument) {

                this.account = account;
                this.instrument = instrument;
            }

            @Override
            public boolean equals(Object o) {

                if (this == o) {
                    return true;
                }

                if (!(o instanceof Key)) {
                    return false;
                }

                Key key = (Key) o;

                return Objects.equals(
                        account,
                        key.account)
                        &&
                        Objects.equals(
                                instrument,
                                key.instrument);
            }

            @Override
            public int hashCode() {

                return Objects.hash(
                        account,
                        instrument);
            }
        }

        private final Map<Key, Long>
                exposure =
                new HashMap<>();

        public void apply(
                String account,
                String instrument,
                long delta) {

            Key key =
                    new Key(
                            account,
                            instrument);

            long next =
                    exposure.getOrDefault(
                            key,
                            0L)
                            + delta;

            if (next == 0L) {
                exposure.remove(key);
            } else {
                exposure.put(
                        key,
                        next);
            }
        }

        public long get(
                String account,
                String instrument) {

            return exposure.getOrDefault(
                    new Key(
                            account,
                            instrument),
                    0L
            );
        }
    }


    // ============================================================
    // DSA-154 — Price Deviation Check
    //
    // deviation =
    //
    // abs(orderPrice - refPrice)
    // -------------------------
    // refPrice
    //
    // O(1) per order.
    // ============================================================
    static class PriceDeviationChecker {

        private final Map<String, Double>
                referencePrice =
                new HashMap<>();

        public void updateReference(
                String instrument,
                double price) {

            if (price <= 0) {

                throw new IllegalArgumentException(
                        "Reference price must be positive"
                );
            }

            referencePrice.put(
                    instrument,
                    price);
        }

        public boolean violates(
                String instrument,
                double orderPrice,
                double maxDeviationPercent) {

            Double ref =
                    referencePrice.get(
                            instrument);

            if (ref == null) {

                throw new IllegalStateException(
                        "No reference price for "
                                + instrument
                );
            }

            double deviationPercent =
                    Math.abs(
                            orderPrice - ref)
                            / ref
                            * 100.0;

            return deviationPercent
                    > maxDeviationPercent;
        }
    }


    // ============================================================
    // DSA-155 — Exchange Throttle
    //
    // Sliding timestamp window.
    //
    // Example:
    // maximum 2 requests per 1000ms.
    //
    // Amortized O(1) per request.
    // ============================================================
    static class ExchangeThrottle {

        private final int maxRequests;
        private final long windowMillis;

        private final Map<
                String,
                Deque<Long>>
                requestTimesByKey =
                new HashMap<>();

        ExchangeThrottle(
                int maxRequests,
                long windowMillis) {

            if (maxRequests <= 0
                    || windowMillis <= 0) {

                throw new IllegalArgumentException(
                        "Limits must be positive"
                );
            }

            this.maxRequests =
                    maxRequests;

            this.windowMillis =
                    windowMillis;
        }

        public boolean allow(
                String key,
                long timestampMillis) {

            Deque<Long> queue =
                    requestTimesByKey
                            .computeIfAbsent(
                                    key,
                                    k ->
                                            new ArrayDeque<>()
                            );

            long cutoff =
                    timestampMillis
                            - windowMillis;

            while (!queue.isEmpty()
                    &&
                    queue.peekFirst()
                            <= cutoff) {

                queue.pollFirst();
            }

            if (queue.size()
                    >= maxRequests) {

                return false;
            }

            queue.addLast(
                    timestampMillis);

            return true;
        }
    }


    // ============================================================
    // DSA-156 — Rolling VWAP
    //
    // VWAP =
    //
    // Σ(price × quantity)
    // -------------------
    // Σ(quantity)
    //
    // Time-window implementation.
    //
    // Amortized O(1)
    // ============================================================
    static class RollingVWAP {

        static class Trade {

            final long timestampMillis;
            final double price;
            final long quantity;

            Trade(long timestampMillis,
                  double price,
                  long quantity) {

                this.timestampMillis =
                        timestampMillis;

                this.price =
                        price;

                this.quantity =
                        quantity;
            }
        }

        private final long windowMillis;

        private final Deque<Trade>
                trades =
                new ArrayDeque<>();

        private double
                priceTimesQuantity;

        private long
                totalQuantity;

        RollingVWAP(
                long windowMillis) {

            if (windowMillis <= 0) {

                throw new IllegalArgumentException(
                        "windowMillis must be positive"
                );
            }

            this.windowMillis =
                    windowMillis;
        }

        public double addAndGetVWAP(
                Trade trade) {

            if (trade.quantity <= 0) {

                throw new IllegalArgumentException(
                        "Quantity must be positive"
                );
            }

            trades.addLast(
                    trade);

            priceTimesQuantity +=
                    trade.price
                            * trade.quantity;

            totalQuantity +=
                    trade.quantity;

            evictOld(
                    trade.timestampMillis);

            return currentVWAP();
        }

        private void evictOld(
                long now) {

            long cutoff =
                    now - windowMillis;

            while (!trades.isEmpty()
                    &&
                    trades.peekFirst()
                            .timestampMillis
                            <= cutoff) {

                Trade old =
                        trades.pollFirst();

                priceTimesQuantity -=
                        old.price
                                * old.quantity;

                totalQuantity -=
                        old.quantity;
            }
        }

        public double currentVWAP() {

            if (totalQuantity == 0) {
                return Double.NaN;
            }

            return priceTimesQuantity
                    / totalQuantity;
        }
    }


    // ============================================================
    // DSA-157 — Market Data Sliding Maximum
    //
    // Monotonic deque.
    //
    // O(n)
    // Space O(k)
    // ============================================================
    static class SlidingMaximum {

        public static int[]
        maxSlidingWindow(
                int[] values,
                int k) {

            if (values == null
                    || values.length == 0
                    || k <= 0
                    || k > values.length) {

                return new int[0];
            }

            int[] result =
                    new int[
                            values.length
                                    - k
                                    + 1];

            // Stores indexes.
            //
            // Values corresponding to indexes
            // are monotonically decreasing.
            Deque<Integer> deque =
                    new ArrayDeque<>();

            for (int i = 0;
                 i < values.length;
                 i++) {

                // Remove expired indexes.
                while (!deque.isEmpty()
                        &&
                        deque.peekFirst()
                                <= i - k) {

                    deque.pollFirst();
                }

                // Remove smaller values.
                while (!deque.isEmpty()
                        &&
                        values[
                                deque.peekLast()]
                                <= values[i]) {

                    deque.pollLast();
                }

                deque.addLast(i);

                if (i >= k - 1) {

                    result[
                            i - k + 1]
                            =
                            values[
                                    deque.peekFirst()];
                }
            }

            return result;
        }
    }


    // ============================================================
    // DSA-158 — Order State Aggregation
    //
    // NEW
    // ACK
    // FILL
    // CANCEL
    // REJECT
    //
    // O(n)
    // ============================================================
    static class OrderStateAggregator {

        enum EventType {
            NEW,
            ACK,
            FILL,
            CANCEL,
            REJECT
        }

        enum State {
            NEW,
            ACKNOWLEDGED,
            PARTIALLY_FILLED,
            FILLED,
            CANCELED,
            REJECTED
        }

        static class Event {

            final String orderId;
            final EventType type;
            final long quantity;

            Event(
                    String orderId,
                    EventType type,
                    long quantity) {

                this.orderId =
                        orderId;

                this.type =
                        type;

                this.quantity =
                        quantity;
            }
        }

        static class OrderInfo {

            long originalQuantity;
            long filledQuantity;

            State state;

            @Override
            public String toString() {

                return "OrderInfo{" +
                        "original=" +
                        originalQuantity +
                        ", filled=" +
                        filledQuantity +
                        ", state=" +
                        state +
                        '}';
            }
        }

        private final Map<
                String,
                OrderInfo>
                orders =
                new HashMap<>();

        public void apply(
                Event event) {

            if (event.type
                    == EventType.NEW) {

                if (event.quantity <= 0) {

                    throw new IllegalArgumentException(
                            "NEW quantity must be positive"
                    );
                }

                if (orders.containsKey(
                        event.orderId)) {

                    throw new IllegalStateException(
                            "Duplicate NEW"
                    );
                }

                OrderInfo info =
                        new OrderInfo();

                info.originalQuantity =
                        event.quantity;

                info.state =
                        State.NEW;

                orders.put(
                        event.orderId,
                        info);

                return;
            }

            OrderInfo info =
                    orders.get(
                            event.orderId);

            if (info == null) {

                throw new IllegalStateException(
                        "Unknown order "
                                + event.orderId
                );
            }

            if (isTerminal(
                    info.state)) {

                throw new IllegalStateException(
                        "Order already terminal: "
                                + event.orderId
                );
            }

            switch (event.type) {

                case ACK:

                    info.state =
                            State.ACKNOWLEDGED;

                    break;

                case FILL:

                    if (event.quantity <= 0) {

                        throw new IllegalArgumentException(
                                "FILL quantity must be positive"
                        );
                    }

                    if (info.filledQuantity
                            + event.quantity
                            >
                            info.originalQuantity) {

                        throw new IllegalStateException(
                                "Overfill"
                        );
                    }

                    info.filledQuantity +=
                            event.quantity;

                    if (info.filledQuantity
                            ==
                            info.originalQuantity) {

                        info.state =
                                State.FILLED;

                    } else {

                        info.state =
                                State.PARTIALLY_FILLED;
                    }

                    break;

                case CANCEL:

                    info.state =
                            State.CANCELED;

                    break;

                case REJECT:

                    info.state =
                            State.REJECTED;

                    break;

                default:

                    throw new IllegalStateException(
                            "Unsupported event"
                    );
            }
        }

        private boolean isTerminal(
                State state) {

            return state == State.FILLED
                    ||
                    state == State.CANCELED
                    ||
                    state == State.REJECTED;
        }

        public OrderInfo get(
                String orderId) {

            return orders.get(
                    orderId);
        }
    }


    // ============================================================
    // DSA-159 — Match Buy and Sell Orders
    //
    // PRICE-TIME PRIORITY
    //
    // BUY:
    // Highest price first.
    //
    // SELL:
    // Lowest price first.
    //
    // Same price:
    // earliest sequence first.
    //
    // Trade executes at resting order price.
    // ============================================================
    static class MatchingEngine {

        enum Side {
            BUY, SELL
        }

        static class Order {

            final long id;
            final Side side;
            final int price;

            long remainingQty;

            final long sequence;

            Order(
                    long id,
                    Side side,
                    int price,
                    long quantity,
                    long sequence) {

                this.id = id;
                this.side = side;
                this.price = price;
                this.remainingQty =
                        quantity;
                this.sequence =
                        sequence;
            }

            @Override
            public String toString() {

                return "Order{" +
                        id +
                        ", " +
                        side +
                        ", px=" +
                        price +
                        ", rem=" +
                        remainingQty +
                        '}';
            }
        }

        static class Trade {

            final long buyOrderId;
            final long sellOrderId;
            final int price;
            final long quantity;

            Trade(
                    long buyOrderId,
                    long sellOrderId,
                    int price,
                    long quantity) {

                this.buyOrderId =
                        buyOrderId;

                this.sellOrderId =
                        sellOrderId;

                this.price =
                        price;

                this.quantity =
                        quantity;
            }

            @Override
            public String toString() {

                return "Trade{" +
                        "buy=" +
                        buyOrderId +
                        ", sell=" +
                        sellOrderId +
                        ", px=" +
                        price +
                        ", qty=" +
                        quantity +
                        '}';
            }
        }

        private final PriorityQueue<Order>
                bids =
                new PriorityQueue<>(
                        Comparator
                                .comparingInt(
                                        (Order o) ->
                                                o.price)
                                .reversed()
                                .thenComparingLong(
                                        o ->
                                                o.sequence)
                );

        private final PriorityQueue<Order>
                asks =
                new PriorityQueue<>(
                        Comparator
                                .comparingInt(
                                        (Order o) ->
                                                o.price)
                                .thenComparingLong(
                                        o ->
                                                o.sequence)
                );

        public List<Trade> submit(
                Order incoming) {

            if (incoming.remainingQty
                    <= 0) {

                throw new IllegalArgumentException(
                        "Quantity must be positive"
                );
            }

            List<Trade> trades =
                    new ArrayList<>();

            if (incoming.side
                    == Side.BUY) {

                matchBuy(
                        incoming,
                        trades);

            } else {

                matchSell(
                        incoming,
                        trades);
            }

            return trades;
        }

        private void matchBuy(
                Order incoming,
                List<Trade> trades) {

            while (
                    incoming.remainingQty > 0
                            &&
                            !asks.isEmpty()
                            &&
                            incoming.price
                                    >=
                                    asks.peek().price) {

                Order resting =
                        asks.peek();

                long quantity =
                        Math.min(
                                incoming.remainingQty,
                                resting.remainingQty);

                /*
                 * Incoming BUY matches resting SELL.
                 *
                 * Price = resting order price.
                 */
                trades.add(
                        new Trade(
                                incoming.id,
                                resting.id,
                                resting.price,
                                quantity
                        )
                );

                incoming.remainingQty -=
                        quantity;

                resting.remainingQty -=
                        quantity;

                if (resting.remainingQty
                        == 0) {

                    asks.poll();
                }
            }

            if (incoming.remainingQty
                    > 0) {

                bids.offer(
                        incoming);
            }
        }

        private void matchSell(
                Order incoming,
                List<Trade> trades) {

            while (
                    incoming.remainingQty > 0
                            &&
                            !bids.isEmpty()
                            &&
                            incoming.price
                                    <=
                                    bids.peek().price) {

                Order resting =
                        bids.peek();

                long quantity =
                        Math.min(
                                incoming.remainingQty,
                                resting.remainingQty);

                /*
                 * Incoming SELL matches resting BUY.
                 *
                 * Resting price wins.
                 */
                trades.add(
                        new Trade(
                                resting.id,
                                incoming.id,
                                resting.price,
                                quantity
                        )
                );

                incoming.remainingQty -=
                        quantity;

                resting.remainingQty -=
                        quantity;

                if (resting.remainingQty
                        == 0) {

                    bids.poll();
                }
            }

            if (incoming.remainingQty
                    > 0) {

                asks.offer(
                        incoming);
            }
        }

        public Order bestBid() {
            return bids.peek();
        }

        public Order bestAsk() {
            return asks.peek();
        }
    }


    // ============================================================
    // DSA-160 — Position From Executions
    //
    // BUY  -> +quantity
    // SELL -> -quantity
    //
    // Expected O(1) / execution.
    // ============================================================
    static class PositionTracker {

        enum Side {
            BUY, SELL
        }

        static class Key {

            final String account;
            final String instrument;

            Key(
                    String account,
                    String instrument) {

                this.account =
                        account;

                this.instrument =
                        instrument;
            }

            @Override
            public boolean equals(
                    Object o) {

                if (this == o) {
                    return true;
                }

                if (!(o instanceof Key)) {
                    return false;
                }

                Key key =
                        (Key) o;

                return Objects.equals(
                        account,
                        key.account)
                        &&
                        Objects.equals(
                                instrument,
                                key.instrument);
            }

            @Override
            public int hashCode() {

                return Objects.hash(
                        account,
                        instrument);
            }
        }

        private final Map<Key, Long>
                positions =
                new HashMap<>();

        public void onExecution(
                String account,
                String instrument,
                Side side,
                long quantity) {

            long signedQuantity =
                    side == Side.BUY
                            ? quantity
                            : -quantity;

            Key key =
                    new Key(
                            account,
                            instrument);

            positions.put(
                    key,
                    positions.getOrDefault(
                            key,
                            0L)
                            +
                            signedQuantity
            );
        }

        public long position(
                String account,
                String instrument) {

            return positions.getOrDefault(
                    new Key(
                            account,
                            instrument),
                    0L
            );
        }
    }


    // ============================================================
    // DSA-161 — Detect Duplicate Orders
    //
    // Composite business identity:
    //
    // account
    // +
    // clientOrderId
    // +
    // instrument
    //
    // Expected O(1).
    // ============================================================
    static class DuplicateOrderDetector {

        static class BusinessKey {

            final String account;
            final String clientOrderId;
            final String instrument;

            BusinessKey(
                    String account,
                    String clientOrderId,
                    String instrument) {

                this.account =
                        account;

                this.clientOrderId =
                        clientOrderId;

                this.instrument =
                        instrument;
            }

            @Override
            public boolean equals(
                    Object o) {

                if (this == o) {
                    return true;
                }

                if (!(o instanceof BusinessKey)) {
                    return false;
                }

                BusinessKey that =
                        (BusinessKey) o;

                return Objects.equals(
                        account,
                        that.account)
                        &&
                        Objects.equals(
                                clientOrderId,
                                that.clientOrderId)
                        &&
                        Objects.equals(
                                instrument,
                                that.instrument);
            }

            @Override
            public int hashCode() {

                return Objects.hash(
                        account,
                        clientOrderId,
                        instrument);
            }
        }

        private final Set<BusinessKey>
                seen =
                new HashSet<>();

        public boolean isDuplicate(
                String account,
                String clientOrderId,
                String instrument) {

            return !seen.add(
                    new BusinessKey(
                            account,
                            clientOrderId,
                            instrument)
            );
        }
    }


    // ============================================================
    // DSA-162 — Most Active Instruments
    //
    // Rolling time-window volume.
    //
    // Ingestion:
    // amortized O(1)
    //
    // Top K:
    // O(M log K)
    //
    // M = number of active instruments.
    // ============================================================
    static class MostActiveInstruments {

        static class Event {

            final long timestampMillis;
            final String instrument;
            final long volume;

            Event(
                    long timestampMillis,
                    String instrument,
                    long volume) {

                this.timestampMillis =
                        timestampMillis;

                this.instrument =
                        instrument;

                this.volume =
                        volume;
            }
        }

        static class Activity {

            final String instrument;
            final long volume;

            Activity(
                    String instrument,
                    long volume) {

                this.instrument =
                        instrument;

                this.volume =
                        volume;
            }

            @Override
            public String toString() {

                return instrument
                        +
                        "="
                        +
                        volume;
            }
        }

        private final long windowMillis;

        private final Deque<Event>
                window =
                new ArrayDeque<>();

        private final Map<
                String,
                Long>
                volumeByInstrument =
                new HashMap<>();

        MostActiveInstruments(
                long windowMillis) {

            if (windowMillis <= 0) {

                throw new IllegalArgumentException(
                        "windowMillis must be positive"
                );
            }

            this.windowMillis =
                    windowMillis;
        }

        public void add(
                Event event) {

            if (event.volume < 0) {

                throw new IllegalArgumentException(
                        "Volume cannot be negative"
                );
            }

            window.addLast(
                    event);

            volumeByInstrument.put(
                    event.instrument,

                    volumeByInstrument
                            .getOrDefault(
                                    event.instrument,
                                    0L)
                            +
                            event.volume
            );

            evict(
                    event.timestampMillis);
        }

        private void evict(
                long now) {

            long cutoff =
                    now - windowMillis;

            while (
                    !window.isEmpty()
                            &&
                            window.peekFirst()
                                    .timestampMillis
                                    <= cutoff) {

                Event old =
                        window.pollFirst();

                long next =
                        volumeByInstrument
                                .get(old.instrument)
                                -
                                old.volume;

                if (next == 0) {

                    volumeByInstrument.remove(
                            old.instrument);

                } else {

                    volumeByInstrument.put(
                            old.instrument,
                            next);
                }
            }
        }

        public List<Activity>
        topK(int k) {

            if (k <= 0) {

                return Collections.emptyList();
            }

            /*
             * Smallest activity kept at heap root.
             */
            PriorityQueue<Activity>
                    minHeap =
                    new PriorityQueue<>(
                            Comparator
                                    .comparingLong(
                                            (Activity a) ->
                                                    a.volume)
                                    .thenComparing(
                                            a ->
                                                    a.instrument)
                    );

            for (Map.Entry<
                    String,
                    Long> entry
                    :
                    volumeByInstrument
                            .entrySet()) {

                minHeap.offer(
                        new Activity(
                                entry.getKey(),
                                entry.getValue())
                );

                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }

            List<Activity> result =
                    new ArrayList<>();

            while (!minHeap.isEmpty()) {
                result.add(
                        minHeap.poll());
            }

            Collections.reverse(
                    result);

            return result;
        }
    }


    // ============================================================
    // DSA-163 — Merge Multiple Ordered Market Feeds
    //
    // Same pattern as:
    // Merge K Sorted Lists
    //
    // O(N log K)
    // Space O(K)
    // ============================================================
    static class MarketFeedMerger {

        static class FeedEvent {

            final long timestamp;
            final int feedId;
            final long feedSequence;
            final String payload;

            FeedEvent(
                    long timestamp,
                    int feedId,
                    long feedSequence,
                    String payload) {

                this.timestamp =
                        timestamp;

                this.feedId =
                        feedId;

                this.feedSequence =
                        feedSequence;

                this.payload =
                        payload;
            }

            @Override
            public String toString() {

                return "("
                        +
                        timestamp
                        +
                        ", feed="
                        +
                        feedId
                        +
                        ", seq="
                        +
                        feedSequence
                        +
                        ", "
                        +
                        payload
                        +
                        ")";
            }
        }

        static class Cursor {

            final int feedIndex;
            final int eventIndex;
            final FeedEvent event;

            Cursor(
                    int feedIndex,
                    int eventIndex,
                    FeedEvent event) {

                this.feedIndex =
                        feedIndex;

                this.eventIndex =
                        eventIndex;

                this.event =
                        event;
            }
        }

        public static List<FeedEvent>
        merge(
                List<
                        List<FeedEvent>>
                        feeds) {

            PriorityQueue<Cursor>
                    heap =
                    new PriorityQueue<>(
                            Comparator
                                    .comparingLong(
                                            (Cursor c) ->
                                                    c.event.timestamp)
                                    .thenComparingInt(
                                            c ->
                                                    c.event.feedId)
                                    .thenComparingLong(
                                            c ->
                                                    c.event.feedSequence)
                    );

            /*
             * Put first event of every feed
             * into heap.
             */
            for (int i = 0;
                 i < feeds.size();
                 i++) {

                if (!feeds.get(i)
                        .isEmpty()) {

                    heap.offer(
                            new Cursor(
                                    i,
                                    0,
                                    feeds
                                            .get(i)
                                            .get(0)
                            )
                    );
                }
            }

            List<FeedEvent> result =
                    new ArrayList<>();

            while (!heap.isEmpty()) {

                Cursor cursor =
                        heap.poll();

                result.add(
                        cursor.event);

                int nextIndex =
                        cursor.eventIndex
                                + 1;

                if (nextIndex
                        <
                        feeds
                                .get(
                                        cursor.feedIndex)
                                .size()) {

                    heap.offer(
                            new Cursor(
                                    cursor.feedIndex,
                                    nextIndex,
                                    feeds
                                            .get(
                                                    cursor.feedIndex)
                                            .get(
                                                    nextIndex)
                            )
                    );
                }
            }

            return result;
        }
    }


    // ============================================================
    // DSA-164 — Snapshot + Incremental Merge
    //
    // Snapshot at sequence X
    // +
    // updates X+1 ...
    //
    // Null value means delete.
    //
    // Expected O(S + U)
    // ============================================================
    static class SnapshotIncrementalMerger {

        static class Update {

            final long sequence;
            final String key;

            // null means delete.
            final Long value;

            Update(
                    long sequence,
                    String key,
                    Long value) {

                this.sequence =
                        sequence;

                this.key =
                        key;

                this.value =
                        value;
            }
        }

        static class Snapshot {

            final long sequence;

            final Map<
                    String,
                    Long>
                    state;

            Snapshot(
                    long sequence,
                    Map<String, Long> state) {

                this.sequence =
                        sequence;

                this.state =
                        new HashMap<>(
                                state);
            }
        }

        public static Map<String, Long>
        merge(
                Snapshot snapshot,
                List<Update> updates) {

            Map<String, Long> state =
                    new HashMap<>(
                            snapshot.state);

            long lastSequence =
                    snapshot.sequence;

            for (Update update
                    : updates) {

                /*
                 * Already represented
                 * by snapshot.
                 */
                if (update.sequence
                        <= snapshot.sequence) {

                    continue;
                }

                if (update.sequence
                        <= lastSequence) {

                    throw new IllegalArgumentException(
                            "Updates must be strictly ordered"
                    );
                }

                if (update.value == null) {

                    state.remove(
                            update.key);

                } else {

                    state.put(
                            update.key,
                            update.value);
                }

                lastSequence =
                        update.sequence;
            }

            return state;
        }
    }


    // ============================================================
    // DSA-165 — Order Latency Percentile
    //
    // Nearest-rank percentile.
    //
    // Uses Quickselect rather than sorting.
    //
    // Expected O(n)
    // Worst O(n²)
    //
    // Copy space O(n)
    // ============================================================
    static class LatencyPercentile {

        public static long percentile(
                long[] latencies,
                double percentile) {

            if (latencies == null
                    ||
                    latencies.length == 0) {

                throw new IllegalArgumentException(
                        "latencies cannot be empty"
                );
            }

            if (percentile <= 0.0
                    ||
                    percentile > 100.0) {

                throw new IllegalArgumentException(
                        "percentile must be in (0, 100]"
                );
            }

            /*
             * Don't mutate caller input.
             */
            long[] copy =
                    Arrays.copyOf(
                            latencies,
                            latencies.length);

            /*
             * Nearest-rank definition:
             *
             * rank =
             * ceil(P / 100 × N)
             *
             * Convert to zero-based index.
             */
            int rank =
                    (int)
                            Math.ceil(
                                    percentile
                                            / 100.0
                                            *
                                            copy.length
                            )
                            - 1;

            return quickSelect(
                    copy,
                    0,
                    copy.length - 1,
                    rank
            );
        }

        private static long quickSelect(
                long[] array,
                int left,
                int right,
                int targetIndex) {

            while (left <= right) {

                int pivotIndex =
                        partition(
                                array,
                                left,
                                right);

                if (pivotIndex
                        == targetIndex) {

                    return array[
                            pivotIndex];
                }

                if (pivotIndex
                        < targetIndex) {

                    left =
                            pivotIndex + 1;

                } else {

                    right =
                            pivotIndex - 1;
                }
            }

            throw new IllegalStateException(
                    "Unreachable"
            );
        }

        private static int partition(
                long[] array,
                int left,
                int right) {

            /*
             * Middle-element pivot avoids
             * trivial already-sorted worst case
             * compared with always using last.
             */
            int middle =
                    left
                            +
                            (right - left)
                                    / 2;

            swap(
                    array,
                    middle,
                    right);

            long pivot =
                    array[right];

            int store =
                    left;

            for (int i = left;
                 i < right;
                 i++) {

                if (array[i]
                        <= pivot) {

                    swap(
                            array,
                            store,
                            i);

                    store++;
                }
            }

            swap(
                    array,
                    store,
                    right);

            return store;
        }

        private static void swap(
                long[] array,
                int i,
                int j) {

            long temp =
                    array[i];

            array[i] =
                    array[j];

            array[j] =
                    temp;
        }
    }


    // ============================================================
    // MAIN
    //
    // Running smoke tests for ALL 19 problems.
    // ============================================================
    public static void main(String[] args) {

        // ========================================================
        // DSA-147
        // ========================================================

        System.out.println(
                "=== DSA-147 Best Bid / Best Ask ===");

        BestBidAskBook bbo =
                new BestBidAskBook();

        bbo.updateBid(
                100,
                10);

        bbo.updateBid(
                101,
                5);

        bbo.updateAsk(
                103,
                7);

        bbo.updateAsk(
                102,
                8);

        System.out.println(
                "Best bid="
                        +
                        bbo.bestBid()
                        +
                        ", best ask="
                        +
                        bbo.bestAsk()
        );


        // ========================================================
        // DSA-148
        // ========================================================

        System.out.println(
                "\n=== DSA-148 Top N Price Levels ===");

        TopNPriceLevels top =
                new TopNPriceLevels();

        top.update(
                TopNPriceLevels.Side.BUY,
                100,
                10);

        top.update(
                TopNPriceLevels.Side.BUY,
                101,
                20);

        top.update(
                TopNPriceLevels.Side.BUY,
                99,
                30);

        System.out.println(
                top.topN(
                        TopNPriceLevels.Side.BUY,
                        2)
        );


        // ========================================================
        // DSA-149
        // ========================================================

        System.out.println(
                "\n=== DSA-149 Price-Time Priority ===");

        PriceTimePriorityQueue pt =
                new PriceTimePriorityQueue();

        pt.add(
                new PriceTimePriorityQueue.Order(
                        1,
                        PriceTimePriorityQueue.Side.BUY,
                        100,
                        2)
        );

        pt.add(
                new PriceTimePriorityQueue.Order(
                        2,
                        PriceTimePriorityQueue.Side.BUY,
                        101,
                        3)
        );

        pt.add(
                new PriceTimePriorityQueue.Order(
                        3,
                        PriceTimePriorityQueue.Side.BUY,
                        101,
                        1)
        );

        System.out.println(
                "Next buy="
                        +
                        pt.peekNext(
                                PriceTimePriorityQueue
                                        .Side.BUY)
        );


        // ========================================================
        // DSA-150
        // ========================================================

        System.out.println(
                "\n=== DSA-150 Execution Deduplication ===");

        ExecutionDeduplicator dedupe =
                new ExecutionDeduplicator();

        System.out.println(
                dedupe.process(
                        new ExecutionDeduplicator.Execution(
                                "E1",
                                10))
        );

        System.out.println(
                dedupe.process(
                        new ExecutionDeduplicator.Execution(
                                "E1",
                                10))
        );

        System.out.println(
                "Total qty="
                        +
                        dedupe.totalQuantity()
        );


        // ========================================================
        // DSA-151
        // ========================================================

        System.out.println(
                "\n=== DSA-151 Sequence Gap ===");

        System.out.println(
                SequenceGapDetector
                        .findMissingRanges(
                                new long[]{
                                        1,
                                        2,
                                        5,
                                        6,
                                        10
                                })
        );


        // ========================================================
        // DSA-152
        // ========================================================

        System.out.println(
                "\n=== DSA-152 Out-of-Order Reordering ===");

        BoundedMessageReorderer<String>
                reorderer =
                new BoundedMessageReorderer<>(
                        1,
                        10);

        System.out.println(
                reorderer.onMessage(
                        new BoundedMessageReorderer.Message<>(
                                2,
                                "B"))
        );

        System.out.println(
                reorderer.onMessage(
                        new BoundedMessageReorderer.Message<>(
                                1,
                                "A"))
        );

        System.out.println(
                reorderer.onMessage(
                        new BoundedMessageReorderer.Message<>(
                                3,
                                "C"))
        );


        // ========================================================
        // DSA-153
        // ========================================================

        System.out.println(
                "\n=== DSA-153 Rolling Exposure ===");

        RollingExposure exposure =
                new RollingExposure();

        exposure.apply(
                "ACC1",
                "AAPL",
                100);

        exposure.apply(
                "ACC1",
                "AAPL",
                -30);

        System.out.println(
                "Exposure="
                        +
                        exposure.get(
                                "ACC1",
                                "AAPL")
        );


        // ========================================================
        // DSA-154
        // ========================================================

        System.out.println(
                "\n=== DSA-154 Price Deviation ===");

        PriceDeviationChecker checker =
                new PriceDeviationChecker();

        checker.updateReference(
                "AAPL",
                100.0);

        System.out.println(
                "104 within 5%? "
                        +
                        !checker.violates(
                                "AAPL",
                                104.0,
                                5.0)
        );

        System.out.println(
                "106 violates 5%? "
                        +
                        checker.violates(
                                "AAPL",
                                106.0,
                                5.0)
        );


        // ========================================================
        // DSA-155
        // ========================================================

        System.out.println(
                "\n=== DSA-155 Exchange Throttle ===");

        ExchangeThrottle throttle =
                new ExchangeThrottle(
                        2,
                        1000);

        System.out.println(
                throttle.allow(
                        "ACC1",
                        1000)
        );

        System.out.println(
                throttle.allow(
                        "ACC1",
                        1100)
        );

        System.out.println(
                throttle.allow(
                        "ACC1",
                        1200)
        );

        System.out.println(
                throttle.allow(
                        "ACC1",
                        2101)
        );


        // ========================================================
        // DSA-156
        // ========================================================

        System.out.println(
                "\n=== DSA-156 Rolling VWAP ===");

        RollingVWAP vwap =
                new RollingVWAP(
                        1000);

        System.out.println(
                vwap.addAndGetVWAP(
                        new RollingVWAP.Trade(
                                1000,
                                100.0,
                                10))
        );

        System.out.println(
                vwap.addAndGetVWAP(
                        new RollingVWAP.Trade(
                                1200,
                                110.0,
                                20))
        );


        // ========================================================
        // DSA-157
        // ========================================================

        System.out.println(
                "\n=== DSA-157 Sliding Maximum ===");

        System.out.println(
                Arrays.toString(
                        SlidingMaximum
                                .maxSlidingWindow(
                                        new int[]{
                                                1,
                                                3,
                                                -1,
                                                -3,
                                                5,
                                                3,
                                                6,
                                                7
                                        },
                                        3)
                )
        );


        // ========================================================
        // DSA-158
        // ========================================================

        System.out.println(
                "\n=== DSA-158 Order State Aggregation ===");

        OrderStateAggregator aggregator =
                new OrderStateAggregator();

        aggregator.apply(
                new OrderStateAggregator.Event(
                        "O1",
                        OrderStateAggregator.EventType.NEW,
                        100)
        );

        aggregator.apply(
                new OrderStateAggregator.Event(
                        "O1",
                        OrderStateAggregator.EventType.ACK,
                        0)
        );

        aggregator.apply(
                new OrderStateAggregator.Event(
                        "O1",
                        OrderStateAggregator.EventType.FILL,
                        40)
        );

        aggregator.apply(
                new OrderStateAggregator.Event(
                        "O1",
                        OrderStateAggregator.EventType.FILL,
                        60)
        );

        System.out.println(
                aggregator.get(
                        "O1")
        );


        // ========================================================
        // DSA-159
        // ========================================================

        System.out.println(
                "\n=== DSA-159 Matching Engine ===");

        MatchingEngine engine =
                new MatchingEngine();

        engine.submit(
                new MatchingEngine.Order(
                        1,
                        MatchingEngine.Side.SELL,
                        101,
                        50,
                        1)
        );

        engine.submit(
                new MatchingEngine.Order(
                        2,
                        MatchingEngine.Side.SELL,
                        102,
                        50,
                        2)
        );

        List<MatchingEngine.Trade>
                trades =
                engine.submit(
                        new MatchingEngine.Order(
                                3,
                                MatchingEngine.Side.BUY,
                                102,
                                70,
                                3)
                );

        System.out.println(
                trades);

        System.out.println(
                "Remaining best ask="
                        +
                        engine.bestAsk()
        );


        // ========================================================
        // DSA-160
        // ========================================================

        System.out.println(
                "\n=== DSA-160 Position From Executions ===");

        PositionTracker positions =
                new PositionTracker();

        positions.onExecution(
                "ACC1",
                "AAPL",
                PositionTracker.Side.BUY,
                100);

        positions.onExecution(
                "ACC1",
                "AAPL",
                PositionTracker.Side.SELL,
                35);

        System.out.println(
                "Position="
                        +
                        positions.position(
                                "ACC1",
                                "AAPL")
        );


        // ========================================================
        // DSA-161
        // ========================================================

        System.out.println(
                "\n=== DSA-161 Duplicate Orders ===");

        DuplicateOrderDetector duplicateOrders =
                new DuplicateOrderDetector();

        System.out.println(
                duplicateOrders.isDuplicate(
                        "ACC1",
                        "C1",
                        "AAPL")
        );

        System.out.println(
                duplicateOrders.isDuplicate(
                        "ACC1",
                        "C1",
                        "AAPL")
        );


        // ========================================================
        // DSA-162
        // ========================================================

        System.out.println(
                "\n=== DSA-162 Most Active Instruments ===");

        MostActiveInstruments active =
                new MostActiveInstruments(
                        1000);

        active.add(
                new MostActiveInstruments.Event(
                        1000,
                        "AAPL",
                        100)
        );

        active.add(
                new MostActiveInstruments.Event(
                        1100,
                        "MSFT",
                        300)
        );

        active.add(
                new MostActiveInstruments.Event(
                        1200,
                        "AAPL",
                        250)
        );

        System.out.println(
                active.topK(
                        2)
        );


        // ========================================================
        // DSA-163
        // ========================================================

        System.out.println(
                "\n=== DSA-163 Merge K Market Feeds ===");

        List<List<
                MarketFeedMerger.FeedEvent>>
                feeds =
                new ArrayList<>();

        feeds.add(
                Arrays.asList(

                        new MarketFeedMerger.FeedEvent(
                                1000,
                                0,
                                1,
                                "A"),

                        new MarketFeedMerger.FeedEvent(
                                1300,
                                0,
                                2,
                                "C")
                )
        );

        feeds.add(
                Arrays.asList(

                        new MarketFeedMerger.FeedEvent(
                                1100,
                                1,
                                1,
                                "B"),

                        new MarketFeedMerger.FeedEvent(
                                1400,
                                1,
                                2,
                                "D")
                )
        );

        System.out.println(
                MarketFeedMerger.merge(
                        feeds)
        );


        // ========================================================
        // DSA-164
        // ========================================================

        System.out.println(
                "\n=== DSA-164 Snapshot + Incremental Merge ===");

        Map<String, Long> baseline =
                new HashMap<>();

        baseline.put(
                "AAPL",
                100L);

        SnapshotIncrementalMerger.Snapshot
                snapshot =
                new SnapshotIncrementalMerger.Snapshot(
                        10,
                        baseline);

        List<
                SnapshotIncrementalMerger.Update>
                updates =
                Arrays.asList(

                        new SnapshotIncrementalMerger.Update(
                                11,
                                "AAPL",
                                120L),

                        new SnapshotIncrementalMerger.Update(
                                12,
                                "MSFT",
                                200L),

                        new SnapshotIncrementalMerger.Update(
                                13,
                                "AAPL",
                                null)
                );

        System.out.println(
                SnapshotIncrementalMerger.merge(
                        snapshot,
                        updates)
        );


        // ========================================================
        // DSA-165
        // ========================================================

        System.out.println(
                "\n=== DSA-165 Latency Percentiles ===");

        long[] latencies = {
                10,
                20,
                15,
                100,
                50,
                30,
                40,
                200,
                25,
                35
        };

        System.out.println(
                "p50="
                        +
                        LatencyPercentile.percentile(
                                latencies,
                                50)
        );

        System.out.println(
                "p95="
                        +
                        LatencyPercentile.percentile(
                                latencies,
                                95)
        );

        System.out.println(
                "p99="
                        +
                        LatencyPercentile.percentile(
                                latencies,
                                99)
        );
    }
}