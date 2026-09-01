package org.chijai.day9.dp.session1;

import java.util.Arrays;

/**
 * ============================================================================
 * UNIQUE PATHS — V4
 * ============================================================================
 *
 * LeetCode 62
 * https://leetcode.com/problems/unique-paths/
 *
 * PURPOSE
 * -------
 * This file is optimized for:
 *
 *      RECONSTRUCTION UNDER INTERVIEW PRESSURE
 *
 * Months later, after hundreds of other problems, recognition is not enough.
 *
 * The goal is to be able to rebuild the solution from:
 *
 *      NATURAL CHOICES
 *          ↓
 *      RECURSIVE STATE
 *          ↓
 *      REPEATED STATES
 *          ↓
 *      MEMOIZATION
 *          ↓
 *      BOTTOM-UP DEPENDENCIES
 *          ↓
 *      STATE → PREDECESSORS → COMBINE → BASE → ORDER → ANSWER
 *
 * Every section should help reconstruct, transfer, explain, or retain.
 */
public class UniquePaths {

    /*
     * =========================================================================
     * 1. PROBLEM STATEMENT
     * =========================================================================
     *
     * A robot starts at the top-left corner of an m x n grid.
     *
     * Start:
     *
     *      (0, 0)
     *
     * Destination:
     *
     *      (m - 1, n - 1)
     *
     * At each step the robot may move only:
     *
     *      RIGHT
     *      DOWN
     *
     * Return the number of DISTINCT valid paths from start to destination.
     *
     * -------------------------------------------------------------------------
     * Example 1
     * -------------------------------------------------------------------------
     *
     * m = 3, n = 2
     *
     *      S   .
     *      .   .
     *      .   E
     *
     * To reach E:
     *
     *      2 DOWN moves
     *      1 RIGHT move
     *
     * Valid paths:
     *
     *      R D D
     *      D R D
     *      D D R
     *
     * Answer = 3
     *
     * -------------------------------------------------------------------------
     * Example 2
     * -------------------------------------------------------------------------
     *
     * m = 3, n = 7
     *
     * Answer = 28
     *
     * Important:
     *
     * We are NOT asked to enumerate all 28 paths.
     * We only need their count.
     *
     * -------------------------------------------------------------------------
     * Example 3
     * -------------------------------------------------------------------------
     *
     * m = 1, n = 5
     *
     *      S → → → → E
     *
     * There is only one possible path.
     *
     * Answer = 1
     *
     * -------------------------------------------------------------------------
     * Constraints
     * -------------------------------------------------------------------------
     *
     *      1 <= m, n <= 100
     *
     * The final answer fits in a signed 32-bit integer.
     */

