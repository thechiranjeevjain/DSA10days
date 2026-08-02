package org.chijai.day10.session2;

import java.util.Objects;

public class AddBinary {

    /*
     * ============================================================
     * 2. 📘 PRIMARY PROBLEM
     * ============================================================
     *
     * Title:
     * Add Binary
     *
     * Difficulty:
     * Easy
     *
     * Tags:
     * String
     * Simulation
     * Two Pointers
     * Math
     *
     * Problem Description
     * -------------------
     * Given two binary strings a and b, return their sum as a binary string.
     *
     * Binary strings contain only characters:
     * '0'
     * '1'
     *
     * No leading zeros exist except the number "0" itself.
     *
     * Constraints
     * -----------
     * 1 <= a.length, b.length <= 10^4
     *
     * a and b contain only:
     * '0'
     * '1'
     *
     * Examples
     * --------
     * Example 1
     * Input:
     * a = "11"
     * b = "1"
     *
     * Output:
     * "100"
     *
     * Explanation
     *   3 + 1 = 4
     *   11 + 1 = 100
     *
     * Example 2
     * Input:
     * a = "1010"
     * b = "1011"
     *
     * Output:
     * "10101"
     *
     * Explanation
     *   10 + 11 = 21
     *   1010 + 1011 = 10101
     *
     * Official LeetCode
     * -----------------
     * https://leetcode.com/problems/add-binary/
     */

    /*
     * ============================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern
     * -------
     * Reverse Simulation using Two Pointers + Carry Propagation
     *
     * Archetype
     * ---------
     * Grade-school addition performed from Least Significant Digit.
     *
     * Core Invariant
     * --------------
     * Every processed suffix has already been converted into the correct
     * binary representation while carry stores the only information that
     * must influence the next higher bit.
     *
     * Why it Works
     * ------------
     * Binary addition never needs information beyond:
     *
     * current bit of a
     * current bit of b
     * incoming carry
     *
     * Therefore every iteration can be completed independently before
     * moving left.
     *
     * Recognition Signals
     * -------------------
     * ✔ Binary strings
     * ✔ Manual addition
     * ✔ Carry propagation
     * ✔ Right-to-left processing
     * ✔ Result length differs by at most one digit
     *
     * When to Use
     * -----------
     * • Binary addition
     * • Decimal addition as strings
     * • Large integer addition
     * • Linked-list number addition
     * • Any digit-wise arithmetic
     *
     * When NOT to Use
     * ---------------
     * • Random access across entire string required
     * • Prefix-dependent computations
     * • Problems requiring carry look-ahead
     *
     * Comparison
     * ----------
     *
     * Reverse Two Pointer Simulation
     *      Processes every digit exactly once.
     *
     * BigInteger
     *      Hides the algorithm.
     *      Usually disallowed in interviews.
     *
     * Integer Conversion
     *      Fails because input length can reach 10^4.
     *
     * Bit Manipulation on Primitive Types
     *      Impossible for extremely long inputs.
     */

