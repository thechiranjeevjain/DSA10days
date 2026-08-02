package org.chijai.day3.session3;

import java.util.Objects;

public class LongestPalindromicSubstring {

/*
 * ============================================================
 * 📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Title:
 * Longest Palindromic Substring
 *
 * Difficulty:
 * Medium on LeetCode (commonly considered interview-tricky)
 *
 * Tags:
 * String
 * Two Pointers
 * Expand Around Center
 * Dynamic Programming
 * Manacher's Algorithm (Advanced)
 *
 * Official Problem:
 * https://leetcode.com/problems/longest-palindromic-substring/
 *
 * ------------------------------------------------------------
 * Problem Statement
 * ------------------------------------------------------------
 *
 * Given a string s,
 * return the longest substring that is a palindrome.
 *
 * A palindrome reads exactly the same from left to right
 * and right to left.
 *
 * If multiple longest palindromes exist,
 * returning any one of them is acceptable.
 *
 * ------------------------------------------------------------
 * Constraints
 * ------------------------------------------------------------
 *
 * 1 <= s.length <= 1000
 *
 * s contains:
 * English letters
 * Digits
 *
 * ------------------------------------------------------------
 * Representative Examples
 * ------------------------------------------------------------
 *
 * Input:
 * "babad"
 *
 * Output:
 * "bab"
 *
 * Explanation:
 * "aba" is equally valid.
 *
 * ------------------------------------------------------------
 *
 * Input:
 * "cbbd"
 *
 * Output:
 * "bb"
 *
 * ------------------------------------------------------------
 *
 * Input:
 * "a"
 *
 * Output:
 * "a"
 *
 * ------------------------------------------------------------
 *
 * Input:
 * "ac"
 *
 * Output:
 * "a"
 *
 * ("c" is also valid.)
 *
 * ------------------------------------------------------------
 *
 * Important Java Reminder
 * ------------------------------------------------------------
 *
 * substring(begin, end)
 *
 * begin -> inclusive
 * end   -> exclusive
 *
 * Example:
 *
 * "abcdef"
 * substring(2,5)
 *
 * returns
 *
 * "cde"
 *
 * This exclusive ending is extremely important when expanding
 * around the center.
 */

/*
 * ============================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern
 * -------
 * Expand Around Center
 *
 * Archetype
 * ---------
 * Bidirectional Expansion
 *
 * Core Invariant
 * --------------
 * Before every expansion,
 * s[left...right]
 * is already a palindrome.
 *
 * We only expand while adding equal characters to BOTH sides.
 *
 * Therefore every successful expansion preserves the palindrome.
 *
 * ------------------------------------------------------------
 * Why It Works
 * ------------------------------------------------------------
 *
 * Every palindrome has exactly one center.
 *
 * Odd palindrome:
 *
 *     racecar
 *        e
 *
 * Even palindrome:
 *
 *      abccba
 *       cc
 *
 * Instead of checking every substring,
 * enumerate every possible center.
 *
 * Then grow outward.
 *
 * ------------------------------------------------------------
 * Number of Centers
 * ------------------------------------------------------------
 *
 * n odd centers
 *
 * n-1 even centers
 *
 * Total
 *
 * 2n-1
 *
 * Each expansion stops as soon as the invariant breaks.
 *
 * ------------------------------------------------------------
 * Recognition Signals
 * ------------------------------------------------------------
 *
 * ✓ String problem
 *
 * ✓ Symmetry matters
 *
 * ✓ Need longest palindrome
 *
 * ✓ Expansion naturally starts from middle
 *
 * ✓ Constraints around 1000
 *
 * ------------------------------------------------------------
 * When To Use
 * ------------------------------------------------------------
 *
 * Longest palindrome
 *
 * Count palindromes
 *
 * Enumerate palindromes
 *
 * Verify local symmetry
 *
 * ------------------------------------------------------------
 * When NOT To Use
 * ------------------------------------------------------------
 *
 * Need arbitrary substring DP states
 *
 * Need edit operations
 *
 * Need lexicographic optimization
 *
 * Very large strings (millions of characters)
 *
 * In those cases,
 * Manacher or suffix structures may be appropriate.
 *
 * ------------------------------------------------------------
 * Comparison
 * ------------------------------------------------------------
 *
 * Brute Force
 * ----------
 * Enumerate every substring.
 *
 * O(n³)
 *
 * ------------------------------------------------------------
 *
 * Dynamic Programming
 *
 * State:
 * palindrome(start,end)
 *
 * O(n²)
 * O(n²)
 *
 * ------------------------------------------------------------
 *
 * Expand Around Center
 *
 * O(n²)
 * O(1)
 *
 * Interview favorite.
 *
 * ------------------------------------------------------------
 *
 * Manacher
 *
 * O(n)
 *
 * Excellent algorithm.
 *
 * But implementation complexity is much higher.
 */

/*
 * ============================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine every character (and every gap between characters)
 * emitting a ripple.
 *
 * The ripple grows equally to the left and right.
 *
 * Expansion stops immediately when symmetry breaks.
 *
 * The largest ripple among every center
 * is the answer.
 *
 * ------------------------------------------------------------
 * Search Space
 * ------------------------------------------------------------
 *
 * We are NOT searching over substrings.
 *
 * We are searching over CENTERS.
 *
 * That is the key mental shift.
 *
 * ------------------------------------------------------------
 * State
 * ------------------------------------------------------------
 *
 * During one expansion:
 *
 * left
 * right
 *
 * always denote the current candidate palindrome.
 *
 * ------------------------------------------------------------
 * Primary Invariant
 * ------------------------------------------------------------
 *
 * BEFORE every iteration:
 *
 * s[left...right]
 * is a palindrome.
 *
 * ------------------------------------------------------------
 * Transition
 * ------------------------------------------------------------
 *
 * Compare
 *
 * s[left-1]
 *
 * with
 *
 * s[right+1]
 *
 * If equal,
 * safely include both.
 *
 * The invariant still holds.
 *
 * ------------------------------------------------------------
 * Expansion Rule
 * ------------------------------------------------------------
 *
 * Expand only if BOTH conditions hold:
 *
 * left >= 0
 *
 * right < n
 *
 * characters equal
 *
 * ------------------------------------------------------------
 * Allowed Moves
 * ------------------------------------------------------------
 *
 * left--
 *
 * right++
 *
 * only together.
 *
 * Never move only one pointer.
 *
 * ------------------------------------------------------------
 * Forbidden Moves
 * ------------------------------------------------------------
 *
 * Expanding after mismatch.
 *
 * Skipping one side.
 *
 * Ignoring boundary checks.
 *
 * Updating best answer before expansion finishes.
 *
 * ------------------------------------------------------------
 * Variable Meaning
 * ------------------------------------------------------------
 *
 * center
 *
 * Current symmetry point.
 *
 * left
 *
 * Left boundary.
 *
 * right
 *
 * Right boundary.
 *
 * bestStart
 *
 * Start of best palindrome discovered so far.
 *
 * bestLength
 *
 * Longest palindrome length found.
 *
 * ------------------------------------------------------------
 * Odd Centers
 * ------------------------------------------------------------
 *
 * left == right
 *
 * Example
 *
 * racecar
 *
 * center = e
 *
 * ------------------------------------------------------------
 * Even Centers
 * ------------------------------------------------------------
 *
 * right = left + 1
 *
 * Example
 *
 * abccba
 *
 * center between c and c
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Expansion terminates when
 *
 * boundary exceeded
 *
 * OR
 *
 * characters mismatch.
 *
 * The previous valid interval
 * is therefore the maximum palindrome
 * for that center.
 *
 * ------------------------------------------------------------
 * Correctness Intuition
 * ------------------------------------------------------------
 *
 * Every palindrome possesses exactly one unique center
 * (or one unique center gap).
 *
 * Since every possible center is examined,
 * no palindrome can escape inspection.
 *
 * Since each center expands maximally,
 * its longest palindrome is found.
 *
 * Taking the maximum across every center
 * therefore returns the global optimum.
 *
 * ------------------------------------------------------------
 * Why Naive Enumeration Fails
 * ------------------------------------------------------------
 *
 * Naive enumeration searches
 * O(n²) substrings.
 *
 * Each substring requires
 * O(n)
 * verification.
 *
 * Total
 *
 * O(n³)
 *
 * The center-expansion invariant avoids repeatedly
 * rechecking interior characters.
 */

/*
 * ============================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================
 *
 * Mistake 1
 * ---------
 * Checking only odd centers.
 *
 * Counterexample
 *
 * "abba"
 *
 * Correct answer:
 *
 * abba
 *
 * Missed completely.
 *
 * Violated invariant:
 *
 * Every palindrome has a center,
 * but centers may lie BETWEEN characters.
 *
 * ------------------------------------------------------------
 *
 * Mistake 2
 * ---------
 * Returning immediately after finding one palindrome.
 *
 * Counterexample
 *
 * "forgeeksskeegfor"
 *
 * Early discoveries are not necessarily optimal.
 *
 * ------------------------------------------------------------
 *
 * Mistake 3
 * ---------
 * Updating answer inside expansion loop.
 *
 * The last expansion usually fails.
 *
 * The valid palindrome is actually
 * one step smaller.
 *
 * This creates off-by-one bugs.
 *
 * ------------------------------------------------------------
 *
 * Mistake 4
 * ---------
 * Confusing substring end index.
 *
 * substring(start,end)
 *
 * end is exclusive.
 *
 * A very common interview bug.
 *
 * ------------------------------------------------------------
 *
 * Mistake 5
 * ---------
 * Forgetting even centers.
 *
 * Typical interviewer trap.
 *
 * Test:
 *
 * "aa"
 *
 * "abba"
 *
 * "cbbd"
 *
 * should all pass.
 */