    /*
     * =========================================================================
     * 2. HOW THE BRAIN SHOULD THINK
     * =========================================================================
     *
     * Do NOT begin with:
     *
     *      "This is DP."
     *
     * Train the thought sequence that INVENTS the DP.
     *
     * -------------------------------------------------------------------------
     * THOUGHT 1 — WHAT IS THE OUTPUT?
     * -------------------------------------------------------------------------
     *
     * The problem asks:
     *
     *      "How many ways?"
     *
     * So this is a COUNTING problem.
     *
     * A useful signal:
     *
     *      when independent previous possibilities lead to the same state,
     *      counting usually combines them with SUM.
     *
     * -------------------------------------------------------------------------
     * THOUGHT 2 — WHAT CHANGES AS I MOVE?
     * -------------------------------------------------------------------------
     *
     * Only the robot's position:
     *
     *      row
     *      col
     *
     * So a natural state is:
     *
     *      solve(row, col)
     *
     * -------------------------------------------------------------------------
     * THOUGHT 3 — WHAT CHOICES EXIST FROM ONE CELL?
     * -------------------------------------------------------------------------
     *
     * From (row, col):
     *
     *      go DOWN
     *
     * or:
     *
     *      go RIGHT
     *
     * This naturally gives recursion.
     *
     * -------------------------------------------------------------------------
     * THOUGHT 4 — WHAT SHOULD solve(row, col) MEAN?
     * -------------------------------------------------------------------------
     *
     * Let:
     *
     *      solve(row, col)
     *
     * mean:
     *
     *      number of valid paths from (row,col) to destination
     *
     * Then:
     *
     *      solve(row, col)
     *      =
     *      solve(row + 1, col)
     *      +
     *      solve(row, col + 1)
     *
     * -------------------------------------------------------------------------
     * THOUGHT 5 — BASE CASES?
     * -------------------------------------------------------------------------
     *
     * Outside grid:
     *
     *      0 paths
     *
     * Destination reached:
     *
     *      1 completed path
     *
     * -------------------------------------------------------------------------
     * THOUGHT 6 — IS WORK REPEATED?
     * -------------------------------------------------------------------------
     *
     * Yes.
     *
     * Example:
     *
     *      (1,1)
     *
     * can be reached through:
     *
     *      Down → Right
     *
     * and:
     *
     *      Right → Down
     *
     * Once at (1,1), the remaining answer depends only on:
     *
     *      (1,1)
     *
     * not on how we arrived there.
     *
     * Therefore:
     *
     *      SAME STATE
     *      → SAME ANSWER
     *      → REPEATED WORK
     *      → MEMOIZE
     *
     * -------------------------------------------------------------------------
     * THOUGHT 7 — HOW DO I TURN THIS BOTTOM-UP?
     * -------------------------------------------------------------------------
     *
     * Flip the state viewpoint.
     *
     * Recursive viewpoint:
     *
     *      ways FROM current cell to destination
     *
     * Bottom-up viewpoint:
     *
     *      ways TO REACH current cell from start
     *
     * Define:
     *
     *      dp[row][col]
     *      =
     *      number of paths from start to (row,col)
     *
     * Now ask the most reusable question:
     *
     *      "WHERE COULD I HAVE COME FROM?"
     *
     *             TOP
     *              ↓
     *           [current] ← LEFT
     *
     * Only two legal predecessors exist.
     *
     * -------------------------------------------------------------------------
     * THOUGHT 8 — HOW DO I COMBINE THEM?
     * -------------------------------------------------------------------------
     *
     * Every path reaching the current cell arrives from:
     *
     *      TOP
     *
     * or:
     *
     *      LEFT
     *
     * The two path sets are disjoint.
     *
     * Since we are COUNTING:
     *
     *      current = top + left
     *
     * -------------------------------------------------------------------------
     * THOUGHT 9 — WHAT IS THE BASE?
     * -------------------------------------------------------------------------
     *
     * First row:
     *
     *      exactly one path to every cell:
     *      keep moving RIGHT
     *
     * First column:
     *
     *      exactly one path to every cell:
     *      keep moving DOWN
     *
     * Therefore initialize both borders to 1.
     *
     * -------------------------------------------------------------------------
     * THOUGHT 10 — WHAT ORDER?
     * -------------------------------------------------------------------------
     *
     * Current needs:
     *
     *      TOP
     *      LEFT
     *
     * Therefore process:
     *
     *      top-left → bottom-right
     *
     * -------------------------------------------------------------------------
     * FINAL RECONSTRUCTION
     * -------------------------------------------------------------------------
     *
     * STATE:
     *      dp[r][c] = ways to reach cell
     *
     * PREDECESSORS:
     *      top, left
     *
     * COMBINE:
     *      COUNT → SUM
     *
     * BASE:
     *      first row = 1
     *      first column = 1
     *
     * ORDER:
     *      top-left → bottom-right
     *
     * ANSWER:
     *      bottom-right
     */

    /*
     * =========================================================================
     * 3. RECURSION — NATURAL CHOICES
     * =========================================================================
     *
     * Why keep this?
     *
     * Because recursion exposes:
     *
     *      the natural choices
     *      the natural state
     *      the repeated subproblems
     *
     * Do not memorize it as the final interview solution.
     */

    static class Recursion {

        public int uniquePaths(int m, int n) {
            return solve(0, 0, m, n);
        }

        private int solve(int row, int col, int m, int n) {

            if (row >= m || col >= n) {
                return 0;
            }

            if (row == m - 1 && col == n - 1) {
                return 1;
            }

            int down = solve(row + 1, col, m, n);
            int right = solve(row, col + 1, m, n);

            return down + right;
        }
    }

