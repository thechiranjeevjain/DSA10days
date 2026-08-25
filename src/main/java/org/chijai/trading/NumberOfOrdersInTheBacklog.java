package org.chijai.trading;

import java.util.*;
import java.util.stream.Stream;

/**
 * LeetCode 1801 - Number of Orders in the Backlog
 *
 * ============================================================================
 * AUDITED FINAL INTERVIEW VERSION
 * ============================================================================
 *
 * PRIMARY:
 *      PriorityQueue<Order>
 *
 * ALTERNATIVE:
 *      TreeMap<Price, TotalQuantity>
 *
 * MATCHING:
 *
 *      Incoming BUY
 *          -> match cheapest SELL
 *          -> bestAsk <= buyPrice
 *
 *      Incoming SELL
 *          -> match highest BUY
 *          -> bestBid >= sellPrice
 *
 * ============================================================================
 * COMPLEXITY
 * ============================================================================
 *
 * Let:
 *
 *      N = number of input order batches
 *      P = maximum number of active distinct price levels
 *
 * PriorityQueue:
 *
 *      Time  : O(N log N)
 *      Space : O(N)
 *
 * TreeMap:
 *
 *      Time  : O(N log P)
 *              worst case O(N log N)
 *
 *      Space : O(P)
 *              worst case O(N)
 *
 * Why are the nested while-loops not O(N^2)?
 *
 * Every successful matching iteration either:
 *
 *      1) finishes the incoming order, or
 *      2) completely consumes a resting heap entry / price level.
 *
 * Across the complete input, fully consumed entries/levels are bounded
 * by entries/levels that were previously inserted.
 */
public class NumberOfOrdersInTheBacklog {

    private static final int MOD = 1_000_000_007;

    // =========================================================================
    // APPROACH 1: PRIORITY QUEUE
    // =========================================================================

    /**
     * Small mutable heap object.
     *
     * price:
     *      immutable because it determines heap ordering
     *
     * quantity:
     *      mutable because partial fills reduce it
     *
     * We never mutate price while the Order is inside a PriorityQueue.
     *
     * Reads use price() / quantity() for consistency.
     * Quantity mutation stays direct inside the matching algorithm.
     */
    static final class Order {

        private final int price;
        private int quantity;

        Order(int price, int quantity) {
            this.price = price;
            this.quantity = quantity;
        }

        int price() {
            return price;
        }

        int quantity() {
            return quantity;
        }
    }

    /**
     * Exact LeetCode method.
     *
     * BUY  -> MAX heap by price
     * SELL -> MIN heap by price
     *
     * We intentionally keep explicit BUY / SELL branches.
     *
     * There is some symmetric duplication, but the business rule remains
     * immediately visible and easy to verify during an interview.
     *
     * Same-price heap ordering is intentionally unspecified here because
     * LC 1801 only asks for the final backlog quantity. Price-time FIFO is
     * introduced explicitly in the production extension below.
     */
    public int getNumberOfBacklogOrders(int[][] orders) {

        PriorityQueue<Order> buyOrders = new PriorityQueue<>(
                Comparator.comparingInt(Order::price).reversed()
        );

        PriorityQueue<Order> sellOrders = new PriorityQueue<>(
                Comparator.comparingInt(Order::price)
        );

        for (int[] order : orders) {

            // LeetCode input format:
            // [price, quantity, type]
            int price = order[0];
            int quantity = order[1];
            int type = order[2];

            if (type == 0) {

                /*
                 * BUY:
                 * Match against the cheapest SELL.
                 */
                while (quantity > 0
                        && !sellOrders.isEmpty()
                        && sellOrders.peek().price() <= price) {

                    Order bestSell = sellOrders.peek();

                    int matchedQuantity = Math.min(
                            quantity,
                            bestSell.quantity()
                    );

                    quantity -= matchedQuantity;
                    bestSell.quantity -= matchedQuantity;

                    if (bestSell.quantity() == 0) {
                        sellOrders.poll();
                    }
                }

                if (quantity > 0) {
                    buyOrders.offer(
                            new Order(price, quantity)
                    );
                }

            } else {

                /*
                 * SELL:
                 * Match against the highest BUY.
                 */
                while (quantity > 0
                        && !buyOrders.isEmpty()
                        && buyOrders.peek().price() >= price) {

                    Order bestBuy = buyOrders.peek();

                    int matchedQuantity = Math.min(
                            quantity,
                            bestBuy.quantity()
                    );

                    quantity -= matchedQuantity;
                    bestBuy.quantity -= matchedQuantity;

                    if (bestBuy.quantity() == 0) {
                        buyOrders.poll();
                    }
                }

                if (quantity > 0) {
                    sellOrders.offer(
                            new Order(price, quantity)
                    );
                }
            }
        }

        /*
         * Functional Java adds value here because this is pure aggregation.
         *
         * Maximum possible total quantity is about 1e14,
         * which safely fits in long.
         */
        long backlog = Stream.concat(
                        buyOrders.stream(),
                        sellOrders.stream()
                )
                .mapToLong(Order::quantity)
                .sum();

        return (int) (backlog % MOD);
    }

