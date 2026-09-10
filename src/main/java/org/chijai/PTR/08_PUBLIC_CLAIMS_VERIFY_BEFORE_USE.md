# Public / Industry Claims — Verify Before Using in an Interview

The shared notes contain several strong public claims and numbers.

They may be useful, but they are **not established by the attached PTR source material** and therefore should not appear in the default ACTUAL PTR answer without independent verification.

This file deliberately quarantines them.

---

# 1. Claims from the notes that require verification

Examples include:

- Nasdaq Russell Reconstitution 2025: **2.5 billion shares in 0.871 seconds**.
- Nasdaq Nordic: **~150–200 active member firms**.
- Nasdaq Nordic: **~1 million trades per day**.
- Nasdaq Nordic: **~1,174 listed companies at end-2024**.
- "Typical production exchange: **<50 microseconds end-to-end order lifecycle**."
- "One CPU core can sustain **millions of order messages per second**."
- LMAX: **6 million orders per second on a single thread**.
- Disruptor benchmark: **3000× lower latency than ArrayBlockingQueue**.
- "Every major exchange — NYSE, Nasdaq, LSE — runs a single-threaded matching engine per instrument."
- exact claims about NYSE Pillar, CME Globex, Coinbase, Binance thread models.
- a Coinbase 2026 outage being caused by a deliberate single-AZ matching-engine choice.
- Redis as simply "single-threaded" and doing a particular million-ops/s number.
- exact context-switch costs such as **1–10 μs**.
- statements that the CPU is "never" the bottleneck and network/serialization is always the bottleneck.

Some may be directionally or historically true; several are too absolute.

---

# 2. Why not memorize these first?

Because the interviewer can challenge the number and derail the conversation away from your strongest evidence: your actual system understanding.

The higher-return answer is:

> The design objective is deterministic tail latency. The hot path is in memory, the matching engine synchronously waits for the decision, and single-owner state reduces synchronization complexity and simplifies ordering.

That is already technically meaningful without an external benchmark.

---

# 3. LMAX — safe usage before verification

Safe:

> LMAX is a well-known public example of a single-threaded business-logic processor and mechanical-sympathy approach. It's a useful analogy for why eliminating shared-state contention can improve predictable latency.

Riskier unless source checked:

> LMAX does exactly 6 million orders per second on one thread and therefore our architecture is identical.

Do not say a public design is "the same architecture" as PTR unless you have evidence for both sides of the comparison.

---

# 4. Redis — safe usage

Safe:

> Redis is a useful historical example of how a simple event-driven execution model can process large workloads without one thread per client.

Avoid:

> Redis is fully single-threaded, therefore adding threads would make it slower.

Modern Redis has multiple threading details depending on version and operation. Keep the analogy conceptual.

---

# 5. Kafka — safe usage

Safe:

> Kafka partitions are a useful mental analogy: preserve ordering inside an ownership domain and obtain parallelism across independent partitions.

Avoid:

> PTR works exactly like Kafka partitions.

Analogy is not architecture evidence.

---

# 6. Public exchange participant counts

If asked "how many users?", the safer conceptual distinction is:

```text
consumer web system:
many independent end users

institutional exchange:
participants/connections generate event volume;
user count is not the primary capacity dimension
```

Then say:

> I don't want to invent our production participant or order-rate number. The capacity metric that matters technically is peak ordered event rate and the latency budget for the synchronous decision.

That answer is stronger than guessing.

---

# 7. Strong absolute statements to avoid

Avoid:

> "You cannot parallelize a matching engine."

Better:

> Matching for one order book has strict ordering constraints; introducing parallel mutation requires additional coordination, so many low-latency designs preserve one ordered ownership boundary.

Avoid:

> "Locks destroy latency."

Better:

> Locks and contention can add latency variance, which is why I prefer avoiding shared mutable state on a latency-sensitive path where an ownership model is practical.

Avoid:

> "CPU is never the bottleneck."

Better:

> I would measure whether the bottleneck is compute, allocation/GC, network, serialization, queueing or state access before redesigning concurrency.

Avoid:

> "Partitioning scales linearly."

Better:

> Partitioning can increase parallel capacity until skew, routing, memory bandwidth, recovery and other shared resources become limiting.

---

# 8. Claims about actual PTR that need separate confirmation

These appeared in later drafts but are not established by the source material currently being treated as authoritative:

```text
exactly one ME thread per instrument
zero ConcurrentHashMap on actual hot path
zero synchronized blocks on actual hot path
exact N instrument instances
exact account/instrument ownership topology
all admin commands dispatched onto ME main thread
one background timer dispatching expiry back to main thread
limits loaded from DB while consumption rebuilt from event log
```

Especially important:

The source-grounded negative-exposure root cause is:

```text
DEFAULT PTLG risk-limit initialization deferred
→ RiskCheckContainer not yet available
→ recovered GTC increment skipped
→ later cancel decrements
```

Do not replace that with the different "consumption not rebuilt from event log" explanation unless separate evidence proves it.

---

# 9. What to do if challenged on a number

Use:

> I don't want to guess the production number. The architectural reason is independent of the exact figure: the risk decision is synchronous and stateful, so predictable tail latency and ordering matter. If we were capacity-planning it, I would start from measured peak messages per second, per-owner skew and the p99/p999 latency budget.

That sounds more senior than defending an uncertain benchmark.

---

# 10. Verification checklist before adding a public number to the main script

For each number/claim:

```text
[ ] primary or highly authoritative source?
[ ] exact year/date?
[ ] exact system/product?
[ ] orders vs trades vs shares clearly distinguished?
[ ] average vs peak clearly distinguished?
[ ] latency definition clear?
[ ] single thread/core claim actually stated?
[ ] not extrapolated from benchmark to production architecture?
```

Only after that should the number move into the memorization sheet.
