package org.chijai.day1.session1;

import java.util.Arrays;
import java.util.HashMap;

public class RansomNote {

    /*
     * ================================================================
     * 2. 📘 PRIMARY PROBLEM
     * ================================================================
     *
     * Title:
     * Ransom Note
     *
     * Difficulty:
     * Easy
     *
     * Tags:
     * Hash Table
     * Array
     * Counting
     * Frequency Table
     * Greedy Counting
     *
     * LeetCode:
     * https://leetcode.com/problems/ransom-note/
     *
     * Problem Description
     * -------------------
     * Given two strings:
     *
     *     ransomNote
     *     magazine
     *
     * determine whether ransomNote can be constructed using characters
     * from magazine.
     *
     * Every character in magazine may be used at most once.
     *
     * Return true if construction is possible.
     * Otherwise return false.
     *
     * Constraints
     * -----------
     * 1 <= ransomNote.length, magazine.length <= 100000
     * ransomNote and magazine contain only lowercase English letters.
     *
     * Representative Examples
     * -----------------------
     *
     * Example 1
     * ransomNote = "a"
     * magazine   = "b"
     * Output = false
     *
     * Explanation
     * Magazine does not contain 'a'.
     *
     * ------------------------------------------------
     *
     * Example 2
     * ransomNote = "aa"
     * magazine   = "ab"
     * Output = false
     *
     * Explanation
     * Only one 'a' exists.
     *
     * ------------------------------------------------
     *
     * Example 3
     * ransomNote = "aa"
     * magazine   = "aab"
     * Output = true
     *
     * Explanation
     * Magazine contains two copies of 'a'.
     */

    /*
     * ================================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ================================================================
     *
     * Pattern
     * -------
     * Frequency Counting
     *
     * Archetype
     * ---------
     * Resource Consumption
     *
     * Core Invariant
     * --------------
     * The frequency table always represents the unused characters that
     * still remain available inside the magazine.
     *
     * Every successful character match consumes exactly one unit from
     * this remaining inventory.
     *
     * Why It Works
     * ------------
     * The problem is not about ordering.
     *
     * It is purely about whether enough copies of every character exist.
     *
     * Therefore only frequencies matter.
     *
     * Recognition Signals
     * -------------------
     * Look for:
     *
     * - "each element may be used once"
     * - duplicates matter
     * - order irrelevant
     * - availability checking
     * - inventory matching
     * - multiset comparison
     *
     * When To Use
     * -----------
     * - character counting
     * - anagram problems
     * - inventory verification
     * - resource allocation
     * - duplicate accounting
     *
     * When NOT To Use
     * ---------------
     * - ordering matters
     * - substring search
     * - adjacency constraints
     * - sequence alignment
     * - positional matching
     *
     * Comparison
     * ----------
     *
     * Frequency Table
     *     cares only about counts.
     *
     * Two Pointers
     *     requires useful ordering.
     *
     * Sliding Window
     *     tracks a moving interval.
     *
     * Hash Set
     *     ignores duplicate counts.
     *
     * Sorting
     *     solves counting indirectly at O(n log n),
     *     while frequency counting is O(n).
     */

