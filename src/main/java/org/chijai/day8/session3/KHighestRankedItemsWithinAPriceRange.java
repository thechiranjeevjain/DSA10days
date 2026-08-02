package org.chijai.day8.session3;

import java.util.*;

/**
 * KHighestRankedItemsWithinAPriceRange
 *
 * ============================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Title:
 * K Highest Ranked Items Within a Price Range (Booking Hotel)
 *
 * Difficulty:
 * Hard
 *
 * Tags:
 * BFS
 * Graph
 * Matrix
 * Multi-Criteria Ranking
 * Queue
 * Sorting
 *
 * LeetCode:
 * https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/
 *
 * ------------------------------------------------------------
 * Problem
 * ------------------------------------------------------------
 *
 * You are given a matrix where:
 *
 * 0 -> blocked cell (cannot pass)
 * 1 -> empty road
 * >1 -> item (hotel) with that price
 *
 * Starting from start = [r,c], you may move in four directions.
 *
 * Find the highest ranked k reachable items whose prices lie inside
 * the inclusive range:
 *
 * [low, high]
 *
 * Ranking rules (highest priority first):
 *
 * 1. Smaller shortest-path distance.
 * 2. Smaller price.
 * 3. Smaller row.
 * 4. Smaller column.
 *
 * Return coordinates of the first k ranked items.
 *
 * ------------------------------------------------------------
 * Constraints
 * ------------------------------------------------------------
 *
 * 1 <= m,n <= 200
 * 1 <= grid[i][j] <= 10^5 except blocked cells (0)
 * start is always inside the grid.
 * start is never blocked.
 *
 * ------------------------------------------------------------
 * Representative Example
 * ------------------------------------------------------------
 *
 * grid =
 * [[1,2,0,1],
 *  [1,3,0,1],
 *  [0,2,5,1]]
 *
 * pricing = [2,5]
 * start = [0,0]
 * k = 3
 *
 * Output:
 *
 * [[0,1],[1,1],[2,1]]
 *
 * Explanation:
 *
 * Reachable qualified items:
 *
 * (0,1)
 * (1,1)
 * (2,1)
 * (2,2)
 *
 * Distances:
 *
 * 1
 * 2
 * 3
 * 4
 *
 * Therefore first three are returned.
 *
 * ============================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern
 * -------
 * Breadth First Search with Level Ranking
 *
 * Archetype
 * ---------
 * Shortest path in an unweighted graph.
 *
 * Core Invariant
 * --------------
 * Every BFS level represents exactly one shortest-path distance.
 *
 * Therefore:
 *
 * distance is already sorted automatically.
 *
 * We only need to sort nodes INSIDE ONE LEVEL using the remaining
 * ranking rules.
 *
 * Why it works
 * ------------
 * BFS guarantees:
 *
 * first visit
 * ==
 * shortest distance.
 *
 * Since ranking priority is
 *
 * distance
 * ->
 * price
 * ->
 * row
 * ->
 * column
 *
 * distance never needs explicit sorting.
 *
 * Recognition Signals
 * -------------------
 *
 * • unweighted movement
 * • shortest distance
 * • four directions
 * • obstacles
 * • grid
 * • return nearest objects
 * • multiple ranking keys
 *
 * When To Use
 * -----------
 *
 * Whenever primary ranking is shortest path and graph is unweighted.
 *
 * When NOT To Use
 * ---------------
 *
 * If edge weights differ.
 *
 * Then BFS no longer preserves shortest distance.
 *
 * Use Dijkstra instead.
 *
 * Comparison
 * ----------
 *
 * BFS
 * ----
 * Primary key = shortest distance
 *
 * Dijkstra
 * --------
 * Primary key = minimum weighted cost
 *
 * Multi-source BFS
 * ----------------
 * Many starting positions.
 *
 * Standard BFS
 * ------------
 * One starting position.
 *
 * ============================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine expanding circles around the start.
 *
 * Every expansion ring represents one exact distance.
 *
 * We never return to an inner ring.
 *
 * Therefore once a level finishes,
 * every future discovered node is strictly farther.
 *
 * ------------------------------------------------------------
 * Invariant 1
 * ------------------------------------------------------------
 *
 * Queue contains exactly one frontier.
 *
 * ------------------------------------------------------------
 * Invariant 2
 * ------------------------------------------------------------
 *
 * Every node popped in current iteration has identical distance.
 *
 * ------------------------------------------------------------
 * Invariant 3
 * ------------------------------------------------------------
 *
 * First discovery gives shortest distance.
 *
 * Therefore visited is marked immediately upon enqueue,
 * not dequeue.
 *
 * ------------------------------------------------------------
 * Invariant 4
 * ------------------------------------------------------------
 *
 * All candidate items inside one BFS level share identical
 * distance.
 *
 * Thus sorting only needs:
 *
 * price
 * row
 * column
 *
 * ------------------------------------------------------------
 * Variable Meaning
 * ------------------------------------------------------------
 *
 * queue
 * -----
 * Current BFS frontier.
 *
 * visited
 * -------
 * Prevent revisiting.
 *
 * levelItems
 * ----------
 * Qualified items found at current distance.
 *
 * answer
 * ------
 * Global ranked result.
 *
 * ------------------------------------------------------------
 * Allowed Moves
 * ------------------------------------------------------------
 *
 * Up
 * Down
 * Left
 * Right
 *
 * Skip:
 *
 * outside grid
 * blocked
 * visited
 *
 * ------------------------------------------------------------
 * Forbidden Moves
 * ------------------------------------------------------------
 *
 * Revisiting nodes.
 *
 * Delaying visited marking.
 *
 * Mixing different BFS levels before sorting.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * BFS ends when:
 *
 * queue empty
 *
 * OR
 *
 * answer size reaches k.
 *
 * ------------------------------------------------------------
 * Why Naive Solutions Fail
 * ------------------------------------------------------------
 *
 * Simply collecting every reachable hotel then sorting globally
 * destroys the primary ranking invariant.
 *
 * Distance must dominate every other comparison.
 *
 * ============================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================
 *
 * Mistake 1
 * ---------
 * Global sort after BFS.
 *
 * Why tempting:
 * Easier implementation.
 *
 * Violated Invariant:
 * Distance ordering no longer guaranteed unless distance stored.
 *
 * ------------------------------------------------------------
 * Mistake 2
 * ---------
 * Using DFS.
 *
 * Why tempting:
 * Easy traversal.
 *
 * Violation:
 * DFS does not preserve shortest distance.
 *
 * Counterexample:
 *
 * Long corridor explored before short branch.
 *
 * ------------------------------------------------------------
 * Mistake 3
 * ---------
 * Mark visited after dequeue.
 *
 * Violation:
 * Same cell may be inserted multiple times.
 *
 * Complexity increases dramatically.
 *
 * ------------------------------------------------------------
 * Mistake 4
 * ---------
 * Sorting the whole queue.
 *
 * Violation:
 * Queue represents traversal state,
 * not ranking state.
 *
 * ------------------------------------------------------------
 * Interview Trap
 * --------------
 *
 * Why don't we sort by distance?
 *
 * Because BFS has already sorted distance for us.
 *
 * ============================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================
 *
 * Typing Order
 * ------------
 *
 * 1.
 * Early validation.
 *
 * 2.
 * Extract dimensions.
 *
 * 3.
 * Prepare visited.
 *
 * 4.
 * Create queue.
 *
 * 5.
 * Push start.
 *
 * 6.
 * While queue not empty
 *
 *      determine current level size
 *
 *      create levelItems
 *
 *      process exactly levelSize nodes
 *
 *      explore neighbors
 *
 *      collect qualified items
 *
 *      sort levelItems
 *
 *      append into answer
 *
 *      stop if k reached
 *
 * 7.
 * Return answer.
 *
 * ============================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================
 *
 * enqueue(start)
 *
 * while queue not empty
 *
 *      process one level
 *
 *      collect candidates
 *
 *      sort candidates
 *
 *      append answers
 *
 * return answer
 */
