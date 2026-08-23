package org.chijai.trading;

import java.util.*;

/**
 * LeetCode 347 - Top K Frequent Elements
 *
 * Problem:
 * Given an integer array nums and integer k,
 * return the k most frequent elements.
 *
 * Follow-up:
 * Must be better than O(n log n).
 *
 * -------------------------------------------------------------------------
 * BEST INTERVIEW APPROACH: HASHMAP + BUCKET SORT
 * -------------------------------------------------------------------------
 *
 * Step 1:
 * Count frequency of every number.
 *
 *      number -> frequency
 *
 * using HashMap.
 *
 * Step 2:
 * Create buckets where:
 *
 *      bucket[f] = all numbers occurring exactly f times
 *
 * Maximum possible frequency is nums.length,
 * so we only need n + 1 buckets.
 *
 * Step 3:
 * Traverse buckets from high frequency to low frequency
 * until we collect k elements.
 *
 * -------------------------------------------------------------------------
 * WHY THIS IS O(n)
 * -------------------------------------------------------------------------
 *
 * Counting frequencies:
 *      O(n)
 *
 * Putting unique numbers into buckets:
 *      O(u)
 *
 * Scanning buckets from n down to 1:
 *      O(n)
 *
 * where u <= n.
 *
 * Therefore:
 *
 *      Total = O(n)
 *
 * This beats sorting unique elements by frequency:
 *
 *      O(u log u)
 *
 * -------------------------------------------------------------------------
 * CORE INVARIANT
 * -------------------------------------------------------------------------
 *
 * bucket[f] contains exactly the elements whose frequency is f.
 *
 * Therefore, scanning frequencies from high to low guarantees
 * that we collect the most frequent elements first.
 *
 * -------------------------------------------------------------------------
 * COMPLEXITY
 * -------------------------------------------------------------------------
 *
 * Time  : O(n)
 * Space : O(n)
 *
 * -------------------------------------------------------------------------
 * ALTERNATIVE
 * -------------------------------------------------------------------------
 *
 * Min-Heap of size k:
 *
 *      O(n log k)
 *
 * Also good, especially when k << number of unique values.
 *
 * But because this problem explicitly asks for better than O(n log n),
 * bucket sort is the cleanest optimal answer.
 */
public class TopKFrequentElements {

    /**
     * Optimal O(n) solution using bucket sort.
     */
    public static int[] topKFrequent(int[] nums, int k) {

        // -------------------------------------------------------------
        // Step 1: Count frequencies.
        // -------------------------------------------------------------
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (int num : nums) {
            frequencyMap.merge(num, 1, Integer::sum);
        }

        // -------------------------------------------------------------
        // Step 2: Bucket by frequency.
        //
        // bucket[f] = list of numbers that appear exactly f times.
        //
        // Maximum frequency is nums.length.
        // -------------------------------------------------------------
        @SuppressWarnings("unchecked")
        List<Integer>[] buckets = new List[nums.length + 1];

        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {

            int num = entry.getKey();
            int frequency = entry.getValue();

            if (buckets[frequency] == null) {
                buckets[frequency] = new ArrayList<>();
            }

            buckets[frequency].add(num);
        }

        // -------------------------------------------------------------
        // Step 3: Scan from highest frequency downward.
        // -------------------------------------------------------------
        int[] result = new int[k];
        int index = 0;

        for (int frequency = buckets.length - 1;
             frequency >= 1 && index < k;
             frequency--) {

            if (buckets[frequency] == null) {
                continue;
            }

            for (int num : buckets[frequency]) {

                result[index++] = num;

                if (index == k) {
                    break;
                }
            }
        }

        return result;
    }

    // ---------------------------------------------------------------------
    // TEST HARNESS
    // ---------------------------------------------------------------------

