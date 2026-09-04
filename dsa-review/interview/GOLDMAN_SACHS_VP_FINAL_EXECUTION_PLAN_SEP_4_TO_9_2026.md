# Goldman Sachs VP CoderPad — Final Execution Plan

**Candidate:** Chiranjeev Jain  
**Interview:** Wednesday, 9 September 2026, 15:00–16:00 IST  
**Plan created:** Friday, 4 September 2026, 16:40 IST  
**Language:** Java  
**Coverage ceiling:** finish the existing 90-problem hands-on plan; do not start the remaining 80 before this interview  
**Performance objective:** calmly derive, code, test, and explain two medium problems in a blank shared editor

This is the only live calendar for Friday through Wednesday. The curriculum labels “Day 5” and “Day 6” refer to content, not their old August dates.

Use the companion files as follows:

- [Master plan and solution vault](./GOLDMAN_SACHS_VP_CODERPAD_SEP_9_2026_MASTER_PLAN.md): evidence, problem answer cards, Java reliability sheet, and VP reference.
- [Final rehearsal script](./GOLDMAN_SACHS_VP_FINAL_REHEARSAL_SCRIPT.md): spoken answers, CoderPad language, honest ownership boundaries, and recording checklist.
- [Original seven-day sprint](./DSA_7-Day_Hourly_WIN_FINAL_v15_HighSignal_Pattern_Triggers.md): source curriculum only. Its old dates no longer control this week.

---

## 1. Decisions already made

1. The 90 hands-on problems are the coverage finish line.
2. The remaining 80 are deliberately deferred until after the Goldman interview.
3. A hands-on attempt is not automatically mastery. Every attempt receives G1, G2, Y, or R.
4. Friday is curriculum Day 5. Saturday is curriculum Day 6. Sunday is permitted overflow and qualification time.
5. Monday and Tuesday are office days unless the objective leave gate says otherwise.
6. If only one leave or half-day is available, protect Wednesday first unless Wednesday is already quiet, remote, and interruption-free.
7. Tuesday leave is for repairing measured weaknesses, never for opening the remaining 80.
8. Wednesday is activation and logistics, not a study marathon.
9. Sleep cannot be traded for another problem. Hard stop is 22:45 Friday, 22:15 Saturday, 21:45 Sunday, and 22:00 Monday/Tuesday.
10. No problem is repeated merely to feel busy. Repeat only a recorded Y/R or a scheduled Core-12 probe.

### Late-start and overrun rule

- If the clock has passed a listed start time, begin the earliest incomplete item immediately and run the same 18-minute protocol.
- Keep the next listed meal, long break, and nightly hard stop fixed. Do not compress a rep to catch the clock.
- Every rep that no longer fits becomes the earliest overflow item on the next day.
- Never do two problems inside one 18-minute rep and never convert a solution reading into a hands-on attempt.

---

## 2. The operating protocol for every 18-minute rep

| Minute | Action | Required spoken output |
|---:|---|---|
| 00:00–02:00 | Read, restate, clarify | “Input is __; output is __; I’m assuming __.” |
| 02:00–04:00 | Give baseline | “The direct solution is __ in O(__).” |
| 04:00–06:00 | Find repeated work and invariant | “The repeated work is __. I will maintain __.” |
| 06:00–14:00 | Write Java | Narrate state changes; do not describe every keystroke. |
| 14:00–16:30 | Test | Normal, boundary, and adversarial case. |
| 16:30–18:00 | Close | Time, space, one trade-off, score. |

At minute 18, stop even if unfinished. Record exactly:

`Score: __ | Stuck: __ | Gap: __ | Fix: __ | Next: __`

Scores:

- **G1:** independently derived, coded, tested, and explained.
- **G2:** achieved the same standard after one repair.
- **Y:** correct direction but a material weakness remains.
- **R:** needed the solution, had a wrong invariant, or could not produce coherent code.

Failure codes:

