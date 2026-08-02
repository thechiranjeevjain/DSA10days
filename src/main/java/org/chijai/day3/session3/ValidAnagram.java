package org.chijai.day3.session3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 *                          VALID ANAGRAM
 * ============================================================================
 *
 * Difficulty
 * ----------
 * Easy
 *
 * Tags
 * ----
 * String
 * Hash Table
 * Counting
 * Sorting
 *
 * Problem
 * -------
 * Given two strings s and t, return true if t is an anagram of s,
 * and false otherwise.
 *
 * An anagram is formed by rearranging every character of another string
 * using exactly the same characters with exactly the same frequencies.
 *
 * Constraints
 * -----------
 * 1 <= s.length, t.length <= 5 * 10^4
 * s and t consist of lowercase English letters.
 *
 * Follow-up
 * ---------
 * What if the input contains Unicode characters?
 *
 * Representative Examples
 * -----------------------
 * Example 1
 * s = "anagram"
 * t = "nagaram"
 * Output = true
 *
 * Example 2
 * s = "rat"
 * t = "car"
 * Output = false
 *
 * Example 3
 * s = "a"
 * t = "a"
 * Output = true
 *
 * Example 4
 * s = "aa"
 * t = "ab"
 * Output = false
 *
 * Official LeetCode
 * -----------------
 * https://leetcode.com/problems/valid-anagram/
 */
public class ValidAnagram {

    /*
     * =========================================================================
     * 🔵 CORE PATTERN OVERVIEW
     * =========================================================================
     *
     * Pattern
     * -------
     * Frequency Counting
     *
     * Archetype
     * ---------
     * Verify whether two collections contain identical element frequencies.
     *
     * Core Invariant
     * --------------
     * After processing corresponding characters,
     * every frequency difference represents:
     *
     * frequency(s) - frequency(t)
     *
     * At completion,
     * every bucket must equal zero.
     *
     * Why It Works
     * ------------
     * Two strings are anagrams iff:
     *
     * 1. Lengths are equal.
     * 2. Every character appears exactly the same number of times.
     *
     * Counting directly verifies this mathematical definition.
     *
     * Recognition Signals
     * -------------------
     * Look for:
     *
     * • same characters
     * • permutation
     * • rearrangement
     * • identical multiset
     * • frequency comparison
     *
     * When To Use
     * -----------
     * ✓ Character frequencies matter.
     * ✓ Order does NOT matter.
     * ✓ Alphabet size is bounded.
     *
     * When NOT To Use
     * ---------------
     * ✗ Relative ordering matters.
     * ✗ Subsequence problems.
     * ✗ Prefix/suffix matching.
     * ✗ Adjacent relationships matter.
     *
     * Comparison With Similar Patterns
     * --------------------------------
     *
     * Sorting
     * -------
     * Time : O(n log n)
     * Space: O(1) or O(n)
     *
     * Easier to think about,
     * slower because ordering is unnecessary.
     *
     * Frequency Counting
     * ------------------
     * Time : O(n)
     * Space: O(1) for fixed alphabet.
     *
     * Directly checks the required property.
     *
     * HashMap Counting
     * ----------------
     * Used when alphabet size is unknown or very large.
     *
     * Sliding Window
     * --------------
     * Similar counting array,
     * but window boundaries continuously move.
     *
     * Here the entire string is one fixed window.
     */

