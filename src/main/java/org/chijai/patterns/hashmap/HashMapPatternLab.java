package org.chijai.patterns.hashmap;

import org.chijai.patterns.PatternChapter;

import java.util.HashMap;
import java.util.Map;

public final class HashMapPatternLab {
    private HashMapPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "HashMap / Frequency / Set",
                "Fast Lookup / Counting",
                "Complement And Frequency",
                "Processed State Cache",
                "Two Sum"
        );
    }

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> indexByValue = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            if (indexByValue.containsKey(need)) {
                return new int[]{indexByValue.get(need), i};
            }
            indexByValue.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    public static boolean isAnagram(String left, String right) {
        if (left.length() != right.length()) {
            return false;
        }
        int[] counts = new int[26];
        for (int i = 0; i < left.length(); i++) {
            counts[left.charAt(i) - 'a']++;
            counts[right.charAt(i) - 'a']--;
        }
        for (int count : counts) {
            if (count != 0) {
                return false;
            }
        }
        return true;
    }

    public static int longestPalindromeLength(String s) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : s.toCharArray()) {
            counts.merge(c, 1, Integer::sum);
        }
        int length = 0;
        boolean hasOdd = false;
        for (int count : counts.values()) {
            length += (count / 2) * 2;
            hasOdd |= (count % 2 == 1);
        }
        return hasOdd ? length + 1 : length;
    }
}
