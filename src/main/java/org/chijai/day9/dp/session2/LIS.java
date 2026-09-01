package org.chijai.day9.dp.session2;

import java.util.Arrays;

/**
 * ============================================================================
 * LONGEST INCREASING SUBSEQUENCE — HIGH-ROI INTERVIEW FILE
 * ============================================================================
 *
 * LeetCode 300
 *
 * GOAL OF THIS FILE
 * ----------------------------------------------------------------------------
 * Do NOT try to memorize every LIS trick.
 *
 * Learn one reusable interview machine deeply:
 *
 *      BEST CHAIN ENDING AT i
 *
 * Then reuse it whenever a problem says:
 *
 *      "Choose items in order and build the best valid chain."
 *
 * DERIVATION / ANTI-FREEZE:
 *      brute-force recursion -> memoization
 *
 * PRIMARY / PRESSURE-SAFE REUSABLE SOLUTION:
 *      O(n^2) ending-at-i DP
 *
 * FOLLOW-UP / RECOGNIZE + EXPLAIN:
 *      O(n log n) tails + binary search
 *
 * ============================================================================
 * 30-SECOND PRESSURE RECONSTRUCTION
 * ============================================================================
 *
 * See:
 *      subsequence + ordering rule + maximize length
 *
 * Ask:
 *      "If nums[i] MUST be the last element,
 *       where could I have come from?"
 *
 * State:
 *      dp[i] = best valid subsequence ending EXACTLY at i
 *
 * Predecessor:
 *      every earlier j
 *
 * Valid transition for LIS:
 *      nums[j] < nums[i]
 *
 * Transition:
 *      dp[i] = max(dp[i], dp[j] + 1)
 *
 * Base:
 *      1
 *
 * Answer:
 *      max(dp)
 *
 * MEMORY HOOK:
 *
 *      END AT i
 *      LOOK LEFT
 *      VALID PREDECESSOR
 *      EXTEND
 *
 * ============================================================================
 */
public class LIS {

    /*
     * =========================================================================
     * PROBLEM STATEMENT — DETAILED
     * =========================================================================
     *
     * Given an integer array nums, return the length of the longest strictly
     * increasing subsequence.
     *
     * A subsequence is formed by deleting zero or more elements while keeping
     * the relative order of the remaining elements unchanged.
     *
     * "Strictly increasing" means:
     *
     *      next value > previous value
     *
     * Equal values do NOT count as increasing.
     *
     * -------------------------------------------------------------------------
     * EXAMPLE 1
     * -------------------------------------------------------------------------
     *
     * nums = [10, 9, 2, 5, 3, 7, 101, 18]
     *
     * One valid longest increasing subsequence is:
     *
     *      [2, 3, 7, 101]
     *
     * Another is:
     *
     *      [2, 3, 7, 18]
     *
     * Length:
     *
     *      4
     *
     * -------------------------------------------------------------------------
     * EXAMPLE 2
     * -------------------------------------------------------------------------
     *
     * nums = [0, 1, 0, 3, 2, 3]
     *
     * One LIS is:
     *
     *      [0, 1, 2, 3]
     *
     * Length:
     *
     *      4
     *
     * -------------------------------------------------------------------------
     * EXAMPLE 3
     * -------------------------------------------------------------------------
     *
     * nums = [7, 7, 7, 7]
     *
     * Since the subsequence must be strictly increasing,
     * equal values cannot extend one another.
     *
     * Answer:
     *
     *      1
     *
     * -------------------------------------------------------------------------
     * IMPORTANT DISTINCTION
     * -------------------------------------------------------------------------
     *
     * SUBSEQUENCE:
     *
     *      may skip elements
     *
     * SUBARRAY:
     *
     *      must remain contiguous
     *
     * Therefore this is NOT a sliding-window problem.
     */

