package org.chijai.day3.session1;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * =====================================================================================
 * LONGEST REPEATING CHARACTER REPLACEMENT — V4 FINAL
 * LeetCode 424
 * https://leetcode.com/problems/longest-repeating-character-replacement/
 * =====================================================================================
 *
 * PRIMARY CLASSIFICATION
 * ----------------------
 * Sliding Window
 *   └── Variable Size
 *       └── MAXIMUM VALID WINDOW
 *           └── Repair / Modification Budget
 *               └── Formula-derived window cost
 *
 * Sliding Window itself is standard.
 * The real reasoning is deriving the CURRENT WINDOW COST.
 *
 * =====================================================================================
 * PRIMARY FORMULA
 * =====================================================================================
 *
 * Goal:
 *   Make the whole current window one repeated character.
 *
 * Cheapest choice:
 *   Keep the character already appearing most often.
 *
 * Pay for:
 *   Everything else.
 *
 * Therefore:
 *
 *     replacementsNeeded = windowSize - maxFreq
 *
 * Valid:
 *
 *     windowSize - maxFreq <= k
 *
 * Invalid:
 *
 *     windowSize - maxFreq > k
 *
 * MEMORY:
 *
 *     KEEP THE MAJORITY, PAY FOR THE REST.
 *
 * =====================================================================================
 * HALF-OPEN WINDOW MODEL
 * =====================================================================================
 *
 * Window = [left, right)
 *
 * right points to the next element NOT inside the window.
 *
 * Therefore:
 *
 *     windowSize = right - left
 *
 * =====================================================================================
 * MAXIMUM-VALID-WINDOW TEMPLATE
 * =====================================================================================
 *
 * while (right < n) {
 *
 *     add(right);
 *     right++;
 *
 *     while (windowIsInvalid()) {
 *         remove(left);
 *         left++;
 *     }
 *
 *     best = Math.max(best, right - left);
 * }
 *
 * MEMORY:
 *
 *     MAXIMUM VALID WINDOW
 *     → SHRINK WHILE INVALID
 *
 * =====================================================================================
 * TWO VERSIONS TO KEEP
 * =====================================================================================
 *
 * VERSION 1 — DEFAULT INTERVIEW VERSION
 * -------------------------------------
 * int[128]
 *
 * Use when:
 * - Input is English / ASCII characters.
 * - You want the simplest reusable string-frequency template.
 *
 * Advantages:
 * - Direct indexing: freq[c]
 * - No hashing / boxing
 * - Fixed O(1) memory
 * - Simple under interview pressure
 *
 * maxFreq:
 *
 *     Arrays.stream(freq).max().getAsInt()
 *
 * Since freq always has length 128, getAsInt() is safe.
 *
 * --------------------------------------------------
 *
 * VERSION 2 — GENERIC CHARACTER MAP
 * ---------------------------------
 * Map<Character, Integer>
 *
 * Use when:
 * - You want a generic Character-frequency abstraction.
 * - You do not want to depend on direct array indexing.
 *
 * Advantages:
 * - freq.merge(...) is concise
 * - Collections.max(freq.values()) is readable
 * - Perfectly valid interview solution
 *
 * For this problem, distinct characters are bounded by 26 uppercase letters,
 * so scanning map values is still O(1) with respect to n.
 *
 * =====================================================================================
 * IMPORTANT DESIGN CHOICE — EXACT CURRENT maxFreq
 * =====================================================================================
 *
 * Both V4 solutions recompute maxFreq from the CURRENT window.
 *
 * Therefore:
 *
 *     freq       = exact current-window frequencies
 *     maxFreq    = exact current-window maximum frequency
 *     cost       = exact replacements needed
 *
 * No stale historical maxFreq reasoning is required.
 *
 * This makes the invariant literal:
 *
 *     while (currentWindowSize - currentMaxFreq > k)
 *         shrink
 *
 * =====================================================================================
 * FORMULA-DERIVATION FRAMEWORK
 * =====================================================================================
 *
 * When Sliding Window is obvious but windowIsInvalid() is not:
 *
 *   1. Freeze one candidate window.
 *   2. What final state do I want?
 *   3. What is already correct / free to keep?
 *   4. What must change?
 *   5. What is the minimum repair cost?
 *   6. Compare that cost with the budget.
 *
 * For 424:
 *
 *   final state      = all same
 *   already correct = majority character
 *   repair           = all non-majority characters
 *   cost             = windowSize - maxFreq
 *
 * =====================================================================================
 * CORRECT SLIDING-WINDOW TAXONOMY
 * =====================================================================================
 *
 * A) FIXED-SIZE WINDOW
 *    --------------------------------------------------
 *
 *    567. Permutation in String
 *    438. Find All Anagrams in a String
 *    239. Sliding Window Maximum
 *
 *    Window length is predetermined.
 *
 * -------------------------------------------------------------------------------------
 *
 * B) VARIABLE-SIZE → MAXIMUM VALID WINDOW
 *    --------------------------------------------------
 *
 *    Expand.
 *    While INVALID → shrink.
 *    Record maximum.
 *
 *    B1) CONSTRAINT / VIOLATION COUNT
 *
 *        3. Longest Substring Without Repeating Characters
 *           valid = no duplicates
 *
 *        159. Longest Substring with At Most Two Distinct Characters
 *        340. Longest Substring with At Most K Distinct Characters
 *        904. Fruit Into Baskets
 *           valid = distinctCount <= allowed
 *
 *    B2) REPAIR / MODIFICATION BUDGET   <-- 424 belongs here
 *
 *        1004. Max Consecutive Ones III
 *           cost = zeroCount
 *
 *        2024. Maximize the Confusion of an Exam
 *           cost = min(countT, countF)
 *
 *        424. Longest Repeating Character Replacement
 *           cost = windowSize - maxFreq
 *
 *        1208. Get Equal Substrings Within Budget
 *           cost = sum(abs(s[i] - t[i]))
 *
 *        1838. Frequency of the Most Frequent Element
 *           sort first
 *           cost = nums[right] * windowSize - windowSum
 *
 *        1493. Longest Subarray of 1's After Deleting One Element
 *           candidate allows <= 1 zero
 *           answer = windowSize - 1
 *
 * -------------------------------------------------------------------------------------
 *
 * C) VARIABLE-SIZE → MINIMUM VALID WINDOW
 *    --------------------------------------------------
 *
 *    Expand until VALID.
 *    While VALID → record + shrink.
 *
 *    76. Minimum Window Substring
 *       valid = missing == 0
 *
 *    209. Minimum Size Subarray Sum
 *       valid = windowSum >= target
 *
 * -------------------------------------------------------------------------------------
 *
 * D) REFORMULATE INTO A WINDOW
 *    --------------------------------------------------
 *
 *    1658. Minimum Operations to Reduce X to Zero
 *       keep longest middle window:
 *       windowSum = totalSum - x
 *
 *    2516. Take K of Each Character From Left and Right
 *       maximize middle window left untouched
 *
 * =====================================================================================
 * PRIMARY PROBLEM — VERSION 1
 * DEFAULT INTERVIEW VERSION: int[128]
 * =====================================================================================
 */
