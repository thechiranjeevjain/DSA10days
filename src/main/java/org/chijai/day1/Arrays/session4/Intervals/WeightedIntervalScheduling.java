package org.chijai.day1.Arrays.session4.Intervals;

import java.util.Arrays;
import java.util.Comparator;

/**
 * INTERVAL PATTERN 4 — WEIGHTED INTERVAL SCHEDULING
 *
 * Horizontal mastery:
 *      Activity Selection            -> maximize compatible COUNT
 *      Weighted Job Scheduling       -> maximize compatible PROFIT / VALUE
 *      Greedy -> DP boundary         -> unequal value breaks earliest-finish greedy
 *      Binary-search predecessor     -> jump to the best compatible past
 *
 * MASTER ENGINE
 * -------------
 * Sort jobs by END so every decision can look only into an already-solved prefix.
 * For each job:
 *
 *      SKIP = best answer without this job
 *      TAKE = this profit + best answer before this job starts
 *
 *      dp[i] = max(SKIP, TAKE)
 *
 * ONE-LINE RECALL
 * ---------------
 *      COUNT  -> sort by end + greedy
 *      PROFIT -> sort by end + take/skip DP + binary-search predecessor
 *
 * CODE STYLE
 * ----------
 * Keep reconstruction familiar: arrays / Comparator / explicit while-loop binary search.
 * No Streams or Collectors in core algorithm logic.
 */
public class WeightedIntervalScheduling {

    // =========================================================================
    // DOMAIN MODEL
    // =========================================================================

    static final class Job {
        final int start;
        final int end;
        final int profit;

        Job(int start, int end, int profit) {
            this.start = start;
            this.end = end;
            this.profit = profit;
        }

        @Override
        public String toString() {
            return "[" + start + "," + end + ", profit=" + profit + "]";
        }
    }

    // =========================================================================
    // PATTERN POSITION / RECOGNITION
    // =========================================================================

    /*
     * USE THIS FILE WHEN:
     *      intervals / jobs must be mutually compatible
     *      AND each choice has unequal profit / value / reward.
     *
     * TRIGGER WORDS:
     *      maximum profit
     *      maximum value
     *      weighted jobs
     *      choose non-overlapping jobs with rewards
     *
     * NOT THIS ENGINE:
     *
     *      Maximize NUMBER of compatible intervals?
     *          -> IntervalGreedyByEnd.java
     *          -> earliest-finish greedy.
     *
     *      Minimum removals / minimum arrows?
     *          -> IntervalGreedyByEnd.java
     *
     *      Peak simultaneous occupancy / minimum resources?
     *          -> IntervalActiveOverTime.java
     *
     *      Merge / detect / insert ranges?
     *          -> IntervalSortByStart.java
     *
     * MASTER QUESTION:
     *      Are all accepted intervals effectively worth the same amount?
     *
     *      YES -> greedy may be enough for maximum COUNT.
     *      NO  -> preserve both TAKE and SKIP possibilities with DP.
     */

    // =========================================================================
    // 1) WEIGHTED JOB SCHEDULING — CANONICAL DP + PREDECESSOR BINARY SEARCH
    // Goal: Maximum total profit from mutually compatible jobs.
    // =========================================================================

    static final class MaximumProfitJobs {

        int maxProfit(Job[] jobs) {
            if (jobs == null || jobs.length == 0) {
                return 0;
            }

            Arrays.sort(jobs, Comparator.comparingInt(j -> j.end));

            int n = jobs.length;
            int[] ends = new int[n];
            int[] dp = new int[n + 1];

            for (int i = 0; i < n; i++) {
                ends[i] = jobs[i].end;
            }

            for (int i = 1; i <= n; i++) {
                Job current = jobs[i - 1];

                int compatibleCount = upperBound(
                        ends,
                        i - 1,
                        current.start
                );

                int take = current.profit + dp[compatibleCount];
                int skip = dp[i - 1];

                dp[i] = Math.max(take, skip);
            }

            return dp[n];
        }

        /*
         * Search only previous jobs: indices [0, exclusiveRight).
         * Returns how many of those jobs have end <= target.
         *
         * Because dp[k] means "best answer using first k jobs",
         * the returned COUNT is also exactly the dp index we need.
         */
        private int upperBound(int[] sortedEnds, int exclusiveRight, int target) {
            int left = 0;
            int right = exclusiveRight;

            while (left < right) {
                int mid = left + (right - left) / 2;

                if (sortedEnds[mid] <= target) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            return left;
        }
    }

