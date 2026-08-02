package org.chijai.day8.graph.session1;

import java.util.Arrays;

public class ColoringABorder {

    /*
     * ============================================================
     * 2. 📘 PRIMARY PROBLEM
     * ============================================================
     *
     * Title:
     * Coloring A Border
     *
     * Difficulty:
     * Medium
     *
     * Tags:
     * DFS
     * Flood Fill
     * Graph Traversal
     * Matrix
     * Connected Component
     *
     * Official LeetCode:
     * https://leetcode.com/problems/coloring-a-border/
     *
     * Problem
     * -------
     * You are given an m x n grid of colors.
     *
     * Starting from (row, col), consider the connected component formed
     * using only 4-directional neighbors having the same color.
     *
     * A cell belongs to the BORDER of this connected component if:
     *
     * 1. it lies on the outer boundary of the grid
     * OR
     * 2. at least one of its four neighbors is NOT inside the component.
     *
     * Paint ONLY the border cells using the new color.
     *
     * Interior cells must remain unchanged.
     *
     * Return the modified grid.
     *
     * Constraints
     * ----------
     * 1 <= m, n <= 50
     * 1 <= grid[i][j], color <= 1000
     *
     * Examples
     * --------
     *
     * Example 1
     *
     * grid =
     * 1 1
     * 1 2
     *
     * start = (0,0)
     * newColor = 3
     *
     * Component:
     *
     * 1 1
     * 1
     *
     * Every component cell touches boundary.
     *
     * Result
     *
     * 3 3
     * 3 2
     *
     * ----------------------------------------------------
     *
     * Example 2
     *
     * grid =
     * 1 2 2
     * 2 3 2
     *
     * start=(0,1)
     *
     * Component:
     *
     * 2 2
     *   2
     *
     * Every component cell is border.
     *
     * ----------------------------------------------------
     *
     * Example 3
     *
     * 1 1 1
     * 1 1 1
     * 1 1 1
     *
     * start=(1,1)
     *
     * Center remains unchanged because it is completely surrounded
     * by cells from the same component.
     *
     * Result
     *
     * 2 2 2
     * 2 1 2
     * 2 2 2
     *
     */

    /*
     * ============================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern
     * -------
     * DFS Connected Component + Deferred Border Classification
     *
     * Archetype
     * ---------
     * Flood Fill with Post-order Decision
     *
     * Core Invariant
     * --------------
     * Every visited component cell is temporarily marked.
     *
     * After exploring every reachable neighbor,
     * we have enough information to decide whether
     * this cell is
     *
     *     border
     * or
     *     interior.
     *
     * Why It Works
     * ------------
     * During traversal we cannot permanently recolor immediately,
     * because neighbors still need the original component identity.
     *
     * Therefore:
     *
     * original color
     *      ↓
     * temporary mark
     *      ↓
     * classify
     *      ↓
     * final recolor
     *
     * Recognition Signals
     * -------------------
     * Look for:
     *
     * • connected component
     * • paint only boundary
     * • preserve interior
     * • traversal before modification
     *
     * When To Use
     * -----------
     * Whenever:
     *
     * component must be discovered
     * and
     * modification depends on neighborhood information.
     *
     * When NOT To Use
     * ---------------
     * Not suitable if:
     *
     * • shortest path required
     * • weighted graph
     * • multiple simultaneous expansions
     * • dynamic connectivity
     *
     * Comparison
     * ----------
     *
     * Classic Flood Fill
     * ------------------
     * Paint every visited cell.
     *
     * Coloring Border
     * ---------------
     * Paint only subset of visited cells.
     *
     * Number of Islands
     * -----------------
     * Count components.
     *
     * Coloring Border
     * ----------------
     * Preserve component while classifying border.
     */

