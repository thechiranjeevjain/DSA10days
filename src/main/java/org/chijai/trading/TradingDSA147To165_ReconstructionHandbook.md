# Trading DSA 147--165 --- Independent Reconstruction Handbook

> **Purpose:** Train yourself to re-invent solutions from first
> principles rather than memorize code.
>
> **Companion file:** `TradingDSA147To165.java`
>
> **Core loop:** **Problem → invariant → required operations →
> constraint pressure → data structure → minimum state → event
> transition → complexity → trade-offs → mutation → blind
> reconstruction.**

------------------------------------------------------------------------

# 0. How to Use This Handbook

For every problem, do **not** begin by reading the solution. Use four
passes:

1.  **Recognition:** Read only the problem + ELI5. Name the pattern.
2.  **Derivation:** Write the invariant, required operations, state, and
    target complexity.
3.  **Reconstruction:** Write pseudocode, then Java without looking.
4.  **Mutation:** Change one requirement and explain what breaks or
    changes.

A solution is learned only when you can answer, without notes:

-   What must always remain true?
-   Which operations must be cheap?
-   Why does the naive approach fail?
-   Why this data structure?
-   What state must survive between events?
-   Why is the complexity true?
-   What are the trade-offs?
-   What production assumption would break this solution?

------------------------------------------------------------------------

# 1. Universal Reconstruction Algorithm

## 1.1 Identify the input shape

Ask whether the input is a:

-   static collection,
-   continuously arriving stream,
-   ordered stream,
-   out-of-order stream,
-   sliding window,
-   keyed aggregation,
-   priority problem,
-   lifecycle/event-state problem,
-   merge of multiple sorted sources.

The input shape often identifies the pattern family.

## 1.2 State the invariant

Examples:

``` text
Execution dedupe:
One execution ID changes state at most once.

Price-time priority:
Best price wins; within one price, earliest arrival wins.

Sequence reorder:
Everything below nextExpected has already been emitted exactly once.

Sliding maximum:
Deque contains only candidates that can still become a future maximum.

Order matching:
Only crossing best prices may trade; price-time priority is preserved.
```

If you cannot state the invariant simply, do not code yet.

## 1.3 List required operations

Examples:

``` text
insert price
delete price
get highest price
get lowest price
```

→ ordered map.

``` text
append newest event
expire oldest events
maintain aggregate
```

→ deque + running aggregate.

``` text
have I seen ID?
```

→ HashSet.

Start from operations, not from an algorithm name.

## 1.4 Let constraints eliminate bad solutions

For `n = 10^6`, repeated full scans, repeated sorting, and `O(n²)`
approaches are immediately suspicious.

For streams ask:

> Can I update the answer from the previous state instead of recomputing
> history?

That question solves a large fraction of this handbook.

## 1.5 Find minimum sufficient state

Ask:

> What is the least information from the past that lets me process the
> next event correctly?

Examples:

``` text
Dedup          → seen IDs
VWAP           → deque + Σ(px×qty) + Σqty
Position       → map(key → position)
Reordering     → nextExpected + buffer
Top K          → aggregate map + size-K heap
```

------------------------------------------------------------------------

# 2. Master Pattern Map

``` text
TRADING DSA

IDENTITY / HASHING
├── 150 Execution Deduplication
├── 153 Rolling Exposure
├── 160 Position From Executions
└── 161 Duplicate Orders

ORDERED STRUCTURES
├── 147 Best Bid / Best Ask
├── 148 Top N Price Levels
├── 149 Price-Time Priority
└── 159 Matching Engine

WINDOWS / DEQUES
├── 155 Exchange Throttle
├── 156 Rolling VWAP
├── 157 Sliding Maximum
└── 162 Most Active Instruments

SEQUENCE / STREAM CORRECTNESS
├── 151 Sequence Gap
├── 152 Out-of-Order Reordering
├── 163 Merge K Ordered Feeds
└── 164 Snapshot + Incrementals

VALIDATION / STATE / STATISTICS
├── 154 Price Deviation
├── 158 Order State Aggregation
└── 165 Latency Percentile
```

## Dependency-Ordered Learning Path

The DSA numbers remain unchanged for traceability, but **study the
chapters in this order rather than numerically**:

``` text
STAGE 1  Identity / membership
150 Execution Deduplication → 161 Duplicate Orders

STAGE 2  Keyed incremental state
160 Position → 153 Rolling Exposure

STAGE 3  Ordered keys
147 Best Bid/Ask → 148 Top N Levels

STAGE 4  Priority
149 Price-Time Priority

STAGE 5  Windows / deques
155 Throttle → 156 VWAP → 157 Sliding Maximum

STAGE 6  First composition
162 Most Active Instruments
= Deque + HashMap + Top-K Heap

STAGE 7  Sequence correctness
151 Gap Detection → 152 Reordering

STAGE 8  Risk validation
154 Price Deviation

STAGE 9  Event state
158 Order State Aggregation

STAGE 10 Multi-stream ordering
163 Merge K Ordered Feeds

STAGE 11 Recovery / replay
164 Snapshot + Incrementals

STAGE 12 Capstone
159 Matching Engine

STAGE 13 Performance statistics
165 Latency Percentile
```

### Why this sequence is better

Each stage adds **one new mental primitive** and deliberately reuses
earlier ones. You learn a compact vocabulary---identity, keyed state,
ordering, priority, expiry, sequence, lifecycle, recovery,
composition---instead of storing 19 unrelated tricks.

Before advancing, reconstruct the current problem from only:

``` text
INVARIANT
→ REQUIRED OPERATIONS
→ DATA STRUCTURE
→ MINIMUM STATE
→ ONE-EVENT TRANSITION
→ COMPLEXITY
```

If you still need implementation lines as prompts, repeat the current
stage before adding another abstraction.

## Prerequisite Matrix

  -----------------------------------------------------------------------------
                   Step Problem          New primitive     Reuses
  --------------------- ---------------- ----------------- --------------------
                      1 DSA-150          HashSet           ---
                                         membership /      
                                         idempotency       

                      2 DSA-161          Business identity HashSet

                      3 DSA-160          HashMap           identity/keying
                                         incremental       
                                         aggregate         

                      4 DSA-153          General keyed     HashMap aggregation
                                         delta state       

                      5 DSA-147          TreeMap ordered   map state
                                         extremes          

                      6 DSA-148          Ordered depth     TreeMap
                                         traversal         

                      7 DSA-149          PriorityQueue +   ordering/tie-break
                                         comparator        

                      8 DSA-155          Deque FIFO expiry streaming state

                      9 DSA-156          Rolling           deque + deltas
                                         aggregate +       
                                         expiry            

                     10 DSA-157          Monotonic deque   deque invariant

                     11 DSA-162          Top-K composition deque + map + heap

                     12 DSA-151          Sequence          streaming
                                         invariant         

                     13 DSA-152          Sequence          gaps + maps
                                         buffer/drain      

                     14 DSA-154          O(1) keyed        latest-state map
                                         validation        

                     15 DSA-158          Finite state      keyed event state
                                         machine           

                     16 DSA-163          K-way merge       heap + ordering

                     17 DSA-164          Snapshot/replay   sequence + state
                                         recovery          

                     18 DSA-159          Matching-engine   ordered book +
                                         composition       priority + mutable
                                                           state

                     19 DSA-165          Selection /       complexity
                                         streaming         trade-offs
                                         quantiles         
  -----------------------------------------------------------------------------

