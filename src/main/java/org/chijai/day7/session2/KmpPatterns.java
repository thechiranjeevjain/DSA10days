package org.chijai.day7.session2;

import java.util.*;

public class KmpPatterns {

    /*
    ============================================================
    📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
    ============================================================

    🔗 https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/
    Difficulty: Easy
    Tags: String, Two Pointers, String Matching, KMP

    Given two strings needle and haystack, return the index of the first occurrence of needle in haystack,
    or -1 if needle is not part of haystack.

    Example 1:
    Input: haystack = "sadbutsad", needle = "sad"
    Output: 0
    Explanation: "sad" occurs at index 0 and 6.
    The first occurrence is at index 0, so we return 0.

    Example 2:
    Input: haystack = "leetcode", needle = "leeto"
    Output: -1
    Explanation: "leeto" did not occur in "leetcode", so we return -1.

    Constraints:
    • 1 <= haystack.length, needle.length <= 10^4
    • haystack and needle consist of only lowercase English characters.

    ------------------------------------------------------------

    🔗 https://leetcode.com/problems/longest-happy-prefix/
    Difficulty: Hard
    Tags: String, Rolling Hash, String Matching

    A string is called a happy prefix if is a non-empty prefix which is also a suffix (excluding itself).

    Given a string s, return the longest happy prefix of s.

    Example 1:
    Input: s = "level"
    Output: "l"

    Example 2:
    Input: s = "ababab"
    Output: "abab"

    Example 3:
    Input: s = "leetcodeleet"
    Output: "leet"

    Constraints:
    • 1 <= s.length <= 10^5
    • s consists of lowercase English letters.

    ============================================================
    */

    /*
    ============================================================
    🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
    ============================================================

    Pattern Name:
    KMP (Knuth-Morris-Pratt) / LPS (Longest Prefix Suffix)

    Problem Archetype:
    Efficient string matching using internal structure reuse

    🟢 Core Invariant:
    At any mismatch, we reuse the longest valid prefix which is also a suffix
    instead of restarting from scratch.

    🟡 Why this works:
    The string already encodes repetition. LPS captures this overlap,
    preventing redundant comparisons.

    When this applies:
    • Pattern search inside text
    • Prefix == suffix problems
    • Repetition detection
    • Palindrome transformations

    Pattern Recognition Signals:
    • "find substring"
    • "prefix equals suffix"
    • "repeating pattern"
    • "longest border"

    ⚫ Difference from brute force:
    Brute force forgets past matches
    KMP remembers via LPS

    ============================================================
    */

    /*
    ============================================================
    🟢 MENTAL MODEL & INVARIANTS
    ============================================================

    🧠 Mental Model:
    The string learns about itself.
    LPS[i] = how much of the string can restart from here

    🟢 Invariants:

    1. LPS correctness:
       lps[i] = length of longest prefix == suffix for substring [0..i]

    2. Matching invariant:
       Characters before j are already matched

    3. Fallback invariant:
       On mismatch, jump to lps[j-1]

    4. No re-check invariant:
       Characters are never re-compared unnecessarily

    State Variables:
    i → pointer in text
    j → pointer in pattern
    len → length of current prefix match

    Allowed Moves:
    ✔ match → i++, j++
    ✔ mismatch + j>0 → j = lps[j-1]
    ✔ mismatch + j==0 → i++

    🔴 Forbidden Moves:
    ✘ resetting j to 0 blindly
    ✘ moving i during fallback
    ✘ recomputing prefix from scratch

    Termination:
    j == pattern.length → match found

    Why alternatives fail:
    They break reuse of structure → O(n*m)

    ============================================================
    */

    /*
    ============================================================
    🔴 WHY NAIVE SOLUTION FAILS
    ============================================================

    Naive Approach:
    Try matching from every index

    Why it seems correct:
    It checks all possibilities

    🔴 Violation:
    Breaks reuse invariant

    Counterexample:
    text = "aaaaab"
    pattern = "aaab"

    Naive:
    Re-checks many 'a's repeatedly

    KMP:
    Skips using LPS

    Interview Trap:
    Candidate says "works for all cases"
    But fails time complexity

    ============================================================
    */

