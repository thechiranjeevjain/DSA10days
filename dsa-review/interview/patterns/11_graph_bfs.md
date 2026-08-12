# Graph BFS / Shortest Path

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

For unweighted minimum steps, mark when enqueuing because first discovery is shortest.

## Interview Move

DFS finds a path, but BFS gives shortest path when every edge has equal cost.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 19 | Phase 1 - No Red Flags | Word Ladder | BFS shortest path | [Java](../../../src/main/java/org/chijai/day8/graph/session3/WordLadder.java) | [LC](https://leetcode.com/problems/word-ladder/) | For unweighted minimum steps, mark when enqueuing because first discovery is shortest. | Queue start states, mark visited immediately, expand valid neighbors by level. |
| 29 | Phase 1 - No Red Flags | Rotting Oranges | Multi-source BFS | [Java](../../../src/main/java/org/chijai/day8/graph/session1/RottenOranges.java) | [LC](https://leetcode.com/problems/rotting-oranges/) | For unweighted minimum steps, mark when enqueuing because first discovery is shortest. | Queue start states, mark visited immediately, expand valid neighbors by level. |
| 30 | Phase 1 - No Red Flags | 01 Matrix | Multi-source BFS | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Matrix01.java) | [LC](https://leetcode.com/problems/01-matrix/) | For unweighted minimum steps, mark when enqueuing because first discovery is shortest. | Queue start states, mark visited immediately, expand valid neighbors by level. |
| 71 | Phase 3 - Important | Burn Binary Tree | Tree + graph BFS | [Java](../../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java) | - | Treat the tree as an undirected graph from the target node and BFS by minutes. | Build parent links, start BFS from target, expand left/right/parent, count levels. |
| 72 | Phase 3 - Important | Network Delay Time | Dijkstra / graph | [Java](../../../src/main/java/org/chijai/day8/graph/session2/NetworkDelayTime.java) | [LC](https://leetcode.com/problems/network-delay-time/) | Dijkstra keeps the next shortest unsettled node in a min-heap. | Build adjacency, push source distance 0, relax neighbors when a smaller distance is found. |
| 80 | Phase 3 - Important | Number Of Provinces | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/number-of-provinces/) | For unweighted minimum steps, mark when enqueuing because first discovery is shortest. | Queue start states, mark visited immediately, expand valid neighbors by level. |
| 125 | Phase 4 - Secondary | K Highest Ranked Items Within A Price Range | BFS + sorting | [Java](../../../src/main/java/org/chijai/day8/graph/session3/KHighestRankedItemsWithinAPriceRange.java) | [LC](https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/) | For unweighted minimum steps, mark when enqueuing because first discovery is shortest. | Queue start states, mark visited immediately, expand valid neighbors by level. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.