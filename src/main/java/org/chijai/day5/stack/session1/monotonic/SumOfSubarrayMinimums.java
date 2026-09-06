package org.chijai.day5.stack.session1.monotonic;

import java.util.*;

/**
 * LeetCode 907 - Sum of Subarray Minimums
 *
 * Pattern:
 * Contribution Counting + Monotonic Increasing Stack
 *
 * Primary:
 * O(n) time, O(n) space.
 */
public class SumOfSubarrayMinimums {

    private static final long MOD = 1_000_000_007L;

    /**
     * ============================================================
     * PRIMARY PHOTOGRAPHIC-MEMORY SOLUTION
     * ============================================================
     *
     * LEFT  = previous STRICTLY smaller
     * RIGHT = next smaller OR equal
     *
     * leftChoices  = i - previousLess[i]
     * rightChoices = nextLessOrEqual[i] - i
     *
     * subarrayCount = leftChoices * rightChoices
     * contribution  = arr[i] * subarrayCount
     *
     * Stack:
     * previous smaller      -> pop >=
     * next smaller or equal -> pop >
     *
     * Missing boundary:
     * left -> -1
     * right -> n
     */

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

            long subarrayCount =
                    leftChoices * rightChoices;

            long contribution =
                    (long) arr[i] * subarrayCount;

