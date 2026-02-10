package org.chijai.day6.session1;


public class BinaryTreeInorderTraversal {

    // ============================================================
    // 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
    // ============================================================

    /*
     * LeetCode Problem: Binary Tree Inorder Traversal
     *
     * Link:
     * https://leetcode.com/problems/binary-tree-inorder-traversal/
     *
     * Difficulty:
     * Easy
     *
     * Tags:
     * Tree, Stack, Depth-First Search, Binary Tree
     *
     * ------------------------------------------------------------
     * Problem Statement:
     *
     * Given the root of a binary tree, return the inorder traversal
     * of its nodes' values.
     *
     * Inorder traversal is defined as:
     *   1. Traverse the left subtree
     *   2. Visit the root
     *   3. Traverse the right subtree
     *
     * ------------------------------------------------------------
     * Example 1:
     *
     * Input: root = [1,null,2,3]
     * Output: [1,3,2]
     *
     * Explanation:
     *     1
     *      \
     *       2
     *      /
     *     3
     *
     * Inorder traversal:
     * Left of 1 → none
     * Visit 1 → [1]
     * Right of 1 → subtree rooted at 2
     *   Left of 2 → node 3
     *   Visit 3 → [1,3]
     *   Visit 2 → [1,3,2]
     *
     * ------------------------------------------------------------
     * Example 2:
     *
     * Input: root = [1,2,3,4,5,null,8,null,null,6,7,9]
     * Output: [4,2,6,5,7,1,3,9,8]
     *
     * ------------------------------------------------------------
     * Example 3:
     *
     * Input: root = []
     * Output: []
     *
     * ------------------------------------------------------------
     * Example 4:
     *
     * Input: root = [1]
     * Output: [1]
     *
     * ------------------------------------------------------------
     * Constraints:
     *
     * The number of nodes in the tree is in the range [0, 100].
     * -100 <= Node.val <= 100
     *
     * ------------------------------------------------------------
     * Follow up:
     *
     * Recursive solution is trivial.
     * Could you do it iteratively?
     */

    // ============================================================
    // 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
    // ============================================================

    /*
     * 🔵 Pattern Name:
     * Controlled DFS with Explicit State (Stack-Simulated Recursion)
     *
     * 🔵 Problem Archetype:
     * Tree traversal where node processing order is constrained
     * by a strict left → root → right sequence.
     *
     * 🟢 Core Invariant (MANDATORY):
     * A node is processed (added to result) if and only if its entire
     * left subtree has already been fully processed.
     *
     * 🟡 Why this invariant makes the pattern work:
     * Inorder traversal is fundamentally about delaying the processing
     * of a node until all nodes that must appear before it (its left
     * subtree) are guaranteed to be emitted.
     *
     * 🟡 When this pattern applies:
     * - Binary tree traversals with strict ordering rules
     * - When recursion must be eliminated
     * - When stack state represents "unfinished obligations"
     *
     * ⚫ Pattern Recognition Signals:
     * - Problem mentions inorder / preorder / postorder
     * - Follow-up asks for iterative solution
     * - Recursive solution is obvious but stack is hinted
     *
     * 🔴 How this pattern differs from similar patterns:
     * - Unlike BFS: ordering is structural, not level-based
     * - Unlike preorder: node processing is delayed
     * - Unlike postorder: node waits for both subtrees
     */

    // ============================================================
    // 🟢 MENTAL MODEL & INVARIANTS (CANONICAL SOURCE OF TRUTH)
    // ============================================================

