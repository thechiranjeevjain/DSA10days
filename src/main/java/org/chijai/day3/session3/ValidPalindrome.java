package org.chijai.day3.session3;

import java.util.Locale;

public class ValidPalindrome {

/*
 * ================================================================
 * 2. 📘 PRIMARY PROBLEM
 * ================================================================
 *
 * Title:
 * Valid Palindrome
 *
 * Difficulty:
 * Easy
 *
 * Tags:
 * Two Pointers
 * String
 * Character Processing
 *
 * Problem Description
 * -------------------
 * A phrase is considered a palindrome after:
 *
 * 1. Converting every uppercase English letter into lowercase.
 * 2. Removing every non-alphanumeric character.
 *
 * Alphanumeric characters include:
 * - English letters
 * - Digits
 *
 * Return true if the resulting sequence reads the same from both
 * directions.
 *
 * Return false otherwise.
 *
 * Constraints
 * -----------
 * 1 <= s.length <= 2 * 10^5
 *
 * s consists of printable ASCII characters.
 *
 * Representative Examples
 * -----------------------
 *
 * Example 1
 *
 * Input:
 * "A man, a plan, a canal: Panama"
 *
 * Normalized:
 * amanaplanacanalpanama
 *
 * Output:
 * true
 *
 *
 * Example 2
 *
 * Input:
 * "race a car"
 *
 * Normalized:
 * raceacar
 *
 * Output:
 * false
 *
 *
 * Example 3
 *
 * Input:
 * " "
 *
 * Normalized:
 * ""
 *
 * Output:
 * true
 *
 * Official LeetCode
 * -----------------
 * https://leetcode.com/problems/valid-palindrome/
 */


/*
 * ================================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ================================================================
 *
 * Pattern
 * -------
 * Opposing Two Pointers
 *
 * Archetype
 * ---------
 * Shrinking Search Space
 *
 * Core Invariant
 * --------------
 * Every time both pointers compare two valid characters,
 * all characters outside the current window have already
 * been verified to mirror each other.
 *
 * Therefore:
 *
 * answer ∈ current window
 *
 * Once a pair matches,
 *
 * search space shrinks inward.
 *
 * Why It Works
 * ------------
 * A palindrome is symmetric.
 *
 * The leftmost surviving character must equal the
 * rightmost surviving character.
 *
 * If they do not,
 * no later comparison can repair that mismatch.
 *
 * Recognition Signals
 * -------------------
 * Look for:
 *
 * ✓ compare both ends
 * ✓ ignore irrelevant characters
 * ✓ symmetry
 * ✓ shrink interval
 * ✓ no revisiting
 *
 * When To Use
 * -----------
 * - palindrome verification
 * - mirrored comparison
 * - checking symmetry
 * - validating strings after filtering
 *
 * When NOT To Use
 * ---------------
 * Do not use when:
 *
 * - arbitrary insertions are allowed
 * - edit distance is required
 * - order may change
 * - matching is not symmetric
 *
 * Comparison With Similar Patterns
 * --------------------------------
 *
 * Sliding Window
 * Tracks a moving contiguous region.
 *
 * Two Pointers (this problem)
 * Shrinks from opposite directions.
 *
 * Fast & Slow Pointer
 * One pointer discovers structure.
 * Here both pointers actively eliminate search space.
 *
 * Binary Search
 * Discards half based on ordering.
 * Here we discard only verified matching boundaries.
 */


/*
 * ================================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ================================================================
 *
 * Mental Model
 * ------------
 * Imagine peeling identical layers from both sides.
 *
 * Non-alphanumeric characters are invisible.
 *
 * They never belong to the palindrome.
 *
 * Therefore they should never participate in comparison.
 *
 * Every iteration performs exactly one of two actions:
 *
 * 1.
 * Skip an invalid character.
 *
 * or
 *
 * 2.
 * Verify a symmetric pair.
 *
 *
 * ------------------------------------------------
 * Invariant 1
 * ------------------------------------------------
 *
 * Every character outside
 *
 * [left ... right]
 *
 * has already been processed correctly.
 *
 *
 * ------------------------------------------------
 * Invariant 2
 * ------------------------------------------------
 *
 * left always searches for the next valid character.
 *
 * It never moves backward.
 *
 *
 * ------------------------------------------------
 * Invariant 3
 * ------------------------------------------------
 *
 * right always searches for the previous valid character.
 *
 * It never moves forward.
 *
 *
 * ------------------------------------------------
 * Invariant 4
 * ------------------------------------------------
 *
 * Whenever both pointers stop,
 *
 * they both reference valid alphanumeric characters.
 *
 *
 * ------------------------------------------------
 * Invariant 5
 * ------------------------------------------------
 *
 * After a successful comparison,
 *
 * both characters are permanently verified.
 *
 * They never need reconsideration.
 *
 *
 * Variable Meaning
 * ----------------
 *
 * left
 * ----
 * First unchecked valid character from the beginning.
 *
 * right
 * -----
 * First unchecked valid character from the end.
 *
 *
 * Allowed Moves
 * -------------
 *
 * left++
 * while skipping punctuation
 *
 * right--
 * while skipping punctuation
 *
 * left++
 * right--
 * after a successful match
 *
 *
 * Forbidden Moves
 * ---------------
 *
 * Never compare punctuation.
 *
 * Never move both pointers before comparison.
 *
 * Never skip a valid character.
 *
 * Never compare without normalizing case.
 *
 *
 * Termination
 * -----------
 *
 * Eventually
 *
 * left >= right
 *
 * meaning every valid mirrored pair has been checked.
 *
 *
 * Correctness Intuition
 * ---------------------
 *
 * A palindrome fails immediately at its first unequal mirrored pair.
 *
 * Therefore the first mismatch is sufficient proof.
 *
 * If no mismatch exists,
 * every mirrored pair matches,
 * therefore the normalized string is a palindrome.
 *
 *
 * Why Naive Solutions Fail
 * ------------------------
 *
 * Naive Approach 1
 * Build another filtered string.
 *
 * Correct but consumes additional memory.
 *
 * This problem can be solved in O(1) auxiliary space.
 *
 *
 * Naive Approach 2
 * Compare original characters directly.
 *
 * Fails because punctuation participates.
 *
 * Example:
 *
 * "A,"
 *
 * should normalize to
 *
 * "a"
 *
 * which is valid.
 */


/*
 * ================================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ================================================================
 *
 * Mistake 1
 * ---------
 * Forget lowercase conversion.
 *
 * Why It Looks Correct
 * --------------------
 * Most examples are already lowercase.
 *
 * Violated Invariant
 * ------------------
 * Equal letters must compare case-insensitively.
 *
 * Counterexample
 * --------------
 * "Aa"
 *
 * Expected:
 * true
 *
 * Incorrect:
 * false
 *
 *
 * ------------------------------------------------
 *
 * Mistake 2
 * ---------
 * Compare punctuation.
 *
 * Counterexample
 *
 * "a."
 *
 * Expected:
 * true
 *
 *
 * ------------------------------------------------
 *
 * Mistake 3
 * ---------
 * Move both pointers whenever one side is invalid.
 *
 * Why It Seems Reasonable
 * -----------------------
 * Programmer wants to keep symmetry.
 *
 * Actual Problem
 * --------------
 * One valid character gets skipped forever.
 *
 * Invariant Broken
 * ----------------
 * Every valid character must be matched exactly once.
 *
 *
 * ------------------------------------------------
 *
 * Mistake 4
 * ---------
 * Skip only left side first using if instead of repeated search.
 *
 * Counterexample
 *
 * "...,a"
 *
 * Multiple invalid symbols require repeated advancement.
 *
 *
 * ------------------------------------------------
 *
 * Interview Trap
 * --------------
 *
 * Candidate says:
 *
 * "I'll remove all punctuation first."
 *
 * That works,
 * but interviewer may ask:
 *
 * Can you solve it in-place with O(1) extra space?
 */


/*
 * ================================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ================================================================
 *
 * Mechanical Typing Order
 * -----------------------
 *
 * Step 1
 *
 * Handle null if required.
 *
 * Step 2
 *
 * left = 0
 *
 * Step 3
 *
 * right = length - 1
 *
 * Step 4
 *
 * while(left < right)
 *
 * Step 5
 *
 * Skip invalid left characters.
 *
 * Step 6
 *
 * Skip invalid right characters.
 *
 * Step 7
 *
 * Compare lowercase versions.
 *
 * Step 8
 *
 * Return false immediately on mismatch.
 *
 * Step 9
 *
 * Move both pointers inward.
 *
 * Step 10
 *
 * Return true.
 */


/*
 * ================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ================================================================
 *
 * left ← start
 * right ← end
 *
 * while left < right
 *     skip invalid left
 *     skip invalid right
 *     compare normalized characters
 *     mismatch → false
 *     inward move
 *
 * return true
 */
    /*
     * ================================================================
     * 6. SOLUTION CLASSES
     * ================================================================
     */


