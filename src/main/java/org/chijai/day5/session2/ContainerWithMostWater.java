package org.chijai.day5.session2;

import java.util.Arrays;

public class ContainerWithMostWater {

    /*
     * ============================================================
     * 2. 📘 PRIMARY PROBLEM
     * ============================================================
     *
     * Title:
     * Container With Most Water
     *
     * Difficulty:
     * Medium (Interview Tricky)
     *
     * Tags:
     * Array
     * Two Pointers
     * Greedy
     * Geometry
     *
     * Problem Description
     * -------------------
     * You are given an integer array height where height[i] represents the
     * height of a vertical line drawn at index i.
     *
     * Two different lines together with the x-axis form a container.
     *
     * Water held by two lines:
     *
     * Area =
     *      width × smaller height
     *
     *      = (right - left)
     *        × min(height[left], height[right])
     *
     * Return the maximum possible water that can be contained.
     *
     * Constraints
     * -----------
     * 2 <= height.length <= 100000
     * 0 <= height[i] <= 10000
     *
     * Representative Example 1
     * ------------------------
     * Input:
     * [1,8,6,2,5,4,8,3,7]
     *
     * Output:
     * 49
     *
     * Explanation:
     *
     * width = 8 - 1 = 7
     * height = min(8,7)=7
     *
     * area = 7 × 7 = 49
     *
     * Representative Example 2
     * ------------------------
     * Input:
     * [1,1]
     *
     * Output:
     * 1
     *
     * Official Formula
     * ----------------
     *
     * Area(i,j)
     * =
     * (j-i) × min(height[i],height[j])
     *
     * Official LeetCode
     * -----------------
     * https://leetcode.com/problems/container-with-most-water/
     */

    /*
     * ============================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern
     * -------
     * Opposite-End Two Pointers
     *
     * Archetype
     * ---------
     * Search Space Elimination
     *
     * Core Invariant
     * --------------
     * Once the smaller wall has been evaluated with the widest possible
     * partner, that wall can never participate in a better answer.
     *
     * Therefore it can be safely discarded.
     *
     * Why It Works
     * ------------
     * Area depends on:
     *
     * width × limiting height
     *
     * Every pointer movement decreases width forever.
     *
     * Therefore after reducing width, the only possible compensation is
     * increasing the limiting height.
     *
     * The shorter wall prevents that.
     *
     * Recognition Signals
     * -------------------
     * ✓ answer uses two indices
     * ✓ width naturally shrinks
     * ✓ score depends on both ends
     * ✓ moving inward is mandatory
     * ✓ one side provably becomes useless
     *
     * When To Use
     * -----------
     * • opposite ends
     * • monotonic elimination
     * • every movement permanently removes candidates
     *
     * When NOT To Use
     * ---------------
     * • no discard proof
     * • both pointers may still be useful
     * • future decisions require revisiting removed indices
     *
     * Comparison
     * ----------
     *
     * Sliding Window
     *     window expands/contracts
     *
     * Binary Search
     *     eliminate half by ordering
     *
     * Two Pointers (this problem)
     *     eliminate one endpoint using invariant
     */

