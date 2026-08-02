package org.chijai.day7.session1.heap;

import java.util.LinkedList;

public class MovingAverage {

    /*
     * ============================================================
     * 2. 📘 PRIMARY PROBLEM
     * ============================================================
     *
     * Title:
     * Moving Average From Data Stream
     *
     * Difficulty:
     * Easy
     *
     * Tags:
     * Queue
     * Sliding Window
     * Data Stream
     * Design
     *
     * Problem Description:
     *
     * Design a data structure that receives an infinite stream of integers.
     *
     * After every newly arriving integer, return the average of the last
     * 'size' integers.
     *
     * If fewer than 'size' elements have been received so far,
     * compute the average of all received elements.
     *
     * Operations:
     *
     * MovingAverage(int size)
     *      Initializes the moving average window.
     *
     * double next(int val)
     *      Inserts a new value into the stream and returns the
     *      current moving average.
     *
     * Constraints:
     *
     * 1 <= size <= 1000
     * Integer values may be negative.
     * next() may be called indefinitely.
     *
     * Representative Example:
     *
     * size = 3
     *
     * next(1)  -> 1.0
     * window = [1]
     *
     * next(10) -> 5.5
     * window = [1,10]
     *
     * next(3)  -> 4.6666666667
     * window = [1,10,3]
     *
     * next(5)  -> 6.0
     * window = [10,3,5]
     *
     * LeetCode:
     * https://leetcode.com/problems/moving-average-from-data-stream/
     */

    /*
     * ============================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern:
     * Fixed-Size Sliding Window
     *
     * Archetype:
     * Streaming Window Aggregation
     *
     * Core Invariant:
     *
     * The queue always contains exactly the elements that belong to
     * the current sliding window.
     *
     * The running sum always equals the sum of every element inside
     * that queue.
     *
     * Therefore:
     *
     * average = sum / queueSize
     *
     * before the window becomes full,
     *
     * or
     *
     * average = sum / windowSize
     *
     * after it becomes full.
     *
     * Why it works:
     *
     * Every element enters exactly once.
     *
     * Every expired element leaves exactly once.
     *
     * Since every insertion is immediately matched by at most one
     * removal, the running sum never needs recomputation.
     *
     * Recognition Signals:
     *
     * - continuous stream
     * - fixed window
     * - latest k elements
     * - online computation
     * - repeated averages
     * - repeated sums
     *
     * Use when:
     *
     * - window length never changes
     * - aggregation supports incremental updates
     * - streaming input
     *
     * Do NOT use when:
     *
     * - arbitrary deletions occur
     * - window boundaries move irregularly
     * - aggregation cannot be updated incrementally
     *   (for example median)
     *
     * Comparison:
     *
     * Prefix Sum
     * ----------
     * Offline.
     * Requires entire array.
     *
     * Sliding Window
     * --------------
     * Online.
     * Constant update.
     *
     * Monotonic Queue
     * ----------------
     * Tracks extrema.
     * Not averages.
     */

    /*
     * ============================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * Mental Model:
     *
     * Imagine a conveyor belt with room for exactly K boxes.
     *
     * Every arriving number is pushed onto the belt.
     *
     * If the belt overflows,
     * the oldest box falls off immediately.
     *
     * The running sum is simply the total weight currently on
     * the conveyor.
     *
     * ------------------------------------------------------------
     * Invariant 1
     * ------------------------------------------------------------
     *
     * queue contains every element currently inside the window.
     *
     * Nothing older.
     *
     * Nothing newer.
     *
     * ------------------------------------------------------------
     * Invariant 2
     * ------------------------------------------------------------
     *
     * sum equals
     *
     * Σ(queue)
     *
     * at every observable program state.
     *
     * ------------------------------------------------------------
     * Invariant 3
     * ------------------------------------------------------------
     *
     * queue size never exceeds window size after next() returns.
     *
     * ------------------------------------------------------------
     * Variable Meaning
     * ------------------------------------------------------------
     *
     * size
     * Fixed window capacity.
     *
     * queue
     * Current window.
     *
     * sum
     * Sum of queue contents.
     *
     * ------------------------------------------------------------
     * Allowed State Transition
     * ------------------------------------------------------------
     *
     * Add new value.
     *
     * Update sum.
     *
     * If overflow:
     *
     * remove oldest
     *
     * subtract oldest
     *
     * return average.
     *
     * ------------------------------------------------------------
     * Forbidden Transition
     * ------------------------------------------------------------
     *
     * Removing before inserting.
     *
     * Forgetting to subtract removed value.
     *
     * Updating queue without updating sum.
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * next() performs one insertion,
     * at most one removal,
     * then returns.
     *
     * Constant work.
     *
     * ------------------------------------------------------------
     * Why Naive Solutions Fail
     * ------------------------------------------------------------
     *
     * Recomputing the entire window every call costs
     *
     * O(windowSize)
     *
     * per insertion.
     *
     * Since only one element changes between consecutive windows,
     * almost all computation is repeated unnecessarily.
     */

