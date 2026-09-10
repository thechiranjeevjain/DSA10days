# Five Java Files — Review, Corrections and Interview Alignment

Files reviewed:

```text
RiskLimitEngine.java
RiskLimitEngineAsync.java
RiskLimitEnginePartitioned.java
RiskLimitEnginePartitionedAsync.java
MatchingEngine.java
```

All five compile together with Java 17.

The important review is therefore semantic: correctness, concurrency guarantees, naming and whether the comments accurately describe what the code really does.

---

# Executive ranking

| File | Best use | Status |
|---|---|---|
| `RiskLimitEngine.java` | Primary 30–40 min risk-engine answer | Strong foundation; fix correctness details |
| `RiskLimitEngineAsync.java` | Event-bus integration follow-up | Keep; it is synchronous dispatch despite name |
| `RiskLimitEnginePartitioned.java` | Ownership/sharding bridge | Keep; fix misleading lock-domain claims |
| `RiskLimitEnginePartitionedAsync.java` | Advanced event-loop follow-up | Intended design strong; current implementation violates strict single-owner rule |
| `MatchingEngine.java` | Separate trading-domain coding drill | Good structures; fix resting/aggressor price logic and market-order behavior |

---

# 1. RiskLimitEngine.java

## What is strong

The structure maps cleanly to the attached class diagrams:

```text
RiskEngine
   ↓
RiskCheckGroup
   ↓
List<RiskCheck>
   ├── MaxQtyCheck
   ├── TotalTradedPerTimeCheck
   └── KillSwitchCheck
```

The concurrency mechanism is also easy to explain:

```java
ConcurrentHashMap<Integer, RiskCheckGroup>
```

for lookup, then:

```java
synchronized (group)
```

around the compound group operation.

This means:

```text
different groups -> can progress independently
same group       -> one compound state transition at a time
```

The `begin() / rollback()` design expresses an important invariant: if one check mutates state and a later check rejects, all check consumption returns to the previous snapshot.

---

## Fix 1 — `MaxQtyCheck` does not filter on its ticker

The class stores a ticker, but `check()` does not first verify that the order belongs to that ticker.

Current conceptual problem:

```text
MaxQty[AAPL]
+ incoming BUY MSFT
→ can still consume AAPL check
```

Add:

```java
if (!order.ticker.equals(ticker)) {
    return Result.PASS;
}
```

before applying quantity logic.

This is a real correctness issue.

---

## Fix 2 — define position semantics explicitly

Current check ignores SELL orders.

That is valid only if the exercise explicitly defines the limit as accumulated BUY quantity.

If the requirement is true signed position:

```text
BUY  -> +qty
SELL -> -qty

candidatePosition = oldPosition + signedQty
abs(candidatePosition) <= limit
```

Pick one interpretation and say the assumption before coding.

---

## Fix 3 — notional overflow

Current:

```java
long notional = order.price * order.quantity;
```

can overflow.

Safer:

```java
long notional = Math.multiplyExact(order.price, order.quantity);
```

and use `Math.addExact` for candidate accumulation if the input domain does not already guarantee bounds.

---

## Fix 4 — name reflects semantics

`TotalTradedPerTimeCheck` executes on order validation before an actual trade.

If the exercise means "order notional submitted in the last second", call it something like:

```text
NotionalPerSecondCheck
```

If it truly means traded notional, update it from trade events instead.

---

## Fix 5 — registration/config ordering

`register()` replaces the group in the map.

If registration is startup-only, say so.

If configuration can change concurrently with orders, define its atomicity:

```text
same lock
or
immutable/versioned replacement
or
same owner event queue
```

---

# 2. RiskLimitEngineAsync.java

## Good

It cleanly introduces integration events:

```text
OrderEvent
ConfigEvent
CommandEvent
     ↓
 MessageBus
     ↓
RiskEngineListener
     ↓
 RiskEngine
```

This is useful architecture practice.

---

## Important: it is not asynchronous execution

`MessageBus.publish()` directly invokes handlers.

So:

```text
event-driven?       YES
message-bus style?  YES
new worker thread?  NO
async dispatch?     NO
```

`CompletableFuture` is the reply container, not the source of parallelism.

Best spoken line:

> This version introduces an event integration boundary. Dispatch is still synchronous and in-process.

---

## Kill-switch visibility / ordering

`KillSwitchCheck.active` is mutated from a command handler while order validation synchronizes on the group.

If publishers can call the bus from different threads, the command mutation does not automatically share the same ordering boundary as order validation.

Defensible fixes:

```text
same group lock
or
volatile for visibility + explicit ordering semantics
or
route command through same owner thread
```

The last is best for the later single-owner design.

---

## Config mutation

Adding/replacing checks while order evaluation is active requires defined semantics.

For a simple exercise, declare:

> configuration is installed before order processing starts.

Then introduce dynamic updates only as a follow-up.

---

# 3. RiskLimitEnginePartitioned.java

## Good

This introduces a useful ownership map:

```text
accountId -> partition
```

That helps explain how the system can later evolve toward partition-owned workers.

---

## Correct the "four lock domains" explanation

Level 1 already has one `synchronized(group)` lock per risk group.

So Level 3a does **not** transform one global lock into four locks.

Accurate wording:

> Partitioning introduces an explicit state-sharding boundary, but validation still occurs on caller threads and each group still protects its own compound mutation.

