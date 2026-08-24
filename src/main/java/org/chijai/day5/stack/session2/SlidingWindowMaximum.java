package org.chijai.day5.stack.session2;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 239 - Sliding Window Maximum.
 *
 * <p>The deque stores indices whose values are in decreasing order. Its front
 * is therefore the maximum for the current window. Each index is added and
 * removed at most once, giving O(n) time and O(k) space.</p>
 */
public class SlidingWindowMaximum {

    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
            return new int[0];
        }

        int[] result = new int[nums.length - k + 1];
        Deque<Integer> decreasingIndices = new ArrayDeque<>();

        for (int right = 0; right < nums.length; right++) {
            while (!decreasingIndices.isEmpty()
                    && decreasingIndices.peekFirst() <= right - k) {
                decreasingIndices.pollFirst();
            }

            while (!decreasingIndices.isEmpty()
                    && nums[decreasingIndices.peekLast()] <= nums[right]) {
                decreasingIndices.pollLast();
            }

            decreasingIndices.addLast(right);

            if (right >= k - 1) {
                result[right - k + 1] = nums[decreasingIndices.peekFirst()];
            }
        }

        return result;
    }
}
