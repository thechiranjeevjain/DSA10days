# Production debugging — incident playbook

**Baseline:** Java 17+ (Java 21 features marked), Spring Boot 3.x, PostgreSQL where applicable. P0 = closed-book recall; P1 = explain and implement with documentation. Snippets are focused fragments, not standalone projects.

## P0 — First five minutes
Establish impact, timeline, affected tenants/regions/endpoints, recent deploy/config changes and business correctness risk. Declare incident/owner if appropriate. Preserve evidence. Mitigate before exhaustive RCA: rollback, disable feature, shed load, isolate bad dependency or scale only verified bottleneck. Avoid destructive experiments and blind restarts. Communicate facts, uncertainty and next checkpoint.

## P0 — Symptom → evidence → action
| Symptom | Evidence | Candidate mitigation (verify first) |
|---|---|---|
| p99 latency high | traces, downstream timings, queue/pool saturation | bound concurrency, shed load, rollback |
| CPU high | process/thread CPU, JFR, GC logs | reduce hot workload, rollback |
| memory growth | live heap after GC, RSS, heap histogram | contain leak, safe rolling restart if needed |
| DB pool exhausted | active/pending connections, query times, locks | fix long transaction, limit admission |
| Kafka lag | lag/age by partition, rebalances, throughput | address hot partition or slow consumer |
| 5xx | status by hop, traces, exceptions, deploy diff | isolate failure, rollback |
| duplicates | request IDs, event IDs, transaction history | stop harmful path, reconcile, dedup |

## P0 — Command recall
```bash
# Linux, subject to permissions and environment
ps -eo pid,pcpu,pmem,comm --sort=-pcpu | head
 top -H -p PID
ss -s
curl -v --max-time 5 https://service.example/health
jcmd PID Thread.print -l
jcmd PID GC.heap_info
```
```sql
SELECT pid, state, wait_event_type, wait_event,
       now()-xact_start AS transaction_age
FROM pg_stat_activity WHERE xact_start IS NOT NULL
ORDER BY transaction_age DESC;
SELECT pid, pg_blocking_pids(pid) AS blockers FROM pg_stat_activity
WHERE cardinality(pg_blocking_pids(pid)) > 0;
```
Use read-only diagnostics first. Thread dumps repeated across time reveal persistent stacks. Heap dumps may pause processes and contain secrets; secure and restrict access. Do not terminate database sessions without ownership/impact assessment.

## P0 — Observability
RED: rate, errors, duration. USE: utilization, saturation, errors. Correlate metrics, structured logs and traces via request/trace ID; avoid high-cardinality metric labels (user/request ID). Alert on user impact and sustained burn rate rather than every isolated exception. For financial systems monitor business invariants: duplicates, reconciliation mismatch, stale limits and audit completeness.

## P0 — RCA template
Impact → timeline → trigger → contributing conditions → why safeguards failed → remediation → owner/date → verification. Distinguish proximate trigger from systemic causes. Use five whys as hypothesis generation, not proof. Include what went well and detection gaps. No blame; concrete prevention.

## Practice
- [ ] Latency doubled after deployment; design investigation and rollback criteria.
- [ ] DB connections exhausted with low CPU; explain likely causes and evidence.
- [ ] Consumer repeats payment event; contain and reconcile without deleting evidence.
- [ ] JVM killed by container despite heap below Xmx; explain native memory investigation.
