# Goldman Sachs VP CoderPad - Coach-Led Final Execution Plan

**Candidate:** Chiranjeev/Cheranjeev Jain - verify the spelling used in the recruiter record; the supplied resume currently renders “Cheranjeev Jain”

**Interview:** Wednesday, 9 September 2026, 15:00-16:00 IST

**Language:** Java

**Plan reset:** Friday, 4 September 2026, 22:00 IST

**Target:** give the first-round CoderPad the fairest possible chance; do not optimize for syllabus completion

This is the only live calendar. It replaces the earlier hypothesis that curriculum Days 5 and 6, 90 problems, or all 170 problems must be completed before the interview.

Companion files:

- [Evidence, Goldman problem cards, and solution vault](./GOLDMAN_SACHS_VP_CODERPAD_SEP_9_2026_MASTER_PLAN.md)
- [Spoken answers and recovery scripts](./GOLDMAN_SACHS_VP_FINAL_REHEARSAL_SCRIPT.md)
- [Original seven-day curriculum](./DSA_7-Day_Hourly_WIN_FINAL_v15_HighSignal_Pattern_Triggers.md) - a source bank, not the calendar

---

## 1. Coach's decision

The immediate bottleneck is not exposure. It is unmeasured performance.

You already have:

- high DSA exposure and a large Java solution corpus;
- 17 interview-sized LLD projects;
- 31 HLD/system-design learning units;
- strong resume alignment through Java/Spring, in-memory pre-trade risk, production problem solving, releases, and mentoring;
- extensive notes and paid Educative and AlgoMonster access.

You do not yet have a completed, scored mock in `G:\TechStudyNotes\MocksPractice`. Its mastery, failure, and coverage ledgers are all unassessed. Therefore more unique-problem coverage cannot be assumed to improve the real bottleneck.

### Training priority until Wednesday

| Share | Work | Why |
|---:|---|---|
| 65% | blind/timed DSA performance and targeted repair | the scheduled first round is a 60-minute CoderPad |
| 15% | clean Java and debugging reliability | correct ideas must become runnable code under observation |
| 10% | introduction, resume depth, and behavioral judgment | reports show some CoderPads include an intro or scenarios |
| 10% | resume-aligned Java/concurrency/risk/LLD/HLD defense | protects senior signal without opening a second syllabus |

### What is explicitly not a goal

- Completing 90 before Sunday.
- Touching the remaining 80 of 170.
- Completing a course module in Educative or AlgoMonster.
- Revising all 17 LLDs or 31 HLD units.
- Memorizing the reported Goldman question list.
- Doing more than two high-pressure simulations in one day.

If a curriculum Day-5/Day-6 problem appears in a targeted slot, solve it. Otherwise its unfinished status is irrelevant before Wednesday.

---

## 2. Evidence behind the decision

Public reports are anecdotal, not promises, but the repeated round shape is consistent enough for training:

- often two coding questions in one hour;
- arrays/strings, hash maps, two pointers, BFS/DFS, binary search, DP, and clean test cases recur;
- Trapping Rain Water appears repeatedly across levels;
- reported examples include Highest Average Score/IP frequency, String Compression, Number of Islands, Koko-style binary search, Container With Most Water, Word Break, Validate BST, Minimum Window/at-most-K, and graph/grid tasks;
- some reports include a short introduction or behavioral scenario;
- Goldman publicly emphasizes algorithms, distributed systems, databases, scalable systems, low-latency infrastructure, and collaboration.

The resume makes the senior narrative simple: you build Java/Spring financial systems, with direct pre-trade risk, in-memory checks, production issue resolution, release leadership, and mentoring. Do not dilute this by trying to sound expert in every repository.

---

## 3. Performance definitions

### A hands-on problem is interview-ready only when

