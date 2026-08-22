package org.chijai.day4.LinkedList.session1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReverseLinkedListTest {

    @Test
    void reverseListFlipsPointersWithoutLosingNodes() {
        ReverseLinkedList.ListNode head = ReverseLinkedList.ListNode.fromArray(new int[]{1, 2, 3, 4, 5});
        ReverseLinkedList.ListNode reversed = ReverseLinkedList.OptimalSolution.reverseList(head);

        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, toArray(reversed));
    }

    @Test
    void reverseListHandlesEmptyList() {
        assertNull(ReverseLinkedList.OptimalSolution.reverseList(null));
    }

    private static int[] toArray(ReverseLinkedList.ListNode head) {
        java.util.List<Integer> values = new java.util.ArrayList<>();
        ReverseLinkedList.ListNode current = head;
        while (current != null) {
            values.add(current.val);
            current = current.next;
        }
        return values.stream().mapToInt(Integer::intValue).toArray();
    }
}
