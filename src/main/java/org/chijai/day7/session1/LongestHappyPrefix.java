package org.chijai.day7.session1;

import java.util.*;

/**
 * ================================================================
 * 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
 * ================================================================
 *
 * 🔗 Link:
 * https://leetcode.com/problems/longest-happy-prefix/
 *
 * Difficulty: Hard
 * Tags: String, KMP, Rolling Hash, String Matching
 *
 * A string is called a happy prefix if it is a non-empty prefix which is also a suffix (excluding itself).
 *
 * Given a string s, return the longest happy prefix of s.
 * Return an empty string "" if no such prefix exists.
 *
 *
 * Example 1:
 * Input: s = "level"
 * Output: "l"
 * Explanation:
 * s contains 4 prefix excluding itself ("l", "le", "lev", "leve"),
 * and suffix ("l", "el", "vel", "evel").
 * The largest prefix which is also suffix is given by "l".
 *
 * Example 2:
 * Input: s = "ababab"
 * Output: "abab"
 * Explanation:
 * "abab" is the largest prefix which is also suffix.
 * They can overlap in the original string.
 *
 *
 * Constraints:
 * 1 <= s.length <= 10^5
 * s contains only lowercase English letters.
 *
 *
 * ================================================================
 * 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
 * ================================================================
 *
 * 🔵 Pattern Name:
 * Prefix-Suffix Matching using KMP (LPS / Prefix Function)
 *
 * 🔵 Problem Archetype:
 * Find longest prefix == suffix (excluding full string)
 *
 * 🟢 Core Invariant:
 * At every index i, LPS[i] stores the length of the longest proper prefix
 * which is also a suffix for substring s[0..i]
 *
 * 🟡 Why this invariant works:
 * Instead of recomputing prefix-suffix matches repeatedly,
 * we reuse previously computed structure via failure links.
 *
 * 🔵 When this pattern applies:
 * • Prefix == suffix problems
 * • Repeated pattern detection
 * • String periodicity
 * • Pattern matching (KMP core)
 *
 * 🔵 Pattern recognition signals:
 * • "prefix equals suffix"
 * • "longest border"
 * • "overlapping allowed"
 * • repeated pattern hints
 *
 * 🔵 Difference from similar patterns:
 * • NOT sliding window → no window invariant
 * • NOT hashing (though possible alternative)
 * • This is deterministic prefix-structure reuse
 *
 *
 * ================================================================
 * 🟢 MENTAL MODEL & INVARIANTS (CANONICAL SOURCE OF TRUTH)
 * ================================================================
 *
 * 🧠 Mental Model:
 *
 * Imagine building the string from left to right.
 * At each position, we ask:
 * "What is the longest prefix that also matches a suffix ending here?"
 *
 * Instead of restarting from scratch when mismatch occurs,
 * we "fall back" using previous LPS values.
 *
 *
 * 🟢 Core Invariants:
 *
 * 1. LPS[i] = length of longest proper prefix == suffix for s[0..i]
 *
 * 2. Proper prefix means:
 *    prefix length < full substring length
 *
 * 3. LPS array builds progressively using:
 *    previous knowledge (LPS[i-1])
 *
 *
 * 🟢 State Variables:
 *
 * i → current index being processed
 * len → length of current matching prefix-suffix
 *
 *
 * 🟢 Allowed Moves:
 *
 * • If s[i] == s[len]:
 *      extend match → len++
 *
 * • If mismatch:
 *      fallback → len = LPS[len - 1]
 *
 *
 * 🟢 Forbidden Moves:
 *
 * ❌ Restart matching from scratch unnecessarily
 * ❌ Compare all prefixes manually (O(n²))
 *
 *
 * 🟢 Termination Logic:
 *
 * Final answer = substring(0, LPS[n-1])
 *
 *
 * 🟡 Why alternatives fail:
 *
 * • Brute force → O(n²)
 * • Hashing → collision risk + complexity
 * • Sliding window → no valid invariant
 *
 *
 * ================================================================
 * 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
 * ================================================================
 *
 * ❌ Wrong Approach 1: Check all prefix-suffix pairs
 *
 * for len from n-1 to 1:
 *     if prefix == suffix:
 *         return prefix
 *
 * 🔴 Why it seems correct:
 * • Directly matches problem statement
 *
 * 🔴 Why it fails:
 * • O(n²) comparisons
 * • Each comparison is O(n)
 *
 * 🔴 Violated Invariant:
 * • Does NOT reuse prefix knowledge
 *
 *
 * ❌ Wrong Approach 2: Restart matching on mismatch
 *
 * 🔴 Why it seems correct:
 * • Logical brute retry
 *
 * 🔴 Why it fails:
 * • Ignores failure links (KMP optimization)
 *
 *
 * ❌ Minimal Counterexample:
 *
 * s = "abababca"
 *
 * Repeated fallback without LPS → exponential retries
 *
 *
 * 🔴 Interview Trap:
 *
 * Interviewer asks:
 * "Can you do better than O(n²)?"
 *
 * If you don't know LPS → you're stuck
 *
 *
 * ================================================================
 * PRIMARY PROBLEM — SOLUTION CLASSES (DERIVED FROM INVARIANT)
 * ================================================================
 */

