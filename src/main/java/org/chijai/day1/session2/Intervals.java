package org.chijai.day1.session2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * INTERVALS — FINAL INTERVIEW VERSION
 *
 * Keep Java modern where it improves readability:
 *   - record Interval
 *   - Comparator.comparingInt(Interval::start)
 *
 * Keep algorithm reconstruction simple:
 *   - plain loops
 *   - no Streams / Collectors in core or plumbing
 *
 * Recall:
 *   MERGE  = sort -> active -> overlap: merge -> gap: commit/reset -> commit last
 *   INSERT = before -> overlap -> add -> after
 *   ROOMS  = sort starts + ends -> overlap: new room -> else reuse
 */
public class Intervals {

    // ============================================================
    // DOMAIN MODEL
    // ============================================================
    record Interval(int start, int end) {

        Interval merge(Interval other) {
            return new Interval(
                    Math.min(start, other.start()),
                    Math.max(end, other.end())
            );
        }
    }

    // ============================================================
    // SIMPLE BOUNDARY CONVERSION
    // ============================================================
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
            result[i][0] = intervals.get(i).start();
            result[i][1] = intervals.get(i).end();
        }

        return result;
    }

    // ============================================================
    // 1) MERGE INTERVALS
    // ============================================================
    static class MergeIntervals {

        public int[][] merge(int[][] rawIntervals) {
            if (rawIntervals.length <= 1) {
                return rawIntervals;
            }

            List<Interval> intervals = toIntervals(rawIntervals);
            intervals.sort(Comparator.comparingInt(Interval::start));

            List<Interval> result = new ArrayList<>();
            Interval active = intervals.get(0);

            for (int i = 1; i < intervals.size(); i++) {
                Interval current = intervals.get(i);

                if (current.start() <= active.end()) {
                    active = active.merge(current);   // overlap -> stretch
                } else {
                    result.add(active);               // gap -> commit
                    active = current;                 // reset
                }
            }

            result.add(active);                       // commit last
            return toArray(result);
        }
    }

    // ============================================================
    // 2) INSERT INTERVAL
    // ============================================================
    static class InsertInterval {

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

    // ============================================================
    // 3) MEETING ROOMS II
    // ============================================================
    static class MeetingRoomsII {

        public int minMeetingRooms(int[][] intervals) {
            if (intervals.length == 0) {
                return 0;
            }

            int n = intervals.length;
            int[] starts = new int[n];
            int[] ends = new int[n];

            for (int i = 0; i < n; i++) {
                starts[i] = intervals[i][0];
                ends[i] = intervals[i][1];
            }

            Arrays.sort(starts);
            Arrays.sort(ends);

            int rooms = 0;
            int endIndex = 0;

            for (int start : starts) {
                if (start < ends[endIndex]) {
                    rooms++;          // overlap -> new room
                } else {
                    endIndex++;       // room freed -> reuse
                }
            }

            return rooms;
        }
    }

    // ============================================================
    // TEST HELPERS
    // ============================================================
    static void check(String name, int[][] actual, int[][] expected) {
        if (!Arrays.deepEquals(actual, expected)) {
            throw new AssertionError(
                    name + " expected=" + Arrays.deepToString(expected)
                            + " actual=" + Arrays.deepToString(actual)
            );
        }
        System.out.println("PASS: " + name + " -> " + Arrays.deepToString(actual));
    }

    static void check(String name, int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError(name + " expected=" + expected + " actual=" + actual);
        }
        System.out.println("PASS: " + name + " -> " + actual);
    }

    // ============================================================
    // MAIN — TESTS
    // ============================================================
    public static void main(String[] args) {
        MergeIntervals merge = new MergeIntervals();

        check(
                "merge/core",
                merge.merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}}),
                new int[][]{{1, 6}, {8, 10}, {15, 18}}
        );

        check(
                "merge/all-overlap",
                merge.merge(new int[][]{{1, 4}, {2, 3}, {3, 5}}),
                new int[][]{{1, 5}}
        );

        check(
                "merge/single",
                merge.merge(new int[][]{{5, 7}}),
                new int[][]{{5, 7}}
        );

        InsertInterval insert = new InsertInterval();

        check(
                "insert/core",
                insert.insert(new int[][]{{1, 3}, {6, 9}}, new int[]{2, 5}),
                new int[][]{{1, 5}, {6, 9}}
        );

        check(
                "insert/multiple-overlaps",
                insert.insert(
                        new int[][]{{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}},
                        new int[]{4, 8}
                ),
                new int[][]{{1, 2}, {3, 10}, {12, 16}}
        );

        check(
                "insert/no-overlap-at-end",
                insert.insert(new int[][]{{1, 2}, {3, 5}}, new int[]{7, 9}),
                new int[][]{{1, 2}, {3, 5}, {7, 9}}
        );

        check(
                "insert/no-overlap-at-start",
                insert.insert(new int[][]{{3, 5}, {7, 9}}, new int[]{1, 2}),
                new int[][]{{1, 2}, {3, 5}, {7, 9}}
        );

        MeetingRoomsII rooms = new MeetingRoomsII();

        check(
                "rooms/overlap",
                rooms.minMeetingRooms(new int[][]{{0, 30}, {5, 10}, {15, 20}}),
                2
        );

        check(
                "rooms/reuse",
                rooms.minMeetingRooms(new int[][]{{7, 10}, {2, 4}}),
                1
        );
    }
}
