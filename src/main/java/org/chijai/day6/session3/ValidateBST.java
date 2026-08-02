package org.chijai.day6.session3;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ============================================================
 * ValidateBinarySearchTree
 * ============================================================
 *
 * LeetCode:
 * https://leetcode.com/problems/validate-binary-search-tree/
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Tree
 * Binary Tree
 * Binary Search Tree
 * DFS
 * Recursion
 * Inorder Traversal
 *
 * ------------------------------------------------------------
 * Problem
 * ------------------------------------------------------------
 *
 * Given the root of a binary tree, determine whether it is a
 * valid Binary Search Tree (BST).
 *
 * A BST satisfies ALL of the following:
 *
 * 1. Every node in the left subtree is strictly smaller.
 *
 * 2. Every node in the right subtree is strictly larger.
 *
 * 3. Both left and right subtrees are themselves valid BSTs.
 *
 * IMPORTANT:
 *
 * The BST property is NOT checked only against the parent.
 *
 * Every node must satisfy constraints inherited from ALL
 * ancestors.
 *
 * ------------------------------------------------------------
 * Constraints
 * ------------------------------------------------------------
 *
 * Number of nodes:
 *      [1, 10^4]
 *
 * Node value:
 *      [-2^31, 2^31-1]
 *
 * ------------------------------------------------------------
 * Example 1
 * ------------------------------------------------------------
 *
 *        2
 *       / \
 *      1   3
 *
 * Output:
 * true
 *
 * ------------------------------------------------------------
 * Example 2
 * ------------------------------------------------------------
 *
 *          5
 *         / \
 *        1   4
 *           / \
 *          3   6
 *
 * Output:
 * false
 *
 * Explanation:
 *
 * Although 4 is the right child of 5,
 * it is smaller than 5.
 *
 * Therefore the entire right subtree violates the BST property.
 *
 * ------------------------------------------------------------
 * Official Link
 * ------------------------------------------------------------
 *
 * https://leetcode.com/problems/validate-binary-search-tree/
 *
 * ============================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern
 * -------
 * Recursive Range Validation
 *
 * Alternative Pattern
 * -------------------
 * Inorder Traversal Produces Strictly Increasing Sequence
 *
 * Archetype
 * ---------
 * Constraint propagation from ancestors.
 *
 * Core Invariant
 * --------------
 * Every recursive call owns one legal value interval.
 *
 * Every node inside that subtree MUST remain inside this
 * interval.
 *
 * Left recursion narrows only the upper bound.
 *
 * Right recursion narrows only the lower bound.
 *
 * Why It Works
 * ------------
 * Every ancestor contributes one ordering constraint.
 *
 * Passing those constraints downward guarantees every node
 * satisfies every ancestor simultaneously.
 *
 * Recognition Signals
 * -------------------
 * Use this pattern when:
 *
 * • subtree validity depends on ancestors
 *
 * • local parent comparison is insufficient
 *
 * • recursive constraints become tighter
 *
 * • descendants inherit restrictions
 *
 * When NOT To Use
 * ---------------
 * Do not use range propagation when:
 *
 * • property depends only on parent
 *
 * • constraints are purely local
 *
 * • tree order does not propagate downward
 *
 * Comparison
 * ----------
 *
 * Parent Check
 *     compares only one edge
 *     incorrect
 *
 * Recursive Bounds
 *     compares against all ancestors
 *     correct
 *
 * Inorder Traversal
 *     verifies global sorted order
 *     equally correct
 *
 * ============================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine every recursive call owns one legal numeric window.
 *
 *                (-∞, +∞)
 *                     |
 *                    10
 *                 /      \
 *          (-∞,10)      (10,+∞)
 *
 * Every child receives a smaller legal window.
 *
 * A node is allowed ONLY inside its inherited window.
 *
 * The tree is valid iff every node stays inside every inherited
 * window.
 *
 * ------------------------------------------------------------
 * Primary Invariant
 * ------------------------------------------------------------
 *
 * For every recursive call:
 *
 * subtree(root, lower, upper)
 *
 * every node inside that subtree MUST satisfy
 *
 * lower < node < upper
 *
 * ------------------------------------------------------------
 * Variable Meaning
 * ------------------------------------------------------------
 *
 * root
 *     current subtree root
 *
 * minNode
 *     strict lower ancestor bound
 *
 * maxNode
 *     strict upper ancestor bound
 *
 * ------------------------------------------------------------
 * Allowed Transition
 * ------------------------------------------------------------
 *
 * Visit left:
 *
 * upper becomes current node
 *
 * lower unchanged
 *
 * Visit right:
 *
 * lower becomes current node
 *
 * upper unchanged
 *
 * ------------------------------------------------------------
 * Forbidden Transition
 * ------------------------------------------------------------
 *
 * Never forget ancestor limits.
 *
 * Example:
 *
 *          20
 *         /
 *       10
 *         \
 *          25
 *
 * Local comparison says
 *
 * 25 > 10
 *
 * therefore looks valid.
 *
 * Global constraint says
 *
 * 25 must also be <20
 *
 * therefore invalid.
 *
 * Losing ancestor constraints breaks correctness.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Null subtree is trivially valid.
 *
 * Every recursive call strictly descends one level.
 *
 * Eventually recursion reaches null.
 *
 * ------------------------------------------------------------
 * Correctness Intuition
 * ------------------------------------------------------------
 *
 * Since every recursive call verifies one node inside the exact
 * legal interval inherited from every ancestor, and every child
 * receives the only interval it may legally occupy, every node is
 * checked against every necessary ancestor exactly once.
 *
 * ------------------------------------------------------------
 * Why Naive Parent Checking Fails
 * ------------------------------------------------------------
 *
 * Example
 *
 *          8
 *         /
 *        4
 *         \
 *          9
 *
 * Parent checks:
 *
 * 9 > 4
 * valid
 *
 * Actual BST:
 *
 * 9 belongs to left subtree of 8.
 *
 * Therefore
 *
 * 9 < 8
 *
 * must hold.
 *
 * It does not.
 *
 * Hence invalid.
 *
 * ============================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================
 *
 * Mistake 1
 * ---------
 * Compare only parent.
 *
 * Why it appears correct
 *
 * Every edge satisfies BST ordering.
 *
 * Violated Invariant
 *
 * Descendants forgot ancestor constraints.
 *
 * Counterexample
 *
 *          10
 *         /
 *        5
 *         \
 *          12
 *
 * Parent comparisons succeed.
 *
 * Global BST fails.
 *
 * ------------------------------------------------------------
 * Mistake 2
 * ---------
 * Use <= on left recursion.
 *
 * BST requires strict ordering.
 *
 * Duplicate values invalidate BST.
 *
 * ------------------------------------------------------------
 * Mistake 3
 * ---------
 * Use integer bounds:
 *
 * Integer.MIN_VALUE
 * Integer.MAX_VALUE
 *
 * This breaks when node values equal those limits.
 *
 * Better:
 *
 * propagate nullable ancestor references
 *
 * or
 *
 * propagate long bounds.
 *
 * ------------------------------------------------------------
 * Mistake 4
 * ---------
 * Update both bounds during recursion.
 *
 * Only ONE bound changes.
 *
 * Left:
 * upper changes.
 *
 * Right:
 * lower changes.
 *
 * Changing both shrinks the legal interval incorrectly.
 *
 * ------------------------------------------------------------
 * Interview Trap
 * --------------
 *
 * Interviewer gives:
 *
 *          50
 *         /
 *       30
 *         \
 *         60
 *
 * Many candidates answer true.
 *
 * Correct answer:
 * false.
 *
 * ============================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================
 *
 * Mechanical Typing Order
 * -----------------------
 *
 * 1.
 * public boolean isValidBST(root)
 *
 * 2.
 * return helper(root,null,null)
 *
 * 3.
 * helper(root,min,max)
 *
 * 4.
 * null -> true
 *
 * 5.
 * check lower bound
 *
 * 6.
 * check upper bound
 *
 * 7.
 * recurse left
 *      max=current
 *
 * 8.
 * recurse right
 *      min=current
 *
 * 9.
 * logical AND
 *
 * ============================================================
 * ULTRA-COMPACT PSEUDOCODE
 * ============================================================
 *
 * validate(node,min,max)
 *
 * if null return true
 *
 * outside interval -> false
 *
 * left(valid,min,node)
 *
 * right(valid,node,max)
 *
 * return both
 *
 * ============================================================
 * 6. SOLUTION CLASSES
 * ============================================================
 */

