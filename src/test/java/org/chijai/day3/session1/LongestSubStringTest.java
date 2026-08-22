package org.chijai.day3.session1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongestSubStringTest {

    @Test
    void longestSubstringMaintainsUniqueWindow() {
        assertEquals(3, LongestSubString.Optimal_SeenArray.lengthOfLongestSubstring("abcabcbb"));
        assertEquals(1, LongestSubString.Optimal_SeenArray.lengthOfLongestSubstring("bbbbb"));
        assertEquals(3, LongestSubString.Optimal_SeenArray.lengthOfLongestSubstring("pwwkew"));
    }
}
