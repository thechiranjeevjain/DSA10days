package org.chijai.day9.session1;

import java.util.*;

/**
 * =============================================================================
 * House Robber
 * LeetCode 198
 * =============================================================================
 *
 * 🔗 Official Link:
 * https://leetcode.com/problems/house-robber/
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Dynamic Programming
 * Array
 *
 * =============================================================================
 * 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
 * =============================================================================
 *
 * You are a professional robber planning to rob houses along a street.
 * Each house has a certain amount of money stashed, the only constraint
 * stopping you from robbing each of them is that adjacent houses have
 * security systems connected and it will automatically contact the police
 * if two adjacent houses were broken into on the same night.
 *
 * Given an integer array nums representing the amount of money of each house,
 * return the maximum amount of money you can rob tonight without alerting
 * the police.
 *
 * -----------------------------------------------------------------------------
 * Example 1
 * -----------------------------------------------------------------------------
 *
 * Input:
 * nums = [1,2,3,1]
 *
 * Output:
 * 4
 *
 * Explanation:
 * Rob house 1 (money = 1) and then rob house 3 (money = 3).
 * Total amount you can rob = 1 + 3 = 4.
 *
 * -----------------------------------------------------------------------------
 * Example 2
 * -----------------------------------------------------------------------------
 *
 * Input:
 * nums = [2,7,9,3,1]
 *
 * Output:
 * 12
 *
 * Explanation:
 * Rob house 1 (money = 2), rob house 3 (money = 9)
 * and rob house 5 (money = 1).
 * Total amount you can rob = 2 + 9 + 1 = 12.
 *
 * -----------------------------------------------------------------------------
 * Constraints
 * -----------------------------------------------------------------------------
 *
 * 1 <= nums.length <= 100
 * 0 <= nums[i] <= 400
 *
 * =============================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * =============================================================================
 *
 * Pattern Name:
 * Dynamic Programming — Take / Skip DP
 *
 * Archetype:
 *
 * At every position:
 *
 * • TAKE current choice
 * • SKIP current choice
 *
 * while respecting local constraints.
 *
 * -----------------------------------------------------------------------------
 * 🟢 CORE INVARIANT
 * -----------------------------------------------------------------------------
 *
 * dp[i]
 * =
 * best possible answer considering houses [0...i]
 *
 * NOT:
 *
 * "best answer ending at i"
 *
 * This distinction matters enormously.
 *
 * -----------------------------------------------------------------------------
 * Why This Works
 * -----------------------------------------------------------------------------
 *
 * Every house creates exactly two valid possibilities:
 *
 * 1. SKIP current house
 *
 *      answer remains:
 *
 *      dp[i-1]
 *
 * 2. ROB current house
 *
 *      previous house becomes forbidden
 *
 *      answer becomes:
 *
 *      nums[i] + dp[i-2]
 *
 * Therefore:
 *
 * dp[i] =
 * max(
 *      dp[i-1],
 *      nums[i] + dp[i-2]
 * )
 *
 * -----------------------------------------------------------------------------
 * Recognition Signals
 * -----------------------------------------------------------------------------
 *
 * Strong indicators:
 *
 * • cannot choose adjacent elements
 * • choose / skip decisions
 * • maximize non-adjacent sum
 * • local conflict constraints
 * • future depends on nearby decisions
 *
 * -----------------------------------------------------------------------------
 * When To Use This Pattern
 * -----------------------------------------------------------------------------
 *
 * Use when:
 *
 * • every index creates binary decision
 * • taking current invalidates nearby choices
 * • optimal substructure exists
 * • state only depends on small previous window
 *
 * -----------------------------------------------------------------------------
 * Difference vs Similar Patterns
 * -----------------------------------------------------------------------------
 *
 * Kadane:
 *
 * • contiguous subarray
 * • no skipping gaps
 *
 * House Robber:
 *
 * • subsequence style selection
 * • adjacency restriction
 *
 * LIS:
 *
 * • value relationship based
 *
 * House Robber:
 *
 * • position relationship based
 *
 * =============================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * =============================================================================
 *
 * Mental Model:
 *
 * Walk left → right.
 *
 * At every house ask:
 *
 * "Does robbing THIS house
 * beat skipping it?"
 *
 * -----------------------------------------------------------------------------
 * 🟢 State Meaning
 * -----------------------------------------------------------------------------
 *
 * dp[i]
 * =
 * best achievable answer till index i
 *
 * meaning:
 *
 * considering ALL valid configurations
 * from houses [0...i]
 *
 * -----------------------------------------------------------------------------
 * 🔴 Extremely Important Clarification
 * -----------------------------------------------------------------------------
 *
 * The rule is NOT:
 *
 * "Always rob alternate houses."
 *
 * Wrong.
 *
 * Example:
 *
 * [5,1,1,5]
 *
 * Optimal:
 *
 * 5 + 5 = 10
 *
 * We skipped TWO middle houses.
 *
 * Therefore:
 *
 * this is NOT:
 *
 * • even index selection
 * • odd index selection
 *
 * Every house creates fresh dynamic decision:
 *
 * • rob
 * • skip
 *
 * -----------------------------------------------------------------------------
 * 🟢 Visual Adjacency Intuition
 * -----------------------------------------------------------------------------
 *
 * Suppose:
 *
 * [2] --- [7] --- [9]
 *          ^
 *       ROBBED
 *
 * Then:
 *
 * 7 blocks its immediate neighbors.
 *
 * Therefore:
 *
 * • cannot rob 2 with 7
 * • cannot rob 9 with 7
 *
 * -----------------------------------------------------------------------------
 * 🧠 The Core Conflict
 * -----------------------------------------------------------------------------
 *
 * At every house:
 *
 * "If I rob THIS house,
 * I lose access to previous house."
 *
 * THAT single sentence generates the entire recurrence.
 *
 * -----------------------------------------------------------------------------
 * 🟢 Physical Visualization Of dp[i-2]
 * -----------------------------------------------------------------------------
 *
 * Suppose:
 *
 * [2] [7] [9]
 *           ^
 *
 * We are standing on house 9.
 *
 * If we rob 9:
 *
 * 7 becomes illegal immediately.
 *
 * Therefore:
 *
 * 9 can ONLY combine with houses BEFORE 7.
 *
 * Meaning:
 *
 * best answer till index 0.
 *
 * Which is:
 *
 * dp[i-2]
 *
 * Therefore:
 *
 * take = nums[i] + dp[i-2]
 *
 * -----------------------------------------------------------------------------
 * 🔥 Entire Recurrence
 * -----------------------------------------------------------------------------
 *
 * ROB current:
 *
 * nums[i] + dp[i-2]
 *
 * SKIP current:
 *
 * dp[i-1]
 *
 * Choose better answer.
 *
 * -----------------------------------------------------------------------------
 * 🟢 Visual Transition Diagram
 * -----------------------------------------------------------------------------
 *
 *               TAKE current
 *            nums[i] + dp[i-2]
 *                    \
 *                     \
 *                      ---> max ---> dp[i]
 *                     /
 *                    /
 *               SKIP current
 *                    dp[i-1]
 *
 * -----------------------------------------------------------------------------
 * 🌉 Why Greedy Fails
 * -----------------------------------------------------------------------------
 *
 * Greedy Thinking:
 *
 * "Take bigger nearby value."
 *
 * Counterexample:
 *
 * [2,7,9]
 *
 * Greedy:
 *
 * choose 7
 *
 * then cannot choose 9
 *
 * total = 7
 *
 * Better:
 *
 * 2 + 9 = 11
 *
 * Therefore:
 *
 * local optimum != global optimum
 *
 * -----------------------------------------------------------------------------
 * 🟢 Transition Invariant
 * -----------------------------------------------------------------------------
 *
 * While processing index i:
 *
 * dp[i-1]
 * already stores optimal answer till previous house.
 *
 * dp[i-2]
 * already stores optimal answer excluding adjacency conflict.
 *
 * Therefore:
 *
 * take = nums[i] + dp[i-2]
 *
 * skip = dp[i-1]
 *
 * dp[i] = max(take, skip)
 *
 * -----------------------------------------------------------------------------
 * 🟢 Allowed Moves
 * -----------------------------------------------------------------------------
 *
 * • rob current house
 * • skip current house
 *
 * -----------------------------------------------------------------------------
 * 🔴 Forbidden Moves
 * -----------------------------------------------------------------------------
 *
 * • rob adjacent houses
 *
 * -----------------------------------------------------------------------------
 * 🟢 Termination Logic
 * -----------------------------------------------------------------------------
 *
 * After final iteration:
 *
 * dp[n-1]
 * stores globally optimal answer.
 *
 * =============================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * =============================================================================
 *
 * -----------------------------------------------------------------------------
 * Wrong Approach 1:
 * Always Take Larger Adjacent House
 * -----------------------------------------------------------------------------
 *
 * Why It Feels Correct:
 *
 * locally larger values seem better.
 *
 * Why It Fails:
 *
 * future combinations become blocked.
 *
 * Counterexample:
 *
 * [2,7,9]
 *
 * greedy => 7
 *
 * optimal => 11
 *
 * -----------------------------------------------------------------------------
 * Wrong Approach 2:
 *
 * dp[i] = nums[i] + dp[i-1]
 * -----------------------------------------------------------------------------
 *
 * Why It Seems Natural:
 *
 * "add current onto previous best"
 *
 * Fatal Problem:
 *
 * dp[i-1] may already include adjacent house.
 *
 * This directly violates invariant.
 *
 * -----------------------------------------------------------------------------
 * Wrong Approach 3:
 *
 * Incorrect DP Meaning
 * -----------------------------------------------------------------------------
 *
 * Wrong:
 *
 * dp[i] =
 * answer IF we rob i
 *
 * Why Dangerous:
 *
 * loses skip-state information.
 *
 * Correct:
 *
 * dp[i] =
 * best answer till i
 *
 * -----------------------------------------------------------------------------
 * Wrong Approach 4:
 *
 * Pure Recursion Without Memoization
 * -----------------------------------------------------------------------------
 *
 * Why It Fails:
 *
 * repeated overlapping subproblems explode exponentially.
 *
 * =============================================================================
 * ⚙️ HOW TO PHYSICALLY ASSEMBLE THE CODE
 * =============================================================================
 *
 * 🛠️ IMPLEMENTATION BLUEPRINT
 *
 * Goal:
 *
 * reduce working-memory load during interviews.
 *
 * -----------------------------------------------------------------------------
 * STEP 1
 * -----------------------------------------------------------------------------
 *
 * Create method skeleton.
 *
 * public int rob(int[] nums)
 *
 * -----------------------------------------------------------------------------
 * STEP 2
 * -----------------------------------------------------------------------------
 *
 * Handle tiny edge cases FIRST.
 *
 * if nums.length == 1
 *
 * return nums[0]
 *
 * -----------------------------------------------------------------------------
 * STEP 3
 * -----------------------------------------------------------------------------
 *
 * Define rolling DP states:
 *
 * dp_i_2
 * =
 * best till i-2
 *
 * dp_i_1
 * =
 * best till i-1
 *
 * -----------------------------------------------------------------------------
 * STEP 4
 * -----------------------------------------------------------------------------
 *
 * Initialize base states:
 *
 * dp_i_2 = nums[0]
 *
 * dp_i_1 = max(nums[0], nums[1])
 *
 * -----------------------------------------------------------------------------
 * STEP 5
 * -----------------------------------------------------------------------------
 *
 * Start loop from i = 2
 *
 * because:
 *
 * first two states already initialized.
 *
 * -----------------------------------------------------------------------------
 * STEP 6
 * -----------------------------------------------------------------------------
 *
 * Compute TAKE branch:
 *
 * take = nums[i] + dp_i_2
 *
 * -----------------------------------------------------------------------------
 * STEP 7
 * -----------------------------------------------------------------------------
 *
 * Compute SKIP branch:
 *
 * skip = dp_i_1
 *
 * -----------------------------------------------------------------------------
 * STEP 8
 * -----------------------------------------------------------------------------
 *
 * Merge branches:
 *
 * dp_i = max(take, skip)
 *
 * -----------------------------------------------------------------------------
 * STEP 9
 * -----------------------------------------------------------------------------
 *
 * Slide DP window:
 *
 * dp_i_2 = dp_i_1
 *
 * dp_i_1 = dp_i
 *
 * -----------------------------------------------------------------------------
 * STEP 10
 * -----------------------------------------------------------------------------
 *
 * Return:
 *
 * dp_i_1
 *
 * because it stores answer till final index.
 *
 * =============================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE (MEMORY SCAFFOLD)
 * =============================================================================
 *
 * handle small case
 *
 * initialize dp_i_2
 * initialize dp_i_1
 *
 * for each remaining house:
 *
 *     take = current + dp_i_2
 *     skip = dp_i_1
 *
 *     dp_i = max(take, skip)
 *
 *     slide window
 *
 * return dp_i_1
 *
 * =============================================================================
 * PRIMARY PROBLEM — SOLUTION CLASSES
 * =============================================================================
 */
