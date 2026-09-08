package org.chijai.day3.session1;



import java.util.Arrays;
import java.util.HashSet;

public class LongestSubString {

    /*
     * ================================================================
     * 2. 📘 PRIMARY PROBLEM
     * ================================================================
     *
     * Title
     * -----
     * Longest Substring Without Repeating Characters
     *
     * Difficulty
     * ----------
     * Medium
     *
     * Tags
     * ----
     * Sliding Window
     * Two Pointers
     * HashSet
     * HashMap
     * String
     *
     * Problem
     * -------
     * Given a string s, return the length of the longest substring
     * containing no repeated characters.
     *
     * A substring is a contiguous portion of the string.
     * A subsequence is NOT acceptable.
     *
     * Constraints
     * -----------
     * 0 <= s.length <= 5 * 10^4
     * s consists of English letters, digits, symbols and spaces.
     *
     * Representative Examples
     * -----------------------
     * Input:
     * abcabcbb
     *
     * Output:
     * 3
     *
     * Explanation:
     * abc
     *
     * ------------------------------------------------
     *
     * Input:
     * bbbbb
     *
     * Output:
     * 1
     *
     * Explanation:
     * b
     *
     * ------------------------------------------------
     *
     * Input:
     * pwwkew
     *
     * Output:
     * 3
     *
     * Explanation:
     * wke
     *
     * "pwke" is NOT a substring.
     *
     * ------------------------------------------------
     *
     * Edge Examples
     * -------------
     *
     * ""
     * answer = 0
     *
     * " "
     * answer = 1
     *
     * "dvdf"
     * answer = 3
     *
     * "abba"
     * answer = 2
     *
     * Official LeetCode
     * -----------------
     * https://leetcode.com/problems/longest-substring-without-repeating-characters/
     */



    /*
     * ================================================================
     * 3. ⭐ PRIMARY O(n) SOLUTIONS
     * ================================================================
     *
     * Start here in an interview.
     *
     * All three solutions use the same sliding-window idea.
     * Only the representation of the current window changes.
     *
     * Canonical reconstruction order:
     *
     * 1. HashSet
     * 2. boolean seen array
     * 3. frequency array + explicit duplicate invariant
     */


    // 🔒 CANONICAL INTERVIEW VERSION
    // Pattern: Sliding Window (uniqueness)
    // Invariant: window [left, right) has NO duplicate characters

    static class Optimal_SetBased {

        static int lengthOfLongestSubstring(String s) {

            int maxLen = 0;
            HashSet<Character> window = new HashSet<>();

            int left = 0, right = 0;

            while (right < s.length()) {
                char curr = s.charAt(right);

                // 🔴 Violation: duplicate detected
                while (window.contains(curr)) {
                    window.remove(s.charAt(left));
                    left++;
                }

                // 🟢 Invariant restored
                window.add(curr);
                right++;

                maxLen = Math.max(maxLen, right - left);
            }
            return maxLen;
        }
    }


    // 🟡 SEEN-ARRAY VERSION
    // Invariant: seen[c] == true ⇔ c exists in window

    static class Optimal_SeenArray {

        static int lengthOfLongestSubstring(String s) {

            boolean[] seen = new boolean[128];
            int left = 0, right = 0, maxLen = 0;

            while (right < s.length()) {
                char curr = s.charAt(right);

                while (seen[curr]) {
                    seen[s.charAt(left)] = false;
                    left++;
                }

                seen[curr] = true;
                right++;
                maxLen = Math.max(maxLen, right - left);
            }
            return maxLen;
        }
    }


    // 🔵 INVARIANT-EXPLICIT VERSION
    // Invariant: duplicateCharCount == number of chars with freq > 1
    // Window valid ⇔ duplicateCharCount == 0

    static class Optimal_ExplicitInvariant {

