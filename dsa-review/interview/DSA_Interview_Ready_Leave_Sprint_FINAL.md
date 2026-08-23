# DSA Interview-Ready Leave Sprint — Deterministic 7-Day Maximum

**Purpose:** convert existing DSA knowledge into reliable closed-book interview performance.

**Maximum duration:** 7 leave days.  
**Early-exit rule:** from the end of Day 3 onward, if the qualification gate passes, **stop the sprint, cancel unused leave, and start interviewing**.

**Primary allocation:** ~85% DSA, ~15% Java/backend/LLD/HLD maintenance.

---

# 0. North Star

Given a problem **without family/pattern/invariant hints**:

1. Understand exactly what must be computed.
2. State brute force.
3. Identify the bottleneck/repeated work.
4. Identify the information/state that would remove the bottleneck.
5. State the invariant.
6. Choose the data structure/pattern.
7. Write working Java from a blank editor.
8. Test/debug independently.
9. Explain correctness + time/space complexity.
10. Adapt when one requirement changes.

**Success is repeatable interview performance, not completing 150 questions.**

---

# 1. Absolute Rules

- Use a **blank Java file** for every attempt.
- No notes, old Java, solution, Family column, Pattern column, or Signal/Invariant before the attempt ends.
- Follow the clock. Do not extend a slot.
- Do not swap, skip, reorder, or replace problems.
- Do not add extra problems during the sprint.
- At the end of a slot, record the result and move to the next row.
- Use the original sprint/reference Java **only during a scheduled postmortem**, never during the closed-book attempt.
- Break when the timetable says break.
- Stop when the timetable says stop.
- The only uncertainty allowed is whether the solution works.

---

# 2. The Only Mental Sequence To Memorize

```text
WHAT → SLOW → NEED → KEEP → CODE → PROVE
```

- **WHAT** — What exactly must I compute?
- **SLOW** — What is brute force and why is it too slow?
- **NEED** — What information would make the slow solution easy?
- **KEEP** — What state/invariant must remain true? Which DS supports it?
- **CODE** — Translate it to Java.
- **PROVE** — Trace, edge cases, complexity, explanation.

---

# 3. Exact 20-Minute Problem Protocol

Use this for every normal 20-minute problem/review slot.

| Minute | Mandatory action | Required output |
|---:|---|---|
| 00:00–01:00 | **WHAT** | exact input/output/objective |
| 01:00–02:00 | Example | one tiny walkthrough |
| 02:00–03:00 | **SLOW** | brute-force approach |
| 03:00–04:00 | **NEED** | bottleneck / repeated work / missing information |
| 04:00–05:00 | **KEEP** | state + invariant + candidate DS/pattern |
| 05:00–14:00 | **CODE** | Java from blank editor |
| 14:00–17:00 | **PROVE** | normal + boundary + adversarial case; debug |
| 17:00–18:00 | Complexity | time + space |
| 18:00–19:00 | Explain | 60-second interview explanation |
| 19:00–20:00 | Score | G / Y / R + failure code |
| 20:00 | **HARD STOP** | move to next timetable row |

---

# 4. Scoring

## GREEN — G
All of the following without help:
- viable optimized derivation;
- correct state/invariant;
- working or essentially working Java;
- tests/edge cases;
- correct complexity;
- clear explanation.

## YELLOW — Y
Correct family/idea, but one or more:
- implementation trouble;
- missed edge case;
- needed a hint;
- debugging gap;
- explanation weakness;
- unfinished within the time-box.

## RED — R
One or more:
- no viable optimized derivation;
- wrong fundamental approach;
- major invariant failure;
- solution lookup required;
- implementation fundamentally incomplete.

## Failure codes

| Code | Meaning |
|---|---|
| U | problem understanding |
| B | brute force |
| N | bottleneck / needed information |
| I | invariant / reasoning |
| P | pattern recognition |
| D | data structure |
| J | Java implementation |
| E | edge cases |
| G | debugging |
| C | complexity |
| T | transfer / mutation |
| M | memorized but not understood |

Record the **first point where the chain broke**.

---

# 5. Exact 40-Minute Mock Protocol

| Minute | Mandatory action |
|---:|---|
| 00–05 | restate problem + examples + constraints |
| 05–10 | brute force → bottleneck → state/invariant → optimized plan |
| 10–30 | code Java from blank editor |
| 30–35 | test + debug |
| 35–38 | complexity + trade-offs |
| 38–40 | 2-minute explanation + PASS/FAIL |

