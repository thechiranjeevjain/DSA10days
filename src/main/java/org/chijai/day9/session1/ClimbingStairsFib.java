package org.chijai.day9.session1;
import java.util.*;

/**
 * =====================================================================================
 * 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
 * =====================================================================================
 *
 * 🔹 Problem: Climbing Stairs
 *
 * 🔹 LeetCode Link:
 * :contentReference[oaicite:0]{index=0}
 *
 * 🔹 Difficulty:
 * Easy
 *
 * 🔹 Tags:
 * Dynamic Programming, Math, Memoization
 *
 * -------------------------------------------------------------------------------------
 * You are climbing a staircase. It takes n steps to reach the top.
 *
 * Each time you can either climb 1 or 2 steps.
 *
 * In how many distinct ways can you climb to the top?
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: 2
 * Explanation:
 * There are two ways to climb to the top.
 * 1. 1 step + 1 step
 * 2. 2 steps
 *
 * Example 2:
 *
 * Input: n = 3
 * Output: 3
 * Explanation:
 * There are three ways to climb to the top.
 * 1. 1 step + 1 step + 1 step
 * 2. 1 step + 2 steps
 * 3. 2 steps + 1 step
 *
 * Constraints:
 *
 * 1 <= n <= 45
 *
 * =====================================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * =====================================================================================
 *
 * 🔹 Pattern Name:
 * Fibonacci-style Dynamic Programming
 *
 * 🔹 Problem Archetype:
 * Count total ways to reach a target using smaller subproblems.
 *
 * 🔹 CORE INVARIANT:
 *
 * dp[i] = total number of valid ways to reach step i
 *
 * Every valid path to step i must come from:
 * - step i - 1
 * - step i - 2
 *
 * Therefore:
 *
 * dp[i] = dp[i - 1] + dp[i - 2]
 *
 * 🔹 Why It Works:
 *
 * The final move into step i is either:
 * - a 1-step jump from i - 1
 * - a 2-step jump from i - 2
 *
 * These two sets are disjoint.
 *
 * So total ways = sum of both.
 *
 * 🔹 When To Use This Pattern:
 *
 * Use when:
 * - counting paths
 * - counting combinations
 * - future state depends on fixed previous states
 * - recurrence relation exists
 *
 * 🔹 Recognition Signals:
 *
 * - "How many ways"
 * - fixed move sizes
 * - recursive branching
 * - overlapping subproblems
 *
 * 🔹 Difference vs Similar Patterns:
 *
 * Recursion:
 * - recomputes same states repeatedly
 *
 * Backtracking:
 * - explores all paths explicitly
 *
 * DP:
 * - stores answers once
 * - builds upward mechanically
 *
 * =====================================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * =====================================================================================
 *
 * 🔹 Mental Model
 *
 * Imagine standing on stair i.
 *
 * To arrive there:
 * - either you came from i - 1
 * - or from i - 2
 *
 * So:
 * every stair accumulates ways from previous reachable stairs.
 *
 * -------------------------------------------------------------------------------------
 * 🟢 Invariants
 * -------------------------------------------------------------------------------------
 *
 * 1.
 * dp[i] always means:
 * "number of valid ways to reach step i"
 *
 * 2.
 * Before computing dp[i]:
 * dp[i - 1] and dp[i - 2] are already correct.
 *
 * 3.
 * No valid path is double counted.
 *
 * 4.
 * Every valid path is included exactly once.
 *
 * -------------------------------------------------------------------------------------
 * 🔹 Meaning of Variables
 * -------------------------------------------------------------------------------------
 *
 * dp[i]
 * = total ways to reach stair i
 *
 * prev1
 * = ways to reach previous stair
 *
 * prev2
 * = ways to reach stair before previous
 *
 * -------------------------------------------------------------------------------------
 * 🔹 Allowed Moves
 * -------------------------------------------------------------------------------------
 *
 * From stair i:
 * - go to i + 1
 * - go to i + 2
 *
 * -------------------------------------------------------------------------------------
 * 🔴 Forbidden Logic
 * -------------------------------------------------------------------------------------
 *
 * - recomputing already solved states
 * - counting invalid negative states
 * - forgetting base cases
 *
 * -------------------------------------------------------------------------------------
 * 🔹 Termination Logic
 * -------------------------------------------------------------------------------------
 *
 * Once we compute step n:
 * answer is finalized.
 *
 * -------------------------------------------------------------------------------------
 * 🔴 Why Naive Approaches Fail
 * -------------------------------------------------------------------------------------
 *
 * Pure recursion branches repeatedly.
 *
 * Example:
 *
 * ways(5)
 * ├── ways(4)
 * │   ├── ways(3)
 * │   └── ways(2)
 * └── ways(3)
 *
 * ways(3) gets recomputed many times.
 *
 * Complexity explodes exponentially.
 *
 * =====================================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * =====================================================================================
 *
 * -------------------------------------------------------------------------------------
 * ❌ Wrong Approach 1:
 * Greedy stepping
 * -------------------------------------------------------------------------------------
 *
 * "Always take 2 steps."
 *
 * Why it fails:
 * Problem asks TOTAL ways, not minimum moves.
 *
 * Counterexample:
 *
 * n = 3
 *
 * Greedy:
 * 2 + 1
 *
 * Missing:
 * 1 + 1 + 1
 * 1 + 2
 *
 * -------------------------------------------------------------------------------------
 * ❌ Wrong Approach 2:
 * Pure recursion without memoization
 * -------------------------------------------------------------------------------------
 *
 * Why it seems correct:
 * Recurrence itself is correct.
 *
 * Invariant violation:
 * Same subproblem recomputed repeatedly.
 *
 * Complexity:
 * O(2^n)
 *
 * -------------------------------------------------------------------------------------
 * ❌ Wrong Approach 3:
 * Wrong base cases
 * -------------------------------------------------------------------------------------
 *
 * Example:
 *
 * ways(0) = 0
 *
 * Incorrect.
 *
 * Because:
 * there is exactly ONE way to stand still.
 *
 * Correct:
 *
 * ways(0) = 1
 *
 * -------------------------------------------------------------------------------------
 * ❌ Interviewer Trap
 * -------------------------------------------------------------------------------------
 *
 * Candidate writes:
 *
 * dp[0] = 0
 *
 * Then recurrence silently breaks.
 *
 * =====================================================================================
 * ⚙️ HOW TO PHYSICALLY ASSEMBLE THE CODE
 * =====================================================================================
 *
 * 🛠️ IMPLEMENTATION BLUEPRINT
 *
 * -------------------------------------------------------------------------------------
 * Step 1:
 * Create method
 * -------------------------------------------------------------------------------------
 *
 * int climbStairs(int n)
 *
 * -------------------------------------------------------------------------------------
 * Step 2:
 * Handle smallest base cases
 * -------------------------------------------------------------------------------------
 *
 * if (n <= 2) return n;
 *
 * -------------------------------------------------------------------------------------
 * Step 3:
 * Initialize previous answers
 * -------------------------------------------------------------------------------------
 *
 * prev2 = 1
 * prev1 = 2
 *
 * -------------------------------------------------------------------------------------
 * Step 4:
 * Iterate from stair 3 to n
 * -------------------------------------------------------------------------------------
 *
 * for (i = 3 → n)
 *
 * -------------------------------------------------------------------------------------
 * Step 5:
 * Compute current answer
 * -------------------------------------------------------------------------------------
 *
 * current = prev1 + prev2
 *
 * -------------------------------------------------------------------------------------
 * Step 6:
 * Shift window forward
 * -------------------------------------------------------------------------------------
 *
 * prev2 = prev1
 * prev1 = current
 *
 * -------------------------------------------------------------------------------------
 * Step 7:
 * Return final answer
 * -------------------------------------------------------------------------------------
 *
 * return prev1
 *
 * =====================================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE (MEMORY SCAFFOLD)
 * =====================================================================================
 *
 * if n small:
 *     return base
 *
 * prev2 = 1
 * prev1 = 2
 *
 * for stair from 3 to n:
 *     current = prev1 + prev2
 *     shift window
 *
 * return prev1
 *
 * =====================================================================================
 * PRIMARY PROBLEM — SOLUTION CLASSES
 * =====================================================================================
 */
