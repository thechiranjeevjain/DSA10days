package org.chijai.day9.session2;

import java.util.*;

/**
 * ============================================================================
 * 📘 PRIMARY PROBLEM — LONGEST INCREASING SUBSEQUENCE
 * ============================================================================
 *
 * LeetCode: 300
 * Link:
 * https://leetcode.com/problems/longest-increasing-subsequence/
 *
 * Difficulty: Medium
 *
 * Tags:
 * Dynamic Programming
 * Binary Search
 * Greedy
 * Array
 *
 * ----------------------------------------------------------------------------
 * FULL OFFICIAL LEETCODE STATEMENT
 * ----------------------------------------------------------------------------
 *
 * Given an integer array nums, return the length of the longest strictly
 * increasing subsequence.
 *
 * A subsequence is a sequence that can be derived from an array by deleting
 * some or no elements without changing the order of the remaining elements.
 *
 * Example 1:
 *
 * Input: nums = [10,9,2,5,3,7,101,18]
 * Output: 4
 * Explanation:
 * The longest increasing subsequence is [2,3,7,101],
 * therefore the length is 4.
 *
 * Example 2:
 *
 * Input: nums = [0,1,0,3,2,3]
 * Output: 4
 *
 * Example 3:
 *
 * Input: nums = [7,7,7,7,7,7,7]
 * Output: 1
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2500
 * -10^4 <= nums[i] <= 10^4
 *
 * Follow up:
 * Can you come up with an algorithm that runs in O(n log(n)) time complexity?
 *
 * ============================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern Name:
 * Longest Increasing Subsequence (LIS)
 *
 * Problem Archetype:
 * "Best subsequence ending somewhere"
 *
 * Core Pattern Families:
 *
 * 1. DP over previous states
 * 2. Greedy + Binary Search optimization
 *
 * ----------------------------------------------------------------------------
 * 🟢 CORE INVARIANT (O(n²) DP)
 * ----------------------------------------------------------------------------
 *
 * dp[i] =
 * length of the longest strictly increasing subsequence
 * that MUST end at index i.
 *
 * Transition:
 *
 * For every previous index j:
 *
 * if nums[j] < nums[i]
 * then nums[i] can extend subsequence ending at j.
 *
 * So:
 *
 * dp[i] = max(dp[i], dp[j] + 1)
 *
 * ----------------------------------------------------------------------------
 * 🟢 CORE INVARIANT (O(n log n))
 * ----------------------------------------------------------------------------
 *
 * tails[len] =
 * smallest possible tail value of any increasing subsequence
 * of length (len + 1).
 *
 * Why smaller tail matters:
 *
 * Smaller tail leaves more future extension possibilities.
 *
 * Example:
 *
 * subsequence tail = 4 is better than tail = 10
 * for same subsequence length.
 *
 * ----------------------------------------------------------------------------
 * WHY THIS WORKS
 * ----------------------------------------------------------------------------
 *
 * We aggressively keep future options open.
 *
 * We do NOT store actual subsequences.
 *
 * We only preserve the BEST tail candidate for each length.
 *
 * ----------------------------------------------------------------------------
 * WHEN TO USE THIS PATTERN
 * ----------------------------------------------------------------------------
 *
 * Use when:
 *
 * - subsequence problems appear
 * - ordering matters
 * - strict/non-strict comparison exists
 * - "best extendable sequence" language appears
 * - optimization over prior states exists
 *
 * ----------------------------------------------------------------------------
 * RECOGNITION SIGNALS
 * ----------------------------------------------------------------------------
 *
 * - "longest increasing"
 * - "subsequence"
 * - extend previous valid states
 * - order preserved
 * - skipping allowed
 *
 * ----------------------------------------------------------------------------
 * DIFFERENCE VS SUBARRAY
 * ----------------------------------------------------------------------------
 *
 * Subsequence:
 * elements can be skipped.
 *
 * Subarray:
 * must remain contiguous.
 *
 * LIS is subsequence DP — NOT sliding window.
 *
 * ============================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 🧠 MENTAL MODEL (DP VERSION)
 * ----------------------------------------------------------------------------
 *
 * Think:
 *
 * "If I force nums[i] to be the LAST element,
 * what is the best subsequence I can build?"
 *
 * Every earlier smaller number is a candidate parent.
 *
 * ----------------------------------------------------------------------------
 * 🧠 MENTAL MODEL (GREEDY + BINARY SEARCH)
 * ----------------------------------------------------------------------------
 *
 * We maintain:
 *
 * tails[k] =
 * smallest tail for subsequence length (k + 1).
 *
 * We are NOT constructing final LIS directly.
 *
 * We are maintaining:
 *
 * "best future-expandable representatives"
 *
 * ----------------------------------------------------------------------------
 * 🟢 ALL INVARIANTS
 * ----------------------------------------------------------------------------
 *
 * DP Invariant:
 *
 * dp[i] always stores correct LIS ending at i.
 *
 * Greedy Invariants:
 *
 * 1. tails[] is sorted.
 *
 * 2. tails[len] stores minimal possible tail value.
 *
 * 3. Replacing a larger tail with smaller tail NEVER hurts.
 *
 * 4. tails size equals LIS length found so far.
 *
 * ----------------------------------------------------------------------------
 * VARIABLE MEANINGS
 * ----------------------------------------------------------------------------
 *
 * dp[i]
 * LIS ending exactly at i.
 *
 * tails[pos]
 * smallest tail for subsequence length pos + 1.
 *
 * size
 * current LIS length discovered.
 *
 * ----------------------------------------------------------------------------
 * ALLOWED MOVES
 * ----------------------------------------------------------------------------
 *
 * DP:
 * extend from smaller previous value.
 *
 * Greedy:
 * replace first >= current number.
 *
 * ----------------------------------------------------------------------------
 * FORBIDDEN MOVES
 * ----------------------------------------------------------------------------
 *
 * Strictly increasing means:
 *
 * nums[j] < nums[i]
 *
 * NOT:
 *
 * nums[j] <= nums[i]
 *
 * ----------------------------------------------------------------------------
 * TERMINATION LOGIC
 * ----------------------------------------------------------------------------
 *
 * DP:
 * answer = max(dp[i])
 *
 * Greedy:
 * answer = size
 *
 * ----------------------------------------------------------------------------
 * WHY NAIVE APPROACHES FAIL
 * ----------------------------------------------------------------------------
 *
 * Greedy local extension fails.
 *
 * Example:
 *
 * [3, 4, 1, 2]
 *
 * If we greedily keep 3,4:
 * future extension becomes weak.
 *
 * Better future comes from:
 * 1,2
 *
 * ============================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * WRONG IDEA 1
 * ----------------------------------------------------------------------------
 *
 * "Always extend current increasing sequence."
 *
 * Fails because:
 * local growth ≠ globally optimal subsequence.
 *
 * Counterexample:
 *
 * [10,9,2,5,3,7,101,18]
 *
 * ----------------------------------------------------------------------------
 * WRONG IDEA 2
 * ----------------------------------------------------------------------------
 *
 * Use <= instead of <.
 *
 * Violates:
 * strictly increasing invariant.
 *
 * Counterexample:
 *
 * [7,7,7]
 *
 * Correct answer:
 * 1
 *
 * Wrong answer:
 * 3
 *
 * ----------------------------------------------------------------------------
 * WRONG IDEA 3
 * ----------------------------------------------------------------------------
 *
 * Binary search replacement misunderstood as "destroying sequence".
 *
 * Example:
 *
 * tails:
 * [2,5,7]
 *
 * current = 3
 *
 * Replace 5 → 3
 *
 * New tails:
 * [2,3,7]
 *
 * This improves future extensibility.
 *
 * ----------------------------------------------------------------------------
 * INTERVIEWER TRAP
 * ----------------------------------------------------------------------------
 *
 * Candidate says:
 *
 * "tails stores actual LIS."
 *
 * Incorrect.
 *
 * tails is only a compressed optimal-state representation.
 *
 * ============================================================================
 * ⚙️ HOW TO PHYSICALLY ASSEMBLE THE CODE
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 🛠️ IMPLEMENTATION BLUEPRINT — O(n²) DP
 * ----------------------------------------------------------------------------
 *
 * 1. Create dp[]
 *
 * 2. Fill all with 1
 *    because each element alone is LIS length 1
 *
 * 3. Outer loop:
 *    choose ending index i
 *
 * 4. Inner loop:
 *    inspect all previous j
 *
 * 5. If nums[j] < nums[i]:
 *       extend subsequence
 *
 * 6. Update global answer
 *
 * ----------------------------------------------------------------------------
 * 🛠️ IMPLEMENTATION BLUEPRINT — O(n log n)
 * ----------------------------------------------------------------------------
 *
 * 1. Create tails[]
 *
 * 2. Maintain size
 *
 * 3. For every number:
 *
 *      binary search first >= num
 *
 * 4. Replace that position
 *
 * 5. If inserted at end:
 *      size++
 *
 * 6. Return size
 *
 * ============================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE (MEMORY SCAFFOLD)
 * ============================================================================
 *
 * tails = []
 *
 * for num in nums:
 *
 *     pos = first index >= num
 *
 *     tails[pos] = num
 *
 *     if pos == size:
 *         size++
 *
 * return size
 *
 * ============================================================================
 * 🟡 PRIMARY PROBLEM — SOLUTION CLASSES
 * ============================================================================
 */
