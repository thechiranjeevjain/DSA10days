package org.chijai.day3.session1;

public class MinimumWindowSubstring {

    /*
     =====================================================================================
     📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
     =====================================================================================

     🔗 https://leetcode.com/problems/minimum-window-substring/

     🧩 Difficulty: Hard
     🏷️ Tags: Hash Table, String, Sliding Window

     -------------------------------------------------------------------------------------
     Given two strings s and t of lengths m and n respectively, return the minimum window
     substring of s such that every character in t (including duplicates) is included
     in the window. If there is no such substring, return the empty string "".

     The testcases will be generated such that the answer is unique.

     Example 1:
     Input: s = "ADOBECODEBANC", t = "ABC"
     Output: "BANC"
     Explanation: The minimum window substring "BANC" includes 'A', 'B', and 'C'
     from string t.

     Example 2:
     Input: s = "a", t = "a"
     Output: "a"
     Explanation: The entire string s is the minimum window.

     Example 3:
     Input: s = "a", t = "aa"
     Output: ""
     Explanation: Both 'a's from t must be included in the window.
     Since the largest window of s only has one 'a', return empty string.

     Constraints:
     m == s.length
     n == t.length
     1 <= m, n <= 10^5
     s and t consist of uppercase and lowercase English letters.

     Follow up: Could you find an algorithm that runs in O(m + n) time?
     */

    /*
     =====================================================================================
     🔵 CORE PATTERN OVERVIEW
     =====================================================================================

     Pattern Name:
     🔵 Sliding Window with Frequency Accounting

     Core Idea:
     🔵 Maintain a window [left, right) that expands to satisfy all requirements,
        then contracts to minimize length while preserving validity.

     Why It Works:
     🔵 Every character entering or leaving the window updates a tracked invariant.
        No character is processed more than twice.

     When To Use:
     🔵 Problems asking for:
        - Minimum / maximum substring
        - All characters must be included
        - Duplicates matter
        - Large input size (10^5)

     🧭 Pattern Recognition Signals:
     🔵 “minimum window”
     🔵 “substring”
     🔵 “contains all characters”
     🔵 “including duplicates”

     How This Differs From Similar Patterns:
     🔵 Unlike two-pointer scans on sorted data, window validity depends on
        **frequency state**, not order.
     */

    /*
     =====================================================================================
     🟢 MENTAL MODEL & INVARIANTS
     =====================================================================================

     Mental Model:
     🟢 Treat string t as a multiset of required characters.
     🟢 The window consumes characters from s.
     🟢 Track exactly how many characters are still missing.

     Core Invariant:
     🟢 missingCharacters == 0  ⇔  current window is VALID

     Variable Roles:
     🟢 leftPointer:
         - start of current window
     🟢 rightPointer:
         - end of current window (exclusive)
     🟢 requiredFrequency[char]:
         - how many more times this character is still needed
     🟢 missingCharacters:
         - total number of characters (including duplicates) still unmet
     🟢 bestWindowStart:
         - start index of best (minimum) valid window found so far
     🟢 bestWindowLength:
         - length of best valid window

     Termination Logic:
     🟢 rightPointer only moves forward → O(n)
     🟢 leftPointer only moves forward → O(n)
     🟢 Loop must terminate because pointers never retreat

     Forbidden Actions:
     🔴 Shrinking the window before it becomes valid
     🔴 Tracking only distinct characters instead of total required
     🔴 Resetting frequency arrays mid-scan

     Why Common Alternatives Are Inferior:
     🔴 Brute force → exponential rechecking
     🔴 HashSet logic → breaks on duplicates
     🔴 Recomputing validity → unnecessary O(n^2)
     */

    /*
     =====================================================================================
     🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
     =====================================================================================

     ❌ Naive Approach #1: Distinct-character tracking

     Why It Seems Correct:
     🔴 If all characters of t appear at least once, the window “looks valid”.

     What It Violates:
     🔴 Invariant ignores multiplicity.
        t = "AA" requires two A’s, not one.

     Counterexample:
     🔴 s = "A"
     🔴 t = "AA"
     🔴 Distinct logic passes incorrectly.

     --------------------------------------------------

     ❌ Naive Approach #2: Incorrect shrinking logic (SUBTLE BUG)

     Why It Seems Correct:
     🔴 Candidate shrinks window as long as it “looks valid”.

     Exact Invariant Broken:
     🔴 Fails to restore missingCharacters **exactly when**
        a required character exits the window.

     Concrete Failure Case:
     🔴 s = "cabwefgewcwaefgcf"
     🔴 t = "cae"

     What Goes Wrong:
     🔴 Window becomes valid.
     🔴 Shrinking removes a required character.
     🔴 But validity flag is not updated correctly.
     🔴 Resulting window is incorrectly accepted.

     Interviewer Trap:
     🔴 This case is designed to test whether you
        understand **why** the window is valid,
        not just **that** it was once valid.
     */

