package org.chijai.day9.session2;

import java.util.*;

/**
 * ============================================================================
 * LEETCODE 72. EDIT DISTANCE
 * ============================================================================
 *
 * 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
 *
 * Difficulty: Hard
 *
 * Official Link:
 * https://leetcode.com/problems/edit-distance/
 *
 * Tags:
 * Dynamic Programming
 * String
 *
 * Problem:
 *
 * Given two strings word1 and word2, return the minimum number of operations
 * required to convert word1 to word2.
 *
 * You have the following three operations permitted on a word:
 *
 * 1. Insert a character
 * 2. Delete a character
 * 3. Replace a character
 *
 * Example 1:
 *
 * Input: word1 = "horse", word2 = "ros"
 * Output: 3
 *
 * Explanation:
 *
 * horse -> rorse (replace 'h' with 'r')
 * rorse -> rose (remove 'r')
 * rose -> ros (remove 'e')
 *
 * Example 2:
 *
 * Input: word1 = "intention", word2 = "execution"
 * Output: 5
 *
 * Explanation:
 *
 * intention -> inention (remove 't')
 * inention -> enention (replace 'i' with 'e')
 * enention -> exention (replace 'n' with 'x')
 * exention -> exection (replace 'n' with 'c')
 * exection -> execution (insert 'u')
 *
 * Constraints:
 *
 * 0 <= word1.length, word2.length <= 500
 * word1 and word2 consist of lowercase English letters.
 *
 * ============================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern Name:
 * Dynamic Programming on Prefixes
 *
 * Problem Archetype:
 * Minimum Transformation Cost Between Two Sequences
 *
 * Core Invariant:
 *
 * dp[i][j] =
 * minimum operations required to transform:
 *
 * word1[0...i-1]
 * into
 * word2[0...j-1]
 *
 * Every cell represents a COMPLETE solved subproblem.
 *
 * Why It Works:
 *
 * Any optimal transformation ending at (i,j) must finish using exactly one of:
 *
 * 1. Insert
 * 2. Delete
 * 3. Replace / Match
 *
 * Therefore:
 *
 * Optimal(i,j)
 * depends only on smaller optimal subproblems.
 *
 * This gives optimal substructure.
 *
 * When To Use:
 *
 * - Transform one string into another
 * - Minimum edit operations
 * - Sequence alignment
 * - Prefix-to-prefix optimization
 * - Cost minimization between strings
 *
 * Recognition Signals:
 *
 * - Two strings
 * - Convert A into B
 * - Minimum operations
 * - Insert/Delete/Replace
 * - Constraints too large for brute force recursion
 *
 * Differences vs Similar Patterns:
 *
 * LCS:
 *     Maximizes matching characters.
 *
 * Edit Distance:
 *     Minimizes operation cost.
 *
 * Longest Common Subsequence:
 *     Keep characters.
 *
 * Edit Distance:
 *     Explicitly models insert/delete/replace.
 *
 * Knapsack:
 *     State usually tracks capacity.
 *
 * Edit Distance:
 *     State tracks prefix lengths.
 *
 * ============================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * Mental Model:
 *
 * Imagine a grid.
 *
 * Rows:
 * prefixes of word1
 *
 * Columns:
 * prefixes of word2
 *
 * Cell (i,j):
 *
 * "What is the cheapest way to transform the first i characters
 *  of word1 into the first j characters of word2?"
 *
 * We never guess globally.
 *
 * We solve smaller prefix problems and reuse them.
 *
 * --------------------------------------------------------------------------
 * Invariant #1
 * --------------------------------------------------------------------------
 *
 * dp[i][j] is FINAL once computed.
 *
 * It never changes afterward.
 *
 * --------------------------------------------------------------------------
 * Invariant #2
 * --------------------------------------------------------------------------
 *
 * dp[i][j] stores the optimal answer.
 *
 * Not:
 *
 * - a possible answer
 * - a greedy answer
 * - a local answer
 *
 * The true minimum answer.
 *
 * --------------------------------------------------------------------------
 * Invariant #3
 * --------------------------------------------------------------------------
 *
 * When computing dp[i][j],
 * all required dependencies already exist:
 *
 * dp[i-1][j]
 * dp[i][j-1]
 * dp[i-1][j-1]
 *
 * Therefore transition is valid.
 *
 * --------------------------------------------------------------------------
 * Variable Meaning
 * --------------------------------------------------------------------------
 *
 * i:
 * number of characters taken from word1
 *
 * j:
 * number of characters taken from word2
 *
 * dp[i][j]:
 * minimum edit cost between those prefixes
 *
 * --------------------------------------------------------------------------
 * Allowed Moves
 * --------------------------------------------------------------------------
 *
 * DELETE:
 *
 * Remove current character from word1.
 *
 * Transition:
 *
 * dp[i][j]
 * <- dp[i-1][j] + 1
 *
 * --------------------------------------------------------------------------
 *
 * INSERT:
 *
 * Insert current target character.
 *
 * Transition:
 *
 * dp[i][j]
 * <- dp[i][j-1] + 1
 *
 * --------------------------------------------------------------------------
 *
 * REPLACE:
 *
 * Replace current character.
 *
 * Transition:
 *
 * dp[i][j]
 * <- dp[i-1][j-1] + 1
 *
 * --------------------------------------------------------------------------
 *
 * MATCH:
 *
 * Characters already equal.
 *
 * Transition:
 *
 * dp[i][j]
 * <- dp[i-1][j-1]
 *
 * --------------------------------------------------------------------------
 * Forbidden Thinking
 * --------------------------------------------------------------------------
 *
 * Wrong:
 *
 * "What is best operation right now?"
 *
 * Because local greedy choices can destroy global optimality.
 *
 * Correct:
 *
 * "What is optimal cost for every smaller prefix pair?"
 *
 * --------------------------------------------------------------------------
 * Termination Logic
 * --------------------------------------------------------------------------
 *
 * Final answer:
 *
 * dp[m][n]
 *
 * because:
 *
 * m characters of word1
 * transformed into
 * n characters of word2
 *
 * is exactly the original problem.
 *
 * --------------------------------------------------------------------------
 * Why Naive Approaches Fail
 * --------------------------------------------------------------------------
 *
 * Recursive brute force explores:
 *
 * insert
 * delete
 * replace
 *
 * at nearly every mismatch.
 *
 * Massive overlap occurs.
 *
 * Same prefix pairs are recomputed repeatedly.
 *
 * Complexity becomes exponential.
 *
 * DP removes recomputation.
 *
 * ============================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================================
 *
 * Wrong Approach #1:
 * Greedily replace whenever mismatch occurs.
 *
 * Why It Seems Correct:
 *
 * Replacement fixes mismatch immediately.
 *
 * Counterexample:
 *
 * word1 = "ab"
 * word2 = "b"
 *
 * Optimal:
 * delete 'a'
 *
 * Cost = 1
 *
 * Greedy replace:
 * a -> b
 * then delete extra b
 *
 * Cost = 2
 *
 * Invariant Violation:
 *
 * Local decision ignores future consequences.
 *
 * --------------------------------------------------------------------------
 *
 * Wrong Approach #2:
 * Always prefer delete before insert.
 *
 * Counterexample:
 *
 * word1 = ""
 * word2 = "abc"
 *
 * Only insertion works.
 *
 * Fixed operation ordering is invalid.
 *
 * --------------------------------------------------------------------------
 *
 * Wrong Approach #3:
 * Pure recursion without memoization.
 *
 * Why It Seems Correct:
 *
 * Enumerates all possibilities.
 *
 * Why It Fails:
 *
 * Recomputes same states thousands of times.
 *
 * Example:
 *
 * edit(100,100)
 * may repeatedly revisit:
 *
 * edit(80,79)
 * edit(80,79)
 * edit(80,79)
 *
 * causing exponential explosion.
 *
 * --------------------------------------------------------------------------
 *
 * Interview Trap:
 *
 * Candidate explains operations correctly
 * but cannot define state.
 *
 * If state is unclear,
 * implementation becomes memorization instead of reasoning.
 *
 * Correct state:
 *
 * dp[i][j]
 * =
 * minimum edits between prefix lengths i and j.
 *
 * ============================================================================
 * ⚙️ HOW TO PHYSICALLY ASSEMBLE THE CODE
 * ============================================================================
 *
 * 🛠️ IMPLEMENTATION BLUEPRINT
 *
 * Step 1:
 *
 * Create function.
 *
 * Step 2:
 *
 * Let:
 *
 * m = word1.length()
 * n = word2.length()
 *
 * Step 3:
 *
 * Create:
 *
 * dp[m + 1][n + 1]
 *
 * Step 4:
 *
 * Initialize first column.
 *
 * dp[i][0] = i
 *
 * Because deleting all characters.
 *
 * Step 5:
 *
 * Initialize first row.
 *
 * dp[0][j] = j
 *
 * Because inserting all characters.
 *
 * Step 6:
 *
 * Loop:
 *
 * i from 1..m
 * j from 1..n
 *
 * Step 7:
 *
 * If characters match:
 *
 * dp[i][j] = dp[i-1][j-1]
 *
 * Step 8:
 *
 * Else compute:
 *
 * delete
 * insert
 * replace
 *
 * Step 9:
 *
 * Take minimum.
 *
 * Step 10:
 *
 * Return dp[m][n]
 *
 * ============================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE (MEMORY SCAFFOLD)
 * ============================================================================
 *
 * create dp
 *
 * initialize borders
 *
 * for each i
 *     for each j
 *         if equal
 *             diagonal
 *         else
 *             1 + min(
 *                 delete,
 *                 insert,
 *                 replace
 *             )
 *
 * return bottom-right
 *
 * ============================================================================
 * PRIMARY PROBLEM — SOLUTION CLASSES
 * ============================================================================
 */
