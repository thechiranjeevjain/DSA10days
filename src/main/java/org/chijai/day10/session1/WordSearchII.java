package org.chijai.day10.session1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordSearchII {

    /*
     * ================================================================
     * 2. 📘 PRIMARY PROBLEM
     * ================================================================
     *
     * Title:
     * Word Search II
     *
     * Difficulty:
     * Hard
     *
     * Tags:
     * Trie
     * Backtracking
     * DFS
     * Matrix
     * Prefix Tree
     * Pruning
     *
     * Problem
     * -------
     * Given an m × n board of lowercase English letters and an array
     * of words, return every word that can be formed on the board.
     *
     * A word may start from any cell.
     *
     * Consecutive characters must come from horizontally or vertically
     * adjacent cells.
     *
     * A board cell may be used at most once while constructing a word.
     *
     * Return every existing word in any order.
     *
     * Constraints
     * -----------
     * 1 <= m, n <= 12
     * board[i][j] is lowercase English letter
     * 1 <= words.length <= 3 * 10^4
     * 1 <= words[i].length <= 10
     * words[i] consists of lowercase English letters
     * All words are unique.
     *
     * Example 1
     * ---------
     * board =
     *
     * o a a n
     * e t a e
     * i h k r
     * i f l v
     *
     * words =
     * ["oath","pea","eat","rain"]
     *
     * Output:
     * ["eat","oath"]
     *
     * Example 2
     * ---------
     *
     * board =
     *
     * a b
     * c d
     *
     * words =
     * ["abcb"]
     *
     * Output:
     * []
     *
     * Official LeetCode
     * -----------------
     * https://leetcode.com/problems/word-search-ii/
     */

    /*
     * ================================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ================================================================
     *
     * Pattern
     * -------
     * Trie + Backtracking DFS + Prefix Pruning
     *
     * Archetype
     * ---------
     * Simultaneous search of many strings over one search space.
     *
     * Instead of searching every word independently,
     * we search every board path once while simultaneously walking
     * through the dictionary Trie.
     *
     * Core Invariant
     * --------------
     * Current DFS path
     * ==
     * Current Trie path.
     *
     * Every recursive level represents exactly one additional character
     * accepted simultaneously by:
     *
     * - the board
     * - the trie
     *
     * If the trie cannot continue,
     * no dictionary word can continue.
     *
     * Therefore the entire subtree can be discarded immediately.
     *
     * Why It Works
     * ------------
     * The board has enormous branching.
     *
     * Most branches never become prefixes of any dictionary word.
     *
     * Trie converts:
     *
     * "Does any word begin with this prefix?"
     *
     * into O(1) child lookup.
     *
     * Therefore dead branches disappear extremely early.
     *
     * Recognition Signals
     * -------------------
     * Think Trie + DFS whenever:
     *
     * ✓ many words
     * ✓ repeated prefix checks
     * ✓ search on grid
     * ✓ dictionary lookup during DFS
     * ✓ need pruning by prefix
     *
     * Use When
     * --------
     * - searching multiple words
     * - autocomplete
     * - prefix pruning
     * - dictionary exploration
     * - Boggle-like games
     *
     * Do NOT Use
     * ----------
     * Single word search.
     *
     * In that case ordinary DFS is simpler.
     *
     * Comparison
     * ----------
     *
     * ------------------------------------------------------------
     * Pattern                 Purpose
     * ------------------------------------------------------------
     * DFS only                One target word
     * Trie only               Dictionary storage
     * Trie + DFS              Many words on one graph
     * HashSet                 Exact lookup only
     * Backtracking            Enumerate all paths
     * ------------------------------------------------------------
     *
     * Trie is not replacing DFS.
     *
     * Trie decides
     * WHICH paths deserve DFS.
     */

    /*
     * ================================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ================================================================
     *
     * Mental Model
     * ------------
     *
     * Imagine walking simultaneously through:
     *
     * Board Path
     * ----------
     *
     * o -> a -> t -> h
     *
     * Trie Path
     * ---------
     *
     * root
     *   |
     *   o
     *   |
     *   a
     *   |
     *   t
     *   |
     *   h
     *
     * Every move must exist in BOTH structures.
     *
     * If either side breaks,
     * exploration immediately stops.
     *
     * ------------------------------------------------------------
     * Invariant 1
     * ------------------------------------------------------------
     *
     * Trie node p always represents exactly the current prefix
     * formed on the board.
     *
     * Never violate this.
     *
     * ------------------------------------------------------------
     * Invariant 2
     * ------------------------------------------------------------
     *
     * Every visited board cell belongs to the current recursive path
     * only.
     *
     * Restoration after recursion must always happen.
     *
     * ------------------------------------------------------------
     * Invariant 3
     * ------------------------------------------------------------
     *
     * Every recursive call extends the prefix
     * by exactly one character.
     *
     * ------------------------------------------------------------
     * Invariant 4
     * ------------------------------------------------------------
     *
     * If
     *
     * trie.next[currentLetter] == null
     *
     * then NO dictionary word continues.
     *
     * Entire DFS subtree is discarded.
     *
     * This is the single most important pruning rule.
     *
     * ------------------------------------------------------------
     * Invariant 5
     * ------------------------------------------------------------
     *
     * Every discovered word is reported once.
     *
     * Instead of HashSet,
     * remove the stored word from Trie.
     *
     * This transforms
     *
     * "already reported"
     *
     * into
     *
     * p.word == null
     *
     * without additional memory.
     *
     * ------------------------------------------------------------
     * Variable Meanings
     * ------------------------------------------------------------
     *
     * board
     * Current mutable search space.
     *
     * root
     * Trie root.
     *
     * p
     * Trie node representing current prefix.
     *
     * word
     * Non-null only at terminal Trie node.
     *
     * '#'
     * Temporary visited marker.
     *
     * res
     * Answer list.
     *
     * ------------------------------------------------------------
     * Allowed State Transitions
     * ------------------------------------------------------------
     *
     * Read board letter
     *
     * ↓
     *
     * Trie child exists
     *
     * ↓
     *
     * Move into Trie child
     *
     * ↓
     *
     * Mark board visited
     *
     * ↓
     *
     * Explore four neighbors
     *
     * ↓
     *
     * Restore board
     *
     * ------------------------------------------------------------
     * Forbidden Moves
     * ------------------------------------------------------------
     *
     * ✗ revisit same cell
     *
     * ✗ continue when Trie child absent
     *
     * ✗ forget restoration
     *
     * ✗ store every prefix inside StringBuilder
     *
     * ✗ search dictionary repeatedly
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * DFS terminates because:
     *
     * - board cells become visited
     * - recursion depth <= word length
     * - trie pruning removes dead branches
     *
     * ------------------------------------------------------------
     * Why Naive Solution Fails
     * ------------------------------------------------------------
     *
     * Naive approach:
     *
     * For every word
     *      run DFS.
     *
     * Complexity roughly becomes
     *
     * (#words)
     * ×
     * (board search)
     *
     * Prefixes shared by thousands of words
     * are recomputed thousands of times.
     *
     * Trie merges identical prefixes once.
     */

    /*
     * ================================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ================================================================
     *
     * Mistake 1
     * ---------
     * Search every word independently.
     *
     * Looks reasonable because Word Search I uses DFS.
     *
     * Violated Invariant:
     * Prefix reuse.
     *
     * Counterexample:
     *
     * words:
     *
     * app
     * apple
     * apply
     * application
     *
     * Prefix "app"
     * is explored repeatedly.
     *
     * ------------------------------------------------------------
     * Mistake 2
     * ------------------------------------------------------------
     *
     * Using StringBuilder to build every prefix.
     *
     * Looks harmless.
     *
     * Actually unnecessary.
     *
     * Trie node already uniquely identifies
     * current prefix.
     *
     * Store completed word directly at terminal node.
     *
     * ------------------------------------------------------------
     * Mistake 3
     * ------------------------------------------------------------
     *
     * Separate visited[][].
     *
     * Correct,
     * but extra memory and slower cache behavior.
     *
     * Board mutation is simpler.
     *
     * ------------------------------------------------------------
     * Mistake 4
     * ------------------------------------------------------------
     *
     * Use HashSet for duplicate removal.
     *
     * Better:
     *
     * p.word = null
     *
     * after reporting.
     *
     * One-time search.
     *
     * ------------------------------------------------------------
     * Mistake 5
     * ------------------------------------------------------------
     *
     * Forget restoration.
     *
     * Remaining searches permanently lose cells.
     *
     * Extremely common interview bug.
     *
     * ------------------------------------------------------------
     * Interview Trap
     * ------------------------------------------------------------
     *
     * Interviewer:
     *
     * "Why not call startsWith() every DFS?"
     *
     * Answer:
     *
     * Because every startsWith() walks again
     * from Trie root.
     *
     * Passing the current Trie node
     * preserves the invariant that
     * recursion already knows its prefix state.
     */

    /*
     * ================================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * ================================================================
     *
     * Mechanical typing order
     * -----------------------
     *
     * 1.
     * Build Trie.
     *
     * 2.
     * Loop over every board cell.
     *
     * 3.
     * DFS(board,row,col,root,result)
     *
     * 4.
     * Read current character.
     *
     * 5.
     * Reject:
     *
     * '#' ?
     *
     * Trie child absent ?
     *
     * 6.
     * Move Trie pointer.
     *
     * 7.
     * If word exists
     * collect answer
     * clear word.
     *
     * 8.
     * Mark board visited.
     *
     * 9.
     * DFS four directions.
     *
     * 10.
     * Restore board.
     *
     * Return result.
     */

    /*
     * ================================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ================================================================
     *
     * build trie
     *
     * for every cell
     *     dfs(root)
     *
     * dfs(node)
     *     reject invalid
     *     advance trie
     *     collect word
     *     mark
     *     explore
     *     restore
     *
     * return answers
     */

    /*
     * ================================================================
     * 6. SOLUTION CLASSES
     * ================================================================
     *
     * ------------------------------------------------------------
     * Brute Force
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Run Word Search I independently
     * for every dictionary word.
     *
     * Invariant
     * ---------
     * Current DFS path equals current position
     * inside one word.
     *
     * Limitation
     * ----------
     * Shared prefixes are recomputed repeatedly.
     *
     * Complexity
     * ----------
     * Approximately
     *
     * O(words × board × 4^L)
     *
     * Interview Usefulness
     * --------------------
     * Good baseline.
     * Expected to TLE.
     */

    static class BruteForce {

        public List<String> findWords(char[][] board, String[] words) {

            List<String> result = new ArrayList<>();

            for (String word : words) {

                boolean found = false;

                for (int r = 0; r < board.length && !found; r++) {

                    for (int c = 0; c < board[0].length && !found; c++) {

                        if (board[r][c] == word.charAt(0)) {

                            if (dfs(board, word, 0, r, c)) {

                                result.add(word);
                                found = true;
                            }
                        }
                    }
                }
            }

            return result;
        }

        private boolean dfs(char[][] board,
                            String word,
                            int index,
                            int row,
                            int col) {

            if (index == word.length()) {
                return true;
            }

            if (row < 0 ||
                    row >= board.length ||
                    col < 0 ||
                    col >= board[0].length ||
                    board[row][col] != word.charAt(index)) {
                return false;
            }

            char saved = board[row][col];
            board[row][col] = '#';

            boolean found =
                    dfs(board, word, index + 1, row - 1, col)
                            || dfs(board, word, index + 1, row + 1, col)
                            || dfs(board, word, index + 1, row, col - 1)
                            || dfs(board, word, index + 1, row, col + 1);

            board[row][col] = saved;

            return found;
        }
    }

    /*
     * ------------------------------------------------------------
     * Improved
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Build a Trie once for all words.
     *
     * During DFS we walk inside the Trie simultaneously.
     *
     * Every invalid prefix is pruned immediately.
     *
     * Invariant
     * ---------
     * The Trie node passed into recursion always represents the
     * exact prefix formed by the current board path.
     *
     * Improvement
     * -----------
     * Shared prefixes are explored only once.
     *
     * Complexity
     * ----------
     * Trie Build:
     *
     * O(Σ|words|)
     *
     * Search:
     *
     * O(m × n × maximumWordLength)
     *
     * in practice because prefix pruning removes almost every
     * impossible branch very early.
     *
     * Extra Space
     * -----------
     * O(Σ|words|)
     *
     * Interview Usefulness
     * --------------------
     * This is the expected optimization that distinguishes
     * Word Search II from Word Search I.
     */

    static class Improved {

        static class TrieNode {

            TrieNode[] next = new TrieNode[26];

            boolean isWord;
        }

        public List<String> findWords(char[][] board, String[] words) {

            TrieNode root = buildTrie(words);

            List<String> answer = new ArrayList<>();

            StringBuilder current = new StringBuilder();

            boolean[][] visited =
                    new boolean[board.length][board[0].length];

            for (int row = 0; row < board.length; row++) {

                for (int col = 0; col < board[0].length; col++) {

                    dfs(board,
                            row,
                            col,
                            root,
                            current,
                            visited,
                            answer);
                }
            }

            return answer;
        }

        private void dfs(char[][] board,
                         int row,
                         int col,
                         TrieNode node,
                         StringBuilder current,
                         boolean[][] visited,
                         List<String> answer) {

            if (row < 0 ||
                    row >= board.length ||
                    col < 0 ||
                    col >= board[0].length ||
                    visited[row][col]) {
                return;
            }

            TrieNode nextNode =
                    node.next[board[row][col] - 'a'];

            if (nextNode == null) {
                return;
            }

            visited[row][col] = true;

            current.append(board[row][col]);

            if (nextNode.isWord) {

                String candidate = current.toString();

                if (!answer.contains(candidate)) {
                    answer.add(candidate);
                }
            }

            dfs(board,
                    row - 1,
                    col,
                    nextNode,
                    current,
                    visited,
                    answer);

            dfs(board,
                    row + 1,
                    col,
                    nextNode,
                    current,
                    visited,
                    answer);

            dfs(board,
                    row,
                    col - 1,
                    nextNode,
                    current,
                    visited,
                    answer);

            dfs(board,
                    row,
                    col + 1,
                    nextNode,
                    current,
                    visited,
                    answer);

            current.deleteCharAt(current.length() - 1);

            visited[row][col] = false;
        }

        private TrieNode buildTrie(String[] words) {

            TrieNode root = new TrieNode();

            for (String word : words) {

                TrieNode current = root;

                for (char ch : word.toCharArray()) {

                    int index = ch - 'a';

                    if (current.next[index] == null) {
                        current.next[index] = new TrieNode();
                    }

                    current = current.next[index];
                }

                current.isWord = true;
            }

            return root;
        }
    }

    /*
     * ------------------------------------------------------------
     * Optimal (Interview Preferred)
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Combine:
     *
     * 1. Trie
     * 2. DFS
     * 3. Prefix pruning
     * 4. In-place visited marking
     * 5. One-time reporting
     *
     * The major optimizations over the previous version are:
     *
     * • No StringBuilder
     * • No visited[][]
     * • No HashSet
     * • No duplicate searches
     *
     * Instead,
     * every terminal Trie node stores the complete word.
     *
     * Once reported:
     *
     * node.word = null
     *
     * guarantees uniqueness.
     *
     * ------------------------------------------------------------
     * Core Invariant
     * ------------------------------------------------------------
     *
     * Before exploring neighbors:
     *
     * board path
     * ==
     * Trie node
     *
     * remains true.
     *
     * Every recursive call preserves this invariant.
     *
     * ------------------------------------------------------------
     * Correctness
     * ------------------------------------------------------------
     *
     * Every recursive move simultaneously:
     *
     * • consumes one board cell
     * • advances one Trie edge
     *
     * Therefore every reported word exists both:
     *
     * • in the board
     * • in the dictionary
     *
     * Conversely,
     * Trie pruning guarantees no valid dictionary path
     * is skipped.
     *
     * ------------------------------------------------------------
     * Complexity
     * ------------------------------------------------------------
     *
     * Build Trie
     *
     * O(Σ|words|)
     *
     * DFS
     *
     * O(m × n × maximumWordLength)
     *
     * Worst theoretical branching is larger,
     * but aggressive Trie pruning dominates practical runtime.
     *
     * Extra Space
     * -----------
     *
     * Trie:
     *
     * O(Σ|words|)
     *
     * Recursion:
     *
     * O(maximumWordLength)
     *
     * ------------------------------------------------------------
     * Interview Usefulness
     * ------------------------------------------------------------
     *
     * This is the canonical solution expected for
     * LeetCode Word Search II.
     */

    static class Optimal {

        static class TrieNode {

            TrieNode[] next = new TrieNode[26];

            String word;
        }

        public List<String> findWords(char[][] board,
                                      String[] words) {

            List<String> answer = new ArrayList<>();

            TrieNode root = buildTrie(words);

            for (int row = 0; row < board.length; row++) {

                for (int col = 0; col < board[0].length; col++) {

                    dfs(board,
                            row,
                            col,
                            root,
                            answer);
                }
            }

            return answer;
        }

        private void dfs(char[][] board,
                         int row,
                         int col,
                         TrieNode node,
                         List<String> answer) {

            char currentCharacter = board[row][col];

            // 🟢 Invariant:
            // Current board prefix must remain a Trie prefix.
            if (currentCharacter == '#') {
                return;
            }

            TrieNode nextNode =
                    node.next[currentCharacter - 'a'];

            // 🔴 Prefix disappeared.
            // Entire subtree becomes impossible.
            if (nextNode == null) {
                return;
            }

            // 🟢 Terminal Trie node stores complete dictionary word.
            if (nextNode.word != null) {

                answer.add(nextNode.word);

                // 🟢 One-time reporting removes duplicates naturally.
                nextNode.word = null;
            }

            // 🟢 Mark current path.
            board[row][col] = '#';

            if (row > 0) {

                dfs(board,
                        row - 1,
                        col,
                        nextNode,
                        answer);
            }

            if (col > 0) {

                dfs(board,
                        row,
                        col - 1,
                        nextNode,
                        answer);
            }

            if (row < board.length - 1) {

                dfs(board,
                        row + 1,
                        col,
                        nextNode,
                        answer);
            }

            if (col < board[0].length - 1) {

                dfs(board,
                        row,
                        col + 1,
                        nextNode,
                        answer);
            }

            // 🟢 Restore board for independent future searches.
            board[row][col] = currentCharacter;
        }

        private TrieNode buildTrie(String[] words) {

            TrieNode root = new TrieNode();

            for (String word : words) {

                TrieNode current = root;

                for (char ch : word.toCharArray()) {

                    int index = ch - 'a';

                    if (current.next[index] == null) {

                        current.next[index] = new TrieNode();
                    }

                    current = current.next[index];
                }

                current.word = word;
            }

            return root;
        }
    }

/*
 * ================================================================
 * 🟣 INTERVIEW ARTICULATION
 * ================================================================
 *
 * Explain the solution like this:
 *
 * "Instead of running DFS separately for every word,
 * I build one Trie containing every dictionary word.
 *
 * Every DFS path walks through the board and the Trie
 * simultaneously.
 *
 * The invariant is that the current Trie node always
 * represents the exact prefix currently formed on the board.
 *
 * If a Trie child does not exist,
 * no dictionary word can continue from this prefix,
 * so the entire DFS subtree is discarded immediately.
 *
 * A board cell is temporarily marked '#'
 * to prevent reuse on the current recursive path,
 * then restored while backtracking.
 *
 * Terminal Trie nodes store complete words.
 * After reporting a word once,
 * I set node.word = null,
 * which naturally removes duplicates without HashSet.
 *
 * The recursion terminates because each recursive level
 * consumes one unused board cell,
 * recursion depth is bounded by the maximum word length,
 * and invalid prefixes are pruned immediately."
 */

/*
 * ================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ================================================================
 *
 * Trigger
 * -------
 * Many dictionary words on one board.
 *
 * Pattern
 * -------
 * Trie + DFS + Prefix Pruning.
 *
 * Invariant
 * ---------
 * Current board prefix
 * ==
 * Current Trie node.
 *
 * Search Space
 * ------------
 * Every possible board path.
 *
 * Discard Rule
 * ------------
 * Trie child missing.
 *
 * Common Trap
 * -----------
 * Forget board restoration.
 *
 * Edge Cases
 * ----------
 * Empty answer.
 * Duplicate discoveries.
 * Single character words.
 * One cell board.
 * Shared prefixes.
 *
 * One-liner
 * ---------
 * Trie tells DFS exactly which prefixes are worth exploring.
 *
 * Re-derivation Cue
 * -----------------
 * Replace:
 *
 * "search every word"
 *
 * with
 *
 * "search every prefix once."
 */

/*
 * ================================================================
 * 🔄 VARIATIONS & TWEAKS
 * ================================================================
 *
 * ------------------------------------------------------------
 * Variation 1
 * ------------------------------------------------------------
 * Word Search I
 *
 * Pattern
 * -------
 * DFS + Backtracking
 *
 * Reasoning Change
 * ----------------
 * Only one target word exists.
 *
 * No Trie is needed because there are no shared prefixes
 * across multiple words.
 *
 * ------------------------------------------------------------
 * Variation 2
 * ------------------------------------------------------------
 * Boggle Solver
 *
 * Pattern
 * -------
 * Trie + DFS
 *
 * Reasoning Change
 * ----------------
 * Eight directions instead of four.
 *
 * Invariant remains unchanged:
 *
 * Current board prefix
 * ==
 * Current Trie node.
 *
 * ------------------------------------------------------------
 * Variation 3
 * ------------------------------------------------------------
 * Prefix Frequency Queries
 *
 * Pattern
 * -------
 * Trie
 *
 * Reasoning Change
 * ----------------
 * Store prefix counts in Trie nodes.
 *
 * DFS disappears.
 *
 * ------------------------------------------------------------
 * Variation 4
 * ------------------------------------------------------------
 * Streaming Characters
 *
 * Pattern
 * -------
 * Trie / Automaton
 *
 * Reasoning Change
 * ----------------
 * Search space becomes a stream instead of a grid.
 *
 * ------------------------------------------------------------
 * Variation 5
 * ------------------------------------------------------------
 * Dynamic Dictionary
 *
 * Pattern
 * -------
 * Mutable Trie
 *
 * Reasoning Change
 * ----------------
 * Insert and delete words while preserving Trie structure.
 *
 * ------------------------------------------------------------
 * Pattern Boundary
 * ------------------------------------------------------------
 *
 * Trie is useful only when:
 *
 * • many strings
 * • repeated prefix checks
 *
 * If there is exactly one target,
 * Trie adds unnecessary overhead.
 */

/*
 * ================================================================
 * 🧠 MASTERY CHECKLIST
 * ================================================================
 *
 * □ What is the invariant?
 *
 * Current Trie node represents exactly the prefix
 * currently formed on the board.
 *
 * ------------------------------------------------------------
 *
 * □ What is the search target?
 *
 * Every board path that is also
 * a dictionary prefix.
 *
 * ------------------------------------------------------------
 *
 * □ What is the discard rule?
 *
 * Trie child missing.
 *
 * ------------------------------------------------------------
 *
 * □ Why does recursion terminate?
 *
 * Cells cannot be revisited.
 * Prefix dies.
 * Word length is finite.
 *
 * ------------------------------------------------------------
 *
 * □ Why does the naive solution fail?
 *
 * Shared prefixes are recomputed
 * independently for every word.
 *
 * ------------------------------------------------------------
 *
 * □ Which edge cases matter?
 *
 * Empty answer.
 * Single cell.
 * One character words.
 * Shared prefixes.
 * Duplicate discoveries.
 *
 * ------------------------------------------------------------
 *
 * □ Debugging readiness?
 *
 * Verify:
 *
 * - board restoration
 * - Trie transitions
 * - duplicate removal
 * - boundary checks
 *
 * ------------------------------------------------------------
 *
 * □ Variant readiness?
 *
 * Can adapt to:
 *
 * - Boggle
 * - Word Search I
 * - Prefix search
 * - Dictionary traversal
 *
 * ------------------------------------------------------------
 *
 * □ Pattern boundary?
 *
 * Trie solves repeated prefix lookup.
 * Not useful for searching one isolated word.
 */

/*
 * ================================================================
 * 🔍 DEBUGGING PLAYBOOK
 * ================================================================
 *
 * Symptom:
 * Same word appears multiple times.
 *
 * Cause:
 * node.word not cleared.
 *
 * ------------------------------------------------------------
 *
 * Symptom:
 * Some valid words disappear.
 *
 * Cause:
 * Board restoration forgotten.
 *
 * ------------------------------------------------------------
 *
 * Symptom:
 * StackOverflowError.
 *
 * Cause:
 * Cell revisit allowed.
 *
 * ------------------------------------------------------------
 *
 * Symptom:
 * Runtime much slower than expected.
 *
 * Cause:
 * Restarting Trie traversal from root
 * inside recursive calls.
 *
 * ------------------------------------------------------------
 *
 * Symptom:
 * TLE.
 *
 * Cause:
 * Searching every word independently.
 */

/*
 * ================================================================
 * ⚫ PATTERN MAPPING
 * ================================================================
 *
 * Problem
 * ------------------------------ Pattern
 *
 * Word Search I
 * DFS + Backtracking
 *
 * Word Search II
 * Trie + DFS
 *
 * Replace Words
 * Trie
 *
 * Design Add and Search Words
 * Trie + DFS
 *
 * Stream of Characters
 * Trie / Automaton
 *
 * Concatenated Words
 * Trie / DP
 *
 * Boggle
 * Trie + DFS
 */

/*
 * ================================================================
 * ⚡ IMPLEMENTATION RECONSTRUCTION
 * ================================================================
 *
 * Memorize only this sequence:
 *
 * Build Trie
 *
 * ↓
 *
 * Loop every cell
 *
 * ↓
 *
 * Reject:
 *     '#'
 *     child absent
 *
 * ↓
 *
 * Advance Trie
 *
 * ↓
 *
 * Report word
 *
 * ↓
 *
 * Mark '#'
 *
 * ↓
 *
 * Explore
 * Up
 * Left
 * Down
 * Right
 *
 * ↓
 *
 * Restore
 *
 * Done.
 */

/*
 * ================================================================
 * ⚡ COMMON INTERVIEW FOLLOW UPS
 * ================================================================
 *
 * Q.
 * Why store the whole word instead of a boolean?
 *
 * A.
 * It removes StringBuilder construction,
 * avoids rebuilding prefixes,
 * and directly returns the answer.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Why mutate the board?
 *
 * A.
 * Saves an O(mn) visited matrix,
 * improves locality,
 * and simplifies state restoration.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Why clear node.word?
 *
 * A.
 * One-time reporting naturally eliminates duplicates.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Can we physically remove Trie branches?
 *
 * A.
 * Yes.
 * After DFS returns,
 * if a Trie node has no children and no word,
 * it can be deleted.
 *
 * This reduces future exploration,
 * although it is not required for interview-quality code.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Why is Trie passed as an argument?
 *
 * A.
 * Restarting from the root each recursive step
 * destroys the prefix invariant
 * and wastes repeated work.
 */

/*
 * ================================================================
 * 📐 COMPLEXITY DERIVATION
 * ================================================================
 *
 * Let
 *
 * M = rows
 * N = columns
 * L = maximum word length
 * W = total characters across all words
 *
 * Trie Construction
 * -----------------
 *
 * O(W)
 *
 * DFS
 * ---
 *
 * Every search begins from M × N cells.
 *
 * Trie pruning prevents exploring prefixes
 * that never occur in the dictionary.
 *
 * Practical complexity:
 *
 * O(M × N × L)
 *
 * Extra Space
 * -----------
 *
 * Trie:
 * O(W)
 *
 * Recursion:
 * O(L)
 */

/*
 * ================================================================
 * 🧩 INVARIANT SUMMARY
 * ================================================================
 *
 * Invariant 1
 * -----------
 * Current DFS path
 * ==
 * Current Trie node.
 *
 * Invariant 2
 * -----------
 * '#' means:
 * this board cell belongs only
 * to the current recursive path.
 *
 * Invariant 3
 * -----------
 * Every recursive level
 * consumes exactly one character.
 *
 * Invariant 4
 * -----------
 * Missing Trie child
 * implies
 * impossible dictionary continuation.
 *
 * Invariant 5
 * -----------
 * node.word == null
 * means
 * already reported.
 */

    /*
     * ================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ================================================================
     */

    private static char[][] copyBoard(char[][] board) {

        char[][] copy = new char[board.length][];

        for (int i = 0; i < board.length; i++) {
            copy[i] = Arrays.copyOf(board[i], board[i].length);
        }

        return copy;
    }

    private static void assertContainsExactly(List<String> actual,
                                              String... expected) {

        List<String> expectedList = new ArrayList<>(Arrays.asList(expected));

        assert actual.size() == expectedList.size()
                : "Expected size " + expectedList.size()
                + " but found " + actual.size();

        for (String word : expectedList) {
            assert actual.contains(word)
                    : "Missing expected word: " + word;
        }
    }

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        /*
         * Happy Path
         *
         * Canonical example from the problem statement.
         */
        char[][] board1 = {
                {'o', 'a', 'a', 'n'},
                {'e', 't', 'a', 'e'},
                {'i', 'h', 'k', 'r'},
                {'i', 'f', 'l', 'v'}
        };

        List<String> answer1 = solver.findWords(
                copyBoard(board1),
                new String[]{"oath", "pea", "eat", "rain"}
        );

        assertContainsExactly(answer1, "eat", "oath");

        /*
         * Edge Case
         *
         * No word can be formed because a cell
         * cannot be reused.
         */
        char[][] board2 = {
                {'a', 'b'},
                {'c', 'd'}
        };

        List<String> answer2 = solver.findWords(
                copyBoard(board2),
                new String[]{"abcb"}
        );

        assert answer2.isEmpty()
                : "Expected empty answer.";

        /*
         * Boundary
         *
         * Single cell board.
         */
        char[][] board3 = {
                {'a'}
        };

        List<String> answer3 = solver.findWords(
                copyBoard(board3),
                new String[]{"a", "aa", "b"}
        );

        assertContainsExactly(answer3, "a");

        /*
         * Shared Prefixes
         *
         * Ensures Trie sharing works.
         */
        char[][] board4 = {
                {'a', 'p', 'p'},
                {'l', 'e', 'y'}
        };

        List<String> answer4 = solver.findWords(
                copyBoard(board4),
                new String[]{
                        "app",
                        "apple",
                        "apply",
                        "ape"
                }
        );

        assertContainsExactly(answer4, "app", "apple");

        /*
         * Duplicate Discovery Trap
         *
         * Same word can be discovered through
         * multiple search starts.
         *
         * node.word = null should guarantee
         * only one copy is returned.
         */
        char[][] board5 = {
                {'a', 'a'},
                {'a', 'a'}
        };

        List<String> answer5 = solver.findWords(
                copyBoard(board5),
                new String[]{"aa"}
        );

        assert answer5.size() == 1
                : "Duplicate removal failed.";

        assert answer5.contains("aa");

        /*
         * Long Straight Path
         */
        char[][] board6 = {
                {'h', 'e', 'l', 'l', 'o'}
        };

        List<String> answer6 = solver.findWords(
                copyBoard(board6),
                new String[]{
                        "hello",
                        "hell",
                        "help"
                }
        );

        assertContainsExactly(answer6, "hell", "hello");

        /*
         * Prefix Without Complete Word
         *
         * DFS should continue through prefixes
         * without reporting them.
         */
        char[][] board7 = {
                {'c', 'a', 't'}
        };

        List<String> answer7 = solver.findWords(
                copyBoard(board7),
                new String[]{
                        "cat"
                }
        );

        assertContainsExactly(answer7, "cat");

        /*
         * Empty Dictionary
         */
        char[][] board8 = {
                {'a', 'b'},
                {'c', 'd'}
        };

        List<String> answer8 = solver.findWords(
                copyBoard(board8),
                new String[]{}
        );

        assert answer8.isEmpty();

        /*
         * Multiple Independent Words
         */
        char[][] board9 = {
                {'d', 'o', 'g'},
                {'c', 'a', 't'}
        };

        List<String> answer9 = solver.findWords(
                copyBoard(board9),
                new String[]{
                        "dog",
                        "cat",
                        "cow"
                }
        );

        assertContainsExactly(answer9, "dog", "cat");

        /*
         * Restoration Verification
         *
         * Running twice on identical boards
         * should produce identical answers.
         */
        List<String> firstRun = solver.findWords(
                copyBoard(board1),
                new String[]{"oath", "eat"}
        );

        List<String> secondRun = solver.findWords(
                copyBoard(board1),
                new String[]{"oath", "eat"}
        );

        assertContainsExactly(firstRun, "eat", "oath");
        assertContainsExactly(secondRun, "eat", "oath");

        /*
         * Deep Prefix Chain
         */
        char[][] board10 = {
                {'a', 'b', 'c', 'd'}
        };

        List<String> answer10 = solver.findWords(
                copyBoard(board10),
                new String[]{
                        "a",
                        "ab",
                        "abc",
                        "abcd",
                        "abcde"
                }
        );

        assertContainsExactly(
                answer10,
                "a",
                "ab",
                "abc",
                "abcd"
        );

        System.out.println("All assertions passed.");
    }

}