package org.chijai.day3.session1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * V9 — Final Retention Version
 *
 * Goal:
 * Keep everything worth remembering for interviews,
 * remove repeated explanations of the same invariant.
 */
public class FindAllAnagramsInAString {

    /*
     * ============================================================
     * 📘 PROBLEM
     * ============================================================
     *
     * Given:
     *
     * s -> text
     * p -> pattern
     *
     * Return every starting index where a substring of s
     * is an anagram of p.
     *
     * An anagram must contain exactly the same character
     * frequencies as p.
     *
     * Example:
     *
     * s = "cbaebabacd"
     * p = "abc"
     *
     * answer = [0, 6]
     *
     * Constraints:
     *
     * 1 <= s.length, p.length <= 3 * 10^4
     * lowercase English letters
     *
     * Target:
     *
     * Time  : O(n)
     * Space : O(1) for a fixed alphabet
     */

    /*
     * ============================================================
     * 🧠 PATTERN + REUSABLE MENTAL MODEL
     * ============================================================
     *
     * PRIMARY CLASSIFICATION:
     *
     * SLIDING WINDOW
     *   └── FIXED SIZE
     *       └── Frequency / Deficit Accounting
     *
     * Trigger:
     * "anagram"
     * "permutation substring"
     * "same frequencies"
     * "window length must equal pattern length"
     *
     * Window = [left, right)
     * size   = right - left
     * k      = p.length()
     *
     * deficit[c]
     * > 0 -> still need copies
     * = 0 -> exactly satisfied
     * < 0 -> surplus in current window
     *
     * missingCount
     * = total individual required characters still missing.
     *
     * ------------------------------------------------------------
     * ORDER TO REMEMBER
     * ------------------------------------------------------------
     *
     * ENTER = CHECK -> CONSUME
     *
     * if (deficit[entering] > 0)
     *     missingCount--;
     *
     * deficit[entering]--;
     *
     * EXIT = RESTORE -> CHECK
     *
     * deficit[exiting]++;
     *
     * if (deficit[exiting] > 0)
     *     missingCount++;
     *
     * MEMORY:
     *
     * ENTER = CHECK -> --
     * EXIT  = ++    -> CHECK
     *
     * Why?
     *
     * ENTER:
     * ask whether it was needed BEFORE consuming it.
     *
     * EXIT:
     * restore the requirement FIRST,
     * then ask whether the window became deficient.
     *
     * ------------------------------------------------------------
     * FIXED-SIZE WINDOW ORDER
     * ------------------------------------------------------------
     *
     * ADD RIGHT
     *   ↓
     * right++
     *   ↓
     * while (size > k)
     *   SHRINK
     *   ↓
     * EXACT SIZE + missingCount == 0?
     *   ↓
     * RECORD left
     *
     * Canonical retention:
     *
     *   while (right - left > k) {
     *       remove left
     *   }
     *
     * Why while?
     * - It matches the reusable sliding-window invariant style.
     * - It literally means: keep shrinking until size <= k.
     *
     * Why can IF also work here?
     * - Before adding, size <= k.
     * - right advances by exactly one.
     * - Therefore size can become at most k + 1.
     * - So the while loop can execute at most once.
     *
     * Hence, for ordinary fixed-size windows:
     *
     *   while (size > k)
     *
     * and
     *
     *   if (size > k)
     *
     * are equivalent.
     *
     * ------------------------------------------------------------
     * CORRECT RELATED SUBPATTERNS
     * ------------------------------------------------------------
     *
     * SAME EXACT SUBPATTERN:
     *
     * 438. Find All Anagrams in a String
     * 567. Permutation in String
     *
     * Both:
     * - fixed size = pattern length
     * - frequency / deficit accounting
     *
     * Difference:
     * - 438 collects all starts
     * - 567 returns true on first match
     *
     * SAME PARENT FAMILY, DIFFERENT SUBPATTERN:
     *
     * 76. Minimum Window Substring
     *
     * - variable-size window
     * - expand until valid
     * - shrink WHILE valid
     *
     * Do NOT mix 76's lifecycle into 438/567.
     */