- [ ] the contract and assumptions were clarified;
- [ ] a correct baseline was stated;
- [ ] the repeated work/bottleneck was named;
- [ ] the optimized invariant was stated before code;
- [ ] coherent Java was written from blank;
- [ ] normal, boundary, and adversarial cases were tested;
- [ ] time and auxiliary-space complexity were explained;
- [ ] one changed constraint was answered without reopening notes.

### Score every attempt

- **G1:** clean cold performance without help.
- **G2:** clean reconstruction after one targeted repair.
- **Y:** approach is substantially correct but one material weakness remains.
- **R:** solution lookup, wrong invariant, incoherent code, or inability to explain why it works.

Failure codes: **P** pattern, **I** invariant, **D** data structure, **J** Java, **E** edge case, **C** complexity, **B** debugging, **M** memorized/not understood, **V** verbal communication.

### A 60-minute CoderPad mock passes only when

- [ ] no solution, hint, notes, course, or AI answer is opened;
- [ ] problem 1 is runnable or fully dry-runnable by minute 28;
- [ ] problem 2 has a correct approach by minute 40;
- [ ] problem 2 is runnable or precise to the last missing lines by minute 55;
- [ ] at least three tests are stated for each completed problem;
- [ ] there is no unexplained silence longer than 45 seconds;
- [ ] a hint, if offered, is integrated without defensiveness;
- [ ] the final complexity statement is correct.

Perfection is not the pass condition. A disciplined recovery from one bug can still pass.

---

## 4. Resource rules

### MocksPractice - primary measurement tool

Use `G:\TechStudyNotes\MocksPractice` for the first diagnostic.

1. Open that repository in Codex.
2. Say `START MOCK Compressed`.
3. Work only in the returned `sessions/session-0003/` directory.
4. Do not inspect references or solutions.
5. When the attempt is complete, say `DONE` and finish the viva before reading the score/editorials.

The mock is about 60 minutes, but reserve 90 minutes for generation, attempt, viva, and scoring.

### Educative and AlgoMonster - repair tools only

They are not scheduled courses this week.

- Open one only after a Y/R.
- Search for the exact failed generator: for example, monotonic-stack boundaries, first-true binary search, multi-source BFS, or DP state.
- Maximum source time: 12 minutes.
- Close it and reconstruct from blank for 18 minutes.
- If both platforms cover the gap, choose the shorter explanation; do not compare courses.
- A watched lesson does not change the score. Only the closed-book retry can produce G2.

### DSA10days - source and repair bank

Use existing local Java and the master solution vault only after the cold timer. Do not browse all problem indexes.

### LLDProjects - one narrow defense

Only these are in scope before round 1:

1. `pre-trade-risk-engine` - rule pipeline, integer ticks/overflow, explainable rejection, and stateful extensions.
2. `token-bucket-rate-limiter` - lazy refill, atomic acquire, per-key state, and multi-instance limitation.

No implementation rebuild is scheduled. Draw and explain from blank.

### SystemDesignProjects - one narrow defense

Only these are in scope:

1. `java-concurrency-lab` - check-then-act invariant, per-client lock, bounded executor, backpressure, immutable snapshots.
2. `trading-risk-platform` - fail-closed risk, atomic reservation, auditability, dependency failure, and recovery trade-offs.

These are reference models. Never imply that every project feature was part of your Nasdaq production work.

---

## 5. The Goldman Core-12

These are performance probes, not a predicted exam. They span the highest-return families and reported shapes.

