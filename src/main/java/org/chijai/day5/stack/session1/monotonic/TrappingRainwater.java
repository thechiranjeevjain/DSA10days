package org.chijai.day5.stack.session1.monotonic;

public class TrappingRainwater {

    /*
     * ============================================================================
     * 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
     * ============================================================================
     *
     * 🔗 Official LeetCode Link:
     * https://leetcode.com/problems/trapping-rain-water/
     *
     * 🧩 Difficulty:
     * Hard
     *
     * 🏷️ Tags:
     * Array, Two Pointers, Stack, Dynamic Programming, Monotonic Stack
     *
     * ----------------------------------------------------------------------------
     * Problem Statement:
     *
     * Given n non-negative integers representing an elevation map where the width
     * of each bar is 1, compute how much water it can trap after raining.
     *
     * ----------------------------------------------------------------------------
     * Example 1:
     *
     * Input:
     * height = [0,1,0,2,1,0,1,3,2,1,2,1]
     *
     * Output:
     * 6
     *
     * Explanation:
     * The above elevation map (black section) is represented by array
     * [0,1,0,2,1,0,1,3,2,1,2,1]. In this case, 6 units of rain water (blue section)
     * are being trapped.
     *
     * ----------------------------------------------------------------------------
     * Example 2:
     *
     * Input:
     * height = [4,2,0,3,2,5]
     *
     * Output:
     * 9
     *
     * ----------------------------------------------------------------------------
     * Constraints:
     *
     * n == height.length
     * 1 <= n <= 2 * 10^4
     * 0 <= height[i] <= 10^5
     *
     * ----------------------------------------------------------------------------
     * Notes:
     * - Water can only be trapped between bars.
     * - Each bar has width exactly 1.
     * - The elevation map is fixed; rain falls uniformly.
     *
     * ============================================================================
     */


    /*
     * ============================================================================
     * 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST · FULL)
     * ============================================================================
     *
     * 🔵 Pattern Name:
     * Boundary-Constrained Accumulation (Two-Sided Maximum Invariant)
     *
     * 🔵 Problem Archetype:
     * For each position, compute a value constrained by the minimum of two
     * opposing boundaries while subtracting the local height.
     *
     * 🟢 Core Invariant (MANDATORY — ONE SENTENCE):
     * At every index i, the water trapped is determined solely by the minimum of
     * the maximum height to its left and the maximum height to its right.
     *
     * 🟡 Why This Invariant Makes the Pattern Work:
     * Water cannot exceed the shorter boundary; any taller boundary is irrelevant.
     * This invariant localizes a global-looking problem into independent columns.
     *
     * 🟢 When This Pattern Applies:
     * - Trapped water / bounded area problems
     * - Problems where constraints come from both directions
     * - Elevation / histogram / skyline interpretations
     *
     * 🧭 Pattern Recognition Signals:
     * - “How much can be trapped”
     * - “Between bars”
     * - Need for left and right context
     * - Independent per-index contribution
     *
     * 🟣 How This Pattern Differs from Similar Patterns:
     * - Unlike monotonic stack for span problems, here the invariant is symmetric.
     * - Unlike prefix-sum problems, accumulation depends on *future* maxima.
     * - Unlike greedy local rules, correctness comes from global boundaries.
     *
     * ============================================================================
     */


