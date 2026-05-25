package org.chijai.day8.session1;

import java.util.*;

/*
 * =========================================================================================
 * Number Of Islands — Invariant First Algorithm Chapter
 * =========================================================================================
 *
 * 🔵 Core Theme:
 * Connected Component Discovery in a Grid using Flood Fill Traversal
 *
 * 🟢 Core Invariant:
 * Every land cell belonging to the same island must be visited exactly once
 * by a single traversal expansion.
 *
 * =========================================================================================
 */

public class Islands {

    /*
     * =====================================================================================
     * 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
     * =====================================================================================
     */

    /*
     * LeetCode Problem:
     * https://leetcode.com/problems/number-of-islands/
     *
     * Difficulty:
     * Medium
     *
     * Tags:
     * Array
     * Depth-First Search
     * Breadth-First Search
     * Union Find
     * Matrix
     *
     * =====================================================================================
     *
     * 200. Number of Islands
     *
     * Given an m x n 2D binary grid grid which represents a map of '1's (land)
     * and '0's (water), return the number of islands.
     *
     * An island is surrounded by water and is formed by connecting adjacent lands
     * horizontally or vertically.
     *
     * You may assume all four edges of the grid are all surrounded by water.
     *
     * =====================================================================================
     *
     * Example 1:
     *
     * Input: grid = [
     *   ["1","1","1","1","0"],
     *   ["1","1","0","1","0"],
     *   ["1","1","0","0","0"],
     *   ["0","0","0","0","0"]
     * ]
     *
     * Output: 1
     *
     * =====================================================================================
     *
     * Example 2:
     *
     * Input: grid = [
     *   ["1","1","0","0","0"],
     *   ["1","1","0","0","0"],
     *   ["0","0","1","0","0"],
     *   ["0","0","0","1","1"]
     * ]
     *
     * Output: 3
     *
     * =====================================================================================
     *
     * Constraints:
     *
     * m == grid.length
     * n == grid[i].length
     * 1 <= m, n <= 300
     * grid[i][j] is '0' or '1'.
     *
     * =====================================================================================
     */

    /*
     * =====================================================================================
     * 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
     * =====================================================================================
     */

    /*
     * 🔵 Pattern Name:
     * Flood Fill / Connected Component Traversal
     *
     * =====================================================================================
     *
     * 🔵 Problem Archetype:
     *
     * "Count independent connected regions in a graph-like structure."
     *
     * Grid problems often secretly represent graphs.
     *
     * Every cell:
     * - becomes a node
     * - adjacent valid neighbors become edges
     *
     * Here:
     * - land ('1') = traversable node
     * - water ('0') = blocked node
     * - island = connected component
     *
     * =====================================================================================
     *
     * 🟢 Core Invariant (MOST IMPORTANT SENTENCE):
     *
     * Once traversal starts from an unvisited land cell,
     * that traversal must consume ALL and ONLY the cells
     * belonging to that island before island count increases again.
     *
     * =====================================================================================
     *
     * 🟡 Why This Invariant Makes The Pattern Work:
     *
     * If traversal fully consumes one connected component:
     *
     * - future scans never recount it
     * - no land cell belongs to two islands
     * - every island contributes exactly one traversal start
     *
     * Therefore:
     *
     * Number of traversal starts from unvisited land cells
     * == Number of islands
     *
     * =====================================================================================
     *
     * 🔵 When This Pattern Applies:
     *
     * Use this pattern when:
     *
     * - problem asks for connected regions
     * - graph is implicit instead of explicit
     * - neighbors are local (up/down/left/right)
     * - traversal spreads outward naturally
     * - marking visited prevents duplicate work
     *
     * Common signals:
     *
     * - islands
     * - provinces
     * - blobs
     * - regions
     * - clusters
     * - enclosed areas
     * - flood fill
     * - infection spread
     *
     * =====================================================================================
     *
     * 🔵 Pattern Recognition Signals:
     *
     * Signal #1:
     * "Count groups/components"
     *
     * Signal #2:
     * Local neighbor movement only
     *
     * Signal #3:
     * Traversal from one point spreads naturally
     *
     * Signal #4:
     * Visited tracking matters critically
     *
     * Signal #5:
     * Revisiting causes overcounting or infinite recursion
     *
     * =====================================================================================
     *
     * 🔵 How This Pattern Differs From Similar Patterns:
     *
     * -------------------------------------------------------------------------
     * Flood Fill vs Multi-Source BFS
     * -------------------------------------------------------------------------
     *
     * Flood Fill:
     * - consumes one component completely
     * - objective = discover structure
     *
     * Multi-source BFS:
     * - spreads from many sources simultaneously
     * - objective = shortest distance/layer expansion
     *
     * -------------------------------------------------------------------------
     * Flood Fill vs Dynamic Programming
     * -------------------------------------------------------------------------
     *
     * Flood Fill:
     * - explores connectivity
     * - path history usually irrelevant
     *
     * DP:
     * - stores reusable subproblem answers
     * - directional state dependency matters
     *
     * -------------------------------------------------------------------------
     * Flood Fill vs Backtracking
     * -------------------------------------------------------------------------
     *
     * Flood Fill:
     * - exhaustive component consumption
     * - no branching decisions needing undo
     *
     * Backtracking:
     * - explores decision trees
     * - requires undo/revert
     *
     * -------------------------------------------------------------------------
     * Flood Fill vs Union Find
     * -------------------------------------------------------------------------
     *
     * Flood Fill:
     * - online traversal based
     * - simpler for static grids
     *
     * Union Find:
     * - connectivity structure maintenance
     * - stronger for dynamic edge merging
     *
     * =====================================================================================
     */

    /*
     * =====================================================================================
     * 🟢 MENTAL MODEL & INVARIANTS (CANONICAL SOURCE OF TRUTH)
     * =====================================================================================
     */

