package org.chijai.day9.dp.session2;

import java.util.*;

/**
 * ============================================================================
 * LEETCODE 322. COIN CHANGE
 * ============================================================================
 *
 * OFFICIAL LINK
 * https://leetcode.com/problems/coin-change/
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Dynamic Programming
 * Breadth-First Search
 *
 * ============================================================================
 * PROBLEM STATEMENT (FULL)
 * ============================================================================
 *
 * You are given an integer array coins representing coins of different
 * denominations and an integer amount representing a total amount of money.
 *
 * Return the fewest number of coins that you need to make up that amount.
 * If that amount of money cannot be made up by any combination of the coins,
 * return -1.
 *
 * You may assume that you have an infinite number of each kind of coin.
 *
 * Example 1:
 *
 * Input: coins = [1,2,5], amount = 11
 * Output: 3
 * Explanation: 11 = 5 + 5 + 1
 *
 * Example 2:
 *
 * Input: coins = [2], amount = 3
 * Output: -1
 *
 * Example 3:
 *
 * Input: coins = [1], amount = 0
 * Output: 0
 *
 * Constraints:
 *
 * 1 <= coins.length <= 12
 * 1 <= coins[i] <= 2^31 - 1
 * 0 <= amount <= 10^4
 *
 * ============================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern Name
 * ------------
 * Unbounded Knapsack DP (Minimization Form)
 *
 * Problem Archetype
 * -----------------
 * We have:
 *
 * - unlimited usage of each item
 * - target amount
 * - minimum number of items required
 *
 * This is one of the most important forms of
 * Unbounded Knapsack Dynamic Programming.
 *
 * ---------------------------------------------------------------------------
 * 🟢 CORE INVARIANT
 * ---------------------------------------------------------------------------
 *
 * dp[x]
 *
 * always means:
 *
 * "minimum number of coins required to build amount x"
 *
 * using ALL coin types processed so far.
 *
 * If amount x is currently impossible:
 *
 * dp[x] = INF
 *
 * We NEVER allow dp[x] to mean different things.
 *
 * Every transition must preserve this meaning.
 *
 * ---------------------------------------------------------------------------
 * Why It Works
 * ---------------------------------------------------------------------------
 *
 * Suppose we want dp[amount].
 *
 * For every coin:
 *
 * coin
 *
 * the final move could have been:
 *
 * previousAmount = amount - coin
 *
 * If we already know:
 *
 * dp[previousAmount]
 *
 * then:
 *
 * dp[amount]
 * =
 * min(
 *      current answer,
 *      dp[previousAmount] + 1
 * )
 *
 * because we add one final coin.
 *
 * This reduces a larger amount into a smaller amount.
 *
 * ---------------------------------------------------------------------------
 * When To Use
 * ---------------------------------------------------------------------------
 *
 * Use when:
 *
 * - unlimited reuse allowed
 * - minimum count needed
 * - target sum/amount exists
 * - optimal answer required
 *
 * Recognition Signals
 *
 * "infinite supply"
 * "fewest coins"
 * "minimum pieces"
 * "minimum operations"
 * "target sum"
 *
 * ---------------------------------------------------------------------------
 * Difference From Similar Patterns
 * ---------------------------------------------------------------------------
 *
 * 1) 0/1 Knapsack
 *
 * Item can be used once.
 *
 * Here:
 *
 * coin can be reused infinitely.
 *
 * ---------------------------------------------------------------------------
 *
 * 2) Coin Change II
 *
 * Counts combinations.
 *
 * This problem:
 *
 * minimizes coin count.
 *
 * State meaning differs.
 *
 * ---------------------------------------------------------------------------
 *
 * 3) Subset Sum
 *
 * asks existence.
 *
 * This problem:
 *
 * asks optimal minimum.
 *
 * ============================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine amounts laid on a number line:
 *
 * 0 1 2 3 4 5 ...
 *
 * We already know best answers for smaller amounts.
 *
 * To reach amount x:
 *
 * try every coin as the LAST coin used.
 *
 * This is the key perspective.
 *
 * ---------------------------------------------------------------------------
 * State Meaning
 * ---------------------------------------------------------------------------
 *
 * dp[x]
 *
 * =
 *
 * minimum coins required to create amount x.
 *
 * ---------------------------------------------------------------------------
 * Variable Meaning
 * ---------------------------------------------------------------------------
 *
 * amount
 *      target amount.
 *
 * coin
 *      candidate last coin.
 *
 * dp[x]
 *      optimal answer for x.
 *
 * INF
 *      impossible/unreached state.
 *
 * ---------------------------------------------------------------------------
 * Invariant #1
 * ---------------------------------------------------------------------------
 *
 * dp[0] = 0
 *
 * because zero amount needs zero coins.
 *
 * This is the anchor state.
 *
 * ---------------------------------------------------------------------------
 * Invariant #2
 * ---------------------------------------------------------------------------
 *
 * dp[x] is always the BEST answer discovered so far.
 *
 * Never store:
 *
 * - arbitrary count
 * - non-minimal count
 * - temporary count
 *
 * ---------------------------------------------------------------------------
 * Invariant #3
 * ---------------------------------------------------------------------------
 *
 * Transition only comes from smaller amount:
 *
 * x - coin
 *
 * Therefore every transition builds upon a valid subproblem.
 *
 * ---------------------------------------------------------------------------
 * Invariant #4
 * ---------------------------------------------------------------------------
 *
 * If dp[x] becomes finite,
 * there exists an actual construction producing x.
 *
 * We never fabricate answers.
 *
 * ---------------------------------------------------------------------------
 * Allowed Move
 * ---------------------------------------------------------------------------
 *
 * dp[x]
 * =
 * min(
 *      dp[x],
 *      dp[x - coin] + 1
 * )
 *
 * ---------------------------------------------------------------------------
 * Forbidden Move
 * ---------------------------------------------------------------------------
 *
 * Using:
 *
 * dp[x + coin]
 *
 * to define dp[x]
 *
 * violates subproblem ordering.
 *
 * ---------------------------------------------------------------------------
 * Termination Logic
 * ---------------------------------------------------------------------------
 *
 * After processing all amounts:
 *
 * dp[amount]
 *
 * is globally optimal.
 *
 * If still INF:
 *
 * impossible.
 *
 * return -1.
 *
 * ---------------------------------------------------------------------------
 * Why Naive Approaches Fail
 * ---------------------------------------------------------------------------
 *
 * Greedy:
 *
 * Always take largest coin.
 *
 * Counterexample:
 *
 * coins = [1,3,4]
 * amount = 6
 *
 * Greedy:
 *
 * 4 + 1 + 1 = 3 coins
 *
 * Optimal:
 *
 * 3 + 3 = 2 coins
 *
 * Greedy loses optimality.
 *
 * ---------------------------------------------------------------------------
 *
 * Exhaustive recursion:
 *
 * Tries every combination.
 *
 * Massive overlap:
 *
 * amount=100
 *
 * repeatedly solves same smaller amounts.
 *
 * DP removes repeated work.
 *
 * ============================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================================
 *
 * Wrong Approach #1
 * -----------------
 * Greedy largest coin first.
 *
 * Why it seems correct:
 *
 * Larger coin appears to reduce work fastest.
 *
 * Invariant Violation:
 *
 * Local optimal choice
 * does not guarantee global optimal answer.
 *
 * Counterexample:
 *
 * coins=[1,3,4]
 * amount=6
 *
 * Greedy => 3 coins
 * Optimal => 2 coins
 *
 * ---------------------------------------------------------------------------
 *
 * Wrong Approach #2
 * -----------------
 * Pure recursion.
 *
 * Why it seems correct:
 *
 * Explores all possibilities.
 *
 * Invariant Violation:
 *
 * No memoization.
 *
 * Same state solved repeatedly.
 *
 * Explosion:
 *
 * amount=10000
 *
 * becomes impractical.
 *
 * ---------------------------------------------------------------------------
 *
 * Wrong Approach #3
 * -----------------
 * Using Integer.MAX_VALUE directly.
 *
 * Example:
 *
 * dp[x - coin] + 1
 *
 * can overflow.
 *
 * Integer.MAX_VALUE + 1
 *
 * becomes negative.
 *
 * Corrupts invariant.
 *
 * Use safe INF.
 *
 * Example:
 *
 * amount + 1
 *
 * is sufficient.
 *
 * ---------------------------------------------------------------------------
 *
 * Interview Trap
 * --------------
 *
 * Candidate says:
 *
 * "Let's sort descending and greedily take coins."
 *
 * Interviewer immediately checks:
 *
 * [1,3,4], amount=6
 *
 * Greedy fails.
 *
 * Dynamic Programming required.
 *
 * ============================================================================
 * ⚙️ HOW TO PHYSICALLY ASSEMBLE THE CODE
 * ============================================================================
 *
 * 🛠️ IMPLEMENTATION BLUEPRINT
 * ============================================================================
 *
 * Step 1
 *
 * Create function.
 *
 * Step 2
 *
 * Handle amount == 0.
 *
 * Step 3
 *
 * Create dp array size amount+1.
 *
 * Step 4
 *
 * Fill with INF.
 *
 * Step 5
 *
 * Set dp[0]=0.
 *
 * Step 6
 *
 * Iterate target amount from 1..amount.
 *
 * Step 7
 *
 * For every coin:
 *
 * check:
 *
 * amount >= coin
 *
 * Step 8
 *
 * Transition:
 *
 * dp[curr]
 * =
 * min(
 *      dp[curr],
 *      dp[curr-coin]+1
 * )
 *
 * Step 9
 *
 * Finish table.
 *
 * Step 10
 *
 * If answer == INF
 * return -1
 *
 * else return answer.
 *
 * ============================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE (MEMORY SCAFFOLD)
 * ============================================================================
 *
 * dp = INF
 * dp[0] = 0
 *
 * for curr from 1..amount
 *      for coin in coins
 *           if curr >= coin
 *                dp[curr] =
 *                   min(dp[curr],
 *                       dp[curr-coin]+1)
 *
 * if unreachable
 *      return -1
 *
 * return dp[amount]
 *
 * ============================================================================
 * PRIMARY PROBLEM — SOLUTION CLASSES
 * ============================================================================
 */
