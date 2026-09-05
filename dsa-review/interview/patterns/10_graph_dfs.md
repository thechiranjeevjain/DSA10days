# Graph DFS / Components

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

Own each component or path with visited state so one traversal fully accounts for it.

## Interview Move

Brute force revisits states; visited DFS gives each component/path a single exploration.

## Pattern Taxonomy Map

```mermaid
flowchart TD
  Topic["TOPIC<br/>Graph DFS / Components"]
  Recognition["RECOGNITION<br/>Own each component or path with visited state so one traversal fully accounts for it."]
  Invariant["INVARIANT<br/>Brute force revisits states; visited DFS gives each component/path a single exploration."]
  Topic --> Recognition --> Invariant
  Invariant --> Sub01["SUB-PATTERN<br/>BFS/DFS coloring<br/>1 problem(s)"]
  Sub01 --> Sub01A01["ANCHOR<br/>rank 41: Is Graph Bipartite?"]
  Invariant --> Sub02["SUB-PATTERN<br/>Bipartite coloring<br/>1 problem(s)"]
  Sub02 --> Sub02A01["ANCHOR<br/>rank 129: Possible Bipartition"]
  Invariant --> Sub03["SUB-PATTERN<br/>Connectivity + cycle<br/>1 problem(s)"]
  Sub03 --> Sub03A01["ANCHOR<br/>rank 128: Graph Valid Tree"]
  Invariant --> Sub04["SUB-PATTERN<br/>Graph DFS/BFS clone<br/>1 problem(s)"]
  Sub04 --> Sub04A01["ANCHOR<br/>rank 77: Clone Graph"]
  Invariant --> Sub05["SUB-PATTERN<br/>Matrix DFS<br/>1 problem(s)"]
  Sub05 --> Sub05A01["ANCHOR<br/>rank 130: Coloring A Border"]
  Invariant --> Sub06["SUB-PATTERN<br/>Matrix DFS/BFS<br/>1 problem(s)"]
  Sub06 --> Sub06A01["ANCHOR<br/>rank 40: Flood Fill"]
  Invariant --> Sub07["SUB-PATTERN<br/>Matrix DFS/BFS components<br/>5 problem(s)"]
  Sub07 --> Sub07A01["ANCHOR<br/>rank 18: Number Of Islands"]
  Sub07 --> Sub07A02["ANCHOR<br/>rank 68: Pacific Atlantic Water Flow"]
  Sub07 --> Sub07A03["ANCHOR<br/>rank 69: Surrounded Regions"]
```

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 18 | Phase 1 - No Red Flags | Number Of Islands | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/number-of-islands/) | Every time you find unvisited land, sink its whole connected component and count one island. | Scan grid; on '1', increment count and DFS/BFS four directions marking visited/water. |
| 40 | Phase 2 - Strong Core | Flood Fill | Matrix DFS/BFS | [Java](../../../src/main/java/org/chijai/day8/graph/session1/FloodFill.java) | [LC](https://leetcode.com/problems/flood-fill/) | Recolor only the connected component matching the starting color. | If oldColor == newColor return; DFS/BFS neighbors with oldColor and recolor them. |
| 41 | Phase 2 - Strong Core | Is Graph Bipartite? | BFS/DFS coloring | [Java](../../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) | [LC](https://leetcode.com/problems/is-graph-bipartite/) | A graph is bipartite if every edge connects opposite colors. | For each uncolored node, BFS/DFS assign colors and fail on same-color neighbor. |
| 68 | Phase 2 - Strong Core | Pacific Atlantic Water Flow | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/pacific-atlantic-water-flow/) | Reverse the flow: start from both oceans and move to equal-or-higher neighboring cells. | Mark cells reachable from Pacific border and Atlantic border; answer intersection. |
| 69 | Phase 2 - Strong Core | Surrounded Regions | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/surrounded-regions/) | Only O-regions connected to the border survive; all other O cells are captured. | DFS/BFS border O cells as safe, flip remaining O to X, restore safe marks. |
| 77 | Phase 3 - Important | Clone Graph | Graph DFS/BFS clone | [Java](../../../src/main/java/org/chijai/day8/graph/session2/CloneGraph.java) | [LC](https://leetcode.com/problems/clone-graph/) | Map original node to cloned node before cloning neighbors to handle cycles. | DFS/BFS: create clone if absent, then connect cloned neighbors from the map. |
| 126 | Phase 4 - Secondary | Number Of Closed Islands | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/number-of-closed-islands/) | A closed island is a land component that never touches the grid boundary. | DFS each land component, return false if any cell touches border, mark visited. |
| 127 | Phase 4 - Secondary | Max Area Of Island | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/max-area-of-island/) | DFS each land component and return its cell count; keep the maximum. | On each unvisited land cell, DFS four directions accumulating area. |
| 128 | Phase 4 - Secondary | Graph Valid Tree | Connectivity + cycle | [Java](../../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) | [LC](https://leetcode.com/problems/graph-valid-tree/) | A valid undirected tree has exactly n - 1 edges and all n nodes in one connected component. | Reject edge count != n - 1, traverse from one node while skipping the parent edge, and require every node to be visited. |
| 129 | Phase 4 - Secondary | Possible Bipartition | Bipartite coloring | [Java](../../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) | [LC](https://leetcode.com/problems/possible-bipartition/) | color[x] is the group assigned to person x; every dislike edge must connect opposite colors. | Build an undirected dislike graph; for every uncolored component, assign opposite colors by BFS/DFS and fail on a same-color edge. |
| 130 | Phase 4 - Secondary | Coloring A Border | Matrix DFS | [Java](../../../src/main/java/org/chijai/day8/graph/session1/ColoringABorder.java) | [LC](https://leetcode.com/problems/coloring-a-border/) | Only cells on the component boundary get recolored; interior cells keep original color. | DFS component, mark a cell as border if it touches outside grid or different color. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.