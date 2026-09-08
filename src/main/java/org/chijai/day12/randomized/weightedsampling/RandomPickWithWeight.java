package org.chijai.day12.randomized.weightedsampling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * LeetCode 528 - Random Pick with Weight
 *
 * PATTERN
 *   Prefix Sum -> Weighted Sampling
 *
 * SECONDARY TOOL
 *   Binary Search -> Lower Bound -> first prefix >= target
 *
 * REPO
 *   org/chijai/randomized/weightedsampling/RandomPickWithWeight.java
 */
public class RandomPickWithWeight {

    /*
     * ================================================================
     * PROBLEM
     * ================================================================
     *
     * Given positive weights w[], return index i with probability:
     *
     *                  w[i]
     *     P(i) = ----------------
     *            sum(all weights)
     *
     * Larger weight -> selected more often, not always.
     *
     * Example 1:
     *   w = [1, 3]
     *   total = 4
     *   P(0) = 1/4 = 25%
     *   P(1) = 3/4 = 75%
     *
     * Example 2:
     *   w = [2, 5, 3]
     *   total = 10
     *   P(0) = 20%, P(1) = 50%, P(2) = 30%
     *
     * Exact short sequences vary; proportions emerge over many calls.
     */

    /*
     * ================================================================
     * MINIMUM INTUITION
     * ================================================================
     *
     * Think server capacity:
     *
     *   A = 2, B = 5, C = 3
     *
     * Give each server sample-space size proportional to capacity:
     *
     *   1..2   -> A
     *   3..7   -> B
     *   8..10  -> C
     *
     * B owns 5 of 10 equally likely positions -> 5/10 = 50%.
     *
     * Prefix sums store only range endings:
     *
     *   weights = [2, 5, 3]
     *   prefix  = [2, 7, 10]
     *
     * Generate target uniformly from 1..10.
     * Return the first prefix >= target.
     *
     * Example:
     *   target = 6
     *   first prefix >= 6 is 7
     *   return index 1 -> Server B
     *
     * Meaning:
     *   numerator   = space owned by this choice = weight
     *   denominator = total sample space         = sum(weights)
     */

    // ================================================================
    // PRIMARY SOLUTION
    // Build O(n) | pickIndex O(log n) | Space O(n)
    // ================================================================
    static class Solution {

        private final int[] prefix;
        private final Random random = new Random();

        Solution(int[] w) {
            prefix = new int[w.length];

            int sum = 0;

            for (int i = 0; i < w.length; i++) {
                sum += w[i];
                prefix[i] = sum;
            }
        }