    /*
     * ============================================================================
     * 🟢 MENTAL MODEL & INVARIANTS (CANONICAL SOURCE OF TRUTH)
     * ============================================================================
     *
     * 🟢 Mental Model (THINKING, NOT CODE):
     * Imagine filling water column by column.
     * Each column can only hold water up to the height of the shorter wall
     * surrounding it from the left and the right.
     *
     * 🟢 State Representation:
     * - height[i]        → elevation at index i
     * - maxLeft[i]       → maximum height strictly to the left of i (inclusive)
     * - maxRight[i]      → maximum height strictly to the right of i (inclusive)
     * - waterAtI         → min(maxLeft[i], maxRight[i]) - height[i]
     *
     * 🟢 ALL Invariants (EXPLICIT):
     *
     * Invariant 1:
     * maxLeft[i] is the tallest bar from index 0 to i.
     *
     * Invariant 2:
     * maxRight[i] is the tallest bar from index i to n-1.
     *
     * Invariant 3:
     * If min(maxLeft[i], maxRight[i]) <= height[i], then waterAtI = 0.
     *
     * Invariant 4:
     * Water contribution at index i is independent of all other indices
     * once maxLeft and maxRight are known.
     *
     * 🟡 Allowed Moves (Preserve Invariant):
     * - Precompute prefix maximums
     * - Precompute suffix maximums
     * - Two-pointer traversal maintaining running maxes
     *
     * 🔴 Forbidden Moves (Break Correctness):
     * - Using nearest greater instead of maximum
     * - Using stack values instead of indices incorrectly
     * - Letting local comparisons override global maxima
     *
     * 🟡 Termination Logic:
     * - Finite array
     * - Each index contributes once
     * - No cyclic dependency
     *
     * 🔴 Why Common Alternatives Are Inferior:
     * - Local greedy fails because water depends on *farthest* boundary
     * - Stack misuse often computes spans, not volume
     *
     * ============================================================================
     */


    /*
     * ============================================================================
     * 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
     * ============================================================================
     *
     * 🔴 Typical Wrong Approach #1:
     * Using nearest greater element on left and right.
     *
     * Why It Seems Correct:
     * - Intuition says “water is trapped by nearest walls”.
     *
     * Why It Fails:
     * - Water does NOT care about nearest wall.
     * - It cares about the TALLEST wall on each side.
     *
     * Exact Invariant Violated:
     * Violates Invariant 1 & 2 — uses local boundary instead of global maximum.
     *
     * Minimal Counterexample:
     * height = [5, 0, 1, 0, 2]
     *
     * Nearest greater on right of index 1 is 1,
     * but actual right boundary is 5 (farther).
     *
     * 🔴 Typical Wrong Approach #2:
     * Stack storing heights instead of indices.
     *
     * Why It Seems Correct:
     * - Monotonic stack feels “advanced”.
     *
     * Why It Fails:
     * - Volume depends on distance (width), not just height.
     * - Height-only stacks lose positional information.
     *
     * 🔴 Interviewer Trap:
     * They let you pass small cases,
     * then give a valley with distant tall walls.
     *
     * 🔴 Your Failing Solution (POST-MORTEM):
     *
     * - You used Stack<Integer> of heights.
     * - You attempted to compute “leftGreater” and “rightGreater”.
     * - You popped based on <= comparison.
     *
     * Fatal Issues:
     * ❌ You stored heights, not indices.
     * ❌ You computed nearest smaller/greater, not maximum.
     * ❌ You allowed popping to overwrite boundaries.
     * ❌ You assumed min(leftGreater, rightGreater) always valid.
     *
     * Result:
     * The core invariant was never enforced.
     *
     * ============================================================================
     */



    /*
     * ============================================================================
     * 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES (DERIVED FROM INVARIANT)
     * ============================================================================
     *
     * We derive every solution strictly from the invariant:
     *
     * 🟢 waterAtI = min(maxLeft[i], maxRight[i]) - height[i]
     *
     * The difference between solutions is HOW we maintain maxLeft and maxRight,
     * not WHAT they mean.
     *
     * ============================================================================
     */


    /*
     * ============================================================================
     * 🔹 BRUTE FORCE SOLUTION
     * ============================================================================
     *
     * 🔵 Core Idea:
     * For each index i, scan left to find maxLeft,
     * scan right to find maxRight.
     *
     * 🟢 Invariant Enforced:
     * Fully correct — directly computes the invariant per index.
     *
     * 🔴 Limitation:
     * Recomputes the same maxima repeatedly.
     *
     * ⏱️ Time Complexity:
     * O(n^2)
     *
     * 🧠 Space Complexity:
     * O(1)
     *
     * 🟣 Interview Preference:
     * ❌ Only acceptable as a baseline explanation.
     *
     * ============================================================================
     */
    static class BruteForceSolution {