    // =========================================================================
    // APPROACH 2: TREEMAP / PRICE LEVELS
    // =========================================================================

    /**
     * Representation:
     *
     *      price -> TOTAL resting quantity at that price
     *
     * BUY:
     *      descending TreeMap
     *
     * SELL:
     *      ascending TreeMap
     *
     * Therefore BOTH books expose their best price using firstEntry().
     *
     * Same-price quantities naturally aggregate with:
     *
     *      merge(price, quantity, Long::sum)
     */
    public int getNumberOfBacklogOrdersTreeMap(int[][] orders) {

        TreeMap<Integer, Long> buyBook =
                new TreeMap<>(Comparator.reverseOrder());

        TreeMap<Integer, Long> sellBook =
                new TreeMap<>();

        for (int[] order : orders) {

            int price = order[0];
            long quantity = order[1];
            int type = order[2];

            if (type == 0) {

                /*
                 * BUY:
                 * sellBook is ascending.
                 * firstEntry() = cheapest ask.
                 */
                while (quantity > 0
                        && !sellBook.isEmpty()) {

                    Map.Entry<Integer, Long> bestAsk =
                            sellBook.firstEntry();

                    int bestAskPrice = bestAsk.getKey();

                    if (bestAskPrice > price) {
                        break;
                    }

                    long availableQuantity = bestAsk.getValue();

                    long matchedQuantity = Math.min(
                            quantity,
                            availableQuantity
                    );

                    quantity -= matchedQuantity;
                    availableQuantity -= matchedQuantity;

                    if (availableQuantity == 0) {
                        sellBook.pollFirstEntry();
                    } else {
                        sellBook.put(
                                bestAskPrice,
                                availableQuantity
                        );
                    }
                }

                if (quantity > 0) {
                    buyBook.merge(
                            price,
                            quantity,
                            Long::sum
                    );
                }

            } else {

                /*
                 * SELL:
                 * buyBook is descending.
                 * firstEntry() = highest bid.
                 */
                while (quantity > 0
                        && !buyBook.isEmpty()) {

                    Map.Entry<Integer, Long> bestBid =
                            buyBook.firstEntry();

                    int bestBidPrice = bestBid.getKey();

                    if (bestBidPrice < price) {
                        break;
                    }

                    long availableQuantity = bestBid.getValue();

                    long matchedQuantity = Math.min(
                            quantity,
                            availableQuantity
                    );

                    quantity -= matchedQuantity;
                    availableQuantity -= matchedQuantity;

                    if (availableQuantity == 0) {
                        buyBook.pollFirstEntry();
                    } else {
                        buyBook.put(
                                bestBidPrice,
                                availableQuantity
                        );
                    }
                }

                if (quantity > 0) {
                    sellBook.merge(
                            price,
                            quantity,
                            Long::sum
                    );
                }
            }
        }

        long backlog = Stream.concat(
                        buyBook.values().stream(),
                        sellBook.values().stream()
                )
                .mapToLong(Long::longValue)
                .sum();

        return (int) (backlog % MOD);
    }

