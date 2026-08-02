package org.chijai.day7.session1;

import java.util.*;

/**
 * ============================================================================
 *  TaskScheduler
 * ============================================================================
 *
 * LeetCode: 621
 * Difficulty: Medium
 *
 * Tags
 * ----
 * Greedy
 * Heap (Priority Queue)
 * Counting
 * Simulation
 * Scheduling
 *
 * ----------------------------------------------------------------------------
 * PROBLEM
 * ----------------------------------------------------------------------------
 *
 * You are given an array of CPU tasks.
 *
 * Every task is represented by an uppercase English letter.
 *
 * Every task requires exactly one unit of execution time.
 *
 * Between two executions of the SAME task there must be at least n units
 * of cooldown.
 *
 * During cooldown the CPU may:
 *
 * • execute another task
 * • remain idle
 *
 * Return the minimum total time required to finish every task.
 *
 * ----------------------------------------------------------------------------
 * CONSTRAINTS
 * ----------------------------------------------------------------------------
 *
 * 1 <= tasks.length <= 10^4
 *
 * tasks[i] is an uppercase English letter.
 *
 * 0 <= n <= 100
 *
 * ----------------------------------------------------------------------------
 * EXAMPLE 1
 * ----------------------------------------------------------------------------
 *
 * tasks = [A,A,A,B,B,B]
 * n = 2
 *
 * Timeline:
 *
 * A B idle A B idle A B
 *
 * Answer = 8
 *
 * ----------------------------------------------------------------------------
 * EXAMPLE 2
 * ----------------------------------------------------------------------------
 *
 * tasks = [A,A,A,B,B,B]
 * n = 0
 *
 * Every task may execute immediately.
 *
 * Answer = 6
 *
 * ----------------------------------------------------------------------------
 * EXAMPLE 3
 * ----------------------------------------------------------------------------
 *
 * tasks =
 * [A,A,A,A,A,A,B,C,D,E,F,G]
 *
 * n = 2
 *
 * Answer = 16
 *
 * ----------------------------------------------------------------------------
 * OFFICIAL
 * ----------------------------------------------------------------------------
 *
 * https://leetcode.com/problems/task-scheduler/
 *
 * ============================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern
 * -------
 * Greedy Scheduling using Frequency
 *
 * Alternative Interview Pattern
 * -----------------------------
 * Max Heap + Cooling Window Simulation
 *
 * Stronger Pattern
 * ----------------
 * Frequency Mathematics
 *
 * ----------------------------------------------------------------------------
 * Core Invariant
 * ----------------------------------------------------------------------------
 *
 * The highest-frequency task determines the skeleton of the schedule.
 *
 * Every other task merely fills the gaps created by this skeleton.
 *
 * If gaps cannot be completely filled,
 * they become idle slots.
 *
 * Therefore:
 *
 * We NEVER optimize idle positions directly.
 *
 * We optimize how completely other tasks fill those positions.
 *
 * ----------------------------------------------------------------------------
 * Why It Works
 * ----------------------------------------------------------------------------
 *
 * Suppose
 *
 * A occurs 6 times.
 *
 * Regardless of ordering,
 * every A must stay n apart.
 *
 * Therefore A creates unavoidable gaps.
 *
 * Every remaining task competes only to fill these gaps.
 *
 * Nothing else can reduce schedule length.
 *
 * ----------------------------------------------------------------------------
 * Recognition Signals
 * ----------------------------------------------------------------------------
 *
 * Look for:
 *
 * • cooldown
 * • same task spacing
 * • minimum total schedule
 * • idle insertion
 * • repeated execution constraints
 *
 * ----------------------------------------------------------------------------
 * Use When
 * ----------------------------------------------------------------------------
 *
 * Frequency dominates ordering.
 *
 * Maximum frequency determines lower bound.
 *
 * ----------------------------------------------------------------------------
 * Do NOT Use
 * ----------------------------------------------------------------------------
 *
 * If task duration differs.
 *
 * If cooldown depends on task type.
 *
 * If weighted completion time matters.
 *
 * If dependencies exist.
 *
 * Then this greedy invariant breaks.
 *
 * ----------------------------------------------------------------------------
 * Comparison
 * ----------------------------------------------------------------------------
 *
 * Rearrange String K Distance Apart
 * ---------------------------------
 * Same scheduling invariant.
 *
 * Priority Queue Scheduling
 * -------------------------
 * Explicit simulation.
 *
 * This problem admits a stronger mathematical shortcut.
 *
 * ============================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * Imagine placing only the most frequent task first.
 *
 * Example:
 *
 * A A A A
 *
 * n = 2
 *
 * Skeleton:
 *
 * A _ _ A _ _ A _ _ A
 *
 * Those blanks are the ONLY places where other tasks can help.
 *
 * ----------------------------------------------------------------------------
 * Invariant 1
 * ----------------------------------------------------------------------------
 *
 * Maximum frequency determines the minimum possible frame.
 *
 * ----------------------------------------------------------------------------
 * Invariant 2
 * ----------------------------------------------------------------------------
 *
 * Other tasks never increase idle count.
 *
 * They only decrease existing idle slots.
 *
 * ----------------------------------------------------------------------------
 * Invariant 3
 * ----------------------------------------------------------------------------
 *
 * Idle slots are created only because
 * there are insufficient distinct tasks.
 *
 * ----------------------------------------------------------------------------
 * Invariant 4
 * ----------------------------------------------------------------------------
 *
 * Extra tasks beyond available gaps simply append naturally
 * without introducing additional idle time.
 *
 * ----------------------------------------------------------------------------
 * Variable Meaning
 * ----------------------------------------------------------------------------
 *
 * maxFreq
 *
 * Largest task frequency.
 *
 * partitions
 *
 * Number of cooling regions created.
 *
 * partitions = maxFreq - 1
 *
 * availableSlots
 *
 * partitions * n
 *
 * occupiedSlots
 *
 * Tasks used to fill available slots.
 *
 * idleSlots
 *
 * Remaining empty slots after filling.
 *
 * ----------------------------------------------------------------------------
 * Allowed State Transition
 * ----------------------------------------------------------------------------
 *
 * Highest frequency fixed.
 *
 * ↓
 *
 * Compute required cooling gaps.
 *
 * ↓
 *
 * Fill gaps using remaining tasks.
 *
 * ↓
 *
 * Remaining gaps become idle.
 *
 * ----------------------------------------------------------------------------
 * Forbidden Thinking
 * ----------------------------------------------------------------------------
 *
 * Do NOT simulate every second unless asked.
 *
 * The schedule itself is irrelevant.
 *
 * Only the unavoidable idle count matters.
 *
 * ----------------------------------------------------------------------------
 * Termination
 * ----------------------------------------------------------------------------
 *
 * After all frequencies have reduced idle slots,
 * answer is:
 *
 * totalTasks + remainingIdle
 *
 * ----------------------------------------------------------------------------
 * Why Naive Simulation Fails
 * ----------------------------------------------------------------------------
 *
 * Building schedules second-by-second
 * creates unnecessary complexity.
 *
 * The optimal ordering is never actually required.
 *
 * ============================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================================
 *
 * Mistake 1
 * ---------
 * Sort once.
 *
 * Why it seems correct:
 *
 * Largest tasks first feels greedy.
 *
 * Failure:
 *
 * Frequencies change after execution.
 *
 * Greedy order must continually adapt.
 *
 * ----------------------------------------------------
 *
 * Mistake 2
 * ---------
 * Count every cooldown literally.
 *
 * Failure:
 *
 * Other tasks may completely hide cooldown.
 *
 * ----------------------------------------------------
 *
 * Mistake 3
 * ---------
 * Simulate timeline without recognizing skeleton.
 *
 * Failure:
 *
 * O(answer)
 * reasoning instead of O(26 log 26) or O(26).
 *
 * ----------------------------------------------------
 *
 * Mistake 4
 * ---------
 * Believe every maximum-frequency task introduces idle.
 *
 * Counterexample
 *
 * AAA BBB CCC
 *
 * n = 2
 *
 * No idle exists.
 *
 * Equal frequencies perfectly fill each other's gaps.
 *
 * ============================================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================================
 *
 * Mechanical Reconstruction
 * -------------------------
 *
 * Step 1
 *
 * Count frequency of all 26 letters.
 *
 * Step 2
 *
 * Sort frequency array.
 *
 * Step 3
 *
 * max = highest frequency - 1
 *
 * Step 4
 *
 * idle = max * n
 *
 * Step 5
 *
 * Traverse remaining frequencies.
 *
 * idle -= min(max, frequency)
 *
 * Step 6
 *
 * idle = max(0, idle)
 *
 * Step 7
 *
 * return tasks.length + idle
 *
 * ============================================================================
 * ULTRA-COMPACT PSEUDOCODE
 * ============================================================================
 *
 * count frequencies
 *
 * sort
 *
 * compute idle slots
 *
 * reduce idle by remaining frequencies
 *
 * clamp idle to zero
 *
 * return tasks + idle
 *
 * ============================================================================
 * 6. SOLUTION CLASSES
 * ============================================================================
 */
