# Goldman Sachs VP CoderPad — Zero-Ambiguity Master Plan

> **Schedule authority:** Use [GOLDMAN_SACHS_VP_FINAL_EXECUTION_PLAN_SEP_4_TO_9_2026.md](./GOLDMAN_SACHS_VP_FINAL_EXECUTION_PLAN_SEP_4_TO_9_2026.md) for the live Friday–Wednesday timetable and leave decisions. It reflects the corrected constraint that curriculum Days 5 and 6 may run through Sunday. This file remains the evidence, problem-answer, Java, and VP-reference vault; any calendar or leave guidance below is reference-only and does not control execution.

**Candidate:** Chiranjeev Jain  
**Role:** Vice President, Software Engineering — The Core, Portfolio Risk Platforms, Risk Division, Bengaluru (Job 179689)  
**Interview:** Wednesday, 9 September 2026, 15:00–16:00 IST  
**Interviewer:** Manju  
**Language:** Java  
**Plan window:** Friday, 4 September 2026, 16:30 IST through interview start  
**Single objective:** enter the interview able to derive, code, test, and explain two medium problems calmly in a blank shared editor.

> This file intentionally does not contain the Zoom passcode or private interview links. Use the calendar invitation for access.

---

## 0. The decision is already made

There are no study choices left to make.

1. **Friday attempts curriculum Day 5 in its fixed order.**
2. **Saturday attempts curriculum Day 6; any genuine overflow has fixed Sunday slots.**
3. **Sunday closes coverage and converts knowledge into interview performance.**
4. **Monday is an office day; Tuesday remains an office day unless the objective leave gate triggers.**
5. **Wednesday protects the 15:00 interview environment and activates known skills; it does not add knowledge.**
6. **Java is the interview language.** Do not switch to Python.
7. **No new problem list is allowed.** Use only the named problems in this document.
8. **Do not chase all 150 problems.** Saturday coverage closes at the end of the named Day 6 set.
9. **Solutions stay closed until the timed attempt ends.** The answer vault is in Section 11.
10. **Sleep is part of the plan.** No work after the stated stop time.

If any slot starts late, begin the current slot at once. Do not rebuild the timetable and do not steal from sleep. Apply the cut rules in Section 4.

---

## 1. What the evidence says — and what it does not say

### Confirmed from the invitation and role description

- This is a 60-minute CoderPad interview using Java or Python.
- The role is a hands-on VP/lead-engineer position in Portfolio Risk Platforms.
- The work emphasizes high-performance risk calculation engines, correct risk metrics, scalable/resilient workflows, AWS/multi-region architecture, modernization, technical leadership, and cross-functional communication.

### Public interview evidence used for prioritization

These are self-reported experiences, not an official question list. They justify pattern priority; they do **not** predict the exact questions.