public class HouseRobber {


    /**
     * =========================================================================
     * BRUTE FORCE SOLUTION
     * =========================================================================
     *
     * Core Idea:
     *
     * At every house:
     *
     * • rob it
     * • skip it
     *
     * recursively explore both.
     *
     * ----------------------------------------------------------------------------
     * 🟢 Invariant
     * ----------------------------------------------------------------------------
     *
     * dfs(index)
     * =
     * optimal answer starting from index.
     *
     * ----------------------------------------------------------------------------
     * Why It Works
     * ----------------------------------------------------------------------------
     *
     * Every valid configuration emerges from:
     *
     * • taking current
     * • skipping current
     *
     * ----------------------------------------------------------------------------
     * Why It Is Bad
     * ----------------------------------------------------------------------------
     *
     * Massive repeated recomputation.
     *
     * ----------------------------------------------------------------------------
     * Time Complexity
     * ----------------------------------------------------------------------------
     *
     * O(2^n)
     *
     * ----------------------------------------------------------------------------
     * Space Complexity
     * ----------------------------------------------------------------------------
     *
     * O(n)
     *
     * recursion stack.
     *
     * ----------------------------------------------------------------------------
     * Interview Preference
     * ----------------------------------------------------------------------------
     *
     * Only useful as:
     *
     * • derivation step
     * • recurrence discovery
     */
    static class BruteForceSolution {

