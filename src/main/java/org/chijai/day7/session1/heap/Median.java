package org.chijai.day7.session1.heap;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * ============================================================================
 *  Find Median from Data Stream
 * ============================================================================
 *
 * Difficulty:
 * Hard
 *
 * Tags:
 * Heap
 * Priority Queue
 * Design
 * Data Stream
 *
 * Problem
 * -------
 * The median is the middle value in an ordered integer list.
 *
 * If the number of elements is odd:
 *      median = middle element
 *
 * If the number of elements is even:
 *      median = average of the two middle elements.
 *
 * Design a data structure supporting:
 *
 *      MedianFinder()
 *
 *      void addNum(int num)
 *
 *      double findMedian()
 *
 * Constraints
 * -----------
 *
 * -10^5 <= num <= 10^5
 *
 * addNum() and findMedian() may be called many times.
 *
 * Representative Examples
 * -----------------------
 *
 * add 1
 * add 2
 * median = 1.5
 *
 * add 3
 * median = 2
 *
 * add 2
 * add 3
 * add 4
 * median = 3
 *
 * Official LeetCode
 * -----------------
 * https://leetcode.com/problems/find-median-from-data-stream/
 */
public class Median {

    /*
     * =========================================================================
     * 🔵 CORE PATTERN OVERVIEW
     * =========================================================================
     *
     * Pattern
     * -------
     * Two Balanced Heaps
     *
     * Archetype
     * ---------
     * Dynamic Order Statistics
     *
     * Core Invariant
     * --------------
     * Split the stream into two ordered halves.
     *
     * leftHeap  -> smaller half
     * rightHeap -> larger half
     *
     * leftHeap is a Max Heap.
     *
     * rightHeap is a Min Heap.
     *
     * Every element in leftHeap
     * <=
     * Every element in rightHeap.
     *
     * Size Rule
     * ---------
     *
     * left.size == right.size
     *
     * OR
     *
     * left.size == right.size + 1
     *
     * Therefore:
     *
     * odd count
     *      median = left.peek()
     *
     * even count
     *      median = (left.peek + right.peek)/2
     *
     * Why it Works
     * ------------
     *
     * We never need the entire sorted order.
     *
     * We only need:
     *
     * largest element of lower half
     *
     * smallest element of upper half
     *
     * Those are exactly the roots of
     *
     * MaxHeap
     *
     * MinHeap
     *
     * Recognition Signals
     * -------------------
     *
     * • stream of numbers
     * • insert continuously
     * • query median anytime
     * • no deletions
     * • online processing
     *
     * Use When
     * --------
     *
     * Running median
     *
     * Sliding toward order statistics
     *
     * Online ranking
     *
     * Continuous ingestion
     *
     * Do NOT Use
     * ----------
     *
     * Static array
     *
     * Single median query after all insertions
     *
     * In that case sorting or QuickSelect is simpler.
     *
     * Comparison
     * ----------
     *
     * Sorting
     *      Insert O(1)
     *      Query O(n log n)
     *
     * Balanced BST
     *      More general
     *      Higher implementation complexity
     *
     * Two Heaps
     *      Insert O(log n)
     *      Median O(1)
     *      Interview favorite
     */