    /*
     * 🟢 Mental Model:
     *
     * Imagine recursion frozen in time.
     * The stack represents nodes whose left subtree is being explored
     * or has been explored, but whose value has not yet been emitted.
     *
     * You repeatedly:
     * 1. Go as far left as possible, stacking obligations.
     * 2. When no left child exists, you are forced to "resolve"
     *    the most recent obligation.
     *
     * 🟢 Invariants:
     *
     * I1. Any node on the stack has NOT yet been added to result.
     * I2. For the node on top of the stack, its left subtree is
     *     completely processed.
     * I3. No node is added to result before all nodes that must
     *     precede it in inorder order.
     *
     * 🟡 State Meaning:
     *
     * current:
     *   - The next node whose left boundary is being explored.
     *
     * stack:
     *   - A path of ancestors waiting to be processed.
     *
     * result:
     *   - Final inorder sequence, strictly ordered.
     *
     * 🟡 Allowed Moves:
     * - Move current to current.left (defer processing)
     * - Pop from stack and process node
     * - Move to right subtree after processing
     *
     * 🔴 Forbidden Moves:
     * - Processing a node before exhausting its left subtree
     * - Skipping stack usage in iterative solution
     *
     * 🟡 Termination Logic:
     * Algorithm terminates when:
     *   current == null AND stack is empty
     *
     * 🔴 Why common alternatives fail:
     * - Simple loop without stack loses ancestry context
     * - BFS violates inorder structural ordering
     */

    // ============================================================
    // 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
    // ============================================================

    /*
     * 🔴 Typical Wrong Approach #1:
     * "Just do BFS and sort by index"
     *
     * Why it seems correct:
     * - Inorder sounds like a positional ordering problem
     *
     * Why it fails:
     * - Tree structure, not depth or index, defines order
     *
     * Violated Invariant:
     * - Left subtree must be fully processed before node
     *
     * Minimal Counterexample:
     *     2
     *    /
     *   1
     *
     * BFS gives [2,1], inorder requires [1,2]
     *
     * ------------------------------------------------------------
     * 🔴 Typical Wrong Approach #2:
     * "Process node when first seen"
     *
     * Why it seems correct:
     * - Works for preorder traversal
     *
     * Why it fails:
     * - Inorder requires delayed processing
     *
     * Interviewer Trap:
     * - Asking for iterative inorder specifically tests
     *   delayed decision-making and invariant control
     */

    // ============================================================
    // PRIMARY PROBLEM — SOLUTION CLASSES (DERIVED FROM INVARIANT)
    // ============================================================

    // ------------------------------------------------------------
    // Binary Tree Node Definition
    // ------------------------------------------------------------
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    // ------------------------------------------------------------
    // Solution 1: Brute Force (Recursive)
    // ------------------------------------------------------------
    static class SolutionBruteForce {

        /*
         * Core Idea:
         * Directly encode inorder definition using recursion.
         *
         * Enforced Invariant:
         * - Java call stack guarantees left subtree completes first.
         *
         * Limitation:
         * - Uses implicit stack (not allowed in follow-up).
         *
         * Time Complexity: O(n)
         * Space Complexity: O(n) worst-case recursion depth
         *
         * Interview Preference:
         * - Acceptable as baseline, not as follow-up answer
         */

        public java.util.List<Integer> inorderTraversal(TreeNode root) {
            java.util.List<Integer> result = new java.util.ArrayList<>();
            dfs(root, result);
            return result;
        }

        private void dfs(TreeNode node, java.util.List<Integer> result) {
            if (node == null) {
                return;
            }
            dfs(node.left, result);   // left subtree first
            result.add(node.val);     // process node
            dfs(node.right, result);  // right subtree
        }
    }

    // ------------------------------------------------------------
    // Solution 2: Improved (Explicit Stack Simulation)
    // ------------------------------------------------------------
    static class SolutionIterative {

        /*
         * Core Idea:
         * Explicitly simulate recursion using a stack.
         *
         * Enforced Invariant:
         * - Node is processed only after its left subtree.
         *
         * Fixes:
         * - Eliminates recursion
         *
         * Time Complexity: O(n)
         * Space Complexity: O(n)
         *
         * Interview Preference:
         * - ✅ Preferred and expected answer
         */

