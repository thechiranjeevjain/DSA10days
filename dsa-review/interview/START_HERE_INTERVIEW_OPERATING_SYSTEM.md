# Start here — one interview preparation operating system

**Current as of 20 September 2026.** This page is the front door for the files created and revised in this conversation. Its job is to end broad preparation by 30 September, keep interview skills retrievable afterward, protect recovery, and turn applications and real interview evidence into offers. The stop date ends the *syllabus sprint*; it does not mean every question is mastered or that an offer is due.

## What “coach” means here

The **mixed-sitting coach** is a local Node script at [mixed-sitting.mjs](../../../../review-os/coach/mixed-sitting.mjs). It reads the existing DSA/design/Java source lists, prints the next 150-minute card, records your *actual* scores in `G:\TechStudyNotes\review-os\coach\mixed-sitting-state.json`, and advances a session pointer only after you run `done`. It is a scheduler and score log, not a tutor, interviewer, FSRS engine, or proof of readiness. Its [user-facing workout and first 88 rows](OCTOBER_150_MIN_MIXED_INTERVIEW_CIRCUIT.md) are the main October artifact.

The **scheduled Codex task** named **Daily interview coach and job links** runs at 06:45 IST in this conversation. From 5 October it reads the next mixed-sitting card and shares it here, and it searches for fresh verified jobs. It never completes a sitting or applies to a job for you. If the computer or app is unavailable, run `plan` yourself. Email delivery is not active.

**MocksPractice** is a separate adaptive mock-interview system. It generates and freezes an unseen question paper, runs the candidate attempt, viva and scoring, and sends corrections to Review OS. It is the readiness *test*. The mixed-sitting coach is the repeated training *workout*. [Review OS](../../../../review-os/README.md) owns spaced-review dates for cards that have recorded attempts. Do not count a quick cue as a solved problem or record one attempt as multiple independent proofs.

## The only daily decision tree

Read top to bottom; the first applicable row wins. A replacement activity never adds a second technical quota that day.

| Situation | Do exactly this | Advance the 150-minute queue? |
| --- | --- | --- |
| **20–30 September** | Follow the dated [September hourly wins](SEP_20_30_2026_INTERVIEW_READY_HOURLY_WINS.md). At 09:00 IST on 30 September, record actual evidence and write `PREP CLOSED`; carry at most three exact gaps. | Not started. |
| **1–4 October bridge** | Use the short bridge blocks in the September file. | No. |
| **Real interview within 72 hours** | Use the September file's interview override: format-specific mock or question, one targeted repair, company/project story, logistics, sleep. Open the relevant [Goldman](GoldmanSachs_Master_Revision_DSA_LLD_HLD.md), [Point72](Point72_Master_Revision_HackerRank_SQL_DSA_LLD_HLD.md), or [Wells Fargo](WellsFargo_Master_Revision_DSA_LLD_HLD.md) sheet only for that confirmed format. | No; resume the held row afterward. |
| **Weekly mock day** | In the **MocksPractice project chat**, send `START MOCK Compressed` as the bounded default. Attempt cold, then send `DONE` and complete the viva. Review its scorecard and choose at most one consequential repair. | No; the mock replaces the sitting. |
| **Normal day with 150 minutes** | Run the mixed-sitting `plan`; follow its exact clock; score once with `done`. | Yes, after a real completed sitting only. |
| **Office, low-energy, or recovery day** | Do the 20-minute minimum in the [mixed circuit](OCTOBER_150_MIN_MIXED_INTERVIEW_CIRCUIT.md) using the **same next row**. | No. |
| **A consequential real/mock failure** | Use the next available 150-minute block for the *exact* failed code move or design requirement. This repair replaces that day's numbered sitting. If the issue is small, use the next card's built-in mini-round instead. | No for a replacement repair block. |
| **Three unproductive days or a rejection stall** | Apply the seven-day lighter recovery protocol in the September file, keep one small retrieval and one job action, then resume the held row. | No until a full sitting is completed. |

**Week structure:** Use one mock in each calendar week. The first three weeks of a four-week cycle are build weeks; the fourth is lighter with brief retrieval and `START MOCK Micro` instead of a full new-coverage quota. A full sitting is available on any day you truly have 150 minutes; Mon/Tue office days may use the 20-minute minimum. Applications/referrals take a separate 10–20-minute action on up to five suitable days, with no poor-fit quota filling. These are capacity limits, not a command to make up missed rows at night.

## Commands — which surface, what it changes

