package org.chijai.day8.graph.session2;

import java.util.*;

/**
 * CloneGraph
 *
 * ============================================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================================
 *
 * Title:
 * Clone Graph
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Graph
 * DFS
 * BFS
 * HashMap
 * Graph Traversal
 * Deep Copy
 *
 * LeetCode:
 * https://leetcode.com/problems/clone-graph/
 *
 * ----------------------------------------------------------------------------
 * Problem
 * ----------------------------------------------------------------------------
 *
 * Given a reference to a node in a connected undirected graph,
 * return a deep copy (clone) of the entire graph.
 *
 * Every node contains:
 *
 *     int val
 *     List<Node> neighbors
 *
 * Each value is unique.
 *
 * The returned graph must:
 *
 * • contain completely new node objects
 * • preserve every edge
 * • preserve every cycle
 * • preserve every self-loop
 * • preserve graph topology exactly
 *
 * No cloned node may reference an original node.
 *
 * ----------------------------------------------------------------------------
 * Constraints
 * ----------------------------------------------------------------------------
 *
 * • Number of nodes: 0 ... 100
 * • 1 <= Node.val <= 100
 * • Node.val is unique
 * • Graph is connected from the given node
 * • Graph may contain cycles
 * • Graph may contain self-loops
 *
 * ----------------------------------------------------------------------------
 * Representative Example
 * ----------------------------------------------------------------------------
 *
 * Input
 *
 * adjList =
 * [
 *   [2,4],
 *   [1,3],
 *   [2,4],
 *   [1,3]
 * ]
 *
 * Graph
 *
 *      1 ----- 2
 *      |       |
 *      |       |
 *      4 ----- 3
 *
 * Output
 *
 * identical graph structure,
 * but every node is newly allocated.
 *
 * ----------------------------------------------------------------------------
 * Example 2
 *
 * Input
 *
 * [[]]
 *
 * One isolated node.
 *
 * Output
 *
 * cloned isolated node.
 *
 * ----------------------------------------------------------------------------
 * Example 3
 *
 * Input
 *
 * []
 *
 * Output
 *
 * null
 *
 * ============================================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern
 *
 * Graph Traversal + HashMap Memoization
 *
 * Archetype
 *
 * "Visit each state exactly once while remembering its translated copy."
 *
 * ----------------------------------------------------------------------------
 * Core Invariant
 * ----------------------------------------------------------------------------
 *
 * For every original node that has already been discovered,
 * there exists exactly one cloned node stored in the map.
 *
 * original -----> clone
 *
 * Once created,
 * the clone object never changes identity.
 *
 * Only its neighbor list grows.
 *
 * ----------------------------------------------------------------------------
 * Why it works
 * ----------------------------------------------------------------------------
 *
 * Graphs are not trees.
 *
 * A node can be reached:
 *
 * • through multiple parents
 * • through cycles
 * • through itself
 *
 * Therefore recursive creation without remembering previous work
 * repeatedly recreates the same logical vertex.
 *
 * The HashMap guarantees:
 *
 * one original node
 * →
 * one cloned node
 *
 * throughout the entire traversal.
 *
 * ----------------------------------------------------------------------------
 * Recognition Signals
 * ----------------------------------------------------------------------------
 *
 * Use this pattern when:
 *
 * • graph contains cycles
 * • graph contains shared neighbors
 * • deep copy is required
 * • identity must be preserved
 * • references must be rebuilt
 *
 * ----------------------------------------------------------------------------
 * When NOT to use
 * ----------------------------------------------------------------------------
 *
 * Not needed for:
 *
 * • trees without parent pointers
 * • arrays
 * • immutable structures
 * • simple value copying
 *
 * ----------------------------------------------------------------------------
 * Comparison
 * ----------------------------------------------------------------------------
 *
 * Tree Copy
 *
 * No memoization required.
 *
 * Every node reached exactly once.
 *
 * -------------------------
 *
 * Graph Copy
 *
 * Memoization mandatory.
 *
 * Same node reachable many times.
 *
 * -------------------------
 *
 * Graph Traversal Only
 *
 * visited:Set<Node>
 *
 * -------------------------
 *
 * Graph Copy
 *
 * oldNode -> newNode map
 *
 * because traversal state and cloned object must stay synchronized.
 *
 * ============================================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * Mental Model
 * ----------------------------------------------------------------------------
 *
 * Imagine rebuilding a city.
 *
 * Every original building receives exactly one replacement building.
 *
 * Roads cannot be connected until destination buildings exist.
 *
 * Therefore:
 *
 * Step 1
 *
 * Create building.
 *
 * Step 2
 *
 * Register mapping.
 *
 * Step 3
 *
 * Build outgoing roads.
 *
 * Never reverse Step 2 and Step 3.
 *
 * ----------------------------------------------------------------------------
 * Primary Invariant
 * ----------------------------------------------------------------------------
 *
 * Whenever traversal begins expanding a node,
 * its clone already exists inside the map.
 *
 * Therefore every future reference to that node
 * immediately returns the same clone.
 *
 * ----------------------------------------------------------------------------
 * Secondary Invariant
 * ----------------------------------------------------------------------------
 *
 * Map size
 * ==
 * number of cloned vertices.
 *
 * Never larger.
 *
 * Never smaller.
 *
 * ----------------------------------------------------------------------------
 * DFS Invariant
 * ----------------------------------------------------------------------------
 *
 * Before recursively visiting neighbors:
 *
 * clone already stored.
 *
 * This single ordering breaks every possible cycle.
 *
 * ----------------------------------------------------------------------------
 * BFS Invariant
 * ----------------------------------------------------------------------------
 *
 * Every queued node already owns a clone.
 *
 * Queue expansion only fills neighbor lists.
 *
 * ----------------------------------------------------------------------------
 * Variable Meaning
 * ----------------------------------------------------------------------------
 *
 * node
 *
 * Current original vertex.
 *
 * clone
 *
 * Deep-copy counterpart.
 *
 * map
 *
 * Original
 * →
 * Clone
 *
 * queue / recursion
 *
 * Frontier of unexplored vertices.
 *
 * ----------------------------------------------------------------------------
 * Allowed State Transitions
 * ----------------------------------------------------------------------------
 *
 * unseen original
 * →
 * create clone
 * →
 * store mapping
 * →
 * traverse neighbors
 * →
 * append cloned neighbors
 *
 * ----------------------------------------------------------------------------
 * Forbidden Transition
 * ----------------------------------------------------------------------------
 *
 * Traverse neighbors
 * →
 * then create clone
 *
 * This immediately causes infinite recursion on cycles.
 *
 * ----------------------------------------------------------------------------
 * Termination
 * ----------------------------------------------------------------------------
 *
 * Every original node is inserted into the map exactly once.
 *
 * After insertion,
 * that node is never recursively expanded again.
 *
 * Finite graph
 * +
 * one insertion per vertex
 * =
 * guaranteed termination.
 *
 * ----------------------------------------------------------------------------
 * Correctness Intuition
 * ----------------------------------------------------------------------------
 *
 * Every edge
 *
 * u ---- v
 *
 * becomes
 *
 * clone(u) ---- clone(v)
 *
 * because:
 *
 * clone(u)
 * already exists
 *
 * and
 *
 * clone(v)
 * is either:
 *
 * • already mapped
 * • created recursively
 *
 * Every original edge is recreated exactly once.
 *
 * ----------------------------------------------------------------------------
 * Why Naive Solutions Fail
 * ----------------------------------------------------------------------------
 *
 * Recursive copying without memory:
 *
 * 1 -> 2 -> 1 -> 2 -> ...
 *
 * infinite recursion.
 *
 * Copying neighbors first:
 *
 * impossible because destination clone
 * has not yet been allocated.
 *
 * Creating duplicate clones:
 *
 * original node
 *
 *      5
 *
 * reached from
 *
 * A and B
 *
 * creates
 *
 * clone5a
 * clone5b
 *
 * Graph topology becomes incorrect.
 *
 * ============================================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================================
 *
 * Mistake 1
 * ----------
 *
 * Forgetting HashMap.
 *
 * Looks correct on trees.
 *
 * Fails on cycles.
 *
 * Violated invariant:
 *
 * one original
 * →
 * one clone.
 *
 * Counterexample:
 *
 * 1--2
 * |  |
 * 4--3
 *
 * DFS loops forever.
 *
 * ---------------------------------------------------------------------------
 *
 * Mistake 2
 * ----------
 *
 * Inserting into map after recursive calls.
 *
 * Why it appears correct:
 *
 * "I'll create clone after children."
 *
 * Reality:
 *
 * Cycle revisits node before insertion.
 *
 * Infinite recursion.
 *
 * Violated invariant:
 *
 * clone must exist before expansion.
 *
 * ---------------------------------------------------------------------------
 *
 * Mistake 3
 * ----------
 *
 * Using node value as visited instead of node reference.
 *
 * Works only because this problem guarantees unique values.
 *
 * In generic graph cloning,
 * identity—not value—is the invariant.
 *
 * Interviewers often generalize this.
 *
 * ---------------------------------------------------------------------------
 *
 * Mistake 4
 * ----------
 *
 * Forgetting neighbor linkage.
 *
 * Clones exist,
 * but graph contains isolated nodes.
 *
 * ---------------------------------------------------------------------------
 *
 * Mistake 5
 * ----------
 *
 * Accidentally attaching original neighbors
 * into cloned graph.
 *
 * Produces hybrid graph.
 *
 * Deep copy requirement violated.
 *
 * ============================================================================
 * ⚙️ IMPLEMENTATION BLUEPRINT
 * ============================================================================
 *
 * Mechanical typing order (DFS)
 *
 * 1.
 *
 * Method(node)
 *
 * 2.
 *
 * Null check.
 *
 * 3.
 *
 * Already cloned?
 *
 * return clone.
 *
 * 4.
 *
 * Allocate clone.
 *
 * 5.
 *
 * Store mapping immediately.
 *
 * 6.
 *
 * Iterate neighbors.
 *
 * 7.
 *
 * Clone neighbor recursively.
 *
 * 8.
 *
 * Append cloned neighbor.
 *
 * 9.
 *
 * Return clone.
 *
 * ----------------------------------------------------------------------------
 *
 * Mechanical typing order (BFS)
 *
 * 1.
 *
 * Null check.
 *
 * 2.
 *
 * Create map.
 *
 * 3.
 *
 * Create queue.
 *
 * 4.
 *
 * Clone source.
 *
 * 5.
 *
 * Insert into map.
 *
 * 6.
 *
 * Push source.
 *
 * 7.
 *
 * While queue not empty:
 *
 *      pop current
 *
 *      for every neighbor
 *
 *          unseen?
 *              create
 *              map
 *              enqueue
 *
 *          connect clones
 *
 * 8.
 *
 * Return source clone.
 *
 * ============================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================================
 *
 * if null
 *     return
 *
 * if already cloned
 *     return clone
 *
 * create clone
 * register mapping
 *
 * for every neighbor
 *     clone recursively
 *     connect
 *
 * return clone
 *
 * ============================================================================
 * 6. SOLUTION CLASSES
 * ============================================================================
 */
