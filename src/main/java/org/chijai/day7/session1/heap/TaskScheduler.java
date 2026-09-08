package org.chijai.day7.session1.heap;

import java.util.*;

/**
 * ==========================================================================
 * Task Scheduler
 * ==========================================================================
 *
 * LeetCode 621
 * https://leetcode.com/problems/task-scheduler/
 *
 * --------------------------------------------------------------------------
 * PROBLEM
 * --------------------------------------------------------------------------
 *
 * You are given an array of CPU tasks.
 *
 * Each task is represented by an uppercase English letter.
 * The same letter means the same type of task.
 *
 * Example:
 *
 *     [A, A, A, B, B, B]
 *
 * means task A must execute three times and task B must execute three times.
 *
 * Every task takes exactly one unit of time.
 *
 * At every unit of time, the CPU can either:
 *
 *     1. execute one task
 *     2. remain idle
 *
 * There is also a cooldown value n.
 *
 * After executing a task, the SAME task cannot execute again until at least
 * n other time units have passed.
 *
 * Different tasks do not have to wait for each other.
 *
 * Example:
 *
 *     n = 2
 *
 * If A executes now:
 *
 *     A _ _ A
 *       ^ ^
 *       2 cooldown units
 *
 * Therefore the earliest another A can execute is n + 1 positions later.
 *
 * We may reorder the tasks in any way.
 *
 * Goal:
 *
 *     Return the minimum total number of time units required
 *     to finish every task.
 *
 * The returned time includes both:
 *
 *     task execution slots
 *     idle slots
 *
 * --------------------------------------------------------------------------
 * EXAMPLE 1
 * --------------------------------------------------------------------------
 *
 * Input:
 *
 *     tasks = [A, A, A, B, B, B]
 *     n = 2
 *
 * One optimal schedule is:
 *
 *     A B idle A B idle A B
 *
 * Check task A:
 *
 *     A B idle A
 *       ^   ^
 *       2 units between A executions
 *
 * Check task B:
 *
 *     B idle A B
 *       ^    ^
 *       2 units between B executions
 *
 * Total time:
 *
 *     8
 *
 * --------------------------------------------------------------------------
 * EXAMPLE 2
 * --------------------------------------------------------------------------
 *
 * Input:
 *
 *     tasks = [A, A, A, B, B, B]
 *     n = 0
 *
 * There is no cooldown requirement.
 *
 * Therefore all six tasks can run continuously in any order.
 *
 * Example:
 *
 *     A B A B A B
 *
 * Total time:
 *
 *     6
 *
 * --------------------------------------------------------------------------
 * EXAMPLE 3
 * --------------------------------------------------------------------------
 *
 * Input:
 *
 *     tasks = [A, A, A, A, A, A, B, C, D, E, F, G]
 *     n = 2
 *
 * One optimal schedule is:
 *
 *     A B C A D E A F G A idle idle A idle idle A
 *
 * A occurs much more frequently than every other task.
 *
 * The other tasks fill many of the required cooldown positions,
 * but eventually there are not enough different tasks left.
 *
 * At that point idle slots become unavoidable.
 *
 * Total time:
 *
 *     16
 *
 * --------------------------------------------------------------------------
 * CONSTRAINTS
 * --------------------------------------------------------------------------
 *
 *     1 <= tasks.length <= 10^4
 *     tasks[i] is an uppercase English letter
 *     0 <= n <= 100
 */
public class TaskScheduler {

