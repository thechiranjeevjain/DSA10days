package org.chijai.day10.session2;

/**
 * LeetCode 268 - Missing Number.
 *
 * <p>XOR starts with n and combines every valid index and array value.
 * Values that appear in both sets cancel, leaving the one missing value.</p>
 */
public class MissingNumber {

    public int missingNumber(int[] nums) {
        int missing = nums.length;

        for (int index = 0; index < nums.length; index++) {
            missing ^= index;
            missing ^= nums[index];
        }

        return missing;
    }
}
