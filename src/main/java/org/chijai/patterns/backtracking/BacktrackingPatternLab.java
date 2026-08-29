package org.chijai.patterns.backtracking;

import org.chijai.patterns.PatternChapter;

import java.util.ArrayList;
import java.util.List;

public final class BacktrackingPatternLab {
    private BacktrackingPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Backtracking / Combinatorial DFS",
                "Generate / Try / Undo",
                "Decision Tree",
                "Path Is Current State",
                "Subsets"
        );
    }

    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        collectSubsets(0, nums, new ArrayList<>(), result);
        return result;
    }

    private static void collectSubsets(int index, int[] nums, List<Integer> path, List<List<Integer>> result) {
        if (index == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        collectSubsets(index + 1, nums, path, result);
        path.add(nums[index]);
        collectSubsets(index + 1, nums, path, result);
        path.remove(path.size() - 1);
    }

    public static List<String> letterCombinations(String digits) {
        if (digits.isEmpty()) {
            return List.of();
        }
        String[] map = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        List<String> result = new ArrayList<>();
        collectLetters(0, digits, map, new StringBuilder(), result);
        return result;
    }

    private static void collectLetters(int index, String digits, String[] map, StringBuilder path, List<String> result) {
        if (index == digits.length()) {
            result.add(path.toString());
            return;
        }
        for (char c : map[digits.charAt(index) - '0'].toCharArray()) {
            path.append(c);
            collectLetters(index + 1, digits, map, path, result);
            path.deleteCharAt(path.length() - 1);
        }
    }
}
