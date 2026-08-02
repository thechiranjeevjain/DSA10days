package org.chijai.day9.session1;

import java.util.Arrays;

public class UniquePaths {

    /*
     * ================================================================
     * 2. 📘 PRIMARY PROBLEM
     * ================================================================
     *
     * Title:
     * Unique Paths
     *
     * Difficulty:
     * Medium
     *
     * Tags:
     * Dynamic Programming
     * Grid DP
     * Counting
     * Combinatorics
     *
     * Problem Description
     * -------------------
     * A robot starts at the top-left cell of an m x n grid.
     *
     * The robot may only move:
     *   • Right
     *   • Down
     *
     * Determine how many different paths exist from
     * (0,0) to (m-1,n-1).
     *
     * Every valid path consists only of right and down moves.
     *
     * Constraints
     * -----------
     * 1 <= m,n <= 100
     *
     * The final answer always fits inside a signed 32-bit integer.
     *
     * Examples
     * --------
     *
     * Example 1
     *
     * m = 3
     * n = 7
     *
     * Answer = 28
     *
     * Example 2
     *
     * m = 3
     * n = 2
     *
     * Answer = 3
     *
     * Paths:
     * R D D
     * D D R
     * D R D
     *
     * Official LeetCode
     * -----------------
     * https://leetcode.com/problems/unique-paths/
     */

    /*
     * ================================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ================================================================
     *
     * Pattern
     * -------
     * Grid Dynamic Programming
     *
     * Archetype
     * ---------
     * Count all possible ways to reach every state using
     * previously solved neighboring states.
     *
     * Core Invariant
     * --------------
     * dp[i][j]
     * always equals
     * the number of distinct ways to reach cell (i,j).
     *
     * Why It Works
     * ------------
     * Every path entering a cell must come from exactly one
     * of two predecessors:
     *
     *      top
     *      left
     *
     * Therefore
     *
     * dp[i][j]
     * =
     * dp[i-1][j]
     * +
     * dp[i][j-1]
     *
     * Every path is counted exactly once.
     *
     * Recognition Signals
     * -------------------
     * ✓ Count ways
     * ✓ Grid
     * ✓ Fixed movement directions
     * ✓ Optimal substructure
     * ✓ State depends only on nearby states
     *
     * Use When
     * --------
     * • Counting paths
     * • Counting sequences
     * • Restricted movement
     * • DAG-style transitions
     *
     * Do NOT Use When
     * ---------------
     * • Cycles exist
     * • Future affects past
     * • State cannot be represented locally
     *
     * Comparison
     * ----------
     *
     * DFS
     * ----
     * Explores every path individually.
     *
     * DP
     * --
     * Reuses overlapping subproblems.
     *
     * BFS
     * ---
     * Finds shortest path.
     *
     * DP
     * --
     * Counts every possible path.
     *
     * Backtracking
     * ------------
     * Enumerates paths.
     *
     * DP
     * --
     * Counts without enumeration.
     */