        public int trap(int[] height) {
            int n = height.length;
            int totalWater = 0;

            for (int i = 0; i < n; i++) {

                int maxLeft = 0;
                for (int left = 0; left <= i; left++) {
                    maxLeft = Math.max(maxLeft, height[left]);
                }

                int maxRight = 0;
                for (int right = i; right < n; right++) {
                    maxRight = Math.max(maxRight, height[right]);
                }

                int boundedHeight = Math.min(maxLeft, maxRight);

                if (boundedHeight > height[i]) {
                    totalWater += boundedHeight - height[i];
                }
            }

            return totalWater;
        }
    }


    /*
     * ============================================================================
     * 🔹 IMPROVED SOLUTION (PREFIX + SUFFIX MAX ARRAYS)
     * ============================================================================
     *
     * 🔵 Core Idea:
     * Precompute maxLeft[] and maxRight[] once.
     *
     * 🟢 Invariant Enforced:
     * Explicitly stored for every index.
     *
     * 🟡 What It Fixes:
     * Eliminates redundant scans.
     *
     * ⏱️ Time Complexity:
     * O(n)
     *
     * 🧠 Space Complexity:
     * O(n)
     *
     * 🟣 Interview Preference:
     * ✅ Very acceptable if space is allowed.
     *
     * ============================================================================
     */
    static class PrefixSuffixSolution {

        public int trap(int[] height) {
            int n = height.length;

            if (n == 0) return 0;

            int[] maxLeft = new int[n];
            int[] maxRight = new int[n];

            // maxLeft[i] = tallest bar from 0 to i
            maxLeft[0] = height[0];
            for (int i = 1; i < n; i++) {
                maxLeft[i] = Math.max(maxLeft[i - 1], height[i]);
            }

            // maxRight[i] = tallest bar from i to n-1
            maxRight[n - 1] = height[n - 1];
            for (int i = n - 2; i >= 0; i--) {
                maxRight[i] = Math.max(maxRight[i + 1], height[i]);
            }

            int totalWater = 0;

            for (int i = 0; i < n; i++) {
                int boundedHeight = Math.min(maxLeft[i], maxRight[i]);
                if (boundedHeight > height[i]) {
                    totalWater += boundedHeight - height[i];
                }
            }

            return totalWater;
        }
    }

    /*
     * ============================================================================
     * 🔹 MONOTONIC STACK SOLUTION (VALLEY-CLOSURE · ALTERNATIVE)
     * ============================================================================
     *
     * 🔵 Core Idea:
     * Water is trapped when a right boundary closes a valley formed earlier.
     *
     * 🟢 Stack Invariant (NON-NEGOTIABLE):
     * The stack stores INDICES of bars in strictly decreasing height order.
     *
     * 🟢 What the Stack Represents:
     * A sequence of potential left boundaries waiting for a right boundary.
     *
     * 🟡 When Water Is Computed:
     * Only when a valley bottom is popped AND a left boundary exists.
     *
     * 🔴 What This Fixes:
     * Correctly accounts for width (distance between boundaries).
     *
     * ⏱️ Time Complexity:
     * O(n) — each index pushed and popped once
     *
     * 🧠 Space Complexity:
     * O(n)
     *
     * 🟣 Interview Preference:
     * ⚠️ Acceptable but harder to explain than two-pointer
     *
     * ============================================================================
     */
    static class MonotonicStackSolution {

