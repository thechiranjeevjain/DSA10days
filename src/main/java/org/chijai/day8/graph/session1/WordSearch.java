package org.chijai.day8.graph.session1;

import java.util.Arrays;

public class WordSearch {

    /*
     * ============================================================
     * 2. 📘 PRIMARY PROBLEM
     * ============================================================
     *
     * Title:
     * Word Search
     *
     * Difficulty:
     * Medium
     *
     * Tags:
     * Backtracking
     * DFS
     * Matrix
     * Recursion
     *
     * Official LeetCode:
     * https://leetcode.com/problems/word-search/
     *
     * ------------------------------------------------------------
     * Problem
     * ------------------------------------------------------------
     *
     * Given an m x n board of characters and a string word,
     * determine whether the word exists in the board.
     *
     * Rules:
     *
     * • Characters must be connected by horizontal or vertical moves.
     * • Diagonal movement is NOT allowed.
     * • Each board cell may be used at most once within one path.
     * • Different searches may reuse the same cell.
     *
     * Return true if such a path exists.
     *
     * ------------------------------------------------------------
     * Constraints
     * ------------------------------------------------------------
     *
     * 1 <= m, n <= 6
     * 1 <= word.length <= 15
     * board and word contain English letters.
     *
     * ------------------------------------------------------------
     * Example 1
     * ------------------------------------------------------------
     *
     * board =
     *
     * A B C E
     * S F C S
     * A D E E
     *
     * word = "ABCCED"
     *
     * Output:
     * true
     *
     * Path:
     *
     * A →
     *     B →
     *         C ↓
     *         C ↓
     *         E ←
     *     D
     *
     * ------------------------------------------------------------
     * Example 2
     * ------------------------------------------------------------
     *
     * word = "SEE"
     *
     * Output:
     * true
     *
     * ------------------------------------------------------------
     * Example 3
     * ------------------------------------------------------------
     *
     * word = "ABCB"
     *
     * Output:
     * false
     *
     * because the same B cannot be reused.
     *
     * ============================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern
     * -------
     * DFS Backtracking on Grid
     *
     * Archetype
     * ---------
     * Explore
     * → Choose
     * → Recurse
     * → Undo
     *
     * Core Invariant
     * --------------
     * Every recursive frame represents exactly one valid prefix
     * of the target word.
     *
     * The visited cells are precisely the cells that form
     * that prefix.
     *
     * Why it Works
     * ------------
     * Whenever we stand at recursion level index,
     * the first index characters have already been matched.
     *
     * The only remaining work is extending that valid prefix
     * by exactly one adjacent character.
     *
     * Since every possible extension is explored,
     * every legal path is examined exactly once.
     *
     * Recognition Signals
     * -------------------
     * Use this pattern whenever:
     *
     * • explore all paths
     * • path constraints
     * • cannot revisit nodes
     * • grid traversal
     * • exact sequence matching
     * • "does there exist?"
     *
     * When NOT to Use
     * ---------------
     * Do NOT use pure backtracking if:
     *
     * • shortest path is required
     * • weighted graph
     * • repeated states can be memoized
     * • dynamic programming naturally fits
     *
     * Comparison
     * ----------
     *
     * Flood Fill
     * ----------
     * Visits entire connected component.
     *
     * Word Search
     * -----------
     * Visits only one candidate path.
     *
     * Number of Islands
     * -----------------
     * Permanently marks visited.
     *
     * Word Search
     * -----------
     * Must restore state after every failed attempt.
     *
     * Maze DFS
     * --------
     * Destination fixed.
     *
     * Word Search
     * -----------
     * Destination depends on matching characters.
     *
     * ============================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * Mental Model
     * ------------
     *
     * Imagine walking through the board while spelling
     * the word one character at a time.
     *
     * Every recursive call answers:
     *
     * "Can I finish the remaining suffix starting
     * from this cell?"
     *
     * ------------------------------------------------------------
     * Primary Invariant
     * ------------------------------------------------------------
     *
     * search(r, c, index)
     *
     * means:
     *
     * word[0 ... index-1]
     * has already been matched.
     *
     * The current cell is expected to match
     * word[index].
     *
     * ------------------------------------------------------------
     * State Variables
     * ------------------------------------------------------------
     *
     * row
     * ----
     * current board row
     *
     * col
     * ----
     * current board column
     *
     * index
     * -----
     * next character that must be matched
     *
     * visited
     * -------
     * cells already used by the current path only.
     *
     * ------------------------------------------------------------
     * Allowed Moves
     * ------------------------------------------------------------
     *
     * Up
     * Down
     * Left
     * Right
     *
     * if
     *
     * • inside board
     * • not visited
     * • character matches
     *
     * ------------------------------------------------------------
     * Forbidden Moves
     * ------------------------------------------------------------
     *
     * Visiting outside board.
     *
     * Revisiting current path.
     *
     * Character mismatch.
     *
     * Skipping characters.
     *
     * Jumping diagonally.
     *
     * ------------------------------------------------------------
     * Transition
     * ------------------------------------------------------------
     *
     * Match current character.
     *
     * Mark visited.
     *
     * Explore four neighbors.
     *
     * Restore visited.
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * If index == word.length(),
     * every character has already been matched.
     *
     * Therefore return true immediately.
     *
     * ------------------------------------------------------------
     * Correctness Intuition
     * ------------------------------------------------------------
     *
     * Each recursive level commits exactly one character.
     *
     * Backtracking guarantees:
     *
     * after exploring one branch,
     * the board returns to exactly the same state
     * before exploring another branch.
     *
     * Therefore branches never interfere.
     *
     * ------------------------------------------------------------
     * Why Naive Search Fails
     * ------------------------------------------------------------
     *
     * Simply walking greedily fails.
     *
     * Example:
     *
     * A B C
     * A C C
     *
     * Word:
     *
     * ABCC
     *
     * Choosing the wrong C first reaches a dead end.
     *
     * Only backtracking allows recovery.
     *
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * Mistake 1
     * ---------
     * Never unmark visited.
     *
     * Looks Correct Because
     * ---------------------
     * We already explored the cell.
     *
     * Violated Invariant
     * ------------------
     * visited belongs only to ONE path.
     *
     * Counterexample
     * --------------
     *
     * Two different starting cells may legally
     * reuse the same board position.
     *
     * ------------------------------------------------------------
     * Mistake 2
     * ---------
     * Forget boundary checking before access.
     *
     * Result
     * ------
     * IndexOutOfBoundsException.
     *
     * ------------------------------------------------------------
     * Mistake 3
     * ---------
     * Mark visited after recursion.
     *
     * Violated Invariant
     * ------------------
     * A cell may appear twice inside one path.
     *
     * ------------------------------------------------------------
     * Mistake 4
     * ---------
     * Returning false immediately after
     * first failed direction.
     *
     * Reality
     * -------
     * One failed direction says nothing
     * about the remaining three.
     *
     * ------------------------------------------------------------
     * Mistake 5
     * ---------
     * Sharing visited across searches
     * without resetting.
     *
     * Interview Trap
     * --------------
     * Every new starting position begins
     * with an empty path.
     *
     * ============================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Typing Order
     * ------------
     *
     * 1. Declare visited.
     *
     * 2. exist(board, word)
     *
     * 3. Allocate visited.
     *
     * 4. Double loop over every cell.
     *
     * 5. Launch DFS.
     *
     * 6. DFS base case.
     *
     * 7. Boundary check.
     *
     * 8. Character check.
     *
     * 9. Visited check.
     *
     * 10. Mark visited.
     *
     * 11. Explore four directions.
     *
     * 12. Restore visited.
     *
     * 13. Return result.
     *
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * for every cell
     *     dfs(start)
     *
     * dfs
     *     if complete return true
     *     if invalid return false
     *     mark
     *     recurse 4 directions
     *     unmark
     *     return result
     */

