package org.chijai.day8.graph.session1;

import java.util.Arrays;

public class FloodFill {

    /*
     * ============================================================
     * 2. 📘 PRIMARY PROBLEM
     * ============================================================
     *
     * Title:
     * Flood Fill
     *
     * Difficulty:
     * Easy
     *
     * Tags:
     * Graph
     * DFS
     * BFS
     * Matrix
     * Recursion
     *
     * LeetCode:
     * https://leetcode.com/problems/flood-fill/
     *
     * ------------------------------------------------------------
     * Problem Description
     * ------------------------------------------------------------
     *
     * You are given an m x n image represented as a 2D integer matrix.
     *
     * image[r][c] represents the color of that pixel.
     *
     * You are also given:
     *
     * sr      -> starting row
     * sc      -> starting column
     * color   -> new color
     *
     * Starting from (sr, sc), replace the color of every pixel that:
     *
     * 1. has the same original color as image[sr][sc]
     * 2. is connected using ONLY 4-directional movement
     *      up
     *      down
     *      left
     *      right
     *
     * Return the modified image.
     *
     * ------------------------------------------------------------
     * Constraints
     * ------------------------------------------------------------
     *
     * 1 <= m, n <= 50
     *
     * 0 <= image[i][j], color < 216
     *
     * 0 <= sr < m
     *
     * 0 <= sc < n
     *
     * ------------------------------------------------------------
     * Representative Example
     * ------------------------------------------------------------
     *
     * image =
     *
     * 1 1 1
     * 1 1 0
     * 1 0 1
     *
     * sr = 1
     * sc = 1
     * color = 2
     *
     * Original component:
     *
     * X X X
     * X X .
     * X . .
     *
     * Result:
     *
     * 2 2 2
     * 2 2 0
     * 2 0 1
     *
     * ------------------------------------------------------------
     * Example 2
     * ------------------------------------------------------------
     *
     * image = [[0,0,0],[0,0,0]]
     *
     * sr = 0
     *
     * sc = 0
     *
     * color = 0
     *
     * Since the starting color already equals the target color,
     * nothing changes.
     *
     * ------------------------------------------------------------
     * Expected Complexity
     * ------------------------------------------------------------
     *
     * Time:
     * O(m × n)
     *
     * Space:
     * O(m × n) recursion stack in worst case
     *
     * (Iterative DFS/BFS avoids recursion depth but not visitation.)
     */

    /*
     * ============================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern
     * -------
     * Graph Traversal on Grid
     *
     * Archetype
     * ---------
     * Connected Component Traversal
     *
     * Core Invariant
     * --------------
     * Every reachable cell having the original color is visited
     * exactly once and immediately converted to the new color.
     *
     * Why It Works
     * ------------
     * The original color defines membership of the connected
     * component.
     *
     * DFS recursively expands to every neighboring cell that still
     * belongs to this component.
     *
     * Recoloring immediately also marks the node as visited.
     *
     * Therefore:
     *
     * - no node is processed twice
     * - traversal cannot loop forever
     * - every reachable node is eventually explored
     *
     * Recognition Signals
     * -------------------
     * Look for:
     *
     * • matrix
     * • connected cells
     * • four directions
     * • spread from one source
     * • replace entire region
     *
     * When To Use
     * -----------
     * Connected-region exploration.
     *
     * Island traversal.
     *
     * Region recoloring.
     *
     * Infection spread.
     *
     * Maze exploration.
     *
     * Component counting.
     *
     * When NOT To Use
     * ---------------
     * Weighted shortest path.
     *
     * Minimum cost problems.
     *
     * Ordered traversal.
     *
     * Dynamic programming states.
     *
     * Comparison
     * ----------
     *
     * DFS
     * ----
     * Natural recursive implementation.
     *
     * BFS
     * ----
     * Same correctness.
     * Different exploration order.
     *
     * Union Find
     * ----------
     * Useful when many connectivity queries exist.
     * Overkill here because only one component is explored.
     */

