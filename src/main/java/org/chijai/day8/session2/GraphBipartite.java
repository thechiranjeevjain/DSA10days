package org.chijai.day8.session2;
import java.util.*;

/*
====================================================================================================
📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
====================================================================================================

LeetCode 785. Is Graph Bipartite?

Difficulty: Medium

Tags:
- Graph
- Breadth-First Search
- Depth-First Search
- Union Find

Official Link:
https://leetcode.com/problems/is-graph-bipartite/description/

Problem Statement:

There is an undirected graph with n nodes, where each node is numbered between 0 and n - 1.
You are given a 2D array graph, where graph[u] is an array of nodes that node u is adjacent to.

More formally, for each v in graph[u], there is an undirected edge between node u and node v.

The graph has the following properties:

• There are no self-edges (graph[u] does not contain u).
• There are no parallel edges (graph[u] does not contain duplicate values).
• If v is in graph[u], then u is in graph[v] (the graph is undirected).
• The graph may not be connected, meaning there may be two nodes u and v such that there is no path between them.

A graph is bipartite if the nodes can be partitioned into two independent sets A and B
such that every edge in the graph connects a node in set A and a node in set B.

Return true if and only if it is bipartite.

Examples:

Example 1:

Input:
graph = [[1,2,3],[0,2],[0,1,3],[0,2]]

Output:
false

Explanation:
There is no way to partition the nodes into two independent sets such that every edge
connects a node in one and a node in the other.

Example 2:

Input:
graph = [[1,3],[0,2],[1,3],[0,2]]

Output:
true

Explanation:
We can partition the nodes into two sets: {0, 2} and {1, 3}.

Constraints:

graph.length == n
1 <= n <= 100
0 <= graph[u].length < n
0 <= graph[u][i] <= n - 1
graph[u] does not contain u.
All the values of graph[u] are unique.
If graph[u] contains v, then graph[v] contains u.

====================================================================================================
🔵 CORE PATTERN OVERVIEW
====================================================================================================

Pattern Name:
Graph Two-Coloring / Bipartite Validation

Problem Archetype:
Constraint propagation on graph edges.

Core Invariant:
Every edge must connect nodes having opposite colors.

Equivalent invariant:
For every edge (u, v):

color[u] != color[v]

Why It Works:
If every edge connects opposite colors, the graph can be split into two valid partitions.
If even one edge connects same-colored nodes, bipartition becomes impossible.

This converts the problem into:
"Can we consistently assign alternating colors across every connected component?"

When To Use:
Use this pattern when:
• graph edges represent incompatibility/confplement/opposite grouping
• alternating assignment is required
• odd-cycle detection matters
• constraints are pairwise

Recognition Signals:
• "Divide into 2 groups"
• "No adjacent nodes together"
• "Enemies/friends/opposite teams"
• "Scheduling conflicts"
• "Alternating layers"
• "Odd cycle detection"

Key Observation:
A graph is bipartite IF AND ONLY IF:
it contains NO odd-length cycle.

Differences vs Similar Patterns:

1. Graph Traversal vs Bipartite Check
Traversal only visits nodes.
Bipartite validation additionally propagates color constraints.

2. DFS Cycle Detection vs Bipartite
Cycle detection asks:
"Does a cycle exist?"

Bipartite asks:
"Does an odd cycle exist?"

3. Union Find vs Coloring
Union Find groups components.
Coloring propagates parity/opposite constraints.

4. Tree Validation vs Bipartite
Trees are ALWAYS bipartite.
General graphs may contain odd cycles.

====================================================================================================
🟢 MENTAL MODEL & INVARIANTS
====================================================================================================

Mental Model:

Imagine every edge is a command:

"If node u is RED,
then neighbor v MUST be BLUE."

Every traversal step propagates this forced opposite-color constraint.

We are not "choosing freely" after initialization.
We are verifying consistency of constraints.

Core State:

color[i] meaning:
0  -> unvisited/uncolored
1  -> RED
-1 -> BLUE

Primary Invariant:

For every processed edge (u, v):

color[u] = -color[v]

If violated once:
graph is NOT bipartite.

Traversal Invariant:

Before processing neighbors of current node:
current node already has a valid fixed color.

Neighbor Handling Rules:

Case 1:
neighbor uncolored

→ assign opposite color
→ continue traversal

Case 2:
neighbor already colored opposite

→ valid edge
→ continue

Case 3:
neighbor already colored SAME

→ invariant broken
→ return false immediately

Connected Component Invariant:

The graph may be disconnected.

Therefore:
every unvisited node may start a NEW independent bipartite coloring process.

Allowed Moves:
✅ Assign opposite color
✅ Revisit already correctly colored node
✅ Start BFS/DFS from unvisited component

Forbidden Moves:
❌ Recolor already colored node differently
❌ Ignore same-color conflict
❌ Assume graph connected

Termination Logic:

Traversal ends when:
• every node processed
OR
• first invariant violation discovered

Why Naive Approaches Fail:

Naive Mistake:
"Every graph without triangle is bipartite."

Wrong.

Counterexample:
5-cycle:
0-1-2-3-4-0

No triangle exists.
Still NOT bipartite because cycle length is odd.

Another Mistake:
Only checking local neighbors without propagation.

Bipartite validity is GLOBAL consistency propagation.

Variable Meanings:

queue:
frontier for BFS constraint propagation

node:
current validated node

neighbor:
must receive opposite color

color[]:
global consistency memory

Why Odd Cycles Break Everything:

Start with:
0 -> RED

Then propagate around odd cycle:

1 -> BLUE
2 -> RED
3 -> BLUE
...
Eventually same node demands BOTH colors.

Contradiction.

That contradiction manifests as:
adjacent same-colored nodes.

====================================================================================================
🔴 WHY WRONG SOLUTIONS FAIL
====================================================================================================

❌ Wrong Approach #1:
Check only immediate neighbors of start node.

Why it seems correct:
Local alternation appears enough.

Invariant violation:
Bipartite property is global across paths.

Counterexample:

0---1
|   |
3---2

Need propagation around full cycle.

Interviewer Trap:
Candidate forgets deeper-level conflicts.

----------------------------------------------------------------------------------------------------

❌ Wrong Approach #2:
Run BFS from node 0 only.

Why it seems correct:
Traversal works on connected graphs.

Invariant violation:
Disconnected components remain unchecked.

Counterexample:

Component 1:
0-1

Component 2:
2-3-4-2 (odd cycle)

Starting only from 0 misses invalid component.

----------------------------------------------------------------------------------------------------

❌ Wrong Approach #3:
Use visited[] without colors[].

Why it seems correct:
Traversal already prevents revisits.

Invariant violation:
Need parity/opposite relationship memory.

Visited alone cannot detect:
same-level conflict.

----------------------------------------------------------------------------------------------------

❌ Wrong Approach #4:
Recolor node when conflict appears.

Why it seems correct:
Trying to "fix" assignment greedily.

Invariant violation:
Earlier propagated constraints become invalid.

Color assignment becomes inconsistent globally.

----------------------------------------------------------------------------------------------------

❌ Wrong Approach #5:
Detect ALL cycles and reject cyclic graphs.

Why it seems correct:
Odd cycles are bad.

Invariant violation:
Even cycles are completely valid.

Counterexample:

0-1-2-3-0

Even cycle.
Perfectly bipartite.

====================================================================================================
⚙️ HOW TO PHYSICALLY ASSEMBLE THE CODE
====================================================================================================

🛠️ IMPLEMENTATION BLUEPRINT (BFS VERSION)

Mechanical Typing Order:

1. Function signature

boolean isBipartite(int[][] graph)

----------------------------------------------------------------------------------------------------

2. Create color array

int n = graph.length;
int[] color = new int[n];

0 means uncolored.

----------------------------------------------------------------------------------------------------

3. Iterate all nodes

for each node:
    if uncolored:
        start BFS

This handles disconnected components.

----------------------------------------------------------------------------------------------------

4. Initialize BFS

Queue<Integer> queue = new LinkedList<>();

Assign initial color:
color[start] = 1

Push into queue.

----------------------------------------------------------------------------------------------------

5. BFS loop skeleton

while queue not empty:
    pop current node

----------------------------------------------------------------------------------------------------

6. Process neighbors

for neighbor in graph[node]:

----------------------------------------------------------------------------------------------------

7. Unvisited neighbor case

if color[neighbor] == 0:

assign opposite color:
color[neighbor] = -color[node]

push neighbor

----------------------------------------------------------------------------------------------------

8. Conflict case

else if color[neighbor] == color[node]:

return false immediately

----------------------------------------------------------------------------------------------------

9. Finish traversal

If all components processed safely:
return true

====================================================================================================
🧾 ULTRA-COMPACT PSEUDOCODE (MEMORY SCAFFOLD)
====================================================================================================

color[] = uncolored

for each node:
    if uncolored:
        assign color
        BFS

        while queue:
            pop node

            for neighbor:
                if uncolored:
                    assign opposite
                else if same color:
                    return false

return true

====================================================================================================
6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES
====================================================================================================
*/

