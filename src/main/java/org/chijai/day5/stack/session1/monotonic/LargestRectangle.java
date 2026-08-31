package org.chijai.day5.stack.session1.monotonic;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Largest Rectangle in Histogram — V3
 *
 * Primary classification:
 *
 * stack/
 *   monotonicStack/
 *     LargestRectangleInHistogram.java
 *
 * Core reusable family:
 *
 * Nearest Smaller / Greater Boundary
 *
 * Reusable motto:
 *
 * "When the current value breaks the monotonic invariant,
 *  pop unresolved indices and finalize their boundary."
 */
public class LargestRectangle {

    /*
     * ============================================================
     * 📘 PROBLEM
     * ============================================================
     *
     * Given histogram bar heights, return the largest rectangle area.
     *
     * Example:
     *
     * heights = [2,1,5,6,2,3]
     * answer  = 10
     *
     * For every bar:
     *
     *     area = height * maximum width
     *
     * The real question is:
     *
     *     "How far can this bar extend left and right
     *      while remaining the minimum height?"
     */

    /*
     * ============================================================
     * 🧭 EXACT CLASSIFICATION
     * ============================================================
     *
     * PRIMARY:
     *
     *     Monotonic Stack
     *
     * SUBTYPE:
     *
     *     Nearest Smaller Boundary
     *
     * ARCHETYPE:
     *
     *     For each element, find the maximal contiguous span
     *     where it remains the minimum.
     *
     * ------------------------------------------------------------
     * DO NOT CONFUSE WITH
     * ------------------------------------------------------------
     *
     * Trapping Rain Water using leftMax[] / rightMax[]
     *
     *     need BEST value anywhere on left/right
     *     -> Prefix / Suffix
     *
     * Container With Most Water
     *
     *     can permanently discard one endpoint
     *     -> Opposite-End Two Pointers
     *
     * Largest Rectangle
     *
     *     need NEAREST smaller boundary on left/right
     *     -> Monotonic Stack
     */

    /*
     * ============================================================
     * 🧠 CORE MENTAL MODEL
     * ============================================================
     *
     * Each bar asks:
     *
     *     "Where is the first smaller bar on my LEFT?"
     *
     *     "Where is the first smaller bar on my RIGHT?"
     *
     * If:
     *
     *     leftSmaller  = L
     *     rightSmaller = R
     *
     * then the bar can occupy:
     *
     *     L + 1 ... R - 1
     *
     * width:
     *
     *     R - L - 1
     *
     * area:
     *
     *     heights[i] * (R - L - 1)
     *
     * ------------------------------------------------------------
     * CORE INVARIANT
     * ------------------------------------------------------------
     *
     * The stack stores indices whose heights are monotonic
     * NON-DECREASING from bottom to top.
     *
     * Every index still in the stack is UNRESOLVED:
     *
     *     it has not yet found a smaller bar on its right.
     *
     * ------------------------------------------------------------
     * POP EVENT
     * ------------------------------------------------------------
     *
     * When:
     *
     *     currentHeight < heights[stack.peek()]
     *
     * the top bar can no longer extend right.
     *
     * Therefore when we pop:
     *
     *     current index
     *     = first smaller boundary on RIGHT
     *
     *     new stack top after pop
     *     = first smaller boundary on LEFT
     *
     * That is the entire algorithm.
     */