public class TaskScheduler {

    /**
     * ========================================================================
     * Brute Force
     * ========================================================================
     *
     * Idea
     * ----
     * Try constructing the schedule second-by-second while searching all
     * executable tasks.
     *
     * Invariant
     * ---------
     * Every scheduled task respects cooldown.
     *
     * Limitation
     * ----------
     * Large simulation state.
     *
     * Complexity
     * ----------
     * Time:
     * O(answer × uniqueTasks)
     *
     * Interview Usefulness
     * --------------------
     * Good intuition.
     * Rarely coded.
     */

    static class BruteForce {

        public int leastInterval(char[] tasks, int n) {

            if (tasks.length == 0) {
                return 0;
            }

            int[] freq = new int[26];
            int[] nextAvailable = new int[26];

            for (char c : tasks) {
                freq[c - 'A']++;
            }

            int remaining = tasks.length;
            int time = 0;

            while (remaining > 0) {

                int candidate = -1;
                int bestFrequency = -1;

                for (int i = 0; i < 26; i++) {

                    if (freq[i] == 0) {
                        continue;
                    }

                    if (nextAvailable[i] > time) {
                        continue;
                    }

                    if (freq[i] > bestFrequency) {
                        bestFrequency = freq[i];
                        candidate = i;
                    }
                }

                if (candidate != -1) {

                    freq[candidate]--;

                    remaining--;

                    nextAvailable[candidate] = time + n + 1;
                }

                time++;
            }

            return time;
        }
    }

