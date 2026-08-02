package org.chijai.day3.session3;

import java.util.Objects;

public class StringToIntegerAtoi {

/*
 * ============================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Title:
 * String to Integer (atoi)
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * String
 * Parsing
 * Simulation
 * Overflow Detection
 *
 * Official LeetCode:
 * https://leetcode.com/problems/string-to-integer-atoi/
 *
 * ------------------------------------------------------------
 * Problem
 * ------------------------------------------------------------
 *
 * Implement myAtoi(String s).
 *
 * Convert the given string into a signed 32-bit integer using
 * rules similar to the C/C++ atoi function.
 *
 * Parsing Rules
 *
 * 1. Ignore leading spaces.
 *
 * 2. Read one optional sign.
 *      '+'
 *      '-'
 *
 * 3. Read consecutive digits.
 *
 * 4. Stop immediately after the first non-digit.
 *
 * 5. If no digits are read, return 0.
 *
 * 6. Clamp overflow.
 *
 *      less than Integer.MIN_VALUE
 *          -> Integer.MIN_VALUE
 *
 *      greater than Integer.MAX_VALUE
 *          -> Integer.MAX_VALUE
 *
 * Remaining characters are ignored.
 *
 * ------------------------------------------------------------
 * Constraints
 * ------------------------------------------------------------
 *
 * 0 <= s.length <= 200
 *
 * Characters may contain
 *
 * letters
 * digits
 * spaces
 * '+'
 * '-'
 * '.'
 *
 * ------------------------------------------------------------
 * Representative Examples
 * ------------------------------------------------------------
 *
 * Input:
 * "42"
 *
 * Output:
 * 42
 *
 * ------------------------------------------------------------
 *
 * Input:
 * "   -42"
 *
 * Output:
 * -42
 *
 * ------------------------------------------------------------
 *
 * Input:
 * "4193 with words"
 *
 * Output:
 * 4193
 *
 * ------------------------------------------------------------
 *
 * Input:
 * "words and 987"
 *
 * Output:
 * 0
 *
 * ------------------------------------------------------------
 *
 * Input:
 * "-91283472332"
 *
 * Output:
 * Integer.MIN_VALUE
 *
 * ------------------------------------------------------------
 *
 * Input:
 * "91283472332"
 *
 * Output:
 * Integer.MAX_VALUE
 *
 * ============================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern
 *
 * Deterministic State Machine / Sequential Parsing
 *
 * Archetype
 *
 * Consume one character at a time.
 *
 * Every character permanently advances the parser.
 *
 * No backtracking.
 *
 * ------------------------------------------------------------
 * Core Invariant
 * ------------------------------------------------------------
 *
 * After processing index i:
 *
 * total exactly equals the numeric value represented by every
 * valid digit consumed so far.
 *
 * Characters before index are completely resolved forever.
 *
 * Characters after index are untouched.
 *
 * ------------------------------------------------------------
 * Why It Works
 * ------------------------------------------------------------
 *
 * Every character belongs to exactly one of four phases:
 *
 * Phase 1
 * Skip spaces.
 *
 * Phase 2
 * Read sign.
 *
 * Phase 3
 * Read digits.
 *
 * Phase 4
 * Stop.
 *
 * Since transitions are one-way, correctness follows from
 * maintaining parser state rather than searching.
 *
 * ------------------------------------------------------------
 * Recognition Signals
 * ------------------------------------------------------------
 *
 * Use this pattern when:
 *
 * • input is scanned left-to-right
 * • decisions depend only on previous characters
 * • parser never revisits characters
 * • parsing rules are sequential
 * • overflow must be detected during parsing
 *
 * ------------------------------------------------------------
 * When NOT to Use
 * ------------------------------------------------------------
 *
 * Do not use this parser pattern when:
 *
 * • nested grammar exists
 * • recursive expressions exist
 * • parentheses require stacks
 * • tokens require look-ahead beyond immediate state
 *
 * ------------------------------------------------------------
 * Comparison
 * ------------------------------------------------------------
 *
 * Two Pointers
 *
 * Moves both ends.
 *
 * Here:
 * only one pointer advances.
 *
 * Binary Search
 *
 * Shrinks search space.
 *
 * Here:
 * there is no search space.
 *
 * Sliding Window
 *
 * Maintains window invariant.
 *
 * Here:
 * maintains parser state.
 *
 * DFS/BFS
 *
 * Explores graph states.
 *
 * Here:
 * explores only one deterministic execution path.
 *
 * ============================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Mental Model
 *
 * Imagine a reader moving over a tape.
 *
 * Once a character is passed, it is never examined again.
 *
 * Every movement either:
 *
 * discard
 * interpret
 * terminate
 *
 * Nothing else.
 *
 * ------------------------------------------------------------
 * State Variables
 * ------------------------------------------------------------
 *
 * index
 *
 * Current unread character.
 *
 * sign
 *
 * +1 or -1.
 *
 * total
 *
 * Absolute value accumulated so far.
 *
 * digit
 *
 * Current numeric contribution.
 *
 * ------------------------------------------------------------
 * Primary Invariant
 * ------------------------------------------------------------
 *
 * Before every loop iteration:
 *
 * total equals the absolute value formed by every digit already
 * consumed.
 *
 * It never includes:
 *
 * future digits
 * skipped spaces
 * sign
 * invalid characters
 *
 * ------------------------------------------------------------
 * Secondary Invariants
 * ------------------------------------------------------------
 *
 * Space skipping finishes exactly once.
 *
 * Sign is read at most once.
 *
 * Digits are contiguous.
 *
 * Parsing stops permanently at first invalid character.
 *
 * Overflow is checked immediately after extending the number.
 *
 * No character is processed twice.
 *
 * ------------------------------------------------------------
 * Allowed State Transitions
 * ------------------------------------------------------------
 *
 * START
 *      ↓
 *
 * SPACES
 *      ↓
 *
 * SIGN
 *      ↓
 *
 * DIGITS
 *      ↓
 *
 * END
 *
 * Every transition moves forward.
 *
 * No backward edges exist.
 *
 * ------------------------------------------------------------
 * Forbidden Moves
 * ------------------------------------------------------------
 *
 * Reading sign twice.
 *
 * Skipping spaces after digits begin.
 *
 * Reading letters after digits.
 *
 * Continuing after overflow.
 *
 * Restarting parsing.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Parsing terminates when:
 *
 * index reaches end
 *
 * OR
 *
 * current character is not a digit.
 *
 * Since index only increases, termination is guaranteed.
 *
 * ------------------------------------------------------------
 * Correctness Intuition
 * ------------------------------------------------------------
 *
 * Each digit performs:
 *
 * newValue = oldValue × 10 + digit
 *
 * Since decimal representation is positional, this is the only
 * correct transition.
 *
 * Maintaining this invariant guarantees total always equals the
 * number represented by consumed digits.
 *
 * ------------------------------------------------------------
 * Why Naive Solutions Fail
 * ------------------------------------------------------------
 *
 * Many implementations first collect digits into a string and
 * later convert.
 *
 * Problems:
 *
 * • unnecessary memory
 * • overflow before conversion
 * • delayed validation
 * • harder debugging
 *
 * Incremental parsing validates every transition immediately.
 *
 * ============================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================
 *
 * Mistake 1
 *
 * Calling Integer.parseInt().
 *
 * Why it seems correct:
 *
 * It parses integers.
 *
 * Why it fails:
 *
 * Throws exception instead of clamping.
 *
 * Violated Invariant:
 *
 * Overflow must be handled during parsing.
 *
 * ------------------------------------------------------------
 *
 * Mistake 2
 *
 * Accepting spaces after sign.
 *
 * Example:
 *
 * "+ 42"
 *
 * Expected:
 *
 * 0
 *
 * Violated Invariant:
 *
 * Digits must begin immediately after optional sign.
 *
 * ------------------------------------------------------------
 *
 * Mistake 3
 *
 * Continuing after letters.
 *
 * Example:
 *
 * "12abc34"
 *
 * Incorrect:
 *
 * 1234
 *
 * Correct:
 *
 * 12
 *
 * Violated Invariant:
 *
 * Parsing ends permanently after first non-digit.
 *
 * ------------------------------------------------------------
 *
 * Mistake 4
 *
 * Overflow detected only at the end.
 *
 * Counterexample:
 *
 * "999999999999999999999999"
 *
 * Intermediate multiplication already exceeds int.
 *
 * The parser must detect overflow while growing the number.
 *
 * ------------------------------------------------------------
 *
 * Mistake 5
 *
 * Forgetting empty input after trimming spaces.
 *
 * Example:
 *
 * "      "
 *
 * Correct:
 *
 * 0
 */

