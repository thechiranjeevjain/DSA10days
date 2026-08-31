package org.chijai.day11.backtracking.session1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ============================================================
 * N-QUEENS — LEAN INTERVIEW STUDY FILE
 * ============================================================
 *
 * Primary:
 *      LeetCode 51 — N-Queens
 *
 * Pattern:
 *      Constraint Backtracking
 *
 * Master idea:
 *      One recursion level = one row.
 *      Choose one safe column.
 *      Recurse to the next row.
 *      Undo.
 */
public class NQueens {

    /*
     * ============================================================
     * 1. PRIMARY INTERVIEW SOLUTION
     * ============================================================
     *
     * Invariant:
     *      Rows [0, row) each contain exactly one queen.
     *      No two placed queens share a column or diagonal.
     *
     * Why no row set?
     *      The recursion itself places exactly one queen per row.
     */
    static final class Solution {

        List<List<String>> solveNQueens(int n) {

            List<List<String>> answer = new ArrayList<>();

            char[][] board = new char[n][n];

            for (char[] row : board) {
                java.util.Arrays.fill(row, '.');
            }

            backtrack(
                    0,
                    board,
                    new HashSet<>(),
                    new HashSet<>(),
                    new HashSet<>(),
                    answer
            );

            return answer;
        }

        private void backtrack(
                int row,
                char[][] board,
                Set<Integer> columns,
                Set<Integer> positiveDiagonals,
                Set<Integer> negativeDiagonals,
                List<List<String>> answer
        ) {

            if (row == board.length) {
                answer.add(snapshot(board));
                return;
            }

            for (int col = 0; col < board.length; col++) {

                int positiveDiagonal = row + col;
                int negativeDiagonal = row - col;

                if (columns.contains(col)
                        || positiveDiagonals.contains(positiveDiagonal)
                        || negativeDiagonals.contains(negativeDiagonal)) {
                    continue;
                }

                board[row][col] = 'Q';
                columns.add(col);
                positiveDiagonals.add(positiveDiagonal);
                negativeDiagonals.add(negativeDiagonal);

                backtrack(
                        row + 1,
                        board,
                        columns,
                        positiveDiagonals,
                        negativeDiagonals,
                        answer
                );

                board[row][col] = '.';
                columns.remove(col);
                positiveDiagonals.remove(positiveDiagonal);
                negativeDiagonals.remove(negativeDiagonal);
            }
        }

        private List<String> snapshot(char[][] board) {

            List<String> configuration = new ArrayList<>(board.length);

            for (char[] row : board) {
                configuration.add(new String(row));
            }

            return configuration;
        }
    }

    /*
     * ============================================================
     * 2. WHY? — FOLLOW THE CODE TOP TO BOTTOM
     * ============================================================
     *
     * WHY 1 — Why recursion by row?
     * ------------------------------------------------------------
     * Every valid solution contains exactly one queen in every row.
     *
     * So row is not a constraint we need to check.
     * It becomes the recursion depth:
     *
     *      backtrack(row)
     *
     * means:
     *
     *      "Place the queen for this row."
     *
     *
     * WHY 2 — Why scan every column?
     * ------------------------------------------------------------
     * For the current row, the only remaining decision is:
     *
     *      which column receives the queen?
     *
     *
     * WHY 3 — Why track columns?
     * ------------------------------------------------------------
     * Two queens cannot share a column.
     *
     *      columns.contains(col)
     *
     * gives O(1)-average rejection.
     *
     *
     * WHY 4 — Why row + col?
     * ------------------------------------------------------------
     * Cells on the same "/" diagonal have the same:
     *
     *      row + col
     *
     * Example:
     *
     *      (0,3) → 3
     *      (1,2) → 3
     *      (2,1) → 3
     *      (3,0) → 3
     *
     *
     * WHY 5 — Why row - col?
     * ------------------------------------------------------------
     * Cells on the same "\" diagonal have the same:
     *
     *      row - col
     *
     * Example:
     *
     *      (0,0) → 0
     *      (1,1) → 0
     *      (2,2) → 0
     *
     * Negative values are fine in a Set<Integer>.
     *
     *
     * WHY 6 — Why place + add constraints BEFORE recursion?
     * ------------------------------------------------------------
     * The child row must see this queen as occupying:
     *
     *      its column
     *      its "/" diagonal
     *      its "\" diagonal
     *
     *
     * WHY 7 — Why undo all four pieces of state?
     * ------------------------------------------------------------
     * A sibling column choice must start from the exact parent state.
     *
     * Undo:
     *
     *      board[row][col] = '.'
     *      columns.remove(col)
     *      positiveDiagonals.remove(row + col)
     *      negativeDiagonals.remove(row - col)
     *
     *
     * WHY 8 — Why save when row == n?
     * ------------------------------------------------------------
     * Rows 0 through n-1 have each received one non-conflicting queen.
     *
     * Therefore one complete solution exists.
     */