public class ClimbingStairsFib {

    /**
     * =================================================================================
     * Brute Force Solution
     * =================================================================================
     *
     * 🔹 Core Idea:
     * Try every possible path recursively.
     *
     * 🔹 Invariant:
     * ways(n) = ways(n - 1) + ways(n - 2)
     *
     * 🔹 Limitation Fixed Later:
     * Repeated recomputation.
     *
     * 🔹 Time Complexity:
     * O(2^n)
     *
     * 🔹 Space Complexity:
     * O(n) recursion depth
     *
     * 🔹 Interview Preference:
     * Usually rejected for scalability.
     */
    static class BruteForceSolution {

        public int climbStairs(int n) {

            // Base invariant:
            // one way to stay where we already are.
            if (n == 0) {
                return 1;
            }

            // Negative stair is invalid.
            if (n < 0) {
                return 0;
            }

            // Explore both legal moves.
            return climbStairs(n - 1) + climbStairs(n - 2);
        }
    }

    /**
     * =================================================================================
     * Improved Solution — Memoization
     * =================================================================================
     *
     * 🔹 Core Idea:
     * Cache already solved states.
     *
     * 🔹 Invariant:
     * memo[i] stores correct ways for stair i once computed.
     *
     * 🔹 Limitation Fixed:
     * Removes repeated recursion.
     *
     * 🔹 Time Complexity:
     * O(n)
     *
     * 🔹 Space Complexity:
     * O(n)
     *
     * 🔹 Interview Preference:
     * Good transition solution.
     */
    static class MemoizationSolution {