> **Rule:** the "Reuses" column should already be retrievable without
> notes before you start that row.

------------------------------------------------------------------------

# PART II --- Dependency-Ordered Deep Dives

# STAGE 1 --- IDENTITY & HASHING

> **Why this comes now:** Start with the smallest streaming primitive:
> exact membership---has this identity already been processed?

# DSA-150 --- Deduplicate Execution Events

## Problem

Ensure every unique execution ID changes state at most once.

## ELI5

A cashier receives receipts over an unreliable network. The same receipt
may arrive twice. Before adding money to the total, stamp each receipt
ID. A stamped receipt is ignored if seen again.

## Invariant

``` text
One execution ID affects state at most once.
```

## Required operations

``` text
have I seen ID?
mark ID seen
```

## Naive approach

Store processed IDs in a list and linearly search: `O(U)` per event,
potentially `O(N²)` total.

## Derivation

Identity membership → `HashSet`.

The elegant Java operation:

``` java
if (!processed.add(executionId)) {
    // duplicate
}
```

## Complexity

``` text
expected time/event   O(1)
space                 O(U)
```

`U` = unique retained IDs.

## Trade-offs

**HashSet:** simple exact in-memory dedupe.\
**Database/idempotency table:** durable but slower.\
**Bloom filter:** bounded memory and fast, but false positives; unsafe
alone when dropping a real execution is unacceptable.\
**Sequence-based dedupe:** better when protocol guarantees sequencing.

## Common confusions

**Exactly once processing** is not magically achieved by a HashSet
across crashes. This gives in-process "at most once per remembered ID."

**Duplicate delivery** is not duplicate business execution unless
identity semantics say so.

## What not to do --- and why

-   Do not update state before checking the ID.
-   Do not use payload equality if execution ID is authoritative.
-   Do not forget memory growth on an infinite stream.
-   Do not call the solution crash-safe exactly-once unless state and
    dedupe record are committed atomically/durably.

## Production reality

Durable idempotency often requires transactionally coupling "execution
ID processed" with the state mutation.

## Mutation

**Memory capped:** introduce retention/TTL only if the protocol
guarantees duplicates cannot arrive after the retention horizon.

## Memory hook

``` text
EXACT ID ONCE → SET BEFORE STATE
```

------------------------------------------------------------------------

------------------------------------------------------------------------

> **Why this comes now:** Reuse HashSet, but separate the harder
> question: what exactly constitutes business identity?

# DSA-161 --- Detect Duplicate Orders

## Problem

Identify duplicate business orders using an ID or supplied identity
rule.

## ELI5

Two envelopes may contain identical-looking text but still be different
requests. First decide what uniquely identifies a request; only then can
you detect duplicates.

## Critical insight

Dedupe has two independent questions:

``` text
1. What constitutes identity?
2. How do I remember identities?
```

A HashSet solves only #2.

## Example identity

``` text
(account, clientOrderId, instrument)
```

## Invariant

Every accepted business identity appears once in the seen set.

## Complexity

``` text
expected O(1) per order
O(unique retained identities) space
```

## Trade-offs

Same exact/durable/bounded-memory choices as DSA-150.

## Common confusions

Two orders with the same economics may be legitimate separate orders. Do
not invent dedupe semantics from payload similarity.

## What not to do --- and why

-   Do not use mutable objects as HashSet keys.
-   Do not omit fields required by the identity contract.
-   Do not include irrelevant fields that make retransmissions look
    unique.
-   Do not confuse duplicate transport message with duplicate business
    order.

## Mutation

**Client order IDs unique only per trading day:** identity/retention
must include the session/day boundary.

## Memory hook

``` text
DEDUPE = DEFINE IDENTITY FIRST, SET SECOND
```

------------------------------------------------------------------------

------------------------------------------------------------------------

# STAGE 2 --- KEYED INCREMENTAL AGGREGATION

> **Why this comes now:** Turn membership into `key → running value`;
> executions become signed deltas.

# DSA-160 --- Position From Executions

## Problem

Compute current position by account/instrument from executions.

## ELI5

Every completed buy adds units to inventory; every completed sell
subtracts them.

## Transformation

``` text
BUY  qty → +qty
SELL qty → -qty
```

Then:

``` text
position[key] += signedQty
```

## Invariant

Position equals the sum of **executed** signed quantities, not
submitted/open-order quantities.

## Complexity

``` text
expected O(1) per execution
O(active account-instrument keys) space
```

## Trade-offs

Composite key choices mirror DSA-153.

## Common confusions

Position is not exposure. A 100-share position can have different
monetary exposure as price changes.

## What not to do --- and why

-   Do not update position on `NEW` or `ACK`.
-   Do not count duplicate executions---compose with DSA-150.
-   Do not forget side sign.
-   Do not assume sell means position cannot become negative; short
    positions may be valid.

## Mutation

**Need average cost/P&L:** position alone is insufficient; maintain cost
basis and realized/unrealized P&L semantics.

## Memory hook

``` text
EXECUTION → SIGN → ACCUMULATE
```

------------------------------------------------------------------------

------------------------------------------------------------------------

> **Why this comes now:** Generalize position into arbitrary keyed
> exposure deltas.

# DSA-153 --- Rolling Exposure

## Problem

Maintain current exposure per account/instrument under incoming deltas.

## ELI5

Each account has a running tab for each instrument. Every event adds or
subtracts from that tab. You never recalculate every receipt from day
one.

## Invariant

``` text
stored exposure(key)
= sum of all accepted exposure-changing deltas for key
```

## Required operations

``` text
lookup key
add delta
read current value
```

## Derivation

Keyed incremental aggregate → `HashMap<Key, Long>`.

## Complexity

``` text
expected update/query   O(1)
space                   O(active keys)
```

## Trade-offs

**Composite object key:** readable but allocates.\
**Nested maps:** avoids custom equality but adds indirection.\
**Encoded primitive key:** lower allocation/latency, more complexity.

## Common confusions

Exposure is domain-defined. Position, notional exposure, open-order
exposure, and risk exposure are not automatically the same quantity.

## What not to do --- and why

-   Do not replay all historical events for every query.
-   Do not forget `equals/hashCode` for composite keys.
-   Do not use `int` if quantities/notionals can overflow.
-   Do not remove a zero entry if zero-valued keys carry business
    meaning; otherwise removing is a useful space optimization.

## Mutation

**Need global + account + instrument exposure simultaneously:** maintain
multiple incremental aggregates per event.

## Memory hook

``` text
KEY + DELTA → RUNNING MAP
```

------------------------------------------------------------------------

------------------------------------------------------------------------

# STAGE 3 --- ORDERED KEYS

> **Why this comes now:** HashMap stops being enough when key order
> itself matters; introduce TreeMap.

# DSA-147 --- Best Bid / Best Ask

## Problem

Maintain the current highest bid and lowest ask while price levels are
updated.

## ELI5

