package org.chijai.day3.session1;

/**
 * ============================================================
 * 📘 LONGEST SUBSTRING WITH AT MOST K DISTINCT CHARACTERS
 * ============================================================
 *
 * SINGLE CONSOLIDATED JAVA CHAPTER FILE
 * ------------------------------------------------------------
 * • IntelliJ-ready
 * • Self-contained
 * • Offline-solvable
 * • One public class only
 * • All other classes are static inner classes
 *
 * This file is a COMPLETE algorithm chapter.
 * Not notes. Not snippets. Not shortcuts.
 *
 * ============================================================
 */
public class AtMostKDistinct {

    /*
     * ============================================================
     * 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
     * ============================================================
     *
     * 🔗 Link:
     * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/
     *
     * 🧩 Difficulty:
     * Medium
     *
     * 🏷️ Tags:
     * Sliding Window, Hash Map, Two Pointers, String
     *
     * ------------------------------------------------------------
     * Description:
     *
     * Given a string s and an integer k, return the length of the longest
     * substring of s that contains at most k distinct characters.
     *
     * ------------------------------------------------------------
     * Example 1:
     *
     * Input: s = "eceba", k = 2
     * Output: 3
     * Explanation: The substring is "ece" with length 3.
     *
     * ------------------------------------------------------------
     * Example 2:
     *
     * Input: s = "aa", k = 1
     * Output: 2
     * Explanation: The substring is "aa" with length 2.
     *
     * ------------------------------------------------------------
     * Constraints:
     *
     * • 1 <= s.length <= 5 * 10^4
     * • 0 <= k <= 50
     * • s consists of English letters, digits, symbols and spaces.
     *
     * ------------------------------------------------------------
     */

    /*
     * ============================================================
     * 3️⃣ 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern Name:
     * Fixed-Constraint Sliding Window
     *
     * ------------------------------------------------------------
     * Core Idea:
     * Maintain a window that always satisfies:
     * “at most k distinct characters”.
     *
     * Expand to explore.
     * Shrink to restore validity.
     *
     * ------------------------------------------------------------
     * Why It Works:
     * The constraint is monotonic:
     * • Adding characters can only violate the constraint
     * • Removing characters can only restore it
     *
     * ------------------------------------------------------------
     * When to Use:
     * • Longest / shortest substring
     * • At most / at least constraints
     * • Contiguous sequences
     *
     * ------------------------------------------------------------
     * 🧭 Pattern Recognition Signals:
     * • Substring (continuous)
     * • Constraint on count / frequency
     * • “At most”, “At least”, “Exactly”
     *
     * ------------------------------------------------------------
     * Difference from Similar Patterns:
     * • Unlike fixed-size window → size is dynamic
     * • Unlike prefix sum → locality matters
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 4️⃣ 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * 🟢 Mental Model:
     * A flexible window that stretches right to gain candidates,
     * and contracts left ONLY when invalid.
     *
     * ------------------------------------------------------------
     * 🟢 Invariant:
     * At ALL times after the inner while-loop:
     * freq.size() <= k
     *
     * ------------------------------------------------------------
     * 🟢 Variable Roles:
     *
     * left  → start of window
     * right → end of window (exclusive)
     * freq  → frequency map of current window
     * maxLen → best valid window seen so far
     *
     * ------------------------------------------------------------
     * 🟢 Termination Logic:
     * right strictly increases → O(n)
     * left never moves backward
     *
     * ------------------------------------------------------------
     * ❌ Forbidden Actions:
     * • Shrinking when already valid
     * • Updating answer before restoring invariant
     *
     * ------------------------------------------------------------
     * Why Alternatives Are Inferior:
     * • Brute force → O(n²)
     * • Restarting window → loses optimal overlaps
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 5️⃣ 🔴 WHY NAIVE SOLUTIONS FAIL (FORENSIC ANALYSIS)
     * ============================================================
     *
     * ❌ Wrong Approach 1:
     * Check all substrings
     * → Violates time constraints
     *
     * ❌ Wrong Approach 2:
     * Reset window when invalid
     * → Misses overlapping optimal windows
     *
     * ❌ Interview Trap:
     * Updating max length BEFORE shrinking
     * → Counts invalid windows
     *
     * Counterexample:
     * s = "abac", k = 2
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES
     * ============================================================
     */

