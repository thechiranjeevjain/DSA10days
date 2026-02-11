package org.chijai.day6.session2;

import java.util.*;

/*
============================================================
🔥 BURN THE BINARY TREE STARTING FROM TARGET NODE
Last Updated : 12 Jul, 2025
============================================================

📘 PRIMARY PROBLEM — FULL OFFICIAL STATEMENT
------------------------------------------------------------

Given a binary tree and target node. By giving the fire to the target node
and fire starts to spread in a complete tree. The task is to print the
sequence of the burning nodes of a binary tree.

Rules for burning the nodes :

Fire will spread constantly to the connected nodes only.
Every node takes the same time to burn.
A node burns only once.

Examples:

Input :
                       12
                     /     \
                   13       10
                          /     \
                       14       15
                      /   \     /  \
                     21   24   22   23
target node = 14

Output :
14
21, 24, 10
15, 12
22, 23, 13

Explanation : First node 14 burns then it gives fire to its
neighbors (21, 24, 10) and so on. This process continues until
the whole tree burns.


Input :
                       12
                     /     \
                  19        82
                 /        /     \
               41       15       95
                 \     /         /  \
                  2   21        7   16
target node = 41

Output :
41
2, 19
12
82
15, 95
21, 7, 16


Difficulty: Medium
Tags: Binary Tree, BFS, DFS, Graph Traversal

============================================================
*/

public class BurnBinaryTree {

    /*
    ============================================================
    🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
    ============================================================

    🔵 Pattern Name:
        Multi-Source Graph Expansion on Tree via Parent Tracking

    🔵 Problem Archetype:
        “Spread / Infection / Distance K / Fire / Time-to-burn” problems

    🟢 Core Invariant (MANDATORY):
        At time T, all nodes at graph-distance T from the target are burning.

    🟡 Why this invariant works:
        Because each edge represents unit time, burning is exactly
        level-order traversal from the target node in the undirected tree graph.

    🔵 When this pattern applies:
        - Infection spread
        - Nodes at distance K
        - Time to infect tree
        - Burning tree
        - Tree converted to undirected graph problems

    🔵 Pattern Recognition Signals:
        - Fire spreads to neighbors
        - Time units mentioned
        - Each node burns once
        - Spread only to connected nodes

    🔵 Difference from similar patterns:
        NOT a simple subtree problem.
        NOT pure DFS.
        Requires treating tree as UNDIRECTED graph.
        Requires parent linkage.
    */

    /*
    ============================================================
    🟢 MENTAL MODEL & INVARIANTS (CANONICAL SOURCE OF TRUTH)
    ============================================================

    🔵 Mental Model:
        Convert tree into an undirected graph.
        Perform BFS starting from target.
        Each BFS level = one time unit.

    🟢 Invariants:

    1. A node burns exactly once.
    2. Nodes at BFS level T are exactly distance T from target.
    3. Fire spreads to left, right, and parent.
    4. Visited set prevents re-burning.
    5. When queue is empty, entire reachable tree burned.

    🔵 State Meaning:
        queue = frontier of burning nodes at current time
        visited = already burned nodes
        parentMap = allows upward traversal

    🟢 Allowed Moves:
        - Move to left child
        - Move to right child
        - Move to parent

    🔴 Forbidden Moves:
        - Revisiting burned nodes
        - Skipping parent linkage
        - DFS without level tracking

    🟡 Termination:
        When BFS queue becomes empty.

    🔴 Why alternatives fail:
        Pure DFS cannot track time-level grouping.
        Without parent pointer, upward spread impossible.
    */

    /*
    ============================================================
    🔴 WHY NAIVE SOLUTION FAILS (FORENSIC ANALYSIS)
    ============================================================

    🔴 Wrong Approach 1:
        Only burn subtree of target.
    ❌ Violates invariant: ignores parent direction.

    Minimal Counterexample:
            1
           /
          2
         /
        3
    target = 3

    Correct burn order:
        3
        2
        1

    Subtree-only approach misses node 1.

    🔴 Wrong Approach 2:
        DFS and print when visiting.
    ❌ Violates level invariant.
        Time grouping lost.

    🔴 Interview Trap:
        “Tree problem” → candidate forgets parent edge.
    */