public class CoinChange {

    /**
     * =========================================================================
     * BRUTE FORCE
     * =========================================================================
     *
     * Core Idea
     * ---------
     * Try every possible coin choice recursively.
     *
     * Invariant Enforced
     * ------------------
     * solve(remain)
     *
     * returns minimum coins needed to build remain.
     *
     * Limitation
     * ----------
     * Recomputes identical states repeatedly.
     *
     * Time Complexity
     * ---------------
     * Exponential
     *
     * Space Complexity
     * ----------------
     * O(amount) recursion depth
     *
     * Interview Preference
     * --------------------
     * Useful only as starting point.
     */
    static class BruteForceSolution {

        public int coinChange(int[] coins, int amount) {

            if (amount == 0) {
                return 0;
            }

            int answer = dfs(coins, amount);

            return answer == Integer.MAX_VALUE
                    ? -1
                    : answer;
        }

        private int dfs(int[] coins, int remain) {

            if (remain == 0) {
                return 0;
            }

            if (remain < 0) {
                return Integer.MAX_VALUE;
            }

            int best = Integer.MAX_VALUE;

            for (int coin : coins) {

                int subAnswer = dfs(coins, remain - coin);

                if (subAnswer != Integer.MAX_VALUE) {
                    best = Math.min(best, subAnswer + 1);
                }
            }

            return best;
        }
    }

