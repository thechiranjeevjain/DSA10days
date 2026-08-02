package org.chijai.day6.trees.session3;

import java.util.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * ============================================================
 *  📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Title:
 * Invert Binary Tree
 *
 * Difficulty:
 * Easy
 *
 * Tags:
 * Tree
 * Binary Tree
 * DFS
 * BFS
 * Recursion
 * Stack
 * Queue
 *
 * Problem Description
 * -------------------
 * Given the root of a binary tree, invert the tree.
 *
 * Every node swaps its left and right child exactly once.
 *
 * Return the root after inversion.
 *
 * Constraints
 * -----------
 * Number of nodes:
 *      [0, 100]
 *
 * Node values:
 *      [-100, 100]
 *
 * Representative Examples
 * -----------------------
 *
 * Example 1
 *
 * Input
 *          4
 *        /   \
 *       2     7
 *      / \   / \
 *     1  3  6  9
 *
 * Output
 *
 *          4
 *        /   \
 *       7     2
 *      / \   / \
 *     9  6  3  1
 *
 *
 * Example 2
 *
 *      2
 *     / \
 *    1   3
 *
 * becomes
 *
 *      2
 *     / \
 *    3   1
 *
 *
 * Example 3
 *
 * Input:
 * null
 *
 * Output:
 * null
 *
 * Official LeetCode
 * https://leetcode.com/problems/invert-binary-tree/
 *
 *
 * ============================================================
 *  🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern
 * -------
 * Tree Traversal + Local Transformation
 *
 * Archetype
 * ---------
 * Visit every node exactly once.
 *
 * At every visit:
 *
 *      Perform one local modification.
 *
 * Continue traversal.
 *
 * Core Invariant
 * --------------
 * After processing a node,
 * that node permanently satisfies the required mirror relationship.
 *
 * The remaining work exists only inside its children.
 *
 * Why It Works
 * ------------
 * Mirror inversion is completely local.
 *
 * Every node depends only on:
 *
 *      left child
 *      right child
 *
 * No ancestor information.
 *
 * No sibling information.
 *
 * Therefore every node can be solved independently.
 *
 * Recognition Signals
 * -------------------
 * Use this pattern whenever:
 *
 * • Every node is modified independently.
 * • Entire tree must be visited.
 * • Parent state does not depend on descendant order.
 * • Transformation is local.
 *
 * When To Use
 * -----------
 * Tree cloning
 * Tree inversion
 * Node value updates
 * Tree serialization
 * Tree pruning
 *
 * When NOT To Use
 * ---------------
 * Lowest Common Ancestor
 * Diameter
 * Maximum Path Sum
 * BST validation
 *
 * Those require combining information from multiple subtrees.
 *
 * Comparison
 * ----------
 *
 * DFS
 * ----
 * Natural recursive formulation.
 *
 * BFS
 * ----
 * Same correctness.
 *
 * Better when recursion depth may overflow.
 *
 * Stack DFS
 * ---------
 * Explicit control over traversal.
 *
 * Preferred in production when tree depth is unknown.
 *
 *
 * ============================================================
 *  🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Mental Model
 * ------------
 * Imagine every node owns a tiny mirror.
 *
 * When we visit a node,
 * it immediately flips its two children.
 *
 * Then each child repeats the exact same rule.
 *
 * Eventually every node has looked into its own mirror.
 *
 * Global inversion emerges from local swaps.
 *
 *
 * Fundamental Invariant
 * ---------------------
 * Whenever processing finishes for one node:
 *
 *      node.left
 *
 * already represents the inverted version
 * of the original right subtree.
 *
 * and
 *
 *      node.right
 *
 * represents the inverted version
 * of the original left subtree.
 *
 *
 * Recursive Invariant
 * -------------------
 * invert(node)
 *
 * always returns
 * the completely inverted subtree rooted at node.
 *
 *
 * Traversal Invariant
 * -------------------
 * Every node is processed exactly once.
 *
 * Every edge participates in exactly one swap.
 *
 *
 * State Variables
 * ---------------
 * root
 *      current subtree root
 *
 * left
 *      original left child
 *
 * right
 *      original right child
 *
 *
 * Allowed Moves
 * -------------
 * Visit node.
 *
 * Preserve child references.
 *
 * Recursively invert children.
 *
 * Attach results to opposite sides.
 *
 *
 * Forbidden Move
 * --------------
 * Never overwrite
 * node.left
 * before saving it.
 *
 * Otherwise the original subtree disappears forever.
 *
 *
 * Termination
 * -----------
 * Empty subtree.
 *
 * root == null
 *
 * immediately returns.
 *
 *
 * Correctness Intuition
 * ---------------------
 * Since every node swaps exactly once,
 * and recursion guarantees every subtree is inverted,
 * the entire tree becomes the mirror image.
 *
 *
 * Why Naive Thinking Fails
 * ------------------------
 * Some candidates try:
 *
 * swap(root.left, root.right)
 *
 * and stop.
 *
 * Only the root changes.
 *
 * Descendants remain unchanged.
 *
 * Example
 *
 * Original
 *
 *      4
 *     / \
 *    2   7
 *   /     \
 *  1       9
 *
 * Root-only swap
 *
 *      4
 *     / \
 *    7   2
 *         \
 *          1
 *
 * This is NOT a mirror tree.
 *
 *
 * ============================================================
 *  🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================
 *
 * Wrong Solution 1
 * ----------------
 * Swap only the root.
 *
 * Why It Looks Correct
 * --------------------
 * Small examples accidentally pass.
 *
 * Violated Invariant
 * ------------------
 * Every node must satisfy the mirror relationship,
 * not just the root.
 *
 *
 * Wrong Solution 2
 * ----------------
 * Overwrite left child before preserving it.
 *
 * Example
 *
 * node.left = invert(node.right);
 * node.right = invert(node.left);
 *
 * The original left subtree is already lost.
 *
 *
 * Wrong Solution 3
 * ----------------
 * Forget base case.
 *
 * Result
 * ------
 * NullPointerException
 * or
 * infinite recursion.
 *
 *
 * Wrong Solution 4
 * ----------------
 * Swap after recursive calls using modified references.
 *
 * The algorithm begins traversing incorrect children.
 *
 *
 * Interview Trap
 * --------------
 * The interviewer may ask:
 *
 * "Can recursion overflow?"
 *
 * Yes.
 *
 * Worst-case skew tree:
 *
 * height = n
 *
 * Recursive DFS uses application stack.
 *
 * Iterative DFS or BFS avoids that limitation.
 *
 *
 * ============================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================
 *
 * Typing Order
 * ------------
 *
 * 1.
 * Method declaration.
 *
 * 2.
 * Empty-tree guard.
 *
 * 3.
 * Save:
 *
 *      left
 *      right
 *
 * 4.
 * Recursively invert right subtree.
 *
 * 5.
 * Assign result to left.
 *
 * 6.
 * Recursively invert left subtree.
 *
 * 7.
 * Assign result to right.
 *
 * 8.
 * Return root.
 *
 *
 * Mechanical Skeleton
 * -------------------
 *
 * if empty
 *     return
 *
 * save left
 * save right
 *
 * left = recurse(right)
 * right = recurse(left)
 *
 * return root
 *
 *
 * ============================================================
 *  🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================
 *
 * empty -> return
 *
 * save left
 * save right
 *
 * left = invert(right)
 * right = invert(left)
 *
 * return root
 *
 */

