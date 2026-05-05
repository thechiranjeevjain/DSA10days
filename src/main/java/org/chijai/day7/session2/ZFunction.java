package org.chijai.day7.session2;

import java.util.*;

public class ZFunction {

    /*
    📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT

    LeetCode Link:
    https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/

    Difficulty: Easy

    Tags:
    String, Two Pointers, String Matching

    --------------------------------------------------------------------------------

    Given two strings needle and haystack, return the index of the first occurrence
    of needle in haystack, or -1 if needle is not part of haystack.

    --------------------------------------------------------------------------------

    Example 1:
    Input: haystack = "sadbutsad", needle = "sad"
    Output: 0
    Explanation:
    "sad" occurs at index 0 and 6.
    The first occurrence is at index 0, so we return 0.

    --------------------------------------------------------------------------------

    Example 2:
    Input: haystack = "leetcode", needle = "leeto"
    Output: -1
    Explanation:
    "leeto" did not occur in "leetcode", so we return -1.

    --------------------------------------------------------------------------------

    Constraints:

    1 <= haystack.length, needle.length <= 10^4
    haystack and needle consist of only lowercase English characters.

    --------------------------------------------------------------------------------
    */


    // ============================================================
    // 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
    // ============================================================

    /*
    🔵 Pattern Name:
    Z-Function (Z-Array String Matching)

    🔵 Problem Archetype:
    "Find pattern inside text" using prefix matching reuse

    🟢 Core Invariant (MANDATORY):
    Z[i] = length of longest substring starting at i which matches prefix of the entire string

    🟡 Why this invariant works:
    If we concatenate:
        combined = needle + "#" + haystack

    Then:
        Any index i where Z[i] == needle.length()
        ⇒ means substring at i matches full needle
        ⇒ match found in haystack

    🔵 When this pattern applies:
    • Exact pattern matching
    • Prefix-based matching problems
    • When we want linear time matching

    🔵 Pattern recognition signals:
    • "Find first occurrence"
    • "Match substring"
    • "Avoid O(n*m)"
    • Prefix reuse is possible

    🔵 Difference from KMP:
    Z:
      → builds matches FORWARD from each index
      → compares with prefix directly

    KMP:
      → builds LPS (failure function)
      → reuses suffix-prefix info

    ⚫ Mapping:
    Z = "How much of prefix matches here?"
    KMP = "If mismatch, where to jump?"
    */


    // ============================================================
    // 🟢 MENTAL MODEL & INVARIANTS
    // ============================================================

    /*
    🔵 Mental Model:

    Imagine sliding the prefix across the string.

    At every index i:
        "How much of prefix matches starting here?"

    We maintain a window [L, R] where:
        substring L..R matches prefix 0..(R-L)

    This window helps us reuse previous comparisons.

    ------------------------------------------------------------

    🟢 Invariants:

    1. Z[i] = length of prefix match starting at i

    2. [L, R] is the rightmost segment where:
       s[L..R] == s[0..R-L]

    3. For i inside [L, R]:
       We reuse previously computed values:
           Z[i] = min(R - i + 1, Z[i - L])

    ------------------------------------------------------------

    🟢 State Variables:

    i → current index
    L → left boundary of Z-box
    R → right boundary of Z-box
    Z[i] → prefix match length

    ------------------------------------------------------------

    🟢 Allowed Moves:

    • Expand match when characters equal
    • Reuse previously computed Z values

    ------------------------------------------------------------

    🔴 Forbidden Moves:

    • Re-check characters already matched
    • Ignore Z-box reuse (leads to O(n^2))

    ------------------------------------------------------------

    🟢 Termination:

    Traverse entire string once → O(n)

    ------------------------------------------------------------

    🔴 Why common alternatives fail:

    Naive matching:
        Rechecks characters repeatedly → O(n*m)

    Without reuse:
        You lose linear time guarantee
    */


    // ============================================================
    // 🔴 WHY NAIVE / WRONG SOLUTION FAILS
    // ============================================================

    /*
    🔴 Wrong Approach 1: Brute Force Matching

    Try matching needle at every index.

    Why it seems correct:
        Works for small inputs

    Why it fails:
        Re-checks same characters repeatedly

    Complexity:
        O(n * m)

    Counterexample:
        haystack = "aaaaaaaaaa"
        needle   = "aaaaaab"

        Repeated comparisons → worst case

    ------------------------------------------------------------

    🔴 Wrong Approach 2: Reset on mismatch

    Example:
        match prefix, mismatch → restart

    Violates invariant:
        Does NOT reuse previous matching knowledge

    ------------------------------------------------------------

    🔴 Interview Trap:

    Interviewer expects:
        "How do you avoid re-checking characters?"

    Correct answer:
        "By maintaining prefix match invariant using Z-box"
    */