Imagine two auction boards. Buyers write what they will pay; sellers
write what they will accept. You constantly need the most generous buyer
and cheapest seller. Re-scanning every sticky note after every change is
wasteful; keep the board ordered.

## Example

``` text
Bids:  99→20, 100→10, 101→5
Asks: 102→8, 103→7

Best bid = 101
Best ask = 102
```

## First-principles question

What must be cheap?

``` text
update arbitrary price
remove empty price
find maximum bid
find minimum ask
```

## Invariant

Every stored price level has positive aggregate quantity, and bid/ask
keys remain ordered.

## Naive approach

`HashMap<price, quantity>` gives expected `O(1)` updates but requires
`O(P)` scanning for every best-price query.

## Derivation

Arbitrary updates **plus** ordered extremes imply an ordered map:

``` java
TreeMap<Integer, Long> bids;
TreeMap<Integer, Long> asks;
```

Best bid = `bids.lastKey()`.\
Best ask = `asks.firstKey()`.

## Minimum state

Only active price levels and their aggregate quantities.

## Mechanism

``` text
newQty = oldQty + delta

newQty > 0 → store
newQty = 0 → remove
newQty < 0 → invalid update
```

## Complexity

With Java `TreeMap`:

``` text
update                  O(log P)
insert/remove           O(log P)
firstKey()/lastKey()    O(log P) defensible bound
space                   O(P)
```

`P` = active price levels.

## Trade-offs

**TreeMap:** excellent arbitrary level updates and ordered traversal.\
**PriorityQueue:** good for repeated best extraction, awkward for
arbitrary level modification/removal.\
**HashMap:** fastest average direct lookup, but no ordered extrema.\
**TreeSet + map:** possible but duplicates state unless there is a
reason.

## Common confusions

**Best order vs best price level:** one level may contain many orders.
This problem aggregates by price.

**`lastKey()` vs `firstKey()`:** bids want maximum; asks want minimum.

## What not to do --- and why

-   Do not scan a HashMap for every BBO query: `O(P)` per query.
-   Do not retain zero-quantity levels: they can become false best
    prices.
-   Do not use `double` as a production price key without understanding
    precision. Integer ticks/scaled longs are safer.
-   Do not assume a heap is automatically best because the word "best"
    appears.

## Edge cases

Empty side, removing current best, repeated updates to same level,
negative resulting quantity, very large quantities.

## Production reality

Real books often use price ticks, specialized arrays/trees, intrusive
queues, or venue-specific bounded price domains to reduce allocations
and latency.

## Mutation

**Need cancellation by order ID:** price-level aggregation alone is
insufficient. Add order identity/indexing.

## 60-second answer

Maintain separate ordered maps for bids and asks. Each map stores
aggregate quantity by price. Updates insert, modify, or remove a level
in `O(log P)`. The highest bid is the largest bid key and lowest ask is
the smallest ask key. The invariant is that only active
positive-quantity levels are stored.

## Blind reconstruction

1.  Why not HashMap alone?
2.  Why not PriorityQueue alone?
3.  What does a map entry represent?
4.  When is a level removed?
5.  What changes if cancellation is by order ID?

## Memory hook

``` text
BBO → ORDERED PRICE LEVELS → TreeMap
```

------------------------------------------------------------------------

------------------------------------------------------------------------

> **Why this comes now:** Extend one extreme into ordered depth
> traversal using the same structure.

# DSA-148 --- Top N Price Levels

## Problem

Maintain the top `N` bid and ask price levels under updates.

## ELI5

Instead of asking for only the best buyer and seller, the trader wants
to see the first few rows of each side of the auction board.

## Example

``` text
Bids: 101, 100, 99, 98
Top 3 bids → 101, 100, 99

Asks: 102, 103, 104, 105
Top 3 asks → 102, 103, 104
```

## Invariant

The book contains exactly the active price levels; query traversal
follows market priority.

## Required operations

``` text
arbitrary level update
ordered traversal from one extreme
stop after N
```

## Naive approach

Store in HashMap and sort all `P` prices for every query:

``` text
O(P log P) per query
```

Wasteful when `N << P`.

## Derivation

Reuse the ordered maps from DSA-147.

``` text
BUY  → descendingMap()
SELL → ascending/default order
```

Read only the first `N`.

## Complexity

``` text
update       O(log P)
top-N        O(log P + N) conservative traversal bound
space        O(P)
```

## Trade-offs

**TreeMap:** natural when updates and depth queries coexist.\
**Heap:** useful for a one-off batch Top-N, but mutable price
levels/cancellations complicate it.\
**Maintain only N levels:** wrong if a current top level
disappears---you need to know what comes next.

## Common confusions

Top N **orders** is not Top N **price levels**. Multiple orders at the
same price collapse into one level.

## What not to do --- and why

-   Do not sort the full book on every query.
-   Do not discard levels below current Top N; they may enter Top N
    after updates.
-   Do not reverse ask ordering; lowest ask is best.
-   Do not model each order if the problem only asks for aggregated
    levels.

## Mutation

**N is huge and queries rare:** full ordered traversal may be fine;
avoid maintaining additional Top-N state.

## Memory hook

``` text
BBO + DEPTH → same TreeMap, walk N
```

------------------------------------------------------------------------

------------------------------------------------------------------------

# STAGE 4 --- PRIORITY / HEAPS

> **Why this comes now:** Move from ordered keys to ordered objects:
> best candidate under a composite comparator.

# DSA-149 --- Price-Time Priority Queue

## Problem

Given orders with side, price, and arrival sequence, return the next
eligible order under price-time priority.

## ELI5

At an auction, better offers jump ahead. If two people offer the same
price, whoever arrived first stays ahead.

## Rule

``` text
BUY:  higher price first, then earlier sequence
SELL: lower price first, then earlier sequence
```

## Invariant

The root/head is always the highest-priority order for that side.

## Required operations

``` text
insert order
peek next
remove next
```

## Naive approach

Append to a list and scan for the best order each time: `O(N)` per
selection.

## Derivation

This is lexicographic priority. A `PriorityQueue` directly supports the
minimal interview problem.

BUY comparator:

``` text
price DESC
sequence ASC
```

SELL comparator:

``` text
price ASC
sequence ASC
```

## Complexity

``` text
insert   O(log N)
peek     O(1)
poll     O(log N)
space    O(N)
```

## Trade-offs

**PriorityQueue:** minimal and clean for "give me next order."\
**TreeMap\<Price, FIFO Queue`<Order>`{=html}\>:** more faithful
order-book model; supports explicit levels and FIFO naturally.\
**Linked list:** good FIFO only after price level is already known.

## Common confusions

**Price priority before time priority.** Earlier time does not beat a
better price.

**Arrival timestamp vs sequence:** timestamps can tie; a monotonic
arrival sequence gives deterministic ordering.

## What not to do --- and why

-   Do not compare sequence before price.
-   Do not use one comparator for both sides.
-   Do not mutate fields used by a heap comparator while an item remains
    in the heap; heap order will become invalid.
-   Do not claim a heap is a complete production order book.

## Mutation

**Need arbitrary cancellation:** heap removal is `O(N)` in Java unless
you add indexing/lazy deletion or switch structures.

## Memory hook