public class LIS {

    /**
     * =========================================================================
     * BRUTE FORCE
     * =========================================================================
     *
     * Try:
     * take / skip recursion.
     *
     * Time:
     * O(2^n)
     *
     * Space:
     * O(n)
     *
     * Interview Preference:
     * Never preferred beyond initial discussion.
     */
    static class BruteForceSolution {

        public int lengthOfLIS(int[] nums) {

            return dfs(nums, 0, Integer.MIN_VALUE);
        }

        private int dfs(int[] nums, int index, int prev) {

            // Base case:
            // no elements left.
            if (index == nums.length) {
                return 0;
            }

            // Option 1:
            // skip current element.
            int skip = dfs(nums, index + 1, prev);

            int take = 0;

            // Strictly increasing condition.
            if (nums[index] > prev) {

                take = 1 + dfs(nums, index + 1, nums[index]);
            }

            return Math.max(skip, take);
        }
    }

    /**
     * =========================================================================
     * IMPROVED — O(n²) DYNAMIC PROGRAMMING
     * =========================================================================
     *
     * Core Idea:
     *
     * dp[i] =
     * LIS ending exactly at i.
     *
     * Time:
     * O(n²)
     *
     * Space:
     * O(n)
     *
     * Interview Preference:
     * Very important foundational solution.
     */

