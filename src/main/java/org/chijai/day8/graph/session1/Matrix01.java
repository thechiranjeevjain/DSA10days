package org.chijai.day8.graph.session1;

import java.util.*;

/**
 * 01 Matrix
 *
 * An implementation-oriented chapter focused on Multi-Source BFS.
 *
 * Java 17
 */
public class Matrix01 {

    /*==============================================================
     *
     * 1. 📘 PRIMARY PROBLEM
     *
     *==============================================================*/

    /*
 *
 * 01 Matrix
 *
 * Difficulty: Medium
 * Primary Pattern: Multi-Source BFS
 * Official LeetCode: https://leetcode.com/problems/01-matrix/
 *
 * --------------------------------------------------
 * Problem Statement
 * --------------------------------------------------
 *
 * You are given an m x n binary matrix mat. Every cell contains
 * either 0 or 1.
 *
 * For every cell, return the minimum number of moves required
 * to reach the nearest cell containing 0.
 *
 * A move is allowed only between cells that share a side:
 *
 * • up
 * • down
 * • left
 * • right
 *
 * Diagonal movement is not allowed, and every legal move costs 1.
 *
 * Therefore, the returned matrix answer must satisfy:
 *
 * answer[row][col] = shortest distance from (row, col)
 *                    to any cell containing 0
 *
 * Every zero naturally has distance 0.
 *
 * --------------------------------------------------
 * Example 1
 * --------------------------------------------------
 *
 * Input
 *
 * [
 *   [0,0,0],
 *   [0,1,0],
 *   [0,0,0]
 * ]
 *
 * Output
 *
 * [
 *   [0,0,0],
 *   [0,1,0],
 *   [0,0,0]
 * ]
 *
 * Explanation
 *
 * Every zero is already at distance 0. The center cell is directly
 * adjacent to a zero, so its nearest-zero distance is 1.
 *
 * --------------------------------------------------
 * Example 2
 * --------------------------------------------------
 *
 * Input
 *
 * [
 *   [0,0,0],
 *   [0,1,0],
 *   [1,1,1]
 * ]
 *
 * Output
 *
 * [
 *   [0,0,0],
 *   [0,1,0],
 *   [1,2,1]
 * ]
 *
 * Explanation
 *
 * Cells (2,0) and (2,2) are each one move from a zero.
 *
 * Cell (2,1) needs two moves. One shortest route is:
 *
 * (2,1) -> (2,0) -> (1,0)
 *
 * so its answer is 2.
 *
 * --------------------------------------------------
 * Example 3
 * --------------------------------------------------
 *
 * Input
 *
 * [
 *   [1,1,1,1,0]
 * ]
 *
 * Output
 *
 * [
 *   [4,3,2,1,0]
 * ]
 *
 * Explanation
 *
 * The zero has distance 0. Moving one cell farther away increases
 * the shortest distance by exactly 1.
 *
 * --------------------------------------------------
 * Constraints
 * --------------------------------------------------
 *
 * 1 <= m, n <= 10^4
 * 1 <= m * n <= 10^4
 * mat[row][col] is either 0 or 1
 * mat contains at least one 0
 *
 */

    /*==============================================================
     *
     * 2. ⭐ PRIMARY SOLUTION — MULTI-SOURCE BFS
     *
     *==============================================================*/

    /*
 *
 * The easiest way to derive the solution is to reverse the search.
 *
 * A direct approach would start from every 1 and search for its
 * nearest 0. That works, but many searches would repeatedly walk
 * through the same cells.
 *
 * Instead, start from the information already known:
 *
 * every 0 has final distance 0.
 *
 * Put all zeros into one queue before BFS begins. They now behave
 * like simultaneous sources whose waves expand through the matrix.
 *
 * Because every move costs exactly 1, ordinary BFS is sufficient.
 *
 * --------------------------------------------------
 * State Representation
 * --------------------------------------------------
 *
 * Reuse the input matrix as both the answer and visited state:
 *
 * 0 or greater  = final shortest distance
 * -1            = not finalized yet
 *
 * During initialization, every original 0 stays 0 and every
 * original 1 becomes -1.
 *
 * --------------------------------------------------
 * Transition
 * --------------------------------------------------
 *
 * When BFS removes a finalized cell, each adjacent unvisited cell
 * is exactly one move farther away:
 *
 * matrix[nextRow][nextCol]
 *     = matrix[currentRow][currentCol] + 1
 *
 * The neighbor is then added to the queue so the wave can continue.
 *
 * --------------------------------------------------
 * Reconstructable Recipe
 * --------------------------------------------------
 *
 * 1. Put every zero into the queue.
 * 2. Mark every one as -1.
 * 3. Repeatedly poll one cell and inspect its four neighbors.
 * 4. Skip boundaries and already-finalized cells.
 * 5. Assign current distance + 1, then enqueue the neighbor.
 * 6. Return the matrix.
 *
 */