    /*
     * ============================================================
     * 📈 APPROACH PROGRESSION
     * ============================================================
     *
     * 1. BRUTE FORCE
     *
     * For every bar:
     *
     *     scan left until smaller
     *     scan right until smaller
     *
     * Time:
     *
     *     O(n^2)
     *
     * Space:
     *
     *     O(1)
     *
     * ------------------------------------------------------------
     *
     * 2. PRECOMPUTE BOUNDARIES
     *
     * Compute:
     *
     *     leftSmaller[i]
     *     rightSmaller[i]
     *
     * using monotonic stacks.
     *
     * Then:
     *
     *     width = rightSmaller[i] - leftSmaller[i] - 1
     *
     * Time:
     *
     *     O(n)
     *
     * Space:
     *
     *     O(n)
     *
     * This is fully correct.
     *
     * ------------------------------------------------------------
     *
     * 3. SINGLE-STACK POP-TIME SOLUTION
     *
     * Do not store both boundary arrays.
     *
     * When a smaller current bar appears:
     *
     *     pop old bar
     *
     *     current index = right boundary
     *
     *     new stack top = left boundary
     *
     * Time:
     *
     *     O(n)
     *
     * Space:
     *
     *     O(n)
     *
     * This is the preferred reusable interview solution.
     */

    /*
     * ============================================================
     * 🧪 SOLUTION 1 — BRUTE FORCE
     * ============================================================
     */

    static class BruteForceSolution {

        public int largestRectangleArea(int[] heights) {

            int best = 0;

            for (int center = 0; center < heights.length; center++) {

                int height = heights[center];

                int left = center;
                while (left >= 0 && heights[left] >= height) {
                    left--;
                }

                int right = center;
                while (right < heights.length
                        && heights[right] >= height) {
                    right++;
                }

                int width = right - left - 1;

                best = Math.max(best, height * width);
            }

            return best;
        }
    }

    /*
     * ============================================================
     * 🧪 SOLUTION 2 — PRECOMPUTED NEAREST-SMALLER ARRAYS
     * ============================================================
     *
     * Same invariant, evaluated eagerly.
     *
     * leftSmaller[i]:
     *
     *     nearest index left of i with smaller height
     *
     * rightSmaller[i]:
     *
     *     nearest index right of i with smaller height
     *
     * Defaults:
     *
     *     no smaller on left  -> -1
     *     no smaller on right -> n
     *
     * This approach is worth knowing because it makes the
     * boundary formula extremely explicit.
     */

    static class BoundaryArraysSolution {

        public int largestRectangleArea(int[] heights) {

            int n = heights.length;

            int[] leftSmaller = new int[n];
            int[] rightSmaller = new int[n];

            Deque<Integer> stack = new ArrayDeque<>();

            // Previous smaller.
            for (int i = 0; i < n; i++) {

                while (!stack.isEmpty()
                        && heights[stack.peek()] >= heights[i]) {
                    stack.pop();
                }

                leftSmaller[i] =
                        stack.isEmpty() ? -1 : stack.peek();

                stack.push(i);
            }

            stack.clear();

            // Next smaller.
            for (int i = n - 1; i >= 0; i--) {

                while (!stack.isEmpty()
                        && heights[stack.peek()] >= heights[i]) {
                    stack.pop();
                }

                rightSmaller[i] =
                        stack.isEmpty() ? n : stack.peek();

                stack.push(i);
            }

            int best = 0;

            for (int i = 0; i < n; i++) {

                int width =
                        rightSmaller[i]
                        - leftSmaller[i]
                        - 1;

                best = Math.max(
                        best,
                        heights[i] * width
                );
            }

            return best;
        }
    }

    /*
     * ============================================================
     * 🏆 SOLUTION 3 — SINGLE MONOTONIC STACK
     * ============================================================
     *
     * REUSABLE SHAPE:
     *
     * for each current index
     *
     *     while current breaks stack invariant
     *
     *         pop unresolved index
     *
     *         right = current
     *         left  = new stack top
     *
     *         compute problem-specific answer
     *
     *     push current
     *
     * ------------------------------------------------------------
     * WHY ONE EXTRA ITERATION?
     * ------------------------------------------------------------
     *
     * Bars surviving until the end never encounter a smaller bar.
     *
     * So we simulate one final bar of height 0.
     *
     * This forces every remaining real bar to be popped and finalized.
     */

    static class OptimalMonotonicStackSolution {

