# Graph DFS

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Mark visited before recursion and explore one component/path completely.

## Interview Move

Brute force revisits states; visited DFS gives each component/path a single exploration.

## Problems

| Global Rank | Must Level | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 67 | Must Must | Flood Fill | Matrix DFS/BFS | [Java](../../../src/main/java/org/chijai/day8/graph/session1/FloodFill.java) | [LC](https://leetcode.com/problems/flood-fill/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 68 | Must Must | Max Area Of Island | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/max-area-of-island/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 69 | Must Must | Number Of Closed Islands | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/number-of-closed-islands/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 70 | Must Must | Number Of Islands | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/number-of-islands/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 71 | Must | Pacific Atlantic Water Flow | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/pacific-atlantic-water-flow/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 72 | Must | Surrounded Regions | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/surrounded-regions/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 73 | Must | Clone Graph | Graph DFS/BFS clone | [Java](../../../src/main/java/org/chijai/day8/graph/session2/CloneGraph.java) | [LC](https://leetcode.com/problems/clone-graph/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 74 | Must | Is Graph Bipartite | BFS/DFS coloring | [Java](../../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) | [LC](https://leetcode.com/problems/is-graph-bipartite/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 108 | Must | Coloring A Border | Matrix DFS | [Java](../../../src/main/java/org/chijai/day8/graph/session1/ColoringABorder.java) | [LC](https://leetcode.com/problems/coloring-a-border/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 109 | Must | Network Delay Time | Dijkstra / graph | [Java](../../../src/main/java/org/chijai/day8/graph/session2/NetworkDelayTime.java) | [LC](https://leetcode.com/problems/network-delay-time/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.