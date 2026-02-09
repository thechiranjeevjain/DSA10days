package org.chijai.day6.session1;

/*
PART 1 / TOTAL 4 — CONTINUATION
(Sections covered in this part:
1. Top-level public class declaration
2. 📘 Primary Problem — Full Official LeetCode Statement
3. 🔵 Core Pattern Overview (Invariant-First)
4. 🟢 Mental Model & Invariants
5. 🔴 Why the Naive / Wrong Solution Fails
)
*/

/**
 * ============================================================
 * 📘 INVARIANT-FIRST ALGORITHM CHAPTER
 * ============================================================
 *
 * Problem: Lowest Common Ancestor of a Binary Search Tree
 * Platform: LeetCode
 *
 * This file is a self-contained, invariant-driven textbook chapter.
 * It is designed for:
 *  - pattern mastery
 *  - interview articulation
 *  - forensic debugging
 *  - correctness confidence
 *
 * ============================================================
 */
public class LCA_BST {

    /* ============================================================
     * 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
     * ============================================================
     */

    /*
     * Lowest Common Ancestor of a Binary Search Tree
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/
     *
     * Difficulty: Medium
     *
     * Tags:
     * Binary Search Tree
     * Tree
     * Depth-First Search
     * Binary Tree
     *
     * ------------------------------------------------------------
     * Problem Statement:
     *
     * Given a binary search tree (BST), find the lowest common ancestor (LCA)
     * node of two given nodes in the BST.
     *
     * According to the definition of LCA on Wikipedia:
     * “The lowest common ancestor is defined between two nodes p and q as the
     * lowest node in T that has both p and q as descendants (where we allow a
     * node to be a descendant of itself).”
     *
     * ------------------------------------------------------------
     * Example 1:
     *
     * Input:
     * root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 8
     *
     * Output:
     * 6
     *
     * Explanation:
     * The LCA of nodes 2 and 8 is 6.
     *
     * ------------------------------------------------------------
     * Example 2:
     *
     * Input:
     * root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 4
     *
     * Output:
     * 2
     *
     * Explanation:
     * The LCA of nodes 2 and 4 is 2, since a node can be a descendant of itself
     * according to the LCA definition.
     *
     * ------------------------------------------------------------
     * Example 3:
     *
     * Input:
     * root = [2,1], p = 2, q = 1
     *
     * Output:
     * 2
     *
     * ------------------------------------------------------------
     * Constraints:
     *
     * The number of nodes in the tree is in the range [2, 10^5].
     * -10^9 <= Node.val <= 10^9
     * All Node.val are unique.
     * p != q
     * p and q will exist in the BST.
     *
     * ------------------------------------------------------------
     */

    /* ============================================================
     * 3️⃣ 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
     * ============================================================
     */

    /*
     * 🔵 Pattern Name:
     * BST-Guided Search via Range Partitioning
     *
     * 🔵 Problem Archetype:
     * "Find a node where two search paths diverge"
     *
     * 🟢 Core Invariant (MANDATORY — single sentence):
     * At any node X, all values in the left subtree are strictly less than X,
     * and all values in the right subtree are strictly greater than X.
     *
     * 🟡 Why this invariant makes the pattern work:
     * Because the BST invariant lets us determine — using only comparisons —
     * whether BOTH target nodes lie entirely on one side of the current node,
     * or whether the current node is the first point of divergence.
     *
     * 🔵 When this pattern applies:
     * - Tree is a Binary Search Tree
     * - Values are unique
     * - We are locating a structural relationship, not a traversal order
     *
     * 🧭 Pattern Recognition Signals:
     * - Explicit mention of "Binary Search Tree"
     * - Queries involving ancestor / path / split point
     * - Ability to discard half the tree at each step
     *
     * 🔵 How this differs from similar patterns:
     * - NOT general binary tree LCA (which needs full DFS)
     * - NOT inorder traversal based
     * - NOT parent-pointer based
     *
     * This pattern is about directional pruning, not traversal completeness.
     */

    /* ============================================================
     * 4️⃣ 🟢 MENTAL MODEL & INVARIANTS (CANONICAL SOURCE OF TRUTH)
     * ============================================================
     */

