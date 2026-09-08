package org.chijai.day12.randomized.remapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * LeetCode 519 - Random Flip Matrix
 *
 * PATTERN
 *   Lazy Fisher-Yates -> Sparse Hash Remapping
 *
 * CORE TRANSFORMATION
 *   2D matrix -> virtual 1D array -> sample without replacement
 *
 * REPO
 *   org/chijai/randomized/remapping/RandomFlipMatrix.java
 */
public class RandomFlipMatrix {

    /*
     * ================================================================
     * PROBLEM
     * ================================================================
     *
     * An m x n matrix starts with all 0s.
     *
     * flip():
     *   uniformly choose one cell that is still 0,
     *   make it 1,
     *   return [row,col].
     *
     * The same cell cannot be returned again until reset().
     *
     * reset():
     *   conceptually restore every cell to 0.
     *
     * Example
     *   m = 2, n = 2
     *
     *   First flip: each of 4 cells has probability 1/4.
     *   After one cell is chosen, each remaining cell has probability 1/3.
     *   After 4 flips, every cell has appeared exactly once.
     */

    /*
     * ================================================================
     * MINIMUM INTUITION
     * ================================================================
     *
     * Flatten the matrix:
     *
     *   index = row * cols + col
     *   row   = index / cols
     *   col   = index % cols
     *
     * Now the problem is:
     *   uniformly choose values from [0..m*n-1] without replacement.
     *
     * Fisher-Yates says:
     *   choose a random slot from the active range,
     *   return its value,
     *   move the last active value into that slot,
     *   shrink the active range.
     *
     * We do not materialize the huge array. A HashMap stores only slots whose
     * value is no longer the identity value.
     */

    // ================================================================
    // PRIMARY SOLUTION — LAZY FISHER-YATES
    // flip O(1) expected | reset O(changes) | Space O(flips since reset)
    // ================================================================
    static class Solution {

        private final int cols;
        private final int total;
        private final Map<Integer, Integer> remap = new HashMap<>();
        private final Random random = new Random();
        private int remaining;

        public Solution(int m, int n) {
            cols = n;
            total = m * n;
            remaining = total;
        }

        public int[] flip() {
            int slot = random.nextInt(remaining);
            int actual = remap.getOrDefault(slot, slot);

            remaining--;

            int last = remap.getOrDefault(remaining, remaining);
            remap.put(slot, last);

            return new int[]{actual / cols, actual % cols};
        }

        public void reset() {
            remap.clear();
            remaining = total;
        }
    }

    /*
     * ================================================================
     * APPROACH PROGRESSION
     * ================================================================
     *
     * 1. MATERIALIZE ALL AVAILABLE CELLS
     *
     *   Store every flat index in a list.
     *   Choose and remove a random list element.
     *   Space O(m*n); ArrayList removal can be O(m*n).
     *
     * 2. RANDOM CELL + REJECTION
     *
     *   Pick any cell; retry if already used.
     *   Needs O(m*n) used[] memory and becomes very slow near the end.
     *
     * 3. FULL FISHER-YATES ARRAY
     *
     *   Store [0,1,2,...,m*n-1].
     *   Each flip is O(1), but space is still O(m*n).
     *
     * 4. LAZY FISHER-YATES
     *
     *   Most virtual slots still contain their own index.
     *   Store only changed slot -> value mappings.
     *   This is the primary solution.
     */

    static class MaterializedAvailableSolution {

        private final int rows;
        private final int cols;
        private final List<Integer> available = new ArrayList<>();
        private final Random random = new Random();

        MaterializedAvailableSolution(int m, int n) {
            rows = m;
            cols = n;
            reset();
        }

        int[] flip() {
            int listIndex = random.nextInt(available.size());
            int actual = available.remove(listIndex);
            return new int[]{actual / cols, actual % cols};
        }

        void reset() {
            available.clear();

            for (int index = 0; index < rows * cols; index++) {
                available.add(index);
            }
        }
    }

    static class RejectionSamplingSolution {

        private final int rows;
        private final int cols;
        private final boolean[] used;
        private final Random random = new Random();

        RejectionSamplingSolution(int m, int n) {
            rows = m;
            cols = n;
            used = new boolean[m * n];
        }

        int[] flip() {
            while (true) {
                int actual = random.nextInt(rows * cols);

                if (!used[actual]) {
                    used[actual] = true;
                    return new int[]{actual / cols, actual % cols};
                }
            }
        }
    }

    /*
     * INVARIANT
     *
     * Virtual slots [0, remaining) represent exactly the cells not returned yet.
     * Choosing one slot uniformly therefore chooses one remaining cell uniformly.
     */

    /*
     * RELATED — WORKING FILES
     *
     * ../shuffle/ShuffleAnArray.java
     *   Fisher-Yates with a real array.
     *
     * RandomPickWithBlacklist.java
     *   Dense random range + sparse remapping.
     */

    /*
     * RECALL
     *
     * flatten matrix
     * -> random slot in active range
     * -> return value stored there
     * -> move last active value into hole
     * -> shrink range
     * -> map only changed slots
     */

    public static void main(String[] args) {
        Solution solution = new Solution(2, 3);
        boolean[][] seen = new boolean[2][3];

        for (int i = 0; i < 6; i++) {
            int[] point = solution.flip();
            assert !seen[point[0]][point[1]];
            seen[point[0]][point[1]] = true;
        }

        solution.reset();

        for (int i = 0; i < 6; i++) {
            int[] point = solution.flip();
            assert 0 <= point[0] && point[0] < 2;
            assert 0 <= point[1] && point[1] < 3;
        }

        System.out.println("RandomFlipMatrix: all checks passed");
    }
}
