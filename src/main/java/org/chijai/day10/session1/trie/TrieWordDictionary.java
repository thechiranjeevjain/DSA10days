package org.chijai.day10.session1.trie;

import java.util.*;

/**
 * ============================================================================
 * LEETCODE 211 — DESIGN ADD AND SEARCH WORDS DATA STRUCTURE
 * JAVA GOLD V4
 * ============================================================================
 *
 * OFFICIAL PROBLEM
 * ----------------
 *
 * Design a data structure supporting:
 *
 * addWord(String word)
 *      Add a lowercase English word.
 *
 * search(String word)
 *      Return true if any inserted word matches the query.
 *
 * The search query may contain:
 *
 * '.'
 *
 * which matches ANY SINGLE lowercase English letter.
 *
 * Example
 * -------
 *
 * WordDictionary dictionary = new WordDictionary();
 *
 * dictionary.addWord("bad");
 * dictionary.addWord("dad");
 * dictionary.addWord("mad");
 *
 * dictionary.search("pad");     // false
 * dictionary.search("bad");     // true
 * dictionary.search(".ad");     // true
 * dictionary.search("b..");     // true
 *
 * Constraints
 * -----------
 *
 * 1 <= word.length <= 25
 *
 * addWord()
 *      lowercase English letters only
 *
 * search()
 *      lowercase English letters or '.'
 *
 * At most 10^4 total calls.
 *
 * Search contains at most two dots.
 *
 * Difficulty
 * ----------
 * Medium
 *
 * Pattern
 * -------
 * Trie + wildcard DFS
 *
 * Foundation
 * ----------
 * LeetCode 208 — Implement Trie
 *
 * Official Link
 * -------------
 * https://leetcode.com/problems/design-add-and-search-words-data-structure/
 *
 */

public class TrieWordDictionary {

    /**
     * ============================================================================
     * 🎯 RECOGNITION CUE
     * ============================================================================
     *
     * Trigger:
     *
     *      many inserted words
     *      +
     *      '.' matches any ONE character
     *
     * Candidate:
     *
     *      Trie + wildcard DFS
     *
     * Fast re-derivation cue:
     *
     *      "LC 208 + branch on dot."
     *
     */

    /**
     * ============================================================================
     * ±Δ FROM TRIEPREFIX — LC 208 -> LC 211
     * ============================================================================
     *
     * This is the shortest way to re-derive the problem.
     *
     * TriePrefix / LC 208 already gives us:
     *
     *      children[26]
     *      isWord
     *      insert()
     *      exact search
     *
     * The ONLY new search rule is:
     *
     *      '.' matches ANY ONE character
     *
     * So the conceptual delta is:
     *
     *      normal letter
     *          -> exactly ONE legal child
     *
     *      '.'
     *          -> up to 26 legal children
     *
     * Nothing about the Trie itself changes.
     * Only the number of possible continuations changes.
     *
     * ---------------------------------------------------------------------------
     * WHY USE DFS FOR EVERY CHARACTER THEN?
     * ---------------------------------------------------------------------------
     *
     * We do NOT need branching for a normal letter.
     *
     * But a uniform recursive helper saves duplicated traversal logic:
     *
     *      normal letter -> DFS with branching factor 1
     *      '.'           -> DFS with branching factor up to 26
     *
     * When there is no '.', recursion behaves exactly like the LC 208 loop:
     *
     *      one node
     *          -> one child
     *              -> one child
     *                  -> one child
     *
     * There is no search-tree branching.
     *
     * The recursive form keeps these rules in ONE place:
     *
     *      null handling
     *      end-of-pattern handling
     *      index advancement
     *      terminal isWord validation
     *      remaining suffix traversal
     *
     * ---------------------------------------------------------------------------
     * PRIMARY STATE
     * ---------------------------------------------------------------------------
     *
     *      dfs(node, word, index)
     *
     * means:
     *
     *      every character before index has already matched,
     *      node represents that matched prefix,
     *      word[index] is the next pattern character to process.
     *
     * ---------------------------------------------------------------------------
     * ALTERNATIVE IMPLEMENTATION
     * ---------------------------------------------------------------------------
     *
     * We can also preserve the LC 208 loop and recurse only when '.' appears.
     * That version is kept as Primary Solution 2 because it makes the delta
     * from TriePrefix visually obvious.
     *
     * ---------------------------------------------------------------------------
     * API DIFFERENCE
     * ---------------------------------------------------------------------------
     *
     * TriePrefix / LC 208
     *
     *      insert(word)
     *      search(word)
     *      startsWith(prefix)
     *
     * TrieWordDictionary / LC 211
     *
     *      addWord(word)
     *      search(pattern)
     *
     * startsWith() disappears from the required API.
     * Wildcard search is the new behavior.
     *
     */