    /*
     * ================================================================
     * Brute Force
     * ================================================================
     *
     * Idea
     * ----
     * Build a normalized string by:
     *
     * 1. Ignoring non-alphanumeric characters.
     * 2. Converting letters to lowercase.
     *
     * Then verify whether the normalized string is a palindrome.
     *
     * 🟢 Invariant
     * ------------
     * The normalized string contains exactly the characters that
     * participate in the palindrome definition.
     *
     * Limitation
     * ----------
     * Requires O(n) extra memory.
     *
     * Complexity
     * ----------
     * Time  : O(n)
     * Space : O(n)
     *
     * Interview Usefulness
     * --------------------
     * Excellent starting point.
     * Easy to explain.
     * Often followed by the interviewer asking for O(1) space.
     */
    static class BruteForce {

        static boolean isPalindrome(String s) {

            if (s == null) {
                return false;
            }

            StringBuilder normalized = new StringBuilder();

            for (int i = 0; i < s.length(); i++) {

                char c = s.charAt(i);

                if (Character.isLetterOrDigit(c)) {
                    normalized.append(Character.toLowerCase(c));
                }
            }

            int left = 0;
            int right = normalized.length() - 1;

            while (left < right) {

                if (normalized.charAt(left) != normalized.charAt(right)) {
                    return false;
                }

                left++;
                right--;
            }

            return true;
        }
    }