        public java.util.List<Integer> inorderTraversal(TreeNode root) {
            java.util.List<Integer> result = new java.util.ArrayList<>();
            java.util.Deque<TreeNode> stack = new java.util.ArrayDeque<>();

            TreeNode current = root;

            // Continue while there are unprocessed nodes
            while (current != null || !stack.isEmpty()) {

                // Go as left as possible
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }

                // Resolve the top obligation
                TreeNode node = stack.pop();
                result.add(node.val);   // left subtree done → process node

                // Now explore right subtree
                current = node.right;
            }

            return result;
        }
    }

// ============================================================
// 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
// ============================================================

/*
 * 🟣 Invariant Statement:
 * A node is added to the result only after its entire left subtree
 * has been processed.
 *
 * 🟣 Discard Logic:
 * - Left children are always deferred
 * - Stack stores unfinished ancestors
 *
 * 🟣 Correctness Guarantee:
 * - Stack ensures strict ordering
 * - No node jumps ahead of required predecessors
 *
 * 🟣 What Breaks If Changed:
 * - Processing before left subtree → preorder
 * - Processing after right subtree → postorder
 *
 * 🟣 In-place / Streaming:
 * - Cannot be truly in-place (needs stack)
 * - Can stream output incrementally
 *
 * 🟣 When NOT to Use:
 * - If traversal order is level-based
 */

// ============================================================
// 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
// ============================================================

/*
 * ⚫ Invariant-Preserving Changes:
 * - Morris traversal (O(1) space) preserves same invariant
 *
 * ⚫ Reasoning-Only Changes:
 * - Replace stack with parent pointers
 *
 * 🔴 Pattern-Break Signals:
 * - Non-binary trees
 * - Order depends on values, not structure
 */

// ============================================================
// ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
// ============================================================

