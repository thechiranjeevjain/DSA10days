package org.chijai.day8.session2;


import java.util.*;

/**
 * ============================================================================
 * 743. Network Delay Time
 * LeetCode Hardness: Medium
 * Official Link:
 * https://leetcode.com/problems/network-delay-time/
 *
 * Tags:
 * Graph, Shortest Path, Heap (Priority Queue), Dijkstra, BFS
 * ============================================================================
 *
 * 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
 *
 * You are given a network of n nodes, labeled from 1 to n.
 *
 * You are also given times, a list of travel times as directed edges
 * times[i] = (ui, vi, wi), where ui is the source node,
 * vi is the target node, and wi is the time it takes for a signal
 * to travel from source to target.
 *
 * We will send a signal from a given node k.
 *
 * Return the minimum time it takes for all the n nodes to receive the signal.
 * If it is impossible for all the n nodes to receive the signal, return -1.
 *
 * Example 1:
 *
 * Input:
 * times = [[2,1,1],[2,3,1],[3,4,1]]
 * n = 4
 * k = 2
 *
 * Output:
 * 2
 *
 * Explanation:
 * Signal reaches:
 * Node 1 in 1 unit
 * Node 3 in 1 unit
 * Node 4 in 2 units
 *
 * So answer = max shortest distance = 2.
 *
 * Example 2:
 *
 * Input:
 * times = [[1,2,1]]
 * n = 2
 * k = 1
 *
 * Output:
 * 1
 *
 * Example 3:
 *
 * Input:
 * times = [[1,2,1]]
 * n = 2
 * k = 2
 *
 * Output:
 * -1
 *
 * Constraints:
 *
 * 1 <= k <= n <= 100
 * 1 <= times.length <= 6000
 * times[i].length == 3
 * 1 <= ui, vi <= n
 * ui != vi
 * 0 <= wi <= 100
 * All the pairs (ui, vi) are unique.
 * (i.e., no multiple edges.)
 *
 * ============================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern Name:
 * Dijkstra's Shortest Path Algorithm
 *
 * Problem Archetype:
 * "Single Source Shortest Path on Weighted Directed Graph
 * with Non-Negative Weights"
 *
 * 🟢 CORE INVARIANT
 *
 * Once a node is removed from the min-heap with the smallest distance,
 * its shortest distance is finalized forever.
 *
 * Why it works:
 *
 * All edge weights are non-negative.
 * Therefore:
 * any future path reaching this node must be >= current shortest path.
 *
 * So:
 * first finalized distance is optimal.
 *
 * When to use:
 *
 * Use Dijkstra when:
 *
 * • weighted graph
 * • non-negative weights
 * • shortest path needed
 * • single source
 *
 * Recognition Signals:
 *
 * • "minimum time"
 * • "minimum cost"
 * • "minimum distance"
 * • weighted graph
 * • shortest route
 * • signal propagation
 *
 * Difference vs BFS:
 *
 * BFS:
 * • only for equal weights / unweighted graph
 *
 * Dijkstra:
 * • handles varying non-negative weights
 *
 * Difference vs Bellman-Ford:
 *
 * Bellman-Ford:
 * • supports negative weights
 * • slower
 *
 * Dijkstra:
 * • faster
 * • requires non-negative weights
 *
 * ============================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * Mental Model:
 *
 * Imagine signal spreading outward from source node.
 *
 * The priority queue always expands:
 * the currently closest reachable node.
 *
 * Like ripples expanding in increasing time order.
 *
 * ----------------------------------------------------------------------------
 * 🟢 INVARIANTS
 * ----------------------------------------------------------------------------
 *
 * Invariant 1:
 *
 * dist[node] always stores the best known distance discovered so far.
 *
 * Invariant 2:
 *
 * When a node is popped from minHeap with distance d,
 * and d == dist[node],
 * then d is the final shortest path.
 *
 * Invariant 3:
 *
 * Relaxation only improves distances:
 *
 * if current + weight < knownDistance
 *     update
 *
 * Invariant 4:
 *
 * Heap may contain stale entries.
 *
 * We skip them using:
 *
 * if (currDist > dist[node]) continue;
 *
 * ----------------------------------------------------------------------------
 * Meaning of Variables
 * ----------------------------------------------------------------------------
 *
 * graph:
 * adjacency list
 *
 * dist[i]:
 * shortest known distance from source k to node i
 *
 * minHeap:
 * expands nodes in increasing shortest-distance order
 *
 * ----------------------------------------------------------------------------
 * Allowed Moves
 * ----------------------------------------------------------------------------
 *
 * From current node:
 *
 * try all outgoing edges
 *
 * relax neighbors
 *
 * ----------------------------------------------------------------------------
 * Forbidden Moves
 * ----------------------------------------------------------------------------
 *
 * Never finalize node twice.
 *
 * Never relax using stale heap state blindly.
 *
 * Never use BFS when weights vary.
 *
 * ----------------------------------------------------------------------------
 * Termination Logic
 * ----------------------------------------------------------------------------
 *
 * Heap becomes empty:
 * all reachable shortest paths finalized.
 *
 * Final answer:
 *
 * maximum shortest distance among all nodes
 *
 * If any node unreachable:
 * return -1
 *
 * ----------------------------------------------------------------------------
 * Why Naive Approaches Fail
 * ----------------------------------------------------------------------------
 *
 * BFS fails:
 *
 * because edge weights differ.
 *
 * DFS fails:
 *
 * explores arbitrary deep paths first,
 * not shortest paths.
 *
 * Greedy local choice fails:
 *
 * shortest edge now
 * does NOT imply globally shortest route.
 *
 * ============================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================================
 *
 * ❌ Wrong Approach 1:
 * Plain BFS
 *
 * Why it seems correct:
 * "We are spreading level by level."
 *
 * Invariant violation:
 * BFS assumes equal edge cost.
 *
 * Counterexample:
 *
 * 1 -> 2 (100)
 * 1 -> 3 (1)
 * 3 -> 2 (1)
 *
 * BFS may finalize node 2 incorrectly.
 *
 * ----------------------------------------------------------------------------
 * ❌ Wrong Approach 2:
 * DFS with pruning
 *
 * Why it seems correct:
 * "Eventually all paths explored."
 *
 * Problem:
 * exponential exploration
 *
 * no guarantee shortest finalized early
 *
 * ----------------------------------------------------------------------------
 * ❌ Wrong Approach 3:
 * Forgetting stale-entry check
 *
 * Why it seems harmless:
 * "Heap already sorted."
 *
 * Problem:
 * old larger distances remain in heap.
 *
 * Can cause:
 * repeated unnecessary relaxations
 *
 * ----------------------------------------------------------------------------
 * ❌ Wrong Approach 4:
 * Using visited[] too early
 *
 * Trap:
 *
 * marking visited during insertion into heap.
 *
 * WRONG.
 *
 * Node may later get shorter path.
 *
 * Must finalize only when popped optimally.
 *
 * ============================================================================
 * ⚙️ HOW TO PHYSICALLY ASSEMBLE THE CODE
 * ============================================================================
 *
 * 🛠️ IMPLEMENTATION BLUEPRINT
 *
 * Step 1:
 * Build adjacency list.
 *
 * Step 2:
 * Initialize distances to INF.
 *
 * Step 3:
 * dist[k] = 0
 *
 * Step 4:
 * Create minHeap ordered by distance.
 *
 * Step 5:
 * Push source node.
 *
 * Step 6:
 * While heap not empty:
 *
 *     pop smallest distance state
 *
 * Step 7:
 * Skip stale states.
 *
 * Step 8:
 * For every neighbor:
 *
 *     newDist = currDist + weight
 *
 * Step 9:
 * If better:
 *
 *     update dist
 *     push into heap
 *
 * Step 10:
 * Scan dist array:
 *
 *     if INF => unreachable
 *
 * Step 11:
 * return maximum distance
 *
 * ============================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE (MEMORY SCAFFOLD)
 * ============================================================================
 *
 * build graph
 *
 * dist = INF
 * dist[source] = 0
 *
 * push(source, 0)
 *
 * while heap not empty:
 *
 *     pop smallest
 *
 *     skip stale
 *
 *     for neighbors:
 *
 *         if shorter:
 *             update
 *             push
 *
 * if unreachable:
 *     return -1
 *
 * return maxDistance
 *
 * ============================================================================
 * PRIMARY PROBLEM — SOLUTION CLASSES
 * ============================================================================
 */
