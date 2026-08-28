package org.chijai.patterns.twopointers;

import org.chijai.patterns.PatternChapter;

import java.util.Arrays;

public final class TwoPointersPatternLab {
    private TwoPointersPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Two Pointers",
                "Pair / Ends / Sorted",
                "Opposite Ends",
                "Shrink Search Space",
                "Two Sum II"
        );
    }

    public static int[] twoSumSortedZeroBased(int[] sorted, int target) {
        int left = 0;
        int right = sorted.length - 1;
        while (left < right) {
            int sum = sorted[left] + sorted[right];
            if (sum == target) {
                return new int[]{left, right};
            }
            if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return new int[]{-1, -1};
    }

    public static boolean isPalindromeIgnoringNonAlphanumeric(String s) {
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static int[] moveZeroesStableCopy(int[] nums) {
        int[] copy = Arrays.copyOf(nums, nums.length);
        int write = 0;
        for (int value : copy) {
            if (value != 0) {
                copy[write++] = value;
            }
        }
        while (write < copy.length) {
            copy[write++] = 0;
        }
        return copy;
    }
}
