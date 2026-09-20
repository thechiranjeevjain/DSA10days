# DSA pattern wheel — 24 cold Java anchors

**Current route:** [Start here](START_HERE_INTERVIEW_OPERATING_SYSTEM.md). This is the high-signal **reference deck for repairs**. The current October full-sitting order is the [150-minute mixed circuit](OCTOBER_150_MIN_MIXED_INTERVIEW_CIRCUIT.md); do not also advance this file's A01 full-code cursor. Its original six-slots-per-week instructions below document the earlier plan.

**Purpose:** Keep the implementation moves used in interviews retrievable. This is a selected wheel, not a promise to hold 263 solutions in memory. The [daily four-domain round-robin](DAILY_FOUR_DOMAIN_WARMUP.md) touches all A/S code styles as brief cues; this file governs their full-code attempts. The [263-question ledger](DSA_FULL_COLD_ATTEMPT_LEDGER.md) remains the full inventory; the [September/October plan](SEP_20_30_2026_INTERVIEW_READY_HOURLY_WINS.md) supplies the timeboxes, mocks, design work, and applications.

## How to run the wheel

1. Start at **A01** on 5 October 2026. Take the next numbered anchor in each full DSA slot. Six full slots per build week: Wed one, Thu one, Sat two, Sun two. The separate daily cue counter touches **two A/S code styles every day**; it does not advance the full-code anchor cursor. A full attempt satisfies a daily cue only when it is the exact assigned item.
2. Work cold in Java. Spend at most 25 minutes on a familiar anchor: 3 clarify, 4 derive/invariant, 14 code, 4 tests and complexity. For an anchor you never actually attempted, use up to 35 minutes in a weekend slot; on WFH days stop at 25 and score the partial attempt. Open old code only after scoring. Log the exact URL in the ledger.
3. `G1` means independently runnable code, normal and edge tests, invariant, complexity, and a short spoken explanation. `G2` means the same proof after a later closed-book repair. `Y` means the approach is known but code/test/explanation fails. `R` means the trigger or implementation is unavailable without help. A watched explanation does not count as green.
4. The next scheduled full slot goes to the oldest due **red**, then **yellow**, then a mock failure, then the next anchor. Ties: earlier due date, then lower A number. A displaced anchor remains next; never skip forward to make the counter look good. `R` is due next full DSA block; `Y` is due after two days at the next full block. `G1/G2` return through the daily A/S cue counter and on the next full-code wheel passage. A failed cue becomes a Sunday repair candidate, or the next full repair if it blocks a mock or fundamental code move. Use Review OS's due date for extra repair priority when that exact item is already entered there.
5. Before each full attempt, say the **trigger** in the row without seeing its answer. After the attempt, check whether the code move worked under one changed input/edge case. If two neighboring patterns get confused, use Sunday's weak-style review to compare their decision conditions; the next full repair can use the weaker one. Each Sunday mark the weakest pattern for the following week.
6. On reaching A24, start at A01 again. On later loops, replace the **last Saturday full slot** every build week with the next satellite S01–S08 below, but only if there is no due red/yellow or live-interview gap. This is transfer practice, not a 263-question completion quota. After S08, restart S01 with a different edge case or pick the next unseen problem from that same family in the ledger. Every fourth calendar week remains recovery; the daily brief cross-domain cue sweep continues but there are no new full-coverage attempts.

**Why this size:** Six full timed DSA reps + daily short mixed-domain cues + one mixed mock in a build week is a starting capacity, not a scientifically exact optimum. It covers distinct coding moves while preserving LLD/HLD, Java, applications, work, and recovery. At the 1 November checkpoint, use actual Y/R rate and energy to adjust it. A cold anchor is more useful than several nearly identical questions answered from memory.

## Anchor order — one implementation move per row