    // =========================================================================
    // COMPARISON / TRADE-OFFS
    // =========================================================================

    /*
     * -------------------------------------------------------------------------
     *                         PRIORITY QUEUE          TREEMAP
     * -------------------------------------------------------------------------
     *
     * Represents              order batches           price levels
     *
     * BUY best                max-heap peek()         firstEntry()
     *
     * SELL best               min-heap peek()         firstEntry()
     *
     * Insert                  O(log N)                O(log P)
     *
     * Remove best             O(log N)                O(log P)
     *
     * Same-price aggregate    No                      Yes
     *
     * Sorted price levels     No                      Yes
     *
     * Space                   O(N)                    O(P)
     *
     * Specific-order cancel   not modeled             not possible after
     *                                                   price aggregation
     *
     * Coding simplicity       BEST                    Good
     *
     * Price-level modeling    Good                    BEST
     *
     * -------------------------------------------------------------------------
     *
     * Cancellation note:
     *
     *      The LC TreeMap stores only price -> total quantity, so individual
     *      order identity is intentionally lost. Specific-order cancellation
     *      requires the production extension below: PriceLevel + orderId index.
     *
     * LC 1801:
     *
     *      PriorityQueue
     *          -> simplest interview implementation
     *
     *      TreeMap
     *          -> strongest price-level representation
     *
     * Both are correct.
     * Both are O(N log N) in the worst case.
     */

    // =========================================================================
    // RELATED INTERVIEW EXTENSION:
    // PRODUCTION-LIKE IN-MEMORY LIMIT ORDER BOOK
    // =========================================================================

    /**
     * Natural LLD follow-up:
     *
     * "Now extend the LeetCode matcher into a small production-like
     * in-memory limit order book."
     *
     * Requirements:
     *
     *      1) submit BUY / SELL limit orders
     *      2) price-time priority
     *      3) partial fills
     *      4) resting unmatched quantity
     *      5) generated trades
     *      6) cancel by orderId
     *      7) replace order
     *      8) best bid / best ask
     *
     * -------------------------------------------------------------------------
     * WHY LC 1801'S REPRESENTATION IS NO LONGER ENOUGH
     * -------------------------------------------------------------------------
     *
     * LC 1801 only needs aggregate backlog quantity.
     *
     * A production-like book needs order identity and FIFO ordering:
     *
     *      TreeMap<Price, PriceLevel>
     *                  +
     *      HashMap<OrderId, LimitOrder>
     *
     * TreeMap:
     *      PRICE priority
     *
     * PriceLevel:
     *      TIME / FIFO priority inside one price
     *
     * HashMap:
     *      fast lookup for cancel / replace
     *
     * -------------------------------------------------------------------------
     * WHY LinkedHashMap INSIDE A PRICE LEVEL?
     * -------------------------------------------------------------------------
     *
     *      insertion order       -> FIFO
     *      remove(orderId)       -> average O(1)
     *
     * A plain Deque gives FIFO nicely, but arbitrary cancellation inside
     * the level would require a scan unless we add more indexing.
     *
     * -------------------------------------------------------------------------
     * COMPLEXITY
     * -------------------------------------------------------------------------
     *
     * Let:
     *
     *      P = number of active price levels
     *      M = number of resting orders matched by one incoming order
     *
     * submit:
     *
     *      O((M + 1) log P) safe interview upper bound
     *
     * cancel:
     *
     *      O(log P) price-level lookup/removal
     *      + average O(1) LinkedHashMap removal
     *
     * bestBid / bestAsk:
     *
     *      O(log P) conservative interview answer
     *
     * space:
     *
     *      O(N) resting orders
     *
     * -------------------------------------------------------------------------
     * IMPORTANT:
     *
     * This is production-LIKE interview scope, not an exchange-grade engine.
     *
     * Real systems may additionally require:
     *
     *      deterministic sequencing
     *      persistence / replay
     *      idempotency
     *      risk checks
     *      IOC / FOK / market orders
     *      self-trade prevention
     *      snapshots / recovery
     *      allocation / GC control
     *      single-writer or other concurrency architecture
     */

