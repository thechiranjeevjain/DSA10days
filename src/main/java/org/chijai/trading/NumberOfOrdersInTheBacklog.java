package org.chijai.trading;

import java.util.*;

/**
 * LeetCode 1801 - Number of Orders in the Backlog
 *
 * -------------------------------------------------------------------------
 * INTERVIEW IDEA
 * -------------------------------------------------------------------------
 *
 * This is a tiny LIMIT ORDER BOOK.
 *
 * BUY side (bids):
 *      We need the HIGHEST buy price first.
 *
 * SELL side (asks):
 *      We need the LOWEST sell price first.
 *
 * Best data structure:
 *
 *      TreeMap<Integer, Long> buyBook
 *      TreeMap<Integer, Long> sellBook
 *
 * Each map stores:
 *
 *      price -> total amount resting at that price level
 *
 * Why aggregate by price?
 * orders[i] can contain amount up to 1,000,000,000.
 *
 * We absolutely do NOT want to insert one object per unit order.
 *
 * -------------------------------------------------------------------------
 * MATCHING RULE
 * -------------------------------------------------------------------------
 *
 * Incoming BUY(price):
 *
 *      Look at lowest SELL price.
 *
 *      Can match if:
 *
 *          bestAsk <= buyPrice
 *
 *
 * Incoming SELL(price):
 *
 *      Look at highest BUY price.
 *
 *      Can match if:
 *
 *          bestBid >= sellPrice
 *
 *
 * -------------------------------------------------------------------------
 * CORE INVARIANT
 * -------------------------------------------------------------------------
 *
 * After processing each incoming order:
 *
 *      buyBook contains only unmatched BUY quantity
 *      sellBook contains only unmatched SELL quantity
 *
 * And there can be no immediately matchable crossing pair left between
 * the incoming order and the opposite book.
 *
 * -------------------------------------------------------------------------
 * WHY TREE MAP?
 * -------------------------------------------------------------------------
 *
 * buyBook.lastEntry()   -> highest bid
 * sellBook.firstEntry() -> lowest ask
 *
 * Also:
 *
 * - updating a price level      O(log N)
 * - deleting empty price level  O(log N)
 * - finding best bid/ask        O(log N)
 *
 * -------------------------------------------------------------------------
 * COMPLEXITY
 * -------------------------------------------------------------------------
 *
 * Let N = number of input batches.
 *
 * Every price level insertion/removal costs O(log N).
 * Each resting price level can be consumed and removed at most once
 * before being inserted again by another input batch.
 *
 * Overall:
 *
 *      Time  : O(N log N)
 *      Space : O(N)
 *
 * -------------------------------------------------------------------------
 * IMPORTANT NUMERIC DETAIL
 * -------------------------------------------------------------------------
 *
 * amount <= 1e9
 * number of orders <= 1e5
 *
 * Total backlog can therefore be much larger than int.
 *
 * Use long internally.
 *
 * Apply modulo 1_000_000_007 only when computing the final answer.
 */
public class NumberOfOrdersInTheBacklog {

    private static final int MOD = 1_000_000_007;

    /**
     * LeetCode method.
     */
    public static int getNumberOfBacklogOrders(int[][] orders) {

        /*
         * BUY BOOK / BIDS
         *
         * price -> unmatched amount
         *
         * Natural ascending TreeMap:
         * highest bid = lastEntry()
         */
        TreeMap<Integer, Long> buyBook = new TreeMap<>();

        /*
         * SELL BOOK / ASKS
         *
         * price -> unmatched amount
         *
         * lowest ask = firstEntry()
         */
        TreeMap<Integer, Long> sellBook = new TreeMap<>();

        for (int[] order : orders) {

            int price = order[0];
            long amount = order[1];
            int type = order[2];

            if (type == 0) {
                // BUY order
                amount = processBuy(price, amount, sellBook);

                // Whatever could not match becomes resting bid quantity.
                if (amount > 0) {
                    buyBook.merge(price, amount, Long::sum);
                }

            } else {
                // SELL order
                amount = processSell(price, amount, buyBook);

                // Whatever could not match becomes resting ask quantity.
                if (amount > 0) {
                    sellBook.merge(price, amount, Long::sum);
                }
            }
        }

        long backlog = 0;

        for (long amount : buyBook.values()) {
            backlog = (backlog + amount) % MOD;
        }

        for (long amount : sellBook.values()) {
            backlog = (backlog + amount) % MOD;
        }

        return (int) backlog;
    }