    /*
    ============================================================
    PRIMARY PROBLEM — SOLUTION CLASSES
    ============================================================
    */

    // 🔴 Brute Force
    static class BruteForce {
        /*
        Core Idea:
        Check every starting index

        Invariant:
        None (recomputes everything)

        Time: O(n * m)
        Space: O(1)
        */

        public int strStr(String haystack, String needle) {
            for (int i = 0; i <= haystack.length() - needle.length(); i++) {
                int j = 0;
                while (j < needle.length() &&
                        haystack.charAt(i + j) == needle.charAt(j)) {
                    j++;
                }
                if (j == needle.length()) return i;
            }
            return -1;
        }
    }

    // 🟡 Improved (still naive optimization)
    static class Improved {
        /*
        Core Idea:
        Slight pruning but still resets

        Limitation:
        Still breaks invariant

        Time: O(n*m)
        */

        public int strStr(String haystack, String needle) {
            int n = haystack.length(), m = needle.length();
            for (int i = 0; i < n; i++) {
                if (haystack.charAt(i) == needle.charAt(0)) {
                    int j = 0;
                    while (i + j < n && j < m &&
                            haystack.charAt(i + j) == needle.charAt(j)) {
                        j++;
                    }
                    if (j == m) return i;
                }
            }
            return -1;
        }
    }

    // 🟢 Optimal — KMP
    static class KMP {

        /*
        🟢 Core Idea:
        Use LPS to avoid recomputation

        🟢 Invariant:
        reuse longest prefix suffix

        Time: O(n + m)
        Space: O(m)
        */

        public int strStr(String haystack, String needle) {
            if (needle.length() == 0) return 0;

            int[] lps = buildLPS(needle);

            int i = 0, j = 0;

            while (i < haystack.length()) {

                if (haystack.charAt(i) == needle.charAt(j)) {
                    i++;
                    j++;
                }

                if (j == needle.length()) {
                    return i - j;
                }

                else if (i < haystack.length()
                        && haystack.charAt(i) != needle.charAt(j)) {

                    if (j != 0) {
                        j = lps[j - 1]; // 🟢 reuse invariant
                    } else {
                        i++;
                    }
                }
            }

            return -1;
        }

        // 🔵 Build LPS
        public int[] buildLPS(String s) {
            int n = s.length();
            int[] lps = new int[n];

            int len = 0;
            int i = 1;

            while (i < n) {

                if (s.charAt(i) == s.charAt(len)) {
                    len++;
                    lps[i] = len;
                    i++;
                } else {
                    if (len != 0) {
                        len = lps[len - 1];
                    } else {
                        lps[i] = 0;
                        i++;
                    }
                }
            }

            return lps;
        }

        // 🔵 Longest Happy Prefix
        public String longestHappyPrefix(String s) {
            int[] lps = buildLPS(s);
            int len = lps[s.length() - 1];
            return s.substring(0, len);
        }
    }

    /*
    ============================================================
    🟣 INTERVIEW ARTICULATION (NO CODE)
    ============================================================

    🟢 Invariant:
    We reuse the longest prefix which is also suffix on mismatch

    🟡 Discard Logic:
    We skip characters that we KNOW will match again

    Correctness:
    No valid match is skipped because LPS preserves all valid overlaps

    🔴 If changed:
    Resetting pointer → O(n*m)

    In-place:
    Yes (LPS array only)

    When NOT to use:
    • Very small strings
    • When hashing simpler

    ============================================================
    */