    /**
     * ============================================================================
     * 🧠 FIRST-PRINCIPLES INVENTION PATH
     * ============================================================================
     *
     * Step 1 — Start from LC 208
     * --------------------------
     *
     * Exact Trie search means:
     *
     *      known character
     *          -> exactly one child
     *
     *      missing child
     *          -> impossible path
     *
     *      all characters consumed
     *          -> answer is final node.isWord
     *
     *
     * Step 2 — Ask what wording changed
     * ----------------------------------
     *
     * LC 211 adds only:
     *
     *      '.' matches any single lowercase letter.
     *
     * Therefore a dot has MANY possible next children instead of one.
     *
     *
     * Step 3 — What state must one search call know?
     * ------------------------------------------------
     *
     *      node
     *          current matched Trie prefix
     *
     *      index
     *          next pattern character to process
     *
     * So:
     *
     *      dfs(node, word, index)
     *
     *
     * Step 4 — Define the two moves
     * --------------------------------
     *
     * Normal character:
     *
     *      recurse to exactly ONE child
     *
     * Dot:
     *
     *      recurse to EVERY existing child
     *
     * Both consume exactly one pattern character:
     *
     *      index + 1
     *
     *
     * Step 5 — Why recursion for normal letters too?
     * ------------------------------------------------
     *
     * We could keep a loop for normal letters.
     *
     * But the fully recursive form reuses the same continuation logic.
     * A normal character is simply a DFS state with one legal branch.
     *
     * That avoids maintaining two traversal mechanisms in the primary code.
     *
     *
     * Step 6 — Base cases
     * -------------------
     *
     * Dead path:
     *
     *      node == null
     *          -> false
     *
     * Pattern consumed:
     *
     *      index == word.length()
     *          -> node.isWord
     *
     *
     * Step 7 — Why can wildcard search stop early?
     * ------------------------------------------------
     *
     * search() asks whether ANY matching word exists.
     *
     * Therefore wildcard children are combined with OR:
     *
     *      first successful child -> return true
     *
     */

    /**
     * ============================================================================
     * ✅ PRIMARY SOLUTION 1 — UNIFORM RECURSIVE DFS
     * ============================================================================
     *
     * Interview-preferred implementation for this file.
     *
     * Why this is primary:
     *
     *      one search mechanism handles both ordinary letters and wildcard dots,
     *      so continuation logic is not duplicated.
     *
     * Mental model:
     *
     *      normal letter -> branching factor 1
     *      '.'           -> branching factor up to 26
     *
     * Core invariant
     * --------------
     *
     *      dfs(node, word, index)
     *
     * means:
     *
     *      every character before index has already matched,
     *      and node represents exactly that matched prefix.
     *
     */

    static class TrieNode {

        TrieNode[] children = new TrieNode[26];

        boolean isWord;

    }

    static class WordDictionary {

        private final TrieNode root = new TrieNode();

        public void addWord(String word) {

            TrieNode current = root;

            for (char ch : word.toCharArray()) {

                int index = ch - 'a';

                if (current.children[index] == null) {

                    current.children[index] = new TrieNode();

                }

                current = current.children[index];

            }

            current.isWord = true;

        }

        public boolean search(String word) {

            return dfs(root, word, 0);

        }

        private boolean dfs(TrieNode node,
                            String word,
                            int index) {

            if (node == null) {

                return false;

            }

            if (index == word.length()) {

                return node.isWord;

            }

            char ch = word.charAt(index);

            if (ch != '.') {

                return dfs(
                        node.children[ch - 'a'],
                        word,
                        index + 1
                );

            }

            for (TrieNode child : node.children) {

                if (child != null
                        && dfs(child, word, index + 1)) {

                    return true;

                }

            }

            return false;

        }

    }

    /**
     * ============================================================================
     * ✅ PRIMARY SOLUTION 2 — LC 208 LOOP + RECURSE ONLY AT DOT
     * ============================================================================
     *
     * Same algorithmic idea, different implementation style.
     *
     * This version keeps ordinary Trie traversal iterative
     * and introduces recursion only where '.' creates alternatives.
     *
     * Why keep it:
     *
     *      it is the easiest visual derivation directly from TriePrefix / LC 208.
     *
     * Trade-off:
     *
     *      clearer delta from LC 208,
     *      but two traversal mechanisms exist in the same helper:
     *      loop for normal characters + recursion for wildcard branches.
     *
     */

