package org.chijai.day8.graph.session3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Word Break — V4 GRAPH-FIRST FINAL
 * LeetCode 139
 * https://leetcode.com/problems/word-break/
 *
 * ============================================================
 * CLASSIFICATION
 * ============================================================
 *
 * PRIMARY MENTAL MODEL
 *     Graph -> Implicit DAG -> Reachability
 *
 * EQUIVALENT MODEL
 *     DP -> 1D Prefix Reachability
 *
 * OPTIMIZATION FOLLOW-UP
 *     Trie -> faster outgoing-edge discovery
 *
 * Recognition trigger:
 *     "Can I consume valid pieces until I reach the end?"
 *
 * One-line anchor:
 *     String positions are nodes.
 *     A dictionary word s[start,end) is an edge start -> end.
 *     Ask whether 0 can reach n.
 *
 * ============================================================
 * PROBLEM
 * ============================================================
 *
 * Return true if the entire string can be segmented into dictionary
 * words. Words may be reused.
 *
 * We return only true/false — not the actual words.
 *
 * Example:
 *
 *     s = "leetcode"
 *     dict = ["leet", "code"]
 *
 *     0 --leet--> 4 --code--> 8
 *
 *     true
 *
 * ============================================================
 * FIRST-PRINCIPLES INVENTION PATH
 * ============================================================
 *
 * 1. Where am I?
 *      At a position in the string.
 *
 * 2. What does position i mean?
 *      Everything before i has already been segmented successfully.
 *
 * 3. What is a move?
 *      If s[i,j) is a dictionary word:
 *
 *          i ----word----> j
 *
 * 4. Start / target?
 *      0 / n
 *
 * 5. Final question?
 *      Can 0 reach n?
 *
 * That is graph reachability -> BFS or DFS.
 *
 * ------------------------------------------------------------
 * THE CRITICAL BFS INVARIANT
 * ------------------------------------------------------------
 *
 * Every index in the queue means:
 *
 *     "The ENTIRE prefix before this index is segmentable."
 *
 * Therefore this early return is safe:
 *
 *     reachable start
 *          +
 *     valid word start -> n
 *          =
 *     complete path 0 -> ... -> start -> n
 *
 * We are NOT returning because we found one arbitrary word.
 * We return because we found the FINAL word from an already-reachable
 * position to the end of the whole string.
 *
 * ------------------------------------------------------------
 * maxWordLength — RE-INVENT IT
 * ------------------------------------------------------------
 *
 * Let L = longest dictionary word length.
 * A word-edge starting at start can travel at most L characters:
 *
 *     furthestEnd = min(n, start + L)
 *
 * Reusable pruning rule:
 *
 *     hard domain bound -> bound the transition -> shrink the loop
 *
 * Do not memorize a formula. Ask:
 *     "How far can one legal move possibly go?"
 */
public class WordBreak {

    private static int maxWordLength(List<String> wordDict) {

        int max = 0;

        for (String word : wordDict) {
            max = Math.max(max, word.length());
        }

        return max;
    }

    /**
     * ============================================================
     * PRIMARY INTERVIEW SOLUTION — BFS + HASHSET
     * ============================================================
     *
     * Why primary here:
     *     - closest to the literal problem
     *     - easiest to reinvent
     *     - explores only reachable positions
     *     - naturally supports early success
     */
    static class PrimaryGraphBfs {

        public boolean wordBreak(String s, List<String> wordDict) {

            Set<String> dictionary = new HashSet<>(wordDict);
            int maxWordLength = maxWordLength(wordDict);

            Queue<Integer> queue = new ArrayDeque<>();
            // index are cut positions letters words are edges
            // n+1 means we have visited the end of the string
            boolean[] visited = new boolean[s.length() + 1];


            // standard bfs is we mark visited when we enqueue, not when we dequeue
            queue.offer(0);
            visited[0] = true;

            while (!queue.isEmpty()) {

                int start = queue.poll();

                int furthestEnd = Math.min(
                        s.length(),
                        start + maxWordLength
                );

                for (int end = start + 1;
                     end<= furthestEnd;
                     end++) {

                    // end is exclusive, so s[start,end) is the candidate word
                    // that's why end <= furthestEnd
                    if (!dictionary.contains(s.substring(start, end))) {
                        continue;
                    }

                    // start is already reachable.
                    // This valid word now reaches the target.
                    if (end == s.length()) {
                        return true;
                    }

                    if (!visited[end]) {
                        visited[end] = true;
                        queue.offer(end);
                    }
                }
            }

            return false;
        }
    }