``` text
PRICE FIRST → TIME BREAKS TIE
```

------------------------------------------------------------------------

------------------------------------------------------------------------

# STAGE 5 --- WINDOWS & DEQUES

> **Why this comes now:** Introduce FIFO expiry: old events leave in
> arrival order.

# DSA-155 --- Exchange Throttle

## Problem

Accept at most `R` requests per key during the last `T` time units.

## ELI5

A nightclub allows at most two entries from the same pass within a
rolling minute. Keep only entry times that are still inside the minute.

## Invariant

The deque contains exactly the accepted request timestamps still inside
the active window.

## Required operations

``` text
remove oldest expired
count current
append newest accepted
```

This is exactly deque behavior.

## Mechanism

``` text
cutoff = now - window

while oldest <= cutoff:
    remove oldest

if size >= limit:
    reject
else:
    append now
    accept
```

## Complexity

Each accepted timestamp enters once and leaves once:

``` text
amortized O(1) per request
space O(R × active keys) under this representation
```

## Trade-offs

**Exact sliding-log deque:** accurate, memory proportional to retained
requests.\
**Fixed window counter:** `O(1)` tiny state, but boundary bursts can
exceed intended rolling behavior.\
**Token bucket:** permits controlled bursts and smooth refill; common
production limiter.\
**Leaky bucket:** smooth output rate.

## Common confusions

Fixed window, sliding window, token bucket, and leaky bucket are
different policies.

Rejected requests: decide whether they count against the limit. The
implementation must match the policy.

## What not to do --- and why

-   Do not keep expired timestamps.
-   Do not assume timestamps arrive monotonically unless stated.
-   Do not use `queue.size()` as a global limit if throttling is per
    account/key.
-   Do not describe a local in-memory limiter as a distributed global
    limiter.

## Mutation

**Per-account and exchange-global limits:** request must pass two
independent limiters.

## Memory hook

``` text
RATE IN LAST T → EXPIRE FRONT + APPEND BACK
```

------------------------------------------------------------------------

------------------------------------------------------------------------

> **Why this comes now:** Add running aggregates to expiry so the window
> answer is maintained, not recomputed.

# DSA-156 --- Rolling VWAP

## Problem

Compute volume-weighted average price over the active trade window.

## ELI5

Buying 100 shares at ₹10 should influence the average more than buying 1
share at ₹20. Weight each price by how much was traded.

## Formula

``` text
VWAP = Σ(price × quantity) / Σ(quantity)
```

## Naive approach

For every new trade, rescan every trade in the window. With a large
window this repeats almost identical work.

## Derivation

Maintain the aggregate incrementally:

``` text
sumPxQty
sumQty
deque of trades for expiry
```

On add:

``` text
sumPxQty += price × qty
sumQty   += qty
```

On expiration, subtract the same contribution.

## Invariant

The deque and both running sums represent exactly the trades currently
inside the window.

## Complexity

``` text
amortized O(1) per trade
O(W) space
```

## Trade-offs

**Count window:** fixed-size deque.\
**Time window:** timestamp expiry.\
**Per-instrument VWAP:** map instrument → window state.\
**Exact decimal:** scaled integers/BigDecimal considerations.

## Common confusions

VWAP is not ordinary average price.

``` text
(100×10 + 110×20) / 30
≠
(100 + 110) / 2
```

## What not to do --- and why

-   Do not recompute the entire window.
-   Do not forget to subtract expired quantities from both numerator and
    denominator.
-   Do not average per-trade averages.
-   Do not assume trade timestamps are ordered if the problem does not
    guarantee it.

## Mutation

**Rolling VWAP per symbol:** each symbol gets independent deque +
aggregates.

## Memory hook

``` text
VWAP → KEEP NUMERATOR + DENOMINATOR, ADD/SUBTRACT
```

------------------------------------------------------------------------

------------------------------------------------------------------------

> **Why this comes now:** Strengthen the deque with a monotonic
> invariant to discard dominated candidates.

# DSA-157 --- Market Data Sliding Maximum

## Problem

Return the maximum value in every sliding window of size `K`.

## ELI5

You look through a moving window. If a taller building appears behind a
shorter building, that shorter building can never become the tallest
while the new taller one remains visible. Forget it permanently.

## Naive approach

Scan each window: `O(NK)`.

Heap approach: roughly `O(N log K)` with expiry handling.

Optimal: monotonic deque.

## Invariant

The deque stores indices that:

1.  are inside the current window, and
2.  have values in decreasing order.

Therefore the front is always the maximum.

## Mechanism

For index `i`:

``` text
remove expired indices from front
remove values <= current from back
append i
front = maximum
```

## Why removing smaller values is safe

If newer value `B >= A`, then while `B` remains in the window:

-   B expires later than A, and
-   B is at least as large.

A can never win again.

## Complexity

Every index enters once and leaves once:

``` text
O(N) time
O(K) space
```

## Trade-offs

**Deque:** optimal for fixed sliding max/min.\
**Heap:** easier to generalize but slower and requires stale-entry
handling.\
**TreeMap/multiset:** `O(log K)` updates and easy min/max/frequency
behavior.

## Common confusions

Store **indices**, not only values, because expiry depends on position.

The deque is not sorted by index value; indices increase by arrival,
while their corresponding values decrease.

## What not to do --- and why

-   Do not scan every window.
-   Do not remove only `< current` without deciding duplicate semantics.
-   Do not forget expired indices.
-   Do not use a normal queue; monotonic pruning is the optimization.

## Mutation

**Need both min and max:** maintain two monotonic deques.

## Memory hook

``` text
SLIDING MAX → DECREASING DEQUE
```

------------------------------------------------------------------------

------------------------------------------------------------------------

# STAGE 6 --- FIRST COMPOSITION

> **Why this comes now:** Compose three mastered primitives: deque
> expiry + HashMap aggregation + size-K heap.

# DSA-162 --- Most Active Instruments

## Problem

Return Top K instruments by trade/message volume in a rolling window.

## ELI5

Keep a scoreboard for activity during the last hour. Old scores expire,
new scores arrive, and you occasionally want the leaders.

## Composition

This is two problems:

``` text
rolling aggregation
+
Top K selection
```

## State

``` text
Deque<Event> window
Map<Instrument, Volume> aggregate
```

For query:

``` text
min-heap of size K
```

## Invariant

The aggregate map equals the sum of events currently inside the
deque/window.

## Mechanism

On event:

``` text
append event
aggregate[instrument] += volume
evict expired events
subtract each expired contribution
```

Top K:

``` text
for each instrument:
    push activity
    if heap.size > K:
        pop smallest
```

## Complexity

``` text
ingestion       amortized O(1)
Top-K query     O(M log K)
window space    O(W)
aggregate       O(M)
```

## Trade-offs

**Query-time heap:** cheap writes, more expensive Top-K queries.\
**Incremental ranking structure:** faster queries, more
expensive/complex updates.\
**Bucket approach:** possible if activity counts have small bounded
integer domain.

## Common confusions

`K` is number of results; `M` is number of active instruments; `W` is
number of retained events.

"Most active" must define whether activity means message count, trade
count, quantity, or notional.

## What not to do --- and why