public class LongestHappyPrefix {

    /**
     * ================================================================
     * 🟠 BRUTE FORCE SOLUTION
     * ================================================================
     *
     * 🟡 Core Idea:
     * Try all prefix lengths from largest to smallest
     * and check if prefix == suffix
     *
     * 🟢 Enforced Invariant:
     * Direct comparison (no reuse)
     *
     * 🔴 Limitation:
     * O(n²)
     *
     * 🟣 Interview Preference:
     * Only as baseline
     */
    static class BruteForce {
        public String longestPrefix(String s) {
            int n = s.length();

            for (int len = n - 1; len >= 1; len--) {
                String prefix = s.substring(0, len);
                String suffix = s.substring(n - len);

                if (prefix.equals(suffix)) {
                    return prefix;
                }
            }

            return "";
        }
    }

    /**
     * ================================================================
     * 🟠 IMPROVED SOLUTION (Rolling Hash)
     * ================================================================
     *
     * 🟡 Core Idea:
     * Compare prefix and suffix hashes incrementally
     *
     * 🟢 Enforced Invariant:
     * Hash(prefix) == Hash(suffix)
     *
     * 🔴 Limitation:
     * • Collision risk
     * • More complex
     *
     * 🟣 Interview Preference:
     * Acceptable but not ideal
     */
    static class RollingHash {
        public String longestPrefix(String s) {
            long prefixHash = 0;
            long suffixHash = 0;
            long base = 31;
            long mod = 1_000_000_007;
            long power = 1;

            int res = 0;

            for (int i = 0; i < s.length() - 1; i++) {
                int left = s.charAt(i) - 'a' + 1;
                int right = s.charAt(s.length() - 1 - i) - 'a' + 1;

                prefixHash = (prefixHash * base + left) % mod;
                suffixHash = (suffixHash + power * right) % mod;

                if (prefixHash == suffixHash) {
                    res = i + 1;
                }

                power = (power * base) % mod;
            }

            return s.substring(0, res);
        }
    }

    /**
     * ================================================================
     * 🟠 OPTIMAL SOLUTION (KMP — LPS ARRAY)
     * ================================================================
     *
     * 🟡 Core Idea:
     * Build LPS array using prefix-suffix invariant
     *
     * 🟢 Enforced Invariant:
     * LPS[i] = longest prefix == suffix for s[0..i]
     *
     * 🔴 Fixes:
     * • Eliminates recomputation
     * • Guarantees O(n)
     *
     * 🟣 Interview Preference:
     * ✅ BEST ANSWER
     */
    static class OptimalKMP {

