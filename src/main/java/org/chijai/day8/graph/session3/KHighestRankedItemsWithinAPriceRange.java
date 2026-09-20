package org.chijai.day8.graph.session3;

import java.util.*;

/**
 * ==========================================================================
 * K Highest Ranked Items Within a Price Range — Java Gold
 * ==========================================================================
 *
 * LeetCode 2146
 *
 * Core classification:
 *
 *     Graph / Grid
 *     Unweighted Shortest Path
 *     BFS Level Traversal
 *     Multi-Criteria Ranking
 *
 * Core memory sentence:
 *
 *     BFS sorts DISTANCE.
 *     Comparator sorts TIES inside the same distance.
 *
 * ==========================================================================
 * 1. PROBLEM STATEMENT
 * ==========================================================================
 *
 * You are given a grid where:
 *
 *     0   = blocked cell; cannot enter
 *     1   = empty traversable cell
 *     > 1 = item whose price is the cell value
 *
 * Starting from start = [row, col], you may move one cell at a time:
 *
 *     up / down / left / right
 *
 * You are also given:
 *
 *     pricing = [low, high]
 *
 * An item qualifies when:
 *
 *     low <= itemPrice <= high
 *
 * Return the coordinates of at most k reachable qualifying items according
 * to this ranking order:
 *
 *     1. smaller SHORTEST-PATH DISTANCE
 *     2. smaller PRICE
 *     3. smaller ROW
 *     4. smaller COLUMN
 *
 * Example:
 *
 *     grid =
 *
 *         1  2  0  1
 *         1  3  0  1
 *         0  2  5  1
 *
 *     pricing = [2, 5]
 *     start   = [0, 0]
 *     k       = 3
 *
 * Reachable qualifying items:
 *
 *     coordinate   distance   price
 *     --------------------------------
 *     [0,1]            1        2
 *     [1,1]            2        3
 *     [2,1]            3        2
 *     [2,2]            4        5
 *
 * Therefore:
 *
 *     answer = [[0,1], [1,1], [2,1]]
 *
 * ==========================================================================
 * 2. RECOGNITION + FIRST-PRINCIPLES INVENTION PATH
 * ==========================================================================
 *
 * STEP 1 — What is the FIRST ranking key?
 *
 *     shortest-path distance
 *
 * STEP 2 — Are all moves equal cost?
 *
 *     yes; every grid move costs exactly 1
 *
 * Therefore:
 *
 *     shortest path in an unweighted graph
 *     -> BFS
 *
 * STEP 3 — Notice what BFS gives us for free.
 *
 * BFS processes:
 *
 *     distance 0
 *     distance 1
 *     distance 2
 *     distance 3
 *     ...
 *
 * Therefore we do NOT need to compare distance between items belonging to
 * different BFS levels. Earlier levels automatically outrank later levels.
 *
 * STEP 4 — What remains unresolved inside one BFS level?
 *
 * Every item in the same level has the SAME shortest distance.
 *
 * So only sort by:
 *
 *     price
 *     -> row
 *     -> column
 *
 * STEP 5 — Stop once k ranked items have been emitted.
 *
 * The whole solution becomes:
 *
 *     BFS one distance level
 *         -> collect qualifying items
 *         -> sort only that level
 *         -> append to answer
 *         -> stop at k
 */
public class KHighestRankedItemsWithinAPriceRange {