    /*
     * ============================================================
     * 3. 30-SECOND RECALL CARD
     * ============================================================
     *
     * TRIGGER
     *      Place n queens so none attack each other.
     *
     * PATTERN
     *      Constraint backtracking.
     *
     * LEVEL
     *      one row.
     *
     * DECISION
     *      choose a column.
     *
     * CONSTRAINTS
     *      col
     *      row + col
     *      row - col
     *
     * MOVE
     *      place → mark 3 constraints → recurse → undo all.
     *
     * BASE
     *      row == n → snapshot board.
     *
     * ONE-LINER
     *      One queen per row; reject occupied column or diagonal.
     */

    /*
     * ============================================================
     * 4. REUSABLE CONSTRAINT-BACKTRACKING TEMPLATE
     * ============================================================
     *
     * void dfs(level) {
     *
     *     if (level == totalLevels) {
     *         saveSolution();
     *         return;
     *     }
     *
     *     for (candidate : candidatesAt(level)) {
     *
     *         if (conflicts(candidate)) {
     *             continue;
     *         }
     *
     *         choose(candidate);
     *         markConstraints(candidate);
     *
     *         dfs(level + 1);
     *
     *         unmarkConstraints(candidate);
     *         undo(candidate);
     *     }
     * }
     *
     * N-Queens specialization:
     *
     *      level       = row
     *      candidate   = column
     *      constraints = col, row+col, row-col
     */

    /*
     * ============================================================
     * 5. VISUALIZE THE RECURSION TREE
     * ============================================================
     *
     * n = 4
     *
     *                       row 0
     *              /      /      \      \
     *            c0      c1      c2      c3
     *            |       |       |       |
     *          row 1   row 1   row 1   row 1
     *           ...      |
     *                  choose c3
     *                    |
     *                  row 2
     *                    |
     *                  choose c0
     *                    |
     *                  row 3
     *                    |
     *                  choose c2
     *                    |
     *                 SOLUTION
     *
     * One known n=4 path:
     *
     *      row 0 → col 1
     *      row 1 → col 3
     *      row 2 → col 0
     *      row 3 → col 2
     *
     * Board:
     *
     *      .Q..
     *      ...Q
     *      Q...
     *      ..Q.
     *
     * At every row, conflicting columns are pruned BEFORE recursion.
     */

    /*
     * ============================================================
     * 6. DIAGONALS — THE INTUITION TO RETAIN
     * ============================================================
     *
     * Grid coordinates:
     *
     *      (row, col)
     *
     * "/" diagonal:
     *
     *      moving down-left:
     *          row increases by 1
     *          col decreases by 1
     *
     *      sum stays constant:
     *
     *          row + col
     *
     *
     * "\" diagonal:
     *
     *      moving down-right:
     *          row increases by 1
     *          col increases by 1
     *
     *      difference stays constant:
     *
     *          row - col
     *
     * Permanent recall:
     *
     *      /  → sum
     *      \  → difference
     */

    /*
     * ============================================================
     * 7. HANDS-ON DRY RUN — n = 4
     * ============================================================
     *
     * Start:
     *
     *      row = 0
     *      columns = {}
     *      +diag   = {}
     *      -diag   = {}
     *
     * Try row 0, col 0.
     *
     *      columns = {0}
     *      +diag   = {0}
     *      -diag   = {0}
     *
     * Row 1:
     *
     *      col 0 → same column      ❌
     *      col 1 → row-col = 0      ❌
     *      col 2 → allowed          ✅
     *
     * Explore row 1, col 2.
     *
     * If a deeper row eventually has no legal column:
     *
     *      return to row 1
     *      remove queen at (1,2)
     *      remove col 2
     *      remove +diag 3
     *      remove -diag -1
     *      try next column
     *
     * Eventually one successful branch is:
     *
     *      (0,1)
     *          ↓
     *      (1,3)
     *          ↓
     *      (2,0)
     *          ↓
     *      (3,2)
     *
     * Constraints for these queens:
     *
     *      columns:
     *          1, 3, 0, 2
     *
     *      row + col:
     *          1, 4, 2, 5
     *
     *      row - col:
     *         -1,-2, 2, 1
     *
     * All unique → no attacks.
     */

    /*
     * ============================================================
     * 8. APPROACH PROGRESSION / TRADE-OFFS
     * ============================================================
     *
     * A. Scan the board to test safety
     * ------------------------------------------------------------
     * For every candidate queen, scan:
     *
     *      column above
     *      upper-left diagonal
     *      upper-right diagonal
     *
     * Easy to invent, but each validity check costs O(n).
     *
     *
     * B. HashSet constraints  ← PREFERRED INTERVIEW VERSION
     * ------------------------------------------------------------
     * Track:
     *
     *      columns
     *      row + col
     *      row - col
     *
     * Validity check becomes O(1)-average.
     *
     * Clear and highly explainable.
     *
     *
     * C. boolean[] constraints
     * ------------------------------------------------------------
     * Since ranges are bounded, sets can become arrays.
     *
     * columns:
     *      size n
     *
     * row + col:
     *      range 0 .. 2n-2
     *
     * row - col:
     *      range -(n-1) .. +(n-1)
     *
     * Offset negative diagonal by n-1:
     *
     *      index = row - col + (n - 1)
     *
     * Faster constants, slightly more index arithmetic.
     *
     *
     * D. Bitmask
     * ------------------------------------------------------------
     * Compact and fast.
     *
     * Excellent follow-up after the clear set-based solution.
     * Worse as a first explanation unless specifically requested.
     */