    /*
     * =========================================================================
     * 🟢 MENTAL MODEL & INVARIANTS
     * =========================================================================
     *
     * Mental Model
     * ------------
     * Imagine one balance sheet for every possible character.
     *
     * Processing s deposits one coin.
     *
     * Processing t withdraws one coin.
     *
     * At the end,
     * every account must return to exactly zero.
     *
     * Positive value
     * --------------
     * s contains more occurrences.
     *
     * Negative value
     * --------------
     * t contains more occurrences.
     *
     * Zero
     * ----
     * Perfect balance.
     *
     * -------------------------------------------------------------------------
     * Primary Invariant
     * -------------------------------------------------------------------------
     *
     * After processing index i,
     *
     * count[c]
     *
     * equals
     *
     * occurrences of c seen in s[0...i]
     * minus
     * occurrences of c seen in t[0...i]
     *
     * This invariant is preserved by:
     *
     * +1 for current character of s
     * -1 for current character of t
     *
     * -------------------------------------------------------------------------
     * Terminal Invariant
     * -------------------------------------------------------------------------
     *
     * Every bucket must be zero.
     *
     * If even one bucket differs,
     * the strings cannot be anagrams.
     *
     * -------------------------------------------------------------------------
     * Variable Meanings
     * -------------------------------------------------------------------------
     *
     * store[i]
     *
     * Net frequency difference for character i.
     *
     * i
     *
     * Current synchronized position in both strings.
     *
     * -------------------------------------------------------------------------
     * Allowed State Transition
     * -------------------------------------------------------------------------
     *
     * Process same index in both strings.
     *
     * Increment one bucket.
     *
     * Decrement another bucket.
     *
     * Continue.
     *
     * -------------------------------------------------------------------------
     * Forbidden State Transition
     * -------------------------------------------------------------------------
     *
     * Never compare positions directly.
     *
     * Position is irrelevant.
     *
     * Only frequencies matter.
     *
     * -------------------------------------------------------------------------
     * Termination
     * -------------------------------------------------------------------------
     *
     * Every character has been processed exactly once.
     *
     * Frequency array completely represents both strings.
     *
     * -------------------------------------------------------------------------
     * Correctness Intuition
     * -------------------------------------------------------------------------
     *
     * Every occurrence added from s must eventually be cancelled by
     * exactly one occurrence from t.
     *
     * Zero everywhere implies perfect cancellation.
     *
     * -------------------------------------------------------------------------
     * Why Naive Solutions Fail
     * -------------------------------------------------------------------------
     *
     * Character-by-character comparison assumes equal ordering.
     *
     * Example
     *
     * s = "abc"
     * t = "cab"
     *
     * Every position differs,
     * yet the strings are valid anagrams.
     *
     * Therefore order is not part of the search space.
     */

    /*
     * =========================================================================
     * 🔴 WHY WRONG SOLUTIONS FAIL
     * =========================================================================
     *
     * Mistake 1
     * ---------
     * Compare characters at identical indices.
     *
     * Why It Looks Correct
     * --------------------
     * Equal strings satisfy this.
     *
     * Violated Invariant
     * ------------------
     * Position is irrelevant.
     *
     * Counterexample
     * --------------
     * "abc"
     * "cab"
     *
     * -------------------------------------------------------------------------
     *
     * Mistake 2
     * ---------
     * Forget length check.
     *
     * Why It Looks Correct
     * --------------------
     * Frequency counting still seems reasonable.
     *
     * Violated Invariant
     * ------------------
     * Anagrams must consume every character exactly once.
     *
     * Counterexample
     * --------------
     * "a"
     * "aa"
     *
     * -------------------------------------------------------------------------
     *
     * Mistake 3
     * ---------
     * Increment counts for both strings.
     *
     * Violated Invariant
     * ------------------
     * Net difference is never computed.
     *
     * -------------------------------------------------------------------------
     *
     * Mistake 4
     * ---------
     * Decrement before increment without careful reasoning
     * in early-exit implementations.
     *
     * Can create debugging confusion.
     *
     * -------------------------------------------------------------------------
     *
     * Mistake 5
     * ---------
     * Use array of size 26 for Unicode input.
     *
     * Works only because current constraints guarantee
     * lowercase English letters.
     *
     * Interview Trap
     * --------------
     * Always mention:
     *
     * "For Unicode I would switch to HashMap<Character,Integer>."
     */