    /*
     * ================================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ================================================================
     *
     * Mental Model
     * ------------
     *
     * Imagine magazine is a warehouse.
     *
     * Every letter is inventory.
     *
     * ransomNote is a list of customer requests.
     *
     * Every fulfilled request permanently removes one item from inventory.
     *
     * If inventory ever becomes negative,
     * we promised something that does not exist.
     *
     * Therefore construction is impossible.
     *
     * ------------------------------------------------------------
     * Primary Invariant
     * ------------------------------------------------------------
     *
     * count[x]
     *
     * always equals
     *
     * remaining unused copies of character x
     *
     * after processing part of ransomNote.
     *
     * ------------------------------------------------------------
     * Variable Meaning
     * ------------------------------------------------------------
     *
     * count[c]
     *     remaining inventory
     *
     * ransom pointer
     *     next requested character
     *
     * magazine preprocessing
     *     builds initial inventory
     *
     * ------------------------------------------------------------
     * Allowed State Transition
     * ------------------------------------------------------------
     *
     * Need character c.
     *
     * Inventory positive.
     *
     * Consume exactly one.
     *
     * count[c]--
     *
     * Invariant remains valid.
     *
     * ------------------------------------------------------------
     * Forbidden Transition
     * ------------------------------------------------------------
     *
     * Consume character when
     *
     * count[c] == 0
     *
     * or
     *
     * count[c] < 0
     *
     * This violates the invariant because inventory cannot become
     * negative in any valid construction.
     *
     * ------------------------------------------------------------
     * Search Space
     * ------------------------------------------------------------
     *
     * We never search for positions.
     *
     * We search only over remaining inventory.
     *
     * ------------------------------------------------------------
     * State
     * ------------------------------------------------------------
     *
     * Current remaining counts of every character.
     *
     * ------------------------------------------------------------
     * Transition
     * ------------------------------------------------------------
     *
     * Request one character.
     *
     * Consume one inventory unit.
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * Every requested character successfully consumed.
     *
     * Inventory invariant still holds.
     *
     * Therefore answer is true.
     *
     * ------------------------------------------------------------
     * Why Naive Solutions Fail
     * ------------------------------------------------------------
     *
     * Naive idea:
     *
     * For every ransom character,
     * scan magazine to find one unused copy.
     *
     * Time becomes
     *
     * O(n × m)
     *
     * because each lookup may rescan almost the entire magazine.
     *
     * Frequency counting compresses the entire magazine into a fixed-size
     * inventory representation before answering requests.
     */

    /*
     * ================================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ================================================================
     *
     * Mistake 1
     * ---------
     * Using HashSet.
     *
     * Why it seems correct
     * --------------------
     * Checks whether letters exist.
     *
     * Violated Invariant
     * ------------------
     * Duplicate counts disappear.
     *
     * Counterexample
     * --------------
     * ransom = "aa"
     * magazine = "ab"
     *
     * HashSet incorrectly says true.
     *
     * ------------------------------------------------------------
     *
     * Mistake 2
     * ---------
     * Never decrementing frequency.
     *
     * Why it seems correct
     * --------------------
     * Character exists.
     *
     * Violated Invariant
     * ------------------
     * Inventory is never consumed.
     *
     * Counterexample
     * --------------
     * ransom = "aaa"
     * magazine = "ab"
     *
     * ------------------------------------------------------------
     *
     * Mistake 3
     * ---------
     * Decrement before validating when using
     * a different invariant.
     *
     * Two correct implementations exist:
     *
     * Version A
     *
     * if (count[c] <= 0)
     *     return false;
     * count[c]--;
     *
     * Version B
     *
     * if (--count[c] < 0)
     *     return false;
     *
     * Mixing these two styles creates off-by-one bugs.
     *
     * ------------------------------------------------------------
     *
     * Mistake 4
     * ---------
     * Sorting both strings.
     *
     * Works.
     *
     * But unnecessarily increases complexity from
     * O(n)
     * to
     * O(n log n).
     *
     * ------------------------------------------------------------
     *
     * Interview Trap
     * --------------
     *
     * The interviewer often asks:
     *
     * "Why is an array faster than HashMap?"
     *
     * Answer:
     *
     * Lower constant factors.
     * Continuous memory.
     * No hashing.
     * No boxing.
     * Fixed alphabet size.
     */

    /*
     * ================================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * ================================================================
     *
     * Mechanical Typing Order
     * -----------------------
     *
     * 1. Create method.
     *
     * 2. Allocate frequency array.
     *
     * 3. Count every magazine character.
     *
     * 4. Traverse ransomNote.
     *
     * 5. Validate inventory.
     *
     * 6. Consume one character.
     *
     * 7. Finish traversal.
     *
     * 8. Return true.
     *
     * Function Skeleton
     * -----------------
     *
     * method(...)
     *     create counts
     *     build inventory
     *     consume requests
     *     return result
     *
     * Variable Initialization
     * -----------------------
     *
     * int[] count = new int[128]
     *
     * Loop Skeleton
     * -------------
     *
     * build counts
     *
     * consume counts
     *
     * Branch Logic
     * ------------
     *
     * inventory empty?
     *     fail
     *
     * otherwise
     *     consume
     *
     * Pointer Movement
     * ----------------
     *
     * Single forward scan over magazine.
     *
     * Single forward scan over ransomNote.
     *
     * Return
     * ------
     *
     * true only if every request succeeds.
     */

