package org.chijai.day9.dp.session2;

import java.util.Arrays;

/**
 * Partition Equal Subset Sum
 *
 * ============================================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================================
 *
 * Title:
 * Partition Equal Subset Sum
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Dynamic Programming
 * 0/1 Knapsack
 * Bounded Knapsack
 * Subset Sum
 * Boolean DP
 *
 * Official LeetCode:
 * https://leetcode.com/problems/partition-equal-subset-sum/
 *
 * ----------------------------------------------------------------------------
 * Problem
 * ----------------------------------------------------------------------------
 *
 * Given a non-empty integer array nums containing only positive integers,
 * determine whether the array can be partitioned into two subsets whose sums
 * are exactly equal.
 *
 * Every element must belong to exactly one subset.
 *
 * Each array element may be used AT MOST ONCE.
 *
 * ----------------------------------------------------------------------------
 * Key Observation
 * ----------------------------------------------------------------------------
 *
 * Let total = sum(nums)
 *
 * If total is odd:
 *
 *      Impossible.
 *
 * Otherwise,
 *
 * We only need to know:
 *
 *      Can we construct total / 2 ?
 *
 * If one subset equals total/2,
 * the remaining elements automatically equal total/2.
 *
 * Therefore the entire problem becomes a classic
 * 0/1 Subset Sum (Bounded Knapsack).
 *
 * ----------------------------------------------------------------------------
 * Constraints
 * ----------------------------------------------------------------------------
 *
 * 1 <= nums.length <= 200
 * 1 <= nums[i] <= 100
 *
 * Maximum total sum:
 *
 *      200 * 100 = 20,000
 *
 * Target subset:
 *
 *      <= 10,000
 *
 * ----------------------------------------------------------------------------
 * Representative Examples
 * ----------------------------------------------------------------------------
 *
 * Example 1
 *
 * nums = [1,5,11,5]
 *
 * total = 22
 * target = 11
 *
 * 11 can be formed:
 *
 *      [11]
 *
 * Remaining:
 *
 *      [1,5,5]
 *
 * Output:
 * true
 *
 * ------------------------------------------------------------
 *
 * Example 2
 *
 * nums = [1,2,3,5]
 *
 * total = 11
 *
 * Odd total.
 *
 * Impossible.
 *
 * Output:
 * false
 *
 * ------------------------------------------------------------
 *
 * Example 3
 *
 * nums = [2,2,3,5]
 *
 * total = 12
 * target = 6
 *
 * Possible sums:
 *
 * 2
 * 4
 * 5
 * 7
 * 8
 * 9
 * 10
 * 12
 *
 * 6 never appears.
 *
 * Output:
 * false
 *
 * ============================================================================
 * 🔵 3. CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern
 *
 *      0/1 Knapsack
 *
 * Also known as
 *
 *      Bounded Knapsack
 *      Limited Copy Knapsack
 *      Subset Sum DP
 *
 * ----------------------------------------------------------------------------
 * Archetype
 * ----------------------------------------------------------------------------
 *
 * We process items one by one.
 *
 * Every item has exactly two choices:
 *
 *      Take it.
 *      Skip it.
 *
 * But once taken,
 * it disappears forever.
 *
 * ----------------------------------------------------------------------------
 * Core Invariant
 * ----------------------------------------------------------------------------
 *
 * After processing the first k numbers,
 *
 * dp[s] == true
 *
 * means
 *
 *      Sum s is achievable
 *      using ONLY those processed numbers.
 *
 * Nothing from the future has been used.
 *
 * Every processed number contributes at most once.
 *
 * ----------------------------------------------------------------------------
 * Why It Works
 * ----------------------------------------------------------------------------
 *
 * Every transition either:
 *
 *      ignores current number
 *
 * or
 *
 *      consumes current number exactly once.
 *
 * Therefore every reachable state corresponds to
 * a valid subset.
 *
 * ----------------------------------------------------------------------------
 * Recognition Signals
 * ----------------------------------------------------------------------------
 *
 * Look for phrases like:
 *
 *      choose
 *      select
 *      subset
 *      exactly once
 *      each item once
 *      split array
 *      partition
 *      target sum
 *
 * These almost always indicate
 * 0/1 Knapsack.
 *
 * ----------------------------------------------------------------------------
 * When To Use
 * ----------------------------------------------------------------------------
 *
 * ✔ each element usable once
 *
 * ✔ subset formation
 *
 * ✔ target sum
 *
 * ✔ feasibility question
 *
 * ✔ boolean DP
 *
 * ----------------------------------------------------------------------------
 * When NOT To Use
 * ----------------------------------------------------------------------------
 *
 * Do NOT use this pattern when
 * elements may be reused infinitely.
 *
 * Example:
 *
 * Coin Change II
 *
 * because one denomination may appear
 * unlimited times.
 *
 * ----------------------------------------------------------------------------
 * Comparison With Similar Patterns
 * ----------------------------------------------------------------------------
 *
 * 0/1 Knapsack (This Problem)
 *
 *      Each item used once.
 *
 * Transition:
 *
 *      dp[i-1][sum-weight]
 *
 * or in 1-D:
 *
 *      iterate sum RIGHT -> LEFT
 *
 * ------------------------------------------------------------
 *
 * Unbounded Knapsack
 *
 * Coin Change II
 *
 * Unlimited reuse.
 *
 * Transition:
 *
 *      dp[i][sum-weight]
 *
 * or in 1-D:
 *
 *      iterate LEFT -> RIGHT
 *
 * ------------------------------------------------------------
 *
 * The entire interview usually reduces to remembering:
 *
 *      Can I reuse an item?
 *
 * YES
 *      forward iteration
 *
 * NO
 *      backward iteration
 *
 * ============================================================================
 * 🟢 4. MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * Mental Model
 * ----------------------------------------------------------------------------
 *
 * Imagine constructing reachable sums.
 *
 * Initially only
 *
 *      sum = 0
 *
 * is reachable.
 *
 * Every new number expands the reachable frontier.
 *
 * But because every number is disposable,
 * it may contribute only once.
 *
 * ----------------------------------------------------------------------------
 * Primary Invariant
 * ----------------------------------------------------------------------------
 *
 * Before processing current number:
 *
 * dp[s]
 *
 * represents reachability WITHOUT using
 * the current number.
 *
 * After processing current number:
 *
 * dp[s]
 *
 * represents reachability AFTER considering it once.
 *
 * ----------------------------------------------------------------------------
 * Why Reverse Iteration Is Mandatory
 * ----------------------------------------------------------------------------
 *
 * Suppose
 *
 * nums = [1]
 *
 * target = 4
 *
 * Initially
 *
 * dp[0] = true
 *
 * Forward update:
 *
 * dp[1] = dp[0]
 *
 * becomes true.
 *
 * Immediately afterwards,
 *
 * dp[2] uses newly updated dp[1].
 *
 * That means the same number 1
 * has already been reused.
 *
 * Then
 *
 * dp[3]
 *
 * reuses it again.
 *
 * Then
 *
 * dp[4]
 *
 * reuses it again.
 *
 * One element magically became four elements.
 *
 * Entire invariant destroyed.
 *
 * ----------------------------------------------------------------------------
 *
 * Reverse update prevents this.
 *
 * When computing
 *
 * dp[s]
 *
 * the state
 *
 * dp[s-num]
 *
 * still belongs to the PREVIOUS iteration,
 * meaning current number has never been used.
 *
 * Therefore adding current number
 * consumes it exactly once.
 *
 * ----------------------------------------------------------------------------
 * Variable Meanings
 * ----------------------------------------------------------------------------
 *
 * target
 *
 *      Required subset sum.
 *
 * num
 *
 *      Current element.
 *
 * dp[s]
 *
 *      Reachability of sum s after processing
 *      processed elements.
 *
 * ----------------------------------------------------------------------------
 * Allowed Transition
 * ----------------------------------------------------------------------------
 *
 * Skip:
 *
 *      dp[s]
 *
 * remains true.
 *
 * Take:
 *
 *      dp[s-num]
 *
 * makes
 *
 *      dp[s]
 *
 * true.
 *
 * ----------------------------------------------------------------------------
 * Forbidden Transition
 * ----------------------------------------------------------------------------
 *
 * Reading a value already updated
 * during the same item iteration.
 *
 * That effectively means:
 *
 * "Current number has already been taken,
 * let's take it again."
 *
 * That is Unbounded Knapsack,
 * not this problem.
 *
 * ----------------------------------------------------------------------------
 * Loop Invariant
 * ----------------------------------------------------------------------------
 *
 * Before processing num:
 *
 * every reachable state was built without num.
 *
 * After finishing reverse traversal:
 *
 * every newly reachable state contains num
 * at most once.
 *
 * ----------------------------------------------------------------------------
 * Termination
 * ----------------------------------------------------------------------------
 *
 * After every element has been processed,
 *
 * dp[target]
 *
 * precisely answers whether target
 * is reachable.
 *
 * ----------------------------------------------------------------------------
 * Correctness Intuition
 * ----------------------------------------------------------------------------
 *
 * Every subset corresponds to one unique sequence
 * of take/skip decisions.
 *
 * DP enumerates those decisions compactly
 * without exponential recursion.
 *
 * Since every transition respects the invariant,
 * no invalid subset is ever produced,
 * and every valid subset eventually appears.
 *
 * ----------------------------------------------------------------------------
 * Why Naive Solutions Fail
 * ----------------------------------------------------------------------------
 *
 * Brute force explores
 *
 *      2^n
 *
 * subsets.
 *
 * n can reach 200.
 *
 * That is astronomically large.
 *
 * DP compresses identical states
 * into reachable sums,
 * reducing complexity to
 *
 *      O(n × target)
 *
 * instead of
 *
 *      O(2^n).
 *
 * ============================================================================
 * 🔴 5. WHY WRONG SOLUTIONS FAIL
 * ============================================================================
 *
 * Mistake 1
 * ----------------------------------------------------------------------------
 *
 * Updating DP from left to right.
 *
 * Looks reasonable because
 * many DP problems iterate forward.
 *
 * Violated Invariant:
 *
 * Current item becomes reusable.
 *
 * Counterexample:
 *
 * nums = [1,2,5]
 *
 * target = 4
 *
 * Forward iteration falsely creates:
 *
 * 1
 * 2
 * 3
 * 4
 *
 * using the single value 1 repeatedly.
 *
 * Correct answer:
 *
 * false.
 *
 * Forward DP incorrectly returns true.
 *
 * ----------------------------------------------------------------------------
 *
 * Mistake 2
 * ----------------------------------------------------------------------------
 *
 * Swapping loop order:
 *
 * for(sum)
 *     for(num)
 *
 * This destroys the chronological meaning
 * of processed items.
 *
 * dp no longer represents
 * "after first k elements".
 *
 * The processed-prefix invariant disappears.
 *
 * Example:
 *
 * nums = [3,3,3,4,5]
 *
 * target = 9
 *
 * States that should be created gradually
 * never become available at the correct time.
 *
 * Reachability propagation breaks.
 *
 * ----------------------------------------------------------------------------
 *
 * Mistake 3
 * ----------------------------------------------------------------------------
 *
 * Forgetting odd-total check.
 *
 * If total is odd,
 * equal partition is mathematically impossible.
 *
 * Running DP wastes time unnecessarily.
 *
 * ----------------------------------------------------------------------------
 *
 * Mistake 4
 * ----------------------------------------------------------------------------
 *
 * Thinking this is Coin Change II.
 *
 * They look almost identical.
 *
 * Critical difference:
 *
 * Coin Change II
 * allows unlimited reuse.
 *
 * Partition Equal Subset Sum
 * allows one copy only.
 *
 * The recurrence therefore changes from
 *
 *      dp[i][...]
 *
 * to
 *
 *      dp[i-1][...]
 *
 * which becomes
 *
 * reverse iteration
 *
 * in the optimized 1-D DP.
 *
 * ============================================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================================
 *
 * Typing Order
 * ----------------------------------------------------------------------------
 *
 * 1. Compute total sum.
 *
 * 2. If total is odd:
 *
 *        return false.
 *
 * 3. target = total / 2.
 *
 * 4. boolean dp[target+1]
 *
 * 5. dp[0] = true.
 *
 * 6. For every number:
 *
 *        iterate sum from target down to number
 *
 *        dp[sum] |= dp[sum-number]
 *
 * 7. Return dp[target].
 *
 * ----------------------------------------------------------------------------
 * Function Skeleton
 * ----------------------------------------------------------------------------
 *
 * canPartition(nums)
 *
 * total
 *
 * odd?
 *
 * target
 *
 * dp
 *
 * process numbers
 *
 * return dp[target]
 *
 * ----------------------------------------------------------------------------
 * Transition
 * ----------------------------------------------------------------------------
 *
 * dp[sum]
 *
 * =
 *
 * dp[sum]
 *
 * OR
 *
 * dp[sum-num]
 *
 * ----------------------------------------------------------------------------
 * Pointer Movement
 * ----------------------------------------------------------------------------
 *
 * sum
 *
 * target
 * ↓
 * ...
 * num
 *
 * Always decreasing.
 *
 * Never increasing.
 *
 * ----------------------------------------------------------------------------
 * Return
 * ----------------------------------------------------------------------------
 *
 * dp[target]
 *
 * ============================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================================
 *
 * total
 *
 * odd -> false
 *
 * target
 *
 * dp[0]=true
 *
 * for each number
 *     for sum descending
 *         reachable |= previous
 *
 * return target reachable
 */
