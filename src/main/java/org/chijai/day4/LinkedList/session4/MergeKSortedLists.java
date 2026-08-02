package org.chijai.day4.LinkedList.session4;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MergeKSortedLists {

    /*
     * ============================================================
     * 2. 📘 PRIMARY PROBLEM
     * ============================================================
     *
     * Title:
     * Merge K Sorted Lists
     *
     * Difficulty:
     * Hard
     *
     * Tags:
     * Linked List
     * Heap (Priority Queue)
     * Divide and Conquer
     * Merge
     *
     * LeetCode:
     * https://leetcode.com/problems/merge-k-sorted-lists/
     *
     * ------------------------------------------------------------
     * Problem Description
     * ------------------------------------------------------------
     *
     * You are given an array of k singly linked lists.
     *
     * Every linked list is already sorted in ascending order.
     *
     * Merge every list into one globally sorted linked list.
     *
     * Return the head of the merged list.
     *
     * ------------------------------------------------------------
     * Constraints
     * ------------------------------------------------------------
     *
     * 0 <= k <= 10^4
     *
     * 0 <= lists[i].length <= 500
     *
     * -10^4 <= node.val <= 10^4
     *
     * Every list is individually sorted.
     *
     * Sum of all node counts <= 10^4
     *
     * ------------------------------------------------------------
     * Representative Examples
     * ------------------------------------------------------------
     *
     * Example 1
     *
     * Input:
     * [[1,4,5],
     *  [1,3,4],
     *  [2,6]]
     *
     * Output:
     * 1->1->2->3->4->4->5->6
     *
     * ------------------------------------------------------------
     *
     * Example 2
     *
     * Input:
     * []
     *
     * Output:
     * []
     *
     * ------------------------------------------------------------
     *
     * Example 3
     *
     * Input:
     * [[]]
     *
     * Output:
     * []
     *
     * ------------------------------------------------------------
     *
     * Goal
     *
     * Merge all lists while preserving sorted order.
     *
     * We seek the optimal
     *
     * Time:
     * O(N log K)
     *
     * Space:
     * O(K)
     *
     * where
     *
     * N = total nodes
     * K = number of lists.
     */

    /*
     * ============================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern
     * -------
     * K-Way Merge using a Min Heap.
     *
     * Archetype
     * ---------
     * Repeatedly extract the globally smallest candidate from
     * multiple already-sorted streams.
     *
     * Core Invariant
     * --------------
     * The heap always stores exactly one candidate from every
     * unfinished list:
     *
     * namely the smallest not-yet-output node of that list.
     *
     * Therefore:
     *
     * Heap minimum
     * ==
     * Global minimum among all remaining nodes.
     *
     * Why It Works
     * ------------
     * Every list is sorted.
     *
     * If the current smallest element of a list is not chosen,
     * then nothing later in that same list can be smaller.
     *
     * Thus one frontier node per list is sufficient.
     *
     * Recognition Signals
     * -------------------
     * Look for:
     *
     * • many sorted sequences
     * • need globally sorted output
     * • simultaneous traversal
     * • repeatedly choosing smallest
     *
     * Use When
     * --------
     * Multiple sorted linked lists.
     *
     * Multiple sorted iterators.
     *
     * External merge.
     *
     * Streaming merge.
     *
     * Log processing.
     *
     * Database merge.
     *
     * When NOT To Use
     * ---------------
     * Lists are unsorted.
     *
     * Need random access.
     *
     * Need arbitrary deletions.
     *
     * Comparison With Similar Patterns
     * --------------------------------
     *
     * Merge Two Sorted Lists
     *
     * Two frontiers.
     *
     * O(N)
     *
     * -----------------------
     *
     * Merge K Sorted Lists
     *
     * K frontiers.
     *
     * Heap decides smallest.
     *
     * O(N log K)
     *
     * -----------------------
     *
     * Divide & Conquer Merge
     *
     * Pairwise merge repeatedly.
     *
     * Also O(N log K).
     *
     * Better when recursion or iterative interval merging is
     * preferred.
     */

    /*
     * ============================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * Mental Model
     * ------------
     *
     * Imagine K conveyor belts.
     *
     * Each conveyor belt exposes only its front package.
     *
     * The heap contains only those exposed packages.
     *
     * Whenever the smallest package leaves,
     * that conveyor exposes exactly one new package.
     *
     * Repeat.
     *
     * Eventually every package leaves exactly once.
     *
     * ------------------------------------------------------------
     * Variables
     * ------------------------------------------------------------
     *
     * heap
     *
     * Current frontier across all unfinished lists.
     *
     * tail
     *
     * Last node already appended into answer.
     *
     * dummy
     *
     * Stable anchor for constructing answer.
     *
     * node
     *
     * Current globally smallest remaining node.
     *
     * ------------------------------------------------------------
     * Primary Invariant
     * ------------------------------------------------------------
     *
     * Every unfinished list contributes exactly one node
     * to the heap.
     *
     * That node is the smallest remaining node from that list.
     *
     * ------------------------------------------------------------
     * Consequence
     * ------------------------------------------------------------
     *
     * Heap.peek()
     *
     * is guaranteed to be the globally smallest remaining node.
     *
     * ------------------------------------------------------------
     * Output Invariant
     * ------------------------------------------------------------
     *
     * Everything before tail
     * is already globally sorted
     * and final.
     *
     * No future operation can invalidate it.
     *
     * ------------------------------------------------------------
     * Progress Invariant
     * ------------------------------------------------------------
     *
     * Each loop permanently outputs exactly one node.
     *
     * One successor may enter the heap.
     *
     * Heap size never exceeds K.
     *
     * ------------------------------------------------------------
     * Allowed Moves
     * ------------------------------------------------------------
     *
     * Remove heap minimum.
     *
     * Append it.
     *
     * Insert its next node if one exists.
     *
     * ------------------------------------------------------------
     * Forbidden Moves
     * ------------------------------------------------------------
     *
     * Never insert every node initially.
     *
     * That destroys the frontier invariant and increases memory
     * to O(N).
     *
     * Never skip inserting node.next.
     *
     * Otherwise that list disappears forever.
     *
     * Never insert arbitrary later nodes.
     *
     * Only immediate successor preserves sorted exposure.
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * Heap empty.
     *
     * Meaning:
     *
     * Every list exhausted.
     *
     * Every node processed exactly once.
     *
     * ------------------------------------------------------------
     * Why Naive Solutions Fail
     * ------------------------------------------------------------
     *
     * Collect all values.
     *
     * Heap them.
     *
     * Rebuild list.
     *
     * Time:
     * O(N log N)
     *
     * Space:
     * O(N)
     *
     * Ignores existing sorted structure.
     */

    /*
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * Mistake 1
     * ---------
     * Insert every node into heap.
     *
     * Looks reasonable because heap sorts everything.
     *
     * Violated Invariant:
     *
     * Heap should contain only frontier nodes.
     *
     * Counterexample:
     *
     * 1->100
     * 2->3
     *
     * We never need 100 until 1 is removed.
     *
     * ------------------------------------------------------------
     *
     * Mistake 2
     * ---------
     * Forget to push node.next.
     *
     * Result:
     *
     * Remaining suffix disappears.
     *
     * Counterexample:
     *
     * 1->2->3
     *
     * Output becomes only:
     *
     * 1
     *
     * ------------------------------------------------------------
     *
     * Mistake 3
     * ---------
     * Poll heap before checking empty.
     *
     * NullPointerException.
     *
     * ------------------------------------------------------------
     *
     * Mistake 4
     * ---------
     * Build new nodes unnecessarily.
     *
     * Correct but wastes memory.
     *
     * Existing nodes can simply be relinked.
     *
     * ------------------------------------------------------------
     *
     * Mistake 5
     * ---------
     * Forget advancing tail.
     *
     * Entire answer collapses into one repeated next pointer.
     *
     * ------------------------------------------------------------
     *
     * Interview Trap
     * --------------
     *
     * Candidate says:
     *
     * "Heap stores every remaining node."
     *
     * Correct statement:
     *
     * Heap stores only the smallest remaining node
     * from every unfinished list.
     */

    /*
     * ============================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Typing Order
     * ------------
     *
     * 1. Handle empty array.
     *
     * 2. Create min heap ordered by node value.
     *
     * 3. Insert every non-null head.
     *
     * 4. Create dummy.
     *
     * 5. tail = dummy.
     *
     * 6. While heap not empty
     *
     *      poll smallest
     *
     *      append node
     *
     *      move tail
     *
     *      if successor exists
     *          push successor
     *
     * 7. Return dummy.next.
     *
     * ------------------------------------------------------------
     * Skeleton
     * ------------------------------------------------------------
     *
     * create heap
     *
     * initialize frontier
     *
     * while heap not empty
     *
     *      smallest = poll
     *
     *      append
     *
     *      expose successor
     *
     * return answer
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * push all heads
     *
     * while heap not empty
     *
     *      smallest = pop
     *
     *      append
     *
     *      push successor
     *
     * return answer
     */

    static class ListNode {

        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static class BruteForce {

/*
 * Idea
 * ----
 * Copy every value.
 *
 * Sort.
 *
 * Build an entirely new linked list.
 *
 * Invariant
 * ---------
 * Values become sorted only after the global sort.
 *
 * Limitation
 * ----------
 * Completely ignores that every input list is already sorted.
 *
 * Complexity
 * ----------
 * Time:
 * O(N log N)
 *
 * Space:
 * O(N)
 *
 * Interview Usefulness
 * --------------------
 * Acceptable baseline only.
 */


        ListNode mergeKLists(ListNode[] lists) {

            if (lists == null || lists.length == 0) {
                return null;
            }

            PriorityQueue<Integer> values = new PriorityQueue<>();

            for (ListNode head : lists) {
                while (head != null) {
                    values.offer(head.val);
                    head = head.next;
                }
            }

            ListNode dummy = new ListNode(-1);
            ListNode tail = dummy;

            while (!values.isEmpty()) {
                tail.next = new ListNode(values.poll());
                tail = tail.next;
            }

            return dummy.next;
        }
    }

    static class Improved {

        /*
         * Idea
         * ----
         * Merge lists pairwise.
         *
         * Instead of globally sorting all values,
         * repeatedly merge two already-sorted lists.
         *
         * This mirrors merge sort.
         *
         * --------------------------------------------------------
         * Pattern
         * --------------------------------------------------------
         *
         * Divide and Conquer
         *
         * Every round approximately halves
         * the number of remaining lists.
         *
         * --------------------------------------------------------
         * Invariant
         * --------------------------------------------------------
         *
         * Every intermediate list produced is completely sorted.
         *
         * Therefore every future merge receives two sorted inputs.
         *
         * --------------------------------------------------------
         * Improvement
         * --------------------------------------------------------
         *
         * Reuses existing sorted structure.
         *
         * Never performs a global O(N log N) sort.
         *
         * --------------------------------------------------------
         * Complexity
         * --------------------------------------------------------
         *
         * Time
         *
         * O(N log K)
         *
         * Space
         *
         * O(1)
         *
         * (Ignoring recursion stack because implementation below
         * is iterative.)
         *
         * --------------------------------------------------------
         * Interview Usefulness
         * --------------------------------------------------------
         *
         * Excellent alternative when interviewer asks:
         *
         * "Can you solve it without a heap?"
         */

        ListNode mergeKLists(ListNode[] lists) {

            if (lists == null || lists.length == 0) {
                return null;
            }

            int interval = 1;

            while (interval < lists.length) {

                for (int i = 0; i + interval < lists.length; i += interval * 2) {

                    lists[i] = mergeTwoLists(lists[i], lists[i + interval]);

                }

                interval *= 2;
            }

            return lists[0];
        }

        private ListNode mergeTwoLists(ListNode first, ListNode second) {

            ListNode dummy = new ListNode(-1);
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

            tail.next = (first != null) ? first : second;

            return dummy.next;
        }
    }

    static class Optimal {

        /*
         * Idea
         * ----
         * Maintain exactly one frontier node
         * from every unfinished list.
         *
         * A min heap always identifies
         * the globally smallest remaining node.
         *
         * --------------------------------------------------------
         * Pattern
         * --------------------------------------------------------
         *
         * K-Way Merge
         *
         * --------------------------------------------------------
         * Invariant
         * --------------------------------------------------------
         *
         * Heap contains only frontier nodes.
         *
         * One unfinished list
         * contributes exactly one node.
         *
         * Therefore:
         *
         * heap.poll()
         *
         * is always the next answer node.
         *
         * --------------------------------------------------------
         * Correctness
         * --------------------------------------------------------
         *
         * After removing a frontier node,
         * only its successor can become the new frontier
         * for that list.
         *
         * Thus inserting node.next
         * restores the invariant immediately.
         *
         * --------------------------------------------------------
         * Complexity
         * --------------------------------------------------------
         *
         * Time
         *
         * O(N log K)
         *
         * Space
         *
         * O(K)
         *
         * --------------------------------------------------------
         * Interview Usefulness
         * --------------------------------------------------------
         *
         * Preferred solution.
         *
         * Demonstrates understanding of:
         *
         * • heaps
         * • linked lists
         * • frontier invariants
         * • streaming merge
         */

        ListNode mergeKLists(ListNode[] lists) {

            // Invariant:
            // No input means no frontier exists.
            if (lists == null || lists.length == 0) {
                return null;
            }

            PriorityQueue<ListNode> minHeap =
                    new PriorityQueue<>(Comparator.comparingInt(node -> node.val));

            // Invariant:
            // Heap begins with exactly one frontier node
            // from every non-empty list.
            for (ListNode head : lists) {
                if (head != null) {
                    minHeap.offer(head);
                }
            }

            ListNode dummy = new ListNode(-1);
            ListNode tail = dummy;

            while (!minHeap.isEmpty()) {

                // Invariant:
                // This is globally smallest remaining node.
                ListNode node = minHeap.poll();

                tail.next = node;

                // Invariant:
                // Output prefix is now finalized forever.
                tail = tail.next;

                if (node.next != null) {

                    // Restore frontier for this list.
                    minHeap.offer(node.next);
                }
            }

            // Defensive termination.
            // Prevent accidental stale links during debugging.
            tail.next = null;

            return dummy.next;
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Pattern
 * -------
 * K-Way Merge using a Min Heap.
 *
 * Invariant
 * ---------
 * The heap stores exactly one frontier node
 * from every unfinished linked list.
 *
 * Because each list is already sorted,
 * that frontier node is the smallest remaining node
 * from its own list.
 *
 * Therefore the heap minimum is also
 * the global minimum.
 *
 * Discard Rule
 * ------------
 * Once the smallest frontier node is removed,
 * it is permanently placed into the answer.
 *
 * Nothing remaining in any list
 * can precede it.
 *
 * Correctness
 * -----------
 * Appending the heap minimum preserves
 * global sorted order.
 *
 * Inserting only its successor
 * immediately restores the frontier invariant.
 *
 * Repeat until every frontier disappears.
 *
 * Termination
 * -----------
 * Every iteration permanently outputs one node.
 *
 * Every node enters and leaves the heap once.
 *
 * Eventually the heap becomes empty.
 *
 * In-place Feasibility
 * --------------------
 * Yes.
 *
 * Existing nodes are relinked.
 *
 * No new list nodes are required.
 *
 * Streaming Feasibility
 * --------------------
 * Excellent.
 *
 * The algorithm never requires loading
 * every value into memory simultaneously.
 *
 * It consumes one node at a time.
 *
 * When NOT To Use
 * ---------------
 * Input lists are unsorted.
 *
 * Ordering depends upon information
 * unavailable at the current frontier.
 */

    /*
     * ============================================================
     * 🎯 INTERVIEW RECALL SHEET
     * ============================================================
     *
     * Trigger
     * -------
     * Multiple individually sorted sequences.
     *
     * Need one globally sorted sequence.
     *
     * ------------------------------------------------------------
     * Pattern
     * ------------------------------------------------------------
     *
     * K-Way Merge using a Min Heap.
     *
     * ------------------------------------------------------------
     * Search Target
     * ------------------------------------------------------------
     *
     * Always locate the globally smallest remaining node.
     *
     * ------------------------------------------------------------
     * Invariant
     * ------------------------------------------------------------
     *
     * Heap stores exactly one frontier node
     * from every unfinished list.
     *
     * ------------------------------------------------------------
     * Discard Rule
     * ------------------------------------------------------------
     *
     * Remove heap minimum.
     *
     * Append permanently.
     *
     * Push only its successor.
     *
     * ------------------------------------------------------------
     * Common Trap
     * ------------------------------------------------------------
     *
     * Putting every node into the heap.
     *
     * That increases memory to O(N)
     * and destroys the frontier viewpoint.
     *
     * ------------------------------------------------------------
     * Edge Cases
     * ------------------------------------------------------------
     *
     * Empty array.
     *
     * Array containing only null heads.
     *
     * Single list.
     *
     * Duplicate values.
     *
     * Negative values.
     *
     * One extremely long list.
     *
     * Large K with many empty lists.
     *
     * ------------------------------------------------------------
     * One-Liner
     * ------------------------------------------------------------
     *
     * "Keep one candidate per list.
     * Always output the smallest candidate.
     * Replace it with its successor."
     *
     * ------------------------------------------------------------
     * Re-Derivation Cue
     * ------------------------------------------------------------
     *
     * Think:
     *
     * "K conveyor belts exposing only
     * their front package."
     */

    /*
     * ============================================================
     * 🔄 VARIATIONS & TWEAKS
     * ============================================================
     *
     * Variation 1
     * -----------
     * Merge Sorted Arrays.
     *
     * Same invariant.
     *
     * Heap stores
     *
     * (value, arrayIndex, elementIndex)
     *
     * instead of ListNode.
     *
     * ------------------------------------------------------------
     *
     * Variation 2
     * -----------
     * Merge Infinite Streams.
     *
     * Still valid.
     *
     * Frontier never requires
     * more than one item per stream.
     *
     * ------------------------------------------------------------
     *
     * Variation 3
     * -----------
     * External Merge Sort.
     *
     * Identical pattern.
     *
     * Lists become disk files.
     *
     * Heap tracks one record
     * from each file.
     *
     * ------------------------------------------------------------
     *
     * Variation 4
     * -----------
     * Pairwise Divide & Conquer.
     *
     * Heap disappears.
     *
     * Invariant changes:
     *
     * Every intermediate merge
     * remains sorted.
     *
     * Complexity remains
     *
     * O(N log K).
     *
     * ------------------------------------------------------------
     *
     * Variation 5
     * -----------
     * Merge Two Lists.
     *
     * Heap unnecessary.
     *
     * Two pointers already expose
     * the only possible frontier.
     *
     * ------------------------------------------------------------
     *
     * Pattern Boundary
     * ----------------
     *
     * If inputs are not individually sorted,
     * frontier reasoning collapses.
     *
     * The heap no longer guarantees
     * the global minimum.
     */

    /*
     * ============================================================
     * 🧠 MASTERY CHECKLIST
     * ============================================================
     *
     * □ I know the invariant.
     *
     * Heap contains one frontier node
     * from every unfinished list.
     *
     * ------------------------------------------------------------
     *
     * □ I know the search target.
     *
     * Global smallest remaining node.
     *
     * ------------------------------------------------------------
     *
     * □ I know the discard rule.
     *
     * Poll.
     *
     * Append.
     *
     * Push successor.
     *
     * ------------------------------------------------------------
     *
     * □ I know termination.
     *
     * Heap becomes empty.
     *
     * ------------------------------------------------------------
     *
     * □ I know why the naive solution fails.
     *
     * It ignores already-sorted structure.
     *
     * ------------------------------------------------------------
     *
     * □ I know debugging checkpoints.
     *
     * Heap initially contains
     * exactly one head
     * per non-empty list.
     *
     * Heap size never exceeds K.
     *
     * Every poll produces
     * non-decreasing values.
     *
     * Every poll removes one node forever.
     *
     * Every successor is inserted
     * exactly once.
     *
     * Tail always points
     * to the final node
     * of constructed answer.
     *
     * ------------------------------------------------------------
     *
     * □ I know important edge cases.
     *
     * Empty array.
     *
     * Null heads.
     *
     * Single node.
     *
     * Duplicate values.
     *
     * Negative values.
     *
     * ------------------------------------------------------------
     *
     * □ I know variant readiness.
     *
     * Arrays.
     *
     * Streams.
     *
     * External merge.
     *
     * Divide & Conquer.
     *
     * ------------------------------------------------------------
     *
     * □ I understand the pattern boundary.
     *
     * Without individually sorted inputs,
     * the frontier invariant is invalid.
     */

    private static ListNode build(int... values) {

        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;

        for (int value : values) {
            tail.next = new ListNode(value);
            tail = tail.next;
        }

        return dummy.next;
    }

    private static int[] toArray(ListNode head) {

        int length = 0;

        for (ListNode current = head; current != null; current = current.next) {
            length++;
        }

        int[] result = new int[length];

        int index = 0;

        for (ListNode current = head; current != null; current = current.next) {
            result[index++] = current.val;
        }

        return result;
    }

    private static boolean equals(int[] expected, int[] actual) {

        if (expected.length != actual.length) {
            return false;
        }

        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        // Happy path:
        // Standard LeetCode example with three sorted lists.
        ListNode[] case1 = {
                build(1, 4, 5),
                build(1, 3, 4),
                build(2, 6)
        };

        assert equals(
                new int[]{1, 1, 2, 3, 4, 4, 5, 6},
                toArray(solver.mergeKLists(case1))
        );

        // Edge case:
        // Empty array of lists.
        ListNode[] case2 = {};

        assert solver.mergeKLists(case2) == null;

        // Edge case:
        // Single null list.
        ListNode[] case3 = {
                null
        };

        assert solver.mergeKLists(case3) == null;

        // Boundary:
        // Only one already-sorted list.
        ListNode[] case4 = {
                build(1, 2, 3, 4)
        };

        assert equals(
                new int[]{1, 2, 3, 4},
                toArray(solver.mergeKLists(case4))
        );

        // Interview trap:
        // Duplicate values across different lists.
        ListNode[] case5 = {
                build(1, 1),
                build(1),
                build(1, 1, 1)
        };

        assert equals(
                new int[]{1, 1, 1, 1, 1, 1},
                toArray(solver.mergeKLists(case5))
        );

        // Boundary:
        // Negative values and zero.
        ListNode[] case6 = {
                build(-10, -2, 8),
                build(-9, -1),
                build(0, 3)
        };

        assert equals(
                new int[]{-10, -9, -2, -1, 0, 3, 8},
                toArray(solver.mergeKLists(case6))
        );

        // Edge case:
        // Many empty lists mixed with non-empty lists.
        ListNode[] case7 = {
                null,
                build(2),
                null,
                build(1, 5),
                null
        };

        assert equals(
                new int[]{1, 2, 5},
                toArray(solver.mergeKLists(case7))
        );

        // Boundary:
        // All lists empty.
        ListNode[] case8 = {
                null,
                null,
                null
        };

        assert solver.mergeKLists(case8) == null;

        // Correctness:
        // Long uneven lists.
        ListNode[] case9 = {
                build(1, 10, 20, 30),
                build(2),
                build(3, 4, 5, 6, 7, 8, 9)
        };

        assert equals(
                new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30},
                toArray(solver.mergeKLists(case9))
        );

        // Minimal input:
        // One single node.
        ListNode[] case10 = {
                build(42)
        };

        assert equals(
                new int[]{42},
                toArray(solver.mergeKLists(case10))
        );

        System.out.println("All assertions passed.");
    }
}