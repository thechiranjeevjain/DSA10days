package org.chijai.patterns.intervalsgreedy;

import org.chijai.patterns.PatternChapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class IntervalsGreedyPatternLab {
    private IntervalsGreedyPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Intervals / Sorting Greedy",
                "Sort To Make Conflict Local",
                "Merge / Sweep / Safe Endpoint",
                "Current End Encodes Active Choice",
                "Merge Intervals"
        );
    }

    public static int[][] merge(int[][] intervals) {
        if (intervals.length == 0) {
            return new int[0][0];
        }
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> merged = new ArrayList<>();
        int[] current = Arrays.copyOf(intervals[0], 2);
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= current[1]) {
                current[1] = Math.max(current[1], intervals[i][1]);
            } else {
                merged.add(current);
                current = Arrays.copyOf(intervals[i], 2);
            }
        }
        merged.add(current);
        return merged.toArray(new int[0][]);
    }

    public static int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));
        int removed = 0;
        int end = Integer.MIN_VALUE;
        for (int[] interval : intervals) {
            if (interval[0] < end) {
                removed++;
            } else {
                end = interval[1];
            }
        }
        return removed;
    }

    public static List<Integer> partitionLabels(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) {
            last[s.charAt(i) - 'a'] = i;
        }
        List<Integer> result = new ArrayList<>();
        int start = 0;
        int end = 0;
        for (int i = 0; i < s.length(); i++) {
            end = Math.max(end, last[s.charAt(i) - 'a']);
            if (i == end) {
                result.add(end - start + 1);
                start = i + 1;
            }
        }
        return result;
    }
}
