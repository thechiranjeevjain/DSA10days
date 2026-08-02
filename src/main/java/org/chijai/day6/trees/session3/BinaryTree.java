package org.chijai.day6.trees.session3;

import java.util.*;

/**
 * ============================================================================
 * BalancedBinaryTree
 * ============================================================================
 *
 * This file groups several closely related Binary Tree DFS/BFS height problems.
 *
 * Primary Problem:
 *      Balanced Binary Tree
 *
 * Related Problems:
 *      Maximum Depth of Binary Tree
 *      Minimum Depth of Binary Tree
 *      Diameter of Binary Tree
 *
 * Java Version:
 *      Java 17
 */
public class BinaryTree {

    /*==========================================================================
        Basic Binary Tree Definition
     ==========================================================================*/

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

    /*==========================================================================
        2. 📘 PRIMARY PROBLEM
     ==========================================================================*/

/*
 * -------------------------------------------------------------------------
 * Title
 * -------------------------------------------------------------------------
 *
 * Balanced Binary Tree
 *
 * Difficulty
 *
 * Easy
 *
 * Tags
 *
 * Tree
 * Binary Tree
 * DFS
 * Bottom-Up DFS
 * Height Computation
 * Divide and Conquer
 *
 * -------------------------------------------------------------------------
 * Problem
 * -------------------------------------------------------------------------
 *
 * Given the root of a binary tree,
 * determine whether the tree is height balanced.
 *
 * A binary tree is height balanced if for EVERY node:
 *
 *      | height(left) - height(right) | <= 1
 *
 * Every subtree must also satisfy the same property.
 *
 * -------------------------------------------------------------------------
 * Constraints
 * -------------------------------------------------------------------------
 *
 * Number of nodes:
 *      [0, 5000]
 *
 * Node values:
 *      [-10^4, 10^4]
 *
 * -------------------------------------------------------------------------
 * Example 1
 * -------------------------------------------------------------------------
 *
 *          3
 *         / \
 *        9  20
 *          /  \
 *         15   7
 *
 * Output:
 *      true
 *
 * Explanation:
 *
 * Every node differs in height by at most one.
 *
 * -------------------------------------------------------------------------
 * Example 2
 * -------------------------------------------------------------------------
 *
 *              1
 *             / \
 *            2   2
 *           / \
 *          3   3
 *         / \
 *        4   4
 *
 * Output:
 *      false
 *
 * Explanation:
 *
 * The root becomes heavily left skewed.
 *
 * -------------------------------------------------------------------------
 * Official Link
 * -------------------------------------------------------------------------
 *
 * https://leetcode.com/problems/balanced-binary-tree/
 *
 */

    /*==========================================================================
        3. 🔵 CORE PATTERN OVERVIEW
     ==========================================================================*/

/*
 * Pattern
 * -------
 *
 * Bottom-Up Tree Dynamic Programming
 *
 * Archetype
 * ---------
 *
 * Postorder DFS
 *
 * Children compute information.
 *
 * Parent consumes it exactly once.
 *
 * -------------------------------------------------------------------------
 * Core Invariant
 * -------------------------------------------------------------------------
 *
 * Every recursive call returns COMPLETE information about
 * the subtree rooted at that node.
 *
 * Parent never recomputes descendants.
 *
 * -------------------------------------------------------------------------
 * Why It Works
 * -------------------------------------------------------------------------
 *
 * Height depends on children.
 *
 * Therefore children must be solved first.
 *
 * That naturally becomes:
 *
 *      Left
 *      Right
 *      Current
 *
 * which is exactly Postorder DFS.
 *
 * -------------------------------------------------------------------------
 * Recognition Signals
 * -------------------------------------------------------------------------
 *
 * If a parent needs:
 *
 *      height
 *      size
 *      diameter
 *      balance
 *      subtree information
 *
 * computed from both children,
 * Bottom-Up DFS is usually the correct pattern.
 *
 * -------------------------------------------------------------------------
 * Use This Pattern When
 * -------------------------------------------------------------------------
 *
 * Parent depends on child summaries.
 *
 * Entire subtree information is required.
 *
 * Information naturally aggregates upward.
 *
 * -------------------------------------------------------------------------
 * Avoid This Pattern When
 * -------------------------------------------------------------------------
 *
 * Searching for one node.
 *
 * Root-to-leaf path decisions only.
 *
 * Level-order processing.
 *
 * Streaming over levels.
 *
 * -------------------------------------------------------------------------
 * Comparison
 * -------------------------------------------------------------------------
 *
 * Top-Down DFS
 *
 *      Parent pushes state downward.
 *
 * Bottom-Up DFS
 *
 *      Children return state upward.
 *
 * BFS
 *
 *      Processes tree level by level.
 *
 * Binary Search
 *
 *      Shrinks ordered search space.
 *
 * Bottom-Up DFS
 *
 *      Compresses subtree information.
 */