| # | Anchor | Generator that must be retrievable |
|---:|---|---|
| 1 | Highest Average Score / IP frequency | map aggregation; sum/count; tie and ordering policy |
| 2 | String Compression | run boundary; read/write indices; multi-digit count |
| 3 | Trapping Rain Water | finalize the side with the safe lower boundary |
| 4 | Container With Most Water | move the limiting wall; prove discarded widths cannot win |
| 5 | Minimum Window / at most K distinct | validity count; expand then shrink under a precise condition |
| 6 | Koko / first-true binary search | monotonic feasibility; smallest true boundary; overflow-safe math |
| 7 | Number of Islands | every unseen land cell starts one component traversal |
| 8 | 01 Matrix / grid shortest path | multi-source BFS; first reach is shortest |
| 9 | Validate BST | ancestor bounds, not parent-only comparisons |
| 10 | Word Break or Coin Change | explicit DP state, base case, transition, iteration order |
| 11 | Gas Station | total feasibility plus why a negative prefix invalidates starts |
| 12 | Largest Rectangle or Task Scheduler | boundary/frequency invariant and adversarial ties |

### Secondary probes - use only when the diagnostic says the family is weak

- Meeting Rooms / Merge Intervals
- Top K Frequent / Kth Largest Stream
- Reverse Linked List / Linked List Cycle
- TimeMap
- Evaluate RPN
- Word Ladder
- LRU Cache
- Fraction to Recurring Decimal

No problem outside Core-12 or this secondary list is added before Wednesday unless it is selected by the blind mock.

---

# 6. Friday, 4 September - stop training tonight

The current time at plan reset is approximately 22:00. The correct athletic decision is recovery.

| Time | Action |
|---|---|
| 22:00-22:10 | close all code, notes, LeetCode, Educative, and AlgoMonster |
| 22:10-22:20 | write only: today attempted __; strongest family __; weakest observed family __ |
| 22:20-22:30 | prepare water, clothes, charger, and Saturday desk |
| 22:30-22:45 | wash and wind down; no screen |
| 22:45 | lights out |

Do not finish one more problem. Saturday's diagnostic requires a rested brain more than Friday needs a higher count.

---

# 7. Saturday, 5 September - measure first, then train

## 06:45-08:30 - readiness setup

| Time | Action |
|---|---|
| 06:45-07:00 | wake, water, no phone feed |
| 07:00-07:30 | wash and breakfast |
| 07:30-07:50 | prayer/walk/quiet reset |
| 07:50-08:05 | blank-editor Java warm-up: map counting plus one boundary test |
| 08:05-08:15 | close warm-up; state its invariant and complexity |
| 08:15-08:30 | open MocksPractice and prepare `START MOCK Compressed` |

## 08:30-10:15 - blind diagnostic

| Time | Action |
|---|---|
| 08:30 | say `START MOCK Compressed` |
| 08:30-09:35 | generation plus cold attempt; obey the frozen session |
| 09:35 | say `DONE` |
| 09:35-10:05 | submission audit/viva; answer without sources |
| 10:05-10:15 | read verdict and record the top three corrections only |

If generation takes longer, preserve the full attempt and viva. Shorten the next break, not the mock.

## 10:15-12:30 - repair the evidence

| Time | Action |
|---|---|
| 10:15-10:35 | walk, water, snack; no post-mortem spiral |
| 10:35-10:53 | repair top correction #1 |
| 10:53-11:11 | closed-book retry #1 |
| 11:11-11:17 | break |
| 11:17-11:35 | repair top correction #2 |
| 11:35-11:53 | closed-book retry #2 |
| 11:53-11:59 | break |
| 11:59-12:17 | repair top correction #3 |
| 12:17-12:30 | summarize: Trigger -> Invariant -> Template -> Failed case |

If fewer than three corrections exist, use the remaining slot for a changed-constraint version of the weakest problem. Do not add coverage.

## 12:30-14:00 - recovery

Lunch, 20-minute walk, and zero interview content.

## 14:00-16:15 - Goldman-specific rehearsal

These are 25-minute cold reps: 3 clarify, 4 baseline/invariant, 12 code, 4 tests, 2 complexity.

| Time | Action |
|---|---|
| 14:00-14:25 | Highest Average Score / IP frequency |
| 14:25-14:32 | break |
| 14:32-14:57 | Trapping Rain Water |
| 14:57-15:12 | break, snack, eyes away |
| 15:12-15:37 | String Compression |
| 15:37-15:44 | break |
| 15:44-16:09 | Number of Islands, use BFS once |
| 16:09-16:15 | scores and failure codes |