    /**
     * Match an incoming BUY against the lowest available SELL prices.
     *
     * A buy at price P can execute against asks <= P.
     *
     * Returns the unmatched amount of the incoming buy.
     */
    private static long processBuy(
            int buyPrice,
            long buyAmount,
            TreeMap<Integer, Long> sellBook
    ) {

        while (buyAmount > 0 && !sellBook.isEmpty()) {

            Map.Entry<Integer, Long> bestAskEntry = sellBook.firstEntry();

            int bestAskPrice = bestAskEntry.getKey();

            // Cheapest seller is still too expensive.
            // Nothing else in the sell book can match either.
            if (bestAskPrice > buyPrice) {
                break;
            }

            long availableSellAmount = bestAskEntry.getValue();

            long matched = Math.min(buyAmount, availableSellAmount);

            buyAmount -= matched;
            availableSellAmount -= matched;

            if (availableSellAmount == 0) {
                sellBook.pollFirstEntry();
            } else {
                sellBook.put(bestAskPrice, availableSellAmount);
            }
        }

        return buyAmount;
    }

    /**
     * Match an incoming SELL against the highest available BUY prices.
     *
     * A sell at price P can execute against bids >= P.
     *
     * Returns the unmatched amount of the incoming sell.
     */
    private static long processSell(
            int sellPrice,
            long sellAmount,
            TreeMap<Integer, Long> buyBook
    ) {

        while (sellAmount > 0 && !buyBook.isEmpty()) {

            Map.Entry<Integer, Long> bestBidEntry = buyBook.lastEntry();

            int bestBidPrice = bestBidEntry.getKey();

            // Highest buyer is still bidding too little.
            // Nothing else in the buy book can match either.
            if (bestBidPrice < sellPrice) {
                break;
            }

            long availableBuyAmount = bestBidEntry.getValue();

            long matched = Math.min(sellAmount, availableBuyAmount);

            sellAmount -= matched;
            availableBuyAmount -= matched;

            if (availableBuyAmount == 0) {
                buyBook.pollLastEntry();
            } else {
                buyBook.put(bestBidPrice, availableBuyAmount);
            }
        }

        return sellAmount;
    }

    // ---------------------------------------------------------------------
    // TEST HARNESS
    // ---------------------------------------------------------------------

    private static void assertEquals(int expected, int actual, String testName) {

        if (expected != actual) {
            throw new AssertionError(
                    testName
                            + " FAILED: expected=" + expected
                            + ", actual=" + actual
            );
        }

        System.out.println("PASS: " + testName + " -> " + actual);
    }

    /**
     * Exact LeetCode Example 1.
     */
    private static void testExample1() {

        System.out.println("\n=== Test 1: LeetCode Example 1 ===");

        int[][] orders = {
                {10, 5, 0},
                {15, 2, 1},
                {25, 1, 1},
                {30, 4, 0}
        };

        assertEquals(
                6,
                getNumberOfBacklogOrders(orders),
                "Example 1"
        );
    }

    /**
     * Exact LeetCode Example 2.
     *
     * Important:
     * Uses quantities near 1e9.
     * This proves we must process batches, not individual units.
     */
    private static void testExample2() {

        System.out.println("\n=== Test 2: LeetCode Example 2 ===");

        int[][] orders = {
                {7, 1_000_000_000, 1},
                {15, 3, 0},
                {5, 999_999_995, 0},
                {5, 1, 1}
        };

        assertEquals(
                999_999_984,
                getNumberOfBacklogOrders(orders),
                "Example 2"
        );
    }

