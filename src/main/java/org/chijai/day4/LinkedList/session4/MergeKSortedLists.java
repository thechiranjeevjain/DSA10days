package org.chijai.day4.LinkedList.session4;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Merge K Sorted Lists
 *
 * Core retrieval:
 * K sorted frontiers -> repeatedly choose global minimum -> min-heap.
 *
 * Preferred interview solution:
 * K-way merge using a min-heap.
 */
public class MergeKSortedLists {

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
    // PREFERRED INTERVIEW SOLUTION — MIN HEAP
    // ============================================================

    static final class Solution {

        ListNode mergeKLists(ListNode[] lists) {
            if (lists == null || lists.length == 0) {
                return null;
            }

            PriorityQueue<ListNode> minHeap =
                    new PriorityQueue<>(Comparator.comparingInt(node -> node.val));

            int index = 0;
            // Heap stores only ONE frontier node per unfinished list,
            // not every node from every list.
            while (index < lists.length) {
                if (lists[index] != null) {
                    minHeap.offer(lists[index]);
                }
                index++;
            }

            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;

            while (!minHeap.isEmpty()) {
                ListNode node = minHeap.poll();

                tail.next = node;
                tail = tail.next;

                // We just consumed this list's frontier.
                // Its successor now becomes that list's new frontier.
                if (node.next != null) {
                    minHeap.offer(node.next);
                }
            }

            return dummy.next;
        }
    }

    /*
     * WHY 1 — Why does the heap contain only one node per list?
     *
     * Each list is sorted.
     *
     * Its current head is the smallest remaining value from that list.
     * Nothing behind that head can become relevant before the head is used.
     *
     * So one frontier node per unfinished list is sufficient.
     */

    /*
     * WHY 2 — Why is heap.poll() globally correct?
     *
     * The heap contains the smallest remaining candidate from every list.
     *
     * Therefore the minimum among those K frontier nodes is the smallest
     * remaining node across all lists.
     */

    /*
     * WHY 3 — Why insert only node.next?
     *
     * After removing one frontier, only that same list needs a replacement.
     *
     * Its immediate successor is now the smallest remaining node from that
     * list, so inserting node.next restores the frontier invariant.
     */

    /*
     * WHY 4 — Why heap instead of scanning all K heads each time?
     *
     * Scanning K frontiers for every output node:
     *
     * O(NK)
     *
     * Heap selection:
     *
     * O(log K) per node
     * -> O(N log K)
     */

    /*
     * ============================================================
     * 30-SECOND RECALL
     * ============================================================
     *
     * Trigger:
     * Many individually sorted sequences -> one sorted output.
     *
     * Pattern:
     * K-way merge.
     *
     * Invariant:
     * Heap stores one frontier node from every unfinished list.
     *
     * Move:
     * poll minimum
     * append it
     * push only its successor
     *
     * Complexity:
     * N = total nodes
     * K = number of lists
     *
     * Time  O(N log K)
     * Space O(K)
     *
     * Re-derive:
     * "K conveyor belts exposing only their front package."
     */

    /*
     * ============================================================
     * REUSABLE MASTER TEMPLATE — K SORTED STREAMS
     * ============================================================
     *
     * initialize minHeap with one frontier per non-empty stream
     *
     * while heap not empty
     *     smallest = poll
     *     output smallest
     *
     *     if smallest's stream has another item
     *         push that next frontier
     *
     * return output
     */

    // ============================================================
    // IMPORTANT ALTERNATIVE — BALANCED PAIRWISE MERGE
    // ============================================================

    static final class DivideAndConquerSolution {

        ListNode mergeKLists(ListNode[] lists) {
            if (lists == null || lists.length == 0) {
                return null;
            }

            int interval = 1;

            while (interval < lists.length) {
                int index = 0;

                while (index + interval < lists.length) {
                    lists[index] =
                            mergeTwoLists(lists[index], lists[index + interval]);

                    index += interval * 2;
                }

                interval *= 2;
            }

            return lists[0];
        }