    /*
     * 🔵 Mental Model:
     *
     * Imagine standing on a land cell.
     *
     * The moment you touch that land,
     * you must spread across every reachable land cell
     * before declaring the island fully processed.
     *
     * You are "burning" or "coloring" the entire island.
     *
     * After that:
     * - no cell from this island should ever trigger another traversal
     * - otherwise island count becomes incorrect
     *
     * =====================================================================================
     *
     * 🟢 PRIMARY INVARIANT:
     *
     * Every visited land cell has already been assigned permanently
     * to exactly one discovered island.
     *
     * =====================================================================================
     *
     * 🟢 SECONDARY INVARIANT:
     *
     * Traversal starting from one unvisited land cell
     * must visit every reachable land cell in that component.
     *
     * =====================================================================================
     *
     * 🟢 THIRD INVARIANT:
     *
     * Island count increases ONLY when we discover
     * a previously unseen connected component root.
     *
     * =====================================================================================
     *
     * 🟢 State Meaning Of Each Variable:
     *
     * rows:
     * Total row count.
     *
     * cols:
     * Total column count.
     *
     * grid[r][c]:
     * Current terrain state.
     *
     * visited[r][c]:
     * Whether this cell has already been assigned to an island.
     *
     * islandCount:
     * Number of connected components discovered so far.
     *
     * queue / stack:
     * Frontier of expansion.
     *
     * =====================================================================================
     *
     * 🟢 Allowed Moves:
     *
     * From current cell:
     *
     * - move UP
     * - move DOWN
     * - move LEFT
     * - move RIGHT
     *
     * ONLY IF:
     *
     * - inside bounds
     * - land cell
     * - not visited yet
     *
     * =====================================================================================
     *
     * 🔴 Forbidden Moves:
     *
     * ❌ Visiting water
     * ❌ Revisiting processed land
     * ❌ Diagonal traversal
     * ❌ Counting before component fully consumed
     *
     * =====================================================================================
     *
     * 🟢 Why Diagonals Are Forbidden:
     *
     * Problem definition explicitly restricts connectivity
     * to horizontal and vertical neighbors only.
     *
     * Therefore:
     *
     * diagonally touching lands belong to different islands.
     *
     * Example:
     *
     * 1 0
     * 0 1
     *
     * Answer = 2 islands, not 1.
     *
     * =====================================================================================
     *
     * 🟢 Termination Logic:
     *
     * Traversal terminates because:
     *
     * - finite number of cells
     * - every land cell visited at most once
     * - no revisits allowed
     *
     * Therefore:
     * recursion / BFS frontier eventually empties.
     *
     * =====================================================================================
     *
     * 🔴 Why Common Alternatives Fail:
     *
     * -------------------------------------------------------------------------
     * Wrong Idea:
     * Count every land cell
     * -------------------------------------------------------------------------
     *
     * Violates invariant:
     * connected component identity ignored.
     *
     * Example:
     *
     * 1 1
     *
     * Correct answer:
     * 1 island
     *
     * Wrong answer:
     * 2
     *
     * -------------------------------------------------------------------------
     * Wrong Idea:
     * Increment count for every neighbor expansion
     * -------------------------------------------------------------------------
     *
     * Violates invariant:
     * one component must map to exactly one count increment.
     *
     * -------------------------------------------------------------------------
     * Wrong Idea:
     * Traverse without visited tracking
     * -------------------------------------------------------------------------
     *
     * Violates invariant:
     * processed ownership not preserved.
     *
     * Causes:
     * - infinite recursion
     * - duplicate counting
     *
     * =====================================================================================
     */

    /*
     * =====================================================================================
     * 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
     * =====================================================================================
     */

    /*
     * 🔴 Wrong Approach #1:
     * Count every '1'
     *
     * Why it seems correct:
     * Every island contains land cells.
     *
     * Why it fails:
     * Multiple land cells may belong to same connected component.
     *
     * Violated invariant:
     * "One connected component must map to one count increment."
     *
     * Minimal Counterexample:
     *
     * 1 1
     *
     * Correct:
     * 1 island
     *
     * Wrong:
     * 2
     *
     * =====================================================================================
     *
     * 🔴 Wrong Approach #2:
     * DFS/BFS without visited tracking
     *
     * Why it seems correct:
     * Traversal naturally spreads.
     *
     * Why it fails:
     * Traversal cycles forever between neighbors.
     *
     * Example:
     *
     * 1 1
     *
     * A <-> B revisits infinitely.
     *
     * Violated invariant:
     * "Every processed land cell must become permanently owned."
     *
     * =====================================================================================
     *
     * 🔴 Wrong Approach #3:
     * Mark visited AFTER recursive calls
     *
     * Why it seems harmless:
     * Eventually cell gets marked.
     *
     * Why it fails:
     * Neighbor recursion can re-enter current cell before marking.
     *
     * Example:
     *
     * A -> B -> A -> B ...
     *
     * Infinite recursion.
     *
     * =====================================================================================
     *
     * 🔴 Wrong Approach #4:
     * Increment island count inside traversal
     *
     * Why it seems intuitive:
     * "We discovered more land."
     *
     * Why it fails:
     * Count now tracks cells, not components.
     *
     * Violated invariant:
     * Component-level accounting destroyed.
     *
     * =====================================================================================
     *
     * 🔴 Wrong Approach #5:
     * Include diagonal neighbors
     *
     * Why it seems natural:
     * Human visual intuition sees touching corners.
     *
     * Why it fails:
     * Problem definition forbids diagonal connectivity.
     *
     * Counterexample:
     *
     * 1 0
     * 0 1
     *
     * Correct:
     * 2
     *
     * Wrong:
     * 1
     *
     * =====================================================================================
     *
     * 🔴 Interviewer Trap #1:
     * Forgetting mutation side effects
     *
     * If solution mutates grid:
     *
     * - caller loses original state
     * - repeated calls may fail
     *
     * Must explicitly mention this tradeoff.
     *
     * =====================================================================================
     *
     * 🔴 Interviewer Trap #2:
     * Stack overflow risk
     *
     * Recursive DFS depth can become O(m*n)
     * in pathological snake-shaped islands.
     *
     * Must discuss:
     * iterative DFS/BFS alternatives.
     *
     * =====================================================================================
     *
     * 🔴 Interviewer Trap #3:
     * Assuming rectangular safety incorrectly
     *
     * Always validate:
     * - row bounds
     * - column bounds
     *
     * BEFORE neighbor access.
     *
     * =====================================================================================
     *
     * 🔴 Interviewer Trap #4:
     * Counting before flood fill completes
     *
     * The order matters critically:
     *
     * 1. detect unvisited land
     * 2. increment island count ONCE
     * 3. fully consume component
     *
     * Never:
     *
     * 1. partially traverse
     * 2. recount remaining cells later
     *
     * =====================================================================================
     */

