# DSA Interview Cockpit

This folder is the near-interview view over the existing Java chapters.

Source of truth remains `src/main/java/org/chijai`. These files link back to the Java chapters and to LeetCode where a link exists.

## What To Open

| Time available | Open this | Goal |
|---|---|---|
| 10 minutes before Zoom | `../notes/PRE_ZOOM_INTERVIEW_RAM_CACHE.md` | Warm up the solve script and blunder guard. |
| 2 hours | `04_TWO_DAY_AND_SEVEN_DAY_PLANS.md` | Cover the top 20 no-red-flag problems. |
| 1 day | `04_TWO_DAY_AND_SEVEN_DAY_PLANS.md` | Cover top 40 plus weak recall. |
| 2 days | `04_TWO_DAY_AND_SEVEN_DAY_PLANS.md` | Cover top 60 with implementation drills. |
| 1 week | `04_TWO_DAY_AND_SEVEN_DAY_PLANS.md` | Cover the full Priority A/B path. |
| Need one master list | `01_ZERO_TO_HERO_RANKED_TABLE.md` | Ranked all-problem table with Java and LeetCode links. |
| Need horizontal pattern discrimination | `../horizontal/README.md` | Winner pattern, near-misses, minimal mutations, and CROSSDRILL. |
| Need complete LeetCode book index | `07_LEETCODE_SOLVED_INDEX.md` | Recursive source scan of LeetCode URLs and explicit LC problem numbers in Java files. |
| Need nested university-course TOC | `09_LEETCODE_CURRICULUM_TOC.md` | One decimal hierarchy: pattern family -> sub-pattern -> every LeetCode problem with LC and local Java links. |
| Need reconstruction plus exact say-before-coding contracts | `12_MASTER_DSA_INTERVIEW_ARTICULATION_TABLE.md` | One continuous pattern -> sub-pattern table with skeletons, correctness contracts, traps, and mutations. |
| Need time/space complexity recall | `13_MASTER_TIME_SPACE_COMPLEXITY_TABLE.md` | One continuous source-linked table with exact bounds, symbols, qualifiers, and one-sentence proofs. |
| Need fast memory refresh | `02_ONE_LINE_RECALL_ALL_PROBLEMS.md` | One sentence per problem in rank order. |
| Need speaking practice | `03_CRISP_INTERVIEW_ANSWERS.md` | Brute force -> bottleneck -> pattern -> invariant -> code -> dry run. |
| Need pattern-only focus | `patterns/README.md` | One file per pattern/category, still ordered by the current heuristic. |
| Need ranking reality check | `05_RANKING_METHODOLOGY_AND_AUDIT.md` | What is objective, what is heuristic, and where ranks can be wrong. |
| Need visual mental retrieval | `00_DSA_MIND_MAP.md` | Generated Mermaid tree: topic -> sub-pattern -> anchor problem. |
| Need structure decision | `08_PROJECT_STRUCTURE_AND_PATTERN_TREE.md` | Why Java stays stable while generated docs expose the pattern taxonomy. |
| Need old static brain map | `DSA_170_Brain_Map_FINAL.md` | Legacy high-signal brain map. |
| Need one-week execution | `DSA_7-Day_Interview_Performance_Sprint.md` | Timed closed-book weekly sprint with review columns. |
| Following the legacy 90-problem hourly plan | `11_ACTIVE_90_PLAN_CUTOFF_AND_EXTENSION.md` | Decision gate for recircling the 90 vs extending to 150/170+ and whether extra leave is justified. |
| Need after-week continuation | `10_AFTER_7_DAY_EXTENSION_PLAN.md` | Days 8-12 for ranks 151+ and source-only LeetCode extras, with stop/recircle rules. |
| Need review control panel | `06_REVIEW_DASHBOARD.md` | Dynamic due/red/yellow/mastered queues from `../../review/review.json`. |

## Current Coverage

- Ranked entries: 206
- Recursive LeetCode solved index: 213
- Nested LeetCode curriculum TOC: `09_LEETCODE_CURRICULUM_TOC.md`
- Pattern files: 22
- Ranking source: `../notes/PROBLEM_PATTERN_INDEX.md` plus LeetCode links found in Java chapters.
- Ranking philosophy: transparent interview triage. Use phase bands more than exact rank numbers.
- Ranking audit: `05_RANKING_METHODOLOGY_AND_AUDIT.md`.
- Canonical mind map: `DSA_170_Brain_Map_FINAL.md`.
- Older brain-map files are kept as drafts/reference snapshots; use the FINAL file during interview prep.

## Interview Rule

For every problem, expose the thought process:

```text
brute force -> bottleneck -> pattern -> invariant -> code -> dry run
```

Do not start by trying to remember the final code.