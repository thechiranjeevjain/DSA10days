package org.chijai.day6.session1;

import java.util.*;

/**
 * BinaryTreeLevelOrderTraversal
 *
 * ============================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Title:
 * Binary Tree Level Order Traversal
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Tree
 * Binary Tree
 * Breadth First Search (BFS)
 * Queue
 *
 * LeetCode:
 * https://leetcode.com/problems/binary-tree-level-order-traversal/
 *
 * ------------------------------------------------------------
 * Problem
 * ------------------------------------------------------------
 *
 * Given the root of a binary tree,
 * return the level order traversal of its nodes' values.
 *
 * Traverse level by level from top to bottom.
 * Inside each level, visit nodes from left to right.
 *
 * Return:
 *
 * [
 *   level0,
 *   level1,
 *   level2,
 *   ...
 * ]
 *
 * ------------------------------------------------------------
 * Constraints
 * ------------------------------------------------------------
 *
 * Number of nodes:
 *      [0, 2000]
 *
 * Node value:
 *      [-1000,1000]
 *
 * ------------------------------------------------------------
 * Example 1
 * ------------------------------------------------------------
 *
 *          3
 *        /   \
 *       9    20
 *           /  \
 *          15   7
 *
 * Output
 *
 * [
 *   [3],
 *   [9,20],
 *   [15,7]
 * ]
 *
 * ------------------------------------------------------------
 * Example 2
 * ------------------------------------------------------------
 *
 * root = [1]
 *
 * Output
 *
 * [[1]]
 *
 * ------------------------------------------------------------
 * Example 3
 * ------------------------------------------------------------
 *
 * root = []
 *
 * Output
 *
 * []
 *
 * ============================================================
 * Related Problems Covered In This Chapter
 * ============================================================
 *
 * 1. Binary Tree Level Order Traversal
 *
 * 2. Binary Tree Level Order Traversal II
 *
 *      Same traversal.
 *      Reverse the final answer.
 *
 * 3. Vertical Order Traversal
 *
 *      BFS +
 *      Column indexing +
 *      Ordering rules.
 *
 * ============================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern
 * -------
 *
 * Breadth First Search (Level-wise Expansion)
 *
 * Archetype
 * ---------
 *
 * Frontier Expansion
 *
 * We always expand one frontier completely before touching
 * the next frontier.
 *
 * Core Invariant
 * --------------
 *
 * At the beginning of every outer loop,
 * the queue contains exactly one tree level.
 *
 * Therefore
 *
 * queue.size()
 *
 * equals
 *
 * number of nodes belonging to the current level.
 *
 * This invariant is the entire algorithm.
 *
 * Why It Works
 * ------------
 *
 * Every node inserts only its children.
 *
 * Children always belong to the next level.
 *
 * Since all nodes of the current level are processed before
 * processing newly inserted children,
 * level boundaries remain perfectly preserved.
 *
 * Recognition Signals
 * -------------------
 *
 * Look for phrases like
 *
 * "level by level"
 *
 * "minimum depth"
 *
 * "shortest number of edges"
 *
 * "nearest"
 *
 * "wave expansion"
 *
 * "process current layer"
 *
 * "distance from root"
 *
 * "distance from source"
 *
 * These almost always indicate BFS.
 *
 * When To Use
 * -----------
 *
 * ✔ Level traversal
 *
 * ✔ Distance computation
 *
 * ✔ Multi-source expansion
 *
 * ✔ Shortest path in unweighted graph
 *
 * ✔ Tree width
 *
 * ✔ Zigzag traversal
 *
 * ✔ Right side view
 *
 * ✔ Average of levels
 *
 * ✔ Vertical traversal preprocessing
 *
 * When NOT To Use
 * ---------------
 *
 * DFS is better when
 *
 * • recursion naturally fits
 *
 * • only subtree information matters
 *
 * • backtracking is required
 *
 * • memory is critical on very wide trees
 *
 * Comparison
 * ----------
 *
 * DFS
 *
 * explores depth first.
 *
 * BFS
 *
 * explores breadth first.
 *
 * DFS stack invariant:
 *
 * current root-to-node path.
 *
 * BFS queue invariant:
 *
 * current frontier.
 *
 * ============================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine water filling a tree.
 *
 * Water reaches every node on depth 0.
 *
 * Then every node on depth 1.
 *
 * Then every node on depth 2.
 *
 * Every wave is one iteration of the outer loop.
 *
 * The queue stores the current wave.
 *
 * ------------------------------------------------------------
 * Invariant 1
 * ------------------------------------------------------------
 *
 * Before entering the inner loop
 *
 * queue contains only nodes of one level.
 *
 * Never mixed levels.
 *
 * ------------------------------------------------------------
 * Invariant 2
 * ------------------------------------------------------------
 *
 * len = queue.size()
 *
 * freezes the current level size.
 *
 * Newly inserted children must never be processed in the
 * current iteration.
 *
 * This single line separates one level from another.
 *
 * ------------------------------------------------------------
 * Invariant 3
 * ------------------------------------------------------------
 *
 * Every node is dequeued exactly once.
 *
 * Every child is enqueued exactly once.
 *
 * Therefore
 *
 * Total Operations = O(N)
 *
 * ------------------------------------------------------------
 * Invariant 4
 * ------------------------------------------------------------
 *
 * Every edge is crossed once.
 *
 * Parent
 *     ->
 * Child
 *
 * No revisiting.
 *
 * ------------------------------------------------------------
 * Variable Meaning
 * ------------------------------------------------------------
 *
 * queue
 *
 * Current frontier.
 *
 * len
 *
 * Number of nodes belonging to current level.
 *
 * level
 *
 * Answer for one depth.
 *
 * curr
 *
 * Node currently expanding frontier.
 *
 * result
 *
 * Levels already completed.
 *
 * ------------------------------------------------------------
 * Allowed Moves
 * ------------------------------------------------------------
 *
 * Poll current node.
 *
 * Record value.
 *
 * Push left child.
 *
 * Push right child.
 *
 * ------------------------------------------------------------
 * Forbidden Moves
 * ------------------------------------------------------------
 *
 * Never iterate while
 *
 * i < queue.size()
 *
 * because queue.size() changes after child insertion.
 *
 * This destroys the level invariant.
 *
 * Always freeze size first.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Queue becomes empty.
 *
 * No frontier remains.
 *
 * Entire tree has been explored.
 *
 * ------------------------------------------------------------
 * Why Naive Solutions Fail
 * ------------------------------------------------------------
 *
 * Pure DFS naturally groups nodes by subtree,
 * not by level.
 *
 * Additional bookkeeping becomes necessary.
 *
 * BFS gives levels for free because of its frontier
 * invariant.
 *
 * ============================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================
 *
 * Mistake 1
 * ---------
 *
 * for (int i = 0; i < queue.size(); i++)
 *
 * Looks reasonable.
 *
 * Wrong because queue.size() changes while children are added.
 *
 * Violated Invariant
 * ------------------
 *
 * Queue no longer represents exactly one level.
 *
 * Counterexample
 *
 *      1
 *     /
 *    2
 *
 * After processing 1,
 * node 2 enters queue,
 * queue.size() changes,
 * node 2 incorrectly joins same level.
 *
 * ------------------------------------------------------------
 * Mistake 2
 * ------------------------------------------------------------
 *
 * Using one shared list for every level.
 *
 * All rows point to same object.
 *
 * ------------------------------------------------------------
 * Mistake 3
 * ------------------------------------------------------------
 *
 * Forgetting null root.
 *
 * Queue receives null.
 *
 * NullPointerException follows.
 *
 * ------------------------------------------------------------
 * Mistake 4
 * ------------------------------------------------------------
 *
 * Adding children before recording node value is fine.
 *
 * Processing children immediately is not.
 *
 * Again,
 * level invariant breaks.
 *
 * ------------------------------------------------------------
 * Interview Trap
 * ------------------------------------------------------------
 *
 * Interviewer asks:
 *
 * "Why store queue size before loop?"
 *
 * Correct answer:
 *
 * Because queue size defines one frontier.
 * Children belong to the next frontier and must not affect
 * current iteration.
 *
 * ============================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================
 *
 * Typing Order
 * ------------
 *
 * 1.
 *
 * if root == null
 *      return empty answer
 *
 * 2.
 *
 * create answer
 *
 * 3.
 *
 * create queue
 *
 * 4.
 *
 * enqueue root
 *
 * 5.
 *
 * while queue not empty
 *
 * 6.
 *
 * freeze size
 *
 * 7.
 *
 * create level list
 *
 * 8.
 *
 * repeat size times
 *
 *      poll node
 *      save value
 *      enqueue left
 *      enqueue right
 *
 * 9.
 *
 * append level
 *
 * 10.
 *
 * return answer
 *
 * ============================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================
 *
 * queue ← root
 *
 * while queue not empty
 *
 *      size ← queue size
 *
 *      repeat size
 *
 *          pop
 *
 *          record
 *
 *          push children
 *
 *      save level
 *
 * return answer
 *
 * ============================================================
 * 6. SOLUTION CLASSES
 * ============================================================
 */
