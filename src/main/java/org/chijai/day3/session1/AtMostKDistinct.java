package org.chijai.day3.session1;

/**
 * ============================================================
 * 📘 LONGEST SUBSTRING WITH AT MOST K DISTINCT CHARACTERS
 * ============================================================
 *
 * Version: v2 — preserved original implementation style
 *
 * SINGLE CONSOLIDATED JAVA CHAPTER FILE
 * ------------------------------------------------------------
 * • IntelliJ-ready
 * • Self-contained
 * • Offline-solvable
 * • One public class only
 * • All other classes are static inner classes
 *
 * This file is a COMPLETE algorithm chapter.
 * Not notes. Not snippets. Not shortcuts.
 *
 * ============================================================
 */
public class AtMostKDistinct {

    /*
     * ============================================================
     * 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
     * ============================================================
     *
     * 🔗 Link:
     * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/
     *
     * 🧩 Difficulty:
     * Medium
     *
     * 🏷️ Tags:
     * Sliding Window, Frequency Counting, Two Pointers, String
     *
     * ------------------------------------------------------------
     * Description:
     *
     * Given a string s and an integer k, return the length of the longest
     * substring of s that contains at most k distinct characters.
     *
     * ------------------------------------------------------------
     * Example 1:
     *
     * Input: s = "eceba", k = 2
     * Output: 3
     * Explanation: The substring is "ece" with length 3.
     *
     * ------------------------------------------------------------
     * Example 2:
     *
     * Input: s = "aa", k = 1
     * Output: 2
     * Explanation: The substring is "aa" with length 2.
     *
     * ------------------------------------------------------------
     * Constraints:
     *
     * • 1 <= s.length <= 5 * 10^4
     * • 0 <= k <= 50
     * • s consists of English letters, digits, symbols and spaces.
     *
     * ------------------------------------------------------------
     */

    /*
     * ============================================================
     * 3️⃣ 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern Name:
     * Variable-Size Sliding Window
     *
     * ------------------------------------------------------------
     * Core Idea:
     * Maintain a window that satisfies:
     *
     *     distinctCount <= k
     *
     * Expand right to explore a larger window.
     * Shrink left only when the constraint is violated.
     *
     * ------------------------------------------------------------
     * Why It Works:
     *
     * Adding a character can increase the number of distinct characters
     * and make the window invalid.
     *
     * Removing characters from the left can only help restore validity.
     *
     * ------------------------------------------------------------
     * 🧭 Pattern Recognition Signals:
     *
     * • Longest substring
     * • Contiguous range
     * • "At most K"
     * • Frequency / distinct-count constraint
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 4️⃣ 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * 🟢 Mental Model:
     *
     * A flexible window stretches right to explore.
     *
     * If too many distinct characters enter,
     * move left until the window becomes valid again.
     *
     * ------------------------------------------------------------
     * 🟢 Invariant:
     *
     * After the shrinking while-loop finishes:
     *
     *     distinctCount <= k
     *
     * ------------------------------------------------------------
     * 🟢 Variable Roles:
     *
     * left
     *     Start of the current window.
     *
     * right
     *     End of the current window, exclusive.
     *
     * frequency[c]
     *     Number of occurrences of character c inside the window.
     *
     * distinctCount
     *     Number of characters whose frequency is greater than zero.
     *
     * maxWindowLength
     *     Best valid window length seen so far.
     *
     * ------------------------------------------------------------
     * 🟢 Termination:
     *
     * right only moves forward.
     * left only moves forward.
     *
     * Therefore each character enters the window once
     * and leaves the window at most once.
     *
     * Time: O(n)
     * Space: O(1) for the fixed-size ASCII frequency array.
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 5️⃣ 🔴 WHY NAIVE SOLUTIONS FAIL
     * ============================================================
     *
     * ❌ Check every substring
     *
     * There are O(n²) substrings.
     * Recounting distinct characters for each one is unnecessary work.
     *
     * ------------------------------------------------------------
     * ❌ Reset the entire window when it becomes invalid
     *
     * This throws away useful overlapping characters.
     *
     * Sliding window keeps the useful suffix and removes only
     * enough characters from the left to restore validity.
     *
     * ------------------------------------------------------------
     * ❌ Record the answer before restoring validity
     *
     * If distinctCount > k, the current window is illegal.
     * It must not be considered for the answer.
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 6️⃣ ✅ PRIMARY SOLUTION
     * ============================================================
     *
     * This is intentionally explicit rather than clever.
     *
     * The code directly mirrors the mental model:
     *
     *     character enters
     *          ↓
     *     update frequency
     *          ↓
     *     update distinctCount if needed
     *          ↓
     *     shrink while invalid
     *          ↓
     *     record valid window
     *
     * ============================================================
     */
    static class Optimal_AtMostKDistinct {

