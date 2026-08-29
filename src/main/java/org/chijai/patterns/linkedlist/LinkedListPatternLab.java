package org.chijai.patterns.linkedlist;

import org.chijai.patterns.PatternChapter;

public final class LinkedListPatternLab {
    private LinkedListPatternLab() {
    }

    public static final class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Linked List Pointers",
                "Pointer Manipulation",
                "Dummy / Fast-Slow / Reversal",
                "Save Next Before Rewire",
                "Reverse Linked List"
        );
    }

    public static Node reverse(Node head) {
        Node previous = null;
        Node current = head;
        while (current != null) {
            Node next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }
        return previous;
    }

    public static boolean hasCycle(Node head) {
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return true;
            }
        }
        return false;
    }

    public static Node mergeSorted(Node left, Node right) {
        Node dummy = new Node(0);
        Node tail = dummy;
        while (left != null && right != null) {
            if (left.value <= right.value) {
                tail.next = left;
                left = left.next;
            } else {
                tail.next = right;
                right = right.next;
            }
            tail = tail.next;
        }
        tail.next = left != null ? left : right;
        return dummy.next;
    }
}