| Evidence | Reported round/questions | Planning implication |
|---|---|---|
| [Goldman, VP, Bengaluru, 2024](https://leetcode.com/discuss/post/5756181/Goldman-Sachs-or-VP-or-Bangalore-or-Offer-or-6-Years/) | CoderPad: Word Break and Trapping Rain Water; later Meeting Rooms variation and Validate BST | Put DP, two pointers, intervals, and BST validation in the final set |
| [Goldman, VP, Bengaluru, 10 YOE](https://leetcode.com/discuss/post/2049038/goldman-sachs-coderpad-virtual-onsite-bangalore/) | Frequency aggregation and Trapping Rain Water | Rehearse map aggregation and constant-space two pointers |
| [Goldman, Senior SWE/VP, low latency](https://leetcode.com/discuss/post/2164579/goldman-sachs-interview-experience-nyc-senior-software-engineer-vp/) | First unique character and Minimum Path Sum | Rehearse queue/map and grid DP |
| [Goldman CoderPad, Aug 2025](https://leetcode.com/discuss/post/7055821/goldman-sachs-coderpad-interview-august-h8kjx/) | Two-pointer array problem and High Five/score aggregation variation; brute/better/optimal discussion and tests | Expect two problems; speak trade-offs and test cases |
| [Goldman Bengaluru, Feb 2025](https://leetcode.com/discuss/post/6482400/Goldman-Sachs-or-Analyst-or-1.5-YOE-or-HydBlr-or-Feb-2025/) | String Compression and Trapping Rain Water; later Group Anagrams and Good Nodes | String edge cases and optimized working Java matter |
| [Goldman Bengaluru](https://leetcode.com/discuss/post/6942117/goldman-sachs-analyst-2yoe-interview-exp-j0or/) | Shortest Subarray to Remove + Number of Islands; later Gas Station + Minimum Window | Preserve array/window, graph traversal, greedy, and communication |
| [Goldman CoderPad, 2024](https://leetcode.com/discuss/interview-question/5826387/) | Unique substrings of length k and Fraction to Recurring Decimal | Rehearse fixed window and remainder-cycle mapping |
| [Recent GS CoderPad discussion, May–Jun 2026](https://www.reddit.com/r/leetcode/comments/1ts4lq2/goldman_sachs_coderpad_interview_for_software/) | Two reported questions; discussion says medium problems can repeat | Use reports as drills, never as guarantees |
| [Recent CoderPad report, Jul 2026](https://www.reddit.com/r/leetcode/comments/1v4nlj1/messed_up_gs_coderpad_feeling_disappointed/) | Random Pick with Weight | Include prefix sum + binary-search sampling as one stretch card |
| [Official Goldman engineering page](https://www.goldmansachs.com/careers/our-firm/engineering) | Algorithms, distributed systems, databases, scalable and low-latency systems | DSA is primary today; VP narrative still needs correctness, scale, and leadership |
| [Official Goldman engineering tenets](https://www.goldmansachs.com/careers/blog/engineering-tenets) | Resilience, incremental delivery, trust, data, learning | Use evidence, trade-offs, failure handling, and clear expectations in answers |

### Evidence-calibrated conclusion

The highest-probability shape is: 5–10 minutes of introduction/resume/behavioral context, followed by one or two coding problems, with runnable code, edge-case testing, and verbal reasoning. Exact level and mix vary. Therefore the plan trains a **two-problem finish**, not recall of a secret list.

---

## 2. Definition of success

### One 18-minute rep is won only when all six boxes are checked

- [ ] Restated the input, output, constraints, and one ambiguity.
- [ ] Stated a correct baseline.
- [ ] Named the repeated work/bottleneck.
- [ ] Stated the optimized invariant before coding.
- [ ] Wrote coherent Java and dry-ran normal + edge cases.
- [ ] Stated time and auxiliary-space complexity.

### One 60-minute mock is won only when

- [ ] There is no solution/reference access.
- [ ] Thinking is spoken; there is no silence longer than 30 seconds.
- [ ] Problem 1 has runnable code by minute 28.
- [ ] Problem 2 has a correct approach by minute 38 and runnable code by minute 53.
- [ ] At least three tests are executed or dry-run for each completed problem.
- [ ] Final complexity and one trade-off are stated.

### Scoring

| Score | Meaning | Mandatory next action |
|---|---|---|
| **G1** | Derived, coded, tested, and explained without help | No same-day repetition |
| **G2** | Became green after repair | Recall next morning |
| **Y** | Correct direction but weak invariant/code/edge case | Repair in next repair slot |
| **R** | Needed lookup or could not form correct solution | Repair before any optional item |

Failure codes: `P` pattern, `I` invariant, `D` data structure, `J` Java, `E` edge case, `C` complexity, `B` debugging, `M` memorized without understanding, `T` time/communication.

After every rep write exactly:

```text
Score: G1/G2/Y/R | Failure: _ | Stuck at: _ | One gap: _ | One fix: _ | Next recall: _
```

---

## 3. Mechanical protocols

### 3.1 The exact 18-minute protocol

| Minute | Action | Required spoken output |
|---:|---|---|
| 00:00–02:00 | WHAT | “Input is __. Output is __. I want to clarify __. For this example, the answer is __.” |
| 02:00–04:00 | BASELINE | “A correct baseline is __, costing __ because __.” |
| 04:00–06:00 | DERIVE | “The repeated work is __. I can remove it with __.” |
| 06:00–07:00 | CONTRACT | “My invariant/state meaning is __.” |
| 07:00–14:00 | CODE | Narrate method boundaries and non-obvious updates; do not narrate punctuation. |
| 14:00–16:00 | TEST | Run normal, boundary, and adversarial/tricky cases. |
| 16:00–17:00 | EXPLAIN | State time, space, and why the invariant proves correctness. |
| 17:00–18:00 | SCORE | Record score/failure. Stop even if incomplete. |

### 3.2 The exact 60-minute two-problem mock

| Minute | Action |
|---:|---|
| 00–02 | Greeting and 30-second introduction |
| 02–05 | Read Problem 1, clarify, work one example |
| 05–09 | Baseline → bottleneck → invariant → complexity target |
| 09–23 | Implement Problem 1 |
| 23–28 | Compile/run, edge cases, complexity |
| 28–31 | Read Problem 2, clarify, work one example |
| 31–36 | Baseline → bottleneck → invariant → complexity target |
| 36–51 | Implement Problem 2 |
| 51–55 | Compile/run, edge cases, complexity |
| 55–58 | Mutation/follow-up and correction buffer |
| 58–60 | Ask one role-specific question and close |

If Problem 1 is not coded at minute 23, say: “I have the correct approach; I’m going to finish a clean baseline now, then use the remaining time for tests.” Do not silently spiral.

### 3.3 The exact 45-minute one-problem mock

| Minute | Action |
|---:|---|
| 00–03 | Restate and clarify |
| 03–08 | Examples and edge cases |
| 08–13 | Baseline and bottleneck |
| 13–17 | Optimized invariant and proof sketch |
| 17–33 | Code |
| 33–39 | Run/debug tests |
| 39–42 | Complexity and trade-offs |
| 42–45 | One mutation/follow-up |

### 3.4 Repair protocol — exactly 18 minutes

1. Minutes 0–3: identify the **single first failure**; do not write “everything.”
2. Minutes 3–6: read only the relevant answer-card paragraph or local solution.
3. Minutes 6–7: close the reference.
4. Minutes 7–14: reconstruct from blank.
5. Minutes 14–17: test and explain.
6. Minute 17–18: score G2/Y/R and schedule recall.

---

## 4. Cut rules — no rescheduling decisions

If behind, remove work in this exact order:

1. Remove optional mutation cards.
2. Remove **Sum of Subarray Minimums**.
3. Remove **Pacific Atlantic Water Flow**.
4. Remove **Accounts Merge**.
5. Remove **Word Ladder**.
6. Shorten a three-rep block to the first two named problems.

Never cut:

- score aggregation/hash map;
- string compression;
- Trapping Rain Water;
- Number of Islands;
- grid path DP;
- binary-search boundary/rotated array;
- Validate BST;
- one full two-problem mock Sunday;
- the Tuesday dress rehearsal;
- sleep.

If energy is low, do a spoken derivation plus Java skeleton and three tests. Do not watch a solution video. If sick or severely sleep-deprived, stop and sleep; resume at the next fixed slot.

---

# 5. Friday, 4 September — close Day 5

**Mission:** finish the remaining Day 5 patterns.  
**Start state:** the plan begins at 16:30 IST.  
**Hard stop:** 23:10. Lights out by 23:30.

| Time | Exact work | LeetCode | Local Java | Hourly WIN |
|---|---|---|---|---|
| 16:30–17:00 | Send/verify interview confirmation; fill water; eat light snack; phone on Do Not Disturb; open blank `Main.java`; paper + pen; close all solution tabs | — | — | Confirmation handled and environment ready |
| 17:00–17:18 | Intersection of Two Linked Lists | [LeetCode](https://leetcode.com/problems/intersection-of-two-linked-lists/) | [Intersection.java](../../src/main/java/org/chijai/day4/LinkedList/session1/Intersection.java) | 18-minute protocol + score |
| 17:18–17:36 | Min Stack | [LeetCode](https://leetcode.com/problems/min-stack/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) | 18-minute protocol + score |
| 17:36–17:54 | Implement Queue Using Stacks | [LeetCode](https://leetcode.com/problems/implement-queue-using-stacks/) | [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) | 18-minute protocol + score |
| 17:54–18:00 | Record scores; stand; water | — | — | Three scores recorded |
| 18:00–18:18 | Implement Stack Using Queues | [LeetCode](https://leetcode.com/problems/implement-stack-using-queues/) | [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) | 18-minute protocol + score |
| 18:18–18:36 | Next Greater Element I | [LeetCode](https://leetcode.com/problems/next-greater-element-i/) | [NextGreaterElement.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/NextGreaterElement.java) | 18-minute protocol + score |
| 18:36–18:54 | Next Greater Element II | [LeetCode](https://leetcode.com/problems/next-greater-element-ii/) | [NextGreaterElement.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/NextGreaterElement.java) | 18-minute protocol + score |
| 18:54–19:00 | Record scores; choose first repair candidate | — | — | No solution reading yet |
| 19:00–19:45 | Dinner, 10-minute walk, no phone/problem content | — | — | Return fed and calm |
| 19:45–20:03 | Online Stock Span | [LeetCode](https://leetcode.com/problems/online-stock-span/) | [OnlineStockSpan.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/OnlineStockSpan.java) | 18-minute protocol + score |
| 20:03–20:21 | Largest Rectangle in Histogram | [LeetCode](https://leetcode.com/problems/largest-rectangle-in-histogram/) | [LargestRectangle.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java) | 18-minute protocol + score |
| 20:21–20:39 | Sum of Subarray Minimums | [LeetCode](https://leetcode.com/problems/sum-of-subarray-minimums/) | [SumOfSubarrayMinimums.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SumOfSubarrayMinimums.java) | 18-minute protocol + score; cut first if late |
| 20:39–20:45 | Record scores; water | — | — | Three scores recorded |
| 20:45–21:03 | Kth Largest in a Stream | [LeetCode](https://leetcode.com/problems/kth-largest-element-in-a-stream/) | [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) | 18-minute protocol + score |
| 21:03–21:21 | K Closest Points to Origin | [LeetCode](https://leetcode.com/problems/k-closest-points-to-origin/) | [KClosestPointsToOrigin.java](../../src/main/java/org/chijai/day7/session1/heap/KClosestPointsToOrigin.java) | 18-minute protocol + score |
| 21:21–21:39 | Evaluate Reverse Polish Notation | [LeetCode](https://leetcode.com/problems/evaluate-reverse-polish-notation/) | [EvalRPN.java](../../src/main/java/org/chijai/day5/stack/session3/EvalRPN.java) | 18-minute protocol + score |
| 21:39–21:45 | Record scores; stand | — | — | Three scores recorded |
| 21:45–22:03 | Letter Combinations of a Phone Number | [LeetCode](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) | [LetterCombinationsOfAPhoneNumber.java](../../src/main/java/org/chijai/day11/backtracking/session1/LetterCombinationsOfAPhoneNumber.java) | 18-minute protocol + score |
| 22:03–22:21 | Time Based Key-Value Store | [LeetCode](https://leetcode.com/problems/time-based-key-value-store/) | [TimeBasedKeyValueStore.java](../../src/main/java/org/chijai/day2/session3/TimeBasedKeyValueStore.java) | 18-minute protocol + score |
| 22:21–22:39 | Implement Trie | [LeetCode](https://leetcode.com/problems/implement-trie-prefix-tree/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) | 18-minute protocol + score |
| 22:39–22:45 | Write Day 5 totals and rank worst gaps | — | — | Red list has evidence, not feelings |
| 22:45–23:03 | Repair the single worst `R`, else worst `Y` | — | — | Closed-book reconstruction completed |
| 23:03–23:10 | Put Saturday materials on desk; close laptop | — | — | No more coding |
| 23:10–23:30 | Wash, breathing, lights down | — | — | Asleep by 23:30 |

### Friday local references

- [Linked-list/stack/queue answer rows](12_MASTER_DSA_INTERVIEW_ARTICULATION_TABLE.md)
- [Day 5 canonical sprint rows](DSA_7-Day_Hourly_WIN_FINAL_v15_HighSignal_Pattern_Triggers.md)
- [Ranked local solutions](01_ZERO_TO_HERO_RANKED_TABLE.md)

### Friday scoreboard

```text
Attempted __/15 | G1 __ | G2 __ | Y __ | R __
Worst first failure: ____________________
Saturday 08:00 recalls: 1) ______ 2) ______ 3) ______
```

---

# 6. Saturday, 5 September — close coverage, then qualify

**Mission:** Day 6 ends at 16:00. No coverage chasing after that.  
**Hard stop:** 22:15. Lights out by 22:45.

## 06:45–08:00 — wake and recall

| Time | Exact work | WIN |
|---|---|---|
| 06:45–07:00 | Wake, water, wash | Upright; no phone |
| 07:00–07:25 | Breakfast | Normal food; no study video |
| 07:25–07:45 | Temple/prayer/quiet reset | Breathing settled |
| 07:45–08:00 | Open blank editor; copy Friday’s three weakest names only | References closed |
| 08:00–08:18 | Recall Friday weakness 1 | Reconstructed and rescored |
| 08:18–08:36 | Recall Friday weakness 2 | Reconstructed and rescored |
| 08:36–08:54 | Recall Friday weakness 3 | Reconstructed and rescored |
| 08:54–09:00 | Water and scores | Ready for coverage |

## 09:00–16:00 — remaining Day 6, fixed order

| Time | Exact work | LeetCode | Local Java | Invariant to say before code |
|---|---|---|---|---|
| 09:00–09:18 | Design Add and Search Words | [LeetCode](https://leetcode.com/problems/design-add-and-search-words-data-structure/) | [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) | Exact char follows one edge; `.` branches; success requires terminal node at end |
| 09:18–09:36 | Gas Station | [LeetCode](https://leetcode.com/problems/gas-station/) | [GasStation.java](../../src/main/java/org/chijai/day9/dp/session1/GasStation.java) | If tank becomes negative at `i`, no start since the candidate can reach `i+1` |
| 09:36–09:54 | Task Scheduler | [LeetCode](https://leetcode.com/problems/task-scheduler/) | [TaskScheduler.java](../../src/main/java/org/chijai/day7/session1/heap/TaskScheduler.java) | Most frequent tasks create the minimum frame/idle pressure |
| 09:54–10:00 | Score/break | — | — | Three scores |
| 10:00–10:18 | Number of Provinces | [LeetCode](https://leetcode.com/problems/number-of-provinces/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | Each unseen city starts exactly one component |
| 10:18–10:36 | Network Delay Time | [LeetCode](https://leetcode.com/problems/network-delay-time/) | [NetworkDelayTime.java](../../src/main/java/org/chijai/day8/graph/session2/NetworkDelayTime.java) | Popped minimum non-stale distance is finalized; relax outgoing edges |
| 10:36–10:54 | Word Ladder | [LeetCode](https://leetcode.com/problems/word-ladder/) | [WordLadder.java](../../src/main/java/org/chijai/day8/graph/session3/WordLadder.java) | First BFS discovery gives the shortest transformation count |
| 10:54–11:00 | Score/break | — | — | Three scores |
| 11:00–11:18 | Accounts Merge | [LeetCode](https://leetcode.com/problems/accounts-merge/) | [AccountsMerge.java](../../src/main/java/org/chijai/day8/graph/session3/AccountsMerge.java) | Shared email proves component/DSU connectivity |
| 11:18–11:36 | 01 Matrix | [LeetCode](https://leetcode.com/problems/01-matrix/) | [Matrix01.java](../../src/main/java/org/chijai/day8/graph/session1/Matrix01.java) | Multi-source BFS first arrival is nearest-zero distance |
| 11:36–11:54 | Pacific Atlantic Water Flow | [LeetCode](https://leetcode.com/problems/pacific-atlantic-water-flow/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | Reverse traversal climbs from each ocean; answer is intersection |
| 11:54–12:00 | Score/break | — | — | Three scores |
| 12:00–13:00 | Lunch + 15-minute walk; zero DSA | — | — | Full reset |
| 13:00–13:18 | Surrounded Regions | [LeetCode](https://leetcode.com/problems/surrounded-regions/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | Only border-connected `O` cells survive |
| 13:18–13:36 | Binary Tree Right Side View | [LeetCode](https://leetcode.com/problems/binary-tree-right-side-view/) | [BinaryTreeSideView.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeSideView.java) | Last node of each BFS level is visible |
| 13:36–13:54 | Path Sum | [LeetCode](https://leetcode.com/problems/path-sum/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) | Remaining target travels down; success is checked only at a leaf |
| 13:54–14:00 | Score/break | — | — | Three scores |
| 14:00–14:18 | Search in BST | [LeetCode](https://leetcode.com/problems/search-in-a-binary-search-tree/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | Ordering eliminates one subtree at every node |
| 14:18–14:36 | Insert into BST | [LeetCode](https://leetcode.com/problems/insert-into-a-binary-search-tree/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | Follow ordering until the first null child; attach exactly once |
| 14:36–14:54 | Invert Binary Tree | [LeetCode](https://leetcode.com/problems/invert-binary-tree/) | [InvertBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session3/InvertBinaryTree.java) | Every node swaps its two children exactly once |
| 14:54–15:00 | Score/break | — | — | Three scores |
| 15:00–15:18 | Repair morning’s worst `R` | — | — | Closed-book reconstruction |
| 15:18–15:36 | Repair next `R`, else worst `Y` | — | — | Closed-book reconstruction |
| 15:36–15:54 | Repair next `R`, else fixed mutation: Network Delay with unweighted edges | [Network Delay Time](https://leetcode.com/problems/network-delay-time/) | [NetworkDelayTime.java](../../src/main/java/org/chijai/day8/graph/session2/NetworkDelayTime.java) | Explain what changes from Dijkstra to BFS |
| 15:54–16:00 | Final scores; close the syllabus | — | — | Write “COVERAGE CLOSED” |

## 16:00–22:15 — Goldman qualification block

| Time | Exact work | LeetCode / closest analogue | Local Java | Pass condition |
|---|---|---|---|---|
| 16:00–16:15 | Snack and walk | — | — | No screen |
| 16:15–17:00 | **Mock 1:** Highest Average Score + String Compression | [High Five - closest analogue](https://leetcode.com/problems/high-five/)<br>[String Compression](https://leetcode.com/problems/string-compression/) | — | Two runnable Java solutions using the 45-minute compressed two-problem timing below |
| 17:00–17:15 | Postmortem | — | — | First failure only; no self-judgment |
| 17:15–18:00 | **Mock 2:** Trapping Rain Water | [LeetCode](https://leetcode.com/problems/trapping-rain-water/) | [TrappingRainwater.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/TrappingRainwater.java) | 45-minute one-problem protocol, constant space |
| 18:00–19:00 | Dinner + movement | — | — | Zero study |
| 19:00–19:45 | **Mock 3:** Maximum Path from bottom-left to top-right | Custom prompt; no exact LeetCode match | — | Correct DP state, boundaries, negative-value policy clarified |
| 19:45–20:00 | Postmortem | — | — | Repair scheduled or G1 recorded |
| 20:00–20:18 | Validate BST | [LeetCode](https://leetcode.com/problems/validate-binary-search-tree/) | [ValidateBST.java](../../src/main/java/org/chijai/day6/trees/session3/ValidateBST.java) | Ancestor bounds, not parent-only comparisons |
| 20:18–20:36 | Word Break | [LeetCode](https://leetcode.com/problems/word-break/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) | `dp[i]` prefix meaning said before code |
| 20:36–20:54 | Find Minimum in Rotated Sorted Array | [LeetCode](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/) | [BinarySearchPatternLab.java](../../src/main/java/org/chijai/patterns/binarysearch/BinarySearchPatternLab.java) | Sorted-half reasoning and duplicate assumption clarified |
| 20:54–21:00 | Score/break | — | — | Three scores |
| 21:00–21:18 | Repair worst mock failure | — | — | Reconstruction |
| 21:18–21:36 | Repair second failure | — | — | Reconstruction |
| 21:36–21:54 | Write Goldman Red List, maximum 10 items | — | — | Each entry is a concrete failure, not a topic |
| 21:54–22:05 | Read Sunday schedule, place paper/charger/water | — | — | No decision tomorrow |
| 22:05–22:15 | Close laptop | — | — | Preparation stops |
| 22:15–22:45 | Wind down | — | — | Lights out |

### Saturday Mock 1 compressed timing

- 00–03: introduce/restate Problem 1.
- 03–06: baseline, invariant, complexity.
- 06–16: code Problem 1.
- 16–20: tests and explanation.
- 20–23: restate Problem 2.
- 23–27: derive/invariant.
- 27–39: code Problem 2.
- 39–43: tests and complexity.
- 43–45: close and score.

### Saturday scoreboard

```text
Day 6 attempted __/15 | G1 __ | G2 __ | Y __ | R __
Mock 1: __ | Mock 2: __ | Mock 3: __
COVERAGE CLOSED: YES / NO (write YES at 16:00 even if tail items were cut)
Goldman Red List:
1. ____________________  2. ____________________  3. ____________________
4. ____________________  5. ____________________  6. ____________________
7. ____________________  8. ____________________  9. ____________________
10. ___________________
```

---

# 7. Sunday, 6 September — interview simulation day

**Mission:** three realistic mocks, repairs, Java reliability, and role articulation.  
**No new syllabus. Heavy DSA stops at 18:00.**

| Time | Exact work | LeetCode / closest analogue | Local Java | WIN |
|---|---|---|---|---|
| 07:15–08:00 | Wake, breakfast, quiet reset | — | — | Seated at 08:00 |
| 08:00–08:18 | Red-list recall 1 | — | — | G1/G2/Y/R |
| 08:18–08:36 | Red-list recall 2 | — | — | G1/G2/Y/R |
| 08:36–08:54 | Red-list recall 3 | — | — | G1/G2/Y/R |
| 08:54–09:00 | Scores/break | — | — | References closed |
| 09:00–10:00 | **Full Mock A:** Number of Islands + Score Aggregation | [Number of Islands](https://leetcode.com/problems/number-of-islands/)<br>[High Five - closest analogue](https://leetcode.com/problems/high-five/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | Use exact 60-minute protocol |
| 10:00–10:15 | Postmortem | — | — | First correctness failure + first communication failure |
| 10:15–10:33 | Repair Mock A | — | — | Closed-book reconstruction |
| 10:33–11:00 | Break/snack/walk | — | — | No screen |
| 11:00–12:00 | **Full Mock B:** Unique Substrings of Size K + Fraction to Recurring Decimal | [Find K-Length Substrings With No Repeated Characters](https://leetcode.com/problems/find-k-length-substrings-with-no-repeated-characters/)<br>[Fraction to Recurring Decimal](https://leetcode.com/problems/fraction-to-recurring-decimal/) | [LongestSubstringVariations.java](../../src/main/java/org/chijai/day3/session1/LongestSubstringVariations.java) | Fixed window, then remainder-cycle map |
| 12:00–12:15 | Postmortem | — | — | First failures recorded |
| 12:15–12:33 | Repair Mock B | — | — | Closed-book reconstruction |
| 12:33–13:30 | Lunch + walk | — | — | Zero study |
| 13:30–14:30 | **Full Mock C:** Shortest Subarray to Remove + Validate BST | [Shortest Subarray to be Removed to Make Array Sorted](https://leetcode.com/problems/shortest-subarray-to-be-removed-to-make-array-sorted/)<br>[Validate Binary Search Tree](https://leetcode.com/problems/validate-binary-search-tree/) | [ValidateBST.java](../../src/main/java/org/chijai/day6/trees/session3/ValidateBST.java) | Two unrelated families; communicate transitions |
| 14:30–14:45 | Postmortem | — | — | First failures recorded |
| 14:45–15:03 | Repair Mock C | — | — | Closed-book reconstruction |
| 15:03–15:20 | Break | — | — | No screen |
| 15:20–15:38 | Java reliability 1: HashMap frequency + deterministic tie | — | — | Compile from blank |
| 15:38–15:56 | Java reliability 2: BFS queue + direction array | — | — | Compile from blank |
| 15:56–16:14 | Java reliability 3: `PriorityQueue<int[]>` comparator + `long` distance | — | — | Compile from blank |
| 16:14–16:20 | Break | — | — | Water |
| 16:20–16:38 | Mutation: Highest Average → top five averages per student | [High Five](https://leetcode.com/problems/high-five/) | — | Explain heap size 5 and missing-count policy |
| 16:38–16:56 | Mutation: Number of Islands → max area | [Max Area of Island](https://leetcode.com/problems/max-area-of-island/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | DFS returns component size |
| 16:56–17:14 | Mutation: rotated minimum → duplicates | [Find Minimum in Rotated Sorted Array II](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/) | [BinarySearchPatternLab.java](../../src/main/java/org/chijai/patterns/binarysearch/BinarySearchPatternLab.java) | Explain ambiguous `mid == right` shrink |
| 17:14–17:20 | Break | — | — | Scores recorded |
| 17:20–17:40 | Speak 30-second introduction + “Why Goldman/Why this role?” three times | — | — | Third version natural and under 90 seconds total |
| 17:40–18:00 | Speak three behavioral answers from Section 12 | — | — | Situation/action/result/learning clear |
| 18:00 onward | Gym/walk, dinner, leisure | — | — | No heavy DSA |
| 22:30 | Lights out | — | — | Sleep protected |

### Sunday pass gate

Pass if at least two of three are true:

- two-problem mock produces two correct approaches and at least one fully runnable solution;
- no repeated `J`/`B` error across two consecutive mocks;
- every solution begins with a stated invariant and ends with tests/complexity.

If the gate is not met, Monday evening’s mock uses the weakest repeated family. Do **not** add hours Sunday night.

---

# 8. Monday, 7 September — office day, retention only

| Time | Exact work | WIN |
|---|---|---|
| 06:30–06:45 | Wake/water | No phone |
| 06:45–07:03 | Recall: one binary-search problem from red list | Score recorded |
| 07:03–07:21 | Recall: one graph/tree problem from red list | Score recorded |
| 07:21–07:39 | Recall: one window/stack/heap problem from red list | Score recorded |
| 07:39–08:00 | Breakfast/get ready | Study closed |
| Office hours | Work normally. No LeetCode during work. At lunch, only read the 10-item red list once for at most 5 minutes. | Attention preserved |
| 20:00–20:30 | Dinner + reset | No study |
| 20:30–21:30 | **Full Mock D:** Grid Maximum Path + First Unique Character | Exact 60-minute protocol |
| 21:30–21:45 | Postmortem and schedule one Tuesday recall | One failure, one fix |
| 21:45 onward | Stop; wind down | No rescue session |
| 22:30 | Lights out | Sleep protected |

If office timing slips, preserve only 20:30–21:30. Cut the morning recalls before moving the mock or sleep.

---

# 9. Tuesday, 8 September — final dress rehearsal

| Time | Exact work | WIN |
|---|---|---|
| 06:30–06:45 | Wake/water | No phone |
| 06:45–07:03 | Score Aggregation from blank | Deterministic tie policy and overflow safe sum |
| 07:03–07:21 | Trapping Rain Water from blank | Two-pointer proof spoken |
| 07:21–07:39 | Validate BST from blank | `long` ancestor bounds |
| 07:39–08:00 | Breakfast/get ready | Study closed |
| Office hours | Work normally. No new questions, videos, or forums. | Calm preserved |
| 20:00–20:30 | Dinner + Zoom/CoderPad equipment check | Charger, internet, hotspot, camera, mic ready |
| 20:30–21:30 | **Final Dress Rehearsal:** String Compression + Number of Islands | Camera on; blank editor; speak exactly as in interview |
| 21:30–21:40 | Write only three reminders on one paper card | No detailed postmortem |
| 21:40–22:00 | Lay out laptop/charger/headset/water/notebook; set alarms | Logistics complete |
| 22:00 onward | No interview material | Wind down |
| 22:30 | Lights out | Preparation finished |

The three-reminder card must be:

```text
1. Clarify + example before code.
2. Invariant aloud; test normal/boundary/tricky.
3. If stuck: baseline → repeated work → optimize. Keep speaking.
```

---

# 10. Wednesday, 9 September — interview day

**Interview:** 15:00–16:00 IST.  
**Purpose of the morning:** activate, not evaluate.

| Time | Exact action | Rule |
|---|---|---|
| 07:30–08:00 | Wake, water, normal breakfast | No interview forum/search |
| 08:00–08:20 | Walk/stretch/prayer | Reduce arousal |
| 08:20–08:38 | Familiar warm-up: Binary Search boundary | Stop at 18 minutes even if imperfect |
| 08:38–08:56 | Familiar warm-up: Number of Islands | Speak invariant |
| 08:56–09:14 | Familiar warm-up: Longest Substring Without Repeating Characters | Speak window contract |
| 09:14–09:30 | Scores, close editor | No repair unless a syntax typo took under 2 minutes |
| 09:30–10:00 | Read pattern-recognition sheet and three-reminder card | No coding |
| 10:00–11:30 | Away from DSA; normal morning tasks | No YouTube/Reddit/LeetCode |
| 11:30–12:00 | Early light lunch preparation | Familiar food only |
| 12:00–12:30 | Lunch | Moderate portion; hydrate normally |
| 12:30–13:00 | Walk/sit quietly | No nap longer than 20 minutes |
| 13:00–13:15 | Verify laptop power, charger, headset, camera, mic, Zoom, browser, internet, hotspot | Use calendar invite; do not paste credentials elsewhere |
| 13:15–13:25 | Open invitation and CoderPad; ensure Java selected if allowed | Do not modify supplied code prematurely |
| 13:25–13:40 | Read introduction, two VP answers, and interviewer questions once | No memorization loop |
| 13:40–14:00 | Away from screen; breathe/walk | Physical reset |
| 14:00–14:10 | Read 10-item red list once | No solutions |
| 14:10–14:20 | On paper, write Java reminders: `long`, null/empty, comparator, boundary, test | One small card only |
| 14:20–14:30 | Bathroom, water, room temperature, notifications off | Logistics complete |
| 14:30–14:40 | Join Zoom waiting room if appropriate; camera framing and audio | Professional setup |
| 14:40–14:50 | Box breathing: inhale 4, hold 4, exhale 4, hold 4 × 5 | No problem solving |
| 14:50–14:57 | Sit upright; read three-reminder card | Calm attention |
| 14:57–15:00 | Smile, hands off keyboard, wait | Do not rehearse |
| 15:00 | Begin | “Hi Manju, it’s great to meet you. Thank you for the time.” |

### During the interview: exact operating script

When a problem arrives:

1. “Let me restate it to make sure I have the contract right.”
2. Ask only relevant ambiguities: null/empty, duplicates, ordering, mutation, overflow, tie behavior.
3. Work one small example aloud.
4. “A straightforward correct solution is __ with __ complexity.”
5. “The repeated work/bottleneck is __, so I can improve it using __.”
6. “The invariant I want to maintain is __.”
7. “Before I code, the target complexity is __ time and __ space.”
8. Code in small methods; narrate decisions, not keystrokes.
9. “I’ll test a normal case, a boundary case, and a case that stresses __.”
10. “The final complexity is __ because __.”

If stuck for 90 seconds:

> “I’m not seeing the optimized transition cleanly yet. I’ll anchor us with the correct baseline, identify its repeated work, and improve from there.”

If the interviewer gives a hint:

> “That suggests __. Let me connect it to the invariant: __.”

If a test fails:

> “The observed failure is __. I expected __. I’ll trace the state at __ before changing code.”

If time is nearly over:

> “The remaining implementation is __. The invariant is __, and the complexity would be __. I’ll finish the highest-risk part first.”

Do not apologize repeatedly. One correction, one explanation, continue.

---

# 11. Solution vault — open only after the attempt

## 11.1 Friday Day 5 answer cards

| Problem | LeetCode | Local Java | Recognition and optimal solution | Correctness contract | Complexity | Must-test cases |
|---|---|---|---|---|---|---|
| Intersection of Two Linked Lists | [LeetCode](https://leetcode.com/problems/intersection-of-two-linked-lists/) | [Intersection.java](../../src/main/java/org/chijai/day4/LinkedList/session1/Intersection.java) | Walk `a` and `b`; on null, switch each to the other head; compare node identity | Each pointer travels `m+n`; unequal prefixes cancel, so they meet at intersection or both null | `O(m+n)` time, `O(1)` space | either null; disjoint; intersect at head; unequal lengths |
| Min Stack | [LeetCode](https://leetcode.com/problems/min-stack/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) | Each pushed node/pair stores value and minimum-so-far, or use synchronized value/min stacks | Top min equals minimum of every value currently below it; duplicate minima are stored separately | all operations `O(1)` | duplicate min; pop min; one element; negative values |
| Queue Using Stacks | [LeetCode](https://leetcode.com/problems/implement-queue-using-stacks/) | [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) | Push to `in`; pop/peek from `out`; transfer only when `out` is empty | `out` contains oldest items in dequeue order; `in` contains newer items in reverse order | amortized `O(1)`, `O(n)` space | alternating push/pop; transfer once; empty policy |
| Stack Using Queues | [LeetCode](https://leetcode.com/problems/implement-stack-using-queues/) | [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) | After push, rotate the queue until new value is at front | Queue front is always the stack top | push `O(n)`, pop/top `O(1)`; `O(n)` space | multiple pushes; pop then push; empty policy |
| Next Greater Element I | [LeetCode](https://leetcode.com/problems/next-greater-element-i/) | [NextGreaterElement.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/NextGreaterElement.java) | Scan `nums2`; decreasing stack; current value resolves all smaller tops into a map | Stack values are unresolved in decreasing order; each pop has found its first greater right value | `O(n+m)`, `O(n)` | decreasing; increasing; unresolved; single element |
| Next Greater Element II | [LeetCode](https://leetcode.com/problems/next-greater-element-ii/) | [NextGreaterElement.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/NextGreaterElement.java) | Scan indices `0..2n-1` using `% n`; resolve stack; push original indices only during first pass | Unresolved indices remain decreasing by value and get exactly one answer | `O(n)`, `O(n)` | wraparound; all equal; decreasing; one value |
| Online Stock Span | [LeetCode](https://leetcode.com/problems/online-stock-span/) | [OnlineStockSpan.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/OnlineStockSpan.java) | Store `(price, compressedSpan)`; pop and add spans while top price `<= current` | Stack prices decrease; each pair summarizes consecutive dominated days | amortized `O(1)` per call, `O(n)` | equal prices; increasing; decreasing; one price |
| Largest Rectangle | [LeetCode](https://leetcode.com/problems/largest-rectangle-in-histogram/) | [LargestRectangle.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java) | Increasing index stack; append conceptual height 0; when lower bar arrives, pop height, left boundary is new top, width is `i-left-1` | A stacked bar waits until its first smaller right boundary; new top marks previous smaller left boundary | `O(n)`, `O(n)` | `[2,1,5,6,2,3]`; equal bars; increasing; empty |
| Sum of Subarray Minimums | [LeetCode](https://leetcode.com/problems/sum-of-subarray-minimums/) | [SumOfSubarrayMinimums.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SumOfSubarrayMinimums.java) | Contribution: `arr[i] * leftChoices * rightChoices`; make one boundary strict and the other non-strict | Asymmetric tie rule assigns every subarray with equal minima to exactly one index | `O(n)`, `O(n)` | duplicates `[2,2]`; increasing; decreasing; modulo/overflow |
| Kth Largest in Stream | [LeetCode](https://leetcode.com/problems/kth-largest-element-in-a-stream/) | [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) | Maintain min-heap of at most `k`; discard smallest when size exceeds `k` | Heap contains exactly the `k` largest values seen; root is kth largest | initialization `O(n log k)`, add `O(log k)`, space `O(k)` | duplicates; initial size `<k` policy; negative values |
| K Closest Points | [LeetCode](https://leetcode.com/problems/k-closest-points-to-origin/) | [KClosestPointsToOrigin.java](../../src/main/java/org/chijai/day7/session1/heap/KClosestPointsToOrigin.java) | Keep size-`k` max-heap by squared distance, or sort when simplicity wins | Heap contains best `k` points seen; root is worst retained point | heap `O(n log k)`, `O(k)` | ties; origin; large coordinates use `long`; `k=n` |
| Evaluate RPN | [LeetCode](https://leetcode.com/problems/evaluate-reverse-polish-notation/) | [EvalRPN.java](../../src/main/java/org/chijai/day5/stack/session3/EvalRPN.java) | Push numbers; operator pops `right` then `left`; push `left op right` | Stack contains values of fully evaluated prefix expressions | `O(n)`, `O(n)` | subtraction/division order; negative; multi-digit |
| Phone Letter Combinations | [LeetCode](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) | [LetterCombinationsOfAPhoneNumber.java](../../src/main/java/org/chijai/day11/backtracking/session1/LetterCombinationsOfAPhoneNumber.java) | Backtrack by digit index; append one mapped letter, recurse, delete | Path length equals processed digit count; recursion enumerates Cartesian product | `O(4^n * n)` output work, `O(n)` stack | empty input; digits 7/9; one digit |
| TimeMap | [LeetCode](https://leetcode.com/problems/time-based-key-value-store/) | [TimeBasedKeyValueStore.java](../../src/main/java/org/chijai/day2/session3/TimeBasedKeyValueStore.java) | Map key to timestamp-sorted list; binary search greatest timestamp `<= query` | Search interval always contains possible floor timestamp | set `O(1)` if ordered input, get `O(log m)`, space `O(n)` | missing key; before first; exact; after last; out-of-order assumption |
| Trie | [LeetCode](https://leetcode.com/problems/implement-trie-prefix-tree/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) | Node has children and `terminal`; insert creates path, search requires terminal, prefix does not | Node reached after `i` chars represents exactly that prefix | `O(L)` each, space proportional to inserted chars | word vs prefix; duplicate insert; missing edge; empty policy |

## 11.2 Saturday Day 6 answer cards

| Problem | LeetCode | Local Java | Optimal solution and invariant | Complexity | Must-test cases |
|---|---|---|---|---|---|
| Add and Search Words | [LeetCode](https://leetcode.com/problems/design-add-and-search-words-data-structure/) | [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) | Trie DFS. Letter follows one child; `.` recursively tries every child; at pattern end return `terminal` | exact search `O(L)`; wildcard worst `O(26^L)` | exact, prefix only, one/multiple `.`, missing length |
| Gas Station | [LeetCode](https://leetcode.com/problems/gas-station/) | [GasStation.java](../../src/main/java/org/chijai/day9/dp/session1/GasStation.java) | If total gas < total cost return `-1`; otherwise accumulate tank and reset start to `i+1` when negative | `O(n)`, `O(1)` | impossible; valid start 0; reset late; one station |
| Task Scheduler | [LeetCode](https://leetcode.com/problems/task-scheduler/) | [TaskScheduler.java](../../src/main/java/org/chijai/day7/session1/heap/TaskScheduler.java) | Count frequencies. Let `max` be largest count and `ties` count tasks with `max`; answer `max(n, (max-1)*(cooldown+1)+ties)` | `O(n)` time, `O(1)` alphabet space | cooldown 0; no idle; one dominant task; tied maxima |
| Number of Provinces | [LeetCode](https://leetcode.com/problems/number-of-provinces/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | Scan cities; each unvisited city starts DFS/BFS over adjacency matrix and increments components | `O(n^2)`, `O(n)` | all isolated; all connected; one city |
| Network Delay Time | [LeetCode](https://leetcode.com/problems/network-delay-time/) | [NetworkDelayTime.java](../../src/main/java/org/chijai/day8/graph/session2/NetworkDelayTime.java) | Adjacency list + Dijkstra min-heap. Skip `(d,u)` when `d != dist[u]`; relax nonnegative edges | `O((V+E) log V)`, `O(V+E)` | unreachable; parallel edges; stale entry; source only |
| Word Ladder | [LeetCode](https://leetcode.com/problems/word-ladder/) | [WordLadder.java](../../src/main/java/org/chijai/day8/graph/session3/WordLadder.java) | BFS words by one-letter mutations; remove/mark on enqueue; level is path length | `O(N*L*26)` typical, `O(N)` space | end absent; begin=end policy; one-step; unreachable |
| Accounts Merge | [LeetCode](https://leetcode.com/problems/accounts-merge/) | [AccountsMerge.java](../../src/main/java/org/chijai/day8/graph/session3/AccountsMerge.java) | Map email to owner index and union repeated owners; collect emails by DSU root and sort | near `O(E α(A) + E log E)` | transitive merge; same name separate; single email |
| 01 Matrix | [LeetCode](https://leetcode.com/problems/01-matrix/) | [Matrix01.java](../../src/main/java/org/chijai/day8/graph/session1/Matrix01.java) | Enqueue all zeros with distance 0; mark others unknown; BFS four directions | `O(rows*cols)`, `O(rows*cols)` | all zero; one zero; corners; rectangular |
| Pacific Atlantic | [LeetCode](https://leetcode.com/problems/pacific-atlantic-water-flow/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | Reverse DFS/BFS from Pacific borders and Atlantic borders, moving to equal/higher cells; intersect visited sets | `O(rows*cols)`, `O(rows*cols)` | one cell; plateau; monotone grid |
| Surrounded Regions | [LeetCode](https://leetcode.com/problems/surrounded-regions/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | Mark `O` connected to border as safe; flip remaining `O` to `X`; restore safe marks | `O(rows*cols)`, traversal space | all border-connected; enclosed; one row/column |
| Right Side View | [LeetCode](https://leetcode.com/problems/binary-tree-right-side-view/) | [BinaryTreeSideView.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeSideView.java) | BFS levels and record last node, or right-first DFS first node per depth | `O(n)`, `O(width)` BFS | null; left-only; sparse tree |
| Path Sum | [LeetCode](https://leetcode.com/problems/path-sum/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) | DFS subtracting node values; success only when at leaf and remaining equals leaf value | `O(n)`, `O(height)` | null; negative values; equal sum at non-leaf; one node |
| Search BST | [LeetCode](https://leetcode.com/problems/search-in-a-binary-search-tree/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | Iteratively compare and choose one branch | `O(height)`, `O(1)` | found root/leaf; absent; skewed |
| Insert BST | [LeetCode](https://leetcode.com/problems/insert-into-a-binary-search-tree/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | Descend by comparison; attach at first null child; return original root | `O(height)`, `O(1)` iterative | empty; left/right; skewed; duplicate policy |
| Invert Tree | [LeetCode](https://leetcode.com/problems/invert-binary-tree/) | [InvertBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session3/InvertBinaryTree.java) | Swap left/right at each node, recursively or BFS | `O(n)`, `O(height)` recursion | null; one node; asymmetric tree |

## 11.3 Research-ranked Goldman drill cards

| Drill | LeetCode / closest analogue | Local Java |
|---|---|---|
| A. Highest Average Score | [High Five - closest analogue](https://leetcode.com/problems/high-five/) | — |
| B. String Compression | [String Compression](https://leetcode.com/problems/string-compression/) | — |
| C. Trapping Rain Water | [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) | [TrappingRainwater.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/TrappingRainwater.java) |
| D. Maximum path from bottom-left to top-right | Custom prompt; no exact LeetCode match | — |
| E. Number of Islands | [Number of Islands](https://leetcode.com/problems/number-of-islands/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |
| F. Validate BST | [Validate Binary Search Tree](https://leetcode.com/problems/validate-binary-search-tree/) | [ValidateBST.java](../../src/main/java/org/chijai/day6/trees/session3/ValidateBST.java) |
| G. Word Break | [Word Break](https://leetcode.com/problems/word-break/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |
| H. Find Minimum in Rotated Sorted Array | [Find Minimum in Rotated Sorted Array](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/) | [BinarySearchPatternLab.java](../../src/main/java/org/chijai/patterns/binarysearch/BinarySearchPatternLab.java) - closest local pattern lab |
| I. Minimum Window Substring | [Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/) | [MinimumWindowSubstring.java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) |
| J. Unique substrings of length K | [Find K-Length Substrings With No Repeated Characters](https://leetcode.com/problems/find-k-length-substrings-with-no-repeated-characters/) | [LongestSubstringVariations.java](../../src/main/java/org/chijai/day3/session1/LongestSubstringVariations.java) - closest local family |
| K. Fraction to Recurring Decimal | [Fraction to Recurring Decimal](https://leetcode.com/problems/fraction-to-recurring-decimal/) | — |
| L. First Unique Character / streaming first non-repeating | [First Unique Character in a String](https://leetcode.com/problems/first-unique-character-in-a-string/) | [FirstNonRepeatingCharacterUsingStreams.java](../../src/main/java/org/chijai/java/FirstNonRepeatingCharacterUsingStreams.java) |
| M. Shortest Subarray to Remove to Make Array Sorted | [Shortest Subarray to be Removed to Make Array Sorted](https://leetcode.com/problems/shortest-subarray-to-be-removed-to-make-array-sorted/) | — |
| N. Random Pick with Weight | [Random Pick with Weight](https://leetcode.com/problems/random-pick-with-weight/) | — |
| O. Interval/calendar conflict variation | [Meeting Rooms - closest analogue](https://leetcode.com/problems/meeting-rooms/) | [IntervalSortByStart.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalSortByStart.java) |
| P. IP address(es) with highest frequency | Custom prompt; no exact LeetCode match | — |
| Q. Forest from child-parent pairs | Custom prompt; no exact LeetCode match | — |
| R. Vehicle capacity / car pooling | [Car Pooling](https://leetcode.com/problems/car-pooling/) | [BoundaryDelta.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/BoundaryDelta.java) |

### A. Highest Average Score — highest priority

**Prompt:** Given `(student, score)` records, return the student with the highest average. Scores may be negative. Clarify rounding and tie behavior.

**Answer:** map each student to `(long sum, int count)`, then scan entries and compare averages. Avoid floating equality by comparing `sumA * countB` with `sumB * countA` using `long` if constraints permit. Apply a stated deterministic tie rule, such as lexicographically smaller name. If the required output is a floored integer, use the language’s specified behavior for negative division only after clarification.

```java
static String highestAverage(String[][] records) {
    Map<String, long[]> stats = new HashMap<>();
    for (String[] r : records) {
        long[] s = stats.computeIfAbsent(r[0], k -> new long[2]);
        s[0] += Long.parseLong(r[1]);
        s[1]++;
    }
    String best = null;
    for (Map.Entry<String, long[]> e : stats.entrySet()) {
        if (best == null) { best = e.getKey(); continue; }
        long[] a = e.getValue(), b = stats.get(best);
        long left = a[0] * b[1], right = b[0] * a[1];
        if (left > right || (left == right && e.getKey().compareTo(best) < 0)) {
            best = e.getKey();
        }
    }
    return best;
}
```

Time `O(n + u)`, space `O(u)`. Test negative-only scores, ties, one record, repeated names, and empty-input policy.

### B. String Compression — highest priority

**Prompt:** Compress a character array in-place so a run becomes character followed by decimal count when count > 1; return new length.

**Answer:** read complete runs with `read`; write the character with `write`; if run length > 1, write each digit of its decimal string. The invariant is that `[0, write)` is the correct compression of `[0, read)`.

```java
static int compress(char[] chars) {
    int read = 0, write = 0;
    while (read < chars.length) {
        int start = read;
        char c = chars[read];
        while (read < chars.length && chars[read] == c) read++;
        chars[write++] = c;
        int count = read - start;
        if (count > 1) {
            for (char d : Integer.toString(count).toCharArray()) chars[write++] = d;
        }
    }
    return write;
}
```

Time `O(n)`, auxiliary space `O(1)` excluding the small count string; if challenged, write digits to a temporary fixed buffer/reverse. Test empty, one char, no repeats, count 10+, and multiple runs.

### C. Trapping Rain Water — highest priority

**Answer:** two pointers with `leftMax` and `rightMax`. Process the side with the smaller maximum; the other side guarantees a sufficient boundary. Update the chosen max before adding `max-height`.

```java
static long trap(int[] h) {
    int l = 0, r = h.length - 1, leftMax = 0, rightMax = 0;
    long water = 0;
    while (l <= r) {
        if (leftMax <= rightMax) {
            leftMax = Math.max(leftMax, h[l]);
            water += leftMax - h[l++];
        } else {
            rightMax = Math.max(rightMax, h[r]);
            water += rightMax - h[r--];
        }
    }
    return water;
}
```

Time `O(n)`, space `O(1)`. Test empty/short, monotone, one basin, multiple basins, equal walls.

### D. Maximum path from bottom-left to top-right — highest priority

**Clarify first:** allowed moves, whether cells may be negative, whether obstacles exist, and what to return if unreachable.

For moves **up or right**, define `dp[r][c]` as maximum sum reaching cell `(r,c)` from bottom-left. Iterate rows bottom-to-top and columns left-to-right. Transition from below `(r+1,c)` or left `(r,c-1)`. Initialize unreachable states to negative infinity, not zero, when values can be negative. Space can compress to one row/column.

Time `O(rows*cols)`, space `O(cols)` or `O(rows*cols)`. Test one cell, one row, one column, all negative, competing paths, obstacles if present.

### E. Number of Islands — highest priority

Scan the grid. Each unvisited land cell increments the count and starts DFS/BFS that marks the entire four-directional component immediately on entry/enqueue. Time `O(rows*cols)`; space up to `O(rows*cols)`. Clarify whether diagonal adjacency counts.

### F. Validate BST — high priority

Use ancestor bounds, not parent-only checks. Recurse with `(long low, long high)` and require `low < node.val < high`; left receives upper bound `node.val`, right receives lower bound `node.val`. Time `O(n)`, stack `O(height)`. Test invalid grandchild, min/max int, duplicate policy, null.

### G. Word Break — high priority

`dp[i]` means `s[0..i)` is segmentable. Set `dp[0]=true`; for each end `i`, find `j<i` where `dp[j]` and `s[j..i)` is in the dictionary. Time `O(n^2)` substring-dependent; space `O(n)`. Test empty string, reuse of word, impossible suffix, overlapping choices.

### H. Find Minimum in Rotated Sorted Array — high priority

With distinct values: while `lo < hi`, if `a[mid] > a[hi]`, minimum is right of `mid`, so `lo=mid+1`; else minimum is at `mid` or left, so `hi=mid`. Return `a[lo]`. With duplicates, `a[mid]==a[hi]` requires `hi--`, degrading worst-case to `O(n)`. Test unrotated, pivot ends, two values, duplicates if allowed.

### I. Minimum Window Substring — high priority

Maintain `need`, window counts, and `formed` quotas. Expand right until all quotas satisfied; then shrink left while valid, saving the best before breaking validity. Extra copies do not increment `formed`. Time `O(|s|+|t|)`, space `O(alphabet)`. Test repeated required chars, impossible, exact match, empty-input policy.

### J. Unique substrings of length K — medium priority

Fixed window. If character set is bounded, maintain counts and a duplicate counter; otherwise use map. Add right char, remove char leaving when size exceeds `k`, and add substring only when size `k` and all counts are one. If the output is only a count, still need a set to deduplicate identical windows unless the prompt means positions. Clarify this.

### K. Fraction to Recurring Decimal — medium priority

Handle sign with `long`, append integer part, then simulate long division. Map each remainder to the output index where its digit begins. If a remainder repeats, insert `(` at the stored index and append `)`. Remainder zero terminates. Test zero numerator, negative, exact division, repeating, `Integer.MIN_VALUE`.

### L. First Unique Character / streaming first non-repeating — medium priority

For one final answer, frequency count then scan original order. For a stream output after each char, maintain counts plus queue; enqueue new chars and pop while queue front count > 1. Test all repeated, one char, late invalidation.

### M. Shortest Subarray to Remove to Make Array Sorted — medium priority

Find longest sorted prefix and suffix. If they overlap, answer 0. Start with removing all after prefix or before suffix. Use two pointers to merge prefix and suffix; when `a[i] <= a[j]`, update removal `j-i-1` and advance `i`, otherwise advance `j`. Time `O(n)`, space `O(1)`.

### N. Random Pick with Weight — stretch only

Build `long[] prefix` and total. Generate a target uniformly in `[1,total]`; binary search first prefix `>= target`. The interval length owned by each index equals its weight. Java randomness API/range must be clarified; avoid modulo bias. Constructor `O(n)`, pick `O(log n)`, space `O(n)`.

### O. Interval/calendar conflict variation — stretch only

Clarify whether the task is maximum overlap, merge, remove overlaps, or produce a price calendar; these are different problems. For Meeting Rooms II, sort starts/ends or use a min-heap of end times. For non-overlap selection, sort by end. For minimum price over interval segments, use sweep-line events plus a multiset/TreeMap of active prices. State boundary semantics such as `[start,end)`.

### P. IP address(es) with highest frequency — quick drill

Frequency map, track maximum, collect keys with that count, sort, join. Do not choose `TreeMap` as a substitute for correct maximum tracking. Time `O(n + u log u)` worst case; test tie, one IP, malformed-input policy.

### Q. Forest from child-parent pairs — quick drill

Clarify “largest”: greatest node value or tree with most nodes. For largest tree, build parent/children information, find roots (parents never appearing as children), DFS/BFS sizes, then apply the specified root tie-breaker. Validate the promise that each child has at most one parent; ask about cycles.

### R. Vehicle capacity / car pooling — quick drill

Each trip adds passengers at start and subtracts at end. Sort events or use ordered difference map; for equal coordinate, drop-offs must apply before pickups when intervals are `[start,end)`. Prefix active passengers; return false if capacity exceeded. Time `O(n log n)` or `O(n+range)` for bounded positions.

---

# 12. VP, resume, Java, and behavioral answer bank

The CoderPad is DSA-primary, but public reports often include introductions, project questions, and behavioral prompts. These answers are deliberately short so they do not consume coding time.

## 12.1 30-second introduction

> “I’m Chiranjeev Jain, a Java engineer with about eight years of experience building business-critical financial systems. My most relevant work has been on Nasdaq pre-trade risk infrastructure, where correctness, deterministic behavior, latency, resiliency, and safe change all matter. I’ve remained hands-on while also driving design decisions and helping teams unblock delivery. This Portfolio Risk Platforms role is compelling because it combines risk-engine domain depth, high-performance calculation workflows, modernization, and technical leadership.”

Do not add a five-minute biography. Stop and let Manju choose the next thread.

## 12.2 Why Goldman Sachs and why this role?

> “The role is a strong intersection of what I already do well and where I want broader ownership. I’ve worked close to pre-trade risk, so I understand that a risk number must be correct, timely, explainable, and resilient under stress—not merely computed. This team owns high-performance portfolio-risk engines and modernization across global stakeholders. I can contribute hands-on Java and performance experience immediately, while taking VP-level ownership of architecture, engineering standards, and cross-team execution.”

## 12.3 Why leave / why now?

> “I’m not moving away from a problem; I’m moving toward broader technical ownership. I want to apply my low-latency and financial-risk experience to a wider portfolio-risk platform, influence architecture across teams, and remain close to implementation. The scope described here—calculation engines, resiliency, modernization, and leadership—is the progression I’m looking for.”

## 12.4 What does VP-level hands-on leadership mean?

> “It means creating clarity and leverage while retaining technical credibility. I make the correctness and operational contracts explicit, use data to identify the bottleneck, choose an incremental architecture path, and keep the highest-risk technical work visible through prototypes, reviews, and tests. I unblock engineers rather than becoming the only person who can solve the system.”

## 12.5 How would you describe a risk engine’s non-functional contract?

> “I would make five dimensions explicit: correctness and reproducibility of every metric; latency and throughput under both normal and stress scenarios; lineage and explainability of inputs, model/version, and output; failure isolation with deterministic retry/idempotency; and observable freshness so consumers know whether a result is complete and current. Availability without trustworthy numbers is not success for a risk platform.”

## 12.6 Behavioral: teammate receives credit for your work

> “I would first avoid assuming intent. I’d speak privately with the teammate, describe the specific contribution and impact, and align on how we represent shared work next time. I’d then make ownership naturally visible through design records, demos, and status updates rather than creating a public confrontation. If it became a repeated pattern affecting the team, I’d raise it factually with the manager, focusing on delivery and trust rather than personal blame.”

## 12.7 Behavioral: you discover an error in a report already sent

> “First I would quantify the impact and stop further propagation. I’d notify the owner and affected consumers quickly with what is known, what remains uncertain, and when the corrected result will arrive. Then I’d issue a clearly versioned correction, preserve the audit trail, identify the control that failed, and add the smallest durable prevention—validation, reconciliation, ownership, or release gating. In a risk context, transparency and traceability are more important than hiding the mistake.”

## 12.8 Behavioral: manager asks for project information you cannot access

> “I would state the access limitation immediately rather than guess. I’d clarify the decision the manager needs to make, provide the information I can verify, identify the correct data owner, and request the minimum necessary access through the approved path. I’d give a concrete follow-up time and document assumptions. I would not bypass controls, especially for financial or client data.”

## 12.9 Java rapid answers

| Question | Crisp answer |
|---|---|
| `==` vs `equals` | `==` compares primitive values or object references; `equals` expresses logical equality when overridden. Equal objects must have equal hash codes. |
| Why String immutable? | Its value cannot change after construction; this enables safe sharing, stable hash keys, pooling, and easier concurrency/security reasoning. Operations return new strings. |
| Make a class immutable | Final class or controlled inheritance; private final fields; validate and defensively copy mutable inputs; no mutators; return defensive/unmodifiable views; do not let `this` escape construction. |
| `HashMap` complexity | Expected `O(1)` get/put with good hashes; collisions exist and modern Java can treeify eligible bins. Correct `equals/hashCode` and immutable keys matter. |
| `ArrayDeque` vs `Stack` | Prefer `ArrayDeque` for stack/queue operations; legacy `Stack` extends synchronized `Vector`. `ArrayDeque` disallows null and supports both ends. |
| Priority queue | Java `PriorityQueue` is a min-heap by default; comparator defines head. Use `Integer.compare`, not subtraction, to avoid overflow. |
| `long` in coding | Use for sums, products, squared distances, prefix totals, and midpoint-related arithmetic when `int` may overflow. Cast before multiplication. |
| `ConcurrentHashMap` | Concurrent retrievals and updates without one global map lock; compound read-modify-write must use atomic APIs such as `compute`, `merge`, or `putIfAbsent`. |
| `Runnable` vs `Callable` | `Runnable` returns no value and cannot declare checked exceptions; `Callable<V>` returns a value and can throw, normally observed through `Future`. |
| Thread pool sizing | CPU-bound near core count; blocking workloads may use more threads based on wait/compute ratio, but measure queueing, latency, saturation, and downstream capacity. |

## 12.10 Questions to ask Manju

Choose one, or two only if invited and time remains:

1. “For Portfolio Risk Platforms, what is the hardest engineering trade-off today between calculation timeliness, correctness, and resilience?”
2. “What would distinguish an excellent VP in this team after the first six months?”
3. “How does the team validate and roll out changes to risk calculations across regions without compromising reproducibility?”

Do not ask compensation, leave policy, or generic “what is the culture?” in this CoderPad round.

---

# 13. Blank-editor Java reliability sheet

Type these from memory Sunday; read only the reminder Tuesday/Wednesday.

```java
import java.util.*;

class Main {
    static final int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};

    public static void main(String[] args) {
        // Create tiny deterministic examples here.
    }
}
```

### Reliable idioms

```java
map.merge(key, 1, Integer::sum);
map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);

Deque<Integer> stack = new ArrayDeque<>();
Queue<Integer> queue = new ArrayDeque<>();

PriorityQueue<int[]> minHeap =
        new PriorityQueue<>(Comparator.comparingLong(a -> (long) a[0]));

int mid = lo + (hi - lo) / 2;
long square = (long) value * value;
long ceilDiv = (x + d - 1L) / d; // only when x >= 0 and d > 0
```

### Compile checklist before blaming the algorithm

- method signature matches caller;
- static/non-static context matches;
- generic types are complete;
- comparator uses the intended field/order;
- arrays use `.length`, strings use `.length()`, collections use `.size()`;
- equality for objects uses `.equals()` where appropriate;
- indices and loop bounds match inclusive/exclusive contract;
- `long` cast occurs before multiplication/addition can overflow;
- visited is marked on enqueue/entry, not after repeated discovery;
- test setup actually represents the intended edge case.

---

# 14. The one-page red-list format

Do not write “graphs” or “DP.” Write a failure that can be repaired.

Good examples:

```text
1. Histogram: after pop, width is i - newTop - 1; empty means i.
2. Dijkstra: skip stale heap entry when popped distance != dist[node].
3. BST validation: ancestor long bounds, strict inequality, duplicate policy.
4. Score average: clarify rounding/ties; compare without floating equality.
5. String compression: write count digits individually; test count >= 10.
6. Grid DFS: mark on entry/enqueue to prevent duplicate work.
7. Binary search: name first-true/last-false contract before choosing updates.
8. Java: cast to long before squared distance or cross multiplication.
9. Communication: state invariant before typing.
10. Testing: normal + empty/single + adversarial structure.
```

Bad examples: `trees`, `be faster`, `revise Java`, `I am weak`, `do more LC`.

---

# 15. Final anti-anxiety rules

1. A red score is routing information, not a prediction of interview outcome.
2. The timer decides when a drill ends; emotion does not.
3. The cut list decides what disappears; you do not renegotiate at night.
4. One failed mock produces one repair, not a new syllabus.
5. Do not search for more “recently asked” questions after Sunday 18:00.
6. Do not compare preparation counts with strangers.
7. The interview does not require instant recognition. It requires observable, correct engineering progress.
8. A correct baseline plus explicit optimization is senior behavior.
9. Hints are collaboration; connect them to the invariant and continue.
10. At 14:57 Wednesday, preparation is over. The only job left is to meet Manju and solve the problem in front of you.

---

# 16. Final readiness checklist

## By Sunday 12:00

- [ ] Day 5 and Day 6 named sets have recorded attempts, subject to the execution plan's safety cut.
- [ ] The remaining 80 are explicitly deferred.
- [ ] Coverage is marked closed.

## By Sunday 20:45

- [ ] At least one full two-problem mock is complete and scored.
- [ ] Core-12 retrieval is scored.
- [ ] Introduction, role fit, CoderPad method, and priority behavioral answers are recorded.
- [ ] Sunday readiness score and Monday target are written.

## By Monday 21:40

- [ ] Second full mock is complete and scored.
- [ ] Tuesday-leave gate is calculated mechanically.
- [ ] Tuesday Version A or B is selected.

## By Tuesday 21:45

- [ ] Final dress rehearsal complete.
- [ ] Only three reminders remain on the desk.
- [ ] Laptop, power, audio, camera, internet, and hotspot checked.
- [ ] No new preparation remains.

## Wednesday 14:57

- [ ] Water present.
- [ ] Notifications off.
- [ ] Invitation open.
- [ ] Java ready.
- [ ] Breathe.
- [ ] Begin with clarification, invariant, code, and tests.

**Final sentence:** I do not need to know the question in advance; I need to make my reasoning, correctness, and recovery visible.

---

# 17. PDF integration addendum - ROI decisions made on 4 September

Nine supplied PDFs were reviewed after this plan was created. They do **not** justify expanding the pre-CoderPad syllabus. They justify one bounded oral-rehearsal block and a separate answer script:

- [Goldman VP Final Rehearsal Script](GOLDMAN_SACHS_VP_FINAL_REHEARSAL_SCRIPT.md)

## 17.1 What to use now

| PDF | Decision before the 9 September CoderPad | Why |
|---|---|---|
| `VP Interview Master Reference - Nasdaq / PTR` | **Primary source for personal stories** | Candidate-specific, compact, and explicit about ownership boundaries |
| `CJ Script` | **Primary source for spoken wording** | Consistent with the PTR reference and includes a safe AWS/EKS answer |
| `Goldman Interview` | **Use only selected VP questions** | 275 pages are valuable for later rounds but too broad for the first-round clock |
| `Java Notes` | **Use only the final 80:20 JVM/concurrency floor** | The full 386 pages would displace coding practice |
| `Java Spring Frameworks Notes` | **Use only DI, bean scope, proxies, transactions, testing, Actuator** | Broad reference; portions are older or lower-probability trivia |
| `Goldman Cloud Migration` | **Design reference, not a personal story** | Its opening ownership claim conflicts with the candidate-specific safe version |
| `PTR Cloud Migration` | **Architecture drill-down only** | Useful concepts, but it contains internal details and conflicts with other notes on runtime/topology |
| `Docker Kubernetes` | **Five-minute fundamentals refresh only** | Three pages; useful vocabulary, insufficient as production evidence |
| `Financial Protocols` | **Conceptual protocol distinctions only** | Exact latency, volume, desk-usage, port, and venue assertions are not needed and should not be repeated without verification |

## 17.2 Source-truth rules

1. **Personal experience:** say only what you personally did. The safe cloud wording is: you worked with PTR in a Kubernetes/EKS environment; you did not own the migration.
2. **Team outcome:** use “we” only for a team result you can defend and explain your contribution immediately afterward.
3. **System explanation:** describe architecture without implying you designed every component.
4. **Hypothetical design:** begin with “I would” and derive from requirements.
5. Do not state exact production volumes, latency numbers, RTO/RPO, certificate lifetimes, internal endpoints, port numbers, region names, Java versions, or workload-controller choices unless independently verified and safe to disclose.
6. Do not expose internal class names, environment variables, infrastructure URLs, customer identifiers, or proprietary operational commands.

## 17.3 The only schedule change

Replace Sunday 17:20-18:00 with this recorded rehearsal. No other DSA or sleep slot changes.

| Time | Recording | Pass condition |
|---|---|---|
| 17:20-17:24 | Tell me about yourself | 45-60 seconds; current role -> relevant depth -> why this role |
| 17:24-17:28 | Why Goldman / why Portfolio Risk Platforms? | Role-specific, no brand/salary answer |
| 17:28-17:33 | What is PTR? | 60 seconds; stateful, event-driven, in-memory, deterministic tail latency |
| 17:33-17:40 | Negative-exposure incident | 90 seconds; symptom -> event ordering -> reproduction -> invariant -> validation |
| 17:40-17:46 | External API/rate-limiting ownership | 75 seconds; alternatives, chosen scope, multi-instance limitation |
| 17:46-17:51 | Split brain and fail-fast | 60 seconds; authority must be continuously proven |
| 17:51-17:56 | Honest AWS/EKS experience | 45 seconds; no migration-ownership inflation |
| 17:56-18:00 | Playback and score | Keep only one correction note per answer; no third take |

Recording score: `T` truth boundary, `S` structure, `D` technical depth, `J` judgment/trade-off, `C` concision. Pass means every dimension is at least 4/5 and no answer exceeds its time box.

Monday and Tuesday: no extra theory session. If commuting safely, listen once to the Sunday recordings; do not record while driving and do not replace the scheduled mock.

Wednesday 13:25-13:40: read only the Tier A Gold Page in the final rehearsal script. Do not open the source PDFs.