Open the solution only for Y/R, and only during the later repair block.

## 16:15-19:15 - physical recovery

| Time | Action |
|---|---|
| 16:15-17:30 | exercise/walk/gym |
| 17:30-18:15 | shower and rest |
| 18:15-19:15 | dinner and leisure |

## 19:15-21:45 - Java, senior signal, and repair

| Time | Action |
|---|---|
| 19:15-19:45 | blank Java reliability: `HashMap`, `ArrayDeque`, priority-queue comparator, overflow-safe midpoint/ceiling |
| 19:45-20:15 | reconstruct only Y/R from the 14:00 block; maximum one problem |
| 20:15-20:25 | break |
| 20:25-20:50 | record A01 introduction, A03 why Goldman, A04 why this role, A24 CoderPad method |
| 20:50-21:10 | listen once; one repair line per answer, no third takes |
| 21:10-21:30 | draw pre-trade risk LLD from blank; explain rule seam, integer arithmetic, stateful extensions |
| 21:30-21:45 | score the day and choose Sunday's first weak family |
| 21:45 | preparation ends |
| 22:30 | lights out |

Saturday win condition: one scored blind diagnostic, its top three gaps repaired, and four Goldman-specific reps scored. Curriculum count is not part of the win.

---

# 8. Sunday, 6 September - simulate, transfer, and defend

## 07:00-08:30 - start calm

| Time | Action |
|---|---|
| 07:00-07:30 | wake and wash |
| 07:30-08:00 | breakfast |
| 08:00-08:20 | walk/prayer |
| 08:20-08:30 | blank editor and timer; no notes |

## 08:30-09:30 - exact CoderPad simulation 1

Use two previously exposed medium problems chosen without seeing solutions. The mock controller/interviewer should reveal only one problem at a time and may ask one follow-up. Use the 60-minute protocol in Section 3.

Preferred families: one array/string/map problem and one graph/tree/DP problem. Do not deliberately select yesterday's exact four anchors.

## 09:30-11:15 - debrief and repair

| Time | Action |
|---|---|
| 09:30-09:45 | score against the pass checklist before touching code |
| 09:45-10:03 | repair the highest P/I gap |
| 10:03-10:21 | reconstruct from blank |
| 10:21-10:35 | break |
| 10:35-10:53 | repair highest J/E/B/V gap |
| 10:53-11:11 | reconstruct or rehearse the recovery line |
| 11:11-11:15 | rescore only the repaired dimensions |

## 11:15-12:45 - Core-12 retrieval circuit

Use seven minutes per probe: 30 seconds trigger, 60 seconds baseline, 90 seconds invariant, two minutes skeleton, one minute tests, one minute complexity/trade-off.

| Time | Action |
|---|---|
| 11:15-11:43 | four weakest Core-12 probes |
| 11:43-11:50 | break |
| 11:50-12:18 | next four weakest probes |
| 12:18-12:25 | break |
| 12:25-12:39 | two next weakest probes |
| 12:39-12:45 | count green/yellow/red; no solution reading |

Ten probes are enough. The strongest two Core-12 anchors do not need rehearsal today.

## 12:45-14:30 - lunch and rest

No passive course content.

## 14:30-15:30 - exact CoderPad simulation 2

Use one pattern that was weak in the morning and one unrelated pattern. The surfaces must differ from the anchor names. Pass/fail uses the same checklist.

## 15:30-16:30 - debrief and stop high-pressure coding

| Time | Action |
|---|---|
| 15:30-15:45 | score |
| 15:45-16:03 | repair one root cause |
| 16:03-16:21 | closed-book retry |
| 16:21-16:30 | final complexity and changed-constraint answer |

No more full mocks Sunday.

