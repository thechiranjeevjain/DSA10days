package org.chijai.day3.session3;

import java.util.HashSet;

/**
 * ============================================================================
 *  Longest Palindrome
 * ============================================================================
 *
 * Difficulty:
 * Easy
 *
 * Tags:
 * Hash Table
 * Greedy
 * String
 * Counting
 *
 * Problem:
 *
 * Given a string s consisting of lowercase and uppercase English letters,
 * return the length of the longest palindrome that can be built using those
 * letters.
 *
 * Letters are case-sensitive.
 *
 * Example:
 *
 * "Aa" is NOT a palindrome because 'A' != 'a'.
 *
 * Constraints:
 *
 * 1 <= s.length <= 2000
 * s consists of lowercase and/or uppercase English letters only.
 *
 * Example 1
 *
 * Input:
 * "abccccdd"
 *
 * Output:
 * 7
 *
 * Explanation:
 *
 * Counts:
 *
 * a -> 1
 * b -> 1
 * c -> 4
 * d -> 2
 *
 * We use:
 *
 * c -> 4
 * d -> 2
 * one odd character as center
 *
 * Total = 7
 *
 * Example 2
 *
 * Input:
 * "a"
 *
 * Output:
 * 1
 *
 * Example 3
 *
 * Input:
 * "abc"
 *
 * Output:
 * 1
 *
 * Only one character may occupy the center.
 *
 * Official LeetCode:
 * https://leetcode.com/problems/longest-palindrome/
 */
public class LongestPalindrome {

    /*
     * =========================================================================
     * 🔵 CORE PATTERN OVERVIEW
     * =========================================================================
     *
     * Pattern
     * -------
     * Frequency Counting + Greedy Pair Construction
     *
     * Archetype
     * ---------
     * Build the answer from frequency information instead of constructing
     * the actual palindrome.
     *
     * Core Invariant
     * --------------
     * Every palindrome is composed of:
     *
     *     symmetric pairs
     *             +
     *     at most one center character.
     *
     * Therefore:
     *
     * Every even occurrence is fully usable.
     *
     * Every odd occurrence contributes:
     *
     *     count - 1
     *
     * as symmetric pairs.
     *
     * Among ALL odd frequencies,
     * exactly ONE remaining character may become the center.
     *
     * Why It Works
     * ------------
     * Pairing is always optimal because every pair occupies two symmetric
     * positions.
     *
     * A second odd leftover cannot be placed because only one center exists.
     *
     * Recognition Signals
     * -------------------
     * Look for:
     *
     * • frequency counting
     * • maximizing palindrome length
     * • rearrangement allowed
     * • actual palindrome not required
     *
     * When To Use
     * -----------
     * Whenever only the maximum possible palindrome size matters.
     *
     * When NOT To Use
     * ---------------
     * If:
     *
     * • actual palindrome must be returned
     * • ordering constraints exist
     * • substring instead of rearrangement
     *
     * Comparison
     * ----------
     *
     * Longest Palindromic Substring
     *      -> center expansion
     *
     * Palindrome Partitioning
     *      -> DP / backtracking
     *
     * Valid Palindrome
     *      -> two pointers
     *
     * This problem
     *      -> frequency counting.
     */

