package org.chijai.day1.Arrays.session4.Intervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * INTERVAL PATTERN 1 — SORT BY START
 *
 * Horizontal mastery:
 *      Meeting Rooms I  -> detect overlap
 *      https://leetcode.com/problems/meeting-rooms/
 *      Merge Intervals  -> combine overlap
 *      https://leetcode.com/problems/merge-intervals/
 *      Insert Interval  -> exploit already-sorted structure
 *      https://leetcode.com/problems/insert-interval/
 *
 * MASTER ENGINE
 * -------------
 * Put intervals in start order so range relationships become local.
 * After sorting, the processed past can be compressed into the relevant
 * previous / active interval.
 *
 * ONE-LINE RECALL
 * ---------------
 *      DETECT -> sort by start -> compare neighbors
 *      MERGE  -> sort by start -> stretch active range -> commit on gap
 *      INSERT -> before -> merge -> add -> after
 *
 * CODE STYLE
 * ----------
 * Keep reconstruction familiar: arrays / ArrayList / Comparator / plain loops.
 * No Streams or Collectors in core algorithm logic.
 * The small Interval class is only a readable domain model; the same engines
 * apply directly to int[][] interview-platform signatures.
 */
public class IntervalSortByStart {

    // =========================================================================
    // DOMAIN MODEL
    // =========================================================================

