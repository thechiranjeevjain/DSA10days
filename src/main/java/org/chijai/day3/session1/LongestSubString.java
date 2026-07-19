package org.chijai.day3.session1;

import java.util.HashMap;
import java.util.HashSet;

public class LongestSubString {

    /*
    =====================================================================
    📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
    =====================================================================

    🔗 https://leetcode.com/problems/longest-substring-without-repeating-characters/
    🧩 Difficulty: Medium
    🏷️ Tags: Hash Table, String, Sliding Window

    Given a string s, find the length of the longest substring
    without repeating characters.

    A substring is a contiguous sequence of characters within the string.

    ---------------------------------------------------------------------
    Example 1:
    Input: s = "abcabcbb"
    Output: 3
    Explanation: The answer is "abc", with the length of 3.

    ---------------------------------------------------------------------
    Example 2:
    Input: s = "bbbbb"
    Output: 1
    Explanation: The answer is "b", with the length of 1.

    ---------------------------------------------------------------------
    Example 3:
    Input: s = "pwwkew"
    Output: 3
    Explanation: The answer is "wke", with the length of 3.
    Notice that the answer must be a substring.
    "pwke" is a subsequence and not a substring.

    ---------------------------------------------------------------------
    Constraints:
    0 <= s.length <= 5 * 10^4
    s consists of English letters, digits, symbols and spaces.
    */

    // =====================================================================
    // 🔵 CORE PATTERN OVERVIEW
    // =====================================================================
    /*
    Pattern Name:
    Sliding Window with Dynamic Shrinking

    Core Idea:
    Maintain a contiguous window that always satisfies a constraint.
    Expand greedily. Shrink only when the constraint is violated.

    Why It Works:
    - Window grows monotonically
    - Violations are resolved locally by shrinking
    - Each element enters and exits the window at most once

    When To Use:
    - Substring / subarray problems
    - Contiguity is mandatory
    - Constraint depends only on window contents

    🧭 Pattern Recognition Signals:
    - “Longest / shortest substring”
    - “At most / without / exactly K”
    - Constraint violated by expansion, fixed by shrinking

    How This Differs From Similar Patterns:
    - Unlike prefix sums: order & contiguity matter
    - Unlike binary search: no monotonic answer space
    */

    // =====================================================================
    // 🟢 MENTAL MODEL & INVARIANTS
    // =====================================================================
    /*
    Mental Model:
    Think of a movable window over the string.
    Right edge explores new characters.
    Left edge cleans up violations.

    🟢 Core Invariant:
    The window [left, right) contains NO repeating characters.

    Variable Roles:
    left  → start of valid window
    right → end (exclusive) of window
    set   → characters currently inside the window
    maxLen → best valid window length seen so far

    Termination Logic:
    - right increases until end of string
    - left never moves backward
    - Both are bounded → loop must end

    Forbidden Actions:
    ❌ Restarting the window completely
    ❌ Moving left backward
    ❌ Checking all substrings

    Why Common Alternatives Are Inferior:
    - Brute force recomputes overlapping substrings
    - Restarting loses valid partial progress
    */

    // =====================================================================
    // 🔴 WHY NAIVE / WRONG SOLUTIONS FAIL
    // =====================================================================
    /*
    Wrong Approach 1: Check all substrings
    Why it seems correct:
    - Direct interpretation of problem
    Why it fails:
    - O(n^3) time → TLE

    Wrong Approach 2: Restart window on duplicate
    Why it seems correct:
    - “Fresh start” intuition
    Exact invariant violated:
    - Left pointer monotonicity
    Counterexample:
    "abba" → restarting misses "ba"

    Interviewer Trap:
    They ask:
    “Why not reset left to right + 1?”
    Correct answer:
    Because we would discard valid characters unnecessarily.
    */

    // =====================================================================
    // 🟢 PRIMARY PROBLEM — SOLUTION CLASSES
    // =====================================================================

    static class BruteForce {
        /*
        Core Idea:
        Try every substring and test uniqueness

        Time: O(n^3)
        Space: O(n)

        Interview Preference:
        ❌ Only acceptable as baseline discussion
        */

        static int lengthOfLongestSubstring(String s) {
            int maxLen = 0;
            for (int i = 0; i < s.length(); i++) {
                for (int j = i; j < s.length(); j++) {
                    if (allUnique(s, i, j)) {
                        maxLen = Math.max(maxLen, j - i + 1);
                    }
                }
            }
            return maxLen;
        }

