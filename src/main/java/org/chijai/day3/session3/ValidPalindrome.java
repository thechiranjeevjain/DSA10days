package org.chijai.day3.session3;

/**
 * 125. Valid Palindrome — Opposite-End Two Pointers V2
 *
 * Primary classification:
 *
 * twoPointers/
 *   oppositeEnds/
 *     ValidPalindrome.java
 *
 * Core reusable idea:
 *
 *     Compare the outermost relevant pair.
 *     Once verified, discard it forever.
 *
 * One template to own:
 *
 *     skip irrelevant endpoints
 *     -> compare valid pair
 *     -> mismatch = fail
 *     -> match = move both inward
 */
public class ValidPalindrome {

    /*
     * ============================================================
     * 📘 PROBLEM
     * ============================================================
     *
     * A phrase is a palindrome after:
     *
     * 1. ignoring non-alphanumeric characters
     * 2. comparing letters case-insensitively
     *
     * Return true if the remaining sequence reads the same
     * forward and backward.
     *
     * Examples:
     *
     * "A man, a plan, a canal: Panama" -> true
     * "race a car"                     -> false
     * " "                              -> true
     */

    /*
     * ============================================================
     * 🧭 EXACT CLASSIFICATION
     * ============================================================
     *
     * PRIMARY:
     *
     *     Two Pointers
     *
     * SUBTYPE:
     *
     *     Opposite-End Two Pointers
     *
     * ARCHETYPE:
     *
     *     Symmetric Pair Validation
     *
     * ------------------------------------------------------------
     * DO NOT CONFUSE WITH
     * ------------------------------------------------------------
     *
     * Container With Most Water
     *
     *     opposite ends
     *     but only ONE endpoint is discarded using a dominance proof
     *
     * Two Sum II
     *
     *     opposite ends
     *     comparison decides WHICH side to discard
     *
     * Valid Palindrome
     *
     *     opposite ends
     *     valid matching pair lets BOTH endpoints disappear
     *
     * Sliding Window
     *
     *     maintains a dynamic contiguous window satisfying a predicate
     *
     * Binary Search
     *
     *     discards search space using sorted/monotonic order
     */

    /*
     * ============================================================
     * 🧠 CORE MENTAL MODEL
     * ============================================================
     *
     * Imagine peeling matching layers from both sides.
     *
     * Non-alphanumeric characters are invisible.
     *
     * The next meaningful left character MUST match
     * the next meaningful right character.
     *
     * ------------------------------------------------------------
     * CORE INVARIANT
     * ------------------------------------------------------------
     *
     * Everything OUTSIDE:
     *
     *     [left ... right]
     *
     * has already been processed correctly.
     *
     * ------------------------------------------------------------
     * POINTER MEANING
     * ------------------------------------------------------------
     *
     * left:
     *
     *     searches for the next unchecked valid character
     *     from the beginning
     *
     * right:
     *
     *     searches for the next unchecked valid character
     *     from the end
     *
     * ------------------------------------------------------------
     * ALLOWED MOVES
     * ------------------------------------------------------------
     *
     * invalid left
     *     -> left++
     *
     * invalid right
     *     -> right--
     *
     * matching valid pair
     *     -> left++, right--
     *
     * mismatch
     *     -> return false
     *
     * ------------------------------------------------------------
     * ONE-LINER
     * ------------------------------------------------------------
     *
     * "Skip noise, compare mirrors, peel inward."
     */

    /*
     * ============================================================
     * ⚠️ IMPORTANT UNDERSTANDING
     * ============================================================
     *
     * Never compare punctuation.
     *
     * Never skip a valid character.
     *
     * Never move BOTH pointers merely because ONE side is invalid.
     *
     * Example:
     *
     *     "a.,"
     *
     * If right points to punctuation:
     *
     *     only right moves.
     *
     * Moving left too would incorrectly discard the valid 'a'.
     *
     * ------------------------------------------------------------
     *
     * A mismatch is final.
     *
     * Why?
     *
     * The outermost remaining valid characters are forced
     * to mirror each other.
     *
     * If they differ, no inner characters can repair that mismatch.
     */