    /*
     * ============================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Mechanical typing order
     *
     * Step 1
     *
     * Create method.
     *
     *     myAtoi(String s)
     *
     * ------------------------------------------------------------
     *
     * Step 2
     *
     * Declare variables.
     *
     * index = 0
     * sign = 1
     * total = 0L
     *
     * total intentionally uses long.
     *
     * Overflow is easier to detect before returning int.
     *
     * ------------------------------------------------------------
     *
     * Step 3
     *
     * Handle empty string.
     *
     * ------------------------------------------------------------
     *
     * Step 4
     *
     * Skip leading spaces.
     *
     * while current character == ' '
     *      index++
     *
     * ------------------------------------------------------------
     *
     * Step 5
     *
     * If entire string consumed
     * return 0.
     *
     * ------------------------------------------------------------
     *
     * Step 6
     *
     * Read optional sign.
     *
     * '+' -> +1
     * '-' -> -1
     *
     * advance pointer exactly once.
     *
     * ------------------------------------------------------------
     *
     * Step 7
     *
     * Read digits.
     *
     * digit = current - '0'
     *
     * if digit not in [0,9]
     * stop parsing.
     *
     * ------------------------------------------------------------
     *
     * Step 8
     *
     * Extend number.
     *
     * total = total * 10 + digit
     *
     * ------------------------------------------------------------
     *
     * Step 9
     *
     * Immediately clamp overflow.
     *
     * sign * total
     *
     * compared against
     *
     * Integer.MIN_VALUE
     * Integer.MAX_VALUE
     *
     * ------------------------------------------------------------
     *
     * Step 10
     *
     * Continue until non-digit.
     *
     * ------------------------------------------------------------
     *
     * Step 11
     *
     * Return
     *
     * sign * total
     *
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * index ← 0
     * sign ← +1
     * total ← 0
     *
     * skip spaces
     *
     * read sign
     *
     * while digit
     *     total = total × 10 + digit
     *     clamp if overflow
     *
     * return sign × total
     *
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    /**
     * ============================================================
     * Brute Force
     * ============================================================
     *
     * Idea
     *
     * Parse valid characters into a StringBuilder.
     *
     * Afterwards convert using larger numeric type.
     *
     * Finally clamp.
     *
     * Invariant
     *
     * Builder always stores every accepted character.
     *
     * Limitation
     *
     * Requires additional memory.
     *
     * Overflow handling becomes delayed.
     *
     * Less representative of interview expectations.
     *
     * Complexity
     *
     * Time  : O(n)
     * Space : O(n)
     *
     * Interview usefulness
     *
     * Good first idea.
     *
     * Rarely preferred.
     */
    static final class BruteForce {

