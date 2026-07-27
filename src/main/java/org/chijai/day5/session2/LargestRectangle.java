package org.chijai.day5.session2;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ============================================================================
 * 📚 INVARIANT-FIRST ALGORITHM CHAPTER
 * ============================================================================
 *
 * Problem: Largest Rectangle in Histogram
 *
 * This file is a complete, self-contained, interview-grade algorithm chapter.
 * It is designed for:
 *   • Pattern mastery
 *   • Invariant reasoning
 *   • Long-term recall
 *   • Teaching & debugging
 *   • Zero dependency on LeetCode or internet
 *
 * ============================================================================
 */
public class LargestRectangle {

    // =========================================================================
    // 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
    // =========================================================================

    /*
     * 🔗 Link:
     * https://leetcode.com/problems/largest-rectangle-in-histogram/
     *
     * 🧩 Difficulty:
     * Hard
     *
     * 🏷️ Tags:
     * Array, Stack, Monotonic Stack
     *
     * ----------------------------------------------------------------------------
     * PROBLEM STATEMENT (VERBATIM)
     * ----------------------------------------------------------------------------
     *
     * Given an array of integers heights representing the histogram's bar height
     * where the width of each bar is 1, return the area of the largest rectangle
     * in the histogram.
     *
     * ----------------------------------------------------------------------------
     * Example 1:
     *
     * Input: heights = [2,1,5,6,2,3]
     * Output: 10
     *
     * Explanation:
     * The above is a histogram where width of each bar is 1.
     * The largest rectangle is shown in the red area, which has an area = 10 units.
     *
     * ----------------------------------------------------------------------------
     * Example 2:
     *
     * Input: heights = [2,4]
     * Output: 4
     *
     * ----------------------------------------------------------------------------
     * Constraints:
     *
     * 1 <= heights.length <= 10^5
     * 0 <= heights[i] <= 10^4
     *
     * ----------------------------------------------------------------------------
     */

    // =========================================================================
    // 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST · CANONICAL)
    // =========================================================================

    /*
     * 🔵 Pattern Name:
     * Monotonic Stack — Nearest Smaller Boundary Pattern
     *
     * 🔵 Problem Archetype:
     * "For each element, find the maximal span where it remains the minimum"
     *
     * 🟢 CORE INVARIANT (MANDATORY — ONE SENTENCE):
     *
     * At any point, the stack maintains indices of bars in strictly increasing
     * height order, such that for each index in the stack, no smaller bar exists
     * between it and the current processing position.
     *
     * -------------------------------------------------------------------------
     * 🟢 Why this invariant makes the pattern work:
     *
     * • A rectangle’s height is always limited by the smallest bar inside it.
     * • If we know the nearest smaller bar on the left and right,
     *   we know the *maximum width* where a bar can act as the minimum.
     * • The monotonic stack guarantees that when a bar is popped,
     *   BOTH boundaries are known *at that exact moment*.
     *
     * -------------------------------------------------------------------------
     * 🟡 When this pattern applies:
     *
     * • Histogram / skyline problems
     * • "Largest area / span" problems
     * • Problems asking for nearest smaller / greater elements
     * • Situations where brute-force span expansion is too slow
     *
     * -------------------------------------------------------------------------
     * 🧭 Pattern recognition signals:
     *
     * • Width is implicit, height varies
     * • Area depends on contiguous range
     * • Constraints up to 10^5 (O(n^2) impossible)
     * • Problem secretly asks:
     *   "For each bar, how far can it stretch?"
     *
     * -------------------------------------------------------------------------
     * ⚫ How this pattern differs from similar patterns:
     *
     * • Unlike two-pointer:
     *   - Boundaries are not symmetric or monotonic in movement
     *
     * • Unlike sliding window:
     *   - Window size is dynamic and element-dependent
     *
     * • Unlike prefix/suffix arrays:
     *   - Boundaries are discovered lazily, not precomputed blindly
     *
     * The MONOTONIC STACK is the only structure that preserves
     * the invariant needed to discover both boundaries correctly.
     *
     * -------------------------------------------------------------------------
     */