public class BinaryTreeTraversal {

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
     *
     * Compute height first.
     *
     * For every level,
     * perform DFS collecting nodes only at that depth.
     *
     * Invariant
     * ---------
     *
     * DFS only records nodes whose remaining depth is zero.
     *
     * Limitation
     * ----------
     *
     * Entire tree is revisited for every level.
     *
     * Complexity
     * ----------
     *
     * Time:
     * O(N * H)
     *
     * Worst case:
     * O(N²)
     *
     * Space:
     * O(H)
     *
     * Interview Usefulness
     * --------------------
     *
     * Good only for discussion.
     */

    static class BruteForce {

        public List<List<Integer>> levelOrder(TreeNode root) {

            List<List<Integer>> answer = new ArrayList<>();

            int height = height(root);

            for (int level = 1; level <= height; level++) {

                List<Integer> current = new ArrayList<>();

                collect(root, level, current);

                answer.add(current);
            }

            return answer;
        }

        private int height(TreeNode node) {

            if (node == null) {
                return 0;
            }

            return 1 + Math.max(height(node.left), height(node.right));
        }

        private void collect(TreeNode node,
                             int level,
                             List<Integer> list) {

            if (node == null) {
                return;
            }

            if (level == 1) {
                list.add(node.val);
                return;
            }

            collect(node.left, level - 1, list);
            collect(node.right, level - 1, list);
        }
    }

/**
 * =========================================================
 * Improved
 * =========================================================
 *
 * Idea
 * ----
 *
 * DFS while tracking depth.
 *
 * First visit to a depth creates its list.
 *
 * Invariant
 * ---------
 *
 * answer.get(depth)
 * always represents one complete level.
 *
 * Improvement
 * -----------
 *
 * Every node visited once.
 *
 * Complexity
 * ----------
 *
 * Time:
 * O(N)
 *
 * Space:
 * O(H)
 *
 * Interview Usefulness
 * --------------------
 *
 * Useful comparison against BFS.
 */

static class Improved {