        public int myAtoi(String s) {

            if (s == null || s.isEmpty()) {
                return 0;
            }

            int index = 0;

            while (index < s.length() && s.charAt(index) == ' ') {
                index++;
            }

            if (index == s.length()) {
                return 0;
            }

            StringBuilder builder = new StringBuilder();

            if (s.charAt(index) == '+' || s.charAt(index) == '-') {
                builder.append(s.charAt(index));
                index++;
            }

            while (index < s.length()) {

                char ch = s.charAt(index);

                if (!Character.isDigit(ch)) {
                    break;
                }

                builder.append(ch);
                index++;
            }

            String token = builder.toString();

            if (token.isEmpty()
                    || Objects.equals(token, "+")
                    || Objects.equals(token, "-")) {
                return 0;
            }

            try {

                long value = Long.parseLong(token);

                if (value > Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                }

                if (value < Integer.MIN_VALUE) {
                    return Integer.MIN_VALUE;
                }

                return (int) value;

            } catch (NumberFormatException ex) {

                if (token.charAt(0) == '-') {
                    return Integer.MIN_VALUE;
                }

                return Integer.MAX_VALUE;
            }
        }
    }

    /**
     * ============================================================
     * Improved
     * ============================================================
     *
     * Idea
     *
     * Parse incrementally.
     *
     * Maintain long accumulator.
     *
     * Clamp immediately after every digit.
     *
     * Invariant
     *
     * total equals every digit consumed so far.
     *
     * Improvement
     *
     * No temporary digit string.
     *
     * Overflow detected during parsing.
     *
     * Complexity
     *
     * Time  : O(n)
     * Space : O(1)
     *
     * Interview usefulness
     *
     * Strong.
     *
     * Easy to derive under pressure.
     */
    static final class Improved {