    enum Side {
        BUY,
        SELL
    }

    /**
     * Class is justified here because remainingQuantity mutates.
     */
    static final class LimitOrder {

        final long orderId;
        final Side side;
        final int price;
        long remainingQuantity;

        LimitOrder(
                long orderId,
                Side side,
                int price,
                long remainingQuantity
        ) {
            this.orderId = orderId;
            this.side = side;
            this.price = price;
            this.remainingQuantity = remainingQuantity;
        }
    }

    /**
     * Trade is immutable, so record is a good fit.
     */
    record Trade(
            long buyOrderId,
            long sellOrderId,
            int price,
            long quantity
    ) {
    }

    /**
     * FIFO orders at one price.
     */
    static final class PriceLevel {

        private final LinkedHashMap<Long, LimitOrder> orders =
                new LinkedHashMap<>();

        void add(LimitOrder order) {
            orders.put(order.orderId, order);
        }

        LimitOrder firstOrder() {

            if (orders.isEmpty()) {
                return null;
            }

            return orders.entrySet()
                    .iterator()
                    .next()
                    .getValue();
        }

        LimitOrder remove(long orderId) {
            return orders.remove(orderId);
        }

        boolean isEmpty() {
            return orders.isEmpty();
        }
    }

    static final class OrderBook {

        /*
         * BUY:
         * descending -> firstEntry() = highest bid
         */
        private final TreeMap<Integer, PriceLevel> buyLevels =
                new TreeMap<>(Comparator.reverseOrder());

        /*
         * SELL:
         * ascending -> firstEntry() = lowest ask
         */
        private final TreeMap<Integer, PriceLevel> sellLevels =
                new TreeMap<>();

        /*
         * Only currently resting orders are indexed here.
         */
        private final Map<Long, LimitOrder> orderById =
                new HashMap<>();

        List<Trade> submit(
                long orderId,
                Side side,
                int price,
                long quantity
        ) {

            validateLimitOrder(price, quantity);

            if (orderById.containsKey(orderId)) {
                throw new IllegalArgumentException(
                        "duplicate active orderId: " + orderId
                );
            }

            LimitOrder incoming = new LimitOrder(
                    orderId,
                    side,
                    price,
                    quantity
            );

            List<Trade> trades = new ArrayList<>();

            if (side == Side.BUY) {
                matchBuy(incoming, trades);
            } else {
                matchSell(incoming, trades);
            }

            if (incoming.remainingQuantity > 0) {
                addRestingOrder(incoming);
            }

            return trades;
        }

        boolean cancel(long orderId) {

            LimitOrder order = orderById.get(orderId);

            if (order == null) {
                return false;
            }

            TreeMap<Integer, PriceLevel> levels =
                    order.side == Side.BUY
                            ? buyLevels
                            : sellLevels;

            PriceLevel level = levels.get(order.price);

            if (level == null) {
                throw new IllegalStateException(
                        "order index and price levels are inconsistent"
                );
            }

            LimitOrder removed = level.remove(orderId);

            if (removed == null) {
                throw new IllegalStateException(
                        "order index and price level are inconsistent"
                );
            }

            if (level.isEmpty()) {
                levels.remove(order.price);
            }

            orderById.remove(orderId);

            return true;
        }