    /*
     * 🟢 Mental Model (How to think, not code):
     *
     * Imagine walking from the root toward both p and q simultaneously.
     * As long as both targets lie strictly on the same side of the current node,
     * you MUST move in that direction.
     *
     * The first node where they no longer lie on the same side
     * is the Lowest Common Ancestor.
     *
     * ------------------------------------------------------------
     * 🟢 ALL Invariants:
     *
     * Invariant 1:
     * For any node X:
     *   - All nodes in X.left have values < X.val
     *   - All nodes in X.right have values > X.val
     *
     * Invariant 2:
     * If p.val < X.val AND q.val < X.val,
     * then LCA(p, q) MUST be in X.left subtree.
     *
     * Invariant 3:
     * If p.val > X.val AND q.val > X.val,
     * then LCA(p, q) MUST be in X.right subtree.
     *
     * Invariant 4 (Split Condition):
     * If p and q lie on different sides of X (or one equals X),
     * then X is the Lowest Common Ancestor.
     *
     * ------------------------------------------------------------
     * 🟢 State Representation:
     *
     * - current node (root)
     * - p.val and q.val as fixed comparison anchors
     *
     * ------------------------------------------------------------
     * 🟢 Allowed Moves:
     * - Move left if BOTH targets are smaller
     * - Move right if BOTH targets are larger
     *
     * ------------------------------------------------------------
     * 🔴 Forbidden Moves:
     * - Traversing both subtrees
     * - Ignoring BST ordering
     * - Performing full inorder traversal
     *
     * ------------------------------------------------------------
     * 🟢 Termination Logic:
     * Stop when current node splits the search paths of p and q.
     *
     * ------------------------------------------------------------
     * 🔴 Why common alternatives fail:
     * - Inorder traversal destroys directional information
     * - Generic LCA DFS ignores pruning power of BST invariant
     */

    /* ============================================================
     * 5️⃣ 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
     * ============================================================
     */

    /*
     * 🔴 Typical Wrong Approach 1:
     * "Convert BST to inorder list, then reason about positions"
     *
     * Why it seems correct:
     * - Inorder traversal gives sorted order
     * - LCA appears related to value ranges
     *
     * Why it fails:
     * ❌ Violates Invariant 2 & 3 (directional pruning)
     * ❌ Loses tree structure information
     * ❌ Requires extra space O(n)
     *
     * Minimal Counterexample:
     * Tree: [6,2,8,0,4,7,9,null,null,3,5]
     * p=2, q=8
     * Inorder gives [0,2,3,4,5,6,7,8,9]
     * Sorted positions do NOT encode ancestry.
     *
     * ------------------------------------------------------------
     * 🔴 Typical Wrong Approach 2:
     * "Use general binary tree LCA DFS"
     *
     * Why it seems correct:
     * - Works for all binary trees
     *
     * Why it fails interview expectations:
     * ❌ Ignores BST invariant
     * ❌ O(n) instead of O(height)
     * ❌ Signals lack of pattern recognition
     *
     * ------------------------------------------------------------
     * 🔴 Interviewer Traps:
     * - Asking for recursive DFS when iterative pruning exists
     * - Checking subtree membership explicitly
     * - Treating BST like a generic tree
     */


/* ============================================================
 * 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES
 * ============================================================
 *
 * All solutions are DERIVED STRICTLY from the BST invariant.
 * Order is intentional: Brute → Improved → Optimal.
 *
 * ------------------------------------------------------------
 * Supporting TreeNode definition (STATIC INNER CLASS)
 * ------------------------------------------------------------
 */

static class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

/* ============================================================
 * 🔴 BRUTE FORCE SOLUTION
 * ============================================================
 */

/**
 * 🔴 Brute Force Approach
 *
 * Core Idea:
 * - Find path from root to p
 * - Find path from root to q
 * - Compare paths to find last common node
 *
 * 🟢 Invariant enforced:
 * - Tree structure preserved, but BST ordering is NOT exploited
 *
 * 🔴 What limitation it has:
 * - Ignores BST invariant completely
 * - Extra space for paths
 *
 * ⏱ Time Complexity:
 * - O(n) in worst case
 *
 * 🧠 Space Complexity:
 * - O(n) for path storage
 *
 * 🟣 Interview Preference:
 * - ❌ Low (signals missed optimization)
 */
static class LCA_BST_BruteForce {

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        java.util.List<TreeNode> pathP = new java.util.ArrayList<>();
        java.util.List<TreeNode> pathQ = new java.util.ArrayList<>();

        findPath(root, p, pathP);
        findPath(root, q, pathQ);

        int i = 0;
        TreeNode lca = null;

        // Compare paths until they diverge
        while (i < pathP.size() && i < pathQ.size()) {
            if (pathP.get(i) == pathQ.get(i)) {
                lca = pathP.get(i);
            } else {
                break;
            }
            i++;
        }
        return lca;
    }