    /**
     * =========================================================================
     * 🟡 VISUAL DRY RUN — BEST SINGLE EXAMPLE
     * =========================================================================
     *
     * nums = [10, 9, 2, 5, 3, 7, 101, 18]
     *
     * =========================================================================
     * PART 1 — O(n²) DP DRY RUN
     * =========================================================================
     *
     * 🟢 INVARIANT
     *
     * dp[i] =
     * length of LIS ending EXACTLY at index i.
     *
     * -------------------------------------------------------------------------
     * INITIAL
     * -------------------------------------------------------------------------
     *
     * nums : [10,  9,  2,  5,  3,  7, 101, 18]
     * dp   : [ 1,  1,  1,  1,  1,  1,  1,  1]
     *
     * Every element alone forms LIS length 1.
     *
     * -------------------------------------------------------------------------
     * VISUAL TABLE
     * -------------------------------------------------------------------------
     *
     * ┌───────┬─────────┬──────────────────────────────┬────────┬─────────────┐
     * │   i   │ nums[i] │ Best Previous Smaller Value │ dp[i]  │ LIS Ending  │
     * ├───────┼─────────┼──────────────────────────────┼────────┼─────────────┤
     * │   0   │   10    │ none                         │   1    │ [10]        │
     * │   1   │    9    │ none                         │   1    │ [9]         │
     * │   2   │    2    │ none                         │   1    │ [2]         │
     * │   3   │    5    │ 2                            │   2    │ [2,5]       │
     * │   4   │    3    │ 2                            │   2    │ [2,3]       │
     * │   5   │    7    │ 5 or 3                       │   3    │ [2,5,7]     │
     * │   6   │  101    │ 7                            │   4    │ [2,5,7,101] │
     * │   7   │   18    │ 7                            │   4    │ [2,5,7,18]  │
     * └───────┴─────────┴──────────────────────────────┴────────┴─────────────┘
     *
     * Final Answer:
     * 4
     *
     * ----------------------------------------------------------------------------
     * 🟢 CORE DP INSIGHT
     * ----------------------------------------------------------------------------
     *
     * We are solving:
     *
     * "What is the best subsequence if nums[i] MUST be the final element?"
     *
     * NOT:
     *
     * "What is best globally so far?"
     *
     * =========================================================================
     * PART 2 — O(n log n) GREEDY + BINARY SEARCH
     * =========================================================================
     *
     * 🟢 INVARIANT
     *
     * tails[k] =
     * smallest possible tail for subsequence length (k + 1)
     *
     * ----------------------------------------------------------------------------
     * VISUAL TABLE
     * ----------------------------------------------------------------------------
     *
     * ┌─────────┬──────────────────────────────┬────────────────────┬────────┐
     * │ Current │ Action                       │ tails[]            │ Size   │
     * ├─────────┼──────────────────────────────┼────────────────────┼────────┤
     * │   10    │ start new subsequence        │ [10]               │   1    │
     * │    9    │ replace 10                   │ [9]                │   1    │
     * │    2    │ replace 9                    │ [2]                │   1    │
     * │    5    │ extend                       │ [2,5]              │   2    │
     * │    3    │ replace 5                    │ [2,3]              │   2    │
     * │    7    │ extend                       │ [2,3,7]            │   3    │
     * │  101    │ extend                       │ [2,3,7,101]        │   4    │
     * │   18    │ replace 101                  │ [2,3,7,18]         │   4    │
     * └─────────┴──────────────────────────────┴────────────────────┴────────┘
     *
     * Final Answer:
     * 4
     *
     * ----------------------------------------------------------------------------
     * 🟢 MOST IMPORTANT GREEDY INSIGHT
     * ----------------------------------------------------------------------------
     *
     * Replacing:
     *
     * [2,5]
     *
     * with:
     *
     * [2,3]
     *
     * is GOOD.
     *
     * Because:
     *
     * smaller tail = easier future extension.
     *
     * ----------------------------------------------------------------------------
     * 🔴 INTERVIEW TRAP
     * ----------------------------------------------------------------------------
     *
     * tails[] is NOT the actual LIS.
     *
     * It is:
     *
     * the best extendable representation for each length.
     */