    /*
     * ============================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * Mental Model
     * ------------
     *
     * Imagine dropping a bucket of paint onto one pixel.
     *
     * Paint spreads only through pixels that:
     *
     * • touch horizontally or vertically
     * • still have the original color
     *
     * Every painted cell immediately becomes part of the finished
     * region and will never be processed again.
     *
     * ------------------------------------------------------------
     * State
     * ------------------------------------------------------------
     *
     * image
     * Current canvas.
     *
     * originalColor
     * Color defining membership of the component.
     *
     * newColor
     * Replacement color.
     *
     * (r,c)
     * Current DFS position.
     *
     * ------------------------------------------------------------
     * Primary Invariant
     * ------------------------------------------------------------
     *
     * Before entering DFS on a cell:
     *
     * the cell belongs to the original connected component.
     *
     * After returning:
     *
     * every reachable cell from this location has already been
     * recolored.
     *
     * ------------------------------------------------------------
     * Visitation Invariant
     * ------------------------------------------------------------
     *
     * Recoloring is the visitation marker.
     *
     * A recolored cell can never satisfy:
     *
     * image[r][c] == originalColor
     *
     * Therefore recursion never revisits it.
     *
     * ------------------------------------------------------------
     * Expansion Invariant
     * ------------------------------------------------------------
     *
     * Expansion occurs ONLY toward neighbors satisfying:
     *
     * inside grid
     *
     * AND
     *
     * original color
     *
     * Every other neighbor is discarded immediately.
     *
     * ------------------------------------------------------------
     * Allowed Moves
     * ------------------------------------------------------------
     *
     * Up
     *
     * Down
     *
     * Left
     *
     * Right
     *
     * ------------------------------------------------------------
     * Forbidden Moves
     * ------------------------------------------------------------
     *
     * Outside boundary.
     *
     * Different color.
     *
     * Already recolored.
     *
     * Diagonal movement.
     *
     * ------------------------------------------------------------
     * Why Immediate Coloring Matters
     * ------------------------------------------------------------
     *
     * Suppose we delayed coloring until recursion finishes.
     *
     * Then adjacent recursive calls could revisit the same cell,
     * producing infinite recursion on cyclic structures.
     *
     * Immediate recoloring simultaneously performs:
     *
     * • update
     * • visited marking
     *
     * with no auxiliary visited array.
     *
     * ------------------------------------------------------------
     * Early Exit Invariant
     * ------------------------------------------------------------
     *
     * If
     *
     * originalColor == newColor
     *
     * then every recursive call would continue satisfying the color
     * check forever because no state changes.
     *
     * Therefore this guard is mandatory before DFS starts.
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * Every recursive call permanently converts one valid cell.
     *
     * Number of valid cells is finite.
     *
     * Therefore recursion must terminate.
     *
     * ------------------------------------------------------------
     * Why Naive Solutions Fail
     * ------------------------------------------------------------
     *
     * A full matrix scan changes disconnected regions.
     *
     * Flood Fill must preserve connectivity.
     *
     * Connectivity—not equality—is the defining property.
     */

    /*
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * Mistake 1
     * ---------
     * Forgetting:
     *
     * if(originalColor == newColor)
     *
     * Why it looks correct:
     *
     * "Nothing changes anyway."
     *
     * Actual problem:
     *
     * Since recoloring does not modify the value,
     * recursion never marks visitation.
     *
     * Infinite recursion occurs.
     *
     * ------------------------------------------------------------
     * Mistake 2
     * ---------
     * Coloring after recursive calls.
     *
     * Violated Invariant:
     *
     * Recolored means visited.
     *
     * Counterexample:
     *
     * Two adjacent equal cells repeatedly recurse into each other.
     *
     * ------------------------------------------------------------
     * Mistake 3
     * ---------
     * Exploring diagonals.
     *
     * Counterexample:
     *
     * 1 0
     * 0 1
     *
     * These two cells are NOT connected.
     *
     * ------------------------------------------------------------
     * Mistake 4
     * ---------
     * Missing boundary checks.
     *
     * Result:
     *
     * ArrayIndexOutOfBoundsException.
     *
     * ------------------------------------------------------------
     * Mistake 5
     * ---------
     * Comparing against newColor instead of originalColor.
     *
     * This accidentally grows into unrelated regions.
     */

