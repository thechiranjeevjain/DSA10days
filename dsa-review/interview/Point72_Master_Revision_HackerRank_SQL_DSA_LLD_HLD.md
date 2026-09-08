# Point72 — Treasury Technology Master Revision and Final Execution Plan

> **Interview:** Friday, 11 September 2026, 1:30–2:30 PM IST
> **Format supplied in invitation:** Zoom + HackerRank Interview
> **Format confirmed directly by the interview team:** Live coding
> **Confirmed coding language context:** They know the candidate's language is Java and that the candidate does not know C#
> **Role:** Software Engineer, Treasury Technology, Bengaluru
> **Research snapshot:** 7 September 2026
> **Preparation window:** Thursday as the main Point72 day; Friday morning for retrieval and activation only

## Security note

The private Zoom meeting URL, passcode, and HackerRank session URL are intentionally **not copied into this repository file**. Open them from the calendar invitation on interview day.

## The high-ROI conclusion

The direct confirmation removes most of the earlier format ambiguity. Prepare this as a **Java live-coding interview first**.

By Wednesday evening, the existing DSA sprint and Goldman workbook provide broad exposure. Thursday is therefore not another syllabus-completion day; it is a performance-conversion day:

```text
cold pattern recognition
→ derive invariant
→ code cleanly in Java without IDE dependence
→ test normal, boundary and adversarial cases
→ explain complexity
→ handle one follow-up or design mutation
```

Active preparation allocation:

```text
70%  Java DSA + two realistic live-coding simulations
20%  interview-sized LLD / data-structure design follow-ups
10%  SQL safety: joins, grouping, windows and one optimization explanation
```

The contingency order is now:

1. **Primary:** Java DSA/live coding.
2. **Secondary:** an LLD-style extension or a design-heavy coding problem.
3. **Safety only:** one SQL query or short SQL discussion, because HackerRank supports SQL and the JD emphasizes SQL Server.
4. **Small hedge:** resume/architecture follow-up if time remains.

Do not allocate active Thursday time to learning C#. The interview team already knows the language boundary.

## Evidence hierarchy and confidence

No public source can reveal the exact question that Krystian will ask. The correct use of online evidence is to rank preparation—not to manufacture certainty.