    /*
     * WHY? — WEIGHTED JOB SCHEDULING
     * ------------------------------
     * 1. Why sort by END?
     *      It creates a solved-prefix structure.
     *      When processing current job i, every potentially compatible predecessor
     *      appears somewhere among the jobs before i.
     *
     *      This is the weighted extension of the same END-order intuition from
     *      unweighted Activity Selection, but the decision rule changes from
     *      "always take earliest finish" to "compare TAKE vs SKIP".
     *
     * 2. What exactly does dp[i] mean?
     *
     *          dp[i] = maximum profit obtainable using the FIRST i jobs
     *                  after sorting by END.
     *
     *      Therefore:
     *          dp[0] = 0
     *          answer = dp[n]
     *
     * 3. Why are there only TWO choices for each current job?
     *
     *      SKIP current:
     *          best remains dp[i - 1].
     *
     *      TAKE current:
     *          current.profit
     *          + best solution using only jobs that finish before current starts.
     *
     *      Every valid optimal solution either contains current or does not.
     *      The cases are exhaustive and mutually exclusive.
     *
     * 4. Why can TAKE use dp[compatibleCount]?
     *      Because all jobs inside that prefix end <= current.start.
     *      The DP has already computed the best mutually compatible profit from
     *      exactly that eligible past.
     *
     * 5. Why binary search?
     *      Without it, for every current job we could scan backward to find the
     *      latest compatible predecessor -> O(n) lookup per job -> O(n^2).
     *
     *      END times are sorted, so compatibility has a monotone boundary:
     *
     *          end <= current.start   end <= current.start   end > current.start ...
     *          compatible             compatible             incompatible
     *
     *      Binary search finds that boundary in O(log n).
     *
     * 6. Why upperBound returns a COUNT instead of predecessor index?
     *      Suppose 3 previous jobs have end <= current.start.
     *      Their array indices are 0..2, but the matching DP state is dp[3].
     *
     *      Returning the count avoids an easy +1 / -1 indexing mistake.
     *
     * 7. Why search only [0, i - 1)?
     *      current is jobs[i - 1].
     *      Only jobs strictly before current in the sorted order may be predecessors.
     *      The helper's exclusiveRight = i - 1 excludes current itself.
     *
     * 8. Correctness invariant
     *      After computing dp[i], dp[i] is optimal for the first i end-sorted jobs.
     *
     *      Induction:
     *          Base: dp[0] = 0 is optimal for no jobs.
     *
     *          Step: every optimal solution for first i jobs either skips current
     *          -> dp[i - 1], or takes current -> current.profit + optimal compatible
     *          prefix. We compute both and keep the larger.
     *
     * 9. Complexity
     *      Sort jobs:                    O(n log n)
     *      n binary-search transitions: O(n log n)
     *      DP / ends arrays:             O(n) space
     *
     *      Total: O(n log n) time, O(n) extra space.
     */

    // =========================================================================
    // GREEDY -> DP BOUNDARY
    // =========================================================================

    /*
     * UNWEIGHTED INTERVAL SCHEDULING
     * ------------------------------
     * Objective:
     *      maximize NUMBER of compatible intervals.
     *
     * Every accepted interval contributes the same value: 1.
     * Earliest finish preserves the most future room, so greedy works.
     *
     *
     * WEIGHTED INTERVAL SCHEDULING
     * ----------------------------
     * Objective:
     *      maximize TOTAL PROFIT.
     *
     * Intervals no longer contribute equal value.
     * An interval that finishes later may be worth more than several earlier jobs.
     * Therefore "leave maximum future room" is not sufficient by itself.
     *
     * COUNTEREXAMPLE TO EARLIEST-FINISH GREEDY
     * ----------------------------------------
     *      A = [1,2], profit 50
     *      B = [2,3], profit 50
     *      C = [1,3], profit 120
     *
     * Earliest-finish greedy:
     *      A + B = 100
     *
     * Optimal weighted answer:
     *      C = 120
     *
     * QUESTION MUTATION:
     *
     *      MAXIMIZE COUNT
     *          ↓ add unequal VALUE / PROFIT
     *      MAXIMIZE WEIGHT
     *
     *      GREEDY
     *          ↓ preserve competing choices
     *      DP
     */

    // =========================================================================
    // BRUTE FORCE -> MEMOIZATION -> BOTTOM-UP PROGRESSION
    // =========================================================================