        public int rob(int[] nums) {

            return dfs(nums, 0);
        }

        private int dfs(int[] nums, int index) {

            /*
             * Base case:
             *
             * no houses remaining.
             */
            if (index >= nums.length) {
                return 0;
            }

            /*
             * LIVE CODING NARRATION:
             *
             * If we rob current house,
             * next adjacent house becomes forbidden.
             */
            int take = nums[index] + dfs(nums, index + 2);

            /*
             * LIVE CODING NARRATION:
             *
             * Skip current house completely.
             */
            int skip = dfs(nums, index + 1);

            /*
             * Invariant:
             *
             * choose better valid configuration.
             */
            return Math.max(take, skip);
        }
    }

    /**
     * =========================================================================
     * IMPROVED SOLUTION — MEMOIZATION
     * =========================================================================
     *
     * Core Idea:
     *
     * Cache repeated subproblems.
     *
     * ----------------------------------------------------------------------------
     * 🟢 Invariant
     * ----------------------------------------------------------------------------
     *
     * memo[i]
     * =
     * optimal answer starting from index i.
     *
     * ----------------------------------------------------------------------------
     * What Problem It Fixes
     * ----------------------------------------------------------------------------
     *
     * Prevents exponential recomputation.
     *
     * ----------------------------------------------------------------------------
     * Time Complexity
     * ----------------------------------------------------------------------------
     *
     * O(n)
     *
     * ----------------------------------------------------------------------------
     * Space Complexity
     * ----------------------------------------------------------------------------
     *
     * O(n)
     *
     * recursion + memo.
     *
     * ----------------------------------------------------------------------------
     * Interview Preference
     * ----------------------------------------------------------------------------
     *
     * Strong intermediate step before bottom-up DP.
     */
    static class MemoizationSolution {