## 16:30-18:15 - senior-depth defense

| Time | Action |
|---|---|
| 16:30-16:50 | walk/snack |
| 16:50-17:15 | Java concurrency: check-then-act, visibility vs atomicity, lock scope, bounded executors/backpressure |
| 17:15-17:40 | risk platform: state ownership, fail-closed trade-off, atomic reservation, recovery and audit |
| 17:40-18:05 | answer A07 PTR, A11 negative exposure, A15 ownership, A18 split brain, A19 honest AWS/EKS boundary |
| 18:05-18:15 | write only unsupported or unclear claims; do not reread all PDFs |

## 18:15-21:30 - recover, rehearse, gate

| Time | Action |
|---|---|
| 18:15-19:15 | dinner and leisure |
| 19:15-19:40 | record A21, A22, A23 and listen once |
| 19:40-20:00 | three recovery scripts: stuck, hint, failed test |
| 20:00-20:20 | Java skeleton recall: BFS, DFS, first-true binary search, sliding window |
| 20:20-20:35 | compute Sunday readiness score |
| 20:35-20:53 | one repair only if a P/I red remains |
| 20:53-21:10 | prepare Monday office and mock setup |
| 21:10-21:30 | shut down and wind down |
| 22:15 | lights out |

### Sunday readiness score - six points

One point for each true statement:

- [ ] the blind diagnostic was completed, graded, and its top gaps repaired;
- [ ] at least one Sunday CoderPad simulation passed;
- [ ] at least 8 of the Core-12 are G1/G2 and none of the tested anchors remains R;
- [ ] no blocking Java issue survived a repair;
- [ ] clarification, invariant, tests, and complexity were spoken without repeated 45-second silence;
- [ ] sleep and energy are adequate for Monday.

- **5-6:** on track.
- **3-4:** amber; Monday mock decides Tuesday leave.
- **0-2:** red; prepare Tuesday leave, but decide only after Monday evidence.

---

# 9. Monday, 7 September - office plus one decisive mock

Assumption: office/commute occupies the normal working day. Preserve block durations around the actual commute; never borrow from sleep.

## Before office

| Time | Action |
|---|---|
| 06:30-06:45 | wake and wash |
| 06:45-07:10 | one 25-minute cold rep from Sunday's weakest family |
| 07:10-07:25 | repair only if Y/R |
| 07:25-07:50 | breakfast |
| 07:50-08:05 | speak A01 and A24 once |
| 08:05 onward | get ready/commute |

## At office

- Do your job normally. Do not run LeetCode between meetings.
- At lunch, maximum 10 minutes: orally retrieve four trigger/invariant pairs.
- No solution videos or course browsing during commute if it compromises recovery.

## After office

| Time | Action |
|---|---|
| 19:30-20:10 | dinner and decompression |
| 20:10-20:20 | setup and breathing |
| 20:20-21:20 | exact CoderPad simulation 3 |
| 21:20-21:35 | score; do not edit code yet |
| 21:35-21:45 | apply Tuesday-leave gate |
| 21:45-22:03 | one root-cause repair if needed |
| 22:03 | preparation ends |
| 22:30 | lights out |

### Tuesday-leave gate

Take Tuesday leave only if two or more are true after Monday's mock:

1. No full 60-minute simulation has passed.
2. Fewer than 8 Core-12 anchors are G1/G2.
3. Three or more P/I/J failures remain unresolved.
4. You still cannot produce one coherent medium solution plus tests in 40 minutes.
5. Communication repeatedly includes unexplained silence over 45 seconds, coding before an invariant, or no adversarial test.

One weak area means targeted Tuesday morning/evening work, not leave. Leave must buy a specific repair plan, not generic study time.

---

# 10. Tuesday, 8 September - selected by Monday's gate

Do not combine the two versions.

## Version A - normal office day, default

