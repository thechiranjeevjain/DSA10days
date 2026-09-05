# DSA Horizontal Mastery

This layer trains pattern discrimination across the existing DSA10days problems without creating a 150-file encyclopedia.

Source of truth remains `../../src/main/java/org/chijai`. This folder is a compact reasoning interface over the ranked cockpit and Java links.

## What This Adds

| Existing layer | Answers |
|---|---|
| `../interview/01_ZERO_TO_HERO_RANKED_TABLE.md` | What should I study first? |
| `../interview/patterns/` | How do I revise one pattern vertically? |
| `../../src/main/java/org/chijai/patterns` | What reusable Java frame does this pattern use? |
| `./` | Why this pattern, why not another, and what minimal mutation switches it? |

## Study Flow

1. Read `00_MASTER_MATRIX.md` for the navigation map.
2. Use `02_MUTATION_SWITCHBOARD.md` to learn pattern-switch triggers.
3. Open one family file only when that discrimination is weak.
4. Use `CROSSDRILL <problem>` when one problem keeps fooling you.

## CROSSDRILL Command

Windows:

```bat
dsa-review\scripts\crossdrill.cmd "Two Sum"
```

macOS/Linux:

```bash
./dsa-review/scripts/crossdrill.sh "Two Sum"
```

The command prints the full 3-loop drill for one problem: problem signal, winner pattern, important near-misses, minimal mutations, and rejection guard.

## Files

| File | Purpose |
|---|---|
| `00_MASTER_MATRIX.md` | Compact problems x patterns navigation table. |
| `01_CROSSDRILL_PROTOCOL.md` | How to run and speak the full 3-loop analysis. |
| `02_MUTATION_SWITCHBOARD.md` | Pattern-to-pattern switch rules. |
| `03_ARRAY_HASH_POINTERS.md` | Lookup, complement, ends, and cumulative-state problems. |
| `04_SLIDING_WINDOW.md` | Contiguous region problems where validity can be repaired incrementally. |
| `05_BINARY_SEARCH.md` | Sorted-index and monotonic-answer problems. |
| `06_LINKED_LIST.md` | Identity, pointer rewiring, cycles, recency lists, and merge structures. |
| `07_TREE_DFS_BFS.md` | Subtree return contracts versus level-order queue contracts. |
| `08_GRAPH_DFS_BFS.md` | Component ownership versus shortest-path or level expansion. |
| `09_TOPO_UNION_FIND.md` | Directed dependency unlocking versus undirected component merging. |
| `10_STACK_HEAP.md` | Most-recent unresolved candidate versus global priority frontier. |
| `11_INTERVALS_GREEDY.md` | Sorted interval decisions, local-choice proof, and weighted-counterexample boundaries. |
| `12_DYNAMIC_PROGRAMMING.md` | Repeated states plus choices: state, transition, base case, fill order. |
| `13_BACKTRACKING_TRIE.md` | Generate/try/undo versus prefix-indexed pruning. |
| `14_MATH_BIT_STRING.md` | Hidden algebra, bit, KMP/Z, and string contribution invariants. |
| `15_DESIGN_DATA_STRUCTURES.md` | Operation contracts, object invariants, and backing-structure choice. |

## Constraint

This folder intentionally stays under 21 human-facing Markdown files. If a new file does not improve discrimination, merge it into an existing family file.

Generated ranked entries: 206