    /**
     * ========================================================================
     * Improved
     * ========================================================================
     *
     * Idea
     * ----
     * Always execute the highest-frequency remaining task.
     *
     * Use a max heap.
     *
     * Execute tasks in windows of length (n + 1).
     *
     * Any unfinished task returns after the current window.
     *
     * Invariant
     * ---------
     * Every window greedily consumes the largest remaining frequencies.
     *
     * Improvement
     * -----------
     * Avoids second-by-second searching.
     *
     * Complexity
     * ----------
     * Time:
     * O(T log 26)
     *
     * Space:
     * O(26)
     *
     * Interview Usefulness
     * --------------------
     * Extremely common.
     * Natural transition toward optimal reasoning.
     */

    static class Improved {

        public int leastInterval(char[] tasks, int n) {

            if (n == 0) {
                return tasks.length;
            }

            int[] frequency = new int[26];

            for (char task : tasks) {
                frequency[task - 'A']++;
            }

            PriorityQueue<Integer> maxHeap =
                    new PriorityQueue<>(Collections.reverseOrder());

            for (int count : frequency) {

                if (count > 0) {
                    maxHeap.offer(count);
                }
            }

            int totalTime = 0;
            int cycle = n + 1;

            List<Integer> pending = new ArrayList<>();

            while (!maxHeap.isEmpty()) {

                pending.clear();

                int workDone = 0;

                for (int i = 0; i < cycle; i++) {

                    if (maxHeap.isEmpty()) {
                        break;
                    }

                    // Invariant:
                    // Execute the most constrained task first.
                    int remaining = maxHeap.poll();

                    remaining--;

                    workDone++;

                    if (remaining > 0) {
                        pending.add(remaining);
                    }
                }

                for (int remaining : pending) {
                    maxHeap.offer(remaining);
                }

                // If work remains, this cycle occupies exactly (n + 1) slots.
                // Otherwise only actual work contributes.
                totalTime += maxHeap.isEmpty() ? workDone : cycle;
            }

            return totalTime;
        }
    }

