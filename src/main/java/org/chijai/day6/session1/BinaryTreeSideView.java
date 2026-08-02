package org.chijai.day6.session1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BinaryTreeSideView {

    /*
     * ============================================================
     * 2. 📘 PRIMARY PROBLEM
     * ============================================================
     *
     * Title:
     * Binary Tree Right Side View
     *
     * Difficulty:
     * Medium
     *
     * Tags:
     * Binary Tree
     * Breadth-First Search (BFS)
     * Depth-First Search (DFS)
     * Level Order Traversal
     *
     * Problem Description:
     *
     * Given the root of a binary tree, imagine standing on the
     * right side of the tree.
     *
     * From this viewpoint, exactly one node from every depth is
     * visible.
     *
     * Return those visible node values ordered from top to bottom.
     *
     * Constraints:
     *
     * - Number of nodes: [0,100]
     * - -100 <= Node.val <= 100
     *
     * Representative Example 1
     *
     * Input:
     *
     *            1
     *          /   \
     *         2     3
     *          \     \
     *           5     4
     *
     * Output:
     * [1,3,4]
     *
     * Explanation:
     *
     * Depth 0 -> 1
     * Depth 1 -> 3
     * Depth 2 -> 4
     *
     * Representative Example 2
     *
     * Input:
     *
     *      1
     *       \
     *        3
     *
     * Output:
     * [1,3]
     *
     * Representative Example 3
     *
     * Input:
     * []
     *
     * Output:
     * []
     *
     * Official LeetCode:
     * https://leetcode.com/problems/binary-tree-right-side-view/
     */

    /*
     * ============================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern
     * -------
     * Level Order Traversal (Breadth-First Search)
     *
     * Archetype
     * ---------
     * One answer per tree level.
     *
     * Core Invariant
     * --------------
     * During processing of one BFS level, every node belonging to
     * that depth is visited exactly once.
     *
     * Therefore:
     *
     * - first node of the level
     * - last node of the level
     *
     * can be identified deterministically depending on traversal
     * order.
     *
     * Why It Works
     * ------------
     * The queue naturally partitions nodes by depth.
     *
     * Before processing begins,
     *
     * queue.size()
     *
     * equals the exact number of nodes in the current level.
     *
     * Those nodes cannot mix with deeper nodes because children are
     * appended only after their parent is processed.
     *
     * Recognition Signals
     * -------------------
     * Use this pattern whenever the question asks:
     *
     * - every level
     * - left view
     * - right view
     * - average of level
     * - largest per level
     * - zigzag traversal
     * - width
     *
     * When To Use
     * -----------
     * ✓ Level-based aggregation.
     *
     * ✓ Need one answer per depth.
     *
     * ✓ Processing order depends on levels.
     *
     * When NOT To Use
     * ---------------
     * ✗ Root-to-leaf path enumeration.
     *
     * ✗ Lowest Common Ancestor.
     *
     * ✗ BST ordering.
     *
     * ✗ Tree DP.
     *
     * Comparison
     * ----------
     *
     * BFS:
     * ----
     * Naturally groups nodes by depth.
     *
     * DFS:
     * ----
     * Requires explicit depth bookkeeping.
     *
     * Either solves this problem optimally.
     *
     * BFS is mechanically simpler for interviews.
     */

    /*
     * ============================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * Mental Model
     * ------------
     * Imagine every tree level standing in a horizontal row.
     *
     * We inspect one row completely before touching the next row.
     *
     * The queue stores exactly the current frontier.
     *
     * At the end of processing one row,
     * the final processed node is the visible node from the right.
     *
     * Visual Example
     *
     *              1
     *            /   \
     *           2     3
     *            \     \
     *             5     4
     *
     * Queue Evolution
     *
     * [1]
     *
     * answer = 1
     *
     * ------------------------
     *
     * [2,3]
     *
     * answer = 3
     *
     * ------------------------
     *
     * [5,4]
     *
     * answer = 4
     *
     * ------------------------
     *
     * Result
     *
     * [1,3,4]
     *
     * ------------------------------------------------------------
     * 🟢 Primary Invariant
     * ------------------------------------------------------------
     *
     * Before processing a level,
     *
     * queue contains every node of exactly one depth.
     *
     * No deeper node belongs to this level.
     *
     * ------------------------------------------------------------
     * 🟢 Secondary Invariant
     * ------------------------------------------------------------
     *
     * levelSize is frozen before the loop starts.
     *
     * Newly inserted children never affect the current level.
     *
     * ------------------------------------------------------------
     * 🟢 Visibility Invariant
     * ------------------------------------------------------------
     *
     * Children are enqueued:
     *
     * left
     * then
     * right
     *
     * Therefore,
     * the last processed node of the level is the rightmost node.
     *
     * If enqueue order changes,
     * the visibility rule also changes.
     *
     * ------------------------------------------------------------
     * Variable Meanings
     * ------------------------------------------------------------
     *
     * queue
     * -----
     * Current search space.
     *
     * levelSize
     * ---------
     * Number of nodes belonging to this depth.
     *
     * current
     * -------
     * Node currently leaving the frontier.
     *
     * answer
     * ------
     * Visible nodes collected so far.
     *
     * ------------------------------------------------------------
     * Allowed State Transitions
     * ------------------------------------------------------------
     *
     * Poll one node.
     *
     * Visit it.
     *
     * Append its children.
     *
     * Continue.
     *
     * ------------------------------------------------------------
     * Forbidden Moves
     * ------------------------------------------------------------
     *
     * Never compute queue.size() inside the loop because children
     * continuously enter the queue.
     *
     * Never append the answer before knowing whether this node is
     * the last node of the level.
     *
     * Never mix two levels into one iteration.
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * Queue becomes empty.
     *
     * Every node has been processed exactly once.
     *
     * ------------------------------------------------------------
     * Correctness Intuition
     * ------------------------------------------------------------
     *
     * Since every level is processed completely before the next,
     * the final node removed from that level is precisely the node
     * farthest to the right under left-first enqueue order.
     *
     * Recording that node after every level constructs the desired
     * right-side view.
     *
     * ------------------------------------------------------------
     * Why Naive Solutions Fail
     * ------------------------------------------------------------
     *
     * Simply walking repeatedly toward right children fails.
     *
     * Example
     *
     *          1
     *         /
     *        2
     *         \
     *          5
     *
     * The visible node at depth two is 5 although no right child
     * exists from the root.
     *
     * Visibility depends on level ordering,
     * not merely following right pointers.
     */

    /*
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * Mistake 1
     * ---------
     * Forget freezing level size.
     *
     * Why It Looks Correct
     * --------------------
     * Queue always stores nodes.
     *
     * Violated Invariant
     * ------------------
     * Current level becomes mixed with the next level.
     *
     * Counterexample
     *
     *      1
     *     / \
     *    2   3
     *
     * Processing until queue becomes empty records incorrect
     * boundaries.
     *
     * ------------------------------------------------------------
     *
     * Mistake 2
     * ---------
     * Record first node instead of last node.
     *
     * Violated Invariant
     * ------------------
     * Under left-first enqueue order,
     * first node is leftmost.
     *
     * You accidentally compute the left-side view.
     *
     * ------------------------------------------------------------
     *
     * Mistake 3
     * ---------
     * Enqueue right child before left child while still recording
     * the last processed node.
     *
     * Violated Invariant
     * ------------------
     * Last processed becomes leftmost.
     *
     * Either:
     *
     * - keep left-first enqueue and record last
     *
     * OR
     *
     * - keep right-first enqueue and record first
     *
     * Mixing strategies breaks correctness.
     *
     * ------------------------------------------------------------
     *
     * Mistake 4
     * ---------
     * Missing null root.
     *
     * Result
     * ------
     * NullPointerException.
     *
     * ------------------------------------------------------------
     *
     * Interview Trap
     * --------------
     * Interviewer may ask:
     *
     * "Can DFS solve this?"
     *
     * Yes.
     *
     * Visit:
     *
     * right before left.
     *
     * Record the first node encountered at every depth.
     *
     * Different traversal.
     *
     * Same invariant:
     *
     * first visit at a depth represents the visible node.
     */

    /*
     * ============================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Mechanical Typing Order
     *
     * 1.
     * Handle null root.
     *
     * 2.
     * Create answer list.
     *
     * 3.
     * Create queue.
     *
     * 4.
     * Push root.
     *
     * 5.
     * while queue not empty
     *
     * 6.
     * Freeze level size.
     *
     * 7.
     * Repeat levelSize times.
     *
     * 8.
     * Poll current node.
     *
     * 9.
     * If last node of level
     * add answer.
     *
     * 10.
     * Push left child.
     *
     * 11.
     * Push right child.
     *
     * 12.
     * Return answer.
     *
     * Loop Skeleton
     *
     * while (...)
     *     levelSize
     *     repeat levelSize
     *         poll
     *         maybe record
     *         enqueue children
     *
     * Return
     *
     * answer
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * if empty
     *     return
     *
     * enqueue root
     *
     * while queue not empty
     *     freeze level size
     *     repeat level size
     *         remove node
     *         if last
     *             record
     *         enqueue children
     *
     * return answer
     */

    /*
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
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
     * ------------------------------------------------------------
     * Brute Force
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * For every depth independently,
     * search the tree to determine the rightmost node.
     *
     * Invariant
     * ---------
     * Each traversal answers exactly one level.
     *
     * Limitation
     * ----------
     * Repeated traversal.
     *
     * Complexity
     * ----------
     * Time : O(N * H)
     * Space: O(H)
     *
     * Interview Usefulness
     * --------------------
     * Mostly educational.
     * Rarely implemented.
     */

    static class BruteForce {

        public List<Integer> rightSideView(TreeNode root) {
            List<Integer> answer = new ArrayList<>();
            int height = height(root);

            for (int level = 0; level < height; level++) {
                Integer value = findRightmost(root, level);
                if (value != null) {
                    answer.add(value);
                }
            }

            return answer;
        }

        private int height(TreeNode node) {
            if (node == null) {
                return 0;
            }

            return 1 + Math.max(height(node.left), height(node.right));
        }

        private Integer findRightmost(TreeNode node, int depth) {
            if (node == null) {
                return null;
            }

            if (depth == 0) {
                return node.val;
            }

            Integer right = findRightmost(node.right, depth - 1);

            if (right != null) {
                return right;
            }

            return findRightmost(node.left, depth - 1);
        }
    }

    /*
     * ------------------------------------------------------------
     * Improved
     * ------------------------------------------------------------
     *
     * Idea
     * ----
     * DFS.
     *
     * Visit right subtree before left subtree.
     *
     * First visit at every depth is visible.
     *
     * Invariant
     * ---------
     * First node reaching a depth is the right-side node.
     *
     * Improvement
     * -----------
     * Single traversal.
     *
     * Complexity
     * ----------
     * Time : O(N)
     * Space: O(H)
     *
     * Interview Usefulness
     * --------------------
     * Excellent recursive alternative.
     */

    static class Improved {


        public List<Integer> rightSideView(TreeNode root) {
            List<Integer> answer = new ArrayList<>();
            dfs(root, 0, answer);
            return answer;
        }

        private void dfs(TreeNode node, int depth, List<Integer> answer) {

            if (node == null) {
                return;
            }

            // 🟢 Invariant:
            // First visit to this depth comes from the rightmost path.
            if (depth == answer.size()) {
                answer.add(node.val);
            }

            // Explore right first so visibility is preserved.
            dfs(node.right, depth + 1, answer);

            // Left subtree only fills depths not yet seen.
            dfs(node.left, depth + 1, answer);
        }
    }

    /*
     * ------------------------------------------------------------
     * Optimal (Interview Preferred)
     * ------------------------------------------------------------
     *
     * Pattern
     * -------
     * Breadth-First Search
     *
     * Idea
     * ----
     * Process exactly one tree level at a time.
     *
     * Since children are inserted left before right,
     * the final node removed from the current level is
     * exactly the rightmost node.
     *
     * Core Invariant
     * --------------
     * Before each outer-loop iteration,
     * the queue contains every node belonging to one
     * and only one depth.
     *
     * Correctness
     * -----------
     * Freezing queue.size() guarantees level separation.
     *
     * Complexity
     * ----------
     * Time  : O(N)
     * Space : O(W)
     *
     * where
     *
     * W = maximum tree width.
     *
     * Interview Usefulness
     * --------------------
     * This is the most mechanically reconstructable
     * implementation and therefore the preferred
     * whiteboard solution.
     */

    static class Optimal {

        public List<Integer> rightSideView(TreeNode root) {

            List<Integer> answer = new ArrayList<>();

            // Empty tree has no visible nodes.
            if (root == null) {
                return answer;
            }

            Queue<TreeNode> queue = new ArrayDeque<>();

            queue.offer(root);

            while (!queue.isEmpty()) {

                // 🟢 Invariant:
                // Exactly these many nodes belong to this depth.
                int levelSize = queue.size();

                for (int i = 0; i < levelSize; i++) {

                    TreeNode current = queue.poll();

                    // 🟢 Invariant:
                    // Left-first enqueue makes the last processed node
                    // the visible node from the right.
                    if (i == levelSize - 1) {
                        answer.add(current.val);
                    }

                    // Preserve level ordering for the next frontier.
                    if (current.left != null) {
                        queue.offer(current.left);
                    }

                    if (current.right != null) {
                        queue.offer(current.right);
                    }
                }
            }

            return answer;
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * If asked:
 *
 * "Explain your approach."
 *
 * A concise answer:
 *
 * I use Level Order Traversal.
 *
 * The queue always stores exactly one tree level before
 * processing begins.
 *
 * I freeze queue.size() so children added during traversal
 * never mix with the current level.
 *
 * Because I enqueue left before right,
 * the last node removed from that level is the rightmost
 * visible node.
 *
 * I record that node and continue until the queue becomes
 * empty.
 *
 * ------------------------------------------------------------
 *
 * Invariant
 * ---------
 * Queue contains exactly one level.
 *
 * ------------------------------------------------------------
 *
 * Search Space
 * ------------
 * Current BFS frontier.
 *
 * ------------------------------------------------------------
 *
 * Discard Rule
 * ------------
 * After finishing a level,
 * those nodes are never revisited.
 *
 * ------------------------------------------------------------
 *
 * Correctness
 * -----------
 * Every level contributes exactly one visible node.
 *
 * ------------------------------------------------------------
 *
 * Termination
 * -----------
 * Queue becomes empty after every node has been processed.
 *
 * ------------------------------------------------------------
 *
 * In-place Feasibility
 * --------------------
 * No.
 *
 * BFS requires auxiliary storage proportional to the maximum
 * width of the tree.
 *
 * ------------------------------------------------------------
 *
 * Streaming Feasibility
 * ---------------------
 * No.
 *
 * Children must be retained until the next level begins.
 *
 * ------------------------------------------------------------
 *
 * When NOT To Use
 * ---------------
 * Do not use this pattern when answers depend on
 * root-to-leaf state,
 * subtree DP,
 * BST ordering,
 * or path accumulation.
 */

/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 * One answer per tree level.
 *
 * Pattern
 * -------
 * Level Order Traversal.
 *
 * Invariant
 * ---------
 * Queue contains exactly one depth.
 *
 * Search Target
 * -------------
 * Last processed node of every level.
 *
 * Discard Rule
 * ------------
 * Entire level is discarded after processing.
 *
 * Common Trap
 * -----------
 * Calling queue.size() inside the inner loop.
 *
 * Edge Cases
 * ----------
 * - Empty tree
 * - One node
 * - Left skew
 * - Right skew
 * - Complete tree
 *
 * One-Liner
 * ---------
 * Freeze level size,
 * process the level,
 * record the last node.
 *
 * Re-derivation Cue
 * -----------------
 * Visibility is determined only after every node of the level
 * has been considered.
 */

/*
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variation 1
 * -----------
 * Left Side View
 *
 * Preserve the same BFS.
 *
 * Record the first node of every level instead of the last.
 *
 * Invariant stays identical.
 *
 * ------------------------------------------------------------
 *
 * Variation 2
 * -----------
 * Right-first DFS.
 *
 * Record the first node reaching every depth.
 *
 * Pattern changes.
 *
 * Visibility invariant remains equivalent.
 *
 * ------------------------------------------------------------
 *
 * Variation 3
 * -----------
 * Largest Value in Each Tree Row.
 *
 * Replace
 *
 * "record last"
 *
 * with
 *
 * "maintain maximum".
 *
 * Queue invariant does not change.
 *
 * ------------------------------------------------------------
 *
 * Variation 4
 * -----------
 * Average of Levels.
 *
 * Aggregate sums over one frozen level.
 *
 * Same level boundary.
 *
 * Different aggregation.
 *
 * ------------------------------------------------------------
 *
 * Variation 5
 * -----------
 * Zigzag Traversal.
 *
 * Same BFS.
 *
 * Reverse insertion order into result or alternate writing
 * direction.
 *
 * Level invariant is unchanged.
 *
 * ------------------------------------------------------------
 *
 * Pattern Break
 * -------------
 *
 * Suppose queue size is recomputed continuously.
 *
 * Example
 *
 *          1
 *         / \
 *        2   3
 *
 * After visiting 1,
 * nodes 2 and 3 immediately enter the queue.
 *
 * If queue.size() is not frozen,
 * the algorithm loses the boundary separating
 * depth 0 and depth 1.
 *
 * The core invariant is violated.
 *
 * ------------------------------------------------------------
 *
 * Another Pattern Break
 * ---------------------
 *
 * Keep recording the last node,
 * but enqueue right child before left child.
 *
 * Now the last processed node becomes the leftmost node.
 *
 * The algorithm silently computes the left-side view instead.
 */

/*
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * Can you answer these without looking?
 *
 * □ What invariant keeps levels separated?
 *
 * □ Why is queue.size() frozen?
 *
 * □ Why is the last processed node visible?
 *
 * □ What changes for left-side view?
 *
 * □ Why does recomputing queue.size() fail?
 *
 * □ What happens for an empty tree?
 *
 * □ Can DFS solve the same problem?
 *
 * □ Which traversal order must DFS use?
 *
 * □ Why is complexity O(N)?
 *
 * □ What determines auxiliary space?
 *
 * □ Can you derive the algorithm from only:
 *
 *    queue
 *    levelSize
 *    last node
 *    enqueue children
 *
 * If yes,
 * implementation has become mechanical.
 */

/*
 * ============================================================
 * ⚫ PATTERN MAPPING
 * ============================================================
 *
 * Binary Tree Right Side View belongs to the family of
 * "Level Aggregation" problems.
 *
 * Generic Skeleton
 * ----------------
 *
 * initialize queue
 *
 * while queue not empty
 *     freeze level size
 *
 *     initialize level state
 *
 *     repeat levelSize times
 *         poll node
 *         update level state
 *         enqueue children
 *
 *     finalize level state
 *
 * Mapping
 * -------
 *
 * Binary Tree Right Side View
 *      finalize = last node
 *
 * Left Side View
 *      finalize = first node
 *
 * Largest Value Per Row
 *      finalize = maximum
 *
 * Average Of Levels
 *      finalize = sum / count
 *
 * Maximum Level Sum
 *      finalize = compare sums
 *
 * Zigzag Traversal
 *      finalize = reverse insertion order
 *
 * Width Of Binary Tree
 *      finalize = index difference
 *
 * Once the invariant
 *
 * "queue stores exactly one level"
 *
 * becomes automatic,
 * all of these problems reduce to changing only the level
 * aggregation logic.
 */

/*
 * ============================================================
 * 🔍 DEBUGGING GUIDE
 * ============================================================
 *
 * Symptom
 * -------
 * Missing one level.
 *
 * Check
 * -----
 * Did you forget to enqueue a child?
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Output is left-side view.
 *
 * Check
 * -----
 * Either:
 *
 * - first node is recorded
 *
 * OR
 *
 * - enqueue order changed.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Infinite processing.
 *
 * Check
 * -----
 * Children must never point back upward.
 *
 * Binary tree assumptions must hold.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Levels become merged.
 *
 * Check
 * -----
 * queue.size() must be frozen before the inner loop.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * NullPointerException.
 *
 * Check
 * -----
 * Empty tree.
 *
 * Also verify child null checks before enqueueing.
 */

/*
 * ============================================================
 * 🧠 RE-DERIVATION UNDER PRESSURE
 * ============================================================
 *
 * Suppose the exact code is forgotten.
 *
 * Reconstruct only from invariants.
 *
 * Step 1
 * ------
 * Need one answer per depth.
 *
 * Therefore:
 * BFS.
 *
 * ------------------------------------------------------------
 *
 * Step 2
 * ------
 * BFS naturally separates depths.
 *
 * Freeze queue size.
 *
 * ------------------------------------------------------------
 *
 * Step 3
 * ------
 * Need rightmost node.
 *
 * Left child enters queue before right child.
 *
 * Therefore,
 * last removed node is rightmost.
 *
 * ------------------------------------------------------------
 *
 * Step 4
 * ------
 * Record only when
 *
 * i == levelSize - 1.
 *
 * ------------------------------------------------------------
 *
 * Step 5
 * ------
 * Continue until queue is empty.
 *
 * Entire implementation follows directly.
 */

/*
 * ============================================================
 * ⚖️ COMPLEXITY ANALYSIS
 * ============================================================
 *
 * Brute Force
 * -----------
 *
 * Time
 * ----
 * O(N * H)
 *
 * Space
 * -----
 * O(H)
 *
 * ------------------------------------------------------------
 *
 * DFS
 * ---
 *
 * Time
 * ----
 * O(N)
 *
 * Space
 * -----
 * O(H)
 *
 * Worst Case
 * ----------
 * O(N)
 *
 * Balanced Tree
 * -------------
 * O(log N)
 *
 * ------------------------------------------------------------
 *
 * BFS
 * ---
 *
 * Time
 * ----
 * O(N)
 *
 * Space
 * -----
 * O(W)
 *
 * W
 * =
 * maximum width of the tree.
 *
 * Balanced Tree
 * -------------
 * Approximately N / 2 nodes may appear in one level.
 *
 * Skewed Tree
 * -----------
 * Queue size never exceeds one.
 */

/*
 * ============================================================
 * 📌 EDGE CASE CATALOG
 * ============================================================
 *
 * Case 1
 * ------
 * Empty tree.
 *
 * Result
 * []
 *
 * ------------------------------------------------------------
 *
 * Case 2
 * ------
 * Single node.
 *
 * Result
 * [root]
 *
 * ------------------------------------------------------------
 *
 * Case 3
 * ------
 * Completely left-skewed.
 *
 * Every node is visible.
 *
 * ------------------------------------------------------------
 *
 * Case 4
 * ------
 * Completely right-skewed.
 *
 * Every node is visible.
 *
 * ------------------------------------------------------------
 *
 * Case 5
 * ------
 * Complete binary tree.
 *
 * Last node of every level is visible.
 *
 * ------------------------------------------------------------
 *
 * Case 6
 * ------
 * Missing interior children.
 *
 * Visibility depends only on level order,
 * not tree symmetry.
 */

/*
 * ============================================================
 * 📖 INTERVIEW FOLLOW-UP QUESTIONS
 * ============================================================
 *
 * Q.
 * Can DFS replace BFS?
 *
 * A.
 * Yes.
 *
 * Traverse:
 *
 * right
 * before
 * left.
 *
 * First node encountered at each depth is visible.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Can we avoid auxiliary memory?
 *
 * A.
 * No.
 *
 * We must remember either:
 *
 * current recursion path
 *
 * or
 *
 * current frontier.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Why not simply follow right pointers?
 *
 * A.
 * A visible node may exist inside a left subtree when the
 * corresponding right subtree is absent.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Does enqueue order matter?
 *
 * A.
 * Absolutely.
 *
 * It determines whether the first or last processed node
 * represents the visible node.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Which approach is easier to debug?
 *
 * A.
 * BFS.
 *
 * Level boundaries are explicit.
 */

/*
 * ============================================================
 * 📌 IMPLEMENTATION MEMORY CARD
 * ============================================================
 *
 * Remember only these five statements:
 *
 * 1.
 * Empty tree -> return [].
 *
 * 2.
 * Queue starts with root.
 *
 * 3.
 * Freeze queue.size().
 *
 * 4.
 * Record last node in every level.
 *
 * 5.
 * Enqueue left then right.
 *
 * Everything else follows naturally.
 */

/*
 * ============================================================
 * 📚 RELATED LEETCODE PROBLEMS
 * ============================================================
 *
 * Easy
 * ----
 * Binary Tree Level Order Traversal
 *
 * Binary Tree Level Order Traversal II
 *
 * Average of Levels in Binary Tree
 *
 * ------------------------------------------------------------
 *
 * Medium
 * ------
 * Binary Tree Zigzag Level Order Traversal
 *
 * Find Largest Value in Each Tree Row
 *
 * Maximum Level Sum of a Binary Tree
 *
 * ------------------------------------------------------------
 *
 * Hard
 * ----
 * Vertical Order Traversal of a Binary Tree
 *
 * Serialize and Deserialize Binary Tree
 *
 * Width of Binary Tree
 */

/*
 * ============================================================
 * 📝 FINAL RECALL
 * ============================================================
 *
 * Pattern
 * -------
 * BFS by levels.
 *
 * Invariant
 * ---------
 * Queue stores exactly one level.
 *
 * State
 * -----
 * Current frontier.
 *
 * Transition
 * ----------
 * Poll node.
 * Push children.
 *
 * Search Target
 * -------------
 * Last processed node.
 *
 * Discard Rule
 * ------------
 * Entire level discarded after processing.
 *
 * Correctness
 * -----------
 * One visible node collected from every depth.
 *
 * Termination
 * -----------
 * Queue empty.
 */

    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        /*
         * ------------------------------------------------------------
         * Happy Path
         * ------------------------------------------------------------
         *
         *          1
         *        /   \
         *       2     3
         *        \     \
         *         5     4
         *
         * Visible:
         * 1,3,4
         */
        TreeNode example =
                new TreeNode(
                        1,
                        new TreeNode(
                                2,
                                null,
                                new TreeNode(5)
                        ),
                        new TreeNode(
                                3,
                                null,
                                new TreeNode(4)
                        )
                );

        assert solver.rightSideView(example)
                .equals(List.of(1, 3, 4));

        /*
         * ------------------------------------------------------------
         * Edge Case
         * Empty tree.
         * ------------------------------------------------------------
         */
        assert solver.rightSideView(null)
                .equals(List.of());

        /*
         * ------------------------------------------------------------
         * Boundary
         * Single node.
         * ------------------------------------------------------------
         */
        TreeNode single = new TreeNode(42);

        assert solver.rightSideView(single)
                .equals(List.of(42));

        /*
         * ------------------------------------------------------------
         * Left skew.
         *
         * Every node remains visible because there is no competing
         * node on the same level.
         * ------------------------------------------------------------
         */
        TreeNode leftSkew =
                new TreeNode(
                        1,
                        new TreeNode(
                                2,
                                new TreeNode(
                                        3,
                                        new TreeNode(4),
                                        null
                                ),
                                null
                        ),
                        null
                );

        assert solver.rightSideView(leftSkew)
                .equals(List.of(1, 2, 3, 4));

        /*
         * ------------------------------------------------------------
         * Right skew.
         * ------------------------------------------------------------
         */
        TreeNode rightSkew =
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

        assert solver.rightSideView(rightSkew)
                .equals(List.of(1, 2, 3, 4));

        /*
         * ------------------------------------------------------------
         * Interview Trap
         *
         * Visible node comes from the left subtree because the right
         * subtree does not continue.
         *
         *          1
         *         / \
         *        2   3
         *         \
         *          5
         *
         * Right-side view:
         * [1,3,5]
         * ------------------------------------------------------------
         */
        TreeNode trap =
                new TreeNode(
                        1,
                        new TreeNode(
                                2,
                                null,
                                new TreeNode(5)
                        ),
                        new TreeNode(3)
                );

        assert solver.rightSideView(trap)
                .equals(List.of(1, 3, 5));

        /*
         * ------------------------------------------------------------
         * Complete tree.
         *
         *             1
         *         /       \
         *        2         3
         *      /   \     /   \
         *     4     5   6     7
         *
         * Visible:
         * [1,3,7]
         * ------------------------------------------------------------
         */
        TreeNode complete =
                new TreeNode(
                        1,
                        new TreeNode(
                                2,
                                new TreeNode(4),
                                new TreeNode(5)
                        ),
                        new TreeNode(
                                3,
                                new TreeNode(6),
                                new TreeNode(7)
                        )
                );

        assert solver.rightSideView(complete)
                .equals(List.of(1, 3, 7));

        /*
         * ------------------------------------------------------------
         * Sparse tree.
         *
         *              10
         *            /    \
         *           5      20
         *            \     /
         *             8   15
         *                  \
         *                  18
         *
         * Visible:
         * [10,20,15,18]
         * ------------------------------------------------------------
         */
        TreeNode sparse =
                new TreeNode(
                        10,
                        new TreeNode(
                                5,
                                null,
                                new TreeNode(8)
                        ),
                        new TreeNode(
                                20,
                                new TreeNode(
                                        15,
                                        null,
                                        new TreeNode(18)
                                ),
                                null
                        )
                );

        assert solver.rightSideView(sparse)
                .equals(List.of(10, 20, 15, 18));

        /*
         * ------------------------------------------------------------
         * Verify BFS and DFS implementations agree.
         * ------------------------------------------------------------
         */
        Improved dfs = new Improved();

        assert dfs.rightSideView(example)
                .equals(solver.rightSideView(example));

        assert dfs.rightSideView(complete)
                .equals(solver.rightSideView(complete));

        assert dfs.rightSideView(leftSkew)
                .equals(solver.rightSideView(leftSkew));

        /*
         * ------------------------------------------------------------
         * Verify brute force agrees with optimal.
         * ------------------------------------------------------------
         */
        BruteForce brute = new BruteForce();

        assert brute.rightSideView(example)
                .equals(solver.rightSideView(example));

        assert brute.rightSideView(single)
                .equals(solver.rightSideView(single));

        assert brute.rightSideView(trap)
                .equals(solver.rightSideView(trap));

        System.out.println("All BinaryTreeRightSideView tests passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/