public class InvertBinaryTree {

    /**
     * ============================================================
     * Shared Tree Definition
     * ============================================================
     */
    static final class TreeNode {

        int val;

        TreeNode left;

        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    /**
     * ------------------------------------------------------------
     * Brute Force
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Build an entirely new mirrored tree.
     *
     * Invariant
     * ---------
     * Newly created node always represents
     * the mirrored copy of the original subtree.
     *
     * Limitation
     * ----------
     * Uses O(n) extra memory.
     *
     * Complexity
     * ----------
     * Time : O(n)
     * Space: O(n)
     *
     * Interview Usefulness
     * --------------------
     * Good stepping stone before discussing in-place inversion.
     */
    static final class BruteForce {

        TreeNode invertTree(TreeNode root) {

            if (root == null) {
                return null;
            }

            TreeNode node = new TreeNode(root.val);

            node.left = invertTree(root.right);

            node.right = invertTree(root.left);

            return node;
        }
    }

    /**
     * ------------------------------------------------------------
     * Improved
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Perform inversion in-place using recursion.
     *
     * Invariant
     * ---------
     * After invertTree(node),
     * node becomes the root of the fully inverted subtree.
     *
     * Improvement
     * -----------
     * Eliminates duplicate tree allocation.
     *
     * Complexity
     * ----------
     * Time : O(n)
     * Space: O(h)
     *
     * h = tree height.
     *
     * Interview Usefulness
     * --------------------
     * Default interview solution.
     */
    static final class RecursiveDFS {