-   Do not sort all instruments if K is tiny unless simplicity is worth
    it.
-   Do not forget to subtract expired events.
-   Do not leave zero aggregate entries indefinitely.
-   Do not call it rolling if old events never expire.

## Mutation

**Top K requested after every event:** consider maintaining an ordered
ranking, but explain update/query trade-off.

## Memory hook

``` text
WINDOW AGGREGATE → MAP; TOP K → MIN-HEAP K
```

------------------------------------------------------------------------

------------------------------------------------------------------------

# STAGE 7 --- SEQUENCE CORRECTNESS

> **Why this comes now:** Introduce sequence numbers in their simplest
> form: detect holes in an ordered stream.

# DSA-151 --- Detect Sequence Gap

## Problem

Given strictly increasing sequence numbers, report missing ranges.

## ELI5

Pages arrive numbered `1, 2, 5, 6, 10`. Looking at neighboring pages
tells you exactly which pages are missing.

## Example

``` text
1, 2, 5, 6, 10
→ 3-4, 7-9
```

## Invariant

For already ordered input, every gap is completely determined by two
adjacent sequence numbers.

## Derivation

For each `current`:

``` text
if current > previous + 1:
    missing = [previous + 1, current - 1]
```

## Complexity

``` text
time       O(N)
extra      O(1), excluding output
```

## Trade-offs

No special data structure is needed if input is already ordered. If
input can be reordered, this problem turns into DSA-152.

## Common confusions

A duplicate/out-of-order sequence (`current <= previous`) is not a
"missing gap"; it violates the stated ordered-input assumption.

## What not to do --- and why

-   Do not create every missing number if ranges are enough; a gap could
    be enormous.
-   Do not sort if the input contract already guarantees order.
-   Beware `previous + 1` overflow at `Long.MAX_VALUE`.

## Mutation

**Need online detection:** keep only the previous sequence and emit gaps
as events arrive.

## Memory hook

``` text
CURRENT > PREVIOUS + 1 → GAP
```

------------------------------------------------------------------------

------------------------------------------------------------------------

> **Why this comes now:** Add buffering when the stream itself arrives
> out of order: expected + buffer + drain.

# DSA-152 --- Reorder Out-of-Order Messages

## Problem

Messages have monotonic sequence numbers but can arrive out of order
within a bounded window. Emit strictly in sequence.

## ELI5

Book pages arrive `3, 2, 1`. Put future pages aside until the missing
page arrives; then read the newly contiguous run.

## Invariant

``` text
Everything < nextExpected has already been emitted exactly once.
Only nextExpected may leave next.
```

## Minimum state

``` text
nextExpected
buffer of early messages
```

## Mechanism

``` text
seq < nextExpected → stale/duplicate
seq > nextExpected → buffer
seq = nextExpected → emit

after emitting:
while buffer contains nextExpected:
    emit
    remove
    nextExpected++
```

## Why `while`, not `if`

If `{2,3,4}` are already buffered when `1` arrives, all four become
immediately emit-able.

## Data-structure trade-off

**HashMap:** exact lookup of `nextExpected`, expected `O(1)`.\
**TreeMap:** `O(log W)` but ordered diagnostics/range inspection are
easier.\
**PriorityQueue:** can show minimum buffered sequence, but you still
need `nextExpected`; minimum available is not necessarily safe to emit.

## Complexity

TreeMap version:

``` text
O(N log W) time
O(W) space
```

HashMap version:

``` text
expected O(N) time
O(W) space
```

## Common confusions

**Gap detection vs reordering:** detecting `3` is missing is different
from buffering `4` until `3` arrives.

**Sorting vs streaming:** there may be no end to the stream.

## What not to do --- and why

-   Do not emit smallest buffered message unless it equals
    `nextExpected`.
-   Do not increment expected merely because a future message arrived.
-   Do not permit unlimited buffering if a sequence can disappear
    forever.
-   Do not overwrite conflicting duplicates silently unless that
    behavior is defined.

## Production reality

Missing sequence timeout may trigger retransmission or a fresh snapshot.

## Mutation

**Missing sequence never arrives:** add timeout → retransmission →
snapshot recovery. This connects directly to DSA-164.

## Memory hook

``` text
HOLE → PARK → FILL → DRAIN
```

------------------------------------------------------------------------

------------------------------------------------------------------------

# STAGE 8 --- CONSTANT-TIME RISK VALIDATION

> **Why this comes now:** Apply keyed latest state to a trading
> validation rule.

# DSA-154 --- Sliding Price Deviation Check

## Problem

Reject/flag an order whose price deviates too far from the latest
reference price.

## ELI5

A product normally costs ₹100. Someone submits ₹160. Compare how far the
submitted price is from the trusted reference and reject if the
permitted band is exceeded.

## Formula

``` text
deviation = abs(orderPrice - referencePrice) / referencePrice
```

## Invariant

The reference map contains the latest valid reference price for each
instrument.

## State

``` text
instrument → reference price
```

## Complexity

With HashMap:

``` text
reference update   expected O(1)
check              expected O(1)
space              O(instruments)
```

## Trade-offs

**double:** easy for interview demonstration, but binary floating-point
can surprise monetary comparisons.\
**scaled long / integer ticks:** deterministic and fast.\
**BigDecimal:** precise decimal semantics but heavier.

## Common confusions

`5%` can mean `5` or `0.05` depending API design. Make units explicit.

A stale reference price can make an algorithm mathematically correct but
operationally unsafe.

## What not to do --- and why

-   Do not divide by zero.
-   Do not silently accept an order when no reference exists unless
    policy explicitly says so.
-   Do not mix percentages and fractions.
-   Do not ignore reference freshness in a production discussion.

## Mutation

**Different thresholds by instrument/account:** threshold becomes keyed
configuration rather than a method constant.

## Memory hook

``` text
ORDER vs REFERENCE → NORMALIZED DISTANCE
```

------------------------------------------------------------------------

------------------------------------------------------------------------

# STAGE 9 --- EVENT-DRIVEN STATE MACHINES

> **Why this comes now:** Upgrade `key → value` into
> `key → lifecycle state` with legal transitions.

# DSA-158 --- Order State Aggregation

## Problem

Given `NEW/ACK/FILL/CANCEL/REJECT` events, derive current/final order
state.

## ELI5

A parcel moves through lifecycle states. An event does not stand alone;
its meaning depends on what already happened.

## Model

``` text
old state + event → new state
```

This is a finite state machine.

## State

Per order:

``` text
original quantity
filled quantity
current lifecycle state
```

## Core invariants

``` text
filledQty <= originalQty
terminal orders do not transition
unknown orders cannot receive normal lifecycle events
duplicate NEW is invalid
FILLED when filledQty == originalQty
```

## Complexity

``` text
expected O(1) per event
O(number of orders) space
```

## Trade-offs

**switch statement:** simple for small state machines.\
**transition table:** more declarative/scalable.\
**State pattern:** useful when states have substantial behavior, often
overkill for interview code.

## Common confusions

`ACK` is not `FILL`.\
`CANCEL` after partial fill leaves executed quantity real even though
remaining quantity is canceled.\
"Final state" and "current state" differ while stream is incomplete.

## What not to do --- and why