    /*==========================================================================
        4. 🟢 MENTAL MODEL & INVARIANTS
     ==========================================================================*/

/*
 * Mental Model
 * ------------
 *
 * Imagine every subtree mailing one sealed envelope upward.
 *
 * The envelope contains:
 *
 *      "My height."
 *
 * or
 *
 *      "Impossible.
 *       I am already unbalanced."
 *
 * Parents never inspect grandchildren directly.
 *
 * They trust the envelopes.
 *
 * -------------------------------------------------------------------------
 * Primary Invariant
 * -------------------------------------------------------------------------
 *
 * helper(node) returns exactly one of two meanings.
 *
 * Case 1
 *
 *      height >= 0
 *
 * Means:
 *
 *      subtree is balanced
 *      returned value equals subtree height
 *
 * Case 2
 *
 *      -1
 *
 * Means:
 *
 *      subtree is already unbalanced
 *
 * Once -1 appears,
 * correctness no longer requires computing real heights.
 *
 * -------------------------------------------------------------------------
 * Variable Meanings
 * -------------------------------------------------------------------------
 *
 * leftHeight
 *
 *      complete result from left subtree
 *
 * rightHeight
 *
 *      complete result from right subtree
 *
 * difference
 *
 *      current node balance factor
 *
 * -------------------------------------------------------------------------
 * Allowed State Transition
 * -------------------------------------------------------------------------
 *
 * left
 *      ↓
 * right
 *      ↓
 * verify
 *      ↓
 * compute height
 *      ↓
 * return upward
 *
 * -------------------------------------------------------------------------
 * Forbidden Transition
 * -------------------------------------------------------------------------
 *
 * Parent asking descendants for height repeatedly.
 *
 * Example:
 *
 *      maxDepth(left)
 *      maxDepth(right)
 *      recursively repeat everywhere
 *
 * This duplicates work.
 *
 * -------------------------------------------------------------------------
 * Early Failure Invariant
 * -------------------------------------------------------------------------
 *
 * Once one subtree returns -1,
 * every ancestor MUST return -1.
 *
 * No further balancing work is meaningful.
 *
 * -------------------------------------------------------------------------
 * Termination
 * -------------------------------------------------------------------------
 *
 * Null node returns height zero.
 *
 * Every recursive path eventually reaches null.
 *
 * Therefore recursion always terminates.
 *
 * -------------------------------------------------------------------------
 * Correctness Intuition
 * -------------------------------------------------------------------------
 *
 * Every node verifies exactly one local property:
 *
 *      children balanced
 *
 * AND
 *
 *      height difference <= 1
 *
 * Since every subtree satisfies this recursively,
 * the entire tree is balanced.
 *
 * -------------------------------------------------------------------------
 * Why Naive Solution Fails
 * -------------------------------------------------------------------------
 *
 * Naive recursion repeatedly computes heights.
 *
 * Example:
 *
 *              A
 *             /
 *            B
 *           /
 *          C
 *
 * Height(C)
 * is recomputed from multiple ancestors.
 *
 * Large skewed trees become O(N²).
 *
 * Bottom-Up DFS computes every subtree exactly once.
 */

    /*==========================================================================
        5. 🔴 WHY WRONG SOLUTIONS FAIL
     ==========================================================================*/

/*
 * Mistake 1
 * ---------
 *
 * Compute height separately for every node.
 *
 * Looks correct.
 *
 * Violated invariant:
 *
 *      Parent recomputes descendant information.
 *
 * Complexity explodes.
 *
 * -------------------------------------------------------------------------
 * Mistake 2
 * -------------------------------------------------------------------------
 *
 * Only compare root heights.
 *
 * Counterexample:
 *
 * Root balanced.
 *
 * Grandchild unbalanced.
 *
 * Root alone cannot certify the entire tree.
 *
 * -------------------------------------------------------------------------
 * Mistake 3
 * -------------------------------------------------------------------------
 *
 * Ignore early failure.
 *
 * Continue computing heights even after imbalance found.
 *
 * Correct answer survives,
 * but unnecessary work is performed.
 *
 * -------------------------------------------------------------------------
 * Mistake 4
 * -------------------------------------------------------------------------
 *
 * Return zero instead of -1 on failure.
 *
 * Height and failure become indistinguishable.
 *
 * Parent loses correctness information.
 *
 * -------------------------------------------------------------------------
 * Interview Trap
 * -------------------------------------------------------------------------
 *
 * Interviewer:
 *
 * "Can you avoid recomputing heights?"
 *
 * Expected insight:
 *
 * Merge
 *
 *      balance check
 *
 * and
 *
 *      height computation
 *
 * into one DFS.
 */

    /*==========================================================================
        ⚙️ IMPLEMENTATION BLUEPRINT
     ==========================================================================*/

    /*
     * Mechanical Typing Order
     * -----------------------
     *
     * Step 1
     *
     * Create public API.
     *
     *      isBalanced(root)
     *
     * Step 2
     *
     * Call helper(root).
     *
     * Step 3
     *
     * Compare returned value with sentinel.
     *
     *      != -1
     *
     * Step 4
     *
     * Implement helper().
     *
     * Step 5
     *
     * Base case.
     *
     *      null -> 0
     *
     * Step 6
     *
     * Solve left subtree.
     *
     * Step 7
     *
     * Early exit if left failed.
     *
     * Step 8
     *
     * Solve right subtree.
     *
     * Step 9
     *
     * Early exit if right failed.
     *
     * Step 10
     *
     * Compute balance factor.
     *
     * Step 11
     *
     * If imbalance:
     *
     *      return -1
     *
     * Step 12
     *
     * Otherwise:
     *
     *      return
     *
     *      1 + max(leftHeight, rightHeight)
     *
     * Entire algorithm is reconstructed from these twelve steps.
     */

    /*==========================================================================
        🧾 ULTRA-COMPACT PSEUDOCODE
     ==========================================================================*/

    /*
     * helper(node)
     *
     * if null
     *      return 0
     *
     * left = helper(left)
     * if failed
     *      return failure
     *
     * right = helper(right)
     * if failed
     *      return failure
     *
     * if difference > 1
     *      return failure
     *
     * return height
     */

    /*==========================================================================
        6. SOLUTION CLASSES
     ==========================================================================*/

    /*==========================================================================
        Brute Force
     ==========================================================================*/

    /*
     * Idea
     * ----
     *
     * Every node independently asks:
     *
     *      What is my left height?
     *
     *      What is my right height?
     *
     * Then recursively verifies children.
     *
     * Invariant
     * ---------
     *
     * Heights are always correct.
     *
     * Limitation
     * ----------
     *
     * Same subtree height is recomputed many times.
     *
     * Interview Usefulness
     * --------------------
     *
     * Excellent starting solution.
     *
     * Naturally motivates optimization.
     *
     * Complexity
     * ----------
     *
     * Balanced tree:
     *
     *      O(N log N)
     *
     * Worst skewed tree:
     *
     *      O(N²)
     */

    static class BruteForceBalanced {

        public boolean isBalanced(TreeNode root) {

            if (root == null) {
                return true;
            }

            int leftHeight = maxDepth(root.left);
            int rightHeight = maxDepth(root.right);

            return Math.abs(leftHeight - rightHeight) <= 1
                    && isBalanced(root.left)
                    && isBalanced(root.right);
        }

        private int maxDepth(TreeNode node) {

            if (node == null) {
                return 0;
            }

            int left = maxDepth(node.left);
            int right = maxDepth(node.right);

            return 1 + Math.max(left, right);
        }
    }

    /*==========================================================================
        Improved
     ==========================================================================*/

