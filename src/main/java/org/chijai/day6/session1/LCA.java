package org.chijai.day6.session1;


import java.util.*;

/********************************************************************************************
 * PART 1 / TOTAL 4 — CONTINUATION
 * Sections covered in this part:
 * 1. Top-level public class declaration
 * 2. 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
 * 3. 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
 * 4. 🟢 MENTAL MODEL & INVARIANTS (CANONICAL SOURCE OF TRUTH)
 * 5. 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
 *
 * ⚠️ STOP after this part. Wait for user: “Continue / next part”
 ********************************************************************************************/

public class LCA {

    /****************************************************************************************
     * 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
     *
     * 🔗 https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Tree, Depth-First Search, Binary Tree
     *
     * ----------------------------------------------------------------------------
     * Given a binary tree, find the lowest common ancestor (LCA) of two given nodes
     * in the tree.
     *
     * According to the definition of LCA on Wikipedia: “The lowest common ancestor
     * is defined between two nodes p and q as the lowest node in T that has both p
     * and q as descendants (where we allow a node to be a descendant of itself).”
     *
     *
     * Example 1:
     *
     * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
     * Output: 3
     * Explanation: The LCA of nodes 5 and 1 is 3.
     *
     * Example 2:
     *
     * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
     * Output: 5
     * Explanation: The LCA of nodes 5 and 4 is 5, since a node can be a descendant of
     * itself according to the LCA definition.
     *
     * Example 3:
     *
     * Input: root = [1,2], p = 1, q = 2
     * Output: 1
     *
     *
     * Constraints:
     *
     * The number of nodes in the tree is in the range [2, 10^5].
     * -10^9 <= Node.val <= 10^9
     * All Node.val are unique.
     * p != q
     * p and q will exist in the tree.
     *
     ****************************************************************************************/


    /****************************************************************************************
     * 3️⃣ 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
     *
     * 🔵 Pattern Name:
     * Lowest Common Ancestor via Postorder Invariant Propagation
     *
     * 🔵 Problem Archetype:
     * “Find the lowest node where two independent targets converge in a hierarchy.”
     *
     * 🟢 Core Invariant (MANDATORY — one sentence):
     * At every node, the recursion truthfully reports whether p, q, or their LCA
     * exists in the current subtree.
     *
     * 🟡 Why this invariant makes the pattern work:
     * Because the first node (from bottom-up) whose left and right subtrees (or self)
     * collectively contain both targets must be the lowest common ancestor.
     *
     * 🔵 When this pattern applies:
     * • Tree (not necessarily BST)
     * • Nodes exist in the tree
     * • “Lowest / deepest meeting point” questions
     *
     * 🧭 Pattern recognition signals:
     * • Tree + two targets
     * • No parent pointers
     * • “Lowest” wording
     * • Order does NOT matter
     *
     * ⚫ How this pattern differs from similar patterns:
     * • NOT a path-comparison problem
     * • NOT a BST ordering problem
     * • NOT level-based
     * • This is a bottom-up truth aggregation pattern
     *
     ****************************************************************************************/


    /****************************************************************************************
     * 4️⃣ 🟢 MENTAL MODEL & INVARIANTS (CANONICAL SOURCE OF TRUTH)
     *
     * 🧠 Mental Model (think, don’t code):
     * Ask every subtree one question:
     *   “Do you contain p, q, or the answer already?”
     *
     * Then combine answers at the parent.
     *
     * ----------------------------------------------------------------------------
     * 🟢 Invariant #1 (Truth propagation):
     * Each recursive call must correctly report whether p or q exists in its subtree.
     *
     * 🟢 Invariant #2 (Lowest guarantee):
     * The first node (lowest in tree) where both p and q are found across children
     * (or self) is the LCA.
     *
     * 🟢 Invariant #3 (No upward corruption):
     * Once LCA is found, it must be returned unchanged upward.
     *
     * ----------------------------------------------------------------------------
     * 🟡 State Representation:
     * • Node return value:
     *     - null → neither p nor q in subtree
     *     - p or q → one target found
     *     - LCA node → both targets found below
     *
     * ----------------------------------------------------------------------------
     * 🟡 Allowed Moves:
     * • Postorder traversal (left → right → self)
     * • Combine left and right results
     *
     * 🔴 Forbidden Moves:
     * • Preorder decision-making (too early)
     * • Assuming root-based answers
     * • Global mutable flags without invariant control
     *
     * ----------------------------------------------------------------------------
     * 🟡 Termination Logic:
     * • Leaf returns itself if it matches p or q
     * • Null returns null
     *
     * ----------------------------------------------------------------------------
     * 🔴 Why common alternatives fail:
     * • Root-to-node paths → memory heavy, comparison complexity
     * • Parent pointers → not provided
     * • Level-based → ignores ancestry relationships
     *
     ****************************************************************************************/


