package org.chijai.day6.session3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class KthSmallestElementInBST {

    /*
     * =========================================================================
     * 2. 📘 PRIMARY PROBLEM
     * =========================================================================
     *
     * Title:
     * Kth Smallest Element in a BST
     *
     * Difficulty:
     * Medium
     *
     * Tags:
     * Binary Search Tree
     * Binary Tree
     * DFS
     * Inorder Traversal
     * Stack
     *
     * Problem Statement:
     *
     * Given the root of a Binary Search Tree (BST) and an integer k,
     * return the kth smallest value (1-indexed) among all nodes.
     *
     * A BST satisfies:
     *
     * left subtree values  < node value
     * right subtree values > node value
     *
     * Therefore an inorder traversal visits nodes in ascending order.
     *
     * Constraints:
     *
     * 1 <= k <= n <= 10^4
     * 0 <= Node.val <= 10^4
     *
     * Examples:
     *
     * Example 1
     *
     *          3
     *         / \
     *        1   4
     *         \
     *          2
     *
     * inorder:
     * 1 2 3 4
     *
     * k = 1
     * answer = 1
     *
     * -------------------------------------------------------
     *
     * Example 2
     *
     *             5
     *            /
     *           3
     *          / \
     *         2   4
     *        /
     *       1
     *
     * inorder:
     * 1 2 3 4 5 6
     *
     * k = 3
     * answer = 3
     *
     * -------------------------------------------------------
     *
     * Follow-up:
     *
     * If insertions and deletions happen frequently while kth-smallest
     * queries are also frequent, augment every node with subtree size.
     * Then kth-smallest becomes an Order Statistic Tree query in O(height).
     *
     * Official:
     * https://leetcode.com/problems/kth-smallest-element-in-a-bst/
     */

    /*
     * =========================================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * =========================================================================
     *
     * Pattern
     * -------
     * Inorder Traversal of BST
     *
     * Archetype
     * ---------
     * Ordered traversal exploiting BST structure.
     *
     * Core Invariant
     * --------------
     * Every node is visited strictly after every smaller element
     * and before every larger element.
     *
     * Therefore:
     *
     * visit #1  -> smallest
     * visit #2  -> second smallest
     * ...
     * visit #k  -> kth smallest
     *
     * Why It Works
     * ------------
     * The BST ordering property transforms inorder traversal into
     * sorted-order traversal without explicitly sorting.
     *
     * Recognition Signals
     * -------------------
     * Look for:
     *
     * • BST
     * • kth smallest
     * • kth largest
     * • sorted order
     * • rank
     * • predecessor
     * • successor
     *
     * When To Use
     * -----------
     * Whenever the answer depends on sorted ordering of BST values.
     *
     * When NOT To Use
     * ---------------
     * General binary trees.
     *
     * Inorder of an arbitrary binary tree has no ordering guarantee.
     *
     * Comparison
     * ----------
     *
     * Full sort:
     * O(n log n)
     *
     * Copy inorder list:
     * O(n)
     *
     * Early-stop inorder:
     * O(H + k) average
     * O(n) worst case
     *
     * Order Statistic Tree:
     * O(log n) on balanced trees after augmentation.
     */

    /*
     * =========================================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * =========================================================================
     *
     * Mental Model
     * ------------
     *
     * Imagine every BST node already standing in a perfectly sorted queue.
     *
     * Inorder traversal simply walks through that invisible queue.
     *
     * We never create the queue.
     *
     * We merely visit nodes in exactly that order.
     *
     * ------------------------------------------------------------
     * Primary Invariant
     * ------------------------------------------------------------
     *
     * Before visiting a node,
     * every smaller value has already been visited.
     *
     * After visiting a node,
     * every larger value is still unvisited.
     *
     * This invariant is guaranteed by:
     *
     * Left
     * Node
     * Right
     *
     * ------------------------------------------------------------
     * Counting Invariant
     * ------------------------------------------------------------
     *
     * visitCount ==
     * number of smallest elements already processed.
     *
     * Therefore:
     *
     * visitCount == k
     *
     * immediately identifies the answer.
     *
     * No additional comparisons are required.
     *
     * ------------------------------------------------------------
     * Stack Invariant (Iterative)
     * ------------------------------------------------------------
     *
     * The stack stores ancestors whose:
     *
     * left subtree has been completely processed,
     * node itself has not yet been processed.
     *
     * Therefore the top of stack is always
     * the next inorder node.
     *
     * This single invariant explains:
     *
     * push
     * pop
     * move right
     *
     * ------------------------------------------------------------
     * Recursive Invariant
     * ------------------------------------------------------------
     *
     * Every recursive frame promises:
     *
     * "I will completely process this subtree in sorted order."
     *
     * The parent never needs to know internal details.
     *
     * ------------------------------------------------------------
     * Variable Meaning
     * ------------------------------------------------------------
     *
     * current
     * -------
     * Current traversal pointer.
     *
     * stack
     * -----
     * Ancestors waiting to be visited.
     *
     * k
     * -
     * Remaining nodes before reaching answer.
     *
     * When k becomes zero,
     * current node is the answer.
     *
     * ------------------------------------------------------------
     * Allowed State Transitions
     * ------------------------------------------------------------
     *
     * current -> left
     *
     * push ancestor
     *
     * pop ancestor
     *
     * visit node
     *
     * current -> right
     *
     * These transitions preserve sorted order.
     *
     * ------------------------------------------------------------
     * Forbidden Moves
     * ------------------------------------------------------------
     *
     * Visit before entire left subtree.
     *
     * Ignore left subtree.
     *
     * Visit right before node.
     *
     * Skip decrementing k after visit.
     *
     * Continue traversal after answer if early stopping
     * is intended.
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * Recursive:
     *
     * null subtree.
     *
     * Iterative:
     *
     * current == null
     * AND
     * stack empty.
     *
     * Early-stop version:
     *
     * k == 0.
     *
     * ------------------------------------------------------------
     * Why Naive Solutions Fail
     * ------------------------------------------------------------
     *
     * Sorting all values ignores BST ordering.
     *
     * Priority queues waste memory.
     *
     * BFS order has no relationship with sorted order.
     *
     * DFS preorder/postorder destroy rank ordering.
     */

    /*
     * =========================================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * =========================================================================
     *
     * Mistake 1
     * ---------
     * Using preorder traversal.
     *
     * Why it seems correct:
     *
     * Every node is still visited once.
     *
     * Violated Invariant:
     *
     * Smaller elements are not guaranteed first.
     *
     * Counterexample:
     *
     *      2
     *     /
     *    1
     *
     * preorder:
     * 2 1
     *
     * sorted:
     * 1 2
     *
     * ----------------------------------------
     *
     * Mistake 2
     * ---------
     * Forgetting to decrement k exactly
     * when visiting the node.
     *
     * Why it seems correct:
     *
     * Traversal still completes.
     *
     * Broken invariant:
     *
     * visitCount no longer equals processed rank.
     *
     * ----------------------------------------
     *
     * Mistake 3
     * ---------
     * Decrement before left subtree.
     *
     * Rank shifts by one.
     *
     * ----------------------------------------
     *
     * Mistake 4
     * ---------
     * Visiting node twice because right transition
     * is implemented incorrectly.
     *
     * Symptom:
     *
     * Duplicate ranks.
     *
     * ----------------------------------------
     *
     * Interview Trap
     * --------------
     *
     * Candidate memorizes inorder
     * but cannot explain why it produces sorted order.
     *
     * Strong answer:
     *
     * "BST guarantees every left value is smaller and every right
     * value is larger, therefore Left-Node-Right is identical to
     * reading the sorted sequence."
     */

    /*
     * =========================================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * =========================================================================
     *
     * Mechanical Typing Order
     *
     * 1.
     * Create stack.
     *
     * 2.
     * current = root.
     *
     * 3.
     * Outer loop:
     *
     * while(current != null || !stack.isEmpty())
     *
     * 4.
     * Push entire left chain.
     *
     * 5.
     * Pop.
     *
     * 6.
     * Visit node.
     *
     * 7.
     * Decrement k.
     *
     * 8.
     * If k == 0
     * return value.
     *
     * 9.
     * Move to right subtree.
     *
     * 10.
     * Continue.
     *
     * Debugging Flow
     * --------------
     *
     * Wrong order?
     * Verify left chain.
     *
     * Wrong answer?
     * Verify decrement timing.
     *
     * Infinite loop?
     * Verify current = current.right.
     */

    /*
     * =========================================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * =========================================================================
     *
     * stack
     * current=root
     *
     * while current or stack
     *     go left
     *     visit
     *     decrement k
     *     if k==0 return
     *     go right
     */

    static class TreeNode {
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

    /*
     * =========================================================================
     * 6. SOLUTION CLASSES
     * =========================================================================
     */

    static class BruteForce {

        /*
         * Idea
         * ----
         * Store complete inorder traversal into a list.
         *
         * Invariant
         * ---------
         * List remains fully sorted because inorder of BST
         * is sorted.
         *
         * Limitation
         * ----------
         * Visits every node even when k is very small.
         *
         * Complexity
         * ----------
         * Time : O(n)
         * Space: O(n)
         *
         * Interview Usefulness
         * --------------------
         * Excellent stepping stone before optimizing.
         */

        public int kthSmallest(TreeNode root, int k) {
            List<Integer> inorder = new ArrayList<>();
            dfs(root, inorder);
            return inorder.get(k - 1);
        }

        private void dfs(TreeNode node, List<Integer> inorder) {
            if (node == null) {
                return;
            }

            dfs(node.left, inorder);

            // Invariant: values are appended in ascending order.
            inorder.add(node.val);

            dfs(node.right, inorder);
        }
    }

    static class Improved {


        /*
         * Idea
         * ----
         * Perform inorder traversal recursively, but stop immediately
         * after visiting the kth node instead of storing the entire order.
         *
         * Invariant
         * ---------
         * visitedCount always equals the number of smallest elements
         * already processed.
         *
         * Improvement
         * -----------
         * Eliminates the O(n) list.
         * Can terminate early when k is reached.
         *
         * Complexity
         * ----------
         * Time : O(H + k) average, O(n) worst case
         * Space: O(H) recursion stack
         *
         * Interview Usefulness
         * --------------------
         * Demonstrates understanding that inorder order is enough;
         * materializing the traversal is unnecessary.
         */

        private int remaining;
        private int answer;
        private boolean found;

        public int kthSmallest(TreeNode root, int k) {
            remaining = k;
            found = false;
            inorder(root);
            return answer;
        }

        private void inorder(TreeNode node) {

            if (node == null || found) {
                return;
            }

            inorder(node.left);

            if (found) {
                return;
            }

            // Invariant: every smaller value has already been visited.
            remaining--;

            if (remaining == 0) {
                answer = node.val;
                found = true;
                return;
            }

            inorder(node.right);
        }
    }

    static class Optimal {

        /*
         * Idea
         * ----
         * Simulate recursive inorder traversal using an explicit stack.
         *
         * The traversal stops exactly when the kth node is visited.
         *
         * Core Invariant
         * --------------
         * Every node inside the stack has:
         *
         * 1. its left subtree completely processed
         * 2. itself not yet processed
         *
         * Therefore the top of the stack is always the next
         * inorder node.
         *
         * Correctness
         * -----------
         * Left subtree is exhausted before visiting a node.
         * Node is visited before exploring its right subtree.
         * Thus visit order is exactly the sorted order.
         *
         * Complexity
         * ----------
         * Time : O(H + k) average
         * Time : O(n) worst case
         * Space: O(H)
         *
         * Interview Usefulness
         * --------------------
         * Preferred implementation because:
         *
         * • avoids recursion depth limits
         * • exposes traversal mechanics clearly
         * • naturally supports early stopping
         */

        public int kthSmallest(TreeNode root, int k) {

            Deque<TreeNode> stack = new ArrayDeque<>();
            TreeNode current = root;

            while (current != null || !stack.isEmpty()) {

                // Invariant:
                // Every push postpones visiting until all smaller
                // elements have been processed.
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }

                current = stack.pop();

                // Invariant:
                // current is now the smallest unvisited node.
                k--;

                if (k == 0) {
                    return current.val;
                }

                // Discard the processed node forever.
                // Remaining candidates exist only in the right subtree
                // or among deferred ancestors.
                current = current.right;
            }

            throw new IllegalArgumentException(
                    "Input guarantees 1 <= k <= number of nodes.");
        }
    }

/*
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * Explain the Invariant
 * ---------------------
 *
 * The BST property guarantees:
 *
 * left subtree
 * <
 * node
 * <
 * right subtree.
 *
 * Therefore inorder traversal naturally enumerates
 * elements in ascending order.
 *
 * ------------------------------------------------------------
 * Explain the Search Target
 * ------------------------------------------------------------
 *
 * We are not searching by value.
 *
 * We are searching by visitation rank.
 *
 * The kth visit is exactly the kth smallest.
 *
 * ------------------------------------------------------------
 * Explain the Discard Rule
 * ------------------------------------------------------------
 *
 * Once a node has been visited,
 * neither the node nor its left subtree
 * can ever contain the answer again.
 *
 * They are permanently discarded.
 *
 * ------------------------------------------------------------
 * Explain Correctness
 * ------------------------------------------------------------
 *
 * Every node is visited only after all smaller nodes.
 *
 * Therefore visit number equals sorted rank.
 *
 * When visit number reaches k,
 * that node must be the kth smallest.
 *
 * ------------------------------------------------------------
 * Explain Termination
 * ------------------------------------------------------------
 *
 * The traversal terminates immediately after visiting
 * the kth node.
 *
 * No later node can change the answer because every later
 * node is strictly larger.
 *
 * ------------------------------------------------------------
 * In-place Feasibility
 * --------------------
 *
 * No.
 *
 * Traversal still requires either:
 *
 * recursion stack
 * or
 * explicit stack.
 *
 * Morris Traversal can reduce auxiliary space to O(1),
 * but temporarily modifies tree links.
 *
 * ------------------------------------------------------------
 * Streaming Feasibility
 * ---------------------
 *
 * Yes.
 *
 * Nodes are processed one at a time.
 *
 * The algorithm never requires future values.
 *
 * ------------------------------------------------------------
 * When NOT To Use
 * ------------------------------------------------------------
 *
 * If the tree is not a BST,
 * inorder traversal is not sorted.
 *
 * If frequent insert/delete operations are followed by
 * many kth-order queries,
 * augment nodes with subtree sizes instead.
 */

/*
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 * -------
 * BST + kth/order/rank.
 *
 * Pattern
 * -------
 * Inorder traversal.
 *
 * Invariant
 * ---------
 * Inorder visits nodes in increasing order.
 *
 * Search Target
 * -------------
 * kth visitation.
 *
 * Discard Rule
 * ------------
 * Visited node and its left subtree are finished forever.
 *
 * Common Trap
 * -----------
 * Decrementing k before visiting the node.
 *
 * Edge Cases
 * ----------
 * Single node.
 * k = 1.
 * Completely skewed tree.
 * Balanced tree.
 *
 * One-liner
 * ---------
 * kth smallest equals kth inorder visit.
 *
 * Re-derivation Cue
 * -----------------
 * BST already stores sorted order implicitly.
 */

    /*
     * =========================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =========================================================================
     *
     * ------------------------------------------------------------
     * Variation 1
     * kth Largest
     * ------------------------------------------------------------
     *
     * Pattern
     * -------
     * Reverse inorder traversal.
     *
     * Right
     * Node
     * Left
     *
     * Invariant
     * ---------
     * Every larger element is processed before the current node.
     *
     * Reasoning Change
     * ----------------
     * Only traversal direction changes.
     *
     * Correctness remains identical.
     *
     * ------------------------------------------------------------
     * Variation 2
     * BST Iterator
     * ------------------------------------------------------------
     *
     * Pattern
     * -------
     * Lazy inorder traversal.
     *
     * Invariant
     * ---------
     * Stack top is always the next smallest element.
     *
     * Instead of stopping after k,
     * expose:
     *
     * hasNext()
     * next()
     *
     * ------------------------------------------------------------
     * Variation 3
     * Validate BST
     * ------------------------------------------------------------
     *
     * Pattern
     * -------
     * Inorder traversal.
     *
     * Invariant
     * ---------
     * Previously visited value must always be smaller
     * than the current value.
     *
     * Pattern changes from:
     *
     * counting
     *
     * to
     *
     * ordering verification.
     *
     * ------------------------------------------------------------
     * Variation 4
     * Recover BST
     * ------------------------------------------------------------
     *
     * Pattern
     * -------
     * Inorder traversal.
     *
     * Invariant
     * ---------
     * Sorted order should never decrease.
     *
     * First inversion identifies one misplaced node.
     * Second inversion identifies the other.
     *
     * ------------------------------------------------------------
     * Variation 5
     * Morris Traversal
     * ------------------------------------------------------------
     *
     * Pattern
     * -------
     * Threaded inorder traversal.
     *
     * Invariant
     * ---------
     * Temporary links always restore the original tree.
     *
     * Advantages
     * ----------
     * O(1) auxiliary space.
     *
     * Drawback
     * --------
     * More difficult to implement correctly under interview pressure.
     *
     * ------------------------------------------------------------
     * Variation 6
     * Frequently Updated BST
     * ------------------------------------------------------------
     *
     * Follow-up Solution
     * ------------------
     *
     * Store:
     *
     * subtreeSize
     *
     * inside every node.
     *
     * During insertion/deletion,
     * update subtree sizes while returning.
     *
     * Query Logic
     * -----------
     *
     * leftSize =
     * size(left subtree)
     *
     * if k <= leftSize
     *     go left
     *
     * else if k == leftSize + 1
     *     current node
     *
     * else
     *     k -= leftSize + 1
     *     go right
     *
     * Balanced Tree Complexity
     * ------------------------
     *
     * Insert:
     * O(log n)
     *
     * Delete:
     * O(log n)
     *
     * kth Query:
     * O(log n)
     *
     * This is the standard Order Statistic Tree idea.
     *
     * ------------------------------------------------------------
     * Pattern Boundary
     * ------------------------------------------------------------
     *
     * If ordering information does not exist,
     * inorder traversal provides no ranking guarantee.
     *
     * Examples:
     *
     * arbitrary binary tree
     * heap
     * graph
     */

    /*
     * =========================================================================
     * 🧠 MASTERY CHECKLIST
     * =========================================================================
     *
     * □ What is the Pattern?
     *
     * Inorder traversal of a BST.
     *
     * ------------------------------------------------------------
     *
     * □ What is the primary Invariant?
     *
     * Every visited node has already seen every smaller value.
     *
     * ------------------------------------------------------------
     *
     * □ What is the Search Target?
     *
     * kth inorder visitation.
     *
     * ------------------------------------------------------------
     *
     * □ What is the Discard Rule?
     *
     * After visiting a node,
     * its left subtree and itself are permanently finished.
     *
     * ------------------------------------------------------------
     *
     * □ Why is the answer correct?
     *
     * Because inorder traversal of a BST is exactly
     * the sorted order.
     *
     * ------------------------------------------------------------
     *
     * □ Why does the algorithm terminate?
     *
     * Either:
     *
     * • kth node is found
     *
     * or
     *
     * • traversal finishes.
     *
     * ------------------------------------------------------------
     *
     * □ Why does the naive approach fail?
     *
     * It ignores the ordering already encoded by the BST.
     *
     * ------------------------------------------------------------
     *
     * □ Which edge cases should be verified?
     *
     * • one node
     * • smallest element
     * • largest element
     * • skewed tree
     * • balanced tree
     * • k equals number of nodes
     *
     * ------------------------------------------------------------
     *
     * □ Can I debug this under pressure?
     *
     * Verify:
     *
     * 1. push left chain
     * 2. pop exactly once
     * 3. decrement exactly on visit
     * 4. move to right subtree
     *
     * ------------------------------------------------------------
     *
     * □ Am I ready for variants?
     *
     * Reverse inorder
     * BST iterator
     * Validate BST
     * Recover BST
     * Morris traversal
     * Order Statistic Tree
     */

    private static TreeNode n(int value) {
        return new TreeNode(value);
    }

    private static TreeNode n(int value, TreeNode left, TreeNode right) {
        return new TreeNode(value, left, right);
    }

    private static TreeNode exampleOneTree() {

        return n(
                3,
                n(
                        1,
                        null,
                        n(2)
                ),
                n(4)
        );
    }

    private static TreeNode exampleTwoTree() {

        return n(
                5,
                n(
                        3,
                        n(
                                2,
                                n(1),
                                null
                        ),
                        n(4)
                ),
                n(6)
        );
    }

    private static TreeNode singleNodeTree() {
        return n(42);
    }

    private static TreeNode leftSkewedTree() {

        return n(
                5,
                n(
                        4,
                        n(
                                3,
                                n(
                                        2,
                                        n(1),
                                        null
                                ),
                                null
                        ),
                        null
                ),
                null
        );
    }

    private static TreeNode rightSkewedTree() {

        return n(
                1,
                null,
                n(
                        2,
                        null,
                        n(
                                3,
                                null,
                                n(
                                        4,
                                        null,
                                        n(5)
                                )
                        )
                )
        );
    }

    public static void main(String[] args) {

        BruteForce brute = new BruteForce();
        Improved improved = new Improved();
        Optimal optimal = new Optimal();

        /*
         * Representative Example 1
         *
         * inorder = 1 2 3 4
         *
         * kth(1) = 1
         */
        assert brute.kthSmallest(exampleOneTree(), 1) == 1
                : "Brute force failed on representative example 1.";

        assert improved.kthSmallest(exampleOneTree(), 1) == 1
                : "Recursive early-stop failed on representative example 1.";

        assert optimal.kthSmallest(exampleOneTree(), 1) == 1
                : "Iterative inorder failed on representative example 1.";

        /*
         * Representative Example 2
         *
         * inorder = 1 2 3 4 5 6
         *
         * kth(3) = 3
         */
        assert brute.kthSmallest(exampleTwoTree(), 3) == 3
                : "Brute force failed on representative example 2.";

        assert improved.kthSmallest(exampleTwoTree(), 3) == 3
                : "Recursive early-stop failed on representative example 2.";

        assert optimal.kthSmallest(exampleTwoTree(), 3) == 3
                : "Iterative inorder failed on representative example 2.";

        /*
         * Edge Case
         *
         * Single node tree.
         */
        assert brute.kthSmallest(singleNodeTree(), 1) == 42
                : "Single-node tree failed.";

        assert improved.kthSmallest(singleNodeTree(), 1) == 42
                : "Recursive single-node case failed.";

        assert optimal.kthSmallest(singleNodeTree(), 1) == 42
                : "Iterative single-node case failed.";

        /*
         * Boundary Condition
         *
         * Smallest element in a left-skewed BST.
         */
        assert optimal.kthSmallest(leftSkewedTree(), 1) == 1
                : "Failed to find smallest element in left-skewed tree.";

        /*
         * Boundary Condition
         *
         * Largest element by asking for the final rank.
         */
        assert optimal.kthSmallest(leftSkewedTree(), 5) == 5
                : "Failed to find largest element.";

        /*
         * Interview Trap
         *
         * Right-skewed trees should still produce sorted order.
         */
        assert optimal.kthSmallest(rightSkewedTree(), 4) == 4
                : "Right-skewed traversal ordering is incorrect.";

        /*
         * Verify every rank in the representative tree.
         *
         * inorder = 1 2 3 4 5 6
         */
        int[] expected = {1, 2, 3, 4, 5, 6};

        for (int i = 0; i < expected.length; i++) {

            int k = i + 1;

            assert brute.kthSmallest(exampleTwoTree(), k) == expected[i]
                    : "Brute force rank verification failed for k = " + k;

            assert improved.kthSmallest(exampleTwoTree(), k) == expected[i]
                    : "Recursive rank verification failed for k = " + k;

            assert optimal.kthSmallest(exampleTwoTree(), k) == expected[i]
                    : "Iterative rank verification failed for k = " + k;
        }

        /*
         * Cross-validation.
         *
         * Every implementation should agree on every valid rank.
         */
        TreeNode validationTree = exampleTwoTree();

        for (int k = 1; k <= 6; k++) {

            int a = brute.kthSmallest(validationTree, k);
            int b = improved.kthSmallest(validationTree, k);
            int c = optimal.kthSmallest(validationTree, k);

            assert a == b : "Brute and recursive implementations disagree.";
            assert b == c : "Recursive and iterative implementations disagree.";
        }

        System.out.println("All assertions passed. Enable assertions with -ea during execution.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/