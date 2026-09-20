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
     * PHOTOGRAPHIC RULE:
     *
     *     Keep the complete primary solution together as one visual unit.
     *     Conceptual explanations come immediately after the code.
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

            if (heights[row][col] < previousHeight) {
                return;
            }

            reachable[row][col] = true;

            int currentHeight = heights[row][col];

            dfs(heights, reachable, row - 1, col, currentHeight);
            dfs(heights, reachable, row + 1, col, currentHeight);
            dfs(heights, reachable, row, col - 1, currentHeight);
            dfs(heights, reachable, row, col + 1, currentHeight);
        }
    }

    /**
     * ========================================================================
     * 5. PRIMARY SOLUTION — CONCEPTUAL EXPLANATION
     * ========================================================================
     *
     * WHY THIS IS PRIMARY
     *
     * - optimal O(rows * cols)
     * - directly expresses the reverse-reachability insight
     * - reuses ordinary DFS
     * - no graph construction
     * - easiest reconstruction once the reverse-edge trick is remembered
     *
     * ------------------------------------------------------------------------
     * A. PRIMARY INVARIANT
     * ------------------------------------------------------------------------
     *
     *     pacificReachable[row][col] == true
     *
     * means:
     *
     *     Starting from the Pacific boundary and walking along REVERSED valid
     *     water edges, we can reach this cell.
     *
     * Equivalently in the original direction:
     *
     *     Water from this cell can reach the Pacific.
     *
     * Same meaning for atlanticReachable.
     *
     * ------------------------------------------------------------------------
     * B. SOURCE SETS
     * ------------------------------------------------------------------------
     *
     * Pacific touches:
     *
     *     LEFT + TOP
     *
     * Atlantic touches:
     *
     *     RIGHT + BOTTOM
     *
     * Every touching border cell is a source.
     *
     * A source may be passed to dfs() more than once.
     *
     * Example:
     *
     *     top-left is both LEFT and TOP Pacific border.
     *
     * Also, an earlier source traversal may already have reached a later
     * border source.
     *
     * That is harmless because:
     *
     *     reachable[row][col] == true
     *         -> immediate return
     *
     * So a cell may receive multiple dfs() CALLS,
     * but it is EXPANDED at most once per ocean.
     *
     * ------------------------------------------------------------------------
     * C. WHY THE HEIGHT COMPARISON IS REVERSED
     * ------------------------------------------------------------------------
     *
     * Normal water flow:
     *
     *     high/equal -> low/equal
     *
     * We traverse backward from an ocean, so reverse movement is:
     *
     *     low/equal -> high/equal
     *
     * Therefore the current cell is valid only when:
     *
     *     currentHeight >= previousHeight
     *
     * Code rejects the opposite condition:
     *
     *     if (heights[row][col] < previousHeight) return;
     *
     * ------------------------------------------------------------------------
     * D. WHY THE INITIAL SOURCE COMPARES AGAINST ITSELF
     * ------------------------------------------------------------------------
     *
     * For a border source we call:
     *
     *     dfs(..., row, col, heights[row][col])
     *
     * There is no real previous cell yet, so we use the source's own height.
     *
     * The first comparison becomes:
     *
     *     cellHeight < cellHeight
     *
     * which is always false.
     *
     * Therefore the source is automatically accepted.
     *
     * Recursive calls then pass the actual currentHeight, so the same guard
     * becomes the real reverse-edge check for every neighbor.
     *
     * ------------------------------------------------------------------------
     * E. WHY ALREADY REACHABLE -> RETURN IS SAFE
     * ------------------------------------------------------------------------
     *
     * This problem asks BOOLEAN REACHABILITY:
     *
     *     can this cell reach this ocean?   true / false
     *
     * Once a cell becomes true, there is no better version of true.
     *
     * We do NOT care about:
     *
     *     minimum distance
     *     minimum cost
     *     shortest path
     *
     * Therefore an already-reachable cell never needs to be expanded again.
     *
     * Reusable distinction:
     *
     *     REACHABILITY
     *         -> boolean visited/reachable
     *         -> once true, done
     *
     *     MINIMUM / BEST VALUE
     *         -> distance/best state
     *         -> a later route may improve the value
     *         -> BFS / Dijkstra / relaxation as appropriate
     *
     * ------------------------------------------------------------------------
     * F. DFS PHOTOGRAPHIC SKELETON
     * ------------------------------------------------------------------------
     *
     *     bounds
     *     -> already reachable
     *     -> invalid reverse height
     *     -> mark reachable
     *     -> recurse four directions
     *
     * Fixed four-neighbor grids use explicit recursive calls here because they
     * are easier to reconstruct than extra direction-array machinery.
     */

    /**
     * ========================================================================
     * 6. 🔁 PATTERN REUSE — LEARN ONCE, STORE ONLY THE DELTA
     * ========================================================================
     *
     * BASE ENGINE — GRID REACHABILITY
     *
     *     choose source cells
     *         ↓
     *     DFS/BFS
     *         ↓
     *     reject invalid neighbors
     *         ↓
     *     mark accepted cells
     *         ↓
     *     use the reached / unreached cells to build the answer
     *
     * Do NOT memorize a brand-new DFS for every grid problem.
     *
     * Keep the stable traversal skeleton and ask only:
     *
     *     Δ1. What are my starting sources?
     *     Δ2. Which neighbors are allowed?
     *     Δ3. What does visited/reachable mean?
     *     Δ4. What do I do with the visited/unvisited cells afterward?
     *
     * ------------------------------------------------------------------------
     * PACIFIC ATLANTIC — REFERENCE PATTERN
     * ------------------------------------------------------------------------
     *
     * representation = height grid
     * sources        = TWO source sets: Pacific borders + Atlantic borders
     * neighbor rule  = reverse edge: nextHeight >= currentHeight
     * state          = boolean[][] reachable, one per ocean
     * result         = intersection of both reachable sets
     *
     * The related problems below keep much of this engine and change only
     * specific pieces.
     */

    /**
     * ========================================================================
     * RELATED PROBLEM 1 — SURROUNDED REGIONS
     * ========================================================================
     *
     * PROBLEM STATEMENT
     *
     * You are given an m x n board containing:
     *
     *     'X' = blocked cell
     *     'O' = open cell
     *
     * An 'O' region is captured when it is completely surrounded by 'X'.
     *
     * Any 'O' connected 4-directionally to a border 'O' cannot be captured,
     * because that region reaches the outside of the board.
     *
     * Modify the board in-place:
     *
     *     surrounded 'O' -> 'X'
     *     border-connected 'O' stays 'O'
     *
     * Example:
     *
     *     input
     *
     *         X X X X
     *         X O O X
     *         X X O X
     *         X O X X
     *
     *     The three O's in the middle are enclosed.
     *     The O at the bottom-left touches the border and is safe.
     *
     *     output
     *
     *         X X X X
     *         X X X X
     *         X X X X
     *         X O X X
     *
     * FIRST-PRINCIPLES IDEA
     *
     * Asking "is this O surrounded?" for every region is awkward.
     *
     * Reverse the perspective:
     *
     *     Which O cells are definitely NOT surrounded?
     *
     * Exactly the O cells reachable from a border O.
     *
     * So:
     *
     *     border O's
     *         ↓
     *     DFS through O's
     *         ↓
     *     mark them temporarily safe '#'
     *         ↓
     *     remaining O's are surrounded -> X
     *         ↓
     *     restore # -> O
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

            if (row < 0
                    || row >= rows
                    || col < 0
                    || col >= cols) {
                return;
            }

            if (board[row][col] != 'O') {
                return;
            }

            board[row][col] = '#';

            dfs(board, row - 1, col);
            dfs(board, row + 1, col);
            dfs(board, row, col - 1);
            dfs(board, row, col + 1);
        }
    }

    /**
     * SURROUNDED REGIONS — WHAT STAYED SAME / WHAT CHANGED
     *
     * SAME as Pacific Atlantic:
     *
     *     - grid is treated as a graph
     *     - 4-direction DFS
     *     - start from boundary source cells
     *     - repeated source calls are harmless after a cell is marked
     *     - use reachability from special sources to solve the whole grid
     *
     * DELTAS:
     *
     *     Δ1. ONE source family instead of TWO.
     *
     *         Pacific Atlantic:
     *             Pacific sources + Atlantic sources
     *
     *         Surrounded Regions:
     *             every border 'O'
     *
     *     Δ2. No height / directional-edge condition.
     *
     *         Pacific Atlantic neighbor:
     *             nextHeight >= currentHeight
     *
     *         Surrounded Regions neighbor:
     *             board[next] == 'O'
     *
     *     Δ3. No separate boolean[][] is required.
     *
     *         We reuse the board itself as visited state:
     *
     *             O -> #
     *
     *         '#' means:
     *             "this O is connected to the border and must survive"
     *
     *     Δ4. The answer is the COMPLEMENT of boundary reachability.
     *
     *         reached O   -> safe -> restore '# -> O'
     *         unreached O -> surrounded -> 'O -> X'
     *
     * Recognition sentence:
     *
     *     "Mark what can escape to the boundary; capture everything else."
     */

    /**
     * ========================================================================
     * RELATED PROBLEM 2 — NUMBER OF ENCLAVES
     * ========================================================================
     *
     * PROBLEM STATEMENT
     *
     * You are given an m x n binary grid:
     *
     *     0 = sea
     *     1 = land
     *
     * From a land cell you may move 4-directionally to another land cell.
     *
     * A land cell is an enclave when there is NO path through land from that
     * cell to any boundary cell of the grid.
     *
     * Return the NUMBER OF LAND CELLS that belong to enclaves.
     *
     * Example:
     *
     *     input
     *
     *         0 0 0 0
     *         1 0 1 0
     *         0 1 1 0
     *         0 0 0 0
     *
     * The land at (1,0) touches the border, so it can escape.
     *
     * The three connected land cells:
     *
     *         (1,2), (2,1), (2,2)
     *
     * cannot reach the border.
     *
     *     answer = 3
     *
     * FIRST-PRINCIPLES IDEA
     *
     * Do not inspect every interior land cell and ask whether it can escape.
     *
     * Start from land that ALREADY touches the boundary and remove everything
     * reachable from it.
     *
     *     border land
     *         ↓
     *     DFS through connected land
     *         ↓
     *     mark/remove it as 0
     *         ↓
     *     count the 1's that remain
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
     * NUMBER OF ENCLAVES — WHAT STAYED SAME / WHAT CHANGED
     *
     * SAME as Pacific Atlantic:
     *
     *     - grid graph
     *     - boundary source cells
     *     - 4-direction DFS
     *     - mark every cell reachable from the selected sources
     *     - final scan of the grid
     *
     * DELTAS from Pacific Atlantic:
     *
     *     Δ1. Only ONE source family:
     *
     *             border land cells
     *
     *     Δ2. Neighbor rule is simply:
     *
     *             grid[next] == 1
     *
     *         There is no reverse-height comparison.
     *
     *     Δ3. Visited state is stored by mutation:
     *
     *             reachable border land: 1 -> 0
     *
     *     Δ4. No intersection.
     *
     *         We want cells NOT reachable from the boundary.
     *
     *     Δ5. Output is a COUNT rather than coordinates or a modified board.
     *
     * Even closer transfer from Surrounded Regions:
     *
     *     Surrounded Regions:
     *         eliminate conceptually unsafe interior O's after protecting
     *         boundary-connected O's.
     *
     *     Number of Enclaves:
     *         physically eliminate boundary-connected land first, then count
     *         whatever interior land remains.
     *
     * Recognition sentence:
     *
     *     "Erase everything that can reach the boundary; count the remainder."
     */

    /**
     * ------------------------------------------------------------------------
     * GENERIC SOURCE-SET REACHABILITY SHAPE
     * ------------------------------------------------------------------------
     *
     * Pacific Atlantic:
     *
     *     sourcesA -> reachableA
     *     sourcesB -> reachableB
     *     answer = reachableA ∩ reachableB
     *
     * Surrounded Regions / Number of Enclaves:
     *
     *     boundary sources -> reachable/safe region
     *     answer uses the complement afterward
     *
     * Stable questions:
     *
     *     What are the sources?
     *     Which edges may I traverse?
     *     What does reached mean?
     *     Do I return reached, intersect reached sets, or use the complement?
     */

    /**
     * ========================================================================
     * 7. PRIMARY VISUAL DRY RUN
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
     * 8. COMPLEXITY DERIVATION
     * ========================================================================
     *
     * Let:
     *
     *     R = rows
     *     C = columns
     *     V = R * C cells
     *
     * A cell may receive multiple dfs() calls because border sources overlap
     * and one source traversal may reach another source.
     *
     * But reachable[][] makes every repeated call return immediately.
     *
     * Therefore each cell is EXPANDED at most once for Pacific and at most
     * once for Atlantic.
     *
     * Every expanded cell checks four neighbors.
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
     * 9. ALTERNATIVES + TRADE-OFFS
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
     * 10. DELTA MAP — SAME ENGINE, DIFFERENT PROBLEM
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
     * 11. RELATED PROBLEMS — WHERE THE PATTERN STAYS / WHERE IT SHIFTS
     * ========================================================================
     *
     * The goal of this section is NOT to memorize six more solutions.
     *
     * For each problem ask:
     *
     *     What part of Pacific Atlantic can I reuse?
     *     What exact delta changes the algorithm?
     *
     * ------------------------------------------------------------------------
     * A. NUMBER OF ISLANDS
     * ------------------------------------------------------------------------
     *
     * PROBLEM STATEMENT
     *
     * Given a binary character grid:
     *
     *     '1' = land
     *     '0' = water
     *
     * An island is a maximal group of horizontally/vertically connected land.
     * Return the number of islands.
     *
     * Example:
     *
     *     1 1 0 0
     *     1 1 0 0
     *     0 0 1 0
     *     0 0 0 1
     *
     * There are three disconnected land components.
     *
     *     answer = 3
     *
     * SAME:
     *
     *     - grid = graph
     *     - 4-direction DFS/BFS
     *     - visited prevents repeated expansion
     *
     * DELTA:
     *
     *     Pacific Atlantic has predefined source sets.
     *
     *     Number of Islands has NO special boundary source set.
     *     We scan the entire grid.
     *
     *     Every unseen land cell starts a NEW component:
     *
     *         unseen land
     *             ↓
     *         islands++
     *             ↓
     *         DFS consumes that whole island
     *
     * State meaning also changes:
     *
     *     Pacific Atlantic visited = reachable from a particular ocean
     *     Islands visited          = already assigned to some island
     *
     * Recognition sentence:
     *
     *     "Every unseen valid node starts one new connected component."
     *
     * ------------------------------------------------------------------------
     * B. NUMBER OF PROVINCES
     * ------------------------------------------------------------------------
     *
     * PROBLEM STATEMENT
     *
     * There are n cities represented by an n x n adjacency matrix isConnected.
     *
     *     isConnected[i][j] == 1
     *
     * means city i and city j are directly connected.
     * Connections are transitive: if A connects to B and B connects to C,
     * all three belong to the same province.
     *
     * Return the number of connected provinces.
     *
     * Example:
     *
     *     1 1 0
     *     1 1 0
     *     0 0 1
     *
     * Cities 0 and 1 form one province.
     * City 2 forms another.
     *
     *     answer = 2
     *
     * SAME as Number of Islands:
     *
     *     unseen node
     *         ↓
     *     count one component
     *         ↓
     *     DFS/BFS consumes every connected node
     *
     * DELTA from grid problems:
     *
     *     representation changes.
     *
     *     Grid:
     *         neighbors are up/down/left/right cells.
     *
     *     Provinces:
     *         neighbors of city i are all j where isConnected[i][j] == 1.
     *
     * Pacific Atlantic's boundary-source/intersection idea is gone.
     * Only the generic graph traversal machinery remains.
     *
     * Recognition sentence:
     *
     *     "Connected-component counting, but the graph is given as a matrix."
     *
     * ------------------------------------------------------------------------
     * C. BIPARTITE GRAPH
     * ------------------------------------------------------------------------
     *
     * PROBLEM STATEMENT
     *
     * Given an undirected graph, determine whether its vertices can be split
     * into two groups such that every edge connects vertices from opposite
     * groups.
     *
     * Example:
     *
     *     0 ----- 1
     *     |       |
     *     |       |
     *     3 ----- 2
     *
     * One valid coloring:
     *
     *     0 = RED
     *     1 = BLUE
     *     2 = RED
     *     3 = BLUE
     *
     *     answer = true
     *
     * SAME:
     *
     *     - DFS/BFS traversal
     *     - persistent per-node state
     *     - do not reprocess an already resolved node unnecessarily
     *
     * DELTA:
     *
     *     State is not boolean reachable/unreachable.
     *
     *     State is:
     *
     *         UNCOLORED / RED / BLUE
     *
     *     Traversing an edge imposes a constraint:
     *
     *         neighborColor must be opposite currentColor
     *
     *     If an already-colored neighbor has the SAME color:
     *
     *         contradiction -> false
     *
     * Contrast:
     *
     *     Pacific Atlantic:
     *         Pacific = true AND Atlantic = true is desirable.
     *
     *     Bipartite:
     *         RED and BLUE are mutually exclusive labels.
     *
     * Recognition sentence:
     *
     *     "Traversal with a coloring constraint, not plain reachability."
     *
     * ------------------------------------------------------------------------
     * D. WORD SEARCH
     * ------------------------------------------------------------------------
     *
     * PROBLEM STATEMENT
     *
     * Given a character board and a word, determine whether the word can be
     * formed by moving 4-directionally through adjacent cells.
     *
     * A board cell may be used at most once in the SAME candidate path.
     *
     * Example:
     *
     *     A B C E
     *     S F C S
     *     A D E E
     *
     *     word = "ABCCED"
     *     answer = true
     *
     * SAME:
     *
     *     - 4-direction grid DFS
     *     - boundary checking
     *     - some form of visited state
     *
     * CRITICAL DELTA:
     *
     *     Pacific Atlantic visited is PERMANENT for that traversal:
     *
     *         once reachable -> always reachable
     *         no undo
     *
     *     Word Search visited means:
     *
     *         "this cell is already used in THIS candidate path"
     *
     *     After that candidate branch finishes, the cell must become available
     *     again for another possible path.
     *
     * Therefore:
     *
     *     CHOOSE
     *       -> mark used
     *
     *     EXPLORE
     *       -> recurse for next character
     *
     *     UNDO
     *       -> unmark
     *
     * This changes the family from ordinary reachability to BACKTRACKING.
     *
     * Recognition sentence:
     *
     *     "Visited belongs to the current path, so it must be undone."
     *
     * ------------------------------------------------------------------------
     * E. ROTTING ORANGES
     * ------------------------------------------------------------------------
     *
     * PROBLEM STATEMENT
     *
     * A grid contains:
     *
     *     0 = empty
     *     1 = fresh orange
     *     2 = rotten orange
     *
     * Every minute, each rotten orange makes its 4-directionally adjacent
     * fresh oranges rotten.
     *
     * Return the minimum number of minutes until no fresh orange remains.
     * Return -1 if some fresh orange can never rot.
     *
     * Example:
     *
     *     2 1 1
     *     1 1 0
     *     0 1 1
     *
     * Minute 0 starts from the existing rotten orange.
     * Rot spreads one graph layer per minute.
     *
     *     answer = 4
     *
     * SAME:
     *
     *     - grid graph
     *     - 4-direction movement
     *     - MULTIPLE starting sources
     *
     * DELTA:
     *
     *     Pacific Atlantic only asks:
     *
     *         reachable or not?
     *
     *     Rotting Oranges asks:
     *
     *         what is the MINIMUM TIME / DISTANCE layer?
     *
     * Therefore source order now matters.
     * Seed ALL initially rotten oranges into one queue at time 0 and use
     * MULTI-SOURCE BFS.
     *
     * BFS processes:
     *
     *     distance 0
     *     distance 1
     *     distance 2
     *     ...
     *
     * This is exactly the kind of variation where boolean DFS alone is not
     * enough to answer the requested minimum time.
     *
     * Recognition sentence:
     *
     *     "Multiple sources + minimum number of equal-cost steps -> BFS."
     *
     * ------------------------------------------------------------------------
     * F. UNION FIND / DSU PROBLEMS
     * ------------------------------------------------------------------------
     *
     * Typical question shape:
     *
     *     relationships arrive as pairs / edges
     *     repeatedly merge groups
     *     ask whether two nodes belong to the same component
     *
     * Example family:
     *
     *     Accounts Merge
     *     Redundant Connection
     *     Number of Provinces
     *
     * SAME broad idea:
     *
     *     reason about connectivity / grouping.
     *
     * MAJOR DELTA:
     *
     *     DSU does not traverse directional paths.
     *     It merges nodes into UNDIRECTED equivalence classes.
     *
     * Pacific Atlantic has a directional condition:
     *
     *     reverse move allowed only when
     *         nextHeight >= currentHeight
     *
     * If two cells are placed in one DSU component, DSU cannot preserve the
     * fact that reachability may be valid in one direction but not the other.
     *
     * Therefore DSU is not the right primary model for Pacific Atlantic.
     *
     * Recognition sentence:
     *
     *     "Merging equivalence groups -> DSU; directional reachability -> DFS/BFS."
     */

    /**
     * ========================================================================
     * 12. INTERVIEW RECONSTRUCTION SHEET
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
     * 13. MAIN + SELF-VERIFYING TESTS
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
