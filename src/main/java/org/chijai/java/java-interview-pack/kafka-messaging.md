# Kafka and messaging — active recall

**Baseline:** Java 17+ (Java 21 features marked), Spring Boot 3.x, PostgreSQL where applicable. P0 = closed-book recall; P1 = explain and implement with documentation. Snippets are focused fragments, not standalone projects.

## P0 — Mental model
Topic → partitions → ordered records within each partition. A consumer group assigns each partition to at most one active consumer at a time in ordinary group consumption. Multiple groups read independently. Offset tracks position; committed offset normally denotes next record to consume. Key-based partitioning gives per-key ordering within a partition, not global order; partition count changes can change mapping. Retention is independent of consumption; compaction keeps latest key values eventually.

## P0 — Delivery semantics
| Mode | Failure window |
|---|---|
| At most once | commit before processing: possible loss |
| At least once | process then commit: possible duplicate |
| Kafka transactions | atomic Kafka writes and consumed offsets; read_committed consumers |

Producer idempotence handles certain producer retry duplicates, not repeated business requests. `acks=all` waits for in-sync replicas subject to min.insync.replicas; it does not mean every replica or absolute no-loss guarantee. External DB effects need idempotency or coordination beyond Kafka transactions.

## P0 — Spring Kafka fragments
```java
kafkaTemplate.send("booking-events", event.bookingId().toString(), event)
    .whenComplete((result, error) -> {
        if (error != null) log.error("Publish failed", error);
    });

@KafkaListener(topics = "booking-events", groupId = "billing")
void consume(BookingEvent event) {
    billingService.applyOnce(event); // transactional DB deduplication
}
```
Configure serializers/deserializers, group ID, `enable.auto.commit=false`, and listener acknowledgment mode deliberately. Error handler with bounded retries and dead-letter recoverer for poison records; define replay procedure. Retry topics may break ordering. Avoid swallowing exceptions; offset advancement must match success/recovery semantics.

## P0 — Consumer deduplication
```sql
INSERT INTO processed_events (consumer_name, event_id)
VALUES ('billing', :event_id) ON CONFLICT DO NOTHING;
```
If insert returns one affected row, perform business update in same DB transaction; otherwise skip. Commit Kafka offset after DB success. Crash after DB commit before offset commit yields safe replay. Define event IDs and retention horizon.

## P1 — Operational judgment
Monitor consumer lag, processing age, rebalance frequency, ISR shrinkage, producer errors and disk. More consumers than partitions do not increase group parallelism. Slow processing can exceed max.poll.interval.ms; tune batch size, timeouts and processing design. Hot keys limit parallelism. Schema evolution needs compatible changes and version strategy. DLT is not a substitute for alerting and remediation.

## Recall drills
- [ ] Explain broker failure, producer retry and consumer crash scenarios.
- [ ] Design outbox → Kafka → idempotent billing consumer.
- [ ] Explain why exactly-once Kafka processing does not imply exactly-once payment.
- [ ] Diagnose lag and choose partition strategy.

Reference: https://kafka.apache.org/documentation/ ; https://docs.spring.io/spring-kafka/reference/