| # | Cold problem | Retrieval trigger and unique code move |
| ---: | --- | --- |
| A01 | [Two Sum](https://leetcode.com/problems/two-sum/) | Need complement seen earlier → hash lookup before insert. |
| A02 | [Number of Islands](https://leetcode.com/problems/number-of-islands/) | Connected cells → bounds, visited marking, DFS stack/recursion. |
| A03 | [Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/) | Minimum feasible answer → monotone predicate and binary-search bounds. |
| A04 | [LRU Cache](https://leetcode.com/problems/lru-cache/) | O(1) recency → map plus doubly linked list and sentinel updates. |
| A05 | [House Robber](https://leetcode.com/problems/house-robber/) | Take/skip recurrence → small-state DP and base cases. |
| A06 | [Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/) | Shortest valid range → required counts, expand/shrink invariant. |
| A07 | [Binary Tree Level Order Traversal](https://leetcode.com/problems/binary-tree-level-order-traversal/) | Process by depth → queue size boundary for each level. |
| A08 | [3Sum](https://leetcode.com/problems/3sum/) | Triplets with duplicate handling → sort, fixed index, two pointers. |
| A09 | [Course Schedule II](https://leetcode.com/problems/course-schedule-ii/) | Dependency order/cycle → indegree, queue, processed count. |
| A10 | [Daily Temperatures](https://leetcode.com/problems/daily-temperatures/) | Next greater position → decreasing index stack. |
| A11 | [Coin Change](https://leetcode.com/problems/coin-change/) | Reuse denominations, minimum items → unbounded DP and unreachable sentinel. |
| A12 | [Product of Array Except Self](https://leetcode.com/problems/product-of-array-except-self/) | Exclude current item without division → prefix and suffix accumulators. |
| A13 | [Reverse Linked List](https://leetcode.com/problems/reverse-linked-list/) | Mutate links safely → save next, reverse pointer, advance. |
| A14 | [Network Delay Time](https://leetcode.com/problems/network-delay-time/) | Nonnegative shortest paths → min heap, stale-distance skip, relaxation. |
| A15 | [Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) | Maximum in each fixed window → monotone deque of indices and expiry. |
| A16 | [Validate Binary Search Tree](https://leetcode.com/problems/validate-binary-search-tree/) | Whole-subtree ordering → recursive lower/upper bounds. |
| A17 | [Merge Intervals](https://leetcode.com/problems/merge-intervals/) | Overlap after ordering → sort by start and merge boundary. |
| A18 | [Subsets](https://leetcode.com/problems/subsets/) | All include/exclude choices → backtracking state and copy-on-emit. |
| A19 | [Redundant Connection](https://leetcode.com/problems/redundant-connection/) | First edge joining an existing component → DSU find/union. |
| A20 | [Binary Tree Maximum Path Sum](https://leetcode.com/problems/binary-tree-maximum-path-sum/) | Best path may turn at a node → postorder return versus global answer. |
| A21 | [Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/) | Keep k highest counts → frequency map and bounded heap. |
| A22 | [Search in Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/) | One side remains sorted → inclusive/exclusive boundary reasoning. |
| A23 | [Implement Trie](https://leetcode.com/problems/implement-trie-prefix-tree/) | Prefix queries → character transitions and terminal marker. |
| A24 | [Rotting Oranges](https://leetcode.com/problems/rotting-oranges/) | Simultaneous spreading → multi-source BFS levels and remaining count. |

These are **links to prompts**, not claims that the matching local Java files are correct. The generated [source index](07_LEETCODE_SOLVED_INDEX.md) links local examples for most rows; 3Sum is in the seven-day sheet. Read any local implementation after the cold attempt.

## Satellite transfer order — one per build week after A24

| # | Cold problem | Distinct move tested |
| ---: | --- | --- |
| S01 | [Jump Game](https://leetcode.com/problems/jump-game/) | Greedy farthest-reachable invariant. |
| S02 | [Edit Distance](https://leetcode.com/problems/edit-distance/) | Two-dimensional DP transitions and base row/column. |
| S03 | [Word Search](https://leetcode.com/problems/word-search/) | Grid backtracking with visited restoration. |
| S04 | [Find Median from Data Stream](https://leetcode.com/problems/find-median-from-data-stream/) | Two-heap balancing and median invariant. |
| S05 | [Car Pooling](https://leetcode.com/problems/car-pooling/) | Coordinate events / difference sweep with departure semantics. |
| S06 | [Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/) | Tails array with lower-bound search. |
| S07 | [Linked List Cycle II](https://leetcode.com/problems/linked-list-cycle-ii/) | Floyd meeting and cycle-entry reconstruction. |
| S08 | [Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | Longest-valid variable window; contrast with A06 shortest-valid window. |

## Stop and change rules

- If **2 of the last 3 full attempts are Y/R**, replace the next two anchors with closed-book repairs of those exact code moves. Keep the mock and job blocks.
- If a mock or real interview fails on a pattern absent from A/S, add that exact question to the ledger and use the next satellite slot for it. Do not add another daily slot.
- If a pattern is G1 twice at least 14 days apart and one changed edge case passes, its next appearance can be a 10-minute micro recall. Use the freed full slot for the next satellite or a weak pattern.
- If energy falls for three days, follow the main plan's recovery protocol. A 10-minute code fragment keeps contact; it does not prove full readiness.