        private static boolean allUnique(String s, int start, int end) {
            boolean[] seen = new boolean[256];
            for (int i = start; i <= end; i++) {
                char c = s.charAt(i);
                if (seen[c]) return false;
                seen[c] = true;
            }
            return true;
        }
    }

    static class Improved {
        /*
        Core Idea:
        Fix left, expand right until duplicate

        Time: O(n^2)
        Space: O(n)

        Still too slow for large input
        */

        static int lengthOfLongestSubstring(String s) {
            int maxLen = 0;
            for (int i = 0; i < s.length(); i++) {
                boolean[] seen = new boolean[256];
                for (int j = i; j < s.length(); j++) {
                    char c = s.charAt(j);
                    if (seen[c]) break;
                    seen[c] = true;
                    maxLen = Math.max(maxLen, j - i + 1);
                }
            }
            return maxLen;
        }
    }

    // 🔵 INVARIANT-EXPLICIT VERSION
// Invariant: duplicateCharCount == number of chars with freq > 1
// Window valid ⇔ duplicateCharCount == 0

    static class Optimal_ExplicitInvariant {

        static int lengthOfLongestSubstring(String s) {

            int[] windowCharCount = new int[128];
            int left = 0, right = 0;
            int maxLen = 0;
            int duplicateCharCount = 0;

            while (right < s.length()) {
                char curr = s.charAt(right);

                // 🔴 Violation introduced
                if (windowCharCount[curr] > 0)
                    duplicateCharCount++;

                windowCharCount[curr]++;
                right++;

                // 🔧 Restore invariant
                while (duplicateCharCount > 0) {
                    char removed = s.charAt(left);

                    if (windowCharCount[removed] > 1)
                        duplicateCharCount--;

                    windowCharCount[removed]--;
                    left++;
                }

                // 🟢 Valid window
                maxLen = Math.max(maxLen, right - left);
            }
            return maxLen;
        }
    }

    // 🟡 SEEN-ARRAY VERSION
    // Invariant: seen[c] == true ⇔ c exists in window

    static class Optimal_SeenArray {

        static int lengthOfLongestSubstring(String s) {

            boolean[] seen = new boolean[128];
            int left = 0, right = 0, maxLen = 0;

            while (right < s.length()) {
                char curr = s.charAt(right);

                while (seen[curr]) {
                    seen[s.charAt(left)] = false;
                    left++;
                }

                seen[curr] = true;
                right++;
                maxLen = Math.max(maxLen, right - left);
            }
            return maxLen;
        }
    }


    // 🔒 CANONICAL INTERVIEW VERSION
    // Pattern: Sliding Window (uniqueness)
    // Invariant: window [left, right) has NO duplicate characters

    static class Optimal_SetBased {

        static int lengthOfLongestSubstring(String s) {

            int maxLen = 0;
            HashSet<Character> window = new HashSet<>();

            int left = 0, right = 0;

            while (right < s.length()) {
                char curr = s.charAt(right);

                // 🔴 Violation: duplicate detected
                while (window.contains(curr)) {
                    window.remove(s.charAt(left));
                    left++;
                }

                // 🟢 Invariant restored
                window.add(curr);
                right++;

                maxLen = Math.max(maxLen, right - left);
            }
            return maxLen;
        }
    }


    // =====================================================================
    // 🟣 INTERVIEW ARTICULATION
    // =====================================================================
    /*
    Why Optimal Works:
    Each character enters and exits the window once.

    Correctness Invariant:
    Window always contains unique characters.

    What Breaks If Changed:
    - Removing inner while → duplicates remain
    - Moving left backward → infinite loop

    Streaming Feasibility:
    Yes, can process characters sequentially.

    When NOT To Use:
    - Non-contiguous subsequences
    */

    // =====================================================================
    // 🔄 VARIATIONS & TWEAKS
    // =====================================================================
    /*
    🟢 Invariant-Preserving:
    - Use frequency array instead of HashSet

    🟡 Reasoning-Only:
    - Return substring instead of length

    🔴 Pattern-Break:
    - Reordering allowed → sliding window fails
    */

