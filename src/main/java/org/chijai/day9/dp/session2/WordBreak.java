package org.chijai.day9.dp.session2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Word Break
 *
 * ============================================================
 * 📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Difficulty:
 * Medium
 *
 * LeetCode:
 * https://leetcode.com/problems/word-break/
 *
 * Pattern:
 * Dynamic Programming -> 1D Prefix / Segmentation DP
 *
 * Problem:
 * Given a string s and a dictionary wordDict, return true if s
 * can be segmented into one or more dictionary words.
 * Dictionary words may be reused.
 *
 * Examples:
 *
 * s = "leetcode"
 * wordDict = ["leet", "code"]
 * -> true
 *
 * s = "applepenapple"
 * wordDict = ["apple", "pen"]
 * -> true
 *
 * s = "catsandog"
 * wordDict = ["cats", "dog", "sand", "and", "cat"]
 * -> false
 *
 * Constraints:
 * 1 <= s.length <= 300
 * 1 <= wordDict.length <= 1000
 * 1 <= wordDict[i].length <= 20
 * lowercase English letters only
 * dictionary words are unique
 *
 * ============================================================
 * 🔵 PATTERN CLASSIFICATION
 * ============================================================
 *
 * FAMILY
 *     Dynamic Programming
 *
 * SUB-PATTERN
 *     Prefix DP / String Segmentation / Boolean Reachability
 *
 * RECOGNITION
 *     "Can this whole sequence be built from valid pieces?"
 *
 * STATE
 *     dp[i] = whether s[0 ... i) can be segmented.
 *
 * TRANSITION
 *     dp[end] is true if there exists a start such that:
 *
 *         dp[start]
 *         &&
 *         s[start ... end) is a dictionary word
 *
 * BASE
 *     dp[0] = true
 *
 *     The empty prefix is already successfully segmented.
 *     It is the seed that allows the first real word to attach.
 *
 * ANSWER
 *     dp[s.length()]
 *
 * ONE-LINE ANCHOR
 *     Reachable prefix + valid next word -> new reachable prefix.
 *
 * ------------------------------------------------------------
 * Why This Is DP, Not Backtracking
 * ------------------------------------------------------------
 *
 * A recursive solution may branch over many possible next words,
 * but different branches repeatedly ask the same question:
 *
 *     "Can the suffix starting at index i be completed?"
 *
 * Repeated state -> cache it -> DP.
 *
 * Backtracking is the search shape.
 * DP is the optimization created by overlapping states.
 *
 * ============================================================
 * 🧠 FIRST-PRINCIPLES INVENTION PATH
 * ============================================================
 *
 * Start from the final question:
 *
 *     Can prefix s[0 ... end) be segmented?
 *
 * If yes, it must have some LAST word.
 *
 * Suppose that last word starts at index start.
 * Then two things must both be true:
 *
 * 1. s[0 ... start) was already segmentable.
 * 2. s[start ... end) is a dictionary word.
 *
 * That directly gives:
 *
 *     dp[end] = exists start:
 *               dp[start]
 *               && dictionary contains s[start ... end)
 *
 * The smallest already-solved state is the empty prefix:
 *
 *     dp[0] = true
 *
 * Now process prefixes from short to long so every dp[start]
 * needed by dp[end] is already known.
 *
 * Final structure:
 *
 *     HashSet dictionary
 *     boolean[n + 1] dp
 *     dp[0] = true
 *
 *     for each end
 *         try legal start positions
 *         reachable prefix + dictionary word -> dp[end] = true
 *
 *     return dp[n]
 *
 * ============================================================
 * ⭐ PRIMARY PHOTOGRAPHIC-MEMORY SOLUTION
 * ============================================================
 *
 * Remember only:
 *
 *     dp[0] = true
 *
 *     for end
 *         for start
 *             if dp[start] && word(start,end)
 *                 dp[end] = true
 *                 break
 *
 *     return dp[n]
 *
 * maxWordLength only removes impossible cuts.
 * It does not change the recurrence.
 */
public class WordBreak {