| Where | Exact command or message | What happens |
| --- | --- | --- |
| PowerShell, any directory | `node G:/TechStudyNotes/review-os/coach/mixed-sitting.mjs plan` | **Read-only.** Shows the next 150-minute card and local links. Starts at sitting 1. |
| PowerShell | `node G:/TechStudyNotes/review-os/coach/mixed-sitting.mjs status` | Shows next number and recorded coverage. A zero today means this new log has no evidence; it does not erase your earlier ~90 attempts. |
| PowerShell, **after** full work | `node G:/TechStudyNotes/review-os/coach/mixed-sitting.mjs done G G Y G Y G GGYGU "one exact repair"` | Replace example scores with your six lane results, five cue results (three on the short cue row), and exact repair or `none`. Records evidence and advances **one** sitting. On the two-DSA final row, use `-` for short DSA 2. Never run this for a skipped/minimum day. |
| PowerShell | `node G:/TechStudyNotes/review-os/coach/mixed-sitting.mjs plan 89` | Read-only preview of any absolute sitting number; LLD/HLD/Java rings continue past row 88. |
| **MocksPractice project chat**, not a terminal | `START MOCK Compressed` | Creates a blinded four-slot mock. **60 minutes is candidate attempt time**; generation, grading and viva add time. Use as the weekly default when the whole day is bounded. |
| MocksPractice project chat | `DONE` | Submit the completed attempt, answer the viva, then receive score/editorials and Review OS corrections. Do not run mixed-sitting `done` for this mock. |
| MocksPractice project chat | `START MOCK Standard` / `START MOCK Full` / `START MOCK Micro` | Candidate attempt budgets are **120 / 180 / ~20 minutes**, respectively, **plus** generation and DONE/viva time. Standard or Full is for a larger breadth checkpoint when that extra wall-clock time is available; Micro fits a recovery week. Shorter modes have less coverage, not higher difficulty. |
| Review OS project chat | `review` | Runs due mixed revision through Review OS. Use it for due corrections or a planned replacement block, not as extra work after a completed 150-minute sitting. |
| PowerShell | `G:\TechStudyNotes\review-os\scripts\review-all.cmd daily` | Read-only view of due Review OS work across decks. Rate a matching card only after the actual attempt, through the Review OS workflow. |

The earlier `G:\TechStudyNotes\review-os\coach\coach.cmd plan` generates a **different calendar-based cue schedule**. It is retained for historical comparison and diagnostics. **Do not run it as a second daily plan** alongside the mixed-sitting queue. Its `record-day`, `record`, and `status` write/read a different event log and do not advance the mixed-sitting pointer.

## Which file has which job

| Artifact | Role in the system | When to open it |
| --- | --- | --- |
| [September hourly wins](SEP_20_30_2026_INTERVIEW_READY_HOURLY_WINS.md) | Closes the finite preparation sprint; contains rejection, three-bad-day, job-search, and interview-call overrides. Its older October calendar paragraphs are superseded by this page and the mixed circuit. | Now through 30 September; later only for overrides. |
| [150-minute mixed circuit](OCTOBER_150_MIN_MIXED_INTERVIEW_CIRCUIT.md) | **Primary October workout** with exact clock, 88-row table, prompt links, local Java references, and score rules. | Each normal full sitting from 5 October. |
| [263-question DSA ledger](DSA_FULL_COLD_ATTEMPT_LEDGER.md) | Source inventory and detailed DSA history. Its printed order is for lookup, not the practice sequence. Mark the earlier ~90 hands-on items only where you can identify them; do not invent green cold scores. | After a full DSA attempt or one-time history check, not as a second daily queue. |
| [24-anchor pattern wheel](DSA_PATTERN_WHEEL_24.md) and [seven-day v15 sheet](DSA_7-Day_Hourly_WIN_FINAL_v15_HighSignal_Pattern_Triggers.md) | High-signal pattern explanations and prior preparation provenance. | When a failed pattern needs a precise repair or a source example; no parallel A01 cursor. |
| [Four-domain warm-up](DAILY_FOUR_DOMAIN_WARMUP.md) and [earlier coach guide](../../../../review-os/coach/README.md) | Earlier calendar-cue design retained as reference. The current 20-minute minimum is in the mixed circuit. | Only to inspect the earlier model or diagnose its generated cues. |
| [Requirement audit snapshot](INTERVIEW_PREP_REQUIREMENTS_AND_DELIVERY_AUDIT_2026-09-20.md) | Records what was built and unproved before this single-entry-point correction. | For traceability; use the gap table below for current decisions. |
| [MocksPractice](../../../../MocksPractice/README.md) | Blinded interview assessment, feedback, changed constraints and correction scheduling. | Weekly mock day and role-specific interview practice. |
| [LLD learning order](../../../../LLDProjects/docs/LLD_LEARNING_ORDER_ROI_RANKING.md), [HLD catalog](../../../../SystemDesignProjects/LEARNING-ORDER.md), Java/deep company sheets, Educative, AlgoMonster | Source/reference libraries. The mixed card selects a bounded interview core; a confirmed gap can promote one distinct move. | After a cold attempt or for a booked format, never as blanket daily reading. |