    /*
     * ============================================================
     * 🧭 NAME THE STATE BY WHAT IT MEANS
     * ============================================================
     *
     * This is the easiest way to avoid mixing update orders.
     *
     * ------------------------------------------------------------
     * MODEL 1 — WINDOW CONTENT
     * ------------------------------------------------------------
     *
     * Use names like:
     *
     *   windowFreq[c]
     *   distinctCount
     *   duplicateCount
     *
     * Meaning:
     *
     *   windowFreq[c] = how many copies of c are INSIDE the window.
     *
     * Typical problems:
     *
     *   3.   Longest Substring Without Repeating Characters
     *   159. At Most Two Distinct
     *   340. At Most K Distinct
     *   904. Fruit Into Baskets
     *   424. Character Replacement
     *
     * Example EXIT:
     *
     *   windowFreq[exiting]--;
     *
     *   if (windowFreq[exiting] == 0) {
     *       distinctCount--;
     *   }
     *
     * Mental model:
     *
     *   windowFreq = WHAT I HAVE
     *
     * ------------------------------------------------------------
     * MODEL 2 — REQUIREMENT DEFICIT
     * ------------------------------------------------------------
     *
     * Use names like:
     *
     *   deficit[c]
     *   missingCount
     *
     * Meaning:
     *
     *   deficit[c] = how many MORE copies of c are still needed.
     *
     * Typical problems:
     *
     *   438. Find All Anagrams in a String
     *   567. Permutation in String
     *   76.  Minimum Window Substring
     *
     * ENTER:
     *
     *   if (deficit[entering] > 0) {
     *       missingCount--;
     *   }
     *
     *   deficit[entering]--;
     *
     * EXIT:
     *
     *   deficit[exiting]++;
     *
     *   if (deficit[exiting] > 0) {
     *       missingCount++;
     *   }
     *
     * Mental model:
     *
     *   deficit = WHAT I LACK
     *
     * ------------------------------------------------------------
     * RETENTION RULE
     * ------------------------------------------------------------
     *
     *   windowFreq = WHAT I HAVE
     *   deficit    = WHAT I LACK
     *
     * Naming rule:
     *
     *   windowFreq[c]
     *      = count currently INSIDE the window
     *
     *   deficit[c]
     *      = count still MISSING from the requirement
     *
     * Do not name both states freq[].
     *
     * The variable name should tell you which update logic belongs here.
     */

    /*
     * ============================================================
     * ⚠️ COMMON BUGS
     * ============================================================
     *
     * 1. Mixing Minimum Window logic into this fixed-size problem.
     *
     * Wrong instinct:
     *   while (missingCount == 0) { shrink... }
     *
     * Correct:
     *   while (size > k) shrink
     *   then check exact size + missingCount == 0
     *
     * 2. Wrong ENTER / EXIT order.
     *
     * ENTER:
     *   if (deficit[entering] > 0)
     *       missingCount--;
     *   deficit[entering]--;
     *
     * EXIT:
     *   deficit[exiting]++;
     *   if (deficit[exiting] > 0)
     *       missingCount++;
     *
     * MEMORY:
     *   ENTER = CHECK -> --
     *   EXIT  = ++    -> CHECK
     *
     * 3. Using missingCount == 0 alone.
     *
     * Also require:
     *   right - left == k
     *
     * 4. Negative deficit[] is not invalid.
     *
     * Negative means surplus.
     *
     * 5. Duplicates count separately.
     *
     * p = "aabc"
     * missingCount starts at 4.
     */

    /*
     * ============================================================
     * 📈 APPROACH PROGRESSION
     * ============================================================
     *
     * 1. Brute Force
     *
     * For every length-k substring:
     * rebuild its frequency table and compare.
     *
     * Time:
     * O(n * k)
     *
     * ------------------------------------------------------------
     *
     * 2. Fixed-Size Sliding Window
     *
     * Maintain window frequencies incrementally.
     *
     * Add one entering character.
     * Remove one leaving character.
     * Compare two 26-sized arrays.
     *
     * Time:
     * O(26n) = O(n)
     *
     * This is already asymptotically optimal.
     *
     * ------------------------------------------------------------
     *
     * 3. Deficit Window — Preferred Reusable Template
     *
     * Track only:
     *
     * deficit[]
     * missingCount
     *
     * No repeated frequency-array equality check.
     *
     * Time:
     * O(n)
     *
     * Preferred here mainly because the invariant transfers
     * directly to related sliding-window problems.
     */