public class NetworkDelayTime {

    /*
     * =========================================================================
     * Shared Structures
     * =========================================================================
     */

    static class Edge {
        int to;
        int weight;

        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    static class NodeState implements Comparable<NodeState> {
        int node;
        int distance;

        NodeState(int node, int distance) {
            this.node = node;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeState other) {
            return Integer.compare(this.distance, other.distance);
        }

    }

    /*
     * =========================================================================
     * 1. BRUTE FORCE
     * =========================================================================
     *
     * Core Idea:
     * Repeatedly relax every edge.
     *
     * This is essentially Bellman-Ford style relaxation.
     *
     * 🟢 Invariant:
     *
     * After i iterations,
     * shortest paths using at most i edges are correct.
     *
     * Limitation Fixed:
     *
     * Works even with negative weights.
     *
     * Limitation:
     *
     * Slower than Dijkstra.
     *
     * Time:
     * O(V * E)
     *
     * Space:
     * O(V)
     *
     * Interview Preference:
     * Rarely preferred here.
     */

    static class BruteForceBellmanFord {

        public int networkDelayTime(int[][] times, int n, int k) {

            int INF = Integer.MAX_VALUE / 2;

            int[] dist = new int[n + 1];
            Arrays.fill(dist, INF);

            dist[k] = 0;

            // Relax all edges n-1 times.
            for (int iteration = 1; iteration <= n - 1; iteration++) {

                boolean updated = false;

                for (int[] edge : times) {

                    int u = edge[0];
                    int v = edge[1];
                    int w = edge[2];

                    // If source unreachable, skip.
                    if (dist[u] == INF) {
                        continue;
                    }

                    // Relaxation step.
                    if (dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                        updated = true;
                    }
                }

                // Optimization:
                // if no updates, shortest paths stabilized.
                if (!updated) {
                    break;
                }
            }

            int maxTime = 0;

            for (int node = 1; node <= n; node++) {

                if (dist[node] == INF) {
                    return -1;
                }

                maxTime = Math.max(maxTime, dist[node]);
            }

            return maxTime;
        }
    }