    // ============================================================
    // PRIMARY PROBLEM — SOLUTION CLASSES
    // ============================================================


    // ============================================================
    // 1. BRUTE FORCE
    // ============================================================

    static class BruteForce {

        /*
        🔵 Core Idea:
        Try matching needle at every index

        🟢 Invariant:
        Compare characters sequentially

        🔴 Limitation:
        Repeats work → O(n*m)
        */

        public int strStr(String haystack, String needle) {

            if (needle.length() == 0) return 0;

            for (int i = 0; i <= haystack.length() - needle.length(); i++) {

                int j = 0;

                while (j < needle.length() &&
                        haystack.charAt(i + j) == needle.charAt(j)) {
                    j++;
                }

                if (j == needle.length()) {
                    return i;
                }
            }

            return -1;
        }

        /*
        Time Complexity: O(n * m)
        Space Complexity: O(1)

        🟣 Interview:
        Mention as baseline only
        */
    }


    // ============================================================
    // 2. IMPROVED (STRING CONCAT + Z WITHOUT OPTIMIZATION)
    // ============================================================

    static class ZBasic {

        /*
        🔵 Core Idea:
        Build Z-array but without optimization

        🟢 Invariant:
        Z[i] = prefix match length

        🔴 Limitation:
        No reuse → still O(n^2)
        */

        public int strStr(String haystack, String needle) {

            String combined = needle + "#" + haystack;

            int n = combined.length();
            int[] Z = new int[n];

            for (int i = 1; i < n; i++) {

                while (i + Z[i] < n &&
                        combined.charAt(Z[i]) == combined.charAt(i + Z[i])) {
                    Z[i]++;
                }

                if (Z[i] == needle.length()) {
                    return i - needle.length() - 1;
                }
            }

            return -1;
        }

        /*
        Time Complexity: O(n^2)
        Space Complexity: O(n)

        🟣 Interview:
        Shows understanding of Z definition,
        but NOT optimal
        */
    }

    // ============================================================
    // 3. OPTIMAL (Z-ALGORITHM WITH Z-BOX OPTIMIZATION)
    // ============================================================

    static class ZOptimal {

        /*
        🔵 Core Idea:
        Maintain a Z-box [L, R] to reuse previous computations

        🟢 Invariant:
        Z[i] = longest prefix match starting at i

        Additionally:
        For i inside Z-box:
            Z[i] = min(R - i + 1, Z[i - L])

        This avoids redundant comparisons

        ------------------------------------------------------------
        🟡 What it fixes:
        Eliminates repeated comparisons → guarantees O(n)

        ------------------------------------------------------------
        🟣 Interview Preference:
        YES — Linear time, invariant-driven, optimal
        */

        public int strStr(String haystack, String needle) {

            String combined = needle + "#" + haystack;
            int n = combined.length();

            int[] Z = new int[n];

            int L = 0, R = 0;

            for (int i = 1; i < n; i++) {

                // 🟢 Case 1: i inside Z-box → reuse info
                if (i <= R) {
                    Z[i] = Math.min(R - i + 1, Z[i - L]);
                }

                // 🟢 Expand beyond known Z-box
                while (i + Z[i] < n &&
                        combined.charAt(Z[i]) == combined.charAt(i + Z[i])) {
                    Z[i]++;
                }

                // 🟢 Update Z-box if expanded
                if (i + Z[i] - 1 > R) {
                    L = i;
                    R = i + Z[i] - 1;
                }

                // 🟢 Match found
                if (Z[i] == needle.length()) {
                    return i - needle.length() - 1;
                }
            }

            return -1;
        }

        /*
        Time Complexity: O(n)
        Space Complexity: O(n)

        🟣 Interview Notes:
        • Always explain Z-box reuse
        • Emphasize invariant
        • Explain why no character is rechecked
        */
    }


// ============================================================
// 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
// ============================================================

    /*
    🟣 Step-by-step articulation:

    1. State invariant:
       "At every index i, Z[i] stores how many characters from prefix match here"

    ------------------------------------------------------------

    2. Explain discard logic:
       Instead of rechecking characters, we reuse previous matches
       using Z-box [L, R]

    ------------------------------------------------------------

    3. Why correctness is guaranteed:
       Every Z[i] is computed based on:
           • previously verified prefix matches
           • controlled expansion

    No match is missed because:
       we always expand when needed

    ------------------------------------------------------------

    4. What breaks if changed:

       ❌ If we don't use min(R - i + 1, Z[i - L]):
          → we may overestimate matches

       ❌ If we skip expansion:
          → we miss longer matches

    ------------------------------------------------------------

    5. In-place feasibility:
       Not in-place (needs extra array)

    ------------------------------------------------------------

    6. When NOT to use:
       • When pattern changes dynamically
       • When approximate matching required
    */


// ============================================================
// 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
// ============================================================