    /*
     * ============================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * Mental Model
     * ------------
     * Imagine stretching a rope between two pillars.
     *
     * Width is fixed by distance.
     *
     * Water level is limited by the shorter pillar.
     *
     * Increasing the taller pillar changes nothing.
     *
     * Only replacing the shorter pillar with a taller one can possibly
     * increase the area after width shrinks.
     *
     * ------------------------------------------------------------
     * Primary Invariant
     * ------------------------------------------------------------
     *
     * The answer always remains inside the current search space.
     *
     * Whenever we discard one endpoint,
     * every pair containing that endpoint has already been proven unable
     * to beat the current or future optimum.
     *
     * ------------------------------------------------------------
     * Variable Meanings
     * ------------------------------------------------------------
     *
     * left
     *     left boundary of remaining search space
     *
     * right
     *     right boundary of remaining search space
     *
     * width
     *     current container width
     *
     * limitingHeight
     *     shorter boundary
     *
     * area
     *     candidate answer
     *
     * best
     *     largest area discovered
     *
     * ------------------------------------------------------------
     * Allowed Moves
     * ------------------------------------------------------------
     *
     * If left wall is shorter:
     *
     * move left
     *
     * If right wall is shorter:
     *
     * move right
     *
     * If equal:
     *
     * either movement preserves correctness.
     *
     * ------------------------------------------------------------
     * Forbidden Move
     * ------------------------------------------------------------
     *
     * Never move the taller wall while keeping the shorter wall.
     *
     * Why?
     *
     * Width decreases.
     *
     * Limiting height stays unchanged.
     *
     * Therefore
     *
     * newArea
     * <=
     * smallerHeight × smallerWidth
     *
     * <
     * smallerHeight × currentWidth
     *
     * So improvement is impossible.
     *
     * ------------------------------------------------------------
     * Correctness Intuition
     * ------------------------------------------------------------
     *
     * Assume:
     *
     * left <= rightHeight
     *
     * Current area:
     *
     * width × leftHeight
     *
     * Every future pair using this same left index has:
     *
     * smaller width
     *
     * limiting height
     * <= leftHeight
     *
     * Therefore every future area involving this left index is no larger.
     *
     * Thus this left index is exhausted forever.
     *
     * Remove it.
     *
     * Symmetric proof applies to the right side.
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * Every iteration removes exactly one endpoint.
     *
     * Search space shrinks monotonically.
     *
     * Eventually
     *
     * left == right
     *
     * No container remains.
     *
     * ------------------------------------------------------------
     * Why Naive Solutions Fail
     * ------------------------------------------------------------
     *
     * The optimal answer is determined jointly by:
     *
     * width
     *
     * and
     *
     * limiting height.
     *
     * Maximizing only width fails.
     *
     * Maximizing only height fails.
     *
     * Sorting destroys index distance.
     *
     * Dynamic Programming has no reusable overlapping subproblem.
     */

    /*
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * Mistake 1
     * ---------
     * Move taller pointer.
     *
     * Why it seems correct
     * --------------------
     * Try to find even taller wall.
     *
     * Violated Invariant
     * ------------------
     * Limiting wall never changed.
     *
     * Width only decreased.
     *
     * Counterexample
     * --------------
     * [1,8,6,2,5]
     *
     * Moving 8 instead of 1 permanently skips optimal candidates.
     *
     * ------------------------------------------------------------
     * Mistake 2
     * ------------------------------------------------------------
     * Always move both pointers.
     *
     * Violated Invariant
     * ------------------
     * Two candidates disappear simultaneously without proof.
     *
     * ------------------------------------------------------------
     * Mistake 3
     * ------------------------------------------------------------
     * Choose tallest two lines.
     *
     * Counterexample
     *
     * Heights:
     *
     * [100,1,100]
     *
     * works.
     *
     * But
     *
     * [100,99,98,100]
     *
     * distance also matters.
     *
     * Height alone is insufficient.
     *
     * ------------------------------------------------------------
     * Mistake 4
     * ------------------------------------------------------------
     * Sort heights.
     *
     * Violated Invariant
     * ------------------
     * Original distance disappears.
     *
     * Width is part of the objective function.
     */

    /*
     * ============================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Typing Order
     * ------------
     *
     * 1.
     * int left = 0;
     *
     * 2.
     * int right = n-1;
     *
     * 3.
     * int best = 0;
     *
     * 4.
     * while(left < right)
     *
     * 5.
     * width
     *
     * 6.
     * limiting height
     *
     * 7.
     * area
     *
     * 8.
     * update best
     *
     * 9.
     * move smaller pointer
     *
     * 10.
     * return best
     *
     * Function Skeleton
     * -----------------
     *
     * initialize
     *
     * while(search space exists)
     *      compute candidate
     *      update answer
     *      discard shorter wall
     *
     * return answer
     *
     * Transition
     * ----------
     *
     * width
     * →
     * right-left
     *
     * limitingHeight
     * →
     * min(leftHeight,rightHeight)
     *
     * area
     * →
     * width × limitingHeight
     *
     * Branch
     * ------
     *
     * left shorter
     * →
     * left++
     *
     * otherwise
     * →
     * right--
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * left ← 0
     * right ← n-1
     * best ← 0
     *
     * while left < right
     *      area
     *      update best
     *      discard shorter wall
     *
     * return best
     */

