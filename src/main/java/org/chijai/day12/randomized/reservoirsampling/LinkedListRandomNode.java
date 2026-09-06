package org.chijai.day12.randomized.reservoirsampling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * LeetCode 382 - Linked List Random Node
 *
 * PATTERN
 *   Randomized Algorithms -> Reservoir Sampling
 *
 * USE WHEN
 *   Population size is unknown/streamed or you do not want to store all items.
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
     * FIRST-PRINCIPLES INVENTION PATH
     *
     * While scanning item k, make the current item replace the reservoir
     * with probability 1/k.
     *
     * Any earlier item survives step k with probability (k-1)/k.
     * Chaining these survival probabilities leaves every one of n items
     * with final probability exactly 1/n.
     */

    // =====================================================================
    // OPTIMAL SOLUTION — Reservoir Sampling, k = 1
    // Constructor O(1), getRandom O(n), extra space O(1)
    // =====================================================================
    static class Solution {

        private final ListNode head;
        private final Random random;

        public Solution(ListNode head) {
            this(head, new Random());
        }

        Solution(ListNode head, Random random) {
            this.head = head;
            this.random = random;
        }

        public int getRandom() {
            int chosen = head.val;
            int seen = 1;

            for (ListNode node = head.next; node != null; node = node.next) {
                seen++;

                if (random.nextInt(seen) == 0) {
                    chosen = node.val;
                }
            }

            return chosen;
        }
    }

    // =====================================================================
    // ALTERNATIVE — Materialize values
    // Constructor O(n), getRandom O(1), space O(n)
    // =====================================================================
    static class StoredValuesSolution {

        private final List<Integer> values = new ArrayList<>();
        private final Random random = new Random();

        StoredValuesSolution(ListNode head) {
            for (ListNode node = head; node != null; node = node.next) {
                values.add(node.val);
            }
        }

        public int getRandom() {
            return values.get(random.nextInt(values.size()));
        }
    }

    /*
     * PROOF SNAPSHOT
     *
     * For item i to remain after n items:
     *
     *   P(chosen at i) * P(survive i+1 ... n)
     *   = (1/i) * (i/(i+1)) * ... * ((n-1)/n)
     *   = 1/n
     */

    /*
     * RECALL
     *
     * SEEN = k
     * REPLACE WITH PROBABILITY 1/k
     */

    /*
     * RELATED
     *   reservoirsampling/RandomPickIndex.java
     *   weightedsampling/RandomPickWithWeight.java  // different: known weights
     */

    public static void main(String[] args) {
        ListNode head = new ListNode(10);
        head.next = new ListNode(20);
        head.next.next = new ListNode(30);

        Solution solution = new Solution(head, new Random(5));

        for (int i = 0; i < 1_000; i++) {
            int value = solution.getRandom();
            assert value == 10 || value == 20 || value == 30;
        }

        System.out.println("LinkedListRandomNode: all checks passed");
    }
}