    /**
     * ======================================================================
     * PRIMARY SOLUTION
     * Max Heap + Cooling Window
     * ======================================================================
     *
     * This is the easiest solution to reconstruct directly from the problem.
     *
     * The central observation is:
     *
     *     if cooldown = n,
     *     the same task can appear at most once inside a window of n + 1 slots.
     *
     * Why n + 1?
     *
     * Suppose n = 2.
     *
     *     A _ _ A
     *
     * After executing A, two complete units must pass before A may execute
     * again. Therefore the next A is three positions later.
     *
     * So we process work in windows of:
     *
     *     n + 1
     *
     * Inside each window we greedily execute the tasks having the highest
     * remaining frequencies.
     *
     * A task removed from the heap is NOT immediately inserted back.
     * We hold it in temp until the current window ends.
     *
     * That single rule guarantees that the same task cannot be selected twice
     * inside the same cooldown window.
     *
     * ----------------------------------------------------------------------
     * DRY RUN
     * ----------------------------------------------------------------------
     *
     * tasks = [A, A, A, B, B, B]
     * n = 2
     *
     * Frequencies:
     *
     *     A = 3
     *     B = 3
     *
     * Heap:
     *
     *     [3, 3]
     *
     * cycleLen = n + 1 = 3
     *
     * --------------------
     * Window 1
     * --------------------
     *
     * poll 3 -> execute A
     * remaining A = 2
     * temp = [2]
     *
     * poll 3 -> execute B
     * remaining B = 2
     * temp = [2, 2]
     *
     * heap is empty.
     * One position remains in this 3-slot window.
     * CPU must idle.
     *
     * Timeline:
     *
     *     A B idle
     *
     * Put unfinished tasks back:
     *
     *     heap = [2, 2]
     *
     * Work still remains, so this window costs the full cycleLen = 3.
     *
     * time = 3
     *
     * --------------------
     * Window 2
     * --------------------
     *
     * Execute A, execute B, then idle.
     *
     *     A B idle
     *
     * heap becomes:
     *
     *     [1, 1]
     *
     * time = 6
     *
     * --------------------
     * Window 3
     * --------------------
     *
     * Execute A.
     * Execute B.
     *
     * Both frequencies become zero.
     * Nothing is inserted back into the heap.
     *
     * The job is finished, so we do NOT pay for the unused third slot.
     *
     *     A B
     *
     * time += work = 2
     *
     * Final time:
     *
     *     3 + 3 + 2 = 8
     *
     * ----------------------------------------------------------------------
     * work VS cycleLen
     * ----------------------------------------------------------------------
     *
     * work
     *
     *     Number of actual tasks executed in the current window.
     *
     * cycleLen
     *
     *     Maximum length of the current cooldown window.
     *
     *     cycleLen = n + 1
     *
     * Example with n = 2:
     *
     *     A B idle
     *
     * Here:
     *
     *     work = 2
     *     cycleLen = 3
     *
     * If tasks still remain after this window, that idle slot is real elapsed
     * time, so we add cycleLen.
     *
     * If this is the final window:
     *
     *     A B
     *
     * then work = 2 and cycleLen = 3, but the CPU is already finished after
     * those two executions. There is no trailing cooldown to wait for, so we
     * add only work.
     *
     * Therefore:
     *
     *     tasks remain  -> add cycleLen
     *     job finished  -> add work
     *
     * ----------------------------------------------------------------------
     * COMPLEXITY
     * ----------------------------------------------------------------------
     *
     * Let T = tasks.length.
     *
     * There are at most 26 task types.
     *
     * Each actual task execution causes one heap removal and possibly one
     * heap insertion.
     *
     * Heap size is at most 26.
     *
     * Time:
     *
     *     O(T log 26)
     *
     * Since 26 is fixed, this behaves like O(T).
     *
     * Space:
     *
     *     O(26)
     */
    static class Primary {

        public int leastInterval(char[] tasks, int n) {
            if (n == 0) return tasks.length;

            Map<Character, Integer> freq = new HashMap<>();
            for (char c : tasks) freq.merge(c, 1, Integer::sum);

            PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            maxHeap.addAll(freq.values());

            int time = 0;
            int cycleLen = n + 1;
            List<Integer> temp = new ArrayList<>();

            while (!maxHeap.isEmpty()) {
                temp.clear();
                int work = 0;

                for (int i = 0; i < cycleLen; i++) {
                    if (!maxHeap.isEmpty()) {
                        int remaining = maxHeap.poll();
                        remaining--;
                        temp.add(remaining);
                        work++;
                    } else {
                        break;
                    }
                }

                for (int cnt : temp)
                    if (cnt > 0) maxHeap.offer(cnt);

                /*
                 * work = actual tasks executed in this window.
                 *
                 * cycleLen = total length of a full cooldown window,
                 * including idle slots if tasks are still unfinished.
                 *
                 * If unfinished tasks remain, the whole window must pass
                 * before tasks used in this window can run again.
                 *
                 * If no tasks remain, this is the final window and there is
                 * no reason to count unused trailing idle slots.
                 */
                time += maxHeap.isEmpty() ? work : cycleLen;
            }

            return time;
        }
    }

    /**
     * ======================================================================
     * SECONDARY SOLUTION
     * Frequency Mathematics
     * ======================================================================
     *
     * This solution does not construct the schedule.
     *
     * Instead, it calculates how many idle slots are forced by the most
     * frequent task and then lets the remaining tasks fill those slots.
     *
     * ----------------------------------------------------------------------
     * DRY RUN
     * ----------------------------------------------------------------------
     *
     * tasks = [A, A, A, B, B, B]
     * n = 2
     *
     * Frequencies after sorting:
     *
     *     ... 0 0 0 3 3
     *
     * Pick one maximum-frequency task as the skeleton.
     *
     * Suppose that task is A.
     *
     *     A _ _ A _ _ A
     *
     * A appears 3 times.
     *
     * The final A does not need a cooldown after it.
     * Therefore the number of cooling partitions is:
     *
     *     max = 3 - 1 = 2
     *
     * Each partition initially needs n = 2 positions.
     *
     *     spaces = max * n
     *            = 2 * 2
     *            = 4
     *
     * Skeleton:
     *
     *     A _ _ | A _ _ | A
     *
     * B occurs 3 times.
     *
     * B can fill at most one position inside each of the two cooling
     * partitions.
     *
     * Therefore:
     *
     *     spaces -= min(max, frequency(B))
     *             = min(2, 3)
     *             = 2
     *
     * Remaining idle spaces:
     *
     *     4 - 2 = 2
     *
     * Schedule length:
     *
     *     tasks.length + spaces
     *     = 6 + 2
     *     = 8
     *
     * One corresponding schedule is:
     *
     *     A B idle A B idle A B
     *
     * ----------------------------------------------------------------------
     * WHY Math.min(max, cnt[i])?
     * ----------------------------------------------------------------------
     *
     * There are only max cooling partitions.
     *
     * A different task can fill at most one slot in each partition.
     *
     * Therefore even if another task appears more than max times, only max of
     * its occurrences can reduce these internal idle positions.
     *
     * ----------------------------------------------------------------------
     * COMPLEXITY
     * ----------------------------------------------------------------------
     *
     * Counting all tasks:
     *
     *     O(T)
     *
     * Sorting exactly 26 frequencies:
     *
     *     O(26 log 26)
     *
     * Total:
     *
     *     O(T + 26 log 26) = O(T)
     *
     * Space:
     *
     *     O(26)
     */
    static class Mathematical {