    /*
     * Idea
     * ----
     *
     * Height and balance travel upward together.
     *
     * Instead of computing height twice,
     * return one combined state.
     *
     * Invariant
     * ---------
     *
     * Every recursive return already contains
     * everything the parent needs.
     *
     * Improvement
     * -----------
     *
     * Eliminates repeated height computation.
     *
     * Complexity
     * ----------
     *
     * Time:
     *      O(N)
     *
     * Space:
     *      O(H)
     *
     * Interview Usefulness
     * --------------------
     *
     * Good stepping stone before introducing
     * sentinel optimization.
     */

    static class ImprovedBalanced {

        static class Result {

            boolean balanced;
            int height;

            Result(boolean balanced, int height) {
                this.balanced = balanced;
                this.height = height;
            }
        }

        public boolean isBalanced(TreeNode root) {
            return dfs(root).balanced;
        }

        private Result dfs(TreeNode node) {

            if (node == null) {
                return new Result(true, 0);
            }

            Result left = dfs(node.left);
            Result right = dfs(node.right);

            boolean balanced =
                    left.balanced
                            && right.balanced
                            && Math.abs(left.height - right.height) <= 1;

            int height = 1 + Math.max(left.height, right.height);

            return new Result(balanced, height);
        }
    }

    /*==========================================================================
        Optimal (Interview Preferred)
     ==========================================================================*/

    /*
     * Idea
     * ----
     *
     * Replace Result object with one integer.
     *
     * Non-negative:
     *
     *      subtree height
     *
     * -1:
     *
     *      imbalance detected
     *
     * Invariant
     * ---------
     *
     * Every return value has exactly one meaning:
     *
     *      height
     *
     * OR
     *
     *      failure
     *
     * Never both.
     *
     * Correctness
     * -----------
     *
     * Each subtree is solved exactly once.
     *
     * Every ancestor trusts returned information.
     *
     * Complexity
     * ----------
     *
     * Time:
     *
     *      O(N)
     *
     * Space:
     *
     *      O(H)
     *
     * Interview Usefulness
     * --------------------
     *
     * This is the standard optimal solution.
     */

    static class OptimalBalanced {

        public boolean isBalanced(TreeNode root) {

            return heightOrFailure(root) != -1;
        }

        private int heightOrFailure(TreeNode node) {

            // Invariant: empty subtree has height zero.
            if (node == null) {
                return 0;
            }

            int leftHeight = heightOrFailure(node.left);

            // Invariant: failure propagates upward immediately.
            if (leftHeight == -1) {
                return -1;
            }

            int rightHeight = heightOrFailure(node.right);

            // Correctness: ancestor need not inspect children again.
            if (rightHeight == -1) {
                return -1;
            }

            // Invariant: local balance must hold.
            if (Math.abs(leftHeight - rightHeight) > 1) {
                return -1;
            }

            // Height is returned only for balanced subtrees.
            return 1 + Math.max(leftHeight, rightHeight);
        }
    }

    /*==========================================================================
        🟣 INTERVIEW ARTICULATION
     ==========================================================================*/

/*
 * Invariant
 * ---------
 *
 * Every recursive call returns either:
 *
 *      subtree height
 *
 * or
 *
 *      failure sentinel.
 *
 * Discard Rule
 * ------------
 *
 * Once a subtree is unbalanced,
 * every ancestor immediately propagates failure.
 *
 * Correctness
 * -----------
 *
 * Every node validates exactly one local condition using
 * already-correct child summaries.
 *
 * Since every subtree satisfies the invariant,
 * the whole tree satisfies it.
 *
 * Termination
 * -----------
 *
 * Recursion always reaches null leaves.
 *
 * In-place Feasibility
 * --------------------
 *
 * Yes.
 *
 * Only recursion stack is used.
 *
 * Streaming Feasibility
 * ---------------------
 *
 * No.
 *
 * Parent depends on both completed child computations.
 *
 * When NOT to Use
 * ---------------
 *
 * Pure search problems.
 *
 * Level-order problems.
 *
 * Ordered tree search.
 */

    /*==========================================================================
        🎯 INTERVIEW RECALL SHEET
     ==========================================================================*/

    /*
     * Trigger
     * -------
     *
     * Parent decision depends on information from BOTH children.
     *
     * Pattern
     * -------
     *
     * Bottom-Up Postorder DFS
     *
     * Search Target
     * -------------
     *
     * Return subtree height while simultaneously detecting imbalance.
     *
     * Invariant
     * ---------
     *
     * Return value:
     *
     *      >= 0  -> subtree height
     *
     *      -1    -> subtree already unbalanced
     *
     * Discard Rule
     * ------------
     *
     * Once -1 appears,
     * stop caring about heights.
     * Only propagate failure.
     *
     * Common Trap
     * -----------
     *
     * Calling maxDepth() separately for every node.
     *
     * Edge Cases
     * ----------
     *
     * null tree
     *
     * single node
     *
     * completely skewed tree
     *
     * perfect tree
     *
     * imbalance deep inside subtree
     *
     * One-liner
     * ---------
     *
     * Merge height computation with balance validation.
     *
     * Re-derivation Cue
     * -----------------
     *
     * Ask:
     *
     * "What information would my parent like to know?"
     *
     * Answer:
     *
     * Only height,
     * unless I already know balancing is impossible.
     */

    /*==========================================================================
        🔄 VARIATIONS & TWEAKS
     ==========================================================================*/

    /*
     * Variant
     * -------
     *
     * Return Pair(height, balanced)
     *
     * Works because parent still receives complete subtree summary.
     *
     * -------------------------------------------------------------
     *
     * Variant
     * -------
     *
     * Sentinel value (-1)
     *
     * Removes object allocation.
     *
     * Same invariant.
     *
     * -------------------------------------------------------------
     *
     * Variant
     * -------
     *
     * Diameter
     *
     * Return:
     *
     *      height
     *
     * Update:
     *
     *      global diameter
     *
     * Invariant remains:
     *
     * Children solved before parent.
     *
     * -------------------------------------------------------------
     *
     * Variant
     * -------
     *
     * Maximum Depth
     *
     * Return only height.
     *
     * No balance validation.
     *
     * -------------------------------------------------------------
     *
     * Variant
     * -------
     *
     * Minimum Depth
     *
     * Same recursive skeleton.
     *
     * Different transition.
     *
     * Need special handling when one child is absent.
     *
     * -------------------------------------------------------------
     *
     * Pattern Break
     * -------------
     *
     * Breadth-first traversal.
     *
     * Heights of subtrees are unavailable while traversing levels.
     *
     * Therefore this invariant cannot be maintained.
     */

