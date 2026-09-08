package org.chijai.day12.randomized.reservoirsampling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * LeetCode 398 - Random Pick Index
 *
 * PRIMARY PATTERN
 *   Pre-index values -> Random choice from matching indices
 *
 * FOLLOW-UP PATTERN
 *   Reservoir Sampling -> O(1) extra space
 *
 * REPO
 *   org/chijai/randomized/reservoirsampling/RandomPickIndex.java
 */
public class RandomPickIndex {

    /*
     * ================================================================
     * PROBLEM
     * ================================================================
     *
     * Store nums[]. pick(target) must return one index whose value equals
     * target. If target occurs multiple times, every matching index must be
     * equally likely.
     *
     * Example
     *   nums = [1, 2, 3, 3, 3]
     *
     *   pick(3) may return 2, 3, or 4.
     *   Each must have probability 1/3.
     *
     *   pick(1) always returns 0.
     */

    /*
     * ================================================================
     * MINIMUM INTUITION
     * ================================================================
     *
     * The array is fixed and pick() may be called repeatedly.
     * Precompute:
     *
     *   value -> all indices where it occurs
     *
     * Then pick one position uniformly from that small list.
     */

    // ================================================================
    // PRIMARY SOLUTION — EASIEST FOR THE BASE PROBLEM
    // Build O(n) | pick O(1) average | Space O(n)
    // ================================================================
    static class Solution {

        private final Map<Integer, List<Integer>> indices = new HashMap<>();
        private final Random random = new Random();

        public Solution(int[] nums) {
            for (int i = 0; i < nums.length; i++) {
                indices
                        .computeIfAbsent(nums[i], ignored -> new ArrayList<>())
                        .add(i);
            }
        }

        public int pick(int target) {
            List<Integer> choices = indices.get(target);
            return choices.get(random.nextInt(choices.size()));
        }
    }

    /*
     * ================================================================
     * APPROACH PROGRESSION
     * ================================================================
     *
     * 1. COLLECT MATCHES ON EVERY pick(target)
     *
     *   Scan nums, collect matching indices, choose one uniformly.
     *   Query O(n), temporary space O(number of matches).
     *   Repeats the same work across calls.
     *
     * 2. PRE-INDEX ONCE
     *
     *   Map value -> matching indices.
     *   Build O(n), query O(1) average, space O(n).
     *   This is the primary solution.
     *
     * 3. RESERVOIR SAMPLING — O(1)-SPACE FOLLOW-UP
     *
     *   Scan nums.
     *   Only matching indices participate.
     *   For the k-th match, replace the answer with probability 1/k.
     *
     *   Query O(n), extra space O(1).
     */

    static class ScanAndCollectSolution {

        private final int[] nums;
        private final Random random = new Random();

        ScanAndCollectSolution(int[] nums) {
            this.nums = nums;
        }

        int pick(int target) {
            List<Integer> matches = new ArrayList<>();

            for (int i = 0; i < nums.length; i++) {
                if (nums[i] == target) {
                    matches.add(i);
                }
            }

            return matches.get(random.nextInt(matches.size()));
        }
    }

    static class ReservoirSolution {

        private final int[] nums;
        private final Random random = new Random();

        ReservoirSolution(int[] nums) {
            this.nums = nums;
        }

        int pick(int target) {
            int answer = -1;
            int matches = 0;

            for (int i = 0; i < nums.length; i++) {
                if (nums[i] != target) {
                    continue;
                }

                matches++;

                if (random.nextInt(matches) == 0) {
                    answer = i;
                }
            }

            return answer;
        }
    }

    /*
     * WHY RESERVOIR WORKS
     *
     * Treat matching indices as a stream of length m.
     * The k-th match replaces the current answer with probability 1/k.
     * Therefore each of the m matches finishes with probability 1/m.
     */

    /*
     * RELATED — WORKING FILE
     *
     * LinkedListRandomNode.java
     *   Same reservoir follow-up when the population cannot be indexed cheaply.
     */

    /*
     * RECALL
     *
     * Fixed array + repeated queries -> pre-index.
     * O(1)-space follow-up -> reservoir over matches only.
     */

    public static void main(String[] args) {
        Solution solution = new Solution(new int[]{1, 2, 3, 3, 3});

        assert solution.pick(1) == 0;

        int[] count = new int[5];

        for (int i = 0; i < 60_000; i++) {
            count[solution.pick(3)]++;
        }

        assert approximately(count[2] / 60_000.0, 1.0 / 3, 0.03);
        assert approximately(count[3] / 60_000.0, 1.0 / 3, 0.03);
        assert approximately(count[4] / 60_000.0, 1.0 / 3, 0.03);

        System.out.println("RandomPickIndex: all checks passed");
    }

    private static boolean approximately(double actual, double expected, double tolerance) {
        return Math.abs(actual - expected) <= tolerance;
    }
}
