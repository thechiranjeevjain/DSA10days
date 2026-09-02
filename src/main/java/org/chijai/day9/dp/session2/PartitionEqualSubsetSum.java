package org.chijai.day9.dp.session2;

/**
 * Partition Equal Subset Sum
 *
 * =============================================================================
 * 1. PROBLEM
 * =============================================================================
 *
 * Given a non-empty array nums containing positive integers, determine whether
 * all elements can be split into TWO subsets whose sums are equal.
 *
 * Every array position must belong to exactly one of the two subsets.
 * Every array position may therefore be used at most once.
 *
 * -----------------------------------------------------------------------------
 * Example 1
 * -----------------------------------------------------------------------------
 *
 * nums = [1, 5, 11, 5]
 *
 * total = 22
 *
 * One valid partition is:
 *
 *      [11]
 *      [1, 5, 5]
 *
 * Both sums are 11.
 *
 * Answer: true
 *
 * -----------------------------------------------------------------------------
 * Example 2
 * -----------------------------------------------------------------------------
 *
 * nums = [1, 2, 3, 5]
 *
 * total = 11
 *
 * An odd total cannot be divided into two equal integer sums.
 *
 * Answer: false
 *
 * -----------------------------------------------------------------------------
 * Example 3
 * -----------------------------------------------------------------------------
 *
 * nums = [2, 2, 3, 5]
 *
 * total  = 12
 * target = 6
 *
 * No subset forms 6.
 *
 * Answer: false
 *
 * =============================================================================
 * 2. FIRST-PRINCIPLES THOUGHT PROGRESSION
 * =============================================================================
 *
 * Do NOT begin by trying to remember:
 *
 *      "Partition Equal Subset Sum = 0/1 Knapsack."
 *
 * Reconstruct it from the English.
 *
 * -----------------------------------------------------------------------------
 * Step 1: What does "two equal subsets" mathematically mean?
 * -----------------------------------------------------------------------------
 *
 * Let their sums be A and B.
 *
 *      A = B
 *      A + B = total
 *
 * Therefore:
 *
 *      2A = total
 *      A = total / 2
 *
 * So I do NOT need to construct both subsets.
 *
 * I only need to answer:
 *
 *      "Can some subset form total / 2?"
 *
 * If yes, all remaining numbers automatically form the other half.
 *
 * Immediate consequence:
 *
 *      odd total -> impossible
 *
 * -----------------------------------------------------------------------------
 * Step 2: Look at ONE number. What choices exist?
 * -----------------------------------------------------------------------------
 *
 * For the subset I am trying to build, each array position gives exactly:
 *
 *      TAKE it
 *      SKIP it
 *
 * That naturally suggests a decision tree.
 *
 * To describe one recursive state, I only need:
 *
 *      index
 *          Where am I?
 *
 *      remaining
 *          How much sum do I still need?
 *
 * So the brute-force state is:
 *
 *      (index, remaining)
 *
 * Choices:
 *
 *      skip -> (index + 1, remaining)
 *
 *      take -> (index + 1, remaining - nums[index])
 *
 * -----------------------------------------------------------------------------
 * Step 3: Why DP?
 * -----------------------------------------------------------------------------
 *
 * Different take/skip paths can reach the same:
 *
 *      (index, remaining)
 *
 * Once that happens, the future work is identical.
 *
 * Cache those repeated states:
 *
 *      recursion -> memoization
 *
 * -----------------------------------------------------------------------------
 * Step 4: Turn the recursive meaning into a table
 * -----------------------------------------------------------------------------
 *
 * A natural 2-D definition is:
 *
 *      dp[i][sum]
 *
 *      = can the first i numbers form sum?
 *
 * For current number num:
 *
 *      SKIP:
 *          dp[i - 1][sum]
 *
 *      TAKE:
 *          dp[i - 1][sum - num]
 *
 * Therefore:
 *
 *      dp[i][sum]
 *          =
 *      dp[i - 1][sum]
 *          OR
 *      dp[i - 1][sum - num]
 *
 * Notice the important fact:
 *
 *      BOTH choices read the PREVIOUS ROW.
 *
 * That is exactly how "use this physical item at most once" appears in DP.
 *
 * -----------------------------------------------------------------------------
 * Step 5: Compress 2-D into 1-D
 * -----------------------------------------------------------------------------
 *
 * Row i only needs row i - 1.
 *
 * So we can collapse the table into:
 *
 *      boolean[] dp
 *
 * where:
 *
 *      dp[sum]
 *
 * means:
 *
 *      "Can the numbers processed so far form sum?"
 *
 * But after collapsing two rows into one array, we must still preserve
 * PREVIOUS-ROW behavior.
 *
 * That determines the loop direction.
 *
 *      current item usable once
 *          -> read old states
 *          -> iterate RIGHT to LEFT
 *
 * This gives:
 *
 *      dp[sum] = dp[sum] || dp[sum - num]
 *
 * with:
 *
 *      sum = target ... num
 *
 * -----------------------------------------------------------------------------
 * The entire reconstruction chain
 * -----------------------------------------------------------------------------
 *
 *      equal partition
 *              ↓
 *      one side = total / 2
 *              ↓
 *      subset target
 *              ↓
 *      take / skip each array position
 *              ↓
 *      (index, remaining)
 *              ↓
 *      repeated states
 *              ↓
 *      memoization
 *              ↓
 *      dp[i][sum]
 *              ↓
 *      take reads previous row
 *              ↓
 *      compress rows
 *              ↓
 *      preserve previous-row semantics
 *              ↓
 *      reverse traversal
 *
 * =============================================================================
 * 3. INTERVIEW-PREFERRED SOLUTION
 * =============================================================================
 */