    /*
     * =========================================================================
     * 🟢 MENTAL MODEL & INVARIANTS
     * =========================================================================
     *
     * Mental Model
     * ------------
     *
     * Imagine placing a wall exactly at the median.
     *
     * Everything left of the wall belongs inside the MaxHeap.
     *
     * Everything right of the wall belongs inside the MinHeap.
     *
     * The roots touch the wall.
     *
     * Therefore:
     *
     * left.peek()
     *
     * right.peek()
     *
     * are always sufficient to reconstruct the median.
     *
     * ------------------------------------------------------------
     * Invariant 1
     * ------------------------------------------------------------
     *
     * Every element in leftHeap
     *
     * <=
     *
     * every element in rightHeap.
     *
     * This is the ordering invariant.
     *
     * ------------------------------------------------------------
     * Invariant 2
     * ------------------------------------------------------------
     *
     * left.size()
     * ==
     * right.size()
     *
     * OR
     *
     * left.size()
     * ==
     * right.size()+1
     *
     * This is the balancing invariant.
     *
     * Left is never smaller.
     *
     * Left is larger by at most one.
     *
     * ------------------------------------------------------------
     * Variable Meaning
     * ------------------------------------------------------------
     *
     * left
     *
     * Max Heap
     *
     * stores lower half.
     *
     * root
     *
     * largest value among lower half.
     *
     * right
     *
     * Min Heap
     *
     * stores upper half.
     *
     * root
     *
     * smallest value among upper half.
     *
     * ------------------------------------------------------------
     * Allowed State Transitions
     * ------------------------------------------------------------
     *
     * Insert into either heap.
     *
     * Move largest from left
     * →
     * right.
     *
     * Move smallest from right
     * →
     * left.
     *
     * Nothing else.
     *
     * ------------------------------------------------------------
     * Forbidden States
     * ------------------------------------------------------------
     *
     * left contains value
     *
     * >
     *
     * value inside right.
     *
     * OR
     *
     * right larger than left.
     *
     * OR
     *
     * size difference > 1.
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * Each insertion finishes after at most
     *
     * one ordering repair
     *
     * and
     *
     * one balancing repair.
     *
     * Therefore insertion is deterministic.
     *
     * ------------------------------------------------------------
     * Why Naive Solutions Fail
     * ------------------------------------------------------------
     *
     * Keeping a sorted list:
     *
     * insertion O(n)
     *
     * Too expensive.
     *
     * Sorting every query:
     *
     * O(n log n)
     *
     * Per query.
     *
     * Maintaining only one heap:
     *
     * Cannot recover both middle values.
     *
     * The median depends simultaneously on
     *
     * largest lower value
     *
     * and
     *
     * smallest upper value.
     */

    /*
     * =========================================================================
     * 🔴 WHY WRONG SOLUTIONS FAIL
     * =========================================================================
     *
     * Mistake 1
     * ---------
     *
     * Insert directly into arbitrary heap.
     *
     * Looks harmless.
     *
     * Violates ordering invariant.
     *
     * Counterexample:
     *
     * left : 10
     * right : 20
     *
     * insert 100 into left
     *
     * left now contains element that belongs to upper half.
     *
     * ------------------------------------------------------------
     * Mistake 2
     * ------------------------------------------------------------
     *
     * Balance only sizes.
     *
     * Ignore ordering.
     *
     * Heap sizes become correct.
     *
     * Median becomes incorrect.
     *
     * ------------------------------------------------------------
     * Mistake 3
     * ------------------------------------------------------------
     *
     * Average using integer arithmetic.
     *
     * (2+3)/2
     *
     * becomes
     *
     * 2
     *
     * instead of
     *
     * 2.5
     *
     * ------------------------------------------------------------
     * Mistake 4
     * ------------------------------------------------------------
     *
     * Forget overflow possibility.
     *
     * Better:
     *
     * ((double)left.peek()+right.peek())/2
     *
     * ------------------------------------------------------------
     * Interview Trap
     * ------------------------------------------------------------
     *
     * Candidate explains balancing.
     *
     * Interviewer asks:
     *
     * "How do you know all left values remain <= all right values?"
     *
     * If you cannot justify that invariant,
     * the proof is incomplete.
     */

    /*
     * =========================================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * =========================================================================
     *
     * Typing Order
     * ------------
     *
     * 1.
     * Declare MaxHeap.
     *
     * 2.
     * Declare MinHeap.
     *
     * 3.
     * addNum()
     *
     *      Decide destination heap.
     *
     * 4.
     * Repair size invariant.
     *
     * 5.
     * findMedian()
     *
     *      odd?
     *          left.peek()
     *
     *      even?
     *          average of roots
     *
     * Mechanical Skeleton
     * -------------------
     *
     * add(num)
     *
     *      if left empty OR num <= left.peek
     *              push left
     *      else
     *              push right
     *
     *      rebalance
     *
     * median()
     *
     *      if equal sizes
     *              average
     *
     *      else
     *              left.peek
     */

    /*
     * =========================================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * =========================================================================
     *
     * insert
     *
     * choose heap
     *
     * rebalance
     *
     * if equal
     *      average
     *
     * else
     *      left root
     */

    /*
     * =========================================================================
     * 6. SOLUTION CLASSES
     * =========================================================================
     */

