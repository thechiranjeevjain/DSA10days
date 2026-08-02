package org.chijai.day3.session3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindAllAnagramsInAString {

/*
 * ============================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Title:
 * Find All Anagrams in a String
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Sliding Window
 * Two Pointers
 * Frequency Counting
 * Hashing
 * String
 *
 * LeetCode:
 * https://leetcode.com/problems/find-all-anagrams-in-a-string/
 *
 * ------------------------------------------------------------
 * Problem
 * ------------------------------------------------------------
 *
 * Given two strings:
 *
 * s -> text
 * p -> pattern
 *
 * Return every starting index where a substring of s is an
 * anagram of p.
 *
 * Every character of p must appear exactly once inside the
 * substring.
 *
 * Order of returned indices does not matter.
 *
 * ------------------------------------------------------------
 * Constraints
 * ------------------------------------------------------------
 *
 * 1 <= s.length <= 3 * 10^4
 * 1 <= p.length <= 3 * 10^4
 *
 * s and p contain lowercase English letters.
 *
 * ------------------------------------------------------------
 * Example 1
 * ------------------------------------------------------------
 *
 * s = "cbaebabacd"
 * p = "abc"
 *
 * Output:
 * [0, 6]
 *
 * Explanation:
 *
 * s[0..2] = "cba"
 * contains exactly:
 *
 * a
 * b
 * c
 *
 * therefore it is an anagram.
 *
 * s[6..8] = "bac"
 * is another anagram.
 *
 * ------------------------------------------------------------
 * Example 2
 * ------------------------------------------------------------
 *
 * s = "abab"
 * p = "ab"
 *
 * Output:
 * [0,1,2]
 *
 * Windows:
 *
 * "ab"
 * "ba"
 * "ab"
 *
 * Every window contains exactly one 'a'
 * and exactly one 'b'.
 *
 * ------------------------------------------------------------
 * Goal
 * ------------------------------------------------------------
 *
 * Scan the text only once.
 *
 * Detect every window whose multiset of characters
 * equals the multiset of p.
 *
 * Target complexity:
 *
 * Time  : O(n)
 * Space : O(1)
 * (constant alphabet)
 */

/*
 * ============================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern
 * -------
 * Sliding Window with Character Deficit Tracking
 *
 * Archetype
 * ---------
 * Variable-size window that shrinks only after every required
 * character has been collected.
 *
 * Core Invariant
 * --------------
 * remainingToMatch always equals the total number of characters
 * from p that have not yet been satisfied inside the current
 * window.
 *
 * need[c]
 * --------
 *
 * Positive
 *     We still need this many copies.
 *
 * Zero
 *     Requirement exactly satisfied.
 *
 * Negative
 *     Window owns extra copies.
 *
 * Why It Works
 * ------------
 *
 * Every character entering the window updates exactly one count.
 *
 * Every character leaving the window restores exactly one count.
 *
 * Therefore every state transition preserves the invariant.
 *
 * Recognition Signals
 * -------------------
 *
 * Look for:
 *
 * • all permutations
 * • anagrams
 * • substring with same frequency
 * • exact multiset matching
 * • window over a string
 *
 * When To Use
 * -----------
 *
 * Entire window must satisfy frequency requirements.
 *
 * Alphabet is reasonably small.
 *
 * Need every valid window.
 *
 * When NOT To Use
 * ---------------
 *
 * Order matters.
 *
 * Pattern depends on sequence rather than frequencies.
 *
 * Window validity depends on values other than counts.
 *
 * Comparison
 * ----------
 *
 * Fixed-size Sliding Window
 *     Window size never changes.
 *
 * Variable-size Sliding Window
 *     Window expands then shrinks.
 *
 * Minimum Window Substring
 *     Need at least required counts.
 *
 * Find All Anagrams
 *     Need exactly required counts together with exact window
 *     length.
 */

/*
 * ============================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine p as a shopping list.
 *
 * need[x]
 * stores how many copies are still missing.
 *
 * Every character entering the window tries to pay one item from
 * that shopping list.
 *
 * If the item was still required,
 * remainingToMatch decreases.
 *
 * If we already had enough,
 * the character simply becomes surplus.
 *
 * ------------------------------------------------------------
 * State Variables
 * ------------------------------------------------------------
 *
 * left
 *
 * Beginning of current window.
 *
 * right
 *
 * First position outside current window.
 *
 * Window:
 *
 * [left, right)
 *
 * need[]
 *
 * Remaining deficit for every character.
 *
 * remainingToMatch
 *
 * Number of required characters still missing.
 *
 * ------------------------------------------------------------
 * Primary Invariant
 * ------------------------------------------------------------
 *
 * remainingToMatch ==
 * total number of still-unmatched characters.
 *
 * NOT
 *
 * number of distinct characters.
 *
 * This distinction is critical.
 *
 * Example:
 *
 * p = "aabc"
 *
 * Initially:
 *
 * need[a]=2
 * need[b]=1
 * need[c]=1
 *
 * remainingToMatch = 4
 *
 * not 3.
 *
 * ------------------------------------------------------------
 * Frequency Invariant
 * ------------------------------------------------------------
 *
 * need[c] > 0
 *
 * Window still lacks this character.
 *
 * need[c] == 0
 *
 * Requirement exactly met.
 *
 * need[c] < 0
 *
 * Window owns extra copies.
 *
 * Negative counts are valid.
 *
 * They are not bugs.
 *
 * They represent surplus.
 *
 * ------------------------------------------------------------
 * Expansion Transition
 * ------------------------------------------------------------
 *
 * Character x enters.
 *
 * If need[x] > 0
 *     we satisfied one missing requirement.
 *
 * Therefore
 *
 * remainingToMatch--
 *
 * Afterwards
 *
 * need[x]--
 *
 * is always executed.
 *
 * Order matters.
 *
 * Test first.
 * Then decrement.
 *
 * ------------------------------------------------------------
 * Shrinking Transition
 * ------------------------------------------------------------
 *
 * Character leaves.
 *
 * First restore:
 *
 * need[x]++
 *
 * If it becomes positive,
 * the window just lost a required character.
 *
 * Therefore:
 *
 * remainingToMatch++
 *
 * Again,
 * order matters.
 *
 * Restore first.
 * Test second.
 *
 * ------------------------------------------------------------
 * Allowed Moves
 * ------------------------------------------------------------
 *
 * Expand:
 *
 * right++
 *
 * Shrink:
 *
 * left++
 *
 * Record answer only when:
 *
 * remainingToMatch == 0
 *
 * AND
 *
 * windowLength == patternLength
 *
 * ------------------------------------------------------------
 * Forbidden Moves
 * ------------------------------------------------------------
 *
 * Never compare entire frequency arrays each iteration.
 *
 * Never rebuild frequency maps.
 *
 * Never reset the window.
 *
 * Never treat negative counts as errors.
 *
 * Never decrement remainingToMatch after decrementing need[].
 *
 * That destroys the invariant.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * right advances exactly n times.
 *
 * left advances at most n times.
 *
 * Therefore total work is linear.
 *
 * ------------------------------------------------------------
 * Why Naive Solutions Fail
 * ------------------------------------------------------------
 *
 * Sorting every substring:
 *
 * O(n * k log k)
 *
 * Comparing frequency arrays for every window:
 *
 * O(26 * n)
 *
 * While acceptable for lowercase letters,
 * it does unnecessary repeated work.
 *
 * Deficit tracking converts every transition into O(1).
 */

/*
 * ============================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================
 *
 * Mistake 1
 * ---------
 * Decrement need[] before checking whether the character was
 * actually needed.
 *
 * Why it looks correct:
 *
 * Both statements modify the same variable.
 *
 * Why wrong:
 *
 * Crossing from +1 to 0 is exactly the event that satisfies a
 * missing character.
 *
 * Once decremented first,
 * that information disappears.
 *
 * ------------------------------------------------------------
 * Mistake 2
 * ---------
 * Treat negative counts as invalid.
 *
 * Why it looks reasonable:
 *
 * Negative feels impossible.
 *
 * Reality:
 *
 * Negative means surplus.
 *
 * Surplus is perfectly legal.
 *
 * ------------------------------------------------------------
 * Mistake 3
 * ---------
 * Record answer whenever remainingToMatch == 0.
 *
 * Missing condition:
 *
 * Window length must equal pattern length.
 *
 * Counterexample:
 *
 * s = "aaab"
 * p = "ab"
 *
 * Window:
 *
 * "aaab"
 *
 * Remaining may become zero,
 * yet the window is too large.
 *
 * It is not an anagram.
 *
 * ------------------------------------------------------------
 * Mistake 4
 * ---------
 * Restore remainingToMatch before restoring need[] while
 * shrinking.
 *
 * Violated invariant:
 *
 * need[] must always describe the current window.
 *
 * Decision must be made after restoration.
 *
 * ------------------------------------------------------------
 * Interview Trap
 * --------------
 *
 * Interviewer:
 *
 * "Why does remainingToMatch count characters instead of distinct
 * letters?"
 *
 * Correct answer:
 *
 * Because duplicated letters represent independent obligations.
 * "aabc" requires four matches, not three distinct symbols.
 */

    /*
     * ============================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Type in this exact order.
     *
     * ------------------------------------------------------------
     * Step 1
     * ------------------------------------------------------------
     *
     * Create method.
     *
     * List<Integer> findAnagrams(String s, String p)
     *
     * ------------------------------------------------------------
     * Step 2
     * ------------------------------------------------------------
     *
     * Handle trivial impossibility.
     *
     * if (p.length() > s.length())
     *     return empty answer
     *
     * ------------------------------------------------------------
     * Step 3
     * ------------------------------------------------------------
     *
     * Create:
     *
     * List<Integer> answer
     *
     * int[] need = new int[128]
     *
     * ------------------------------------------------------------
     * Step 4
     * ------------------------------------------------------------
     *
     * Build required frequencies.
     *
     * for every character in p
     *     need[c]++
     *
     * ------------------------------------------------------------
     * Step 5
     * ------------------------------------------------------------
     *
     * Initialize:
     *
     * left = 0
     * right = 0
     *
     * remainingToMatch = p.length()
     *
     * ------------------------------------------------------------
     * Step 6
     * ------------------------------------------------------------
     *
     * Expand.
     *
     * Read current character.
     *
     * If need[curr] > 0
     *     remainingToMatch--
     *
     * need[curr]--
     *
     * right++
     *
     * ------------------------------------------------------------
     * Step 7
     * ------------------------------------------------------------
     *
     * While every required character has been matched:
     *
     * if current window length == pattern length
     *      record left
     *
     * Restore left character.
     *
     * need[leftChar]++
     *
     * If restored value > 0
     *      remainingToMatch++
     *
     * left++
     *
     * ------------------------------------------------------------
     * Step 8
     * ------------------------------------------------------------
     *
     * Return answer.
     *
     * ------------------------------------------------------------
     * Debug Reconstruction
     * ------------------------------------------------------------
     *
     * Expansion:
     *
     * check
     * decrement remaining
     * decrement need
     * move right
     *
     * Shrink:
     *
     * increment need
     * check
     * increment remaining
     * move left
     *
     * Expansion and shrinking are exact mirror images.
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * build need
     *
     * remaining = patternLength
     *
     * while right exists
     *
     *     consume character
     *
     *     while remaining == 0
     *
     *         if exact window
     *             record answer
     *
     *         release left character
     *
     * return answers
     */

    /*
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    /*
     * ------------------------------------------------------------
     * Brute Force
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     *
     * Generate every substring having length p.length().
     *
     * Build its frequency table.
     *
     * Compare with the pattern frequency table.
     *
     * ------------------------------------------------------------
     * Invariant
     * ------------------------------------------------------------
     *
     * Every candidate window is checked independently.
     *
     * ------------------------------------------------------------
     * Limitation
     * ------------------------------------------------------------
     *
     * Frequency table rebuilt repeatedly.
     *
     * Large repeated work.
     *
     * ------------------------------------------------------------
     * Complexity
     * ------------------------------------------------------------
     *
     * Time:
     *
     * O(n * k)
     *
     * k = pattern length
     *
     * Space:
     *
     * O(1)
     *
     * ------------------------------------------------------------
     * Interview Usefulness
     * ------------------------------------------------------------
     *
     * Good baseline.
     *
     * Shows understanding before optimization.
     */
    static class BruteForce {

        static List<Integer> findAnagrams(String s, String p) {

            List<Integer> answer = new ArrayList<>();

            if (p.length() > s.length()) {
                return answer;
            }

            int[] target = new int[26];

            for (char c : p.toCharArray()) {
                target[c - 'a']++;
            }

            int windowLength = p.length();

            for (int start = 0; start <= s.length() - windowLength; start++) {

                int[] current = new int[26];

                for (int i = start; i < start + windowLength; i++) {
                    current[s.charAt(i) - 'a']++;
                }

                if (Arrays.equals(target, current)) {
                    answer.add(start);
                }
            }

            return answer;
        }
    }

    /*
     * ------------------------------------------------------------
     * Improved
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     *
     * Maintain a fixed-size sliding window.
     *
     * Update only:
     *
     * one entering character
     *
     * one leaving character
     *
     * Compare frequency arrays after each move.
     *
     * ------------------------------------------------------------
     * Invariant
     * ------------------------------------------------------------
     *
     * Window frequency table always represents exactly the current
     * window.
     *
     * ------------------------------------------------------------
     * Improvement
     * ------------------------------------------------------------
     *
     * No rebuilding of frequency arrays.
     *
     * ------------------------------------------------------------
     * Complexity
     * ------------------------------------------------------------
     *
     * Time:
     *
     * O(26 * n)
     *
     * Alphabet is constant.
     *
     * Space:
     *
     * O(1)
     *
     * ------------------------------------------------------------
     * Interview Usefulness
     * ------------------------------------------------------------
     *
     * Excellent stepping stone toward the optimal invariant-driven
     * solution.
     */
    static class Improved {

        static List<Integer> findAnagrams(String s, String p) {

            List<Integer> answer = new ArrayList<>();

            if (p.length() > s.length()) {
                return answer;
            }

            int[] target = new int[26];
            int[] window = new int[26];

            for (char c : p.toCharArray()) {
                target[c - 'a']++;
            }

            int k = p.length();

            for (int i = 0; i < k; i++) {
                window[s.charAt(i) - 'a']++;
            }

            if (Arrays.equals(target, window)) {
                answer.add(0);
            }

            for (int right = k; right < s.length(); right++) {

                window[s.charAt(right) - 'a']++;

                window[s.charAt(right - k) - 'a']--;

                if (Arrays.equals(target, window)) {
                    answer.add(right - k + 1);
                }
            }

            return answer;
        }
    }

    /*
     * ------------------------------------------------------------
     * Optimal (Interview Preferred)
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     *
     * Track only the remaining deficit instead of repeatedly
     * comparing complete frequency tables.
     *
     * ------------------------------------------------------------
     * Invariant
     * ------------------------------------------------------------
     *
     * need[]
     * always stores the remaining deficit of the CURRENT window.
     *
     * remainingToMatch
     * always equals the total number of still-unsatisfied required
     * characters.
     *
     * ------------------------------------------------------------
     * Correctness
     * ------------------------------------------------------------
     *
     * Expansion reduces deficit.
     *
     * Shrinking restores deficit.
     *
     * Every transition preserves the invariant.
     *
     * A window is an anagram exactly when:
     *
     * remainingToMatch == 0
     *
     * AND
     *
     * window length == pattern length.
     *
     * ------------------------------------------------------------
     * Complexity
     * ------------------------------------------------------------
     *
     * Time:
     *
     * O(n)
     *
     * Every pointer moves only forward.
     *
     * Space:
     *
     * O(1)
     *
     * ------------------------------------------------------------
     * Interview Usefulness
     * ------------------------------------------------------------
     *
     * This is the expected production-quality interview solution.
     */
    static class Optimal {

        static List<Integer> findAnagrams(String s, String p) {

            List<Integer> answer = new ArrayList<>();

            if (p.length() > s.length()) {
                return answer;
            }

            int[] need = new int[128];

            for (char c : p.toCharArray()) {
                need[c]++;
            }

            int left = 0;
            int right = 0;

            int patternLength = p.length();

            int remainingToMatch = patternLength;

            while (right < s.length()) {

                char current = s.charAt(right);

                // 🟢 Consume one character from the search space.

                if (need[current] > 0) {
                    // 🟢 This character satisfies one missing requirement.
                    remainingToMatch--;
                }

                // 🟢 Window now owns one additional copy.
                need[current]--;

                right++;

                while (remainingToMatch == 0) {

                    // 🟢 Every required character exists inside the window.
                    // Exact length guarantees an anagram.
                    if (right - left == patternLength) {
                        answer.add(left);
                    }

                    char removed = s.charAt(left);

                    // 🟢 Restore the deficit because the character leaves.
                    need[removed]++;

                    if (need[removed] > 0) {
                        // 🟢 Window just lost a required character.
                        remainingToMatch++;
                    }

                    left++;
                }
            }

            return answer;
        }
    }
/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Explain the algorithm verbally instead of describing code.
 *
 * ------------------------------------------------------------
 * Pattern
 * ------------------------------------------------------------
 *
 * This is a variable-size sliding window using character deficit
 * tracking.
 *
 * Instead of repeatedly comparing frequency tables,
 * I maintain how many required characters are still missing.
 *
 * ------------------------------------------------------------
 * Invariant
 * ------------------------------------------------------------
 *
 * The array need[] always represents the remaining deficit of the
 * current window.
 *
 * Positive value
 *      Still missing.
 *
 * Zero
 *      Exactly satisfied.
 *
 * Negative
 *      Extra copies already inside the window.
 *
 * remainingToMatch always equals the total number of individual
 * characters still missing.
 *
 * ------------------------------------------------------------
 * Discard Rule
 * ------------------------------------------------------------
 *
 * Whenever remainingToMatch becomes zero,
 * every required character has been collected.
 *
 * I now shrink from the left until removing another character
 * would violate the invariant.
 *
 * During shrinking,
 * if the current window length equals the pattern length,
 * the window must be an anagram.
 *
 * ------------------------------------------------------------
 * Correctness
 * ------------------------------------------------------------
 *
 * Every expansion consumes one character.
 *
 * Every contraction restores exactly one character.
 *
 * Because both transitions preserve the deficit invariant,
 * every reported window contains exactly the same multiset of
 * characters as the pattern.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * right only moves forward.
 *
 * left only moves forward.
 *
 * Each pointer visits every character at most once.
 *
 * Therefore total work is O(n).
 *
 * ------------------------------------------------------------
 * In-place Feasibility
 * ------------------------------------------------------------
 *
 * Yes.
 *
 * Only a constant-sized frequency array and a few integers are
 * maintained.
 *
 * ------------------------------------------------------------
 * Streaming Feasibility
 * ------------------------------------------------------------
 *
 * Yes,
 * as long as characters arrive sequentially.
 *
 * Only the active window state must be preserved.
 *
 * ------------------------------------------------------------
 * When NOT To Use
 * ------------------------------------------------------------
 *
 * If ordering matters.
 *
 * If characters cannot leave the window.
 *
 * If validity depends on position rather than frequencies.
 */

/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 *
 * "Find every anagram."
 *
 * "Same character frequencies."
 *
 * "Substring permutation."
 *
 * ------------------------------------------------------------
 * Pattern
 * ------------------------------------------------------------
 *
 * Sliding Window
 *
 * Character Deficit Tracking
 *
 * ------------------------------------------------------------
 * Invariant
 * ------------------------------------------------------------
 *
 * need[]
 * stores remaining deficit.
 *
 * remainingToMatch
 * stores remaining required characters.
 *
 * ------------------------------------------------------------
 * Search Target
 * ------------------------------------------------------------
 *
 * Every window with:
 *
 * remainingToMatch == 0
 *
 * and
 *
 * windowLength == patternLength
 *
 * ------------------------------------------------------------
 * Discard Rule
 * ------------------------------------------------------------
 *
 * Remove leftmost character.
 *
 * Restore deficit.
 *
 * If deficit becomes positive,
 * stop shrinking.
 *
 * ------------------------------------------------------------
 * Common Trap
 * ------------------------------------------------------------
 *
 * Checking equality of frequency arrays every iteration.
 *
 * ------------------------------------------------------------
 * Edge Cases
 * ------------------------------------------------------------
 *
 * Pattern longer than text.
 *
 * Repeated letters.
 *
 * Entire string is one anagram.
 *
 * Multiple overlapping anagrams.
 *
 * Surplus characters.
 *
 * ------------------------------------------------------------
 * One-Liner
 * ------------------------------------------------------------
 *
 * "Track remaining deficit instead of repeatedly comparing
 * frequency tables."
 *
 * ------------------------------------------------------------
 * Re-derivation Cue
 * ------------------------------------------------------------
 *
 * Consume →
 * Reduce deficit →
 * All matched →
 * Shrink →
 * Restore deficit →
 * Repeat.
 */

/*
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * ------------------------------------------------------------
 * Variation 1
 * ------------------------------------------------------------
 *
 * Check only existence instead of indices.
 *
 * Change:
 *
 * Return immediately after finding the first valid window.
 *
 * Invariant unchanged.
 *
 * ------------------------------------------------------------
 * Variation 2
 * ------------------------------------------------------------
 *
 * Count total anagrams.
 *
 * Change:
 *
 * Increment a counter instead of storing indices.
 *
 * Invariant unchanged.
 *
 * ------------------------------------------------------------
 * Variation 3
 * ------------------------------------------------------------
 *
 * Unicode characters.
 *
 * Replace:
 *
 * int[128]
 *
 * with
 *
 * HashMap<Character,Integer>
 *
 * Invariant remains identical.
 *
 * Only the storage changes.
 *
 * ------------------------------------------------------------
 * Variation 4
 * ------------------------------------------------------------
 *
 * DNA alphabet.
 *
 * Alphabet size = 4.
 *
 * Replace frequency table size accordingly.
 *
 * Pattern unchanged.
 *
 * ------------------------------------------------------------
 * Variation 5
 * ------------------------------------------------------------
 *
 * Lowercase English only.
 *
 * Replace:
 *
 * int[128]
 *
 * with
 *
 * int[26]
 *
 * Slightly smaller memory footprint.
 *
 * Invariant unchanged.
 *
 * ------------------------------------------------------------
 * Variation 6
 * ------------------------------------------------------------
 *
 * Minimum Window Substring.
 *
 * Similarity:
 *
 * Same deficit tracking.
 *
 * Difference:
 *
 * Valid window does NOT require exact length.
 *
 * Objective changes from
 *
 * "all exact windows"
 *
 * to
 *
 * "smallest valid window".
 *
 * ------------------------------------------------------------
 * Pattern Break
 * ------------------------------------------------------------
 *
 * If ordering must be preserved,
 * frequency matching becomes insufficient.
 *
 * Example:
 *
 * Pattern:
 *
 * "abc"
 *
 * Required:
 *
 * exact sequence,
 * not permutation.
 *
 * Sliding-window deficit tracking alone can no longer determine
 * correctness.
 */

/*
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * □ I know the invariant.
 *
 * need[] represents the remaining deficit of the current window.
 *
 * ------------------------------------------------------------
 *
 * □ I know the search target.
 *
 * remainingToMatch == 0
 * &&
 * windowLength == patternLength
 *
 * ------------------------------------------------------------
 *
 * □ I know the discard rule.
 *
 * Restore left character.
 *
 * If restoration creates a positive deficit,
 * stop shrinking.
 *
 * ------------------------------------------------------------
 *
 * □ I know why termination is linear.
 *
 * Every pointer moves forward only.
 *
 * ------------------------------------------------------------
 *
 * □ I know why naive rebuilding is slower.
 *
 * It recomputes information that already exists.
 *
 * ------------------------------------------------------------
 *
 * □ I know all important edge cases.
 *
 * Pattern longer than text.
 *
 * Duplicate letters.
 *
 * Overlapping answers.
 *
 * Empty answer.
 *
 * Whole-string answer.
 *
 * ------------------------------------------------------------
 *
 * □ I can debug the invariant.
 *
 * Expansion:
 *
 * Check need first.
 *
 * Then decrement.
 *
 * Shrink:
 *
 * Restore first.
 *
 * Then check.
 *
 * ------------------------------------------------------------
 *
 * □ I can adapt the pattern.
 *
 * Count answers.
 *
 * Return first answer.
 *
 * Unicode alphabet.
 *
 * Different fixed alphabets.
 *
 * ------------------------------------------------------------
 *
 * □ I know the boundary.
 *
 * Frequency equality problems.
 *
 * Not sequence equality problems.
 */

    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    private static void assertListEquals(List<Integer> expected,
                                         List<Integer> actual) {

        if (!expected.equals(actual)) {
            throw new AssertionError(
                    "Expected: " + expected + ", Actual: " + actual);
        }
    }

    public static void main(String[] args) {

        /*
         * Happy Path
         *
         * Classic example from the problem statement.
         */
        assertListEquals(
                List.of(0, 6),
                Optimal.findAnagrams("cbaebabacd", "abc")
        );

        /*
         * Happy Path
         *
         * Multiple overlapping anagrams.
         */
        assertListEquals(
                List.of(0, 1, 2),
                Optimal.findAnagrams("abab", "ab")
        );

        /*
         * Edge Case
         *
         * Pattern longer than text.
         */
        assertListEquals(
                List.of(),
                Optimal.findAnagrams("ab", "abcd")
        );

        /*
         * Edge Case
         *
         * Entire string itself is one anagram.
         */
        assertListEquals(
                List.of(0),
                Optimal.findAnagrams("abc", "cba")
        );

        /*
         * Edge Case
         *
         * No possible anagram.
         */
        assertListEquals(
                List.of(),
                Optimal.findAnagrams("abcdef", "zzz")
        );

        /*
         * Interview Trap
         *
         * Duplicate characters inside the pattern.
         */
        assertListEquals(
                List.of(1),
                Optimal.findAnagrams("baa", "aa")
        );

        /*
         * Interview Trap
         *
         * Surplus characters should not invalidate future windows.
         */
        assertListEquals(
                List.of(2),
                Optimal.findAnagrams("xxabc", "abc")
        );

        /*
         * Boundary
         *
         * Single-character pattern.
         */
        assertListEquals(
                List.of(0, 1, 2),
                Optimal.findAnagrams("aaa", "a")
        );

        /*
         * Boundary
         *
         * Repeated identical letters.
         */
        assertListEquals(
                List.of(0, 1),
                Optimal.findAnagrams("aaaa", "aaa")
        );

        /*
         * Boundary
         *
         * Pattern equals text with repeated letters.
         */
        assertListEquals(
                List.of(0),
                Optimal.findAnagrams("aabb", "bbaa")
        );

        /*
         * Cross Verification
         *
         * Every implementation should produce identical answers.
         */
        String s = "cbaebabacd";
        String p = "abc";

        List<Integer> brute = BruteForce.findAnagrams(s, p);
        List<Integer> improved = Improved.findAnagrams(s, p);
        List<Integer> optimal = Optimal.findAnagrams(s, p);

        assertListEquals(brute, improved);
        assertListEquals(improved, optimal);

        System.out.println("All assertions passed.");
    }

}

