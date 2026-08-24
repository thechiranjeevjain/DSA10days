# DSA — 21 Core Pattern Cards: One-Page Recall

> **Goal:** problem statement → trigger pattern → state invariant → recall skeleton → code.

| # | Pattern | Recognition Trigger | Core Invariant | Canonical Anchor | Complexity Shape | Recall Phrase |
|---:|---|---|---|---|---|---|
| 1 | HashMap / Frequency | count, duplicates, complement, seen-before | map = useful processed history | Two Sum | O(n) expected | Need fast memory of what I saw. |
| 2 | Two Pointers | sorted/palindrome/pair/ends | outside active pointers is resolved | Container With Most Water | O(n), or O(n log n) with sort | One comparison discards one side. |
| 3 | Sliding Window | contiguous + longest/shortest/count + maintainable constraint | `[L..R]` = current valid window | Longest Substring Without Repeating | O(n) | Expand right; repair with left. |
| 4 | Prefix Sum | range/subarray aggregate/count | range = difference of cumulative states | Binary Subarrays With Sum | O(n) build/count; O(1) range query | Range = cumulative-state difference. |
| 5 | Binary Search | sorted/ordered space; discard half | answer remains inside search interval | Find First/Last Position | O(log n) | One test discards half. |
| 6 | Binary Search on Answer | minimum/maximum feasible value + monotonic predicate | answer = feasibility boundary | Koko Eating Bananas | O(check × log range) | Candidate answer has monotonic feasibility. |
| 7 | Intervals / Sweep Line | overlaps, rooms, concurrent events, capacity timeline | sorted boundaries make active state local | Meeting Rooms II | O(n log n) | Sort boundaries, reason locally. |
| 8 | Greedy | local choice can be proven safe | local decision never hurts optimal future | Gas Station | O(n) or O(n log n) | Prove local choice cannot hurt future. |
| 9 | Monotonic Stack | next/prev greater/smaller; nearest boundary | stack = monotonic unresolved candidates | Daily Temperatures | O(n) amortized | Nearest boundary where order breaks. |
| 10 | Monotonic Deque | moving window + repeated max/min + expiry | deque = in-window, monotonic; front = best | Sliding Window Maximum | O(n), O(k) space | Moving window + repeated best + expiry. |
| 11 | Heap / Top-K | repeated min/max, kth, top-k, streaming priority | heap contains exactly the best needed candidates | Top K Frequent Elements | O(n log k) typical | Need next best repeatedly, not full sort. |
| 12 | Linked-List Pointers | reverse, cycle, middle, reconnect | preserve unexplored remainder before rewiring | Linked List Cycle | O(n), usually O(1) space | Preserve remainder before rewiring. |
| 13 | Tree DFS | parent answer built from child answers | define `dfs(node) returns ___` | Lowest Common Ancestor | O(n), O(h) stack | dfs(node) returns ______. |
| 14 | Tree BFS | level, frontier, nearest-by-edges | queue = current frontier | Level Order Traversal | O(n), O(w) queue | Frontier / level / nearest. |
| 15 | BST | ordered tree; search/rank/range | global BST ordering / inorder sorted | Validate BST | O(h) search; O(n) traversal | Tree + sorted-order leverage. |
| 16 | Graph DFS/BFS | components, reachability, unweighted shortest path | each state visited once | Number of Islands | O(V+E) | Node / edge / visited / start / target. |
| 17 | Topological Sort | directed prerequisites/order | queue = remaining indegree-zero nodes | Course Schedule II | O(V+E) | Dependencies → indegree zero first. |
| 18 | Dijkstra | nonnegative weighted shortest path | relax from current best distance; skip stale states | Network Delay Time | O((V+E) log V) | Weighted shortest → min-heap + relaxation. |
| 19 | Union-Find / DSU | repeated merge/connectivity in undirected graph | every item has one representative root | Accounts Merge | O(α(n)) amortized | Merge/connectivity → representative roots. |
| 20 | Backtracking | enumerate choices / constraint search | choose → recurse → undo | Combination Sum | output-dependent exponential | Choose → recurse → undo. |
| 21 | Dynamic Programming | repeated min/max/count/can with overlapping subproblems | STATE → CHOICE → RECURRENCE → BASE | House Robber | states × transitions | STATE → CHOICE → RECURRENCE → BASE. |

---

# A / B / C Anchor Rule

```text
A = primitive mechanics
B = canonical interview anchor
C = harder transfer / mutation
```

For each anchor, recall only:

```text
RECOGNITION
CORE INVARIANT
TRANSFER
```

Do not memorize the final solution.

---

# High-Value Confusion Guards

| Confusion | Discriminator |
|---|---|
| Sliding Window vs Prefix Sum | Window can be repaired by moving `left`; arbitrary negatives often break this. Prefix Sum handles historical differences. |
| Binary Search vs BS on Answer | Search data directly vs search a candidate answer through a monotonic feasibility test. |
| BFS vs Dijkstra | Equal edge cost / fewest edges vs unequal nonnegative edge weights. |
| DFS/BFS vs DSU | Traverse current graph vs repeatedly merge/query undirected components. |
| Heap vs Monotonic Deque | General repeated best vs moving-window best with expiry + domination. |
| Greedy vs DP | Greedy needs proof local choice cannot hurt future; otherwise preserve competing futures with DP. |
| Backtracking vs DP | Enumerate actual solutions/paths vs cache repeated states. |
| Tree DFS vs Tree BFS | Child answers build parent answer vs level/frontier/nearest. |
| Interval Merge vs Sweep Line | Produce merged ranges vs track active count/capacity over ordered boundaries. |
| Topological Sort vs Generic Graph Traversal | Directed prerequisite ordering vs ordinary reachability/components. |

---

# 10-Second Pattern Ownership Check

```text
SEE IT?       recognition
DERIVE IT?    reasoning
STATE IT?     invariant
TYPE IT?      implementation
TEST IT?      correctness
DEBUG IT?     recovery
EXPLAIN IT?   communication
CHANGE IT?    transfer
RECALL IT?    retention
DO IT TIMED?  interview readiness
```

> **3 independent successes on different problems/variants = owned.**