    /*
     * =========================================================================
     * 2. IMPROVED
     * =========================================================================
     *
     * Core Idea:
     * Dijkstra using adjacency matrix.
     *
     * No heap.
     *
     * Repeatedly choose minimum unfixed node manually.
     *
     * 🟢 Invariant:
     *
     * Chosen node has finalized shortest path.
     *
     * Limitation Fixed:
     *
     * Faster than Bellman-Ford.
     *
     * Limitation:
     *
     * O(V^2)
     *
     * Time:
     * O(V^2 + E)
     *
     * Space:
     * O(V^2)
     *
     * Interview Preference:
     * Acceptable for small constraints.
     */

    static class ImprovedDijkstraMatrix {

        public int networkDelayTime(int[][] times, int n, int k) {

            int INF = Integer.MAX_VALUE / 2;

            int[][] graph = new int[n + 1][n + 1];

            for (int i = 1; i <= n; i++) {
                Arrays.fill(graph[i], INF);
            }

            for (int[] edge : times) {

                int u = edge[0];
                int v = edge[1];
                int w = edge[2];

                graph[u][v] = w;
            }

            int[] dist = new int[n + 1];
            Arrays.fill(dist, INF);

            boolean[] finalized = new boolean[n + 1];

            dist[k] = 0;

            for (int iteration = 1; iteration <= n; iteration++) {

                int currNode = -1;
                int currMinDist = INF;

                // Find closest unfinished node.
                for (int node = 1; node <= n; node++) {

                    if (!finalized[node] && dist[node] < currMinDist) {

                        currMinDist = dist[node];
                        currNode = node;
                    }
                }

                // No more reachable nodes.
                if (currNode == -1) {
                    break;
                }

                // Finalize shortest path.
                finalized[currNode] = true;

                // Relax outgoing edges.
                for (int neighbor = 1; neighbor <= n; neighbor++) {

                    if (graph[currNode][neighbor] == INF) {
                        continue;
                    }

                    int newDist = dist[currNode] + graph[currNode][neighbor];

                    if (newDist < dist[neighbor]) {
                        dist[neighbor] = newDist;
                    }
                }
            }

            int answer = 0;

            for (int node = 1; node <= n; node++) {

                if (dist[node] == INF) {
                    return -1;
                }

                answer = Math.max(answer, dist[node]);
            }

            return answer;
        }
    }

    /*
     * =========================================================================
     * 3. OPTIMAL (INTERVIEW-PREFERRED)
     * =========================================================================
     *
     * Core Idea:
     * Dijkstra + Min Heap + Adjacency List
     *
     * 🟢 Invariant:
     *
     * First valid pop from heap finalizes shortest path.
     *
     * Limitation Fixed:
     *
     * Avoids O(V^2) node scanning.
     *
     * Time:
     * O((V + E) log V)
     *
     * Space:
     * O(V + E)
     *
     * Interview Preference:
     * BEST solution.
     */