    /**
     * ------------------------------------------------------------
     * PRIMARY — Multi-Source BFS
     * ------------------------------------------------------------
     *
     * Time:
     * O(rows * cols)
     *
     * Space:
     * O(rows * cols)
     *
     * Every cell enters the queue at most once.
     */
    static class Optimal {

        private static final int[][] DIRECTIONS = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        public int[][] updateMatrix(int[][] matrix) {

            int rows = matrix.length;
            int cols = matrix[0].length;

            Queue<int[]> queue = new ArrayDeque<>();

            /*
             *
             * Every zero is already a finalized source
             * with distance 0.
             *
             * -1 means the distance is not finalized yet.
             *
             */
            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (matrix[row][col] == 0) {

                        queue.offer(new int[]{row, col});

                    } else {

                        matrix[row][col] = -1;
                    }
                }
            }

            while (!queue.isEmpty()) {

                int[] current = queue.poll();

                int currentRow = current[0];
                int currentCol = current[1];

                for (int[] direction : DIRECTIONS) {

                    int nextRow = currentRow + direction[0];
                    int nextCol = currentCol + direction[1];

                    /*
                     *
                     * Ignore positions outside the matrix.
                     *
                     */
                    if (nextRow < 0
                            || nextCol < 0
                            || nextRow >= rows
                            || nextCol >= cols) {
                        continue;
                    }

                    /*
                     *
                     * Only -1 cells are still unvisited.
                     *
                     * Any non-negative value is already
                     * the final shortest distance.
                     *
                     */
                    if (matrix[nextRow][nextCol] != -1) {
                        continue;
                    }

                    /*
                     *
                     * First BFS arrival is the shortest.
                     *
                     * The neighbor is exactly one move farther
                     * than the current finalized cell.
                     *
                     */
                    matrix[nextRow][nextCol] =
                            matrix[currentRow][currentCol] + 1;

                    queue.offer(new int[]{nextRow, nextCol});
                }
            }