    // DFS path finder
    private boolean findPath(TreeNode root, TreeNode target, java.util.List<TreeNode> path) {
        if (root == null) return false;

        path.add(root);

        if (root == target) return true;

        if (findPath(root.left, target, path) ||
                findPath(root.right, target, path)) {
            return true;
        }

        // backtrack
        path.remove(path.size() - 1);
        return false;
    }
}

/* ============================================================
 * 🟡 IMPROVED SOLUTION (BST-AWARE BUT RECURSIVE)
 * ============================================================
 */

/**
 * 🟡 Improved Approach
 *
 * Core Idea:
 * - Use BST ordering to discard one subtree
 * - Recursively move left or right
 *
 * 🟢 Invariant enforced:
 * - If both p and q are on same side, LCA must be there
 *
 * 🟡 What limitation it fixes:
 * - Avoids full traversal
 *
 * 🔴 Remaining limitation:
 * - Recursive stack usage
 *
 * ⏱ Time Complexity:
 * - O(height of tree)
 *
 * 🧠 Space Complexity:
 * - O(height) due to recursion
 *
 * 🟣 Interview Preference:
 * - ✅ Acceptable
 */
static class LCA_BST_Improved {

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;

        // Both nodes lie in left subtree
        if (p.val < root.val && q.val < root.val) {
            return lowestCommonAncestor(root.left, p, q);
        }

        // Both nodes lie in right subtree
        if (p.val > root.val && q.val > root.val) {
            return lowestCommonAncestor(root.right, p, q);
        }

        // Split point found
        return root;
    }
}

/* ============================================================
 * 🟢 OPTIMAL SOLUTION (INTERVIEW-PREFERRED)
 * ============================================================
 */

/**
 * 🟢 Optimal Approach (Iterative)
 *
 * Core Idea:
 * - Walk down the BST once
 * - Stop at the first node that splits p and q
 *
 * 🟢 Invariant enforced (EXPLICIT):
 * At every step, current node is the lowest node
 * such that p and q are still in its subtree.
 *
 * 🟢 What limitation it fixes:
 * - Removes recursion overhead
 * - Preserves invariant at every step
 *
 * ⏱ Time Complexity:
 * - O(height of tree)
 *
 * 🧠 Space Complexity:
 * - O(1)
 *
 * 🟣 Interview Preference:
 * - ⭐⭐⭐ (Best possible)
 */
static class LCA_BST_Optimal {

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode current = root;

        while (current != null) {

            // Both targets lie in left subtree
            if (p.val < current.val && q.val < current.val) {
                current = current.left;
            }
            // Both targets lie in right subtree
            else if (p.val > current.val && q.val > current.val) {
                current = current.right;
            }
            // Split point OR one equals current
            else {
                return current;
            }
        }
        return null; // unreachable due to constraints
    }
}


/* ============================================================
 * 7️⃣ 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
 * ============================================================
 */

/*
 * 🟣 How to explain this in an interview (NO CODE):
 *
 * 1️⃣ State the invariant clearly:
 * "In a BST, all nodes in the left subtree are smaller than the root,
 *  and all nodes in the right subtree are larger."
 *
 * 2️⃣ Explain discard logic:
 * - If both p and q are smaller than the current node,
 *   the LCA must lie entirely in the left subtree.
 * - If both are larger, it must lie in the right subtree.
 *
 * 3️⃣ Explain correctness guarantee:
 * The first node where p and q no longer fall on the same side
 * is the lowest node that has both as descendants.
 *
 * 4️⃣ What breaks if logic is changed:
 * - Traversing both subtrees breaks pruning
 * - Ignoring value comparisons breaks the invariant
 *
 * 5️⃣ In-place / streaming feasibility:
 * - Yes, O(1) space using iteration
 *
 * 6️⃣ When NOT to use this pattern:
 * - When the tree is NOT a BST
 * - When values are not unique
 */

/* ============================================================
 * 8️⃣ 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
 * ============================================================
 */

/*
 * 🔄 Invariant-Preserving Variations:
 *
 * • Recursive vs Iterative
 *   - Invariant stays the same
 *   - Only control flow changes
 *
 * • Allow p or q to be root
 *   - Covered naturally by split condition
 *
 * • Extremely skewed BST
 *   - Still O(height), worst-case O(n)
 *
 * ------------------------------------------------------------
 * 🟡 Reasoning-Only Tweaks:
 *
 * - Using min(p, q) and max(p, q) simplifies comparisons
 * - Early termination remains identical
 *
 * ------------------------------------------------------------
 * 🔴 Pattern-Break Signals:
 *
 * - Tree is not BST → invariant collapses
 * - Duplicate values → comparisons ambiguous
 * - Parent pointers provided → different pattern
 */

