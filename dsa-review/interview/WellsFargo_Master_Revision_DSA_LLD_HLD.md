# Wells Fargo — Master Revision Sheet (DSA + LLD + HLD)

> **Purpose:** Timed, blind reconstruction for senior Java / electronic-trading interviews.
>
> **Scope:** 18 DSA + 7 LLD + 5 HLD = **30 revision items**.
>
> **LeetCode links:** Exact where available. For custom trading problems, the link is marked as a **closest analogue**. For pure design questions, `N/A` is intentional.
>
> **Rule:** Read only **Problem + Pattern + Hint** → solve/design from memory → compare with notes/code only after the hard stop.

---

## How to Use This Sheet

```text
1. Start timer.
2. Read only Problem + Pattern + Hint.
3. State the invariant / core design in one sentence.
4. Code or whiteboard from scratch.
5. Test / challenge the design.
6. Explain complexity + trade-offs.
7. Check Definition of Done.
8. Fill only the Missed / Notes column.
```

### Missed / Notes Codes

```text
P   = pattern recognition miss
DS  = wrong data structure
I   = invariant miss
C   = coding / syntax miss
E   = edge-case miss
T   = exceeded time
X   = complexity / trade-off explanation miss
API = weak API / class design
FR  = failure/recovery gap
HA  = high-availability gap
OBS = observability gap
✓   = clean blind reconstruction
```

---

# DSA Revision