        /**
         * Interview semantics:
         *
         *      replace = cancel + submit
         *
         * Therefore the order loses FIFO priority.
         *
         * Validate BEFORE cancel so an invalid replacement cannot delete
         * a valid resting order.
         */
        List<Trade> replace(
                long orderId,
                int newPrice,
                long newQuantity
        ) {

            validateLimitOrder(newPrice, newQuantity);

            LimitOrder existing = orderById.get(orderId);

            if (existing == null) {
                throw new IllegalArgumentException(
                        "orderId is not resting: " + orderId
                );
            }

            Side side = existing.side;

            cancel(orderId);

            return submit(
                    orderId,
                    side,
                    newPrice,
                    newQuantity
            );
        }

        Integer bestBid() {
            return buyLevels.isEmpty()
                    ? null
                    : buyLevels.firstKey();
        }

        Integer bestAsk() {
            return sellLevels.isEmpty()
                    ? null
                    : sellLevels.firstKey();
        }

        Long remainingQuantity(long orderId) {

            LimitOrder order = orderById.get(orderId);

            return order == null
                    ? null
                    : order.remainingQuantity;
        }

        boolean contains(long orderId) {
            return orderById.containsKey(orderId);
        }

        private static void validateLimitOrder(
                int price,
                long quantity
        ) {

            if (price <= 0) {
                throw new IllegalArgumentException(
                        "price must be positive"
                );
            }

            if (quantity <= 0) {
                throw new IllegalArgumentException(
                        "quantity must be positive"
                );
            }
        }

        private void matchBuy(
                LimitOrder incomingBuy,
                List<Trade> trades
        ) {

            while (incomingBuy.remainingQuantity > 0
                    && !sellLevels.isEmpty()) {

                Map.Entry<Integer, PriceLevel> bestAskEntry =
                        sellLevels.firstEntry();

                int bestAskPrice = bestAskEntry.getKey();

                if (bestAskPrice > incomingBuy.price) {
                    break;
                }

                PriceLevel bestAskLevel =
                        bestAskEntry.getValue();

                LimitOrder restingSell =
                        bestAskLevel.firstOrder();

                long matchedQuantity = Math.min(
                        incomingBuy.remainingQuantity,
                        restingSell.remainingQuantity
                );

                incomingBuy.remainingQuantity -= matchedQuantity;
                restingSell.remainingQuantity -= matchedQuantity;

                /*
                 * Common convention:
                 * execute at the resting order's price.
                 */
                trades.add(
                        new Trade(
                                incomingBuy.orderId,
                                restingSell.orderId,
                                restingSell.price,
                                matchedQuantity
                        )
                );

                if (restingSell.remainingQuantity == 0) {

                    bestAskLevel.remove(restingSell.orderId);
                    orderById.remove(restingSell.orderId);

                    if (bestAskLevel.isEmpty()) {
                        sellLevels.pollFirstEntry();
                    }
                }
            }
        }

        private void matchSell(
                LimitOrder incomingSell,
                List<Trade> trades
        ) {

            while (incomingSell.remainingQuantity > 0
                    && !buyLevels.isEmpty()) {

                Map.Entry<Integer, PriceLevel> bestBidEntry =
                        buyLevels.firstEntry();

                int bestBidPrice = bestBidEntry.getKey();

                if (bestBidPrice < incomingSell.price) {
                    break;
                }

                PriceLevel bestBidLevel =
                        bestBidEntry.getValue();

                LimitOrder restingBuy =
                        bestBidLevel.firstOrder();

                long matchedQuantity = Math.min(
                        incomingSell.remainingQuantity,
                        restingBuy.remainingQuantity
                );

                incomingSell.remainingQuantity -= matchedQuantity;
                restingBuy.remainingQuantity -= matchedQuantity;

                trades.add(
                        new Trade(
                                restingBuy.orderId,
                                incomingSell.orderId,
                                restingBuy.price,
                                matchedQuantity
                        )
                );

                if (restingBuy.remainingQuantity == 0) {

                    bestBidLevel.remove(restingBuy.orderId);
                    orderById.remove(restingBuy.orderId);

                    if (bestBidLevel.isEmpty()) {
                        buyLevels.pollFirstEntry();
                    }
                }
            }
        }