    /*
     * ================================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ================================================================
     *
     * Mental Model
     * ------------
     * Imagine pouring water from the start.
     *
     * Every cell accumulates all water arriving from:
     *
     *   ↑
     *   ←
     *
     * The amount collected at a cell equals the number of
     * unique paths reaching that cell.
     *
     * Eventually the destination stores the final answer.
     *
     * ------------------------------------------------
     * Primary Invariant
     * ------------------------------------------------
     *
     * After processing cell (i,j),
     *
     * dp[i][j]
     *
     * permanently equals the number of valid paths from
     * (0,0) to (i,j).
     *
     * Once written,
     * this value never changes.
     *
     * ------------------------------------------------
     * State Meaning
     * ------------------------------------------------
     *
     * dp[i][j]
     *
     * =
     *
     * number of paths ending exactly at (i,j)
     *
     * NOT
     *
     * number of remaining paths.
     *
     * ------------------------------------------------
     * Variable Meaning
     * ------------------------------------------------
     *
     * m
     * number of rows
     *
     * n
     * number of columns
     *
     * i
     * current row
     *
     * j
     * current column
     *
     * ------------------------------------------------
     * Allowed Transition
     * ------------------------------------------------
     *
     * From top
     *
     * dp[i-1][j]
     *
     * From left
     *
     * dp[i][j-1]
     *
     * Therefore
     *
     * dp[i][j]
     * =
     * top + left
     *
     * ------------------------------------------------
     * Forbidden Transition
     * ------------------------------------------------
     *
     * Never use
     *
     * bottom
     * right
     * diagonal
     *
     * because those states have not yet been computed in
     * row-major order.
     *
     * ------------------------------------------------
     * Initialization Invariant
     * ------------------------------------------------
     *
     * First row:
     *
     * Every cell has exactly one path.
     *
     * Keep moving right.
     *
     * First column:
     *
     * Every cell has exactly one path.
     *
     * Keep moving down.
     *
     * ------------------------------------------------
     * Termination
     * ------------------------------------------------
     *
     * Once every cell has been processed,
     *
     * dp[m-1][n-1]
     *
     * is final.
     *
     * ------------------------------------------------
     * Why Naive Recursion Fails
     * ------------------------------------------------
     *
     * Every state repeatedly recomputes identical
     * subproblems.
     *
     * Example:
     *
     * Paths to
     * (5,5)
     *
     * are recomputed by nearly every ancestor.
     *
     * This causes exponential growth.
     */

    /*
     * ================================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ================================================================
     *
     * Mistake 1
     * ---------
     * Forgetting base initialization.
     *
     * Why It Looks Correct
     * --------------------
     * Transition itself is correct.
     *
     * Broken Invariant
     * ----------------
     * Every state depends on already-correct predecessor
     * values.
     *
     * Counterexample
     * --------------
     * m=1
     * n=5
     *
     * Without initializing first row,
     * answer becomes zero.
     *
     * ------------------------------------------------
     * Mistake 2
     * ------------------------------------------------
     * Starting inner loops from zero.
     *
     * Result
     * ------
     * Access
     *
     * dp[-1][j]
     *
     * or
     *
     * dp[i][-1]
     *
     * ------------------------------------------------
     * Mistake 3
     * ------------------------------------------------
     * Using multiplication instead of addition.
     *
     * Trap
     * ----
     * We combine independent path counts,
     * not probabilities.
     *
     * ------------------------------------------------
     * Mistake 4
     * ------------------------------------------------
     * Recursive brute force during interview.
     *
     * Looks Elegant
     * -------------
     * Tiny implementation.
     *
     * Reality
     * -------
     * Explodes exponentially.
     *
     * ------------------------------------------------
     * Mistake 5
     * ------------------------------------------------
     * Thinking greedy movement works.
     *
     * There is no optimization objective.
     *
     * We are counting every valid path.
     */

    /*
     * ================================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * ================================================================
     *
     * Typing Order
     * ------------
     *
     * 1.
     * Create dp table.
     *
     * 2.
     * Fill first column with 1.
     *
     * 3.
     * Fill first row with 1.
     *
     * 4.
     * Loop rows from 1.
     *
     * 5.
     * Loop columns from 1.
     *
     * 6.
     * Compute
     *
     * top = dp[i-1][j]
     * left = dp[i][j-1]
     *
     * 7.
     * Store
     *
     * top + left
     *
     * 8.
     * Return bottom-right.
     *
     * Mechanical Skeleton
     * -------------------
     *
     * allocate
     *
     * initialize borders
     *
     * nested loops
     *
     * transition
     *
     * return destination
     */

    /*
     * ================================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ================================================================
     *
     * create table
     *
     * initialize borders
     *
     * for each remaining cell
     *     state = top + left
     *
     * return destination
     */

    /*
     * ================================================================
     * 6. SOLUTION CLASSES
     * ================================================================
     */