    static class OptimalDijkstraHeap {

        public int networkDelayTime(int[][] times, int n, int k) {

            /*
             * LIVE CODING NARRATION:
             * Build adjacency list first for efficient traversal.
             */
            List<List<Edge>> graph = buildGraph(times, n);

            int INF = Integer.MAX_VALUE / 2;

            /*
             * dist[i] stores best known distance from source k.
             */
            int[] dist = new int[n + 1];
            Arrays.fill(dist, INF);

            /*
             * Source reaches itself in zero time.
             */
            dist[k] = 0;

            /*
             * Min-heap expands closest node first.
             */
            PriorityQueue<NodeState> minHeap =
                    new PriorityQueue<>();

            minHeap.offer(new NodeState(k, 0));

            /*
             * Process nodes in increasing shortest-distance order.
             */
            while (!minHeap.isEmpty()) {

                NodeState current = minHeap.poll();

                int currNode = current.node;
                int currDist = current.distance;

                /*
                 * Invariant:
                 * stale heap entries must be ignored.
                 */
                if (currDist > dist[currNode]) {
                    continue;
                }

                /*
                 * Try improving neighbors.
                 */
                for (Edge edge : graph.get(currNode)) {

                    int neighbor = edge.to;
                    int weight = edge.weight;

                    int newDist = currDist + weight;

                    /*
                     * Found shorter path.
                     */
                    if (newDist < dist[neighbor]) {

                        dist[neighbor] = newDist;

                        /*
                         * Push improved state into heap.
                         */
                        minHeap.offer(new NodeState(neighbor, newDist));
                    }
                }
            }

            /*
             * Final answer is maximum shortest-path delay.
             */
            int answer = 0;

            for (int node = 1; node <= n; node++) {

                /*
                 * If unreachable node exists,
                 * signal cannot reach entire graph.
                 */
                if (dist[node] == INF) {
                    return -1;
                }

                answer = Math.max(answer, dist[node]);
            }

            return answer;
        }

        private List<List<Edge>> buildGraph(int[][] times, int n) {

            List<List<Edge>> graph = new ArrayList<>();

            for (int i = 0; i <= n; i++) {
                graph.add(new ArrayList<>());
            }
            for (int[] edge : times) {

                int from = edge[0];
                int to = edge[1];
                int weight = edge[2];

                graph.get(from).add(new Edge(to, weight));
            }

            return graph;
        }
    }

    /*
     * =========================================================================
     * 🟣 INTERVIEW ARTICULATION (NO CODE)
     * =========================================================================
     *
     * "This is a classic single-source shortest path problem
     * on a weighted directed graph with non-negative weights,
     * so Dijkstra is the correct pattern.
     *
     * The core invariant is:
     * once a node is popped optimally from the min-heap,
     * its shortest distance is finalized forever.
     *
     * We repeatedly relax outgoing edges and improve distances.
     *
     * The answer is the maximum finalized shortest path,
     * because the signal must reach ALL nodes.
     *
     * If any node remains unreachable,
     * we return -1."
     *
     * ----------------------------------------------------------------------------
     * What breaks if changed?
     * ----------------------------------------------------------------------------
     *
     * If weights become negative:
     * Dijkstra invariant breaks.
     *
     * If BFS used:
     * weighted distances become incorrect.
     *
     * ----------------------------------------------------------------------------
     * In-place feasibility
     * ----------------------------------------------------------------------------
     *
     * Distances can be updated in-place safely.
     *
     * ----------------------------------------------------------------------------
     * Streaming feasibility
     * ----------------------------------------------------------------------------
     *
     * Dynamic edge updates are harder.
     * Usually rerun Dijkstra.
     *
     * ----------------------------------------------------------------------------
     * When NOT to use Dijkstra
     * ----------------------------------------------------------------------------
     *
     * • negative weights
     * • negative cycles
     * • all-pairs shortest path requirement
     *
     * =========================================================================
     * 🎯 INTERVIEW RECALL SHEET (30-SECOND RECALL)
     * =========================================================================
     *
     * Pattern Trigger:
     * weighted shortest path
     *
     * Core Invariant:
     * first valid heap pop finalizes shortest path
     *
     * Search Target:
     * minimum delay to every node
     *
     * Discard Rule:
     * skip stale heap entries
     *
     * Common Trap:
     * using BFS on weighted graph
     *
     * Edge Cases:
     * unreachable nodes
     * single node
     * disconnected graph
     *
     * Interview One-Liner:
     *
     * "Dijkstra expands nodes in globally increasing shortest-distance order."
     *
     * Re-derivation Cue:
     *
     * "Closest unfinished node becomes permanently optimal."
     *
     * =========================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =========================================================================
     *
     * Variation 1:
     * Undirected graph
     *
     * Add edges both directions.
     *
     * Invariant still valid.
     *
     * ----------------------------------------------------------------------------
     * Variation 2:
     * Count shortest paths
     *
     * Maintain ways[] during relaxations.
     *
     * ----------------------------------------------------------------------------
     * Variation 3:
     * Negative weights
     *
     * Dijkstra fails.
     *
     * Need Bellman-Ford.
     *
     * ----------------------------------------------------------------------------
     * Variation 4:
     * Binary weights {0,1}
     *
     * Use 0-1 BFS instead.
     *
     * ----------------------------------------------------------------------------
     * Pattern Break Signals
     * ----------------------------------------------------------------------------
     *
     * • negative edges
     * • negative cycles
     * • maximizing instead of minimizing
     *
     * =========================================================================
     * ⚫ REINFORCEMENT PROBLEMS
     * =========================================================================
     */

