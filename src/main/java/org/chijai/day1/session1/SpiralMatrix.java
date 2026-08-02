package org.chijai.day1.session1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpiralMatrix {

    /*
     * ============================================================
     * 2. 📘 PRIMARY PROBLEM
     * ============================================================
     *
     * Title:
     * Spiral Matrix
     *
     * Difficulty:
     * Medium
     *
     * Tags:
     * Matrix
     * Simulation
     * Boundary Traversal
     *
     * Problem Description
     * -------------------
     * Given an m × n matrix, return every element exactly once in
     * clockwise spiral order.
     *
     * Spiral traversal begins from the top-left corner.
     *
     * Order:
     *
     * →
     * ↓
     * ←
     * ↑
     *
     * Repeat until every element has been visited.
     *
     * Constraints
     * -----------
     * 1 <= m, n <= 10
     * -100 <= matrix[i][j] <= 100
     *
     * Examples
     * --------
     *
     * Example 1
     *
     * 1 2 3
     * 4 5 6
     * 7 8 9
     *
     * Output
     *
     * [1,2,3,6,9,8,7,4,5]
     *
     * Example 2
     *
     * 1  2  3  4
     * 5  6  7  8
     * 9 10 11 12
     *
     * Output
     *
     * [1,2,3,4,8,12,11,10,9,5,6,7]
     *
     * Official Link
     *
     * https://leetcode.com/problems/spiral-matrix/
     */

    /*
     * ============================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern
     * -------
     * Shrinking Boundary Traversal
     *
     * Archetype
     * ---------
     * Maintain the remaining unexplored rectangle.
     *
     * Core Invariant
     * --------------
     * At every iteration:
     *
     * top
     * bottom
     * left
     * right
     *
     * describe the smallest rectangle containing every unvisited
     * cell.
     *
     * Everything outside this rectangle has already been output
     * exactly once.
     *
     * Why It Works
     * ------------
     * Every spiral layer consumes one outer rectangle.
     *
     * After finishing four sides, that layer is complete.
     *
     * Shrinking all four boundaries reveals the next layer.
     *
     * Recognition Signals
     * -------------------
     * Entire outer layer removed each iteration.
     *
     * Clockwise or anticlockwise traversal.
     *
     * Matrix dimensions stay fixed.
     *
     * Need every element exactly once.
     *
     * When To Use
     * -----------
     * Spiral traversal.
     *
     * Onion layer processing.
     *
     * Matrix border stripping.
     *
     * Ring-by-ring simulation.
     *
     * When NOT To Use
     * ---------------
     * BFS exploration.
     *
     * Shortest path.
     *
     * Dynamic programming over neighbors.
     *
     * Unknown traversal order.
     *
     * Comparison
     * ----------
     * Boundary Traversal
     *     Rectangle shrinks.
     *
     * Two Pointers
     *     Usually one-dimensional.
     *
     * DFS
     *     Uses recursive exploration instead of deterministic
     *     geometric layers.
     *
     * Simulation with visited[]
     *     Uses extra memory.
     *
     * Boundary traversal
     *     Uses O(1) auxiliary space.
     */

    /*
     * ============================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * Mental Model
     * ------------
     * Imagine peeling an onion.
     *
     * Every iteration removes exactly one outer layer.
     *
     * The remaining inner rectangle becomes the next problem.
     *
     * The matrix itself never changes.
     *
     * Only the boundaries move.
     *
     * ----------------------------
     * Primary Invariant
     * ----------------------------
     *
     * top
     *
     * ...
     *
     * bottom
     *
     * left              right
     *
     * Everything:
     *
     * above top
     * below bottom
     * left of left
     * right of right
     *
     * has already been processed.
     *
     * Every unvisited element lies inside:
     *
     * rows    [top, bottom]
     * columns [left, right]
     *
     * ----------------------------
     * Variable Meaning
     * ----------------------------
     *
     * top
     * First remaining row.
     *
     * bottom
     * Last remaining row.
     *
     * left
     * First remaining column.
     *
     * right
     * Last remaining column.
     *
     * result
     * Spiral traversal collected so far.
     *
     * ----------------------------
     * Allowed Moves
     * ----------------------------
     *
     * Traverse top row.
     *
     * Increment top.
     *
     * Traverse right column.
     *
     * Decrement right.
     *
     * Traverse bottom row.
     *
     * Decrement bottom.
     *
     * Traverse left column.
     *
     * Increment left.
     *
     * Every boundary moves inward exactly once.
     *
     * ----------------------------
     * Forbidden Moves
     * ----------------------------
     *
     * Never traverse a side after its boundary crossed another.
     *
     * Never visit corners twice.
     *
     * Never shrink before finishing that side.
     *
     * Never forget boundary guards before bottom and left
     * traversals.
     *
     * ----------------------------
     * Why Boundary Guards Exist
     * ----------------------------
     *
     * Suppose one row remains.
     *
     * Top traversal already visits every element.
     *
     * Bottom traversal would revisit them.
     *
     * Therefore:
     *
     * if (top <= bottom)
     *
     * must be checked.
     *
     * Similarly,
     *
     * if (left <= right)
     *
     * prevents duplicate traversal for a single remaining column.
     *
     * ----------------------------
     * Termination
     * ----------------------------
     *
     * Eventually
     *
     * top > bottom
     *
     * or
     *
     * left > right
     *
     * meaning no unexplored rectangle exists.
     *
     * ----------------------------
     * Correctness Intuition
     * ----------------------------
     *
     * Each iteration removes exactly one outer ring.
     *
     * Rings never overlap.
     *
     * Rings cover the entire matrix.
     *
     * Therefore every cell is produced exactly once.
     *
     * ----------------------------
     * Why Naive Solutions Fail
     * ----------------------------
     *
     * Direction simulation with visited[] works,
     * but requires extra memory.
     *
     * Forgetting visited checks causes cycles.
     *
     * Forgetting boundary guards duplicates middle rows or columns.
     */

    /*
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * Mistake 1
     * ---------
     * Always execute all four traversals.
     *
     * Looks correct because rectangles usually have four sides.
     *
     * Violated Invariant
     * ------------------
     * Remaining rectangle may already be empty.
     *
     * Counterexample
     *
     * 1 2 3
     *
     * Top traversal outputs:
     *
     * 1 2 3
     *
     * Bottom traversal repeats:
     *
     * 3 2 1
     *
     * Mistake 2
     * ---------
     * Shrink boundaries before finishing current side.
     *
     * Violated Invariant
     * ------------------
     * Boundary no longer represents current rectangle.
     *
     * Mistake 3
     * ---------
     * Forget left-column guard.
     *
     * Counterexample
     *
     * 1
     * 2
     * 3
     *
     * Left traversal repeats the only column.
     *
     * Mistake 4
     * ---------
     * Incorrect loop condition.
     *
     * Using OR instead of AND.
     *
     * Search space becomes invalid.
     *
     * Access outside remaining rectangle.
     *
     * Interview Trap
     * --------------
     * Interviewers frequently ask:
     *
     * "Why are there exactly two boundary checks?"
     *
     * Correct answer:
     *
     * Top and right traversals always consume fresh edges.
     *
     * Bottom and left may disappear after shrinking,
     * therefore require explicit validity checks.
     */

    /*
     * ============================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Mechanical typing order
     *
     * 1.
     * Create answer list.
     *
     * 2.
     * Handle empty matrix.
     *
     * 3.
     * Initialize:
     *
     * top
     * bottom
     * left
     * right
     *
     * 4.
     * while rectangle exists
     *
     * 5.
     * Traverse top row.
     *
     * 6.
     * top++
     *
     * 7.
     * Traverse right column.
     *
     * 8.
     * right--
     *
     * 9.
     * if (top <= bottom)
     * traverse bottom row.
     *
     * 10.
     * bottom--
     *
     * 11.
     * if (left <= right)
     * traverse left column.
     *
     * 12.
     * left++
     *
     * 13.
     * Return answer.
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * initialize boundaries
     *
     * while rectangle exists
     *     traverse top
     *     shrink top
     *     traverse right
     *     shrink right
     *     if valid
     *         traverse bottom
     *         shrink bottom
     *     if valid
     *         traverse left
     *         shrink left
     *
     * return answer
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
     * Simulate movement using direction arrays and a visited matrix.
     *
     * Invariant
     * ---------
     * Never revisit a visited cell.
     *
     * Limitation
     * ----------
     * Extra O(mn) memory.
     *
     * Complexity
     * ----------
     * Time  : O(mn)
     * Space : O(mn)
     *
     * Interview Usefulness
     * --------------------
     * Good starting point.
     * Usually improved into boundary traversal.
     */

    static class BruteForce {

        public List<Integer> spiralOrder(int[][] matrix) {

            List<Integer> answer = new ArrayList<>();

            if (matrix == null || matrix.length == 0) {
                return answer;
            }

            int rows = matrix.length;
            int cols = matrix[0].length;

            boolean[][] visited = new boolean[rows][cols];

            int[] dr = {0, 1, 0, -1};
            int[] dc = {1, 0, -1, 0};

            int direction = 0;
            int r = 0;
            int c = 0;

            for (int count = 0; count < rows * cols; count++) {

                answer.add(matrix[r][c]);
                visited[r][c] = true;

                int nr = r + dr[direction];
                int nc = c + dc[direction];

                if (nr < 0
                        || nr >= rows
                        || nc < 0
                        || nc >= cols
                        || visited[nr][nc]) {

                    direction = (direction + 1) % 4;

                    nr = r + dr[direction];
                    nc = c + dc[direction];
                }

                r = nr;
                c = nc;
            }

            return answer;
        }
    }

/*
 * ------------------------------------------------------------
 * Improved
 * ------------------------------------------------------------
 *
 * Idea
 * ----
 * Remove visited[][].
 *
 * Maintain shrinking rectangle.
 *
 * Invariant
 * ---------
 * Remaining search space equals current rectangle.
 *
 * Improvement
 * -----------
 * O(1) auxiliary space.
 *
 * Complexity
 * ----------
 * Time  : O(mn)
 * Space : O(1)
 */
static class Improved {

    public List<Integer> spiralOrder(int[][] matrix) {

        List<Integer> answer = new ArrayList<>();

        if (matrix == null || matrix.length == 0) {
            return answer;
        }

        int top = 0;
        int bottom = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;

        while (top <= bottom && left <= right) {

            for (int col = left; col <= right; col++) {
                answer.add(matrix[top][col]);
            }
            top++;

            for (int row = top; row <= bottom; row++) {
                answer.add(matrix[row][right]);
            }
            right--;

            if (top <= bottom) {
                for (int col = right; col >= left; col--) {
                    answer.add(matrix[bottom][col]);
                }
                bottom--;
            }

            if (left <= right) {
                for (int row = bottom; row >= top; row--) {
                    answer.add(matrix[row][left]);
                }
                left++;
            }
        }

        return answer;
    }
}

    /*
     * ------------------------------------------------------------
     * Optimal (Interview Preferred)
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Represent the unexplored region using four boundaries.
     *
     * Every iteration removes one complete outer ring.
     *
     * 🟢 Invariant
     * ------------
     * The remaining search space is exactly:
     *
     * rows    [top, bottom]
     * columns [left, right]
     *
     * Everything outside this rectangle has already been emitted
     * exactly once.
     *
     * Correctness
     * -----------
     * Every boundary moves inward exactly once.
     *
     * No ring overlaps another.
     *
     * The guards prevent duplicate traversal when only one row or
     * one column remains.
     *
     * Complexity
     * ----------
     * Time  : O(m × n)
     * Space : O(1) auxiliary
     *
     * Interview Usefulness
     * --------------------
     * This is the expected solution.
     */

    static class Optimal {

        public List<Integer> spiralOrder(int[][] matrix) {

            List<Integer> answer = new ArrayList<>();

            // Invariant: empty input has no search space.
            if (matrix == null || matrix.length == 0) {
                return answer;
            }

            int top = 0;
            int bottom = matrix.length - 1;

            int left = 0;
            int right = matrix[0].length - 1;

            // Invariant: every unvisited element lies inside
            // the current rectangle.
            while (top <= bottom && left <= right) {

                // Traverse the current top boundary.
                for (int col = left; col <= right; col++) {
                    answer.add(matrix[top][col]);
                }

                // Discard the processed top edge.
                top++;

                // Traverse the current right boundary.
                for (int row = top; row <= bottom; row++) {
                    answer.add(matrix[row][right]);
                }

                // Right edge has been consumed.
                right--;

                // Guard prevents revisiting a single remaining row.
                if (top <= bottom) {

                    // Traverse the current bottom boundary.
                    for (int col = right; col >= left; col--) {
                        answer.add(matrix[bottom][col]);
                    }

                    // Bottom edge removed from search space.
                    bottom--;
                }

                // Guard prevents revisiting a single remaining column.
                if (left <= right) {

                    // Traverse the current left boundary.
                    for (int row = bottom; row >= top; row--) {
                        answer.add(matrix[row][left]);
                    }

                    // Left edge removed.
                    left++;
                }
            }

            return answer;
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Pattern
 * -------
 * Shrinking Boundary Traversal.
 *
 * Invariant
 * ---------
 * Four boundaries always describe the smallest rectangle that
 * still contains every unvisited cell.
 *
 * Search Space
 * ------------
 * rows    [top, bottom]
 * columns [left, right]
 *
 * Discard Rule
 * ------------
 * After finishing one side, that side can never contribute to
 * future answers.
 *
 * Therefore move its corresponding boundary inward.
 *
 * Correctness
 * -----------
 * Each iteration removes one complete ring.
 *
 * Rings are disjoint.
 *
 * Together they partition the matrix.
 *
 * Hence every cell appears exactly once.
 *
 * Termination
 * -----------
 * The algorithm stops when the remaining rectangle becomes
 * empty.
 *
 * In-place Feasibility
 * --------------------
 * Yes.
 *
 * The matrix is never modified.
 *
 * Only four integers are maintained.
 *
 * Streaming Feasibility
 * ---------------------
 * Yes.
 *
 * Instead of storing values, they may be emitted directly to a
 * consumer.
 *
 * When NOT To Use
 * ---------------
 * Problems requiring graph exploration,
 * shortest paths,
 * arbitrary movement,
 * or revisiting cells.
 */

/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 * Spiral traversal.
 *
 * Invariant
 * ---------
 * Remaining rectangle equals remaining search space.
 *
 * Search Target
 * -------------
 * Emit every boundary exactly once.
 *
 * Discard Rule
 * ------------
 * Finish one side.
 *
 * Move that boundary inward.
 *
 * Common Trap
 * -----------
 * Forgetting the bottom and left validity checks.
 *
 * Edge Cases
 * ----------
 * Empty matrix.
 *
 * One row.
 *
 * One column.
 *
 * Rectangle.
 *
 * Square.
 *
 * One-liner
 * ---------
 * Peel one rectangle at a time.
 *
 * Re-derivation Cue
 * -----------------
 * Top → Right → Bottom → Left
 *
 * Shrink after every completed side.
 */

/*
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variant
 * -------
 * Anti-clockwise spiral.
 *
 * Reasoning Change
 * ----------------
 * Change traversal order while preserving the shrinking
 * rectangle invariant.
 *
 * Variant
 * -------
 * Spiral Matrix II.
 *
 * Reasoning Change
 * ----------------
 * Fill values instead of reading values.
 *
 * The invariant remains identical.
 *
 * Variant
 * -------
 * Spiral from center.
 *
 * Pattern Break
 * -------------
 * Boundaries alone are insufficient because expansion replaces
 * shrinking.
 *
 * Variant
 * -------
 * Jagged arrays.
 *
 * Pattern Break
 * -------------
 * Rectangle invariant no longer holds.
 *
 * Boundary traversal is no longer mechanically valid.
 */

/*
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * □ I know the invariant.
 *
 * □ I know the search space.
 *
 * □ I know why four boundaries are sufficient.
 *
 * □ I know why only two boundary guards are required.
 *
 * □ I can justify correctness.
 *
 * □ I know termination.
 *
 * □ I know why naive simulation needs extra memory.
 *
 * □ I can debug duplicated center elements immediately.
 *
 * □ I can adapt this invariant to Spiral Matrix II.
 *
 * □ I know where this pattern stops being applicable.
 */
public static void main(String[] args) {

    Optimal solver = new Optimal();

    /*
     * Happy Path
     * ----------
     * Standard odd-sized square.
     */
    int[][] matrix1 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
    };

    assert solver.spiralOrder(matrix1).equals(
            Arrays.asList(1, 2, 3, 6, 9, 8, 7, 4, 5)
    );

    /*
     * Happy Path
     * ----------
     * Rectangular matrix.
     */
    int[][] matrix2 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12}
    };

    assert solver.spiralOrder(matrix2).equals(
            Arrays.asList(
                    1, 2, 3, 4,
                    8, 12,
                    11, 10, 9,
                    5, 6, 7
            )
    );

    /*
     * Edge Case
     * ---------
     * Single element.
     */
    int[][] matrix3 = {
            {42}
    };

    assert solver.spiralOrder(matrix3).equals(
            Arrays.asList(42)
    );

    /*
     * Edge Case
     * ---------
     * Single row.
     *
     * Verifies that the bottom traversal is skipped.
     */
    int[][] matrix4 = {
            {1, 2, 3, 4, 5}
    };

    assert solver.spiralOrder(matrix4).equals(
            Arrays.asList(1, 2, 3, 4, 5)
    );

    /*
     * Edge Case
     * ---------
     * Single column.
     *
     * Verifies that the left traversal is skipped.
     */
    int[][] matrix5 = {
            {1},
            {2},
            {3},
            {4}
    };

    assert solver.spiralOrder(matrix5).equals(
            Arrays.asList(1, 2, 3, 4)
    );

    /*
     * Boundary Condition
     * ------------------
     * Two rows.
     */
    int[][] matrix6 = {
            {1, 2, 3},
            {4, 5, 6}
    };

    assert solver.spiralOrder(matrix6).equals(
            Arrays.asList(1, 2, 3, 6, 5, 4)
    );

    /*
     * Boundary Condition
     * ------------------
     * Two columns.
     */
    int[][] matrix7 = {
            {1, 2},
            {3, 4},
            {5, 6}
    };

    assert solver.spiralOrder(matrix7).equals(
            Arrays.asList(1, 2, 4, 6, 5, 3)
    );

    /*
     * Interview Trap
     * --------------
     * Duplicate-center bugs appear here if boundary guards
     * are missing.
     */
    int[][] matrix8 = {
            {1, 2},
            {3, 4}
    };

    assert solver.spiralOrder(matrix8).equals(
            Arrays.asList(1, 2, 4, 3)
    );

    /*
     * Interview Trap
     * --------------
     * Center element must appear exactly once.
     */
    int[][] matrix9 = {
            {1, 2, 3},
            {8, 9, 4},
            {7, 6, 5}
    };

    assert solver.spiralOrder(matrix9).equals(
            Arrays.asList(
                    1, 2, 3,
                    4, 5, 6,
                    7, 8, 9
            )
    );

    /*
     * Boundary Condition
     * ------------------
     * Empty matrix.
     */
    int[][] matrix10 = new int[0][0];

    assert solver.spiralOrder(matrix10).isEmpty();

    System.out.println("All Spiral Matrix tests passed.");
}
}