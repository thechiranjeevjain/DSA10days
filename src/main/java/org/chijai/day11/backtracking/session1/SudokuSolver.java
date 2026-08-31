package org.chijai.day11.backtracking.session1;

import java.util.Arrays;

/**
 * ============================================================
 * SUDOKU SOLVER — LEAN INTERVIEW STUDY FILE
 * ============================================================
 *
 * Primary:
 *      LeetCode 37 — Sudoku Solver
 *
 * Pattern:
 *      Constraint Backtracking
 *
 * Master idea:
 *      Find one unresolved position.
 *      Try every legal candidate.
 *      Recurse.
 *      Undo if the choice fails.
 */
public class SudokuSolver {

    /*
     * ============================================================
     * 1. PRIMARY INTERVIEW SOLUTION
     * ============================================================
     *
     * Invariant:
     *      Every filled cell in board is currently valid.
     *
     * Each recursive call solves the first empty cell it finds.
     * Returning true means:
     *      "From this board state, a complete solution exists."
     */
    static final class Solution {

        void solveSudoku(char[][] board) {
            solve(board);
        }

        private boolean solve(char[][] board) {

            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {

                    if (board[row][col] != '.') {
                        continue;
                    }

                    for (char digit = '1'; digit <= '9'; digit++) {

                        if (!isValid(board, row, col, digit)) {
                            continue;
                        }

                        board[row][col] = digit;

                        if (solve(board)) {
                            return true;
                        }

                        board[row][col] = '.';
                    }

                    return false;
                }
            }

            return true;
        }