    /*
     * -------------------------------------------------------------------------
     * RECURSION TREE — REPEATED STATE
     * -------------------------------------------------------------------------
     *
     *                          solve(0,0)
     *                         /          \
     *                    down              right
     *                     /                  \
     *              solve(1,0)              solve(0,1)
     *                /     \                 /      \
     *         solve(2,0)  solve(1,1)   solve(1,1)  solve(0,2)
     *                          ↑               ↑
     *                          └──── SAME ─────┘
     *
     * The same state solve(1,1) appears twice.
     *
     * Reusable signal:
     *
     *      same input state
     *      → same answer
     *      → repeated work
     *      → cache candidate
     *
     * Complexity:
     *
     *      Time  = exponential
     *      Space = O(m + n) recursion depth
     */

    /*
     * =========================================================================
     * 4. MEMOIZATION — RECURSION + CACHE
     * =========================================================================
     *
     * Keep the same recursive meaning:
     *
     *      solve(row, col)
     *      =
     *      ways from this cell to destination
     *
     * But solve every coordinate once.
     */

    static class Memoization {

        public int uniquePaths(int m, int n) {

            int[][] memo = new int[m][n];

            for (int[] row : memo) {
                Arrays.fill(row, -1);
            }

            return solve(0, 0, m, n, memo);
        }

        private int solve(
                int row,
                int col,
                int m,
                int n,
                int[][] memo
        ) {

            if (row >= m || col >= n) {
                return 0;
            }

            if (row == m - 1 && col == n - 1) {
                return 1;
            }

            if (memo[row][col] != -1) {
                return memo[row][col];
            }

            int down = solve(row + 1, col, m, n, memo);
            int right = solve(row, col + 1, m, n, memo);

            memo[row][col] = down + right;

            return memo[row][col];
        }
    }

    /*
     * -------------------------------------------------------------------------
     * REPEATED-STATE / CACHE-HIT VISUAL
     * -------------------------------------------------------------------------
     *
     * First time:
     *
     *      solve(1,1)
     *          ↓
     *      calculate
     *          ↓
     *      memo[1][1] = answer
     *
     * Later:
     *
     *      solve(1,1)
     *          ↓
     *      memo[1][1] already exists
     *          ↓
     *      CACHE HIT
     *          ↓
     *      return immediately
     *
     * Possible states:
     *
     *      m * n
     *
     * Each solved once:
     *
     *      Time  = O(m * n)
     *      Space = O(m * n)
     *
     * This is the recursion → DP bridge.
     */

    /*
     * =========================================================================
     * 5. BOTTOM-UP DP — PRIMARY INTERVIEW SOLUTION
     * =========================================================================
     *
     * For fixed rectangular traversal, nested FOR loops are clearer than WHILE.
     *
     * They visually express:
     *
     *      "process every cell in this rectangular range"
     *
     * The transition should also remain visible as ONE equation:
     *
     *      current = top + left
     *
     * That equation is the heart of the DP.
     */

    public int uniquePaths(int m, int n) {

        int[][] dp = new int[m][n];

        // Every first-column cell can be reached in exactly one way:
        // keep moving DOWN from the start 0,0
        for (int row = 0; row < m; row++) {
            dp[row][0] = 1;
        }

        // Every first-row cell can be reached in exactly one way:
        // keep moving RIGHT from the start 0,0
        for (int col = 0; col < n; col++) {
            dp[0][col] = 1;
        }

        for (int row = 1; row < m; row++) {

            for (int col = 1; col < n; col++) {

                dp[row][col] =
                        dp[row - 1][col]
                                + dp[row][col - 1];
            }
        }

        return dp[m - 1][n - 1];
    }