    public List<List<Integer>> levelOrder(TreeNode root) {

        List<List<Integer>> answer = new ArrayList<>();

        dfs(root, 0, answer);

        return answer;
    }

    private void dfs(TreeNode node,
                     int depth,
                     List<List<Integer>> answer) {

        if (node == null) {
            return;
        }

        if (depth == answer.size()) {
            answer.add(new ArrayList<>());
        }

        answer.get(depth).add(node.val);

        dfs(node.left, depth + 1, answer);
        dfs(node.right, depth + 1, answer);
    }
}

    /**
     * =========================================================
     * Optimal (Interview Preferred)
     * =========================================================
     *
     * Idea
     * ----
     *
     * Breadth First Search using one queue.
     *
     * The queue always stores the current frontier.
     *
     * Invariant
     * ---------
     *
     * Before every outer iteration:
     *
     * queue contains exactly one tree level.
     *
     * Correctness
     * -----------
     *
     * The current queue size is frozen before children are
     * inserted.
     *
     * Therefore children are postponed to the next iteration,
     * preserving perfect level boundaries.
     *
     * Complexity
     * ----------
     *
     * Time:
     * O(N)
     *
     * Space:
     * O(W)
     *
     * W = maximum width of tree.
     *
     * Interview Usefulness
     * --------------------
     *
     * This is the canonical solution expected in interviews.
     */

    static class Optimal {