    /**
     * ------------------------------------------------
     * Brute Force
     * ------------------------------------------------
     *
     * Idea
     * ----
     * Try both legal moves recursively.
     *
     * Invariant
     * ---------
     * Every recursive call represents one robot position.
     *
     * Limitation
     * ----------
     * Massive repeated computation.
     *
     * Complexity
     * ----------
     * Time:
     * O(2^(m+n))
     *
     * Space:
     * O(m+n)
     *
     * Interview Usefulness
     * --------------------
     * Good starting point before introducing memoization.
     */
    static class BruteForce {

        public int uniquePaths(int m, int n) {
            return dfs(m, n, 0, 0);
        }

        private int dfs(int m, int n, int row, int col) {

            // Invariant: outside grid cannot contribute a valid path.
            if (row >= m || col >= n) {
                return 0;
            }

            // Invariant: reaching destination completes exactly one path.
            if (row == m - 1 && col == n - 1) {
                return 1;
            }

            int down = dfs(m, n, row + 1, col);
            int right = dfs(m, n, row, col + 1);

            return down + right;
        }
    }

    /**
     * ------------------------------------------------
     * Improved
     * ------------------------------------------------
     *
     * Idea
     * ----
     * Cache every state once.
     *
     * Invariant
     * ---------
     * memo[row][col]
     * stores the final answer for that state forever.
     *
     * Improvement
     * -----------
     * Eliminates repeated recursion.
     *
     * Complexity
     * ----------
     * Time:
     * O(m*n)
     *
     * Space:
     * O(m*n)
     *
     * Interview Usefulness
     * --------------------
     * Natural bridge from recursion to tabulation.
     */
    static class Memoization {

        public int uniquePaths(int m, int n) {

            int[][] memo = new int[m][n];

            for (int[] row : memo) {
                Arrays.fill(row, -1);
            }

            return solve(0, 0, m, n, memo);
        }

        private int solve(int row, int col, int m, int n, int[][] memo) {

            if (row >= m || col >= n) {
                return 0;
            }

            if (row == m - 1 && col == n - 1) {
                return 1;
            }

            if (memo[row][col] != -1) {
                return memo[row][col];
            }

            memo[row][col] =
                    solve(row + 1, col, m, n, memo)
                            + solve(row, col + 1, m, n, memo);

            return memo[row][col];
        }
    }
    /**
     * ------------------------------------------------
     * Optimal (Interview Preferred)
     * ------------------------------------------------
     *
     * Idea
     * ----
     * Build the answer bottom-up.
     *
     * Every state is computed exactly once after both of
     * its predecessor states are already finalized.
     *
     * 🟢 Invariant
     * ------------
     * Before computing dp[i][j]:
     *
     * dp[i - 1][j]
     * and
     * dp[i][j - 1]
     *
     * already contain their final path counts.
     *
     * Therefore
     *
     * dp[i][j]
     *
     * can also become final immediately.
     *
     * Correctness
     * -----------
     * Every valid path reaching (i,j)
     * must enter from exactly one predecessor:
     *
     *   top
     *   left
     *
     * The predecessor sets are disjoint.
     *
     * Adding their counts counts every path exactly once.
     *
     * Complexity
     * ----------
     * Time:
     * O(m × n)
     *
     * Space:
     * O(m × n)
     *
     * Interview Usefulness
     * --------------------
     * This is the standard expected interview solution.
     */
    static class Optimal {

        public int uniquePaths(int m, int n) {

            if (m <= 0 || n <= 0) {
                return 0;
            }

            if (m == 1 || n == 1) {
                return 1;
            }

            int[][] dp = new int[m][n];

            // Invariant: only one way to stay on first column.
            for (int row = 0; row < m; row++) {
                dp[row][0] = 1;
            }

            // Invariant: only one way to stay on first row.
            for (int col = 0; col < n; col++) {
                dp[0][col] = 1;
            }

            for (int row = 1; row < m; row++) {

                for (int col = 1; col < n; col++) {

                    // Invariant: predecessor states are already final.
                    int fromTop = dp[row - 1][col];

                    // Invariant: left predecessor is finalized.
                    int fromLeft = dp[row][col - 1];

                    // Every path enters from exactly one predecessor.
                    dp[row][col] = fromTop + fromLeft;
                }
            }

            // Invariant: destination now stores total path count.
            return dp[m - 1][n - 1];
        }
    }