        private ListNode mergeTwoLists(ListNode first, ListNode second) {
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
     * Why keep this alternative?
     *
     * It teaches a second route from Merge Two -> Merge K:
     *
     * merge pairs
     * -> merge the merged pairs
     * -> repeat in balanced rounds
     *
     * Each node participates in O(log K) merge levels.
     *
     * Time:
     * O(N log K)
     *
     * Auxiliary space:
     * O(1) for this iterative implementation,
     * ignoring the input array itself.
     *
     * Strong follow-up when interviewer asks:
     * "Can you solve it without a heap?"
     */

    /*
     * ============================================================
     * APPROACH LADDER
     * ============================================================
     *
     * 1. Collect all values + sort
     *    O(N log N), O(N)
     *    Ignores the sorted structure.
     *
     * 2. Repeatedly scan K current heads
     *    O(NK)
     *    Uses the frontier idea, but selection is expensive.
     *
     * 3. Min-heap over K frontiers
     *    O(N log K), O(K)
     *    Preferred general K-way merge.
     *
     * 4. Balanced pairwise merge
     *    O(N log K)
     *    Reuses Merge Two as a primitive.
     */

    /*
     * ============================================================
     * MERGE FAMILY — PATTERN EVOLUTION
     * ============================================================
     *
     * 2 sorted streams
     *     -> 2 candidates
     *     -> direct comparison
     *     -> TWO POINTERS
     *     -> O(N)
     *
     * K sorted streams
     *     -> K candidates
     *     -> repeated minimum selection
     *     -> MIN HEAP
     *     -> O(N log K)
     *
     * Alternative:
     *
     * K sorted streams
     *     -> repeatedly merge pairs
     *     -> DIVIDE & CONQUER
     *     -> O(N log K)
     */

    /*
     * ============================================================
     * INTERVIEW ARTICULATION
     * ============================================================
     *
     * "Each list is already sorted, so I only need its smallest unprocessed
     * node as a candidate. I keep exactly one such frontier node from every
     * unfinished list in a min-heap.
     *
     * The heap minimum is therefore the globally smallest remaining node.
     * I append it, and then only that list can expose a new candidate, so I
     * insert its successor.
     *
     * Every node enters and leaves the heap once. The heap has at most K
     * elements, so the total time is O(N log K) and extra space is O(K)."
     */

    /*
     * ============================================================
     * RELATED / REINFORCEMENT PROBLEMS
     * ============================================================
     *
     * 1. Merge Two Sorted Lists
     *    Foundation. With only two frontiers, direct comparison is enough.
     *
     * 2. Kth Smallest Element in a Sorted Matrix
     *    Similar "multiple sorted frontiers" viewpoint.
     *
     * 3. Smallest Range Covering Elements from K Lists
     *    Same K-stream frontier machinery, but the objective changes.
     *
     * 4. Merge Sorted Arrays / Iterators
     *    Same K-way merge pattern with different state representation.
     *
     * 5. External Merge Sort
     *    Same heap frontier idea for sorted files / runs.
     */

    /*
     * ============================================================
     * PATTERN BOUNDARY
     * ============================================================
     *
     * The heap solution is powerful because every source is individually
     * sorted.
     *
     * If a source is unsorted, its current frontier is not guaranteed to be
     * that source's smallest remaining value, so the invariant collapses.
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

    private static void assertArrayEquals(int[] expected, int[] actual) {
        if (expected.length != actual.length) {
            throw new AssertionError("Length mismatch.");
        }

        int index = 0;
        while (index < expected.length) {
            if (expected[index] != actual[index]) {
                throw new AssertionError(
                        "Mismatch at index " + index
                                + ": expected " + expected[index]
                                + ", actual " + actual[index]
                );
            }
            index++;
        }
    }

    public static void main(String[] args) {
        Solution solver = new Solution();

        ListNode[] case1 = {
                build(1, 4, 5),
                build(1, 3, 4),
                build(2, 6)
        };

        assertArrayEquals(
                new int[]{1, 1, 2, 3, 4, 4, 5, 6},
                toArray(solver.mergeKLists(case1))
        );

        ListNode[] case2 = {};
        assert solver.mergeKLists(case2) == null;

        ListNode[] case3 = {
                null,
                build(2),
                null,
                build(1, 5)
        };

        assertArrayEquals(
                new int[]{1, 2, 5},
                toArray(solver.mergeKLists(case3))
        );

        ListNode[] case4 = {
                build(-10, -2, 8),
                build(-9, -1),
                build(0, 3)
        };

        assertArrayEquals(
                new int[]{-10, -9, -2, -1, 0, 3, 8},
                toArray(solver.mergeKLists(case4))
        );

        System.out.println("MergeKSortedLists: all assertions passed.");
    }
}
