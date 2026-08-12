# Graph DFS / Components

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Mark visited before recursion and explore one component/path completely.

## Interview Move

Brute force revisits states; visited DFS gives each component/path a single exploration.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 70 | Phase 2 - Strong Core | Flood Fill | Matrix DFS/BFS | [Java](../../../src/main/java/org/chijai/day8/graph/session1/FloodFill.java) | [LC](https://leetcode.com/problems/flood-fill/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 71 | Phase 3 - Important | Max Area Of Island | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/max-area-of-island/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 72 | Phase 3 - Important | Number Of Closed Islands | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/number-of-closed-islands/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 73 | Phase 3 - Important | Number Of Islands | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/number-of-islands/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 74 | Phase 3 - Important | Pacific Atlantic Water Flow | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/pacific-atlantic-water-flow/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 75 | Phase 3 - Important | Surrounded Regions | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/surrounded-regions/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 76 | Phase 3 - Important | Clone Graph | Graph DFS/BFS clone | [Java](../../../src/main/java/org/chijai/day8/graph/session2/CloneGraph.java) | [LC](https://leetcode.com/problems/clone-graph/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 77 | Phase 3 - Important | Is Graph Bipartite | BFS/DFS coloring | [Java](../../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) | [LC](https://leetcode.com/problems/is-graph-bipartite/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 110 | Phase 3 - Important | Coloring A Border | Matrix DFS | [Java](../../../src/main/java/org/chijai/day8/graph/session1/ColoringABorder.java) | [LC](https://leetcode.com/problems/coloring-a-border/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 111 | Phase 4 - Secondary | Network Delay Time | Dijkstra / graph | [Java](../../../src/main/java/org/chijai/day8/graph/session2/NetworkDelayTime.java) | [LC](https://leetcode.com/problems/network-delay-time/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.