package org.chijai.day4.session2;

/**
 * =====================================================================================
 * 🧠 INVARIANT-FIRST ALGORITHM CHAPTER
 * Problem: Copy List with Random Pointer
 * =====================================================================================
 * <p>
 * This file is a complete, standalone, invariant-driven algorithm chapter.
 * It is designed so that months later, you can re-derive the solution
 * WITHOUT reopening LeetCode or the internet.
 * <p>
 * ⚠️ Exactly ONE public class. All others are static inner classes.
 * =====================================================================================
 */
public class CopyListWithRandomPointer {

    // =================================================================================
    // 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
    // =================================================================================
    /*
     * A linked list of length n is given such that each node contains an additional
     * random pointer, which could point to any node in the list, or null.
     *
     * Construct a deep copy of the list. The deep copy should consist of exactly n
     * brand new nodes, where each new node has its value set to the value of its
     * corresponding original node.
     *
     * Both the next and random pointer of the new nodes should point to new nodes
     * in the copied list such that the pointers in the original list and copied list
     * represent the same list state.
     *
     * None of the pointers in the new list should point to nodes in the original list.
     *
     * For example, if there are two nodes X and Y in the original list, where
     * X.random --> Y, then for the corresponding two nodes x and y in the copied list,
     * x.random --> y.
     *
     * Return the head of the copied linked list.
     *
     * The linked list is represented in the input/output as a list of n nodes.
     * Each node is represented as a pair of [val, random_index] where:
     *
     * val: an integer representing Node.val
     * random_index: the index of the node (range from 0 to n-1) that the random
     * pointer points to, or null if it does not point to any node.
     *
     * Your code will only be given the head of the original linked list.
     *
     * Example 1:
     * Input: head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
     * Output: [[7,null],[13,0],[11,4],[10,2],[1,0]]
     *
     * Example 2:
     * Input: head = [[1,1],[2,1]]
     * Output: [[1,1],[2,1]]
     *
     * Example 3:
     * Input: head = [[3,null],[3,0],[3,null]]
     * Output: [[3,null],[3,0],[3,null]]
     *
     * Constraints:
     * 0 <= n <= 1000
     * -10^4 <= Node.val <= 10^4
     * Node.random is null or pointing to some node in the linked list.
     *
     * 🔗 Link: https://leetcode.com/problems/copy-list-with-random-pointer/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Linked List, Hash Table
     */

    // =================================================================================
    // 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
    // =================================================================================
    /*
     * Pattern Name:
     *   Node-to-Node Structural Mapping with Invariant Preservation
     *
     * Problem Archetype:
     *   Deep copy of a graph-like structure with cross-links
     *
     * 🟢 Core Invariant (MANDATORY):
     *   Every original node must have exactly one corresponding clone,
     *   and all relationships (next, random) must be preserved exclusively
     *   among cloned nodes.
     *
     * Why this invariant works:
     *   Because random pointers are arbitrary, correctness depends on a
     *   bijection between original nodes and cloned nodes.
     *
     * When this pattern applies:
     *   - Deep copy with arbitrary cross pointers
     *   - Graph cloning
     *   - Object graphs with identity preservation
     *
     * 🧭 Pattern recognition signals:
     *   - Random pointers
     *   - Arbitrary back / forward references
     *   - "Deep copy" explicitly stated
     *
     * How this differs from similar patterns:
     *   - Unlike simple list copy, relationships are non-linear
     *   - Unlike DFS graph clone, structure is linear but references are not
     */

