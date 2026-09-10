# PTR Interview Master Script

These are the default spoken answers.

**Label discipline**

- **ACTUAL PTR** = use only source-grounded statements.
- **INTERVIEW DESIGN** = your engineering recommendation, not a claim about the deployed system.
- **VERIFY FIRST** = public scale/industry statements kept outside the default answer.

---

# 1. Tell me about yourself / what do you work on?

> I'm a Senior Software Engineer with eight years in financial systems. I currently work on pre-trade risk at Nasdaq, and previously worked at Morgan Stanley on technology change-risk tooling.
>
> My strongest area is stateful, latency-sensitive financial software. In PTR, the matching engine synchronously waits for the risk decision before the order can proceed, so the hot path has to be extremely predictable. The system keeps that path in memory and avoids database calls and remote-service dependencies.
>
> My work has covered feature development, production debugging of state-related issues, customer escalations, release ownership and broader delivery coordination.

## What this answer signals

```text
domain depth
+ stateful correctness
+ production experience
+ ownership
```

Do not start with Java/Spring. Start with the business system and its engineering constraints.

---

# 2. What is PTR?

## ACTUAL PTR — source-safe answer

> PTR is a stateful, event-driven pre-trade risk system integrated with the matching-engine environment. Before an order enters the order book, PTR evaluates it against configured risk limits such as exposure, quantity, value and price deviation.
>
> The matching engine synchronously waits for that risk decision, so the critical path is in memory and avoids database calls and remote-service dependencies. The risk plugin executes inside the matching-engine JVM, which avoids introducing a distributed RPC into every order decision.
>
> I think about the architecture as two worlds: the hot data plane that makes the real-time risk decision, and a richer control plane responsible for configuration, persistence, recovery, security, observability and operations.

## Memory line

> **PTR = stateful + event-driven + in-memory + synchronous risk gating + predictable tail latency.**

## Why stateful?

> Because the next decision depends on previous events. Orders, cancellations, trades and configuration changes modify the state used by later decisions.

---

# 3. What was the most technically challenging thing you did?

Open with:

> **The hardest problem I investigated was a negative-exposure bug. The arithmetic wasn't wrong — the required state wasn't ready.**

Then stop. Let the interviewer pull the thread.

## 30-second version

> It was an intermittent startup-ordering issue involving DEFAULT PTLGs and recovered GTC orders. Some DEFAULT PTLG risk limits were deferred, but `cacheLoadComplete` could be set before that deferred work completed. That allowed GTC replay to reach the order path before the corresponding `RiskCheckContainer` existed. The initial exposure increment was skipped; later cancellation found the initialized state and decremented from zero, producing negative exposure. I replayed the customer event stream, amplified the timing window using the limit-send interval, reproduced it deterministically, fixed the readiness ordering, and validated the patch with both synthetic and original-event replay.

For the full drill-down, use `03_NEGATIVE_EXPOSURE_TECHNICAL_CHALLENGE.md`.

---

# 4. Walk me end-to-end through your system

## ACTUAL PTR — safe 60-second narration

> An order reaches the matching-engine environment and must receive a pre-trade risk decision before it can proceed to the order book. PTR executes in the matching-engine JVM and evaluates the order against the risk state already held in memory.
>
> That risk state is stateful: prior orders, cancellations, trades and configuration changes affect what the next order is allowed to do. If a configured limit breaches, the order is rejected; otherwise the matching engine can continue its normal processing.
>
> Separately, the control side is responsible for loading and changing reference/risk configuration, persistence, startup and recovery, observability, security and operations. One of the hardest production bugs I handled was exactly at that boundary: the system declared itself ready before deferred DEFAULT PTLG risk state was fully installed.

Then draw the diagrams in `04_PTR_END_TO_END_AND_MERMAID.md`.

## Only add these details if you can independently establish them

- exact FIX/OUCH ingress path;
- exact UI product;
- exact REST endpoint path;
- exact database names;
- exact thread count;
- exact partition ownership scheme.

---

# 5. Why is the system single-threaded / single-owner?

## Source-safe opener

> **Single-owner mutable state is a deliberate design choice for predictable tail latency and simpler event ordering, not simply a lack of concurrency.**

## 60-second answer

