package org.chijai.day3.session1;

/**
 * =====================================================================================
 * MINIMUM WINDOW SUBSTRING — V3 STUDY CHAPTER
 * LeetCode 76
 * https://leetcode.com/problems/minimum-window-substring/
 * =====================================================================================
 *
 * PRIMARY CLASSIFICATION
 * ----------------------
 * Sliding Window
 *   └── Variable Size
 *       └── MINIMUM VALID WINDOW
 *           └── Requirement / Coverage Accounting
 *
 * IMPORTANT:
 * This is NOT the same subpattern as "longest valid window".
 *
 * LONGEST VALID:
 *   expand
 *   while INVALID -> shrink
 *   record maximum
 *
 * MINIMUM VALID:
 *   expand UNTIL valid
 *   while VALID -> record + shrink
 *
 * =====================================================================================
 * PRIMARY PROBLEM
 * =====================================================================================
 *
 * Given strings s and t, return the minimum substring of s that contains every
 * character of t, including duplicates.
 *
 * Examples:
 *   "ADOBECODEBANC", "ABC" -> "BANC"
 *   "a", "a"               -> "a"
 *   "a", "aa"              -> ""
 *
 * =====================================================================================
 * HALF-OPEN WINDOW
 * =====================================================================================
 *
 * Window = [left, right)
 * size   = right - left
 *
 * right always points to the next element NOT yet inside the window.
 *
 * =====================================================================================
 * CORE INVARIANT
 * =====================================================================================
 *
 * needed[c]
 *   > 0 : still need copies of c
 *   = 0 : exactly satisfied
 *   < 0 : extra copies exist in the window
 *
 * missing
 *   = total required characters still missing, INCLUDING duplicates.
 *
 * Therefore:
 *
 *     missing == 0  <=>  window is VALID
 *
 * =====================================================================================
 * ENTER / EXIT ACCOUNTING
 * =====================================================================================
 *
 * ENTER c:
 *
 *   if (needed[c] > 0)
 *       missing--;
 *
 *   needed[c]--;
 *
 * EXIT c:
 *
 *   needed[c]++;
 *
 *   if (needed[c] > 0)
 *       missing++;
 *
 * =====================================================================================
 * MINIMUM-VALID-WINDOW TEMPLATE
 * =====================================================================================
 *
 * while (right < n) {
 *
 *     add(right);
 *     right++;
 *
 *     while (windowIsValid()) {
 *         recordMinimum();
 *         remove(left);
 *         left++;
 *     }
 * }
 *
 * MEMORY:
 * MINIMUM WINDOW -> SHRINK WHILE VALID.
 *
 * =====================================================================================
 * CORRECT SLIDING-WINDOW TAXONOMY
 * =====================================================================================
 *
 * A) FIXED-SIZE WINDOW
 *    --------------------------------------------------
 *    567. Permutation in String
 *    438. Find All Anagrams in a String
 *    239. Sliding Window Maximum
 *
 *    Window length is predetermined.
 *
 * -------------------------------------------------------------------------------------
 *
 * B) VARIABLE-SIZE -> MAXIMUM VALID WINDOW
 *    --------------------------------------------------
 *    Expand.
 *    While INVALID -> shrink.
 *    Record maximum.
 *
 *    B1) Constraint / violation
 *        3   Longest Substring Without Repeating Characters
 *        159 At Most Two Distinct
 *        340 At Most K Distinct
 *        904 Fruit Into Baskets
 *
 *    B2) Repair / modification budget
 *        1004 Max Consecutive Ones III
 *        2024 Maximize the Confusion of an Exam
 *        424  Longest Repeating Character Replacement
 *        1208 Get Equal Substrings Within Budget
 *        1838 Frequency of the Most Frequent Element
 *
 * -------------------------------------------------------------------------------------
 *
 * C) VARIABLE-SIZE -> MINIMUM VALID WINDOW
 *    --------------------------------------------------
 *    Expand until VALID.
 *    While VALID -> record + shrink.
 *
 *    C1) REQUIREMENT / COVERAGE ACCOUNTING   <-- PRIMARY PROBLEM
 *        76. Minimum Window Substring
 *
 *        Validity:
 *            missing == 0
 *
 *    C2) THRESHOLD ACCUMULATION
 *        209. Minimum Size Subarray Sum
 *
 *        Validity:
 *            windowSum >= target
 *
 *        Safety reason:
 *            all nums are positive.
 *
 *    C3) REPAIR-WINDOW / OUTSIDE-QUOTA
 *        1234. Replace the Substring for Balanced String
 *
 *        Choose the smallest window to replace.
 *        Everything OUTSIDE that window must already fit the final quota.
 *
 *        Validity:
 *            outsideCount[Q/W/E/R] <= n/4
 *
 * -------------------------------------------------------------------------------------
 *
 * D) REFORMULATE INTO A WINDOW
 *    --------------------------------------------------
 *    1658. Minimum Operations to Reduce X to Zero
 *        -> keep longest middle window with sum = total - x
 *
 *    2516. Take K of Each Character From Left and Right
 *        -> maximize the middle window left untouched
 *
 * -------------------------------------------------------------------------------------
 *
 * E) COUNTING WINDOWS — DIFFERENT GOAL
 *    --------------------------------------------------
 *    992. Subarrays with K Different Integers
 *    930. Binary Subarrays With Sum
 *
 *    Usually uses:
 *        exactly(k) = atMost(k) - atMost(k - 1)
 *
 *    Do not mix this with min/max window templates.
 *
 * =====================================================================================
 * PRIMARY SOLUTION
 * =====================================================================================
 */