    /**
     * ============================================================
     * BFS WORKED EXAMPLE — VISUAL TRAVERSAL
     * ============================================================
     *
     * Example:
     *
     *     s = "catsanddog"
     *     dict = ["cat", "cats", "and", "sand", "dog"]
     *
     * Positions are graph nodes. Valid word edges are:
     *
     *     0 --"cat"--> 3
     *     0 --"cats"-> 4
     *     3 --"sand"-> 7
     *     4 --"and"--> 7
     *     7 --"dog"--> 10
     *
     * Tree-shaped view of the exploration:
     *
     *     0
     *     |-- 3   via "cat"
     *     |   `-- 7   via "sand"
     *     |       `-- 10  via "dog"
     *     `-- 4   via "cats"
     *         `-- 7   via "and"
     *             `-- 10  via "dog"
     *
     * IMPORTANT:
     * This is really a GRAPH, not a pure tree. Both 3 and 4 reach 7.
     * visited[] prevents us from expanding the same graph state twice.
     *
     * Queue simulation:
     *
     *     start:
     *         queue   = [0]
     *         visited = {0}
     *
     *     pop 0:
     *         "cat"  -> 3
     *         "cats" -> 4
     *         queue = [3, 4]
     *
     *     pop 3:
     *         "sand" -> 7
     *         queue = [4, 7]
     *
     *     pop 4:
     *         "and" -> 7
     *         7 already visited -> do not enqueue again
     *         queue = [7]
     *
     *     pop 7:
     *         "dog" -> 10
     *         10 == s.length() -> return true
     *
     * Why is returning at 10 safe?
     *
     *     queue contains 7
     *         => prefix s[0,7) is already segmentable
     *
     *     "dog" gives edge 7 -> 10
     *         => complete path 0 -> ... -> 7 -> 10 exists
     *
     * We are not returning after finding one arbitrary word.
     * We return after finding the FINAL word from an already-reachable
     * position to the end.
     *
     * ------------------------------------------------------------
     * FAILURE EXAMPLE
     * ------------------------------------------------------------
     *
     *     s = "catsandog"
     *     dict = ["cat", "cats", "and", "sand", "dog"]
     *
     * Reachable positions:
     *
     *     0 -> 3 -> 7
     *      \-> 4 -> 7
     *
     * From 7, no dictionary word reaches n = 9.
     * Eventually the queue becomes empty.
     *
     *     queue empty -> every reachable state exhausted -> false
     *
     * Reusable BFS lesson:
     *     one branch failing is NOT failure;
     *     failure means no reachable state can reach the target.
     */

