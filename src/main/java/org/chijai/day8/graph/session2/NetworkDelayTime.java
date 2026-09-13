package org.chijai.day8.graph.session2;

import java.util.*;

/**
 * =============================================================================
 * 743. Network Delay Time
 * LeetCode Hardness: Medium
 *
 * Tags:
 * Graph, Shortest Path, Dijkstra, Priority Queue, Relaxation
 * =============================================================================
 *
 * JAVA GOLD ORDER
 *
 * 1. Problem statement
 * 2. Gold snapshot
 * 3. PRIMARY solution immediately up top
 * 4. Code-aligned dry run
 * 5. First-principles invention path + the unique Dijkstra move
 * 6. Core invariant + stale-state reasoning
 * 7. Complexity derivation from the actual Java operations
 * 8. Reconstruction scaffold
 * 9. Meaningful alternatives only
 * 10. Wrong approaches / traps
 * 11. Dijkstra-family delta map
 * 12. Reinforcement + related problems with complete solutions
 * 13. Interview articulation
 * 14. 30-second recall
 * 15. Mastery checklist
 * 16. Self-verifying tests
 *
 * UNIQUE-PLACE RULE:
 * Each important idea is explained once in its strongest location.
 * Do not repeat the same invariant, trap, or derivation across sections.
 *
 * =============================================================================
 * PRIMARY PROBLEM
 * =============================================================================
 *
 * You are given a network of n nodes labeled from 1 to n.
 *
 * times[i] = [u, v, w] means there is a directed edge:
 *
 *      u -> v
 *
 * and the signal takes w units of time to travel across that edge.
 *
 * A signal starts at node k.
 *
 * Return the minimum time required for ALL nodes to receive the signal.
 * If some node can never receive the signal, return -1.
 *
 * Example:
 *
 * times = [[2,1,1],[2,3,1],[3,4,1]]
 * n = 4
 * k = 2
 *
 * Shortest arrival times:
 *
 * node 1 = 1
 * node 2 = 0
 * node 3 = 1
 * node 4 = 2
 *
 * Answer = 2
 *
 * Why MAX at the end?
 * Because every node must receive the signal.
 * The whole network is done only when the LAST node receives it.
 *
 * =============================================================================
 * GOLD SNAPSHOT
 * =============================================================================
 *
 * Pattern:
 * Single-source shortest path on a weighted directed graph
 * with non-negative edge weights.
 *
 * Primary Solution:
 * Dijkstra + adjacency list + min-heap.
 *
 * Recognition:
 * "minimum time / cost / distance from one source"
 * + weighted edges
 * + no negative weights
 *
 * Core invariant:
 * The first NON-STALE state popped for a node has that node's final
 * shortest distance.
 *
 * Time:
 * O((V + E) log V)
 *
 * Space:
 * O(V + E)
 *
 * Re-derivation cue:
 * "Always expand the closest unfinished place next."
 * =============================================================================
 */
public class NetworkDelayTime {

    /*
     * =========================================================================
     * SHARED STRUCTURES
     * =========================================================================
     */

    static record Edge(
            int to,
            int weight) {
    }

    static record NodeState(
            int node,
            int distance) {
    }

    /*
     * =========================================================================
     * 1. PRIMARY — OPTIMAL / INTERVIEW-PREFERRED
     * DIJKSTRA + MIN-HEAP + ADJACENCY LIST
     * =========================================================================
     *
     * Pattern:
     * Weighted shortest path from one source with non-negative weights.
     *
     * Time:
     * O((V + E) log V)
     *
     * Space:
     * O(V + E)
     *
     * WHY THIS IS THE PRIMARY VERSION:
     *
     * This is the version worth reconstructing six months later.
     * It directly mirrors the algorithm:
     *
     *      graph
     *      dist[]
     *      minHeap
     *      pop closest
     *      skip stale
     *      relax neighbors
     *      max(dist)
     *
     * No visited[] is required.
     * The stale-entry check is enough.
     */

    static class OptimalDijkstraHeap {

