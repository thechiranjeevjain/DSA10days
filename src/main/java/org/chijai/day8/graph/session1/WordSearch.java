package org.chijai.day8.graph.session1;

import java.util.Arrays;

/**
 * WORD SEARCH — LC 79
 *
 * Interview goal:
 * Do not memorize this exact problem.
 *
 * Reconstruct the reusable pattern:
 *
 *      PATH constraint
 *      + choose one cell
 *      + explore
 *      + undo the choice
 *
 * That is grid backtracking.
 */
public class WordSearch {

    /*
     * ========================================================================
     * 1. 📘 PRIMARY PROBLEM
     * ========================================================================
     *
     * Given an m x n board of characters and a string word,
     * return true if the word can be constructed from letters in the board.
     *
     * Consecutive characters of the word must come from cells that are
     * horizontally or vertically adjacent.
     *
     * Rules:
     *
     * 1. You may move UP, DOWN, LEFT, or RIGHT.
     * 2. Diagonal movement is not allowed.
     * 3. A board cell may be used at most once in the SAME candidate path.
     * 4. A cell may be reused by a DIFFERENT candidate path after backtracking.
     *
     * ------------------------------------------------------------------------
     * Example 1
     * ------------------------------------------------------------------------
     *
     * board =
     *
     *      0   1   2   3
     *    +---+---+---+---+
     * 0  | A | B | C | E |
     *    +---+---+---+---+
     * 1  | S | F | C | S |
     *    +---+---+---+---+
     * 2  | A | D | E | E |
     *    +---+---+---+---+
     *
     * word = "ABCCED"
     *
     * One valid path:
     *
     * (0,0) A
     *    →
     * (0,1) B
     *    →
     * (0,2) C
     *    ↓
     * (1,2) C
     *    ↓
     * (2,2) E
     *    ←
     * (2,1) D
     *
     * Output: true
     *
     * ------------------------------------------------------------------------
     * Example 2
     * ------------------------------------------------------------------------
     *
     * Same board.
     *
     * word = "SEE"
     *
     * One path:
     *
     * (1,3) S -> (2,3) E -> (2,2) E
     *
     * Output: true
     *
     * ------------------------------------------------------------------------
     * Example 3
     * ------------------------------------------------------------------------
     *
     * Same board.
     *
     * word = "ABCB"
     *
     * Output: false
     *
     * Why?
     *
     * A -> B -> C is possible.
     *
     * But completing the final B would require reusing the B at (0,1),
     * and one cell cannot appear twice in the same path.
     *
     * ========================================================================
     * 2. 🧠 FIRST-PRINCIPLES THOUGHT PROGRESSION
     * ========================================================================
     *
     * Train this sequence, because this is what should be reconstructible
     * months later under interview pressure.
     *
     * ------------------------------------------------------------------------
     * Thought 1 — What is the question really asking?
     * ------------------------------------------------------------------------
     *
     * "Does there EXIST one legal path that spells this exact sequence?"
     *
     * The output is only true/false.
     *
     * So I do not need all paths.
     * I can stop as soon as one complete path succeeds.
     *
     * ------------------------------------------------------------------------
     * Thought 2 — Where can the path start?
     * ------------------------------------------------------------------------
     *
     * There is no fixed starting cell.
     *
     * Therefore every board cell is a possible starting point.
     *
     * OUTER SEARCH:
     *
     *      try DFS from every cell
     *
     * ------------------------------------------------------------------------
     * Thought 3 — Once I choose a starting cell, what changes?
     * ------------------------------------------------------------------------
     *
     * Now I am following ONE candidate path.
     *
     * At each step:
     *
     *      current board cell must match current word character
     *
     * and the next character must come from one of four neighbors.
     *
     * This naturally becomes DFS.
     *
     * ------------------------------------------------------------------------
     * Thought 4 — Why is plain DFS not enough?
     * ------------------------------------------------------------------------
     *
     * Because the current path is not allowed to reuse a cell.
     *
     * So DFS needs PATH-SPECIFIC state:
     *
     *      "Which cells are already used by THIS path?"
     *
     * ------------------------------------------------------------------------
     * Thought 5 — Is that state permanent?
     * ------------------------------------------------------------------------
     *
     * No.
     *
     * Suppose one branch uses cell X and fails.
     * A sibling branch is still allowed to use X.
     *
     * Therefore:
     *
     *      choose  -> mark
     *      explore -> recurse
     *      undo    -> restore
     *
     * The need to UNDO is the signal that this is BACKTRACKING,
     * not ordinary flood-fill DFS.
     *
     * ------------------------------------------------------------------------
     * Thought 6 — What should one recursive call mean?
     * ------------------------------------------------------------------------
     *
     * Give the function one sentence:
     *
     *      dfs(row, col, index)
     *
     * means:
     *
     *      "Can I match word[index ... end]
     *       starting exactly from board[row][col]?"
     *
     * Once this meaning is clear, the code almost writes itself.
     *
     * ------------------------------------------------------------------------
     * Thought 7 — When is this call impossible?
     * ------------------------------------------------------------------------
     *
     * It fails immediately if:
     *
     *      1. row/col is outside the board
     *      2. this cell is already used by the current path
     *      3. board[row][col] != word[index]
     *
     * These are the three discard rules.
     *
     * ------------------------------------------------------------------------
     * Thought 8 — What happens after the current character matches?
     * ------------------------------------------------------------------------
     *
     * If this was the final character:
     *
     *      success
     *
     * Otherwise:
     *
     *      mark this cell unavailable
     *      try four neighbors for index + 1
     *      restore this cell
     *
     * ------------------------------------------------------------------------
     * THE WHOLE RE-DERIVATION
     * ------------------------------------------------------------------------
     *
     *      Every cell can start
     *              ↓
     *      DFS follows one candidate path
     *              ↓
     *      path cannot reuse cells
     *              ↓
     *      mark current choice
     *              ↓
     *      explore 4 neighbors
     *              ↓
     *      restore choice
     *
     *      = GRID BACKTRACKING
     */

