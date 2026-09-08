package org.chijai.day10.session2;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 166 - Fraction to Recurring Decimal
 * https://leetcode.com/problems/fraction-to-recurring-decimal/
 *
 * PATTERN
 * -------
 * Primary:   Simulation / Long Division + HashMap
 * Deeper:    Cycle Detection on Deterministic State
 * State:     remainder
 * Map value: first output index generated from that remainder
 *
 * Recognition:
 *   A process generates output step by step.
 *   If the same state returns, the future output repeats.
 */
public class FractionToRecurringDecimal {

    /*
     * ==============================================================
     * Problem Statement
     * ==============================================================
     * Given two integers, numerator and denominator, return their fraction
     * as a decimal string.
     *
     * If the fractional part repeats, wrap only the repeating digits
     * in parentheses.
     *
     * Examples
     * --------
     * Input:  numerator = 1, denominator = 2
     * Output: "0.5"
     *
     * Input:  numerator = 2, denominator = 1
     * Output: "2"
     *
     * Input:  numerator = 4, denominator = 333
     * Output: "0.(012)"
     *
     * Useful extra example:
     * Input:  numerator = 1, denominator = 6
     * Output: "0.1(6)"
     *
     * Constraints that matter to the implementation:
     *   - numerator and denominator are int values
     *   - denominator != 0
     *   - the result must be exact, not floating-point approximation
     */

    /*
     * ==============================================================
     * Why the Obvious Alternatives Fail
     * ==============================================================
     *
     * double:
     *   approximate representation; cannot reliably locate repetition.
     *
     * Track repeated digits:
     *   same digit can occur in unrelated positions.
     *   The future is determined by remainder, not by emitted digit.
     *
     * Linear search through old remainders:
     *   correct, but O(k^2). HashMap makes lookup expected O(1).
     */

    /*
     * ==============================================================
     * Prerequisite - Decimal Long Division From Zero
     * ==============================================================
     *
     * Example: 1 / 4
     *
     * 4 does not fit into 1 as a whole number, so answer starts "0.".
     * Remainder = 1.
     *
     * To get the next decimal digit, move one decimal place right.
     * One whole = ten tenths, so:
     *
     *     remainder * 10
     *     1 * 10 = 10
     *
     * Then split 10 into quotient + leftover:
     *
     *     10 / 4 = 2       -> next digit
     *     10 % 4 = 2       -> new remainder
     *
     * Answer is now "0.2".
     *
     * Repeat with remainder 2:
     *
     *     2 * 10 = 20
     *     20 / 4 = 5       -> next digit
     *     20 % 4 = 0       -> nothing left
     *
     * Final answer = "0.25".
     *
     * Reusable decimal step:
     *
     *     remainder *= 10;
     *     digit = remainder / divisor;
     *     remainder %= divisor;
     *
     * Read it as:
     *
     *     leftover
     *        -> move one decimal place right
     *        -> take next digit
     *        -> keep new leftover
     */

    /*
     * ==============================================================
     * First-Principles Invention Path
     * ==============================================================
     *
     * 1. Decimal digits come from the long-division step above.
     *
     * 2. For a fixed divisor, what completely determines the NEXT step?
     *    The remainder.
     *
     *       same remainder
     *            -> same next digit
     *            -> same next remainder
     *            -> same future
     *
     * 3. A remainder is NOT necessarily one digit.
     *    It is only guaranteed to be smaller than the divisor:
     *
     *       0 <= remainder < divisor
     *
     *    Example: with divisor 333, remainder may be 40 or 67.
     *
     * 4. Only finitely many remainders are possible.
     *    Therefore the process must eventually:
     *
     *       remainder == 0   -> decimal terminates
     *
     *    OR
     *
     *       remainder repeats -> decimal repeats forever
     *
     * 5. A Set could tell us that a remainder repeated.
     *    But we also need WHERE its output began so we can insert '('.
     *
     *       remainder -> first output index
     *
     *       Map<Long, Integer> firstPosition
     *
     *    Long    = remainder state; arithmetic is kept in long safely.
     *    Integer = StringBuilder index returned by answer.length().
     *
     * Final structure:
     *   sign -> whole part -> remainder loop -> repeated remainder detection.
     */

    /*
     * ==============================================================
     * Optimal Solution (Interview Preferred)
     * ==============================================================
     *
     * Invariant:
     *   Before each fractional digit, remainder is the unresolved state.
     *   firstPosition[r] is the answer index where output generated from r
     *   begins.
     *
     * Important ordering:
     *   STORE remainder -> answer.length()
     *   BEFORE remainder *= 10.
     *
     * Time:  O(k) expected
     * Space: O(k)
     * k = digits generated before termination or first repeated remainder.
     */
    static class Solution {