        public int largestRectangleArea(int[] heights) {

            int n = heights.length;
            int best = 0;

            Deque<Integer> stack = new ArrayDeque<>();

            for (int i = 0; i <= n; i++) {

                int currentHeight =
                        (i == n) ? 0 : heights[i];

                while (!stack.isEmpty()
                        && currentHeight
                        < heights[stack.peek()]) {

                    int index = stack.pop();
                    int height = heights[index];

                    int rightBoundary = i;

                    int leftBoundary =
                            stack.isEmpty()
                            ? -1
                            : stack.peek();

                    int width =
                            rightBoundary
                            - leftBoundary
                            - 1;

                    best = Math.max(
                            best,
                            height * width
                    );
                }

                // Do not push virtual index n.
                if (i < n) {
                    stack.push(i);
                }
            }

            return best;
        }
    }

    /*
     * ============================================================
     * 🔬 WHY POP GIVES BOTH BOUNDARIES
     * ============================================================
     *
     * Suppose index x is popped because current index i is smaller.
     *
     * RIGHT:
     *
     *     i is the first smaller bar encountered after x.
     *
     * LEFT:
     *
     *     after popping x, the new stack top is the nearest
     *     surviving smaller bar on the left.
     *
     * Therefore:
     *
     *     left  = stack.peek() after pop, or -1
     *     right = i
     *
     *     width = right - left - 1
     *
     * ------------------------------------------------------------
     * COMPLEXITY
     * ------------------------------------------------------------
     *
     * Every index:
     *
     *     pushed once
     *     popped once
     *
     * Total:
     *
     *     O(n)
     *
     * The while loop does NOT make this O(n^2).
     */

    /*
     * ============================================================
     * ⚠️ IMPORTANT NUANCES
     * ============================================================
     *
     * 1. DUPLICATES
     *
     * Chosen one-pass rule:
     *
     *     pop only when current < top
     *
     * Therefore equal heights may coexist in the stack.
     *
     * Stack is:
     *
     *     non-decreasing
     *
     * not strictly increasing.
     *
     * ------------------------------------------------------------
     *
     * In the two-pass boundary-array solution we use:
     *
     *     >=
     *
     * while searching nearest strictly smaller boundaries.
     *
     * Both approaches are valid because duplicate handling is
     * internally consistent.
     *
     * ------------------------------------------------------------
     *
     * 2. SENTINEL
     *
     * Virtual final height 0 exists only to flush the stack.
     *
     * We do not need to physically modify the input array.
     *
     * ------------------------------------------------------------
     *
     * 3. SPACE
     *
     * Input is untouched, but this is NOT O(1)-space.
     *
     * The stack may hold O(n) indices.
     */

    /*
     * ============================================================
     * 🔴 EARLIER ATTEMPT — EXACT FAILURE
     * ============================================================
     *
     * Using two arbitrary stacks is NOT wrong merely because
     * there are two stacks.
     *
     * The real requirement is:
     *
     *     each boundary must be guaranteed NEAREST and SMALLER.
     *
     * A stack top has meaning only when a monotonic invariant
     * proves that meaning.
     *
     * Therefore:
     *
     * two proper monotonic-stack passes
     *     -> valid
     *
     * two unrelated stacks with guessed peek boundaries
     *     -> invalid
     *
     * Core debugging question:
     *
     *     "What exactly does stack.peek() mathematically guarantee?"
     */

    /*
     * ============================================================
     * ♻️ REUSABLE FAMILY
     * ============================================================
     *
     * SAME BOUNDARY / CONTRIBUTION ENGINE
     * -----------------------------------
     *
     * Largest Rectangle in Histogram
     *
     *     pop -> get span
     *     height * width
     *
     * Maximal Rectangle
     *
     *     convert each matrix row into histogram heights
     *     reuse Largest Rectangle unchanged
     *
     * Sum of Subarray Minimums
     *
     *     pop -> get previous/next boundary
     *
     *     contribution =
     *
     *         value
     *         * choicesOnLeft
     *         * choicesOnRight
     *
     * Sum of Subarray Ranges
     *
     *     maximum contributions
     *     -
     *     minimum contributions
     *
     * ------------------------------------------------------------
     * SAME UNRESOLVED-STACK ENGINE, SIMPLER OUTPUT
     * ------------------------------------------------------------
     *
     * Next Greater Element
     * Next Smaller Element
     * Daily Temperatures
     * Stock Span
     *
     * Recognition:
     *
     *     current element resolves previous unresolved elements
     */