        public int rob(int[] nums) {

            Integer[] memo = new Integer[nums.length];

            return dfs(nums, 0, memo);
        }

        private int dfs(
                int[] nums,
                int index,
                Integer[] memo
        ) {

            /*
             * Base case:
             *
             * beyond final house.
             */
            if (index >= nums.length) {
                return 0;
            }

            /*
             * LIVE CODING NARRATION:
             *
             * Reuse previously solved subproblem.
             */
            if (memo[index] != null) {
                return memo[index];
            }

            /*
             * Rob current house.
             *
             * Adjacent becomes forbidden.
             */
            int take = nums[index] + dfs(
                    nums,
                    index + 2,
                    memo
            );

            /*
             * Skip current house.
             */
            int skip = dfs(
                    nums,
                    index + 1,
                    memo
            );

            /*
             * Store globally optimal answer
             * for this starting index.
             */
            memo[index] = Math.max(take, skip);

            return memo[index];
        }
    }

    /**
     * =========================================================================
     * OPTIMAL SOLUTION — INTERVIEW PREFERRED
     * =========================================================================
     *
     * Core Idea:
     *
     * Compress DP because:
     *
     * dp[i]
     * only depends on:
     *
     * • dp[i-1]
     * • dp[i-2]
     *
     * ----------------------------------------------------------------------------
     * 🟢 Core Invariant
     * ----------------------------------------------------------------------------
     *
     * dp_i_2
     * =
     * best answer till i-2
     *
     * dp_i_1
     * =
     * best answer till i-1
     *
     * ----------------------------------------------------------------------------
     * Why This Is Elite Interview Version
     * ----------------------------------------------------------------------------
     *
     * • optimal complexity
     * • minimal memory
     * • invariant remains visible
     * • low cognitive load
     * • easy debugging
     * • recurrence remains derivable
     *
     * ----------------------------------------------------------------------------
     * Time Complexity
     * ----------------------------------------------------------------------------
     *
     * O(n)
     *
     * ----------------------------------------------------------------------------
     * Space Complexity
     * ----------------------------------------------------------------------------
     *
     * O(1)
     */
    static class OptimalSolution {

        public int rob(int[] nums) {

            /*
             * LIVE CODING NARRATION:
             *
             * Handle smallest edge case early.
             */
            if (nums.length == 1) {
                return nums[0];
            }

            /*
             * dp_i_2
             *
             * Initially:
             *
             * dp[0]
             */
            int dp_i_2 = nums[0];

            /*
             * dp_i_1
             *
             * Initially:
             *
             * best answer till index 1.
             */
            int dp_i_1 = Math.max(
                    nums[0],
                    nums[1]
            );

            /*
             * LIVE CODING NARRATION:
             *
             * Start from third house because
             * first two states already initialized.
             */
            for (int i = 2; i < nums.length; i++) {

                /*
                 * LIVE CODING NARRATION:
                 *
                 * Taking current means previous
                 * house becomes illegal.
                 */
                int take = nums[i] + dp_i_2;

                /*
                 * LIVE CODING NARRATION:
                 *
                 * Skipping current preserves
                 * previous optimal answer.
                 */
                int skip = dp_i_1;

                /*
                 * dp_i
                 * =
                 * best answer till current index.
                 */
                int dp_i = Math.max(take, skip);

                /*
                 * LIVE CODING NARRATION:
                 *
                 * Slide DP window forward.
                 */
                dp_i_2 = dp_i_1;
                dp_i_1 = dp_i;
            }

            /*
             * Final invariant:
             *
             * dp_i_1 stores answer till last index.
             */
            return dp_i_1;
        }
    }

    /**
     * =========================================================================
     * FULL DP ARRAY SOLUTION
     * =========================================================================
     *
     * Purpose:
     *
     * Best for:
     *
     * • first-time learners
     * • recurrence visualization
     * • debugging transitions
     * • understanding state growth
     *
     * ----------------------------------------------------------------------------
     * Time Complexity
     * ----------------------------------------------------------------------------
     *
     * O(n)
     *
     * ----------------------------------------------------------------------------
     * Space Complexity
     * ----------------------------------------------------------------------------
     *
     * O(n)
     */
    static class DPArraySolution {