    // =================================================================================
    // 🟢 MENTAL MODEL & INVARIANTS (CANONICAL)
    // =================================================================================
    /*
     * Mental Model:
     *   Think of the list as a graph where each node has TWO outgoing edges:
     *   next and random.
     *
     *   A deep copy means recreating the graph with brand new nodes,
     *   while preserving the edge structure.
     *
     * ALL Invariants:
     *   1. One-to-one mapping between original and cloned nodes
     *   2. No cloned node points to any original node
     *   3. Relative structure (next/random topology) is identical
     *
     * State Representation:
     *   - Original node identity
     *   - Corresponding cloned node
     *
     * Allowed Moves:
     *   - Temporarily interleave cloned nodes with originals
     *   - Use original nodes as anchors to reach clones
     *
     * Forbidden Moves:
     *   - Assigning random before clone exists
     *   - Losing access to original structure
     *
     * Termination Logic:
     *   Finite list, linear traversal, each step advances
     *
     * Why common alternatives are inferior:
     *   Index-based or partial mappings break identity invariant
     */

    // =================================================================================
    // 🔴 WHY NAIVE / WRONG SOLUTIONS FAIL
    // =================================================================================
    /*
     * Typical wrong approach:
     *   - Try to assign random pointers using indices
     *
     * Why it seems correct:
     *   - The problem statement describes indices
     *
     * Invariant violated:
     *   - Node identity is not preserved by index
     *
     * Minimal counterexample:
     *   Two nodes pointing to same random target
     *
     * Interviewer trap:
     *   They wait for you to realize this is NOT an array problem
     */

    // =================================================================================
    // PRIMARY PROBLEM — SOLUTION CLASSES
    // =================================================================================

    // -----------------------------------------------------------------
    // Node definition
    // -----------------------------------------------------------------
    static class Node {
        int val;
        Node next;
        Node random;

        Node(int val) {
            this.val = val;
        }
    }

    // -----------------------------------------------------------------
    // Brute Force Solution (HashMap)
    // -----------------------------------------------------------------
    static class BruteForceSolution {
        /*
         * Core Idea:
         *   Maintain explicit mapping from original → clone
         *
         * Invariant:
         *   Mapping ensures one-to-one identity
         *
         * Time: O(n)
         * Space: O(n)
         * Interview Preference: Acceptable
         */
        static Node copyRandomList(Node head) {
            if (head == null) return null;

            java.util.Map<Node, Node> map = new java.util.HashMap<>();

            Node current = head;
            while (current != null) {
                map.put(current, new Node(current.val));
                current = current.next;
            }

            current = head;
            while (current != null) {
                Node clone = map.get(current);
                clone.next = map.get(current.next);
                clone.random = map.get(current.random);
                current = current.next;
            }

            return map.get(head);
        }
    }

    // -----------------------------------------------------------------
    // Optimal Solution (O(1) Extra Space)
    // -----------------------------------------------------------------
    static class OptimalSolution {
        /*
         * Core Idea:
         *   Interleave cloned nodes with original nodes
         *
         * FULL invariant enforcement without extra memory
         */
        static Node copyRandomList(Node head) {
            if (head == null) return null;

            // Step 1: Interleave clone nodes
            Node current = head;
            while (current != null) {
                Node clone = new Node(current.val);
                clone.next = current.next;
                current.next = clone;
                current = clone.next;
            }

            // Step 2: Assign random pointers using invariant
            current = head;
            while (current != null) {
                if (current.random != null) {
                    current.next.random = current.random.next;
                }
                current = current.next.next;
            }

            // Step 3: Separate the lists
            Node dummyHead = new Node(0);
            Node cloneCurrent = dummyHead;
            current = head;

            while (current != null) {
                cloneCurrent.next = current.next;
                current.next = current.next.next;

                cloneCurrent = cloneCurrent.next;
                current = current.next;
            }

            return dummyHead.next;
        }
    }

    // =================================================================================
    // ⚫ PATTERN REINFORCEMENT SUB-CHAPTERS (INVARIANT REUSED)
    // =================================================================================

    // =================================================================================
    // 1️⃣ CLONE GRAPH — LEETCODE 133
    // =================================================================================
    /*
     * 📘 FULL OFFICIAL STATEMENT
     *
     * Given a reference of a node in a connected undirected graph.
     * Return a deep copy (clone) of the graph.
     *
     * Each node in the graph contains a value (int) and a list of neighbors.
     *
     * The graph is represented as an adjacency list.
     *
     * Constraints:
     * - The number of nodes <= 100
     * - Node values are unique
     *
     * 🔗 https://leetcode.com/problems/clone-graph/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Graph, DFS, BFS, HashMap
     */

