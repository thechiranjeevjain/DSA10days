# Goldman Sachs - Master Revision Sheet (DSA + LLD + HLD)

> **Purpose:** Timed, blind reconstruction for the Goldman Sachs VP Software Engineering process, with the immediate 60-minute CoderPad treated as the first gate.
>
> **Scope:** 18 DSA + 7 LLD + 5 HLD = **30 revision items**.
>
> **LeetCode links:** Exact where available. Custom Goldman/trading questions use a clearly labelled closest analogue or `N/A`.
>
> **Local links:** DSA links point to portable repository-relative Java files. LLD/HLD links point to the focused local project README.
>
> **Rule:** Read only **Problem + Pattern** -> speak the **Script** -> reconstruct from memory -> compare with local material only after the hard stop.

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
2. Read only Problem + Pattern; speak the Script before coding or drawing.
3. Clarify input, output, bounds, and one ambiguity.
4. State a correct baseline.
5. State the optimized invariant or core design in one sentence.
6. Code or whiteboard from scratch.
7. Test normal, boundary, and adversarial cases.
8. Explain complexity, failure behavior, and one trade-off.
9. Check Definition of Done.
10. Record any miss in the review system after the attempt.
```

### Self-Audit Codes

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

| # | Priority | Problem | LeetCode / Closest Analogue | Local DSA10days Solution | Pattern | Time Limit | Script |
|---:|:---:|---|---|---|---|---|---|
| 1 | **A** | **Highest Average Score / Highest-Frequency IP** | [1086. High Five](https://leetcode.com/problems/high-five/) *(closest score-aggregation analogue)* | [HighFiveV3.java - grouped top-score average](../../src/main/java/org/chijai/day7/session1/heap/HighFiveV3.java)<br>[FrequencyMapUsingStreams.java - frequency variant](../../src/main/java/org/chijai/java/FrequencyMapUsingStreams.java) | aggregation map + deterministic tie policy | **12m / 18m stop** | **DECIDE:** Aggregate `(long sum, count)` or frequency per key; keep winner selection separate from output ordering. **CODE-GUARD:** Define empty/malformed input and tie policy; avoid floating equality. Test negative scores, repeated keys, and ties. Defend O(n + u log u) when sorting tied keys. |
| 2 | **A** | **String Compression** | [443. String Compression](https://leetcode.com/problems/string-compression/) | [StringCompression.java](../../src/main/java/org/chijai/day3/session3/StringCompression.java) | two read/write pointers + run length | **12m / 18m stop** | **DECIDE:** Read one complete run, then write its character and decimal count. **CODE-GUARD:** Advance read past the run before writing; emit every digit for counts 10+. Test empty, singleton, all-distinct, and all-same. Defend O(n) time, O(1) extra space. |
| 3 | **A** | **Trapping Rain Water** | [42. Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) | [TrappingRainwater.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/TrappingRainwater.java) | two pointers + left/right maxima | **15m / 22m stop** | **DECIDE:** Finalize the side with the lower known maximum; the opposite boundary is already sufficient. **CODE-GUARD:** Update that side's maximum before adding trapped water. Test short, monotone, equal, and deep-basin arrays. Defend O(n) time, O(1) space. |
| 4 | **A** | **Container With Most Water** | [11. Container With Most Water](https://leetcode.com/problems/container-with-most-water/) | [ContainerWithMostWater.java](../../src/main/java/org/chijai/day1/Arrays/session2/ContainerWithMostWater.java) | two pointers + limiting boundary | **10m / 15m stop** | **DECIDE:** Area is width times the shorter wall; only moving that limiting wall can improve height. **CODE-GUARD:** Compute area before moving one pointer; handle equal heights consistently. Test two bars, ties, and best-at-boundary. Defend O(n) time, O(1) space. |
| 5 | **A** | **Minimum Window / At Most K Distinct** | [76. Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/)<br>[340. Longest Substring with At Most K Distinct Characters](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/) | [MinimumWindowSubstring.java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java)<br>[AtMostKDistinct.java](../../src/main/java/org/chijai/day3/session1/AtMostKDistinct.java) | variable sliding window + counts | **15m / 22m stop** | **DECIDE:** Expand to satisfy the exact count predicate; shrink only while it remains valid. **CODE-GUARD:** State whether `have` counts characters or satisfied requirements; save the answer before removal breaks validity. Test duplicate requirements, impossible input, and boundary windows. Defend linear pointer movement. |
| 6 | **A** | **Koko / First-True Binary Search** | [875. Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/) | [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | binary search on a monotonic answer | **15m / 22m stop** | **DECIDE:** Define `feasible(speed)` first; feasible speeds form a true suffix, so find its first value. **CODE-GUARD:** Search `[1,maxPile]`, use overflow-safe ceiling division, and preserve a working midpoint. Test one pile and both answer boundaries. Defend O(n log R). |
| 7 | **A** | **Number of Islands** | [200. Number of Islands](https://leetcode.com/problems/number-of-islands/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | DFS/BFS connected components | **12m / 18m stop** | **DECIDE:** Each unseen land cell starts exactly one component traversal. **CODE-GUARD:** Mark on entry/enqueue, then explore only valid four-direction land neighbors. Test all water, all land, disconnected cells, and rectangular grids. Defend O(rows x cols). |
| 8 | **A** | **01 Matrix / Multi-Source Grid Distance** | [542. 01 Matrix](https://leetcode.com/problems/01-matrix/) | [Matrix01.java](../../src/main/java/org/chijai/day8/graph/session1/Matrix01.java) | multi-source BFS | **15m / 22m stop** | **DECIDE:** Put every zero in the initial queue; equal-cost BFS makes first arrival the nearest distance. **CODE-GUARD:** Mark unknown cells distinctly and enqueue each once. Test all-zero, one-zero, and rectangular grids. Defend O(rows x cols) time and space. |
| 9 | **A** | **Validate Binary Search Tree** | [98. Validate Binary Search Tree](https://leetcode.com/problems/validate-binary-search-tree/) | [ValidateBST.java](../../src/main/java/org/chijai/day6/trees/session3/ValidateBST.java) | DFS with inherited bounds | **10m / 15m stop** | **DECIDE:** Every node must satisfy all ancestor bounds, not only its parent. **CODE-GUARD:** Pass strict `long` lower/upper bounds; reject equality unless the contract allows duplicates. Test a deep invalid descendant and integer extremes. Defend O(n), O(height). |
| 10 | **A** | **Word Break / Coin Change DP State** | [139. Word Break](https://leetcode.com/problems/word-break/)<br>[322. Coin Change](https://leetcode.com/problems/coin-change/) | [WordBreak.java](../../src/main/java/org/chijai/day8/graph/session3/WordBreak.java)<br>[CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) | one-dimensional DP | **15m / 22m stop** | **DECIDE:** Define exactly what `dp[i]` proves before writing transitions. **CODE-GUARD:** Establish the empty-prefix/zero-amount base, preserve unreachable states, and choose iteration order from reuse rules. Test impossible suffix, amount zero, and repeated coin use. Defend state count times transitions. |
| 11 | **A** | **Gas Station** | [134. Gas Station](https://leetcode.com/problems/gas-station/) | [GasStation.java](../../src/main/java/org/chijai/day9/dp/session1/GasStation.java) | greedy prefix reset | **12m / 18m stop** | **DECIDE:** If balance fails at `i`, no start inside that attempted segment can reach `i`; restart at `i+1`. **CODE-GUARD:** Separately verify total gas covers total cost. Test impossible, start zero, and wraparound. Defend O(n), O(1) with the elimination proof. |
| 12 | **A** | **Task Scheduler / Frequency Bound** | [621. Task Scheduler](https://leetcode.com/problems/task-scheduler/) | [TaskScheduler.java](../../src/main/java/org/chijai/day7/session1/heap/TaskScheduler.java) | frequency counting + greedy lower bound | **12m / 18m stop** | **DECIDE:** Maximum-frequency tasks define the frame lower bound; total tasks cover zero-idle cases. **CODE-GUARD:** Count how many tasks share the maximum and use `max(total, (max-1)*(n+1)+ties)`. Test cooldown zero, tied maxima, and one dominant task. |
| 13 | **B** | **Meeting Rooms II** | [253. Meeting Rooms II](https://leetcode.com/problems/meeting-rooms-ii/) | [IntervalActiveMinHeap.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalActiveMinHeap.java) | sort + min-heap of active end times | **12m / 18m stop** | **DECIDE:** Sort by start; the earliest active end decides whether a room is reusable. **CODE-GUARD:** Release every end `<= start` under half-open intervals, then push current end and track maximum heap size. Test touching, nested, and simultaneous meetings. Defend O(n log n). |
| 14 | **B** | **Top K Frequent Elements** | [347. Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/) | [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) | frequency map + size-k heap | **12m / 18m stop** | **DECIDE:** Count first; retain only the k highest-frequency keys in a size-k min-heap. **CODE-GUARD:** Compare frequency, not value, and evict only when size exceeds k. Test k=1, ties, and k equal to distinct count. Defend O(n log k), O(n). |
| 15 | **B** | **LRU Cache** | [146. LRU Cache](https://leetcode.com/problems/lru-cache/) | [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) | hash map + doubly linked list | **18m / 25m stop** | **DECIDE:** Map gives lookup; sentinel doubly linked list gives O(1) recency mutation and eviction. **CODE-GUARD:** Centralize detach/attach; updates move to MRU; eviction removes from both map and list. Test capacity one and duplicate updates. Defend O(1) expected operations. |
| 16 | **B** | **Fraction to Recurring Decimal** | [166. Fraction to Recurring Decimal](https://leetcode.com/problems/fraction-to-recurring-decimal/) | [FractionToRecurringDecimal.java](../../src/main/java/org/chijai/day10/session2/FractionToRecurringDecimal.java) | remainder-position map | **18m / 25m stop** | **DECIDE:** A repeated remainder proves the digits since its first output position repeat. **CODE-GUARD:** Convert operands to `long` before absolute value; store remainder position before generating its digit. Test zero, exact, repeating, negative, and `Integer.MIN_VALUE`. |
| 17 | **B** | **First Unique Character / Streaming Non-Repeating** | [387. First Unique Character in a String](https://leetcode.com/problems/first-unique-character-in-a-string/) | [FirstUniqueCharacterInAString.java - canonical LC 387](../../src/main/java/org/chijai/day1/Arrays/session1/FirstUniqueCharacterInAString.java)<br>[FirstNonRepeatingCharacterUsingStreams.java - Streams variation](../../src/main/java/org/chijai/java/FirstNonRepeatingCharacterUsingStreams.java) | count + original-order scan; queue for streaming | **10m / 15m stop** | **DECIDE:** Frequency answers uniqueness; original order or a queue answers first. **CODE-GUARD:** Distinguish batch from streaming before choosing two passes or count+queue. Test all repeated, singleton, and a late duplicate invalidating the leader. Defend O(n), O(sigma). |
| 18 | **B** | **Random Pick with Weight** | [528. Random Pick with Weight](https://leetcode.com/problems/random-pick-with-weight/) | [RandomPickWithWeight.java](../../src/main/java/org/chijai/day12/randomized/weightedsampling/RandomPickWithWeight.java) | prefix sums + first-prefix binary search | **15m / 22m stop** | **DECIDE:** Prefix interval length equals weight; sample one integer over total weight and locate its first covering prefix. **CODE-GUARD:** Use `long`, a correct random range, and lower-bound binary search. Test one weight, equal weights, and skewed weights. Defend build O(n), pick O(log n). |

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

| # | Design | LeetCode / Analogue | Local Project | Core Pattern | Time Limit | Script |
|---:|---|---|---|---|---|---|
| 19 | **Pre-Trade Risk Engine** | `N/A - custom risk LLD` | [pre-trade-risk-engine](../../../../LLDProjects/pre-trade-risk-engine/README.md) | rule Strategy/pipeline + explainable result | **30m / 40m stop** | **FRAME:** Separate orchestration from each risk rule; validate before mutating state. **GUARD:** Use integer ticks/long notional, explicit rejection reasons, and a clear state owner. **DEFEND:** Implement quantity/notional first; scope exposure, reservations, price bands, kill switch, and concurrency as extensions. |
| 20 | **Token Bucket Rate Limiter** | [359. Logger Rate Limiter](https://leetcode.com/problems/logger-rate-limiter/) *(closest analogue)* | [token-bucket-rate-limiter](../../../../LLDProjects/token-bucket-rate-limiter/README.md) | lazy refill + per-key atomic acquire | **25m / 35m stop** | **FRAME:** Inject time; lazily refill on request; atomically consume only when a token exists. **GUARD:** Clamp at capacity and avoid clock/precision errors. **DEFEND:** Test burst, rejection, refill, and independent keys; state the synchronization scope and multi-instance limitation. |
| 21 | **LRU Cache LLD** | [146. LRU Cache](https://leetcode.com/problems/lru-cache/) | [lru-cache](../../../../LLDProjects/lru-cache/README.md) | map + sentinel DLL + policy boundary | **20m / 30m stop** | **FRAME:** Map owns lookup; sentinel DLL owns recency and eviction. **GUARD:** Centralize detach/attach and update map/list together. **DEFEND:** Prove get/put O(1), test capacity one and overwrite, then discuss synchronization without adding a class zoo. |
| 22 | **Order Management System** | `N/A - custom OMS LLD` | [order-management-system](../../../../LLDProjects/order-management-system/README.md) | lifecycle state machine + dual IDs + audit | **30m / 40m stop** | **FRAME:** Every command enters one validated lifecycle transition keyed by stable client/exchange IDs. **GUARD:** Terminal states reject illegal late transitions; executions are idempotent and snapshots immutable. **DEFEND:** Walk ACK, partial fill, fill, cancel/replace, reject, and a cancel/fill race; identify event and audit boundaries. |
| 23 | **Order Book** | [1801. Number of Orders in the Backlog](https://leetcode.com/problems/number-of-orders-in-the-backlog/) *(closest coding analogue)* | [DesignOrderBook](../../../../LLDProjects/DesignOrderBook/README.md) | ordered price levels + FIFO + active ID index | **30m / 40m stop** | **FRAME:** Ordered price levels enforce price priority; FIFO queue enforces time; active-ID index enables cancellation. **GUARD:** Partial fills preserve queue position; replacements follow the stated priority rule. **DEFEND:** Walk add/cancel/replace/BBO and explain hot-operation complexity plus the O(1)-cancel production upgrade. |
| 24 | **Matching Engine** | [1801. Number of Orders in the Backlog](https://leetcode.com/problems/number-of-orders-in-the-backlog/) *(closest coding analogue)* | [matching-engine](../../../../LLDProjects/matching-engine/README.md) | crossing loop + price-time execution | **30m / 40m stop** | **FRAME:** While best prices cross, execute `min(incomingRemaining, restingRemaining)`, update both, remove fills, then rest only a valid limit remainder. **GUARD:** State maker-price and market-remainder policy. **DEFEND:** Walk multi-level partial fills and keep order-book versus matching ownership explicit. |
| 25 | **FIX Gateway** | `N/A - protocol/gateway LLD` | [fix-gateway](../../../../LLDProjects/fix-gateway/README.md) | wire-to-domain adapter + risk/router orchestration | **35m / 45m stop** | **FRAME:** Separate wire parsing, session state, domain validation/risk, routing, and lifecycle ownership. **GUARD:** Preserve IDs and map every reject/execution back to a valid FIX response. **DEFEND:** Walk a new order through routing and report; assign sequence/reconnect to the session component and discuss uncertain outcomes. |

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

| # | Design | LeetCode | Local Project | Core Pattern | Time Limit | Script |
|---:|---|---|---|---|---|---|
| 26 | **Portfolio Risk Calculation Platform** | `N/A - system design` | [mini-risk-management-platform](../../../../SystemDesignProjects/mini-risk-management-platform/README.md) | event ingestion + versioned state + calculation workers + query plane | **40m / 50m stop** | **FRAME:** Positions and market data are versioned source state; risk is derived, reproducible output carrying input/model versions. **GUARD:** Define stale-data and idempotent recalculation policy. **DEFEND:** Walk ingestion -> partitioned workers -> result store/query; cover triggers, lineage, replay, scaling, SLOs, and audit. |
| 27 | **Low-Latency Pre-Trade Risk Platform** | `N/A - system design` | [trading-risk-platform](../../../../SystemDesignProjects/trading-risk-platform/README.md) | synchronous in-memory data plane + asynchronous control plane | **35m / 45m stop** | **FRAME:** Keep database/config distribution off the order hot path; one owner atomically checks and reserves state. **GUARD:** Version limits and choose fail-open/closed explicitly. **DEFEND:** Walk request, reservation, rollback/commit, snapshot/replay, HA, overload/backpressure, observability, and RTO/RPO. |
| 28 | **High-Performance Java Calculation Engine** | `N/A - performance/system design` | [java-concurrency-lab](../../../../SystemDesignProjects/java-concurrency-lab/README.md) | bounded concurrency + per-key atomic invariant + backpressure | **35m / 45m stop** | **FRAME:** Parallelize independent calculations; keep the business state transition short and atomic. **GUARD:** Bound executor and queue, publish immutable results, and reject/backpressure overload. **DEFEND:** Explain lock granularity, contention, timeout/cancellation, metrics, and a benchmark plan; never claim more threads are automatically faster. |
| 29 | **Market Data Platform** | `N/A - system design` | [market-data-platform](../../../../SystemDesignProjects/market-data-platform/README.md) | ingest -> sequence -> normalize -> book/state -> fan-out | **40m / 50m stop** | **FRAME:** Ingest -> sequence/deduplicate -> normalize -> update book/state -> fan out. **GUARD:** A detected gap blocks false continuity and triggers snapshot/replay recovery; slow consumers cannot block the feed. **DEFEND:** Cover partitioning, backpressure, HA, observability, and recovery ownership. |
| 30 | **End-to-End Electronic Trading Platform** | `N/A - system design` | [electronic-trading-platform](../../../../SystemDesignProjects/electronic-trading-platform/README.md) | gateway -> risk -> OMS -> exchange plus market-data feedback | **45m / 60m stop** | **FRAME:** Draw order and market-data paths separately; connect executions to OMS, positions, and risk. **GUARD:** Preserve identity and idempotency across synchronous checks and asynchronous outcomes. **DEFEND:** Walk gateway -> risk -> OMS -> exchange -> execution, then recovery, security, observability, HA, and one failure scenario. |

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
