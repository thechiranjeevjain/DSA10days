package org.chijai.day6.trees.session3;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * ============================================================================
 * BinaryTree.java
 * ============================================================================
 *
 * PRIMARY ANCHOR
 *   110. Balanced Binary Tree
 *
 * CLOSE REINFORCEMENT
 *   104. Maximum Depth of Binary Tree
 *   111. Minimum Depth of Binary Tree
 *   543. Diameter of Binary Tree
 *   124. Binary Tree Maximum Path Sum
 *   687. Longest Univalue Path
 *
 * MASTER FAMILY
 *   Bottom-Up / Postorder Tree DP
 *
 * Java 17 | IntelliJ-ready | self-verifying with assertions
 */
public class BinaryTree {

    /*==========================================================================
        BASIC TREE DEFINITION
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
        PRIMARY PROBLEM — 110. BALANCED BINARY TREE
        Preferred interview solution
     ==========================================================================*/

    static class BalancedBinaryTree {

        public boolean isBalanced(TreeNode root) {
            return heightOrFailure(root) != -1;
        }

        private int heightOrFailure(TreeNode node) {

            if (node == null) {
                return 0;
            }

            int leftHeight = heightOrFailure(node.left);
            if (leftHeight == -1) {
                return -1;
            }

            int rightHeight = heightOrFailure(node.right);
            if (rightHeight == -1) {
                return -1;
            }

            if (Math.abs(leftHeight - rightHeight) > 1) {
                return -1;
            }

            return 1 + Math.max(leftHeight, rightHeight);
        }
    }

    /*
     * WHY 1 — Why postorder?
     * -----------------------
     * Current node cannot know its height/balance until BOTH children
     * have already returned their summaries.
     *
     *      LEFT -> RIGHT -> CURRENT
     *
     * Important refinement:
     *
     * The naive solution was ALSO using postorder inside maxDepth().
     * The optimization is not "discover postorder".
     *
     * It is:
     *
     *      ONE postorder traversal returns COMPLETE subtree information.
     */

    /*
     * WHY 2 — Why return height?
     * ---------------------------
     * Parent needs child heights to evaluate:
     *
     *      |leftHeight - rightHeight| <= 1
     *
     * So height is the natural quantity that flows upward.
     */

    /*
     * WHY 3 — Why -1?
     * ----------------
     * We need to communicate TWO facts:
     *
     *      1. subtree height
     *      2. whether subtree is already unbalanced
     *
     * Sentinel compression:
     *
     *      0,1,2,... -> balanced + actual height
     *      -1        -> unbalanced
     *
     * One return value now carries everything the parent needs.
     */

    /*
     * WHY 4 — Why early-return on -1?
     * --------------------------------
     * Once a descendant is unbalanced, no ancestor can repair it.
     * Real heights no longer matter; only failure propagation matters.
     */

    /*
     * WHY 5 — Why is this O(N)?
     * --------------------------
     * Every node is processed once.
     * No ancestor starts a fresh maxDepth() traversal over the same subtree.
     *
     * Time  : O(N)
     * Space : O(H) recursion stack
     */

    /*==========================================================================
        30-SECOND RECALL CARD
     ==========================================================================*/

    /*
     * BALANCED TREE
     *
     * Trigger:
     *      Every node must satisfy a property based on child heights.
     *
     * Pattern:
     *      Bottom-up postorder DFS.
     *
     * Return invariant:
     *      >= 0 -> balanced subtree height
     *      -1   -> subtree already unbalanced
     *
     * Mechanical order:
     *      null -> 0
     *      left
     *      left failed?
     *      right
     *      right failed?
     *      abs(left-right) > 1?
     *      return 1 + max(left,right)
     *
     * One-liner:
     *      Merge height computation and balance validation into ONE traversal.
     */

    /*==========================================================================
        REUSABLE MASTER TEMPLATE — POSTORDER TREE DP
     ==========================================================================*/

    /*
     * The strongest question is NOT merely:
     *
     *      "Should I use postorder?"
     *
     * Ask:
     *
     *      Q1. What information does my PARENT need from me?
     *      Q2. What COMPLETE answer can I construct HERE using both children?
     *
     * Generic skeleton:
     *
     *      State dfs(node) {
     *          if (node == null) return BASE;
     *
     *          State left  = dfs(node.left);
     *          State right = dfs(node.right);
     *
     *          // optional:
     *          // update a complete/local/global answer using BOTH children
     *
     *          return something parent can legally extend/use;
     *      }
     *
     * Critical distinction:
     *
     *      RETURN TO PARENT
     *          often only ONE extendable branch / compressed subtree summary
     *
     *      LOCAL/GLOBAL CANDIDATE
     *          may combine BOTH children
     *
     * This distinction drives Diameter, Maximum Path Sum,
     * Longest Univalue Path, and many advanced tree-DP problems.
     */

