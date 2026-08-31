package org.chijai.day7.session1.heap;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * ============================================================================
 * FIND MEDIAN FROM DATA STREAM
 * ============================================================================
 *
 * Pattern:
 *      Two Heaps / Dynamic Order Statistics
 *
 * Invariants:
 *
 *      lower = MaxHeap = lower half
 *      upper = MinHeap = upper half
 *
 *      every lower value <= every upper value
 *
 *      lower.size() == upper.size()
 *      OR
 *      lower.size() == upper.size() + 1
 *
 * Complexity:
 *
 *      addNum      O(log n)
 *      findMedian  O(1)
 *      space       O(n)
 *
 * LeetCode:
 *      https://leetcode.com/problems/find-median-from-data-stream/
 */
public class Median {

    /*
     * =========================================================================
     * PRIMARY INTERVIEW SOLUTION
     * =========================================================================
     */

    static final class MedianFinder {

        private final PriorityQueue<Integer> lower =
                new PriorityQueue<>(Collections.reverseOrder());

        private final PriorityQueue<Integer> upper =
                new PriorityQueue<>();

        public void addNum(int num) {

            if (lower.isEmpty() || num <= lower.peek()) {
                lower.offer(num);
            } else {
                upper.offer(num);
            }

            if (lower.size() > upper.size() + 1) {
                upper.offer(lower.poll());
            } else if (upper.size() > lower.size()) {
                lower.offer(upper.poll());
            }
        }

        public double findMedian() {

            if (lower.isEmpty()) {
                throw new IllegalStateException("Median is undefined for an empty stream.");
            }

            if (lower.size() == upper.size()) {
                return ((double) lower.peek() + upper.peek()) / 2.0;
            }

            return lower.peek();
        }
    }

    /*
     * =========================================================================
     * FOLLOW-UP 1 — ALL VALUES ARE IN [0,100]
     * =========================================================================
     *
     * Tiny fixed domain -> use frequency counting.
     *
     * Median = middle rank(s) in sorted order.
     *
     *      n=5 -> ranks 3,3
     *      n=6 -> ranks 3,4
     *
     * While scanning values:
     *
     *      seen += frequency[value]
     *
     * seen = number of elements <= current value.
     *
     * The first value where:
     *
     *      seen >= targetRank
     *
     * is the value occupying that rank.
     *
     * Complexity:
     *
     *      addNum      O(1)
     *      findMedian  O(101) = O(1)
     *      space       O(101)
     */

    static final class MedianFinderCounting {

        private final int[] frequency = new int[101];
        private int size;

        public void addNum(int num) {

            if (num < 0 || num > 100) {
                throw new IllegalArgumentException("Expected a value in [0,100].");
            }

            frequency[num]++;
            size++;
        }

        public double findMedian() {

            if (size == 0) {
                throw new IllegalStateException("Median is undefined for an empty stream.");
            }

            // 1-indexed sorted middle positions:
            //
            // n=5 -> 1 2 [3] 4 5   -> 3,3
            // n=6 -> 1 2 [3 4] 5 6 -> 3,4
            int leftMedianRank = (size + 1) / 2;
            int rightMedianRank = (size + 2) / 2;

            // Prefix count = number of elements <= current value.
            int seen = 0;

            int leftMedianValue = -1;
            int rightMedianValue = -1;

            int value = 0;

            while (value <= 100) {

                seen += frequency[value];

                if (leftMedianValue == -1 && seen >= leftMedianRank) {
                    leftMedianValue = value;
                }

                if (seen >= rightMedianRank) {
                    rightMedianValue = value;
                    break;
                }

                value++;
            }

            return ((double) leftMedianValue + rightMedianValue) / 2.0;
        }
    }