    /**
     * =========================================================================
     * IMPROVED (TOP-DOWN DP / MEMOIZATION)
     * =========================================================================
     *
     * Core Idea
     * ---------
     * Cache every amount once.
     *
     * Invariant Enforced
     * ------------------
     * memo[x]
     *
     * stores the optimal answer for amount x.
     *
     * Limitation Fixed
     * ----------------
     * Eliminates repeated recursion.
     *
     * Time Complexity
     * ---------------
     * O(amount * numberOfCoins)
     *
     * Space Complexity
     * ----------------
     * O(amount)
     *
     * Interview Preference
     * --------------------
     * Good.
     *
     * Bottom-up is usually preferred because:
     *
     * - no recursion depth concerns
     * - easier debugging
     * - predictable iteration
     */
    static class MemoizedSolution {

        public int coinChange(int[] coins, int amount) {

            if (amount == 0) {
                return 0;
            }

            Integer[] memo = new Integer[amount + 1];

            int answer = dfs(coins, amount, memo);

            return answer == Integer.MAX_VALUE
                    ? -1
                    : answer;
        }

        private int dfs(
                int[] coins,
                int remain,
                Integer[] memo) {

            if (remain == 0) {
                return 0;
            }

            if (remain < 0) {
                return Integer.MAX_VALUE;
            }

            if (memo[remain] != null) {
                return memo[remain];
            }

            int best = Integer.MAX_VALUE;

            for (int coin : coins) {

                int subAnswer =
                        dfs(coins, remain - coin, memo);

                if (subAnswer != Integer.MAX_VALUE) {

                    best =
                            Math.min(
                                    best,
                                    subAnswer + 1
                            );
                }
            }

            memo[remain] = best;

            return best;
        }
    }

    /*
     * 0/1 KNAPSACK
     *
     * One physical item.
     * Use it at most ONCE.
     *
     * Decision:
     * Skip -> dp[cap]
     * Take -> value + dp[cap - weight]
     *
     * dp[cap] = max(skip, take)
     *
     * IMPORTANT:
     * Iterate capacity BACKWARD.
     *
     * Why?
     * dp[cap - weight] must represent ONLY previous items.
     * Backward prevents the current item from reading its own update,
     * so the same item cannot be used twice.
     *
     * Memory:
     *
     * One Item
     *    │
     *    ▼
     * Bag5 -> reads old Bag3
     * Bag4 -> reads old Bag2
     * Bag3 -> reads old Bag1
     * Bag2 -> updated LAST
     *
     * Current item never clones itself.
     */

    static int knapsack01(int[] weights, int[] values, int capacity) {

        int[] dp = new int[capacity + 1];

        // Process one item at a time.
        for (int item = 0; item < weights.length; item++) {

            // Backward => current item cannot reuse itself.
            for (int cap = capacity; cap >= weights[item]; cap--) {

                int skip = dp[cap];

                int take = values[item] + dp[cap - weights[item]];

                dp[cap] = Math.max(skip, take);
            }
        }

        return dp[capacity];
    }

    /*
     * =========================================================================
     * COIN CHANGE (Minimum Coins)
     * =========================================================================
     *
     * State:
     * dp[x] = minimum coins needed to make amount x.
     *
     * Transition:
     *
     * dp[amount] =
     * min(dp[amount],
     *     dp[amount - coin] + 1)
     *
     * -------------------------------------------------------------------------
     * APPROACH 1 — Solve One Amount DP ⭐
     * -------------------------------------------------------------------------
     *
     * Think:
     *
     * "I need THIS amount.
     * Which coin should I pick LAST?"
     *
     * Need Amount = 7
     *        │
     *        ▼
     *   Try ₹1
     *   Try ₹2
     *   Try ₹5
     *        │
     *        ▼
     *    Take Minimum
     *
     * for(amount)
     *     for(coin)
     *
     * Mirrors the recursive decision tree.
     * Easiest approach to invent during interviews.
     *
     * -------------------------------------------------------------------------
     * Why it works
     * -------------------------------------------------------------------------
     *
     * Every amount asks the same question:
     *
     * "Which previous amount + one coin
     * gives the minimum answer?"
     */

    static class OptimalSolution {

            public int coinChange(int[] coins, int target) {

                int INF = target + 1;

                int[] dp = new int[target + 1];
                Arrays.fill(dp, INF);

                dp[0] = 0;

                // Solve ONE amount.
                for (int amount = 1; amount <= target; amount++) {

                    // Try EVERY coin.
                    for (int coin : coins) {

                        if (coin <= amount) {

                            dp[amount] = Math.min(
                                    dp[amount],
                                    dp[amount - coin] + 1
                            );
                        }
                    }
                }

                return dp[target] == INF ? -1 : dp[target];
            }
    }