public class PartitionEqualSubsetSum {

    /**
     * Preferred solution:
     *
     * Time:  O(n * target)
     * Space: O(target)
     */
    public static boolean canPartition(int[] nums) {

        int total = 0;

        for (int num : nums) {
            total += num;
        }

        if ((total & 1) == 1) {
            return false;
        }

        int target = total / 2;

        boolean[] dp = new boolean[target + 1];
        dp[0] = true;

        for (int num : nums) {

            // Descend so this number cannot reuse a state it created itself.
            // sum>=num ensures we do not read negative indices.
            for (int sum = target; sum >= num; sum--) {
                dp[sum] = dp[sum] || dp[sum - num];
            }
        }

        return dp[target];
    }

    /*
     * =============================================================================
     * 4. WHY THIS SOLUTION WORKS
     * =============================================================================
     *
     * Core invariant
     * -----------------------------------------------------------------------------
     *
     * After processing the first k numbers:
     *
     *      dp[sum] == true
     *
     * means:
     *
     *      sum can be formed using only those k processed array positions.
     *
     * Each processed array position contributes at most once.
     *
     * -----------------------------------------------------------------------------
     * Why dp[0] = true?
     * -----------------------------------------------------------------------------
     *
     * The empty subset forms sum 0.
     *
     * It is the seed from which all reachable positive sums grow.
     *
     * If every dp value started false, no transition could ever become true.
     *
     * -----------------------------------------------------------------------------
     * Why the transition is OR
     * -----------------------------------------------------------------------------
     *
     * For current number num and desired sum:
     *
     *      dp[sum]
     *
     * can be true in exactly two ways:
     *
     *      SKIP num:
     *          sum was already reachable.
     *
     *      TAKE num:
     *          sum - num was reachable before using num.
     *
     * Hence:
     *
     *      dp[sum]
     *          =
     *      dp[sum]
     *          OR
     *      dp[sum - num]
     *
     * -----------------------------------------------------------------------------
     * Why reverse traversal is correctness, not style
     * -----------------------------------------------------------------------------
     *
     * The 2-D recurrence reads:
     *
     *      dp[i - 1][sum - num]
     *
     * not:
     *
     *      dp[i][sum - num]
     *
     * Therefore the 1-D version must make dp[sum - num] behave as though it
     * still belongs to the previous row.
     *
     * Descending traversal does exactly that.
     *
     * When computing a larger sum, every smaller index that could be read has
     * NOT yet been updated for the current number.
     *
     * So the current number cannot feed into itself.
     *
     * =============================================================================
     * 5. VISUAL DRY RUN #1 — REACHABLE-SUM FRONTIER
     * =============================================================================
     *
     * nums   = [1, 5, 11, 5]
     * total  = 22
     * target = 11
     *
     * Think of DP as gradually expanding the set of sums we can build.
     *
     * +----------------------+-------------------------+
     * | Processed numbers    | Reachable sums          |
     * +----------------------+-------------------------+
     * | none                 | {0}                     |
     * | 1                    | {0, 1}                  |
     * | 1, 5                 | {0, 1, 5, 6}            |
     * | 1, 5, 11             | {0, 1, 5, 6, 11}        |
     * +----------------------+-------------------------+
     *
     * target 11 appears.
     *
     * Therefore:
     *
     *      true
     *
     * Mental picture:
     *
     *      each new number expands the reachable-sum frontier.
     *
     * =============================================================================
     * 6. VISUAL DRY RUN #2 — WHY BACKWARD WORKS
     * =============================================================================
     *
     * Suppose:
     *
     *      nums   = [1]
     *      target = 4
     *
     * Initial DP:
     *
     * +-----+---+---+---+---+---+
     * | sum | 0 | 1 | 2 | 3 | 4 |
     * +-----+---+---+---+---+---+
     * | dp  | T | F | F | F | F |
     * +-----+---+---+---+---+---+
     *
     * Process num = 1 from RIGHT -> LEFT:
     *
     * +------+----------------+---------+
     * | sum  | read           | result  |
     * +------+----------------+---------+
     * | 4    | dp[3] = false  | false   |
     * | 3    | dp[2] = false  | false   |
     * | 2    | dp[1] = false  | false   |
     * | 1    | dp[0] = true   | true    |
     * +------+----------------+---------+
     *
     * Final:
     *
     * +-----+---+---+---+---+---+
     * | sum | 0 | 1 | 2 | 3 | 4 |
     * +-----+---+---+---+---+---+
     * | dp  | T | T | F | F | F |
     * +-----+---+---+---+---+---+
     *
     * Critical observation:
     *
     *      dp[2] checked dp[1]
     *      BEFORE dp[1] became true.
     *
     * Therefore the new 1 cannot use itself again.
     *
     * =============================================================================
     * 7. VISUAL DRY RUN #3 — WHY FORWARD FAILS
     * =============================================================================
     *
     * Same starting point:
     *
     *      nums   = [1]
     *      target = 4
     *
     * Initial:
     *
     * +-----+---+---+---+---+---+
     * | sum | 0 | 1 | 2 | 3 | 4 |
     * +-----+---+---+---+---+---+
     * | dp  | T | F | F | F | F |
     * +-----+---+---+---+---+---+
     *
     * Incorrect LEFT -> RIGHT traversal:
     *
     * +------+------------------+------------------------------------------+
     * | sum  | update           | meaning                                  |
     * +------+------------------+------------------------------------------+
     * | 1    | dp[1] |= dp[0]  | one copy of 1                            |
     * | 2    | dp[2] |= dp[1]  | reads dp[1] JUST created by same 1       |
     * | 3    | dp[3] |= dp[2]  | reuses same 1 again                      |
     * | 4    | dp[4] |= dp[3]  | reuses same 1 again                      |
     * +------+------------------+------------------------------------------+
     *
     * One physical number:
     *
     *      1
     *
     * has magically become:
     *
     *      1 + 1 + 1 + 1
     *
     * That is NOT 0/1 knapsack anymore.
     *
     * It has accidentally become an unbounded-reuse transition.
     *
     * =============================================================================
     * 8. VISUAL DRY RUN #4 — 2-D ROW TO 1-D ROW
     * =============================================================================
     *
     * nums = [2, 3]
     * target = 5
     *
     * 2-D meaning:
     *
     *      dp[i][s]
     *      = can first i numbers make s?
     *
     * +----------------+---+---+---+---+---+---+
     * |                | 0 | 1 | 2 | 3 | 4 | 5 |
     * +----------------+---+---+---+---+---+---+
     * | no numbers     | T | F | F | F | F | F |
     * | after 2        | T | F | T | F | F | F |
     * | after 2, 3     | T | F | T | T | F | T |
     * +----------------+---+---+---+---+---+---+
     *
     * To create sum 5 while processing 3:
     *
     *      5 = previous-row 2 + current 3
     *
     * The TAKE edge literally comes from the previous row.
     *
     * When the rows are collapsed, reverse traversal preserves exactly that
     * dependency.
     *
     * =============================================================================
     * 9. BRUTE FORCE -> MEMO -> DP EVOLUTION
     * =============================================================================
     *
     * Brute-force state:
     *
     *      (index, remaining)
     *
     * Each call branches:
     *
     *      skip
     *      take
     *
     * Time:
     *
     *      O(2^n)
     *
     * Repeated states appear.
     *
     * Memoization stores each:
     *
     *      (index, remaining)
     *
     * once.
     *
     * Time becomes:
     *
     *      O(n * target)
     *
     * Bottom-up DP stores the same information in table form.
     *
     * 2-D:
     *
     *      dp[i][sum]
     *
     * 1-D compression removes the processed-item dimension while reverse
     * traversal preserves its meaning.
     */