    /*
     * =========================================================================
     * 6. FULL DP TABLE — 3 x 3
     * =========================================================================
     *
     * After border initialization:
     *
     *      +---+---+---+
     *      | 1 | 1 | 1 |
     *      +---+---+---+
     *      | 1 | 0 | 0 |
     *      +---+---+---+
     *      | 1 | 0 | 0 |
     *      +---+---+---+
     *
     * -------------------------------------------------------------------------
     * Compute row = 1
     * -------------------------------------------------------------------------
     *
     * dp[1][1]
     * =
     * dp[0][1] + dp[1][0]
     * =
     * 1 + 1
     * =
     * 2
     *
     * dp[1][2]
     * =
     * dp[0][2] + dp[1][1]
     * =
     * 1 + 2
     * =
     * 3
     *
     *      +---+---+---+
     *      | 1 | 1 | 1 |
     *      +---+---+---+
     *      | 1 | 2 | 3 |
     *      +---+---+---+
     *      | 1 | 0 | 0 |
     *      +---+---+---+
     *
     * -------------------------------------------------------------------------
     * Compute row = 2
     * -------------------------------------------------------------------------
     *
     * dp[2][1]
     * =
     * dp[1][1] + dp[2][0]
     * =
     * 2 + 1
     * =
     * 3
     *
     * dp[2][2]
     * =
     * dp[1][2] + dp[2][1]
     * =
     * 3 + 3
     * =
     * 6
     *
     *      +---+---+---+
     *      | 1 | 1 | 1 |
     *      +---+---+---+
     *      | 1 | 2 | 3 |
     *      +---+---+---+
     *      | 1 | 3 | 6 |
     *      +---+---+---+
     *
     * Answer = 6
     */

    /*
     * =========================================================================
     * 7. FOCUSED INNER-LOOP TRACE
     * =========================================================================
     *
     * We are at:
     *
     *      row = 2
     *      col = 2
     *
     * Current table:
     *
     *      +---+---+---+
     *      | 1 | 1 | 1 |
     *      +---+---+---+
     *      | 1 | 2 | 3 |
     *      +---+---+---+
     *      | 1 | 3 | 0 |
     *      +---+---+---+
     *
     * Exact line:
     *
     *      dp[row][col]
     *      =
     *      dp[row - 1][col]
     *      +
     *      dp[row][col - 1]
     *
     * Substitute:
     *
     *      dp[2][2]
     *      =
     *      dp[1][2]
     *      +
     *      dp[2][1]
     *
     *      =
     *      3 + 3
     *
     *      =
     *      6
     *
     * Full table:
     *
     *      teaches global state evolution.
     *
     * Focused trace:
     *
     *      teaches exact code execution under pressure.
     */

    /*
     * =========================================================================
     * 8. INVARIANT + CORRECTNESS PROOF
     * =========================================================================
     *
     * Invariant:
     *
     *      when dp[row][col] is computed,
     *      it equals the number of valid paths from start to that cell.
     *
     * Why?
     *
     * Every path reaching (row,col) must make its final move from exactly one:
     *
     *      TOP
     *      LEFT
     *
     * No other move is legal.
     *
     * The two path sets are disjoint because their final moves differ.
     *
     * By row-major order:
     *
     *      dp[row - 1][col] is already correct
     *      dp[row][col - 1] is already correct
     *
     * Therefore:
     *
     *      dp[row][col]
     *      =
     *      top + left
     *
     * counts every valid path exactly once.
     *
     * The first row and first column are correctly initialized to 1.
     *
     * Therefore, by induction over the table,
     * the destination value is correct.
     *
     * Complexity:
     *
     *      Time  = O(m * n)
     *      Space = O(m * n)
     */

    /*
     * =========================================================================
     * 9. IMPLEMENTATION BLUEPRINT
     * =========================================================================
     *
     * Generic:
     *
     *      define state
     *      initialize base states
     *      choose dependency-safe order
     *      iterate states
     *      combine predecessor answers
     *      return target
     *
     * Unique Paths:
     *
     *      create dp[m][n]
     *      first row = 1
     *      first column = 1
     *
     *      for every interior cell:
     *
     *          current = top + left
     *
     *      return bottom-right
     *
     * Ultra-compact pseudocode:
     *
     *      initialize borders
     *
     *      for each remaining cell:
     *          dp[cell] = top + left
     *
     *      return destination
     */

