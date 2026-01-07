package org.chijai.day1.session1;

/**
 * ===============================================================
 * 53. Maximum Subarray — Kadane Pattern (MASTER LEARNING ARTIFACT)
 * ===============================================================
 *
 * This file is intentionally LONG.
 * It is a color-coded algorithm textbook chapter.
 *
 * READ TOP → BOTTOM. ORDER IS NON-NEGOTIABLE.
 *
 * ===============================================================
 */
public class KadaneMaxSubArray {

    // ============================================================
    // 🔵 CORE PATTERN OVERVIEW
    // ============================================================
    /*
     * PRIMARY PROBLEM STATEMENT:
     * Given an integer array nums, find the contiguous subarray
     * (containing at least one number) which has the largest sum
     * and return its sum.
     *
     * Example:
     * nums = [-2,1,-3,4,-1,2,1,-5,4]
     * Output = 6
     * Subarray = [4, -1, 2, 1]
     *
     * ------------------------------------------------------------
     * PATTERN NAME:
     * Best Subarray Ending Here (Kadane / 1D DP Compression)
     *
     * WHY THIS PROBLEM EXISTS:
     * - Teaches how to convert a global optimization problem
     *   into a rolling local decision.
     * - Introduces DP state compression.
     *
     * BIG INSIGHT:
     * Any subarray ending at index i is either:
     * 1) extended from index i-1
     * 2) restarted at index i
     *
     * There is NO third option.
     */

    // ============================================================
    // 🟢 MENTAL MODEL & INVARIANTS
    // ============================================================
    /*
     * MENTAL MODEL:
     * Walk left → right.
     * At each index, ask:
     *
     * “Is my past helping me or hurting me?”
     *
     * ------------------------------------------------------------
     * INVARIANTS:
     * ------------------------------------------------------------
     * Invariant 1:
     * currentBestEndingHere
     * = maximum sum of a subarray that MUST end at this index
     *
     * Invariant 2:
     * globalBestSoFar
     * = maximum subarray sum seen anywhere so far
     *
     * INDEX TRACKING INVARIANT:
     * - startCandidate tracks tentative start
     * - finalStart / finalEnd track best answer
     *
     * ------------------------------------------------------------
     * ALLOWED ACTIONS:
     * ✔ Extend previous subarray
     * ✔ Restart at current element
     *
     * ------------------------------------------------------------
     * FORBIDDEN ACTIONS:
     * ❌ Reset blindly to zero
     * ❌ Ignore restart option
     * ❌ Either extend or keep what I have , you will never allow restart the subarray at nums[i].
     * ❌ Choosing between extend or do nothing.
     * ❌ carry negative history forever. Only NEGATIVE history hurts the future, not zero.
     * ❌ Once currSum becomes negative, it poisons future sums.
     *
     * currSum is “the best subarray that MUST end at index i”
     *
     *
     *
     *
     * ------------------------------------------------------------
     * TERMINATION:
     * One pass. Invariant #2 guarantees correctness.
     */

    // ============================================================
    // PRIMARY PROBLEM — SOLUTION CLASSES
    // ============================================================

    // ============================================================
    // 🔴 BRUTE FORCE
    // ============================================================
    static class BruteForce {

        /*
         * Approach:
         * Try all possible subarrays and compute their sums.
         *
         * Time: O(n³)
         * Space: O(1)
         *
         * ❌ Interview Preferred: NO
         */
        public int maxSubArray(int[] numbers) {
            int maximumSum = Integer.MIN_VALUE;

            for (int startIndex = 0; startIndex < numbers.length; startIndex++) {
                for (int endIndex = startIndex; endIndex < numbers.length; endIndex++) {
                    int runningSum = 0;
                    for (int scanIndex = startIndex; scanIndex <= endIndex; scanIndex++) {
                        runningSum += numbers[scanIndex];
                    }
                    maximumSum = Math.max(maximumSum, runningSum);
                }
            }
            return maximumSum;
        }
    }

    // ============================================================
    // 🟡 IMPROVED (PREFIX OPTIMIZATION)
    // ============================================================
    static class Improved {

        /*
         * Improvement:
         * Avoid re-computing subarray sums.
         *
         * Time: O(n²)
         * Space: O(1)
         *
         * ❌ Interview Preferred: NO
         */
        public int maxSubArray(int[] numbers) {
            int maximumSum = Integer.MIN_VALUE;

            for (int startIndex = 0; startIndex < numbers.length; startIndex++) {
                int runningSum = 0;
                for (int endIndex = startIndex; endIndex < numbers.length; endIndex++) {
                    runningSum += numbers[endIndex];
                    maximumSum = Math.max(maximumSum, runningSum);
                }
            }
            return maximumSum;
        }
    }

