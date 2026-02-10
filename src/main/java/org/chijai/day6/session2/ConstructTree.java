package org.chijai.day6.session2;

public class ConstructTree {

/*
 * ============================================================
 * 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
 * ============================================================
 *
 * Title: Construct Binary Tree from Preorder and Inorder Traversal
 * Link: https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
 * Difficulty: Medium
 * Tags: Tree, Binary Tree, Divide and Conquer, Hash Table
 *
 * ------------------------------------------------------------
 * Description:
 *
 * Given two integer arrays preorder and inorder where preorder is the
 * preorder traversal of a binary tree and inorder is the inorder traversal
 * of the same tree, construct and return the binary tree.
 *
 * ------------------------------------------------------------
 * Example 1:
 *
 * Input:
 * preorder = [3,9,20,15,7]
 * inorder  = [9,3,15,20,7]
 *
 * Output:
 * [3,9,20,null,null,15,7]
 *
 * Explanation:
 * The preorder traversal visits nodes as:
 *   root → left subtree → right subtree
 *
 * The inorder traversal visits nodes as:
 *   left subtree → root → right subtree
 *
 * ------------------------------------------------------------
 * Example 2:
 *
 * Input:
 * preorder = [-1]
 * inorder  = [-1]
 *
 * Output:
 * [-1]
 *
 * ------------------------------------------------------------
 * Constraints:
 *
 * 1 <= preorder.length <= 3000
 * inorder.length == preorder.length
 * -3000 <= preorder[i], inorder[i] <= 3000
 * preorder and inorder consist of unique values.
 * Each value of inorder also appears in preorder.
 * preorder is guaranteed to be the preorder traversal of the tree.
 * inorder is guaranteed to be the inorder traversal of the tree.
 *
 */

/*
 * ============================================================
 * 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
 * ============================================================
 *
 * Pattern Name:
 *   Root-Partition Recursion using Traversal Invariants
 *
 * Problem Archetype:
 *   Tree reconstruction from traversal orderings
 *
 * 🟢 Core Invariant (MANDATORY):
 *   At every recursive step, the next unused preorder element
 *   is the root of the current (sub)tree, and its position in
 *   inorder uniquely partitions left and right subtrees.
 *
 * 🟡 Why this invariant makes the pattern work:
 *   Preorder fixes the root order globally.
 *   Inorder fixes the left/right subtree boundaries locally.
 *   Together, they eliminate ambiguity.
 *
 * When this pattern applies:
 *   • Given two traversals that together uniquely define a tree
 *   • Values are unique
 *   • Traversals are consistent and valid
 *
 * Pattern recognition signals:
 *   • “Construct / Rebuild tree”
 *   • Preorder + Inorder (or Inorder + Postorder)
 *   • Unique values guaranteed
 *
 * How this pattern differs from similar patterns:
 *   • Unlike traversal printing, we are consuming order as state
 *   • Unlike generic recursion, subtree boundaries are index-based
 *   • Unlike DFS problems, structure is being created, not visited
 *
 */

/*
 * ============================================================
 * 🟢 MENTAL MODEL & INVARIANTS (CANONICAL SOURCE OF TRUTH)
 * ============================================================
 *
 * Mental Model (Think, don’t code):
 *
 * Imagine preorder as a conveyor belt of roots.
 * You always take the next root from the belt.
 *
 * Inorder is a ruler:
 *   it tells you how much belongs to the left
 *   and how much belongs to the right of that root.
 *
 * You never “search” preorder.
 * You only move forward.
 *
 * ------------------------------------------------------------
 * 🟢 ALL Invariants:
 *
 * 1. preorderIndex always points to the root of the current subtree.
 * 2. inorder[left..right] represents exactly the nodes available
 *    for the current subtree.
 * 3. No node is constructed twice.
 * 4. Subtrees do not overlap in inorder range.
 *
 * ------------------------------------------------------------
 * State meaning of each variable:
 *
 * preorderIndex:
 *   Global pointer → next root to consume
 *
 * inorderLeft, inorderRight:
 *   Closed interval representing subtree boundary
 *
 * hashmap (value → inorder index):
 *   O(1) partition lookup
 *
 * ------------------------------------------------------------
 * Allowed moves:
 *   • Consume preorder[preorderIndex] as root
 *   • Split inorder into left and right ranges
 *   • Recurse left, then right
 *
 * Forbidden moves:
 *   • Scanning preorder to find subtree size
 *   • Reusing preorderIndex
 *   • Ignoring inorder boundaries
 *
 * ------------------------------------------------------------
 * Termination logic:
 *
 * If inorderLeft > inorderRight:
 *   subtree is empty → return null
 *
 * ------------------------------------------------------------
 * Why common alternatives fail:
 *
 * • Without inorder boundaries → children bleed across subtrees
 * • Without preorder pointer discipline → wrong root ordering
 * • Without hashmap → O(n²) timeouts
 *
 */

/*
 * ============================================================
 * 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
 * ============================================================
 *
 * Typical wrong approaches:
 *
 * 1. Rebuilding left subtree by counting elements manually
 *    in preorder.
 *
 *    Why it seems correct:
 *      Preorder “looks” sequential.
 *
 *    Exact invariant violated:
 *      Preorder does NOT encode subtree size.
 *
 *    Minimal counterexample:
 *      preorder = [1,2,3]
 *      inorder  = [2,1,3]
 *
 *      Assuming left subtree is next element breaks immediately.
 *
 * ------------------------------------------------------------
 *
 * 2. Searching inorder linearly every time
 *
 *    Why it seems correct:
 *      Correctness still holds.
 *
 *    Invariant violated:
 *      Efficiency invariant (must be O(n))
 *
 *    Result:
 *      O(n²) time → TLE at n = 3000
 *
 * ------------------------------------------------------------
 *
 * 3. Passing preorder subarrays instead of index pointer
 *
 *    Why it seems correct:
 *      Functional-style recursion feels clean.
 *
 *    What breaks:
 *      Massive copying → memory + time overhead
 *
 * ------------------------------------------------------------
 *
 * Interviewer traps:
 *
 * 🔴 “Can you do it without hashmap?”
 *     → Yes, but worse complexity.
 *
 * 🔴 “Why preorder first?”
 *     → Because root must be fixed before partition.
 *
 */

// NOTE:
// Solution classes begin next.



