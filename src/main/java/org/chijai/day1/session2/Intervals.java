package org.chijai.day1.session2;

import java.util.*;

/*
 =====================================================================
 📘 MERGE INTERVALS — SORT + SWEEP PATTERN CHAPTER
 =====================================================================

 This file is designed to be:
 ✔ Read like a textbook chapter
 ✔ Used for interview articulation
 ✔ Revisited months later without relearning
 ✔ Taught to others confidently

 Pattern Scope:
 - Merge Intervals
 - Insert Interval
 - Meeting Rooms II
*/

public class Intervals {

    // ============================================================
    // 🔵 CORE PATTERN OVERVIEW
    // ============================================================

    /*
     🔵 Pattern Name:
        Sort + Sweep (Interval Merging)

     🔵 Core Idea:
        Sort intervals by start.
        Sweep left → right.
        Maintain ONE active interval.
        Expand it while overlapping.
        Commit it when overlap stops.

     🔵 Why It Works:
        Sorting guarantees that overlap decisions are local.
        Once overlap breaks, it can NEVER reappear later.

     🔵 When To Use:
        - Overlapping ranges
        - Time windows / bookings
        - Schedule compression
        - Range union problems

     🧭 Pattern Recognition Signals:
        - Input looks like [start, end]
        - Overlap is defined by boundary comparison
        - Output requires non-overlapping ranges
    */

    // ============================================================
    // 🟢 MENTAL MODEL & INVARIANTS
    // ============================================================

    /*
     🟢 Mental Model:
        Walk along a number line holding ONE elastic band.
        Stretch it while intervals overlap.
        Drop it and pick a new one when overlap stops.

     🟢 Invariants (Must Always Hold):
        1. Intervals are processed in sorted start order
        2. activeInterval covers ALL overlaps seen so far
        3. activeInterval.end is the MAX end among merged intervals
        4. Once overlap breaks, it cannot resume

     🟢 Forbidden Actions:
        ❌ Checking overlap without sorting
        ❌ Tracking multiple active intervals
        ❌ Forgetting to commit the final interval
    */

    // ============================================================
    // 🟢 DOMAIN MODEL (FOR CLARITY & TEACHING)
    // ============================================================

    static class Interval {
        int start;
        int end;

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    // ============================================================
    // 🟢 BOUNDARY CONVERSION HELPERS
    // ============================================================

    static List<Interval> toIntervalList(int[][] rawIntervals) {
        List<Interval> list = new ArrayList<>();
        for (int[] raw : rawIntervals) {
            list.add(new Interval(raw[0], raw[1]));
        }
        return list;
    }

    static int[][] toArray(List<Interval> intervals) {
        int n = intervals.size();
        int[][] result = new int[n][2];
        for (int i = 0; i < n; i++) {
            result[i][0] = intervals.get(i).start;
            result[i][1] = intervals.get(i).end;
        }
        return result;
    }

    // ============================================================
    // PRIMARY PROBLEM — MERGE INTERVALS
    // ============================================================

    // ------------------------------------------------------------
    // 🟢 OPTIMAL — ARRAY VERSION (PLATFORM STYLE)

    static class MergeIntervalsOptimal {

        /**
         * ---------------------------------------------------------------------------
         * Mental Model
         * ---------------------------------------------------------------------------
         *
         * Sort intervals
         *         ↓
         * Open the first interval (activeInterval)
         *         ↓
         * For every remaining interval:
         *
         *     Does it overlap the active interval?
         *              ↓
         *        Yes → Extend the active interval.
         *
         *        No  → Finalize (store) the active interval.
         *              Start a new active interval.
         *
         * Finish iteration
         *         ↓
         * Finalize the last active interval.
         *
         * ---------------------------------------------------------------------------
         * Invariant:
         * activeInterval always represents the merged interval currently being built.
         * It is the accumulated merged span of all overlapping intervals processed
         * so far that has not yet been added to the answer.
         * ---------------------------------------------------------------------------
         */
        public int[][] merge(int[][] intervals) {

            if (intervals.length < 2)
                return intervals;

            // Sort intervals by start position.
            Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

            List<int[]> merged = new ArrayList<>();

            // Active merged interval currently being built.
            int[] activeInterval = intervals[0];

            for (int i = 1; i < intervals.length; i++) {

                int[] current = intervals[i];

                int currentStart = current[0];
                int currentEnd = current[1];

                // Overlap → extend the active interval.
                if (currentStart <= activeInterval[1]) {
                    activeInterval[1] = Math.max(activeInterval[1], currentEnd);
                }
                // Gap → finalize current interval and start a new one.
                else {
                    merged.add(activeInterval);
                    activeInterval = current;
                }
            }

            // Don't forget to finalize the last active interval.
            merged.add(activeInterval);

            return merged.toArray(new int[merged.size()][]);
        }
    }