public class GraphBipartite {

    /*
    ================================================================================================
    BRUTE FORCE SOLUTION
    ================================================================================================

    Core Idea:
    Try all possible 2-color assignments.

    Invariant Enforced:
    Every edge must connect opposite sets.

    Limitation Fixed Later:
    Exponential exploration is unnecessary because graph constraints propagate deterministically.

    Time Complexity:
    O(2^V * E)

    Space Complexity:
    O(V)

    Interview Preference:
    ❌ Never preferred.
    Only useful pedagogically to understand search space.
    */
    static class BruteForceBacktrackingSolution {

        public boolean isBipartite(int[][] graph) {
            int n = graph.length;

            int[] color = new int[n];

            return backtrack(graph, color, 0);
        }

        private boolean backtrack(int[][] graph, int[] color, int node) {

            // All nodes assigned successfully.
            if (node == graph.length) {
                return isValid(graph, color);
            }

            // Try RED.
            color[node] = 1;

            if (backtrack(graph, color, node + 1)) {
                return true;
            }

            // Try BLUE.
            color[node] = -1;

            if (backtrack(graph, color, node + 1)) {
                return true;
            }

            return false;
        }

        private boolean isValid(int[][] graph, int[] color) {

            for (int node = 0; node < graph.length; node++) {

                for (int neighbor : graph[node]) {

                    // Every edge must connect opposite colors.
                    if (color[node] == color[neighbor]) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    /*
    ================================================================================================
    IMPROVED SOLUTION — DFS TWO COLORING
    ================================================================================================

    Core Idea:
    Use DFS to propagate opposite-color constraints.

    Invariant:
    Adjacent nodes must always have opposite colors.

    Limitation Fixed:
    Avoids exponential assignments by deterministic propagation.

    Time Complexity:
    O(V + E)

    Space Complexity:
    O(V) recursion stack

    Interview Preference:
    ✅ Good
    But recursion depth may worry some interviewers in larger constraints.
    */
    static class DFSTwoColoringSolution {

        public boolean isBipartite(int[][] graph) {

            int n = graph.length;

            int[] color = new int[n];

            // Graph may be disconnected.
            for (int node = 0; node < n; node++) {

                if (color[node] != 0) {
                    continue;
                }

                // Start new component with arbitrary color.
                if (!dfs(graph, color, node, 1)) {
                    return false;
                }
            }

            return true;
        }

        private boolean dfs(int[][] graph, int[] color, int node, int currentColor) {

            color[node] = currentColor;

            for (int neighbor : graph[node]) {

                // Neighbor uncolored -> force opposite color.
                if (color[neighbor] == 0) {

                    if (!dfs(graph, color, neighbor, -currentColor)) {
                        return false;
                    }
                }

                // Same color on both ends -> invariant broken.
                else if (color[neighbor] == currentColor) {
                    return false;
                }
            }

            return true;
        }
    }

    /*
    ================================================================================================
    OPTIMAL SOLUTION — BFS TWO COLORING (INTERVIEW PREFERRED)
    ================================================================================================

    Core Idea:
    BFS propagates alternating parity/color constraints level-by-level.

    Core Invariant:
    Every traversed edge connects opposite colors.

    Why This Is Interview Preferred:
    • iterative
    • stable
    • easy to reason about
    • avoids recursion depth concerns
    • naturally exposes invariant propagation

    Time Complexity:
    O(V + E)

    Space Complexity:
    O(V)
    */
    static class BFSOptimalSolution {

        public boolean isBipartite(int[][] graph) {

            // Handle empty graph defensively.
            if (graph == null) {
                return true;
            }

            int n = graph.length;

            /*
             color meanings:
             0  -> uncolored
             1  -> RED
             -1 -> BLUE
            */
            int[] color = new int[n];

            // Graph may contain multiple disconnected components.
            for (int start = 0; start < n; start++) {

                // Already processed inside earlier traversal.
                if (color[start] != 0) {
                    continue;
                }

                Queue<Integer> queue = new LinkedList<>();

                // Arbitrarily assign initial color.
                color[start] = 1;

                queue.offer(start);

                while (!queue.isEmpty()) {

                    int node = queue.poll();

                    // Current node already satisfies all previous constraints.
                    for (int neighbor : graph[node]) {

                        // Unvisited neighbor -> assign opposite color.
                        if (color[neighbor] == 0) {

                            // Invariant:
                            // Every edge connects opposite colors.
                            color[neighbor] = -color[node];

                            queue.offer(neighbor);
                        }

                        // Conflict:
                        // Adjacent nodes cannot share same color.
                        else if (color[neighbor] == color[node]) {

                            // Odd-cycle contradiction discovered.
                            return false;
                        }
                    }
                }
            }

            // Every edge satisfied bipartite invariant.
            return true;
        }
    }

    /*
    ================================================================================================
    🟣 INTERVIEW ARTICULATION (NO CODE)
    ================================================================================================

    Verbal Explanation:

    "I model bipartite validation as a graph coloring problem.

    The invariant is:
    every edge must connect nodes of opposite colors.

    I traverse each connected component using BFS or DFS.
    Whenever I visit an uncolored neighbor,
    I assign the opposite color.

    If I ever encounter an edge connecting same-colored nodes,
    that means constraints became contradictory,
    which implies an odd cycle exists,
    so the graph is not bipartite."

    ------------------------------------------------------------------------------------------------

    Discard Logic Equivalent:

    In binary search:
    invalid half discarded.

    Here:
    invalid coloring immediately terminates traversal.

    ------------------------------------------------------------------------------------------------

    Correctness Guarantee:

    Every reachable node receives exactly one parity-consistent color.

    Since all edges are checked:
    if no contradiction exists,
    a valid partition exists.

    ------------------------------------------------------------------------------------------------

    What Breaks If Changed?

    If we remove:
    color[neighbor] == color[node]

    then odd-cycle conflicts go undetected.

    If we skip disconnected components:
    invalid subgraphs may remain unchecked.

    ------------------------------------------------------------------------------------------------

    In-Place Feasibility:

    Yes.
    We only maintain:
    • queue/stack
    • color array

    ------------------------------------------------------------------------------------------------

    Streaming Feasibility:

    Limited.

    Full correctness requires remembering prior color assignments.

    ------------------------------------------------------------------------------------------------

    When NOT To Use This Pattern:

    Do NOT use:
    • directed graph SCC problems
    • weighted shortest path
    • topological ordering
    • arbitrary k-coloring

    This pattern specifically solves:
    binary parity constraints.


    /*
    ================================================================================================
    🎯 INTERVIEW RECALL SHEET (30-SECOND RECALL)
    ================================================================================================

    Pattern Trigger:
    "Split graph into 2 groups with no internal edges."

    Core Invariant:
    Every edge connects opposite colors.

    Search Target:
    Detect coloring contradiction.

    Discard Rule:
    Same-color adjacent nodes => impossible => return false.

    Common Trap:
    Forgetting disconnected components.

    Edge Cases:
    • isolated nodes
    • disconnected graph
    • even cycle
    • odd cycle

    Interview One-Liner:
    "Bipartite checking is simply parity propagation across edges."

    Re-Derivation Cue:
    "Neighbor must always be opposite color."

    Memory Compression:

    color node
    BFS/DFS neighbors
    assign opposite
    detect same-color conflict
    process all components

    ================================================================================================
    🔄 VARIATIONS & TWEAKS
    ================================================================================================

    Variation #1:
    DFS instead of BFS

    Invariant Status:
    SAME invariant.

    Only traversal order changes.

    ------------------------------------------------------------------------------------------------

    Variation #2:
    Union Find Approach

    Idea:
    All neighbors of a node must belong to opposite partition from node.

    Modified reasoning:
    Group neighbors together.

    Invariant still preserved indirectly.

    ------------------------------------------------------------------------------------------------

    Variation #3:
    Detect Odd Cycle Explicitly

    Observation:
    Graph is bipartite iff no odd cycle exists.

    BFS level parity can detect this.

    ------------------------------------------------------------------------------------------------

    Variation #4:
    Directed Graph

    Pattern Break Signal:
    Bipartite definition fundamentally assumes undirected constraints.

    Need problem reinterpretation.

    ------------------------------------------------------------------------------------------------

    Variation #5:
    K-Coloring Instead of 2-Coloring

    Pattern Break:
    Opposite-color invariant no longer sufficient.

    Problem becomes significantly harder.

    ------------------------------------------------------------------------------------------------

    Variation #6:
    Tree Graph

    Observation:
    Every tree is automatically bipartite.

    Why?
    Trees contain no cycles,
    therefore cannot contain odd cycles.

    ------------------------------------------------------------------------------------------------

    Variation #7:
    Self Edge Present

    Example:
    0 -> 0

    Immediate failure because:
    node would need opposite color from itself.

    ================================================================================================
    ⚫ REINFORCEMENT PROBLEMS
    ================================================================================================
    */

    /*
    ================================================================================================
    REINFORCEMENT PROBLEM #1
    Possible Bipartition
    LeetCode 886
    ================================================================================================

    Problem Summary:

    Given n people and dislike pairs,
    determine if they can be split into 2 groups such that
    no disliked pair exists inside same group.

    Key Mapping:
    people -> nodes
    dislikes -> edges

    SAME invariant:
    adjacent nodes must have opposite colors.

    Example:

    n = 4
    dislikes = [[1,2],[1,3],[2,4]]

    Valid split:
    {1,4} and {2,3}

    Edge Cases:
    • disconnected people groups
    • isolated nodes
    • odd dislike cycle

    Interview Trap:
    Nodes are 1-indexed.
    */
    static class PossibleBipartitionSolution {

        public boolean possibleBipartition(int n, int[][] dislikes) {

            List<Integer>[] graph = new ArrayList[n + 1];

            for (int i = 0; i <= n; i++) {
                graph[i] = new ArrayList<>();
            }

            for (int[] edge : dislikes) {

                int u = edge[0];
                int v = edge[1];

                graph[u].add(v);
                graph[v].add(u);
            }

            int[] color = new int[n + 1];

            for (int person = 1; person <= n; person++) {

                if (color[person] != 0) {
                    continue;
                }

                Queue<Integer> queue = new LinkedList<>();

                color[person] = 1;

                queue.offer(person);

                while (!queue.isEmpty()) {

                    int current = queue.poll();

                    for (int neighbor : graph[current]) {

                        if (color[neighbor] == 0) {

                            color[neighbor] = -color[current];

                            queue.offer(neighbor);
                        }

                        else if (color[neighbor] == color[current]) {

                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    /*
    🟣 Interview Articulation:

    "This is structurally identical to bipartite graph checking.
    Dislikes become undirected edges.
    The invariant remains:
    disliked people must belong to opposite groups."
    */

    /*
    ================================================================================================
    REINFORCEMENT PROBLEM #2
    Course Schedule (Invariant Adaptation)
    LeetCode 207
    ================================================================================================

    Problem Summary:

    Determine if all courses can be completed given prerequisites.

    Important:
    NOT bipartite.

    But still graph constraint propagation.

    Invariant Mapping:
    Instead of opposite colors,
    invariant becomes:
    no directed cycle.

    Key Example:

    numCourses = 2
    prerequisites = [[1,0]]

    Valid.

    numCourses = 2
    prerequisites = [[1,0],[0,1]]

    Invalid cycle.

    Interview Insight:
    Helps distinguish:
    undirected odd-cycle problems
    vs
    directed cycle problems.
    */
    static class CourseScheduleSolution {

        public boolean canFinish(int numCourses, int[][] prerequisites) {

            List<Integer>[] graph = new ArrayList[numCourses];

            for (int i = 0; i < numCourses; i++) {
                graph[i] = new ArrayList<>();
            }

            int[] indegree = new int[numCourses];

            for (int[] edge : prerequisites) {

                int course = edge[0];
                int prerequisite = edge[1];

                graph[prerequisite].add(course);

                indegree[course]++;
            }

            Queue<Integer> queue = new LinkedList<>();

            for (int i = 0; i < numCourses; i++) {

                if (indegree[i] == 0) {
                    queue.offer(i);
                }
            }

            int completed = 0;

            while (!queue.isEmpty()) {

                int current = queue.poll();

                completed++;

                for (int next : graph[current]) {

                    indegree[next]--;

                    if (indegree[next] == 0) {
                        queue.offer(next);
                    }
                }
            }

            return completed == numCourses;
        }
    }

    /*
    🟣 Interview Articulation:

    "This problem is NOT bipartite because edges are directed.
    The invariant changes from parity consistency
    to acyclic dependency ordering."
    */

    /*
    ================================================================================================
    REINFORCEMENT PROBLEM #3
    Graph Valid Tree
    LeetCode 261
    ================================================================================================

    Problem Summary:

    Determine whether graph forms a valid tree.

    Tree Properties:
    • connected
    • acyclic

    Invariant Mapping:

    Tree => automatically bipartite.

    But this problem asks stricter constraints.

    Example:

    n = 5
    edges = [[0,1],[0,2],[0,3],[1,4]]

    valid tree -> true

    Edge Cases:
    • disconnected graph
    • extra edge causing cycle

    Interview Trap:
    Candidate checks only cycle OR only connectivity.
    Need BOTH.
    */
    static class GraphValidTreeSolution {

        public boolean validTree(int n, int[][] edges) {

            // Tree with n nodes must contain exactly n - 1 edges.
            if (edges.length != n - 1) {
                return false;
            }

            List<Integer>[] graph = new ArrayList[n];

            for (int i = 0; i < n; i++) {
                graph[i] = new ArrayList<>();
            }

            for (int[] edge : edges) {

                int u = edge[0];
                int v = edge[1];

                graph[u].add(v);
                graph[v].add(u);
            }

            boolean[] visited = new boolean[n];

            Queue<Integer> queue = new LinkedList<>();

            queue.offer(0);

            visited[0] = true;

            int visitedCount = 0;

            while (!queue.isEmpty()) {

                int node = queue.poll();

                visitedCount++;

                for (int neighbor : graph[node]) {

                    if (!visited[neighbor]) {

                        visited[neighbor] = true;

                        queue.offer(neighbor);
                    }
                }
            }

            return visitedCount == n;
        }
    }

    /*
    🟣 Interview Articulation:

    "All trees are bipartite,
    but not all bipartite graphs are trees.

    Bipartite allows even cycles.
    Trees allow no cycles."
    */

    /*
    ================================================================================================
    🧩 RELATED PROBLEMS
    ================================================================================================
    */

    /*
    ================================================================================================
    RELATED PROBLEM #1
    Redundant Connection
    LeetCode 684
    ================================================================================================

    Problem Summary:

    Find extra edge creating cycle in undirected graph.

    Invariant Relationship:
    Modified invariant.

    Instead of:
    opposite parity consistency

    We care about:
    cycle creation.

    Preferred Pattern:
    Union Find.

    Edge Case:
    Last edge causing cycle must be returned.
    */
    static class RedundantConnectionSolution {

        static class UnionFind {

            int[] parent;
            int[] rank;

            UnionFind(int n) {

                parent = new int[n + 1];
                rank = new int[n + 1];

                for (int i = 0; i <= n; i++) {
                    parent[i] = i;
                }
            }

            int find(int x) {

                if (parent[x] != x) {
                    parent[x] = find(parent[x]);
                }

                return parent[x];
            }

            boolean union(int a, int b) {

                int rootA = find(a);
                int rootB = find(b);

                if (rootA == rootB) {
                    return false;
                }

                if (rank[rootA] < rank[rootB]) {

                    parent[rootA] = rootB;
                }

                else if (rank[rootA] > rank[rootB]) {

                    parent[rootB] = rootA;
                }

                else {

                    parent[rootB] = rootA;

                    rank[rootA]++;
                }

                return true;
            }
        }

        public int[] findRedundantConnection(int[][] edges) {

            UnionFind uf = new UnionFind(edges.length);

            for (int[] edge : edges) {

                if (!uf.union(edge[0], edge[1])) {
                    return edge;
                }
            }

            return new int[0];
        }
    }

    /*
    🟣 Interview Note:

    "This problem detects cycle formation directly.
    Bipartite instead detects parity contradiction."
    */

    /*
    ================================================================================================
    RELATED PROBLEM #2
    Number of Provinces
    LeetCode 547
    ================================================================================================

    Problem Summary:

    Count connected components.

    Same or Modified Invariant?
    Modified.

    We only care about reachability,
    not opposite coloring.

    Edge Case:
    isolated nodes count as provinces.
    */
    static class NumberOfProvincesSolution {

        public int findCircleNum(int[][] isConnected) {

            int n = isConnected.length;

            boolean[] visited = new boolean[n];

            int provinces = 0;

            for (int city = 0; city < n; city++) {

                if (!visited[city]) {

                    provinces++;

                    bfs(isConnected, visited, city);
                }
            }

            return provinces;
        }

        private void bfs(int[][] graph, boolean[] visited, int start) {

            Queue<Integer> queue = new LinkedList<>();

            queue.offer(start);

            visited[start] = true;

            while (!queue.isEmpty()) {

                int node = queue.poll();

                for (int neighbor = 0; neighbor < graph.length; neighbor++) {

                    if (graph[node][neighbor] == 1 && !visited[neighbor]) {

                        visited[neighbor] = true;

                        queue.offer(neighbor);
                    }
                }
            }
        }
    }

    /*
    🟣 Interview Note:

    "Traversal structure is same as bipartite BFS,
    but invariant changed from color consistency
    to component discovery."
    */


    /*
================================================================================================
RELATED PROBLEM #3
Clone Graph
LeetCode 133
================================================================================================

Problem Summary:

Deep copy an undirected graph.

Same or Modified Invariant?
Modified.

We preserve:
structural equivalence.

Instead of:
opposite-color consistency.

Key Edge Case:
cycles causing infinite recursion if not memoized.
*/
    static class CloneGraphSolution {

        static class Node {

            public int val;
            public List<Node> neighbors;

            public Node() {
                val = 0;
                neighbors = new ArrayList<>();
            }

            public Node(int val) {
                this.val = val;
                neighbors = new ArrayList<>();
            }

            public Node(int val, ArrayList<Node> neighbors) {
                this.val = val;
                this.neighbors = neighbors;
            }
        }

        public Node cloneGraph(Node node) {

            if (node == null) {
                return null;
            }

            Map<Node, Node> map = new HashMap<>();

            return dfs(node, map);
        }

        private Node dfs(Node node, Map<Node, Node> map) {

            if (map.containsKey(node)) {
                return map.get(node);
            }

            Node clone = new Node(node.val);

            map.put(node, clone);

            for (Node neighbor : node.neighbors) {

                clone.neighbors.add(dfs(neighbor, map));
            }

            return clone;
        }
    }

    /*
    🟣 Interview Note:

    "Clone Graph preserves topology.
    Bipartite checking preserves parity consistency."
    */

    /*
    ================================================================================================
    🧠 MASTERY CHECKLIST
    ================================================================================================

    Q1. What is the invariant?

    Every edge must connect opposite-colored nodes.

    ------------------------------------------------------------------------------------------------

    Q2. What is the search target?

    Detect contradiction:
    adjacent nodes having same color.

    ------------------------------------------------------------------------------------------------

    Q3. What is the discard/failure rule?

    If color[u] == color[v] for any edge,
    graph cannot be bipartite.

    ------------------------------------------------------------------------------------------------

    Q4. What is termination logic?

    Traverse all connected components.
    Return false immediately on first contradiction.

    ------------------------------------------------------------------------------------------------

    Q5. Why does naive approach fail?

    Local checking misses global parity conflicts.

    ------------------------------------------------------------------------------------------------

    Q6. What edge cases must be remembered?

    • disconnected graph
    • isolated nodes
    • odd cycle
    • even cycle
    • single node
    • empty adjacency list

    ------------------------------------------------------------------------------------------------

    Q7. Debugging Readiness

    If output wrong:
    verify:
    • disconnected component traversal
    • opposite-color assignment
    • conflict detection
    • queue/stack processing

    ------------------------------------------------------------------------------------------------

    Q8. Variant Readiness

    Can adapt to:
    • BFS
    • DFS
    • Union Find parity
    • odd-cycle detection

    ------------------------------------------------------------------------------------------------

    Q9. Pattern Boundary

    This pattern solves:
    binary parity constraints.

    NOT:
    • weighted shortest path
    • directed DAG ordering
    • arbitrary k-coloring
    • MST problems

    ================================================================================================
    🧪 SELF-VERIFYING TEST UTILITIES
    ================================================================================================
    */

    static void assertTrue(boolean condition, String message) {

        if (!condition) {
            throw new RuntimeException("Assertion failed: " + message);
        }
    }

    static void assertFalse(boolean condition, String message) {

        if (condition) {
            throw new RuntimeException("Assertion failed: " + message);
        }
    }

    static void assertEquals(int expected, int actual, String message) {

        if (expected != actual) {

            throw new RuntimeException(
                    "Assertion failed: " + message
                            + " | expected = " + expected
                            + " | actual = " + actual
            );
        }
    }

    static void assertArrayEquals(int[] expected, int[] actual, String message) {

        if (!Arrays.equals(expected, actual)) {

            throw new RuntimeException(
                    "Assertion failed: " + message
                            + " | expected = " + Arrays.toString(expected)
                            + " | actual = " + Arrays.toString(actual)
            );
        }
    }

    /*
    ================================================================================================
    🧪 PRIMARY PROBLEM TESTS
    ================================================================================================
    */

    static void runPrimaryProblemTests() {

        BFSOptimalSolution solver = new BFSOptimalSolution();

        /*
        --------------------------------------------------------------------------------------------
        Happy Path:
        Simple even cycle.
        Valid bipartite graph.
        --------------------------------------------------------------------------------------------
        */
        int[][] graph1 = {
                {1, 3},
                {0, 2},
                {1, 3},
                {0, 2}
        };

        assertTrue(
                solver.isBipartite(graph1),
                "Even cycle should be bipartite"
        );



        /*
        --------------------------------------------------------------------------------------------
        Interview Trap:
        Odd cycle.
        */

        int[][] graph2 = {
                {1, 2, 3},
                {0, 2},
                {0, 1, 3},
                {0, 2}
        };

        assertFalse(
                solver.isBipartite(graph2),
                "Odd cycle should NOT be bipartite"
        );

        /*
        --------------------------------------------------------------------------------------------
        Boundary Case:
        Single isolated node.
        --------------------------------------------------------------------------------------------
        */
        int[][] graph3 = {
                {}
        };

        assertTrue(
                solver.isBipartite(graph3),
                "Single node graph should be bipartite"
        );

        /*
        --------------------------------------------------------------------------------------------
        Disconnected graph:
        One component valid, another invalid.
        Must still return false.
        --------------------------------------------------------------------------------------------
        */
        int[][] graph4 = {
                {1},
                {0},

                {3, 4},
                {2, 4},
                {2, 3}
        };

        assertFalse(
                solver.isBipartite(graph4),
                "Disconnected graph with odd cycle component should fail"
        );

        /*
        --------------------------------------------------------------------------------------------
        Tree graph:
        Every tree is bipartite.
        --------------------------------------------------------------------------------------------
        */
        int[][] graph5 = {
                {1, 2},
                {0, 3},
                {0},
                {1}
        };

        assertTrue(
                solver.isBipartite(graph5),
                "Trees are always bipartite"
        );

        /*
        --------------------------------------------------------------------------------------------
        Empty adjacency lists.
        Multiple isolated nodes.
        --------------------------------------------------------------------------------------------
        */
        int[][] graph6 = {
                {},
                {},
                {}
        };

        assertTrue(
                solver.isBipartite(graph6),
                "All isolated nodes should be bipartite"
        );

        /*
        --------------------------------------------------------------------------------------------
        Long even chain.
        Tests propagation stability.
        --------------------------------------------------------------------------------------------
        */
        int[][] graph7 = {
                {1},
                {0, 2},
                {1, 3},
                {2, 4},
                {3, 5},
                {4}
        };

        assertTrue(
                solver.isBipartite(graph7),
                "Linear chain should be bipartite"
        );

        /*
        --------------------------------------------------------------------------------------------
        Triangle graph.
        Smallest odd cycle.
        --------------------------------------------------------------------------------------------
        */
        int[][] graph8 = {
                {1, 2},
                {0, 2},
                {0, 1}
        };

        assertFalse(
                solver.isBipartite(graph8),
                "Triangle graph should fail bipartite validation"
        );

        System.out.println("✅ Primary problem tests passed.");
    }

    /*
    ================================================================================================
    🧪 REINFORCEMENT PROBLEM TESTS
    ================================================================================================
    */

    static void runPossibleBipartitionTests() {

        PossibleBipartitionSolution solver = new PossibleBipartitionSolution();

        int[][] dislikes1 = {
                {1, 2},
                {1, 3},
                {2, 4}
        };

        assertTrue(
                solver.possibleBipartition(4, dislikes1),
                "Valid dislike partition should succeed"
        );

        int[][] dislikes2 = {
                {1, 2},
                {1, 3},
                {2, 3}
        };

        assertFalse(
                solver.possibleBipartition(3, dislikes2),
                "Odd conflict cycle should fail"
        );

        System.out.println("✅ Possible Bipartition tests passed.");
    }

    static void runCourseScheduleTests() {

        CourseScheduleSolution solver = new CourseScheduleSolution();

        int[][] prerequisites1 = {
                {1, 0}
        };

        assertTrue(
                solver.canFinish(2, prerequisites1),
                "Simple dependency chain should succeed"
        );

        int[][] prerequisites2 = {
                {1, 0},
                {0, 1}
        };

        assertFalse(
                solver.canFinish(2, prerequisites2),
                "Directed cycle should fail"
        );

        System.out.println("✅ Course Schedule tests passed.");
    }

    static void runGraphValidTreeTests() {

        GraphValidTreeSolution solver = new GraphValidTreeSolution();

        int[][] edges1 = {
                {0, 1},
                {0, 2},
                {0, 3},
                {1, 4}
        };

        assertTrue(
                solver.validTree(5, edges1),
                "Connected acyclic graph should be valid tree"
        );

        int[][] edges2 = {
                {0, 1},
                {1, 2},
                {2, 3},
                {1, 3},
                {1, 4}
        };

        assertFalse(
                solver.validTree(5, edges2),
                "Cycle should invalidate tree"
        );

        System.out.println("✅ Graph Valid Tree tests passed.");
    }

    static void runRedundantConnectionTests() {

        RedundantConnectionSolution solver = new RedundantConnectionSolution();

        int[][] edges1 = {
                {1, 2},
                {1, 3},
                {2, 3}
        };

        assertArrayEquals(
                new int[]{2, 3},
                solver.findRedundantConnection(edges1),
                "Last edge creating cycle should be returned"
        );

        System.out.println("✅ Redundant Connection tests passed.");
    }

    static void runNumberOfProvincesTests() {

        NumberOfProvincesSolution solver = new NumberOfProvincesSolution();

        int[][] matrix1 = {
                {1, 1, 0},
                {1, 1, 0},
                {0, 0, 1}
        };

        assertEquals(
                2,
                solver.findCircleNum(matrix1),
                "Should detect two connected provinces"
        );

        int[][] matrix2 = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };

        assertEquals(
                3,
                solver.findCircleNum(matrix2),
                "All isolated cities should form separate provinces"
        );

        System.out.println("✅ Number Of Provinces tests passed.");
    }

    /*
    ================================================================================================
    🧪 MAIN
    ================================================================================================
    */

    public static void main(String[] args) {

        runPrimaryProblemTests();

        runPossibleBipartitionTests();

        runCourseScheduleTests();

        runGraphValidTreeTests();

        runRedundantConnectionTests();

        runNumberOfProvincesTests();

        System.out.println();
        System.out.println("================================================================================");
        System.out.println("ALL TESTS PASSED");
        System.out.println("================================================================================");

        /*
        ============================================================================================
        🧘 FINAL CLOSURE STATEMENT
        ============================================================================================

        I understand the invariant.
        I can re-derive the solution.
        I can physically reconstruct the implementation under pressure.
        This chapter is complete.
        */
    }
}