    static class Primary {

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
     * 🟢 MENTAL MODEL: REACHABLE CUT POSITIONS
     * ============================================================
     *
     * Think of positions between characters:
     *
     *     l e e t c o d e
     *     0 1 2 3 4 5 6 7 8
     *
     * dp[i] answers:
     *
     *     "Can I legally reach cut position i?"
     *
     * For:
     *
     *     s = "leetcode"
     *     dictionary = {"leet", "code"}
     *
     * Start:
     *
     *     dp[0] = true
     *
     * "leet" creates an edge:
     *
     *     0 --------> 4
     *
     * so:
     *
     *     dp[4] = true
     *
     * "code" creates:
     *
     *     4 --------> 8
     *
     * because 4 was already reachable:
     *
     *     dp[8] = true
     *
     * This is reachability on an implicit DAG of string indices.
     * DP avoids explicitly building the graph.
     *
     * ------------------------------------------------------------
     * Variable Meanings
     * ------------------------------------------------------------
     *
     * end
     *     Exclusive end of the prefix currently being proved.
     *
     * start
     *     Candidate boundary before the final word.
     *
     * dp[start]
     *     Proof that everything before the candidate word works.
     *
     * s.substring(start, end)
     *     Candidate final word.
     *
     * dp[end]
     *     Becomes true after finding ONE valid predecessor.
     *
     * ------------------------------------------------------------
     * Core Correctness Contract
     * ------------------------------------------------------------
     *
     * Before processing end:
     *
     *     dp[0 ... end-1] already correctly describe those prefixes.
     *
     * After processing end:
     *
     *     dp[end] is true exactly when some reachable earlier cut
     *     can connect to end using one dictionary word.
     *
     * ============================================================
     * 🔴 TRAPS / WRONG DIRECTIONS
     * ============================================================
     *
     * 1. Greedy longest-word choice
     *
     *    s = "cars"
     *    dict = ["car", "ca", "rs"]
     *
     *    Choosing "car" first leaves "s" -> failure.
     *    Correct segmentation is "ca" + "rs".
     *
     *    Therefore no locally longest/shortest choice is safe.
     *
     * 2. Plain DFS without memo
     *
     *    The same starting index is recomputed from many branches.
     *    Repeated suffix state causes exponential work.
     *
     * 3. Forgetting dp[0] = true
     *
     *    Then no first word can ever attach to the empty prefix.
     *
     * 4. Checking only dictionary membership
     *
     *    A valid word ending at end is useless if the prefix before
     *    it is unreachable.
     *
     *    Both are required:
     *
     *        dp[start]
     *        &&
     *        dictionary.contains(...)
     *
     * 5. Off-by-one confusion
     *
     *    dp[i] describes the first i characters.
     *    substring(start, end) uses start inclusive, end exclusive.
     *
     * ============================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * 1. Put dictionary words in HashSet.
     * 2. Find maximum dictionary word length.
     * 3. Create boolean dp[n + 1].
     * 4. Set dp[0] = true.
     * 5. Loop end from 1 through n.
     * 6. Try starts that produce words no longer than maxWordLength.
     * 7. If reachable prefix + valid word:
     *        dp[end] = true
     *        break
     * 8. Return dp[n].
     *
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * dp[0] = true
     *
     * for end = 1..n
     *     for start in possible previous cuts
     *         if dp[start] AND s[start:end] in dictionary
     *             dp[end] = true
     *             break
     *
     * return dp[n]
     *
     * ============================================================
     * SOLUTION PROGRESSION
     * ============================================================
     */

    /**
     * ============================================================
     * Brute Force
     * ============================================================
     *
     * Core Idea
     * ---------
     * From each index, try every dictionary-sized next substring.
     * Recurse after every valid word.
     *
     * Invariant
     * ---------
     * start is the first character not yet segmented.
     *
     * Limitation
     * ----------
     * The same start index may be solved again from many branches.
     *
     * Time
     * ----
     * Exponential in the worst case.
     *
     * Space
     * -----
     * O(n) recursion depth.
     *
     * Interview Preference
     * --------------------
     * Useful only to expose the repeated-state bottleneck.
     */
    static class BruteForce {