    // =====================================================================
    // 🧠 CRITICAL SLIDING WINDOW RULE — CHECK-BEFORE-ADD vs ADD-BEFORE-CHECK
    // =====================================================================
    /*
    This rule explains an extremely common source of confusion.

    Two different sliding window styles exist:
    1) Check-before-add (prospective violation handling)
    2) Add-before-check (reactive violation handling)

    They are NOT interchangeable.

    --------------------------------------------------
    RULE (LOCK THIS):
    --------------------------------------------------
    You may check-before-add ONLY if the violation condition
    depends solely on the incoming element.

    You MUST add-before-check if the violation depends on
    the aggregate window state.

    --------------------------------------------------
    WHY THIS MATTERS:
    --------------------------------------------------

    Case 1: Longest Substring Without Repeating Characters

        Invariant:
        Window must NOT contain the incoming character.

        Violation condition:
        set.contains(incomingChar)

        This depends ONLY on the incoming character.

        Therefore, it is VALID to:
        - check for violation first
        - shrink window if needed
        - then add the character

        Example:
            while (set.contains(ch)) {
                remove left
            }
            add ch

        This is called PROSPECTIVE violation handling.

    --------------------------------------------------
    Case 2: At Most K Distinct Characters

        Invariant:
        freq.size() <= k

        Violation condition:
        freq.size() > k

        This depends on the ENTIRE window state,
        which only changes AFTER adding the character.

        Therefore:
        - you CANNOT detect the violation before adding
        - you MUST add first, then fix if violated

        Example (CORRECT):
            add character
            while (freq.size() > k) {
                shrink
            }

        Doing shrink-before-add here is LOGICALLY UNSAFE.

    --------------------------------------------------
    SUMMARY TABLE:
    --------------------------------------------------

    Problem Type                                | Violation Depends On        | Order Required
    --------------------------------------------|-----------------------------|---------------
    No repeating characters                     | Incoming element only       | Check → Add
    At most K distinct                          | Whole window state          | Add → Fix
    Minimum window substring                   | Deficit after add           | Add → Fix
    Longest repeating character replacement     | Budget after add            | Add → Fix

    --------------------------------------------------
    ONE-LINE SAFETY RULE:
    --------------------------------------------------
    Any time you update the answer, the invariant
    MUST already hold.

    If you remember this rule, you will not make
    ordering mistakes in sliding window problems.
    */


    // =====================================================================
    // ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
    // =====================================================================

    /*
    Reinforcement 1:
    📘 Longest Substring with At Most K Distinct Characters
    🔗 https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/
    🧩 Difficulty: Medium
    🏷️ Tags: Sliding Window, Hash Map

    Description:
    Given a string s and an integer k, return the length of the longest
    substring that contains at most k distinct characters.

    Input: s = "eceba", k = 2
    Output: 3
    Explanation: "ece"
    */

    // 🔒 CANONICAL VERSION — MENTAL MODEL FIRST
// Pattern: Sliding Window
// Invariant: freq.size() == number of distinct characters in window
// Default choice for interviews & long-term retention

    static class AtMostKDistinct_MapBased {

        static int solve(String s, int k) {

            HashMap<Character, Integer> freq = new HashMap<>();
            int left = 0, right = 0, maxLen = 0;

            while (right < s.length()) {
                char curr = s.charAt(right);
                freq.put(curr, freq.getOrDefault(curr, 0) + 1);
                right++;

                // 🔴 Violation: too many distinct characters
                while (freq.size() > k) {
                    char out = s.charAt(left);
                    freq.put(out, freq.get(out) - 1);
                    if (freq.get(out) == 0) freq.remove(out);
                    left++;
                }

                // 🟢 Valid window
                maxLen = Math.max(maxLen, right - left);
            }
            return maxLen;
        }
    }

    // 🟡 SPECIALIZED VERSION — ARRAY + EXPLICIT COUNTER
// Derived from Map-based version
// Same invariant, optimized state representation
// Use when alphabet is fixed and performance matters

    static class AtMostKDistinct_ArrayBased {

        static int solve(String s, int k) {

            int[] windowCharCount = new int[128];
            int left = 0, right = 0;
            int distinctCharCount = 0, maxLen = 0;

            while (right < s.length()) {
                char curr = s.charAt(right);

                if (windowCharCount[curr] == 0)
                    distinctCharCount++;

                windowCharCount[curr]++;
                right++;

                // 🔴 Violation: too many distinct characters
                while (distinctCharCount > k) {
                    char removed = s.charAt(left);

                    if (windowCharCount[removed] == 1)
                        distinctCharCount--;

                    windowCharCount[removed]--;
                    left++;
                }

                maxLen = Math.max(maxLen, right - left);
            }
            return maxLen;
        }
    }