    static class DPSolution {

        public int lengthOfLIS(int[] nums) {

            // Edge case:
            // problem constraints guarantee at least one element,
            // but defensive coding is still useful.
            if (nums == null || nums.length == 0) {
                return 0;
            }

            int n = nums.length;

            int[] dp = new int[n];

            // Invariant:
            // every element alone forms LIS length 1.
            Arrays.fill(dp, 1);

            int answer = 1;

            // Choose ending position.
            for (int i = 0; i < n; i++) {

                // Explore all previous states.
                for (int j = 0; j < i; j++) {

                    // Valid extension only if strictly increasing.
                    if (nums[j] < nums[i]) {

                        // Extend best subsequence ending at j.
                        dp[i] = Math.max(dp[i], dp[j] + 1);
                    }
                }

                // Maintain global optimum.
                answer = Math.max(answer, dp[i]);
            }

            return answer;
        }
    }

    /**
     * =========================================================================
     * OPTIMAL — GREEDY + BINARY SEARCH
     * =========================================================================
     *
     * Time:
     * O(n log n)
     *
     * Space:
     * O(n)
     *
     * Interview Preferred:
     * YES
     *
     * ----------------------------------------------------------------------------
     * 🟢 CORE INVARIANT
     * ----------------------------------------------------------------------------
     *
     * tails[k] =
     * smallest possible tail value
     * for increasing subsequence length (k + 1).
     */

    /**
     * =========================================================================
     * 🟡 VISUAL DRY RUN — OPTIMAL GREEDY + BINARY SEARCH
     * =========================================================================
     *
     * nums = [10, 9, 2, 5, 3, 7, 101, 18]
     *
     * ----------------------------------------------------------------------------
     * 🟢 CORE INVARIANT
     * ----------------------------------------------------------------------------
     *
     * tails[k] =
     * smallest possible tail value
     * for an increasing subsequence of length (k + 1)
     *
     * IMPORTANT:
     *
     * tails[] is NOT necessarily the actual LIS.
     *
     * ----------------------------------------------------------------------------
     * 🧠 MENTAL MODEL
     * ----------------------------------------------------------------------------
     *
     * For every subsequence length:
     *
     * keep the MOST FUTURE-EXTENDABLE tail.
     *
     * Smaller tail = better future growth potential.
     *
     *
     * =========================================================================
     * 🟢 WHY REPLACEMENT IS CORRECT
     * =========================================================================
     *
     * ----------------------------------------------------------------------------
     * KEY MOMENT:
     * ----------------------------------------------------------------------------
     *
     * tails = [2,5]
     * current = 3
     *
     * Replace:
     *
     * [2,5]
     *
     * with:
     *
     * [2,3]
     *
     * ----------------------------------------------------------------------------
     * WHY THIS IS BETTER
     * ----------------------------------------------------------------------------
     *
     * Both represent subsequence length = 2
     *
     * But:
     *
     * tail 3 is superior to tail 5.
     *
     * Because:
     *
     * more future numbers can extend 3.
     *
     * Example:
     *
     * future number = 4
     *
     * can extend:
     *
     * [2,3]
     *
     * but NOT:
     *
     * [2,5]
     *
     * =========================================================================
     * 🟡 VISUAL DRY RUN TABLE — OPTIMAL GREEDY + BINARY SEARCH
     * =========================================================================
     *
     * nums = [10, 9, 2, 5, 3, 7, 101, 18]
     *
     * ----------------------------------------------------------------------------
     * 🟢 CORE INVARIANT
     * ----------------------------------------------------------------------------
     *
     * tails[i] =
     * smallest possible tail
     * for increasing subsequence length (i + 1)
     *
     * Smaller tail =
     * easier future extension.
     *
     * =========================================================================
     * VISUAL TABLE
     * =========================================================================
     *
     * ┌──────┬─────────┬──────────────────────────────┬────────────────────┬──────┐
     * │ Step │ Current │ First Value >= Current      │ tails[]            │ size │
     * ├──────┼─────────┼──────────────────────────────┼────────────────────┼──────┤
     * │  1   │   10    │ none                         │ [10]               │  1   │
     * │  2   │    9    │ 10                           │ [9]                │  1   │
     * │  3   │    2    │ 9                            │ [2]                │  1   │
     * │  4   │    5    │ none                         │ [2,5]              │  2   │
     * │  5   │    3    │ 5                            │ [2,3]              │  2   │
     * │  6   │    7    │ none                         │ [2,3,7]            │  3   │
     * │  7   │  101    │ none                         │ [2,3,7,101]        │  4   │
     * │  8   │   18    │ 101                          │ [2,3,7,18]         │  4   │
     * └──────┴─────────┴──────────────────────────────┴────────────────────┴──────┘
     *
     * =========================================================================
     * 🟢 HOW TO READ THE TABLE
     * =========================================================================
     *
     * ----------------------------------------------------------------------------
     * STEP 5 → current = 3
     * ----------------------------------------------------------------------------
     *
     * Current tails:
     *
     * [2,5]
     *
     * First value >= 3:
     *
     * 5
     *
     * Replace:
     *
     * [2,5]
     *
     * with:
     *
     * [2,3]
     *
     * Why?
     *
     * Smaller tail is easier to extend later.
     *
     * =========================================================================
     * 🟢 MOST IMPORTANT INSIGHT
     * =========================================================================
     *
     * We are NOT storing actual LIS.
     *
     * We are storing:
     *
     * the BEST POSSIBLE tail
     * for every subsequence length.
     *
     * =========================================================================
     * 🧠 ONE-LINE MEMORY HOOK
     * =========================================================================
     *
     * "For every subsequence length,
     * keep the easiest ending value to extend later."
     */