    /*
     * =========================================================================
     * Reinforcement Problem 1
     * 1631. Path With Minimum Effort
     * =========================================================================
     *
     * Summary:
     * Minimize maximum edge effort on path.
     *
     * ⚫ Invariant Mapping:
     *
     * Heap always expands currently best effort path.
     *
     * Key Twist:
     * path cost = max(edge costs), not sum.
     */

    static class PathWithMinimumEffort {

        static class CellState {
            int row;
            int col;
            int effort;

            CellState(int row, int col, int effort) {
                this.row = row;
                this.col = col;
                this.effort = effort;
            }
        }

        public int minimumEffortPath(int[][] heights) {

            int rows = heights.length;
            int cols = heights[0].length;

            int[][] best = new int[rows][cols];

            for (int[] row : best) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }

            PriorityQueue<CellState> minHeap =
                    new PriorityQueue<>(Comparator.comparingInt(a -> a.effort));

            minHeap.offer(new CellState(0, 0, 0));

            best[0][0] = 0;

            int[][] directions = {
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {0, -1}
            };

            while (!minHeap.isEmpty()) {

                CellState current = minHeap.poll();

                int row = current.row;
                int col = current.col;
                int effort = current.effort;

                if (row == rows - 1 && col == cols - 1) {
                    return effort;
                }

                if (effort > best[row][col]) {
                    continue;
                }

                for (int[] dir : directions) {

                    int nextRow = row + dir[0];
                    int nextCol = col + dir[1];

                    if (nextRow < 0 || nextCol < 0 ||
                            nextRow >= rows || nextCol >= cols) {
                        continue;
                    }

                    int edgeCost =
                            Math.abs(heights[row][col] - heights[nextRow][nextCol]);

                    int newEffort = Math.max(effort, edgeCost);

                    if (newEffort < best[nextRow][nextCol]) {

                        best[nextRow][nextCol] = newEffort;

                        minHeap.offer(
                                new CellState(nextRow, nextCol, newEffort)
                        );
                    }
                }
            }

            return 0;
        }
    }

    /*
     * =========================================================================
     * Reinforcement Problem 2
     * 1514. Path with Maximum Probability
     * =========================================================================
     *
     * Summary:
     * Find maximum probability path.
     *
     * ⚫ Invariant Mapping:
     *
     * First valid maxHeap pop finalizes maximum probability.
     */

    static class PathWithMaximumProbability {

        static class ProbabilityState {
            int node;
            double probability;

            ProbabilityState(int node, double probability) {
                this.node = node;
                this.probability = probability;
            }
        }

        public double maxProbability(
                int n,
                int[][] edges,
                double[] succProb,
                int start,
                int end
        ) {

            List<List<ProbabilityState>> graph = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for (int i = 0; i < edges.length; i++) {

                int u = edges[i][0];
                int v = edges[i][1];
                double p = succProb[i];

                graph.get(u).add(new ProbabilityState(v, p));
                graph.get(v).add(new ProbabilityState(u, p));
            }

            double[] best = new double[n];
            best[start] = 1.0;

            PriorityQueue<ProbabilityState> maxHeap =
                    new PriorityQueue<>(
                            (a, b) -> Double.compare(b.probability, a.probability)
                    );

            maxHeap.offer(new ProbabilityState(start, 1.0));

            while (!maxHeap.isEmpty()) {

                ProbabilityState current = maxHeap.poll();

                int node = current.node;
                double probability = current.probability;

                if (node == end) {
                    return probability;
                }

                if (probability < best[node]) {
                    continue;
                }

                for (ProbabilityState neighbor : graph.get(node)) {

                    double newProbability =
                            probability * neighbor.probability;

                    if (newProbability > best[neighbor.node]) {

                        best[neighbor.node] = newProbability;

                        maxHeap.offer(
                                new ProbabilityState(
                                        neighbor.node,
                                        newProbability
                                )
                        );
                    }
                }
            }

            return 0.0;
        }
    }