        private boolean isValid(
                char[][] board,
                int row,
                int col,
                char digit
        ) {

            for (int i = 0; i < 9; i++) {

                if (board[row][i] == digit) {
                    return false;
                }

                if (board[i][col] == digit) {
                    return false;
                }

                int boxRow = 3 * (row / 3) + i / 3;
                int boxCol = 3 * (col / 3) + i % 3;

                if (board[boxRow][boxCol] == digit) {
                    return false;
                }
            }

            return true;
        }
    }

    /*
     * ============================================================
     * 2. WHY? — FOLLOW THE CODE TOP TO BOTTOM
     * ============================================================
     *
     * WHY 1 — Why find an empty cell first?
     * ------------------------------------------------------------
     * A Sudoku decision is:
     *
     *      "Which digit should occupy this unresolved cell?"
     *
     * Once we select one empty cell, its legal digits form the
     * candidate set for this recursion level.
     *
     *
     * WHY 2 — Why try digits 1..9?
     * ------------------------------------------------------------
     * Every empty cell must eventually contain exactly one digit
     * from 1 through 9.
     *
     *
     * WHY 3 — Why validate BEFORE placing?
     * ------------------------------------------------------------
     * Backtracking is strongest when every recursive state already
     * satisfies all constraints.
     *
     * Never recurse into a board that is already invalid.
     *
     *
     * WHY 4 — Why return boolean?
     * ------------------------------------------------------------
     * Sudoku asks for one completed board, not every possible board.
     *
     * true:
     *      a descendant found a full solution → stop immediately.
     *
     * false:
     *      this choice cannot lead to a solution → undo and try next.
     *
     *
     * WHY 5 — Why board[row][col] = '.' after failure?
     * ------------------------------------------------------------
     * The parent must receive exactly the same state it had before
     * trying the failed digit.
     *
     * choose:
     *      board[row][col] = digit
     *
     * explore:
     *      solve(board)
     *
     * undo:
     *      board[row][col] = '.'
     *
     *
     * WHY 6 — Why return false after all 1..9 fail?
     * ------------------------------------------------------------
     * We fixed one empty cell for this frame.
     *
     * If no legal digit can eventually solve the board,
     * the current board state is impossible.
     *
     * Signal failure to the parent.
     *
     *
     * WHY 7 — Why return true after scanning the whole board?
     * ------------------------------------------------------------
     * If no '.' exists, every cell is filled.
     *
     * Since we only ever placed legal digits, the completed board
     * is a valid solution.
     */

    /*
     * ============================================================
     * 3. 30-SECOND RECALL CARD
     * ============================================================
     *
     * TRIGGER
     *      Fill a grid under row / column / box constraints.
     *
     * PATTERN
     *      Constraint backtracking.
     *
     * STATE
     *      Current board.
     *
     * INVARIANT
     *      Every placed digit is legal.
     *
     * DECISION
     *      First empty cell.
     *
     * CANDIDATES
     *      '1' ... '9'.
     *
     * PRUNE
     *      Reject candidate if row, column, or 3×3 box conflicts.
     *
     * MOVE
     *      place → recurse → erase.
     *
     * STOP
     *      No empty cell remains.
     *
     * ONE-LINER
     *      Fill one empty cell with a legal digit; undo if it blocks
     *      the rest of the board.
     */

    /*
     * ============================================================
     * 4. REUSABLE CONSTRAINT-BACKTRACKING TEMPLATE
     * ============================================================
     *
     * boolean dfs(state) {
     *
     *     if (complete(state)) {
     *         return true;
     *     }
     *
     *     decision = chooseUnresolvedPosition(state);
     *
     *     for (candidate : candidates(decision)) {
     *
     *         if (!valid(candidate, state)) {
     *             continue;
     *         }
     *
     *         choose(candidate);
     *
     *         if (dfs(state)) {
     *             return true;
     *         }
     *
     *         undo(candidate);
     *     }
     *
     *     return false;
     * }
     *
     * Sudoku specialization:
     *
     *      unresolved position = empty cell
     *      candidates          = digits 1..9
     *      validity            = row + column + box
     */

    /*
     * ============================================================
     * 5. VISUALIZE THE RECURSION TREE
     * ============================================================
     *
     * Suppose the next empty cell is (r,c).
     *
     *                         board
     *                           |
     *                    first empty cell
     *                      (row, col)
     *                 /      /   \      \
     *                1      2    ...     9
     *                X      |             X
     *                    next empty
     *                    /   |   \
     *                   ...
     *
     * X = illegal immediately, so prune before recursion.
     *
     * A deeper dead end:
     *
     *      choose 4
     *          ↓
     *      several valid placements
     *          ↓
     *      some later cell has NO legal digit
     *          ↓
     *      return false
     *          ↓
     *      erase 4
     *          ↓
     *      try next candidate
     *
     * Important:
     *      failure may appear far below the choice that caused it.
     *      Backtracking propagates that failure upward.
     */

    /*
     * ============================================================
     * 6. BOX INDEXING — DERIVE, DON'T MEMORIZE
     * ============================================================
     *
     * For cell (row, col), the top-left corner of its 3×3 box is:
     *
     *      boxStartRow = 3 * (row / 3)
     *      boxStartCol = 3 * (col / 3)
     *
     * Iterate the 9 positions using i = 0..8:
     *
     *      boxRow = boxStartRow + i / 3
     *      boxCol = boxStartCol + i % 3
     *
     * Example:
     *
     *      row = 5, col = 7
     *
     *      row / 3 = 1 → box starts at row 3
     *      col / 3 = 2 → box starts at col 6
     *
     *      top-left = (3,6)
     *
     * Then i maps:
     *
     *      0 → (3,6)
     *      1 → (3,7)
     *      2 → (3,8)
     *      3 → (4,6)
     *      ...
     *      8 → (5,8)
     */

    /*
     * ============================================================
     * 7. APPROACH PROGRESSION / TRADE-OFFS
     * ============================================================
     *
     * A. Scan board for validity each placement  ← PREFERRED FIRST
     * ------------------------------------------------------------
     * Pros:
     *      smallest code
     *      easiest to derive under pressure
     *      no synchronization bugs
     *
     * Cons:
     *      each candidate validation scans row/column/box.
     *
     *
     * B. Precomputed row/column/box occupancy
     * ------------------------------------------------------------
     * Maintain:
     *
     *      rows[9][10]
     *      cols[9][10]
     *      boxes[9][10]
     *
     * or bitmasks.
     *
     * Candidate validity becomes O(1).
     *
     * Better when:
     *      interviewer asks for optimization
     *      solving many boards
     *      you are comfortable maintaining extra state correctly
     *
     * Cost:
     *      more code and more undo bookkeeping.
     *
     *
     * C. MRV heuristic
     * ------------------------------------------------------------
     * Instead of "first empty cell", choose the empty cell with the
     * fewest legal candidates.
     *
     * This often shrinks the search tree dramatically.
     *
     * Interview progression:
     *
     *      correct simple DFS
     *          →
     *      occupancy sets / bitmasks
     *          →
     *      choose most constrained empty cell
     */

    /*
     * ============================================================
     * 8. COMPLEXITY
     * ============================================================
     *
     * Let E = number of empty cells.
     *
     * Loose worst-case search bound:
     *
     *      O(9^E)
     *
     * Each candidate validity check scans a constant-size 9×9 board,
     * so for standard Sudoku that work is constant-bounded.
     *
     * Recursion depth:
     *
     *      O(E)
     *
     * Board is modified in place.
     *
     * Practical runtime is far smaller because Sudoku constraints
     * prune most branches.
     */

    /*
     * ============================================================
     * 9. COMMON FAILURE MODES
     * ============================================================
     *
     * 1. Forget to erase a failed digit
     *
     *      board[row][col] = '.';
     *
     * Later branches inherit corrupted state.
     *
     *
     * 2. Continue searching after solve(board) returns true
     *
     * The solved board may be undone.
     *
     * Correct:
     *
     *      if (solve(board)) {
     *          return true;
     *      }
     *
     *
     * 3. Return false too early
     *
     * Only return false after ALL candidate digits for the chosen
     * empty cell have failed.
     *
     *
     * 4. Recurse before validating
     *
     * This explodes the search space with already-invalid boards.
     *
     *
     * 5. Box math confusion
     *
     * Derive top-left:
     *
     *      3 * (row / 3)
     *      3 * (col / 3)
     *
     * then enumerate offsets.
     *
     *
     * 6. Mix "find one solution" with "generate all solutions"
     *
     * Boolean early-return is intentional because LeetCode 37 needs
     * one solved board.
     */

    /*
     * ============================================================
     * 10. INTERVIEW ARTICULATION
     * ============================================================
     *
     * "I use backtracking over empty cells. For the first empty cell,
     * I try digits 1 through 9, recurse only when the digit satisfies
     * its row, column, and 3×3 box constraints, and undo the digit if
     * the recursive call fails. The helper returns true as soon as one
     * complete valid board is found."
     */

    /*
     * ============================================================
     * 11. FINAL RETENTION CARD
     * ============================================================
     *
     * SUDOKU
     *      empty cell → legal digits → recurse → erase
     *
     * THREE CHECKS
     *      row
     *      column
     *      box
     *
     * WHY BOOLEAN?
     *      We need one solution → stop on first success.
     *
     * INVARIANT
     *      Never recurse with an invalid board.
     *
     * MASTER LINE
     *      Choose a legal value; if the future becomes impossible,
     *      restore the cell and try the next value.
     */

    /*
     * ============================================================
     * 12. SELF-VERIFYING TESTS
     * ============================================================
     *
     * Run with assertions enabled:
     *      -ea
     */
    public static void main(String[] args) {

        char[][] board = {
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };

        char[][] expected = {
                {'5', '3', '4', '6', '7', '8', '9', '1', '2'},
                {'6', '7', '2', '1', '9', '5', '3', '4', '8'},
                {'1', '9', '8', '3', '4', '2', '5', '6', '7'},
                {'8', '5', '9', '7', '6', '1', '4', '2', '3'},
                {'4', '2', '6', '8', '5', '3', '7', '9', '1'},
                {'7', '1', '3', '9', '2', '4', '8', '5', '6'},
                {'9', '6', '1', '5', '3', '7', '2', '8', '4'},
                {'2', '8', '7', '4', '1', '9', '6', '3', '5'},
                {'3', '4', '5', '2', '8', '6', '1', '7', '9'}
        };

        new Solution().solveSudoku(board);

        assert Arrays.deepEquals(board, expected)
                : "Sudoku board was not solved correctly.";

        assert isSolved(board)
                : "Solved board must contain no empty cells and remain valid.";

        System.out.println("All SudokuSolver assertions passed.");
    }

    private static boolean isSolved(char[][] board) {
        for (int row = 0; row < 9; row++) {
            boolean[] seen = new boolean[10];

            for (int col = 0; col < 9; col++) {
                char value = board[row][col];

                if (value < '1' || value > '9') {
                    return false;
                }

                int digit = value - '0';

                if (seen[digit]) {
                    return false;
                }

                seen[digit] = true;
            }
        }

        for (int col = 0; col < 9; col++) {
            boolean[] seen = new boolean[10];

            for (int row = 0; row < 9; row++) {
                int digit = board[row][col] - '0';

                if (seen[digit]) {
                    return false;
                }

                seen[digit] = true;
            }
        }

        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                boolean[] seen = new boolean[10];

                for (int row = boxRow; row < boxRow + 3; row++) {
                    for (int col = boxCol; col < boxCol + 3; col++) {
                        int digit = board[row][col] - '0';

                        if (seen[digit]) {
                            return false;
                        }

                        seen[digit] = true;
                    }
                }
            }
        }

        return true;
    }
}
