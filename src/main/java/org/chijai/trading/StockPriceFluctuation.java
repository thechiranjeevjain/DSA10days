package org.chijai.trading;

import java.util.*;

/**
 * LeetCode 2034 - Stock Price Fluctuation
 *
 * Problem:
 * We receive stock price records as:
 *
 *      timestamp -> price
 *
 * Records can:
 * 1. arrive out of order
 * 2. correct a previously seen timestamp
 *
 * Required operations:
 * - update(timestamp, price)
 * - current()  -> price at latest timestamp
 * - maximum()  -> maximum current price
 * - minimum()  -> minimum current price
 *
 * -------------------------------------------------------------------------
 * CORE DESIGN
 * -------------------------------------------------------------------------
 *
 * We need TWO different views of the same data.
 *
 * 1) timestamp -> current price
 *
 *      HashMap<Integer, Integer>
 *
 *    Why?
 *    Because when a correction comes for an existing timestamp,
 *    we must know the OLD price so we can remove it from the global
 *    ordered price structure.
 *
 * 2) price -> frequency
 *
 *      TreeMap<Integer, Integer>
 *
 *    Why frequency?
 *    Multiple timestamps may have the same price.
 *
 *    Example:
 *
 *      timestamp 1 -> 100
 *      timestamp 2 -> 100
 *
 *    If timestamp 1 gets corrected, price 100 must still remain
 *    because timestamp 2 still has price 100.
 *
 * 3) latestTimestamp
 *
 *      int latestTimestamp
 *
 *    Because records arrive out of order.
 *    "Latest" means the largest timestamp seen so far,
 *    NOT the most recently received update.
 *
 * -------------------------------------------------------------------------
 * INVARIANT
 * -------------------------------------------------------------------------
 *
 * timestampToPrice contains exactly one CURRENT price per timestamp.
 *
 * priceFrequency contains exactly the same prices, aggregated by count.
 *
 * If a correction arrives:
 *
 *      old price OUT
 *      new price IN
 *
 * This is the crucial invariant-preserving operation.
 *
 * -------------------------------------------------------------------------
 * COMPLEXITY
 * -------------------------------------------------------------------------
 *
 * update()   -> O(log N)
 * current()  -> O(1)
 * maximum()  -> O(log N) for TreeMap.lastKey()
 * minimum()  -> O(log N) for TreeMap.firstKey()
 * space      -> O(N)
 *
 * N = number of distinct timestamps currently stored.
 */
public class StockPriceFluctuation {

    static class StockPrice {

        // timestamp -> latest corrected price
        private final Map<Integer, Integer> timestampToPrice = new HashMap<>();

        // price -> number of timestamps currently having this price
        private final TreeMap<Integer, Integer> priceFrequency = new TreeMap<>();

        // Largest timestamp ever seen.
        private int latestTimestamp = -1;

        /**
         * Update or correct the stock price at a timestamp.
         *
         * Example:
         *
         *      update(1, 10)
         *      update(2, 5)
         *      update(1, 3)
         *
         * The last call is a correction:
         *
         *      old price 10 must be removed
         *      new price 3 must be added
         */
        public void update(int timestamp, int price) {

            // latest timestamp is based on timestamp value,
            // not arrival order.
            latestTimestamp = Math.max(latestTimestamp, timestamp);

            // If this timestamp already exists,
            // remove its OLD price from the ordered multiset.
            if (timestampToPrice.containsKey(timestamp)) {
                int oldPrice = timestampToPrice.get(timestamp);
                removePrice(oldPrice);
            }

            // Store the corrected/latest value for this timestamp.
            timestampToPrice.put(timestamp, price);

            // Add the new price to the ordered multiset.
            priceFrequency.merge(price, 1, Integer::sum);
        }

        /**
         * Return price at the largest timestamp seen so far.
         */
        public int current() {
            return timestampToPrice.get(latestTimestamp);
        }

        /**
         * Return the maximum CURRENT stock price.
         *
         * TreeMap stores prices in sorted ascending order,
         * so the largest key is the maximum price.
         */
        public int maximum() {
            return priceFrequency.lastKey();
        }

        /**
         * Return the minimum CURRENT stock price.
         *
         * TreeMap stores prices in sorted ascending order,
         * so the smallest key is the minimum price.
         */
        public int minimum() {
            return priceFrequency.firstKey();
        }

        /**
         * Remove exactly ONE occurrence of a price.
         *
         * We cannot blindly remove the TreeMap key because
         * another timestamp may still have the same price.
         */
        private void removePrice(int price) {

            int frequency = priceFrequency.get(price);

            if (frequency == 1) {
                priceFrequency.remove(price);
            } else {
                priceFrequency.put(price, frequency - 1);
            }
        }
    }

    // ---------------------------------------------------------------------
    // TEST HARNESS
    // ---------------------------------------------------------------------