    /*
     * ============================================================
     * 📈 APPROACH PROGRESSION
     * ============================================================
     *
     * 1. BUILD NORMALIZED STRING
     *
     * Filter valid characters into a new StringBuilder,
     * lowercase them, then compare from both ends.
     *
     * Time:
     *
     *     O(n)
     *
     * Space:
     *
     *     O(n)
     *
     * ------------------------------------------------------------
     *
     * 2. NORMALIZE CASE, FILTER LAZILY
     *
     * Convert the whole string to lowercase once.
     *
     * Then skip punctuation with two pointers.
     *
     * Time:
     *
     *     O(n)
     *
     * Space:
     *
     *     O(n)
     *
     * because lowercasing creates another String.
     *
     * ------------------------------------------------------------
     *
     * 3. OPTIMAL TWO POINTERS
     *
     * Do not build any normalized copy.
     *
     * Skip irrelevant characters lazily and compare
     * case-insensitively only when needed.
     *
     * Time:
     *
     *     O(n)
     *
     * Extra space:
     *
     *     O(1)
     */

    /*
     * ============================================================
     * 🔴 SOLUTION 1 — NORMALIZED COPY
     * ============================================================
     */

    static class BruteForce {

        static boolean isPalindrome(String s) {

            if (s == null) {
                return false;
            }

            StringBuilder normalized = new StringBuilder();

            for (int i = 0; i < s.length(); i++) {

                char c = s.charAt(i);

                if (Character.isLetterOrDigit(c)) {
                    normalized.append(
                            Character.toLowerCase(c)
                    );
                }
            }

            int left = 0;
            int right = normalized.length() - 1;

            while (left < right) {

                if (normalized.charAt(left)
                        != normalized.charAt(right)) {
                    return false;
                }

                left++;
                right--;
            }

            return true;
        }
    }

    /*
     * ============================================================
     * 🟡 SOLUTION 2 — LOWERCASE COPY + LAZY FILTER
     * ============================================================
     *
     * This is mainly a progression step.
     *
     * It avoids building a filtered string,
     * but still creates a lowercase copy.
     */

    static class Improved {

        static boolean isPalindrome(String s) {

            if (s == null) {
                return false;
            }

            String normalizedCase =
                    s.toLowerCase(java.util.Locale.ROOT);

            int left = 0;
            int right = normalizedCase.length() - 1;

            while (left < right) {

                while (left < right
                        && !Character.isLetterOrDigit(
                                normalizedCase.charAt(left))) {
                    left++;
                }

                while (left < right
                        && !Character.isLetterOrDigit(
                                normalizedCase.charAt(right))) {
                    right--;
                }

                if (normalizedCase.charAt(left)
                        != normalizedCase.charAt(right)) {
                    return false;
                }

                left++;
                right--;
            }

            return true;
        }
    }

    /*
     * ============================================================
     * 🏆 SOLUTION 3 — OPTIMAL O(1) EXTRA SPACE
     * ============================================================
     *
     * THIS is the implementation to memorize.
     */

    static class Optimal {

        static boolean isPalindrome(String s) {

            if (s == null) {
                return false;
            }

            int left = 0;
            int right = s.length() - 1;

            while (left < right) {

                char leftChar = s.charAt(left);
                char rightChar = s.charAt(right);

                // Skip invalid left character only.
                if (!Character.isLetterOrDigit(leftChar)) {
                    left++;
                    continue;
                }

                // Skip invalid right character only.
                if (!Character.isLetterOrDigit(rightChar)) {
                    right--;
                    continue;
                }

                // Both are valid -> compare normalized pair.
                if (Character.toLowerCase(leftChar)
                        != Character.toLowerCase(rightChar)) {
                    return false;
                }

                // Pair verified -> peel inward.
                left++;
                right--;
            }

            return true;
        }
    }

