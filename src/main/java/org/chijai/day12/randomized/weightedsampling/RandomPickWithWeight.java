package org.chijai.day12.randomized.weightedsampling;

import java.util.Random;

/**
 * LeetCode 528 - Random Pick with Weight
 *
 * PATTERN
 *   Randomized Algorithms -> Weighted Sampling
 *
 * CORE TRANSFORMATION
 *   weight -> cumulative interval -> uniform ticket -> lower bound
 *
 * SECONDARY TOOL
 *   Prefix Sum + Binary Search (first prefix >= target)
 */
public class RandomPickWithWeight {

    /*
     * FIRST-PRINCIPLES INVENTION PATH
     *
     * Need P(i) = w[i] / total.
     * Give index i exactly w[i] equal-probability integer tickets.
     * Prefix sums compress those tickets into contiguous intervals.
     * Pick one uniform ticket and locate the first interval endpoint >= it.
     */

    // =====================================================================
    // OPTIMAL SOLUTION — Interview Preferred
    // Constructor O(n), pickIndex O(log n), space O(n)
    // =====================================================================
    static class Solution {

        private final long[] prefix;
        private final Random random;

        public Solution(int[] w) {
            this(w, new Random());
        }

        Solution(int[] w, Random random) {
            this.random = random;
            this.prefix = new long[w.length];

            long runningSum = 0;

            for (int i = 0; i < w.length; i++) {
                runningSum += w[i];
                prefix[i] = runningSum;
            }
        }

        public int pickIndex() {
            long totalWeight = prefix[prefix.length - 1];
            long target = random.nextLong(totalWeight) + 1;

            return firstPrefixAtLeast(target);
        }

        private int firstPrefixAtLeast(long target) {
            int left = 0;
            int right = prefix.length - 1;

            while (left < right) {
                int mid = left + (right - left) / 2;

                if (prefix[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            return left;
        }
    }

    /*
     * WHY IT WORKS
     *
     * Example: w = [1, 3, 2]
     * prefix    = [1, 4, 6]
     *
     * index 0 owns ticket  [1]       -> 1 ticket
     * index 1 owns tickets [2..4]    -> 3 tickets
     * index 2 owns tickets [5..6]    -> 2 tickets
     *
     * Every ticket is equally likely, so interval width / total
     * is exactly the required probability.
     */

    // =====================================================================
    // APPROACHES
    // =====================================================================

    /*
     * 1. MATERIALIZE EVERY TICKET
     *
     * Repeat index i exactly w[i] times in a list, then pick uniformly.
     *
     * Build: O(sum(w)) time and space
     * Pick : O(1)
     *
     * Limitation: total weight can be far larger than n.
     */

    /*
     * 2. PREFIX SUM + LINEAR SCAN
     *
     * Build cumulative intervals, pick one ticket, scan for the first
     * prefix >= target.
     *
     * Build: O(n)
     * Pick : O(n)
     * Space: O(n)
     *
     * Improvement: compresses tickets, but lookup is still linear.
     */

    /*
     * 3. PREFIX SUM + BINARY SEARCH
     *
     * Positive weights make prefix sums strictly increasing, so locate
     * the first prefix >= target in O(log n).
     */

    /*
     * BOUNDARY TRAP
     *
     * If target is sampled from 1..total inclusive:
     *
     *     answer = first prefix >= target
     *
     * If instead target were sampled from 0..total-1:
     *
     *     answer = first prefix > target
     *
     * Pick one convention and keep the random range + comparison aligned.
     */

    /*
     * RECALL
     *
     * BUILD PREFIX
     * PICK UNIFORM TICKET
     * FIRST PREFIX >= TICKET
     */

    /*
     * RELATED — working solutions live in their own canonical files
     *
     * Same weighted-bucket mechanism:
     *   weightedsampling/RandomPointInNonOverlappingRectangles.java
     *
     * Different random mechanisms:
     *   reservoirsampling/LinkedListRandomNode.java
     *   reservoirsampling/RandomPickIndex.java
     *   shuffle/ShuffleAnArray.java
     *   remapping/RandomPickWithBlacklist.java
     *   remapping/RandomFlipMatrix.java
     */

    public static void main(String[] args) {
        Solution solution = new Solution(new int[]{1, 3, 2}, new Random(7));

        for (int i = 0; i < 1_000; i++) {
            int picked = solution.pickIndex();
            assert picked >= 0 && picked < 3;
        }

        System.out.println("RandomPickWithWeight: all checks passed");
    }
}