    /*
    ============================================================
    🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
    ============================================================

    🟢 Invariant-Preserving Changes:

    1. Counting occurrences:
       Instead of returning on first match, continue after match
       using j = lps[j-1]

    2. Prefix extraction:
       Directly use lps[n-1]

    3. Repetition detection:
       Use math on lps[n-1]

    🟡 Reasoning-only changes:
    • Same LPS logic reused
    • Only interpretation changes

    🔴 Pattern-break signals:
    • If no overlap → KMP unnecessary
    • If substring size very small → brute may suffice

    ============================================================
    */

    /*
    ============================================================
    ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
    ============================================================
    */

    /*
    ------------------------------------------------------------
    PROBLEM 1: Repeated Substring Pattern
    🔗 https://leetcode.com/problems/repeated-substring-pattern/
    ------------------------------------------------------------

    Given a string s, check if it can be constructed by repeating a substring.

    Example:
    Input: "abab"
    Output: true

    🟢 Invariant:
    If a string repeats, LPS captures its periodic structure

    🟡 Mapping:
    len = lps[n-1]
    If len > 0 and n % (n - len) == 0 → TRUE

    Edge Cases:
    • "a" → false
    • "aaaa" → true

    🟣 Interview:
    The repetition length is derived from LPS

    */

    static class RepeatedSubstringPattern {

        public boolean repeatedSubstringPattern(String s) {
            int[] lps = buildLPS(s);
            int n = s.length();
            int len = lps[n - 1];

            return (len > 0 && n % (n - len) == 0);
        }

