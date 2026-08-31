package org.chijai.day1.Arrays.session2;

import java.util.Arrays;

/**
 * Container With Most Water — V2
 *
 * Primary classification:
 *
 * Array
 * -> Two Pointers
 * -> Opposite-End Two Pointers
 * -> Greedy Search-Space Elimination
 *
 * Core motto:
 *
 * "Discard an endpoint only when you can prove it is exhausted forever."
 */
public class ContainerWithMostWater {

    /*
     * ============================================================
     * 📘 PROBLEM
     * ============================================================
     *
     * Given height[], choose two indices left < right.
     *
     * Area:
     *
     *     (right - left)
     *     *
     *     min(height[left], height[right])
     *
     * Return the maximum possible area.
     *
     * Example:
     *
     * [1,8,6,2,5,4,8,3,7]
     *
     * answer = 49
     *
     * Target:
     *
     * Time  : O(n)
     * Space : O(1)
     */

    /*
     * ============================================================
     * 🧠 PATTERN + MENTAL MODEL
     * ============================================================
     *
     * Pattern:
     *
     * OPPOSITE-END TWO POINTERS
     *
     * Archetype:
     *
     * SEARCH-SPACE ELIMINATION
     *
     * Start with:
     *
     * left  = 0
     * right = n - 1
     *
     * This gives the widest possible container.
     *
     * ------------------------------------------------------------
     * FORMULA
     * ------------------------------------------------------------
     *
     * area = width * limitingHeight
     *
     * width:
     *
     *     right - left
     *
     * limitingHeight:
     *
     *     min(height[left], height[right])
     *
     * ------------------------------------------------------------
     * KEY OBSERVATION
     * ------------------------------------------------------------
     *
     * Every pointer movement inward makes width SMALLER.
     *
     * Therefore, after moving inward, the only way area can improve
     * is if the limiting height becomes larger.
     *
     * The SHORTER wall is the limiting wall.
     *
     * So only replacing the shorter wall can possibly help.
     *
     * ------------------------------------------------------------
     * DISCARD RULE
     * ------------------------------------------------------------
     *
     * if height[left] <= height[right]
     *
     *     left++
     *
     * else
     *
     *     right--
     *
     * ------------------------------------------------------------
     * ONE-LINER
     * ------------------------------------------------------------
     *
     * "Width always decreases, so only increasing the limiting
     *  height can compensate."
     */

    /*
     * ============================================================
     * 🔬 WHY DISCARDING THE SHORTER WALL IS SAFE
     * ============================================================
     *
     * Suppose:
     *
     *     height[left] <= height[right]
     *
     * Current area:
     *
     *     (right - left) * height[left]
     *
     * Now consider ANY future pair that keeps this same left:
     *
     *     (left, right - 1)
     *     (left, right - 2)
     *     ...
     *
     * Every such pair has:
     *
     * 1. smaller width
     *
     * 2. limiting height <= height[left]
     *
     * Therefore:
     *
     * futureArea
     * <= smallerWidth * height[left]
     * <  currentWidth * height[left]
     *
     * So this left endpoint has already produced its largest
     * possible container.
     *
     * It is EXHAUSTED FOREVER.
     *
     * Therefore:
     *
     *     left++
     *
     * is mathematically safe.
     *
     * Symmetric proof applies when the right wall is shorter.
     */

    /*
     * ============================================================
     * 📈 APPROACH PROGRESSION
     * ============================================================
     *
     * 1. BRUTE FORCE
     *
     * Try every pair.
     *
     * Number of pairs:
     *
     *     C(n, 2)
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
     * 2. PROVE DOMINATED PAIRS CAN BE DISCARDED
     *
     * Once the shorter endpoint is evaluated against its widest
     * possible partner, every remaining pair using that endpoint
     * is worse.
     *
     * So one whole row/column of the pair search space disappears.
     *
     * ------------------------------------------------------------
     *
     * 3. OPPOSITE-END TWO POINTERS
     *
     * Evaluate current pair.
     *
     * Permanently discard the shorter endpoint.
     *
     * Exactly one endpoint disappears per iteration.
     *
     * Time:
     *
     *     O(n)
     *
     * Space:
     *
     *     O(1)
     */