        private void addRestingOrder(LimitOrder order) {

            TreeMap<Integer, PriceLevel> levels =
                    order.side == Side.BUY
                            ? buyLevels
                            : sellLevels;

            PriceLevel level = levels.computeIfAbsent(
                    order.price,
                    ignored -> new PriceLevel()
            );

            level.add(order);
            orderById.put(order.orderId, order);
        }
    }

    /*
     * FOLLOW-UP FLAVOURS
     * -------------------------------------------------------------------------
     *
     * Market order:
     *      consume opposite side without a price constraint.
     *
     * IOC:
     *      execute immediately; discard remainder.
     *
     * FOK:
     *      execute only if full quantity can be filled.
     *
     * Modify:
     *      price change / quantity increase normally loses priority;
     *      quantity decrease may preserve it depending on venue rules.
     *
     * Top-K depth:
     *      walk first K TreeMap levels.
     *
     * Concurrency:
     *      a single-writer event loop per instrument/partition is a strong
     *      design to discuss for deterministic matching.
     *
     * Persistence:
     *      durable event log + snapshot + replay.
     *
     * Low latency:
     *      bounded tick ranges may justify arrays / bitmaps / specialized
     *      primitive structures instead of TreeMap.
     */


    // =========================================================================
    // TEST HARNESS
    // =========================================================================

    private void assertBoth(
            int expected,
            int[][] orders,
            String testName
    ) {

        int priorityQueueResult =
                getNumberOfBacklogOrders(orders);

        int treeMapResult =
                getNumberOfBacklogOrdersTreeMap(orders);

        assertEquals(
                expected,
                priorityQueueResult,
                testName + " [PriorityQueue]"
        );

        assertEquals(
                expected,
                treeMapResult,
                testName + " [TreeMap]"
        );
    }

    private void assertEquals(
            int expected,
            int actual,
            String testName
    ) {

        if (expected != actual) {
            throw new AssertionError(
                    testName
                            + " FAILED: expected=" + expected
                            + ", actual=" + actual
            );
        }

        System.out.println(
                "PASS: " + testName + " -> " + actual
        );
    }

    private void runTests() {

        assertBoth(
                6,
                new int[][]{
                        {10, 5, 0},
                        {15, 2, 1},
                        {25, 1, 1},
                        {30, 4, 0}
                },
                "LeetCode Example 1"
        );

        assertBoth(
                999_999_984,
                new int[][]{
                        {7, 1_000_000_000, 1},
                        {15, 3, 0},
                        {5, 999_999_995, 0},
                        {5, 1, 1}
                },
                "LeetCode Example 2"
        );

        assertBoth(
                5,
                new int[][]{
                        {10, 2, 1},
                        {12, 3, 1},
                        {15, 4, 1},
                        {12, 4, 0}
                },
                "BUY consumes multiple SELL levels"
        );

        assertBoth(
                5,
                new int[][]{
                        {20, 2, 0},
                        {18, 3, 0},
                        {15, 4, 0},
                        {18, 4, 1}
                },
                "SELL consumes multiple BUY levels"
        );

        assertBoth(
                0,
                new int[][]{
                        {100, 10, 0},
                        {100, 10, 1}
                },
                "Exact-price match"
        );

        assertBoth(
                9,
                new int[][]{
                        {10, 3, 0},
                        {10, 4, 0},
                        {20, 2, 1}
                },
                "Same-price resting orders"
        );

        assertBoth(
                2,
                new int[][]{
                        {14, 4, 1},
                        {10, 3, 1},
                        {15, 5, 0}
                },
                "Best-price priority"
        );

        assertBoth(
                15,
                new int[][]{
                        {90, 7, 0},
                        {110, 8, 1}
                },
                "No crossing prices"
        );

        assertBoth(
                7,
                new int[][]{
                        {100, 3, 1},
                        {100, 10, 0}
                },
                "Partial fill leaves remainder"
        );

        assertBoth(
                999_999_986,
                new int[][]{
                        {1, 1_000_000_000, 0},
                        {1, 1_000_000_000, 0},
                        {2, 1_000_000_000, 1}
                },
                "Long arithmetic and modulo"
        );

        System.out.println("\nALL TESTS PASSED");
    }