    /*
     * BRUTE-FORCE DERIVATION
     * ----------------------
     * For every job:
     *      SKIP it
     *      TAKE it if compatible
     *
     * That is an exponential decision tree in the naive form.
     * This is the SAME TAKE / SKIP skeleton seen in many DP problems.
     *
     * Why not keep a full brute-force implementation here?
     *      It adds little interview ROI once the state transition is understood.
     *      The useful part is recognizing the repeated subproblem.
     *
     * MEMOIZATION
     * -----------
     * A top-down formulation can memoize the best answer from an index and binary
     * search the next compatible job. This is correct and common.
     *
     * BOTTOM-UP CHOICE HERE
     * ---------------------
     * The canonical file keeps end-sorted bottom-up DP because:
     *      - it connects directly to IntervalGreedyByEnd.java
     *      - dp[i] has a compact "best first i jobs" invariant
     *      - predecessor lookup cleanly maps to an already-solved prefix
     *      - no recursion stack is required
     *
     * Learn ONE deeply. Recognize the top-down/start-sorted version as the same
     * DP viewed from the opposite direction; do not treat it as a new pattern.
     */

    // =========================================================================
    // 30-SECOND RECALL CARD
    // =========================================================================

    /*
     * WEIGHTED INTERVAL SCHEDULING
     * ----------------------------
     * Trigger:
     *      compatible intervals + unequal profit / value.
     *
     * Sort:
     *      by END.
     *
     * State:
     *      dp[i] = best profit using first i jobs.
     *
     * Transition:
     *      skip = dp[i - 1]
     *      take = profit[i] + dp[number of previous jobs ending <= start[i]]
     *      dp[i] = max(skip, take)
     *
     * Search:
     *      binary search predecessor boundary in sorted END times.
     *
     * Complexity:
     *      O(n log n) time, O(n) space.
     *
     * Memory line:
     *      COUNT -> greedy.
     *      PROFIT -> take/skip DP + predecessor BS.
     */

    // =========================================================================
    // REUSABLE MASTER TEMPLATE
    // =========================================================================

    /*
     * sort choices by finishing boundary
     * build sorted finish[]
     *
     * dp[0] = 0
     *
     * for i = 1..n:
     *      current = choice[i - 1]
     *
     *      compatibleCount = upperBound(
     *              finish,
     *              previous choices only,
     *              current.start
     *      )
     *
     *      skip = dp[i - 1]
     *      take = current.value + dp[compatibleCount]
     *
     *      dp[i] = max(skip, take)
     *
     * return dp[n]
     *
     * REUSABLE IDEA BEYOND INTERVALS:
     *      sort states so a decision depends on a monotone searchable predecessor,
     *      then DP over the resulting DAG / ordered dependency structure.
     */

    // =========================================================================
    // HORIZONTAL MASTERY — SAME ENGINE / GENERALIZATIONS
    // =========================================================================

    /*
     * SAME ENGINE
     * -----------
     * Weighted Job Scheduling / Maximum Profit Job Scheduling
     *      -> compatible intervals + profit.
     *
     * Weighted Activity Selection
     *      -> same mathematical problem under different nouns.
     *
     *
     * IMPORTANT GENERALIZATION
     * ------------------------
     * Unweighted Activity Selection:
     *      each selected interval contributes 1.
     *
     * Weighted Scheduling:
     *      each selected interval contributes arbitrary value.
     *
     * The input geometry barely changed.
     * The OBJECTIVE changed enough to move the algorithm from greedy to DP.
     *
     *
     * BINARY SEARCH ROLE
     * ------------------
     * Binary search is NOT the main pattern here.
     * DP is the optimization engine.
     * Binary search only accelerates the transition by locating the compatible past.
     *
     * This distinction matters:
     *      "uses binary search" does not make the problem a Binary Search problem.
     */

    // =========================================================================
    // QUESTION MUTATIONS / CROSS-BRANCH CONNECTIONS
    // =========================================================================

    /*
     * MUTATION 1 — PROFIT -> COUNT
     * ----------------------------
     * Remove unequal profits; every accepted interval is worth 1.
     *
     *      -> maximum compatible count
     *      -> IntervalGreedyByEnd.java
     *      -> earliest-finish greedy
     *
     *
     * MUTATION 2 — CHOOSE -> PEAK OCCUPANCY
     * -------------------------------------
     * Stop choosing a compatible subset and instead ask:
     *      how many intervals are active simultaneously?
     *
     *      -> IntervalActiveOverTime.java
     *
     *
     * MUTATION 3 — CHOOSE -> COMBINE / DETECT
     * ---------------------------------------
     * Ask for union / insertion / existence of overlap instead of optimal value.
     *
     *      -> IntervalSortByStart.java
     *
     *
     * MUTATION 4 — STATIC OFFLINE -> DYNAMIC ONLINE
     * ---------------------------------------------
     * This canonical DP assumes the full job set is available so we can sort it.
     * Repeated online insertions / deletions / queries form a different dynamic
     * data-structure problem; do not force this offline DP onto it.
     */

