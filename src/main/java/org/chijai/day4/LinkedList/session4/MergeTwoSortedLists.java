package org.chijai.day4.LinkedList.session4;

import java.util.Arrays;

/**
 * Merge Two Sorted Lists
 *
 * Core retrieval:
 * 2 sorted frontiers -> compare directly -> advance one pointer.
 *
 * Preferred interview solution:
 * Iterative two-pointer merge.
 */
public class MergeTwoSortedLists {

    static final class ListNode {
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

    // ============================================================
    // PREFERRED INTERVIEW SOLUTION
    // ============================================================

    static final class Solution {

        ListNode mergeTwoLists(ListNode first, ListNode second) {
            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;

            while (first != null && second != null) {
                if (first.val <= second.val) {
                    tail.next = first;
                    first = first.next;
                } else {
                    tail.next = second;
                    second = second.next;
                }

                tail = tail.next;
            }

            tail.next = first != null ? first : second;

            return dummy.next;
        }
    }

    /*
     * WHY 1 — Why can we compare only the two current nodes?
     *
     * Each list is already sorted.
     *
     * first is the smallest remaining node in list 1.
     * second is the smallest remaining node in list 2.
     *
     * Therefore min(first.val, second.val)
     * must be the globally smallest remaining node.
     */

    /*
     * WHY 2 — Why advance only the chosen list?
     *
     * The chosen node is now permanently placed in the answer.
     *
     * The other frontier is still unprocessed, so it must stay.
     */

    /*
     * WHY 3 — Why can we append the remainder directly?
     *
     * Once one list ends, every remaining node belongs to the other
     * already-sorted list.
     *
     * No competing frontier remains.
     */

    /*
     * WHY 4 — Why use a dummy node?
     *
     * It removes the special case for creating the first output node.
     *
     * tail always means:
     * "last node already placed in the merged answer."
     */

    /*
     * ============================================================
     * 30-SECOND RECALL
     * ============================================================
     *
     * Trigger:
     * Two sorted inputs -> one sorted output.
     *
     * Pattern:
     * Two sorted frontiers.
     *
     * Invariant:
     * The merged prefix is final and sorted.
     *
     * Move:
     * Compare front nodes.
     * Attach smaller.
     * Advance only that source.
     *
     * Finish:
     * Append the non-empty remainder.
     *
     * Complexity:
     * Time  O(m + n)
     * Space O(1)
     *
     * Re-derive:
     * "Merge Sort's merge step."
     */

    /*
     * ============================================================
     * REUSABLE MASTER TEMPLATE — TWO SORTED STREAMS
     * ============================================================
     *
     * dummy
     * tail = dummy
     *
     * while both streams have values
     *     choose smaller frontier
     *     append it
     *     advance chosen stream
     *     advance tail
     *
     * append remaining stream
     * return dummy.next
     */

    // ============================================================
    // ALTERNATIVE — RECURSIVE
    // ============================================================

    static final class RecursiveSolution {

        ListNode mergeTwoLists(ListNode first, ListNode second) {
            if (first == null) {
                return second;
            }

            if (second == null) {
                return first;
            }

            if (first.val <= second.val) {
                first.next = mergeTwoLists(first.next, second);
                return first;
            }

            second.next = mergeTwoLists(first, second.next);
            return second;
        }
    }

    /*
     * Recursive tradeoff:
     *
     * Time:
     * O(m + n)
     *
     * Extra space:
     * O(m + n) recursion stack in the worst case.
     *
     * Good for elegance.
     * Iterative is usually the safer interview default.
     */

    /*
     * ============================================================
     * INTERVIEW ARTICULATION
     * ============================================================
     *
     * "Both lists are individually sorted, so each current head is the
     * smallest remaining value in its own list. Therefore the smaller
     * of the two heads must be the globally smallest remaining node.
     *
     * I append that node, advance only the list it came from, and keep
     * the merged prefix final and sorted. When one list is exhausted,
     * I append the other list because it is already sorted.
     *
     * Every node is processed once, so time is O(m+n), and the iterative
     * version uses O(1) auxiliary space."
     */

    /*
     * ============================================================
     * RELATED / REINFORCEMENT PROBLEMS
     * ============================================================
     *
     * 1. Merge Sorted Array
     *    Same ordered-frontier idea, different storage.
     *
     * 2. Merge K Sorted Lists
     *    Same frontier principle, but now there are K candidates.
     *    Direct comparison no longer scales; use a min-heap or balanced
     *    pairwise merging.
     *
     * 3. Merge Sort
     *    This exact operation is the merge primitive.
     *
     * 4. External / streaming merge
     *    Same rule: expose only the current frontier of each sorted stream.
     */

    /*
     * ============================================================
     * PATTERN BOUNDARY
     * ============================================================
     *
     * This logic depends completely on each input being sorted.
     *
     * If inputs are unsorted, the current front node is no longer guaranteed
     * to be that stream's smallest remaining node, so local comparison is not
     * enough to prove global correctness.
     */

    private static ListNode build(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        int index = 0;
        while (index < values.length) {
            tail.next = new ListNode(values[index]);
            tail = tail.next;
            index++;
        }

        return dummy.next;
    }

    private static int[] toArray(ListNode head) {
        int length = 0;
        ListNode current = head;

        while (current != null) {
            length++;
            current = current.next;
        }

        int[] result = new int[length];
        int index = 0;
        current = head;

        while (current != null) {
            result[index++] = current.val;
            current = current.next;
        }

        return result;
    }

    private static void assertListEquals(int[] expected, ListNode actual) {
        int[] actualArray = toArray(actual);

        if (!Arrays.equals(expected, actualArray)) {
            throw new AssertionError(
                    "Expected: " + Arrays.toString(expected)
                            + "\nActual:   " + Arrays.toString(actualArray)
            );
        }
    }

    public static void main(String[] args) {
        Solution solver = new Solution();

        assertListEquals(
                new int[]{1, 1, 2, 3, 4, 4},
                solver.mergeTwoLists(
                        build(1, 2, 4),
                        build(1, 3, 4)
                )
        );

        assertListEquals(
                new int[]{0},
                solver.mergeTwoLists(
                        null,
                        build(0)
                )
        );

        assertListEquals(
                new int[]{1, 2, 3, 4, 5, 6},
                solver.mergeTwoLists(
                        build(1, 3, 5),
                        build(2, 4, 6)
                )
        );

        assertListEquals(
                new int[]{-5, -4, -2, 0, 3},
                solver.mergeTwoLists(
                        build(-5, -2, 3),
                        build(-4, 0)
                )
        );

        assertListEquals(
                new int[]{1, 2, 3, 10, 11},
                solver.mergeTwoLists(
                        build(1, 2, 3),
                        build(10, 11)
                )
        );

        System.out.println("MergeTwoSortedLists: all assertions passed.");
    }
}