    /**
     * =========================================================================
     * 🟢 OPTIMAL LIS — CLEAN MANUAL BINARY SEARCH VERSION
     * =========================================================================
     *
     * 🧠 CORE IDEA
     *
     * For every subsequence length:
     *
     * keep the SMALLEST POSSIBLE ending value.
     *
     * Smaller ending value =
     * easier future extension.
     *
     * ----------------------------------------------------------------------------
     * 🟢 CORE INVARIANT
     * ----------------------------------------------------------------------------
     *
     * tails[i] =
     * smallest possible tail
     * for increasing subsequence length (i + 1)
     *
     * ----------------------------------------------------------------------------
     * Example:
     * ----------------------------------------------------------------------------
     *
     * tails = [2,3,7]
     *
     * Means:
     *
     * length 1 subsequence can end at 2
     * length 2 subsequence can end at 3
     * length 3 subsequence can end at 7
     */
    static class OptimalSolution {

        public int lengthOfLIS(int[] nums) {

            if (nums == null || nums.length == 0) {
                return 0;
            }

            int[] tails = new int[nums.length];

            int size = 0;

            for (int current : nums) {

                /**
                 * Find:
                 *
                 * first value >= current
                 */
                int left = 0;
                int right = size;

                while (left < right) {

                    int mid = left + (right - left) / 2;

                    /**
                     * Current number can extend after mid.
                     *
                     * So answer must be on right side.
                     */
                    if (tails[mid] < current) {

                        left = mid + 1;

                    } else {

                        /**
                         * mid might be first >= current.
                         */
                        right = mid;
                    }
                }

                /**
                 * left =
                 * first position with value >= current
                 */
                tails[left] = current;

                /**
                 * Inserted beyond current LIS length.
                 */
                if (left == size) {
                    size++;
                }
            }

            return size;
        }
    }
    /**
     * =========================================================================
     * 🟣 INTERVIEW ARTICULATION (NO CODE)
     * =========================================================================
     *
     * ----------------------------------------------------------------------------
     * HOW TO EXPLAIN THE DP SOLUTION
     * ----------------------------------------------------------------------------
     *
     * "I define dp[i] as the LIS ending exactly at index i.
     *
     * Then I inspect all previous indices j.
     *
     * If nums[j] < nums[i],
     * then nums[i] can extend subsequence ending at j.
     *
     * So transition becomes:
     *
     * dp[i] = max(dp[i], dp[j] + 1)
     *
     * Final answer is maximum dp value."
     *
     * ----------------------------------------------------------------------------
     * HOW TO EXPLAIN THE OPTIMAL SOLUTION
     * ----------------------------------------------------------------------------
     *
     * "Instead of storing all subsequences,
     * I maintain the smallest possible tail for every length.
     *
     * Smaller tails are always better because they leave more room
     * for future extension.
     *
     * I binary search replacement position for each number."
     *
     * ----------------------------------------------------------------------------
     * WHAT BREAKS IF CHANGED
     * ----------------------------------------------------------------------------
     *
     * If strict comparison becomes <=:
     *
     * duplicates incorrectly extend LIS.
     *
     * If binary search finds first > instead of >=:
     *
     * duplicate handling breaks.
     *
     * ----------------------------------------------------------------------------
     * IN-PLACE FEASIBILITY
     * ----------------------------------------------------------------------------
     *
     * Possible with careful mutation,
     * but clarity worsens.
     *
     * ----------------------------------------------------------------------------
     * STREAMING FEASIBILITY
     * ----------------------------------------------------------------------------
     *
     * O(n log n) approach works beautifully in streaming.
     *
     * Each new number updates tails incrementally.
     *
     * ----------------------------------------------------------------------------
     * WHEN NOT TO USE THIS PATTERN
     * ----------------------------------------------------------------------------
     *
     * If contiguity matters:
     * use subarray techniques instead.
     *
     * If state depends on multiple dimensions:
     * LIS alone insufficient.
     *
     * =========================================================================
     * 🎯 INTERVIEW RECALL SHEET (30-SECOND RECALL)
     * =========================================================================
     *
     * Pattern Trigger:
     * subsequence + increasing + optimal length
     *
     * Core Invariant:
     * smallest tail for each length
     *
     * Search Target:
     * first >= current
     *
     * Discard Rule:
     * larger tail is dominated by smaller tail
     *
     * Common Trap:
     * tails is NOT actual LIS
     *
     * Edge Cases:
     * duplicates
     * descending array
     * single element
     *
     * Interview One-Liner:
     *
     * "Maintain smallest extendable tail for every subsequence length."
     *
     * Re-derivation Cue:
     *
     * "Smaller tail always gives better future extension."
     *
     * =========================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =========================================================================
     *
     * ----------------------------------------------------------------------------
     * VARIATION 1 — NON-DECREASING SUBSEQUENCE
     * ----------------------------------------------------------------------------
     *
     * Change:
     *
     * <
     *
     * to:
     *
     * <=
     *
     * Binary search target changes too.
     *
     * ----------------------------------------------------------------------------
     * VARIATION 2 — PRINT ACTUAL LIS
     * ----------------------------------------------------------------------------
     *
     * Need:
     *
     * parent reconstruction arrays.
     *
     * Pattern still works,
     * but bookkeeping increases.
     *
     * ----------------------------------------------------------------------------
     * VARIATION 3 — COUNT NUMBER OF LIS
     * ----------------------------------------------------------------------------
     *
     * LIS length invariant alone insufficient.
     *
     * Need:
     *
     * count DP dimension.
     *
     * ----------------------------------------------------------------------------
     * PATTERN BREAK SIGNALS
     * ----------------------------------------------------------------------------
     *
     * If problem asks:
     *
     * contiguous range
     * exact partitioning
     * arbitrary graph traversal
     *
     * LIS invariant likely breaks.
     *
     * =========================================================================
     * ⚫ REINFORCEMENT PROBLEMS
     * =========================================================================
     */