    /*
     * =========================================================================
     * HOW THE BRAIN SHOULD PROGRESS WHILE READING THE PROBLEM
     * =========================================================================
     *
     * The purpose of this section is not to memorize LIS.
     *
     * It is to train a reusable reasoning sequence that can be applied
     * to unfamiliar "best ordered chain" problems.
     *
     * -------------------------------------------------------------------------
     * STEP 1 — PARSE THE WORDS
     * -------------------------------------------------------------------------
     *
     * "Subsequence"
     *
     *      I can skip elements.
     *      Order still matters.
     *
     * "Increasing"
     *
     *      There is a transition rule between chosen elements.
     *
     * "Longest"
     *
     *      This is an optimization problem.
     *
     * So mentally:
     *
     *      choose / skip
     *      preserve order
     *      obey a pairwise rule
     *      maximize chain length
     *
     * -------------------------------------------------------------------------
     * STEP 2 — FIRST NAIVE THOUGHT
     * -------------------------------------------------------------------------
     *
     * "Can I just greedily keep taking the next larger value?"
     *
     * Example:
     *
     *      [3, 4, 1, 2]
     *
     * Greedily taking:
     *
     *      [3, 4]
     *
     * seems reasonable,
     * but later:
     *
     *      [1, 2]
     *
     * is another equally long possibility with a much better future tail.
     *
     * This tells me:
     *
     *      one local path is not enough.
     *
     * I need to remember multiple possible histories or compressed states.
     *
     * -------------------------------------------------------------------------
     * STEP 3 — WHAT MAKES A STATE USEFUL?
     * -------------------------------------------------------------------------
     *
     * Asking:
     *
     *      "What is the LIS of the whole prefix?"
     *
     * is not enough.
     *
     * Why?
     *
     * Because whether I can append nums[i] depends on
     * WHAT VALUE the previous subsequence ends with.
     *
     * So I need a state that exposes the ending point.
     *
     * This suggests:
     *
     *      "What if I force nums[i] to be the LAST element?"
     *
     * That is the key state-discovery move.
     *
     * -------------------------------------------------------------------------
     * STEP 4 — ONCE i IS FORCED TO BE LAST, THE PROBLEM BECOMES LOCAL
     * -------------------------------------------------------------------------
     *
     * Suppose nums[i] must be the final element.
     *
     * Who could come before it?
     *
     * Any earlier j such that:
     *
     *      j < i
     *      nums[j] < nums[i]
     *
     * So now the question becomes:
     *
     *      "Among all valid previous endings j,
     *       which one gives me the longest chain?"
     *
     * That naturally gives:
     *
     *      dp[i] = 1 + max(dp[j])
     *
     * over every valid predecessor j.
     *
     * If there is no valid predecessor:
     *
     *      dp[i] = 1
     *
     * -------------------------------------------------------------------------
     * STEP 5 — WHY THIS IS A REUSABLE PATTERN
     * -------------------------------------------------------------------------
     *
     * Notice what was generic:
     *
     *      force i to be the end
     *      scan previous j
     *      check whether j can precede i
     *      extend the best previous chain
     *
     * Only this part was LIS-specific:
     *
     *      nums[j] < nums[i]
     *
     * In another problem, the rule could become:
     *
     *      nums[i] % nums[j] == 0
     *
     * or:
     *
     *      pair[j].end < pair[i].start
     *
     * or:
     *
     *      word[j] is a predecessor of word[i]
     *
     * This is why the important thing to learn is:
     *
     *      BEST CHAIN ENDING AT i
     *
     * not "LeetCode 300 code."
     *
     * -------------------------------------------------------------------------
     * STEP 6 — WHAT IS THE GLOBAL ANSWER?
     * -------------------------------------------------------------------------
     *
     * dp[i] answers:
     *
     *      best LIS ending exactly at i
     *
     * But the final LIS may end anywhere.
     *
     * Therefore:
     *
     *      answer = max(dp[i])
     *
     * not:
     *
     *      dp[n - 1]
     *
     * -------------------------------------------------------------------------
     * STEP 7 — ONLY AFTER THE O(n^2) DP IS CLEAR, ASK FOR OPTIMIZATION
     * -------------------------------------------------------------------------
     *
     * The DP may keep many states representing the same progress.
     *
     * Ask:
     *
     *      "If two subsequences have the same length,
     *       do I really need both?"
     *
     * Example:
     *
     *      length 3 ending at 8
     *      length 3 ending at 5
     *
     * The one ending at 5 is always at least as useful for the future.
     *
     * So 8 is dominated.
     *
     * This leads to:
     *
     *      same length -> keep the smallest tail
     *
     * That is the conceptual bridge to tails[].
     *
     * -------------------------------------------------------------------------
     * INTERVIEW THOUGHT SCRIPT
     * -------------------------------------------------------------------------
     *
     * When reading a similar random problem, mentally ask:
     *
     *      1. Am I building a chain while preserving order?
     *      2. Can I skip elements?
     *      3. What makes one item legally follow another?
     *      4. If I force item i to be last, can I describe the best answer?
     *      5. Which earlier states can transition into i?
     *      6. Is the final answer one dp state or the best among all states?
     *      7. Are some states dominated and therefore compressible?
     *
     * This sequence is more valuable than memorizing the finished code.
     */


    /*
     * =========================================================================
     * DP ANTI-FREEZE LADDER — THE ORDER TO THINK IN AN INTERVIEW
     * =========================================================================
     *
     * If bottom-up DP does not appear immediately, do NOT freeze trying to
     * invent a table.
     *
     * Use this ladder:
     *
     *      1. RECURSION
     *         What is my state?
     *         What choices do I have?
     *
     *      2. MEMOIZATION
     *         Which recursive states repeat?
     *         Cache them.
     *
     *      3. BOTTOM-UP DP
     *         Can I express the same dependency with a simpler iterative state?
     *
     *      4. FURTHER OPTIMIZATION
     *         Are some states dominated or compressible?
     *
     * For LIS:
     *
     *      recursion:
     *          (index, previousIndex)
     *          TAKE / SKIP
     *
     *      memoization:
     *          cache (index, previousIndex)
     *
     *      bottom-up:
     *          dp[i] = best LIS ending exactly at i
     *
     *      optimized follow-up:
     *          same length -> keep smallest tail
     *
     * UNIVERSAL MEMORY HOOK:
     *
     *      STATE -> CHOICES -> RECURSE -> MEMOIZE -> TABULATE -> OPTIMIZE
     */

