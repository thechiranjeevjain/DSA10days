package org.chijai.day8.graph.session3;

import java.util.*;

/**
 * MinimumHeightTrees
 *
 * ============================================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================================
 *
 * Title:
 * Minimum Height Trees
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Graph
 * Tree
 * Breadth First Search (BFS)
 * Topological Peeling
 * Degree Counting
 * Centroid
 *
 * Problem:
 *
 * A tree is an undirected connected graph having exactly one simple path
 * between every pair of vertices.
 *
 * You are given:
 *
 *      n nodes numbered from 0 to n-1
 *
 * and
 *
 *      n-1 undirected edges.
 *
 * You may choose any node as the root.
 *
 * The height of the rooted tree is the maximum number of edges from the root
 * to any leaf.
 *
 * Return every root producing the minimum possible height.
 *
 * Those roots are called Minimum Height Trees (MHTs).
 *
 * Constraints
 *
 * 1 <= n <= 2 * 10^4
 * edges.length == n - 1
 * edges[i].length == 2
 * 0 <= ai, bi < n
 * The input is guaranteed to be a tree.
 *
 * Representative Example
 *
 * Input
 *
 * n = 6
 *
 * edges =
 *
 * [
 *   [3,0],
 *   [3,1],
 *   [3,2],
 *   [3,4],
 *   [5,4]
 * ]
 *
 * Output
 *
 * [3,4]
 *
 * Explanation
 *
 * Rooting at either 3 or 4 gives minimum possible height.
 *
 * LeetCode
 *
 * https://leetcode.com/problems/minimum-height-trees/
 *
 * ============================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern
 *
 * Multi-Source BFS Layer Peeling
 *
 * Also Known As
 *
 * Topological Leaf Removal
 *
 * Centroid Finding
 *
 * Onion Peeling
 *
 * Archetype
 *
 * Instead of searching outward from the center,
 * repeatedly delete everything known to be outside.
 *
 * Eventually only the optimal core survives.
 *
 * Core Invariant
 *
 * Every leaf can never become the centroid.
 *
 * Therefore every current leaf can safely be discarded simultaneously.
 *
 * Why It Works
 *
 * The longest path (diameter) determines the tree height.
 *
 * Every iteration removes one layer from BOTH ends of every diameter.
 *
 * Therefore every remaining node stays equally distant from every boundary.
 *
 * Eventually only the center(s) remain.
 *
 * Recognition Signals
 *
 * ✓ Undirected tree
 *
 * ✓ Root may be chosen arbitrarily
 *
 * ✓ Need minimum possible height
 *
 * ✓ Need tree center instead of traversal order
 *
 * ✓ Leaves obviously look like bad candidates
 *
 * When To Use
 *
 * • Find tree centers
 * • Find centroids
 * • Remove graph layer by layer
 * • Multi-source shrinking process
 *
 * When NOT To Use
 *
 * • General graphs containing cycles
 * • Weighted shortest-path problems
 * • DFS ordering problems
 * • Lowest Common Ancestor
 *
 * Comparison
 *
 * --------------------------------------------------------------------
 * Diameter Method
 * --------------------------------------------------------------------
 * Compute diameter then return middle node(s).
 *
 * Correct.
 *
 * Requires two BFS/DFS traversals plus path reconstruction.
 *
 * --------------------------------------------------------------------
 * Leaf Peeling (this solution)
 * --------------------------------------------------------------------
 * Never explicitly computes diameter.
 *
 * Directly discovers its center.
 *
 * Easier to reason about during interviews.
 *
 * O(n)
 *
 * ============================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * Mental Model
 *
 * Imagine peeling an onion.
 *
 * The outermost layer never belongs to the center.
 *
 * Remove every outer layer simultaneously.
 *
 * Continue until only the core remains.
 *
 * That core is exactly the centroid set.
 *
 * Another intuition:
 *
 * Suppose every leaf starts walking toward the center at equal speed.
 *
 * Nobody changes speed.
 *
 * Nobody waits.
 *
 * The final meeting location is exactly the answer.
 *
 * Why Leaves Can Never Be Answers Initially
 *
 * A leaf is already at one extreme of the tree.
 *
 * Choosing it as root stretches one branch to the entire opposite side.
 *
 * Hence its height is unnecessarily large.
 *
 * Removing it cannot eliminate any optimal solution.
 *
 * Primary Invariant
 *
 * Every node removed has already been proven incapable of being a centroid.
 *
 * Therefore deleting it never removes a valid answer.
 *
 * Layer Invariant
 *
 * Every iteration removes exactly one boundary layer from every branch.
 *
 * Remaining nodes preserve their relative centrality.
 *
 * Degree Invariant
 *
 * degree[node]
 *
 * always equals
 *
 * "number of neighbors not yet removed."
 *
 * Queue Invariant
 *
 * Queue always contains exactly the current leaves.
 *
 * Never internal nodes.
 *
 * Remaining Nodes Invariant
 *
 * remainingNodes
 *
 * equals
 *
 * total nodes still alive after previous peeling rounds.
 *
 * Allowed State Transition
 *
 * Internal node
 *
 *      degree >= 2
 *
 * loses neighbors
 *
 * becomes
 *
 * degree == 1
 *
 * therefore becomes a new leaf.
 *
 * Forbidden Transition
 *
 * Never enqueue a node before its degree becomes exactly one.
 *
 * Otherwise the same node may be processed too early.
 *
 * Variable Meanings
 *
 * degree[]
 *
 * Current live degree.
 *
 * graph
 *
 * Adjacency list.
 *
 * queue
 *
 * Current outer boundary.
 *
 * remainingNodes
 *
 * Number of surviving vertices.
 *
 * layerSize
 *
 * Number of leaves removed together.
 *
 * Why We Remove Entire Layers
 *
 * Removing one leaf at a time would incorrectly influence neighboring leaves
 * inside the same layer.
 *
 * All leaves represent the same distance from the center.
 *
 * Therefore every leaf of one level must disappear together.
 *
 * Termination
 *
 * Stop once
 *
 * remainingNodes <= 2
 *
 * because
 *
 * A tree has either
 *
 * exactly one centroid
 *
 * or
 *
 * exactly two adjacent centroids.
 *
 * Those are precisely the surviving nodes.
 *
 * Correctness Intuition
 *
 * Every iteration shortens every longest path by exactly two edges:
 *
 * one removed from each end.
 *
 * Therefore the midpoint(s) of every diameter never disappear.
 *
 * Eventually only those midpoint(s) survive.
 *
 * Since tree centers minimize the maximum distance to every leaf,
 * surviving nodes are exactly the Minimum Height Tree roots.
 *
 * Why Naive Solutions Fail
 *
 * Brute force:
 *
 * Try every node as root.
 *
 * Perform BFS/DFS.
 *
 * Compute height.
 *
 * Complexity
 *
 * O(n^2)
 *
 * which is unnecessary because neighboring roots reuse almost all information.
 *
 * This algorithm instead eliminates impossible candidates globally.
 *
 * ============================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================================
 *
 * Mistake 1
 *
 * Remove one leaf at a time.
 *
 * Looks reasonable because leaves are bad roots.
 *
 * Failure:
 *
 * Newly created leaves are processed too early.
 *
 * Layer synchronization is broken.
 *
 * Invariant Violated
 *
 * Entire boundary must disappear simultaneously.
 *
 * ---------------------------------------------------------------
 *
 * Mistake 2
 *
 * Stop after first peeling round.
 *
 * Counterexample
 *
 * Long chain.
 *
 * One peel still leaves many non-central nodes.
 *
 * ---------------------------------------------------------------
 *
 * Mistake 3
 *
 * Stop when queue becomes empty.
 *
 * Queue should never become empty before the answer.
 *
 * Correct stopping condition depends on remaining nodes,
 * not queue emptiness.
 *
 * ---------------------------------------------------------------
 *
 * Mistake 4
 *
 * Forget to decrement neighbor degree.
 *
 * New leaves never appear.
 *
 * BFS freezes forever.
 *
 * ---------------------------------------------------------------
 *
 * Mistake 5
 *
 * Enqueue whenever degree <= 1.
 *
 * Duplicate insertions become possible.
 *
 * Correct trigger:
 *
 * degree == 1
 *
 * exactly once.
 *
 * ============================================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================================
 *
 * Mechanical typing order:
 *
 * 1. Handle n == 1.
 *
 * 2. Build adjacency list.
 *
 * 3. Compute every degree.
 *
 * 4. Push every degree==1 node into queue.
 *
 * 5. remainingNodes = n.
 *
 * 6. While remainingNodes > 2
 *
 *      layerSize = queue.size()
 *
 *      remainingNodes -= layerSize
 *
 *      Process exactly layerSize leaves
 *
 *          pop leaf
 *
 *          visit neighbors
 *
 *              degree--
 *
 *              if degree==1
 *                  enqueue
 *
 * 7. Queue now stores centroids.
 *
 * 8. Return queue contents.
 *
 * ============================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================================
 *
 * build graph
 *
 * compute degree
 *
 * enqueue all leaves
 *
 * while remaining > 2
 *
 *     remove one layer
 *
 *     update degree
 *
 *     enqueue new leaves
 *
 * return remaining nodes
 *
 * ============================================================================
 * 6. SOLUTION CLASSES
 * ============================================================================
 *
 * ---------------------------------------------------------------------------
 * Brute Force
 * ---------------------------------------------------------------------------
 *
 * Idea
 *
 * Root the tree at every vertex.
 *
 * Compute tree height using BFS/DFS.
 *
 * Smallest height wins.
 *
 * Invariant
 *
 * Every traversal computes one root's exact height.
 *
 * Limitation
 *
 * Massive repeated traversals.
 *
 * Complexity
 *
 * Time  : O(n²)
 *
 * Space : O(n)
 *
 * Interview Usefulness
 *
 * Good starting discussion.
 *
 * Rarely accepted as final answer.
 *
 * ---------------------------------------------------------------------------
 * Improved
 * ---------------------------------------------------------------------------
 *
 * Idea
 *
 * Compute tree diameter then return midpoint(s).
 *
 * Invariant
 *
 * Centers always lie in middle of diameter.
 *
 * Improvement
 *
 * O(n)
 *
 * Complexity
 *
 * Time  : O(n)
 *
 * Space : O(n)
 *
 * Interview Usefulness
 *
 * Correct alternative.
 *
 * Slightly harder to reconstruct because diameter path must be recovered.
 *
 * ---------------------------------------------------------------------------
 * Optimal (Interview Preferred)
 * ---------------------------------------------------------------------------
 *
 * Idea
 *
 * Peel leaves level by level until only centroid(s) survive.
 *
 * Invariant
 *
 * The queue always contains exactly the current outer boundary.
 *
 * Remaining nodes always contain every possible centroid.
 */
