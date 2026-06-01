package org.chijai.day8.session1;

import java.util.*;

/**
 * =====================================================================================
 * 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
 * =====================================================================================
 *
 * 🔗 Official Link:
 * https://leetcode.com/problems/rotting-oranges/
 *
 * Difficulty: Medium
 *
 * Tags:
 * Breadth-First Search (BFS)
 * Matrix
 * Queue
 * Graph
 *
 * Problem Statement:
 *
 * You are given an m x n grid where each cell can have one of three values:
 *
 * 0 representing an empty cell,
 * 1 representing a fresh orange, or
 * 2 representing a rotten orange.
 *
 * Every minute, any fresh orange that is 4-directionally adjacent to a rotten orange
 * becomes rotten.
 *
 * Return the minimum number of minutes that must elapse until no cell has a fresh orange.
 * If this is impossible, return -1.
 *
 * Example 1:
 *
 * Input: grid = [[2,1,1],[1,1,0],[0,1,1]]
 * Output: 4
 *
 * Example 2:
 *
 * Input: grid = [[2,1,1],[0,1,1],[1,0,1]]
 * Output: -1
 *
 * Explanation:
 * The orange in the bottom left corner (row 2, column 0) is never rotten,
 * because rotting only happens 4-directionally.
 *
 * Example 3:
 *
 * Input: grid = [[0,2]]
 * Output: 0
 *
 * Explanation:
 * Since there are already no fresh oranges at minute 0,
 * the answer is just 0.
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 10
 * grid[i][j] is 0, 1, or 2.
 *
 * =====================================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * =====================================================================================
 *
 * Pattern:
 * Multi-Source Breadth First Search (Multi-Source BFS)
 *
 * Problem Archetype:
 * "Spread / Infection / Wave Expansion Over Time"
 *
 * Core Invariant:
 *
 * All nodes processed in the same BFS layer rot at the SAME minute.
 *
 * Why It Works:
 *
 * BFS explores level-by-level.
 * Each BFS level naturally models:
 *
 * "everything affected after exactly X minutes"
 *
 * Since all initially rotten oranges start simultaneously,
 * they all become BFS sources together.
 *
 * When To Use:
 *
 * Use Multi-Source BFS when:
 *
 * • multiple starting points spread simultaneously
 * • shortest time / minimum steps in unweighted grid
 * • infection / fire / signal propagation
 * • nearest-distance expansion
 *
 * Recognition Signals:
 *
 * • "every minute"
 * • "adjacent cells become affected"
 * • minimum time to spread
 * • simultaneous expansion
 * • unweighted movement
 *
 * Difference vs Standard BFS:
 *
 * Standard BFS:
 * one starting node
 *
 * Multi-Source BFS:
 * many starting nodes pushed initially
 *
 * Difference vs DFS:
 *
 * DFS cannot guarantee minimum-time layering naturally.
 *
 * BFS preserves:
 * exact temporal layers.
 *
 * =====================================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * =====================================================================================
 *
 * Mental Model:
 *
 * Imagine rot spreading outward like expanding water waves.
 *
 * Minute 0:
 * initial rotten oranges
 *
 * Minute 1:
 * all neighbors rot
 *
 * Minute 2:
 * neighbors of newly rotten oranges rot
 *
 * and so on...
 *
 * BFS layer = one minute.
 *
 * -------------------------------------------------------------------------------------
 * 🟢 ALL INVARIANTS
 * -------------------------------------------------------------------------------------
 *
 * Invariant 1:
 *
 * Every orange inside the queue belongs to the SAME or FUTURE minute layer.
 *
 * Invariant 2:
 *
 * When processing one BFS layer:
 * all oranges in that layer rot neighbors simultaneously.
 *
 * Invariant 3:
 *
 * A fresh orange is added to queue EXACTLY ONCE.
 *
 * Why?
 *
 * The moment it becomes rotten,
 * we immediately mark grid[r][c] = 2.
 *
 * This prevents duplicate enqueue.
 *
 * Invariant 4:
 *
 * Remaining fresh count is always accurate.
 *
 * Each successful infection:
 * fresh--
 *
 * Invariant 5:
 *
 * If fresh > 0 after BFS ends:
 * some oranges were unreachable.
 *
 * -------------------------------------------------------------------------------------
 * Variable Meanings
 * -------------------------------------------------------------------------------------
 *
 * queue:
 * current frontier of rotten oranges
 *
 * fresh:
 * number of fresh oranges remaining
 *
 * minutes:
 * number of BFS layers completed
 *
 * directions:
 * 4-directional movement vectors
 *
 * -------------------------------------------------------------------------------------
 * Allowed Moves
 * -------------------------------------------------------------------------------------
 *
 * • move up/down/left/right
 * • infect only fresh oranges
 * • infect once
 *
 * -------------------------------------------------------------------------------------
 * Forbidden Moves
 * -------------------------------------------------------------------------------------
 *
 * • diagonal spread
 * • infect empty cells
 * • revisit already rotten cells
 * • enqueue same cell twice
 *
 * -------------------------------------------------------------------------------------
 * Termination Logic
 * -------------------------------------------------------------------------------------
 *
 * BFS stops when:
 *
 * • queue becomes empty
 *
 * Then:
 *
 * if fresh == 0:
 *      success
 *
 * else:
 *      unreachable fresh oranges exist
 *
 * -------------------------------------------------------------------------------------
 * Why Naive Approaches Fail
 * -------------------------------------------------------------------------------------
 *
 * Naive Mistake:
 * Rot one orange fully before another.
 *
 * Problem:
 * Rotting is simultaneous.
 *
 * DFS models sequential spread,
 * not synchronized time layers.
 *
 * Example:
 *
 * 2 1 1
 *
 * DFS path:
 * takes wrong timing semantics.
 *
 * BFS layers:
 * correctly model parallel spread.
 *
 * =====================================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * =====================================================================================
 *
 * -------------------------------------------------------------------------------------
 * ❌ Wrong Approach 1: DFS Infection
 * -------------------------------------------------------------------------------------
 *
 * Why It Seems Correct:
 *
 * "Spread recursively to neighbors."
 *
 * Invariant Violation:
 *
 * DFS processes depth-first,
 * not minute-by-minute.
 *
 * Counterexample:
 *
 * [2,1,1]
 *
 * DFS may propagate:
 *
 * minute sequence incorrectly.
 *
 * BFS guarantees:
 *
 * distance from nearest rotten source.
 *
 * -------------------------------------------------------------------------------------
 * ❌ Wrong Approach 2: Run BFS Separately From Each Rotten Orange
 * -------------------------------------------------------------------------------------
 *
 * Why It Seems Correct:
 *
 * "Try every rotten orange independently."
 *
 * Invariant Violation:
 *
 * Rotting is simultaneous.
 *
 * This approach:
 *
 * • repeats work
 * • loses global minimum timing
 * • increases complexity
 *
 * Multi-source BFS naturally merges all waves.
 *
 * -------------------------------------------------------------------------------------
 * ❌ Wrong Approach 3: Increment Minutes Per Cell
 * -------------------------------------------------------------------------------------
 *
 * Wrong Logic:
 *
 * minutes++
 * whenever one orange rots
 *
 * Why Wrong:
 *
 * Multiple oranges rot in SAME minute.
 *
 * Minute increment must happen:
 *
 * per BFS layer,
 * NOT per cell.
 *
 * -------------------------------------------------------------------------------------
 * ❌ Wrong Approach 4: Forgetting Fresh Counter
 * -------------------------------------------------------------------------------------
 *
 * Trap:
 *
 * BFS ends => assume success
 *
 * Problem:
 *
 * isolated fresh oranges may remain unreachable.
 *
 * Example:
 *
 * [[2,1,1],
 *  [0,1,1],
 *  [1,0,1]]
 *
 * bottom-left fresh orange never rots.
 *
 * =====================================================================================
 * ⚙️ HOW TO PHYSICALLY ASSEMBLE THE CODE
 * =====================================================================================
 *
 * 🛠️ IMPLEMENTATION BLUEPRINT
 *
 * Step 1:
 * Create queue
 *
 * Step 2:
 * Traverse entire grid
 *
 * Step 3:
 * Count fresh oranges
 *
 * Step 4:
 * Push all rotten oranges into queue
 *
 * Step 5:
 * Early exit:
 * if fresh == 0 return 0
 *
 * Step 6:
 * Create directions array
 *
 * Step 7:
 * Start BFS loop:
 * while queue not empty AND fresh > 0
 *
 * Step 8:
 * Capture current layer size
 *
 * Step 9:
 * Process exactly one BFS layer
 *
 * Step 10:
 * Pop current rotten orange
 *
 * Step 11:
 * Explore 4 neighbors
 *
 * Step 12:
 * Boundary checks
 *
 * Step 13:
 * Skip non-fresh cells
 *
 * Step 14:
 * Convert fresh -> rotten
 *
 * Step 15:
 * Decrease fresh count
 *
 * Step 16:
 * Push newly rotten into queue
 *
 * Step 17:
 * After full layer:
 * minutes++
 *
 * Step 18:
 * Final check:
 *
 * if fresh == 0 return minutes
 * else return -1
 *
 * =====================================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE (MEMORY SCAFFOLD)
 * =====================================================================================
 *
 * collect all rotten sources
 * count fresh
 *
 * while queue not empty AND fresh exists:
 *
 *     process one BFS layer
 *
 *     rot valid fresh neighbors
 *
 *     add new rotten cells
 *
 *     minutes++
 *
 * return fresh == 0 ? minutes : -1
 *
 * =====================================================================================
 * 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES
 * =====================================================================================
 */