public class LongestRepeatingCharacterReplacement {

    static class CharacterReplacementArray {

        public int characterReplacement(String s, int k) {

            int[] freq = new int[128];

            int left = 0;
            int right = 0;
            int best = 0;

            while (right < s.length()) {

                freq[s.charAt(right)]++;
                right++;

                // Invalid if actual replacements needed exceed k.
                while ((right - left) - maxFreq(freq) > k) {
                    freq[s.charAt(left)]--;
                    left++;
                }

                best = Math.max(best, right - left);
            }

            return best;
        }

        private int maxFreq(int[] freq) {
            return Arrays.stream(freq)
                    .max()
                    .getAsInt();
        }
    }

    /*
     =====================================================================================
     PRIMARY PROBLEM — VERSION 2
     GENERIC ALTERNATIVE: Map<Character, Integer>
     =====================================================================================
     */
    static class CharacterReplacementMap {

        public int characterReplacement(String s, int k) {

            Map<Character, Integer> freq = new HashMap<>();

            int left = 0;
            int right = 0;
            int best = 0;

            while (right < s.length()) {

                freq.merge(s.charAt(right), 1, Integer::sum);
                right++;

                // Invalid if actual replacements needed exceed k.
                while ((right - left) - maxFreq(freq) > k) {

                    char exiting = s.charAt(left);

                    freq.merge(exiting, -1, Integer::sum);

                    if (freq.get(exiting) == 0) {
                        freq.remove(exiting);
                    }

                    left++;
                }

                best = Math.max(best, right - left);
            }

            return best;
        }