    /*
     * =========================================================================
     * 10. HIGH-ROI FOLLOW-UP — 2D → 1D SPACE COMPRESSION
     * =========================================================================
     *
     * Do NOT memorize the 1D formula independently.
     *
     * Start from the 2D equation:
     *
     *      dp[row][col]
     *      =
     *      dp[row - 1][col]
     *      +
     *      dp[row][col - 1]
     *
     * When processing row by row, we only need:
     *
     *      TOP
     *      LEFT
     *
     * So keep one array indexed by COLUMN.
     *
     * Why column?
     *
     * Because the outer loop walks rows.
     *
     * The 1D array represents one horizontal row of DP values.
     *
     * -------------------------------------------------------------------------
     * BEFORE / AFTER MEANING
     * -------------------------------------------------------------------------
     *
     * Before updating dp[col]:
     *
     *      dp[col]
     *      =
     *      previous row, same column
     *      =
     *      TOP
     *
     * Since col - 1 has already been processed in the current row:
     *
     *      dp[col - 1]
     *      =
     *      current row, previous column
     *      =
     *      LEFT
     *
     * Therefore the SAME recurrence becomes:
     *
     *      dp[col]
     *      =
     *      dp[col]
     *      +
     *      dp[col - 1]
     *
     * -------------------------------------------------------------------------
     * VISUAL EQUIVALENCE
     * -------------------------------------------------------------------------
     *
     * 2D:
     *
     *      dp[row][col]
     *      =
     *      dp[row - 1][col]      // TOP
     *      +
     *      dp[row][col - 1]      // LEFT
     *
     * 1D:
     *
     *      dp[col]
     *      =
     *      dp[col]               // TOP before update
     *      +
     *      dp[col - 1]           // LEFT after update
     *
     * -------------------------------------------------------------------------
     * 1D DRY RUN
     * -------------------------------------------------------------------------
     *
     * Start:
     *
     *      [1, 1, 1]
     *
     * Processing second row:
     *
     * col = 1
     *
     *      [1, 1, 1]
     *          ↑
     *
     *      dp[1] = 1 + 1 = 2
     *
     *      [1, 2, 1]
     *
     * col = 2
     *
     *      dp[2] = 1 + 2 = 3
     *
     *      [1, 2, 3]
     *
     * Processing third row:
     *
     * col = 1
     *
     *      dp[1] = 2 + 1 = 3
     *
     *      [1, 3, 3]
     *
     * col = 2
     *
     *      dp[2] = 3 + 3 = 6
     *
     *      [1, 3, 6]
     *
     * Complexity:
     *
     *      Time  = O(m * n)
     *      Space = O(n)
     *
     * Further optimization:
     *
     * Since the grid is symmetric, the smaller dimension can be used
     * as the array length for O(min(m,n)) space.
     *
     * But O(n) is usually the cleaner interview explanation.
     */

    static class SpaceOptimized {

        public int uniquePaths(int m, int n) {

            int[] dp = new int[n];

            Arrays.fill(dp, 1);

            for (int row = 1; row < m; row++) {

                for (int col = 1; col < n; col++) {

                    dp[col] =
                            dp[col]
                                    + dp[col - 1];
                }
            }

            return dp[n - 1];
        }
    }

    /*
     * =========================================================================
     * 11. HORIZONTAL MASTERY — ONE LEARNED PATTERN, MANY PAYOFFS
     * =========================================================================
     *
     * The reusable family is:
     *
     *      CURRENT STATE
     *           ↑
     *      combine answers
     *           ↑
     *      VALID PREDECESSORS
     *
     * -------------------------------------------------------------------------
     * PROBLEM                  STATE                    PREDECESSORS    COMBINE
     * -------------------------------------------------------------------------
     *
     * Unique Paths            ways to reach cell       top, left       SUM
     *
     * Unique Paths II         valid ways to reach      top, left       SUM
     *                         cell; obstacle = 0
     *
     * Minimum Path Sum        min cost to reach         top, left       MIN
     *                         cell
     *
     * Maximum Path Value      max value to reach        top, left       MAX
     *                         cell
     *
     * Min Falling Path Sum    min cost to reach         up-left,        MIN
     *                         cell                      up,
     *                                                   up-right
     *
     * Triangle                best cost to reach        two parents     MIN
     *                         position
     *
     * -------------------------------------------------------------------------
     * WHAT SHOULD YOUR BRAIN NOTICE?
     * -------------------------------------------------------------------------
     *
     * Unique Paths II:
     *
     *      same geometry
     *      same SUM
     *      add invalid-state rule
     *
     * Minimum Path Sum:
     *
     *      same geometry
     *      change state meaning
     *      change SUM → MIN
     *
     * Falling Path:
     *
     *      same grammar
     *      change predecessor set
     *
     * Triangle:
     *
     *      same parent-combination idea
     *      different shape
     *
     * That is horizontal mastery.
     *
     * Do not store six isolated solutions.
     * Store one reconstruction grammar.
     */