    static class WordDictionaryLoopThenDFS {

        private final TrieNode root = new TrieNode();

        public void addWord(String word) {

            TrieNode current = root;

            for (char ch : word.toCharArray()) {

                int index = ch - 'a';

                if (current.children[index] == null) {

                    current.children[index] = new TrieNode();

                }

                current = current.children[index];

            }

            current.isWord = true;

        }

        public boolean search(String word) {

            return searchFrom(root, word, 0);

        }

        private boolean searchFrom(TrieNode node,
                                   String word,
                                   int start) {

            TrieNode current = node;

            for (int i = start; i < word.length(); i++) {

                char ch = word.charAt(i);

                if (ch == '.') {

                    for (TrieNode child : current.children) {

                        if (child != null
                                && searchFrom(child, word, i + 1)) {

                            return true;

                        }

                    }

                    return false;

                }

                TrieNode child = current.children[ch - 'a'];

                if (child == null) {

                    return false;

                }

                current = child;

            }

            return current.isWord;

        }

    }

    /**
     * ============================================================================
     * 🧩 PRIMARY CODE — HARD LINES
     * ============================================================================
     *
     * 1.
     *
     *      return dfs(root, word, 0);
     *
     * Start from the empty prefix.
     * index 0 is the first pattern character.
     *
     *
     * 2.
     *
     *      if (node == null)
     *          return false;
     *
     * This centralizes missing-child handling.
     *
     * A normal character can recurse directly into a null child.
     * The next dfs() call rejects that dead path.
     *
     *
     * 3.
     *
     *      if (index == word.length())
     *          return node.isWord;
     *
     * Consuming the pattern proves only that a Trie path exists.
     * isWord proves that the path is a complete inserted word.
     *
     *
     * 4.
     *
     *      return dfs(node.children[ch - 'a'], word, index + 1);
     *
     * A normal character has exactly ONE legal continuation.
     *
     * This recursion is logically the same as one iteration of the LC 208 loop.
     * There is no branching here.
     *
     *
     * 5.
     *
     *      for (TrieNode child : node.children)
     *
     * '.' has no known child index.
     * Every existing child is a legal one-character match.
     *
     *
     * 6.
     *
     *      dfs(child, word, index + 1)
     *
     * The chosen child itself consumes the wildcard character,
     * so the next unresolved character is index + 1.
     *
     *
     * 7.
     *
     *      if (child != null && dfs(...))
     *          return true;
     *
     * Search is existential:
     * one successful wildcard branch is sufficient.
     *
     *
     * 8. Why no choose / un-choose backtracking?
     * -------------------------------------------
     *
     * dfs() does not mutate a shared path, board, visited set, or list.
     * Every recursive call receives only node, word, and index.
     * Nothing needs restoring.
     *
     */

    /**
     * ============================================================================
     * 👁 VISUAL DRY RUN
     * ============================================================================
     *
     * Insert:
     *
     *      bad
     *      dad
     *      mad
     *
     * --------------------------------------------------
     * SEARCH WITHOUT DOT
     * --------------------------------------------------
     *
     *      search("bad")
     *
     *      dfs(root, 0)
     *          b -> dfs(node("b"), 1)
     *          a -> dfs(node("ba"), 2)
     *          d -> dfs(node("bad"), 3)
     *          end -> node("bad").isWord
     *
     * There was exactly one recursive child at every step.
     *
     * So although the code says dfs(), it behaved exactly like a loop:
     *
     *      one node -> one child -> one child -> one child
     *
     * --------------------------------------------------
     * SEARCH WITH DOT
     * --------------------------------------------------
     *
     *      search("b.d")
     *
     * index = 0
     * ch = 'b'
     *
     *      one legal child
     *      -> node("b")
     *
     * index = 1
     * ch = '.'
     *
     * Now branching actually appears.
     * Try every existing child below "b".
     *
     *      a -> dfs(node("ba"), 2)
     *      ... other existing children if any
     *
     * On the 'a' branch:
     *
     * index = 2
     * ch = 'd'
     *
     *      one legal child
     *      -> node("bad")
     *
     * index = 3
     * pattern consumed
     *
     *      node("bad").isWord == true
     *
     * Therefore the wildcard branch returns true immediately.
     *
     * Mental picture:
     *
     *      normal char -> DFS behaves like loop
     *      '.'         -> DFS actually branches
     *
     */