    /**
     * =========================================================================
     * INTUITIVE DERIVATION — BRUTE-FORCE RECURSION
     * =========================================================================
     *
     * PURPOSE:
     *
     *      easiest anti-freeze working solution
     *      easiest way to discover the DP state
     *
     * At each index:
     *
     *      SKIP current
     *
     *      TAKE current
     *          only if it is greater than the previously chosen value
     *
     * State:
     *
     *      index
     *      previousIndex
     *
     * Why previousIndex instead of previousValue?
     *
     *      It gives a finite O(n^2) state space for memoization later.
     *
     * Time:
     *
     *      O(2^n)
     *
     * Space:
     *
     *      O(n) recursion depth
     */
    static class RecursiveSolution {

        public int lengthOfLIS(int[] nums) {

            if (nums == null || nums.length == 0) {
                return 0;
            }

            return dfs(nums, 0, -1);
        }

        private int dfs(int[] nums, int index, int previousIndex) {

            if (index == nums.length) {
                return 0;
            }

            int skip = dfs(nums, index + 1, previousIndex);

            int take = 0;

            if (previousIndex == -1 || nums[index] > nums[previousIndex]) {
                take = 1 + dfs(nums, index + 1, index);
            }

            return Math.max(take, skip);
        }
    }

    /*
     * =========================================================================
     * VISUAL RECURSION TREE — SMALL EXAMPLE
     * =========================================================================
     *
     * nums = [3, 1, 2]
     *
     * State:
     *
     *      dfs(index, previousIndex)
     *
     * Start:
     *
     *                              dfs(0,-1)
     *                              current=3
     *                            /           \
     *                         TAKE           SKIP
     *                          3
     *                       /                   \
     *                  dfs(1,0)              dfs(1,-1)
     *                  current=1             current=1
     *                     |                   /       \
     *              TAKE invalid            TAKE      SKIP
     *                     |                  1
     *                   SKIP              /             \
     *                     |            dfs(2,1)       dfs(2,-1)
     *                  dfs(2,0)         current=2      current=2
     *                  current=2          /   \\          /   \\
     *                     |             TAKE SKIP      TAKE  SKIP
     *              TAKE invalid           2              2
     *                     |                |              |
     *                   SKIP             end            end
     *                     |
     *                    end
     *
     * Important paths:
     *
     *      TAKE 3
     *          -> 1 cannot be taken
     *          -> 2 cannot be taken
     *          -> length 1
     *
     *      SKIP 3
     *          -> TAKE 1
     *          -> TAKE 2
     *          -> length 2
     *
     * Answer:
     *
     *      2
     *
     * -------------------------------------------------------------------------
     * HOW TO READ THIS IN AN INTERVIEW
     * -------------------------------------------------------------------------
     *
     * Do not memorize the tree.
     *
     * See the recurring structure:
     *
     *      CURRENT STATE
     *          |
     *          +-- SKIP -> next index, same previous
     *          |
     *          +-- TAKE -> next index, current becomes previous
     *
     * That is the recurrence.
     */

    /*
     * =========================================================================
     * RECURSIVE RECURRENCE — WRITE THIS BEFORE THINKING ABOUT A TABLE
     * =========================================================================
     *
     * solve(index, previousIndex)
     *
     *      if index == n:
     *          return 0
     *
     *      skip = solve(index + 1, previousIndex)
     *
     *      take = 0
     *
     *      if previousIndex == -1
     *         OR nums[index] > nums[previousIndex]:
     *
     *          take = 1 + solve(index + 1, index)
     *
     *      return max(take, skip)
     *
     * This is already a correct solution.
     *
     * Only after correctness is clear do we optimize repeated work.
     */

    /**
     * =========================================================================
     * TOP-DOWN DP — MEMOIZED RECURSION
     * =========================================================================
     *
     * Same exact reasoning as brute-force recursion.
     *
     * Difference:
     *
     *      each unique (index, previousIndex) state is solved once.
     *
     * previousIndex may be -1.
     * Arrays cannot use -1 as an index.
     *
     * So memo column is:
     *
     *      previousIndex + 1
     *
     * Mapping:
     *
     *      previousIndex = -1 -> column 0
     *      previousIndex =  0 -> column 1
     *      previousIndex =  1 -> column 2
     *      ...
     *
     * Number of unique states:
     *
     *      index         = O(n)
     *      previousIndex = O(n)
     *
     * Time:
     *
     *      O(n^2)
     *
     * Space:
     *
     *      O(n^2) memo
     *      + O(n) recursion stack
     */
    static class MemoizedSolution {

        public int lengthOfLIS(int[] nums) {

            if (nums == null || nums.length == 0) {
                return 0;
            }

            int[][] memo = new int[nums.length][nums.length + 1];

            int row = 0;

            while (row < memo.length) {
                Arrays.fill(memo[row], -1);
                row++;
            }

            return dfs(nums, 0, -1, memo);
        }