    /*
     =====================================================================================
     🧪 PRIMARY PROBLEM — SOLUTION CLASSES
     =====================================================================================
     */

    /* --------------------------------------------------
       🟥 BRUTE FORCE SOLUTION
       -------------------------------------------------- */
    static class BruteForce {

        /*
         🔵 Core Idea:
         Try every possible substring of s and check if it covers t.

         🟡 What Limitation It Fixes:
         None. This is the baseline for correctness only.

         ⏱ Time Complexity:
         O(n^3) — all substrings + frequency check

         🧠 Space Complexity:
         O(1) — fixed alphabet

         🟣 Interview Preference:
         ❌ Never acceptable beyond correctness discussion
         */

        public String minWindow(String s, String t) {
            int bestLen = Integer.MAX_VALUE;
            int bestStart = 0;

            for (int start = 0; start < s.length(); start++) {
                for (int end = start + 1; end <= s.length(); end++) {

                    // 🟡 Check validity from scratch (expensive)
                    if (coversAll(s, start, end, t)) {
                        if (end - start < bestLen) {
                            bestLen = end - start;
                            bestStart = start;
                        }
                    }
                }
            }

            return bestLen == Integer.MAX_VALUE
                    ? ""
                    : s.substring(bestStart, bestStart + bestLen);
        }

        private boolean coversAll(String s, int start, int end, String t) {
            int[] freq = new int[128];

            // 🔵 Count window characters
            for (int i = start; i < end; i++) {
                freq[s.charAt(i)]++;
            }

            // 🔴 Consume t requirements
            for (int i = 0; i < t.length(); i++) {
                char c = t.charAt(i);
                if (freq[c] == 0) return false;
                freq[c]--;
            }

            return true;
        }
    }

    /* --------------------------------------------------
       🟢 OPTIMAL SOLUTION (INTERVIEW-PREFERRED)
       -------------------------------------------------- */
    static class Optimal {

        /*
         🔵 Core Idea:
         Sliding window + exact unmet demand counter.

         🟡 What Limitation It Fixes:
         Removes repeated validity scans.

         ⏱ Time Complexity:
         O(m + n)

         🧠 Space Complexity:
         O(1)

         🟣 Interview Preference:
         ✅ YES — gold standard
         */

        public String minWindow(String s, String t) {

            int left = 0;
            int right = 0;

            int minLen = Integer.MAX_VALUE;
            int startOfMinWindow = 0;

            // 🟢 total characters (including duplicates) still needed
            int totalCharsLeftToMatch = t.length();

            // 🟢 needed[c] > 0  → still required
            // 🟢 needed[c] <= 0 → extra or exactly satisfied
            int[] needed = new int[128];

            for (char c : t.toCharArray()) {
                needed[c]++;
            }

            while (right < s.length()) {

                char enteringChar = s.charAt(right);

                // 🟢 Progress only if this character was still needed
                if (needed[enteringChar] > 0) {
                    totalCharsLeftToMatch--;
                }

                // Consume it regardless
                needed[enteringChar]--;
                right++;

                // 🟢 Window is VALID → try shrinking
                while (totalCharsLeftToMatch == 0) {

                    // 🔒 Record LAST valid window before invalidation
                    if (right - left < minLen) {
                        minLen = right - left;
                        startOfMinWindow = left;
                    }

                    char exitingChar = s.charAt(left);

                    // Restore requirement for exiting char
                    needed[exitingChar]++;

                    // 🔴 Window becomes INVALID exactly here
                    if (needed[exitingChar] > 0) {
                        totalCharsLeftToMatch++;
                    }

                    left++;
                }
            }

            return minLen == Integer.MAX_VALUE
                    ? ""
                    : s.substring(startOfMinWindow, startOfMinWindow + minLen);
        }
    }

