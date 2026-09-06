package org.chijai.day12.randomized.shuffle;

import java.util.Arrays;
import java.util.Random;

/**
 * LeetCode 384 - Shuffle an Array
 *
 * PATTERN
 *   Randomized Algorithms -> Fisher-Yates Shuffle
 */
public class ShuffleAnArray {

    /*
     * FIRST-PRINCIPLES INVENTION PATH
     *
     * A uniform shuffle means every permutation must have probability 1/n!.
     * Fix the last position by choosing uniformly among all n remaining items.
     * Then fix the second-last from n-1 remaining items, and continue.
     *
     * Number of equally likely choice paths:
     *   n * (n-1) * ... * 1 = n!
     *
     * Exactly one path creates each permutation.
     */

    // =====================================================================
    // OPTIMAL SOLUTION — Fisher-Yates
    // shuffle O(n), reset O(n), space O(n) for owned array copies
    // =====================================================================
    static class Solution {

        private final int[] original;
        private final Random random;

        public Solution(int[] nums) {
            this(nums, new Random());
        }

        Solution(int[] nums, Random random) {
            this.original = nums.clone();
            this.random = random;
        }

        public int[] reset() {
            return original.clone();
        }

        public int[] shuffle() {
            int[] shuffled = original.clone();

            for (int i = shuffled.length - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                swap(shuffled, i, j);
            }

            return shuffled;
        }

        private static void swap(int[] nums, int i, int j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
    }

    // =====================================================================
    // APPROACHES
    // =====================================================================

    /*
     * 1. REPEATEDLY REMOVE A RANDOM ELEMENT FROM A LIST
     *    Correct if implemented carefully, but ArrayList removals make it O(n^2).
     *
     * 2. SWAP EACH POSITION WITH A RANDOM INDEX FROM THE WHOLE ARRAY
     *    Tempting but biased. It creates n^n random paths, and n! generally does
     *    not divide n^n evenly, so permutations cannot all receive equal mass.
     *
     * 3. FISHER-YATES
     *    At position i, choose uniformly only from [0..i]. O(n), unbiased.
     */

    /*
     * RECALL
     *
     * FOR i = n-1 DOWN TO 1
     *     j = random [0..i]
     *     swap(i, j)
     */

    /*
     * RELATED
     *   remapping/RandomFlipMatrix.java
     *
     * RandomFlipMatrix is Fisher-Yates over a huge virtual array,
     * represented lazily with a HashMap instead of materializing the array.
     */

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4};
        Solution solution = new Solution(nums, new Random(3));

        int[] shuffled = solution.shuffle();
        int[] sorted = shuffled.clone();
        Arrays.sort(sorted);

        assert Arrays.equals(sorted, nums);
        assert Arrays.equals(solution.reset(), nums);

        System.out.println("ShuffleAnArray: all checks passed");
    }
}