        public boolean wordBreak(String s, List<String> wordDict) {

            Set<String> dictionary = new HashSet<>(wordDict);
            int maxWordLength = maxWordLength(wordDict);

            return canBreak(
                    s,
                    0,
                    dictionary,
                    maxWordLength
            );
        }

        private boolean canBreak(
                String s,
                int start,
                Set<String> dictionary,
                int maxWordLength
        ) {

            if (start == s.length()) {
                return true;
            }

            int latestEnd = Math.min(
                    s.length(),
                    start + maxWordLength
            );

            for (int end = start + 1; end <= latestEnd; end++) {

                if (dictionary.contains(s.substring(start, end))
                        && canBreak(
                                s,
                                end,
                                dictionary,
                                maxWordLength
                        )) {

                    return true;
                }
            }

            return false;
        }
    }

    /**
     * ============================================================
     * Improved: Top-Down DP / Memoized DFS
     * ============================================================
     *
     * Core Idea
     * ---------
     * Cache the answer for every start index.
     *
     * State
     * -----
     * memo[start] = whether suffix s[start ... n) can be segmented.
     *
     * Limitation Fixed
     * ----------------
     * Every suffix-start state is solved at most once.
     *
     * Complexity
     * ----------
     * Let L = maximum dictionary word length.
     *
     * Transition count:
     * O(n * L)
     *
     * With Java substring creation + hashing:
     * O(n * L^2) worst case.
     *
     * Space:
     * O(n + dictionary characters).
     *
     * Interview Preference
     * --------------------
     * Very natural if recursion is discovered first.
     */
    static class Improved {

        public boolean wordBreak(String s, List<String> wordDict) {

            Set<String> dictionary = new HashSet<>(wordDict);
            int maxWordLength = maxWordLength(wordDict);
            Map<Integer, Boolean> memo = new HashMap<>();

            return canBreak(
                    s,
                    0,
                    dictionary,
                    maxWordLength,
                    memo
            );
        }

