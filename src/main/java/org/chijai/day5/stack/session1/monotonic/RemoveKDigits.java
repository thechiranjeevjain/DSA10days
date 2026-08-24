package org.chijai.day5.stack.session1.monotonic;

import java.util.*;

/**
 * LeetCode 402 - Remove K Digits
 *
 * -------------------------------------------------------------------------
 * CORE IDEA: GREEDY + MONOTONIC INCREASING STACK
 * -------------------------------------------------------------------------
 *
 * We want the numerically smallest possible result after removing exactly k digits.
 *
 * The most important digits are the LEFTMOST digits.
 *
 * So whenever a smaller digit appears after a larger digit:
 *
 *      ... 4, 3 ...
 *
 * keeping 4 before 3 makes the number larger than necessary.
 *
 * If we are still allowed to remove digits, remove 4.
 *
 * Example:
 *
 *      1432219, k = 3
 *
 * Process digits:
 *
 *      1
 *      4
 *      3 arrives -> remove 4
 *      2 arrives -> remove 3
 *      2
 *      1 arrives -> remove one 2
 *
 * Result:
 *
 *      1219
 *
 * -------------------------------------------------------------------------
 * MONOTONIC STACK RULE
 * -------------------------------------------------------------------------
 *
 * While:
 *
 *      removalsRemaining > 0
 *      AND stack not empty
 *      AND stack top > current digit
 *
 * remove stack top.
 *
 * Then push current digit.
 *
 * This keeps the kept prefix as small as possible.
 *
 * -------------------------------------------------------------------------
 * WHY STRICTLY '>' AND NOT '>=' ?
 * -------------------------------------------------------------------------
 *
 * Equal digits do not improve the prefix if we remove the earlier one.
 *
 * Example:
 *
 *      112, k = 1
 *
 * Removing the first 1 gives 12.
 * Removing the second 1 also gives 12.
 *
 * No benefit from aggressively removing equal digits.
 *
 * So the natural greedy condition is:
 *
 *      top > current
 *
 * -------------------------------------------------------------------------
 * WHAT IF k IS STILL LEFT AFTER THE SCAN?
 * -------------------------------------------------------------------------
 *
 * Example:
 *
 *      num = "12345", k = 2
 *
 * No digit ever causes a pop because digits are already increasing.
 *
 * To minimize the number:
 *
 *      remove from the RIGHT
 *
 *      12345 -> 123
 *
 * So after scanning:
 *
 *      while k > 0:
 *          remove last digit
 *
 * -------------------------------------------------------------------------
 * LEADING ZEROES
 * -------------------------------------------------------------------------
 *
 * Example:
 *
 *      10200, k = 1
 *
 * Greedy removes 1:
 *
 *      0200
 *
 * Final answer must be:
 *
 *      200
 *
 * So strip leading zeroes from the constructed result.
 *
 * If everything disappears or becomes zeroes:
 *
 *      return "0"
 *
 * -------------------------------------------------------------------------
 * INVARIANT
 * -------------------------------------------------------------------------
 *
 * After processing each digit:
 *
 *      the stack contains the lexicographically smallest possible prefix
 *      obtainable from the processed digits using the removals already spent.
 *
 * -------------------------------------------------------------------------
 * COMPLEXITY
 * -------------------------------------------------------------------------
 *
 * Every digit:
 *
 *      is pushed once
 *      is popped at most once
 *
 * Time  : O(n)
 * Space : O(n)
 *
 * -------------------------------------------------------------------------
 * MEMORY HOOK
 * -------------------------------------------------------------------------
 *
 * "If a smaller digit arrives, let it kick out bigger digits on its left."
 *
 * Leftmost digits dominate the value of the number.
 */
public class RemoveKDigits {

    /**
     * Optimal O(n) greedy solution.
     *
     * We use StringBuilder as a stack because:
     *
     *      append()                  -> push
     *      charAt(length - 1)       -> peek
     *      deleteCharAt(length - 1) -> pop
     */
    public static String removeKdigits(String num, int k) {

        // Edge case: remove every digit.
        if (k == num.length()) {
            return "0";
        }

        StringBuilder stack = new StringBuilder();

        for (char digit : num.toCharArray()) {

            /*
             * Remove larger digits from the left
             * while removals are still available.
             */
            while (k > 0
                    && stack.length() > 0
                    && stack.charAt(stack.length() - 1) > digit) {

                stack.deleteCharAt(stack.length() - 1);
                k--;
            }

            stack.append(digit);
        }

        /*
         * If digits were non-decreasing, some removals may remain.
         *
         * Remove from the end because later digits have the least
         * positional importance.
         */
        while (k > 0) {
            stack.deleteCharAt(stack.length() - 1);
            k--;
        }

        /*
         * Strip leading zeroes.
         */
        int firstNonZero = 0;

        while (firstNonZero < stack.length()
                && stack.charAt(firstNonZero) == '0') {

            firstNonZero++;
        }

        /*
         * If all remaining digits are zero,
         * return canonical "0".
         */
        if (firstNonZero == stack.length()) {
            return "0";
        }

        return stack.substring(firstNonZero);
    }