    // ------------------------------------------------------------
    // 🟢 OPTIMAL — DOMAIN MODEL VERSION (CLARITY STYLE)
    // ------------------------------------------------------------
    static class MergeIntervalsUsingDomain {

        public int[][] merge(int[][] intervals) {

            if (intervals.length < 2) return intervals;

            List<Interval> sortedIntervals = toIntervalList(intervals);

            // 🟢 INVARIANT: sorted by start
            sortedIntervals.sort((a, b) -> a.start - b.start);

            List<Interval> merged = new ArrayList<>();

            // 🟢 INVARIANT: activeInterval covers all overlaps so far
            Interval activeInterval = sortedIntervals.get(0);

            for (int i = 1; i < sortedIntervals.size(); i++) {
                Interval current = sortedIntervals.get(i);

                if (current.start <= activeInterval.end) {
                    activeInterval.end = Math.max(activeInterval.end, current.end);
                } else {
                    merged.add(activeInterval);
                    activeInterval = current;
                }
            }

            merged.add(activeInterval);
            return toArray(merged);
        }
    }

    // ============================================================
    // ⚫ REINFORCEMENT 1 — INSERT INTERVAL
    // ============================================================

    /*
     Four explicit cases:
     1. curr ends before new starts
     2. curr starts after new ends
     3. overlap → merge
     4. new interval is last
    */

        /*
     =========================================================
     INSERT INTERVAL — VISUAL MENTAL MODEL (FROM DIAGRAM)
     =========================================================

     Timeline:  min ------------------------------------ max

     Red dashed lines = current interval boundaries

     Case 1: Current ends BEFORE new starts
     --------------------------------------------------
       New:        [----]
       Curr:               [========]

       Rule:
         curr.end < new.start
       Action:
         add curr

     --------------------------------------------------

     Case 2: Current starts AFTER new ends
     --------------------------------------------------
       Curr:      [========]
       New:                   [----]

       Rule:
         curr.start > new.end
       Action:
         add new, then curr
         mark new as finalized

     --------------------------------------------------

     Case 3: Overlap / Touch (merge zone)
     --------------------------------------------------
       Curr:         [========]
       New:       [------]

       OR

       Curr:      [========]
       New:             [------]

       OR

       Curr:      [========]
       New:   [----------------]

       Rule:
         curr.start <= new.end
         AND
         curr.end   >= new.start

       Action:
         expand new:
           new.start = min(new.start, curr.start)
           new.end   = max(new.end,   curr.end)

     --------------------------------------------------

     Case 4: New interval survives till the end
     --------------------------------------------------
       No curr interval exists AFTER new

       Rule:
         newInterval != null after loop

       Action:
         add new at the end

     =========================================================
     INVARIANT:
       newInterval always represents the merged block so far
     =========================================================
    */

    // ------------------------------------------------------------
    // 🟢 ARRAY VERSION — 4 CASE LOGIC
    // ------------------------------------------------------------
    static class InsertIntervalArray {

        public int[][] insert(int[][] intervals, int[] newInterval) {

            List<int[]> result = new ArrayList<>();

            for (int[] curr : intervals) {

                // Case 1: curr completely before new
                if (newInterval != null && curr[1] < newInterval[0]) {
                    result.add(curr);
                }

                // Case 2: curr completely after new
                else if (newInterval != null && curr[0] > newInterval[1]) {
                    result.add(newInterval);
                    result.add(curr);
                    newInterval = null; // new interval finalized
                }

                // Case 3: overlap → merge
                else if (newInterval != null) {
                    newInterval[0] = Math.min(newInterval[0], curr[0]);
                    newInterval[1] = Math.max(newInterval[1], curr[1]);
                }

                // new already placed, just copy remaining
                else {
                    result.add(curr);
                }
            }

            // Case 4: new interval goes till the end
            if (newInterval != null) {
                result.add(newInterval);
            }

            return result.toArray(new int[result.size()][]);
        }
    }