        public int trap(int[] height) {

            int n = height.length;
            int totalWater = 0;

            // Stack stores INDICES, not heights
            java.util.Stack<Integer> stack = new java.util.Stack<>();

            for (int current = 0; current < n; current++) {

                // Close valleys while current bar is taller
                while (!stack.isEmpty() && height[current] > height[stack.peek()]) {

                    int valleyIndex = stack.pop(); // bottom of the valley

                    // No left boundary → cannot trap water
                    if (stack.isEmpty()) {
                        break;
                    }

                    int leftBoundaryIndex = stack.peek();

                    int width = current - leftBoundaryIndex - 1;

                    int boundedHeight =
                            Math.min(height[leftBoundaryIndex], height[current])
                                    - height[valleyIndex];

                    if (boundedHeight > 0) {
                        totalWater += width * boundedHeight;
                    }
                }

                stack.push(current);
            }

            return totalWater;
        }
    }


    /*
     * ============================================================================
     * 🔹 OPTIMAL SOLUTION (TWO POINTER · INTERVIEW-PREFERRED)
     * ============================================================================
     *
     * 🔵 Core Idea:
     * Use two pointers and maintain running maxLeft and maxRight.
     *
     * 🟢 Core Invariant (Maintained Dynamically):
     * At any step, the side with the smaller boundary determines water.
     *
     * 🟡 Why This Works:
     * If maxLeft < maxRight,
     * then water is limited by maxLeft regardless of unseen right side.
     *
     * ⏱️ Time Complexity:
     * O(n)
     *
     * 🧠 Space Complexity:
     * O(1)
     *
     * 🟣 Interview Preference:
     * ⭐⭐ GOLD STANDARD ⭐⭐
     *
     * ============================================================================
     */
    static class TwoPointerOptimalSolution {

        public int trap(int[] height) {

            int left = 0;
            int right = height.length - 1;

            int maxLeft = 0;
            int maxRight = 0;

            int totalWater = 0;

            while (left <= right) {

                if (height[left] <= height[right]) {
                    // Left side is the limiting boundary
                    if (height[left] >= maxLeft) {
                        maxLeft = height[left]; // update boundary
                    } else {
                        // bounded by maxLeft invariant
                        totalWater += maxLeft - height[left];
                    }
                    left++;
                } else {
                    // Right side is the limiting boundary
                    if (height[right] >= maxRight) {
                        maxRight = height[right]; // update boundary
                    } else {
                        // bounded by maxRight invariant
                        totalWater += maxRight - height[right];
                    }
                    right--;
                }
            }

            return totalWater;
        }
    }


    /*
     * ============================================================================
     * 7️⃣ 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
     * ============================================================================
     *
     * 🟣 State the Invariant:
     * For any index i, trapped water equals
     * min(max height to the left, max height to the right) minus height[i].
     *
     * 🟣 Discard / Transition Logic:
     * In the two-pointer approach, we always advance the pointer with the smaller
     * current boundary because the smaller boundary fully determines the water
     * that can be trapped on that side.
     *
     * 🟣 Why Correctness Is Guaranteed:
     * When maxLeft <= maxRight, the right side cannot reduce the bound below
     * maxLeft, so future right elements are irrelevant for current left.
     *
     * 🟣 What Breaks If Logic Changes:
     * - Advancing the larger side first violates the invariant.
     * - Using nearest greater instead of maximum violates global boundary logic.
     *
     * 🟣 In-place Feasibility:
     * Yes — O(1) space with two pointers.
     *
     * 🟣 Streaming Feasibility:
     * No — future right boundary is required unless approximated.
     *
     * 🟣 When NOT to Use This Pattern:
     * - When boundaries are not monotonic or comparable.
     * - When accumulation depends on local neighbors only.
     *
     * ============================================================================
     */


    /*
     * ============================================================================
     * 8️⃣ 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
     * ============================================================================
     *
     * 🟢 Invariant-Preserving Changes:
     * - Use long instead of int for very large heights.
     * - Replace array with list if random access is preserved.
     *
     * 🟡 Reasoning-Only Changes:
     * - Switching between prefix/suffix and two-pointer methods.
     * - Explaining via valley-filling analogy instead of math.
     *
     * 🔴 Pattern-Break Signals:
     * - Boundaries depend on distance or slope.
     * - Volume depends on more than two sides.
     *
     * 🟡 Why Invariant Still Holds or Collapses:
     * The invariant holds as long as water is constrained by two opposing maxima.
     *
     * ============================================================================
     */


