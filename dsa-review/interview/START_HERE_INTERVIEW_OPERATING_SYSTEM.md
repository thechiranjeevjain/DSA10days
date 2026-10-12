# Start here — one interview preparation operating system

> **CURRENT ROUTING — do not record work here.** On an ordinary no-call day, the sole live pointer is the `▶ ACTIVE` row in the one-table [Single-Domain 8-Session Circular Queue](SINGLE_DOMAIN_8_SESSION_CIRCULAR_QUEUE.md). Use [Interview Ready Master Queue V2](INTERVIEW_READY_MASTER_QUEUE_V2.md) only for a deliberately scheduled mixed simulation. The [October circuit](OCTOBER_150_MIN_MIXED_INTERVIEW_CIRCUIT.md) and the older P01-P06 material below are historical references, not additional queues.

**Current as of 13 October 2026.** Open the single-domain queue whenever study time appears. With 15 minutes, complete only its Floor and keep the same row active. With a larger block, complete Floor then the 90-minute Core; choose before starting whether to add the optional same-domain Ceiling. After evidenced Core, close an unchosen Ceiling with `⊘ CLOSED` and advance to the printed Next row. Missed dates create no debt.

## Current routing — first applicable row wins

| Situation | Use | Result |
| --- | --- | --- |
| **Interview within 72 hours** | Freeze the ordinary pointer and use the matching company/format sheet for one simulation, exact repair, stories, logistics, and sleep. | Resume the frozen row afterward. |
| **Consequential real/mock failure** | Replace the next compatible session with the exact failed move or changed requirement. | Repair the failure; do not restart a course. |
| **Ordinary no-call interval** | Follow the `▶ ACTIVE` row in the [single-domain queue](SINGLE_DOMAIN_8_SESSION_CIRCULAR_QUEUE.md). | Floor = 15-minute progress; Core = completed domain artifact; Ceiling = optional depth. |
| **Scheduled broad rehearsal** | Run the active row in [Master Queue V2](INTERVIEW_READY_MASTER_QUEUE_V2.md). | One mixed cross-domain simulation; never an extra quota after the single-domain session. |
| **S08 mock** | In MocksPractice send `START MOCK Compressed`, then complete the attempt, `DONE`, grading, viva, and one repair. | Advance only after actual evidence. |
| **Fresh role/referral** | Spend one bounded opportunity block verifying the official posting and applying/following up. | Record sent action or honest zero-fit; saving a link is not evidence. |

## Historical architecture and command reference

## What “coach” means here

The **mixed-sitting coach** is a local Node script at [mixed-sitting.mjs](../../../../review-os/coach/mixed-sitting.mjs). It reads the existing DSA/design/Java source lists, prints the next 150-minute card, records your *actual* scores in `G:\TechStudyNotes\review-os\coach\mixed-sitting-state.json`, and advances a session pointer only after you run `done`. It is a scheduler and score log, not a tutor, interviewer, FSRS engine, or proof of readiness. Its user-facing workout, first 88 rows, and live no-call pipeline are consolidated in the [Master Queue V2](INTERVIEW_READY_MASTER_QUEUE_V2.md).

The **scheduled Codex task** named **Daily interview coach and job links** runs at 06:45 IST in this conversation. It reads the `▶ ACTIVE` session in the single-domain queue and shares its Floor, Core, optional Ceiling, evidence rule, career batch, and Next token. It repeats the active session until you report evidence. It also searches for fresh verified jobs and reports them in this conversation without writing them into the curriculum table. It never invents completion, advances a pointer, or applies to a job for you. Email delivery is not active.

**MocksPractice** is a separate adaptive mock-interview system. It generates and freezes an unseen question paper, runs the candidate attempt, viva and scoring, and sends corrections to Review OS. It is the readiness *test*. The mixed-sitting coach is the repeated training *workout*. [Review OS](../../../../review-os/README.md) owns spaced-review dates for cards that have recorded attempts. Do not count a quick cue as a solved problem or record one attempt as multiple independent proofs.

## The only next-session decision tree

Read top to bottom; the first applicable row wins. A replacement activity never adds a second technical quota to the same session.

