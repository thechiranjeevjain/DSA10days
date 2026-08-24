package org.chijai.day5.stack.session1.monotonic;

import java.util.*;

/**
 * LeetCode 907 - Sum of Subarray Minimums
 *
 * -------------------------------------------------------------------------
 * CORE IDEA: CONTRIBUTION COUNTING
 * -------------------------------------------------------------------------
 *
 * Brute force thinks:
 *
 *      "For every subarray, find its minimum."
 *
 * That is too expensive.
 *
 * Better question:
 *
 *      "For each arr[i], in how many subarrays is arr[i] the minimum?"
 *
 * If arr[i] is the minimum in X subarrays, then its contribution is:
 *
 *      arr[i] * X
 *
 * -------------------------------------------------------------------------
 * HOW MANY SUBARRAYS CHOOSE arr[i] AS MINIMUM?
 * -------------------------------------------------------------------------
 *
 * Suppose:
 *
 *      previousLess[i] = index of previous element STRICTLY smaller
 *      nextLessOrEqual[i] = index of next element smaller OR equal
 *
 * Then:
 *
 *      leftChoices  = i - previousLess[i]
 *      rightChoices = nextLessOrEqual[i] - i
 *
 * Number of subarrays where arr[i] owns the minimum:
 *
 *      leftChoices * rightChoices
 *
 * Contribution:
 *
 *      arr[i] * leftChoices * rightChoices
 *
 * -------------------------------------------------------------------------
 * WHY THE ASYMMETRY (< on one side, <= on the other)?
 * -------------------------------------------------------------------------
 *
 * Duplicate values are the subtle part.
 *
 * Example:
 *
 *      [2, 2]
 *
 * Subarray [2,2] has minimum 2.
 *
 * If BOTH equal 2s claimed it, we would double count.
 *
 * So ties must be broken consistently.
 *
 * Here we use:
 *
 *      LEFT  -> previous STRICTLY smaller
 *      RIGHT -> next smaller OR equal
 *
 * That means equal minima are "owned" consistently by one side.
 *
 * Equivalent valid choice:
 *
 *      LEFT  -> previous smaller OR equal
 *      RIGHT -> next STRICTLY smaller
 *
 * Just do not use the same strictness on both sides.
 *
 * -------------------------------------------------------------------------
 * MONOTONIC STACK
 * -------------------------------------------------------------------------
 *
 * We find boundaries using increasing stacks of indices.
 *
 * previousLess:
 *
 *      while stack top >= arr[i]:
 *          pop
 *
 *      remaining top = previous strictly smaller
 *
 *
 * nextLessOrEqual:
 *
 *      scan right -> left
 *
 *      while stack top > arr[i]:
 *          pop
 *
 *      remaining top = next smaller or equal
 *
 * -------------------------------------------------------------------------
 * COMPLEXITY
 * -------------------------------------------------------------------------
 *
 * Every index is pushed once and popped at most once per pass.
 *
 * Time  : O(n)
 * Space : O(n)
 */
public class SumOfSubarrayMinimums {

    private static final long MOD = 1_000_000_007L;

    public static int sumSubarrayMins(int[] arr) {

        int n = arr.length;

        int[] previousLess = new int[n];
        int[] nextLessOrEqual = new int[n];

        Deque<Integer> stack = new ArrayDeque<>();

        // -------------------------------------------------------------
        // PASS 1:
        // previous STRICTLY smaller element
        // -------------------------------------------------------------
        for (int i = 0; i < n; i++) {

            /*
             * Pop >= current so the element left on top
             * is strictly smaller than arr[i].
             */
            while (!stack.isEmpty()
                    && arr[stack.peek()] >= arr[i]) {
                stack.pop();
            }

            previousLess[i] =
                    stack.isEmpty() ? -1 : stack.peek();

            stack.push(i);
        }

        stack.clear();

        // -------------------------------------------------------------
        // PASS 2:
        // next smaller OR equal element
        // -------------------------------------------------------------
        for (int i = n - 1; i >= 0; i--) {

            /*
             * Pop only elements strictly greater.
             *
             * Therefore an equal element is allowed to remain
             * and acts as the right boundary.
             */
            while (!stack.isEmpty()
                    && arr[stack.peek()] > arr[i]) {
                stack.pop();
            }

            nextLessOrEqual[i] =
                    stack.isEmpty() ? n : stack.peek();

            stack.push(i);
        }

        // -------------------------------------------------------------
        // CONTRIBUTION OF EACH ELEMENT
        // -------------------------------------------------------------
        long answer = 0;

        for (int i = 0; i < n; i++) {

            long leftChoices =
                    i - previousLess[i];

            long rightChoices =
                    nextLessOrEqual[i] - i;

            long contribution =
                    (arr[i] * leftChoices) % MOD;

            contribution =
                    (contribution * rightChoices) % MOD;

            answer =
                    (answer + contribution) % MOD;
        }

        return (int) answer;
    }

    // ---------------------------------------------------------------------
    // BRUTE FORCE VALIDATOR
    // ---------------------------------------------------------------------

    /**
     * O(n^2) validator for tests only.
     *
     * Not used by the actual solution.
     */
    private static int bruteForce(int[] arr) {

        long answer = 0;

        for (int left = 0; left < arr.length; left++) {

            int minimum = Integer.MAX_VALUE;

            for (int right = left;
                 right < arr.length;
                 right++) {

                minimum =
                        Math.min(minimum, arr[right]);

                answer =
                        (answer + minimum) % MOD;
            }
        }

        return (int) answer;
    }

