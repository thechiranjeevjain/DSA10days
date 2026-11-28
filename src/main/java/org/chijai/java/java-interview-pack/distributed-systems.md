# Distributed systems — active recall

**Baseline:** Java 17+ (Java 21 features marked), Spring Boot 3.x, PostgreSQL where applicable. P0 = closed-book recall; P1 = explain and implement with documentation. Snippets are focused fragments, not standalone projects.

## P0 — Guarantees and failure model
Network calls can fail ambiguously: timeout does not prove operation failed. CAP concerns consistency (linearizability) vs availability during a partition; partition tolerance is a reality, not a feature to casually drop. Strong consistency, eventual consistency, causal consistency and read-your-writes differ. Replication is not backup. Quorums with R+W>N overlap under assumptions but do not alone establish linearizability. Consensus (Raft/Paxos) coordinates replicated state; distinguish leader election from agreement.

## P0 — Resilience
Deadline budget across hops; bounded timeouts, exponential backoff with jitter, retry only transient and safe/idempotent operations. Retry amplification: three attempts at three layers can produce 27 downstream attempts. Circuit breaker stops repeated calls during failure; bulkhead limits isolation domains; rate limiter limits admission; backpressure slows producers. Load shedding protects critical paths. Never retry every failure blindly.

## P0 — Idempotency design
1. Scope key by tenant and operation; store request fingerprint.
2. Atomically claim unique key and perform DB effect in same transaction when possible.
3. Return prior response on same key/fingerprint; reject conflicting payload.
4. For external payment, propagate provider idempotency key and reconcile ambiguous outcomes.
5. Define expiry, in-progress behavior, retention and recovery.

## P0 — Saga and outbox
```text
Booking transaction: reserve inventory + insert booking + insert outbox row
Relay: publish event (may duplicate) → mark sent after broker confirmation
Consumer: deduplicate event ID + apply local effect in one transaction
Saga: reserve → authorize payment → confirm; on failure release/void as appropriate
```
Compensation is a business action, not time reversal. Handle retries, stuck states, manual reconciliation and compensations that themselves fail. CDC or polling can relay outbox; monitor backlog. Distributed two-phase commit offers a different coordination trade-off and may block during failures.

## P0 — Distributed locking and time
A lease can expire while old owner still runs. Use fencing tokens checked by resource or database constraints/conditional writes. Do not rely on wall-clock ordering; clocks skew. Use monotonic elapsed time for deadlines. Use logical versions when ordering matters.

## P1 — Caching and replication
Cache-aside: read cache → miss DB → fill; write DB then invalidate, but races remain. TTL bounds staleness only under assumptions. Prevent stampedes with coalescing, jitter or controlled refresh. Write-through/write-behind have different durability and latency trade-offs. Replicas can be stale; route critical read-after-write appropriately. Consistent hashing reduces remapping but skew/hot keys persist.

## Recall drills
- [ ] Walk through payment succeeds but response times out.
- [ ] Explain duplicate event after outbox relay crash.
- [ ] Design booking during network partition; state availability trade-off.
- [ ] Explain split brain, lease expiry and fencing.
- [ ] Calculate retry amplification and design end-to-end deadlines.

Reference: https://aws.amazon.com/builders-library/ ; https://raft.github.io/
