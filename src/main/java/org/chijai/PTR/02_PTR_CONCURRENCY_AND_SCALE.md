# PTR Concurrency and Scale — Interview Defense

This file answers one question deeply:

> **"Wait — single-threaded / single-owner in an exchange risk path? How does that scale?"**

The most important discipline is separating **what is established about actual PTR** from **how you would design concurrency if asked**.

---

# 1. What is actually established about PTR

The source material supports these statements:

```text
PTR is stateful
PTR is event-driven
PTR keeps the hot risk path in memory
matching engine synchronously waits for the decision
risk plugin executes inside the matching-engine JVM
hot path avoids DB calls and remote-service dependencies
single-owner mutable state is used to reduce synchronization complexity
event ordering becomes easier to reason about
design objective is predictable tail latency, not maximum parallel throughput
```

This is enough to defend the design intelligently.

## Source-safe answer

> Single-owner mutable state is deliberate. The next risk decision depends on previous events, so the state has ordering constraints. Allowing several threads to mutate that state creates synchronization and makes event ordering harder to reason about. PTR instead optimizes for a predictable synchronous decision on the matching-engine critical path.
>
> The important distinction is that **single-owner state does not mean the entire exchange has no concurrency**. It means one invariant-bearing state domain has one mutation owner. If more throughput is required, I would parallelize independent ownership domains rather than introduce shared-state contention.

---

# 2. What the available PTR sources do NOT establish

Do not state the following as actual PTR facts unless you have separate source/code evidence:

- exactly one matching-engine thread per instrument;
- every instrument has its own independent ME instance;
- exact number of instruments per process;
- exact thread count;
- exact absence of every `synchronized` or `ConcurrentHashMap` on the real hot path;
- exact account/instrument partition scheme;
- exact command-bus serialization of admin/config changes onto the ME thread;
- an exact once-per-second background timer model;
- exact microsecond latency figures;
- exact orders/messages per second;
- exact scale-out topology.

These can be **excellent design explanations** without being presented as deployed facts.

---

# 3. The core mental model

```text
BAD QUESTION:
"How many threads can I add?"

BETTER QUESTION:
"What state must preserve one ordered invariant?"
```

If two operations mutate the same state and the result of operation 2 depends on operation 1, you have an ordering problem.

There are two broad solutions:

```text
A. shared-state concurrency
   several threads
   + synchronization/CAS/locks

B. ownership
   one mutation owner per state domain
   + parallel owners for independent domains
```

For a latency-sensitive ordered path, ownership is often simpler to reason about.

---

# 4. Why shared mutable state can hurt tail latency

## Locks / contention

If multiple threads contend for the same state, one waits.

```text
fast operation
+ occasional waiting
= low average can still have poor p99
```

## Cache coherence

Two cores repeatedly writing the same cache line require coherence traffic. The cost is not visible in business code, but it affects consistency of latency.

## Scheduling / handoff

Moving work between threads adds queueing and scheduling. Even if average cost is small, variance matters on a synchronous critical path.

## Ordering complexity

With a single owner:

```text
event 1
event 2
event 3
```

is naturally one sequence.

With several mutators, you need an explicit mechanism to re-establish the required order.

## Failure complexity

The more independently executing mutators you have, the harder rollback, replay and recovery semantics become.

---

# 5. One-paragraph answer

> Single-owner state is the design, not simply a limitation. PTR is stateful, and the matching engine synchronously waits for its decision, so predictable tail latency and deterministic ordering matter more than maximizing thread-level parallelism inside one state domain. A single mutation owner avoids lock contention and makes event ordering easier to reason about. If throughput required more parallelism, I would partition independent ownership domains and run those partitions concurrently while preserving one ordered owner inside each partition.

This is the safest default wording.

---

# 6. If interviewer says: "But exchanges need concurrency"

> Absolutely — at the system level. What I would avoid is confusing **system concurrency** with **several threads mutating the same invariant-bearing state**.
>
> If two accounts or instrument groups are genuinely independent, they can be owned by different partitions and execute concurrently. But events that contribute to the same position/exposure invariant should have one ordering boundary. That can be a lock or, for a lower-variance design, a single-owner event loop.

---

# 7. If interviewer says: "How would YOU make it concurrent?"

Use this progression.

## Option 1 — direct single-owner

```text
caller
  ↓
risk state owner
  ↓
decision
```

### Reward
Simplest correctness and predictable latency.

### Cost
One owner has a finite throughput ceiling.

---

## Option 2 — per-account/group lock

```text
ConcurrentHashMap<accountId, AccountState>
                ↓
      synchronized(AccountState)
                ↓
          check + commit
```

### Invariant

```text
read state
→ validate ALL checks
→ commit ALL changes
```

must occur atomically for the account/group.

### Reward

Different accounts can execute concurrently.

### Cost