    /*==========================================================================
        🧠 MASTERY CHECKLIST
     ==========================================================================*/

    /*
     * □ Can I state the invariant?
     *
     *      Every subtree returns either height or failure.
     *
     * □ Can I identify the search target?
     *
     *      Height with embedded balance information.
     *
     * □ Can I explain the discard rule?
     *
     *      Failure propagates upward immediately.
     *
     * □ Can I explain termination?
     *
     *      Null nodes terminate recursion.
     *
     * □ Can I explain naive failure?
     *
     *      Repeated height computation.
     *
     * □ Can I list edge cases?
     *
     *      Empty tree
     *      One node
     *      Skew tree
     *      Perfect tree
     *
     * □ Can I debug confidently?
     *
     *      Inspect returned heights before parent combines them.
     *
     * □ Can I adapt this to Diameter?
     *
     *      Yes.
     *
     * □ Can I identify pattern boundary?
     *
     *      Parent consumes child summaries exactly once.
     */

    /*==========================================================================
        ==========================================================================
            RELATED PROBLEM 1
            Maximum Depth of Binary Tree
        ==========================================================================
     ==========================================================================*/

    /*
     * -------------------------------------------------------------------------
     * Title
     * -------------------------------------------------------------------------
     *
     * Maximum Depth of Binary Tree
     *
     * Difficulty
     *
     * Easy
     *
     * Tags
     *
     * Tree
     * DFS
     * Recursion
     * Binary Tree
     *
     * -------------------------------------------------------------------------
     * Problem
     * -------------------------------------------------------------------------
     *
     * Return the maximum depth of the tree.
     *
     * Depth is the number of nodes along the longest path
     * from the root to any leaf.
     *
     * -------------------------------------------------------------------------
     * Official Link
     * -------------------------------------------------------------------------
     *
     * https://leetcode.com/problems/maximum-depth-of-binary-tree/
     */

    /*
     * Pattern
     * -------
     *
     * Bottom-Up DFS
     *
     * Invariant
     * ---------
     *
     * maxDepth(node)
     * always returns the height of that subtree.
     *
     * Transition
     * ----------
     *
     * height =
     *
     *      1 + max(leftHeight, rightHeight)
     *
     * Complexity
     * ----------
     *
     * Time:
     *
     *      O(N)
     *
     * Space:
     *
     *      O(H)
     */

    static class MaximumDepth {

        public int maxDepth(TreeNode root) {

            // Invariant: empty subtree contributes height zero.
            if (root == null) {
                return 0;
            }

            int leftHeight = maxDepth(root.left);

            int rightHeight = maxDepth(root.right);

            // Parent height equals one plus taller child.
            return 1 + Math.max(leftHeight, rightHeight);
        }
    }

    /*==========================================================================
        Maximum Depth Recall Sheet
     ==========================================================================*/

/*
 * Trigger
 * -------
 *
 * Parent needs taller child.
 *
 * Invariant
 * ---------
 *
 * Every call returns subtree height.
 *
 * Transition
 * ----------
 *
 * 1 + max(left, right)
 *
 * Edge Cases
 * ----------
 *
 * null
 *
 * single node
 *
 * skewed tree
 *
 * perfect tree
 *
 * Interview One-liner
 * -------------------
 *
 * Height naturally forms a postorder recurrence.
 */

    /*==========================================================================
        ==========================================================================
            RELATED PROBLEM 2
            Minimum Depth of Binary Tree
        ==========================================================================
     ==========================================================================*/

/*
 * -------------------------------------------------------------------------
 * Title
 * -------------------------------------------------------------------------
 *
 * Minimum Depth of Binary Tree
 *
 * Difficulty
 *
 * Easy
 *
 * -------------------------------------------------------------------------
 * Problem
 * -------------------------------------------------------------------------
 *
 * Return the shortest distance from root
 * to any leaf node.
 *
 * A leaf has:
 *
 *      left == null
 *      right == null
 *
 * -------------------------------------------------------------------------
 * Important Observation
 * -------------------------------------------------------------------------
 *
 * This problem is NOT simply:
 *
 *      1 + min(left, right)
 *
 * because a missing child is NOT a valid path.
 */

    /*==========================================================================
        🔵 CORE PATTERN OVERVIEW
     ==========================================================================*/

    /*
     * Pattern
     * -------
     *
     * Recursive Bottom-Up DFS
     *
     * and
     *
     * Level Order BFS
     *
     * -------------------------------------------------------------------------
     * Recognition Signals
     * -------------------------------------------------------------------------
     *
     * DFS
     *
     *      Compute minimum depth using recursive subtree summaries.
     *
     * BFS
     *
     *      Stop immediately after the first leaf.
     *
     * -------------------------------------------------------------------------
     * Interview Preference
     * -------------------------------------------------------------------------
     *
     * If asked for the minimum distance to a leaf,
     * BFS is usually preferred because it naturally discovers
     * the nearest leaf first.
     */

    /*==========================================================================
        🟢 MENTAL MODEL
     ==========================================================================*/

    /*
     * Recursive Mental Model
     * ----------------------
     *
     * Every subtree returns:
     *
     *      minimum depth to a leaf.
     *
     * Missing child does NOT represent
     * a valid root-to-leaf path.
     *
     * Therefore:
     *
     *      if one side is missing,
     *      we must continue through the existing side.
     *
     * -------------------------------------------------------------------------
     *
     * BFS Mental Model
     * ----------------
     *
     * Expand tree level by level.
     *
     * The first encountered leaf
     * is mathematically guaranteed
     * to have minimum depth.
     *
     * Therefore traversal terminates immediately.
     */

    /*==========================================================================
        Recursive Solution
     ==========================================================================*/

    /*
     * Idea
     * ----
     *
     * Handle missing-child cases explicitly.
     *
     * Transition
     * ----------
     *
     * if one child absent
     *
     *      use the other child
     *
     * otherwise
     *
     *      take minimum
     *
     * Complexity
     * ----------
     *
     * Time:
     *
     *      O(N)
     *
     * Space:
     *
     *      O(H)
     */