| Situation | Do exactly this | What advances? |
| --- | --- | --- |
| **Real interview within 72 hours** | Use the September file's interview override: format-specific mock or question, one targeted repair, company/project story, logistics, sleep. Open the relevant [Goldman](GoldmanSachs_Master_Revision_DSA_LLD_HLD.md), [Point72](Point72_Master_Revision_HackerRank_SQL_DSA_LLD_HLD.md), or [Wells Fargo](WellsFargo_Master_Revision_DSA_LLD_HLD.md) sheet only for that confirmed format. | No; resume the held row afterward. |
| **Fresh verified high-fit role or warm referral needs action** | Use one 15-minute opportunity-interrupt packet for the next physical stage: verify, tailor, complete fields, submit, or follow up. Then resume the held technical packet. Maximum one interrupt before one technical packet. | Funnel stage may advance; technical pointer stays held. Funnel source advances only after sent/zero-fit evidence. |
| **A consequential real/mock failure** | Use the next available session for the exact failed code move or design requirement. This repair replaces the next P-session. If the issue is small, use the next card's built-in retry. | No pointer advances during the replacement repair. |
| **No interview booked; waiting-for-calls interval** | Follow the exact `NEXT` item in the [Master Queue V2](INTERVIEW_READY_MASTER_QUEUE_V2.md#live-control-panel). One active build track, maintenance, funnel work, benchmarks, and mocks remain parts of this one queue. | Packet advances after packet evidence. Larger pointers advance only at their stated exit rules. |
| **Only 15-30 minutes available** | Complete the one next 15-minute packet, or two packets in order. During P05/P06 use the H1-H4 holding ring. Save a resume token. | Packet or holding pointer advances with evidence; no false session, benchmark, or mock completion. |
| **Three attempted packets produce no useful output, or rejection causes a stall** | Reduce the held packet to one physical artifact: a method, test, paragraph, diagram edge, answer outline, or application field. Complete that before selecting anything else. | The same packet stays held until evidence exists. |

**Cycle structure:** Complete P01 -> P02 -> P03 -> P04 -> P05 -> P06 in order. P01-P04 are 12 ordered 15-minute packets, consumed separately or consecutively. P05 remains a contiguous benchmark; P06 remains a real mock. Let `n = Completed cycles + 1`: P06 cycles 1 and 3 use `START MOCK Compressed`, cycle 2 uses the Educative coding-pattern mock, and cycle 4 uses `START MOCK Micro`; repeat that four-cycle ring. Applications and referrals are inside the same packet system. There is no calendar quota and no catch-up work.

## P05 mixed-benchmark session - one command, one sitting, one career edge

Use this when the no-call pipeline reaches **P05**, or when another applicable rule explicitly assigns a full circuit and three hours are genuinely available. P01-P04 use the progress clock and P06 uses the mock workflow in the mixed-circuit file. It wraps the existing 150-minute circuit; it does not create another technical queue.

| Clock | Minutes | Do exactly this | Evidence / stop rule |
| --- | ---: | --- | --- |
| 00:00–02:30 | 150 | Run `node G:/TechStudyNotes/review-os/coach/mixed-sitting.mjs plan` and follow the printed [mixed-circuit clock](INTERVIEW_READY_MASTER_QUEUE_V2.md). This already contains three DSA turns, five rapid DSA cues, LLD, HLD, Java/backend, spaced changed-constraint recall, scoring, and one repair. | Run `done` only after the full sitting with actual scores. Stop every lane at its boundary. |
| 02:30–02:45 | 15 | **Job-funnel action:** submit one relevant application, send one authorized referral request/follow-up, or record `zero suitable roles today` after checking the verified links. | A sent action or an honest zero-fit entry. Saving a link is not evidence. Maximum one action; no poor-fit quota filling. |
| 02:45–03:00 | 15 | Do the sitting's **E01–E07 career-edge prompt printed by `plan`** from memory, then inspect one source only if needed. | One dated answer, query, story outline, or diagram. Stop at 15 minutes; do not turn it into a course session. |

### Fifteen-minute career-edge circular queue

This queue uses the same absolute sitting number as the technical circuit, so missed dates cannot repeatedly starve one area. After E07, return to E01.

| Edge | Exact prompt | Green evidence |
| --- | --- | --- |
| **E01 Résumé impact story** | Give one 2-minute `context → problem → action → measurable result → lesson` story from Morgan Stanley, Nasdaq, or another real project. Answer two likely follow-ups. | Concrete ownership, scale/result, trade-off, and no invented metric. |
| **E02 SQL and database reasoning** | Write or explain one query/index/execution-plan problem: joins, aggregation/windowing, pagination, isolation, locking, indexing, or a slow-query diagnosis. | Correct query or plan reasoning, one edge case, and one performance consequence. |
| **E03 Project architecture** | Explain one local project end to end: entry point, request/data path, state, concurrency, failure, observability, and one production limitation. | Five-minute coherent walkthrough plus one changed requirement. |
| **E04 Behavioral and leadership** | Answer one senior-engineer prompt: disagreement, incident, mentoring, prioritization, failure, influence without authority, or ambiguous requirements. | Specific situation, your decision, consequence, learning, and concise follow-ups. |
| **E05 Trading and risk domain** | Explain one domain flow: order lifecycle, pre-trade control, market data, exposure/limits, venue connectivity, reconciliation, or failure recovery. | Correct actors/data path, invariant, latency/failure concern, and one business trade-off. |
| **E06 System communication** | Take today's LLD or HLD and deliver a clean 10-minute interviewer-style explanation without code or notes; spend five minutes answering interruptions. | Structured requirements, decisive choices, one rejected alternative, and clear trade-off. |
| **E07 Evidence and funnel control** | Run `status`, read the latest mock scorecard, count sent job actions/screens, and fill the after-cycle score line below. | One priority for the next seven sittings; no new syllabus or backlog creation. |

**Warmth coverage:** P01-P04 and P05 keep the interview domains in circulation while one active build track advances. P05's circular queues rotate 263 DSA URLs, 15 LLDs, 8 HLDs, and 20 Java questions. The E01-E07 ring keeps resume evidence, SQL, project explanation, behavioral leadership, trading/risk language, communication, and cycle control active every seven completed mixed sittings. The funnel block keeps preparation connected to real interviews.

```mermaid
flowchart TD
    A[Read the decision tree] --> B{First applicable mode}
    B -->|Interview within 72 hours| I[Role-specific preparation]
    B -->|Consequential failure| R[Targeted repair]
    B -->|P06| M[MocksPractice]
    B -->|P01-P04| P[Build plus maintenance]
    B -->|P05| Q[Next mixed sitting n]
    P --> U[Advance one build unit]
    U --> V[Maintenance plus funnel]
    V --> W[Advance the P-session pointer]
    Q --> D[DSA row and cue ring]
    Q --> L[LLD n mod 15]
    Q --> H[HLD n mod 8]
    Q --> J[Java n mod 20]
    Q --> F[Design focus n mod 7]
    Q --> E[Career edge n mod 7]
    D --> T[150-minute technical sitting]
    L --> T
    H --> T
    J --> T
    F --> T
    E --> X[Three-hour extension]
    T --> S[Record actual scores and one repair]
    S --> N[Advance mixed row and P-session]
    M --> Z[Mock scorecard and one repair]
    Z --> W
    I --> K[Hold sitting n]
    R --> K
    M --> K
```

The queues choose **what to retrieve**. Scores measure the attempt. Repair, mocks, recovery, and the 72-hour interview rule choose **whether the normal queue runs at all**. Keeping those controls outside the circular content rings prevents an urgent failure or booked interview from waiting for its turn.

**Replacement rules still win:** P06 is the assigned mock session; platform setup/generation, candidate attempt, grading, and viva can have different wall-clock totals. A consequential repair replaces the matching packet sequence. A live interview within 72 hours replaces the normal pipeline. The four-cycle mock ring is defined in V2. A missed date leaves every pointer where it is, while any later 15-minute slot can resume the exact packet.

## Exact course protocol — one course builds; evidence selects repairs

In P01-P04, only the **active build track** may advance in source order. All other courses are reference libraries. Outside that build block, open a course only when a cold mixed sitting, mock, or real interview produces a named failure. A dashboard, progress bar, or streak never chooses the work. This preserves visible completion without allowing four parallel curricula.

### The one repair loop

1. Write one sentence: `I failed ___ because I could not ___`.
2. Attempt the exact skill cold before opening a lesson. Save code, a diagram, or a spoken-answer note.
3. Open **one** matching lesson or problem for **at most 20 minutes**. Read only enough to identify the missing trigger, invariant, data path, trade-off, or Java rule.
4. Close the lesson. Reconstruct the same skill from blank for **20–30 minutes**, then handle one changed constraint.
5. Score the closed-book retry. Record one exact remaining miss through the normal mixed-sitting/Review OS evidence flow. Stop; do not continue to the next lesson.

The useful completion unit is a **closed-book retry**, not a watched/read lesson, solved example, chapter, certificate, course percentage, or streak.

| Resource | Open it only when | Exact bounded use | Required output / stop rule |
| --- | --- | --- | --- |
| [Educative system-design roadmap](https://www.educative.io/interview-prep/system-design?page=roadmap) | The HLD lane, a mock, or a real interview exposes one named gap such as partitioning, consistency, caching, queues, idempotency, capacity, or failure recovery. | Start with a 10–15-minute blank design. Read only the matching concept/example for up to 20 minutes. Close it and redraw the request/data path, state, failure handling, scale choice, and one trade-off in 20–30 minutes. | A fresh diagram and a 5-minute explanation that survives one changed requirement. Do not traverse the roadmap top to bottom. |
| [Educative Decode the Coding Interview in Java](https://www.educative.io/courses/decode-coding-interview-java) | Its DSA Translation track is active, or a DSA pattern remains Y/R after a cold attempt. | When active, complete one project chapter at a time through selected feature/DIY cold attempts. When inactive, search only for the failed move. Deduplicate against the local 263-question ledger. | Runnable Java, edge tests, invariant, complexity, and a 60-second explanation. A chapter closes with an unseen cold problem, not lesson-count completion. |
| [AlgoMaster LLD](https://algomaster.io/learn/lld/what-is-lld) | The LLD Core track is active, or a named LLD failure requires repair. | Follow Units 01-21 in the mixed-circuit file. Use no more than 25 minutes of source before closed-book modeling/coding. Local LLD projects provide reconstruction evidence. | Required unit artifact plus the final unseen machine-coding rehearsal. |
| AlgoMaster HLD | HLD Core is active and the PDF foundation is complete, or an HLD failure needs an unseen case study. | Use it for one case study or missing design move; do not duplicate PDF theory merely to finish a dashboard. | Fresh diagram, data path, failures, scale choice, trade-off, and changed requirement. |
| [AlgoMaster dashboard](https://algomaster.io/dashboard) | The mixed queue or a mock needs one unseen validation problem, or the 263-question ledger has a confirmed implementation-family gap. | Select one problem from the named weak pattern. Check whether it duplicates a local problem. If duplicate, add a changed constraint or choose a distinct implementation move. Use the normal 40-minute DSA standard and inspect its explanation only after scoring. | One cold score and one exact failure/repair. Do not run an independent AlgoMaster daily streak or second problem queue. The logged-in track inventory must be mapped before claiming unique coverage. |
| [Educative coding-pattern mock](https://www.educative.io/mock-interview?category_id=coding-interviews&interview_id=coding-patterns-general) | P06 cycle 2 of each four-cycle block, or a booked round that closely matches its format. | It **replaces** the internal mock at that P06; use the platform timer and attempt cold. Do not run both mocks for the same checkpoint. | Save the asked question, where you broke, score/feedback, and at most one repair. It does not advance the mixed-sitting pointer. |

**Session rule:** The P01-P06 pointer chooses the next normal session. P01-P04 advance one active course unit; P05 measures mixed performance; P06 tests unseen performance. A real interview within 72 hours holds all pointers and may select one role-relevant module, but no broad course begins in that window.

**Name check:** the supplied dashboard URL is **AlgoMaster**. Earlier notes also say **AlgoMonster**, which is a different product. Do not merge their progress or inventories unless both subscriptions and their exact tracks are deliberately audited.

### How courses enter the operating loop

```mermaid
flowchart TD
    A[September close-out or October mixed sitting] --> B[Cold attempt]
    B --> C[Score evidence]
    C -->|Green| D[Continue circular queue]
    C -->|Small yellow or red| E[Next sitting mini-round]
    C -->|Consequential failure| F[Replacement repair block]
    F --> G[One matching Educative or AlgoMaster item]
    G --> H[Close source and reconstruct]
    H --> I[Changed requirement]
    I --> J[Record result in normal evidence system]
    J --> D
    K[P06 mock] --> C
    L[Real interview] --> C
```

The course platforms do not select the next task. Evidence from a cold sitting, mock, or real interview selects a single repair; successful reconstruction returns the work to the same circular queue.

## Commands — which surface, what it changes

| Where | Exact command or message | What happens |
| --- | --- | --- |
| PowerShell, any directory | `node G:/TechStudyNotes/review-os/coach/mixed-sitting.mjs plan` | **Read-only.** Shows the next 150-minute card and local links. Starts at sitting 1. |
| PowerShell | `node G:/TechStudyNotes/review-os/coach/mixed-sitting.mjs status` | Shows next number and recorded coverage. A zero today means this new log has no evidence; it does not erase your earlier ~90 attempts. |
| PowerShell, **after a contiguous P05 only** | `node G:/TechStudyNotes/review-os/coach/mixed-sitting.mjs done G G Y G Y G GGYGU "one exact repair"` | Replace example scores with your six lane results, cue results, and exact repair or `none`. Records evidence and advances one mixed row. Never run this for P01-P04 packets, holding packets, P06, or a skipped session. |
| PowerShell | `node G:/TechStudyNotes/review-os/coach/mixed-sitting.mjs plan 89` | Read-only preview of any absolute sitting number; LLD/HLD/Java rings continue past row 88. |
| **MocksPractice project chat**, not a terminal | `START MOCK Compressed` | Creates a blinded four-slot mock. **60 minutes is candidate attempt time**; generation, grading and viva add time. Use on P06 cycles 1 and 3 of each four-cycle block. Cycle 2 uses the Educative coding-pattern mock and cycle 4 uses Micro. |
| MocksPractice project chat | `DONE` | Submit the completed attempt, answer the viva, then receive score/editorials and Review OS corrections. Do not run mixed-sitting `done` for this mock. |
| MocksPractice project chat | `START MOCK Standard` / `START MOCK Full` / `START MOCK Micro` | Candidate attempt budgets are **120 / 180 / ~20 minutes**, respectively, **plus** generation and DONE/viva time. Standard or Full is for a larger breadth checkpoint when that extra wall-clock time is available; Micro fits the fourth completed cycle or a recovery checkpoint. Shorter modes have less coverage, not higher difficulty. |
| Review OS project chat | `review` | Runs due mixed revision through Review OS. Use it for due corrections or a planned replacement block, not as extra work after a completed 150-minute sitting. |
| PowerShell | `G:\TechStudyNotes\review-os\scripts\review-all.cmd daily` | Read-only view of due Review OS work across decks. Rate a matching card only after the actual attempt, through the Review OS workflow. |

The earlier `G:\TechStudyNotes\review-os\coach\coach.cmd plan` generates a **different calendar-based cue schedule**. It is retained for historical comparison and diagnostics. **Do not run it as a second daily plan** alongside the mixed-sitting queue. Its `record-day`, `record`, and `status` write/read a different event log and do not advance the mixed-sitting pointer.

## Which file has which job

**One mission:** produce cold, explainable interview performance while keeping the application funnel active. The files are a library around that mission, not 42 parallel plans. The normal path is:

`this page → next P-session → one active build unit plus maintenance/funnel → P05 mixed benchmark → P06 blinded mock → one exact repair when needed → repeat`

Only the **single-domain queue's `▶ ACTIVE` session** chooses ordinary work. A live-interview override or consequential repair replaces that session temporarily. Master Queue V2 is an on-demand mixed simulation; ledgers and reference files support the ordinary queue. Neither a saved URL nor reading a solution counts as a solved interview problem.

| Artifact | Role in the system | When to open it |
| --- | --- | --- |
| [September hourly wins](SEP_20_30_2026_INTERVIEW_READY_HOURLY_WINS.md) | Historical finite preparation sprint; still contains useful rejection, low-output, job-search, and interview-call overrides. Its October calendar paragraphs are superseded by this page and the mixed circuit. | Open only for those overrides or historical evidence. |
| [Single-Domain 8-Session Circular Queue](SINGLE_DOMAIN_8_SESSION_CIRCULAR_QUEUE.md) | **Ordinary-day control surface:** one primary domain per session, 15-minute Floor, 90-minute Core, optional same-domain Ceiling, mocks, career batches, spacing, and one Next pointer. | Open whenever ordinary study time appears. Execute only the active row and advance after evidenced Core or Full completion. |
| [Interview Ready Master Queue V2](INTERVIEW_READY_MASTER_QUEUE_V2.md) | On-demand mixed simulation spanning build, DSA, LLD/HLD, Java, career, cues, and interview edges. | Open only when a broad mixed rehearsal is deliberately scheduled. It replaces that ordinary session; it is never an extra quota. |
| [October circuit snapshot](OCTOBER_150_MIN_MIXED_INTERVIEW_CIRCUIT.md) and [original packet log](INTERVIEW_PIPELINE_PACKET_LOG.md) | Superseded historical snapshots retained for provenance. | Do not update or use them to select work. |
| [263-question DSA ledger](DSA_FULL_COLD_ATTEMPT_LEDGER.md) | Source inventory and detailed DSA history. Its printed order is for lookup, not the practice sequence. Mark the earlier ~90 hands-on items only where you can identify them; do not invent green cold scores. | After a full DSA attempt or one-time history check, not as a second daily queue. |
| [24-anchor pattern wheel](DSA_PATTERN_WHEEL_24.md) and [seven-day v15 sheet](DSA_7-Day_Hourly_WIN_FINAL_v15_HighSignal_Pattern_Triggers.md) | High-signal pattern explanations and prior preparation provenance. | When a failed pattern needs a precise repair or a source example; no parallel A01 cursor. |
| [Four-domain warm-up](DAILY_FOUR_DOMAIN_WARMUP.md) and [earlier coach guide](../../../../review-os/coach/README.md) | Earlier calendar-cue design retained as reference. The current 15-minute packet queue is in the mixed circuit. | Only to inspect the earlier model or diagnose its generated cues. |
| [Requirement audit snapshot](INTERVIEW_PREP_REQUIREMENTS_AND_DELIVERY_AUDIT_2026-09-20.md) | Records what was built and unproved before this single-entry-point correction. | For traceability; use the gap table below for current decisions. |
| [MocksPractice](../../../../MocksPractice/README.md) | Blinded interview assessment, feedback, changed constraints and correction scheduling. | P06 and role-specific interview practice. |
| [LLD learning order](../../../../LLDProjects/docs/LLD_LEARNING_ORDER_ROI_RANKING.md), [HLD catalog](../../../../SystemDesignProjects/LEARNING-ORDER.md), Java/deep company sheets, Educative, AlgoMaster (and AlgoMonster only if separately owned) | One may be the active build source; all others remain references. The mixed card maintains a bounded interview core, and a confirmed gap can promote one distinct repair. | Use the active source only in P01-P04; otherwise open a source after a cold attempt or for a booked format. |

### Complete map of this folder — every top-level Markdown file

The status in the middle column is the instruction: **Run** means it can select work; **Record** holds evidence; **Lookup** is opened for an exact gap; **Historical** is retained for provenance, not a second queue. The [22 pattern chapters and their index](patterns/README.md) sit under `patterns/` and are lookup pages, not 23 more daily tasks.

| Role / status | Files in this folder | How they connect |
| --- | --- | --- |
| **Control — Run** | [Single-Domain 8-Session Circular Queue](SINGLE_DOMAIN_8_SESSION_CIRCULAR_QUEUE.md) | This is the only file that selects ordinary work. The Master Queue is an on-demand mixed simulation; this architecture page is explanatory; September retains only its explicit live-interview and rejection protocols. |
| **Evidence — Record / inspect** | Master Queue V2 unified ledger · [263-item DSA cold-attempt ledger](DSA_FULL_COLD_ATTEMPT_LEDGER.md) · [DSA recall ledger](DSA_RECALL_LEDGER_v1.md) · [Java recall ledger](JAVA_RECALL_LEDGER_v1.md) · [active-recall failure ledger](ACTIVE_RECALL_FAILURE_LEDGER.md) · [review dashboard](06_REVIEW_DASHBOARD.md) | The V2 ledger owns daily resume state; the mixed coach's `done` log owns completed P05 benchmarks. Use the other ledgers for detailed named attempts or existing review workflows. Do not copy one attempt into several independent green scores. |
| **Company / format — Lookup after a call** | [Goldman master](GoldmanSachs_Master_Revision_DSA_LLD_HLD.md) · [Goldman VP rehearsal](GOLDMAN_SACHS_VP_FINAL_REHEARSAL_SCRIPT.md) · [Point72 master](Point72_Master_Revision_HackerRank_SQL_DSA_LLD_HLD.md) · [Point72 morning order](POINT72_TREASURY_TOMORROW_MORNING_STUDY_ORDER.md) · [Wells Fargo master](WellsFargo_Master_Revision_DSA_LLD_HLD.md) | When a confirmed round matches the format, take only its relevant questions, stories and logistics into the 72-hour override. The dated morning order is historical. |
| **Fast DSA retrieval — Lookup** | [Pattern recognition 80/20](00_PATTERN_RECOGNITION_80_20.md) · [one-line recall](02_ONE_LINE_RECALL_ALL_PROBLEMS.md) · [crisp interview answers](03_CRISP_INTERVIEW_ANSWERS.md) · [one-hour table](14_ONE_HOUR_BEFORE_INTERVIEW_MASTER_TABLE.md) · [21 one-page cards](DSA_21_Pattern_Cards_One_Page_Recall_FINAL.md) · [21 reconstruction cards](DSA_21_Pattern_Cards_CUSTOMER_READY_FINAL.md) · [24-anchor pattern wheel](DSA_PATTERN_WHEEL_24.md) · [audited pattern OS](DSA_MUST_MEMORIZE_PATTERN_OS_FINAL_AUDITED.md) | Open **one** representation of the failed pattern or one brief pre-interview cue. These are overlapping views, not additive quotas. |
| **DSA indexes / explanations — Lookup** | [Ranked table](01_ZERO_TO_HERO_RANKED_TABLE.md) · [ranking audit](05_RANKING_METHODOLOGY_AND_AUDIT.md) · [solved index](07_LEETCODE_SOLVED_INDEX.md) · [project/pattern tree](08_PROJECT_STRUCTURE_AND_PATTERN_TREE.md) · [curriculum TOC](09_LEETCODE_CURRICULUM_TOC.md) · [articulation table](12_MASTER_DSA_INTERVIEW_ARTICULATION_TABLE.md) · [complexity table](13_MASTER_TIME_SPACE_COMPLEXITY_TABLE.md) · [pattern chapters](patterns/README.md) | Find a local solution, disambiguate a pattern, or repair an explanation or complexity proof **after** attempting. Do not follow the ranked order as a second queue. |
| **Older DSA maps — Historical lookup** | [150 brain map](DSA_150_Human_Brain_Mind_Map.md) · [170 base map](DSA_170_Brain_Map.md) · [170 final map](DSA_170_Brain_Map_FINAL.md) · [170 Java-skeleton map](DSA_170_Brain_Map_With_Java_Skeletons.md) | Alternative visual/summary views of overlapping DSA material. Use a specific row if helpful; no independent completion target. |
| **Earlier schedules — Historical** | [Two/seven-day plans](04_TWO_DAY_AND_SEVEN_DAY_PLANS.md) · [after-seven-day extension](10_AFTER_7_DAY_EXTENSION_PLAN.md) · [90-problem cutoff](11_ACTIVE_90_PLAN_CUTOFF_AND_EXTENSION.md) · [18-problem hourly schedule](DSA_18_Problems_Hourly_Schedule_All_Original_Columns.md) · [seven-day performance sprint](DSA_7-Day_Interview_Performance_Sprint.md) · [seven-day v15 hands-on record](DSA_7-Day_Hourly_WIN_FINAL_v15_HighSignal_Pattern_Triggers.md) · [earlier four-domain warm-up](DAILY_FOUR_DOMAIN_WARMUP.md) | Your v15 hands-on pass remains real history. These schedules explain previous choices or supply a targeted drill. Their clocks, cursors and quotas do not run alongside the current plan. |
| **Earlier entry points — Historical pointers** | [Folder README / DSA cockpit](README.md) · [Goldman-era study roadmap](START_HERE_INTERVIEW_STUDY_SEQUENCE_AND_ROADMAP.md) | Both now point here. The roadmap's 10 September Goldman instructions are historical; neither is a competing front door. |
| **Audit — Traceability** | [Requirements and delivery audit](INTERVIEW_PREP_REQUIREMENTS_AND_DELIVERY_AUDIT_2026-09-20.md) | Explains why this system exists and what was still unproved at the audit. The decision tree on this page is current authority. |

**Where the source code lives:** DSA links in the circuit lead into `G:\TechStudyNotes\Codes\DSA10days`; LLD links lead into `G:\TechStudyNotes\LLDProjects`; HLD and Java examples lead primarily into `G:\TechStudyNotes\SystemDesignProjects`. The Java/backend column now links a related local `.java` example for each J01–J20 question, with a scope note where the example is incomplete. Read the question first, reconstruct cold, score, then inspect code. A JVM or SQL concept can require an explanation or tool output that no Java source file alone can prove.

**Where other systems fit:** `START MOCK Compressed` is a message in the **MocksPractice project chat**, which tests unseen performance and provides feedback. `review` is a message in the **Review OS project chat**, which handles due corrections. The local mixed coach and Master Queue support deliberately scheduled mixed rehearsals. The single-domain queue remains the ordinary human-facing state and resume point. The scheduled job-link task posts fresh verified links in this Codex conversation; it does not send applications or email. These systems feed one loop, but their scores and cursors are not interchangeable.

## Current gaps, correction, and proof

| Gap today | Correction within this system | Evidence that it closed |
| --- | --- | --- |
| **Conflicting daily plans and double counting.** The old `coach.cmd plan` and new queue both sounded mandatory. | This page names one P-session/packet queue. The old calendar card is diagnostic only. Mocks and repairs replace the matching work. | One next packet; no date has two required planners or duplicate scores. |
| **Mock timing was described as 120-150 minutes without respecting modes.** | At P06 use the actual candidate budget: Compressed is 60 minutes plus variable setup, grading, and viva; Standard is 120 and Full is 180. | Recorded completed mock with scorecard and viva, not merely `START`. |
| **No evidence yet that you are one-day-call ready.** A timetable cannot prove it. | Complete P05 cold mixed benchmarks, P06 blinded mocks, and one simulated 24-hour role-specific refresh. Target two of the latest three relevant mocks with no critical unresolved coding/design failure before calling one-day refresh reliable. | Mock scorecards, exact repaired failures, and a role-specific refresh that fits a real day. |
| **The earlier ~90 DSA hands-on questions are not identified by title.** | Mark `Past pass?` in the ledger only when certain; score each current cold attempt independently. | Named prior-pass rows and new cold results, with no bulk-imported greens. |
| **Distinct URL is not a distinct skill, and 263 full solutions cannot stay instantly callable on a 150-minute budget.** | Use five rapid cues (263 in 53 completed sittings), three substantive DSA turns (263 in 88), one full cold code per sitting (all designated once over three circuits), mock failures, and changed constraints. | Rising cold-code and mock scores; fewer repeated pattern confusions, not just URL coverage. |
| **Personal-gap adaptation is still partial.** The mixed coach retries last-session weak cues, but it does not automatically import MocksPractice outcomes or write Review OS ratings. | After each `DONE`, take the single highest-impact failure into a replacement repair sitting or due Review OS session. Keep the exact error in the score line. Do not duplicate FSRS ratings. | A dated repair with a later cold recheck; open critical failures decline. Automatic cross-system transfer remains an engineering gap. |
| **LLD/HLD/Java/SQL and all notes are not completely inventoried.** | The selected recurring core is 15 LLD, 8 HLD, 20 Java questions. Use a real/mock failure or booked SQL-heavy format to add a *small* exact deck; source pages are lookup material until then. | Cold proof for the format actually being interviewed; no false claim that all files are maintained. |
| **Job links are not applications; callback rate is unknown.** | Log every relevant sent action below. After 20 relevant actions with zero screens, review targeting, résumé evidence and referrals before increasing DSA hours. | Sent-action denominator, screens and next steps, then changed targeting if the funnel is weak. |
| **Email delivery is unavailable.** | The scheduled task posts in this Codex conversation. | A connected email channel and verified send/notification path would be needed before promising email. |

## After-P06 scorecard — 15 minutes, no syllabus expansion

Run mixed-sitting `status`; read the last MocksPractice scorecard; count sent job actions and screens. Write one line:

`Cycle ____ | P-sessions completed __ | 15-minute packets completed __ | holding packets __ | mock mode/result __ | DSA full G/Y/R __ | LLD/HLD/Java weakest exact item __ | one repaired gap __ | sent relevant actions __ | screens __ | energy 1-5 __ | next packet __`

If **two of the latest three** full cold DSA attempts fail, the next DSA packet repairs that move. If **three attempted packets** are unproductive, shrink the held packet to one physical artifact instead of increasing hours. If mock scores improve while energy falls, use Micro on the fourth cycle and preserve recovery; an offer search needs sustained ability and an active application funnel.

## Job funnel log — record only actions actually sent

The daily Codex task discovers roles; you decide whether to apply or request a referral. Keep the denominator here or in your own tracker using these exact columns. A role merely saved or opened is not “sent.”

| Date | Employer / exact role | Location | Official apply URL | Action sent: application/referral/follow-up | Reply or screen date | Next action/date |
| --- | --- | --- | --- | --- | --- | --- |
|  |  |  |  |  |  |  |

## What is *not* yet accomplished

No new October sitting or mock has been completed in the new logs as of this audit; the reported ~90 earlier DSA hands-on attempts remain your history, not invented scores. No application or callback count is inferred from the scheduled link search. This system is a way to **generate and preserve evidence of readiness** while ending the endless-prep phase. The evidence must come from your cold attempts, mocks, real interviews and actual job actions.