        static int lengthOfLongestSubstring(String s) {

            int[] windowCharCount = new int[128];
            int left = 0, right = 0;
            int maxLen = 0;
            int duplicateCharCount = 0;

            while (right < s.length()) {
                char curr = s.charAt(right);

                // 🔴 Violation introduced
                if (windowCharCount[curr] > 0)
                    duplicateCharCount++;

                windowCharCount[curr]++;
                right++;

                // 🔧 Restore invariant
                while (duplicateCharCount > 0) {
                    char removed = s.charAt(left);

                    if (windowCharCount[removed] > 1)
                        duplicateCharCount--;

                    windowCharCount[removed]--;
                    left++;
                }

                // 🟢 Valid window
                maxLen = Math.max(maxLen, right - left);
            }
            return maxLen;
        }
    }


    /*
     * ================================================================
     * 4. 🔵 CORE PATTERN OVERVIEW
     * ================================================================
     *
     * Pattern
     * -------
     * Variable Size Sliding Window
     *
     * Archetype
     * ---------
     * Grow until constraint breaks.
     * Shrink until constraint is restored.
     * Repeat.
     *
     * Recognition Signals
     * -------------------
     * Look for phrases like:
     *
     * - longest substring
     * - shortest substring
     * - contiguous sequence
     * - at most
     * - at least
     * - exactly
     * - unique characters
     * - frequency restriction
     *
     * When To Use
     * -----------
     * Continuous ranges.
     *
     * Window validity depends only on current contents.
     *
     * Left boundary is allowed to move only forward.
     *
     * When NOT To Use
     * ---------------
     * Arbitrary subsequences.
     *
     * Non-local dependencies.
     *
     * Backtracking requirements.
     *
     * ================================================================
     */


    /*
     * ================================================================
     * 5. 🟢 MENTAL MODEL & INVARIANTS
     * ================================================================
     *
     * Mental Model
     * ------------
     *
     * Imagine stretching a rubber band.
     *
     * right expands.
     *
     * If expansion creates an illegal window,
     * move left until legality returns.
     *
     * Never move right backward.
     * Never move left backward.
     *
     * Eventually every possible legal window is examined.
     *
     * ------------------------------------------------------------
     *
     * Window Definition
     * -----------------
     *
     * [left ... right)
     *
     * left inclusive, right exclusive
     *
     * ------------------------------------------------------------
     *
     * State Variables
     * ---------------
     *
     * left
     * ----
     * first index inside current window
     *
     * right
     * -----
     * current expanding position
     *
     * window
     * ------
     * data structure representing characters
     * currently inside window
     *
     * answer
     * ------
     * best legal window encountered so far
     *
     * ------------------------------------------------------------
     *
     * Primary Invariant
     * -----------------
     *
     * Every character inside
     *
     * [left ... right)
     *
     * appears exactly once.
     *
     * If this invariant holds,
     * then
     *
     * right-left
     *
     * is immediately a candidate answer.
     *
     * ------------------------------------------------------------
     *
     * Allowed Moves
     * -------------
     *
     * Add s.charAt(right), then move right by one.
     *
     * If invariant still holds:
     *
     * update answer.
     *
     * Otherwise:
     *
     * repeatedly move left.
     *
     * ------------------------------------------------------------
     *
     * Forbidden Move
     * --------------
     *
     * Never update answer while invariant is broken.
     *
     * That would count an invalid substring.
     *
     * ------------------------------------------------------------
     *
     * Repair Operation
     * ----------------
     *
     * Duplicate created?
     *
     * Remove characters from the left
     * until duplicate disappears.
     *
     * Notice:
     *
     * We never remove from the middle.
     *
     * Only the left boundary changes.
     *
     * ------------------------------------------------------------
     *
     * Termination
     * -----------
     *
     * right reaches end of string.
     *
     * Since left only increases,
     * both pointers advance monotonically.
     *
     * Total pointer movement <= 2n.
     *
     * ------------------------------------------------------------
     *
     * ================================================================
     */