    /*
     * =========================================================================
     * COIN CHANGE (Minimum Coins)
     * =========================================================================
     *
     * State:
     * dp[x] = minimum coins needed to make amount x.
     *
     * Transition:
     *
     * dp[amount] =
     * min(dp[amount],
     *     dp[amount - coin] + 1)
     *
     * -------------------------------------------------------------------------
     * APPROACH 2 — Process One Coin DP
     * -------------------------------------------------------------------------
     *
     * Think:
     *
     * "I'm holding THIS coin.
     * Which amounts can it improve?"
     *
     * Holding Coin ₹2
     *        │
     *        ▼
     * Improve Amount2
     *        ▼
     * Improve Amount3
     *        ▼
     * Improve Amount4
     *        ▼
     * Improve Amount5
     *
     * for(coin)
     *     for(amount)
     *
     * The current coin relaxes every reachable amount.
     *
     * -------------------------------------------------------------------------
     * Why it works
     * -------------------------------------------------------------------------
     *
     * Every coin propagates improvements to all
     * reachable amounts.
     *
     * Since this problem asks for MINIMUM,
     * every (amount, coin) transition is eventually
     * evaluated, so both traversal orders converge
     * to the same optimal answer.
     *
     * NOTE:
     * This flexibility is UNIQUE to Coin Change (minimum).
     * Coin Change II cannot freely swap loop order.
     */

    static class ProcessOneCoinDP {

        public int coinChange(int[] coins, int target) {

            int INF = target + 1;

            int[] dp = new int[target + 1];
            Arrays.fill(dp, INF);

            dp[0] = 0;

            // Process ONE coin.
            for (int coin : coins) {

                // Let THIS coin improve every reachable amount.
                for (int amount = coin; amount <= target; amount++) {

                    dp[amount] = Math.min(
                            dp[amount],
                            dp[amount - coin] + 1
                    );
                }
            }

            return dp[target] == INF ? -1 : dp[target];
        }
    }
    /*
     * =========================================================================
     * COIN CHANGE PERMUTATIONS (Order Matters)
     * =========================================================================
     *
     * State:
     * dp[x] = number of sequences to make amount x.
     *
     * Transition:
     *
     * dp[amount] += dp[amount - coin]
     *
     * -------------------------------------------------------------------------
     * THINK
     * -------------------------------------------------------------------------
     *
     * "I need THIS amount.
     * Which coin can I place LAST?"
     *
     * Need Amount = 3
     *        │
     *        ▼
     *   Try LAST coin = ₹1
     *   Try LAST coin = ₹2
     *        │
     *        ▼
     * Count ALL sequences.
     *
     * -------------------------------------------------------------------------
     * Loop Order
     * -------------------------------------------------------------------------
     *
     * for(amount)
     *     for(coin)
     *
     * Every amount tries every possible LAST coin.
     *
     * -------------------------------------------------------------------------
     * Example
     * -------------------------------------------------------------------------
     *
     * coins = [1,2]
     * amount = 3
     *
     * Sequences:
     *
     * 111
     * 12
     * 21
     *
     * Answer = 3
     *
     * NOTE:
     * 12 and 21 are DIFFERENT because order matters.
     *
     * This naturally generates permutations.
     */

    static class CoinChangePermutations {

        public int countPermutations(int amount, int[] coins) {

            int[] dp = new int[amount + 1];

            // One way to make amount 0:
            // choose nothing.
            dp[0] = 1;

            // Solve ONE amount.
            for (int curr = 1; curr <= amount; curr++) {

                // Try EVERY possible last coin.
                for (int coin : coins) {

                    if (coin <= curr) {

                        dp[curr] += dp[curr - coin];
                    }
                }
            }

            return dp[amount];
        }
    }

    /*
     * =========================================================================
     * COIN CHANGE II (Count Combinations)
     * =========================================================================
     *
     * State:
     * dp[x] = number of UNIQUE combinations to make amount x.
     *
     * Transition:
     *
     * dp[amount] += dp[amount - coin]
     *
     * -------------------------------------------------------------------------
     * THINK
     * -------------------------------------------------------------------------
     *
     * We NO longer count every sequence.
     *
     * We count UNIQUE combinations.
     *
     * Example:
     *
     * coins = [1,2]
     * amount = 3
     *
     * Permutations
     * ------------
     * 111
     * 12
     * 21      ❌ duplicate combination
     *
     * Combinations
     * ------------
     * 111
     * 12
     *
     * Answer = 2
     *
     * -------------------------------------------------------------------------
     * HOW DO WE PREVENT DUPLICATES?
     * -------------------------------------------------------------------------
     *
     * Process coins in a FIXED order.
     *
     * Finish Coin1
     *      │
     *      ▼
     * Freeze
     *      │
     *      ▼
     * Introduce Coin2
     *      │
     *      ▼
     * Extend existing combinations ONLY
     *      │
     *      ▼
     * Freeze
     *      │
     *      ▼
     * Introduce Coin5
     *
     * Once a coin is finished,
     * we NEVER go back to earlier coins.
     *
     * Therefore:
     *
     * 1 + 2
     *
     * is generated,
     *
     * but
     *
     * 2 + 1
     *
     * is never generated.
     *
     * -------------------------------------------------------------------------
     * Loop Order
     * -------------------------------------------------------------------------
     *
     * for(coin)
     *     for(amount)
     *
     * Think:
     *
     * "I'm holding THIS coin.
     * Which combinations can I extend?"
     *
     * -------------------------------------------------------------------------
     * Memory
     * -------------------------------------------------------------------------
     *
     * Coin1
     *   │
     *   ▼
     * Build ALL combinations
     *
     *      ▼
     * Freeze
     *
     * Coin2
     *   │
     *   ▼
     * Extend previous combinations
     *
     *      ▼
     * Freeze
     *
     * Coin5
     *   │
     *   ▼
     * Extend again
     *
     * -------------------------------------------------------------------------
     * Compare
     * -------------------------------------------------------------------------
     *
     * Coin Change (Minimum)
     *
     * Need Amount X
     * → Try every coin
     *
     * Coin Change II
     *
     * Holding Coin X
     * → Extend every amount
     */