            answer += contribution;
        }

        return (int) (answer % MOD);
    }


    /**
     * ============================================================
     * RECONSTRUCTION + CONSTRAINT JUDGMENT
     * ============================================================
     *
     * Surface:
     *     sum the minimum of every contiguous subarray.
     *
     * Number of subarrays:
     *
     *     n + (n - 1) + ... + 1
     *     = n(n + 1) / 2
     *     = Theta(n^2)
     *
     * Why:
     *     start 0 gives n endings,
     *     start 1 gives n - 1,
     *     ...
     *     last start gives 1.
     *
     * At n = 3 * 10^4:
     *     about 4.5 * 10^8 subarrays.
     *
     * Therefore even O(1) work per subarray is too much.
     *
     * Key inversion:
     *
     *     subarray-centric
     *         ->
     *     element-centric contribution
     *
     * Ask:
     *     "How many subarrays have arr[i] as their designated minimum?"
     *
     * For arr[i]:
     *     choose one legal start
     *     *
     *     choose one legal end.
     *
     * Therefore:
     *     count = leftChoices * rightChoices.
     *
     * What limits those choices?
     *     nearest smaller blockers.
     *
     * Therefore:
     *     nearest smaller boundaries
     *         ->
     *     monotonic stack.
     *
     * Arithmetic:
     *     max answer <= 30,000 * 450,015,000
     *                = 13,500,450,000,000
     *
     * This exceeds int but safely fits in long.
     * So compute exactly in long and apply % MOD once at the end.
     *
     * Durable reconstruction:
     *
     *     every subarray
     *         ->
     *     contribution per element
     *         ->
     *     leftChoices * rightChoices
     *         ->
     *     nearest smaller blockers
     *         ->
     *     monotonic stack
     */


    /**
     * ============================================================
     * STACK CONTRACT: MONOTONICITY, BOUNDARIES, DUPLICATES
     * ============================================================
     *
     * The array need not be monotonic.
     * The stack stores indices whose surviving values satisfy
     * the required increasing-order relation.
     *
     * Previous STRICTLY smaller:
     *     scan left -> right
     *     pop >= current
     *     top after popping = previous strictly smaller
     *     empty -> virtual boundary -1
     *
     * Next smaller OR equal:
     *     scan right -> left
     *     pop > current
     *     top after popping = next smaller or equal
     *     empty -> virtual boundary n
     *
     * Why -1 and n:
     *     i - (-1) = i + 1 possible starts
     *     n - i    = n - i possible ends
     *
     * Why popping helps:
     *     stack entries are unresolved boundary candidates;
     *     a new smaller value proves larger candidates cannot
     *     extend through it.
     *
     * Duplicate ownership:
     *     [2,2] has one subarray [2,2], not two.
     *
     * Use one strict side and one non-strict side:
     *     previous strictly smaller + next smaller/equal
     *
     * or the symmetric opposite convention.
     *
     * Complexity:
     *     each index is pushed once and popped at most once per pass
     *     -> O(n) time, O(n) space.
     */


    /**
     * ============================================================
     * INTERVIEW APPROACH LADDER
     * ============================================================
     *
     * | Approach                          | Time       | Why / Trade-off                                      |
     * |-----------------------------------|------------|------------------------------------------------------|
     * | Rescan minimum for every subarray | O(n^3)     | Literal baseline; repeated minimum work.             |
     * | Running minimum                   | O(n^2)     | Removes rescan; still visits every subarray.         |
     * | Divide + RMQ                      | O(n log n) | Fits; more machinery + recursion-depth risk.         |
     * | Two-pass contribution stack       | O(n)       | PRIMARY: explicit, easiest to prove/reconstruct.     |
     * | Single-pass pop contribution      | O(n)       | Elegant; denser boundary/duplicate reasoning.        |
     * | DP + previous-smaller stack       | O(n)       | Valid alternate; less transferable than contribution.|
     *
     * Decision rule:
     * eliminate approaches that violate constraints;
     * among survivors, prefer the simplest comfortably-correct one.
     */


    /**
     * ============================================================
     * HORIZONTAL MONOTONIC-STACK MASTERY
     * ============================================================
     *
     * | Problem                    | Boundary / Event Needed              | What the distance becomes              |
     * |----------------------------|--------------------------------------|----------------------------------------|
     * | Next Greater Element       | next greater                         | answer value/index                     |
     * | Next Smaller Element       | next smaller                         | answer value/index                     |
     * | Daily Temperatures         | next greater temperature             | waiting days                           |
     * | Stock Span                 | previous greater blocker             | left span                              |
     * | Largest Rectangle         | smaller blockers on both sides       | maximal width                          |
     * | Sum Subarray Minimums      | smaller ownership boundaries         | number of owned subarrays              |
     * | Sum Subarray Ranges        | smaller + greater ownership bounds   | min/max contribution counts            |
     *
     * Transfer question:
     *     "What unresolved boundary/event is each stack entry waiting for?"
     */


    /**
     * ============================================================
     * INTERVIEW ARTICULATION
     * ============================================================
     *
     * "There are Theta(n^2) subarrays, so n = 30,000 rules out
     * enumeration. I count each arr[i]'s contribution instead.
     *
     * Previous strictly smaller and next smaller-or-equal give
     * the legal start/end ranges. Their distance product is the
     * number of subarrays owned by arr[i].
     *
     * Monotonic increasing stacks find both boundaries in O(n).
     * Asymmetric comparisons assign duplicate minima one owner."
     */


    /**
     * ============================================================
     * RUNNABLE APPROACH PROGRESSION
     * ============================================================
     *
     * Same problem, progressively less repeated work.
     * Primary remains sumSubarrayMins(...).
     */

    /** A1: literal O(n^3) baseline; rescans each [left,right]. */
    static class CubicBruteForce {

        static int solve(int[] arr) {

            long answer = 0;

            for (int left = 0; left < arr.length; left++) {

                for (int right = left;
                     right < arr.length;
                     right++) {

                    int minimum = Integer.MAX_VALUE;

                    for (int i = left; i <= right; i++) {
                        minimum = Math.min(minimum, arr[i]);
                    }

                    answer += minimum;
                }
            }

            return (int) (answer % MOD);
        }
    }

    /** A2: O(n^2); extend right and maintain the running minimum. */
    static class QuadraticRunningMinimum {

        static int solve(int[] arr) {

            long answer = 0;

            for (int left = 0; left < arr.length; left++) {

                int minimum = Integer.MAX_VALUE;

                for (int right = left;
                     right < arr.length;
                     right++) {

                    minimum =
                            Math.min(minimum, arr[right]);

                    answer += minimum;
                }
            }

            return (int) (answer % MOD);
        }
    }

    /**
     * A3: O(n log n) divide + RMQ.
     * The interval minimum owns every subarray crossing its index.
     */
    static class DivideAndConquerRMQ {

        private final int[] arr;
        private final int[] tree;

        DivideAndConquerRMQ(int[] arr) {
            this.arr = arr;
            this.tree = new int[arr.length * 4];
            build(1, 0, arr.length - 1);
        }

        static int solve(int[] arr) {

            DivideAndConquerRMQ solver =
                    new DivideAndConquerRMQ(arr);

            long answer =
                    solver.solveRange(
                            0,
                            arr.length - 1
                    );

            return (int) (answer % MOD);
        }

        private long solveRange(
                int left,
                int right
        ) {

            if (left > right) {
                return 0;
            }

            int minIndex =
                    queryMinIndex(
                            1,
                            0,
                            arr.length - 1,
                            left,
                            right
                    );

            long leftChoices =
                    minIndex - left + 1L;

            long rightChoices =
                    right - minIndex + 1L;

            long subarrayCount =
                    leftChoices * rightChoices;

            long contribution =
                    (long) arr[minIndex] * subarrayCount;

            return contribution
                    + solveRange(left, minIndex - 1)
                    + solveRange(minIndex + 1, right);
        }

        private void build(
                int node,
                int left,
                int right
        ) {

            if (left == right) {
                tree[node] = left;
                return;
            }

            int mid =
                    left + (right - left) / 2;

            build(node * 2, left, mid);
            build(node * 2 + 1, mid + 1, right);

            tree[node] =
                    betterIndex(
                            tree[node * 2],
                            tree[node * 2 + 1]
                    );
        }

        private int queryMinIndex(
                int node,
                int segmentLeft,
                int segmentRight,
                int queryLeft,
                int queryRight
        ) {

            if (queryRight < segmentLeft
                    || segmentRight < queryLeft) {
                return -1;
            }

            if (queryLeft <= segmentLeft
                    && segmentRight <= queryRight) {
                return tree[node];
            }

            int mid =
                    segmentLeft
                            + (segmentRight - segmentLeft) / 2;

            int leftIndex =
                    queryMinIndex(
                            node * 2,
                            segmentLeft,
                            mid,
                            queryLeft,
                            queryRight
                    );

            int rightIndex =
                    queryMinIndex(
                            node * 2 + 1,
                            mid + 1,
                            segmentRight,
                            queryLeft,
                            queryRight
                    );

            return betterIndex(
                    leftIndex,
                    rightIndex
            );
        }

        private int betterIndex(
                int first,
                int second
        ) {

            if (first == -1) {
                return second;
            }

            if (second == -1) {
                return first;
            }

            if (arr[first] != arr[second]) {
                return arr[first] < arr[second]
                        ? first
                        : second;
            }

            return Math.min(first, second);
        }
    }

    /**
     * A5: O(n) single-pass.
     * On pop: current index is right boundary; new top is left boundary.
     */
    static class SinglePassContribution {

        static int solve(int[] arr) {

            Deque<Integer> stack =
                    new ArrayDeque<>();

            long answer = 0;

            for (int i = 0;
                 i <= arr.length;
                 i++) {

                while (!stack.isEmpty()
                        && (i == arr.length
                        || arr[stack.peek()] >= arr[i])) {

                    int currentIndex =
                            stack.pop();

                    int previousLess =
                            stack.isEmpty()
                                    ? -1
                                    : stack.peek();

                    int nextLessOrEqual = i;

                    long leftChoices =
                            currentIndex - previousLess;

                    long rightChoices =
                            nextLessOrEqual - currentIndex;

                    long subarrayCount =
                            leftChoices * rightChoices;

                    long contribution =
                            (long) arr[currentIndex]
                                    * subarrayCount;

                    answer += contribution;
                }

                if (i < arr.length) {
                    stack.push(i);
                }
            }

            return (int) (answer % MOD);
        }
    }

    /**
     * A6: O(n) DP + stack.
     * dp[i] = sum of minima of all subarrays ending at i.
     */
    static class DynamicProgrammingWithStack {

        static int solve(int[] arr) {

            long[] dp =
                    new long[arr.length];

            Deque<Integer> stack =
                    new ArrayDeque<>();

            long answer = 0;

            for (int i = 0; i < arr.length; i++) {

                while (!stack.isEmpty()
                        && arr[stack.peek()] >= arr[i]) {
                    stack.pop();
                }

                int previousLess =
                        stack.isEmpty()
                                ? -1
                                : stack.peek();

                if (previousLess == -1) {

                    dp[i] =
                            (long) arr[i]
                                    * (i + 1);

                } else {

                    dp[i] =
                            dp[previousLess]
                                    + (long) arr[i]
                                    * (i - previousLess);
                }

                answer += dp[i];

                stack.push(i);
            }

            return (int) (answer % MOD);
        }
    }


    /**
     * ============================================================
     * RELATED WORKING SOLUTIONS
     * ============================================================
     */

    /**
     * Largest Rectangle in Histogram:
     * same smaller-boundary geometry; distances produce width.
     */
    static class LargestRectangleInHistogram {

        static int solve(int[] heights) {

            Deque<Integer> stack =
                    new ArrayDeque<>();

            long maxArea = 0;

            for (int i = 0;
                 i <= heights.length;
                 i++) {

                int currentHeight =
                        i == heights.length
                                ? 0
                                : heights[i];

                while (!stack.isEmpty()
                        && currentHeight
                        < heights[stack.peek()]) {

                    int currentIndex =
                            stack.pop();

                    int previousSmaller =
                            stack.isEmpty()
                                    ? -1
                                    : stack.peek();

                    int nextSmaller = i;

                    long width =
                            nextSmaller
                                    - previousSmaller
                                    - 1L;

                    long area =
                            (long) heights[currentIndex]
                                    * width;

                    maxArea =
                            Math.max(maxArea, area);
                }

                stack.push(i);
            }

            return (int) maxArea;
        }
    }

    /**
     * Sum of Subarray Ranges:
     * total maximum contribution - total minimum contribution.
     */
    static class SumOfSubarrayRanges {

        static long solve(int[] nums) {

            return sumMaximumContributions(nums)
                    - sumMinimumContributions(nums);
        }

        private static long sumMinimumContributions(
                int[] nums
        ) {

            int n = nums.length;

            int[] previousLess =
                    new int[n];

            int[] nextLessOrEqual =
                    new int[n];

            Deque<Integer> stack =
                    new ArrayDeque<>();

            for (int i = 0; i < n; i++) {

                while (!stack.isEmpty()
                        && nums[stack.peek()] >= nums[i]) {
                    stack.pop();
                }

                previousLess[i] =
                        stack.isEmpty()
                                ? -1
                                : stack.peek();

                stack.push(i);
            }

            stack.clear();

            for (int i = n - 1; i >= 0; i--) {

                while (!stack.isEmpty()
                        && nums[stack.peek()] > nums[i]) {
                    stack.pop();
                }

                nextLessOrEqual[i] =
                        stack.isEmpty()
                                ? n
                                : stack.peek();

                stack.push(i);
            }

            long total = 0;

            for (int i = 0; i < n; i++) {

                long leftChoices =
                        i - previousLess[i];

                long rightChoices =
                        nextLessOrEqual[i] - i;

                long subarrayCount =
                        leftChoices * rightChoices;

                total +=
                        (long) nums[i]
                                * subarrayCount;
            }

            return total;
        }

        private static long sumMaximumContributions(
                int[] nums
        ) {

            int n = nums.length;

            int[] previousGreater =
                    new int[n];

            int[] nextGreaterOrEqual =
                    new int[n];

            Deque<Integer> stack =
                    new ArrayDeque<>();

            for (int i = 0; i < n; i++) {

                while (!stack.isEmpty()
                        && nums[stack.peek()] <= nums[i]) {
                    stack.pop();
                }

                previousGreater[i] =
                        stack.isEmpty()
                                ? -1
                                : stack.peek();

                stack.push(i);
            }

            stack.clear();

            for (int i = n - 1; i >= 0; i--) {

                while (!stack.isEmpty()
                        && nums[stack.peek()] < nums[i]) {
                    stack.pop();
                }

                nextGreaterOrEqual[i] =
                        stack.isEmpty()
                                ? n
                                : stack.peek();

                stack.push(i);
            }

            long total = 0;

            for (int i = 0; i < n; i++) {

                long leftChoices =
                        i - previousGreater[i];

                long rightChoices =
                        nextGreaterOrEqual[i] - i;

                long subarrayCount =
                        leftChoices * rightChoices;

                total +=
                        (long) nums[i]
                                * subarrayCount;
            }

            return total;
        }
    }