    /*
     * ================================================================
     * Improved
     * ================================================================
     *
     * Idea
     * ----
     * Normalize the entire string once using lowercase,
     * then perform manual ASCII filtering while comparing.
     *
     * Compared with the brute-force approach,
     * this avoids constructing a second filtered string.
     *
     * 🟢 Invariant
     * ------------
     * Every comparison occurs only between lowercase
     * alphanumeric characters.
     *
     * Improvement
     * -----------
     * Avoids creating another string containing only valid
     * characters.
     *
     * Complexity
     * ----------
     * Time  : O(n)
     * Space : O(n)
     *
     * (The lowercase conversion itself creates another String.)
     *
     * Interview Usefulness
     * --------------------
     * Demonstrates understanding of pointer movement before
     * introducing the fully optimal solution.
     */
    static class Improved {

        static boolean isPalindrome(String s) {

            if (s == null) {
                return false;
            }

            s = s.toLowerCase(Locale.ROOT);

            int left = 0;
            int right = s.length() - 1;

            while (left < right) {

                while (left < right && !isAsciiAlphaNumeric(s.charAt(left))) {
                    left++;
                }

                while (left < right && !isAsciiAlphaNumeric(s.charAt(right))) {
                    right--;
                }

                if (s.charAt(left) != s.charAt(right)) {
                    return false;
                }

                left++;
                right--;
            }

            return true;
        }

        private static boolean isAsciiAlphaNumeric(char c) {

            return (c >= 'a' && c <= 'z')
                    || (c >= '0' && c <= '9');
        }
    }