    /*
    🔵 Invariant-preserving changes:

    • Find ALL occurrences:
        Instead of returning first, collect all i where Z[i] == pattern length

    • Count matches:
        Increment counter instead of returning

    ------------------------------------------------------------

    🔵 Reasoning-only changes:

    • Replace '#' with any separator not in string
    • Works for any character set

    ------------------------------------------------------------

    🔴 Pattern-break signals:

    • If problem involves mismatches (approximate match)
        → Z fails

    • If pattern is streaming input
        → KMP preferred

    ------------------------------------------------------------

    ⚫ Pattern Mapping:

    Z → prefix comparison forward
    KMP → fallback on mismatch

    Choose:
    • Z → when prefix comparisons dominate
    • KMP → when mismatches frequent
    */


    // ============================================================
    // ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
    // ============================================================

    // ------------------------------------------------------------
    // PROBLEM 1: Find All Occurrences of a Pattern in a String
    // ------------------------------------------------------------
    static class AllOccurrences_Z {

        /*
        📘 Problem Statement:

        Given two strings text and pattern, return ALL starting indices where
        pattern appears in text.

        Example:
        Input: text = "ababa", pattern = "aba"
        Output: [0, 2]

        ------------------------------------------------------------

        🟢 Invariant Mapping:
        Z[i] = prefix match length

        If Z[i] == pattern.length()
            → full match

        ------------------------------------------------------------

        🔵 Core Idea:
        Same as main problem, but collect all matches
        */

        public List<Integer> findAll(String text, String pattern) {

            String combined = pattern + "#" + text;
            int n = combined.length();

            int[] Z = new int[n];
            int L = 0, R = 0;

            List<Integer> result = new ArrayList<>();

            for (int i = 1; i < n; i++) {

                if (i <= R) {
                    Z[i] = Math.min(R - i + 1, Z[i - L]);
                }

                while (i + Z[i] < n &&
                        combined.charAt(Z[i]) == combined.charAt(i + Z[i])) {
                    Z[i]++;
                }

                if (i + Z[i] - 1 > R) {
                    L = i;
                    R = i + Z[i] - 1;
                }

                if (Z[i] == pattern.length()) {
                    result.add(i - pattern.length() - 1);
                }
            }

            return result;
        }

        /*
        Time: O(n)
        Space: O(n)

        🟣 Interview:
        Extension of base problem
        */
    }


    // ------------------------------------------------------------
    // PROBLEM 2: Longest Prefix Matching Anywhere
    // ------------------------------------------------------------
    static class LongestPrefixMatch {

        /*
        📘 Problem Statement:

        Given a string s, find the maximum length substring that matches
        the prefix of s but appears somewhere else.

        ------------------------------------------------------------

        🟢 Invariant:
        Z[i] = prefix match length

        Take max over all i > 0

        ------------------------------------------------------------

        🔵 Core Idea:
        Compute Z array, return max(Z[i])
        */

        public int longestPrefix(String s) {

            int n = s.length();
            int[] Z = new int[n];

            int L = 0, R = 0;
            int max = 0;

            for (int i = 1; i < n; i++) {

                if (i <= R) {
                    Z[i] = Math.min(R - i + 1, Z[i - L]);
                }

                while (i + Z[i] < n &&
                        s.charAt(Z[i]) == s.charAt(i + Z[i])) {
                    Z[i]++;
                }

                if (i + Z[i] - 1 > R) {
                    L = i;
                    R = i + Z[i] - 1;
                }

                max = Math.max(max, Z[i]);
            }

            return max;
        }

        /*
        Edge Cases:
        • All same characters
        • No repetition

        🟣 Interview:
        Direct invariant application
        */
    }


    // ------------------------------------------------------------
    // PROBLEM 3: Count Pattern Occurrences
    // ------------------------------------------------------------
    static class CountOccurrences {

        /*
        📘 Problem Statement:

        Count how many times pattern appears in text.

        Example:
        text = "aaaaa", pattern = "aa"
        Output = 4

        ------------------------------------------------------------

        🟢 Invariant:
        Same as base problem

        ------------------------------------------------------------

        🔵 Core Idea:
        Count matches instead of returning first
        */

        public int count(String text, String pattern) {

            String combined = pattern + "#" + text;

            int[] Z = new int[combined.length()];
            int L = 0, R = 0;
            int count = 0;

            for (int i = 1; i < combined.length(); i++) {

                if (i <= R) {
                    Z[i] = Math.min(R - i + 1, Z[i - L]);
                }

                while (i + Z[i] < combined.length() &&
                        combined.charAt(Z[i]) == combined.charAt(i + Z[i])) {
                    Z[i]++;
                }

                if (i + Z[i] - 1 > R) {
                    L = i;
                    R = i + Z[i] - 1;
                }

                if (Z[i] == pattern.length()) {
                    count++;
                }
            }

            return count;
        }

