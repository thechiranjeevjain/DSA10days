package org.chijai.day3.session3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * LEETCODE 49 — GROUP ANAGRAMS
 *
 * Problem:
 * Given an array of strings strs, group the anagrams together.
 * You can return the answer in any order.
 *
 * An anagram is a word formed by rearranging all the letters of another word.
 *
 * Example 1:
 *
 * Input:
 *     ["eat", "tea", "tan", "ate", "nat", "bat"]
 *
 * Output:
 *     [
 *         ["eat", "tea", "ate"],
 *         ["tan", "nat"],
 *         ["bat"]
 *     ]
 *
 * Example 2:
 *
 * Input:
 *     [""]
 *
 * Output:
 *     [[""]]
 *
 * Example 3:
 *
 * Input:
 *     ["a"]
 *
 * Output:
 *     [["a"]]
 *
 * ------------------------------------------------------------
 * CLASSIFICATION
 * ------------------------------------------------------------
 *
 * HashMap
 * Grouping
 * Canonical representation / canonical key
 * Strings
 * Sorting
 *
 * ------------------------------------------------------------
 * FIRST-PRINCIPLES INVENTION PATH
 * ------------------------------------------------------------
 *
 * We need to know which words belong to the same anagram group.
 *
 * Comparing every word with every other word would be unnecessary.
 * Instead, find a representation that is identical for all anagrams.
 *
 * Example:
 *
 *     eat -> aet
 *     tea -> aet
 *     ate -> aet
 *
 *     tan -> ant
 *     nat -> ant
 *
 * Sorting removes the original ordering of the letters.
 * Therefore:
 *
 *     two words are anagrams
 *              iff
 *     their sorted strings are equal.
 *
 * The sorted string becomes the HashMap key.
 *
 *     "aet" -> ["eat", "tea", "ate"]
 *     "ant" -> ["tan", "nat"]
 *     "abt" -> ["bat"]
 *
 * Once that key is discovered, the rest is ordinary HashMap grouping.
 */
public class GroupAnagrams {

    /*
     * PRIMARY SOLUTION — SORT EACH WORD
     *
     * This is the version to remember first.
     * It is simple to reconstruct because the idea follows directly
     * from the definition of an anagram.
     */
    public static List<List<String>> groupAnagrams(String[] strs) {

        Map<String, List<String>> groups = new HashMap<>();

        for (String word : strs) {

            char[] chars = word.toCharArray();
            Arrays.sort(chars);

            String key = new String(chars);

            groups.putIfAbsent(key, new ArrayList<>());
            groups.get(key).add(word);
        }

        return new ArrayList<>(groups.values());
    }

    /*
     * DRY RUN
     *
     * Input:
     *
     *     ["eat", "tea", "tan", "ate", "nat", "bat"]
     *
     * --------------------------------------------------
     * word = "eat"
     *
     * chars = ['e', 'a', 't']
     * sort
     * chars = ['a', 'e', 't']
     * key = "aet"
     *
     * groups:
     *     "aet" -> ["eat"]
     *
     * --------------------------------------------------
     * word = "tea"
     *
     * sorted key = "aet"
     *
     * groups:
     *     "aet" -> ["eat", "tea"]
     *
     * --------------------------------------------------
     * word = "tan"
     *
     * sorted key = "ant"
     *
     * groups:
     *     "aet" -> ["eat", "tea"]
     *     "ant" -> ["tan"]
     *
     * --------------------------------------------------
     * word = "ate"
     *
     * sorted key = "aet"
     *
     * groups:
     *     "aet" -> ["eat", "tea", "ate"]
     *     "ant" -> ["tan"]
     *
     * --------------------------------------------------
     * word = "nat"
     *
     * sorted key = "ant"
     *
     * groups:
     *     "aet" -> ["eat", "tea", "ate"]
     *     "ant" -> ["tan", "nat"]
     *
     * --------------------------------------------------
     * word = "bat"
     *
     * sorted key = "abt"
     *
     * groups:
     *     "aet" -> ["eat", "tea", "ate"]
     *     "ant" -> ["tan", "nat"]
     *     "abt" -> ["bat"]
     *
     * Finally return all HashMap values.
     */

    /*
     * TIME AND SPACE COMPLEXITY
     *
     * Let:
     *
     *     n = number of strings
     *     k = maximum / average length of a string
     *
     * For each word:
     *
     *     converting to char[] = O(k)
     *     sorting chars        = O(k log k)
     *     creating key         = O(k)
     *
     * Sorting dominates.
     *
     * Therefore:
     *
     *     Time = O(n * k log k)
     *
     * The result and HashMap keys together store characters proportional
     * to the total input size.
     *
     *     Space = O(n * k)
     *
     * Ignoring the returned output itself, the additional key storage
     * is still O(n * k) in the worst case.
     */

    /*
     * SECONDARY SOLUTION — CHARACTER FREQUENCY KEY
     *
     * LeetCode constrains words to lowercase English letters.
     * So instead of sorting, we can count the 26 letters.
     *
     * Example:
     *
     *     "eat" and "tea"
     *
     * both have:
     *
     *     a = 1
     *     e = 1
     *     t = 1
     *
     * and every other letter = 0.
     *
     * Their frequency signature is therefore identical.
     *
     * Time:
     *     O(n * k)
     *
     * This is asymptotically faster than sorting, but the sorting solution
     * is the easier primary interview solution to reinvent from scratch.
     */
    public static List<List<String>> groupAnagramsByFrequency(String[] strs) {

        Map<String, List<String>> groups = new HashMap<>();

        for (String word : strs) {

            int[] count = new int[26];

            for (char ch : word.toCharArray()) {
                count[ch - 'a']++;
            }

            StringBuilder keyBuilder = new StringBuilder();

            for (int frequency : count) {
                keyBuilder.append('#');
                keyBuilder.append(frequency);
            }

            String key = keyBuilder.toString();

            groups.putIfAbsent(key, new ArrayList<>());
            groups.get(key).add(word);
        }

        return new ArrayList<>(groups.values());
    }
}