    static final class BruteForce {

        static int maxArea(int[] height) {

            int best = 0;

            for (int left = 0; left < height.length - 1; left++) {

                for (int right = left + 1; right < height.length; right++) {

                    int area =
                            (right - left)
                            * Math.min(height[left], height[right]);

                    best = Math.max(best, area);
                }
            }

            return best;
        }
    }

    static final class Optimal {

        static int maxArea(int[] height) {

            if (height == null || height.length < 2) {
                return 0;
            }

            int left = 0;
            int right = height.length - 1;

            int best = 0;

            while (left < right) {

                int width = right - left;

                int limitingHeight =
                        Math.min(height[left], height[right]);

                int area = width * limitingHeight;

                best = Math.max(best, area);

                // Discard only the exhausted / limiting endpoint.
                if (height[left] <= height[right]) {
                    left++;
                } else {
                    right--;
                }
            }

            return best;
        }
    }

    /*
     * ============================================================
     * ⚠️ COMMON WRONG MOVES
     * ============================================================
     *
     * 1. Move the taller wall.
     *
     * Wrong because:
     *
     * width decreases
     * +
     * shorter wall still limits the height
     *
     * so improvement is impossible.
     *
     * ------------------------------------------------------------
     *
     * 2. Move both pointers.
     *
     * Wrong because:
     *
     * two endpoints are discarded without proving both are useless.
     *
     * ------------------------------------------------------------
     *
     * 3. Pick the two tallest walls.
     *
     * Wrong because:
     *
     * area depends on BOTH:
     *
     * height
     * and
     * distance.
     *
     * ------------------------------------------------------------
     *
     * 4. Sort the heights.
     *
     * Wrong because:
     *
     * sorting destroys original index distance.
     */

    /*
     * ============================================================
     * 🔍 WHY THIS IS NOT BINARY SEARCH
     * ============================================================
     *
     * It LOOKS similar:
     *
     * left
     * right
     * while (left < right)
     * discard something
     *
     * But the discard reason is different.
     *
     * Binary Search:
     *
     *     discard using sorted order / monotonic predicate
     *
     * Container With Most Water:
     *
     *     discard using a dominance proof from the objective formula
     *
     * Binary-search question:
     *
     *     "Which half cannot contain the answer?"
     *
     * Two-pointer question:
     *
     *     "Which endpoint has become useless forever?"
     */

    /*
     * ============================================================
     * 🧭 EXACT CLASSIFICATION
     * ============================================================
     *
     * arrays/
     *   twoPointers/
     *     oppositeEnds/
     *       ContainerWithMostWater.java
     *
     * Primary:
     *
     *     Two Pointers
     *
     * Subtype:
     *
     *     Opposite-End Two Pointers
     *
     * Reasoning:
     *
     *     Greedy / Search-Space Elimination
     *
     * NOT:
     *
     *     Stack
     *     Monotonic Stack
     *     Sliding Window
     *     Binary Search
     */

    /*
     * ============================================================
     * 🔗 RELATED PROBLEMS
     * ============================================================
     *
     * SAME OPPOSITE-END FAMILY
     * ------------------------
     *
     * Two Sum II
     *
     *     sum too small -> left++
     *     sum too large -> right--
     *
     * 3Sum
     *
     *     sort once, then use opposite-end two pointers
     *     inside each fixed first element.
     *
     * Boats to Save People
     *
     *     heaviest person creates the greedy endpoint decision.
     *
     * Trapping Rain Water — two-pointer version
     *
     *     process the side whose boundary is already determined.
     *
     * ------------------------------------------------------------
     * LOOKS SIMILAR, DIFFERENT PATTERN
     * ------------------------------------------------------------
     *
     * Largest Rectangle in Histogram
     *
     *     Monotonic Stack
     *
     * Why?
     *
     *     Need nearest smaller boundaries for MANY unresolved bars.
     *
     * Daily Temperatures
     *
     *     Monotonic Stack
     *
     * Why?
     *
     *     Need to remember MANY unresolved earlier indices.
     *
     * ------------------------------------------------------------
     * SEPARATOR
     * ------------------------------------------------------------
     *
     * Can one endpoint be permanently discarded with proof?
     *
     *     -> Opposite-End Two Pointers
     *
     * Need to remember many unresolved previous items?
     *
     *     -> Monotonic Stack
     */