    /* ------------------------------------------------------------
     * 🔹 Brute Force Solution
     * ------------------------------------------------------------
     */
    static class BruteForceSolution {
        // Core idea: check all substrings
        // Time: O(n^2)
        // Space: O(k)
        // Interview: ❌ Never preferred
        public int solve(String s, int k) {
            int maxLen = 0;
            for (int start = 0; start < s.length(); start++) {
                java.util.Set<Character> set = new java.util.HashSet<>();
                for (int end = start; end < s.length(); end++) {
                    set.add(s.charAt(end));
                    if (set.size() > k) break;
                    maxLen = Math.max(maxLen, end - start + 1);
                }
            }
            return maxLen;
        }
    }

    /* ------------------------------------------------------------
     * 🔹 Improved Solution
     * ------------------------------------------------------------
     */
    static class ImprovedSolution {
        // Uses sliding window but recalculates counts inefficiently
        // Time: O(n^2)
        // Interview: ❌ Partial credit only
        public int solve(String s, int k) {
            int left = 0, maxLen = 0;
            while (left < s.length()) {
                java.util.Map<Character, Integer> freq = new java.util.HashMap<>();
                for (int right = left; right < s.length(); right++) {
                    freq.put(s.charAt(right), freq.getOrDefault(s.charAt(right), 0) + 1);
                    if (freq.size() > k) break;
                    maxLen = Math.max(maxLen, right - left + 1);
                }
                left++;
            }
            return maxLen;
        }
    }

    /* ------------------------------------------------------------
     * 🔹 Optimal Solution (Interview-Preferred)
     * ------------------------------------------------------------
     */
    static class OptimalSolution {
        // Time: O(n)
        // Space: O(k)
        public int kDistinctChar(String s, int k) {

            if (k == 0 || s.isEmpty()) return 0;

            java.util.Map<Character, Integer> freq = new java.util.HashMap<>();

            int left = 0;
            int right = 0;
            int maxLen = 0;

            while (right < s.length()) {

                // 🔵 Expand window
                char current = s.charAt(right);
                freq.put(current, freq.getOrDefault(current, 0) + 1);
                right++;

                // 🔴 Shrink until invariant restored
                while (freq.size() > k) {
                    char leftChar = s.charAt(left);
                    freq.put(leftChar, freq.get(leftChar) - 1);
                    if (freq.get(leftChar) == 0) {
                        freq.remove(leftChar);
                    }
                    left++;
                }

                // 🟢 Valid window → update answer
                maxLen = Math.max(maxLen, right - left);
            }

            return maxLen;
        }
    }

    static class Optimal_AtMostKDistinct {

    /*
    ---------------------------------------------------------------------------
    Mental Model
    ---------------------------------------------------------------------------

    Expand the window by adding one character.

    If the window contains more than K distinct characters,
    shrink it until the invariant is restored.

    Every valid window is a candidate answer.

            Expand
               ↓
       Distinct <= K ?
          /        \
        Yes        No
         |          |
    Update Answer  Shrink
                    ↓
             Restore Invariant

    ---------------------------------------------------------------------------
    Invariant
    ---------------------------------------------------------------------------

    frequency[c]
        Number of occurrences of character c inside the current window.

    distinctCount
        Number of distinct characters currently inside the window.

    Window is VALID iff

        distinctCount <= k

    ---------------------------------------------------------------------------
    State Transitions
    ---------------------------------------------------------------------------

    Character enters:

        frequency[c]++

        If frequency becomes 1,
        a new distinct character has entered the window.

    Character leaves:

        frequency[c]--

        If frequency becomes 0,
        one distinct character has completely left the window.
    ---------------------------------------------------------------------------
    */