    private static void assertEquals(int expected, int actual, String testName) {
        if (expected != actual) {
            throw new AssertionError(
                    testName + " FAILED: expected=" + expected + ", actual=" + actual
            );
        }

        System.out.println("PASS: " + testName + " -> " + actual);
    }

    /**
     * Exact LeetCode example.
     */
    private static void testLeetCodeExample() {

        System.out.println("\n=== Test 1: LeetCode Example ===");

        StockPrice stockPrice = new StockPrice();

        stockPrice.update(1, 10);
        stockPrice.update(2, 5);

        assertEquals(
                5,
                stockPrice.current(),
                "Current after timestamps 1 and 2"
        );

        assertEquals(
                10,
                stockPrice.maximum(),
                "Maximum before correction"
        );

        stockPrice.update(1, 3);

        assertEquals(
                5,
                stockPrice.maximum(),
                "Maximum after correcting timestamp 1"
        );

        stockPrice.update(4, 2);

        assertEquals(
                2,
                stockPrice.minimum(),
                "Minimum after adding timestamp 4"
        );
    }

    /**
     * Records arrive out of order.
     *
     * Latest means maximum timestamp,
     * NOT latest arrival.
     */
    private static void testOutOfOrderArrival() {

        System.out.println("\n=== Test 2: Out Of Order Arrival ===");

        StockPrice stockPrice = new StockPrice();

        stockPrice.update(10, 100);
        stockPrice.update(5, 500);
        stockPrice.update(7, 700);

        assertEquals(
                100,
                stockPrice.current(),
                "Current uses largest timestamp, not last arrival"
        );

        assertEquals(
                700,
                stockPrice.maximum(),
                "Maximum with out-of-order arrivals"
        );

        assertEquals(
                100,
                stockPrice.minimum(),
                "Minimum with out-of-order arrivals"
        );
    }

    /**
     * Correction of the latest timestamp.
     */
    private static void testCorrectionAtLatestTimestamp() {

        System.out.println("\n=== Test 3: Correction At Latest Timestamp ===");

        StockPrice stockPrice = new StockPrice();

        stockPrice.update(1, 10);
        stockPrice.update(2, 20);

        assertEquals(
                20,
                stockPrice.current(),
                "Initial current"
        );

        stockPrice.update(2, 5);

        assertEquals(
                5,
                stockPrice.current(),
                "Current after correcting latest timestamp"
        );

        assertEquals(
                10,
                stockPrice.maximum(),
                "Maximum after correcting latest timestamp"
        );

        assertEquals(
                5,
                stockPrice.minimum(),
                "Minimum after correcting latest timestamp"
        );
    }

    /**
     * Duplicate prices prove why we need frequency counts.
     */
    private static void testDuplicatePrices() {

        System.out.println("\n=== Test 4: Duplicate Prices ===");

        StockPrice stockPrice = new StockPrice();

        stockPrice.update(1, 100);
        stockPrice.update(2, 100);
        stockPrice.update(3, 50);

        assertEquals(
                100,
                stockPrice.maximum(),
                "Maximum with duplicate prices"
        );

        // Correct only one of the two timestamps having price 100.
        stockPrice.update(1, 25);

        // Price 100 must still exist because timestamp 2 still has it.
        assertEquals(
                100,
                stockPrice.maximum(),
                "One duplicate remains after correction"
        );

        assertEquals(
                25,
                stockPrice.minimum(),
                "New minimum after correction"
        );
    }

    /**
     * A correction can completely remove an old maximum.
     */
    private static void testMaximumRemovedByCorrection() {

        System.out.println("\n=== Test 5: Maximum Removed By Correction ===");

        StockPrice stockPrice = new StockPrice();

        stockPrice.update(1, 10);
        stockPrice.update(2, 50);
        stockPrice.update(3, 30);

        assertEquals(
                50,
                stockPrice.maximum(),
                "Initial maximum"
        );

        stockPrice.update(2, 20);

        assertEquals(
                30,
                stockPrice.maximum(),
                "Maximum after removing old maximum"
        );
    }

    /**
     * A correction can completely remove an old minimum.
     */
    private static void testMinimumRemovedByCorrection() {

        System.out.println("\n=== Test 6: Minimum Removed By Correction ===");

        StockPrice stockPrice = new StockPrice();

        stockPrice.update(1, 10);
        stockPrice.update(2, 50);
        stockPrice.update(3, 30);

        assertEquals(
                10,
                stockPrice.minimum(),
                "Initial minimum"
        );

        stockPrice.update(1, 40);

        assertEquals(
                30,
                stockPrice.minimum(),
                "Minimum after removing old minimum"
        );
    }

    public static void main(String[] args) {

        testLeetCodeExample();
        testOutOfOrderArrival();
        testCorrectionAtLatestTimestamp();
        testDuplicatePrices();
        testMaximumRemovedByCorrection();
        testMinimumRemovedByCorrection();

        System.out.println("\nALL TESTS PASSED");
    }
}