| Evidence | Public signal | Confidence | Preparation consequence |
|---|---|:---:|---|
| **Direct statement from interview team, supplied by candidate** | The round will be live coding; the team knows Java is the candidate's language and knows the candidate does not know C# | **Controlling evidence** | Java DSA is the gate; C# is removed from the active plan |
| [Official Point72 role](https://careers.point72.com/CSJobDetail?jobCode=IVS-0014939&jobName=software-engineer-treasury-technology&locale=English&location=Bengaluru%2C+India&retURL=%2FCSCareerSearch) | C#/.NET, financial domain, React, MS SQL Server, automated tests/TDD, patterns, distributed systems, microservices, observability, Docker, DevOps, production support and three time zones | **Highest for role, not format** | Use SQL/TDD/design as bounded safety and later-round coverage; do not let the broad JD displace confirmed live-coding preparation |
| [Recent Point72 Bengaluru candidate report](https://leetcode.com/discuss/post/7701688/point72-software-engineer-bangalore-r/) | Round was scheduled as Java LLD + DSA with HackerRank, but that interviewer used the hour for projects and architecture | **Relevant but overridden for format** | Keep only a small resume/design hedge; do not let one anonymous report override direct confirmation |
| [Point72 Software Engineer report: T-SQL, OOP and architecture](https://www.glassdoor.com/Interview/A-lot-of-t-SQL-related-questions-general-programming-OOP-architecture-questions-QTN_6359644.htm) | Heavy T-SQL plus general programming/OOP/architecture | **Medium source, low format match** | Justifies a bounded SQL hedge, not an SQL-first day |
| [Point72 report: SQL and past projects](https://www.glassdoor.com/Interview/Mostly-SQL-questions-and-questions-about-past-projects-QTN_7630742.htm) | Mostly SQL plus project questions | **Medium source, low format match** | Keep one query outline and one concise owned-project summary |
| [Point72 report: outer joins and optimization](https://www.glassdoor.com/Interview/SQLs-outer-joins-and-optimization-QTN_7782977.htm) | Outer joins and SQL optimization | **Medium source, low format match** | Use one outer-join/window query and one optimization framework for the safety block |
| [Recent Point72 Data Engineer discussion](https://www.reddit.com/r/csMajors/comments/1pk9i3l/point72_data_engineer_intern_interview/) | Simple HackerRank plus rapid fundamentals such as stack/heap, GC and database optimization | **Low for this role** | Keep a fundamentals lightning round, but do not let it displace role-specific work |
| [Aggregated Point72 Software Engineer guide](https://www.interviewquery.com/interview-guides/point72-software-engineer) | DSA, language-specific discussion and SQL examples such as employee salary ranking/duplicates | **Lower than direct reports** | Use as practice inspiration only, never call these guaranteed questions |

### Source register — what is reported, what is role-derived

| ID | Source | Observed signal | How much to trust it |
|---|---|---|---|
| **D1** | Direct interview-team statement supplied by candidate | One-hour live coding; Java background and lack of C# are already known | **Controlling for this round** |
| **I1** | [Krystian Rytel public LinkedIn search result](https://pl.linkedin.com/in/krystianrytel) | Point72 in Warsaw; searchable activity references hiring for Treasury Technology and Central Funding | **Strong team-context signal; zero evidence of an exact question** |
| **J1** | [Exact Bengaluru Treasury Technology role](https://careers.point72.com/CSJobDetail?jobCode=IVS-0014939&jobName=software-engineer-treasury-technology&locale=English&location=Bengaluru%2C+India&retURL=%2FCSCareerSearch) | Global trading, business requirements, C#/.NET, SQL Server, TDD, design/distributed systems, observability, Docker, DevOps and production support | **Authoritative role coverage** |
| **J2** | [Current Warsaw Treasury Technology role — real-time systems](https://careers.point72.com/CSJobDetail?jobCode=PIT-0015127&jobName=software-engineer-treasury-technology&locale=English&location=Warsaw%2C+PL&retURL=%2FCSCareerSearch) | Real-time treasury/financing systems; trades, financing and liquidity data; low latency/reliability; testing; incidents; mentoring across USA/Poland/India | **Authoritative same-organization/team context** |
| **J3** | [Current Warsaw Treasury Technology role — margin/platform modernization](https://careers.point72.com/CSJobDetail?jobCode=PIT-0014987&jobName=software-engineer-treasury-technology&locale=English&location=Warsaw%2C+PL&retURL=%2FCSCareerSearch) | Margin/financing platforms; large financial datasets; REST/pagination; modernization; testing; production debugging; code reviews and architecture | **Authoritative same-organization/team context** |
| **R1** | [Recent Bengaluru software-engineer report](https://leetcode.com/discuss/post/7701688/point72-software-engineer-bangalore-r/) | Scheduled Java LLD + DSA on HackerRank; actual hour pivoted to resume projects and architecture | **Recent and location-relevant, but anonymous and 2.1 YOE** |
| **R2** | [Point72 Software Engineer reports — 30 May 2026 entry](https://www.glassdoor.co.in/Interview/Point72-Software-Engineer-Interview-Questions-EI_IE1032703.0,7_KO8,25.htm) | Practical screen: call a paginated API, parse JSON, apply custom filters/aggregations; discuss web parsing at scale | **Recent exact title; different team unknown** |
| **R3** | [Point72 Software Engineer reports — 12 Jun 2024 entry](https://www.glassdoor.co.in/Interview/Point72-Software-Engineer-Interview-Questions-EI_IE1032703.0,7_KO8,25_IP2.htm) | Graph problem using breadth-first search | **Exact title and coding family; older/different location** |
| **R4** | [Point72 Software Engineer reports — 13 Feb 2023 entry](https://www.glassdoor.co.in/Interview/Point72-Software-Engineer-Interview-Questions-EI_IE1032703.0,7_KO8,25_IP3.htm) | Data structures/algorithms plus resume details | **Corroborating only** |
| **R5** | [Point72 Software Engineer reports — 28 Sep 2024 entry](https://www.glassdoor.co.in/Interview/Point72-Software-Engineer-Interview-Questions-EI_IE1032703.0,7_KO8,25_IP2.htm) | Mostly SQL and past projects | **Relevant secondary-round hedge; not this confirmed format** |
| **R6** | [Point72 Software Engineer reports — 6 Jun 2023 entry](https://www.glassdoor.co.in/Interview/Point72-Software-Engineer-Interview-Questions-EI_IE1032703.0,7_KO8,25_IP3.htm) | T-SQL, programming, OOP and architecture | **Older but aligned with JD** |
| **R7** | [Point72 Software Engineer reports — 20 Aug 2026 entry](https://www.glassdoor.co.in/Interview/Point72-Software-Engineer-Interview-Questions-EI_IE1032703.0,7_KO8,25.htm) | Describe a difficult project challenge and how it was solved | **Recent resume/behavioral signal** |
| **R8** | [2025 Point72 HFT-team report](https://www.1point3acres.com/interview/thread/1155304) | Search summary identifies SmartString and expandable-array design questions | **Low for Treasury; useful only as collection/OOP mutation practice** |
| **CV1** | Candidate resume supplied locally | Java/Spring Boot, low-latency in-memory pre-trade risk, matching-engine integration, production ownership, releases, mentoring, microservices, SQL, Docker/Kubernetes/EKS and message bus | **Controlling for claim-defense questions** |

### Claims discipline

```text
REPORTED = a candidate source states that question/family was asked.
ROLE-DERIVED = the official job or same Treasury organization makes the topic relevant.
CV-DERIVED = the resume makes the topic fair game at an 8-YOE bar.
COVERAGE = a representative drill selected to prevent a pattern blind spot.

No source can establish the exact question Krystian will choose.
The LinkedIn profile improves team/domain weighting; it does not justify personal speculation.
```

### Why the 30 workbook items earned a slot

| Workbook items | Selection basis | Evidence strength |
|---|---|---|
| **1–7, 9–17** | Representative easy/medium Java patterns for a confirmed live-coding round; chosen for horizontal transfer, not because the exact LeetCode number was reported | **D1 + R4 + COVERAGE** |
| **8** | Graph/BFS is the only clearly stated algorithm family in the accessible Point72 software-engineer reports | **R3 + D1** |
| **18** | Sequence-gap/deduplication directly tests resume claims around exchange/risk correctness and supports idempotency follow-ups | **CV1 + J2** |
| **19–25** | Small object/API designs reflect LLD scheduling, Treasury workflows, TDD, large-data REST/pagination and OOP expectations | **R1 + J1/J2/J3 + R6** |
| **26–30** | Later-round insurance for global Treasury, financing/liquidity/margin, production support, reliability and distributed ownership | **J1/J2/J3 + CV1** |

### Two explicitly reported practical drills outside the LeetCode list

1. **Graph BFS:** given a graph/grid and a start/target, compute reachability or shortest unweighted distance; state queue invariant, visited timing, O(V+E), disconnected/blocked/start-equals-target cases. **Basis: R3.**
2. **Paginated API aggregation:** repeatedly fetch pages, parse records, apply filters, aggregate deterministically and handle empty/partial/error pages; then discuss bounded concurrency, retry/idempotency, rate limits and memory. **Basis: R2 + J3.**

The second drill is particularly valuable for an experienced engineer because it can test coding, API judgment, data handling, failure behavior and scale in one prompt.

### Evidence-weighted probability bands

These are relative preparation priorities, not mathematical probabilities.

| Band | Topic | Why |
|---|---|---|
| **A — perform twice under time** | Java easy/medium DSA with follow-ups | Direct live-coding confirmation plus the already-disclosed Java background |
| **A — retrieve cold** | Hash map/string, sliding window, intervals, binary search, heap, graph and compound-data-structure anchors | Highest horizontal ROI from completed DSA work |
| **A — communicate** | Clarification, invariant, complexity and adversarial testing | Live evaluation observes the solving process, not only final code |
| **B — prepare one implementation** | LLD-style problem such as LRU, rate limiter, cash aggregator or state machine | Live coding may evolve into class/API/data-structure design |
| **B — short safety lane** | SQL joins, aggregation, windows and optimization | HackerRank can host SQL; official JD and reports emphasize SQL Server |
| **B — concise hedge** | Resume/project architecture | Senior role and one recent Bengaluru pivot report |
| **C — later-round insurance** | TDD, production support, Treasury HLD, distributed systems and observability | Strong JD relevance, but lower expected share of this confirmed coding hour |
| **DEFER** | C#/.NET syntax study | Team already knows Java/C# boundary; one day of C# harms Java coding readiness |
| **DEFER** | React and broad infrastructure study | Poor live-coding ROI for this round |
| **DEFER** | New hard DP, segment trees and obscure puzzles | Retrieval and execution beat last-minute breadth |

## Java-language operating rule

The JD asks for **8–12 years of C#/.NET experience**, but the interview team already knows that your production language is Java and that you do not know C#. There is no need to reopen the mismatch as an apology.

Use this answer if asked:

> “My deepest production experience is Java, particularly business-critical financial and pre-trade risk systems. I do not want to overstate production C# experience. The transferable part is strong: object-oriented design, concurrency, distributed services, SQL, testing, incident ownership and financial-domain correctness. I would expect to ramp deliberately on framework-specific C#/.NET depth while contributing from those foundations.”

At the start of the coding exercise, confirm rather than renegotiate:

> “As discussed, I’ll implement this in Java. Please let me know if you want language-agnostic pseudocode for any follow-up.”

Use Java throughout. If an unexpected C# request appears, calmly remind the interviewer that the language boundary was disclosed and ask to demonstrate the same design in Java. Do not burn Thursday on C# syntax.

## Definition of success for this one-day plan

By Friday at 12:45 PM, success means:

```text
[ ] Two 60-minute Java live-coding simulations completed
[ ] Eight Tier-A DSA anchors retrieved or coded from blank
[ ] One interview-sized LLD implementation completed in Java
[ ] One SQL query and one SQL-optimization answer rehearsed as safety coverage
[ ] One Nasdaq/PTR project summary rehearsed for a possible follow-up
[ ] Clarify → baseline → invariant → code → test → complexity spoken naturally
[ ] Java collections, comparators, overflow and edge-case syntax recalled
[ ] Private interview links tested from the calendar invitation
[ ] New learning stopped at least 40 minutes before the call
```

---

# Exact Thursday–Friday Execution Plan

## Wednesday, 9 September — transition only after Goldman

Do not turn Wednesday evening into a second interview marathon.

| Time | Task | Exact output |
|---|---|---|
| 4:00–5:00 PM | Decompress, food, short walk | No Point72 study |
| 5:00–5:20 PM | Capture Goldman lessons | Only reusable misses go to the recall ledger |
| 5:20–5:40 PM | Open this Point72 plan | Confirm Thursday start time, Java editor/HackerRank setup, and required materials |
| After 5:40 PM | Stop or very light reading | Protect sleep; no full mock and no new DSA |

## Thursday, 10 September — primary Point72 day

| Time | Minute-level task | SMART WIN |
|---|---|---|
| **08:00–08:20** | 08:00–08:05 open this file and blank Java editor; 08:05–08:10 write the live-coding communication sequence; 08:10–08:15 choose first mock; 08:15–08:20 state stop rules aloud | Java workspace ready; no resource hunting after 08:20 |
| **08:20–09:20** | **Live-coding Mock 1:** 5m clarify, 5m baseline/invariant, 35m code, 10m tests/follow-ups, 5m retrospective | One unfamiliar medium solved in Java under interview narration |
| **09:20–09:35** | Break, water, no phone rabbit hole | Back by 09:35 |
| **09:35–10:50** | **Tier-A retrieval circuit:** 09:35–09:50 hash map/string; 09:50–10:05 sliding window; 10:05–10:20 intervals; 10:20–10:35 binary search; 10:35–10:50 heap/deque | Five pattern triggers and invariants spoken; code only the two marked red |
| **10:50–11:05** | Break | Full reset |
| **11:05–12:05** | **Graph/tree/DP circuit:** 20m graph BFS/topological; 20m tree invariant; 20m one-dimensional DP state | Three cold skeletons or dry runs; no new problems |
| **12:05–01:00** | Lunch + walk | Zero study |
| **01:00–02:00** | **Live-coding Mock 2:** choose a different family; simulate screen-share and interviewer interruptions | One complete Java solution, tests, complexity and one changed constraint |
| **02:00–02:20** | Repair only the demonstrated miss | One corrected reconstruction; no second random problem |
| **02:20–02:35** | Break | Reset |
| **02:35–03:35** | **Java implementation safety:** collections APIs, comparator direction, `ArrayDeque`, `PriorityQueue`, map null/presence, `equals`, overflow and return contracts | Reconstruct six syntax fragments without IDE completion |
| **03:35–04:20** | **Interview-sized LLD in Java:** choose LRU, token bucket or cash aggregator; clarify 5m, model 10m, code core 25m, test 5m | Classes and APIs remain small; critical path compiles conceptually |
| **04:20–04:40** | Break + snack | Reset |
| **04:40–05:25** | **LLD follow-ups:** concurrency, state ownership, idempotency, testing and scaling limits of the implementation | Defend choices without expanding into a class zoo |
| **05:25–05:55** | **Resume hedge:** PTR five-minute architecture + one production follow-up | Concise and truthful; no full HLD study |
| **05:55–06:40** | Dinner/walk | No screen if possible |
| **06:40–07:25** | **SQL safety only:** one outer-join/window query + one slow-query optimization viva | One correct query; explain grain, nulls, ties and index hypothesis |
| **07:25–07:40** | Break | Reset |
| **07:40–08:40** | **Live-coding Mock 3 only if energy is good; otherwise replay Mock 1 failure** | Performance evidence, not problem count |
| **08:40–09:10** | Failure-only repair | Repair top three coding misses; add nothing else |
| **09:10–09:35** | Rehearse opening, Java confirmation, testing narration and questions to interviewer | Every script short enough to sound natural |
| **9:35 PM** | Hard stop | Prepare clothes/device; protect sleep |

## Friday, 11 September — activation, not expansion

| Time | Minute-level task | SMART WIN |
|---|---|---|
| **07:45–08:15** | Breakfast + setup | Calm start; no feeds/news |
| **08:15–09:00** | Java warm-up 1: easy hash map/string; 20m code + 10m tests + 15m explain | Clean code and calm narration; no novelty |
| **09:10–09:55** | Java warm-up 2: one previously solved medium from weakest Tier-A family | Correct invariant and completion within 35 minutes |
| **10:05–10:45** | Java API + edge-case retrieval | Comparator, deque, heap, maps, equality, overflow and empty input recalled |
| **10:55–11:25** | One LLD skeleton + tests | Public API, invariant and core operation from blank |
| **11:25–11:35** | SQL/resume safety verbal only | One query outline and one-minute PTR summary |
| **11:35–12:05** | Early lunch + water | No technical content |
| **12:05–12:25** | Technical check using invitation: Zoom, audio, camera, charger, backup internet, HackerRank access | All links work; private links remain outside repository |
| **12:25–12:45** | Review only unresolved Java-coding red items and opening script | At most three items; no SQL/C#/HLD expansion |
| **12:45–01:00** | Stop studying; breathe and walk | Cognitive reset |
| **01:00–01:20** | Join setup, blank paper, water; close notes | Ready before call |
| **01:30–02:30** | Interview | Clarify → structure → execute → test → summarize |

## Compressed Thursday fallback — if only six hours remain

Execute in this order and stop when time ends:

| Order | Time | Mandatory output |
|---:|---:|---|
| 1 | 60m | Java live-coding Mock 1 |
| 2 | 90m | Tier-A DSA retrieval + repair |
| 3 | 60m | Java live-coding Mock 2 |
| 4 | 60m | Interview-sized LLD implementation in Java |
| 5 | 45m | Java API/edge-case safety |
| 6 | 45m | SQL safety + resume hedge |

Do not sacrifice sleep to complete lower-ranked rows.

---

# Master Revision Workbook — 30 Items

## How to use every row

```text
1. Read only Question + Pattern + Minimal Hint.
2. State assumptions and result contract.
3. Attempt from blank.
4. Test normal, boundary and failure cases.
5. Explain complexity or operational trade-off.
6. Open source/notes only after the hard stop.
7. Record only the failure code.
```

Failure codes:

```text
P=pattern  DS=data structure  I=invariant  C=code/syntax  SQL=query
E=edge case  T=time  X=complexity/trade-off  V=verbal explanation
API=design boundary  TEST=testability  FR=failure/recovery
OBS=observability  OWN=ownership claim  MOCK=timed simulation  ✓=clean
```

## Round-1 Gate — Java Live Coding / DSA (Items 1–18)

The exact problems below are **coverage anchors**, not claims that Point72 will ask these exact LeetCode numbers. The target is transferable pattern retrieval, clean Java, testing and communication under observation.

| # | Priority | Question / Drill | Public or Practice Link | Local Source | Pattern | Time / Stop | Minimal Hint | Definition of Done | Missed |
|---:|:---:|---|---|---|---|---|---|---|---|
| 1 | **A** | **Group Anagrams / canonical grouping key** | [49. Group Anagrams](https://leetcode.com/problems/group-anagrams/) | [Closest local anagram source](../../src/main/java/org/chijai/day3/session3/ValidAnagram.java) | hash map + canonical signature | **12m / 18m** | Equivalent strings must produce exactly the same immutable key. | Chooses sorted or count-vector key; handles empty/repeated strings; no mutable-array key bug; states exact complexity. | |
| 2 | **A** | **Longest Substring Without Repeating Characters** | [3. Longest Substring](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | [Local Java](../../src/main/java/org/chijai/day3/session1/LongestSubString.java) | variable sliding window + last seen | **12m / 18m** | Move `left` only forward. | Unique-window invariant; repeat before current left and empty input tested; O(n); map/alphabet trade-off explained. | |
| 3 | **A** | **Merge Intervals / scheduling conflicts** | [56. Merge Intervals](https://leetcode.com/problems/merge-intervals/) | [Local Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalSortByStart.java) | sort + linear merge | **12m / 18m** | Normalize by start; compare next start to current merged end. | Boundary convention stated; nested/disjoint intervals work; O(n log n); can answer insert-interval follow-up. | |
| 4 | **A** | **Time-Based Key-Value Store** | [981. Time Based Key-Value Store](https://leetcode.com/problems/time-based-key-value-store/) | [Local Java](../../src/main/java/org/chijai/day2/session3/TimeBasedKeyValueStore.java) | per-key ordered history + binary search | **15m / 22m** | Find greatest timestamp less than or equal to query. | Missing/too-early query handled; timestamp-order assumption stated; O(log n) get; discusses out-of-order writes. | |
| 5 | **A** | **Sliding Window Maximum** | [239. Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) | [Local Java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java) | monotonic deque | **15m / 22m** | Store useful indices, not every value. | Expired indices removed first; decreasing-value invariant defended; duplicates and k=1 tested; O(n) proof stated. | |
| 6 | **A** | **Top K / streaming priority** | [347. Top K Frequent](https://leetcode.com/problems/top-k-frequent-elements/)<br>[295. Median from Data Stream](https://leetcode.com/problems/find-median-from-data-stream/) | [Top K Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java)<br>[Median Java](../../src/main/java/org/chijai/day7/session1/heap/Median.java) | bounded heap / two heaps | **18m / 25m** | State what each heap owns and why its root matters. | Reconstruct one; heap direction/balance correct; overflow-safe median; tests ties, odd/even and duplicates. | |
| 7 | **A** | **LRU Cache** | [146. LRU Cache](https://leetcode.com/problems/lru-cache/) | [Local Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) | map + doubly linked list | **18m / 25m** | Map finds; list mutates recency and evicts. | O(1) `get/put`; update moves to MRU; capacity one works; sentinel/link invariant explained; concurrency is a follow-up. | |
| 8 | **A** | **Number of Islands** | [200. Number of Islands](https://leetcode.com/problems/number-of-islands/) | [Local Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | grid DFS/BFS | **15m / 22m** | Each unvisited land cell starts exactly one component traversal. | Bounds/visited mutation clear; four-direction policy stated; empty/single-cell tested; O(rows x cols) proven. | |
| 9 | **A** | **Dependency cycle / Course Schedule** | [207. Course Schedule](https://leetcode.com/problems/course-schedule/) | [Local Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) | graph + topological sort | **15m / 22m** | Zero indegree means all prerequisites are satisfied. | Edge direction correct; processes each edge once; cycle proven by processed count; handles disconnected graph. | |
| 10 | **A** | **Koko / binary search on answer** | [875. Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/) | [Local Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | monotonic feasibility + binary search | **15m / 22m** | Define the smallest feasible answer and prove feasibility monotonicity. | Correct bounds and midpoint; ceiling division avoids overflow; one pile and tight deadline tested; O(n log range). | |
| 11 | **A** | **3Sum** | [15. 3Sum](https://leetcode.com/problems/3sum/) | [Local Java](../../src/main/java/org/chijai/day1/Arrays/session2/Three3Sum2Sum.java) | sort + two pointers | **15m / 22m** | Fix one value; solve two-sum in the suffix; skip duplicates at every layer. | Unique triplets; all-zero/duplicate/no-solution tests; pointer movement defended; O(n squared). | |
| 12 | **A** | **Letter Combinations / backtracking** | [17. Letter Combinations](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) | [Local Java](../../src/main/java/org/chijai/day11/backtracking/session1/LetterCombinationsOfAPhoneNumber.java) | choose → recurse → undo | **15m / 22m** | Depth equals input position; path is restored after each choice. | Empty input policy; base case and mutation correct; small trace spoken; output-sensitive complexity stated. | |
| 13 | **B** | **Validate Binary Search Tree** | [98. Validate Binary Search Tree](https://leetcode.com/problems/validate-binary-search-tree/) | [Local Java](../../src/main/java/org/chijai/day6/trees/session3/ValidateBST.java) | recursive lower/upper bounds | **12m / 18m** | Every node inherits bounds from all ancestors, not only its parent. | Strict duplicate policy; extreme integer values safe; invalid deep descendant tested; O(n) time/O(h) stack. | |
| 14 | **B** | **Coin Change / one-dimensional DP** | [322. Coin Change](https://leetcode.com/problems/coin-change/) | [Local Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) | optimal substructure + sentinel | **18m / 25m** | `dp[a]` is the fewest coins needed for exact amount `a`. | Base/impossible state correct; no sentinel overflow; loop order defended; O(amount x coins). | |
| 15 | **B** | **First Unique Character** | [387. First Unique Character](https://leetcode.com/problems/first-unique-character-in-a-string/) | [Local Java](../../src/main/java/org/chijai/java/FirstNonRepeatingCharacterUsingStreams.java) | frequency count + stable scan | **10m / 15m** | Count first, then scan original order. | Empty/no-unique/repeated input tested; deterministic order preserved; loop solution preferred during live coding. | |
| 16 | **B** | **Min Stack / stack design** | [155. Min Stack](https://leetcode.com/problems/min-stack/) | [Local Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) | main stack + minimum invariant | **12m / 18m** | Every stored state must make current minimum retrievable in O(1). | Duplicate minima and empty-operation contract handled; O(1) operations; two valid designs compared. | |
| 17 | **B** | **Employee maximum via Java Streams** | [Java Stream API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/Stream.html) | [Local Java](../../src/main/java/org/chijai/java/EmployeeMaxSalary.java) | comparator + terminal reduction | **10m / 15m** | Decide empty-result and tie behavior before choosing `max`. | Comparator correct; `Optional` handled; ties explained; can rewrite as plain loop if Streams obscure reasoning. | |
| 18 | **B** | **Sequence gap / duplicate execution detector** | [217. Contains Duplicate](https://leetcode.com/problems/contains-duplicate/) *(set anchor)* | [Sequence-gap Java](../../src/main/java/org/chijai/java/SequenceGapDetector.java)<br>[Deduplication Java](../../src/main/java/org/chijai/java/ExecutionDeduplication.java) | set/map + ordered-state invariant | **15m / 22m** | Define whether input is ordered and what makes an event a duplicate. | Contract, ordering assumption, duplicate/gap semantics and edge cases stated; connects to idempotency only after coding. | |

## Round-1 time-budget rule

Do **not** execute all 18 rows on Thursday. Use them as a bounded retrieval pool.

```text
Mock 1: choose one unseen or weak problem from 1–6.
Mock 2: choose one unseen or weak problem from 7–12.
Cold retrieval circuit: state trigger + invariant + edge case + complexity for 1–12.
Secondary recall: touch 13–18 only when the ledger marks the pattern red/due.
LLD follow-up: implement exactly one of 19–25.
SQL safety: use the appendix for at most 45 minutes; do not execute an SQL syllabus.
```

---

# LLD Revision — Treasury and Testable Services (Items 19–25)

| # | Priority | Design | Local Analogue | Core Pattern | Time / Stop | Minimal Hint | Definition of Done | Missed |
|---:|:---:|---|---|---|---|---|---|---|
| 19 | **A** | **Cash Position Aggregator** | `N/A — custom Treasury LLD` | account + currency + as-of key; immutable events | **25m / 35m** | Separate posted, pending and projected cash; preserve source/as-of lineage. | APIs for ingest/query/adjustment; idempotent event ID; money uses currency-safe decimal/minor units; out-of-order/as-of behavior, concurrency and audit stated. | |
| 20 | **A** | **Funding / Transfer Instruction Workflow** | [Order-management state-machine analogue](../../../../LLDProjects/order-management-system/README.md) | explicit state machine + idempotency | **30m / 40m** | Every command validates current state before mutation/publication. | Draft/approved/submitted/acknowledged/settled/failed/cancelled states; invalid transitions; approval boundary; duplicate callback; uncertain outcome; audit and retry ownership. | |
| 21 | **A** | **Reconciliation Engine** | [Stripe ledger-reconciliation analogue](../../../../SystemDesignProjects/stripe-ledger-reconciliation/README.md) | match keys + tolerance policy + exception queue | **30m / 40m** | Preserve both source records and explain every match/unmatched decision. | Exact/fuzzy policy separated; one-to-many ambiguity, duplicate file, late record, replay and manual resolution; deterministic rerun and metrics. | |
| 22 | **B** | **Per-user/API Rate Limiter** | [Token bucket project](../../../../LLDProjects/token-bucket-rate-limiter/README.md) | lazy refill + atomic acquire | **25m / 35m** | Inject time; keep one bucket mutation atomic. | Capacity/refill/reject tests; burst semantics; per-key independence; deterministic clock; single-instance limit and distributed alternative explained. | |
| 23 | **B** | **Treasury Reference-Data Cache / LRU** | [LRU project](../../../../LLDProjects/lru-cache/README.md) | map + DLL + explicit freshness policy | **25m / 35m** | Eviction and financial-data freshness are different policies. | O(1) operations; TTL/version/source-of-truth policy; stampede and dependency failure discussed; does not claim LRU solves stale correctness. | |
| 24 | **A** | **Incident-Safe Scheduled Job Runner** | [Distributed task scheduler analogue](../../../../SystemDesignProjects/distributed-task-scheduler/README.md) | leased work + idempotent execution + retry/DLQ | **30m / 40m** | At-least-once delivery requires idempotent business effects. | States/API, lease expiry, duplicate worker, bounded retry, poison job, checkpoint/restart, metrics and operator controls. | |
| 25 | **B** | **Treasury Query API + React View-Model Boundary** | `N/A — custom full-stack LLD` | service/repository + DTO + pagination | **25m / 35m** | UI must not infer financial correctness from presentation data. | Filters/as-of/currency; DTO validation; loading/error/empty states; pagination/sorting; authorization/audit; API tests and one React component data-flow explanation. | |

## LLD definition of done

```text
[ ] Functional requirements and exclusions clarified
[ ] Money, currency, timezone and as-of semantics explicit
[ ] Core invariant stated before classes
[ ] Public API and state transitions visible
[ ] Idempotency key and audit boundary identified
[ ] Invalid, duplicate and out-of-order paths covered
[ ] Data structures and hot-operation complexity justified
[ ] Thread/state ownership stated
[ ] Database transaction boundary stated
[ ] Unit and integration tests named
[ ] Java implementation is idiomatic, testable and explainable
```

---

# HLD Revision — Global Treasury Reliability (Items 26–30)

| # | Priority | Design | Local Analogue | Core Pattern | Time / Stop | Minimal Hint | Definition of Done | Missed |
|---:|:---:|---|---|---|---|---|---|---|
| 26 | **A** | **Global Cash and Liquidity Platform** | [Ledger-reconciliation source](../../../../SystemDesignProjects/stripe-ledger-reconciliation/README.md) | ingest → normalized ledger → projections → query/alerts | **40m / 50m** | Separate authoritative balances, pending movements and derived forecasts. | Accounts/currencies/as-of requirements; source lineage; partitioning; consistency/staleness; idempotency; cutoff/time zones; reconciliation; HA/recovery; audit and alerts. | |
| 27 | **A** | **Funding and Payment Instruction Platform** | [Reliable-order workflow analogue](../../../../SystemDesignProjects/reliable-order-platform/README.md) | durable workflow + outbox + idempotent adapters | **40m / 50m** | Network timeout creates an uncertain outcome, not automatic failure. | Approval, scheduling, bank/custodian adapter, correlation, retry, duplicate response, reconciliation, manual repair, encryption/authorization and audit. | |
| 28 | **A** | **Treasury Reconciliation Platform** | [Ledger-reconciliation project](../../../../SystemDesignProjects/stripe-ledger-reconciliation/README.md) | immutable inputs + deterministic matching + exception workflow | **35m / 45m** | Reprocessing the same file/event must not duplicate financial effects. | Ingestion identity, matching keys/tolerance, late/corrected records, scalable partition, replay, operator queue, lineage, metrics and daily close/SLO. | |
| 29 | **B** | **Global Scheduled Calculation / Reporting Platform** | [Distributed scheduler](../../../../SystemDesignProjects/distributed-task-scheduler/README.md) | control plane + workers + leases + dependency DAG | **35m / 45m** | Time zones and business calendars are domain data, not server-local assumptions. | Schedule/version, dependency graph, leases, retries, idempotency, catch-up after outage, holiday/cutoff handling, backpressure, audit and operator controls. | |
| 30 | **A** | **Production-Support Architecture for Global Trading Services** | [Multi-service aggregator](../../../../SystemDesignProjects/multi-service-aggregator/README.md)<br>[Electronic trading platform](../../../../SystemDesignProjects/electronic-trading-platform/README.md) | SLO + observability + bounded failure + remediation | **40m / 50m** | Start with user/business impact, then first failing boundary. | Logs/metrics/traces correlation; dependency timeouts; bounded retries; circuit/bulkhead/backpressure; graceful degradation; runbook, rollback, reconciliation, RTO/RPO and prevention action. | |

## HLD definition of done

```text
[ ] Functional scope and exclusions
[ ] Users, volumes, latency and availability SLOs
[ ] Money/currency/as-of/business-calendar semantics
[ ] Critical path and state source of truth
[ ] IDs, versioning and partition key
[ ] Sync versus async boundaries
[ ] Transaction/outbox/idempotency strategy
[ ] Consistency and stale-data policy
[ ] Timeouts, retries and overload behavior
[ ] Dependency failure and uncertain outcomes
[ ] HA, recovery, replay and reconciliation
[ ] Authorization, approvals, encryption and audit
[ ] Logs, metrics, traces, alerts and runbooks
[ ] One bottleneck and one trade-off deep dive
```

# Deferred Java-to-C# Reference — Not Part of Thursday

The interviewer knows the language gap and Java has been accepted for live coding. Do **not** schedule this section before the round. Keep it only as a later-stage reference if Point72 explicitly asks you to prepare for C#/.NET.

| Java anchor | C#/.NET bridge | What to say if asked |
|---|---|---|
| `HashMap<K,V>` | `Dictionary<TKey,TValue>` | `TryGetValue` combines presence check and retrieval |
| `ArrayList<T>` | `List<T>` | Resizable indexed collection |
| `HashSet<T>` | `HashSet<T>` | Equality/hash contract still matters |
| `ArrayDeque<T>` | `Queue<T>`, `Stack<T>`, `LinkedList<T>` as needed | C# exposes distinct queue/stack abstractions rather than one direct `ArrayDeque` equivalent |
| Streams | LINQ over `IEnumerable<T>` | Both compose transformations; deferred execution and repeated enumeration must be understood |
| `Optional<T>` | nullable reference/value types, result types | Nullability syntax helps static analysis; absence contract still needs design |
| `CompletableFuture<T>` | `Task<T>` + `async`/`await` | Async is not automatically parallel and does not remove shared-state races |
| Spring constructor DI | ASP.NET Core constructor DI | Depend on interfaces at external boundaries; do not make every value object injectable |
| JUnit/Mockito | xUnit/NUnit/MSTest + Moq/NSubstitute ecosystems | Test behavior and isolate true boundaries |
| try-with-resources | `using` / `IDisposable` | Deterministic cleanup of resources |
| checked/unchecked exceptions | C# exceptions are unchecked | API contracts and recovery policy matter more than compiler enforcement |
| JPA/Hibernate | Entity Framework Core | ORM is not a substitute for SQL/query-plan understanding |

Minimum C# retrieval skeleton:

```csharp
public static Dictionary<string, int> CountByKey(IEnumerable<string> values)
{
    var counts = new Dictionary<string, int>();
    foreach (var value in values)
    {
        counts.TryGetValue(value, out var current);
        counts[value] = current + 1;
    }
    return counts;
}
```

Know these words accurately:

```text
interface, class, record, struct, reference type, value type, nullable,
IEnumerable, LINQ, Dictionary, List, HashSet, Queue, Stack,
Task, async, await, CancellationToken, IDisposable, using,
dependency injection, middleware, controller, service, repository
```

Do not spend Thursday on this table, ASP.NET attributes or Entity Framework configuration.

---

# SQL Safety Appendix — Maximum 45 Minutes

The confirmed live-coding format makes Java DSA primary. Complete only one representative query and one slow-query explanation unless the recruiter/interviewer later says SQL coding is part of this round.

Before typing any SQL, say:

```text
1. My output grain is one row per ____.
2. The business key is ____.
3. This join is one-to-one / one-to-many / optional.
4. Unmatched rows should be kept / dropped.
5. Tie behavior is ____.
6. I will test nulls, duplicates, ties and empty groups.
```

For query optimization, use this order:

```text
reproduce with representative parameters
→ capture duration, reads, CPU and waits
→ inspect actual execution plan
→ compare estimated versus actual rows
→ find scans, repeated lookups, spills, sorts and blocking
→ check SARGability and implicit conversions
→ evaluate existing indexes and key order
→ change query/index/statistics only for a stated hypothesis
→ retest read and write impact
```

High-frequency SQL questions and answer anchors:

| Question | Answer anchor |
|---|---|
| `WHERE` versus `HAVING` | row filtering before grouping versus group filtering after aggregation |
| `INNER` versus `LEFT JOIN` | only matches versus preserve all left rows; a right-table predicate in `WHERE` can destroy outer semantics |
| `ROW_NUMBER` versus `RANK` versus `DENSE_RANK` | unique sequence versus gaps after ties versus no gaps after ties |
| clustered versus nonclustered index | data/leaf organization and one clustered ordering versus separate lookup structure; write/storage cost |
| composite index order | equality/filter/sort/access pattern and leftmost usable prefix |
| covering index | avoid lookup by including required columns, balanced against write/storage cost |
| SARGability | allow index seek; avoid wrapping filtered indexed column in a function or incompatible conversion |
| deadlock | cycle of lock waits; consistent order, shorter transactions, correct indexes, victim retry |
| optimistic concurrency | version check prevents lost update; conflict is detected and handled |
| CTE versus temp table | readability/single statement versus materialization, reuse, statistics and indexing needs |

---

# Treasury Domain Minimum Viable Fluency

Do not claim knowledge of Point72’s internal Treasury architecture. Use these as generic financial-domain requirements to ask and reason about.

## Core objects

```text
LegalEntity
BankAccount / CustodyAccount
Currency
BalanceSnapshot(asOf, source, status)
CashMovement / FundingInstruction
SettlementObligation
Forecast
FXRate(asOf, source)
ReconciliationBreak
Approval / AuditEvent
```

## Questions that reveal the real contract

1. Is the view posted cash, available cash, projected cash, or all three?
2. What is the authoritative source for each balance?
3. What does “as of” mean across time zones and delayed feeds?
4. Are amounts represented in minor units or decimal with currency rules?
5. Which instructions require maker/checker approval?
6. What is the idempotency identity for a file, event and external instruction?
7. How are pending, failed, cancelled, reversed and settled movements represented?
8. How is an uncertain external outcome reconciled?
9. Which stale data should block action versus show a warning?
10. What must be reproducible for audit at end of day?

## Treasury correctness invariants

```text
No amount without currency.
No balance without source and as-of time.
No external side effect without stable idempotency/correlation identity.
No approval-required instruction bypasses authorization.
No retry assumes a timed-out external operation failed.
No derived projection is confused with an authoritative posted balance.
Every adjustment and reconciliation decision is auditable.
Time zones and business-day cutoffs are explicit domain data.
```

---

# The 60-Minute Interview Branch Playbook

## First 90 seconds

```text
“As discussed, I’ll implement in Java. I’ll first restate the problem and
constraints, then outline the approach and complexity before I code.”
```

If the interviewer immediately gives the problem, shorten this to one sentence and begin clarifying. Do not re-open the already-settled C# question.

## Branch A — live coding

```text
0–3m   Restate contract; ask constraints and edge semantics
3–7m   Give baseline; identify repeated work/bottleneck
7–10m  State chosen pattern and invariant
10–35m Implement while narrating decisions
35–43m Test normal, boundary and adversarial cases
43–48m Complexity and one trade-off/follow-up
```

## Branch B — LLD follow-up after coding

```text
0–3m   Clarify use cases, exclusions and scale
3–6m   State the core invariant and public API
6–20m  Implement the smallest correct object model
20–27m Add invalid, duplicate and transition tests
27–35m Discuss concurrency, persistence and one extension
```

## Branch C — SQL surprise

```text
0–3m   State output grain, business key and sample rows
3–6m   State join cardinality, null and tie policy
6–20m  Build the query in stages/CTEs
20–27m Test nulls, duplicates, ties and missing relations
27–35m Explain index/plan reasoning without guessing
```

## Branch D — brief resume/design follow-up

```text
business problem and users
→ one critical request/event flow
→ state and concurrency ownership
→ failure, recovery and observability
→ your exact decision, alternative and trade-off
```

Keep this concise unless the interviewer explicitly changes the round into a project/design deep dive.

# Likely Non-Coding Questions and Rehearsal Answers

These are answer structures, not fabricated biography. Insert only facts you can defend.

| Question | Rehearsed answer structure | Red flag to avoid |
|---|---|---|
| **Tell me about yourself.** | Present role/domain → two strongest capabilities → one relevant outcome → why this role is the logical next step. Target 75–90 seconds. | Eight-year chronological autobiography |
| **Walk me through your current architecture.** | Users/contract → component diagram → critical flow → state owner → failure/recovery → your contribution → trade-off. | Naming tools without explaining why they exist |
| **Why Point72?** | Investor-led technology environment + software supporting global trading + business-user proximity + role fit in financial systems/reliability. | Generic “prestige and compensation” answer |
| **Why Treasury Technology?** | Correct global cash/funding information is business-critical; the domain rewards correctness, auditability, data lineage, timely decisions and resilient integration—strengths adjacent to risk infrastructure. | Pretending prior ownership of Treasury systems if untrue |
| **You are a Java engineer; why this C# role?** | State boundary honestly → demonstrate transferable OOP/distributed/SQL/testing/domain depth → show prepared mappings → explain ramp plan. | Claiming 8–12 years of C# or sounding unwilling to learn |
| **Tell me about a production incident.** | Impact → detection → containment → hypothesis/evidence → root cause → safe recovery/reconciliation → preventive code/test/alert/runbook. | Hero story with no prevention or team communication |
| **How do you handle incomplete requirements?** | Identify decision owner → write examples/invariants → separate reversible from irreversible choices → thin slice → validate with user → record assumptions. | Building before confirming financial semantics |
| **How do you practice TDD?** | Small observable behavior → failing test → minimal implementation → refactor → boundary/integration tests; give one real example. | Saying “we have high coverage” without test design |
| **How do you optimize a slow query?** | Measure → plan/cardinality/waits → hypothesis → query/index/statistics change → compare → watch write regression. | “Add an index” as the first and only answer |
| **How do you work across USA, Poland and India?** | Written decisions, overlap windows, clear ownership, asynchronous handoff, reproducible runbooks, incident escalation and no hidden single-person knowledge. | Promising permanent availability across all time zones |
| **Tell me about disagreement with a business user.** | Shared objective → expose competing risks with examples/data → propose options → document decision → support outcome → revisit with evidence. | Describing the stakeholder as nontechnical or wrong |
| **How do you uphold ethical standards?** | Protect data/access, surface material risk, preserve audit trail, refuse to bypass controls, escalate appropriately and document objectively. | Abstract values with no behavioral decision |

## The 5-minute PTR project answer

```text
0:00–0:30  Business context and users
0:30–1:15  Correctness/latency/resiliency contract
1:15–2:15  Components and one end-to-end flow
2:15–3:00  State, consistency and concurrency
3:00–3:40  Failure/recovery/observability
3:40–4:20  My exact contribution and decision
4:20–5:00  Trade-off, outcome and what I would improve
```

For every claim, label it mentally:

```text
I did      = my direct action
We did     = team outcome; state my part
I observed = production/environment fact I personally saw
I would    = hypothetical design, not historical fact
```

---

# Production Incident Drill

Use this scenario if you need a blank rehearsal prompt:

> A global treasury dashboard shows stale cash balances for one region shortly before a funding cutoff. Some upstream events are delayed, and an operator has retried an import. Explain diagnosis, containment, correctness, recovery, communication and prevention.

Required sequence:

```text
1. Establish business impact, affected entities/currencies and cutoff.
2. Freeze unsafe automated action if stale data could create financial harm.
3. Compare freshness/source/as-of metrics and trace correlation IDs.
4. Determine whether data is missing, delayed, duplicated or only the projection is stale.
5. Preserve raw evidence; do not “fix” by overwriting history.
6. Recover through idempotent replay/reconciliation.
7. Communicate status, decision owner and next checkpoint across time zones.
8. Add prevention: freshness SLO, alert, idempotency test, runbook and failure drill.
```

---

# Questions to Ask Krystian

Choose two or three based on what the interview already covered:

1. “Which Treasury workflows or systems would this Bengaluru role own first?”
2. “What are the most important correctness and availability expectations for the team’s applications?”
3. “How is engineering ownership divided and handed off across the USA, Poland and India?”
4. “How much of the near-term work is modernization versus new capability?”
5. “What depth in C#/.NET would you expect on day one, and what does successful ramp-up look like?”
6. “What distinguishes someone who performs strongly in this team after the first six months?”

Do not ask all six.

---

# Final Go / No-Go Gates

## Thursday 9:35 PM gate

| Gate | Pass condition | If failed |
|---|---|---|
| Mock 1 | One Java problem completed in 60 minutes with narration and tests | Repair only the recorded failure; then repeat the same problem cold |
| Mock 2 | A second Java problem from a different pattern completed under observation rules | Friday warm-up repeats the failed pattern, not a new hard problem |
| Tier A retrieval | For rows 1–12, trigger + invariant + edge case + complexity recalled without code | Review only red/due rows; do not reread all solutions |
| Java safety | Collections, comparator, equality, overflow, boundaries and basic testing are fluent | Run a 30-minute syntax/edge-case circuit |
| LLD | One item from 19–25 implemented with API, invariant and tests | Friday rehearse the same design verbally; do not start a second design |
| SQL hedge | One join/window query plus a slow-query answer recalled | Spend Friday at most 20 minutes repairing the exact gap |

## Friday 12:45 PM gate

```text
GO if:
- rested enough to reason;
- invitation links and audio work;
- opening, Java editor choice and communication loop are ready;
- no unresolved correctness misconception is being crammed.

STOP STUDYING even if:
- every C# API is unknown;
- one optional DSA row remains;
- React depth is shallow;
- the entire 30-row workbook is not completed.
```

The objective is not to finish this document. The objective is to enter the interview able to handle its most plausible branch calmly and honestly.

---

# One-Glance Point72 Recall Map

```text
CONFIRMED LIVE CODING
------------------------------------------------
Java DSA is the primary lane
possible LLD follow-up | short SQL safety hedge

LIVE-CODING LOOP
------------------------------------------------
contract -> examples -> baseline -> invariant -> clean Java
-> normal/boundary/adversarial tests -> complexity

PATTERN TRIGGERS
------------------------------------------------
lookup/group -> hash map | contiguous -> window
ordered feasibility -> binary search | priority -> heap
dependency/components -> graph | choices -> backtracking/DP

LLD FOLLOW-UP
------------------------------------------------
requirements -> invariant -> API -> objects/state
-> invalid/duplicate path -> tests -> concurrency/trade-off

SQL SURPRISE
------------------------------------------------
grain -> keys -> cardinality -> null policy -> query
-> ties -> tests -> plan/index hypothesis
```

## Final rule

```text
Do not expand the DSA syllabus or chase problem count.
Use the completed syllabus for cold reconstruction and transfer.
Two realistic mocks plus failure repair beat new passive coverage.
Implement one LLD and keep SQL to one bounded safety block.
Do no C# study before this round.
```