    /*
     * ================================================================
     * Optimal (Interview Preferred)
     * ================================================================
     *
     * Idea
     * ----
     * Never build a filtered string.
     *
     * Instead,
     * both pointers search lazily for the next valid character.
     *
     * Invalid characters simply disappear from the search space.
     *
     * 🟢 Primary Invariant
     * --------------------
     * Whenever the comparison executes,
     * both pointers reference valid alphanumeric characters
     * that have not been verified previously.
     *
     * Search Space
     * ------------
     * [left ... right]
     *
     * Everything outside this interval has already been proven
     * symmetric.
     *
     * Discard Rule
     * ------------
     *
     * Invalid left character
     *      ->
     *      discard only left.
     *
     * Invalid right character
     *      ->
     *      discard only right.
     *
     * Matching pair
     *      ->
     *      discard both.
     *
     * Mismatch
     *      ->
     *      terminate immediately.
     *
     * Correctness
     * -----------
     * Every valid character participates exactly once.
     *
     * No valid comparison is skipped.
     *
     * Complexity
     * ----------
     * Time  : O(n)
     * Space : O(1)
     *
     * Interview Usefulness
     * --------------------
     * This is the expected optimal solution for the problem.
     */
    static class Optimal {

        static boolean isPalindrome(String s) {

            if (s == null) {
                return false;
            }

            int left = 0;
            int right = s.length() - 1;

            while (left < right) {

                char leftCharacter = s.charAt(left);
                char rightCharacter = s.charAt(right);

                // 🟢 Invariant:
                // Left pointer always searches for the next
                // unchecked valid character.
                if (!Character.isLetterOrDigit(leftCharacter)) {
                    left++;
                    continue;
                }

                // 🟢 Invariant:
                // Right pointer always searches for the previous
                // unchecked valid character.
                if (!Character.isLetterOrDigit(rightCharacter)) {
                    right--;
                    continue;
                }

                // 🔵 Both pointers now reference valid characters.
                if (Character.toLowerCase(leftCharacter)
                        != Character.toLowerCase(rightCharacter)) {

                    // 🔴 First mismatch proves symmetry cannot exist.
                    return false;
                }

                // 🟢 This mirrored pair is permanently verified.
                left++;
                right--;
            }

            // 🟢 Every mirrored pair matched.
            return true;
        }
    }


/*
 * ================================================================
 * 🟣 INTERVIEW ARTICULATION
 * ================================================================
 *
 * If asked,
 * explain the algorithm like this:
 *
 * "The search space is always the interval between the
 * two pointers.
 *
 * Characters outside that interval have already been
 * verified.
 *
 * Invalid characters are never compared because they are
 * not part of the normalized string.
 *
 * Whenever both pointers stop,
 * they reference the next valid mirrored pair.
 *
 * A mismatch immediately proves the string cannot be a
 * palindrome.
 *
 * Otherwise that pair is permanently verified and the
 * search space shrinks."
 *
 * In-place Feasibility
 * --------------------
 * Yes.
 *
 * No character movement is required.
 *
 * Streaming Feasibility
 * ---------------------
 * No.
 *
 * We require random access to both ends simultaneously.
 *
 * When NOT To Use
 * ---------------
 * Do not use this pattern if the comparison is no longer
 * symmetric or if characters may be reordered.
 */
/*
 * ================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ================================================================
 *
 * Trigger
 * -------
 * Compare two ends while ignoring unwanted characters.
 *
 * Pattern
 * -------
 * Opposing Two Pointers
 *
 * Search Target
 * -------------
 * The next unmatched valid character on each side.
 *
 * Primary Invariant
 * -----------------
 * Everything outside the current window has already been
 * proven symmetric.
 *
 * Discard Rule
 * ------------
 * Invalid left
 *      -> left++
 *
 * Invalid right
 *      -> right--
 *
 * Matching pair
 *      -> left++, right--
 *
 * Mismatch
 *      -> return false
 *
 * Common Trap
 * -----------
 * Moving both pointers when only one side is invalid.
 *
 * Edge Cases
 * ----------
 * ✓ Empty normalized string
 * ✓ Only punctuation
 * ✓ Single character
 * ✓ Mixed uppercase/lowercase
 * ✓ Digits
 * ✓ Consecutive punctuation
 *
 * 30-Second One-Liner
 * -------------------
 * Skip invalid characters, compare lowercase valid
 * characters, shrink inward until the pointers cross.
 *
 * Re-Derivation Cue
 * -----------------
 * Ask:
 *
 * "Which two characters must match first?"
 *
 * Answer:
 *
 * The outermost remaining valid pair.
 */


/*
 * ================================================================
 * 🔄 VARIATIONS & TWEAKS
 * ================================================================
 *
 * ------------------------------------------------
 * Variation 1
 * ------------------------------------------------
 *
 * Valid Palindrome II
 *
 * Change
 * ------
 * One deletion allowed.
 *
 * Reasoning Change
 * ----------------
 * First mismatch creates exactly two candidate search
 * spaces:
 *
 * skip left
 *
 * OR
 *
 * skip right
 *
 * The same two-pointer invariant remains inside each
 * candidate interval.
 *
 *
 * ------------------------------------------------
 * Variation 2
 * ------------------------------------------------
 *
 * Strict Palindrome
 *
 * No filtering.
 *
 * Pattern stays identical.
 *
 * Skip logic disappears.
 *
 *
 * ------------------------------------------------
 * Variation 3
 * ------------------------------------------------
 *
 * Linked List Palindrome
 *
 * Pattern changes.
 *
 * Reason
 * ------
 * Random access disappears.
 *
 * Need:
 *
 * Fast & Slow Pointer
 *
 * +
 *
 * Reverse second half.
 *
 *
 * ------------------------------------------------
 * Variation 4
 * ------------------------------------------------
 *
 * Unicode Normalization
 *
 * Pattern still works.
 *
 * Character normalization becomes more complicated.
 *
 * The invariant remains unchanged.
 *
 *
 * ------------------------------------------------
 * Pattern Boundary
 * ------------------------------------------------
 *
 * Works
 * -----
 *
 * ✓ Symmetric verification
 * ✓ Mirror comparison
 * ✓ Endpoint elimination
 *
 * Does Not Work
 * -------------
 *
 * ✗ Longest palindrome
 * ✗ Edit distance
 * ✗ Minimum insertions
 * ✗ Subsequence problems
 *
 * Those require different state definitions.
 */


/*
 * ================================================================
 * 🧠 MASTERY CHECKLIST
 * ================================================================
 *
 * Q.
 * What is the invariant?
 *
 * A.
 * Everything outside the current window has already been
 * verified.
 *
 *
 * ------------------------------------------------
 *
 * Q.
 * What is the search target?
 *
 * A.
 * The next unchecked valid character from both ends.
 *
 *
 * ------------------------------------------------
 *
 * Q.
 * What is the discard rule?
 *
 * A.
 * Invalid characters disappear individually.
 * Matching valid characters disappear together.
 *
 *
 * ------------------------------------------------
 *
 * Q.
 * Why does termination prove correctness?
 *
 * A.
 * Every mirrored valid pair has been checked exactly once.
 *
 *
 * ------------------------------------------------
 *
 * Q.
 * Why does the naive solution consume more memory?
 *
 * A.
 * It explicitly constructs the normalized string.
 *
 *
 * ------------------------------------------------
 *
 * Q.
 * Which edge cases should you mentally verify?
 *
 * A.
 * Empty normalization,
 * all punctuation,
 * single character,
 * digits,
 * uppercase letters,
 * repeated punctuation.
 *
 *
 * ------------------------------------------------
 *
 * Q.
 * Are you debugging-ready?
 *
 * A.
 * Verify:
 *
 * 1. Skip logic.
 * 2. Lowercase comparison.
 * 3. Pointer movement.
 * 4. Early mismatch return.
 * 5. Pointer crossing.
 *
 *
 * ------------------------------------------------
 *
 * Q.
 * Are you variant-ready?
 *
 * A.
 * Yes.
 *
 * Only the state transition changes.
 *
 * The shrinking-window invariant survives.
 *
 *
 * ------------------------------------------------
 *
 * Q.
 * Where does this pattern stop working?
 *
 * A.
 * When symmetry is no longer sufficient to determine
 * correctness.
 */


/*
 * ================================================================
 * ⚫ PATTERN MAPPING
 * ================================================================
 *
 * Problem
 * -------------------------------
 * Valid Palindrome
 *
 * Pattern
 * -------------------------------
 * Opposing Two Pointers
 *
 * Search Space
 * -------------------------------
 * Current unchecked interval
 *
 * State
 * -------------------------------
 * (left, right)
 *
 * Transition
 * -------------------------------
 * Skip invalid
 * or
 * Verify mirrored pair
 *
 * Discard Rule
 * -------------------------------
 * Remove one invalid endpoint
 * or
 * remove one verified mirrored pair
 *
 * Correctness
 * -------------------------------
 * Every valid character is processed exactly once.
 *
 * Termination
 * -------------------------------
 * left >= right
 *
 * Complexity
 * -------------------------------
 * Time  : O(n)
 * Space : O(1)
 */


/*
 * ================================================================
 * IMPLEMENTATION RECONSTRUCTION DRILL
 * ================================================================
 *
 * Without looking at the solution,
 * reconstruct it in this exact order:
 *
 * 1. Null check.
 *
 * 2. left = 0.
 *
 * 3. right = length - 1.
 *
 * 4. while (left < right)
 *
 * 5. Skip invalid left.
 *
 * 6. Skip invalid right.
 *
 * 7. Compare lowercase characters.
 *
 * 8. Return false on mismatch.
 *
 * 9. Move both pointers.
 *
 * 10. Return true.
 */


/*
 * ================================================================
 * DEBUGGING CHECKLIST
 * ================================================================
 *
 * If the answer is wrong:
 *
 * □ Are punctuation characters skipped?
 *
 * □ Is lowercase conversion performed before comparison?
 *
 * □ Is only the invalid pointer advanced?
 *
 * □ Are both pointers advanced after a successful match?
 *
 * □ Is the comparison executed only after both pointers
 *   reference valid characters?
 *
 * □ Does the loop terminate using left < right?
 */