        static int lengthOfLongestSubstringAtMostKDistinct(String s, int k) {

            if (k == 0 || s.isEmpty())
                return 0;

            int[] frequency = new int[128];

            int left = 0;
            int right = 0;

            int distinctCount = 0;
            int maxWindowLength = 0;

            while (right < s.length()) {

                // 🔵 Expand window
                char enteringChar = s.charAt(right);

                frequency[enteringChar]++;

                if (frequency[enteringChar] == 1)
                    distinctCount++;

                right++;

                // 🔴 Restore invariant
                while (distinctCount > k) {

                    char leavingChar = s.charAt(left);

                    frequency[leavingChar]--;

                    if (frequency[leavingChar] == 0)
                        distinctCount--;

                    left++;
                }

                // 🟢 Valid window
                maxWindowLength = Math.max(maxWindowLength, right - left);
            }

            return maxWindowLength;
        }
    }

    /*
     * ============================================================
     * 7️⃣ 🟣 INTERVIEW ARTICULATION
     * ============================================================
     *
     * Why it works:
     * The window only grows when valid, shrinks only when invalid.
     *
     * Correctness invariant:
     * freq.size() <= k
     *
     * What breaks if changed:
     * Updating answer before shrinking counts invalid windows.
     *
     * In-place feasibility:
     * Yes (map size ≤ k)
     *
     * Streaming feasibility:
     * Yes (single pass)
     *
     * When NOT to use:
     * Non-contiguous or non-monotonic constraints
     *
     * Whiteboard explanation:
     * “Expand → Violate → Shrink → Restore → Record”
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 8️⃣ 🔄 VARIATIONS & TWEAKS
     * ============================================================
     *
     * 🟢 Invariant-preserving:
     * • Longest substring with at most K vowels
     *
     * 🟡 Reasoning-only:
     * • Return substring instead of length
     *
     * 🔴 Pattern-break:
     * • Longest subsequence → not contiguous
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 9️⃣ ⚫ REINFORCEMENT PROBLEMS
     * ============================================================
     *
     * (Example shown: others omitted for brevity)
     *
     * Longest Substring Without Repeating Characters
     * (Same pattern, k = freq <= 1)
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 11️⃣ 🟢 LEARNING VERIFICATION
     * ============================================================
     *
     * • Recall invariant without code
     * • Debug by forcing freq.size() > k
     * • Detect via “longest substring + constraint”
     *
     * ============================================================
     */

    /*
     * ============================================================
     * 12️⃣ 🧪 main() METHOD + SELF-VERIFYING TESTS
     * ============================================================
     */
    public static void main(String[] args) {
        OptimalSolution solution = new OptimalSolution();

        // Happy path
        assert solution.kDistinctChar("aababbcaacc", 2) == 6 : "Failed Example 1";

        // Boundary
        assert solution.kDistinctChar("", 2) == 0 : "Failed empty string";

        // Interview trap
        assert solution.kDistinctChar("abccab", 4) == 6 : "Failed full coverage";

        System.out.println("All tests passed ✔");
    }

    /*
     * ============================================================
     * 13️⃣ 🧠 CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
     * ============================================================
     *
     * • Invariant clarity
     * → freq.size() <= k
     *
     * • Search target clarity
     * → Longest valid contiguous window
     *
     * • Discard logic
     * → Shrink when distinct chars exceed k
     *
     * • Termination guarantee
     * → right increases monotonically
     *
     * • Failure awareness
     * → Naive resets lose overlap
     *
     * • Edge-case confidence
     * → Handles empty, k=0, full coverage
     *
     * • Variant readiness
     * → Replace constraint logic
     *
     * • Pattern boundary
     * → Non-contiguous problems
     *
     * ============================================================
     */

    /*
     * 🧘 FINAL CLOSURE STATEMENT
     *
     * For this problem, the invariant is:
     * “The window always contains at most k distinct characters.”
     *
     * The answer represents the maximum length of such a window.
     * The search terminates because pointers move forward only.
     * I can re-derive this solution under pressure.
     *
     * This chapter is complete.
     *
     * 📌 If I can explain it, I don’t need to reread it.
     */
}