    /*
     * =========================================================================
     * Reinforcement Problem 3
     * 1976. Number of Ways to Arrive at Destination
     * =========================================================================
     *
     * Summary:
     * Count shortest paths.
     *
     * ⚫ Invariant Mapping:
     *
     * Dijkstra shortest distance invariant remains same.
     *
     * Additional state:
     * ways[node]
     */

    static class NumberOfWaysToArrive {

        static class Pair {
            int node;
            long distance;

            Pair(int node, long distance) {
                this.node = node;
                this.distance = distance;
            }
        }

        public int countPaths(int n, int[][] roads) {

            long MOD = 1_000_000_007L;

            List<List<long[]>> graph = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for (int[] road : roads) {

                int u = road[0];
                int v = road[1];
                int time = road[2];

                graph.get(u).add(new long[]{v, time});
                graph.get(v).add(new long[]{u, time});
            }

            long[] dist = new long[n];
            Arrays.fill(dist, Long.MAX_VALUE);

            long[] ways = new long[n];

            dist[0] = 0;
            ways[0] = 1;

            PriorityQueue<Pair> minHeap =
                    new PriorityQueue<>(Comparator.comparingLong(a -> a.distance));

            minHeap.offer(new Pair(0, 0));

            while (!minHeap.isEmpty()) {

                Pair current = minHeap.poll();

                int node = current.node;
                long distance = current.distance;

                if (distance > dist[node]) {
                    continue;
                }

                for (long[] edge : graph.get(node)) {

                    int neighbor = (int) edge[0];
                    long weight = edge[1];

                    long newDist = distance + weight;

                    if (newDist < dist[neighbor]) {

                        dist[neighbor] = newDist;

                        ways[neighbor] = ways[node];

                        minHeap.offer(new Pair(neighbor, newDist));
                    }
                    else if (newDist == dist[neighbor]) {

                        ways[neighbor] =
                                (ways[neighbor] + ways[node]) % MOD;
                    }
                }
            }

            return (int) ways[n - 1];
        }
    }

    /*
     * =========================================================================
     * 🧩 RELATED PROBLEMS
     * =========================================================================
     */

    /*
     * =========================================================================
     * Related Problem 1
     * 787. Cheapest Flights Within K Stops
     * =========================================================================
     *
     * Same / Modified / Broken Invariant:
     *
     * Modified.
     *
     * State must include:
     * node + stopsUsed
     */

    static class CheapestFlightsWithinKStops {

        static class FlightState {
            int node;
            int cost;
            int stops;

            FlightState(int node, int cost, int stops) {
                this.node = node;
                this.cost = cost;
                this.stops = stops;
            }
        }

        public int findCheapestPrice(
                int n,
                int[][] flights,
                int src,
                int dst,
                int k
        ) {

            List<List<int[]>> graph = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for (int[] flight : flights) {

                graph.get(flight[0]).add(
                        new int[]{flight[1], flight[2]}
                );
            }

            PriorityQueue<FlightState> minHeap =
                    new PriorityQueue<>(Comparator.comparingInt(a -> a.cost));

            minHeap.offer(new FlightState(src, 0, 0));

            int[] bestStops = new int[n];
            Arrays.fill(bestStops, Integer.MAX_VALUE);

            while (!minHeap.isEmpty()) {

                FlightState current = minHeap.poll();

                if (current.node == dst) {
                    return current.cost;
                }

                if (current.stops > k ||
                        current.stops > bestStops[current.node]) {
                    continue;
                }

                bestStops[current.node] = current.stops;

                for (int[] edge : graph.get(current.node)) {

                    int neighbor = edge[0];
                    int price = edge[1];

                    minHeap.offer(
                            new FlightState(
                                    neighbor,
                                    current.cost + price,
                                    current.stops + 1
                            )
                    );
                }
            }

            return -1;
        }
    }

    /*
     * =========================================================================
     * Related Problem 2
     * 778. Swim in Rising Water
     * =========================================================================
     *
     * Modified Invariant:
     *
     * Minimize maximum elevation seen so far.
     */