    /*
     * ============================================================
     * 6. PRIMARY PROBLEM — SOLUTION CLASSES
     * ============================================================
     *
     * Solutions are presented in increasing order of correctness
     * confidence, efficiency, and interview preference.
     *
     * All solutions are DERIVED DIRECTLY from the invariant.
     * No solution violates the invariant; weaker ones enforce it
     * less efficiently.
     */

    // ------------------------------------------------------------
    // Basic TreeNode definition (used by all solutions)
    // ------------------------------------------------------------
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /*
     * ============================================================
     * 🟥 BRUTE FORCE SOLUTION
     * ============================================================
     *
     * Core Idea:
     *   • Take first element of preorder as root
     *   • Find that root in inorder by LINEAR SEARCH
     *   • Recursively construct left and right subtrees using
     *     array slicing (copying)
     *
     * 🟢 Invariant enforced:
     *   Root comes from preorder, partition comes from inorder
     *
     * 🔴 Limitation:
     *   • O(n²) time due to repeated scans + array copies
     *
     * Time Complexity:
     *   O(n²)
     *
     * Space Complexity:
     *   O(n²) due to subarray creation
     *
     * Interview Preference:
     *   ❌ NOT preferred
     *   (Accepted only as a stepping stone explanation)
     */
    static class BruteForce {