    /*
     * ============================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Goal
     * ----
     * Mechanically reconstruct the optimal algorithm from memory.
     *
     * ------------------------------------------------------------
     * Step 1
     * ------------------------------------------------------------
     *
     * Handle trivial inputs.
     *
     * null
     * empty
     * length 1
     *
     * ------------------------------------------------------------
     * Step 2
     * ------------------------------------------------------------
     *
     * Create
     *
     * bestStart = 0
     * bestLength = 1
     *
     * Every single character is already a palindrome.
     *
     * ------------------------------------------------------------
     * Step 3
     * ------------------------------------------------------------
     *
     * Iterate every possible center.
     *
     * for center = 0 ... n-1
     *
     * ------------------------------------------------------------
     * Step 4
     * ------------------------------------------------------------
     *
     * Expand odd palindrome.
     *
     * left = center
     * right = center
     *
     * ------------------------------------------------------------
     * Step 5
     * ------------------------------------------------------------
     *
     * Expand even palindrome.
     *
     * left = center
     * right = center + 1
     *
     * ------------------------------------------------------------
     * Step 6
     * ------------------------------------------------------------
     *
     * Compute
     *
     * currentLength
     *
     * If larger than best,
     * update
     *
     * bestStart
     * bestLength
     *
     * ------------------------------------------------------------
     * Step 7
     * ------------------------------------------------------------
     *
     * Return
     *
     * substring(bestStart,
     *           bestStart + bestLength)
     *
     * ------------------------------------------------------------
     * Mechanical Typing Order
     * ------------------------------------------------------------
     *
     * longestPalindrome()
     *
     * ↓
     *
     * Guard clause
     *
     * ↓
     *
     * bestStart
     *
     * bestLength
     *
     * ↓
     *
     * loop over centers
     *
     * ↓
     *
     * odd expansion
     *
     * ↓
     *
     * update answer
     *
     * ↓
     *
     * even expansion
     *
     * ↓
     *
     * update answer
     *
     * ↓
     *
     * substring()
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * initialize answer
     *
     * for every center
     *
     *     expand odd
     *
     *     update answer
     *
     *     expand even
     *
     *     update answer
     *
     * return answer
     */