    /*
     * ============================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Typing Order
     * ------------
     *
     * 1.
     * public int[][] floodFill(...)
     *
     * 2.
     * Save original color.
     *
     * 3.
     * Early exit if already same color.
     *
     * 4.
     * Call dfs(...)
     *
     * 5.
     * Return image.
     *
     * ------------------------------
     * DFS Skeleton
     * ------------------------------
     *
     * Boundary check.
     *
     * Color mismatch check.
     *
     * Paint current cell.
     *
     * Visit:
     *
     * down
     * up
     * right
     * left
     *
     * Return.
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * old ← image[start]
     *
     * if old == new
     *     return
     *
     * dfs(start)
     *
     * dfs(cell)
     *     reject invalid
     *     reject wrong color
     *     recolor
     *     visit 4 neighbors
     */

    /*
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    /*
     * ------------------------------------------------------------
     * Brute Force
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Keep rescanning the matrix until no additional pixels change.
     *
     * Invariant
     * ---------
     * Every iteration expands one layer.
     *
     * Limitation
     * ----------
     * Repeated scans waste work.
     *
     * Complexity
     * ----------
     * Time:
     * O((mn)^2)
     *
     * Space:
     * O(1)
     *
     * Interview Usefulness
     * --------------------
     * Rarely accepted beyond initial discussion.
     */

    static class BruteForce {

        public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {

            int rows = image.length;
            int cols = image[0].length;

            int oldColor = image[sr][sc];

            if (oldColor == newColor) {
                return image;
            }

            boolean changed = true;

            while (changed) {

                changed = false;

                int[][] snapshot = copy(image);

                for (int r = 0; r < rows; r++) {

                    for (int c = 0; c < cols; c++) {

                        if (snapshot[r][c] != oldColor) {
                            continue;
                        }

                        if (r == sr && c == sc) {
                            image[r][c] = newColor;
                            changed = true;
                            continue;
                        }

                        if (touchesColored(snapshot, image, r, c, newColor)) {
                            image[r][c] = newColor;
                            changed = true;
                        }
                    }
                }
            }

            return image;
        }

        private static boolean touchesColored(int[][] snapshot,
                                              int[][] image,
                                              int r,
                                              int c,
                                              int newColor) {

            int[][] directions = {
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {0, -1}
            };

            for (int[] direction : directions) {

                int nr = r + direction[0];
                int nc = c + direction[1];

                if (nr < 0 || nr >= snapshot.length ||
                        nc < 0 || nc >= snapshot[0].length) {
                    continue;
                }

                if (image[nr][nc] == newColor) {
                    return true;
                }
            }

            return false;
        }

        private static int[][] copy(int[][] image) {

            int[][] result = new int[image.length][];

            for (int i = 0; i < image.length; i++) {
                result[i] = Arrays.copyOf(image[i], image[i].length);
            }

            return result;
        }
    }

    /*
     * ------------------------------------------------------------
     * Improved
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Perform a graph traversal using an explicit queue (BFS).
     *
     * Instead of repeatedly scanning the matrix, only cells that
     * belong to the connected component are explored.
     *
     * Invariant
     * ---------
     * Every cell inside the queue:
     *
     * • belongs to the original connected component
     * • has already been recolored
     * • will expand exactly once
     *
     * Improvement
     * -----------
     * Every reachable pixel is processed exactly once.
     *
     * Complexity
     * ----------
     * Time:
     * O(m × n)
     *
     * Space:
     * O(m × n)
     *
     * Interview Usefulness
     * --------------------
     * Useful when recursion depth may overflow the stack.
     */

    static class Improved {