    /*
     * ============================================================
     * ♻️ GENERIC POP-TIME SKELETON
     * ============================================================
     *
     * Deque<Integer> stack = new ArrayDeque<>();
     *
     * for (int i = 0; i <= n; i++) {
     *
     *     int current = (i == n)
     *             ? sentinel
     *             : nums[i];
     *
     *     while (!stack.isEmpty()
     *             && current breaksPredicate(
     *                     nums[stack.peek()])) {
     *
     *         int index = stack.pop();
     *
     *         int right = i;
     *         int left = stack.isEmpty()
     *                 ? -1
     *                 : stack.peek();
     *
     *         // PROBLEM-SPECIFIC CALCULATION
     *     }
     *
     *     stack.push(i);
     * }
     *
     * ------------------------------------------------------------
     * MENTAL RULE
     * ------------------------------------------------------------
     *
     * "The stack stores unresolved indices.
     *  When current defeats the top,
     *  pop and finalize it."
     */

    /*
     * ============================================================
     * 🔗 RELATED IMPLEMENTATION 1 — MAXIMAL RECTANGLE
     * ============================================================
     *
     * Each row becomes a histogram of consecutive 1s.
     *
     * Then call the SAME histogram solver.
     */

    static class MaximalRectangleSolution {

        public int maximalRectangle(char[][] matrix) {

            if (matrix == null || matrix.length == 0) {
                return 0;
            }

            int cols = matrix[0].length;
            int[] heights = new int[cols];

            int best = 0;

            OptimalMonotonicStackSolution histogram =
                    new OptimalMonotonicStackSolution();

            for (char[] row : matrix) {

                for (int col = 0; col < cols; col++) {

                    heights[col] =
                            row[col] == '1'
                            ? heights[col] + 1
                            : 0;
                }

                best = Math.max(
                        best,
                        histogram.largestRectangleArea(heights)
                );
            }

            return best;
        }
    }

    /*
     * ============================================================
     * 🔗 RELATED IMPLEMENTATION 2 — SUM OF SUBARRAY MINIMUMS
     * ============================================================
     *
     * For a popped value nums[index]:
     *
     *     leftChoices  = index - left
     *     rightChoices = right - index
     *
     * Number of subarrays where this value is the chosen minimum:
     *
     *     leftChoices * rightChoices
     *
     * Contribution:
     *
     *     nums[index]
     *     * leftChoices
     *     * rightChoices
     *
     * Duplicate tie-handling matters.
     */

    static class SumOfSubarrayMinimums {

        private static final long MOD = 1_000_000_007L;

        public int sumSubarrayMins(int[] arr) {

            long result = 0;
            Deque<Integer> stack = new ArrayDeque<>();

            for (int i = 0; i <= arr.length; i++) {

                int current =
                        (i == arr.length)
                        ? Integer.MIN_VALUE
                        : arr[i];

                while (!stack.isEmpty()
                        && current < arr[stack.peek()]) {

                    int index = stack.pop();

                    int left =
                            stack.isEmpty()
                            ? -1
                            : stack.peek();

                    int right = i;

                    long leftChoices = index - left;
                    long rightChoices = right - index;

                    long contribution =
                            (long) arr[index]
                            * leftChoices
                            * rightChoices;

                    result =
                            (result + contribution) % MOD;
                }

                if (i < arr.length) {
                    stack.push(i);
                }
            }

            return (int) result;
        }
    }