    /**
     * ============================================================
     * WHY visited[]?
     * ============================================================
     *
     * Different segmentations may reach the same index:
     *
     *     0 -> 1 -> 4
     *     0 -> 2 -> 4
     *
     * Once 4 is reached, its future depends only on index 4, not on
     * which path reached it. So expand each index at most once.
     *
     * ============================================================
     * COMPLEXITY / TRADE-OFFS — DERIVE, DON'T MEMORIZE
     * ============================================================
     *
     * Let:
     *     n = string length
     *     L = longest dictionary word length
     *     D = total characters across all dictionary words
     *
     * BFS + HashSet: GRAPH-LEVEL WORK
     *
     * 1. How many graph states can exist?
     *
     *        positions 0..n -> O(n)
     *
     * 2. How many times can one state be expanded?
     *
     *        once, because visited[] prevents re-enqueueing
     *
     * 3. From one start, how many candidate edges can we try?
     *
     *        at most L
     *        because no legal word-edge can be longer than L
     *
     * Therefore:
     *
     *        O(n) states * O(L) candidate edges/state
     *        = O(nL) candidate transitions
     *
     * Without maxWordLength pruning:
     *
     *        n + (n - 1) + ... + 1 = O(n^2) candidates
     *
     * ------------------------------------------------------------
     * STRICT JAVA substring() CAVEAT
     * ------------------------------------------------------------
     *
     * Modern Java substring(start,end) creates a new String and copies
     * characters. For one start we may build lengths 1..L:
     *
     *        1 + 2 + ... + L = O(L^2) character work
     *
     * Across O(n) reachable starts, this exact implementation can cost:
     *
     *        O(nL^2) worst-case character-copy/hash work
     *
     * Interview-level graph complexity is usually stated as O(nL)
     * candidate transitions; mention the Java substring caveat if pushed.
     *
     * ------------------------------------------------------------
     * SPACE
     * ------------------------------------------------------------
     *
     *     visited[] -> O(n)
     *     queue     -> O(n) worst case
     *
     * Auxiliary graph space:
     *
     *     O(n)
     *
     * HashSet dictionary storage:
     *
     *     O(D)
     *
     * Total including dictionary preprocessing:
     *
     *     O(n + D)
     *
     * ------------------------------------------------------------
     * BFS vs DP vs TRIE
     * ------------------------------------------------------------
     *
     * BFS + HashSet
     *     graph-level time: O(nL) candidate transitions
     *     strict Java substring work: up to O(nL^2)
     *     auxiliary space: O(n)
     *     strength: explores only reachable starts; early success natural
     *
     * Bottom-up DP + HashSet
     *     same asymptotic candidate-cut work and O(n) DP state
     *     difference is traversal direction, not a major complexity win
     *
     * BFS + Trie
     *     build Trie: O(D)
     *     traversal: O(nL) character walks
     *     total space: O(D + n)
     *     avoids temporary substring creation and stops at dead prefixes
     *     but costs extra code and memory
     *
     * For LC 139, BFS + HashSet is the 80/20 primary solution.
     * Trie stays only as an optional optimization follow-up.
     */

    /**
     * ============================================================
     * FOLLOW-UP — SAME GRAPH + TRIE EDGE DISCOVERY
     * ============================================================
     *
     * HashSet repeatedly asks:
     *
     *     "a"? "ap"? "app"? "appl"? "apple"?
     *
     * Trie walks those shared prefixes once:
     *
     *     root -> a -> p -> p* -> l -> e*
     *                        ^           ^
     *                       word        word
     *
     * Key Trie advantage:
     *
     *     If current characters are not even a dictionary PREFIX,
     *     break. Making that same dead prefix longer cannot recover.
     */
    static class TrieGraphBfs {

        static class TrieNode {
            TrieNode[] children = new TrieNode[26];
            boolean isWord;
        }

        public boolean wordBreak(String s, List<String> wordDict) {

            TrieNode root = buildTrie(wordDict);

            Queue<Integer> queue = new ArrayDeque<>();
            boolean[] visited = new boolean[s.length() + 1];

            queue.offer(0);
            visited[0] = true;

            while (!queue.isEmpty()) {

                int start = queue.poll();
                TrieNode node = root;

                for (int end = start; end < s.length(); end++) {

                    node = node.children[s.charAt(end) - 'a'];

                    if (node == null) {
                        break;
                    }

                    if (!node.isWord) {
                        continue;
                    }

                    int nextPosition = end + 1;

                    if (nextPosition == s.length()) {
                        return true;
                    }

                    if (!visited[nextPosition]) {
                        visited[nextPosition] = true;
                        queue.offer(nextPosition);
                    }
                }
            }

            return false;
        }

        private TrieNode buildTrie(List<String> wordDict) {

            TrieNode root = new TrieNode();

            for (String word : wordDict) {

                TrieNode node = root;

                for (char ch : word.toCharArray()) {

                    int index = ch - 'a';

                    if (node.children[index] == null) {
                        node.children[index] = new TrieNode();
                    }

                    node = node.children[index];
                }

                node.isWord = true;
            }

            return root;
        }
    }