    // =====================================================================
    // 🧩 RELATED PROBLEMS (MINI SUB-CHAPTERS)
    // =====================================================================

   /*
    =====================================================================
    📘 PROBLEM: MINIMUM WINDOW SUBSTRING
    =====================================================================

    🔗 https://leetcode.com/problems/minimum-window-substring/
    🧩 Difficulty: Hard
    🏷️ Tags: String, Sliding Window, Hash Table

    ---------------------------------------------------------------------
    PROBLEM STATEMENT (OFFICIAL):

    Given two strings s and t, return the minimum window substring of s
    such that every character in t (including duplicates) is included
    in the window.

    If there is no such substring, return the empty string "".

    The testcases will be generated such that the answer is unique.

    ---------------------------------------------------------------------
    EXAMPLE 1:
    Input: s = "ADOBECODEBANC", t = "ABC"
    Output: "BANC"

    EXAMPLE 2:
    Input: s = "a", t = "a"
    Output: "a"

    EXAMPLE 3:
    Input: s = "a", t = "aa"
    Output: ""

    ---------------------------------------------------------------------
    CONSTRAINTS:
    1 <= s.length, t.length <= 10^5
    s and t consist of uppercase and lowercase English letters.
    */

    // =====================================================================
    // 🔵 CORE PATTERN OVERVIEW
    // =====================================================================
    /*
    Pattern Name:
    Sliding Window — Deficit Based

    Core Idea:
    Track how many required characters are still missing.
    Expand the window until all requirements are met.
    Then shrink greedily to minimize length.

    Why It Works:
    - Validity depends only on window contents
    - Violations can be fixed by shrinking
    - Each character enters and exits the window once

    When To Use:
    - Substring (contiguous)
    - Exact frequency requirements
    - “Smallest window containing …”

    Pattern Recognition Signals:
    - “Minimum window”
    - “Contains all characters”
    - Duplicates matter
    */

    // =====================================================================
    // 🟢 MENTAL MODEL & INVARIANT
    // =====================================================================
    /*
    Mental Model:
    Treat t as a shopping list.
    Each character has a required quantity.
    As you scan s:
      - Picking required items reduces the list
      - Dropping required items adds them back

    🟢 Core Invariant:
    count = number of required characters still missing

    Window validity:
    - count > 0  → invalid
    - count == 0 → valid

    Goal:
    Among all valid windows, keep the smallest one.
    */

    // =====================================================================
    // 🔴 WHY NAIVE SOLUTIONS FAIL
    // =====================================================================
    /*
    ❌ Brute force all substrings:
       O(n^3) → time limit exceeded

    ❌ Using Set instead of frequency:
       Fails when t has duplicate characters

    ❌ Restarting window on mismatch:
       Loses overlapping valid windows

    Interview Trap:
    “Why not restart when invalid?”
    → Because shrinking preserves valid progress
    */



    /*
        MENTAL MODEL — MINIMUM WINDOW SUBSTRING

        What do I need to track?
        → Number of required characters still missing.

        When does the window become valid?
        → When missing == 0.

        What happens when I add a character?
        → If it was needed, missing decreases.
        → Either way, the character is consumed.

        What happens when I remove a character?
        → If it becomes needed again, missing increases.

        When do I update the answer?
        → Only when the window is valid.

        If you remember only this block,
        you can re-derive the entire solution.
     */

    // =====================================================================
    // 🟢 PRIMARY PROBLEM — SOLUTION CLASS
    // =====================================================================

    static class MinimumWindowSubstring_Explicit {

        static String solve(String s, String t) {

            int[] need = new int[128];
            for (char c : t.toCharArray()) {
                need[c]++;
            }

            int left = 0, right = 0;
            int totalCharsLeftToMatch = t.length();
            int minLen = Integer.MAX_VALUE;
            int minStart = 0;

            while (right < s.length()) {

                char curr = s.charAt(right);

                // If this character was still needed,
                // we just made progress toward validity

                if (need[curr] > 0) {
                    totalCharsLeftToMatch--;
                }

                // Consume the character regardless:
                // negative values mean "extra", which is allowed
                need[curr]--;
                right++;

                while (totalCharsLeftToMatch == 0) {

                    if (right - left < minLen) {
                        minLen = right - left;
                        minStart = left;
                    }

                    char removed = s.charAt(left);
                    need[removed]++;

                    // We are undoing the effect of adding this character.
                    // If it becomes needed again, window turns invalid.
                    if (need[removed] > 0) {
                        totalCharsLeftToMatch++;
                    }
                    left++;
                }
            }

            return minLen == Integer.MAX_VALUE
                    ? ""
                    : s.substring(minStart, minStart + minLen);
        }
    }

