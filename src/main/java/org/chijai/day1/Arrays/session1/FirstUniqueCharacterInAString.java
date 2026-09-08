package org.chijai.day1.Arrays.session1;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 387 — First Unique Character in a String
 *
 * Primary Pattern:
 *     Frequency Counting + Two-Pass Ordered Scan
 */
public class FirstUniqueCharacterInAString {

    /**
     * ==============================================================
     * PRIMARY INTERVIEW SOLUTION 1 — int[26]
     * ==============================================================
     *
     * Best choice for this exact problem because input contains only
     * lowercase English letters.
     *
     * Time:  O(n)
     * Space: O(1)
     */
    public static int firstUniqChar(String s) {

        int[] frequency = new int[26];

        for (int i = 0; i < s.length(); i++) {
            frequency[s.charAt(i) - 'a']++;
        }

        for (int i = 0; i < s.length(); i++) {
            if (frequency[s.charAt(i) - 'a'] == 1) {
                return i;
            }
        }

        return -1;
    }


    /**
     * ==============================================================
     * PRIMARY INTERVIEW SOLUTION 2 — HashMap
     * ==============================================================
     *
     * Generalized version when the character set is not restricted
     * to a fixed small alphabet.
     *
     * Time:  O(n)
     * Space: O(k), k = distinct characters
     */
    public static int firstUniqCharUsingMap(String s) {

        Map<Character, Integer> frequency = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);

            frequency.put(
                    current,
                    frequency.getOrDefault(current, 0) + 1);
        }

        for (int i = 0; i < s.length(); i++) {
            if (frequency.get(s.charAt(i)) == 1) {
                return i;
            }
        }

        return -1;
    }


    /**
     * ==============================================================
     * First-Principles Invention Path
     * ==============================================================
     *
     * "Unique" depends on TOTAL frequency.
     *
     * At an early index, seeing a character once so far is not enough:
     * it may appear again later.
     *
     * Therefore:
     *
     *     1. Count every character globally.
     *     2. Scan the original string from left to right.
     *     3. Return the first index whose total count is 1.
     *
     * Why the second scan?
     *
     *     The frequency structure answers "is it unique?"
     *     The original string answers "which unique one came first?"
     */


    /**
     * ==============================================================
     * Pattern Classification
     * ==============================================================
     *
     * Category:
     *     String / Hashing
     *
     * Primary pattern:
     *     Frequency Counting
     *
     * Supporting pattern:
     *     Two-Pass Ordered Scan
     *
     * Recognition:
     *
     *     GLOBAL PROPERTY + FIRST/LAST IN ORIGINAL ORDER
     *
     * Reusable skeleton:
     *
     *     build global metadata
     *
     *     scan original order:
     *         if metadata says valid:
     *             return
     */


    /**
     * ==============================================================
     * Correctness Contract
     * ==============================================================
     *
     * After pass 1:
     *
     *     frequency[c] = total occurrences of c in the entire string.
     *
     * Pass 2 examines indices in increasing order.
     *
     * Therefore the first index whose character has frequency 1
     * is exactly the first unique character.
     */


    /**
     * ==============================================================
     * Choosing int[26] vs HashMap
     * ==============================================================
     *
     * Fixed small alphabet:
     *
     *     int[26]
     *
     *     simpler
     *     constant space
     *     direct indexing
     *
     * General / unknown character set:
     *
     *     HashMap<Character, Integer>
     *
     * Same algorithm.
     * Only the frequency-storage mechanism changes.
     */


    /**
     * ==============================================================
     * Common Traps
     * ==============================================================
     *
     * 1. First occurrence != first unique character.
     *
     *        "aab"
     *
     *    'a' is first, but not unique.
     *
     * 2. Do not iterate over a normal HashMap to determine "first".
     *    Scan the original string because string order matters.
     *
     * 3. Repeated indexOf / lastIndexOf calls can make the solution
     *    O(n^2).
     */


    /**
     * ==============================================================
     * Interview Articulation
     * ==============================================================
     *
     * "Uniqueness depends on total frequency, so I first count all
     * characters. Then I scan the string again from left to right
     * and return the first index whose count is one.
     *
     * Since this problem guarantees lowercase English letters,
     * I can use int[26]. If the character set were unrestricted,
     * I would use a HashMap."
     */


    /**
     * ==============================================================
     * Recall Card
     * ==============================================================
     *
     * FIRST UNIQUE
     *
     *     count globally
     *     scan original order
     *     first count == 1
     *
     * Fixed alphabet -> int[]
     * General input  -> HashMap
     *
     * O(n)
     */


    /**
     * ==============================================================
     * Related / Transfer
     * ==============================================================
     *
     * Valid Anagram
     * Ransom Note
     * Find Common Characters
     * Sort Characters By Frequency
     *
     * Transfer rule:
     *
     *     If validity needs information from the whole input,
     *     compute that information first, then make the ordered choice.
     */


    public static void main(String[] args) {

        assertEquals(0, firstUniqChar("leetcode"));
        assertEquals(2, firstUniqChar("loveleetcode"));
        assertEquals(-1, firstUniqChar("aabb"));
        assertEquals(2, firstUniqChar("aab"));

        assertEquals(0, firstUniqCharUsingMap("leetcode"));
        assertEquals(2, firstUniqCharUsingMap("loveleetcode"));
        assertEquals(-1, firstUniqCharUsingMap("aabb"));
        assertEquals(2, firstUniqCharUsingMap("aab"));

        System.out.println("All tests passed.");
    }

    private static void assertEquals(int expected, int actual) {

        if (expected != actual) {
            throw new AssertionError(
                    "Expected " + expected + ", but got " + actual);
        }
    }
}