    /*
     * ============================================================
     * 🔄 USEFUL VARIATIONS
     * ============================================================
     *
     * Return indices instead of area:
     *
     * store bestLeft / bestRight whenever area improves.
     *
     * ------------------------------------------------------------
     *
     * Equal heights:
     *
     * either endpoint may be discarded.
     *
     * ------------------------------------------------------------
     *
     * Skip dominated heights:
     *
     * after discarding a wall of height h,
     * you may skip inward walls with height <= h.
     *
     * Same proof:
     *
     * smaller width + no better limiting height.
     *
     * Complexity remains O(n).
     *
     * ------------------------------------------------------------
     *
     * Count ALL containers:
     *
     * this pattern no longer works.
     *
     * The discard proof preserves the optimum,
     * not complete enumeration.
     */

    /*
     * ============================================================
     * 🎯 INTERVIEW RECALL
     * ============================================================
     *
     * Trigger:
     *
     * opposite ends
     * +
     * score depends on both boundaries
     * +
     * one endpoint can be proved useless forever
     *
     * ------------------------------------------------------------
     *
     * Formula:
     *
     * area = width * min(leftHeight, rightHeight)
     *
     * ------------------------------------------------------------
     *
     * Invariant:
     *
     * every discarded endpoint has already produced its best
     * possible container.
     *
     * ------------------------------------------------------------
     *
     * Discard rule:
     *
     * move the shorter wall.
     *
     * ------------------------------------------------------------
     *
     * Re-derive:
     *
     * width only decreases
     * -> improvement needs greater limiting height
     * -> shorter wall is the limiter
     * -> discard shorter wall
     *
     * ------------------------------------------------------------
     *
     * Interview proof:
     *
     * "If left is shorter, every future pair using left has
     *  smaller width and limiting height no greater than left.
     *  Therefore none can beat the current pair using left,
     *  so left is safe to discard."
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

    private static void assertMatchesBruteForce(int[] height) {

        int brute = BruteForce.maxArea(height);
        int optimal = Optimal.maxArea(height);

        if (brute != optimal) {
            throw new AssertionError(
                    "Mismatch for " + Arrays.toString(height) +
                    "\nBrute:   " + brute +
                    "\nOptimal: " + optimal
            );
        }
    }

    public static void main(String[] args) {

        assertEquals(
                49,
                Optimal.maxArea(
                        new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}
                ),
                "Classic example failed"
        );

        assertEquals(
                1,
                Optimal.maxArea(new int[]{1, 1}),
                "Minimum input failed"
        );

        assertEquals(
                6,
                Optimal.maxArea(new int[]{1, 2, 3, 4, 5}),
                "Increasing heights failed"
        );

        assertEquals(
                6,
                Optimal.maxArea(new int[]{5, 4, 3, 2, 1}),
                "Decreasing heights failed"
        );

        assertEquals(
                400,
                Optimal.maxArea(new int[]{100, 1, 1, 1, 100}),
                "Tall endpoints failed"
        );

        assertEquals(
                16,
                Optimal.maxArea(new int[]{4, 4, 4, 4, 4}),
                "Equal heights failed"
        );

        int[][] regression = {
                {1, 8, 6, 2, 5, 4, 8, 3, 7},
                {1, 1},
                {5, 5},
                {1, 2, 3, 4, 5},
                {5, 4, 3, 2, 1},
                {2, 3, 10, 5, 7, 8, 9},
                {100, 1, 1, 1, 100},
                {4, 4, 4, 4, 4},
                {0, 2, 0, 4, 0},
                {9, 1, 2, 3, 9},
                {2, 4, 2, 4, 2},
                {6, 9, 3, 4, 5, 8}
        };

        for (int[] test : regression) {
            assertMatchesBruteForce(test);
        }

        System.out.println(
                "All ContainerWithMostWaterV2 assertions passed."
        );
    }
}