- **P:** pattern recognition
- **I:** invariant/correctness
- **D:** data structure choice
- **J:** Java/API/syntax
- **E:** edge case
- **C:** complexity
- **B:** debugging
- **M:** memorized but not understood

### Solution rule

Do not open a solution during the first 18 minutes. For Y/R, use the 18-minute repair protocol:

| Minute | Repair action |
|---:|---|
| 00:00–03:00 | Write the exact failure code and one-sentence gap. |
| 03:00–07:00 | Read only the needed hint/answer card. |
| 07:00–08:00 | Close the reference completely. |
| 08:00–15:00 | Reconstruct the missing logic from blank. |
| 15:00–17:00 | Run the failed edge case and one new case. |
| 17:00–18:00 | Explain invariant and complexity; rescore G2/Y/R. |

---

## 3. Fixed 30-problem completion queue

Always take the earliest incomplete item. If an item was already completed before this plan was created, mark its existing score and skip it; do not repeat it in a coverage slot.

### Curriculum Day 5 — queue items 01–15

| # | Problem | Core purpose |
|---:|---|---|
| 01 | Intersection of Two Linked Lists | pointer alignment and identity |
| 02 | Min Stack | augmented state and duplicate minima |
| 03 | Implement Queue Using Stacks | amortized transfer |
| 04 | Implement Stack Using Queues | queue rotation and operation trade-off |
| 05 | Next Greater Element I | monotonic stack |
| 06 | Next Greater Element II | circular monotonic stack |
| 07 | Online Stock Span | compressed monotonic state |
| 08 | Largest Rectangle in Histogram | boundary width and final flush |
| 09 | Sum of Subarray Minimums | contribution counting and asymmetric ties |
| 10 | Kth Largest Element in a Stream | size-k min-heap |
| 11 | K Closest Points to Origin | bounded selection |
| 12 | Evaluate Reverse Polish Notation | operand order and stack evaluation |
| 13 | Letter Combinations of a Phone Number | backtracking state restoration |
| 14 | Time Based Key-Value Store | greatest timestamp not exceeding query |
| 15 | Implement Trie | prefix traversal and terminal marker |

### Curriculum Day 6 — queue items 16–30

| # | Problem | Core purpose |
|---:|---|---|
| 16 | Design Add and Search Words | trie plus wildcard DFS |
| 17 | Gas Station | greedy reset proof |
| 18 | Task Scheduler | frequency-bound scheduling |
| 19 | Number of Provinces | component counting |
| 20 | Network Delay Time | Dijkstra and stale heap entries |
| 21 | Word Ladder | shortest path in an implicit graph |
| 22 | Accounts Merge | transitive connectivity/DSU |
| 23 | 01 Matrix | multi-source BFS |
| 24 | Pacific Atlantic Water Flow | reverse reachability |
| 25 | Surrounded Regions | boundary-connected preservation |
| 26 | Binary Tree Right Side View | one answer per depth |
| 27 | Path Sum | root-to-leaf state |
| 28 | Search in a BST | ordering eliminates a subtree |
| 29 | Insert into a BST | preserve ordering at the first null |
| 30 | Invert Binary Tree | recursive state transformation |

---

# 4. Friday, 4 September — finish curriculum Day 5

Start at the earliest unfinished Day-5 queue item. The timetable below has capacity for all 15 items from scratch. If some are already complete, finish early and use freed slots only for the three highest-priority Y/R repairs; do not start Day 6 tonight.

## 16:40–17:00 — reset and setup

- **16:40–16:45:** water, washroom, phone on Do Not Disturb.
- **16:45–16:50:** open blank Java editor, original sprint, and scoreboard only.
- **16:50–16:55:** mark already completed Day-5 items and their honest scores.
- **16:55–17:00:** say aloud: “Coverage attempt, not perfection. At minute 18 I score and move.”

## 17:00–20:00 — queue items 01–09