        public String fractionToDecimal(int numerator, int denominator) {
            if (numerator == 0) {
                return "0";
            }

            StringBuilder answer = new StringBuilder();

            // Different signs => exactly one value is negative => negative result.
            // Boolean != is the same truth condition as XOR, but reads more directly.
            if ((numerator < 0) != (denominator < 0)) {
                answer.append('-');
            }

            // Cast before abs: abs(Integer.MIN_VALUE) overflows as int.
            long dividend = Math.abs((long) numerator);
            long divisor = Math.abs((long) denominator);

            answer.append(dividend / divisor);

            long remainder = dividend % divisor;

            if (remainder == 0) {
                return answer.toString();
            }

            answer.append('.');

            // remainder -> output index where digits produced from it begin
            Map<Long, Integer> firstPosition = new HashMap<>();

            while (remainder != 0) {

                if (firstPosition.containsKey(remainder)) {
                    int cycleStart = firstPosition.get(remainder);

                    // Everything from cycleStart to the current end repeats.
                    answer.insert(cycleStart, '(');
                    answer.append(')');
                    break;
                }

                // Store BEFORE generating the next digit.
                // If this remainder returns, '(' belongs at this exact index.
                firstPosition.put(remainder, answer.length());

                // Ordinary decimal long division: bring down a zero.
                remainder *= 10;

                // Integer division gives the next decimal digit.
                answer.append(remainder / divisor);

                // % keeps whatever is still left for the next iteration.
                remainder %= divisor;
            }

            return answer.toString();
        }
    }

    /*
     * ==============================================================
     * Dry Run - 4 / 333
     * ==============================================================
     *
     * Whole part:
     *
     *     4 / 333 = 0
     *     4 % 333 = 4
     *
     * answer = "0."
     * firstPosition = {}
     *
     * --------------------------------------------------------------
     * Iteration 1 - remainder = 4
     * --------------------------------------------------------------
     *
     * 4 has not been seen.
     *
     * answer.length() = 2
     * store: 4 -> 2
     *
     * Meaning:
     *   digits produced from remainder 4 begin at answer index 2.
     *
     *     remainder *= 10   -> 4 * 10 = 40
     *     40 / 333          -> digit 0
     *     40 % 333          -> new remainder 40
     *
     * answer = "0.0"
     * map    = {4 -> 2}
     *
     * --------------------------------------------------------------
     * Iteration 2 - remainder = 40
     * --------------------------------------------------------------
     *
     * 40 has not been seen.
     *
     * answer.length() = 3
     * store: 40 -> 3
     *
     *     40 * 10 = 400
     *     400 / 333 = 1
     *     400 % 333 = 67
     *
     * because:
     *     400 = 333 * 1 + 67
     *
     * answer = "0.01"
     * map    = {4 -> 2, 40 -> 3}
     *
     * --------------------------------------------------------------
     * Iteration 3 - remainder = 67
     * --------------------------------------------------------------
     *
     * 67 has not been seen.
     *
     * answer.length() = 4
     * store: 67 -> 4
     *
     *     67 * 10 = 670
     *     670 / 333 = 2
     *     670 % 333 = 4
     *
     * because:
     *     670 = 333 * 2 + 4
     *
     * answer = "0.012"
     * map    = {4 -> 2, 40 -> 3, 67 -> 4}
     *
     * --------------------------------------------------------------
     * Iteration 4 - remainder = 4 AGAIN
     * --------------------------------------------------------------
     *
     * Map already contains:
     *
     *     4 -> 2
     *
     * Current answer with indices:
     *
     *     chars:  0 . 0 1 2
     *     index:  0 1 2 3 4
     *               ^
     *               cycleStart = 2
     *
     * Insert '(' at index 2:
     *
     *     "0.(012"
     *
     * Append ')' at the current end:
     *
     *     "0.(012)"
     *
     * Why can we break?
     *
     * We returned to the exact same remainder 4.
     * For a fixed divisor, the same remainder produces the same future:
     *
     *     4 -> digit 0 -> 40
     *    40 -> digit 1 -> 67
     *    67 -> digit 2 -> 4
     *
     * So the future is now guaranteed to be:
     *
     *     012 012 012 ...
     *
     * There is no new information left to generate.
     *
     * Final:
     *
     *     4 / 333 = 0.(012)
     *
     * Compact table:
     *
     * current r | store    | r*10 | digit | next r | answer
     * ----------+----------+------+-------+--------+--------
     * 4         | 4  -> 2  | 40   | 0     | 40     | 0.0
     * 40        | 40 -> 3  | 400  | 1     | 67     | 0.01
     * 67        | 67 -> 4  | 670  | 2     | 4      | 0.012
     * 4 again   | seen     |  -   | -     | -      | 0.(012)
     *
     * Key line:
     *
     *     firstPosition.put(remainder, answer.length());
     *
     * means:
     *
     *     "If this remainder ever returns, the repeating output starts
     *      at the current answer index."
     */

