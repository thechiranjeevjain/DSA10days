# CROSSDRILL Protocol

Use this when one problem keeps causing pattern confusion.

## Command

```bat
dsa-review\scripts\crossdrill.cmd "Minimum Window Substring"
```

```bash
./dsa-review/scripts/crossdrill.sh "Minimum Window Substring"
```

If several titles match, the command prints candidates. Use the exact title from `00_MASTER_MATRIX.md`.

## Speak This Loop

```text
Problem -> Patterns -> Mutation
WHY NOT NOW? -> WHAT IS MISSING? -> MINIMAL CHANGE -> NOW WHY DOES IT WORK?
```

## Output Contract

| Section | What must be said |
|---|---|
| Problem | Required output, input structure, constraints/workload signal. |
| Winner | The natural pattern and the invariant that makes it correct. |
| Near-misses | Patterns that almost fit, why they do not fit now, and the smallest mutation that makes them fit. |
| Tempting wrong patterns | Short rejection guard. |
| Irrelevant patterns | Aggregated, not listed one by one. |
| Close | Brute force -> bottleneck -> pattern -> invariant -> code -> dry run. |

## Examples To Start

Top-ranked examples: Two Sum, Binary Search, Longest Substring Without Repeating Characters, Product Of Array Except Self, Minimum Window Substring.