    /*
     * ============================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * Mental Model
     * ------------
     * Imagine walking through one island.
     *
     * Every time you step onto land,
     * you place a temporary flag.
     *
     * After exploring around you,
     * you ask:
     *
     * "Am I completely surrounded by my own island?"
     *
     * YES
     *     remove temporary flag
     *
     * NO
     *     keep temporary flag
     *
     * Later,
     * every remaining temporary flag becomes newColor.
     *
     * --------------------------------------------------------
     * Invariant 1
     * --------------------------------------------------------
     * Negative value means:
     *
     * visited
     * AND
     * belongs to original component.
     *
     * Therefore
     *
     * abs(value)==startColor
     *
     * still identifies component membership.
     *
     * --------------------------------------------------------
     * Invariant 2
     * --------------------------------------------------------
     * During DFS,
     * neighbor checks must ignore visitation state.
     *
     * Therefore always compare:
     *
     * abs(grid[x][y])
     *
     * instead of
     *
     * grid[x][y]
     *
     * --------------------------------------------------------
     * Invariant 3
     * --------------------------------------------------------
     * A cell is interior iff
     * every one of its four neighbors belongs
     * to the component.
     *
     * Otherwise it is border.
     *
     * --------------------------------------------------------
     * Invariant 4
     * --------------------------------------------------------
     * Boundary cells are automatically border.
     *
     * They never require neighbor verification.
     *
     * --------------------------------------------------------
     * Variable Meaning
     * --------------------------------------------------------
     *
     * startColor
     * ----------
     * Original component color.
     *
     * color
     * -----
     * Final border color.
     *
     * Negative value
     * --------------
     * Temporarily marked component cell.
     *
     * --------------------------------------------------------
     * Allowed State Transition
     * --------------------------------------------------------
     *
     * startColor
     *      ↓
     * -startColor
     *      ↓
     * startColor      (interior)
     *
     * OR
     *
     * -startColor
     *      ↓
     * newColor
     *
     * --------------------------------------------------------
     * Forbidden Transition
     * --------------------------------------------------------
     *
     * startColor
     *      ↓
     * newColor
     *
     * immediately during DFS.
     *
     * Doing so destroys component recognition.
     *
     * --------------------------------------------------------
     * Termination
     * --------------------------------------------------------
     *
     * DFS stops when
     *
     * • outside grid
     * • different color
     * • already visited
     *
     * Every component cell is visited exactly once.
     *
     * --------------------------------------------------------
     * Why Naive Solutions Fail
     * --------------------------------------------------------
     *
     * A naive DFS that immediately recolors border cells
     * changes neighborhood information.
     *
     * Subsequent recursive calls cannot distinguish:
     *
     * original color
     * versus
     * recolored border.
     *
     * The connected component becomes fragmented.
     */

    /*
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * Mistake 1
     * ---------
     * Recolor during traversal.
     *
     * Looks reasonable because border is already identified.
     *
     * Broken Invariant:
     * Original component identity disappears.
     *
     * --------------------------------------------------------
     *
     * Mistake 2
     * ---------
     * Compare neighbors without abs().
     *
     * Visited cells become negative.
     *
     * They falsely appear outside component.
     *
     * Broken Invariant:
     * Negative still represents same component.
     *
     * --------------------------------------------------------
     *
     * Mistake 3
     * ---------
     * Decide border before exploring neighbors.
     *
     * Neighbor may later become visited.
     *
     * Classification becomes inconsistent.
     *
     * --------------------------------------------------------
     *
     * Mistake 4
     * ---------
     * Forget grid boundary rule.
     *
     * Even if all existing neighbors match,
     * boundary cells remain border.
     *
     * --------------------------------------------------------
     *
     * Interview Trap
     * --------------
     * Why use negative marking?
     *
     * Because we need BOTH:
     *
     * visited information
     * AND
     * original color information
     *
     * simultaneously.
     */

