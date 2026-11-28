# JVM internals — active recall

**Baseline:** Java 17+ (Java 21 features marked), Spring Boot 3.x, PostgreSQL where applicable. P0 = closed-book recall; P1 = explain and implement with documentation. Snippets are focused fragments, not standalone projects.

## P0 — Runtime memory
| Area | Contents / failure |
|---|---|
| Heap | objects, arrays; retained references cause heap OOM |
| Thread stack | frames, local variables, operand stack; deep recursion StackOverflowError |
| Metaspace | class metadata in native memory; classloader leaks |
| Code cache | compiled machine code |
| Native/off-heap | direct buffers, thread stacks, JNI; RSS can exceed heap |

Class loading: loading → linking (verification, preparation, resolution, which may be lazy) → initialization. Parent delegation is typical, not universal. Class identity includes defining classloader. JIT optimizes hot paths; escape analysis may eliminate allocations; benchmark with JMH, not naive loops.

## P0 — Garbage collection
GC traces reachable objects from roots (threads, static references, JNI). Unreachable cycles are collectible. Generational collectors exploit short object lifetimes. G1 balances throughput and pause goals; ZGC targets low pauses at different resource costs. Collector availability depends on JDK. Stop-the-world phases still exist. Distinguish allocation rate from retained live set. GC tuning follows measurement, not intuition.

## P0 — Diagnostics commands
```bash
jcmd -l
jcmd PID VM.flags
jcmd PID Thread.print -l
jcmd PID GC.heap_info
jcmd PID GC.class_histogram
jcmd PID JFR.start name=incident settings=profile duration=60s filename=/tmp/incident.jfr
# potentially expensive; assess disk, pause and privacy risk first
jcmd PID GC.heap_dump /secure/path/heap.hprof
```
Startup examples: `-Xms512m -Xmx2g -Xlog:gc*:file=gc.log:time,level,tags`. Container memory budget must include native memory. NMT requires startup configuration (`-XX:NativeMemoryTracking=summary`); then `jcmd PID VM.native_memory summary`. Use matching JDK tools and permissions.

## P0 — Interpret symptoms
- Heap rises after full collections → inspect retained objects, dominators, GC roots.
- High allocation + stable live set → allocation churn rather than necessarily leak.
- High CPU → thread CPU, repeated dumps, JFR profiling; distinguish GC and application.
- Many BLOCKED threads → monitor contention; WAITING can be normal idle workers.
- OOM unable to create native thread → check thread counts, OS limits, native memory.
- Latency spike → correlate GC pauses, safepoints, queues and downstream latency.

## Recall drills
- [ ] Draw heap, stacks, metaspace and classloading.
- [ ] Explain strong/weak/soft references and why weak references do not guarantee timely cleanup.
- [ ] Diagnose heap leak vs native memory growth.
- [ ] Explain why increasing heap may worsen pause or container OOM risk.

Reference: https://docs.oracle.com/en/java/javase/21/troubleshoot/