        /*
     =====================================================================================
     🟣 INTERVIEW ARTICULATION (PRIMARY PROBLEM)
     =====================================================================================

     🟣 Why the Optimal Solution Works:
     The algorithm maintains a strict invariant:
     → missingCharacters == 0 iff the window satisfies all requirements of t.
     Every character entering or leaving the window updates this invariant
     exactly once.

     🟣 Correctness Invariant:
     The window [leftPointer, rightPointer) is valid if and only if
     all required characters (with duplicates) have been consumed.

     🟣 What Breaks If Changed:
     - Counting only distinct characters breaks duplicate handling.
     - Shrinking before missingCharacters == 0 leads to false positives.
     - Not restoring missingCharacters when removing a required char
       causes invalid windows to be accepted.

     🟣 In-Place Feasibility:
     Yes. Uses fixed-size frequency array.

     🟣 Streaming Feasibility:
     Yes. Right pointer advances monotonically.

     🟣 When NOT To Use This Pattern:
     - When order matters (subsequence problems)
     - When skipping characters is allowed
     - When window size is not contiguous
     */

    /*
     =====================================================================================
     🔄 VARIATIONS & TWEAKS
     =====================================================================================

     🟢 Invariant-Preserving Changes:
     - Different alphabets (ASCII → Unicode map)
     - Case-insensitive matching
     - Returning indices instead of substring

     🟡 Reasoning-Only Changes:
     - Track window count instead of missingCharacters
     - Use two maps instead of array

     🔴 Pattern-Break Signals:
     - “subsequence” instead of “substring”
     - “relative order must be preserved”
     - “can skip characters freely”

     🟣 Why Pattern Must Be Abandoned:
     Sliding window relies on contiguous structure.
     Once contiguity is broken, invariants collapse.
     */

    /*
     =====================================================================================
     ⚫ REINFORCEMENT PROBLEM 1 — FULL SUB-CHAPTER
     =====================================================================================

     📘 PROBLEM: Longest Substring Without Repeating Characters
     🔗 https://leetcode.com/problems/longest-substring-without-repeating-characters/

     🧩 Difficulty: Medium
     🏷️ Tags: Hash Table, String, Sliding Window

     -------------------------------------------------------------------------------------
     Given a string s, find the length of the longest substring without repeating characters.

     Example 1:
     Input: s = "abcabcbb"
     Output: 3
     Explanation: The answer is "abc", with the length of 3.

     Example 2:
     Input: s = "bbbbb"
     Output: 1
     Explanation: The answer is "b", with the length of 1.

     Example 3:
     Input: s = "pwwkew"
     Output: 3
     Explanation: The answer is "wke", with the length of 3.

     Constraints:
     0 <= s.length <= 5 * 10^4
     s consists of English letters, digits, symbols and spaces.
     */

    /*
     =====================================================================================
     🧠 PATTERN MAPPING (REINFORCEMENT 1)
     =====================================================================================

     ⚫ Why This Is the Same Pattern:
     Sliding window with invariant enforcement.

     ⚫ Invariant:
     Window contains no duplicate characters.

     ⚫ What Changes:
     Instead of “missingCharacters”, we track violation count (duplicates).

     ⚫ Why It Still Works:
     Window validity depends only on frequency state.
     */

    static class LongestSubstringWithoutRepeating {

        /*
         🔵 Core Idea:
         Expand until duplicate appears, then shrink until resolved.

         ⏱ Time Complexity: O(n)
         🧠 Space Complexity: O(1)
         */

        public int lengthOfLongestSubstring(String s) {

            int[] freq = new int[128];
            int left = 0;
            int best = 0;

            for (int right = 0; right < s.length(); right++) {

                char entering = s.charAt(right);
                freq[entering]++;

                // 🔴 Violation: duplicate detected
                while (freq[entering] > 1) {
                    char exiting = s.charAt(left);
                    freq[exiting]--;
                    left++;
                }

                // 🟢 Window valid
                best = Math.max(best, right - left + 1);
            }

            return best;
        }
    }

    /*
     =====================================================================================
     🧪 EDGE CASES & TRAPS (REINFORCEMENT 1)
     =====================================================================================

     🔴 Edge Case:
     s = "" → output 0

     🔴 Why Naive Fails:
     Restarting window loses linear guarantee.

     🟣 Interview Trap:
     Candidates forget to shrink until violation resolves.
     */