    /*
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    /*
     * ============================================================
     * Brute Force
     * ============================================================
     *
     * Idea
     * ----
     *
     * Enumerate every substring.
     *
     * Verify whether it is a palindrome.
     *
     * Keep the longest.
     *
     * ------------------------------------------------------------
     * Invariant
     * ------------------------------------------------------------
     *
     * Before checking a substring,
     * current answer stores the best palindrome
     * among every previously examined substring.
     *
     * ------------------------------------------------------------
     * Limitation
     * ------------------------------------------------------------
     *
     * Every substring is independently verified.
     *
     * Interior comparisons repeat many times.
     *
     * ------------------------------------------------------------
     * Complexity
     * ------------------------------------------------------------
     *
     * Time
     *
     * O(n³)
     *
     * Space
     *
     * O(1)
     *
     * ------------------------------------------------------------
     * Interview Usefulness
     * ------------------------------------------------------------
     *
     * Good starting point.
     *
     * Demonstrates progression toward better solutions.
     */

    static final class BruteForce {

        static String longestPalindrome(String s) {

            Objects.requireNonNull(s);

            if (s.isEmpty()) {
                return "";
            }

            int bestStart = 0;
            int bestLength = 1;

            for (int start = 0; start < s.length(); start++) {

                for (int end = start; end < s.length(); end++) {

                    if (isPalindrome(s, start, end)) {

                        int length = end - start + 1;

                        if (length > bestLength) {

                            bestLength = length;
                            bestStart = start;
                        }
                    }
                }
            }

            return s.substring(bestStart, bestStart + bestLength);
        }

