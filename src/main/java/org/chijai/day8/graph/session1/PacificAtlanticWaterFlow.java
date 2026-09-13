package org.chijai.day8.graph.session1;

import java.util.*;

/**
 * ============================================================================
 * Pacific Atlantic Water Flow — Java Gold
 * ============================================================================
 *
 * LeetCode 417
 *
 * Core classification:
 *
 *     Graph / Grid
 *     Reverse Reachability
 *     Multi-Source DFS/BFS
 *     Set Intersection
 *
 * THIS IS NOT CONNECTED-COMPONENT COUNTING.
 *
 * Number of Islands asks:
 *
 *     "Starting from an unseen node, which whole component belongs together?"
 *
 * Pacific Atlantic asks:
 *
 *     "Which cells are reachable from source-set A?"
 *     "Which cells are reachable from source-set B?"
 *     "Which cells belong to BOTH reachable sets?"
 *
 * ==========================================================================
 * 1. PROBLEM STATEMENT
 * ==========================================================================
 *
 * A rectangular matrix heights represents cell heights.
 *
 * Water can flow from a cell to a 4-directionally adjacent cell when the next
 * cell's height is less than or equal to the current cell's height.
 *
 * The Pacific Ocean touches the TOP and LEFT borders.
 * The Atlantic Ocean touches the BOTTOM and RIGHT borders.
 *
 * Return every cell from which water can eventually reach BOTH oceans.
 *
 * Example:
 *
 *     heights =
 *
 *       1 2 2 3 5
 *       3 2 3 4 4
 *       2 4 5 3 1
 *       6 7 1 4 5
 *       5 1 1 2 4
 *
 *     answer =
 *
 *       [0,4]
 *       [1,3]
 *       [1,4]
 *       [2,2]
 *       [3,0]
 *       [3,1]
 *       [4,0]
 *
 * ==========================================================================
 * 2. RECOGNITION + FIRST-PRINCIPLES INVENTION PATH
 * ==========================================================================
 *
 * STEP 1 — Model the grid as a graph.
 *
 *     cell = node
 *     4-direction move = possible edge
 *
 * STEP 2 — Notice that the edges are directional.
 *
 * Normal water flow:
 *
 *     currentHeight >= neighborHeight
 *
 * so water can move:
 *
 *     HIGHER / EQUAL  --->  LOWER / EQUAL
 *
 * STEP 3 — Naive thought.
 *
 * For EVERY cell:
 *
 *     can this cell reach Pacific?
 *     can this cell reach Atlantic?
 *
 * That repeats almost the same traversal many times.
 *
 * STEP 4 — Reverse the question.
 *
 * Instead of:
 *
 *     cell ---> ocean
 *
 * traverse the reversed edges:
 *
 *     ocean ---> cells that could have flowed into it
 *
 * Reverse movement therefore becomes:
 *
 *     LOWER / EQUAL  --->  HIGHER / EQUAL
 *
 * STEP 5 — Run the same reachability engine twice.
 *
 *     Pacific borders
 *          ↓
 *     reverse DFS/BFS
 *          ↓
 *     pacificReachable
 *
 *     Atlantic borders
 *          ↓
 *     reverse DFS/BFS
 *          ↓
 *     atlanticReachable
 *
 * STEP 6 — Intersect.
 *
 *     pacificReachable[row][col]
 *              &&
 *     atlanticReachable[row][col]
 *
 *     => answer
 *
 * ==========================================================================
 * 3. REUSABLE BASE ENGINE — MULTI-SOURCE REACHABILITY
 * ==========================================================================
 *
 * Learn this once:
 *
 *     reachable = visited state
 *
 *     seed ALL valid source nodes
 *
 *     for every source:
 *         dfs/bfs(source)
 *
 *     traversal(node):
 *         mark reachable
 *         traverse every neighbor satisfying the movement rule
 *
 * For Pacific Atlantic, run that SAME engine twice and intersect the results.
 *
 * Stable engine:
 *
 *     source set
 *         ↓
 *     DFS/BFS
 *         ↓
 *     reachable set
 *
 * Problem delta:
 *
 *     Δ1. Which nodes are the sources?
 *     Δ2. Which neighbor moves are allowed?
 *     Δ3. How are the reachability sets used in the answer?
 */