        TreeNode invertTree(TreeNode root) {

            // Invariant: empty subtree is already inverted.
            if (root == null) {
                return null;
            }

            // Preserve original children before modification.
            TreeNode left = root.left;
            TreeNode right = root.right;

            // Invariant: left becomes inverted original right subtree.
            root.left = invertTree(right);

            // Invariant: right becomes inverted original left subtree.
            root.right = invertTree(left);

            // Entire subtree rooted here now satisfies mirror property.
            return root;
        }
    }

    /**
     * ------------------------------------------------------------
     * Optimal (Interview Preferred)
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Explicit DFS using a stack.
     *
     * Invariant
     * ---------
     * Every popped node is swapped exactly once.
     *
     * Children waiting in the stack still need processing.
     *
     * Correctness
     * -----------
     * Local swap plus full traversal.
     *
     * Complexity
     * ----------
     * Time : O(n)
     * Space: O(h)
     *
     * Interview Usefulness
     * --------------------
     * Preferred when recursion depth may overflow.
     */
    static final class IterativeDFS {

        TreeNode invertTree(TreeNode root) {

            // Empty input handled early.
            if (root == null) {
                return null;
            }

            Deque<TreeNode> stack = new ArrayDeque<>();

            stack.push(root);

            while (!stack.isEmpty()) {

                TreeNode node = stack.pop();

                // Invariant: this node has never been swapped before.
                TreeNode left = node.left;

                node.left = node.right;

                node.right = left;

                // Children remain valid subproblems after the swap.
                if (node.left != null) {
                    stack.push(node.left);
                }

                if (node.right != null) {
                    stack.push(node.right);
                }
            }

            return root;
        }
    }

    /**
     * ------------------------------------------------------------
     * Optimal Alternative
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * Breadth-First Search.
     *
     * Invariant
     * ---------
     * Every node removed from the queue has not yet been inverted.
     *
     * After processing it,
     * that node permanently satisfies the mirror property.
     *
     * Complexity
     * ----------
     * Time : O(n)
     * Space: O(w)
     *
     * w = maximum tree width.
     *
     * Interview Usefulness
     * --------------------
     * Preferred when level-order traversal is already required.
     */
    static final class BFS {

        TreeNode invertTree(TreeNode root) {

            // Invariant: empty tree is already inverted.
            if (root == null) {
                return null;
            }

            Queue<TreeNode> queue = new LinkedList<>();

            queue.offer(root);

            while (!queue.isEmpty()) {

                TreeNode node = queue.poll();

                // Local transformation.
                TreeNode left = node.left;
                node.left = node.right;
                node.right = left;

                // Order is irrelevant because both children
                // will eventually be processed exactly once.
                if (node.left != null) {
                    queue.offer(node.left);
                }

                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

            return root;
        }
    }

    /**
     * ============================================================
     * 🟣 INTERVIEW ARTICULATION
     * ============================================================
     *
     * Invariant
     * ---------
     * Every processed node permanently becomes the mirror of its
     * original state.
     *
     * Search Space
     * ------------
     * Every node in the tree.
     *
     * State
     * -----
     * Current node.
     *
     * Transition
     * ----------
     * Swap children.
     *
     * Continue traversal.
     *
     * Discard Rule
     * ------------
     * Once a node has been swapped,
     * it never needs to be visited again.
     *
     * Correctness
     * -----------
     * Every node performs exactly one correct local transformation.
     *
     * Since every subtree is visited,
     * the global tree becomes the mirror image.
     *
     * Termination
     * -----------
     * Traversal finishes after every reachable node has been visited.
     *
     * In-place Feasibility
     * --------------------
     * Yes.
     *
     * Only child references are exchanged.
     *
     * Streaming Feasibility
     * ---------------------
     * No.
     *
     * The algorithm requires random access to tree pointers.
     *
     * When NOT To Use
     * ---------------
     * Problems requiring subtree aggregation:
     *
     * • Diameter
     * • Maximum Path Sum
     * • Balanced Tree
     * • Lowest Common Ancestor
     *
     * Those require information flow upward instead of purely local
     * transformation.
     */

