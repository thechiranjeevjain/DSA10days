# Pattern Recognition 80/20

Pattern name is only level 1. The real interview signal is whether you can derive the solution from constraints and invariants.

## Opening Script

Use this rhythm:

```text
brute force -> bottleneck -> pattern -> invariant -> code -> dry run
```

1. Let me restate the problem.
2. What are the constraints and edge cases?
3. A brute-force way is...
4. The bottleneck is...
5. This looks like [pattern] because...
6. The invariant/state is...
7. I will code that, then dry-run.

## Core Pattern Selector

| Signal | Pattern | Why |
|---|---|---|
| Contiguous array/string | Sliding Window | Fixed or variable contiguous region with maintainable condition. |
| Pair, ends, sorted, palindrome | Two Pointers | Search space can shrink from one or both ends. |
| Repeated range/subarray aggregate | Prefix Sum | Precompute cumulative information. |
| Monotonic search space | Binary Search | If X works, all larger or smaller X also work. |
| Tree/graph path/component exploration | DFS | Explore deeply and define recursive state. |
| Minimum steps or levels | BFS | Unweighted shortest path or layer expansion. |
| Connectivity/component merging | Union Find | Maintain dynamic components cheaply. |
| Dependencies/order | Topological Sort | Process prerequisites before dependents. |
| Repeated states plus choices | Dynamic Programming | State, transition, base case. |
| Locally best safe choice | Greedy | Only valid when local choice is globally safe. |
| Generate/try/undo | Backtracking | Decision tree with constraints and pruning. |
| Top K, next best, stream priority | Heap | Priority-based frontier. |
| Fast lookup, frequency, complement | HashMap/HashSet | O(1) lookup, counting, caching. |
| Prefix/dictionary search | Trie | Shared prefixes. |
| Range query plus updates | Segment Tree | Fast range aggregation with mutation. |

## Force These Questions

1. What is the brute force?
2. What work is being repeated?
3. What property can I exploit?
4. What state must I maintain?
5. What is the invariant?
6. Which data structure maintains it cheaply?
7. Why is the algorithm correct?
8. Time and space?
9. What change would break this approach?

## No-Red-Flag Defaults

- Minimum moves in unweighted graph: BFS first.
- Contiguous substring/subarray: sliding window or prefix sum first.
- Sorted or answer-feasibility range: binary search first.
- Tree problem: define DFS helper return value before coding.
- Linked list: name pointers and save `next` before rewiring.
- DP: never code before stating `dp[...]` meaning.
- Greedy: do not use it unless you can justify the local choice.