    /*==========================================================================
        APPROACH PROGRESSION — BALANCED TREE
     ==========================================================================*/

    static class BalancedBruteForce {

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

    /*
     * BRUTE-FORCE DIAGNOSIS
     *
     * Correctness:
     *      Correct, because it checks EVERY node.
     *
     * But:
     *      maxDepth() is itself postorder,
     *      then isBalanced(child) starts more traversals.
     *
     * Worst skewed tree:
     *
     *      N + (N-1) + (N-2) + ... -> O(N^2)
     *
     * Balanced tree:
     *      O(N log N)
     */

    static class BalancedWithResult {

        record Result(boolean balanced, int height) {}

        public boolean isBalanced(TreeNode root) {
            return dfs(root).balanced();
        }

        private Result dfs(TreeNode node) {

            if (node == null) {
                return new Result(true, 0);
            }

            Result left = dfs(node.left);
            Result right = dfs(node.right);

            boolean balanced =
                    left.balanced()
                            && right.balanced()
                            && Math.abs(left.height() - right.height()) <= 1;

            int height = 1 + Math.max(left.height(), right.height());

            return new Result(balanced, height);
        }
    }

    /*
     * Result-object version is conceptually explicit:
     *
     *      return (balanced, height)
     *
     * Sentinel version compresses the same state into one int:
     *
     *      height OR -1
     *
     * Both are O(N).
     */

    /*==========================================================================
        RELATED 1 — 104. MAXIMUM DEPTH OF BINARY TREE
     ==========================================================================*/

    static class MaximumDepth {

        public int maxDepth(TreeNode root) {

            if (root == null) {
                return 0;
            }

            int leftHeight = maxDepth(root.left);
            int rightHeight = maxDepth(root.right);

            return 1 + Math.max(leftHeight, rightHeight);
        }
    }

    /*
     * Transfer:
     *
     * Parent needs:
     *      subtree height
     *
     * No global answer.
     * No failure state.
     *
     * return = 1 + max(left,right)
     *
     * Time O(N), Space O(H)
     */

    /*==========================================================================
        RELATED 2 — 111. MINIMUM DEPTH OF BINARY TREE
     ==========================================================================*/

    static class MinimumDepthDfs {

        public int minDepth(TreeNode root) {

            if (root == null) {
                return 0;
            }

            int leftDepth = minDepth(root.left);
            int rightDepth = minDepth(root.right);

            // A missing child is NOT a valid root-to-leaf path.
            if (leftDepth == 0 || rightDepth == 0) {
                return 1 + leftDepth + rightDepth;
            }

            return 1 + Math.min(leftDepth, rightDepth);
        }
    }

    static class MinimumDepthBfs {

        public int minDepth(TreeNode root) {

            if (root == null) {
                return 0;
            }

            Queue<TreeNode> queue = new ArrayDeque<>();
            queue.offer(root);

            int depth = 1;

            while (!queue.isEmpty()) {

                int levelSize = queue.size();

                for (int i = 0; i < levelSize; i++) {

                    TreeNode node = queue.poll();

                    if (node.left == null && node.right == null) {
                        return depth;
                    }

                    if (node.left != null) {
                        queue.offer(node.left);
                    }

                    if (node.right != null) {
                        queue.offer(node.right);
                    }
                }

                depth++;
            }

            return depth;
        }
    }

    /*
     * Minimum-depth trap:
     *
     *      return 1 + min(left,right)
     *
     * is WRONG when one child is missing.
     *
     * Example:
     *
     *      1
     *       \
     *        2
     *
     * left depth = 0 does NOT mean root reached a leaf.
     *
     * For "nearest leaf", BFS is often the cleanest interview choice:
     * first leaf reached is globally minimum depth.
     */

    /*==========================================================================
        RELATED 3 — 543. DIAMETER OF BINARY TREE
        Closest "return one thing, update another" anchor
     ==========================================================================*/

    static class DiameterOfBinaryTree {

        private int diameter;

        public int diameterOfBinaryTree(TreeNode root) {
            diameter = 0;
            height(root);
            return diameter;
        }

        private int height(TreeNode node) {

            if (node == null) {
                return 0;
            }

            int leftHeight = height(node.left);
            int rightHeight = height(node.right);

            diameter = Math.max(diameter, leftHeight + rightHeight);

            return 1 + Math.max(leftHeight, rightHeight);
        }
    }

