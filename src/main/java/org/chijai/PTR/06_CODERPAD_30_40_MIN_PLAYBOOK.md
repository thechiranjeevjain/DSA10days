# 30–40 Minute CoderPad Playbook — Risk Limit Engine

The goal is not to produce the final architecture immediately.

The goal is:

```text
working answer in minutes
→ prove correctness
→ improve design
→ discuss concurrency
```

This is the anti-freeze protocol.

---

# 0–3 minutes — clarify only what changes the code

Ask concise questions:

```text
1. Position limit:
   absolute signed position or BUY quantity only?

2. Notional rate:
   submitted order notional or executed/traded notional?

3. Window:
   fixed one-second bucket or exact rolling second?

4. State update:
   does an accepted order immediately consume risk?

5. Cancellation:
   do I need to release position consumption in this exercise?

6. Concurrency:
   should I first implement correct single-threaded behavior,
   then make it thread-safe?
```

If interviewer does not care, state reasonable assumptions and move.

Say:

> I'll make the invariant correct first and then evolve the concurrency model.

---

# 3–5 minutes — draw tiny LLD

```text
Order
  ↓
RiskEngine
  ↓
Account/RiskGroup State
  ├── positionByTicker
  ├── rate-window state
  └── limits
  ↓
RiskDecision
```

If they value extensibility:

```text
RiskEngine
  ↓
RiskCheckGroup
  ↓
RiskCheck[]
```

Do not spend ten minutes drawing inheritance.

---

# 5–12 minutes — V1 position-only working answer

Minimal state:

```java
Map<Integer, AccountState> accounts;
```

Account state:

```java
Map<String, Long> positionByTicker;
long maxPosition;
```

Core logic:

```text
old position
+ signed quantity
= candidate position

if candidate breaches:
    reject

else:
    commit
    accept
```

Run one pass and one reject example.

At this point you already have a working answer.

---

# 12–20 minutes — V2 add notional-per-second

Assume price stored in integer smallest currency unit.

```text
orderNotional = price * quantity
```

Use checked arithmetic if practical.

Fixed-window MVP:

```text
second = nowMs / 1000

if second changed:
    candidate old usage = 0

candidateNotional = old + orderNotional
```

Critical rule:

```text
DO NOT update position
then discover notional breach
```

Instead:

```text
calculate candidatePosition
calculate candidateNotional

validate position
validate notional

ONLY THEN
commit both
```

Say:

> I want all checks and the state commit to behave as one logical transaction.

---

# 20–25 minutes — tests that prove the invariant

Test:

```text
1. normal accept
2. position breach
3. notional breach
4. second rollover
5. reject does not partially mutate state
6. unknown account
```

The most valuable test is #5.

A senior interviewer cares whether a rejected order can corrupt later decisions.

---

# 25–32 minutes — thread safety

Use:

```java
ConcurrentHashMap<Integer, AccountState>
```

for account lookup.

Then:

```java
synchronized (state) {
    calculate candidates
    validate all
    commit all
}
```

Say:

> ConcurrentHashMap makes individual map operations safe. It does not make my read-check-update business transaction atomic, so I lock the account state for that compound invariant.

Then:

> Different accounts have different state objects, so they can execute concurrently. Same-account mutations are serialized.

This is a complete defensible concurrency answer.

---

# 32–37 minutes — scale discussion, not necessarily more code

If interviewer asks for lower lock variance / more scale:

```text
accountId
  ↓ hash
partition
  ↓
bounded queue
  ↓
single owner
  ↓
state
```

Say:

> If measured contention justified it, I would move from per-account locking to partition-owned event loops. Independent partitions run concurrently, while all state mutations for one ownership domain stay ordered.

Then immediately add:

> Orders, cancels, config and kill-switch changes must all go through that owner, otherwise the single-owner guarantee is false.

That is the senior-level detail.

---

# 37–40 minutes — failure / production evolution

Pick only the highest-value topics:

```text
bounded queue / backpressure
state recovery
idempotency
audit trail
dynamic config ordering
overload policy
metrics: latency + rejects + queue depth
```

Do not start implementing Kafka, Redis, databases or Spring.

---

# Phrases to use while coding

> I'll start with the smallest correct state model.

> I'm separating candidate calculation from commit so a rejection cannot partially mutate state.

> ConcurrentHashMap protects the container; it doesn't protect this multi-step invariant.

> The lock scope is one account/group, not the whole engine.

> If contention becomes material, I would partition ownership rather than add arbitrary shared-state parallelism.

> A bounded queue converts overload into an explicit policy instead of unbounded latency.

> I'm keeping the CoderPad solution in-process. Distributed infrastructure is a separate design decision.

---

# What not to do

```text
✗ start with Kafka
✗ start with five interfaces
✗ spend 15 minutes on diagrams
✗ code final partition architecture first
✗ mutate one risk check before validating the others
✗ say AtomicLong makes the whole thing atomic
✗ use double for money
✗ call a fixed bucket an exact rolling window
✗ invent production throughput numbers
```

---

# Fixed vs rolling answer

> For the timed implementation I'll use an epoch-second bucket because it is O(1) and easy to verify. It permits a boundary burst. If the requirement is an exact rolling second, I would keep timestamped usage in a deque or ring buffer and evict entries older than one second.

---

# Why not global synchronized?

> It would make unrelated accounts block each other. The invariant is account/group-local, so the synchronization boundary should be local too.

---

# Why not AtomicLong?

> Because the decision spans multiple pieces of state. I have to read position and rate consumption, validate both and commit both consistently. Making each counter individually atomic doesn't make that compound transaction atomic.

---

# Why not ConcurrentHashMap only?

> It protects map operations such as `get` and `put`. It does not protect `read current values -> validate -> update several fields` as one business transaction.

---

# Why not parallelize one account?

> If its events contribute to the same position/exposure invariant, their relative order matters. I would first preserve one ordering boundary for that state. Parallelize independent ownership domains instead.

---

# What if the process crashes?

> In-memory state needs a recovery contract. Depending on the architecture that can be snapshot plus replay or authoritative event/config reconstruction. I would not put persistence calls on every hot-path decision just to solve recovery.

---

# How do limits update safely?

> A limit update needs a defined ordering relationship with orders. In the lock design, apply it under the same account/group lock or atomically replace immutable/versioned config. In the single-owner design, route the update through the same owner queue.

---

# How do you audit?

> Emit the decision and enough identifiers/config version to reconstruct why it was accepted or rejected, preferably off the synchronous calculation path where semantics allow.

---

# What if interviewer asks "make it extensible"?

Then introduce:

```java
interface RiskCheck {
    Result check(...);
}
```

and:

```text
RiskCheckGroup
  ├── PositionCheck
  ├── NotionalCheck
  └── KillSwitchCheck
```

Do this **after** the business invariant is working.

---

# Final priority

```text
correct runnable MVP
   > complete architecture
```

A finished, correct 25-line core with intelligent follow-ups is stronger than half of a sophisticated design.