    /**
     * Brute force.
     *
     * Useful as the FIRST derivation because the take/skip choices are obvious.
     *
     * Time:  O(2^n)
     * Space: O(n) recursion depth
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

            if (remaining == 0) {
                return true;
            }

            if (index == nums.length || remaining < 0) {
                return false;
            }

            if (dfs(nums, index + 1, remaining)) {
                return true;
            }

            return dfs(nums, index + 1, remaining - nums[index]);
        }
    }

    /**
     * Memoized recursion.
     *
     * Same take/skip state as brute force, but every
     * (index, remaining) state is solved once.
     *
     * Time:  O(n * target)
     * Space: O(n * target)
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

        private boolean dfs(int[] nums, int index, int remaining) {

            if (remaining == 0) {
                return true;
            }

            if (index == nums.length || remaining < 0) {
                return false;
            }

            if (memo[index][remaining] != null) {
                return memo[index][remaining];
            }

            boolean skip = dfs(nums, index + 1, remaining);

            if (skip) {
                memo[index][remaining] = true;
                return true;
            }

            boolean take = dfs(
                    nums,
                    index + 1,
                    remaining - nums[index]
            );

            memo[index][remaining] = take;

            return take;
        }
    }

    /*
     * =============================================================================
     * 10. FORMAL CORRECTNESS — SHORTEST DEFENSIBLE PROOF
     * =============================================================================
     *
     * Invariant:
     *
     * After processing the first k numbers, dp[s] is true exactly when some
     * subset of those k numbers has sum s.
     *
     * Base:
     *
     * Before processing any number:
     *
     *      dp[0] = true
     *
     * because the empty subset forms 0.
     *
     * Every positive sum is false.
     *
     * Step:
     *
     * Process current number x.
     *
     * A sum s is reachable after considering x iff:
     *
     *      1. s was already reachable without x
     *
     *         OR
     *
     *      2. s - x was reachable before x, then we take x once.
     *
     * Reverse traversal guarantees dp[s - x] is still a previous-prefix state.
     * Therefore x cannot be reused in the same iteration.
     *
     * Hence the invariant remains true.
     *
     * After every number is processed, dp[target] answers whether a valid
     * target subset exists.
     *
     * =============================================================================
     * 11. COMMON WRONG TURNS
     * =============================================================================
     *
     * Wrong turn 1:
     *
     *      Iterate sum left -> right.
     *
     * Why wrong:
     *
     *      current item can read a state created by itself
     *      -> accidental unlimited reuse.
     *
     * -----------------------------------------------------------------------------
     * Wrong turn 2:
     *
     *      Forget dp[0] = true.
     *
     * Why wrong:
     *
     *      there is no reachable seed from which positive sums can grow.
     *
     * -----------------------------------------------------------------------------
     * Wrong turn 3:
     *
     *      Start DP before checking odd total.
     *
     * Why wrong:
     *
     *      not incorrect, but unnecessary work.
     *
     * -----------------------------------------------------------------------------
     * Wrong turn 4:
     *
     *      Treat 0/1 Knapsack and Bounded Knapsack as identical names.
     *
     * Better terminology:
     *
     *      0/1 Knapsack:
     *          each physical item is usable 0 or 1 time.
     *
     *      Bounded Knapsack:
     *          an item type has a finite quantity, possibly greater than 1.
     *
     *      0/1 is the simplest bounded case.
     *
     * -----------------------------------------------------------------------------
     * Wrong turn 5:
     *
     *      Memorize "reverse loop" without knowing why.
     *
     * Better reconstruction:
     *
     *      TAKE reads previous row
     *          -> compressed array must preserve previous-row value
     *          -> traverse backward.
     *
     * =============================================================================
     * 12. 30-SECOND RECONSTRUCTION CARD
     * =============================================================================
     *
     * PARTITION EQUAL SUBSET SUM
     *
     * Equal halves
     *      -> one subset must make total / 2.
     *
     * Odd total
     *      -> false.
     *
     * Each array position usable once
     *      -> 0/1 subset DP.
     *
     * State:
     *
     *      dp[s]
     *      = can processed numbers make s?
     *
     * Seed:
     *
     *      dp[0] = true
     *
     * Transition:
     *
     *      dp[s] |= dp[s - num]
     *
     * Direction:
     *
     *      target -> num
     *
     * Why:
     *
     *      descending makes dp[s - num]
     *      behave like PREVIOUS ROW.
     *
     * Core contrast:
     *
     *      NO reuse  -> backward
     *      YES reuse -> forward
     *
     * Complexity:
     *
     *      O(n * target) time
     *      O(target) space
     *
     * =============================================================================
     * 13. BLANK-EDITOR RECONSTRUCTION QUESTIONS
     * =============================================================================
     *
     * If you forget the solution months later, ask:
     *
     * 1. What does equal partition imply mathematically?
     *
     * 2. What is the transformed target?
     *
     * 3. What choices do I make for one array position?
     *
     * 4. What information completely describes the remaining future?
     *
     * 5. Do states repeat?
     *
     * 6. What does dp[s] mean in ONE English sentence?
     *
     * 7. Does TAKE depend on previous row or current row?
     *
     * 8. Can this physical item be used again immediately?
     *
     * 9. If I compress dimensions, what dependency must I preserve?
     *
     * These questions are more durable than memorizing a LeetCode number.
     *
     * =============================================================================
     * 14. REUSABLE 0/1 SUBSET-DP TEMPLATE
     * =============================================================================
     *
     * Boolean reachability:
     *
     *      boolean[] dp = new boolean[target + 1];
     *      dp[0] = true;
     *
     *      for (int item : items) {
     *          for (int s = target; s >= item; s--) {
     *              dp[s] = dp[s] || dp[s - item];
     *          }
     *      }
     *
     * Ask before using it:
     *
     *      Is each PHYSICAL item usable at most once?
     *
     * If YES, backward traversal is a strong signal.
     *
     * =============================================================================
     * 15. HORIZONTAL MASTERY — ONE FAMILY, MANY PROBLEMS
     * =============================================================================
     *
     * Do not memorize these as unrelated algorithms.
     *
     * Reuse the same three questions:
     *
     *      1. What does dp[s] store?
     *             boolean / count / min / max
     *
     *      2. Can the current item be reused?
     *             NO  -> backward
     *             YES -> forward
     *
     *      3. What target or quantity am I constructing?
     *
     * +----------------------------+---------------------------+----------+-------------+
     * | Problem                    | dp[s] means               | Reuse?   | Combine     |
     * +----------------------------+---------------------------+----------+-------------+
     * | Partition Equal Subset Sum | reachable?                | once     | OR          |
     * | Subset Sum                 | reachable?                | once     | OR          |
     * | Count Subsets Sum K        | number of subsets         | once     | addition    |
     * | Target Sum                 | number of subsets         | once     | addition    |
     * | Last Stone Weight II       | reachable partition sum   | once     | OR          |
     * | 0/1 Knapsack               | maximum value             | once     | max         |
     * | Coin Change II             | number of combinations    | unlimited| addition    |
     * | Coin Change                | minimum coins             | unlimited| min         |
     * | Perfect Squares            | minimum pieces            | unlimited| min         |
     * +----------------------------+---------------------------+----------+-------------+
     *
     * -----------------------------------------------------------------------------
     * Variation: Subset Sum
     * -----------------------------------------------------------------------------
     *
     * Same exact boolean DP.
     *
     * Only the target is given directly instead of being total / 2.
     *
     * -----------------------------------------------------------------------------
     * Variation: Count Subsets With Sum K
     * -----------------------------------------------------------------------------
     *
     * Change:
     *
     *      boolean -> count
     *
     *      OR -> addition
     *
     * Keep:
     *
     *      reverse traversal
     *
     * because each array position is still usable once.
     *
     * -----------------------------------------------------------------------------
     * Variation: Target Sum
     * -----------------------------------------------------------------------------
     *
     * Suppose:
     *
     *      P = sum of numbers assigned +
     *      N = sum of numbers assigned -
     *
     * We know:
     *
     *      P - N = target
     *      P + N = total
     *
     * Add:
     *
     *      2P = total + target
     *
     * Therefore:
     *
     *      P = (total + target) / 2
     *
     * So Target Sum becomes:
     *
     *      count subsets with sum (total + target) / 2
     *
     * Same 0/1 family.
     *
     * -----------------------------------------------------------------------------
     * Variation: Last Stone Weight II
     * -----------------------------------------------------------------------------
     *
     * Partition stones into two groups.
     *
     * Instead of requiring exact equality, find a reachable subset sum as close
     * as possible to total / 2.
     *
     * Same reachability machinery.
     *
     * -----------------------------------------------------------------------------
     * Variation: 0/1 Knapsack
     * -----------------------------------------------------------------------------
     *
     * Weight controls capacity.
     *
     * DP no longer stores boolean reachability.
     *
     * It stores best value.
     *
     * Combine with:
     *
     *      max
     *
     * But each physical item is still usable once:
     *
     *      reverse capacity traversal.
     *
     * -----------------------------------------------------------------------------
     * Variation: Coin Change / Coin Change II
     * -----------------------------------------------------------------------------
     *
     * Coin denomination can be used repeatedly.
     *
     * That changes the dependency:
     *
     *      current row may feed current row.
     *
     * Therefore:
     *
     *      LEFT -> RIGHT
     *
     * This is the reusable contrast:
     *
     *      take once      -> previous row -> backward
     *      take again     -> current row  -> forward
     *
     * =============================================================================
     * 16. PATTERN BOUNDARY
     * =============================================================================
     *
     * Do NOT force this 1-D sum DP onto every partition problem.
     *
     * Example:
     *
     *      Partition to K Equal Sum Subsets
     *
     * Now we must coordinate multiple buckets, not just answer whether ONE
     * target sum is reachable.
     *
     * Typical approaches move toward:
     *
     *      backtracking
     *      pruning
     *      bitmask DP
     *
     * Knowing where a pattern stops is part of pattern mastery.
     *
     * =============================================================================
     * 17. INTERVIEW ARTICULATION
     * =============================================================================
     *
     * Strong explanation:
     *
     * "If the array can be partitioned equally, one side must sum to half of
     * the total, so I first reject odd totals and reduce the problem to subset
     * sum with target total / 2.
     *
     * Each array position can be used at most once, so this is a 0/1 subset-DP
     * problem. I define dp[s] as whether sum s can be formed using the numbers
     * processed so far. dp[0] is true because the empty subset forms zero.
     *
     * For each number, sum s remains reachable if it was already reachable, or
     * becomes reachable if s - num was reachable before using this number.
     * Therefore dp[s] |= dp[s - num].
     *
     * I iterate sums from right to left because the 2-D take transition reads
     * the previous row. Descending order preserves that previous-row meaning
     * after space compression and prevents the current number from being reused.
     *
     * The complexity is O(n * target) time and O(target) space."
     *
     * -----------------------------------------------------------------------------
     * Correctness in one sentence
     * -----------------------------------------------------------------------------
     *
     * Every transition represents either skipping the current number or taking
     * it exactly once, and reverse traversal prevents any third illegal choice:
     * taking the same physical number again.
     *
     * -----------------------------------------------------------------------------
     * If interviewer asks: "Why backward?"
     * -----------------------------------------------------------------------------
     *
     * "Because the take transition conceptually reads dp[i - 1][s - num].
     * After compressing rows, backward traversal makes that source cell remain
     * an old-row value until I read it."
     *
     * -----------------------------------------------------------------------------
     * If interviewer asks: "Why not two subsets directly?"
     * -----------------------------------------------------------------------------
     *
     * "Their sums add to total and must be equal, so finding one subset of
     * total / 2 automatically determines the other."
     *
     * -----------------------------------------------------------------------------
     * If interviewer asks: "Why is Coin Change different?"
     * -----------------------------------------------------------------------------
     *
     * "Coin Change permits immediate reuse of the same denomination, so its
     * current row may depend on itself. That is why capacity/sum can move
     * forward instead of backward."
     *
     * =============================================================================
     * 18. RELATED / REINFORCEMENT PROBLEMS
     * =============================================================================
     *
     * Learn this one deeply, then use these to reinforce the SAME machinery:
     *
     * High-priority:
     *
     *      1. Subset Sum
     *      2. Target Sum
     *      3. Last Stone Weight II
     *      4. 0/1 Knapsack
     *
     * Contrast problems:
     *
     *      5. Coin Change
     *      6. Coin Change II
     *
     * Pattern boundary:
     *
     *      7. Partition to K Equal Sum Subsets
     *
     * The goal is not seven memorized solutions.
     *
     * The goal is to see what stays fixed and what changes.
     *
     * =============================================================================
     * 19. MASTERY CHECK
     * =============================================================================
     *
     * Without looking above, can I reconstruct:
     *
     *      □ why equal partition becomes total / 2?
     *
     *      □ why odd total fails immediately?
     *
     *      □ the take/skip brute-force state?
     *
     *      □ why repeated states create DP?
     *
     *      □ what dp[s] means?
     *
     *      □ why dp[0] is true?
     *
     *      □ the transition?
     *
     *      □ why TAKE conceptually reads the previous row?
     *
     *      □ why row compression forces reverse traversal?
     *
     *      □ the forward-loop counterexample?
     *
     *      □ NO reuse -> backward?
     *
     *      □ YES reuse -> forward?
     *
     *      □ how Target Sum transforms into subset counting?
     *
     *      □ how Last Stone Weight II reuses the same reachable-sum DP?
     *
     *      □ how to explain all of this aloud in under two minutes?
     *
     * If I can do those from a blank editor, I own the pattern.
     *
     * =============================================================================
     * 20. TESTS
     * =============================================================================
     */