    /*
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * Mistake:
     * Forget subtracting expired value.
     *
     * Appears Correct:
     * Sum keeps increasing.
     *
     * Violated Invariant:
     * sum != Σ(window)
     *
     * ------------------------------------------------------------
     *
     * Mistake:
     * Divide by fixed size before window fills.
     *
     * Counterexample:
     *
     * size = 5
     *
     * first value = 20
     *
     * correct average = 20
     *
     * incorrect = 4
     *
     * ------------------------------------------------------------
     *
     * Mistake:
     * Remove newest instead of oldest.
     *
     * Violated Invariant:
     * Queue is no longer the current window.
     *
     * ------------------------------------------------------------
     *
     * Interview Trap:
     *
     * Candidate explains queue,
     * but recomputes sum every time.
     *
     * Queue alone is insufficient.
     *
     * Running sum is the second invariant.
     */

    /*
     * ============================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Typing Order
     *
     * 1. Declare queue.
     * 2. Declare running sum.
     * 3. Store window size.
     * 4. Constructor.
     * 5. next(val)
     *      add value
     *      update sum
     *      overflow?
     *          remove oldest
     *          subtract oldest
     *      divide using current denominator
     *      return
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * insert
     * update sum
     * overflow -> remove oldest
     * subtract oldest
     * return average
     */

    /*
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    /*
     * ------------------------------------------------------------
     * Brute Force
     * ------------------------------------------------------------
     *
     * Idea:
     * Store window.
     * Recompute sum every call.
     *
     * Invariant:
     * Queue is correct.
     *
     * Limitation:
     * Sum repeatedly recomputed.
     *
     * Complexity:
     * Time  : O(k)
     * Space : O(k)
     *
     * Interview Usefulness:
     * Good starting discussion.
     */

    static class BruteForceMovingAverage {

        private final int size;
        private final LinkedList<Integer> queue = new LinkedList<>();

        BruteForceMovingAverage(int size) {
            this.size = size;
        }

        double next(int value) {

            queue.offer(value);

            if (queue.size() > size) {
                queue.poll();
            }

            long total = 0;

            for (int number : queue) {
                total += number;
            }

            return (double) total / queue.size();
        }
    }

    /*
     * ------------------------------------------------------------
     * Improved
     * ------------------------------------------------------------
     *
     * Idea:
     * Maintain running sum.
     *
     * Invariant:
     * Running sum equals queue sum.
     *
     * Improvement:
     * No repeated traversal.
     *
     * Complexity:
     * Time  : O(1)
     * Space : O(k)
     *
     * Interview Usefulness:
     * Usually sufficient.
     */

    static class ImprovedMovingAverage {

        protected final int size;
        protected final LinkedList<Integer> queue = new LinkedList<>();
        protected double sum;

        ImprovedMovingAverage(int size) {
            this.size = size;
        }

        double next(int value) {

            sum += value;

            queue.offer(value);

            if (queue.size() > size) {
                sum -= queue.poll();
            }

            return sum / queue.size();
        }
    }

    /*
     * ------------------------------------------------------------
     * Optimal (Interview Preferred)
     * ------------------------------------------------------------
     *
     * Idea:
     *
     * Maintain exactly one queue and one running sum.
     *
     * Invariant:
     *
     * queue == current window
     *
     * sum == Σ(queue)
     *
     * Correctness:
     *
     * Every insertion changes the window by exactly one element.
     *
     * If overflow occurs,
     * exactly one expired element is removed.
     *
     * Therefore the invariant is preserved after every update.
     *
     * Complexity:
     *
     * Time  : O(1)
     * Space : O(k)
     *
     * Interview Usefulness:
     *
     * Canonical solution.
     */

    private final int size;
    private final LinkedList<Integer> queue;
    private double sum;

    public MovingAverage(int size) {
        this.size = size;
        this.queue = new LinkedList<>();
    }

    public double next(int value) {

        // Invariant: add newest stream element.
        queue.offer(value);

        // Invariant: sum always equals queue contents.
        sum += value;

        if (queue.size() > size) {

            // Invariant: discard exactly the expired element.
            sum -= queue.poll();
        }

        // Window not yet full uses actual size.
        return sum / queue.size();
    }

    /*
     * ============================================================
     * 🟣 INTERVIEW ARTICULATION
     * ============================================================
     *
     * Invariant:
     *
     * The queue always represents the current sliding window.
     * The running sum always equals the sum of every value inside
     * that queue.
     *
     * Discard Rule:
     *
     * After inserting the newest value, if the window exceeds its
     * capacity, remove exactly one element from the front because
     * it is the oldest value and can never contribute to any future
     * window.
     *
     * Correctness:
     *
     * Every stream element is added exactly once and removed exactly
     * once. Therefore the running sum remains synchronized with the
     * window throughout execution.
     *
     * Termination:
     *
     * Each call performs one insertion, at most one removal, and
     * returns immediately.
     *
     * In-place Feasibility:
     *
     * No. The previous values must remain available until they expire
     * from the window.
     *
     * Streaming Feasibility:
     *
     * Yes. This algorithm is specifically designed for online data
     * streams.
     *
     * When NOT to Use:
     *
     * - Variable-sized windows.
     * - Sliding median.
     * - Arbitrary deletions.
     * - Aggregations without incremental updates.
     */