    // ------------------------------------------------------------
    // 🟢 DOMAIN MODEL VERSION — SAME 4 CASES
    // ------------------------------------------------------------
    static class InsertIntervalUsingDomain {

        public int[][] insert(int[][] intervals, int[] newRaw) {

            List<Interval> sorted = toIntervalList(intervals);
            Interval newInterval = new Interval(newRaw[0], newRaw[1]);

            List<Interval> result = new ArrayList<>();

            for (Interval curr : sorted) {

                // Case 1: curr completely before new
                if (newInterval != null && curr.end < newInterval.start) {
                    result.add(curr);
                }

                // Case 2: curr completely after new
                else if (newInterval != null && curr.start > newInterval.end) {
                    result.add(newInterval);
                    result.add(curr);
                    newInterval = null;
                }

                // Case 3: overlap → merge
                else if (newInterval != null) {
                    newInterval.start = Math.min(newInterval.start, curr.start);
                    newInterval.end   = Math.max(newInterval.end, curr.end);
                }

                // new already placed
                else {
                    result.add(curr);
                }
            }

            // Case 4: new interval is last
            if (newInterval != null) {
                result.add(newInterval);
            }

            return toArray(result);
        }
    }

    // ============================================================
    // ⚫ REINFORCEMENT 2 — MEETING ROOMS II
    // ============================================================

    /*
     ⚫ SAME CORE IDEA:
        Sweep sorted boundaries.
        But here we COUNT overlaps instead of merging them.

        Given an array of meeting time intervals consisting of start and end times[[s1,e1],[s2,e2],...](si< ei), find the minimum number of conference rooms required.
        Example 1:
        Input:
        [[0, 30],[5, 10],[15, 20]]
        Output:
        2
        Example 2:
        Input:
        [[7,10],[2,4]]
        Output:

    */

    // ------------------------------------------------------------
    // 🟢 ARRAY VERSION
    // ------------------------------------------------------------
    static class MeetingRoomsIIArray {

        public int minMeetingRooms(int[][] intervals) {

            // Whenever an old meeting ends before a new meeting starts, we reuse the room (i.e., do not add more room). Otherwise, we need an extra room (i.e., add a room).
            int[] starts = new int[intervals.length];
            int[] ends = new int[intervals.length];

            for (int i = 0; i < intervals.length; i++) {
                starts[i] = intervals[i][0];
                ends[i] = intervals[i][1];
            }

            Arrays.sort(starts);
            Arrays.sort(ends);

            int rooms = 0;
            int endIndex = 0;

            for (int start : starts) {

                // If a meeting starts before the earliest one ends → overlap -> need a new room
                if (start < ends[endIndex]) {
                    rooms++;
                } else {
                    // Otherwise, reuse a room
                    // Meeting ended , room freed up.
                    endIndex++;
                }
            }
            return rooms;
        }
    }


    // ============================================================
    // 🧪 MAIN METHOD — TESTS (MUST BE LAST)
    // ============================================================

    public static void main(String[] args) {

        MergeIntervalsUsingDomain merge = new MergeIntervalsUsingDomain();

        // 🟡 Core case
        int[][] input1 = {{1,3},{2,6},{8,10},{15,18}};
        System.out.println(Arrays.deepToString(merge.merge(input1)));
        // Expected: [[1,6],[8,10],[15,18]]

        // 🟡 Single interval (interviewer trap)
        int[][] input2 = {{5,7}};
        System.out.println(Arrays.deepToString(merge.merge(input2)));
        // Expected: [[5,7]]

        // 🟡 All overlapping
        int[][] input3 = {{1,4},{2,3},{3,5}};
        System.out.println(Arrays.deepToString(merge.merge(input3)));
        // Expected: [[1,5]]

        // ❌ INTERVIEW TRAP: touching but non-overlapping
        int[][] input4 = {{1,2},{3,3}};
        System.out.println(Arrays.deepToString(merge.merge(input4)));
        // Expected: [[1,2],[3,3]]

        InsertIntervalUsingDomain insert = new InsertIntervalUsingDomain();
        System.out.println(Arrays.deepToString(
                insert.insert(new int[][]{{1,3},{6,9}}, new int[]{2,5})
        ));
        // Expected: [[1,5],[6,9]]
        
    }
}