        /*
        Time: O(n)
        Space: O(n)

        🟣 Interview:
        Simple tweak → same invariant
        */
    }


    // ============================================================
    // 🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
    // ============================================================

    // ------------------------------------------------------------
    // RELATED 1: Longest Happy Prefix (KMP-based)
    // ------------------------------------------------------------
    static class LongestHappyPrefix {

        /*
        📘 Problem:
        Longest prefix which is also suffix

        🟢 Invariant:
        LPS[i] = longest prefix-suffix

        ⚫ Mapping:
        KMP instead of Z

        Difference:
        Z → forward prefix matching
        KMP → suffix-prefix reuse
        */

        public String longestPrefix(String s) {

            int n = s.length();
            int[] lps = new int[n];

            int len = 0;

            for (int i = 1; i < n; ) {

                if (s.charAt(i) == s.charAt(len)) {
                    lps[i++] = ++len;
                } else if (len > 0) {
                    len = lps[len - 1];
                } else {
                    lps[i++] = 0;
                }
            }

            return s.substring(0, lps[n - 1]);
        }
    }


    // ------------------------------------------------------------
    // RELATED 2: Repeated Substring Pattern
    // ------------------------------------------------------------
    static class RepeatedSubstring {

        /*
        📘 Problem:
        Check if string is repetition of substring

        🟢 Invariant:
        LPS[n-1] > 0 AND divides n

        ⚫ Mapping:
        KMP variant
        */

        public boolean repeatedSubstringPattern(String s) {

            int n = s.length();
            int[] lps = new int[n];

            int len = 0;

            for (int i = 1; i < n; ) {

                if (s.charAt(i) == s.charAt(len)) {
                    lps[i++] = ++len;
                } else if (len > 0) {
                    len = lps[len - 1];
                } else {
                    lps[i++] = 0;
                }
            }

            int l = lps[n - 1];
            return l > 0 && n % (n - l) == 0;
        }
    }


    // ============================================================
    // 🟢 LEARNING VERIFICATION
    // ============================================================

    /*
    🟢 Can you answer without code?

    1. What does Z[i] represent?
       → prefix match length at i

    2. Why naive fails?
       → repeated comparisons

    3. What is Z-box?
       → reusable prefix match window

    4. When to use Z?
       → exact pattern matching

    5. Key invariant?
       → prefix match reuse

    ------------------------------------------------------------

    🟢 Debug readiness:

    If wrong:
        • Check separator '#'
        • Check index conversion
        • Check Z-box boundaries

    ------------------------------------------------------------

    🟢 Pattern signals:

    • "find substring"
    • "match pattern"
    • "optimize brute force"
    */


    // ============================================================
    // 🧪 MAIN METHOD + SELF-VERIFYING TESTS
    // ============================================================

    public static void main(String[] args) {

        ZOptimal optimal = new ZOptimal();

        // Test 1: Basic match
        assert optimal.strStr("sadbutsad", "sad") == 0 :
                "Test 1 Failed";

        // Test 2: No match
        assert optimal.strStr("leetcode", "leeto") == -1 :
                "Test 2 Failed";

        // Test 3: Match at end
        assert optimal.strStr("hello", "lo") == 3 :
                "Test 3 Failed";

        // Test 4: Repeated pattern
        assert optimal.strStr("aaaaa", "aaa") == 0 :
                "Test 4 Failed";

        // Test 5: Single character
        assert optimal.strStr("a", "a") == 0 :
                "Test 5 Failed";

        // Reinforcement test: All occurrences
        AllOccurrences_Z all = new AllOccurrences_Z();
        List<Integer> res = all.findAll("ababa", "aba");

        assert res.equals(Arrays.asList(0, 2)) :
                "All occurrences failed";

        // Count occurrences
        CountOccurrences count = new CountOccurrences();
        assert count.count("aaaaa", "aa") == 4 :
                "Count failed";

        System.out.println("All tests passed ✅");
    }


    // ============================================================
    // ✅ COMPLETION CHECKLIST
    // ============================================================

    /*
    • Invariant:
      Z[i] = prefix match length

    • Search target:
      index where Z[i] == pattern length

    • Discard rule:
      reuse Z-box instead of rechecking

    • Termination:
      single pass

    • Naive failure:
      repeated comparisons

    • Edge cases:
      empty, single char, repeated chars

    • Variant readiness:
      count / all matches / longest prefix

    • Pattern boundary:
      exact matching only
    */


    /*
    🧘 FINAL CLOSURE STATEMENT

    I understand the invariant.
    I can re-derive the solution.
    This chapter is complete.
    */
}


