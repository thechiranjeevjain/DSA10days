# System design — interview playbook

**Baseline:** Java 17+ (Java 21 features marked), Spring Boot 3.x, PostgreSQL where applicable. P0 = closed-book recall; P1 = explain and implement with documentation. Snippets are focused fragments, not standalone projects.

## P0 — 45-minute structure
| Minutes | Deliverable |
|---|---|
| 0–5 | users, use cases, exclusions, scale, correctness requirements |
| 5–10 | API, data model, estimates and SLOs |
| 10–20 | high-level architecture and main flows |
| 20–35 | deep dive on bottleneck and correctness |
| 35–45 | failure modes, observability, trade-offs and evolution |

## P0 — Capacity math
Example: 10 million requests/day ≈ 116 average RPS; 5× peak ≈ 580 RPS. At 200 ms average service time, Little's Law gives ~116 in-flight requests at peak (stable system). 1 KB × 10 million events/day ≈ 10 GB/day raw (decimal), before indexes, replication, compression and retention. State assumptions; avoid pretending estimates are precise. 99.9% monthly availability gives ~43.2 minutes unavailability in a 30-day month for time-based measurement; request-based SLO differs.

## P0 — Hotel booking reference design
```text
Client → gateway / load balancer → stateless booking API
                                  → PostgreSQL (inventory, booking, outbox)
                                  → cache (search/read models)
Outbox relay → Kafka → notifications / billing / read model
Payment provider ← payment worker (idempotency + reconciliation)
```
Search can tolerate stale data; booking must revalidate authoritative inventory. Use per-room/per-date inventory, atomic conditional updates or ordered row locks for all nights, unique request keys and a state machine (PENDING → CONFIRMED / CANCELLED / EXPIRED). Keep payment network calls outside DB lock duration. Expire holds with conditional transitions; reconcile payment versus booking state. Explain oversell prevention and failure after payment success.

## P0 — Design decision matrix
| Choice | When / trade-off |
|---|---|
| SQL vs NoSQL | invariants/joins vs workload-specific access/scaling; not a blanket rule |
| synchronous vs async | immediate answer vs decoupling and eventual consistency |
| cache | read-heavy, tolerance for stale data; invalidation complexity |
| read replicas | read scaling, possible lag |
| sharding | write/storage scale; cross-shard queries/transactions costly |
| queue | absorb bursts; backlog increases latency |
| monolith vs services | simpler deployment/transactions vs independent ownership/scaling |

## P1 — Reliability and security
SLI = measured behavior; SLO = objective; SLA = contractual commitment. Monitor p50/p95/p99, errors, saturation, throughput and business invariants. Partition by tenant/hotel where suitable; watch hot partitions. Define RPO/RTO, failover and restore tests. Authn/authz, tenant isolation, secrets, encryption, rate limiting, audit trails and PII retention. Roll out with canary/feature flags and backward-compatible schema changes.

## Practice prompts
- [ ] Hotel booking: double booking and payment reconciliation.
- [ ] Trading risk checks: low-latency pre-trade decisions, limits, stale data, audit.
- [ ] Rate limiter: token bucket vs sliding window, distributed atomicity.
- [ ] Notification system: dedup, preferences, retries, provider limits.
- [ ] URL shortener: ID generation, redirects, hot keys, abuse.
- [ ] Event ingestion: partitioning, replay, schema evolution, backpressure.
For each, draw architecture and defend one rejected alternative. Never jump to microservices before understanding requirements.
