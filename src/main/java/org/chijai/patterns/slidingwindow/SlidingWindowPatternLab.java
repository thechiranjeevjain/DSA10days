package org.chijai.patterns.slidingwindow;

import org.chijai.patterns.PatternChapter;

import java.util.HashMap;
import java.util.Map;

public final class SlidingWindowPatternLab {
    private SlidingWindowPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Sliding Window",
                "Contiguous Array / String",
                "Variable Window",
                "Maintain Valid Window",
                "Longest Substring Without Repeating Characters"
        );
    }

    public static int longestAtMostKDistinct(String s, int k) {
        if (k <= 0 || s.isEmpty()) {
            return 0;
        }

        Map<Character, Integer> frequency = new HashMap<>();
        int left = 0;
        int best = 0;
        for (int right = 0; right < s.length(); right++) {
            char in = s.charAt(right);
            frequency.merge(in, 1, Integer::sum);

            while (frequency.size() > k) {
                char out = s.charAt(left++);
                int next = frequency.get(out) - 1;
                if (next == 0) {
                    frequency.remove(out);
                } else {
                    frequency.put(out, next);
                }
            }

            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static int minLengthSubarrayAtLeastTarget(int target, int[] nums) {
        int left = 0;
        int sum = 0;
        int best = Integer.MAX_VALUE;
        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum >= target) {
                best = Math.min(best, right - left + 1);
                sum -= nums[left++];
            }
        }
        return best == Integer.MAX_VALUE ? 0 : best;
    }

    public static int countFixedWindowAnagrams(String text, String pattern) {
        if (pattern.length() > text.length()) {
            return 0;
        }

        int[] diff = new int[26];
        for (int i = 0; i < pattern.length(); i++) {
            diff[pattern.charAt(i) - 'a']++;
            diff[text.charAt(i) - 'a']--;
        }

        int matches = isZero(diff) ? 1 : 0;
        for (int right = pattern.length(); right < text.length(); right++) {
            diff[text.charAt(right) - 'a']--;
            diff[text.charAt(right - pattern.length()) - 'a']++;
            if (isZero(diff)) {
                matches++;
            }
        }
        return matches;
    }

    private static boolean isZero(int[] values) {
        for (int value : values) {
            if (value != 0) {
                return false;
            }
        }
        return true;
    }
}