    /*
     * ============================================================
     * 9. COMPLEXITY
     * ============================================================
     *
     * Search is bounded roughly by permutations of columns:
     *
     *      O(n!)
     *
     * because each row can use a column at most once,
     * with diagonal pruning reducing the practical tree.
     *
     * Creating each returned board costs:
     *
     *      O(n²)
     *
     * Auxiliary recursion/state space:
     *
     *      O(n)
     *
     * excluding the explicit n×n board and output.
     */

    /*
     * ============================================================
     * 10. COMMON FAILURE MODES
     * ============================================================
     *
     * 1. Track rows unnecessarily
     *
     * Recursion already guarantees one queen per row.
     *
     *
     * 2. Check only columns
     *
     * Queens also attack diagonally.
     *
     *
     * 3. Confuse diagonal formulas
     *
     *      / → row + col
     *      \ → row - col
     *
     *
     * 4. Forget to remove a constraint during undo
     *
     * Later sibling branches falsely think the line is occupied.
     *
     *
     * 5. Stop after the first solution
     *
     * LeetCode 51 asks for ALL configurations.
     *
     * Unlike Sudoku Solver, do not return boolean on first success.
     *
     *
     * 6. Store the mutable board directly
     *
     * Snapshot each solved board into new String rows.
     */

    /*
     * ============================================================
     * 11. FOLLOW-UP VARIATIONS
     * ============================================================
     *
     * COUNT ONLY
     * ------------------------------------------------------------
     * If asked only for number of solutions:
     *
     *      do not build boards
     *      return / accumulate an integer count
     *
     * This is the N-Queens II variation.
     *
     *
     * RETURN ONE SOLUTION
     * ------------------------------------------------------------
     * Change recursion to boolean and stop at first completed board,
     * exactly like Sudoku's early-success pattern.
     *
     *
     * PERFORMANCE
     * ------------------------------------------------------------
     * Progression:
     *
     *      board scans
     *          →
     *      HashSet constraints
     *          →
     *      boolean arrays
     *          →
     *      bitmasks
     */

    /*
     * ============================================================
     * 12. INTERVIEW ARTICULATION
     * ============================================================
     *
     * "I recurse row by row, so the row constraint is satisfied
     * automatically. For each row I try every column and reject a
     * position if its column, row+col diagonal, or row-col diagonal
     * is already occupied. I mark those three constraints, recurse
     * to the next row, then undo them. When row reaches n, I snapshot
     * one complete configuration."
     */

    /*
     * ============================================================
     * 13. FINAL RETENTION CARD
     * ============================================================
     *
     * N-QUEENS
     *      one recursion level = one row
     *
     * CHOOSE
     *      column
     *
     * BLOCK
     *      col
     *      row + col
     *      row - col
     *
     * MOVE
     *      place → mark → recurse → unmark → remove
     *
     * BASE
     *      row == n → snapshot
     *
     * DIAGONALS
     *      / → sum
     *      \ → difference
     *
     * ONE-LINER
     *      Row is recursion; column and diagonals are constraints.
     */

    /*
     * ============================================================
     * 14. SELF-VERIFYING TESTS
     * ============================================================
     *
     * Run with assertions enabled:
     *      -ea
     */
    public static void main(String[] args) {

        Solution solution = new Solution();

        List<List<String>> n1 = solution.solveNQueens(1);

        assert n1.size() == 1;
        assert n1.get(0).equals(List.of("Q"));

        List<List<String>> n4 = solution.solveNQueens(4);

        assert n4.size() == 2 : "n=4 must have exactly two solutions.";

        List<String> expectedA = List.of(
                ".Q..",
                "...Q",
                "Q...",
                "..Q."
        );

        List<String> expectedB = List.of(
                "..Q.",
                "Q...",
                "...Q",
                ".Q.."
        );

        assert n4.contains(expectedA);
        assert n4.contains(expectedB);

        for (List<String> board : n4) {
            assert isValid(board);
        }

        assert solution.solveNQueens(2).isEmpty();
        assert solution.solveNQueens(3).isEmpty();

        System.out.println("All NQueens assertions passed.");
    }

    private static boolean isValid(List<String> board) {

        int n = board.size();

        Set<Integer> columns = new HashSet<>();
        Set<Integer> positiveDiagonals = new HashSet<>();
        Set<Integer> negativeDiagonals = new HashSet<>();

        for (int row = 0; row < n; row++) {

            int queensInRow = 0;

            for (int col = 0; col < n; col++) {

                if (board.get(row).charAt(col) != 'Q') {
                    continue;
                }

                queensInRow++;

                if (!columns.add(col)) {
                    return false;
                }

                if (!positiveDiagonals.add(row + col)) {
                    return false;
                }

                if (!negativeDiagonals.add(row - col)) {
                    return false;
                }
            }

            if (queensInRow != 1) {
                return false;
            }
        }

        return columns.size() == n;
    }
}