    /*
     * =====================================================================================
     * PRIMARY PROBLEM — SOLUTION CLASSES
     * =====================================================================================
     */



    /*
     * =====================================================================================
     * Solution #1 — Brute Force
     * =====================================================================================
     */

    static class BruteForceDFSWithSeparateVisited {

        /*
         * 🔵 Core Idea:
         *
         * Scan every cell.
         *
         * Whenever an unvisited land cell appears:
         * - increment island count
         * - DFS entire connected component
         *
         * =================================================================================
         *
         * 🟢 Invariant Enforced:
         *
         * Every land cell becomes permanently assigned
         * to exactly one DFS traversal.
         *
         * =================================================================================
         *
         * 🟡 What Limitation It Fixes:
         *
         * Prevents duplicate counting.
         *
         * =================================================================================
         *
         * ⏱ Time Complexity:
         * O(rows * cols)
         *
         * Each cell processed at most once.
         *
         * =================================================================================
         *
         * 📦 Space Complexity:
         * O(rows * cols)
         *
         * Due to:
         * - visited matrix
         * - recursion stack
         *
         * =================================================================================
         *
         * 🟣 Interview Preference:
         *
         * Good starter solution.
         * Very readable.
         * Excellent for invariant explanation.
         *
         * =================================================================================
         */

        private static final int[][] DIRECTIONS = {
                {-1, 0}, // UP
                {1, 0},  // DOWN
                {0, -1}, // LEFT
                {0, 1}   // RIGHT
        };

        public int numIslands(char[][] grid) {

            if (grid == null || grid.length == 0) {
                return 0;
            }

            int rows = grid.length;
            int cols = grid[0].length;

            boolean[][] visited = new boolean[rows][cols];

            int islandCount = 0;

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    /*
                     * 🟢 Only unvisited land can become
                     * a new connected component root.
                     */
                    if (grid[row][col] == '1' && !visited[row][col]) {

                        islandCount++;

                        dfs(grid, visited, row, col);
                    }
                }
            }