    /**
     * ============================================================================
     * ⚖ COMPLEXITY — DERIVED
     * ============================================================================
     *
     * Let:
     *
     *      L = pattern / word length
     *      D = number of wildcard dots
     *      A = alphabet size = 26
     *
     * addWord
     * -------
     *
     * We process each of L characters exactly once.
     *
     *      Time = O(L)
     *
     *
     * search without '.'
     * ------------------
     *
     * Every recursive state has exactly one continuation.
     * Exactly L characters are consumed.
     *
     *      Time = O(L)
     *
     * The recursion is only an implementation form here;
     * there is no branching search tree.
     *
     *
     * search with '.'
     * ---------------
     *
     * A normal character creates one continuation.
     * A dot may create at most 26 continuations.
     *
     * With D dots, a loose worst-case branch bound is:
     *
     *      26^D
     *
     * Each branch consumes at most L characters.
     *
     * Simple upper bound:
     *
     *      O(26^D * L)
     *
     * Actual work is often much smaller because nonexistent children
     * terminate immediately.
     *
     *
     * Trie space
     * ----------
     *
     *      O(total inserted characters)
     *
     *
     * Recursive auxiliary space
     * -------------------------
     *
     * Primary Solution 1 recursively consumes one pattern character per call.
     * Maximum active depth is therefore L.
     *
     *      O(L)
     *
     * Primary Solution 2 keeps normal stretches iterative and only recurses
     * at wildcard branching points, but O(L) remains a safe general bound.
     *
     */

    /**
     * ============================================================================
     * 🔴 TRAPS / WRONG TURNS
     * ============================================================================
     *
     * Trap 1 — Treat '.' as a literal edge
     * -------------------------------------
     *
     * Wrong:
     *
     *      children['.' - 'a']
     *
     * '.' is not stored in the Trie.
     * It is a SEARCH instruction meaning:
     *
     *      try every possible one-character continuation.
     *
     *
     * Trap 2 — Restart from root after '.'
     * ------------------------------------
     *
     * Wildcard alternatives continue from the CURRENT matched prefix.
     *
     * Never restart from root.
     *
     *
     * Trap 3 — Recurse from i instead of i + 1
     * -------------------------------------------
     *
     * The chosen child already represents the ONE character matched by '.'.
     *
     * Therefore recursion must continue from:
     *
     *      i + 1
     *
     *
     * Trap 4 — Return true when pattern is merely consumed
     * ----------------------------------------------------
     *
     * Must still check:
     *
     *      node.isWord
     *
     *
     * Trap 5 — Generate all 26^D concrete strings first
     * -------------------------------------------------
     *
     * That creates many strings whose prefixes may not exist.
     *
     * Trie DFS instead branches only through actual children.
     *
     *
     * Trap 6 — Add StringBuilder to boolean search
     * ---------------------------------------------
     *
     * The boolean problem does not need the matched word itself.
     *
     * Adding a shared path introduces unnecessary mutable state
     * and unnecessary backtracking.
     *
     */

    /**
     * ============================================================================
     * 📈 APPROACH PROGRESSION
     * ============================================================================
     *
     * 1. Brute Force — List<String>
     * ----------------------------
     *
     * Store every word.
     *
     * Search every stored word position-by-position.
     *
     * addWord
     *      O(1)
     *
     * search
     *      O(N * L)
     *
     * Useful for deriving the matching rule.
     *
     */

    static class WordDictionaryBruteForce {

        private final List<String> words = new ArrayList<>();

        public void addWord(String word) {

            words.add(word);

        }

        public boolean search(String pattern) {

            for (String word : words) {

                if (word.length() != pattern.length()) {

                    continue;

                }

                boolean matches = true;

                for (int i = 0; i < word.length(); i++) {

                    char patternChar = pattern.charAt(i);

                    if (patternChar != '.'
                            && patternChar != word.charAt(i)) {

                        matches = false;
                        break;

                    }

                }

                if (matches) {

                    return true;

                }

            }

            return false;

        }

    }

    /**
     * 2. HashSet
     * ----------
     *
     * Exact lookup improves,
     * but wildcard lookup is still not directly indexed.
     *
     * Exact search:
     *
     *      O(L) expected because Java must hash the String.
     *
     * Wildcard search:
     *
     *      O(N * L)
     *
     * Remaining problem:
     *
     *      HashSet indexes whole strings.
     *      Trie indexes prefixes.
     *
     */

    static class WordDictionaryHashSet {

        private final Set<String> words = new HashSet<>();

        public void addWord(String word) {

            words.add(word);

        }

