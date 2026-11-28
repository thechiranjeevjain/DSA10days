# SQL and database internals — active recall

**Baseline:** Java 17+ (Java 21 features marked), Spring Boot 3.x, PostgreSQL where applicable. P0 = closed-book recall; P1 = explain and implement with documentation. Snippets are focused fragments, not standalone projects.

## P0 — Query templates (PostgreSQL)
```sql
SELECT h.id, COUNT(b.id) AS confirmed_count
FROM hotels h LEFT JOIN bookings b
  ON b.hotel_id = h.id AND b.status = 'CONFIRMED'
GROUP BY h.id HAVING COUNT(b.id) > 0;

WITH ranked AS (
  SELECT b.*, ROW_NUMBER() OVER
    (PARTITION BY hotel_id ORDER BY created_at DESC, id DESC) AS rn
  FROM bookings b
)
SELECT * FROM ranked WHERE rn <= 3;

SELECT * FROM bookings WHERE (created_at, id) < (:ts, :id)
ORDER BY created_at DESC, id DESC LIMIT 20;
```
Logical order: FROM/JOIN → WHERE → GROUP BY → HAVING → SELECT → ORDER BY → LIMIT. Filtering right-side rows in WHERE can turn a left join effectively into inner join. NULL comparisons use IS NULL; NOT IN with NULL can surprise; prefer NOT EXISTS where appropriate. COUNT(column) ignores nulls.

## P0 — Invariants and indexes
```sql
CREATE UNIQUE INDEX uq_booking_request ON bookings (tenant_id, request_id);
CREATE INDEX ix_booking_hotel_date ON bookings (hotel_id, created_at DESC, id DESC);
EXPLAIN (ANALYZE, BUFFERS) SELECT * FROM bookings WHERE hotel_id = 7;
```
B-tree supports equality/ranges/order; composite leading columns usually matter; planner can choose sequential scan. Indexes cost writes and storage. `EXPLAIN ANALYZE` executes query (including writes), so use carefully. Check actual vs estimated rows, loops, scan type and buffer reads. Avoid blind index creation.

## P0 — ACID, MVCC and concurrency
Atomicity all-or-nothing; consistency invariants; isolation concurrency semantics; durability committed data survives defined failures. PostgreSQL MVCC uses row versions; readers generally don't block writers. Read Committed uses statement snapshots; Repeatable Read uses transaction snapshot and can allow write skew; Serializable may abort and requires full transaction retry. PostgreSQL maps Read Uncommitted to Read Committed. Deadlocks can occur at any isolation level.
```sql
BEGIN;
UPDATE room_inventory SET available = available - 1
WHERE room_id = 7 AND stay_date = DATE '2027-01-01' AND available > 0;
-- verify exactly one row updated; otherwise rollback
-- insert booking in same transaction
COMMIT;
```
Alternatively SELECT FOR UPDATE before invariant check. Lock rows in consistent order. Unique constraints defend against races; check-then-insert alone does not. Optimistic version checks compare expected version in UPDATE. Keep transactions short; retry serialization/deadlock errors with bounds and idempotency.

## P1 — Storage and scaling
WAL supports crash recovery; replication may be asynchronous (lag, stale reads, failover loss). Backups require restore testing; RPO = tolerable data loss, RTO = recovery time. Vacuum reclaims dead tuple space for reuse; long transactions can delay cleanup. Pool size must respect DB capacity across all replicas. Offset pagination gets expensive and can shift with concurrent inserts; keyset pagination requires stable unique order.

## Recall drills
- [ ] Write top-N per group, anti-join, running sum and duplicate detection.
- [ ] Explain dirty/nonrepeatable/phantom reads, lost update and write skew.
- [ ] Design booking inventory constraints for multi-night stays.
- [ ] Interpret a slow query plan and propose an index with trade-offs.

Reference: https://www.postgresql.org/docs/current/