    /*
     * 🧠 INVARIANT MAPPING
     *
     * SAME invariant:
     *   Every original node must map to exactly one clone.
     *
     * What remains unchanged:
     *   - Identity mapping
     *   - Clone-before-link discipline
     *
     * What changes:
     *   - Traversal is DFS/BFS instead of linear
     */

    static class GraphNode {
        int val;
        java.util.List<GraphNode> neighbors = new java.util.ArrayList<>();

        GraphNode(int v) {
            val = v;
        }
    }

    static class CloneGraphSolution {
        static GraphNode cloneGraph(GraphNode node) {
            if (node == null) return null;

            java.util.Map<GraphNode, GraphNode> map = new java.util.HashMap<>();
            return dfs(node, map);
        }

        private static GraphNode dfs(GraphNode original,
                                     java.util.Map<GraphNode, GraphNode> map) {
            // 🟢 Identity invariant: one clone per original
            if (map.containsKey(original)) {
                return map.get(original);
            }

            GraphNode clone = new GraphNode(original.val);
            map.put(original, clone);

            for (GraphNode neighbor : original.neighbors) {
                clone.neighbors.add(dfs(neighbor, map));
            }
            return clone;
        }
    }

    /*
     * 🧪 EDGE CASE & TRAP
     *
     * Trap:
     *   Creating a new node every time you see a neighbor
     *
     * Invariant violated:
     *   One-to-one identity mapping
     */


    /*
     * 🟥 INTERVIEW NOTE
     *
     * Why interviewer still prefers O(1) version:
     *   - Same invariant
     *   - Stronger control of structure
     *   - Tests deeper understanding
     */