        public boolean search(String pattern) {

            if (pattern.indexOf('.') == -1) {

                return words.contains(pattern);

            }

            for (String word : words) {

                if (matches(word, pattern)) {

                    return true;

                }

            }

            return false;

        }

        private boolean matches(String word,
                                String pattern) {

            if (word.length() != pattern.length()) {

                return false;

            }

            for (int i = 0; i < word.length(); i++) {

                char patternChar = pattern.charAt(i);

                if (patternChar != '.'
                        && patternChar != word.charAt(i)) {

                    return false;

                }

            }

            return true;

        }

    }

    /**
     * 3. Trie + wildcard branching
     * ----------------------------
     *
     * Primary Solution 1 uses one uniform recursive search:
     * normal letter = one child, dot = many children.
     *
     * Primary Solution 2 preserves the LC 208 loop and recurses only at '.'.
     *
     * In both versions, wildcard branching happens only through
     * prefixes that actually exist.
     *
     */

    /**
     * ============================================================================
     * ±Δ FAMILY MAP — WHAT CHANGES, WHAT STAYS
     * ============================================================================
     *
     * Problem                            Delta from ordinary Trie
     * ---------------------------------------------------------------------------
     * LC 208 exact search                One edge per character; final isWord.
     * LC 208 startsWith                  One edge per character; ignore isWord.
     * LC 211 wildcard                    '.' branches to every existing child.
     * Return all wildcard matches        Explore ALL branches; no early true.
     * Count wildcard matches             SUM branch results instead of OR.
     * LC 648 shortest root               Stop at FIRST terminal node.
     * LC 212 Word Search II              State = board cell + Trie node.
     * LC 1268 suggestions                Find prefix node, then enumerate below.
     * Approximate / edit-distance match  Trie alone is insufficient.
     *
     * Stable invariant across the family:
     *
     *      Trie node represents a prefix.
     *
     * What usually changes:
     *
     *      branching
     *      stopping
     *      validation
     *      aggregation
     *
     */

    /**
     * ============================================================================
     * ⚫ REINFORCEMENT 1 — LC 208 IMPLEMENT TRIE
     * ============================================================================
     *
     * Purpose
     * -------
     *
     * Reconstruct the foundation implementation directly.
     *
     * The conceptual delta from LC 208 -> LC 211 is defined once near the top
     * in the "±Δ FROM TRIEPREFIX" section.
     *
     */

    static class ImplementTrie {

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

        public boolean search(String word) {

            Node current = walk(word);

            return current != null && current.isWord;

        }

        public boolean startsWith(String prefix) {

            return walk(prefix) != null;

        }

        private Node walk(String text) {

            Node current = root;

            for (char ch : text.toCharArray()) {

                int index = ch - 'a';

                if (current.children[index] == null) {

                    return null;

                }

                current = current.children[index];

            }

            return current;

        }

    }

    /**
     * ============================================================================
     * ⚫ REINFORCEMENT 2 — RETURN ALL MATCHING WORDS
     * ============================================================================
     *
     * Example
     * -------
     *
     * Insert:
     *
     *      bad
     *      dad
     *      mad
     *      bed
     *
     * searchAll(".ad")
     *
     * returns:
     *
     *      [bad, dad, mad]
     *
     * Delta from LC 211
     * -----------------
     *
     * Boolean search:
     *
     *      first success -> return true
     *
     * Return-all search:
     *
     *      continue every valid branch
     *
     * We now need the actual matched word,
     * so StringBuilder path becomes shared mutable state.
     *
     * Therefore:
     *
     *      choose
     *      explore
     *      un-choose
     *
     * is real backtracking here.
     *
     */

    static class WordDictionaryAllMatches {

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

        public List<String> searchAll(String pattern) {

            List<String> matches = new ArrayList<>();

            dfs(
                    root,
                    pattern,
                    0,
                    new StringBuilder(),
                    matches
            );

            return matches;

        }

        private void dfs(Node node,
                         String pattern,
                         int index,
                         StringBuilder path,
                         List<String> matches) {

            if (node == null) {

                return;

            }

            if (index == pattern.length()) {

                if (node.isWord) {

                    matches.add(path.toString());

                }

                return;

            }

            char currentChar = pattern.charAt(index);

            if (currentChar == '.') {

                for (int i = 0; i < 26; i++) {

                    Node child = node.children[i];

                    if (child == null) {

                        continue;

                    }

                    path.append((char) ('a' + i));

                    dfs(
                            child,
                            pattern,
                            index + 1,
                            path,
                            matches
                    );

                    path.deleteCharAt(path.length() - 1);

                }

                return;

            }

            Node child = node.children[currentChar - 'a'];

            if (child == null) {

                return;

            }

            path.append(currentChar);

            dfs(
                    child,
                    pattern,
                    index + 1,
                    path,
                    matches
            );

            path.deleteCharAt(path.length() - 1);

        }

    }

