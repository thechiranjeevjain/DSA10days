package org.chijai.trading;

import java.util.*;

/**
 * LeetCode 346 - Moving Average from Data Stream
 *
 * Problem:
 * Given a stream of integers and a fixed window size,
 * return the moving average of the most recent values.
 *
 * Example:
 *
 * size = 3
 *
 * next(1)   -> 1.0
 * next(10)  -> (1 + 10) / 2 = 5.5
 * next(3)   -> (1 + 10 + 3) / 3 = 4.666...
 * next(5)   -> (10 + 3 + 5) / 3 = 6.0
 *
 * -------------------------------------------------------------------------
 * CORE IDEA
 * -------------------------------------------------------------------------
 *
 * We only care about the most recent 'size' elements.
 *
 * Data structure:
 *
 *      Queue<Integer> window
 *
 * We also maintain:
 *
 *      long sum
 *
 * Why keep a running sum?
 *
 * Without it, every next() call would need to scan the whole window:
 *
 *      O(size)
 *
 * With a running sum:
 *
 *      add new value
 *      remove oldest if needed
 *      update sum
 *      divide
 *
 * So:
 *
 *      next() = O(1)
 *
 * -------------------------------------------------------------------------
 * INVARIANT
 * -------------------------------------------------------------------------
 *
 * At all times:
 *
 *      queue contains exactly the current sliding window
 *
 * and:
 *
 *      sum == sum of all values currently inside the queue
 *
 * -------------------------------------------------------------------------
 * WINDOW RULE
 * -------------------------------------------------------------------------
 *
 * If queue size becomes greater than capacity:
 *
 *      remove oldest element
 *
 * That is why FIFO / Queue is the natural structure.
 *
 * -------------------------------------------------------------------------
 * COMPLEXITY
 * -------------------------------------------------------------------------
 *
 * next() -> O(1)
 * space  -> O(size)
 *
 * -------------------------------------------------------------------------
 * NUMERIC DETAIL
 * -------------------------------------------------------------------------
 *
 * Use long for sum.
 *
 * Even if individual values fit inside int,
 * summing many values can overflow int in more general variants.
 */
public class MovingAverageFromDataStream {

    static class MovingAverage {

        private final int capacity;

        // Stores only the current sliding window.
        private final Queue<Integer> window = new ArrayDeque<>();

        // Running sum of values in the current window.
        private long sum = 0;

        public MovingAverage(int size) {
            this.capacity = size;
        }

        /**
         * Add a new value and return the average of the
         * most recent 'capacity' values.
         */
        public double next(int val) {

            // Add newest value.
            window.offer(val);
            sum += val;

            // If the window is too large,
            // remove the oldest value.
            if (window.size() > capacity) {
                int removed = window.poll();
                sum -= removed;
            }

            // Important:
            // Before the window becomes full,
            // divide by the CURRENT number of elements.
            return (double) sum / window.size();
        }
    }

    // ---------------------------------------------------------------------
    // TEST HARNESS
    // ---------------------------------------------------------------------

