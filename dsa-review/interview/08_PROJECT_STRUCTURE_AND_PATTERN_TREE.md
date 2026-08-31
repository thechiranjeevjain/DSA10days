# Project Structure And Pattern Tree

Do not physically move Java files to match the pattern taxonomy. Keep source code stable and let generated Markdown provide the interview-facing pattern tree.

## Source Layout

| Path | Responsibility |
|---|---|
| `../../src/main/java/org/chijai` | Java source of truth, package structure, tests, and implementation history. |
| `../../src/main/java/org/chijai/patterns` | Additive pattern-lab package: reusable skeletons for visualizing common frames across problems. Do not move existing solved files into it. |
| `../notes/PROBLEM_PATTERN_INDEX.md` | Curated mapping from Java files to pattern metadata and priority. |
| `../notes/LEETCODE_ID_CATALOG.csv` | Local catalog for explicit `LC 123` references found in Java source. |
| `01_ZERO_TO_HERO_RANKED_TABLE.md` | Interview-ROI order. |
| `00_DSA_MIND_MAP.md` | Generated visual retrieval tree. |
| `patterns/` | Generated per-pattern taxonomy pages. |

## Chapter Pattern

Use this order inside rich Java chapter files:

```text
PROBLEM -> BASELINE -> RECOGNITION -> INVARIANT -> TRAPS -> FALLBACK -> OPTIMAL -> DEFEND
```

## Taxonomy Shape

```text
TOPIC
  CATEGORY
    SUBCATEGORY
      SUB-PATTERN
        ANCHOR PROBLEM
```

## Generated Pattern Tree

| Topic | Ranked entries | First rank | Generated file |
|---|---:|---:|---|
| HashMap / Frequency / Set | 5 | 1 | [01_hashmap_hashset.md](patterns/01_hashmap_hashset.md) |
| Binary Search / Answer Search | 13 | 2 | [02_binary_search.md](patterns/02_binary_search.md) |
| Sliding Window | 5 | 3 | [03_sliding_window.md](patterns/03_sliding_window.md) |
| Prefix Sum / Prefix-Suffix | 2 | 4 | [04_prefix_suffix.md](patterns/04_prefix_suffix.md) |
| Linked List Pointers | 17 | 6 | [05_linked_list.md](patterns/05_linked_list.md) |
| Two Pointers | 6 | 10 | [06_two_pointers.md](patterns/06_two_pointers.md) |
| Tree BFS / Level Order | 2 | 15 | [07_tree_bfs.md](patterns/07_tree_bfs.md) |
| Tree DFS / Recursion | 32 | 16 | [08_tree_dfs.md](patterns/08_tree_dfs.md) |
| Graph DFS / Components | 12 | 18 | [09_graph_dfs.md](patterns/09_graph_dfs.md) |
| Topological Sort | 9 | 19 | [10_topological_sort.md](patterns/10_topological_sort.md) |
| Graph BFS / Shortest Path | 6 | 21 | [11_graph_bfs.md](patterns/11_graph_bfs.md) |
| Dynamic Programming | 29 | 32 | [12_dynamic_programming.md](patterns/12_dynamic_programming.md) |
| Backtracking / Combinatorial DFS | 6 | 34 | [13_backtracking.md](patterns/13_backtracking.md) |
| Stack / Monotonic Stack | 15 | 35 | [14_stack.md](patterns/14_stack.md) |
| Heap / Priority Queue | 11 | 36 | [15_heap.md](patterns/15_heap.md) |
| Intervals / Sorting Greedy | 7 | 38 | [16_intervals_greedy.md](patterns/16_intervals_greedy.md) |
| Trie | 14 | 39 | [17_trie.md](patterns/17_trie.md) |
| Union Find / DSU | 1 | 71 | [18_union_find.md](patterns/18_union_find.md) |
| Math / Bit / String | 7 | 101 | [19_math_bit_string.md](patterns/19_math_bit_string.md) |
| Basics / Implementation | 3 | 152 | [20_core_basics.md](patterns/20_core_basics.md) |
| Design Data Structures | 5 | 199 | [21_design_lld.md](patterns/21_design_lld.md) |

When a Java file belongs to several problems, keep the file where it is and let the generated index list every linked problem under the right pattern branch.

Use ../../src/main/java/org/chijai/patterns only for pattern labs: one small reusable skeleton per high-ROI family, with tests proving the frame. This helps compare commonality and variation without breaking existing package links.