    /*
     * ================================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ================================================================
     *
     * build frequency
     *
     * for every request
     *     if unavailable
     *         false
     *     consume
     *
     * true
     */

    /*
     * ================================================================
     * 6. SOLUTION CLASSES
     * ================================================================
     */

    /**
     * ------------------------------------------------------------
     * Brute Force
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * For every ransom character,
     * search magazine for one unused occurrence.
     *
     * Invariant
     * ---------
     * Every magazine index is used at most once.
     *
     * Limitation
     * ----------
     * Repeated rescanning.
     *
     * Complexity
     * ----------
     * Time  : O(n × m)
     * Space : O(m)
     *
     * Interview Usefulness
     * --------------------
     * Good baseline only.
     */
    static class BruteForce {

        static boolean canConstruct(String ransomNote, String magazine) {

            boolean[] used = new boolean[magazine.length()];

            for (char need : ransomNote.toCharArray()) {

                boolean found = false;

                for (int i = 0; i < magazine.length(); i++) {

                    if (!used[i] && magazine.charAt(i) == need) {
                        used[i] = true;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * ------------------------------------------------------------
     * Improved
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Store frequencies inside a HashMap.
     *
     * Invariant
     * ---------
     * Map always stores remaining inventory.
     *
     * Improvement
     * -----------
     * Removes repeated rescanning.
     *
     * Complexity
     * ----------
     * Time  : O(n + m)
     * Space : O(k)
     *
     * where k is the number of distinct characters.
     *
     * Interview Usefulness
     * --------------------
     * Useful when alphabet size is unknown.
     */
    static class ImprovedHashMap {

        static boolean canConstruct(String ransomNote, String magazine) {

            HashMap<Character, Integer> counts = new HashMap<>();

            for (char c : magazine.toCharArray()) {
                counts.put(c, counts.getOrDefault(c, 0) + 1);
            }

            for (char c : ransomNote.toCharArray()) {

                Integer remaining = counts.get(c);

                // 🟢 Invariant:
                // Remaining inventory must stay non-negative.
                if (remaining == null || remaining == 0) {
                    return false;
                }

                counts.put(c, remaining - 1);
            }

            return true;
        }
    }


    /**
     * ------------------------------------------------------------
     * Optimal (Interview Preferred)
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Since the problem guarantees lowercase English letters, hashing is
     * unnecessary.
     *
     * Replace the HashMap with a fixed-size frequency table.
     *
     * Every array index directly represents one character.
     *
     * Invariant
     * ---------
     * count[c] always equals the number of unused copies of character c
     * remaining inside magazine.
     *
     * Correctness
     * -----------
     * Every successful request consumes exactly one remaining copy.
     *
     * If inventory ever becomes unavailable, construction is impossible.
     *
     * Complexity
     * ----------
     * Time  : O(n + m)
     * Space : O(1)
     *
     * because the alphabet size is constant.
     *
     * Interview Usefulness
     * --------------------
     * This is the preferred interview solution.
     *
     * It demonstrates:
     *
     * - recognizing fixed alphabets
     * - reducing constant factors
     * - preserving the counting invariant
     */
    static class Optimal {

        /**
         * Version A
         *
         * Validate first.
         * Consume second.
         *
         * Easier for many candidates to reason about.
         */
        static boolean canConstruct(String ransomNote, String magazine) {

            int[] count = new int[128];

            // 🟢 Build the inventory before consuming anything.
            for (char c : magazine.toCharArray()) {
                count[c]++;
            }

            for (char c : ransomNote.toCharArray()) {

                // 🟢 Invariant:
                // count[c] is remaining inventory before consumption.
                if (count[c] <= 0) {
                    return false;
                }

                // 🟢 Consume exactly one available character.
                count[c]--;
            }

            // 🟢 Every request succeeded.
            return true;
        }

        /**
         * Version B
         *
         * Decrement first.
         * Validate after decrement.
         *
         * Mechanically shorter but depends on a slightly different
         * invariant.
         */
        static boolean canConstructDecrementFirst(String ransomNote,
                                                  String magazine) {

            int[] count = new int[128];

            // 🟢 Initial inventory.
            for (char c : magazine.toCharArray()) {
                count[c]++;
            }

            for (char c : ransomNote.toCharArray()) {

                // 🟢 After decrement,
                // negative means we consumed more than existed.
                if (--count[c] < 0) {
                    return false;
                }
            }

            return true;
        }

        /**
         * Lowercase-only implementation.
         *
         * Uses the exact constraint that characters belong to
         * ['a' ... 'z'].
         */
        static boolean canConstructLowercaseOnly(String ransomNote,
                                                 String magazine) {

            int[] frequency = new int[26];

            for (char c : magazine.toCharArray()) {
                frequency[c - 'a']++;
            }

            for (char c : ransomNote.toCharArray()) {

                int index = c - 'a';

                // 🟢 Invariant:
                // frequency[index] represents remaining copies.
                if (frequency[index] == 0) {
                    return false;
                }

                frequency[index]--;
            }

            return true;
        }
    }

/*
 * ================================================================
 * 🟣 INTERVIEW ARTICULATION
 * ================================================================
 *
 * If asked,
 *
 * "Explain your solution."
 *
 * a strong answer is:
 *
 * ------------------------------------------------------------
 *
 * "The problem is purely an inventory problem.
 *
 * Order never matters.
 *
 * Therefore I convert magazine into a frequency table.
 *
 * The invariant is that every entry stores the remaining unused
 * inventory of that character.
 *
 * While scanning ransomNote I consume one unit of inventory for
 * every requested character.
 *
 * If inventory is unavailable, construction immediately becomes
 * impossible.
 *
 * Otherwise every request succeeds and the answer is true."
 *
 * ------------------------------------------------------------
 *
 * Invariant
 * ---------
 *
 * Frequency table equals remaining inventory.
 *
 * ------------------------------------------------------------
 *
 * Search Space
 * ------------
 *
 * Character inventory.
 *
 * Not positions.
 *
 * ------------------------------------------------------------
 *
 * Discard Rule
 * ------------
 *
 * A character whose remaining count reaches zero can no longer be
 * supplied.
 *
 * Any future request for that character immediately fails.
 *
 * ------------------------------------------------------------
 *
 * Correctness
 * -----------
 *
 * Every successful transition preserves the inventory invariant.
 *
 * Every failed transition proves insufficient inventory.
 *
 * ------------------------------------------------------------
 *
 * Termination
 * -----------
 *
 * One forward pass over magazine.
 *
 * One forward pass over ransomNote.
 *
 * ------------------------------------------------------------
 *
 * In-place Feasibility
 * --------------------
 *
 * No.
 *
 * The algorithm requires external storage for frequencies.
 *
 * ------------------------------------------------------------
 *
 * Streaming Feasibility
 * ---------------------
 *
 * Magazine:
 * Yes.
 *
 * We can build counts while streaming.
 *
 * ransomNote:
 * Also yes.
 *
 * Each character is processed once.
 *
 * ------------------------------------------------------------
 *
 * When NOT To Use
 * ----------------
 *
 * If ordering matters.
 *
 * If adjacency matters.
 *
 * If substring matching is required.
 *
 * Frequency counting alone becomes insufficient.
 */

/*
 * ================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ================================================================
 *
 * Trigger
 * -------
 * Resource availability.
 *
 * Duplicate counts matter.
 *
 * ------------------------------------------------------------
 *
 * Pattern
 * -------
 * Frequency Counting.
 *
 * ------------------------------------------------------------
 *
 * Invariant
 * ---------
 * Frequency table stores remaining inventory.
 *
 * ------------------------------------------------------------
 *
 * Search Target
 * -------------
 * Remaining character counts.
 *
 * ------------------------------------------------------------
 *
 * Discard Rule
 * ------------
 * Remaining count reaches zero.
 *
 * Future request immediately fails.
 *
 * ------------------------------------------------------------
 *
 * Common Trap
 * -----------
 * HashSet ignores duplicates.
 *
 * ------------------------------------------------------------
 *
 * Edge Cases
 * ----------
 * Empty request.
 * Missing character.
 * Duplicate character.
 * Exact inventory match.
 *
 * ------------------------------------------------------------
 *
 * One-Liner
 * ---------
 * Build inventory.
 * Consume inventory.
 * Never allow inventory below zero.
 *
 * ------------------------------------------------------------
 *
 * Re-derivation Cue
 * -----------------
 *
 * Warehouse.
 *
 * Customer requests.
 *
 * Inventory decreases.
 *
 * Negative inventory is impossible.
 */

/*
 * ================================================================
 * 🔄 VARIATIONS & TWEAKS
 * ================================================================
 *
 * Variation 1
 * -----------
 * Unicode Characters
 *
 * Replace array with HashMap.
 *
 * Invariant remains identical.
 *
 * Only storage changes.
 *
 * ------------------------------------------------------------
 *
 * Variation 2
 * -----------
 * Large Unknown Alphabet
 *
 * Again use HashMap.
 *
 * Counting invariant remains unchanged.
 *
 * ------------------------------------------------------------
 *
 * Variation 3
 * -----------
 * Case-sensitive letters.
 *
 * Increase alphabet size.
 *
 * Invariant unchanged.
 *
 * ------------------------------------------------------------
 *
 * Variation 4
 * -----------
 * Multiple ransom notes.
 *
 * Reuse one frequency table only after cloning or rebuilding.
 *
 * Otherwise previous queries permanently consume inventory.
 *
 * ------------------------------------------------------------
 *
 * Variation 5
 * -----------
 * Check whether two strings are anagrams.
 *
 * Pattern is almost identical.
 *
 * Difference:
 *
 * Final inventory must return to exactly zero instead of merely
 * remaining non-negative.
 *
 * ------------------------------------------------------------
 *
 * Variation 6
 * -----------
 * Find missing characters.
 *
 * Instead of returning false immediately,
 * continue scanning and record shortages.
 *
 * Inventory invariant still holds.
 *
 * Only the reporting strategy changes.
 */

/*
 * ================================================================
 * 🧠 MASTERY CHECKLIST
 * ================================================================
 *
 * □ Can I state the invariant without looking?
 *
 *     Yes.
 *
 *     count[c] always stores the remaining unused inventory of
 *     character c.
 *
 * ------------------------------------------------------------
 *
 * □ What is the search target?
 *
 *     Remaining character inventory.
 *
 * ------------------------------------------------------------
 *
 * □ What is the discard rule?
 *
 *     If remaining inventory becomes unavailable,
 *     construction is impossible immediately.
 *
 * ------------------------------------------------------------
 *
 * □ Why does the algorithm terminate?
 *
 *     Every character from both strings is processed exactly once.
 *
 * ------------------------------------------------------------
 *
 * □ Why does the naive solution fail?
 *
 *     It repeatedly rescans the magazine,
 *     producing O(n × m) time.
 *
 * ------------------------------------------------------------
 *
 * □ What are the important edge cases?
 *
 *     - identical strings
 *     - ransom longer than magazine
 *     - duplicate requests
 *     - missing character
 *     - empty strings
 *     - exact inventory exhaustion
 *
 * ------------------------------------------------------------
 *
 * □ Debugging readiness?
 *
 *     Print frequency table before and after each transition.
 *
 *     First negative value immediately identifies failure.
 *
 * ------------------------------------------------------------
 *
 * □ Variant readiness?
 *
 *     Array
 *     HashMap
 *     Unicode
 *     Streaming
 *     Anagram
 *
 * ------------------------------------------------------------
 *
 * □ Pattern boundary?
 *
 *     Stops working once ordering becomes part of the problem.
 */

/*
 * ================================================================
 * ⚫ PATTERN MAPPING
 * ================================================================
 *
 * Frequency Counting Family
 * -------------------------
 *
 * Easy
 *
 * ✓ Ransom Note
 * ✓ Valid Anagram
 * ✓ Find the Difference
 * ✓ First Unique Character
 *
 * ------------------------------------------------------------
 *
 * Sliding Window + Frequency
 * --------------------------
 *
 * ✓ Permutation in String
 * ✓ Find All Anagrams
 * ✓ Minimum Window Substring
 *
 * ------------------------------------------------------------
 *
 * Inventory Problems
 * ------------------
 *
 * ✓ Can Construct
 * ✓ Magazine Supply
 * ✓ Resource Allocation
 *
 * Same invariant:
 *
 * Remaining inventory.
 */

/*
 * ================================================================
 * 🔍 DEBUGGING GUIDE
 * ================================================================
 *
 * Symptom
 * -------
 * False returned unexpectedly.
 *
 * Check
 * -----
 * Is the initial counting correct?
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Works for unique letters but fails for duplicates.
 *
 * Check
 * -----
 * Did you forget to decrement?
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Off-by-one failure.
 *
 * Check
 * -----
 * Are you mixing
 *
 *     if (count <= 0)
 *
 * with
 *
 *     if (--count < 0)
 *
 * ?
 *
 * Choose exactly one invariant.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * ArrayIndexOutOfBoundsException.
 *
 * Check
 * -----
 * Alphabet assumption.
 *
 * If input is not lowercase,
 * size 26 is insufficient.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * HashMap solution slower.
 *
 * Explanation
 * -----------
 * Hashing,
 * boxing,
 * cache misses,
 * object allocation.
 *
 * Array avoids these costs.
 */

/*
 * ================================================================
 * ⚫ COMPLEXITY SUMMARY
 * ================================================================
 *
 * Brute Force
 * -----------
 * Time  : O(n × m)
 * Space : O(m)
 *
 * ------------------------------------------------------------
 *
 * HashMap
 * -------
 * Time  : O(n + m)
 * Space : O(k)
 *
 * k = distinct characters.
 *
 * ------------------------------------------------------------
 *
 * Array
 * -----
 * Time  : O(n + m)
 * Space : O(1)
 *
 * Constant alphabet.
 */

/*
 * ================================================================
 * ⚫ IMPLEMENTATION RECONSTRUCTION DRILL
 * ================================================================
 *
 * Memorize these mechanical steps:
 *
 * Step 1
 *
 * Allocate frequency table.
 *
 * Step 2
 *
 * Count every magazine character.
 *
 * Step 3
 *
 * Iterate ransomNote.
 *
 * Step 4
 *
 * Validate remaining inventory.
 *
 * Step 5
 *
 * Consume inventory.
 *
 * Step 6
 *
 * Finish traversal.
 *
 * Step 7
 *
 * Return true.
 *
 * If you can remember these seven steps,
 * you can reconstruct the implementation from scratch.
 */

/*
 * ================================================================
 * ⚫ QUICK COMPARISON OF THE TWO ARRAY STYLES
 * ================================================================
 *
 * Style A
 *
 * if (count[c] <= 0)
 *     return false;
 *
 * count[c]--;
 *
 * --------------------
 *
 * Invariant
 *
 * count[c]
 * =
 * remaining inventory BEFORE consumption.
 *
 * --------------------
 *
 * Easier for beginners.
 *
 * ================================================================
 *
 * Style B
 *
 * if (--count[c] < 0)
 *     return false;
 *
 * --------------------
 *
 * Invariant
 *
 * count[c]
 * =
 * remaining inventory AFTER consumption.
 *
 * --------------------
 *
 * Slightly shorter.
 *
 * Frequently preferred in competitive programming.
 *
 * ================================================================
 *
 * Never combine the two invariants.
 */

/*
 * ================================================================
 * ⚫ INTERVIEW FOLLOW-UP QUESTIONS
 * ================================================================
 *
 * Q.
 * Why is the array solution faster than HashMap?
 *
 * A.
 * Direct indexing.
 * Better cache locality.
 * No hashing.
 * No object allocation.
 * No auto-boxing.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Why is the algorithm linear?
 *
 * A.
 * Every input character is processed exactly once.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Can sorting solve it?
 *
 * A.
 * Yes.
 *
 * But O(n log n) instead of O(n).
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Why is the space O(1)?
 *
 * A.
 * Alphabet size is fixed.
 *
 * Twenty-six lowercase letters (or 128 ASCII entries) do not grow
 * with the input size.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Can this be done without extra memory?
 *
 * A.
 * Not in general.
 *
 * Some representation of remaining inventory must exist.
 */


    /*
     * ================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ================================================================
     */

    public static void main(String[] args) {

        // Enable assertions:
        // IntelliJ -> Run Configuration -> VM Options -> -ea

        /*
         * Happy Path
         *
         * Magazine contains enough copies.
         */
        assert Optimal.canConstruct("aa", "aab");

        /*
         * Missing character.
         */
        assert !Optimal.canConstruct("a", "b");

        /*
         * Duplicate unavailable.
         */
        assert !Optimal.canConstruct("aa", "ab");

        /*
         * Exact inventory exhaustion.
         */
        assert Optimal.canConstruct("abc", "abc");

        /*
         * Magazine larger than necessary.
         */
        assert Optimal.canConstruct("abc", "zzabcyyy");

        /*
         * Single character.
         */
        assert Optimal.canConstruct("a", "a");

        /*
         * Character absent.
         */
        assert !Optimal.canConstruct("z", "aaaaaaaa");

        /*
         * Repeated character available.
         */
        assert Optimal.canConstruct("bbbb", "abbbbbc");

        /*
         * Ransom longer than magazine.
         */
        assert !Optimal.canConstruct("abcdef", "abcde");

        /*
         * Lowercase-only implementation.
         */
        assert Optimal.canConstructLowercaseOnly("leetcode", "leetcode");

        /*
         * Lowercase-only duplicate failure.
         */
        assert !Optimal.canConstructLowercaseOnly("aaa", "aa");

        /*
         * HashMap implementation.
         */
        assert ImprovedHashMap.canConstruct("hello", "hello");

        /*
         * HashMap duplicate failure.
         */
        assert !ImprovedHashMap.canConstruct("hello", "helo");

        /*
         * Brute force correctness.
         */
        assert BruteForce.canConstruct("abc", "abc");

        /*
         * Brute force duplicate failure.
         */
        assert !BruteForce.canConstruct("aaaa", "aaa");

        /*
         * Verify both optimal implementations produce identical answers.
         */
        String[] ransomTests = {
                "",
                "a",
                "aa",
                "abc",
                "leetcode",
                "zz",
                "bbbb",
                "xyz"
        };

        String[] magazineTests = {
                "",
                "a",
                "ab",
                "aab",
                "leetcode",
                "zzzz",
                "abbbbbc",
                "xyzz"
        };

        for (String ransom : ransomTests) {
            for (String magazine : magazineTests) {

                boolean answer1 =
                        Optimal.canConstruct(ransom, magazine);

                boolean answer2 =
                        Optimal.canConstructDecrementFirst(ransom, magazine);

                assert answer1 == answer2
                        : "Invariant mismatch for ransom=\""
                        + ransom
                        + "\" magazine=\""
                        + magazine
                        + "\"";
            }
        }

        /*
         * Verify lowercase implementation on representative examples.
         */
        assert Optimal.canConstructLowercaseOnly("aa", "aab");
        assert !Optimal.canConstructLowercaseOnly("aa", "ab");
        assert !Optimal.canConstructLowercaseOnly("a", "b");

        /*
         * Stress-style sanity check.
         */
        char[] manyA = new char[1000];
        Arrays.fill(manyA, 'a');

        char[] manyB = new char[1000];
        Arrays.fill(manyB, 'a');

        String s1 = new String(manyA);
        String s2 = new String(manyB);

        assert Optimal.canConstruct(s1, s2);

        /*
         * One character missing at the end.
         */
        String s3 = s2.substring(0, 999);

        assert !Optimal.canConstruct(s1, s3);

        System.out.println("All assertions passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/
