# DSA Drill System

This turns the existing Java chapter files into active interview practice.

The rule is simple: do not open the solution first. The drill starts from recall, derivation, and blank implementation.

## Files

- Drill script: `dsa-review/scripts/random-drill.ps1`
- Pattern tracker: `dsa-review/notes/PROBLEM_PATTERN_INDEX.md`
- Main mastery guide: `dsa-review/notes/INTERVIEW_DSA_MASTERY.md`
- Pre-call review: `dsa-review/notes/PRE_ZOOM_INTERVIEW_RAM_CACHE.md`

The script is read-only. It reads the index and Java file paths, then prints a drill prompt. It does not edit Java files.

## How To Run

From repo root:

```powershell
powershell -ExecutionPolicy Bypass -File .\dsa-review\scripts\random-drill.ps1
```

Shortcut:

```powershell
.\dsa-review\scripts\drill.cmd
```

Pick 3 Priority A drills:

```powershell
powershell -ExecutionPolicy Bypass -File .\dsa-review\scripts\random-drill.ps1 -Priority A -Count 3
```

Pick from all priorities:

```powershell
powershell -ExecutionPolicy Bypass -File .\dsa-review\scripts\random-drill.ps1 -Priority All -Count 2
```

Use a fixed seed to repeat the same drill:

```powershell
powershell -ExecutionPolicy Bypass -File .\dsa-review\scripts\random-drill.ps1 -Priority A -Count 2 -Seed 42
```

Include LLD/design files:

```powershell
powershell -ExecutionPolicy Bypass -File .\dsa-review\scripts\random-drill.ps1 -Priority All -IncludeDesign
```

## Frictionless Review OS Commands

The external review engine stays at `G:\TechStudyNotes\dsa-review`.

This repo has bridge scripts so you can work from `G:\TechStudyNotes\Codes\DSA10days` without typing the full tool path.

```powershell
.\dsa-review\scripts\today.cmd
.\dsa-review\scripts\stats.cmd
.\dsa-review\scripts\dashboard.cmd
.\dsa-review\scripts\review.cmd search binary
```

Record a local result without auto-commit/push:

```powershell
.\dsa-review\scripts\good.cmd DSA10-D2-S1-BINARY-SEARCH --solve-time 1200 --hints 0 --compile-success
.\dsa-review\scripts\hard.cmd DSA10-D3-S1-AT-MOST-K-DISTINCT --solve-time 1800 --hints 1 --compile-success
.\dsa-review\scripts\again.cmd DSA10-D5-S2-LARGEST-RECTANGLE --solve-time 2400 --hints 2 --compile-success=false --mistake "Wrong width after stack pop"
```

The generic bridge passes any command to the review OS:

```powershell
.\dsa-review\scripts\review.cmd due --date 2026-08-06
.\dsa-review\scripts\review.cmd export --format markdown --output review\weekly-report.md
```

If the review OS folder ever moves, set `DSA_REVIEW_OS_ROOT` before running commands:

```powershell
$env:DSA_REVIEW_OS_ROOT = "G:\TechStudyNotes\dsa-review"
```

## Importing This Repo Into Review OS

The importer reads `PROBLEM_PATTERN_INDEX.md` and merges it into `review/review.json`.

```powershell
.\dsa-review\scripts\import-review.cmd
```

Default behavior:

- Preserves existing review history.
- Updates matching metadata and source paths.
- Adds missing `DSA10-*` problems.
- Staggers new problems starting tomorrow, 5 per day.

Useful options:

```powershell
.\dsa-review\scripts\import-review.cmd -DryRun
.\dsa-review\scripts\import-review.cmd -Priority A
.\dsa-review\scripts\import-review.cmd -InitialDueMode Today
.\dsa-review\scripts\import-review.cmd -DailyLimit 8 -StartOffsetDays 0
```

## Drill Contract

For each selected problem:

1. Do not open the Java file yet.
2. Write the likely problem from the file name.
3. Write the pattern.
4. Write the invariant or state meaning.
5. Write brute force and bottleneck.
6. Code from a blank editor.
7. Dry-run sample and edge cases.
8. Open the Java chapter and compare.
9. Grade yourself in `PROBLEM_PATTERN_INDEX.md`.

## Time Boxes

| Drill type | Time |
|---|---:|
| Pattern recall only | 5 min |
| Full implementation | 25 min |
| Variant implementation | 30 min |
| Weak-problem retry | 20 min |

If you cannot state the invariant in 3 minutes, stop and mark it weak. Reading more is not the fix. Re-attempt it after reviewing.

## Daily Active Recall Routine

Use this when preparing seriously:

1. Run one Priority A random drill.
2. Solve from blank for 25 minutes.
3. Compare against Java chapter.
4. Run one variant prompt from the script.
5. Update grades in `PROBLEM_PATTERN_INDEX.md`.
6. Revisit weak problems after 1, 3, 7, and 14 days.

Use this when tired or short on time:

1. Run 3 random drills.
2. For each, write only pattern, invariant, brute force, and edge cases.
3. Do not code.

This keeps the patterns warm in memory without pretending passive reading is practice.

## Scoring

Use the 0-5 grading from `INTERVIEW_DSA_MASTERY.md`.

| Grade | Meaning |
|---|---|
| 0 | I only recognize the file name |
| 1 | I understand after reading |
| 2 | I can explain the invariant from memory |
| 3 | I can implement from blank |
| 4 | I can solve a nearby variant |
| 5 | I can teach it under pressure |

Minimum target before interviews:

- Priority A: 4+
- Priority B: 3+
- Priority C: 2-3 unless role-specific

## What To Log

Do not log generic notes. Log only failures that can repeat.

```text
Date:
Problem:
Pattern:
Failed because:
Correct invariant:
One-line fix:
Retest date:
```

Examples:

- Forgot to remove zero-count key from sliding-window map.
- Marked BFS visited after dequeue instead of enqueue.
- Used DFS for shortest path in unweighted graph.
- Wrote binary search without a clear boundary invariant.
- Lost linked-list `next` during reversal.
- Mixed DP state meaning halfway through.

## Variant Rules

After solving a known problem, force one twist:

- Return actual answer instead of length/count.
- Convert "at most K" to "exactly K".
- Convert single-source BFS to multi-source BFS.
- Add duplicates.
- Add no-answer case.
- Add streaming input.
- Ask for count of ways instead of existence.
- Ask for minimum instead of boolean.

If the twist breaks the solution, write the broken assumption. That is the useful learning.

## Interview Readiness Signal

You are not ready because you completed a file.

You are ready when a random Priority A prompt feels familiar enough to derive, even if the exact problem is not in memory.