public class RottenOranges {

    /**
     * =================================================================================
     * 🔴 BRUTE FORCE SOLUTION
     * =================================================================================
     *
     * Core Idea:
     *
     * Repeatedly scan entire matrix minute-by-minute.
     *
     * During one pass:
     * mark cells that should rot next.
     *
     * Invariant:
     *
     * One full matrix scan = one minute.
     *
     * Limitation:
     *
     * Extremely inefficient because entire matrix scanned repeatedly.
     *
     * Time Complexity:
     * O((m*n)^2)
     *
     * Space Complexity:
     * O(m*n)
     *
     * Interview Preference:
     * Low
     */

    static class BruteForceSolution {

        public int orangesRotting(int[][] grid) {

            int rows = grid.length;
            int cols = grid[0].length;

            int fresh = 0;

            for (int[] row : grid) {
                for (int cell : row) {
                    if (cell == 1) {
                        fresh++;
                    }
                }
            }

            int minutes = 0;

            int[][] directions = {
                    {-1, 0},
                    {1, 0},
                    {0, -1},
                    {0, 1}
            };

            while (fresh > 0) {

                List<int[]> toRot = new ArrayList<>();

                // Scan entire grid every minute.
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {

                        if (grid[r][c] != 2) {
                            continue;
                        }

                        for (int[] dir : directions) {

                            int nr = r + dir[0];
                            int nc = c + dir[1];

                            if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) {
                                continue;
                            }

                            if (grid[nr][nc] == 1) {
                                toRot.add(new int[]{nr, nc});
                            }
                        }
                    }
                }