public class KHighestRankedItemsWithinAPriceRange {

    private static final int[] DIR = {0, 1, 0, -1, 0};

    private static final class Cell {
        final int row;
        final int col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    /**
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    /**
     * ------------------------------------------------------------
     * Brute Force
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Explore every reachable node.
     *
     * Store:
     * distance,
     * price,
     * row,
     * column.
     *
     * Globally sort all candidates.
     *
     * Invariant
     * ---------
     * Distances are explicitly stored.
     *
     * Limitation
     * ----------
     * Stores every candidate before producing answer.
     *
     * Complexity
     * ----------
     * Time:
     * O(MN log(MN))
     *
     * Space:
     * O(MN)
     *
     * Interview usefulness
     * --------------------
     * Good stepping stone.
     */

    static final class BruteForce {

        private static final class Candidate {
            final int distance;
            final int price;
            final int row;
            final int col;

            Candidate(int distance, int price, int row, int col) {
                this.distance = distance;
                this.price = price;
                this.row = row;
                this.col = col;
            }
        }

        public List<List<Integer>> highestRankedKItems(
                int[][] grid,
                int[] pricing,
                int[] start,
                int k) {

            int m = grid.length;
            int n = grid[0].length;

            boolean[][] visited = new boolean[m][n];

            Queue<Cell> queue = new ArrayDeque<>();

            queue.offer(new Cell(start[0], start[1]));

            visited[start[0]][start[1]] = true;

            int distance = 0;

            List<Candidate> candidates = new ArrayList<>();

            while (!queue.isEmpty()) {

                int size = queue.size();

                for (int s = 0; s < size; s++) {

                    Cell current = queue.poll();

                    int value = grid[current.row][current.col];

                    if (value >= pricing[0] && value <= pricing[1]) {
                        candidates.add(
                                new Candidate(
                                        distance,
                                        value,
                                        current.row,
                                        current.col));
                    }

                    for (int d = 0; d < 4; d++) {

                        int nr = current.row + DIR[d];
                        int nc = current.col + DIR[d + 1];

                        if (nr < 0 || nr >= m || nc < 0 || nc >= n)
                            continue;

                        if (visited[nr][nc])
                            continue;

                        if (grid[nr][nc] == 0)
                            continue;

                        visited[nr][nc] = true;

                        queue.offer(new Cell(nr, nc));
                    }
                }

                distance++;
            }

            candidates.sort((a, b) -> {

                if (a.distance != b.distance)
                    return Integer.compare(a.distance, b.distance);

                if (a.price != b.price)
                    return Integer.compare(a.price, b.price);

                if (a.row != b.row)
                    return Integer.compare(a.row, b.row);

                return Integer.compare(a.col, b.col);
            });

            List<List<Integer>> answer = new ArrayList<>();

            for (Candidate candidate : candidates) {

                if (answer.size() == k)
                    break;

                answer.add(List.of(candidate.row, candidate.col));
            }

            return answer;
        }
    }