// ---------------------------------------------------------------------
    // BRUTE FORCE VALIDATOR
    // ---------------------------------------------------------------------

    private static int bruteForce(int[] arr) {

        long answer = 0;

        for (int left = 0; left < arr.length; left++) {

            int minimum = Integer.MAX_VALUE;

            for (int right = left;
                 right < arr.length;
                 right++) {

                minimum =
                        Math.min(minimum, arr[right]);

                answer += minimum;
            }
        }

        return (int) (answer % MOD);
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

    private static void testExample1() {

        System.out.println("\n=== Test 1: Example 1 ===");

        int[] arr = {3, 1, 2, 4};

        assertEquals(
                17,
                sumSubarrayMins(arr),
                "Example 1"
        );
    }

    private static void testExample2() {

        System.out.println("\n=== Test 2: Example 2 ===");

        int[] arr = {11, 81, 94, 43, 3};

        assertEquals(
                444,
                sumSubarrayMins(arr),
                "Example 2"
        );
    }

    private static void testSingleElement() {

        System.out.println("\n=== Test 3: Single Element ===");

        int[] arr = {5};

        assertEquals(
                5,
                sumSubarrayMins(arr),
                "Single element"
        );
    }

    private static void testIncreasing() {

        System.out.println("\n=== Test 4: Increasing ===");

        int[] arr = {1, 2, 3};

        assertEquals(
                10,
                sumSubarrayMins(arr),
                "Strictly increasing"
        );
    }

    private static void testDecreasing() {

        System.out.println("\n=== Test 5: Decreasing ===");

        int[] arr = {3, 2, 1};

        assertEquals(
                10,
                sumSubarrayMins(arr),
                "Strictly decreasing"
        );
    }

    private static void testDuplicates() {

        System.out.println("\n=== Test 6: Duplicate Values ===");

        int[] arr = {2, 2};

        assertEquals(
                6,
                sumSubarrayMins(arr),
                "Two equal values"
        );
    }

    private static void testAllEqual() {

        System.out.println("\n=== Test 7: All Equal ===");

        int[] arr = {5, 5, 5, 5};

        assertEquals(
                50,
                sumSubarrayMins(arr),
                "All equal values"
        );
    }

    private static void testValley() {

        System.out.println("\n=== Test 8: Valley Pattern ===");

        int[] arr = {3, 1, 3};

        assertMatchesBruteForce(
                arr,
                "Valley pattern"
        );
    }

    private static void testMixedDuplicates() {

        System.out.println("\n=== Test 9: Mixed Duplicates ===");

        int[] arr = {4, 2, 2, 5, 1, 3};

        assertMatchesBruteForce(
                arr,
                "Mixed duplicates"
        );
    }

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


    private static void testAllApproachesAgree() {

        System.out.println(
                "\n=== Test 11: All Approach Progressions Agree ==="
        );

        int[][] cases = {
                {3, 1, 2, 4},
                {11, 81, 94, 43, 3},
                {2, 2},
                {5, 5, 5, 5},
                {4, 2, 2, 5, 1, 3}
        };

        for (int[] arr : cases) {

            int expected =
                    QuadraticRunningMinimum.solve(arr);

            assertEquals(
                    expected,
                    CubicBruteForce.solve(arr),
                    "Cubic -> "
                            + Arrays.toString(arr)
            );

            assertEquals(
                    expected,
                    DivideAndConquerRMQ.solve(arr),
                    "Divide/RMQ -> "
                            + Arrays.toString(arr)
            );

            assertEquals(
                    expected,
                    sumSubarrayMins(arr),
                    "Two-pass primary -> "
                            + Arrays.toString(arr)
            );

            assertEquals(
                    expected,
                    SinglePassContribution.solve(arr),
                    "Single-pass -> "
                            + Arrays.toString(arr)
            );

            assertEquals(
                    expected,
                    DynamicProgrammingWithStack.solve(arr),
                    "DP + stack -> "
                            + Arrays.toString(arr)
            );
        }
    }

    private static void testRelatedProblems() {

        System.out.println(
                "\n=== Test 12: Related Monotonic-Stack Problems ==="
        );

        assertEquals(
                10,
                LargestRectangleInHistogram.solve(
                        new int[]{2, 1, 5, 6, 2, 3}
                ),
                "Largest Rectangle"
        );

        long ranges =
                SumOfSubarrayRanges.solve(
                        new int[]{1, 2, 3}
                );

        if (ranges != 4L) {
            throw new AssertionError(
                    "Sum of Subarray Ranges FAILED: expected=4, actual="
                            + ranges
            );
        }

        System.out.println(
                "PASS: Sum of Subarray Ranges -> " + ranges
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
        testAllApproachesAgree();
        testRelatedProblems();

        System.out.println("\nALL TESTS PASSED");
    }
}