    /*
     * ================================================================
     * 6. 🔴 WHY WRONG SOLUTIONS FAIL
     * ================================================================
     *
     * Mistake 1
     * ---------
     * Remove only one character after seeing duplicate.
     *
     * Why It Looks Correct
     * --------------------
     * Duplicate seems fixed immediately.
     *
     * Violated Invariant
     * ------------------
     * Duplicate may still exist.
     *
     * Example
     * -------
     * abcccb
     *
     * One removal is insufficient.
     *
     * ------------------------------------------------------------
     *
     * Mistake 2
     * ---------
     * Update answer before repairing window.
     *
     * Counterexample
     * --------------
     * abca
     *
     * Window length becomes four,
     * but window is illegal.
     *
     * ------------------------------------------------------------
     *
     * Mistake 3
     * ---------
     * Restart search after every duplicate.
     *
     * Looks Simple
     * ------------
     * Easy implementation.
     *
     * Cost
     * ----
     * O(n²)
     *
     * Sliding window preserves previous work.
     *
     * ------------------------------------------------------------
     *
     * Mistake 4
     * ---------
     * Forget removing characters from tracking structure.
     *
     * Result
     * ------
     * Ghost duplicates remain forever.
     *
     * Window never becomes valid again.
     *
     * ------------------------------------------------------------
     *
     * Mistake 5
     * ---------
     * Confusing substring with subsequence.
     *
     * Interview Trap
     * --------------
     * pwwkew
     *
     * pwke
     *
     * is illegal because it skips characters.
     *
     * ------------------------------------------------------------
     *
     * Mistake 6
     * ---------
     * Thinking HashSet itself solves the problem.
     *
     * Reality
     * -------
     * HashSet only tracks state.
     *
     * The algorithm is driven by
     * the sliding window invariant,
     * not by the data structure.
     */


    /*
     * ================================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * ================================================================
     *
     * Mechanical Typing Order
     * -----------------------
     *
     * 1.
     * Handle empty input.
     *
     * 2.
     * Create window structure.
     *
     * 3.
     * Initialize:
     *
     * left = 0
     * answer = 0
     *
     * 4.
     * Iterate right from left to right.
     *
     * 5.
     * While current character already exists:
     *
     * remove left character
     * increment left
     *
     * 6.
     * Insert current character.
     *
     * 7.
     * Update answer.
     *
     * 8.
     * Return answer.
     *
     * Debugging Checklist
     * ------------------
     *
     * □ remove before incrementing left
     *
     * □ while instead of if
     *
     * □ update answer only after repair
     *
     * □ half-open window length:
     *
     * right-left
     */



    /*
     * ================================================================
     * 7. SUPPORTING / DERIVATION SOLUTIONS
     * ================================================================
     *
     * These are below the primary O(n) solutions intentionally.
     *
     * BruteForce and Improved show the optimization path.
     * OptimalLastSeen is a useful O(n) follow-up, but it is less
     * mechanical to reconstruct than the primary sliding-window forms.
     */

    /**
     * ------------------------------------------------------------
     * Brute Force
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Enumerate every substring.
     *
     * Invariant
     * ---------
     * Candidate substring is checked independently.
     *
     * Limitation
     * ----------
     * Massive repeated work.
     *
     * Complexity
     * ----------
     * Time : O(n^3)
     * Space: O(1)
     *
     * Interview Usefulness
     * --------------------
     * Good starting point before optimization.
     */
    static class BruteForce {

        static int lengthOfLongestSubstring(String s) {

            int answer = 0;

            for (int start = 0; start < s.length(); start++) {

                for (int end = start; end < s.length(); end++) {

                    if (allCharactersUnique(s, start, end)) {
                        answer = Math.max(answer, end - start + 1);
                    }
                }
            }

            return answer;
        }

        private static boolean allCharactersUnique(String s, int start, int end) {

            boolean[] seen = new boolean[128];

            for (int i = start; i <= end; i++) {

                char c = s.charAt(i);

                if (seen[c]) {
                    return false;
                }

                seen[c] = true;
            }

            return true;
        }
    }


    /**
     * ------------------------------------------------------------
     * Improved
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Extend every start position until duplicate appears.
     *
     * Invariant
     * ---------
     * Current substring is unique.
     *
     * Improvement
     * -----------
     * Stops scanning once uniqueness breaks.
     *
     * Complexity
     * ----------
     * Time : O(n²)
     * Space: O(128)
     *
     * Interview Usefulness
     * --------------------
     * Natural bridge toward sliding window.
     */
    static class Improved {

