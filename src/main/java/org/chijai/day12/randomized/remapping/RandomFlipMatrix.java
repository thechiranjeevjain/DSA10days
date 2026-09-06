package org.chijai.day12.randomized.remapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * LeetCode 519 - Random Flip Matrix
 *
 * PATTERN
 *   Randomized Algorithms -> Lazy Fisher-Yates / Hash Remapping
 */
public class RandomFlipMatrix {

    /*
     * FIRST-PRINCIPLES INVENTION PATH
     *
     * Flatten the matrix into virtual indices [0..m*n).
     * We need to sample without replacement.
     *
     * Fisher-Yates would choose one slot from the remaining range and swap it
     * with the last remaining slot. But materializing m*n cells is wasteful.
     *
     * Store only swaps that differ from identity in a HashMap.
     */

    // =====================================================================
    // OPTIMAL SOLUTION — Lazy Fisher-Yates
    // flip O(1) expected, space O(number of flips since reset)
    // reset O(number of stored remaps) because HashMap.clear()
    // =====================================================================
    static class Solution {

        private final int cols;
        private final int total;
        private final Map<Integer, Integer> remap = new HashMap<>();
        private final Random random;
        private int remaining;

        public Solution(int m, int n) {
            this(m, n, new Random());
        }

        Solution(int m, int n, Random random) {
            this.cols = n;
            this.total = m * n;
            this.remaining = total;
            this.random = random;
        }

        public int[] flip() {
            int slot = random.nextInt(remaining);
            int actual = remap.getOrDefault(slot, slot);

            remaining--;

            int lastAvailable = remap.getOrDefault(remaining, remaining);
            remap.put(slot, lastAvailable);
            remap.remove(remaining);

            return new int[]{actual / cols, actual % cols};
        }

        public void reset() {
            remap.clear();
            remaining = total;
        }
    }

    // =====================================================================
    // APPROACHES
    // =====================================================================

    /*
     * 1. STORE ALL ZERO CELLS IN A LIST
     *    Correct, but O(m*n) memory.
     *
     * 2. RANDOM CELL + RETRY IF ALREADY USED
     *    Rejection sampling becomes terrible near the end.
     *
     * 3. LAZY FISHER-YATES
     *    Treat cells as a virtual array and remember only displaced indices.
     */

    /*
     * INVARIANT
     *
     * Virtual slots [0..remaining) represent exactly the cells that have not
     * yet been returned. remap[x] tells which real cell currently occupies
     * virtual slot x when identity no longer holds.
     */

    /*
     * THE CRITICAL FOUR LINES
     *
     * slot = random [0..remaining)
     * actual = map.getOrDefault(slot, slot)
     * --remaining
     * map[slot] = map.getOrDefault(remaining, remaining)
     */

    /*
     * RECALL
     *
     * VIRTUAL ARRAY
     * RANDOM REMAINING SLOT
     * RETURN ITS REAL VALUE
     * MOVE LAST AVAILABLE VALUE INTO THE HOLE
     */

    /*
     * RELATED
     *   shuffle/ShuffleAnArray.java
     *   remapping/RandomPickWithBlacklist.java
     */

    public static void main(String[] args) {
        Solution solution = new Solution(2, 3, new Random(17));
        boolean[][] seen = new boolean[2][3];

        for (int i = 0; i < 6; i++) {
            int[] cell = solution.flip();
            assert !seen[cell[0]][cell[1]];
            seen[cell[0]][cell[1]] = true;
        }

        solution.reset();
        int[] firstAfterReset = solution.flip();
        assert firstAfterReset[0] >= 0 && firstAfterReset[0] < 2;
        assert firstAfterReset[1] >= 0 && firstAfterReset[1] < 3;

        System.out.println("RandomFlipMatrix: all checks passed");
    }
}