    // =================================================================================
// 3️⃣ DEEP COPY BINARY TREE WITH RANDOM POINTER
// =================================================================================
    /*
     * 📘 FULL PROBLEM STATEMENT (INTERVIEW / PREMIUM VARIANT)
     *
     * You are given the root of a binary tree where each node contains:
     *
     *   - int val
     *   - TreeNode left
     *   - TreeNode right
     *   - TreeNode random
     *
     * The random pointer can point to ANY node in the tree (including itself),
     * or be null.
     *
     * Your task is to construct a DEEP COPY of the binary tree.
     *
     * The deep copy must satisfy ALL of the following:
     *
     * 1. Each original node must have exactly ONE corresponding cloned node.
     * 2. The cloned node must have the same value as the original.
     * 3. The left and right child pointers of the cloned nodes must point to
     *    cloned nodes corresponding to the original children.
     * 4. The random pointer of each cloned node must point to the cloned node
     *    corresponding to the original node’s random pointer.
     * 5. NO pointer in the cloned tree may reference any node from the original tree.
     *
     * In short:
     *   The cloned tree must preserve BOTH structural relationships
     *   (left/right) AND arbitrary relationships (random),
     *   while being completely independent from the original tree.
     *
     * --------------------------------------------
     * INPUT FORMAT
     * --------------------------------------------
     * You are given ONLY the root of the original binary tree.
     *
     * TreeNode structure:
     *
     * class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode random;
     * }
     *
     * --------------------------------------------
     * OUTPUT FORMAT
     * --------------------------------------------
     * Return the root of the deep-copied binary tree.
     *
     * --------------------------------------------
     * EXAMPLE
     * --------------------------------------------
     * Original Tree:
     *
     *        1
     *       / \
     *      2   3
     *
     * Random pointers:
     *   1.random -> 3
     *   2.random -> 1
     *   3.random -> 2
     *
     * Output:
     *   A new binary tree with the same structure and random relationships,
     *   but with ALL nodes newly created.
     *
     * --------------------------------------------
     * CONSTRAINTS
     * --------------------------------------------
     * - Number of nodes ≤ 1000
     * - -10^4 ≤ Node.val ≤ 10^4
     * - random pointer is null or points to any node in the tree
     *
     * --------------------------------------------
     * 🧩 DIFFICULTY
     * --------------------------------------------
     * Medium
     *
     * --------------------------------------------
     * 🏷️ TAGS
     * --------------------------------------------
     * Binary Tree
     * Graph
     * DFS
     * HashMap
     * Deep Copy
     * Identity Mapping
     *
     * --------------------------------------------
     * 🧠 CORE INSIGHT
     * --------------------------------------------
     * Although the base structure is a TREE,
     * the presence of a random pointer converts the problem into a GRAPH.
     *
     * Therefore, the SAME invariant applies as:
     *   - Copy List with Random Pointer
     *   - Clone Graph
     *
     * 👉 Identity must be established BEFORE wiring.
     */

// -----------------------------------------------------------------------------
// 💻 JAVA SOLUTION (INVARIANT-DERIVED)
// -----------------------------------------------------------------------------

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode random;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static class CopyBinaryTreeWithRandom {

        /*
         * 🧠 CORE INVARIANT
         *
         * Each original TreeNode must map to EXACTLY ONE cloned TreeNode.
         * Once cloned, the same clone must be reused everywhere.
         *
         * This prevents:
         * - duplicate nodes
         * - broken random pointers
         * - infinite recursion due to cycles
         */

        static TreeNode copy(TreeNode root) {
            // HashMap preserves identity mapping: original → clone
            return clone(root, new java.util.HashMap<>());
        }

        private static TreeNode clone(
                TreeNode original,
                java.util.Map<TreeNode, TreeNode> visited
        ) {
            // 🟢 Base case: null remains null
            if (original == null) return null;

            // 🟢 Invariant enforcement:
            // If this node was already cloned, reuse it
            if (visited.containsKey(original)) {
                return visited.get(original);
            }

            // 🔵 Clone node FIRST (identity anchoring)
            TreeNode copy = new TreeNode(original.val);
            visited.put(original, copy);

            // 🟡 Wire structural edges
            copy.left = clone(original.left, visited);
            copy.right = clone(original.right, visited);

            // 🟡 Wire arbitrary edge (random)
            copy.random = clone(original.random, visited);

            return copy;
        }
    }

    // =================================================================================
// 🔁 ITERATIVE SOLUTION — DEEP COPY BINARY TREE WITH RANDOM POINTER
// =================================================================================
    /*
     * 🧠 ITERATIVE MENTAL MODEL
     *
     * Treat the tree as a GRAPH.
     * Use an explicit stack to traverse nodes.
     *
     * Invariant (unchanged):
     *   Each original node is cloned EXACTLY once.
     *   All pointers must reference ONLY cloned nodes.
     *
     * Traversal order does NOT matter.
     */

    static class CopyBinaryTreeWithRandomIterative {

        static TreeNode copy(TreeNode root) {
            if (root == null) return null;

            // 🟢 Identity mapping: original → clone
            java.util.Map<TreeNode, TreeNode> map = new java.util.HashMap<>();

            // 🔵 Explicit stack replaces recursion
            java.util.Deque<TreeNode> stack = new java.util.ArrayDeque<>();
            stack.push(root);

            // 🔵 Clone root first (identity anchoring)
            map.put(root, new TreeNode(root.val));

            while (!stack.isEmpty()) {
                TreeNode original = stack.pop();
                TreeNode clone = map.get(original);

                // 🟡 Process LEFT child
                if (original.left != null) {
                    if (!map.containsKey(original.left)) {
                        map.put(original.left, new TreeNode(original.left.val));
                        stack.push(original.left);
                    }
                    clone.left = map.get(original.left);
                }

                // 🟡 Process RIGHT child
                if (original.right != null) {
                    if (!map.containsKey(original.right)) {
                        map.put(original.right, new TreeNode(original.right.val));
                        stack.push(original.right);
                    }
                    clone.right = map.get(original.right);
                }

                // 🟡 Process RANDOM pointer
                if (original.random != null) {
                    if (!map.containsKey(original.random)) {
                        map.put(original.random, new TreeNode(original.random.val));
                        stack.push(original.random);
                    }
                    clone.random = map.get(original.random);
                }
            }

            return map.get(root);
        }
    }