    /*
     * =========================================================================
     * 🟢 MENTAL MODEL & INVARIANTS
     * =========================================================================
     *
     * Mental Model
     * ------------
     *
     * Imagine building a wall.
     *
     * Every block must appear on BOTH sides.
     *
     * Therefore characters are consumed two at a time.
     *
     * Left Side      Right Side
     * ---------      ----------
     *      c             c
     *      a             a
     *      d             d
     *
     * After all pairs are consumed,
     * only one block may remain in the middle.
     *
     * ----------------------------
     * Invariant 1
     * ----------------------------
     *
     * Every usable character outside the center belongs to a pair.
     *
     * ----------------------------
     * Invariant 2
     * ----------------------------
     *
     * Every even frequency contributes completely.
     *
     * Example:
     *
     * 6 -> use 6
     * 8 -> use 8
     *
     * ----------------------------
     * Invariant 3
     * ----------------------------
     *
     * Every odd frequency contributes:
     *
     * count - 1
     *
     * because one occurrence cannot form a pair.
     *
     * Example:
     *
     * 5 -> use 4
     * 7 -> use 6
     *
     * ----------------------------
     * Invariant 4
     * ----------------------------
     *
     * If ANY odd frequency exists,
     * exactly one leftover character can become the center.
     *
     * This is the only time answer increases by one.
     *
     * Variable Meanings
     * -----------------
     *
     * count[]
     *      frequency table
     *
     * ans
     *      total paired characters accumulated so far
     *
     * hasOddCount
     *      whether a center is available
     *
     * Allowed State Transition
     * ------------------------
     *
     * even count
     *      answer += count
     *
     * odd count
     *      answer += count - 1
     *      hasOddCount = true
     *
     * Forbidden Transition
     * --------------------
     *
     * Adding every odd occurrence.
     *
     * Example:
     *
     * counts:
     *
     * 3
     * 5
     *
     * Adding 3 + 5 violates palindrome symmetry.
     *
     * Termination
     * -----------
     *
     * Every frequency bucket processed exactly once.
     *
     * Correctness Intuition
     * ---------------------
     *
     * Pair usage is mandatory.
     *
     * Center usage is optional.
     *
     * Therefore greedy never loses an optimal solution.
     *
     * Why Naive Thinking Fails
     * ------------------------
     *
     * Many people try to actually build the palindrome.
     *
     * This is unnecessary.
     *
     * The question asks only for length.
     *
     * Therefore frequencies completely determine the answer.
     */

    /*
     * =========================================================================
     * 🔴 WHY WRONG SOLUTIONS FAIL
     * =========================================================================
     *
     * Mistake 1
     * ---------
     * Add every frequency directly.
     *
     * Counterexample:
     *
     * a -> 3
     * b -> 1
     *
     * Total = 4
     *
     * Impossible.
     *
     * Maximum = 3.
     *
     * Violated Invariant
     * ------------------
     * Multiple unpaired characters cannot exist.
     *
     * -------------------------------------------------------
     *
     * Mistake 2
     * ---------
     * Add one center for every odd frequency.
     *
     * Counterexample:
     *
     * a=3
     * b=5
     *
     * Wrong:
     *
     * 2 + 1 + 4 + 1 = 8
     *
     * Correct:
     *
     * 2 + 4 + 1 = 7
     *
     * Violated Invariant
     * ------------------
     * Only one center exists.
     *
     * -------------------------------------------------------
     *
     * Mistake 3
     * ---------
     * Forget that uppercase and lowercase differ.
     *
     * Example:
     *
     * A
     * a
     *
     * Frequency buckets must remain separate.
     *
     * -------------------------------------------------------
     *
     * Mistake 4
     * ---------
     * Use HashMap when ASCII array is sufficient.
     *
     * Constraints guarantee ASCII letters.
     *
     * int[128] is simpler and faster.
     *
     * Interview Trap
     * --------------
     *
     * The interviewer often asks:
     *
     * "Why can't two odd leftovers both appear?"
     *
     * Correct answer:
     *
     * Because only one index of a palindrome has no mirrored partner.
     */

    /*
     * =========================================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * =========================================================================
     *
     * Mechanical Typing Order
     * -----------------------
     *
     * 1. Create frequency array.
     *
     * 2. Scan string.
     *
     *      count[c]++
     *
     * 3. Initialize:
     *
     *      ans = 0
     *      hasOddCount = false
     *
     * 4. Iterate through frequency table.
     *
     *      even
     *          ans += count
     *
     *      odd
     *          ans += count - 1
     *          hasOddCount = true
     *
     * 5. After loop:
     *
     *      if (hasOddCount)
     *          ans++
     *
     * 6. Return answer.
     */