        private int maxFreq(Map<Character, Integer> freq) {
            return Collections.max(freq.values());
        }
    }

    /*
     =====================================================================================
     REINFORCEMENT 1
     1004. MAX CONSECUTIVE ONES III
     =====================================================================================

     CLASSIFICATION
     --------------
     Sliding Window
       └── Variable Size
           └── Maximum Valid Window
               └── Repair / Modification Budget
                   └── Simple bad-element count

     Goal:
       Make the window all 1s.

     Already correct:
       Existing 1s.

     Repair:
       Zeros.

     Formula:
       cost = zeroCount

     Valid:
       zeroCount <= k

     This is the simplest ancestor of 424.
     */
    static class MaxConsecutiveOnesIII {

        public int longestOnes(int[] nums, int k) {

            int left = 0;
            int right = 0;
            int zeros = 0;
            int best = 0;

            while (right < nums.length) {

                if (nums[right] == 0) {
                    zeros++;
                }

                right++;

                while (zeros > k) {

                    if (nums[left] == 0) {
                        zeros--;
                    }

                    left++;
                }

                best = Math.max(best, right - left);
            }

            return best;
        }
    }

    /*
     =====================================================================================
     REINFORCEMENT 2
     2024. MAXIMIZE THE CONFUSION OF AN EXAM
     =====================================================================================

     CLASSIFICATION
     --------------
     Sliding Window
       └── Variable Size
           └── Maximum Valid Window
               └── Repair / Modification Budget
                   └── Binary majority/minority formula

     Goal:
       Make a T/F window all equal.

     Keep:
       Majority answer.

     Repair:
       Minority answer.

     Formula:
       cost = min(countT, countF)

     Equivalent:
       cost = windowSize - max(countT, countF)
     */
    static class MaximizeConfusionOfExam {

        public int maxConsecutiveAnswers(String answerKey, int k) {

            int left = 0;
            int right = 0;

            int countT = 0;
            int countF = 0;

            int best = 0;

            while (right < answerKey.length()) {

                if (answerKey.charAt(right) == 'T') {
                    countT++;
                } else {
                    countF++;
                }

                right++;

                while (Math.min(countT, countF) > k) {

                    if (answerKey.charAt(left) == 'T') {
                        countT--;
                    } else {
                        countF--;
                    }

                    left++;
                }

                best = Math.max(best, right - left);
            }

            return best;
        }
    }

    /*
     =====================================================================================
     REINFORCEMENT 3
     1208. GET EQUAL SUBSTRINGS WITHIN BUDGET
     =====================================================================================

     CLASSIFICATION
     --------------
     Sliding Window
       └── Variable Size
           └── Maximum Valid Window
               └── Repair / Modification Budget
                   └── Additive per-position cost

     Goal:
       Transform s[left:right) into t[left:right).

     Cost per index:
       abs(s[i] - t[i])

     Window cost:
       sum of per-index transformation costs

     Valid:
       windowCost <= maxCost
     */
    static class EqualSubstringWithinBudget {

        public int equalSubstring(String s, String t, int maxCost) {

            int left = 0;
            int right = 0;

            int windowCost = 0;
            int best = 0;

            while (right < s.length()) {

                windowCost += Math.abs(s.charAt(right) - t.charAt(right));
                right++;

                while (windowCost > maxCost) {

                    windowCost -= Math.abs(
                            s.charAt(left) - t.charAt(left)
                    );

                    left++;
                }

                best = Math.max(best, right - left);
            }

            return best;
        }
    }

