# DSA Review

This folder turns the existing Java solutions into an interview-prep system.

The goal is active recall, not rereading. Use the generated cockpit to decide what to study first, use the pattern files when a topic is weak, then use scripts to drill and track review state.

## What Each Folder Does

| Path | Role |
|---|---|
| `interview/` | Before-company-call cockpit. Start here when an interview is near. |
| `interview/patterns/` | One generated Markdown file per pattern/category, still ordered by the current ranking heuristic. |
| `notes/` | Strategy docs, pre-Zoom checklist, drill system, and the problem-pattern index. |
| `scripts/` | Commands for generating docs, validating links, random drills, and spaced-review actions. |
| `solutions/` | Intentionally reserved. Java source stays in `../src/main/java/org/chijai`; this folder is not the source of truth. |

## Recommended Use

1. Open `interview/README.md`.
2. If time is short, follow `interview/04_TWO_DAY_AND_SEVEN_DAY_PLANS.md`.
3. For global order, use `interview/01_ZERO_TO_HERO_RANKED_TABLE.md`.
4. For fast recall, use `interview/02_ONE_LINE_RECALL_ALL_PROBLEMS.md`.
5. For speaking practice, use `interview/03_CRISP_INTERVIEW_ANSWERS.md`.
6. For weak-topic focus, open `interview/patterns/README.md`.
7. If the ranking feels questionable, read `interview/05_RANKING_METHODOLOGY_AND_AUDIT.md`.
8. After a miss, run the review command and mark the item honestly.

## Command Index

Run commands from the repo root unless noted otherwise.

| Need | Command | Notes |
|---|---|---|
| Rebuild cockpit docs | `dsa-review\scripts\build-interview-cockpit.cmd` | Regenerates `interview/` and `interview/patterns/` from `notes/PROBLEM_PATTERN_INDEX.md` plus Java LeetCode links. |
| Validate cockpit | `dsa-review\scripts\validate-interview-cockpit.cmd` | Checks ranked rows, Java links, LeetCode coverage, pattern files, ASCII output, and core interview text. |
| Random drill | `dsa-review\scripts\drill.cmd -Priority A -Count 3` | Active-recall prompt from the local problem index. Use `-Priority B`, `-Priority C`, or `-Priority All` when needed. |
| Random drill including design | `dsa-review\scripts\drill.cmd -Priority All -Count 5 -IncludeDesign` | Useful when the interview can mix DSA and design-flavored coding. |
| Import into review state | `dsa-review\scripts\import-review.cmd` | Creates or refreshes local spaced-review items in `../review/review.json`. |
| Today queue | `dsa-review\scripts\today.cmd` | Uses the external review engine through `scripts/review-os.ps1`. |
| Due queue | `dsa-review\scripts\due.cmd` | Shows due review items. |
| Review stats | `dsa-review\scripts\stats.cmd` | Shows progress and review state. |
| Mark again | `dsa-review\scripts\again.cmd <id>` | Use when you could not derive the approach. |
| Mark hard | `dsa-review\scripts\hard.cmd <id>` | Use when you derived it but implementation was shaky. |
| Mark good | `dsa-review\scripts\good.cmd <id>` | Use when approach and implementation were solid. |
| Mark easy | `dsa-review\scripts\easy.cmd <id>` | Use only when recall was instant and clean. |
| Dashboard | `dsa-review\scripts\dashboard.cmd` | Starts the review dashboard on port `7070`. |
| Raw review command | `dsa-review\scripts\review.cmd <args>` | Pass-through to the external review engine. |

## Quality Gate

Before relying on generated docs for an interview pass, run:

```bat
dsa-review\scripts\build-interview-cockpit.cmd
dsa-review\scripts\validate-interview-cockpit.cmd
```

A clean validation means every ranked row has a Java link, pattern files cover all ranked rows, and the generated Markdown has no broken local Java links.

## Keep This Simple

Do not move Java files into `dsa-review`. The review layer should stay a generated interface over the original code, not a second source tree.