/* ============================================================
 * 9️⃣ ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
 * ============================================================
 *
 * All problems below use the SAME CORE INVARIANT:
 * BST ordering allows directional pruning.
 */

/* ------------------------------------------------------------
 * ⚫ Reinforcement Problem 1
 * ------------------------------------------------------------
 *
 * Problem: Insert into a Binary Search Tree
 * https://leetcode.com/problems/insert-into-a-binary-search-tree/
 *
 * Official Statement (Summary):
 * Given the root of a BST and a value to insert, return the BST after insertion.
 *
 * 🟢 Invariant Mapping:
 * - At each node, decide left or right based on comparison
 *
 * 🔑 Key Insight:
 * Insertion point is the first null encountered while preserving ordering.
 */

static class InsertIntoBST {

    public TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) return new TreeNode(val);

        TreeNode current = root;
        while (true) {
            if (val < current.val) {
                if (current.left == null) {
                    current.left = new TreeNode(val);
                    break;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new TreeNode(val);
                    break;
                }
                current = current.right;
            }
        }
        return root;
    }
}

/*
 * Edge Cases:
 * - Empty tree
 * - Inserting smallest / largest value
 *
 * Interview Articulation:
 * "I follow the BST invariant until the first valid insertion point."
 */

/* ------------------------------------------------------------
 * ⚫ Reinforcement Problem 2
 * ------------------------------------------------------------
 *
 * Problem: Search in a Binary Search Tree
 * https://leetcode.com/problems/search-in-a-binary-search-tree/
 */

static class SearchInBST {

    public TreeNode searchBST(TreeNode root, int val) {
        TreeNode current = root;
        while (current != null) {
            if (current.val == val) return current;
            if (val < current.val) current = current.left;
            else current = current.right;
        }
        return null;
    }
}

/*
 * Edge Cases:
 * - Value not present
 * - Single-node tree
 *
 * Interview Articulation:
 * "Each comparison discards half the remaining search space."
 */

/* ------------------------------------------------------------
 * ⚫ Reinforcement Problem 3
 * ------------------------------------------------------------
 *
 * Problem: Validate Binary Search Tree
 * https://leetcode.com/problems/validate-binary-search-tree/
 */

static class ValidateBST {

    public boolean isValidBST(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean validate(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;
        return validate(node.left, min, node.val) &&
                validate(node.right, node.val, max);
    }
}

/*
 * Edge Cases:
 * - Integer boundary values
 * - Deep subtrees violating ancestor constraints
 *
 * Interview Articulation:
 * "BST validity is a global range invariant, not a local check."
 */

// ⛔ STOP POINT — NEXT SECTION WOULD START RELATED PROBLEMS
// Waiting for explicit continuation command.

/*
PART 4 / TOTAL 4 — CONTINUATION
(Sections covered in this part:
10. 🧩 RELATED PROBLEMS (Mini Invariant Chapters)
11. 🟢 LEARNING VERIFICATION
12. 🧪 main() METHOD + SELF-VERIFYING TESTS
13. ✅ COMPLETION CHECKLIST
🧘 FINAL CLOSURE STATEMENT
)
*/

/* ============================================================
 * 10️⃣ 🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
 * ============================================================
 *
 * These problems are adjacent to LCA-in-BST and reinforce
 * the SAME directional-pruning invariant.
 */

/* ------------------------------------------------------------
 * 🧩 Related Problem 1
 * ------------------------------------------------------------
 *
 * Problem: Range Sum of BST
 * https://leetcode.com/problems/range-sum-of-bst/
 *
 * Official Statement (condensed but complete):
 * Given the root of a BST and two integers low and high,
 * return the sum of values of all nodes with value in [low, high].
 *
 * 🟢 Invariant Mapping:
 * - If node.val < low → entire left subtree is irrelevant
 * - If node.val > high → entire right subtree is irrelevant
 *
 * 🟡 Reasoning:
 * Directional pruning avoids visiting subtrees that cannot
 * possibly contribute to the sum.
 */

static class RangeSumBST {

