# Graph DFS / Components

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

Mark visited before recursion and explore one component/path completely.

## Interview Move

Brute force revisits states; visited DFS gives each component/path a single exploration.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 17 | Phase 1 - No Red Flags | Number Of Islands | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/number-of-islands/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 34 | Phase 2 - Strong Core | Flood Fill | Matrix DFS/BFS | [Java](../../../src/main/java/org/chijai/day8/graph/session1/FloodFill.java) | [LC](https://leetcode.com/problems/flood-fill/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 35 | Phase 2 - Strong Core | Is Graph Bipartite | BFS/DFS coloring | [Java](../../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) | [LC](https://leetcode.com/problems/is-graph-bipartite/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 73 | Phase 3 - Important | Pacific Atlantic Water Flow | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/pacific-atlantic-water-flow/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 74 | Phase 3 - Important | Surrounded Regions | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/surrounded-regions/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 81 | Phase 3 - Important | Clone Graph | Graph DFS/BFS clone | [Java](../../../src/main/java/org/chijai/day8/graph/session2/CloneGraph.java) | [LC](https://leetcode.com/problems/clone-graph/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 126 | Phase 4 - Secondary | Number Of Closed Islands | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/number-of-closed-islands/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 127 | Phase 4 - Secondary | Max Area Of Island | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/max-area-of-island/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |
| 128 | Phase 4 - Secondary | Coloring A Border | Matrix DFS | [Java](../../../src/main/java/org/chijai/day8/graph/session1/ColoringABorder.java) | [LC](https://leetcode.com/problems/coloring-a-border/) | Mark visited before recursion and explore one component/path completely. | Mark visited, recursively explore neighbors, carry parent/state when cycles matter. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.