    /*
     =====================================================================================
     REINFORCEMENT 4
     1838. FREQUENCY OF THE MOST FREQUENT ELEMENT
     =====================================================================================

     CLASSIFICATION
     --------------
     Sort
       └── Sliding Window
           └── Variable Size
               └── Maximum Valid Window
                   └── Repair / Increment Budget
                       └── Aggregate formula

     Sort first.

     Goal:
       Raise every value in the window to nums[right - 1].

     Total value needed:
       target * windowSize

     Value already present:
       windowSum

     Formula:
       cost = target * windowSize - windowSum

     MEMORY:
       TOTAL NEEDED - ALREADY HAVE.
     */
    static class FrequencyOfMostFrequentElement {

        public int maxFrequency(int[] nums, int k) {

            Arrays.sort(nums);

            int left = 0;
            int right = 0;

            long windowSum = 0;
            int best = 0;

            while (right < nums.length) {

                windowSum += nums[right];
                right++;

                long target = nums[right - 1];

                while (target * (right - left) - windowSum > k) {
                    windowSum -= nums[left];
                    left++;
                }

                best = Math.max(best, right - left);
            }

            return best;
        }
    }

    /*
     =====================================================================================
     REINFORCEMENT 5
     1493. LONGEST SUBARRAY OF 1'S AFTER DELETING ONE ELEMENT
     =====================================================================================

     CLASSIFICATION
     --------------
     Sliding Window
       └── Variable Size
           └── Maximum Valid Window
               └── Allowed violation count
                   └── Mandatory deletion adjustment

     Candidate:
       Allow at most one zero.

     Subtlety:
       Exactly one element must be deleted.

     Therefore:
       answer = candidateWindowSize - 1

     Even if all elements are 1, one element still must be deleted.
     */
    static class LongestSubarrayAfterDeletingOne {

        public int longestSubarray(int[] nums) {

            int left = 0;
            int right = 0;

            int zeros = 0;
            int best = 0;

            while (right < nums.length) {

                if (nums[right] == 0) {
                    zeros++;
                }

                right++;

                while (zeros > 1) {

                    if (nums[left] == 0) {
                        zeros--;
                    }

                    left++;
                }

                best = Math.max(best, (right - left) - 1);
            }

            return best;
        }
    }

    /*
     =====================================================================================
     FORMULA FAMILY — ONE-LINE RETENTION
     =====================================================================================

     1004
     ----
     Goal: all 1
     Cost: zeros

     2024
     ----
     Goal: all same T/F
     Cost: minority count

     424
     ---
     Goal: all same arbitrary char
     Cost: windowSize - maxFreq

     1208
     ----
     Goal: transform s-window -> t-window
     Cost: sum(per-index transform cost)

     1838
     ----
     Goal: raise all sorted values to window max
     Cost: target * size - sum

     1493
     ----
     Candidate permits <= 1 zero
     Answer: windowSize - 1 because one deletion is mandatory

     =====================================================================================
     80:20 RETENTION CARD — 424
     =====================================================================================

     TRIGGER
     -------
     Longest contiguous window + at most k modifications.

     PATTERN
     -------
     Maximum Valid Sliding Window + Repair Budget.

     CORE FORMULA
     ------------
     replacementsNeeded = windowSize - maxFreq

     TEMPLATE
     --------
     expand right

     while (cost > budget):
         remove left

     record maximum

     DEFAULT IMPLEMENTATION
     ----------------------
     int[128]

     GENERIC ALTERNATIVE
     -------------------
     Map<Character, Integer>

     MEMORY LINE
     -----------
     KEEP THE MAJORITY, PAY FOR THE REST.
     */

    public static void main(String[] args) {

        test424();
        test1004();
        test2024();
        test1208();
        test1838();
        test1493();

        System.out.println("LongestRepeatingCharacterReplacementV4: ALL TESTS PASSED");
    }