    static final class Interval {
        final int start;
        final int end;

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "[" + start + "," + end + "]";
        }
    }



    // =========================================================================
    // SIMPLE BOUNDARY CONVERSION — JAVA FLUENCY
    // =========================================================================

    static List<Interval> toIntervals(int[][] raw) {
        List<Interval> result = new ArrayList<>();

        for (int[] interval : raw) {
            result.add(new Interval(interval[0], interval[1]));
        }

        return result;
    }

    static int[][] toArray(List<Interval> intervals) {
        int[][] result = new int[intervals.size()][2];

        for (int i = 0; i < intervals.size(); i++) {
            result[i][0] = intervals.get(i).start;
            result[i][1] = intervals.get(i).end;
        }

        return result;
    }

    /*
     * WHY KEEP THESE HELPERS?
     * -----------------------
     * Interview platforms often expose intervals as int[][].
     * Reasoning is often clearer with a small Interval domain object.
     *
     *      int[][] -> List<Interval>
     *          use raw.length for arrays
     *          use add(...) to build the List
     *
     *      List<Interval> -> int[][]
     *          use list.size() for Lists
     *          allocate the fixed-size output array first
     *
     * This is boundary-conversion fluency, not a new interval algorithm.
     */

    // =========================================================================
    // PATTERN POSITION / RECOGNITION
    // =========================================================================

    /*
     * USE THIS FILE WHEN THE QUESTION ASKS ABOUT ORDERED RANGE RELATIONSHIPS:
     *
     *      Any conflict exists?
     *          -> Meeting Rooms I
     *
     *      Union / merge overlapping ranges?
     *          -> Merge Intervals
     *
     *      Insert into already sorted, non-overlapping ranges?
     *          -> Insert Interval
     *
     * NOT THIS ENGINE:
     *
     *      Peak simultaneous overlap / minimum rooms?
     *          -> IntervalActiveOverTime.java
     *
     *      Maximize compatible interval count / minimum removals / arrows?
     *          -> IntervalGreedyByEnd.java
     *
     *      Maximize weighted compatible profit?
     *          -> WeightedIntervalScheduling.java
     *
     * MASTER QUESTION:
     *      What must I COMPUTE from the intervals?
     *      Similar nouns do not guarantee the same pattern.
     *
     * FINITE-TIME INTERVIEW LOOP:
     *      1. Identify the exact output.
     *      2. State the brute force.
     *      3. Name the expensive operation.
     *      4. Choose the engine that removes that bottleneck.
     *      5. State one invariant before coding.
     *      6. Prefer the lowest-risk correct implementation you can explain.
     */

    // =========================================================================
    // 1) MEETING ROOMS I — DETECT
    // Goal: Can one person attend every meeting?
    // =========================================================================

    static final class MeetingRoomsI {

        boolean canAttendMeetings(int[][] rawMeetings) {
            if (rawMeetings == null || rawMeetings.length <= 1) {
                return true;
            }

            List<Interval> meetings = toIntervals(rawMeetings);
            meetings.sort(Comparator.comparingInt(i -> i.start));

            for (int i = 1; i < meetings.size(); i++) {
                if (meetings.get(i).start < meetings.get(i - 1).end) {
                    return false;
                }
            }

            return true;
        }
    }

    /*
     * WHY? — MEETING ROOMS I
     * ----------------------
     * 1. Why sort by START?
     *      It puts meetings into chronological start order.
     *      Then overlap existence can be verified locally.
     *
     * 2. Why are adjacent checks enough?
     *      Suppose every adjacent pair processed so far is non-overlapping.
     *      Then each previous meeting ends before the next one starts.
     *      Therefore an earlier non-adjacent meeting cannot suddenly overlap
     *      the current meeting without an adjacent conflict having appeared first.
     *
     * 3. Why start < previousEnd?
     *      This file uses the Meeting Rooms convention that [1,5] and [5,8]
     *      are compatible: the first meeting has ended when the second starts.
     *
     * 4. Complexity
     *      O(n log n) time from sorting; O(n) is the scan.
     *
     * BRUTE-FORCE DERIVATION
     *      Compare every pair -> O(n^2).
     *      Generic half-open overlap test:
     *          max(a.start, b.start) < min(a.end, b.end)
     *      Sorting removes the need to ask about every pair.
     *
     * IMPORTANT NUANCE
     *      Sorting by end can also be made correct for existence detection.
     *      START order remains canonical here because its invariant is simpler
     *      and transfers directly to Merge / Insert reasoning.
     */

    // =========================================================================
    // 2) MERGE INTERVALS — COMBINE
    // Goal: Return the union of overlapping ranges.
    // =========================================================================

    static final class MergeIntervals {

        // LeetCode signature: int[][] -> int[][]
        int[][] merge(int[][] rawIntervals) {
            if (rawIntervals == null || rawIntervals.length == 0) {
                return new int[0][0];
            }

            List<Interval> intervals = toIntervals(rawIntervals);

            if (intervals.size() == 1) {
                return toArray(intervals);
            }

            intervals.sort(Comparator.comparingInt(i -> i.start));

            List<Interval> merged = new ArrayList<>();
            int activeStart = intervals.get(0).start;
            int activeEnd = intervals.get(0).end;

            for (int i = 1; i < intervals.size(); i++) {
                Interval current = intervals.get(i);

                if (current.start <= activeEnd) {
                    activeEnd = Math.max(activeEnd, current.end);
                } else {
                    merged.add(new Interval(activeStart, activeEnd));
                    activeStart = current.start;
                    activeEnd = current.end;
                }
            }

            merged.add(new Interval(activeStart, activeEnd));
            return toArray(merged);
        }
    }

    /*
     * WHY? — MERGE INTERVALS
     * ----------------------
     * 1. Why sort by START?
     *      Once current.start is beyond activeEnd, every later interval starts
     *      at least as late, so the active merged range can never connect again.
     *
     * 2. What does the active range mean?
     *      [activeStart, activeEnd] is the union of the current connected block
     *      of overlapping intervals processed so far.
     *
     * 3. Why only extend activeEnd?
     *      Start order guarantees current.start >= activeStart.
     *      On overlap, only the right boundary can expand.
     *
     * 4. Why commit on a gap?
     *      current.start > activeEnd proves the active block is complete.
     *
     * 5. Why commit once after the loop?
     *      A block is committed when a later gap exposes its end.
     *      The final block has no later interval to trigger that commit.
     *
     * 6. Complexity
     *      O(n log n) time from sorting; O(n) output space.
     */

    // =========================================================================
    // 3) INSERT INTERVAL — EXPLOIT EXISTING ORDER
    // Goal: Insert into sorted, non-overlapping intervals and merge if needed.
    // =========================================================================

    static final class InsertInterval {

        // LeetCode already gives exactly the representation this problem needs.
        // Keep this solution directly in int[][] / int[] form.
        int[][] insert(int[][] intervals, int[] newInterval) {
            if (newInterval == null) {
                return intervals == null ? new int[0][0] : intervals;
            }

            if (intervals == null) {
                intervals = new int[0][];
            }

            List<int[]> result = new ArrayList<>();

            for (int[] current : intervals) {

                // Case 1: current is completely before newInterval.
                if (newInterval != null && current[1] < newInterval[0]) {
                    result.add(current);
                }

                // Case 2: current is completely after newInterval.
                // Place newInterval once, then current.
                else if (newInterval != null && current[0] > newInterval[1]) {
                    result.add(newInterval);
                    result.add(current);
                    newInterval = null;
                }

                // Case 3: current overlaps newInterval -> grow newInterval.
                else if (newInterval != null) {
                    newInterval[0] = Math.min(newInterval[0], current[0]);
                    newInterval[1] = Math.max(newInterval[1], current[1]);
                }

                // Case 4: newInterval was already placed -> copy the rest.
                else {
                    result.add(current);
                }
            }

            // Case 5: newInterval belongs at the end.
            if (newInterval != null) {
                result.add(newInterval);
            }

            return result.toArray(new int[result.size()][]);
        }
    }

    /*
     * WHY? — INSERT INTERVAL
     * ----------------------
     * PRECONDITION:
     *      Input intervals are already sorted by start and non-overlapping.
     *
     * This stronger input invariant means we do NOT need to append the new
     * interval and re-sort everything.
     *
     * The timeline is still:
     *
     *      BEFORE | OVERLAP | AFTER
     *
     * but the code deliberately expresses it as explicit cases because that is
     * easier to reconstruct under interview pressure:
     *
     * 1. current BEFORE pending
     *      current.end < pending.start
     *      -> copy current.
     *
     * 2. current AFTER pending
     *      current.start > pending.end
     *      -> place pending once, then copy current; pending is finalized.
     *
     * 3. OVERLAP
     *      neither strictly before nor strictly after
     *      -> grow pending to the union.
     *
     * 4. pending already placed
     *      -> copy every remaining current interval.
     *
     * 5. pending still exists after the loop
     *      -> it belongs at the end; append it once.
     *
     * Complexity:
     *      O(n) time, O(n) output space.
     *
     * HIGH-ROI INSIGHT:
     *      Merge Intervals must create order first.
     *      Insert Interval receives that order as a precondition and exploits it.
     */

    // =========================================================================
    // 30-SECOND RECALL CARD
    // =========================================================================

    /*
     * ENGINE
     *      Ordered range relationships -> sort by START.
     *
     * MEETING ROOMS I
     *      sort -> adjacent conflict?
     *      current.start < previous.end -> conflict
     *
     * MERGE
     *      sort -> active range
     *      overlap -> extend end
     *      gap     -> commit + reset
     *      finish  -> commit last
     *
     * INSERT
     *      input already sorted + non-overlapping
     *      before -> after/place -> overlap/grow -> already placed -> append pending
     *
     * MEMORY HOOK
     *      DETECT -> COMBINE -> INSERT
     */

    // =========================================================================
    // REUSABLE MASTER TEMPLATES
    // =========================================================================

    /*
     * TEMPLATE A — SORT + LOCAL VERIFICATION
     * --------------------------------------
     * sort by start
     * for each current after the first:
     *      compare current with relevant previous range
     *
     * Use when the output is a property such as:
     *      conflict exists / validity / ordered range relationship.
     *
     *
     * TEMPLATE B — SORT + ACTIVE RANGE
     * --------------------------------
     * sort by start
     * active = first interval
     *
     * for each current:
     *      if current connects to active:
     *          extend active
     *      else:
     *          commit active
     *          active = current
     *
     * commit active
     *
     * Use when ranges must be combined into connected components.
     *
     *
     * TEMPLATE C — ALREADY SORTED INSERTION
     * -------------------------------------
     * pending = new interval
     *
     * for each current:
     *      if current before pending -> copy current
     *      else if current after pending -> place pending + current; finalize pending
     *      else if pending exists -> merge current into pending
     *      else -> copy current
     *
     * if pending still exists -> append it
     */

    // =========================================================================
    // HORIZONTAL MASTERY — SAME ENGINE, DIFFERENT OUTPUT
    // =========================================================================

    /*
     * SAME START-ORDERING IDEA
     * ------------------------
     * Meeting Rooms I
     *      output = boolean
     *      ask whether overlap exists.
     *
     * Merge Intervals
     *      output = union ranges
     *      keep a running active range instead of returning on first overlap.
     *
     * Insert Interval
     *      output = updated union after one insertion
     *      sorting is already guaranteed by the input, so skip the sort.
     *
     * PROGRESSION
     * -----------
     *      DETECT
     *        ↓ retain the connected range instead of stopping
     *      MERGE
     *        ↓ assume ranges are already normalized and insert one new range
     *      INSERT
     */

    // =========================================================================
    // QUESTION MUTATIONS / CROSS-BRANCH CONNECTIONS
    // =========================================================================

    /*
     * MUTATION 1
     * ----------
     * Meeting Rooms I:
     *      "Does ANY overlap exist?"
     *      -> Sort by start + adjacent verification.
     *
     * Change the question to:
     *      "What is the PEAK number of simultaneous meetings?"
     *
     *      -> Meeting Rooms II
     *      -> ACTIVE-OVER-TIME
     *      -> IntervalActiveOverTime.java
     *
     * Same nouns. Different requested information. Different engine.
     *
     *
     * MUTATION 2
     * ----------
     * Merge Intervals:
     *      "Combine overlapping ranges."
     *
     * Change the question to:
     *      "Choose the maximum number of mutually compatible ranges."
     *
     *      -> SORT BY END + GREEDY
     *      -> IntervalGreedyByEnd.java
     *
     *
     * MUTATION 3
     * ----------
     * Unweighted compatible selection:
     *      maximize COUNT -> greedy by end.
     *
     * Add unequal profit/value:
     *      maximize VALUE -> DP + predecessor search.
     *      -> WeightedIntervalScheduling.java
     */

    // =========================================================================
    // HIGH-ROI VARIATIONS / APPROACH POLICY
    // =========================================================================

    /*
     * MEETING ROOMS I
     *      Brute force all pairs is useful only as derivation: O(n^2).
     *      Do not drill it after sort + scan is understood.
     *
     * MERGE INTERVALS
     *      Sorting by start + active range is the canonical reusable solution.
     *      No need to memorize alternative container tricks.
     *
     * INSERT INTERVAL
     *      Generic fallback: append new interval + run Merge Intervals.
     *      Correct, but O(n log n) and throws away the stronger input invariant.
     *      Keep as a derivation/fallback, not as the preferred implementation.
     *
     * RULE
     *      An alternative earns code only if it adds a reusable pattern,
     *      meaningful trade-off, complexity improvement, or common follow-up.
     */

    // =========================================================================
    // ENDPOINT POLICY / TRAPS
    // =========================================================================

    /*
     * BEFORE CODING, ASK:
     *      If one interval ends at t and another starts at t,
     *      are they considered compatible or overlapping?
     *
     * This is problem policy, not a separate pattern.
     *
     * In THIS file:
     *
     * Meeting Rooms I
     *      [1,5] and [5,8] are compatible.
     *      conflict -> current.start < previous.end
     *
     * Merge / Insert
     *      Closed-range convention: touching ranges merge.
     *      overlap -> current.start <= activeEnd
     *
     * These conditions differ because the requested semantics differ.
     * Do not memorize < or <= without reading the statement.
     *
     * GENERIC OVERLAP VIEW:
     *      Half-open / reusable-at-touch semantics:
     *          max(start1, start2) < min(end1, end2)
     *
     *      Closed-range / touching-overlaps semantics:
     *          max(start1, start2) <= min(end1, end2)
     */

    // =========================================================================
    // JAVA / REPRESENTATION NOTES
    // =========================================================================

    /*
     * ARRAYS vs LISTS
     *      LeetCode gives intervals as int[][] / int[].
     *      Meeting Rooms I / Merge use List<Interval> where the domain object improves reasoning.
     *      Insert stays directly in int[][] / int[] because the four explicit boundary cases
     *      are clearer as current[0], current[1], newInterval[0], newInterval[1].
     *      Its output size is unknown -> List<int[]> is the natural temporary result.
     *      Java syntax reminder: array.length, but list.size().
     *
     * PLATFORM SIGNATURES
     *      Keep the interview-facing method in the platform's int[][] form.
     *      Convert only when conversion improves the algorithm's readability.
     *      Do NOT force every sibling problem through the same representation.
     *
     * SORT SIDE EFFECT
     *      Meeting Rooms I / Merge sort only their converted List, so the caller's int[][]
     *      order survives. Insert needs no sort because sorted/non-overlapping order is given.
     *
     * JAVA SPACE NUANCE
     *      Meeting Rooms I / Merge conversion uses O(n) extra space.
     *      Merge / Insert require O(n) output space.
     *
     * STYLE POLICY
     *      Prefer explicit loops and state over Streams / Collectors in interview code.
     *      Use modern Java only when it makes the invariant easier to read.
     */

    // =========================================================================
    // INTERVIEW ARTICULATION
    // =========================================================================

    /*
     * MASTER EXPLANATION
     * ------------------
     * "I first identify that the question is about ordered relationships between
     * intervals, not peak concurrency or interval selection. Sorting by start
     * makes the relevant relationship local. From there, the exact state I keep
     * depends on the output: the previous interval for conflict detection, an
     * active merged range for union, or the new interval itself for insertion."
     *
     * MEETING ROOMS I
     *      Pattern      : sort by start + adjacent verification
     *      Invariant    : no conflict exists among processed adjacent pairs
     *      Correctness  : any conflict must expose an adjacent conflict in start order
     *      Complexity   : O(n log n)
     *
     * MERGE INTERVALS
     *      Pattern      : sort by start + active range
     *      Invariant    : active range is the union of the current overlap block
     *      Correctness  : a gap finalizes the block because all later starts are later
     *      Complexity   : O(n log n)
     *
     * INSERT INTERVAL
     *      Pattern      : exploit sorted/non-overlapping input
     *      Invariant    : processed output remains sorted and non-overlapping
     *      Termination  : index only moves right through the input
     *      Complexity   : O(n)
     */

    // =========================================================================
    // REINFORCEMENT / PATTERN DISCRIMINATION
    // =========================================================================

    /*
     * SAME ENGINE — GOOD REINFORCEMENT
     * --------------------------------
     * Meeting Rooms I
     * Merge Intervals
     * Insert Interval
     *
     * CLOSE BUT DIFFERENT — USE TO TEST CLASSIFICATION
     * ------------------------------------------------
     * Meeting Rooms II
     *      peak overlap -> IntervalActiveOverTime.java
     *
     * Non-overlapping Intervals
     *      choose what to keep -> IntervalGreedyByEnd.java
     *
     * Minimum Arrows
     *      minimum covering points -> IntervalGreedyByEnd.java
     *
     * Weighted Job Scheduling
     *      weighted compatible choice -> WeightedIntervalScheduling.java
     */

    // =========================================================================
    // MASTERY EXIT CHECK
    // =========================================================================

    /*
     * Move on when you can do all of these without notes:
     *
     * [ ] Recognize ordered-range questions that want START ordering.
     * [ ] Derive Meeting Rooms I from pairwise conflict detection.
     * [ ] Explain why Merge keeps one active range.
     * [ ] Reconstruct INSERT using the explicit before / after / overlap / placed cases.
     * [ ] Decide < vs <= from endpoint semantics rather than memory.
     * [ ] Explain why Meeting Rooms II leaves this branch.
     * [ ] Code all three preferred solutions cleanly.
     */

    // =========================================================================
    // SELF-VERIFYING TESTS
    // Run with assertions enabled: java -ea ...IntervalSortByStart
    // =========================================================================

    public static void main(String[] args) {
        MeetingRoomsI meetingRoomsI = new MeetingRoomsI();
        MergeIntervals mergeIntervals = new MergeIntervals();
        InsertInterval insertInterval = new InsertInterval();

        assert !meetingRoomsI.canAttendMeetings(new int[][]{
                {0, 30},
                {5, 10},
                {15, 20}
        });

        assert meetingRoomsI.canAttendMeetings(new int[][]{
                {10, 15},
                {1, 5},
                {5, 10}
        });

        assert meetingRoomsI.canAttendMeetings(new int[][]{});

        assert Arrays.deepEquals(
                mergeIntervals.merge(new int[][]{
                        {1, 3},
                        {2, 6},
                        {8, 10},
                        {15, 18}
                }),
                new int[][]{{1, 6}, {8, 10}, {15, 18}}
        );

        assert Arrays.deepEquals(
                mergeIntervals.merge(new int[][]{
                        {1, 10},
                        {2, 3},
                        {4, 8}
                }),
                new int[][]{{1, 10}}
        );

        assert Arrays.deepEquals(
                mergeIntervals.merge(new int[][]{
                        {1, 2},
                        {2, 3}
                }),
                new int[][]{{1, 3}}
        );

        assert Arrays.deepEquals(
                insertInterval.insert(
                        new int[][]{{1, 3}, {6, 9}},
                        new int[]{2, 5}
                ),
                new int[][]{{1, 5}, {6, 9}}
        );

        assert Arrays.deepEquals(
                insertInterval.insert(
                        new int[][]{{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}},
                        new int[]{4, 8}
                ),
                new int[][]{{1, 2}, {3, 10}, {12, 16}}
        );

        assert Arrays.deepEquals(
                insertInterval.insert(
                        new int[][]{{3, 5}, {7, 9}},
                        new int[]{1, 2}
                ),
                new int[][]{{1, 2}, {3, 5}, {7, 9}}
        );

        assert Arrays.deepEquals(
                insertInterval.insert(
                        new int[][]{{1, 2}, {3, 5}},
                        new int[]{7, 9}
                ),
                new int[][]{{1, 2}, {3, 5}, {7, 9}}
        );

        assert Arrays.deepEquals(
                insertInterval.insert(
                        new int[][]{{1, 10}},
                        new int[]{3, 5}
                ),
                new int[][]{{1, 10}}
        );

        assert Arrays.deepEquals(
                insertInterval.insert(new int[][]{}, new int[]{4, 6}),
                new int[][]{{4, 6}}
        );

        System.out.println("All IntervalSortByStart assertions passed.");
    }
}