This file is therefore a structural bridge, not yet a true single-owner event-loop implementation.

---

## HashMap assumption

Each partition uses a plain `HashMap`.

That is acceptable if configuration completes before concurrent order processing and the map is then read-only structurally.

If registration occurs concurrently, define a safe ownership or synchronization model.

---

## Inheritance note

`PartitionedRiskEngine extends RiskEngine`, but effective group state is stored in its partitions rather than the base engine map.

Not a correctness bug, but composition may be conceptually cleaner.

Do not refactor this during a timed interview unless asked.

---

# 4. RiskLimitEnginePartitionedAsync.java

## Intended architecture is strong

The intended model is:

```text
ownership key
   ↓
partition
   ↓
bounded queue
   ↓
one worker
   ↓
owned mutable state
```

This is a strong concurrency answer.

The bounded queue and explicit overload rejection are also good signals.

---

## Critical issue — the current code is not truly single-owner

The order worker intentionally avoids `synchronized` because it says the worker is the only thread touching state.

But other mutations occur outside that worker:

```text
ConfigEvent      -> register directly
KillSwitchEvent  -> activate/deactivate directly
Cancel           -> inherited direct cancel path
```

Therefore:

> the partition worker is **not** currently the only state mutator.

This invalidates the strongest "zero-lock because one owner" claim.

---

## Correct design

Route every mutation into the owning partition:

```text
OrderEvent ───────┐
CancelEvent ──────┤
ConfigEvent ──────┼──> partition queue
KillSwitchEvent ──┘
                         ↓
                      worker
                         ↓
                       state
```

Then `groups`, checks and command state can genuinely be worker-owned.

---

## Cancel race

The inherited cancel path takes `synchronized(group)`.

The worker order path does not.

A lock only protects code that cooperates by using the same lock, so cancel can still race with worker mutation.

Routing cancel through the owner queue is the clean fix.

---

## Broadcast routing inefficiency

Every partition subscribes to every `OrderEvent` and then ignores events it does not own.

That makes dispatch proportional to number of partitions.

Simpler:

```java
partitionFor(account).offer(event);
```

Route once.

For four partitions this is irrelevant performance-wise, but direct routing is easier to explain.

---

## Worker exception handling

If a runtime exception escapes `g.check(...)`, the worker may die and a caller can wait forever on its future.

A production-grade ownership loop needs a per-event failure policy:

```text
catch
→ reject / complete exceptionally
→ log/metric
→ decide whether worker remains safe to continue
```

A timed CoderPad version only needs to mention it.

---

## Timestamp mismatch

`validate(order, nowMs)` creates an `OrderEvent` that captures a new `System.currentTimeMillis()`.

The passed timestamp is therefore ignored.

Either carry `nowMs` into the event or remove the misleading parameter.

---

# 5. MatchingEngine.java

## What is strong

The core data structures are exactly what you want to remember:

```text
bids -> TreeMap descending
asks -> TreeMap ascending
same price -> FIFO queue
```

That naturally gives:

```text
price priority
then
time/FIFO priority
```

Partial fills are represented by reducing remaining quantity.

---

## Critical execution-price issue

Current flow:

```text
add incoming to book
then
match best bid vs best ask
```

After insertion, the matching method does not know which order was incoming/aggressive and which was resting.

For two limit orders it defaults to bid price.

That is wrong for:

```text
resting SELL @100
incoming BUY @105
```

A typical price-time book would execute at the resting order's price, 100.

---

## Cleaner 30-minute algorithm

```text
incoming order
   ↓
match it against opposite resting book
   ↓
trade at RESTING order price
   ↓
decrease quantities
   ↓
if incoming quantity remains
   ↓
rest remaining LIMIT order on own book
```

This also naturally handles market-order semantics better.

---

## Market orders should not normally rest

Current code gives market orders synthetic extreme prices and adds them to the book before matching.

If there is insufficient opposite liquidity, a remaining market order can stay on the book.

For a clean interview implementation:

```text
market order
→ consume available opposite liquidity
→ cancel/reject any remainder according to stated semantics
→ never rest as a market order
```

Or omit market orders entirely from V1 and add them only if asked.

---

# Recommended interview progression using these files

```text
PRIMARY
RiskLimitEngine.java
   ↓
fix ticker + side semantics + overflow

FOLLOW-UP 1
RiskLimitEngineAsync.java
   ↓
event integration, still synchronous

FOLLOW-UP 2
RiskLimitEnginePartitioned.java
   ↓
explicit ownership/sharding boundary

FOLLOW-UP 3
corrected conceptual RiskLimitEnginePartitionedAsync
   ↓
all mutations through one partition owner

SEPARATE DRILL
MatchingEngine.java
```

---

# The class diagram and code now align like this

```text
RiskEngine
   ↓
RiskCheckGroup
   ↓
RiskCheck[]
   ├── MaxQtyCheck
   ├── NotionalPerSecondCheck
   └── KillSwitchCheck
```

Concurrency evolution:

```text
group lock
   ↓
explicit partitions
   ↓
one owner per partition
```

Do not add abstraction merely to make the diagram impressive. The interviewer should be able to see why every class exists.

---

# One sentence to remember

> **Concurrent collections protect data-structure operations; the business invariant still needs one atomic ownership boundary.**