public class CloneGraph {

    static class Node {
        int val;
        List<Node> neighbors;

        Node() {
            neighbors = new ArrayList<>();
        }

        Node(int val) {
            this.val = val;
            this.neighbors = new ArrayList<>();
        }

        Node(int val, List<Node> neighbors) {
            this.val = val;
            this.neighbors = neighbors;
        }
    }

    /**
     * =========================================================================
     * Brute Force
     * =========================================================================
     *
     * Idea
     *
     * Recursively recreate every neighbor without remembering
     * previously visited nodes.
     *
     * Invariant
     *
     * None that survives cycles.
     *
     * Limitation
     *
     * Infinite recursion on cyclic graphs.
     *
     * Complexity
     *
     * Undefined because recursion never terminates.
     *
     * Interview usefulness
     *
     * Useful only to explain why memoization is mandatory.
     */
    static class BruteForce {
        Node cloneGraph(Node node) {
            throw new UnsupportedOperationException(
                    "Brute force graph cloning fails on cyclic graphs.");
        }
    }

/**
 * =========================================================================
 * Improved
 * =========================================================================

 /**
 * Idea
 *
 * Traverse the graph using BFS while maintaining a mapping from each
 * original node to its unique clone.
 *
 * 🟢 Invariant
 *
 * Every node inside the queue already has an allocated clone stored
 * inside the map.
 *
 * Improvement
 *
 * Eliminates infinite recursion while preserving graph identity.
 *
 * Complexity
 *
 * Time:
 * O(|V| + |E|)
 *
 * Space:
 * O(|V|)
 *
 * Interview usefulness
 *
 * Demonstrates iterative graph traversal and avoids recursion depth limits.
 */
static class Improved {

