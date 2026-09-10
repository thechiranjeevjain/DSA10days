# Minimum to Memorize

Do not add more until this can be spoken and reconstructed cold.

---

# 1. PTR in one line

> **Stateful, event-driven, in-memory pre-trade risk; matching engine synchronously waits for the decision; hot path is optimized for predictable tail latency.**

---

# 2. Why stateful?

> Orders, cancels, trades and configuration changes alter the state used by the next risk decision.

---

# 3. Why single-owner?

> **Single-owner mutable state reduces synchronization complexity and makes event ordering easier to reason about. The objective is predictable tail latency, not maximizing parallel throughput.**

Do not automatically claim exact one-thread-per-instrument topology unless established separately.

---

# 4. Hardest technical story

Hook:

> **Negative exposure. Intermittent after startup. The arithmetic wasn't wrong — the state wasn't ready.**

Causal chain:

```text
DEFAULT PTLG deferred
→ cacheLoadComplete too early
→ GTC replay can arrive
→ RiskCheckContainer missing
→ initial increment skipped
→ limit later initialized
→ CANCEL decrements
→ negative exposure
```

Investigation:

```text
logs
→ DSF/event replay
→ reconstruct ordering
→ enlarge RX_SRV_SEND_LIMITS_INTERVAL
→ deterministic reproduction
→ fix readiness boundary
→ synthetic + original replay validation
```

Lesson:

> **Readiness is an invariant over required downstream state, not just a flag.**

---

# 5. End-to-end anchors

```text
incoming order
      ↓
matching-engine environment
      ↓
PTR synchronous in-memory decision
   /       \
BREACH     PASS
  ↓         ↓
reject    continue toward order book
```

Separate:

```text
HOT DATA PLANE
risk decision

CONTROL PLANE
configuration
persistence
recovery
security
observability
operations
```

---

# 6. Class diagram

```text
Order
  ↓
RiskEngine
  ↓
RiskCheckGroup
  ↓
RiskCheck[]
   ├── Position
   ├── Notional/rate
   └── Kill switch
```

One sentence:

> RiskEngine orchestrates, RiskCheckGroup owns the grouped decision invariant, and individual checks own rule logic.

---

# 7. Coding invariant

```text
READ
→ CALCULATE CANDIDATES
→ VALIDATE ALL
→ COMMIT ALL
```

If any check fails:

```text
REJECT
+
ZERO PARTIAL STATE CHANGE
```

---

# 8. 30-minute concurrency answer

```text
ConcurrentHashMap<accountId, AccountState>
                   ↓
           synchronized(state)
                   ↓
          check + commit all
```

Say:

> ConcurrentHashMap protects map operations. It does not make a compound read-check-update invariant atomic.

Different accounts can use different locks. Same-account mutations serialize.

---

# 9. Scale-up answer

**INTERVIEW DESIGN**

```text
ownership key
   ↓ hash
partition
   ↓
bounded queue
   ↓
ONE worker
   ↓
owned state
```

All mutations must use that owner:

```text
orders
cancels
config
kill switch
```

Memory sentence:

> **Parallelize independent state; serialize mutations that share an invariant.**

---

# 10. Fixed one-second rate window

Primary timed implementation:

```text
bucket = nowMs / 1000
```

Say:

> O(1) and simple, but permits boundary bursts. Exact rolling semantics require timestamped deque/ring-buffer state.

---

# 11. What I own

> Feature development, production issue investigation, customer escalations, release responsibilities and concrete feature delivery within my team scope. I do not claim overall PTR architecture ownership.

---

# 12. What I would redesign

> Explicit startup state machine with invariant-gated transitions, plus first-class deterministic event-replay regression testing.

---

# 13. VP answer shape

```text
business
→ invariant
→ constraints
→ decision + why
→ trade-off
→ failure
→ validation
→ outcome
```

---

# 14. Five sentences worth remembering

> The arithmetic wasn't wrong; the required state wasn't ready.

> ConcurrentHashMap protects the data structure, not the compound business invariant.

> System concurrency does not require multiple threads mutating the same state.

> The unit of ownership is the delivered outcome, not the merged pull request.

> I start from state, latency, availability, security and recovery requirements before choosing technology.

---

# 15. Interview order

```text
clarify
→ draw 2-minute LLD
→ working MVP
→ test invariant
→ add concurrency
→ discuss partitioning / failure
```

Do not attempt to impress before you have a working answer.