    /*
    ============================================================
    PRIMARY PROBLEM — SOLUTION CLASSES
    ============================================================
    */

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    /*
    ------------------------------------------------------------
    🟠 BRUTE FORCE
    ------------------------------------------------------------

    Core Idea:
        For every time unit, scan entire tree and print nodes at distance T.

    Invariant Enforced:
        Distance-from-target grouping.

    Limitation:
        Repeated traversal → O(N^2)

    Time Complexity: O(N^2)
    Space: O(N)
    Interview Preference: ❌ No
    */

    static class BruteForce {

        public List<List<Integer>> burn(TreeNode root, int target) {
            List<List<Integer>> result = new ArrayList<>();

            Map<TreeNode, TreeNode> parent = new HashMap<>();
            TreeNode targetNode = mapParents(root, null, parent, target);

            int height = height(root);
            for (int d = 0; d <= height; d++) {
                List<Integer> level = new ArrayList<>();
                collectAtDistance(targetNode, parent, d, new HashSet<>(), level);
                if (!level.isEmpty())
                    result.add(level);
            }
            return result;
        }

        private TreeNode mapParents(TreeNode node, TreeNode par,
                                    Map<TreeNode, TreeNode> parent, int target) {
            if (node == null) return null;
            parent.put(node, par);
            if (node.val == target) return node;

            TreeNode left = mapParents(node.left, node, parent, target);
            if (left != null) return left;

            return mapParents(node.right, node, parent, target);
        }

        private void collectAtDistance(TreeNode node,
                                       Map<TreeNode, TreeNode> parent,
                                       int dist,
                                       Set<TreeNode> visited,
                                       List<Integer> level) {
            if (node == null || visited.contains(node)) return;

            visited.add(node);

            if (dist == 0) {
                level.add(node.val);
                return;
            }

            collectAtDistance(node.left, parent, dist - 1, visited, level);
            collectAtDistance(node.right, parent, dist - 1, visited, level);
            collectAtDistance(parent.get(node), parent, dist - 1, visited, level);
        }

        private int height(TreeNode node) {
            if (node == null) return -1;
            return 1 + Math.max(height(node.left), height(node.right));
        }
    }

    /*
    ------------------------------------------------------------
    🟡 IMPROVED SOLUTION
    ------------------------------------------------------------

    Core Idea:
        Build parent map once.
        Perform DFS with level tracking.

    Limitation:
        DFS complicates time grouping.
        BFS is more natural.

    Time Complexity: O(N)
    Space: O(N)
    Interview Preference: ⚠️ Acceptable
    */

    static class Improved {

        public List<List<Integer>> burn(TreeNode root, int target) {
            Map<TreeNode, TreeNode> parent = new HashMap<>();
            TreeNode targetNode = mapParents(root, null, parent, target);

            List<List<Integer>> result = new ArrayList<>();
            Set<TreeNode> visited = new HashSet<>();

            dfs(targetNode, parent, visited, 0, result);
            return result;
        }

        private void dfs(TreeNode node,
                         Map<TreeNode, TreeNode> parent,
                         Set<TreeNode> visited,
                         int time,
                         List<List<Integer>> result) {

            if (node == null || visited.contains(node)) return;

            visited.add(node);

            if (result.size() == time)
                result.add(new ArrayList<>());

            result.get(time).add(node.val);

            dfs(node.left, parent, visited, time + 1, result);
            dfs(node.right, parent, visited, time + 1, result);
            dfs(parent.get(node), parent, visited, time + 1, result);
        }

        private TreeNode mapParents(TreeNode node, TreeNode par,
                                    Map<TreeNode, TreeNode> parent, int target) {
            if (node == null) return null;
            parent.put(node, par);
            if (node.val == target) return node;

            TreeNode left = mapParents(node.left, node, parent, target);
            if (left != null) return left;

            return mapParents(node.right, node, parent, target);
        }
    }

    /*
    ------------------------------------------------------------
    🟢 OPTIMAL (INTERVIEW-PREFERRED)
    ------------------------------------------------------------

    Core Idea:
        Convert tree to undirected graph via parent map.
        Perform BFS from target.

    🟢 Enforces core invariant directly:
        BFS level == time unit.

    Time Complexity: O(N)
    Space Complexity: O(N)

    Interview Preference: ⭐ BEST
    */