    /**
     * Since the problem allows any output order,
     * tests compare sets instead of array order.
     */
    private static void assertArraySameElements(
            int[] expected,
            int[] actual,
            String testName
    ) {

        if (expected.length != actual.length) {
            throw new AssertionError(
                    testName
                            + " FAILED: different lengths"
                            + ", expected=" + Arrays.toString(expected)
                            + ", actual=" + Arrays.toString(actual)
            );
        }

        int[] expectedCopy = expected.clone();
        int[] actualCopy = actual.clone();

        Arrays.sort(expectedCopy);
        Arrays.sort(actualCopy);

        if (!Arrays.equals(expectedCopy, actualCopy)) {
            throw new AssertionError(
                    testName
                            + " FAILED"
                            + ", expected=" + Arrays.toString(expected)
                            + ", actual=" + Arrays.toString(actual)
            );
        }

        System.out.println(
                "PASS: " + testName + " -> " + Arrays.toString(actual)
        );
    }

    /**
     * Example 1:
     *
     * 1 -> 3 times
     * 2 -> 2 times
     * 3 -> 1 time
     *
     * top 2 = {1, 2}
     */
    private static void testExample1() {

        System.out.println("\n=== Test 1: Example 1 ===");

        int[] nums = {1, 1, 1, 2, 2, 3};

        assertArraySameElements(
                new int[]{1, 2},
                topKFrequent(nums, 2),
                "Example 1"
        );
    }

    /**
     * Example 2:
     */
    private static void testExample2() {

        System.out.println("\n=== Test 2: Example 2 ===");

        int[] nums = {1};

        assertArraySameElements(
                new int[]{1},
                topKFrequent(nums, 1),
                "Single element"
        );
    }

    /**
     * Example 3:
     *
     * 1 -> 4
     * 2 -> 4
     * 3 -> 2
     *
     * top 2 = {1, 2}
     */
    private static void testExample3() {

        System.out.println("\n=== Test 3: Example 3 ===");

        int[] nums = {
                1, 2, 1, 2, 1,
                2, 3, 1, 3, 2
        };

        assertArraySameElements(
                new int[]{1, 2},
                topKFrequent(nums, 2),
                "Example 3"
        );
    }

    /**
     * Negative numbers are valid.
     */
    private static void testNegativeNumbers() {

        System.out.println("\n=== Test 4: Negative Numbers ===");

        int[] nums = {
                -1, -1, -1,
                -2, -2,
                3
        };

        assertArraySameElements(
                new int[]{-1, -2},
                topKFrequent(nums, 2),
                "Negative numbers"
        );
    }

    /**
     * k can equal the number of unique elements.
     */
    private static void testKEqualsUniqueCount() {

        System.out.println("\n=== Test 5: K Equals Unique Count ===");

        int[] nums = {
                1, 1,
                2, 2, 2,
                3
        };

        assertArraySameElements(
                new int[]{1, 2, 3},
                topKFrequent(nums, 3),
                "Return all unique values"
        );
    }

    /**
     * Large frequency gap.
     */
    private static void testLargeFrequencyGap() {

        System.out.println("\n=== Test 6: Large Frequency Gap ===");

        int[] nums = {
                9, 9, 9, 9, 9, 9,
                8, 8,
                7,
                6
        };

        assertArraySameElements(
                new int[]{9, 8},
                topKFrequent(nums, 2),
                "Large frequency gap"
        );
    }

    /**
     * Demonstrates bucket grouping.
     *
     * Frequencies:
     *
     * 10 -> 5
     * 20 -> 4
     * 30 -> 3
     * 40 -> 2
     * 50 -> 1
     */
    private static void testDescendingFrequencyStructure() {

        System.out.println("\n=== Test 7: Descending Frequency Structure ===");

        int[] nums = {
                10, 10, 10, 10, 10,
                20, 20, 20, 20,
                30, 30, 30,
                40, 40,
                50
        };

        assertArraySameElements(
                new int[]{10, 20, 30},
                topKFrequent(nums, 3),
                "Top 3 descending frequencies"
        );
    }

    public static void main(String[] args) {

        testExample1();
        testExample2();
        testExample3();
        testNegativeNumbers();
        testKEqualsUniqueCount();
        testLargeFrequencyGap();
        testDescendingFrequencyStructure();

        System.out.println("\nALL TESTS PASSED");
    }
}