    /**
     * Incoming BUY consumes several SELL price levels.
     *
     * Sell book:
     *
     *      10 -> 2
     *      12 -> 3
     *      15 -> 4
     *
     * Buy:
     *
     *      price 12, amount 4
     *
     * It consumes:
     *
     *      2 @ 10
     *      2 @ 12
     *
     * Remaining:
     *
     *      1 @ 12
     *      4 @ 15
     *
     * backlog = 5
     */
    private static void testBuyConsumesMultipleAskLevels() {

        System.out.println("\n=== Test 3: Buy Consumes Multiple Ask Levels ===");

        int[][] orders = {
                {10, 2, 1},
                {12, 3, 1},
                {15, 4, 1},
                {12, 4, 0}
        };

        assertEquals(
                5,
                getNumberOfBacklogOrders(orders),
                "Buy consumes multiple ask levels"
        );
    }

    /**
     * Incoming SELL consumes several BUY price levels.
     *
     * Buy book:
     *
     *      20 -> 2
     *      18 -> 3
     *      15 -> 4
     *
     * Sell:
     *
     *      price 18, amount 4
     *
     * It consumes:
     *
     *      2 @ 20
     *      2 @ 18
     *
     * Remaining:
     *
     *      1 @ 18
     *      4 @ 15
     *
     * backlog = 5
     */
    private static void testSellConsumesMultipleBidLevels() {

        System.out.println("\n=== Test 4: Sell Consumes Multiple Bid Levels ===");

        int[][] orders = {
                {20, 2, 0},
                {18, 3, 0},
                {15, 4, 0},
                {18, 4, 1}
        };

        assertEquals(
                5,
                getNumberOfBacklogOrders(orders),
                "Sell consumes multiple bid levels"
        );
    }

    /**
     * Same price on opposite sides matches completely.
     */
    private static void testExactPriceMatch() {

        System.out.println("\n=== Test 5: Exact Price Match ===");

        int[][] orders = {
                {100, 10, 0},
                {100, 10, 1}
        };

        assertEquals(
                0,
                getNumberOfBacklogOrders(orders),
                "Exact price match"
        );
    }

    /**
     * Orders at the same price on the SAME side aggregate into one level.
     */
    private static void testSameSidePriceAggregation() {

        System.out.println("\n=== Test 6: Same-Side Price Aggregation ===");

        int[][] orders = {
                {10, 3, 0},
                {10, 4, 0},
                {20, 2, 1}
        };

        // No crossing:
        // bids = 7 @ 10
        // asks = 2 @ 20
        // total = 9
        assertEquals(
                9,
                getNumberOfBacklogOrders(orders),
                "Same-side price aggregation"
        );
    }

    /**
     * Best-price priority matters.
     *
     * Buy at 15 must consume ask 10 before ask 14.
     */
    private static void testBestPricePriority() {

        System.out.println("\n=== Test 7: Best Price Priority ===");

        int[][] orders = {
                {14, 4, 1},
                {10, 3, 1},
                {15, 5, 0}
        };

        // Buy 5 consumes:
        // 3 @ 10
        // 2 @ 14
        //
        // Leaves:
        // 2 @ 14
        assertEquals(
                2,
                getNumberOfBacklogOrders(orders),
                "Best-price priority"
        );
    }

    /**
     * Large totals verify long arithmetic and modulo behavior.
     */
    private static void testModulo() {

        System.out.println("\n=== Test 8: Modulo ===");

        int[][] orders = {
                {1, 1_000_000_000, 0},
                {1, 1_000_000_000, 0},
                {2, 1_000_000_000, 1}
        };

        /*
         * No matching:
         *
         * Buy @ 1 total = 2,000,000,000
         * Sell @ 2      = 1,000,000,000
         *
         * total = 3,000,000,000
         *
         * 3,000,000,000 % 1,000,000,007
         * = 999,999,986
         */
        assertEquals(
                999_999_986,
                getNumberOfBacklogOrders(orders),
                "Modulo with large backlog"
        );
    }

    public static void main(String[] args) {

        testExample1();
        testExample2();
        testBuyConsumesMultipleAskLevels();
        testSellConsumesMultipleBidLevels();
        testExactPriceMatch();
        testSameSidePriceAggregation();
        testBestPricePriority();
        testModulo();

        System.out.println("\nALL TESTS PASSED");
    }
}