    static class SwimInRisingWater {

        static class State {
            int row;
            int col;
            int time;

            State(int row, int col, int time) {
                this.row = row;
                this.col = col;
                this.time = time;
            }
        }

        public int swimInWater(int[][] grid) {

            int n = grid.length;

            PriorityQueue<State> minHeap =
                    new PriorityQueue<>(Comparator.comparingInt(a -> a.time));

            boolean[][] visited = new boolean[n][n];

            minHeap.offer(new State(0, 0, grid[0][0]));

            int[][] directions = {
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {0, -1}
            };

            while (!minHeap.isEmpty()) {

                State current = minHeap.poll();

                int row = current.row;
                int col = current.col;

                if (visited[row][col]) {
                    continue;
                }

                visited[row][col] = true;

                if (row == n - 1 && col == n - 1) {
                    return current.time;
                }

                for (int[] dir : directions) {

                    int nextRow = row + dir[0];
                    int nextCol = col + dir[1];

                    if (nextRow < 0 || nextCol < 0 ||
                            nextRow >= n || nextCol >= n ||
                            visited[nextRow][nextCol]) {
                        continue;
                    }

                    minHeap.offer(
                            new State(
                                    nextRow,
                                    nextCol,
                                    Math.max(
                                            current.time,
                                            grid[nextRow][nextCol]
                                    )
                            )
                    );
                }
            }

            return -1;
        }
    }

    /*
     * =========================================================================
     * Related Problem 3
     * 1334. Find the City With the Smallest Number of Neighbors
     * =========================================================================
     *
     * Same / Modified / Broken Invariant:
     *
     * Same Dijkstra invariant repeated from every source.
     */

    static class FindTheCity {

        public int findTheCity(int n, int[][] edges, int distanceThreshold) {

            List<List<int[]>> graph = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for (int[] edge : edges) {

                int u = edge[0];
                int v = edge[1];
                int w = edge[2];

                graph.get(u).add(new int[]{v, w});
                graph.get(v).add(new int[]{u, w});
            }

            int answerCity = -1;
            int minimumReachable = Integer.MAX_VALUE;

            for (int source = 0; source < n; source++) {

                int reachable =
                        countReachable(
                                source,
                                graph,
                                n,
                                distanceThreshold
                        );

                if (reachable <= minimumReachable) {

                    minimumReachable = reachable;
                    answerCity = source;
                }
            }

            return answerCity;
        }

        private int countReachable(
                int source,
                List<List<int[]>> graph,
                int n,
                int threshold
        ) {

            int[] dist = new int[n];
            Arrays.fill(dist, Integer.MAX_VALUE);

            dist[source] = 0;

            PriorityQueue<int[]> minHeap =
                    new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

            minHeap.offer(new int[]{source, 0});

            while (!minHeap.isEmpty()) {

                int[] current = minHeap.poll();

                int node = current[0];
                int distance = current[1];

                if (distance > dist[node]) {
                    continue;
                }

                for (int[] edge : graph.get(node)) {

                    int neighbor = edge[0];
                    int weight = edge[1];

                    int newDist = distance + weight;

                    if (newDist < dist[neighbor]) {

                        dist[neighbor] = newDist;

                        minHeap.offer(
                                new int[]{neighbor, newDist}
                        );
                    }
                }
            }

            int reachable = 0;

            for (int node = 0; node < n; node++) {

                if (node != source &&
                        dist[node] <= threshold) {
                    reachable++;
                }
            }

            return reachable;
        }
    }

    /*
     * =========================================================================
     * 🧠 MASTERY CHECKLIST
     * =========================================================================
     *
     * ✅ Invariant
     *
     * First optimal heap pop finalizes shortest path.
     *
     * ----------------------------------------------------------------------------
     * ✅ Search Target
     *
     * Minimum distance from source to every node.
     *
     * ----------------------------------------------------------------------------
     * ✅ Discard Rule
     *
     * Ignore stale heap entries.
     *
     * ----------------------------------------------------------------------------
     * ✅ Termination Logic
     *
     * Heap empty => all reachable shortest paths finalized.
     *
     * ----------------------------------------------------------------------------
     * ✅ Naive Failure
     *
     * BFS fails for weighted edges.
     *
     * ----------------------------------------------------------------------------
     * ✅ Edge Cases
     *
     * • disconnected graph
     * • single node
     * • zero-weight edges
     * • unreachable nodes
     *
     * ----------------------------------------------------------------------------
     * ✅ Debugging Readiness
     *
     * Check:
     *
     * • stale-entry skipping
     * • adjacency construction
     * • incorrect relaxation condition
     * • INF overflow
     *
     * ----------------------------------------------------------------------------
     * ✅ Variant Readiness
     *
     * Can adapt to:
     *
     * • max probability
     * • minimum effort
     * • constrained shortest path
     *
     * ----------------------------------------------------------------------------
     * ✅ Pattern Boundary
     *
     * Dijkstra fails with negative weights.
     *
     * =========================================================================
     * 🧪 main() + SELF-VERIFYING TESTS
     * =========================================================================
     */