    /*
     =====================================================================================
     🟣 INTERVIEW ARTICULATION (REINFORCEMENT 1)
     =====================================================================================

     🟣 Explanation:
     “I maintain a window with no duplicates.
      If a duplicate appears, I shrink until invariant holds again.”

     🟣 Follow-Up:
     - Return substring instead of length
     - Unicode handling
     */

    /*
     =====================================================================================
     ⚫ REINFORCEMENT PROBLEM 2 — FULL SUB-CHAPTER
     =====================================================================================

     📘 PROBLEM: Permutation in String
     🔗 https://leetcode.com/problems/permutation-in-string/

     🧩 Difficulty: Medium
     🏷️ Tags: Hash Table, String, Sliding Window

     -------------------------------------------------------------------------------------
     Given two strings s1 and s2, return true if s2 contains a permutation of s1,
     or false otherwise.

     In other words, return true if one of s1's permutations is the substring of s2.

     Example 1:
     Input: s1 = "ab", s2 = "eidbaooo"
     Output: true
     Explanation: s2 contains one permutation of s1 ("ba").

     Example 2:
     Input: s1 = "ab", s2 = "eidboaoo"
     Output: false

     Constraints:
     1 <= s1.length, s2.length <= 10^4
     s1 and s2 consist of lowercase English letters.
     */

    /*
     =====================================================================================
     🧠 PATTERN MAPPING (REINFORCEMENT 2)
     =====================================================================================

     ⚫ Why This Is the Same Pattern:
     Fixed-size sliding window with frequency accounting.

     ⚫ Invariant:
     Window length == s1.length AND all frequencies match.

     ⚫ What Changes:
     Window size is fixed, not variable.

     ⚫ Why Pattern Still Applies:
     Validity depends purely on frequency equality.
     */

    static class PermutationInString {

        public boolean checkInclusion(String s1, String s2) {

            if (s1.length() > s2.length()) return false;

            int[] required = new int[26];
            for (char c : s1.toCharArray()) {
                required[c - 'a']++;
            }

            int left = 0;
            int missing = s1.length();

            for (int right = 0; right < s2.length(); right++) {

                char entering = s2.charAt(right);
                if (required[entering - 'a'] > 0) {
                    missing--;
                }
                required[entering - 'a']--;

                // 🔵 Enforce fixed window size
                if (right - left + 1 > s1.length()) {
                    char exiting = s2.charAt(left);
                    if (required[exiting - 'a'] >= 0) {
                        missing++;
                    }
                    required[exiting - 'a']++;
                    left++;
                }

                // 🟢 Valid permutation found
                if (missing == 0) return true;
            }

            return false;
        }
    }

    /*
     =====================================================================================
     🧪 EDGE CASES & TRAPS (REINFORCEMENT 2)
     =====================================================================================

     🔴 Edge Case:
     s1 longer than s2 → immediate false

     🔴 Why Naive Fails:
     Sorting substrings costs O(n log n)

     🟣 Interview Trap:
     Forgetting to restore missing count on exit
     */

    /*
     =====================================================================================
     🟣 INTERVIEW ARTICULATION (REINFORCEMENT 2)
     =====================================================================================

     🟣 Explanation:
     “I slide a fixed window and track exact frequency balance.”

     🟣 Follow-Up:
     - Unicode extension
     - Return index instead of boolean
     */

    /*
     =====================================================================================
     ⚫ REINFORCEMENT PROBLEM 3 — FULL SUB-CHAPTER
     =====================================================================================

     📘 PROBLEM: Minimum Size Subarray Sum
     🔗 https://leetcode.com/problems/minimum-size-subarray-sum/

     🧩 Difficulty: Medium
     🏷️ Tags: Array, Binary Search, Sliding Window

     -------------------------------------------------------------------------------------
     Given an array of positive integers nums and a positive integer target,
     return the minimal length of a contiguous subarray of which the sum is
     greater than or equal to target. If there is no such subarray, return 0.

     Example 1:
     Input: target = 7, nums = [2,3,1,2,4,3]
     Output: 2
     Explanation: The subarray [4,3] has the minimal length.

     Example 2:
     Input: target = 4, nums = [1,4,4]
     Output: 1

     Example 3:
     Input: target = 11, nums = [1,1,1,1,1,1,1,1]
     Output: 0

     Constraints:
     1 <= target <= 10^9
     1 <= nums.length <= 10^5
     1 <= nums[i] <= 10^5
     */