public class PartitionEqualSubsetSum {

    /**
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
     * For every element we make exactly one binary decision:
     *
     *      Take
     *      Skip
     *
     * Instead of directly constructing two subsets, we search for one subset
     * whose sum equals target = total / 2.
     *
     * -------------------------------------------------------------------------
     * State
     * -------------------------------------------------------------------------
     *
     * index
     * currentSum
     *
     * -------------------------------------------------------------------------
     * Invariant
     * -------------------------------------------------------------------------
     *
     * At recursion level index,
     * currentSum equals the sum formed using only elements in
     * nums[0 ... index-1].
     *
     * Future elements have never been used.
     *
     * -------------------------------------------------------------------------
     * Limitation
     * -------------------------------------------------------------------------
     *
     * Every element doubles the search tree.
     *
     * Complexity becomes exponential.
     *
     * -------------------------------------------------------------------------
     * Complexity
     * -------------------------------------------------------------------------
     *
     * Time:
     * O(2^n)
     *
     * Space:
     * O(n)
     *
     * recursion depth.
     *
     * -------------------------------------------------------------------------
     * Interview Usefulness
     * -------------------------------------------------------------------------
     *
     * Excellent starting point.
     *
     * Demonstrates:
     *
     *  • binary decision tree
     *  • subset formulation
     *  • transition to memoization
     *  • transition to knapsack DP
     */
    static class BruteForce {