        static TreeNode buildTree(int[] preorder, int[] inorder) {
            if (preorder.length == 0) return null;

            int rootVal = preorder[0];
            TreeNode root = new TreeNode(rootVal);

            // 🔴 Linear search — brute force
            int rootIndex = 0;
            for (int i = 0; i < inorder.length; i++) {
                if (inorder[i] == rootVal) {
                    rootIndex = i;
                    break;
                }
            }

            int[] leftInorder = new int[rootIndex];
            int[] rightInorder = new int[inorder.length - rootIndex - 1];
            System.arraycopy(inorder, 0, leftInorder, 0, rootIndex);
            System.arraycopy(inorder, rootIndex + 1, rightInorder, 0, rightInorder.length);

            int[] leftPreorder = new int[leftInorder.length];
            int[] rightPreorder = new int[rightInorder.length];
            System.arraycopy(preorder, 1, leftPreorder, 0, leftPreorder.length);
            System.arraycopy(preorder, 1 + leftPreorder.length, rightPreorder, 0, rightPreorder.length);

            root.left = buildTree(leftPreorder, leftInorder);
            root.right = buildTree(rightPreorder, rightInorder);

            return root;
        }
    }

    /*
     * ============================================================
     * 🟨 IMPROVED SOLUTION
     * ============================================================
     *
     * Core Idea:
     *   • Avoid array copying
     *   • Use index ranges instead
     *   • Still search inorder linearly
     *
     * 🟢 Invariant enforced:
     *   Preorder index gives root
     *   Inorder range gives subtree boundary
     *
     * 🔴 Limitation:
     *   • Still O(n²) due to repeated inorder scans
     *
     * Time Complexity:
     *   O(n²)
     *
     * Space Complexity:
     *   O(n) recursion stack
     *
     * Interview Preference:
     *   ⚠️ Borderline
     *   (Acceptable if constraints are small)
     */
    static class Improved {

        static int preorderIndex;

        static TreeNode buildTree(int[] preorder, int[] inorder) {
            preorderIndex = 0;
            return helper(preorder, inorder, 0, inorder.length - 1);
        }

        private static TreeNode helper(int[] preorder, int[] inorder, int inLeft, int inRight) {
            if (inLeft > inRight) return null;

            int rootVal = preorder[preorderIndex++];
            TreeNode root = new TreeNode(rootVal);

            int rootIndex = inLeft;
            while (inorder[rootIndex] != rootVal) {
                rootIndex++;
            }

            root.left = helper(preorder, inorder, inLeft, rootIndex - 1);
            root.right = helper(preorder, inorder, rootIndex + 1, inRight);

            return root;
        }
    }

    /*
     * ============================================================
     * 🟩 OPTIMAL SOLUTION (INTERVIEW-PREFERRED)
     * ============================================================
     *
     * Core Idea:
     *   • Preprocess inorder into hashmap for O(1) partition lookup
     *   • Maintain a single global preorder index
     *   • Recursively respect inorder boundaries
     *
     * 🟢 Invariant enforced (FULLY):
     *   • preorderIndex always points to current root
     *   • inorder range strictly defines subtree
     *
     * 🟢 What limitation it fixes:
     *   • Eliminates repeated scans
     *   • Guarantees linear time
     *
     * Time Complexity:
     *   O(n)
     *
     * Space Complexity:
     *   O(n) hashmap + recursion stack
     *
     * Interview Preference:
     *   ✅ STRONGLY preferred
     */
    static class Optimal {

        private static int preorderIndex;
        private static java.util.Map<Integer, Integer> inorderIndexMap;

        static TreeNode buildTree(int[] preorder, int[] inorder) {
            preorderIndex = 0;
            inorderIndexMap = new java.util.HashMap<>();

            for (int i = 0; i < inorder.length; i++) {
                inorderIndexMap.put(inorder[i], i);
            }

            return helper(preorder, 0, inorder.length - 1);
        }