        private int dfs(
                int[] nums,
                int index,
                int previousIndex,
                int[][] memo
        ) {

            if (index == nums.length) {
                return 0;
            }

            int memoColumn = previousIndex + 1;

            if (memo[index][memoColumn] != -1) {
                return memo[index][memoColumn];
            }

            int skip = dfs(
                    nums,
                    index + 1,
                    previousIndex,
                    memo
            );

            int take = 0;

            if (previousIndex == -1 || nums[index] > nums[previousIndex]) {

                take = 1 + dfs(
                        nums,
                        index + 1,
                        index,
                        memo
                );
            }

            memo[index][memoColumn] = Math.max(take, skip);

            return memo[index][memoColumn];
        }
    }

    /*
     * =========================================================================
     * VISUAL MEMOIZATION TABLE — WHAT THE CACHE REPRESENTS
     * =========================================================================
     *
     * memo[index][previousIndex + 1]
     *
     * means:
     *
     *      best LIS length obtainable from `index` onward
     *      when `previousIndex` was the last selected element.
     *
     * Small conceptual table:
     *
     * +----------------------+----------------------------+--------------------+
     * | State                | First encounter            | Later encounter    |
     * +----------------------+----------------------------+--------------------+
     * | (3, 1)               | recursively compute        | return memo[3][2]  |
     * | (4, 2)               | recursively compute        | return memo[4][3]  |
     * | (5, -1)              | recursively compute        | return memo[5][0]  |
     * +----------------------+----------------------------+--------------------+
     *
     * -------------------------------------------------------------------------
     * WHY THIS IS DP
     * -------------------------------------------------------------------------
     *
     * Brute recursion:
     *
     *      solve the same state every time a path reaches it
     *
     * Memoized recursion:
     *
     *      solve each unique state once
     *
     * That is the key DP recognition signal:
     *
     *      RECURSION + OVERLAPPING SUBPROBLEMS = MEMOIZATION
     */

    /*
     * =========================================================================
     * RECURSION -> MEMOIZATION -> BOTTOM-UP BRIDGE
     * =========================================================================
     *
     * Recursive state:
     *
     *      (index, previousIndex)
     *
     * is intuitive, but uses O(n^2) table space.
     *
     * We can find a cleaner formulation by changing the question:
     *
     *      Instead of:
     *          "What can I build from here?"
     *
     *      Ask:
     *          "What is the best chain if i MUST be the last element?"
     *
     * That produces the simpler 1D state:
     *
     *      dp[i] = best LIS ending exactly at i
     *
     * This is not a completely different idea.
     * It is a more compact way to organize the same search space.
     */

    /**
     * =========================================================================
     * PRIMARY — O(n^2) DP
     * =========================================================================
     *
     * This is the solution to be able to RECONSTRUCT under interview pressure.
     *
     * Do not memorize the code line-by-line.
     *
     * Reconstruct it from:
     *
     *      dp[i] = best LIS ending exactly at i
     */
    static class DPSolution {

        public int lengthOfLIS(int[] nums) {

            if (nums == null || nums.length == 0) {
                return 0;
            }

            int[] dp = new int[nums.length];

            /*
             * Every element alone is an increasing subsequence of length 1.
             */
            Arrays.fill(dp, 1);

            int answer = 1;

            int i = 0;

            while (i < nums.length) {

                int j = 0;

                while (j < i) {

                    /*
                     * Can the chain ending at j continue into i?
                     */
                    if (nums[j] < nums[i]) {
                        dp[i] = Math.max(dp[i], dp[j] + 1);
                    }

                    j++;
                }

                /*
                 * LIS may end anywhere, not necessarily at nums[n - 1].
                 */
                answer = Math.max(answer, dp[i]);

                i++;
            }

            return answer;
        }
    }

    /*
     * =========================================================================
     * WHY THIS DP STATE?
     * =========================================================================
     *
     * "Longest increasing subsequence somewhere in the array"
     * is too vague to transition from.
     *
     * So force one decision:
     *
     *      nums[i] MUST be the final element.
     *
     * Now the problem becomes local:
     *
     *      Which earlier j can come immediately before i?
     *
     * For LIS:
     *
     *      j < i
     *      nums[j] < nums[i]
     *
     * If a valid chain ending at j has length dp[j],
     * appending nums[i] creates:
     *
     *      dp[j] + 1
     *
     * Therefore:
     *
     *      dp[i] = max(dp[i], dp[j] + 1)
     */

    /*
     * =========================================================================
     * WHY dp[i] STARTS AT 1?
     * =========================================================================
     *
     * Even if no earlier value can precede nums[i],
     * nums[i] by itself is a valid subsequence.
     *
     * Therefore:
     *
     *      dp[i] = 1
     *
     * before considering predecessors.
     */

    /*
     * =========================================================================
     * WHY max(dp) AND NOT dp[n - 1]?
     * =========================================================================
     *
     * dp[i] means:
     *
     *      best LIS ending EXACTLY at i
     *
     * The global LIS may finish before the final array position.
     *
     * Example:
     *
     *      [1, 2, 3, 0]
     *
     * dp:
     *
     *      [1, 2, 3, 1]
     *
     * dp[n - 1] = 1
     * answer      = 3
     */