    public static void main(String[] args) {

        assert canPartition(new int[]{1, 5, 11, 5});
        assert !canPartition(new int[]{1, 2, 3, 5});
        assert !canPartition(new int[]{2, 2, 3, 5});

        assert canPartition(new int[]{3, 3, 3, 4, 5});
        assert canPartition(new int[]{1, 1});
        assert !canPartition(new int[]{2});

        assert canPartition(new int[]{100, 100});
        assert canPartition(new int[]{2, 2, 2, 2});
        assert canPartition(new int[]{2, 3, 7, 8});

        assert !canPartition(new int[]{1, 2, 5});
        assert canPartition(new int[]{1, 2, 3, 4});

        // Previously mislabeled as impossible:
        // [8] and [5, 3] both sum to 8.
        assert canPartition(new int[]{8, 5, 3});

        assert !canPartition(new int[]{9, 1, 1});
        assert canPartition(new int[]{4, 4, 4, 4});
        assert canPartition(new int[]{6, 1, 2, 3});
        assert canPartition(new int[]{5, 5, 5, 5});

        int[][] agreementTests = {
                {1, 5, 11, 5},
                {1, 2, 3, 5},
                {2, 2, 3, 5},
                {3, 3, 3, 4, 5},
                {1, 1},
                {2},
                {2, 3, 7, 8},
                {1, 2, 5},
                {8, 5, 3}
        };

        int index = 0;

        while (index < agreementTests.length) {

            int[] test = agreementTests[index];

            boolean brute = new BruteForce().canPartition(test);
            boolean memo = new Memoized().canPartition(test);
            boolean optimal = canPartition(test);

            assert brute == memo;
            assert memo == optimal;

            index++;
        }

        System.out.println("All assertions passed.");
    }
}

/*
 * FINAL RECALL LINE
 *
 *      TAKE once  -> preserve previous row -> go backward.
 *      TAKE again -> current row may feed itself -> go forward.
 *
 * If I forget the code, reconstruct the dependency.
 */