    // =====================================================================
    // 🟣 INTERVIEW ARTICULATION
    // =====================================================================
    /*
    How to explain:

    “I use a sliding window and track how many required characters
    are still missing. I expand the window until all requirements
    are satisfied, then shrink it greedily to find the minimum window.
    Each character is processed at most twice.”

    Time Complexity: O(n)
    Space Complexity: O(1)
    */

    // =====================================================================
    // 🟢 LEARNING VERIFICATION
    // =====================================================================
    /*
    Mastery Check:
    - State invariant without code
    - Explain shrink condition
    - Detect sliding window applicability

    Intentional Bugs:
    - Remove inner while
    - Update answer before restoring invariant
    */

    // =====================================================================
// 🧪 main() METHOD + SELF-VERIFYING TESTS (UPDATED)
// =====================================================================
    public static void main(String[] args) {

    /*
    Test Case Group 1: Happy paths
    Why:
    - Standard examples
    - Verify core correctness
    */
        verifyAll("abcabcbb", 3);
        verifyAll("pwwkew", 3);
        verifyAll("bbbbb", 1);

    /*
    Test Case Group 2: Boundary cases
    Why:
    - Empty input
    - Single character
    - Minimal valid windows
    */
        verifyAll("", 0);
        verifyAll("a", 1);
        verifyAll("au", 2);

    /*
    Test Case Group 3: Interview traps
    Why:
    - Overlapping duplicates
    - Forces correct left-pointer movement
    */
        verifyAll("abba", 2);      // "ab" or "ba"
        verifyAll("dvdf", 3);      // "vdf"
        verifyAll("tmmzuxt", 5);   // "mzuxt"

    /*
    Test Case Group 4: Mixed characters
    Why:
    - Non-alphabet characters
    - Validates ASCII handling
    */
        verifyAll("a b!c a", 5);   // "b!c a"

        System.out.println("✅ ALL SLIDING WINDOW VARIANTS PASSED ALL TESTS");
    }

    /*
    Cross-verification helper:
    - Ensures all optimal implementations return the same correct answer
    - Prevents silent divergence between variants
    */
    private static void verifyAll(String input, int expected) {

        int ansSetBased =
                Optimal_SetBased.lengthOfLongestSubstring(input);

        int ansSeenArray =
                Optimal_SeenArray.lengthOfLongestSubstring(input);

        int ansExplicitInvariant =
                Optimal_ExplicitInvariant.lengthOfLongestSubstring(input);

        assertEquals(expected, ansSetBased,
                "Set-based failed for input: \"" + input + "\"");

        assertEquals(expected, ansSeenArray,
                "Seen-array failed for input: \"" + input + "\"");

        assertEquals(expected, ansExplicitInvariant,
                "Explicit-invariant failed for input: \"" + input + "\"");

        // 🔒 Strong guarantee: all implementations agree
        assertEquals(ansSetBased, ansSeenArray,
                "Mismatch between Set-based and Seen-array for input: \"" + input + "\"");

        assertEquals(ansSetBased, ansExplicitInvariant,
                "Mismatch between Set-based and Explicit-invariant for input: \"" + input + "\"");
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(
                    message + " | Expected: " + expected + ", Actual: " + actual
            );
        }
    }

    // =====================================================================
    // 🧠 CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
    // =====================================================================
    /*
    Invariant clarity
    → Window contains unique characters

    Search target clarity
    → Maximum length of valid substring

    Discard logic
    → Shrink left until duplicate removed

    Termination guarantee
    → Monotonic pointer movement

    Failure awareness
    → Restarting window discards valid progress

    Edge-case confidence
    → Empty and single-character strings handled naturally

    Variant readiness
    → Modify invariant (frequency / distinct count)

    Pattern boundary
    → Non-contiguous problems
    */

    /*
    🧘 FINAL CLOSURE STATEMENT

    For this problem, the invariant is that the sliding window
    always contains unique characters.
    The answer represents the maximum width of such a window.
    The search terminates because both pointers move forward.
    I can re-derive this solution under pressure.
    This chapter is complete.

    📌 If I can explain it, I don’t need to reread it.
    */
}

