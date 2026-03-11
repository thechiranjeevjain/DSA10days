package org.chijai.day6.session2;

import java.util.*;

/**
 * ====================================================================================================
 *                                   Recover Binary Search Tree
 * ====================================================================================================
 *
 * 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
 *
 * LeetCode Link:
 * https://leetcode.com/problems/recover-binary-search-tree/
 *
 * Difficulty:
 * Hard
 *
 * Tags:
 * Tree, Depth-First Search, Binary Search Tree, Binary Tree
 *
 * ----------------------------------------------------------------------------------------------------
 *
 * You are given the root of a binary search tree (BST), where the values of exactly two nodes
 * of the tree were swapped by mistake. Recover the tree without changing its structure.
 *
 * Example 1:
 *
 * Input: root = [1,3,null,null,2]
 * Output: [3,1,null,null,2]
 * Explanation:
 * 3 cannot be a left child of 1 because 3 > 1.
 * Swapping 1 and 3 makes the BST valid.
 *
 * Example 2:
 *
 * Input: root = [3,1,4,null,null,2]
 * Output: [2,1,4,null,null,3]
 * Explanation:
 * 2 cannot be in the right subtree of 3 because 2 < 3.
 * Swapping 2 and 3 makes the BST valid.
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [2, 1000].
 * -2^31 <= Node.val <= 2^31 - 1
 *
 * Follow up:
 * A solution using O(n) space is pretty straight-forward.
 * Could you devise a constant O(1) space solution?
 *
 * ====================================================================================================
 *
 * 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
 *
 * 🔵 Pattern Name:
 * Inorder Invariant Violation Detection in BST
 *
 * 🔵 Problem Archetype:
 * Detect structural ordering violation in a globally sorted traversal.
 *
 * 🟢 Core Invariant (MANDATORY):
 * Inorder traversal of a valid BST produces a strictly increasing sequence.
 *
 * 🟡 Why this invariant makes the pattern work:
 * If exactly two nodes are swapped, the inorder sequence will contain exactly
 * two ordering violations (or one if adjacent).
 *
 * Instead of fixing structure, we fix values by detecting where sorted order breaks.
 *
 * 🔵 When this pattern applies:
 * - BST validation problems
 * - BST recovery
 * - Detecting misplaced elements in sorted structure
 *
 * 🔵 Pattern recognition signals:
 * - “Exactly two nodes swapped”
 * - BST property violated
 * - Structure must not change
 *
 * 🔵 How this differs from:
 * - General tree repair (no ordering invariant)
 * - Heap violations (local property)
 * - Sorting array (global reordering)
 *
 * ====================================================================================================
 *
 * 🟢 MENTAL MODEL & INVARIANTS (CANONICAL SOURCE OF TRUTH)
 *
 * 🔵 Mental Model:
 *
 * Think of BST as a sorted array revealed through inorder traversal.
 *
 * If two values are swapped:
 * The sorted order will break.
 *
 * There are only TWO possibilities:
 *
 * Case 1: Non-adjacent swap
 * Example:
 * Original: 1 2 3 4 5
 * Swapped: 1 4 3 2 5
 * Violations:
 *   4 > 3
 *   3 > 2
 *
 * Case 2: Adjacent swap
 * Example:
 * Original: 1 2 3 4
 * Swapped: 1 3 2 4
 * Violation:
 *   3 > 2
 *
 * 🟢 Invariants:
 *
 * 1. During inorder traversal:
 *      prev.val < curr.val  (for valid BST)
 *
 * 2. First violation:
 *      prev > curr
 *      → prev is first misplaced node
 *
 * 3. Second violation:
 *      prev > curr
 *      → curr is second misplaced node
 *
 * 4. After traversal:
 *      Swap first and second values
 *
 * 🔵 State Meaning:
 *
 * prev   → previous node in inorder traversal
 * first  → first node violating sorted order
 * second → second node violating sorted order
 *
 * 🔵 Allowed Moves:
 * - Inorder traversal (recursive / iterative / Morris)
 * - Track previous node
 *
 * 🔴 Forbidden Moves:
 * - Rebuilding tree
 * - Changing structure
 * - Sorting values then rebuilding
 *
 * 🔵 Termination Logic:
 * Traverse entire tree once.
 * Swap detected nodes.
 *
 * 🔴 Why common alternatives fail:
 * Sorting values and reinserting breaks structure constraint.
 *
 * ====================================================================================================
 *
 * 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
 *
 * 🔴 Wrong Approach 1:
 * Extract inorder into list → sort list → reassign values in inorder order.
 *
 * Why it seems correct:
 * Because inorder of BST is sorted.
 *
 * Why it violates constraint:
 * Uses O(n) extra space.
 *
 * 🔴 Wrong Approach 2:
 * Only detect first violation and swap those two immediately.
 *
 * Minimal Counterexample:
 * 1 4 3 2 5
 * If you swap 4 and 3, still incorrect.
 *
 * 🔴 Interviewer Trap:
 * Thinking there is always only one inversion.
 *
 * Truth:
 * There can be two inversions.
 *
 * ====================================================================================================
 *
 * PRIMARY PROBLEM — SOLUTION CLASSES
 *
 * STRICT ORDER:
 * 1. Brute Force
 * 2. Improved
 * 3. Optimal (in next part)
 *
 * ====================================================================================================
 */
