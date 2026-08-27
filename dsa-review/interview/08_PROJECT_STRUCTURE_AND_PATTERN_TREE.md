# Project Structure And Pattern Tree

Do not physically move Java files to match the pattern taxonomy. Keep source code stable and let generated Markdown provide the interview-facing pattern tree.

## Source Layout

| Path | Responsibility |
|---|---|
| `../../src/main/java/org/chijai` | Java source of truth, package structure, tests, and implementation history. |
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
| Two Pointers | 6 | 1 | [01_two_pointers.md](patterns/01_two_pointers.md) |
| Binary Search / Answer Search | 13 | 2 | [02_binary_search.md](patterns/02_binary_search.md) |
| Sliding Window | 9 | 3 | [03_sliding_window.md](patterns/03_sliding_window.md) |
| Prefix Sum / Prefix-Suffix | 2 | 4 | [04_prefix_suffix.md](patterns/04_prefix_suffix.md) |
| Linked List Pointers | 17 | 6 | [05_linked_list.md](patterns/05_linked_list.md) |
| HashMap / Frequency / Set | 4 | 9 | [06_hashmap_hashset.md](patterns/06_hashmap_hashset.md) |
| Tree BFS / Level Order | 2 | 14 | [07_tree_bfs.md](patterns/07_tree_bfs.md) |
| Tree DFS / Recursion | 31 | 15 | [08_tree_dfs.md](patterns/08_tree_dfs.md) |
| Graph DFS / Components | 9 | 17 | [09_graph_dfs.md](patterns/09_graph_dfs.md) |
| Topological Sort | 2 | 18 | [10_topological_sort.md](patterns/10_topological_sort.md) |
| Graph BFS / Shortest Path | 7 | 19 | [11_graph_bfs.md](patterns/11_graph_bfs.md) |
| Intervals / Sorting Greedy | 5 | 31 | [12_intervals_greedy.md](patterns/12_intervals_greedy.md) |
| Stack / Monotonic Stack | 17 | 32 | [13_stack.md](patterns/13_stack.md) |
| Heap / Priority Queue | 10 | 36 | [14_heap.md](patterns/14_heap.md) |
| Dynamic Programming | 11 | 39 | [15_dynamic_programming.md](patterns/15_dynamic_programming.md) |
| Backtracking / Combinatorial DFS | 6 | 41 | [16_backtracking.md](patterns/16_backtracking.md) |
| Trie | 5 | 47 | [17_trie.md](patterns/17_trie.md) |
| Union Find / DSU | 1 | 76 | [18_union_find.md](patterns/18_union_find.md) |
| Math / Bit / String | 7 | 109 | [19_math_bit_string.md](patterns/19_math_bit_string.md) |
| Basics / Implementation | 2 | 144 | [20_core_basics.md](patterns/20_core_basics.md) |
| Design Data Structures | 5 | 165 | [21_design_lld.md](patterns/21_design_lld.md) |

When a Java file belongs to several problems, keep the file where it is and let the generated index list every linked problem under the right pattern branch.