    static class Optimal {

        public List<List<Integer>> burn(TreeNode root, int target) {

            // Step 1: Build full parent map
            Map<TreeNode, TreeNode> parentMap = new HashMap<>();
            buildParentMap(root, null, parentMap);

            // Step 2: Find target node
            TreeNode targetNode = findNode(root, target);
            if (targetNode == null)
                return new ArrayList<>();  // Defensive safety

            // Step 3: BFS from target
            return bfsBurn(targetNode, parentMap);
        }

        // 🔵 Build parent mapping for entire tree
        private void buildParentMap(TreeNode node,
                                    TreeNode parent,
                                    Map<TreeNode, TreeNode> parentMap) {

            if (node == null) return;

            parentMap.put(node, parent);

            buildParentMap(node.left, node, parentMap);
            buildParentMap(node.right, node, parentMap);
        }

        // 🔵 Find node by value (separate concern)
        private TreeNode findNode(TreeNode node, int target) {

            if (node == null) return null;

            if (node.val == target)
                return node;

            TreeNode left = findNode(node.left, target);
            if (left != null) return left;

            return findNode(node.right, target);
        }

        // 🔵 BFS burning logic (pure expansion logic)
        private List<List<Integer>> bfsBurn(TreeNode start,
                                            Map<TreeNode, TreeNode> parentMap) {

            List<List<Integer>> result = new ArrayList<>();
            Queue<TreeNode> queue = new LinkedList<>();
            Set<TreeNode> visited = new HashSet<>();

            queue.offer(start);
            visited.add(start);

            while (!queue.isEmpty()) {

                int size = queue.size();
                List<Integer> level = new ArrayList<>();

                for (int i = 0; i < size; i++) {

                    TreeNode curr = queue.poll();
                    level.add(curr.val);

                    // Explore neighbors
                    for (TreeNode neighbor :
                            Arrays.asList(curr.left,
                                    curr.right,
                                    parentMap.get(curr))) {

                        if (neighbor != null && !visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.offer(neighbor);
                        }
                    }
                }

                result.add(level);
            }

            return result;
        }
    }


    /*
    ============================================================
    🟣 INTERVIEW ARTICULATION (NO CODE)
    ============================================================

    🟢 Invariant:
        At time T, nodes at distance T from target burn.

    🟡 Discard Logic:
        Use visited set to avoid revisiting.

    🟢 Why correctness guaranteed:
        BFS guarantees shortest distance layering.

    🔴 What breaks if changed:
        If DFS without level tracking → time grouping fails.
        If no parent → upward spread fails.

    🟣 When NOT to use:
        When problem is subtree-only.
    */

    /*
    ============================================================
    🔄 VARIATIONS & TWEAKS
    ============================================================

    🟢 Invariant-Preserving Changes:
        - Count time only (no printing)
        - Stop at distance K
        - Return max time only

    ⚫ Pattern Mapping:
        - All Nodes Distance K
        - Time to Infect Tree
        - Graph infection problems

    🔴 Pattern-Break Signals:
        - Weighted edges
        - Directed graph only
        - Variable burn times
    */

    /*
    ============================================================
    ⚫ REINFORCEMENT PROBLEMS
    (FULL INVARIANT SUB-CHAPTERS)
    ============================================================

    All problems below use SAME CORE INVARIANT:

    🟢 Invariant:
        Nodes discovered at BFS level T are exactly distance T
        from the source in an unweighted graph.

    ============================================================
    PROBLEM 1 — ALL NODES DISTANCE K IN BINARY TREE
    (LeetCode 863)
    ============================================================

    📘 OFFICIAL PROBLEM STATEMENT

    Given the root of a binary tree, the value of a target node target,
    and an integer k, return an array of the values of all nodes that
    have a distance k from the target node.

    You can return the answer in any order.

    Example:

    Input:
            3
           / \
          5   1
         / \ / \
        6  2 0  8
          / \
         7   4
    target = 5, k = 2

    Output: [7,4,1]

    Difficulty: Medium
    Tags: Tree, BFS, DFS

    ------------------------------------------------------------
    🟢 Invariant Mapping:
        BFS level K from target gives answer directly.

    🔴 Naive Failure:
        Searching only subtree misses upward nodes.

    ------------------------------------------------------------
    */