    /*
     * =========================================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * =========================================================================
     *
     * Typing Order
     * ------------
     *
     * 1.
     * Validate equal lengths.
     *
     * 2.
     * Allocate counting structure.
     *
     * 3.
     * Single synchronized loop.
     *
     * 4.
     * Increment bucket for s.
     *
     * 5.
     * Decrement bucket for t.
     *
     * 6.
     * Scan buckets.
     *
     * 7.
     * Any non-zero → false.
     *
     * 8.
     * Otherwise true.
     *
     * Function Skeleton
     * -----------------
     *
     * boolean isAnagram(...)
     *
     * Variables
     * ---------
     *
     * int[] count
     * int i
     *
     * Loop Skeleton
     * -------------
     *
     * for each index
     *
     *     increment
     *
     *     decrement
     *
     * Verification
     * ------------
     *
     * for every bucket
     *
     *     bucket != 0
     *         false
     *
     * return true
     */

    /*
     * =========================================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * =========================================================================
     *
     * lengths differ
     *     return false
     *
     * create frequency array
     *
     * for each index
     *     add from first
     *     subtract from second
     *
     * verify all buckets zero
     *
     * return true
     */

    /*
     * =========================================================================
     * 6. SOLUTION CLASSES
     * =========================================================================
     */

    /**
     * -------------------------------------------------------------------------
     * Brute Force
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     * Sort both strings.
     *
     * Equal sorted arrays imply equal frequencies.
     *
     * Invariant
     * ---------
     * Sorting groups identical characters together.
     *
     * Limitation
     * ----------
     * Pays O(n log n) to establish ordering,
     * even though ordering is irrelevant.
     *
     * Complexity
     * ----------
     * Time  : O(n log n)
     * Space : O(n) depending on implementation.
     *
     * Interview Usefulness
     * --------------------
     * Good baseline.
     * Usually improved to counting.
     */
    static class BruteForceSorting {

        static boolean isAnagram(String s, String t) {

            if (s.length() != t.length()) {
                return false;
            }

            char[] first = s.toCharArray();
            char[] second = t.toCharArray();

            Arrays.sort(first);
            Arrays.sort(second);

            return Arrays.equals(first, second);
        }
    }

    /**
     * -------------------------------------------------------------------------
     * Improved
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     * Fixed-size counting array for lowercase English letters.
     *
     * Invariant
     * ---------
     * Each bucket stores net frequency difference.
     *
     * Improvement
     * -----------
     * Eliminates sorting.
     *
     * Complexity
     * ----------
     * Time  : O(n)
     * Space : O(1)
     *
     * Interview Usefulness
     * --------------------
     * Preferred whenever alphabet size is fixed.
     */
    static class ImprovedCounting26 {

        static boolean isAnagram(String s, String t) {

            if (s.length() != t.length()) {
                return false;
            }

            int[] store = new int[26];

            for (int i = 0; i < s.length(); i++) {

                store[s.charAt(i) - 'a']++;

                store[t.charAt(i) - 'a']--;
            }

            for (int frequencyDifference : store) {

                if (frequencyDifference != 0) {
                    return false;
                }
            }

            return true;
        }
    }
    /**
     * -------------------------------------------------------------------------
     * Optimal (Interview Preferred)
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     * Generalize the counting array from 26 lowercase letters to the
     * entire ASCII character set.
     *
     * The invariant remains identical:
     *
     * count[c] =
     * occurrences of c in s processed so far
     * -
     * occurrences of c in t processed so far
     *
     * Because every decrement happens while scanning t, we can detect an
     * impossible state immediately.
     *
     * If a bucket becomes negative, then t has already consumed more copies
     * of a character than s has supplied.
     *
     * No future character can repair that deficit because characters are
     * independent buckets.
     *
     * Therefore an early return is correct.
     *
     * Correctness
     * -----------
     * The invariant is preserved after every processed character.
     *
     * A negative bucket violates the invariant permanently.
     *
     * If every bucket finishes at zero, both multisets are identical.
     *
     * Complexity
     * ----------
     * Time  : O(n)
     * Space : O(1)
     *
     * Interview Usefulness
     * --------------------
     * This is typically the best interview implementation because:
     *
     * • linear time
     * • constant space
     * • easy invariant
     * • supports early failure
     * • mechanically reconstructable
     */
    static class OptimalCountingASCII {

