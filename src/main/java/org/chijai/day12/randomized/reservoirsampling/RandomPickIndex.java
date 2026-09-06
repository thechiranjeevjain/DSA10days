package org.chijai.day12.randomized.reservoirsampling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * LeetCode 398 - Random Pick Index
 *
 * PATTERN
 *   Randomized Algorithms -> Reservoir Sampling over matching items
 */
public class RandomPickIndex {

    /*
     * FIRST-PRINCIPLES INVENTION PATH
     *
     * Ignore non-matches.
     * Among the matches seen so far, keep exactly one uniformly random index.
     * When the k-th match arrives, replace the current answer with probability 1/k.
     */

    // =====================================================================
    // OPTIMAL FOR THE LARGE-INPUT / LOW-EXTRA-MEMORY FOLLOW-UP
    // Constructor O(1), pick O(n), extra space O(1)
    // =====================================================================
    static class Solution {

        private final int[] nums;
        private final Random random;

        public Solution(int[] nums) {
            this(nums, new Random());
        }

        Solution(int[] nums, Random random) {
            this.nums = nums;
            this.random = random;
        }

        public int pick(int target) {
            int chosenIndex = -1;
            int matchesSeen = 0;

            for (int i = 0; i < nums.length; i++) {
                if (nums[i] != target) {
                    continue;
                }

                matchesSeen++;

                if (random.nextInt(matchesSeen) == 0) {
                    chosenIndex = i;
                }
            }

            return chosenIndex;
        }
    }

    // =====================================================================
    // ALTERNATIVE — Pre-index every value
    // Build O(n), pick O(1), space O(n)
    // Better when memory is fine and picks are extremely frequent.
    // =====================================================================
    static class IndexedSolution {

        private final Map<Integer, List<Integer>> indices = new HashMap<>();
        private final Random random = new Random();

        IndexedSolution(int[] nums) {
            for (int i = 0; i < nums.length; i++) {
                indices.computeIfAbsent(nums[i], ignored -> new ArrayList<>())
                        .add(i);
            }
        }

        public int pick(int target) {
            List<Integer> choices = indices.get(target);
            return choices.get(random.nextInt(choices.size()));
        }
    }

    /*
     * TRADE-OFF
     *
     * Reservoir:
     *   O(1) extra memory, O(n) each query.
     *
     * Pre-index map:
     *   O(n) memory, O(1) expected each query.
     *
     * The reservoir version is the transferable interview pattern when
     * storing all matching indices is undesirable.
     */

    /*
     * RECALL
     *
     * ONLY COUNT MATCHES
     * k-th MATCH REPLACES WITH PROBABILITY 1/k
     */

    /*
     * RELATED
     *   reservoirsampling/LinkedListRandomNode.java
     */

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 3, 3};
        Solution solution = new Solution(nums, new Random(9));

        for (int i = 0; i < 1_000; i++) {
            int index = solution.pick(3);
            assert index == 2 || index == 3 || index == 4;
        }

        System.out.println("RandomPickIndex: all checks passed");
    }
}