public class RecoverBST {

    // =============================================================================================
    // Basic TreeNode Definition
    // =============================================================================================
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    // =============================================================================================
    // 1️⃣ BRUTE FORCE SOLUTION
    // =============================================================================================
    static class BruteForceSolution {

        /*
         * 🔵 Core Idea:
         * Perform inorder traversal.
         * Store nodes in list.
         * Sort values.
         * Reassign values.
         *
         * 🟢 Enforces invariant:
         * Sorted inorder list.
         *
         * 🔴 Limitation:
         * Uses O(n) extra space.
         *
         * Time Complexity: O(n log n)
         * Space Complexity: O(n)
         *
         * 🔴 Interview Preference:
         * Not preferred due to space and sorting.
         */

        public void recoverTree(TreeNode root) {
            List<TreeNode> nodes = new ArrayList<>();
            inorder(root, nodes);

            List<Integer> values = new ArrayList<>();
            for (TreeNode node : nodes) {
                values.add(node.val);
            }

            Collections.sort(values);

            for (int i = 0; i < nodes.size(); i++) {
                nodes.get(i).val = values.get(i);
            }
        }

        private void inorder(TreeNode root, List<TreeNode> nodes) {
            if (root == null) return;
            inorder(root.left, nodes);
            nodes.add(root);
            inorder(root.right, nodes);
        }
    }

    // =============================================================================================
    // 2️⃣ IMPROVED SOLUTION (O(n) space, no sorting)
    // =============================================================================================
    static class ImprovedSolution {

        /*
         * 🔵 Core Idea:
         * During inorder traversal:
         * Detect violations directly.
         *
         * 🟢 Invariant enforced:
         * prev.val < curr.val
         *
         * First violation:
         *     first = prev
         *     second = curr
         *
         * Second violation:
         *     second = curr
         *
         * After traversal:
         * Swap first and second.
         *
         * Time Complexity: O(n)
         * Space Complexity: O(n) recursion stack
         *
         * 🟣 Interview:
         * Good explanation baseline before Morris.
         */

        private TreeNode first = null;
        private TreeNode second = null;
        private TreeNode prev = null;

        public void recoverTree(TreeNode root) {
            inorder(root);

            if (first != null && second != null) {
                int temp = first.val;
                first.val = second.val;
                second.val = temp;
            }
        }

        private void inorder(TreeNode root) {
            if (root == null) return;

            inorder(root.left);

            if (prev != null && prev.val > root.val) {

                if (first == null) {
                    first = prev;
                }

                second = root;
            }

            prev = root;

            inorder(root.right);
        }
    }

    // =============================================================================================
    // 3️⃣ OPTIMAL SOLUTION — MORRIS TRAVERSAL (O(1) SPACE)
    // =============================================================================================
    static class OptimalSolution {

        /*
         * 🔵 Core Idea:
         * Use Morris Inorder Traversal to achieve O(1) space.
         *
         * Morris traversal temporarily modifies tree using threaded links.
         *
         * 🟢 Invariant enforced:
         * Inorder traversal must produce strictly increasing sequence.
         *
         * 🔵 Why Morris works:
         * It simulates recursion stack by creating temporary right pointers
         * from inorder predecessor to current node.
         *
         * 🔴 Critical:
         * We MUST restore tree structure after traversal.
         *
         * Time Complexity: O(n)
         * Space Complexity: O(1)
         *
         * 🟣 Interview Preference:
         * This is the follow-up optimal answer.
         */

