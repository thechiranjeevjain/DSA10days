package org.chijai.day12.randomized.reservoirsampling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * LeetCode 382 - Linked List Random Node
 *
 * PRIMARY PATTERN
 *   Materialize once -> Uniform random index
 *
 * FOLLOW-UP PATTERN
 *   Reservoir Sampling -> O(1) extra space / unknown length
 *
 * REPO
 *   org/chijai/randomized/reservoirsampling/LinkedListRandomNode.java
 */
public class LinkedListRandomNode {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    /*
     * ================================================================
     * PROBLEM
     * ================================================================
     *
     * Given a singly linked list, getRandom() must return one node value.
     * Every node must have equal probability.
     *
     * Example
     *   list = 10 -> 20 -> 30
     *
     *   P(10) = 1/3
     *   P(20) = 1/3
     *   P(30) = 1/3
     *
     * Follow-up:
     *   What if the list is extremely large and its length is unknown?
     *   Can you use O(1) extra space?
     */

    /*
     * ================================================================
     * MINIMUM INTUITION
     * ================================================================
     *
     * Base problem: the list is available at construction time.
     * Copy its values once into an ArrayList, then choose a uniform index.
     *
     * Do not pay the complexity of reservoir sampling until the follow-up
     * actually requires O(1) extra space or unknown stream length.
     */

    // ================================================================
    // PRIMARY SOLUTION — EASIEST FOR THE BASE CONSTRAINTS
    // Build O(n) | getRandom O(1) | Space O(n)
    // ================================================================
    static class Solution {

        private final List<Integer> values = new ArrayList<>();
        private final Random random = new Random();

        public Solution(ListNode head) {
            while (head != null) {
                values.add(head.val);
                head = head.next;
            }
        }

        public int getRandom() {
            return values.get(random.nextInt(values.size()));
        }
    }

    /*
     * ================================================================
     * APPROACH PROGRESSION
     * ================================================================
     *
     * 1. COPY VALUES ONCE
     *
     *   ArrayList gives O(1) random indexing.
     *   Build O(n), query O(1), space O(n).
     *   Best default when memory is allowed.
     *
     * 2. STORE ONLY LENGTH
     *
     *   Count n once.
     *   Pick random position 0..n-1.
     *   Walk the list to that position.
     *
     *   Build O(n), query O(n), extra space O(1).
     *   Simpler than reservoir, but repeated queries repeatedly traverse.
     *
     * 3. RESERVOIR SAMPLING — FOLLOW-UP
     *
     *   Traverse once per query and keep one candidate.
     *   When visiting the k-th node, replace the candidate with probability
     *   1/k.
     *
     *   Query O(n), extra space O(1), no length required in advance.
     */

    static class LengthOnlySolution {

        private final ListNode head;
        private final int length;
        private final Random random = new Random();

        LengthOnlySolution(ListNode head) {
            this.head = head;

            int count = 0;
            ListNode node = head;

            while (node != null) {
                count++;
                node = node.next;
            }

            this.length = count;
        }

        int getRandom() {
            int steps = random.nextInt(length);
            ListNode node = head;

            while (steps-- > 0) {
                node = node.next;
            }

            return node.val;
        }
    }

    static class ReservoirSolution {

        private final ListNode head;
        private final Random random = new Random();

        ReservoirSolution(ListNode head) {
            this.head = head;
        }

        int getRandom() {
            int answer = 0;
            int seen = 0;

            for (ListNode node = head; node != null; node = node.next) {
                seen++;

                if (random.nextInt(seen) == 0) {
                    answer = node.val;
                }
            }

            return answer;
        }
    }

    /*
     * WHY RESERVOIR WORKS
     *
     * At node k:
     *   choose it with probability 1/k.
     *
     * A previous candidate survives that step with probability (k-1)/k.
     * Repeating this leaves every final node with probability 1/n.
     */

    /*
     * RELATED — WORKING FILE
     *
     * RandomPickIndex.java
     *   Same O(1)-space follow-up idea, but only matching indices compete.
     */

    /*
     * RECALL
     *
     * Base problem: copy -> random index.
     * Follow-up: unknown/huge stream -> reservoir, replace with probability 1/k.
     */

    public static void main(String[] args) {
        ListNode head = list(10, 20, 30);
        Solution solution = new Solution(head);

        int[] count = new int[3];

        for (int i = 0; i < 60_000; i++) {
            int value = solution.getRandom();
            if (value == 10) count[0]++;
            if (value == 20) count[1]++;
            if (value == 30) count[2]++;
        }

        assert approximately(count[0] / 60_000.0, 1.0 / 3, 0.03);
        assert approximately(count[1] / 60_000.0, 1.0 / 3, 0.03);
        assert approximately(count[2] / 60_000.0, 1.0 / 3, 0.03);

        System.out.println("LinkedListRandomNode: all checks passed");
    }

    private static ListNode list(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        for (int value : values) {
            tail.next = new ListNode(value);
            tail = tail.next;
        }

        return dummy.next;
    }

    private static boolean approximately(double actual, double expected, double tolerance) {
        return Math.abs(actual - expected) <= tolerance;
    }
}