    /*
     * ================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ================================================================
     *
     * Run with assertions enabled:
     *
     * java -ea ValidPalindrome
     */

    public static void main(String[] args) {

        /*
         * Happy path.
         *
         * Mixed case and punctuation should be ignored.
         */
        assert Optimal.isPalindrome("A man, a plan, a canal: Panama");

        /*
         * Representative negative example.
         */
        assert !Optimal.isPalindrome("race a car");

        /*
         * Empty normalized string.
         */
        assert Optimal.isPalindrome(" ");

        /*
         * Single character.
         */
        assert Optimal.isPalindrome("a");

        /*
         * Only punctuation.
         */
        assert Optimal.isPalindrome(".,:;!?");

        /*
         * Digits also participate.
         */
        assert Optimal.isPalindrome("12321");

        /*
         * Digits with punctuation.
         */
        assert Optimal.isPalindrome("1,2,3,2,1");

        /*
         * Mixed letters and digits.
         */
        assert Optimal.isPalindrome("A1b2B1a");

        /*
         * Case-insensitive comparison.
         */
        assert Optimal.isPalindrome("Aa");

        /*
         * Consecutive punctuation.
         */
        assert Optimal.isPalindrome("...Madam,,,,");

        /*
         * Immediate mismatch.
         */
        assert !Optimal.isPalindrome("ab");

        /*
         * Mismatch after skipping punctuation.
         */
        assert !Optimal.isPalindrome("a.,b");

        /*
         * Null handling.
         */
        assert !Optimal.isPalindrome(null);

        /*
         * Verify all implementations agree.
         */
        String[] samples = {
                "",
                " ",
                ".,",
                "A",
                "Aa",
                "ab",
                "abc",
                "abba",
                "abcba",
                "0P",
                "1a2",
                "1a1",
                "No 'x' in Nixon",
                "Able was I, ere I saw Elba",
                "Was it a car or a cat I saw?",
                "race a car",
                "A man, a plan, a canal: Panama"
        };

        for (String sample : samples) {

            boolean brute = BruteForce.isPalindrome(sample);
            boolean improved = Improved.isPalindrome(sample);
            boolean optimal = Optimal.isPalindrome(sample);

            assert brute == improved
                    : "BruteForce and Improved disagree for: " + sample;

            assert improved == optimal
                    : "Improved and Optimal disagree for: " + sample;
        }

        System.out.println("All assertions passed.");
    }

    /*
     * ================================================================
     * 🧘 FINAL CLOSURE STATEMENT
     * ================================================================
     *
     * I understand the invariant.
     *
     * I can re-derive the solution.
     *
     * I can physically reconstruct the implementation under pressure.
     *
     * This chapter is complete.
     */
}
