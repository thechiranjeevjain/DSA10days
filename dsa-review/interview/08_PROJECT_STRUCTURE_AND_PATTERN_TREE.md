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
| Sliding Window | 7 | 3 | [03_sliding_window.md](patterns/03_sliding_window.md) |
| Prefix Sum / Prefix-Suffix | 2 | 4 | [04_prefix_suffix.md](patterns/04_prefix_suffix.md) |
| Linked List Pointers | 14 | 6 | [05_linked_list.md](patterns/05_linked_list.md) |
| Two Pointers | 6 | 10 | [06_two_pointers.md](patterns/06_two_pointers.md) |
| Heap / Priority Queue | 12 | 11 | [07_heap.md](patterns/07_heap.md) |
| Tree BFS / Level Order | 2 | 15 | [08_tree_bfs.md](patterns/08_tree_bfs.md) |
| Tree DFS / Recursion | 31 | 16 | [09_tree_dfs.md](patterns/09_tree_dfs.md) |
| Graph DFS / Components | 12 | 18 | [10_graph_dfs.md](patterns/10_graph_dfs.md) |
| Topological Sort | 9 | 19 | [11_topological_sort.md](patterns/11_topological_sort.md) |
| Graph BFS / Shortest Path | 6 | 21 | [12_graph_bfs.md](patterns/12_graph_bfs.md) |
| Dynamic Programming | 26 | 32 | [13_dynamic_programming.md](patterns/13_dynamic_programming.md) |
| Backtracking / Combinatorial DFS | 6 | 34 | [14_backtracking.md](patterns/14_backtracking.md) |
| Stack / Monotonic Stack | 14 | 35 | [15_stack.md](patterns/15_stack.md) |
| Trie | 14 | 39 | [16_trie.md](patterns/16_trie.md) |
| Intervals / Sorting Greedy | 7 | 42 | [17_intervals_greedy.md](patterns/17_intervals_greedy.md) |
| Union Find / DSU | 1 | 70 | [18_union_find.md](patterns/18_union_find.md) |
| Greedy | 4 | 72 | [19_greedy.md](patterns/19_greedy.md) |
| Design Data Structures | 7 | 89 | [20_design_lld.md](patterns/20_design_lld.md) |
| Math / Bit / String | 7 | 105 | [21_math_bit_string.md](patterns/21_math_bit_string.md) |
| Basics / Implementation | 2 | 153 | [22_core_basics.md](patterns/22_core_basics.md) |

When a Java file belongs to several problems, keep the file where it is and let the generated index list every linked problem under the right pattern branch.

Use ../../src/main/java/org/chijai/patterns only for pattern labs: one small reusable skeleton per high-ROI family, with tests proving the frame. This helps compare commonality and variation without breaking existing package links.