| # | Problem | LeetCode / Closest Analogue | Local DSA10days Solution | Pattern | Time Limit | Minimal Hint | Definition of Done | Missed / Notes |
| ---: | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | **LRU Cache** | [146. LRU Cache](https://leetcode.com/problems/lru-cache/) | [LRU Cache](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) | `HashMap + doubly linked list` | **15m / 22m stop** | Map gives O(1) lookup; list gives O(1) recency move + eviction. | Implement `get/put` O(1); move accessed node to MRU; evict LRU; handle update/existing key/capacity=1; explain why map alone is insufficient. |  |
| 2 | **Binary Search** | [704. Binary Search](https://leetcode.com/problems/binary-search/) | [Binary Search](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) | binary search | **5m / 8m stop** | Maintain a search interval that still may contain the target. | Correct `left/right/mid`; no infinite loop; handles empty/1-element/not-found; O(log n), O(1); can state interval invariant. |  |
| 3 | **Missing Number** | [268. Missing Number](https://leetcode.com/problems/missing-number/) | [Missing Number](../../src/main/java/org/chijai/day10/session2/MissingNumber.java) | XOR or arithmetic invariant | **5m / 8m stop** | XOR cancels equal values; one value remains. | O(n), O(1); handles missing `0` and missing `n`; explain XOR cancellation or overflow-safe arithmetic alternative. |  |
| 4 | **Employee Max Salary via Streams** | `N/A — Java Streams` | [Employee Max Salary via Streams](../../src/main/java/org/chijai/java/EmployeeMaxSalary.java) | `stream().max(comparator)` | **5m / 8m stop** | `max(Comparator.comparing...)`. | Returns correct employee; handles empty list deliberately (`Optional` or specified behavior); can write comparator cleanly; no unnecessary sorting. |  |
| 5 | **Longest Substring Without Repeating Characters** | [3. Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | [Longest Substring Without Repeating Characters](../../src/main/java/org/chijai/day3/session1/LongestSubString.java) | sliding window + last-seen map | **12m / 18m stop** | Move `left` only forward using last seen index. | O(n); no backtracking of `left`; handles repeated adjacent chars/empty string; explains window invariant: all chars inside window are unique. |  |
| 6 | **3Sum** | [15. 3Sum](https://leetcode.com/problems/3sum/) | [3Sum](../../src/main/java/org/chijai/day1/Arrays/Arrays/session2/Three3Sum2Sum.java) | sort + two pointers | **15m / 22m stop** | Sort; fix one number; solve 2Sum on the suffix; skip duplicates. | Unique triplets only; duplicate handling correct; O(n²); can explain why sorting enables two pointers. |  |
| 7 | **Sliding Window Maximum** | [239. Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) | [Sliding Window Maximum](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java) | monotonic deque | **15m / 22m stop** | Deque stores indices whose values are decreasing; front is max. | O(n); removes expired front and dominated back; handles `k=1`; explains each index enters/leaves once. |  |
| 8 | **Reverse Linked List** | [206. Reverse Linked List](https://leetcode.com/problems/reverse-linked-list/) | [Reverse Linked List](../../src/main/java/org/chijai/day4/LinkedList/session1/ReverseLinkedList.java) | pointer reversal | **7m / 10m stop** | Save `next` before changing `current.next`. | Correct 3-pointer loop; handles null/1-node; O(n), O(1); can explain pointer invariant. |  |
| 9 | **Linked List Cycle** | [141. Linked List Cycle](https://leetcode.com/problems/linked-list-cycle/) | [Linked List Cycle](../../src/main/java/org/chijai/day4/LinkedList/session1/LinkedListCycle.java) | Floyd slow/fast pointers | **7m / 10m stop** | If a cycle exists, fast eventually laps slow. | O(n), O(1); null-safe loop condition; handles self-cycle; explains why meeting implies cycle. |  |
| 10 | **Merge Intervals** | [56. Merge Intervals](https://leetcode.com/problems/merge-intervals/) | [Merge Intervals](../../src/main/java/org/chijai/day1/Arrays/Arrays/session2/Intervals.java) | sort + linear merge | **10m / 15m stop** | Sort by start; compare next start with current merged end. | Correct overlap rule; handles nested/touching intervals per requirement; O(n log n); no unnecessary data structures. |  |
| 11 | **Top K Frequent Elements** | [347. Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/) | [Top K Frequent Elements](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) | frequency map + min-heap K | **12m / 18m stop** | Count first; keep only K best candidates in min-heap. | Correct top K; O(n log k); explains why min-heap is size K; handles ties consistently if asked. |  |
| 12 | **Binary Tree Level Order Traversal** | [102. Binary Tree Level Order Traversal](https://leetcode.com/problems/binary-tree-level-order-traversal/) | [Binary Tree Level Order Traversal](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeTraversal.java) | BFS queue | **10m / 15m stop** | `levelSize = queue.size()` before processing each level. | Correct level grouping; handles null root; O(n); can explain queue invariant. |  |
| 13 | **Lowest Common Ancestor** | [236. Lowest Common Ancestor of a Binary Tree](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/) | [Lowest Common Ancestor](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) | post-order recursion | **12m / 18m stop** | If left and right both return non-null, current node is LCA. | Correct base cases; handles ancestor-of-other case; O(n); can explain recursive return meaning. |  |
| 14 | **Number of Islands** | [200. Number of Islands](https://leetcode.com/problems/number-of-islands/) | [Number of Islands](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | DFS/BFS connected components | **12m / 18m stop** | Every unseen land cell starts exactly one traversal/island. | Marks visited; no double count; handles boundaries; O(rows×cols); can code DFS or BFS cleanly. |  |
| 15 | **Course Schedule** | [207. Course Schedule](https://leetcode.com/problems/course-schedule/) | [Course Schedule](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) | graph + topological sort / cycle detection | **15m / 22m stop** | Kahn: indegree 0 → queue → remove edges → count processed. | Correct graph direction; detects cycle; O(V+E); can explain why processed count < V means cycle. |  |
| 16 | **Sequence Gap Detector** | [163. Missing Ranges](https://leetcode.com/problems/missing-ranges/) *(closest analogue)* | [Sequence Gap Detector](../../src/main/java/org/chijai/java/SequenceGapDetector.java) | sequence invariant / ordered scan | **10m / 15m stop** | Compare incoming sequence with `nextExpected`: `<`, `=`, `>`. | Detect duplicate/stale, exact next, and missing range; explains ordered vs out-of-order assumption; O(n) for ordered input. |  |
| 17 | **Execution Deduplication** | [217. Contains Duplicate](https://leetcode.com/problems/contains-duplicate/) *(closest analogue)* | [Execution Deduplication](../../src/main/java/org/chijai/java/ExecutionDeduplication.java) | idempotency + `HashSet` | **7m / 10m stop** | Mutate state only if `seenExecutionIds.add(id)` succeeds. | Duplicate event cannot update position/state twice; expected O(1); explains bounded-memory/durable dedup only as follow-up. |  |
| 18 | **Price-Time Priority** | [1801. Number of Orders in the Backlog](https://leetcode.com/problems/number-of-orders-in-the-backlog/) *(closest analogue)* | [Price-Time Priority](../../src/main/java/org/chijai/java/PriceTimePriority.java) | `PriorityQueue` comparator | **12m / 18m stop** | BUY: price ↓ then time ↑. SELL: price ↑ then time ↑. | Correct comparator for both sides; deterministic tie-break if needed; `peek()` returns correct next order; explains price-time invariant. |  |

---

## DSA Pattern Time Budget

| Pattern | Problems | Recognition Target | Normal Coding Target |
|---|---|---:|---:|
| Hash / membership | Missing Number, Execution Dedup | **≤20 sec** | 5–7 min |
| Binary search | Binary Search | **≤10 sec** | 5 min |
| Java Streams | Employee Max Salary | **≤10 sec** | 5 min |
| Sliding window | Longest Substring | **≤30 sec** | 10–12 min |
| Sort + two pointers | 3Sum | **≤30 sec** | 15 min |
| Monotonic deque | Sliding Window Maximum | **≤45 sec** | 15 min |
| Linked-list pointers | Reverse List, Cycle | **≤20 sec** | 7 min |
| Sort + merge | Merge Intervals | **≤20 sec** | 10 min |
| Top-K heap | Top K Frequent | **≤30 sec** | 12 min |
| BFS / DFS | Level Order, Islands | **≤20 sec** | 10–12 min |
| Tree recursion | LCA | **≤30 sec** | 12 min |
| Graph / topo | Course Schedule | **≤30 sec** | 15 min |
| Sequence correctness | Gap Detector | **≤30 sec** | 10 min |
| Price-time ordering | Price-Time Priority | **≤30 sec** | 12 min |
| O(1) cache design | LRU Cache | **≤45 sec** | 15 min |

---

# LLD Revision

> **LLD time rule:** spend the first 2–3 minutes clarifying requirements and invariants. Do not start drawing classes immediately.

| # | Design | LeetCode / Analogue | Core Pattern | Time Limit | Minimal Hint | Definition of Done | Missed / Notes |
|---:|---|---|---|---|---|---|---|
| 19 | **Order Book** | [1801. Number of Orders in the Backlog](https://leetcode.com/problems/number-of-orders-in-the-backlog/) *(closest coding analogue)* | `TreeMap<price, PriceLevel>` + FIFO + `orderId` index | **30m / 40m stop** | Separate **price priority**, **FIFO within price**, and **ID lookup**. | Requirements clarified; classes/APIs for `Order`, `PriceLevel`, `OrderBook`; add/cancel/replace/partial fill/BBO work; price-time preserved; complexity stated; explain O(1) cancel production improvement. | |
| 20 | **Matching Engine** | [1801. Number of Orders in the Backlog](https://leetcode.com/problems/number-of-orders-in-the-backlog/) *(closest coding analogue)* | best-opposite loop + partial fills | **30m / 40m stop** | `cross → min(remQty) → fill → update → repeat → rest remainder`. | Handles BUY/SELL, limit/market assumptions, passive execution price, partial fills, time priority retention, IOC/FOK discussion, SMP follow-up; clear ownership of book mutation. | |
| 21 | **Pre-Trade Risk Engine** | `N/A — custom trading LLD` | Strategy/Chain of Responsibility + transactional state | **30m / 40m stop** | Separate **risk orchestration** from individual `RiskCheck`s. | Pluggable checks for qty/notional/exposure/deviation/kill switch; clear `PASS/REJECT`; provisional state commit/rollback; no double mutation; thread/concurrency assumptions stated. | |
| 22 | **Order Management System** | `N/A — custom OMS LLD` | state machine + order repository | **25m / 35m stop** | `orderId → Order`; all lifecycle changes go through validated transitions. | States/events defined; ACK/fill/partial/cancel/replace/reject handled; invalid transitions rejected; idempotency/duplicate execution discussed; persistence/event publication boundaries clear. | |
| 23 | **FIX Session Manager** | `N/A — protocol/session LLD` | session state machine + sequence numbers + resend | **35m / 45m stop** | Separate **session protocol state** from business order state. | Logon/logout/heartbeat/TestRequest; inbound/outbound seq; gap detection; ResendRequest; PossDup handling; reconnect recovery; persistent sequence store; concurrency/ownership stated. | |
| 24 | **Exchange Gateway** | `N/A — custom trading gateway LLD` | adapter + session + throttle + correlation | **30m / 40m stop** | Internal canonical order ↔ exchange-specific message; preserve correlation IDs. | Submit/cancel/replace APIs; protocol adapters; ACK/execution mapping; throttle; sequence/reconnect; uncertain outcome handling; dedup/idempotency; failure boundaries and metrics. | |
| 25 | **LRU Cache LLD** | [146. LRU Cache](https://leetcode.com/problems/lru-cache/) | map + doubly linked list + policy boundary | **20m / 30m stop** | First get O(1) single-threaded design right; then discuss extensibility/thread safety. | Clean cache API; node/list invariants; O(1) get/put/evict; capacity/update behavior; thread-safety strategy; optional eviction-policy abstraction only if requirement asks. | |

---

## LLD Definition-of-Done Checklist

Use this after **every** LLD:

```text
[ ] Requirements / assumptions clarified
[ ] Core invariant stated
[ ] Main classes have one clear responsibility
[ ] Public APIs are visible
[ ] Main happy-path sequence explained
[ ] Important failure / invalid path covered
[ ] Correct data structures chosen
[ ] Time complexity of hot operations stated
[ ] Concurrency / thread ownership addressed
[ ] Persistence boundary addressed if relevant
[ ] Extensibility discussed without over-engineering
[ ] Can code the critical 20–30% if interviewer asks
```

---

# HLD Revision

> **HLD time rule:** first establish requirements, scale, latency/SLA, consistency, failure model and hot path. Then draw components.

| # | Design | LeetCode | Core Pattern | Time Limit | Minimal Hint | Definition of Done | Missed / Notes |
|---:|---|---|---|---|---|---|---|
| 26 | **Pre-Trade Risk Platform** | `N/A — system design` | synchronous hot path + in-memory state + control plane | **35m / 45m stop** | Separate **data plane** (low latency) from **control plane** (limits/config). | Requirements/SLA; request flow; in-memory checks; limit distribution/versioning; no DB hot path; recovery/snapshot/replay; HA/failover; consistency model; observability; RTO/RPO; bottlenecks/trade-offs. | |
| 27 | **Exchange Connectivity Platform** | `N/A — system design` | session ownership + active/passive failover + sequence recovery | **35m / 45m stop** | One writer/session owner; failover must preserve sequence and order uncertainty semantics. | Multi-exchange adapters; FIX/OUCH sessions; sequence persistence/recovery; throttling; duplicate handling; active/standby; uncertain outcomes; replay; scaling partition key; metrics/alerts. | |
| 28 | **Market Data Platform** | `N/A — system design` | ingest → sequence → normalize → book build → fan-out | **40m / 50m stop** | Loss/gap recovery is as important as throughput. | Feed handlers; multicast/packet loss; sequence gaps; snapshot/recovery; normalization; order-book reconstruction; partitioning; fan-out; backpressure/slow consumers; replay; HA; observability. | |
| 29 | **End-to-End Electronic Trading Platform** | `N/A — system design` | client → gateway → risk → OMS → exchange + market data feedback | **45m / 60m stop** | Draw **order path** and **market-data path** separately, then connect them. | End-to-end components; sync vs async boundaries; order identity; risk hot path; OMS; gateway/exchange; executions/positions; market data; recovery/idempotency; HA; observability; security; RTO/RPO; failure scenarios. | |
| 30 | **Multi-Service Aggregator** | `N/A — system design` | concurrent fan-out + deadline + partial failure + idempotent persistence | **30m / 40m stop** | Overall deadline first; downstream calls run concurrently within remaining budget. | Parallel calls; per-call timeout; overall deadline; retry policy; required vs optional dependencies; partial response; bulkheads; persistence exactly-once/idempotency discussion; scaling; tracing/metrics. | |

---

## HLD Definition-of-Done Checklist

```text
[ ] Functional requirements
[ ] Non-functional requirements / SLA
[ ] Rough scale / throughput assumptions
[ ] Critical path identified
[ ] Core components + responsibilities
[ ] Data model / IDs / partition key
[ ] Sync vs async boundaries
[ ] Storage / cache / stream choices justified
[ ] Consistency model stated
[ ] Failure scenarios
[ ] Retry / timeout / idempotency
[ ] HA / failover
[ ] Recovery / replay
[ ] RTO / RPO where relevant
[ ] Backpressure / overload behavior
[ ] Security / authorization where relevant
[ ] Logs + metrics + traces + alerts
[ ] Bottlenecks / trade-offs
[ ] One deep-dive ready
```

---

# One-Glance Wells Fargo Recall Map

```text
DSA
────────────────────────────────────
lookup / dedup        → HashSet / HashMap
ordered key           → TreeMap
next best             → PriorityQueue
window expires FIFO   → Deque
window max            → Monotonic Deque
sorted pair search    → Two Pointers
graph dependency      → Topological Sort
tree level            → BFS
tree ancestry         → DFS recursion
linked list cycle     → Slow/Fast
LRU                    → Map + DLL

TRADING
────────────────────────────────────
BUY = BID = HIGH
SELL = ASK = LOW

price-time
→ better price
→ then earlier time/sequence

matching
→ best opposite
→ crosses?
→ min quantity
→ passive price
→ repeat
→ rest remainder

sequence
< expected → duplicate/stale
= expected → process
> expected → gap/buffer/recover

LLD
────────────────────────────────────
requirements
→ invariant
→ classes
→ APIs
→ data structures
→ flow
→ invalid/failure path
→ complexity
→ concurrency
→ extension

HLD
────────────────────────────────────
requirements + SLA
→ scale
→ critical path
→ components
→ partition/state/storage
→ consistency
→ failure/recovery
→ HA
→ observability
→ trade-offs
```

---

# Suggested Revision Cadence

```text
PASS 1 — Recognition
30 items, only say:
pattern / invariant / first data structure / first component

PASS 2 — Timed Reconstruction
DSA: code
LLD: whiteboard + critical code
HLD: architecture + deep dive

PASS 3 — Failure-Only Review
Redo only rows marked:
P / DS / I / C / E / T / API / FR / HA / OBS

PASS 4 — Last-Day
Only rows without ✓
```

## Final Success Standard

```text
DSA
→ recognize ≤30–45 sec
→ code within target
→ explain invariant + complexity

LLD
→ derive classes/data structures from requirements
→ no memorized class zoo
→ code critical path confidently

HLD
→ establish hot path + failure model early
→ explain trade-offs, recovery and HA
→ not just draw boxes
```