    /*
     =====================================================================================
     🧠 PATTERN MAPPING (REINFORCEMENT 3)
     =====================================================================================

     ⚫ Why This Is the Same Pattern:
     Sliding window minimizing size once invariant is met.

     ⚫ Invariant:
     Window sum >= target.

     ⚫ What Changes:
     Numeric sum instead of frequency array.

     ⚫ Why Pattern Still Applies:
     Validity still monotonic with expansion and contraction.
     */

    static class MinimumSizeSubarraySum {

        public int minSubArrayLen(int target, int[] nums) {

            int left = 0;
            int sum = 0;
            int best = Integer.MAX_VALUE;

            for (int right = 0; right < nums.length; right++) {

                sum += nums[right];

                // 🟢 Window valid → try shrinking
                while (sum >= target) {
                    best = Math.min(best, right - left + 1);
                    sum -= nums[left];
                    left++;
                }
            }

            return best == Integer.MAX_VALUE ? 0 : best;
        }
    }

    /*
     =====================================================================================
     🧪 EDGE CASES & TRAPS (REINFORCEMENT 3)
     =====================================================================================

     🔴 Edge Case:
     No valid subarray → return 0

     🔴 Why Naive Fails:
     O(n^2) checking all subarrays

     🟣 Interview Trap:
     Forgetting positivity assumption breaks sliding window
     */

    /*
     =====================================================================================
     🟣 INTERVIEW ARTICULATION (REINFORCEMENT 3)
     =====================================================================================

     🟣 Explanation:
     “Because all numbers are positive, expanding increases sum monotonically,
      so sliding window is safe.”

     🟣 Follow-Up:
     - What if negatives allowed? → pattern breaks
     */


    /*
     =====================================================================================
     🧩 RELATED PROBLEMS (UPGRADED → MINI SUB-CHAPTERS)
     =====================================================================================
     */

    /*
     =====================================================================================
     🧩 RELATED PROBLEM 1
     =====================================================================================

     📘 PROBLEM: Substring with Concatenation of All Words
     🔗 https://leetcode.com/problems/substring-with-concatenation-of-all-words/

     🧩 Difficulty: Hard
     🏷️ Tags: Hash Table, String, Sliding Window

     -------------------------------------------------------------------------------------
     You are given a string s and an array of strings words. All the strings of words
     are of the same length.

     A concatenated substring in s is a substring that contains all the strings of any
     permutation of words concatenated.

     Return all the starting indices of concatenated substrings in s.

     Constraints:
     1 <= s.length <= 10^4
     1 <= words.length <= 5000
     1 <= words[i].length <= 30
     */

    /*
     🧠 RELATIONSHIP TO PRIMARY PATTERN
     -------------------------------------------------------------------------------------
     ⚫ Same pattern (sliding window + frequency accounting)
     ⚫ Boundary variant: fixed chunk size, multiple offsets

     Why it still works:
     - Window validity still depends on frequency balance
     - Just segmented by word length
     */

    static class SubstringConcatenation {

        public java.util.List<Integer> findSubstring(String s, String[] words) {

            java.util.List<Integer> result = new java.util.ArrayList<>();
            if (words.length == 0) return result;

            int wordLen = words[0].length();
            int totalWords = words.length;
            int windowLen = wordLen * totalWords;

            java.util.Map<String, Integer> required = new java.util.HashMap<>();
            for (String w : words) {
                required.put(w, required.getOrDefault(w, 0) + 1);
            }

            for (int offset = 0; offset < wordLen; offset++) {

                int left = offset;
                int count = 0;
                java.util.Map<String, Integer> window = new java.util.HashMap<>();

                for (int right = offset; right + wordLen <= s.length(); right += wordLen) {

                    String word = s.substring(right, right + wordLen);

                    if (!required.containsKey(word)) {
                        window.clear();
                        count = 0;
                        left = right + wordLen;
                        continue;
                    }

                    window.put(word, window.getOrDefault(word, 0) + 1);
                    count++;

                    while (window.get(word) > required.get(word)) {
                        String leftWord = s.substring(left, left + wordLen);
                        window.put(leftWord, window.get(leftWord) - 1);
                        left += wordLen;
                        count--;
                    }

                    if (count == totalWords) {
                        result.add(left);
                    }
                }
            }

            return result;
        }
    }