public class PacificAtlanticWaterFlow {

    /**
     * ========================================================================
     * 4. ⭐ PRIMARY SOLUTION — REVERSE MULTI-SOURCE DFS
     * ========================================================================
     *
     * Why primary?
     *
     * - optimal O(rows * cols)
     * - directly expresses the reverse-reachability insight
     * - reuses ordinary DFS
     * - no graph construction
     * - easiest reconstruction once the reverse-edge trick is remembered
     *
     * Primary invariant:
     *
     *     pacificReachable[r][c] == true
     *
     * means:
     *
     *     Starting from the Pacific boundary and walking along REVERSED valid
     *     water edges, we can reach (r,c).
     *
     * Equivalently in the original direction:
     *
     *     Water from (r,c) can reach the Pacific.
     *
     * Same meaning for atlanticReachable.
     */
    static class PrimaryReverseDFS {

        public List<List<Integer>> pacificAtlantic(int[][] heights) {

            if (heights == null
                    || heights.length == 0
                    || heights[0].length == 0) {
                return Collections.emptyList();
            }

            int rows = heights.length;
            int cols = heights[0].length;

            boolean[][] pacificReachable = new boolean[rows][cols];
            boolean[][] atlanticReachable = new boolean[rows][cols];

            /*
             * Pacific touches LEFT.
             * Atlantic touches RIGHT.
             */
            for (int row = 0; row < rows; row++) {

                dfs(
                        heights,
                        pacificReachable,
                        row,
                        0,
                        heights[row][0]
                );

                dfs(
                        heights,
                        atlanticReachable,
                        row,
                        cols - 1,
                        heights[row][cols - 1]
                );
            }

            /*
             * Pacific touches TOP.
             * Atlantic touches BOTTOM.
             */
            for (int col = 0; col < cols; col++) {

                dfs(
                        heights,
                        pacificReachable,
                        0,
                        col,
                        heights[0][col]
                );

                dfs(
                        heights,
                        atlanticReachable,
                        rows - 1,
                        col,
                        heights[rows - 1][col]
                );
            }

            List<List<Integer>> answer = new ArrayList<>();

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (pacificReachable[row][col]
                            && atlanticReachable[row][col]) {

                        answer.add(List.of(row, col));
                    }
                }
            }