    /*
     * ============================================================================
     * 9️⃣ ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS · SAME INVARIANT)
     * ============================================================================
     */

    /*
     * --------------------------------------------------------------------------
     * Reinforcement Problem 1: Container With Most Water
     * --------------------------------------------------------------------------
     *
     * Full Problem Statement:
     *
     * Given n non-negative integers a1, a2, ..., an, where each represents a point
     * at coordinate (i, ai). n vertical lines are drawn such that the two endpoints
     * of line i are at (i, ai) and (i, 0). Find two lines, which together with the
     * x-axis forms a container, such that the container contains the most water.
     *
     * Pattern Mapping:
     * Same invariant — volume limited by shorter boundary.
     *
     * Edge Cases & Traps:
     * - Moving taller boundary is useless.
     *
     * Interview Articulation:
     * Always move the pointer at the shorter line.
     */
    static class ContainerWithMostWater {

        public int maxArea(int[] height) {
            int left = 0, right = height.length - 1;
            int maxArea = 0;

            while (left < right) {
                int width = right - left;
                int boundedHeight = Math.min(height[left], height[right]);
                maxArea = Math.max(maxArea, width * boundedHeight);

                if (height[left] < height[right]) {
                    left++;
                } else {
                    right--;
                }
            }
            return maxArea;
        }
    }


    /*
     * --------------------------------------------------------------------------
     * Reinforcement Problem 2: Trapping Rain Water II (2D)
     * --------------------------------------------------------------------------
     *
     * Full Problem Statement:
     * Given an m x n matrix of non-negative integers representing an elevation map,
     * compute how much water it can trap after raining.
     *
     * Pattern Mapping:
     * Same invariant generalized to 2D using a min-heap.
     *
     * Interview Note:
     * Boundary-first expansion preserves the invariant.
     *
     * (Code omitted intentionally — requires priority queue and grid traversal;
     * conceptually same invariant but structurally heavier.)
     */


    /*
     * --------------------------------------------------------------------------
     * Reinforcement Problem 3: Largest Rectangle in Histogram
     * --------------------------------------------------------------------------
     *
     * Full Problem Statement:
     * Given an array of integers heights representing the histogram's bar height
     * where the width of each bar is 1, return the area of the largest rectangle.
     *
     * Pattern Mapping:
     * Invariant flips — nearest smaller boundaries instead of maximum.
     *
     * Interview Note:
     * This problem LOOKS similar but violates this chapter’s invariant.
     */


    /*
     * ============================================================================
     * 10️⃣ 🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
     * ============================================================================
     */

    /*
     * Related Problem: Pour Water
     *
     * Same invariant partially holds but local movement dominates.
     *
     * Key Insight:
     * Global maxima matter less than nearest slope.
     */


    /*
     * ============================================================================
     * 11️⃣ 🟢 LEARNING VERIFICATION (INVARIANT-FIRST)
     * ============================================================================
     *
     * 🟢 Invariant to Recall (Without Code):
     * Water at index i is limited by the shorter of the tallest bar on its left
     * and the tallest bar on its right.
     *
     * 🔴 Why Naive Approaches Fail:
     * - Nearest greater element ≠ tallest boundary
     * - Local reasoning ignores distant constraints
     *
     * 🔴 Bugs to Debug Intentionally:
     * - Replace max with nearest greater → wrong
     * - Advance larger pointer first → wrong
     * - Use heights instead of indices in stack → wrong
     *
     * 🧭 How to Detect This Invariant in Unseen Problems:
     * Ask:
     * “Is the answer at each position bounded by two opposing global constraints?”
     *
     * ============================================================================
     */