    /**
     * ============================================================
     * SAME GRAPH WRITTEN AS BOTTOM-UP DP
     * ============================================================
     *
     * Graph:
     *     reachable start + valid edge(start,end) -> reachable end
     *
     * DP:
     *     dp[start] && word(start,end) -> dp[end] = true
     *
     * So dp[i] means the same core fact as graph visited/reachable i.
     *
     * BFS thinks forward:
     *     "I am at start. Where can I go?"
     *
     * Prefix DP thinks backward:
     *     "For this end, which reachable start can lead here?"
     */
    static class EquivalentBottomUpDp {

        public boolean wordBreak(String s, List<String> wordDict) {

            Set<String> dictionary = new HashSet<>(wordDict);
            int maxWordLength = maxWordLength(wordDict);

            boolean[] dp = new boolean[s.length() + 1];
            dp[0] = true;

            for (int end = 1; end <= s.length(); end++) {

                int earliestStart = Math.max(0, end - maxWordLength);

                for (int start = earliestStart; start < end; start++) {

                    if (dp[start]
                            && dictionary.contains(s.substring(start, end))) {

                        dp[end] = true;
                        break;
                    }
                }
            }

            return dp[s.length()];
        }
    }

    /**
     * ============================================================
     * SAME GRAPH AS MEMOIZED DFS
     * ============================================================
     *
     * Recursive idea:
     *     from start, try each valid next word and recurse from its end.
     *
     * Repeated question:
     *     "Can this same start index eventually reach n?"
     *
     * Cache that state -> memoized DFS.
     */
    static class MemoizedDfs {

        public boolean wordBreak(String s, List<String> wordDict) {

            Set<String> dictionary = new HashSet<>(wordDict);
            int maxWordLength = maxWordLength(wordDict);
            Map<Integer, Boolean> memo = new HashMap<>();

            return canReachEnd(
                    s,
                    0,
                    dictionary,
                    maxWordLength,
                    memo
            );
        }

        private boolean canReachEnd(
                String s,
                int start,
                Set<String> dictionary,
                int maxWordLength,
                Map<Integer, Boolean> memo
        ) {

            if (start == s.length()) {
                return true;
            }

            if (memo.containsKey(start)) {
                return memo.get(start);
            }

            int furthestEnd = Math.min(
                    s.length(),
                    start + maxWordLength
            );

            for (int end = start + 1; end <= furthestEnd; end++) {

                if (dictionary.contains(s.substring(start, end))
                        && canReachEnd(
                                s,
                                end,
                                dictionary,
                                maxWordLength,
                                memo
                        )) {

                    memo.put(start, true);
                    return true;
                }
            }

            memo.put(start, false);
            return false;
        }
    }

    /**
     * ============================================================
     * TRAPS + INTERVIEW ARTICULATION + RECALL
     * ============================================================
     *
     * GREEDY TRAP
     *
     *     s = "cars"
     *     dict = ["car", "ca", "rs"]
     *
     *     longest first: "car" + "s" -> fail
     *     valid path:     "ca" + "rs"
     *
     * NO visited / memo
     *     Same index can be recomputed through many paths.
     *
     * FOUND ONE WORD -> true
     *     Wrong unless that valid word starts from a reachable index AND
     *     ends exactly at n.
     *
     * BUILD FULL GRAPH FIRST
     *     Unnecessary. Generate edges only when exploring a state.
     *
     * ------------------------------------------------------------
     * SAY BEFORE CODING
     * ------------------------------------------------------------
     *
     * "I model each cut position as a graph node. From a reachable start,
     * if s[start,end) is a dictionary word, there is an edge to end. So I
     * BFS from 0, visit each position once, bound each outgoing scan by the
     * longest dictionary word, and return true when I discover n."
     *
     * ------------------------------------------------------------
     * 30-SECOND RECALL CARD
     * ------------------------------------------------------------
     *
     * node        = string index
     * edge        = dictionary word from start to end
     * start       = 0
     * target      = n
     * algorithm   = BFS reachable positions
     * invariant   = queued index => whole prefix before it is segmentable
     * pruning     = longest word L => end <= start + L
     * Trie        = shared-prefix edge discovery
     * DP          = dp[i] means node i is reachable
     */