    static class CoinChangeII {

        public int change(int amount, int[] coins) {

            int[] dp = new int[amount + 1];

            // One way to make amount 0:
            // choose nothing.
            dp[0] = 1;

            // Process ONE coin.
            for (int coin : coins) {

                // Let THIS coin extend every reachable amount.
                for (int curr = coin; curr <= amount; curr++) {

                    dp[curr] += dp[curr - coin];
                }
            }

            return dp[amount];
        }
    }

    /*
==============================================================================================================
                                       DP FAMILY CHEAT SHEET
==============================================================================================================

+----------------------+------------------+------------------+------------------+----------------------+
|                      | 0/1 Knapsack     | Coin Change      | Coin Permutation | Coin Change II       |
+----------------------+------------------+------------------+------------------+----------------------+
| Goal                 | Max Value        | Min Coins        | Count Sequences  | Count Combinations   |
+----------------------+------------------+------------------+------------------+----------------------+
| Reuse?               | NO               | YES              | YES              | YES                  |
+----------------------+------------------+------------------+------------------+----------------------+
| Human Question       | Take / Skip?     | Need Amount X    | Need Amount X    | Holding Coin X       |
|                      |                  | Try every coin   | Last coin?       | Extend combinations  |
+----------------------+------------------+------------------+------------------+----------------------+
| DP State             | dp[c]=Max Value  | dp[a]=Min Coins  | dp[a]=Sequences  | dp[a]=Combinations   |
+----------------------+------------------+------------------+------------------+----------------------+
| Transition           | max()            | min()            | +=               | +=                   |
+----------------------+------------------+------------------+------------------+----------------------+
| Outer Loop           | Item             | Amount ⭐         | Amount           | Coin ⭐              |
| Why?                 | Decide one item  | Solve one amount | Solve one amount | Finish one coin      |
|                      | at a time        | at a time        | at a time        | before next coin     |
+----------------------+------------------+------------------+------------------+----------------------+
| Inner Loop           | Capacity         | Coin             | Coin             | Amount               |
| Why?                 | Try every bag    | Try every coin   | Try LAST coin    | Extend every amount  |
|                      | for this item    | for this amount  | for this amount  | using THIS coin      |
+----------------------+------------------+------------------+------------------+----------------------+
| Direction            | Backward ↓       | Forward ↑        | Forward ↑        | Forward ↑            |
| Why?                 | Prevent current  | Reuse current    | Reuse current    | Reuse current coin   |
|                      | item reuse       | coin immediately | coin immediately | while fixing order   |
+----------------------+------------------+------------------+------------------+----------------------+
| Duplicate Control    | N/A              | N/A              | Order Matters    | Freeze Coin Order    |
|                      |                  |                  | (12 != 21)       | (12 == 21)           |
+----------------------+------------------+------------------+------------------+----------------------+
| Mental Movie         | Hold Item        | Need Amount      | Need Amount      | Hold Coin            |
|                      | Try every bag    | Try every coin   | Try LAST coin    | Build→Freeze→Extend  |
+----------------------+------------------+------------------+------------------+----------------------+

==============================================================================================================
INTERVIEW DECISION TREE
==============================================================================================================

Can I reuse the current object?
        |
   +----+----+
   |         |
  NO        YES
   |         |
Backward    Forward

Then ask...

What is driving the DP?
        |
   +----+----+
   |         |
Need Amount  Holding Coin
   |             |
Amount→Coin   Coin→Amount

Then ask...

Optimization?
        |
   +---------+----------------+
   |                          |
Minimum                   Count Ways
                              |
                      +-------+-------+
                      |               |
                  Order Matters?   Order Doesn't Matter?
                      |               |
               Coin Permutation   Coin Change II
               Amount→Coin        Coin→Amount

==============================================================================================================

    /**
     * =========================================================================
     * 🟣 INTERVIEW ARTICULATION
     * =========================================================================
     *
     * Same unbounded knapsack family.
     *
     * Difference:
     *
     * We count possibilities
     * rather than minimizing cost.
     *
     * =========================================================================
     * ⚫ REINFORCEMENT PROBLEM #2
     * =========================================================================
     *
     * Perfect Squares
     *
     * LeetCode 279
     *
     * -------------------------------------------------------------------------
     * Problem Summary
     * -------------------------------------------------------------------------
     *
     * Given n,
     * return minimum number of perfect squares
     * whose sum equals n.
     *
     * Example:
     *
     * n = 12
     *
     * 4 + 4 + 4
     *
     * answer = 3
     *
     * -------------------------------------------------------------------------
     * Invariant Mapping
     * -------------------------------------------------------------------------
     *
     * Coin values become:
     *
     * 1,4,9,16,...
     *
     * Same DP invariant:
     *
     * dp[x]
     *
     * =
     *
     * minimum pieces needed.
     *
     * -------------------------------------------------------------------------
     * Edge Cases
     * -------------------------------------------------------------------------
     *
     * n=0
     * n=1
     */
    static class PerfectSquares {