        public int rob(int[] nums) {

            /*
             * Edge case:
             *
             * single house.
             */
            if (nums.length == 1) {
                return nums[0];
            }

            /*
             * dp[i]
             * =
             * best answer till index i
             */
            int[] dp = new int[nums.length];

            /*
             * Base case:
             *
             * best till first house.
             */
            dp[0] = nums[0];

            /*
             * Base case:
             *
             * choose better among first two houses.
             */
            dp[1] = Math.max(
                    nums[0],
                    nums[1]
            );

            /*
             * Build states left → right.
             */
            for (int i = 2; i < nums.length; i++) {

                /*
                 * If we rob current:
                 *
                 * previous house becomes forbidden.
                 */
                int take = nums[i] + dp[i - 2];

                /*
                 * If we skip current:
                 *
                 * answer remains previous best.
                 */
                int skip = dp[i - 1];

                /*
                 * Store optimal answer till i.
                 */
                dp[i] = Math.max(take, skip);
            }

            /*
             * Final DP state stores global optimum.
             */
            return dp[nums.length - 1];
        }
    }

    /**
     * =========================================================================
     * 🔴 FORENSIC DEBUGGING SECTION
     * =========================================================================
     *
     * Goal:
     *
     * understand HOW incorrect transitions fail.
     *
     * ----------------------------------------------------------------------------
     * Bug 1:
     *
     * nums[i] + dp[i-1]
     * ----------------------------------------------------------------------------
     *
     * Why Wrong:
     *
     * dp[i-1]
     * may already contain adjacent house.
     *
     * Example:
     *
     * [2,7]
     *
     * Wrong computation:
     *
     * 7 + 2 = 9
     *
     * Impossible.
     *
     * Adjacent houses robbed simultaneously.
     *
     * ----------------------------------------------------------------------------
     * Bug 2:
     *
     * Wrong DP Meaning
     * ----------------------------------------------------------------------------
     *
     * Wrong:
     *
     * dp[i]
     * =
     * answer ending at i
     *
     * Why Dangerous:
     *
     * loses skip-state information.
     *
     * ----------------------------------------------------------------------------
     * Bug 3:
     *
     * forgetting edge case:
     * nums.length == 1
     * ----------------------------------------------------------------------------
     *
     * Causes:
     *
     * index out of bounds.
     *
     * ----------------------------------------------------------------------------
     * Bug 4:
     *
     * incorrect window shift order
     * ----------------------------------------------------------------------------
     *
     * Wrong:
     *
     * dp_i_1 = dp_i
     * dp_i_2 = dp_i_1
     *
     * Why Wrong:
     *
     * old dp_i_1 gets destroyed too early.
     */