    /**
     * ============================================================
     * HORIZONTAL MASTERY — SAME GRAPH, SMALL DELTAS
     * ============================================================
     *
     * Learn in this order:
     *
     * 1. Return one segmentation
     *      reachability + parent pointers
     *
     * 2. Count segmentations
     *      ANY path -> COUNT paths
     *
     * 3. Minimum words
     *      reachability -> shortest number of edges
     *
     * 4. Word Break II (LC 140)
     *      existence -> ALL paths
     *
     * 5. Extra Characters (LC 2707)
     *      add a skip edge costing 1
     *
     * 6. Decode Ways (LC 91)
     *      dictionary rule -> numeric token rule; COUNT paths
     *
     * FAMILY CARD
     *
     *     ANY PATH      -> boolean reachability
     *     ONE PATH      -> parent
     *     ALL PATHS     -> enumerate
     *     COUNT PATHS   -> SUM
     *     FEWEST EDGES  -> BFS / MIN
     *     NEW VALIDITY  -> change edge-generation rule
     */

    /** DELTA 1: return one valid segmentation. */
    static class ReturnOneSegmentation {

        public List<String> wordBreakOne(String s, List<String> wordDict) {

            Set<String> dictionary = new HashSet<>(wordDict);
            int maxWordLength = maxWordLength(wordDict);

            Queue<Integer> queue = new ArrayDeque<>();
            boolean[] visited = new boolean[s.length() + 1];
            int[] parent = new int[s.length() + 1];
            Arrays.fill(parent, -1);

            queue.offer(0);
            visited[0] = true;

            while (!queue.isEmpty()) {

                int start = queue.poll();
                int furthestEnd = Math.min(
                        s.length(),
                        start + maxWordLength
                );

                for (int end = start + 1; end <= furthestEnd; end++) {

                    if (!dictionary.contains(s.substring(start, end))) {
                        continue;
                    }

                    if (visited[end]) {
                        continue;
                    }

                    visited[end] = true;
                    parent[end] = start;

                    if (end == s.length()) {
                        return reconstruct(s, parent);
                    }

                    queue.offer(end);
                }
            }

            return List.of();
        }

        private List<String> reconstruct(String s, int[] parent) {

            List<String> words = new ArrayList<>();

            for (int end = s.length(); end > 0; end = parent[end]) {
                int start = parent[end];
                words.add(s.substring(start, end));
            }

            Collections.reverse(words);
            return words;
        }
    }

    /** DELTA 2: count all segmentations = count paths in the DAG. */
    static class CountSegmentations {

        public long count(String s, List<String> wordDict) {

            Set<String> dictionary = new HashSet<>(wordDict);
            int maxWordLength = maxWordLength(wordDict);

            long[] ways = new long[s.length() + 1];
            ways[0] = 1;

            for (int start = 0; start < s.length(); start++) {

                if (ways[start] == 0) {
                    continue;
                }

                int furthestEnd = Math.min(
                        s.length(),
                        start + maxWordLength
                );

                for (int end = start + 1; end <= furthestEnd; end++) {

                    if (dictionary.contains(s.substring(start, end))) {
                        ways[end] += ways[start];
                    }
                }
            }

            return ways[s.length()];
        }
    }

    /** DELTA 3: minimum words = shortest number of unit-cost graph edges. */
    static class MinimumWords {