        boolean canPartition(int[] nums) {

            int total = 0;

            for (int num : nums) {
                total += num;
            }

            if ((total & 1) == 1) {
                return false;
            }

            return dfs(nums, 0, total / 2);
        }

        private boolean dfs(int[] nums, int index, int remaining) {

            // 🟢 Invariant:
            // remaining is still required using unused suffix elements.

            if (remaining == 0) {
                return true;
            }

            if (index == nums.length) {
                return false;
            }

            if (remaining < 0) {
                return false;
            }

            // Skip current element.

            if (dfs(nums, index + 1, remaining)) {
                return true;
            }

            // Take current element exactly once.

            return dfs(nums, index + 1, remaining - nums[index]);
        }
    }

    /**
     * -------------------------------------------------------------------------
     * Improved
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     *
     * The brute force repeatedly recomputes identical states.
     *
     * Example
     *
     * dfs(10,37)
     *
     * may be reached through many different decision paths.
     *
     * We cache:
     *
     *      (index, remaining)
     *
     * -------------------------------------------------------------------------
     * State
     * -------------------------------------------------------------------------
     *
     * index
     *
     * remaining target
     *
     * -------------------------------------------------------------------------
     * Invariant
     * -------------------------------------------------------------------------
     *
     * memo[index][remaining]
     *
     * permanently stores whether that suffix can complete the remaining sum.
     *
     * Once computed,
     * it never changes.
     *
     * -------------------------------------------------------------------------
     * Improvement
     * -------------------------------------------------------------------------
     *
     * Exponential duplicate exploration disappears.
     *
     * Every state computed once.
     *
     * -------------------------------------------------------------------------
     * Complexity
     * -------------------------------------------------------------------------
     *
     * Time
     *
     * O(n × target)
     *
     * Space
     *
     * O(n × target)
     *
     * -------------------------------------------------------------------------
     * Interview Usefulness
     * -------------------------------------------------------------------------
     *
     * Great bridge from recursion
     * to bottom-up DP.
     */
    static class Memoized {

