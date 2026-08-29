package org.chijai.patterns.stack;

import org.chijai.patterns.PatternChapter;

import java.util.ArrayDeque;
import java.util.Deque;

public final class StackPatternLab {
    private StackPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Stack / Monotonic Stack",
                "Unresolved Candidates",
                "LIFO Or Monotonic Boundary",
                "Current Item Resolves Stack Top",
                "Valid Parentheses"
        );
    }

    public static boolean isValidParentheses(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (stack.isEmpty() || !matches(stack.pop(), c)) {
                return false;
            }
        }
        return stack.isEmpty();
    }

    private static boolean matches(char open, char close) {
        return (open == '(' && close == ')') || (open == '[' && close == ']') || (open == '{' && close == '}');
    }

    public static int[] nextGreaterToRight(int[] nums) {
        int[] answer = new int[nums.length];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = nums.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && stack.peek() <= nums[i]) {
                stack.pop();
            }
            answer[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(nums[i]);
        }
        return answer;
    }

    public static int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int best = 0;
        for (int i = 0; i <= heights.length; i++) {
            int current = i == heights.length ? 0 : heights[i];
            while (!stack.isEmpty() && current < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int left = stack.isEmpty() ? -1 : stack.peek();
                best = Math.max(best, height * (i - left - 1));
            }
            stack.push(i);
        }
        return best;
    }
}