-   Do not merely store the last event as state.
-   Do not allow overfill.
-   Do not transition from terminal states without explicit correction
    semantics.
-   Do not ignore event ordering assumptions.

## Mutation

**Events can arrive out of order:** now lifecycle aggregation must
compose with sequence/reordering logic.

## Memory hook

``` text
LIFECYCLE EVENTS → STATE MACHINE
```

------------------------------------------------------------------------

------------------------------------------------------------------------

# STAGE 10 --- MULTI-STREAM ORDERING

> **Why this comes now:** Reuse heap priority with one candidate per
> already-ordered source.

# DSA-163 --- Merge Multiple Ordered Market Feeds

## Problem

Merge `K` individually ordered streams into one globally ordered stream.

## ELI5

K checkout lines are each internally ordered. The next global customer
must be at the front of one of those K lines. You never need to inspect
everyone.

## Key invariant

The globally smallest remaining event must be the current head of one of
the K feeds.

## State

A min-heap containing at most one current candidate from each feed.

## Mechanism

``` text
push first event from each nonempty feed

while heap not empty:
    smallest = poll
    emit smallest

    advance that same feed

    if it has another event:
        push next
```

## Complexity

For `N` total events:

``` text
O(N log K) time
O(K) heap space
```

## Trade-offs

**Min heap:** standard general K-way merge.\
**Repeated scan of K heads:** `O(NK)`, only attractive when K is tiny.\
**Pairwise merge:** can also achieve good asymptotics but is less
natural for live streaming.

## Common confusions

Each source must already satisfy its own ordering guarantee. This
algorithm does not repair arbitrary disorder within a feed.

Timestamp ties need deterministic tie-breaking, e.g. feed ID + feed
sequence.

## What not to do --- and why

-   Do not push all N events into the heap: space becomes `O(N)` and
    wastes the sorted-source property.
-   Do not sort the concatenated dataset: `O(N log N)` and
    non-streaming.
-   Do not advance the wrong feed after polling.

## Mutation

**One feed temporarily stalls:** global event-time ordering may require
watermark/latency policy; pure K-way merge assumes heads are available.

## Memory hook

``` text
K SORTED SOURCES → HEAP OF K HEADS
```

------------------------------------------------------------------------

------------------------------------------------------------------------

# STAGE 11 --- RECOVERY / REPLAY

> **Why this comes now:** Compose state + sequence validation +
> incremental replay.

# DSA-164 --- Snapshot + Incremental Merge

## Problem

Start from a snapshot valid through sequence `S`, then apply ordered
incremental updates after `S`.

## ELI5

You receive a saved game at level 10 plus a log of actions from level 11
onward. Do not replay actions already included in the save; apply only
newer actions in order.

## Invariant

After applying update sequence `X`, state represents every valid update
through `X` exactly once.

## Mechanism

``` text
state = copy(snapshot.state)
last = snapshot.sequence

for update:
    if update.seq <= snapshot.seq:
        ignore as already represented

    require update.seq > last
    apply update
    last = update.seq
```

## Complexity

If snapshot copy has `S` entries and `U` updates:

``` text
O(S + U) time
O(S) resulting state
```

## Critical subtlety: ordered is not necessarily contiguous

Checking only:

``` text
update.seq > last
```

detects duplicate/reversal but does **not** detect a missing sequence.

If protocol requires contiguous increments, require:

``` text
update.seq == last + 1
```

Otherwise `101, 102, 104` silently skips 103.

## Trade-offs

**Strict contiguous validation:** safer when every sequence matters.\
**Monotonic-only validation:** valid if sequence gaps can legitimately
represent irrelevant events or protocol semantics allow them.\
**Copy snapshot:** preserves caller state but costs memory/time.\
**In-place:** cheaper but mutates source.

## Common confusions

Snapshot sequence defines what is already included. Snapshot wall-clock
timestamp is not necessarily a safe substitute.

## What not to do --- and why

-   Do not reapply updates already represented by snapshot.
-   Do not silently apply through a required sequence gap.
-   Do not apply unordered incrementals without
    buffering/sorting/recovery.
-   Do not mutate the snapshot if immutability is part of the contract.

## Production recovery

``` text
gap detected
→ request retransmission
→ if unavailable, request new snapshot
→ resume after snapshot sequence
```

This composes DSA-151 + DSA-152 + DSA-164.

## Mutation

**Incrementals arrive while snapshot is downloading:** buffer them,
install snapshot atomically, discard increments covered by snapshot,
then replay contiguous newer increments.

## Memory hook

``` text
SNAPSHOT S → APPLY ONLY > S → VERIFY SEQUENCE
```

------------------------------------------------------------------------

------------------------------------------------------------------------

# STAGE 12 --- CAPSTONE MATCHING ENGINE

> **Why this comes now:** Compose ordered prices, price-time priority,
> mutable quantities, and event processing.

# DSA-159 --- Match Buy and Sell Orders

## Problem

Process incoming buy/sell limit orders and generate trades according to
price-time priority.

## ELI5

Buyers want the lowest acceptable seller; sellers want the highest
acceptable buyer. Better prices win, and at the same price the person
who queued first wins.

## Crossing rules

Incoming BUY can trade when:

``` text
buy.limit >= bestAsk
```

Incoming SELL can trade when:

``` text
sell.limit <= bestBid
```

## Invariants

``` text
best bid = highest-priority resting buy
best ask = highest-priority resting sell
same-price orders preserve FIFO
only crossing prices trade
quantities never become negative
unfilled remainder rests in the book
```

## Derivation

This composes DSA-149 with quantity consumption.

Minimal interview structure:

``` text
PriorityQueue bids
PriorityQueue asks
```

Production-shaped structure:

``` text
TreeMap<Price, FIFO Queue<Order>>
+ orderId index
```

## Incoming BUY mechanism

``` text
while remaining > 0
  and ask exists
  and buyPrice >= bestAskPrice:

    resting = best ask
    traded = min(incomingRemaining, restingRemaining)

    emit trade
    subtract from both

    if restingRemaining == 0:
        remove resting

if incomingRemaining > 0:
    rest incoming on bid side
```

SELL is symmetric.

## Trade price

For this exercise, state the assumption:

``` text
trade executes at resting order's price
```

Do not leave execution-price semantics implicit.

## Complexity

For heap version, inserting/removing an order is `O(log N)`. If an
incoming order consumes `M` resting orders, matching is approximately
`O(M log N)` plus eventual insertion.

For a price-level tree:

``` text
price-level lookup/update O(log P)
FIFO head operations      O(1)
```

## Trade-offs

**Two heaps:** concise, good for basic matching, poor arbitrary
cancellation/amendment.\
**TreeMap\<price, deque\>:** natural price levels and FIFO.\
**Order-ID index:** needed for fast cancellation.\
**Specialized structures:** used in latency-sensitive systems to avoid
GC/cache misses.

## Common confusions

-   Best bid and best ask do not automatically trade if they do not
    cross.
-   Incoming order and resting order are not interchangeable when
    deciding execution price.
-   Partial fill does not remove the order unless remaining quantity
    reaches zero.
-   Price-time priority is not simply timestamp sorting.

## What not to do --- and why