        public List<List<Integer>> levelOrder(TreeNode root) {

            List<List<Integer>> answer = new ArrayList<>();

            // Invariant: empty tree has no frontier.
            if (root == null) {
                return answer;
            }

            Queue<TreeNode> queue = new ArrayDeque<>();

            // Initial frontier contains only the root.
            queue.offer(root);

            while (!queue.isEmpty()) {

                // Invariant: queue currently stores exactly one level.
                int levelSize = queue.size();

                List<Integer> currentLevel = new ArrayList<>(levelSize);

                for (int i = 0; i < levelSize; i++) {

                    // Remove one node from current frontier.
                    TreeNode current = queue.poll();

                    currentLevel.add(current.val);

                    // Children belong to next frontier.
                    if (current.left != null) {
                        queue.offer(current.left);
                    }

                    // Preserve left-to-right ordering.
                    if (current.right != null) {
                        queue.offer(current.right);
                    }
                }

                // Entire level completed.
                answer.add(currentLevel);
            }

            return answer;
        }
    }

    /**
     * =========================================================
     * Binary Tree Level Order Traversal II
     * =========================================================
     *
     * Pattern
     * -------
     *
     * Identical BFS.
     *
     * Only output order changes.
     *
     * Core Invariant
     * --------------
     *
     * Level discovery remains top-to-bottom.
     *
     * Final answer alone is reversed.
     *
     * Why Reverse At End?
     * -------------------
     *
     * BFS naturally discovers shallower levels first.
     *
     * Trying to build the answer backwards during traversal
     * complicates the implementation without reducing
     * asymptotic complexity.
     *
     * Complexity
     * ----------
     *
     * BFS:
     * O(N)
     *
     * Reverse:
     * O(L)
     *
     * L = number of levels.
     */

    static class LevelOrderBottom {

        public List<List<Integer>> levelOrderBottom(TreeNode root) {

            List<List<Integer>> answer =
                    new Optimal().levelOrder(root);

            Collections.reverse(answer);

            return answer;
        }
    }

    /**
     * =========================================================
     * Vertical Order Traversal
     * =========================================================
     *
     * Problem
     * -------
     *
     * Every node has coordinates.
     *
     * Root
     * (row = 0, col = 0)
     *
     * Left child
     * (row + 1, col - 1)
     *
     * Right child
     * (row + 1, col + 1)
     *
     * Return columns from leftmost to rightmost.
     *
     * Inside one column:
     *
     * 1. smaller row first
     * 2. if same row and same column,
     *    smaller value first
     *
     * Pattern
     * -------
     *
     * BFS +
     * Coordinate propagation +
     * Ordered grouping.
     *
     * Core Invariant
     * --------------
     *
     * Every queued node carries exactly one immutable column.
     *
     * Children derive their columns deterministically.
     *
     * Left:
     * column - 1
     *
     * Right:
     * column + 1
     *
     * Additional Ordering
     * -------------------
     *
     * We must remember
     *
     * row,
     * column,
     * value
     *
     * because ties require sorting.
     */

    static class VerticalTraversal {

        static class Entry {

            int row;
            int col;
            int value;

            Entry(int row,
                  int col,
                  int value) {

                this.row = row;
                this.col = col;
                this.value = value;
            }
        }

        static class State {

            TreeNode node;
            int row;
            int col;

            State(TreeNode node,
                  int row,
                  int col) {

                this.node = node;
                this.row = row;
                this.col = col;
            }
        }