            return islandCount;
        }

        private void dfs(char[][] grid,
                         boolean[][] visited,
                         int row,
                         int col) {

            int rows = grid.length;
            int cols = grid[0].length;

            /*
             * 🔴 Boundary guard.
             */
            if (row < 0 || row >= rows || col < 0 || col >= cols) {
                return;
            }

            /*
             * 🔴 Water cells are non-traversable.
             */
            if (grid[row][col] == '0') {
                return;
            }

            /*
             * 🔴 Already assigned to an island.
             */
            if (visited[row][col]) {
                return;
            }

            /*
             * 🟢 Mark BEFORE exploring neighbors.
             *
             * Critical invariant protection.
             */
            visited[row][col] = true;

            for (int[] direction : DIRECTIONS) {

                int nextRow = row + direction[0];
                int nextCol = col + direction[1];

                dfs(grid, visited, nextRow, nextCol);
            }
        }
    }

    /*
     * =====================================================================================
     * Solution #2 — Improved
     * =====================================================================================
     */

    static class ImprovedBFS {

        /*
         * 🔵 Production Improvement:
         *
         * Semantic coordinate object instead of raw int[].
         *
         * Benefits:
         *
         * - readability
         * - self-documenting traversal state
         * - easier debugging
         * - removes magic indices
         */

        private static class Cell {

            final int row;
            final int col;

            Cell(int row, int col) {

                this.row = row;
                this.col = col;
            }
        }

        private static final int[][] DIRECTIONS = {
                {-1, 0}, // UP
                {1, 0},  // DOWN
                {0, -1}, // LEFT
                {0, 1}   // RIGHT
        };

        public int numIslands(char[][] grid) {

            if (grid == null || grid.length == 0) {
                return 0;
            }

            int totalRows = grid.length;
            int totalCols = grid[0].length;

            boolean[][] visited = new boolean[totalRows][totalCols];

            int islandCount = 0;

            for (int currentRow = 0;
                 currentRow < totalRows;
                 currentRow++) {

                for (int currentCol = 0;
                     currentCol < totalCols;
                     currentCol++) {

                    /*
                     * 🟢 Only fresh unvisited land
                     * can start a new island traversal.
                     */
                    if (grid[currentRow][currentCol] == '1'
                            && !visited[currentRow][currentCol]) {

                        islandCount++;

                        bfs(
                                grid,
                                visited,
                                currentRow,
                                currentCol
                        );
                    }
                }
            }

            return islandCount;
        }

        private void bfs(char[][] grid,
                         boolean[][] visited,
                         int sourceRow,
                         int sourceCol) {

            int totalRows = grid.length;
            int totalCols = grid[0].length;

            Queue<Cell> queue = new LinkedList<>();

            /*
             * 🟢 Mark immediately upon insertion.
             *
             * Prevents duplicate queue insertion.
             */
            visited[sourceRow][sourceCol] = true;

            queue.offer(new Cell(sourceRow, sourceCol));

            while (!queue.isEmpty()) {

                Cell currentCell = queue.poll();

                int currentRow = currentCell.row;
                int currentCol = currentCell.col;

                for (int[] direction : DIRECTIONS) {

                    int neighborRow =
                            currentRow + direction[0];

                    int neighborCol =
                            currentCol + direction[1];

                    /*
                     * 🔴 Boundary validation.
                     */
                    if (neighborRow < 0
                            || neighborRow >= totalRows
                            || neighborCol < 0
                            || neighborCol >= totalCols) {

                        continue;
                    }

                    /*
                     * 🔴 Water cannot belong to island.
                     */
                    if (grid[neighborRow][neighborCol] == '0') {
                        continue;
                    }

                    /*
                     * 🔴 Already assigned to an island.
                     */
                    if (visited[neighborRow][neighborCol]) {
                        continue;
                    }

                    /*
                     * 🟢 Ownership assignment happens NOW.
                     */
                    visited[neighborRow][neighborCol] = true;

                    queue.offer(
                            new Cell(
                                    neighborRow,
                                    neighborCol
                            )
                    );
                }
            }
        }
    }

    /*
     * =====================================================================================
     * Solution #3 — Optimal (Interview Preferred)
     * =====================================================================================
     */

    static class OptimalDFSInPlace {

        private static final int[][] DIRECTIONS = {
                {-1, 0}, // UP
                {1, 0},  // DOWN
                {0, -1}, // LEFT
                {0, 1}   // RIGHT
        };

        public int numIslands(char[][] grid) {

            if (grid == null || grid.length == 0) {
                return 0;
            }

            int totalRows = grid.length;
            int totalCols = grid[0].length;

            int islandCount = 0;

            for (int i = 0; i < totalRows; i++) {

                for (int j = 0; j < totalCols; j++) {

                    /*
                     * 🟢 New island root discovered.
                     *
                     * Every DFS traversal consumes
                     * exactly one connected component.
                     */
                    if (grid[i][j] == '1') {

                        islandCount++;

                        floodFill(grid, i, j);
                    }
                }
            }

            return islandCount;
        }

        private void floodFill(char[][] grid,
                               int row,
                               int col) {

            int totalRows = grid.length;
            int totalCols = grid[0].length;

            /*
             * 🔴 Boundary termination.
             */
            if (row < 0
                    || row >= totalRows
                    || col < 0
                    || col >= totalCols) {

                return;
            }

            /*
             * 🔴 Only unprocessed land expandable.
             */
            if (grid[row][col] != '1') {
                return;
            }

            /*
             * 🟢 Consume this land permanently.
             *
             * This prevents duplicate counting.
             */
            grid[row][col] = '0';

            for (int[] direction : DIRECTIONS) {

                int neighborRow =
                        row + direction[0];

                int neighborCol =
                        col + direction[1];

                floodFill(
                        grid,
                        neighborRow,
                        neighborCol
                );
            }
        }
    }


    /*
     * 🔴 BFS GUARANTEES SHORTEST PATH.
     * DFS DOES NOT.
     *
     * This single fact decides many graph/grid problems.
     *
     * -------------------------------------------------------------------------------------
     *
     * 🟣 Interview Rule:
     *
     * If order/distance/minimum steps matter -> BFS
     *
     * If completeness/component discovery matters -> DFS often enough
     *
     * -------------------------------------------------------------------------------------
     *
     * Number Of Islands:
     *
     * We only care about consuming the full connected component.
     *
     * Traversal order does NOT matter.
     *
     * Therefore:
     * both DFS and BFS work correctly.
     */


    /*

     * =====================================================================================
     * 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
     * =====================================================================================
     */

    /*
     * 🟣 If Asked:
     * "Explain your approach."
     *
     * Strong Interview Answer:
     *
     * "I treat the grid as an implicit graph where each land cell is a node
     * and horizontal/vertical neighbors form edges.
     *
     * The key invariant is:
     * once I discover an unvisited land cell,
     * I must consume its entire connected component before continuing scanning.
     *
     * Every DFS/BFS traversal corresponds to exactly one island.
     *
     * So:
     * - each traversal start increments island count once
     * - visited tracking prevents duplicate counting
     * - every land cell gets processed exactly once."
     *
     * =====================================================================================
     *
     * 🟣 Explain Discard Logic:
     *
     * We discard:
     *
     * - out-of-bounds cells
     * - water cells
     * - already visited cells
     *
     * because they cannot contribute
     * new component information.
     *
     * =====================================================================================
     *
     * 🟣 Why Correctness Is Guaranteed:
     *
     * Correctness comes from two properties:
     *
     * 1. Completeness:
     * DFS/BFS reaches every reachable land cell.
     *
     * 2. Exclusivity:
     * Visited marking ensures no cell belongs
     * to multiple island traversals.
     *
     * Therefore:
     * each connected component counted exactly once.
     *
     * =====================================================================================
     *
     * 🟣 What Breaks If Changed:
     *
     * -------------------------------------------------------------------------
     * If visited marking removed:
     * -------------------------------------------------------------------------
     *
     * Infinite revisits and duplicate counting.
     *
     * -------------------------------------------------------------------------
     * If diagonal movement added:
     * -------------------------------------------------------------------------
     *
     * Connectivity definition changes incorrectly.
     *
     * -------------------------------------------------------------------------
     * If counting happens per cell:
     * -------------------------------------------------------------------------
     *
     * We count area, not components.
     *
     * =====================================================================================
     *
     * 🟣 In-Place Feasibility:
     *
     * Yes.
     *
     * We can mutate:
     *
     * '1' -> '0'
     *
     * to encode visited state directly in grid.
     *
     * Tradeoff:
     * input destroyed.
     *
     * =====================================================================================
     *
     * 🟣 Streaming Feasibility:
     *
     * Not naturally streaming-friendly.
     *
     * Why?
     *
     * Connectivity depends on future neighboring cells.
     *
     * Full topology awareness required.
     *
     * =====================================================================================
     *
     * 🟣 When NOT To Use This Pattern:
     *
     * Avoid plain flood fill when:
     *
     * - weighted shortest path needed
     * - dynamic connectivity updates frequent
     * - global optimization required
     * - revisitation carries meaningful state
     *
     * Then prefer:
     *
     * - Dijkstra
     * - Union Find
     * - DP
     * - A*
     *
     * =====================================================================================
     */

    /*
     * =====================================================================================
     * 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
     * =====================================================================================
     */

    /*
     * 🔵 Variation #1:
     * Count Island Area Instead Of Count
     *
     * Examples:
     * - Max Area of Island
     *
     * =================================================================================
     *
     * 🟢 Invariant Change:
     *
     * Instead of:
     * "one traversal = one island count"
     *
     * We track:
     * "one traversal accumulates component size"
     *
     * =================================================================================
     *
     * 🔵 Variation #2:
     * Capture Surrounded Regions
     *
     * =================================================================================
     *
     * 🟢 Invariant Change:
     *
     * Border-connected regions are protected.
     *
     * Flood fill starts from borders instead of interiors.
     *
     * =================================================================================
     *
     * 🔵 Variation #3:
     * Distinct Island Shapes
     *
     * =================================================================================
     *
     * 🟢 Invariant Change:
     *
     * Relative geometry of traversal path matters.
     *
     * Need canonical shape encoding.
     *
     * =================================================================================
     *
     * 🔵 Variation #4:
     * Dynamic Island Addition
     *
     * Example:
     * Number of Islands II
     *
     * =================================================================================
     *
     * 🟢 Pattern Shift:
     *
     * Static flood fill becomes inefficient.
     *
     * Prefer:
     * Union Find with incremental merging.
     *
     * =================================================================================
     *
     * 🔵 Variation #5:
     * 8-Directional Connectivity
     *
     * =================================================================================
     *
     * 🟢 Invariant Change:
     *
     * Neighbor definition changes.
     *
     * Directions expand from 4 -> 8.
     *
     * =================================================================================
     *
     * 🔴 Pattern-Break Signals:
     *
     * Flood fill may NOT be ideal if:
     *
     * - edge weights exist
     * - shortest path required
     * - traversal costs differ
     * - revisits meaningful
     * - graph sparse and explicit already
     *
     * =================================================================================
     */



    /*
     * =====================================================================================
     * ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
     * =====================================================================================
     */

    /*
     * =====================================================================================
     * Reinforcement Problem #1
     * Max Area of Island
     * =====================================================================================
     */

    /*
     * LeetCode:
     * https://leetcode.com/problems/max-area-of-island/
     *
     * Difficulty:
     * Medium
     *
     * Tags:
     * DFS
     * BFS
     * Matrix
     * Graph
     *
     * =====================================================================================
     *
     * Official Problem Statement:
     *
     * You are given an m x n binary matrix grid.
     *
     * An island is a group of 1's (representing land) connected 4-directionally
     * (horizontal or vertical.)
     *
     * You may assume all four edges of the grid are surrounded by water.
     *
     * The area of an island is the number of cells with a value 1 in the island.
     *
     * Return the maximum area of an island in grid.
     * If there is no island, return 0.
     *
     * =====================================================================================
     */

    static class Reinforcement_MaxAreaOfIsland {

        /*
         * 🟢 Invariant Mapping:
         *
         * SAME connected-component invariant.
         *
         * Difference:
         * Instead of counting components,
         * we accumulate component size.
         *
         * One traversal = one island area computation.
         */

        private static final int[][] DIRECTIONS = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        public int maxAreaOfIsland(int[][] grid) {

            int rows = grid.length;
            int cols = grid[0].length;

            int maxArea = 0;

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (grid[row][col] == 1) {

                        int currentArea = dfs(grid, row, col);

                        maxArea = Math.max(maxArea, currentArea);
                    }
                }
            }

            return maxArea;
        }

        private int dfs(int[][] grid,
                        int row,
                        int col) {

            int rows = grid.length;
            int cols = grid[0].length;

            if (row < 0 || row >= rows
                    || col < 0 || col >= cols) {

                return 0;
            }

            if (grid[row][col] != 1) {
                return 0;
            }

            /*
             * 🟢 Consume cell immediately.
             */
            grid[row][col] = 0;

            int area = 1;

            for (int[] direction : DIRECTIONS) {

                area += dfs(
                        grid,
                        row + direction[0],
                        col + direction[1]
                );
            }

            return area;
        }
    }

    /*
     * =====================================================================================
     * 🟣 Interview Articulation:
     *
     * "This is the same flood fill invariant as Number of Islands.
     * The only difference is the traversal now returns component size
     * instead of just marking ownership."
     *
     * =====================================================================================
     *
     * 🔴 Edge Cases:
     *
     * - all water
     * - single cell island
     * - one huge island
     * - disconnected tiny islands
     *
     * =====================================================================================
     */

    /*
     * =====================================================================================
     * Reinforcement Problem #2
     * Flood Fill
     * =====================================================================================
     */

    /*
     * LeetCode:
     * https://leetcode.com/problems/flood-fill/
     *
     * Difficulty:
     * Easy
     *
     * Tags:
     * DFS
     * BFS
     * Matrix
     *
     * =====================================================================================
     *
     * Official Problem Statement:
     *
     * An image is represented by an m x n integer grid image
     * where image[i][j] represents the pixel value of the image.
     *
     * You are also given three integers sr, sc, and color.
     *
     * You should perform a flood fill on the image starting from
     * the pixel image[sr][sc].
     *
     * To perform a flood fill:
     *
     * - consider the starting pixel
     * - plus any pixels connected 4-directionally
     *   having the same original color
     *
     * Replace the color of all of the aforementioned pixels with color.
     *
     * Return the modified image after performing the flood fill.
     *
     * =====================================================================================
     */

    static class Reinforcement_FloodFill {

        /*
         * 🟢 Invariant Mapping:
         *
         * Traversal expands ONLY through cells
         * matching original color.
         *
         * Component membership defined by:
         * same-color connectivity.
         */

        private static final int[][] DIRECTIONS = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        public int[][] floodFill(int[][] image,
                                 int sr,
                                 int sc,
                                 int color) {

            int originalColor = image[sr][sc];

            /*
             * 🔴 Critical infinite recursion prevention.
             */
            if (originalColor == color) {
                return image;
            }

            dfs(image, sr, sc, originalColor, color);

            return image;
        }

        private void dfs(int[][] image,
                         int row,
                         int col,
                         int originalColor,
                         int newColor) {

            int rows = image.length;
            int cols = image[0].length;

            if (row < 0 || row >= rows
                    || col < 0 || col >= cols) {

                return;
            }

            if (image[row][col] != originalColor) {
                return;
            }

            /*
             * 🟢 Ownership/color transformation.
             */
            image[row][col] = newColor;

            for (int[] direction : DIRECTIONS) {

                dfs(
                        image,
                        row + direction[0],
                        col + direction[1],
                        originalColor,
                        newColor
                );
            }
        }
    }

    /*
     * =====================================================================================
     * 🟣 Interview Articulation:
     *
     * "Flood fill is literally connected-component traversal with transformation.
     * Instead of counting or measuring,
     * we mutate all cells belonging to same component."
     *
     * =====================================================================================
     *
     * 🔴 Edge Cases:
     *
     * - replacement color already same
     * - isolated pixel
     * - entire image same color
     * - border-connected expansion
     *
     * =====================================================================================
     */

    /*
     * =====================================================================================
     * Reinforcement Problem #3
     * Surrounded Regions
     * =====================================================================================
     */

    /*
     * LeetCode:
     * https://leetcode.com/problems/surrounded-regions/
     *
     * Difficulty:
     * Medium
     *
     * Tags:
     * DFS
     * BFS
     * Matrix
     *
     * =====================================================================================
     *
     * Official Problem Statement:
     *
     * You are given an m x n matrix board containing letters 'X' and 'O'.
     *
     * Capture regions that are surrounded:
     *
     * - Connect:
     *   a cell to adjacent cells horizontally or vertically.
     *
     * - Region:
     *   To form a region connect every 'O' cell.
     *
     * - Surround:
     *   The region is surrounded with 'X' cells if you can connect the region
     *   with 'X' cells and none of the region cells are on the edge of the board.
     *
     * A surrounded region is captured by replacing all 'O's with 'X's
     * in the input matrix board.
     *
     * =====================================================================================
     */

    static class Reinforcement_SurroundedRegions {

        /*
         * 🟢 Invariant Mapping:
         *
         * Border-connected 'O' cells are SAFE.
         *
         * Interior unmarked 'O' cells are capturable.
         *
         * Flood fill starts from borders.
         */

        private static final int[][] DIRECTIONS = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        public void solve(char[][] board) {

            if (board == null || board.length == 0) {
                return;
            }

            int rows = board.length;
            int cols = board[0].length;

            /*
             * 🟢 Mark all border-connected safe regions.
             */

            for (int row = 0; row < rows; row++) {

                dfs(board, row, 0);
                dfs(board, row, cols - 1);
            }

            for (int col = 0; col < cols; col++) {

                dfs(board, 0, col);
                dfs(board, rows - 1, col);
            }

            /*
             * 🔵 Final conversion pass.
             */

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (board[row][col] == 'O') {

                        /*
                         * Captured region.
                         */
                        board[row][col] = 'X';

                    } else if (board[row][col] == '#') {

                        /*
                         * Restore protected region.
                         */
                        board[row][col] = 'O';
                    }
                }
            }
        }

        private void dfs(char[][] board,
                         int row,
                         int col) {

            int rows = board.length;
            int cols = board[0].length;

            if (row < 0 || row >= rows
                    || col < 0 || col >= cols) {

                return;
            }

            if (board[row][col] != 'O') {
                return;
            }

            /*
             * 🟢 Temporarily mark safe region.
             */
            board[row][col] = '#';

            for (int[] direction : DIRECTIONS) {

                dfs(
                        board,
                        row + direction[0],
                        col + direction[1]
                );
            }
        }
    }

    /*
     * =====================================================================================
     * 🟣 Interview Articulation:
     *
     * "This problem flips the traversal perspective.
     * Instead of finding capturable regions directly,
     * we first preserve border-connected regions because those cannot be captured."
     *
     * =====================================================================================
     *
     * 🔴 Edge Cases:
     *
     * - single row
     * - single column
     * - all O
     * - all X
     * - tiny enclosed pocket
     *
     * =====================================================================================
     */

    /*
     * =====================================================================================
     * Reinforcement Problem #4
     * Number of Closed Islands
     * =====================================================================================
     */

    /*
     * LeetCode:
     * https://leetcode.com/problems/number-of-closed-islands/
     *
     * Difficulty:
     * Medium
     *
     * Tags:
     * DFS
     * BFS
     * Matrix
     *
     * =====================================================================================
     *
     * Official Problem Statement:
     *
     * Given a 2D grid consists of 0s (land) and 1s (water).
     * An island is a maximal 4-directionally connected group of 0s
     * and a closed island is an island totally
     * (all left, top, right, bottom) surrounded by 1s.
     *
     * Return the number of closed islands.
     *
     * =====================================================================================
     */

    static class Reinforcement_NumberOfClosedIslands {

        /*
         * 🟢 Invariant Mapping:
         *
         * Any component touching border is NOT closed.
         *
         * Traversal must propagate closure validity.
         */

        private static final int[][] DIRECTIONS = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        public int closedIsland(int[][] grid) {

            int rows = grid.length;
            int cols = grid[0].length;

            int closedCount = 0;

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (grid[row][col] == 0) {

                        if (dfs(grid, row, col)) {
                            closedCount++;
                        }
                    }
                }
            }

            return closedCount;
        }

        private boolean dfs(int[][] grid,
                            int row,
                            int col) {

            int rows = grid.length;
            int cols = grid[0].length;

            /*
             * 🔴 Escaped boundary.
             *
             * Therefore not closed.
             */
            if (row < 0 || row >= rows
                    || col < 0 || col >= cols) {

                return false;
            }

            /*
             * Water acts like valid enclosing wall.
             */
            if (grid[row][col] == 1) {
                return true;
            }

            /*
             * Already visited land treated as safe.
             */
            if (grid[row][col] == -1) {
                return true;
            }

            /*
             * 🟢 Mark visited.
             */
            grid[row][col] = -1;

            boolean isClosed = true;

            for (int[] direction : DIRECTIONS) {

                boolean childClosed = dfs(
                        grid,
                        row + direction[0],
                        col + direction[1]
                );

                /*
                 * 🟢 Closure validity propagates upward.
                 */
                isClosed = isClosed && childClosed;
            }

            return isClosed;
        }
    }

    /*
     * =====================================================================================
     * 🟣 Interview Articulation:
     *
     * "The key extension here is that traversal now returns a boolean property
     * describing the entire component.
     *
     * A single boundary escape invalidates closure for the whole island."
     *
     * =====================================================================================
     *
     * 🔴 Edge Cases:
     *
     * - land touching edge
     * - tiny enclosed island
     * - giant open region
     * - all land
     * - all water
     *
     * =====================================================================================
     */




    /*
     * =====================================================================================
     * 🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
     * =====================================================================================
     */

    /*
     * =====================================================================================
     * Related Problem #1
     * Number of Provinces
     * =====================================================================================
     */

    /*
     * LeetCode:
     * https://leetcode.com/problems/number-of-provinces/
     *
     * Difficulty:
     * Medium
     *
     * =====================================================================================
     *
     * 🔵 Core Difference:
     *
     * Graph represented as adjacency matrix instead of grid.
     *
     * =====================================================================================
     *
     * 🟢 Shared Invariant:
     *
     * One traversal completely consumes one connected component.
     *
     * =====================================================================================
     *
     * 🟣 Interview Mapping:
     *
     * "Number of Islands on a graph instead of a matrix."
     *
     * =====================================================================================
     */

    static class Related_NumberOfProvinces {

        public int findCircleNum(int[][] isConnected) {

            int n = isConnected.length;

            boolean[] visited = new boolean[n];

            int provinceCount = 0;

            for (int city = 0; city < n; city++) {

                if (!visited[city]) {

                    provinceCount++;

                    dfs(isConnected, visited, city);
                }
            }

            return provinceCount;
        }

        private void dfs(int[][] graph,
                         boolean[] visited,
                         int city) {

            visited[city] = true;

            for (int neighbor = 0; neighbor < graph.length; neighbor++) {

                if (graph[city][neighbor] == 1
                        && !visited[neighbor]) {

                    dfs(graph, visited, neighbor);
                }
            }
        }
    }

    /*
     * =====================================================================================
     * Related Problem #2
     * Rotten Oranges
     * =====================================================================================
     */

    /*
     * LeetCode:
     * https://leetcode.com/problems/rotting-oranges/
     *
     * Difficulty:
     * Medium
     *
     * =====================================================================================
     *
     * 🔵 Core Difference:
     *
     * This is NOT pure connected component counting.
     *
     * Time/layer propagation matters.
     *
     * =====================================================================================
     *
     * 🟢 Shared Invariant:
     *
     * Expansion occurs through local neighbors.
     *
     * =====================================================================================
     *
     * 🔴 Important Pattern Shift:
     *
     * Multi-source BFS required because
     * shortest-time propagation matters.
     *
     * =====================================================================================
     */

    static class Related_RottingOranges {

        private static final int[][] DIRECTIONS = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        public int orangesRotting(int[][] grid) {

            int rows = grid.length;
            int cols = grid[0].length;

            Queue<int[]> queue = new LinkedList<>();

            int fresh = 0;

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (grid[row][col] == 2) {

                        queue.offer(new int[]{row, col});

                    } else if (grid[row][col] == 1) {

                        fresh++;
                    }
                }
            }

            int minutes = 0;

            while (!queue.isEmpty() && fresh > 0) {

                int size = queue.size();

                for (int i = 0; i < size; i++) {

                    int[] current = queue.poll();

                    int row = current[0];
                    int col = current[1];

                    for (int[] direction : DIRECTIONS) {

                        int nextRow = row + direction[0];
                        int nextCol = col + direction[1];

                        if (nextRow < 0 || nextRow >= rows
                                || nextCol < 0 || nextCol >= cols) {

                            continue;
                        }

                        if (grid[nextRow][nextCol] != 1) {
                            continue;
                        }

                        grid[nextRow][nextCol] = 2;

                        fresh--;

                        queue.offer(new int[]{nextRow, nextCol});
                    }
                }

                minutes++;
            }

            return fresh == 0 ? minutes : -1;
        }
    }

    /*
     * =====================================================================================
     * Related Problem #3
     * Pacific Atlantic Water Flow
     * =====================================================================================
     */

    /*
     * LeetCode:
     * https://leetcode.com/problems/pacific-atlantic-water-flow/
     *
     * Difficulty:
     * Medium
     *
     * =====================================================================================
     *
     * 🔵 Core Difference:
     *
     * Reachability from multiple boundary sources.
     *
     * =====================================================================================
     *
     * 🟢 Shared Invariant:
     *
     * Traversal validity defined by neighbor constraints.
     *
     * =====================================================================================
     *
     * 🔴 Pattern Shift:
     *
     * Reverse-thinking traversal.
     *
     * Instead of:
     * source -> ocean
     *
     * We do:
     * ocean -> reachable cells
     *
     * =====================================================================================
     */

    static class Related_PacificAtlanticWaterFlow {

        private static final int[][] DIRECTIONS = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        public List<List<Integer>> pacificAtlantic(int[][] heights) {

            int rows = heights.length;
            int cols = heights[0].length;

            boolean[][] pacific = new boolean[rows][cols];
            boolean[][] atlantic = new boolean[rows][cols];

            for (int row = 0; row < rows; row++) {

                dfs(heights, pacific, row, 0, Integer.MIN_VALUE);

                dfs(heights, atlantic, row, cols - 1, Integer.MIN_VALUE);
            }

            for (int col = 0; col < cols; col++) {

                dfs(heights, pacific, 0, col, Integer.MIN_VALUE);

                dfs(heights, atlantic, rows - 1, col, Integer.MIN_VALUE);
            }

            List<List<Integer>> result = new ArrayList<>();

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (pacific[row][col] && atlantic[row][col]) {

                        result.add(Arrays.asList(row, col));
                    }
                }
            }

            return result;
        }

        private void dfs(int[][] heights,
                         boolean[][] visited,
                         int row,
                         int col,
                         int previousHeight) {

            int rows = heights.length;
            int cols = heights[0].length;

            if (row < 0 || row >= rows
                    || col < 0 || col >= cols) {

                return;
            }

            if (visited[row][col]) {
                return;
            }

            if (heights[row][col] < previousHeight) {
                return;
            }

            visited[row][col] = true;

            for (int[] direction : DIRECTIONS) {

                dfs(
                        heights,
                        visited,
                        row + direction[0],
                        col + direction[1],
                        heights[row][col]
                );
            }
        }
    }

    /*
     * =====================================================================================
     * 🟢 LEARNING VERIFICATION
     * =====================================================================================
     */

    /*
     * 🟢 Invariant Recall Without Code:
     *
     * Q:
     * What is the SINGLE most important invariant?
     *
     * A:
     * One traversal must consume one entire connected component exactly once.
     *
     * =====================================================================================
     *
     * 🟢 Naive Failure Explanation:
     *
     * Q:
     * Why does counting every land cell fail?
     *
     * A:
     * Because islands are connected components,
     * not individual cells.
     *
     * =====================================================================================
     *
     * 🟢 Debugging Readiness:
     *
     * If answer too large:
     * - duplicate counting likely
     * - visited marking broken
     *
     * If stack overflow:
     * - recursive DFS depth issue
     * - switch to iterative BFS/DFS
     *
     * If diagonal merge happening:
     * - neighbor directions incorrect
     *
     * =====================================================================================
     *
     * 🟢 Pattern Recognition Signals:
     *
     * - Count connected groups
     * - Local neighbor expansion
     * - Grid-as-graph
     * - Need ownership marking
     * - Revisits dangerous
     *
     * =====================================================================================
     */

    /*
     * =====================================================================================
     * 🧪 MAIN METHOD + SELF-VERIFYING TESTS
     * =====================================================================================
     */

    public static void main(String[] args) {

        testExampleOne();

        testExampleTwo();

        testSingleCellIsland();

        testAllWater();

        testDiagonalTrap();

        testLargeConnectedIsland();

        testFloodFillMutationInvariant();

        System.out.println();
        System.out.println("✅ ALL TESTS PASSED");
    }

    /*
     * =====================================================================================
     * Helper Utilities
     * =====================================================================================
     */

    private static void assertEquals(int expected,
                                     int actual,
                                     String message) {

        if (expected != actual) {

            throw new AssertionError(
                    message
                            + " | Expected = "
                            + expected
                            + " , Actual = "
                            + actual
            );
        }

        System.out.println("PASS -> " + message);
    }

    private static char[][] deepCopy(char[][] original) {

        char[][] copy = new char[original.length][];

        for (int i = 0; i < original.length; i++) {

            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }

        return copy;
    }

    /*
     * =====================================================================================
     * Test #1
     * Official Example 1
     * =====================================================================================
     */

    private static void testExampleOne() {

        char[][] grid = {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'}
        };

        OptimalDFSInPlace solver = new OptimalDFSInPlace();

        int result = solver.numIslands(deepCopy(grid));

        assertEquals(
                1,
                result,
                "Official Example 1"
        );
    }

    /*
     * =====================================================================================
     * Test #2
     * Official Example 2
     * =====================================================================================
     */

    private static void testExampleTwo() {

        char[][] grid = {
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };

        OptimalDFSInPlace solver = new OptimalDFSInPlace();

        int result = solver.numIslands(deepCopy(grid));

        assertEquals(
                3,
                result,
                "Official Example 2"
        );
    }

    /*
     * =====================================================================================
     * Test #3
     * Boundary Case — Single Land Cell
     * =====================================================================================
     */

    private static void testSingleCellIsland() {

        char[][] grid = {
                {'1'}
        };

        OptimalDFSInPlace solver = new OptimalDFSInPlace();

        int result = solver.numIslands(deepCopy(grid));

        assertEquals(
                1,
                result,
                "Single land cell should form one island"
        );
    }

    /*
     * =====================================================================================
     * Test #4
     * Boundary Case — All Water
     * =====================================================================================
     */

    private static void testAllWater() {

        char[][] grid = {
                {'0', '0'},
                {'0', '0'}
        };

        OptimalDFSInPlace solver = new OptimalDFSInPlace();

        int result = solver.numIslands(deepCopy(grid));

        assertEquals(
                0,
                result,
                "All water should produce zero islands"
        );
    }

    /*
     * =====================================================================================
     * Test #5
     * Interview Trap — Diagonal Connectivity
     * =====================================================================================
     */

    private static void testDiagonalTrap() {

        char[][] grid = {
                {'1', '0'},
                {'0', '1'}
        };

        OptimalDFSInPlace solver = new OptimalDFSInPlace();

        int result = solver.numIslands(deepCopy(grid));

        /*
         * 🔴 Critical invariant verification:
         * diagonal adjacency does NOT connect islands.
         */

        assertEquals(
                2,
                result,
                "Diagonal cells must remain separate islands"
        );
    }

    /*
     * =====================================================================================
     * Test #6
     * Large Connected Component
     * =====================================================================================
     */

    private static void testLargeConnectedIsland() {

        char[][] grid = {
                {'1', '1', '1'},
                {'1', '1', '1'},
                {'1', '1', '1'}
        };

        OptimalDFSInPlace solver = new OptimalDFSInPlace();

        int result = solver.numIslands(deepCopy(grid));

        assertEquals(
                1,
                result,
                "Entire connected grid should form one island"
        );
    }

    /*
     * =====================================================================================
     * Test #7
     * Mutation Invariant Validation
     * =====================================================================================
     */

    private static void testFloodFillMutationInvariant() {

        char[][] grid = {
                {'1', '1'},
                {'1', '0'}
        };

        OptimalDFSInPlace solver = new OptimalDFSInPlace();

        char[][] workingGrid = deepCopy(grid);

        int result = solver.numIslands(workingGrid);

        assertEquals(
                1,
                result,
                "Mutation traversal should still count correctly"
        );

        /*
         * 🟢 After traversal:
         * all consumed land must become water.
         */

        if (workingGrid[0][0] != '0') {

            throw new AssertionError(
                    "Consumed land should mutate to character '0'"
            );
        }

        System.out.println(
                "PASS -> Consumed land should mutate to character '0'"
        );
    }

    /*
     * =====================================================================================
     * ✅ COMPLETION CHECKLIST
     * =====================================================================================
     */

    /*
     * ✅ Invariant
     *
     * One traversal consumes one entire connected component exactly once.
     *
     * =====================================================================================
     *
     * ✅ Search Target
     *
     * Discover all unvisited land-connected regions.
     *
     * =====================================================================================
     *
     * ✅ Discard Rule
     *
     * Ignore:
     * - water
     * - out of bounds
     * - already visited cells
     *
     * =====================================================================================
     *
     * ✅ Termination
     *
     * Finite grid + each cell processed at most once.
     *
     * =====================================================================================
     *
     * ✅ Naive Failure
     *
     * Counting cells instead of components violates connectivity invariant.
     *
     * =====================================================================================
     *
     * ✅ Edge Cases
     *
     * - all water
     * - all land
     * - single cell
     * - diagonal trap
     * - disconnected islands
     *
     * =====================================================================================
     *
     * ✅ Variant Readiness
     *
     * Can adapt to:
     * - area counting
     * - border protection
     * - dynamic connectivity
     * - shape recognition
     *
     * =====================================================================================
     *
     * ✅ Pattern Boundary
     *
     * Avoid plain flood fill when:
     * - shortest path required
     * - weighted traversal exists
     * - revisits meaningful
     * - dynamic graph updates dominate
     *
     * =====================================================================================
     *
     * 🧘 FINAL CLOSURE STATEMENT
     *
     * I understand the invariant.
     * I can re-derive the solution.
     * This chapter is complete.
     *
     * =====================================================================================
     */

}