    /**
     * ============================================================================
     * ⚫ REINFORCEMENT 3 — COUNT MATCHING WORDS
     * ============================================================================
     *
     * Same:
     *
     *      Trie
     *      DFS state
     *      branching
     *
     * Only aggregation changes.
     *
     * Boolean:
     *
     *      OR
     *
     * Count:
     *
     *      SUM
     *
     */

    static class WordDictionaryCountMatches {

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

        public int countMatches(String pattern) {

            return dfs(root, pattern, 0);

        }

        private int dfs(Node node,
                        String pattern,
                        int index) {

            if (node == null) {

                return 0;

            }

            if (index == pattern.length()) {

                return node.isWord ? 1 : 0;

            }

            char currentChar = pattern.charAt(index);

            if (currentChar != '.') {

                return dfs(
                        node.children[currentChar - 'a'],
                        pattern,
                        index + 1
                );

            }

            int count = 0;

            for (Node child : node.children) {

                count += dfs(child, pattern, index + 1);

            }

            return count;

        }

    }

    /**
     * ============================================================================
     * 🧩 RELATED 1 — LC 212 WORD SEARCH II
     * ============================================================================
     *
     * Relationship
     * ------------
     *
     * LC 211:
     *
     *      state = (trieNode, stringIndex)
     *
     * LC 212:
     *
     *      state = (boardCell, trieNode)
     *
     * Trie still means:
     *
     *      matched dictionary prefix.
     *
     * Grid DFS supplies the next character.
     *
     */

    static class WordSearchII {

        static class Node {

            Node[] children = new Node[26];

            String word;

        }

        public List<String> findWords(char[][] board,
                                      String[] words) {

            Node root = buildTrie(words);

            List<String> answer = new ArrayList<>();

            for (int row = 0; row < board.length; row++) {

                for (int col = 0; col < board[0].length; col++) {

                    dfs(board, row, col, root, answer);

                }

            }

            return answer;

        }

        private Node buildTrie(String[] words) {

            Node root = new Node();

            for (String word : words) {

                Node current = root;

                for (char ch : word.toCharArray()) {

                    int index = ch - 'a';

                    if (current.children[index] == null) {

                        current.children[index] = new Node();

                    }

                    current = current.children[index];

                }

                current.word = word;

            }

            return root;

        }

        private void dfs(char[][] board,
                         int row,
                         int col,
                         Node node,
                         List<String> answer) {

            if (row < 0
                    || row >= board.length
                    || col < 0
                    || col >= board[0].length
                    || board[row][col] == '#') {

                return;

            }

            char ch = board[row][col];

            Node next = node.children[ch - 'a'];

            if (next == null) {

                return;

            }

            if (next.word != null) {

                answer.add(next.word);
                next.word = null;

            }

            board[row][col] = '#';

            dfs(board, row + 1, col, next, answer);
            dfs(board, row - 1, col, next, answer);
            dfs(board, row, col + 1, next, answer);
            dfs(board, row, col - 1, next, answer);

            board[row][col] = ch;

        }

    }