/**
 * Exactly matches LeetCode definition.
 */
public class ValidateBST {

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

    /**
     * =========================================================
     * Brute Force
     * =========================================================
     *
     * Idea
     * ----
     * For every node:
     *
     * Find maximum value in left subtree.
     *
     * Find minimum value in right subtree.
     *
     * Verify ordering.
     *
     * Repeat recursively.
     *
     * Invariant
     * ---------
     * Every subtree recomputes extrema independently.
     *
     * Limitation
     * ----------
     * Massive repeated traversal.
     *
     * Complexity
     * ----------
     * Time:
     * O(n²)
     *
     * Space:
     * O(h)
     *
     * Interview Usefulness
     * --------------------
     * Good starting point before optimization.
     */
    static class BruteForce {

        public boolean isValidBST(TreeNode root) {
            if (root == null) {
                return true;
            }

            if (root.left != null && maximum(root.left) >= root.val) {
                return false;
            }

            if (root.right != null && minimum(root.right) <= root.val) {
                return false;
            }

            return isValidBST(root.left)
                    && isValidBST(root.right);
        }

        private int maximum(TreeNode node) {
            if (node == null) {
                return Integer.MIN_VALUE;
            }

            return Math.max(
                    node.val,
                    Math.max(
                            maximum(node.left),
                            maximum(node.right)
                    )
            );
        }