    /*
     * ============================================================
     * 1. BRUTE FORCE
     * ============================================================
     */

    static class BruteForce {

        static List<Integer> findAnagrams(String s, String p) {

            List<Integer> ans = new ArrayList<>();

            if (p.length() > s.length()) {
                return ans;
            }

            int[] target = new int[26];

            for (char c : p.toCharArray()) {
                target[c - 'a']++;
            }

            int k = p.length();

            for (int start = 0; start <= s.length() - k; start++) {

                int[] window = new int[26];

                for (int i = start; i < start + k; i++) {
                    window[s.charAt(i) - 'a']++;
                }

                if (Arrays.equals(target, window)) {
                    ans.add(start);
                }
            }

            return ans;
        }
    }

    /*
     * ============================================================
     * 2. FIXED-SIZE SLIDING WINDOW
     * ============================================================
     *
     * Also O(n) because alphabet size 26 is constant.
     */

    static class Improved {

        static List<Integer> findAnagrams(String s, String p) {

            List<Integer> ans = new ArrayList<>();

            if (p.length() > s.length()) {
                return ans;
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
                ans.add(0);
            }

            for (int right = k; right < s.length(); right++) {

                window[s.charAt(right) - 'a']++;
                window[s.charAt(right - k) - 'a']--;

                if (Arrays.equals(target, window)) {
                    ans.add(right - k + 1);
                }
            }

            return ans;
        }
    }

    /*
     * ============================================================
     * 3. OPTIMAL / INTERVIEW-PREFERRED REUSABLE TEMPLATE
     * ============================================================
     *
     * Variables:
     *
     * need      -> deficit table
     * missingCount -> total required chars still missing
     * in        -> entering character
     * out       -> leaving character
     * left/right-> [left, right)
     * k         -> pattern length
     *
     * Time:
     * O(n)
     *
     * Space:
     * O(1) for this fixed alphabet.
     */

    static class Optimal {

        static List<Integer> findAnagrams(String s, String p) {

            List<Integer> ans = new ArrayList<>();

            if (p.length() > s.length()) {
                return ans;
            }

            // deficit[c] = how many MORE copies of c are still needed.
            // > 0 => missing
            // = 0 => exactly satisfied
            // < 0 => surplus inside the window
            int[] deficit = new int[128];

            for (char c : p.toCharArray()) {
                deficit[c]++;
            }

            int left = 0;
            int right = 0;

            int k = p.length();
            int missingCount = k;

            while (right < s.length()) {

                char entering = s.charAt(right);

                // DEFICIT STATE:
                // ENTER = ASK IF NEEDED -> CONSUME
                if (deficit[entering] > 0) {
                    missingCount--;
                }

                deficit[entering]--;
                right++;

                // FIXED SIZE: shrink until size <= k.
                // With right advancing by 1, this loop runs at most once.
                while (right - left > k) {

                    char exiting = s.charAt(left);

                    // DEFICIT STATE:
                    // EXIT = RESTORE NEED -> ASK IF NOW MISSING
                    deficit[exiting]++;

                    if (deficit[exiting] > 0) {
                        missingCount++;
                    }

                    left++;
                }

                // Exact size + no deficit => exact multiset => anagram.
                if (right - left == k && missingCount == 0) {
                    ans.add(left);
                }
            }

            return ans;
        }
    }

    /*
     * ============================================================
     * 🎯 INTERVIEW RECALL
     * ============================================================
     *
     * CLASSIFICATION:
     *
     * Fixed-Size Sliding Window + Deficit Accounting
     *
     * ENTER:
     *
     * if (deficit[entering] > 0)
     *     missingCount--;
     *
     * deficit[entering]--;
     *
     * Memory:
     * CHECK -> --
     *
     * EXIT:
     *
     * deficit[exiting]++;
     *
     * if (deficit[exiting] > 0)
     *     missingCount++;
     *
     * Memory:
     * ++ -> CHECK
     *
     * WINDOW ORDER:
     *
     * add right
     * -> right++
     * -> while size > k, shrink
     * -> if size == k && missingCount == 0, record
     *
     * ONE-LINER:
     *
     * "Keep an exact-size window and track how many
     * pattern characters are still missing."
     */

