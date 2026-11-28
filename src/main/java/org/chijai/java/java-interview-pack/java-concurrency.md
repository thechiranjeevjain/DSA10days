# Java concurrency — active recall

**Baseline:** Java 17+ (Java 21 features marked), Spring Boot 3.x, PostgreSQL where applicable. P0 = closed-book recall; P1 = explain and implement with documentation. Snippets are focused fragments, not standalone projects.

## P0 — Java Memory Model
Atomicity, visibility, ordering are different. Happens-before guarantees visibility/order: monitor unlock → subsequent lock of same monitor; volatile write → subsequent read of same variable; thread start and successful join establish ordering. A data race is conflicting accesses without happens-before, at least one write. `volatile count++` is not atomic.

```java
final AtomicLong count = new AtomicLong();
count.incrementAndGet();
volatile boolean stopped;
```
Use immutable objects, confinement, locks or atomic operations; safely publish objects. LongAdder helps contended metrics, but sum is not an atomic snapshot.

## P0 — Locks and concurrent collections
```java
private final ReentrantLock lock = new ReentrantLock();
void reserve() {
    lock.lock();
    try { /* check and update shared invariant */ }
    finally { lock.unlock(); }
}
ConcurrentMap<Long, Integer> counts = new ConcurrentHashMap<>();
counts.merge(7L, 1, Integer::sum);
```
`synchronized` provides mutual exclusion and visibility; ReentrantLock adds timed/interruptible acquisition. Avoid nested locks or enforce global order. ConcurrentHashMap individual operations are thread safe, not arbitrary multi-step workflows. JVM locks do not coordinate multiple service instances.

## P0 — Executor and backpressure
```java
ExecutorService pool = new ThreadPoolExecutor(4, 8, 30, TimeUnit.SECONDS,
    new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.AbortPolicy());
try {
    Future<Integer> future = pool.submit(() -> 42);
    int result = future.get(500, TimeUnit.MILLISECONDS);
} finally { pool.shutdown(); }
```
Bound queue and concurrency; choose rejection semantics explicitly. Unbounded queues hide overload. `get(timeout)` does not cancel work; cancellation is cooperative. Restore interrupt status when unable to propagate InterruptedException. Monitor active threads, queue depth, rejected tasks, latency.

## P0 — CompletableFuture
```java
CompletableFuture<Hotel> h = CompletableFuture.supplyAsync(() -> loadHotel(id), pool);
CompletableFuture<Price> pr = CompletableFuture.supplyAsync(() -> loadPrice(id), pool);
CompletableFuture<Quote> quote = h.thenCombine(pr, Quote::new)
    .orTimeout(1, TimeUnit.SECONDS);
```
`thenApply` maps; `thenCompose` flattens dependent async work; `thenCombine` joins independent work. `handle` handles result/failure; `exceptionally` recovers failures. Timeout does not necessarily stop underlying work. Use explicit executors for blocking calls; avoid blocking on tasks queued in same saturated pool.

## P1 — Coordination and Java 21
CountDownLatch is one-shot; Semaphore bounds concurrent access; BlockingQueue supports producer-consumer; Condition awaits in a loop because of spurious wakeups. Virtual threads (Java 21) suit blocking I/O concurrency, not CPU acceleration. Limit scarce resources using semaphores/pools rather than pooling virtual threads. In Java 21, blocking while holding intrinsic monitors can pin carrier threads; later JDKs improve this.
```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    var result = executor.submit(() -> loadHotel(id)).get();
}
```
## Failure drills
- [ ] Reproduce lost update and fix it with AtomicInteger and synchronized.
- [ ] Explain deadlock, starvation, livelock, contention and false sharing.
- [ ] Design bounded parallel calls with timeout, rejection and partial failure.
- [ ] Explain why local locking cannot prevent distributed double booking.
- [ ] Diagnose executor starvation and ThreadLocal leaks in pooled threads.

Reference: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/package-summary.html