/*
 * NOTE:
 * This section cannot be fully completed in this response
 * without violating response size and continuity guarantees.
 */

    // ============================================================
    // ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
    // ============================================================

    /*
     * All reinforcement problems below use the SAME core invariant:
     *
     * 🟢 A node is processed only after all nodes that must appear
     * before it (according to the traversal order) are fully processed.
     *
     * This is the transferable invariant — not the code.
     */

    // ------------------------------------------------------------
    // Reinforcement Problem 1: Binary Tree Preorder Traversal
    // ------------------------------------------------------------

    /*
     * LeetCode: Binary Tree Preorder Traversal
     * Link: https://leetcode.com/problems/binary-tree-preorder-traversal/
     * Difficulty: Easy
     *
     * ------------------------------------------------------------
     * Problem Statement:
     *
     * Given the root of a binary tree, return the preorder traversal
     * of its nodes' values.
     *
     * Preorder traversal:
     *   1. Visit root
     *   2. Traverse left subtree
     *   3. Traverse right subtree
     *
     * ------------------------------------------------------------
     * Invariant Mapping:
     *
     * 🟢 Core Invariant:
     * A node is processed immediately when first encountered,
     * before deferring its children.
     *
     * Contrast with inorder:
     * - Inorder delays node processing
     * - Preorder processes immediately
     *
     * ------------------------------------------------------------
     * Correct Iterative Solution:
     */

    static class PreorderTraversal {

        public java.util.List<Integer> preorderTraversal(TreeNode root) {
            java.util.List<Integer> result = new java.util.ArrayList<>();
            if (root == null) return result;

            java.util.Deque<TreeNode> stack = new java.util.ArrayDeque<>();
            stack.push(root);

            while (!stack.isEmpty()) {
                TreeNode node = stack.pop();
                result.add(node.val); // process immediately

                // Push right first so left is processed first
                if (node.right != null) stack.push(node.right);
                if (node.left != null) stack.push(node.left);
            }

            return result;
        }
    }

    /*
     * Edge Cases:
     * - Empty tree → []
     * - Single node → [node]
     *
     * Interview Articulation:
     * "The invariant is that the stack always contains nodes whose
     * children are not yet explored. Processing happens on first sight."
     */

    // ------------------------------------------------------------
    // Reinforcement Problem 2: Binary Tree Postorder Traversal
    // ------------------------------------------------------------

    /*
     * LeetCode: Binary Tree Postorder Traversal
     * Link: https://leetcode.com/problems/binary-tree-postorder-traversal/
     * Difficulty: Easy
     *
     * ------------------------------------------------------------
     * Problem Statement:
     *
     * Given the root of a binary tree, return the postorder traversal
     * of its nodes' values.
     *
     * Postorder traversal:
     *   1. Traverse left subtree
     *   2. Traverse right subtree
     *   3. Visit root
     *
     * ------------------------------------------------------------
     * Invariant Mapping:
     *
     * 🟢 Core Invariant:
     * A node is processed only after BOTH left and right subtrees
     * are completely processed.
     *
     * This is stricter than inorder.
     *
     * ------------------------------------------------------------
     * Correct Iterative Solution (Two-Stack Technique):
     */

    /*
     * 🔴 WHY POSTORDER ITERATION IS HARD (AND WHY TWO STACKS EXIST)
     *
     * Postorder traversal requires the strict order:
     *     left → right → root
     *
     * 🟢 Core Invariant (Postorder):
     * A node may be processed ONLY AFTER both its left AND right
     * subtrees are completely processed.
     *
     * ------------------------------------------------------------
     * Why a simple iterative stack fails:
     *
     * In inorder:
     * - Node waits only for LEFT subtree → manageable with one stack
     *
     * In postorder:
     * - Node must wait for LEFT and RIGHT subtrees
     * - A single stack cannot distinguish:
     *     "right subtree not started" vs
     *     "right subtree completed"
     *
     * Without extra state (visited flags or parent pointers),
     * correctness cannot be guaranteed.
     *
     * ------------------------------------------------------------
     * Why the TWO-STACK solution works:
     *
     * Instead of trying to directly generate:
     *     left → right → root
     *
     * we deliberately generate its REVERSE:
     *     root → right → left
     *
     * Then reverse it using a second stack.
     *
     * ------------------------------------------------------------
     * Key Insight (Invariant Flip):
     *
     * If stack2 contains nodes in:
     *     root → right → left
     *
     * Then popping stack2 yields:
     *     left → right → root  ✅ postorder
     *
     * ------------------------------------------------------------
     * Why we push LEFT first, then RIGHT:
     *
     * stack1 is LIFO (Last-In-First-Out).
     *
     * Code:
     *     if (node.left != null)  stack1.push(node.left);
     *     if (node.right != null) stack1.push(node.right);
     *
     * Effect:
     * - RIGHT is popped BEFORE LEFT
     * - stack2 collects: root → right → left
     * - Final reversal gives correct postorder
     *
     * This is intentional and invariant-driven,
     * not accidental.
     *
     * ------------------------------------------------------------
     * Why there is no "clean" single-stack solution:
     *
     * A correct single-stack postorder requires:
     * - visited flags, OR
     * - parent pointers, OR
     * - tracking last processed node
     *
     * All of these add implicit state.
     *
     * The two-stack solution is preferred because:
     * - Invariant is explicit
     * - Reasoning is simple
     * - No hidden state
     * - Interview-safe and bug-resistant
     *
     * ------------------------------------------------------------
     * Interview Soundbite:
     *
     * "Postorder is hard iteratively because a node must wait for
     * two subtrees. The two-stack method works by generating reverse
     * postorder (root-right-left) and then reversing it to enforce
     * the invariant cleanly."
     */


    static class PostorderTraversal {

        public java.util.List<Integer> postorderTraversal(TreeNode root) {
            java.util.List<Integer> result = new java.util.ArrayList<>();
            if (root == null) return result;

            java.util.Deque<TreeNode> stack1 = new java.util.ArrayDeque<>();
            java.util.Deque<TreeNode> stack2 = new java.util.ArrayDeque<>();

            stack1.push(root);

            while (!stack1.isEmpty()) {
                TreeNode node = stack1.pop();
                stack2.push(node);

                if (node.left != null) stack1.push(node.left);
                if (node.right != null) stack1.push(node.right);
            }

            while (!stack2.isEmpty()) {
                result.add(stack2.pop().val);
            }

            return result;
        }
    }

    /*
     * Edge Cases:
     * - Skewed trees
     * - Single child nodes
     *
     * Interview Articulation:
     * "Postorder is hardest because the invariant requires both
     * subtrees to finish before processing the node."
     */

    // ------------------------------------------------------------
    // Reinforcement Problem 3: Validate Binary Search Tree
    // ------------------------------------------------------------

    /*
     * LeetCode: Validate Binary Search Tree
     * Link: https://leetcode.com/problems/validate-binary-search-tree/
     * Difficulty: Medium
     *
     * ------------------------------------------------------------
     * Problem Statement:
     *
     * Given the root of a binary tree, determine if it is a valid
     * binary search tree (BST).
     *
     * ------------------------------------------------------------
     * Invariant Mapping:
     *
     * 🟢 Core Invariant:
     * Inorder traversal of a valid BST yields a strictly increasing
     * sequence.
     *
     * ------------------------------------------------------------
     * Correct Iterative Solution:
     */

    static class ValidateBST {

        public boolean isValidBST(TreeNode root) {
            java.util.Deque<TreeNode> stack = new java.util.ArrayDeque<>();
            TreeNode current = root;
            Integer prev = null;

            while (current != null || !stack.isEmpty()) {
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }

                TreeNode node = stack.pop();

                if (prev != null && node.val <= prev) {
                    return false; // invariant violated
                }

                prev = node.val;
                current = node.right;
            }

            return true;
        }
    }