    private static final int[][] DIRECTIONS = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };


    /**
     * Traversal state only.
     */
    record Cell(
            int row,
            int col) {
    }


    /**
     * Ranking state.
     *
     * Distance is intentionally absent because all RankedItems produced by
     * processCurrentLevel() already belong to the same BFS distance.
     */
    record RankedItem(
            int price,
            int row,
            int col) {

        static final Comparator<RankedItem> COMPARATOR =
                Comparator
                        .comparingInt(RankedItem::price)
                        .thenComparingInt(RankedItem::row)
                        .thenComparingInt(RankedItem::col);
    }


    /**
     * ======================================================================
     * 3. ⭐ PRIMARY SOLUTION — PHOTOGRAPHIC CODE
     * ======================================================================
     *
     * Keep this code visually together.
     *
     * Mental skeleton:
     *
     *     initialize BFS
     *     -> process current distance
     *     -> sort that distance
     *     -> append until k
     */
    static class Optimal {

        public List<List<Integer>> highestRankedKItems(
                int[][] grid,
                int[] pricing,
                int[] start,
                int k) {

            int rows = grid.length;
            int cols = grid[0].length;

            boolean[][] visited =
                    new boolean[rows][cols];

            Queue<Cell> queue =
                    new ArrayDeque<>();

            queue.offer(
                    new Cell(start[0], start[1]));

            visited[start[0]][start[1]] = true;

            List<List<Integer>> answer =
                    new ArrayList<>();

            while (!queue.isEmpty()) {

                List<RankedItem> currentDistanceItems =
                        processCurrentLevel(
                                grid,
                                pricing,
                                visited,
                                queue);

                currentDistanceItems.sort(
                        RankedItem.COMPARATOR);

                for (RankedItem item : currentDistanceItems) {

                    answer.add(
                            List.of(item.row(), item.col()));

                    if (answer.size() == k) {
                        return answer;
                    }
                }
            }

            return answer;
        }


        private List<RankedItem> processCurrentLevel(
                int[][] grid,
                int[] pricing,
                boolean[][] visited,
                Queue<Cell> queue) {

            int rows = grid.length;
            int cols = grid[0].length;

            int levelSize =
                    queue.size();

            List<RankedItem> currentDistanceItems =
                    new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {

                Cell current =
                        queue.poll();

                int cellValue =
                        grid[current.row()][current.col()];

                if (cellValue >= pricing[0]
                        && cellValue <= pricing[1]) {

                    currentDistanceItems.add(
                            new RankedItem(
                                    cellValue,
                                    current.row(),
                                    current.col()));
                }

                for (int[] direction : DIRECTIONS) {

                    int nextRow =
                            current.row() + direction[0];

                    int nextCol =
                            current.col() + direction[1];

                    if (nextRow < 0
                            || nextRow >= rows
                            || nextCol < 0
                            || nextCol >= cols) {
                        continue;
                    }

                    if (grid[nextRow][nextCol] == 0) {
                        continue;
                    }

                    if (visited[nextRow][nextCol]) {
                        continue;
                    }

                    visited[nextRow][nextCol] = true;

                    queue.offer(
                            new Cell(nextRow, nextCol));
                }
            }

            return currentDistanceItems;
        }
    }


    /**
     * ======================================================================
     * 4. PRIMARY SOLUTION EXPLANATION
     * ======================================================================
     *
     * ----------------------------------------------------------------------
     * WHY BFS, NOT DFS?
     * ----------------------------------------------------------------------
     *
     * This problem is different from pure reachability problems such as:
     *
     *     Number of Provinces
     *     Pacific Atlantic Water Flow
     *
     * Those only ask whether something is connected/reachable, so DFS and BFS
     * are interchangeable and DFS may be shorter.
     *
     * Here the FIRST ranking key is:
     *
     *     shortest distance
     *
     * BFS preserves shortest distance in an unweighted graph.
     * DFS does not.
     *
     * ----------------------------------------------------------------------
     * WHY levelSize?
     * ----------------------------------------------------------------------
     *
     * At the beginning of an iteration:
     *
     *     levelSize = queue.size()
     *
     * freezes the number of cells belonging to the CURRENT distance.
     *
     * While those cells are processed, their neighbors are appended to the
     * same queue. Therefore the queue may temporarily contain:
     *
     *     remaining current-level cells
     *     +
     *     newly discovered next-level cells
     *
     * The queue itself is NOT always "exactly one level".
     * levelSize is what separates the levels.
     *
     * ----------------------------------------------------------------------
     * WHY MARK visited WHEN ENQUEUING?
     * ----------------------------------------------------------------------
     *
     * The first time an unweighted BFS discovers a cell, that route is already
     * a shortest route to it.
     *
     * Therefore:
     *
     *     visited[next] = true
     *     queue.offer(next)
     *
     * happen together.
     *
     * If visited were delayed until dequeue, multiple parents could enqueue the
     * same cell unnecessarily.
     *
     * ----------------------------------------------------------------------
     * WHY DOES RankedItem NOT STORE distance?
     * ----------------------------------------------------------------------
     *
     * processCurrentLevel() returns only items from ONE BFS level.
     *
     * Therefore every returned item already has identical distance.
     *
     * Comparator only needs the remaining ranking keys:
     *
     *     price
     *     -> row
     *     -> column
     *
     * This is the central insight:
     *
     *     BFS handles distance.
     *     RankedItem.COMPARATOR handles ties.
     *
     * ----------------------------------------------------------------------
     * WHY CAN WE RETURN AS SOON AS answer.size() == k?
     * ----------------------------------------------------------------------
     *
     * Items from earlier BFS levels always outrank every later level.
     *
     * Inside the current level, items have already been fully sorted by:
     *
     *     price -> row -> column
     *
     * So after taking the kth item, nothing remaining can outrank it.
     */


    /**
     * ======================================================================
     * 5. PRIMARY VISUAL DRY RUN
     * ======================================================================
     *
     * grid =
     *
     *     1  2  0  1
     *     1  3  0  1
     *     0  2  5  1
     *
     * start   = [0,0]
     * pricing = [2,5]
     * k       = 3
     *
     * ----------------------------------------------------------------------
     * DISTANCE 0
     * ----------------------------------------------------------------------
     *
     * queue at level start:
     *
     *     [0,0]
     *
     * value = 1
     * not an item
     *
     * enqueue:
     *
     *     [0,1]
     *     [1,0]
     *
     * currentDistanceItems = []
     *
     * ----------------------------------------------------------------------
     * DISTANCE 1
     * ----------------------------------------------------------------------
     *
     * frozen current level:
     *
     *     [0,1], [1,0]
     *
     * [0,1] has price 2 -> qualifies
     * [1,0] has value 1 -> road
     *
     * currentDistanceItems:
     *
     *     (price=2,row=0,col=1)
     *
     * answer:
     *
     *     [[0,1]]
     *
     * ----------------------------------------------------------------------
     * DISTANCE 2
     * ----------------------------------------------------------------------
     *
     * [1,1] has price 3 -> qualifies
     *
     * answer:
     *
     *     [[0,1], [1,1]]
     *
     * ----------------------------------------------------------------------
     * DISTANCE 3
     * ----------------------------------------------------------------------
     *
     * [2,1] has price 2 -> qualifies
     *
     * answer:
     *
     *     [[0,1], [1,1], [2,1]]
     *
     * answer.size() == k
     * -> return immediately
     */


    /**
     * ======================================================================
     * 6. COMPLEXITY DERIVATION
     * ======================================================================
     *
     * Let:
     *
     *     R = rows
     *     C = columns
     *     V = R * C reachable-grid upper bound
     *
     * BFS:
     *
     *     every cell is enqueued at most once
     *     every processed cell checks four neighbors
     *
     *     O(V)
     *
     * Sorting:
     *
     * Suppose BFS levels contain:
     *
     *     t1, t2, ... items
     *
     * Sorting cost is:
     *
     *     Σ ti log ti
     *
     * which is at most:
     *
     *     O(V log V)
     *
     * Therefore worst-case total:
     *
     *     Time = O(R * C * log(R * C))
     *
     * Space:
     *
     *     visited                 O(R * C)
     *     BFS queue               O(R * C)
     *     currentDistanceItems    O(R * C) worst case
     *
     *     Space = O(R * C)
     */


    /**
     * ======================================================================
     * 7. DISTINCT ALTERNATIVE — STORE DISTANCE + GLOBAL SORT
     * ======================================================================
     *
     * This is VALID, not wrong.
     *
     * Difference from the primary:
     *
     * PRIMARY
     *     BFS already orders distance
     *     -> sort only same-distance items
     *
     * ALTERNATIVE
     *     explicitly store distance on every candidate
     *     -> collect all candidates
     *     -> globally sort by distance, price, row, column
     *
     * The alternative is conceptually simpler as a stepping stone, but stores
     * and sorts information that BFS already gave us implicitly.
     */
    static class GlobalSortAlternative {

        record Candidate(
                int distance,
                int price,
                int row,
                int col) {

            static final Comparator<Candidate> COMPARATOR =
                    Comparator
                            .comparingInt(Candidate::distance)
                            .thenComparingInt(Candidate::price)
                            .thenComparingInt(Candidate::row)
                            .thenComparingInt(Candidate::col);
        }


        public List<List<Integer>> highestRankedKItems(
                int[][] grid,
                int[] pricing,
                int[] start,
                int k) {

            int rows = grid.length;
            int cols = grid[0].length;

            boolean[][] visited =
                    new boolean[rows][cols];

            Queue<Cell> queue =
                    new ArrayDeque<>();

            queue.offer(
                    new Cell(start[0], start[1]));

            visited[start[0]][start[1]] = true;

            List<Candidate> candidates =
                    new ArrayList<>();

            int distance = 0;

            while (!queue.isEmpty()) {

                int levelSize =
                        queue.size();

                for (int i = 0; i < levelSize; i++) {

                    Cell current =
                            queue.poll();

                    int cellValue =
                            grid[current.row()][current.col()];

                    if (cellValue >= pricing[0]
                            && cellValue <= pricing[1]) {

                        candidates.add(
                                new Candidate(
                                        distance,
                                        cellValue,
                                        current.row(),
                                        current.col()));
                    }

                    for (int[] direction : DIRECTIONS) {

                        int nextRow =
                                current.row() + direction[0];

                        int nextCol =
                                current.col() + direction[1];

                        if (nextRow < 0
                                || nextRow >= rows
                                || nextCol < 0
                                || nextCol >= cols) {
                            continue;
                        }

                        if (grid[nextRow][nextCol] == 0) {
                            continue;
                        }

                        if (visited[nextRow][nextCol]) {
                            continue;
                        }

                        visited[nextRow][nextCol] = true;

                        queue.offer(
                                new Cell(nextRow, nextCol));
                    }
                }

                distance++;
            }

            candidates.sort(
                    Candidate.COMPARATOR);

            List<List<Integer>> answer =
                    new ArrayList<>();

            for (Candidate candidate : candidates) {

                if (answer.size() == k) {
                    break;
                }

                answer.add(
                        List.of(
                                candidate.row(),
                                candidate.col()));
            }

            return answer;
        }
    }


    /**
     * ======================================================================
     * 8. 🔁 PATTERN REUSE — SAME ENGINE, STORE ONLY THE Δ
     * ======================================================================
     *
     * BASE ENGINE
     *
     *     unweighted graph/grid
     *     -> BFS by level
     *     -> first discovery gives shortest distance
     *
     * ----------------------------------------------------------------------
     * Δ1 — MULTIPLE STARTING LOCATIONS
     * ----------------------------------------------------------------------
     *
     * Example:
     *
     *     Find items nearest to ANY warehouse.
     *
     * SAME:
     *
     *     BFS levels still mean shortest distance.
     *
     * CHANGE:
     *
     *     seed every start into the queue at distance 0
     *     mark every start visited
     *
     * This becomes multi-source BFS.
     *
     * ----------------------------------------------------------------------
     * Δ2 — DIFFERENT TIE-BREAKERS
     * ----------------------------------------------------------------------
     *
     * Ranking changes from:
     *
     *     distance -> price -> row -> col
     *
     * to:
     *
     *     distance -> rating -> price
     *
     * SAME:
     *
     *     BFS still handles distance.
     *
     * CHANGE:
     *
     *     RankedItem fields + comparator only.
     *
     * ----------------------------------------------------------------------
     * Δ3 — EIGHT-DIRECTION MOVEMENT
     * ----------------------------------------------------------------------
     *
     * SAME:
     *
     *     BFS engine
     *     visited rule
     *     level processing
     *
     * CHANGE:
     *
     *     DIRECTIONS only.
     *
     * ----------------------------------------------------------------------
     * Δ4 — RETURN ONLY THE NEAREST QUALIFYING ITEM
     * ----------------------------------------------------------------------
     *
     * Process one BFS level completely.
     *
     * If that level contains qualifying items:
     *
     *     sort that level
     *     return its first item
     *
     * Do NOT return immediately upon seeing the first qualifying cell because
     * another item in the SAME distance may win by price/row/column.
     *
     * ----------------------------------------------------------------------
     * Δ5 — WEIGHTED ROADS
     * ----------------------------------------------------------------------
     *
     * Example:
     *
     *     each move has a different travel cost.
     *
     * Pattern breaks:
     *
     *     BFS levels no longer mean minimum cost.
     *
     * Replace BFS with:
     *
     *     Dijkstra
     *
     * New primary key:
     *
     *     minimum accumulated cost
     */


    /**
     * ======================================================================
     * 9. COMMON TRAPS
     * ======================================================================
     *
     * TRAP 1 — DFS
     *
     * DFS can discover a farther item before a nearer item.
     * It does not preserve shortest-distance ranking.
     *
     * TRAP 2 — Thinking the queue always contains one level.
     *
     * During processing, the queue can contain the remainder of the current
     * level plus nodes from the next level.
     *
     * levelSize freezes the current level.
     *
     * TRAP 3 — Marking visited on dequeue.
     *
     * Multiple parents may enqueue the same cell.
     * Mark visited when the cell is first enqueued.
     *
     * TRAP 4 — Putting distance into RankedItem.COMPARATOR.
     *
     * currentDistanceItems already contains exactly one BFS distance.
     * Distance would be redundant there.
     *
     * TRAP 5 — Returning the first qualifying item seen in a level.
     *
     * Same-distance candidates still need:
     *
     *     price -> row -> column
     *
     * ordering.
     *
     * TRAP 6 — Calling global sorting incorrect.
     *
     * Global sorting is correct IF distance is explicitly stored and included
     * as the first comparison key. It is simply a distinct, less specialized
     * solution.
     */


    /**
     * ======================================================================
     * 10. INTERVIEW RECONSTRUCTION SHEET
     * ======================================================================
     *
     * Recognition cue:
     *
     *     "Rank reachable items primarily by shortest distance."
     *
     * Trigger:
     *
     *     shortest distance + equal edge cost
     *     -> BFS
     *
     * Reconstruction:
     *
     *     1. visited[][]
     *     2. queue with start
     *     3. while queue not empty
     *     4. levelSize = queue.size()
     *     5. process exactly levelSize cells
     *     6. collect qualifying items from this level
     *     7. discover valid unvisited neighbors
     *     8. sort current level by price, row, col
     *     9. append to answer
     *    10. stop at k
     *
     * One-liner:
     *
     *     BFS sorts distance; comparator sorts ties.
     *
     * Photographic object map:
     *
     *     Cell
     *     -> traversal state
     *
     *     RankedItem
     *     -> ranking state
     *     -> price + row + col + comparator
     *
     *     processCurrentLevel()
     *     -> one exact BFS distance
     *     -> collect candidates + expand neighbors
     */


    /**
     * ======================================================================
     * 11. MAIN + SELF-VERIFYING TESTS
     * ======================================================================
     */
    public static void main(String[] args) {

        Optimal optimal =
                new Optimal();

        GlobalSortAlternative alternative =
                new GlobalSortAlternative();

        testRepresentativeExample(
                optimal,
                alternative);

        testSameDistancePriceTieBreak(
                optimal,
                alternative);

        testSamePriceRowColumnTieBreak(
                optimal,
                alternative);

        testStartCellQualifies(
                optimal,
                alternative);

        testBlockedItemIsUnreachable(
                optimal,
                alternative);

        testKGreaterThanAvailable(
                optimal,
                alternative);

        System.out.println(
                "All K Highest Ranked Items Java Gold assertions passed.");
    }


    private static void testRepresentativeExample(
            Optimal optimal,
            GlobalSortAlternative alternative) {

        int[][] grid = {
                {1, 2, 0, 1},
                {1, 3, 0, 1},
                {0, 2, 5, 1}
        };

        int[] pricing = {2, 5};
        int[] start = {0, 0};

        List<List<Integer>> expected =
                List.of(
                        List.of(0, 1),
                        List.of(1, 1),
                        List.of(2, 1));

        assert optimal.highestRankedKItems(
                grid,
                pricing,
                start,
                3
        ).equals(expected);

        assert alternative.highestRankedKItems(
                grid,
                pricing,
                start,
                3
        ).equals(expected);
    }


    private static void testSameDistancePriceTieBreak(
            Optimal optimal,
            GlobalSortAlternative alternative) {

        int[][] grid = {
                {1, 2},
                {3, 1}
        };

        List<List<Integer>> expected =
                List.of(
                        List.of(0, 1),
                        List.of(1, 0));

        assertBoth(
                optimal,
                alternative,
                grid,
                new int[]{2, 3},
                new int[]{0, 0},
                2,
                expected);
    }


    private static void testSamePriceRowColumnTieBreak(
            Optimal optimal,
            GlobalSortAlternative alternative) {

        int[][] grid = {
                {2, 1, 2},
                {1, 1, 1},
                {2, 1, 2}
        };

        List<List<Integer>> expected =
                List.of(
                        List.of(0, 0),
                        List.of(0, 2),
                        List.of(2, 0),
                        List.of(2, 2));

        assertBoth(
                optimal,
                alternative,
                grid,
                new int[]{2, 2},
                new int[]{1, 1},
                4,
                expected);
    }


    private static void testStartCellQualifies(
            Optimal optimal,
            GlobalSortAlternative alternative) {

        int[][] grid = {
                {5}
        };

        List<List<Integer>> expected =
                List.of(
                        List.of(0, 0));

        assertBoth(
                optimal,
                alternative,
                grid,
                new int[]{2, 6},
                new int[]{0, 0},
                1,
                expected);
    }


    private static void testBlockedItemIsUnreachable(
            Optimal optimal,
            GlobalSortAlternative alternative) {

        int[][] grid = {
                {1, 0, 2},
                {0, 0, 1},
                {3, 1, 1}
        };

        assertBoth(
                optimal,
                alternative,
                grid,
                new int[]{2, 5},
                new int[]{0, 0},
                5,
                Collections.emptyList());
    }


    private static void testKGreaterThanAvailable(
            Optimal optimal,
            GlobalSortAlternative alternative) {

        int[][] grid = {
                {1, 2},
                {1, 1}
        };

        List<List<Integer>> expected =
                List.of(
                        List.of(0, 1));

        assertBoth(
                optimal,
                alternative,
                grid,
                new int[]{2, 2},
                new int[]{0, 0},
                10,
                expected);
    }


    private static void assertBoth(
            Optimal optimal,
            GlobalSortAlternative alternative,
            int[][] grid,
            int[] pricing,
            int[] start,
            int k,
            List<List<Integer>> expected) {

        assert optimal.highestRankedKItems(
                grid,
                pricing,
                start,
                k
        ).equals(expected);

        assert alternative.highestRankedKItems(
                grid,
                pricing,
                start,
                k
        ).equals(expected);
    }
}