    /**
     * =========================================================================
     * 🟣 INTERVIEW ARTICULATION (NO CODE)
     * =========================================================================
     *
     * ----------------------------------------------------------------------------
     * How To Explain The Invariant
     * ----------------------------------------------------------------------------
     *
     * "dp[i] represents the maximum money we can rob
     * considering houses from 0 to i."
     *
     * ----------------------------------------------------------------------------
     * How To Explain The Transition
     * ----------------------------------------------------------------------------
     *
     * "At every house,
     * I have exactly two valid decisions:
     *
     * 1. rob current house
     * 2. skip current house"
     *
     * If I rob current:
     *
     * previous house becomes illegal.
     *
     * Therefore:
     *
     * take = nums[i] + dp[i-2]
     *
     * If I skip current:
     *
     * skip = dp[i-1]
     *
     * Then choose larger answer.
     *
     * ----------------------------------------------------------------------------
     * Correctness Guarantee
     * ----------------------------------------------------------------------------
     *
     * Every valid configuration must either:
     *
     * • rob current house
     * • skip current house
     *
     * Therefore recurrence is exhaustive.
     *
     * ----------------------------------------------------------------------------
     * What Breaks If Changed
     * ----------------------------------------------------------------------------
     *
     * If:
     *
     * nums[i] + dp[i-1]
     *
     * is used,
     *
     * adjacent robbery becomes possible.
     *
     * Constraint violated immediately.
     *
     * ----------------------------------------------------------------------------
     * Why Space Optimization Works
     * ----------------------------------------------------------------------------
     *
     * Because:
     *
     * dp[i]
     * only depends on:
     *
     * • dp[i-1]
     * • dp[i-2]
     *
     * older states become irrelevant.
     *
     * ----------------------------------------------------------------------------
     * Streaming Feasibility
     * ----------------------------------------------------------------------------
     *
     * Yes.
     *
     * One-pass left-to-right processing works.
     *
     * ----------------------------------------------------------------------------
     * When NOT To Use This Pattern
     * ----------------------------------------------------------------------------
     *
     * Not suitable when:
     *
     * • dependency range is unbounded
     * • state depends on arbitrary history
     * • graph dependencies replace linear dependencies
     *
     * =========================================================================
     * 🎯 INTERVIEW RECALL SHEET (30-SECOND RECALL)
     * =========================================================================
     *
     * Pattern Trigger:
     *
     * "Cannot take adjacent elements."
     *
     * ----------------------------------------------------------------------------
     * Core Invariant
     * ----------------------------------------------------------------------------
     *
     * dp[i]
     * =
     * best answer till i
     *
     * ----------------------------------------------------------------------------
     * Core Conflict
     * ----------------------------------------------------------------------------
     *
     * taking current blocks previous.
     *
     * ----------------------------------------------------------------------------
     * Transition
     * ----------------------------------------------------------------------------
     *
     * take = nums[i] + dp[i-2]
     *
     * skip = dp[i-1]
     *
     * dp[i] = max(take, skip)
     *
     * ----------------------------------------------------------------------------
     * Common Trap
     * ----------------------------------------------------------------------------
     *
     * nums[i] + dp[i-1]
     *
     * adjacency violation.
     *
     * ----------------------------------------------------------------------------
     * Edge Cases
     * ----------------------------------------------------------------------------
     *
     * • single house
     * • two houses
     * • all zeros
     *
     * ----------------------------------------------------------------------------
     * Re-Derivation Cue
     * ----------------------------------------------------------------------------
     *
     * "If I rob current,
     * previous becomes forbidden."
     *
     * =========================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =========================================================================
     *
     * ----------------------------------------------------------------------------
     * Variation 1:
     * House Robber II
     * ----------------------------------------------------------------------------
     *
     * Houses become circular.
     *
     * First and last houses become adjacent.
     *
     * Solution:
     *
     * max(
     *      rob[0...n-2],
     *      rob[1...n-1]
     * )
     *
     * ----------------------------------------------------------------------------
     * Invariant Status
     * ----------------------------------------------------------------------------
     *
     * Same invariant survives.
     *
     * Only decomposition changes.
     *
     * ----------------------------------------------------------------------------
     * Variation 2:
     * Distance-K Restriction
     * ----------------------------------------------------------------------------
     *
     * Cannot rob within K distance.
     *
     * Transition becomes:
     *
     * take = nums[i] + dp[i-k-1]
     *
     * ----------------------------------------------------------------------------
     * Invariant Status
     * ----------------------------------------------------------------------------
     *
     * Same take/skip structure survives.
     *
     * ----------------------------------------------------------------------------
     * Variation 3:
     * House Robber III
     * ----------------------------------------------------------------------------
     *
     * Houses become tree nodes.
     *
     * Adjacency:
     *
     * parent-child relationship.
     *
     * ----------------------------------------------------------------------------
     * Invariant Status
     * ----------------------------------------------------------------------------
     *
     * Linear invariant breaks.
     *
     * Need tree DP.
     *
     * ----------------------------------------------------------------------------
     * Pattern-Break Signals
     * ----------------------------------------------------------------------------
     *
     * Pattern usually breaks when:
     *
     * • dependency no longer local
     * • structure no longer linear
     * • state dimension explodes
     *
     * =========================================================================
     * ⚫ REINFORCEMENT PROBLEMS
     * =========================================================================
     */

    /**
     * =========================================================================
     * Reinforcement Problem 1
     * Maximum Sum Of Non-Adjacent Elements
     * =========================================================================
     *
     * Summary:
     *
     * Given array,
     * maximize sum of non-adjacent elements.
     *
     * ----------------------------------------------------------------------------
     * Example
     * ----------------------------------------------------------------------------
     *
     * [3,2,7,10]
     *
     * answer:
     *
     * 13
     *
     * ----------------------------------------------------------------------------
     * Invariant Mapping
     * ----------------------------------------------------------------------------
     *
     * EXACT same invariant as House Robber.
     *
     * ----------------------------------------------------------------------------
     * Interview Trap
     * ----------------------------------------------------------------------------
     *
     * confusing with contiguous subarray problems.
     */
    static class MaximumNonAdjacentSum {

        public int maxSum(int[] nums) {

            if (nums.length == 1) {
                return nums[0];
            }

            int dp_i_2 = nums[0];

            int dp_i_1 = Math.max(
                    nums[0],
                    nums[1]
            );

            for (int i = 2; i < nums.length; i++) {

                int take = nums[i] + dp_i_2;

                int skip = dp_i_1;

                int dp_i = Math.max(take, skip);

                dp_i_2 = dp_i_1;
                dp_i_1 = dp_i;
            }

            return dp_i_1;
        }
    }

    /**
     * =========================================================================
     * Reinforcement Problem 2
     * Min Cost Climbing Stairs
     * =========================================================================
     *
     * Summary:
     *
     * Reach top with minimum cost.
     *
     * ----------------------------------------------------------------------------
     * Example
     * ----------------------------------------------------------------------------
     *
     * [10,15,20]
     *
     * answer:
     *
     * 15
     *
     * ----------------------------------------------------------------------------
     * Invariant Mapping
     * ----------------------------------------------------------------------------
     *
     * Same rolling-state idea survives.
     *
     * maximize -> minimize
     *
     * ----------------------------------------------------------------------------
     * Interview Trap
     * ----------------------------------------------------------------------------
     *
     * top exists BEYOND array.
     */
    static class MinCostClimbingStairs {