    /**
     * =========================================================================
     * REINFORCEMENT 1 — MAXIMUM LENGTH OF PAIR CHAIN
     * =========================================================================
     *
     * LeetCode 646
     *
     * Summary:
     *
     * Given pairs [a,b],
     * find longest chain where previous end < next start.
     *
     * ----------------------------------------------------------------------------
     * INVARIANT MAPPING
     * ----------------------------------------------------------------------------
     *
     * Same LIS extension idea:
     *
     * previous_end < current_start
     */
    static class MaximumLengthOfPairChain {

        public int findLongestChain(int[][] pairs) {

            Arrays.sort(pairs, Comparator.comparingInt(a -> a[0]));

            int n = pairs.length;

            int[] dp = new int[n];

            Arrays.fill(dp, 1);

            int answer = 1;

            for (int i = 0; i < n; i++) {

                for (int j = 0; j < i; j++) {

                    if (pairs[j][1] < pairs[i][0]) {

                        dp[i] = Math.max(dp[i], dp[j] + 1);
                    }
                }

                answer = Math.max(answer, dp[i]);
            }

            return answer;
        }
    }

    /**
     * =========================================================================
     * REINFORCEMENT 2 — RUSSIAN DOLL ENVELOPES
     * =========================================================================
     *
     * LeetCode 354
     *
     * Summary:
     *
     * Nest envelopes by width and height.
     *
     * ----------------------------------------------------------------------------
     * INVARIANT MAPPING
     * ----------------------------------------------------------------------------
     *
     * Convert to LIS on heights after sorting widths.
     */
    static class RussianDollEnvelopes {