| Time | Work |
|---|---|
| 17:00–17:18 | Next incomplete item, beginning at 01 |
| 17:18–17:36 | Next incomplete item |
| 17:36–17:54 | Next incomplete item |
| 17:54–18:00 | stand, water, record scores |
| 18:00–18:18 | Next incomplete item |
| 18:18–18:36 | Next incomplete item |
| 18:36–18:54 | Next incomplete item |
| 18:54–19:00 | eyes away from screen, no solution reading |
| 19:00–19:18 | Next incomplete item |
| 19:18–19:36 | Next incomplete item |
| 19:36–19:54 | Next incomplete item |
| 19:54–20:00 | record failure codes and close editor |

## 20:00–20:45 — dinner and full detachment

No video solutions, LeetCode discussion, Java notes, or interview audio.

## 20:45–22:45 — queue items 10–15

| Time | Work |
|---|---|
| 20:45–21:03 | Next incomplete item |
| 21:03–21:21 | Next incomplete item |
| 21:21–21:39 | Next incomplete item |
| 21:39–21:45 | break and scores |
| 21:45–22:03 | Next incomplete item |
| 22:03–22:21 | Next incomplete item |
| 22:21–22:39 | Next incomplete item |
| 22:39–22:45 | write final Day-5 scoreboard; choose Saturday’s top three repairs by R before Y, then P/I/J/E/B/C/M |

## 22:45 onward

- **22:45–23:00:** shut down, prepare clothes/water for morning.
- **23:00–23:15:** wash and wind down.
- **23:15:** lights out.

Friday is successful if every Day-5 item has an attempt and a score. G1 is not required for all 15.

---

# 5. Saturday, 5 September — finish curriculum Day 6

## 06:45–09:00 — recovery and repair

| Time | Work |
|---|---|
| 06:45–07:00 | wake, water, no phone feed |
| 07:00–07:30 | wash, breakfast |
| 07:30–07:45 | walk/prayer/quiet reset |
| 07:45–08:03 | Repair 1: highest-priority Friday R |
| 08:03–08:21 | Repair 2: next Friday R/Y |
| 08:21–08:39 | Repair 3: next Friday R/Y |
| 08:39–08:45 | break |
| 08:45–09:00 | write three trigger–invariant pairs from memory; setup Day 6 |

## 09:00–13:00 — queue items 16–24

| Time | Work |
|---|---|
| 09:00–09:18 | 16 Add and Search Words |
| 09:18–09:36 | 17 Gas Station |
| 09:36–09:54 | 18 Task Scheduler |
| 09:54–10:00 | break and scores |
| 10:00–10:18 | 19 Number of Provinces |
| 10:18–10:36 | 20 Network Delay Time |
| 10:36–10:54 | 21 Word Ladder |
| 10:54–11:10 | longer break, snack, walk |
| 11:10–11:28 | 22 Accounts Merge |
| 11:28–11:46 | 23 01 Matrix |
| 11:46–12:04 | 24 Pacific Atlantic Water Flow |
| 12:04–12:15 | scores and failure codes |
| 12:15–13:00 | recovery buffer: first use for a late start; otherwise repair the single worst P/I failure from items 16–24 |

## 13:00–14:00 — lunch and walk

Zero study. Do not turn lunch into passive tutorial watching.

## 14:00–17:00 — queue items 25–30 and repair

| Time | Work |
|---|---|
| 14:00–14:18 | 25 Surrounded Regions |
| 14:18–14:36 | 26 Binary Tree Right Side View |
| 14:36–14:54 | 27 Path Sum |
| 14:54–15:00 | break and scores |
| 15:00–15:18 | 28 Search in a BST |
| 15:18–15:36 | 29 Insert into a BST |
| 15:36–15:54 | 30 Invert Binary Tree |
| 15:54–16:10 | snack and walk |
| 16:10–16:28 | Repair: highest Day-6 R |
| 16:28–16:46 | Repair: next Day-6 R/Y |
| 16:46–17:00 | scoreboard and Sunday overflow count |