    public int rangeSumBST(TreeNode root, int low, int high) {
        if (root == null) return 0;

        if (root.val < low) {
            return rangeSumBST(root.right, low, high);
        }
        if (root.val > high) {
            return rangeSumBST(root.left, low, high);
        }
        return root.val
                + rangeSumBST(root.left, low, high)
                + rangeSumBST(root.right, low, high);
    }
}

/*
 * Edge Cases:
 * - Entire tree out of range
 * - Single-node tree
 *
 * Interview Articulation:
 * "I prune subtrees using the BST ordering invariant."
 */

/* ------------------------------------------------------------
 * 🧩 Related Problem 2
 * ------------------------------------------------------------
 *
 * Problem: Minimum Absolute Difference in BST
 * https://leetcode.com/problems/minimum-absolute-difference-in-bst/
 *
 * Official Statement (condensed but complete):
 * Given the root of a BST, return the minimum absolute difference
 * between values of any two nodes in the tree.
 *
 * 🟢 Invariant Mapping:
 * - Inorder traversal of BST yields sorted order
 * - Minimum difference occurs between adjacent values
 */

static class MinAbsDiffBST {

    Integer prev = null;
    int minDiff = Integer.MAX_VALUE;

    public int getMinimumDifference(TreeNode root) {
        inorder(root);
        return minDiff;
    }

    private void inorder(TreeNode node) {
        if (node == null) return;
        inorder(node.left);
        if (prev != null) {
            minDiff = Math.min(minDiff, node.val - prev);
        }
        prev = node.val;
        inorder(node.right);
    }
}

/*
 * Edge Cases:
 * - Skewed BST
 * - Large value gaps
 *
 * Interview Articulation:
 * "Sorted order emerges from inorder traversal due to BST invariant."
 */

/* ============================================================
 * 11️⃣ 🟢 LEARNING VERIFICATION
 * ============================================================
 */

/*
 * 🟢 Invariant Recall (without code):
 * - BST ordering allows directional pruning.
 *
 * 🟢 Naive Failure Explanation:
 * - Generic DFS ignores BST structure → unnecessary traversal.
 *
 * 🟢 Debugging Readiness:
 * - If result is wrong, check split-condition logic.
 *
 * 🟢 Pattern Recognition Signals:
 * - BST + ancestor / path / split-point queries.
 */

/* ============================================================
 * 12️⃣ 🧪 main() METHOD + SELF-VERIFYING TESTS
 * ============================================================
 */

public static void main(String[] args) {
    // Build sample BST:
    //        6
    //      /   \
    //     2     8
    //    / \   / \
    //   0   4 7   9
    //      / \
    //     3   5

    TreeNode root = new TreeNode(6);
    root.left = new TreeNode(2);
    root.right = new TreeNode(8);
    root.left.left = new TreeNode(0);
    root.left.right = new TreeNode(4);
    root.left.right.left = new TreeNode(3);
    root.left.right.right = new TreeNode(5);
    root.right.left = new TreeNode(7);
    root.right.right = new TreeNode(9);

    TreeNode p = root.left;              // 2
    TreeNode q = root.right;             // 8
    TreeNode r = root.left.right;        // 4

    // --- Test 1: Standard LCA split at root ---
    TreeNode lca1 = new LCA_BST_Optimal().lowestCommonAncestor(root, p, q);
    assert lca1.val == 6 : "Test 1 failed: expected 6";

    // --- Test 2: One node is ancestor of the other ---
    TreeNode lca2 = new LCA_BST_Optimal().lowestCommonAncestor(root, p, r);
    assert lca2.val == 2 : "Test 2 failed: expected 2";

    // --- Test 3: Degenerate case ---
    TreeNode root2 = new TreeNode(2);
    root2.left = new TreeNode(1);
    TreeNode lca3 = new LCA_BST_Optimal()
            .lowestCommonAncestor(root2, root2, root2.left);
    assert lca3.val == 2 : "Test 3 failed: expected 2";

    // --- Test 4: Range Sum BST ---
    int sum = new RangeSumBST().rangeSumBST(root, 2, 8);
    assert sum == (2 + 3 + 4 + 5 + 6 + 7 + 8)
            : "Range sum test failed";

    // --- Test 5: Validate BST ---
    boolean valid = new ValidateBST().isValidBST(root);
    assert valid : "Validate BST test failed";

    System.out.println("All tests passed. Invariant holds.");
}

/* ============================================================
 * 13️⃣ ✅ COMPLETION CHECKLIST (ANSWERED)
 * ============================================================
 *
 * • Invariant → BST ordering enables directional pruning
 * • Search target → First node where p and q diverge
 * • Discard rule → Both < root or both > root
 * • Termination → Split point or equality
 * • Naive failure → Ignores BST invariant
 * • Edge cases → Ancestor node, skewed tree
 * • Variant readiness → Range queries, validation, insertion
 * • Pattern boundary → Not applicable to non-BST trees
 */

/* ============================================================
 * 🧘 FINAL CLOSURE STATEMENT
 * ============================================================
 *
 * I understand the invariant.
 * I can re-derive the solution.
 * This chapter is complete.
 */

}
