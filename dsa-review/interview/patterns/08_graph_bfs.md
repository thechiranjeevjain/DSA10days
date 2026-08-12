# Graph BFS

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

For unweighted minimum steps, mark when enqueuing because first discovery is shortest.

## Interview Move

DFS finds a path, but BFS gives shortest path when every edge has equal cost.

## Problems

| Global Rank | Must Level | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 63 | Must Must | Number Of Provinces | Matrix DFS/BFS components | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LC](https://leetcode.com/problems/number-of-provinces/) | For unweighted minimum steps, mark when enqueuing because first discovery is shortest. | Queue start states, mark visited immediately, expand valid neighbors by level. |
| 64 | Must Must | 01 Matrix | Multi-source BFS | [Java](../../../src/main/java/org/chijai/day8/graph/session1/Matrix01.java) | [LC](https://leetcode.com/problems/01-matrix/) | For unweighted minimum steps, mark when enqueuing because first discovery is shortest. | Queue start states, mark visited immediately, expand valid neighbors by level. |
| 65 | Must Must | Rotting Oranges | Multi-source BFS | [Java](../../../src/main/java/org/chijai/day8/graph/session1/RottenOranges.java) | [LC](https://leetcode.com/problems/rotting-oranges/) | For unweighted minimum steps, mark when enqueuing because first discovery is shortest. | Queue start states, mark visited immediately, expand valid neighbors by level. |
| 66 | Must Must | Word Ladder | BFS shortest path | [Java](../../../src/main/java/org/chijai/day8/graph/session3/WordLadder.java) | [LC](https://leetcode.com/problems/word-ladder/) | For unweighted minimum steps, mark when enqueuing because first discovery is shortest. | Queue start states, mark visited immediately, expand valid neighbors by level. |
| 107 | Must | K Highest Ranked Items Within A Price Range | BFS + sorting | [Java](../../../src/main/java/org/chijai/day8/graph/session3/KHighestRankedItemsWithinAPriceRange.java) | [LC](https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/) | For unweighted minimum steps, mark when enqueuing because first discovery is shortest. | Queue start states, mark visited immediately, expand valid neighbors by level. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.