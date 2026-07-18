package org.chijai.day10.session1;
import java.util.*;

/**
 * ============================================================================
 * LeetCode 208 — Implement Trie (Prefix Tree)
 * ============================================================================
 *
 * OFFICIAL PROBLEM
 * ----------------
 *
 * A Trie (pronounced as "try"), also called a Prefix Tree, is a tree
 * data structure used to efficiently store and retrieve strings.
 *
 * Implement the Trie class:
 *
 * Trie()
 *      Initializes the trie object.
 *
 * void insert(String word)
 *      Inserts word into the trie.
 *
 * boolean search(String word)
 *      Returns true if the exact word exists.
 *
 * boolean startsWith(String prefix)
 *      Returns true if any inserted word starts with prefix.
 *
 * Example
 *
 * Trie trie = new Trie();
 * trie.insert("apple");
 * trie.search("apple");      // true
 * trie.search("app");        // false
 * trie.startsWith("app");    // true
 * trie.insert("app");
 * trie.search("app");        // true
 *
 * Constraints
 *
 * 1 <= word.length <= 2000
 * word contains lowercase English letters.
 * At most 3 * 10^4 operations.
 *
 * Difficulty
 * Medium
 *
 * Tags
 * Trie
 * Design
 * String
 *
 * Official Link
 * https://leetcode.com/problems/implement-trie-prefix-tree/
 *
 * ============================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern
 * -------
 * Trie (Prefix Tree)
 *
 * Archetype
 * ---------
 * Hierarchical prefix indexing.
 *
 * Core Invariant
 * --------------
 * Every node uniquely represents ONE prefix from the root.
 *
 * Root
 * ""
 *
 * root
 *   |
 *   a
 *   |
 *   ap
 *   |
 *   app
 *   |
 *   appl
 *   |
 *   apple
 *
 * A node NEVER represents multiple prefixes.
 *
 * Every edge corresponds to exactly one character.
 *
 * A word exists iff
 *
 * 1) every character path exists
 * AND
 * 2) last node is marked as terminal.
 *
 * Why it Works
 * ------------
 * Shared prefixes are stored once.
 *
 * Example
 *
 * apple
 * app
 * apply
 *
 * All share
 *
 * app
 *
 * so only one path is created.
 *
 * Time
 * ----
 * O(length of string)
 *
 * independent of number of stored words.
 *
 * Recognition Signals
 * -------------------
 *
 * ✓ Prefix queries
 *
 * ✓ Autocomplete
 *
 * ✓ Dictionary
 *
 * ✓ Word lookup
 *
 * ✓ StartsWith
 *
 * ✓ Replace words
 *
 * ✓ Longest common prefix using inserted words
 *
 * Differences vs HashSet
 * ----------------------
 *
 * HashSet
 *
 * Exact lookup only.
 *
 * startsWith("app")
 *
 * requires scanning.
 *
 * Trie
 *
 * Prefix search is naturally supported.
 *
 * Differences vs Binary Search Tree
 * ---------------------------------
 *
 * BST orders complete keys.
 *
 * Trie indexes character-by-character.
 *
 * ============================================================================
 * 🟢 MENTAL MODEL
 * ============================================================================
 *
 * Imagine every character is one road.
 *
 * root
 *   |
 *   a
 *  / \
 * p   n
 *
 * Continue walking.
 *
 * If road exists
 *      move.
 *
 * Otherwise
 *      create it (insert)
 *      or fail (search).
 *
 * ============================================================================
 * 🟢 VARIABLES
 * ============================================================================
 *
 * Node
 *
 * children
 *      outgoing character edges
 *
 * isWord
 *      whether current prefix is a complete word
 *
 * current
 *      current prefix while traversing
 *
 * ============================================================================
 * 🟢 INVARIANTS
 * ============================================================================
 *
 * Invariant 1
 * -----------
 * Root always represents empty prefix.
 *
 * Invariant 2
 * -----------
 * Each edge adds exactly one character.
 *
 * Invariant 3
 * -----------
 * Current node always equals the prefix processed so far.
 *
 * Invariant 4
 * -----------
 * Missing edge means no word containing that prefix exists.
 *
 * Invariant 5
 * -----------
 * Terminal marker distinguishes
 *
 * app
 *
 * from
 *
 * apple
 *
 * even though both share nodes.
 *
 * Invariant 6
 * -----------
 * insert() never destroys existing paths.
 *
 * It only extends them.
 *
 * Invariant 7
 * -----------
 * search() succeeds ONLY if
 *
 * final node exists
 * &&
 * final node.isWord == true
 *
 * Invariant 8
 * -----------
 * startsWith() ignores terminal marker.
 *
 * It only requires path existence.
 *
 * ============================================================================
 * 🟢 ALLOWED MOVES
 * ============================================================================
 *
 * Move to child.
 *
 * Create child.
 *
 * Mark terminal.
 *
 * Stop when character missing.
 *
 * ============================================================================
 * 🔴 FORBIDDEN MOVES
 * ============================================================================
 *
 * Never delete shared prefix accidentally.
 *
 * Never return true from search()
 * without checking terminal flag.
 *
 * Never create nodes during search().
 *
 * Never stop traversal before all characters are processed.
 *
 * ============================================================================
 * 🟡 TERMINATION
 * ============================================================================
 *
 * Insert
 * ------
 * After final character,
 * mark node as terminal.
 *
 * Search
 * ------
 * After final character,
 * verify terminal.
 *
 * StartsWith
 * ----------
 * After final character,
 * return true immediately.
 *
 * ============================================================================
 * 🟡 WHY NAIVE APPROACHES FAIL
 * ============================================================================
 *
 * Using ArrayList<String>
 *
 * insert
 * O(1)
 *
 * search
 * O(N)
 *
 * startsWith
 * O(N)
 *
 * because every string may need checking.
 *
 * HashSet
 *
 * search
 * O(1)
 *
 * startsWith
 * O(N)
 *
 * because prefixes are not indexed.
 *
 * Trie stores prefixes directly.
 *
 * ============================================================================
 * 🔴 COMMON WRONG SOLUTIONS
 * ============================================================================
 *
 * Wrong #1
 * --------
 * Returning true after walking path.
 *
 * Counterexample
 *
 * insert("apple")
 *
 * search("app")
 *
 * Incorrectly returns true.
 *
 * Broken invariant:
 * terminal node ignored.
 *
 * Wrong #2
 * --------
 * Creating nodes during search.
 *
 * This mutates the dictionary.
 *
 * Search must never modify state.
 *
 * Wrong #3
 * --------
 * Overwriting children during insert.
 *
 * Existing words disappear.
 *
 * Broken invariant:
 * insert only extends.
 *
 * ============================================================================
 * 🛠 IMPLEMENTATION BLUEPRINT
 * ============================================================================
 *
 * Step 1
 * -------
 * Create TrieNode.
 *
 * Step 2
 * -------
 * Root node.
 *
 * Step 3
 * -------
 * insert(word)
 *
 * current = root
 *
 * for every character
 *
 *      create child if absent
 *
 *      move
 *
 * mark terminal
 *
 * Step 4
 * -------
 * search(word)
 *
 * current=root
 *
 * walk path
 *
 * missing child -> false
 *
 * return current.isWord
 *
 * Step 5
 * -------
 * startsWith(prefix)
 *
 * walk path
 *
 * missing child -> false
 *
 * otherwise true
 *
 * ============================================================================
 * MEMORY SCAFFOLD
 * ============================================================================
 *
 * insert
 *
 * root
 * loop chars
 * create if absent
 * move
 * mark word
 *
 * search
 *
 * root
 * loop chars
 * missing -> false
 * move
 * return terminal
 *
 * prefix
 *
 * root
 * loop chars
 * missing -> false
 * move
 * return true
 *
 * ============================================================================
 * PUBLIC CLASS
 * ============================================================================
 */