    static class BruteForceSolution {

        /*
         * Idea
         * ----
         * Enumerate every possible path of length L
         * without remembering already-used cells.
         *
         * Invariant
         * ---------
         * Every sequence of moves is explored.
         *
         * Limitation
         * ----------
         * Generates many impossible paths because
         * cells may repeat.
         *
         * Complexity
         * ----------
         * Exponential.
         *
         * Interview Usefulness
         * --------------------
         * Demonstrates why path state is necessary.
         */
        boolean conceptualOnly(char[][] board, String word) {
            throw new UnsupportedOperationException(
                    "Conceptual brute-force only."
            );
        }
    }

    static class ImprovedSolution {

/*
 * Idea
 * ----
 * DFS with visited matrix.
 *
 * Improvement
 * -----------
 * Invalid paths terminate immediately.
 *
 * Invariant
 * ---------
 * visited contains exactly the current path.
 *
 * Complexity
 * ----------
 * Time:
 * O(m * n * 4^L)
 *
 * Space:
 * O(L) recursion
 * +
 * O(m * n) visited
 *
 * Interview Usefulness
 * --------------------
 * Natural stepping stone toward the
 * preferred implementation.
 */

        private boolean[][] visited;

        boolean exist(char[][] board, String word) {

            int rows = board.length;
            int cols = board[0].length;

            visited = new boolean[rows][cols];

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {

                    if (dfs(board, word, r, c, 0)) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean dfs(char[][] board,
                            String word,
                            int row,
                            int col,
                            int index) {

            if (index == word.length()) {
                return true;
            }

            if (row < 0 ||
                    row >= board.length ||
                    col < 0 ||
                    col >= board[0].length) {
                return false;
            }

            if (visited[row][col]) {
                return false;
            }

            if (board[row][col] != word.charAt(index)) {
                return false;
            }

            visited[row][col] = true;

            boolean found =
                    dfs(board, word, row - 1, col, index + 1)
                            || dfs(board, word, row + 1, col, index + 1)
                            || dfs(board, word, row, col - 1, index + 1)
                            || dfs(board, word, row, col + 1, index + 1);

            visited[row][col] = false;

            return found;
        }
    }

    static class OptimalSolution {

        /*
         * ============================================================
         * Optimal (Interview Preferred)
         * ============================================================
         *
         * Idea
         * ----
         * Launch DFS from every board cell.
         *
         * The DFS maintains one invariant:
         *
         * "The current recursion stack exactly represents
         * one valid prefix of the target word."
         *
         * Whenever a mismatch occurs,
         * that entire branch is impossible and can be
         * discarded immediately.
         *
         * ------------------------------------------------------------
         * 🟢 Invariant
         * ------------------------------------------------------------
         *
         * Before dfs(row, col, index):
         *
         * • word[0 ... index-1] is already matched.
         *
         * • visited contains exactly those matched cells.
         *
         * • Current cell is responsible only for
         *   matching word[index].
         *
         * ------------------------------------------------------------
         * Why Backtracking is Correct
         * ------------------------------------------------------------
         *
         * Marking visited commits this cell to
         * the current candidate path.
         *
         * Unmarking restores the board exactly to its
         * previous state.
         *
         * Therefore every recursive branch explores
         * an independent candidate path.
         *
         * ------------------------------------------------------------
         * Complexity
         * ------------------------------------------------------------
         *
         * Time
         * ----
         * O(M × N × 4^L)
         *
         * M*N
         * possible starting cells.
         *
         * Each recursive level explores at most
         * four directions.
         *
         * Space
         * -----
         * O(M × N)
         * visited matrix.
         *
         * O(L)
         * recursion stack.
         *
         * ------------------------------------------------------------
         * Interview Usefulness
         * ------------------------------------------------------------
         *
         * Canonical grid backtracking pattern.
         *
         * Reappears in:
         *
         * • Word Search II
         * • Path finding
         * • Maze exploration
         * • Sudoku
         * • N Queens
         */

        private boolean[][] visited;

        boolean exist(char[][] board, String word) {

            if (board == null ||
                    board.length == 0 ||
                    board[0].length == 0) {
                return false;
            }

            if (word == null) {
                return false;
            }

            int rows = board.length;
            int cols = board[0].length;

            visited = new boolean[rows][cols];

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    // Invariant:
                    // Every cell is treated as an independent start.

                    if (dfs(board, word, row, col, 0)) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean dfs(char[][] board,
                            String word,
                            int row,
                            int col,
                            int index) {

            // Invariant:
            // Entire word already matched.

            if (index == word.length()) {
                return true;
            }

            // Discard impossible coordinates.

            if (row < 0 ||
                    row >= board.length ||
                    col < 0 ||
                    col >= board[0].length) {
                return false;
            }

            // Current path cannot reuse cells.

            if (visited[row][col]) {
                return false;
            }

            // Prefix cannot be extended.

            if (board[row][col] != word.charAt(index)) {
                return false;
            }

            // Commit this character to the current path.

            visited[row][col] = true;

            boolean found =
                    dfs(board, word, row - 1, col, index + 1)
                            || dfs(board, word, row + 1, col, index + 1)
                            || dfs(board, word, row, col - 1, index + 1)
                            || dfs(board, word, row, col + 1, index + 1);

            // Restore state for sibling branches.

            visited[row][col] = false;

            return found;
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Explain the Invariant
 * ---------------------
 *
 * Each DFS frame represents one valid prefix of the word.
 *
 * The visited matrix contains exactly the cells used
 * by that prefix.
 *
 * Every recursive step attempts to extend the prefix
 * by exactly one adjacent character.
 *
 * ------------------------------------------------------------
 * Discard Rule
 * ------------------------------------------------------------
 *
 * Immediately abandon a branch if:
 *
 * • outside board
 * • already visited
 * • character mismatch
 *
 * None of these branches can ever become valid later.
 *
 * ------------------------------------------------------------
 * Correctness
 * ------------------------------------------------------------
 *
 * Every legal path is explored.
 *
 * Every illegal path is pruned at the earliest point
 * its invariant is violated.

 *
 * Because backtracking restores the previous state,
 * exploring one branch never corrupts another.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * The recursion terminates because every recursive call
 * increases index by exactly one.
 *
 * index can never exceed word.length().
 *
 * Therefore recursion depth is bounded by L.
 *
 * ------------------------------------------------------------
 * In-place Feasibility
 * ------------------------------------------------------------
 *
 * Yes.
 *
 * Instead of a visited matrix,
 * temporarily replace the current character with
 * a sentinel such as '#',
 * then restore it after recursion.
 *
 * Space becomes:
 *
 * O(L)
 *
 * since the separate visited matrix disappears.
 *
 * ------------------------------------------------------------
 * Streaming Feasibility
 * ------------------------------------------------------------
 *
 * No.
 *
 * DFS requires random access to neighboring cells
 * multiple times.
 *
 * ------------------------------------------------------------
 * When NOT to Use
 * ------------------------------------------------------------
 *
 * Avoid this pattern when:
 *
 * • repeated states can be memoized
 * • shortest path is required
 * • graph contains weighted transitions
 *
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 * Grid
 * +
 * Exact word
 * +
 * No reuse
 *
 * Pattern
 * -------
 * DFS Backtracking
 *
 * Invariant
 * ---------
 * Current recursion equals one valid prefix.
 *
 * Search Space
 * ------------
 * Every board cell.
 *
 * State
 * -----
 * row
 * col
 * index
 * visited
 *
 * Discard Rule
 * ------------
 * Out of bounds
 * OR
 * mismatch
 * OR
 * visited.
 *
 * Edge Cases
 * ----------
 *
 * Empty board.
 *
 * Single cell.
 *
 * Word length one.
 *
 * Word longer than number of cells.
 *
 * Repeated characters.
 *
 * One-liner
 * ---------
 * Match one character,
 * recurse four directions,
 * undo before returning.
 *
 * Re-derivation Cue
 * -----------------
 * Prefix committed.
 * Suffix unexplored.
 * Restore after exploration.
 *
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variation 1
 * -----------
 * In-place marking.
 *
 * Replace current character by '#'
 * and restore afterwards.
 *
 * Invariant
 * ---------
 * Board itself stores visitation state.
 *
 * Benefits
 * --------
 * Removes O(MN) visited array.
 *
 * ------------------------------------------------------------
 * Variation 2
 * -----------
 * Direction array.
 *
 * int[] dr = {-1,1,0,0}
 * int[] dc = {0,0,-1,1}
 *
 * Invariant
 * ---------
 * Neighbor generation is centralized.
 *
 * Less duplicated code.
 *
 * ------------------------------------------------------------
 * Variation 3
 * -----------
 * Frequency pruning.
 *
 * Count characters in board.
 *
 * If board lacks enough occurrences,
 * return false before DFS.
 *
 * Invariant
 * ---------
 * Impossible instances terminate early.
 *
 * ------------------------------------------------------------
 * Variation 4
 * -----------
 * Reverse search.
 *
 * Start from whichever end of the word
 * begins with the rarer character.
 *
 * Branching factor often decreases.
 *
 * ------------------------------------------------------------
 * Variation 5
 * -----------
 * Word Search II.
 *
 * Replace single word with Trie.
 *
 * Invariant changes.
 *
 * Current path represents a Trie prefix
 * instead of one target string.
 *
 * ============================================================
 * ⚫ PATTERN MAPPING
 * ============================================================
 *
 * Same Pattern
 * ------------
 *
 * • Word Search II
 * • N Queens
 * • Sudoku Solver
 * • Restore IP Addresses
 * • Letter Combinations
 * • Palindrome Partitioning
 *
 * Same Invariant
 * --------------
 *
 * Current recursion frame represents
 * one partial valid construction.
 *
 * ------------------------------------------------------------
 * Similar but Different
 * ---------------------
 *
 * Flood Fill
 * ----------
 * Never undo.
 *
 * Number of Islands
 * -----------------
 * Permanent visitation.
 *
 * Rat in a Maze
 * -------------
 * Destination fixed.
 *
 * Word Search
 * -----------
 * Destination depends on sequence matching.
 *
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * Can you explain the invariant?
 *
 * YES
 *
 * Can you define the search space?
 *
 * YES
 *
 * Can you explain the recursive state?
 *
 * YES
 *
 * Can you justify the discard rule?
 *
 * YES
 *
 * Can you explain termination?
 *
 * YES
 *
 * Can you explain why greedy fails?
 *
 * YES
 *
 * Can you debug missing backtracking?
 *
 * YES
 *
 * Can you implement without notes?
 *
 * YES
 *
 * Can you recognize pattern boundaries?
 *
 * YES
 *
 * ============================================================
 * Additional Debugging Checklist
 * ============================================================
 *
 * □ Base case before boundary?
 *
 * □ Boundary before board access?
 *
 * □ Character check before recursion?
 *
 * □ Mark before exploring?
 *
 * □ Unmark on every exit path?
 *
 * □ Four directions explored?
 *
 * □ New DFS launched from every cell?
 *
 * □ index incremented exactly once?
 *
 * □ Current cell never revisited?
 *
 * □ Early return only after successful path?
 */


    public static void main(String[] args) {

        OptimalSolution solver = new OptimalSolution();

        char[][] board1 = {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}
        };

        // Happy path:
        // Standard example requiring turns.
        assert solver.exist(copy(board1), "ABCCED");

        // Happy path:
        // Multiple occurrences of the same letter.
        assert solver.exist(copy(board1), "SEE");

        // Trap:
        // Cannot reuse the same cell.
        assert !solver.exist(copy(board1), "ABCB");

        char[][] single = {
                {'A'}
        };

        // Boundary:
        // Single matching cell.
        assert solver.exist(copy(single), "A");

        // Boundary:
        // Single mismatch.
        assert !solver.exist(copy(single), "B");

        char[][] repeated = {
                {'A', 'A'},
                {'A', 'A'}
        };

        // Repeated characters require proper visited handling.
        assert solver.exist(copy(repeated), "AAAA");

        // Longer than total cells.
        assert !solver.exist(copy(repeated), "AAAAA");

        char[][] line = {
                {'A', 'B', 'C', 'D'}
        };

        // Horizontal traversal.
        assert solver.exist(copy(line), "ABCD");

        // Reverse direction should also work because
        // every cell is a possible starting point.
        assert solver.exist(copy(line), "DCBA");

        char[][] vertical = {
                {'A'},
                {'B'},
                {'C'},
                {'D'}
        };

        // Vertical traversal.
        assert solver.exist(copy(vertical), "ABCD");

        char[][] deadEnd = {
                {'A', 'B'},
                {'C', 'D'}
        };

        // Requires backtracking but no valid completion exists.
        assert !solver.exist(copy(deadEnd), "ABDCB");

        char[][] zigzag = {
                {'C', 'A', 'A'},
                {'A', 'A', 'A'},
                {'B', 'C', 'D'}
        };

        // Common interviewer edge case.
        assert solver.exist(copy(zigzag), "AAB");

        System.out.println("All assertions passed.");
    }

    private static char[][] copy(char[][] board) {

        char[][] clone = new char[board.length][];

        for (int i = 0; i < board.length; i++) {
            clone[i] = Arrays.copyOf(board[i], board[i].length);
        }

        return clone;
    }

}

/*
============================================================
🧘 FINAL CLOSURE STATEMENT
============================================================

I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/