        public int climbStairs(int n) {

            int[] memo = new int[n + 1];
            Arrays.fill(memo, -1);

            return dfs(n, memo);
        }

        private int dfs(int n, int[] memo) {

            // Standing still counts as one valid completion.
            if (n == 0) {
                return 1;
            }

            // Invalid path.
            if (n < 0) {
                return 0;
            }

            // Reuse cached answer.
            if (memo[n] != -1) {
                return memo[n];
            }

            memo[n] = dfs(n - 1, memo) + dfs(n - 2, memo);

            return memo[n];
        }
    }

    /**
     * =================================================================================
     * Optimal Solution — Interview Preferred
     * =================================================================================
     *
     * 🔹 Core Idea:
     * Fibonacci rolling window.
     *
     * 🔹 Invariant:
     *
     * prev2 = ways(i - 2)
     * prev1 = ways(i - 1)
     *
     * Before every iteration:
     * both are already correct.
     *
     * 🔹 Time Complexity:
     * O(n)
     *
     * 🔹 Space Complexity:
     * O(1)
     *
     * 🔹 Interview Preference:
     * Best solution.
     */
    static class OptimalSolution {

        public int climbStairs(int n) {

            // Handle smallest inputs immediately.
            if (n <= 2) {
                return n;
            }

            // prev2 = ways to reach stair 1
            int prev2 = 1;

            // prev1 = ways to reach stair 2
            int prev1 = 2;

            // Build answers from smaller solved states.
            for (int stair = 3; stair <= n; stair++) {

                // Invariant:
                // current stair receives ways from previous two stairs.
                int current = prev1 + prev2;

                // Slide DP window forward.
                prev2 = prev1;
                prev1 = current;
            }

            // Final invariant:
            // prev1 now stores ways(n)
            return prev1;
        }
    }

    /**
     * =================================================================================
     * 🟣 INTERVIEW ARTICULATION (NO CODE)
     * =================================================================================
     *
     * "The invariant is:
     * at every stair,
     * total ways equal the sum of ways from previous two stairs.
     *
     * I only need the previous two computed values,
     * so I can compress space to O(1).
     *
     * The recurrence works because every valid path to stair i
     * must end from either i - 1 or i - 2.
     *
     * If we changed allowed jumps,
     * the recurrence would also change.
     *
     * This pattern is excellent when:
     * future states depend on fixed earlier states."
     *
     * =================================================================================
     * 🎯 INTERVIEW RECALL SHEET (30-SECOND RECALL)
     * =================================================================================
     *
     * 🔹 Pattern Trigger
     * Count total ways with fixed moves.
     *
     * 🔹 Core Invariant
     * ways(i) = ways(i - 1) + ways(i - 2)
     *
     * 🔹 Search Target
     * Compute ways(n)
     *
     * 🔹 Discard Rule
     * Older states beyond previous two are unnecessary.
     *
     * 🔹 Common Trap
     * Wrong base case for 0.
     *
     * 🔹 Edge Cases
     * n = 1
     * n = 2
     *
     * 🔹 Interview One-Liner
     * "This is Fibonacci DP with rolling variables."
     *
     * 🔹 Re-derivation Cue
     * "How can I enter current stair?"
     *
     * =================================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =================================================================================
     *
     * ---------------------------------------------------------------------------------
     * Variation 1:
     * Allowed moves = 1, 2, 3
     * ---------------------------------------------------------------------------------
     *
     * New recurrence:
     *
     * dp[i] = dp[i - 1] + dp[i - 2] + dp[i - 3]
     *
     * Invariant still works.
     *
     * ---------------------------------------------------------------------------------
     * Variation 2:
     * Minimum cost instead of count
     * ---------------------------------------------------------------------------------
     *
     * Replace:
     * addition of counts
     *
     * with:
     * minimum of costs
     *
     * Pattern changes into optimization DP.
     *
     * ---------------------------------------------------------------------------------
     * Variation 3:
     * Variable jump sizes
     * ---------------------------------------------------------------------------------
     *
     * Still DP,
     * but transition loops over all legal jumps.
     *
     * ---------------------------------------------------------------------------------
     * Pattern Break Signal
     * ---------------------------------------------------------------------------------
     *
     * If state depends on future decisions,
     * greedy constraints,
     * or non-local conditions,
     * this recurrence may fail.
     *
     * =================================================================================
     * ⚫ REINFORCEMENT PROBLEMS
     * =================================================================================
     */