        private static boolean isPalindrome(String s,
                                            int left,
                                            int right) {

            while (left < right) {

                if (s.charAt(left) != s.charAt(right)) {
                    return false;
                }

                left++;
                right--;
            }

            return true;
        }
    }

    /*
     * ============================================================
     * Improved
     * ============================================================
     *
     * Idea
     * ----
     *
     * Dynamic Programming.
     *
     * dp[left][right]
     *
     * indicates whether
     * s[left...right]
     * is a palindrome.
     *
     * ------------------------------------------------------------
     * State
     * ------------------------------------------------------------
     *
     * dp[left][right]
     *
     * ------------------------------------------------------------
     * Transition
     * ------------------------------------------------------------
     *
     * Equal boundary characters
     *
     * &&
     *
     * Inner substring already palindrome.
     *
     * ------------------------------------------------------------
     * Invariant
     * ------------------------------------------------------------
     *
     * When processing length L,
     * every shorter substring has already been solved.
     *
     * Therefore
     * dp[left+1][right-1]
     * is immediately available.
     *
     * ------------------------------------------------------------
     * Improvement
     * ------------------------------------------------------------
     *
     * Reuses interior palindrome results.
     *
     * Eliminates repeated verification.
     *
     * ------------------------------------------------------------
     * Complexity
     * ------------------------------------------------------------
     *
     * Time
     *
     * O(n²)
     *
     * Space
     *
     * O(n²)
     *
     * ------------------------------------------------------------
     * Interview Usefulness
     * ------------------------------------------------------------
     *
     * Important transition toward interval DP.
     */

    static final class DynamicProgramming {

