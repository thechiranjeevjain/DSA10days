# Tree BFS / Level Order

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Use a queue by levels; capture level size before pushing children.

## Interview Move

DFS can mix levels; BFS preserves level order for views, distances, and serialization.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 36 | Phase 2 - Strong Core | Binary Tree Right Side View | Tree BFS / DFS | [Java](../../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeSideView.java) | [LC](https://leetcode.com/problems/binary-tree-right-side-view/) | The last node seen at each BFS level is visible from the right. | For each level size, process nodes and record value when i == size - 1. |
| 37 | Phase 2 - Strong Core | Binary Tree Level Order Traversal | Tree traversal | [Java](../../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeTraversal.java) | [LC](https://leetcode.com/problems/binary-tree-level-order-traversal/) | Capture queue size to process exactly one tree level at a time. | For each level, poll size nodes, collect values, enqueue children. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.