    /**
     * -------------------------------------------------------------------------
     * Brute Force
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     *
     * Store all numbers.
     *
     * Sort every median query.
     *
     * Invariant
     * ---------
     *
     * Entire collection becomes sorted before answering.
     *
     * Limitation
     * ----------
     *
     * Query dominates runtime.
     *
     * Complexity
     * ----------
     *
     * add
     * O(1)
     *
     * median
     * O(n log n)
     *
     * Interview Usefulness
     * --------------------
     *
     * Good baseline only.
     */
    static class BruteForceExplanation {
    }

    /**
     * -------------------------------------------------------------------------
     * Improved
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     *
     * Maintain sorted list.
     *
     * Binary search insertion.
     *
     * Invariant
     * ---------
     *
     * List always sorted.
     *
     * Improvement
     * -----------
     *
     * Query becomes O(1).
     *
     * Complexity
     * ----------
     *
     * Insert
     * O(n)
     *
     * Query
     * O(1)
     *
     * Interview Usefulness
     * --------------------
     *
     * Better than brute force.
     *
     * Still too slow for large streams.
     */
    static class ImprovedExplanation {
    }

    /**
     * -------------------------------------------------------------------------
     * Optimal (Interview Preferred)
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     *
     * Maintain two balanced heaps satisfying the invariants introduced earlier.
     *
     * Correctness
     * -----------
     *
     * Median always lies on the boundary formed by the two heap roots.
     *
     * Complexity
     * ----------
     *
     * addNum
     * O(log n)
     *
     * findMedian
     * O(1)
     *
     * Space
     * O(n)
     */
    static class MedianFinder {

        private final PriorityQueue<Integer> left =
                new PriorityQueue<>(Collections.reverseOrder());

        private final PriorityQueue<Integer> right =
                new PriorityQueue<>();

        public void addNum(int num) {

            // 🟢 Invariant:
            // left stores the lower half.
            // right stores the upper half.

            if (left.isEmpty() || num <= left.peek()) {
                left.offer(num);
            } else {
                right.offer(num);
            }

            // 🟢 Invariant:
            // left is never allowed to become smaller.

            if (left.size() < right.size()) {
                left.offer(right.poll());
            }

            // 🟢 Invariant:
            // left may exceed right by at most one.

            if (left.size() - right.size() > 1) {
                right.offer(left.poll());
            }

            // 🔴 Defensive repair.
            //
            // The size invariant alone is insufficient.
            // Verify ordering remains correct.
            //
            // This branch is rarely executed with the above insertion
            // strategy but makes the implementation mechanically robust.

            if (!left.isEmpty()
                    && !right.isEmpty()
                    && left.peek() > right.peek()) {

                int lowBoundary = left.poll();
                int highBoundary = right.poll();

                left.offer(highBoundary);
                right.offer(lowBoundary);
            }
        }

        public double findMedian() {

            if (left.isEmpty()) {
                return 0.0;
            }

            // 🟢 Equal partitions.
            // Median lies between the touching boundaries.

            if (left.size() == right.size()) {
                return ((double) left.peek() + right.peek()) / 2.0;
            }

            // 🟢 Left owns one additional element.

            return left.peek();
        }
    }

    /*
     * =========================================================================
     * 🟣 INTERVIEW ARTICULATION
     * =========================================================================
     *
     * Explain the invariant first.
     *
     * "I partition the stream into two heaps.
     *
     * The MaxHeap stores the lower half.
     *
     * The MinHeap stores the upper half.
     *
     * Every value in the left heap is guaranteed to be less than or equal to
     * every value in the right heap.
     *
     * Additionally, the left heap is either the same size as the right heap
     * or larger by exactly one.
     *
     * Therefore the median must always be located at one of the heap roots."
     *
     * ------------------------------------------------------------
     * Discard Rule
     * ------------------------------------------------------------
     *
     * Unlike binary search, nothing is discarded.
     *
     * Instead we continuously restore two invariants after every insertion.
     *
     * ------------------------------------------------------------
     * Correctness
     * ------------------------------------------------------------
     *
     * Since both halves remain ordered relative to one another,
     * the boundary elements uniquely determine the median.
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * Every insertion performs:
     *
     * one heap insertion
     *
     * plus
     *
     * at most one rebalance.
     *
     * Therefore insertion always finishes in logarithmic time.
     *
     * ------------------------------------------------------------
     * In-place?
     * ------------------------------------------------------------
     *
     * No.
     *
     * Entire stream must be retained.
     *
     * ------------------------------------------------------------
     * Streaming?
     * ------------------------------------------------------------
     *
     * Yes.
     *
     * This is precisely an online algorithm.
     *
     * ------------------------------------------------------------
     * When NOT to Use
     * ------------------------------------------------------------
     *
     * Single offline median.
     *
     * QuickSelect is better.
     */