    /**
     * ============================================================
     * 🎯 INTERVIEW RECALL SHEET
     * ============================================================
     *
     * Trigger
     * -------
     * Mirror an entire binary tree.
     *
     * Pattern
     * -------
     * Tree Traversal + Local Transformation.
     *
     * Search Target
     * -------------
     * Every node exactly once.
     *
     * Invariant
     * ---------
     * Processed node is permanently mirrored.
     *
     * Discard Rule
     * ------------
     * Swapped node never needs another visit.
     *
     * Common Trap
     * -----------
     * Forgetting to preserve the original left child before overwrite.
     *
     * Edge Cases
     * ----------
     * • null
     * • single node
     * • skew tree
     * • complete tree
     *
     * Complexity
     * ----------
     * Time  : O(n)
     * Space : O(h) DFS
     *         O(w) BFS
     *
     * One-Liner
     * ---------
     * Swap children once at every node.
     *
     * Re-derivation Cue
     * -----------------
     * "Every node owns its own mirror."
     */

    /**
     * ============================================================
     * 🔄 VARIATIONS & TWEAKS
     * ============================================================
     *
     * Variant
     * -------
     * Recursive DFS
     *
     * Reasoning Change
     * ----------------
     * Uses application stack.
     *
     * Invariant remains identical.
     *
     *
     * Variant
     * -------
     * Iterative DFS
     *
     * Reasoning Change
     * ----------------
     * Explicit stack replaces recursion.
     *
     * Same invariant.
     *
     *
     * Variant
     * -------
     * BFS
     *
     * Reasoning Change
     * ----------------
     * Nodes are processed level by level.
     *
     * Local correctness does not depend on traversal order.
     *
     *
     * Variant
     * -------
     * Immutable Tree
     *
     * Reasoning Change
     * ----------------
     * Allocate new nodes.
     *
     * Preserve original tree.
     *
     * Pattern still works because each node independently constructs
     * its mirrored counterpart.
     *
     *
     * Pattern Boundary
     * ----------------
     * This pattern fails whenever a node must know information from
     * descendants before deciding how to transform itself.
     *
     * Example
     * -------
     * Diameter of Binary Tree.
     *
     * There the state is aggregated upward instead of swapped locally.
     */

    /**
     * ============================================================
     * 🧠 MASTERY CHECKLIST
     * ============================================================
     *
     * Can you answer these without looking?
     *
     * □ What is the invariant?
     *
     *      Every processed node is permanently mirrored.
     *
     * □ What is the search target?
     *
     *      Visit every node once.
     *
     * □ What is the discard rule?
     *
     *      Never revisit a swapped node.
     *
     * □ Why does termination happen?
     *
     *      Finite tree.
     *
     *      Each node processed exactly once.
     *
     * □ Why does the naive solution fail?
     *
     *      It mirrors only the root.
     *
     * □ Edge cases?
     *
     *      null
     *      one node
     *      skew tree
     *      complete tree
     *
     * □ Debugging readiness?
     *
     *      Verify every node swaps exactly once.
     *
     * □ Variant readiness?
     *
     *      Recursive DFS
     *      Iterative DFS
     *      BFS
     *      Immutable copy
     *
     * □ Pattern boundary?
     *
     *      Do not use for subtree aggregation problems.
     */

    /**
     * ============================================================
     * Helper Methods For Tests
     * ============================================================
     */

    static TreeNode node(int value) {
        return new TreeNode(value);
    }

    static TreeNode node(int value, TreeNode left, TreeNode right) {
        return new TreeNode(value, left, right);
    }

    static List<Integer> levelOrder(TreeNode root) {

        List<Integer> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new ArrayDeque<>();

        queue.offer(root);

        while (!queue.isEmpty()) {

            TreeNode current = queue.poll();

            if (current == null) {
                result.add(null);
                continue;
            }

            result.add(current.val);

            if (current.left != null || current.right != null) {
                queue.offer(current.left);
                queue.offer(current.right);
            }
        }

        return result;
    }

    static void assertLevelOrder(TreeNode root, Integer... expected) {

        List<Integer> actual = levelOrder(root);

        assert actual.size() == expected.length
                : "Size mismatch. Expected "
                + expected.length
                + " but found "
                + actual.size();

        for (int i = 0; i < expected.length; i++) {
            assert Objects.equals(actual.get(i), expected[i])
                    : "Mismatch at index "
                    + i
                    + ". Expected "
                    + expected[i]
                    + " but found "
                    + actual.get(i);
        }
    }