    // =========================================================================
    // 🟢 MENTAL MODEL & INVARIANTS (CANONICAL SOURCE OF TRUTH)
    // =========================================================================

    /*
     * 🟢 MENTAL MODEL (THINKING, NOT CODE)
     *
     * Imagine each bar asking a single question:
     *
     *   "How far can I extend left and right
     *    before I hit a bar shorter than me?"
     *
     * If a bar of height H can extend from index L+1 to R-1,
     * then it forms a rectangle of:
     *
     *   area = H × (R - L - 1)
     *
     * The ENTIRE problem reduces to finding,
     * for each bar:
     *   • nearest smaller bar on the left
     *   • nearest smaller bar on the right
     *
     * -------------------------------------------------------------------------
     * 🟢 WHY THIS IS HARD NAIVELY
     *
     * • Checking left and right for every bar is O(n²)
     * • Constraints allow only O(n)
     *
     * So we need a structure that:
     *   • remembers previous bars
     *   • forgets useless ones
     *   • reveals boundaries at the exact correct time
     *
     * That structure is a MONOTONIC INCREASING STACK.
     *
     * -------------------------------------------------------------------------
     */

    /*
     * 🟢 STATE REPRESENTATION
     *
     * Stack contents:
     *   • indices of histogram bars
     *
     * Stack invariant:
     *   • heights[stack[0]] < heights[stack[1]] < ... < heights[top]
     *
     * Meaning:
     *   • Stack is strictly increasing by height
     *   • Each bar in stack has NOT yet found a smaller bar on the right
     *
     * -------------------------------------------------------------------------
     */

    /*
     * 🟢 ALL INVARIANTS (EXPLICIT)
     *
     * Invariant #1:
     * Stack heights are strictly increasing from bottom to top.
     *
     * Invariant #2:
     * For any index i in the stack,
     *   there is NO smaller bar between i and the current index.
     *
     * Invariant #3:
     * When a bar is popped,
     *   • current index is the first smaller bar on the RIGHT
     *   • new stack top is the first smaller bar on the LEFT
     *
     * These three invariants together guarantee correctness.
     *
     * -------------------------------------------------------------------------
     */

    /*
     * 🟢 ALLOWED MOVES (INVARIANT-PRESERVING)
     *
     * ✔ Push index i:
     *   Only if heights[i] >= heights[stack.peek()]
     *
     * ✔ Pop index top:
     *   When heights[i] < heights[top]
     *
     * ✔ Compute area at pop time:
     *   height = heights[top]
     *   right boundary = i
     *   left boundary = stack.peek() (after pop) OR -1
     *
     * -------------------------------------------------------------------------
     */

    /*
     * 🔴 FORBIDDEN MOVES (INVARIANT VIOLATIONS)
     *
     * ❌ Precomputing left/right boundaries independently
     * ❌ Using two unrelated stacks
     * ❌ Guessing width without guaranteed smaller boundaries
     * ❌ Computing area BEFORE both boundaries are known
     *
     * Each of these breaks at least one invariant.
     *
     * -------------------------------------------------------------------------
     */

    /*
     * 🟢 TERMINATION LOGIC
     *
     * • Each index is pushed exactly once
     * • Each index is popped exactly once
     * • Stack operations are finite → O(n)
     *
     * Final cleanup:
     *   Treat "end of array" as a virtual height = 0
     *   to flush remaining bars
     *
     * -------------------------------------------------------------------------
     */

    /*
     * 🟡 WHY COMMON ALTERNATIVES ARE INFERIOR
     *
     * Two pointers:
     *   ❌ Cannot determine both boundaries simultaneously
     *
     * Prefix arrays:
     *   ❌ Compute boundaries blindly, not lazily
     *
     * Brute force:
     *   ❌ O(n²), fails constraints
     *
     * Only monotonic stack reveals boundaries
     * EXACTLY when they become valid.
     *
     * -------------------------------------------------------------------------
     */


    // =========================================================================
    // 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
    // =========================================================================