    /****************************************************************************************
     * 5️⃣ 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
     *
     * 🔴 Wrong Approach #1: Store paths from root to p and q
     *
     * Why it seems correct:
     * • LCA is last common node in paths
     *
     * Why it fails invariant thinking:
     * • Path storage ignores “lowest-first” discovery
     * • Requires extra memory O(n)
     * • Over-engineered for interviewer expectations
     *
     * Minimal counterexample:
     * • Deep skewed tree → stack overflow risk
     *
     * ----------------------------------------------------------------------------
     * 🔴 Wrong Approach #2: Assume BST logic (value comparison)
     *
     * Why it seems correct:
     * • Works for BST LCA
     *
     * Exact invariant violated:
     * ❌ Assumes ordering invariant that DOES NOT exist
     *
     * Minimal counterexample:
     *   3
     *  / \
     * 5   1
     *
     * ----------------------------------------------------------------------------
     * 🔴 Wrong Approach #3: Count matches globally
     *
     * Why it seems correct:
     * • “When count == 2, that node is LCA”
     *
     * Why it fails:
     * • Breaks locality invariant
     * • Loses “lowest” guarantee
     *
     * ----------------------------------------------------------------------------
     * 🟣 Interviewer traps:
     * • “Can p be ancestor of q?” → YES
     * • “Do values matter?” → NO, nodes matter
     * • “Can you do this in one traversal?” → YES (this pattern)
     *
     ****************************************************************************************/


/****************************************************************************************
 * 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES (DERIVED FROM INVARIANT)
 ****************************************************************************************/