    /*
     * ============================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Typing Order
     * ------------
     *
     * 1.
     * colorBorder(...)
     *
     * 2.
     * save startColor
     *
     * 3.
     * dfs(...)
     *
     * 4.
     * scan matrix
     *
     * 5.
     * convert remaining negative cells
     *
     * 6.
     * return grid
     *
     * DFS Skeleton
     * ------------
     *
     * boundary check
     *
     * wrong color check
     *
     * mark negative
     *
     * recurse 4 directions
     *
     * if boundary
     *     keep negative
     *
     * else
     *     inspect four neighbors
     *
     * if all belong to component
     *     restore original color
     *
     * otherwise
     *     remain negative
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * save startColor
     *
     * dfs(start)
     *
     * scan matrix
     *
     * negative → newColor
     *
     * return
     */

    /*
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    static class BruteForce {

        /*
         * Idea
         * ----
         * Discover component.
         * Store every cell.
         * Recheck every stored cell independently.
         *
         * Invariant
         * ---------
         * Component is fully known before classification.
         *
         * Limitation
         * ----------
         * Requires explicit component storage.
         *
         * Complexity
         * ----------
         * Time  : O(mn)
         * Space : O(mn)
         *
         * Interview Usefulness
         * --------------------
         * Good stepping stone.
         */
    }

    static class Improved {

        /*
         * Idea
         * ----
         * DFS plus visited array.
         *
         * Invariant
         * ---------
         * Visited tracked separately.
         *
         * Improvement
         * -----------
         * Cleaner than brute force.
         *
         * Complexity
         * ----------
         * Time  : O(mn)
         * Space : O(mn)
         */
    }

    static class Optimal {

        /*
         * Idea
         * ----
         * Encode visitation inside grid itself.
         *
         * Negative value simultaneously means:
         *
         * visited
         * +
         * original component.
         *
         * This removes the need for an extra visited matrix.
         */

        public int[][] colorBorder(int[][] grid, int row, int col, int color) {

            int startColor = grid[row][col];

            dfs(grid, row, col, startColor);

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {

                    // Invariant: only border cells remain negative.
                    if (grid[i][j] < 0) {
                        grid[i][j] = color;
                    }
                }
            }

            return grid;
        }

        private void dfs(int[][] grid, int r, int c, int startColor) {

            if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length) {
                return;
            }

            if (grid[r][c] != startColor) {
                return;
            }

            // Invariant: negative still identifies this component.
            grid[r][c] = -startColor;

            dfs(grid, r + 1, c, startColor);
            dfs(grid, r - 1, c, startColor);
            dfs(grid, r, c + 1, startColor);
            dfs(grid, r, c - 1, startColor);

            // Boundary cells are always border.
            if (r == 0 || r == grid.length - 1 || c == 0 || c == grid[0].length - 1) {
                return;
            }

            // Every neighbor must still belong to the original component.
            // abs() ignores temporary visitation marking.
            if (Math.abs(grid[r + 1][c]) == startColor
                    && Math.abs(grid[r - 1][c]) == startColor
                    && Math.abs(grid[r][c + 1]) == startColor
                    && Math.abs(grid[r][c - 1]) == startColor) {

                // Invariant: interior cells must preserve original color.
                grid[r][c] = startColor;
            }
        }

        /*
         * Correctness
         * -----------
         * Every reachable component cell is visited exactly once.
         *
         * Border cells remain negative because either:
         *   1. they touch the grid boundary, or
         *   2. at least one neighboring position is outside the component.
         *
         * Interior cells are restored back to startColor after all recursive
         * exploration confirms that every 4-directional neighbor belongs to
         * the same connected component.
         *
         * Therefore, after DFS:
         *
         *      negative  -> border
         *      positive  -> interior
         *
         * A final scan safely recolors only border cells.
         *
         * Complexity
         * ----------
         * Time  : O(m × n)
         * Space : O(m × n)
         *
         * Space Explanation
         * -----------------
         * The algorithm uses no visited matrix.
         * The auxiliary space comes only from recursion depth,
         * which can reach O(m × n) in the worst case.
         *
         * Interview Usefulness
         * --------------------
         * Excellent.
         *
         * Demonstrates:
         *
         * • DFS
         * • Connected Component
         * • In-place state encoding
         * • Deferred modification
         * • Post-order reasoning
         */
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Explain the Invariant
 * ---------------------
 *
 * I first discover the entire connected component.
 *
 * Instead of allocating a visited matrix,
 * I temporarily negate the color.
 *
 * A negative value means:
 *
 *      visited
 * AND
 *      still belongs to this component.
 *
 * Since abs(value) recovers the original color,
 * neighboring cells can still recognize each other.
 *
 * ------------------------------------------------------------
 *
 * Explain the Discard Rule
 * ------------------------
 *
 * There is no search-space pruning.
 *
 * Instead, every recursive call immediately terminates when:
 *
 * • outside grid
 * • different color
 * • already visited
 *
 * ------------------------------------------------------------
 *
 * Explain Border Classification
 * -----------------------------
 *
 * After recursion finishes,
 * I know whether every neighboring cell belongs to
 * the same connected component.
 *
 * If all four neighbors belong,
 * this cell is interior.
 *
 * Otherwise,
 * it is border.
 *
 * ------------------------------------------------------------
 *
 * Explain Correctness
 * -------------------
 *
 * Every connected component cell is visited exactly once.
 *
 * Only interior cells are restored.
 *
 * Therefore,
 * after DFS,
 * every remaining negative cell must be border.
 *
 * Recoloring negatives therefore recolors exactly the border.
 *
 * ------------------------------------------------------------
 *
 * Explain Termination
 * -------------------
 *
 * Each recursive call permanently marks one new component cell.
 *
 * Therefore recursion cannot revisit that state,
 * guaranteeing termination.
 *
 * ------------------------------------------------------------
 *
 * In-place Feasibility
 * --------------------
 *
 * Yes.
 *
 * Negative marking stores visitation information
 * inside the original matrix.
 *
 * ------------------------------------------------------------
 *
 * Streaming Feasibility
 * ---------------------
 *
 * No.
 *
 * Border classification depends on complete local neighborhood
 * information and recursive exploration.
 *
 * ------------------------------------------------------------
 *
 * When NOT To Use
 * ---------------
 *
 * Avoid this technique if:
 *
 * • matrix values cannot be temporarily modified
 * • negative values are meaningful input
 * • recursion depth risks stack overflow on huge grids
 * • graph is not represented by local neighbors
 */