    /*
     * =========================================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * =========================================================================
     *
     * count frequencies
     *
     * answer = 0
     *
     * odd = false
     *
     * for each frequency
     *     use all pairs
     *     remember odd
     *
     * if odd
     *     answer++
     *
     * return answer
     */

    /*
     * =========================================================================
     * 6. SOLUTION CLASSES
     * =========================================================================
     */

    /**
     * -------------------------------------------------------------------------
     * Brute Force
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     * Enumerate character selections and attempt to build palindromes.
     *
     * Invariant
     * ---------
     * None efficiently maintained.
     *
     * Limitation
     * ----------
     * Combinatorial explosion.
     *
     * Complexity
     * ----------
     * Exponential.
     *
     * Interview Usefulness
     * --------------------
     * Mention only to motivate counting.
     */
    static class BruteForce {

        /*
         * Intentionally omitted because exponential search is never practical
         * for this problem.
         */

    }

    /**
     * -------------------------------------------------------------------------
     * Improved
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     * Maintain characters with odd frequency inside a HashSet.
     *
     * Every repeated occurrence completes one pair.
     *
     * Invariant
     * ---------
     * HashSet always stores characters currently having odd frequency.
     *
     * Improvement
     * -----------
     * No explicit frequency array required.
     *
     * Complexity
     * ----------
     * Time : O(n)
     * Space: O(k)
     *
     * Interview Usefulness
     * --------------------
     * Good alternative when alphabet size is unknown.
     */
    static class Improved {

        public int longestPalindrome(String s) {

            if (s == null || s.isEmpty()) {
                return 0;
            }

            HashSet<Character> oddCharacters = new HashSet<>();

            int pairCount = 0;

            for (char ch : s.toCharArray()) {

                // Invariant: characters in the set currently have odd frequency.
                if (oddCharacters.contains(ch)) {

                    oddCharacters.remove(ch);

                    // One additional symmetric pair has been completed.
                    pairCount++;

                } else {

                    oddCharacters.add(ch);
                }
            }

            if (!oddCharacters.isEmpty()) {
                return pairCount * 2 + 1;
            }

            return pairCount * 2;
        }
    }

    /**
     * -------------------------------------------------------------------------
     * Optimal (Interview Preferred)
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     * Count the frequency of every ASCII character.
     *
     * Every even frequency contributes completely.
     *
     * Every odd frequency contributes:
     *
     *      count - 1
     *
     * If at least one odd frequency exists,
     * exactly one remaining character becomes the center.
     *
     * 🟢 Invariant
     * ------------
     * Throughout the frequency scan:
     *
     * ans always equals the maximum number of characters that can already be
     * placed symmetrically.
     *
     * hasOddCount remembers whether a valid center exists.
     *
     * Correctness
     * -----------
     * Every character outside the center must have a mirror.
     *
     * Therefore every usable character must belong to a pair.
     *
     * Complexity
     * ----------
     * Time : O(n)
     * Space: O(1)
     *
     * Interview Usefulness
     * --------------------
     * Preferred solution because:
     *
     * • deterministic
     * • easy to derive
     * • constant memory
     * • directly follows the invariant
     */
    static class Optimal {