        private int minimum(TreeNode node) {
            if (node == null) {
                return Integer.MAX_VALUE;
            }

            return Math.min(
                    node.val,
                    Math.min(
                            minimum(node.left),
                            minimum(node.right)
                    )
            );
        }
    }

    /**
     * =========================================================
     * Improved
     * =========================================================
     *
     * Idea
     * ----
     * Carry ancestor bounds downward.
     *
     * Every recursive call owns exactly one legal interval.
     *
     * Invariant
     * ---------
     * Current node must satisfy
     *
     * lower < node < upper
     *
     * Improvement
     * -----------
     * Every node visited once.
     *
     * Complexity
     * ----------
     * Time:
     * O(n)
     *
     * Space:
     * O(h)
     *
     * Interview Usefulness
     * --------------------
     * Canonical recursive solution.
     */
    static class Improved {

        public boolean isValidBST(TreeNode root) {
            return isValidBST(root, null, null);
        }

        private boolean isValidBST(
                TreeNode root,
                TreeNode minNode,
                TreeNode maxNode
        ) {

            // Invariant: empty subtree is always valid.
            if (root == null) {
                return true;
            }

            // Invariant: current node must stay above lower bound.
            if (minNode != null && root.val <= minNode.val) {
                return false;
            }

            // Invariant: current node must stay below upper bound.
            if (maxNode != null && root.val >= maxNode.val) {
                return false;
            }

            // Invariant: left subtree inherits only upper bound.
            return isValidBST(root.left, minNode, root)

                    // Invariant: right subtree inherits only lower bound.
                    && isValidBST(root.right, root, maxNode);
        }
    }

    /**
     * =========================================================
     * Optimal (Interview Preferred)
     * =========================================================
     *
     * Idea
     * ----
     * Perform an inorder traversal.
     *
     * A valid BST always produces a strictly increasing inorder
     * sequence.
     *
     * Instead of storing the full traversal, remember only the
     * previously visited node.
     *
     * Invariant
     * ---------
     * Before visiting the current node, every previously visited
     * node has already appeared in sorted inorder order.
     *
     * Therefore:
     *
     * previous.val < current.val
     *
     * must always hold.
     *
     * Correctness
     * -----------
     * Inorder visits:
     *
     * left
     * current
     * right
     *
     * Since BST ordering guarantees every left value is smaller
     * and every right value is larger, the traversal must be
     * strictly increasing.
     *
     * The first inversion immediately proves the tree is not a
     * BST.
     *
     * Complexity
     * ----------
     * Time:
     * O(n)
     *
     * Space:
     * O(h)
     *
     * Interview Usefulness
     * --------------------
     * Excellent iterative solution.
     *
     * Demonstrates:
     *
     * • stack simulation
     * • inorder traversal
     * • invariant reasoning
     * • no recursion depth concerns
     */
    static class Optimal {