public class EditDistance {

    /**
     * ------------------------------------------------------------------------
     * Brute Force
     * ------------------------------------------------------------------------
     *
     * Core Idea:
     *
     * Explore all possible operations recursively.
     *
     * Invariant:
     *
     * Every branch represents a valid edit sequence.
     *
     * Limitation Fixed Later:
     *
     * Massive overlapping subproblems.
     *
     * Time:
     * Exponential
     *
     * Space:
     * O(m + n)
     *
     * Interview Preference:
     * Only for deriving recurrence.
     */
    static class BruteForceSolution {

        public int minDistance(String word1, String word2) {
            return dfs(word1, word2, 0, 0);
        }

        private int dfs(String a, String b, int i, int j) {

            if (i == a.length()) {
                return b.length() - j;
            }

            if (j == b.length()) {
                return a.length() - i;
            }

            if (a.charAt(i) == b.charAt(j)) {
                return dfs(a, b, i + 1, j + 1);
            }

            int insert =
                    dfs(a, b, i, j + 1);

            int delete =
                    dfs(a, b, i + 1, j);

            int replace =
                    dfs(a, b, i + 1, j + 1);

            return 1 + Math.min(insert,
                    Math.min(delete, replace));
        }
    }


    /**
     * ------------------------------------------------------------------------
     * Improved Solution
     * ------------------------------------------------------------------------
     *
     * Core Idea:
     *
     * Same recurrence.
     *
     * Cache every state.
     *
     * State:
     *
     * (i,j)
     *
     * meaning:
     *
     * minimum edits required to convert:
     *
     * word1[i...]
     * into
     * word2[j...]
     *
     * Invariant:
     *
     * Every state is solved exactly once.
     *
     * Limitation Fixed:
     *
     * Removes exponential recomputation.
     *
     * Time:
     * O(m * n)
     *
     * Space:
     * O(m * n)
     *
     * Interview Preference:
     *
     * Excellent derivation step before bottom-up DP.
     */
    static class MemoizedSolution {

