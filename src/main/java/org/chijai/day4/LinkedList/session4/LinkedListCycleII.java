package org.chijai.day4.LinkedList.session4;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LinkedListCycleII {

    /*
     * ============================================================
     * PRIMARY PROBLEM — LINKED LIST CYCLE II
     * ============================================================
     *
     * Return the node where a cycle begins.
     * Return null if no cycle exists.
     *
     * Preferred Pattern:
     * Floyd's Fast & Slow Pointers
     *
     * Time  : O(n)
     * Space : O(1)
     */

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

    /*
     * ============================================================
     * PREFERRED INTERVIEW SOLUTION
     * ============================================================
     */

    static final class Optimal {

        ListNode detectCycle(ListNode head) {

            ListNode slow = head;
            ListNode fast = head;

            while (fast != null && fast.next != null) {

                slow = slow.next;
                fast = fast.next.next;

                if (slow == fast) {

                    ListNode entry = head;

                    while (entry != slow) {
                        entry = entry.next;
                        slow = slow.next;
                    }

                    return entry;
                }
            }

            return null;
        }
    }

    /*
     * ============================================================
     * WHY 1 — WHY MUST SLOW AND FAST COLLIDE?
     * ============================================================
     *
     * Inside the cycle:
     *
     *      slow moves 1
     *      fast moves 2
     *
     * So fast gains exactly 1 node on slow every round.
     *
     * The cycle has finite length L.
     *
     * Relative gap therefore changes by 1 modulo L:
     *
     *      gap, gap-1, gap-2, ... mod L
     *
     * Eventually the gap becomes 0.
     *
     * Therefore slow == fast.
     *
     * INTERVIEW ONE-LINER:
     *
     * "Fast gains one node per round inside a finite loop,
     *  so modulo L it must eventually catch slow."
     */

    /*
     * ============================================================
     * WHY 2 — WHY DOES RESETTING ONE POINTER FIND THE ENTRY?
     * ============================================================
     *
     * Let:
     *
     *      a = head  -> cycle entry
     *      b = entry -> meeting point
     *      L = cycle length
     *
     * At collision:
     *
     *      slow distance = a + b
     *      fast distance = 2(a + b)
     *
     * Both are on the SAME node.
     *
     * Therefore fast's EXTRA distance over slow must be
     * complete cycle laps:
     *
     *      fast - slow = kL
     *
     * But:
     *
     *      fast - slow
     *      = 2(a + b) - (a + b)
     *      = a + b
     *
     * Hence:
     *
     *      a + b = kL
     *
     * So:
     *
     *      a = kL - b
     *        = (k - 1)L + (L - b)
     *
     * And:
     *
     *      L - b = meeting -> entry
     *
     * Therefore:
     *
     *      head -> entry
     *      =
     *      meeting -> entry
     *      + whole cycle laps
     *
     * Whole laps change distance, not final position.
     *
     * So if one pointer starts at head and one at meeting,
     * and both move one step at a time,
     * they meet at the cycle entry.
     *
     * INTERVIEW ONE-LINER:
     *
     * "Collision gives a + b = kL, so a = kL - b.
     *  That is meeting-to-entry plus full laps.
     *  Move both one step; they meet at entry."
     */

    /*
     * ============================================================
     * WHY 3 — WHY DOES "SAME NODE" MEAN EXTRA DISTANCE = WHOLE LAPS?
     * ============================================================
     *
     * On a cycle, leaving a node and returning to that SAME node
     * requires travelling:
     *
     *      1 full cycle
     *      2 full cycles
     *      3 full cycles
     *      ...
     *
     * So if slow and fast are on the same cycle node:
     *
     *      fast distance - slow distance = kL
     *
     * This is the bridge from pointer movement to the distance proof.
     */

    /*
     * ============================================================
     * WHY 4 — WHY CHECK fast AND fast.next?
     * ============================================================
     *
     * fast moves two steps:
     *
     *      fast = fast.next.next;
     *
     * Therefore before moving, both must exist:
     *
     *      fast != null
     *      fast.next != null
     *
     * If fast escapes the list, there is no cycle.
     */

    /*
     * ============================================================
     * 30-SECOND RECALL CARD
     * ============================================================
     *
     * TRIGGER
     * -------
     * Linked list + cycle entry + O(1) extra space.
     *
     * PATTERN
     * -------
     * Floyd's Tortoise and Hare.
     *
     * PHASE 1
     * -------
     * slow = 1x
     * fast = 2x
     *
     * Fast gains 1 per round inside finite cycle
     * -> collision inevitable.
     *
     * PHASE 2
     * -------
     * Reset one pointer to head.
     * Keep the other at meeting.
     * Move both 1 step.
     *
     * PROOF
     * -----
     * a + b = kL
     * -> a = kL - b
     * -> head->entry = meeting->entry + full laps
     * -> walk together
     * -> entry.
     *
     * TRAPS
     * -----
     * Collision != entry.
     * Compare node references, not values.
     * Recovery pointers both move exactly 1.
     *
     * COMPLEXITY
     * ----------
     * Time  : O(n)
     * Space : O(1)
     */

    /*
     * ============================================================
     * INTERVIEW ARTICULATION
     * ============================================================
     *
     * "I solve this in two phases.
     *
     * First, slow moves one step and fast moves two.
     * Once both are inside the cycle, fast gains one node on slow
     * every iteration. Since the loop has finite length L,
     * the relative gap modulo L must eventually become zero,
     * so they collide.
     *
     * The collision is not necessarily the cycle entry.
     *
     * Let a be head-to-entry and b be entry-to-meeting.
     * At collision, slow travelled a+b and fast travelled twice that.
     * Because they are on the same cycle node, fast's extra distance
     * must be whole cycle laps, so a+b = kL.
     *
     * Therefore a = kL-b, which is meeting-to-entry plus full laps.
     * Full laps do not change position.
     *
     * So I reset one pointer to head, keep the other at the meeting
     * point, move both one step at a time, and their first meeting
     * is the cycle entry.
     *
     * This is O(n) time and O(1) extra space."
     */

    /*
     * ============================================================
     * INTERVIEWER FOLLOW-UP — SHORTEST DEFENSIBLE PROOFS
     * ============================================================
     *
     * Q1. Why must they collide?
     *
     * "Inside the cycle, fast gains one node per round.
     *  The gap is modulo finite length L, so eventually gap = 0."
     *
     * Q2. Why does reset find the entry?
     *
     * "At collision, fast's extra distance is whole laps:
     *  a+b = kL.
     *  Hence a = kL-b, which is meeting-to-entry plus full laps.
     *  Move both one step; they meet at entry."
     *
     * Q3. Why is the extra distance whole laps?
     *
     * "Because they are on the same node of a closed cycle.
     *  Any extra distance that returns to the same node must be kL."
     */

    /*
     * ============================================================
     * REUSABLE MASTER TEMPLATE — FLOYD
     * ============================================================
     *
     * ListNode slow = head;
     * ListNode fast = head;
     *
     * while (fast != null && fast.next != null) {
     *
     *     slow = slow.next;
     *     fast = fast.next.next;
     *
     *     if (slow == fast) {
     *
     *         ListNode entry = head;
     *
     *         while (entry != slow) {
     *             entry = entry.next;
     *             slow = slow.next;
     *         }
     *
     *         return entry;
     *     }
     * }
     *
     * return null;
     */

    /*
     * ============================================================
     * BASELINE — HASHSET
     * ============================================================
     *
     * First repeated node is the cycle entry.
     *
     * Time  : O(n)
     * Space : O(n)
     *
     * Useful as the obvious baseline before optimizing space.
     */

    static final class BruteForce {

        ListNode detectCycle(ListNode head) {

            Set<ListNode> visited = new HashSet<>();

            ListNode current = head;

            while (current != null) {

                if (!visited.add(current)) {
                    return current;
                }

                current = current.next;
            }

            return null;
        }
    }

    /*
     * ============================================================
     * RELATED / REINFORCEMENT PROBLEMS
     * ============================================================
     *
     * 1. Linked List Cycle
     *    Same detection phase.
     *    Stop at collision; no recovery phase.
     *
     * 2. Middle of Linked List
     *    Same 1x / 2x movement.
     *    Fast reaching the end reveals halfway position.
     *
     * 3. Happy Number
     *    Floyd on generated integer states.
     *
     * 4. Find the Duplicate Number
     *    Array values form an implicit functional graph.
     *    Same cycle-entry mathematics.
     *
     * 5. Palindrome Linked List
     *    Fast/slow finds the midpoint before reversing half.
     *
     * 6. Reorder List
     *    Find middle -> reverse second half -> merge.
     *
     * PATTERN BOUNDARY:
     *
     * Floyd works when every state has exactly ONE deterministic
     * next state.
     *
     * General graph with multiple outgoing edges:
     * use graph-cycle techniques instead.
     */

    /*
     * ============================================================
     * COMMON FAILURE MODES
     * ============================================================
     *
     * 1. Return slow immediately after collision.
     *    -> collision proves a cycle, not necessarily the entry.
     *
     * 2. Move one recovery pointer faster.
     *    -> destroys the equal-speed convergence property.
     *
     * 3. Compare values instead of references.
     *    -> duplicate values can exist.
     *
     * 4. Use while (fast.next != null).
     *    -> crashes when fast itself is null.
     *
     * 5. Memorize x1 = x3.
     *    -> incomplete in general.
     *
     * Correct statement:
     *
     *      head->entry
     *      =
     *      meeting->entry
     *      + whole cycle laps
     */

    /*
     * ============================================================
     * SELF-VERIFYING TESTS
     * ============================================================
     *
     * Run with:
     *
     *      java -ea LinkedListCycleII
     */

    public static void main(String[] args) {

        Optimal optimal = new Optimal();
        BruteForce brute = new BruteForce();

        // 3 -> 2 -> 0 -> -4 -> 2
        {
            ListNode[] nodes = createNodes(3, 2, 0, -4);
            ListNode head = connectCycle(nodes, 1);

            assert optimal.detectCycle(head) == nodes[1];
            assert brute.detectCycle(head) == nodes[1];
        }

        // Entire list is a cycle.
        {
            ListNode[] nodes = createNodes(1, 2, 3, 4);
            ListNode head = connectCycle(nodes, 0);

            assert optimal.detectCycle(head) == nodes[0];
        }

        // Self-cycle.
        {
            ListNode node = new ListNode(42);
            node.next = node;

            assert optimal.detectCycle(node) == node;
        }

        // Two-node cycle.
        {
            ListNode[] nodes = createNodes(10, 20);
            ListNode head = connectCycle(nodes, 0);

            assert optimal.detectCycle(head) == nodes[0];
        }

        // Long tail before cycle.
        {
            ListNode[] nodes = createNodes(1, 2, 3, 4, 5, 6, 7, 8, 9);
            ListNode head = connectCycle(nodes, 5);

            assert optimal.detectCycle(head) == nodes[5];
        }

        // Late cycle entry.
        {
            ListNode[] nodes = createNodes(1, 2, 3, 4, 5, 6, 7, 8);
            ListNode head = connectCycle(nodes, 6);

            assert optimal.detectCycle(head) == nodes[6];
        }

        // Duplicate values: identity matters.
        {
            ListNode[] nodes = createNodes(5, 5, 5, 5, 5);
            ListNode head = connectCycle(nodes, 2);

            assert optimal.detectCycle(head) == nodes[2];
        }

        // No cycle.
        {
            ListNode[] nodes = createNodes(1, 2, 3, 4);
            ListNode head = connectCycle(nodes, -1);

            assert optimal.detectCycle(head) == null;
        }

        // Single node, no cycle.
        {
            ListNode node = new ListNode(100);

            assert optimal.detectCycle(node) == null;
        }

        // Empty list.
        assert optimal.detectCycle(null) == null;

        // Cross-check brute force and Floyd.
        {
            ListNode[] nodes = createNodes(9, 8, 7, 6, 5);
            ListNode head = connectCycle(nodes, 3);

            assert brute.detectCycle(head) == optimal.detectCycle(head);
        }

        System.out.println("All assertions passed.");
    }

    private static ListNode[] createNodes(int... values) {

        ListNode[] nodes = new ListNode[values.length];

        for (int i = 0; i < values.length; i++) {
            nodes[i] = new ListNode(values[i]);
        }

        return nodes;
    }

    private static ListNode connectCycle(ListNode[] nodes, int entryIndex) {

        Objects.requireNonNull(nodes);

        if (nodes.length == 0) {
            return null;
        }

        for (int i = 0; i < nodes.length - 1; i++) {
            nodes[i].next = nodes[i + 1];
        }

        if (entryIndex >= 0) {
            nodes[nodes.length - 1].next = nodes[entryIndex];
        }

        return nodes[0];
    }
}