    /**
     * ============================================================================
     * 🧩 RELATED 2 — LC 648 REPLACE WORDS
     * ============================================================================
     *
     * Same prefix Trie.
     *
     * No wildcard branching.
     *
     * Key stopping rule:
     *
     *      FIRST terminal node wins
     *
     * because the shortest matching root is required.
     *
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

            for (String rootWord : dictionary) {

                insert(rootWord);

            }

            String[] words = sentence.split(" ");

            StringBuilder answer = new StringBuilder();

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

                current = current.children[index];
                prefix.append(ch);

                if (current.isWord) {

                    return prefix.toString();

                }

            }

            return word;

        }

    }

    /**
     * ============================================================================
     * 🧩 RELATED 3 — LC 1268 SEARCH SUGGESTIONS
     * ============================================================================
     *
     * Phase 1
     * -------
     *
     * Deterministically locate the requested prefix node.
     *
     * Phase 2
     * -------
     *
     * DFS below that node to enumerate suggestions.
     *
     * Compare with LC 211:
     *
     * LC 211 branches because the QUERY character is ambiguous.
     *
     * Suggestions branch only AFTER the prefix itself is known.
     *
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

            Node current = root;

            for (char ch : prefix.toCharArray()) {

                current = current.children[ch - 'a'];

                if (current == null) {

                    return new ArrayList<>();

                }

            }

            List<String> answer = new ArrayList<>();

            dfs(
                    current,
                    new StringBuilder(prefix),
                    answer
            );

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

                if (node.children[i] == null) {

                    continue;

                }

                path.append((char) ('a' + i));

                dfs(node.children[i], path, answer);

                path.deleteCharAt(path.length() - 1);

            }

        }

    }

    /**
     * ============================================================================
     * 🟣 INTERVIEW ARTICULATION
     * ============================================================================
     *
     * "I use the same Trie structure as LC 208.
     *
     * The only new behavior is the wildcard dot.
     * I model search as dfs(node, index), where node is the prefix already
     * matched and index is the next pattern character.
     *
     * For a normal character there is exactly one legal child, so the DFS has
     * branching factor one and behaves like the ordinary LC 208 loop.
     * For '.', every existing child is legal, so I branch across those children.
     *
     * Every recursive call consumes exactly one pattern character.
     * If the pattern is exhausted I return node.isWord, because path existence
     * alone can represent only a prefix.
     *
     * I use the uniform recursive form as the primary implementation because it
     * keeps null handling, termination, and continuation logic in one place."
     *
     */

    /**
     * ============================================================================
     * 🎯 30-SECOND RECALL
     * ============================================================================
     *
     * Foundation
     *      Same Trie as LC 208.
     *
     * State
     *      dfs(node, word, index)
     *      = prefix before index already matched.
     *
     * Normal letter
     *      recurse to ONE child.
     *      DFS behaves like a loop.
     *
     * Dot
     *      recurse to EVERY existing child.
     *      first success wins.
     *
     * Missing child
     *      node == null -> false.
     *
     * Pattern consumed
     *      return node.isWord.
     *
     * Why uniform DFS?
     *      same continuation logic for both cases;
     *      less duplicated traversal code.
     *
     * Alternative
     *      keep LC 208 loop and recurse only at '.'.
     *
     * Backtracking?
     *      No shared mutable state, so no un-choose step.
     *
     * Re-derivation cue
     *      "Normal char = one branch. Dot = many branches."
     *
     */

    /**
     * ============================================================================
     * 🧠 MASTERY CHECKLIST
     * ============================================================================
     *
     * □ Can I state the exact delta from LC 208?
     *
     * □ Can I define dfs(node, word, index)?
     *
     * □ Can I explain why a normal character is DFS with branching factor 1?
     *
     * □ Can I explain why that behaves like the old LC 208 loop?
     *
     * □ Can I explain exactly why '.' forces branching?
     *
     * □ Can I explain why every recursive call uses index + 1?
     *
     * □ Can I explain why node == null centralizes missing-child handling?
     *
     * □ Can I explain why node.isWord is required at the end?
     *
     * □ Can I explain why this is DFS but not backtracking?
     *
     * □ Can I derive O(L) when there is no wildcard?
     *
     * □ Can I derive the wildcard branching upper bound?
     *
     * □ Can I reconstruct the loop-then-DFS alternative from LC 208?
     *
     * □ Can I explain why uniform DFS removes duplicated traversal logic?
     *
     */

    /**
     * ============================================================================
     * TEST UTILITIES
     * ============================================================================
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
     * ============================================================================
     * MAIN — SELF-VERIFYING TESTS
     * ============================================================================
     */