    /**
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    public static void main(String[] args) {

        RecursiveDFS recursive = new RecursiveDFS();
        IterativeDFS iterative = new IterativeDFS();
        BFS bfs = new BFS();
        BruteForce brute = new BruteForce();

        /*
         * Happy Path
         *
         *             4
         *           /   \
         *          2     7
         *         / \   / \
         *        1  3  6  9
         *
         * Expected
         *
         *             4
         *           /   \
         *          7     2
         *         / \   / \
         *        9  6  3  1
         */
        TreeNode tree1 = node(
                4,
                node(2, node(1), node(3)),
                node(7, node(6), node(9))
        );

        recursive.invertTree(tree1);

        assertLevelOrder(
                tree1,
                4,
                7,
                2,
                9,
                6,
                3,
                1
        );

        /*
         * Verify iterative DFS.
         */
        TreeNode tree2 = node(
                4,
                node(2, node(1), node(3)),
                node(7, node(6), node(9))
        );

        iterative.invertTree(tree2);

        assertLevelOrder(
                tree2,
                4,
                7,
                2,
                9,
                6,
                3,
                1
        );

        /*
         * Verify BFS.
         */
        TreeNode tree3 = node(
                4,
                node(2, node(1), node(3)),
                node(7, node(6), node(9))
        );

        bfs.invertTree(tree3);

        assertLevelOrder(
                tree3,
                4,
                7,
                2,
                9,
                6,
                3,
                1
        );

        /*
         * Small tree.
         */
        TreeNode tree4 = node(
                2,
                node(1),
                node(3)
        );

        recursive.invertTree(tree4);

        assertLevelOrder(
                tree4,
                2,
                3,
                1
        );

        /*
         * Empty tree.
         */
        assert recursive.invertTree(null) == null
                : "Null tree should remain null.";

        /*
         * Single node.
         */
        TreeNode single = node(10);

        iterative.invertTree(single);

        assert single.val == 10;
        assert single.left == null;
        assert single.right == null;

        /*
         * Left-skew tree.
         *
         *      1
         *     /
         *    2
         *   /
         *  3
         *
         * becomes
         *
         * 1
         *  \
         *   2
         *    \
         *     3
         */
        TreeNode leftSkew = node(
                1,
                node(
                        2,
                        node(3),
                        null
                ),
                null
        );

        bfs.invertTree(leftSkew);

        assert leftSkew.left == null;
        assert leftSkew.right != null;
        assert leftSkew.right.val == 2;
        assert leftSkew.right.right != null;
        assert leftSkew.right.right.val == 3;

        /*
         * Right-skew tree.
         */
        TreeNode rightSkew = node(
                1,
                null,
                node(
                        2,
                        null,
                        node(3)
                )
        );

        iterative.invertTree(rightSkew);

        assert rightSkew.right == null;
        assert rightSkew.left != null;
        assert rightSkew.left.val == 2;
        assert rightSkew.left.left != null;
        assert rightSkew.left.left.val == 3;

        /*
         * Double inversion should recover
         * the original tree.
         */
        TreeNode reversible = node(
                8,
                node(4),
                node(12)
        );

        recursive.invertTree(reversible);
        recursive.invertTree(reversible);

        assertLevelOrder(
                reversible,
                8,
                4,
                12
        );

        /*
         * Brute-force version should not
         * mutate the original tree.
         */
        TreeNode original = node(
                5,
                node(2),
                node(8)
        );

        TreeNode mirrored = brute.invertTree(original);

        assertLevelOrder(
                original,
                5,
                2,
                8
        );

        assertLevelOrder(
                mirrored,
                5,
                8,
                2
        );

        /*
         * Root references should differ
         * because a new tree is created.
         */
        assert original != mirrored;

        /*
         * Interview trap:
         * Verify every node was swapped,
         * not just the root.
         */
        TreeNode trap = node(
                10,
                node(
                        5,
                        node(2),
                        node(7)
                ),
                node(
                        20,
                        node(15),
                        node(25)
                )
        );

        recursive.invertTree(trap);

        assert trap.left.val == 20;
        assert trap.right.val == 5;

        assert trap.left.left.val == 25;
        assert trap.left.right.val == 15;

        assert trap.right.left.val == 7;
        assert trap.right.right.val == 2;

        System.out.println("All assertions passed.");
    }
}