    /*
     * =========================================================================
     * 12. HIGH-ROI VARIATION DERIVATIONS
     * =========================================================================
     *
     * The previous section is the FAST comparison layer.
     *
     * This section trains how to MUTATE the base pattern.
     *
     * -------------------------------------------------------------------------
     * UNIQUE PATHS II
     * -------------------------------------------------------------------------
     *
     * Ask the same questions.
     *
     * STATE:
     *
     *      valid paths reaching this cell
     *
     * PREDECESSORS:
     *
     *      top, left
     *
     * COMBINE:
     *
     *      SUM
     *
     * NEW RULE:
     *
     *      obstacle cannot be entered
     *
     * Therefore:
     *
     *      if blocked:
     *
     *          dp[r][c] = 0
     *
     *      else:
     *
     *          dp[r][c] = top + left
     *
     * WHAT SURVIVED?
     *
     *      state shape
     *      predecessors
     *      SUM
     *      traversal order
     *
     * WHAT CHANGED?
     *
     *      validity rule
     *
     * -------------------------------------------------------------------------
     * MINIMUM PATH SUM
     * -------------------------------------------------------------------------
     *
     * STATE:
     *
     *      minimum cost to reach this cell
     *
     * PREDECESSORS:
     *
     *      top, left
     *
     * Problem asks for MINIMUM.
     *
     * Therefore:
     *
     *      dp[r][c]
     *      =
     *      grid[r][c]
     *      +
     *      min(top, left)
     *
     * WHAT SURVIVED?
     *
     *      grid state
     *      predecessor geometry
     *      traversal order
     *
     * WHAT CHANGED?
     *
     *      state meaning
     *      SUM → MIN
     *      add current cell cost
     *
     * -------------------------------------------------------------------------
     * MAXIMUM PATH VALUE
     * -------------------------------------------------------------------------
     *
     * STATE:
     *
     *      maximum value reaching this cell
     *
     * PREDECESSORS:
     *
     *      top, left
     *
     * COMBINE:
     *
     *      MAX
     *
     * Recurrence:
     *
     *      dp[r][c]
     *      =
     *      value[r][c]
     *      +
     *      max(top, left)
     *
     * Again:
     *
     *      same predecessor skeleton
     *      different objective
     *
     * -------------------------------------------------------------------------
     * MINIMUM FALLING PATH SUM
     * -------------------------------------------------------------------------
     *
     * STATE:
     *
     *      minimum cost reaching this cell
     *
     * PREDECESSORS:
     *
     *      upper-left
     *      up
     *      upper-right
     *
     * COMBINE:
     *
     *      MIN
     *
     * WHAT SURVIVED?
     *
     *      predecessor-combination DP grammar
     *
     * WHAT CHANGED?
     *
     *      predecessor set
     *
     * -------------------------------------------------------------------------
     * TRIANGLE
     * -------------------------------------------------------------------------
     *
     * STATE:
     *
     *      minimum/best cost reaching this triangle position
     *
     * PREDECESSORS:
     *
     *      the two valid parents above
     *
     * COMBINE:
     *
     *      MIN
     *
     * Different shape.
     *
     * Same reconstruction method:
     *
     *      What does my state mean?
     *      Where can I come from?
     *      How should I combine those parents?
     */

