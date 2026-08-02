package org.chijai.day3.session1;



import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LongestSubstringVariations {

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
     * 3. 🔵 CORE PATTERN OVERVIEW
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
     * Core Invariant
     * --------------
     * The current window ALWAYS satisfies the problem constraint.
     *
     * Here:
     *
     * Every character inside the window is unique.
     *
     * Why It Works
     * ------------
     * We never need to restart from scratch.
     *
     * Whenever adding one new character violates uniqueness,
     * only the left boundary can repair the violation.
     *
     * Every character enters the window once.
     * Every character leaves the window once.
     *
     * Therefore:
     *
     * O(n)
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
     * Comparison
     * ----------
     *
     * Fixed Window
     * ------------
     * Window size predetermined.
     *
     * Variable Window
     * ---------------
     * Window size determined by invariant.
     *
     * Prefix Sum
     * ----------
     * Works for additive information.
     *
     * Sliding Window
     * --------------
     * Works when validity depends on current interval state.
     *
     * Binary Search
     * -------------
     * Searches answer space.
     *
     * Sliding Window
     * --------------
     * Maintains feasible search space directly.
     */


    /*
     * ================================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
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
     * [left ... right]
     *
     * inclusive
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
     * [left ... right]
     *
     * appears exactly once.
     *
     * If this invariant holds,
     * then
     *
     * right-left+1
     *
     * is immediately a candidate answer.
     *
     * ------------------------------------------------------------
     *
     * Allowed Moves
     * -------------
     *
     * Move right by one.
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
     * Why Only Left Moves?
     * --------------------
     *
     * The newly added character is fixed.
     *
     * The duplicate already exists somewhere
     * inside the current window.
     *
     * The only way to eliminate that earlier copy
     * while keeping maximal future possibilities
     * is shrinking from the left.
     *
     * ------------------------------------------------------------
     *
     * Why Right Never Moves Back?
     * ---------------------------
     *
     * Every prefix ending before current right
     * has already been processed.
     *
     * Revisiting them cannot improve complexity
     * or discover unseen windows.
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
     * Correctness Intuition
     * ---------------------
     *
     * Every maximal legal window ending at each right
     * is constructed exactly once.
     *
     * Therefore every candidate optimum
     * is evaluated.
     *
     * ------------------------------------------------------------
     *
     * Why Brute Force Fails
     * ---------------------
     *
     * Brute force checks every substring.
     *
     * Number of substrings:
     *
     * O(n²)
     *
     * Verifying uniqueness:
     *
     * O(n)
     *
     * Total:
     *
     * O(n³)
     *
     * Even improving verification with a HashSet
     * still leaves
     *
     * O(n²)
     *
     * Sliding window avoids rebuilding
     * nearly identical substrings.
     */


    /*
     * ================================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
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
     * □ inclusive window length:
     *
     * right-left+1
     */


    /*
     * ================================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ================================================================
     *
     * initialize window
     * initialize left
     * initialize answer
     *
     * for every right
     *     while duplicate
     *         shrink
     *     expand
     *     update answer
     *
     * return answer
     */


    /*
     * ================================================================
     * 6. SOLUTION CLASSES
     * ================================================================
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
     * Optimal (Interview Preferred)
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Maintain a variable-size sliding window that always contains
     * unique characters.
     *
     * Pattern
     * -------
     * Variable Size Sliding Window
     *
     * Core Invariant
     * --------------
     * The current window [left...right] contains no duplicate
     * characters.
     *
     * Whenever adding the next character violates the invariant,
     * repeatedly shrink from the left until the invariant becomes
     * true again.
     *
     * Correctness
     * -----------
     * Every valid maximal window ending at each right index is
     * examined exactly once.
     *
     * Complexity
     * ----------
     * Time : O(n)
     * Space: O(min(n, alphabet))
     *
     * Interview Usefulness
     * --------------------
     * Canonical sliding-window template.
     */
    static class OptimalHashSet {

        static int lengthOfLongestSubstring(String s) {

            if (s == null || s.isEmpty()) {
                return 0;
            }

            Set<Character> window = new HashSet<>();

            int left = 0;
            int answer = 0;

            for (int right = 0; right < s.length(); right++) {

                char current = s.charAt(right);

                // 🟢 Invariant:
                // Window must contain only unique characters.
                while (window.contains(current)) {

                    // Remove from the left because only the left
                    // boundary can repair the violated invariant.
                    window.remove(s.charAt(left));
                    left++;
                }

                // Window becomes valid again.
                window.add(current);

                // Safe because invariant now holds.
                answer = Math.max(answer, right - left + 1);
            }

            return answer;
        }
    }


    /**
     * ------------------------------------------------------------
     * Optimal Using Frequency Array
     * ------------------------------------------------------------
     *
     * This version avoids HashSet overhead.
     *
     * Recommended when alphabet is small (ASCII).
     *
     * Pattern remains IDENTICAL.
     *
     * Only the window representation changes.
     */
    static class OptimalFrequencyArray {

        static int lengthOfLongestSubstring(String s) {

            if (s == null || s.isEmpty()) {
                return 0;
            }

            int[] frequency = new int[128];

            int duplicateCount = 0;
            int left = 0;
            int answer = 0;

            for (int right = 0; right < s.length(); right++) {

                char current = s.charAt(right);

                // Existing occurrence means uniqueness breaks.
                if (frequency[current] > 0) {
                    duplicateCount++;
                }

                frequency[current]++;

                // 🟢 Restore invariant.
                while (duplicateCount > 0) {

                    char removed = s.charAt(left);

                    // This character was duplicated.
                    if (frequency[removed] > 1) {
                        duplicateCount--;
                    }

                    frequency[removed]--;
                    left++;
                }

                answer = Math.max(answer, right - left + 1);
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
 * 🟣 INTERVIEW ARTICULATION
 * ================================================================
 *
 * "The pattern is a variable-size sliding window.
 *
 * The invariant is that every character inside the current
 * window is unique.
 *
 * Whenever adding one character introduces a duplicate,
 * I repeatedly move the left pointer until uniqueness is
 * restored.
 *
 * Since both pointers move only forward, every character
 * enters and leaves the window at most once.
 *
 * Therefore the total complexity is O(n).
 *
 * This approach is ideal whenever the validity of a substring
 * depends only on the characters currently inside the window.
 *
 * I would not use this pattern for arbitrary subsequences,
 * non-contiguous selections, or problems whose validity cannot
 * be repaired by shrinking only from the left."
 */


/*
 * ================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ================================================================
 *
 * Trigger
 * -------
 * Longest / shortest substring.
 *
 * Pattern
 * -------
 * Variable-size sliding window.
 *
 * Invariant
 * ---------
 * Window always valid.
 *
 * Search Space
 * ------------
 * Every contiguous window.
 *
 * Discard Rule
 * ------------
 * Remove from left until invariant returns.
 *
 * Common Trap
 * -----------
 * Using if instead of while.
 *
 * Edge Cases
 * ----------
 * ""
 * single character
 * all duplicates
 * all unique
 * duplicate immediately after left
 *
 * One-Liner
 * ---------
 * Expand right.
 * Repair with left.
 * Record valid window.
 *
 * Re-Derivation Cue
 * -----------------
 * Ask:
 *
 * "What must always remain true about my current window?"
 *
 * Everything else follows naturally.
 */


/*
 * ================================================================
 * 🔄 VARIATIONS & TWEAKS
 * ================================================================
 *
 * Pattern Family
 * --------------
 * All these problems share exactly the same sliding-window
 * architecture.
 *
 * Only the invariant changes.
 *
 * ------------------------------------------------------------
 * Variation 1
 * ------------------------------------------------------------
 *
 * Longest Substring Without Repeating Characters
 *
 * Invariant
 * ---------
 * Every frequency <= 1
 *
 * State
 * -----
 * HashSet
 * or frequency array
 *
 * ------------------------------------------------------------
 * Variation 2
 * ------------------------------------------------------------
 *
 * Longest Substring With At Most Two Distinct Characters
 *
 * Invariant
 * ---------
 * Distinct count <= 2
 *
 * State
 * -----
 * Frequency array
 * +
 * distinct counter
 *
 * Shrink Rule
 * -----------
 * While distinct > 2
 * shrink.
 *
 * ------------------------------------------------------------
 * Variation 3
 * ------------------------------------------------------------
 *
 * Longest Substring With At Most K Distinct Characters
 *
 * Invariant
 * ---------
 * Distinct count <= K
 *
 * Only constant changes.
 *
 * Pattern identical.
 *
 * ------------------------------------------------------------
 * Variation 4
 * ------------------------------------------------------------
 *
 * Minimum Window Substring
 *
 * Goal changes.
 *
 * Instead of maximizing a valid window,
 * minimize a valid window.
 *
 * Window grows until valid.
 * Then shrinks greedily.
 *
 * Same architecture.
 *
 * Different optimization target.
 */

    /*
     * ------------------------------------------------------------
     * Variation 5
     * ------------------------------------------------------------
     *
     * Longest Repeating Character Replacement
     *
     * Invariant
     * ---------
     * Window is repairable using at most k replacements.
     *
     * Formula
     * -------
     * windowLength - maxFrequency <= k
     *
     * Pattern
     * -------
     * Same sliding window.
     *
     * State Change
     * ------------
     * Maintain character frequencies and the maximum frequency
     * inside the current window.
     *
     * ------------------------------------------------------------
     * Variation 6
     * ------------------------------------------------------------
     *
     * Permutation in String
     *
     * Invariant
     * ---------
     * Window has exactly the required frequencies.
     *
     * Pattern
     * -------
     * Fixed-size sliding window.
     *
     * ------------------------------------------------------------
     * Variation 7
     * ------------------------------------------------------------
     *
     * Find All Anagrams
     *
     * Invariant
     * ---------
     * Window frequency vector matches target frequency vector.
     *
     * Pattern
     * -------
     * Fixed-size sliding window.
     *
     * ------------------------------------------------------------
     * Pattern Boundary
     * ------------------------------------------------------------
     *
     * Sliding window works because:
     *
     * 1. Search space is contiguous.
     *
     * 2. Left pointer never needs to move backward.
     *
     * 3. Violations can always be repaired by shrinking.
     *
     * If any of these fail,
     * reconsider the pattern.
     */


    /*
     * ================================================================
     * 🧠 SLIDING WINDOW TEMPLATE LIBRARY
     * ================================================================
     *
     * Almost every substring interview problem can be expressed as:
     *
     * while (right expands) {
     *
     *     update entering state
     *
     *     while (window invalid)
     *         remove left
     *
     *     update answer
     * }
     *
     * The ONLY changing component is:
     *
     * "What defines invalid?"
     */


    /**
     * ================================================================
     * Generic Template:
     * Unique Characters
     * ================================================================
     */
    static class SlidingWindowTemplates {

        static int uniqueCharacters(String s) {

            Set<Character> window = new HashSet<>();

            int left = 0;
            int answer = 0;

            for (int right = 0; right < s.length(); right++) {

                char current = s.charAt(right);

                while (window.contains(current)) {
                    window.remove(s.charAt(left));
                    left++;
                }

                window.add(current);

                answer = Math.max(answer, right - left + 1);
            }

            return answer;
        }

        /**
         * ============================================================
         * At Most Two Distinct Characters
         * ============================================================
         */
        static int atMostTwoDistinct(String s) {

            int[] frequency = new int[128];

            int distinct = 0;
            int left = 0;
            int answer = 0;

            for (int right = 0; right < s.length(); right++) {

                char current = s.charAt(right);

                if (frequency[current] == 0) {
                    distinct++;
                }

                frequency[current]++;

                while (distinct > 2) {

                    char removed = s.charAt(left);

                    if (frequency[removed] == 1) {
                        distinct--;
                    }

                    frequency[removed]--;
                    left++;
                }

                answer = Math.max(answer, right - left + 1);
            }

            return answer;
        }

        /**
         * ============================================================
         * At Most K Distinct Characters
         * ============================================================
         */
        static int atMostKDistinct(String s, int k) {

            if (k <= 0) {
                return 0;
            }

            int[] frequency = new int[128];

            int distinct = 0;
            int left = 0;
            int answer = 0;

            for (int right = 0; right < s.length(); right++) {

                char current = s.charAt(right);

                if (frequency[current] == 0) {
                    distinct++;
                }

                frequency[current]++;

                while (distinct > k) {

                    char removed = s.charAt(left);

                    if (frequency[removed] == 1) {
                        distinct--;
                    }

                    frequency[removed]--;
                    left++;
                }

                answer = Math.max(answer, right - left + 1);
            }

            return answer;
        }
    }


/*
 * ================================================================
 * 🧠 MASTERY CHECKLIST
 * ================================================================
 *
 * Can you answer these without looking?
 *
 * ------------------------------------------------------------
 *
 * Pattern?
 *
 * ✔ Variable-size sliding window.
 *
 * ------------------------------------------------------------
 *
 * Invariant?
 *
 * ✔ Window always satisfies the constraint.
 *
 * ------------------------------------------------------------
 *
 * Search Target?
 *
 * ✔ Longest valid contiguous window.
 *
 * ------------------------------------------------------------
 *
 * Discard Rule?
 *
 * ✔ Remove from the left until valid.
 *
 * ------------------------------------------------------------
 *
 * Why left only?
 *
 * ✔ Only shrinking from the left preserves all future
 * possibilities while repairing the current violation.
 *
 * ------------------------------------------------------------
 *
 * Why while instead of if?
 *
 * ✔ One removal may not restore validity.
 *
 * ------------------------------------------------------------
 *
 * Why O(n)?
 *
 * ✔ Every index enters once and leaves once.
 *
 * ------------------------------------------------------------
 *
 * Termination?
 *
 * ✔ Right reaches the end.
 * Left never moves backward.
 *
 * ------------------------------------------------------------
 *
 * Common Bugs?
 *
 * ✔ Update answer before repair.
 *
 * ✔ Forget to decrement frequency.
 *
 * ✔ Use if instead of while.
 *
 * ✔ Move wrong pointer.
 *
 * ------------------------------------------------------------
 *
 * Edge Cases?
 *
 * ✔ Empty string.
 *
 * ✔ One character.
 *
 * ✔ All same.
 *
 * ✔ All unique.
 *
 * ✔ Duplicate immediately after left.
 *
 * ------------------------------------------------------------
 *
 * Pattern Boundary?
 *
 * ✔ Works only when invalid windows can be repaired by
 * shrinking from the left.
 */


/*
 * ================================================================
 * ⚫ PATTERN MAPPING
 * ================================================================
 *
 * Problem
 * --------------------------------------------
 * Longest substring without repeating
 *
 * Invariant
 * --------------------------------------------
 * frequency <= 1
 *
 * ------------------------------------------------
 *
 * Problem
 * --------------------------------------------
 * At most K distinct
 *
 * Invariant
 * --------------------------------------------
 * distinct <= K
 *
 * ------------------------------------------------
 *
 * Problem
 * --------------------------------------------
 * Character replacement
 *
 * Invariant
 * --------------------------------------------
 * windowLength - maxFrequency <= k
 *
 * ------------------------------------------------
 *
 * Problem
 * --------------------------------------------
 * Minimum window substring
 *
 * Invariant
 * --------------------------------------------
 * Required frequencies satisfied
 *
 * ------------------------------------------------
 *
 * The architecture remains identical.
 *
 * Only the validity condition changes.
 */


    /*
     * ================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ================================================================
     *
     * Run with assertions enabled:
     *
     * java -ea LongestSubstringWithoutRepeatingCharacters
     */

    private static void verifyAllImplementations(String input, int expected) {

        assert BruteForce.lengthOfLongestSubstring(input) == expected
                : "BruteForce failed for input: " + input;

        assert Improved.lengthOfLongestSubstring(input) == expected
                : "Improved failed for input: " + input;

        assert OptimalHashSet.lengthOfLongestSubstring(input) == expected
                : "OptimalHashSet failed for input: " + input;

        assert OptimalFrequencyArray.lengthOfLongestSubstring(input) == expected
                : "OptimalFrequencyArray failed for input: " + input;

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
        verifyAllImplementations("ab!cd!ef", 6);

        /*
         * Validate reusable template variants.
         */

        assert SlidingWindowTemplates.uniqueCharacters("abcabcbb") == 3;

        assert SlidingWindowTemplates.uniqueCharacters("bbbbb") == 1;

        assert SlidingWindowTemplates.atMostTwoDistinct("eceba") == 3;

        assert SlidingWindowTemplates.atMostTwoDistinct("ccaabbb") == 5;

        assert SlidingWindowTemplates.atMostKDistinct("eceba", 2) == 3;

        assert SlidingWindowTemplates.atMostKDistinct("ccaabbb", 2) == 5;

        assert SlidingWindowTemplates.atMostKDistinct("aaabbcccc", 1) == 4;

        assert SlidingWindowTemplates.atMostKDistinct("aaabbcccc", 3) == 9;

        System.out.println("All assertions passed.");
    }

}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/