        public List<List<Integer>> verticalTraversal(TreeNode root) {

            List<List<Integer>> answer = new ArrayList<>();

            if (root == null) {
                return answer;
            }

            List<Entry> entries = new ArrayList<>();

            Queue<State> queue = new ArrayDeque<>();

            // Root defines coordinate system.
            queue.offer(new State(root, 0, 0));

            while (!queue.isEmpty()) {

                State current = queue.poll();

                entries.add(
                        new Entry(
                                current.row,
                                current.col,
                                current.node.val));

                if (current.node.left != null) {

                    // Left child shifts one column left.
                    queue.offer(
                            new State(
                                    current.node.left,
                                    current.row + 1,
                                    current.col - 1));
                }

                if (current.node.right != null) {

                    // Right child shifts one column right.
                    queue.offer(
                            new State(
                                    current.node.right,
                                    current.row + 1,
                                    current.col + 1));
                }
            }

            // Required ordering:
            // column
            // row
            // value
            entries.sort((a, b) -> {

                if (a.col != b.col) {
                    return Integer.compare(a.col, b.col);
                }

                if (a.row != b.row) {
                    return Integer.compare(a.row, b.row);
                }

                return Integer.compare(a.value, b.value);
            });

            Integer previousColumn = null;
            List<Integer> currentColumn = null;

            for (Entry entry : entries) {

                if (previousColumn == null
                        || previousColumn != entry.col) {

                    currentColumn = new ArrayList<>();

                    answer.add(currentColumn);

                    previousColumn = entry.col;
                }

                currentColumn.add(entry.value);
            }

            return answer;
        }
    }

/**
 * =========================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================
 *
 * Explain The Invariant
 * ---------------------
 *
 * "The queue always represents the current frontier.
 * Before processing a level, I freeze its size.
 * That frozen size guarantees every node processed belongs
 * to the same depth."
 *
 * Explain The Discard Rule
 * ------------------------
 *
 * BFS does not explicitly discard search space like binary
 * search.
 *
 * Instead, once a node leaves the queue it is permanently
 * finished.
 *
 * Only its children remain candidates.
 *
 * Correctness
 * -----------
 *
 * Every node appears exactly once because every node has
 * exactly one parent (except the root).
 *
 * Every child enters exactly one future frontier.
 *
 * Therefore every level is complete before the next begins.

 *
 * Termination
 * -----------
 *
 * Each iteration permanently removes one node from the
 * frontier.
 *
 * Since the tree contains a finite number of nodes,
 * eventually the queue becomes empty.
 *
 * In-Place Feasibility
 * --------------------
 *
 * Not possible.
 *
 * BFS fundamentally requires remembering future work.
 *
 * At least one frontier must be stored.
 *
 * Streaming Feasibility
 * ---------------------
 *
 * Yes.
 *
 * One completed level can be emitted immediately without
 * waiting for the rest of the tree.
 *
 * This is useful for online processing.
 *
 * When NOT To Use
 * ---------------
 *
 * Prefer DFS when
 *
 * • subtree aggregation is needed
 *
 * • recursion naturally expresses the solution
 *
 * • path-specific state dominates
 *
 * • memory on extremely wide trees is a concern
 */

    /**
     * =========================================================
     * 🎯 INTERVIEW RECALL SHEET
     * =========================================================
     *
     * Trigger
     * -------
     *
     * "Level"
     *
     * "Breadth"
     *
     * "Distance"
     *
     * "Wave"
     *
     * "Nearest"
     *
     * Pattern
     * -------
     *
     * Breadth First Search
     *
     * Search Space
     * ------------
     *
     * Current frontier.
     *
     * Invariant
     * ---------
     *
     * Queue stores exactly one level.
     *
     * Search Target
     * -------------
     *
     * Expand every node in one level before touching the next.
     *
     * Discard Rule
     * ------------
     *
     * Once dequeued,
     * a node is permanently finished.
     *
     * Common Trap
     * -----------
     *
     * Iterating using queue.size() dynamically.
     *
     * Freeze it first.
     *
     * Edge Cases
     * ----------
     *
     * • null root
     *
     * • single node
     *
     * • left chain
     *
     * • right chain
     *
     * • complete tree
     *
     * • duplicate values
     *
     * One-Liner
     * ---------
     *
     * Queue equals one frontier.
     * Frozen queue size equals one level.
     *
     * Re-Derivation Cue
     * -----------------
     *
     * Ask:
     *
     * "How do I stop newly discovered children from joining the
     * current level?"
     *
     * Answer:
     *
     * Freeze queue size before expansion.
     */