    /*
     * =========================================================================
     * REUSABLE MACHINE — BEST CHAIN ENDING AT i
     * =========================================================================
     *
     * This is the real reason LIS is worth learning.
     *
     * Generic template:
     *
     *      dp[i] = best valid chain ending at item i
     *
     *      for every earlier j:
     *
     *          if j can precede i:
     *              dp[i] = best(dp[i], dp[j] + contribution)
     *
     * LIS:
     *
     *      nums[j] < nums[i]
     *
     * Largest Divisible Subset:
     *
     *      nums[i] % nums[j] == 0
     *
     * Pair Chain:
     *
     *      pair[j].end < pair[i].start
     *
     * Longest String Chain:
     *
     *      word[j] is a valid predecessor of word[i]
     *
     * The transition rule changes.
     * The mental machine stays the same.
     */

    /*
     * =========================================================================
     * PHYSICAL CODE SKELETON
     * =========================================================================
     *
     * int[] dp = new int[n];
     * fill(dp, BASE);
     *
     * answer = BASE;
     *
     * i = 0;
     *
     * while (i < n) {
     *
     *     j = 0;
     *
     *     while (j < i) {
     *
     *         if (canPrecede(j, i)) {
     *             dp[i] = best(dp[i], dp[j] + contribution);
     *         }
     *
     *         j++;
     *     }
     *
     *     answer = best(answer, dp[i]);
     *     i++;
     * }
     *
     * return answer;
     */

    /*
     * =========================================================================
     * VISUAL DRY RUN — DP
     * =========================================================================
     *
     * nums = [10, 9, 2, 5, 3, 7, 101, 18]
     *
     * dp[i] = LIS ending EXACTLY at i
     *
     * value   best valid predecessor     dp
     * ------------------------------------------------
     * 10      none                       1
     *  9      none                       1
     *  2      none                       1
     *  5      2                          2
     *  3      2                          2
     *  7      5 or 3                     3
     * 101     7                          4
     * 18      7                          4
     *
     * answer = 4
     */

    /*
     * =========================================================================
     * COMMON FAILURES
     * =========================================================================
     *
     * 1. Using <=
     *
     *      LIS is STRICTLY increasing.
     *
     *      Correct:
     *          nums[j] < nums[i]
     *
     * 2. Returning dp[n - 1]
     *
     *      LIS can end anywhere.
     *
     * 3. Thinking this is sliding window
     *
     *      Subsequence allows skipping.
     *      Sliding window is for contiguous ranges.
     *
     * 4. Greedily extending one currently increasing sequence
     *
     *      Local choices can block a better future subsequence.
     */

    /*
     * =========================================================================
     * INTERVIEW ARTICULATION — PRIMARY SOLUTION
     * =========================================================================
     *
     * "I define dp[i] as the length of the longest increasing subsequence
     * ending exactly at index i.
     *
     * Every element alone gives dp[i] = 1.
     *
     * For each i, I inspect every earlier j.
     * If nums[j] < nums[i], then nums[i] can extend the subsequence ending
     * at j, so I update dp[i] with dp[j] + 1.
     *
     * Since the global LIS can end at any index, I keep the maximum dp value.
     *
     * There are O(n^2) predecessor checks and O(n) extra space."
     *
     * Correctness:
     *
     * For each i, we examine every possible previous element that can legally
     * precede nums[i], so every increasing subsequence ending at i is covered.
     * Taking the best such predecessor gives the optimal dp[i].
     *
     * Complexity:
     *
     *      Time  = O(n^2)
     *      Space = O(n)
     */

    /**
     * =========================================================================
     * FOLLOW-UP OPTIMIZATION — O(n log n)
     * =========================================================================
     *
     * RETENTION PRIORITY:
     *
     *      Recognize the idea.
     *      Be able to explain the invariant.
     *      Do NOT make this the only solution you depend on under pressure.
     *
     * Core idea:
     *
     *      For the SAME subsequence length,
     *      a smaller ending value dominates a larger ending value.
     *
     * Example:
     *
     *      length 2 ending at 5
     *      length 2 ending at 3
     *
     * Keep 3.
     *
     * Anything that can extend 5 can also extend 3,
     * while some future values can extend 3 but not 5.
     *
     * Therefore:
     *
     *      tails[len - 1]
     *      = smallest possible tail for subsequence length len
     *
     * MEMORY HOOK:
     *
     *      SAME LENGTH -> KEEP SMALLER TAIL
     */
    static class OptimalFollowUp {