| Time | Action |
|---|---|
| 06:30-06:45 | wake and wash |
| 06:45-07:30 | 45-minute one-problem mock from Monday's weakest family |
| 07:30-07:45 | score and one repair line |
| 07:45-08:15 | breakfast/get ready |
| 08:15-08:25 | A01, A03, A04, A24 once |
| Office lunch | 10-minute trigger/invariant shuffle only |
| 19:30-20:10 | dinner and decompress |
| 20:10-21:10 | final CoderPad simulation 4 |
| 21:10-21:28 | repair one root cause only |
| 21:28-21:42 | Java reliability checklist |
| 21:42-21:55 | laptop, charger, browser, camera, microphone, link, quiet room, hotspot |
| 21:55 | all preparation ends |
| 22:20 | lights out |

## Version B - Tuesday leave, only if triggered

| Time | Action |
|---|---|
| 07:00-07:45 | wake, breakfast, walk |
| 07:45-08:45 | CoderPad simulation on the two highest-risk families |
| 08:45-09:15 | score and break |
| 09:15-09:33 | repair highest P/I failure |
| 09:33-09:51 | closed-book retry |
| 09:51-10:10 | break |
| 10:10-10:28 | repair highest J/E/B/V failure |
| 10:28-10:46 | closed-book retry |
| 10:46-11:15 | walk/snack |
| 11:15-12:15 | eight weakest Core-12 oral probes, seven minutes each |
| 12:15-13:45 | lunch and rest |
| 13:45-14:30 | one-problem mock on the last red family |
| 14:30-15:00 | score and repair |
| 15:00-16:00 | nap/walk/complete detachment |
| 16:00-17:00 | final CoderPad simulation |
| 17:00-17:30 | score; high-pressure coding ends |
| 17:30-19:00 | exercise, shower, dinner |
| 19:00-19:30 | mandatory Tier-A answers and recovery scripts |
| 19:30-19:50 | Java reliability sheet |
| 19:50-20:10 | interview setup test |
| 20:10-20:30 | one optional red repair; otherwise stop |
| 20:30 | all preparation ends |
| 22:15 | lights out |

Version B intentionally contains only two full mocks and one shorter problem. Leave is not permission to overtrain.

---

# 11. Wednesday, 9 September - protect performance

## Leave recommendation

Try to protect at least a half-day Wednesday now. This has higher expected value than pre-booking Tuesday leave because it removes commute, meeting, privacy, and setup risk immediately before a 15:00 interview.

- If office or commute prevents a quiet, private, tested environment from 12:30-16:30, take Wednesday leave or half-day if feasible.
- If you can work remotely and hard-block 12:30-16:30 without interruption, full leave is optional.
- If only one leave day is possible, prefer Wednesday unless Monday's objective gate triggers Tuesday and Wednesday is already fully protected.

## Interview-day calendar

| Time | Action |
|---|---|
| 07:00-07:30 | wake and wash |
| 07:30-08:00 | breakfast |
| 08:00-08:20 | walk/prayer |
| 08:20-08:38 | easy familiar map/array warm-up |
| 08:38-08:56 | easy familiar tree/graph warm-up |
| 08:56-09:10 | test, complexity, close code |
| 09:10-09:25 | A01, A03, A04, A24 once |
| 09:25-12:00 | normal light morning; no study |
| 12:00-12:30 | light familiar lunch |
| 12:30-13:00 | shower/change; quiet-room boundary begins |
| 13:00-13:15 | charger, internet, hotspot, camera, microphone, browser |
| 13:15-13:30 | invitation/link check; do not expose passcodes on screen |
| 13:30-13:50 | eight Core-12 trigger/invariant sentences; no code |
| 13:50-14:05 | opening, stuck, hint, and failed-test scripts |
| 14:05-14:25 | eyes closed or short walk |
| 14:25-14:35 | washroom, water, Do Not Disturb, close unrelated apps |
| 14:35-14:45 | blank paper: clarify -> baseline -> invariant -> code -> test -> complexity |
| 14:45-14:50 | open/join interview environment |
| 14:50-14:57 | inhale 4, hold 2, exhale 6; repeat |
| 14:57-15:00 | “I do not need instant recognition. I need the next correct step.” |
| 15:00-16:00 | interview |
| 16:00-16:15 | factual debrief only; no catastrophic interpretation |