public class MinHTree {

    static class OptimalSolution {

        public List<Integer> findMinHeightTrees(int n, int[][] edges) {

            List<Integer> answer = new ArrayList<>();

            // Invariant: single node is already the unique centroid.
            if (n == 1) {
                answer.add(0);
                return answer;
            }

            List<List<Integer>> graph = new ArrayList<>(n);

            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            int[] degree = new int[n];

            for (int[] edge : edges) {

                int u = edge[0];
                int v = edge[1];

                graph.get(u).add(v);
                graph.get(v).add(u);

                degree[u]++;
                degree[v]++;
            }

            Queue<Integer> leaves = new ArrayDeque<>();

            for (int node = 0; node < n; node++) {

                // Invariant: current queue stores only boundary nodes.
                if (degree[node] == 1) {
                    leaves.offer(node);
                }
            }

            int remainingNodes = n;

            while (remainingNodes > 2) {

                int currentLayerSize = leaves.size();

                remainingNodes -= currentLayerSize;

                while (currentLayerSize-- > 0) {

                    int leaf = leaves.poll();

                    for (int neighbor : graph.get(leaf)) {

                        // Invariant: degree counts only surviving neighbors.
                        degree[neighbor]--;

                        // Node has just become the next boundary.
                        if (degree[neighbor] == 1) {
                            leaves.offer(neighbor);
                        }
                    }
                }
            }

            // Invariant: only centroid(s) remain alive.
            while (!leaves.isEmpty()) {
                answer.add(leaves.poll());
            }

            return answer;
        }
    }

/**
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * How would I explain this solution verbally?
 *
 * "Instead of asking which node should become the root,
 * I ask which nodes definitely cannot become the root.
 *
 * Every leaf is already on the outer boundary of the tree.
 * Rooting at a leaf stretches the longest path almost across the
 * entire tree, so a leaf cannot be an optimal root unless the tree
 * has only one node.
 *
 * Therefore I remove every leaf simultaneously.
 *
 * Once removed, some internal nodes lose neighbors and themselves
 * become the next layer of leaves.
 *
 * This repeats exactly like peeling an onion.
 *
 * The process stops when only one or two nodes survive.
 *
 * Those surviving nodes are the centroids.
 *
 * Every tree has either one centroid or two adjacent centroids.
 *
 * Those are exactly the roots producing minimum height."
 *
 * -------------------------------------------------------------------------
 *
 * Why is the discard rule correct?
 *
 * Every removed node lies strictly farther from at least one diameter
 * midpoint than the remaining nodes.
 *
 * Removing it cannot eliminate the optimal center.
 *
 * -------------------------------------------------------------------------
 *
 * Why does termination guarantee correctness?
 *
 * Each peeling round removes one layer from every longest path.
 *
 * Therefore the midpoint(s) of every diameter survive the longest.
 *
 * The final survivors are exactly those midpoint(s).
 *
 * -------------------------------------------------------------------------
 *
 * Is this in-place?
 *
 * No.
 *
 * We maintain
 *
 * • adjacency list
 * • degree array
 * • queue
 *
 * Total extra memory remains O(n).
 *
 * -------------------------------------------------------------------------
 *
 * Is this streaming?
 *
 * No.
 *
 * We need the complete graph before peeling can begin because
 * degree information for every node must be known.
 *
 * -------------------------------------------------------------------------
 *
 * When should this pattern NOT be used?
 *
 * • Graph contains cycles.
 * • Graph is disconnected.
 * • Weighted edges matter.
 * • Need shortest paths instead of centers.
 * • Need arbitrary graph topological properties.
 *
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 *
 * "Choose root minimizing tree height."
 *
 * Pattern
 *
 * Multi-source BFS leaf peeling.
 *
 * Search Target
 *
 * Tree centroid(s).
 *
 * Invariant
 *
 * Queue always contains the current outer boundary.
 *
 * Discard Rule
 *
 * Current leaves can never remain centroids.
 *
 * Transition
 *
 * Remove leaves.
 *
 * Decrease neighbor degree.
 *
 * Newly created degree==1 nodes become next leaves.
 *
 * Termination
 *
 * remainingNodes <= 2
 *
 * Answer
 *
 * Remaining nodes.
 *
 * Common Trap
 *
 * Removing leaves one-by-one instead of layer-by-layer.
 *
 * Edge Cases
 *
 * • n == 1
 * • Two nodes
 * • Star graph
 * • Long chain
 *
 * One-Liner
 *
 * "Peel the tree until only its center survives."
 *
 * Re-Derivation Cue
 *
 * Ask:
 *
 * "Which nodes are obviously bad roots?"
 *
 * Answer:
 *
 * Leaves.
 *
 * Remove them repeatedly.
 *
 * =========================================================================
 * 🔄 VARIATIONS & TWEAKS
 * =========================================================================
 *
 * -------------------------------------------------------------------------
 * Variation 1
 * Find One Centroid
 * -------------------------------------------------------------------------
 *
 * If only one centroid is desired and two remain,
 * either centroid minimizes the height.
 *
 * Invariant remains unchanged.
 *
 * -------------------------------------------------------------------------
 * Variation 2
 * Diameter Approach
 * -------------------------------------------------------------------------
 *
 * Compute
 *
 * BFS/DFS
 * ->
 * farthest node
 * ->
 * second BFS
 * ->
 * diameter path
 * ->
 * middle node(s)
 *
 * Same answer.
 *
 * Different reasoning.
 *
 * -------------------------------------------------------------------------
 * Variation 3
 * Weighted Tree
 * -------------------------------------------------------------------------
 *
 * This pattern breaks.
 *
 * Degree tells nothing about weighted distance.
 *
 * Centers depend on accumulated edge weights.
 *
 * -------------------------------------------------------------------------
 * Variation 4
 * General Graph
 * -------------------------------------------------------------------------
 *
 * This pattern breaks.
 *
 * Cycles prevent clean layer removal.
 *
 * Degree-one vertices may never even exist.
 *
 * -------------------------------------------------------------------------
 * Variation 5
 * Dynamic Tree
 * -------------------------------------------------------------------------
 *
 * If edges are continuously inserted or removed,
 * rebuilding is usually simpler.
 *
 * The peeling process assumes a static tree.
 *
 * -------------------------------------------------------------------------
 * Variation 6
 * Tree Diameter
 * -------------------------------------------------------------------------
 *
 * Both approaches rely on the same geometric fact:
 *
 * the center lies on every diameter.
 *
 * Diameter explicitly computes the longest path.
 *
 * Peeling implicitly exposes its midpoint.
 *
 * =========================================================================
 * 🧠 MASTERY CHECKLIST
 * =========================================================================
 *
 * □ Do I know the invariant?
 *
 * Yes.
 *
 * Queue contains exactly the current leaves.
 *
 * -------------------------------------------------------------------------
 *
 * □ What am I searching for?
 *
 * Tree centroid(s).
 *
 * -------------------------------------------------------------------------
 *
 * □ What is discarded?
 *
 * Every current leaf.
 *
 * -------------------------------------------------------------------------
 *
 * □ Why is discarding safe?
 *
 * Leaves cannot minimize maximum distance to all other nodes.
 *
 * -------------------------------------------------------------------------
 *
 * □ How do new candidates appear?
 *
 * Neighbor degree decreases to exactly one.
 *
 * -------------------------------------------------------------------------
 *
 * □ Why stop at two nodes?
 *
 * Every tree has at most two centroids.
 *
 * -------------------------------------------------------------------------
 *
 * □ Why does brute force lose?
 *
 * Recomputes almost identical traversals from every root.
 *
 * -------------------------------------------------------------------------
 *
 * □ What should I debug first?
 *
 * 1. Degree initialization.
 *
 * 2. Queue initialization.
 *
 * 3. Layer size captured before processing.
 *
 * 4. Degree decrement.
 *
 * 5. Trigger uses degree == 1 exactly.
 *
 * 6. Remaining node count.
 *
 * -------------------------------------------------------------------------
 *
 * □ Can I derive the implementation from memory?
 *
 * Build graph
 * →
 * Count degrees
 * →
 * Queue all leaves
 * →
 * Peel one layer
 * →
 * Update degrees
 * →
 * Stop at <=2 nodes.
 *
 * -------------------------------------------------------------------------
 *
 * □ Pattern Boundary
 *
 * Works because a tree has
 *
 * • unique paths
 * • no cycles
 * • exactly n-1 edges
 *
 * Breaks once these guarantees disappear.
 */


/**
 * =========================================================================
 * ⚫ PATTERN MAPPING
 * =========================================================================
 *
 * Similar Problems
 *
 * ------------------------------------------------------------
 * Course Schedule
 * ------------------------------------------------------------
 *
 * Pattern:
 * Topological BFS
 *
 * Similarity:
 * Both repeatedly process degree-based frontier nodes.
 *
 * Difference:
 * Course Schedule removes indegree-zero nodes in a DAG.
 * Here we remove degree-one nodes in an undirected tree.
 *
 * ------------------------------------------------------------
 * Tree Diameter
 * ------------------------------------------------------------
 *
 * Pattern:
 * Two BFS / DFS traversals.
 *
 * Similarity:
 * Both ultimately locate the center.
 *
 * Difference:
 * Diameter explicitly computes the longest path.
 * Leaf peeling implicitly converges to its midpoint.
 *
 * ------------------------------------------------------------
 * Topological Sorting
 * ------------------------------------------------------------
 *
 * Pattern:
 * Kahn's Algorithm.
 *
 * Similarity:
 * Queue + degree updates.
 *
 * Difference:
 * Directed graph:
 * indegree reaches zero.
 *
 * Undirected tree:
 * degree reaches one.
 *
 * ------------------------------------------------------------
 * Graph Center Problems
 * ------------------------------------------------------------
 *
 * Pattern:
 * Progressive boundary elimination.
 *
 * Similarity:
 * Remove impossible candidates until optimal core survives.
 *
 * =========================================================================
 * 🔬 CORRECTNESS SKETCH
 * =========================================================================
 *
 * Claim 1
 *
 * Every removed node is not a centroid.
 *
 * Proof Sketch
 *
 * A leaf lies at an extreme end of at least one simple path.
 *
 * Moving one step toward the interior cannot increase the maximum
 * distance to any remaining node and usually decreases it.
 *
 * Therefore the leaf cannot be strictly better than its only neighbor.
 *
 * ------------------------------------------------------------
 *
 * Claim 2
 *
 * Removing every leaf together preserves every centroid.
 *
 * Proof Sketch
 *
 * Every centroid lies strictly inside the tree unless only one or
 * two nodes remain.
 *
 * Therefore no centroid belongs to the removable boundary layer.
 *
 * ------------------------------------------------------------
 *
 * Claim 3
 *
 * The algorithm terminates.
 *
 * Proof Sketch
 *
 * Every iteration removes at least one node.
 *
 * The number of remaining nodes decreases monotonically.
 *
 * Hence termination is guaranteed.
 *
 * ------------------------------------------------------------
 *
 * Claim 4
 *
 * Final survivors are exactly the MHT roots.
 *
 * Proof Sketch
 *
 * Every iteration strips one layer from every diameter.
 *
 * Eventually only the midpoint(s) remain.
 *
 * Midpoints minimize the maximum distance to every leaf.
 *
 * Hence they are precisely the minimum-height roots.
 *
 * =========================================================================
 * 🐞 DEBUGGING PLAYBOOK
 * =========================================================================
 *
 * Symptom
 *
 * Infinite loop.
 *
 * Check
 *
 * Did remainingNodes decrease?
 *
 * ------------------------------------------------------------
 *
 * Symptom
 *
 * Queue becomes empty too early.
 *
 * Check
 *
 * Degree initialization.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 *
 * Duplicate answers.
 *
 * Check
 *
 * A node must enter the queue only when degree becomes exactly one.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 *
 * Missing centroid.
 *
 * Check
 *
 * Entire layer must be processed together.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 *
 * Wrong answer for n = 1.
 *
 * Check
 *
 * Early return before graph construction.
 *
 * =========================================================================
 * 📈 COMPLEXITY ANALYSIS
 * =========================================================================
 *
 * Let
 *
 * V = n
 *
 * E = n - 1
 *
 * Graph Construction
 *
 * O(V)
 *
 * ------------------------------------------------------------
 *
 * Degree Computation
 *
 * O(E)
 *
 * ------------------------------------------------------------
 *
 * BFS Peeling
 *
 * Every node enters the queue once.
 *
 * Every edge is examined at most twice.
 *
 * Therefore
 *
 * O(V + E)
 *
 * Since
 *
 * E = V - 1
 *
 * Final Complexity
 *
 * Time
 *
 * O(n)
 *
 * Space
 *
 * O(n)
 *
 * =========================================================================
 * 🧩 EDGE CASE CATALOG
 * =========================================================================
 *
 * Case 1
 *
 * Single Node
 *
 * n = 1
 *
 * Answer
 *
 * [0]
 *
 * ------------------------------------------------------------
 *
 * Case 2
 *
 * Two Nodes
 *
 * 0 ----- 1
 *
 * Both are centroids.
 *
 * Answer
 *
 * [0,1]
 *
 * ------------------------------------------------------------
 *
 * Case 3
 *
 * Star
 *
 *      1
 *      |
 * 2 -- 0 -- 3
 *      |
 *      4
 *
 * One peeling round removes every leaf.
 *
 * Remaining center
 *
 * [0]
 *
 * ------------------------------------------------------------
 *
 * Case 4
 *
 * Even Length Chain
 *
 * 0-1-2-3
 *
 * Answer
 *
 * [1,2]
 *
 * ------------------------------------------------------------
 *
 * Case 5
 *
 * Odd Length Chain
 *
 * 0-1-2-3-4
 *
 * Answer
 *
 * [2]
 *
 * ------------------------------------------------------------
 *
 * Case 6
 *
 * Highly Unbalanced Tree
 *
 * Peeling naturally progresses from every branch simultaneously.
 *
 * =========================================================================
 * 💡 IMPLEMENTATION RECONSTRUCTION DRILL
 * =========================================================================
 *
 * If the code is forgotten during an interview,
 * reconstruct it mechanically:
 *
 * Step 1
 *
 * Handle n == 1.
 *
 * Step 2
 *
 * Build adjacency list.
 *
 * Step 3
 *
 * Compute degree of every vertex.
 *
 * Step 4
 *
 * Push every degree==1 node into queue.
 *
 * Step 5
 *
 * remainingNodes = n.
 *
 * Step 6
 *
 * Repeat while remainingNodes > 2.
 *
 * Step 7
 *
 * Capture current queue size.
 *
 * Step 8
 *
 * Remove that many leaves.
 *
 * Step 9
 *
 * Decrement neighbor degrees.
 *
 * Step 10
 *
 * When degree becomes exactly one,
 * enqueue that neighbor.
 *
 * Step 11
 *
 * Queue now contains every centroid.
 *
 * Return queue contents.
 */

public static void main(String[] args) {

    OptimalSolution solver = new OptimalSolution();

    // ---------------------------------------------------------------------
    // Happy Path
    // Representative example from the problem statement.
    // ---------------------------------------------------------------------
    {
        int n = 6;
        int[][] edges = {
                {3, 0},
                {3, 1},
                {3, 2},
                {3, 4},
                {5, 4}
        };

        List<Integer> actual = solver.findMinHeightTrees(n, edges);
        Collections.sort(actual);

        List<Integer> expected = Arrays.asList(3, 4);

        assert actual.equals(expected)
                : "Representative example should return the two centroids.";
    }

    // ---------------------------------------------------------------------
    // Edge Case
    // Single node tree.
    // ---------------------------------------------------------------------
    {
        int n = 1;
        int[][] edges = {};

        List<Integer> actual = solver.findMinHeightTrees(n, edges);

        List<Integer> expected = Collections.singletonList(0);

        assert actual.equals(expected)
                : "Single node is its own centroid.";
    }

    // ---------------------------------------------------------------------
    // Boundary Condition
    // Two-node tree has two valid roots.
    // ---------------------------------------------------------------------
    {
        int n = 2;
        int[][] edges = {
                {0, 1}
        };

        List<Integer> actual = solver.findMinHeightTrees(n, edges);
        Collections.sort(actual);

        List<Integer> expected = Arrays.asList(0, 1);

        assert actual.equals(expected)
                : "Both endpoints are centroids.";
    }

    // ---------------------------------------------------------------------
    // Interview Trap
    // Star graph.
    // All leaves disappear in one round.
    // ---------------------------------------------------------------------
    {
        int n = 5;
        int[][] edges = {
                {0, 1},
                {0, 2},
                {0, 3},
                {0, 4}
        };

        List<Integer> actual = solver.findMinHeightTrees(n, edges);

        List<Integer> expected = Collections.singletonList(0);

        assert actual.equals(expected)
                : "Center of star must survive.";
    }

    // ---------------------------------------------------------------------
    // Even Diameter
    // Two centroids.
    // ---------------------------------------------------------------------
    {
        int n = 4;
        int[][] edges = {
                {0, 1},
                {1, 2},
                {2, 3}
        };

        List<Integer> actual = solver.findMinHeightTrees(n, edges);
        Collections.sort(actual);

        List<Integer> expected = Arrays.asList(1, 2);

        assert actual.equals(expected)
                : "Even-length chain has two centroids.";
    }

    // ---------------------------------------------------------------------
    // Odd Diameter
    // Single centroid.
    // ---------------------------------------------------------------------
    {
        int n = 5;
        int[][] edges = {
                {0, 1},
                {1, 2},
                {2, 3},
                {3, 4}
        };

        List<Integer> actual = solver.findMinHeightTrees(n, edges);

        List<Integer> expected = Collections.singletonList(2);

        assert actual.equals(expected)
                : "Odd-length chain has one centroid.";
    }

    // ---------------------------------------------------------------------
    // Unbalanced Tree
    // Ensures peeling works across asymmetric branches.
    // ---------------------------------------------------------------------
    {
        int n = 7;
        int[][] edges = {
                {0, 1},
                {1, 2},
                {2, 3},
                {2, 4},
                {4, 5},
                {5, 6}
        };

        List<Integer> actual = solver.findMinHeightTrees(n, edges);

        List<Integer> expected = Collections.singletonList(2);

        assert actual.equals(expected)
                : "Asymmetric tree should still converge to its centroid.";
    }

    // ---------------------------------------------------------------------
    // Small Balanced Binary Tree
    // Root is the unique centroid.
    // ---------------------------------------------------------------------
    {
        int n = 7;
        int[][] edges = {
                {0, 1},
                {0, 2},
                {1, 3},
                {1, 4},
                {2, 5},
                {2, 6}
        };

        List<Integer> actual = solver.findMinHeightTrees(n, edges);

        List<Integer> expected = Collections.singletonList(0);

        assert actual.equals(expected)
                : "Balanced tree center should remain after peeling.";
    }

    System.out.println("All assertions passed.");
}
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/