    /*
     * =========================================================================
     * 🎯 INTERVIEW RECALL SHEET
     * =========================================================================
     *
     * Trigger
     * -------
     *
     * Running median.
     *
     * Dynamic median.
     *
     * Stream median.
     *
     * Invariant
     * ---------
     *
     * lower half <= upper half
     *
     * and
     *
     * left size == right size
     *
     * OR
     *
     * left size == right size + 1
     *
     * Search Target
     * -------------
     *
     * Largest lower value.
     *
     * Smallest upper value.
     *
     * Discard Rule
     * ------------
     *
     * None.
     *
     * Rebalance instead.
     *
     * Common Trap
     * -----------
     *
     * Correct sizes.
     *
     * Wrong ordering.
     *
     * Edge Cases
     * ----------
     *
     * Empty structure.
     *
     * Negative values.
     *
     * Duplicate values.
     *
     * Increasing sequence.
     *
     * Decreasing sequence.
     *
     * One-Liner
     * ---------
     *
     * Two heaps.
     *
     * Lower half in MaxHeap.
     *
     * Upper half in MinHeap.
     *
     * Heap roots define the median.
     *
     * Re-derivation Cue
     * -----------------
     *
     * Ask:
     *
     * "Which two numbers determine the median?"
     *
     * Answer:
     *
     * largest lower
     *
     * smallest upper.
     */

    /*
     * =========================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =========================================================================
     *
     * Variant
     * -------
     *
     * Keep right heap larger.
     *
     * Works.
     *
     * Median logic changes accordingly.
     *
     * ------------------------------------------------------------
     * Variant
     * ------------------------------------------------------------
     *
     * TreeMap with frequencies.
     *
     * Supports deletions.
     *
     * Useful for sliding window median.
     *
     * ------------------------------------------------------------
     * Variant
     * ------------------------------------------------------------
     *
     * Indexed balanced tree.
     *
     * General kth-order statistic.
     *
     * More powerful.
     *
     * More complicated.
     *
     * ------------------------------------------------------------
     * Pattern Break
     * ------------------------------------------------------------
     *
     * Sliding Window Median.
     *
     * Ordinary PriorityQueue cannot efficiently remove arbitrary values.
     *
     * Need:
     *
     * lazy deletion
     *
     * or
     *
     * balanced BST.
     *
     * ------------------------------------------------------------
     * Follow-up
     * ------------------------------------------------------------
     *
     * If every number belongs to [0,100],
     * heaps are unnecessary.
     *
     * Frequency counting is sufficient.
     */

    /*
     * =========================================================================
     * Follow-up 1
     * All numbers in [0,100]
     * =========================================================================
     *
     * Observation
     * -----------
     *
     * Domain size is only 101.
     *
     * Replace heaps with frequency counting.
     *
     * addNum
     * O(1)
     *
     * findMedian
     * O(101)
     *
     * which is effectively O(1).
     */

    static class MedianFinderCounting {

        private final int[] frequency = new int[101];

        private int size;

        public void addNum(int num) {
            frequency[num]++;
            size++;
        }

