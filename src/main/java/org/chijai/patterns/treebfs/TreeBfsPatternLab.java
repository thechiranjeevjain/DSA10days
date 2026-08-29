package org.chijai.patterns.treebfs;

import org.chijai.patterns.PatternChapter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public final class TreeBfsPatternLab {
    private TreeBfsPatternLab() {
    }

    public static final class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int value) {
            this.value = value;
        }
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Tree BFS / Level Order",
                "Layer Traversal",
                "Queue By Level",
                "Snapshot Level Size",
                "Binary Tree Level Order Traversal"
        );
    }

    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.remove();
                level.add(node.value);
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            result.add(level);
        }
        return result;
    }

    public static List<Integer> rightSideView(TreeNode root) {
        List<Integer> view = new ArrayList<>();
        for (List<Integer> level : levelOrder(root)) {
            view.add(level.get(level.size() - 1));
        }
        return view;
    }
}