    /**
     * 🔵 Binary Tree Node Definition (canonical LeetCode shape)
     */
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            this.val = x;
        }
    }

    /****************************************************************************************
     * 🔴 BRUTE FORCE SOLUTION
     *
     * Core idea:
     * • Find path from root → p
     * • Find path from root → q
     * • Walk until paths diverge
     *
     * Which invariant it enforces:
     * • Correct ancestry, but NOT lowest-first discovery
     *
     * Limitation:
     * • Extra memory
     * • Two traversals + path comparison
     *
     * Time:  O(n)
     * Space: O(n)
     *
     * Interview preference:
     * ❌ Acceptable but not ideal
     ****************************************************************************************/
    static class LCA_BruteForce {

        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            java.util.List<TreeNode> pathP = new java.util.ArrayList<>();
            java.util.List<TreeNode> pathQ = new java.util.ArrayList<>();

            findPath(root, p, pathP);
            findPath(root, q, pathQ);

            int i = 0;
            TreeNode lca = null;
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

        // 🟡 DFS path discovery (root → target)
        private boolean findPath(TreeNode node, TreeNode target, java.util.List<TreeNode> path) {
            if (node == null) return false;

            path.add(node);

            if (node == target) return true;

            if (findPath(node.left, target, path) ||
                    findPath(node.right, target, path)) {
                return true;
            }

            // 🔴 backtrack if target not found here
            path.remove(path.size() - 1);
            return false;
        }
    }

    /****************************************************************************************
     * 🟡 IMPROVED SOLUTION
     *
     * Core idea:
     * • Single DFS
     * • Count matches in subtrees
     * • Mark LCA when count == 2
     *
     * Which invariant it enforces:
     * • Presence tracking, but with external state
     *
     * Limitation:
     * • Uses mutable global variable
     * • Harder to reason under interview pressure
     *
     * Time:  O(n)
     * Space: O(h)
     *
     * Interview preference:
     * ⚠️ Acceptable but less clean
     ****************************************************************************************/
    static class LCA_Improved {

        private TreeNode lca = null;

        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            dfs(root, p, q);
            return lca;
        }

        // returns how many targets found in subtree
        private int dfs(TreeNode node, TreeNode p, TreeNode q) {
            if (node == null) return 0;

            int left = dfs(node.left, p, q);
            int right = dfs(node.right, p, q);

            int mid = (node == p || node == q) ? 1 : 0;

            if (left + right + mid >= 2 && lca == null) {
                // 🔴 first such node = lowest
                lca = node;
            }

            return left + right + mid;
        }
    }

    /****************************************************************************************
     * 🟢 OPTIMAL SOLUTION (INTERVIEW-PREFERRED)
     *
     * Core idea:
     * • Postorder traversal
     * • Each call returns:
     *     - null (no target)
     *     - p or q
     *     - LCA node
     *
     * Which invariant it enforces:
     * 🟢 Truthful subtree reporting + lowest-first discovery
     *
     * What limitation it fixes:
     * • No globals
     * • Single traversal
     * • Pure invariant logic
     *
     * Time:  O(n)
     * Space: O(h)
     *
     * Interview preference:
     * ✅ STRONGLY preferred
     /****************************************************************************************
     * 🔴 APPROACH A — RECURSIVE POSTORDER (CANONICAL INVARIANT SOLUTION)
     *
     * Core invariant:
     * Each recursive call returns one truthful signal:
     *   • null  → neither p nor q found
     *   • p/q   → exactly one found
     *   • LCA   → both found and resolved
     *
     * Time:  O(n)
     * Space: O(h)
     * Interview preference: ⭐⭐⭐⭐⭐ (PRIMARY)
     ****************************************************************************************/
    static class LCA_Recursive {

        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {

            // 🟢 Base case enforces truthfulness
            if (root == null || root == p || root == q) {
                return root;
            }

            // 🟡 Postorder: resolve children first
            TreeNode left = lowestCommonAncestor(root.left, p, q);
            TreeNode right = lowestCommonAncestor(root.right, p, q);

            // 🟢 Both sides found → this is the LOWEST join point
            if (left != null && right != null) {
                return root;
            }

            // 🟡 Propagate the single truth upward
            return (left != null) ? left : right;
        }
    }


    /****************************************************************************************
     * 🟡 APPROACH B — PARENT POINTERS + ANCESTOR INTERSECTION
     *
     * Core invariant:
     * The first intersection of ancestor chains of p and q is the LCA.
     *
     * Time:  O(n)
     * Space: O(n)
     * Interview preference: ⭐⭐⭐ (CONDITIONAL)
     ****************************************************************************************/
    static class LCA_UsingParentPointers {

        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {

            // Map each node → its parent
            java.util.Map<TreeNode, TreeNode> parent = new java.util.HashMap<>();
            java.util.Deque<TreeNode> stack = new java.util.ArrayDeque<>();

            parent.put(root, null);
            stack.push(root);

            // Build parent pointers until both p and q are found
            while (!parent.containsKey(p) || !parent.containsKey(q)) {
                TreeNode node = stack.pop();

                if (node.left != null) {
                    parent.put(node.left, node);
                    stack.push(node.left);
                }
                if (node.right != null) {
                    parent.put(node.right, node);
                    stack.push(node.right);
                }
            }

            // Collect ancestors of p
            java.util.Set<TreeNode> ancestors = new java.util.HashSet<>();
            while (p != null) {
                ancestors.add(p);
                p = parent.get(p);
            }

            // First ancestor of q appearing in p's ancestor set is LCA
            while (!ancestors.contains(q)) {
                q = parent.get(q);
            }

            return q;
        }
    }


/****************************************************************************************
 * 7️⃣ 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
 *
 * 🟣 State the invariant:
 * “Each subtree reports whether it contains p, q, or their LCA.”
 *
 * 🟣 Discard logic:
 * • If neither side reports → discard subtree
 * • If only one side reports → propagate upward
 *
 * 🟣 Why correctness is guaranteed:
 * • Postorder ensures children resolved before parent
 * • First node seeing both targets must be the lowest
 *
 * 🟣 What breaks if changed:
 * • Preorder → premature decisions
 * • Globals → loss of locality
 *
 * 🟣 In-place / streaming feasibility:
 * • In-place (stack only)
 * • Streaming not applicable (tree dependency)
 *
 * 🟣 When NOT to use this pattern:
 * • When parent pointers exist (simpler upward walk)
 * • When tree is BST (ordering-based shortcut)
 *
 ****************************************************************************************/