    static class MinimumDepthRecursive {

        public int minDepth(TreeNode root) {

            if (root == null) {
                return 0;
            }

            int leftDepth = minDepth(root.left);

            int rightDepth = minDepth(root.right);

            // Missing child cannot terminate a path.
            if (leftDepth == 0 || rightDepth == 0) {
                return leftDepth + rightDepth + 1;
            }

            return 1 + Math.min(leftDepth, rightDepth);
        }
    }

    /*==========================================================================
        BFS Using Two Queues
     ==========================================================================*/

    /*
     * Idea
     * ----
     *
     * Maintain:
     *
     * node
     *
     * and
     *
     * corresponding depth.
     *
     * The first leaf reached is the answer.
     */

    static class MinimumDepthBfsTwoQueues {

        public int minDepth(TreeNode root) {

            if (root == null) {
                return 0;
            }

            Queue<TreeNode> nodes = new LinkedList<>();
            Queue<Integer> depths = new LinkedList<>();

            nodes.offer(root);
            depths.offer(1);

            while (!nodes.isEmpty()) {

                TreeNode node = nodes.poll();

                int depth = depths.poll();

                // First discovered leaf has minimum depth.
                if (node.left == null && node.right == null) {
                    return depth;
                }

                if (node.left != null) {
                    nodes.offer(node.left);
                    depths.offer(depth + 1);
                }

                if (node.right != null) {
                    nodes.offer(node.right);
                    depths.offer(depth + 1);
                }
            }

            return 0;
        }
    }

    /*==========================================================================
        BFS Using One Queue (Interview Preferred)
     ==========================================================================*/

    /*
     * Idea
     * ----
     *
     * Traverse level by level.
     *
     * External depth variable tracks current layer.
     *
     * Invariant
     * ---------
     *
     * Before each outer iteration,
     * every node currently inside the queue
     * belongs to exactly one depth.
     *
     * Therefore once a leaf is encountered,
     * no shallower leaf can exist.
     *
     * Complexity
     * ----------
     *
     * Time:
     *
     *      O(N)
     *
     * Space:
     *
     *      O(W)
     *
     * where
     *
     *      W = maximum width of tree.
     */

    static class MinimumDepthBfsOptimal {

        public int minDepth(TreeNode root) {

            if (root == null) {
                return 0;
            }

            Queue<TreeNode> queue = new LinkedList<>();

            queue.offer(root);

            int depth = 1;

            while (!queue.isEmpty()) {

                int levelSize = queue.size();

                // Invariant:
                // Every node processed below belongs to the same depth.
                for (int i = 0; i < levelSize; i++) {

                    TreeNode current = queue.poll();

                    // First leaf discovered is globally optimal.
                    if (current.left == null && current.right == null) {
                        return depth;
                    }

                    if (current.left != null) {
                        queue.offer(current.left);
                    }

                    if (current.right != null) {
                        queue.offer(current.right);
                    }
                }

                depth++;
            }

            return depth;
        }
    }

    /*==========================================================================
        🟣 INTERVIEW ARTICULATION
     ==========================================================================*/

/*
 * Recursive Invariant
 * -------------------
 *
 * Every subtree returns minimum depth to a leaf.
 *
 * Missing child is not a completed path.
 *
 * ---------------------------------------------------------
 *
 * BFS Invariant
 * -------------
 *
 * Queue always stores exactly one tree level.
 *
 * ---------------------------------------------------------
 *
 * Correctness
 * -----------
 *
 * BFS explores increasing depths.
 *
 * Therefore first discovered leaf
 * must be globally closest.
 *
 * ---------------------------------------------------------
 *
 * When BFS Wins
 * -------------
 *
 * Very shallow answer.
 *
 * Huge deep subtree elsewhere.
 *
 * BFS exits immediately.
 *
 * DFS may unnecessarily traverse
 * almost the entire deep subtree.
 *
 * ---------------------------------------------------------
 *
 * Pattern Boundary
 * ----------------
 *
 * Need nearest node?
 *
 * Think BFS first.
 */

    /*==========================================================================
        🎯 INTERVIEW RECALL SHEET
     ==========================================================================*/

    /*
     * Trigger
     * -------
     *
     * Find nearest leaf.
     *
     * Pattern
     * -------
     *
     * BFS
     *
     * Search Target
     * -------------
     *
     * First leaf encountered.
     *
     * Invariant
     * ---------
     *
     * Queue contains one complete level.
     *
     * Discard Rule
     * ------------
     *
     * Ignore deeper levels once a leaf appears.
     *
     * Edge Cases
     * ----------
     *
     * Empty tree
     *
     * Root is leaf
     *
     * Only left child
     *
     * Only right child
     *
     * Extremely skewed tree
     *
     * One-liner
     * ---------
     *
     * First leaf seen by BFS is the minimum depth.
     */

    /*==========================================================================
        🔄 VARIATIONS
     ==========================================================================*/

    /*
     * Variant
     * -------
     *
     * N-ary Tree
     *
     * Same BFS invariant.
     *
     * ---------------------------------------------------------
     *
     * Variant
     * -------
     *
     * Nearest Exit
     *
     * First destination discovered by BFS.
     *
     * ---------------------------------------------------------
     *
     * Variant
     * -------
     *
     * Multi-source BFS
     *
     * Start queue with several roots.
     *
     * ---------------------------------------------------------
     *
     * Pattern Break
     * -------------
     *
     * Longest path.
     *
     * BFS no longer stops at first leaf.
     */

    /*==========================================================================
        🧠 MASTERY CHECKLIST
     ==========================================================================*/

    /*
     * □ Why can't we use
     *      1 + min(left,right)?
     *
     * Because a missing child is not a valid path.
     *
     * □ Why is BFS optimal?
     *
     * It explores increasing depths.
     *
     * □ What is queue invariant?
     *
     * One level at a time.
     *
     * □ Earliest stopping point?
     *
     * First leaf.
     *
     * □ Complexity?
     *
     * O(N)
     */

    /*==========================================================================
        ==========================================================================
            RELATED PROBLEM 3
            Diameter of Binary Tree
        ==========================================================================
     ==========================================================================*/

