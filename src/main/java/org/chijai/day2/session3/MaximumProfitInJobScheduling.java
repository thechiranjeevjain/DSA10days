package org.chijai.day2.session3;

import java.util.*;

/**
 * MaximumProfitInJobScheduling
 *
 * ============================================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================================
 *
 * Title:
 * Maximum Profit in Job Scheduling
 *
 * Difficulty:
 * Hard
 *
 * Tags:
 * Dynamic Programming
 * Binary Search
 * Weighted Interval Scheduling
 * Sorting
 * TreeMap
 * Priority Queue
 *
 * Problem:
 *
 * We are given three arrays:
 *
 * startTime[i]
 * endTime[i]
 * profit[i]
 *
 * Each index represents one job.
 *
 * A chosen job occupies the interval:
 *
 * [startTime[i], endTime[i])
 *
 * Jobs are compatible if:
 *
 * previous.endTime <= next.startTime
 *
 * (Starting exactly when another ends is allowed.)
 *
 * Return the maximum obtainable profit.
 *
 * Constraints:
 *
 * 1 <= n <= 5 * 10^4
 * 1 <= start < end <= 10^9
 * 1 <= profit <= 10^4
 *
 * Representative Example:
 *
 * start = [1,2,3,3]
 * end   = [3,4,5,6]
 * profit= [50,10,40,70]
 *
 * Answer:
 * 120
 *
 * Choose:
 *
 * (1,3,50)
 * (3,6,70)
 *
 * Official:
 * https://leetcode.com/problems/maximum-profit-in-job-scheduling/
 *
 * ============================================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern:
 * Weighted Interval Scheduling
 *
 * Archetype:
 * Dynamic Programming over sorted states with Binary Search transition.
 *
 * Core Invariant:
 *
 * dp[i]
 * =
 * maximum obtainable profit considering jobs starting from index i onward.
 *
 * At every state exactly two legal transitions exist:
 *
 * 1.
 * Skip current job.
 *
 * 2.
 * Take current job.
 *
 * Therefore
 *
 * dp[i] =
 * max(
 *      skip,
 *      take
 * )
 *
 * Why it works:
 *
 * Sorting by start time creates a monotonic search space.
 *
 * Once a job is chosen,
 * every overlapping job is permanently discarded.
 *
 * Binary search immediately finds the first compatible state.
 *
 * Recognition Signals:
 *
 * • intervals
 * • weighted intervals
 * • choose / skip
 * • maximize reward
 * • overlapping forbidden
 * • next compatible interval
 * • binary search transition
 *
 * When to use:
 *
 * • weighted scheduling
 * • weighted meetings
 * • weighted projects
 * • weighted bookings
 * • interval DP
 *
 * When NOT to use:
 *
 * • intervals can partially overlap
 * • selecting multiple simultaneous jobs
 * • graph dependency instead of timeline dependency
 *
 * Comparison:
 *
 * Activity Selection:
 * Greedy because every interval has equal value.
 *
 * Here:
 * Profits differ.
 *
 * Greedy becomes incorrect.
 *
 * DP becomes necessary.
 *
 * ============================================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine standing at job i.
 *
 * Everything before i has already been decided.
 *
 * Your future consists only of:
 *
 * jobs[i...]
 *
 * Your only responsibility:
 *
 * Decide whether this job belongs to the optimal schedule.
 *
 * If skipped:
 *
 * Move to i+1.
 *
 * If taken:
 *
 * Jump directly to the first compatible future job.
 *
 * Nothing in between can ever become legal again.
 *
 * -------------------------
 * State Definition
 * -------------------------
 *
 * State:
 *
 * i
 *
 * Meaning:
 *
 * Best answer obtainable from jobs[i...].
 *
 * -------------------------
 * Search Space
 * -------------------------
 *
 * Sorted by increasing start time.
 *
 * Binary search relies on this ordering.
 *
 * -------------------------
 * Invariant #1
 * -------------------------
 *
 * dp[i]
 * always stores the optimal answer from suffix i onward.
 *
 * Never partial.
 *
 * Never approximate.
 *
 * -------------------------
 * Invariant #2
 * -------------------------
 *
 * Binary search always returns the first job satisfying
 *
 * start >= currentEnd
 *
 * Every earlier job overlaps.
 *
 * Every later compatible job is reachable through DP.
 *
 * -------------------------
 * Invariant #3
 * -------------------------
 *
 * Taking current job immediately removes all overlapping jobs.
 *
 * We never revisit discarded states.
 *
 * -------------------------
 * Invariant #4
 * -------------------------
 *
 * Transition only moves forward.
 *
 * No cycles.
 *
 * Therefore memoization is valid.
 *
 * -------------------------
 * Variable Meanings
 * -------------------------
 *
 * jobs[]
 *
 * Sorted jobs.
 *
 * start[]
 *
 * Sorted starting times.
 *
 * i
 *
 * Current DP state.
 *
 * next
 *
 * First compatible job.
 *
 * take
 *
 * Profit if current chosen.
 *
 * skip
 *
 * Profit if current ignored.
 *
 * -------------------------
 * Allowed Moves
 * -------------------------
 *
 * Skip:
 *
 * i -> i+1
 *
 * Take:
 *
 * i -> nextCompatible
 *
 * -------------------------
 * Forbidden Moves
 * -------------------------
 *
 * Returning to previous jobs.
 *
 * Choosing overlapping intervals.
 *
 * Binary searching unsorted data.
 *
 * -------------------------
 * Termination
 * -------------------------
 *
 * i == n
 *
 * No jobs remain.
 *
 * Profit = 0.
 *
 * -------------------------
 * Correctness Intuition
 * -------------------------
 *
 * Every feasible schedule beginning at state i must either:
 *
 * include job i
 *
 * or
 *
 * exclude job i.
 *
 * There is no third possibility.
 *
 * DP exhausts exactly those two choices.
 *
 * Binary search guarantees the take transition lands on the earliest legal
 * future state.
 *
 * Therefore every feasible schedule is represented exactly once.
 *
 * -------------------------
 * Why Naive Solutions Fail
 * -------------------------
 *
 * Earliest ending interval:
 *
 * Wrong because profits differ.
 *
 * Highest profit interval:
 *
 * Wrong because multiple medium-profit intervals may exceed one large interval.
 *
 * Earliest starting interval:
 *
 * Ignores accumulated future value.
 *
 * Local optimization cannot reason about future compatible chains.
 *
 * ============================================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================================
 *
 * Mistake 1
 * ---------
 * Greedy by highest profit.
 *
 * Looks attractive because immediate reward is largest.
 *
 * Violated Invariant:
 * Future profit is ignored.
 *
 * Counterexample:
 *
 * (1,10,100)
 * (10,20,90)
 * (1,5,70)
 * (5,10,70)
 * (10,20,90)
 *
 * Greedy:
 * 190
 *
 * Optimal:
 * 230
 *
 * ------------------------------------------------
 * Mistake 2
 * ------------------------------------------------
 * Linear search for next compatible interval.
 *
 * Correct.
 *
 * Too slow.
 *
 * O(n²)
 *
 * ------------------------------------------------
 * Mistake 3
 * ------------------------------------------------
 * Binary searching before sorting.
 *
 * Binary search requires monotonic ordering.
 *
 * Violated Invariant:
 * Search space is no longer ordered.
 *
 * ------------------------------------------------
 * Mistake 4
 * ------------------------------------------------
 * Sorting by profit.
 *
 * DP transitions depend on timeline,
 * not reward ordering.
 *
 * ------------------------------------------------
 * Mistake 5
 * ------------------------------------------------
 * Forgetting equality.
 *
 * Compatibility is
 *
 * next.start >= current.end
 *
 * NOT
 *
 * >
 *
 * Missing equality loses valid schedules.
 *
 * ============================================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================================
 *
 * Mechanical typing order:
 *
 * 1.
 * Create Job class.
 *
 * 2.
 * Build Job array.
 *
 * 3.
 * Sort by start time.
 *
 * 4.
 * Extract sorted starts.
 *
 * 5.
 * Allocate memo.
 *
 * 6.
 * DFS(index)
 *
 * 7.
 * Base case.
 *
 * 8.
 * Binary search next compatible.
 *
 * 9.
 * Compute take.
 *
 * 10.
 * Compute skip.
 *
 * 11.
 * Store max.
 *
 * 12.
 * Return memo.
 *
 * Function Skeleton
 * -----------------
 *
 * jobScheduling(...)
 *
 * build jobs
 *
 * sort
 *
 * build starts
 *
 * memo
 *
 * dfs(0)
 *
 * Variable Initialization
 * -----------------------
 *
 * Job[] jobs
 *
 * int[] starts
 *
 * Integer[] memo
 *
 * Loop Skeleton
 * -------------
 *
 * binary search
 *
 * left
 *
 * right
 *
 * mid
 *
 * Branch Logic
 * ------------
 *
 * if compatible
 *
 * move left
 *
 * else
 *
 * move right
 *
 * Transition
 * ----------
 *
 * skip = dfs(i+1)
 *
 * take = profit + dfs(next)
 *
 * answer = max(skip,take)
 *
 * Return
 * ------
 *
 * memo[i]
 *
 * ============================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================================
 *
 * sort
 *
 * dfs(i):
 *
 * if end return 0
 *
 * next = binarySearch(end)
 *
 * take = profit + dfs(next)
 *
 * skip = dfs(i+1)
 *
 * return max(take,skip)
 *
 * ============================================================================
 * 6. SOLUTION CLASSES
 * ============================================================================
 */