    // =========================================================================
    // PRODUCTION-LIKE ORDER BOOK TESTS
    // =========================================================================

    private void runProductionOrderBookTests() {

        testPriceTimePriority();
        testBestBidAskAndCancel();
        testCrossesMultiplePriceLevels();
        testReplaceLosesPriority();
        testInvalidReplaceDoesNotDeleteOrder();

        System.out.println(
                "\nALL PRODUCTION-LIKE ORDER BOOK TESTS PASSED"
        );
    }

    private void testPriceTimePriority() {

        OrderBook book = new OrderBook();

        book.submit(1L, Side.SELL, 100, 5);
        book.submit(2L, Side.SELL, 100, 5);

        List<Trade> trades =
                book.submit(3L, Side.BUY, 100, 7);

        assertEquals(
                2,
                trades.size(),
                "Order book: two trades generated"
        );

        assertLongEquals(
                1L,
                trades.get(0).sellOrderId(),
                "Order book: FIFO first sell"
        );

        assertLongEquals(
                5L,
                trades.get(0).quantity(),
                "Order book: FIFO first quantity"
        );

        assertLongEquals(
                2L,
                trades.get(1).sellOrderId(),
                "Order book: FIFO second sell"
        );

        assertLongEquals(
                2L,
                trades.get(1).quantity(),
                "Order book: FIFO second quantity"
        );

        assertLongEquals(
                3L,
                book.remainingQuantity(2L),
                "Order book: partial remainder"
        );

        assertObjectEquals(
                100,
                book.bestAsk(),
                "Order book: best ask after partial fill"
        );
    }

    private void testBestBidAskAndCancel() {

        OrderBook book = new OrderBook();

        book.submit(10L, Side.BUY, 99, 10);
        book.submit(11L, Side.BUY, 101, 4);

        book.submit(20L, Side.SELL, 105, 8);
        book.submit(21L, Side.SELL, 103, 7);

        assertObjectEquals(
                101,
                book.bestBid(),
                "Order book: highest bid"
        );

        assertObjectEquals(
                103,
                book.bestAsk(),
                "Order book: lowest ask"
        );

        assertBooleanEquals(
                true,
                book.cancel(11L),
                "Order book: cancel existing order"
        );

        assertObjectEquals(
                99,
                book.bestBid(),
                "Order book: next best bid after cancel"
        );

        assertBooleanEquals(
                false,
                book.cancel(999L),
                "Order book: cancel missing order"
        );
    }

    private void testCrossesMultiplePriceLevels() {

        OrderBook book = new OrderBook();

        book.submit(100L, Side.SELL, 100, 2);
        book.submit(101L, Side.SELL, 101, 3);
        book.submit(102L, Side.SELL, 105, 4);

        List<Trade> trades =
                book.submit(200L, Side.BUY, 101, 6);

        assertEquals(
                2,
                trades.size(),
                "Order book: crosses two sell levels"
        );

        assertObjectEquals(
                101,
                book.bestBid(),
                "Order book: unmatched incoming BUY rests"
        );

        assertObjectEquals(
                105,
                book.bestAsk(),
                "Order book: non-crossing SELL remains"
        );

        assertLongEquals(
                1L,
                book.remainingQuantity(200L),
                "Order book: incoming remainder"
        );
    }