        public String longestPrefix(String s) {
            int n = s.length();
            int[] lps = new int[n];

            int len = 0; // length of previous longest prefix suffix
            int i = 1;

            while (i < n) {
                if (s.charAt(i) == s.charAt(len)) {
                    len++;
                    lps[i] = len;
                    i++;
                } else {
                    if (len != 0) {
                        // fallback using invariant
                        len = lps[len - 1];
                    } else {
                        lps[i] = 0;
                        i++;
                    }
                }
            }

            int longest = lps[n - 1];
            return s.substring(0, longest);
        }
    }

/**
 * ================================================================
 * 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
 * ================================================================
 *
 * 🟣 How to explain this in an interview:
 *
 * 1. 🟢 State the invariant clearly:
 *
 * "For every index i, we maintain LPS[i] which represents
 * the length of the longest proper prefix which is also a suffix
 * for substring s[0..i]."
 *
 *
 * 2. 🟡 Explain the discard / fallback logic:
 *
 * "When a mismatch occurs, instead of restarting from scratch,
 * we fall back to LPS[len - 1], which represents the next best
 * candidate prefix length."
 *
 *
 * 3. 🟢 Why correctness is guaranteed:
 *
 * • We never miss any valid prefix-suffix match
 * • Every fallback preserves the invariant
 * • We only reuse previously validated prefix structures
 *
 *
 * 4. 🔴 What breaks if changed:
 *
 * ❌ If we reset len = 0 on mismatch:
 *     → we lose optimal substructure reuse → O(n²)
 *
 * ❌ If we allow full string:
 *     → violates "proper prefix" constraint
 *
 *
 * 5. 🟡 In-place / streaming feasibility:
 *
 * • Can be done in O(n) space
 * • Streaming possible with rolling prefix tracking
 *
 *
 * 6. 🟣 When NOT to use this pattern:
 *
 * ❌ When substring is not prefix-based
 * ❌ When order doesn't matter
 * ❌ When matching is non-contiguous
 *
 *
 * ================================================================
 * 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
 * ================================================================
 *
 * 🔵 Invariant-Preserving Changes:
 *
 * • Find shortest period of string:
 *   period = n - LPS[n-1]
 *
 * • Check repeated pattern:
 *   if n % (n - LPS[n-1]) == 0 → repeat exists
 *
 *
 * 🔵 Reasoning-Only Changes:
 *
 * • Instead of returning substring,
 *   return length of border
 *
 * • Extend to pattern matching (classic KMP)
 *
 *
 * 🔴 Pattern-Break Signals:
 *
 * • Need substring anywhere → not prefix-based
 * • Need maximum frequency → use hashmap
 * • Need window → use sliding window
 *
 *
 * ⚫ Pattern Mapping:
 *
 * prefix == suffix → KMP
 * substring search → KMP / Z-algorithm
 * periodicity → LPS
 */


    /**
     * ================================================================
     * ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
     * ================================================================
     *
     * ================================================================
     * 🧩 PROBLEM 1: Repeated Substring Pattern
     * ================================================================
     *
     * 🔗 https://leetcode.com/problems/repeated-substring-pattern/
     * Difficulty: Easy
     * Tags: String, KMP
     *
     * 📘 Problem Statement:
     *
     * Given a string s, check if it can be constructed by taking a substring
     * of it and appending multiple copies of the substring together.
     *
     *
     * Example 1:
     * Input: s = "abab"
     * Output: true
     * Explanation: It is the substring "ab" twice.
     *
     * Example 2:
     * Input: s = "aba"
     * Output: false
     *
     * Example 3:
     * Input: s = "abcabcabcabc"
     * Output: true
     * Explanation: It is the substring "abc" four times.
     *
     *
     * Constraints:
     * 1 <= s.length <= 10^4
     * s consists of lowercase English letters.
     *
     *
     * 🟢 Invariant Mapping:
     *
     * LPS[n-1] gives longest prefix == suffix
     *
     * If string is made of repeated pattern:
     * n % (n - LPS[n-1]) == 0
     *
     *
     * 🟡 Reasoning:
     *
     * If a pattern repeats, then prefix-suffix structure encodes periodicity
     *
     *
     * 🔴 Edge Cases:
     *
     * • "a" → false
     * • "aaaa" → true
     *
     *
     * 🟣 Interview Articulation:
     *
     * "If the string has a repeating unit, then the leftover after removing
     * the longest prefix-suffix must divide the full length exactly."
     */
    static class RepeatedSubstringPattern {

        public boolean repeatedSubstringPattern(String s) {
            int n = s.length();
            int[] lps = buildLPS(s);

            int len = lps[n - 1];

            return len > 0 && n % (n - len) == 0;
        }

        private int[] buildLPS(String s) {
            int n = s.length();
            int[] lps = new int[n];

            int len = 0;
            int i = 1;

            while (i < n) {
                if (s.charAt(i) == s.charAt(len)) {
                    lps[i++] = ++len;
                } else {
                    if (len != 0) {
                        len = lps[len - 1];
                    } else {
                        lps[i++] = 0;
                    }
                }
            }

            return lps;
        }
    }