        public int minWords(String s, List<String> wordDict) {

            Set<String> dictionary = new HashSet<>(wordDict);
            int maxWordLength = maxWordLength(wordDict);

            Queue<Integer> queue = new ArrayDeque<>();
            boolean[] visited = new boolean[s.length() + 1];

            queue.offer(0);
            visited[0] = true;

            int wordsUsed = 0;

            while (!queue.isEmpty()) {

                int levelSize = queue.size();
                wordsUsed++;

                for (int i = 0; i < levelSize; i++) {

                    int start = queue.poll();
                    int furthestEnd = Math.min(
                            s.length(),
                            start + maxWordLength
                    );

                    for (int end = start + 1; end <= furthestEnd; end++) {

                        if (!dictionary.contains(s.substring(start, end))) {
                            continue;
                        }

                        if (end == s.length()) {
                            return wordsUsed;
                        }

                        if (!visited[end]) {
                            visited[end] = true;
                            queue.offer(end);
                        }
                    }
                }
            }

            return -1;
        }
    }

    /** DELTA 4: LeetCode 140 — enumerate all segmentations. */
    static class WordBreakII {

        public List<String> wordBreak(String s, List<String> wordDict) {

            Set<String> dictionary = new HashSet<>(wordDict);
            int maxWordLength = maxWordLength(wordDict);

            return sentencesFrom(
                    s,
                    0,
                    dictionary,
                    maxWordLength,
                    new HashMap<>()
            );
        }

        private List<String> sentencesFrom(
                String s,
                int start,
                Set<String> dictionary,
                int maxWordLength,
                Map<Integer, List<String>> memo
        ) {

            if (memo.containsKey(start)) {
                return memo.get(start);
            }

            if (start == s.length()) {
                return List.of("");
            }

            List<String> answers = new ArrayList<>();
            int furthestEnd = Math.min(
                    s.length(),
                    start + maxWordLength
            );

            for (int end = start + 1; end <= furthestEnd; end++) {

                String word = s.substring(start, end);

                if (!dictionary.contains(word)) {
                    continue;
                }

                for (String suffix : sentencesFrom(
                        s,
                        end,
                        dictionary,
                        maxWordLength,
                        memo
                )) {

                    answers.add(
                            suffix.isEmpty()
                                    ? word
                                    : word + " " + suffix
                    );
                }
            }

            memo.put(start, answers);
            return answers;
        }
    }

    /**
     * DELTA 5: LeetCode 2707 — Extra Characters in a String.
     * Dictionary-word edge costs 0; skipping one character costs 1.
     */
    static class ExtraCharacters {

        public int minExtraChar(String s, String[] dictionaryWords) {

            Set<String> dictionary = new HashSet<>(
                    Arrays.asList(dictionaryWords)
            );

            int maxWordLength = 0;

            for (String word : dictionaryWords) {
                maxWordLength = Math.max(maxWordLength, word.length());
            }

            int[] minExtra = new int[s.length() + 1];

            for (int end = 1; end <= s.length(); end++) {

                minExtra[end] = minExtra[end - 1] + 1;
                int earliestStart = Math.max(0, end - maxWordLength);

                for (int start = earliestStart; start < end; start++) {

                    if (dictionary.contains(s.substring(start, end))) {
                        minExtra[end] = Math.min(
                                minExtra[end],
                                minExtra[start]
                        );
                    }
                }
            }

            return minExtra[s.length()];
        }
    }

    /**
     * DELTA 6: LeetCode 91 — Decode Ways.
     * Valid edges are 1-digit / 2-digit decoding rules; count paths.
     */
    static class DecodeWays {

        public int numDecodings(String s) {

            int[] ways = new int[s.length() + 1];
            ways[0] = 1;

            for (int end = 1; end <= s.length(); end++) {

                if (s.charAt(end - 1) != '0') {
                    ways[end] += ways[end - 1];
                }

                if (end >= 2) {

                    int value = (s.charAt(end - 2) - '0') * 10
                            + (s.charAt(end - 1) - '0');

                    if (value >= 10 && value <= 26) {
                        ways[end] += ways[end - 2];
                    }
                }
            }

            return ways[s.length()];
        }
    }