    /**
     * =========================================================
     * 🔄 VARIATIONS & TWEAKS
     * =========================================================
     *
     * ---------------------------------------------------------
     * Zigzag Level Order
     * ---------------------------------------------------------
     *
     * Pattern
     * -------
     *
     * Same BFS.
     *
     * Alternate insertion direction.
     *
     * Invariant Preserved
     * -------------------
     *
     * Queue still represents one level.
     *
     * Only output ordering changes.
     *
     * ---------------------------------------------------------
     * Right Side View
     * ---------------------------------------------------------
     *
     * Pattern
     * -------
     *
     * Same BFS.
     *
     * Save last node of every frozen level.
     *
     * ---------------------------------------------------------
     * Average Of Levels
     * ---------------------------------------------------------
     *
     * Pattern
     * -------
     *
     * Same BFS.
     *
     * Replace level list with running sum.
     *
     * ---------------------------------------------------------
     * Maximum Width
     * ---------------------------------------------------------
     *
     * Pattern
     * -------
     *
     * Same BFS.
     *
     * Store positional indices.
     *
     * Queue invariant remains unchanged.
     *
     * ---------------------------------------------------------
     * Binary Tree Level Order II
     * ---------------------------------------------------------
     *
     * Pattern
     * -------
     *
     * Same BFS.
     *
     * Reverse final answer.
     *
     * ---------------------------------------------------------
     * Vertical Traversal
     * ---------------------------------------------------------
     *
     * Pattern
     * -------
     *
     * Same frontier expansion.
     *
     * Extra state:
     *
     * row
     *
     * column
     *
     * value
     *
     * Additional ordering performed afterwards.
     *
     * ---------------------------------------------------------
     * N-ary Tree Level Order
     * ---------------------------------------------------------
     *
     * Pattern
     * -------
     *
     * Identical.
     *
     * Iterate over every child instead of two children.
     *
     * Queue invariant is unchanged.
     *
     * ---------------------------------------------------------
     * Graph BFS
     * ---------------------------------------------------------
     *
     * Pattern
     * -------
     *
     * Same frontier expansion.
     *
     * Additional invariant:
     *
     * Every vertex must be marked visited before enqueueing.
     *
     * Otherwise duplicates appear in the frontier.
     *
     * ---------------------------------------------------------
     * Pattern Boundary
     * ---------------------------------------------------------
     *
     * If future states depend on multiple previous paths,
     * BFS alone may not be sufficient.
     *
     * Dynamic Programming,
     * Dijkstra,
     * or Topological Processing
     * may become necessary.
     */

    /**
     * =========================================================
     * 🧠 MASTERY CHECKLIST
     * =========================================================
     *
     * Can you state the invariant?
     *
     * ✔ Queue stores exactly one frontier.
     *
     * Can you identify the search space?
     *
     * ✔ Nodes currently waiting in queue.
     *
     * Can you explain the discard rule?
     *
     * ✔ Dequeued nodes are permanently complete.
     *
     * Can you explain termination?
     *
     * ✔ Queue eventually becomes empty.
     *
     * Can you explain naive failure?
     *
     * ✔ Dynamic queue size mixes levels.
     *
     * Can you identify edge cases?
     *
     * ✔ Empty tree.
     *
     * ✔ Single node.
     *
     * ✔ Degenerate tree.
     *
     * ✔ Perfect tree.
     *
     * ✔ Duplicate values.
     *
     * Can you debug confidently?
     *
     * ✔ Verify frozen level size.
     *
     * ✔ Verify enqueue order.
     *
     * ✔ Verify exactly one dequeue per node.
     *
     * Can you derive variants?
     *
     * ✔ Reverse answer.
     *
     * ✔ Alternate direction.
     *
     * ✔ Store column.
     *
     * ✔ Store index.
     *
     * ✔ Store running sum.
     *
     * Can you identify the boundary?
     *
     * ✔ Queue invariant applies only to breadth expansion.
     */

    // =========================================================
    // Helper Methods For Tests
    // =========================================================

    private static TreeNode sampleTree() {

        TreeNode fifteen = new TreeNode(15);
        TreeNode seven = new TreeNode(7);

        TreeNode twenty =
                new TreeNode(20, fifteen, seven);

        TreeNode nine = new TreeNode(9);

        return new TreeNode(3, nine, twenty);
    }

    private static TreeNode leftChain() {

        return new TreeNode(
                1,
                new TreeNode(
                        2,
                        new TreeNode(
                                3,
                                new TreeNode(4),
                                null),
                        null),
                null);
    }

    private static TreeNode singleNode() {

        return new TreeNode(42);
    }

    private static TreeNode emptyTree() {

        return null;
    }

    private static TreeNode duplicateTree() {

        return new TreeNode(
                5,
                new TreeNode(
                        5,
                        new TreeNode(5),
                        new TreeNode(5)),
                new TreeNode(5));
    }

    // =========================================================
    // 🧪 MAIN + SELF-VERIFYING TESTS
    // =========================================================

