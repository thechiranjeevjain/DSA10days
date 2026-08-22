package org.chijai.day3.session1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinimumWindowSubstringTest {

    @Test
    void minWindowTracksDuplicateRequirementsExactly() {
        MinimumWindowSubstring.Optimal solution = new MinimumWindowSubstring.Optimal();

        assertEquals("BANC", solution.minWindow("ADOBECODEBANC", "ABC"));
        assertEquals("a", solution.minWindow("a", "a"));
        assertEquals("", solution.minWindow("a", "aa"));
    }
}