    /*
     =====================================================================================
     🧩 RELATED PROBLEM 2
     =====================================================================================

     📘 PROBLEM: Longest Repeating Character Replacement
     🔗 https://leetcode.com/problems/longest-repeating-character-replacement/

     🧩 Difficulty: Medium
     🏷️ Tags: Sliding Window, String

     -------------------------------------------------------------------------------------
     You are given a string s and an integer k.
     You can choose any character of the string and change it to any other uppercase
     English character. You can perform this operation at most k times.

     Return the length of the longest substring containing the same letter you can get
     after performing the above operations.
     */

    /*
     🧠 RELATIONSHIP TO PRIMARY PATTERN
     -------------------------------------------------------------------------------------
     ⚫ Same sliding window
     ⚫ Validity depends on maxFrequency instead of missingCharacters
     */

    static class CharacterReplacement {

        public int characterReplacement(String s, int k) {

            int[] freq = new int[26];
            int left = 0;
            int maxFreq = 0;
            int best = 0;

            for (int right = 0; right < s.length(); right++) {

                char c = s.charAt(right);
                freq[c - 'A']++;
                maxFreq = Math.max(maxFreq, freq[c - 'A']);

                // 🔴 Window invalid if replacements exceed k
                while ((right - left + 1) - maxFreq > k) {
                    freq[s.charAt(left) - 'A']--;
                    left++;
                }

                best = Math.max(best, right - left + 1);
            }

            return best;
        }
    }

    /*
     =====================================================================================
     🟢 LEARNING VERIFICATION
     =====================================================================================

     ✔ Invariants to recall without code:
     - missingCharacters == 0 means valid window
     - shrink only when invariant holds

     ✔ Bugs to intentionally debug:
     - Forgetting to restore missingCharacters
     - Shrinking before validity
     - Counting distinct instead of total

     ✔ Pattern detection in unseen problems:
     - Keywords: substring, minimum, all characters, duplicates
     - Constraints allow O(n)
     */

    /*
     =====================================================================================
     🧪 main() METHOD + SELF-VERIFYING TESTS
     =====================================================================================
     */

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        assertEquals(
                "BANC",
                solver.minWindow("ADOBECODEBANC", "ABC"),
                "Classic example"
        );

        assertEquals(
                "a",
                solver.minWindow("a", "a"),
                "Single char exact match"
        );

        assertEquals(
                "",
                solver.minWindow("a", "aa"),
                "Impossible duplicate requirement"
        );

        assertEquals(
                "cwae",
                solver.minWindow("cabwefgewcwaefgcf", "cae"),
                "Interviewer trap case"
        );

        System.out.println("✅ ALL TESTS PASSED — FILE VERIFIED");
    }

    private static void assertEquals(String expected, String actual, String reason) {
        if (!expected.equals(actual)) {
            throw new RuntimeException(
                    "❌ Test failed: " + reason +
                            "\nExpected: " + expected +
                            "\nActual: " + actual
            );
        }
    }

    /*
     =====================================================================================
     🧠 CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
     =====================================================================================

     Invariant clarity
     → Answer: missingCharacters == 0 means window satisfies t completely

     Search target clarity
     → Answer: shortest contiguous substring covering all required characters

     Discard logic
     → Answer: shrink window only when invariant holds

     Termination guarantee
     → Answer: left and right pointers move monotonically forward

     Failure awareness
     → Answer: incorrect shrinking or ignoring duplicates breaks validity

     Edge-case confidence
     → Answer: empty result when no valid window exists

     Variant readiness
     → Answer: invariant adapts (sum, maxFreq, exact match)

     Pattern boundary
     → Answer: fails for subsequence or negative-number windows
     */

    /*
     🧘 FINAL CLOSURE STATEMENT

     For this problem, the invariant is missingCharacters == 0.
     The answer represents the smallest valid window.
     The search terminates because both pointers only move forward.
     I can re-derive this solution under pressure.
     This chapter is complete.

     📌 If I can explain it, I don’t need to reread it.
     */
}


