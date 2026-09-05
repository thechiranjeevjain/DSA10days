package org.chijai.day4.LinkedList.session1;

import java.util.ArrayList;
import java.util.List;

/**
 * =====================================================================================
 * REMOVE NTH NODE FROM END OF LIST — INTERVIEW CHAPTER V4
 * =====================================================================================
 *
 * Primary Pattern:
 * FIXED-GAP TWO POINTERS
 *
 * Retrieval Rule:
 * Need a node relative to the END, but traversal is forward-only?
 * Create the required gap first, then move both pointers together.
 *
 * Primary Problem:
 * LeetCode 19 — Remove Nth Node From End of List
 *
 * Goal:
 * Remove the nth node from the end of a singly linked list and return the head.
 *
 * Example:
 * 1 -> 2 -> 3 -> 4 -> 5, n = 2
 * result: 1 -> 2 -> 3 -> 5
 *
 * =====================================================================================
 */
public class RemoveNthFromEnd {

    // =================================================================================
    // LIST NODE
    // =================================================================================

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int value) {
            this.val = value;
        }
    }

    // =================================================================================
    // FIRST-PRINCIPLES INVENTION PATH — READ THIS BEFORE THE PRIMARY SOLUTION
    // =================================================================================
    /*
     * Trigger:
     * nth / kth position FROM THE END in a forward-only singly linked list.
     *
     * Actual obstacle:
     * while standing at a node, we do not know its distance from the end until we reach null.
     *
     * Obvious baseline:
     *      count length L
     *          ↓
     *      convert nth-from-end into a position from the front
     *          ↓
     *      walk again and delete
     *
     * Can we avoid knowing L explicitly?
     *
     *      use null as the END boundary
     *          ↓
     *      target is n edges from null
     *          ↓
     *      deletion needs target's predecessor
     *          ↓
     *      predecessor is n + 1 edges from null
     *          ↓
     *      create an n + 1 gap between slow and fast
     *          ↓
     *      move both one step together so the gap stays fixed
     *          ↓
     *      when fast == null, slow is exactly before target
     *
     * Head edge case:
     * if target is the original head, no predecessor exists.
     * Add dummy solely to manufacture that predecessor.
     *
     * Important distinction:
     *      n + 1 comes from needing the PREDECESSOR.
     *      dummy does NOT determine the gap; it only makes head deletion uniform.
     */

    // =================================================================================
    // PRIMARY INTERVIEW SOLUTION — FIXED GAP + DUMMY
    // =================================================================================

    static class OptimalFixedGapSolution {

        static ListNode removeNthFromEnd(ListNode head, int n) {

            ListNode dummy = new ListNode(0);
            dummy.next = head;

            ListNode slow = dummy;
            ListNode fast = dummy;

            for (int i = 0; i <= n; i++) {
                fast = fast.next;
            }

            while (fast != null) {
                slow = slow.next;
                fast = fast.next;
            }

            slow.next = slow.next.next;

            return dummy.next;
        }
    }

    // =================================================================================
    // APPROACH TRADE-OFFS — WHY EACH NEXT APPROACH EXISTS
    // =================================================================================
    /*
     * +----------------------+----------+----------+-------------------------------------+
     * | Approach             | Time     | Space    | Trade-off / Why move forward        |
     * +----------------------+----------+----------+-------------------------------------+
     * | Store nodes          | O(n)     | O(n)     | Very obvious; pays linear memory    |
     * | Two-pass length      | O(n)     | O(1)     | Removes memory; traverses twice     |
     * | Recursive unwind     | O(n)     | O(n)     | Elegant end-distance idea; stack    |
     * | Fixed-gap pointers   | O(n)     | O(1)     | One pass, constant space — PRIMARY  |
     * +----------------------+----------+----------+-------------------------------------+
     *
     * Important:
     * Two-pass length and fixed-gap are BOTH O(n) time and O(1) auxiliary space.
     *
     * Fixed-gap is preferred because it avoids an explicit length pass and directly
     * encodes the actual invariant needed by the problem.
     */

    // =================================================================================
    // FULL APPROACH PROGRESSION — RUNNABLE IMPLEMENTATIONS
    // =================================================================================

    /**
     * APPROACH 1 — STORE ALL NODES
     *
     * Natural thought:
     * If backward traversal is unavailable, remember the forward traversal.
     * Then index relative to the end.
     *
     * Time:  O(n)
     * Space: O(n)
     */
    static class StoreNodesSolution {

        static ListNode removeNthFromEnd(ListNode head, int n) {

            ListNode dummy = new ListNode(0);
            dummy.next = head;

            List<ListNode> nodes = new ArrayList<>();

            ListNode current = dummy;

            while (current != null) {
                nodes.add(current);
                current = current.next;
            }

            int predecessorIndex = nodes.size() - n - 1;
            ListNode predecessor = nodes.get(predecessorIndex);

            predecessor.next = predecessor.next.next;

            return dummy.next;
        }
    }

    /**
     * APPROACH 2 — COUNT LENGTH, THEN WALK TO PREDECESSOR
     *
     * Derivation:
     * If list length is L, the nth node from the end is position L - n from the start
     * using zero-based indexing.
     *
     * We actually want its predecessor, so starting from dummy we move L - n steps.
     *
     * Time:  O(n) overall
     * Space: O(1)
     *
     * This is the most important bridge to the primary solution:
     * we learn that the real problem is POSITION RELATIVE TO THE END.
     */
    static class TwoPassLengthSolution {

        static ListNode removeNthFromEnd(ListNode head, int n) {

            int length = 0;

            for (ListNode current = head;
                 current != null;
                 current = current.next) {
                length++;
            }

            ListNode dummy = new ListNode(0);
            dummy.next = head;

            ListNode predecessor = dummy;

            for (int i = 0; i < length - n; i++) {
                predecessor = predecessor.next;
            }

            predecessor.next = predecessor.next.next;

            return dummy.next;
        }
    }

    /**
     * APPROACH 3 — RECURSIVE UNWIND
     *
     * Natural idea:
     * Recursion reaches the end first.
     * During unwind we can count distance from the end.
     *
     * Time:  O(n)
     * Space: O(n) call stack
     *
     * Educational, but not preferred for large lists because the call stack grows
     * linearly and can overflow.
     */
    static class RecursiveDistanceSolution {

        static ListNode removeNthFromEnd(ListNode head, int n) {

            ListNode dummy = new ListNode(0);
            dummy.next = head;

            removeFromEnd(dummy, n);

            return dummy.next;
        }

        private static int removeFromEnd(ListNode node, int n) {

            if (node == null) {
                return 0;
            }

            int distanceFromEnd =
                    removeFromEnd(node.next, n) + 1;

            if (distanceFromEnd == n + 1) {
                node.next = node.next.next;
            }

            return distanceFromEnd;
        }
    }

    // =================================================================================
    // RECONSTRUCTION SKELETON + INVARIANT
    // =================================================================================
    /*
     * dummy -> head
     * slow = fast = dummy
     * move fast n + 1 steps
     * move both until fast == null
     * slow.next = slow.next.next
     * return dummy.next
     *
     * Invariant:
     * after initialization, fast stays exactly n + 1 edges ahead of slow.
     * Equal movement preserves that gap. Therefore when fast reaches null,
     * slow is the predecessor of the nth node from the end.
     */

    // =================================================================================
    // VISUAL DRY RUN
    // =================================================================================
    /*
     * Input:
     *
     *      1 -> 2 -> 3 -> 4 -> 5
     *      n = 2
     *
     * Add dummy:
     *
     *      D -> 1 -> 2 -> 3 -> 4 -> 5 -> null
     *      S
     *      F
     *
     * Move fast n + 1 = 3 times:
     *
     *      D -> 1 -> 2 -> 3 -> 4 -> 5 -> null
     *      S              F
     *
     * Move both together:
     *
     * +-----------+------+------+
     * | Iteration | slow | fast |
     * +-----------+------+------+
     * | start     | D    | 3    |
     * | 1         | 1    | 4    |
     * | 2         | 2    | 5    |
     * | 3         | 3    | null |
     * +-----------+------+------+
     *
     * Now:
     *
     *      slow = 3
     *      slow.next = 4   <- target
     *
     * Delete:
     *
     *      slow.next = slow.next.next
     *
     *      3.next = 5
     *
     * Result:
     *
     *      1 -> 2 -> 3 -> 5
     */

    // =================================================================================
    // EDGE CASE DRY RUN — REMOVE ORIGINAL HEAD
    // =================================================================================
    /*
     * Input:
     *
     *      1 -> 2 -> 3
     *      n = 3
     *
     *      D -> 1 -> 2 -> 3 -> null
     *      S
     *
     * Move fast n + 1 = 4 times:
     *
     *      fast = null
     *      slow = D
     *
     * No joint movement occurs.
     *
     *      slow.next = slow.next.next
     *      D.next = 2
     *
     * return dummy.next -> 2
     *
     * Result:
     *
     *      2 -> 3
     *
     * Dummy eliminates the special-case branch for deleting head.
     */

    // =================================================================================
    // CORRECTNESS PROOF — INTERVIEW DEFENSIBLE
    // =================================================================================
    /*
     * 1. Initially both pointers start at dummy.
     *
     * 2. fast advances n + 1 steps, so fast is exactly n + 1 edges ahead of slow.
     *
     * 3. Every later iteration advances both pointers once, preserving that gap.
     *
     * 4. When fast becomes null, there are zero nodes after fast and therefore exactly
     *    n nodes after slow.next. Equivalently, slow.next is the nth node from the end.
     *
     * 5. Assigning slow.next = slow.next.next removes exactly that node.
     *
     * Therefore the algorithm is correct.
     */

    // =================================================================================
    // COMPLEXITY DERIVATION
    // =================================================================================
    /*
     * Let N be the number of list nodes.
     *
     * fast advances at most N + 1 edges total.
     * slow advances at most N edges total.
     *
     * Each pointer only moves forward.
     * No node is revisited by the same pointer.
     *
     * Time:  O(N)
     * Space: O(1)
     *
     * Dummy is one constant-sized node, so auxiliary space remains O(1).
     */

    // =================================================================================
    // HIGH-ROI TRAPS AND NUANCES
    // =================================================================================
    /*
     * 1. This is FIXED GAP, not fast/slow relative speed.
     *    After setup, both pointers move at the same speed.
     *
     * 2. LeetCode guarantees valid n.
     *    Without that guarantee, validate n before dereferencing fast.
     *
     * 3. Do not confuse "nth from end" with "middle".
     *    Middle uses a 2:1 speed ratio; this problem preserves a chosen distance.
     */

    // =================================================================================
    // ±Δ WORDING / CONSTRAINT CHANGES — WHEN THE PATTERN CHANGES
    // =================================================================================
    /*
     * "Return the nth node from the end"
     *      → gap n, no predecessor required
     *
     * "Remove the nth node from the end"
     *      → dummy + gap n + 1
     *
     * "Find the middle node"
     *      → slow/fast relative-speed pattern
     *
     * "Find intersection of two lists"
     *      → total traversal equalization / length alignment
     *
     * "Detect a cycle"
     *      → relative-speed collision
     *
     * "Doubly linked list with tail pointer"
     *      → backward traversal may make fixed-gap unnecessary
     *
     * "Array instead of linked list"
     *      → direct indexing from length usually dominates
     */

    // =================================================================================
    // INTERVIEW ARTICULATION
    // =================================================================================
    /*
     * "The two-pass O(1)-space solution counts length first.
     *  To remove that explicit length pass, I encode distance from the end as a fixed
     *  gap between two pointers. Because deletion needs the predecessor, I keep fast
     *  n + 1 edges ahead. Dummy only handles the head-deletion boundary case uniformly."
     */

    // =================================================================================
    // 30-SECOND RECALL CARD
    // =================================================================================
    /*
     * nth from END + singly linked list -> FIXED GAP
     *
     * target      = n edges from null
     * predecessor = n + 1 edges from null
     * dummy       = predecessor for original head
     *
     * slow = fast = dummy
     * move fast n + 1
     * move both until fast == null
     * delete slow.next
     *
     * Time O(n), Space O(1)
     */

    // =================================================================================
    // SELF-VERIFYING TESTS
    // =================================================================================

    public static void main(String[] args) {

        testAllPrimaryApproaches();

        System.out.println("All RemoveNthFromEndV4 tests passed ✔");
    }

    private static void testAllPrimaryApproaches() {

        assertListEquals(
                new int[]{1, 2, 3, 5},
                OptimalFixedGapSolution.removeNthFromEnd(
                        listOf(1, 2, 3, 4, 5), 2));

        assertListEquals(
                new int[]{2, 3},
                OptimalFixedGapSolution.removeNthFromEnd(
                        listOf(1, 2, 3), 3));

        assertListEquals(
                new int[]{1, 2},
                OptimalFixedGapSolution.removeNthFromEnd(
                        listOf(1, 2, 3), 1));

        assertListEquals(
                new int[]{},
                OptimalFixedGapSolution.removeNthFromEnd(
                        listOf(1), 1));

        assertListEquals(
                new int[]{1, 2, 3, 5},
                StoreNodesSolution.removeNthFromEnd(
                        listOf(1, 2, 3, 4, 5), 2));

        assertListEquals(
                new int[]{1, 2, 3, 5},
                TwoPassLengthSolution.removeNthFromEnd(
                        listOf(1, 2, 3, 4, 5), 2));

        assertListEquals(
                new int[]{1, 2, 3, 5},
                RecursiveDistanceSolution.removeNthFromEnd(
                        listOf(1, 2, 3, 4, 5), 2));
    }

    private static ListNode listOf(int... values) {

        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        for (int value : values) {
            tail.next = new ListNode(value);
            tail = tail.next;
        }

        return dummy.next;
    }

    private static void assertListEquals(int[] expected, ListNode actual) {

        int index = 0;
        ListNode current = actual;

        while (current != null && index < expected.length) {
            assert current.val == expected[index]
                    : "Expected " + expected[index] + " but found " + current.val;

            current = current.next;
            index++;
        }

        assert index == expected.length
                : "List ended before all expected values were found";

        assert current == null
                : "Actual list contains extra nodes";
    }
}