    /**
     * ------------------------------------------------
     * Space Optimized
     * ------------------------------------------------
     *
     * Idea
     * ----
     * Observe that each row only depends on:
     *
     * current row
     * previous row
     *
     * Instead of storing the entire matrix,
     * reuse one array.
     *
     * State Meaning
     * -------------
     *
     * dp[col]
     *
     * before update
     * =
     * paths from above
     *
     * dp[col - 1]
     *
     * after update
     * =
     * paths from left
     *
     * Transition
     * ----------
     *
     * dp[col]
     * =
     * dp[col]
     * +
     * dp[col - 1]
     *
     * Complexity
     * ----------
     * Time:
     * O(m × n)
     *
     * Space:
     * O(n)
     */
    static class SpaceOptimized {

        public int uniquePaths(int m, int n) {

            int[] dp = new int[n];

            Arrays.fill(dp, 1);

            for (int row = 1; row < m; row++) {

                for (int col = 1; col < n; col++) {

                    // Current value represents paths from above.
                    // Left value already represents current row.
                    dp[col] = dp[col] + dp[col - 1];
                }
            }

            return dp[n - 1];
        }
    }

    /**
     * ------------------------------------------------
     * Mathematical Combination
     * ------------------------------------------------
     *
     * Observation
     * -----------
     * Every valid path consists of
     *
     * (m-1) downs
     * and
     * (n-1) rights.
     *
     * Total moves
     *
     * =
     * m+n-2
     *
     * We simply choose where one move type occurs.
     *
     * Formula
     * -------
     *
     * C(m+n-2, m-1)
     *
     * or
     *
     * C(m+n-2, n-1)
     *
     * Complexity
     * ----------
     * Time:
     * O(min(m,n))
     *
     * Space:
     * O(1)
     *
     * Interview Note
     * --------------
     * Elegant but DP is usually preferred because the
     * obstacle variation extends naturally.
     */
    static class Mathematical {

