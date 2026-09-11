# Risk Engine — 15 Minutes Before Interview

Read this once, slowly.
Do **not** learn anything new after this.

---

# 0. Communication mode — sound like an 8 YOE engineer

Your pattern for almost every answer:

```text
ANSWER FIRST
→ state the decision

INVARIANT
→ what must remain true?

DESIGN
→ how does the code enforce it?

TRADE-OFF
→ what did you choose and why?

FOLLOW-UP
→ what would change at larger scale?
```

Speak in short blocks.

Use:

```text
“The key invariant here is…”

“I would keep the ownership boundary at…”

“I chose this because…”

“The trade-off is…”

“For this version I would keep it simple.”

“If that becomes a requirement, I would extend it by…”

“I would not use X here because it solves a smaller problem than the invariant I need to protect.”
```

Senior communication:

```text
clear
→ not rushed

decisive
→ not dogmatic

technical
→ not encyclopedic

confident
→ willing to say “that depends on the contract”

passionate
→ show interest through reasoning, not hype
```

---

# 1. 30-second system explanation

Say:

> “This is a stateful pre-trade risk engine. Configuration comes through the control side and initializes in-memory risk state. On the hot path, the matching engine sends the order into the PTR risk path, the configured checks run synchronously in memory, and the engine returns PASS or BREACH before the order proceeds further.”

Then:

```text
CONTROL
End User
→ PTR REST Config API
→ RX services
← RDM / RDM DB
→ in-memory configuration/state

HOT PATH
Customer
→ Matching Engine
↔ PTR Risk Plugin
→ PASS   → OrderBook / Trade
→ BREACH → reject
```

Memorize:

> **“The hot path stays synchronous and in memory; persistence and configuration are control-plane concerns.”**

Do not overclaim exact transport hops between RX processes.

---

# 2. 30-second code architecture

```text
Order
→ RiskEngine
→ RiskCheckGroup
→ List<RiskCheck>
→ RiskDecision
```

Say:

> “`RiskEngine` resolves the correct risk group. `RiskCheckGroup` owns the set of checks and the transaction boundary. Every rule implements `RiskCheck`. Stateful rules can reuse `AbstractStatefulRiskCheck`, while stateless rules implement the interface directly.”

```text
RiskCheck
├── AbstractStatefulRiskCheck
│   ├── MaxQtyCheck
│   └── TotalTradedPerTimeCheck
└── KillSwitchCheck
```

Why interface + abstract class?

> “The interface defines the universal rule contract. The abstract class only provides reusable snapshot/rollback behavior for rules that actually own mutable state.”

---

# 3. Extensibility — memorize this exactly

> **“The risk rule and the scope to which it applies are separate concerns.”**

```text
MaxQtyCheck
+
Predicate<Order>
```

Same class can support:

```text
AAPL BUY
TECH SELL
whole account
ticker set
market segment
```

Say:

> “A new risk rule implements `RiskCheck` and is added to the group. A new applicability scope changes the injected predicate. `RiskEngine` stays unchanged.”

OCP:

```text
new behavior
→ add implementation / wiring

not
→ add switch statements inside RiskEngine
```

---

# 4. Concurrency — the sharp answer

Start with:

> **“ConcurrentHashMap protects the registry; the synchronized RiskCheckGroup protects the business transaction.”**

Then:

```text
ConcurrentHashMap
→ safe concurrent group lookup

same RiskCheckGroup
→ synchronized evaluate()
→ one thread at a time

different RiskCheckGroups
→ different monitors
→ parallel execution
```

Same account:

```text
Thread 1 → Group A → evaluate()
Thread 2 → Group A → waits
```

Different accounts:

```text
Thread 1 → Group A
Thread 2 → Group B
→ both run in parallel
```

Why not `AtomicLong`?

> “Because the invariant spans multiple operations and potentially multiple checks: snapshot, mutate, detect breach and rollback. Atomic fields make individual counters atomic, not the whole business transaction.”

Memorize:

> **“Atomic field is not the same as an atomic business transaction.”**

Why `synchronized` instead of `ReentrantLock`?

> “I do not need `tryLock`, timeout, fairness or conditions here. `synchronized` is smaller, reentrant, automatically released and gives the required memory visibility.”

Scale follow-up:

> “At higher throughput I would partition by ownership key so independent groups have independent owners. I would avoid one global lock.”

---

# 5. Transaction / rollback — 20 seconds

```text
begin()
→ snapshot state

run checks

first BREACH
→ rollback every stateful check
→ return breach

all PASS
→ keep provisional state
```

Say:

> “The group owns the transaction because one order can mutate more than one risk check. If a later rule breaches, earlier provisional mutations must be rolled back.”

---

# 6. MaxQtyCheck — explain while coding

```text
predicate applies?
→ no  → PASS

candidate = consumption + quantity

candidate > limit?
→ yes → BREACH
→ no  → consumption = candidate
```

Say:

> “I use `Math.addExact` so overflow does not silently wrap and accidentally pass a risk limit.”

Cancel:

```text
consumption -= cancelled quantity
```

If asked:

> “Production cancellation also needs lifecycle/idempotency guarantees so the same quantity is not decremented twice.”

---

# 7. TotalTradedPerTimeCheck — 30 seconds

Current version = **fixed processing-time window**.

```text
window expired?
→ reset windowStartMs
→ reset consumption

tradedValue = price × quantity

candidate = consumption + tradedValue

candidate > limit?
→ BREACH
→ otherwise keep
```

Say:

> “`windowStartMs` and `consumption` form one invariant, so they are snapshot and rolled back together under the group transaction.”

If asked exact rolling window:

> “I would use a deque of `(timestamp, value)` plus a running sum. Before each order I evict entries older than `now - window`, then add the new value.”

---

# 8. Kill switch — why keep it?

> “It demonstrates that not every rule is stateful.”

```text
RiskCheck
→ universal interface

KillSwitchCheck
→ stateless
→ BooleanSupplier injected
```

Say:

> “The rule only needs to know whether the switch is active. It does not need to know whether that state came from an atomic flag, configuration service or another adapter.”

---

# 9. Clock — one sentence

> “I inject `Clock` so every check sees one consistent processing timestamp and tests can use `Clock.fixed()` instead of sleeping.”

If asked about `eventTime`:

> “Event time and processing time are separate business concepts; which drives the limit window is part of the contract.”

---

# 10. Publisher — one sentence

> “`RiskDecision` is domain data; `RiskDecisionPublisher` is an infrastructure port.”

Flow:

```text
group.evaluate()
→ lock released

publisher.publish(...)
→ outside group lock
```

Say:

> “I would not hold the risk-state lock across network or broker I/O.”

If latency critical:

> “The publisher can hand off asynchronously, but then ordering, backpressure and delivery guarantees become explicit design decisions.”

---

# 11. Money — sharp answer

> “For this coding exercise I am treating price as an explicit integer/fixed-point representation. I would not use `double` for exact financial risk.”

```text
business financial service
→ BigDecimal

latency-sensitive exchange hot path
→ fixed-point long / integer ticks
```

---

# 12. Add a new risk check

Answer immediately:

> “Create a new class implementing `RiskCheck`, inject whatever scope or external dependency it needs, and add it to the group. `RiskEngine` does not change.”

Example:

```text
PriceDeviationCheck
→ RiskCheck
→ Predicate<Order>
→ ReferencePriceProvider
```

---

# 13. PTR production incident — 45 seconds

Say:

> “We had a startup-ordering issue around the DEFAULT PTLG. The default group was intentionally processed later, but readiness was being marked too early. A recovered GTC order could arrive before the required risk container existed, so its initial exposure increment was skipped. Later, when the cancel arrived after state existed, the system decremented exposure and we could end up negative. The arithmetic itself was not wrong; the required state was not ready when the first event arrived. The fix was to process the deferred default PTLG before marking the cache ready.”

Invariant:

> **“Required risk state must exist before an order is allowed to affect exposure.”**

Classification:

> “It was an initialization/event-ordering bug, not simply a shared-memory race.”

---

# 14. If they challenge the design

Use:

```text
“Yes, that is a valid alternative.”

“The reason I chose this version is…”

“If the requirement changes to X, I would switch to Y.”

“For the current invariant, I think this is the smallest correct design.”
```

Why not `ReentrantLock`?

> “Valid option. I would use it if I needed timeout, interruption or conditions. I do not need those features here.”

Why not atomics?

> “They solve individual-field atomicity. My invariant spans the whole group transaction.”

Why not one global lock?

> “Correct but unnecessarily serializes independent accounts.”

Why not actor model?

> “Good production scaling option. It gives deterministic ownership but introduces routing, queues and backpressure, so I would not add it to the one-hour implementation unless required.”

---

# 15. Live-coding communication

Before code:

> “I’ll state the invariant first, then make one rule correct, then compose it.”

While coding:

```text
say what you are doing
not every keystroke

use examples
“Suppose current consumption is 900 and this order adds 200…”

name the trade-off

dry run before declaring done
```

If you make a mistake:

> “That breaks the invariant because ___. I’ll correct it here.”

If unsure:

> “I see two reasonable interpretations. I’ll state my assumption and continue unless you want the other one.”

---

# FINAL 60-SECOND MEMORY

```text
SYSTEM
control plane configures
hot path synchronous + in memory

DESIGN
RiskEngine
→ RiskCheckGroup
→ RiskCheck

EXTENSION
new rule → implement RiskCheck
new scope → Predicate
engine unchanged

CONCURRENCY
ConcurrentHashMap → registry
synchronized group → transaction
same group serial
different groups parallel
AtomicLong ≠ transaction

STATE
begin → mutate → breach? rollback : keep

RULES
MaxQty
TotalTradedPerTime
KillSwitch

TIME
Clock injected

OUTPUT
RiskDecision = data
Publisher = infrastructure

PRODUCTION STORY
DEFAULT PTLG readiness too early
→ initial increment skipped
→ later cancel decremented
→ negative exposure

INVARIANT
state must exist before event affects exposure
```

Last line before joining:

> **“I do not need to demonstrate that I know everything. I need to make the reasoning easy to follow.”**