    // =========================================================================
    // HIGH-ROI VARIATIONS / FOLLOW-UPS
    // =========================================================================

    /*
     * MASTER DEEPLY
     *      end-sort + dp[i] prefix state + predecessor upperBound.
     *
     * KEEP FLUENT
     *      top-down memoization + binary search next compatible job.
     *      Same DP, opposite direction; recognize, do not over-drill.
     *
     * HIGH-ROI FOLLOW-UP — RETURN THE ACTUAL JOBS
     *      Profit only requires dp[].
     *      To reconstruct the chosen schedule, store enough decision information
     *      or backtrack through dp:
     *
     *          if dp[i] == dp[i - 1] -> current can be skipped
     *          else                  -> current was taken; jump to compatibleCount
     *
     *      If ties matter, define a deterministic tie policy.
     *
     * HIGH-ROI FOLLOW-UP — LARGE PROFIT TOTALS
     *      If constraints can make the total exceed int range, use long for:
     *          Job.profit
     *          dp[]
     *          take / skip
     *
     * DO NOT ADD SECONDARY IMPLEMENTATIONS JUST BECAUSE THEY ARE CORRECT.
     * An alternative earns code only if it adds a reusable pattern, meaningful
     * tradeoff, complexity improvement, or common interview follow-up.
     */

    // =========================================================================
    // ENDPOINT SEMANTICS / BINARY SEARCH DETAILS / JAVA NOTES
    // =========================================================================

    /*
     * ENDPOINT POLICY — ASK BEFORE CODING
     * -----------------------------------
     * This file treats:
     *
     *      previous.end == current.start
     *
     * as COMPATIBLE.
     * Therefore predecessor condition is:
     *
     *      end <= current.start
     *
     * If touching intervals conflict in the problem statement, change the search
     * condition to strict end < current.start.
     *
     *
     * UPPER BOUND — WHAT IT MEANS HERE
     * --------------------------------
     * We want the FIRST end that is > current.start.
     * Its index is also the NUMBER of previous jobs with end <= current.start.
     * That count maps directly to dp[count].
     *
     * Boundary invariant during binary search:
     *      [0, left)  -> known compatible ends <= target
     *      [right, R) -> known incompatible ends > target
     *      [left,right) remains unresolved
     *
     * Termination:
     *      every iteration shrinks [left,right), so eventually left == right.
     *
     *
     * JAVA NOTES
     * ----------
     * Arrays.sort(Object[], Comparator) mutates the supplied jobs array and may use
     * O(n) auxiliary memory in Java. Do not blindly claim O(1) sort space.
     *
     * Comparator.comparingInt(j -> j.end) is preferred over subtraction such as
     * (a, b) -> a.end - b.end, which can overflow.
     *
     * Why an int[] ends array?
     *      Binary search needs only end boundaries, not complete Job objects.
     *      Separating ends makes the search target explicit and keeps the helper simple.
     *
     * Why no List in the canonical solution?
     *      The requested output is a scalar maximum profit. Arrays are sufficient.
     *      Introduce a List only if the interviewer asks for the chosen job identities.
     */

    // =========================================================================
    // COMPACT DRY RUN — DP INDEXING
    // =========================================================================

    /*
     * Jobs after sorting by END:
     *
     *      J1 = [1,3], p=50
     *      J2 = [2,4], p=10
     *      J3 = [3,5], p=40
     *      J4 = [3,6], p=70
     *
     * ends = [3,4,5,6]
     *
     * dp[0] = 0
     *
     * i=1, J1:
     *      previous compatible count = 0
     *      take = 50 + dp[0] = 50
     *      skip = dp[0]      = 0
     *      dp[1] = 50
     *
     * i=2, J2:
     *      no previous end <= 2
     *      take = 10
     *      skip = 50
     *      dp[2] = 50
     *
     * i=3, J3 starts at 3:
     *      J1.end == 3 is compatible -> compatibleCount = 1
     *      take = 40 + dp[1] = 90
     *      skip = 50
     *      dp[3] = 90
     *
     * i=4, J4 starts at 3:
     *      compatibleCount = 1
     *      take = 70 + dp[1] = 120
     *      skip = 90
     *      dp[4] = 120
     *
     * Answer = 120.
     *
     * KEY INDEXING IDEA:
     *      compatibleCount is a PREFIX SIZE, so use dp[compatibleCount] directly.
     */

    // =========================================================================
    // INTERVIEW ARTICULATION
    // =========================================================================