    /*
     * ============================================================
     * 🔄 USEFUL VARIATIONS
     * ============================================================
     *
     * 567. Permutation in String
     *
     * SAME exact subpattern.
     *
     * Keep the same:
     * - fixed-size window
     * - deficit[]
     * - missingCount
     * - ENTER / EXIT order
     *
     * Return true instead of collecting indices.
     *
     * ------------------------------------------------------------
     *
     * 76. Minimum Window Substring
     *
     * SAME deficit machinery,
     * DIFFERENT window lifecycle.
     *
     * 438 / 567:
     *   fixed size
     *   enforce size <= k
     *   check exact-size validity
     *
     * 76:
     *   variable size
     *   expand until valid
     *   shrink while valid
     *
     * ------------------------------------------------------------
     *
     * Count matches:
     * increment a counter instead of storing indices.
     *
     * Full Unicode:
     * process both strings with codePoints()
     * and use Map<Integer, Integer>.
     */

    /*
     * ============================================================
     * 🧠 THREE SLIDING-WINDOW LIFECYCLES TO RETAIN
     * ============================================================
     *
     * FIXED SIZE
     *
     *   while (size > k)
     *       shrink
     *
     *   evaluate when size == k
     *
     * ------------------------------------------------------------
     *
     * MAXIMUM VALID WINDOW
     *
     *   while (invalid)
     *       shrink
     *
     *   record maximum
     *
     * ------------------------------------------------------------
     *
     * MINIMUM VALID WINDOW
     *
     *   while (valid)
     *       record minimum
     *       shrink
     *
     * ------------------------------------------------------------
     *
     * FIXED-SIZE NOTE:
     *
     * When right advances by exactly one and previous size <= k,
     * the window can overshoot k by at most one.
     *
     * Therefore:
     *
     *   while (size > k)
     *
     * can execute at most once, so:
     *
     *   if (size > k)
     *
     * is also correct.
     *
     * Learn WHILE as the reusable template.
     * Remember IF as the concise equivalent.
     */

    /*
     * ============================================================
     * 🧩 REINFORCEMENT MAP — SAME → SLIGHTLY DIFFERENT
     * ============================================================
     *
     * PRIMARY:
     *
     * 438. Find All Anagrams in a String
     *
     * Sliding Window
     *   └── Fixed Size
     *       └── Deficit Accounting
     *
     * ------------------------------------------------------------
     * 1. SAME EXACT SUBPATTERN
     * ------------------------------------------------------------
     *
     * 567. Permutation in String
     *
     * Same:
     * - fixed size = pattern length
     * - deficit[]
     * - missingCount
     * - ENTER = CHECK -> --
     * - EXIT  = ++ -> CHECK
     *
     * Only difference:
     * - 438 collects all matching starts
     * - 567 returns true on first match
     *
     * ------------------------------------------------------------
     * 2. SAME FIXED-SIZE WINDOW, SIMPLER STATE
     * ------------------------------------------------------------
     *
     * 1456. Maximum Number of Vowels in a Substring of Given Length
     *
     * Same:
     * - exact fixed window size k
     * - add one right
     * - remove one left when size > k
     *
     * Different:
     * - no deficit[]
     * - just track current window content:
     *     vowelCount = WHAT I HAVE
     *
     * This reinforces:
     *
     *   fixed-size lifecycle
     *   !=
     *   deficit accounting
     *
     * ------------------------------------------------------------
     * 3. SAME DEFICIT ACCOUNTING, DIFFERENT WINDOW LIFECYCLE
     * ------------------------------------------------------------
     *
     * 76. Minimum Window Substring
     *
     * Same:
     * - deficit[]
     * - missingCount
     * - same ENTER / EXIT accounting
     *
     * Different:
     * - variable-size window
     * - no fixed k
     * - expand until valid
     * - shrink WHILE valid
     *
     * This is the most important contrast.
     *
     * ------------------------------------------------------------
     * 4. SAME SLIDING-WINDOW PARENT, DIFFERENT STATE MODEL
     * ------------------------------------------------------------
     *
     * 3. Longest Substring Without Repeating Characters
     *
     * Uses:
     *
     *   windowFreq = WHAT I HAVE
     *
     * not:
     *
     *   deficit = WHAT I LACK
     *
     * Lifecycle:
     * - variable size
     * - expand
     * - while invalid, shrink
     * - record maximum
     *
     * ------------------------------------------------------------
     * RETENTION MATRIX
     * ------------------------------------------------------------
     *
     * 438 / 567
     *   FIXED SIZE
     *   DEFICIT
     *
     * 1456
     *   FIXED SIZE
     *   WINDOW CONTENT
     *
     * 76
     *   VARIABLE SIZE — MINIMUM VALID
     *   DEFICIT
     *
     * 3
     *   VARIABLE SIZE — MAXIMUM VALID
     *   WINDOW CONTENT
     *
     * This 2 x 2 distinction is more useful than memorizing four solutions:
     *
     *                 WINDOW CONTENT       DEFICIT
     *
     * FIXED SIZE      1456                 438 / 567
     *
     * VARIABLE        3                    76
     *
     */