/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 * Connected component where only boundary changes.
 *
 * Pattern
 * -------
 * DFS + In-place temporary marking.
 *
 * Invariant
 * ---------
 * Negative means:
 *
 * visited
 * +
 * same component.
 *
 * Search Target
 * -------------
 * Entire connected component.
 *
 * Classification Rule
 * -------------------
 * Interior iff
 * all four neighbors remain inside component.
 *
 * Border Rule
 * -----------
 * Grid edge
 * OR
 * neighbor outside component.
 *
 * Common Trap
 * -----------
 * Forgetting Math.abs().
 *
 * Edge Cases
 * ----------
 * • single cell
 * • whole grid
 * • already same target color
 * • one-row grid
 * • one-column grid
 *
 * One-liner
 * ---------
 * Mark first.
 * Classify later.
 * Paint last.
 *
 * Re-derivation Cue
 * -----------------
 * Ask:
 *
 * "Can neighbors still recognize me after I visit?"
 *
 * If yes,
 * the invariant is preserved.
 */

/*
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variant 1
 * ---------
 * Separate visited matrix.
 *
 * Reasoning
 * ---------
 * Original colors remain untouched.
 *
 * Preserves Invariant?
 * --------------------
 * Yes.
 *
 * Memory increases to O(mn).
 *
 * ------------------------------------------------------------
 *
 * Variant 2
 * ---------
 * BFS instead of DFS.
 *
 * Reasoning
 * ---------
 * Component discovery order changes,
 * but component definition does not.
 *
 * Preserves Invariant?
 * --------------------
 * Yes.
 *
 * ------------------------------------------------------------
 *
 * Variant 3
 * ---------
 * Store border cells in a list.
 *
 * Reasoning
 * ---------
 * Discover component first,
 * recolor afterward.
 *
 * Preserves Invariant?
 * --------------------
 * Yes.
 *
 * Uses additional memory.
 *
 * ------------------------------------------------------------
 *
 * Variant 4
 * ---------
 * Immediate recoloring.
 *
 * Preserves Invariant?
 * --------------------
 * No.
 *
 * Neighbor recognition is destroyed.
 *
 * ------------------------------------------------------------
 *
 * Variant 5
 * ---------
 * Compare neighbors directly instead of abs().
 *
 * Preserves Invariant?
 * --------------------
 * No.
 *
 * Visited cells falsely appear outside component.
 */

