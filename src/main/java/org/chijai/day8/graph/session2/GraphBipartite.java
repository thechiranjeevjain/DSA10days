package org.chijai.day8.graph.session2;

import java.util.*;

/*
====================================================================================================
LEETCODE 785 — IS GRAPH BIPARTITE?
====================================================================================================

PATTERN:
Graph traversal + two-coloring

CORE INVARIANT:
Every edge must connect opposite colors.

STATE:
 0  = unvisited
 1  = color A
-1  = color B

TIME:  O(V + E)
SPACE: O(V)
====================================================================================================
*/

public class GraphBipartite {

    /*
    ================================================================================================
    ⭐ PRIMARY INTERVIEW SOLUTION — BFS
    ================================================================================================

    Recall:

        FOR every node
            if already colored → skip

            start BFS for this component

            WHILE queue not empty
                FOR every neighbor
                    uncolored → assign opposite color
                    same color → false

        true
    ================================================================================================
    */
    static class Solution {

        public boolean isBipartite(int[][] graph) {

            if (graph == null) {
                return false;
            }

            int n = graph.length;
            int[] color = new int[n];

            for (int start = 0; start < n; start++) {

                if (color[start] != 0) {
                    continue;
                }

                Deque<Integer> queue = new ArrayDeque<>();

                color[start] = 1;
                queue.offer(start);

                while (!queue.isEmpty()) {

                    int current = queue.poll();

                    for (int next : graph[current]) {

                        if (color[next] == 0) {
                            color[next] = -color[current];
                            queue.offer(next);
                        }
                        else if (color[next] == color[current]) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    /*
    ================================================================================================
    WHY THE PRIMARY CODE IS SHAPED THIS WAY
    ================================================================================================
    */

    /*
    ------------------------------------------------------------------------------------------------
    1. graph.length YES — graph[0].length AS "COLUMNS" NO
    ------------------------------------------------------------------------------------------------

    int[][] does NOT automatically mean rectangular matrix.

    Here:

        graph.length
        → number of vertices

        graph[i]
        → adjacency list of vertex i

        graph[i].length
        → number of neighbors of vertex i

    Example:

        graph = [[], [3], [], [1], []]

        graph.length    = 5
        graph[0].length = 0

    That does NOT mean the graph is empty.
    It only means vertex 0 is isolated.

    Compare:

        GRID:
            image.length     → rows
            image[0].length  → columns

        GRAPH ADJACENCY LIST:
            graph.length     → nodes
            graph[i].length  → degree of node i
    ------------------------------------------------------------------------------------------------
    */

    /*
    ------------------------------------------------------------------------------------------------
    2. WHY THE OUTER FOR LOOP?
    ------------------------------------------------------------------------------------------------

    The graph may be disconnected.

        0 -- 1       2 -- 3       4

    BFS started from 0 can only reach {0, 1}.

    Therefore:

        OUTER FOR
        → finds the next unvisited component

        BFS / DFS
        → completely explores that component

    Reusable rule:

        FOR every node
            if already seen → skip
            otherwise start a new component traversal

    Mental model:

        SCAN → START → EXPLORE
    ------------------------------------------------------------------------------------------------
    */

    /*
    ------------------------------------------------------------------------------------------------
    3. WHY QUEUE? WHY NOT STACK?
    ------------------------------------------------------------------------------------------------

    Queue is only the traversal mechanism.

        Queue     → BFS
        Stack     → iterative DFS
        Recursion → recursive DFS

    ALL work for bipartite checking.

    Do NOT memorize:

        Bipartite = Queue

    Memorize:

        Bipartite
        = traversal
        + opposite-color propagation
        + contradiction detection
    ------------------------------------------------------------------------------------------------
    */

    /*
    ------------------------------------------------------------------------------------------------
    4. WHY A NEW QUEUE INSIDE EACH COMPONENT?
    ------------------------------------------------------------------------------------------------

    Conceptually:

        ONE COMPONENT
        → ONE BFS
        → ONE WORKLIST

    Could the queue be declared outside the outer loop?

        Yes.

    After each full BFS it is empty.

    Keeping it inside gives cleaner ownership:
    the queue belongs to the traversal of that component.
    ------------------------------------------------------------------------------------------------
    */

    /*
    ------------------------------------------------------------------------------------------------
    5. WHY WHILE(queue not empty) INSTEAD OF ONLY A SIMPLE FOR LOOP?
    ------------------------------------------------------------------------------------------------

    A simple:

        for (int i = 0; i < n; i++)

    follows node-number order:

        0, 1, 2, 3...

    Graph traversal must follow ACTUAL EDGES.

    The queue stores newly discovered nodes whose neighbors still need processing.

    Therefore:

        WHILE queue not empty
        → keep processing discovered reachable work
        → until this component is exhausted
    ------------------------------------------------------------------------------------------------
    */

    /*
    ------------------------------------------------------------------------------------------------
    6. WHY THE INNER FOR LOOP?
    ------------------------------------------------------------------------------------------------

        for (int next : graph[current])

    THIS is what follows actual graph edges.

    Distinguish the three levels:

        OUTER FOR
        → FIND a disconnected component

        WHILE + QUEUE
        → TRAVERSE that component

        INNER FOR
        → FOLLOW current node's neighbors
    ------------------------------------------------------------------------------------------------
    */

    /*
    ------------------------------------------------------------------------------------------------
    7. WHY color[next] = -color[current]?
    ------------------------------------------------------------------------------------------------

    Every edge requires opposite colors.

        current =  1  → next = -1
        current = -1  → next =  1

    So:

        color[next] = -color[current];

    is the complete propagation rule.
    ------------------------------------------------------------------------------------------------
    */

    /*
    ------------------------------------------------------------------------------------------------
    8. WHY CHECK AN ALREADY-COLORED NEIGHBOR?
    ------------------------------------------------------------------------------------------------

    An already-colored node may have been reached through another path.

    We do NOT recolor it.
    We validate consistency.

        UNVISITED → DISCOVER
        VISITED   → VALIDATE

    If:

        color[next] == color[current]

    then an edge connects equal colors.

    Bipartite invariant is broken → return false.

    This is how an odd-cycle contradiction eventually appears.
    ------------------------------------------------------------------------------------------------
    */

    /*
    ================================================================================================
    30-SECOND RECALL
    ================================================================================================

    TRIGGER:
    Split nodes into two groups so connected/conflicting nodes are separated.

    PATTERN:
    BFS / DFS + two-coloring

    INVARIANT:
    Every edge connects opposite colors.

    MASTER FLOW:

        SCAN
        → find an unvisited component

        START
        → color its first node
        → add it to worklist

        EXPLORE
        → traverse neighbors

            uncolored
            → assign opposite

            already colored same
            → contradiction

    ONE-LINER:

        "Scan every disconnected component, propagate opposite colors along edges,
         and fail if an edge ever connects equal colors."
    ================================================================================================
    */

    /*
    ================================================================================================
    REUSABLE DISCONNECTED-GRAPH MASTER TEMPLATE
    ================================================================================================

        state[]

        for every start:

            if already seen:
                continue

            initialize new component
            mark start
            add start to worklist

            while worklist not empty:

                current = remove

                for each neighbor:

                    if unseen:
                        assign / mark state
                        add

                    problem-specific validation

    Across related problems, usually only THREE things change:

        1. STATE
        2. HOW NEIGHBORS ARE FOUND
        3. PROBLEM-SPECIFIC ACTION / VALIDATION
    ================================================================================================
    */

    /*
    ================================================================================================
    REINFORCEMENT PROBLEM #1 — POSSIBLE BIPARTITION
    LEETCODE 886
    ================================================================================================

    SAME AS BIPARTITE:
        outer component scan
        BFS / DFS
        +1 / -1 coloring
        same-color conflict

    DIFFERENCE:
        input is given as dislike EDGE PAIRS,
        so we first BUILD the adjacency list.

    Mapping:

        person     → node
        dislike    → undirected edge

    This is the closest direct transfer problem.
    ================================================================================================
    */
    static class PossibleBipartitionSolution {

        public boolean possibleBipartition(int n, int[][] dislikes) {

            List<Integer>[] graph = new ArrayList[n + 1];

            for (int i = 0; i <= n; i++) {
                graph[i] = new ArrayList<>();
            }

            for (int[] edge : dislikes) {

                int a = edge[0];
                int b = edge[1];

                graph[a].add(b);
                graph[b].add(a);
            }

            int[] color = new int[n + 1];

            for (int start = 1; start <= n; start++) {

                if (color[start] != 0) {
                    continue;
                }

                Deque<Integer> queue = new ArrayDeque<>();

                color[start] = 1;
                queue.offer(start);

                while (!queue.isEmpty()) {

                    int current = queue.poll();

                    for (int next : graph[current]) {

                        if (color[next] == 0) {
                            color[next] = -color[current];
                            queue.offer(next);
                        }
                        else if (color[next] == color[current]) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    /*
    ================================================================================================
    REINFORCEMENT PROBLEM #2 — NUMBER OF PROVINCES
    LEETCODE 547
    ================================================================================================

    SAME SKELETON:
        outer scan
        start traversal when unseen
        exhaust one component

    CHANGE:
        state is boolean visited[]
        new component means provinces++

    IMPORTANT INPUT DIFFERENCE:
        this one IS an adjacency MATRIX.

        isConnected[i][j] == 1
        → edge exists between i and j

    Compare carefully:

        Bipartite input:
            graph[current]
            → direct list of neighbors

        Provinces input:
            scan all possible neighbor indices
            → check matrix[current][neighbor]
    ================================================================================================
    */
    static class NumberOfProvincesSolution {

        public int findCircleNum(int[][] isConnected) {

            int n = isConnected.length;
            boolean[] visited = new boolean[n];

            int provinces = 0;

            for (int start = 0; start < n; start++) {

                if (visited[start]) {
                    continue;
                }

                provinces++;

                Deque<Integer> queue = new ArrayDeque<>();

                visited[start] = true;
                queue.offer(start);

                while (!queue.isEmpty()) {

                    int current = queue.poll();

                    for (int next = 0; next < n; next++) {

                        if (isConnected[current][next] == 1 && !visited[next]) {
                            visited[next] = true;
                            queue.offer(next);
                        }
                    }
                }
            }

            return provinces;
        }
    }

    /*
    ================================================================================================
    REINFORCEMENT PROBLEM #3 — NUMBER OF ISLANDS
    LEETCODE 200
    ================================================================================================

    SAME COMPONENT PATTERN:

        scan every possible node/cell

        unseen valid node
        → new component
        → BFS / DFS until component exhausted

    DIFFERENCE:

        graph nodes are IMPLICIT grid cells.

        We do not have:
            graph[current]

        We GENERATE neighbors using directions.

    Mapping:

        graph vertex     → grid cell
        graph edge       → adjacent land cell
        component count  → island count
    ================================================================================================
    */
    static class NumberOfIslandsSolution {

        private static final int[][] DIRECTIONS = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        public int numIslands(char[][] grid) {

            if (grid == null || grid.length == 0) {
                return 0;
            }

            int rows = grid.length;
            int cols = grid[0].length;

            int islands = 0;

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (grid[row][col] != '1') {
                        continue;
                    }

                    islands++;

                    Deque<int[]> queue = new ArrayDeque<>();

                    grid[row][col] = '0';
                    queue.offer(new int[]{row, col});

                    while (!queue.isEmpty()) {

                        int[] current = queue.poll();

                        for (int[] direction : DIRECTIONS) {

                            int nextRow = current[0] + direction[0];
                            int nextCol = current[1] + direction[1];

                            if (nextRow < 0 || nextRow >= rows ||
                                nextCol < 0 || nextCol >= cols ||
                                grid[nextRow][nextCol] != '1') {
                                continue;
                            }

                            grid[nextRow][nextCol] = '0';
                            queue.offer(new int[]{nextRow, nextCol});
                        }
                    }
                }
            }

            return islands;
        }
    }

    /*
    ================================================================================================
    REINFORCEMENT PROBLEM #4 — FLOOD FILL
    LEETCODE 733
    ================================================================================================

    SAME:
        traverse connected neighbors
        mark as soon as discovered
        queue / stack / recursion all possible

    KEY DIFFERENCE:
        one starting source is GIVEN.

    Therefore we do NOT need:

        for every node:
            if unseen:
                start traversal

    We only traverse the component containing (sr, sc).

    REUSABLE DFS PREDICATE:

        not originalColor
        → stop

        originalColor
        → paint + explore

    Important:
        if originalColor == newColor,
        return before DFS/BFS starts.
    ================================================================================================
    */
    static class FloodFillSolution {

        public int[][] floodFill(int[][] image, int sr, int sc, int color) {

            int originalColor = image[sr][sc];

            if (originalColor == color) {
                return image;
            }

            dfs(image, sr, sc, originalColor, color);

            return image;
        }

        private void dfs(
                int[][] image,
                int row,
                int col,
                int originalColor,
                int newColor
        ) {

            if (row < 0 || row >= image.length ||
                col < 0 || col >= image[0].length ||
                image[row][col] != originalColor) {
                return;
            }

            image[row][col] = newColor;

            dfs(image, row + 1, col, originalColor, newColor);
            dfs(image, row - 1, col, originalColor, newColor);
            dfs(image, row, col + 1, originalColor, newColor);
            dfs(image, row, col - 1, originalColor, newColor);
        }
    }

    /*
    ================================================================================================
    REINFORCEMENT PROBLEM #5 — CLONE GRAPH
    LEETCODE 133
    ================================================================================================

    SAME:
        traverse actual neighbors
        need memory to avoid reprocessing cycles

    DIFFERENCE:
        state is NOT boolean visited[]
        and NOT color[].

        state becomes:

            Map<OriginalNode, CloneNode>

    Why?

        We need both:
            "already visited?"
        AND:
            "where is its clone?"

    Critical invariant:

        register clone BEFORE exploring neighbors.

    Otherwise a cycle can recurse forever.
    ================================================================================================
    */
    static class CloneGraphSolution {

        static class Node {

            int val;
            List<Node> neighbors = new ArrayList<>();

            Node(int val) {
                this.val = val;
            }
        }

        public Node cloneGraph(Node node) {

            if (node == null) {
                return null;
            }

            Map<Node, Node> oldToNew = new HashMap<>();

            return dfs(node, oldToNew);
        }

        private Node dfs(Node node, Map<Node, Node> oldToNew) {

            if (oldToNew.containsKey(node)) {
                return oldToNew.get(node);
            }

            Node clone = new Node(node.val);

            oldToNew.put(node, clone);

            for (Node neighbor : node.neighbors) {
                clone.neighbors.add(dfs(neighbor, oldToNew));
            }

            return clone;
        }
    }

    /*
    ================================================================================================
    REINFORCEMENT PROBLEM #6 — GRAPH VALID TREE
    LEETCODE 261
    ================================================================================================

    SAME:
        graph traversal
        visited state
        connectivity reasoning

    DIFFERENCE IN QUESTION:

        Bipartite:
            every edge must connect opposite colors

        Valid Tree:
            graph must be connected AND acyclic

    Very useful distinction:

        TREE
        → no cycles at all

        BIPARTITE
        → even cycles are allowed
        → odd cycles are forbidden

    Shortcut:

        an undirected graph with n nodes is a tree iff:
            edges == n - 1
            AND
            all n nodes are connected
    ================================================================================================
    */
    static class GraphValidTreeSolution {

        public boolean validTree(int n, int[][] edges) {

            if (n == 0) {
                return false;
            }

            if (edges.length != n - 1) {
                return false;
            }

            List<Integer>[] graph = new ArrayList[n];

            for (int i = 0; i < n; i++) {
                graph[i] = new ArrayList<>();
            }

            for (int[] edge : edges) {

                int a = edge[0];
                int b = edge[1];

                graph[a].add(b);
                graph[b].add(a);
            }

            boolean[] visited = new boolean[n];

            Deque<Integer> queue = new ArrayDeque<>();
            queue.offer(0);
            visited[0] = true;

            int visitedCount = 0;

            while (!queue.isEmpty()) {

                int current = queue.poll();
                visitedCount++;

                for (int next : graph[current]) {

                    if (!visited[next]) {
                        visited[next] = true;
                        queue.offer(next);
                    }
                }
            }

            return visitedCount == n;
        }
    }

    /*
    ================================================================================================
    REINFORCEMENT PROBLEM #7 — COURSE SCHEDULE
    LEETCODE 207
    ================================================================================================

    RELATED BECAUSE:
        graph traversal / graph state / cycle reasoning

    BUT NOT THE SAME FAMILY.

    Course Schedule:
        DIRECTED graph
        prerequisite ordering
        detect directed cycle

    Preferred reusable pattern:
        Kahn's Algorithm / Topological Sort

    State changes:

        Bipartite:
            color[]

        Course Schedule:
            indegree[]

    Worklist meaning changes:

        Bipartite queue:
            discovered component nodes waiting to process

        Kahn queue:
            courses whose remaining indegree is ZERO

    Pattern boundary matters:
        do not force the bipartite template onto every graph problem.
    ================================================================================================
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

            Deque<Integer> queue = new ArrayDeque<>();

            for (int course = 0; course < numCourses; course++) {

                if (indegree[course] == 0) {
                    queue.offer(course);
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
    ================================================================================================
    REINFORCEMENT PROBLEM #8 — REDUNDANT CONNECTION
    LEETCODE 684
    ================================================================================================

    RELATED:
        undirected graph
        cycle reasoning

    DIFFERENCE:

        Bipartite asks:
            "Does an ODD-cycle contradiction exist?"

        Redundant Connection asks:
            "Which edge creates ANY cycle?"

    Preferred pattern:
        Union Find

    Reusable Union Find invariant:

        if find(a) == find(b)
        → a and b were already connected
        → adding this edge creates a cycle
    ================================================================================================
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

            int find(int node) {

                if (parent[node] != node) {
                    parent[node] = find(parent[node]);
                }

                return parent[node];
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

            UnionFind unionFind = new UnionFind(edges.length);

            for (int[] edge : edges) {

                if (!unionFind.union(edge[0], edge[1])) {
                    return edge;
                }
            }

            return new int[0];
        }
    }

    /*
    ================================================================================================
    RELATED-PATTERN TRANSFER TABLE
    ================================================================================================

    Problem                  State                     Outer Scan?   Neighbor Source        Core Goal
    ------------------------------------------------------------------------------------------------
    Bipartite                color[]                   YES           adjacency list        opposite color
    Possible Bipartition     color[]                   YES           built adjacency list  opposite color
    Provinces                visited[]                 YES           matrix row            count components
    Number of Islands        grid mutation/visited     YES           4 directions          count components
    Flood Fill               image mutation            NO            4 directions          mutate one component
    Clone Graph              Map<old,new>              usually NO*   node.neighbors        copy topology
    Valid Tree               visited[]                 NO**          adjacency list        connected + acyclic
    Course Schedule          indegree[]                different     directed adjacency    topological completion
    Redundant Connection     parent[] / rank[]         N/A           edge list             detect cycle edge

    * Clone Graph starts from the supplied node and clones what is reachable from it.
    ** With the n-1 edge-count shortcut, one traversal from node 0 is enough to verify connectivity.

    High-value recognition rule:

        SAME "graph" does NOT mean SAME pattern.

    First ask:

        What STATE must survive across visits?
        What does the WORKLIST represent?
        What condition makes the answer fail/succeed?
    ================================================================================================
    */

    /*
    ================================================================================================
    USEFUL ALTERNATIVE — DFS TWO-COLORING
    ================================================================================================

    SAME invariant.
    Only traversal mechanism changes.

    Do not learn separate BFS and DFS bipartite algorithms.

    Learn ONE rule:

        uncolored neighbor
        → opposite color

        already-colored same-color neighbor
        → fail
    ================================================================================================
    */
    static class DFSSolution {

        public boolean isBipartite(int[][] graph) {

            if (graph == null) {
                return false;
            }

            int[] color = new int[graph.length];

            for (int start = 0; start < graph.length; start++) {

                if (color[start] != 0) {
                    continue;
                }

                color[start] = 1;

                if (!dfs(graph, color, start)) {
                    return false;
                }
            }

            return true;
        }

        private boolean dfs(int[][] graph, int[] color, int current) {

            for (int next : graph[current]) {

                if (color[next] == 0) {

                    color[next] = -color[current];

                    if (!dfs(graph, color, next)) {
                        return false;
                    }
                }
                else if (color[next] == color[current]) {

                    return false;
                }
            }

            return true;
        }
    }

    /*
    ================================================================================================
    COMMON TRAPS
    ================================================================================================

    1. Start only from node 0
       → disconnected components can be missed.

    2. Treat graph[0].length as number of columns
       → adjacency list is not a rectangular grid.

    3. Return false when graph[0].length == 0
       → isolated node 0 is perfectly legal.

    4. Use only visited[]
       → remembers reachability but not color/parity.

    5. Reject every cycle
       → even cycles are bipartite.

    6. Recolor an already-colored node
       → breaks previously established constraints.

    7. Think BFS is mandatory
       → DFS works with the same invariant.

    8. See int[][] and assume one representation
       → int[][] can represent grid, adjacency matrix, adjacency lists, or edge pairs.

    9. See "graph" and force one universal graph template
       → the state/invariant determines the pattern.
    ================================================================================================
    */

    /*
    ================================================================================================
    SELF-VERIFYING TEST UTILITIES
    ================================================================================================
    */

    private static void assertTrue(boolean condition, String message) {

        if (!condition) {
            throw new AssertionError("Expected true: " + message);
        }
    }

    private static void assertFalse(boolean condition, String message) {

        if (condition) {
            throw new AssertionError("Expected false: " + message);
        }
    }

    private static void assertEquals(int expected, int actual, String message) {

        if (expected != actual) {
            throw new AssertionError(
                    message
                    + " | expected = " + expected
                    + ", actual = " + actual
            );
        }
    }

    private static void assertArrayEquals(int[] expected, int[] actual, String message) {

        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(
                    message
                    + " | expected = " + Arrays.toString(expected)
                    + ", actual = " + Arrays.toString(actual)
            );
        }
    }

    private static void assertMatrixEquals(int[][] expected, int[][] actual, String message) {

        if (!Arrays.deepEquals(expected, actual)) {
            throw new AssertionError(
                    message
                    + " | expected = " + Arrays.deepToString(expected)
                    + ", actual = " + Arrays.deepToString(actual)
            );
        }
    }

    /*
    ================================================================================================
    PRIMARY PROBLEM TESTS
    ================================================================================================
    */

    private static void runPrimaryTests() {

        Solution bfs = new Solution();
        DFSSolution dfs = new DFSSolution();

        int[][] evenCycle = {
                {1, 3},
                {0, 2},
                {1, 3},
                {0, 2}
        };

        assertTrue(bfs.isBipartite(evenCycle), "even cycle BFS");
        assertTrue(dfs.isBipartite(evenCycle), "even cycle DFS");


        int[][] triangle = {
                {1, 2},
                {0, 2},
                {0, 1}
        };

        assertFalse(bfs.isBipartite(triangle), "triangle BFS");
        assertFalse(dfs.isBipartite(triangle), "triangle DFS");


        /*
        Valid component + disconnected odd-cycle component.

        Proves why the outer loop is required.
        */
        int[][] disconnectedOddCycle = {
                {1},
                {0},
                {3, 4},
                {2, 4},
                {2, 3}
        };

        assertFalse(
                bfs.isBipartite(disconnectedOddCycle),
                "disconnected odd-cycle component BFS"
        );

        assertFalse(
                dfs.isBipartite(disconnectedOddCycle),
                "disconnected odd-cycle component DFS"
        );


        /*
        Regression case from discussion:

            [[], [3], [], [1], []]

        graph[0].length == 0 only means node 0 is isolated.
        */
        int[][] isolatedFirstNode = {
                {},
                {3},
                {},
                {1},
                {}
        };

        assertTrue(
                bfs.isBipartite(isolatedFirstNode),
                "isolated first node BFS"
        );

        assertTrue(
                dfs.isBipartite(isolatedFirstNode),
                "isolated first node DFS"
        );


        int[][] isolatedNodes = {
                {},
                {},
                {}
        };

        assertTrue(bfs.isBipartite(isolatedNodes), "all isolated BFS");
        assertTrue(dfs.isBipartite(isolatedNodes), "all isolated DFS");


        int[][] tree = {
                {1, 2},
                {0, 3},
                {0},
                {1}
        };

        assertTrue(bfs.isBipartite(tree), "tree BFS");
        assertTrue(dfs.isBipartite(tree), "tree DFS");


        int[][] singleNode = {
                {}
        };

        assertTrue(bfs.isBipartite(singleNode), "single node BFS");
        assertTrue(dfs.isBipartite(singleNode), "single node DFS");


        int[][] empty = {};

        assertTrue(bfs.isBipartite(empty), "empty graph BFS");
        assertTrue(dfs.isBipartite(empty), "empty graph DFS");

        System.out.println("✅ Primary Bipartite tests passed.");
    }

    /*
    ================================================================================================
    REINFORCEMENT TESTS
    ================================================================================================
    */

    private static void runPossibleBipartitionTests() {

        PossibleBipartitionSolution solver = new PossibleBipartitionSolution();

        int[][] valid = {
                {1, 2},
                {1, 3},
                {2, 4}
        };

        assertTrue(
                solver.possibleBipartition(4, valid),
                "possible bipartition valid case"
        );

        int[][] invalid = {
                {1, 2},
                {1, 3},
                {2, 3}
        };

        assertFalse(
                solver.possibleBipartition(3, invalid),
                "possible bipartition odd cycle"
        );

        System.out.println("✅ Possible Bipartition tests passed.");
    }

    private static void runProvinceTests() {

        NumberOfProvincesSolution solver = new NumberOfProvincesSolution();

        int[][] matrix1 = {
                {1, 1, 0},
                {1, 1, 0},
                {0, 0, 1}
        };

        assertEquals(
                2,
                solver.findCircleNum(matrix1),
                "two provinces"
        );

        int[][] matrix2 = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };

        assertEquals(
                3,
                solver.findCircleNum(matrix2),
                "three isolated provinces"
        );

        System.out.println("✅ Number Of Provinces tests passed.");
    }

    private static void runIslandTests() {

        NumberOfIslandsSolution solver = new NumberOfIslandsSolution();

        char[][] grid = {
                {'1', '1', '0', '0'},
                {'1', '0', '0', '1'},
                {'0', '0', '1', '1'}
        };

        assertEquals(
                2,
                solver.numIslands(grid),
                "two islands"
        );

        System.out.println("✅ Number Of Islands tests passed.");
    }

    private static void runFloodFillTests() {

        FloodFillSolution solver = new FloodFillSolution();

        int[][] image = {
                {1, 1, 1},
                {1, 1, 0},
                {1, 0, 1}
        };

        int[][] expected = {
                {2, 2, 2},
                {2, 2, 0},
                {2, 0, 1}
        };

        assertMatrixEquals(
                expected,
                solver.floodFill(image, 1, 1, 2),
                "flood fill connected component"
        );

        int[][] sameColor = {
                {1, 1},
                {1, 0}
        };

        int[][] sameExpected = {
                {1, 1},
                {1, 0}
        };

        assertMatrixEquals(
                sameExpected,
                solver.floodFill(sameColor, 0, 0, 1),
                "flood fill originalColor == newColor"
        );

        System.out.println("✅ Flood Fill tests passed.");
    }

    private static void runCloneGraphTests() {

        CloneGraphSolution.Node one = new CloneGraphSolution.Node(1);
        CloneGraphSolution.Node two = new CloneGraphSolution.Node(2);

        one.neighbors.add(two);
        two.neighbors.add(one);

        CloneGraphSolution solver = new CloneGraphSolution();
        CloneGraphSolution.Node clone = solver.cloneGraph(one);

        assertTrue(clone != one, "clone must be a different object");
        assertEquals(1, clone.val, "clone root value");
        assertEquals(1, clone.neighbors.size(), "clone root neighbor count");
        assertEquals(2, clone.neighbors.get(0).val, "clone neighbor value");
        assertTrue(
                clone.neighbors.get(0) != two,
                "neighbor must also be deeply cloned"
        );
        assertTrue(
                clone.neighbors.get(0).neighbors.get(0) == clone,
                "cycle should point back to cloned root"
        );

        System.out.println("✅ Clone Graph tests passed.");
    }

    private static void runValidTreeTests() {

        GraphValidTreeSolution solver = new GraphValidTreeSolution();

        int[][] valid = {
                {0, 1},
                {0, 2},
                {0, 3},
                {1, 4}
        };

        assertTrue(
                solver.validTree(5, valid),
                "valid tree"
        );

        int[][] invalid = {
                {0, 1},
                {1, 2},
                {2, 3},
                {1, 3},
                {1, 4}
        };

        assertFalse(
                solver.validTree(5, invalid),
                "cycle / too many edges should fail tree"
        );

        System.out.println("✅ Graph Valid Tree tests passed.");
    }

    private static void runCourseScheduleTests() {

        CourseScheduleSolution solver = new CourseScheduleSolution();

        assertTrue(
                solver.canFinish(
                        2,
                        new int[][]{{1, 0}}
                ),
                "simple course dependency"
        );

        assertFalse(
                solver.canFinish(
                        2,
                        new int[][]{{1, 0}, {0, 1}}
                ),
                "course cycle"
        );

        System.out.println("✅ Course Schedule tests passed.");
    }

    private static void runRedundantConnectionTests() {

        RedundantConnectionSolution solver = new RedundantConnectionSolution();

        int[][] edges = {
                {1, 2},
                {1, 3},
                {2, 3}
        };

        assertArrayEquals(
                new int[]{2, 3},
                solver.findRedundantConnection(edges),
                "redundant connection"
        );

        System.out.println("✅ Redundant Connection tests passed.");
    }

    /*
    ================================================================================================
    FINAL RETENTION CARD
    ================================================================================================

    PRIMARY BIPARTITE:

        OUTER FOR
        → find disconnected component

        QUEUE / STACK / RECURSION
        → traverse that component

        INNER FOR
        → follow actual edges

        UNCOLORED
        → opposite color

        ALREADY COLORED SAME
        → false


    MASTER GRAPH TRANSFER QUESTIONS:

        1. What STATE must I remember?
        2. How do I get NEIGHBORS?
        3. Do I need an OUTER component scan?
        4. What does the WORKLIST mean?
        5. What condition is the problem asking me to VALIDATE?


    CORE DISTINCTIONS:

        Bipartite
        → odd-cycle / parity contradiction

        Components / Provinces / Islands
        → reachability groups

        Flood Fill
        → mutate one supplied component

        Clone Graph
        → old → new mapping

        Valid Tree
        → connected + acyclic

        Course Schedule
        → directed topological dependency

        Redundant Connection
        → any undirected cycle edge


    ONE SENTENCE:

        "Graph traversal is the skeleton; the remembered state and invariant determine the problem."
    ================================================================================================
    */

    public static void main(String[] args) {

        runPrimaryTests();

        runPossibleBipartitionTests();
        runProvinceTests();
        runIslandTests();
        runFloodFillTests();
        runCloneGraphTests();
        runValidTreeTests();
        runCourseScheduleTests();
        runRedundantConnectionTests();

        System.out.println();
        System.out.println("================================================================================");
        System.out.println("✅ ALL GraphBipartiteV3 TESTS PASSED");
        System.out.println("================================================================================");
    }
}