                // No new infections possible.
                if (toRot.isEmpty()) {
                    return -1;
                }

                for (int[] cell : toRot) {

                    int r = cell[0];
                    int c = cell[1];

                    if (grid[r][c] == 1) {

                        grid[r][c] = 2;
                        fresh--;
                    }
                }

                minutes++;
            }

            return minutes;
        }
    }

    /**
     * =================================================================================
     * 🟡 IMPROVED SOLUTION
     * =================================================================================
     *
     * Core Idea:
     *
     * Store rotten cells separately instead of rescanning entire matrix blindly.
     *
     * Still less elegant than BFS layering.
     *
     * Invariant:
     *
     * Newly rotten oranges become future spread sources.
     *
     * Limitation Fixed:
     *
     * Avoids unnecessary scanning.
     *
     * Remaining Problem:
     *
     * Still manually simulating spread phases.
     *
     * Time Complexity:
     * O(m*n)
     *
     * Space Complexity:
     * O(m*n)
     *
     * Interview Preference:
     * Medium
     */

    static class ImprovedSolution {

        public int orangesRotting(int[][] grid) {

            int rows = grid.length;
            int cols = grid[0].length;

            List<int[]> current = new ArrayList<>();

            int fresh = 0;

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {

                    if (grid[r][c] == 2) {
                        current.add(new int[]{r, c});
                    } else if (grid[r][c] == 1) {
                        fresh++;
                    }
                }
            }

            if (fresh == 0) {
                return 0;
            }

            int[][] directions = {
                    {-1, 0},
                    {1, 0},
                    {0, -1},
                    {0, 1}
            };

            int minutes = 0;

            while (!current.isEmpty()) {

                List<int[]> next = new ArrayList<>();

                for (int[] cell : current) {

                    int r = cell[0];
                    int c = cell[1];

                    for (int[] dir : directions) {

                        int nr = r + dir[0];
                        int nc = c + dir[1];

                        if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) {
                            continue;
                        }

                        if (grid[nr][nc] != 1) {
                            continue;
                        }

                        grid[nr][nc] = 2;
                        fresh--;

                        next.add(new int[]{nr, nc});
                    }
                }

                if (!next.isEmpty()) {
                    minutes++;
                }

                current = next;
            }

            return fresh == 0 ? minutes : -1;
        }
    }

    /**
     * =================================================================================
     * 🟢 OPTIMAL SOLUTION — INTERVIEW PREFERRED
     * =================================================================================
     *
     * Pattern:
     * Multi-Source BFS
     *
     * Core Idea:
     *
     * Push ALL rotten oranges initially.
     *
     * Each BFS layer:
     * represents exactly one minute.
     *
     * Core Invariant:
     *
     * Queue always contains oranges currently rotten at this minute layer.
     *
     * Why Optimal:
     *
     * • exact time modeling
     * • shortest propagation naturally
     * • no repeated work
     * • extremely interview-standard
     *
     * Time Complexity:
     * O(m*n)
     *
     * Space Complexity:
     * O(m*n)
     *
     * Interview Preference:
     * Highest
     */

    static class OptimalSolution {

        public int orangesRotting(int[][] grid) {

            // Handle malformed input early.
            if (grid == null || grid.length == 0) {
                return 0;
            }

            int rows = grid.length;
            int cols = grid[0].length;

            Queue<int[]> queue = new LinkedList<>();

            int fresh = 0;

            // Collect all starting rotten sources.
            // Invariant: all these start at minute 0.
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {

                    if (grid[r][c] == 2) {
                        queue.offer(new int[]{r, c});
                    } else if (grid[r][c] == 1) {
                        fresh++;
                    }
                }
            }

            // No fresh oranges means no time needed.
            if (fresh == 0) {
                return 0;
            }

            int[][] directions = {
                    {-1, 0},
                    {1, 0},
                    {0, -1},
                    {0, 1}
            };

            int minutes = 0;

            // Invariant:
            // one full BFS layer == one minute.
            while (!queue.isEmpty() && fresh > 0) {

                int size = queue.size();

                // Process current minute layer.
                for (int i = 0; i < size; i++) {

                    int[] current = queue.poll();

                    int r = current[0];
                    int c = current[1];

                    for (int[] dir : directions) {

                        int nr = r + dir[0];
                        int nc = c + dir[1];

                        // Boundary protection.
                        if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) {
                            continue;
                        }

                        // Only fresh oranges can rot.
                        if (grid[nr][nc] != 1) {
                            continue;
                        }

                        // Rot immediately to avoid duplicate enqueue.
                        grid[nr][nc] = 2;

                        fresh--;

                        // Newly rotten becomes future BFS source.
                        queue.offer(new int[]{nr, nc});
                    }
                }

                // Entire layer completed => one minute passed.
                minutes++;
            }

            // If fresh remains,
            // some oranges were unreachable.
            return fresh == 0 ? minutes : -1;
        }
    }

    /**
     * =================================================================================
     * 🟣 INTERVIEW ARTICULATION (NO CODE)
     * =================================================================================
     *
     * Verbal Explanation:
     *
     * "This is a classic multi-source BFS problem.
     *
     * All rotten oranges start spreading simultaneously,
     * so I push all rotten oranges into the queue initially.
     *
     * Each BFS layer represents one minute.
     *
     * While processing one layer,
     * I infect all adjacent fresh oranges.
     *
     * Newly infected oranges become the next layer.
     *
     * The key invariant is:
     * every node processed in the same BFS layer rots at the same minute.
     *
     * If fresh oranges remain after BFS ends,
     * they were unreachable."
     *
     * ---------------------------------------------------------------------------------
     * Correctness Guarantee
     * ---------------------------------------------------------------------------------
     *
     * BFS guarantees minimum-time spread because:
     *
     * first time a fresh orange becomes rotten
     * is via shortest possible minute-distance
     * from any rotten source.
     *
     * ---------------------------------------------------------------------------------
     * What Breaks If Changed
     * ---------------------------------------------------------------------------------
     *
     * If we:
     *
     * • increment minutes per cell instead of per layer
     * -> timing becomes incorrect
     *
     * • use DFS
     * -> simultaneous spread invariant breaks
     *
     * • delay marking visited
     * -> duplicate enqueue possible
     *
     * ---------------------------------------------------------------------------------
     * In-Place Feasibility
     * ---------------------------------------------------------------------------------
     *
     * Yes.
     *
     * Grid itself acts as visited structure.
     *
     * Fresh -> Rotten transition marks visited.
     *
     * ---------------------------------------------------------------------------------
     * Streaming Feasibility
     * ---------------------------------------------------------------------------------
     *
     * No.
     *
     * Need full grid access because future spread depends on spatial adjacency.
     *
     * ---------------------------------------------------------------------------------
     * When NOT To Use This Pattern
     * ---------------------------------------------------------------------------------
     *
     * Avoid BFS when:
     *
     * • weighted edges exist
     * • spread cost differs
     * • not shortest-path-by-level problem
     *
     * Then:
     * Dijkstra may be required.
     *
     * =================================================================================
     * 🎯 INTERVIEW RECALL SHEET (30-SECOND RECALL)
     * =================================================================================
     *
     * Pattern Trigger:
     * simultaneous spread over time
     *
     * Core Invariant:
     * one BFS layer = one minute
     *
     * Search Target:
     * infect all reachable fresh oranges
     *
     * Discard Rule:
     * ignore non-fresh neighbors
     *
     * Common Trap:
     * incrementing time per cell
     *
     * Edge Cases:
     * • no fresh oranges
     * • isolated fresh oranges
     * • all empty cells
     *
     * Interview One-Liner:
     *
     * "Multi-source BFS where each level models one minute of spread."
     *
     * Re-Derivation Cue:
     *
     * "Simultaneous expansion => BFS layers."
     *
     * =================================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =================================================================================
     *
     * ---------------------------------------------------------------------------------
     * Variation 1:
     * Diagonal Spread Allowed
     * ---------------------------------------------------------------------------------
     *
     * Change:
     * directions array expands to 8 directions.
     *
     * Invariant Still Holds:
     * BFS layer still equals one minute.
     *
     * ---------------------------------------------------------------------------------
     * Variation 2:
     * Weighted Rotting Time
     * ---------------------------------------------------------------------------------
     *
     * Example:
     * some cells take 2 minutes to infect.
     *
     * BFS Breaks.
     *
     * Why?
     *
     * Uniform edge-cost invariant destroyed.
     *
     * Need:
     * Dijkstra.
     *
     * ---------------------------------------------------------------------------------
     * Variation 3:
     * Walls Blocking Spread
     * ---------------------------------------------------------------------------------
     *
     * Simply skip wall cells.
     *
     * BFS invariant unchanged.
     *
     * ---------------------------------------------------------------------------------
     * Variation 4:
     * Infinite Grid
     * ---------------------------------------------------------------------------------
     *
     * Need:
     * HashSet visited.
     *
     * Core BFS invariant still valid.
     *
     * =================================================================================
     * ⚫ REINFORCEMENT PROBLEMS
     * =================================================================================
     */

    /**
     * =================================================================================
     * ⚫ Reinforcement Problem 1 — 01 Matrix
     * =================================================================================
     *
     * Summary:
     *
     * For each cell,
     * find distance to nearest 0.
     *
     * Invariant Mapping:
     *
     * All 0s become simultaneous BFS sources.
     *
     * Same multi-source BFS pattern.
     *
     * Key Example:
     *
     * Input:
     * 0 0 0
     * 0 1 0
     * 1 1 1
     *
     * Output:
     * 0 0 0
     * 0 1 0
     * 1 2 1
     *
     * Edge Cases:
     * • all zeros
     * • single row
     *
     * Interview Trap:
     * running BFS separately from each 1
     */

    static class Matrix01Solution {

        public int[][] updateMatrix(int[][] mat) {

            int rows = mat.length;
            int cols = mat[0].length;

            Queue<int[]> queue = new LinkedList<>();

            int[][] distance = new int[rows][cols];

            for (int r = 0; r < rows; r++) {
                Arrays.fill(distance[r], -1);
            }

            // All zeroes are starting BFS sources.
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {

                    if (mat[r][c] == 0) {

                        queue.offer(new int[]{r, c});
                        distance[r][c] = 0;
                    }
                }
            }

            int[][] directions = {
                    {-1, 0},
                    {1, 0},
                    {0, -1},
                    {0, 1}
            };

            while (!queue.isEmpty()) {

                int[] current = queue.poll();

                int r = current[0];
                int c = current[1];

                for (int[] dir : directions) {

                    int nr = r + dir[0];
                    int nc = c + dir[1];

                    if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) {
                        continue;
                    }

                    if (distance[nr][nc] != -1) {
                        continue;
                    }

                    distance[nr][nc] = distance[r][c] + 1;

                    queue.offer(new int[]{nr, nc});
                }
            }

            return distance;
        }
    }

    /**
     * =================================================================================
     * ⚫ Reinforcement Problem 2 — Walls and Gates
     * =================================================================================
     *
     * Summary:
     *
     * Fill each empty room with distance to nearest gate.
     *
     * Invariant Mapping:
     *
     * Gates are simultaneous BFS sources.
     *
     * Interview Articulation:
     *
     * "Reverse the perspective.
     * Instead of searching from each room,
     * spread outward from all gates together."
     */

    static class WallsAndGatesSolution {

        private static final int INF = Integer.MAX_VALUE;

        public void wallsAndGates(int[][] rooms) {

            int rows = rooms.length;
            int cols = rooms[0].length;

            Queue<int[]> queue = new LinkedList<>();

            // All gates start simultaneously.
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {

                    if (rooms[r][c] == 0) {
                        queue.offer(new int[]{r, c});
                    }
                }
            }

            int[][] directions = {
                    {-1, 0},
                    {1, 0},
                    {0, -1},
                    {0, 1}
            };

            while (!queue.isEmpty()) {

                int[] current = queue.poll();

                int r = current[0];
                int c = current[1];

                for (int[] dir : directions) {

                    int nr = r + dir[0];
                    int nc = c + dir[1];

                    if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) {
                        continue;
                    }

                    if (rooms[nr][nc] != INF) {
                        continue;
                    }

                    rooms[nr][nc] = rooms[r][c] + 1;

                    queue.offer(new int[]{nr, nc});
                }
            }
        }
    }

    /**
     * =================================================================================
     * ⚫ Reinforcement Problem 3 — As Far from Land as Possible
     * =================================================================================
     *
     * Summary:
     *
     * Find water cell farthest from land.
     *
     * Invariant Mapping:
     *
     * All land cells start BFS simultaneously.
     *
     * Pattern:
     * expanding distance layers.
     */

    static class AsFarFromLandSolution {

        public int maxDistance(int[][] grid) {

            int rows = grid.length;
            int cols = grid[0].length;

            Queue<int[]> queue = new LinkedList<>();

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {

                    if (grid[r][c] == 1) {
                        queue.offer(new int[]{r, c});
                    }
                }
            }

            // All land or all water.
            if (queue.isEmpty() || queue.size() == rows * cols) {
                return -1;
            }

            int[][] directions = {
                    {-1, 0},
                    {1, 0},
                    {0, -1},
                    {0, 1}
            };

            int distance = -1;

            while (!queue.isEmpty()) {

                int size = queue.size();

                for (int i = 0; i < size; i++) {

                    int[] current = queue.poll();

                    int r = current[0];
                    int c = current[1];

                    for (int[] dir : directions) {

                        int nr = r + dir[0];
                        int nc = c + dir[1];

                        if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) {
                            continue;
                        }

                        if (grid[nr][nc] != 0) {
                            continue;
                        }

                        grid[nr][nc] = 1;

                        queue.offer(new int[]{nr, nc});
                    }
                }

                distance++;
            }

            return distance;
        }
    }

    /**
     * =================================================================================
     * 🧩 RELATED PROBLEMS
     * =================================================================================
     */

    /**
     * =================================================================================
     * 🧩 Related Problem 1 — Number of Islands
     * =================================================================================
     *
     * Invariant:
     * connected-component traversal
     *
     * Difference:
     * not time-layer BFS
     *
     * Goal:
     * count components
     */

    static class NumberOfIslandsSolution {

        public int numIslands(char[][] grid) {

            int rows = grid.length;
            int cols = grid[0].length;

            int islands = 0;

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {

                    if (grid[r][c] == '1') {

                        islands++;

                        bfs(grid, r, c);
                    }
                }
            }

            return islands;
        }

        private void bfs(char[][] grid, int row, int col) {

            Queue<int[]> queue = new LinkedList<>();

            queue.offer(new int[]{row, col});

            grid[row][col] = '0';

            int[][] directions = {
                    {-1, 0},
                    {1, 0},
                    {0, -1},
                    {0, 1}
            };

            while (!queue.isEmpty()) {

                int[] current = queue.poll();

                int r = current[0];
                int c = current[1];

                for (int[] dir : directions) {

                    int nr = r + dir[0];
                    int nc = c + dir[1];

                    if (nr < 0 || nc < 0 ||
                            nr >= grid.length || nc >= grid[0].length) {
                        continue;
                    }

                    if (grid[nr][nc] != '1') {
                        continue;
                    }

                    grid[nr][nc] = '0';

                    queue.offer(new int[]{nr, nc});
                }
            }
        }
    }

    /**
     * =================================================================================
     * 🧩 Related Problem 2 — Shortest Path in Binary Matrix
     * =================================================================================
     *
     * Same Invariant:
     * BFS level = shortest distance
     *
     * Modified:
     * now searching shortest path.
     *
     * Edge Case:
     * blocked start/end.
     */

    static class ShortestPathBinaryMatrixSolution {

        public int shortestPathBinaryMatrix(int[][] grid) {

            int n = grid.length;

            if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) {
                return -1;
            }

            Queue<int[]> queue = new LinkedList<>();

            queue.offer(new int[]{0, 0});

            grid[0][0] = 1;

            int[][] directions = {
                    {-1, -1}, {-1, 0}, {-1, 1},
                    {0, -1},           {0, 1},
                    {1, -1},  {1, 0}, {1, 1}
            };

            int distance = 1;

            while (!queue.isEmpty()) {

                int size = queue.size();

                for (int i = 0; i < size; i++) {

                    int[] current = queue.poll();

                    int r = current[0];
                    int c = current[1];

                    if (r == n - 1 && c == n - 1) {
                        return distance;
                    }

                    for (int[] dir : directions) {

                        int nr = r + dir[0];
                        int nc = c + dir[1];

                        if (nr < 0 || nc < 0 || nr >= n || nc >= n) {
                            continue;
                        }

                        if (grid[nr][nc] != 0) {
                            continue;
                        }

                        grid[nr][nc] = 1;

                        queue.offer(new int[]{nr, nc});
                    }
                }

                distance++;
            }

            return -1;
        }
    }

    /**
     * =================================================================================
     * 🧩 Related Problem 3 — Pacific Atlantic Water Flow
     * =================================================================================
     *
     * Modified Invariant:
     *
     * reverse-flow reachability
     *
     * Pattern Difference:
     * BFS/DFS from oceans inward.
     */

    static class PacificAtlanticSolution {

        public List<List<Integer>> pacificAtlantic(int[][] heights) {

            int rows = heights.length;
            int cols = heights[0].length;

            boolean[][] pacific = new boolean[rows][cols];
            boolean[][] atlantic = new boolean[rows][cols];

            Queue<int[]> pacificQueue = new LinkedList<>();
            Queue<int[]> atlanticQueue = new LinkedList<>();

            for (int r = 0; r < rows; r++) {

                pacific[r][0] = true;
                atlantic[r][cols - 1] = true;

                pacificQueue.offer(new int[]{r, 0});
                atlanticQueue.offer(new int[]{r, cols - 1});
            }

            for (int c = 0; c < cols; c++) {

                pacific[0][c] = true;
                atlantic[rows - 1][c] = true;

                pacificQueue.offer(new int[]{0, c});
                atlanticQueue.offer(new int[]{rows - 1, c});
            }

            bfs(heights, pacificQueue, pacific);
            bfs(heights, atlanticQueue, atlantic);

            List<List<Integer>> result = new ArrayList<>();

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {

                    if (pacific[r][c] && atlantic[r][c]) {
                        result.add(Arrays.asList(r, c));
                    }
                }
            }

            return result;
        }

        private void bfs(int[][] heights,
                         Queue<int[]> queue,
                         boolean[][] visited) {

            int rows = heights.length;
            int cols = heights[0].length;

            int[][] directions = {
                    {-1, 0},
                    {1, 0},
                    {0, -1},
                    {0, 1}
            };

            while (!queue.isEmpty()) {

                int[] current = queue.poll();

                int r = current[0];
                int c = current[1];

                for (int[] dir : directions) {

                    int nr = r + dir[0];
                    int nc = c + dir[1];

                    if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) {
                        continue;
                    }

                    if (visited[nr][nc]) {
                        continue;
                    }

                    if (heights[nr][nc] < heights[r][c]) {
                        continue;
                    }

                    visited[nr][nc] = true;

                    queue.offer(new int[]{nr, nc});
                }
            }
        }
    }

    /**
     * =================================================================================
     * 🧠 MASTERY CHECKLIST
     * =================================================================================
     *
     * Q: What is the invariant?
     *
     * A:
     * one BFS layer = one minute
     *
     * ---------------------------------------------------------------------------------
     * Q: What is the search target?
     *
     * A:
     * infect all reachable fresh oranges
     *
     * ---------------------------------------------------------------------------------
     * Q: What is the discard rule?
     *
     * A:
     * skip non-fresh neighbors
     *
     * ---------------------------------------------------------------------------------
     * Q: What is the termination logic?
     *
     * A:
     * BFS ends when queue empty
     *
     * if fresh remains:
     * unreachable exists
     *
     * ---------------------------------------------------------------------------------
     * Q: Why does naive fail?
     *
     * A:
     * sequential processing breaks simultaneous timing semantics
     *
     * ---------------------------------------------------------------------------------
     * Q: Edge Cases?
     *
     * A:
     * • no fresh oranges
     * • all empty
     * • isolated fresh
     * • single cell
     *
     * ---------------------------------------------------------------------------------
     * Q: Debugging Readiness?
     *
     * A:
     * Verify:
     * • minute increments per layer
     * • immediate marking visited
     * • fresh decrement correctness
     * • layer size captured before iteration
     *
     * ---------------------------------------------------------------------------------
     * Q: Variant Readiness?
     *
     * A:
     * Can adapt to:
     * • infection spread
     * • shortest distance
     * • simultaneous propagation
     *
     * ---------------------------------------------------------------------------------
     * Q: Pattern Boundary?
     *
     * A:
     * BFS breaks when edge weights differ.
     *
     * =================================================================================
     * 🧪 main() + SELF-VERIFYING TESTS
     * =================================================================================
     */

    public static void main(String[] args) {

        OptimalSolution solution = new OptimalSolution();

        // -------------------------------------------------------------------------
        // Happy Path:
        // Standard layered spread.
        // -------------------------------------------------------------------------
        int[][] grid1 = {
                {2, 1, 1},
                {1, 1, 0},
                {0, 1, 1}
        };

        assert solution.orangesRotting(copy(grid1)) == 4
                : "Expected 4 minutes for full spread.";

        // -------------------------------------------------------------------------
        // Interview Trap:
        // Unreachable fresh orange.
        // -------------------------------------------------------------------------
        int[][] grid2 = {
                {2, 1, 1},
                {0, 1, 1},
                {1, 0, 1}
        };

        assert solution.orangesRotting(copy(grid2)) == -1
                : "Expected -1 due to isolated fresh orange.";

        // -------------------------------------------------------------------------
        // Edge Case:
        // No fresh oranges initially.
        // -------------------------------------------------------------------------
        int[][] grid3 = {
                {0, 2}
        };

        assert solution.orangesRotting(copy(grid3)) == 0
                : "Expected 0 because no fresh oranges exist.";

        // -------------------------------------------------------------------------
        // Boundary Case:
        // Single fresh orange without rotten source.
        // -------------------------------------------------------------------------
        int[][] grid4 = {
                {1}
        };

        assert solution.orangesRotting(copy(grid4)) == -1
                : "Expected -1 because no infection source exists.";

        // -------------------------------------------------------------------------
        // Boundary Case:
        // Single rotten orange.
        // -------------------------------------------------------------------------
        int[][] grid5 = {
                {2}
        };

        assert solution.orangesRotting(copy(grid5)) == 0
                : "Expected 0 because already rotten.";

        // -------------------------------------------------------------------------
        // Edge Case:
        // All empty cells.
        // -------------------------------------------------------------------------
        int[][] grid6 = {
                {0, 0},
                {0, 0}
        };

        assert solution.orangesRotting(copy(grid6)) == 0
                : "Expected 0 because nothing exists to rot.";

        // -------------------------------------------------------------------------
        // Multi-source simultaneous spread validation.
        // -------------------------------------------------------------------------
        int[][] grid7 = {
                {2, 1, 1},
                {1, 1, 1},
                {1, 1, 2}
        };

        assert solution.orangesRotting(copy(grid7)) == 2
                : "Expected 2 due to simultaneous multi-source BFS.";

        System.out.println("All self-verifying tests passed.");
        System.out.println();

        System.out.println("I understand the invariant.");
        System.out.println("I can re-derive the solution.");
        System.out.println("I can physically reconstruct the implementation under pressure.");
        System.out.println("This chapter is complete.");
    }

    private static int[][] copy(int[][] grid) {

        int[][] result = new int[grid.length][];

        for (int i = 0; i < grid.length; i++) {
            result[i] = grid[i].clone();
        }

        return result;
    }
}

