# DSA Review

This folder turns the existing Java solutions into an interview-prep system.

The goal is active recall, not rereading. Use the generated cockpit to decide what to study first, use the pattern files when a topic is weak, then use scripts to drill and track review state.

## What Each Folder Does

| Path | Role |
|---|---|
| `interview/` | Before-company-call cockpit. Start here when an interview is near. Includes generated Mermaid mind maps. |
| `interview/patterns/` | One generated Markdown file per pattern/category, still ordered by the current ranking heuristic, with a local Mermaid taxonomy map. |
| `horizontal/` | Pattern-discrimination layer: compact master matrix, mutation switchboard, and CROSSDRILL protocol. |
| `notes/` | Strategy docs, pre-Zoom checklist, drill system, and the problem-pattern index. |
| `scripts/` | Commands for generating docs, validating links, random drills, and spaced-review actions. |
| `solutions/` | Intentionally reserved. Java source stays in `../src/main/java/org/chijai`; this folder is not the source of truth. |

## Recommended Use

1. Open `interview/README.md`.
2. If time is short, follow `interview/04_TWO_DAY_AND_SEVEN_DAY_PLANS.md`.
3. For global order, use `interview/01_ZERO_TO_HERO_RANKED_TABLE.md`.
4. For visual retrieval, use `interview/00_DSA_MIND_MAP.md`.
5. For the complete recursive LeetCode source index, use `interview/07_LEETCODE_SOLVED_INDEX.md`.
6. For pattern discrimination and mutation practice, use `horizontal/README.md`.
7. For structure decisions, use `interview/08_PROJECT_STRUCTURE_AND_PATTERN_TREE.md`.
8. If you are following the legacy 90-problem hourly plan, use `interview/11_ACTIVE_90_PLAN_CUTOFF_AND_EXTENSION.md`.
9. After the first week, continue with `interview/10_AFTER_7_DAY_EXTENSION_PLAN.md`.
10. For fast recall, use `interview/02_ONE_LINE_RECALL_ALL_PROBLEMS.md`.
11. For speaking practice, use `interview/03_CRISP_INTERVIEW_ANSWERS.md`.
12. For exact say-before-coding contracts, use `interview/12_MASTER_DSA_INTERVIEW_ARTICULATION_TABLE.md`.
13. For weak-topic focus, open `interview/patterns/README.md`.
14. If the ranking feels questionable, read `interview/05_RANKING_METHODOLOGY_AND_AUDIT.md`.
15. After a miss, run the review command and mark the item honestly.

## Command Index

Run commands from the repo root unless noted otherwise.

| Need | Command | Notes |
|---|---|---|
| Full repo verification | Windows: `verify-all.cmd`; macOS/Linux: `pwsh ./verify-all.ps1` or `./verify-all.sh` | Runs Maven tests, rebuilds generated interview docs, then validates the cockpit. |
| Rebuild cockpit docs | Windows: `dsa-review\scripts\build-interview-cockpit.cmd`; macOS/Linux: `./dsa-review/scripts/build-interview-cockpit.sh` | Regenerates `interview/` and `interview/patterns/` from `notes/PROBLEM_PATTERN_INDEX.md` plus Java LeetCode metadata. |
| Validate cockpit | Windows: `dsa-review\scripts\validate-interview-cockpit.cmd`; macOS/Linux: `./dsa-review/scripts/validate-interview-cockpit.sh` | Checks ranked rows, Java links, LeetCode coverage, Mermaid maps, pattern files, articulation rows, ASCII output, and core interview text. |
| Random drill | Windows: `dsa-review\scripts\drill.cmd -Priority A -Count 3`; macOS/Linux: `./dsa-review/scripts/drill.sh -Priority A -Count 3` | Active-recall prompt from the local problem index. Use `-Priority B`, `-Priority C`, or `-Priority All` when needed. |
| One-problem horizontal drill | Windows: `dsa-review\scripts\crossdrill.cmd "Two Sum"`; macOS/Linux: `./dsa-review/scripts/crossdrill.sh "Two Sum"` | Generates the Problem -> Patterns -> Mutation reasoning loop for one matched problem. |
| Random drill including design | Windows: `dsa-review\scripts\drill.cmd -Priority All -Count 5 -IncludeDesign`; macOS/Linux: `./dsa-review/scripts/drill.sh -Priority All -Count 5 -IncludeDesign` | Useful when the interview can mix DSA and design-flavored coding. |
| Import into review state | Windows: `dsa-review\scripts\import-review.cmd`; macOS/Linux: `./dsa-review/scripts/import-review.sh` | Creates or refreshes local spaced-review items in `../review/review.json`. |
| Today queue | Windows: `dsa-review\scripts\today.cmd`; macOS/Linux: `./dsa-review/scripts/today.sh` | Uses the external review engine through `scripts/review-os.ps1`. |
| Due queue | Windows: `dsa-review\scripts\due.cmd`; macOS/Linux: `./dsa-review/scripts/due.sh` | Shows due review items. |
| Review stats | Windows: `dsa-review\scripts\stats.cmd`; macOS/Linux: `./dsa-review/scripts/stats.sh` | Shows progress and review state. |
| Mark again | Windows: `dsa-review\scripts\again.cmd <id>`; macOS/Linux: `./dsa-review/scripts/again.sh <id>` | Use when you could not derive the approach. |
| Mark hard | Windows: `dsa-review\scripts\hard.cmd <id>`; macOS/Linux: `./dsa-review/scripts/hard.sh <id>` | Use when you derived it but implementation was shaky. |
| Mark good | Windows: `dsa-review\scripts\good.cmd <id>`; macOS/Linux: `./dsa-review/scripts/good.sh <id>` | Use when approach and implementation were solid. |
| Mark easy | Windows: `dsa-review\scripts\easy.cmd <id>`; macOS/Linux: `./dsa-review/scripts/easy.sh <id>` | Use only when recall was instant and clean. |
| Dashboard | Windows: `dsa-review\scripts\dashboard.cmd`; macOS/Linux: `./dsa-review/scripts/dashboard.sh` | Starts the review dashboard on port `7070`. |
| Raw review command | Windows: `dsa-review\scripts\review.cmd <args>`; macOS/Linux: `./dsa-review/scripts/review.sh <args>` | Pass-through to the external review engine. |
| Docker verification | `docker compose run --rm verify` | Runs the same verification path inside Linux with Java 17, Maven, and PowerShell. |

## Quality Gate

Before relying on generated docs for an interview pass, run:

```bat
dsa-review\scripts\build-interview-cockpit.cmd
dsa-review\scripts\validate-interview-cockpit.cmd
```

On macOS/Linux:

```bash
./dsa-review/scripts/build-interview-cockpit.sh
./dsa-review/scripts/validate-interview-cockpit.sh
```

A clean validation means every ranked row has a Java link, pattern files cover all ranked rows, and the generated Markdown has no broken local Java links.

## Keep This Simple

Do not move Java files into `dsa-review`. The review layer should stay a generated interface over the original code, not a second source tree.

Do not reorganize Java packages to mirror the pattern tree. Use `interview/00_DSA_MIND_MAP.md`, `interview/08_PROJECT_STRUCTURE_AND_PATTERN_TREE.md`, and `interview/patterns/` as the pattern-taxonomy layer.