    /**
     * ================================================================
     * 🧩 PROBLEM 2: Implement strStr() (KMP)
     * ================================================================
     *
     * 🔗 https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/
     * Difficulty: Easy
     * Tags: String, KMP
     *
     * 📘 Problem Statement:
     *
     * Given two strings needle and haystack,
     * return the index of the first occurrence of needle in haystack,
     * or -1 if needle is not part of haystack.
     *
     *
     * Example 1:
     * Input: haystack = "sadbutsad", needle = "sad"
     * Output: 0
     *
     * Example 2:
     * Input: haystack = "leetcode", needle = "leeto"
     * Output: -1
     *
     *
     * Constraints:
     * 1 <= haystack.length, needle.length <= 10^4
     * haystack and needle consist of lowercase English characters.
     *
     *
     * 🟢 Invariant Mapping:
     *
     * LPS enables skipping redundant comparisons
     *
     *
     * 🟡 Reasoning:
     *
     * When mismatch occurs, fallback using LPS instead of restarting
     *
     *
     * 🔴 Edge Cases:
     *
     * • needle longer than haystack
     * • identical strings
     *
     *
     * 🟣 Interview Articulation:
     *
     * "We preprocess the pattern to build LPS so that we avoid
     * redundant comparisons during matching."
     */
    static class KMPStrStr {

        public int strStr(String haystack, String needle) {
            if (needle.length() == 0) return 0;

            int[] lps = buildLPS(needle);

            int i = 0; // haystack
            int j = 0; // needle

            while (i < haystack.length()) {
                if (haystack.charAt(i) == needle.charAt(j)) {
                    i++;
                    j++;

                    if (j == needle.length()) {
                        return i - j;
                    }
                } else {
                    if (j != 0) {
                        j = lps[j - 1];
                    } else {
                        i++;
                    }
                }
            }

            return -1;
        }

        private int[] buildLPS(String s) {
            int n = s.length();
            int[] lps = new int[n];

            int len = 0;
            int i = 1;

            while (i < n) {
                if (s.charAt(i) == s.charAt(len)) {
                    lps[i++] = ++len;
                } else {
                    if (len != 0) {
                        len = lps[len - 1];
                    } else {
                        lps[i++] = 0;
                    }
                }
            }

            return lps;
        }
    }

    /**
     * ================================================================
     * 🧩 PROBLEM 3: Longest Prefix Also Suffix (GFG variant)
     * ================================================================
     *
     * 📘 Problem Statement:
     *
     * Given a string s, find the length of the longest proper prefix
     * which is also a suffix.
     *
     * NOTE: Proper prefix means prefix length < full string.
     *
     *
     * Example 1:
     * Input: "abab"
     * Output: 2
     *
     * Example 2:
     * Input: "aaaa"
     * Output: 3
     *
     *
     * Constraints:
     * 1 <= s.length <= 10^5
     *
     *
     * 🟢 Invariant Mapping:
     *
     * Direct usage of:
     * LPS[n-1]
     *
     *
     * 🟡 Reasoning:
     *
     * The LPS last value directly encodes longest prefix == suffix
     *
     *
     * 🔴 Edge Cases:
     *
     * • "a" → 0
     * • "abc" → 0
     *
     *
     * 🟣 Interview Articulation:
     *
     * "We don't need to recompute anything — LPS already stores this."
     */
    static class LongestPrefixSuffixLength {

        public int longestPrefixSuffix(String s) {
            int[] lps = buildLPS(s);
            return lps[s.length() - 1];
        }

        private int[] buildLPS(String s) {
            int n = s.length();
            int[] lps = new int[n];

            int len = 0, i = 1;

            while (i < n) {
                if (s.charAt(i) == s.charAt(len)) {
                    lps[i++] = ++len;
                } else {
                    if (len != 0) {
                        len = lps[len - 1];
                    } else {
                        lps[i++] = 0;
                    }
                }
            }
            return lps;
        }
    }


    /**
     * ================================================================
     * 🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
     * ================================================================
     *
     * ================================================================
     * 🧩 RELATED 1: Shortest Palindrome
     * ================================================================
     *
     * 🔗 https://leetcode.com/problems/shortest-palindrome/
     *
     * 📘 Problem Statement:
     *
     * Given a string s, you can convert it to a palindrome
     * by adding characters in front of it.
     * Return the shortest palindrome you can find.
     *
     *
     * 🟢 Invariant Mapping:
     *
     * s + "#" + reverse(s)
     * Apply LPS → longest prefix palindrome
     *
     *
     * 🟡 Reasoning:
     *
     * Prefix-suffix match across original and reversed string
     *
     *
     * 🔴 Edge Cases:
     *
     * • already palindrome
     * • single char
     *
     *
     * 🟣 Interview Insight:
     *
     * "We transform palindrome detection into prefix-suffix matching"
     */
    static class ShortestPalindrome {