        public void recoverTree(TreeNode root) {

            TreeNode first = null;
            TreeNode second = null;
            TreeNode prev = null;

            TreeNode curr = root;

            while (curr != null) {

                if (curr.left == null) {

                    // VISIT NODE
                    if (prev != null && prev.val > curr.val) {
                        if (first == null) {
                            first = prev;
                        }
                        second = curr;
                    }

                    prev = curr;
                    curr = curr.right;

                } else {

                    // Find inorder predecessor
                    TreeNode predecessor = curr.left;
                    while (predecessor.right != null && predecessor.right != curr) {
                        predecessor = predecessor.right;
                    }

                    if (predecessor.right == null) {
                        // Create thread
                        predecessor.right = curr;
                        curr = curr.left;
                    } else {
                        // Remove thread
                        predecessor.right = null;

                        // VISIT NODE
                        if (prev != null && prev.val > curr.val) {
                            if (first == null) {
                                first = prev;
                            }
                            second = curr;
                        }

                        prev = curr;
                        curr = curr.right;
                    }
                }
            }

            if (first != null && second != null) {
                int temp = first.val;
                first.val = second.val;
                second.val = temp;
            }
        }
    }

    // =============================================================================================
    // 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
    // =============================================================================================

    /*
     * 🟣 State the invariant:
     * Inorder traversal of BST must be strictly increasing.
     *
     * 🟣 Discard Logic:
     * Whenever prev.val > curr.val, sorted invariant is violated.
     *
     * First violation:
     *     first = prev
     *     second = curr
     *
     * Second violation:
     *     second = curr
     *
     * After traversal:
     * Swap first and second.
     *
     * 🟣 Why correctness guaranteed:
     * Only two nodes are swapped.
     * Therefore inorder sequence has at most two violations.
     *
     * 🟣 What breaks if changed:
     * If more than two nodes swapped → invariant detection insufficient.
     *
     * 🟣 In-place feasibility:
     * Yes using Morris traversal.
     *
     * 🟣 When NOT to use this pattern:
     * - If structure must be changed.
     * - If more than two nodes corrupted.
     */

    // =============================================================================================
    // 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
    // =============================================================================================

    /*
     * 🔵 Invariant-preserving changes:
     * - Use iterative stack instead of recursion.
     * - Use Morris for O(1) space.
     *
     * 🔵 Reasoning-only change:
     * If asked only to validate BST → detect any violation and return false.
     *
     * 🔴 Pattern-break signals:
     * - More than two nodes corrupted.
     * - Duplicates allowed (must adjust invariant to non-decreasing).
     */

    // =============================================================================================
    // ⚫ REINFORCEMENT PROBLEM 1
    // Validate Binary Search Tree
    // =============================================================================================

    /*
     * LeetCode:
     * https://leetcode.com/problems/validate-binary-search-tree/
     *
     * Difficulty:
     * Medium
     *
     * Problem:
     * Given the root of a binary tree, determine if it is a valid BST.
     *
     * A valid BST:
     * - Left subtree contains only nodes with keys less than node's key.
     * - Right subtree contains only nodes with keys greater than node's key.
     * - Both subtrees must also be BST.
     *
     * 🟢 Invariant:
     * Inorder traversal must produce strictly increasing sequence.
     *
     * Time: O(n)
     * Space: O(n) or O(1) Morris
     */

    static class ValidateBST {

        private TreeNode prev = null;

        public boolean isValidBST(TreeNode root) {
            prev = null;
            return inorder(root);
        }

        private boolean inorder(TreeNode root) {
            if (root == null) return true;

            if (!inorder(root.left)) return false;

            if (prev != null && prev.val >= root.val) return false;

            prev = root;

            return inorder(root.right);
        }
    }

    // =============================================================================================
    // ⚫ REINFORCEMENT PROBLEM 2
    // Kth Smallest Element in BST
    // =============================================================================================