            return answer;
        }

        private void dfs(int[][] heights,
                         boolean[][] reachable,
                         int row,
                         int col,
                         int previousHeight) {

            int rows = heights.length;
            int cols = heights[0].length;

            if (row < 0
                    || row >= rows
                    || col < 0
                    || col >= cols) {
                return;
            }

            if (reachable[row][col]) {
                return;
            }

            /*
             * REVERSED EDGE RULE.
             *
             * Normal water flow:
             *
             *     high -> low
             *
             * Reverse traversal from an ocean:
             *
             *     low -> high
             *
             * Therefore the next cell must be >= the previous cell.
             */
            if (heights[row][col] < previousHeight) {
                return;
            }

            reachable[row][col] = true;

            int currentHeight = heights[row][col];

            // Fixed 4-neighbor grid: explicit calls are easiest to reconstruct.
            dfs(heights, reachable, row - 1, col, currentHeight); // up
            dfs(heights, reachable, row + 1, col, currentHeight); // down
            dfs(heights, reachable, row, col - 1, currentHeight); // left
            dfs(heights, reachable, row, col + 1, currentHeight); // right
        }
    }

    /**
     * ========================================================================
     * 5. 🔁 PATTERN REUSE — LEARN ONCE, STORE ONLY THE DELTA
     * ========================================================================
     *
     * BASE ENGINE — REACHABILITY DFS/BFS
     *
     *     source set
     *         ↓
     *     mark reachable
     *         ↓
     *     traverse valid neighbors
     *         ↓
     *     use reachable set
     *
     * Do NOT memorize a new DFS for every problem.
     * Keep the traversal engine fixed and isolate the delta.
     *
     * ------------------------------------------------------------------------
     * Δ PACIFIC ATLANTIC — PRIMARY ABOVE
     * ------------------------------------------------------------------------
     *
     * representation = height grid
     * node           = cell
     * sources        = ocean-border cells
     * neighbors      = 4-direction cells with height >= current height
     * visited        = boolean[][] per ocean
     * result         = intersection of two reachable sets
     *
     * Δ1 = two source sets: Pacific borders + Atlantic borders
     * Δ2 = reverse edge rule: nextHeight >= currentHeight
     * Δ3 = answer = Pacific ∩ Atlantic
     *
     * ------------------------------------------------------------------------
     * Δ SURROUNDED REGIONS
     * ------------------------------------------------------------------------
     *
     * representation = char grid
     * node           = 'O' cell
     * sources        = border 'O' cells
     * neighbors      = adjacent 'O' cells
     * visited        = mutate safe cells to '#'
     * result         = preserve reachable border region; flip the rest
     *
     * SAME reachability idea:
     *
     *     start from boundary sources
     *     mark everything reachable
     *     use the complement afterward
     */
    static class SurroundedRegions {

        public void solve(char[][] board) {

            if (board == null || board.length == 0) {
                return;
            }

            int rows = board.length;
            int cols = board[0].length;

            for (int row = 0; row < rows; row++) {
                dfs(board, row, 0);
                dfs(board, row, cols - 1);
            }

            for (int col = 0; col < cols; col++) {
                dfs(board, 0, col);
                dfs(board, rows - 1, col);
            }

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (board[row][col] == 'O') {
                        board[row][col] = 'X';
                    } else if (board[row][col] == '#') {
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

            // SAME GRID-DFS BOILERPLATE: boundary guard.
            if (row < 0
                    || row >= rows
                    || col < 0
                    || col >= cols) {
                return;
            }

            // DELTA: only 'O' belongs to the safe component.
            if (board[row][col] != 'O') {
                return;
            }

            // DELTA: mark this border-reachable cell as protected.
            board[row][col] = '#';

            dfs(board, row - 1, col);
            dfs(board, row + 1, col);
            dfs(board, row, col - 1);
            dfs(board, row, col + 1);
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Δ NUMBER OF ENCLAVES
     * ------------------------------------------------------------------------
     *
     * representation = binary grid
     * node           = land cell
     * sources        = border land cells
     * neighbors      = adjacent land
     * visited        = mutate reachable border land to water
     * result         = count land NOT reachable from boundary
     *
     * SAME boundary-source reachability engine as Surrounded Regions.
     */
    static class NumberOfEnclaves {

        public int numEnclaves(int[][] grid) {

            int rows = grid.length;
            int cols = grid[0].length;

            for (int row = 0; row < rows; row++) {
                removeBorderLand(grid, row, 0);
                removeBorderLand(grid, row, cols - 1);
            }

            for (int col = 0; col < cols; col++) {
                removeBorderLand(grid, 0, col);
                removeBorderLand(grid, rows - 1, col);
            }

            int enclaves = 0;

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (grid[row][col] == 1) {
                        enclaves++;
                    }
                }
            }

            return enclaves;
        }

        private void removeBorderLand(int[][] grid,
                                      int row,
                                      int col) {

            int rows = grid.length;
            int cols = grid[0].length;

            if (row < 0
                    || row >= rows
                    || col < 0
                    || col >= cols) {
                return;
            }

            if (grid[row][col] != 1) {
                return;
            }

            grid[row][col] = 0;

            removeBorderLand(grid, row - 1, col);
            removeBorderLand(grid, row + 1, col);
            removeBorderLand(grid, row, col - 1);
            removeBorderLand(grid, row, col + 1);
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Δ GENERIC TWO-SOURCE-SET REACHABILITY
     * ------------------------------------------------------------------------
     *
     * Pacific Atlantic is an instance of a broader shape:
     *
     *     sourcesA -> reachableA
     *     sourcesB -> reachableB
     *
     *     answer = reachableA ∩ reachableB
     *
     * The traversal engine can stay identical while the following change:
     *
     *     source sets
     *     neighbor predicate
     *     final set operation
     */

    /**
     * ========================================================================
     * 6. PRIMARY VISUAL DRY RUN
     * ========================================================================
     *
     * Tiny example:
     *
     *     heights
     *
     *          Pacific (top)
     *             ↓ ↓ ↓
     *
     *             1  2  2
     * Pacific ->  3  2  3  <- Atlantic
     *             2  4  5
     *             ↑  ↑  ↑
     *          Atlantic (bottom)
     *
     * Pacific sources:
     *
     *     top row + left column
     *
     * Atlantic sources:
     *
     *     bottom row + right column
     *
     * ------------------------------------------------------------------------
     * WHY REVERSE?
     * ------------------------------------------------------------------------
     *
     * Suppose:
     *
     *     A(height 5) ---> B(height 2)
     *
     * Water can flow A -> B.
     *
     * Therefore when traversing BACKWARD from B, we may move:
     *
     *     B(height 2) ---> A(height 5)
     *
     * so the reverse rule is:
     *
     *     nextHeight >= currentHeight
     *
     * ------------------------------------------------------------------------
     * PACIFIC TRAVERSAL
     * ------------------------------------------------------------------------
     *
     * Start from all Pacific-border cells.
     * DFS uphill/equal and mark every cell from which water could eventually
     * flow down to the Pacific.
     *
     * ------------------------------------------------------------------------
     * ATLANTIC TRAVERSAL
     * ------------------------------------------------------------------------
     *
     * Repeat from all Atlantic-border cells.
     *
     * ------------------------------------------------------------------------
     * FINAL
     * ------------------------------------------------------------------------
     *
     *     reachable by Pacific?   true/false
     *     reachable by Atlantic?  true/false
     *
     * Only:
     *
     *     true && true
     *
     * enters the answer.
     */

    /**
     * ========================================================================
     * 7. COMPLEXITY DERIVATION
     * ========================================================================
     *
     * Let:
     *
     *     R = rows
     *     C = columns
     *     V = R * C cells
     *
     * Each Pacific DFS marks a cell at most once.
     * Each Atlantic DFS marks a cell at most once.
     *
     * Every processed cell checks four neighbors.
     * Four is constant.
     *
     * Therefore:
     *
     *     Pacific traversal  = O(R * C)
     *     Atlantic traversal = O(R * C)
     *     final intersection = O(R * C)
     *
     * Total:
     *
     *     O(R * C)
     *
     * Space:
     *
     *     pacificReachable = O(R * C)
     *     atlanticReachable = O(R * C)
     *     recursion stack = O(R * C) worst case
     *
     * Overall auxiliary space:
     *
     *     O(R * C)
     */

    /**
     * ========================================================================
     * 8. ALTERNATIVES + TRADE-OFFS
     * ========================================================================
     *
     * ------------------------------------------------------------------------
     * A. NAIVE — DFS FROM EVERY CELL TOWARD BOTH OCEANS
     * ------------------------------------------------------------------------
     *
     * For each cell:
     *
     *     search whether Pacific is reachable
     *     search whether Atlantic is reachable
     *
     * Worst case:
     *
     *     O(R * C) start cells
     *     × O(R * C) traversal per start
     *
     *     = O((R * C)^2)
     *
     * Space per traversal:
     *
     *     O(R * C)
     *
     * Why inferior:
     *
     *     repeats the same reachability work from many cells.
     *
     * ------------------------------------------------------------------------
     * B. REVERSE MULTI-SOURCE BFS
     * ------------------------------------------------------------------------
     *
     * Same asymptotic complexity as primary DFS:
     *
     *     Time  = O(R * C)
     *     Space = O(R * C)
     *
     * Why DFS remains primary here:
     *
     *     no shortest-distance/layer requirement
     *     recursive implementation is compact and reconstructable
     *
     * Why BFS may be preferable in production:
     *
     *     avoids recursion-depth / stack-overflow risk on large grids
     */
    static class AlternativeReverseBFS {

        record Cell(int row, int col) {
        }

        public List<List<Integer>> pacificAtlantic(int[][] heights) {

            if (heights == null
                    || heights.length == 0
                    || heights[0].length == 0) {
                return Collections.emptyList();
            }

            int rows = heights.length;
            int cols = heights[0].length;

            boolean[][] pacificReachable = new boolean[rows][cols];
            boolean[][] atlanticReachable = new boolean[rows][cols];

            Queue<Cell> pacificQueue = new ArrayDeque<>();
            Queue<Cell> atlanticQueue = new ArrayDeque<>();

            for (int row = 0; row < rows; row++) {
                offerIfNew(pacificQueue, pacificReachable, row, 0);
                offerIfNew(atlanticQueue, atlanticReachable, row, cols - 1);
            }

            for (int col = 0; col < cols; col++) {
                offerIfNew(pacificQueue, pacificReachable, 0, col);
                offerIfNew(atlanticQueue, atlanticReachable, rows - 1, col);
            }

            bfs(heights, pacificReachable, pacificQueue);
            bfs(heights, atlanticReachable, atlanticQueue);

            List<List<Integer>> answer = new ArrayList<>();

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (pacificReachable[row][col]
                            && atlanticReachable[row][col]) {

                        answer.add(List.of(row, col));
                    }
                }
            }

            return answer;
        }

        private void bfs(int[][] heights,
                         boolean[][] reachable,
                         Queue<Cell> queue) {

            int rows = heights.length;
            int cols = heights[0].length;

            while (!queue.isEmpty()) {

                Cell current = queue.poll();

                int row = current.row();
                int col = current.col();
                int currentHeight = heights[row][col];

                offerNeighbor(
                        heights,
                        reachable,
                        queue,
                        row - 1,
                        col,
                        currentHeight,
                        rows,
                        cols
                );

                offerNeighbor(
                        heights,
                        reachable,
                        queue,
                        row + 1,
                        col,
                        currentHeight,
                        rows,
                        cols
                );

                offerNeighbor(
                        heights,
                        reachable,
                        queue,
                        row,
                        col - 1,
                        currentHeight,
                        rows,
                        cols
                );

                offerNeighbor(
                        heights,
                        reachable,
                        queue,
                        row,
                        col + 1,
                        currentHeight,
                        rows,
                        cols
                );
            }
        }

        private void offerNeighbor(int[][] heights,
                                   boolean[][] reachable,
                                   Queue<Cell> queue,
                                   int row,
                                   int col,
                                   int previousHeight,
                                   int rows,
                                   int cols) {

            if (row < 0
                    || row >= rows
                    || col < 0
                    || col >= cols) {
                return;
            }

            if (reachable[row][col]) {
                return;
            }

            if (heights[row][col] < previousHeight) {
                return;
            }

            reachable[row][col] = true;
            queue.offer(new Cell(row, col));
        }

        private void offerIfNew(Queue<Cell> queue,
                                boolean[][] reachable,
                                int row,
                                int col) {

            if (reachable[row][col]) {
                return;
            }

            reachable[row][col] = true;
            queue.offer(new Cell(row, col));
        }
    }

    /**
     * ========================================================================
     * 9. DELTA MAP — SAME ENGINE, DIFFERENT PROBLEM
     * ========================================================================
     *
     * Problem                 Sources            Neighbor Rule              Result
     * ------------------------------------------------------------------------------
     * Pacific Atlantic        2 ocean borders    nextHeight >= current      intersection
     * Surrounded Regions      border O           adjacent O                 protect region
     * Number of Enclaves      border land        adjacent land              count complement
     * Number of Islands       each unseen land   adjacent land              count components
     * Provinces               each unseen city   matrix edge                count components
     *
     * IMPORTANT:
     *
     * The first three are naturally SOURCE-SET REACHABILITY problems.
     * Islands/Provinces are COMPONENT-CONSUMPTION problems.
     *
     * Same DFS/BFS mechanics can appear in both families, but the objective is
     * different. Do not classify only by syntax.
     */

    /**
     * ========================================================================
     * 10. PATTERN BOUNDARIES / SHIFTS
     * ========================================================================
     *
     * ------------------------------------------------------------------------
     * NUMBER OF ISLANDS / NUMBER OF PROVINCES
     * ------------------------------------------------------------------------
     *
     * Question:
     *
     *     "How many connected components exist?"
     *
     * Shape:
     *
     *     unseen node
     *         ↓
     *     DFS/BFS consumes component
     *         ↓
     *     count++
     *
     * Pacific Atlantic does NOT consume independent components.
     * It computes membership in reachability sets.
     *
     * ------------------------------------------------------------------------
     * BIPARTITE GRAPH
     * ------------------------------------------------------------------------
     *
     * DFS/BFS engine reused, but state means COLOR rather than reachability.
     *
     *     RED xor BLUE
     *
     * Pacific Atlantic allows a node to be in BOTH sets:
     *
     *     Pacific = true
     *     Atlantic = true     <-- desired
     *
     * ------------------------------------------------------------------------
     * WORD SEARCH
     * ------------------------------------------------------------------------
     *
     * Same 4-direction DFS mechanics, different state semantics.
     *
     * Pacific Atlantic:
     *
     *     visited/reachable is PERMANENT
     *     no undo
     *
     * Word Search:
     *
     *     visited means "used in current candidate path"
     *     temporary
     *     CHOOSE -> EXPLORE -> UNDO
     *
     * Therefore Word Search is backtracking, not ordinary reachability.
     *
     * ------------------------------------------------------------------------
     * ROTTING ORANGES
     * ------------------------------------------------------------------------
     *
     * Multiple sources again, but TIME/LAYERS matter.
     *
     * Therefore:
     *
     *     multi-source BFS
     *
     * is preferred because BFS naturally processes distance/time layers.
     *
     * ------------------------------------------------------------------------
     * UNION FIND
     * ------------------------------------------------------------------------
     *
     * DSU answers component membership under merging relationships.
     * Pacific Atlantic has directional height-constrained reachability.
     * DSU loses that direction information, so it is not the right model.
     */

    /**
     * ========================================================================
     * 11. INTERVIEW RECONSTRUCTION SHEET
     * ========================================================================
     *
     * Recognition cue:
     *
     *     "Which cells can reach BOTH destinations?"
     *
     * First thought:
     *
     *     two reachability sets + intersection
     *
     * Optimization cue:
     *
     *     Many cells -> few destinations
     *     Ask whether edges can be reversed.
     *
     * Original movement:
     *
     *     high -> low
     *
     * Reverse traversal:
     *
     *     low -> high/equal
     *
     * Mechanical reconstruction:
     *
     *     1. rows / cols
     *     2. pacificReachable[][]
     *     3. atlanticReachable[][]
     *     4. DFS from left + top borders
     *     5. DFS from right + bottom borders
     *     6. DFS guard:
     *            out of bounds
     *            already reachable
     *            current height < previous height
     *     7. mark reachable
     *     8. recurse 4 directions
     *     9. scan grid
     *    10. add cells marked by BOTH
     *
     * One-liner:
     *
     *     Reverse the water flow from each ocean and intersect the two visited sets.
     *
     * Common traps:
     *
     *     - traversing downward from oceans instead of upward/equal
     *     - starting DFS from every cell
     *     - confusing two reachability flags with bipartite coloring
     *     - treating it as connected-component counting
     *     - forgetting that both ocean borders are multi-source sets
     *
     * Complexity:
     *
     *     Time  O(rows * cols)
     *     Space O(rows * cols)
     */

    /**
     * ========================================================================
     * 12. MAIN + SELF-VERIFYING TESTS
     * ========================================================================
     */
    public static void main(String[] args) {

        PrimaryReverseDFS dfsSolver = new PrimaryReverseDFS();
        AlternativeReverseBFS bfsSolver = new AlternativeReverseBFS();

        testOfficialExample(dfsSolver, bfsSolver);
        testSingleCell(dfsSolver, bfsSolver);
        testFlatGrid(dfsSolver, bfsSolver);
        testStrictSlope(dfsSolver, bfsSolver);
        testSurroundedRegions();
        testNumberOfEnclaves();

        System.out.println("All Pacific Atlantic Java Gold assertions passed.");
    }

    private static void testOfficialExample(
            PrimaryReverseDFS dfsSolver,
            AlternativeReverseBFS bfsSolver) {

        int[][] heights = {
                {1, 2, 2, 3, 5},
                {3, 2, 3, 4, 4},
                {2, 4, 5, 3, 1},
                {6, 7, 1, 4, 5},
                {5, 1, 1, 2, 4}
        };

        List<List<Integer>> expected = List.of(
                List.of(0, 4),
                List.of(1, 3),
                List.of(1, 4),
                List.of(2, 2),
                List.of(3, 0),
                List.of(3, 1),
                List.of(4, 0)
        );

        assertCoordinatesEqual(
                expected,
                dfsSolver.pacificAtlantic(heights),
                "Official example DFS"
        );

        assertCoordinatesEqual(
                expected,
                bfsSolver.pacificAtlantic(heights),
                "Official example BFS"
        );
    }

    private static void testSingleCell(
            PrimaryReverseDFS dfsSolver,
            AlternativeReverseBFS bfsSolver) {

        int[][] heights = {
                {7}
        };

        List<List<Integer>> expected = List.of(
                List.of(0, 0)
        );

        assertCoordinatesEqual(
                expected,
                dfsSolver.pacificAtlantic(heights),
                "Single cell DFS"
        );

        assertCoordinatesEqual(
                expected,
                bfsSolver.pacificAtlantic(heights),
                "Single cell BFS"
        );
    }

    private static void testFlatGrid(
            PrimaryReverseDFS dfsSolver,
            AlternativeReverseBFS bfsSolver) {

        int[][] heights = {
                {2, 2},
                {2, 2}
        };

        List<List<Integer>> expected = List.of(
                List.of(0, 0),
                List.of(0, 1),
                List.of(1, 0),
                List.of(1, 1)
        );

        assertCoordinatesEqual(
                expected,
                dfsSolver.pacificAtlantic(heights),
                "Flat grid DFS"
        );

        assertCoordinatesEqual(
                expected,
                bfsSolver.pacificAtlantic(heights),
                "Flat grid BFS"
        );
    }

    private static void testStrictSlope(
            PrimaryReverseDFS dfsSolver,
            AlternativeReverseBFS bfsSolver) {

        int[][] heights = {
                {1, 2, 3},
                {2, 3, 4},
                {3, 4, 5}
        };

        List<List<Integer>> expected = List.of(
                List.of(0, 2),
                List.of(1, 2),
                List.of(2, 0),
                List.of(2, 1),
                List.of(2, 2)
        );

        assertCoordinatesEqual(
                expected,
                dfsSolver.pacificAtlantic(heights),
                "Increasing slope DFS"
        );

        assertCoordinatesEqual(
                expected,
                bfsSolver.pacificAtlantic(heights),
                "Increasing slope BFS"
        );
    }

    private static void testSurroundedRegions() {

        char[][] board = {
                {'X', 'X', 'X', 'X'},
                {'X', 'O', 'O', 'X'},
                {'X', 'X', 'O', 'X'},
                {'X', 'O', 'X', 'X'}
        };

        char[][] expected = {
                {'X', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X'},
                {'X', 'O', 'X', 'X'}
        };

        new SurroundedRegions().solve(board);

        if (!Arrays.deepEquals(board, expected)) {
            throw new AssertionError("Surrounded Regions delta failed.");
        }
    }

    private static void testNumberOfEnclaves() {

        int[][] grid = {
                {0, 0, 0, 0},
                {1, 0, 1, 0},
                {0, 1, 1, 0},
                {0, 0, 0, 0}
        };

        int actual = new NumberOfEnclaves().numEnclaves(grid);

        if (actual != 3) {
            throw new AssertionError(
                    "Number of Enclaves delta failed. Expected 3 but got " + actual
            );
        }
    }

    private static void assertCoordinatesEqual(
            List<List<Integer>> expected,
            List<List<Integer>> actual,
            String message) {

        Set<List<Integer>> expectedSet = new HashSet<>(expected);
        Set<List<Integer>> actualSet = new HashSet<>(actual);

        if (!expectedSet.equals(actualSet)) {
            throw new AssertionError(
                    message
                            + " | expected=" + expectedSet
                            + " actual=" + actualSet
            );
        }
    }
}