        public String shortestPalindrome(String s) {
            String rev = new StringBuilder(s).reverse().toString();
            String combined = s + "#" + rev;

            int[] lps = buildLPS(combined);

            int len = lps[combined.length() - 1];

            return rev.substring(0, s.length() - len) + s;
        }

        private int[] buildLPS(String s) {
            int n = s.length();
            int[] lps = new int[n];

            int len = 0, i = 1;

            while (i < n) {
                if (s.charAt(i) == s.charAt(len)) {
                    lps[i++] = ++len;
                } else {
                    if (len != 0) {
                        len = lps[len - 1];
                    } else {
                        lps[i++] = 0;
                    }
                }
            }
            return lps;
        }
    }


    /**
     * ================================================================
     * 🧩 RELATED 2: Longest Repeating Substring (Conceptual Contrast)
     * ================================================================
     *
     * 📘 Problem Statement:
     *
     * Given a string, find the length of the longest repeating substring.
     *
     *
     * 🟢 Invariant Mapping:
     *
     * ❌ NOT prefix-suffix invariant
     * ✅ Uses suffix array / binary search / hashing
     *
     *
     * 🟡 Reasoning:
     *
     * This problem breaks prefix constraint → KMP not applicable
     *
     *
     * 🔴 Pattern Boundary:
     *
     * If substring can start anywhere → KMP LPS not sufficient
     *
     *
     * 🟣 Interview Insight:
     *
     * "Recognize when NOT to use KMP — avoid pattern overfitting"
     */


    /**
     * ================================================================
     * 🟢 LEARNING VERIFICATION
     * ================================================================
     *
     * ✔ Can you state invariant without code?
     * → LPS[i] = longest prefix == suffix for s[0..i]
     *
     * ✔ Can you explain naive failure?
     * → O(n²) due to recomputation
     *
     * ✔ Can you debug mismatch?
     * → fallback using LPS[len-1]
     *
     * ✔ Pattern recognition:
     * → prefix == suffix → KMP
     *
     * ✔ Boundary:
     * → substring anywhere → NOT KMP
     */


    /**
     * ================================================================
     * 🧪 MAIN METHOD + SELF-VERIFYING TESTS
     * ================================================================
     */
    public static void main(String[] args) {

        OptimalKMP solver = new OptimalKMP();

        // 🟢 Happy path
        assert solver.longestPrefix("ababab").equals("abab") :
                "Expected 'abab' — repeated structure";

        // 🟢 Minimal case
        assert solver.longestPrefix("a").equals("") :
                "Single char → no proper prefix";

        // 🟢 Edge: no match
        assert solver.longestPrefix("abc").equals("") :
                "No prefix == suffix";

        // 🟢 Palindrome-like
        assert solver.longestPrefix("level").equals("l") :
                "Only 'l' matches";

        // 🔴 Interview trap: overlapping
        assert solver.longestPrefix("aaaaa").equals("aaaa") :
                "Overlapping allowed";

        // 🟢 Random case
        assert solver.longestPrefix("abcdabc").equals("abc") :
                "Classic prefix-suffix";

        System.out.println("All tests passed ✔");
    }


    /**
     * ================================================================
     * ✅ COMPLETION CHECKLIST
     * ================================================================
     *
     * ✔ Invariant:
     * LPS[i] = longest prefix == suffix
     *
     * ✔ Search target:
     * longest border of full string
     *
     * ✔ Discard rule:
     * fallback using LPS[len-1]
     *
     * ✔ Termination:
     * LPS[n-1]
     *
     * ✔ Naive failure:
     * recomputation → O(n²)
     *
     * ✔ Edge cases:
     * single char, no match, full repetition
     *
     * ✔ Variant readiness:
     * repeated pattern, palindrome, KMP search
     *
     * ✔ Pattern boundary:
     * substring anywhere → not KMP
     *
     *
     * 🧘 FINAL CLOSURE STATEMENT
     *
     * I understand the invariant.
     * I can re-derive the solution.
     * This chapter is complete.
     * ================================================================
     */
}