        private Integer[][] memo;

        public int minDistance(String word1, String word2) {

            memo = new Integer[word1.length() + 1]
                    [word2.length() + 1];

            return dfs(word1, word2, 0, 0);
        }

        private int dfs(
                String a,
                String b,
                int i,
                int j) {

            if (i == a.length()) {
                return b.length() - j;
            }

            if (j == b.length()) {
                return a.length() - i;
            }

            if (memo[i][j] != null) {
                return memo[i][j];
            }

            int answer;

            if (a.charAt(i) == b.charAt(j)) {

                answer =
                        dfs(a, b, i + 1, j + 1);

            } else {

                int insert =
                        dfs(a, b, i, j + 1);

                int delete =
                        dfs(a, b, i + 1, j);

                int replace =
                        dfs(a, b, i + 1, j + 1);

                answer =
                        1 + Math.min(
                                insert,
                                Math.min(delete, replace)
                        );
            }

            memo[i][j] = answer;

            return answer;
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Optimal Solution (Interview Preferred)
     * ------------------------------------------------------------------------
     *
     * Core Idea:
     *
     * Bottom-up DP.
     *
     * Build answers for smaller prefixes first.
     *
     * Invariant:
     *
     * Before computing dp[i][j]:
     *
     * dp[i-1][j]
     * dp[i][j-1]
     * dp[i-1][j-1]
     *
     * are already correct.
     *
     * Time:
     * O(m * n)
     *
     * Space:
     * O(m * n)
     *
     * Interview Preference:
     *
     * Preferred.
     *
     * Easy to reason about.
     * Easy to debug.
     * Easy to derive.
     */
    static class OptimalSolution {

        public int minDistance(String word1, String word2) {

            int m = word1.length();
            int n = word2.length();

            int[][] dp = new int[m + 1][n + 1];

            // Transform prefix into empty string.
            for (int i = 0; i <= m; i++) {
                dp[i][0] = i;
            }

            // Transform empty string into prefix.
            for (int j = 0; j <= n; j++) {
                dp[0][j] = j;
            }

            for (int i = 1; i <= m; i++) {

                for (int j = 1; j <= n; j++) {

                    // Invariant:
                    // dependencies already solved.

                    if (word1.charAt(i - 1)
                            == word2.charAt(j - 1)) {

                        // Characters already match.
                        // Carry optimal diagonal answer.

                        dp[i][j] =
                                dp[i - 1][j - 1];

                    } else {

                        int delete =
                                dp[i - 1][j];

                        int insert =
                                dp[i][j - 1];

                        int replace =
                                dp[i - 1][j - 1];

                        // One operation plus best choice.

                        dp[i][j] =
                                1 + Math.min(
                                        delete,
                                        Math.min(insert, replace)
                                );
                    }
                }
            }

            // Bottom-right cell is entire problem.

            return dp[m][n];
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Space Optimized Variant
     * ------------------------------------------------------------------------
     *
     * Not usually required in interviews unless asked.
     *
     * Observation:
     *
     * Current row only depends on:
     *
     * previous row
     * current row left cell
     *
     * Space:
     * O(n)
     */
    static class SpaceOptimizedSolution {

        public int minDistance(String word1, String word2) {

            int m = word1.length();
            int n = word2.length();

            int[] prev = new int[n + 1];

            for (int j = 0; j <= n; j++) {
                prev[j] = j;
            }

            for (int i = 1; i <= m; i++) {

                int[] curr = new int[n + 1];

                curr[0] = i;

                for (int j = 1; j <= n; j++) {

                    if (word1.charAt(i - 1)
                            == word2.charAt(j - 1)) {

                        curr[j] = prev[j - 1];

                    } else {

                        curr[j] =
                                1 + Math.min(
                                        prev[j],
                                        Math.min(
                                                curr[j - 1],
                                                prev[j - 1]
                                        )
                                );
                    }
                }

                prev = curr;
            }

            return prev[n];
        }
    }

/**
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION (NO CODE)
 * =========================================================================
 *
 * State:
 *
 * dp[i][j]
 * =
 * minimum edits needed to convert
 * first i chars of word1
 * into
 * first j chars of word2.
 *
 * Invariant:
 *
 * Every cell stores the true optimal answer
 * for its prefix pair.
 *
 * Discard Logic:
 *
 * Not a search problem.
 *
 * Instead:
 *
 * every state considers all valid final operations.
 *
 * Correctness Guarantee:
 *
 * Any optimal sequence must end with:
 *
 * insert
 * delete
 * replace
 * match
 *
 * Therefore recurrence is complete.
 *
 * What Breaks If Changed?
 *
 * If delete transition removed:
 *
 * strings requiring deletion become impossible.
 *
 * If insert transition removed:
 *
 * target growth becomes impossible.
 *
 * If replace removed:
 *
 * many optimal paths disappear.
 *
 * In-place Feasibility:
 *
 * Full matrix not required.
 *
 * O(n) space possible.
 *
 * Streaming Feasibility:
 *
 * Not naturally streaming because
 * future states depend on previous row information.
 *
 * When NOT To Use:
 *
 * If operations differ.
 *
 * If costs are weighted differently.
 *
 * If transformation rules change completely.
 */

/**
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET (30-SECOND RECALL)
 * =========================================================================
 *
 * Pattern Trigger:
 *
 * Two strings.
 * Minimum conversion cost.
 *
 * Core Invariant:
 *
 * dp[i][j]
 * =
 * optimal answer for prefix pair.
 *
 * Search Target:
 *
 * dp[m][n]
 *
 * Transition:
 *
 * match -> diagonal
 *
 * mismatch ->
 * 1 + min(
 *      delete,
 *      insert,
 *      replace
 * )
 *
 * Common Trap:
 *
 * Forgetting first row / first column initialization.
 *
 * Edge Cases:
 *
 * empty strings
 * equal strings
 * single character strings
 *
 * Interview One-Liner:
 *
 * "I model edit distance as minimum cost between
 * prefix pairs and fill a DP table bottom-up."
 *
 * Re-Derivation Cue:
 *
 * Ask:
 *
 * "What could the final operation have been?"
 */

    /**
     * =========================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =========================================================================
     *
     * Variation:
     * Different insert/delete/replace costs.
     *
     * Status:
     * Invariant survives.
     *
     * Only transition weights change.
     *
     * -------------------------------------------------------------------------
     *
     * Variation:
     * Replace not allowed.
     *
     * Status:
     * Invariant survives.
     *
     * Remove replace transition.
     *
     * -------------------------------------------------------------------------
     *
     * Variation:
     * Adjacent swap allowed.
     *
     * Status:
     * Standard edit-distance recurrence breaks.
     *
     * Need Damerau-Levenshtein logic.
     *
     * -------------------------------------------------------------------------
     *
     * Variation:
     * Case-insensitive comparison.
     *
     * Status:
     * Same DP.
     *
     * Normalize input first.
     *
     * -------------------------------------------------------------------------
     *
     * Pattern Break Signals:
     *
     * - State cannot be expressed as prefixes.
     * - Operation depends on distant history.
     * - Future decisions alter past costs.
     * - Optimal substructure disappears.
     *
     * Then standard edit distance DP is insufficient.
     */

    /**
     * =========================================================================
     * ⚫ REINFORCEMENT PROBLEM #1
     * =========================================================================
     *
     * LeetCode 583
     * Delete Operation for Two Strings
     *
     * Summary:
     *
     * Minimum deletions required to make
     * two strings equal.
     *
     * Example:
     *
     * sea
     * eat
     *
     * answer = 2
     *
     * Invariant Mapping:
     *
     * Same prefix DP.
     *
     * Difference:
     *
     * Only deletion operations exist.
     *
     * State:
     *
     * dp[i][j]
     * =
     * minimum deletions required to make
     * prefixes equal.
     *
     * Edge Cases:
     *
     * empty strings
     * identical strings
     *
     * Interview Trap:
     *
     * Many candidates solve via LCS.
     *
     * Valid.
     *
     * But direct DP also works.
     */
    static class DeleteOperationForTwoStrings {

        public int minDistance(String word1, String word2) {

            int m = word1.length();
            int n = word2.length();

            int[][] dp = new int[m + 1][n + 1];

            for (int i = 0; i <= m; i++) {
                dp[i][0] = i;
            }

            for (int j = 0; j <= n; j++) {
                dp[0][j] = j;
            }

            for (int i = 1; i <= m; i++) {

                for (int j = 1; j <= n; j++) {

                    if (word1.charAt(i - 1)
                            == word2.charAt(j - 1)) {

                        dp[i][j] = dp[i - 1][j - 1];

                    } else {

                        dp[i][j] =
                                1 + Math.min(
                                        dp[i - 1][j],
                                        dp[i][j - 1]
                                );
                    }
                }
            }

            return dp[m][n];
        }
    }

    /**
     * =========================================================================
     * ⚫ REINFORCEMENT PROBLEM #2
     * =========================================================================
     *
     * LeetCode 712
     * Minimum ASCII Delete Sum For Two Strings
     *
     * Summary:
     *
     * Delete characters from both strings
     * so remaining strings become equal.
     *
     * Cost:
     *
     * ASCII value of deleted characters.
     *
     * Example:
     *
     * s = "sea"
     * t = "eat"
     *
     * answer = 231
     *
     * Invariant Mapping:
     *
     * Same prefix-to-prefix state.
     *
     * Transition costs change.
     *
     * Interview Trap:
     *
     * Structure remains identical.
     *
     * Only weights differ.
     */
    static class MinimumASCIIDeleteSum {

        public int minimumDeleteSum(
                String s1,
                String s2) {

            int m = s1.length();
            int n = s2.length();

            int[][] dp = new int[m + 1][n + 1];

            for (int i = 1; i <= m; i++) {
                dp[i][0] =
                        dp[i - 1][0]
                                + s1.charAt(i - 1);
            }

            for (int j = 1; j <= n; j++) {
                dp[0][j] =
                        dp[0][j - 1]
                                + s2.charAt(j - 1);
            }

            for (int i = 1; i <= m; i++) {

                for (int j = 1; j <= n; j++) {

                    if (s1.charAt(i - 1)
                            == s2.charAt(j - 1)) {

                        dp[i][j] =
                                dp[i - 1][j - 1];

                    } else {

                        int deleteFromS1 =
                                dp[i - 1][j]
                                        + s1.charAt(i - 1);

                        int deleteFromS2 =
                                dp[i][j - 1]
                                        + s2.charAt(j - 1);

                        dp[i][j] =
                                Math.min(
                                        deleteFromS1,
                                        deleteFromS2
                                );
                    }
                }
            }

            return dp[m][n];
        }
    }

    /**
     * =========================================================================
     * ⚫ REINFORCEMENT PROBLEM #3
     * =========================================================================
     *
     * LeetCode 1143
     * Longest Common Subsequence
     *
     * Summary:
     *
     * Find longest subsequence present
     * in both strings.
     *
     * Example:
     *
     * abcde
     * ace
     *
     * answer = 3
     *
     * Invariant Mapping:
     *
     * Same grid.
     *
     * Same prefix state.
     *
     * Optimization target changes:
     *
     * maximize matches instead of
     * minimize edit cost.
     *
     * Interview Trap:
     *
     * Same state shape.
     *
     * Different recurrence.
     */
    static class LongestCommonSubsequence {

        public int longestCommonSubsequence(
                String text1,
                String text2) {

            int m = text1.length();
            int n = text2.length();

            int[][] dp = new int[m + 1][n + 1];

            for (int i = 1; i <= m; i++) {

                for (int j = 1; j <= n; j++) {

                    if (text1.charAt(i - 1)
                            == text2.charAt(j - 1)) {

                        dp[i][j] =
                                1 + dp[i - 1][j - 1];

                    } else {

                        dp[i][j] =
                                Math.max(
                                        dp[i - 1][j],
                                        dp[i][j - 1]
                                );
                    }
                }
            }

            return dp[m][n];
        }
    }

    /**
     * =========================================================================
     * 🧩 RELATED PROBLEM #1
     * =========================================================================
     *
     * LeetCode 115
     * Distinct Subsequences
     *
     * Same / Modified / Broken Invariant:
     *
     * Modified.
     *
     * Prefix state survives.
     *
     * Objective changes:
     *
     * count ways instead of minimize cost.
     *
     * Edge Case:
     *
     * target longer than source.
     *
     * Interview Note:
     *
     * Excellent example showing that
     * state design matters more than recurrence.
     */
    static class DistinctSubsequences {

        public int numDistinct(String s, String t) {

            int m = s.length();
            int n = t.length();

            long[][] dp = new long[m + 1][n + 1];

            for (int i = 0; i <= m; i++) {
                dp[i][0] = 1;
            }

            for (int i = 1; i <= m; i++) {

                for (int j = 1; j <= n; j++) {

                    dp[i][j] = dp[i - 1][j];

                    if (s.charAt(i - 1)
                            == t.charAt(j - 1)) {

                        dp[i][j] +=
                                dp[i - 1][j - 1];
                    }
                }
            }

            return (int) dp[m][n];
        }
    }


    /**
     * =========================================================================
     * 🧩 RELATED PROBLEM #2
     * =========================================================================
     *
     * LeetCode 97
     * Interleaving String
     *
     * Summary:
     *
     * Determine whether s3 can be formed by
     * interleaving s1 and s2.
     *
     * Same / Modified / Broken Invariant:
     *
     * Modified.
     *
     * State remains prefix-based:
     *
     * dp[i][j]
     *
     * but meaning changes.
     *
     * dp[i][j]
     * =
     * whether first i chars of s1 and
     * first j chars of s2 can build
     * first (i+j) chars of s3.
     *
     * Edge Case:
     *
     * Length mismatch.
     *
     * Interview Note:
     *
     * Excellent example of reusing
     * prefix-state thinking.
     */
    static class InterleavingString {

        public boolean isInterleave(
                String s1,
                String s2,
                String s3) {

            int m = s1.length();
            int n = s2.length();

            if (m + n != s3.length()) {
                return false;
            }

            boolean[][] dp =
                    new boolean[m + 1][n + 1];

            dp[0][0] = true;

            for (int i = 0; i <= m; i++) {

                for (int j = 0; j <= n; j++) {

                    if (i > 0) {

                        dp[i][j] |=
                                dp[i - 1][j]
                                        &&
                                        s1.charAt(i - 1)
                                                ==
                                                s3.charAt(i + j - 1);
                    }

                    if (j > 0) {

                        dp[i][j] |=
                                dp[i][j - 1]
                                        &&
                                        s2.charAt(j - 1)
                                                ==
                                                s3.charAt(i + j - 1);
                    }
                }
            }

            return dp[m][n];
        }
    }

    /**
     * =========================================================================
     * 🧩 RELATED PROBLEM #3
     * =========================================================================
     *
     * LeetCode 516
     * Longest Palindromic Subsequence
     *
     * Summary:
     *
     * Find longest palindromic subsequence.
     *
     * Same / Modified / Broken Invariant:
     *
     * Modified.
     *
     * State becomes interval DP.
     *
     * Prefix invariant no longer sufficient.
     *
     * Edge Case:
     *
     * Single character string.
     *
     * Interview Note:
     *
     * Important boundary.
     *
     * Demonstrates when prefix DP
     * evolves into interval DP.
     */
    static class LongestPalindromicSubsequence {

        public int longestPalindromeSubseq(
                String s) {

            int n = s.length();

            int[][] dp = new int[n][n];

            for (int i = n - 1; i >= 0; i--) {

                dp[i][i] = 1;

                for (int j = i + 1; j < n; j++) {

                    if (s.charAt(i)
                            == s.charAt(j)) {

                        dp[i][j] =
                                2 + dp[i + 1][j - 1];

                    } else {

                        dp[i][j] =
                                Math.max(
                                        dp[i + 1][j],
                                        dp[i][j - 1]
                                );
                    }
                }
            }

            return dp[0][n - 1];
        }
    }

    /**
     * =========================================================================
     * 🧠 MASTERY CHECKLIST
     * =========================================================================
     *
     * Q: What is the invariant?
     *
     * A:
     *
     * dp[i][j]
     * =
     * minimum edit operations required
     * to convert first i chars of word1
     * into first j chars of word2.
     *
     * -------------------------------------------------------------------------
     *
     * Q: What is the search target?
     *
     * A:
     *
     * dp[m][n]
     *
     * -------------------------------------------------------------------------
     *
     * Q: What is the discard rule?
     *
     * A:
     *
     * This is DP.
     *
     * We do not discard search space.
     *
     * We evaluate all valid final operations.
     *
     * -------------------------------------------------------------------------
     *
     * Q: What is termination logic?
     *
     * A:
     *
     * Bottom-right cell represents
     * complete strings.
     *
     * -------------------------------------------------------------------------
     *
     * Q: Why does naive recursion fail?
     *
     * A:
     *
     * Overlapping subproblems create
     * exponential recomputation.
     *
     * -------------------------------------------------------------------------
     *
     * Q: Essential edge cases?
     *
     * A:
     *
     * "" , ""
     *
     * "" , "abc"
     *
     * "abc" , ""
     *
     * identical strings
     *
     * single-character strings
     *
     * -------------------------------------------------------------------------
     *
     * Q: Debugging readiness?
     *
     * A:
     *
     * Verify:
     *
     * first row
     * first column
     * diagonal transition
     * indexing uses i-1/j-1
     *
     * -------------------------------------------------------------------------
     *
     * Q: Variant readiness?
     *
     * A:
     *
     * Yes.
     *
     * Most weighted-edit problems
     * reuse same state.
     *
     * -------------------------------------------------------------------------
     *
     * Q: Pattern boundary?
     *
     * A:
     *
     * Once state depends on intervals,
     * history,
     * or non-local edits,
     * this recurrence may break.
     */

    private static void assertEquals(
            int expected,
            int actual,
            String message) {

        if (expected != actual) {

            throw new AssertionError(
                    message
                            + " Expected="
                            + expected
                            + " Actual="
                            + actual
            );
        }
    }

    private static void assertTrue(
            boolean value,
            String message) {

        if (!value) {

            throw new AssertionError(message);
        }
    }

    private static void verifyEditDistance(
            String word1,
            String word2,
            int expected) {

        OptimalSolution solution =
                new OptimalSolution();

        int actual =
                solution.minDistance(
                        word1,
                        word2
                );

        assertEquals(
                expected,
                actual,
                "Edit Distance Failure"
        );
    }

    private static void verifyDeleteOperation(
            String a,
            String b,
            int expected) {

        DeleteOperationForTwoStrings solution =
                new DeleteOperationForTwoStrings();

        int actual =
                solution.minDistance(a, b);

        assertEquals(
                expected,
                actual,
                "Delete Operation Failure"
        );
    }


    private static void verifyLCS(
            String a,
            String b,
            int expected) {

        LongestCommonSubsequence solution =
                new LongestCommonSubsequence();

        int actual =
                solution.longestCommonSubsequence(
                        a,
                        b
                );

        assertEquals(
                expected,
                actual,
                "LCS Failure"
        );
    }

    private static void verifyInterleaving(
            String s1,
            String s2,
            String s3,
            boolean expected) {

        InterleavingString solution =
                new InterleavingString();

        boolean actual =
                solution.isInterleave(
                        s1,
                        s2,
                        s3
                );

        if (actual != expected) {

            throw new AssertionError(
                    "Interleaving Failure"
                            + " Expected="
                            + expected
                            + " Actual="
                            + actual
            );
        }
    }

    private static void verifyDistinctSubsequence(
            String s,
            String t,
            int expected) {

        DistinctSubsequences solution =
                new DistinctSubsequences();

        int actual =
                solution.numDistinct(
                        s,
                        t
                );

        assertEquals(
                expected,
                actual,
                "Distinct Subsequences Failure"
        );
    }

    private static void verifyLPS(
            String s,
            int expected) {

        LongestPalindromicSubsequence solution =
                new LongestPalindromicSubsequence();

        int actual =
                solution.longestPalindromeSubseq(s);

        assertEquals(
                expected,
                actual,
                "Longest Palindromic Subsequence Failure"
        );
    }

    private static void verifyAsciiDeleteSum(
            String a,
            String b,
            int expected) {

        MinimumASCIIDeleteSum solution =
                new MinimumASCIIDeleteSum();

        int actual =
                solution.minimumDeleteSum(
                        a,
                        b
                );

        assertEquals(
                expected,
                actual,
                "ASCII Delete Sum Failure"
        );
    }

    /**
     * =========================================================================
     * 🧪 SELF-VERIFYING TEST PLAN
     * =========================================================================
     *
     * Test Category:
     *
     * Happy Paths
     * Edge Cases
     * Boundary Cases
     * Interview Traps
     * Reinforcement Problems
     *
     * Every test exists for a reason.
     */

    public static void main(String[] args) {

        /**
         * ---------------------------------------------------------------------
         * PRIMARY PROBLEM TESTS
         * ---------------------------------------------------------------------
         */

        // Official Example #1
        // Replace + delete + delete.
        verifyEditDistance(
                "horse",
                "ros",
                3
        );

        // Official Example #2
        verifyEditDistance(
                "intention",
                "execution",
                5
        );

        // Equal strings.
        verifyEditDistance(
                "abc",
                "abc",
                0
        );

        // Empty to empty.
        verifyEditDistance(
                "",
                "",
                0
        );

        // Empty source.
        verifyEditDistance(
                "",
                "abc",
                3
        );

        // Empty target.
        verifyEditDistance(
                "abc",
                "",
                3
        );

        // Single replacement.
        verifyEditDistance(
                "a",
                "b",
                1
        );

        // Single insertion.
        verifyEditDistance(
                "a",
                "ab",
                1
        );

        // Single deletion.
        verifyEditDistance(
                "ab",
                "a",
                1
        );

        // Common interview example.
        verifyEditDistance(
                "kitten",
                "sitting",
                3
        );

        /**
         * ---------------------------------------------------------------------
         * REINFORCEMENT TESTS
         * ---------------------------------------------------------------------
         */

        verifyDeleteOperation(
                "sea",
                "eat",
                2
        );

        verifyAsciiDeleteSum(
                "sea",
                "eat",
                231
        );

        verifyLCS(
                "abcde",
                "ace",
                3
        );

        /**
         * ---------------------------------------------------------------------
         * RELATED PROBLEM TESTS
         * ---------------------------------------------------------------------
         */

        verifyDistinctSubsequence(
                "rabbbit",
                "rabbit",
                3
        );

        verifyInterleaving(
                "aabcc",
                "dbbca",
                "aadbbcbcac",
                true
        );

        verifyInterleaving(
                "aabcc",
                "dbbca",
                "aadbbbaccc",
                false
        );

        verifyLPS(
                "bbbab",
                4
        );

        /**
         * ---------------------------------------------------------------------
         * BOUNDARY TESTS
         * ---------------------------------------------------------------------
         */

        StringBuilder left =
                new StringBuilder();

        StringBuilder right =
                new StringBuilder();

        for (int i = 0; i < 100; i++) {

            left.append('a');
            right.append('a');
        }

        verifyEditDistance(
                left.toString(),
                right.toString(),
                0
        );

        /**
         * ---------------------------------------------------------------------
         * INTERNAL CONSISTENCY TEST
         * ---------------------------------------------------------------------
         *
         * Brute Force
         * Memoized
         * Optimal
         *
         * must agree.
         */

        String a = "abc";
        String b = "yabd";

        int brute =
                new BruteForceSolution()
                        .minDistance(a, b);

        int memo =
                new MemoizedSolution()
                        .minDistance(a, b);

        int optimal =
                new OptimalSolution()
                        .minDistance(a, b);

        assertTrue(
                brute == memo
                        && memo == optimal,
                "Solutions disagree"
        );




        /**
         * ---------------------------------------------------------------------
         * INDEXING TRAP TEST
         * ---------------------------------------------------------------------
         *
         * Catches many i-1 / j-1 mistakes.
         */

        verifyEditDistance(
                "z",
                "abcdefghijklmnopz",
                16
        );

        /**
         * ---------------------------------------------------------------------
         * REPEATED CHARACTER TEST
         * ---------------------------------------------------------------------
         *
         * Catches incorrect greedy reasoning.
         */

        verifyEditDistance(
                "aaaaaa",
                "aaa",
                3
        );

        /**
         * ---------------------------------------------------------------------
         * PREFIX/SUFFIX TEST
         * ---------------------------------------------------------------------
         *
         * Large common prefix.
         */

        verifyEditDistance(
                "abcdefxyz",
                "abcdef",
                3
        );

        /**
         * ---------------------------------------------------------------------
         * COMPLETE
         * ---------------------------------------------------------------------
         */

        System.out.println(
                "All tests passed."
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