Contended accounts still queue on the lock; lock acquisition adds variance.

### Important

`ConcurrentHashMap` alone does **not** make this compound business operation atomic.

---

## Option 3 — partitioned single-owner workers

```text
ownershipKey
    ↓ hash
Partition 0 ──> queue ──> Worker 0 ──> owned state
Partition 1 ──> queue ──> Worker 1 ──> owned state
Partition 2 ──> queue ──> Worker 2 ──> owned state
Partition 3 ──> queue ──> Worker 3 ──> owned state
```

### Reward

- concurrency across independent partitions;
- deterministic ordering within a partition;
- no lock on partition-owned mutable state;
- natural backpressure boundary.

### Cost

- routing;
- queueing;
- partition skew;
- ownership/rebalancing;
- recovery complexity;
- cross-partition invariants become difficult.

This is usually the strongest **INTERVIEW DESIGN** follow-up.

---

# 8. The rule that makes partition ownership real

A partition is not truly single-owner if only orders use its queue.

Every state mutation for that ownership domain must pass through the same owner:

```text
OrderEvent ───────┐
CancelEvent ──────┤
ConfigEvent ──────┼──> partition queue -> ONE worker -> state
KillSwitchEvent ──┘
```

Otherwise a background/config thread can still race with the owner.

This matters directly when discussing the attached `RiskLimitEnginePartitionedAsync.java`.

---

# 9. What happens when the queue is full?

Do not say "make it unbounded."

> A bounded queue makes overload explicit. The correct policy depends on the business semantics: reject immediately, apply upstream backpressure, or use a controlled timeout. For a synchronous risk gate I would avoid silently building an unbounded queue because that converts overload into unpredictable latency and memory pressure.

Questions to clarify:

```text
Can caller retry?
Can order be delayed?
Must overload fail closed?
What is maximum acceptable latency?
```

---

# 10. Config changes / kill switch while orders are flowing

## Safe design principle

Configuration that changes the state used by an order decision must have a defined ordering relationship with orders.

Three defensible approaches:

```text
1. immutable/versioned configuration swap
2. same account/group lock as order processing
3. same partition-owner event queue
```

Do not claim the third is actual PTR unless independently established.

---

# 11. Fixed window vs rolling window

For the CoderPad notional-rate check:

## Fixed one-second bucket

```text
bucket = timestamp / 1000
```

### Reward
O(1), tiny state, very easy to write and explain.

### Risk
Boundary burst:

```text
999 ms  -> full allowance
1001 ms -> full allowance again
```

## Exact rolling second

Maintain timestamped usage in a deque/ring buffer and expire entries older than one second.

### Reward
Exact rolling semantics.

### Cost
More state and more work per event.

Interview line:

> I'll implement the fixed bucket unless the requirement explicitly says rolling one-second window. If it does, I'll move to a timestamped deque or ring buffer.

---

# 12. Multi-JVM evolution

**INTERVIEW DESIGN**

```text
incoming event
      ↓
ownership router
      ↓
JVM A / JVM B / JVM C
      ↓
local partition
      ↓
single owner
```

The hard questions are not the hash function.

The hard questions are:

```text
Who owns a key now?
How is ownership moved?
What happens during failover?
How is state recovered?
Can two owners ever act simultaneously?
How are in-flight events handled?
```

A mature answer acknowledges those problems instead of saying "consistent hashing solves it."

---

# 13. Single vs multi-threaded decision table

| Situation | Prefer single owner | Prefer parallel workers |
|---|---:|---:|
| State mutations require strict order | Yes | Only across independent partitions |
| Requests truly independent | Usually no | Yes |
| Tail-latency variance critical | Strong candidate | Possible, but coordinate carefully |
| Shared mutable invariant | Simplifies correctness | Requires synchronization/ownership |
| CPU work independently parallelizable | Leaves cores unused | Strong candidate |
| Hot-key / one-account concentration | Can bottleneck | Partitioning alone may not solve hot key |

The deciding question is not "exchange or web?" It is:

> **How independent is the work, and what ordering/state invariant must be preserved?**

---

# 14. Industry analogies — use as analogies, not proof of PTR

Useful conceptual parallels:

### Kafka partitions
Ordering within a partition, parallelism across partitions.

### Actor/event-loop systems
One owner receives messages and mutates private state.

### LMAX architecture
A famous public example of single-threaded business-logic processing and cache-conscious design.

### Redis
Historically useful as an example that a simple execution model can achieve high throughput when each operation is small, although modern Redis threading details are more nuanced than "Redis is single-threaded."

Do not use an analogy as evidence that the deployed Nasdaq design has the exact same topology.

---

# 15. Best senior-level sentence

> **Parallelize independent state; serialize mutations that share an invariant.**

And the companion:

> **Measure the bottleneck before paying the complexity cost of concurrency.**