    /*
     * ============================================================
     * 🎯 INTERVIEW RECALL SHEET
     * ============================================================
     *
     * TRIGGER:
     *
     *     For each element, need nearest smaller/greater boundary
     *     or maximum span where it remains min/max.
     *
     * INVARIANT:
     *
     *     Stack stores unresolved indices in monotonic order.
     *
     * POP CONDITION:
     *
     *     Current value breaks that monotonic order.
     *
     * POP MEANING:
     *
     *     Current index resolves the popped element's right boundary.
     *
     * LEFT BOUNDARY:
     *
     *     New stack top after pop.
     *
     * WIDTH:
     *
     *     right - left - 1
     *
     * COMPLEXITY:
     *
     *     Each index pushed once, popped once -> O(n).
     *
     * COMMON BUGS:
     *
     *     wrong < / <= with duplicates
     *     wrong left default (-1)
     *     wrong width formula
     *     forgetting final flush
     *
     * ONE-LINER:
     *
     *     "Smaller current bar closes taller unresolved bars."
     */

    /*
     * ============================================================
     * 🧪 SELF-VERIFYING TESTS
     * ============================================================
     */

    private static void assertEquals(int expected,
                                     int actual,
                                     String reason) {

        if (expected != actual) {
            throw new AssertionError(
                    reason +
                    "\nExpected: " + expected +
                    "\nActual:   " + actual
            );
        }
    }

    private static void assertSameAsBrute(int[] heights) {

        int brute =
                new BruteForceSolution()
                        .largestRectangleArea(heights);

        int optimal =
                new OptimalMonotonicStackSolution()
                        .largestRectangleArea(heights);

        if (brute != optimal) {
            throw new AssertionError(
                    "Brute/optimal mismatch"
            );
        }
    }

    public static void main(String[] args) {

        OptimalMonotonicStackSolution optimal =
                new OptimalMonotonicStackSolution();

        BoundaryArraysSolution boundaryArrays =
                new BoundaryArraysSolution();

        assertEquals(
                10,
                optimal.largestRectangleArea(
                        new int[]{2, 1, 5, 6, 2, 3}
                ),
                "Classic histogram"
        );

        assertEquals(
                4,
                optimal.largestRectangleArea(
                        new int[]{1, 2, 3}
                ),
                "Increasing histogram"
        );

        assertEquals(
                4,
                optimal.largestRectangleArea(
                        new int[]{3, 2, 1}
                ),
                "Decreasing histogram"
        );

        assertEquals(
                8,
                optimal.largestRectangleArea(
                        new int[]{2, 2, 2, 2}
                ),
                "Duplicate heights"
        );

        assertEquals(
                10,
                boundaryArrays.largestRectangleArea(
                        new int[]{2, 1, 5, 6, 2, 3}
                ),
                "Boundary-array solution"
        );

        assertEquals(
                6,
                new MaximalRectangleSolution()
                        .maximalRectangle(
                                new char[][]{
                                        {'1','0','1','0','0'},
                                        {'1','0','1','1','1'},
                                        {'1','1','1','1','1'},
                                        {'1','0','0','1','0'}
                                }
                        ),
                "Maximal Rectangle reuse"
        );

        assertEquals(
                17,
                new SumOfSubarrayMinimums()
                        .sumSubarrayMins(
                                new int[]{3, 1, 2, 4}
                        ),
                "Sum of Subarray Minimums"
        );

        int[][] regression = {
                {2, 1, 5, 6, 2, 3},
                {4},
                {1, 2, 3},
                {3, 2, 1},
                {2, 2, 2, 2},
                {2, 1, 2},
                {6, 2, 5, 4, 5, 1, 6},
                {0, 0, 0},
                {5, 4, 5}
        };

        for (int[] heights : regression) {
            assertSameAsBrute(heights);
        }

        System.out.println(
                "All LargestRectangleV3 tests passed."
        );
    }
}