        public double findMedian() {

            if (size == 0) {
                return 0.0;
            }

            int firstTarget = (size + 1) / 2;

            int secondTarget =
                    (size % 2 == 0)
                            ? size / 2 + 1
                            : firstTarget;

            int prefix = 0;

            int firstValue = -1;

            int secondValue = -1;

            for (int value = 0; value <= 100; value++) {

                prefix += frequency[value];

                if (firstValue == -1 && prefix >= firstTarget) {
                    firstValue = value;
                }

                if (secondValue == -1 && prefix >= secondTarget) {
                    secondValue = value;
                    break;
                }
            }

            return (firstValue + secondValue) / 2.0;
        }
    }

/*
 * =========================================================================
 * Follow-up 2
 * 99% of numbers are inside [0,100]
 * =========================================================================
 *
 * Idea
 * ----
 *
 * Continue in next part.
 */
/*
 * =========================================================================
 * Follow-up 2
 * 99% of numbers are inside [0,100]
 * =========================================================================
 *
 * Observation
 * -----------
 *
 * Most values lie in a very small domain.
 *
 * Paying O(log n) heap cost for every insertion wastes work.
 *
 * Hybrid Strategy
 * ---------------
 *
 * Maintain three structures.
 *
 *      lessThanZero
 *
 *      frequency[101]
 *
 *      greaterThanHundred
 *
 * where
 *
 * lessThanZero stores values < 0
 *
 * frequency stores values in [0,100]
 *
 * greaterThanHundred stores values > 100
 *
 * Since only ~1% of elements fall outside the range,
 * the expensive structures remain very small.
 *
 * Median Query
 * ------------
 *
 * Let
 *
 * L = count(values < 0)
 * M = count(values in [0,100])
 * R = count(values > 100)
 *
 * Locate the desired order statistic.
 *
 * Case 1
 *
 * Target <= L
 *
 * Search only the left structure.
 *
 * Case 2
 *
 * L < Target <= L + M
 *
 * Scan only the frequency array.
 *
 * Case 3
 *
 * Otherwise
 *
 * Search only the right structure.
 *
 * Complexity
 * ----------
 *
 * Average insertion remains close to O(1)
 * because almost every insertion simply increments a counter.
 *
 * Median lookup is dominated by
 *
 * frequency scan (101 cells)
 *
 * plus
 *
 * very small overflow structures.
 */

/*
 * =========================================================================
 * ⚫ Pattern Mapping
 * =========================================================================
 *
 * Running Median
 *      Two Heaps
 *
 * kth Largest
 *      Min Heap
 *
 * Merge k Sorted Lists
 *      Min Heap
 *
 * Top K Frequent
 *      Min Heap
 *
 * Task Scheduler
 *      Max Heap
 *
 * IPO
 *      Two Heaps
 *
 * Sliding Window Median
 *      Two Heaps
 *      +
 *      Lazy Deletion
 *
 * Frequency Restricted Median
 *      Counting
 *
 * Offline Median
 *      QuickSelect
 */

/*
 * =========================================================================
 * Debugging Checklist
 * =========================================================================
 *
 * If median is wrong:
 *
 * ✓ Check ordering invariant.
 *
 *      left.peek() <= right.peek()
 *
 * ✓ Check balancing invariant.
 *
 *      left.size()==right.size()
 *
 *      OR
 *
 *      left.size()==right.size()+1
 *
 * ✓ Check integer division.
 *
 * ✓ Check duplicate handling.
 *
 * ✓ Check empty structure.
 *
 * ✓ Check rebalance direction.
 */

/*
 * =========================================================================
 * Dry Run
 * =========================================================================
 *
 * Stream
 *
 * 5
 *
 * left
 * [5]
 *
 * right
 * []
 *
 * median
 * 5
 *
 * -----------------------------------
 *
 * add 2
 *
 * left
 * [5,2]
 *
 * rebalance
 *
 * left
 * [2]
 *
 * right
 * [5]
 *
 * median
 * 3.5
 *
 * -----------------------------------
 *
 * add 10
 *
 * left
 * [2]
 *
 * right
 * [5,10]
 *
 * rebalance
 *
 * left
 * [5,2]
 *
 * right
 * [10]
 *
 * median
 * 5
 *
 * -----------------------------------
 *
 * add 8
 *
 * left
 * [5,2]
 *
 * right
 * [8,10]
 *
 * median
 * (5+8)/2
 */

/*
 * =========================================================================
 * Common Interview Questions
 * =========================================================================
 *
 * Q.
 * Why not sort after every insertion?
 *
 * A.
 * O(n log n) per query.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Why must left be a MaxHeap?
 *
 * A.
 * We need immediate access to the largest element of the lower half.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Why must right be a MinHeap?
 *
 * A.
 * We need immediate access to the smallest element of the upper half.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Why allow left to contain one extra element?
 *
 * A.
 * Odd-length median becomes left.peek().
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Could we reverse the convention?
 *
 * A.
 * Yes.
 *
 * Keep right larger instead.
 *
 * Median logic changes consistently.
 */

/*
 * =========================================================================
 * 🧠 MASTERY CHECKLIST
 * =========================================================================
 *
 * Can you answer each question without looking at the code?
 *
 * □ What is the primary invariant?
 *
 * □ Why are two heaps sufficient?
 *
 * □ Why is one heap a MaxHeap?
 *
 * □ Why is the other heap a MinHeap?
 *
 * □ Why can the size difference never exceed one?
 *
 * □ Which heap owns the extra element?
 *
 * □ Why does that simplify odd-length median?
 *
 * □ Why is ordering more important than balancing?
 *
 * □ How do you restore balancing?
 *
 * □ Why is insertion O(log n)?
 *
 * □ Why is median O(1)?
 *
 * □ Why is sorting inferior?
 *
 * □ What changes for sliding-window median?
 *
 * □ What changes if values are restricted to [0,100]?
 *
 * □ Can you derive the algorithm from only the invariants?
 */