        public int maxEnvelopes(int[][] envelopes) {

            Arrays.sort(envelopes, (a, b) -> {

                if (a[0] == b[0]) {
                    return b[1] - a[1];
                }

                return a[0] - b[0];
            });

            int[] tails = new int[envelopes.length];

            int size = 0;

            for (int[] envelope : envelopes) {

                int height = envelope[1];

                int left = 0;
                int right = size;

                while (left < right) {

                    int mid = left + (right - left) / 2;

                    if (tails[mid] < height) {
                        left = mid + 1;
                    } else {
                        right = mid;
                    }
                }

                tails[left] = height;

                if (left == size) {
                    size++;
                }
            }

            return size;
        }
    }

    /**
     * =========================================================================
     * REINFORCEMENT 3 — LONGEST CONTINUOUS INCREASING SUBSEQUENCE
     * =========================================================================
     *
     * LeetCode 674
     *
     * ----------------------------------------------------------------------------
     * IMPORTANT
     * ----------------------------------------------------------------------------
     *
     * This is SUBARRAY,
     * not subsequence.
     *
     * Pattern changes.
     *
     * Edge Case Trap:
     * skipping not allowed.
     */
    static class LongestContinuousIncreasingSubsequence {

        public int findLengthOfLCIS(int[] nums) {

            if (nums == null || nums.length == 0) {
                return 0;
            }

            int best = 1;
            int current = 1;

            for (int i = 1; i < nums.length; i++) {

                if (nums[i] > nums[i - 1]) {

                    current++;
                } else {

                    current = 1;
                }

                best = Math.max(best, current);
            }

            return best;
        }
    }

    /**
     * =========================================================================
     * 🧩 RELATED PROBLEMS
     * =========================================================================
     */

    /**
     * =========================================================================
     * RELATED 1 — NUMBER OF LONGEST INCREASING SUBSEQUENCES
     * =========================================================================
     *
     * LeetCode 673
     *
     * Modified Invariant:
     *
     * Need:
     *
     * length DP + count DP
     */
    static class NumberOfLIS {

        public int findNumberOfLIS(int[] nums) {

            int n = nums.length;

            int[] length = new int[n];
            int[] count = new int[n];

            Arrays.fill(length, 1);
            Arrays.fill(count, 1);

            int maxLen = 1;

            for (int i = 0; i < n; i++) {

                for (int j = 0; j < i; j++) {

                    if (nums[j] < nums[i]) {

                        if (length[j] + 1 > length[i]) {

                            length[i] = length[j] + 1;
                            count[i] = count[j];

                        } else if (length[j] + 1 == length[i]) {

                            count[i] += count[j];
                        }
                    }
                }

                maxLen = Math.max(maxLen, length[i]);
            }

            int answer = 0;

            for (int i = 0; i < n; i++) {

                if (length[i] == maxLen) {
                    answer += count[i];
                }
            }

            return answer;
        }
    }

    /**
     * =========================================================================
     * RELATED 2 — LONGEST BITONIC SUBSEQUENCE
     * =========================================================================
     *
     * Same invariant twice:
     *
     * increasing from left
     * decreasing from right
     */
    static class LongestBitonicSubsequence {

        public int longestBitonic(int[] nums) {

            int n = nums.length;

            int[] lis = new int[n];
            int[] lds = new int[n];

            Arrays.fill(lis, 1);
            Arrays.fill(lds, 1);

            for (int i = 0; i < n; i++) {

                for (int j = 0; j < i; j++) {

                    if (nums[j] < nums[i]) {

                        lis[i] = Math.max(lis[i], lis[j] + 1);
                    }
                }
            }

            for (int i = n - 1; i >= 0; i--) {

                for (int j = n - 1; j > i; j--) {

                    if (nums[j] < nums[i]) {

                        lds[i] = Math.max(lds[i], lds[j] + 1);
                    }
                }
            }

            int answer = 1;

            for (int i = 0; i < n; i++) {

                answer = Math.max(answer, lis[i] + lds[i] - 1);
            }

            return answer;
        }
    }

    /**
     * =========================================================================
     * RELATED 3 — LARGEST DIVISIBLE SUBSET
     * =========================================================================
     *
     * Modified invariant:
     *
     * divisibility replaces increasing condition.
     */
    static class LargestDivisibleSubset {

        public List<Integer> largestDivisibleSubset(int[] nums) {

            Arrays.sort(nums);

            int n = nums.length;

            int[] dp = new int[n];
            int[] parent = new int[n];

            Arrays.fill(dp, 1);
            Arrays.fill(parent, -1);

            int bestIndex = 0;

            for (int i = 0; i < n; i++) {

                for (int j = 0; j < i; j++) {

                    if (nums[i] % nums[j] == 0) {

                        if (dp[j] + 1 > dp[i]) {

                            dp[i] = dp[j] + 1;
                            parent[i] = j;
                        }
                    }
                }

                if (dp[i] > dp[bestIndex]) {
                    bestIndex = i;
                }
            }

            List<Integer> answer = new ArrayList<>();

            while (bestIndex != -1) {

                answer.add(nums[bestIndex]);
                bestIndex = parent[bestIndex];
            }

            Collections.reverse(answer);

            return answer;
        }
    }

