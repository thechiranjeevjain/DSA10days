package org.chijai.day12.randomized.remapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * LeetCode 710 - Random Pick with Blacklist
 *
 * PATTERN
 *   Dense Random Range -> Sparse Remapping
 *
 * REPO
 *   org/chijai/randomized/remapping/RandomPickWithBlacklist.java
 */
public class RandomPickWithBlacklist {

    /*
     * ================================================================
     * PROBLEM
     * ================================================================
     *
     * Numbers are 0..n-1. Some values are blacklisted.
     * pick() must return one allowed value uniformly at random.
     *
     * Example
     *   n = 7
     *   blacklist = [2,3,5]
     *
     *   allowed = [0,1,4,6]
     *
     * Each allowed value must have probability 1/4.
     *
     * n can be huge, so storing every allowed value is not acceptable.
     */

    /*
     * ================================================================
     * MINIMUM INTUITION
     * ================================================================
     *
     * There are exactly:
     *
     *   validCount = n - blacklist.length
     *
     * allowed answers.
     *
     * Randomly choose only from the compact range:
     *
     *   0 .. validCount-1
     *
     * Some numbers in that lower range may themselves be blacklisted.
     * Treat those as holes and remap each hole to one valid number from the
     * upper range validCount..n-1.
     *
     * Example
     *   n = 7, blacklist = [2,3,5]
     *   validCount = 4
     *   random source = [0,1,2,3]
     *
     *   2 and 3 are holes.
     *   upper valid values are 4 and 6.
     *
     *   remap could be:
     *   2 -> 4
     *   3 -> 6
     *
     * Every random source value now represents exactly one allowed answer.
     */

    // ================================================================
    // PRIMARY SOLUTION
    // Build O(b) expected | pick O(1) expected | Space O(b)
    // b = blacklist length
    // ================================================================
    static class Solution {

        private final int validCount;
        private final Map<Integer, Integer> remap = new HashMap<>();
        private final Random random = new Random();

        public Solution(int n, int[] blacklist) {
            validCount = n - blacklist.length;

            Set<Integer> blocked = new HashSet<>();
            for (int value : blacklist) {
                blocked.add(value);
            }

            int candidate = validCount;

            for (int blockedValue : blacklist) {
                if (blockedValue >= validCount) {
                    continue;
                }

                while (blocked.contains(candidate)) {
                    candidate++;
                }

                remap.put(blockedValue, candidate);
                candidate++;
            }
        }

        public int pick() {
            int value = random.nextInt(validCount);
            return remap.getOrDefault(value, value);
        }
    }

    /*
     * ================================================================
     * APPROACH PROGRESSION
     * ================================================================
     *
     * 1. MATERIALIZE ALL ALLOWED VALUES
     *
     *   Build [0..n-1] excluding blacklist, then pick from the list.
     *   Easy, but space/time O(n). n is too large.
     *
     * 2. REJECTION SAMPLING
     *
     *   Pick random 0..n-1.
     *   Retry while the result is blacklisted.
     *
     *   Simple, but when blacklist is large, retries can dominate.
     *
     * 3. DENSE RANGE + REMAPPING
     *
     *   Pick directly from exactly validCount random slots.
     *   Repair only blacklisted slots in that lower range.
     *
     *   This is the primary solution.
     */

    static class MaterializedAllowedSolution {

        private final List<Integer> allowed = new ArrayList<>();
        private final Random random = new Random();

        MaterializedAllowedSolution(int n, int[] blacklist) {
            Set<Integer> blocked = new HashSet<>();
            for (int value : blacklist) {
                blocked.add(value);
            }

            for (int value = 0; value < n; value++) {
                if (!blocked.contains(value)) {
                    allowed.add(value);
                }
            }
        }

        int pick() {
            return allowed.get(random.nextInt(allowed.size()));
        }
    }

    static class RejectionSamplingSolution {

        private final int n;
        private final Set<Integer> blocked = new HashSet<>();
        private final Random random = new Random();

        RejectionSamplingSolution(int n, int[] blacklist) {
            this.n = n;
            for (int value : blacklist) {
                blocked.add(value);
            }
        }

        int pick() {
            while (true) {
                int value = random.nextInt(n);

                if (!blocked.contains(value)) {
                    return value;
                }
            }
        }
    }

    /*
     * CORRECTNESS
     *
     * The random source has validCount equally likely values.
     * Every source value maps one-to-one to one allowed answer:
     *   - allowed lower values map to themselves
     *   - blocked lower values map to distinct allowed upper values
     * Therefore every allowed answer has probability 1/validCount.
     */

    /*
     * RELATED — WORKING FILE
     *
     * RandomFlipMatrix.java
     *   Same idea: keep a dense random range and store only sparse remappings.
     */

    /*
     * RECALL
     *
     * allowed count = n - blacklist size
     * -> random only in [0, allowedCount)
     * -> lower blacklisted values are holes
     * -> fill holes with valid upper values
     */

    public static void main(String[] args) {
        int[] blacklist = {2, 3, 5};
        Solution solution = new Solution(7, blacklist);
        Set<Integer> blocked = Set.of(2, 3, 5);

        int[] count = new int[7];

        for (int i = 0; i < 80_000; i++) {
            int value = solution.pick();
            assert !blocked.contains(value);
            count[value]++;
        }

        assert approximately(count[0] / 80_000.0, 0.25, 0.03);
        assert approximately(count[1] / 80_000.0, 0.25, 0.03);
        assert approximately(count[4] / 80_000.0, 0.25, 0.03);
        assert approximately(count[6] / 80_000.0, 0.25, 0.03);

        System.out.println("RandomPickWithBlacklist: all checks passed");
    }

    private static boolean approximately(double actual, double expected, double tolerance) {
        return Math.abs(actual - expected) <= tolerance;
    }
}