-   Do not scan the whole book for every match.
-   Do not mutate comparator fields of an order still inside a heap.
-   Do not lose FIFO at equal price.
-   Do not reinsert a partially consumed resting order in a way that
    changes its time priority.
-   Do not use floating prices casually in comparator logic.

## Production reality

Real engines must define cancellation, amendments, market orders,
self-trade prevention, tick sizes, session state, deterministic
sequencing, persistence/recovery, and concurrency ownership.

## Mutation

**Add cancellation by order ID:** two heaps alone become awkward;
introduce order index/lazy deletion or move to price-level queues.

## 60-second answer

Maintain buy and sell sides ordered by price-time priority. An incoming
buy repeatedly matches the best ask while its limit crosses that ask; an
incoming sell symmetrically matches the best bid. Each trade consumes
the minimum remaining quantity. Fully consumed resting orders are
removed; any incoming remainder rests on its side. For a simple
interview implementation heaps work, while a production-shaped book uses
ordered price levels with FIFO queues and an order-ID index.

## Memory hook

``` text
CROSS BEST OPPOSITE → CONSUME → REPEAT → REST REMAINDER
```

------------------------------------------------------------------------

------------------------------------------------------------------------

# STAGE 13 --- PERFORMANCE STATISTICS

> **Why this comes now:** Finish with a new decision axis: exact vs
> approximate, offline vs streaming, memory bounded vs unbounded.

# DSA-165 --- Order Latency Percentile

## Problem

Calculate p50/p95/p99 latency under stated exactness and memory
constraints.

## ELI5

Average waiting time can hide a few terrible waits. Percentiles ask:
"How fast were 95% or 99% of requests?"

## First question before algorithm

Ask:

``` text
Exact or approximate?
Offline or streaming?
One percentile or many?
Memory bounded?
Latency range bounded?
```

These determine the solution.

## Case A --- exact offline, simplest

Sort:

``` text
O(N log N)
```

Then index the requested percentile.

Best when simplicity matters and N fits comfortably in memory.

## Case B --- exact single percentile

Quickselect:

``` text
expected O(N)
worst O(N²) for naive pivot strategy
```

Useful when only one/few order statistics are needed.

## Case C --- streaming / bounded memory

Exact arbitrary quantiles require substantial state. Discuss approximate
structures such as:

``` text
t-digest
HDR Histogram
KLL sketch
DDSketch
```

Choice depends on error guarantees and latency domain.

## Nearest-rank definition

For percentile `P` and `N` values:

``` text
rank = ceil(P/100 × N)
```

Use zero-based index `rank - 1`.

State the definition because percentile conventions differ.

## Trade-offs

**Sort:** simplest, all percentiles cheap afterward.\
**Quickselect:** great for one exact percentile; mutates/copies input
and repeated percentiles can reduce its advantage.\
**Histogram:** excellent if value range/buckets are controlled.\
**Sketch:** bounded memory and streaming, approximate.

## Common confusions

p99 is not "99% of the maximum."\
p50 is a median convention, not an average.\
Different libraries may interpolate percentiles differently.

## What not to do --- and why

-   Do not use arithmetic mean as p50.
-   Do not claim Quickselect has guaranteed `O(N)` unless using a
    deterministic linear-time selection algorithm.
-   Do not promise exact p99 from a tiny bounded-memory stream without
    additional assumptions.
-   Do not omit the percentile definition.

## Mutation

**Need p50/p95/p99 continuously for millions of events/minute:** switch
discussion from repeated sorting/Quickselect to histogram/sketch design.

## Memory hook

``` text
PERCENTILE → ASK EXACT? STREAMING? MEMORY? → SORT / SELECT / SKETCH
```

------------------------------------------------------------------------

------------------------------------------------------------------------

# PART III --- Cross-Problem Pattern Compression

Do not memorize 19 independent answers. Compress them into these
triggers.

## Identity

``` text
Have I seen this?
→ HashSet

Current state for this key?
→ HashMap
```

Problems: 150, 153, 160, 161.

## Ordered keys

``` text
Arbitrary update + min/max/range traversal
→ TreeMap
```

Problems: 147, 148, production-shaped 159.

## Best candidate

``` text
Repeatedly need best candidate, not full order
→ PriorityQueue
```

Problems: 149, 162, 163, simplified 159.

## Sliding expiry

``` text
Oldest event expires first
→ Deque
```

Problems: 155, 156, 162.

## Sliding extreme

``` text
Window max/min
→ Monotonic deque
```

Problem: 157.

## Lifecycle

``` text
entity + ordered events → state transition
→ State machine
```

Problem: 158.

## Sequence correctness

``` text
must not skip/reorder
→ nextExpected / lastSequence + buffer/recovery
```

Problems: 151, 152, 164.

## K sorted streams

``` text
global next is among K heads
→ min heap
```

Problem: 163.

## Repeated aggregate

``` text
new answer differs from old answer by small delta
→ update incrementally; don't recompute
```

Problems: 153, 156, 160, 162.

------------------------------------------------------------------------

# PART IV --- The Most Important Trade-Off Table

  -------------------------------------------------------------------------
  Need                Default            Why               Watch out for
  ------------------- ------------------ ----------------- ----------------
  Membership/dedupe   HashSet            Expected O(1)     memory,
                                                           durability

  Key → aggregate     HashMap            Expected O(1)     composite keys,
                                                           concurrency

  Ordered price keys  TreeMap            min/max/range +   O(log N),
                                         updates           allocations

  Repeated best       PriorityQueue      O(1) peek, O(log  arbitrary
  candidate                              N) update         removal

  FIFO expiry         ArrayDeque         O(1) ends         assumes ordered
                                                           arrival

  Sliding max/min     Monotonic deque    O(N) total        invariant is
                                                           easy to break

  Lifecycle           state machine      explicit          invalid
                                         correctness       transitions

  Sequence reorder    expected + buffer  streaming         missing forever
                                         correctness       

  Merge K sorted      min heap           only K candidates source ordering
                                                           assumption

  Exact percentile    sort/select        exact             memory/time

  Streaming           sketch/histogram   bounded state     approximation
  percentile                                               
  -------------------------------------------------------------------------

------------------------------------------------------------------------

# PART V --- Common Confusions Across the Entire Set

## Heap vs TreeMap

Use a heap when you mainly need the best candidate.

Use TreeMap when you need arbitrary ordered-key updates, level
traversal, min/max, floor/ceiling, or range operations.

## HashMap vs TreeMap

HashMap answers **"what value belongs to this key?"**

TreeMap additionally answers **"where is this key in order?"**

Do not pay `O(log N)` for ordering you never use.

## Queue vs Deque vs Monotonic Deque

A queue preserves FIFO.

A deque lets both ends change.

A monotonic deque is a deque with an additional ordering invariant that
aggressively deletes dominated candidates.

## Event time vs arrival time vs sequence

They are different:

``` text
event time   = when business event happened
arrival time = when your system received it
sequence     = protocol ordering identity
```

Do not casually substitute one for another.

## Exactly-once vs idempotency

Networks commonly provide retries/duplicates. "Exactly once" is usually
achieved by making processing idempotent and coordinating durable state,
not by assuming exactly one delivery.