            return matrix;
        }
    }

    /*==============================================================
     *
     * 3. 🧪 PRIMARY DRY RUN
     *
     *==============================================================*/

    /*
 *
 * Input
 *
 * 0 0 0
 * 0 1 0
 * 1 1 1
 *
 * --------------------------------------------------
 * Initialization
 * --------------------------------------------------
 *
 * All zeros enter the queue and every 1 becomes -1:
 *
 *  0  0  0
 *  0 -1  0
 * -1 -1 -1
 *
 * --------------------------------------------------
 * First Expansion
 * --------------------------------------------------
 *
 * The initial zeros have distance 0. Any adjacent -1 cell reached
 * from them receives:
 *
 * 0 + 1 = 1
 *
 * Matrix after those discoveries:
 *
 * 0 0 0
 * 0 1 0
 * 1 . 1
 *
 * The newly finalized distance-1 cells are now waiting in the queue.
 *
 * --------------------------------------------------
 * Second Expansion
 * --------------------------------------------------
 *
 * The bottom-middle cell is still unvisited. It is reached from
 * a cell whose distance is 1, so:
 *
 * 1 + 1 = 2
 *
 * Final matrix:
 *
 * 0 0 0
 * 0 1 0
 * 1 2 1
 *
 * No cell chooses a particular zero. All zero-sources expand
 * together, and BFS naturally keeps the nearest arrival.
 *
 */

    /*==============================================================
     *
     * 4. ✅ CORRECTNESS + COMPLEXITY
     *
     *==============================================================*/

    /*
 *
 * Core Invariant
 *
 * Every cell removed from the queue already stores its final
 * shortest distance to a zero.
 *
 * --------------------------------------------------
 * Why First Arrival Is Optimal
 * --------------------------------------------------
 *
 * Assume a cell is first assigned distance d.
 *
 * If a shorter path of length d - 1 existed, BFS would have
 * processed that shorter-distance path earlier and reached the
 * cell before distance d was assigned.
 *
 * That is impossible.
 *
 * Therefore the first assignment is the shortest distance, and
 * the cell never needs to be updated again.
 *
 * --------------------------------------------------
 * Time Complexity
 * --------------------------------------------------
 *
 * O(rows * cols)
 *
 * There are rows * cols cells. Each cell is finalized and enqueued
 * at most once, and each dequeue checks only four neighbors.
 *
 * Total work is therefore proportional to the number of cells.
 *
 * --------------------------------------------------
 * Space Complexity
 * --------------------------------------------------
 *
 * O(rows * cols)
 *
 * The queue may contain a linear number of cells in the worst case.
 *
 * No separate visited matrix is required because -1 represents
 * the unvisited state.
 *
 */

    /*==============================================================
     *
     * 5. 🔵 CLASSIFICATION + PATTERN RECOGNITION
     *
     *==============================================================*/

    /*
 *
 * Primary Category: Graph
 * Representation: Matrix / Grid Graph
 * Primary Pattern: Multi-Source BFS
 *
 * Each matrix cell is a graph node, and legal up/down/left/right
 * moves are unweighted edges.
 *
 * --------------------------------------------------
 * Recognition Signals
 * --------------------------------------------------
 *
 * Think Multi-Source BFS when the problem contains most of these:
 *
 * • nearest source
 * • shortest number of moves
 * • multiple valid starting sources
 * • equal movement cost
 * • grid or other unweighted graph
 *
 * --------------------------------------------------
 * Pattern Boundary
 * --------------------------------------------------
 *
 * One source + equal edge cost
 * -> Single-Source BFS
 *
 * Many sources + equal edge cost
 * -> Multi-Source BFS
 *
 * Unequal non-negative edge costs
 * -> Dijkstra
 *
 * Negative edge weights
 * -> Bellman-Ford
 *
 */

    /*==============================================================
     *
     * 6. ⚠️ TRAPS + WRONG APPROACHES
     *
     *==============================================================*/

    /*
 *
 * Trap 1 — Run one BFS from every 1
 *
 * Each individual BFS is correct, but the same matrix regions are
 * explored repeatedly. Worst-case time becomes O((rows * cols)^2).
 *
 * --------------------------------------------------
 *
 * Trap 2 — Use DFS for shortest distance
 *
 * DFS does not process nodes in increasing distance order, so its
 * first arrival at a cell is not guaranteed to be shortest.
 *
 * --------------------------------------------------
 *
 * Trap 3 — Start from only one zero
 *
 * That computes distance to one particular zero, not distance to
 * the nearest zero among all possible sources.
 *
 * --------------------------------------------------
 *
 * Trap 4 — Revisit finalized cells
 *
 * Once BFS assigns a cell, later arrivals cannot improve it.
 * Reprocessing only adds unnecessary work.
 *
 * --------------------------------------------------
 *
 * Trap 5 — Ignore unequal movement costs
 *
 * The BFS guarantee depends on every move costing the same amount.
 * If costs vary, use an appropriate weighted shortest-path algorithm.
 *
 */

    /*==============================================================
     *
     * 7. 🟡 ALTERNATIVE — TWO-PASS DP
     *
     *==============================================================*/

    /*
 *
 * A second O(rows * cols) solution uses two directional DP passes.
 *
 * Pass 1 moves from top-left to bottom-right and lets each non-zero
 * cell use information already available from its top and left.
 *
 * Pass 2 moves from bottom-right to top-left and adds information
 * from its bottom and right.
 *
 * Across both passes, every cell can receive information from all
 * four directions.
 *
 * Time: O(rows * cols)
 * Extra Space: O(1), excluding the reused matrix.
 *
 * This is a valuable alternative, but not the primary solution here.
 * Multi-Source BFS follows more directly from the problem statement
 * and is easier to reconstruct after a long gap.
 *
 */

    static class Improved {

        public int[][] updateMatrix(int[][] mat) {

            int rows = mat.length;
            int cols = mat[0].length;

            int infinity = rows + cols + 5;

            /*
             *
             * First pass:
             *
             * use only top and left information.
             *
             */
            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (mat[row][col] == 0) {
                        continue;
                    }

                    int best = infinity;

                    if (row > 0) {
                        best = Math.min(
                                best,
                                mat[row - 1][col] + 1
                        );
                    }

                    if (col > 0) {
                        best = Math.min(
                                best,
                                mat[row][col - 1] + 1
                        );
                    }

                    mat[row][col] = best;
                }
            }

            /*
             *
             * Second pass:
             *
             * add bottom and right information.
             *
             */
            for (int row = rows - 1; row >= 0; row--) {

                for (int col = cols - 1; col >= 0; col--) {

                    if (row + 1 < rows) {
                        mat[row][col] =
                                Math.min(
                                        mat[row][col],
                                        mat[row + 1][col] + 1
                                );
                    }

                    if (col + 1 < cols) {
                        mat[row][col] =
                                Math.min(
                                        mat[row][col],
                                        mat[row][col + 1] + 1
                                );
                    }
                }
            }

            return mat;
        }
    }

    /*==============================================================
     *
     * 8. 🔴 BRUTE FORCE — LEARNING ONLY
     *
     *==============================================================*/

    /*
 *
 * For every cell containing 1, run a separate BFS until a zero is
 * found.
 *
 * The approach is correct because each individual BFS finds a
 * shortest path. The problem is duplicated exploration: different
 * starting cells repeatedly search the same regions.
 *
 * Time: O((rows * cols)^2)
 * Space: O(rows * cols)
 *
 * Its main value is pedagogical: it motivates reversing the search
 * and running one BFS from all zeros together.
 *
 */

    static class BruteForce {

        public int[][] updateMatrix(int[][] mat) {

            int rows = mat.length;
            int cols = mat[0].length;

            int[][] answer = new int[rows][cols];

            int[][] directions = {
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {0, -1}
            };

            for (int startRow = 0; startRow < rows; startRow++) {

                for (int startCol = 0; startCol < cols; startCol++) {

                    if (mat[startRow][startCol] == 0) {
                        answer[startRow][startCol] = 0;
                        continue;
                    }

                    boolean[][] visited =
                            new boolean[rows][cols];

                    Queue<int[]> queue =
                            new ArrayDeque<>();

                    queue.offer(new int[]{
                            startRow,
                            startCol
                    });

                    visited[startRow][startCol] = true;

                    int distance = 0;

                    boolean found = false;

                    while (!queue.isEmpty() && !found) {

                        int levelSize = queue.size();

                        while (levelSize-- > 0) {

                            int[] current = queue.poll();

                            if (mat[current[0]][current[1]] == 0) {

                                answer[startRow][startCol] =
                                        distance;

                                found = true;

                                break;
                            }

                            for (int[] direction : directions) {

                                int nextRow =
                                        current[0] + direction[0];

                                int nextCol =
                                        current[1] + direction[1];

                                if (nextRow < 0
                                        || nextCol < 0
                                        || nextRow >= rows
                                        || nextCol >= cols
                                        || visited[nextRow][nextCol]) {
                                    continue;
                                }

                                visited[nextRow][nextCol] = true;

                                queue.offer(new int[]{
                                        nextRow,
                                        nextCol
                                });
                            }
                        }

                        distance++;
                    }
                }
            }

            return answer;
        }
    }

    /*==============================================================
     *
     * 9. 🔄 RELATED PROBLEMS + DELTA
     *
     *==============================================================*/

    /*
 *
 * Walls and Gates
 *
 * Sources: every gate
 * Delta: walls block movement
 *
 * --------------------------------------------------
 *
 * Rotting Oranges
 *
 * Sources: every initially rotten orange
 * Delta: BFS distance represents elapsed minutes
 *
 * --------------------------------------------------
 *
 * Nearest Facility / Nearest Exit
 *
 * Sources: every valid destination
 * Delta: choose the source set according to the problem
 *
 * --------------------------------------------------
 *
 * Fire / Virus Spread
 *
 * Sources: every initial fire or infected cell
 * Delta: BFS distance represents spread time
 *
 * --------------------------------------------------
 *
 * Transfer Rule
 *
 * Many sources spreading through equal-cost edges
 * -> think Multi-Source BFS.
 *
 */

    /*==============================================================
     *
     * 10. 🎯 INTERVIEW ARTICULATION
     *
     *==============================================================*/

    /*
 *
 * I would treat every zero as a BFS source and put all of them into
 * the queue before traversal begins.
 *
 * I mark every 1 as unvisited using -1. Since every grid move costs
 * one, BFS reaches cells in increasing shortest-distance order.
 *
 * When I first reach an unvisited neighbor, its final distance is
 * the current cell's distance + 1. I assign that value immediately
 * and enqueue the neighbor so the wave continues.
 *
 * Every cell is finalized at most once, so the algorithm runs in
 * O(rows * cols) time with O(rows * cols) worst-case queue space.
 *
 */

    /*==============================================================
     *
     * 11. 🧠 SIX-MONTH RECALL CARD
     *
     *==============================================================*/

    /*
 *
 * Trigger:
 * nearest source + many sources + equal move cost
 *
 * Pattern:
 * Multi-Source BFS
 *
 * Sources:
 * every zero
 *
 * Sentinel:
 * -1 = unvisited
 *
 * Invariant:
 * first BFS arrival is shortest
 *
 * Transition:
 * neighbor = current + 1
 *
 * Skip:
 * outside matrix or already finalized
 *
 * Complexity:
 * O(rows * cols)
 *
 * Memory Cue:
 * Do not make every 1 search for a zero.
 * Let every zero spread together.
 *
 */

    /*==============================================================
     *
     * 12. 🧪 MAIN + SELF-VERIFYING TESTS
     *
     *==============================================================*/

    private static void assertMatrixEquals(
            int[][] expected,
            int[][] actual) {

        assert expected.length == actual.length
                : "Row count mismatch";

        for (int row = 0; row < expected.length; row++) {

            assert Arrays.equals(expected[row], actual[row])
                    : "Mismatch at row " + row
                    + " expected=" + Arrays.toString(expected[row])
                    + " actual=" + Arrays.toString(actual[row]);
        }
    }

    private static int[][] copy(int[][] matrix) {

        int[][] result = new int[matrix.length][];

        for (int row = 0; row < matrix.length; row++) {
            result[row] = matrix[row].clone();
        }

        return result;
    }

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        /*
         *
         * Official-style example.
         *
         */
        {
            int[][] input = {
                    {0, 0, 0},
                    {0, 1, 0},
                    {1, 1, 1}
            };

            int[][] expected = {
                    {0, 0, 0},
                    {0, 1, 0},
                    {1, 2, 1}
            };

            assertMatrixEquals(
                    expected,
                    solver.updateMatrix(copy(input))
            );
        }

        /*
         *
         * Single cell.
         *
         */
        {
            int[][] input = {
                    {0}
            };

            int[][] expected = {
                    {0}
            };

            assertMatrixEquals(
                    expected,
                    solver.updateMatrix(copy(input))
            );
        }

        /*
         *
         * Single row.
         *
         */
        {
            int[][] input = {
                    {1, 1, 0, 1}
            };

            int[][] expected = {
                    {2, 1, 0, 1}
            };

            assertMatrixEquals(
                    expected,
                    solver.updateMatrix(copy(input))
            );
        }

        /*
         *
         * Single column.
         *
         */
        {
            int[][] input = {
                    {1},
                    {1},
                    {0},
                    {1}
            };

            int[][] expected = {
                    {2},
                    {1},
                    {0},
                    {1}
            };

            assertMatrixEquals(
                    expected,
                    solver.updateMatrix(copy(input))
            );
        }

        /*
         *
         * Every cell is already zero.
         *
         */
        {
            int[][] input = {
                    {0, 0},
                    {0, 0}
            };

            int[][] expected = {
                    {0, 0},
                    {0, 0}
            };

            assertMatrixEquals(
                    expected,
                    solver.updateMatrix(copy(input))
            );
        }

        /*
         *
         * One zero in a corner.
         *
         */
        {
            int[][] input = {
                    {0, 1, 1},
                    {1, 1, 1},
                    {1, 1, 1}
            };

            int[][] expected = {
                    {0, 1, 2},
                    {1, 2, 3},
                    {2, 3, 4}
            };

            assertMatrixEquals(
                    expected,
                    solver.updateMatrix(copy(input))
            );
        }

        /*
         *
         * Checkerboard.
         *
         */
        {
            int[][] input = {
                    {0, 1, 0},
                    {1, 0, 1},
                    {0, 1, 0}
            };

            int[][] expected = {
                    {0, 1, 0},
                    {1, 0, 1},
                    {0, 1, 0}
            };

            assertMatrixEquals(
                    expected,
                    solver.updateMatrix(copy(input))
            );
        }

        /*
         *
         * Long propagation.
         *
         */
        {
            int[][] input = {
                    {1, 1, 1, 1, 0}
            };

            int[][] expected = {
                    {4, 3, 2, 1, 0}
            };

            assertMatrixEquals(
                    expected,
                    solver.updateMatrix(copy(input))
            );
        }

        /*
         *
         * Multiple independent sources.
         *
         */
        {
            int[][] input = {
                    {0, 1, 1},
                    {1, 1, 1},
                    {1, 1, 0}
            };

            int[][] expected = {
                    {0, 1, 2},
                    {1, 2, 1},
                    {2, 1, 0}
            };

            assertMatrixEquals(
                    expected,
                    solver.updateMatrix(copy(input))
            );
        }

        System.out.println("All assertions passed.");
    }
}