    /*
     * DIAMETER — THREE THINKING TRAPS
     *
     * Mistake 1:
     * ----------
     *      return height(root.left) + height(root.right)
     *
     * This checks only the path THROUGH THE ROOT.
     *
     * Actual diameter may live entirely inside a lower subtree.
     * Therefore EVERY node must be evaluated as a possible turning point.
     *
     *
     * Mistake 2:
     * ----------
     * Mixing:
     *
     *      what I RETURN upward
     *
     * with
     *
     *      what COMPLETE answer I can form here.
     *
     * At node X:
     *
     *               parent
     *                 ^
     *                 |
     *                 X
     *                / \
     *               L   R
     *
     * Return to parent:
     *
     *      1 + max(leftHeight, rightHeight)
     *
     * Only ONE arm can continue upward.
     *
     * Local complete path through X:
     *
     *      leftHeight + rightHeight
     *
     * BOTH arms can meet at X.
     *
     * MEMORY:
     *
     *      RETURN = ONE branch
     *      ANSWER = may combine TWO branches
     *
     *
     * Mistake 3:
     * ----------
     * Mixing node count with edge count.
     *
     * Our height convention:
     *
     *      null -> 0
     *      leaf -> 1
     *
     * Therefore:
     *
     *      path in NODES = left + right + 1
     *      path in EDGES = left + right
     *
     * LeetCode 543 asks for EDGES.
     */

    static class DiameterBruteForce {

        public int diameterOfBinaryTree(TreeNode root) {

            if (root == null) {
                return 0;
            }

            int throughRoot = height(root.left) + height(root.right);
            int leftDiameter = diameterOfBinaryTree(root.left);
            int rightDiameter = diameterOfBinaryTree(root.right);

            return Math.max(
                    throughRoot,
                    Math.max(leftDiameter, rightDiameter)
            );
        }

        private int height(TreeNode node) {

            if (node == null) {
                return 0;
            }

            return 1 + Math.max(height(node.left), height(node.right));
        }
    }

    /*
     * Diameter brute force is CORRECT but may be O(N^2)
     * because height is recomputed for many overlapping subtrees.
     *
     * Optimal version computes:
     *
     *      child heights once
     *      local diameter immediately
     *      returns only the height parent needs
     *
     * Time O(N), Space O(H)
     */

    /*==========================================================================
        RELATED 4 — 124. BINARY TREE MAXIMUM PATH SUM
        Closest cousin to Diameter
     ==========================================================================*/

    static class BinaryTreeMaximumPathSum {

        private int best;

        public int maxPathSum(TreeNode root) {
            best = Integer.MIN_VALUE;
            maxGain(root);
            return best;
        }

        private int maxGain(TreeNode node) {

            if (node == null) {
                return 0;
            }

            int leftGain = Math.max(0, maxGain(node.left));
            int rightGain = Math.max(0, maxGain(node.right));

            best = Math.max(
                    best,
                    node.val + leftGain + rightGain
            );

            return node.val + Math.max(leftGain, rightGain);
        }
    }

    /*
     * EXACT SAME SKELETON AS DIAMETER
     *
     * Diameter:
     *
     *      return to parent
     *          1 + max(leftHeight,rightHeight)
     *
     *      local/global candidate
     *          leftHeight + rightHeight
     *
     * Maximum Path Sum:
     *
     *      return to parent
     *          node.val + max(leftGain,rightGain)
     *
     *      local/global candidate
     *          node.val + leftGain + rightGain
     *
     * Difference:
     *
     *      Negative branches hurt the sum,
     *      so clamp them:
     *
     *          Math.max(0, childGain)
     *
     * RECOGNITION:
     *
     *      Final path may start/end anywhere.
     *      Parent can extend only ONE side.
     *      Current node may combine BOTH sides.
     */

    /*==========================================================================
        RELATED 5 — 687. LONGEST UNIVALUE PATH
     ==========================================================================*/

    static class LongestUnivaluePath {

        private int best;

        public int longestUnivaluePath(TreeNode root) {
            best = 0;
            sameValueArm(root);
            return best;
        }

        private int sameValueArm(TreeNode node) {

            if (node == null) {
                return 0;
            }

            int leftArm = sameValueArm(node.left);
            int rightArm = sameValueArm(node.right);

            int extendLeft = 0;
            int extendRight = 0;

            if (node.left != null && node.left.val == node.val) {
                extendLeft = 1 + leftArm;
            }

            if (node.right != null && node.right.val == node.val) {
                extendRight = 1 + rightArm;
            }

            best = Math.max(best, extendLeft + extendRight);

            return Math.max(extendLeft, extendRight);
        }
    }