        public int lengthOfLIS(int[] nums) {

            if (nums == null || nums.length == 0) {
                return 0;
            }

            /*
             * tails[length - 1]
             * = smallest tail found for an increasing subsequence of that length.
             *
             * The array has nums.length CAPACITY,
             * but only indices [0, lisLen) contain meaningful state.
             */
            int[] tails = new int[nums.length];

            int lisLen = 0;
            int i = 0;

            while (i < nums.length) {

                int currentValue = nums[i];

                /*
                 * Search ONLY the meaningful prefix:
                 *
                 *      tails[0 ... lisLen - 1]
                 *
                 * NOT the whole allocated array.
                 *
                 * Search interval is half-open:
                 *
                 *      [left, right)
                 */
                int left = 0;
                int right = lisLen; // Search only the valid prefix: tails[0 ... lisLen).

                while (left < right) {

                    int middle = left
                            + (right - left) / 2;

                    if (tails[middle] < currentValue) {
                        left = middle + 1;
                    } else {
                        right = middle;
                    }
                }

                /*
                 * left is now the first position whose tail >= currentValue.
                 *
                 * If left == lisLen, no such tail existed,
                 * so currentValue extends the longest subsequence found so far.
                 *
                 * Otherwise it replaces a larger/equal tail for the SAME length.
                 */
                int tailPosition = left;

                tails[tailPosition] = currentValue;

                if (tailPosition == lisLen) {
                    lisLen++;
                }

                i++;
            }

            return lisLen;
        }
    }

    /*
     * =========================================================================
     * VARIABLE MAP — KEEP THE ROLES SEPARATE
     * =========================================================================
     *
     * i
     *      current position in nums[]
     *
     * current
     *      nums[i]
     *
     * tails
     *      array capacity = nums.length
     *
     * lisLen
     *      number of MEANINGFUL values currently stored in tails
     *
     * IMPORTANT:
     *
     *      only tails[0 ... lisLen - 1] is valid algorithm state
     *
     *      binary search range = [0, lisLen)
     *
     * Example:
     *
     *      tails  = [2, 3, 0, 0, 0, 0]
     *                ^^^^
     *                valid
     *
     *      lisLen = 2
     *
     * The unused zeroes are only Java array initialization.
     * They are NOT searched.
     */

    /*
     * =========================================================================
     * VISUAL DRY RUN — O(n log n) FULL STATE EVOLUTION
     * =========================================================================
     *
     * nums = [10, 9, 2, 5, 3, 7, 101, 18]
     *
     * INVARIANT:
     *
     *      tails[k] =
     *      smallest possible tail for an increasing subsequence
     *      of length k + 1
     *
     * +------+---------+--------------------------+--------------------+------+
     * | Step | Current | First value >= current   | meaningful tails   | LIS  |
     * +------+---------+--------------------------+--------------------+------+
     * |  1   |   10    | none                     | [10]               |  1   |
     * |  2   |    9    | 10                       | [9]                |  1   |
     * |  3   |    2    | 9                        | [2]                |  1   |
     * |  4   |    5    | none                     | [2,5]              |  2   |
     * |  5   |    3    | 5                        | [2,3]              |  2   |
     * |  6   |    7    | none                     | [2,3,7]            |  3   |
     * |  7   |  101    | none                     | [2,3,7,101]        |  4   |
     * |  8   |   18    | 101                      | [2,3,7,18]         |  4   |
     * +------+---------+--------------------------+--------------------+------+
     *
     * Final:
     *
     *      lisLen = 4
     *
     * IMPORTANT:
     *
     *      tails[] is a compressed state representation.
     *      It is NOT guaranteed to be the actual LIS.
     */

    /*
     * =========================================================================
     * VISUAL DRY RUN — REPLACE VS APPEND
     * =========================================================================
     *
     * CASE 1 — REPLACE
     *
     *      tails   = [2,5]
     *      current = 3
     *
     *      first >= 3 is 5
     *
     *      [2,5]
     *         |
     *         v
     *      [2,3]
     *
     *      lisLen stays 2.
     *
     * Meaning:
     *
     *      We did NOT discover a longer subsequence.
     *      We improved the tail for an already achievable length.
     *
     * -------------------------------------------------------------------------
     * CASE 2 — APPEND
     * -------------------------------------------------------------------------
     *
     *      tails   = [2,3]
     *      current = 7
     *
     *      no existing tail >= 7
     *
     *      [2,3]
     *           \
     *            + 7
     *
     *      [2,3,7]
     *
     *      lisLen grows from 2 to 3.
     *
     * Meaning:
     *
     *      We really discovered a longer increasing subsequence.
     *
     * MEMORY:
     *
     *      REPLACE = same length, better future
     *      APPEND  = longer length discovered
     */

    /*
     * =========================================================================
     * VISUAL DRY RUN — MANUAL BINARY SEARCH
     * =========================================================================
     *
     * Suppose:
     *
     *      tails   = [2,3,7,101]
     *      lisLen = 4
     *      current = 18
     *
     * Goal:
     *
     *      find FIRST value >= 18
     *
     * Search interval is:
     *
     *      [left, right)
     *
     *      left = 0
     *      right = 4
     *
     * +-----------+------+-------+-----+------------+-------------------------+
     * | Iteration | left | rightX | mid | tail[mid]  | Decision                |
     * +-----------+------+-------+-----+------------+-------------------------+
     * |     1     |  0   |   4   |  2  |     7      | 7 < 18 -> left = 3    |
     * |     2     |  3   |   4   |  3  |   101      | >=18 -> right = 3     |
     * +-----------+------+-------+-----+------------+-------------------------+
     *
     * Stop:
     *
     *      left == right == 3
     *
     * Therefore:
     *
     *      first value >= 18 is at index 3
     *
     * Replace:
     *
     *      [2,3,7,101]
     *
     * becomes:
     *
     *      [2,3,7,18]
     */