## Mock PASS
- no solution/hints;
- optimized approach independently derived;
- code is working or has only a tiny mechanical defect;
- tests are sensible;
- complexity is correct;
- explanation is coherent.

Otherwise: **FAIL**.

---

# 6. Exact Postmortem Protocol

Use only when the timetable says `POSTMORTEM`.

```text
00–05  Inspect reference only if needed.
05–08  Write the missing insight in one sentence.
08–10  CLOSE reference.
10–17  Reconstruct the key code/logic from memory.
17–20  Execute the fixed mutation written for that day.
```

If the mock passed, still use the same 20 minutes:
- first 10 minutes: identify one implementation risk + one edge case;
- last 10 minutes: fixed mutation.

---

# 7. Break Protocol

During every 10-minute break:

```text
Leave chair.
Drink water.
Walk/stretch/toilet as needed.
No LeetCode.
No notes.
No YouTube/tutorials.
No social-media rabbit hole.
Return when the next row starts.
```

Lunch is fully off from DSA.

---

# 8. EARLY-EXIT QUALIFICATION GATE

Run this **only at the scheduled gate time**.

Earliest possible exit: **end of Day 3**.

## PASS only if ALL are true

1. **Last 15 normal DSA attempts:** at least **13 GREEN**.
2. **Repeated fundamental RED:** **0** on the fixed review problems.
3. **Last 3 fixed mocks:** **3 consecutive PASS**.
4. **Last 5 blank-editor implementation attempts:** at least **4 GREEN**.
5. **Last 3 fixed mutations:** at least **2 PASS**.
6. No Family/Pattern/Invariant hints were viewed before attempts.
7. No solution lookup occurred during any attempt counted above.

```text
IF ALL TRUE:
    SPRINT COMPLETE.
    CANCEL ALL UNUSED LEAVE DAYS.
    START GIVING INTERVIEWS.

IF ANY FALSE:
    DO NOT DEBATE.
    EXECUTE THE NEXT DAY EXACTLY AS WRITTEN.
```

The thresholds are practical stopping criteria, not claims of mathematical certainty.

---

# DAY 1 — CORE LINEAR BASELINE

**Objective:** test the highest-ROI linear/array/string fundamentals from blank memory.