        public int numSquares(int n) {

            int[] dp = new int[n + 1];

            Arrays.fill(dp, n + 1);

            dp[0] = 0;

            for (int amount = 1;
                 amount <= n;
                 amount++) {

                for (int square = 1;
                     square * square <= amount;
                     square++) {

                    dp[amount] =
                            Math.min(
                                    dp[amount],
                                    dp[amount - square * square] + 1
                            );
                }
            }

            return dp[n];
        }
    }

    /**
     * =========================================================================
     * 🟣 INTERVIEW ARTICULATION
     * =========================================================================
     *
     * Same exact invariant.
     *
     * Only the available "coin values"
     * have changed.
     *
     * =========================================================================
     * ⚫ REINFORCEMENT PROBLEM #3
     * =========================================================================
     *
     * Minimum Cost Climbing Stairs
     *
     * LeetCode 746
     *
     * -------------------------------------------------------------------------
     * Problem Summary
     * -------------------------------------------------------------------------
     *
     * Reach the top
     * with minimum cumulative cost.
     *
     * -------------------------------------------------------------------------
     * Key Example
     * -------------------------------------------------------------------------
     *
     * cost=[10,15,20]
     *
     * answer=15
     *
     * -------------------------------------------------------------------------
     * Invariant Mapping
     * -------------------------------------------------------------------------
     *
     * dp[i]
     *
     * =
     *
     * minimum cost required
     * to reach step i.
     *
     * Same philosophy:
     *
     * optimal substructure.
     */
    static class MinimumCostClimbingStairs {

        public int minCostClimbingStairs(int[] cost) {

            int n = cost.length;

            int[] dp = new int[n + 1];

            dp[0] = 0;
            dp[1] = 0;

            for (int i = 2; i <= n; i++) {

                dp[i] =
                        Math.min(
                                dp[i - 1] + cost[i - 1],
                                dp[i - 2] + cost[i - 2]
                        );
            }

            return dp[n];
        }
    }

/**
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * State meaning changes.
 *
 * But invariant-driven DP remains identical.
 *
 * Define state precisely.
 * Build from smaller states.
 * Preserve meaning.
 */


    /**
     * =========================================================================
     * 🧩 RELATED PROBLEM #1
     * =========================================================================
     *
     * LeetCode 70
     * Climbing Stairs
     *
     * -------------------------------------------------------------------------
     * Problem Summary
     * -------------------------------------------------------------------------
     *
     * You can climb:
     *
     * 1 step
     * or
     * 2 steps
     *
     * Return total number of distinct ways.
     *
     * -------------------------------------------------------------------------
     * Same / Modified / Broken Invariant
     * -------------------------------------------------------------------------
     *
     * Modified.
     *
     * dp[i]
     *
     * =
     *
     * number of ways to reach i.
     *
     * Not minimum.
     *
     * Counting.
     *
     * -------------------------------------------------------------------------
     * Edge Case
     * -------------------------------------------------------------------------
     *
     * n=1
     */
    static class ClimbingStairs {

        public int climbStairs(int n) {

            if (n <= 2) {
                return n;
            }

            int[] dp = new int[n + 1];

            dp[1] = 1;
            dp[2] = 2;

            for (int i = 3; i <= n; i++) {

                dp[i] = dp[i - 1] + dp[i - 2];
            }

            return dp[n];
        }
    }

    /**
     * =========================================================================
     * Interview Note
     * =========================================================================
     *
     * Same DP construction.
     *
     * Different state meaning.
     *
     * =========================================================================
     * 🧩 RELATED PROBLEM #2
     * =========================================================================
     *
     * LeetCode 139
     * Word Break
     *
     * -------------------------------------------------------------------------
     * Problem Summary
     * -------------------------------------------------------------------------
     *
     * Determine whether a string
     * can be segmented using dictionary words.
     *
     * -------------------------------------------------------------------------
     * Same / Modified / Broken Invariant
     * -------------------------------------------------------------------------
     *
     * Modified.
     *
     * dp[i]
     *
     * =
     *
     * whether prefix [0...i)
     * is constructible.
     *
     * -------------------------------------------------------------------------
     * Edge Case
     * -------------------------------------------------------------------------
     *
     * Empty string.
     */
    static class WordBreak {

        public boolean wordBreak(
                String s,
                List<String> wordDict) {

            Set<String> set =
                    new HashSet<>(wordDict);

            boolean[] dp =
                    new boolean[s.length() + 1];

            dp[0] = true;

            for (int end = 1;
                 end <= s.length();
                 end++) {

                for (int start = 0;
                     start < end;
                     start++) {

                    if (dp[start]
                            && set.contains(
                            s.substring(start, end))) {

                        dp[end] = true;
                        break;
                    }
                }
            }

            return dp[s.length()];
        }
    }

    /**
     * =========================================================================
     * Interview Note
     * =========================================================================
     *
     * Still:
     *
     * solve smaller states first.
     *
     * State meaning changed from:
     *
     * minimum
     *
     * to:
     *
     * feasibility.
     *
     * =========================================================================
     * 🧩 RELATED PROBLEM #3
     * =========================================================================
     *
     * LeetCode 198
     * House Robber
     *
     * -------------------------------------------------------------------------
     * Problem Summary
     * -------------------------------------------------------------------------
     *
     * Maximum money
     * without robbing adjacent houses.
     *
     * -------------------------------------------------------------------------
     * Same / Modified / Broken Invariant
     * -------------------------------------------------------------------------
     *
     * Modified.
     *
     * dp[i]
     *
     * =
     *
     * maximum money obtainable
     * considering first i houses.
     *
     * -------------------------------------------------------------------------
     * Edge Case
     * -------------------------------------------------------------------------
     *
     * Single house.
     */
    static class HouseRobber {