    /*
     * ========================================================================
     * 3. ✅ PREFERRED INTERVIEW SOLUTION
     * ========================================================================
     *
     * Mental model:
     *
     *      Word Search
     *      =
     *      Flood Fill navigation
     *      +
     *      Backtracking lifecycle
     *
     * Flood Fill contributes:
     *
     *      • boundary checks
     *      • 4-direction movement
     *
     * Backtracking contributes:
     *
     *      • path-local used state
     *      • mark
     *      • recurse
     *      • unmark
     *
     * Primary invariant:
     *
     * dfs(row, col, index) asks:
     *
     *      "Can I match word[index ... end]
     *       starting exactly from board[row][col]?"
     *
     * used[row][col] means:
     *
     *      "This cell already belongs to the CURRENT candidate path."
     */
    static final class Solution {

        private boolean[][] used;

        boolean exist(char[][] board, String word) {

            if (board == null
                    || board.length == 0
                    || board[0].length == 0
                    || word == null
                    || word.isEmpty()) {
                return false;
            }

            if (word.length() > board.length * board[0].length) {
                return false;
            }

            used = new boolean[board.length][board[0].length];

            for (int row = 0; row < board.length; row++) {

                for (int col = 0; col < board[0].length; col++) {

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

            if (row < 0
                    || row >= board.length
                    || col < 0
                    || col >= board[0].length) {
                return false;
            }

            if (used[row][col]) {
                return false;
            }

            if (board[row][col] != word.charAt(index)) {
                return false;
            }

            if (index == word.length() - 1) {
                return true;
            }

            used[row][col] = true;

            boolean found =
                    dfs(board, word, row - 1, col, index + 1)
                            || dfs(board, word, row + 1, col, index + 1)
                            || dfs(board, word, row, col - 1, index + 1)
                            || dfs(board, word, row, col + 1, index + 1);

            used[row][col] = false;

            return found;
        }
    }

    /*
     * ========================================================================
     * 4. 🔍 WHY EACH PIECE EXISTS
     * ========================================================================
     *
     * ------------------------------------------------------------------------
     * WHY TRY EVERY CELL?
     * ------------------------------------------------------------------------
     *
     * The problem gives no starting coordinate.
     *
     * Therefore every board cell is a possible start:
     *
     *      for every row
     *          for every col
     *              try dfs(row, col, 0)
     *
     * Nested for-loops are the clearest fit because this is a fixed,
     * complete traversal of the board.
     *
     * ------------------------------------------------------------------------
     * WHY DFS?
     * ------------------------------------------------------------------------
     *
     * Once one character matches, the next character must come from
     * one adjacent cell.
     *
     * We follow one candidate path until it succeeds or reaches a dead end.
     *
     * That is naturally DFS.
     *
     * ------------------------------------------------------------------------
     * WHY `used[row][col]`?
     * ------------------------------------------------------------------------
     *
     * The same board cell cannot appear twice in ONE candidate path.
     *
     * Therefore the current path needs to remember which cells it already owns.
     *
     *      used[row][col] = true
     *
     * means:
     *
     *      "This cell is unavailable to descendants of the current path."
     *
     * ------------------------------------------------------------------------
     * WHY UNMARK IT?
     * ------------------------------------------------------------------------
     *
     * This is the backtracking part.
     *
     * The cell is forbidden only for the CURRENT candidate path.
     * A sibling path may legally use it.
     *
     * Therefore:
     *
     *      used[row][col] = true;    // choose / mark
     *
     *      recurse
     *
     *      used[row][col] = false;   // undo
     *
     * ------------------------------------------------------------------------
     * WHY DOES THIS LOOK LIKE PERMUTATIONS?
     * ------------------------------------------------------------------------
     *
     * Because it is the same backtracking lifecycle.
     *
     * Permutations:
     *
     *      used[i] = true;
     *      recurse;
     *      used[i] = false;
     *
     * Word Search:
     *
     *      used[row][col] = true;
     *      recurse to neighbors;
     *      used[row][col] = false;
     *
     * The difference is only the shape of the choices:
     *
     *      Permutations -> choose another unused element
     *      Word Search  -> choose an adjacent unused cell
     *
     * ------------------------------------------------------------------------
     * WHY DOES GREEDY FAIL?
     * ------------------------------------------------------------------------
     *
     * A character can have multiple matching neighbors.
     *
     * One locally valid neighbor may lead to a dead end,
     * while another completes the word.
     *
     * Therefore all valid alternatives must be explored.
     */

    /*
     * ========================================================================
     * 5. 👀 VISUAL DRY RUN #1 — SUCCESSFUL PATH
     * ========================================================================
     *
     * board:
     *
     *      0   1   2   3
     *    +---+---+---+---+
     * 0  | A | B | C | E |
     *    +---+---+---+---+
     * 1  | S | F | C | S |
     *    +---+---+---+---+
     * 2  | A | D | E | E |
     *    +---+---+---+---+
     *
     * word = ABCCED
     *
     * +------+-------+-------+----------+-----------------------------+
     * | step | index | cell  | expected | action                      |
     * +------+-------+-------+----------+-----------------------------+
     * |  1   |   0   | (0,0) |    A     | match A, mark (0,0)         |
     * |  2   |   1   | (0,1) |    B     | match B, mark (0,1)         |
     * |  3   |   2   | (0,2) |    C     | match C, mark (0,2)         |
     * |  4   |   3   | (1,2) |    C     | match C, mark (1,2)         |
     * |  5   |   4   | (2,2) |    E     | match E, mark (2,2)         |
     * |  6   |   5   | (2,1) |    D     | final char matches -> true  |
     * +------+-------+-------+----------+-----------------------------+
     *
     * Recursion stack at the key moment:
     *
     *      A(0,0)
     *        └── B(0,1)
     *              └── C(0,2)
     *                    └── C(1,2)
     *                          └── E(2,2)
     *                                └── D(2,1) ✓
     *
     * The stack itself is the current path.
     */

    /*
     * ========================================================================
     * 6. 👀 VISUAL DRY RUN #2 — WHY REUSE MUST FAIL
     * ========================================================================
     *
     * word = ABCB
     *
     * board top row:
     *
     *      A   B   C   E
     *      0   1   2   3
     *
     * Candidate path:
     *
     *      A(0,0)
     *        →
     *      B(0,1)
     *        →
     *      C(0,2)
     *
     * Need final B.
     *
     * The tempting B is behind us at (0,1):
     *
     *      A  [B]  C
     *          ↑   |
     *          +---+
     *
     * But:
     *
     *      used[0][1] == true
     *
     * while this path is active.
     *
     * Therefore revisiting (0,1) is rejected.
     *
     * Important:
     *
     * It is forbidden only INSIDE this branch.
     * Once the branch unwinds, B is restored.
     */

    /*
     * ========================================================================
     * 7. 👀 VISUAL DRY RUN #3 — BACKTRACKING / RESTORATION
     * ========================================================================
     *
     * Imagine:
     *
     *      A A
     *      B C
     *
     * and we are exploring a word whose current prefix starts at top-left A.
     *
     * Before choice:
     *
     *      used[0][0] = false
     *
     * Choose top-left A:
     *
     *      used[0][0] = true
     *
     * Try one direction.
     *
     * Suppose that branch fails.
     *
     * Before trying a sibling path:
     *
     *      used[0][0] = false
     *
     * State timeline:
     *
     * +----------------------+-------------------+
     * | moment               | used[0][0]        |
     * +----------------------+-------------------+
     * | before choosing      | false             |
     * | during current path  | true              |
     * | after branch returns | false again       |
     * +----------------------+-------------------+
     *
     * BACKTRACKING EQUATION:
     *
     *      STATE AFTER CHILD RETURNS
     *      =
     *      STATE BEFORE CHILD WAS TRIED
     *
     * That equation is more reusable than memorizing Word Search.
     */

    /*
     * ========================================================================
     * 8. 🧩 THE REUSABLE BACKTRACKING SKELETON
     * ========================================================================
     *
     * General pattern:
     *
     *      search(state):
     *
     *          if complete:
     *              return success
     *
     *          for each possible choice:
     *
     *              if choice invalid:
     *                  continue
     *
     *              MAKE choice
     *
     *              search(next state)
     *
     *              UNDO choice
     *
     * Word Search translation:
     *
     *      choice       = use current board cell
     *      make         = used[row][col] = true
     *      next choices = 4 neighbors
     *      undo         = used[row][col] = false
     *      complete     = final word character matched
     *
     * ------------------------------------------------------------------------
     * RECONSTRUCTION QUESTION
     * ------------------------------------------------------------------------
     *
     * When stuck, ask:
     *
     *      "What temporary decision did I make
     *       that a sibling branch must not inherit?"
     *
     * Whatever the answer is:
     *
     *      that state probably needs to be undone.
     */

    /*
     * ========================================================================
     * 9. ↔ HORIZONTAL MASTERY — SAME IDEA ACROSS PROBLEMS
     * ========================================================================
     *
     * Learn the distinction, not 20 isolated implementations.
     *
     * +----------------------+----------------------+--------------------------+
     * | Problem family       | State / choice       | Undo?                    |
     * +----------------------+----------------------+--------------------------+
     * | Flood Fill           | mark reached cell    | NO — visit is permanent  |
     * | Number of Islands    | sink/mark land       | NO — visit is permanent  |
     * | Word Search          | use cell in path     | YES                      |
     * | Rat in a Maze        | use cell in path     | usually YES              |
     * | Permutations         | choose unused item   | YES: used[i] = false     |
     * | Subsets              | choose current item  | YES: remove last         |
     * | Combination Sum      | choose candidate     | YES: remove last         |
     * | N-Queens             | place queen          | YES: remove queen/state  |
     * | Sudoku               | place digit          | YES: clear digit         |
     * | Word Search II       | use grid cell        | YES + Trie prefix        |
     * +----------------------+----------------------+--------------------------+
     *
     * ------------------------------------------------------------------------
     * HIGH-ROI BOUNDARY
     * ------------------------------------------------------------------------
     *
     * Permanent visitation:
     *
     *      "I have processed this node for the whole problem."
     *
     *      -> ordinary DFS / flood fill
     *
     * Path-local visitation:
     *
     *      "I am using this node only in the current candidate."
     *
     *      -> backtracking
     *
     * This distinction is one of the highest-ROI things to retain.
     */

    /*
     * ========================================================================
     * 10. ⚖️ WORD SEARCH VS OTHER GRID PATTERNS
     * ========================================================================
     *
     * WORD SEARCH
     * -----------
     * Goal:
     *      existence of one exact sequence
     *
     * State:
     *      current path + word index
     *
     * Visit:
     *      temporary
     *
     * Typical tool:
     *      DFS backtracking
     *
     *
     * NUMBER OF ISLANDS / FLOOD FILL
     * ------------------------------
     * Goal:
     *      consume an entire connected component
     *
     * State:
     *      which cells have globally been processed
     *
     * Visit:
     *      permanent
     *
     * Typical tool:
     *      DFS or BFS
     *
     *
     * SHORTEST PATH IN GRID
     * ---------------------
     * Goal:
     *      minimum number of edges / moves
     *
     * Visit:
     *      usually permanent by shortest discovered distance
     *
     * Typical tool:
     *      BFS for unweighted edges
     *
     *
     * MEMORIZATION CUE
     * ----------------
     *
     *      exact candidate path + undo  -> backtracking
     *      whole component              -> DFS/BFS
     *      minimum moves                -> BFS
     */

    /*
     * ========================================================================
     * 11. 🔴 COMMON WRONG SOLUTIONS
     * ========================================================================
     *
     * 1. Never restore the marked cell
     * --------------------------------
     *
     * Wrong because visitation is path-local, not global.
     *
     *
     * 2. Mark only after recursion
     * ----------------------------
     *
     * Wrong because a descendant can revisit the current cell
     * before it has been marked.
     *
     *
     * 3. Return false after the first failed direction
     * ------------------------------------------------
     *
     * Wrong:
     *
     *      one neighbor failed
     *
     * does not imply:
     *
     *      all neighbors fail
     *
     *
     * 4. Search from only the first occurrence of word[0]
     * ----------------------------------------------------
     *
     * Another occurrence may be the start of the valid path.
     *
     *
     * 5. Use a global visited set
     * ---------------------------
     *
     * Wrong model.
     *
     * A cell used by one failed candidate path must become available
     * to another candidate path.
     *
     *
     * 6. Forget restoration on success
     * --------------------------------
     *
     * If the caller expects the board unchanged, mutation leaks outside.
     *
     * Compute `found`, restore, then return.
     */

    /*
     * ========================================================================
     * 12. ⏱ COMPLEXITY
     * ========================================================================
     *
     * Let:
     *
     *      M = rows
     *      N = columns
     *      L = word length
     *
     * Interview derivation:
     *
     *      M*N possible starting cells
     *      ×
     *      up to 4 choices at each step
     *      ×
     *      at most L recursive levels
     *
     * Therefore:
     *
     *      Time = O(M * N * 4^L)
     *
     * Short articulation:
     *
     *      "MN starts, up to 4 choices per step, and at most L steps."
     *
     * Reusable rule:
     *
     *      STARTS × CHOICES^DEPTH
     *
     * Important contrast:
     *
     *      Flood Fill:
     *      visited is permanent
     *      -> each cell is processed once
     *      -> O(MN)
     *
     *      Word Search:
     *      used state is undone
     *      -> many different candidate paths are explored
     *      -> count the search tree
     *
     * Space:
     *
     *      O(MN) for used[][]
     *      +
     *      O(L) recursion stack
     *
     *      = O(MN + L)
     */

    /*
     * ========================================================================
     * 13. 🎙 INTERVIEW ARTICULATION
     * ========================================================================
     *
     * "I try every board cell as a possible starting point.
     *
     * My DFS asks whether the suffix beginning at `index`
     * can be matched starting from the current cell.
     *
     * I reject out-of-bounds cells, already-used cells, and character
     * mismatches. When the current cell matches, I mark it used for this
     * candidate path, recurse in the four directions, and then unmark it
     * so sibling paths can reuse it.
     *
     * That is DFS backtracking.
     *
     * For time complexity, there are MN starting cells, up to 4 choices
     * per recursive step, and at most L steps, so the upper bound is
     * O(MN * 4^L).
     *
     * Space is O(MN + L): the used matrix plus recursion depth."
     *
     * ------------------------------------------------------------------------
     * If interviewer asks: "Why backtracking?"
     * ------------------------------------------------------------------------
     *
     * "Because used-state belongs only to the current path.
     * After one branch finishes, I must undo it for sibling branches."
     *
     * ------------------------------------------------------------------------
     * If interviewer asks: "How is this different from Flood Fill?"
     * ------------------------------------------------------------------------
     *
     * "Flood Fill marks visited permanently.
     * Word Search marks a cell only for the current path and then unmarks it."
     */

    /*
     * ========================================================================
     * 14. 🧠 30-SECOND RECALL CARD
     * ========================================================================
     *
     * WORD SEARCH
     * =
     * FLOOD FILL
     * +
     * BACKTRACKING
     *
     * GRID gives:
     *
     *      bounds
     *      4 directions
     *
     * WORD gives:
     *
     *      index
     *      character match
     *
     * BACKTRACKING gives:
     *
     *      used[row][col] = true
     *      recurse
     *      used[row][col] = false
     *
     * DFS meaning:
     *
     *      dfs(r, c, i)
     *      =
     *      can word[i...] be matched starting here?
     *
     * Complexity:
     *
     *      STARTS × CHOICES^DEPTH
     *
     *      MN × 4^L
     *
     * One-liner:
     *
     *      MATCH -> MARK USED -> 4-WAY DFS -> UNMARK
     */

    /*
     * ========================================================================
     * 15. ↔ RELATED / REINFORCEMENT PROBLEMS
     * ========================================================================
     *
     * Flood Fill / Number of Islands
     * ------------------------------
     *
     * Same:
     *      bounds + 4-direction DFS
     *
     * Different:
     *      visited is permanent
     *
     *
     * Permutations
     * ------------
     *
     * Same:
     *
     *      used = true
     *      recurse
     *      used = false
     *
     * Different:
     *      choices are array elements instead of neighboring grid cells
     *
     *
     * N-Queens / Sudoku
     * -----------------
     *
     * Same:
     *      choose -> recurse -> undo
     *
     * Different:
     *      validity rules and state representation
     *
     *
     * Word Search II
     * --------------
     *
     * Same:
     *      grid DFS + path-local used state
     *
     * Upgrade:
     *      Trie is used to share prefixes across many words
     */

    /*
     * ========================================================================
     * 16. 🎯 RETENTION / RECONSTRUCTION TEST
     * ========================================================================
     *
     * Months later, do NOT ask:
     *
     *      "Do I remember LeetCode 79?"
     *
     * Ask:
     *
     * 1. What does one DFS call mean?
     *
     * 2. Is visited state global or path-local?
     *
     * 3. What are my invalid states?
     *
     * 4. What choice do I temporarily make?
     *
     * 5. What must I undo before sibling branches?
     *
     * If you can answer those five,
     * you can reconstruct the implementation.
     *
     * ------------------------------------------------------------------------
     * MINIMUM MATERIAL TO RETAIN
     * ------------------------------------------------------------------------
     *
     *      every cell can start
     *
     *      dfs(r,c,i) = match suffix from here
     *
     *      invalid -> false
     *
     *      final match -> true
     *
     *      used = true
     *      4 directions
     *      used = false
     *
     * Everything else can be re-derived from that.
     */

    public static void main(String[] args) {

        Solution solver = new Solution();

        char[][] board1 = {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}
        };

        assert solver.exist(copy(board1), "ABCCED");
        assert solver.exist(copy(board1), "SEE");
        assert !solver.exist(copy(board1), "ABCB");

        char[][] single = {
                {'A'}
        };

        assert solver.exist(copy(single), "A");
        assert !solver.exist(copy(single), "B");

        char[][] repeated = {
                {'A', 'A'},
                {'A', 'A'}
        };

        assert solver.exist(copy(repeated), "AAAA");
        assert !solver.exist(copy(repeated), "AAAAA");

        char[][] horizontal = {
                {'A', 'B', 'C', 'D'}
        };

        assert solver.exist(copy(horizontal), "ABCD");
        assert solver.exist(copy(horizontal), "DCBA");

        char[][] vertical = {
                {'A'},
                {'B'},
                {'C'},
                {'D'}
        };

        assert solver.exist(copy(vertical), "ABCD");
        assert solver.exist(copy(vertical), "DCBA");

        char[][] zigzag = {
                {'C', 'A', 'A'},
                {'A', 'A', 'A'},
                {'B', 'C', 'D'}
        };

        assert solver.exist(copy(zigzag), "AAB");

        char[][] branchChoice = {
                {'A', 'A', 'A'},
                {'A', 'B', 'A'},
                {'A', 'A', 'A'}
        };

        // Many locally-valid A choices; DFS must be willing to backtrack.
        assert solver.exist(copy(branchChoice), "AAAB");

        char[][] impossibleReuse = {
                {'A', 'B'},
                {'C', 'D'}
        };

        assert !solver.exist(copy(impossibleReuse), "ABDCB");

        System.out.println("All WordSearch assertions passed.");
    }

    private static char[][] copy(char[][] board) {

        char[][] clone = new char[board.length][];

        for (int index = 0; index < board.length; index++) {
            clone[index] = Arrays.copyOf(board[index], board[index].length);
        }

        return clone;
    }
}

/*
 * ============================================================================
 * FINAL CLOSURE
 * ============================================================================
 *
 * Do not retain the whole file.
 *
 * Retain the invariant:
 *
 *      CURRENT PATH OWNS TEMPORARY STATE.
 *
 * Therefore:
 *
 *      choose
 *      explore
 *      undo
 *
 * That is the reusable interview pattern.
 */