    /*
     * ============================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * Mental Model
     * ------------
     * Imagine performing handwritten binary addition.
     *
     *            carry
     *              ↓
     *         101101
     *       + 110011
     *       --------
     *
     * Always begin from the rightmost column.
     *
     * Finish one column completely.
     *
     * Move exactly one position left.
     *
     * Repeat.
     *
     * Eventually every column has been solved.
     *
     * ------------------------------------------------------------
     * Invariant 1
     * ------------------------------------------------------------
     *
     * Every digit already appended represents the correct answer for
     * every processed column.
     *
     * Nothing later can modify those digits.
     *
     * ------------------------------------------------------------
     * Invariant 2
     * ------------------------------------------------------------
     *
     * carry represents exactly the overflow entering the current column.
     *
     * It is always either:
     *
     * 0
     * 1
     *
     * Never larger.
     *
     * ------------------------------------------------------------
     * Invariant 3
     * ------------------------------------------------------------
     *
     * i always points to the next unprocessed digit of a.
     *
     * j always points to the next unprocessed digit of b.
     *
     * Every index greater than i or j has already been processed.
     *
     * ------------------------------------------------------------
     * Invariant 4
     * ------------------------------------------------------------
     *
     * sum =
     * carry
     * + current digit from a (if exists)
     * + current digit from b (if exists)
     *
     * No hidden state exists.
     *
     * ------------------------------------------------------------
     * Variable Meanings
     * ------------------------------------------------------------
     *
     * i
     * Current index inside a.
     *
     * j
     * Current index inside b.
     *
     * carry
     * Overflow from previous column.
     *
     * sum
     * Temporary value for current column.
     *
     * StringBuilder
     * Stores answer in reverse because least significant bits are
     * generated first.
     *
     * ------------------------------------------------------------
     * Allowed Moves
     * ------------------------------------------------------------
     *
     * Read current bits.
     *
     * Compute sum.
     *
     * Produce output bit.
     *
     * Update carry.
     *
     * Move both pointers left.
     *
     * ------------------------------------------------------------
     * Forbidden Moves
     * ------------------------------------------------------------
     *
     * Reading digits twice.
     *
     * Skipping carry propagation.
     *
     * Building answer from the front using String concatenation.
     *
     * Forgetting leftover carry.
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * Loop ends only after:
     *
     * every digit of a processed
     *
     * every digit of b processed
     *
     * then possibly append one final carry.
     *
     * ------------------------------------------------------------
     * Correctness Intuition
     * ------------------------------------------------------------
     *
     * Binary addition is locally complete.
     *
     * Once one column has been solved, only carry influences the next.
     *
     * Since carry is preserved exactly, every future column is computed
     * under identical conditions to handwritten addition.
     *
     * ------------------------------------------------------------
     * Why Naive Solutions Fail
     * ------------------------------------------------------------
     *
     * Parsing into integers overflows.
     *
     * Front-to-back traversal violates dependency because higher bits
     * depend on carries generated later.
     */

    /*
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * Mistake 1
     * ---------
     * Traverse left to right.
     *
     * Why it looks correct
     * --------------------
     * Natural reading direction.
     *
     * Violated Invariant
     * ------------------
     * Carry comes from the right.
     *
     * Counterexample
     * --------------
     * 1111
     * 0001
     *
     * Carry cannot be predicted early.
     *
     * ------------------------------------------------------------
     *
     * Mistake 2
     * ---------
     * Forget final carry.
     *
     * Counterexample
     * --------------
     * 1
     * 1
     *
     * Correct:
     * 10
     *
     * Wrong:
     * 0
     *
     * ------------------------------------------------------------
     *
     * Mistake 3
     * ---------
     * Use String concatenation.
     *
     * Why it appears harmless
     * -----------------------
     * Strings are easy to append.
     *
     * Reality
     * -------
     * Every concatenation creates a new immutable String.
     *
     * Complexity can degrade toward O(n²).
     *
     * ------------------------------------------------------------
     *
     * Mistake 4
     * ---------
     * Forget reverse().
     *
     * Counterexample
     * --------------
     * 1010
     * 1011
     *
     * Produced
     * 10101 reversed incorrectly.
     *
     * ------------------------------------------------------------
     *
     * Mistake 5
     * ---------
     * Assume both strings have equal length.
     *
     * Counterexample
     * --------------
     * 1
     * 111111
     *
     * One pointer finishes much earlier.
     *
     * ------------------------------------------------------------
     *
     * Interview Trap
     * --------------
     * Candidates often overcomplicate this into bit manipulation.
     *
     * The interview is usually evaluating whether you can simulate
     * elementary arithmetic while maintaining a clean invariant.
     */

    /*
     * ============================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Mechanical Typing Order
     * -----------------------
     *
     * Step 1
     * Create function.
     *
     * Step 2
     * Create StringBuilder.
     *
     * Step 3
     * Initialize
     *
     * i = last index of a
     * j = last index of b
     * carry = 0
     *
     * Step 4
     * while either pointer remains
     *
     * Step 5
     * sum = carry
     *
     * Step 6
     * Add digit from a if available.
     *
     * Step 7
     * Add digit from b if available.
     *
     * Step 8
     * Append
     *
     * sum % 2
     *
     * Step 9
     * Update
     *
     * carry = sum / 2
     *
     * Step 10
     * Continue until both strings exhausted.
     *
     * Step 11
     * Append remaining carry.
     *
     * Step 12
     * Reverse builder.
     *
     * Step 13
     * Return string.
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * initialize pointers
     * initialize carry
     *
     * while digits remain
     *      compute sum
     *      append sum mod 2
     *      carry = sum / 2
     *
     * append final carry
     *
     * reverse
     *
     * return
     */

