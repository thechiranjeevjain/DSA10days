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
public class FractionToRecurringDecimalV5 {

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
     * First-Principles Invention Path
     * ==============================================================
     *
     * 1. Decimal digits are exactly school long division.
     *
     *      digit     = (remainder * 10) / divisor
     *      remainder = (remainder * 10) % divisor
     *
     * 2. Ask: what information completely determines the next step?
     *    For a fixed divisor: the remainder.
     *
     * 3. Possible remainders are finite:
     *      0 ... divisor - 1
     *
     *    Therefore the process must eventually either:
     *      - reach 0      -> terminates
     *      - repeat state -> cycles
     *
     * 4. If remainder r repeats, the exact future produced from r repeats.
     *
     * 5. We need not only "have I seen r?" but "where did r first begin
     *    producing output?" so that '(' can be inserted there.
     *
     *      Map<remainder, firstOutputIndex>
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

            if ((numerator < 0) ^ (denominator < 0)) {
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

            Map<Long, Integer> firstPosition = new HashMap<>();

            while (remainder != 0) {
                Integer cycleStart = firstPosition.get(remainder);

                if (cycleStart != null) {
                    answer.insert(cycleStart.intValue(), '(');
                    answer.append(')');
                    break;
                }

                firstPosition.put(remainder, answer.length());

                remainder *= 10;
                answer.append(remainder / divisor);
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
     * answer = "0."
     *
     * remainder | stored index | emitted digit | next remainder
     * ----------+--------------+---------------+---------------
     * 4         | 2            | 0             | 40
     * 40        | 3            | 1             | 67
     * 67        | 4            | 2             | 4
     *
     * remainder 4 repeats; its first index was 2.
     * Insert '(' at 2 and append ')' -> "0.(012)".
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
     * sign: XOR
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