    /*
     * Again:
     *
     *      RETURN upward = ONE same-value arm
     *      LOCAL answer  = LEFT arm + RIGHT arm
     *
     * Difference from Diameter:
     *
     * A child arm is usable only when:
     *
     *      child.val == node.val
     *
     * So the familiar tree-DP skeleton stays,
     * but the edge is conditionally allowed.
     */

    /*==========================================================================
        ADVANCED TRANSFER PROBLEMS
     ==========================================================================*/

    /*
     * 333. Largest BST Subtree
     * ------------------------
     *
     * Parent needs MORE THAN ONE scalar.
     *
     * Return a record/state such as:
     *
     *      isBST
     *      min
     *      max
     *      size
     *
     * Lesson:
     *      If one int cannot summarize the subtree,
     *      return a structured state object/record.
     *
     *
     * 968. Binary Tree Cameras
     * ------------------------
     *
     * Child returns a STATE rather than a numeric height:
     *
     *      NEEDS_CAMERA
     *      HAS_CAMERA
     *      COVERED
     *
     * Lesson:
     *      Postorder means "children summarize first";
     *      the summary does not have to be height.
     *
     *
     * N-ary Tree Diameter
     * -------------------
     *
     * Parent may have many children.
     *
     * Return:
     *      largest child arm
     *
     * Local candidate:
     *      largest arm + second-largest arm
     *
     * Same exact "return one / combine best two" idea.
     */

    /*==========================================================================
        PATTERN COMPARISON — WHAT FLOWS UP VS WHAT IS COMPLETED HERE
     ==========================================================================*/

    /*
     * Problem                     RETURN TO PARENT             LOCAL / GLOBAL USE
     * -------------------------------------------------------------------------------
     * Maximum Depth               height                       none
     *
     * Balanced Tree               height OR -1                 validate difference
     *
     * Diameter                    best ONE height arm           combine BOTH heights
     *
     * Maximum Path Sum            best ONE sum arm              combine BOTH gains
     *
     * Longest Univalue Path       best ONE same-value arm       combine BOTH valid arms
     *
     * Largest BST Subtree         BST metadata                  update largest BST
     *
     * Binary Tree Cameras         coverage state                place/count cameras
     */

    /*==========================================================================
        TREE-DP RECOGNITION FLOW
     ==========================================================================*/

    /*
     * New tree question:
     *
     * 1. Does current node depend on completed child information?
     *
     *      YES -> strongly consider POSTORDER.
     *
     * 2. What exactly must each child RETURN?
     *
     *      height?
     *      sum/gain?
     *      boolean?
     *      min/max/size?
     *      enum-like state?
     *
     * 3. Can the final/local answer use BOTH children?
     *
     *      YES -> compute/update it at the CURRENT node.
     *
     * 4. Can BOTH branches legally continue upward?
     *
     *      Usually NO for simple paths.
     *
     *      Then:
     *          local answer may use both
     *          return only best one
     *
     * 5. Is there failure/impossibility?
     *
     *      Consider:
     *          sentinel
     *          record/state
     *
     * 6. Am I accidentally recomputing a subtree?
     *
     *      If yes, ask whether the child can return that information once.
     */

    /*==========================================================================
        CRITICAL DIFFERENCES: DFS / BFS / TOP-DOWN
     ==========================================================================*/

    /*
     * Bottom-Up Postorder
     * -------------------
     * Children -> parent.
     *
     * Use when:
     *      subtree summary determines parent.
     *
     *
     * Top-Down DFS
     * ------------
     * Parent -> children.
     *
     * Use when:
     *      current path/state is carried downward.
     *
     * Examples:
     *      root-to-leaf path sum
     *      path construction
     *      depth carried from root
     *
     *
     * BFS
     * ---
     * Level by level.
     *
     * Use when:
     *      nearest node / minimum unweighted distance
     *      level order
     *      first valid level
     */

    /*==========================================================================
        COMMON INTERVIEW MISTAKES
     ==========================================================================*/