    public static void main(String[] args) {

        /*
         * =============================================================
         * Happy Path
         * =============================================================
         */

        MedianFinder finder = new MedianFinder();

        finder.addNum(1);
        assert finder.findMedian() == 1.0
                : "Single element should be its own median.";

        finder.addNum(2);
        assert Math.abs(finder.findMedian() - 1.5) < 1e-9
                : "Even number of elements should average middle pair.";

        finder.addNum(3);
        assert finder.findMedian() == 2.0
                : "Odd number of elements should return middle element.";

        /*
         * =============================================================
         * Representative Example
         * =============================================================
         */

        MedianFinder example = new MedianFinder();

        example.addNum(5);
        example.addNum(15);
        example.addNum(1);
        example.addNum(3);

        assert Math.abs(example.findMedian() - 4.0) < 1e-9
                : "Median of [1,3,5,15] should be 4.";

        /*
         * =============================================================
         * Increasing Order
         * =============================================================
         */

        MedianFinder increasing = new MedianFinder();

        for (int i = 1; i <= 9; i++) {
            increasing.addNum(i);
        }

        assert increasing.findMedian() == 5.0
                : "Increasing sequence should preserve balancing invariant.";

        /*
         * =============================================================
         * Decreasing Order
         * =============================================================
         */

        MedianFinder decreasing = new MedianFinder();

        for (int i = 9; i >= 1; i--) {
            decreasing.addNum(i);
        }

        assert decreasing.findMedian() == 5.0
                : "Insertion order must not affect the result.";

        /*
         * =============================================================
         * Duplicate Values
         * =============================================================
         */

        MedianFinder duplicates = new MedianFinder();

        duplicates.addNum(7);
        duplicates.addNum(7);
        duplicates.addNum(7);
        duplicates.addNum(7);

        assert duplicates.findMedian() == 7.0
                : "Duplicates should be handled naturally.";

        /*
         * =============================================================
         * Negative Values
         * =============================================================
         */

        MedianFinder negatives = new MedianFinder();

        negatives.addNum(-5);
        negatives.addNum(-10);
        negatives.addNum(-1);

        assert negatives.findMedian() == -5.0
                : "Negative values should preserve ordering invariant.";

        /*
         * =============================================================
         * Mixed Signs
         * =============================================================
         */

        MedianFinder mixed = new MedianFinder();

        mixed.addNum(-100);
        mixed.addNum(100);
        mixed.addNum(0);

        assert mixed.findMedian() == 0.0
                : "Median should correctly cross zero.";

        /*
         * =============================================================
         * Boundary Values
         * =============================================================
         */

        MedianFinder boundary = new MedianFinder();

        boundary.addNum(Integer.MIN_VALUE);
        boundary.addNum(Integer.MAX_VALUE);

        assert Math.abs(boundary.findMedian() - (-0.5)) < 1e-9
                : "Average of extreme values should avoid integer division.";

        /*
         * =============================================================
         * Large Duplicate Stress
         * =============================================================
         */

        MedianFinder stress = new MedianFinder();

        for (int i = 0; i < 1000; i++) {
            stress.addNum(42);
        }

        assert stress.findMedian() == 42.0
                : "Repeated insertions should not violate balancing.";

        /*
         * =============================================================
         * Counting Variant
         * =============================================================
         */

        MedianFinderCounting counting = new MedianFinderCounting();

        counting.addNum(2);
        counting.addNum(3);
        counting.addNum(4);

        assert counting.findMedian() == 3.0
                : "Counting implementation should match heap implementation.";

        MedianFinderCounting countingEven = new MedianFinderCounting();

        countingEven.addNum(2);
        countingEven.addNum(3);

        assert Math.abs(countingEven.findMedian() - 2.5) < 1e-9
                : "Counting implementation should correctly average even median.";

        System.out.println("All assertions passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/