        private boolean canBreak(
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

            int latestEnd = Math.min(
                    s.length(),
                    start + maxWordLength
            );

            for (int end = start + 1; end <= latestEnd; end++) {

                if (dictionary.contains(s.substring(start, end))
                        && canBreak(
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
     * Optimal (Interview Preferred)
     * ============================================================
     *
     * Core Idea
     * ---------
     * Build prefix reachability left to right.
     *
     * State
     * -----
     * dp[i] = first i characters can be segmented.
     *
     * Transition
     * ----------
     * A reachable start plus one dictionary word makes end reachable.
     *
     * Why Preferred
     * -------------
     * No recursion.
     * State meaning is explicit.
     * Correctness is easy to articulate.
     * maxWordLength removes impossible cut positions.
     *
     * Complexity
     * ----------
     * Let n = s.length(), L = maximum dictionary word length.
     *
     * At most O(n * L) candidate cuts are considered.
     * Java substring construction and hashing cost up to O(L), so:
     *
     *     O(n * L^2) worst-case time
     *
     * With L <= 20, this is comfortably within constraints.
     *
     * Space:
     *
     *     O(n + total dictionary characters)
     *
     * A trie can remove repeated substring/hash work for much larger
     * workloads, but it adds code and is not the 80/20 interview choice
     * for this problem.
     */
    static class Optimal {

        public boolean wordBreak(String s, List<String> wordDict) {

            Set<String> dictionary = new HashSet<>(wordDict);
            int maxWordLength = maxWordLength(wordDict);

            boolean[] dp = new boolean[s.length() + 1];
            dp[0] = true;

            for (int end = 1; end <= s.length(); end++) {

                int earliestStart = Math.max(0, end - maxWordLength);

                for (int start = earliestStart; start < end; start++) {

                    // Prefix before this candidate word must be reachable.
                    if (!dp[start]) {
                        continue;
                    }

                    if (dictionary.contains(s.substring(start, end))) {
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
     * 🟣 INTERVIEW ARTICULATION
     * ============================================================
     *
     * Say Before Coding
     * -----------------
     *
     * "I will use prefix DP. dp[i] means the first i characters can
     * be segmented. For every end position, I try a possible final
     * word starting at start. end becomes reachable exactly when
     * dp[start] is already true and s[start,end) is in the dictionary.
     * I seed dp[0] = true and return dp[n]."
     *
     * Correctness
     * -----------
     *
     * Every valid segmentation of prefix end has a final word.
     * Removing that final word leaves some earlier prefix start.
     * Therefore every valid solution is represented by one transition
     * dp[start] -> dp[end].
     *
     * Conversely, every transition accepted by the algorithm combines
     * an already valid segmentation with a dictionary word, so it cannot
     * create an invalid segmentation.
     *
     * Why break Is Safe
     * -----------------
     *
     * dp[end] is boolean.
     * We need existence, not the number of segmentations.
     * One valid predecessor is enough.
     *
     * Why maxWordLength Is Safe
     * -------------------------
     *
     * No dictionary word is longer than maxWordLength.
     * Therefore starts earlier than end - maxWordLength cannot possibly
     * produce a dictionary word ending at end.
     *
     * ============================================================
     * 🎯 30-SECOND RECALL SHEET
     * ============================================================
     *
     * TRIGGER
     *     Can whole string be split into valid dictionary pieces?
     *
     * PATTERN
     *     1D prefix DP / segmentation reachability.
     *
     * STATE
     *     dp[i] = s[0 ... i) is breakable.
     *
     * BASE
     *     dp[0] = true.
     *
     * TRANSITION
     *     dp[start] && dict.contains(s[start:end]) -> dp[end].
     *
     * ANSWER
     *     dp[n].
     *
     * TRAP
     *     A dictionary word is useful only if its starting prefix
     *     is already reachable.
     *
     * RE-DERIVATION CUE
     *     "What could the LAST word be?"
     *
     * ============================================================
     * 🔄 HORIZONTAL MASTERY — WORKING DELTAS
     * ============================================================
     *
     * Do not learn nearby problems as unrelated tricks.
     * Keep the same cut-position graph and change ONE dimension.
     *
     * Best learning order:
     *
     *     1. ReturnOneSegmentation
     *        boolean reachability + parent[] reconstruction
     *
     *     2. CountSegmentations
     *        OR -> SUM
     *
     *     3. MinimumWords
     *        OR -> MIN
     *
     *     4. WordBreakII              (LeetCode 140)
     *        one/existence -> ALL valid paths
     *
     *     5. ExtraCharacters          (LeetCode 2707)
     *        valid-word edge costs 0; skipping a char costs 1
     *
     *     6. DecodeWays               (LeetCode 91)
     *        dictionary becomes an implicit 1/2-digit validity rule;
     *        output remains path count
     *
     * Why these belong here:
     *     Each has a small enough delta that comparing code side-by-side
     *     strengthens pattern recognition instead of adding a new pattern.
     *
     * NEXT RING — learn in their own files:
     *     Concatenated Words (472)
     *         Word Break used repeatedly as a subroutine.
     *
     *     Palindrome Partitioning (131)
     *         dictionary-valid -> palindrome-valid; enumerate cuts.
     *
     *     Palindrome Partitioning II (132)
     *         palindrome-valid + minimize cuts.
     *
     *     Restore IP Addresses (93)
     *         constrained segmentation with exactly four pieces.
     *
     * FAMILY DELTA CARD
     * -----------------
     * BASE
     *     reachable prefix + valid piece -> reachable prefix
     *
     * ONE PATH
     *     + parent
     *
     * COUNT PATHS
     *     OR -> SUM
     *
     * MINIMUM PATH
     *     OR -> MIN
     *
     * ALL PATHS
     *     boolean -> memoized lists / backtracking
     *
     * ALLOW INVALID MATERIAL AT A COST
     *     add fallback / skip edge
     *
     * DIFFERENT VALIDITY RULE
     *     dictionary lookup -> another local predicate
     *
     * ONE-LINE TRANSFER ANCHOR
     *     Keep the cut graph; ask what changed:
     *     edge validity, state value, or required output?
     *
     * ============================================================
     * 🧪 SELF-VERIFYING TESTS
     * ============================================================
     */

    /**
     * DELTA 1: EXISTENCE -> ONE ACTUAL PATH
     *
     * Same boolean DP. The only addition is parent[end] = start.
     */
    static class ReturnOneSegmentation {

        public List<String> wordBreakOne(String s, List<String> wordDict) {

            Set<String> dictionary = new HashSet<>(wordDict);
            int maxWordLength = maxWordLength(wordDict);

            boolean[] dp = new boolean[s.length() + 1];
            int[] parent = new int[s.length() + 1];
            Arrays.fill(parent, -1);

            dp[0] = true;

            for (int end = 1; end <= s.length(); end++) {

                int earliestStart = Math.max(0, end - maxWordLength);

                for (int start = earliestStart; start < end; start++) {

                    if (dp[start]
                            && dictionary.contains(s.substring(start, end))) {

                        dp[end] = true;
                        parent[end] = start;
                        break;
                    }
                }
            }

            if (!dp[s.length()]) {
                return List.of();
            }

            List<String> words = new ArrayList<>();

            for (int end = s.length(); end > 0; end = parent[end]) {
                int start = parent[end];
                words.add(s.substring(start, end));
            }

            Collections.reverse(words);
            return words;
        }
    }

    /**
     * DELTA 2: BOOLEAN REACHABILITY -> NUMBER OF PATHS
     *
     * OR becomes SUM. Do not break after the first valid predecessor.
     */
    static class CountSegmentations {

        public long count(String s, List<String> wordDict) {

            Set<String> dictionary = new HashSet<>(wordDict);
            int maxWordLength = maxWordLength(wordDict);

            long[] dp = new long[s.length() + 1];
            dp[0] = 1;

            for (int end = 1; end <= s.length(); end++) {

                int earliestStart = Math.max(0, end - maxWordLength);

                for (int start = earliestStart; start < end; start++) {

                    if (dp[start] > 0
                            && dictionary.contains(s.substring(start, end))) {

                        dp[end] += dp[start];
                    }
                }
            }

            return dp[s.length()];
        }
    }

    /**
     * DELTA 3: BOOLEAN REACHABILITY -> MINIMUM NUMBER OF EDGES
     *
     * OR becomes MIN. Every dictionary word contributes cost 1.
     */
    static class MinimumWords {

        public int minWords(String s, List<String> wordDict) {

            Set<String> dictionary = new HashSet<>(wordDict);
            int maxWordLength = maxWordLength(wordDict);

            int[] dp = new int[s.length() + 1];
            Arrays.fill(dp, Integer.MAX_VALUE);
            dp[0] = 0;

            for (int end = 1; end <= s.length(); end++) {

                int earliestStart = Math.max(0, end - maxWordLength);

                for (int start = earliestStart; start < end; start++) {

                    if (dp[start] != Integer.MAX_VALUE
                            && dictionary.contains(s.substring(start, end))) {

                        dp[end] = Math.min(dp[end], dp[start] + 1);
                    }
                }
            }

            return dp[s.length()] == Integer.MAX_VALUE
                    ? -1
                    : dp[s.length()];
        }
    }

    /**
     * DELTA 4: EXISTENCE -> ALL PATHS
     *
     * LeetCode 140 — Word Break II.
     * Memoize by start index because many branches ask for the same suffix.
     */
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
            int latestEnd = Math.min(s.length(), start + maxWordLength);

            for (int end = start + 1; end <= latestEnd; end++) {

                String word = s.substring(start, end);

                if (!dictionary.contains(word)) {
                    continue;
                }

                for (String suffix : sentencesFrom(
                        s, end, dictionary, maxWordLength, memo)) {

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
     * DELTA 5: ADD A FALLBACK EDGE WITH COST 1
     *
     * LeetCode 2707 — Extra Characters in a String.
     *
     * Skip current char:    dp[end] = dp[end - 1] + 1
     * Use dictionary word:  dp[end] = min(dp[end], dp[start])
     */
    static class ExtraCharacters {

        public int minExtraChar(String s, String[] dictionaryWords) {

            Set<String> dictionary = new HashSet<>(Arrays.asList(dictionaryWords));

            int maxWordLength = 0;
            for (String word : dictionaryWords) {
                maxWordLength = Math.max(maxWordLength, word.length());
            }

            int[] dp = new int[s.length() + 1];

            for (int end = 1; end <= s.length(); end++) {

                dp[end] = dp[end - 1] + 1;
                int earliestStart = Math.max(0, end - maxWordLength);

                for (int start = earliestStart; start < end; start++) {

                    if (dictionary.contains(s.substring(start, end))) {
                        dp[end] = Math.min(dp[end], dp[start]);
                    }
                }
            }

            return dp[s.length()];
        }
    }

    /**
     * DELTA 6: EXPLICIT DICTIONARY -> IMPLICIT TOKEN VALIDITY,
     *          BOOLEAN -> PATH COUNT
     *
     * LeetCode 91 — Decode Ways.
     * Only one- and two-character pieces can be legal.
     */
    static class DecodeWays {

        public int numDecodings(String s) {

            int[] dp = new int[s.length() + 1];
            dp[0] = 1;

            for (int end = 1; end <= s.length(); end++) {

                if (s.charAt(end - 1) != '0') {
                    dp[end] += dp[end - 1];
                }

                if (end >= 2) {

                    int value = (s.charAt(end - 2) - '0') * 10
                            + (s.charAt(end - 1) - '0');

                    if (value >= 10 && value <= 26) {
                        dp[end] += dp[end - 2];
                    }
                }
            }

            return dp[s.length()];
        }
    }

    public static void main(String[] args) {

        Primary solution = new Primary();

        check(
                solution.wordBreak(
                        "leetcode",
                        List.of("leet", "code")
                ),
                true,
                "basic true"
        );

        check(
                solution.wordBreak(
                        "applepenapple",
                        List.of("apple", "pen")
                ),
                true,
                "dictionary word reuse"
        );

        check(
                solution.wordBreak(
                        "catsandog",
                        List.of("cats", "dog", "sand", "and", "cat")
                ),
                false,
                "unreachable prefix before final word"
        );

        check(
                solution.wordBreak(
                        "cars",
                        List.of("car", "ca", "rs")
                ),
                true,
                "greedy-longest-word counterexample"
        );

        check(
                solution.wordBreak(
                        "aaaaaaa",
                        List.of("aaaa", "aaa")
                ),
                true,
                "multiple valid cut lengths"
        );

        check(
                solution.wordBreak(
                        "a",
                        List.of("b")
                ),
                false,
                "single-character false"
        );

        checkEquals(
                new ReturnOneSegmentation().wordBreakOne(
                        "cars",
                        List.of("car", "ca", "rs")
                ),
                List.of("ca", "rs"),
                "delta: reconstruct one segmentation"
        );

        checkEquals(
                new CountSegmentations().count(
                        "catsanddog",
                        List.of("cat", "cats", "and", "sand", "dog")
                ),
                2L,
                "delta: count segmentations"
        );

        checkEquals(
                new MinimumWords().minWords(
                        "pineapple",
                        List.of("pine", "pineapple", "apple")
                ),
                1,
                "delta: minimum words"
        );

        checkEquals(
                new WordBreakII().wordBreak(
                        "catsanddog",
                        List.of("cat", "cats", "and", "sand", "dog")
                ),
                List.of("cat sand dog", "cats and dog"),
                "delta: enumerate all segmentations"
        );

        checkEquals(
                new ExtraCharacters().minExtraChar(
                        "leetscode",
                        new String[]{"leet", "code", "leetcode"}
                ),
                1,
                "delta: skipped characters cost one"
        );

        checkEquals(
                new DecodeWays().numDecodings("226"),
                3,
                "delta: implicit-token path counting"
        );

        System.out.println("All WordBreak family tests passed.");
    }

    private static int maxWordLength(List<String> wordDict) {

        int max = 0;

        for (String word : wordDict) {
            max = Math.max(max, word.length());
        }

        return max;
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
                            + " expected=" + expected
                            + " actual=" + actual
            );
        }
    }

}