## 17:00–22:15 — recovery plus one qualification probe

| Time | Work |
|---|---|
| 17:00–18:00 | exercise/walk; no interview content |
| 18:00–19:00 | shower and dinner |
| 19:00–19:30 | leisure |
| 19:30–20:15 | one 45-minute closed-book mock: **Highest Average Score** |
| 20:15–20:33 | repair only if the mock scored Y/R |
| 20:33–20:50 | break |
| 20:50–21:10 | record A01 introduction, A03 why Goldman, A04 why role |
| 21:10–21:30 | listen once at 1×; note only one fix per answer |
| 21:30–21:50 | record A24 CoderPad method and the three recovery lines |
| 21:50–22:15 | write Saturday scorecard; shut down |
| 22:15–22:45 | wind down |
| 22:45 | lights out |

Saturday completion rule:

- **Coverage complete:** all queue items 01–30 have scores.
- **Overflow exists:** any unattempted item keeps its queue number and moves to Sunday 09:00. No guilt and no new scheduling decision.

---

# 6. Sunday, 6 September — overflow, remastery, and simulation

## 07:00–09:00 — calm start

| Time | Work |
|---|---|
| 07:00–07:30 | wake, wash, water |
| 07:30–08:00 | breakfast |
| 08:00–08:30 | prayer/walk/quiet reset |
| 08:30–08:45 | read scoreboard only; count unattempted queue items |
| 08:45–09:00 | blank editor and timer setup |

## 09:00–12:00 — deterministic overflow rule

Use each 18-minute slot for the earliest unattempted item from 01–30. Once no unattempted item remains, immediately use all remaining slots for Core-12 retrieval in the order listed in Section 7.

| Time | Slot |
|---|---|
| 09:00–09:18 | earliest unattempted, otherwise Core-12 #1 |
| 09:18–09:36 | next unattempted, otherwise next Core-12 |
| 09:36–09:54 | next unattempted, otherwise next Core-12 |
| 09:54–10:00 | break |
| 10:00–10:18 | next unattempted, otherwise next Core-12 |
| 10:18–10:36 | next unattempted, otherwise next Core-12 |
| 10:36–10:54 | next unattempted, otherwise next Core-12 |
| 10:54–11:06 | break |
| 11:06–11:24 | next unattempted, otherwise next Core-12 |
| 11:24–11:42 | next unattempted, otherwise next Core-12 |
| 11:42–12:00 | next unattempted, otherwise next Core-12 |

If more than nine queue items remain unattempted at 12:00, stop coverage anyway. That is evidence the 18-minute protocol was not being followed. Do not sacrifice qualification to manufacture a count of 90.

## 12:00–14:00 — reset

- **12:00–12:20:** scoreboard; identify top three P/I/J failures.
- **12:20–13:00:** lunch.
- **13:00–13:30:** walk/rest.
- **13:30–14:00:** CoderPad-style blank editor, camera/audio/network check.

## 14:00–15:00 — full Goldman mock 1

Use **String Compression** followed by **Number of Islands**. No references.

| Minute | Action |
|---:|---|
| 00–03 | introduction and clarify problem 1 |
| 03–07 | baseline, bottleneck, invariant |
| 07–24 | code problem 1 |
| 24–28 | test and complexity |
| 28–31 | clarify problem 2 |
| 31–38 | baseline and invariant |
| 38–53 | code problem 2 |
| 53–58 | tests and complexity |
| 58–60 | concise close |

## 15:00–16:00 — mock evidence and repair

| Time | Work |
|---|---|
| 15:00–15:10 | score mock without changing code |
| 15:10–15:28 | repair highest-impact failure |
| 15:28–15:46 | reconstruct failed section from blank |
| 15:46–16:00 | explain both invariants and complexities aloud |

## 16:00–17:00 — full reset

Snack, walk, no screen.

## 17:00–18:30 — Core-12 oral retrieval