        private Boolean[][] memo;

        boolean canPartition(int[] nums) {

            int total = 0;

            for (int num : nums) {
                total += num;
            }

            if ((total & 1) == 1) {
                return false;
            }

            int target = total / 2;

            memo = new Boolean[nums.length][target + 1];

            return dfs(nums, 0, target);
        }

        private boolean dfs(int[] nums,
                            int index,
                            int remaining) {

            if (remaining == 0) {
                return true;
            }

            if (index == nums.length) {
                return false;
            }

            if (remaining < 0) {
                return false;
            }

            if (memo[index][remaining] != null) {
                return memo[index][remaining];
            }

            boolean skip = dfs(nums,
                    index + 1,
                    remaining);

            boolean take = dfs(nums,
                    index + 1,
                    remaining - nums[index]);

            memo[index][remaining] = skip || take;

            return memo[index][remaining];
        }
    }

    /**
     * -------------------------------------------------------------------------
     * Optimal (Interview Preferred)
     * -------------------------------------------------------------------------
     *
     * Pattern
     * -------
     *
     * 0/1 Knapsack
     *
     * Boolean Reachability DP
     *
     * -------------------------------------------------------------------------
     * Core Invariant
     * -------------------------------------------------------------------------
     *
     * After processing the first k numbers,
     *
     * dp[s]
     *
     * answers:
     *
     * "Can sum s be formed using only those k numbers?"
     *
     * Every processed number contributes at most once.
     *
     * -------------------------------------------------------------------------
     * Search Space
     * -------------------------------------------------------------------------
     *
     * Reachable subset sums.
     *
     * 0
     * ...
     * target
     *
     * -------------------------------------------------------------------------
     * Transition
     * -------------------------------------------------------------------------
     *
     * Skip current number:
     *
     * dp[s]
     *
     * remains true.
     *
     * Take current number:
     *
     * dp[s-num]
     *
     * implies
     *
     * dp[s]
     *
     * -------------------------------------------------------------------------
     * Why Reverse Iteration?
     * -------------------------------------------------------------------------
     *
     * This is the single most important implementation detail.
     *
     * Reverse traversal guarantees:
     *
     * dp[s-num]
     *
     * still belongs to the previous processed prefix.
     *
     * Therefore the current element has not yet been consumed.
     *
     * Every element enters the subset at most once.
     *
     * -------------------------------------------------------------------------
     * Complexity
     * -------------------------------------------------------------------------
     *
     * Time
     *
     * O(n × target)
     *
     * Space
     *
     * O(target)
     *
     * -------------------------------------------------------------------------
     * Interview Usefulness
     * -------------------------------------------------------------------------
     *
     * This is the expected solution.
     *
     * It demonstrates:
     *
     *  • 0/1 knapsack recognition
     *  • DP optimization
     *  • invariant-driven implementation
     *  • space optimization
     */
    static class Optimal {

