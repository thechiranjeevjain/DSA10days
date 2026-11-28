# Senior Java Backend Interview Pack

A finite, active-recall syllabus for senior Java/Spring backend roles, with a recurring hotel booking example and financial/risk-system discussion prompts. Java 17+ baseline, Java 21 features identified. Examples are study fragments, not a compiled application. Verify version-specific configuration against project documentation.

| File | Focus | Preparation budget |
|---|---|---:|
| [java-core](java-core.md) | Language, collections, streams | 9% |
| [java-concurrency](java-concurrency.md) | JMM, locks, async | 10% |
| [jvm-internals](jvm-internals.md) | GC, memory, JIT | 6% |
| [spring-boot](spring-boot.md) | API, JPA, security, transactions, testing | 15% |
| [sql-database](sql-database.md) | SQL, indexes, MVCC | 10% |
| [kafka-messaging](kafka-messaging.md) | Events, offsets, delivery | 5% |
| [distributed-systems](distributed-systems.md) | Guarantees, resilience | 7% |
| [system-design](system-design.md) | Architecture, scale | 8% |
| [dsa-patterns](dsa-patterns.md) | Algorithms and coding | 15% |
| [design-patterns](design-patterns.md) | LLD and SOLID | 3% |
| [devops-cloud](devops-cloud.md) | Deployment, cloud, networking | 5% |
| [production-debugging](production-debugging.md) | Incidents and diagnosis | 3% |
| [project-deep-dive](project-deep-dive.md) | Real stories and decisions | 4% |

Budget is a proposed allocation, not measured interview frequency. Adjust for job description and mock-interview weaknesses.

## Four-week first pass

- Week 1: Java core, concurrency, JVM, daily DSA.
- Week 2: Spring Boot, SQL, build and test booking service, daily DSA.
- Week 3: Kafka, distributed systems, system design, LLD, daily DSA.
- Week 4: DevOps, debugging, project stories, mock interviews and revisit failures.

Daily block (90 minutes): 20 min closed-book recall, 25 min implementation and testing, 25 min DSA, 20 min explaining a design or project. Repeat missed topics on days 1, 3, 7, 14. On weekends run a timed mock interview.

## Definition of done

For each file, reproduce essential syntax without notes, explain guarantees and failure modes, implement at least one representative task, and defend an alternative. Score 0 = cannot recall, 1 = with hints, 2 = independent, 3 = independent plus trade-offs and failure diagnosis. Target 2+ on all P0 topics before expanding scope. This is a personal readiness rubric, not a hiring guarantee.

Do not endlessly rewrite notes. Maintain one runnable reference application, one mistake log and these 13 topic files. Update only when a concrete practice failure reveals a gap.
