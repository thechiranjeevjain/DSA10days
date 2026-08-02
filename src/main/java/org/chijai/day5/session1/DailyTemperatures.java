package org.chijai.day5.session1;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class DailyTemperatures {

    /*
     * ============================================================
     * 2. 📘 PRIMARY PROBLEM
     * ============================================================
     *
     * Title:
     * Daily Temperatures
     *
     * Difficulty:
     * Medium
     *
     * Tags:
     * Monotonic Stack
     * Stack
     * Array
     * Next Greater Element
     *
     * Problem Description
     * -------------------
     * Given an integer array temperatures where temperatures[i]
     * represents the temperature on the i-th day,
     * compute an answer array such that:
     *
     * answer[i] =
     * number of days until a strictly warmer temperature appears.
     *
     * If no warmer day exists, answer[i] = 0.
     *
     * Constraints
     * -----------
     * 1 <= temperatures.length <= 100000
     * 30 <= temperatures[i] <= 100
     *
     * Representative Example 1
     * ------------------------
     * Input:
     * [73,74,75,71,69,72,76,73]
     *
     * Output:
     * [1,1,4,2,1,1,0,0]
     *
     * Explanation:
     * Day 0 waits 1 day.
     * Day 1 waits 1 day.
     * Day 2 waits until day 6.
     * Day 3 waits until day 5.
     * Day 4 waits until day 5.
     * Day 5 waits until day 6.
     * Day 6 has no warmer future day.
     * Day 7 has no warmer future day.
     *
     * Representative Example 2
     * ------------------------
     * Input:
     * [30,40,50,60]
     *
     * Output:
     * [1,1,1,0]
     *
     * Representative Example 3
     * ------------------------
     * Input:
     * [30,60,90]
     *
     * Output:
     * [1,1,0]
     *
     * Official Problem:
     * https://leetcode.com/problems/daily-temperatures/
     */

    /*
     * ============================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern
     * -------
     * Monotonic Stack
     *
     * Archetype
     * ---------
     * Next Greater Element to the Right
     *
     * Core Invariant
     * --------------
     * The stack stores indices whose answer has not yet been found.
     *
     * Their temperatures remain in monotonically decreasing order
     * from bottom to top.
     *
     * Therefore:
     *
     * temperatures[stack[0]] >= temperatures[stack[1]]
     * >= ...
     * >= temperatures[top]
     *
     * Every index inside the stack is still searching for its
     * first strictly warmer future day.
     *
     * Why It Works
     * ------------
     * Whenever a hotter temperature appears,
     * it immediately becomes the first warmer day
     * for every smaller temperature sitting on top.
     *
     * Those indices can never obtain a better answer later,
     * because the current day is already the earliest warmer day.
     *
     * Recognition Signals
     * -------------------
     * Look for:
     *
     * - nearest greater element
     * - next warmer day
     * - first larger value on one side
     * - unresolved previous elements
     * - linear-time replacement for nested loops
     *
     * When To Use
     * -----------
     * Use when:
     *
     * - searching nearest larger/smaller element
     * - answer depends on first qualifying future element
     * - elements become permanently resolved
     * - O(n²) scan should become O(n)
     *
     * When NOT To Use
     * ---------------
     * Avoid when:
     *
     * - answers depend on every future element
     * - order does not matter
     * - random access dominates
     * - no monotonic invariant exists
     *
     * Comparison
     * ----------
     *
     * Sliding Window
     *   Maintains a contiguous interval.
     *
     * Binary Search
     *   Discards ordered search space.
     *
     * Prefix Sum
     *   Accumulates history.
     *
     * Monotonic Stack
     *   Maintains unresolved states until a future event resolves
     *   them permanently.
     */

    /*
     * ============================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * Mental Model
     * ------------
     * Imagine people standing in a queue.
     *
     * Every person is waiting to see someone taller arrive.
     *
     * As soon as someone taller appears,
     * everyone shorter immediately receives the answer
     * and leaves the queue.
     *
     * The remaining queue is still ordered from tallest
     * to shortest.
     *
     * Daily Temperatures is exactly this model.
     *
     * ------------------------------------------------------------
     * State
     * ------------------------------------------------------------
     *
     * stack
     * -----
     * Indices whose answer is still unknown.
     *
     * currDay
     * -------
     * Current index being processed.
     *
     * prevDay
     * -------
     * Newly resolved index removed from stack.
     *
     * answer[]
     * --------
     * Distance to first warmer day.
     *
     * ------------------------------------------------------------
     * Primary Invariant
     * ------------------------------------------------------------
     *
     * Every index inside the stack has not yet discovered
     * a warmer future day.
     *
     * ------------------------------------------------------------
     * Ordering Invariant
     * ------------------------------------------------------------
     *
     * Stack temperatures are monotonically decreasing.
     *
     * Example:
     *
     * Index:
     * 2 5 7
     *
     * Temp:
     * 80 76 72
     *
     * Top always contains the smallest unresolved temperature.
     *
     * ------------------------------------------------------------
     * Resolution Invariant
     * ------------------------------------------------------------
     *
     * When current temperature exceeds the stack top,
     * the current day is guaranteed to be the FIRST warmer day.
     *
     * Not merely a warmer day.
     *
     * The first warmer day.
     *
     * This is the most important correctness invariant.
     *
     * ------------------------------------------------------------
     * Why First?
     * ------------------------------------------------------------
     *
     * Suppose prevDay waits until currDay.
     *
     * If another warmer day existed earlier,
     * prevDay would already have been removed then.
     *
     * Contradiction.
     *
     * Therefore current day is necessarily the earliest warmer day.
     *
     * ------------------------------------------------------------
     * Allowed Moves
     * ------------------------------------------------------------
     *
     * 1. Pop resolved indices.
     *
     * 2. Record waiting distance.
     *
     * 3. Push current unresolved index.
     *
     * ------------------------------------------------------------
     * Forbidden Moves
     * ------------------------------------------------------------
     *
     * Never pop equal temperatures.
     *
     * The problem requires STRICTLY warmer.
     *
     * Therefore:
     *
     * current > stackTop
     *
     * not
     *
     * current >= stackTop
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * Every index is pushed exactly once.
     *
     * Every index is popped at most once.
     *
     * Remaining stack entries have no warmer future day.
     *
     * Their answers correctly remain zero.
     *
     * ------------------------------------------------------------
     * Why Naive Solutions Fail
     * ------------------------------------------------------------
     *
     * Brute force repeatedly scans future days.
     *
     * Long decreasing sequences become:
     *
     * O(n²)
     *
     * Example:
     *
     * 100
     * 99
     * 98
     * 97
     * 96
     * ...
     *
     * Every element scans almost the entire suffix.
     *
     * The monotonic stack avoids rescanning by permanently
     * removing resolved indices.
     */

    /*
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * Mistake 1
     * ---------
     * Pop using >=
     *
     * Why it seems correct:
     * Equal temperatures feel "not colder."
     *
     * Violated Invariant:
     * We require strictly warmer.
     *
     * Counterexample:
     *
     * [70,70]
     *
     * Answer:
     * [0,0]
     *
     * Not
     * [1,0]
     *
     * ------------------------------------------------------------
     * Mistake 2
     * ---------
     * Store temperatures instead of indices.
     *
     * Why it seems correct:
     * Comparison only needs temperatures.
     *
     * Violated Invariant:
     * Final answer requires day difference.
     *
     * Without indices,
     * distance cannot be computed.
     *
     * ------------------------------------------------------------
     * Mistake 3
     * ---------
     * Pop only one element.
     *
     * Counterexample:
     *
     * [60,55,50,70]
     *
     * Day 70 resolves
     * all three previous days.
     *
     * Hence while-loop,
     * not if-statement.
     *
     * ------------------------------------------------------------
     * Mistake 4
     * ---------
     * Push before resolving.
     *
     * Current day could compare against itself,
     * breaking the ordering invariant.
     *
     * ------------------------------------------------------------
     * Interview Trap
     * --------------
     * Ask yourself:
     *
     * "Why is the first warmer day guaranteed?"
     *
     * If this cannot be justified,
     * the correctness proof is incomplete.
     */

    /*
     * ============================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Mechanical Typing Order
     * -----------------------
     *
     * 1. Create answer array.
     *
     * 2. Create decreasing stack of indices.
     *
     * 3. Loop left to right.
     *
     * 4. While current temperature is warmer:
     *
     *      pop previous index
     *      compute distance
     *
     * 5. Push current index.
     *
     * 6. Return answer.
     *
     * Function Skeleton
     * -----------------
     *
     * answer[]
     * stack
     *
     * for each day
     *     resolve previous colder days
     *     push current
     *
     * return answer
     *
     * Transition
     * ----------
     * currentTemperature >
     * temperature[topIndex]
     *
     * =>
     * resolve topIndex
     *
     * Pointer Movement
     * ----------------
     * currDay only moves forward.
     *
     * Stack only pops resolved indices.
     *
     * No index ever re-enters.
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * create answer
     * create stack
     *
     * for every index
     *     while warmer
     *         resolve top
     *     push current
     *
     * return answer
     */

    /*
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    static class BruteForce {

        /*
         * Idea
         * ----
         * Scan every future day until a warmer temperature appears.
         *
         * Invariant
         * ---------
         * The current suffix is searched completely.
         *
         * Limitation
         * ----------
         * Repeated rescanning.
         *
         * Complexity
         * ----------
         * Time  : O(n²)
         * Space : O(1)
         *
         * Interview Usefulness
         * --------------------
         * Good baseline before optimization.
         */

        public int[] dailyTemperatures(int[] temperatures) {
            int n = temperatures.length;
            int[] answer = new int[n];

            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (temperatures[j] > temperatures[i]) {
                        answer[i] = j - i;
                        break;
                    }
                }
            }

            return answer;
        }
    }

    static class Improved {

        /*
         * Idea
         * ----
         * Observe that the brute-force algorithm repeatedly scans the
         * same future days.
         *
         * Instead of asking:
         *
         * "Where is the next warmer day for this index?"
         *
         * Process days from right to left so that information about the
         * future has already been computed.
         *
         * We repeatedly jump using previously computed answers.
         *
         * This avoids many unnecessary comparisons, although in the
         * worst case it is still not as clean or generally reusable as
         * the monotonic stack solution.
         *
         * Invariant
         * ---------
         * Every day to the right already knows how far its own next
         * warmer temperature is.
         *
         * Improvement
         * -----------
         * Previously computed distances are reused to skip regions that
         * cannot contain the answer.
         *
         * Complexity
         * ----------
         * Typical:
         * Time  : Better than brute force.
         *
         * Worst Case:
         * Time  : O(n²)
         *
         * Space : O(1)
         *
         * Interview Usefulness
         * --------------------
         * Useful for understanding why we eventually move to a
         * monotonic stack, but the optimal interview solution remains
         * the stack approach.
         */

        public int[] dailyTemperatures(int[] temperatures) {

            int n = temperatures.length;
            int[] answer = new int[n];

            for (int i = n - 2; i >= 0; i--) {

                int j = i + 1;

                while (true) {

                    if (temperatures[j] > temperatures[i]) {
                        answer[i] = j - i;
                        break;
                    }

                    if (answer[j] == 0) {
                        break;
                    }

                    j += answer[j];
                }
            }

            return answer;
        }
    }

    static class Optimal {

        /*
         * Idea
         * ----
         * Maintain a monotonic decreasing stack of unresolved indices.
         *
         * Whenever the current temperature becomes strictly warmer than
         * the stack top, the current day is the earliest warmer day for
         * that index.
         *
         * Continue resolving until the invariant is restored.
         *
         * ------------------------------------------------------------
         * Invariant
         * ------------------------------------------------------------
         *
         * 1. Every index inside the stack is unresolved.
         *
         * 2. Stack temperatures decrease from bottom to top.
         *
         * 3. Once an index leaves the stack,
         *    its answer is final forever.
         *
         * ------------------------------------------------------------
         * Correctness
         * ------------------------------------------------------------
         *
         * The first day that can pop an index is necessarily the first
         * warmer day because any earlier warmer day would already have
         * removed it.
         *
         * ------------------------------------------------------------
         * Complexity
         * ------------------------------------------------------------
         *
         * Time:
         * O(n)
         *
         * Every index is pushed once.
         *
         * Every index is popped once.
         *
         * Space:
         * O(n)
         *
         * ------------------------------------------------------------
         * Interview Usefulness
         * ------------------------------------------------------------
         *
         * This is the expected optimal solution.
         *
         * The monotonic stack pattern appears repeatedly across
         * interview problems involving nearest greater/smaller element.
         */

        public int[] dailyTemperatures(int[] temperatures) {

            // 🔴 Edge Case:
            // Empty input immediately returns empty output.
            if (temperatures == null || temperatures.length == 0) {
                return new int[0];
            }

            int n = temperatures.length;

            int[] answer = new int[n];

            Deque<Integer> stack = new ArrayDeque<>();

            for (int currDay = 0; currDay < n; currDay++) {

                // 🟢 Invariant:
                // Current day resolves every colder unresolved day.
                while (!stack.isEmpty()
                        && temperatures[currDay] > temperatures[stack.peek()]) {

                    int prevDay = stack.pop();

                    // 🟢 Current day is guaranteed to be the earliest
                    // warmer day for prevDay.
                    answer[prevDay] = currDay - prevDay;
                }

                // 🟢 Remaining stack stays monotonically decreasing.
                stack.push(currDay);
            }

            // 🟢 Any remaining indices never encounter a warmer future
            // day. Their default answer of zero is already correct.
            return answer;
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Explain the Invariant
 * ---------------------
 *
 * I maintain a monotonic decreasing stack of indices.
 *
 * Every index inside the stack is still waiting for its first
 * warmer future day.
 *
 * The temperatures corresponding to those indices decrease from
 * bottom to top.
 *
 * ------------------------------------------------------------
 * Explain the Discard Rule
 * ------------------------------------------------------------
 *
 * Whenever the current temperature exceeds the temperature at the
 * top of the stack, I immediately resolve that index.
 *
 * I continue popping until the decreasing invariant is restored.
 *
 * ------------------------------------------------------------
 * Explain Correctness
 * ------------------------------------------------------------
 *
 * The current day is the first warmer day because if any earlier
 * warmer day existed, that earlier day would already have removed
 * the index from the stack.
 *
 * Therefore every computed distance is final.
 *
 * ------------------------------------------------------------
 * Explain Termination
 * ------------------------------------------------------------
 *
 * Each index is pushed exactly once.
 *
 * Each index is popped at most once.
 *
 * Therefore the algorithm performs a linear number of stack
 * operations.
 *
 * ------------------------------------------------------------
 * In-Place Feasibility
 * ------------------------------------------------------------
 *
 * No.
 *
 * We require an auxiliary stack because unresolved indices must be
 * remembered until a future day resolves them.
 *
 * ------------------------------------------------------------
 * Streaming Feasibility
 * ------------------------------------------------------------
 *
 * Yes.
 *
 * Incoming temperatures can continue resolving unresolved past
 * indices without revisiting earlier input.
 *
 * ------------------------------------------------------------
 * When NOT To Use
 * ------------------------------------------------------------
 *
 * Avoid this pattern when answers depend on every future element
 * instead of only the first qualifying element.
 */

/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 * First greater element to the right.
 *
 * Pattern
 * -------
 * Monotonic Decreasing Stack.
 *
 * Search Target
 * -------------
 * Earliest strictly warmer future day.
 *
 * Invariant
 * ---------
 * Stack stores unresolved indices in decreasing temperature order.
 *
 * Discard Rule
 * ------------
 * While current temperature is greater than stack top
 * temperature,
 * resolve and pop.
 *
 * Common Trap
 * -----------
 * Using >= instead of >.
 *
 * Edge Cases
 * ----------
 * Empty array.
 * Single element.
 * Strictly decreasing temperatures.
 * Duplicate temperatures.
 * Strictly increasing temperatures.
 *
 * One-Liner
 * ---------
 * Resolve colder unresolved days as soon as a warmer day appears.
 *
 * Re-Derivation Cue
 * -----------------
 * "Who is still waiting for a warmer day?"
 */
/*
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variation 1
 * -----------
 * Next Greater Element I
 *
 * Same Pattern
 * ------------
 * Monotonic decreasing stack.
 *
 * State Change
 * ------------
 * Instead of returning waiting distance,
 * return the first larger value.
 *
 * Invariant
 * ---------
 * Unchanged.
 *
 * ------------------------------------------------------------
 * Variation 2
 * -----------
 * Next Greater Element II (Circular Array)
 *
 * Pattern
 * -------
 * Monotonic stack with two passes.
 *
 * Reasoning Change
 * ----------------
 * Traverse indices from
 *
 * 0 ... n-1 ... 0 ... n-1
 *
 * using modulo arithmetic.
 *
 * Invariant
 * ---------
 * Still stores unresolved indices.
 *
 * Only the traversal changes.
 *
 * ------------------------------------------------------------
 * Variation 3
 * -----------
 * Stock Span
 *
 * Pattern
 * -------
 * Monotonic stack.
 *
 * Difference
 * ----------
 * Search direction reverses.
 *
 * Instead of searching future greater values,
 * we compress previous smaller values.
 *
 * Invariant
 * ---------
 * Still monotonic.
 *
 * ------------------------------------------------------------
 * Variation 4
 * -----------
 * Largest Rectangle in Histogram
 *
 * Pattern
 * -------
 * Monotonic increasing stack.
 *
 * Reasoning Change
 * ----------------
 * A pop computes a maximal rectangle instead of a waiting
 * distance.
 *
 * Invariant
 * ---------
 * Monotonic ordering still guarantees nearest boundary.
 *
 * ------------------------------------------------------------
 * Variation 5
 * -----------
 * Trapping Rain Water (Stack Version)
 *
 * Pattern
 * -------
 * Monotonic stack.
 *
 * Pop Event
 * ---------
 * Reveals a bounded valley.
 *
 * Same Philosophy
 * ---------------
 * A future element resolves previously unresolved structure.
 *
 * ------------------------------------------------------------
 * Variation 6
 * -----------
 * Previous Greater Element
 *
 * Change
 * ------
 * Traverse left-to-right while querying previous elements.
 *
 * Invariant
 * ---------
 * Monotonic ordering remains identical.
 *
 * ------------------------------------------------------------
 * Variation 7
 * -----------
 * Next Smaller Element
 *
 * Pattern Change
 * --------------
 * Reverse the comparison.
 *
 * Replace:
 *
 * current > top
 *
 * with
 *
 * current < top
 *
 * The invariant becomes monotonically increasing instead of
 * decreasing.
 *
 * ------------------------------------------------------------
 * Pattern Boundary
 * ------------------------------------------------------------
 *
 * This pattern succeeds whenever:
 *
 * - elements become permanently resolved
 * - nearest qualifying neighbor is required
 * - monotonic ordering prevents rescanning
 *
 * This pattern breaks whenever:
 *
 * - answers require every future element
 * - previous answers can become invalid later
 * - state cannot be represented by a monotonic structure
 */

/*
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * □ Do I know the invariant?
 *
 * Yes.
 *
 * Stack contains unresolved indices whose temperatures decrease
 * from bottom to top.
 *
 * ------------------------------------------------------------
 * □ Do I know the search target?
 *
 * Yes.
 *
 * First strictly warmer future day.
 *
 * ------------------------------------------------------------
 * □ Do I know the discard rule?
 *
 * Yes.
 *
 * While current temperature is strictly greater than the stack
 * top temperature,
 * resolve and pop.
 *
 * ------------------------------------------------------------
 * □ Do I know why correctness holds?
 *
 * Yes.
 *
 * Earlier warmer days would already have removed the index.
 *
 * ------------------------------------------------------------
 * □ Do I know termination?
 *
 * Yes.
 *
 * Push once.
 *
 * Pop once.
 *
 * Total stack operations are linear.
 *
 * ------------------------------------------------------------
 * □ Do I know why brute force fails?
 *
 * Yes.
 *
 * Future suffixes are rescanned repeatedly.
 *
 * ------------------------------------------------------------
 * □ Do I remember important edge cases?
 *
 * Yes.
 *
 * Empty array.
 *
 * Single element.
 *
 * Duplicate temperatures.
 *
 * Strictly decreasing temperatures.
 *
 * Strictly increasing temperatures.
 *
 * ------------------------------------------------------------
 * □ Can I debug confidently?
 *
 * Yes.
 *
 * Check:
 *
 * 1. Stack stores indices.
 *
 * 2. Comparison uses >
 *
 * 3. Pop occurs inside while.
 *
 * 4. Push occurs after all pops.
 *
 * ------------------------------------------------------------
 * □ Am I ready for variants?
 *
 * Yes.
 *
 * Switch comparison direction or traversal direction while
 * preserving the monotonic invariant.
 *
 * ------------------------------------------------------------
 * □ Do I know the pattern boundary?
 *
 * Yes.
 *
 * Use monotonic stacks only when unresolved states become
 * permanently solved by a future element.
 */

/*
 * ============================================================
 * ⚫ PATTERN MAPPING
 * ============================================================
 *
 * Problem
 * -------
 * Daily Temperatures
 *
 * Pattern
 * -------
 * Monotonic Decreasing Stack
 *
 * Search Space
 * ------------
 * Future indices.
 *
 * State
 * -----
 * Unresolved day indices.
 *
 * Transition
 * ----------
 * Current warmer day resolves previous colder days.
 *
 * Discard Rule
 * ------------
 * Pop every colder unresolved day.
 *
 * Correctness
 * -----------
 * First pop equals first warmer day.
 *
 * Termination
 * -----------
 * Each index enters once and leaves once.
 *
 * Complexity
 * ----------
 * Time  : O(n)
 * Space : O(n)
 */

/*
 * ============================================================
 * 🧠 IMPLEMENTATION RECONSTRUCTION DRILL
 * ============================================================
 *
 * Without looking at code, reconstruct the implementation:
 *
 * Step 1
 * ------
 * Create answer array.
 *
 * Step 2
 * ------
 * Create stack of indices.
 *
 * Step 3
 * ------
 * Traverse from left to right.
 *
 * Step 4
 * ------
 * While current temperature is warmer than the temperature at
 * the stack top:
 *
 *     pop previous day
 *
 *     compute waiting distance
 *
 * Step 5
 * ------
 * Push current day.
 *
 * Step 6
 * ------
 * Return answer.
 *
 * If these six mechanical steps are remembered, the full optimal
 * implementation can be reconstructed under interview pressure.
 */

/*
 * ============================================================
 * 🧠 DEBUGGING GUIDE
 * ============================================================
 *
 * Symptom
 * -------
 * Duplicate temperatures incorrectly resolve.
 *
 * Check
 * -----
 * Ensure comparison is:
 *
 * current > previous
 *
 * never
 *
 * current >= previous
 *
 * ------------------------------------------------------------
 * Symptom
 * -------
 * Waiting distance is incorrect.
 *
 * Check
 * -----
 * Stack must store indices,
 * not temperatures.
 *
 * ------------------------------------------------------------
 * Symptom
 * -------
 * Some earlier days never receive answers.
 *
 * Check
 * -----
 * Resolution loop must be a while-loop,
 * not an if-statement.
 *
 * ------------------------------------------------------------
 * Symptom
 * -------
 * Stack ordering becomes incorrect.
 *
 * Check
 * -----
 * Push the current index only after every possible pop has
 * completed.
 *
 * ------------------------------------------------------------
 * Symptom
 * -------
 * Time complexity unexpectedly becomes quadratic.
 *
 * Check
 * -----
 * Never scan backwards or rescan future indices manually.
 * Every unresolved index should only leave the stack once.
 */
    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        /*
         * Happy Path
         * ----------
         * Official representative example.
         */
        assert Arrays.equals(
                solver.dailyTemperatures(
                        new int[]{73, 74, 75, 71, 69, 72, 76, 73}),
                new int[]{1, 1, 4, 2, 1, 1, 0, 0});

        /*
         * Strictly Increasing
         * -------------------
         * Every day except the last immediately finds a warmer day.
         */
        assert Arrays.equals(
                solver.dailyTemperatures(
                        new int[]{30, 40, 50, 60}),
                new int[]{1, 1, 1, 0});

        /*
         * Increasing With Larger Gaps
         * ---------------------------
         * Every answer is still exactly one day.
         */
        assert Arrays.equals(
                solver.dailyTemperatures(
                        new int[]{30, 60, 90}),
                new int[]{1, 1, 0});

        /*
         * Strictly Decreasing
         * -------------------
         * Nobody ever finds a warmer future day.
         */
        assert Arrays.equals(
                solver.dailyTemperatures(
                        new int[]{90, 80, 70, 60}),
                new int[]{0, 0, 0, 0});

        /*
         * Duplicate Temperatures
         * ----------------------
         * Equal temperatures are NOT warmer.
         */
        assert Arrays.equals(
                solver.dailyTemperatures(
                        new int[]{70, 70, 70}),
                new int[]{0, 0, 0});

        /*
         * Duplicate Before Warmer Day
         * ---------------------------
         * Ensures strict comparison (>) is used.
         */
        assert Arrays.equals(
                solver.dailyTemperatures(
                        new int[]{70, 70, 71}),
                new int[]{2, 1, 0});

        /*
         * Single Element
         * --------------
         * Smallest non-empty input.
         */
        assert Arrays.equals(
                solver.dailyTemperatures(
                        new int[]{55}),
                new int[]{0});

        /*
         * Empty Input
         * -----------
         * Defensive implementation check.
         */
        assert Arrays.equals(
                solver.dailyTemperatures(
                        new int[]{}),
                new int[]{});

        /*
         * Valley Pattern
         * --------------
         * One warmer day resolves multiple previous days.
         */
        assert Arrays.equals(
                solver.dailyTemperatures(
                        new int[]{60, 55, 50, 70}),
                new int[]{3, 2, 1, 0});

        /*
         * Mixed Pattern
         * -------------
         * Common interview scenario with alternating rises/falls.
         */
        assert Arrays.equals(
                solver.dailyTemperatures(
                        new int[]{65, 62, 63, 61, 66}),
                new int[]{4, 1, 2, 1, 0});

        /*
         * Equal Peak
         * ----------
         * Equal temperatures should remain unresolved until a
         * strictly warmer day appears.
         */
        assert Arrays.equals(
                solver.dailyTemperatures(
                        new int[]{75, 74, 75, 76}),
                new int[]{3, 1, 1, 0});

        /*
         * Late Resolution
         * ---------------
         * Long wait before the warmer day arrives.
         */
        assert Arrays.equals(
                solver.dailyTemperatures(
                        new int[]{80, 79, 78, 77, 81}),
                new int[]{4, 3, 2, 1, 0});

        /*
         * Oscillating Temperatures
         * ------------------------
         * Verifies repeated push/pop behavior.
         */
        assert Arrays.equals(
                solver.dailyTemperatures(
                        new int[]{70, 72, 71, 73, 69, 74}),
                new int[]{1, 2, 1, 2, 1, 0});

        /*
         * Compare All Implementations
         * ---------------------------
         * Ensures algorithmic consistency.
         */
        BruteForce brute = new BruteForce();
        Improved improved = new Improved();

        int[] sample = {73, 71, 75, 70, 69, 72, 76, 73};

        assert Arrays.equals(
                brute.dailyTemperatures(sample),
                improved.dailyTemperatures(sample));

        assert Arrays.equals(
                improved.dailyTemperatures(sample),
                solver.dailyTemperatures(sample));

        System.out.println("All assertions passed.");
    }

}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/