    /*
     * ==============================================================
     * Interview Articulation
     * ==============================================================
     *
     * "I simulate long division. For a fixed denominator, the remainder
     * uniquely determines the next digit and all future digits. I map every
     * remainder to the output index where its generated sequence begins.
     * Remainder zero means termination; a repeated remainder means a cycle,
     * so I insert '(' at its first recorded position. I use long before
     * Math.abs to handle Integer.MIN_VALUE safely."
     */

    /*
     * ==============================================================
     * 30-Second Recall
     * ==============================================================
     *
     * FRACTION = LONG DIVISION + REMAINDER CYCLE
     *
     * sign: signs differ => negative
     * cast to long BEFORE abs
     * append quotient
     * remainder = dividend % divisor
     *
     * while remainder != 0:
     *   repeated remainder -> insert '(' at stored index, append ')'
     *   store remainder -> answer.length()
     *   remainder *= 10
     *   append remainder / divisor
     *   remainder %= divisor
     *
     * Remember the remainder, not the digit.
     */

    /*
     * ==============================================================
     * Reusable Pattern Skeleton
     * ==============================================================
     *
     * Map<State, Integer> firstPosition = new HashMap<>();
     *
     * while (!terminal(state)) {
     *     if (firstPosition.containsKey(state)) {
     *         // cycle begins at firstPosition.get(state)
     *         break;
     *     }
     *
     *     firstPosition.put(state, output.length());
     *     output.append(emit(state));
     *     state = next(state);
     * }
     *
     * Use when:
     *   deterministic state -> generated output -> repeated state matters.
     */

    /*
     * ==============================================================
     * Traps / Pattern Boundaries
     * ==============================================================
     *
     * HashSet vs HashMap:
     *   Set answers WHETHER a cycle exists.
     *   Map also answers WHERE the cycle starts in the output.
     *
     * Floyd cycle detection:
     *   good when only cycle structure matters.
     *   Here Map is more direct because parentheses need an output index.
     *
     * Negative values:
     *   decide sign once, then perform division on positive long values.
     *
     * Why long for remainder / Map key:
     *   remainder is < divisor, not < 10. It may have many digits.
     *   The arithmetic uses long because abs(Integer.MIN_VALUE) and
     *   remainder * 10 can exceed int range.
     */

    /*
     * ==============================================================
     * RELATED / Transfer
     * ==============================================================
     *
     * LC 202  Happy Number
     *         Same deterministic-state cycle idea.
     *         Repeated number => cycle.
     *
     * LC 142  Linked List Cycle II
     *         Find where a cycle begins.
     *
     * LC 287  Find the Duplicate Number
     *         Functional-graph / cycle-detection viewpoint.
     *
     * LC 187  Repeated DNA Sequences
     *         Remember previously seen states / sequences.
     *
     * Transfer:
     *
     *     deterministic process
     *              +
     *        finite state space
     *              =>
     *     repeated state means repeated future
     *
     * LC 166 specifically needs:
     *
     *     Map<remainder, firstOutputPosition>
     *
     * because detecting the cycle is not enough;
     * we must also know where to insert '('.
     *
     * Strongest recall chain:
     *
     *     166 Fraction to Recurring Decimal
     *          remainder -> first output position
     *
     *     202 Happy Number
     *          number -> next number
     *
     *     142 Linked List Cycle II
     *          node -> next node
     *
     *     287 Find the Duplicate Number
     *          value/index -> next state
     *
     * Set<State> answers: have I seen this state?
     * Map<State, Position> additionally answers: where did it first occur?
     */