    /**
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    static final class BruteForce {

        /*
         * Idea
         * ----
         * Simulate binary addition manually while repeatedly inserting
         * the newest digit at the front of the answer.
         *
         * Invariant
         * ---------
         * Processed suffix is always correct.
         *
         * Limitation
         * ----------
         * Front insertion into StringBuilder shifts characters.
         *
         * Complexity
         * ----------
         * Time : O(n²)
         * Space: O(n)
         *
         * Interview Usefulness
         * --------------------
         * Good stepping stone before optimization.
         */

        static String addBinary(String a, String b) {

            Objects.requireNonNull(a);
            Objects.requireNonNull(b);

            StringBuilder answer = new StringBuilder();

            int i = a.length() - 1;
            int j = b.length() - 1;
            int carry = 0;

            while (i >= 0 || j >= 0) {

                int sum = carry;

                if (i >= 0) {
                    sum += a.charAt(i--) - '0';
                }

                if (j >= 0) {
                    sum += b.charAt(j--) - '0';
                }

                answer.insert(0, sum % 2);

                carry = sum / 2;
            }

            if (carry == 1) {
                answer.insert(0, '1');
            }

            return answer.toString();
        }
    }

    static final class Improved {

/*
 * Idea
 * ----
 * Produce digits in reverse order.
 *
 * Reverse only once.
 *
 * Improvement
 * -----------
 * Eliminates repeated front insertions.
 *
 * Invariant
 * ---------
 * Builder contains the exact processed suffix in reverse order.
 *
 * Complexity
 * ----------
 * Time : O(n)
 * Space: O(n)
 *
 * Interview Usefulness
 * --------------------
 * Already interview acceptable.
 */

        static String addBinary(String a, String b) {

            Objects.requireNonNull(a);
            Objects.requireNonNull(b);

            StringBuilder reversed = new StringBuilder();

            int i = a.length() - 1;
            int j = b.length() - 1;
            int carry = 0;

            while (i >= 0 || j >= 0) {

                int sum = carry;

                if (i >= 0) {
                    sum += a.charAt(i--) - '0';
                }

                if (j >= 0) {
                    sum += b.charAt(j--) - '0';
                }

                reversed.append(sum % 2);

                carry = sum / 2;
            }

            if (carry == 1) {
                reversed.append('1');
            }

            return reversed.reverse().toString();
        }
    }

    static final class Optimal {

        /*
         * Idea
         * ----
         * Perform the exact handwritten binary addition algorithm.
         *
         * Read both strings from right to left.
         *
         * Emit one output bit immediately.
         *
         * Preserve only carry for the next iteration.
         *
         * 🟢 Invariant
         * ------------
         * At the start of every iteration:
         *
         * 1. Every processed column has already been converted into the
         *    correct answer.
         *
         * 2. carry is exactly the overflow entering the current column.
         *
         * 3. i and j identify the next unprocessed digits.
         *
         * Correctness
         * -----------
         * Every binary column depends only upon:
         *
         * current bit from a
         * current bit from b
         * incoming carry
         *
         * Therefore solving one column completely cannot invalidate any
         * previous output.
         *
         * Complexity
         * ----------
         * Time  : O(max(m, n))
         *
         * Space : O(max(m, n))
         *
         * Interview Usefulness
         * --------------------
         * This is the expected interview solution.
         */
        static String addBinary(String a, String b) {

            Objects.requireNonNull(a);
            Objects.requireNonNull(b);

            StringBuilder answer = new StringBuilder();

            int i = a.length() - 1;
            int j = b.length() - 1;
            int carry = 0;

            while (i >= 0 || j >= 0) {

                // 🟢 Invariant:
                // sum contains every contribution for this column only.
                int sum = carry;

                if (i >= 0) {

                    // Current bit participates exactly once.
                    sum += a.charAt(i--) - '0';
                }

                if (j >= 0) {

                    // Missing digits contribute zero naturally.
                    sum += b.charAt(j--) - '0';
                }

                // Current binary digit is finalized forever.
                answer.append(sum % 2);

                // Preserve only the overflow.
                carry = sum / 2;
            }

            if (carry != 0) {

                // Remaining overflow becomes the new MSB.
                answer.append(carry);
            }

            // Digits were produced from LSB to MSB.
            return answer.reverse().toString();
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Pattern
 * -------
 * Reverse simulation with carry propagation.
 *
 * Invariant
 * ---------
 * Every processed suffix is already correct while carry completely
 * summarizes all remaining dependency.
 *
 * Search Space
 * ------------
 * The unprocessed prefixes of both strings.
 *
 * State
 * -----
 * (i, j, carry)
 *
 * Transition
 * ----------
 * Read current bits.
 *
 * Compute:
 *
 * sum = carry + bitA + bitB
 *
 * Emit:
 *
 * sum % 2
 *
 * Update:
 *
 * carry = sum / 2
 *
 * Discard Rule
 * ------------
 * Once one column has produced its output bit, that column is
 * permanently finished.
 *
 * Correctness
 * -----------
 * Binary arithmetic is local.
 *
 * No future column can alter an already completed lower-order bit.
 *
 * Termination
 * -----------
 * Both pointers leave the strings.
 *
 * Any remaining carry becomes one final digit.
 *
 * In-place Feasibility
 * --------------------
 * No.
 *
 * Java Strings are immutable.
 *
 * Streaming Feasibility
 * ---------------------
 * Only if digits are supplied from least significant toward most
 * significant.
 *
 * When NOT to Use
 * ---------------
 * Do not use this pattern when arithmetic depends on future digits
 * instead of carry alone.
 */

/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 * Add two very large binary strings.
 *
 * Pattern
 * -------
 * Reverse simulation.
 *
 * Invariant
 * ---------
 * carry stores the entire dependency.
 *
 * Search Target
 * -------------
 * Consume one binary column at a time.
 *
 * Discard Rule
 * ------------
 * Completed columns never change again.
 *
 * Common Trap
 * -----------
 * Forget reverse().
 *
 * Edge Cases
 * ----------
 * Different lengths.
 *
 * All carries.
 *
 * One input equals "0".
 *
 * Final carry.
 *
 * One-liner
 * ---------
 * Simulate handwritten binary addition from right to left.
 *
 * Re-derivation Cue
 * -----------------
 * Think of adding decimal numbers column by column, replacing base
 * ten with base two.
 */

/*
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variation
 * ---------
 * Add Decimal Strings
 *
 * Reasoning Change
 * ----------------
 * Replace:
 *
 * mod 2
 * divide by 2
 *
 * with
 *
 * mod 10
 * divide by 10
 *
 * Invariant remains identical.
 *
 * ------------------------------------------------------------
 *
 * Variation
 * ---------
 * Add Linked List Numbers
 *
 * Reasoning Change
 * ----------------
 * Replace indices by node pointers.
 *
 * Carry invariant remains unchanged.
 *
 * ------------------------------------------------------------
 *
 * Variation
 * ---------
 * Add Numbers in Arbitrary Base
 *
 * Replace:
 *
 * 2
 *
 * by
 *
 * base
 *
 * Carry computation becomes:
 *
 * digit = sum % base
 * carry = sum / base
 *
 * Pattern still holds because overflow is still summarized by carry.
 *
 * ------------------------------------------------------------
 *
 * Pattern Boundary
 * ----------------
 * This technique assumes each column depends only on a bounded
 * amount of previous information (carry).
 *
 * If future columns influence earlier decisions, this invariant
 * breaks completely.
 */

/*
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * □ I know the Pattern.
 *
 * □ I know the Invariant.
 *
 * □ I know why carry is sufficient state.
 *
 * □ I know why traversal starts from the right.
 *
 * □ I know the Discard Rule.
 *
 * □ I know Termination.
 *
 * □ I can explain why integer parsing fails.
 *
 * □ I remember reverse() is mandatory.
 *
 * □ I can derive decimal-string addition from the same Pattern.
 *
 * □ I can debug missing final carry immediately.
 */


    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    public static void main(String[] args) {

        /*
         * Run with:
         *
         * java -ea AddBinary
         *
         * Assertions must be enabled.
         */

        // Representative Example 1
        assert Optimal.addBinary("11", "1").equals("100")
                : "Simple carry propagation failed.";

        // Representative Example 2
        assert Optimal.addBinary("1010", "1011").equals("10101")
                : "Different length addition failed.";

        // Single zero with zero
        assert Optimal.addBinary("0", "0").equals("0")
                : "Zero plus zero should remain zero.";

        // Zero with one
        assert Optimal.addBinary("0", "1").equals("1")
                : "Identity element failed.";

        // One with zero
        assert Optimal.addBinary("1", "0").equals("1")
                : "Identity element is symmetric.";

        // Smallest carry
        assert Optimal.addBinary("1", "1").equals("10")
                : "Final carry not appended.";

        // Multiple cascading carries
        assert Optimal.addBinary("1111", "1").equals("10000")
                : "Carry chain across every column failed.";

        // Unequal lengths
        assert Optimal.addBinary("111111", "1").equals("1000000")
                : "Unequal length traversal failed.";

        // Longer unequal lengths
        assert Optimal.addBinary("100000", "1111").equals("101111")
                : "Pointer exhaustion handling failed.";

        // No carries anywhere
        assert Optimal.addBinary("101010", "010101").equals("111111")
                : "Independent columns computed incorrectly.";

        // Alternating carries
        assert Optimal.addBinary("110011", "101101").equals("1100000")
                : "Alternating carry pattern failed.";

        // Large all-ones stress pattern
        String a = "1111111111111111";
        String b = "1";
        assert Optimal.addBinary(a, b).equals("10000000000000000")
                : "Long carry propagation failed.";

        // Compare all implementations on representative inputs.
        verifyAllImplementations("11", "1");
        verifyAllImplementations("1010", "1011");
        verifyAllImplementations("0", "0");
        verifyAllImplementations("1", "1");
        verifyAllImplementations("111111", "111111");
        verifyAllImplementations("100010101010", "101011");

        // Boundary: maximum logical behavior (small sample here)
        String left = "100000000000000000000";
        String right = "100000000000000000000";
        assert Optimal.addBinary(left, right)
                .equals("1000000000000000000000")
                : "Highest bit growth failed.";

        System.out.println("All assertions passed.");
    }

    private static void verifyAllImplementations(String a, String b) {

        String brute = BruteForce.addBinary(a, b);
        String improved = Improved.addBinary(a, b);
        String optimal = Optimal.addBinary(a, b);

        assert brute.equals(improved)
                : "Brute Force and Improved disagree.";

        assert improved.equals(optimal)
                : "Improved and Optimal disagree.";
    }

    /*
     * ============================================================
     * 🧘 FINAL CLOSURE
     * ============================================================
     *
     * Key Pattern
     * -----------
     * Reverse Simulation with Carry Propagation.
     *
     * State
     * -----
     * (i, j, carry)
     *
     * Transition
     * ----------
     * sum = carry + bitA + bitB
     *
     * output = sum % 2
     *
     * carry = sum / 2
     *
     * Search Space
     * ------------
     * Remaining unprocessed suffixes.
     *
     * Correctness
     * -----------
     * Every completed binary column is permanently correct because future
     * computation depends only upon the propagated carry.
     *
     * Complexity
     * ----------
     * Time  : O(max(m, n))
     * Space : O(max(m, n))
     *
     * Debug Checklist
     * ---------------
     * ✔ Start from the right.
     * ✔ Initialize carry to zero.
     * ✔ Append current bit.
     * ✔ Update carry after every column.
     * ✔ Append leftover carry.
     * ✔ Reverse exactly once.
     * ✔ Return String.
     *
     * Transfer Learning
     * -----------------
     * The same invariant directly solves:
     *
     * • Add Strings
     * • Add Two Numbers (Linked Lists)
     * • Arbitrary Base Addition
     * • Manual Decimal Addition
     */

}