For each problem, spend exactly seven minutes: 30 seconds trigger, 60 seconds baseline, 90 seconds invariant/proof, two minutes skeleton, one minute tests, one minute complexity/trade-off. Do not fully code unless it is Y/R.

| Time | Problems |
|---|---|
| 17:00–17:28 | Core-12 #1–#4 |
| 17:28–17:35 | break |
| 17:35–18:03 | Core-12 #5–#8 |
| 18:03–18:10 | break |
| 18:10–18:38 | Core-12 #9–#12 |

## 18:38–21:45 — rehearsal and leave pre-gate

| Time | Work |
|---|---|
| 18:38–19:15 | dinner |
| 19:15–19:35 | record A01, A03, A04, A07 |
| 19:35–19:55 | record A11, A15, A18 |
| 19:55–20:15 | record A19, A21, A22, A23, A24 |
| 20:15–20:30 | listen once; one fix per answer only |
| 20:30–20:45 | calculate Sunday readiness gate in Section 8 |
| 20:45–21:15 | repair one red only: P/I first, then J, then E/B/C |
| 21:15–21:30 | write Monday’s one target and prepare office items |
| 21:30–21:45 | shut down |
| 22:15 | lights out |

---

## 7. The must-remaster set

These 12 cover the highest-ROI pattern families and reported Goldman-style shapes. The goal is not recognizing the title; it is reconstructing the invariant and clean Java after a context switch.

| # | Problem | Required proof sentence | Required adversarial test |
|---:|---|---|---|
| 1 | Highest Average Score | aggregate sum and count per key; compare averages without losing precision | negative scores, repeated person, tie policy |
| 2 | String Compression | read one run, write its symbol and count, never overwrite unread input incorrectly | run length 1, 10+, all same |
| 3 | Trapping Rain Water | the lower boundary is the currently safe side to finalize | monotone, short array, deep basin |
| 4 | Minimum Window Substring | shrink only while all required multiplicities are satisfied | duplicate required chars, impossible case |
| 5 | Koko / first-true binary search | feasibility is monotonic; search for the smallest true answer | one pile, overflow-safe ceiling, tight boundary |
| 6 | Largest Rectangle in Histogram | a smaller bar finalizes the popped bar’s maximal width | equal heights, increasing input, final flush |
| 7 | Number of Islands | every unseen land cell starts exactly one component traversal | all water, all land, diagonal separation |
| 8 | 01 Matrix | all zeros enter the queue together; first reach is shortest distance | all zero, one zero, rectangular grid |
| 9 | Validate BST | every node obeys bounds inherited from all ancestors | invalid deep descendant, integer extremes |
| 10 | Word Break | dp[i] means the prefix ending before i is constructible | empty prefix, reused word, impossible suffix |
| 11 | Gas Station | negative running balance invalidates every start in that segment | impossible total, valid wraparound |
| 12 | Task Scheduler | maximum task frequency creates the lower bound; real task count handles zero-idle cases | n=0, dominant task, many distinct tasks |

### Secondary set — only after Core-12 is stable

1. Top K Frequent / size-k heap
2. TimeMap / greatest timestamp not exceeding target
3. Word Ladder / implicit-graph BFS
4. LRU Cache / map plus doubly linked list
5. Merge Intervals or Meeting Rooms / boundary convention
6. Evaluate RPN / operand order
7. Reverse Linked List / preserve suffix before rewiring
8. Minimum Path Sum / grid-DP state and initialization

“Stable” means trigger, invariant, skeleton, three tests, and complexity can be produced closed-book in seven minutes. It does not mean memorizing a full solution.

---

## 8. Objective readiness and leave gates

### Sunday 20:30 preliminary gate

Score one point for each true statement:

- [ ] **Coverage:** all 30 Day-5/Day-6 queue items have a recorded attempt.
- [ ] **Core retrieval:** at least 9 of the Core-12 are G1/G2; none remains R after its scheduled repair.
- [ ] **Mock coding:** in mock 1, problem 1 was runnable by minute 28 and problem 2 had a correct approach by minute 38.
- [ ] **Communication:** no silence exceeded 45 seconds; clarification, invariant, tests, and complexity were spoken.
- [ ] **Java reliability:** collections, comparator, queue/stack use, bounds, and return values did not cause an unresolved failure.
- [ ] **Energy:** the last two days included at least seven hours of sleep and no signs of cognitive collapse.

Interpretation:

- **5–6 points:** on track. Tuesday leave is not justified for study.
- **3–4 points:** amber. Keep Monday office plan; decide Tuesday only after Monday mock.
- **0–2 points:** red. Prepare to take Tuesday leave if feasible, but still run Monday mock before the final decision.

### Monday 21:35 final Tuesday-leave gate

Take Tuesday leave only if **two or more** of these remain true after Monday’s mock:

1. Fewer than 9 Core-12 problems are G1/G2.
2. Two timed mocks have failed because of derivation/coding, not a single typo.
3. You still cannot produce coherent Java plus three tests inside 45 minutes for one medium.
4. Your narration repeatedly has silence over 45 seconds or jumps into code without an invariant.

Do **not** take Tuesday leave when only one item is weak. Use the Tuesday morning/evening targeted slots instead. Do **not** take leave to complete the remaining 80.

### Wednesday leave/half-day gate

This is a logistics and performance decision:

- If office/commute/meetings prevent a quiet, private, tested setup from **12:30 through 16:30**, take Wednesday leave or at least a half-day if at all possible.
- If you can work remotely, block the calendar from 12:30, and guarantee no interruption, full-day leave is optional.
- If only one leave day can be taken, choose **Wednesday** unless the Wednesday environment is already protected and the Monday readiness gate explicitly requires Tuesday repair.

Recommendation with present evidence: plan to work Monday; do not pre-commit Tuesday leave; protect Wednesday afternoon now. The decision may change only at the stated gates.

---

# 9. Monday, 7 September — office day

Assumption: leave home around 08:30 and return around 19:30. If commute differs, preserve the morning and evening block lengths; remove passive review before reducing sleep.

## 06:30–08:30

| Time | Work |
|---|---|
| 06:30–06:45 | wake, water, wash |
| 06:45–07:03 | closed-book repair of Sunday’s single worst Core-12 item |
| 07:03–07:21 | closed-book reconstruction of one unrelated Core-12 item |
| 07:21–07:30 | tests, complexity, scores |
| 07:30–08:00 | breakfast and get ready |
| 08:00–08:15 | speak A01, A03, A04 once; no rerecording |
| 08:15–08:30 | leave/setup for office |

## Office hours

- No LeetCode during work.
- Lunch: ten minutes maximum for trigger-only flash recall of Core-12 #1–#6. No code and no solution reading.
- If work is unusually draining, cancel the 20:00 micro-recall; keep the 20:20 mock and sleep.

## 19:30–22:00

| Time | Work |
|---|---|
| 19:30–20:00 | dinner and decompress |
| 20:00–20:20 | Core-12 #7–#12 trigger/invariant oral recall |
| 20:20–21:20 | full mock 2: **Trapping Rain Water** then **Validate BST** |
| 21:20–21:35 | score without emotional commentary |
| 21:35–21:40 | apply final Tuesday-leave gate |
| 21:40–21:58 | repair one precise failure only |
| 21:58–22:00 | write Tuesday target; shut down |
| 22:30 | lights out |

---

# 10. Tuesday, 8 September — two fixed versions

At Monday 21:40, the gate selects exactly one version. Do not blend them.

## Version A — normal office day, default