    /*
     * ============================================================
     * 🎯 ONE TEMPLATE TO OWN
     * ============================================================
     *
     * int left = 0;
     * int right = n - 1;
     *
     * while (left < right) {
     *
     *     read leftChar
     *     read rightChar
     *
     *     if left invalid:
     *         left++
     *         continue
     *
     *     if right invalid:
     *         right--
     *         continue
     *
     *     normalize + compare pair
     *
     *     mismatch:
     *         return false
     *
     *     match:
     *         left++
     *         right--
     * }
     *
     * return true;
     *
     * ------------------------------------------------------------
     * REUSABLE QUESTION
     * ------------------------------------------------------------
     *
     * "Which outermost remaining pair MUST satisfy the rule?"
     */

    /*
     * ============================================================
     * 🔗 DIRECT SAME-PATTERN VARIANT — VALID PALINDROME II
     * ============================================================
     *
     * One deletion is allowed.
     *
     * Same two-pointer invariant until the FIRST mismatch.
     *
     * At that mismatch there are exactly two useful possibilities:
     *
     *     skip left
     *
     * OR
     *
     *     skip right
     *
     * Then verify the remaining interval with the SAME
     * palindrome helper.
     */

    static class ValidPalindromeII {

        static boolean validPalindrome(String s) {

            int left = 0;
            int right = s.length() - 1;

            while (left < right) {

                if (s.charAt(left) == s.charAt(right)) {
                    left++;
                    right--;
                    continue;
                }

                return isStrictPalindrome(
                        s,
                        left + 1,
                        right
                ) || isStrictPalindrome(
                        s,
                        left,
                        right - 1
                );
            }

            return true;
        }

        private static boolean isStrictPalindrome(
                String s,
                int left,
                int right) {

            while (left < right) {

                if (s.charAt(left) != s.charAt(right)) {
                    return false;
                }

                left++;
                right--;
            }

            return true;
        }
    }

    /*
     * ============================================================
     * ♻️ REUSABILITY MAP — OPPOSITE-END TWO POINTERS
     * ============================================================
     *
     * VALID PALINDROME
     *
     *     compare mirrored pair
     *     match -> move BOTH
     *
     * ------------------------------------------------------------
     *
     * VALID PALINDROME II
     *
     *     same pair validation
     *     first mismatch -> branch by skipping one endpoint
     *
     * ------------------------------------------------------------
     *
     * TWO SUM II
     *
     *     sum too small -> left++
     *     sum too large -> right--
     *
     * ------------------------------------------------------------
     *
     * CONTAINER WITH MOST WATER
     *
     *     shorter wall is exhausted
     *     -> discard shorter endpoint
     *
     * ------------------------------------------------------------
     *
     * SQUARES OF A SORTED ARRAY
     *
     *     compare magnitudes at both ends
     *     place larger square from the back
     *
     * ------------------------------------------------------------
     *
     * 3SUM
     *
     *     fix one value
     *     then reuse opposite-end two pointers
     *
     * ------------------------------------------------------------
     * SHARED META-QUESTION
     * ------------------------------------------------------------
     *
     * "After inspecting both ends,
     *  which endpoint(s) can be permanently discarded?"
     */

    /*
     * ============================================================
     * 🔴 PATTERN BOUNDARIES
     * ============================================================
     *
     * Works well for:
     *
     *     mirrored validation
     *     sorted-pair search
     *     endpoint elimination
     *
     * Does NOT directly solve:
     *
     *     Longest Palindromic Substring
     *         -> expand around center / DP
     *
     *     Palindrome Linked List
     *         -> fast/slow + reverse second half
     *
     *     Minimum insertions / edit distance
     *         -> DP
     *
     *     Palindromic subsequence
     *         -> interval DP
     */