public class MinimumWindowSubstring {

    static class MinimumWindowSubstring {

        public String minWindow(String s, String t) {

            if (t.length() > s.length()) {
                return "";
            }

            int[] needed = new int[128];

            for (char c : t.toCharArray()) {
                needed[c]++;
            }

            int left = 0;
            int right = 0;

            int missing = t.length();

            int bestStart = 0;
            int bestLength = Integer.MAX_VALUE;

            while (right < s.length()) {

                char entering = s.charAt(right);

                if (needed[entering] > 0) {
                    missing--;
                }

                needed[entering]--;
                right++;

                // MINIMUM valid window -> shrink WHILE valid.
                while (missing == 0) {

                    if (right - left < bestLength) {
                        bestLength = right - left;
                        bestStart = left;
                    }

                    char exiting = s.charAt(left);

                    needed[exiting]++;

                    if (needed[exiting] > 0) {
                        missing++;
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
     =====================================================================================
     REINFORCEMENT 1
     209. MINIMUM SIZE SUBARRAY SUM
     =====================================================================================

     CLASSIFICATION
     --------------
     Sliding Window
       └── Variable Size
           └── Minimum Valid Window
               └── Threshold Accumulation

     PROBLEM
     -------
     Given positive integers nums and target, return the minimum length contiguous
     subarray whose sum >= target. Return 0 if none exists.

     CORE INVARIANT
     --------------
     valid <=> windowSum >= target

     WHY SLIDING WINDOW IS SAFE
     --------------------------
     nums are positive:
     - expanding can only increase sum
     - shrinking can only decrease sum

     TEMPLATE RELATION
     -----------------
     SAME parent pattern as Minimum Window Substring:
       expand until valid
       shrink while valid

     DIFFERENT validity state:
       MWS  -> missing == 0
       209  -> sum >= target
     */
    static class MinimumSizeSubarraySum {

        public int minSubArrayLen(int target, int[] nums) {

            int left = 0;
            int right = 0;

            int sum = 0;
            int best = Integer.MAX_VALUE;

            while (right < nums.length) {

                sum += nums[right];
                right++;

                while (sum >= target) {
                    best = Math.min(best, right - left);

                    sum -= nums[left];
                    left++;
                }
            }

            return best == Integer.MAX_VALUE ? 0 : best;
        }
    }

    /*
     =====================================================================================
     REINFORCEMENT 2
     1234. REPLACE THE SUBSTRING FOR BALANCED STRING
     =====================================================================================

     CLASSIFICATION
     --------------
     Sliding Window
       └── Variable Size
           └── Minimum Valid Window
               └── Repair Window / Outside Quota

     PROBLEM
     -------
     s contains only Q, W, E, R and n is divisible by 4.
     Replace ONE substring with any same-length string.
     Return the minimum substring length needed to make the whole string balanced.

     Balanced means each character appears exactly n / 4 times.

     THE TRICKY REFORMULATION
     ------------------------
     Do NOT ask:
       "What should the replacement window become?"

     Ask:
       "When is the OUTSIDE already safe enough that the inside can repair the rest?"

     If target = n / 4, everything outside the chosen replacement window must satisfy:

       outside[Q] <= target
       outside[W] <= target
       outside[E] <= target
       outside[R] <= target

     Once this is true, the current window contains all excess characters that must
     be repaired, so it is a VALID candidate replacement window.

     FORMULA / VALIDITY
     ------------------
     valid <=> every outsideCount[c] <= n / 4

     Again:
       expand until valid
       shrink while valid
     */
    static class ReplaceSubstringBalancedString {

        public int balancedString(String s) {

            int n = s.length();
            int target = n / 4;

            int[] outside = new int[128];

            for (char c : s.toCharArray()) {
                outside[c]++;
            }

            if (isBalancedOutside(outside, target)) {
                return 0;
            }

            int left = 0;
            int right = 0;
            int best = n;

            while (right < n) {

                outside[s.charAt(right)]--;
                right++;

                while (left < right && isBalancedOutside(outside, target)) {

                    best = Math.min(best, right - left);

                    outside[s.charAt(left)]++;
                    left++;
                }
            }

            return best;
        }

        private boolean isBalancedOutside(int[] outside, int target) {
            return outside['Q'] <= target
                    && outside['W'] <= target
                    && outside['E'] <= target
                    && outside['R'] <= target;
        }
    }

    /*
     =====================================================================================
     REINFORCEMENT 3 — CONTRAST, NOT SAME SUBPATTERN
     567. PERMUTATION IN STRING
     =====================================================================================

     CLASSIFICATION
     --------------
     Sliding Window
       └── FIXED SIZE
           └── Frequency Accounting

     WHY INCLUDED
     ------------
     It uses very similar deficit accounting to Minimum Window Substring,
     BUT the window lifecycle is different.

     76 Minimum Window:
       variable size
       shrink while valid

     567 Permutation:
       exact size = s1.length()
       maintain that fixed size

     This separation prevents pattern mixing.
     */
    static class PermutationInString {

        public boolean checkInclusion(String s1, String s2) {

            if (s1.length() > s2.length()) {
                return false;
            }

            int[] needed = new int[26];

            for (char c : s1.toCharArray()) {
                needed[c - 'a']++;
            }

            int left = 0;
            int right = 0;
            int missing = s1.length();

            while (right < s2.length()) {

                char entering = s2.charAt(right);

                if (needed[entering - 'a'] > 0) {
                    missing--;
                }

                needed[entering - 'a']--;
                right++;

                if (right - left > s1.length()) {

                    char exiting = s2.charAt(left);

                    needed[exiting - 'a']++;

                    if (needed[exiting - 'a'] > 0) {
                        missing++;
                    }

                    left++;
                }

                if (right - left == s1.length() && missing == 0) {
                    return true;
                }
            }

            return false;
        }
    }

    /*
     =====================================================================================
     80:20 RETENTION CARDS
     =====================================================================================

     76 Minimum Window Substring
     ---------------------------
     Trigger:
       minimum substring covering requirements

     Valid:
       missing == 0

     Movement:
       expand until valid
       shrink WHILE valid

     Memory:
       MINIMUM VALID -> SHRINK WHILE VALID.


     209 Minimum Size Subarray Sum
     -----------------------------
     Valid:
       sum >= target

     Why safe:
       all positive


     1234 Balanced String
     --------------------
     Think OUTSIDE, not inside.

     Valid replacement window:
       outside counts all <= n/4


     567 Permutation in String
     -------------------------
     Similar frequency accounting,
     but FIXED SIZE — do not classify as minimum-valid window.
     */

    public static void main(String[] args) {

        testMinimumWindow();
        testMinimumSizeSum();
        testBalancedString();
        testPermutation();

        System.out.println("MinimumWindowSubstringV3: ALL TESTS PASSED");
    }

    private static void testMinimumWindow() {
        MinimumWindowSubstring solver = new MinimumWindowSubstring();

        assertEquals("BANC",
                solver.minWindow("ADOBECODEBANC", "ABC"),
                "MWS classic");

        assertEquals("a",
                solver.minWindow("a", "a"),
                "MWS single");

        assertEquals("",
                solver.minWindow("a", "aa"),
                "MWS impossible");

        assertEquals("cwae",
                solver.minWindow("cabwefgewcwaefgcf", "cae"),
                "MWS accounting trap");
    }

    private static void testMinimumSizeSum() {
        MinimumSizeSubarraySum solver = new MinimumSizeSubarraySum();

        assertEquals(2,
                solver.minSubArrayLen(7, new int[]{2, 3, 1, 2, 4, 3}),
                "209 classic");

        assertEquals(1,
                solver.minSubArrayLen(4, new int[]{1, 4, 4}),
                "209 single");

        assertEquals(0,
                solver.minSubArrayLen(11, new int[]{1, 1, 1, 1, 1, 1, 1, 1}),
                "209 impossible");
    }

    private static void testBalancedString() {
        ReplaceSubstringBalancedString solver = new ReplaceSubstringBalancedString();

        assertEquals(0,
                solver.balancedString("QWER"),
                "1234 already balanced");

        assertEquals(1,
                solver.balancedString("QQWE"),
                "1234 one replacement");

        assertEquals(2,
                solver.balancedString("QQQW"),
                "1234 two replacement");
    }

    private static void testPermutation() {
        PermutationInString solver = new PermutationInString();

        assertEquals(true,
                solver.checkInclusion("ab", "eidbaooo"),
                "567 true");

        assertEquals(false,
                solver.checkInclusion("ab", "eidboaoo"),
                "567 false");
    }

    private static void assertEquals(String expected, String actual, String reason) {
        if (!expected.equals(actual)) {
            throw new AssertionError(
                    reason
                            + " | expected=\"" + expected + "\""
                            + ", actual=\"" + actual + "\""
            );
        }
    }

    private static void assertEquals(int expected, int actual, String reason) {
        if (expected != actual) {
            throw new AssertionError(
                    reason
                            + " | expected=" + expected
                            + ", actual=" + actual
            );
        }
    }

    private static void assertEquals(boolean expected, boolean actual, String reason) {
        if (expected != actual) {
            throw new AssertionError(
                    reason
                            + " | expected=" + expected
                            + ", actual=" + actual
            );
        }
    }
}