    /*
     * =========================================================================
     * FOLLOW-UP 2 — 99% OF VALUES ARE IN [0,100]
     * =========================================================================
     *
     * Assume the 99% guarantee holds when findMedian() is called.
     *
     * Then the median must lie inside [0,100]:
     *
     *      <=1% of values are outside
     *      median is around the 50th percentile
     *
     * Keep:
     *
     *      belowZeroCount
     *          values before the [0,100] range
     *
     *      frequency[101]
     *          exact in-range frequencies
     *
     *      size
     *          total stream size
     *
     * Values < 0 matter because they shift the median's rank
     * inside [0,100].
     *
     * Values > 100 affect total size,
     * but do not need exact storage because they lie after
     * the guaranteed in-range median.
     *
     * Example:
     *
     *      global median rank = 50
     *      10 values are below 0
     *
     *      target inside [0,100] = 40
     *
     * Complexity:
     *
     *      addNum      O(1)
     *      findMedian  O(101) = O(1)
     *      space       O(101)
     */

    static final class MedianFinderMostlyBounded {

        private final int[] frequency = new int[101];

        private int belowZeroCount;
        private int size;

        public void addNum(int num) {

            if (num < 0) {
                belowZeroCount++;
            } else if (num <= 100) {
                frequency[num]++;
            }

            size++;
        }

        public double findMedian() {

            if (size == 0) {
                throw new IllegalStateException("Median is undefined for an empty stream.");
            }

            int leftMedianRank = (size + 1) / 2;
            int rightMedianRank = (size + 2) / 2;

            int leftTarget = leftMedianRank - belowZeroCount;
            int rightTarget = rightMedianRank - belowZeroCount;

            int seen = 0;

            int leftMedianValue = -1;
            int rightMedianValue = -1;

            int value = 0;

            while (value <= 100) {

                seen += frequency[value];

                if (leftMedianValue == -1 && seen >= leftTarget) {
                    leftMedianValue = value;
                }

                if (seen >= rightTarget) {
                    rightMedianValue = value;
                    break;
                }

                value++;
            }

            if (leftMedianValue == -1 || rightMedianValue == -1) {
                throw new IllegalStateException(
                        "99% in [0,100] guarantee is not satisfied."
                );
            }

            return ((double) leftMedianValue + rightMedianValue) / 2.0;
        }
    }

    /*
     * =========================================================================
     * FOLLOW-UP 3 — WHAT IF OLD VALUES MUST LEAVE?
     * =========================================================================
     *
     * This becomes:
     *
     *      Sliding Window Median
     *
     * Original:
     *
     *      insert only
     *
     * New requirement:
     *
     *      insert incoming value
     *      remove outgoing value
     *
     * What breaks?
     *
     *      Java PriorityQueue cannot efficiently remove
     *      an arbitrary buried value.
     *
     * Typical optimal solution:
     *
     *      two heaps
     *      +
     *      lazy deletion
     *      +
     *      logical heap sizes
     *
     * Complexity:
     *
     *      O(n log k)
     *
     * This is a substantial new problem,
     * so its full implementation lives in:
     *
     *      SlidingWindowMedian.java
     *
     * Recall:
     *
     *      STREAM MEDIAN
     *          -> two heaps
     *
     *      SLIDING MEDIAN
     *          -> two heaps + deletion problem
     *          -> lazy deletion
     */

    /*
     * =========================================================================
     * WHY?
     * =========================================================================
     *
     * WHY TWO HEAPS?
     *
     * Median needs only:
     *
     *      largest value in lower half
     *      smallest value in upper half
     *
     * A MaxHeap and MinHeap expose exactly those boundaries.
     *
     * ------------------------------------------------------------
     *
     * WHY IS LOWER NEVER SMALLER?
     *
     * Convention:
     *
     *      lower.size() == upper.size()
     *      OR
     *      lower.size() == upper.size() + 1
     *
     * So lower owns the odd extra middle element.
     *
     * ------------------------------------------------------------
     *
     * WHY DO REBALANCE MOVES PRESERVE ORDER?
     *
     * lower too large -> move lower maximum
     * upper too large -> move upper minimum
     *
     * Those are the values closest to the partition boundary.
     *
     * ------------------------------------------------------------
     *
     * WHY IS ONE MOVE ENOUGH?
     *
     * Before insertion size difference is at most one.
     * One insertion changes only one heap size by one.
     *
     * ------------------------------------------------------------
     *
     * WHY CAST BEFORE ADDITION?
     *
     *      ((double) lower.peek() + upper.peek()) / 2.0
     *
     * avoids integer overflow before division.
     */