    /*
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    static final class BruteForce {

        /*
         * Idea
         * ----
         * Enumerate every possible pair.
         *
         * Invariant
         * ---------
         * best equals the largest area among all pairs examined so far.
         *
         * Limitation
         * ----------
         * Every pair is evaluated.
         *
         * Complexity
         * ----------
         * Time  : O(n²)
         * Space : O(1)
         *
         * Interview Usefulness
         * --------------------
         * Excellent baseline before deriving the discard rule.
         */
        static int maxArea(int[] height) {

            int best = 0;

            for (int left = 0; left < height.length - 1; left++) {

                for (int right = left + 1; right < height.length; right++) {

                    int area = (right - left) * Math.min(height[left], height[right]);

                    best = Math.max(best, area);
                }
            }

            return best;
        }
    }
    static final class Improved {

        /*
         * Idea
         * ----
         * There is no genuine asymptotic improvement between the brute-force
         * solution and the optimal two-pointer solution.
         *
         * This section presents the proof that bridges them.
         *
         * We start from the brute-force state space and ask:
         *
         * "Which states are mathematically impossible to become optimal?"
         *
         * Once that proof exists, exhaustive enumeration is no longer needed.
         *
         * ------------------------------------------------------------
         * State
         * ------------------------------------------------------------
         *
         * (left, right)
         *
         * represents exactly one container.
         *
         * ------------------------------------------------------------
         * Search Space
         * ------------------------------------------------------------
         *
         * Initially:
         *
         * all C(n,2) pairs.
         *
         * ------------------------------------------------------------
         * Key Observation
         * ------------------------------------------------------------
         *
         * Suppose
         *
         * height[left] <= height[right]
         *
         * Current area:
         *
         * A
         * =
         * (right-left)
         * ×
         * height[left]
         *
         * Consider ANY future pair using the SAME left index.
         *
         * Example:
         *
         * (left,right-1)
         * (left,right-2)
         * ...
         * (left,left+1)
         *
         * Every one of them has:
         *
         * • smaller width
         *
         * and
         *
         * • limiting height
         *   ≤ height[left]
         *
         * Therefore:
         *
         * futureArea
         * ≤
         * smallerWidth × height[left]
         *
         * <
         * currentWidth × height[left]
         *
         * =
         * currentArea
         *
         * Thus:
         *
         * Every pair using this left endpoint has already been dominated.
         *
         * The entire column of the search space disappears.
         *
         * ------------------------------------------------------------
         * Symmetry
         * ------------------------------------------------------------
         *
         * If
         *
         * height[right] < height[left]
         *
         * then the entire row containing right is discarded instead.
         *
         * ------------------------------------------------------------
         * Improvement
         * ------------------------------------------------------------
         *
         * Instead of examining
         *
         * O(n²)
         *
         * states,
         *
         * every iteration permanently deletes one endpoint.
         *
         * Only
         *
         * n−1
         *
         * iterations remain.
         *
         * ------------------------------------------------------------
         * Complexity
         * ------------------------------------------------------------
         *
         * Time
         * O(n)
         *
         * Space
         * O(1)
         *
         * ------------------------------------------------------------
         * Interview Usefulness
         * ------------------------------------------------------------
         *
         * This proof is usually more valuable than simply memorizing
         * "move the smaller pointer."
         *
         * Most interviewers ask:
         *
         * "Why is moving the smaller pointer correct?"
         *
         * This elimination proof is the expected answer.
         */

        static int proofOnlyReference(int[] height) {

            return Optimal.maxArea(height);
        }
    }

    static final class Optimal {

        /*
         * Idea
         * ----
         * Start from the widest container.
         *
         * Each iteration evaluates the current widest remaining container.
         *
         * Then permanently discard the limiting wall.
         *
         * ------------------------------------------------------------
         * Invariant
         * ------------------------------------------------------------
         *
         * Every discarded endpoint has already formed its largest possible
         * container.
         *
         * Therefore no future optimal solution can contain it.
         *
         * ------------------------------------------------------------
         * Correctness
         * ------------------------------------------------------------
         *
         * Width always decreases.
         *
         * Therefore improvement is only possible if the limiting height
         * increases.
         *
         * Hence only the shorter wall deserves replacement.
         *
         * ------------------------------------------------------------
         * Complexity
         * ------------------------------------------------------------
         *
         * Time
         * O(n)
         *
         * Space
         * O(1)
         *
         * ------------------------------------------------------------
         * Interview Usefulness
         * ------------------------------------------------------------
         *
         * Canonical opposite-end two-pointer problem.
         *
         * Mastering this discard proof transfers directly to many
         * elimination-style interview questions.
         */

        static int maxArea(int[] height) {

            if (height == null || height.length < 2) {
                return 0;
            }

            int left = 0;
            int right = height.length - 1;

            int best = 0;

            while (left < right) {

                // Invariant:
                // The optimal answer is still inside the current range.

                int width = right - left;

                // Water level is controlled by the shorter wall.
                int limitingHeight = Math.min(height[left], height[right]);

                int area = width * limitingHeight;

                if (area > best) {
                    best = area;
                }

                if (height[left] <= height[right]) {

                    // Discard Rule:
                    // This left endpoint has already produced its
                    // maximum possible area.
                    left++;

                } else {

                    // Symmetric discard rule for the right endpoint.
                    right--;
                }
            }

            return best;
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Q.
 * Explain the invariant.
 *
 * A.
 *
 * At every iteration the remaining search space still contains the
 * optimal answer because only endpoints that are mathematically
 * incapable of producing a better solution have been removed.
 *
 * ------------------------------------------------------------
 * Q.
 * Why discard the shorter wall?
 *
 * A.
 *
 * Area equals:
 *
 * width × limiting height.
 *
 * Moving inward always decreases width.
 *
 * Therefore improvement requires increasing the limiting height.
 *
 * The taller wall is already not limiting.
 *
 * Replacing it cannot improve the limiting height.
 *
 * Only replacing the shorter wall can possibly compensate for the
 * reduced width.
 *
 * ------------------------------------------------------------
 * Q.
 * Why is the discard mathematically safe?
 *
 * A.
 *
 * Suppose the left wall is shorter.
 *
 * Every remaining pair using that same left index has:
 *
 * • strictly smaller width
 *
 * • limiting height no greater than the current one
 *
 * Hence every such area is bounded above by the area already computed.
 *
 * That endpoint is exhausted forever.
 *
 * ------------------------------------------------------------
 * Q.
 * Why does the algorithm terminate?
 *
 * A.
 *
 * Every iteration removes exactly one endpoint.
 *
 * Therefore the search space shrinks monotonically until
 *
 * left == right.
 *
 * ------------------------------------------------------------
 * Q.
 * Can this be performed in-place?
 *
 * A.
 *
 * Yes.
 *
 * Only two indices and one answer variable are maintained.
 *
 * ------------------------------------------------------------
 * Q.
 * Is this streaming friendly?
 *
 * A.
 *
 * No.
 *
 * The right endpoint is required before reasoning about the left.
 *
 * A single left-to-right stream cannot reproduce the elimination proof.
 *
 * ------------------------------------------------------------
 * Q.
 * When should this pattern NOT be used?
 *
 * A.
 *
 * Whenever removing one endpoint cannot be justified by a formal
 * discard proof.
 *
 * Without such a proof,
 * opposite-end two pointers become a heuristic rather than a correct
 * algorithm.
 */

/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 * Opposite ends.
 * Maximize width × boundary property.
 *
 * Invariant
 * ---------
 * Discarded endpoints can never belong to a future optimum.
 *
 * Search Target
 * -------------
 * Maximum area.
 *
 * Discard Rule
 * ------------
 * Move the shorter wall.
 *
 * Common Trap
 * -----------
 * Moving the taller wall.
 *
 * Edge Cases
 * ----------
 * • two elements
 * • equal heights
 * • increasing heights
 * • decreasing heights
 * • repeated maximums
 *
 * One-Liner
 * ---------
 * Width always decreases, therefore only increasing the limiting
 * height can compensate.
 *
 * Re-Derivation Cue
 * -----------------
 * Ask:
 *
 * "Which endpoint has already produced its widest possible container?"
 */
/*
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * ------------------------------------------------------------
 * Variation 1
 * ------------------------------------------------------------
 * Equal Heights
 *
 * if (height[left] == height[right])
 *
 * Either pointer may move.
 *
 * Why?
 *
 * Both walls have identical limiting height.
 *
 * Discarding either preserves the invariant because neither endpoint
 * can participate in a better container with a smaller width.
 *
 * ------------------------------------------------------------
 * Variation 2
 * ------------------------------------------------------------
 * Skip Dominated Heights
 *
 * Example:
 *
 * while (left < right &&
 *        height[left] <= previousHeight)
 *      left++;
 *
 * Similar optimization on the right side.
 *
 * Why It Still Works
 * ------------------
 * Every skipped wall is no taller than the wall already discarded.
 *
 * Width is also smaller.
 *
 * Therefore no skipped wall can improve the answer.
 *
 * This optimization preserves the same discard proof.
 *
 * Complexity
 * ----------
 * Still O(n).
 *
 * ------------------------------------------------------------
 * Variation 3
 * ------------------------------------------------------------
 * Return Indices Instead of Area
 *
 * Store:
 *
 * bestLeft
 * bestRight
 *
 * whenever
 *
 * area > best
 *
 * Complexity remains unchanged.
 *
 * ------------------------------------------------------------
 * Variation 4
 * ------------------------------------------------------------
 * Count All Containers
 *
 * Pattern Break
 * -------------
 *
 * Two pointers are no longer sufficient.
 *
 * Why?
 *
 * The discard rule only proves optimality preservation.
 *
 * It does NOT preserve complete enumeration.
 *
 * ------------------------------------------------------------
 * Variation 5
 * ------------------------------------------------------------
 * Minimum Width Constraint
 *
 * Example:
 *
 * width >= K
 *
 * Continue eliminating endpoints while respecting the additional
 * stopping condition.
 *
 * The discard proof remains valid because width still decreases
 * monotonically.
 *
 * ------------------------------------------------------------
 * Variation 6
 * ------------------------------------------------------------
 * Maximum Perimeter Instead of Area
 *
 * Pattern Break.
 *
 * Area proof relies specifically on:
 *
 * width × limitingHeight.
 *
 * Perimeter obeys a different objective.
 *
 * The shorter-wall elimination argument no longer applies.
 *
 * ------------------------------------------------------------
 * Variation 7
 * ------------------------------------------------------------
 * Trapping Rain Water
 *
 * Similarity
 * ----------
 * Two pointers.
 *
 * Difference
 * ----------
 * State depends on prefix and suffix maxima rather than only the
 * current endpoints.
 *
 * Different invariant.
 *
 * ------------------------------------------------------------
 * Variation 8
 * ------------------------------------------------------------
 * Largest Rectangle in Histogram
 *
 * Looks similar because heights matter.
 *
 * Actually uses:
 *
 * Monotonic Stack.
 *
 * Reason
 * ------
 * Width is discovered by nearest smaller boundaries instead of
 * opposite-end elimination.
 */

/*
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * □ Can I state the invariant without looking?
 *
 * Yes.
 *
 * Every discarded endpoint has already formed its largest possible
 * container.
 *
 * ------------------------------------------------------------
 * □ What is the search target?
 *
 * Maximum
 *
 * width × limitingHeight.
 *
 * ------------------------------------------------------------
 * □ What is the discard rule?
 *
 * Remove only the shorter wall.
 *
 * ------------------------------------------------------------
 * □ Why is termination guaranteed?
 *
 * Exactly one endpoint disappears each iteration.
 *
 * ------------------------------------------------------------
 * □ Why does the naive solution fail?
 *
 * It examines every pair despite the existence of a formal
 * elimination proof.
 *
 * ------------------------------------------------------------
 * □ Which edge cases must I remember?
 *
 * • length = 2
 * • equal heights
 * • all equal
 * • strictly increasing
 * • strictly decreasing
 * • zeros
 *
 * ------------------------------------------------------------
 * □ Debugging Readiness
 *
 * If the answer is wrong, verify:
 *
 * 1.
 * width computed before pointer movement
 *
 * 2.
 * min() not max()
 *
 * 3.
 * pointer movement occurs after area calculation
 *
 * 4.
 * while(left < right)
 *
 * 5.
 * best updated before pointer movement
 *
 * ------------------------------------------------------------
 * □ Variant Readiness
 *
 * Can I explain why returning indices is easy but counting every
 * valid container is not?
 *
 * ------------------------------------------------------------
 * □ Pattern Boundary
 *
 * Never use opposite-end two pointers unless every pointer movement
 * is backed by a mathematical discard proof.
 */

/*
 * ============================================================
 * ⚫ PATTERN MAPPING
 * ============================================================
 *
 * Pattern
 * -------------------------
 * Opposite-End Two Pointers
 *
 * Core Invariant
 * -------------------------
 * Remove only an exhausted endpoint.
 *
 * Search Space
 * -------------------------
 * Current interval [left,right]
 *
 * State
 * -------------------------
 * (left,right)
 *
 * Transition
 * -------------------------
 * Move shorter endpoint.
 *
 * Discard Rule
 * -------------------------
 * Shorter wall can never produce a larger future area.
 *
 * Correctness
 * -------------------------
 * Every removed endpoint is permanently dominated.
 *
 * Termination
 * -------------------------
 * Search space shrinks until one endpoint remains.
 */

/*
 * ============================================================
 * 🔬 FORENSIC DEBUGGING GUIDE
 * ============================================================
 *
 * Symptom
 * -------
 * Area too small.
 *
 * Likely Cause
 * ------------
 * max() used instead of min().
 *
 * ------------------------------------------------------------
 * Symptom
 * -------
 * Misses optimal answer.
 *
 * Likely Cause
 * ------------
 * Pointer moved before computing area.
 *
 * ------------------------------------------------------------
 * Symptom
 * -------
 * Infinite loop.
 *
 * Likely Cause
 * ------------
 * Pointer not updated when heights are equal.
 *
 * ------------------------------------------------------------
 * Symptom
 * -------
 * Off-by-one.
 *
 * Likely Cause
 * ------------
 * Width computed incorrectly.
 *
 * Correct:
 *
 * right - left
 *
 * ------------------------------------------------------------
 * Symptom
 * -------
 * Wrong answers on increasing arrays.
 *
 * Likely Cause
 * ------------
 * Moving the taller wall.
 *
 * ------------------------------------------------------------
 * Symptom
 * -------
 * Correct for small inputs only.
 *
 * Likely Cause
 * ------------
 * Incorrect discard rule.
 */

/*
 * ============================================================
 * ⚡ IMPLEMENTATION RECONSTRUCTION DRILL
 * ============================================================
 *
 * Without memorizing code, reconstruct mechanically:
 *
 * Step 1
 * ------
 * left = 0
 *
 * Step 2
 * ------
 * right = n - 1
 *
 * Step 3
 * ------
 * best = 0
 *
 * Step 4
 * ------
 * while(left < right)
 *
 * Step 5
 * ------
 * width
 *
 * Step 6
 * ------
 * limitingHeight = min(...)
 *
 * Step 7
 * ------
 * area = width × limitingHeight
 *
 * Step 8
 * ------
 * update answer
 *
 * Step 9
 * ------
 * move shorter pointer
 *
 * Step 10
 * -------
 * return best
 */


    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    public static void main(String[] args) {

        // Enable assertions with:
        // java -ea ContainerWithMostWater

        // Representative LeetCode example.
        assert Optimal.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}) == 49;

        // Minimum valid input.
        assert Optimal.maxArea(new int[]{1, 1}) == 1;

        // Equal heights.
        assert Optimal.maxArea(new int[]{5, 5}) == 5;

        // Strictly increasing heights.
        assert Optimal.maxArea(new int[]{1, 2, 3, 4, 5}) == 6;

        // Strictly decreasing heights.
        assert Optimal.maxArea(new int[]{5, 4, 3, 2, 1}) == 6;

        // Interior optimum.
        assert Optimal.maxArea(new int[]{2, 3, 10, 5, 7, 8, 9}) == 36;

        // Tall walls at both ends.
        assert Optimal.maxArea(new int[]{100, 1, 1, 1, 100}) == 400;

        // All equal heights.
        assert Optimal.maxArea(new int[]{4, 4, 4, 4, 4}) == 16;

        // Contains zero heights.
        assert Optimal.maxArea(new int[]{0, 2, 0, 4, 0}) == 4;

        // Dominated interior heights.
        assert Optimal.maxArea(new int[]{9, 1, 2, 3, 9}) == 36;

        // Large middle height should not fool the algorithm.
        assert Optimal.maxArea(new int[]{1, 100, 1, 1, 1}) == 4;

        // Multiple optimal answers.
        assert Optimal.maxArea(new int[]{2, 4, 2, 4, 2}) == 8;

        // Brute-force cross verification on representative cases.
        int[][] regressionTests = {
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
                {2, 4, 2, 4, 2}
        };

        for (int[] test : regressionTests) {
            int brute = BruteForce.maxArea(test);
            int optimal = Optimal.maxArea(test);

            // Optimal implementation must match exhaustive search.
            assert brute == optimal :
                    "Mismatch for " + Arrays.toString(test)
                            + " brute=" + brute
                            + " optimal=" + optimal;
        }

        // Small exhaustive-style sanity checks.
        assert Optimal.maxArea(new int[]{2, 1}) == 1;
        assert Optimal.maxArea(new int[]{3, 2, 3}) == 6;
        assert Optimal.maxArea(new int[]{1, 2, 1}) == 2;
        assert Optimal.maxArea(new int[]{6, 9, 3, 4, 5, 8}) == BruteForce.maxArea(new int[]{6, 9, 3, 4, 5, 8});

        System.out.println("All assertions passed.");
    }
}