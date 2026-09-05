# Goldman Sachs - Master Revision Sheet (DSA + LLD + HLD)

> **Purpose:** Timed, blind reconstruction for the Goldman Sachs VP Software Engineering process, with the immediate 60-minute CoderPad treated as the first gate.
>
> **Scope:** 18 DSA + 7 LLD + 5 HLD = **30 revision items**.
>
> **LeetCode links:** Exact where available. Custom Goldman/trading questions use a clearly labelled closest analogue or `N/A`.
>
> **Local links:** DSA links point to portable repository-relative Java files. LLD/HLD links point to the focused local project README.
>
> **Rule:** Read only **Problem + Pattern + Minimal Hint** -> reconstruct from memory -> compare with local material only after the hard stop.

Companion documents:

- [Coach-led final execution plan](GOLDMAN_SACHS_VP_FINAL_EXECUTION_PLAN_SEP_4_TO_9_2026.md)
- [Goldman evidence and solution vault](GOLDMAN_SACHS_VP_CODERPAD_SEP_9_2026_MASTER_PLAN.md)
- [Final spoken-answer rehearsal script](GOLDMAN_SACHS_VP_FINAL_REHEARSAL_SCRIPT.md)

---

## Round-1 Priority Gate

The scheduled first round is a one-hour CoderPad. Until it is complete:

1. DSA items **1-12** are Tier A.
2. DSA items **13-18** are Tier B and used only for a demonstrated weak family or transfer drill.
3. LLD items **19-25** and HLD items **26-30** are later-round insurance. Before the CoderPad, rehearse only item 19 plus one item selected from 26-28.
4. Do not attempt all 30 sequentially before Wednesday.
5. Progress is measured by cold reconstruction and mock performance, not checked boxes or repository size.

---

## How to Use This Sheet

```text
1. Start the timer.
2. Read only Problem + Pattern + Minimal Hint.
3. Clarify input, output, bounds, and one ambiguity.
4. State a correct baseline.
5. State the optimized invariant or core design in one sentence.
6. Code or whiteboard from scratch.
7. Test normal, boundary, and adversarial cases.
8. Explain complexity, failure behavior, and one trade-off.
9. Check Definition of Done.
10. Fill only Missed / Notes.
```

### Missed / Notes Codes

```text
P   = pattern-recognition miss
DS  = wrong data structure
I   = invariant/correctness miss
J   = Java/API/syntax miss
E   = edge-case miss
T   = exceeded time
X   = complexity/trade-off miss
V   = unclear or silent communication
API = weak API/class boundary
FR  = failure/recovery gap
HA  = high-availability gap
OBS = observability gap
OWN = ownership/claim boundary crossed
✓   = clean blind reconstruction
```

---

# DSA Revision