    /*
     * 🔴 COMMON WRONG APPROACH #1:
     * "Find next smaller element on left and right using two stacks"
     *
     * Why it seems correct:
     *   • Sounds modular
     *   • Works for simple test cases
     *
     * Why it fails:
     *   ❌ Left and right boundaries are NOT independent
     *   ❌ Boundaries must be discovered in a single temporal sweep
     *
     * Violated invariant:
     *   Invariant #3 — boundaries must be discovered together
     *
     * -------------------------------------------------------------------------
     */

    /*
     * 🔴 COMMON WRONG APPROACH #2:
     * "Track increasing and decreasing indices separately"
     *
     * Why it seems correct:
     *   • Attempts to capture shape
     *
     * Why it fails:
     *   ❌ Height dominance is local, not global
     *   ❌ You cannot pre-classify peaks and valleys
     *
     * Minimal counterexample:
     *   heights = [2, 1, 2]
     *
     * Correct answer = 3
     * Wrong logic computes only width 1 areas
     *
     * -------------------------------------------------------------------------
     */

    /*
     * 🔴 YOUR SOLUTION — WHY IT FAILS (IMPORTANT)
     *
     * Key issues:
     *
     * 1️⃣ Two independent stacks (leftStack, rightStack)
     *    → Violates the SINGLE-STACK invariant
     *
     * 2️⃣ Boundaries guessed using peek logic
     *    → No guarantee that peek is the nearest smaller
     *
     * 3️⃣ currLeft defaults to 0 instead of -1
     *    → Off-by-one boundary corruption
     *
     * 4️⃣ No guarantee that:
     *    "all bars between currLeft and currRight ≥ heights[i]"
     *
     * This violates the CORE invariant:
     *
     *   "No smaller bar exists between boundaries"
     *
     * -------------------------------------------------------------------------
     */

    /*
     * 🔴 MINIMAL COUNTEREXAMPLE FOR YOUR CODE
     *
     * heights = [2,1,5,6,2,3]
     *
     * For bar height 5 (index 2):
     *   Correct boundaries:
     *     left = index 1
     *     right = index 4
     *     width = 2
     *     area = 10
     *
     * Your logic:
     *   ❌ rightStack / leftStack lose ordering guarantee
     *   ❌ currRight often jumps to n incorrectly
     *
     * Result:
     *   Either undercounts or overcounts width
     *
     * -------------------------------------------------------------------------
     */

    /*
     * 🎯 INTERVIEWER TRAP EXPLANATION
     *
     * Interviewers EXPECT:
     *   • You to attempt left/right arrays
     *   • Then realize they fail edge cases
     *
     * What they want:
     *   "We must discover boundaries at the moment
     *    when a bar is invalidated by a smaller bar."
     *
     * That sentence is the invariant.
     *
     * -------------------------------------------------------------------------
     */


    // =========================================================================
    // 🟢 PRIMARY PROBLEM — SOLUTION CLASSES (DERIVED FROM INVARIANT)
    // =========================================================================

    /*
     * -------------------------------------------------------------------------
     * 🧪 SOLUTION 1: BRUTE FORCE
     * -------------------------------------------------------------------------
     */

    static class BruteForceSolution {

        /*
         * 🔵 Core idea:
         * For each bar, expand left and right until a smaller bar is found.
         *
         * 🟢 Invariant (partially enforced):
         * We ensure all bars in the chosen span are >= current height.
         *
         * 🔴 Limitation:
         * Boundaries are re-discovered redundantly → O(n²)
         *
         * ⏱ Time: O(n²)
         * 🧠 Space: O(1)
         * 🎤 Interview preference: ❌ (baseline only)
         */
        public int largestRectangleArea(int[] heights) {
            int maxArea = 0;

            for (int center = 0; center < heights.length; center++) {
                int height = heights[center];

                int left = center;
                while (left >= 0 && heights[left] >= height) {
                    left--;
                }

                int right = center;
                while (right < heights.length && heights[right] >= height) {
                    right++;
                }

                int width = right - left - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            return maxArea;
        }
    }

    /*
     * -------------------------------------------------------------------------
     * 🧪 SOLUTION 2: IMPROVED (PRECOMPUTED BOUNDARIES)
     * -------------------------------------------------------------------------
     */

    static class ImprovedSolution {