> PTR is stateful, so multiple events can touch the same mutable risk state. If several threads mutate that state concurrently, we introduce synchronization, contention and harder event-order reasoning. The design rationale documented for PTR is to keep mutable state under a single owner so the critical path is easier to reason about and tail latency stays predictable.
>
> The hot path is in memory and avoids database calls and remote-service dependencies because the matching engine is synchronously waiting for the answer.
>
> If more throughput were required, my design approach would be to parallelize independent ownership domains rather than allow arbitrary threads to mutate the same state. For example, partition by a stable ownership key, give each partition one event loop, and preserve ordering inside that partition while different partitions run in parallel. That's an interview design evolution, not a claim about the deployed partition topology.

For the deeper version, use `02_PTR_CONCURRENCY_AND_SCALE.md`.

---

# 6. Where is the concurrency?

## ACTUAL PTR

> What I can safely say from the source material is that the hot-path design favors single-owner state to reduce synchronization complexity and make ordering easier to reason about. The system is optimized for predictable tail latency rather than maximizing parallel throughput.

## INTERVIEW DESIGN

> If measured throughput required more parallelism, I would partition by an ownership key. Each partition would own its mutable state and process its events serially, while independent partitions execute in parallel.
>
> The key principle is: **system concurrency does not require multiple threads mutating the same state.**

Do not turn a hypothetical account-partition model into an actual PTR claim.

---

# 7. What would you redesign today?

> The negative-exposure incident exposed a weak readiness boundary. I would make startup an explicit state machine where each phase transitions only when its invariants are true — including deferred work — rather than relying on a broad flag such as "cache loaded."
>
> I would also make deterministic event replay a first-class regression-test primitive. Replaying the customer's event stream and deliberately enlarging the suspected timing window were what converted an intermittent production problem into a repeatable test.
>
> The principle is: **readiness is a contract, not a boolean.**

---

# 8. What do YOU own versus the team?

> My ownership includes backend feature development, production issue investigation, customer escalations, release support and code-quality responsibilities within the team's scope.
>
> I've also owned concrete pieces end-to-end, including investigation and delivery work around controlled external API access and rate limiting, and I've coordinated quarterly release cycles across branches and dependencies.
>
> I don't claim ownership of the overall PTR architecture. Architecture decisions are broader team and organization decisions. I distinguish clearly between what I implemented, what the team delivered, and architecture I understand.

## Memory rule

```text
"I did"      = my contribution
"We did"     = team contribution
"System does"= architecture I understand
"I don't know" = do not invent
```

---

# 9. Why should I believe you can handle VP / lead scope?

> The strongest evidence is how I handle ambiguity and production risk. In the negative-exposure investigation I didn't patch the symptom. I reconstructed the event sequence, formed a timing hypothesis, deliberately amplified the timing window, reproduced the failure deterministically and fixed the violated readiness invariant.
>
> Beyond implementation, I've coordinated releases, handled customer escalations, broken ambiguous requirements into executable work, evaluated alternatives, built POCs, and driven delivery across stakeholders.
>
> I wouldn't claim that makes me the architect of the whole platform. The lead-level signal is that I can take an unclear problem, identify the important invariant and trade-off, get to evidence, and own the outcome through validation.

---

# 10. What happens at 10× volume?

## INTERVIEW DESIGN — not actual PTR topology

> First I would measure where saturation actually occurs rather than assume the risk calculation itself is the bottleneck.
>
> If the hot state owner is CPU-saturated, I would partition independent ownership domains across N workers or instances. Events for one ownership key must always route to the same owner so ordering and state correctness remain deterministic, while independent keys can execute in parallel.
>
> I would use bounded queues and an explicit overload policy rather than create unbounded latency. At multi-JVM scale, a routing layer can consistently direct an ownership key to its current owner.
>
> Startup/recovery and audit volume need separate capacity planning because they have very different characteristics from the synchronous risk-check hot path.

Avoid saying "scales linearly" without qualification. Real scaling eventually hits routing, skew, memory bandwidth, queueing, recovery and operational limits.

---

# 11. Reusable VP answer shape

For almost every technical project discussion:

```text
1. Business context
2. Technical challenge / invariant
3. Constraints
4. Options
5. Decision + why
6. Trade-off
7. Failure mode
8. Validation / observability
9. Outcome
10. What I would change today
```

The spoken answer should still be short. Use the structure internally; do not recite ten headings.

---

# Five principles to weave in

### State before computation

> The arithmetic wasn't wrong; the required state wasn't ready.

### Correctness before availability under ambiguous authority

> I'd rather temporarily have no leader than two leaders.

### Delivery over architectural elegance

> The most sophisticated architecture isn't necessarily the appropriate one.

### Outcome over code

> The unit of ownership is the delivered outcome, not the merged pull request.

### Requirements before technology

> I start from latency, state, availability, security and recovery requirements before choosing technology.
