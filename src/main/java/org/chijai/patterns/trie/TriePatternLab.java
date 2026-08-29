package org.chijai.patterns.trie;

import org.chijai.patterns.PatternChapter;

public final class TriePatternLab {
    private final Node root = new Node();

    private static final class Node {
        private final Node[] next = new Node[26];
        private boolean word;
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Trie",
                "Prefix / Dictionary Search",
                "One Character Per Edge",
                "Terminal Flag Separates Word From Prefix",
                "Implement Trie"
        );
    }

    public void insert(String word) {
        Node node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (node.next[index] == null) {
                node.next[index] = new Node();
            }
            node = node.next[index];
        }
        node.word = true;
    }

    public boolean search(String word) {
        Node node = find(word);
        return node != null && node.word;
    }

    public boolean startsWith(String prefix) {
        return find(prefix) != null;
    }

    private Node find(String value) {
        Node node = root;
        for (char c : value.toCharArray()) {
            int index = c - 'a';
            if (node.next[index] == null) {
                return null;
            }
            node = node.next[index];
        }
        return node;
    }
}