        public int leastInterval(char[] tasks, int n) {
            if (tasks == null || tasks.length == 0) {
                return 0;
            }

            int[] cnt = new int[26];

            for (char c : tasks) {
                cnt[c - 'A']++;
            }

            Arrays.sort(cnt);

            int max = cnt[25] - 1;
            int spaces = max * n;

            for (int i = 24; i >= 0; i--) {
                spaces -= Math.min(max, cnt[i]);
            }

            spaces = Math.max(0, spaces);

            return tasks.length + spaces;
        }
    }

    /**
     * ======================================================================
     * HOW TO RECONSTRUCT THE PRIMARY SOLUTION
     * ======================================================================
     *
     * Start only from the cooldown rule.
     *
     * Same task needs n units between executions.
     *
     * Therefore:
     *
     *     same task appears at most once
     *     inside n + 1 positions
     *
     * That suggests processing one n + 1 window at a time.
     *
     * Which tasks should go into the window first?
     *
     * The ones with the largest remaining frequencies are the hardest to
     * place later, so choose them first.
     *
     * That suggests a max heap.
     *
     * How do we prevent selecting the same task twice in one window?
     *
     * Remove it from the heap and keep its reduced frequency outside the heap
     * until the current window ends.
     *
     * That suggests the temp list.
     *
     * Finally:
     *
     *     if work remains -> whole window counts
     *     if no work remains -> only actual work in final window counts
     *
     * This reconstructs the entire primary implementation without memorizing
     * a formula.
     */

    /**
     * ======================================================================
     * COMMON TRAPS
     * ======================================================================
     *
     * 1. Using n instead of n + 1 as the window length.
     *
     *    n is the number of units BETWEEN equal tasks.
     *    The distance from one execution to the next is therefore n + 1.
     *
     * 2. Putting a task back into the heap immediately after executing it.
     *
     *    That would allow the same task to be chosen again inside the same
     *    cooling window.
     *
     * 3. Always adding cycleLen to time.
     *
     *    The final window does not need trailing idle time after all work is
     *    finished.
     *
     * 4. Thinking every cooldown produces idle time.
     *
     *    Other task types can occupy those cooldown positions.
     *
     * 5. Sorting tasks once and then executing in that order.
     *
     *    Remaining frequencies change after every execution. The greedy
     *    priority must therefore adapt dynamically.
     */

    /**
     * ======================================================================
     * PATTERN RECOGNITION
     * ======================================================================
     *
     * Trigger words:
     *
     *     repeated tasks
     *     cooldown
     *     minimum schedule length
     *     idle slots
     *     same item must stay apart
     *
     * Primary pattern:
     *
     *     Greedy + Max Heap + Fixed Cooling Window
     *
     * Related problem:
     *
     *     Rearrange String K Distance Apart
     *
     * The connection is direct:
     *
     *     CPU time distance here
     *     = character distance there
     */

    /**
     * ======================================================================
     * RECALL CARD
     * ======================================================================
     *
     * Cooldown n
     *      ↓
     * window size n + 1
     *      ↓
     * same task at most once per window
     *      ↓
     * choose highest remaining frequencies first
     *      ↓
     * max heap
     *      ↓
     * hold used tasks outside heap until window ends
     *      ↓
     * reinsert unfinished frequencies
     *      ↓
     * full cycle if work remains
     * actual work only for final cycle
     */

    public static void main(String[] args) {

        Primary primary = new Primary();
        Mathematical mathematical = new Mathematical();

        assert primary.leastInterval(
                new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 2) == 8;

        assert primary.leastInterval(
                new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 0) == 6;

        assert primary.leastInterval(
                new char[]{
                        'A', 'A', 'A', 'A', 'A', 'A',
                        'B', 'C', 'D', 'E', 'F', 'G'
                }, 2) == 16;

        assert primary.leastInterval(
                new char[]{'A'}, 100) == 1;

        assert primary.leastInterval(
                new char[]{'A', 'A', 'A'}, 2) == 7;

        assert mathematical.leastInterval(
                new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 2) == 8;

        assert mathematical.leastInterval(
                new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 0) == 6;

        assert mathematical.leastInterval(
                new char[]{
                        'A', 'A', 'A', 'A', 'A', 'A',
                        'B', 'C', 'D', 'E', 'F', 'G'
                }, 2) == 16;

        System.out.println("All Task Scheduler tests passed.");
    }
}