| # | Priority | Problem | LeetCode / Closest Analogue | Local DSA10days Solution | Pattern | Time Limit | Minimal Hint | Definition of Done | Missed / Notes |
|---:|:---:|---|---|---|---|---|---|---|---|
| 1 | **A** | **Highest Average Score / Highest-Frequency IP** | [1086. High Five](https://leetcode.com/problems/high-five/) *(closest score-aggregation analogue)* | `No exact local Java yet` | aggregation map + deterministic tie policy | **12m / 18m stop** | Store `(long sum, count)` or frequency per key; separate maximum tracking from output ordering. | Handles negative scores, repeated keys and ties; avoids unsafe floating equality; states empty/malformed-input policy; O(n + u log u) when tied keys are sorted. | |
| 2 | **A** | **String Compression** | [443. String Compression](https://leetcode.com/problems/string-compression/) | `No exact local Java yet` | two read/write pointers + run length | **12m / 18m stop** | Read one complete run, then write its character and decimal count. | In-place result and returned length correct; count 10+ emits multiple digits; handles empty, singleton, all-distinct and all-same; O(n). | |
| 3 | **A** | **Trapping Rain Water** | [42. Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) | [TrappingRainwater.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/TrappingRainwater.java) | two pointers + left/right maxima | **15m / 22m stop** | Finalize the side whose maximum boundary is currently lower. | Derives why the opposite side is sufficient; handles short/monotone/equal/deep-basin arrays; O(n), O(1); arithmetic policy stated. | |
| 4 | **A** | **Container With Most Water** | [11. Container With Most Water](https://leetcode.com/problems/container-with-most-water/) | [ContainerWithMostWater.java](../../src/main/java/org/chijai/day1/Arrays/session2/ContainerWithMostWater.java) | two pointers + limiting boundary | **10m / 15m stop** | Move only the shorter wall. | Proves why moving the taller wall cannot improve the current limiting height; correct width; handles ties and short input; O(n), O(1). | |
| 5 | **A** | **Minimum Window / At Most K Distinct** | [76. Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/)<br>[340. Longest Substring with At Most K Distinct Characters](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/) | [MinimumWindowSubstring.java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java)<br>[LongestSubstringVariations.java](../../src/main/java/org/chijai/day3/session1/LongestSubstringVariations.java) | variable sliding window + counts | **15m / 22m stop** | Expand to satisfy; shrink only while the exact validity predicate remains true. | Duplicate requirements and impossible case work; left never moves backward; validity counter meaning is explicit; O(n). | |
| 6 | **A** | **Koko / First-True Binary Search** | [875. Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/) | [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | binary search on a monotonic answer | **15m / 22m stop** | Define `feasible(x)` first; search for the smallest true value. | Bounds are justified; ceiling division and accumulated work are overflow-safe; returns minimal feasible answer; states monotonic proof. | |
| 7 | **A** | **Number of Islands** | [200. Number of Islands](https://leetcode.com/problems/number-of-islands/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | DFS/BFS connected components | **12m / 18m stop** | Every unseen land cell starts exactly one traversal. | Marks on entry/enqueue; no double count; four-direction assumption clarified; all-water/all-land/rectangular cases work; O(rows x cols). | |
| 8 | **A** | **01 Matrix / Multi-Source Grid Distance** | [542. 01 Matrix](https://leetcode.com/problems/01-matrix/) | [Matrix01.java](../../src/main/java/org/chijai/day8/graph/session1/Matrix01.java) | multi-source BFS | **15m / 22m stop** | Put every zero in the initial queue; first arrival is shortest. | Initializes unknown distances correctly; each cell enqueued once; handles all-zero, one-zero and rectangular grids; O(rows x cols). | |
| 9 | **A** | **Validate Binary Search Tree** | [98. Validate Binary Search Tree](https://leetcode.com/problems/validate-binary-search-tree/) | [ValidateBST.java](../../src/main/java/org/chijai/day6/trees/session3/ValidateBST.java) | DFS with inherited bounds | **10m / 15m stop** | A node must satisfy every ancestor constraint, not only its parent. | Deep invalid descendant fails; duplicates policy stated; integer extremes safe through `long` bounds; O(n), O(height). | |
| 10 | **A** | **Word Break / Coin Change DP State** | [139. Word Break](https://leetcode.com/problems/word-break/)<br>[322. Coin Change](https://leetcode.com/problems/coin-change/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) | one-dimensional DP | **15m / 22m stop** | Define exactly what `dp[i]` means before writing a transition. | Base case, unreachable state and iteration order are correct; tests reuse/impossible suffix/amount zero; explains time and space. | |
| 11 | **A** | **Gas Station** | [134. Gas Station](https://leetcode.com/problems/gas-station/) | [GasStation.java](../../src/main/java/org/chijai/day9/dp/session1/GasStation.java) | greedy prefix reset | **12m / 18m stop** | If running balance becomes negative, no start inside that failed segment can work. | Checks total feasibility; resets start correctly; explains proof rather than only code; handles impossible, start zero and wraparound; O(n), O(1). | |
| 12 | **A** | **Task Scheduler / Frequency Bound** | [621. Task Scheduler](https://leetcode.com/problems/task-scheduler/) | [TaskScheduler.java](../../src/main/java/org/chijai/day7/session1/heap/TaskScheduler.java) | frequency counting + greedy lower bound | **12m / 18m stop** | The maximum frequency creates frames; actual task count covers the zero-idle case. | Formula/simulation is justified; cooldown zero, tied maxima, many distinct and dominant-task cases work; O(n). | |
| 13 | **B** | **Meeting Rooms II** | [253. Meeting Rooms II](https://leetcode.com/problems/meeting-rooms-ii/) | [IntervalActiveMinHeap.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalActiveMinHeap.java) | sort + min-heap of active end times | **12m / 18m stop** | Before allocating a room, release every meeting that has ended. | Boundary convention such as `[start,end)` stated; nested/touching intervals work; returns maximum simultaneous rooms; O(n log n). | |
| 14 | **B** | **Top K Frequent Elements** | [347. Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/) | [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) | frequency map + size-k heap | **12m / 18m stop** | Count first; retain only the k best candidates. | Exactly k results; heap direction correct; duplicates/ties policy discussed; O(n log k), O(n). | |
| 15 | **B** | **LRU Cache** | [146. LRU Cache](https://leetcode.com/problems/lru-cache/) | [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) | hash map + doubly linked list | **18m / 25m stop** | Map gives lookup; list gives recency mutation and eviction. | `get/put` O(1); update moves to MRU; true LRU evicted; capacity one and duplicate update work; sentinel/link invariants explained. | |
| 16 | **B** | **Fraction to Recurring Decimal** | [166. Fraction to Recurring Decimal](https://leetcode.com/problems/fraction-to-recurring-decimal/) | `No exact local Java yet` | remainder-position map | **18m / 25m stop** | A repeated remainder means the digits from its first output position repeat. | Sign and `Integer.MIN_VALUE` handled with `long`; zero/exact/repeating cases work; parentheses inserted at the stored index. | |
| 17 | **B** | **First Unique Character / Streaming Non-Repeating** | [387. First Unique Character in a String](https://leetcode.com/problems/first-unique-character-in-a-string/) | [FirstNonRepeatingCharacterUsingStreams.java](../../src/main/java/org/chijai/java/FirstNonRepeatingCharacterUsingStreams.java) | count + original-order scan; queue for streaming | **10m / 15m stop** | Frequency answers uniqueness; original order or queue answers “first.” | Distinguishes batch from streaming contract; all-repeated, singleton and late invalidation work; complexity stated. | |
| 18 | **B** | **Random Pick with Weight** | [528. Random Pick with Weight](https://leetcode.com/problems/random-pick-with-weight/) | `No exact local Java yet` | prefix sums + first-prefix binary search | **15m / 22m stop** | Each index owns a prefix interval whose length equals its weight. | Uses `long` total; draws from the correct random range; binary-searches first prefix meeting target; avoids modulo bias; constructor O(n), pick O(log n). | |

---

## DSA Pattern Time Budget

| Pattern | Revision Items | Recognition Target | Normal Coding Target |
|---|---|---:|---:|
| Map aggregation | 1, 17 | **<=20 sec** | 8-12 min |
| Read/write pointers | 2 | **<=20 sec** | 10-12 min |
| Two pointers | 3, 4 | **<=30 sec** | 10-15 min |
| Sliding window | 5 | **<=30 sec** | 15 min |
| Binary search on answer | 6, 18 | **<=30 sec** | 15 min |
| Grid BFS/DFS | 7, 8 | **<=20 sec** | 12-15 min |
| Tree bounds | 9 | **<=20 sec** | 10 min |
| Dynamic programming | 10 | **<=45 sec** | 15 min |
| Greedy proof | 11, 12 | **<=30 sec** | 12 min |
| Intervals/heap | 13, 14 | **<=30 sec** | 12 min |
| Compound data structure | 15 | **<=45 sec** | 18 min |
| Remainder-cycle mapping | 16 | **<=45 sec** | 18 min |

---

# LLD Revision

> **LLD rule:** spend the first 2-3 minutes clarifying requirements and invariants. The local project is an interview learning source, not proof that its entire design was deployed at Nasdaq.

| # | Design | LeetCode / Analogue | Local Project | Core Pattern | Time Limit | Minimal Hint | Definition of Done | Missed / Notes |
|---:|---|---|---|---|---|---|---|---|
| 19 | **Pre-Trade Risk Engine** | `N/A - custom risk LLD` | [pre-trade-risk-engine](../../../../LLDProjects/pre-trade-risk-engine/README.md) | rule Strategy/pipeline + explainable result | **30m / 40m stop** | Separate orchestration from each risk rule; validate before state mutation. | Quantity/notional rules, integer ticks, overflow safety and explainable rejection work; exposure, reservation, price bands and kill switch are scoped extensions; state/concurrency boundary is explicit. | |
| 20 | **Token Bucket Rate Limiter** | [359. Logger Rate Limiter](https://leetcode.com/problems/logger-rate-limiter/) *(closest analogue)* | [token-bucket-rate-limiter](../../../../LLDProjects/token-bucket-rate-limiter/README.md) | lazy refill + per-key atomic acquire | **25m / 35m stop** | Refill only when a request arrives; inject time. | Capacity/refill formula, burst/rejection/refill and independent keys work; synchronization scope and multi-instance limitation explained. | |
| 21 | **LRU Cache LLD** | [146. LRU Cache](https://leetcode.com/problems/lru-cache/) | [lru-cache](../../../../LLDProjects/lru-cache/README.md) | map + sentinel DLL + policy boundary | **20m / 30m stop** | First make single-threaded O(1) invariants correct; discuss synchronization afterward. | Clean API, node helpers, update/evict/capacity behavior, complexity and thread-safety option are defensible without a class zoo. | |
| 22 | **Order Management System** | `N/A - custom OMS LLD` | [order-management-system](../../../../LLDProjects/order-management-system/README.md) | lifecycle state machine + dual IDs + audit | **30m / 40m stop** | Every state change passes through a validated transition. | ACK, partial fill, fill, cancel/replace and reject modeled; terminal-state race, duplicate execution, immutable snapshot and event boundary explained. | |
| 23 | **Order Book** | [1801. Number of Orders in the Backlog](https://leetcode.com/problems/number-of-orders-in-the-backlog/) *(closest coding analogue)* | [DesignOrderBook](../../../../LLDProjects/DesignOrderBook/README.md) | ordered price levels + FIFO + active ID index | **30m / 40m stop** | Separate price priority, FIFO within a level, and direct cancellation lookup. | Add/cancel/replace/partial fill/BBO preserve price-time; hot-operation complexity and O(1)-cancel production improvement explained. | |
| 24 | **Matching Engine** | [1801. Number of Orders in the Backlog](https://leetcode.com/problems/number-of-orders-in-the-backlog/) *(closest coding analogue)* | [matching-engine](../../../../LLDProjects/matching-engine/README.md) | crossing loop + price-time execution | **30m / 40m stop** | Cross -> execute minimum remaining quantity -> update -> repeat -> rest valid remainder. | BUY/SELL, limit/market assumptions, maker price, multi-level partial fills and non-resting market remainder are correct; order-book ownership is clear. | |
| 25 | **FIX Gateway** | `N/A - protocol/gateway LLD` | [fix-gateway](../../../../LLDProjects/fix-gateway/README.md) | wire-to-domain adapter + risk/router orchestration | **35m / 45m stop** | Keep parsing, session state, risk, routing and business lifecycle in separate owners. | New-order mapping, validation/risk rejection, routing and execution report are modeled; sequence/reconnect depth is assigned to the session component; uncertain outcome is discussed. | |

## LLD Definition-of-Done Checklist

```text
[ ] Requirements and exclusions clarified
[ ] Core correctness invariant stated
[ ] Main classes each have one clear responsibility
[ ] Public APIs and key value objects visible
[ ] Happy-path sequence explained
[ ] Invalid transition/failure path covered
[ ] Data structures justified
[ ] Hot-operation complexity stated
[ ] Thread/state ownership addressed
[ ] Persistence/audit boundary addressed where relevant
[ ] Extension points discussed without implementing everything
[ ] Personal production ownership kept separate from learning project
```

---

# HLD Revision

> **HLD rule:** establish functional requirements, scale, latency/SLO, consistency, state ownership and failure model before drawing components.

| # | Design | LeetCode | Local Project | Core Pattern | Time Limit | Minimal Hint | Definition of Done | Missed / Notes |
|---:|---|---|---|---|---|---|---|---|
| 26 | **Portfolio Risk Calculation Platform** | `N/A - system design` | [mini-risk-management-platform](../../../../SystemDesignProjects/mini-risk-management-platform/README.md) | event ingestion + versioned state + calculation workers + query plane | **40m / 50m stop** | Separate source-of-truth positions/market data from derived risk results; every result needs input/model version. | Portfolios, positions, market data, scenarios and requested metrics clarified; partitioning, recalculation triggers, idempotency, stale-data policy, result lineage, replay, scaling, SLOs and audit are covered. | |
| 27 | **Low-Latency Pre-Trade Risk Platform** | `N/A - system design` | [trading-risk-platform](../../../../SystemDesignProjects/trading-risk-platform/README.md) | synchronous in-memory data plane + asynchronous control plane | **35m / 45m stop** | Keep database/config distribution off the order hot path; identify one state owner. | Risk request flow, atomic reservation, limit/version distribution, fail-open/closed decision, snapshot/replay, HA, backpressure, observability and RTO/RPO are defensible. | |
| 28 | **High-Performance Java Calculation Engine** | `N/A - performance/system design` | [java-concurrency-lab](../../../../SystemDesignProjects/java-concurrency-lab/README.md) | bounded concurrency + per-key atomic invariant + backpressure | **35m / 45m stop** | Separate independent calculations from the short atomic business transition. | Executor/queue budget, lock granularity, immutable publication, contention, overload, timeouts, metrics and measurement plan are explained; no “more threads is faster” claim. | |
| 29 | **Market Data Platform** | `N/A - system design` | [market-data-platform](../../../../SystemDesignProjects/market-data-platform/README.md) | ingest -> sequence -> normalize -> book/state -> fan-out | **40m / 50m stop** | Gap recovery and slow-consumer handling matter as much as throughput. | Feed/session boundary, sequencing, duplicates/gaps, snapshot/replay, partitioning, order-book state, fan-out, backpressure, HA and observability are covered. | |
| 30 | **End-to-End Electronic Trading Platform** | `N/A - system design` | [electronic-trading-platform](../../../../SystemDesignProjects/electronic-trading-platform/README.md) | gateway -> risk -> OMS -> exchange plus market-data feedback | **45m / 60m stop** | Draw the order path and market-data path separately, then connect executions to state/risk. | Identity, sync/async boundaries, pre-trade risk, OMS, connectivity, executions/positions, recovery/idempotency, security, observability, HA and failure scenarios are defensible. | |

## HLD Definition-of-Done Checklist

```text
[ ] Functional requirements and exclusions
[ ] Non-functional requirements / SLO
[ ] Rough throughput, data size and latency assumptions
[ ] Critical path identified
[ ] Components and ownership boundaries
[ ] Data model, IDs, version and partition key
[ ] Synchronous vs asynchronous boundaries
[ ] State source of truth and derived-data policy
[ ] Consistency and stale-data policy
[ ] Timeouts, retries, idempotency and backpressure
[ ] Dependency failure behavior
[ ] HA, failover, recovery and replay
[ ] RTO / RPO where relevant
[ ] Logs, metrics, traces and alerts
[ ] Security, authorization and audit
[ ] Bottlenecks and one deep dive
[ ] Design statements separated from personal production claims
```

---

# Goldman VP One-Glance Recall Map

```text
CODERPAD
------------------------------------------------
clarify contract and constraints
-> correct baseline
-> repeated work / bottleneck
-> invariant
-> simple Java
-> normal + boundary + adversarial tests
-> time + auxiliary space
-> one trade-off / mutation

PATTERNS
------------------------------------------------
aggregate by key        -> HashMap<key, sum/count>
shortest unweighted     -> BFS
connected components    -> DFS/BFS/DSU
smallest feasible       -> first-true binary search
valid contiguous range  -> sliding window
bounded best K          -> heap
ordered intervals       -> sort + sweep/heap
global BST validity     -> ancestor bounds
prefix constructible    -> DP state
compound O(1) cache     -> map + DLL

RISK / PERFORMANCE
------------------------------------------------
contract                -> correctness + determinism + latency + recovery
hot path                -> bounded work; no remote DB dependency
state transition        -> check and reserve atomically
configuration           -> immutable, versioned, observable
overload                -> bounded queues + backpressure
recovery                -> snapshot + ordered replay + reconciliation
proof                    -> tests + metrics + failure drills

VP SIGNAL
------------------------------------------------
I did                   -> personal contribution
we did                  -> team outcome + my contribution
I would                 -> hypothetical design
unknown                 -> say boundary; reason from principles
hint                    -> connect to invariant; continue
bug                     -> expected vs actual; first divergence; one fix
```

---

# Exact CoderPad Communication Card

```text
OPEN
“I’ll restate the contract, clarify the important bounds, give a correct
baseline, and then optimize deliberately.”

OPTIMIZE
“The baseline is O(__). The repeated work is __. I can remove it with __.
The invariant I need to preserve is __.”

STUCK
“I have a correct O(__) baseline. Its bottleneck is __. I’m checking whether
__ can preserve the required state more efficiently.”

HINT
“That suggests __. Let me connect it to the invariant: __.”

FAILED TEST
“I expected __ and observed __. I’ll trace the first state divergence before
changing the algorithm.”

CLOSE
“Each element/state is processed __, so time is __. The auxiliary structures
hold at most __, so space is __.”
```

---

# Suggested Revision Cadence

```text
PASS 0 - BLIND DIAGNOSTIC
Run one scored 60-minute mock before selecting revision rows.

PASS 1 - RECOGNITION
Tier A only. Say:
trigger / baseline / invariant / first data structure / adversarial test.

PASS 2 - TIMED RECONSTRUCTION
DSA: code from blank.
LLD: requirements + diagram + critical code.
HLD: architecture + failure deep dive.

PASS 3 - FAILURE-ONLY REPAIR
Redo only rows marked:
P / DS / I / J / E / T / X / V / API / FR / HA / OBS / OWN.

PASS 4 - LAST DAY
Only unresolved rows plus two easy warm-ups. No new material.
```

## Final Success Standard

```text
DSA
-> pattern family recognized within 30-45 seconds
-> correct baseline and invariant spoken before code
-> one medium completed in about 25-30 minutes
-> two-problem mock managed within 60 minutes
-> three tests and correct complexity stated

LLD
-> responsibilities and invariant derived from requirements
-> critical path can be coded without an oversized framework
-> concurrency/state/persistence boundary is defensible

HLD
-> hot path, state owner and failure policy established early
-> consistency, overload, recovery, HA and observability explained
-> assumptions and trade-offs stated instead of buzzwords

VP
-> calm after a hint or bug
-> no silent coding
-> no inflated ownership or invented metric
-> resume story connects Java + pre-trade risk + production judgment
```

**Final rule:** use this sheet to retrieve and perform. Do not turn it into another reading syllabus.