    private static void test424() {

        CharacterReplacementArray arraySolver =
                new CharacterReplacementArray();

        CharacterReplacementMap mapSolver =
                new CharacterReplacementMap();

        test424Case(
                arraySolver,
                mapSolver,
                "ABAB",
                2,
                4,
                "424 classic 1"
        );

        test424Case(
                arraySolver,
                mapSolver,
                "AABABBA",
                1,
                4,
                "424 classic 2"
        );

        test424Case(
                arraySolver,
                mapSolver,
                "ABCDE",
                0,
                1,
                "424 no budget"
        );

        test424Case(
                arraySolver,
                mapSolver,
                "ABBB",
                2,
                4,
                "424 majority"
        );

        test424Case(
                arraySolver,
                mapSolver,
                "BAAA",
                0,
                3,
                "424 existing run"
        );

        test424Case(
                arraySolver,
                mapSolver,
                "ABCDE",
                4,
                5,
                "424 replace all but one"
        );
    }

    private static void test424Case(
            CharacterReplacementArray arraySolver,
            CharacterReplacementMap mapSolver,
            String s,
            int k,
            int expected,
            String reason
    ) {

        assertEquals(
                expected,
                arraySolver.characterReplacement(s, k),
                reason + " | array"
        );

        assertEquals(
                expected,
                mapSolver.characterReplacement(s, k),
                reason + " | map"
        );
    }

    private static void test1004() {

        MaxConsecutiveOnesIII solver =
                new MaxConsecutiveOnesIII();

        assertEquals(
                6,
                solver.longestOnes(
                        new int[]{
                                1, 1, 1, 0, 0, 0,
                                1, 1, 1, 1, 0
                        },
                        2
                ),
                "1004 classic"
        );

        assertEquals(
                3,
                solver.longestOnes(
                        new int[]{0, 0, 1, 1, 1, 0, 0},
                        0
                ),
                "1004 no flips"
        );
    }

    private static void test2024() {

        MaximizeConfusionOfExam solver =
                new MaximizeConfusionOfExam();

        assertEquals(
                4,
                solver.maxConsecutiveAnswers("TTFF", 2),
                "2024 classic 1"
        );

        assertEquals(
                3,
                solver.maxConsecutiveAnswers("TFFT", 1),
                "2024 classic 2"
        );
    }

    private static void test1208() {

        EqualSubstringWithinBudget solver =
                new EqualSubstringWithinBudget();

        assertEquals(
                3,
                solver.equalSubstring(
                        "abcd",
                        "bcdf",
                        3
                ),
                "1208 classic 1"
        );

        assertEquals(
                1,
                solver.equalSubstring(
                        "abcd",
                        "cdef",
                        3
                ),
                "1208 classic 2"
        );
    }

    private static void test1838() {

        FrequencyOfMostFrequentElement solver =
                new FrequencyOfMostFrequentElement();

        assertEquals(
                3,
                solver.maxFrequency(
                        new int[]{1, 2, 4},
                        5
                ),
                "1838 classic 1"
        );

        assertEquals(
                2,
                solver.maxFrequency(
                        new int[]{1, 4, 8, 13},
                        5
                ),
                "1838 classic 2"
        );
    }

    private static void test1493() {

        LongestSubarrayAfterDeletingOne solver =
                new LongestSubarrayAfterDeletingOne();

        assertEquals(
                3,
                solver.longestSubarray(
                        new int[]{1, 1, 0, 1}
                ),
                "1493 classic 1"
        );

        assertEquals(
                2,
                solver.longestSubarray(
                        new int[]{1, 1, 1}
                ),
                "1493 mandatory deletion"
        );

        assertEquals(
                5,
                solver.longestSubarray(
                        new int[]{
                                0, 1, 1, 1, 0,
                                1, 1, 0, 1
                        }
                ),
                "1493 classic 2"
        );
    }

    private static void assertEquals(
            int expected,
            int actual,
            String reason
    ) {

        if (expected != actual) {
            throw new AssertionError(
                    reason
                            + " | expected=" + expected
                            + ", actual=" + actual
            );
        }
    }
}