        public int uniquePaths(int m, int n) {

            int totalMoves = m + n - 2;

            int choose = Math.min(m - 1, n - 1);

            long answer = 1;

            for (int i = 1; i <= choose; i++) {

                answer = answer * (totalMoves - choose + i) / i;
            }

            return (int) answer;
        }
    }

/*
 * ================================================================
 * 🟣 INTERVIEW ARTICULATION
 * ================================================================
 *
 * Pattern
 * -------
 * Grid Dynamic Programming.
 *
 * State
 * -----
 * dp[i][j]
 * =
 * number of paths reaching (i,j).
 *
 * Invariant
 * ---------
 * When processing a cell,
 * both predecessor states are already finalized.
 *
 * Transition
 * ----------
 * top + left.
 *
 * Discard Rule
 * ------------
 * There is no search-space elimination.
 *
 * Instead,
 * every state is solved exactly once.
 *
 * Correctness
 * -----------
 * Every valid path reaching a cell must arrive
 * through exactly one predecessor.
 *
 * Since predecessor path sets are disjoint,
 * adding them counts every valid path exactly once.
 *
 * Termination
 * -----------
 * The nested loops visit every reachable state.
 *
 * After the final iteration,
 * destination is finalized.
 *
 * In-place Feasibility
 * --------------------
 * Yes.
 *
 * Reduce to one-dimensional DP because only the
 * previous row is required.
 *
 * Streaming Feasibility
 * ---------------------
 * Yes.
 *
 * Row-by-row processing is sufficient.
 *
 * When NOT To Use
 * ---------------
 * If movement is unrestricted or cycles exist,
 * this simple recurrence no longer holds.
 */

/*
 * ================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ================================================================
 *
 * Trigger
 * -------
 * Count paths on a grid.
 *
 * Pattern
 * -------
 * Grid DP.
 *
 * State
 * -----
 * Paths ending at current cell.
 *
 * Invariant
 * ---------
 * Every predecessor is already correct.
 *
 * Transition
 * ----------
 * top + left.
 *
 * Search Target
 * -------------
 * Bottom-right cell.
 *
 * Common Trap
 * -----------
 * Forgetting first row / first column initialization.
 *
 * Edge Cases
 * ----------
 * 1 x n
 * m x 1
 * 1 x 1
 *
 * One-liner
 * ---------
 * Every path enters from top or left.
 *
 * Re-derivation Cue
 * -----------------
 * Ask:
 *
 * "Where could I have come from?"
 */
    /*
     * ================================================================
     * 🔄 VARIATIONS & TWEAKS
     * ============================================================================
     *
     * Variation 1
     * -----------
     * Unique Paths II (Obstacles)
     *
     * Change
     * ------
     * Some cells cannot be entered.
     *
     * New Invariant
     * -------------
     * dp[i][j]
     * always equals the number of valid paths that avoid
     * every obstacle encountered so far.
     *
     * Transition
     * ----------
     * obstacle
     * ->
     * dp[i][j] = 0
     *
     * otherwise
     *
     * dp[i][j]
     * =
     * top + left
     *
     * Pattern
     * -------
     * Exactly the same DP.
     *
     * Only the transition changes.
     *
     * ------------------------------------------------
     * Variation 2
     * ------------------------------------------------
     * Minimum Path Sum
     *
     * State
     * -----
     * Minimum cost instead of path count.
     *
     * Transition
     * ----------
     * min(top,left)
     * +
     * currentCell
     *
     * Pattern survives.
     *
     * Aggregation changes.
     *
     * ------------------------------------------------
     * Variation 3
     * ------------------------------------------------
     * Maximum Path Value
     *
     * Replace
     * -------
     * +
     *
     * with
     *
     * max()
     *
     * depending on problem definition.
     *
     * ------------------------------------------------
     * Variation 4
     * ------------------------------------------------
     * Diagonal Moves Allowed
     *
     * Transition becomes
     *
     * top
     * +
     * left
     * +
     * diagonal
     *
     * State definition stays identical.
     *
     * ------------------------------------------------
     * Variation 5
     * ------------------------------------------------
     * Variable Movement Length
     *
     * Transition must consider additional predecessor
     * states.
     *
     * Complexity usually increases.
     *
     * ------------------------------------------------
     * Variation 6
     * ------------------------------------------------
     * Cyclic Graph
     *
     * Pattern Breaks.
     *
     * Reason
     * ------
     * There is no topological evaluation order.
     *
     * DP state may depend on itself.
     *
     * ------------------------------------------------
     * Variation 7
     * ------------------------------------------------
     * Count Paths Mod M
     *
     * Transition
     *
     * (top + left) % MOD
     *
     * Invariant remains unchanged.
     *
     * ------------------------------------------------
     * Variation 8
     * ------------------------------------------------
     * Huge Grid
     *
     * Prefer
     *
     * O(n)
     *
     * space optimization.
     */

    /*
     * ================================================================
     * ⚫ PATTERN MAPPING
     * ================================================================
     *
     * Problem
     * -------
     * Unique Paths
     *
     * State
     * -----
     * Count
     *
     * Transition
     * ----------
     * Sum
     *
     * ------------------------------------------------
     *
     * Problem
     * -------
     * Unique Paths II
     *
     * State
     * -----
     * Count
     *
     * Transition
     * ----------
     * Obstacle ?
     * 0
     * :
     * top + left
     *
     * ------------------------------------------------
     *
     * Problem
     * -------
     * Minimum Path Sum
     *
     * State
     * -----
     * Minimum Cost
     *
     * Transition
     * ----------
     * min(top,left)
     * +
     * current
     *
     * ------------------------------------------------
     *
     * Problem
     * -------
     * Dungeon Game
     *
     * State
     * -----
     * Minimum health needed.
     *
     * Transition
     * ----------
     * Reverse DP.
     *
     * ------------------------------------------------
     *
     * Problem
     * -------
     * Triangle
     *
     * State
     * -----
     * Best path.
     *
     * Transition
     * ----------
     * Parent states.
     */