    /*
     * 1. Checking only the root when property must hold globally.
     *
     * 2. Seeing postorder but still starting repeated subtree traversals.
     *
     * 3. Not distinguishing:
     *      RETURN TO PARENT
     *      vs
     *      COMPLETE ANSWER AT CURRENT NODE.
     *
     * 4. Returning both branches upward for a path that cannot branch.
     *
     * 5. Mixing nodes and edges.
     *
     * 6. Forgetting that null may mean:
     *      height 0
     * but NOT necessarily
     *      a valid root-to-leaf endpoint.
     *
     * 7. Using a scalar return when parent actually needs multiple facts.
     */

    /*==========================================================================
        TEST HELPERS
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
                n(20, n(15), n(7))
        );
    }

    private static TreeNode deepUnbalancedButRootLooksBalanced() {

        /*
         *          1
         *         / \
         *        2   2
         *       /     \
         *      3       3
         *     /         \
         *    4           4
         *
         * Root:
         *      left height  = 3
         *      right height = 3
         *
         * Root itself looks balanced.
         *
         * But each node 2 has child-height difference 2.
         */
        return n(
                1,
                n(2, n(3, n(4), null), null),
                n(2, null, n(3, null, n(4)))
        );
    }

    private static TreeNode diameterExample() {
        return n(
                1,
                n(2, n(4), n(5)),
                n(3)
        );
    }

    private static TreeNode diameterNotThroughRoot() {

        /*
         *         1
         *        /
         *       2
         *      / \
         *     3   4
         *    /
         *   5
         *  /
         * 6
         *
         * Longest path:
         *      6-5-3-2-4
         *
         * Diameter = 4 edges.
         * It does NOT pass through root 1.
         */
        return n(
                1,
                n(
                        2,
                        n(3, n(5, n(6), null), null),
                        n(4)
                ),
                null
        );
    }

    private static TreeNode maximumPathSumExample() {
        return n(
                -10,
                n(9),
                n(20, n(15), n(7))
        );
    }

    private static TreeNode univalueExample() {
        return n(
                5,
                n(4, n(1), n(1)),
                n(5, null, n(5))
        );
    }

    /*==========================================================================
        MAIN + SELF-VERIFYING TESTS
     ==========================================================================*/

    public static void main(String[] args) {

        BalancedBinaryTree balanced = new BalancedBinaryTree();

        assert balanced.isBalanced(balancedExample());
        assert balanced.isBalanced(null);
        assert balanced.isBalanced(n(1));
        assert !balanced.isBalanced(deepUnbalancedButRootLooksBalanced());

        BalancedBruteForce bruteBalanced = new BalancedBruteForce();
        assert bruteBalanced.isBalanced(balancedExample());
        assert !bruteBalanced.isBalanced(deepUnbalancedButRootLooksBalanced());

        BalancedWithResult pairBalanced = new BalancedWithResult();
        assert pairBalanced.isBalanced(balancedExample());
        assert !pairBalanced.isBalanced(deepUnbalancedButRootLooksBalanced());

        MaximumDepth maxDepth = new MaximumDepth();
        assert maxDepth.maxDepth(null) == 0;
        assert maxDepth.maxDepth(n(1)) == 1;
        assert maxDepth.maxDepth(balancedExample()) == 3;

        MinimumDepthDfs minDepthDfs = new MinimumDepthDfs();
        MinimumDepthBfs minDepthBfs = new MinimumDepthBfs();

        assert minDepthDfs.minDepth(balancedExample()) == 2;
        assert minDepthBfs.minDepth(balancedExample()) == 2;

        TreeNode oneSided = n(1, null, n(2, null, n(3)));
        assert minDepthDfs.minDepth(oneSided) == 3;
        assert minDepthBfs.minDepth(oneSided) == 3;

        DiameterOfBinaryTree diameter = new DiameterOfBinaryTree();
        assert diameter.diameterOfBinaryTree(null) == 0;
        assert diameter.diameterOfBinaryTree(n(1)) == 0;
        assert diameter.diameterOfBinaryTree(diameterExample()) == 3;
        assert diameter.diameterOfBinaryTree(diameterNotThroughRoot()) == 4;

        DiameterBruteForce diameterBrute = new DiameterBruteForce();
        assert diameterBrute.diameterOfBinaryTree(diameterNotThroughRoot()) == 4;

        BinaryTreeMaximumPathSum maxPathSum = new BinaryTreeMaximumPathSum();
        assert maxPathSum.maxPathSum(maximumPathSumExample()) == 42;
        assert maxPathSum.maxPathSum(n(-3)) == -3;

        LongestUnivaluePath univalue = new LongestUnivaluePath();
        assert univalue.longestUnivaluePath(univalueExample()) == 2;
        assert univalue.longestUnivaluePath(n(1)) == 0;

        System.out.println("All assertions passed.");
    }
}