    /*
     * ============================================================================
     * 12️⃣ 🧪 main() METHOD + SELF-VERIFYING TESTS (MUST BE LAST)
     * ============================================================================
     *
     * These tests validate the INVARIANT, not just outputs.
     * Each test explains WHY it exists.
     *
     * ============================================================================
     */
    public static void main(String[] args) {

        TwoPointerOptimalSolution optimal = new TwoPointerOptimalSolution();
        PrefixSuffixSolution prefixSuffix = new PrefixSuffixSolution();
        BruteForceSolution brute = new BruteForceSolution();
        MonotonicStackSolution stackSol = new MonotonicStackSolution();


        // ------------------------------------------------------------
        // Happy path: official example
        // ------------------------------------------------------------
        int[] example1 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        assertEquals(6, optimal.trap(example1), "Example 1 - Optimal");
        assertEquals(6, prefixSuffix.trap(example1), "Example 1 - PrefixSuffix");
        assertEquals(6, brute.trap(example1), "Example 1 - Brute");
        assertEquals(6, stackSol.trap(example1), "Example 1 - Monotonic Stack");

        // ------------------------------------------------------------
        // Valley with distant tall walls (nearest-greater trap)
        // ------------------------------------------------------------
        int[] distantWalls = {5, 0, 1, 0, 2};
        assertEquals(7, optimal.trap(distantWalls), "Distant walls invariant test");

        // ------------------------------------------------------------
        // Monotonic increasing (no trapping)
        // ------------------------------------------------------------
        int[] increasing = {1, 2, 3, 4, 5};
        assertEquals(0, optimal.trap(increasing), "Increasing heights");

        // ------------------------------------------------------------
        // Monotonic decreasing (no trapping)
        // ------------------------------------------------------------
        int[] decreasing = {5, 4, 3, 2, 1};
        assertEquals(0, optimal.trap(decreasing), "Decreasing heights");

        // ------------------------------------------------------------
        // Flat surface
        // ------------------------------------------------------------
        int[] flat = {3, 3, 3, 3};
        assertEquals(0, optimal.trap(flat), "Flat surface");

        // ------------------------------------------------------------
        // Single bar (boundary case)
        // ------------------------------------------------------------
        int[] single = {5};
        assertEquals(0, optimal.trap(single), "Single bar");

        System.out.println("✅ All invariant-based tests passed.");
    }

    // Simple assertion helper (no external libs allowed)
    private static void assertEquals(int expected, int actual, String testName) {
        if (expected != actual) {
            throw new AssertionError(
                    "❌ Test failed [" + testName + "] — expected: "
                            + expected + ", actual: " + actual
            );
        }
    }


    /*
     * ============================================================================
     * 13️⃣ 🧠 CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
     * ============================================================================
     *
     * • Invariant → min(maxLeft, maxRight) bounds water
     * • Search target → water trapped at each index
     * • Discard rule → side with larger boundary is irrelevant
     * • Termination guarantee → pointers move inward, finite array
     * • Naive failure → nearest boundary ≠ tallest boundary
     * • Edge cases → monotonic, flat, single bar
     * • Variant readiness → container, 2D rainwater
     * • Pattern boundary → breaks when >2 constraints exist
     *
     * ============================================================================
     */


    /*
     * ============================================================================
     * 🧘 FINAL CLOSURE STATEMENT (PROBLEM-SPECIFIC)
     * ============================================================================
     *
     * For this problem, the invariant is that trapped water at any index is bounded
     * by the shorter of the tallest bars on its left and right.
     *
     * The answer represents the sum of all such bounded excess heights.
     *
     * The search terminates because each index is processed exactly once while
     * maintaining valid boundaries.
     *
     * I can re-derive this solution under pressure by stating the invariant first.
     *
     * This chapter is complete.
     *
     * 📌 Rule to prevent over-study:
     * If I can explain the invariant and the discard rule, I am done.
     *
     * ============================================================================
     */
}