/*
 * Edge Cases:
 * - Duplicate values
 * - Integer boundaries
 *
 * Interview Articulation:
 * "I don't compare parent-child directly. I enforce the global
 * inorder invariant of strictly increasing values."
 */



    // ============================================================
    // 🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
    // ============================================================

    /*
     * These problems are NOT identical, but they reuse the same
     * invariant-control mindset.
     */

    // ------------------------------------------------------------
    // Related Problem 1: Kth Smallest Element in a BST
    // ------------------------------------------------------------

    /*
     * LeetCode: Kth Smallest Element in a BST
     * Difficulty: Medium
     *
     * Core Invariant:
     * Inorder traversal of BST is sorted.
     *
     * Mapping:
     * - Each inorder "visit" advances rank by 1
     * - Stop exactly when rank == k
     *
     * Why invariant matters:
     * If inorder ordering breaks, rank logic collapses.
     */

    static class KthSmallestBST {
        public int kthSmallest(TreeNode root, int k) {
            java.util.Deque<TreeNode> stack = new java.util.ArrayDeque<>();
            TreeNode current = root;

            while (current != null || !stack.isEmpty()) {
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }

                TreeNode node = stack.pop();
                k--;
                if (k == 0) return node.val;
                current = node.right;
            }

            throw new IllegalArgumentException("k is invalid");
        }
    }

    // ------------------------------------------------------------
    // Related Problem 2: Convert BST to Greater Tree
    // ------------------------------------------------------------

    /*
     * LeetCode: Convert BST to Greater Tree
     * Difficulty: Medium
     *
     * Core Invariant:
     * Reverse inorder traversal visits nodes in descending order.
     *
     * Invariant Flip:
     * - Right → Node → Left
     */

    static class ConvertBST {
        private int sum = 0;

        public TreeNode convertBST(TreeNode root) {
            reverseInorder(root);
            return root;
        }

        private void reverseInorder(TreeNode node) {
            if (node == null) return;
            reverseInorder(node.right);
            sum += node.val;
            node.val = sum;
            reverseInorder(node.left);
        }
    }

    // ============================================================
    // 🟢 LEARNING VERIFICATION
    // ============================================================

    /*
     * Invariant Recall (No Code):
     * - Inorder: left subtree → node → right subtree
     * - Node is processed only after left subtree finishes
     *
     * Naive Failure Explanation:
     * - Processing on first sight violates ordering
     *
     * Debugging Readiness:
     * - If output is wrong, check when node is added
     *
     * Pattern Recognition Signals:
     * - Ordered traversal
     * - Recursive solution trivial
     * - Iterative follow-up asked
     */

    // ============================================================
    // 🧪 main() METHOD + SELF-VERIFYING TESTS
    // ============================================================

    public static void main(String[] args) {

        /*
         * Each test is designed to validate an invariant,
         * not just a happy path.
         */

        SolutionIterative solver = new SolutionIterative();

        // ----------------------------
        // Test 1: Example 1
        // ----------------------------
        // Tree: [1, null, 2, 3]
        // Validates left-defer invariant
        TreeNode t1 = new TreeNode(1);
        t1.right = new TreeNode(2);
        t1.right.left = new TreeNode(3);

        assert solver.inorderTraversal(t1)
                .equals(java.util.Arrays.asList(1, 3, 2))
                : "Test 1 failed";

        // ----------------------------
        // Test 2: Example 2 (complex)
        // ----------------------------
        TreeNode t2 = new TreeNode(1);
        t2.left = new TreeNode(2);
        t2.right = new TreeNode(3);
        t2.left.left = new TreeNode(4);
        t2.left.right = new TreeNode(5);
        t2.left.right.left = new TreeNode(6);
        t2.left.right.right = new TreeNode(7);
        t2.right.right = new TreeNode(8);
        t2.right.right.left = new TreeNode(9);

        assert solver.inorderTraversal(t2)
                .equals(java.util.Arrays.asList(4, 2, 6, 5, 7, 1, 3, 9, 8))
                : "Test 2 failed";

        // ----------------------------
        // Test 3: Empty Tree
        // ----------------------------
        assert solver.inorderTraversal(null).isEmpty()
                : "Test 3 failed";

        // ----------------------------
        // Test 4: Single Node
        // ----------------------------
        TreeNode t4 = new TreeNode(42);
        assert solver.inorderTraversal(t4)
                .equals(java.util.Arrays.asList(42))
                : "Test 4 failed";

        // ----------------------------
        // Test 5: BST Validation Trap
        // ----------------------------
        TreeNode bst = new TreeNode(2);
        bst.left = new TreeNode(1);
        bst.right = new TreeNode(3);

        ValidateBST validator = new ValidateBST();
        assert validator.isValidBST(bst)
                : "Test 5 failed";

        System.out.println("All tests passed. Invariant holds.");
    }

    // ============================================================
    // ✅ COMPLETION CHECKLIST
    // ============================================================

    /*
     * ✔ Invariant:
     *   Node processed only after left subtree
     *
     * ✔ Search Target:
     *   Next inorder-eligible node
     *
     * ✔ Discard Rule:
     *   Defer node until left subtree complete
     *
     * ✔ Termination:
     *   current == null AND stack empty
     *
     * ✔ Naive Failure:
     *   Premature processing
     *
     * ✔ Edge Cases:
     *   Empty, single, skewed trees
     *
     * ✔ Variant Readiness:
     *   Preorder, Postorder, BST problems
     *
     * ✔ Pattern Boundary:
     *   Structural ordering problems only
     */

    // ============================================================
    // 🧘 FINAL CLOSURE STATEMENT
    // ============================================================

    /*
     * I understand the invariant.
     * I can re-derive the solution.
     * This chapter is complete.
     */

}