    /*
     * ============================================================
     * ⚡ RECONSTRUCTION DRILL
     * ============================================================
     *
     * 1. left = 0
     *
     * 2. right = n - 1
     *
     * 3. while left < right
     *
     * 4. read leftChar and rightChar
     *
     * 5. invalid left -> left++ -> continue
     *
     * 6. invalid right -> right-- -> continue
     *
     * 7. both valid -> normalize + compare
     *
     * 8. mismatch -> false
     *
     * 9. match -> move both
     *
     * 10. return true
     */

    /*
     * ============================================================
     * 🎯 INTERVIEW RECALL SHEET
     * ============================================================
     *
     * TRIGGER:
     *
     *     symmetry / mirrored pair validation
     *
     * PATTERN:
     *
     *     Opposite-End Two Pointers
     *
     * INVARIANT:
     *
     *     everything outside [left, right]
     *     has already been verified
     *
     * SKIP RULE:
     *
     *     irrelevant endpoint moves alone
     *
     * MATCH RULE:
     *
     *     matching pair moves together
     *
     * FAILURE:
     *
     *     first valid mismatch -> false
     *
     * ONE-LINER:
     *
     *     "Skip noise, compare mirrors, peel inward."
     *
     * COMPLEXITY:
     *
     *     O(n) time
     *     O(1) extra space
     *
     * STREAMING:
     *
     *     not naturally one-pass streaming;
     *     the algorithm needs access to both ends
     */

    /*
     * ============================================================
     * 🧪 SELF-VERIFYING TESTS
     * ============================================================
     */

    private static void assertEquals(
            boolean expected,
            boolean actual,
            String reason) {

        if (expected != actual) {
            throw new AssertionError(
                    reason
                    + "\nExpected: " + expected
                    + "\nActual:   " + actual
            );
        }
    }

    public static void main(String[] args) {

        assertEquals(
                true,
                Optimal.isPalindrome(
                        "A man, a plan, a canal: Panama"
                ),
                "Classic palindrome"
        );

        assertEquals(
                false,
                Optimal.isPalindrome("race a car"),
                "Classic non-palindrome"
        );

        assertEquals(
                true,
                Optimal.isPalindrome(" "),
                "Empty normalized string"
        );

        assertEquals(
                true,
                Optimal.isPalindrome(".,:;!?"),
                "Only punctuation"
        );

        assertEquals(
                true,
                Optimal.isPalindrome("Aa"),
                "Case insensitive"
        );

        assertEquals(
                true,
                Optimal.isPalindrome("1,2,3,2,1"),
                "Digits with punctuation"
        );

        assertEquals(
                false,
                Optimal.isPalindrome("0P"),
                "Digit-letter mismatch"
        );

        assertEquals(
                false,
                Optimal.isPalindrome(null),
                "Null handling"
        );

        String[] samples = {
                "",
                " ",
                ".,",
                "A",
                "Aa",
                "ab",
                "abc",
                "abba",
                "abcba",
                "0P",
                "1a2",
                "1a1",
                "No 'x' in Nixon",
                "Able was I, ere I saw Elba",
                "Was it a car or a cat I saw?",
                "race a car",
                "A man, a plan, a canal: Panama"
        };

        for (String sample : samples) {

            boolean brute =
                    BruteForce.isPalindrome(sample);

            boolean improved =
                    Improved.isPalindrome(sample);

            boolean optimal =
                    Optimal.isPalindrome(sample);

            assertEquals(
                    brute,
                    improved,
                    "Brute and improved disagree: " + sample
            );

            assertEquals(
                    improved,
                    optimal,
                    "Improved and optimal disagree: " + sample
            );
        }

        assertEquals(
                true,
                ValidPalindromeII.validPalindrome("aba"),
                "Palindrome II already valid"
        );

        assertEquals(
                true,
                ValidPalindromeII.validPalindrome("abca"),
                "Palindrome II delete one"
        );

        assertEquals(
                false,
                ValidPalindromeII.validPalindrome("abc"),
                "Palindrome II impossible"
        );

        System.out.println(
                "All ValidPalindromeV4 tests passed."
        );
    }
}