    /**
     * =================================================================================
     * Reinforcement Problem 1 — Min Cost Climbing Stairs
     * =================================================================================
     *
     * 🔹 Summary
     * Reach top with minimum total cost.
     *
     * 🔹 Invariant Mapping
     *
     * dp[i] =
     * minimum cost to reach stair i
     *
     * 🔹 Edge Cases
     * small arrays
     *
     * 🔴 Trap
     * forgetting top itself has no cost
     */
    static class MinCostClimbingStairs {

        public int minCostClimbingStairs(int[] cost) {

            int prev2 = cost[0];
            int prev1 = cost[1];

            for (int i = 2; i < cost.length; i++) {

                int current = cost[i] + Math.min(prev1, prev2);

                prev2 = prev1;
                prev1 = current;
            }

            return Math.min(prev1, prev2);
        }
    }

    /**
     * =================================================================================
     * Reinforcement Problem 2 — Fibonacci Number
     * =================================================================================
     *
     * 🔹 Summary
     * Compute nth Fibonacci number.
     *
     * 🔹 Invariant Mapping
     *
     * F(n) = F(n - 1) + F(n - 2)
     *
     * Exact same rolling-window invariant.
     */
    static class FibonacciSolution {

        public int fib(int n) {

            if (n <= 1) {
                return n;
            }

            int prev2 = 0;
            int prev1 = 1;

            for (int i = 2; i <= n; i++) {

                int current = prev1 + prev2;

                prev2 = prev1;
                prev1 = current;
            }

            return prev1;
        }
    }

    /**
     * =================================================================================
     * Reinforcement Problem 3 — House Robber
     * =================================================================================
     *
     * 🔹 Summary
     * Maximize robbery amount without adjacent houses.
     *
     * 🔹 Invariant Mapping
     *
     * current =
     * max(skip current, rob current)
     *
     * Similar rolling-state DP.
     */
    static class HouseRobber {

        public int rob(int[] nums) {

            int prev2 = 0;
            int prev1 = 0;

            for (int money : nums) {

                int current = Math.max(prev1, prev2 + money);

                prev2 = prev1;
                prev1 = current;
            }

            return prev1;
        }
    }

    /**
     * =================================================================================
     * 🧩 RELATED PROBLEMS
     * =================================================================================
     */

    /**
     * =================================================================================
     * Related Problem 1 — Decode Ways
     * =================================================================================
     *
     * 🔹 Same / Modified Invariant
     *
     * ways(i) depends on:
     * - one-digit decode
     * - two-digit decode
     *
     * Modified Fibonacci-style DP.
     */
    static class DecodeWays {

        public int numDecodings(String s) {

            if (s == null || s.length() == 0 || s.charAt(0) == '0') {
                return 0;
            }

            int prev2 = 1;
            int prev1 = 1;

            for (int i = 2; i <= s.length(); i++) {

                int current = 0;

                char one = s.charAt(i - 1);

                if (one != '0') {
                    current += prev1;
                }

                int two = Integer.parseInt(s.substring(i - 2, i));

                if (two >= 10 && two <= 26) {
                    current += prev2;
                }

                prev2 = prev1;
                prev1 = current;
            }

            return prev1;
        }
    }

    /**
     * =================================================================================
     * Related Problem 2 — Tribonacci Number
     * =================================================================================
     *
     * 🔹 Modified Invariant
     *
     * current =
     * previous three states
     */
    static class TribonacciSolution {

        public int tribonacci(int n) {

            if (n == 0) {
                return 0;
            }

            if (n <= 2) {
                return 1;
            }

            int a = 0;
            int b = 1;
            int c = 1;

            for (int i = 3; i <= n; i++) {

                int current = a + b + c;

                a = b;
                b = c;
                c = current;
            }

            return c;
        }
    }