    public static void main(String[] args) {

        WordDictionary dictionary = new WordDictionary();

        dictionary.addWord("bad");
        dictionary.addWord("dad");
        dictionary.addWord("mad");

        assertFalse(
                dictionary.search("pad"),
                "Unknown word should not exist."
        );

        assertTrue(
                dictionary.search("bad"),
                "Exact inserted word should exist."
        );

        assertTrue(
                dictionary.search(".ad"),
                "Wildcard at beginning should match."
        );

        assertTrue(
                dictionary.search("b.."),
                "Multiple wildcard positions should match."
        );

        WordDictionaryLoopThenDFS loopThenDFS =
                new WordDictionaryLoopThenDFS();

        loopThenDFS.addWord("bad");
        loopThenDFS.addWord("dad");
        loopThenDFS.addWord("mad");

        assertTrue(
                loopThenDFS.search(".ad"),
                "Loop-then-DFS primary alternative should match wildcard."
        );

        assertFalse(
                loopThenDFS.search("pad"),
                "Loop-then-DFS primary alternative should reject absent word."
        );

        WordDictionary prefixOnly = new WordDictionary();

        prefixOnly.addWord("badger");

        assertFalse(
                prefixOnly.search("bad"),
                "Prefix path must not count as a complete word."
        );

        assertTrue(
                prefixOnly.search("badger"),
                "Complete inserted word must match."
        );

        WordDictionary single = new WordDictionary();

        single.addWord("a");
        single.addWord("z");

        assertTrue(
                single.search("."),
                "Single wildcard should match one-character word."
        );

        assertFalse(
                single.search("b"),
                "Missing one-character word should fail."
        );

        WordDictionary sharedPrefix = new WordDictionary();

        sharedPrefix.addWord("app");
        sharedPrefix.addWord("apple");
        sharedPrefix.addWord("application");

        assertTrue(
                sharedPrefix.search("app"),
                "Inserted shared prefix should remain a complete word."
        );

        assertTrue(
                sharedPrefix.search("appl."),
                "Wildcard at end should match apple."
        );

        assertFalse(
                sharedPrefix.search("apply"),
                "Uninserted word should fail."
        );

        WordDictionary duplicate = new WordDictionary();

        duplicate.addWord("hello");
        duplicate.addWord("hello");

        assertTrue(
                duplicate.search("hello"),
                "Duplicate insertion must not corrupt Trie."
        );

        WordDictionary allDots = new WordDictionary();

        allDots.addWord("code");
        allDots.addWord("cope");
        allDots.addWord("cake");

        assertTrue(
                allDots.search("...."),
                "All-wildcard pattern should match same-length word."
        );

        assertFalse(
                allDots.search("....."),
                "Different-length wildcard pattern should fail."
        );

        ImplementTrie trie = new ImplementTrie();

        trie.insert("apple");

        assertTrue(
                trie.search("apple"),
                "LC 208 exact search failed."
        );

        assertFalse(
                trie.search("app"),
                "LC 208 prefix must not automatically be a word."
        );

        assertTrue(
                trie.startsWith("app"),
                "LC 208 startsWith failed."
        );

        WordDictionaryAllMatches allMatches = new WordDictionaryAllMatches();

        allMatches.addWord("bad");
        allMatches.addWord("dad");
        allMatches.addWord("mad");
        allMatches.addWord("bed");

        List<String> matches = allMatches.searchAll(".ad");

        Collections.sort(matches);

        assertEquals(
                Arrays.asList("bad", "dad", "mad"),
                matches,
                "Return-all wildcard variation failed."
        );

        WordDictionaryCountMatches countMatches =
                new WordDictionaryCountMatches();

        countMatches.addWord("bat");
        countMatches.addWord("cat");
        countMatches.addWord("rat");
        countMatches.addWord("bed");

        assertEquals(
                3,
                countMatches.countMatches(".at"),
                "Count-matches aggregation failed."
        );

        ReplaceWords replaceWords = new ReplaceWords();

        assertEquals(
                "the cat was rat by the bat",
                replaceWords.replaceWords(
                        Arrays.asList("cat", "bat", "rat"),
                        "the cattle was rattled by the battery"
                ),
                "Replace Words variation failed."
        );

        SearchSuggestions suggestions = new SearchSuggestions();

        suggestions.insert("mobile");
        suggestions.insert("mouse");
        suggestions.insert("moneypot");
        suggestions.insert("monitor");
        suggestions.insert("mousepad");

        assertEquals(
                Arrays.asList("mobile", "moneypot", "monitor"),
                suggestions.suggestions("mo"),
                "Search Suggestions variation failed."
        );

        WordSearchII wordSearch = new WordSearchII();

        char[][] board = {
                {'o', 'a', 'a', 'n'},
                {'e', 't', 'a', 'e'},
                {'i', 'h', 'k', 'r'},
                {'i', 'f', 'l', 'v'}
        };

        List<String> found = wordSearch.findWords(
                board,
                new String[]{
                        "oath",
                        "pea",
                        "eat",
                        "rain"
                }
        );

        Collections.sort(found);

        assertEquals(
                Arrays.asList("eat", "oath"),
                found,
                "Word Search II variation failed."
        );

        System.out.println();
        System.out.println("==================================================");
        System.out.println("JAVA GOLD V4 VERIFIED");
        System.out.println("All self-verifying tests passed.");
        System.out.println("Uniform Trie DFS + wildcard branching invariant preserved.");
        System.out.println("==================================================");

    }

}