    // ============================================================
    // 🟢 OPTIMAL — KADANE (SUM ONLY)
    // ============================================================
    static class Optimal {

        /*
         * Time: O(n)
         * Space: O(1)
         *
         * ✅ Interview Preferred: YES
         */
        public int maxSubArray(int[] numbers) {

            int currentBestEndingHere = numbers[0];
            int globalBestSoFar = numbers[0];

            for (int index = 1; index < numbers.length; index++) {

                // 🟡 Extend or restart decision , carry baggage or fresh start
                currentBestEndingHere =
                        Math.max(numbers[index],
                                currentBestEndingHere + numbers[index]);

                globalBestSoFar =
                        Math.max(globalBestSoFar, currentBestEndingHere);
            }
            return globalBestSoFar;
        }
    }

    // ============================================================
    // 🟢 OPTIMAL — KADANE WITH INDEX TRACKING
    // ============================================================
    static class OptimalWithIndices {

        static class Result {
            int maxSum;
            int startIndex;
            int endIndex;

            Result(int maxSum, int startIndex, int endIndex) {
                this.maxSum = maxSum;
                this.startIndex = startIndex;
                this.endIndex = endIndex;
            }
        }

        public Result maxSubArrayWithIndices(int[] numbers) {

            int currentBestEndingHere = numbers[0];
            int globalBestSoFar = numbers[0];

            int startCandidate = 0;
            int finalStart = 0;
            int finalEnd = 0;

            for (int index = 1; index < numbers.length; index++) {

                if (numbers[index] > currentBestEndingHere + numbers[index]) {
                    currentBestEndingHere = numbers[index];
                    startCandidate = index;
                } else {
                    currentBestEndingHere += numbers[index];
                }

                if (currentBestEndingHere > globalBestSoFar) {
                    globalBestSoFar = currentBestEndingHere;
                    finalStart = startCandidate;
                    finalEnd = index;
                }
            }
            return new Result(globalBestSoFar, finalStart, finalEnd);
        }
    }

    // ============================================================
    // 🟣 INTERVIEW ARTICULATION & FOLLOW-UPS
    // ============================================================
    /*
     * 🟣 One-liner:
     * “At each index, I compute the best subarray ending here
     *  and track the global maximum.”
     *
     * 🟣 Why it works:
     * Any subarray ending here must either extend or restart.
     *
     * 🟣 What breaks if reset to zero?
     * All-negative arrays fail.
     *
     * 🟣 Streaming input:
     * Works perfectly.
     */

    // ============================================================
    // VARIATIONS & TWEAKS — COMPLETE COVERAGE
    // ============================================================
    /*
     * 🟢 Product variant:
     * Track both maxEndingHere and minEndingHere.
     *
     * 🟡 Circular array:
     * max(normalKadane, totalSum - minSubarray)
     *
     * 🔴 Pattern break:
     * Reordering allowed → Kadane invalid.
     */

    // ============================================================
    // ⚫ PATTERN REINFORCEMENT PROBLEMS (WITH STATEMENTS)
    // ============================================================

    // ------------------------------------------------------------
    // 🔵 PROBLEM: Maximum Product Subarray
    // ------------------------------------------------------------
    /*
     * Given an integer array nums,
     * find the contiguous subarray that has the largest product.
     *
     * Example:
     * nums = [2,3,-2,4]
     * Output = 6  (subarray [2,3])
     *
     * ⚫ SAME PATTERN AS PRIMARY PROBLEM BECAUSE:
     * We track best subarray ending here,
     * but multiplication requires tracking BOTH max and min.
     */

//    At each index, three possibilities exist:
//
//    Start fresh: nums[i]
//
//    Extend previous max product
//
//    Extend previous min product (negative × negative)
//
//    Because:
//
//    maxEndingHere could become bad if multiplied by negative
//
//    minEndingHere could become the new maximum
//    In sum problems, negative history is always bad.
//    In product problems, negative history can become the best answer.

    static class MaximumProductSubarray {

        public int maxProduct(int[] numbers) {
            int maxEndingHere = numbers[0];
            int minEndingHere = numbers[0];
            int globalMax = numbers[0];

            for (int index = 1; index < numbers.length; index++) {
                int current = numbers[index];

                int tempMax = Math.max(
                        current,
                        Math.max(maxEndingHere * current,
                                minEndingHere * current));

                minEndingHere = Math.min(
                        current,
                        Math.min(maxEndingHere * current,
                                minEndingHere * current));

                maxEndingHere = tempMax;
                globalMax = Math.max(globalMax, maxEndingHere);
            }
            return globalMax;
        }
    }