        /*
         * --------------------------------------------------------------------
         * Mental Model
         * --------------------------------------------------------------------
         *
         * Expand the window by adding one character.
         *
         * If the window contains more than K distinct characters,
         * shrink it until the invariant is restored.
         *
         * Every valid window is a candidate answer.
         *
         *             Expand
         *                ↓
         *        Distinct <= K ?
         *           /        \
         *         Yes        No
         *          |          |
         *     Update Answer  Shrink
         *                     ↓
         *              Restore Invariant
         *
         * --------------------------------------------------------------------
         * Invariant
         * --------------------------------------------------------------------
         *
         * frequency[c]
         *     Number of occurrences of character c inside the current window.
         *
         * distinctCount
         *     Number of distinct characters currently inside the window.
         *
         * Window is VALID iff:
         *
         *     distinctCount <= k
         *
         * --------------------------------------------------------------------
         * State Transitions
         * --------------------------------------------------------------------
         *
         * Character enters:
         *
         *     frequency[c]++
         *
         * If frequency becomes 1,
         * a new distinct character entered the window.
         *
         * Character leaves:
         *
         *     frequency[c]--
         *
         * If frequency becomes 0,
         * that distinct character completely left the window.
         *
         * --------------------------------------------------------------------
         */
        static int lengthOfLongestSubstringAtMostKDistinct(String s, int k) {

            if (k == 0 || s.isEmpty())
                return 0;

            int[] frequency = new int[128];

            int left = 0;
            int right = 0;

            int distinctCount = 0;
            int maxWindowLength = 0;

            while (right < s.length()) {

                /*
                 * Expand window.
                 */
                char enteringChar = s.charAt(right);

                frequency[enteringChar]++;

                if (frequency[enteringChar] == 1)
                    distinctCount++;

                right++;

                /*
                 * Restore invariant if the new character made
                 * the window contain more than k distinct characters.
                 */
                while (distinctCount > k) {

                    char leavingChar = s.charAt(left);

                    frequency[leavingChar]--;

                    if (frequency[leavingChar] == 0)
                        distinctCount--;

                    left++;
                }

                /*
                 * The window is valid here because:
                 *
                 *     distinctCount <= k
                 */
                maxWindowLength = Math.max(maxWindowLength, right - left);
            }

            return maxWindowLength;
        }
    }