    /*
     * 🧪 EDGE CASES & INTERVIEW TRAPS
     *
     * 1. Random pointer forming a cycle
     *    - Without HashMap → infinite recursion
     *
     * 2. random pointing to ancestor
     *    - Breaks naive tree assumptions
     *
     * 3. random pointing to self
     *    - Identity reuse must be correct
     *
     * 🟥 COMMON MISTAKE
     *   Treating this as a pure tree problem.
     *
     * 🧠 INTERVIEWER INTENT
     *   To test whether you recognize when a TREE
     *   silently becomes a GRAPH.
     */


    // =================================================================================
    // 5️⃣ COPY LIST WITH MULTIPLE RANDOM POINTERS (FOLLOW-UP VARIANT)
    // =================================================================================
    /*
     * 📘 PROBLEM STATEMENT (INTERVIEW VARIANT)
     *
     * Each node contains:
     *  - int val
     *  - Node next
     *  - List<Node> randoms   (instead of a single random pointer)
     *
     * Construct a deep copy of the list.
     * All random pointers must point ONLY to cloned nodes.
     *
     * 🧩 Difficulty: Medium+
     * 🏷️ Tags: Linked List, Graph, Identity Mapping
     */

    /*
     * 🧠 INVARIANT MAPPING
     *
     * SAME invariant (unchanged):
     *   Each original node must map to exactly one cloned node.
     *
     * What changes:
     *   - random pointer becomes a list
     *
     * What stays:
     *   - identity-first
     *   - wire-after-clone
     *
     * Interview value:
     *   Distinguishes invariant thinkers from memorized-code candidates.
     */

    static class MultiRandomNode {
        int val;
        MultiRandomNode next;
        java.util.List<MultiRandomNode> randoms = new java.util.ArrayList<>();

        MultiRandomNode(int v) {
            val = v;
        }
    }

    static class CopyListWithMultipleRandoms {
        static MultiRandomNode copy(MultiRandomNode head) {
            if (head == null) return null;

            // 🟢 Identity invariant: explicit mapping
            java.util.Map<MultiRandomNode, MultiRandomNode> map = new java.util.HashMap<>();

            MultiRandomNode curr = head;
            while (curr != null) {
                map.put(curr, new MultiRandomNode(curr.val));
                curr = curr.next;
            }

            curr = head;
            while (curr != null) {
                MultiRandomNode clone = map.get(curr);
                clone.next = map.get(curr.next);

                for (MultiRandomNode rnd : curr.randoms) {
                    clone.randoms.add(map.get(rnd));
                }
                curr = curr.next;
            }
            return map.get(head);
        }
    }

    /*
     * 🧪 EDGE CASE & TRAP
     *
     * Trap:
     *   Cloning random lists before all nodes exist
     *
     * Invariant violated:
     *   Wiring before identity anchoring
     */

    // =================================================================================
    // 6️⃣ CLONE N-ARY GRAPH WITH BACK-REFERENCES
    // =================================================================================
    /*
     * 📘 PROBLEM STATEMENT (SYSTEM DESIGN / INTERVIEW)
     *
     * Clone an N-ary graph.
     * Nodes may have:
     *  - arbitrary number of neighbors
     *  - back-references
     *  - cycles
     *
     * Return a deep copy of the graph.
     *
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Graph, DFS/BFS, Identity Mapping
     */

