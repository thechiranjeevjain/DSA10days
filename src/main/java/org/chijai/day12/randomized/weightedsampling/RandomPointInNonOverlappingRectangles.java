package org.chijai.day12.randomized.weightedsampling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * LeetCode 497 - Random Point in Non-overlapping Rectangles
 *
 * PATTERN
 *   Prefix Sum -> Weighted Sampling
 *
 * SUB-PATTERN
 *   Bucket weight = number of valid outcomes inside the bucket
 *
 * REPO
 *   org/chijai/randomized/weightedsampling/RandomPointInNonOverlappingRectangles.java
 */
public class RandomPointInNonOverlappingRectangles {

    /*
     * ================================================================
     * PROBLEM
     * ================================================================
     *
     * Each rectangle is [x1, y1, x2, y2], boundaries inclusive.
     * Rectangles do not overlap.
     *
     * pick() must return an integer point [x,y] inside any rectangle.
     * Every valid integer point across all rectangles must be equally likely.
     *
     * Example 1
     *   rects = [[1,1,1,1]]
     *   Only valid point = [1,1], so every pick returns [1,1].
     *
     * Example 2
     *   A = [0,0,0,0]       -> 1 point
     *   B = [10,10,11,11]   -> 4 points
     *
     *   Total = 5 points.
     *   Rectangle A must be chosen 1/5 of the time.
     *   Rectangle B must be chosen 4/5 of the time.
     *
     * Choosing rectangles 50/50 would make their individual points unfair.
     */

    /*
     * ================================================================
     * MINIMUM INTUITION
     * ================================================================
     *
     * A rectangle with more valid points needs a larger share of the random
     * sample space.
     *
     * Integer-point count:
     *
     *   width  = x2 - x1 + 1
     *   height = y2 - y1 + 1
     *   weight = width * height
     *
     * Then use LC 528's pattern to choose a rectangle by weight.
     * Once chosen, pick x uniformly in [x1,x2] and y uniformly in [y1,y2].
     */

    // ================================================================
    // PRIMARY SOLUTION
    // Build O(r) | pick O(log r) | Space O(r)
    // ================================================================
    static class Solution {

        private final int[][] rects;
        private final int[] prefix;
        private final Random random = new Random();

        public Solution(int[][] rects) {
            this.rects = rects;
            this.prefix = new int[rects.length];

            int totalPoints = 0;

            for (int i = 0; i < rects.length; i++) {
                totalPoints += pointCount(rects[i]);
                prefix[i] = totalPoints;
            }
        }

        public int[] pick() {
            int totalPoints = prefix[prefix.length - 1];
            int target = random.nextInt(totalPoints) + 1;

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

            int[] rect = rects[left];

            int width = rect[2] - rect[0] + 1;
            int height = rect[3] - rect[1] + 1;

            int x = rect[0] + random.nextInt(width);
            int y = rect[1] + random.nextInt(height);

            return new int[]{x, y};
        }
    }

    /*
     * ================================================================
     * APPROACH PROGRESSION
     * ================================================================
     *
     * 1. MATERIALIZE EVERY INTEGER POINT
     *
     *   Enumerate all valid [x,y] points and pick one uniformly.
     *   Build/Space O(totalPoints), pick O(1).
     *   Correct but can store far more data than needed.
     *
     * 2. WEIGHT RECTANGLES + LINEAR SCAN
     *
     *   Store cumulative point counts.
     *   Pick one target in 1..totalPoints.
     *   Scan for the rectangle owning it.
     *   Build O(r), pick O(r), space O(r).
     *
     * 3. WEIGHT RECTANGLES + BINARY SEARCH
     *
     *   Prefix counts are sorted, so binary-search first prefix >= target.
     *   Build O(r), pick O(log r), space O(r).
     *
     *   This is the primary solution.
     */

    static class MaterializedPointsSolution {

        private final List<int[]> points = new ArrayList<>();
        private final Random random = new Random();

        MaterializedPointsSolution(int[][] rects) {
            for (int[] rect : rects) {
                for (int x = rect[0]; x <= rect[2]; x++) {
                    for (int y = rect[1]; y <= rect[3]; y++) {
                        points.add(new int[]{x, y});
                    }
                }
            }
        }

        int[] pick() {
            return points.get(random.nextInt(points.size()));
        }
    }

    static class WeightedLinearSolution {

        private final int[][] rects;
        private final int[] prefix;
        private final Random random = new Random();

        WeightedLinearSolution(int[][] rects) {
            this.rects = rects;
            this.prefix = new int[rects.length];

            int total = 0;

            for (int i = 0; i < rects.length; i++) {
                total += pointCount(rects[i]);
                prefix[i] = total;
            }
        }

        int[] pick() {
            int target = random.nextInt(prefix[prefix.length - 1]) + 1;

            int index = 0;
            while (prefix[index] < target) {
                index++;
            }

            int[] rect = rects[index];
            int x = rect[0] + random.nextInt(rect[2] - rect[0] + 1);
            int y = rect[1] + random.nextInt(rect[3] - rect[1] + 1);

            return new int[]{x, y};
        }
    }

    /*
     * WHY +1?
     *
     * Coordinates are inclusive.
     * [1,1,1,1] has width 1 - 1 + 1 = 1, not 0.
     */

    /*
     * CORRECTNESS
     *
     * Let rectangle R contain k points out of T total points.
     *
     *   P(choose R)          = k / T
     *   P(point | chose R)   = 1 / k
     *
     * Therefore every point has probability:
     *
     *   (k / T) * (1 / k) = 1 / T
     */

    /*
     * RELATED — WORKING FILE
     *
     * RandomPickWithWeight.java
     *   Exact same weighted-bucket selection pattern.
     */

    /*
     * RECALL
     *
     * rectangle weight = integer-point count
     * -> prefix counts
     * -> weighted rectangle
     * -> uniform x and y inside it
     */

    private static int pointCount(int[] rect) {
        int width = rect[2] - rect[0] + 1;
        int height = rect[3] - rect[1] + 1;
        return width * height;
    }

    public static void main(String[] args) {
        Solution onePoint = new Solution(new int[][]{{1, 1, 1, 1}});
        assert equals(onePoint.pick(), new int[]{1, 1});

        Solution solution = new Solution(new int[][]{
                {0, 0, 0, 0},
                {10, 10, 11, 11}
        });

        for (int i = 0; i < 10_000; i++) {
            int[] point = solution.pick();
            assert isInside(point, new int[]{0, 0, 0, 0})
                    || isInside(point, new int[]{10, 10, 11, 11});
        }

        System.out.println("RandomPointInNonOverlappingRectangles: all checks passed");
    }

    private static boolean isInside(int[] point, int[] rect) {
        return rect[0] <= point[0] && point[0] <= rect[2]
                && rect[1] <= point[1] && point[1] <= rect[3];
    }

    private static boolean equals(int[] a, int[] b) {
        return a[0] == b[0] && a[1] == b[1];
    }
}