        private int[] buildLPS(String s) {
            int n = s.length();
            int[] lps = new int[n];
            int len = 0, i = 1;

            while (i < n) {
                if (s.charAt(i) == s.charAt(len)) {
                    lps[i++] = ++len;
                } else if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i++] = 0;
                }
            }
            return lps;
        }
    }

    /*
    ------------------------------------------------------------
    PROBLEM 2: Shortest Palindrome
    🔗 https://leetcode.com/problems/shortest-palindrome/
    ------------------------------------------------------------

    Given a string s, add characters in front to make it palindrome.

    🟢 Invariant:
    We find the longest prefix which is already palindrome

    🟡 Mapping:
    s + "#" + reverse(s)
    apply LPS

    Edge Cases:
    • empty string
    • already palindrome

    🟣 Interview:
    We convert palindrome detection into prefix-suffix match

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
            int[] lps = new int[s.length()];
            int len = 0, i = 1;

            while (i < s.length()) {
                if (s.charAt(i) == s.charAt(len)) {
                    lps[i++] = ++len;
                } else if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i++] = 0;
                }
            }
            return lps;
        }
    }

    /*
    ------------------------------------------------------------
    PROBLEM 3: Count Occurrences of Pattern
    ------------------------------------------------------------

    Given text and pattern, count all occurrences

    Example:
    text = "ababab", pattern = "ab"
    output = 3

    🟢 Invariant:
    Same KMP, but do not stop after match

    Edge Cases:
    • overlapping matches
    • pattern longer than text

    🟣 Interview:
    Continue search after match using LPS fallback

    */

    static class CountOccurrences {

        public int count(String text, String pattern) {
            int[] lps = buildLPS(pattern);

            int i = 0, j = 0, count = 0;

            while (i < text.length()) {

                if (text.charAt(i) == pattern.charAt(j)) {
                    i++;
                    j++;
                }

                if (j == pattern.length()) {
                    count++;
                    j = lps[j - 1];
                }

                else if (i < text.length() &&
                        text.charAt(i) != pattern.charAt(j)) {

                    if (j != 0) j = lps[j - 1];
                    else i++;
                }
            }

            return count;
        }

        private int[] buildLPS(String s) {
            int[] lps = new int[s.length()];
            int len = 0, i = 1;

            while (i < s.length()) {
                if (s.charAt(i) == s.charAt(len)) {
                    lps[i++] = ++len;
                } else if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i++] = 0;
                }
            }
            return lps;
        }
    }

    /*
    ------------------------------------------------------------
    PROBLEM 4: String Rotation Check
    ------------------------------------------------------------

    Check if s2 is rotation of s1

    Example:
    s1 = "abcd", s2 = "cdab"

    🟢 Invariant:
    Rotation preserves substring structure

    🟡 Mapping:
    s2 in s1 + s1

    Edge Cases:
    • unequal length → false

    🟣 Interview:
    Reduces rotation → substring search

    */

    static class RotationCheck {

        public boolean isRotation(String s1, String s2) {
            if (s1.length() != s2.length()) return false;

            String combined = s1 + s1;

            return new KMP().strStr(combined, s2) != -1;
        }
    }


        /*
    ============================================================
    🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
    ============================================================
    */

    /*
    ------------------------------------------------------------
    RELATED PROBLEM 1: Longest Prefix also appearing in middle
    ------------------------------------------------------------

    Problem:
    Find longest prefix which appears somewhere else (not just suffix)

    🟢 Invariant:
    LPS chain gives all valid prefix-suffix candidates

    Approach:
    Traverse LPS chain and check presence

    Edge Cases:
    • no repetition → ""
    • all same chars → full chain

    🟣 Interview:
    Use LPS chain traversal

    */

    static class LongestPrefixInMiddle {

        public String solve(String s) {
            int[] lps = buildLPS(s);
            int n = s.length();

            Set<Integer> seen = new HashSet<>();
            for (int i = 0; i < n - 1; i++) {
                seen.add(lps[i]);
            }

            int len = lps[n - 1];

            while (len > 0) {
                if (seen.contains(len)) {
                    return s.substring(0, len);
                }
                len = lps[len - 1];
            }

            return "";
        }

        private int[] buildLPS(String s) {
            int[] lps = new int[s.length()];
            int len = 0, i = 1;

            while (i < s.length()) {
                if (s.charAt(i) == s.charAt(len)) {
                    lps[i++] = ++len;
                } else if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i++] = 0;
                }
            }
            return lps;
        }
    }

    /*
    ------------------------------------------------------------
    RELATED PROBLEM 2: Longest Border
    ------------------------------------------------------------

    Problem:
    Find longest prefix == suffix

    🟢 Invariant:
    lps[n-1] gives longest border

    Edge Cases:
    • no match → 0

    */

    static class LongestBorder {

        public int getBorderLength(String s) {
            int[] lps = buildLPS(s);
            return lps[s.length() - 1];
        }

        private int[] buildLPS(String s) {
            int[] lps = new int[s.length()];
            int len = 0, i = 1;

            while (i < s.length()) {
                if (s.charAt(i) == s.charAt(len)) {
                    lps[i++] = ++len;
                } else if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i++] = 0;
                }
            }
            return lps;
        }
    }

    /*
    ============================================================
    🟢 LEARNING VERIFICATION
    ============================================================

    ✔ Can you state invariant?
    Reuse longest prefix-suffix

    ✔ Can you explain naive failure?
    Recomputes from scratch → O(n*m)

    ✔ Debug readiness?
    Check LPS fallback logic

    ✔ Recognition signal?
    prefix == suffix / substring search

    ✔ Pattern boundary?
    No repetition → no KMP benefit

    ============================================================
    */

    /*
    ============================================================
    🧪 MAIN METHOD + SELF-VERIFYING TESTS
    ============================================================
    */

    public static void main(String[] args) {

        KMP kmp = new KMP();

        // ✅ Test 1: Basic match
        assert kmp.strStr("sadbutsad", "sad") == 0 : "Basic match failed";

        // ✅ Test 2: No match
        assert kmp.strStr("leetcode", "leeto") == -1 : "No match failed";

        // ✅ Test 3: Happy prefix
        assert kmp.longestHappyPrefix("ababab").equals("abab") : "Happy prefix failed";

        // ✅ Test 4: Repeated substring
        assert new RepeatedSubstringPattern().repeatedSubstringPattern("abab") : "Repeat failed";

        // ✅ Test 5: Count occurrences
        assert new CountOccurrences().count("ababab", "ab") == 3 : "Count failed";

        // ✅ Test 6: Rotation check
        assert new RotationCheck().isRotation("abcd", "cdab") : "Rotation failed";

        // ✅ Test 7: Shortest palindrome
        assert new ShortestPalindrome().shortestPalindrome("aacecaaa").equals("aaacecaaa") : "Palindrome failed";

        // ✅ Test 8: Edge case — single char
        assert kmp.strStr("a", "a") == 0 : "Single char failed";

        // ✅ Test 9: Overlapping pattern
        assert new CountOccurrences().count("aaaa", "aa") == 3 : "Overlap failed";

        // ✅ Test 10: No border
        assert new LongestBorder().getBorderLength("abc") == 0 : "Border failed";

        System.out.println("All tests passed ✅");
    }

    /*
    ============================================================
    ✅ COMPLETION CHECKLIST
    ============================================================

    ✔ Invariant:
    Reuse longest prefix which is also suffix

    ✔ Search target:
    Matching pattern / structural overlap

    ✔ Discard rule:
    Use lps[j-1]

    ✔ Termination:
    j == pattern.length

    ✔ Naive failure:
    O(n*m) recomputation

    ✔ Edge cases:
    empty, single char, overlap

    ✔ Variant readiness:
    repetition, palindrome, rotation

    ✔ Pattern boundary:
    no overlap → no need for KMP

    ============================================================

    🧘 FINAL CLOSURE STATEMENT

    I understand the invariant.
    I can re-derive the solution.
    This chapter is complete.

    ============================================================
    */
}

    /*
    ============================================================
    🔴 CRITICAL DIFFERENTIATION — KMP vs LPS USAGE (INTERVIEW CORE)
    ============================================================

    This section corrects a common but dangerous misunderstanding:

    ❌ Misconception:
    "All KMP problems are the same because they use LPS"

    🟢 Truth:
    SAME TOOL (LPS), DIFFERENT PROBLEM CLASSES

    ------------------------------------------------------------
    🧠 CLASSIFICATION FRAMEWORK (MANDATORY IN INTERVIEWS)
    ------------------------------------------------------------

    TYPE 1: EXTERNAL MATCHING (KMP SEARCH)

    Problem Form:
    • Given two strings (text + pattern)
    • Find occurrence / count occurrences

    Example:
    strStr()

    🟢 Invariant:
    Pattern prefix already matched can be reused after mismatch

    🟡 Role of LPS:
    TOOL (used during matching)

    🟣 Interpretation:
    We are MATCHING two strings

    ------------------------------------------------------------

    TYPE 2: INTERNAL STRUCTURE (LPS DIRECT)

    Problem Form:
    • Single string
    • Find prefix == suffix / repetition / border

    Example:
    Longest Happy Prefix

    🟢 Invariant:
    String overlaps with itself

    🟡 Role of LPS:
    ANSWER ITSELF

    🟣 Interpretation:
    We are ANALYZING one string

    ------------------------------------------------------------

    TYPE 3: TRANSFORMATION PROBLEMS

    Problem Form:
    • Modify string (palindrome, rotation, etc.)

    Example:
    Shortest Palindrome

    🟢 Invariant:
    Transform problem into prefix-suffix structure

    🟡 Role of LPS:
    USED AFTER TRANSFORMATION

    ------------------------------------------------------------

    ⚫ QUICK INTERVIEW DECISION TREE (10 seconds)

    Step 1:
    "Are there TWO strings?"
        YES → KMP SEARCH
        NO → Step 2

    Step 2:
    "Is this about prefix == suffix?"
        YES → LPS DIRECT
        NO → Step 3

    Step 3:
    "Can I transform into prefix-suffix?"
        YES → KMP TRANSFORMATION
        NO → Not KMP

    ------------------------------------------------------------

    🔴 FAILURE MODE (WHAT INTERVIEWERS CATCH)

    Candidate says:
    "Both problems are same, both use KMP"

    ❌ This shows shallow understanding

    Correct articulation:

    🟣 Interview Answer:

    "Both problems use the same LPS preprocessing,
     but they belong to different categories.

     In substring search, LPS helps match a pattern
     against another string efficiently.

     In longest happy prefix, we are analyzing the
     structure of a single string, and the LPS value
     itself is the answer."

    ------------------------------------------------------------

    🧠 DEEP UNIFYING INSIGHT

    KMP answers ONE fundamental question:

    "How does a string overlap with itself?"

    • Search → apply overlap to another string
    • Happy prefix → extract overlap directly
    • Repetition → quantify overlap
    • Palindrome → enforce overlap via reverse

    ------------------------------------------------------------

    🟢 FINAL INVARIANT CONSOLIDATION

    All KMP problems reduce to:

    "Reuse previously matched prefix using LPS"

    The ONLY difference:
    → where and how that reuse is applied

    ============================================================
    */

    /*
    ============================================================
    🔴 EXPLICIT DIFFERENCE — KMP SEARCH vs HAPPY PREFIX
    ============================================================

    This section makes the distinction NON-AMBIGUOUS for interviews.

    ------------------------------------------------------------
    🧠 SIDE-BY-SIDE COMPARISON
    ------------------------------------------------------------

    Dimension                | KMP Search (strStr)              | Longest Happy Prefix
    ----------------------------------------------------------------------------------------
    Problem Type             | Search problem                   | Structural analysis
    Input                    | TWO strings                      | ONE string
    Goal                     | Find occurrence index            | Find prefix == suffix
    LPS Role                 | TOOL                             | FINAL ANSWER
    Output                   | Integer index                    | Substring
    Thinking Mode            | Compare two strings              | Analyze one string
    Core Question            | "Where does pattern occur?"      | "How does string overlap?"

    ------------------------------------------------------------
    🟢 CORE INVARIANT (SAME BASE, DIFFERENT APPLICATION)

    Both rely on:

    "Reuse longest prefix which is also suffix"

    BUT:

    KMP Search:
    → applies this invariant DURING matching

    Happy Prefix:
    → extracts this invariant DIRECTLY as answer

    ------------------------------------------------------------
    🔍 CODE-LEVEL DIFFERENCE (CRITICAL)

    KMP Search:

        int[] lps = buildLPS(needle);

        // LPS used dynamically during matching
        while (i < haystack.length()) {
            if (match) {
                i++; j++;
            } else {
                j = lps[j - 1];
            }
        }

    Happy Prefix:

        int[] lps = buildLPS(s);

        // LPS directly gives answer
        return s.substring(0, lps[n - 1]);

    ------------------------------------------------------------
    ⚫ INTERVIEW RECOGNITION RULE (10-SECOND DECISION)

    Step 1:
    "Am I matching TWO strings?"
        YES → KMP SEARCH

    Step 2:
    "Am I analyzing prefix == suffix of ONE string?"
        YES → LPS DIRECT (Happy Prefix)

    ------------------------------------------------------------
    🔴 COMMON INTERVIEW MISTAKE

    Candidate says:
    "Both are same because both use KMP"

    ❌ Incorrect reasoning

    Correct answer:

    🟣 Interview articulation:

    "Both problems use the same LPS preprocessing,
     but they solve different categories of problems.

     In KMP search, LPS is used as a helper to match
     a pattern within another string efficiently.

     In longest happy prefix, we are analyzing a single
     string, and the LPS value itself directly gives the answer."

    ------------------------------------------------------------
    🧠 DEEP INSIGHT

    KMP solves:

    "How does a string overlap with itself?"

    Then:

    • Search problem → apply overlap to another string
    • Happy prefix → extract overlap directly

    ------------------------------------------------------------
    🟢 FINAL MEMORY HOOK

    KMP Search:
    → USING structure

    Happy Prefix:
    → FINDING structure

    ============================================================
    */

