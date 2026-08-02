package org.chijai.day6.session2;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class SerializeAndDeserializeBinaryTree {

/*
 * =============================================================================
 * 2. 📘 PRIMARY PROBLEM
 * =============================================================================
 *
 * Title:
 * Serialize and Deserialize Binary Tree
 *
 * Difficulty:
 * Hard
 *
 * Tags:
 * Binary Tree
 * DFS
 * BFS
 * Design
 * String
 * Queue
 * Recursion
 *
 * LeetCode:
 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/
 *
 * -----------------------------------------------------------------------------
 * Problem
 * -----------------------------------------------------------------------------
 *
 * Design an algorithm to serialize and deserialize a binary tree.
 *
 * Serialization converts an in-memory binary tree into a string so that it can
 * later be reconstructed.
 *
 * Deserialization performs the inverse operation.
 *
 * After
 *
 *      deserialize(serialize(root))
 *
 * the reconstructed tree must be structurally identical to the original tree,
 * with exactly the same node values.
 *
 * The format is not prescribed.
 *
 * Any deterministic encoding is acceptable provided that serialization and
 * deserialization are perfect inverses.
 *
 * -----------------------------------------------------------------------------
 * Constraints
 * -----------------------------------------------------------------------------
 *
 * Number of nodes:
 *      0 ... 10^4
 *
 * Node value:
 *      -1000 ... 1000
 *
 * Tree may be:
 *      completely empty
 *      balanced
 *      skewed
 *      sparse
 *      contain duplicate values
 *
 * -----------------------------------------------------------------------------
 * Representative Example 1
 * -----------------------------------------------------------------------------
 *
 * Input
 *
 *            1
 *          /   \
 *         2     3
 *              / \
 *             4   5
 *
 * Serialized (one possible encoding)
 *
 * 1 2 n n 3 4 n n 5 n n
 *
 * Deserialized tree
 *
 *            1
 *          /   \
 *         2     3
 *              / \
 *             4   5
 *
 * -----------------------------------------------------------------------------
 * Representative Example 2
 * -----------------------------------------------------------------------------
 *
 * Input
 *
 * null
 *
 * Serialized
 *
 * n
 *
 * Deserialized
 *
 * null
 *
 * -----------------------------------------------------------------------------
 * Representative Example 3
 * -----------------------------------------------------------------------------
 *
 * Input
 *
 *      7
 *
 * Serialized
 *
 * 7 n n
 *
 * -----------------------------------------------------------------------------
 * Goal
 * -----------------------------------------------------------------------------
 *
 * Produce a deterministic encoding that uniquely represents:
 *
 *      values
 *      shape
 *
 * because values alone are insufficient to reconstruct the tree.
 *
 * =============================================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * =============================================================================
 *
 * Primary Pattern
 *
 *      Recursive DFS with Explicit Null Markers
 *
 * Archetype
 *
 *      Tree -> Linear Stream
 *      Linear Stream -> Tree
 *
 * Core Invariant
 *
 * Every recursive call consumes or produces exactly one subtree.
 *
 * Every missing child is represented explicitly.
 *
 * Therefore the serialized stream contains enough information to recover both
 * topology and values.
 *
 * Why It Works
 *
 * A preorder traversal alone is insufficient.
 *
 * Example:
 *
 *      1          1
 *     /            \
 *    2              2
 *
 * Preorder:
 *
 *      1 2
 *
 * Identical.
 *
 * Once null markers are added:
 *
 * Left tree:
 *
 *      1 2 n n n
 *
 * Right tree:
 *
 *      1 n 2 n n
 *
 * They become distinguishable.
 *
 * Recognition Signals
 *
 * Use this pattern whenever:
 *
 * - tree must be persisted
 * - tree must cross network boundaries
 * - tree must be reconstructed later
 * - exact structure matters
 * - duplicate values exist
 *
 * When NOT To Use
 *
 * - only traversal order is required
 * - BST reconstruction from preorder is allowed
 * - structure is irrelevant
 *
 * Comparison
 *
 * -------------------------------------------------------------------------
 * Pattern                    Shape Preserved     Extra Markers
 * -------------------------------------------------------------------------
 * Preorder only                   No                No
 * Inorder only                    No                No
 * Level order only                No                Usually
 * DFS + null markers             Yes               Yes
 * BFS + null markers             Yes               Yes
 * -------------------------------------------------------------------------
 *
 * Why DFS Is Usually Preferred
 *
 * - naturally recursive
 * - implementation is shorter
 * - reconstruction mirrors serialization
 * - easier invariant reasoning
 * - no index arithmetic
 *
 * BFS is equally valid but requires maintaining frontier state explicitly.
 *
 * =============================================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * =============================================================================
 *
 * Mental Model
 *
 * Imagine recursively replacing every subtree by a sentence.
 *
 * A subtree sentence has exactly one of two forms.
 *
 * Non-null subtree
 *
 *      value
 *      left sentence
 *      right sentence
 *
 * Null subtree
 *
 *      n
 *
 * Since every subtree obeys the same grammar, reconstruction simply parses
 * the same grammar recursively.
 *
 * -------------------------------------------------------------------------
 * Primary Invariant
 * -------------------------------------------------------------------------
 *
 * One recursive call corresponds to exactly one subtree.
 *
 * During serialization:
 *
 *      subtree
 *          ↓
 *      contiguous token sequence
 *
 * During deserialization:
 *
 *      contiguous token sequence
 *          ↓
 *      subtree
 *
 * This one-to-one mapping never changes.
 *
 * -------------------------------------------------------------------------
 * State
 * -------------------------------------------------------------------------
 *
 * Serialization
 *
 * Current recursion frame owns exactly one subtree.
 *
 * Deserialization
 *
 * Queue head always points to the first token of the current subtree.
 *
 * -------------------------------------------------------------------------
 * Variable Meanings
 * -------------------------------------------------------------------------
 *
 * root
 *      current subtree root
 *
 * sb
 *      output token stream
 *
 * queue
 *      remaining preorder tokens
 *
 * token
 *      either:
 *          integer
 *          n
 *
 * -------------------------------------------------------------------------
 * Allowed State Transition
 * -------------------------------------------------------------------------
 *
 * Current node
 *
 *          ↓
 *
 * emit value
 *
 *          ↓
 *
 * serialize left subtree
 *
 *          ↓
 *
 * serialize right subtree
 *
 * Deserialization performs the exact inverse transitions.
 *
 * -------------------------------------------------------------------------
 * Forbidden Move
 * -------------------------------------------------------------------------
 *
 * Never omit a null child.
 *
 * Omitting even one null destroys uniqueness.
 *
 * Example
 *
 *          1
 *         /
 *        2
 *
 * versus
 *
 *          1
 *           \
 *            2
 *
 * Without null markers they serialize identically.
 *
 * -------------------------------------------------------------------------
 * Termination
 * -------------------------------------------------------------------------
 *
 * Recursion terminates immediately on null.
 *
 * Every recursive call strictly moves to a smaller subtree.
 *
 * Every token is consumed exactly once.
 *
 * Therefore recursion always finishes.
 *
 * -------------------------------------------------------------------------
 * Correctness Intuition
 * -------------------------------------------------------------------------
 *
 * Think of preorder with null markers as a complete grammar.
 *
 * Every subtree starts with exactly one token.
 *
 * If token == n
 *
 *      subtree is empty.
 *
 * Otherwise
 *
 *      build root,
 *      recursively build left,
 *      recursively build right.
 *
 * Because recursion consumes tokens in the exact order they were produced,
 * every subtree is reconstructed identically.
 *
 * -------------------------------------------------------------------------
 * Why Naive Solutions Fail
 * -------------------------------------------------------------------------
 *
 * Value sequence alone loses topology.
 *
 * Even preorder + inorder cannot be used because inorder is unavailable.
 *
 * Duplicate values further eliminate any possibility of reconstructing the
 * tree without structural markers.
 */

    /*
     * =============================================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * =============================================================================
     *
     * -------------------------------------------------------------------------
     * Mistake 1
     * -------------------------------------------------------------------------
     *
     * Serialize only node values.
     *
     * Example
     *
     *          1
     *         /
     *        2
     *
     * and
     *
     *          1
     *           \
     *            2
     *
     * Both become
     *
     *      1 2
     *
     * Violated Invariant
     *
     * Structure is no longer uniquely represented.
     *
     * -------------------------------------------------------------------------
     * Mistake 2
     * -------------------------------------------------------------------------
     *
     * Skip trailing nulls inconsistently.
     *
     * Why it seems correct
     *
     * Many online examples visually omit them.
     *
     * Why it fails
     *
     * Serialization and deserialization must agree on exactly the same grammar.
     *
     * Changing only one side immediately breaks inversion.
     *
     * -------------------------------------------------------------------------
     * Mistake 3
     * -------------------------------------------------------------------------
     *
     * Deserialize using preorder but consume tokens in the wrong order.
     *
     * Incorrect
     *
     * Root
     * Right
     * Left
     *
     * instead of
     *
     * Root
     * Left
     * Right
     *
     * Violated Invariant
     *
     * Token ownership.
     *
     * Every recursive frame must consume exactly the tokens belonging to its own
     * subtree.
     *
     * -------------------------------------------------------------------------
     * Mistake 4
     * -------------------------------------------------------------------------
     *
     * Forget to serialize null children.
     *
     * Counterexample
     *
     *          1
     *         /
     *        2
     *
     * versus
     *
     *          1
     *           \
     *            2
     *
     * Impossible to distinguish.
     *
     * -------------------------------------------------------------------------
     * Mistake 5
     * -------------------------------------------------------------------------
     *
     * Deserialize using a shared index that advances incorrectly.
     *
     * Typical bug
     *
     * Increment before reading.
     *
     * Result
     *
     * Entire remaining tree shifts by one token.
     *
     * -------------------------------------------------------------------------
     * Mistake 6
     * -------------------------------------------------------------------------
     *
     * Treat empty string and null marker as identical.
     *
     * Empty tree
     *
     *      n
     *
     * should represent
     *
     *      null
     *
     * not
     *
     *      ""
     *
     * unless serializer intentionally defines that contract.
     *
     * -------------------------------------------------------------------------
     * Interview Traps
     * -------------------------------------------------------------------------
     *
     * Trap:
     *
     * "Can preorder alone reconstruct a tree?"
     *
     * Answer:
     *
     * No.
     *
     * Only BSTs have additional ordering information.
     *
     * -------------------------------------------------------
     *
     * Trap:
     *
     * "Why are null markers necessary?"
     *
     * Answer:
     *
     * They encode missing children.
     *
     * Missing children define the tree topology.
     *
     * -------------------------------------------------------
     *
     * Trap:
     *
     * "Why does duplicate data not matter?"
     *
     * Answer:
     *
     * Reconstruction depends on traversal grammar, not uniqueness of values.
     *
     * -------------------------------------------------------
     *
     * Trap:
     *
     * "Can BFS serialize too?"
     *
     * Answer:
     *
     * Yes.
     *
     * Both BFS and DFS work as long as both directions use the identical format.
     *
     * =============================================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * =============================================================================
     *
     * Optimal DFS (Preorder)
     *
     * Typing Order
     *
     * Step 1
     *
     * Create
     *
     *      serialize(root)
     *
     * Step 2
     *
     * Create StringBuilder.
     *
     * Step 3
     *
     * Call preorder(root).
     *
     * Step 4
     *
     * Base case
     *
     * if node == null
     *
     *      append "n "
     *      return
     *
     * Step 5
     *
     * Append node value.
     *
     * Step 6
     *
     * Recurse left.
     *
     * Step 7
     *
     * Recurse right.
     *
     * -------------------------------------------------------
     *
     * Deserialization
     *
     * Step 1
     *
     * Split tokens.
     *
     * Step 2
     *
     * Store inside queue.
     *
     * Step 3
     *
     * Read queue head.
     *
     * Step 4
     *
     * If token == n
     *
     *      return null.
     *
     * Step 5
     *
     * Create node.
     *
     * Step 6
     *
     * Build left subtree.
     *
     * Step 7
     *
     * Build right subtree.
     *
     * Step 8
     *
     * Return node.
     *
     * Mechanical Reconstruction
     *
     * serialize
     *
     * builder
     * ↓
     * preorder
     * ↓
     * null?
     * ↓
     * value
     * ↓
     * left
     * ↓
     * right
     *
     * deserialize
     *
     * queue
     * ↓
     * poll
     * ↓
     * null?
     * ↓
     * create node
     * ↓
     * left
     * ↓
     * right
     *
     * =============================================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * =============================================================================
     *
     * Serialize
     *
     * recurse(node)
     *
     * if null
     *      emit n
     * else
     *      emit value
     *      recurse(left)
     *      recurse(right)
     *
     * Deserialize
     *
     * token = next
     *
     * if n
     *      return null
     *
     * node
     * node.left = recurse()
     * node.right = recurse()
     * return node
     *
     * =============================================================================
     * 6. SOLUTION CLASSES
     * =============================================================================
     */

    static final class TreeNode {

        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /*
     * ============================================================================
     * Solution 1
     * Brute Force
     * ============================================================================
     *
     * Idea
     *
     * Serialize using preorder together with inorder, or serialize complete
     * structural metadata separately.
     *
     * Invariant
     *
     * Enough information exists to uniquely reconstruct every subtree.
     *
     * Limitation
     *
     * Larger serialized output.
     *
     * More bookkeeping.
     *
     * More opportunities for implementation bugs.
     *
     * Complexity
     *
     * Time:
     *      O(n)
     *
     * Space:
     *      O(n)
     *
     * Interview Usefulness
     *
     * Mainly useful for motivating why a single preorder traversal with explicit
     * null markers is sufficient.
     */

    static final class BruteForce {

        public String serialize(TreeNode root) {
            StringBuilder preorder = new StringBuilder();
            StringBuilder inorder = new StringBuilder();

            preorder(root, preorder);
            inorder(root, inorder);

            return preorder.append("|").append(inorder).toString();
        }

        public TreeNode deserialize(String ignored) {
            throw new UnsupportedOperationException(
                    "Two traversals are intentionally omitted because this chapter "
                            + "focuses on the invariant-first optimal design.");
        }

        private void preorder(TreeNode node, StringBuilder sb) {
            if (node == null) {
                sb.append("n ");
                return;
            }

            sb.append(node.val).append(' ');
            preorder(node.left, sb);
            preorder(node.right, sb);
        }

        private void inorder(TreeNode node, StringBuilder sb) {
            if (node == null) {
                sb.append("n ");
                return;
            }

            inorder(node.left, sb);
            sb.append(node.val).append(' ');
            inorder(node.right, sb);
        }
    }

    /*
     * ============================================================================
     * Solution 2
     * Improved
     * ============================================================================
     *
     * Pattern
     *
     * Breadth-First Search
     *
     * Idea
     *
     * Serialize nodes level by level.
     *
     * Explicitly record null children so every parent always contributes exactly
     * two child positions.
     *
     * Invariant
     *
     * Queue always represents the current frontier whose children have not yet
     * been emitted.
     *
     * Improvement
     *
     * Naturally mirrors level-order traversal.
     *
     * Often easier to visualize during debugging.
     *
     * Complexity
     *
     * Time:
     *      O(n)
     *
     * Space:
     *      O(n)
     *
     * Interview Usefulness
     *
     * Excellent alternative when interviewer specifically requests iterative
     * serialization.
     */

    static final class ImprovedBFSCodec {

        public String serialize(TreeNode root) {

            if (root == null) {
                return "n";
            }

            StringBuilder sb = new StringBuilder();

            Queue<TreeNode> queue = new java.util.LinkedList<>();
            queue.offer(root);

            while (!queue.isEmpty()) {

                TreeNode current = queue.poll();

                // Invariant:
                // Every dequeued position contributes exactly one token.

                if (current == null) {
                    sb.append("n ");
                    continue;
                }

                sb.append(current.val).append(' ');

                queue.offer(current.left);
                queue.offer(current.right);
            }

            return sb.toString().trim();
        }

        public TreeNode deserialize(String data) {

            if (data == null || data.equals("n") || data.isEmpty()) {
                return null;
            }

            String[] tokens = data.split(" ");

            TreeNode root = new TreeNode(Integer.parseInt(tokens[0]));

            Queue<TreeNode> queue = new ArrayDeque<>();
            queue.offer(root);

            int index = 1;

            while (!queue.isEmpty() && index < tokens.length) {

                TreeNode current = queue.poll();

                // Invariant:
                // The next two unread tokens belong to this parent.

                if (!tokens[index].equals("n")) {
                    current.left = new TreeNode(Integer.parseInt(tokens[index]));
                    queue.offer(current.left);
                }
                index++;

                if (index >= tokens.length) {
                    break;
                }

                if (!tokens[index].equals("n")) {
                    current.right = new TreeNode(Integer.parseInt(tokens[index]));
                    queue.offer(current.right);
                }
                index++;
            }

            return root;
        }
    }

    /*
     * ============================================================================
     * Solution 3
     * Optimal (Interview Preferred)
     * ============================================================================
     *
     * Pattern
     *
     * Recursive DFS
     *
     * Traversal
     *
     * Preorder
     *
     * Grammar
     *
     *      Subtree
     *          :=
     *          n
     *          |
     *          value Subtree Subtree
     *
     * Idea
     *
     * Every recursive call owns exactly one subtree.
     *
     * During serialization it emits the subtree.
     *
     * During deserialization it consumes exactly the same subtree.
     *
     * Because both directions obey the identical recursive grammar,
     * they become perfect inverses.
     *
     * Invariant
     *
     * Queue head always points to the first unread token
     * belonging to the current subtree.
     *
     * Correctness
     *
     * Each recursive frame consumes precisely the tokens produced by
     * the corresponding serialization frame.
     *
     * No token is skipped.
     *
     * No token is reused.
     *
     * Therefore every subtree is reconstructed exactly once.
     *
     * Complexity
     *
     * Time
     *
     *      O(n)
     *
     * Space
     *
     *      O(n)
     *
     * Recursion depth
     *
     *      O(height)
     *
     * Interview Usefulness
     *
     * This is the canonical solution expected in interviews.
     */

    static final class OptimalDFSCodec {

        private static final String NULL = "n";

        public String serialize(TreeNode root) {

            StringBuilder builder = new StringBuilder();

            preorderSerialize(root, builder);

            return builder.toString().trim();
        }

        private void preorderSerialize(TreeNode node,
                                       StringBuilder builder) {

            // Invariant:
            // One recursive frame serializes exactly one subtree.

            if (node == null) {

                builder.append(NULL).append(' ');
                return;
            }

            builder.append(node.val).append(' ');

            // Left subtree immediately follows the root token.

            preorderSerialize(node.left, builder);

            // Right subtree immediately follows the left subtree.

            preorderSerialize(node.right, builder);
        }

        public TreeNode deserialize(String data) {

            if (data == null || data.isBlank()) {
                return null;
            }

            Queue<String> tokens =
                    new ArrayDeque<>(Arrays.asList(data.split(" ")));

            return preorderDeserialize(tokens);
        }

        private TreeNode preorderDeserialize(Queue<String> tokens) {

            // Invariant:
            // Queue head is always the beginning of this subtree.

            String token = tokens.poll();

            if (NULL.equals(token)) {
                return null;
            }

            TreeNode root = new TreeNode(Integer.parseInt(token));

            // The preorder grammar guarantees that the next unread
            // tokens belong to the left subtree.

            root.left = preorderDeserialize(tokens);

            // Whatever remains after the left subtree belongs to
            // the right subtree.

            root.right = preorderDeserialize(tokens);

            return root;
        }
    }

/*
 * =============================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =============================================================================
 *
 * If asked:
 *
 * "Explain your solution."
 *
 * A concise answer:
 *
 * I serialize the tree using preorder traversal together with explicit null
 * markers.
 *
 * The invariant is that every recursive call owns exactly one subtree.
 *
 * A null subtree emits exactly one token:
 *
 *      n
 *
 * A non-null subtree emits
 *
 *      root
 *      left subtree
 *      right subtree
 *
 * During deserialization I consume tokens using the identical recursive
 * grammar.
 *
 * Every recursive call consumes exactly the tokens belonging to one subtree.
 *
 * Because serialization and deserialization obey the same grammar,
 * they are perfect inverses.
 *
 * -------------------------------------------------------------------------
 * Discard Rule
 * -------------------------------------------------------------------------
 *
 * There is no search space to prune.
 *
 * Instead,
 * every recursive frame consumes its subtree completely before returning.
 *
 * -------------------------------------------------------------------------
 * Why Correct
 * -------------------------------------------------------------------------
 *
 * Every subtree has a unique serialized representation.
 *
 * Every serialized representation reconstructs exactly one subtree.
 *
 * Therefore the mapping is bijective.
 *
 * -------------------------------------------------------------------------
 * Why Termination Is Guaranteed
 * -------------------------------------------------------------------------
 *
 * Every recursive call immediately reaches either
 *
 *      null
 *
 * or a strictly smaller subtree.
 *
 * Every token is consumed exactly once.
 *
 * -------------------------------------------------------------------------
 * In-place Feasibility
 * -------------------------------------------------------------------------
 *
 * Not applicable.
 *
 * Serialization inherently produces a new representation.
 *
 * -------------------------------------------------------------------------
 * Streaming Feasibility
 * -------------------------------------------------------------------------
 *
 * Serialization naturally supports streaming output.
 *
 * Deserialization supports streaming input provided tokens arrive in preorder.
 *
 * -------------------------------------------------------------------------
 * When NOT To Use
 * -------------------------------------------------------------------------
 *
 * If only traversal order is required.
 *
 * If the tree is known to be a BST and a more compact specialized encoding
 * is desired.
 *
 * Otherwise this solution is broadly applicable.
 *
 * =============================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =============================================================================
 *
 * Trigger
 *
 *      Preserve an arbitrary binary tree exactly.
 *
 * Pattern
 *
 *      DFS Preorder + Null Markers
 *
 * Invariant
 *
 *      One recursive frame ↔ One subtree.
 *
 * Search Target
 *
 *      Consume exactly one subtree.
 *
 * Common Trap
 *
 *      Forgetting null markers.
 *
 * Edge Cases
 *
 *      Empty tree.
 *      Single node.
 *      Left-skewed tree.
 *      Right-skewed tree.
 *      Duplicate values.
 *
 * One-Liner
 *
 *      Emit root, left, right, while explicitly emitting every null child.
 *
 * Re-Derivation Cue
 *
 *      Think of preorder as a recursive grammar rather than as a traversal.
 */

    /*
     * =============================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =============================================================================
     *
     * -------------------------------------------------------------------------
     * Variation 1
     * -------------------------------------------------------------------------
     *
     * BFS Serialization
     *
     * Pattern
     *
     *      Level Order Traversal
     *
     * Invariant
     *
     * Every parent contributes exactly two child positions.
     *
     * Why It Works
     *
     * Missing children are explicitly emitted.
     *
     * Therefore topology is preserved.
     *
     * Trade-offs
     *
     * Advantages
     *
     * - iterative
     * - natural for complete trees
     * - easy visualization
     *
     * Disadvantages
     *
     * - queue management
     * - more bookkeeping
     *
     * -------------------------------------------------------------------------
     * Variation 2
     * -------------------------------------------------------------------------
     *
     * Index-Based DFS
     *
     * Instead of Queue<String>
     *
     * Maintain
     *
     *      int index
     *
     * shared across recursion.
     *
     * Invariant
     *
     * Index always points to the first unread token of the current subtree.
     *
     * Works Equally Well
     *
     * Queue simply encapsulates the advancing index.
     *
     * -------------------------------------------------------------------------
     * Variation 3
     * -------------------------------------------------------------------------
     *
     * Immutable Token Iterator
     *
     * Replace Queue with
     *
     *      Iterator<String>
     *
     * The recursive grammar remains identical.
     *
     * Only the token source changes.
     *
     * -------------------------------------------------------------------------
     * Variation 4
     * -------------------------------------------------------------------------
     *
     * Custom Delimiter
     *
     * Space
     *
     * may be replaced with
     *
     *      ,
     *      |
     *      #
     *
     * provided serialization and deserialization agree.
     *
     * Invariant is unchanged.
     *
     * -------------------------------------------------------------------------
     * Variation 5
     * -------------------------------------------------------------------------
     *
     * Binary Encoding
     *
     * Instead of textual tokens,
     * emit binary values.
     *
     * The recursive ownership invariant is unchanged.
     *
     * Only representation changes.
     *
     * -------------------------------------------------------------------------
     * Variation 6
     * -------------------------------------------------------------------------
     *
     * BST Serialization
     *
     * If the tree is guaranteed to be a BST,
     * null markers may be omitted.
     *
     * Why?
     *
     * BST ordering provides missing structural information.
     *
     * Pattern Break
     *
     * This optimization fails immediately for arbitrary binary trees.
     *
     * -------------------------------------------------------------------------
     * Variation 7
     * -------------------------------------------------------------------------
     *
     * N-ary Tree
     *
     * Instead of
     *
     *      left
     *      right
     *
     * recursively serialize every child.
     *
     * Additional child-count information is required.
     *
     * Pattern remains recursive grammar.
     *
     * =============================================================================
     * 🧠 MASTERY CHECKLIST
     * =============================================================================
     *
     * Can you answer these without looking?
     *
     * -------------------------------------------------------------------------
     * Invariant
     * -------------------------------------------------------------------------
     *
     * □ Every recursive frame owns exactly one subtree.
     *
     * □ Every subtree maps to one contiguous token sequence.
     *
     * □ Queue head always begins the current subtree.
     *
     * -------------------------------------------------------------------------
     * Search Target
     * -------------------------------------------------------------------------
     *
     * □ Consume exactly one subtree before returning.
     *
     * -------------------------------------------------------------------------
     * Transition
     * -------------------------------------------------------------------------
     *
     * □ Root
     * □ Left
     * □ Right
     *
     * -------------------------------------------------------------------------
     * Termination
     * -------------------------------------------------------------------------
     *
     * □ Null immediately returns.
     *
     * □ Every recursive call moves to a smaller subtree.
     *
     * -------------------------------------------------------------------------
     * Naive Failure
     * -------------------------------------------------------------------------
     *
     * □ Values alone cannot preserve topology.
     *
     * □ Duplicate values make topology even more important.
     *
     * -------------------------------------------------------------------------
     * Edge Cases
     * -------------------------------------------------------------------------
     *
     * □ Empty tree
     *
     * □ Single node
     *
     * □ Duplicate values
     *
     * □ Left chain
     *
     * □ Right chain
     *
     * □ Complete tree
     *
     * □ Sparse tree
     *
     * -------------------------------------------------------------------------
     * Debugging Readiness
     * -------------------------------------------------------------------------
     *
     * □ Can verify every emitted token belongs to one subtree.
     *
     * □ Can detect missing null markers.
     *
     * □ Can reason about token ownership.
     *
     * □ Can manually simulate recursion.
     *
     * -------------------------------------------------------------------------
     * Variant Readiness
     * -------------------------------------------------------------------------
     *
     * □ Can implement DFS version.
     *
     * □ Can implement BFS version.
     *
     * □ Can replace Queue with Iterator.
     *
     * □ Can replace Queue with shared index.
     *
     * -------------------------------------------------------------------------
     * Pattern Boundary
     * -------------------------------------------------------------------------
     *
     * □ Understand why BST serialization differs.
     *
     * □ Understand why null markers are mandatory for arbitrary trees.
     *
     * □ Understand why preorder alone is insufficient.
     *
     * =============================================================================
     * Helper Utilities For Tests
     * =============================================================================
     */

    private static boolean sameTree(TreeNode a, TreeNode b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.val == b.val
                && sameTree(a.left, b.left)
                && sameTree(a.right, b.right);
    }

    private static TreeNode sampleTree() {

        TreeNode root = new TreeNode(1);

        root.left = new TreeNode(2);

        root.right = new TreeNode(3);

        root.right.left = new TreeNode(4);

        root.right.right = new TreeNode(5);

        return root;
    }

    private static TreeNode leftSkewed() {

        TreeNode root = new TreeNode(10);

        root.left = new TreeNode(20);

        root.left.left = new TreeNode(30);

        root.left.left.left = new TreeNode(40);

        return root;
    }

    private static TreeNode rightSkewed() {

        TreeNode root = new TreeNode(10);

        root.right = new TreeNode(20);

        root.right.right = new TreeNode(30);

        root.right.right.right = new TreeNode(40);

        return root;
    }

    private static TreeNode duplicateTree() {

        TreeNode root = new TreeNode(7);

        root.left = new TreeNode(7);

        root.right = new TreeNode(7);

        root.left.left = new TreeNode(7);

        root.right.right = new TreeNode(7);

        return root;
    }

    /*
     * =============================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * =============================================================================
     */

    public static void main(String[] args) {

        OptimalDFSCodec dfsCodec = new OptimalDFSCodec();

        ImprovedBFSCodec bfsCodec = new ImprovedBFSCodec();

        /*
         * Happy Path
         *
         * Canonical interview example.
         */
        {
            TreeNode original = sampleTree();

            String serialized = dfsCodec.serialize(original);

            TreeNode reconstructed = dfsCodec.deserialize(serialized);

            assert sameTree(original, reconstructed)
                    : "DFS codec failed on representative example.";
        }

        /*
         * Empty tree.
         *
         * Smallest possible input.
         */
        {
            TreeNode reconstructed =
                    dfsCodec.deserialize(dfsCodec.serialize(null));

            assert reconstructed == null
                    : "Null tree should remain null.";
        }

        /*
         * Single node.
         *
         * Ensures both null children are preserved.
         */
        {
            TreeNode root = new TreeNode(42);

            TreeNode reconstructed =
                    dfsCodec.deserialize(dfsCodec.serialize(root));

            assert sameTree(root, reconstructed)
                    : "Single-node tree reconstruction failed.";
        }

        /*
         * Left-skewed tree.
         *
         * Verifies repeated left recursion.
         */
        {
            TreeNode original = leftSkewed();

            TreeNode reconstructed =
                    dfsCodec.deserialize(dfsCodec.serialize(original));

            assert sameTree(original, reconstructed)
                    : "Left-skewed tree reconstruction failed.";
        }

        /*
         * Right-skewed tree.
         *
         * Detects incorrect preorder ownership.
         */
        {
            TreeNode original = rightSkewed();

            TreeNode reconstructed =
                    dfsCodec.deserialize(dfsCodec.serialize(original));

            assert sameTree(original, reconstructed)
                    : "Right-skewed tree reconstruction failed.";
        }

        /*
         * Duplicate values.
         *
         * Demonstrates reconstruction depends on topology,
         * not value uniqueness.
         */
        {
            TreeNode original = duplicateTree();

            TreeNode reconstructed =
                    dfsCodec.deserialize(dfsCodec.serialize(original));

            assert sameTree(original, reconstructed)
                    : "Duplicate-value tree reconstruction failed.";
        }

        /*
         * Cross-check BFS implementation.
         *
         * Alternative serialization strategy.
         */
        {
            TreeNode original = sampleTree();

            String serialized = bfsCodec.serialize(original);

            TreeNode reconstructed = bfsCodec.deserialize(serialized);

            assert sameTree(original, reconstructed)
                    : "BFS codec reconstruction failed.";
        }

        /*
         * Repeated serialization.
         *
         * Serialization should be deterministic.
         */
        {
            TreeNode original = sampleTree();

            String first = dfsCodec.serialize(original);

            String second = dfsCodec.serialize(original);

            assert first.equals(second)
                    : "Serialization must be deterministic.";
        }

        /*
         * Round-trip stability.
         *
         * serialize -> deserialize -> serialize
         * should produce identical output.
         */
        {
            TreeNode original = sampleTree();

            String first = dfsCodec.serialize(original);

            TreeNode rebuilt = dfsCodec.deserialize(first);

            String second = dfsCodec.serialize(rebuilt);

            assert first.equals(second)
                    : "Round-trip serialization changed representation.";
        }

        /*
         * BFS and DFS both reconstruct the same logical tree.
         */
        {
            TreeNode original = sampleTree();

            TreeNode dfsTree =
                    dfsCodec.deserialize(dfsCodec.serialize(original));

            TreeNode bfsTree =
                    bfsCodec.deserialize(bfsCodec.serialize(original));

            assert sameTree(dfsTree, bfsTree)
                    : "DFS and BFS produced different reconstructed trees.";
        }

        System.out.println("All assertions passed.");
        System.out.println();
        System.out.println("I understand the invariant.");
        System.out.println();
        System.out.println("I can re-derive the solution.");
        System.out.println();
        System.out.println("I can physically reconstruct the implementation under pressure.");
        System.out.println();
        System.out.println("This chapter is complete.");
    }
}