    /**
     * ============================================================
     * NEXT RING — OWN FILES
     * ============================================================
     *
     * Concatenated Words — LC 472
     *     Word Break repeatedly per dictionary word.
     *
     * Palindrome Partitioning — LC 131
     *     dictionary-valid edge -> palindrome-valid edge; all paths.
     *
     * Palindrome Partitioning II — LC 132
     *     palindrome-valid edges + minimum cuts.
     *
     * Restore IP Addresses — LC 93
     *     exactly four pieces + local numeric validity.
     *
     * ============================================================
     * GENERAL STATE-GRAPH TRANSFER
     * ============================================================
     *
     * For a new sequential-choice problem ask:
     *
     *     1. What is a state?
     *     2. What choice creates an edge?
     *     3. What is the start?
     *     4. What is the target / output?
     *
     * Then:
     *
     *     can reach?      -> BFS / DFS / boolean DP
     *     fewest moves?   -> BFS if unit-cost edges
     *     count ways?     -> count paths / counting DP
     *     one solution?   -> parent pointers
     *     all solutions?  -> enumerate paths
     *
     * DP is often a compact way to process an implicit state graph.
     */

    public static void main(String[] args) {

        List<Solver> solvers = List.of(
                new Solver("BFS + HashSet", new PrimaryGraphBfs()::wordBreak),
                new Solver("BFS + Trie", new TrieGraphBfs()::wordBreak),
                new Solver("Bottom-up DP", new EquivalentBottomUpDp()::wordBreak),
                new Solver("Memoized DFS", new MemoizedDfs()::wordBreak)
        );

        for (Solver solver : solvers) {

            check(
                    solver.solve("leetcode", List.of("leet", "code")),
                    true,
                    solver.name + " basic true"
            );

            check(
                    solver.solve(
                            "applepenapple",
                            List.of("apple", "pen")
                    ),
                    true,
                    solver.name + " word reuse"
            );

            check(
                    solver.solve(
                            "catsandog",
                            List.of("cats", "dog", "sand", "and", "cat")
                    ),
                    false,
                    solver.name + " unreachable target"
            );

            check(
                    solver.solve(
                            "cars",
                            List.of("car", "ca", "rs")
                    ),
                    true,
                    solver.name + " greedy counterexample"
            );
        }

        checkEquals(
                new ReturnOneSegmentation().wordBreakOne(
                        "cars",
                        List.of("car", "ca", "rs")
                ),
                List.of("ca", "rs"),
                "return one segmentation"
        );

        checkEquals(
                new CountSegmentations().count(
                        "catsanddog",
                        List.of("cat", "cats", "and", "sand", "dog")
                ),
                2L,
                "count segmentations"
        );

        checkEquals(
                new MinimumWords().minWords(
                        "pineapple",
                        List.of("pine", "pineapple", "apple")
                ),
                1,
                "minimum words"
        );

        checkEquals(
                new WordBreakII().wordBreak(
                        "catsanddog",
                        List.of("cat", "cats", "and", "sand", "dog")
                ),
                List.of("cat sand dog", "cats and dog"),
                "Word Break II"
        );

        checkEquals(
                new ExtraCharacters().minExtraChar(
                        "leetscode",
                        new String[]{"leet", "code", "leetcode"}
                ),
                1,
                "Extra Characters"
        );

        checkEquals(
                new DecodeWays().numDecodings("226"),
                3,
                "Decode Ways"
        );

        System.out.println("All WordBreak V4 graph-first tests passed.");
    }

    @FunctionalInterface
    interface WordBreakFunction {
        boolean solve(String s, List<String> wordDict);
    }

    static class Solver {
        final String name;
        final WordBreakFunction function;

        Solver(String name, WordBreakFunction function) {
            this.name = name;
            this.function = function;
        }

        boolean solve(String s, List<String> wordDict) {
            return function.solve(s, wordDict);
        }
    }

    private static void check(
            boolean actual,
            boolean expected,
            String testName
    ) {

        if (actual != expected) {
            throw new AssertionError(
                    testName
                            + " failed: expected "
                            + expected
                            + ", got "
                            + actual
            );
        }
    }

    private static void checkEquals(
            Object actual,
            Object expected,
            String testName
    ) {

        if (!actual.equals(expected)) {
            throw new AssertionError(
                    testName
                            + " failed: expected="
                            + expected
                            + ", actual="
                            + actual
            );
        }
    }
}
