package org.chijai.day12.randomized.shuffle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * LeetCode 384 - Shuffle an Array
 *
 * PATTERN
 *   Fisher-Yates Shuffle
 *
 * REPO
 *   org/chijai/randomized/shuffle/ShuffleAnArray.java
 */
public class ShuffleAnArray {

    /*
     * ================================================================
     * PROBLEM
     * ================================================================
     *
     * Given nums[], support:
     *
     *   reset()   -> return the original ordering
     *   shuffle() -> return a uniformly random permutation
     *
     * Every possible permutation must be equally likely.
     *
     * Example
     *   nums = [1,2,3]
     *
     *   Possible shuffles include:
     *   [1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]
     *
     * Each should have probability 1/6.
     */

    /*
     * ================================================================
     * MINIMUM INTUITION
     * ================================================================
     *
     * Fill the array from the end.
     *
     * For position i:
     *   choose uniformly from indices 0..i
     *   swap that choice into position i
     *
     * Then position i is fixed and never touched again.
     */

    // ================================================================
    // PRIMARY SOLUTION — FISHER-YATES
    // reset O(n) | shuffle O(n) | Space O(n) for returned copy
    // ================================================================
    static class Solution {

        private final int[] original;
        private final Random random = new Random();

        public Solution(int[] nums) {
            original = nums.clone();
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
    }

    /*
     * ================================================================
     * APPROACH PROGRESSION
     * ================================================================
     *
     * 1. RANDOMLY REMOVE FROM A LIST
     *
     *   Put all values in a list.
     *   Repeatedly choose one random remaining value and remove it.
     *
     *   Correct and intuitive, but ArrayList removal shifts elements.
     *   Time O(n^2), space O(n).
     *
     * 2. FISHER-YATES
     *
     *   The unfilled part of the array itself is the remaining pool.
     *   Pick one random element from that pool and swap it into place.
     *
     *   Time O(n), extra working space O(1) beyond the returned copy.
     *   This is the primary solution.
     */

    static class RemoveFromListSolution {

        private final int[] original;
        private final Random random = new Random();

        RemoveFromListSolution(int[] nums) {
            original = nums.clone();
        }

        int[] shuffle() {
            List<Integer> remaining = new ArrayList<>();

            for (int value : original) {
                remaining.add(value);
            }

            int[] result = new int[original.length];

            for (int i = 0; i < result.length; i++) {
                int index = random.nextInt(remaining.size());
                result[i] = remaining.remove(index);
            }

            return result;
        }
    }

    /*
     * CORRECTNESS
     *
     * At position i there are i+1 remaining candidates and each is equally
     * likely to be chosen. Repeating this gives every permutation probability:
     *
     *   1/n * 1/(n-1) * ... * 1/1 = 1/n!
     */

    /*
     * TRAP
     *
     * Do not repeatedly swap every position with a random index from 0..n-1.
     * That does not generate all permutations with equal probability.
     */

    /*
     * RELATED — WORKING FILE
     *
     * ../remapping/RandomFlipMatrix.java
     *   Fisher-Yates without materializing the entire virtual array.
     */

    /*
     * RECALL
     *
     * for i from n-1 down to 1:
     *   j = random 0..i
     *   swap(i,j)
     */

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public static void main(String[] args) {
        Solution solution = new Solution(new int[]{1, 2, 3});

        assert Arrays.equals(solution.reset(), new int[]{1, 2, 3});

        for (int i = 0; i < 100; i++) {
            int[] shuffled = solution.shuffle();
            Arrays.sort(shuffled);
            assert Arrays.equals(shuffled, new int[]{1, 2, 3});
        }

        System.out.println("ShuffleAnArray: all checks passed");
    }
}