public class TriePrefix {

    /**
     * ==============================================================
     * Brute Force
     * ==============================================================
     *
     * Store every word.
     *
     * Search scans.
     *
     * Prefix scans.
     *
     * Good for intuition only.
     */

    static class BruteForceTrie {

        private final List<String> words = new ArrayList<>();

        public void insert(String word) {
            words.add(word);
        }

        public boolean search(String word) {
            for (String s : words) {
                if (s.equals(word)) {
                    return true;
                }
            }
            return false;
        }

        public boolean startsWith(String prefix) {
            for (String s : words) {
                if (s.startsWith(prefix)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * ==============================================================
     * Improved Solution
     * ==============================================================
     *
     * Core Idea
     * ---------
     * Use a HashSet to make exact-word lookup O(1).
     *
     * Prefix lookup is still O(N) because every stored word may need
     * to be examined.
     *
     * Invariant
     * ---------
     * The HashSet always contains every inserted word exactly once.
     *
     * Limitation Fixed
     * ----------------
     * Improves exact search compared to brute force.
     *
     * Remaining Limitation
     * --------------------
     * Prefix queries are not indexed.
     *
     * Time
     * ----
     * insert      O(1) average
     * search      O(1) average
     * startsWith  O(N * L)
     *
     * Space
     * -----
     * O(total characters)
     *
     * Interview Preference
     * --------------------
     * Mention briefly, then move to Trie.
     */

    static class HashSetTrie {

        private final Set<String> dictionary = new HashSet<>();

        public void insert(String word) {
            dictionary.add(word);
        }

        public boolean search(String word) {
            return dictionary.contains(word);
        }

        public boolean startsWith(String prefix) {

            for (String word : dictionary) {

                if (word.startsWith(prefix)) {
                    return true;
                }

            }

            return false;
        }
    }

    /**
     * ==============================================================
     * Optimal Solution (Interview Preferred)
     * ==============================================================
     *
     * Core Idea
     * ---------
     * Build one node for every prefix.
     *
     * Shared prefixes share nodes.
     *
     * Invariant
     * ---------
     * Current node always represents the prefix processed so far.
     *
     * Every processed character advances exactly one edge.
     *
     * Missing edge immediately proves that the requested prefix
     * does not exist.
     *
     * Limitation Fixed
     * ----------------
     * Prefix queries become proportional only to query length.
     *
     * Time
     * ----
     * insert      O(L)
     * search      O(L)
     * startsWith  O(L)
     *
     * Space
     * -----
     * O(total inserted characters)
     *
     * Interview Preference
     * --------------------
     * This is the expected solution.
     */

    /**
     * ==============================================================
     * Trie Node
     *                     TrieNode
     *       +--------------------------------------+
     *       | children[0]  -> child for 'a'        |
     *       | children[1]  -> child for 'b'        |
     *       | children[2]  -> child for 'c'        |
     *       | ...                                  |
     *       | children[15] -> child for 'p'        |
     *       | ...                                  |
     *       | children[25] -> child for 'z'        |
     *       | isWord                               |
     *       +--------------------------------------+
     *
     * Every child is again another TrieNode with
     * its own 26-child array.
     *
     * TrieNode
     *     ↓
     * 26 pointers
     *     ↓
     * TrieNode
     *     ↓
     * 26 pointers
     *     ↓
     * TrieNode
     *     ...
     * ==============================================================
     */


    static class TrieNode {

        TrieNode[] children = new TrieNode[26];

        boolean isWord;

    }

    static class Trie {

        private final TrieNode root;

        public Trie() {

            root = new TrieNode();

        }

        /**
         * Insert a word.
         */
        public void insert(String word) {

            // Invariant:
            // current always equals processed prefix.

            TrieNode current = root;

            for (char ch : word.toCharArray()) {

                int index = ch - 'a';

                // Prefix does not exist yet.
                // Extend the trie.
                if (current.children[index] == null) {

                    current.children[index] = new TrieNode();

                }

                // Move one character deeper.
                current = current.children[index];

            }

            // Entire word processed.
            // Mark terminal node.
            current.isWord = true;

        }

        /**
         * Search an exact word.
         */
        public boolean search(String word) {

            // Follow exactly the same path
            // created during insertion.

            TrieNode current = root;

            for (char ch : word.toCharArray()) {

                int index = ch - 'a';

                // Missing edge.
                // Word cannot exist.
                if (current.children[index] == null) {

                    return false;

                }

                current = current.children[index];

            }

            // IMPORTANT:
            // Path existence alone is insufficient.
            // Terminal flag distinguishes
            // "app" from "apple".

            return current.isWord;

        }

        /**
         * Search only the prefix.
         */
        public boolean startsWith(String prefix) {

            TrieNode current = root;

            for (char ch : prefix.toCharArray()) {

                int index = ch - 'a';

                // Prefix path breaks.
                if (current.children[index] == null) {

                    return false;

                }

                current = current.children[index];

            }

            // Entire prefix exists.
            return true;

        }

    }

/**
 * ==============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ==============================================================
 *
 * Explain the Invariant
 * ---------------------
 *
 * Every node represents exactly one prefix.
 *
 * During traversal,
 * current always equals the prefix processed so far.
 *
 * --------------------------------------------------------------
 * Why insert works
 * --------------------------------------------------------------
 *
 * Whenever an edge is missing,
 * we create it.
 *
 * Existing paths remain untouched,
 * so previously inserted words stay valid.
 *
 * --------------------------------------------------------------
 * Why search works
 * --------------------------------------------------------------
 *
 * Missing edge immediately proves
 * that no inserted word can contain
 * that prefix.
 *
 * After reaching the final node,
 * we still verify isWord,
 * because prefixes are not necessarily
 * complete words.
 *
 * --------------------------------------------------------------
 * Why startsWith works
 * --------------------------------------------------------------
 *
 * Prefix queries care only about
 * path existence.
 *
 * Terminal marker is irrelevant.
 *
 * --------------------------------------------------------------
 * Correctness Guarantee
 * --------------------------------------------------------------
 *
 * Every inserted character creates
 * exactly one corresponding edge.
 *
 * Every search follows exactly
 * the same deterministic path.
 *
 * Therefore,
 * successful traversal plus terminal flag
 * is equivalent to exact-word existence.
 *
 * --------------------------------------------------------------
 * What breaks if terminal flag is removed?
 * --------------------------------------------------------------
 *
 * insert("apple")
 *
 * search("app")
 *
 * would incorrectly return true.
 *
 * --------------------------------------------------------------
 * In-place feasibility
 * --------------------------------------------------------------
 *
 * Not applicable.
 *
 * Trie is a separate indexing structure.
 *
 * --------------------------------------------------------------
 * Streaming feasibility
 * --------------------------------------------------------------
 *
 * Excellent.
 *
 * New words can be inserted online
 * without rebuilding existing data.
 *
 * --------------------------------------------------------------
 * When NOT to use Trie
 * --------------------------------------------------------------
 *
 * Small datasets.
 *
 * Large alphabets with sparse prefixes.
 *
 * Memory-constrained systems.
 */

    /**
     * ==============================================================
     * 🎯 INTERVIEW RECALL SHEET (30-SECOND RECALL)
     * ==============================================================
     *
     * Pattern Trigger
     * ---------------
     * Prefix queries.
     * Dictionary.
     * Autocomplete.
     * Spell checker.
     *
     * Core Invariant
     * --------------
     * One node == one prefix.
     *
     * Search Target
     * -------------
     * Follow one edge per character.
     *
     * Discard Rule
     * ------------
     * Missing edge immediately proves
     * the requested prefix cannot exist.
     *
     * Common Trap
     * -----------
     * Forgetting isWord.
     *
     * Edge Cases
     * ----------
     * Prefix equals complete word.
     * Prefix longer than any stored word.
     * Shared prefixes.
     * Duplicate insertion.
     *
     * Interview One-Liner
     * -------------------
     * "I index prefixes instead of whole strings."
     *
     * Re-Derivation Cue
     * -----------------
     * Root
     * ↓
     * Character
     * ↓
     * Child
     * ↓
     * Repeat
     * ↓
     * Terminal?
     *
     * ==============================================================
     * 🔄 VARIATIONS & TWEAKS
     * ==============================================================
     *
     * Variation 1
     * -----------
     * Count how many words share a prefix.
     *
     * Add
     *
     * prefixCount
     *
     * to every node.
     *
     * Invariant
     * ---------
     * Every visited node increments count
     * during insertion.
     *
     * --------------------------------------------------------------
     * Variation 2
     * --------------------------------------------------------------
     *
     * Count duplicate words.
     *
     * Replace
     *
     * boolean isWord
     *
     * with
     *
     * int wordCount
     *
     * --------------------------------------------------------------
     * Variation 3
     * --------------------------------------------------------------
     *
     * Delete words.
     *
     * Need reference counts
     * so shared prefixes survive.
     *
     * --------------------------------------------------------------
     * Variation 4
     * --------------------------------------------------------------
     *
     * Unicode support.
     *
     * Replace fixed array
     * with HashMap<Character, TrieNode>.
     *
     * Same invariant.
     *
     * --------------------------------------------------------------
     * Pattern Break Signals
     * --------------------------------------------------------------
     *
     * Need sorted iteration.
     *
     * Need substring search.
     *
     * Need edit distance.
     *
     * Trie alone is insufficient.
     *
     * ==============================================================
     * ⚫ REINFORCEMENT PROBLEM 1
     * ==============================================================
     *
     * LeetCode 211
     * Design Add and Search Words Data Structure
     *
     * Summary
     * -------
     * Trie supporting '.'
     * wildcard.
     *
     * Example
     *
     * addWord("bad")
     *
     * search(".ad")
     *
     * true
     *
     * Mapping
     * -------
     * Same Trie.
     *
     * New invariant:
     * '.' may explore every child.
     */

    static class WordDictionary {

        static class Node {

            Node[] children = new Node[26];

            boolean isWord;

        }

        private final Node root = new Node();

        public void addWord(String word) {

            Node current = root;

            for (char ch : word.toCharArray()) {

                int index = ch - 'a';

                if (current.children[index] == null) {

                    current.children[index] = new Node();

                }

                current = current.children[index];

            }

            current.isWord = true;

        }

        public boolean search(String word) {

            return dfs(word, 0, root);

        }

        private boolean dfs(String word,
                            int index,
                            Node current) {

            if (current == null) {

                return false;

            }

            if (index == word.length()) {

                return current.isWord;

            }

            char ch = word.charAt(index);

            if (ch == '.') {

                for (Node child : current.children) {

                    if (dfs(word,
                            index + 1,
                            child)) {

                        return true;

                    }

                }

                return false;

            }

            return dfs(word,
                    index + 1,
                    current.children[ch - 'a']);

        }

    }

    /**
     * Reinforcement Mapping
     * ---------------------
     *
     * Same invariant:
     * Node == prefix.
     *
     * Wildcard temporarily branches,
     * but every recursive call
     * still represents one prefix.
     *
     * Edge Cases
     * ----------
     * Multiple dots.
     * Dot at beginning.
     * Dot at end.
     *
     * Interview Trap
     * --------------
     * Forgetting to stop at
     * index == word.length().
     *
     * Interview Articulation
     * ----------------------
     * Wildcard changes branching,
     * not the prefix invariant.
     *
     * ==============================================================
     * ⚫ REINFORCEMENT PROBLEM 2
     * ==============================================================
     *
     * LeetCode 648
     * Replace Words
     *
     * Summary
     * -------
     * Replace each word
     * with the shortest matching root.
     *
     * Example
     *
     * Dictionary
     *
     * cat
     * bat
     * rat
     *
     * Sentence
     *
     * cattle was rattled
     *
     * becomes
     *
     * cat was rat
     *
     * Mapping
     * -------
     * Walk until
     * first terminal node.
     */

    static class ReplaceWords {

        static class Node {

            Node[] children = new Node[26];

            boolean isWord;

        }

        private final Node root = new Node();

        private void insert(String word) {

            Node current = root;

            for (char ch : word.toCharArray()) {

                int index = ch - 'a';

                if (current.children[index] == null) {

                    current.children[index] = new Node();

                }

                current = current.children[index];

            }

            current.isWord = true;

        }

        public String replaceWords(List<String> dictionary,
                                   String sentence) {

            for (String word : dictionary) {

                insert(word);

            }

            StringBuilder answer = new StringBuilder();

            String[] words = sentence.split(" ");

            for (int i = 0; i < words.length; i++) {

                if (i > 0) {

                    answer.append(' ');

                }

                answer.append(findRoot(words[i]));

            }

            return answer.toString();

        }

        private String findRoot(String word) {

            Node current = root;

            StringBuilder prefix = new StringBuilder();

            for (char ch : word.toCharArray()) {

                int index = ch - 'a';

                if (current.children[index] == null) {

                    return word;

                }

                prefix.append(ch);

                current = current.children[index];

                if (current.isWord) {

                    return prefix.toString();

                }

            }

            return word;

        }

    }

    /**
     * Reinforcement Mapping
     * ---------------------
     *
     * Same invariant:
     * Every visited node represents the current prefix.
     *
     * Difference from LeetCode 208
     * ----------------------------
     * Instead of asking "does this word exist?",
     * we stop at the FIRST terminal node because
     * the shortest valid root is required.
     *
     * Edge Cases
     * ----------
     * No matching root.
     * Multiple possible roots.
     * Word itself is already a root.
     *
     * Interview Trap
     * --------------
     * Continuing traversal after reaching the
     * first terminal node returns a longer root,
     * violating the problem requirement.
     *
     * Interview Articulation
     * ----------------------
     * The Trie invariant is unchanged.
     * Only the stopping condition changes.
     *
     * ==============================================================
     * ⚫ REINFORCEMENT PROBLEM 3
     * ==============================================================
     *
     * LeetCode 820
     * Short Encoding of Words
     *
     * Summary
     * -------
     * Encode multiple words by sharing common suffixes.
     *
     * Example
     * -------
     * time
     * me
     * bell
     *
     * Encoding:
     * time#bell#
     *
     * Mapping
     * -------
     * Reverse every word before insertion.
     * Shared suffixes become shared prefixes.
     */

    static class MinimumEncoding {

        static class Node {

            Node[] children = new Node[26];

        }

        private final Node root = new Node();

        public int minimumLengthEncoding(String[] words) {

            Arrays.sort(words,
                    (a, b) -> Integer.compare(b.length(), a.length()));

            Set<String> inserted = new HashSet<>();

            int answer = 0;

            for (String word : words) {

                if (inserted.contains(word)) {
                    continue;
                }

                if (insertReverse(word)) {

                    answer += word.length() + 1;
                }

                inserted.add(word);

            }

            return answer;

        }

        /**
         * Returns true if new nodes were created.
         * If no new node is created,
         * this word is already represented.
         */
        private boolean insertReverse(String word) {

            Node current = root;

            boolean created = false;

            for (int i = word.length() - 1; i >= 0; i--) {

                int index = word.charAt(i) - 'a';

                if (current.children[index] == null) {

                    current.children[index] = new Node();

                    created = true;

                }

                current = current.children[index];

            }

            return created;

        }

    }

    /**
     * Reinforcement Mapping
     * ---------------------
     *
     * Same invariant.
     *
     * Prefix Trie
     * becomes
     *
     * Suffix Trie
     *
     * by reversing insertion order.
     *
     * Edge Cases
     * ----------
     * Duplicate words.
     * One word completely contains another suffix.
     *
     * Interview Trap
     * --------------
     * Forgetting to sort by decreasing length.
     *
     * ==============================================================
     * 🧩 RELATED PROBLEM 1
     * ==============================================================
     *
     * LeetCode 14
     * Longest Common Prefix
     *
     * Modified Invariant
     * ------------------
     * Continue walking while
     * exactly one child exists
     * and node is not terminal.
     */

    static class LongestCommonPrefix {

        public String longestCommonPrefix(String[] strs) {

            if (strs == null || strs.length == 0) {
                return "";
            }

            Trie trie = new Trie();

            for (String word : strs) {
                trie.insert(word);
            }

            TrieNode current = trie.root;

            StringBuilder answer = new StringBuilder();

            while (true) {

                int childCount = 0;
                int childIndex = -1;

                for (int i = 0; i < 26; i++) {

                    if (current.children[i] != null) {

                        childCount++;
                        childIndex = i;

                    }

                }

                if (childCount != 1 || current.isWord) {
                    break;
                }

                answer.append((char) ('a' + childIndex));
                current = current.children[childIndex];

            }

            return answer.toString();

        }

    }

    /**
     * Same / Modified / Broken Invariant
     * ----------------------------------
     * Modified.
     *
     * Node still represents one prefix,
     * but traversal stops when
     * branching begins.
     *
     * Edge Case
     * ---------
     * One word is prefix of another.
     *
     * Interview Note
     * --------------
     * Branching destroys commonality.
     *
     * ==============================================================
     * 🧩 RELATED PROBLEM 2
     * ==============================================================
     *
     * LeetCode 720
     * Longest Word in Dictionary
     *
     * Modified Invariant
     * ------------------
     * Every prefix must itself
     * be a valid word.
     */

    static class LongestWordDictionary {

        public String longestWord(String[] words) {

            Trie trie = new Trie();

            for (String word : words) {
                trie.insert(word);
            }

            String best = "";

            for (String word : words) {

                if (valid(word, trie)) {

                    if (word.length() > best.length()
                            || (word.length() == best.length()
                            && word.compareTo(best) < 0)) {

                        best = word;

                    }

                }

            }

            return best;

        }

        private boolean valid(String word,
                              Trie trie) {

            TrieNode current = trie.root;

            for (char ch : word.toCharArray()) {

                current = current.children[ch - 'a'];

                if (!current.isWord) {
                    return false;
                }

            }

            return true;

        }

    }


    /**
     * Same / Modified / Broken Invariant
     * ----------------------------------
     * Modified.
     *
     * Every intermediate prefix
     * must also terminate a valid word.
     *
     * Example
     * -------
     * a
     * ap
     * app
     * appl
     * apple
     *
     * Valid.
     *
     * Missing "ap"
     * immediately breaks the invariant.
     *
     * Edge Case
     * ---------
     * Multiple words with identical length.
     *
     * Interview Note
     * --------------
     * The traversal is identical to search(),
     * but every intermediate node must satisfy
     * isWord == true.
     *
     * ==============================================================
     * 🧩 RELATED PROBLEM 3
     * ==============================================================
     *
     * LeetCode 1268
     * Search Suggestions System
     *
     * Modified Invariant
     * ------------------
     * First locate the prefix node.
     * Then enumerate words below it.
     */

    static class SearchSuggestions {

        static class Node {

            Node[] children = new Node[26];

            boolean isWord;

        }

        private final Node root = new Node();

        public void insert(String word) {

            Node current = root;

            for (char ch : word.toCharArray()) {

                int index = ch - 'a';

                if (current.children[index] == null) {

                    current.children[index] = new Node();

                }

                current = current.children[index];

            }

            current.isWord = true;

        }

        public List<String> suggestions(String prefix) {

            List<String> answer = new ArrayList<>();

            Node current = root;

            for (char ch : prefix.toCharArray()) {

                current = current.children[ch - 'a'];

                if (current == null) {

                    return answer;

                }

            }

            dfs(current,
                    new StringBuilder(prefix),
                    answer);

            return answer;

        }

        private void dfs(Node node,
                         StringBuilder path,
                         List<String> answer) {

            if (node == null || answer.size() == 3) {
                return;
            }

            if (node.isWord) {

                answer.add(path.toString());

            }

            for (int i = 0; i < 26 && answer.size() < 3; i++) {

                if (node.children[i] != null) {

                    path.append((char) ('a' + i));

                    dfs(node.children[i],
                            path,
                            answer);

                    path.deleteCharAt(path.length() - 1);

                }

            }

        }

    }

    /**
     * Same / Modified / Broken Invariant
     * ----------------------------------
     * Same.
     *
     * Enumeration begins only after
     * the requested prefix node is found.
     *
     * Edge Case
     * ---------
     * Prefix absent.
     *
     * Interview Note
     * --------------
     * Search phase and enumeration phase
     * are independent.
     *
     * ==============================================================
     * 🧠 MASTERY CHECKLIST
     * ==============================================================
     *
     * □ What is the invariant?
     *
     * Every node uniquely represents one prefix.
     *
     * --------------------------------------------------------------
     * □ What is the search target?
     *
     * The node representing the entire query.
     *
     * --------------------------------------------------------------
     * □ What is the discard rule?
     *
     * Missing edge immediately proves the
     * prefix cannot exist.
     *
     * --------------------------------------------------------------
     * □ Why does search() need isWord?
     *
     * Paths are prefixes.
     * Prefixes are not necessarily words.
     *
     * --------------------------------------------------------------
     * □ Why does startsWith() ignore isWord?
     *
     * It only asks whether the prefix exists.
     *
     * --------------------------------------------------------------
     * □ Why does insert() never overwrite?
     *
     * Prefixes may be shared by many words.
     *
     * --------------------------------------------------------------
     * □ Termination logic?
     *
     * Stop after processing every character.
     *
     * --------------------------------------------------------------
     * □ Why do naive methods fail?
     *
     * Prefixes are not indexed.
     *
     * --------------------------------------------------------------
     * □ Edge cases?
     *
     * Shared prefixes.
     * Duplicate insertion.
     * Prefix equals complete word.
     * Prefix longer than stored words.
     *
     * --------------------------------------------------------------
     * □ Debugging readiness?
     *
     * Verify:
     *
     * current always equals processed prefix.
     *
     * Verify:
     *
     * search checks terminal marker.
     *
     * Verify:
     *
     * insert never replaces children.
     *
     * --------------------------------------------------------------
     * □ Variant readiness?
     *
     * Add frequency.
     * Delete words.
     * Wildcards.
     * Suggestions.
     * Replace words.
     *
     * --------------------------------------------------------------
     * □ Pattern boundary?
     *
     * Trie solves prefix indexing.
     *
     * It is not appropriate for
     * arbitrary substring search.
     *
     * ==============================================================
     * TEST UTILITIES
     * ==============================================================
     */

    static void assertTrue(boolean value,
                           String message) {

        if (!value) {

            throw new AssertionError(message);

        }

    }

    static void assertFalse(boolean value,
                            String message) {

        if (value) {

            throw new AssertionError(message);

        }

    }

    static void assertEquals(Object expected,
                             Object actual,
                             String message) {

        if (!Objects.equals(expected, actual)) {

            throw new AssertionError(
                    message
                            + "\nExpected : "
                            + expected
                            + "\nActual   : "
                            + actual
            );

        }

    }

    /**
     * ==============================================================
     * MAIN
     * ==============================================================
     *
     * Self-verifying tests.
     *
     * Every test exists to validate one invariant,
     * not merely to print output.
     */

    public static void main(String[] args) {

        /**
         * ----------------------------------------------------------
         * Happy Path
         * ----------------------------------------------------------
         *
         * Validates the official example.
         */
        Trie trie = new Trie();

        trie.insert("apple");

        assertTrue(
                trie.search("apple"),
                "Inserted word must be searchable."
        );

        assertFalse(
                trie.search("app"),
                "Prefix alone is not a complete word."
        );

        assertTrue(
                trie.startsWith("app"),
                "Existing prefix should be found."
        );

        trie.insert("app");

        assertTrue(
                trie.search("app"),
                "Inserted prefix must now become a word."
        );

        /**
         * ----------------------------------------------------------
         * Shared Prefix Invariant
         * ----------------------------------------------------------
         *
         * Existing paths must never be destroyed.
         */
        Trie shared = new Trie();

        shared.insert("car");
        shared.insert("cart");
        shared.insert("carbon");

        assertTrue(shared.search("car"),
                "Shortest word lost.");

        assertTrue(shared.search("cart"),
                "Shared path corrupted.");

        assertTrue(shared.search("carbon"),
                "Longest word lost.");

        /**
         * ----------------------------------------------------------
         * Missing Edge
         * ----------------------------------------------------------
         *
         * Missing child immediately proves failure.
         */
        assertFalse(
                shared.search("cat"),
                "Search crossed nonexistent edge."
        );

        /**
         * ----------------------------------------------------------
         * Prefix Longer Than Stored Word
         * ----------------------------------------------------------
         */
        assertFalse(
                shared.startsWith("carbons"),
                "Impossible prefix accepted."
        );

        /**
         * ----------------------------------------------------------
         * Duplicate Insertion
         * ----------------------------------------------------------
         *
         * Inserting the same word twice
         * must not break the structure.
         */
        Trie duplicate = new Trie();

        duplicate.insert("hello");
        duplicate.insert("hello");

        assertTrue(
                duplicate.search("hello"),
                "Duplicate insertion corrupted trie."
        );

        /**
         * ----------------------------------------------------------
         * Prefix vs Word
         * ----------------------------------------------------------
         */
        Trie prefix = new Trie();

        prefix.insert("abcd");

        assertTrue(
                prefix.startsWith("abc"),
                "Prefix lookup failed."
        );

        assertFalse(
                prefix.search("abc"),
                "Prefix incorrectly marked as word."
        );

        /**
         * ----------------------------------------------------------
         * Single Character
         * ----------------------------------------------------------
         */
        Trie single = new Trie();

        single.insert("a");

        assertTrue(
                single.search("a"),
                "Single-character word failed."
        );

        assertFalse(
                single.search("b"),
                "Unexpected word found."
        );

        /**
         * ----------------------------------------------------------
         * Reinforcement Test
         * ----------------------------------------------------------
         */
        WordDictionary wd = new WordDictionary();

        wd.addWord("bad");
        wd.addWord("dad");
        wd.addWord("mad");

        assertTrue(
                wd.search(".ad"),
                "Wildcard search failed."
        );

        assertFalse(
                wd.search("pad"),
                "Absent word returned true."
        );

        /**
         * ----------------------------------------------------------
         * Replace Words
         * ----------------------------------------------------------
         */
        ReplaceWords rw = new ReplaceWords();

        assertEquals(
                "the cat was rat by the bat",
                rw.replaceWords(
                        Arrays.asList("cat", "bat", "rat"),
                        "the cattle was rattled by the battery"
                ),
                "Shortest root replacement failed."
        );

        /**
         * ----------------------------------------------------------
         * Longest Common Prefix
         * ----------------------------------------------------------
         */
        LongestCommonPrefix lcp = new LongestCommonPrefix();

        assertEquals(
                "fl",
                lcp.longestCommonPrefix(
                        new String[]{
                                "flower",
                                "flow",
                                "flight"
                        }
                ),
                "Incorrect longest common prefix."
        );

        /**
         * ----------------------------------------------------------
         * Longest Word Dictionary
         * ----------------------------------------------------------
         */
        LongestWordDictionary longest =
                new LongestWordDictionary();

        assertEquals(
                "world",
                longest.longestWord(
                        new String[]{
                                "w",
                                "wo",
                                "wor",
                                "worl",
                                "world"
                        }
                ),
                "Every prefix should exist."
        );

        /**
         * ----------------------------------------------------------
         * Suggestions
         * ----------------------------------------------------------
         */
        SearchSuggestions ss = new SearchSuggestions();

        ss.insert("mobile");
        ss.insert("mouse");
        ss.insert("moneypot");
        ss.insert("monitor");
        ss.insert("mousepad");

        assertEquals(
                Arrays.asList(
                        "mobile",
                        "moneypot",
                        "monitor"
                ),
                ss.suggestions("mo"),
                "Suggestion traversal failed."
        );

        /**
         * ----------------------------------------------------------
         * Boundary Case
         * ----------------------------------------------------------
         *
         * Deep word.
         */
        Trie deep = new Trie();

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 2000; i++) {

            builder.append('a');

        }

        String longWord = builder.toString();

        deep.insert(longWord);

        assertTrue(
                deep.search(longWord),
                "Maximum-length word failed."
        );

        System.out.println();
        System.out.println("==================================================");
        System.out.println("All self-verifying tests passed.");
        System.out.println("Trie invariants preserved.");
        System.out.println("Interview implementation verified.");
        System.out.println("==================================================");
        System.out.println();

        System.out.println("FINAL CLOSURE");
        System.out.println("-----------------------------");
        System.out.println("I understand the invariant.");
        System.out.println("I can re-derive the solution.");
        System.out.println("I can physically reconstruct the implementation under pressure.");
        System.out.println("This chapter is complete.");
    }
}