    /**
     * ------------------------------------------------------------
     * Improved
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Observe that BFS has already ordered nodes by distance.
     *
     * Therefore:
     *
     * We never globally sort every reachable item.
     *
     * Instead:
     *
     * 1. Process exactly one BFS level.
     * 2. Collect only qualified items in this level.
     * 3. Sort only this level using
     *      price
     *      row
     *      column
     * 4. Append into answer.
     *
     * Since later BFS levels are always farther,
     * earlier levels always dominate the ranking.
     *
     * 🟢 Invariant
     * ------------
     * Every candidate inside levelItems has identical shortest-path
     * distance.
     *
     * Therefore distance disappears from the comparator.
     *
     * Improvement
     * -----------
     * Avoids sorting every candidate globally.
     *
     * Complexity
     * ----------
     * Worst Case
     *
     * Time
     * O(MN log(MN))
     *
     * because one level could theoretically contain O(MN) nodes.
     *
     * Typical practical behavior is better.
     *
     * Space
     * O(MN)
     *
     * Interview usefulness
     * --------------------
     * Demonstrates understanding that BFS itself performs the first
     * ranking key.
     */

    static final class Improved {

        public List<List<Integer>> highestRankedKItems(
                int[][] grid,
                int[] pricing,
                int[] start,
                int k) {

            int m = grid.length;
            int n = grid[0].length;

            int low = pricing[0];
            int high = pricing[1];

            boolean[][] visited = new boolean[m][n];

            Queue<Cell> queue = new ArrayDeque<>();

            queue.offer(new Cell(start[0], start[1]));

            visited[start[0]][start[1]] = true;

            List<List<Integer>> answer = new ArrayList<>();

            while (!queue.isEmpty()) {

                int levelSize = queue.size();

                List<Cell> levelItems = new ArrayList<>();

                for (int i = 0; i < levelSize; i++) {

                    Cell current = queue.poll();

                    int value = grid[current.row][current.col];

                    if (value >= low && value <= high) {
                        levelItems.add(current);
                    }

                    for (int d = 0; d < 4; d++) {

                        int nr = current.row + DIR[d];
                        int nc = current.col + DIR[d + 1];

                        if (nr < 0 || nr >= m || nc < 0 || nc >= n)
                            continue;

                        if (visited[nr][nc])
                            continue;

                        if (grid[nr][nc] == 0)
                            continue;

                        // Invariant:
                        // First enqueue guarantees shortest distance.
                        visited[nr][nc] = true;

                        queue.offer(new Cell(nr, nc));
                    }
                }

                levelItems.sort((a, b) -> {

                    if (grid[a.row][a.col] != grid[b.row][b.col]) {
                        return Integer.compare(
                                grid[a.row][a.col],
                                grid[b.row][b.col]);
                    }

                    if (a.row != b.row)
                        return Integer.compare(a.row, b.row);

                    return Integer.compare(a.col, b.col);
                });

                for (Cell cell : levelItems) {

                    answer.add(List.of(cell.row, cell.col));

                    if (answer.size() == k)
                        return answer;
                }
            }

            return answer;
        }
    }