/*
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * □ I know the Pattern.
 *
 * □ I know why negative marking replaces visited[][].
 *
 * □ I know why abs() is mandatory.
 *
 * □ I know why recoloring is deferred.
 *
 * □ I know why boundary cells are automatically border.
 *
 * □ I know how an interior cell is detected.
 *
 * □ I know why recursion is post-order.
 *
 * □ I can derive correctness from the invariant.
 *
 * □ I can debug incorrect border classification.
 *
 * □ I know when BFS is an equivalent replacement.
 *
 * □ I know when this pattern should not be used.
 */

/*
 * ============================================================
 * ⚫ PATTERN MAPPING
 * ============================================================
 *
 * This problem belongs to the Connected Component family.
 *
 * ----------------------------------------------------------------
 * Flood Fill
 * ----------------------------------------------------------------
 * Goal
 *      Paint entire connected component.
 *
 * State
 *      Component membership.
 *
 * Transition
 *      Visit every same-colored neighbor.
 *
 * Difference
 *      No border classification.
 *
 * ----------------------------------------------------------------
 * Number of Islands
 * ----------------------------------------------------------------
 * Goal
 *      Count connected components.
 *
 * Difference
 *      Entire component is consumed.
 *
 * ----------------------------------------------------------------
 * Max Area of Island
 * ----------------------------------------------------------------
 * Goal
 *      Measure component size.
 *
 * Difference
 *      Aggregate information instead of recoloring.
 *
 * ----------------------------------------------------------------
 * Surrounded Regions
 * ----------------------------------------------------------------
 * Goal
 *      Preserve only components connected to the boundary.
 *
 * Difference
 *      Classification is at component level rather than cell level.
 *
 * ----------------------------------------------------------------
 * Coloring A Border
 * ----------------------------------------------------------------
 * Goal
 *      Preserve interior while recoloring only boundary cells.
 *
 * Distinguishing Idea
 * -------------------
 * Post-order border classification after complete exploration.
 */

/*
 * ============================================================
 * 🔍 DEBUGGING PLAYBOOK
 * ============================================================
 *
 * Symptom
 * -------
 * Entire component gets recolored.
 *
 * Likely Cause
 * ------------
 * Interior cells were never restored.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Interior cells incorrectly become border.
 *
 * Likely Cause
 * ------------
 * Neighbor comparison forgot Math.abs().
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * DFS stops too early.
 *
 * Likely Cause
 * ------------
 * Recursive call rejects negative cells
 * before they can participate in neighbor checks.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Infinite recursion.
 *
 * Likely Cause
 * ------------
 * Cell not marked before recursive expansion.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Boundary cells stay unchanged.
 *
 * Likely Cause
 * ------------
 * Final conversion from negative → newColor missing.
 *
 * ------------------------------------------------------------
 *
 * Mechanical Debug Checklist
 * --------------------------
 *
 * Step 1
 * Confirm marking occurs before recursion.
 *
 * Step 2
 * Confirm every recursive entry requires original color.
 *
 * Step 3
 * Confirm neighbor comparison uses abs().
 *
 * Step 4
 * Confirm interior restoration happens after recursion.
 *
 * Step 5
 * Confirm final matrix scan recolors negatives.
 */