    /**
     * ========================================================================
     * Optimal (Interview Preferred)
     * ========================================================================
     *
     * Pattern
     * -------
     * Greedy Frequency Mathematics
     *
     * ------------------------------------------------------------------------
     * Idea
     * ------------------------------------------------------------------------
     *
     * Instead of constructing the schedule,
     * compute only unavoidable idle slots.
     *
     * Highest-frequency task creates the framework.
     *
     * Every remaining task simply fills those gaps.
     *
     * ------------------------------------------------------------------------
     * Correctness
     * ------------------------------------------------------------------------
     *
     * Let
     *
     * maxFreq = maximum frequency.
     *
     * There are
     *
     * maxFreq - 1
     *
     * cooling partitions.
     *
     * Each partition initially contains n idle positions.
     *
     * Every remaining task fills at most one position in every partition.
     *
     * Therefore
     *
     * occupied = Σ min(partitions, frequency)
     *
     * Remaining positions become unavoidable idle slots.
     *
     * ------------------------------------------------------------------------
     * Complexity
     * ------------------------------------------------------------------------
     *
     * Time
     *
     * O(26 log 26)
     *
     * which is effectively O(1).
     *
     * Space
     *
     * O(26)
     *
     * ------------------------------------------------------------------------
     * Interview Usefulness
     * ------------------------------------------------------------------------
     *
     * This is the expected optimal solution.
     *
     * Easy to derive from invariants.
     *
     * Very short implementation.
     */

    static class Optimal {