        private static TreeNode helper(int[] preorder, int inLeft, int inRight) {
            // 🟢 Termination invariant
            if (inLeft > inRight) return null;

            // 🟢 Root comes from preorder, exactly once
            int rootVal = preorder[preorderIndex++];
            TreeNode root = new TreeNode(rootVal);

            // 🟢 Partition inorder using O(1) lookup
            int index = inorderIndexMap.get(rootVal);

            // 🟢 Left subtree consumes inorder[inLeft ... index-1]
            root.left = helper(preorder, inLeft, index - 1);

            // 🟢 Right subtree consumes inorder[index+1 ... inRight]
            root.right = helper(preorder, index + 1, inRight);

            return root;
        }
    }


/*
 * ============================================================
 * 🟣 7. INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
 * ============================================================
 *
 * How to explain this solution in an interview — clearly,
 * confidently, and without touching code.
 *
 * ------------------------------------------------------------
 * 🟢 State the invariant (FIRST, ALWAYS):
 *
 * “At every step, the next unused element in preorder is the
 * root of the current subtree, and its index in inorder
 * uniquely splits the left and right subtrees.”
 *
 * ------------------------------------------------------------
 * 🟡 Explain the discard logic:
 *
 * • Preorder gives me roots in exact construction order.
 * • Inorder tells me how many nodes belong to the left subtree
 *   and how many belong to the right.
 * • Once I consume a root from preorder, it is never revisited.
 *
 * ------------------------------------------------------------
 * 🟢 Why correctness is guaranteed:
 *
 * • Every node is used exactly once as a root.
 * • Inorder boundaries ensure nodes never leak between subtrees.
 * • Recursion terminates when a subtree has no nodes.
 *
 * ------------------------------------------------------------
 * 🔴 What breaks if something changes:
 *
 * • If preorderIndex is not global → root order breaks.
 * • If inorder boundaries are ignored → structure collapses.
 * • If values are not unique → partition becomes ambiguous.
 *
 * ------------------------------------------------------------
 * 🟣 In-place / streaming feasibility:
 *
 * • Tree nodes are created on the fly.
 * • Preorder is consumed sequentially (stream-friendly).
 * • Inorder must be indexed (cannot be streamed fully).
 *
 * ------------------------------------------------------------
 * ⚠️ When NOT to use this pattern:
 *
 * • If only one traversal is given.
 * • If values are not unique.
 * • If traversals are inconsistent or invalid.
 *
 */

/*
 * ============================================================
 * 🔄 8. VARIATIONS & TWEAKS (INVARIANT-BASED)
 * ============================================================
 *
 * These are NOT new patterns.
 * They are invariant-preserving adaptations.
 *
 * ------------------------------------------------------------
 * 🟢 Invariant-preserving changes:
 *
 * 1. Inorder + Postorder
 *
 *    • Postorder’s LAST element is the root.
 *    • Traverse postorder from end to start.
 *    • Same partition logic using inorder.
 *
 *    Invariant becomes:
 *    “Next unused postorder element from the end is the root.”
 *
 * ------------------------------------------------------------
 * 2. Iterative version (advanced)
 *
 *    • Use a stack to simulate recursion.
 *    • Maintain inorder pointer to detect subtree completion.
 *
 *    Same invariant, different mechanics.
 *
 * ------------------------------------------------------------
 * 🟡 Reasoning-only changes:
 *
 * • Remove hashmap → still correct, but slower.
 * • Replace recursion with explicit stack → same invariant.
 *
 * ------------------------------------------------------------
 * 🔴 Pattern-break signals (DO NOT APPLY):
 *
 * • Duplicate values
 * • Missing nodes between traversals
 * • Preorder and inorder of different trees
 *
 * ------------------------------------------------------------
 * ⚫ Pattern mapping summary:
 *
 * Traversals define ORDER.
 * Invariant defines STRUCTURE.
 * Code only enforces discipline.
 *
 */


    /*
     * ============================================================
     * ⚫ 9. REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
     * ============================================================
     *
     * All problems below use THE SAME CORE INVARIANT:
     *
     * 🟢 “A traversal fixes root order; another traversal fixes
     *     subtree boundaries.”
     *
     * ------------------------------------------------------------
     */

    /*
     * ------------------------------------------------------------
     * PROBLEM 1: Construct Binary Tree from Inorder and Postorder
     * ------------------------------------------------------------
     *
     * LeetCode Link:
     * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/
     *
     * Official Statement (abridged but complete):
     *
     * Given two integer arrays inorder and postorder where inorder
     * is the inorder traversal of a binary tree and postorder is the
     * postorder traversal of the same tree, construct and return
     * the binary tree.
     *
     * Constraints:
     * • Unique values
     * • Valid traversals
     *
     * 🟢 Invariant Mapping:
     *
     * • Postorder LAST element is the root.
     * • Inorder partitions left/right subtrees.
     *
     * Key Difference:
     * • Traverse postorder from END to START.
     *
     * Edge Cases:
     * • Single node
     * • Completely skewed tree
     *
     * Interview Articulation:
     * “Same invariant, reversed root consumption.”
     */