/*
 * ============================================================
 * 📈 COMPLEXITY ANALYSIS
 * ============================================================
 *
 * Time
 * ----
 * DFS visits every component cell once.
 *
 * Final scan visits every grid cell once.
 *
 * Total:
 *
 * O(m × n)
 *
 * ------------------------------------------------------------
 *
 * Space
 * -----
 * Extra matrix:
 *
 * None.
 *
 * Recursive stack:
 *
 * Worst case:
 *
 * O(m × n)
 *
 * Best case:
 *
 * O(1)
 *
 * Average:
 *
 * Depends on component shape.
 */

/*
 * ============================================================
 * 🧩 EDGE CASE CATALOG
 * ============================================================
 *
 * Case 1
 * ------
 * Single cell grid.
 *
 * Border?
 *
 * Yes.
 *
 * ------------------------------------------------------------
 *
 * Case 2
 * ------
 * Entire grid same color.
 *
 * Only outer ring changes.
 *
 * Interior remains unchanged.
 *
 * ------------------------------------------------------------
 *
 * Case 3
 * ------
 * Component size = 1.
 *
 * Always border.
 *
 * ------------------------------------------------------------
 *
 * Case 4
 * ------
 * One row.
 *
 * Every component cell lies on boundary.
 *
 * ------------------------------------------------------------
 *
 * Case 5
 * ------
 * One column.
 *
 * Every component cell lies on boundary.
 *
 * ------------------------------------------------------------
 *
 * Case 6
 * ------
 * New color equals original color.
 *
 * Algorithm still works.
 *
 * Negative marking guarantees correct traversal.
 *
 * Final appearance is unchanged.
 */

/*
 * ============================================================
 * ⚡ IMPLEMENTATION RECONSTRUCTION DRILL
 * ============================================================
 *
 * Without looking at code, reconstruct mechanically:
 *
 * 1.
 * Save startColor.
 *
 * 2.
 * DFS.
 *
 * 3.
 * Reject:
 *      outside
 *      wrong color
 *
 * 4.
 * Negate current cell.
 *
 * 5.
 * Explore four directions.
 *
 * 6.
 * If boundary:
 *      keep negative.
 *
 * 7.
 * Else check:
 *
 * abs(down)
 * abs(up)
 * abs(right)
 * abs(left)
 *
 * 8.
 * If all match:
 *      restore startColor.
 *
 * 9.
 * Scan matrix.
 *
 * 10.
 * Convert remaining negatives to newColor.
 */

/*
 * ============================================================
 * 📝 INTERVIEW WHITEBOARD SUMMARY
 * ============================================================
 *
 * Pattern
 * -------
 * DFS + Deferred Classification
 *
 * State
 * -----
 * Negative value = visited component.
 *
 * Transition
 * ----------
 * Explore four same-colored neighbors.
 *
 * Border Test
 * -----------
 * Grid boundary
 * OR
 * missing component neighbor.
 *
 * Interior Test
 * -------------
 * All four neighbors belong to component.
 *
 * Final Pass
 * ----------
 * Remaining negatives become newColor.
 */