    private void testReplaceLosesPriority() {

        OrderBook book = new OrderBook();

        book.submit(300L, Side.BUY, 100, 5);
        book.submit(301L, Side.BUY, 100, 5);

        /*
         * Replace = cancel + reinsert.
         * Order 300 moves behind order 301.
         */
        book.replace(300L, 100, 5);

        List<Trade> trades =
                book.submit(400L, Side.SELL, 100, 5);

        assertEquals(
                1,
                trades.size(),
                "Order book: replace trade count"
        );

        assertLongEquals(
                301L,
                trades.get(0).buyOrderId(),
                "Order book: replaced order loses FIFO priority"
        );
    }

    private void testInvalidReplaceDoesNotDeleteOrder() {

        OrderBook book = new OrderBook();

        book.submit(500L, Side.BUY, 100, 5);

        try {
            book.replace(500L, 100, 0);

            throw new AssertionError(
                    "Order book: invalid replace should throw"
            );

        } catch (IllegalArgumentException expected) {
            // Expected.
        }

        assertBooleanEquals(
                true,
                book.contains(500L),
                "Order book: invalid replace preserves order"
        );

        assertLongEquals(
                5L,
                book.remainingQuantity(500L),
                "Order book: invalid replace preserves quantity"
        );
    }

    private void assertLongEquals(
            long expected,
            Long actual,
            String testName
    ) {

        if (actual == null || expected != actual) {
            throw new AssertionError(
                    testName
                            + " FAILED: expected=" + expected
                            + ", actual=" + actual
            );
        }

        System.out.println(
                "PASS: " + testName + " -> " + actual
        );
    }

    private void assertObjectEquals(
            Object expected,
            Object actual,
            String testName
    ) {

        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(
                    testName
                            + " FAILED: expected=" + expected
                            + ", actual=" + actual
            );
        }

        System.out.println(
                "PASS: " + testName + " -> " + actual
        );
    }

    private void assertBooleanEquals(
            boolean expected,
            boolean actual,
            String testName
    ) {

        if (expected != actual) {
            throw new AssertionError(
                    testName
                            + " FAILED: expected=" + expected
                            + ", actual=" + actual
            );
        }

        System.out.println(
                "PASS: " + testName + " -> " + actual
        );
    }


    // =========================================================================
    // MAIN
    // =========================================================================

    public static void main(String[] args) {

        NumberOfOrdersInTheBacklog solution =
                new NumberOfOrdersInTheBacklog();

        solution.runTests();

        System.out.println(
                "\n=== Production-Like Order Book Extension ===\n"
        );

        solution.runProductionOrderBookTests();

        System.out.println("""

                ============================================================
                LAST-MINUTE MEMORY CARD
                ============================================================

                MATCH

                BUY:
                    cheapest SELL <= buy price

                SELL:
                    highest BUY >= sell price


                PRIORITY QUEUE

                    BUY  = max heap
                    SELL = min heap

                    offer / peek / poll

                    Comparator.comparingInt(Order::price)
                    BUY adds .reversed()


                TREEMAP

                    BUY  = descending map
                    SELL = ascending map

                    BOTH:
                        best = firstEntry()
                        remove best = pollFirstEntry()

                    same price:
                        merge(price, quantity, Long::sum)


                FUNCTIONAL JAVA

                    Stream.concat(...)
                          .mapToLong(Order::quantity)
                          .sum()

                    Great for pure aggregation.
                    Keep matching imperative.


                CHOOSE

                    next-best item
                        -> PriorityQueue

                    sorted / aggregated price levels
                        -> TreeMap

                    production price-time order book
                        -> TreeMap<Price, PriceLevel>
                           + LinkedHashMap inside level
                           + HashMap<OrderId, LimitOrder>


                COMPLEXITY

                    PriorityQueue:
                        O(N log N) time
                        O(N) space

                    TreeMap:
                        O(N log P) time
                        O(P) space

                    TreeMap worst case:
                        O(N log N) time
                        O(N) space
                ============================================================
                """);
    }
}