        static int lengthOfLongestSubstring(String s) {

            int answer = 0;

            for (int start = 0; start < s.length(); start++) {

                boolean[] seen = new boolean[128];

                for (int end = start; end < s.length(); end++) {

                    char current = s.charAt(end);

                    if (seen[current]) {
                        break;
                    }

                    seen[current] = true;
                    answer = Math.max(answer, end - start + 1);
                }
            }

            return answer;
        }
    }


    /**
     * ------------------------------------------------------------
     * Optimal Last-Seen Index Version
     * ------------------------------------------------------------
     *
     * Strong interview variant.
     *
     * Instead of shrinking one step at a time,
     * directly jump left beyond the previous occurrence.
     *
     * Pattern
     * -------
     * Sliding Window with Last Seen Index
     *
     * Complexity
     * ----------
     * Time : O(n)
     * Space: O(128)
     */
    static class OptimalLastSeen {

        static int lengthOfLongestSubstring(String s) {

            if (s == null || s.isEmpty()) {
                return 0;
            }

            int[] lastSeen = new int[128];
            Arrays.fill(lastSeen, -1);

            int left = 0;
            int answer = 0;

            for (int right = 0; right < s.length(); right++) {

                char current = s.charAt(right);

                // 🟢 Never move left backward.
                left = Math.max(left, lastSeen[current] + 1);

                answer = Math.max(answer, right - left + 1);

                lastSeen[current] = right;
            }

            return answer;
        }
    }


    /*
     * ================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ================================================================
     *
     * Run with assertions enabled:
     *
     * java -ea LongestSubString
     */

    private static void verifyAllImplementations(String input, int expected) {

        assert Optimal_SetBased.lengthOfLongestSubstring(input) == expected
                : "Optimal_SetBased failed for input: " + input;

        assert Optimal_SeenArray.lengthOfLongestSubstring(input) == expected
                : "Optimal_SeenArray failed for input: " + input;

        assert Optimal_ExplicitInvariant.lengthOfLongestSubstring(input) == expected
                : "Optimal_ExplicitInvariant failed for input: " + input;

        assert BruteForce.lengthOfLongestSubstring(input) == expected
                : "BruteForce failed for input: " + input;

        assert Improved.lengthOfLongestSubstring(input) == expected
                : "Improved failed for input: " + input;

        assert OptimalLastSeen.lengthOfLongestSubstring(input) == expected
                : "OptimalLastSeen failed for input: " + input;
    }

    public static void main(String[] args) {

        /*
         * Representative LeetCode examples.
         */
        verifyAllImplementations("abcabcbb", 3);
        verifyAllImplementations("bbbbb", 1);
        verifyAllImplementations("pwwkew", 3);

        /*
         * Empty string.
         */
        verifyAllImplementations("", 0);

        /*
         * Single character.
         */
        verifyAllImplementations("a", 1);

        /*
         * Single whitespace.
         */
        verifyAllImplementations(" ", 1);

        /*
         * All unique.
         */
        verifyAllImplementations("abcdef", 6);

        /*
         * All duplicates.
         */
        verifyAllImplementations("aaaaaa", 1);

        /*
         * Duplicate appears after several unique characters.
         */
        verifyAllImplementations("dvdf", 3);

        /*
         * Classic interviewer trap.
         */
        verifyAllImplementations("abba", 2);

        /*
         * Duplicate immediately after left pointer.
         */
        verifyAllImplementations("tmmzuxt", 5);

        /*
         * Duplicate near the end.
         */
        verifyAllImplementations("anviaj", 5);

        /*
         * Alternating duplicates.
         */
        verifyAllImplementations("abababab", 2);

        /*
         * Entire string is optimal.
         */
        verifyAllImplementations("qwertyuiop", 10);

        /*
         * Digits.
         */
        verifyAllImplementations("123451234", 5);

        /*
         * Symbols.
         */
        verifyAllImplementations("!@#$%^&*", 8);

        /*
         * Mixed letters and symbols.
         */
        verifyAllImplementations("ab!cd!ef", 5);

        System.out.println("All assertions passed.");
    }

}
