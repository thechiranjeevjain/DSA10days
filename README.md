# DSA10days

Open this first when preparing for a DSA interview from this repo.

The Java files under `src/main/java/org/chijai` are the source of truth. The `dsa-review` folder is the interview-facing layer over those solutions: ranked lists, pattern views, recall prompts, drill scripts, and validation.

## Fast Start

| Situation | Open / run |
|---|---|
| Interview in 10 minutes | `dsa-review/interview/README.md`, then `dsa-review/notes/PRE_ZOOM_INTERVIEW_RAM_CACHE.md` |
| Need the ranked study order | `dsa-review/interview/01_ZERO_TO_HERO_RANKED_TABLE.md` |
| Need one-line memory refresh | `dsa-review/interview/02_ONE_LINE_RECALL_ALL_PROBLEMS.md` |
| Need spoken interview answers | `dsa-review/interview/03_CRISP_INTERVIEW_ANSWERS.md` |
| Need pattern-focused revision | `dsa-review/interview/patterns/README.md` |
| Need to audit the ranking | `dsa-review/interview/05_RANKING_METHODOLOGY_AND_AUDIT.md` |
| Need a random active-recall drill | `dsa-review\scripts\drill.cmd -Priority A -Count 3` |

## Interview Solve Rhythm

```text
brute force -> bottleneck -> pattern -> invariant -> code -> dry run
```

Use this script before writing code:

1. Let me restate the problem.
2. What are the constraints and edge cases?
3. A brute-force way is...
4. The bottleneck is...
5. This looks like this pattern because...
6. The invariant/state is...
7. I will code that, then dry-run.

## Main Commands

Run these from the repo root.

| Goal | Command |
|---|---|
| Run full repo verification | `verify-all.cmd` |
| Rebuild interview cockpit docs | `dsa-review\scripts\build-interview-cockpit.cmd` |
| Validate generated cockpit links/counts | `dsa-review\scripts\validate-interview-cockpit.cmd` |
| Pick random priority A drills | `dsa-review\scripts\drill.cmd -Priority A -Count 3` |
| Import problems into spaced review | `dsa-review\scripts\import-review.cmd` |
| See today's review queue | `dsa-review\scripts\today.cmd` |
| See due review items | `dsa-review\scripts\due.cmd` |
| See review stats | `dsa-review\scripts\stats.cmd` |
| Mark an item again/hard/good/easy | `dsa-review\scripts\again.cmd <id>`, `hard.cmd <id>`, `good.cmd <id>`, `easy.cmd <id>` |
| Open dashboard | `dsa-review\scripts\dashboard.cmd` |

## Folder Map

| Folder | Purpose |
|---|---|
| `src/main/java/org/chijai` | Original Java solutions and examples. Keep this as the source of truth. |
| `dsa-review/interview` | Near-interview cockpit: ranked lists, recall prompts, crisp answers, time-boxed plans. |
| `dsa-review/interview/patterns` | Per-pattern focused revision files generated from the ranked list. |
| `dsa-review/notes` | Strategy notes and source problem index used by the generator. |
| `dsa-review/scripts` | Build, validate, random drill, and spaced-review helper commands. |
| `review/review.json` | Local spaced-review state imported from this repo. |

## Source Of Truth Rule

Do not copy Java solutions into a second study tree. Keep implementation code in `src/main/java/org/chijai`; use Markdown links in `dsa-review` as the interview interface.
