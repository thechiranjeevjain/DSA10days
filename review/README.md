# Review State

This folder stores local spaced-review state for this repo.

`review.json` is machine-managed progress data. Do not edit it by hand during normal study; use the commands in `../dsa-review/scripts` instead.

## Common Commands

Run from the repo root:

| Goal | Command |
|---|---|
| Import or refresh review items | `dsa-review\scripts\import-review.cmd` |
| See today's queue | `dsa-review\scripts\today.cmd` |
| See due items | `dsa-review\scripts\due.cmd` |
| See review stats | `dsa-review\scripts\stats.cmd` |
| Mark again | `dsa-review\scripts\again.cmd <id>` |
| Mark hard | `dsa-review\scripts\hard.cmd <id>` |
| Mark good | `dsa-review\scripts\good.cmd <id>` |
| Mark easy | `dsa-review\scripts\easy.cmd <id>` |

## Folder Relationship

`../dsa-review` contains human-facing docs, generated interview views, and scripts.

`review` contains the state file those scripts update.