    static class InorderPostorderOptimal {

        private static int postIndex;
        private static java.util.Map<Integer, Integer> inorderIndexMap;

        static TreeNode buildTree(int[] inorder, int[] postorder) {
            postIndex = postorder.length - 1;
            inorderIndexMap = new java.util.HashMap<>();

            for (int i = 0; i < inorder.length; i++) {
                inorderIndexMap.put(inorder[i], i);
            }

            return helper(postorder, 0, inorder.length - 1);
        }

        private static TreeNode helper(int[] postorder, int inLeft, int inRight) {
            // 🟢 termination invariant
            if (inLeft > inRight) return null;

            // 🟢 root comes from postorder (from the end)
            int rootVal = postorder[postIndex--];
            TreeNode root = new TreeNode(rootVal);

            // 🟢 split inorder
            int index = inorderIndexMap.get(rootVal);

            // 🔴 IMPORTANT ORDER (do NOT swap)
            root.right = helper(postorder, index + 1, inRight);
            root.left  = helper(postorder, inLeft, index - 1);

            return root;
        }
    }

    /*
     * ============================================================
     * 🟩 BST FROM PREORDER (OPTIMAL · INVARIANT-DRIVEN)
     * ============================================================
     *
     * Core Invariant:
     *   While consuming preorder, a node can be placed ONLY if its
     *   value lies within the allowed (min, max) range.
     *
     * preorder gives root order
     * BST property gives subtree boundaries
     *
     * Time:  O(n)
     * Space: O(h)
     */
    static class BSTFromPreorder {

        static int preIndex;

