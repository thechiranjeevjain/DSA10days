package org.chijai.patterns.treedfs;

import org.chijai.patterns.PatternChapter;

public final class TreeDfsPatternLab {
    private TreeDfsPatternLab() {
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
                "Tree DFS / Recursion",
                "Recursive Return Contract",
                "Subtree Summary",
                "Postorder Combine",
                "Maximum Depth Of Binary Tree"
        );
    }

    public static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    public static boolean isValidBst(TreeNode root) {
        return isValidBst(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private static boolean isValidBst(TreeNode root, long lowExclusive, long highExclusive) {
        if (root == null) {
            return true;
        }
        if (root.value <= lowExclusive || root.value >= highExclusive) {
            return false;
        }
        return isValidBst(root.left, lowExclusive, root.value)
                && isValidBst(root.right, root.value, highExclusive);
    }

    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root;
        }
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null) {
            return root;
        }
        return left != null ? left : right;
    }
}