        public int minCostClimbingStairs(int[] cost) {

            int dp_i_2 = cost[0];

            int dp_i_1 = cost[1];

            for (int i = 2; i < cost.length; i++) {

                int dp_i = cost[i] + Math.min(
                        dp_i_1,
                        dp_i_2
                );

                dp_i_2 = dp_i_1;
                dp_i_1 = dp_i;
            }

            return Math.min(
                    dp_i_1,
                    dp_i_2
            );
        }
    }

    /**
     * =========================================================================
     * Reinforcement Problem 3
     * Delete And Earn
     * =========================================================================
     *
     * Summary:
     *
     * Taking x deletes:
     *
     * • x-1
     * • x+1
     *
     * ----------------------------------------------------------------------------
     * Example
     * ----------------------------------------------------------------------------
     *
     * [3,4,2]
     *
     * answer:
     *
     * 6
     *
     * ----------------------------------------------------------------------------
     * Invariant Mapping
     * ----------------------------------------------------------------------------
     *
     * adjacent VALUES become adjacent houses.
     *
     * ----------------------------------------------------------------------------
     * Interview Trap
     * ----------------------------------------------------------------------------
     *
     * forgetting frequency compression step.
     */
    static class DeleteAndEarn {

        public int deleteAndEarn(int[] nums) {

            int max = 0;

            for (int num : nums) {
                max = Math.max(max, num);
            }

            int[] points = new int[max + 1];

            /*
             * Compress frequencies into points.
             */
            for (int num : nums) {
                points[num] += num;
            }

            if (points.length == 1) {
                return points[0];
            }

            int dp_i_2 = points[0];

            int dp_i_1 = Math.max(
                    points[0],
                    points[1]
            );

            for (int i = 2; i < points.length; i++) {

                int take = points[i] + dp_i_2;

                int skip = dp_i_1;

                int dp_i = Math.max(take, skip);

                dp_i_2 = dp_i_1;
                dp_i_1 = dp_i;
            }

            return dp_i_1;
        }
    }

    /**
     * =========================================================================
     * 🧩 RELATED PROBLEMS
     * =========================================================================
     */

    /**
     * =========================================================================
     * Related Problem 1
     * House Robber II
     * =========================================================================
     *
     * Same / Modified / Broken Invariant:
     *
     * SAME invariant.
     *
     * Modified topology:
     *
     * circular street.
     *
     * ----------------------------------------------------------------------------
     * Edge Case
     * ----------------------------------------------------------------------------
     *
     * single house.
     *
     * ----------------------------------------------------------------------------
     * Interview Note
     * ----------------------------------------------------------------------------
     *
     * Break circular dependency
     * into two linear robberies.
     */
    static class HouseRobberII {

        public int rob(int[] nums) {

            if (nums.length == 1) {
                return nums[0];
            }

            return Math.max(
                    robLinear(nums, 0, nums.length - 2),
                    robLinear(nums, 1, nums.length - 1)
            );
        }

        private int robLinear(
                int[] nums,
                int start,
                int end
        ) {

            int dp_i_2 = 0;
            int dp_i_1 = 0;

            for (int i = start; i <= end; i++) {

                int take = nums[i] + dp_i_2;

                int skip = dp_i_1;

                int dp_i = Math.max(take, skip);

                dp_i_2 = dp_i_1;
                dp_i_1 = dp_i;
            }

            return dp_i_1;
        }
    }

    /**
     * =========================================================================
     * Related Problem 2
     * Paint House
     * =========================================================================
     *
     * Same / Modified / Broken Invariant:
     *
     * MODIFIED invariant.
     *
     * Adjacent houses cannot have same color.
     *
     * ----------------------------------------------------------------------------
     * Edge Case
     * ----------------------------------------------------------------------------
     *
     * single house.
     *
     * ----------------------------------------------------------------------------
     * Interview Note
     * ----------------------------------------------------------------------------
     *
     * multi-state DP.
     */
    static class PaintHouse {

        public int minCost(int[][] costs) {

            for (int i = 1; i < costs.length; i++) {

                costs[i][0] += Math.min(
                        costs[i - 1][1],
                        costs[i - 1][2]
                );

                costs[i][1] += Math.min(
                        costs[i - 1][0],
                        costs[i - 1][2]
                );

                costs[i][2] += Math.min(
                        costs[i - 1][0],
                        costs[i - 1][1]
                );
            }

            int n = costs.length - 1;

            return Math.min(
                    costs[n][0],
                    Math.min(
                            costs[n][1],
                            costs[n][2]
                    )
            );
        }
    }

    /**
     * =========================================================================
     * Related Problem 3
     * Jump Game
     * =========================================================================
     *
     * Same / Modified / Broken Invariant:
     *
     * BROKEN invariant.
     *
     * Reachability replaces optimization.
     *
     * ----------------------------------------------------------------------------
     * Edge Case
     * ----------------------------------------------------------------------------
     *
     * zero causing dead-end.
     *
     * ----------------------------------------------------------------------------
     * Interview Note
     * ----------------------------------------------------------------------------
     *
     * Greedy dominates DP here.
     */
    static class JumpGame {