    /*
     * LeetCode:
     * https://leetcode.com/problems/kth-smallest-element-in-a-bst/
     *
     * Difficulty:
     * Medium
     *
     * Problem:
     * Given root of BST and integer k, return kth smallest element.
     *
     * 🟢 Invariant:
     * Inorder traversal gives sorted order.
     *
     * Time: O(n)
     * Space: O(n) recursion
     */

    static class KthSmallest {

        private int count = 0;
        private int result = -1;

        public int kthSmallest(TreeNode root, int k) {
            count = 0;
            result = -1;
            inorder(root, k);
            return result;
        }

        private void inorder(TreeNode root, int k) {
            if (root == null) return;

            inorder(root.left, k);

            count++;
            if (count == k) {
                result = root.val;
                return;
            }

            inorder(root.right, k);
        }
    }

    // =============================================================================================
    // ⚫ REINFORCEMENT PROBLEM 3
    // Convert BST to Greater Tree
    // =============================================================================================

    /*
     * LeetCode:
     * https://leetcode.com/problems/convert-bst-to-greater-tree/
     *
     * Difficulty:
     * Medium
     *
     * Problem:
     * Given root of BST, convert it to Greater Tree such that every node
     * contains sum of all greater or equal keys.
     *
     * 🟢 Invariant:
     * Reverse inorder traversal gives decreasing order.
     *
     * Time: O(n)
     * Space: O(n)
     */

    static class ConvertBSTToGreater {

        private int sum = 0;

        public TreeNode convertBST(TreeNode root) {
            reverseInorder(root);
            return root;
        }

        private void reverseInorder(TreeNode root) {
            if (root == null) return;

            reverseInorder(root.right);

            sum += root.val;
            root.val = sum;

            reverseInorder(root.left);
        }
    }

    // =============================================================================================
    // 🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
    // =============================================================================================

    // ---------------------------------------------------------------------------------------------
    // Related Problem 1: Binary Search Tree Iterator
    // ---------------------------------------------------------------------------------------------
    /*
     * LeetCode:
     * https://leetcode.com/problems/binary-search-tree-iterator/
     *
     * Difficulty:
     * Medium
     *
     * Problem Statement:
     * Implement the BSTIterator class that represents an iterator over the inorder traversal
     * of a binary search tree (BST):
     *
     * BSTIterator(TreeNode root) Initializes an object of the BSTIterator class.
     * The root of the BST is given as part of the constructor.
     * The pointer should be initialized to a non-existent number smaller than any element in the BST.
     *
     * boolean hasNext() Returns true if there exists a number in the traversal
     * to the right of the pointer, otherwise returns false.
     *
     * int next() Moves the pointer to the right, then returns the number at the pointer.
     *
     * Follow up:
     * Could you implement next() and hasNext() to run in average O(1) time and use O(h) memory?
     *
     * 🟢 Invariant:
     * Inorder traversal of BST produces strictly increasing sequence.
     *
     * ⚫ Pattern Mapping:
     * Instead of collecting entire inorder, lazily simulate it using stack.
     */

    static class BSTIterator {

        private Stack<TreeNode> stack = new Stack<>();

        public BSTIterator(TreeNode root) {
            pushLeft(root);
        }