/****************************************************************************************
 * 8️⃣ 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
 *
 * 🟢 Invariant-preserving changes:
 * • Return boolean flags instead of nodes
 * • Wrap return in Result object (foundP, foundQ, lca)
 *
 * 🟡 Reasoning-only changes:
 * • Convert recursion to explicit stack
 * • Tail recursion elimination
 *
 * 🔴 Pattern-break signals:
 * • More than two targets
 * • Dynamic tree updates
 * • DAG instead of tree
 *
 ****************************************************************************************/

/****************************************************************************************
 * 9️⃣ ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
 *
 * All problems below use the SAME invariant:
 * “Each subtree truthfully reports whether the target(s) exist below it,
 *  and the first node combining required truths is the answer.”
 ****************************************************************************************/

    /****************************************************************************************
     * ⚫ Reinforcement Problem 1:
     * Lowest Common Ancestor of a Binary Tree II
     *
     * 🔗 https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-ii/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Tree, DFS
     *
     * ----------------------------------------------------------------------------
     * FULL OFFICIAL STATEMENT:
     *
     * Given the root of a binary tree, return the lowest common ancestor (LCA)
     * of two given nodes, p and q. If either node p or q does not exist in the tree,
     * return null.
     *
     * All Node.val are unique.
     *
     * ----------------------------------------------------------------------------
     * 🟢 Invariant Mapping:
     * • Subtree must report BOTH presence and LCA validity
     *
     * ----------------------------------------------------------------------------
     * 🟢 Java Solution:
     ****************************************************************************************/
    static class LCA_II {

        static class Result {
            TreeNode node;
            boolean foundP;
            boolean foundQ;

            Result(TreeNode n, boolean p, boolean q) {
                node = n;
                foundP = p;
                foundQ = q;
            }
        }

        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            Result r = dfs(root, p, q);
            return (r.foundP && r.foundQ) ? r.node : null;
        }

        private Result dfs(TreeNode node, TreeNode p, TreeNode q) {
            if (node == null) return new Result(null, false, false);

            Result left = dfs(node.left, p, q);
            Result right = dfs(node.right, p, q);

            boolean foundP = left.foundP || right.foundP || node == p;
            boolean foundQ = left.foundQ || right.foundQ || node == q;

            if (left.node != null) return left;
            if (right.node != null) return right;

            if (foundP && foundQ) return new Result(node, true, true);

            return new Result(null, foundP, foundQ);
        }
    }

    /****************************************************************************************
     * ⚫ Reinforcement Problem 2:
     * Lowest Common Ancestor of a Binary Tree III
     *
     * 🔗 https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Tree, Parent Pointer
     *
     * ----------------------------------------------------------------------------
     * FULL OFFICIAL STATEMENT:
     *
     * Given two nodes of a binary tree p and q, return their lowest common ancestor (LCA).
     * Each node has a parent pointer to its parent.
     *
     * ----------------------------------------------------------------------------
     * 🟢 Invariant Mapping:
     * • First intersection of ancestor chains
     *
     * ----------------------------------------------------------------------------
     * 🟢 Java Solution:
     ****************************************************************************************/
    static class LCA_III {

        static class Node {
            int val;
            Node parent;
            Node left;
            Node right;
        }

        public Node lowestCommonAncestor(Node p, Node q) {
            java.util.Set<Node> visited = new java.util.HashSet<>();

            while (p != null) {
                visited.add(p);
                p = p.parent;
            }

            while (q != null) {
                if (visited.contains(q)) return q;
                q = q.parent;
            }
            return null;
        }
    }

    /****************************************************************************************
     * ⚫ Reinforcement Problem 3:
     * Lowest Common Ancestor of a Binary Tree IV
     *
     * 🔗 https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iv/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Tree, DFS
     *
     * ----------------------------------------------------------------------------
     * FULL OFFICIAL STATEMENT:
     *
     * Given the root of a binary tree and an array of TreeNode objects nodes,
     * return the lowest common ancestor (LCA) of all the nodes.
     *
     * ----------------------------------------------------------------------------
     * 🟢 Invariant Mapping:
     * • Count-based extension of p/q invariant
     *
     * ----------------------------------------------------------------------------
     * 🟢 Java Solution:
     ****************************************************************************************/
    static class LCA_IV {

        private TreeNode answer;
        private int total;

        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode[] nodes) {
            total = nodes.length;
            java.util.Set<TreeNode> set = new java.util.HashSet<>();
            for (TreeNode n : nodes) set.add(n);
            dfs(root, set);
            return answer;
        }

        private int dfs(TreeNode node, java.util.Set<TreeNode> set) {
            if (node == null) return 0;

            int count = dfs(node.left, set) + dfs(node.right, set);
            if (set.contains(node)) count++;

            if (count == total && answer == null) {
                answer = node;
            }
            return count;
        }
    }