    /*
     * -------------------------------------------------------------------------
     * Title
     * -------------------------------------------------------------------------
     *
     * Diameter of Binary Tree
     *
     * Difficulty
     *
     * Easy
     *
     * Tags
     *
     * Tree
     * DFS
     * Dynamic Programming on Trees
     *
     * -------------------------------------------------------------------------
     * Problem
     * -------------------------------------------------------------------------
     *
     * Return the length (in edges)
     * of the longest path between any two nodes.
     *
     * The path:
     *
     *      may
     *
     * or
     *
     *      may not
     *
     * pass through the root.
     *
     * -------------------------------------------------------------------------
     * Official Link
     * -------------------------------------------------------------------------
     *
     * https://leetcode.com/problems/diameter-of-binary-tree/
     */

    /*==========================================================================
        🔵 CORE PATTERN OVERVIEW
     ==========================================================================*/

    /*
     * Pattern
     * -------
     *
     * Bottom-Up DFS
     *
     * Height Aggregation
     *
     * -------------------------------------------------------------------------
     * Core Invariant
     * -------------------------------------------------------------------------
     *
     * Every recursive call returns
     * subtree height.
     *
     * During return,
     * parent computes
     * local diameter candidate.
     *
     * -------------------------------------------------------------------------
     * Key Formula
     * -------------------------------------------------------------------------
     *
     * height(node)
     *
     * =
     *
     * 1 + max(leftHeight, rightHeight)
     *
     * ---------------------------------------------------------
     *
     * diameterThroughNode
     *
     * =
     *
     * leftHeight + rightHeight
     *
     * because heights count nodes,
     * while diameter counts edges.
     *
     * -------------------------------------------------------------------------
     * Recognition Signal
     * -------------------------------------------------------------------------
     *
     * Global answer depends on
     * every node,
     * not only the root.
     */

    /*==========================================================================
        🟢 MENTAL MODEL
     ==========================================================================*/

    /*
     * Imagine every node acting as
     * the center of a possible longest path.
     *
     * Every node asks:
     *
     * "If the longest path passes through me,
     * how long would it be?"
     *
     * That answer requires only:
     *
     * left height
     *
     * and
     *
     * right height.
     *
     * Therefore:
     *
     * children first,
     * parent afterwards.
     *
     * Postorder naturally appears.
     */

    /*==========================================================================
        Brute Force
     ==========================================================================*/

    /*
     * Idea
     * ----
     *
     * For every node:
     *
     * compute left height
     *
     * compute right height
     *
     * recursively inspect children.
     *
     * Limitation
     * ----------
     *
     * Heights repeatedly recomputed.
     *
     * Complexity
     * ----------
     *
     * O(N²)
     */

    static class DiameterBruteForce {

        public int diameter(TreeNode root) {

            if (root == null) {
                return 0;
            }

            int throughRoot =
                    height(root.left)
                            + height(root.right);

            int left =
                    diameter(root.left);

            int right =
                    diameter(root.right);

            return Math.max(
                    throughRoot,
                    Math.max(left, right));
        }

        private int height(TreeNode node) {

            if (node == null) {
                return 0;
            }

            return 1 + Math.max(
                    height(node.left),
                    height(node.right));
        }
    }

    /*==========================================================================
        Optimal
     ==========================================================================*/

    /*
     * Idea
     * ----
     *
     * Compute height exactly once.
     *
     * Update global diameter
     * while returning height.
     *
     * Invariant
     * ---------
     *
     * height(node)
     *
     * always returns subtree height.
     *
     * globalDiameter
     *
     * always stores
     * best answer found so far.
     *
     * Complexity
     * ----------
     *
     * Time:
     *
     * O(N)
     *
     * Space:
     *
     * O(H)
     */

    static class DiameterOptimal {

        private int diameter;

        public int diameterOfBinaryTree(TreeNode root) {

            diameter = 0;

            height(root);

            return diameter;
        }
        private int height(TreeNode node) {

            // Invariant: empty subtree contributes zero height.
            if (node == null) {
                return 0;
            }

            int leftHeight = height(node.left);

            int rightHeight = height(node.right);

            // Every node is evaluated as the possible turning point
            // of the longest path.
            diameter = Math.max(diameter, leftHeight + rightHeight);

            // Parent only needs subtree height.
            return 1 + Math.max(leftHeight, rightHeight);
        }
    }

    /*==========================================================================
        🟣 INTERVIEW ARTICULATION
     ==========================================================================*/

/*
 * Invariant
 * ---------
 *
 * Every recursive call returns subtree height.
 *
 * Parent immediately has enough information
 * to evaluate diameter passing through itself.
 *
 * ---------------------------------------------------------
 *
 * Search Space
 * ------------
 *
 * Every node is a candidate turning point.
 *
 * ---------------------------------------------------------
 *
 * Transition
 * ----------
 *
 * diameterThroughNode
 *
 * =
 *
 * leftHeight + rightHeight
 *
 * ---------------------------------------------------------
 *
 * Correctness
 * -----------
 *
 * Since every node is examined once,
 * every possible turning point is considered.
 *
 * Therefore the maximum recorded value
 * equals the global diameter.
 *
 * ---------------------------------------------------------
 *
 * Termination
 * -----------
 *
 * DFS eventually reaches null children.
 *
 * ---------------------------------------------------------
 *
 * In-place Feasibility
 * --------------------
 *
 * Yes.
 *
 * Only recursion stack and one integer are used.
 */

    /*==========================================================================
        🎯 INTERVIEW RECALL SHEET
     ==========================================================================*/

/*
 * Trigger
 * -------
 *
 * Longest path anywhere in tree.
 *
 * Pattern
 * -------
 *
 * Bottom-Up DFS
 *
 * Search Target
 * -------------
 *
 * Global maximum over every node.
 *
 * Invariant
 * ---------
 *
 * DFS returns height.
 *
 * Global variable stores best diameter.
 *
 * Transition
 * ----------
 *
 * diameter =
 * max(diameter,
 * leftHeight + rightHeight)
 *
 * Return
 * ------
 *
 * 1 + max(leftHeight, rightHeight)
 *
 * Edge Cases
 * ----------
 *
 * Empty tree
 *
 * One node
 *
 * Chain
 *
 * Perfect tree
 *
 * One-liner
 * ---------
 *
 * Height flows upward.
 * Diameter is updated sideways.
 */