        public int rob(int[] nums) {

            if (nums.length == 1) {
                return nums[0];
            }

            int[] dp =
                    new int[nums.length + 1];

            dp[0] = 0;
            dp[1] = nums[0];

            for (int i = 2;
                 i <= nums.length;
                 i++) {

                dp[i] =
                        Math.max(
                                dp[i - 1],
                                dp[i - 2] + nums[i - 1]
                        );
            }

            return dp[nums.length];
        }
    }

    /**
     * =========================================================================
     * Interview Note
     * =========================================================================
     *
     * Optimization objective changed.
     *
     * DP discipline stayed identical.
     *
     * =========================================================================
     * 🧠 MASTERY CHECKLIST
     * =========================================================================
     *
     * Can You State The Invariant?
     * ----------------------------
     *
     * dp[x]
     *
     * =
     *
     * minimum coins required
     * for amount x.
     *
     * -------------------------------------------------------------------------
     * Can You State The Search Target?
     * -------------------------------------------------------------------------
     *
     * dp[amount]
     *
     * -------------------------------------------------------------------------
     * Can You State The Transition?
     * -------------------------------------------------------------------------
     *
     * dp[x]
     * =
     * min(
     *      dp[x],
     *      dp[x - coin] + 1
     * )
     *
     * -------------------------------------------------------------------------
     * Can You Explain Why It Works?
     * -------------------------------------------------------------------------
     *
     * Every valid solution
     * has some final coin.
     *
     * We test all possible final coins.
     *
     * -------------------------------------------------------------------------
     * Can You Explain Naive Failure?
     * -------------------------------------------------------------------------
     *
     * Greedy lacks global optimality.
     *
     * Recursion repeats states.
     *
     * -------------------------------------------------------------------------
     * Can You Handle Edge Cases?
     * -------------------------------------------------------------------------
     *
     * amount = 0
     *
     * unreachable target
     *
     * single denomination
     *
     * denomination > amount
     *
     * -------------------------------------------------------------------------
     * Can You Debug Quickly?
     * -------------------------------------------------------------------------
     *
     * Verify:
     *
     * dp[0]
     *
     * Verify:
     *
     * INF initialization.
     *
     * Verify:
     *
     * transition index.
     *
     * Verify:
     *
     * unreachable return.
     *
     * -------------------------------------------------------------------------
     * Variant Readiness
     * -------------------------------------------------------------------------
     *
     * Can switch to:
     *
     * counting
     * feasibility
     * maximization
     *
     * by changing state meaning.
     *
     * -------------------------------------------------------------------------
     * Pattern Boundary
     * -------------------------------------------------------------------------
     *
     * Requires:
     *
     * optimal substructure
     * reusable subproblems
     * finite state space
     *
     * Otherwise seek another pattern.
     *
     * =========================================================================
     * TEST HELPERS
     * =========================================================================
     */

    private static void assertEquals(
            int expected,
            int actual,
            String testName) {

        if (expected != actual) {

            throw new AssertionError(
                    testName
                            + " FAILED. Expected="
                            + expected
                            + " Actual="
                            + actual
            );
        }
    }

    private static void assertTrue(
            boolean condition,
            String testName) {

        if (!condition) {

            throw new AssertionError(
                    testName + " FAILED."
            );
        }
    }

/**
 * =========================================================================
 * SELF-VERIFYING TESTS
 * =========================================================================
 */

private static void runCoinChangeTests() {

    OptimalSolution solution =
            new OptimalSolution();

    /*
     * Happy Path
     *
     * Canonical example.
     *
     * 11 = 5 + 5 + 1
     */
    assertEquals(
            3,
            solution.coinChange(
                    new int[]{1, 2, 5},
                    11
            ),
            "CoinChange-HappyPath"
    );

    /*
     * Impossible target.
     *
     * No combination of 2s
     * can create 3.
     */
    assertEquals(
            -1,
            solution.coinChange(
                    new int[]{2},
                    3
            ),
            "CoinChange-Unreachable"
    );

    /*
     * Boundary.
     *
     * Zero amount needs
     * zero coins.
     */
    assertEquals(
            0,
            solution.coinChange(
                    new int[]{1},
                    0
            ),
            "CoinChange-ZeroAmount"
    );

    /*
     * Greedy trap.
     *
     * Greedy:
     * 4 + 1 + 1
     *
     * Optimal:
     * 3 + 3
     */
    assertEquals(
            2,
            solution.coinChange(
                    new int[]{1, 3, 4},
                    6
            ),
            "CoinChange-GreedyTrap"
    );

    /*
     * Single exact coin.
     */
    assertEquals(
            1,
            solution.coinChange(
                    new int[]{7},
                    7
            ),
            "CoinChange-SingleCoin"
    );

    /*
     * Coin larger than amount.
     */
    assertEquals(
            -1,
            solution.coinChange(
                    new int[]{10},
                    5
            ),
            "CoinChange-LargeCoin"
    );

    /*
     * Multiple optimal paths.
     *
     * 6 + 6
     */
    assertEquals(
            2,
            solution.coinChange(
                    new int[]{1, 4, 6},
                    12
            ),
            "CoinChange-MultipleChoices"
    );
}

    private static void runCoinChangeIITests() {

        CoinChangeII solution =
                new CoinChangeII();

        assertEquals(
                4,
                solution.change(
                        5,
                        new int[]{1, 2, 5}
                ),
                "CoinChangeII-Basic"
        );

        assertEquals(
                1,
                solution.change(
                        0,
                        new int[]{1, 2}
                ),
                "CoinChangeII-ZeroAmount"
        );
    }