    /*
     * =========================================================================
     * 13. GENERIC DP RECONSTRUCTION GRAMMAR
     * =========================================================================
     *
     * When a random problem feels like DP:
     *
     * 1. STATE
     *
     *      What exactly does dp[...] mean?
     *
     * 2. PREDECESSORS
     *
     *      Which already-solved states can produce this state?
     *
     * 3. COMBINE
     *
     *      COUNT       → SUM
     *      MINIMUM     → MIN
     *      MAXIMUM     → MAX
     *      POSSIBLE?   → OR
     *
     * 4. BASE
     *
     *      What smallest state is directly known?
     *
     * 5. ORDER
     *
     *      In what order are dependencies ready first?
     *
     * 6. ANSWER
     *
     *      Which state represents what the problem asks?
     *
     * If you do not yet know the DP:
     *
     *      write natural recursion
     *      → identify state
     *      → notice repeated states
     *      → memoize
     *      → derive dependency order
     *      → tabulate
     */

    /*
     * =========================================================================
     * 14. COMMON FAILURE MODES
     * =========================================================================
     *
     * FAILURE 1
     * ---------
     * Writing recurrence before defining state.
     *
     * Repair:
     *
     *      say:
     *
     *      "dp[row][col] means ______."
     *
     * -------------------------------------------------------------------------
     * FAILURE 2
     * -------------------------------------------------------------------------
     * Forgetting the recurrence.
     *
     * Repair:
     *
     *      draw ONE current cell
     *
     *      ask:
     *
     *      "Where could I have come from?"
     *
     * -------------------------------------------------------------------------
     * FAILURE 3
     * -------------------------------------------------------------------------
     * Forgetting why border cells are 1.
     *
     * Repair:
     *
     * First row:
     *
     *      only RIGHT is possible
     *
     * First column:
     *
     *      only DOWN is possible
     *
     * Therefore exactly one path.
     *
     * -------------------------------------------------------------------------
     * FAILURE 4
     * -------------------------------------------------------------------------
     * Wrong combine operator.
     *
     * Repair:
     *
     *      COUNT → SUM
     *      MIN   → MIN
     *      MAX   → MAX
     *
     * -------------------------------------------------------------------------
     * FAILURE 5
     * -------------------------------------------------------------------------
     * Wrong traversal direction.
     *
     * Repair:
     *
     *      dependencies determine order
     *
     * Need top + left?
     *
     *      process top-left → bottom-right
     *
     * -------------------------------------------------------------------------
     * FAILURE 6
     * -------------------------------------------------------------------------
     * Memorizing 1D compression mechanically.
     *
     * Repair:
     *
     *      dp[col]     = TOP before update
     *      dp[col - 1] = LEFT after update
     */

    /*
     * =========================================================================
     * 15. INTERVIEW ARTICULATION
     * =========================================================================
     *
     * "I'll use grid DP.
     *
     * Let dp[row][col] represent the number of valid paths from the start
     * to that cell.
     *
     * Because movement is only right or down, every path reaching a cell
     * must come from either the cell above or the cell to the left.
     *
     * Those path sets are disjoint, so I add their counts.
     *
     * The first row and first column each contain one path because there is
     * only one possible direction along either border.
     *
     * I then process the remaining cells from top-left to bottom-right,
     * using:
     *
     *      dp[row][col]
     *      =
     *      dp[row - 1][col]
     *      +
     *      dp[row][col - 1].
     *
     * The answer is dp[m - 1][n - 1].
     *
     * Time is O(mn) and space is O(mn).
     *
     * If needed, I can reduce the space to O(n) because each row only needs
     * the value above and the already-updated value to the left."
     */

    /*
     * =========================================================================
     * 16. PRESSURE RECALL CARD
     * =========================================================================
     *
     * UNIQUE PATHS
     *
     * Trigger:
     *
     *      count ways through directed grid
     *
     * Natural recursion:
     *
     *      down + right
     *
     * Repeated state:
     *
     *      same (row,col)
     *
     * Bottom-up state:
     *
     *      dp[r][c] = ways to reach cell
     *
     * Recovery question:
     *
     *      "Where could I have come from?"
     *
     * Parents:
     *
     *      TOP
     *      LEFT
     *
     * Combine:
     *
     *      COUNT → SUM
     *
     * Base:
     *
     *      first row = 1
     *      first column = 1
     *
     * Order:
     *
     *      top-left → bottom-right
     *
     * Transition:
     *
     *      current = top + left
     *
     * 1D follow-up:
     *
     *      dp[col]     = TOP before update
     *      dp[col - 1] = LEFT after update
     *
     * One-liner:
     *
     *      Every path enters a cell from top or left.
     */