        static String longestPalindrome(String s) {

            Objects.requireNonNull(s);

            int n = s.length();

            if (n <= 1) {
                return s;
            }

            boolean[][] dp = new boolean[n][n];

            int bestStart = 0;
            int bestLength = 1;

            for (int i = 0; i < n; i++) {
                dp[i][i] = true;
            }

            for (int left = 0; left < n - 1; left++) {

                if (s.charAt(left) == s.charAt(left + 1)) {

                    dp[left][left + 1] = true;

                    bestStart = left;
                    bestLength = 2;
                }
            }

            for (int length = 3; length <= n; length++) {

                for (int left = 0;
                     left + length - 1 < n;
                     left++) {

                    int right = left + length - 1;

                    if (s.charAt(left) != s.charAt(right)) {
                        continue;
                    }

                    if (!dp[left + 1][right - 1]) {
                        continue;
                    }

                    dp[left][right] = true;

                    if (length > bestLength) {

                        bestLength = length;
                        bestStart = left;
                    }
                }
            }

            return s.substring(bestStart,
                    bestStart + bestLength);
        }
    }

    /*
     * ============================================================
     * Optimal (Interview Preferred)
     * ============================================================
     *
     * Idea
     * ----
     *
     * Enumerate every possible center.
     *
     * Expand while symmetry survives.
     *
     * ------------------------------------------------------------
     * Core Invariant
     * ------------------------------------------------------------
     *
     * The current interval is always a palindrome.
     *
     * Expansion preserves this invariant
     * by adding equal characters to both ends.
     *
     * ------------------------------------------------------------
     * Correctness
     * ------------------------------------------------------------
     *
     * Every palindrome has one unique center
     * (character or gap).
     *
     * Since every center is explored maximally,
     * the longest palindrome cannot be missed.
     *
     * ------------------------------------------------------------
     * Complexity
     * ------------------------------------------------------------
     *
     * Time
     *
     * O(n²)
     *
     * Space
     *
     * O(1)
     *
     * ------------------------------------------------------------
     * Interview Usefulness
     * ------------------------------------------------------------
     *
     * This is the expected interview solution.
     */

    static final class Optimal {

        static String longestPalindrome(String s) {

            Objects.requireNonNull(s);

            if (s.isEmpty()) {
                return "";
            }

            if (s.length() == 1) {
                return s;
            }

            int bestStart = 0;
            int bestLength = 1;

            for (int center = 0;
                 center < s.length();
                 center++) {

                int oddLength = expandLength(
                        s,
                        center,
                        center
                );

                if (oddLength > bestLength) {

                    bestLength = oddLength;

                    bestStart =
                            center - (oddLength - 1) / 2;
                }

                int evenLength = expandLength(
                        s,
                        center,
                        center + 1
                );

                if (evenLength > bestLength) {

                    bestLength = evenLength;

                    bestStart =
                            center - (evenLength - 1) / 2;
                }
            }

            return s.substring(
                    bestStart,
                    bestStart + bestLength
            );
        }