/****************************************************************************************
 * 10️⃣ 🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
 ****************************************************************************************/

/****************************************************************************************
 * 🧩 Related Problem 1:
 * Binary Tree Maximum Path Sum
 *
 * Invariant:
 * • Each node reports max downward contribution
 *
 * Pattern boundary:
 * • Similar postorder truth propagation
 ****************************************************************************************/

/****************************************************************************************
 * 🧩 Related Problem 2:
 * Diameter of Binary Tree
 *
 * Invariant:
 * • Each subtree reports height
 *
 * Pattern boundary:
 * • First node combining two heights gives diameter
 ****************************************************************************************/

/****************************************************************************************
 * 🧩 Related Problem 3:
 * Subtree with All Deepest Nodes
 *
 * Invariant:
 * • Track depth and subtree root simultaneously
 ****************************************************************************************/


/********************************************************************************************
 * PART 4 / TOTAL 4 — CONTINUATION
 * Sections covered in this part:
 * 11. 🟢 LEARNING VERIFICATION
 * 12. 🧪 main() METHOD + SELF-VERIFYING TESTS
 * 13. ✅ COMPLETION CHECKLIST
 * 🧘 FINAL CLOSURE STATEMENT
 *
 * ⚠️ FINAL PART
 * ⚠️ Closes all remaining braces
 ********************************************************************************************/

/****************************************************************************************
 * 11️⃣ 🟢 LEARNING VERIFICATION
 *
 * • Invariant recall without code:
 *   “Each subtree truthfully reports whether it contains p, q, or the LCA.”
 *
 * • Naive failure explanation:
 *   Root-based or preorder decisions violate the ‘lowest-first’ invariant.
 *
 * • Debugging readiness:
 *   If wrong answer → check postorder order or early returns.
 *
 * • Pattern recognition signals:
 *   ‘Lowest’, ‘common’, ‘ancestor’, ‘tree’, ‘no parent pointer’.
 *
 ****************************************************************************************/


    /****************************************************************************************
     * 12️⃣ 🧪 main() METHOD + SELF-VERIFYING TESTS
     *
     * Tests are explicit, reasoned, and assert correctness programmatically.
     ****************************************************************************************/
    public static void main(String[] args) {

        /*
         * Test Tree:
         *          3
         *        /   \
         *       5     1
         *      / \   / \
         *     6   2 0   8
         *        / \
         *       7   4
         */
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(5);
        root.right = new TreeNode(1);
        root.left.left = new TreeNode(6);
        root.left.right = new TreeNode(2);
        root.right.left = new TreeNode(0);
        root.right.right = new TreeNode(8);
        root.left.right.left = new TreeNode(7);
        root.left.right.right = new TreeNode(4);

        TreeNode p = root.left;              // 5
        TreeNode q = root.right;             // 1
        TreeNode r = root.left.right.right;  // 4



        LCA_Recursive rec = new LCA_Recursive();
        LCA_UsingParentPointers pp = new LCA_UsingParentPointers();

        assert rec.lowestCommonAncestor(root, p, q).val == 3;
        assert pp.lowestCommonAncestor(root, p, q).val == 3;


        System.out.println("✅ All LCA invariant tests passed.");
    }


/****************************************************************************************
 * 13️⃣ ✅ COMPLETION CHECKLIST
 *
 * • Invariant → Each subtree reports presence of p/q/LCA
 * • Search target → Lowest node aggregating both truths
 * • Discard rule → Subtrees with no targets return null
 * • Termination → Leaf or null
 * • Naive failure → Violates lowest-first discovery
 * • Edge cases → p ancestor of q, root involvement
 * • Variant readiness → LCA II, III, IV supported
 * • Pattern boundary → Fails for DAG or dynamic trees
 *
 ****************************************************************************************/

/****************************************************************************************
 * 🧘 FINAL CLOSURE STATEMENT
 *
 * I understand the invariant.
 * I can re-derive the solution.
 * This chapter is complete.
 *
 ****************************************************************************************/

}
