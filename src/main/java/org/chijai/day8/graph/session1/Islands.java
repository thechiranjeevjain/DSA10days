package org.chijai.day8.graph.session1;

import java.util.*;

/**
 * ============================================================================
 * NUMBER OF ISLANDS — JAVA GOLD
 * ============================================================================
 *
 * Repository rule:
 *
 *     LEARN THE BASE ENGINE ONCE.
 *     FOR EACH RELATED PROBLEM, STORE ONLY THE DELTA.
 *
 * ============================================================================
 * 1. PROBLEM STATEMENT
 * ============================================================================
 *
 * Given an m x n grid containing:
 *
 *     '1' = land
 *     '0' = water
 *
 * return the number of islands.
 *
 * Land cells belong to the same island when they are connected horizontally
 * or vertically. Diagonal touching does NOT connect islands.
 *
 * Example:
 *
 *     1 1 0 0 0
 *     1 1 0 0 0
 *     0 0 1 0 0
 *     0 0 0 1 1
 *
 * Connected components:
 *
 *     {top-left block}
 *     {middle cell}
 *     {bottom-right pair}
 *
 * Answer = 3
 *
 * ============================================================================
 * 2. RECOGNITION + FIRST-PRINCIPLES INVENTION PATH
 * ============================================================================
 *
 * Question asks:
 *
 *     "How many independent connected groups exist?"
 *
 * Therefore:
 *
 *     CONNECTED COMPONENTS
 *
 * The grid is only the representation.
 * Conceptually it is a graph:
 *
 *     land cell       -> node
 *     valid adjacency -> edge
 *     island          -> connected component
 *
 *
 * FIRST-PRINCIPLES INVENTION:
 *
 * 1. Scanning the grid is unavoidable because any cell might start an island.
 *
 * 2. When we encounter fresh land, we know we discovered one new component.
 *
 * 3. Increment the answer ONCE.
 *
 * 4. DFS/BFS must consume every land cell reachable from that starting cell.
 *
 * 5. Consumed cells must become permanently visited so the outer scan can
 *    never count that same component again.
 *
 * Therefore the reusable component-counting skeleton is:
 *
 *     for every node:
 *         if node is valid and unvisited:
 *             components++
 *             dfs/bfs(node)
 *
 * ---------------------------------------------------------------------------
 * NUMBER OF ISLANDS — TWO DELTAS
 * ---------------------------------------------------------------------------
 *
 * SAME ENGINE:
 *
 *     Connected Components DFS/BFS
 *
 * Δ1 — WHAT COUNTS AS A NEIGHBOR / SAME COMPONENT?
 *
 *     4-direction adjacent land cell
 *
 * Δ2 — WHAT DO WE DO WITH ONE COMPLETE COMPONENT?
 *
 *     islandCount++ exactly once
 *
 * "Flood Fill" is only a grid/image nickname for this DFS/BFS traversal.
 * It is NOT another algorithm to memorize.
 *
 * ============================================================================
 * 3. REUSABLE BASE ENGINE — CONNECTED COMPONENTS DFS/BFS
 * ============================================================================
 *
 *     for every node:
 *         if node is valid and unvisited:
 *             componentCount++          // Δ2
 *             dfs/bfs(node)
 *
 *     dfs/bfs(node):
 *         mark node visited
 *         for each valid neighbor:       // Δ1
 *             dfs/bfs(neighbor)
 *
 * SAME BOILERPLATE ACROSS THE FAMILY.
 * ONLY Δ1 AND Δ2 CHANGE.
 */
public class Islands {