    /*
     * =========================================================================
     * VISUAL DRY RUN — DUPLICATES / STRICTNESS
     * =========================================================================
     *
     * nums = [7,7,7,7]
     *
     * Strict LIS searches:
     *
     *      first tail >= current
     *
     * +------+---------+----------------------+---------+------+
     * | Step | Current | Action               | tails   | size |
     * +------+---------+----------------------+---------+------+
     * |  1   |    7    | append               | [7]     |  1   |
     * |  2   |    7    | replace index 0      | [7]     |  1   |
     * |  3   |    7    | replace index 0      | [7]     |  1   |
     * |  4   |    7    | replace index 0      | [7]     |  1   |
     * +------+---------+----------------------+---------+------+
     *
     * Answer:
     *
     *      1
     *
     * This is why strict LIS uses FIRST >= current.
     */

    /*
     * =========================================================================
     * WHY FIRST >= current?
     * =========================================================================
     *
     * Strict LIS must NOT allow duplicates to extend the length.
     *
     * Example:
     *
     *      [2, 2]
     *
     * answer must remain 1.
     *
     * So the second 2 replaces the first 2.
     *
     * Therefore strict LIS searches for:
     *
     *      first value >= current
     *
     * If the problem asks for LONGEST NON-DECREASING subsequence,
     * equal values ARE allowed to extend.
     *
     * Then search for:
     *
     *      first value > current
     */

    /*
     * =========================================================================
     * WHY DOES size EQUAL LIS LENGTH?
     * =========================================================================
     *
     * Replacing a tail does not create a new length.
     * It only gives an already achievable length a better ending value.
     *
     * size grows only when current is greater than every existing tail.
     *
     * Then current can extend an already achievable subsequence of length size,
     * so a subsequence of length size + 1 really exists.
     *
     * Therefore:
     *
     *      size = LIS length
     *
     * IMPORTANT:
     *
     * tails[] is NOT guaranteed to be the actual LIS.
     */

    /*
     * =========================================================================
     * DP -> OPTIMAL DERIVATION
     * =========================================================================
     *
     * DP keeps many ending states.
     *
     * Ask:
     *
     *      "For the same amount of progress,
     *       are some states strictly worse than others?"
     *
     * For LIS:
     *
     *      same length + larger tail
     *
     * is dominated by:
     *
     *      same length + smaller tail
     *
     * So keep only one best representative per length.
     *
     * Those representatives are ordered,
     * which makes binary search possible.
     *
     * GENERAL INTERVIEW LESSON:
     *
     *      SAME PROGRESS + BETTER FUTURE OPTIONS
     *      -> DISCARD THE DOMINATED STATE
     */

    /*
     * =========================================================================
     * PRESSURE PRIORITY
     * =========================================================================
     *
     * MUST BE ABLE TO DERIVE WHEN BLANK:
     *
     *      brute-force take / skip recursion
     *
     * MUST BE ABLE TO TURN INTO WORKING DP:
     *
     *      memoized recursion
     *
     * MUST RECONSTRUCT AS THE MAIN REUSABLE PATTERN:
     *
     *      O(n^2) ending-at-i bottom-up DP
     *
     * MUST RECOGNIZE / EXPLAIN:
     *
     *      smaller-tail dominance
     *      tails + binary search
     *
     * SAFE TO LOOK UP / RE-DERIVE LATER:
     *
     *      actual LIS reconstruction
     *      Fenwick / segment-tree variants
     *      specialized counting optimizations
     */

    /*
     * =========================================================================
     * VARIATION MAP
     * =========================================================================
     *
     * LONGEST NON-DECREASING SUBSEQUENCE
     *
     *      DP condition:
     *          nums[j] <= nums[i]
     *
     *      tails search:
     *          first > current
     *
     * -------------------------------------------------------------------------
     * PRINT ACTUAL LIS
     *
     *      Length alone is not enough.
     *      Track parent/predecessor information and reconstruct.
     *
     * -------------------------------------------------------------------------
     * NUMBER OF LIS — LeetCode 673
     *
     *      dp length alone is not enough.
     *
     *      Track:
     *          length[i]
     *          count[i]
     *
     * -------------------------------------------------------------------------
     * LONGEST CONTINUOUS INCREASING SUBSEQUENCE — LeetCode 674
     *
     *      Contiguous.
     *      NOT this DP pattern.
     *
     *      Just maintain current consecutive run.
     */

    /*
     * =========================================================================
     * INTERVIEW ARTICULATION — THE FULL DP LADDER
     * =========================================================================
     *
     * RECURSION:
     *
     *      "At each index I can skip the current number, or take it if it is
     *       larger than the previously selected number. That gives the state
     *       (index, previousIndex) and a take/skip recurrence."
     *
     * MEMOIZATION:
     *
     *      "The same (index, previousIndex) state can be reached through
     *       different decision paths, so I cache each state. There are O(n^2)
     *       states, giving O(n^2) time and O(n^2) memo space."
     *
     * BOTTOM-UP DP:
     *
     *      "I can simplify the state by defining dp[i] as the LIS ending
     *       exactly at i. I scan all earlier j, and if nums[j] < nums[i],
     *       i can extend dp[j]. The answer is max(dp), because the LIS can
     *       end anywhere."
     *
     * OPTIMAL FOLLOW-UP:
     *
     *      "For equal subsequence length, a smaller tail dominates a larger
     *       tail. I keep the smallest tail for every achievable length and
     *       binary-search the first tail >= current. This gives O(n log n)."
     */