    static class DistanceK {

        public List<Integer> distanceK(TreeNode root,
                                       int target,
                                       int k) {

            Map<TreeNode, TreeNode> parentMap = new HashMap<>();
            buildParentMap(root, null, parentMap);

            TreeNode targetNode = findNode(root, target);
            if (targetNode == null) return new ArrayList<>();

            return bfsDistanceK(targetNode, parentMap, k);
        }

        private void buildParentMap(TreeNode node,
                                    TreeNode parent,
                                    Map<TreeNode, TreeNode> parentMap) {

            if (node == null) return;

            parentMap.put(node, parent);

            buildParentMap(node.left, node, parentMap);
            buildParentMap(node.right, node, parentMap);
        }

        private TreeNode findNode(TreeNode node, int target) {

            if (node == null) return null;

            if (node.val == target)
                return node;

            TreeNode left = findNode(node.left, target);
            if (left != null) return left;

            return findNode(node.right, target);
        }

        private List<Integer> bfsDistanceK(TreeNode start,
                                           Map<TreeNode, TreeNode> parentMap,
                                           int k) {

            Queue<TreeNode> queue = new LinkedList<>();
            Set<TreeNode> visited = new HashSet<>();

            queue.offer(start);
            visited.add(start);

            int level = 0;

            while (!queue.isEmpty()) {

                int size = queue.size();

                if (level == k) {
                    List<Integer> ans = new ArrayList<>();
                    for (TreeNode node : queue)
                        ans.add(node.val);
                    return ans;
                }

                for (int i = 0; i < size; i++) {

                    TreeNode curr = queue.poll();

                    for (TreeNode neighbor :
                            Arrays.asList(curr.left,
                                    curr.right,
                                    parentMap.get(curr))) {

                        if (neighbor != null && !visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.offer(neighbor);
                        }
                    }
                }

                level++;
            }

            return new ArrayList<>();
        }
    }


    /*
    ============================================================
    PROBLEM 2 — TIME NEEDED TO INFECT BINARY TREE
    (LeetCode 2385)
    ============================================================

    📘 OFFICIAL PROBLEM STATEMENT

    You are given the root of a binary tree with unique values,
    and an integer start.

    At minute 0, an infection starts from the node with value start.
    Each minute, a node becomes infected if:
        - It is not infected yet
        - It is adjacent to an infected node

    Return the number of minutes needed for the entire tree to be infected.

    Example:

    Input:
            1
           / \
          5   3
               \
                4
                 \
                  9
    start = 3

    Output: 4

    Difficulty: Medium
    Tags: BFS, Tree

    ------------------------------------------------------------
    🟢 Invariant Mapping:
        Final BFS level count - 1 = total minutes.

    🔴 Naive Failure:
        Counting height from start subtree misses parent direction.

    ------------------------------------------------------------
    */

    static class TimeToInfect {

        public int amountOfTime(TreeNode root, int start) {

            // Step 1: Build complete parent map
            Map<TreeNode, TreeNode> parentMap = new HashMap<>();
            buildParentMap(root, null, parentMap);

            // Step 2: Locate start node
            TreeNode startNode = findNode(root, start);
            if (startNode == null) return 0;

            // Step 3: BFS infection
            return bfsInfect(startNode, parentMap);
        }

        private void buildParentMap(TreeNode node,
                                    TreeNode parent,
                                    Map<TreeNode, TreeNode> parentMap) {

            if (node == null) return;

            parentMap.put(node, parent);

            buildParentMap(node.left, node, parentMap);
            buildParentMap(node.right, node, parentMap);
        }

        private TreeNode findNode(TreeNode node, int target) {

            if (node == null) return null;

            if (node.val == target)
                return node;

            TreeNode left = findNode(node.left, target);
            if (left != null) return left;

            return findNode(node.right, target);
        }

