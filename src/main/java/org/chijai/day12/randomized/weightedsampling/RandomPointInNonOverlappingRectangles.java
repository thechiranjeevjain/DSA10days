package org.chijai.day12.randomized.weightedsampling;

import java.util.Random;

/**
 * LeetCode 497 - Random Point in Non-overlapping Rectangles
 *
 * PATTERN
 *   Randomized Algorithms -> Weighted Sampling
 *
 * CORE TRANSFORMATION
 *   rectangle point-count -> cumulative interval -> uniform ticket
 */
public class RandomPointInNonOverlappingRectangles {

    /*
     * FIRST-PRINCIPLES INVENTION PATH
     *
     * Every integer point across all rectangles must be equally likely.
     * A rectangle containing more integer points must therefore be selected
     * proportionally more often.
     *
     * Weight(rect) = number of integer points inside it
     *              = (x2 - x1 + 1) * (y2 - y1 + 1)
     *
     * Weighted-pick a rectangle, then map the chosen local ticket to one
     * exact point inside that rectangle.
     */

    // =====================================================================
    // OPTIMAL SOLUTION — Interview Preferred
    // Constructor O(r), pick O(log r), space O(r)
    // =====================================================================
    static class Solution {

        private final int[][] rects;
        private final long[] prefix;
        private final Random random;

        public Solution(int[][] rects) {
            this(rects, new Random());
        }

        Solution(int[][] rects, Random random) {
            this.rects = rects;
            this.random = random;
            this.prefix = new long[rects.length];

            long running = 0;

            for (int i = 0; i < rects.length; i++) {
                running += pointCount(rects[i]);
                prefix[i] = running;
            }
        }

        public int[] pick() {
            long totalPoints = prefix[prefix.length - 1];
            long ticket = random.nextLong(totalPoints) + 1;

            int rectangleIndex = firstPrefixAtLeast(ticket);
            int[] rect = rects[rectangleIndex];

            long previousPrefix = rectangleIndex == 0
                    ? 0
                    : prefix[rectangleIndex - 1];

            long offset = ticket - previousPrefix - 1;
            long width = (long) rect[2] - rect[0] + 1;

            int x = (int) (rect[0] + offset % width);
            int y = (int) (rect[1] + offset / width);

            return new int[]{x, y};
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

        private static long pointCount(int[] rect) {
            long width = (long) rect[2] - rect[0] + 1;
            long height = (long) rect[3] - rect[1] + 1;
            return width * height;
        }
    }

    /*
     * WHY THE +1 MATTERS
     *
     * Rectangle [x1, y1, x2, y2] uses inclusive integer coordinates.
     * [1..1] contains one coordinate, not zero.
     * Therefore width = x2 - x1 + 1 and height = y2 - y1 + 1.
     */

    // =====================================================================
    // APPROACHES
    // =====================================================================

    /*
     * 1. MATERIALIZE EVERY INTEGER POINT
     *    Easy but space/time proportional to total number of points.
     *
     * 2. CHOOSE RECTANGLE UNIFORMLY, THEN POINT UNIFORMLY
     *    Wrong when rectangles contain different numbers of points.
     *
     * 3. WEIGHTED RECTANGLE + PREFIX + BINARY SEARCH
     *    Correct: rectangle probability is proportional to point count.
     */

    /*
     * RECALL
     *
     * RECTANGLE WEIGHT = INTEGER POINT COUNT
     * PREFIX COUNTS
     * PICK GLOBAL TICKET
     * LOWER BOUND RECTANGLE
     * CONVERT LOCAL OFFSET -> (x, y)
     */

    /*
     * RELATED
     *   weightedsampling/RandomPickWithWeight.java
     */

    public static void main(String[] args) {
        int[][] rects = {
                {1, 1, 1, 1},
                {10, 20, 11, 21}
        };

        Solution solution = new Solution(rects, new Random(11));

        for (int i = 0; i < 1_000; i++) {
            int[] point = solution.pick();

            boolean inFirst = point[0] == 1 && point[1] == 1;
            boolean inSecond = point[0] >= 10 && point[0] <= 11
                    && point[1] >= 20 && point[1] <= 21;

            assert inFirst || inSecond;
        }

        System.out.println("RandomPointInNonOverlappingRectangles: all checks passed");
    }
}