        private static final int[][] DIRECTIONS = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        public int[][] floodFill(int[][] image,
                                 int sr,
                                 int sc,
                                 int newColor) {

            int originalColor = image[sr][sc];

            // 🟢 Nothing to change.
            if (originalColor == newColor) {
                return image;
            }

            java.util.ArrayDeque<int[]> queue = new java.util.ArrayDeque<>();

            // 🟢 Recolor immediately so the cell becomes visited.
            image[sr][sc] = newColor;
            queue.offer(new int[]{sr, sc});

            while (!queue.isEmpty()) {

                int[] current = queue.poll();

                int row = current[0];
                int col = current[1];

                for (int[] direction : DIRECTIONS) {

                    int nextRow = row + direction[0];
                    int nextCol = col + direction[1];

                    if (nextRow < 0 ||
                            nextRow >= image.length ||
                            nextCol < 0 ||
                            nextCol >= image[0].length) {
                        continue;
                    }

                    // 🟢 Only original-color neighbors remain
                    // inside the search space.
                    if (image[nextRow][nextCol] != originalColor) {
                        continue;
                    }

                    // 🟢 Immediate recoloring prevents duplicates.
                    image[nextRow][nextCol] = newColor;

                    queue.offer(new int[]{nextRow, nextCol});
                }
            }

            return image;
        }
    }

    /*
     * ------------------------------------------------------------
     * Optimal (Interview Preferred)
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Recursive Depth First Search.
     *
     * The image itself stores visitation state.
     *
     * No separate visited[][] array is needed.
     *
     * ------------------------------------------------------------
     * Core Invariant
     * ------------------------------------------------------------
     *
     * Enter dfs(r,c)
     *
     * =>
     *
     * (r,c) belongs to the original connected component.
     *
     * Exit dfs(r,c)
     *
     * =>
     *
     * Every reachable original-color cell from (r,c)
     * has already been recolored.
     *
     * ------------------------------------------------------------
     * Correctness
     * ------------------------------------------------------------
     *
     * Every valid cell is recolored once.
     *
     * Every recolored cell becomes permanently ineligible for
     * future recursion because it no longer equals originalColor.
     *
     * Therefore:
     *
     * • no duplicate processing
     * • no infinite recursion
     * • complete coverage
     *
     * ------------------------------------------------------------
     * Complexity
     * ------------------------------------------------------------
     *
     * Time:
     * O(m × n)
     *
     * Space:
     * O(m × n)
     *
     * (worst-case recursion depth)
     *
     * ------------------------------------------------------------
     * Interview Usefulness
     * ------------------------------------------------------------
     *
     * This is the canonical implementation expected in interviews.
     */

    static class Optimal {

        public int[][] floodFill(int[][] image,
                                 int sr,
                                 int sc,
                                 int newColor) {

            int originalColor = image[sr][sc];

            // 🟢 If colors already match, visitation cannot
            // be distinguished. Return immediately.
            if (originalColor == newColor) {
                return image;
            }

            dfs(image, sr, sc, originalColor, newColor);

            return image;
        }