    /*
     * ============================================================
     * 🎯 INTERVIEW RECALL SHEET
     * ============================================================
     *
     * Trigger:
     * Last K elements from a stream.
     *
     * Pattern:
     * Fixed-Size Sliding Window.
     *
     * Invariant:
     * queue == current window
     * sum == Σ(queue)
     *
     * Search Target:
     * Running aggregate over latest K values.
     *
     * Discard Rule:
     * Remove oldest immediately after overflow.
     *
     * Common Trap:
     * Forgetting to subtract the removed element.
     *
     * Edge Cases:
     * - First insertion.
     * - Window not yet full.
     * - Window size equals one.
     * - Negative values.
     *
     * One-Liner:
     * Add newest, remove oldest if needed, maintain running sum.
     *
     * Re-derivation Cue:
     * Only one value enters and at most one value leaves.
     */

    /*
     * ============================================================
     * 🔄 VARIATIONS & TWEAKS
     * ============================================================
     *
     * Variation:
     * Sliding Window Sum.
     *
     * Change:
     * Return sum directly.
     *
     * Invariant:
     * Unchanged.
     *
     * ------------------------------------------------------------
     *
     * Variation:
     * Sliding Window Maximum.
     *
     * Change:
     * Running sum no longer works.
     *
     * Pattern:
     * Monotonic Queue.
     *
     * ------------------------------------------------------------
     *
     * Variation:
     * Sliding Window Median.
     *
     * Pattern Break:
     * Median cannot be updated by simple subtraction/addition.
     *
     * Requires:
     * Two heaps or balanced tree.
     *
     * ------------------------------------------------------------
     *
     * Variation:
     * Variable Window.
     *
     * Pattern Change:
     * Window boundaries depend on conditions rather than capacity.
     *
     * Queue invariant changes accordingly.
     */

    /*
     * ============================================================
     * 🧠 MASTERY CHECKLIST
     * ============================================================
     *
     * ✔ Invariant identified?
     * Yes.
     * queue == window
     * sum == Σ(queue)
     *
     * ✔ Search Space?
     * Current sliding window.
     *
     * ✔ Discard Rule?
     * Remove oldest after overflow.
     *
     * ✔ Termination?
     * One insertion and at most one removal.
     *
     * ✔ Why naive fails?
     * Recomputes entire sum repeatedly.
     *
     * ✔ Edge cases?
     * Empty history, partial window, size one, negatives.
     *
     * ✔ Debugging readiness?
     * Verify queue contents and running sum after each update.
     *
     * ✔ Variant readiness?
     * Understand when incremental aggregation remains valid.
     *
     * ✔ Pattern boundary?
     * Stops working for non-incremental statistics like median.
     */

    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    public static void main(String[] args) {

        // Representative example from the problem.
        MovingAverage average = new MovingAverage(3);

        assert Math.abs(average.next(1) - 1.0) < 1e-9
                : "Single element average.";

        assert Math.abs(average.next(10) - 5.5) < 1e-9
                : "Window not yet full.";

        assert Math.abs(average.next(3) - (14.0 / 3.0)) < 1e-9
                : "Exactly full window.";

        assert Math.abs(average.next(5) - 6.0) < 1e-9
                : "Oldest element discarded.";

        // Window size one behaves like latest value.
        MovingAverage one = new MovingAverage(1);

        assert Math.abs(one.next(7) - 7.0) < 1e-9
                : "Initial value.";

        assert Math.abs(one.next(9) - 9.0) < 1e-9
                : "Previous value removed immediately.";

        // Negative values.
        MovingAverage negative = new MovingAverage(2);

        assert Math.abs(negative.next(-2) - (-2.0)) < 1e-9
                : "Negative single value.";

        assert Math.abs(negative.next(2) - 0.0) < 1e-9
                : "Balanced negatives.";

        assert Math.abs(negative.next(4) - 3.0) < 1e-9
                : "Window correctly advances.";

        // Large overflow sequence.
        MovingAverage overflow = new MovingAverage(3);

        overflow.next(1);
        overflow.next(2);
        overflow.next(3);

        assert Math.abs(overflow.next(4) - 3.0) < 1e-9
                : "Window should contain 2,3,4.";

        assert Math.abs(overflow.next(5) - 4.0) < 1e-9
                : "Window should contain 3,4,5.";

        // Compare against brute force implementation.
        BruteForceMovingAverage brute = new BruteForceMovingAverage(4);
        ImprovedMovingAverage improved = new ImprovedMovingAverage(4);
        MovingAverage optimal = new MovingAverage(4);

        int[] stream = {5, -1, 7, 2, 8, 9, -4, 6};

        for (int value : stream) {

            double b = brute.next(value);
            double i = improved.next(value);
            double o = optimal.next(value);

            assert Math.abs(b - i) < 1e-9
                    : "Improved must match brute force.";

            assert Math.abs(i - o) < 1e-9
                    : "Optimal must match improved.";
        }

        System.out.println("All assertions passed.");
    }

}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/