        public int leastInterval(char[] tasks, int n) {

            // Invariant:
            // Without cooldown, schedule length equals task count.
            if (n == 0) {
                return tasks.length;
            }

            int[] frequency = new int[26];

            for (char task : tasks) {
                frequency[task - 'A']++;
            }

            Arrays.sort(frequency);

            // Highest frequency defines the scheduling skeleton.
            int partitions = frequency[25] - 1;

            // Initial idle positions before gap filling.
            int idleSlots = partitions * n;

            for (int i = 24; i >= 0; i--) {

                // Every task can fill at most one slot per partition.
                idleSlots -= Math.min(partitions, frequency[i]);
            }

            // Negative idle means every gap is already filled.
            idleSlots = Math.max(0, idleSlots);

            // Remaining idle slots are unavoidable.
            return tasks.length + idleSlots;
        }
    }

/**
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * How to Explain
 * --------------
 *
 * "The highest-frequency task is the bottleneck.
 *
 * I first imagine placing only that task.
 *
 * This immediately creates fixed cooling partitions.
 *
 * Every remaining task competes only to fill those partitions.
 *
 * If partitions become completely filled,
 * there is no idle.
 *
 * Otherwise leftover positions become mandatory idle slots.
 *
 * Therefore I never need to construct the schedule."
 *
 * -------------------------------------------------------------------------
 * Invariant
 * -------------------------------------------------------------------------
 *
 * Maximum frequency uniquely determines the schedule framework.
 *
 * -------------------------------------------------------------------------
 * Discard Rule
 * -------------------------------------------------------------------------
 *
 * There is nothing to optimize outside those cooling partitions.
 *
 * -------------------------------------------------------------------------
 * Correctness
 * -------------------------------------------------------------------------
 *
 * Every remaining task decreases idle by at most one per partition.
 *
 * -------------------------------------------------------------------------
 * Termination
 * -------------------------------------------------------------------------
 *
 * After all frequencies are processed,
 * every possible gap has either been filled
 * or remains idle.
 *
 * -------------------------------------------------------------------------
 * In-place?
 * -------------------------------------------------------------------------
 *
 * Yes.
 *
 * Only constant-sized counting array is required.
 *
 * -------------------------------------------------------------------------
 * Streaming?
 * -------------------------------------------------------------------------
 *
 * No.
 *
 * Global frequencies are required before computing the answer.
 *
 * -------------------------------------------------------------------------
 * When NOT to Use
 * -------------------------------------------------------------------------
 *
 * Variable execution times.
 *
 * Weighted tasks.
 *
 * Different cooldowns.
 *
 * Dependency graphs.
 *
 * Those require different scheduling strategies.
 *
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 * -------
 * Cooldown between identical tasks.
 *
 * Pattern
 * -------
 * Greedy Frequency Mathematics.
 *
 * Invariant
 * ---------
 * Highest frequency builds the skeleton.
 *
 * Search Target
 * -------------
 * Remaining unavoidable idle slots.
 *
 * Discard Rule
 * ------------
 * Fill every partition using other tasks.
 *
 * Common Trap
 * -----------
 * Simulating every second.
 *
 * Edge Cases
 * ----------
 * n == 0
 *
 * Only one unique task.
 *
 * Equal maximum frequencies.
 *
 * Enough distinct tasks to eliminate idle.
 *
 * One-liner
 * ---------
 * Count → Sort → Build partitions → Fill gaps → Clamp idle.
 *
 * Re-derivation Cue
 * -----------------
 *
 * Draw only the most frequent task first.
 *
 * Everything else simply fills blanks.
 *
 * =========================================================================
 * 🔄 VARIATIONS & TWEAKS
 * =========================================================================
 *
 * Variation 1
 * -----------
 * Produce actual schedule.
 *
 * Use max heap plus cooldown queue.
 *
 * Same invariant.
 *
 * -------------------------------------------------------------------------
 *
 * Variation 2
 * -----------
 * Rearrange String K Distance Apart.
 *
 * Identical scheduling principle.
 *
 * Need explicit ordering instead of only length.
 *
 * -------------------------------------------------------------------------
 *
 * Variation 3
 * -----------
 * Variable cooldown per task.
 *
 * Pattern breaks.
 *
 * Mathematical shortcut no longer holds.
 *
 * Cooldown state becomes task-dependent.
 *
 * Heap simulation is required.
 *

 * -------------------------------------------------------------------------
 *
 * Variation 4
 * -----------
 * Different execution durations.
 *
 * Pattern breaks because partitions are no longer uniform.
 *
 * -------------------------------------------------------------------------
 *
 * Variation 5
 * -----------
 * Multiple processors.
 *
 * Pattern partially survives.
 *
 * Resource allocation becomes an additional scheduling constraint.
 *
 * =========================================================================
 * 🧠 MASTERY CHECKLIST
 * =========================================================================
 *
 * □ I know the Pattern.
 *
 * □ I know why maximum frequency dominates.
 *
 * □ I know why partitions equal (maxFreq - 1).
 *
 * □ I know why every remaining task fills at most one slot
 *   in each partition.
 *
 * □ I know why idle never becomes negative.
 *
 * □ I know when heap simulation is preferable.
 *
 * □ I can explain correctness without code.
 *
 * □ I can derive the implementation from the invariant.
 *
 * □ I know the limitations of the mathematical shortcut.
 *
 * =========================================================================
 * ⚫ PATTERN MAPPING
 * =========================================================================
 *
 * Similar Problems
 * ----------------
 *
 * Rearrange String k Distance Apart
 *
 * Reorganize String
 *
 * Distant Barcodes
 *
 * Process Tasks Using Servers
 *
 * Single-Threaded CPU
 *
 * Meeting Rooms
 *
 * Interval Scheduling
 *
 * =========================================================================
 * 🔵 DEBUGGING GUIDE
 * =========================================================================
 *
 * Symptom
 * -------
 * Answer too large.
 *
 * Check
 * -----
 * Did you forget:
 *
 * idleSlots = Math.max(0, idleSlots)
 *
 * -------------------------------------------------------------------------
 *
 * Symptom
 * -------
 * Wrong answer when n == 0.
 *
 * Check
 * -----
 * Immediate return.
 *
 * -------------------------------------------------------------------------
 *
 * Symptom
 * -------
 * Off by one.
 *
 * Check
 * -----
 * partitions = maxFrequency - 1
 *
 * NOT
 *
 * maxFrequency
 *
 * -------------------------------------------------------------------------
 *
 * Symptom
 * -------
 * Wrong answer when multiple tasks share maximum frequency.
 *
 * Check
 * -----
 * Every remaining frequency contributes
 *
 * min(partitions, frequency)
 *
 * rather than the entire frequency.
 *
 * =========================================================================
 * ⚫ COMPLEXITY SUMMARY
 * =========================================================================
 *
 * Brute Force
 * -----------
 * Time
 * O(answer × 26)
 *
 * Space
 * O(26)
 *
 * -------------------------------------------------------------------------
 *
 * Heap Simulation
 * ---------------
 * Time
 * O(T log 26)
 *
 * Space
 * O(26)
 *
 * -------------------------------------------------------------------------
 *
 * Mathematical Greedy
 * -------------------
 * Time
 * O(26 log 26)
 *
 * Space
 * O(26)
 *
 * =========================================================================
 * ⚫ IMPLEMENTATION RECONSTRUCTION
 * =========================================================================
 *
 * If you forget the code during an interview:
 *
 * 1.
 * Count frequencies.
 *
 * 2.
 * Sort them.
 *
 * 3.
 * Highest frequency creates the skeleton.
 *
 * 4.
 * Compute
 *
 * partitions = maxFreq - 1
 *
 * 5.
 * Compute
 *
 * idle = partitions × n
 *
 * 6.
 * Reduce idle using
 *
 * min(partitions, frequency)
 *
 * for every remaining task.
 *
 * 7.
 * Clamp idle to zero.
 *
 * 8.
 * Return
 *
 * tasks.length + idle
 *
 * =========================================================================
 * ⚫ FREQUENT INTERVIEW QUESTIONS
 * =========================================================================
 *
 * Q.
 * Why subtract one from the maximum frequency?
 *
 * A.
 * The final occurrence creates no trailing cooling partition.
 *
 * -------------------------------------------------------------------------
 *
 * Q.
 * Why does every task contribute at most
 * min(partitions, frequency)?
 *
 * A.
 * A task can occupy only one slot inside each partition.
 *
 * -------------------------------------------------------------------------
 *
 * Q.
 * Why clamp idle to zero?
 *
 * A.
 * Extra tasks completely eliminate idle;
 * they never create negative time.
 *
 * -------------------------------------------------------------------------
 *
 * Q.
 * Why is sorting only 26 values effectively constant?
 *
 * A.
 * Alphabet size is fixed.
 *
 * =========================================================================
 * ⚫ COMMON EDGE CASES
 * =========================================================================
 *
 * tasks = [A]
 * n = 100
 *
 * Answer = 1
 *
 * -------------------------------------------------------------------------
 *
 * tasks = [A,A,A]
 * n = 0
 *
 * Answer = 3
 *
 * -------------------------------------------------------------------------
 *
 * tasks = [A,A,A,B,B,B]
 * n = 2
 *
 * Answer = 8
 *
 * -------------------------------------------------------------------------
 *
 * tasks = [A,A,A,B,B,B,C,C,C]
 * n = 2
 *
 * No idle.
 *
 * -------------------------------------------------------------------------
 *
 * tasks = [A,A,A,A,B,C,D]
 * n = 3
 *
 * Idle is unavoidable.
 *
 * =========================================================================
 * ⚫ FORMULA DERIVATION
 * =========================================================================
 *
 * Let
 *
 * F = maximum frequency.
 *
 * Skeleton:
 *
 * A _ _ A _ _ A ...
 *
 * Number of partitions:
 *
 * F - 1
 *
 * Idle capacity:
 *
 * (F - 1) × n
 *
 * Fill capacity using every remaining task.
 *
 * Remaining capacity becomes idle.
 *
 * Total schedule:
 *
 * totalTasks + idle
 *

 /**
 * =========================================================================
 * 🧪 MAIN + SELF-VERIFYING TESTS
 * =========================================================================
 */

public static void main(String[] args) {

    Optimal optimal = new Optimal();

    // Representative example from the problem statement.
    assert optimal.leastInterval(
            new char[]{'A','A','A','B','B','B'}, 2) == 8;

    // No cooldown means no idle.
    assert optimal.leastInterval(
            new char[]{'A','A','A','B','B','B'}, 0) == 6;

    // Large dominant task frequency forces idle slots.
    assert optimal.leastInterval(
            new char[]{
                    'A','A','A','A','A','A',
                    'B','C','D','E','F','G'
            }, 2) == 16;

    // Single task.
    assert optimal.leastInterval(
            new char[]{'A'}, 100) == 1;

    // Equal frequencies eliminate idle.
    assert optimal.leastInterval(
            new char[]{
                    'A','A','A',
                    'B','B','B',
                    'C','C','C'
            }, 2) == 9;

    // Plenty of filler tasks.
    assert optimal.leastInterval(
            new char[]{
                    'A','A',
                    'B','B',
                    'C','C',
                    'D','D'
            }, 2) == 8;

    // Dominant task with unavoidable idle.
    assert optimal.leastInterval(
            new char[]{
                    'A','A','A','A',
                    'B','C','D'
            }, 3) == 13;

    // Every task unique.
    assert optimal.leastInterval(
            new char[]{
                    'A','B','C','D','E'
            }, 10) == 5;

    // One task repeated.
    assert optimal.leastInterval(
            new char[]{
                    'A','A','A'
            }, 2) == 7;

    // Cooldown zero with repeated task.
    assert optimal.leastInterval(
            new char[]{
                    'A','A','A'
            }, 0) == 3;

    // Heap solution should match optimal.
    Improved improved = new Improved();

    assert improved.leastInterval(
            new char[]{'A','A','A','B','B','B'}, 2) == 8;

    assert improved.leastInterval(
            new char[]{
                    'A','A','A','A','A','A',
                    'B','C','D','E','F','G'
            }, 2) == 16;

    assert improved.leastInterval(
            new char[]{
                    'A','B','C','D'
            }, 5) == 4;

    // Brute-force verification on small inputs.
    BruteForce brute = new BruteForce();

    assert brute.leastInterval(
            new char[]{'A','A','B'}, 2)
            == optimal.leastInterval(
            new char[]{'A','A','B'}, 2);

    assert brute.leastInterval(
            new char[]{'A','B','C'}, 2)
            == optimal.leastInterval(
            new char[]{'A','B','C'}, 2);

    assert brute.leastInterval(
            new char[]{'A','A','B','B'}, 1)
            == optimal.leastInterval(
            new char[]{'A','A','B','B'}, 1);

    System.out.println("All Task Scheduler tests passed.");
}

}

/*
===============================================================================
Source Notes
===============================================================================

This chapter incorporates and restructures the problem statement, examples,
and multiple solution approaches provided in the attached notes, including the
priority-queue simulation, greedy mathematical derivation, and implementation
insights. :contentReference[oaicite:0]{index=0}

===============================================================================

I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/