/*
 * ============================================================
 * 🧠 MEMORY ANCHOR
 * ============================================================
 *
 * Discover.
 *
 * Mark.
 *
 * Explore.
 *
 * Classify.
 *
 * Restore interiors.
 *
 * Paint borders.
 */

    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    private static void assertGridEquals(int[][] expected, int[][] actual) {
        assert expected.length == actual.length : "Row count mismatch.";

        for (int i = 0; i < expected.length; i++) {
            assert Arrays.equals(expected[i], actual[i])
                    : "Mismatch at row " + i
                    + "\nExpected: " + Arrays.toString(expected[i])
                    + "\nActual  : " + Arrays.toString(actual[i]);
        }
    }

    private static int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = Arrays.copyOf(grid[i], grid[i].length);
        }
        return copy;
    }

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        /*
         * Happy Path
         * ----------
         * Entire component lies on grid boundary.
         */
        {
            int[][] grid = {
                    {1, 1},
                    {1, 2}
            };

            int[][] expected = {
                    {3, 3},
                    {3, 2}
            };

            assertGridEquals(expected,
                    solver.colorBorder(copyGrid(grid), 0, 0, 3));
        }

        /*
         * Representative Example
         * ----------------------
         * Irregular connected component.
         */
        {
            int[][] grid = {
                    {1, 2, 2},
                    {2, 3, 2}
            };

            int[][] expected = {
                    {1, 3, 3},
                    {2, 3, 3}
            };

            assertGridEquals(expected,
                    solver.colorBorder(copyGrid(grid), 0, 1, 3));
        }

        /*
         * Interview Trap
         * --------------
         * Interior must remain unchanged.
         */
        {
            int[][] grid = {
                    {1, 1, 1},
                    {1, 1, 1},
                    {1, 1, 1}
            };

            int[][] expected = {
                    {2, 2, 2},
                    {2, 1, 2},
                    {2, 2, 2}
            };

            assertGridEquals(expected,
                    solver.colorBorder(copyGrid(grid), 1, 1, 2));
        }

        /*
         * Edge Case
         * ---------
         * Single cell grid.
         */
        {
            int[][] grid = {
                    {5}
            };

            int[][] expected = {
                    {9}
            };

            assertGridEquals(expected,
                    solver.colorBorder(copyGrid(grid), 0, 0, 9));
        }

        /*
         * Edge Case
         * ---------
         * Single row.
         * Every component cell is border.
         */
        {
            int[][] grid = {
                    {1, 1, 1, 2}
            };

            int[][] expected = {
                    {7, 7, 7, 2}
            };

            assertGridEquals(expected,
                    solver.colorBorder(copyGrid(grid), 0, 1, 7));
        }

        /*
         * Edge Case
         * ---------
         * Single column.
         */
        {
            int[][] grid = {
                    {4},
                    {4},
                    {4}
            };

            int[][] expected = {
                    {8},
                    {8},
                    {8}
            };

            assertGridEquals(expected,
                    solver.colorBorder(copyGrid(grid), 1, 0, 8));
        }

        /*
         * Boundary Verification
         * ---------------------
         * Component of size one.
         */
        {
            int[][] grid = {
                    {1, 2},
                    {2, 2}
            };

            int[][] expected = {
                    {9, 2},
                    {2, 2}
            };

            assertGridEquals(expected,
                    solver.colorBorder(copyGrid(grid), 0, 0, 9));
        }

        /*
         * Stability Check
         * ---------------
         * New color equals original color.
         * Logical result remains identical.
         */
        {
            int[][] grid = {
                    {3, 3},
                    {3, 4}
            };

            int[][] expected = {
                    {3, 3},
                    {3, 4}
            };

            assertGridEquals(expected,
                    solver.colorBorder(copyGrid(grid), 0, 0, 3));
        }

        /*
         * Interior Preservation
         * ---------------------
         * Larger solid block.
         */
        {
            int[][] grid = {
                    {1, 1, 1, 1},
                    {1, 1, 1, 1},
                    {1, 1, 1, 1},
                    {1, 1, 1, 1}
            };

            int[][] expected = {
                    {5, 5, 5, 5},
                    {5, 1, 1, 5},
                    {5, 1, 1, 5},
                    {5, 5, 5, 5}
            };

            assertGridEquals(expected,
                    solver.colorBorder(copyGrid(grid), 1, 1, 5));
        }

        /*
         * Disconnected Region
         * -------------------
         * Only the selected connected component changes.
         */
        {
            int[][] grid = {
                    {1, 1, 2},
                    {1, 2, 2},
                    {2, 2, 1}
            };

            int[][] expected = {
                    {7, 7, 2},
                    {7, 2, 2},
                    {2, 2, 1}
            };

            assertGridEquals(expected,
                    solver.colorBorder(copyGrid(grid), 0, 0, 7));
        }

        System.out.println("All assertions passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/