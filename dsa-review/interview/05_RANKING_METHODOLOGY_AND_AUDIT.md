# Ranking Methodology And Audit

Read this before treating [Zero To Hero Ranked Table](01_ZERO_TO_HERO_RANKED_TABLE.md) as truth.

## Verdict

This ranking is not objectively correct in the mathematical sense. It is a transparent interview-ROI heuristic generated from the local repo.

It is useful for crunch-time triage. It would be a scam if presented as a universal proof that rank 42 is objectively more important than rank 57.

Use phase bands more than exact rank numbers:

- Phase 1 beats Phase 2.
- Phase 2 beats Phase 3.
- Inside the same phase, your weak pattern or target company signal can override the exact rank.

## What Is Objective

| Check | Current result | Meaning |
|---|---:|---|
| Ranked rows generated | 206 | Rows came from [Problem Pattern Index](../notes/PROBLEM_PATTERN_INDEX.md) and Java LeetCode links. |
| Java source missing | 0 | Should stay 0. |
| LeetCode-linked rows | 189 | Rows that open LeetCode directly. |
| Local-only rows | 17 | Repo-only or design rows without direct LeetCode source link. |
| Pattern files generated | 22 | One focused view per generated category. |
| Pattern rows covered | 206 | Should match ranked rows so no problem disappears from pattern files. |
| Weekly sprint rows | 150 | Timed sprint covers the first 150 ranks once each in a cognitive training order. |
| Review dashboard rows | 206 | Dashboard ledger covers every ranked row and merges local review state. |

These are objective repository checks. They do not prove the ranking is globally correct.

## Scoring Model

The generator sorts rows by a per-problem interview-ROI weight first. Category ROI, source priority, and match confidence are tie-breakers only:

~~~text
SortKey = ImportanceWeight, then CategoryWeight, then PriorityWeight
then MatchScore, File, Title
~~~

ImportanceWeight is hand-tuned in the generator for individual problems. That is the main answer to 'rank by individual problem ROI, not only by pattern or source chapter.'

| Input | Weight | Meaning |
|---|---:|---|
| Priority A | 0 | Master first from the source index. |
| Priority B | 1000 | Stabilize after Priority A. |
| Priority C | 2000 | Review after core is stable. |

Problem ROI tiers currently used:

| Importance weight | Meaning |
|---:|---|
| 0 | Core no-red-flag interview staples. |
| 15 | Very common and still high-value. |
| 35 | Strong secondary problems once the core is stable. |
| 55 | Useful breadth, but not first-pass mandatory. |
| 80+ | Low-priority or role-specific for general DSA prep. |

## Top-Band Policy

The first pass is designed to reduce interviewer red flags, not to teach algorithms in textbook order.

| Band | Purpose | Examples |
|---|---|---|
| Ranks 1-20 | No-red-flag staples and high-signal patterns | Binary Search, Anagram, Sliding Window, Linked List Cycle, Tree BFS/DFS, Islands, Course Schedule |
| Ranks 21-40 | Common follow-ups and must-know implementation drills | Rotated Search, LRU, Copy Random List, Rotting Oranges, Heap, DP baseline, Backtracking baseline |
| Ranks 41-70 | Strong second pass after the core is stable | Monotonic Stack, more Sliding Window, List variants, Tree variants, Binary Search on answer |
| Ranks 71+ | Breadth, variants, and role/company-specific extras | Advanced DP, Trie variants, Union-Find, design-style local rows |

## Ranking QA Gates

These checks make the ranking less scammy by catching obvious placement mistakes.

| Gate | Result | Why it matters |
|---|---|---|
| Problem ROI sorts before category/source | PASS | Individual problem importance is the first ranking signal. |
| Java links resolve | PASS | A review row must open its real Java source. |
| Pattern files cover ranked rows | PASS | Pattern-specific review must not drop problems. |
| Phase 1 avoids role-specific/design rows | PASS | The first 30 should remove broad DSA red flags, not niche extras. |
| Top 40 has broad pattern coverage | PASS: 16 categories | Early prep should not be trapped inside one pattern family. |
| Top 40 contains core anchor problems | PASS | The obvious high-ROI anchors should not drift late. |
| Design rows deferred from top 70 | PASS | Design-flavored rows are useful, but not first-pass DSA ROI. |
| Weekly sprint generated from canonical rows | PASS | Sprint titles, patterns, and signals must match ranked/pattern data. |
| Review dashboard covers ranked rows | PASS | Review state is merged into due/red/yellow/mastered queues without polluting the ranked table. |

Category weights currently used:

| Weight | Category | Rationale |
|---:|---|---|
| 10 | HashMap / Frequency / Set | Low implementation cost, high red-flag risk if missed. |
| 20 | Two Pointers | Common pair/string/array interview pattern. |
| 30 | Sliding Window | High ROI for contiguous array/string problems. |
| 40 | Prefix Sum / Prefix-Suffix | Frequent repeated-range optimization. |
| 50 | Linked List Pointers | Low theory, high bug-risk in interviews. |
| 60 | Tree BFS / Level Order | Core tree traversal and level logic. |
| 70 | Tree DFS / Recursion | Core recursive return contracts and tree invariants. |
| 80 | Graph BFS / Shortest Path | Minimum-step and level-expansion problems. |
| 90 | Graph DFS / Components | Components, visited state, path exploration. |
| 100 | Binary Search / Answer Search | Important, but usually easier to recover once invariant is known. |
| 110 | Stack / Monotonic Stack | Parentheses, monotonic stack, deque-like candidate maintenance. |
| 120 | Heap / Priority Queue | Top-K, stream, and frontier problems. |
| 130+ | Remaining categories | Useful breadth after the core signal is reliable. |

## Why It Can Feel Off

- A Java chapter can contain many LeetCode links; source priority is only a tie-breaker after problem ROI and category ROI.
- Importance weights are curated heuristics, not measured company frequency data.
- Exact rank inside one phase is weaker than the phase itself.
- The ranking is not trained on company-specific interview data.
- Some rows need problem-specific hooks; generic pattern text is only a fallback.
- If the target company emphasizes DP, graphs, or tries, manually promote that pattern for that week.

## Current Anti-Scam Rule

Say this: 'This is my local interview triage order based on repo priorities, pattern ROI, and no-red-flag risk.'

Do not say this: 'This is the objectively correct global ranking of DSA problems.'

## Practical Use

For a 2-hour or 1-day crunch, follow Phase 1 in order.

For a 2-day crunch, follow Phase 1, then Phase 2, but swap in your weakest pattern if it is already known.

For a 1-week prep, use the rank order for coverage and the pattern files for targeted repair.