    // ---------------------------------------------------------------------
    // TEST HARNESS
    // ---------------------------------------------------------------------

    private static void assertEquals(
            int expected,
            int actual,
            String testName
    ) {

        if (expected != actual) {
            throw new AssertionError(
                    testName
                            + " FAILED: expected=" + expected
                            + ", actual=" + actual
            );
        }

        System.out.println(
                "PASS: " + testName + " -> " + actual
        );
    }

    private static void assertMatchesBruteForce(
            int[] arr,
            String testName
    ) {

        int expected = bruteForce(arr);
        int actual = sumSubarrayMins(arr);

        assertEquals(expected, actual, testName);
    }

    /**
     * LeetCode Example 1.
     *
     * arr = [3,1,2,4]
     *
     * Answer = 17
     */
    private static void testExample1() {

        System.out.println("\n=== Test 1: Example 1 ===");

        int[] arr = {3, 1, 2, 4};

        assertEquals(
                17,
                sumSubarrayMins(arr),
                "Example 1"
        );
    }

    /**
     * LeetCode Example 2.
     */
    private static void testExample2() {

        System.out.println("\n=== Test 2: Example 2 ===");

        int[] arr = {11, 81, 94, 43, 3};

        assertEquals(
                444,
                sumSubarrayMins(arr),
                "Example 2"
        );
    }

    /**
     * Single element.
     */
    private static void testSingleElement() {

        System.out.println("\n=== Test 3: Single Element ===");

        int[] arr = {5};

        assertEquals(
                5,
                sumSubarrayMins(arr),
                "Single element"
        );
    }

    /**
     * Strictly increasing.
     *
     * [1,2,3]
     *
     * Subarray minimums:
     * [1]     -> 1
     * [2]     -> 2
     * [3]     -> 3
     * [1,2]   -> 1
     * [2,3]   -> 2
     * [1,2,3] -> 1
     *
     * total = 10
     */
    private static void testIncreasing() {

        System.out.println("\n=== Test 4: Increasing ===");

        int[] arr = {1, 2, 3};

        assertEquals(
                10,
                sumSubarrayMins(arr),
                "Strictly increasing"
        );
    }

    /**
     * Strictly decreasing.
     */
    private static void testDecreasing() {

        System.out.println("\n=== Test 5: Decreasing ===");

        int[] arr = {3, 2, 1};

        assertEquals(
                10,
                sumSubarrayMins(arr),
                "Strictly decreasing"
        );
    }

    /**
     * Duplicate values.
     *
     * This specifically validates tie handling.
     *
     * [2,2]
     *
     * subarrays:
     * [2]
     * [2]
     * [2,2]
     *
     * sum = 6
     */
    private static void testDuplicates() {

        System.out.println("\n=== Test 6: Duplicate Values ===");

        int[] arr = {2, 2};

        assertEquals(
                6,
                sumSubarrayMins(arr),
                "Two equal values"
        );
    }

    /**
     * All equal values.
     *
     * n = 4
     * total subarrays = 4*5/2 = 10
     *
     * Each minimum = 5
     *
     * answer = 50
     */
    private static void testAllEqual() {

        System.out.println("\n=== Test 7: All Equal ===");

        int[] arr = {5, 5, 5, 5};

        assertEquals(
                50,
                sumSubarrayMins(arr),
                "All equal values"
        );
    }

    /**
     * Valley shape:
     *
     * 3,1,3
     */
    private static void testValley() {

        System.out.println("\n=== Test 8: Valley Pattern ===");

        int[] arr = {3, 1, 3};

        assertMatchesBruteForce(
                arr,
                "Valley pattern"
        );
    }

    /**
     * Mixed pattern with duplicates.
     */
    private static void testMixedDuplicates() {

        System.out.println("\n=== Test 9: Mixed Duplicates ===");

        int[] arr = {4, 2, 2, 5, 1, 3};

        assertMatchesBruteForce(
                arr,
                "Mixed duplicates"
        );
    }

    /**
     * Deterministic randomized cross-check against brute force.
     *
     * This is useful for validating duplicate and boundary logic.
     */
    private static void testRandomizedAgainstBruteForce() {

        System.out.println("\n=== Test 10: Randomized Cross-Check ===");

        Random random = new Random(42);

        for (int test = 1; test <= 100; test++) {

            int n = 1 + random.nextInt(8);

            int[] arr = new int[n];

            for (int i = 0; i < n; i++) {
                arr[i] = 1 + random.nextInt(6);
            }

            int expected = bruteForce(arr);
            int actual = sumSubarrayMins(arr);

            if (expected != actual) {
                throw new AssertionError(
                        "Random test FAILED"
                                + "\narr      = " + Arrays.toString(arr)
                                + "\nexpected = " + expected
                                + "\nactual   = " + actual
                );
            }
        }

        System.out.println(
                "PASS: 100 randomized arrays matched brute force"
        );
    }

    public static void main(String[] args) {

        testExample1();
        testExample2();
        testSingleElement();
        testIncreasing();
        testDecreasing();
        testDuplicates();
        testAllEqual();
        testValley();
        testMixedDuplicates();
        testRandomizedAgainstBruteForce();

        System.out.println("\nALL TESTS PASSED");
    }
}