        int pickIndex() {
            int total = prefix[prefix.length - 1];
            int target = random.nextInt(total) + 1;

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
     * ================================================================
     * WHY THIS PROBLEM EXISTS
     * ================================================================
     *
     * Sometimes choices should be random but not equally likely.
     *
     * Servers:
     *   capacities 10, 30, 60
     *   desired long-run traffic ~10%, 30%, 60%
     *
     * User regions:
     *   India 5000, US 3000, UK 2000
     *   proportional region sampling ~50%, 30%, 20%
     *
     * The target is generated inside the program when a choice is needed.
     * It does not create the ratio; the differently sized ranges do.
     */

    /*
     * ================================================================
     * INVENTION PATH
     * ================================================================
     *
     * 1. Weight should control how much probability a choice owns.
     * 2. Represent that probability as proportional sample-space size.
     * 3. Materializing every position works but wastes O(sum(weights)) space.
     * 4. Ranges are contiguous -> store only their ends with prefix sums.
     * 5. Pick one uniform target from 1..total.
     * 6. Its owner is the first prefix >= target.
     * 7. prefix[] is sorted -> binary search that boundary.
     */

    /*
     * ================================================================
     * APPROACH 1 - MATERIALIZE SAMPLE SPACE
     * ================================================================
     *
     * w = [1, 3, 2]
     *
     * Store:
     *   [0, 1, 1, 1, 2, 2]
     *
     * Pick one position uniformly.
     *
     * Build/Space: O(sum(weights))
     * Pick:        O(1)
     *
     * Limitation: repeated storage can be huge.
     */
    static class MaterializedSolution {

        private final List<Integer> sample = new ArrayList<>();
        private final Random random = new Random();

        MaterializedSolution(int[] w) {
            for (int i = 0; i < w.length; i++) {
                for (int count = 0; count < w[i]; count++) {
                    sample.add(i);
                }
            }
        }

        int pickIndex() {
            return sample.get(random.nextInt(sample.size()));
        }
    }

    /*
     * ================================================================
     * APPROACH 2 - PREFIX SUM + LINEAR SCAN
     * ================================================================
     *
     * [1,3,2] -> prefix [1,4,6]
     *
     * Pick target 1..6, then scan for first prefix >= target.
     *
     * Build: O(n)
     * Pick:  O(n)
     * Space: O(n)
     *
     * Limitation: prefix[] is sorted, so scanning wastes that structure.
     */
    static class PrefixLinearSolution {

        private final int[] prefix;
        private final Random random = new Random();

        PrefixLinearSolution(int[] w) {
            prefix = new int[w.length];

            int sum = 0;

            for (int i = 0; i < w.length; i++) {
                sum += w[i];
                prefix[i] = sum;
            }
        }

        int pickIndex() {
            int target = random.nextInt(prefix[prefix.length - 1]) + 1;

            for (int i = 0; i < prefix.length; i++) {
                if (prefix[i] >= target) {
                    return i;
                }
            }

            throw new IllegalStateException();
        }
    }

    /*
     * ================================================================
     * WHY >= ?
     * ================================================================
     *
     * weights = [2,5,3]
     * prefix  = [2,7,10]
     *
     * index 1 owns 3..7.
     * target 7 still belongs to index 1.
     * Therefore search first prefix >= target.
     */

    /*
     * ================================================================
     * CORRECTNESS
     * ================================================================
     *
     * Every target in 1..totalWeight is equally likely.
     * Index i owns exactly w[i] targets.
     *
     * Therefore:
     *
     *     P(i) = w[i] / totalWeight
     */

    /*
     * ================================================================
     * RELATED IDEA - CONSISTENT HASHING
     * ================================================================
     *
     * Both assign portions of a space to choices.
     *
     * Weighted random:
     *   random point -> weighted range -> owner
     *
     * Consistent hashing:
     *   hash(key) -> ownership range -> owner
     *
     * Random gives a new probabilistic choice.
     * Hashing tries to keep the same key on the same owner.
     */

    /*
     * ================================================================
     * RECALL CARD
     * ================================================================
     *
     * weighted choice
     *   -> allot sample space by weight
     *   -> prefix = range endings
     *   -> random target 1..total
     *   -> first prefix >= target
     *
     * weight[i]   = numerator / owned space
     * sum(weights)= denominator / total space
     */

    /*
     * RELATED
     *
     * Same weighted-range idea:
     *   RandomPointInNonOverlappingRectangles.java
     *
     * Different randomized patterns:
     *   ../reservoirsampling/LinkedListRandomNode.java
     *   ../reservoirsampling/RandomPickIndex.java
     *   ../shuffle/ShuffleAnArray.java
     *   ../remapping/RandomPickWithBlacklist.java
     *   ../remapping/RandomFlipMatrix.java
     */

    public static void main(String[] args) {
        int[] weights = {1, 3, 2};

        Solution primary = new Solution(weights);
        PrefixLinearSolution linear = new PrefixLinearSolution(weights);
        MaterializedSolution materialized = new MaterializedSolution(weights);

        for (int i = 0; i < 1_000; i++) {
            assertValid(primary.pickIndex(), weights.length);
            assertValid(linear.pickIndex(), weights.length);
            assertValid(materialized.pickIndex(), weights.length);
        }

        System.out.println("RandomPickWithWeightV6: all checks passed");
    }

    private static void assertValid(int index, int n) {
        assert index >= 0 && index < n;
    }
}