    private static void runPerfectSquaresTests() {

        PerfectSquares solution =
                new PerfectSquares();

        assertEquals(
                3,
                solution.numSquares(12),
                "PerfectSquares-12"
        );

        assertEquals(
                2,
                solution.numSquares(13),
                "PerfectSquares-13"
        );
    }

    private static void runClimbingStairsTests() {

        ClimbingStairs solution =
                new ClimbingStairs();

        assertEquals(
                2,
                solution.climbStairs(2),
                "ClimbingStairs-2"
        );

        assertEquals(
                3,
                solution.climbStairs(3),
                "ClimbingStairs-3"
        );

        assertEquals(
                8,
                solution.climbStairs(5),
                "ClimbingStairs-5"
        );
    }

    private static void runWordBreakTests() {

        WordBreak solution =
                new WordBreak();

        assertTrue(
                solution.wordBreak(
                        "leetcode",
                        Arrays.asList(
                                "leet",
                                "code"
                        )
                ),
                "WordBreak-Positive"
        );

        assertTrue(
                !solution.wordBreak(
                        "catsandog",
                        Arrays.asList(
                                "cats",
                                "dog",
                                "sand",
                                "and",
                                "cat"
                        )
                ),
                "WordBreak-Negative"
        );
    }

    private static void runHouseRobberTests() {

        HouseRobber solution =
                new HouseRobber();

        assertEquals(
                4,
                solution.rob(
                        new int[]{1, 2, 3, 1}
                ),
                "HouseRobber-Basic1"
        );

        assertEquals(
                12,
                solution.rob(
                        new int[]{2, 7, 9, 3, 1}
                ),
                "HouseRobber-Basic2"
        );
    }

    /**
     * =========================================================================
     * DP TABLE VISUALIZER
     * =========================================================================
     *
     * Useful for debugging and teaching.
     *
     * Not required for solution.
     */
    static class DPVisualizer {

        public static int[] buildTable(
                int[] coins,
                int amount) {

            int INF = amount + 1;

            int[] dp = new int[amount + 1];

            Arrays.fill(dp, INF);

            dp[0] = 0;

            for (int curr = 1;
                 curr <= amount;
                 curr++) {

                for (int coin : coins) {

                    if (curr >= coin) {

                        dp[curr] =
                                Math.min(
                                        dp[curr],
                                        dp[curr - coin] + 1
                                );
                    }
                }
            }

            return dp;
        }
    }

/**
 * =========================================================================
 * COMMON DEBUGGING WORKFLOW
 * =========================================================================
 *
 * If answer is wrong:
 *
 * 1. Verify dp[0].
 *
 * 2. Verify INF value.
 *
 * 3. Print DP table.
 *
 * 4. Verify:
 *
 *    curr - coin
 *
 *    index.
 *
 * 5. Verify unreachable handling.
 *
 * 6. Check for overflow caused by
 *    Integer.MAX_VALUE.
 *
 * =========================================================================
 * RE-DERIVATION DRILL
 * =========================================================================
 *
 * Step 1
 *
 * Define state:
 *
 * dp[x]
 * =
 * minimum coins for amount x.
 *
 * Step 2
 *
 * Base:
 *
 * dp[0] = 0
 *
 * Step 3
 *
 * Ask:
 *
 * What could be the final coin?
 *
 * Step 4
 *
 * Try every coin.
 *
 * Step 5
 *
 * Take minimum.
 *
 * Step 6
 *
 * Return dp[amount].
 *
 * If unreachable:
 *
 * return -1.
 *
 * =========================================================================
 * IMPLEMENTATION MUSCLE MEMORY
 * =========================================================================
 *
 * Memorize structure,
 * not code.
 *
 * 1. dp size amount+1
 * 2. fill INF
 * 3. dp[0]=0
 * 4. amount loop
 * 5. coin loop
 * 6. transition
 * 7. unreachable check
 * 8. return answer
 */

public static void main(String[] args) {

    runCoinChangeTests();
    runCoinChangeIITests();
    runPerfectSquaresTests();
    runClimbingStairsTests();
    runWordBreakTests();
    runHouseRobberTests();

    /*
     * Additional boundary verification.
     */

    OptimalSolution solution =
            new OptimalSolution();

    assertEquals(
            20,
            solution.coinChange(
                    new int[]{1, 5, 10, 25},
                    500
            ),
            "CoinChange-LargeBoundary"
    );

    /*
     * DP table sanity check.
     */

    int[] table =
            DPVisualizer.buildTable(
                    new int[]{1, 2, 5},
                    11
            );

    assertEquals(
            3,
            table[11],
            "DPTableVerification"
    );

    /*
     * Verify monotonic constructibility
     * when coin 1 exists.
     */

    for (int amount = 0;
         amount <= 25;
         amount++) {

        int answer =
                solution.coinChange(
                        new int[]{1, 3, 4},
                        amount
                );

        assertTrue(
                answer >= 0,
                "ConstructibleWithCoinOne-" + amount
        );
    }

    /*
     * Verify unreachable pattern.
     */

    for (int odd = 1;
         odd <= 21;
         odd += 2) {

        int answer =
                solution.coinChange(
                        new int[]{2},
                        odd
                );

        assertEquals(
                -1,
                answer,
                "OddAmountUnreachable-" + odd
        );
    }

    System.out.println(
            "All tests passed successfully."
    );

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
}