    /*
     * 🧠 INVARIANT MAPPING
     *
     * SAME invariant:
     *   Clone each node ONCE, reuse everywhere.
     *
     * What changes:
     *   - Degree > 2
     *   - Traversal flexibility
     *
     * What stays:
     *   - Identity mapping
     *   - Clone-before-link
     *
     * Interview signal:
     *   Structural changes do not affect invariant thinkers.
     */

    static class NaryNode {
        int val;
        java.util.List<NaryNode> neighbors = new java.util.ArrayList<>();

        NaryNode(int v) {
            val = v;
        }
    }

    static class CloneNaryGraph {
        static NaryNode clone(NaryNode node) {
            if (node == null) return null;
            return dfs(node, new java.util.HashMap<>());
        }

        private static NaryNode dfs(NaryNode original,
                                    java.util.Map<NaryNode, NaryNode> map) {
            // 🟢 Identity invariant enforcement
            if (map.containsKey(original)) {
                return map.get(original);
            }

            NaryNode clone = new NaryNode(original.val);
            map.put(original, clone);

            for (NaryNode neighbor : original.neighbors) {
                clone.neighbors.add(dfs(neighbor, map));
            }
            return clone;
        }
    }

    // =================================================================================
    // 🟣 INTERVIEW ARTICULATION
    // =================================================================================
    /*
     * State the invariant:
     *   Every original node has a unique clone directly next to it.
     *
     * Discard rule:
     *   Use original.next to reach clone, original.random.next for random clone.
     *
     * Correctness:
     *   No pointer assignment ever references original nodes.
     *
     * When NOT to use:
     *   Immutable nodes or concurrent modification constraints
     */

    // =================================================================================
    // 🟢 LEARNING VERIFICATION
    // =================================================================================
    /*
     * Invariant to recall:
     *   Original → Clone adjacency preserves identity
     *
     * Why naive fails:
     *   Indices ≠ identity
     *
     * Detect invariant in wild:
     *   Any deep copy with arbitrary references
     */

    // =================================================================================
    // 🧪 main() METHOD + SELF-VERIFYING TESTS
    // =================================================================================
    public static void main(String[] args) {
        // Happy path test
        Node a = new Node(1);
        Node b = new Node(2);
        a.next = b;
        a.random = b;
        b.random = b;

        Node copy = OptimalSolution.copyRandomList(a);

        assert copy != a;
        assert copy.val == 1;
        assert copy.random == copy.next;
        assert copy.next.random == copy.next;

        // Boundary case
        assert OptimalSolution.copyRandomList(null) == null;

        System.out.println("All invariant-based tests passed.");

        // Random list test
         a = new Node(1);
         b = new Node(2);
        a.next = b;
        a.random = b;
        b.random = a;

         copy = OptimalSolution.copyRandomList(a);

        assert copy != a;
        assert copy.random != b;
        assert copy.random.val == 2;
        assert copy.random.random == copy;

        // Graph clone sanity
        GraphNode g1 = new GraphNode(1);
        GraphNode g2 = new GraphNode(2);
        g1.neighbors.add(g2);
        g2.neighbors.add(g1);

        GraphNode cg = CloneGraphSolution.cloneGraph(g1);
        assert cg != g1;
        assert cg.neighbors.get(0) != g2;

        System.out.println("All reinforcement invariant tests passed.");
    }

    // =================================================================================
    // 🧠 CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
    // =================================================================================
    /*
     * Invariant clarity → One-to-one node mapping preserved
     * Search target clarity → Clone of original head
     * Discard logic → Skip original nodes after cloning
     * Termination guarantee → Linear traversal
     * Failure awareness → Index-based mapping fails
     * Edge-case confidence → null, self-random
     * Variant readiness → Graph clone
     * Pattern boundary → Immutable or shared memory
     */

    // =================================================================================
    // 🧘 FINAL CLOSURE STATEMENT
    // =================================================================================
    /*
     * For this problem, the invariant is one-to-one node identity preservation.
     * The answer represents a deep structural clone.
     * The search terminates because the list is finite.
     * I can re-derive this solution under pressure.
     * This chapter is complete.
     */
}