    Node cloneGraph(Node node) {

        // 🔴 Edge Case
        if (node == null) {
            return null;
        }

        Map<Node, Node> map = new HashMap<>();
        Queue<Node> queue = new ArrayDeque<>();

        Node sourceClone = new Node(node.val);

        map.put(node, sourceClone);
        queue.offer(node);

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            Node currentClone = map.get(current);

            for (Node neighbor : current.neighbors) {

                if (!map.containsKey(neighbor)) {

                    // 🟢 Invariant:
                    // Clone exists before neighbor expansion.
                    map.put(neighbor, new Node(neighbor.val));

                    queue.offer(neighbor);
                }

                // 🟢 Preserve every edge exactly once.
                currentClone.neighbors.add(map.get(neighbor));
            }
        }

        return sourceClone;
    }
}

    /**
     * =========================================================================
     * Optimal (Interview Preferred)
     * =========================================================================
     *
     * Idea
     *
     * DFS + Memoization.
     *
     * Every original node receives exactly one cloned node.
     *
     * Recursion naturally traverses the graph while the HashMap prevents
     * duplicate cloning.
     *
     * 🟢 Invariant
     *
     * Before recursively exploring neighbors,
     * the clone already exists inside the map.
     *
     * This is the single most important invariant.
     *
     * Correctness
     *
     * Every original node
     * →
     * exactly one clone.
     *
     * Every original edge
     * →
     * exactly one cloned edge.
     *
     * Cycles terminate because revisits immediately return the stored clone.
     *
     * Complexity
     *
     * Time:
     * O(|V| + |E|)
     *
     * Space:
     * O(|V|)
     *
     * Interview usefulness
     *
     * This is the canonical solution expected by most interviewers.
     */
    static class Optimal {

        private final Map<Node, Node> oldToNew = new HashMap<>();

        Node cloneGraph(Node node) {

            // 🔴 Empty graph handled immediately.
            if (node == null) {
                return null;
            }

            // 🟢 Invariant:
            // Existing mapping means this state has already been cloned.
            if (oldToNew.containsKey(node)) {
                return oldToNew.get(node);
            }

            Node clone = new Node(node.val);

            // 🟢 Critical ordering:
            // Register before exploring neighbors.
            oldToNew.put(node, clone);

            for (Node neighbor : node.neighbors) {

                // 🟢 Neighbor clone is guaranteed to exist after recursion.
                clone.neighbors.add(cloneGraph(neighbor));
            }

            return clone;
        }
    }

    /*
     * =====================================================================================
     * 🧠 DFS RECURSION VISUALIZATION — GRAPH VS RECURSION TREE
     * =====================================================================================
     *
     * IMPORTANT:
     *
     * The ORIGINAL structure is a GRAPH and may contain cycles.
     *
     * Example:
     *
     *      A ----- B
     *      |       |
     *      |       |
     *      D ----- C
     *
     * But recursive execution forms a CALL TREE.
     *
     * Suppose DFS starts from A:
     *
     * cloneGraph(A)
     * │
     * ├── cloneGraph(B)
     * │   │
     * │   ├── cloneGraph(A)
     * │   │       ↓
     * │   │   already in map
     * │   │   return A'          ← STOP branch
     * │   │
     * │   └── cloneGraph(C)
     * │       │
     * │       ├── cloneGraph(B)
     * │       │       ↓
     * │       │   already mapped
     * │       │   return B'      ← STOP branch
     * │       │
     * │       └── cloneGraph(D)
     * │           │
     * │           ├── cloneGraph(A)
     * │           │       ↓
     * │           │   return A'  ← STOP
     * │           │
     * │           └── cloneGraph(C)
     * │                   ↓
     * │               return C'  ← STOP
     * │
     * └── cloneGraph(D)
     *         ↓
     *     already mapped
     *     return D'              ← STOP
     *
     *
     * Compact recursion tree:
     *
     *              A
     *            /   \
     *           B     D✓
     *         /   \
     *       A✓     C
     *             / \
     *           B✓   D
     *               / \
     *             A✓   C✓
     *
     * ✓ = node already exists in oldToNew,
     *     so recursion DOES NOT expand further.
     *
     *
     * =====================================================================================
     * 🔑 CORE RECURSION RULE
     * =====================================================================================
     *
     * FIRST VISIT:
     *
     *      CREATE
     *        ↓
     *      MAP IT
     *        ↓
     *      EXPLORE neighbors
     *
     *
     * REVISIT:
     *
     *      Already mapped?
     *          ↓
     *      Return existing clone
     *          ↓
     *      STOP this recursion branch
     *
     *
     * Therefore:
     *
     *      FIRST VISIT  → EXPANDS
     *      REVISIT      → RETURNS
     *
     *
     * =====================================================================================
     * 🟢 CRITICAL ORDERING INVARIANT
     * =====================================================================================
     *
     * The clone MUST be registered BEFORE recursively exploring neighbors.
     *
     * Correct:
     *
     *      Node clone = new Node(node.val);
     *      oldToNew.put(node, clone);       // REGISTER FIRST
     *
     *      for (Node neighbor : node.neighbors) {
     *          clone.neighbors.add(cloneGraph(neighbor));
     *      }
     *
     *
     * Why?
     *
     * Consider cycle:
     *
     *      A → B
     *      ↑   ↓
     *      └───┘
     *
     * Execution:
     *
     *      clone(A)
     *          create A'
     *          map A → A'
     *
     *          clone(B)
     *              create B'
     *              map B → B'
     *
     *              clone(A)
     *                  A already mapped
     *                  return A'
     *
     * The cycle terminates.
     *
     *
     * WRONG ORDER:
     *
     *      CREATE
     *        ↓
     *      RECURSE
     *        ↓
     *      MAP
     *
     * Then for A ↔ B:
     *
     *      A
     *       → B
     *          → A
     *             → B
     *                → A
     *                   → ...
     *
     * Infinite recursion, because A was not registered before recursion returned to it.
     *
     *
     * =====================================================================================
     * 🧠 MENTAL MODEL
     * =====================================================================================
     *
     * Do NOT think:
     *
     *      "The graph became a tree."
     *
     * Think:
     *
     *      "The graph remains cyclic,
     *       but recursion creates a call tree.
     *
     *       HashMap turns every revisit into a leaf."
     *
     *
     * One-line memory anchor:
     *
     *      CREATE → MAP → EXPLORE
     *
     * And:
     *
     *      FIRST VISIT EXPANDS.
     *      REVISIT RETURNS.
     */



    /**
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * Explain the invariant
 * ---------------------
 *
 * "The key invariant is that every original node has at most one clone.
 * As soon as I allocate a clone, I immediately store it in the HashMap.
 * Any future visit simply reuses that clone."
 *
 * -------------------------------------------------------------------------
 *
 * Explain the discard rule
 * ------------------------
 *
 * Unlike binary search, we do not discard part of the search space.
 *
 * Instead,
 * we discard repeated exploration.
 *
 * Once a node has been mapped,
 * recursion stops expanding that state again.
 *
 * -------------------------------------------------------------------------
 *
 * Explain correctness
 * -------------------
 *
 * Because every node has exactly one clone,
 * all incoming and outgoing edges point toward the same cloned object,
 * preserving graph topology.
 *
 * -------------------------------------------------------------------------
 *
 * Explain termination
 * -------------------
 *
 * Every original node enters the map once.
 *
 * Every future encounter immediately returns.
 *
 * Since the graph is finite,
 * recursion must terminate.
 *
 * -------------------------------------------------------------------------
 *
 * In-place feasibility
 * --------------------
 *
 * Impossible.
 *
 * A deep copy requires allocating completely new nodes.
 *
 * -------------------------------------------------------------------------
 *
 * Streaming feasibility
 * ---------------------
 *
 * Impossible in the general case.
 *
 * Neighbor relationships may point to nodes discovered later,
 * requiring previously allocated clone references.
 *
 * -------------------------------------------------------------------------
 *
 * When NOT to use this pattern
 * ----------------------------
 *
 * If the task only asks for graph traversal,
 * a visited set is sufficient.
 *
 * If the graph is guaranteed to be a tree,
 * memoization is unnecessary.
 *
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 * -------
 *
 * Deep copy of a graph.
 *
 * -------------------------------------------------------------------------
 *
 * Pattern
 * -------
 *
 * DFS/BFS + HashMap Memoization.
 *
 * -------------------------------------------------------------------------
 *
 * Invariant
 * ---------
 *
 * Every original node owns exactly one clone.
 *
 * -------------------------------------------------------------------------
 *
 * Search Target
 * -------------
 *
 * Visit every reachable node exactly once.
 *
 * -------------------------------------------------------------------------
 *
 * Discard Rule
 * ------------
 *
 * Already cloned?
 *
 * Return immediately.
 *
 * -------------------------------------------------------------------------
 *
 * Common Trap
 * -----------
 *
 * Inserting into the map after recursion.
 *
 * -------------------------------------------------------------------------
 *
 * Edge Cases
 * ----------
 *
 * • null
 * • one node
 * • cycle
 * • self-loop
 * • shared neighbor
 *
 * -------------------------------------------------------------------------
 *
 * One-Liner
 * ---------
 *
 * "Allocate once, remember forever."
 *
 * -------------------------------------------------------------------------
 *
 * Re-Derivation Cue
 * -----------------
 *
 * Graph with cycles
 * →
 * recursion needs memory
 * →
 * memory maps original to clone.
 *
 * =========================================================================
 * 🔄 VARIATIONS & TWEAKS
 * =========================================================================
 *
 * Variation 1
 * -----------
 *
 * DFS Recursive
 *
 * Reasoning
 *
 * Natural recursive expansion.
 *
 * Preserves invariant unchanged.
 *
 * -------------------------------------------------------------------------
 *
 * Variation 2
 * -----------
 *
 * BFS Iterative
 *
 * Reasoning
 *
 * Explicit queue replaces recursion.
 *
 * Mapping invariant remains identical.
 *
 * -------------------------------------------------------------------------
 *
 * Variation 3
 * -----------
 *
 * Directed Graph
 *
 * Still works.
 *
 * Neighbor direction is copied exactly.
 *
 * -------------------------------------------------------------------------
 *
 * Variation 4
 * -----------
 *
 * Self-loop
 *
 * Node
 * →
 * itself
 *
 * Already stored clone prevents infinite recursion.
 *
 * -------------------------------------------------------------------------
 *
 * Variation 5
 * -----------
 *
 * Disconnected Graph
 *
 * Pattern changes slightly.
 *
 * Iterate over every unvisited component,
 * invoking clone from each source.

 /**
 * -------------------------------------------------------------------------
 *
 * Variation 6
 * -----------
 *
 * Node values are not unique.
 *
 * Reasoning
 *
 * Never use node values as HashMap keys.
 *
 * The invariant depends on object identity, not stored data.
 *
 * -------------------------------------------------------------------------
 *
 * Pattern Break
 * -------------
 *
 * Using
 *
 * Map<Integer, Node>
 *
 * instead of
 *
 * Map<Node, Node>
 *
 * only works because this LeetCode problem guarantees unique values.
 *
 * In production code or interviews where uniqueness is removed,
 * multiple distinct nodes could overwrite one another.
 *
 * The invariant
 *
 * one original object
 * →
 * one cloned object
 *
 * is destroyed.
 *
 * =========================================================================
 * 🧠 MASTERY CHECKLIST
 * =========================================================================
 *
 * □ What is the invariant?
 *
 * Every original node has exactly one cloned node.
 *
 * -------------------------------------------------------------------------
 *
 * □ Why store before recursion?
 *
 * So cycles immediately resolve to the existing clone.
 *
 * -------------------------------------------------------------------------
 *
 * □ What is the search target?
 *
 * Every reachable vertex.
 *
 * -------------------------------------------------------------------------
 *
 * □ What is the transition?
 *
 * Original node
 * →
 * clone node
 * →
 * recursively clone neighbors
 * →
 * connect edges.
 *
 * -------------------------------------------------------------------------
 *
 * □ What prevents infinite recursion?
 *
 * The memoization map.
 *
 * -------------------------------------------------------------------------
 *
 * □ Why does every edge remain correct?
 *
 * Because every endpoint has a unique clone,
 * and each original edge recreates one cloned edge.
 *
 * -------------------------------------------------------------------------
 *
 * □ Why does the naive approach fail?
 *
 * Cycles revisit nodes forever.
 *
 * -------------------------------------------------------------------------
 *
 * □ Which edge cases should be remembered?
 *
 * • null
 * • isolated node
 * • cycle
 * • self-loop
 * • shared neighbor
 *
 * -------------------------------------------------------------------------
 *
 * □ Debugging readiness
 *
 * Verify:
 *
 * map size
 * ==
 * number of unique visited vertices.
 *
 * Every cloned neighbor should belong to the map,
 * never to the original graph.
 *
 * -------------------------------------------------------------------------
 *
 * □ Variant readiness
 *
 * Able to switch between DFS and BFS
 * without changing the invariant.
 *
 * -------------------------------------------------------------------------
 *
 * □ Pattern boundary
 *
 * Memoization is mandatory for graphs.
 *
 * Trees generally do not require it.
 *
 * =========================================================================
 * Utility Methods Used By Tests
 * =========================================================================
 */