    /*
     * =========================================================================
     * REINFORCEMENT — PATTERN TRANSFER
     * =========================================================================
     *
     * 1. Largest Divisible Subset
     *
     *      SAME MACHINE:
     *          best chain ending at i
     *
     *      CHANGE ONLY:
     *          predecessor rule
     *
     *      nums[i] % nums[j] == 0
     *
     * -------------------------------------------------------------------------
     * 2. Maximum Length of Pair Chain
     *    LeetCode 646
     *
     *      SAME MACHINE:
     *          best chain ending at pair i
     *
     *      predecessor rule:
     *
     *          pair[j].end < pair[i].start
     *
     * -------------------------------------------------------------------------
     * 3. Longest String Chain
     *
     *      SAME MACHINE:
     *          best chain ending at word i
     *
     *      predecessor rule:
     *
     *          word[j] can become word[i]
     *
     * -------------------------------------------------------------------------
     * 4. Longest Bitonic Subsequence
     *
     *      Reuse ending-at-i thinking from both directions.
     *
     * -------------------------------------------------------------------------
     * 5. Russian Doll Envelopes
     *    LeetCode 354
     *
     *      Sort first dimension carefully.
     *      Then reduce second dimension to LIS.
     *
     *      Useful optimization follow-up,
     *      but not required to reconstruct basic LIS DP.
     */

    /*
     * =========================================================================
     * INTERVIEW ANTI-FREEZE SCRIPT
     * =========================================================================
     *
     * If I am blank, say and do this:
     *
     *      "I'll first express this top-down so the state and choices are
     *       explicit. Once the recurrence is correct, I'll memoize repeated
     *       states. Then I can convert it to bottom-up if useful."
     *
     * For LIS:
     *
     *      STATE:
     *          (index, previousIndex)
     *
     *      CHOICES:
     *          TAKE / SKIP
     *
     *      BASE:
     *          index == n -> 0
     *
     *      COMBINE:
     *          max(take, skip)
     *
     *      REPEATED STATE:
     *          memo[index][previousIndex + 1]
     *
     *      CLEANER BOTTOM-UP STATE:
     *          dp[i] = best LIS ending at i
     *
     * Even if tails[] is completely forgotten,
     * this path still gets to a correct O(n^2) solution.
     */

    /*
     * =========================================================================
     * FINAL 15-SECOND RECALL CARD
     * =========================================================================
     *
     * RANDOM PROBLEM:
     *      ordered items + choose/skip + best chain
     *
     * IF BLANK:
     *      state -> choices -> recursion -> memoize
     *
     * THEN SIMPLIFY:
     *      "Force i to be last."
     *
     * WRITE:
     *      dp[i] = best chain ending at i
     *
     * DO:
     *      scan earlier j
     *      if j can precede i -> extend
     *
     * LIS SPECIFIC:
     *      nums[j] < nums[i]
     *
     * ANSWER:
     *      max(dp)
     *
     * FOLLOW-UP:
     *      same length -> smaller tail wins
     */

    private static void assertEqual(int actual, int expected, String testName) {

        if (actual != expected) {
            throw new AssertionError(
                    testName
                            + " FAILED | expected = "
                            + expected
                            + ", actual = "
                            + actual
            );
        }

        System.out.println("PASSED: " + testName);
    }

    public static void main(String[] args) {

        RecursiveSolution recursive = new RecursiveSolution();
        MemoizedSolution memoized = new MemoizedSolution();
        DPSolution dp = new DPSolution();
        OptimalFollowUp optimal = new OptimalFollowUp();

        int[][] tests = {
                {10, 9, 2, 5, 3, 7, 101, 18},
                {0, 1, 0, 3, 2, 3},
                {7, 7, 7, 7},
                {5, 4, 3, 2, 1},
                {1, 2, 3, 4, 5},
                {3, 4, 1, 2},
                {42}
        };

        int[] expected = {
                4,
                4,
                1,
                1,
                5,
                2,
                1
        };

        int index = 0;

        while (index < tests.length) {

            assertEqual(
                    recursive.lengthOfLIS(tests[index]),
                    expected[index],
                    "Recursive test " + (index + 1)
            );

            assertEqual(
                    memoized.lengthOfLIS(tests[index]),
                    expected[index],
                    "Memoized test " + (index + 1)
            );

            assertEqual(
                    dp.lengthOfLIS(tests[index]),
                    expected[index],
                    "Bottom-up DP test " + (index + 1)
            );

            assertEqual(
                    optimal.lengthOfLIS(tests[index]),
                    expected[index],
                    "Optimal follow-up test " + (index + 1)
            );

            index++;
        }

        System.out.println();
        System.out.println("All LIS tests passed.");
    }
}