    /*
     * SAY IT LIKE THIS
     * ----------------
     * "This is weighted interval scheduling. If every interval were worth one,
     * earliest-finish greedy would maximize the count, but unequal profits break
     * that greedy choice. I sort jobs by end time and define dp[i] as the maximum
     * profit using the first i jobs. For each current job I either skip it, giving
     * dp[i-1], or take it and add its profit to the best prefix ending no later than
     * its start. Because end times are sorted, I binary search that compatible
     * prefix in O(log n). The transition considers both exhaustive possibilities,
     * so by induction dp[i] is optimal. Sorting plus n binary searches gives
     * O(n log n) time and O(n) space."
     *
     * SHORTEST DEFENSIBLE PROOF
     * -------------------------
     * Every optimal solution for the first i jobs either excludes current or includes
     * it. Excluding gives dp[i-1]. Including current forbids every overlapping job,
     * leaving exactly an already-solved compatible prefix. We take the better case.
     *
     * STOPPING CONDITION
     * ------------------
     * The outer loop increases i from 1 through n exactly once.
     * Each predecessor binary search strictly shrinks its search interval.
     */

    // =========================================================================
    // REINFORCEMENT / DISCRIMINATION
    // =========================================================================

    /*
     * SAME ENGINE
     * -----------
     * Maximum Profit Job Scheduling / Weighted Activity Selection
     *      -> compatible ranges + unequal reward.
     *
     * IMMEDIATE PREDECESSOR PATTERN
     * -----------------------------
     * Whenever a sorted DP transition needs:
     *      "best state before this boundary"
     * ask whether that boundary can be located with binary search.
     *
     * PATTERN BOUNDARIES
     * ------------------
     * Activity Selection / Transaction Scheduling
     *      maximize compatible COUNT
     *      -> IntervalGreedyByEnd.java
     *
     * Meeting Rooms II / Minimum Platforms
     *      peak simultaneous occupancy
     *      -> IntervalActiveOverTime.java
     *
     * Merge / Insert / Meeting Rooms I
     *      ordered-range relationship questions
     *      -> IntervalSortByStart.java
     */

    // =========================================================================
    // MASTERY EXIT CHECK
    // =========================================================================

    /*
     * MOVE ON WHEN YOU CAN:
     *      [ ] Explain exactly why unequal profit breaks earliest-finish greedy.
     *      [ ] State dp[i] without looking it up.
     *      [ ] Derive TAKE and SKIP from that state.
     *      [ ] Explain why the compatible past is a prefix after END sorting.
     *      [ ] Write the upperBound while-loop without an off-by-one mistake.
     *      [ ] Explain why upperBound returns a COUNT and how that maps to dp[].
     *      [ ] Handle touching endpoints from the problem statement.
     *      [ ] State O(n log n) time / O(n) space and justify both.
     *      [ ] Explain the Greedy -> DP question mutation aloud.
     *      [ ] Reconstruct the canonical solution under interview pressure.
     */

    // =========================================================================
    // TESTS
    // Run with assertions enabled:
    // java -ea org.chijai.day1.Arrays.session4.Intervals.WeightedIntervalScheduling
    // =========================================================================

    public static void main(String[] args) {

        MaximumProfitJobs scheduler = new MaximumProfitJobs();

        assert scheduler.maxProfit(new Job[]{
                new Job(1, 3, 50),
                new Job(2, 4, 10),
                new Job(3, 5, 40),
                new Job(3, 6, 70)
        }) == 120;

        // Explicit greedy counterexample: earliest-finish count logic would get 100.
        assert scheduler.maxProfit(new Job[]{
                new Job(1, 2, 50),
                new Job(2, 3, 50),
                new Job(1, 3, 120)
        }) == 120;

        assert scheduler.maxProfit(new Job[]{
                new Job(1, 2, 20),
                new Job(2, 3, 30),
                new Job(3, 4, 40)
        }) == 90;

        assert scheduler.maxProfit(new Job[]{
                new Job(1, 10, 100),
                new Job(2, 3, 30),
                new Job(3, 4, 30),
                new Job(4, 5, 30),
                new Job(5, 6, 30)
        }) == 120;

        assert scheduler.maxProfit(new Job[]{
                new Job(5, 7, 42)
        }) == 42;

        assert scheduler.maxProfit(new Job[]{}) == 0;
        assert scheduler.maxProfit(null) == 0;

        // Input order should not matter.
        assert scheduler.maxProfit(new Job[]{
                new Job(3, 5, 40),
                new Job(1, 3, 50),
                new Job(3, 6, 70),
                new Job(2, 4, 10)
        }) == 120;

        System.out.println("All WeightedIntervalScheduling assertions passed.");
    }
}