        public int networkDelayTime(int[][] times, int n, int k) {

            List<List<Edge>> graph = buildGraph(times, n);

            /*
             * INF means: this node has not been reached yet.
             *
             * We can use Integer.MAX_VALUE directly here.
             * Dijkstra never puts an unreachable INF state into the heap,
             * so newDist is only calculated from a real finite currDist.
             *
             * Under this problem's constraints, every real shortest-path
             * distance is also far below Integer.MAX_VALUE.
             */
            int INF = Integer.MAX_VALUE;

            /*
             * dist[node] = best distance discovered so far from k to node.
             *
             * Important:
             * "best discovered" is not automatically "finalized".
             * Finalization happens when the matching best state reaches
             * the top of the min-heap.
             */
            int[] dist = new int[n + 1];
            Arrays.fill(dist, INF);

            dist[k] = 0;

            /*
             * Heap stores candidate states ordered by total distance
             * from the source, not by individual edge weight.
             */
            PriorityQueue<NodeState> minHeap =
                    new PriorityQueue<>(
                            Comparator.comparingInt(NodeState::distance)
                    );

            minHeap.offer(new NodeState(k, 0));

            while (!minHeap.isEmpty()) {

                NodeState current = minHeap.poll();

                int currNode = current.node();
                int currDist = current.distance();

                /*
                 * STALE-ENTRY RULE
                 *
                 * Think of the two structures differently:
                 *
                 *      dist[node]  = current best truth
                 *      heap entry  = candidate snapshot created earlier
                 *
                 * Java's PriorityQueue has no efficient decrease-key.
                 * If node 2 first gets distance 10, we push (2, 10).
                 * If we later discover distance 2, we update dist[2] to 2
                 * and push a NEW state (2, 2).
                 *
                 * The old (2, 10) is still physically inside the heap.
                 * Eventually it is polled too. At that moment:
                 *
                 *      currDist       = 10
                 *      dist[currNode] = 2
                 *
                 * Therefore:
                 *
                 *      currDist > dist[currNode]
                 *
                 * means this heap state is outdated. We already know a
                 * strictly better route, so expanding its neighbors would
                 * only repeat useless work.
                 *
                 * continue = discard THIS stale snapshot and process the
                 * next heap candidate.
                 */
                if (currDist > dist[currNode]) {
                    continue;
                }

                for (Edge edge : graph.get(currNode)) {

                    int neighbor = edge.to();
                    int weight = edge.weight();

                    int newDist = currDist + weight;

                    /*
                     * RELAXATION
                     *
                     * Ask one reusable shortest-path question:
                     *
                     * "Does reaching neighbor THROUGH currNode beat the
                     *  best route to neighbor that I already know?"
                     */
                    if (newDist < dist[neighbor]) {

                        dist[neighbor] = newDist;

                        minHeap.offer(
                                new NodeState(neighbor, newDist)
                        );
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

        private List<List<Edge>> buildGraph(int[][] times, int n) {

            List<List<Edge>> graph = new ArrayList<>();

            /*
             * Nodes are labeled 1..n, so index 0 is intentionally unused.
             */
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
     * CODE-ALIGNED DRY RUN
     * =========================================================================
     *
     * Input:
     *
     * times = [[2,1,1],[2,3,1],[3,4,1]]
     * n = 4
     * k = 2
     *
     * Graph:
     *
     * 2 -> 1 (1)
     * 2 -> 3 (1)
     * 3 -> 4 (1)
     *
     * Initial:
     *
     * dist = [unused, INF, 0, INF, INF]
     * heap = [(2,0)]
     *
     * -------------------------------------------------------------------------
     * POP (2,0)
     * -------------------------------------------------------------------------
     *
     * Edge 2 -> 1 weight 1
     * newDist = 0 + 1 = 1
     * 1 < INF => update
     *
     * dist[1] = 1
     * push (1,1)
     *
     * Edge 2 -> 3 weight 1
     * newDist = 0 + 1 = 1
     * 1 < INF => update
     *
     * dist[3] = 1
     * push (3,1)
     *
     * heap = [(1,1),(3,1)]
     * dist = [unused, 1, 0, 1, INF]
     *
     * -------------------------------------------------------------------------
     * POP one of the distance-1 states
     * -------------------------------------------------------------------------
     *
     * Node 1 has no outgoing edge.
     * Nothing changes.
     *
     * -------------------------------------------------------------------------
     * POP (3,1)
     * -------------------------------------------------------------------------
     *
     * Edge 3 -> 4 weight 1
     * newDist = 1 + 1 = 2
     * 2 < INF => update
     *
     * dist[4] = 2
     * push (4,2)
     *
     * -------------------------------------------------------------------------
     * POP (4,2)
     * -------------------------------------------------------------------------
     *
     * No outgoing edge.
     * Heap becomes empty.
     *
     * Final shortest distances:
     *
     * node 1 = 1
     * node 2 = 0
     * node 3 = 1
     * node 4 = 2
     *
     * We need ALL nodes to receive the signal.
     * Therefore answer = max shortest distance = 2.
     */

    /*
     * =========================================================================
     * FIRST-PRINCIPLES INVENTION PATH
     * =========================================================================
     *
     * Forget the word "Dijkstra" for a moment.
     * Derive the algorithm from the problem.
     *
     * -------------------------------------------------------------------------
     * STEP 1 — What are we actually trying to know?
     * -------------------------------------------------------------------------
     *
     * For every node:
     *
     *      earliest time signal can reach that node
     *
     * So define:
     *
     *      dist[node]
     *
     * -------------------------------------------------------------------------
     * STEP 2 — If I already reached node u at time d, what can I do?
     * -------------------------------------------------------------------------
     *
     * For every outgoing edge:
     *
     *      u -> v with weight w
     *
     * I can reach v at:
     *
     *      d + w
     *
     * If that beats dist[v], I learned something better.
     * That is relaxation.
     *
     * -------------------------------------------------------------------------
     * STEP 3 — Which discovered node should I expand next?
     * -------------------------------------------------------------------------
     *
     * Expanding an arbitrary node is dangerous because a cheaper route may
     * still be waiting elsewhere.
     *
     * The safest choice is:
     *
     *      expand the currently smallest discovered distance
     *
     * That immediately suggests a min-heap.
     *
     * -------------------------------------------------------------------------
     * THE UNIQUE DIJKSTRA MOVE — CHANGE HOW THE FRONTIER IS CHOSEN
     * -------------------------------------------------------------------------
     *
     * Most of this still looks like normal graph traversal:
     *
     *      keep discovered states
     *      remove one state
     *      visit its neighbors
     *      add improved neighbors
     *
     * The distinctive Dijkstra idea is NOT "use a graph" and not even
     * "relax an edge". Bellman-Ford relaxes edges too.
     *
     * The distinctive choice is:
     *
     *      ALWAYS expand the discovered state with the smallest
     *      TOTAL path cost from the source.
     *
     * Compare the frontier policies:
     *
     *      BFS:
     *          Queue / FIFO
     *          expands by number of edges (levels)
     *
     *      Dijkstra:
     *          Min-heap / PriorityQueue
     *          expands by accumulated path cost
     *
     * Example:
     *
     *      1 -> 2 (100)
     *      1 -> 3 (1)
     *      3 -> 2 (1)
     *
     * After expanding node 1, we know:
     *
     *      node 2 candidate = 100
     *      node 3 candidate = 1
     *
     * BFS mainly sees that both were discovered one edge away.
     * Dijkstra asks which COMPLETE source-to-node cost is smaller right now.
     *
     * So Dijkstra expands:
     *
     *      (3, 1)
     *
     * before:
     *
     *      (2, 100)
     *
     * From node 3 it discovers:
     *
     *      1 -> 3 -> 2 = 1 + 1 = 2
     *
     * and improves node 2 from 100 to 2.
     *
     * This is why a normal Queue or Deque is not enough here:
     * front/back order cannot give us the globally smallest accumulated cost.
     *
     * Special cases:
     *
     *      equal / unweighted edges   -> BFS
     *      only weights 0 and 1       -> 0-1 BFS + Deque
     *      arbitrary non-negative     -> Dijkstra + Min-Heap
     *
     * GOLD RECALL:
     *
     *      BFS expands by EDGE COUNT.
     *      Dijkstra expands by TOTAL PATH COST.
     *
     * -------------------------------------------------------------------------
     * STEP 4 — Why can the smallest one become permanent?
     * -------------------------------------------------------------------------
     *
     * Suppose node x is popped with distance d.
     * Any alternative route that reaches x later must first travel through
     * some state whose current distance is >= d, because x was the minimum
     * state available.
     *
     * Every remaining edge adds a NON-NEGATIVE amount.
     * So that alternative route cannot come back below d.
     *
     * Therefore the first non-stale pop at distance d is optimal.
     *
     * THIS is the reason negative weights break classic Dijkstra.
     * A later edge of -100 could make a route that looked worse suddenly
     * become much better.
     *
     * -------------------------------------------------------------------------
     * STEP 5 — Why is the final answer a maximum?
     * -------------------------------------------------------------------------
     *
     * Dijkstra gives the earliest arrival time to EACH node.
     * The question asks when ALL nodes have received the signal.
     *
     * Therefore:
     *
     *      answer = maximum of all shortest arrival times
     *
     * If any distance is still INF, that node is unreachable => -1.
     */

    /*
     * =========================================================================
     * CORE INVARIANT — THE ONE TO REMEMBER
     * =========================================================================
     *
     * A NodeState(node, distance) is VALID when:
     *
     *      distance == dist[node]
     *
     * When the minimum VALID state is popped from the heap,
     * that distance is the final shortest distance to that node.
     *
     * Why?
     *
     *      1. It is currently globally smallest.
     *      2. Every remaining edge weight is >= 0.
     *      3. Any future route can only stay equal or become larger.
     *
     * Two different ideas must not be confused:
     *
     * dist[node]
     *      best route DISCOVERED so far
     *
     * valid heap pop
     *      route that is now safe to treat as FINAL
     */

    /*
     * =========================================================================
     * WHY NO visited[] IN THE PRIMARY VERSION?
     * =========================================================================
     *
     * You CAN implement Dijkstra with finalized/visited marking when you mark
     * a node only after its correct minimum pop.
     *
     * But this version is easier to reconstruct with one rule:
     *
     *      if (currDist > dist[currNode]) continue;
     *
     * Why marking visited when OFFERING into the heap is wrong:
     *
     *      1 -> 2 (10)
     *      1 -> 3 (1)
     *      3 -> 2 (1)
     *
     * First we discover node 2 with distance 10.
     * If we mark node 2 visited immediately, we would reject the later
     * route 1 -> 3 -> 2 with distance 2.
     *
     * Discovery is not finalization.
     */

    /*
     * =========================================================================
     * ACTUAL JAVA COMPLEXITY DERIVATION
     * =========================================================================
     *
     * Let:
     *
     * V = number of nodes
     * E = number of directed edges
     *
     * -------------------------------------------------------------------------
     * BUILD GRAPH
     * -------------------------------------------------------------------------
     *
     * Create V + 1 adjacency lists:
     * O(V)
     *
     * Insert each of E edges once:
     * O(E)
     *
     * Graph construction:
     * O(V + E)
     *
     * -------------------------------------------------------------------------
     * DIJKSTRA LOOP
     * -------------------------------------------------------------------------
     *
     * Every successful relaxation can offer one new NodeState.
     * Across the run, this is O(E) offers in the standard adjacency-list
     * implementation.
     *
     * PriorityQueue offer/poll costs logarithmic time in heap size.
     * Heap size can grow to O(E), so a fully literal Java bound is:
     *
     *      O(E log E)
     *
     * For a simple graph E <= V^2, therefore log E = O(log V).
     * This is conventionally written as:
     *
     *      O((V + E) log V)
     *
     * -------------------------------------------------------------------------
     * FINAL SCAN
     * -------------------------------------------------------------------------
     *
     * Scan dist[1..n]:
     * O(V)
     *
     * -------------------------------------------------------------------------
     * TOTAL TIME
     * -------------------------------------------------------------------------
     *
     * O((V + E) log V)
     *
     * -------------------------------------------------------------------------
     * SPACE
     * -------------------------------------------------------------------------
     *
     * adjacency list = O(V + E)
     * dist[]         = O(V)
     * heap           = O(E) worst case because stale candidates may remain
     *
     * Total:
     * O(V + E)
     */

    /*
     * =========================================================================
     * RECONSTRUCTION SCAFFOLD — 20 SECOND MEMORY
     * =========================================================================
     *
     * build adjacency list
     *
     * dist = INF
     * dist[source] = 0
     *
     * minHeap.offer(source, 0)
     *
     * while heap not empty:
     *
     *      current = heap.poll()
     *
     *      if current is stale:
     *          continue
     *
     *      for each outgoing edge:
     *
     *          newDist = current.distance + edge.weight
     *
     *          if newDist improves dist[neighbor]:
     *              update dist
     *              offer improved state
     *
     * scan dist:
     *      INF => -1
     *      otherwise take maximum
     */

    /*
     * =========================================================================
     * 2. DISTINCT ALTERNATIVE — DIJKSTRA WITHOUT HEAP
     * ADJACENCY MATRIX + O(V^2) MANUAL MINIMUM SELECTION
     * =========================================================================
     *
     * WHY KEEP THIS VERSION?
     *
     * It exposes what the heap is optimizing.
     * The algorithmic idea is still Dijkstra.
     * We simply find the closest unfinished node by scanning all V nodes.
     *
     * Time:
     * O(V^2 + E), usually written O(V^2)
     *
     * Space:
     * O(V^2) because of the adjacency matrix
     *
     * Prefer when:
     * graph is tiny/dense or interviewer explicitly asks for heap-free Dijkstra.
     */

    static class ImprovedDijkstraMatrix {

        public int networkDelayTime(int[][] times, int n, int k) {

            int INF = Integer.MAX_VALUE;

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

                /*
                 * This O(V) scan performs the same job that minHeap.poll()
                 * performs efficiently in the primary version.
                 */
                for (int node = 1; node <= n; node++) {

                    if (!finalized[node] && dist[node] < currMinDist) {

                        currMinDist = dist[node];
                        currNode = node;
                    }
                }

                if (currNode == -1) {
                    break;
                }

                finalized[currNode] = true;

                for (int neighbor = 1; neighbor <= n; neighbor++) {

                    if (graph[currNode][neighbor] == INF) {
                        continue;
                    }

                    int newDist =
                            dist[currNode] + graph[currNode][neighbor];

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
     * 3. BRUTE / GENERALIZED ALTERNATIVE — BELLMAN-FORD STYLE RELAXATION
     * =========================================================================
     *
     * WHY KEEP THIS VERSION?
     *
     * It shows the more general shortest-path idea before the Dijkstra
     * optimization: repeatedly relax edges.
     *
     * After i full rounds, shortest paths using at most i edges are correct.
     * A simple shortest path uses at most V - 1 edges, so V - 1 rounds suffice.
     *
     * Time:
     * O(V * E)
     *
     * Space:
     * O(V)
     *
     * Advantage over Dijkstra:
     * Bellman-Ford can support negative edge weights.
     *
     * For THIS problem, Dijkstra is preferred because all weights are
     * non-negative.
     */

    static class BruteForceBellmanFord {

        public int networkDelayTime(int[][] times, int n, int k) {

            int INF = Integer.MAX_VALUE;

            int[] dist = new int[n + 1];
            Arrays.fill(dist, INF);

            dist[k] = 0;

            for (int iteration = 1; iteration <= n - 1; iteration++) {

                boolean updated = false;

                for (int[] edge : times) {

                    int u = edge[0];
                    int v = edge[1];
                    int w = edge[2];

                    if (dist[u] == INF) {
                        continue;
                    }

                    if (dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                        updated = true;
                    }
                }

                if (!updated) {
                    break;
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
     * WRONG APPROACHES / HIGH-VALUE TRAPS
     * =========================================================================
     *
     * -------------------------------------------------------------------------
     * TRAP 1 — Mark visited when offering into heap
     * -------------------------------------------------------------------------
     *
     * A node may first be discovered through a bad route and later improved.
     * Discovery cannot permanently close the node.
     *
     * -------------------------------------------------------------------------
     * TRAP 2 — Forget stale-state handling
     * -------------------------------------------------------------------------
     *
     * PriorityQueue cannot mutate the old key already sitting inside it.
     * Improved routes create new heap states, so old states remain.
     * Skip them when popped.
     *
     * -------------------------------------------------------------------------
     * TRAP 3 — Return the sum of shortest distances
     * -------------------------------------------------------------------------
     *
     * Signals propagate concurrently.
     * The question asks when the last node receives the signal.
     * Therefore take MAX, not SUM.
     *
     * -------------------------------------------------------------------------
     * TRAP 4 — Use classic Dijkstra with negative edges
     * -------------------------------------------------------------------------
     *
     * Negative edges can make a later-looking route suddenly cheaper.
     * That destroys the "smallest valid pop is permanent" proof.
     */

    /*
     * =========================================================================
     * DIJKSTRA FAMILY — STABLE ENGINE + CHANGING POLICY
     * =========================================================================
     *
     * The reusable ENGINE is usually:
     *
     *      best[state]
     *      priority queue
     *      pop best candidate
     *      discard obsolete candidate
     *      expand transitions
     *      combine path score with edge score
     *      improve neighbor
     *      push improved state
     *
     * What changes from problem to problem is the POLICY.
     *
     * -------------------------------------------------------------------------
     * Problem                         Heap       Combine        Better means
     * -------------------------------------------------------------------------
     * Network Delay Time              min        d + w          smaller
     * Path With Minimum Effort        min        max(d,w)       smaller
     * Maximum Probability             max        p * edgeP      larger
     * Number of Ways to Arrive        min        d + w          smaller
     * Swim in Rising Water            min        max(time,h)    smaller
     * -------------------------------------------------------------------------
     *
     * Maximum Probability is NOT a contradiction to Dijkstra.
     * Probabilities are in [0,1], so multiplying by another edge probability
     * cannot increase a path's probability above its current prefix score.
     * That monotonicity supports the analogous best-first finalization proof.
     *
     * Classic additive Dijkstra's important break condition is negative edge
     * weight, because adding a future edge could decrease total cost.
     */

    /*
     * =========================================================================
     * REINFORCEMENT PROBLEM 1
     * 1631. Path With Minimum Effort
     * =========================================================================
     *
     * DELTA FROM NETWORK DELAY:
     *
     * Network Delay path score:
     *      sum of edge weights
     *
     * Minimum Effort path score:
     *      maximum edge difference seen anywhere on the path
     *
     * So only the combine rule changes:
     *
     *      newEffort = max(currentEffort, edgeCost)
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

            best[0][0] = 0;
            minHeap.offer(new CellState(0, 0, 0));

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

                if (effort > best[row][col]) {
                    continue;
                }

                if (row == rows - 1 && col == cols - 1) {
                    return effort;
                }

                for (int[] dir : directions) {

                    int nextRow = row + dir[0];
                    int nextCol = col + dir[1];

                    if (nextRow < 0 || nextCol < 0 ||
                            nextRow >= rows || nextCol >= cols) {
                        continue;
                    }

                    int edgeCost =
                            Math.abs(
                                    heights[row][col] -
                                            heights[nextRow][nextCol]
                            );

                    int newEffort = Math.max(effort, edgeCost);

                    if (newEffort < best[nextRow][nextCol]) {

                        best[nextRow][nextCol] = newEffort;

                        minHeap.offer(
                                new CellState(
                                        nextRow,
                                        nextCol,
                                        newEffort
                                )
                        );
                    }
                }
            }

            return 0;
        }
    }

    /*
     * =========================================================================
     * REINFORCEMENT PROBLEM 2
     * 1514. Path With Maximum Probability
     * =========================================================================
     *
     * DELTA:
     *
     * minHeap  -> maxHeap
     * addition -> multiplication
     * smaller  -> larger
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
                double probability = succProb[i];

                graph.get(u).add(
                        new ProbabilityState(v, probability)
                );
                graph.get(v).add(
                        new ProbabilityState(u, probability)
                );
            }

            double[] best = new double[n];
            best[start] = 1.0;

            PriorityQueue<ProbabilityState> maxHeap =
                    new PriorityQueue<>(
                            (a, b) ->
                                    Double.compare(
                                            b.probability,
                                            a.probability
                                    )
                    );

            maxHeap.offer(
                    new ProbabilityState(start, 1.0)
            );

            while (!maxHeap.isEmpty()) {

                ProbabilityState current = maxHeap.poll();

                int node = current.node;
                double probability = current.probability;

                if (probability < best[node]) {
                    continue;
                }

                if (node == end) {
                    return probability;
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
     * REINFORCEMENT PROBLEM 3
     * 1976. Number of Ways to Arrive at Destination
     * =========================================================================
     *
     * DELTA:
     * Keep ordinary shortest distance, but attach one extra state:
     *
     *      ways[node]
     *
     * If strictly shorter:
     *      replace distance
     *      ways[neighbor] = ways[node]
     *
     * If equally short:
     *      ways[neighbor] += ways[node]
     *
     * long is required because path-distance sums can exceed int range under
     * this problem's constraints.
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
                    new PriorityQueue<>(
                            Comparator.comparingLong(a -> a.distance)
                    );

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

                        minHeap.offer(
                                new Pair(neighbor, newDist)
                        );
                    } else if (newDist == dist[neighbor]) {

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
     * RELATED PROBLEM 1
     * 787. Cheapest Flights Within K Stops
     * =========================================================================
     *
     * IMPORTANT DELTA:
     * "node" alone is no longer enough to describe the state.
     *
     * Reaching the same city with different numbers of stops can leave
     * different future possibilities.
     *
     * State must therefore include:
     *
     *      node + cost + stops
     *
     * This is a constrained shortest-path problem, not plain one-dimensional
     * Dijkstra state.
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
                    new PriorityQueue<>(
                            Comparator.comparingInt(a -> a.cost)
                    );

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
     * RELATED PROBLEM 2
     * 778. Swim in Rising Water
     * =========================================================================
     *
     * DELTA:
     * Path score is not a sum.
     *
     * score(path) = maximum elevation encountered so far.
     *
     * The min-heap still asks:
     * "Which frontier state currently has the best achievable path score?"
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
                    new PriorityQueue<>(
                            Comparator.comparingInt(a -> a.time)
                    );

            boolean[][] visited = new boolean[n][n];

            minHeap.offer(
                    new State(0, 0, grid[0][0])
            );

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

                /*
                 * Here visited is marked at POP time, which is safe.
                 * This is completely different from marking visited when
                 * merely DISCOVERING / OFFERING a state.
                 */
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
     * RELATED PROBLEM 3
     * 1334. Find the City With the Smallest Number of Neighbors
     * =========================================================================
     *
     * DELTA:
     * Same shortest-path engine, but run it from every source.
     * Then count nodes whose shortest distance <= threshold.
     */

    static class FindTheCity {

        public int findTheCity(
                int n,
                int[][] edges,
                int distanceThreshold
        ) {

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

                /*
                 * <= intentionally prefers the larger city index on ties
                 * because source increases from 0 to n - 1.
                 */
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
                    new PriorityQueue<>(
                            Comparator.comparingInt(a -> a[1])
                    );

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
     * INTERVIEW ARTICULATION
     * =========================================================================
     *
     * "This is a single-source shortest-path problem on a weighted directed
     * graph with non-negative weights, so I would use Dijkstra.
     *
     * I build an adjacency list, keep dist[node] as the best distance found
     * so far, and use a min-heap ordered by total distance from the source.
     *
     * When I pop a state, I skip it if its distance is worse than dist[node],
     * because Java's PriorityQueue can contain stale states after a later
     * relaxation improves the same node.
     *
     * For every outgoing edge, I relax the neighbor. Because all weights are
     * non-negative, the first valid minimum pop for a node is its final
     * shortest distance.
     *
     * Finally I take the maximum shortest distance because every node must
     * receive the signal. If any node remains unreachable, I return -1.
     *
     * Complexity is O((V + E) log V) time and O(V + E) space."
     */

    /*
     * =========================================================================
     * 30-SECOND RECALL
     * =========================================================================
     *
     * Trigger:
     * weighted + non-negative + shortest from one source
     *
     * Structure:
     * adjacency list + dist[] + minHeap
     *
     * Heap key:
     * total distance from source
     *
     * Core operation:
     * relax neighbor
     *
     * Stale rule:
     * currDist > dist[currNode] => continue
     *
     * Final answer here:
     * max(dist[1..n])
     *
     * Impossible:
     * any INF => -1
     *
     * Boundary:
     * negative edge => classic Dijkstra proof breaks
     */

    /*
     * =========================================================================
     * MASTERY CHECKLIST
     * =========================================================================
     *
     * [ ] Can I explain why heap key is TOTAL path distance, not edge weight?
     *
     * [ ] Can I derive relaxation without memorizing the line?
     *
     * [ ] Can I explain discovered distance vs finalized distance?
     *
     * [ ] Can I explain exactly why stale heap states exist in Java?
     *
     * [ ] Can I explain why visited-at-offer is wrong?
     *
     * [ ] Can I prove first valid pop using non-negative weights?
     *
     * [ ] Can I explain why answer is MAX shortest distance rather than SUM?
     *
     * [ ] Can I derive O((V + E) log V) from graph + heap operations?
     *
     * [ ] Can I mutate the engine for max-product / min-max path problems?
     *
     * [ ] Can I identify negative weights as the classic Dijkstra boundary?
     */

    /*
     * =========================================================================
     * SELF-VERIFYING TESTS
     * =========================================================================
     */

    public static void main(String[] args) {

        OptimalDijkstraHeap optimal =
                new OptimalDijkstraHeap();

        ImprovedDijkstraMatrix matrix =
                new ImprovedDijkstraMatrix();

        BruteForceBellmanFord bellmanFord =
                new BruteForceBellmanFord();

        /*
         * Standard example.
         */
        verifyAllPrimarySolutions(
                optimal,
                matrix,
                bellmanFord,
                new int[][]{
                        {2, 1, 1},
                        {2, 3, 1},
                        {3, 4, 1}
                },
                4,
                2,
                2,
                "Standard propagation"
        );

        /*
         * Direct edge.
         */
        verifyAllPrimarySolutions(
                optimal,
                matrix,
                bellmanFord,
                new int[][]{
                        {1, 2, 1}
                },
                2,
                1,
                1,
                "Single directed edge"
        );

        /*
         * Source cannot reach every node.
         */
        verifyAllPrimarySolutions(
                optimal,
                matrix,
                bellmanFord,
                new int[][]{
                        {1, 2, 1}
                },
                2,
                2,
                -1,
                "Unreachable node"
        );

        /*
         * Critical stale-state / indirect-shorter-route case.
         *
         * Node 2 is first discovered with 10,
         * then improved through node 3 to 2.
         */
        verifyAllPrimarySolutions(
                optimal,
                matrix,
                bellmanFord,
                new int[][]{
                        {1, 2, 10},
                        {1, 3, 1},
                        {3, 2, 1}
                },
                3,
                1,
                2,
                "Indirect route beats earlier discovery"
        );

        /*
         * Zero-weight edges remain valid because Dijkstra requires
         * non-negative, not strictly positive, weights.
         */
        verifyAllPrimarySolutions(
                optimal,
                matrix,
                bellmanFord,
                new int[][]{
                        {1, 2, 0},
                        {2, 3, 0}
                },
                3,
                1,
                0,
                "Zero-weight edges"
        );

        /*
         * Single-node custom edge case.
         */
        verifyAllPrimarySolutions(
                optimal,
                matrix,
                bellmanFord,
                new int[][]{},
                1,
                1,
                0,
                "Single node"
        );

        /*
         * Several competing routes.
         */
        verifyAllPrimarySolutions(
                optimal,
                matrix,
                bellmanFord,
                new int[][]{
                        {1, 2, 1},
                        {1, 3, 4},
                        {2, 3, 2},
                        {2, 4, 7},
                        {3, 4, 1}
                },
                4,
                1,
                4,
                "Several competing routes"
        );

        System.out.println();
        System.out.println("All NetworkDelayTime primary-solution tests passed.");
    }

    private static void verifyAllPrimarySolutions(
            OptimalDijkstraHeap optimal,
            ImprovedDijkstraMatrix matrix,
            BruteForceBellmanFord bellmanFord,
            int[][] times,
            int n,
            int k,
            int expected,
            String testName
    ) {

        int optimalActual =
                optimal.networkDelayTime(times, n, k);

        int matrixActual =
                matrix.networkDelayTime(times, n, k);

        int bellmanFordActual =
                bellmanFord.networkDelayTime(times, n, k);

        assertEqual(
                optimalActual,
                expected,
                testName + " | Optimal Dijkstra"
        );

        assertEqual(
                matrixActual,
                expected,
                testName + " | Matrix Dijkstra"
        );

        assertEqual(
                bellmanFordActual,
                expected,
                testName + " | Bellman-Ford"
        );
    }

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