private static Node buildRepresentativeGraph() {

    Node n1 = new Node(1);
    Node n2 = new Node(2);
    Node n3 = new Node(3);
    Node n4 = new Node(4);

    n1.neighbors.add(n2);
    n1.neighbors.add(n4);

    n2.neighbors.add(n1);
    n2.neighbors.add(n3);

    n3.neighbors.add(n2);
    n3.neighbors.add(n4);

    n4.neighbors.add(n1);
    n4.neighbors.add(n3);

    return n1;
}

    private static Node buildSingleNode() {
        return new Node(1);
    }

    private static Node buildSelfLoopGraph() {

        Node node = new Node(7);
        node.neighbors.add(node);

        return node;
    }

    private static int countNodes(Node node) {

        if (node == null) {
            return 0;
        }

        Set<Node> visited = new HashSet<>();
        Queue<Node> queue = new ArrayDeque<>();

        visited.add(node);
        queue.offer(node);

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            for (Node neighbor : current.neighbors) {

                if (visited.add(neighbor)) {
                    queue.offer(neighbor);
                }
            }
        }

        return visited.size();
    }

    private static Map<Integer, Node> indexByValue(Node node) {

        Map<Integer, Node> result = new HashMap<>();

        if (node == null) {
            return result;
        }

        Queue<Node> queue = new ArrayDeque<>();
        Set<Node> visited = new HashSet<>();

        queue.offer(node);
        visited.add(node);

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            result.put(current.val, current);

            for (Node neighbor : current.neighbors) {

                if (visited.add(neighbor)) {
                    queue.offer(neighbor);
                }
            }
        }

        return result;
    }

    private static void verifyDeepCopy(Node original, Node clone) {

        assert original != clone :
                "Root node must be newly allocated.";

        Map<Integer, Node> originalMap = indexByValue(original);
        Map<Integer, Node> cloneMap = indexByValue(clone);

        assert originalMap.size() == cloneMap.size() :
                "Both graphs must contain the same number of vertices.";

        for (Map.Entry<Integer, Node> entry : originalMap.entrySet()) {

            Node originalNode = entry.getValue();
            Node clonedNode = cloneMap.get(entry.getKey());

            assert clonedNode != null :
                    "Every original node must exist in clone.";

            assert originalNode != clonedNode :
                    "Original node and clone must never share identity.";

            assert originalNode.val == clonedNode.val :
                    "Node values must match.";

            assert originalNode.neighbors.size() == clonedNode.neighbors.size() :
                    "Neighbor counts must remain identical.";

            for (int i = 0; i < originalNode.neighbors.size(); i++) {

                Node originalNeighbor = originalNode.neighbors.get(i);
                Node clonedNeighbor = clonedNode.neighbors.get(i);

                assert clonedNeighbor.val == originalNeighbor.val :
                        "Edge endpoints must be preserved.";

                assert clonedNeighbor != originalNeighbor :
                        "Neighbor must also be deeply cloned.";
            }
        }
    }