    /*
     * =========================================================================
     * 17. REINFORCEMENT MAP
     * =========================================================================
     *
     * Solve in this order:
     *
     *      1. Unique Paths
     *
     *         Learn:
     *         predecessor accumulation
     *
     *      2. Unique Paths II
     *
     *         Learn:
     *         same DP + invalid states
     *
     *      3. Minimum Path Sum
     *
     *         Learn:
     *         same geometry, SUM → MIN
     *
     *      4. Minimum Falling Path Sum
     *
     *         Learn:
     *         change predecessor set
     *
     *      5. Triangle
     *
     *         Learn:
     *         same parent-combination grammar in another shape
     *
     * Goal:
     *
     *      not five memorized solutions
     *
     * but:
     *
     *      one reconstructible predecessor-combination DP family
     */

    /*
     * =========================================================================
     * 18. MASTERY CHECK
     * =========================================================================
     *
     * Close this file.
     *
     * Can you reconstruct:
     *
     *      1. natural recursive choices?
     *
     *      2. recursive state meaning?
     *
     *      3. repeated state visual?
     *
     *      4. why memoization becomes O(mn)?
     *
     *      5. bottom-up state meaning?
     *
     *      6. top / left predecessors?
     *
     *      7. why COUNT means SUM?
     *
     *      8. why borders are 1?
     *
     *      9. why nested loops start at 1?
     *
     *     10. why top-left → bottom-right?
     *
     *     11. obstacle variation?
     *
     *     12. minimum-cost variation?
     *
     *     13. why 1D dp[col] represents TOP before update?
     *
     * If yes:
     *
     *      you can reconstruct the family.
     */

    /*
     * =========================================================================
     * 19. TESTS
     * =========================================================================
     */

    public static void main(String[] args) {

        Recursion recursion = new Recursion();
        Memoization memoization = new Memoization();

        UniquePaths bottomUp = new UniquePaths();
        SpaceOptimized optimized = new SpaceOptimized();

        // Canonical example.
        assert recursion.uniquePaths(3, 2) == 3;
        assert memoization.uniquePaths(3, 2) == 3;
        assert bottomUp.uniquePaths(3, 2) == 3;
        assert optimized.uniquePaths(3, 2) == 3;

        // Larger example.
        assert memoization.uniquePaths(3, 7) == 28;
        assert bottomUp.uniquePaths(3, 7) == 28;
        assert optimized.uniquePaths(3, 7) == 28;

        // Visual dry-run example.
        assert bottomUp.uniquePaths(3, 3) == 6;
        assert optimized.uniquePaths(3, 3) == 6;

        // Single cell.
        assert bottomUp.uniquePaths(1, 1) == 1;
        assert optimized.uniquePaths(1, 1) == 1;

        // Single row.
        assert bottomUp.uniquePaths(1, 5) == 1;
        assert optimized.uniquePaths(1, 5) == 1;

        // Single column.
        assert bottomUp.uniquePaths(5, 1) == 1;
        assert optimized.uniquePaths(5, 1) == 1;

        // Symmetry.
        assert bottomUp.uniquePaths(5, 8)
                == bottomUp.uniquePaths(8, 5);

        // Cross-check implementations.
        int expected = bottomUp.uniquePaths(10, 10);

        assert memoization.uniquePaths(10, 10) == expected;
        assert optimized.uniquePaths(10, 10) == expected;

        System.out.println("All assertions passed.");
    }
}

/*
 * ============================================================================
 * FINAL RETENTION RULE
 * ============================================================================
 *
 * Months later, do NOT ask:
 *
 *      "What was the Unique Paths code?"
 *
 * Rebuild:
 *
 *      NATURAL CHOICES
 *          ↓
 *      STATE
 *          ↓
 *      REPEATED STATES
 *          ↓
 *      MEMOIZATION
 *          ↓
 *      PREDECESSORS
 *          ↓
 *      COMBINE
 *          ↓
 *      BASE
 *          ↓
 *      ORDER
 *          ↓
 *      ANSWER
 *
 * And across variations ask:
 *
 *      WHAT SURVIVES?
 *      WHAT CHANGES?
 *
 * Then write the code.
 */