    /**
     * =================================================================================
     * Related Problem 3 — Unique Paths
     * =================================================================================
     *
     * 🔹 Same Invariant Family
     *
     * Current cell accumulates paths from previous reachable states.
     */
    static class UniquePaths {

        public int uniquePaths(int m, int n) {

            int[][] dp = new int[m][n];

            for (int row = 0; row < m; row++) {
                dp[row][0] = 1;
            }

            for (int col = 0; col < n; col++) {
                dp[0][col] = 1;
            }

            for (int row = 1; row < m; row++) {

                for (int col = 1; col < n; col++) {

                    dp[row][col] = dp[row - 1][col] + dp[row][col - 1];
                }
            }

            return dp[m - 1][n - 1];
        }
    }

    /**
     * =================================================================================
     * 🧠 MASTERY CHECKLIST
     * =================================================================================
     *
     * ✅ Invariant
     * dp[i] = ways from previous reachable stairs
     *
     * ✅ Search Target
     * ways(n)
     *
     * ✅ Discard Rule
     * only previous two states needed
     *
     * ✅ Termination Logic
     * stop after computing stair n
     *
     * ✅ Naive Failure
     * exponential recomputation
     *
     * ✅ Edge Cases
     * n = 1
     * n = 2
     *
     * ✅ Debugging Readiness
     * print rolling variables each iteration
     *
     * ✅ Variant Readiness
     * change recurrence for different moves
     *
     * ✅ Pattern Boundary
     * fails when state dependency becomes non-local
     *
     * =================================================================================
     * 🧪 SELF-VERIFYING TESTS
     * =================================================================================
     */

    private static void assertEquals(int expected, int actual, String message) {

        if (expected != actual) {

            throw new AssertionError(
                    message +
                            " | Expected: " + expected +
                            " | Actual: " + actual
            );
        }
    }

    public static void main(String[] args) {

        OptimalSolution optimal = new OptimalSolution();

        /**
         * -------------------------------------------------------------------------
         * Happy Path
         * -------------------------------------------------------------------------
         *
         * Basic correctness.
         */
        assertEquals(
                2,
                optimal.climbStairs(2),
                "n = 2 should have exactly 2 ways"
        );

        /**
         * -------------------------------------------------------------------------
         * Standard Fibonacci progression
         * -------------------------------------------------------------------------
         */
        assertEquals(
                3,
                optimal.climbStairs(3),
                "n = 3 should have exactly 3 ways"
        );

        /**
         * -------------------------------------------------------------------------
         * Medium input
         * -------------------------------------------------------------------------
         *
         * Verifies recurrence propagation.
         */
        assertEquals(
                8,
                optimal.climbStairs(5),
                "n = 5 should produce Fibonacci-style result"
        );

        /**
         * -------------------------------------------------------------------------
         * Smallest boundary
         * -------------------------------------------------------------------------
         */
        assertEquals(
                1,
                optimal.climbStairs(1),
                "n = 1 should have exactly 1 way"
        );

        /**
         * -------------------------------------------------------------------------
         * Larger constraint validation
         * -------------------------------------------------------------------------
         *
         * Common interviewer stress test.
         */
        assertEquals(
                1836311903,
                optimal.climbStairs(45),
                "n = 45 should match known Fibonacci mapping"
        );

        /**
         * -------------------------------------------------------------------------
         * Reinforcement problem validation
         * -------------------------------------------------------------------------
         */

        FibonacciSolution fib = new FibonacciSolution();

        assertEquals(
                55,
                fib.fib(10),
                "Fibonacci verification failed"
        );

        MinCostClimbingStairs minCost = new MinCostClimbingStairs();

        assertEquals(
                15,
                minCost.minCostClimbingStairs(
                        new int[]{10, 15, 20}
                ),
                "Min cost climbing stairs failed"
        );

        HouseRobber robber = new HouseRobber();

        assertEquals(
                4,
                robber.rob(new int[]{1, 2, 3, 1}),
                "House robber failed"
        );

        System.out.println("✅ All tests passed.");

        /**
         * =========================================================================
         * 🧘 FINAL CLOSURE STATEMENT
         * =========================================================================
         */

        System.out.println();
        System.out.println("I understand the invariant.");
        System.out.println("I can re-derive the solution.");
        System.out.println("I can physically reconstruct the implementation under pressure.");
        System.out.println("This chapter is complete.");
    }
}