    /*
     * =========================================================================
     * 30-SECOND RECALL
     * =========================================================================
     *
     * Running median
     *      -> lower MaxHeap + upper MinHeap
     *
     * ORDER
     *      lower <= upper
     *
     * SIZE
     *      lower == upper
     *      OR
     *      lower == upper + 1
     *
     * INSERT
     *      num <= lower.peek() ? lower : upper
     *
     * REBALANCE
     *      lower too big -> move max
     *      upper bigger  -> move min
     *
     * MEDIAN
     *      odd  -> lower.peek()
     *      even -> average of roots
     *
     * FOLLOW-UPS
     *      [0,100]     -> frequency array
     *      99% bounded -> frequency + below-range count
     */

    /*
     * =========================================================================
     * INTERVIEW ARTICULATION
     * =========================================================================
     *
     * "I maintain two heaps around the median boundary. The max-heap stores
     * the lower half and the min-heap stores the upper half. Every lower value
     * is <= every upper value, and lower has either equal size or one extra.
     * I insert on the correct side and rebalance one boundary element if
     * needed. Odd median is lower.peek(); even median is the average of both
     * roots. Insert is O(log n), median lookup is O(1)."
     */

    /*
     * =========================================================================
     * RELATED / REINFORCEMENT
     * =========================================================================
     *
     * Sliding Window Median
     *      direct Follow-up 3 above
     *      -> full implementation in SlidingWindowMedian.java
     *
     * IPO
     *      two heaps for eligibility + greedy selection
     *      -> separate IPO.java
     *
     * Kth Largest in Stream
     *      MinHeap of size k
     *
     * Kth Largest in Array
     *      heap or QuickSelect
     */

    public static void main(String[] args) {

        MedianFinder finder = new MedianFinder();

        finder.addNum(1);
        assert finder.findMedian() == 1.0;

        finder.addNum(2);
        assert Math.abs(finder.findMedian() - 1.5) < 1e-9;

        finder.addNum(3);
        assert finder.findMedian() == 2.0;

        MedianFinder extremes = new MedianFinder();

        extremes.addNum(Integer.MIN_VALUE);
        extremes.addNum(Integer.MAX_VALUE);

        assert Math.abs(extremes.findMedian() - (-0.5)) < 1e-9;

        MedianFinderCounting counting = new MedianFinderCounting();

        counting.addNum(2);
        counting.addNum(3);
        counting.addNum(4);

        assert counting.findMedian() == 3.0;

        MedianFinderCounting countingEven = new MedianFinderCounting();

        countingEven.addNum(2);
        countingEven.addNum(3);

        assert Math.abs(countingEven.findMedian() - 2.5) < 1e-9;

        MedianFinderMostlyBounded mostlyBounded =
                new MedianFinderMostlyBounded();

        int value = 0;

        while (value < 99) {
            mostlyBounded.addNum(50);
            value++;
        }

        mostlyBounded.addNum(-1_000_000);

        assert mostlyBounded.findMedian() == 50.0;

        MedianFinderMostlyBounded highOutlier =
                new MedianFinderMostlyBounded();

        value = 0;

        while (value < 99) {
            highOutlier.addNum(40);
            value++;
        }

        highOutlier.addNum(1_000_000);

        assert highOutlier.findMedian() == 40.0;

        System.out.println("Median: all assertions passed.");
    }
}