    /*
     * ================================================================
     * 🧠 UNIQUE PATHS II
     * ================================================================
     *
     * Problem
     * -------
     * Some cells contain obstacles.
     *
     * Robot still moves only:
     *
     * Right
     * Down
     *
     * State
     * -----
     * dp[i][j]
     *
     * =
     * valid paths reaching this cell.
     *
     * New Invariant
     * -------------
     * Obstacle cells permanently contain zero paths.
     *
     * Initialization
     * --------------
     * If start or destination is blocked,
     * answer is immediately zero.
     */

    static class UniquePathsII {

        public int uniquePathsWithObstacles(int[][] obstacleGrid) {

            if (obstacleGrid == null || obstacleGrid.length == 0) {
                return 0;
            }

            int rows = obstacleGrid.length;
            int cols = obstacleGrid[0].length;

            // Invariant: impossible if start or finish is blocked.
            if (obstacleGrid[0][0] == 1
                    || obstacleGrid[rows - 1][cols - 1] == 1) {
                return 0;
            }

            int[][] dp = new int[rows][cols];

            // Exactly one way to stand at the start.
            dp[0][0] = 1;

            // First column.
            for (int row = 1; row < rows; row++) {

                if (obstacleGrid[row][0] == 1) {

                    // Obstacle permanently blocks all paths below through
                    // this direction.
                    dp[row][0] = 0;

                } else {

                    // Only predecessor is directly above.
                    dp[row][0] = dp[row - 1][0];
                }
            }

            // First row.
            for (int col = 1; col < cols; col++) {

                if (obstacleGrid[0][col] == 1) {

                    dp[0][col] = 0;

                } else {

                    // Only predecessor is from the left.
                    dp[0][col] = dp[0][col - 1];
                }
            }

            for (int row = 1; row < rows; row++) {

                for (int col = 1; col < cols; col++) {

                    if (obstacleGrid[row][col] == 1) {

                        // Invariant: blocked cells cannot accumulate paths.
                        dp[row][col] = 0;

                    } else {

                        // Valid paths arrive only from reachable predecessors.
                        dp[row][col] =
                                dp[row - 1][col]
                                        + dp[row][col - 1];
                    }
                }
            }

            return dp[rows - 1][cols - 1];
        }
    }

/*
 * ================================================================
 * 🧠 MASTERY CHECKLIST
 * ================================================================
 *
 * □ I can define the DP state before writing code.
 *
 * □ I know why the first row is initialized to one.
 *
 * □ I know why the first column is initialized to one.
 *
 * □ I can derive the recurrence:
 *
 *   top + left
 *
 * □ I understand why no path is counted twice.
 *
 * □ I know why recursion becomes exponential.
 *
 * □ I can optimize O(m×n) space to O(n).
 *
 * □ I can adapt immediately to obstacles.
 *
 * □ I know when this DP pattern no longer applies.
 *
 * □ I can explain correctness without referring to code.
 */