        boolean canPartition(int[] nums) {

            int total = 0;

            for (int num : nums) {
                total += num;
            }

            // 🔴 Odd total can never be split equally.
            if ((total & 1) == 1) {
                return false;
            }

            int target = total / 2;

            boolean[] dp = new boolean[target + 1];

            // 🟢 Empty subset always creates sum zero.
            dp[0] = true;

            for (int num : nums) {

                // 🟢 Reverse traversal prevents reusing current element.
                for (int sum = target; sum >= num; sum--) {

                    // 🟢 Invariant:
                    // dp[sum-num] belongs to the previous processed prefix.
                    dp[sum] = dp[sum] || dp[sum - num];
                }
            }

            return dp[target];
        }
    }

/**
 * =========================================================================
 * Deep Dive:
 * Why Reverse Iteration Is Correct
 * =========================================================================
 *
 * Consider:
 *
 * nums = [1,2,5]
 *
 * target = 4
 *
 * Initially
 *
 * dp
 *
 * index:
 *
 * 0 1 2 3 4
 *
 * T F F F F
 *
 * ------------------------------------------------------------
 *
 * Process number = 1
 *
 * RIGHT -> LEFT
 *
 * sum = 4
 *
 * dp[4]
 * |=
 * dp[3]
 *
 * dp[3] is still FALSE.
 *
 * No change.
 *
 * sum = 3
 *
 * dp[3]
 * |=
 * dp[2]
 *
 * Still FALSE.
 *
 * sum = 2
 *
 * dp[2]
 * |=
 * dp[1]
 *
 * Still FALSE.
 *
 * sum = 1
 *
 * dp[1]
 * |=
 * dp[0]
 *
 * becomes TRUE.
 *
 * Final:
 *
 * T T F F F
 *
 * Notice:
 *
 * dp[2]
 *
 * never observed the newly-created dp[1].
 *
 * Therefore number 1
 * entered exactly once.
 */

/**
 * =========================================================================
 * Reverse vs Forward Iteration (Invariant Proof)
 * =========================================================================
 *
 * Now repeat the same example using FORWARD iteration.
 *
 * nums = [1,2,5]
 *
 * target = 4
 *
 * Initial DP
 *
 * Sum : 0 1 2 3 4
 * DP  : T F F F F
 *
 * -------------------------------------------------------------------------
 * Processing number = 1
 * -------------------------------------------------------------------------
 *
 * sum = 1
 *
 * dp[1] |= dp[0]
 *
 * DP
 *
 * T T F F F
 *
 * --------------------------------------------------
 *
 * sum = 2
 *
 * dp[2] |= dp[1]
 *
 * But dp[1] was JUST updated.
 *
 * Therefore we have unknowingly used
 * number 1 twice.
 *
 * DP
 *
 * T T T F F
 *
 * --------------------------------------------------
 *
 * sum = 3
 *
 * dp[3] |= dp[2]
 *
 * dp[2] already contains the same 1.
 *
 * Number 1 has now been used
 * three times.
 *
 * DP
 *
 * T T T T F
 *
 * --------------------------------------------------
 *
 * sum = 4
 *
 * dp[4] |= dp[3]
 *
 * Number 1 has now magically appeared
 * four times.
 *
 * DP
 *
 * T T T T T
 *
 * Completely impossible.
 *
 * -------------------------------------------------------------------------
 * Invariant Violation
 * -------------------------------------------------------------------------
 *
 * dp[sum-num]
 *
 * no longer belongs to the previous processed prefix.
 *
 * It already includes the current number.
 *
 * Therefore
 *
 * current number
 * ->
 * current number again
 * ->
 * current number again
 *
 * which is exactly Unbounded Knapsack.
 *
 * =========================================================================
 * Why Swapping Loop Order Fails
 * =========================================================================
 *
 * Incorrect code
 *
 * for (sum = target ...)
 *     for (num : nums)
 *
 * Instead of
 *
 * for (num : nums)
 *     for (sum ...)
 *
 * -------------------------------------------------------------------------
 * Lost Meaning
 * -------------------------------------------------------------------------
 *
 * Our invariant is:
 *
 * "After processing first k numbers..."
 *
 * If sum becomes the outer loop,
 * there is no notion of processed prefix.
 *
 * Different numbers update the same DP row
 * during the same iteration.
 *
 * Chronological ordering disappears.
 *
 * DP loses its interpretation.
 *
 * -------------------------------------------------------------------------
 * Example
 * -------------------------------------------------------------------------
 *
 * nums
 *
 * [3,3,3,4,5]
 *
 * target = 9
 *
 * Correct answer:
 *
 * true
 *
 * because
 *
 * 3+3+3
 *
 * -------------------------------------------------------------------------
 *
 * Correct ordering
 *
 * Number
 * ↓
 *
 * DP gradually expands:
 *
 * after first 3
 *
 * 0
 * 3
 *
 * after second 3
 *
 * 0
 * 3
 * 6
 *
 * after third 3
 *
 * 0
 * 3
 * 6
 * 9
 *
 * Success.
 *
 * -------------------------------------------------------------------------
 *
 * Swapped loops instead ask:
 *
 * Can 9 be built
 * by immediately inspecting every number?
 *
 * Every lookup still depends on
 * states that have not yet been produced.
 *
 * Propagation never happens correctly.
 *
 * =========================================================================
 * Relationship with Classical 2-D DP
 * =========================================================================
 *
 * Let
 *
 * dp[i][s]
 *
 * mean
 *
 * Can first i elements produce sum s?
 *
 * -------------------------------------------------------------------------
 * Transition
 * -------------------------------------------------------------------------
 *
 * Skip
 *
 * dp[i][s]
 * =
 * dp[i-1][s]
 *
 * --------------------------------------------------
 *
 * Take
 *
 * dp[i][s]
 * =
 * dp[i-1][s-num]
 *
 * because current element has not yet
 * been used.
 *
 * -------------------------------------------------------------------------
 * Final Transition
 * -------------------------------------------------------------------------
 *
 * dp[i][s]
 * =
 *
 * dp[i-1][s]
 *
 * OR
 *
 * dp[i-1][s-num]
 *
 * -------------------------------------------------------------------------
 * Space Optimization
 * -------------------------------------------------------------------------
 *
 * Observe carefully.
 *
 * Row i
 *
 * depends ONLY on
 *
 * row i-1.
 *
 * Therefore
 *
 * we may collapse
 *
 * N rows
 *
 * into one row.
 *
 * Reverse traversal guarantees
 * that every lookup still behaves
 * like reading row i-1.
 *
 * =========================================================================
 * Bounded vs Unbounded Knapsack
 * =========================================================================
 *
 * This interview question is often paired with
 * Coin Change II.
 *
 * They differ in only ONE idea.
 *
 * -------------------------------------------------------------------------
 * Partition Equal Subset Sum
 * -------------------------------------------------------------------------
 *
 * Every element
 *
 * may be used once.
 *
 * Transition
 *
 * dp[i-1][...]
 *
 * 1-D optimization
 *
 * RIGHT
 * →
 * LEFT
 *
 * -------------------------------------------------------------------------
 * Coin Change II
 * -------------------------------------------------------------------------
 *
 * Every denomination
 *
 * may be reused forever.
 *
 * Transition
 *
 * dp[i][...]
 *
 * 1-D optimization
 *
 * LEFT
 * →
 * RIGHT
 *
 * -------------------------------------------------------------------------
 * Memory Trick
 * -------------------------------------------------------------------------
 *
 * Ask one question.
 *
 * "After taking this item,
 * may I immediately take it again?"
 *
 * YES
 *
 * Stay in same DP row.
 *
 * Forward iteration.
 *
 * --------------------------------------------------
 *
 * NO
 *
 * Must remain in previous row.
 *
 * Reverse iteration.
 *
 * =========================================================================
 * Interview Debugging Checklist
 * =========================================================================
 *
 * If the answer is unexpectedly TRUE,
 * check:
 *
 * ✓ Did I iterate forward?
 *
 * ✓ Did I accidentally reuse an element?
 *
 * ✓ Did I initialize dp[0]=true?
 *
 * ✓ Did I compute target correctly?
 *
 * ✓ Did I reject odd totals?
 *
 * -------------------------------------------------------------------------
 *
 * If the answer is unexpectedly FALSE,
 * check:
 *
 * ✓ Reverse loop bounds.
 *
 * ✓ sum >= num.
 *
 * ✓ OR transition.
 *
 * ✓ target = total / 2.
 *
 * ✓ Total sum calculation.
 *
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * A strong interview explanation:
 *
 * "The problem reduces to finding whether half of the total sum can be
 * formed. Since every number can be chosen at most once, this is a classic
 * 0/1 Knapsack problem. The invariant is that after processing the first k
 * numbers, dp[s] indicates whether sum s is reachable using only those
 * numbers. Reverse iteration is essential because it prevents the current
 * element from being reused within the same iteration, preserving the
 * bounded-knapsack invariant."
 *
 * -------------------------------------------------------------------------
 * Correctness
 * -------------------------------------------------------------------------
 *
 * Every transition either:
 *
 * • skips current element
 *
 * or
 *
 * • takes it exactly once.
 *
 * Since every valid subset can be represented by one sequence of such
 * decisions, and every transition preserves the invariant, dp[target]
 * is correct.
 *
 * -------------------------------------------------------------------------
 * Discard Rule
 * -------------------------------------------------------------------------
 *
 * None.
 *
 * Unlike binary search,
 * dynamic programming explores reachable states instead of discarding
 * search space.
 *
 * -------------------------------------------------------------------------
 * Termination
 * -------------------------------------------------------------------------
 *
 * Every element is processed exactly once.
 *
 * Every reachable sum is updated once per element.
 *
 * After the final element,
 * dp[target] represents the complete search space.
 *
 * -------------------------------------------------------------------------
 * In-place Feasibility
 * -------------------------------------------------------------------------
 *
 * Yes.
 *
 * Reverse traversal safely compresses
 * the 2-D table into one row.
 *
 * -------------------------------------------------------------------------
 * Streaming Feasibility
 * -------------------------------------------------------------------------
 *
 * Yes.
 *
 * Incoming numbers can continue updating
 * the same DP array,
 * although removing numbers is not supported
 * without recomputation.
 */

/**
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 * -------------------------------------------------------------------------
 *
 * ✓ Subset
 * ✓ Equal Partition
 * ✓ Target Sum
 * ✓ Every element usable once
 *
 * Think immediately:
 *
 *      0/1 Knapsack
 *
 * -------------------------------------------------------------------------
 * Pattern
 * -------------------------------------------------------------------------
 *
 * Bounded Knapsack
 *
 * -------------------------------------------------------------------------
 * Search Space
 * -------------------------------------------------------------------------
 *
 * Reachable subset sums
 *
 * 0
 * ...
 * target
 *
 * -------------------------------------------------------------------------
 * State
 * -------------------------------------------------------------------------
 *
 * dp[s]
 *
 * =
 *
 * Is sum s reachable?
 *
 * -------------------------------------------------------------------------
 * Invariant
 * -------------------------------------------------------------------------
 *
 * After processing first k numbers,
 *
 * dp[s]
 *
 * only uses those k numbers.
 *
 * Each processed element appears
 * at most once.
 *
 * -------------------------------------------------------------------------
 * Transition
 * -------------------------------------------------------------------------
 *
 * dp[s]
 *
 * =
 *
 * dp[s]
 *
 * OR
 *
 * dp[s-num]
 *
 * -------------------------------------------------------------------------
 * Loop Direction
 * -------------------------------------------------------------------------
 *
 * RIGHT
 * →
 * LEFT
 *
 * Never forward.
 *
 * -------------------------------------------------------------------------
 * Common Trap
 * -------------------------------------------------------------------------
 *
 * Forward iteration
 *
 * =
 *
 * accidental reuse
 *
 * =
 *
 * unbounded knapsack.
 *
 * -------------------------------------------------------------------------
 * Edge Cases
 * -------------------------------------------------------------------------
 *
 * ✓ Odd total
 *
 * ✓ Single element
 *
 * ✓ Large element
 *
 * ✓ Duplicate numbers
 *
 * ✓ target = 0
 *
 * -------------------------------------------------------------------------
 * Complexity
 * -------------------------------------------------------------------------
 *
 * Time
 *
 * O(n × target)
 *
 * Space
 *
 * O(target)
 *
 * -------------------------------------------------------------------------
 * One-Liner
 * -------------------------------------------------------------------------
 *
 * Reachable subset sums updated
 * in reverse
 * so every element is consumed once.
 *
 * -------------------------------------------------------------------------
 * Re-derivation Cue
 * -------------------------------------------------------------------------
 *
 * Can I reuse an element?
 *
 * NO
 *
 * →
 *
 * Reverse DP.
 *
 * =========================================================================
 * 🔄 VARIATIONS & TWEAKS
 * =========================================================================
 *
 * -------------------------------------------------------------------------
 * Variation 1
 * Target Sum
 * -------------------------------------------------------------------------
 *
 * Instead of asking:
 *
 * Can target be reached?
 *
 * We ask:
 *
 * How many sign assignments
 * produce target?
 *
 * Same subset transformation,
 * different DP value.
 *
 * -------------------------------------------------------------------------
 * Variation 2
 * Subset Sum
 * -------------------------------------------------------------------------
 *
 * Exactly the same DP.
 *
 * Only target changes.
 *
 * -------------------------------------------------------------------------
 * Variation 3
 * Count Subsets
 * -------------------------------------------------------------------------
 *
 * DP stores counts
 * instead of booleans.
 *
 * Transition changes from
 *
 * OR
 *
 * to
 *
 * addition.
 *
 * -------------------------------------------------------------------------
 * Variation 4
 * Minimum Difference Partition
 * -------------------------------------------------------------------------
 *
 * Compute every reachable sum.
 *
 * Pick reachable sum
 * closest to total/2.
 *
 * -------------------------------------------------------------------------
 * Variation 5
 * K Equal Partition
 * -------------------------------------------------------------------------
 *
 * This DP no longer scales.
 *
 * Usually solved using
 *
 * backtracking
 * +
 * pruning
 * +
 * bitmask.
 *
 * Pattern boundary reached.
 *
 * -------------------------------------------------------------------------
 * Variation 6
 * Coin Change II
 * -------------------------------------------------------------------------
 *
 * Same state.
 *
 * Different invariant.
 *
 * Unlimited reuse.
 *
 * Therefore
 *
 * LEFT
 * →
 * RIGHT.
 *
 * -------------------------------------------------------------------------
 * Variation 7
 * 2-D DP
 * -------------------------------------------------------------------------
 *
 * Easier to understand.
 *
 * Harder to optimize.
 *
 * Reverse traversal is simply
 * row compression.
 *
 * =========================================================================
 * ⚫ Pattern Mapping
 * =========================================================================
 *
 * Problem
 * --------------------------------------------
 * Partition Equal Subset Sum
 *
 * Pattern
 * --------------------------------------------
 * 0/1 Knapsack
 *
 * --------------------------------------------
 * Subset Sum
 *
 * Pattern
 * --------------------------------------------
 * 0/1 Knapsack
 *
 * --------------------------------------------
 * Last Stone Weight II
 *
 * Pattern
 * --------------------------------------------
 * 0/1 Knapsack
 *
 * --------------------------------------------
 * Target Sum
 *
 * Pattern
 * --------------------------------------------
 * Subset Transformation
 *
 * --------------------------------------------
 * Coin Change II
 *
 * Pattern
 * --------------------------------------------
 * Unbounded Knapsack
 *
 * --------------------------------------------
 * Coin Change
 *
 * Pattern
 * --------------------------------------------
 * Unbounded Knapsack
 *
 * --------------------------------------------
 * Perfect Squares
 *
 * Pattern
 * --------------------------------------------
 * Unbounded Knapsack
 *
 * =========================================================================
 * 🧠 MASTERY CHECKLIST
 * =========================================================================
 *
 * □ I know why the problem becomes subset sum.
 *
 * □ I know why odd total immediately fails.
 *
 * □ I know the DP state.
 *
 * □ I know the invariant.
 *
 * □ I know why reverse traversal is mandatory.
 *
 * □ I know why forward traversal becomes
 *   unbounded knapsack.
 *
 * □ I know why swapping loops breaks
 *   chronological processing.
 *
 * □ I can derive the transition
 *   without memorizing it.
 *
 * □ I know why
 *
 * dp[s] |= dp[s-num]
 *
 * works.
 *
 * □ I know how 2-D DP compresses into
 * one row.
 *
 * □ I know the complexity.
 *
 * □ I can explain correctness.
 *
 * □ I know the difference from
 * Coin Change II.
 *
 * =========================================================================
 * Quick Reference
 * =========================================================================
 *
 * State
 *
 * dp[s]
 *
 * Meaning
 *
 * Reachability.
 *
 * Initialization
 *
 * dp[0]=true
 *
 * Transition
 *
 * dp[s] |= dp[s-num]
 *
 * Loop
 *
 * target
 * ↓
 * num
 *
 * Answer
 *
 * dp[target]
 */