| Time | Exact task |
|---|---|
| 09:00–09:20 | [2Sum / 3Sum / 4Sum](https://leetcode.com/problems/3sum/) |
| 09:20–09:40 | [Binary Search](https://leetcode.com/problems/binary-search/) |
| 09:40–10:00 | [Valid Anagram](https://leetcode.com/problems/valid-anagram/) |
| 10:00–10:10 | **BREAK** |
| 10:10–10:30 | [Valid Palindrome](https://leetcode.com/problems/valid-palindrome/) |
| 10:30–10:50 | [Majority Element](https://leetcode.com/problems/majority-element/) |
| 10:50–11:10 | [Sort Colors](https://leetcode.com/problems/sort-colors/) |
| 11:10–11:20 | **BREAK** |
| 11:20–11:40 | [Maximum Subarray — Kadane](https://leetcode.com/problems/maximum-subarray/) |
| 11:40–12:00 | [Best Time to Buy and Sell Stock](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/) |
| 12:00–13:00 | **LUNCH + WALK — DSA OFF** |
| 13:00–13:20 | [Product of Array Except Self](https://leetcode.com/problems/product-of-array-except-self/) |
| 13:20–13:40 | [Container With Most Water](https://leetcode.com/problems/container-with-most-water/) |
| 13:40–14:00 | [Meeting Rooms](https://leetcode.com/problems/meeting-rooms/) |
| 14:00–14:10 | **BREAK** |
| 14:10–14:30 | [Meeting Rooms II](https://leetcode.com/problems/meeting-rooms-ii/) |
| 14:30–14:50 | [Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/) |
| 14:50–15:10 | [Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/) |
| 15:10–15:20 | **BREAK** |
| 15:20–16:00 | **MOCK 1:** [Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/) |
| 16:00–16:20 | **POSTMORTEM + FIXED MUTATION:** Explain how the feasibility predicate changes for **Capacity to Ship Packages Within D Days**. No code. |
| 16:20–16:40 | Java maintenance: `HashMap`, `HashSet`, `TreeMap`, `PriorityQueue` — explain operation complexities aloud |
| 16:40–17:00 | Java maintenance: `equals()` + `hashCode()` contract + mutable-key failure |
| 17:00–17:20 | Java maintenance: `ArrayList` vs `LinkedList` vs `ArrayDeque` interview trade-offs |
| 17:20–17:30 | Record scoreboard. **No early exit on Day 1.** |

### Day 1 Scoreboard

```text
Normal attempts:  ___
GREEN:            ___
YELLOW:           ___
RED:              ___
Mock 1:           PASS / FAIL
Mutation 1:       PASS / FAIL
```

---

# DAY 2 — BINARY SEARCH + LINKED-LIST FOUNDATIONS

## Fixed reviews

These are mandatory regardless of yesterday's score.

| Time | Exact task |
|---|---|
| 09:00–09:20 | **REVIEW:** 2Sum / 3Sum / 4Sum |
| 09:20–09:40 | **REVIEW:** Binary Search |
| 09:40–10:00 | **REVIEW:** Longest Substring Without Repeating Characters |
| 10:00–10:10 | **BREAK** |
| 10:10–10:30 | [Search in Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/) |
| 10:30–10:50 | [Find First and Last Position](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) |
| 10:50–11:10 | [Search Insert Position](https://leetcode.com/problems/search-insert-position/) |
| 11:10–11:20 | **BREAK** |
| 11:20–11:40 | [Find Peak Element](https://leetcode.com/problems/find-peak-element/) |
| 11:40–12:00 | [First Bad Version](https://leetcode.com/problems/first-bad-version/) |
| 12:00–13:00 | **LUNCH + WALK — DSA OFF** |
| 13:00–13:20 | [Capacity to Ship Packages Within D Days](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/) |
| 13:20–13:40 | [Minimum Number of Days to Make m Bouquets](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/) |
| 13:40–14:00 | [Reverse Linked List](https://leetcode.com/problems/reverse-linked-list/) |
| 14:00–14:10 | **BREAK** |
| 14:10–14:30 | [Linked List Cycle](https://leetcode.com/problems/linked-list-cycle/) |
| 14:30–14:50 | [Merge Two Sorted Lists](https://leetcode.com/problems/merge-two-sorted-lists/) |
| 14:50–15:10 | [Middle of the Linked List](https://leetcode.com/problems/middle-of-the-linked-list/) |
| 15:10–15:20 | **BREAK** |
| 15:20–16:00 | **MOCK 2:** [LRU Cache](https://leetcode.com/problems/lru-cache/) |
| 16:00–16:20 | **POSTMORTEM + FIXED MUTATION:** LRU capacity can change at runtime; explain exactly what `resize(newCapacity)` must do and its complexity. |
| 16:20–16:40 | Java maintenance: `volatile` vs `synchronized` |
| 16:40–17:00 | Java maintenance: `AtomicInteger` / CAS / race condition |
| 17:00–17:20 | Java maintenance: `ExecutorService` / thread pool sizing / queueing |
| 17:20–17:30 | Record scoreboard. **No early exit on Day 2.** |

---

# DAY 3 — STACK / DEQUE / HEAP + FIRST QUALIFICATION GATE

## Fixed reviews

| Time | Exact task |
|---|---|
| 09:00–09:20 | **REVIEW:** Reverse Linked List |
| 09:20–09:40 | **REVIEW:** Linked List Cycle |
| 09:40–10:00 | **REVIEW:** LRU Cache core `get/put/moveToFront/evict` |
| 10:00–10:10 | **BREAK** |
| 10:10–10:30 | [Intersection of Two Linked Lists](https://leetcode.com/problems/intersection-of-two-linked-lists/) |
| 10:30–10:50 | [Linked List Cycle II](https://leetcode.com/problems/linked-list-cycle-ii/) |
| 10:50–11:10 | [Valid Parentheses](https://leetcode.com/problems/valid-parentheses/) |
| 11:10–11:20 | **BREAK** |
| 11:20–11:40 | [Min Stack](https://leetcode.com/problems/min-stack/) |
| 11:40–12:00 | [Implement Queue Using Stacks](https://leetcode.com/problems/implement-queue-using-stacks/) |
| 12:00–13:00 | **LUNCH + WALK — DSA OFF** |
| 13:00–13:20 | [Daily Temperatures](https://leetcode.com/problems/daily-temperatures/) |
| 13:20–13:40 | [Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) |
| 13:40–14:00 | [Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/) |
| 14:00–14:10 | **BREAK** |
| 14:10–14:30 | [Kth Largest Element in an Array](https://leetcode.com/problems/kth-largest-element-in-an-array/) |
| 14:30–14:50 | [Kth Largest Element in a Stream](https://leetcode.com/problems/kth-largest-element-in-a-stream/) |
| 14:50–15:10 | [Binary Tree Level Order Traversal](https://leetcode.com/problems/binary-tree-level-order-traversal/) |
| 15:10–15:20 | **BREAK** |
| 15:20–16:00 | **MOCK 3:** [Validate Binary Search Tree](https://leetcode.com/problems/validate-binary-search-tree/) |
| 16:00–16:20 | **POSTMORTEM + FIXED MUTATION:** duplicates are allowed **only in the right subtree**; explain how the BST-bound invariant changes. |
| 16:20–16:40 | Backend maintenance: DB index — what it accelerates and what it costs |
| 16:40–17:00 | Backend maintenance: transaction isolation / lost update / optimistic locking |
| 17:00–17:20 | Backend maintenance: idempotency + retries + duplicate request handling |
| 17:20–17:30 | **RUN EARLY-EXIT GATE.** If PASS, sprint ends. If FAIL, execute Day 4. |

---

# DAY 4 — TREES + GRAPH ENTRY

## Fixed reviews

| Time | Exact task |
|---|---|
| 09:00–09:20 | **REVIEW:** Valid Parentheses |
| 09:20–09:40 | **REVIEW:** Daily Temperatures |
| 09:40–10:00 | **REVIEW:** Top K Frequent Elements |
| 10:00–10:10 | **BREAK** |
| 10:10–10:30 | [Maximum Depth of Binary Tree](https://leetcode.com/problems/maximum-depth-of-binary-tree/) |
| 10:30–10:50 | [Binary Tree Inorder Traversal](https://leetcode.com/problems/binary-tree-inorder-traversal/) |
| 10:50–11:10 | [Binary Tree Preorder Traversal](https://leetcode.com/problems/binary-tree-preorder-traversal/) |
| 11:10–11:20 | **BREAK** |
| 11:20–11:40 | [Binary Tree Postorder Traversal](https://leetcode.com/problems/binary-tree-postorder-traversal/) |
| 11:40–12:00 | [Kth Smallest Element in a BST](https://leetcode.com/problems/kth-smallest-element-in-a-bst/) |
| 12:00–13:00 | **LUNCH + WALK — DSA OFF** |
| 13:00–13:20 | [Balanced Binary Tree](https://leetcode.com/problems/balanced-binary-tree/) |
| 13:20–13:40 | [Diameter of Binary Tree](https://leetcode.com/problems/diameter-of-binary-tree/) |
| 13:40–14:00 | [Lowest Common Ancestor of a Binary Tree](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/) |
| 14:00–14:10 | **BREAK** |
| 14:10–14:30 | [Lowest Common Ancestor of a BST](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/) |
| 14:30–14:50 | [Invert Binary Tree](https://leetcode.com/problems/invert-binary-tree/) |
| 14:50–15:10 | [Binary Tree Right Side View](https://leetcode.com/problems/binary-tree-right-side-view/) |
| 15:10–15:20 | **BREAK** |
| 15:20–16:00 | **MOCK 4:** [Number of Islands](https://leetcode.com/problems/number-of-islands/) |
| 16:00–16:20 | **POSTMORTEM + FIXED MUTATION:** instead of counting islands, return the **maximum island area**. Explain exactly what the DFS return value becomes. |
| 16:20–16:40 | LLD maintenance: requirements + API for an LRU cache |
| 16:40–17:00 | LLD maintenance: class/data-structure design for LRU |
| 17:00–17:20 | LLD maintenance: concurrency + extensibility trade-offs |
| 17:20–17:30 | **RUN EARLY-EXIT GATE.** If PASS, stop. Else Day 5. |

---

# DAY 5 — GRAPHS + DP CORE

## Fixed reviews

| Time | Exact task |
|---|---|
| 09:00–09:20 | **REVIEW:** Maximum Depth of Binary Tree |
| 09:20–09:40 | **REVIEW:** Diameter of Binary Tree |
| 09:40–10:00 | **REVIEW:** Number of Islands |
| 10:00–10:10 | **BREAK** |
| 10:10–10:30 | [Rotting Oranges](https://leetcode.com/problems/rotting-oranges/) |
| 10:30–10:50 | [01 Matrix](https://leetcode.com/problems/01-matrix/) |
| 10:50–11:10 | [Flood Fill](https://leetcode.com/problems/flood-fill/) |
| 11:10–11:20 | **BREAK** |
| 11:20–11:40 | [Number of Provinces](https://leetcode.com/problems/number-of-provinces/) |
| 11:40–12:00 | [Is Graph Bipartite?](https://leetcode.com/problems/is-graph-bipartite/) |
| 12:00–13:00 | **LUNCH + WALK — DSA OFF** |
| 13:00–13:20 | [Course Schedule II](https://leetcode.com/problems/course-schedule-ii/) |
| 13:20–13:40 | [Network Delay Time](https://leetcode.com/problems/network-delay-time/) |
| 13:40–14:00 | [Accounts Merge](https://leetcode.com/problems/accounts-merge/) |
| 14:00–14:10 | **BREAK** |
| 14:10–14:30 | [House Robber](https://leetcode.com/problems/house-robber/) |
| 14:30–14:50 | [Climbing Stairs](https://leetcode.com/problems/climbing-stairs/) |
| 14:50–15:10 | [Coin Change](https://leetcode.com/problems/coin-change/) |
| 15:10–15:20 | **BREAK** |
| 15:20–16:00 | **MOCK 5:** [Partition Equal Subset Sum](https://leetcode.com/problems/partition-equal-subset-sum/) |
| 16:00–16:20 | **POSTMORTEM + FIXED MUTATION:** instead of `total/2`, determine whether any subset reaches an arbitrary supplied `target`. State the DP meaning and loop direction. |
| 16:20–16:40 | HLD maintenance: requirements + scale + API |
| 16:40–17:00 | HLD maintenance: data model + cache + DB |
| 17:00–17:20 | HLD maintenance: failures + consistency + observability |
| 17:20–17:30 | **RUN EARLY-EXIT GATE.** If PASS, stop. Else Day 6. |

---

# DAY 6 — DP / BACKTRACKING + FIXED INTERLEAVED QUALIFICATION

## Fixed reviews

| Time | Exact task |
|---|---|
| 09:00–09:20 | **REVIEW:** Course Schedule II |
| 09:20–09:40 | **REVIEW:** Coin Change |
| 09:40–10:00 | **REVIEW:** Partition Equal Subset Sum |
| 10:00–10:10 | **BREAK** |
| 10:10–10:30 | [Unique Paths](https://leetcode.com/problems/unique-paths/) |
| 10:30–10:50 | [Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/) |
| 10:50–11:10 | [Subsets](https://leetcode.com/problems/subsets/) |
| 11:10–11:20 | **BREAK** |
| 11:20–11:40 | [Combination Sum](https://leetcode.com/problems/combination-sum/) |
| 11:40–12:00 | [Word Search](https://leetcode.com/problems/word-search/) |
| 12:00–13:00 | **LUNCH + WALK — DSA OFF** |
| 13:00–13:20 | [Letter Combinations of a Phone Number](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) |
| 13:20–13:40 | [Permutations](https://leetcode.com/problems/permutations/) |
| 13:40–14:00 | [Find Median from Data Stream](https://leetcode.com/problems/find-median-from-data-stream/) |
| 14:00–14:10 | **BREAK** |
| 14:10–14:30 | [Merge K Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/) |
| 14:30–15:10 | **MOCK 6A:** [Gas Station](https://leetcode.com/problems/gas-station/) |
| 15:10–15:20 | **BREAK** |
| 15:20–16:00 | **MOCK 6B:** [Task Scheduler](https://leetcode.com/problems/task-scheduler/) |
| 16:00–16:20 | **POSTMORTEM + FIXED MUTATION:** `Subsets` now contains duplicates; explain exactly how sorting + duplicate skipping changes backtracking. |
| 16:20–16:40 | Java/backend maintenance: `ConcurrentHashMap` + atomic compound operations |
| 16:40–17:00 | Java/backend maintenance: `CompletableFuture` + exception handling |
| 17:00–17:20 | Java/backend maintenance: GC roots / G1 / allocation pressure |
| 17:20–17:30 | **RUN EARLY-EXIT GATE.** If PASS, stop. Else execute Day 7. |

---

# DAY 7 — FINAL QUALIFICATION DAY

No new-sheet completion target. This day exists only to prove interview performance.

| Time | Exact task |
|---|---|
| 09:00–09:20 | **REVIEW:** Partition Equal Subset Sum |
| 09:20–09:40 | **REVIEW:** Word Search |
| 09:40–10:00 | **REVIEW:** Merge K Sorted Lists |
| 10:00–10:10 | **BREAK** |
| 10:10–10:50 | **MOCK 7A:** [Pacific Atlantic Water Flow](https://leetcode.com/problems/pacific-atlantic-water-flow/) |
| 10:50–11:10 | **POSTMORTEM 7A:** explain why reverse traversal from oceans is cheaper conceptually than launching a search from every cell |
| 11:10–11:20 | **BREAK** |
| 11:20–12:00 | **MOCK 7B:** [Maximum Profit in Job Scheduling](https://leetcode.com/problems/maximum-profit-in-job-scheduling/) |
| 12:00–13:00 | **LUNCH + WALK — DSA OFF** |
| 13:00–13:20 | **POSTMORTEM 7B:** state `dp[i]` in one sentence and explain the binary-search boundary |
| 13:20–14:00 | **MOCK 7C:** [Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/) |
| 14:00–14:20 | **POSTMORTEM 7C:** explain exactly when a popped bar learns its final width |
| 14:20–14:30 | **BREAK** |
| 14:30–15:10 | **MOCK 7D:** [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) |
| 15:10–15:30 | **POSTMORTEM 7D:** explain why the side with the smaller current boundary can be finalized |
| 15:30–15:40 | **BREAK** |
| 15:40–15:50 | **MUTATION A:** Longest Substring Without Repeats → at most `K` distinct |
| 15:50–16:00 | **MUTATION B:** Binary Search exact target → first index satisfying a monotonic predicate |
| 16:00–16:10 | **MUTATION C:** Number of Islands → maximum island area |
| 16:10–16:20 | **MUTATION D:** House Robber → circular houses |
| 16:20–16:40 | Final maintenance: Java/concurrency — 10 highest-risk questions from your existing notes, closed-book oral recall |
| 16:40–17:00 | Final maintenance: LLD — 20-minute LRU/Order-Book design explanation |
| 17:00–17:10 | Final maintenance: HLD — requirements → architecture → failure modes in 10 minutes |
| 17:10–17:30 | **FINAL QUALIFICATION GATE + DECISION** |

---

# 9. Final Qualification Decision

At Day 7, calculate only these metrics:

```text
LAST 15 NORMAL ATTEMPTS
GREEN = ___ / 15

REPEATED FUNDAMENTAL RED
COUNT = ___

LAST 3 MOCKS
PASS / FAIL:
1. ___
2. ___
3. ___

LAST 5 BLANK JAVA ATTEMPTS
GREEN = ___ / 5

LAST 3 MUTATIONS
PASS = ___ / 3
```

## INTERVIEW READY

All must be true:

```text
[ ] Last 15 normal attempts >= 13 GREEN
[ ] Repeated fundamental RED = 0
[ ] Last 3 mocks = 3 PASS
[ ] Last 5 blank Java attempts >= 4 GREEN
[ ] Last 3 mutations >= 2 PASS
[ ] No family/pattern hints used
[ ] No solution lookup during counted attempts
```

If true:

```text
DSA PREP PHASE = COMPLETE ENOUGH.

STOP accumulating sheets.
START interviews immediately.

Maintenance after this:
2 DSA problems/week
+ spaced review of failures
+ occasional timed mock.
```

If false:

```text
DO NOT restart a giant 150-problem sheet.

Continue working only on the exact dimensions that failed
while interviews begin in parallel.
```

---

# 10. What This Sprint Deliberately Does NOT Optimize For

It does **not** optimize for:

- 150/150 completion;
- memorizing every solution;
- touching every low-ROI problem;
- feeling zero anxiety;
- perfect performance;
- LeetCode Hard coverage.

It optimizes for:

```text
READ
→ WHAT
→ SLOW
→ NEED
→ KEEP
→ CODE
→ PROVE
→ MUTATE
→ INTERVIEW
```

---

# 11. Problems From The Original Bank Not Scheduled Here

They remain a **post-sprint reference/overflow bank**, not a leave-day obligation.

Do not use remaining leave merely to finish them after the qualification gate has passed.

The original ranked bank remains useful for:
- future spaced review;
- company-specific preparation;
- targeted repair after interview feedback;
- long-term maintenance.

---

# 12. Execution Mantra

```text
LOOK AT CLOCK
→ EXECUTE CURRENT ROW
→ FOLLOW 20-MIN PROTOCOL
→ SCORE
→ MOVE ON
→ RUN GATE ONLY WHEN SCHEDULED
→ PASS = STOP
→ FAIL = NEXT PREWRITTEN DAY
```

**No randomness. No ambiguity. No choosing. No guilt-based extra study.**