    /*
     * ============================================================
     * REINFORCEMENT 1 — 567. PERMUTATION IN STRING
     * SAME EXACT SUBPATTERN
     * ============================================================
     */
    static class PermutationInString {

        static boolean checkInclusion(String p, String s) {

            if (p.length() > s.length()) {
                return false;
            }

            int[] deficit = new int[128];

            for (char c : p.toCharArray()) {
                deficit[c]++;
            }

            int left = 0;
            int right = 0;

            int k = p.length();
            int missingCount = k;

            while (right < s.length()) {

                char entering = s.charAt(right);

                if (deficit[entering] > 0) {
                    missingCount--;
                }

                deficit[entering]--;
                right++;

                while (right - left > k) {

                    char exiting = s.charAt(left);

                    deficit[exiting]++;

                    if (deficit[exiting] > 0) {
                        missingCount++;
                    }

                    left++;
                }

                if (right - left == k && missingCount == 0) {
                    return true;
                }
            }

            return false;
        }
    }

    /*
     * ============================================================
     * REINFORCEMENT 2 — 1456. MAXIMUM VOWELS IN LENGTH K
     * SAME FIXED-SIZE WINDOW, DIFFERENT STATE
     * ============================================================
     *
     * windowFreq-style thinking:
     *
     * vowelCount = WHAT I HAVE
     *
     * No requirement deficit exists.
     */
    static class MaximumVowels {

        static int maxVowels(String s, int k) {

            int left = 0;
            int right = 0;

            int vowelCount = 0;
            int best = 0;

            while (right < s.length()) {

                if (isVowel(s.charAt(right))) {
                    vowelCount++;
                }

                right++;

                while (right - left > k) {

                    if (isVowel(s.charAt(left))) {
                        vowelCount--;
                    }

                    left++;
                }

                if (right - left == k) {
                    best = Math.max(best, vowelCount);
                }
            }

            return best;
        }

        private static boolean isVowel(char c) {
            return c == 'a'
                    || c == 'e'
                    || c == 'i'
                    || c == 'o'
                    || c == 'u';
        }
    }

    /*
     * ============================================================
     * REINFORCEMENT 3 — 76. MINIMUM WINDOW SUBSTRING
     * SAME DEFICIT STATE, DIFFERENT LIFECYCLE
     * ============================================================
     *
     * Same accounting:
     *
     * ENTER = CHECK -> --
     * EXIT  = ++ -> CHECK
     *
     * Different lifecycle:
     *
     * expand
     * while (missingCount == 0)
     *     record minimum
     *     shrink
     */
    static class MinimumWindowSubstring {

        static String minWindow(String s, String t) {

            if (t.length() > s.length()) {
                return "";
            }

            int[] deficit = new int[128];

            for (char c : t.toCharArray()) {
                deficit[c]++;
            }

            int left = 0;
            int right = 0;

            int missingCount = t.length();

            int bestStart = 0;
            int bestLength = Integer.MAX_VALUE;

            while (right < s.length()) {

                char entering = s.charAt(right);

                if (deficit[entering] > 0) {
                    missingCount--;
                }

                deficit[entering]--;
                right++;

                while (missingCount == 0) {

                    if (right - left < bestLength) {
                        bestLength = right - left;
                        bestStart = left;
                    }

                    char exiting = s.charAt(left);

                    deficit[exiting]++;

                    if (deficit[exiting] > 0) {
                        missingCount++;
                    }

                    left++;
                }
            }

            return bestLength == Integer.MAX_VALUE
                    ? ""
                    : s.substring(bestStart, bestStart + bestLength);
        }
    }