        public int myAtoi(String s) {

            if (s == null || s.isEmpty()) {
                return 0;
            }

            int index = 0;
            int sign = 1;
            long total = 0;

            while (index < s.length() && s.charAt(index) == ' ') {
                index++;
            }

            if (index == s.length()) {
                return 0;
            }

            if (s.charAt(index) == '+' || s.charAt(index) == '-') {
                sign = s.charAt(index) == '+' ? 1 : -1;
                index++;
            }

            while (index < s.length()) {

                int digit = s.charAt(index) - '0';

                if (digit < 0 || digit > 9) {
                    break;
                }

                total = total * 10 + digit;

                long signedValue = sign * total;

                if (signedValue >= Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                }

                if (signedValue <= Integer.MIN_VALUE) {
                    return Integer.MIN_VALUE;
                }

                index++;
            }

            return (int) (sign * total);
        }
    }

    /**
     * ============================================================
     * Optimal (Interview Preferred)
     * ============================================================
     *
     * Idea
     *
     * Same sequential parser.
     *
     * Maintain parser state with one index,
     * one sign,
     * one accumulator.
     *
     * Invariant
     *
     * Refer to the Primary Invariant defined earlier.
     *
     * Correctness
     *
     * Every accepted digit performs the only legal decimal transition.
     *
     * Every rejected character permanently terminates parsing.
     *
     * Overflow is checked immediately after every transition.
     *
     * Complexity
     *
     * Time  : O(n)
     * Space : O(1)
     *
     * Interview usefulness
     *
     * Preferred implementation.
     *
     * Small,
     * deterministic,
     * easy to debug,
     * easy to reconstruct.
     */
    static final class Optimal {