        private void dfs(int[][] image,
                         int row,
                         int col,
                         int originalColor,
                         int newColor) {

            // 🟢 Outside search space.
            if (row < 0 ||
                    row >= image.length ||
                    col < 0 ||
                    col >= image[0].length) {
                return;
            }

            // 🟢 Component membership check.
            if (image[row][col] != originalColor) {
                return;
            }

            // 🟢 Recolor immediately.
            // This is simultaneously the visitation marker.
            image[row][col] = newColor;

            // 🟢 Explore every remaining candidate.
            dfs(image, row + 1, col, originalColor, newColor);

            dfs(image, row - 1, col, originalColor, newColor);

            dfs(image, row, col + 1, originalColor, newColor);

            dfs(image, row, col - 1, originalColor, newColor);
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Pattern
 * -------
 * Grid DFS on a connected component.
 *
 * Invariant
 * ---------
 * Every recursive call starts on a cell that belongs to the
 * original connected component.
 *
 * Before expanding, the current cell is recolored.
 *
 * Therefore it cannot be revisited.
 *
 * Search Space
 * ------------
 * Only cells whose value equals originalColor.
 *
 * Discard Rule
 * ------------
 * Immediately reject:
 *
 * • outside matrix
 * • different color
 * • already recolored
 *
 * Correctness
 * -----------
 * Every valid neighbor is explored exactly once.
 *
 * Every invalid neighbor is permanently discarded.
 *
 * Termination
 * -----------
 * Each recursive call permanently removes one cell from the
 * remaining search space.
 *
 * Since the component is finite, recursion must finish.
 *
 * In-place Feasibility
 * --------------------
 * Yes.
 *
 * The image itself stores visitation state.
 *
 * Streaming Feasibility
 * ---------------------
 * No.
 *
 * Random access to neighboring cells is required.
 *
 * When NOT To Use
 * ---------------
 * Weighted graph problems.
 *
 * Shortest-path problems.
 *
 * Dynamic programming.
 */

/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 * Connected region inside a matrix.
 *
 * Invariant
 * ---------
 * Recolored means visited.
 *
 * Search Target
 * -------------
 * Entire connected component of originalColor.
 *
 * Discard Rule
 * ------------
 * Outside grid.
 *
 * Wrong color.
 *
 * Already recolored.
 *
 * Common Trap
 * -----------
 * Forgetting:
 *
 * oldColor == newColor
 *
 * Edge Cases
 * ----------
 * Single cell.
 *
 * Entire matrix.
 *
 * One-row matrix.
 *
 * One-column matrix.
 *
 * Component size one.
 *
 * One-liner
 * ---------
 * DFS until the original color disappears.
 *
 * Re-derivation Cue
 * -----------------
 * Paint first.
 *
 * Then spread.
 */

/*
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * 1.
 * Replace DFS with BFS.
 *
 * Invariant is identical.
 *
 * Only traversal order changes.
 *
 * ------------------------------------------------------------
 *
 * 2.
 * Eight-direction connectivity.
 *
 * Add four diagonal directions.
 *
 * Component definition changes.
 *
 * Invariant remains unchanged.
 *
 * ------------------------------------------------------------
 *
 * 3.
 * Separate visited[][]
 *
 * Valid but unnecessary.
 *
 * Recoloring already stores visitation.
 *
 * ------------------------------------------------------------
 *
 * 4.
 * Immutable image.
 *
 * Copy matrix first.
 *
 * Visited array becomes necessary.
 *
 * ------------------------------------------------------------
 *
 * Pattern Break
 * -------------
 *
 * If movement has weights or costs,
 * DFS/BFS Flood Fill is no longer appropriate.
 *
 * Dijkstra or A* becomes necessary.
 */

    /*
     * ============================================================
     * 🧠 MASTERY CHECKLIST
     * ============================================================
     *
     * □ What is the invariant?
     *   Every reachable cell having originalColor is recolored
     *   exactly once.
     *
     * □ What is the search target?
     *   The connected component containing (sr, sc).
     *
     * □ What is the discard rule?
     *   Reject cells that are:
     *   - outside the grid
     *   - different from originalColor
     *   - already recolored
     *
     * □ Why is immediate recoloring required?
     *   It simultaneously updates the answer and marks the cell as
     *   visited, preventing cycles.
     *
     * □ Why must oldColor == newColor be handled first?
     *   Otherwise no state changes occur, so recursion repeatedly
     *   revisits the same cells and never terminates.
     *
     * □ Why does recursion terminate?
     *   Every successful recursive call permanently removes one cell
     *   from the remaining search space.
     *
     * □ Why does the naive scan fail?
     *   Equality alone does not define the answer.
     *   Connectivity defines the answer.
     *
     * □ Which edge cases should you remember?
     *   - already same color
     *   - single cell
     *   - entire image
     *   - disconnected equal colors
     *   - one row
     *   - one column
     *
     * □ Debugging readiness?
     *   Check:
     *   1. boundary condition
     *   2. color comparison
     *   3. early exit
     *   4. recolor before recursion
     *
     * □ Variant readiness?
     *   Can switch between DFS and BFS without changing the invariant.
     *
     * □ Pattern boundary?
     *   Flood Fill solves connected-component traversal,
     *   not weighted shortest-path problems.
     */

    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    private static int[][] copy(int[][] image) {

        int[][] result = new int[image.length][];

        for (int i = 0; i < image.length; i++) {
            result[i] = Arrays.copyOf(image[i], image[i].length);
        }

        return result;
    }

    private static void assertMatrixEquals(int[][] expected,
                                           int[][] actual) {

        assert expected.length == actual.length
                : "Row count mismatch.";

        for (int i = 0; i < expected.length; i++) {

            assert Arrays.equals(expected[i], actual[i])
                    : "Mismatch at row " + i +
                    "\nExpected: " + Arrays.toString(expected[i]) +
                    "\nActual:   " + Arrays.toString(actual[i]);
        }
    }

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        // --------------------------------------------------------
        // Representative LeetCode example.
        // Entire connected component should change.
        // --------------------------------------------------------
        {
            int[][] image = {
                    {1, 1, 1},
                    {1, 1, 0},
                    {1, 0, 1}
            };

            int[][] expected = {
                    {2, 2, 2},
                    {2, 2, 0},
                    {2, 0, 1}
            };

            assertMatrixEquals(
                    expected,
                    solver.floodFill(copy(image), 1, 1, 2)
            );
        }

        // --------------------------------------------------------
        // Early-exit case.
        // New color already equals original color.
        // --------------------------------------------------------
        {
            int[][] image = {
                    {0, 0},
                    {0, 0}
            };

            int[][] expected = {
                    {0, 0},
                    {0, 0}
            };

            assertMatrixEquals(
                    expected,
                    solver.floodFill(copy(image), 0, 0, 0)
            );
        }

        // --------------------------------------------------------
        // Single-cell image.
        // --------------------------------------------------------
        {
            int[][] image = {
                    {5}
            };

            int[][] expected = {
                    {9}
            };

            assertMatrixEquals(
                    expected,
                    solver.floodFill(copy(image), 0, 0, 9)
            );
        }

        // --------------------------------------------------------
        // Entire matrix belongs to one component.
        // --------------------------------------------------------
        {
            int[][] image = {
                    {3, 3},
                    {3, 3}
            };

            int[][] expected = {
                    {7, 7},
                    {7, 7}
            };

            assertMatrixEquals(
                    expected,
                    solver.floodFill(copy(image), 0, 1, 7)
            );
        }

        // --------------------------------------------------------
        // Disconnected equal colors must NOT be recolored.
        // --------------------------------------------------------
        {
            int[][] image = {
                    {1, 0, 1},
                    {0, 0, 0},
                    {1, 0, 1}
            };

            int[][] expected = {
                    {2, 0, 1},
                    {0, 0, 0},
                    {1, 0, 1}
            };

            assertMatrixEquals(
                    expected,
                    solver.floodFill(copy(image), 0, 0, 2)
            );
        }

        // --------------------------------------------------------
        // One-row matrix.
        // --------------------------------------------------------
        {
            int[][] image = {
                    {4, 4, 4, 1}
            };

            int[][] expected = {
                    {8, 8, 8, 1}
            };

            assertMatrixEquals(
                    expected,
                    solver.floodFill(copy(image), 0, 1, 8)
            );
        }

        // --------------------------------------------------------
        // One-column matrix.
        // --------------------------------------------------------
        {
            int[][] image = {
                    {2},
                    {2},
                    {3},
                    {2}
            };

            int[][] expected = {
                    {5},
                    {5},
                    {3},
                    {2}
            };

            assertMatrixEquals(
                    expected,
                    solver.floodFill(copy(image), 0, 0, 5)
            );
        }

        // --------------------------------------------------------
        // Component size one.
        // Neighboring values differ.
        // --------------------------------------------------------
        {
            int[][] image = {
                    {1, 2},
                    {3, 4}
            };

            int[][] expected = {
                    {9, 2},
                    {3, 4}
            };

            assertMatrixEquals(
                    expected,
                    solver.floodFill(copy(image), 0, 0, 9)
            );
        }

        System.out.println("All Flood Fill tests passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/