    // ------------------------------------------------------------
    // 🔵 PROBLEM: Best Time to Buy and Sell Stock (Single Transaction)
    // ------------------------------------------------------------
    /*
     * Given an array prices where prices[i] is the stock price on day i,
     * find the maximum profit you can achieve from one transaction.
     *
     * Example:
     * prices = [7,1,5,3,6,4]
     * Output = 5
     *
     * ⚫ SAME PATTERN:
     * Apply Kadane on day-to-day price differences.
     */
    static class BestTimeToBuySellStock {

        public int maxProfit(int[] prices) {
            int bestEndingHere = 0;
            int globalBest = 0;

            for (int index = 1; index < prices.length; index++) {
                //max sum of price differences
                int dailyDiff = prices[index] - prices[index - 1];
                //fresh start or carry forward
                bestEndingHere = Math.max(0, bestEndingHere + dailyDiff);
                globalBest = Math.max(globalBest, bestEndingHere);
            }
            return globalBest;
        }
    }

    // ------------------------------------------------------------
    // 🔵 PROBLEM: Maximum Sum Circular Subarray
    // ------------------------------------------------------------
    /*
     * Given a circular integer array nums,
     * return the maximum possible sum of a non-empty subarray.
     *
     * The subarray may wrap around the end of the array.
     *
     * Example:
     * nums = [5, -3, 5]
     * Output = 10  ([5,5])
     *
     * ⚫ SAME PATTERN:
     * max(normalKadane, totalSum - minSubarray)
     * to maximize you need to minimize the part you exclude.
     * ------------------------------------------------------------
     * ❌ WHAT GOES WRONG (ALL-NEGATIVE ARRAYS)
     * ------------------------------------------------------------
     * Example:
     * nums = [-3, -2, -5]
     *
     * totalSum   = -10
     * globalMax  = -2    (normal Kadane result)
     * globalMin  = -10   (entire array)
     *
     * totalSum - globalMin = 0
     *
     * ❌ 0 implies removing the entire array
     * ❌ Remaining subarray is EMPTY
     * ❌ Empty subarray is NOT allowed by problem statement
     * ------------------------------------------------------------
     *  When all numbers are negative:
     * - globalMin == totalSum (entire array removed)
     * - totalSum - globalMin == 0 → EMPTY subarray (invalid)
     *
     * globalMax < 0 detects this case.
     * The correct answer is the least negative element,
     * which is already stored in globalMax.
     * ------------------------------------------------------------
     */
    static class MaximumCircularSubarray {

        public int maxSubarraySumCircular(int[] numbers) {
            int totalSum = 0;
            int maxEndingHere = numbers[0];
            int minEndingHere = numbers[0];
            int globalMax = numbers[0];
            int globalMin = numbers[0];

            for (int value : numbers) {
                totalSum += value;
                maxEndingHere = Math.max(value, maxEndingHere + value);
                minEndingHere = Math.min(value, minEndingHere + value);
                globalMax = Math.max(globalMax, maxEndingHere);
                globalMin = Math.min(globalMin, minEndingHere);
            }

            if (globalMax < 0) return globalMax;
            return Math.max(globalMax, totalSum - globalMin);
        }
    }

    // ============================================================
    // 📄 PRINTABLE DSA NOTEBOOK — KADANE (PDF READY)
    // ============================================================
    /*
     * KEY QUESTION:
     * Is my past helping me?
     *
     * STATE:
     * bestEndingHere
     * globalBest
     *
     * TRANSITION:
     * bestEndingHere = max(nums[i], bestEndingHere + nums[i])
     *
     * EDGE CASE:
     * All negatives → pick max element
     */

    // ============================================================
    // 🧠 ONE-PAGE KADANE MENTAL MAP
    // ============================================================
    /*
     * Extend or restart.
     * Track ending-here.
     * Lock global best.
     * Handle negatives.
     */

    // ============================================================
    // 🟢 LEARNING VERIFICATION
    // ============================================================
    /*
     * ✔ Recall invariants from memory
     * ✔ Debug all-negative cases
     * ✔ Track indices confidently
     * ✔ Transfer to product / circular / stock
     */

    // ============================================================
    // main() — MUST BE LAST
    // ============================================================
    public static void main(String[] args) {

        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};

        Optimal solver = new Optimal();
        System.out.println("Max Sum = " + solver.maxSubArray(nums));

        OptimalWithIndices solverWithIndices = new OptimalWithIndices();
        OptimalWithIndices.Result result =
                solverWithIndices.maxSubArrayWithIndices(nums);

        System.out.println("Max Sum (with indices) = " + result.maxSum);
        System.out.print("Subarray = [ ");
        for (int i = result.startIndex; i <= result.endIndex; i++) {
            System.out.print(nums[i] + " ");
        }
        System.out.println("]");
    }
}
