package org.chijai.day12.randomized.remapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * LeetCode 710 - Random Pick with Blacklist
 *
 * PATTERN
 *   Randomized Algorithms -> Range Remapping
 *
 * CORE IDEA
 *   Compress all valid outcomes into [0, validCount), then remap any
 *   blacklisted number inside that range to a valid number outside it.
 */
public class RandomPickWithBlacklist {

    /*
     * FIRST-PRINCIPLES INVENTION PATH
     *
     * There are exactly n - blacklist.length valid values.
     * If we could represent them as a dense range [0, validCount),
     * one uniform random integer would solve the problem.
     *
     * Some numbers in that dense range are blacklisted.
     * Pair each such hole with a valid number from [validCount, n).
     * Now every random slot in the dense range maps to exactly one valid value.
     */

    // =====================================================================
    // OPTIMAL SOLUTION — Interview Preferred
    // Build O(B) expected, pick O(1) expected, space O(B)
    // =====================================================================
    static class Solution {

        private final int validCount;
        private final Map<Integer, Integer> remap = new HashMap<>();
        private final Random random;

        public Solution(int n, int[] blacklist) {
            this(n, blacklist, new Random());
        }

        Solution(int n, int[] blacklist, Random random) {
            this.random = random;
            this.validCount = n - blacklist.length;

            Set<Integer> blockedUpper = new HashSet<>();

            for (int blocked : blacklist) {
                if (blocked >= validCount) {
                    blockedUpper.add(blocked);
                }
            }

            int candidate = validCount;

            for (int blocked : blacklist) {
                if (blocked >= validCount) {
                    continue;
                }

                while (blockedUpper.contains(candidate)) {
                    candidate++;
                }

                remap.put(blocked, candidate);
                candidate++;
            }
        }

        public int pick() {
            int slot = random.nextInt(validCount);
            return remap.getOrDefault(slot, slot);
        }
    }

    // =====================================================================
    // APPROACHES
    // =====================================================================

    /*
     * 1. MATERIALIZE ALL ALLOWED NUMBERS
     *    O(n) memory/time — impossible when n is huge.
     *
     * 2. REJECTION SAMPLING
     *    Pick from [0,n), retry if blacklisted.
     *    Simple, but runtime deteriorates as blacklist density rises and
     *    a single call has no deterministic retry bound.
     *
     * 3. RANGE REMAPPING
     *    Randomize only over validCount dense slots and repair the holes.
     */

    /*
     * INVARIANT
     *
     * Every slot in [0, validCount) corresponds to exactly one allowed value.
     * Therefore a uniform slot produces a uniform allowed result.
     */

    /*
     * RECALL
     *
     * M = n - B
     * PICK [0..M)
     * LOW BLACKLIST HOLE -> HIGH WHITELIST VALUE
     */

    /*
     * RELATED
     *   remapping/RandomFlipMatrix.java
     */

    public static void main(String[] args) {
        int[] blacklist = {2, 3, 5};
        Solution solution = new Solution(7, blacklist, new Random(13));

        for (int i = 0; i < 1_000; i++) {
            int value = solution.pick();
            assert value >= 0 && value < 7;
            assert value != 2 && value != 3 && value != 5;
        }

        System.out.println("RandomPickWithBlacklist: all checks passed");
    }
}