    private static final int[][] DIRECTIONS = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };

    /**
     * ========================================================================
     * 4. ⭐ PRIMARY SOLUTION — DFS CONNECTED COMPONENTS, IN-PLACE VISITED STATE
     * ========================================================================
     *
     * Why primary?
     *
     * - optimal O(rows * cols) traversal
     * - shortest reconstruction path
     * - no separate visited[][] needed
     * - directly exposes the connected-components invariant
     *
     * Trade-off:
     *
     * - destroys the input by changing visited land from '1' to '0'
     * - recursive DFS can use O(rows * cols) stack in the worst case
     *
     * "Flood fill" here is just a descriptive nickname for this DFS spreading
     * through neighboring cells. The algorithm is ordinary DFS.
     */
    static class PrimaryDFSInPlace {

        public int numIslands(char[][] grid) {

            if (grid == null || grid.length == 0) {
                return 0;
            }

            int rows = grid.length;
            int cols = grid[0].length;
            int islandCount = 0;

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (grid[row][col] == '1') {

                        /*
                         * Fresh land means a new connected component starts here.
                         * DFS consumes that entire component before scanning resumes.
                         */
                        islandCount++;

                        dfs(grid, row, col);
                    }
                }
            }

            return islandCount;
        }

        private void dfs(char[][] grid,
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

            if (grid[row][col] != '1') {
                return;
            }

            /*
             * Mark BEFORE exploring neighbors.
             *
             * This is the visited invariant:
             * once a cell is claimed by one component traversal,
             * no future traversal may claim it again.
             */
            grid[row][col] = '0';

            // Fixed 4-neighbor grid: explicit calls are easier to reconstruct.
            dfs(grid, row - 1, col); // up
            dfs(grid, row + 1, col); // down
            dfs(grid, row, col - 1); // left
            dfs(grid, row, col + 1); // right
        }
    }

    /**
     * ========================================================================
     * 5. 🔁 PATTERN REUSE — LEARN ONCE, STORE ONLY THE DELTA
     * ========================================================================
     *
     * BASE ENGINE — CONNECTED COMPONENTS DFS/BFS
     *
     *     for every node:
     *         if valid and unvisited:
     *             components++
     *             dfs/bfs(node)
     *
     *     dfs/bfs(node):
     *         mark visited
     *         traverse valid neighbors
     *
     * DO NOT memorize a new DFS algorithm for every problem.
     * Keep the traversal engine fixed and identify only the problem delta.
     *
     *     Δ1. What defines a valid neighbor / same component?
     *     Δ2. What do we need from the completed component?
     *
     * The compact runnable examples live HERE, immediately beside the primary
     * solution, so the sameness and the delta are visible together.
     *
     * ------------------------------------------------------------------------
     * Δ NUMBER OF ISLANDS — PRIMARY ABOVE
     * ------------------------------------------------------------------------
     *
     * representation = grid
     * node           = land cell
     * neighbors      = 4-direction land cells
     * visited        = grid mutation or boolean[][]
     * result         = component count
     *
     * Δ1 = adjacent 4-direction land
     * Δ2 = one completed component -> islandCount++
     *
     * Code: PrimaryDFSInPlace directly above.
     */

    /**
     * ------------------------------------------------------------------------
     * Δ NUMBER OF PROVINCES
     * ------------------------------------------------------------------------
     *
     * representation = adjacency matrix
     * node           = city
     * neighbors      = every j where isConnected[city][j] == 1
     * visited        = boolean[]
     * result         = component count
     *
     * Δ1 = adjacency-matrix edge
     * Δ2 = one completed component -> provinceCount++
     *
     * SAME DFS/BFS ALGORITHM AS NUMBER OF ISLANDS.
     * Only the graph representation / neighbor generator changed.
     *
     * Time  : O(N^2)
     * Space : O(N)
     */
    static class NumberOfProvinces {

        public int findCircleNum(int[][] isConnected) {

            int cities = isConnected.length;
            boolean[] visited = new boolean[cities];
            int provinceCount = 0;

            for (int city = 0; city < cities; city++) {

                if (!visited[city]) {
                    provinceCount++;
                    dfs(isConnected, visited, city);
                }
            }

            return provinceCount;
        }

        private void dfs(int[][] isConnected,
                         boolean[] visited,
                         int city) {

            visited[city] = true;

            for (int neighbor = 0;
                 neighbor < isConnected.length;
                 neighbor++) {

                if (isConnected[city][neighbor] == 1
                        && !visited[neighbor]) {

                    dfs(isConnected, visited, neighbor);
                }
            }
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Δ MAX AREA OF ISLAND
     * ------------------------------------------------------------------------
     *
     * representation = grid
     * node           = land cell
     * neighbors      = same 4-direction land cells
     * visited        = grid mutation
     * result         = maximum component size
     *
     * Δ1 = unchanged from Number of Islands
     * Δ2 = DFS RETURNS component size instead of merely consuming it
     *
     * Notice how the four recursive calls stay the same.
     */
    static class MaxAreaOfIsland {

        public int maxAreaOfIsland(int[][] grid) {

            int rows = grid.length;
            int cols = grid[0].length;
            int maxArea = 0;

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (grid[row][col] == 1) {
                        maxArea = Math.max(maxArea, dfs(grid, row, col));
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

            if (row < 0
                    || row >= rows
                    || col < 0
                    || col >= cols
                    || grid[row][col] != 1) {
                return 0;
            }

            grid[row][col] = 0;

            return 1
                    + dfs(grid, row - 1, col)
                    + dfs(grid, row + 1, col)
                    + dfs(grid, row, col - 1)
                    + dfs(grid, row, col + 1);
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Δ FLOOD FILL IMAGE PROBLEM
     * ------------------------------------------------------------------------
     *
     * representation = image grid
     * node           = pixel
     * neighbors      = 4-direction pixels with same original color
     * visited        = recoloring itself encodes visited
     * result         = transformed component
     *
     * Δ1 = valid neighbor must have the original color
     * Δ2 = recolor every node in the component
     *
     * "Flood Fill" is the problem/application name here.
     * The traversal engine is still ordinary DFS/BFS.
     */
    static class FloodFillImage {

        public int[][] floodFill(int[][] image,
                                 int startRow,
                                 int startCol,
                                 int newColor) {

            int originalColor = image[startRow][startCol];

            if (originalColor == newColor) {
                return image;
            }

            dfs(image, startRow, startCol, originalColor, newColor);

            return image;
        }

        private void dfs(int[][] image,
                         int row,
                         int col,
                         int originalColor,
                         int newColor) {

            int rows = image.length;
            int cols = image[0].length;

            if (row < 0
                    || row >= rows
                    || col < 0
                    || col >= cols
                    || image[row][col] != originalColor) {
                return;
            }

            image[row][col] = newColor;

            dfs(image, row - 1, col, originalColor, newColor);
            dfs(image, row + 1, col, originalColor, newColor);
            dfs(image, row, col - 1, originalColor, newColor);
            dfs(image, row, col + 1, originalColor, newColor);
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Δ SURROUNDED REGIONS
     * ------------------------------------------------------------------------
     *
     * representation = board grid
     * node           = 'O' cell
     * neighbors      = adjacent 'O' cells
     * visited        = temporary protected marker '#'
     * result         = preserve border-connected components; flip the rest
     *
     * Δ1 = same 4-direction traversal
     * Δ2 = STARTING POINT changes: begin from borders because those O's are safe
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
     * Δ CLOSED ISLANDS
     * ------------------------------------------------------------------------
     *
     * representation = grid
     * node           = land cell (0 in this problem)
     * neighbors      = adjacent land cells
     * visited        = mutation to -1
     * result         = count components that never escape the boundary
     *
     * Δ1 = same 4-direction traversal
     * Δ2 = DFS RETURNS a component-level boolean: did it remain closed?
     *
     * IMPORTANT:
     * Evaluate all four recursive calls BEFORE combining them. Using a single
     * short-circuit expression could stop traversal after the first false and
     * leave part of the component unvisited.
     */
    static class NumberOfClosedIslands {

        public int closedIsland(int[][] grid) {

            int rows = grid.length;
            int cols = grid[0].length;
            int closedCount = 0;

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (grid[row][col] == 0
                            && dfs(grid, row, col)) {
                        closedCount++;
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

            if (row < 0
                    || row >= rows
                    || col < 0
                    || col >= cols) {
                return false;
            }

            if (grid[row][col] == 1
                    || grid[row][col] == -1) {
                return true;
            }

            grid[row][col] = -1;

            boolean upClosed = dfs(grid, row - 1, col);
            boolean downClosed = dfs(grid, row + 1, col);
            boolean leftClosed = dfs(grid, row, col - 1);
            boolean rightClosed = dfs(grid, row, col + 1);

            return upClosed
                    && downClosed
                    && leftClosed
                    && rightClosed;
        }
    }

    /**
     * ------------------------------------------------------------------------
     * DELTA MAP — SAME ENGINE, DIFFERENT PLUG-IN RULES
     * ------------------------------------------------------------------------
     *
     * Problem                 Δ1: neighbor / membership       Δ2: component result
     * ---------------------   ------------------------------   -----------------------------
     * Number of Islands       4-dir land                      count component
     * Number of Provinces     adjacency-matrix edge           count component
     * Max Area of Island      4-dir land                      return component size
     * Flood Fill              same original color             recolor component
     * Surrounded Regions      adjacent 'O'                    protect border component
     * Closed Islands          adjacent land                   return closed/open property
     *
     * MEMORY RULE:
     *
     *     ONE DFS/BFS COMPONENT ENGINE.
     *     MANY PROBLEMS = SMALL DELTAS AROUND THAT ENGINE.
     */

    /**
     * ========================================================================
     * 6. PRIMARY VISUAL DRY RUN — WHAT DFS IS ACTUALLY DOING
     * ========================================================================
     *
     * Grid:
     *
     *     1 1 0 0
     *     1 0 0 1
     *     0 0 1 1
     *
     * Outer scan reaches (0,0):
     *
     *     islandCount = 1
     *
     * DFS component traversal:
     *
     *     (0,0) ---- (0,1)
     *        |
     *      (1,0)
     *
     * All three become visited/0.
     *
     * The outer scan later reaches (1,3):
     *
     *     islandCount = 2
     *
     * DFS:
     *
     *     (1,3)
     *        |
     *     (2,3) ---- (2,2)
     *
     * End result:
     *
     *     two DFS traversal starts
     *     = two connected components
     *     = two islands
     *
     * CORE MEMORY PEG:
     *
     *     NEW UNVISITED NODE
     *            ↓
     *       components++
     *            ↓
     *     CONSUME EVERYTHING REACHABLE
     */

    /**
     * ========================================================================
     * 7. COMPLEXITY DERIVATION — PRIMARY DFS
     * ========================================================================
     *
     * Let:
     *
     *     R = rows
     *     C = cols
     *     V = R * C cells
     *
     * Outer scan examines every cell once.
     *
     * A land cell becomes '0' the first time DFS processes it, therefore it can
     * never be processed as fresh land again.
     *
     * Each processed cell checks exactly four directions.
     * Four is constant.
     *
     * Time:
     *
     *     O(R * C)
     *
     * Space:
     *
     *     no visited[][]       = O(1) explicit state
     *     recursion stack      = O(R * C) worst case
     *
     *     overall auxiliary    = O(R * C) worst case
     *
     * Saying O(1) total space would incorrectly ignore the recursion stack.
     */

    /**
     * ========================================================================
     * 8. ALTERNATIVES + TRADE-OFFS
     *
     * ALTERNATIVE #1 — BFS CONNECTED COMPONENTS
     * ========================================================================
     *
     * SAME algorithmic family.
     *
     * The only engine change is:
     *
     *     DFS recursion stack
     *             ↓
     *     BFS explicit queue
     *
     * BFS is not asymptotically faster here because shortest path/layers do not
     * matter. Both DFS and BFS simply need to consume the whole component.
     *
     * Time  : O(R * C)
     * Space : O(R * C) worst case
     */
    static class BFSWithVisited {

        private record Cell(int row, int col) {
        }

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

                    if (grid[row][col] == '1'
                            && !visited[row][col]) {

                        islandCount++;
                        bfs(grid, visited, row, col);
                    }
                }
            }

            return islandCount;
        }

        private void bfs(char[][] grid,
                         boolean[][] visited,
                         int startRow,
                         int startCol) {

            int rows = grid.length;
            int cols = grid[0].length;

            Queue<Cell> queue = new ArrayDeque<>();

            /*
             * Mark when offered, not when polled.
             * This prevents duplicate queue insertion.
             */
            visited[startRow][startCol] = true;
            queue.offer(new Cell(startRow, startCol));

            while (!queue.isEmpty()) {

                Cell current = queue.poll();

                for (int[] direction : DIRECTIONS) {

                    int neighborRow = current.row() + direction[0];
                    int neighborCol = current.col() + direction[1];

                    if (neighborRow < 0
                            || neighborRow >= rows
                            || neighborCol < 0
                            || neighborCol >= cols) {
                        continue;
                    }

                    if (grid[neighborRow][neighborCol] != '1') {
                        continue;
                    }

                    if (visited[neighborRow][neighborCol]) {
                        continue;
                    }

                    visited[neighborRow][neighborCol] = true;
                    queue.offer(new Cell(neighborRow, neighborCol));
                }
            }
        }
    }

    /**
     * ========================================================================
     * ALTERNATIVE #2 — UNION FIND / DSU
     * ========================================================================
     *
     * DSU is valid, but NOT the primary solution for the static Number of
     * Islands problem.
     *
     * Why DFS/BFS is preferred here:
     *
     *     grid already exposes local neighbors directly
     *     DFS/BFS naturally consumes a static component
     *     less machinery
     *
     * DSU becomes especially attractive when connectivity changes over time,
     * such as Number of Islands II where land is added incrementally.
     *
     * Static Number of Islands:
     *
     *     DFS/BFS
     *         Time  O(R*C)
     *         Space O(R*C) worst case
     *
     *     DSU
     *         Time  O(R*C * alpha(R*C))
     *         Space O(R*C)
     *
     * alpha is inverse Ackermann and practically constant, but DFS/BFS remains
     * the simpler primary reconstruction.
     */
    static class UnionFindAlternative {

        public int numIslands(char[][] grid) {

            if (grid == null || grid.length == 0) {
                return 0;
            }

            int rows = grid.length;
            int cols = grid[0].length;
            int landComponents = 0;

            UnionFind unionFind = new UnionFind(rows * cols);

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (grid[row][col] == '1') {
                        landComponents++;
                    }
                }
            }

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (grid[row][col] != '1') {
                        continue;
                    }

                    int current = toNode(row, col, cols);

                    /*
                     * Only RIGHT and DOWN are needed.
                     * LEFT/UP represent the same undirected edges again.
                     */
                    if (row + 1 < rows && grid[row + 1][col] == '1') {

                        int down = toNode(row + 1, col, cols);

                        if (unionFind.union(current, down)) {
                            landComponents--;
                        }
                    }

                    if (col + 1 < cols && grid[row][col + 1] == '1') {

                        int right = toNode(row, col + 1, cols);

                        if (unionFind.union(current, right)) {
                            landComponents--;
                        }
                    }
                }
            }

            return landComponents;
        }

        private int toNode(int row,
                           int col,
                           int cols) {

            return row * cols + col;
        }
    }

    /**
     * Reusable DSU engine.
     *
     * Not specific to islands.
     */
    static class UnionFind {

        private final int[] parent;
        private final int[] size;

        UnionFind(int n) {

            parent = new int[n];
            size = new int[n];

            for (int node = 0; node < n; node++) {
                parent[node] = node;
                size[node] = 1;
            }
        }

        int find(int node) {

            if (parent[node] == node) {
                return node;
            }

            parent[node] = find(parent[node]);
            return parent[node];
        }

        boolean union(int a,
                      int b) {

            int rootA = find(a);
            int rootB = find(b);

            if (rootA == rootB) {
                return false;
            }

            if (size[rootA] < size[rootB]) {
                int temp = rootA;
                rootA = rootB;
                rootB = temp;
            }

            parent[rootB] = rootA;
            size[rootA] += size[rootB];

            return true;
        }
    }

    /**
     * ========================================================================
     * 9. PATTERN BOUNDARIES / SHIFTS
     * ========================================================================
     *
     * These problems reuse traversal mechanics but CHANGE the objective enough
     * that they should not be stored as ordinary connected-component deltas.
     *
     * ------------------------------------------------------------------------
     * PATTERN SHIFT — ROTTING ORANGES
     * ========================================================================
     *
     * Reuses grid-neighbor traversal, but NOT the connected-component-counting
     * objective.
     *
     * Why BFS specifically?
     *
     *     We need minimum elapsed time / layers.
     *     All initially rotten oranges are simultaneous sources.
     *
     * Therefore:
     *
     *     multi-source BFS
     */
    static class RottingOranges {

        private record Cell(int row, int col) {
        }

        public int orangesRotting(int[][] grid) {

            int rows = grid.length;
            int cols = grid[0].length;

            Queue<Cell> queue = new ArrayDeque<>();
            int fresh = 0;

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (grid[row][col] == 2) {
                        queue.offer(new Cell(row, col));
                    } else if (grid[row][col] == 1) {
                        fresh++;
                    }
                }
            }

            int minutes = 0;

            while (!queue.isEmpty() && fresh > 0) {

                int levelSize = queue.size();

                for (int i = 0; i < levelSize; i++) {

                    Cell current = queue.poll();

                    for (int[] direction : DIRECTIONS) {

                        int neighborRow = current.row() + direction[0];
                        int neighborCol = current.col() + direction[1];

                        if (neighborRow < 0
                                || neighborRow >= rows
                                || neighborCol < 0
                                || neighborCol >= cols
                                || grid[neighborRow][neighborCol] != 1) {
                            continue;
                        }

                        grid[neighborRow][neighborCol] = 2;
                        fresh--;

                        queue.offer(new Cell(neighborRow, neighborCol));
                    }
                }

                minutes++;
            }

            return fresh == 0 ? minutes : -1;
        }
    }

    /**
     * ========================================================================
     * PATTERN SHIFT — PACIFIC ATLANTIC WATER FLOW
     * ========================================================================
     *
     * NOT connected-component counting.
     *
     * Question:
     *
     *     Which cells can reach BOTH destination sets?
     *
     * Naive direction:
     *
     *     every cell -> try to reach each ocean
     *
     * Better direction:
     *
     *     Pacific borders  -> reverse traversal uphill
     *     Atlantic borders -> reverse traversal uphill
     *
     * A reverse traversal may move from current cell to neighbor when:
     *
     *     neighborHeight >= currentHeight
     *
     * because in the real water-flow direction that neighbor could flow down to
     * the current cell.
     *
     * Answer:
     *
     *     pacificReachable ∩ atlanticReachable
     *
     * Time  : O(R*C)
     * Space : O(R*C)
     */
    static class PacificAtlanticWaterFlow {

        public List<List<Integer>> pacificAtlantic(int[][] heights) {

            int rows = heights.length;
            int cols = heights[0].length;

            boolean[][] pacificReachable = new boolean[rows][cols];
            boolean[][] atlanticReachable = new boolean[rows][cols];

            for (int row = 0; row < rows; row++) {
                dfs(heights, pacificReachable, row, 0);
                dfs(heights, atlanticReachable, row, cols - 1);
            }

            for (int col = 0; col < cols; col++) {
                dfs(heights, pacificReachable, 0, col);
                dfs(heights, atlanticReachable, rows - 1, col);
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
                         int col) {

            if (reachable[row][col]) {
                return;
            }

            reachable[row][col] = true;

            int rows = heights.length;
            int cols = heights[0].length;

            for (int[] direction : DIRECTIONS) {

                int neighborRow = row + direction[0];
                int neighborCol = col + direction[1];

                if (neighborRow < 0
                        || neighborRow >= rows
                        || neighborCol < 0
                        || neighborCol >= cols) {
                    continue;
                }

                /*
                 * Reverse the original water-flow rule.
                 *
                 * Real flow:
                 *     high -> low
                 *
                 * Reverse search from ocean:
                 *     low -> high/equal
                 */
                if (heights[neighborRow][neighborCol]
                        < heights[row][col]) {
                    continue;
                }

                dfs(
                        heights,
                        reachable,
                        neighborRow,
                        neighborCol
                );
            }
        }
    }

    /**
     * ========================================================================
     * 10. INTERVIEW ARTICULATION / RECONSTRUCTION SHEET
     * ========================================================================
     *
     * NUMBER OF ISLANDS
     *
     * Trigger:
     *     count connected groups
     *
     * Base pattern:
     *     connected components DFS/BFS
     *
     * Δ1 — neighbor / membership rule:
     *     4-direction adjacent land
     *
     * Δ2 — component result:
     *     islandCount++ once per fresh component
     *
     * Graph representation:
     *     implicit grid graph
     *
     * Node:
     *     land cell
     *
     * Neighbor rule:
     *     4-direction land
     *
     * Component-start rule:
     *     fresh land found during outer scan
     *
     * Component action:
     *     islandCount++ once, then consume entire component
     *
     * Visited rule:
     *     mark BEFORE exploring neighbors
     *
     * Common traps:
     *     count cells instead of components
     *     allow diagonals
     *     forget visited marking
     *     call recursive DFS before marking visited
     *     claim O(1) space while ignoring recursion stack
     *
     * DFS vs BFS:
     *     both work because traversal order does not matter
     *
     * DSU:
     *     valid alternative, but more machinery for this static grid
     *
     * Dynamic land additions:
     *     DSU becomes much more attractive
     *
     * ONE-LINER:
     *
     *     Every fresh land cell starts exactly one DFS/BFS that consumes exactly
     *     one connected component.
     */

    /**
     * ========================================================================
     * 11. MAIN + SELF-VERIFYING TESTS
     * ========================================================================
     */
    public static void main(String[] args) {

        testPrimaryExamples();
        testDiagonalBoundary();
        testBFSAgreement();
        testDSUAgreement();
        testMaxAreaDelta();
        testFloodFillDelta();
        testProvinceDelta();
        testRottingOrangesBoundary();
        testPacificAtlanticBoundary();

        System.out.println("All Java Gold assertions passed.");
    }

    private static void testPrimaryExamples() {

        char[][] gridOne = {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'}
        };

        char[][] gridTwo = {
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };

        PrimaryDFSInPlace solver = new PrimaryDFSInPlace();

        assert solver.numIslands(copy(gridOne)) == 1;
        assert solver.numIslands(copy(gridTwo)) == 3;
    }

    private static void testDiagonalBoundary() {

        char[][] grid = {
                {'1', '0'},
                {'0', '1'}
        };

        assert new PrimaryDFSInPlace().numIslands(copy(grid)) == 2;
    }

    private static void testBFSAgreement() {

        char[][] grid = {
                {'1', '1', '0'},
                {'0', '1', '0'},
                {'1', '0', '1'}
        };

        int dfs = new PrimaryDFSInPlace().numIslands(copy(grid));
        int bfs = new BFSWithVisited().numIslands(copy(grid));

        assert dfs == 3;
        assert bfs == dfs;
    }

    private static void testDSUAgreement() {

        char[][] grid = {
                {'1', '1', '0'},
                {'0', '1', '0'},
                {'1', '0', '1'}
        };

        int dfs = new PrimaryDFSInPlace().numIslands(copy(grid));
        int dsu = new UnionFindAlternative().numIslands(copy(grid));

        assert dsu == dfs;
    }

    private static void testMaxAreaDelta() {

        int[][] grid = {
                {1, 1, 0},
                {1, 0, 0},
                {0, 1, 1}
        };

        assert new MaxAreaOfIsland().maxAreaOfIsland(grid) == 3;
    }

    private static void testFloodFillDelta() {

        int[][] image = {
                {1, 1, 1},
                {1, 1, 0},
                {1, 0, 1}
        };

        int[][] result = new FloodFillImage().floodFill(image, 1, 1, 2);

        assert result[0][0] == 2;
        assert result[2][2] == 1;
    }

    private static void testProvinceDelta() {

        int[][] isConnected = {
                {1, 1, 0},
                {1, 1, 0},
                {0, 0, 1}
        };

        assert new NumberOfProvinces().findCircleNum(isConnected) == 2;
    }

    private static void testRottingOrangesBoundary() {

        int[][] grid = {
                {2, 1, 1},
                {1, 1, 0},
                {0, 1, 1}
        };

        assert new RottingOranges().orangesRotting(grid) == 4;
    }

    private static void testPacificAtlanticBoundary() {

        int[][] heights = {
                {1, 2, 2, 3, 5},
                {3, 2, 3, 4, 4},
                {2, 4, 5, 3, 1},
                {6, 7, 1, 4, 5},
                {5, 1, 1, 2, 4}
        };

        List<List<Integer>> result =
                new PacificAtlanticWaterFlow().pacificAtlantic(heights);

        assert result.contains(List.of(0, 4));
        assert result.contains(List.of(4, 0));
        assert result.contains(List.of(1, 3));
    }

    private static char[][] copy(char[][] original) {

        char[][] copy = new char[original.length][];

        for (int row = 0; row < original.length; row++) {
            copy[row] = Arrays.copyOf(original[row], original[row].length);
        }

        return copy;
    }
}
