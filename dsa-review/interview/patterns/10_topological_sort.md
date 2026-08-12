# Topological Sort

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

Use indegree or DFS states to process dependencies before dependents.

## Interview Move

Brute force dependency checks loop; topo processes nodes only when prerequisites are done.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 18 | Phase 1 - No Red Flags | Course Schedule Ii | Topological sort / cycle | [Java](../../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) | [LC](https://leetcode.com/problems/course-schedule-ii/) | A course enters the order only when its indegree drops to zero. | Build prerequisite->course graph, queue indegree-zero courses, append order, fail if processed < n. |
| 76 | Phase 3 - Important | Minimum Height Trees | Topological trimming | [Java](../../../src/main/java/org/chijai/day8/graph/session3/MinHTree.java) | [LC](https://leetcode.com/problems/minimum-height-trees/) | Peel all current leaves together until one or two centroid roots remain. | Build graph/degrees, queue degree-1 leaves, remove layers while remainingNodes > 2. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.