    /*
     * ============================================================
     * 7️⃣ 🧪 DRY RUN
     * ============================================================
     *
     * Input:
     *
     *     s = "eceba"
     *     k = 2
     *
     * ------------------------------------------------------------
     * Start:
     *
     * left = 0
     * right = 0
     * distinctCount = 0
     * maxWindowLength = 0
     *
     * ------------------------------------------------------------
     * Add 'e'
     *
     * window = "e"
     * frequency[e] = 1
     * distinctCount = 1
     *
     * Valid.
     * maxWindowLength = 1
     *
     * ------------------------------------------------------------
     * Add 'c'
     *
     * window = "ec"
     * distinctCount = 2
     *
     * Valid.
     * maxWindowLength = 2
     *
     * ------------------------------------------------------------
     * Add 'e'
     *
     * window = "ece"
     * frequency[e] becomes 2
     * distinctCount stays 2
     *
     * Valid.
     * maxWindowLength = 3
     *
     * ------------------------------------------------------------
     * Add 'b'
     *
     * window = "eceb"
     * distinctCount = 3
     *
     * Invalid because 3 > 2.
     *
     * Shrink from left:
     *
     * remove 'e'
     * frequency[e] becomes 1
     * distinctCount stays 3
     *
     * remove 'c'
     * frequency[c] becomes 0
     * distinctCount becomes 2
     *
     * window = "eb"
     *
     * Valid again.
     *
     * ------------------------------------------------------------
     * Add 'a'
     *
     * window temporarily becomes "eba"
     * distinctCount = 3
     *
     * Shrink until valid again.
     *
     * Final answer remains:
     *
     *     3
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 8️⃣ 🟣 INTERVIEW ARTICULATION
     * ============================================================
     *
     * Say before coding:
     *
     * "I will use a variable-size sliding window.
     * I keep the frequency of each character currently inside the window
     * and separately track how many distinct characters are present.
     *
     * I expand right normally. If distinctCount becomes greater than k,
     * I move left forward until distinctCount is back to at most k.
     * After that, the window is valid, so I update the maximum length."
     *
     * ------------------------------------------------------------
     * Correctness invariant:
     *
     *     distinctCount <= k
     *
     * after the shrinking loop.
     *
     * ------------------------------------------------------------
     * Why O(n):
     *
     * right visits every character once.
     * left also visits every character at most once.
     *
     * The nested while-loop does NOT make the algorithm O(n²)
     * because left never moves backward.
     *
     * ------------------------------------------------------------
     * Whiteboard memory line:
     *
     *     EXPAND → VIOLATE → SHRINK → RESTORE → RECORD
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 9️⃣ 🔄 VARIATIONS & TWEAKS
     * ============================================================
     *
     * 🟢 Return the substring instead of its length
     *
     * Store the best starting index whenever maxWindowLength improves.
     *
     * ------------------------------------------------------------
     * 🟢 Fruit Into Baskets
     *
     * Same structure with:
     *
     *     k = 2
     *
     * Each fruit type behaves like one distinct character.
     *
     * ------------------------------------------------------------
     * 🟡 Exactly K distinct characters
     *
     * For COUNTING subarrays/substrings:
     *
     *     exactly(K) = atMost(K) - atMost(K - 1)
     *
     * This is a related transformation,
     * not a direct replacement for this longest-window solution.
     *
     * ------------------------------------------------------------
     * 🔴 Longest subsequence
     *
     * Sliding window no longer applies because subsequences
     * do not require contiguity.
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 🔟 ⚫ RELATED / REINFORCEMENT PROBLEMS
     * ============================================================
     *
     * 1. Fruit Into Baskets
     *
     *    Same invariant family:
     *
     *        at most 2 distinct values
     *
     * ------------------------------------------------------------
     * 2. Longest Substring Without Repeating Characters
     *
     *    Related variable-size sliding window,
     *    but the validity condition changes to:
     *
     *        every character frequency <= 1
     *
     * ------------------------------------------------------------
     * 3. Longest Repeating Character Replacement
     *
     *    Same expand/shrink skeleton,
     *    different validity equation.
     *
     * ------------------------------------------------------------
     * 4. Subarrays with K Different Integers
     *
     *    Same "at most K distinct" counting primitive,
     *    then:
     *
     *        exactly(K) = atMost(K) - atMost(K - 1)
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 1️⃣1️⃣ 🟢 LEARNING VERIFICATION
     * ============================================================
     *
     * Can I explain why distinctCount changes only when:
     *
     *     0 → 1
     *
     * or:
     *
     *     1 → 0
     *
     * ?
     *
     * Can I explain why we use a while-loop instead of one if-statement
     * when the window becomes invalid?
     *
     * Can I explain why right - left is the current window length
     * when right is exclusive?
     *
     * Can I derive O(n) even though there is a while-loop
     * inside another while-loop?
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 1️⃣2️⃣ 🧪 main() METHOD + SELF-VERIFYING TESTS
     * ============================================================
     */
    public static void main(String[] args) {

        /*
         * Official-style examples.
         */
        assert Optimal_AtMostKDistinct
                .lengthOfLongestSubstringAtMostKDistinct("eceba", 2) == 3
                : "Failed eceba, k = 2";

        assert Optimal_AtMostKDistinct
                .lengthOfLongestSubstringAtMostKDistinct("aa", 1) == 2
                : "Failed aa, k = 1";

        /*
         * Existing chapter example.
         */
        assert Optimal_AtMostKDistinct
                .lengthOfLongestSubstringAtMostKDistinct("aababbcaacc", 2) == 6
                : "Failed aababbcaacc, k = 2";

        /*
         * Boundary cases.
         */
        assert Optimal_AtMostKDistinct
                .lengthOfLongestSubstringAtMostKDistinct("", 2) == 0
                : "Failed empty string";

        assert Optimal_AtMostKDistinct
                .lengthOfLongestSubstringAtMostKDistinct("abc", 0) == 0
                : "Failed k = 0";

        assert Optimal_AtMostKDistinct
                .lengthOfLongestSubstringAtMostKDistinct("abccab", 4) == 6
                : "Failed full coverage";

        System.out.println("All tests passed ✔");
    }

    /*
     * ============================================================
     * 1️⃣3️⃣ 🧠 CHAPTER COMPLETION CHECKLIST
     * ============================================================
     *
     * • Search target
     *
     *   Longest valid contiguous substring.
     *
     * • Invariant
     *
     *   distinctCount <= k after shrinking.
     *
     * • Expand rule
     *
     *   Add s[right] to frequency and move right.
     *
     * • Distinct-entry rule
     *
     *   frequency changes from 0 to 1.
     *
     * • Shrink rule
     *
     *   While distinctCount > k, remove s[left].
     *
     * • Distinct-exit rule
     *
     *   frequency changes from 1 to 0.
     *
     * • Record rule
     *
     *   Update the answer only after validity is restored.
     *
     * • Time
     *
     *   O(n).
     *
     * • Space
     *
     *   O(1) for the fixed-size frequency array.
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 🧠 RECALL CARD
     * ============================================================
     *
     * Problem signal:
     *
     *     Longest + contiguous + at most K distinct
     *
     * Pattern:
     *
     *     Variable-size sliding window
     *
     * State:
     *
     *     frequency[]
     *     distinctCount
     *     left
     *     right
     *
     * Invariant:
     *
     *     distinctCount <= k
     *
     * Transition:
     *
     *     EXPAND
     *       ↓
     *     if invalid
     *       ↓
     *     SHRINK until valid
     *       ↓
     *     RECORD
     *
     * ============================================================
     */

    /*
     * 🧘 FINAL CLOSURE STATEMENT
     *
     * I do not need to memorize this code line by line.
     *
     * I need to remember:
     *
     *     Each character enters once.
     *     Each character leaves at most once.
     *     Track when a distinct character enters or completely leaves.
     *     Shrink only while distinctCount > k.
     *     Record only after the invariant is restored.
     *
     * From that, the code can be reconstructed.
     */
}