    /*
     * ==============================================================
     * Related Learning Sequence
     * ==============================================================
     *
     * Learn the same deeper idea by adding one requirement at a time:
     *
     *   1. LC 202 - Happy Number
     *      Detect repeated deterministic state.
     *      Tool: HashSet.
     *
     *   2. LC 142 - Linked List Cycle II
     *      Detect a cycle AND recover its entry without extra memory.
     *      Tool: Floyd slow/fast pointers.
     *
     *   3. LC 287 - Find the Duplicate Number
     *      Recognize an array as a functional graph, then reuse Floyd.
     *
     *   4. LC 166 - Fraction to Recurring Decimal
     *      A cycle is not enough: we need the output position where it began.
     *      Tool: Map<State, Position>.
     *
     * Progression:
     *
     *   repeated state
     *       -> cycle entry
     *       -> hidden functional graph
     *       -> cycle entry in generated output
     *
     * LC 187 Repeated DNA Sequences is useful HashSet/HashMap practice,
     * but it is not as structurally close as the four-problem chain above.
     */

    /*
     * ==============================================================
     * Related Working Drills
     * ==============================================================
     * These are intentionally minimal.
     * Their purpose is transfer practice, not to replace each problem's
     * canonical study file.
     */

    static class HappyNumberDrill {

        boolean isHappy(int n) {
            java.util.Set<Integer> seen = new java.util.HashSet<>();

            while (n != 1 && seen.add(n)) {
                n = next(n);
            }

            return n == 1;
        }

        private int next(int n) {
            int sum = 0;

            while (n > 0) {
                int digit = n % 10;
                sum += digit * digit;
                n /= 10;
            }

            return sum;
        }
    }

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    static class LinkedListCycleIIDrill {

        ListNode detectCycle(ListNode head) {
            ListNode slow = head;
            ListNode fast = head;

            do {
                if (fast == null || fast.next == null) {
                    return null;
                }

                slow = slow.next;
                fast = fast.next.next;
            } while (slow != fast);

            slow = head;

            while (slow != fast) {
                slow = slow.next;
                fast = fast.next;
            }

            return slow;
        }
    }

    static class FindDuplicateNumberDrill {

        int findDuplicate(int[] nums) {
            int slow = nums[0];
            int fast = nums[0];

            do {
                slow = nums[slow];
                fast = nums[nums[fast]];
            } while (slow != fast);

            slow = nums[0];

            while (slow != fast) {
                slow = nums[slow];
                fast = nums[fast];
            }

            return slow;
        }
    }

    /*
     * ==============================================================
     * Mastery Check
     * ==============================================================
     *
     * 1. Why does the remainder uniquely determine the future?
     * 2. Why store it before multiplying by 10?
     * 3. Why Map instead of Set?
     * 4. Why cast before Math.abs?
     * 5. For 1 / 6, why is the answer "0.1(6)" rather than "0.(16)"?
     */

    public static void main(String[] args) {
        Solution solution = new Solution();

        test(solution, 1, 2, "0.5");
        test(solution, 2, 1, "2");
        test(solution, 4, 333, "0.(012)");
        test(solution, 1, 6, "0.1(6)");
        test(solution, 1, 3, "0.(3)");
        test(solution, -50, 8, "-6.25");
        test(solution, 7, -12, "-0.58(3)");
        test(solution, Integer.MIN_VALUE, 1, "-2147483648");

        testRelatedDrills();
    }

    private static void testRelatedDrills() {
        HappyNumberDrill happy = new HappyNumberDrill();
        if (!happy.isHappy(19) || happy.isHappy(2)) {
            throw new AssertionError("Happy Number drill failed");
        }

        ListNode a = new ListNode(3);
        ListNode b = new ListNode(2);
        ListNode c = new ListNode(0);
        ListNode d = new ListNode(-4);
        a.next = b;
        b.next = c;
        c.next = d;
        d.next = b;

        LinkedListCycleIIDrill cycle = new LinkedListCycleIIDrill();
        if (cycle.detectCycle(a) != b) {
            throw new AssertionError("Linked List Cycle II drill failed");
        }

        FindDuplicateNumberDrill duplicate = new FindDuplicateNumberDrill();
        if (duplicate.findDuplicate(new int[]{1, 3, 4, 2, 2}) != 2) {
            throw new AssertionError("Find Duplicate Number drill failed");
        }
    }

    private static void test(
            Solution solution,
            int numerator,
            int denominator,
            String expected) {

        String actual = solution.fractionToDecimal(numerator, denominator);

        if (!expected.equals(actual)) {
            throw new AssertionError(
                    numerator + "/" + denominator
                            + " expected=" + expected
                            + " actual=" + actual);
        }
    }
}