    /**
     * =========================================================================
     * 🧠 MASTERY CHECKLIST
     * =========================================================================
     *
     * Q: invariant?
     *
     * DP:
     * dp[i] = LIS ending at i
     *
     * Optimal:
     * tails[k] = minimal tail for length k+1
     *
     * ----------------------------------------------------------------------------
     * Q: search target?
     *
     * first value >= current number
     *
     * ----------------------------------------------------------------------------
     * Q: discard rule?
     *
     * larger tail dominated by smaller tail
     *
     * ----------------------------------------------------------------------------
     * Q: termination logic?
     *
     * size = LIS length
     *
     * ----------------------------------------------------------------------------
     * Q: naive failure?
     *
     * local greedy choices block future extension
     *
     * ----------------------------------------------------------------------------
     * Q: edge cases?
     *
     * duplicates
     * descending array
     * single element
     *
     * ----------------------------------------------------------------------------
     * Q: debugging readiness?
     *
     * verify:
     *
     * tails sorted
     * binary search target correct
     * strict inequality correct
     *
     * ----------------------------------------------------------------------------
     * Q: variant readiness?
     *
     * understand:
     *
     * changing comparator changes invariant
     *
     * ----------------------------------------------------------------------------
     * Q: pattern boundary?
     *
     * subsequence only
     * not contiguous window problems
     *
     * =========================================================================
     * 🧪 SELF-VERIFYING TESTS
     * =========================================================================
     */

    private static void assertEqual(int actual, int expected, String testName) {

        if (actual != expected) {

            throw new AssertionError(
                    testName
                            + " FAILED | expected = "
                            + expected
                            + " but got = "
                            + actual
            );
        }

        System.out.println("PASSED: " + testName);
    }

    public static void main(String[] args) {

        OptimalSolution optimal = new OptimalSolution();
        DPSolution dp = new DPSolution();

        /**
         * -------------------------------------------------------------
         * Happy Path
         * -------------------------------------------------------------
         *
         * Classic LIS example.
         */
        int[] nums1 = {10, 9, 2, 5, 3, 7, 101, 18};

        assertEqual(
                optimal.lengthOfLIS(nums1),
                4,
                "Classic LIS Example — Optimal"
        );

        assertEqual(
                dp.lengthOfLIS(nums1),
                4,
                "Classic LIS Example — DP"
        );

        /**
         * -------------------------------------------------------------
         * Duplicate Handling
         * -------------------------------------------------------------
         *
         * Strictly increasing means duplicates cannot extend.
         */
        int[] nums2 = {7, 7, 7, 7};

        assertEqual(
                optimal.lengthOfLIS(nums2),
                1,
                "Duplicates Strictness Test"
        );

        /**
         * -------------------------------------------------------------
         * Mixed Oscillation
         * -------------------------------------------------------------
         *
         * Tests replacement behavior in tails[].
         */
        int[] nums3 = {0, 1, 0, 3, 2, 3};

        assertEqual(
                optimal.lengthOfLIS(nums3),
                4,
                "Oscillation Replacement Test"
        );

        /**
         * -------------------------------------------------------------
         * Strictly Descending
         * -------------------------------------------------------------
         *
         * Every element alone.
         */
        int[] nums4 = {9, 8, 7, 6, 5};

        assertEqual(
                optimal.lengthOfLIS(nums4),
                1,
                "Descending Array Test"
        );

        /**
         * -------------------------------------------------------------
         * Strictly Increasing
         * -------------------------------------------------------------
         *
         * Entire array is LIS.
         */
        int[] nums5 = {1, 2, 3, 4, 5};

        assertEqual(
                optimal.lengthOfLIS(nums5),
                5,
                "Strictly Increasing Test"
        );

        /**
         * -------------------------------------------------------------
         * Single Element Boundary
         * -------------------------------------------------------------
         */
        int[] nums6 = {42};

        assertEqual(
                optimal.lengthOfLIS(nums6),
                1,
                "Single Element Boundary Test"
        );

        /**
         * -------------------------------------------------------------
         * Interview Trap
         * -------------------------------------------------------------
         *
         * Greedy local extension would fail.
         */
        int[] nums7 = {3, 4, 1, 2};

        assertEqual(
                optimal.lengthOfLIS(nums7),
                2,
                "Greedy Trap Test"
        );

        System.out.println();
        System.out.println("All tests passed.");

        System.out.println();
        System.out.println("I understand the invariant.");
        System.out.println("I can re-derive the solution.");
        System.out.println("I can physically reconstruct the implementation under pressure.");
        System.out.println("This chapter is complete.");
    }
}