        /*
         * 🔵 Core idea:
         * Precompute nearest smaller bar on left and right.
         *
         * 🟡 Reasoning:
         * Each bar needs both boundaries.
         *
         * 🔴 Limitation:
         * Requires two passes and two arrays.
         * Still misses the elegance of lazy boundary discovery.
         *
         * ⏱ Time: O(n)
         * 🧠 Space: O(n)
         * 🎤 Interview preference: ⚠️ Acceptable but not ideal
         */
        public int largestRectangleArea(int[] heights) {
            int n = heights.length;
            int[] leftSmaller = new int[n];
            int[] rightSmaller = new int[n];

            java.util.Stack<Integer> stack = new java.util.Stack<>();

            // Nearest smaller to left
            for (int i = 0; i < n; i++) {
                while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                    stack.pop();
                }
                leftSmaller[i] = stack.isEmpty() ? -1 : stack.peek();
                stack.push(i);
            }

            stack.clear();

            // Nearest smaller to right
            for (int i = n - 1; i >= 0; i--) {
                while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                    stack.pop();
                }
                rightSmaller[i] = stack.isEmpty() ? n : stack.peek();
                stack.push(i);
            }

            int maxArea = 0;
            for (int i = 0; i < n; i++) {
                int width = rightSmaller[i] - leftSmaller[i] - 1;
                maxArea = Math.max(maxArea, heights[i] * width);
            }
            return maxArea;
        }
    }

    /*
     * -------------------------------------------------------------------------
     * 🏆 SOLUTION 3: OPTIMAL (INTERVIEW-PREFERRED)
     * -------------------------------------------------------------------------
     */

    static class OptimalMonotonicStackSolution {

        /*
         * 🔵 Core idea:
         * Use a single monotonic increasing stack.
         *
         * 🟢 Full invariant enforcement:
         * • Stack always increasing by height
         * • Pop reveals both boundaries immediately
         *
         * ⏱ Time: O(n)
         * 🧠 Space: O(n)
         * 🎤 Interview preference: ✅ GOLD STANDARD
         */
        public int largestRectangleArea(int[] heights) {
            int n = heights.length;
            long maxArea = 0; // long for safe intermediate multiplication

            // Stack stores indices of bars with increasing heights
            // Modern Java Stack:
            // Deque + ArrayDeque provides stack (LIFO) operations.
            // Use push(), pop(), peek().
            // Preferred over the legacy synchronized Stack class.
            Deque<Integer> increasingStack = new ArrayDeque<>();

            /*
             * We iterate ONE EXTRA step (i == n) with a virtual height = 0.
             *
             * Purpose:
             * - Force all remaining bars in the stack to be popped
             * - This finalizes rectangles that extend till the end
             */
            /*
             * Every bar eventually needs a "closing event"
             * that tells it:
             *
             * "You cannot extend any further."
             *
             * Smaller bars naturally close taller bars.
             *
             * But bars that survive until the end never
             * receive such an event.
             *
             * So we create one final imaginary bar
             * of height 0.
             *
             * It is smaller than every real bar,
             * forcing every remaining rectangle
             * to be finalized.
             */
            for (int i = 0; i <= n; i++) {

                int currentHeight = (i == n) ? 0 : heights[i];

                /*
                 * Invariant violation:
                 * If the current bar is shorter than the bar at stack top,
                 * the rectangle with height = heights[top] CANNOT extend further.
                 *
                 * So we must:
                 * 1) Pop it
                 * 2) Compute its maximal rectangle NOW
                 */
                while (!increasingStack.isEmpty()
                        && currentHeight < heights[increasingStack.peek()]) {

                    int heightIndex = increasingStack.pop();
                    int height = heights[heightIndex];

                    /*
                     * Right boundary:
                     * - The current index i is the first smaller bar on the right

                     */
                    int rightBoundary = i ;

                    /*
                     * Left boundary:
                     * - If stack is empty after pop, no smaller bar exists on the left
                     *   → rectangle starts from index 0
                     * - Else, the new stack top is the nearest smaller bar on the left
                     *   → rectangle starts from stack.peek() + 1
                     */
                    int leftBoundary = increasingStack.isEmpty()
                            ? 0
                            : increasingStack.peek() + 1;

                    int width = rightBoundary - leftBoundary;
                    long area = (long) height * width;

                    maxArea = Math.max(maxArea, area);
                }

                /*
                 * Push current index.
                 *
                 * This maintains the invariant:
                 * stack heights remain strictly increasing.
                 */
                increasingStack.push(i);
            }

            // Problem guarantees result fits in int
            return (int) maxArea;
        }
    }


    // =========================================================================
    // 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
    // =========================================================================

    /*
     * 🟣 HOW TO EXPLAIN THIS IN AN INTERVIEW (60–90 SECONDS)
     *
     * 1️⃣ State the invariant first (this is non-negotiable):
     *
     *   "I maintain a monotonic increasing stack of bar indices.
     *    For each index in the stack, there is no smaller bar
     *    between it and the current index."
     *
     * 2️⃣ Explain transitions:
     *
     *   • When the current bar is taller or equal, I push it.
     *   • When it is shorter, I pop until the invariant is restored.
     *
     * 3️⃣ Explain why popping is correct:
     *
     *   • The current index is the first smaller bar on the right.
     *   • The new stack top is the first smaller bar on the left.
     *   • That uniquely determines the maximum width.
     *
     * 4️⃣ Explain termination:
     *
     *   • Each index is pushed once and popped once → O(n).
     *   • A virtual 0-height bar flushes remaining rectangles.
     *
     * 5️⃣ What breaks if logic changes:
     *
     *   • If stack isn’t monotonic → boundaries become invalid.
     *   • If area is computed earlier → right boundary is unknown.
     *
     * 6️⃣ In-place feasibility:
     *
     *   • Yes, input array untouched.
     *
     * 7️⃣ Streaming feasibility:
     *
     *   • Partially — requires flush at the end.
     *
     * 8️⃣ When NOT to use this pattern:
     *
     *   • When width is not contiguous
     *   • When minimum element does not define the score
     *
     * -------------------------------------------------------------------------
     */


    // =========================================================================
    // 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
    // =========================================================================

    /*
     * 🟢 INVARIANT-PRESERVING CHANGES
     *
     * • Using ArrayDeque instead of Stack
     * • Using long instead of int for area
     * • Prepending/appending sentinel 0 explicitly
     *
     * Invariant remains unchanged.
     *
     * -------------------------------------------------------------------------
     */

    /*
     * 🟡 REASONING-ONLY CHANGES
     *
     * • Changing >= to >
     *   → Affects duplicate height handling
     *   → Must be reasoned carefully
     *
     * • Using leftSmaller/rightSmaller arrays
     *   → Still same invariant, eagerly evaluated
     *
     * -------------------------------------------------------------------------
     */

    /*
     * 🔴 PATTERN-BREAK SIGNALS
     *
     * • Bars can be rearranged
     * • Width is not unit-based
     * • Rectangle score is not min-height based
     *
     * Invariant collapses → stack no longer applies.
     *
     * -------------------------------------------------------------------------
     */


    // =========================================================================
    // ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS · SAME INVARIANT)
    // =========================================================================

    /*
     * ========================================================================
     * REINFORCEMENT PROBLEM 1
     * ========================================================================
     */

    /*
     * 📘 LeetCode 85 — Maximal Rectangle
     *
     * 🔗 https://leetcode.com/problems/maximal-rectangle/
     * 🧩 Difficulty: Hard
     * 🏷️ Tags: Array, DP, Stack
     *
     * ----------------------------------------------------------------------------
     * Given a rows x cols binary matrix filled with '0's and '1's,
     * find the largest rectangle containing only '1's and return its area.
     *
     * ----------------------------------------------------------------------------
     * Input:
     * matrix = [
     *   ["1","0","1","0","0"],
     *   ["1","0","1","1","1"],
     *   ["1","1","1","1","1"],
     *   ["1","0","0","1","0"]
     * ]
     *
     * Output: 6
     * ----------------------------------------------------------------------------
     */

    /*
     * 🧠 INVARIANT MAPPING
     *
     * • Each row builds a histogram of consecutive 1s
     * • For each row, invariant is IDENTICAL to histogram problem
     *
     * What remains unchanged:
     *   • Monotonic stack invariant
     *
     * What changes:
     *   • Heights are accumulated row-wise
     *
     * -------------------------------------------------------------------------
     */

    static class MaximalRectangleSolution {

        public int maximalRectangle(char[][] matrix) {
            if (matrix.length == 0) return 0;

            int columns = matrix[0].length;
            int[] heights = new int[columns];
            int maxArea = 0;

            for (char[] row : matrix) {
                for (int col = 0; col < columns; col++) {
                    heights[col] = (row[col] == '1')
                            ? heights[col] + 1
                            : 0;
                }
                maxArea = Math.max(
                        maxArea,
                        new OptimalMonotonicStackSolution()
                                .largestRectangleArea(heights)
                );
            }
            return maxArea;
        }
    }

    /*
     * 🧪 EDGE CASE & TRAP
     *
     * • All zeros row resets histogram
     * • Interview trap: forgetting to reset heights
     *
     * -------------------------------------------------------------------------
     */


    /*
     * ========================================================================
     * REINFORCEMENT PROBLEM 2
     * ========================================================================
     */

    /*
     * 📘 LeetCode 84 Variant — Largest Rectangle with At Least K Width
     *
     * 🧠 INVARIANT MAPPING
     *
     * Same invariant, but:
     * • Only rectangles with width >= K are valid
     *
     * Strategy:
     * • Compute width normally
     * • Discard rectangles with width < K
     *
     * Invariant holds; filter is post-computation.
     *
     * -------------------------------------------------------------------------
     */

    static class LargestRectangleAtLeastKWidth {

        public int largestRectangleAreaAtLeastK(int[] heights, int k) {
            java.util.Stack<Integer> stack = new java.util.Stack<>();
            int maxArea = 0;

            for (int i = 0; i <= heights.length; i++) {
                int currentHeight = (i == heights.length) ? 0 : heights[i];

                while (!stack.isEmpty()
                        && currentHeight < heights[stack.peek()]) {

                    int heightIndex = stack.pop();
                    int height = heights[heightIndex];

                    int right = i;
                    int left = stack.isEmpty() ? -1 : stack.peek();
                    int width = right - left - 1;

                    if (width >= k) {
                        maxArea = Math.max(maxArea, height * width);
                    }
                }
                stack.push(i);
            }
            return maxArea;
        }
    }

    /*
     * 🟣 INTERVIEW ARTICULATION (REINFORCEMENT)
     *
     * "Same invariant. We only add a validation condition on width."
     *
     * -------------------------------------------------------------------------
     */


    // =========================================================================
    // 🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
    // =========================================================================

    /*
     * ========================================================================
     * RELATED PROBLEM 1
     * ========================================================================
     */

    /*
     * 📘 LeetCode 907 — Sum of Subarray Minimums
     *
     * 🔗 https://leetcode.com/problems/sum-of-subarray-minimums/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Stack, Array
     *
     * ----------------------------------------------------------------------------
     * Given an array of integers arr, find the sum of min(b),
     * where b ranges over every (contiguous) subarray of arr.
     *
     * ----------------------------------------------------------------------------
     */

    /*
     * 🧠 RELATIONSHIP TO PRIMARY INVARIANT
     *
     * Same invariant:
     * • Each element contributes as the minimum
     * • Contribution span is bounded by nearest smaller elements
     *
     * Difference:
     * • We SUM contributions instead of maximizing area
     *
     * Invariant remains identical.
     *
     * -------------------------------------------------------------------------
     */

    static class SumOfSubarrayMinimums {

        public int sumSubarrayMins(int[] arr) {
            int n = arr.length;
            long MOD = 1_000_000_007L;
            long result = 0;

            java.util.Stack<Integer> stack = new java.util.Stack<>();

            for (int i = 0; i <= n; i++) {
                int current = (i == n) ? Integer.MIN_VALUE : arr[i];

                while (!stack.isEmpty() && current < arr[stack.peek()]) {
                    int minIndex = stack.pop();
                    int left = stack.isEmpty() ? -1 : stack.peek();
                    int right = i;

                    long count =
                            (long) (minIndex - left) * (right - minIndex);

                    result = (result + arr[minIndex] * count) % MOD;
                }
                stack.push(i);
            }
            return (int) result;
        }
    }

    /*
     * 🧪 EDGE CASE + INTERVIEW NOTE
     *
     * • Duplicates require careful < vs <=
     * • Interviewers test invariant flexibility
     *
     * -------------------------------------------------------------------------
     */


    // =========================================================================
    // 🟢 LEARNING VERIFICATION (INVARIANT-FIRST)
    // =========================================================================

    /*
     * You must be able to recall WITHOUT code:
     *
     * • Invariant:
     *   Stack is strictly increasing by height.
     *
     * • Why naive fails:
     *   Boundaries are not independent.
     *
     * • Bugs you should debug intentionally:
     *   - Forgetting sentinel flush
     *   - Wrong comparison operator
     *   - Wrong left boundary default
     *
     * • Detecting this invariant in unseen problems:
     *   "Each element’s value limits a contiguous span"
     *
     * -------------------------------------------------------------------------
     */


    // =========================================================================
    // 🧪 main() METHOD + SELF-VERIFYING TESTS (MUST BE LAST)
    // =========================================================================

    public static void main(String[] args) {

        OptimalMonotonicStackSolution optimal =
                new OptimalMonotonicStackSolution();

        // ---------------------------------------------------------
        // Happy path
        // ---------------------------------------------------------
        assertEquals(
                10,
                optimal.largestRectangleArea(
                        new int[]{2, 1, 5, 6, 2, 3}
                ),
                "Classic example with internal valley"
        );

        // ---------------------------------------------------------
        // Boundary: single bar
        // ---------------------------------------------------------
        assertEquals(
                4,
                optimal.largestRectangleArea(new int[]{4}),
                "Single bar should return its height"
        );

        // ---------------------------------------------------------
        // Increasing bars
        // ---------------------------------------------------------
        assertEquals(
                4,
                optimal.largestRectangleArea(new int[]{1, 2, 3}),
                "Increasing sequence max uses last bar"
        );

        // ---------------------------------------------------------
        // Decreasing bars (interviewer trap)
        // ---------------------------------------------------------
        assertEquals(
                4,
                optimal.largestRectangleArea(new int[]{3, 2, 1}),
                "Decreasing sequence forces repeated pops"
        );

        // ---------------------------------------------------------
        // All equal bars
        // ---------------------------------------------------------
        assertEquals(
                8,
                optimal.largestRectangleArea(new int[]{2, 2, 2, 2}),
                "Equal bars should span full width"
        );

        System.out.println("✅ All invariant-based tests passed.");
    }

    private static void assertEquals(
            int expected,
            int actual,
            String reason
    ) {
        if (expected != actual) {
            throw new AssertionError(
                    "FAILED: " + reason +
                            " | expected=" + expected +
                            " actual=" + actual
            );
        }
    }


    // =========================================================================
    // 🧠 CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
    // =========================================================================

    /*
     * • Invariant → Stack strictly increasing by height
     * • Search target → Max rectangle where bar is minimum
     * • Discard rule → Pop when smaller bar appears
     * • Termination guarantee → Each index pushed & popped once
     * • Naive failure → Boundaries guessed independently
     * • Edge cases → Decreasing, duplicates, single bar
     * • Variant readiness → Yes (sum of mins, maximal rectangle)
     * • Pattern boundary → Breaks if width not contiguous
     *
     * -------------------------------------------------------------------------
     */


    // =========================================================================
    // 🧘 FINAL CLOSURE STATEMENT (PROBLEM-SPECIFIC)
    // =========================================================================

    /*
     * For this problem, the invariant is that the stack remains
     * strictly increasing by bar height.
     *
     * The answer represents the maximum area rectangle where
     * a bar is the minimum height across its valid span.
     *
     * The search terminates because every bar is pushed and popped once.
     *
     * I can re-derive this solution under pressure.
     *
     * This chapter is complete.
     *
     * 📌 If I can explain the invariant and the discard rule, I am done.
     */
}