    /*==========================================================================
        🔄 VARIATIONS
     ==========================================================================*/

/*
 * Variant
 * -------
 *
 * Diameter in N-ary Tree
 *
 * Keep the two largest child heights.
 *
 * ---------------------------------------------------------
 *
 * Variant
 * -------
 *
 * Maximum Path Sum
 *
 * Replace heights with gains.
 *
 * ---------------------------------------------------------
 *
 * Variant
 * -------
 *
 * Longest Univalue Path
 *
 * Height contributes only if values match.
 *
 * ---------------------------------------------------------
 *
 * Variant
 * -------
 *
 * Tree DP
 *
 * Return one quantity upward,
 * update another globally.
 */

    /*==========================================================================
        🧠 MASTERY CHECKLIST
     ==========================================================================*/

/*
 * □ What does DFS return?
 *
 * Height.
 *
 * □ What is global state?
 *
 * Best diameter.
 *
 * □ Why update before returning?
 *
 * Parent only needs height,
 * but current node can evaluate
 * the complete path through itself.
 *
 * □ Complexity?
 *
 * O(N)
 *
 * □ Pattern?
 *
 * Bottom-Up Tree DP.
 */

    /*==========================================================================
        ==========================================================================
            PATTERN MAPPING
        ==========================================================================
     ==========================================================================*/

/*
 * Problem                           Return Value          Global State
 * -------------------------------------------------------------------------
 * Balanced Tree                     Height / -1           None
 *
 * Maximum Depth                     Height               None
 *
 * Minimum Depth (DFS)               Minimum Depth        None
 *
 * Minimum Depth (BFS)               Queue Levels         None
 *
 * Diameter                          Height              Diameter
 *
 * Longest Univalue Path             Height              Answer
 *
 * Maximum Path Sum                  Gain                Answer
 *
 * Binary Tree Cameras               Camera State        Cameras
 *
 * -------------------------------------------------------------------------
 *
 * Common Theme
 *
 * Parent consumes child summaries exactly once.
 */

    /*==========================================================================
        DEBUGGING GUIDE
     ==========================================================================*/

/*
 * Balanced Tree
 * -------------
 *
 * Print:
 *
 * node value
 * left height
 * right height
 * returned value
 *
 * If a balanced subtree returns -1,
 * sentinel propagation is incorrect.
 *
 * ---------------------------------------------------------
 *
 * Maximum Depth
 * -------------
 *
 * Height should increase by exactly one
 * when returning to parent.
 *
 * ---------------------------------------------------------
 *
 * Minimum Depth
 * -------------
 *
 * Verify:
 *
 * missing child
 *
 * does not incorrectly become depth zero answer.
 *
 * ---------------------------------------------------------
 *
 * Diameter
 * --------
 *
 * Ensure:
 *
 * diameter uses
 *
 * leftHeight + rightHeight
 *
 * not
 *
 * returned height.
 */

    /*==========================================================================
        COMMON INTERVIEW MISTAKES ACROSS ALL PROBLEMS
     ==========================================================================*/

    /*
     * 🔴 Mistake 1
     *
     * Mixing node count and edge count.
     *
     * Maximum Depth
     *      counts nodes.
     *
     * Diameter
     *      counts edges.
     *
     * ---------------------------------------------------------
     *
     * 🔴 Mistake 2
     *
     * Using
     *
     *      1 + min(left,right)
     *
     * for Minimum Depth.
     *
     * Missing children are not valid root-to-leaf paths.
     *
     * ---------------------------------------------------------
     *
     * 🔴 Mistake 3
     *
     * Recomputing subtree height repeatedly.
     *
     * Always ask:
     *
     * Can height travel upward once?
     *
     * ---------------------------------------------------------
     *
     * 🔴 Mistake 4
     *
     * Forgetting early propagation
     * of failure sentinel (-1).
     *
     * ---------------------------------------------------------
     *
     * 🔴 Mistake 5
     *
     * Updating diameter after return.
     *
     * Diameter must be computed
     * before parent loses access
     * to both child heights.
     */

    /*==========================================================================
        UNIFIED RE-DERIVATION FRAMEWORK
     ==========================================================================*/

    /*
     * Whenever a tree problem appears,
     * mentally answer these questions.
     *
     * ---------------------------------------------------------
     *
     * Question 1
     *
     * What information does my parent need?
     *
     * ---------------------------------------------------------
     *
     * Question 2
     *
     * Can children compute it independently?
     *
     * ---------------------------------------------------------
     *
     * Question 3
     *
     * Should I return it upward?
     *
     * ---------------------------------------------------------
     *
     * Question 4
     *
     * Is there a global answer
     * that should be updated
     * while returning?
     *
     * ---------------------------------------------------------
     *
     * Question 5
     *
     * Can failure be encoded
     * inside the return value?
     *
     * ---------------------------------------------------------
     *
     * Most interview tree DP problems
     * reduce to answering these questions.
     */

    /*==========================================================================
        TREE PATTERN DECISION TABLE
     ==========================================================================*/

    /*
     * Need                       Pattern
     * ---------------------------------------------------------
     * Height                     Bottom-Up DFS
     *
     * Balance                    Bottom-Up DFS
     *
     * Diameter                   Bottom-Up DFS
     *
     * Subtree Sum                Bottom-Up DFS
     *
     * Maximum Gain               Bottom-Up DFS
     *
     * Nearest Node               BFS
     *
     * Level Order                BFS
     *
     * Zigzag                     BFS
     *
     * Serialize                  DFS/BFS
     *
     * Search BST                 BST Traversal
     *
     * Root-to-Leaf Path          Top-Down DFS
     */

    /*==========================================================================
        IMPLEMENTATION MUSCLE MEMORY
     ==========================================================================*/