## Current gaps, correction, and proof

| Gap today | Correction within this system | Evidence that it closed |
| --- | --- | --- |
| **Conflicting daily plans and double counting.** The old `coach.cmd plan` and new queue both sounded mandatory. | This page names one normal full queue. The old calendar card is diagnostic only. Mocks and repairs replace a sitting. | One next-sitting pointer; no day has both `record-day` and mixed `done` as required work. |
| **A weekly mock was described as 120–150 minutes without respecting modes.** | Use MocksPractice's actual candidate budgets above. Default weekly `Compressed` is 60 minutes **plus** unbounded setup/grading/viva; `Standard` is 120, `Full` is 180. | Recorded completed mock with scorecard and viva, not merely `START`. |
| **No evidence yet that you are one-day-call ready.** A timetable cannot prove it. | Complete cold mixed sittings, a weekly blinded mock, and one simulated 24-hour role-specific refresh. Target two of the latest three relevant mocks with no critical unresolved coding/design failure before calling one-day refresh reliable. | Mock scorecards, exact repaired failures, and a role-specific refresh that fits a real day. |
| **The earlier ~90 DSA hands-on questions are not identified by title.** | Mark `Past pass?` in the ledger only when certain; score each current cold attempt independently. | Named prior-pass rows and new cold results, with no bulk-imported greens. |
| **Distinct URL is not a distinct skill, and 263 full solutions cannot stay instantly callable on a 150-minute budget.** | Use five rapid cues (263 in 53 completed sittings), three substantive DSA turns (263 in 88), one full cold code per sitting (all designated once over three circuits), mock failures, and changed constraints. | Rising cold-code and mock scores; fewer repeated pattern confusions, not just URL coverage. |
| **Personal-gap adaptation is still partial.** The mixed coach retries last-session weak cues, but it does not automatically import MocksPractice outcomes or write Review OS ratings. | After each `DONE`, take the single highest-impact failure into a replacement repair sitting or due Review OS session. Keep the exact error in the score line. Do not duplicate FSRS ratings. | A dated repair with a later cold recheck; open critical failures decline. Automatic cross-system transfer remains an engineering gap. |
| **LLD/HLD/Java/SQL and all notes are not completely inventoried.** | The selected recurring core is 15 LLD, 8 HLD, 20 Java questions. Use a real/mock failure or booked SQL-heavy format to add a *small* exact deck; source pages are lookup material until then. | Cold proof for the format actually being interviewed; no false claim that all files are maintained. |
| **Job links are not applications; callback rate is unknown.** | Log every relevant sent action below. After 20 relevant actions with zero screens, review targeting, résumé evidence and referrals before increasing DSA hours. | Sent-action denominator, screens and next steps, then changed targeting if the funnel is weak. |
| **Email delivery is unavailable.** | The scheduled task posts in this Codex conversation. | A connected email channel and verified send/notification path would be needed before promising email. |

## Weekly Sunday scorecard — 15 minutes, no syllabus expansion

Run mixed-sitting `status`; read the last MocksPractice scorecard; count sent job actions and screens. Write one line:

`Week ____ | completed full sittings __ | 20-minute minimum days __ | mock mode/result __ | DSA full G/Y/R __ | LLD/HLD/Java weakest exact item __ | one repaired gap __ | sent relevant actions __ | screens __ | energy 1–5 __ | next week's one priority __`

If **two of the latest three** full cold DSA attempts fail, next technical block repairs that move. If **three days** are unproductive, take the lighter protocol rather than increasing hours. If mock scores improve while energy falls, keep the recovery week; an offer search needs sustained ability and an active application funnel.

## Job funnel log — record only actions actually sent

The daily Codex task discovers roles; you decide whether to apply or request a referral. Keep the denominator here or in your own tracker using these exact columns. A role merely saved or opened is not “sent.”

| Date | Employer / exact role | Location | Official apply URL | Action sent: application/referral/follow-up | Reply or screen date | Next action/date |
| --- | --- | --- | --- | --- | --- | --- |
|  |  |  |  |  |  |  |

## What is *not* yet accomplished

No new October sitting or mock has been completed in the new logs as of this audit; the reported ~90 earlier DSA hands-on attempts remain your history, not invented scores. No application or callback count is inferred from the scheduled link search. This system is a way to **generate and preserve evidence of readiness** while ending the endless-prep phase. The evidence must come from your cold attempts, mocks, real interviews and actual job actions.
