package org.chijai.patterns.binarysearch;

import org.chijai.patterns.PatternChapter;

import java.util.function.IntPredicate;

public final class BinarySearchPatternLab {
    private BinarySearchPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Binary Search",
                "Monotonic Search Space",
                "Boundary / Answer Search",
                "First True Predicate",
                "Binary Search"
        );
    }

    public static int lowerBound(int[] sorted, int target) {
        int left = 0;
        int right = sorted.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted[mid] >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    public static int firstTrue(int lowInclusive, int highInclusive, IntPredicate predicate) {
        int left = lowInclusive;
        int right = highInclusive + 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (predicate.test(mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    public static int minimumFeasible(int lowInclusive, int highInclusive, IntPredicate feasible) {
        int answer = highInclusive + 1;
        int left = lowInclusive;
        int right = highInclusive;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (feasible.test(mid)) {
                answer = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return answer;
    }
}