| Time | Work |
|---|---|
| 06:30–06:45 | wake and wash |
| 06:45–07:30 | 45-minute one-problem mock: **Minimum Window Substring** |
| 07:30–07:45 | score and repair note; no solution unless Y/R |
| 07:45–08:15 | breakfast/get ready |
| 08:15–08:30 | speak A01 and A24 once |
| Office lunch | 10-minute Core-12 trigger shuffle; no code |
| 19:30–20:00 | dinner/decompress |
| 20:00–21:00 | final full dress rehearsal: **Koko** then **Gas Station** |
| 21:00–21:18 | repair only a concrete Y/R |
| 21:18–21:30 | read Java reliability checklist and interview opening |
| 21:30–21:45 | verify laptop, charger, browser, camera, microphone, calendar link, quiet room, backup connection |
| 21:45 | all preparation ends |
| 22:15 | lights out |

## Version B — Tuesday leave, only if the gate triggered

| Time | Work |
|---|---|
| 07:00–07:45 | wake, breakfast, walk |
| 07:45–08:03 | repair top P/I failure |
| 08:03–08:21 | reconstruct it from blank |
| 08:21–08:39 | repair top J/E/B failure |
| 08:39–09:00 | break |
| 09:00–10:00 | full mock: **Minimum Window** then **Gas Station** |
| 10:00–10:30 | score and one repair |
| 10:30–11:00 | walk/snack |
| 11:00–12:00 | Core-12 oral retrieval; seven minutes each for the eight weakest |
| 12:00–13:30 | lunch and rest |
| 13:30–14:15 | one-problem mock on the weakest remaining family |
| 14:15–14:45 | repair and reconstruct |
| 14:45–15:30 | nap/rest/walk; zero content |
| 15:30–16:00 | record mandatory Tier-A answers once |
| 16:00–17:00 | full mock: **Koko** then **Validate BST** |
| 17:00–17:30 | score; no further full coding |
| 17:30–19:00 | exercise, shower, dinner |
| 19:00–19:30 | Java reliability sheet and three recovery scripts |
| 19:30–20:00 | interview setup test |
| 20:00–20:30 | optional repair of one red only; otherwise stop |
| 20:30 | all preparation ends |
| 22:15 | lights out |

Taking leave does not authorize eight hours of coding. Version B deliberately contains recovery blocks.

---

# 11. Wednesday, 9 September — interview day

No new problems, articles, discussions, videos, system-design topics, or remaining-80 coverage.

| Time | Work |
|---|---|
| 07:00–07:30 | wake, wash, water |
| 07:30–08:00 | breakfast |
| 08:00–08:20 | walk/prayer/quiet reset |
| 08:20–08:38 | warm-up 1: easy familiar array/map problem |
| 08:38–08:56 | warm-up 2: easy familiar tree/graph problem |
| 08:56–09:10 | tests and complexity; close code |
| 09:10–09:30 | speak A01, A03, A04, A24 once |
| 09:30–12:00 | normal light day; no study |
| 12:00–12:30 | lunch; familiar, not heavy |
| 12:30–13:00 | shower/change; quiet room begins |
| 13:00–13:15 | laptop power, charger, internet, backup hotspot, camera, microphone, browser, CoderPad familiarity |
| 13:15–13:30 | calendar invite and meeting access; do not expose passcodes in notes/screenshare |
| 13:30–13:50 | final Core-12 trigger scan only—no code |
| 13:50–14:05 | speak opening and stuck/hint/bug scripts |
| 14:05–14:25 | eyes closed or short walk; water |
| 14:25–14:35 | washroom; phone on Do Not Disturb; close unrelated apps |
| 14:35–14:45 | blank paper: clarify → baseline → invariant → code → test → complexity |
| 14:45–14:50 | join link/open meeting environment |
| 14:50–14:57 | breathing: inhale 4, hold 2, exhale 6; repeat |
| 14:57–15:00 | sit still; say: “I do not need recognition. I need a correct next step.” |
| 15:00–16:00 | interview |
| 16:00–16:15 | write factual debrief only; no catastrophizing |

### Exact interview behavior

When the problem appears:

> “I’ll restate it to confirm the contract, ask about the important bounds, then outline a correct baseline before optimizing.”

