package org.chijai.patterns.gaps;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * Exact implementations for four prompts whose earlier review-queue links
 * pointed only to a neighboring pattern example.
 */
public final class InterviewQueueGapSolutions {

    private InterviewQueueGapSolutions() {
    }

    /** LeetCode 30 — Substring with Concatenation of All Words. */
    public static List<Integer> findSubstring(String text, String[] words) {
        List<Integer> starts = new ArrayList<>();
        if (text == null || words == null || words.length == 0 || words[0].isEmpty()) {
            return starts;
        }

        int wordLength = words[0].length();
        int wordCount = words.length;
        int windowLength = wordLength * wordCount;
        if (text.length() < windowLength) {
            return starts;
        }

        Map<String, Integer> required = new HashMap<>();
        for (String word : words) {
            if (word.length() != wordLength) {
                return starts;
            }
            required.merge(word, 1, Integer::sum);
        }

        for (int offset = 0; offset < wordLength; offset++) {
            int left = offset;
            int wordsInWindow = 0;
            Map<String, Integer> seen = new HashMap<>();

            for (int right = offset; right + wordLength <= text.length(); right += wordLength) {
                String word = text.substring(right, right + wordLength);
                if (!required.containsKey(word)) {
                    seen.clear();
                    wordsInWindow = 0;
                    left = right + wordLength;
                    continue;
                }

                seen.merge(word, 1, Integer::sum);
                wordsInWindow++;

                while (seen.get(word) > required.get(word)) {
                    String removed = text.substring(left, left + wordLength);
                    seen.merge(removed, -1, Integer::sum);
                    left += wordLength;
                    wordsInWindow--;
                }

                if (wordsInWindow == wordCount) {
                    starts.add(left);
                    String removed = text.substring(left, left + wordLength);
                    seen.merge(removed, -1, Integer::sum);
                    left += wordLength;
                    wordsInWindow--;
                }
            }
        }
        return starts;
    }

    /** LeetCode 622 — Design Circular Queue. */
    public static final class CircularQueue {
        private final int[] values;
        private int head;
        private int size;

        public CircularQueue(int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("capacity must be positive");
            }
            values = new int[capacity];
        }

        public boolean enQueue(int value) {
            if (isFull()) {
                return false;
            }
            values[(head + size) % values.length] = value;
            size++;
            return true;
        }

        public boolean deQueue() {
            if (isEmpty()) {
                return false;
            }
            head = (head + 1) % values.length;
            size--;
            return true;
        }

        public int front() {
            return isEmpty() ? -1 : values[head];
        }

        public int rear() {
            return isEmpty() ? -1 : values[(head + size - 1) % values.length];
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean isFull() {
            return size == values.length;
        }
    }

    /** LeetCode 716 — Max Stack with O(log n) popMax. */
    public static final class MaxStack {
        private final Node sentinel = new Node(0);
        private final TreeMap<Integer, ArrayDeque<Node>> byValue = new TreeMap<>();

        public MaxStack() {
            sentinel.previous = sentinel;
            sentinel.next = sentinel;
        }

        public void push(int value) {
            Node node = new Node(value);
            linkBeforeSentinel(node);
            byValue.computeIfAbsent(value, ignored -> new ArrayDeque<>()).addLast(node);
        }

        public int pop() {
            Node node = lastNode();
            ArrayDeque<Node> nodes = byValue.get(node.value);
            nodes.removeLast();
            removeValueBucketIfEmpty(node.value, nodes);
            unlink(node);
            return node.value;
        }

        public int top() {
            return lastNode().value;
        }

        public int peekMax() {
            requireNotEmpty();
            return byValue.lastKey();
        }

        public int popMax() {
            requireNotEmpty();
            int maximum = byValue.lastKey();
            ArrayDeque<Node> nodes = byValue.get(maximum);
            Node node = nodes.removeLast();
            removeValueBucketIfEmpty(maximum, nodes);
            unlink(node);
            return maximum;
        }

        private Node lastNode() {
            requireNotEmpty();
            return sentinel.previous;
        }

        private void requireNotEmpty() {
            if (sentinel.next == sentinel) {
                throw new NoSuchElementException("stack is empty");
            }
        }

        private void linkBeforeSentinel(Node node) {
            node.previous = sentinel.previous;
            node.next = sentinel;
            sentinel.previous.next = node;
            sentinel.previous = node;
        }

        private static void unlink(Node node) {
            node.previous.next = node.next;
            node.next.previous = node.previous;
        }

        private void removeValueBucketIfEmpty(int value, ArrayDeque<Node> nodes) {
            if (nodes.isEmpty()) {
                byValue.remove(value);
            }
        }

        private static final class Node {
            private final int value;
            private Node previous;
            private Node next;

            private Node(int value) {
                this.value = value;
            }
        }
    }

    /** LeetCode 677 — Map Sum Pairs. */
    public static final class MapSum {
        private final TrieNode root = new TrieNode();
        private final Map<String, Integer> values = new HashMap<>();

        public void insert(String key, int value) {
            int difference = value - values.getOrDefault(key, 0);
            values.put(key, value);

            TrieNode node = root;
            node.sum += difference;
            for (char character : key.toCharArray()) {
                node = node.children.computeIfAbsent(character, ignored -> new TrieNode());
                node.sum += difference;
            }
        }

        public int sum(String prefix) {
            TrieNode node = root;
            for (char character : prefix.toCharArray()) {
                node = node.children.get(character);
                if (node == null) {
                    return 0;
                }
            }
            return node.sum;
        }

        private static final class TrieNode {
            private final Map<Character, TrieNode> children = new HashMap<>();
            private int sum;
        }
    }

    public static void main(String[] args) {
        require(findSubstring("barfoothefoobarman", new String[]{"foo", "bar"}).equals(List.of(0, 9)),
                "substring concatenation");

        CircularQueue queue = new CircularQueue(3);
        require(queue.enQueue(1) && queue.enQueue(2) && queue.enQueue(3), "circular queue enqueue");
        require(!queue.enQueue(4) && queue.rear() == 3 && queue.isFull(), "circular queue full");
        require(queue.deQueue() && queue.enQueue(4) && queue.rear() == 4 && queue.front() == 2,
                "circular queue wrap");

        MaxStack stack = new MaxStack();
        stack.push(5);
        stack.push(1);
        stack.push(5);
        require(stack.top() == 5 && stack.popMax() == 5 && stack.top() == 1, "max stack popMax");
        require(stack.peekMax() == 5 && stack.pop() == 1 && stack.top() == 5, "max stack order");

        MapSum mapSum = new MapSum();
        mapSum.insert("apple", 3);
        require(mapSum.sum("ap") == 3, "map sum first insert");
        mapSum.insert("app", 2);
        require(mapSum.sum("ap") == 5, "map sum second insert");
        mapSum.insert("apple", 2);
        require(mapSum.sum("ap") == 4, "map sum overwrite delta");

        System.out.println("InterviewQueueGapSolutions: all checks passed");
    }

    private static void require(boolean condition, String name) {
        if (!condition) {
            throw new AssertionError(name + " failed");
        }
    }
}