        private void pushLeft(TreeNode node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        public int next() {
            TreeNode node = stack.pop();
            if (node.right != null) {
                pushLeft(node.right);
            }
            return node.val;
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Related Problem 2: Minimum Absolute Difference in BST
    // ---------------------------------------------------------------------------------------------
    /*
     * LeetCode:
     * https://leetcode.com/problems/minimum-absolute-difference-in-bst/
     *
     * Difficulty:
     * Easy
     *
     * Problem:
     * Given the root of a BST, return the minimum absolute difference between
     * values of any two different nodes.
     *
     * 🟢 Invariant:
     * Inorder traversal produces sorted order.
     * Minimum difference must occur between adjacent elements in sorted sequence.
     *
     * 🔴 Trap:
     * Comparing all pairs is O(n^2) — unnecessary.
     */

    static class MinAbsoluteDifference {

        private Integer prev = null;
        private int minDiff = Integer.MAX_VALUE;

        public int getMinimumDifference(TreeNode root) {
            prev = null;
            minDiff = Integer.MAX_VALUE;
            inorder(root);
            return minDiff;
        }

        private void inorder(TreeNode root) {
            if (root == null) return;

            inorder(root.left);

            if (prev != null) {
                minDiff = Math.min(minDiff, root.val - prev);
            }

            prev = root.val;

            inorder(root.right);
        }
    }

    // =============================================================================================
    // 🟢 LEARNING VERIFICATION
    // =============================================================================================

    /*
     * 1️⃣ Invariant Recall Without Code:
     * Inorder traversal of BST must be strictly increasing.
     *
     * 2️⃣ Naive Failure Explanation:
     * Swapping first detected violation immediately may fail
     * when nodes are non-adjacent in inorder.
     *
     * 3️⃣ Debugging Readiness:
     * If still invalid after swap:
     * - Check if both violations detected.
     * - Check if prev updated correctly.
     *
     * 4️⃣ Pattern Recognition Signals:
     * - Exactly two nodes swapped
     * - Structure must remain unchanged
     * - BST property broken
     */

    // =============================================================================================
    // 🧪 MAIN METHOD + SELF-VERIFYING TESTS
    // =============================================================================================

    public static void main(String[] args) {

        // Helper for validation
        ValidateBST validator = new ValidateBST();

        // -----------------------------------------------------------------------------------------
        // Test 1: Example 1
        // Input: [1,3,null,null,2]
        // -----------------------------------------------------------------------------------------
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(3);
        root1.left.right = new TreeNode(2);

        new OptimalSolution().recoverTree(root1);

        assert validator.isValidBST(root1) : "Test 1 failed: BST not recovered properly";

        // -----------------------------------------------------------------------------------------
        // Test 2: Example 2
        // Input: [3,1,4,null,null,2]
        // -----------------------------------------------------------------------------------------
        TreeNode root2 = new TreeNode(3);
        root2.left = new TreeNode(1);
        root2.right = new TreeNode(4);
        root2.right.left = new TreeNode(2);

        new OptimalSolution().recoverTree(root2);

        assert validator.isValidBST(root2) : "Test 2 failed: BST not recovered properly";

        // -----------------------------------------------------------------------------------------
        // Test 3: Adjacent Swap Case
        // -----------------------------------------------------------------------------------------
        TreeNode root3 = new TreeNode(2);
        root3.left = new TreeNode(3);
        root3.right = new TreeNode(1);

        new OptimalSolution().recoverTree(root3);

        assert validator.isValidBST(root3) : "Test 3 failed: Adjacent swap case";

        // -----------------------------------------------------------------------------------------
        // Test 4: Larger Tree
        // -----------------------------------------------------------------------------------------
        TreeNode root4 = new TreeNode(6);
        root4.left = new TreeNode(3);
        root4.right = new TreeNode(8);
        root4.left.left = new TreeNode(1);
        root4.left.right = new TreeNode(4);
        root4.right.left = new TreeNode(7);
        root4.right.right = new TreeNode(10);

        // Swap 1 and 10
        int temp = root4.left.left.val;
        root4.left.left.val = root4.right.right.val;
        root4.right.right.val = temp;

        new OptimalSolution().recoverTree(root4);

        assert validator.isValidBST(root4) : "Test 4 failed: Larger tree swap";

        System.out.println("All tests passed. BST successfully recovered.");
    }

    // =============================================================================================
    // ✅ COMPLETION CHECKLIST
    // =============================================================================================

    /*
     * ✔ Invariant:
     * Inorder traversal must be strictly increasing.
     *
     * ✔ Search Target:
     * Two nodes violating sorted order.
     *
     * ✔ Discard Rule:
     * prev.val > curr.val indicates violation.
     *
     * ✔ Termination:
     * After full traversal, swap first and second.
     *
     * ✔ Naive Failure:
     * Immediate swap after first violation fails for non-adjacent case.
     *
     * ✔ Edge Cases:
     * - Adjacent swap
     * - Root involved
     * - Deep subtree swap
     *
     * ✔ Variant Readiness:
     * Works for validation, kth smallest, min diff, greater tree.
     *
     * ✔ Pattern Boundary:
     * Only valid when exactly two nodes swapped.
     */

    /*
     * 🧘 FINAL CLOSURE STATEMENT
     *
     * I understand the invariant.
     * I can re-derive the solution.
     * This chapter is complete.
     */
}