    public static void main(String[] args) {

        Optimal optimal = new Optimal();

        // -----------------------------------------------------
        // Happy Path
        // -----------------------------------------------------
        // Canonical example from the problem statement.
        List<List<Integer>> expected =
                Arrays.asList(
                        Arrays.asList(3),
                        Arrays.asList(9, 20),
                        Arrays.asList(15, 7));

        assert optimal.levelOrder(sampleTree()).equals(expected)
                : "Representative example failed.";

        // -----------------------------------------------------
        // Empty Tree
        // -----------------------------------------------------
        // No frontier should produce an empty answer.
        assert optimal.levelOrder(emptyTree()).isEmpty()
                : "Empty tree should return empty list.";

        // -----------------------------------------------------
        // Single Node
        // -----------------------------------------------------
        // Smallest non-empty tree.
        assert optimal.levelOrder(singleNode()).equals(
                Collections.singletonList(
                        Collections.singletonList(42)))
                : "Single node traversal failed.";

        // -----------------------------------------------------
        // Degenerate Left Chain
        // -----------------------------------------------------
        // Every level contains exactly one node.
        List<List<Integer>> leftExpected =
                Arrays.asList(
                        Collections.singletonList(1),
                        Collections.singletonList(2),
                        Collections.singletonList(3),
                        Collections.singletonList(4));

        assert optimal.levelOrder(leftChain()).equals(leftExpected)
                : "Left chain traversal failed.";

        // -----------------------------------------------------
        // Duplicate Values
        // -----------------------------------------------------
        // Traversal must preserve structure, not unique values.
        List<List<Integer>> duplicateExpected =
                Arrays.asList(
                        Collections.singletonList(5),
                        Arrays.asList(5, 5),
                        Arrays.asList(5, 5));

        assert optimal.levelOrder(duplicateTree()).equals(duplicateExpected)
                : "Duplicate values should not affect traversal.";

        // -----------------------------------------------------
        // Brute Force Cross Verification
        // -----------------------------------------------------
        BruteForce brute = new BruteForce();

        assert brute.levelOrder(sampleTree()).equals(expected)
                : "Brute force verification failed.";

        // -----------------------------------------------------
        // DFS Improvement Cross Verification
        // -----------------------------------------------------
        Improved improved = new Improved();

        assert improved.levelOrder(sampleTree()).equals(expected)
                : "DFS depth-tracking verification failed.";

        // -----------------------------------------------------
        // Bottom-Up Traversal
        // -----------------------------------------------------
        LevelOrderBottom bottom = new LevelOrderBottom();

        List<List<Integer>> bottomExpected =
                Arrays.asList(
                        Arrays.asList(15, 7),
                        Arrays.asList(9, 20),
                        Collections.singletonList(3));

        assert bottom.levelOrderBottom(sampleTree()).equals(bottomExpected)
                : "Bottom-up traversal failed.";

        // -----------------------------------------------------
        // Vertical Traversal
        // -----------------------------------------------------
        VerticalTraversal vertical = new VerticalTraversal();

        List<List<Integer>> verticalExpected =
                Arrays.asList(
                        Collections.singletonList(9),
                        Arrays.asList(3, 15),
                        Collections.singletonList(20),
                        Collections.singletonList(7));

        assert vertical.verticalTraversal(sampleTree()).equals(verticalExpected)
                : "Vertical traversal failed.";

        // -----------------------------------------------------
        // Boundary Verification
        // -----------------------------------------------------
        // Running twice must produce identical results.
        assert optimal.levelOrder(sampleTree()).equals(expected)
                : "Algorithm should be deterministic.";

        // -----------------------------------------------------
        // Queue Invariant Stress
        // -----------------------------------------------------
        // Wide second level verifies frozen queue size.
        TreeNode wide =
                new TreeNode(
                        1,
                        new TreeNode(
                                2,
                                new TreeNode(4),
                                new TreeNode(5)),
                        new TreeNode(
                                3,
                                new TreeNode(6),
                                new TreeNode(7)));

        List<List<Integer>> wideExpected =
                Arrays.asList(
                        Collections.singletonList(1),
                        Arrays.asList(2, 3),
                        Arrays.asList(4, 5, 6, 7));

        assert optimal.levelOrder(wide).equals(wideExpected)
                : "Frozen frontier invariant violated.";

        System.out.println("All assertions passed.");
    }

    /*
     * =========================================================
     * I understand the invariant.
     *
     * I can re-derive the solution.
     *
     * I can physically reconstruct the implementation under pressure.
     *
     * This chapter is complete.
     * =========================================================
     */
}