Before coding:

> “The repeated work is __. I can remove it with __. The invariant I need to preserve is __.”

If stuck:

> “I have a correct O(__) baseline. The bottleneck is __. I’m checking whether __ can preserve the needed state more efficiently.”

After a hint:

> “That suggests __. Let me connect it to the invariant before changing the code.”

After a failing test:

> “The observed result is __ and I expected __. I’ll trace the boundary/state update before changing the algorithm.”

At the end:

> “Each element/state is processed __, so time is __. The auxiliary structures hold at most __, so space is __.”

---

## 12. Eight-years-of-experience red-flag firewall

The interview does not require instant perfection. It does require senior engineering behavior under uncertainty.

| Red flag | What the interviewer may infer | Required replacement behavior |
|---|---|---|
| Silent coding | memorized pattern or weak collaboration | explain the contract and invariant before typing |
| Jumping straight to optimal code | pattern recall without proof | give the correct baseline and name repeated work |
| Basic Java thrashing | insufficient hands-on fluency | use boring collections and compile-safe idioms |
| Only happy-path testing | weak production judgment | test normal, boundary, and adversarial inputs |
| Wrong complexity stated confidently | shallow understanding | count operations and maximum auxiliary state aloud |
| Rewriting randomly after a bug | poor debugging discipline | compare expected/actual, trace state, change one cause |
| Becoming defensive after a hint | poor coachability | acknowledge it and reconnect it to the invariant |
| Overengineering | cannot scope for the setting | solve the stated problem; mention production extensions afterward |
| Buzzword dumping | breadth without depth | answer the asked question, then one trade-off and one failure mode |
| Inflated ownership | trust risk | separate “I did,” “we did,” and “I would” exactly |
| Invented metrics or internal details | judgment/confidentiality risk | use only verified, permitted facts; describe impact qualitatively |
| Treating trade-offs as universal truths | weak VP judgment | state the assumption that makes the choice appropriate |

### The five signals to deliberately show

1. **Contract discipline:** clarify inputs, outputs, constraints, and ambiguity.
2. **Correctness discipline:** state a baseline and an invariant.
3. **Implementation discipline:** write simple Java, not a framework.
4. **Operational discipline:** test boundaries and debug from evidence.
5. **Leadership discipline:** take hints well, communicate trade-offs, and maintain honest ownership boundaries.

---

## 13. End-of-day scorecards

### Friday

`Day-5 attempted __/15 | G1 __ | G2 __ | Y __ | R __ | top failure codes __ | sleep time __`

### Saturday

`Day-6 attempted __/15 | total 30 attempted __ | G1/G2 __ | Y __ | R __ | Highest Average mock __ | overflow __`

### Sunday

`Coverage __/30 | Core-12 green __/12 | mock P1 runnable minute __ | P2 approach minute __ | P2 runnable minute __ | silence breach Y/N | readiness points __/6`

### Monday

`Core-12 green __/12 | mock 2 pass/fail __ | P/I failures __ | J/E/B failures __ | Tuesday gate count __/4 | selected version A/B __`

### Tuesday

`Final mock pass/fail __ | unresolved red count __ | setup verified Y/N | preparation stopped at __ | lights out __`

---

## 14. Final anti-anxiety contract

- The remaining 80 are not unfinished business for this interview; they are post-interview curriculum.
- A red score is scheduling information, not a judgment about ability.
- A failed mock is useful only when converted into one named repair.
- Recognition is optional. A correct baseline plus deliberate optimization is enough to demonstrate engineering judgment.
- A hint does not end the interview. Senior behavior is visible in how the hint is integrated.
- One typo is not a readiness failure. Chaotic recovery is; use the debugging script.
- No late-night rescue session is allowed.
- On Wednesday, calm reasoning has higher ROI than one more recalled solution.

**Final instruction:** work the next scheduled slot, score honestly, repair only measured gaps, and stop on time.