        private int bfsInfect(TreeNode start,
                              Map<TreeNode, TreeNode> parentMap) {

            Queue<TreeNode> queue = new LinkedList<>();
            Set<TreeNode> visited = new HashSet<>();

            queue.offer(start);
            visited.add(start);

            int time = -1;

            while (!queue.isEmpty()) {

                int size = queue.size();
                time++;

                for (int i = 0; i < size; i++) {

                    TreeNode curr = queue.poll();

                    for (TreeNode neighbor :
                            Arrays.asList(curr.left,
                                    curr.right,
                                    parentMap.get(curr))) {

                        if (neighbor != null && !visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.offer(neighbor);
                        }
                    }
                }
            }

            return time;
        }
    }


    /*
    ============================================================
    PROBLEM 3 — MAXIMUM DISTANCE FROM A GIVEN NODE
    (Custom Reinforcement)
    ============================================================

    📘 PROBLEM STATEMENT

    Given a binary tree and a target node value,
    return the maximum distance from that node to any other node.

    Example:

            1
           / \
          2   3
         /
        4

    target = 2
    Output = 2   (distance to node 3)

    ------------------------------------------------------------
    🟢 Invariant Mapping:
        Last BFS level depth = maximum distance.

    🔴 Naive Failure:
        Using subtree height ignores upward expansion.

    ------------------------------------------------------------
    */

    static class MaxDistanceFromNodeClean {

        public int maxDistance(TreeNode root, int target) {

            // Step 1: Build full parent map
            Map<TreeNode, TreeNode> parentMap = new HashMap<>();
            buildParentMap(root, null, parentMap);

            // Step 2: Locate target node
            TreeNode targetNode = findNode(root, target);
            if (targetNode == null) return 0;

            // Step 3: BFS to compute max distance
            return bfsMaxDistance(targetNode, parentMap);
        }

        private void buildParentMap(TreeNode node,
                                    TreeNode parent,
                                    Map<TreeNode, TreeNode> parentMap) {

            if (node == null) return;

            parentMap.put(node, parent);

            buildParentMap(node.left, node, parentMap);
            buildParentMap(node.right, node, parentMap);
        }

        private TreeNode findNode(TreeNode node, int target) {

            if (node == null) return null;

            if (node.val == target)
                return node;

            TreeNode left = findNode(node.left, target);
            if (left != null) return left;

            return findNode(node.right, target);
        }

        private int bfsMaxDistance(TreeNode start,
                                   Map<TreeNode, TreeNode> parentMap) {

            Queue<TreeNode> queue = new LinkedList<>();
            Set<TreeNode> visited = new HashSet<>();

            queue.offer(start);
            visited.add(start);

            int maxDist = -1;

            while (!queue.isEmpty()) {

                int size = queue.size();
                maxDist++;

                for (int i = 0; i < size; i++) {

                    TreeNode curr = queue.poll();

                    for (TreeNode neighbor :
                            Arrays.asList(curr.left,
                                    curr.right,
                                    parentMap.get(curr))) {

                        if (neighbor != null && !visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.offer(neighbor);
                        }
                    }
                }
            }

            return maxDist;
        }
    }


        /*
    ============================================================
    🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
    ============================================================

    These problems are closely related but slightly shift the invariant
    or extend it into graph generalization.

    ============================================================
    RELATED 1 — BINARY TREE LEVEL ORDER TRAVERSAL
    (LeetCode 102)
    ============================================================

    📘 Problem Statement (Condensed)

    Given the root of a binary tree, return the level order traversal
    of its nodes' values (i.e., from left to right, level by level).

    🟢 Invariant:
        BFS level = depth from root.

    ⚫ Pattern Mapping:
        This is same level-group invariant,
        but source = root (not arbitrary node).

    🔴 Pattern Boundary:
        No parent edge needed.
        No visited set needed (tree is directed downward).

    ============================================================
    RELATED 2 — SHORTEST PATH IN UNWEIGHTED GRAPH
    ============================================================

    📘 Problem Statement (Generic Graph)

    Given an unweighted graph and source node,
    find shortest distance to all other nodes.

    🟢 Invariant:
        First time a node is visited in BFS
        equals its shortest path distance.

    ⚫ Pattern Mapping:
        Burning tree is exactly shortest path
        in an undirected acyclic graph.

    🔴 Pattern Break:
        If edges have weights → need Dijkstra.
    */