    public static void main(String[] args) {

        OptimalDijkstraHeap solver =
                new OptimalDijkstraHeap();

        /*
         * ---------------------------------------------------------
         * Happy Path
         * Standard shortest path propagation.
         * ---------------------------------------------------------
         */
        {
            int[][] times = {
                    {2, 1, 1},
                    {2, 3, 1},
                    {3, 4, 1}
            };

            int actual = solver.networkDelayTime(times, 4, 2);
            int expected = 2;

            assertEqual(
                    actual,
                    expected,
                    "Happy path: layered propagation"
            );
        }

        /*
         * ---------------------------------------------------------
         * Smallest non-trivial graph.
         * ---------------------------------------------------------
         */
        {
            int[][] times = {
                    {1, 2, 1}
            };

            int actual = solver.networkDelayTime(times, 2, 1);
            int expected = 1;

            assertEqual(
                    actual,
                    expected,
                    "Single directed edge"
            );
        }

        /*
         * ---------------------------------------------------------
         * Unreachable node trap.
         * Must return -1.
         * ---------------------------------------------------------
         */
        {
            int[][] times = {
                    {1, 2, 1}
            };

            int actual = solver.networkDelayTime(times, 2, 2);
            int expected = -1;

            assertEqual(
                    actual,
                    expected,
                    "Disconnected graph"
            );
        }

        /*
         * ---------------------------------------------------------
         * Multiple paths:
         * ensure shortest selected.
         * ---------------------------------------------------------
         */
        {
            int[][] times = {
                    {1, 2, 10},
                    {1, 3, 1},
                    {3, 2, 1}
            };

            int actual = solver.networkDelayTime(times, 3, 1);
            int expected = 2;

            assertEqual(
                    actual,
                    expected,
                    "Indirect shorter route"
            );
        }

        /*
         * ---------------------------------------------------------
         * Zero-weight edge case.
         * ---------------------------------------------------------
         */
        {
            int[][] times = {
                    {1, 2, 0},
                    {2, 3, 0}
            };

            int actual = solver.networkDelayTime(times, 3, 1);
            int expected = 0;

            assertEqual(
                    actual,
                    expected,
                    "Zero-weight edges"
            );
        }

        /*
         * ---------------------------------------------------------
         * Single node graph.
         * ---------------------------------------------------------
         */
        {
            int[][] times = {};

            int actual = solver.networkDelayTime(times, 1, 1);
            int expected = 0;

            assertEqual(
                    actual,
                    expected,
                    "Single node graph"
            );
        }

        /*
         * ---------------------------------------------------------
         * Dense graph stress-style correctness.
         * ---------------------------------------------------------
         */
        {
            int[][] times = {
                    {1, 2, 1},
                    {1, 3, 4},
                    {2, 3, 2},
                    {2, 4, 7},
                    {3, 4, 1}
            };

            int actual = solver.networkDelayTime(times, 4, 1);
            int expected = 4;

            assertEqual(
                    actual,
                    expected,
                    "Dense graph shortest path selection"
            );
        }

        System.out.println();
        System.out.println("All self-verifying tests passed.");

        /*
         * =========================================================================
         * 🧘 FINAL CLOSURE STATEMENT
         * =========================================================================
         */

        System.out.println();
        System.out.println("I understand the invariant.");
        System.out.println("I can re-derive the solution.");
        System.out.println(
                "I can physically reconstruct the implementation under pressure."
        );
        System.out.println("This chapter is complete.");
    }

    /*
     * =========================================================================
     * Assertion Utility
     * =========================================================================
     */

    private static void assertEqual(
            int actual,
            int expected,
            String testName
    ) {

        if (actual != expected) {

            throw new AssertionError(
                    "FAILED: " + testName +
                            " | Expected = " + expected +
                            " | Actual = " + actual
            );
        }

        System.out.println(
                "PASSED: " + testName +
                        " | Output = " + actual
        );
    }
}