        public int longestPalindrome(String s) {

            // Empty input handled immediately.
            if (s == null || s.isEmpty()) {
                return 0;
            }

            int[] frequency = new int[128];

            // Count every character exactly once.
            for (char ch : s.toCharArray()) {
                frequency[ch]++;
            }

            int answer = 0;

            boolean hasOddCount = false;

            for (int count : frequency) {

                if ((count & 1) == 0) {

                    // Invariant:
                    // Every even occurrence forms symmetric pairs.
                    answer += count;

                } else {

                    // Keep only complete pairs.
                    answer += count - 1;

                    // Exactly one odd bucket may later donate its center.
                    hasOddCount = true;
                }
            }

            // Only one center position exists.
            if (hasOddCount) {
                answer++;
            }

            return answer;
        }
    }

/*
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * If asked to explain verbally:
 *
 * "A palindrome is made of mirrored pairs plus at most one center.
 * Therefore I count every character frequency.
 *
 * Every even frequency is fully usable.
 *
 * Every odd frequency contributes only its paired portion:
 *
 *      count - 1
 *
 * If I have seen at least one odd frequency,
 * one leftover character becomes the center.
 *
 * The invariant is that the running answer always represents the maximum
 * number of characters already guaranteed to be placeable symmetrically.
 *
 * After processing all frequencies,
 * if any odd exists,
 * I add one center.
 *
 * Since every frequency bucket is processed once,
 * complexity is O(n) time and O(1) space."
 *
 * Discard Rule
 * ------------
 * Discard exactly one occurrence from every odd frequency.
 *
 * In-place Feasibility
 * --------------------
 * No.
 *
 * Frequency information must be stored somewhere.
 *
 * Streaming Feasibility
 * ---------------------
 * Yes.
 *
 * Characters may be streamed while updating frequency counts.
 *
 * Final answer is computed after the stream ends.
 *
 * When NOT To Use
 * ---------------
 * Do not use this pattern when:
 *
 * • original ordering must be preserved
 * • longest palindromic substring is required
 * • palindrome itself must be constructed efficiently
 */

/*
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 * -------
 * Rearrangement allowed.
 *
 * Need only maximum palindrome length.
 *
 * Invariant
 * ---------
 * Answer stores only guaranteed mirrored pairs.
 *
 * Search Target
 * -------------
 * Frequency of every character.
 *
 * Discard Rule
 * ------------
 * Remove one occurrence from every odd frequency.
 *
 * Common Trap
 * -----------
 * Adding one center for every odd bucket.
 *
 * Edge Cases
 * ----------
 * ""
 * "a"
 * "aa"
 * "abc"
 * all even
 * all odd
 * uppercase/lowercase mix
 *
 * One-liner
 * ---------
 * Use every pair, then choose one center.
 *
 * Re-derivation Cue
 * -----------------
 * Ask:
 *
 * "How many characters can have mirrors?"
 */

/*
 * =========================================================================
 * 🔄 VARIATIONS & TWEAKS
 * =========================================================================
 *
 * Variation 1
 * -----------
 * Unknown character set.
 *
 * Replace:
 *
 *      int[128]
 *
 * with:
 *
 *      HashMap<Character, Integer>
 *
 * Invariant remains unchanged.
 *
 * ----------------------------------------------------------
 *
 * Variation 2
 * -----------
 * Unicode input.
 *
 * Frequency storage changes.
 *
 * Pair invariant does not.
 *
 * ----------------------------------------------------------
 *
 * Variation 3
 * -----------
 * Construct the actual palindrome.
 *
 * First collect half of every frequency.
 *
 * Choose one odd center.
 *
 * Mirror the first half.
 *
 * Additional bookkeeping is required.
 *
 * ----------------------------------------------------------
 *
 * Variation 4
 * -----------
 * Longest palindromic substring.
 *
 * Pattern breaks completely.
 *
 * Frequency alone loses positional information.
 *
 * Center expansion or Manacher's algorithm becomes necessary.
 *
 * ----------------------------------------------------------
 *
 * Variation 5
 * -----------
 * Dynamic updates after insert/delete.
 *
 * Maintain:
 *
 * • frequency
 * • number of odd buckets
 *
 * Answer can then be recomputed efficiently.
 */

/*
 * =========================================================================
 * 🧠 MASTERY CHECKLIST
 * =========================================================================
 *
 * Can you answer these immediately?
 *
 * □ What is the invariant?
 *
 *   Answer:
 *   Running answer contains only mirrored characters.
 *
 * □ What is the search target?
 *
 *   Character frequencies.
 *
 * □ What is the discard rule?
 *
 *   Discard one occurrence from every odd bucket.
 *
 * □ Why does the algorithm terminate?
 *
 *   Every frequency bucket is visited exactly once.
 *
 * □ Why does the naive solution fail?
 *
 *   Multiple unpaired characters cannot all occupy the center.
 *
 * □ Which edge cases matter?
 *
 *   Empty
 *   Single
 *   All even
 *   All odd
 *   Mixed case
 *
 * □ Debugging readiness?
 *
 *   Verify:
 *
 *   frequency table
 *   paired contribution
 *   center addition
 *
 * □ Variant readiness?
 *
 *   Replace frequency container without changing the invariant.
 *
 * □ Pattern boundary?
 *
 *   Frequency problems.
 *
 *   Not positional palindrome problems.
 */

