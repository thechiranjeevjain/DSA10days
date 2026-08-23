package org.chijai.trading;

import java.util.*;

/**
 * LeetCode 163 - Missing Ranges
 *
 * Current LeetCode contract:
 *
 * Given:
 * - inclusive range [lower, upper]
 * - sorted UNIQUE array nums
 * - every nums[i] lies inside [lower, upper]
 *
 * Return the smallest sorted list of missing ranges.
 *
 * Example:
 *
 * nums  = [0, 1, 3, 50, 75]
 * lower = 0
 * upper = 99
 *
 * result:
 *
 * [[2,2], [4,49], [51,74], [76,99]]
 *
 * -------------------------------------------------------------------------
 * CORE PATTERN: "NEXT EXPECTED" / GAP SCAN
 * -------------------------------------------------------------------------
 *
 * Maintain:
 *
 *      nextMissingCandidate
 *
 * meaning:
 *
 *      "the first number that could still be missing"
 *
 * Initially:
 *
 *      nextMissingCandidate = lower
 *
 * For every num:
 *
 * 1. If num > nextMissingCandidate:
 *
 *      everything from:
 *
 *          nextMissingCandidate ... num - 1
 *
 *      is missing.
 *
 * 2. Then advance:
 *
 *      nextMissingCandidate = num + 1
 *
 * After scanning all nums:
 *
 * if nextMissingCandidate <= upper:
 *
 *      [nextMissingCandidate, upper]
 *
 * is the final missing range.
 *
 * -------------------------------------------------------------------------
 * INVARIANT
 * -------------------------------------------------------------------------
 *
 * Before processing the next array element:
 *
 *      every value below nextMissingCandidate
 *      has already been completely accounted for.
 *
 * So we only need to ask:
 *
 *      "Is there a gap between nextMissingCandidate and num?"
 *
 * -------------------------------------------------------------------------
 * WHY USE long FOR THE RUNNING BOUNDARY?
 * -------------------------------------------------------------------------
 *
 * Current LeetCode constraints are only +/- 1e9, so int arithmetic is safe.
 *
 * Still, using long for:
 *
 *      num + 1
 *
 * makes the code robust to the classic Integer.MAX_VALUE edge case
 * and avoids accidental overflow in closely related interview variants.
 *
 * -------------------------------------------------------------------------
 * COMPLEXITY
 * -------------------------------------------------------------------------
 *
 * Time:
 *      O(n)
 *
 * Extra space:
 *      O(1)
 *
 * Ignoring the returned answer.
 */
public class MissingRanges {

    /**
     * O(n) one-pass solution.
     */
    public static List<List<Integer>> findMissingRanges(
            int[] nums,
            int lower,
            int upper
    ) {

        List<List<Integer>> result = new ArrayList<>();

        /*
         * First number in [lower, upper]
         * that has not yet been accounted for.
         */
        long nextMissingCandidate = lower;

        for (int num : nums) {

            /*
             * If num is ahead of what we expected,
             * there is a missing interval:
             *
             * nextMissingCandidate ... num - 1
             */
            if (num > nextMissingCandidate) {

                result.add(
                        Arrays.asList(
                                (int) nextMissingCandidate,
                                num - 1
                        )
                );
            }

            /*
             * num itself exists, so the next candidate
             * must begin after num.
             */
            nextMissingCandidate = (long) num + 1;
        }

        /*
         * Anything left after the last array element
         * up to upper is also missing.
         */
        if (nextMissingCandidate <= upper) {

            result.add(
                    Arrays.asList(
                            (int) nextMissingCandidate,
                            upper
                    )
            );
        }

        return result;
    }

    // ---------------------------------------------------------------------
    // TEST HARNESS
    // ---------------------------------------------------------------------

