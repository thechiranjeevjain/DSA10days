package org.chijai.day4.LinkedList.session4;

import java.util.ArrayList;
import java.util.List;

public class MiddleOfLinkedList {

    /*
     * ============================================================
     * PRIMARY PROBLEM — MIDDLE OF LINKED LIST
     * ============================================================
     *
     * Given the head of a singly linked list, return its middle node.
     *
     * If there are two middle nodes, return the SECOND middle.
     *
     * Example:
     * 1 -> 2 -> 3 -> 4 -> 5      => 3
     * 1 -> 2 -> 3 -> 4 -> 5 -> 6 => 4
     *
     * Pattern:
     * Fast & Slow Pointers
     *
     * Time  : O(n)
     * Space : O(1)
     */

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    /*
     * ============================================================
     * PREFERRED INTERVIEW SOLUTION
     * ============================================================
     */

    static class Optimal {

        ListNode middleNode(ListNode head) {

            ListNode slow = head;
            ListNode fast = head;

            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }

            return slow;
        }
    }

    /*
     * ============================================================
     * WHY 1 — WHY TWO SPEEDS?
     * ============================================================
     *
     * We want the halfway position without first counting n.
     *
     * slow moves 1 step.
     * fast moves 2 steps.
     *
     * So fast consumes the list twice as quickly.
     * When fast reaches the end, slow has reached halfway.
     */

    /*
     * ============================================================
     * WHY 2 — SHORTEST CORRECTNESS PROOF
     * ============================================================
     *
     * After k iterations:
     *
     *      slow has moved k steps
     *      fast has moved 2k steps
     *
     * When fast reaches / cannot continue past the end:
     *
     *      k = floor(n / 2)
     *
     * Therefore slow is at index floor(n / 2):
     *
     *      odd n  -> unique middle
     *      even n -> second middle
     *
     * MEMORY:
     *
     *      FAST travels 2x
     *      -> SLOW ends halfway.
     */

    /*
     * ============================================================
     * WHY 3 — WHY THIS LOOP CONDITION?
     * ============================================================
     *
     * while (fast != null && fast.next != null)
     *
     * fast moves TWO nodes, so before moving we must know:
     *
     *      1. fast exists
     *      2. fast.next exists
     *
     * Java && short-circuits left to right, so fast.next is never
     * evaluated when fast is null.
     */

    /*
     * ============================================================
     * WHY 4 — WHY DOES EVEN LENGTH RETURN SECOND MIDDLE?
     * ============================================================
     *
     * Example:
     *
     *      1 -> 2 -> 3 -> 4 -> 5 -> 6
     *
     * start:  slow=1 fast=1
     * round1: slow=2 fast=3
     * round2: slow=3 fast=5
     * round3: slow=4 fast=null
     *
     * Return 4.
     *
     * Index:
     *
     *      n / 2 = 6 / 2 = 3
     *
     * Zero-based index 3 is node 4 — the SECOND middle.
     */

    /*
     * ============================================================
     * 30-SECOND RECALL CARD
     * ============================================================
     *
     * TRIGGER
     * -------
     * "Find middle / halfway point of linked list."
     *
     * PATTERN
     * -------
     * Fast & Slow Pointers.
     *
     * INVARIANT
     * ---------
     * After k rounds:
     *
     *      slow = k steps
     *      fast = 2k steps
     *
     * TEMPLATE
     * --------
     *
     * slow = head
     * fast = head
     *
     * while (fast != null && fast.next != null):
     *      slow = slow.next
     *      fast = fast.next.next
     *
     * return slow
     *
     * TRAP
     * ----
     * Check BOTH fast and fast.next before moving fast twice.
     *
     * ONE-LINER
     * ---------
     * Fast travels 2x -> slow ends halfway.
     */

    /*
     * ============================================================
     * REUSABLE MASTER TEMPLATE — FAST / SLOW POINTERS
     * ============================================================
     *
     * ListNode slow = head;
     * ListNode fast = head;
     *
     * while (fast != null && fast.next != null) {
     *     slow = slow.next;
     *     fast = fast.next.next;
     * }
     *
     * // Meaning of slow now depends on the problem:
     * // middle, cycle state, split point, etc.
     *
     * Core idea:
     *
     *      relative movement creates positional information
     *      without storing positions explicitly.
     */

    /*
     * ============================================================
     * BASELINE APPROACH — COUNT LENGTH
     * ============================================================
     *
     * Useful as the obvious first solution:
     *
     * Pass 1: count n.
     * Pass 2: move n / 2 steps.
     *
     * Time  : O(n)
     * Space : O(1)
     *
     * Same asymptotic complexity, but two traversals.
     */

    static class CountLength {

        ListNode middleNode(ListNode head) {

            int length = 0;

            for (ListNode current = head;
                 current != null;
                 current = current.next) {
                length++;
            }

            ListNode current = head;

            for (int i = 0; i < length / 2; i++) {
                current = current.next;
            }

            return current;
        }
    }

    /*
     * ============================================================
     * VARIATION — RETURN FIRST MIDDLE
     * ============================================================
     *
     * For:
     *
     *      1 -> 2 -> 3 -> 4
     *
     * return 2 instead of 3.
     *
     * Start fast one step ahead.
     *
     * Why?
     *
     * The head start shifts the stopping point one node left
     * for even-length lists while preserving the same 2:1 motion.
     */

    static class FirstMiddle {

        ListNode middleNode(ListNode head) {

            if (head == null) {
                return null;
            }

            ListNode slow = head;
            ListNode fast = head.next;

            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }

            return slow;
        }
    }

    /*
     * ============================================================
     * PATTERN BOUNDARY — SPEED RATIO VS FIXED GAP
     * ============================================================
     *
     * MIDDLE / CYCLE
     * --------------
     * Relative SPEED matters.
     *
     *      slow = 1x
     *      fast = 2x
     *
     * N-TH NODE FROM END
     * -----------------
     * Relative DISTANCE matters.
     *
     * Create a fixed gap of n nodes,
     * then move both pointers at the SAME speed.
     *
     * Useful distinction:
     *
     *      DIFFERENT SPEED -> positional/cycle relationship
     *      FIXED GAP       -> offset-from-end relationship
     */

    /*
     * ============================================================
     * RELATED / REINFORCEMENT PROBLEMS
     * ============================================================
     *
     * 1. Linked List Cycle
     *    Same fast/slow movement.
     *    Difference: stop when slow == fast instead of at list end.
     *
     * 2. Linked List Cycle II
     *    First detect collision, then use the cycle-entry property.
     *
     * 3. Palindrome Linked List
     *    Use fast/slow to find the middle,
     *    reverse the second half, then compare.
     *
     * 4. Reorder List
     *    Find middle -> reverse second half -> merge halves.
     *
     * 5. Remove Nth Node From End
     *    Related two-pointer family,
     *    but uses a FIXED GAP rather than a 2:1 speed ratio.
     *
     * 6. Happy Number
     *    Floyd cycle detection on generated states rather than nodes.
     *
     * REINFORCEMENT QUESTION:
     *
     * Ask first:
     *
     *      "Do I need a speed relationship,
     *       or a fixed-distance relationship?"
     */

    /*
     * ============================================================
     * COMMON FAILURE MODES
     * ============================================================
     *
     * 1. while (fast.next != null)
     *    -> crashes if fast becomes null.
     *
     * 2. fast moves only once
     *    -> both pointers move 1:1; midpoint property disappears.
     *
     * 3. Wrong middle convention
     *    -> this problem requires SECOND middle for even n.
     *
     * 4. Over-explaining
     *    -> interview proof only needs:
     *
     *       after k rounds slow=k, fast=2k;
     *       fast reaches end => slow is halfway.
     */

    /*
     * ============================================================
     * SELF-VERIFYING TESTS
     * ============================================================
     *
     * Run with assertions enabled:
     *
     *      java -ea MiddleOfLinkedList
     */

    public static void main(String[] args) {

        Optimal optimal = new Optimal();
        CountLength baseline = new CountLength();
        FirstMiddle firstMiddle = new FirstMiddle();

        ListNode odd = build(1, 2, 3, 4, 5);
        assert optimal.middleNode(odd).val == 3;
        assert baseline.middleNode(odd).val == 3;

        ListNode even = build(1, 2, 3, 4, 5, 6);
        assert optimal.middleNode(even).val == 4;
        assert baseline.middleNode(even).val == 4;

        ListNode single = build(42);
        assert optimal.middleNode(single).val == 42;

        ListNode two = build(7, 9);
        assert optimal.middleNode(two).val == 9;
        assert firstMiddle.middleNode(two).val == 7;

        ListNode four = build(1, 2, 3, 4);
        assert optimal.middleNode(four).val == 3;
        assert firstMiddle.middleNode(four).val == 2;

        ListNode identity = build(1, 2, 3, 4, 5);
        assert optimal.middleNode(identity) == identity.next.next;

        System.out.println("All assertions passed.");
    }

    static ListNode build(int... values) {

        if (values == null || values.length == 0) {
            return null;
        }

        ListNode head = new ListNode(values[0]);
        ListNode tail = head;

        for (int i = 1; i < values.length; i++) {
            tail.next = new ListNode(values[i]);
            tail = tail.next;
        }

        return head;
    }

    static List<Integer> toList(ListNode head) {

        List<Integer> result = new ArrayList<>();

        while (head != null) {
            result.add(head.val);
            head = head.next;
        }

        return result;
    }
}