    /**
     * ------------------------------------------------------------
     * Optimal (Interview Preferred)
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Use BFS level-order traversal.
     *
     * Distance ranking is produced naturally.
     *
     * Inside one level,
     * sort only by the remaining ranking rules.
     *
     * Final ranking therefore becomes:
     *
     * BFS level
     * →
     * price
     * →
     * row
     * →
     * column
     *
     * 🟢 Core Invariant
     * -----------------
     * Queue never mixes different shortest-path distances while one
     * level is being processed.
     *
     * 🟢 Correctness
     * --------------
     * Because:
     *
     * 1.
     * BFS discovers every node at minimum distance.
     *
     * 2.
     * Current level contains exactly one distance.
     *
     * 3.
     * Comparator resolves remaining ranking keys.
     *
     * 4.
     * Earlier levels are always ranked before later levels.
     *
     * Complexity
     * ----------
     * Time
     * O(MN log(MN))
     *
     * Space
     * O(MN)
     *
     * Interview usefulness
     * --------------------
     * This is the intended solution.
     */

    static final class Optimal {

        public List<List<Integer>> highestRankedKItems(
                int[][] grid,
                int[] pricing,
                int[] start,
                int k) {

            int rows = grid.length;
            int cols = grid[0].length;

            int low = pricing[0];
            int high = pricing[1];

            boolean[][] visited = new boolean[rows][cols];

            Queue<Cell> queue = new ArrayDeque<>();

            List<List<Integer>> answer = new ArrayList<>();

            // Invariant:
            // Start is the unique node at distance zero.
            queue.offer(new Cell(start[0], start[1]));

            visited[start[0]][start[1]] = true;

            while (!queue.isEmpty()) {

                int levelSize = queue.size();

                List<Cell> currentDistanceItems = new ArrayList<>();

                for (int i = 0; i < levelSize; i++) {

                    Cell current = queue.poll();

                    int value = grid[current.row][current.col];

                    // Invariant:
                    // Current node is visited at minimum distance.
                    if (value >= low && value <= high) {
                        currentDistanceItems.add(current);
                    }

                    for (int d = 0; d < 4; d++) {

                        int nextRow = current.row + DIR[d];
                        int nextCol = current.col + DIR[d + 1];

                        if (nextRow < 0 || nextRow >= rows)
                            continue;

                        if (nextCol < 0 || nextCol >= cols)
                            continue;

                        if (grid[nextRow][nextCol] == 0)
                            continue;

                        if (visited[nextRow][nextCol])
                            continue;

                        // Invariant:
                        // First enqueue locks shortest distance.
                        visited[nextRow][nextCol] = true;

                        queue.offer(new Cell(nextRow, nextCol));
                    }
                }

                currentDistanceItems.sort((a, b) -> {

                    int priceA = grid[a.row][a.col];
                    int priceB = grid[b.row][b.col];

                    if (priceA != priceB)
                        return Integer.compare(priceA, priceB);

                    if (a.row != b.row)
                        return Integer.compare(a.row, b.row);

                    return Integer.compare(a.col, b.col);
                });

                for (Cell item : currentDistanceItems) {

                    answer.add(List.of(item.row, item.col));

                    // Correctness:
                    // Remaining BFS levels are farther.
                    if (answer.size() == k)
                        return answer;
                }
            }

            return answer;
        }
    }

/**
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Explain the invariant:
 *
 * "Because movement is unweighted,
 * BFS guarantees that every node is discovered at its minimum
 * possible distance.
 *
 * Therefore every node processed in one BFS level has identical
 * distance.
 *
 * Distance is already sorted.
 *
 * I only need to sort the current level using:
 *
 * price,
 * row,
 * column.
 *
 * This preserves the required ranking exactly."
 *
 * ------------------------------------------------------------
 * Discard Rule
 * ------------------------------------------------------------
 *
 * Once one BFS level finishes,
 * every future node has larger distance.
 *
 * Therefore no later item can outrank an earlier-distance item.
 *
 * ------------------------------------------------------------
 * Correctness
 * ------------------------------------------------------------
 *
 * BFS guarantees shortest path.
 *
 * Level sorting resolves remaining tie-breakers.
 *
 * Combined ordering equals the specification.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Stop when:
 *
 * queue empty
 *
 * or
 *
 * k answers collected.
 *
 * ------------------------------------------------------------
 * In-place Feasibility
 * ------------------------------------------------------------
 *
 * No.
 *
 * Visited information must be maintained.
 *
 * ------------------------------------------------------------
 * Streaming Feasibility
 * ------------------------------------------------------------
 *
 * Partially.
 *
 * One BFS level must be completed before its candidates can be
 * emitted because they require intra-level sorting.
 *
 * ------------------------------------------------------------
 * When NOT To Use
 * ------------------------------------------------------------
 *
 * Weighted graph.
 *
 * Dynamic edge costs.
 *
 * Teleport edges with unequal cost.
 *
 * Those require Dijkstra rather than BFS.
 *
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 * Grid
 * +
 * shortest path
 * +
 * ranking
 *
 * Pattern
 * -------
 * BFS Level Traversal
 *
 * Invariant
 * ---------
 * One level = one shortest distance.
 *
 * Search Space
 * ------------
 * Reachable cells.
 *
 * Discard Rule
 * ------------
 * Finished BFS levels can never be outranked.
 *
 * Common Trap
 * -----------
 * Global sorting.
 *
 * Edge Cases
 * ----------
 * Start already contains an item.
 *
 * No reachable item.
 *
 * k larger than available items.
 *
 * Entire grid blocked.
 *
 * One-liner
 * ---------
 * BFS sorts distance.
 * Comparator sorts ties.
 *
 * Re-derivation Cue
 * -----------------
 * Ask:
 *
 * "Which ranking key is already guaranteed by traversal?"

 /**
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * ------------------------------------------------------------
 * Variation 1
 * ------------------------------------------------------------
 *
 * Ranking:
 *
 * Distance
 * →
 * Rating
 * →
 * Price
 *
 * Change:
 *
 * Only comparator changes.
 *
 * BFS invariant is unchanged.
 *
 * ------------------------------------------------------------
 * Variation 2
 * ------------------------------------------------------------
 *
 * Multiple starting locations.
 *
 * Use:
 *
 * Multi-source BFS.
 *
 * Invariant:
 *
 * First discovery is still the shortest distance from any source.
 *
 * ------------------------------------------------------------
 * Variation 3
 * ------------------------------------------------------------
 *
 * Weighted roads.
 *
 * Pattern breaks.
 *
 * Why?
 *
 * BFS no longer guarantees shortest distance.
 *
 * Replace with:
 *
 * Dijkstra.
 *
 * ------------------------------------------------------------
 * Variation 4
 * ------------------------------------------------------------
 *
 * Eight-direction movement.
 *
 * Only direction array changes.
 *
 * BFS correctness is unchanged because every edge still has unit
 * cost.
 *
 * ------------------------------------------------------------
 * Variation 5
 * ------------------------------------------------------------
 *
 * Return only nearest item.
 *
 * Stop after first qualified item in the current BFS level.
 *
 * If multiple qualified items exist in that same level, sort only
 * that level and return the first.
 *
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * ✓ What is the invariant?
 *
 * One BFS level represents exactly one shortest-path distance.
 *
 * ✓ What is the search space?
 *
 * Every reachable non-blocked cell.
 *
 * ✓ What is the discard rule?
 *
 * Finished BFS levels can never be outranked by later levels.
 *
 * ✓ Why does termination happen?
 *
 * Queue becomes empty or k items have been collected.
 *
 * ✓ Why does the naive solution fail?
 *
 * It ignores that distance must dominate every other ranking key.
 *
 * ✓ Edge cases remembered?
 *
 * Yes:
 *
 * - start already qualifies
 * - no reachable items
 * - blocked paths
 * - k exceeds available items
 * - all roads
 *
 * ✓ Debugging readiness?
 *
 * Verify:
 *
 * - visited marked on enqueue
 * - process exactly one BFS level
 * - comparator excludes distance
 * - comparator order:
 *      price
 *      row
 *      column
 *
 * ✓ Variant readiness?
 *
 * Yes.
 *
 * Replace BFS only if edge weights change.
 *
 * ✓ Pattern boundary?
 *
 * Unweighted shortest-path search.
 *
 * ============================================================
 * 🧪 MAIN + SELF-VERIFYING TESTS
 * ============================================================
 */

public static void main(String[] args) {

    Optimal solution = new Optimal();

    {
        // Representative example 1.
        int[][] grid = {
                {1, 2, 0, 1},
                {1, 3, 0, 1},
                {0, 2, 5, 1}
        };

        List<List<Integer>> expected = List.of(
                List.of(0, 1),
                List.of(1, 1),
                List.of(2, 1)
        );

        assert solution.highestRankedKItems(
                grid,
                new int[]{2, 5},
                new int[]{0, 0},
                3
        ).equals(expected);
    }

    {
        // Representative example 2.
        int[][] grid = {
                {1, 2, 0, 1},
                {1, 3, 3, 1},
                {0, 2, 5, 1}
        };

        List<List<Integer>> expected = List.of(
                List.of(2, 1),
                List.of(1, 2)
        );

        assert solution.highestRankedKItems(
                grid,
                new int[]{2, 3},
                new int[]{2, 3},
                2
        ).equals(expected);
    }

    {
        // Representative example 3.
        int[][] grid = {
                {1, 1, 1},
                {0, 0, 1},
                {2, 3, 4}
        };

        List<List<Integer>> expected = List.of(
                List.of(2, 1),
                List.of(2, 0)
        );

        assert solution.highestRankedKItems(
                grid,
                new int[]{2, 3},
                new int[]{0, 0},
                3
        ).equals(expected);
    }

    {
        // Start cell itself qualifies.
        int[][] grid = {
                {5}
        };

        List<List<Integer>> expected = List.of(
                List.of(0, 0)
        );

        assert solution.highestRankedKItems(
                grid,
                new int[]{2, 6},
                new int[]{0, 0},
                1
        ).equals(expected);
    }

    {
        // No qualifying items.
        int[][] grid = {
                {1, 1},
                {1, 1}
        };

        assert solution.highestRankedKItems(
                grid,
                new int[]{2, 5},
                new int[]{0, 0},
                3
        ).isEmpty();
    }

    {
        // Unreachable because of blockers.
        int[][] grid = {
                {1, 0, 2},
                {0, 0, 1},
                {3, 1, 1}
        };

        assert solution.highestRankedKItems(
                grid,
                new int[]{2, 5},
                new int[]{0, 0},
                5
        ).isEmpty();
    }

    {
        // Same distance -> lower price first.
        int[][] grid = {
                {1, 2},
                {3, 1}
        };

        List<List<Integer>> expected = List.of(
                List.of(0, 1),
                List.of(1, 0)
        );

        assert solution.highestRankedKItems(
                grid,
                new int[]{2, 3},
                new int[]{0, 0},
                2
        ).equals(expected);
    }

    {
        // Same distance and same price -> row then column.
        int[][] grid = {
                {1, 2, 2},
                {1, 1, 1}
        };

        List<List<Integer>> expected = List.of(
                List.of(0, 1),
                List.of(0, 2)
        );

        assert solution.highestRankedKItems(
                grid,
                new int[]{2, 2},
                new int[]{1, 1},
                2
        ).equals(expected);
    }

    {
        // k larger than available items.
        int[][] grid = {
                {1, 2},
                {1, 1}
        };

        List<List<Integer>> expected = List.of(
                List.of(0, 1)
        );

        assert solution.highestRankedKItems(
                grid,
                new int[]{2, 2},
                new int[]{0, 0},
                10
        ).equals(expected);
    }

    {
        // Fully traversable grid with no obstacles.
        int[][] grid = {
                {1, 2, 3},
                {1, 4, 5},
                {1, 6, 7}
        };

        List<List<Integer>> result =
                solution.highestRankedKItems(
                        grid,
                        new int[]{2, 7},
                        new int[]{0, 0},
                        6
                );

        assert result.size() == 6;
    }

    System.out.println("All tests passed.");
}
}