        static boolean isAnagram(String s, String t) {

            // Invariant: unequal lengths can never be repaired.
            if (s.length() != t.length()) {
                return false;
            }

            int[] count = new int[128];

            // Invariant:
            // count[c] stores net supply remaining for character c.
            for (char c : s.toCharArray()) {
                ++count[c];
            }

            for (char c : t.toCharArray()) {

                // Invariant:
                // bucket must never become negative.
                if (--count[c] < 0) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * -------------------------------------------------------------------------
     * Unicode Version
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     * When the alphabet is not bounded,
     * replace the counting array with HashMap.
     *
     * The invariant does not change.
     *
     * Only the storage structure changes.
     *
     * Complexity
     * ----------
     * Time  : O(n)
     * Space : O(k)
     *
     * k = distinct characters.
     */
    static class UnicodeHashMap {

        static boolean isAnagram(String s, String t) {

            if (s.length() != t.length()) {
                return false;
            }

            Map<Character, Integer> frequency = new HashMap<>();

            for (int i = 0; i < s.length(); i++) {

                char c = s.charAt(i);

                frequency.put(c, frequency.getOrDefault(c, 0) + 1);
            }

            for (int i = 0; i < t.length(); i++) {

                char c = t.charAt(i);

                Integer remaining = frequency.get(c);

                // Invariant:
                // every character in t must already exist in s.
                if (remaining == null) {
                    return false;
                }

                if (remaining == 1) {

                    frequency.remove(c);

                } else {

                    frequency.put(c, remaining - 1);
                }
            }

            return frequency.isEmpty();
        }
    }

/*
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * Explain The Invariant
 * ---------------------
 * I maintain one frequency difference bucket per character.
 *
 * Every occurrence from s increases the bucket.
 *
 * Every occurrence from t decreases the same bucket.
 *
 * If all buckets become zero, both strings contain identical
 * character frequencies.
 *
 * -------------------------------------------------------------------------
 * Search Space
 * -------------------------------------------------------------------------
 *
 * We are not searching over positions.
 *
 * We are verifying equality of frequency distributions.
 *
 * -------------------------------------------------------------------------
 * Discard Rule
 * -------------------------------------------------------------------------
 *
 * If lengths differ,
 * immediately discard.
 *
 * During the ASCII early-exit solution,
 * if any bucket becomes negative,
 * immediately discard because t has already consumed more occurrences
 * than s can possibly provide.
 *
 * -------------------------------------------------------------------------
 * Correctness
 * -------------------------------------------------------------------------
 *
 * Every processed character updates exactly one independent bucket.
 *
 * Since buckets never interfere,
 * equality of every bucket implies equality of every character count.
 *
 * -------------------------------------------------------------------------
 * Termination
 * -------------------------------------------------------------------------
 *
 * After scanning every character once,
 * every required frequency has been accounted for.
 *
 * -------------------------------------------------------------------------
 * In-place Feasibility
 * -------------------------------------------------------------------------
 *
 * No.
 *
 * We require external state for frequencies.
 *
 * -------------------------------------------------------------------------
 * Streaming Feasibility
 * -------------------------------------------------------------------------
 *
 * Yes.
 *
 * If both streams arrive simultaneously,
 * frequency differences can be updated online.
 *
 * -------------------------------------------------------------------------
 * When NOT To Use
 * -------------------------------------------------------------------------
 *
 * Do not use this pattern when:
 *
 * • relative order matters
 * • substring positions matter
 * • lexicographical comparison is required
 * • adjacency is part of correctness
 */

/*
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 * -------
 * Same characters?
 * Rearrangement?
 * Permutation?
 *
 * Invariant
 * ---------
 * Bucket =
 * frequency(s)
 * -
 * frequency(t)
 *
 * Search Target
 * -------------
 * Every bucket equals zero.
 *
 * Discard Rule
 * ------------
 * Different length.
 *
 * Negative bucket (ASCII early-exit version).
 *
 * Common Trap
 * -----------
 * Comparing positions instead of frequencies.
 *
 * Edge Cases
 * ----------
 * Empty strings.
 *
 * Single character.
 *
 * Duplicate characters.
 *
 * Unicode alphabet.
 *
 * One-Liner
 * ---------
 * Equal frequencies imply an anagram.
 *
 * Re-derivation Cue
 * -----------------
 * Add from first.
 *
 * Subtract from second.
 *
 * Verify complete cancellation.
 */

/*
 * =========================================================================
 * 🔄 VARIATIONS & TWEAKS
 * =========================================================================
 *
 * Variation 1
 * -----------
 * Sort both strings.
 *
 * Invariant
 * ---------
 * Equal sorted sequences imply equal frequencies.
 *
 * Trade-off
 * ---------
 * Simpler.
 * Slower.
 *
 * -------------------------------------------------------------------------
 *
 * Variation 2
 * -----------
 * 26-element counting array.
 *
 * Invariant
 * ---------
 * One bucket for every lowercase letter.
 *
 * Requirement
 * -----------
 * Alphabet must be fixed.
 *
 * -------------------------------------------------------------------------
 *
 * Variation 3
 * -----------
 * ASCII counting array.
 *
 * Reasoning Change
 * ----------------
 * Increase alphabet size without changing the invariant.
 *
 * -------------------------------------------------------------------------
 *
 * Variation 4
 * -----------
 * Unicode HashMap.
 *
 * Reasoning Change
 * ----------------
 * Replace array indexing with hash lookup.
 *
 * Invariant remains unchanged.
 *
 * -------------------------------------------------------------------------
 *
 * Variation 5
 * -----------
 * Parallel stream processing.
 *
 * Both streams update the same frequency difference structure.
 *
 * Final verification remains identical.
 *
 * -------------------------------------------------------------------------
 *
 * Pattern Break
 * -------------
 * If order becomes important,
 * counting alone is insufficient.
 *
 * Example
 * -------
 * Subsequence.
 *
 * Palindrome.
 *
 * String matching.
 *
 * Those require additional structural information.
 */
/*
 * =========================================================================
 * 🧠 MASTERY CHECKLIST
 * =========================================================================
 *
 * □ Do I know the Pattern?
 *
 * Yes.
 *
 * This is the Frequency Counting Pattern.
 *
 * -------------------------------------------------------------------------
 *
 * □ What is the Invariant?
 *
 * Every bucket stores:
 *
 *      occurrences processed in s
 *      -
 *      occurrences processed in t
 *
 * Correctness requires every bucket to become exactly zero.
 *
 * -------------------------------------------------------------------------
 *
 * □ What is the Search Target?
 *
 * Not an index.
 *
 * Not a substring.
 *
 * The search target is equality of frequency distributions.
 *
 * -------------------------------------------------------------------------
 *
 * □ What is the Discard Rule?
 *
 * 1.
 * Different lengths.
 *
 * 2.
 * (ASCII early-exit version)
 * Any bucket becomes negative.
 *
 * -------------------------------------------------------------------------
 *
 * □ Why does the algorithm terminate?
 *
 * Every character is processed exactly once.
 *
 * Afterwards every bucket fully represents the net frequency
 * difference between both strings.
 *
 * -------------------------------------------------------------------------
 *
 * □ Why does the naive solution fail?
 *
 * It compares ordering.
 *
 * Anagrams preserve frequency,
 * not position.
 *
 * -------------------------------------------------------------------------
 *
 * □ Which edge cases should I always test?
 *
 * • identical strings
 * • different lengths
 * • repeated letters
 * • one missing occurrence
 * • single character
 * • empty strings
 * • Unicode
 *
 * -------------------------------------------------------------------------
 *
 * □ Can I debug from the invariant?
 *
 * Yes.
 *
 * Print every bucket.
 *
 * Any non-zero bucket immediately identifies
 * the mismatched character.
 *
 * -------------------------------------------------------------------------
 *
 * □ Am I ready for variants?
 *
 * Yes.
 *
 * Swap:
 *
 * int[26]
 *
 * →
 *
 * int[128]
 *
 * →
 *
 * HashMap<Character,Integer>
 *
 * without changing the invariant.
 *
 * -------------------------------------------------------------------------
 *
 * □ Pattern Boundary
 *
 * This pattern solves:
 *
 * "same multiset?"
 *
 * It does NOT solve:
 *
 * "same order?"
 *
 * "same position?"
 *
 * "substring?"
 *
 * "sequence?"
 */

/*
 * =========================================================================
 * ⚫ PATTERN MAPPING
 * =========================================================================
 *
 * Related Problems
 * ----------------
 *
 * Valid Anagram
 *      Compare two frequency distributions.
 *
 * Find All Anagrams in a String
 *      Sliding window + frequency counting.
 *
 * Group Anagrams
 *      Frequency signature or sorted signature.
 *
 * Ransom Note
 *      Available frequency >= required frequency.
 *
 * First Unique Character
 *      Frequency counting followed by scan.
 *
 * Isomorphic Strings
 *      Mapping pattern instead of counting.
 *
 * Valid Palindrome
 *      Two pointers.
 *
 * Permutation in String
 *      Sliding window maintaining identical invariant.
 */

/*
 * =========================================================================
 * ⚫ DEBUGGING GUIDE
 * =========================================================================
 *
 * Symptom
 * -------
 * Returns false for valid anagrams.
 *
 * Inspect
 * -------
 * Length check.
 *
 * Character indexing.
 *
 * Increment/decrement direction.
 *
 * -------------------------------------------------------------------------
 *
 * Symptom
 * -------
 * ArrayIndexOutOfBoundsException
 *
 * Inspect
 * -------
 * Alphabet assumption.
 *
 * Input may contain Unicode while implementation assumes
 * lowercase English letters.
 *
 * -------------------------------------------------------------------------
 *
 * Symptom
 * -------
 * ASCII implementation unexpectedly returns true.
 *
 * Inspect
 * -------
 * Did decrement happen before comparison?
 *
 * Correct check:
 *
 * if (--count[c] < 0)
 *
 * -------------------------------------------------------------------------
 *
 * Symptom
 * -------
 * HashMap version leaves entries behind.
 *
 * Inspect
 * -------
 * Remove keys when frequency reaches zero.
 *
 * Otherwise the final map emptiness test fails.
 *
 * -------------------------------------------------------------------------
 *
 * Debug Trick
 * -----------
 * Print:
 *
 * character
 *
 * frequency difference
 *
 * Every non-zero bucket immediately identifies
 * the mismatch.
 */

/*
 * =========================================================================
 * ⚫ COMPLEXITY SUMMARY
 * =========================================================================
 *
 * --------------------------------------------------------------
 * Approach              Time          Space
 * --------------------------------------------------------------
 * Sorting              O(n log n)    O(1)/O(n)
 * Count[26]            O(n)          O(1)
 * Count[128]           O(n)          O(1)
 * HashMap              O(n)          O(k)
 * --------------------------------------------------------------
 *
 * k = distinct characters.
 */

/*
 * =========================================================================
 * ⚫ IMPLEMENTATION RECONSTRUCTION DRILL
 * =========================================================================
 *
 * Without looking at the solution,
 * reproduce the implementation mechanically.
 *
 * Step 1
 * ------
 * Compare lengths.
 *
 * Step 2
 * ------
 * Allocate counting structure.
 *
 * Step 3
 * ------
 * Process every index once.
 *
 * Step 4
 * ------
 * Increase bucket for s.
 *
 * Step 5
 * ------
 * Decrease bucket for t.
 *
 * Step 6
 * ------
 * Verify complete cancellation.
 *
 * Step 7
 * ------
 * Return true.
 *
 * If you can perform these seven steps from memory,
 * you understand the implementation rather than
 * memorizing code.
 */

/*
 * =========================================================================
 * ⚫ INTERVIEW FOLLOW-UP QUESTIONS
 * =========================================================================
 *
 * Q.
 * Why is length checked first?
 *
 * A.
 * Every character must be consumed exactly once.
 * Different lengths violate the definition immediately.
 *
 * -------------------------------------------------------------------------
 *
 * Q.
 * Why is counting faster than sorting?
 *
 * A.
 * Sorting establishes an ordering we never use.
 * Counting directly verifies the required invariant.
 *
 * -------------------------------------------------------------------------
 *
 * Q.
 * Why can the ASCII solution return early?
 *
 * A.
 * A negative bucket means t already requires more copies
 * than s has supplied.
 *
 * No future character can repair that deficit because
 * buckets are independent.
 *
 * -------------------------------------------------------------------------
 *
 * Q.
 * Why does the HashMap solution remove zero-count entries?
 *
 * A.
 * It keeps the representation canonical.
 *
 * An empty map directly represents perfect cancellation.
 *
 * -------------------------------------------------------------------------
 *
 * Q.
 * How would you support arbitrary Unicode?
 *
 * A.
 * Replace the fixed counting array with
 * HashMap<Character,Integer>.
 *
 * The invariant is unchanged.
 */

/*
 * =========================================================================
 * ⚫ MEMORY PEGS
 * =========================================================================
 *
 * "Deposit from first."
 *
 * "Withdraw from second."
 *
 * "Everything must cancel."
 *
 * ---------------------------------------
 *
 * Length
 * →
 * Count
 * →
 * Cancel
 * →
 * Verify
 *
 * ---------------------------------------
 *
 * Frequency difference,
 * not frequency itself,
 * is the central invariant.
 */
    /*
     * =========================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * =========================================================================
     */

    public static void main(String[] args) {

        /*
         * -------------------------------------------------------------
         * Representative LeetCode Examples
         * -------------------------------------------------------------
         */

        // Valid anagram.
        assert BruteForceSorting.isAnagram("anagram", "nagaram");
        assert ImprovedCounting26.isAnagram("anagram", "nagaram");
        assert OptimalCountingASCII.isAnagram("anagram", "nagaram");
        assert UnicodeHashMap.isAnagram("anagram", "nagaram");

        // Different character frequencies.
        assert !BruteForceSorting.isAnagram("rat", "car");
        assert !ImprovedCounting26.isAnagram("rat", "car");
        assert !OptimalCountingASCII.isAnagram("rat", "car");
        assert !UnicodeHashMap.isAnagram("rat", "car");

        /*
         * -------------------------------------------------------------
         * Length Mismatch
         * -------------------------------------------------------------
         */

        // Cannot be repaired by any permutation.
        assert !BruteForceSorting.isAnagram("abc", "ab");
        assert !ImprovedCounting26.isAnagram("abc", "ab");
        assert !OptimalCountingASCII.isAnagram("abc", "ab");
        assert !UnicodeHashMap.isAnagram("abc", "ab");

        /*
         * -------------------------------------------------------------
         * Single Character
         * -------------------------------------------------------------
         */

        assert BruteForceSorting.isAnagram("a", "a");
        assert ImprovedCounting26.isAnagram("a", "a");
        assert OptimalCountingASCII.isAnagram("a", "a");
        assert UnicodeHashMap.isAnagram("a", "a");

        assert !BruteForceSorting.isAnagram("a", "b");
        assert !ImprovedCounting26.isAnagram("a", "b");
        assert !OptimalCountingASCII.isAnagram("a", "b");
        assert !UnicodeHashMap.isAnagram("a", "b");

        /*
         * -------------------------------------------------------------
         * Duplicate Characters
         * -------------------------------------------------------------
         */

        // Same multiplicities.
        assert BruteForceSorting.isAnagram("aabbcc", "ccbbaa");
        assert ImprovedCounting26.isAnagram("aabbcc", "ccbbaa");
        assert OptimalCountingASCII.isAnagram("aabbcc", "ccbbaa");
        assert UnicodeHashMap.isAnagram("aabbcc", "ccbbaa");

        // One frequency differs.
        assert !BruteForceSorting.isAnagram("aabbcc", "aabbcd");
        assert !ImprovedCounting26.isAnagram("aabbcc", "aabbcd");
        assert !OptimalCountingASCII.isAnagram("aabbcc", "aabbcd");
        assert !UnicodeHashMap.isAnagram("aabbcc", "aabbcd");

        /*
         * -------------------------------------------------------------
         * Order Is Irrelevant
         * -------------------------------------------------------------
         */

        assert BruteForceSorting.isAnagram("listen", "silent");
        assert ImprovedCounting26.isAnagram("listen", "silent");
        assert OptimalCountingASCII.isAnagram("listen", "silent");
        assert UnicodeHashMap.isAnagram("listen", "silent");

        /*
         * -------------------------------------------------------------
         * Early Exit Scenario
         * -------------------------------------------------------------
         */

        // ASCII implementation should reject as soon as a bucket
        // becomes negative.
        assert !OptimalCountingASCII.isAnagram("aaaa", "aaab");

        /*
         * -------------------------------------------------------------
         * Empty Strings
         * -------------------------------------------------------------
         *
         * Although LeetCode's constraints require length >= 1,
         * testing this improves implementation robustness.
         */

        assert BruteForceSorting.isAnagram("", "");
        assert ImprovedCounting26.isAnagram("", "");
        assert OptimalCountingASCII.isAnagram("", "");
        assert UnicodeHashMap.isAnagram("", "");

        /*
         * -------------------------------------------------------------
         * Unicode Example
         * -------------------------------------------------------------
         */

        // Unicode requires the HashMap implementation.
        assert UnicodeHashMap.isAnagram("åßç", "çåß");
        assert !UnicodeHashMap.isAnagram("åßç", "çåå");

        /*
         * -------------------------------------------------------------
         * Stress-Like Scenario
         * -------------------------------------------------------------
         */

        String first = "zzzzzzzzzzaaaaaabbbbbccccddd";
        String second = "abczzzzzzzzzdcbdcaabbaa";

        assert !BruteForceSorting.isAnagram(first, second);
        assert !ImprovedCounting26.isAnagram(first, second);
        assert !OptimalCountingASCII.isAnagram(first, second);
        assert !UnicodeHashMap.isAnagram(first, second);

        /*
         * -------------------------------------------------------------
         * Boundary Frequency Check
         * -------------------------------------------------------------
         */

        assert BruteForceSorting.isAnagram("bbbbbbbb", "bbbbbbbb");
        assert ImprovedCounting26.isAnagram("bbbbbbbb", "bbbbbbbb");
        assert OptimalCountingASCII.isAnagram("bbbbbbbb", "bbbbbbbb");
        assert UnicodeHashMap.isAnagram("bbbbbbbb", "bbbbbbbb");

        /*
         * -------------------------------------------------------------
         * Every Character Different
         * -------------------------------------------------------------
         */

        assert !BruteForceSorting.isAnagram("abcd", "efgh");
        assert !ImprovedCounting26.isAnagram("abcd", "efgh");
        assert !OptimalCountingASCII.isAnagram("abcd", "efgh");
        assert !UnicodeHashMap.isAnagram("abcd", "efgh");

        /*
         * -------------------------------------------------------------
         * Canonical Interview Case
         * -------------------------------------------------------------
         */

        assert BruteForceSorting.isAnagram("evil", "vile");
        assert ImprovedCounting26.isAnagram("evil", "vile");
        assert OptimalCountingASCII.isAnagram("evil", "vile");
        assert UnicodeHashMap.isAnagram("evil", "vile");

        /*
         * -------------------------------------------------------------
         * Verify All Implementations Agree
         * -------------------------------------------------------------
         */

        String[] left = {
                "abc",
                "abc",
                "xyz",
                "hello",
                "leetcode",
                "mississippi"
        };

        String[] right = {
                "bca",
                "abd",
                "zyx",
                "hlelo",
                "codeleet",
                "mipsissispi"
        };

        for (int i = 0; i < left.length; i++) {

            boolean brute = BruteForceSorting.isAnagram(left[i], right[i]);
            boolean improved = ImprovedCounting26.isAnagram(left[i], right[i]);
            boolean optimal = OptimalCountingASCII.isAnagram(left[i], right[i]);
            boolean unicode = UnicodeHashMap.isAnagram(left[i], right[i]);

            assert brute == improved;
            assert improved == optimal;
            assert optimal == unicode;
        }

        System.out.println("All self-verifying tests passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/