---

## 12. Exact CoderPad operating script

### Opening

> “Before I code, I’ll restate the contract, clarify the important bounds, outline a correct baseline, and then optimize deliberately.”

### Before optimization

> “The direct solution is __ with O(__). The repeated work is __. I can remove it by maintaining __. The invariant is __.”

### While coding

> “This structure stores __. At this update, __ remains true.”

Do not narrate punctuation or every keystroke.

### If stuck

> “I have a correct O(__) baseline. Its bottleneck is __. I’m checking whether __ can preserve the required state more efficiently.”

### If given a hint

> “That suggests __. Let me connect it to the invariant: __.”

### If code fails

> “For this input I expected __ and observed __. I’ll trace the boundary/state at the first divergence before changing the algorithm.”

### If time is running out

> “I’ll make the remaining logic precise: state is __, update is __, termination is __, tests are __, and complexity is __.”

### Close

> “Each element/state is processed __, so time is __. The auxiliary structures hold at most __, so space is __.”

---

## 13. Eight-year-experience red-flag firewall

The interviewer does not need instant brilliance. They do need observable engineering discipline.

| Avoid | Show instead |
|---|---|
| silent pattern dumping | clarify, baseline, bottleneck, invariant |
| memorized code with no proof | explain why each discarded state cannot matter |
| basic Java chaos | boring collections, safe arithmetic, small helpers |
| happy-path-only testing | normal, boundary, adversarial |
| confident wrong complexity | count operations and maximum live state |
| random edits after failure | expected vs actual, first divergence, one causal change |
| defensiveness after a hint | acknowledge, connect hint to invariant, continue |
| overengineering the coding problem | solve stated scope; mention production extensions afterward |
| buzzword-heavy senior answers | one decision, one trade-off, one failure mode, one proof |
| inflated ownership | separate “I did,” “we did,” and “I would” |
| invented scale/latency numbers | use verified permitted facts or qualitative impact |
| claiming every repo as production experience | label projects as learning/reference implementations |

Your resume already supplies senior credibility. The CoderPad task is to avoid subtracting from it through disorganized execution.

---

## 14. Daily scorecards

### Saturday

`Blind mock verdict __ | top corrections __ | Goldman reps G/Y/R __ | Java blocker __ | sleep __`

### Sunday

`Mock 1 pass __ | Mock 2 pass __ | Core-12 green __/12 | unresolved P/I/J __ | readiness __/6`

### Monday

`Mock 3 pass __ | total passed simulations __ | Core-12 green __/12 | leave triggers __/5 | Tuesday A/B __`

### Tuesday

`Final mock pass __ | unresolved red __ | setup verified __ | stopped at __ | lights out __`

---

## 15. Final coaching contract

- Exposure is not readiness; cold performance is readiness.
- Count passed simulations and repaired failure modes, not unique problems.
- The 90/170 plan can resume after Goldman.
- Paid resources are useful only when they shorten a measured repair.
- LLD/HLD repositories are depth insurance, not this round's primary event.
- A hint is not failure. Poor integration of the hint is the failure.
- One bug is not failure. Chaotic, silent debugging is the risk.
- Sleep and physical recovery are scheduled work.
- At every ambiguity, follow the next clock block and the most recent scorecard; do not redesign the week.

**Success this week means:** by Tuesday night, produce at least two passing 60-minute simulations, keep at least eight Core-12 anchors green, eliminate blocking Java failures, speak the Tier-A story truthfully, and enter Wednesday rested with a protected interview environment.
