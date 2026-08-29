package org.chijai.patterns.prefixsuffix;

import org.chijai.patterns.PatternChapter;

import java.util.HashMap;
import java.util.Map;

public final class PrefixSuffixPatternLab {
    private PrefixSuffixPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Prefix Sum / Prefix-Suffix",
                "Repeated Range Aggregate",
                "Prefix State",
                "Range Difference",
                "Product Of Array Except Self"
        );
    }

    public static int[] productExceptSelf(int[] nums) {
        int[] result = new int[nums.length];
        int prefix = 1;
        for (int i = 0; i < nums.length; i++) {
            result[i] = prefix;
            prefix *= nums[i];
        }
        int suffix = 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            result[i] *= suffix;
            suffix *= nums[i];
        }
        return result;
    }

    public static int rangeSum(int[] prefix, int leftInclusive, int rightInclusive) {
        return prefix[rightInclusive + 1] - prefix[leftInclusive];
    }

    public static int[] prefixSums(int[] nums) {
        int[] prefix = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        return prefix;
    }

    public static int subarraySumEqualsK(int[] nums, int k) {
        Map<Integer, Integer> seen = new HashMap<>();
        seen.put(0, 1);
        int prefix = 0;
        int count = 0;
        for (int num : nums) {
            prefix += num;
            count += seen.getOrDefault(prefix - k, 0);
            seen.merge(prefix, 1, Integer::sum);
        }
        return count;
    }
}