        private static int expandLength(String s,
                                        int left,
                                        int right) {

            // 🟢 Invariant:
            // Before each expansion,
            // s[left...right] is a palindrome
            // whenever left/right are valid.

            while (left >= 0
                    && right < s.length()
                    && s.charAt(left) == s.charAt(right)) {

                // Expand symmetrically.
                left--;
                right++;
            }

            // Expansion failed one step ago.
            // Recover previous valid palindrome length.

            return right - left - 1;
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * If asked "Explain your approach."
 *
 * I observe that every palindrome has exactly one center.
 *
 * The center can either be:
 *
 * 1. one character (odd length)
 * 2. between two characters (even length)
 *
 * Therefore I simply enumerate every possible center.
 *
 * For each center I expand outward while the two boundary
 * characters remain equal.
 *
 * The expansion invariant is:
 *
 * Every currently accepted interval is already a palindrome.
 *
 * When expansion stops,
 * the previous interval is the largest palindrome
 * for that center.
 *
 * Since every possible center is explored,
 * every palindrome is considered exactly once through its
 * natural center.
 *
 * Therefore taking the maximum length over all centers
 * gives the global optimum.
 *
 * ------------------------------------------------------------
 * Discard Rule
 * ------------------------------------------------------------
 *
 * The first mismatch permanently terminates that expansion.
 *
 * Any larger interval would necessarily contain the mismatch,
 * so it cannot become a palindrome.
 *
 * ------------------------------------------------------------
 * Correctness
 * ------------------------------------------------------------
 *
 * Local optimality:
 * maximal palindrome for one center.
 *
 * Global optimality:
 * maximum across all centers.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Expansion stops because either
 *
 * left < 0
 *
 * right >= n
 *
 * or
 *
 * characters differ.
 *
 * ------------------------------------------------------------
 * In-place Feasibility
 * ------------------------------------------------------------
 *
 * Yes.
 *
 * Only constant extra variables are required.
 *
 * ------------------------------------------------------------
 * Streaming Feasibility
 * ------------------------------------------------------------
 *
 * No.
 *
 * Expansion may require characters arbitrarily far
 * to both left and right.
 *
 * Future characters are necessary.
 *
 * ------------------------------------------------------------
 * When NOT To Use
 * ------------------------------------------------------------
 *
 * Extremely large strings.
 *
 * Strict linear-time requirement.
 *
 * Then Manacher's algorithm becomes preferable.
 */

/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 *
 * Longest palindrome in a string.
 *
 * ------------------------------------------------------------
 * Pattern
 * ------------------------------------------------------------
 *
 * Expand Around Center.
 *
 * ------------------------------------------------------------
 * Search Target
 * ------------------------------------------------------------
 *
 * Centers.
 *
 * Never substrings.
 *
 * ------------------------------------------------------------
 * Invariant
 * ------------------------------------------------------------
 *
 * Current interval is always a palindrome.
 *
 * ------------------------------------------------------------
 * Transition
 * ------------------------------------------------------------
 *
 * Compare outside characters.
 *
 * Expand together.
 *
 * ------------------------------------------------------------
 * Discard Rule
 * ------------------------------------------------------------
 *
 * First mismatch ends expansion.
 *
 * ------------------------------------------------------------
 * Common Trap
 * ------------------------------------------------------------
 *
 * Forgetting even centers.
 *
 * ------------------------------------------------------------
 * Edge Cases
 * ------------------------------------------------------------
 *
 * ""
 *
 * "a"
 *
 * "aa"
 *
 * "abba"
 *
 * "abc"
 *
 * all identical characters
 *
 * ------------------------------------------------------------
 * One-Liner
 * ------------------------------------------------------------
 *
 * Every palindrome has one center.
 * Expand every center.
 *
 * ------------------------------------------------------------
 * Re-derivation Cue
 * ------------------------------------------------------------
 *
 * Draw:
 *
 * aba
 *
 * and
 *
 * abba
 *
 * Immediately both center types become obvious.
 */

/*
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variation
 * ---------
 * Count Palindromic Substrings
 *
 * Pattern
 * -------
 * Same.
 *
 * Difference
 * ----------
 * Instead of recording longest length,
 * increment answer after every successful expansion.
 *
 * ------------------------------------------------------------
 * Variation
 * ------------------------------------------------------------
 *
 * Return longest palindrome length.
 *
 * Keep only bestLength.
 *
 * No substring construction.
 *
 * ------------------------------------------------------------
 * Variation
 * ------------------------------------------------------------
 *
 * Return all maximal palindromes.
 *
 * Store every maximal interval.
 *
 * ------------------------------------------------------------
 * Variation
 * ------------------------------------------------------------
 *
 * Lexicographically smallest longest palindrome.
 *
 * Same expansion.
 *
 * Tie-breaking changes.
 *
 * ------------------------------------------------------------
 * Variation
 * ------------------------------------------------------------
 *
 * Longest Palindromic Subsequence.
 *
 * Pattern breaks.
 *
 * Why?
 *
 * Characters may be skipped.
 *
 * Symmetric expansion no longer preserves correctness.
 *
 * Interval DP is required.
 *
 * ------------------------------------------------------------
 * Variation
 * ------------------------------------------------------------
 *
 * Very large input.
 *
 * Pattern changes.
 *
 * Use Manacher's algorithm.
 *
 * Same mathematical idea.
 *
 * Better reuse of previously computed radii.
 *
 * O(n).
 */

/*
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * □ Can I explain why every palindrome has one center?
 *
 * □ Can I explain why both odd and even centers are required?
 *
 * □ Can I state the invariant without looking?
 *
 * □ Can I derive the expansion loop?
 *
 * □ Can I justify termination?
 *
 * □ Can I explain why the previous interval is returned?
 *
 * □ Can I derive
 *
 * right - left - 1
 *
 * after expansion fails?
 *
 * □ Can I derive
 *
 * center - (length - 1) / 2
 *
 * for the starting index?
 *
 * □ Can I explain why brute force repeats work?
 *
 * □ Can I explain why DP removes repeated checking?
 *
 * □ Can I explain why center expansion removes O(n²) memory?
 *
 * □ Can I identify when Manacher is required?
 *
 * □ Am I confident debugging off-by-one errors?
 *
 * □ Can I reconstruct the solution from the invariant alone?
 */

/*
 * ============================================================
 * ⚫ PATTERN MAPPING
 * ============================================================
 *
 * Problem
 * ------------------------------
 * Longest Palindromic Substring
 *
 * Pattern
 * ------------------------------
 * Expand Around Center
 *
 * ------------------------------------------------------------
 *
 * Problem
 * ------------------------------
 * Count Palindromic Substrings
 *
 * Pattern
 * ------------------------------
 * Expand Around Center
 *
 * ------------------------------------------------------------
 *
 * Problem
 * ------------------------------
 * Valid Palindrome
 *
 * Pattern
 * ------------------------------
 * Two Pointers
 *
 * ------------------------------------------------------------
 *
 * Problem
 * ------------------------------
 * Longest Palindromic Subsequence
 *
 * Pattern
 * ------------------------------
 * Interval DP
 *
 * ------------------------------------------------------------
 *
 * Problem
 * ------------------------------
 * Shortest Palindrome
 *
 * Pattern
 * ------------------------------
 * KMP / Rolling Hash
 *
 * ------------------------------------------------------------
 *
 * Problem
 * ------------------------------
 * Palindrome Partitioning
 *
 * Pattern
 * ------------------------------
 * Backtracking + DP
 */

/*
 * ============================================================
 * 🔴 DEBUGGING GUIDE
 * ============================================================
 *
 * Symptom
 * -------
 * Missing "bb"
 *
 * Cause
 * -----
 * Even center not checked.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * StringIndexOutOfBoundsException
 *
 * Cause
 * -----
 * Boundary checked after accessing characters.
 *
 * Always verify bounds first.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Result shorter by one.
 *
 * Cause
 * -----
 * Forgot expansion fails before exiting.
 *
 * Correct length:
 *
 * right - left - 1
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Wrong substring.
 *
 * Cause
 * -----
 * Confused exclusive end index.
 *
 * Correct:
 *
 * substring(start,
 *           start + length)
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Wrong starting index.
 *
 * Cause
 * -----
 * Forgot odd/even unification.
 *
 * Correct:
 *
 * start = center - (length - 1) / 2
 *
 * Works for both center types.
 */

/*
 * ============================================================
 * 🟡 COMPLEXITY SUMMARY
 * ============================================================
 *
 * Method                  Time        Space
 * --------------------------------------------
 * Brute Force            O(n³)       O(1)
 *
 * Dynamic Programming    O(n²)       O(n²)
 *
 * Expand Around Center   O(n²)       O(1)
 *
 * Manacher               O(n)        O(n)
 */

    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     *
     * Run with assertions enabled:
     *
     * java -ea LongestPalindromicSubstring
     */

    private static void assertOneOf(String actual, String... expected) {

        for (String candidate : expected) {
            if (candidate.equals(actual)) {
                return;
            }
        }

        throw new AssertionError(
                "Expected one of "
                        + java.util.Arrays.toString(expected)
                        + " but found "
                        + actual
        );
    }

    public static void main(String[] args) {

        /*
         * Happy path.
         *
         * Two valid optimal answers exist.
         */
        assertOneOf(
                Optimal.longestPalindrome("babad"),
                "bab",
                "aba"
        );

        /*
         * Classic even palindrome.
         */
        assert Optimal.longestPalindrome("cbbd")
                .equals("bb");

        /*
         * Single character.
         */
        assert Optimal.longestPalindrome("a")
                .equals("a");

        /*
         * No palindrome longer than one.
         */
        assertOneOf(
                Optimal.longestPalindrome("ac"),
                "a",
                "c"
        );

        /*
         * Entire string is a palindrome.
         */
        assert Optimal.longestPalindrome("racecar")
                .equals("racecar");

        /*
         * Entire string, even length.
         */
        assert Optimal.longestPalindrome("abba")
                .equals("abba");

        /*
         * All identical characters.
         */
        assert Optimal.longestPalindrome("aaaaaa")
                .equals("aaaaaa");

        /*
         * Long palindrome inside larger string.
         */
        assert Optimal.longestPalindrome("forgeeksskeegfor")
                .equals("geeksskeeg");

        /*
         * Prefix palindrome.
         */
        assert Optimal.longestPalindrome("abac")
                .equals("aba");

        /*
         * Suffix palindrome.
         */
        assert Optimal.longestPalindrome("cabac")
                .equals("cabac");

        /*
         * Empty input.
         */
        assert Optimal.longestPalindrome("")
                .equals("");

        /*
         * Brute force and optimal should agree.
         */
        assert BruteForce.longestPalindrome("banana")
                .equals(
                        Optimal.longestPalindrome("banana")
                );

        /*
         * DP and optimal should agree.
         */
        assert DynamicProgramming.longestPalindrome("banana")
                .equals(
                        Optimal.longestPalindrome("banana")
                );

        /*
         * Interview trap:
         * even center in middle.
         */
        assert Optimal.longestPalindrome("abccba")
                .equals("abccba");

        /*
         * Interview trap:
         * palindrome surrounded by noise.
         */
        assert Optimal.longestPalindrome("xyzabccbay")
                .equals("abccba");

        /*
         * Boundary:
         * two equal characters.
         */
        assert Optimal.longestPalindrome("aa")
                .equals("aa");

        /*
         * Boundary:
         * two different characters.
         */
        assertOneOf(
                Optimal.longestPalindrome("ab"),
                "a",
                "b"
        );

        /*
         * Multiple optimal palindromes.
         */
        assertOneOf(
                Optimal.longestPalindrome("abacdfgdcaba"),
                "aba"
        );

        /*
         * Large odd palindrome.
         */
        assert Optimal.longestPalindrome("abcddcbaxyz")
                .equals("abcddcba");

        /*
         * Large even palindrome.
         */
        assert Optimal.longestPalindrome("xyzabccba")
                .equals("abccba");

        System.out.println("All assertions passed.");
    }

    /*
     * ============================================================
     * FINAL RECALL
     * ============================================================
     *
     * Pattern
     * -------
     * Expand Around Center.
     *
     * Search Space
     * ------------
     * Centers.
     *
     * Invariant
     * ---------
     * Current interval is always a palindrome.
     *
     * Transition
     * ----------
     * Expand equally to both sides.
     *
     * Discard Rule
     * ------------
     * First mismatch permanently ends expansion.
     *
     * Correctness
     * -----------
     * Every palindrome has exactly one center.
     *
     * Complexity
     * ----------
     * Time  : O(n²)
     * Space : O(1)
     *
     * Interview One-Liner
     * -------------------
     * Enumerate every possible center, expand while symmetry
     * holds, and keep the longest expansion.
     */

}