    private static void assertRangesEqual(
            List<List<Integer>> expected,
            List<List<Integer>> actual,
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
     * Canonical example:
     *
     * Present:
     * 0, 1, 3, 50, 75
     *
     * Missing:
     * 2
     * 4..49
     * 51..74
     * 76..99
     */
    private static void testCanonicalExample() {

        System.out.println("\n=== Test 1: Canonical Example ===");

        int[] nums = {0, 1, 3, 50, 75};

        List<List<Integer>> expected = Arrays.asList(
                Arrays.asList(2, 2),
                Arrays.asList(4, 49),
                Arrays.asList(51, 74),
                Arrays.asList(76, 99)
        );

        assertRangesEqual(
                expected,
                findMissingRanges(nums, 0, 99),
                "Canonical example"
        );
    }

    /**
     * No numbers are missing.
     */
    private static void testNoMissingRange() {

        System.out.println("\n=== Test 2: No Missing Range ===");

        int[] nums = {-1};

        assertRangesEqual(
                Collections.emptyList(),
                findMissingRanges(nums, -1, -1),
                "Single covered value"
        );
    }

    /**
     * Empty nums means the whole interval is missing.
     */
    private static void testEmptyArray() {

        System.out.println("\n=== Test 3: Empty Array ===");

        int[] nums = {};

        List<List<Integer>> expected = Collections.singletonList(
                Arrays.asList(1, 5)
        );

        assertRangesEqual(
                expected,
                findMissingRanges(nums, 1, 5),
                "Entire range missing"
        );
    }

    /**
     * Missing only before the first element.
     */
    private static void testMissingAtBeginning() {

        System.out.println("\n=== Test 4: Missing At Beginning ===");

        int[] nums = {3, 4, 5};

        List<List<Integer>> expected = Collections.singletonList(
                Arrays.asList(1, 2)
        );

        assertRangesEqual(
                expected,
                findMissingRanges(nums, 1, 5),
                "Missing prefix"
        );
    }

    /**
     * Missing only after the last element.
     */
    private static void testMissingAtEnd() {

        System.out.println("\n=== Test 5: Missing At End ===");

        int[] nums = {1, 2, 3};

        List<List<Integer>> expected = Collections.singletonList(
                Arrays.asList(4, 5)
        );

        assertRangesEqual(
                expected,
                findMissingRanges(nums, 1, 5),
                "Missing suffix"
        );
    }

    /**
     * Multiple isolated single-number gaps.
     */
    private static void testSingleValueGaps() {

        System.out.println("\n=== Test 6: Single-Value Gaps ===");

        int[] nums = {1, 3, 5, 7};

        List<List<Integer>> expected = Arrays.asList(
                Arrays.asList(2, 2),
                Arrays.asList(4, 4),
                Arrays.asList(6, 6)
        );

        assertRangesEqual(
                expected,
                findMissingRanges(nums, 1, 7),
                "Alternating single-value gaps"
        );
    }

    /**
     * Negative values and zero.
     */
    private static void testNegativeRange() {

        System.out.println("\n=== Test 7: Negative Range ===");

        int[] nums = {-5, -3, 0, 2};

        List<List<Integer>> expected = Arrays.asList(
                Arrays.asList(-4, -4),
                Arrays.asList(-2, -1),
                Arrays.asList(1, 1),
                Arrays.asList(3, 3)
        );

        assertRangesEqual(
                expected,
                findMissingRanges(nums, -5, 3),
                "Negative and positive boundaries"
        );
    }

    /**
     * Only lower exists.
     */
    private static void testOnlyLowerPresent() {

        System.out.println("\n=== Test 8: Only Lower Present ===");

        int[] nums = {10};

        List<List<Integer>> expected = Collections.singletonList(
                Arrays.asList(11, 15)
        );

        assertRangesEqual(
                expected,
                findMissingRanges(nums, 10, 15),
                "Only lower boundary present"
        );
    }

    /**
     * Only upper exists.
     */
    private static void testOnlyUpperPresent() {

        System.out.println("\n=== Test 9: Only Upper Present ===");

        int[] nums = {15};

        List<List<Integer>> expected = Collections.singletonList(
                Arrays.asList(10, 14)
        );

        assertRangesEqual(
                expected,
                findMissingRanges(nums, 10, 15),
                "Only upper boundary present"
        );
    }

    /**
     * Large interval gap.
     */
    private static void testLargeGap() {

        System.out.println("\n=== Test 10: Large Gap ===");

        int[] nums = {1, 1_000_000_000};

        List<List<Integer>> expected = Collections.singletonList(
                Arrays.asList(2, 999_999_999)
        );

        assertRangesEqual(
                expected,
                findMissingRanges(nums, 1, 1_000_000_000),
                "Large missing interval"
        );
    }

    public static void main(String[] args) {

        testCanonicalExample();
        testNoMissingRange();
        testEmptyArray();
        testMissingAtBeginning();
        testMissingAtEnd();
        testSingleValueGaps();
        testNegativeRange();
        testOnlyLowerPresent();
        testOnlyUpperPresent();
        testLargeGap();

        System.out.println("\nALL TESTS PASSED");
    }
}