    /*
     * ============================================================
     * REINFORCEMENT 4 — 3. LONGEST SUBSTRING WITHOUT REPEATING
     * DIFFERENT STATE MODEL
     * ============================================================
     *
     * windowFreq = WHAT I HAVE
     *
     * Invalid when the entering character appears more than once.
     */
    static class LongestSubstringWithoutRepeating {

        static int lengthOfLongestSubstring(String s) {

            int[] windowFreq = new int[128];

            int left = 0;
            int right = 0;

            int best = 0;

            while (right < s.length()) {

                char entering = s.charAt(right);
                windowFreq[entering]++;
                right++;

                while (windowFreq[entering] > 1) {

                    char exiting = s.charAt(left);
                    windowFreq[exiting]--;
                    left++;
                }

                best = Math.max(best, right - left);
            }

            return best;
        }
    }

    /*
     * ============================================================
     * 🧪 TESTS
     * ============================================================
     */

    private static void assertListEquals(List<Integer> expected,
                                         List<Integer> actual) {

        if (!expected.equals(actual)) {
            throw new AssertionError(
                    "Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void assertAllSolutions(List<Integer> expected,
                                           String s,
                                           String p) {

        assertListEquals(expected, BruteForce.findAnagrams(s, p));
        assertListEquals(expected, Improved.findAnagrams(s, p));
        assertListEquals(expected, Optimal.findAnagrams(s, p));
    }


    private static void assertBooleanEquals(boolean expected,
                                            boolean actual) {

        if (expected != actual) {
            throw new AssertionError(
                    "Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void assertIntEquals(int expected,
                                        int actual) {

        if (expected != actual) {
            throw new AssertionError(
                    "Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void assertStringEquals(String expected,
                                           String actual) {

        if (!expected.equals(actual)) {
            throw new AssertionError(
                    "Expected: " + expected + ", Actual: " + actual);
        }
    }

    public static void main(String[] args) {

        // Classic example.
        assertAllSolutions(
                List.of(0, 6),
                "cbaebabacd",
                "abc"
        );

        // Overlapping answers.
        assertAllSolutions(
                List.of(0, 1, 2),
                "abab",
                "ab"
        );

        // Pattern longer than text.
        assertAllSolutions(
                List.of(),
                "ab",
                "abcd"
        );

        // Whole string is one anagram.
        assertAllSolutions(
                List.of(0),
                "abc",
                "cba"
        );

        // No answer.
        assertAllSolutions(
                List.of(),
                "abcdef",
                "zzz"
        );

        // Duplicate requirement.
        assertAllSolutions(
                List.of(1),
                "baa",
                "aa"
        );

        // Surplus before valid window.
        assertAllSolutions(
                List.of(2),
                "xxabc",
                "abc"
        );

        // Single-character pattern.
        assertAllSolutions(
                List.of(0, 1, 2),
                "aaa",
                "a"
        );

        // Repeated identical letters.
        assertAllSolutions(
                List.of(0, 1),
                "aaaa",
                "aaa"
        );

        // Same multiset, different order.
        assertAllSolutions(
                List.of(0),
                "aabb",
                "bbaa"
        );


        // 567 — same exact pattern as 438.
        assertBooleanEquals(
                true,
                PermutationInString.checkInclusion("ab", "eidbaooo")
        );

        assertBooleanEquals(
                false,
                PermutationInString.checkInclusion("ab", "eidboaoo")
        );

        // 1456 — same fixed-size lifecycle, simpler state.
        assertIntEquals(
                3,
                MaximumVowels.maxVowels("abciiidef", 3)
        );

        // 76 — same deficit accounting, variable minimum-valid lifecycle.
        assertStringEquals(
                "BANC",
                MinimumWindowSubstring.minWindow("ADOBECODEBANC", "ABC")
        );

        // 3 — variable maximum-valid window with windowFreq state.
        assertIntEquals(
                3,
                LongestSubstringWithoutRepeating.lengthOfLongestSubstring("abcabcbb")
        );


        System.out.println("All V9 assertions passed.");
    }
}