    public static void main(String[] args) {

        BruteForce brute = new BruteForce();
        Memoization memo = new Memoization();
        Optimal optimal = new Optimal();
        SpaceOptimized optimized = new SpaceOptimized();
        Mathematical math = new Mathematical();
        UniquePathsII obstacleSolver = new UniquePathsII();

        /*
         * ================================================================
         * 🧪 SELF-VERIFYING TESTS
         * ================================================================
         *
         * Run with:
         *
         * java -ea UniquePaths
         *
         * Assertions remain silent when all invariants hold.
         */

        // ------------------------------------------------
        // Happy Path
        // Canonical LeetCode example.
        // ------------------------------------------------
        assert brute.uniquePaths(3, 2) == 3;
        assert memo.uniquePaths(3, 2) == 3;
        assert optimal.uniquePaths(3, 2) == 3;
        assert optimized.uniquePaths(3, 2) == 3;
        assert math.uniquePaths(3, 2) == 3;

        // ------------------------------------------------
        // Larger representative example.
        // ------------------------------------------------
        assert optimal.uniquePaths(3, 7) == 28;
        assert optimized.uniquePaths(3, 7) == 28;
        assert math.uniquePaths(3, 7) == 28;

        // ------------------------------------------------
        // Single cell.
        // Start is destination.
        // ------------------------------------------------
        assert optimal.uniquePaths(1, 1) == 1;
        assert optimized.uniquePaths(1, 1) == 1;
        assert math.uniquePaths(1, 1) == 1;

        // ------------------------------------------------
        // Single row.
        // Only right moves exist.
        // ------------------------------------------------
        assert optimal.uniquePaths(1, 10) == 1;
        assert optimized.uniquePaths(1, 10) == 1;
        assert math.uniquePaths(1, 10) == 1;

        // ------------------------------------------------
        // Single column.
        // Only down moves exist.
        // ------------------------------------------------
        assert optimal.uniquePaths(8, 1) == 1;
        assert optimized.uniquePaths(8, 1) == 1;
        assert math.uniquePaths(8, 1) == 1;

        // ------------------------------------------------
        // Symmetry property.
        // m x n == n x m.
        // ------------------------------------------------
        assert optimal.uniquePaths(5, 8) == optimal.uniquePaths(8, 5);
        assert optimized.uniquePaths(5, 8) == optimized.uniquePaths(8, 5);
        assert math.uniquePaths(5, 8) == math.uniquePaths(8, 5);

        // ------------------------------------------------
        // Larger grid within constraints.
        // Cross-check multiple implementations.
        // ------------------------------------------------
        int expected = optimal.uniquePaths(10, 10);

        assert memo.uniquePaths(10, 10) == expected;
        assert optimized.uniquePaths(10, 10) == expected;
        assert math.uniquePaths(10, 10) == expected;

        // ------------------------------------------------
        // Unique Paths II
        // Standard obstacle example.
        // ------------------------------------------------
        int[][] obstacleGrid1 = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };

        assert obstacleSolver.uniquePathsWithObstacles(obstacleGrid1) == 2;

        // ------------------------------------------------
        // One obstacle blocks first row.
        // ------------------------------------------------
        int[][] obstacleGrid2 = {
                {0, 1},
                {0, 0}
        };

        assert obstacleSolver.uniquePathsWithObstacles(obstacleGrid2) == 1;

        // ------------------------------------------------
        // Blocked start.
        // ------------------------------------------------
        int[][] obstacleGrid3 = {
                {1}
        };

        assert obstacleSolver.uniquePathsWithObstacles(obstacleGrid3) == 0;

        // ------------------------------------------------
        // Blocked destination.
        // ------------------------------------------------
        int[][] obstacleGrid4 = {
                {0, 0},
                {0, 1}
        };

        assert obstacleSolver.uniquePathsWithObstacles(obstacleGrid4) == 0;

        // ------------------------------------------------
        // One-cell empty grid.
        // ------------------------------------------------
        int[][] obstacleGrid5 = {
                {0}
        };

        assert obstacleSolver.uniquePathsWithObstacles(obstacleGrid5) == 1;

        // ------------------------------------------------
        // Entire middle row blocks traversal.
        // ------------------------------------------------
        int[][] obstacleGrid6 = {
                {0, 0, 0},
                {1, 1, 1},
                {0, 0, 0}
        };

        assert obstacleSolver.uniquePathsWithObstacles(obstacleGrid6) == 0;

        // ------------------------------------------------
        // Narrow grid with obstacle.
        // ------------------------------------------------
        int[][] obstacleGrid7 = {
                {0},
                {0},
                {1},
                {0}
        };

        assert obstacleSolver.uniquePathsWithObstacles(obstacleGrid7) == 0;

        // ------------------------------------------------
        // Two-by-two with no obstacles.
        // ------------------------------------------------
        int[][] obstacleGrid8 = {
                {0, 0},
                {0, 0}
        };

        assert obstacleSolver.uniquePathsWithObstacles(obstacleGrid8) == 2;

        System.out.println("All assertions passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/