    /**
     * =========================================================================
     * Additional Correctness Notes
     * =========================================================================
     *
     * Formal Correctness Argument
     * -------------------------------------------------------------------------
     *
     * We prove the algorithm by induction over processed elements.
     *
     * -------------------------------------------------------------------------
     * Base Case
     * -------------------------------------------------------------------------
     *
     * Before processing any element:
     *
     * dp[0] = true
     *
     * because the empty subset forms sum 0.
     *
     * Every other sum is unreachable.
     *
     * Therefore the invariant holds.
     *
     * -------------------------------------------------------------------------
     * Induction Hypothesis
     * -------------------------------------------------------------------------
     *
     * Assume after processing the first k elements:
     *
     * dp[s]
     *
     * correctly represents whether sum s is reachable using only those k
     * elements.
     *
     * -------------------------------------------------------------------------
     * Induction Step
     * -------------------------------------------------------------------------
     *
     * Process element x.
     *
     * Every reachable sum after processing x must be produced by exactly one
     * of two possibilities:
     *
     * 1.
     * Skip x.
     *
     * The previous reachability remains valid.
     *
     * 2.
     * Take x.
     *
     * Then the remaining sum
     *
     * s - x
     *
     * must already have been reachable before x was processed.
     *
     * Reverse iteration guarantees that
     *
     * dp[s - x]
     *
     * still represents the previous processed prefix.
     *
     * Therefore x is introduced exactly once.
     *
     * Hence the invariant continues to hold.
     *
     * By induction,
     * after every element is processed,
     *
     * dp[target]
     *
     * is correct.
     *
     * =========================================================================
     * Frequently Asked Interview Questions
     * =========================================================================
     *
     * Q.
     * Why does dp[0] start as true?
     *
     * A.
     * Because an empty subset always exists.
     *
     * It is the seed from which every reachable sum grows.
     *
     * -------------------------------------------------------------------------
     *
     * Q.
     * Why not initialize every value as false?
     *
     * A.
     * Then no transition could ever become true because every update depends on
     * an already reachable state.
     *
     * -------------------------------------------------------------------------
     *
     * Q.
     * Why do duplicates not cause problems?
     *
     * A.
     * Every occurrence is processed independently.
     *
     * Reverse traversal ensures each occurrence contributes at most once.
     *
     * -------------------------------------------------------------------------
     *
     * Q.
     * Can the algorithm reconstruct the chosen subset?
     *
     * A.
     * Not with the compressed 1-D DP alone.
     *
     * Either:
     *
     * • keep a full 2-D parent table
     *
     * or
     *
     * • store predecessor information.
     *
     * -------------------------------------------------------------------------
     *
     * Q.
     * Can this DP minimize or maximize values?
     *
     * A.
     * Yes.
     *
     * Replace boolean states with numeric states while preserving the same
     * bounded-knapsack dependency.
     *
     * =========================================================================
     * Common Interview Pitfalls
     * =========================================================================
     *
     * Pitfall:
     *
     * "Reverse iteration is only an optimization."
     *
     * Wrong.
     *
     * Reverse iteration is part of the correctness proof.
     *
     * -------------------------------------------------------------------------
     *
     * Pitfall:
     *
     * "Forward iteration is faster."
     *
     * Wrong.
     *
     * Complexity is identical.
     *
     * Only the invariant changes.
     *
     * -------------------------------------------------------------------------
     *
     * Pitfall:
     *
     * "Loop order is interchangeable."
     *
     * Wrong.
     *
     * The outer loop establishes chronological processing of items.
     *
     * Removing that ordering destroys the DP interpretation.
     *
     * -------------------------------------------------------------------------
     *
     * Pitfall:
     *
     * "The recurrence was memorized."
     *
     * Better answer:
     *
     * Derive it from the invariant:
     *
     * Either the current element participates,
     * or it does not.
     *
     * =========================================================================
     * Mechanical Reconstruction Checklist
     * =========================================================================
     *
     * Step 1
     *
     * Compute total.
     *
     * -----------------------------------------
     *
     * Step 2
     *
     * Odd?
     *
     * Return false.
     *
     * -----------------------------------------
     *
     * Step 3
     *
     * target = total / 2.
     *
     * -----------------------------------------
     *
     * Step 4
     *
     * boolean dp[target + 1]
     *
     * -----------------------------------------
     *
     * Step 5
     *
     * dp[0] = true.
     *
     * -----------------------------------------
     *
     * Step 6
     *
     * for each number
     *
     * -----------------------------------------
     *
     * Step 7
     *
     * for sum from target down to number
     *
     * -----------------------------------------
     *
     * Step 8
     *
     * dp[sum] |= dp[sum - number]
     *
     * -----------------------------------------
     *
     * Step 9
     *
     * return dp[target]
     *
     * =========================================================================
     * Pattern Summary
     * =========================================================================
     *
     * Pattern
     *
     *      0/1 Knapsack
     *
     * State
     *
     *      Reachable sums.
     *
     * Invariant
     *
     *      Processed prefix only.
     *
     * Transition
     *
     *      Skip
     *      or
     *      Take once.
     *
     * Loop Direction
     *
     *      Descending.
     *
     * Complexity
     *
     *      O(n × target)
     *
     * Memory
     *
     *      O(target)
     *
     * Recognition Cue
     *
     *      "Choose each element at most once."
     *
     * =========================================================================
     * Self-Contained API
     * =========================================================================
     */