    // ---------------------------------------------------------------------
    // TEST HARNESS
    // ---------------------------------------------------------------------

    private static void assertEquals(
            String expected,
            String actual,
            String testName
    ) {

        if (!expected.equals(actual)) {
            throw new AssertionError(
                    testName
                            + " FAILED"
                            + "\nexpected = " + expected
                            + "\nactual   = " + actual
            );
        }

        System.out.println(
                "PASS: " + testName + " -> " + actual
        );
    }

    /**
     * LeetCode Example 1.
     *
     * 1432219, k = 3
     *
     * remove:
     * 4
     * 3
     * one of the 2s
     *
     * result = 1219
     */
    private static void testExample1() {

        System.out.println("\n=== Test 1: Example 1 ===");

        assertEquals(
                "1219",
                removeKdigits("1432219", 3),
                "1432219 remove 3"
        );
    }

    /**
     * LeetCode Example 2.
     *
     * 10200, k = 1
     *
     * remove 1:
     *
     * 0200 -> strip leading zero -> 200
     */
    private static void testExample2() {

        System.out.println("\n=== Test 2: Example 2 ===");

        assertEquals(
                "200",
                removeKdigits("10200", 1),
                "Leading zero cleanup"
        );
    }

    /**
     * LeetCode Example 3.
     *
     * Remove all digits.
     */
    private static void testExample3() {

        System.out.println("\n=== Test 3: Example 3 ===");

        assertEquals(
                "0",
                removeKdigits("10", 2),
                "Remove all digits"
        );
    }

    /**
     * Strictly increasing digits.
     *
     * No greedy pop happens during scan.
     *
     * Remaining removals must come from the end.
     */
    private static void testIncreasingDigits() {

        System.out.println("\n=== Test 4: Increasing Digits ===");

        assertEquals(
                "123",
                removeKdigits("12345", 2),
                "Remove from end"
        );
    }

    /**
     * Strictly decreasing digits.
     *
     * Each new digit kicks out the previous larger digit
     * while removals remain.
     */
    private static void testDecreasingDigits() {

        System.out.println("\n=== Test 5: Decreasing Digits ===");

        assertEquals(
                "321",
                removeKdigits("54321", 2),
                "Decreasing digits"
        );
    }

    /**
     * Duplicate digits.
     *
     * Strict '>' comparison avoids unnecessary equal-digit pops.
     */
    private static void testDuplicateDigits() {

        System.out.println("\n=== Test 6: Duplicate Digits ===");

        assertEquals(
                "11",
                removeKdigits("111", 1),
                "Equal digits"
        );
    }

    /**
     * Result becomes all zeros.
     */
    private static void testAllZerosResult() {

        System.out.println("\n=== Test 7: All-Zero Result ===");

        assertEquals(
                "0",
                removeKdigits("1000", 1),
                "Only zeroes remain"
        );
    }

    /**
     * Common tricky case.
     *
     * 112, k = 1
     *
     * No useful pop happens, so remove last digit.
     *
     * result = 11
     */
    private static void testNoUsefulGreedyPop() {

        System.out.println("\n=== Test 8: No Useful Greedy Pop ===");

        assertEquals(
                "11",
                removeKdigits("112", 1),
                "Remove last digit after scan"
        );
    }

    /**
     * Another common tricky case.
     */
    private static void testComplexMixedCase() {

        System.out.println("\n=== Test 9: Complex Mixed Case ===");

        assertEquals(
                "221",
                removeKdigits("5337" + "221", 4),
                "Mixed decreasing/increasing structure"
        );
    }

    /**
     * Repeated zero handling.
     */
    private static void testLeadingZerosAfterRemoval() {

        System.out.println("\n=== Test 10: Leading Zeros After Removal ===");

        assertEquals(
                "200",
                removeKdigits("100200", 1),
                "Leading zeroes normalize correctly"
        );
    }

    public static void main(String[] args) {

        testExample1();
        testExample2();
        testExample3();
        testIncreasingDigits();
        testDecreasingDigits();
        testDuplicateDigits();
        testAllZerosResult();
        testNoUsefulGreedyPop();
        testComplexMixedCase();
        testLeadingZerosAfterRemoval();

        System.out.println("\nALL TESTS PASSED");
    }
}