        static TreeNode buildBST(int[] preorder) {
            preIndex = 0;
            return helper(preorder, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        private static TreeNode helper(int[] preorder, int min, int max) {
            if (preIndex >= preorder.length) return null;

            int val = preorder[preIndex];

            // 🟢 Boundary invariant
            if (val < min || val > max) return null;

            TreeNode root = new TreeNode(val);
            preIndex++;

            // 🟢 Left subtree: values < root.val
            root.left = helper(preorder, min, root.val);

            // 🟢 Right subtree: values > root.val
            root.right = helper(preorder, root.val, max);

            return root;
        }
    }


    /*
     * ============================================================
     * 🟩 BST FROM POSTORDER (OPTIMAL · INVARIANT-DRIVEN)
     * ============================================================
     *
     * Core Invariant:
     *   While consuming postorder from the END, a node can be placed
     *   ONLY if its value lies within the allowed (min, max) range.
     *
     * postorder (reversed) gives root order
     * BST property gives subtree boundaries
     *
     * Time:  O(n)
     * Space: O(h)
     */
    static class BSTFromPostorder {

        static int postIndex;

        static TreeNode buildBST(int[] postorder) {
            postIndex = postorder.length - 1;
            return helper(postorder, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        private static TreeNode helper(int[] postorder, int min, int max) {
            if (postIndex < 0) return null;

            int val = postorder[postIndex];

            // 🟢 Boundary invariant
            if (val < min || val > max) return null;

            TreeNode root = new TreeNode(val);
            postIndex--;

            // 🔴 IMPORTANT ORDER (reverse of preorder)
            root.right = helper(postorder, val, max);
            root.left  = helper(postorder, min, val);

            return root;
        }
    }


    /*
     * ------------------------------------------------------------
     * PROBLEM 2: Verify Preorder Serialization of a Binary Tree
     * ------------------------------------------------------------
     *
     * LeetCode Link:
     * https://leetcode.com/problems/verify-preorder-serialization-of-a-binary-tree/
     *
     * Official Statement:
     *
     * One way to serialize a binary tree is to use preorder
     * traversal. We use # to denote null nodes.
     * Given a string of nodes, determine if it is a valid
     * serialization.
     *
     * 🟢 Invariant Mapping:
     *
     * • Slots invariant:
     *   Every node consumes one slot and creates two new slots.
     *
     * Why it belongs here:
     * • Still preorder-root discipline.
     * • Still structure validation via invariant.
     *
     * Edge Case:
     * • "#" alone is valid.
     */

    /*
     * ------------------------------------------------------------
     * PROBLEM 3: Construct BST from Preorder Traversal
     * ------------------------------------------------------------
     *
     * LeetCode Link:
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/
     *
     * Official Statement:
     *
     * Given an array representing preorder traversal of a BST,
     * construct the BST.
     *
     * 🟢 Invariant Mapping:
     *
     * • Preorder gives root order.
     * • BST property replaces inorder array.
     *
     * Mental shift:
     * • Boundary comes from value limits, not inorder indices.
     *
     */

    /*
     * ============================================================
     * 🧩 10. RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
     * ============================================================
     *
     * These problems share structural reasoning but differ in surface.
     *
     * ------------------------------------------------------------
     *
     * 1. Serialize and Deserialize Binary Tree
     *    • Same preorder root discipline
     *    • Null markers maintain invariant
     *
     * 2. Binary Tree Maximum Path Sum
     *    • Still subtree composition
     *    • Invariant is contribution propagation
     *
     * 3. Flatten Binary Tree to Linked List
     *    • Preorder structure preserved
     *
     */

    /*
     * ============================================================
     * 🟢 11. LEARNING VERIFICATION
     * ============================================================
     *
     * You truly understand this chapter if you can:
     *
     * • State the invariant without code.
     * • Explain why preorder must be consumed globally.
     * • Explain why inorder defines subtree size.
     * • Explain why duplicates break the solution.
     * • Debug a wrong tree by checking invariant violation.
     *
     */

    /*
     * ============================================================
     * 🧪 12. main() METHOD + SELF-VERIFYING TESTS
     * ============================================================
     */

    public static void main(String[] args) {
        // Happy path test
        int[] preorder1 = {3, 9, 20, 15, 7};
        int[] inorder1 = {9, 3, 15, 20, 7};

        TreeNode root1 = Optimal.buildTree(preorder1, inorder1);
        assert root1.val == 3 : "Root should be 3";
        assert root1.left.val == 9 : "Left child should be 9";
        assert root1.right.val == 20 : "Right child should be 20";
        assert root1.right.left.val == 15 : "Right-left should be 15";
        assert root1.right.right.val == 7 : "Right-right should be 7";

        // Boundary case: single node
        int[] preorder2 = {-1};
        int[] inorder2 = {-1};

        TreeNode root2 = Optimal.buildTree(preorder2, inorder2);
        assert root2.val == -1 : "Single node tree failed";
        assert root2.left == null && root2.right == null : "Leaf must have no children";

        // Skewed tree (all left)
        int[] preorder3 = {3, 2, 1};
        int[] inorder3 = {1, 2, 3};

        TreeNode root3 = Optimal.buildTree(preorder3, inorder3);
        assert root3.val == 3;
        assert root3.left.val == 2;
        assert root3.left.left.val == 1;

        System.out.println("All tests passed. Invariant holds.");
    }

    /*
     * ============================================================
     * ✅ 13. COMPLETION CHECKLIST (ANSWERED)
     * ============================================================
     *
     * • Invariant:
     *   Preorder fixes root order; inorder fixes subtree boundaries.
     *
     * • Search target:
     *   Current root position in inorder.
     *
     * • Discard rule:
     *   Subtree ends when inorder bounds cross.
     *
     * • Termination:
     *   inLeft > inRight.
     *
     * • Naive failure:
     *   Preorder does not encode subtree size.
     *
     * • Edge cases:
     *   Single node, skewed trees.
     *
     * • Variant readiness:
     *   Inorder + Postorder, BST from Preorder.
     *
     * • Pattern boundary:
     *   Requires unique values and valid traversals.
     *
     */

    /*
     * ============================================================
     * 🧘 FINAL CLOSURE STATEMENT
     * ============================================================
     *
     * I understand the invariant.
     * I can re-derive the solution.
     * This chapter is complete.
     *
     */
}