        public int myAtoi(String str) {

            // 🔴 Empty input has no numeric interpretation.
            if (str == null || str.isEmpty()) {
                return 0;
            }

            int index = 0;

            // 🟢 Invariant:
            // total is always the absolute value of consumed digits.
            long total = 0;

            // 🟢 Invariant:
            // sign is chosen exactly once.
            int sign = 1;

            // 🟡 Consume only leading spaces.
            while (index < str.length() && str.charAt(index) == ' ') {
                index++;
            }

            // 🔴 Entire string contained only spaces.
            if (index == str.length()) {
                return 0;
            }

            // 🟢 Sign may appear at most once before digits.
            if (str.charAt(index) == '+' || str.charAt(index) == '-') {

                sign = str.charAt(index) == '+' ? 1 : -1;

                index++;
            }

            while (index < str.length()) {

                int digit = str.charAt(index) - '0';

                // 🟢 First non-digit permanently ends parsing.
                if (digit < 0 || digit > 9) {
                    break;
                }

                // 🟢 Decimal transition preserving the parser invariant.
                total = total * 10 + digit;

                long signedValue = sign * total;
                // 🔴 Clamp immediately after extending the number.
                // Waiting until the end risks incorrect intermediate values.
                if (signedValue >= Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                }

                if (signedValue <= Integer.MIN_VALUE) {
                    return Integer.MIN_VALUE;
                }

                // 🟡 Every character is consumed exactly once.
                index++;
            }

            // 🟢 Final value still satisfies the parser invariant.
            return (int) (sign * total);
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Explain the invariant
 * ---------------------
 *
 * "The parser maintains one invariant:
 * after processing every valid digit,
 * total equals the absolute value represented by all consumed
 * digits so far.
 *
 * Characters before the current index are permanently resolved.
 * Characters after the index have never been examined."
 *
 * ------------------------------------------------------------
 * Explain the discard rule
 * ------------------------------------------------------------
 *
 * Leading spaces are discarded once.
 *
 * One optional sign is consumed.
 *
 * Digits extend the current number.
 *
 * The first non-digit permanently terminates parsing.
 *
 * ------------------------------------------------------------
 * Explain correctness
 * ------------------------------------------------------------
 *
 * Decimal notation grows one digit at a time:
 *
 * value = value × 10 + digit
 *
 * Since every accepted digit performs exactly this transition,
 * the invariant always matches the consumed prefix.
 *
 * ------------------------------------------------------------
 * Explain termination
 * ------------------------------------------------------------
 *
 * Index increases monotonically.
 *
 * No branch decreases index.
 *
 * Therefore every character is processed at most once.
 *
 * ------------------------------------------------------------
 * In-place feasibility
 * ------------------------------------------------------------
 *
 * Yes.
 *
 * Input is read only.
 *
 * Constant auxiliary memory.
 *
 * ------------------------------------------------------------
 * Streaming feasibility
 * ------------------------------------------------------------
 *
 * Yes.
 *
 * Only the current parser state is required.
 *
 * Previous characters never need to be revisited.
 *
 * ------------------------------------------------------------
 * When NOT to use
 * ------------------------------------------------------------
 *
 * Do not use this deterministic parser when grammar requires:
 *
 * nested expressions
 * recursion
 * precedence parsing
 * backtracking
 *
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 *
 * Sequential string parsing.
 *
 * ------------------------------------------------------------
 *
 * Invariant
 *
 * total equals every consumed digit.
 *
 * ------------------------------------------------------------
 *
 * Search Target
 *
 * None.
 *
 * We consume, not search.
 *
 * ------------------------------------------------------------
 *
 * Discard Rule
 *
 * spaces
 * →
 * sign
 * →
 * digits
 * →
 * stop.
 *
 * ------------------------------------------------------------
 *
 * Common Trap
 *
 * Detecting overflow too late.
 *
 * ------------------------------------------------------------
 *
 * Edge Cases
 *
 * ""
 *
 * " "
 *
 * "+"
 *
 * "-"
 *
 * "+-12"
 *
 * "00042"
 *
 * "-00012"
 *
 * Integer overflow.
 *
 * ------------------------------------------------------------
 *
 * One-liner
 *
 * "Maintain one parser state and grow the number one digit at a
 * time."
 *
 * ------------------------------------------------------------
 *
 * Re-derivation Cue
 *
 * Skip.
 *
 * Sign.
 *
 * Digits.
 *
 * Clamp.
 *
 * Stop.
 *
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variant
 *
 * Parse unsigned integer.
 *
 * Reasoning
 *
 * Remove sign state.
 *
 * Invariant remains identical.
 *
 * ------------------------------------------------------------
 *
 * Variant
 *
 * Parse hexadecimal.
 *
 * Reasoning
 *
 * Transition becomes:
 *
 * total = total × 16 + digit
 *
 * Invariant is unchanged.
 *
 * ------------------------------------------------------------
 *
 * Variant
 *
 * Arbitrary precision.
 *
 * Reasoning
 *
 * Replace long with BigInteger.
 *
 * Overflow checks disappear.
 *
 * Parsing state remains unchanged.
 *
 * ------------------------------------------------------------
 *
 * Variant
 *
 * Decimal fractions.
 *
 * Pattern Break
 *
 * Need additional parser state after '.'
 *
 * Single-state parser is no longer sufficient.
 *
 * ------------------------------------------------------------
 *
 * Variant
 *
 * Scientific notation.
 *
 * Pattern Break
 *
 * Requires exponent parsing.
 *
 * Additional parser states become necessary.
 *
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * Can you state the invariant?
 *
 * □ total equals the consumed digits.
 *
 * ------------------------------------------------------------
 *
 * Can you identify the search target?
 *
 * □ There is no search space.
 *
 * ------------------------------------------------------------
 *
 * Can you explain the discard rule?
 *
 * □ spaces
 * □ sign
 * □ digits
 * □ terminate
 *
 * ------------------------------------------------------------
 *
 * Can you explain termination?
 *
 * □ index only increases.
 *
 * ------------------------------------------------------------
 *
 * Can you explain naive failure?
 *
 * □ delayed overflow
 * □ unnecessary buffering
 * □ parser state lost
 *
 * ------------------------------------------------------------
 *
 * Can you enumerate edge cases?
 *
 * □ empty
 * □ spaces
 * □ sign only
 * □ overflow
 * □ leading zeros
 * □ first character invalid
 *
 * ------------------------------------------------------------
 *
 * Debugging readiness
 *
 * □ verify index
 * □ verify sign
 * □ verify total
 * □ verify overflow
 * □ verify stop condition
 *
 * ------------------------------------------------------------
 *
 * Variant readiness
 *
 * □ hexadecimal
 * □ unsigned
 * □ arbitrary precision
 *
 * ------------------------------------------------------------
 *
 * Pattern boundary
 *
 * □ deterministic sequential parser
 * □ not recursive descent
 * □ not sliding window
 * □ not binary search
 *
 * ============================================================
 * 🧪 MAIN + SELF-VERIFYING TESTS
 * ============================================================
 */

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        // Happy path.
        assert solver.myAtoi("42") == 42;

        // Leading spaces followed by negative sign.
        assert solver.myAtoi("   -42") == -42;

        // Parsing stops at first non-digit.
        assert solver.myAtoi("4193 with words") == 4193;

        // Invalid first character.
        assert solver.myAtoi("words and 987") == 0;

        // Sign without digits.
        assert solver.myAtoi("+") == 0;

        // Negative sign without digits.
        assert solver.myAtoi("-") == 0;

        // Empty string.
        assert solver.myAtoi("") == 0;

        // Only spaces.
        assert solver.myAtoi("      ") == 0;

        // Positive sign.
        assert solver.myAtoi("+7") == 7;

        // Leading zeros.
        assert solver.myAtoi("00000123") == 123;

        // Leading zeros with sign.
        assert solver.myAtoi("-0000123") == -123;

        // Stop after digits.
        assert solver.myAtoi("12abc34") == 12;

        // Space after sign is illegal.
        assert solver.myAtoi("+ 42") == 0;

        // Multiple signs.
        assert solver.myAtoi("+-12") == 0;

        // Minus followed by plus.
        assert solver.myAtoi("-+12") == 0;

        // Decimal point terminates parsing.
        assert solver.myAtoi("3.14159") == 3;

        // Overflow above Integer.MAX_VALUE.
        assert solver.myAtoi("91283472332") == Integer.MAX_VALUE;

        // Overflow below Integer.MIN_VALUE.
        assert solver.myAtoi("-91283472332") == Integer.MIN_VALUE;

        // Exact Integer.MAX_VALUE.
        assert solver.myAtoi("2147483647") == Integer.MAX_VALUE;

        // Exact Integer.MIN_VALUE.
        assert solver.myAtoi("-2147483648") == Integer.MIN_VALUE;

        // One above Integer.MAX_VALUE.
        assert solver.myAtoi("2147483648") == Integer.MAX_VALUE;

        // One below Integer.MIN_VALUE.
        assert solver.myAtoi("-2147483649") == Integer.MIN_VALUE;

        // Maximum leading spaces.
        assert solver.myAtoi("          99") == 99;

        // Single digit.
        assert solver.myAtoi("5") == 5;

        // Zero.
        assert solver.myAtoi("0") == 0;

        // Negative zero.
        assert solver.myAtoi("-0") == 0;

        // Positive zero.
        assert solver.myAtoi("+0") == 0;

        // Digits followed immediately by sign.
        assert solver.myAtoi("123-45") == 123;

        // Sign followed by letters.
        assert solver.myAtoi("-abc") == 0;

        // Dot before digits.
        assert solver.myAtoi(".123") == 0;

        // Mixed spaces after digits are ignored.
        assert solver.myAtoi("15   99") == 15;

        // Null input handled defensively.
        assert solver.myAtoi(null) == 0;

        System.out.println("All assertions passed.");
    }

}

/*
============================================================
🧘 FINAL CLOSURE STATEMENT
============================================================

I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/