    public static boolean canPartition(int[] nums) {
        return new Optimal().canPartition(nums);
    }

    public static boolean canPartitionMemoized(int[] nums) {
        return new Memoized().canPartition(nums);
    }

    public static boolean canPartitionBruteForce(int[] nums) {
        return new BruteForce().canPartition(nums);
    }

    /**
     * =========================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * =========================================================================
     *
     * Run with assertions enabled:
     *
     *      java -ea PartitionEqualSubsetSum
     */

    public static void main(String[] args) {

        // Happy path from problem statement.
        assert canPartition(new int[]{1, 5, 11, 5});

        // Odd total can never be partitioned.
        assert !canPartition(new int[]{1, 2, 3, 5});

        // Representative false example.
        assert !canPartition(new int[]{2, 2, 3, 5});

        // Three threes form the target.
        assert canPartition(new int[]{3, 3, 3, 4, 5});

        // Smallest positive partition.
        assert canPartition(new int[]{1, 1});

        // Single element cannot split.
        assert !canPartition(new int[]{2});

        // Equal values.
        assert canPartition(new int[]{100, 100});

        // Duplicate values requiring independent usage.
        assert canPartition(new int[]{2, 2, 2, 2});

        // Target exists only by combining multiple elements.
        assert canPartition(new int[]{2, 3, 7, 8});

        // Large value dominates.
        assert !canPartition(new int[]{1, 2, 5});

        // Requires choosing non-adjacent elements.
        assert canPartition(new int[]{1, 2, 3, 4});

        // Verify all implementations agree.

        int[][] tests = {
                {1, 5, 11, 5},
                {1, 2, 3, 5},
                {2, 2, 3, 5},
                {3, 3, 3, 4, 5},
                {1, 1},
                {2},
                {100, 100},
                {2, 2, 2, 2},
                {2, 3, 7, 8},
                {1, 2, 5},
                {1, 2, 3, 4}
        };

        for (int[] test : tests) {

            boolean brute = canPartitionBruteForce(test);
            boolean memo = canPartitionMemoized(test);
            boolean optimal = canPartition(test);

            // Every implementation should compute the same answer.
            assert brute == memo;
            assert memo == optimal;
        }

        /*
         * Additional invariant sanity checks.
         */

        // Total is odd.
        assert !canPartition(new int[]{9, 1, 1});

        // Multiple identical numbers.
        assert canPartition(new int[]{4, 4, 4, 4});

        // Impossible although total is even.
        assert !canPartition(new int[]{8, 5, 3});

        // Entire target formed by one element.
        assert canPartition(new int[]{6, 1, 2, 3});

        // Multiple equivalent subsets exist.
        assert canPartition(new int[]{5, 5, 5, 5});

        System.out.println("All assertions passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/