package org.chijai.day7.session1.heap;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * ============================================================================
 * IPO
 * ============================================================================
 *
 * Pattern:
 *      Two Heaps + Greedy
 *
 * Two questions:
 *
 *      Which projects are affordable?
 *          -> MinHeap by required capital
 *
 *      Which affordable project is best?
 *          -> MaxHeap by profit
 *
 * Complexity:
 *
 *      O((n + k) log n)
 *      O(n) space
 *
 * LeetCode:
 *      https://leetcode.com/problems/ipo/
 */
public class IPO {

    /*
     * Java 16+:
     *
     * A record is enough because Project is only an immutable data carrier.
     */
    private record Project(int capital, int profit) {
    }

    /*
     * =========================================================================
     * PRIMARY INTERVIEW SOLUTION
     * =========================================================================
     */

    public int findMaximizedCapital(
            int k,
            int w,
            int[] profits,
            int[] capital) {

        if (profits == null
                || capital == null
                || profits.length != capital.length) {
            throw new IllegalArgumentException("Invalid project arrays.");
        }

        PriorityQueue<Project> byCapital =
                new PriorityQueue<>(
                        Comparator.comparingInt(Project::capital)
                );

        PriorityQueue<Project> byProfit =
                new PriorityQueue<>(
                        Comparator.comparingInt(Project::profit)
                                .reversed()
                );

        int index = 0;

        while (index < profits.length) {

            byCapital.offer(
                    new Project(
                            capital[index],
                            profits[index]
                    )
            );

            index++;
        }

        int currentCapital = w;
        int completed = 0;

        while (completed < k) {

            // Move every newly affordable project into the candidate heap.
            while (!byCapital.isEmpty()
                    && byCapital.peek().capital() <= currentCapital) {

                byProfit.offer(byCapital.poll());
            }

            // No affordable project -> capital cannot grow further.
            if (byProfit.isEmpty()) {
                break;
            }

            // Best affordable choice.
            currentCapital += byProfit.poll().profit();

            completed++;
        }

        return currentCapital;
    }

    /*
     * =========================================================================
     * WHY?
     * =========================================================================
     *
     * INVARIANT
     *
     *      byCapital
     *          = locked projects
     *
     *      byProfit
     *          = every affordable, unchosen project
     *
     * ------------------------------------------------------------
     *
     * WHY MOVE ALL AFFORDABLE PROJECTS?
     *
     * Once a project is affordable, it remains affordable because
     * currentCapital never decreases.
     *
     * ------------------------------------------------------------
     *
     * WHY TAKE MAXIMUM PROFIT?
     *
     * Among currently affordable projects, the largest profit leaves us
     * with at least as much capital as any alternative.
     *
     * More capital cannot reduce future options.
     * It can only preserve or unlock more projects.
     *
     * ------------------------------------------------------------
     *
     * WHY STOP WHEN byProfit IS EMPTY?
     *
     * No project is affordable.
     *
     * Capital increases only by completing a project.
     *
     * Therefore no further project can become affordable.
     */

    /*
     * =========================================================================
     * MODERN JAVA — WHAT EARNS ITS PLACE?
     * =========================================================================
     *
     * record Project(...)
     *      removes DTO boilerplate
     *
     * Comparator.comparingInt(Project::capital)
     *      states the MinHeap ordering directly
     *
     * Comparator.comparingInt(Project::profit).reversed()
     *      states the MaxHeap ordering directly
     *
     * Keep loops imperative.
     *
     * The algorithm is stateful:
     *
     *      currentCapital changes
     *          -> eligibility changes
     *
     * Explicit while-loops make that transition easiest to derive
     * and reproduce in an interview.
     */

    /*
     * =========================================================================
     * DRY RUN
     * =========================================================================
     *
     * k = 2
     * w = 0
     *
     * profits = [1,2,3]
     * capital = [0,1,1]
     *
     * capital=0
     *      unlock profit 1
     *      choose 1
     *      capital=1
     *
     * capital=1
     *      unlock profits 2 and 3
     *      choose 3
     *      capital=4
     */

    /*
     * =========================================================================
     * 30-SECOND RECALL
     * =========================================================================
     *
     * LOCKED
     *      MinHeap by capital
     *
     * ELIGIBLE
     *      MaxHeap by profit
     *
     * LOOP
     *
     *      unlock all affordable
     *
     *      if none
     *          stop
     *
     *      choose max profit
     *
     *      repeat at most k times
     *
     * Mental model:
     *
     *      LOCKED
     *          --capital sufficient-->
     *      ELIGIBLE
     *          --max profit-->
     *      CHOSEN
     */

    /*
     * =========================================================================
     * INTERVIEW ARTICULATION
     * =========================================================================
     *
     * "I use a min-heap by required capital to expose projects as they become
     * affordable, and a max-heap by profit to choose the best currently
     * affordable project. Each round I unlock everything I can afford and
     * then choose maximum profit. That greedy choice is safe because more
     * current capital cannot reduce future options. Complexity is
     * O((n+k) log n)."
     */

    /*
     * =========================================================================
     * RELATED / REINFORCEMENT
     * =========================================================================
     *
     * Find Median from Data Stream
     *      also two heaps
     *      but heaps represent lower/upper halves
     *
     * Reusable lesson:
     *
     *      SAME DATA STRUCTURES
     *      !=
     *      SAME INVARIANT
     */

    public static void main(String[] args) {

        IPO solution = new IPO();

        assert solution.findMaximizedCapital(
                2,
                0,
                new int[]{1, 2, 3},
                new int[]{0, 1, 1}
        ) == 4;

        assert solution.findMaximizedCapital(
                3,
                0,
                new int[]{1, 2, 3},
                new int[]{1, 1, 2}
        ) == 0;

        assert solution.findMaximizedCapital(
                1,
                2,
                new int[]{5},
                new int[]{2}
        ) == 7;

        System.out.println("IPO: all assertions passed.");
    }
}