    /*
    ============================================================
    🟢 LEARNING VERIFICATION
    ============================================================

    Answer WITHOUT looking at code:

    1. 🟢 Invariant:
       Nodes at BFS level T are exactly distance T from target.

    2. 🔴 Why naive subtree-only fails:
       Because it ignores upward parent traversal.

    3. 🟡 Debug readiness:
       If output grouping wrong → level handling wrong.
       If infinite loop → missing visited set.
       If upward nodes missing → parent map missing.

    4. ⚫ Pattern recognition signals:
       - Spread
       - Infection
       - Distance K
       - Time units
       - “Each node once”
    */

    /*
    ============================================================
    🧪 MAIN METHOD + SELF-VERIFYING TESTS
    ============================================================
    */

    public static void main(String[] args) {

        /*
        --------------------------------------------------------
        Test 1 — Example 1 from Problem Statement
        --------------------------------------------------------
        */

        TreeNode root1 = new TreeNode(12);
        root1.left = new TreeNode(13);
        root1.right = new TreeNode(10);
        root1.right.left = new TreeNode(14);
        root1.right.right = new TreeNode(15);
        root1.right.left.left = new TreeNode(21);
        root1.right.left.right = new TreeNode(24);
        root1.right.right.left = new TreeNode(22);
        root1.right.right.right = new TreeNode(23);

        Optimal optimal = new Optimal();
        List<List<Integer>> burn1 = optimal.burn(root1, 14);

        assert burn1.get(0).equals(Arrays.asList(14));
        assert burn1.get(1).containsAll(Arrays.asList(21,24,10));
        assert burn1.get(2).containsAll(Arrays.asList(15,12));
        assert burn1.get(3).containsAll(Arrays.asList(22,23,13));

        /*
        --------------------------------------------------------
        Test 2 — Single Node Tree
        --------------------------------------------------------
        */

        TreeNode single = new TreeNode(1);
        List<List<Integer>> burn2 = optimal.burn(single, 1);
        assert burn2.size() == 1;
        assert burn2.get(0).equals(Arrays.asList(1));

        /*
        --------------------------------------------------------
        Test 3 — Linear Chain (Interviewer Trap)
        --------------------------------------------------------
        */

        TreeNode chain = new TreeNode(1);
        chain.left = new TreeNode(2);
        chain.left.left = new TreeNode(3);
        chain.left.left.left = new TreeNode(4);

        List<List<Integer>> burn3 = optimal.burn(chain, 4);

        assert burn3.size() == 4;
        assert burn3.get(0).equals(Arrays.asList(4));
        assert burn3.get(1).equals(Arrays.asList(3));
        assert burn3.get(2).equals(Arrays.asList(2));
        assert burn3.get(3).equals(Arrays.asList(1));

        /*
        --------------------------------------------------------
        Test 4 — DistanceK Reinforcement
        --------------------------------------------------------
        */

        DistanceK dk = new DistanceK();
        List<Integer> dist = dk.distanceK(root1, 14, 2);
        assert dist.containsAll(Arrays.asList(15,12));

        /*
        --------------------------------------------------------
        Test 5 — Time To Infect Reinforcement
        --------------------------------------------------------
        */

        TimeToInfect infect = new TimeToInfect();
        int time = infect.amountOfTime(chain, 4);
        assert time == 3;

        System.out.println("All tests passed ✔");
    }

    /*
    ============================================================
    ✅ COMPLETION CHECKLIST
    ============================================================

    Invariant:
        BFS level = distance from target.

    Search Target:
        All nodes reachable via undirected expansion.

    Discard Rule:
        Skip visited nodes.

    Termination:
        Queue empty.

    Naive Failure:
        Ignores parent direction.

    Edge Cases:
        - Single node
        - Linear chain
        - Target at root
        - Target at leaf

    Variant Readiness:
        Distance K
        Time to infect
        Max distance

    Pattern Boundary:
        Breaks for weighted graphs.
    */

    /*
    🧘 FINAL CLOSURE STATEMENT

    I understand the invariant.
    I can re-derive the solution.
    This chapter is complete.
    */
}