    private static void assertDoubleEquals(
            double expected,
            double actual,
            double tolerance,
            String testName
    ) {

        if (Math.abs(expected - actual) > tolerance) {
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

    /**
     * Canonical LeetCode example.
     */
    private static void testLeetCodeExample() {

        System.out.println("\n=== Test 1: LeetCode Example ===");

        MovingAverage movingAverage = new MovingAverage(3);

        assertDoubleEquals(
                1.0,
                movingAverage.next(1),
                1e-9,
                "next(1)"
        );

        assertDoubleEquals(
                5.5,
                movingAverage.next(10),
                1e-9,
                "next(10)"
        );

        assertDoubleEquals(
                14.0 / 3.0,
                movingAverage.next(3),
                1e-9,
                "next(3)"
        );

        assertDoubleEquals(
                6.0,
                movingAverage.next(5),
                1e-9,
                "next(5)"
        );
    }

    /**
     * Window size = 1.
     *
     * Every new value completely replaces the previous one.
     */
    private static void testWindowSizeOne() {

        System.out.println("\n=== Test 2: Window Size One ===");

        MovingAverage movingAverage = new MovingAverage(1);

        assertDoubleEquals(
                5.0,
                movingAverage.next(5),
                1e-9,
                "First value"
        );

        assertDoubleEquals(
                10.0,
                movingAverage.next(10),
                1e-9,
                "Second value replaces first"
        );

        assertDoubleEquals(
                -3.0,
                movingAverage.next(-3),
                1e-9,
                "Third value replaces second"
        );
    }

    /**
     * Before the window is full,
     * average uses only the elements seen so far.
     */
    private static void testPartiallyFilledWindow() {

        System.out.println("\n=== Test 3: Partially Filled Window ===");

        MovingAverage movingAverage = new MovingAverage(5);

        assertDoubleEquals(
                2.0,
                movingAverage.next(2),
                1e-9,
                "One element"
        );

        assertDoubleEquals(
                3.0,
                movingAverage.next(4),
                1e-9,
                "Two elements"
        );

        assertDoubleEquals(
                4.0,
                movingAverage.next(6),
                1e-9,
                "Three elements"
        );
    }

    /**
     * Tests proper eviction of the oldest element.
     *
     * size = 3
     *
     * stream:
     * 1, 2, 3 -> avg = 2
     * then 100
     *
     * window must become:
     *
     * 2, 3, 100
     *
     * not:
     *
     * 1, 2, 3, 100
     */
    private static void testOldestEviction() {

        System.out.println("\n=== Test 4: Oldest Eviction ===");

        MovingAverage movingAverage = new MovingAverage(3);

        movingAverage.next(1);
        movingAverage.next(2);

        assertDoubleEquals(
                2.0,
                movingAverage.next(3),
                1e-9,
                "Average before eviction"
        );

        assertDoubleEquals(
                35.0,
                movingAverage.next(100),
                1e-9,
                "Oldest value evicted"
        );
    }

    /**
     * Negative numbers should work naturally.
     */
    private static void testNegativeValues() {

        System.out.println("\n=== Test 5: Negative Values ===");

        MovingAverage movingAverage = new MovingAverage(3);

        assertDoubleEquals(
                -10.0,
                movingAverage.next(-10),
                1e-9,
                "First negative"
        );

        assertDoubleEquals(
                -15.0,
                movingAverage.next(-20),
                1e-9,
                "Two negatives"
        );

        assertDoubleEquals(
                -20.0,
                movingAverage.next(-30),
                1e-9,
                "Three negatives"
        );

        assertDoubleEquals(
                -30.0,
                movingAverage.next(-40),
                1e-9,
                "Sliding negative window"
        );
    }

    /**
     * Mixed positive and negative values.
     */
    private static void testMixedValues() {

        System.out.println("\n=== Test 6: Mixed Values ===");

        MovingAverage movingAverage = new MovingAverage(4);

        movingAverage.next(10);
        movingAverage.next(-10);
        movingAverage.next(20);

        assertDoubleEquals(
                5.0,
                movingAverage.next(0),
                1e-9,
                "Mixed window"
        );

        // Oldest 10 is evicted:
        // [-10, 20, 0, 30] => 40 / 4 = 10
        assertDoubleEquals(
                10.0,
                movingAverage.next(30),
                1e-9,
                "Mixed window after eviction"
        );
    }

    /**
     * Tests repeated equal values.
     */
    private static void testRepeatedValues() {

        System.out.println("\n=== Test 7: Repeated Values ===");

        MovingAverage movingAverage = new MovingAverage(3);

        assertDoubleEquals(
                7.0,
                movingAverage.next(7),
                1e-9,
                "Repeated 1"
        );

        assertDoubleEquals(
                7.0,
                movingAverage.next(7),
                1e-9,
                "Repeated 2"
        );

        assertDoubleEquals(
                7.0,
                movingAverage.next(7),
                1e-9,
                "Repeated 3"
        );

        assertDoubleEquals(
                7.0,
                movingAverage.next(7),
                1e-9,
                "Repeated after sliding"
        );
    }

    public static void main(String[] args) {

        testLeetCodeExample();
        testWindowSizeOne();
        testPartiallyFilledWindow();
        testOldestEviction();
        testNegativeValues();
        testMixedValues();
        testRepeatedValues();

        System.out.println("\nALL TESTS PASSED");
    }
}