        public boolean isValidBST(TreeNode root) {

            // Empty tree satisfies the BST invariant.
            if (root == null) {
                return true;
            }

            Deque<TreeNode> stack = new ArrayDeque<>();

            TreeNode previous = null;

            while (root != null || !stack.isEmpty()) {

                // Invariant:
                // descend left until the smallest remaining node.
                while (root != null) {
                    stack.push(root);
                    root = root.left;
                }

                root = stack.pop();

                // Invariant:
                // inorder sequence must remain strictly increasing.
                if (previous != null && root.val <= previous.val) {
                    return false;
                }

                previous = root;

                // Transition:
                // after current, the next candidate lives on the
                // right side.
                root = root.right;
            }

            return true;
        }
    }

    /**
     * =========================================================
     * Alternative Optimal
     * =========================================================
     *
     * Uses long bounds instead of ancestor node references.
     *
     * Some interviewers prefer numeric bounds because the legal
     * interval becomes visually explicit.
     *
     * Interval:
     *
     * (lower, upper)
     *
     * remains strict.
     */
    static class OptimalLongBounds {

        public boolean isValidBST(TreeNode root) {
            return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
        }

        private boolean validate(
                TreeNode node,
                long lower,
                long upper
        ) {

            // Invariant:
            // empty subtree never violates ordering.
            if (node == null) {
                return true;
            }

            if (node.val <= lower || node.val >= upper) {
                return false;
            }

            return validate(node.left, lower, node.val)
                    && validate(node.right, node.val, upper);
        }
    }

/**
 * =========================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================
 *
 * If asked:
 *
 * "Why does this work?"
 *
 * A concise answer:
 *
 * ---------------------------------------------------------
 *
 * The important invariant is that every recursive call owns
 * the complete legal interval for that subtree.
 *
 * The interval comes from every ancestor, not only the
 * parent.
 *
 * A node outside that interval immediately violates the BST
 * property.
 *
 * The left recursion tightens only the upper bound because
 * everything left of the current node must remain smaller.
 *
 * The right recursion tightens only the lower bound because
 * everything right of the current node must remain larger.
 *
 * Since every recursive call moves one level downward, the
 * algorithm terminates after visiting every node exactly
 * once.
 *
 * ---------------------------------------------------------
 * Discard Rule
 * ---------------------------------------------------------
 *
 * There is no search-space pruning.
 *
 * Every node must be verified because a violation may occur
 * anywhere.
 *
 * We terminate early only when an invariant is broken.
 *
 * ---------------------------------------------------------
 * In-place Feasibility
 * ---------------------------------------------------------
 *
 * Yes.
 *
 * No node modifications are required.
 *
 * ---------------------------------------------------------
 * Streaming Feasibility
 * ---------------------------------------------------------
 *
 * No.
 *
 * A subtree cannot be validated before its ancestor
 * constraints are known.
 *
 * ---------------------------------------------------------
 * When NOT To Use
 * ---------------------------------------------------------
 *
 * This invariant depends on strict global ordering.
 *
 * It does not apply to:
 *
 * • general binary trees
 * • heaps
 * • complete trees
 * • balanced trees
 *
 * Those structures satisfy different invariants.
 *
 * =========================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================
 *
 * Trigger
 * -------
 * Global BST validation.
 *
 * Invariant
 * ---------
 * Every subtree owns one legal interval.
 *
 * Search Space
 * ------------
 * Entire tree.
 *
 * Search Target
 * -------------
 * Any node violating inherited bounds.
 *
 * Discard Rule
 * ------------
 * None.
 *
 * Fail immediately on first violation.
 *
 * Common Trap
 * -----------
 * Comparing only against the parent.
 *
 * Edge Cases
 * ----------
 * • single node
 * • duplicates
 * • Integer.MIN_VALUE
 * • Integer.MAX_VALUE
 * • skewed trees
 * • deep trees
 *
 * One-liner
 * ---------
 * Every recursive call owns the only legal interval for its
 * subtree.
 *
 * Re-derivation Cue
 * -----------------
 * Ask:
 *
 * "What values are still legal here?"
 *
 * The answer defines the recursion.
 *
 * =========================================================
 * 🔄 VARIATIONS & TWEAKS
 * =========================================================
 *
 * Variation 1
 * -----------
 * Recursive ancestor references.
 *
 * Preserves the interval invariant directly.
 *
 * ---------------------------------------------------------
 * Variation 2
 * -----------
 * Recursive long bounds.
 *
 * Same invariant.
 *
 * Simpler mathematical interpretation.
 *
 * ---------------------------------------------------------
 * Variation 3
 * -----------
 * Recursive Integer bounds.
 *
 * Pattern breaks.
 *
 * Why?
 *
 * Integer.MIN_VALUE and Integer.MAX_VALUE are valid node
 * values.
 *
 * Sentinel values therefore collide with legal inputs.
 *
 * ---------------------------------------------------------
 * Variation 4
 * -----------
 * Iterative inorder traversal.
 *
 * Preserves a different invariant:
 *
 * previously visited value must remain strictly smaller than
 * the current value.
 *
 * Same asymptotic complexity.
 *
 * ---------------------------------------------------------
 * Variation 5
 * -----------
 * Store entire inorder traversal.
 *
 * Works.
 *
 * But unnecessarily increases auxiliary memory from O(h) to
 * O(n).

 /**
 * ---------------------------------------------------------
 * Variation 6
 * -----------
 * Morris Inorder Traversal.
 *
 * Preserves the inorder invariant while reducing auxiliary
 * space to O(1).
 *
 * Trade-off:
 *
 * • temporarily modifies tree links
 * • implementation is substantially harder
 * • easier to introduce subtle bugs
 *
 * Usually unnecessary unless constant auxiliary space is
 * explicitly required.
 *
 * ---------------------------------------------------------
 * Pattern Boundary
 * ----------------
 *
 * This pattern validates ordering.
 *
 * It does NOT verify:
 *
 * • balance
 * • completeness
 * • fullness
 * • perfection
 * • heap ordering
 *
 * =========================================================
 * 🧠 MASTERY CHECKLIST
 * =========================================================
 *
 * □ I know the invariant.
 *
 * Every subtree owns one strict legal interval.
 *
 * □ I know the state.
 *
 * Current node plus inherited lower and upper bounds.
 *
 * □ I know the transition.
 *
 * Left:
 *     upper becomes current node.
 *
 * Right:
 *     lower becomes current node.
 *
 * □ I know the discard rule.
 *
 * Terminate immediately when a node falls outside its legal
 * interval.
 *
 * □ I know termination.
 *
 * Every recursive call descends exactly one level until a
 * null subtree.
 *
 * □ I know why naive solutions fail.
 *
 * Parent comparison ignores ancestor constraints.
 *
 * □ I know the critical edge cases.
 *
 * • duplicates
 * • extreme integer values
 * • skewed trees
 * • single node
 * • ancestor violations
 *
 * □ I can debug the implementation.
 *
 * Check:
 *
 * 1. strict inequalities
 * 2. left updates only upper bound
 * 3. right updates only lower bound
 * 4. duplicate handling
 * 5. null base case
 *
 * □ I know interchangeable optimal solutions.
 *
 * • recursive bounds
 * • recursive ancestor references
 * • iterative inorder
 * • Morris inorder
 *
 * □ I know the pattern boundary.
 *
 * This validates ordering only.
 *
 * =========================================================
 * ⚫ PATTERN MAPPING
 * =========================================================
 *
 * Related Problems
 * ----------------
 *
 * 98.
 * Validate Binary Search Tree
 *
 * Core Pattern:
 * Range propagation
 *
 * ---------------------------------------------------------
 *
 * 99.
 * Recover Binary Search Tree
 *
 * Core Pattern:
 * Inorder inversion detection
 *
 * ---------------------------------------------------------
 *
 * 230.
 * Kth Smallest Element in BST
 *
 * Core Pattern:
 * Inorder traversal
 *
 * ---------------------------------------------------------
 *
 * 530.
 * Minimum Absolute Difference in BST
 *
 * Core Pattern:
 * Adjacent inorder comparison
 *
 * ---------------------------------------------------------
 *
 * 700.
 * Search in BST
 *
 * Core Pattern:
 * BST directed search
 *
 * ---------------------------------------------------------
 *
 * 701.
 * Insert into BST
 *
 * Core Pattern:
 * Recursive ordering
 *
 * ---------------------------------------------------------
 *
 * 450.
 * Delete Node in BST
 *
 * Core Pattern:
 * Ordered restructuring
 *
 * ---------------------------------------------------------
 *
 * Transfer Learning
 * -----------------
 *
 * Once you recognize that every subtree inherits constraints
 * from every ancestor, numerous BST interview problems become
 * significantly easier to derive.
 *
 * =========================================================
 * 🔍 FORENSIC DEBUGGING GUIDE
 * =========================================================
 *
 * Symptom
 * -------
 * Tree accepted even though a deep descendant violates the
 * root ordering.
 *
 * Likely Cause
 * ------------
 * Comparing only with the parent.
 *
 * ---------------------------------------------------------
 * Symptom
 * -------
 * Duplicate values accepted.
 *
 * Likely Cause
 * ------------
 * Using:
 *
 * <
 *
 * instead of
 *
 * <=
 *
 * or
 *
 * >
 *
 * instead of
 *
 * >=
 *
 * BST requires strict ordering.
 *
 * ---------------------------------------------------------
 * Symptom
 * -------
 * Valid tree rejected.
 *
 * Likely Cause
 * ------------
 * Updating both bounds during recursion.
 *
 * Only one bound changes per recursive step.
 *
 * ---------------------------------------------------------
 * Symptom
 * -------
 * Failure only for Integer.MIN_VALUE.
 *
 * Likely Cause
 * ------------
 * Integer sentinels used as infinity.
 *
 * Use:
 *
 * • nullable ancestor references
 * • long bounds
 *
 * ---------------------------------------------------------
 * Symptom
 * -------
 * Iterative version reports false positives.
 *
 * Likely Cause
 * ------------
 * Previous node updated before comparison.
 *
 * Correct order:
 *
 * compare
 * then
 * assign previous.
 *
 * =========================================================
 * IMPLEMENTATION RECONSTRUCTION
 * =========================================================
 *
 * If the code disappears during an interview, reconstruct it
 * mechanically.
 *
 * Step 1
 * ------
 *
 * Public wrapper.
 *
 * return helper(root, null, null)
 *
 * ---------------------------------------------------------
 * Step 2
 * ------
 *
 * Base case.
 *
 * null => true
 *
 * ---------------------------------------------------------
 * Step 3
 * ------
 *
 * Check lower bound.
 *
 * node <= lower => false
 *
 * ---------------------------------------------------------
 * Step 4
 * ------
 *
 * Check upper bound.
 *
 * node >= upper => false
 *
 * ---------------------------------------------------------
 * Step 5
 * ------
 *
 * Left recursion.
 *
 * helper(left, lower, current)
 *
 * ---------------------------------------------------------
 * Step 6
 * ------
 *
 * Right recursion.
 *
 * helper(right, current, upper)
 *
 * ---------------------------------------------------------
 * Step 7
 * ------
 *
 * Return logical AND.
 *
 * =========================================================
 * QUICK COMPARISON TABLE
 * =========================================================
 *
 * ---------------------------------------------------------
 * Approach             Time     Space
 * ---------------------------------------------------------
 * Brute Force          O(n²)    O(h)
 * Recursive Bounds     O(n)     O(h)
 * Ancestor Nodes       O(n)     O(h)
 * Iterative Inorder    O(n)     O(h)
 * Morris Inorder       O(n)     O(1)
 * ---------------------------------------------------------
 *
 * Preferred Interview Answer
 * --------------------------
 *
 * First choice:
 *
 * Recursive range propagation.
 *
 * Second choice:
 *
 * Iterative inorder traversal.
 *
 * Mention Morris only if constant auxiliary space is
 * specifically requested.
 */


public static void main(String[] args) {

    ValidateBST validator =
            new ValidateBST();

    Improved recursive = new Improved();
    Optimal iterative = new Optimal();
    OptimalLongBounds longBounds = new OptimalLongBounds();

    /*
     * ----------------------------------------------------
     * Happy Path
     * Small valid BST.
     * ----------------------------------------------------
     */
    TreeNode t1 =
            new TreeNode(
                    2,
                    new TreeNode(1),
                    new TreeNode(3)
            );

    assert recursive.isValidBST(t1);
    assert iterative.isValidBST(t1);
    assert longBounds.isValidBST(t1);

    /*
     * ----------------------------------------------------
     * Representative LeetCode invalid example.
     * Right subtree violates ancestor ordering.
     * ----------------------------------------------------
     */
    TreeNode t2 =
            new TreeNode(
                    5,
                    new TreeNode(1),
                    new TreeNode(
                            4,
                            new TreeNode(3),
                            new TreeNode(6)
                    )
            );

    assert !recursive.isValidBST(t2);
    assert !iterative.isValidBST(t2);
    assert !longBounds.isValidBST(t2);

    /*
     * ----------------------------------------------------
     * Interview trap.
     * Parent comparison succeeds.
     * Ancestor comparison fails.
     * ----------------------------------------------------
     */
    TreeNode t3 =
            new TreeNode(
                    20,
                    new TreeNode(
                            10,
                            null,
                            new TreeNode(25)
                    ),
                    null
            );

    assert !recursive.isValidBST(t3);
    assert !iterative.isValidBST(t3);
    assert !longBounds.isValidBST(t3);

    /*
     * ----------------------------------------------------
     * Duplicate values.
     * BST requires strict ordering.
     * ----------------------------------------------------
     */
    TreeNode t4 =
            new TreeNode(
                    2,
                    new TreeNode(2),
                    new TreeNode(3)
            );

    assert !recursive.isValidBST(t4);
    assert !iterative.isValidBST(t4);
    assert !longBounds.isValidBST(t4);

    /*
     * ----------------------------------------------------
     * Single node.
     * Smallest non-empty BST.
     * ----------------------------------------------------
     */
    TreeNode t5 = new TreeNode(42);

    assert recursive.isValidBST(t5);
    assert iterative.isValidBST(t5);
    assert longBounds.isValidBST(t5);

    /*
     * ----------------------------------------------------
     * Integer boundary values.
     * Ensures sentinel bugs do not exist.
     * ----------------------------------------------------
     */
    TreeNode t6 =
            new TreeNode(
                    0,
                    new TreeNode(Integer.MIN_VALUE),
                    new TreeNode(Integer.MAX_VALUE)
            );

    assert recursive.isValidBST(t6);
    assert iterative.isValidBST(t6);
    assert longBounds.isValidBST(t6);

    /*
     * ----------------------------------------------------
     * Deep valid skewed BST.
     * Exercises recursion and inorder ordering.
     * ----------------------------------------------------
     */
    TreeNode t7 =
            new TreeNode(
                    1,
                    null,
                    new TreeNode(
                            2,
                            null,
                            new TreeNode(
                                    3,
                                    null,
                                    new TreeNode(4)
                            )
                    )
            );

    assert recursive.isValidBST(t7);
    assert iterative.isValidBST(t7);
    assert longBounds.isValidBST(t7);

    /*
     * ----------------------------------------------------
     * Deep violation.
     * Right descendant enters forbidden interval.
     * ----------------------------------------------------
     */
    TreeNode t8 =
            new TreeNode(
                    50,
                    new TreeNode(
                            30,
                            new TreeNode(20),
                            new TreeNode(
                                    40,
                                    null,
                                    new TreeNode(60)
                            )
                    ),
                    new TreeNode(70)
            );

    assert !recursive.isValidBST(t8);
    assert !iterative.isValidBST(t8);
    assert !longBounds.isValidBST(t8);

    /*
     * ----------------------------------------------------
     * Valid mixed tree.
     * Exercises multiple inherited intervals.
     * ----------------------------------------------------
     */
    TreeNode t9 =
            new TreeNode(
                    8,
                    new TreeNode(
                            3,
                            new TreeNode(1),
                            new TreeNode(
                                    6,
                                    new TreeNode(4),
                                    new TreeNode(7)
                            )
                    ),
                    new TreeNode(
                            10,
                            null,
                            new TreeNode(
                                    14,
                                    new TreeNode(13),
                                    null
                            )
                    )
            );

    assert recursive.isValidBST(t9);
    assert iterative.isValidBST(t9);
    assert longBounds.isValidBST(t9);

    /*
     * ----------------------------------------------------
     * Root violation from right subtree.
     * Classic ancestor constraint failure.
     * ----------------------------------------------------
     */
    TreeNode t10 =
            new TreeNode(
                    10,
                    new TreeNode(5),
                    new TreeNode(
                            15,
                            new TreeNode(6),
                            new TreeNode(20)
                    )
            );

    assert !recursive.isValidBST(t10);
    assert !iterative.isValidBST(t10);
    assert !longBounds.isValidBST(t10);

    System.out.println("All Validate Binary Search Tree tests passed.");
}
}