## Amortized O(1)

A loop inside another loop is not automatically `O(N²)`. If each item
enters and leaves a deque once, total work is `O(N)`.

------------------------------------------------------------------------

# PART VI --- What Not to Do --- Global Rules

1.  **Do not start coding before naming the invariant.**
2.  **Do not choose a data structure because its name resembles the
    problem.**
3.  **Do not sort a stream repeatedly when state can be maintained
    incrementally.**
4.  **Do not scan an entire collection for an answer needed after every
    event.**
5.  **Do not use floating-point money casually in production
    discussions.**
6.  **Do not ignore duplicate, stale, missing, or out-of-order events.**
7.  **Do not claim expected HashMap complexity as a hard real-time
    guarantee.**
8.  **Do not call an in-memory solution crash-safe or distributed.**
9.  **Do not ignore unbounded memory growth.**
10. **Do not optimize into exotic structures before requirements justify
    them.**
11. **Do not state complexity only for the whole algorithm when
    interviewers care about per-operation latency.**
12. **Do not memorize implementation lines without understanding why
    each piece of state exists.**

------------------------------------------------------------------------

# PART VII --- Complexity Cheat Sheet

``` text
147 Best Bid/Ask
TreeMap update O(log P), extreme lookup O(log P), space O(P)

148 Top N Levels
update O(log P), query O(log P + N), space O(P)

149 Price-Time Queue
offer O(log N), peek O(1), poll O(log N), space O(N)

150 Execution Dedupe
expected O(1)/event, O(U) space

151 Sequence Gap
O(N) time, O(1) extra excluding output

152 Reorder Messages
TreeMap O(N log W), HashMap expected O(N), O(W) space

153 Rolling Exposure
expected O(1)/event, O(keys) space

154 Price Deviation
expected O(1)/event, O(instruments) space

155 Exchange Throttle
amortized O(1)/request, retained-window space

156 Rolling VWAP
amortized O(1)/trade, O(W) space

157 Sliding Maximum
O(N) time, O(K) space

158 Order State
expected O(1)/event, O(orders) space

159 Matching Engine
heap operations O(log N);
matching M resting orders ≈ O(M log N)

160 Position
expected O(1)/execution, O(keys) space

161 Duplicate Orders
expected O(1)/order, O(U) space

162 Most Active
amortized O(1) ingest;
O(M log K) Top-K query;
O(W + M) space

163 Merge K Feeds
O(N log K), O(K) heap

164 Snapshot + Incrementals
O(S + U), O(S) resulting state

165 Percentile
sort O(N log N);
Quickselect expected O(N);
streaming sketch depends on chosen structure
```

------------------------------------------------------------------------

# PART VIII --- Java Retrieval Cheat Sheet

``` java
// Membership
Set<K> seen = new HashSet<>();
boolean firstTime = seen.add(key);

// Aggregation
map.merge(key, delta, Long::sum);

// Ordered keys
TreeMap<Integer, Long> book = new TreeMap<>();
book.firstKey();
book.lastKey();
book.descendingMap();

// Heap
PriorityQueue<T> pq = new PriorityQueue<>(comparator);
pq.offer(x);
pq.peek();
pq.poll();

// FIFO / sliding expiry
Deque<T> dq = new ArrayDeque<>();
dq.addLast(x);
dq.peekFirst();
dq.pollFirst();

// Composite comparator
Comparator<Order> buy =
    Comparator.comparingInt((Order o) -> o.price)
              .reversed()
              .thenComparingLong(o -> o.sequence);
```

Do not memorize these as isolated syntax. Attach each to an operation
requirement.

------------------------------------------------------------------------

# PART IX --- Requirement-Mutation Drill

Once a base solution is easy, mutate it.

``` text
147 Best Bid/Ask
→ arbitrary order cancellation

148 Top N Levels
→ Top N by quantity rather than price

149 Price-Time
→ amend order loses time priority

150 Dedup
→ bounded memory + crash recovery

151 Gap
→ input itself is out of order

152 Reorder
→ missing sequence timeout

153 Exposure
→ account + global limits

154 Deviation
→ stale reference + asymmetric bands

155 Throttle
→ per-account + global + burst allowance

156 VWAP
→ per-symbol time windows

157 Sliding Max
→ min and max simultaneously

158 State Aggregation
→ correction/bust events

159 Matching
→ market orders + cancellation

160 Position
→ average cost + realized P&L

161 Duplicate Orders
→ identity scope changes by session

162 Top K
→ query after every event

163 Merge Feeds
→ one source stalls

164 Snapshot Merge
→ incrementals arrive during snapshot download

165 Percentile
→ continuous p50/p95/p99 with 1 MB memory
```

A mutation is where memorized code stops helping and actual
understanding becomes visible.

------------------------------------------------------------------------

# PART X --- Blind Reconstruction Template

Copy this section once per problem during practice.

``` text
PROBLEM:

ELI5 IN ONE SENTENCE:

INPUT SHAPE:

INVARIANT:

REQUIRED OPERATIONS:

CONSTRAINT THAT KILLS NAIVE APPROACH:

NAIVE SOLUTION:

WHY IT FAILS:

DATA STRUCTURE:

WHY THIS DS:

MINIMUM STATE:

ONE-EVENT TRANSITION:

PSEUDOCODE:

TIME COMPLEXITY:

WHY COMPLEXITY IS TRUE:

SPACE COMPLEXITY:

TRADE-OFF:

COMMON CONFUSION:

WHAT NOT TO DO:

EDGE CASES:

PRODUCTION DIFFERENCE:

FOLLOW-UP MUTATION:

60-SECOND EXPLANATION:

ONE-LINE MEMORY HOOK:
```

------------------------------------------------------------------------

# PART XI --- 80:20 Core

If time is limited, be able to reconstruct these cold:

``` text
147 TreeMap / ordered price levels
149 comparator + priority
150 HashSet idempotency
152 sequence buffer
155 sliding-window deque
156 incremental rolling aggregate
157 monotonic deque
159 matching engine
163 K-way merge
```

Then add **165 percentile** for latency/performance discussions.

The remaining problems become variations of these primitives.

------------------------------------------------------------------------

# PART XII --- Final Mental Decision Tree

``` text
Do I need to know whether identity was seen?
→ HashSet

Do I need current value by identity?
→ HashMap

Do keys need ordering/min/max/range?
→ TreeMap

Do I repeatedly need only the best candidate?
→ PriorityQueue

Do old events expire FIFO?
→ Deque

Do I need max/min inside a moving window?
→ Monotonic deque

Do events move an entity through lifecycle states?
→ State machine

Can events be missing/reordered?
→ sequence invariant + buffer/recovery

Are there K already-sorted sources?
→ heap of K heads

Am I recalculating nearly the same aggregate repeatedly?
→ maintain running state incrementally

Do I need a percentile?
→ exact/approximate + offline/streaming + memory
→ sort / select / histogram / sketch
```

------------------------------------------------------------------------

# PART XIII --- Final Reconstruction Rule

Before writing Java, you should be able to say:

> **"I need these operations under these constraints. Therefore this
> data structure maintains this invariant with this state and this
> complexity."**

That sentence is the skill.

The code is only its consequence.