    /*
     * Balanced Tree
     * -------------
     *
     * helper(node)
     *
     * null -> 0
     *
     * left
     *
     * fail?
     *
     * right
     *
     * fail?
     *
     * difference?
     *
     * fail?
     *
     * return height
     *
     * ---------------------------------------------------------
     *
     * Maximum Depth
     *
     * null
     *
     * left
     *
     * right
     *
     * return
     *
     * 1 + max
     *
     * ---------------------------------------------------------
     *
     * Minimum Depth
     *
     * null
     *
     * recurse
     *
     * missing child?
     *
     * use other side
     *
     * else
     *
     * min
     *
     * ---------------------------------------------------------
     *
     * Diameter
     *
     * recurse left
     *
     * recurse right
     *
     * update answer
     *
     * return height
     */

    /*==========================================================================
        HELPER METHODS FOR TESTING
     ==========================================================================*/

    private static TreeNode n(int value) {
        return new TreeNode(value);
    }

    private static TreeNode n(int value, TreeNode left, TreeNode right) {
        return new TreeNode(value, left, right);
    }

    private static TreeNode balancedExample() {

        return n(
                3,
                n(9),
                n(
                        20,
                        n(15),
                        n(7)
                )
        );
    }

    private static TreeNode unbalancedExample() {

        return n(
                1,
                n(
                        2,
                        n(
                                3,
                                n(4),
                                n(4)
                        ),
                        n(3)
                ),
                n(2)
        );
    }

    private static TreeNode diameterExample() {

        return n(
                1,
                n(
                        2,
                        n(4),
                        n(5)
                ),
                n(3)
        );
    }

    private static TreeNode singleNode() {
        return n(1);
    }

    private static TreeNode emptyTree() {
        return null;
    }

        /*==========================================================================
        🧪 MAIN + SELF-VERIFYING TESTS
     ==========================================================================*/

    public static void main(String[] args) {

        /*
         * Run with:
         *
         *      java -ea BalancedBinaryTree
         *
         * Assertions must be enabled.
         */

        OptimalBalanced balancedSolver = new OptimalBalanced();

        /*
         * ---------------------------------------------------------------------
         * Balanced Binary Tree
         * ---------------------------------------------------------------------
         */

        // Happy path: perfectly balanced example.
        assert balancedSolver.isBalanced(balancedExample());

        // Interview example containing deep imbalance.
        assert !balancedSolver.isBalanced(unbalancedExample());

        // Empty tree is balanced by definition.
        assert balancedSolver.isBalanced(emptyTree());

        // Single node is always balanced.
        assert balancedSolver.isBalanced(singleNode());

        /*
         * ---------------------------------------------------------------------
         * Maximum Depth
         * ---------------------------------------------------------------------
         */

        MaximumDepth maxDepthSolver = new MaximumDepth();

        // Representative LeetCode example.
        assert maxDepthSolver.maxDepth(balancedExample()) == 3;

        // Boundary: empty tree.
        assert maxDepthSolver.maxDepth(emptyTree()) == 0;

        // Boundary: one node.
        assert maxDepthSolver.maxDepth(singleNode()) == 1;

        /*
         * ---------------------------------------------------------------------
         * Minimum Depth (Recursive)
         * ---------------------------------------------------------------------
         */

        MinimumDepthRecursive recursiveMinDepth =
                new MinimumDepthRecursive();

        // Shortest path ends at node 9.
        assert recursiveMinDepth.minDepth(balancedExample()) == 2;

        // Empty tree.
        assert recursiveMinDepth.minDepth(emptyTree()) == 0;

        // Single node.
        assert recursiveMinDepth.minDepth(singleNode()) == 1;

        /*
         * ---------------------------------------------------------------------
         * Minimum Depth (BFS)
         * ---------------------------------------------------------------------
         */

        MinimumDepthBfsOptimal bfsMinDepth =
                new MinimumDepthBfsOptimal();

        // BFS should stop at first discovered leaf.
        assert bfsMinDepth.minDepth(balancedExample()) == 2;

        // Empty tree.
        assert bfsMinDepth.minDepth(emptyTree()) == 0;

        // One node.
        assert bfsMinDepth.minDepth(singleNode()) == 1;

        /*
         * ---------------------------------------------------------------------
         * Diameter
         * ---------------------------------------------------------------------
         */

        DiameterOptimal diameterSolver =
                new DiameterOptimal();

        // LeetCode representative example.
        assert diameterSolver.diameterOfBinaryTree(
                diameterExample()) == 3;

        // Diameter of empty tree.
        assert diameterSolver.diameterOfBinaryTree(
                emptyTree()) == 0;

        // Diameter of single node is zero edges.
        assert diameterSolver.diameterOfBinaryTree(
                singleNode()) == 0;

        /*
         * ---------------------------------------------------------------------
         * Edge Case:
         * Completely Left Skewed Tree
         * ---------------------------------------------------------------------
         */

        TreeNode skew =
                n(
                        1,
                        n(
                                2,
                                n(
                                        3,
                                        n(4),
                                        null
                                ),
                                null
                        ),
                        null
                );

        // Should be unbalanced.
        assert !balancedSolver.isBalanced(skew);

        // Height counts nodes.
        assert maxDepthSolver.maxDepth(skew) == 4;

        // Only one root-to-leaf path exists.
        assert recursiveMinDepth.minDepth(skew) == 4;

        // Longest path uses all edges.
        assert diameterSolver.diameterOfBinaryTree(skew) == 3;

        /*
         * ---------------------------------------------------------------------
         * Edge Case:
         * Perfect Binary Tree
         * ---------------------------------------------------------------------
         */

        TreeNode perfect =
                n(
                        1,
                        n(
                                2,
                                n(4),
                                n(5)
                        ),
                        n(
                                3,
                                n(6),
                                n(7)
                        )
                );

        assert balancedSolver.isBalanced(perfect);

        assert maxDepthSolver.maxDepth(perfect) == 3;

        assert recursiveMinDepth.minDepth(perfect) == 3;

        assert diameterSolver.diameterOfBinaryTree(perfect) == 4;

        /*
         * ---------------------------------------------------------------------
         * Edge Case:
         * One Missing Child
         * ---------------------------------------------------------------------
         */

        TreeNode oneSide =
                n(
                        1,
                        null,
                        n(
                                2,
                                null,
                                n(3)
                        )
                );

        // Important interview trap for minimum depth.
        assert recursiveMinDepth.minDepth(oneSide) == 3;

        assert bfsMinDepth.minDepth(oneSide) == 3;

        /*
         * All tests passed if execution reaches here.
         */
        System.out.println("All assertions passed.");
    }

}