        public boolean canJump(int[] nums) {

            int farthest = 0;

            for (int i = 0; i < nums.length; i++) {

                /*
                 * Cannot even reach current position.
                 */
                if (i > farthest) {
                    return false;
                }

                /*
                 * Expand reachable frontier.
                 */
                farthest = Math.max(
                        farthest,
                        i + nums[i]
                );
            }

            return true;
        }
    }

    /**
     * =========================================================================
     * 🧠 MASTERY CHECKLIST
     * =========================================================================
     *
     * Can I explain:
     *
     * ✔ what dp[i] means?
     *
     * ✔ why dp[i-2] exists?
     *
     * ✔ why adjacent houses conflict?
     *
     * ✔ why greedy fails?
     *
     * ✔ why recurrence is exhaustive?
     *
     * ✔ why space optimization works?
     *
     * ✔ why only two previous states matter?
     *
     * ✔ why nums[i] + dp[i-1] is invalid?
     *
     * ✔ how to derive recurrence from constraint?
     *
     * ✔ how to adapt pattern to variants?
     *
     * ----------------------------------------------------------------------------
     * Debugging Readiness
     * ----------------------------------------------------------------------------
     *
     * Verify:
     *
     * • take uses dp[i-2]
     * • skip uses dp[i-1]
     * • window shift order correct
     * • edge cases handled
     *
     * ----------------------------------------------------------------------------
     * Pattern Boundary
     * ----------------------------------------------------------------------------
     *
     * Pattern weakens when:
     *
     * • dependencies become non-local
     * • graph replaces array
     * • state dimension explodes
     *
     * =========================================================================
     * 🧪 SELF-VERIFYING TESTS
     * =========================================================================
     */

    public static void main(String[] args) {

        OptimalSolution solver = new OptimalSolution();

        /*
         * =========================================================================
         * OFFICIAL EXAMPLE 1
         * =========================================================================
         *
         * [1,2,3,1]
         *
         * Best:
         *
         * 1 + 3 = 4
         */
        assertEquals(
                4,
                solver.rob(new int[]{1, 2, 3, 1}),
                "Official example 1 failed."
        );

        /*
         * =========================================================================
         * OFFICIAL EXAMPLE 2
         * =========================================================================
         *
         * [2,7,9,3,1]
         *
         * Best:
         *
         * 2 + 9 + 1 = 12
         */
        assertEquals(
                12,
                solver.rob(new int[]{2, 7, 9, 3, 1}),
                "Official example 2 failed."
        );

        /*
         * =========================================================================
         * EDGE CASE:
         * single house
         * =========================================================================
         */
        assertEquals(
                5,
                solver.rob(new int[]{5}),
                "Single house edge case failed."
        );

        /*
         * =========================================================================
         * EDGE CASE:
         * two houses
         * =========================================================================
         */
        assertEquals(
                10,
                solver.rob(new int[]{10, 2}),
                "Two house edge case failed."
        );

        /*
         * =========================================================================
         * GREEDY FAILURE CASE
         * =========================================================================
         *
         * Greedy may incorrectly choose:
         *
         * 2 + 1 = 3
         *
         * Correct:
         *
         * 2 + 2 = 4
         */
        assertEquals(
                4,
                solver.rob(new int[]{2, 1, 1, 2}),
                "Greedy failure test failed."
        );

        /*
         * =========================================================================
         * ALL ZERO CASE
         * =========================================================================
         */
        assertEquals(
                0,
                solver.rob(new int[]{0, 0, 0, 0}),
                "All zero case failed."
        );

        /*
         * =========================================================================
         * COMPLEX MIXED CASE
         * =========================================================================
         *
         * Ensures DP transitions remain stable.
         */
        assertEquals(
                41,
                solver.rob(new int[]{6, 7, 1, 30, 8, 2, 4}),
                "Complex mixed case failed."
        );

        /*
         * =========================================================================
         * LARGE BLOCKING CASE
         * =========================================================================
         *
         * Tests long-distance optimal selection.
         */
        assertEquals(
                103,
                solver.rob(new int[]{1, 3, 1, 3, 100}),
                "Long-distance selection case failed."
        );

        /*
         * =========================================================================
         * ADJACENCY TRAP
         * =========================================================================
         *
         * Ensures adjacent robbery never happens.
         */
        assertEquals(
                200,
                solver.rob(new int[]{100, 1, 1, 100}),
                "Adjacency constraint case failed."
        );

        System.out.println("All tests passed.");
        System.out.println();

        System.out.println(
                "I understand the invariant."
        );

        System.out.println(
                "I can re-derive the solution."
        );

        System.out.println(
                "I can physically reconstruct the implementation under pressure."
        );

        System.out.println(
                "This chapter is complete."
        );
    }

    private static void assertEquals(
            int expected,
            int actual,
            String message
    ) {

        if (expected != actual) {

            throw new AssertionError(
                    message
                            + " Expected = "
                            + expected
                            + ", Actual = "
                            + actual
            );
        }
    }
}