    /*
     * =========================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * =========================================================================
     */

    public static void main(String[] args) {

        Optimal optimal = new Optimal();

        /*
         * ------------------------------------------------------------
         * Representative example from the problem statement.
         * ------------------------------------------------------------
         */
        assert optimal.longestPalindrome("abccccdd") == 7
                : "Expected 7 because 4 c's + 2 d's + one odd center.";

        /*
         * ------------------------------------------------------------
         * Single character.
         * ------------------------------------------------------------
         */
        assert optimal.longestPalindrome("a") == 1
                : "Single character itself forms a palindrome.";

        /*
         * ------------------------------------------------------------
         * Every character has odd frequency.
         * Only one center is possible.
         * ------------------------------------------------------------
         */
        assert optimal.longestPalindrome("abc") == 1
                : "Only one odd character may occupy the center.";

        /*
         * ------------------------------------------------------------
         * All frequencies are even.
         * Everything participates.
         * ------------------------------------------------------------
         */
        assert optimal.longestPalindrome("aabbcc") == 6
                : "Every character can be mirrored.";

        /*
         * ------------------------------------------------------------
         * One odd bucket.
         * ------------------------------------------------------------
         */
        assert optimal.longestPalindrome("aaabb") == 5
                : "Pairs contribute four characters, one odd becomes center.";

        /*
         * ------------------------------------------------------------
         * Multiple odd buckets.
         * Exactly one center allowed.
         * ------------------------------------------------------------
         */
        assert optimal.longestPalindrome("aaabbbbccc") == 9
                : "Use 2 + 4 + 2 pairs and one center.";

        /*
         * ------------------------------------------------------------
         * Case sensitivity.
         * ------------------------------------------------------------
         */
        assert optimal.longestPalindrome("Aa") == 1
                : "Uppercase and lowercase are different characters.";

        /*
         * ------------------------------------------------------------
         * Mixed case with independent buckets.
         * ------------------------------------------------------------
         */
        assert optimal.longestPalindrome("AaAa") == 4
                : "'A' pairs separately from 'a'.";

        /*
         * ------------------------------------------------------------
         * One repeated character.
         * ------------------------------------------------------------
         */
        assert optimal.longestPalindrome("aaaaaaa") == 7
                : "Six mirrored characters plus one center.";

        /*
         * ------------------------------------------------------------
         * Empty string.
         * Not required by constraints but useful defensively.
         * ------------------------------------------------------------
         */
        assert optimal.longestPalindrome("") == 0
                : "Defensive handling for empty input.";

        /*
         * ------------------------------------------------------------
         * Null input.
         * Defensive implementation.
         * ------------------------------------------------------------
         */
        assert optimal.longestPalindrome(null) == 0
                : "Defensive handling for null input.";

        /*
         * ------------------------------------------------------------
         * Verify HashSet implementation against optimal solution.
         * ------------------------------------------------------------
         */
        Improved improved = new Improved();

        String[] regressionInputs = {
                "",
                "a",
                "aa",
                "ab",
                "abc",
                "abccccdd",
                "aaabbbbccc",
                "Aa",
                "AaAa",
                "abcdef",
                "zzzz",
                "banana",
                "racecar",
                "aabbccd",
                "xxxxxxxxxxxx"
        };

        for (String input : regressionInputs) {

            int expected = optimal.longestPalindrome(input);
            int actual = improved.longestPalindrome(input);

            assert expected == actual
                    : "Mismatch for input: " + input;
        }

        System.out.println("All assertions passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/