public class MaximumProfitInJobScheduling {

    /**
     * Shared immutable job representation.
     */
    static final class Job {
        final int start;
        final int end;
        final int profit;

        Job(int start, int end, int profit) {
            this.start = start;
            this.end = end;
            this.profit = profit;
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Brute Force
     * ------------------------------------------------------------------------
     *
     * Idea:
     * Explore every legal subset recursively.
     *
     * Invariant:
     * Every recursive path represents one feasible schedule.
     *
     * Limitation:
     * Exponential.
     *
     * Complexity:
     *
     * Time:
     * O(2^n)
     *
     * Space:
     * O(n)
     *
     * Interview usefulness:
     *
     * Establishes the recurrence before optimization.
     */
    static final class BruteForce {

        public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {

            Job[] jobs = buildAndSort(startTime, endTime, profit);

            return dfs(jobs, 0, 0);
        }

        private int dfs(Job[] jobs, int index, int currentTime) {

            if (index == jobs.length) {
                return 0;
            }

            int skip = dfs(jobs, index + 1, currentTime);

            int take = 0;

            if (jobs[index].start >= currentTime) {
                take = jobs[index].profit +
                        dfs(jobs, index + 1, jobs[index].end);
            }

            return Math.max(skip, take);
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Improved
     * ------------------------------------------------------------------------
     *
     * Idea:
     * DP + Binary Search.
     *
     * Invariant:
     * dp[i] stores the optimal answer beginning from suffix i.
     *
     * Improvement:
     * Removes repeated exploration.
     *
     * Complexity:
     *
     * Time:
     * O(n log n)
     *
     * Space:
     * O(n)
     *
     * Interview usefulness:
     *
     * Canonical weighted interval scheduling solution.
     */
    static final class Improved {

        private Job[] jobs;
        private int[] starts;
        private Integer[] memo;

        public int jobScheduling(
                int[] startTime,
                int[] endTime,
                int[] profit
        ) {

            jobs = buildAndSort(startTime, endTime, profit);

            starts = new int[jobs.length];

            for (int i = 0; i < jobs.length; i++) {
                starts[i] = jobs[i].start;
            }

            memo = new Integer[jobs.length];

            return solve(0);
        }

        private int solve(int index) {

            // 🟢 Invariant:
            // Optimal answer for every suffix is computed once.

            if (index == jobs.length) {
                return 0;
            }

            if (memo[index] != null) {
                return memo[index];
            }

            int next = firstCompatible(index);

            int take =
                    jobs[index].profit +
                            solve(next);

            int skip =
                    solve(index + 1);

            memo[index] = Math.max(take, skip);

            return memo[index];
        }

        private int firstCompatible(int index) {

            int target = jobs[index].end;

            int left = index + 1;
            int right = jobs.length;

            // 🟢 Invariant:
            // Search space is sorted by start time.
            // Answer remains inside [left, right).

            while (left < right) {

                int mid = left + (right - left) / 2;

                if (starts[mid] >= target) {

                    // First compatible job may still exist on the left.
                    right = mid;

                } else {

                    // Every job before and including mid overlaps.
                    left = mid + 1;
                }
            }

            return left;
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Optimal (Interview Preferred)
     * ------------------------------------------------------------------------
     *
     * Idea
     * ----
     *
     * Bottom-up Dynamic Programming after sorting by start time.
     *
     * Instead of recursive memoization,
     * compute answers from the end of the array toward the beginning.
     *
     * Invariant
     * ---------
     *
     * dp[i]
     *
     * stores the maximum obtainable profit beginning from job i.
     *
     * Since every transition only moves to a larger index,
     * every required future value has already been computed.
     *
     * Correctness
     * -----------
     *
     * Every optimal schedule beginning at i must:
     *
     * • skip job i
     * OR
     * • take job i
     *
     * Binary search finds the earliest compatible future state.
     *
     * Therefore
     *
     * dp[i]
     *
     * is optimal.
     *
     * Complexity
     * ----------
     *
     * Time:
     * O(n log n)
     *
     * Space:
     * O(n)
     *
     * Interview usefulness
     * --------------------
     *
     * This is the implementation most interviewers expect because:
     *
     * • no recursion depth concern
     * • deterministic transition
     * • binary-search DP
     * • easy to debug
     */
    static final class Optimal {

        public int jobScheduling(
                int[] startTime,
                int[] endTime,
                int[] profit
        ) {

            Job[] jobs = buildAndSort(startTime, endTime, profit);

            int n = jobs.length;

            int[] starts = new int[n];

            for (int i = 0; i < n; i++) {
                starts[i] = jobs[i].start;
            }

            int[] dp = new int[n + 1];

            // 🟢 Invariant:
            // dp[n] = 0 because no jobs remain.

            for (int i = n - 1; i >= 0; i--) {

                int next =
                        firstCompatible(starts, jobs[i].end, i + 1);

                // Taking current job jumps directly
                // to the first legal future state.
                int take =
                        jobs[i].profit +
                                dp[next];

                // Skipping preserves every future possibility.
                int skip =
                        dp[i + 1];

                // Invariant:
                // dp[i] is optimal for suffix i.
                dp[i] =
                        Math.max(take, skip);
            }

            return dp[0];
        }

        private int firstCompatible(
                int[] starts,
                int target,
                int left
        ) {

            int right = starts.length;

            // 🟢 Invariant:
            // Answer always lies inside [left, right).

            while (left < right) {

                int mid =
                        left + (right - left) / 2;

                if (starts[mid] >= target) {

                    // Compatible found.
                    // Continue shrinking left boundary.
                    right = mid;

                } else {

                    // Mid overlaps.
                    left = mid + 1;
                }
            }

            return left;
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Alternative Optimal
     * TreeMap DP
     * ------------------------------------------------------------------------
     *
     * Pattern
     * -------
     *
     * Weighted Interval Scheduling
     * using ordered map instead of suffix DP.
     *
     * State
     * -----
     *
     * map[endTime]
     *
     * =
     *
     * maximum achievable profit up to this ending time.
     *
     * Invariant
     * ---------
     *
     * Map values never decrease.
     *
     * Every inserted value represents the globally best
     * achievable profit seen so far.
     *
     * Transition
     * ----------
     *
     * floorKey(start)
     *
     * finds the latest compatible schedule.
     *
     * Complexity
     * ----------
     *
     * O(n log n)
     */
    static final class TreeMapDP {

        public int jobScheduling(
                int[] startTime,
                int[] endTime,
                int[] profit
        ) {

            Job[] jobs = buildByEndTime(
                    startTime,
                    endTime,
                    profit
            );

            TreeMap<Integer, Integer> dp =
                    new TreeMap<>();

            int best = 0;

            for (Job job : jobs) {

                Integer compatibleEnd =
                        dp.floorKey(job.start);

                int previousProfit =
                        compatibleEnd == null
                                ? 0
                                : dp.get(compatibleEnd);

                int candidate =
                        previousProfit + job.profit;

                if (candidate > best) {

                    best = candidate;

                    // 🟢 Invariant:
                    // Every stored value is globally optimal
                    // for all schedules ending at or before this key.
                    dp.put(job.end, best);
                }
            }

            return best;
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Alternative Optimal
     * Priority Queue Sweep
     * ------------------------------------------------------------------------
     *
     * Pattern
     * -------
     *
     * Timeline sweep.
     *
     * Jobs are processed by start time.
     *
     * Heap stores unfinished schedules ordered by end time.
     *
     * Invariant
     * ---------
     *
     * maxFinishedProfit
     *
     * always equals the maximum obtainable profit among
     * schedules whose ending time is already compatible
     * with the current job.
     *
     * Complexity
     * ----------
     *
     * O(n log n)
     */
    static final class PriorityQueueDP {

        static final class State {

            final int end;
            final int accumulatedProfit;

            State(int end, int accumulatedProfit) {
                this.end = end;
                this.accumulatedProfit = accumulatedProfit;
            }
        }

        public int jobScheduling(
                int[] startTime,
                int[] endTime,
                int[] profit
        ) {

            Job[] jobs =
                    buildAndSort(startTime, endTime, profit);

            PriorityQueue<State> heap =
                    new PriorityQueue<>(
                            Comparator.comparingInt(a -> a.end)
                    );

            int maxFinishedProfit = 0;

            for (Job job : jobs) {

                while (!heap.isEmpty()
                        && heap.peek().end <= job.start) {

                    // Every popped schedule is now compatible
                    // with every future job.
                    maxFinishedProfit =
                            Math.max(
                                    maxFinishedProfit,
                                    heap.poll().accumulatedProfit
                            );
                }

                heap.offer(
                        new State(
                                job.end,
                                maxFinishedProfit + job.profit
                        )
                );
            }

            while (!heap.isEmpty()) {

                maxFinishedProfit =
                        Math.max(
                                maxFinishedProfit,
                                heap.poll().accumulatedProfit
                        );
            }

            return maxFinishedProfit;
        }
    }

/**
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * Explain the invariant:
 *
 * "After sorting by start time,
 * every state represents the optimal answer beginning
 * from one suffix of jobs.
 *
 * The DP never needs to revisit previous jobs because
 * every transition moves strictly forward."
 *
 * Explain the discard rule:
 *
 * "When a job is chosen,
 * every overlapping interval becomes permanently illegal.
 * Binary search jumps directly to the first legal interval."
 *
 * Explain correctness:
 *
 * "Every feasible schedule either contains
 * the current job or excludes it.
 *
 * DP evaluates exactly those two exhaustive possibilities."
 *
 * Explain termination:
 *
 * "Index eventually reaches n,
 * meaning no future jobs remain.
 *
 * Profit becomes zero."
 *
 * In-place feasibility:
 *
 * Impossible without destroying ordering or
 * sacrificing transition information.
 *
 * O(n) DP memory is appropriate.
 *
 * Streaming feasibility:
 *
 * Pure suffix DP is not streamable because
 * future intervals affect present decisions.
 *
 * TreeMap and heap formulations are better suited
 * for online chronological processing.
 *
 * When NOT to use:
 *
 * If overlapping jobs may both be selected,
 * this recurrence no longer models the problem.
 */

    /**
     * =========================================================================
     * 🎯 INTERVIEW RECALL SHEET
     * =========================================================================
     *
     * Trigger
     * -------
     * • Intervals
     * • Profit / Weight
     * • No overlap
     * • Maximize reward
     * • Next compatible interval
     *
     * Pattern
     * -------
     * Weighted Interval Scheduling
     *
     * Invariant
     * ---------
     * dp[i] = optimal profit obtainable from suffix i onward.
     *
     * Search Target
     * -------------
     * First job with
     *
     * start >= currentEnd
     *
     * Discard Rule
     * ------------
     * Choosing a job permanently removes every overlapping interval.
     *
     * Common Trap
     * -----------
     * Greedy.
     *
     * Edge Cases
     * ----------
     * • Single job
     * • All overlapping
     * • No overlapping
     * • Equal start times
     * • Equal end/start boundary
     * • Large coordinates
     *
     * One-liner
     * ---------
     * Sort -> Binary Search -> DP.
     *
     * Re-derivation Cue
     * -----------------
     * Ask:
     *
     * "If I choose this job,
     * where is the first future job I am allowed to visit?"
     *
     * =========================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =========================================================================
     *
     * Variation:
     * ----------
     * Memoization
     *
     * Change:
     * Recursive implementation.
     *
     * Invariant unchanged.
     *
     * ---------------------------------------------
     * Variation:
     * Bottom-up DP
     * ---------------------------------------------
     *
     * Change:
     * Compute suffix answers backwards.
     *
     * Same recurrence.
     *
     * ---------------------------------------------
     * Variation:
     * TreeMap DP
     * ---------------------------------------------
     *
     * State changes from index
     * to ending time.
     *
     * floorKey()
     * replaces binary search.
     *
     * ---------------------------------------------
     * Variation:
     * Heap Sweep
     * ---------------------------------------------
     *
     * State becomes:
     *
     * maximum finished schedule.
     *
     * Sweep line replaces explicit DP array.
     *
     * ---------------------------------------------
     * Variation:
     * Max Number of Non-overlapping Jobs
     * ---------------------------------------------
     *
     * Profit becomes constant.
     *
     * DP unnecessary.
     *
     * Greedy by earliest finishing works.
     *
     * ---------------------------------------------
     * Variation:
     * Weighted Meetings
     * ---------------------------------------------
     *
     * Identical recurrence.
     *
     * Only interpretation changes.
     *
     * ---------------------------------------------
     * Variation:
     * Project Selection
     * ---------------------------------------------
     *
     * Same pattern if projects occupy
     * non-overlapping timelines.
     *
     * ---------------------------------------------
     * Pattern Break
     * ---------------------------------------------
     *
     * If overlapping jobs are allowed
     * with penalties,
     * this recurrence no longer models
     * the state space.
     *
     * =========================================================================
     * 🧠 MASTERY CHECKLIST
     * =========================================================================
     *
     * □ Can I define the DP state?
     *
     * Yes.
     *
     * dp[i]
     *
     * □ Can I explain the invariant?
     *
     * Yes.
     *
     * Optimal suffix answer.
     *
     * □ Can I explain the search target?
     *
     * First compatible interval.
     *
     * □ Can I derive binary search?
     *
     * Search smallest index with
     *
     * start >= end.
     *
     * □ Can I explain discard rule?
     *
     * Choosing current permanently
     * removes every overlapping interval.
     *
     * □ Can I justify correctness?
     *
     * Exhaustive:
     *
     * take
     *
     * vs
     *
     * skip.
     *
     * □ Can I explain termination?
     *
     * Index reaches n.
     *
     * □ Can I explain why greedy fails?
     *
     * Local reward ignores future chains.
     *
     * □ Can I debug binary search?
     *
     * Verify:
     *
     * first compatible,
     * not merely any compatible.
     *
     * □ Can I recognize equivalent problems?
     *
     * Weighted interval scheduling.
     *
     * □ Can I identify pattern boundary?
     *
     * Requires
     * non-overlapping interval constraint.
     */

    private static Job[] buildAndSort(
            int[] startTime,
            int[] endTime,
            int[] profit
    ) {

        int n = startTime.length;

        Job[] jobs = new Job[n];

        for (int i = 0; i < n; i++) {

            jobs[i] =
                    new Job(
                            startTime[i],
                            endTime[i],
                            profit[i]
                    );
        }

        Arrays.sort(
                jobs,
                Comparator.comparingInt(a -> a.start)
        );

        return jobs;
    }

    private static Job[] buildByEndTime(
            int[] startTime,
            int[] endTime,
            int[] profit
    ) {

        int n = startTime.length;

        Job[] jobs = new Job[n];

        for (int i = 0; i < n; i++) {

            jobs[i] =
                    new Job(
                            startTime[i],
                            endTime[i],
                            profit[i]
                    );
        }

        Arrays.sort(
                jobs,
                Comparator.comparingInt(a -> a.end)
        );

        return jobs;
    }

    private static void verify(
            int expected,
            int actual,
            String testName
    ) {

        assert expected == actual :
                testName
                        + " Expected = "
                        + expected
                        + " Actual = "
                        + actual;
    }

    private static int[] copy(int[] nums) {

        return Arrays.copyOf(nums, nums.length);
    }


    /**
     * =========================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * =========================================================================
     */
    public static void main(String[] args) {

        Optimal optimal = new Optimal();

        // ---------------------------------------------------------------------
        // Happy Path
        // Basic representative example.
        // ---------------------------------------------------------------------
        verify(
                120,
                optimal.jobScheduling(
                        copy(new int[]{1, 2, 3, 3}),
                        copy(new int[]{3, 4, 5, 6}),
                        copy(new int[]{50, 10, 40, 70})
                ),
                "Representative Example 1"
        );

        // ---------------------------------------------------------------------
        // Happy Path
        // Multiple compatible intervals outperform one long interval.
        // ---------------------------------------------------------------------
        verify(
                150,
                optimal.jobScheduling(
                        copy(new int[]{1, 2, 3, 4, 6}),
                        copy(new int[]{3, 5, 10, 6, 9}),
                        copy(new int[]{20, 20, 100, 70, 60})
                ),
                "Representative Example 2"
        );

        // ---------------------------------------------------------------------
        // All jobs overlap.
        // Best individual profit should be selected.
        // ---------------------------------------------------------------------
        verify(
                6,
                optimal.jobScheduling(
                        copy(new int[]{1, 1, 1}),
                        copy(new int[]{2, 3, 4}),
                        copy(new int[]{5, 6, 4})
                ),
                "Representative Example 3"
        );

        // ---------------------------------------------------------------------
        // Boundary:
        // Equality is compatible.
        // end == next.start must be allowed.
        // ---------------------------------------------------------------------
        verify(
                60,
                optimal.jobScheduling(
                        copy(new int[]{1, 3}),
                        copy(new int[]{3, 5}),
                        copy(new int[]{20, 40})
                ),
                "Boundary Equality"
        );

        // ---------------------------------------------------------------------
        // Single job.
        // ---------------------------------------------------------------------
        verify(
                99,
                optimal.jobScheduling(
                        copy(new int[]{5}),
                        copy(new int[]{10}),
                        copy(new int[]{99})
                ),
                "Single Job"
        );

        // ---------------------------------------------------------------------
        // Completely non-overlapping.
        // Every job should be taken.
        // ---------------------------------------------------------------------
        verify(
                100,
                optimal.jobScheduling(
                        copy(new int[]{1, 3, 5, 7}),
                        copy(new int[]{2, 4, 6, 8}),
                        copy(new int[]{10, 20, 30, 40})
                ),
                "All Compatible"
        );

        // ---------------------------------------------------------------------
        // Greedy trap.
        // Highest immediate reward is not optimal.
        // ---------------------------------------------------------------------
        verify(
                160,
                optimal.jobScheduling(
                        copy(new int[]{1, 1, 3}),
                        copy(new int[]{3, 5, 5}),
                        copy(new int[]{60, 90, 100})
                ),
                "Greedy Trap"
        );

        // ---------------------------------------------------------------------
        // Dense overlap.
        // DP should skip low-value jobs.
        // ---------------------------------------------------------------------
        verify(
                18,
                optimal.jobScheduling(
                        copy(new int[]{1, 2, 3, 4}),
                        copy(new int[]{10, 5, 6, 7}),
                        copy(new int[]{10, 6, 6, 6})
                ),
                "Dense Overlap"
        );

        // ---------------------------------------------------------------------
        // Large time coordinates.
        // Binary search should rely only on ordering.
        // ---------------------------------------------------------------------
        verify(
                30,
                optimal.jobScheduling(
                        copy(new int[]{1, 1_000_000_000}),
                        copy(new int[]{2, 1_000_000_001}),
                        copy(new int[]{10, 20})
                ),
                "Large Coordinates"
        );

        // ---------------------------------------------------------------------
        // Compare implementations on the same input.
        // ---------------------------------------------------------------------
        int[] s = {1, 2, 3, 3};
        int[] e = {3, 4, 5, 6};
        int[] p = {50, 10, 40, 70};

        int expected = optimal.jobScheduling(
                copy(s),
                copy(e),
                copy(p)
        );

        verify(
                expected,
                new Improved().jobScheduling(
                        copy(s),
                        copy(e),
                        copy(p)
                ),
                "Memoized DP"
        );

        verify(
                expected,
                new TreeMapDP().jobScheduling(
                        copy(s),
                        copy(e),
                        copy(p)
                ),
                "TreeMap DP"
        );

        verify(
                expected,
                new PriorityQueueDP().jobScheduling(
                        copy(s),
                        copy(e),
                        copy(p)
                ),
                "Priority Queue DP"
        );

        System.out.println("All assertions passed.");
    }
}

