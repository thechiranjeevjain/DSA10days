package org.chijai.day3.session3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidAnagramTest {

    @Test
    void asciiCountingDetectsAnagramsAndMismatches() {
        assertTrue(ValidAnagram.OptimalCountingASCII.isAnagram("anagram", "nagaram"));
        assertFalse(ValidAnagram.OptimalCountingASCII.isAnagram("rat", "car"));
    }

    @Test
    void unicodeMapHandlesNonAsciiCharacters() {
        assertTrue(ValidAnagram.UnicodeHashMap.isAnagram("\u00e5b\u00e5", "b\u00e5\u00e5"));
        assertFalse(ValidAnagram.UnicodeHashMap.isAnagram("\u00e5b\u00e5", "b\u00e5a"));
    }
}