/**
 * =========================================================================
 * 🧪 MAIN + SELF-VERIFYING TESTS
 * =========================================================================
 */

public static void main(String[] args) {

    Optimal dfs = new Optimal();
    Improved bfs = new Improved();

    // ---------------------------------------------------------------------
    // Happy Path
    // Representative cyclic graph from the problem statement.
    // ---------------------------------------------------------------------
    Node graph = buildRepresentativeGraph();

    Node dfsClone = dfs.cloneGraph(graph);
    verifyDeepCopy(graph, dfsClone);

    Node bfsClone = bfs.cloneGraph(graph);
    verifyDeepCopy(graph, bfsClone);

    // ---------------------------------------------------------------------
    // Edge Case
    // Null input should remain null.
    // ---------------------------------------------------------------------
    assert dfs.cloneGraph(null) == null :
            "Null graph should clone to null.";

    assert bfs.cloneGraph(null) == null :
            "Null graph should clone to null.";

    // ---------------------------------------------------------------------
    // Boundary Case
    // Single isolated vertex.
    // ---------------------------------------------------------------------
    Node single = buildSingleNode();

    Node singleClone = new Optimal().cloneGraph(single);

    assert singleClone != single :
            "Single node must be newly allocated.";

    assert singleClone.val == single.val :
            "Node value should be preserved.";

    assert singleClone.neighbors.isEmpty() :
            "Isolated node must remain isolated.";

    // ---------------------------------------------------------------------
    // Interview Trap
    // Self-loop.
    // ---------------------------------------------------------------------
    Node selfLoop = buildSelfLoopGraph();

    Node selfLoopClone = new Optimal().cloneGraph(selfLoop);

    assert selfLoopClone != selfLoop :
            "Self-loop node must be cloned.";

    assert selfLoopClone.neighbors.size() == 1 :
            "Self-loop must be preserved.";

    assert selfLoopClone.neighbors.get(0) == selfLoopClone :
            "Neighbor should point to cloned self, not original.";

    // ---------------------------------------------------------------------
    // Shared neighbor / cycle preservation.
    // ---------------------------------------------------------------------
    assert countNodes(graph) == countNodes(dfsClone) :
            "Clone must preserve every reachable vertex.";

    assert countNodes(graph) == countNodes(bfsClone) :
            "BFS clone must preserve every reachable vertex.";

    // ---------------------------------------------------------------------
    // Mutation independence.
    // Modifying clone must never modify original.
    // ---------------------------------------------------------------------
    dfsClone.neighbors.clear();

    assert graph.neighbors.size() == 2 :
            "Original graph must remain unchanged after clone mutation.";

    // ---------------------------------------------------------------------
    // BFS / DFS structural equivalence.
    // ---------------------------------------------------------------------
    assert countNodes(dfsClone) <= countNodes(graph) :
            "Mutation only affects cloned graph.";

    Node freshClone = new Optimal().cloneGraph(graph);

    verifyDeepCopy(graph, freshClone);

    assert freshClone.neighbors.size() == graph.neighbors.size() :
            "Fresh clone should exactly match original.";

    System.out.println("All assertions passed.");
}
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/