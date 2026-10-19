# Hybrid 12-Week Engineering Career OS — V6.1

**Fixed dates:** Monday 19 October 2026 → Sunday 10 January 2027  
**Execution days:** Monday–Friday, 08:00–11:00 IST (60 scheduled sessions; 180 scheduled hours)  
**Target:** Senior/Lead Backend or Financial Risk Platform Engineer; ₹50–80 LPA is an aspirational compensation range, not a guaranteed market outcome.  
**Secondary differentiators:** Applied AI, fraud analytics, FDE integrations.  
**Leadership target:** Independently build, own, and coordinate a bounded initiative with multiple engineers. Real-world people leadership requires experience beyond simulations.

## Zero-choice start instructions

1. On **Day 001**, open the existing interview circuit's Day 001 entry in **Appendix A**. Open engineering ticket **E01** in the week table below.
2. Follow the exact daily clock. Use only the assigned week's resources and today's task. Do not substitute another course, problem or project.
3. Record test evidence, actual time, score, blockers and Git commit in `logs/day-NNN.md`.
4. On the next scheduled day, advance to the next numbered interview circuit day **and** engineering ticket unless a blocking prerequisite has failed; use the repair policy below.
5. No extra study hours are assumed. The original optional 90-minute bonus is explicitly excluded.

## Daily timebox (180 minutes total)

| Clock | Work | Output |
|---|---|---|
| 08:00–08:15 | Read current circuit day's 4–5 DSA cues; mark G/Y/R; review oldest failed cue | Cue status in daily log |
| 08:15–09:30 | Perform the circuit day's **core** DSA/LLD/HLD/Java/mock task (75-minute capped version) | Code/design/mock score |
| 09:30–09:40 | Read today's narrow documentation subsection | Three learning notes |
| 09:40–10:35 | Execute today's numbered engineering ticket | Commit or document |
| 10:35–10:50 | Run tests and score against rubric | Test output and score |
| 10:50–11:00 | Log result, blockers, next ticket, and Friday career action | `logs/day-NNN.md` |

**Honest capacity constraint:** The source circuit expects 15 minutes of DSA cues **plus a 90-minute core**. Here the core is capped at 75 minutes to fit 180 minutes alongside engineering. Some 90-minute designs and mocks cannot be fully completed in 75 minutes; mark them *timeboxed/incomplete* rather than falsely passing. A full 60-minute mock still fits. When core work overruns, stop at 09:30 and put the gap in the repair queue. This is a deliberate hybrid adaptation, not a claim that the original 90-minute core was preserved unchanged.

## Locked stack and architectural constraints

- **Java 21, Maven, Spring Boot 3.x (pin actual release in POM), JUnit 5, Mockito, Testcontainers.**
- **PostgreSQL 16** authoritative for decisions/limits; **Redis 7** only for caching; **Kafka** for asynchronous events.
- **Python 3.12, pandas, scikit-learn, FastAPI, pytest** for fraud and integration.
- **JMH + JFR** for JVM performance; **k6** for load tests; **Docker Compose**, **GitHub Actions**, **OpenTelemetry** for deployment/observability.
- **OpenAI API** for advisory structured summaries only. Read-only tools, allowlisted fields, no autonomous financial action, no secrets or real customer data.
- Repository: `fraud-risk-lab`. Java modular monolith + separate Python scoring service. Use synthetic orders/merchants for integration and a separate labeled public dataset for statistical fraud evaluation. Do **not** claim the public dataset contains merchant features or that hypothetical costs represent observed business savings.
- Git: `main` and `weekNN/dayNN`; commits `E01: <specific change>`. Maintain `README.md`, `docs/adr/`, `docs/architecture/`, `docs/leadership/`, `docs/evaluations/`, `logs/`, `java-risk-service/`, `python-fraud-service/`.
- If a version is unavailable in the local environment, log it as a dependency blocker; do not silently switch versions. Exact patch releases are pinned at setup, then held constant.

## Daily engineering rubric

| Criterion | Points | Evidence |
|---|---:|---|
| Correctness | 40 | Assertions, invariants, expected outputs |
| Failure handling | 20 | Negative, timeout, race or recovery test |
| Tests and reproducibility | 20 | Automated tests, deterministic reproduction |
| Explanation and trade-offs | 10 | Three concise bullets in log |
| Documentation and commit | 10 | Commit hash, setup and test commands |

**Green:** ≥85/100; **Yellow:** 65–84; **Red:** <65. Any failed financial correctness, authorization, isolation, or sensitive-data test overrides to **Red**. Scores are internal coaching criteria, not industry certifications.

**Deterministic repair:** (a) record the failing assertion and reproduction; (b) enqueue the gap FIFO, critical prerequisites first; (c) at 08:00 use at most 15 minutes on the oldest gap; (d) never claim the ticket passed before its acceptance test passes; (e) dependent tickets are *blocked* if they require failed functionality; (f) do not double sessions to catch up; (g) if the calendar ends with unfinished work, report actual completion rather than inventing it. Unblocked scheduled tickets continue; blocked tickets wait for the prerequisite. Optional features are dropped before core correctness or interview fundamentals.

**Weekly review:** Friday 10:50–11:00 includes the career action listed for that week. If ten minutes is insufficient, record it incomplete and prioritize it in the next repair window. This does not magically make applications/referrals fit within ten minutes.

## Integrated 12-week plan — engineering tickets E01–E60

Each numbered ticket has a **55-minute implementation window** followed by a **15-minute verification window**. Work may remain incomplete; evidence and repair status are mandatory.

### Week 01 — Java concurrency | Circuit days 001–005

**Primary resource:** https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/package-summary.html  
**Exact scope:** ExecutorService, AtomicLong, ConcurrentHashMap, ReentrantLock; JMM visibility and atomicity  
**Friday career action:** Extract five genuine production achievements; record context, actions and evidence

| Day / ticket | Implementation task | Required evidence |
|---|---|---|
| 001 / **E01** | Initialize Java 21 Maven repo; create RiskRequest, RiskDecision, RiskRule | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 002 / **E02** | Implement amount and account-limit rules with deterministic test fixtures | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 003 / **E03** | Implement thread-safe in-memory reservation with explicit invariants | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 004 / **E04** | Add 100-worker concurrency tests and interruption handling | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 005 / **E05** | Run 1,000 reservation attempts; verify limits; ADR-001 on synchronization | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |

**Week 01 exit gate:** No overspending, no unhandled race, repeatable concurrency tests; defend happens-before and atomicity.

### Week 02 — JVM performance | Circuit days 006–010

**Primary resource:** https://docs.oracle.com/en/java/javase/21/gctuning/  
**Exact scope:** Heap, GC/G1, JFR recordings and thread/allocation investigation; https://docs.oracle.com/en/java/java-components/jdk-mission-control/9/user-guide/jdk-mission-control.html  
**Friday career action:** Rewrite two resume bullets without invented metrics

| Day / ticket | Implementation task | Required evidence |
|---|---|---|
| 006 / **E06** | Add JMH benchmark harness and baseline configuration | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 007 / **E07** | Measure single-thread throughput and latency distribution | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 008 / **E08** | Create allocation-heavy workload and capture JFR | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 009 / **E09** | Create lock contention and capture JFR/thread dump | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 010 / **E10** | Fix one measured bottleneck and rerun same benchmark; report | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |

**Week 02 exit gate:** Reproduce and diagnose allocation and contention; record comparable before/after data.

### Week 03 — Spring Boot correctness | Circuit days 011–015

**Primary resource:** https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative.html  
**Exact scope:** @Transactional, propagation, rollback, proxy/self-invocation; https://spring.io/guides/gs/testing-web/  
**Friday career action:** Finalize Senior Backend / Risk Platform resume

| Day / ticket | Implementation task | Required evidence |
|---|---|---|
| 011 / **E11** | Add REST API, request validation, standardized errors | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 012 / **E12** | Add rule configuration endpoint and API tests | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 013 / **E13** | Implement transactional reservation service | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 014 / **E14** | Add rollback and concurrent integration tests | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 015 / **E15** | Seed and review transaction bug; fix; publish code review | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |

**Week 03 exit gate:** Invalid requests rejected, rollback verified, correct concurrent reservations, tests passing.

### Week 04 — PostgreSQL and SQL | Circuit days 016–020

**Primary resource:** https://www.postgresql.org/docs/current/transaction-iso.html  
**Exact scope:** Read Committed, Repeatable Read, Serializable; https://www.postgresql.org/docs/current/using-explain.html; B-tree and multicolumn indexes  
**Friday career action:** Apply to three suitable senior backend/risk roles

| Day / ticket | Implementation task | Required evidence |
|---|---|---|
| 016 / **E16** | Create SQL schema and migrations | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 017 / **E17** | Persist requests, decisions, limit reservations | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 018 / **E18** | Implement conditional atomic limit update | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 019 / **E19** | Capture three EXPLAIN ANALYZE plans and justified indexes | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 020 / **E20** | Run concurrent DB updates and rollback tests; ADR-002 | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |

**Week 04 exit gate:** Atomicity and no overspending proven with DB as authority; query plans saved.

### Week 05 — Kafka and Redis | Circuit days 021–025

**Primary resource:** https://kafka.apache.org/documentation/  
**Exact scope:** Producers, consumers, partitions, offsets, at-least-once implications; https://redis.io/docs/latest/develop/ — TTL and cache-aside  
**Friday career action:** Request two targeted referrals

| Day / ticket | Implementation task | Required evidence |
|---|---|---|
| 021 / **E21** | Add risk-decision Kafka producer | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 022 / **E22** | Add consumer with durable idempotency key | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 023 / **E23** | Add bounded retry, failure handling and dead-letter path | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 024 / **E24** | Add Redis cache with PostgreSQL authoritative fallback | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 025 / **E25** | Replay duplicate events, restart consumer, disable Redis; report | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |

**Week 05 exit gate:** Duplicates do not double-apply side effects; recovery demonstrated; cache outage safe.

### Week 06 — Distributed systems and HLD | Circuit days 026–030

**Primary resource:** https://sre.google/sre-book/handling-overload/  
**Exact scope:** Entire overload chapter; https://github.com/donnemartin/system-design-primer — capacity, caches, queues, consistency  
**Friday career action:** Review mocks; record top three pass-changing gaps

| Day / ticket | Implementation task | Required evidence |
|---|---|---|
| 026 / **E26** | Create system diagram and data-flow map | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 027 / **E27** | Specify traffic assumptions, SLOs, failure budget | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 028 / **E28** | Implement bounded processing and overload response | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 029 / **E29** | Run fixed load test; collect p95/p99, errors, throughput | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 030 / **E30** | Conduct 45-minute HLD defense; ADR-003; runbook | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |

**Week 06 exit gate:** Assumptions, failure recovery, capacity math and measured bottlenecks defensible.

### Week 07 — Fraud analytics | Circuit days 031–035

**Primary resource:** https://scikit-learn.org/stable/modules/model_evaluation.html  
**Exact scope:** Classification precision, recall, PR-AUC; https://scikit-learn.org/stable/modules/classification_threshold.html; pandas quickstart  
**Friday career action:** Apply to three suitable risk/backend roles

| Day / ticket | Implementation task | Required evidence |
|---|---|---|
| 031 / **E31** | Load labeled public fraud dataset; provenance and prevalence | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 032 / **E32** | Create leakage-safe split and dummy baseline | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 033 / **E33** | Train logistic regression; measure PR-AUC, precision, recall | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 034 / **E34** | Compare five validation thresholds and three hypothetical cost models | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 035 / **E35** | Lock threshold; evaluate held-out test once; publish limitations | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |

**Week 07 exit gate:** Reproducible notebook; no test-set threshold tuning; distinguish synthetic cost assumptions.

### Week 08 — FDE APIs and security | Circuit days 036–040

**Primary resource:** https://fastapi.tiangolo.com/tutorial/  
**Exact scope:** Request/response models, dependencies, auth, tests; https://docs.stripe.com/webhooks — signature verification, retries, duplicates  
**Friday career action:** Prepare a 5-minute FDE customer-facing explanation

| Day / ticket | Implementation task | Required evidence |
|---|---|---|
| 036 / **E36** | Create Python fraud scoring API | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 037 / **E37** | Add schemas, input validation and tests | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 038 / **E38** | Add signed synthetic webhook ingestion and deduplication | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 039 / **E39** | Add tenant authz and negative access tests | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 040 / **E40** | Demo Java→event→Python integration; publish API contract | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |

**Week 08 exit gate:** Reject invalid signatures and cross-tenant access; duplicate events safe; integration reproducible.

### Week 09 — Applied AI and evaluation | Circuit days 041–045

**Primary resource:** https://platform.openai.com/docs  
**Exact scope:** Structured outputs and tool calling; https://genai.owasp.org/ — prompt injection and data leakage  
**Friday career action:** Apply to three relevant FDE/AI-platform roles

| Day / ticket | Implementation task | Required evidence |
|---|---|---|
| 041 / **E41** | Define investigation schema and allowlisted evidence fields | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 042 / **E42** | Generate structured case summary with citation fields | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 043 / **E43** | Add authorized read-only evidence retrieval tool | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 044 / **E44** | Create 30 synthetic golden and adversarial cases | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 045 / **E45** | Run evaluation, record unsupported claims, threat model | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |

**Week 09 exit gate:** Schema valid; evidence traceable; no unauthorized tool actions; report all failures.

### Week 10 — Deployment and operations | Circuit days 046–050

**Primary resource:** https://docs.docker.com/get-started/  
**Exact scope:** Images, containers, Compose; https://docs.github.com/en/actions — workflows; https://opentelemetry.io/docs/ — traces/metrics/logs  
**Friday career action:** Conduct production incident interview simulation

| Day / ticket | Implementation task | Required evidence |
|---|---|---|
| 046 / **E46** | Add Dockerfiles and Compose services | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 047 / **E47** | Add CI build/test workflow | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 048 / **E48** | Add structured logs, request IDs, basic metrics | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 049 / **E49** | Add traces, health and readiness endpoints | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 050 / **E50** | Inject three failures; document detection, recovery and postmortem | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |

**Week 10 exit gate:** Fresh-clone local startup, passing CI, request trace and three failure records.

### Week 11 — Technical leadership and LLD | Circuit days 051–055

**Primary resource:** https://google.github.io/eng-practices/review/  
**Exact scope:** Code review principles; https://adr.github.io/ — ADRs; https://refactoring.guru/design-patterns — Strategy, Factory, Observer, State  
**Friday career action:** Conduct tech-lead behavioral mock

| Day / ticket | Implementation task | Required evidence |
|---|---|---|
| 051 / **E51** | Refactor risk-rule engine for extensibility | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 052 / **E52** | Write three independently deliverable tickets with interfaces | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 053 / **E53** | Review three seeded defective PRs and give constructive feedback | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 054 / **E54** | Write stakeholder update, risk register, two-week delivery plan | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 055 / **E55** | Run 60-minute changed-requirement LLD defense; leadership scorecard | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |

**Week 11 exit gate:** New rule added without modifying core evaluator; reviews, RFC and planning artifacts defensible.

### Week 12 — Hiring proof and readiness | Circuit days 056–060

**Primary resource:** https://leetcode.com/studyplan/top-interview-150/  
**Exact scope:** Only weak-pattern revision; no new primary curriculum  
**Friday career action:** Publish final portfolio, resume and next hiring cycle

| Day / ticket | Implementation task | Required evidence |
|---|---|---|
| 056 / **E56** | Finalize README, setup and architecture | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 057 / **E57** | Run clean-clone reproduction and regression suite | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 058 / **E58** | Record five-minute demo; publish limitations and benchmarks | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 059 / **E59** | Run combined Java/LLD/HLD/behavioral defense; revise resume | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |
| 060 / **E60** | Run final acceptance suite, mock scorecard and 30/60/90-day onboarding plan | Code/document + relevant passing test output + commit hash; if unfinished, failing test and blocker |

**Week 12 exit gate:** Project demo reproducible; mocks scored; leadership evidence and resume truthful.

## Interview circuit — deterministic mapping

The uploaded source's **60 core sessions** are mapped to the 60 weekday slots in order. Its original eight calendar weeks plus four days become twelve five-day study weeks. **Appendix A reproduces the supplied circuit verbatim** to preserve every specific DSA cue, Java question, LLD/HLD assignment, mock, career reminder and optional Educative task. Source day numbering remains authoritative; do not use the source's original calendar-week labels as this hybrid's week numbers.

| Hybrid week | Source days | Core progression |
|---|---|---|
| 01 | 001–005 | DSA, LLD, Java, DSA, HLD |
| 02 | 006–010 | DSA repair, LLD, DSA, Java, Mock 1 |
| 03 | 011–015 | LLD, DSA, HLD, LLD, DSA repair |
| 04 | 016–020 | Java, DSA, LLD, DSA, Mock 2 |
| 05 | 021–025 | HLD, LLD, DSA repair, Java, LLD |
| 06 | 026–030 | Java, DSA, HLD, LLD, Mock 3 |
| 07 | 031–035 | DSA, Java, DSA repair, LLD, DSA |
| 08 | 036–040 | HLD, LLD, DSA, Java, Mock 4 |
| 09 | 041–045 | LLD, DSA repair, HLD, DSA, LLD |
| 10 | 046–050 | DSA, Java, LLD, DSA repair, Mock 5 |
| 11 | 051–055 | HLD, DSA, LLD, DSA, Java |
| 12 | 056–060 | DSA repair, HLD, LLD, DSA, Mock 6 |

### Interview scoring and readiness gates

- **DSA:** cold attempt; correctness tests, invariant, complexity, edge cases, explanation. Revision cue ≠ solved problem. Log unseen vs repeated.
- **Java/JVM:** explain without notes, demonstrate runnable example/failure, answer a changed constraint.
- **LLD:** requirements, interfaces, state invariants, tests, concurrency if applicable, changed requirement, defensible trade-off.
- **HLD:** requirements, capacity assumptions, API, data, consistency, failure recovery, monitoring, 10× change.
- **Mock:** uninterrupted attempt and postmortem; note pass/partial/fail by skill, not one blended feel-good score.
- **Leadership:** three ADRs, RFC, three code reviews, work breakdown, stakeholder update, incident postmortem, 30/60/90-day plan. Simulations do not substitute for leading actual colleagues.

## Exit deliverables and portfolio acceptance

- [ ] Java risk service: reproducible concurrency and financial-limit correctness tests.
- [ ] PostgreSQL authoritative reservation path with race/rollback tests and three explain plans.
- [ ] Kafka duplicate/replay handling and Redis outage fallback.
- [ ] JMH/JFR benchmark and one documented investigation.
- [ ] Capacity assumptions, SLOs, load test and HLD defense.
- [ ] Fraud ML baseline: data provenance, leakage-safe split, PR-AUC, validation threshold choice and untouched test evaluation.
- [ ] FastAPI integration: schema validation, signed synthetic webhook, idempotency and tenant authorization.
- [ ] AI evaluation: 30 synthetic cases, structured output, evidence checks and adversarial tests.
- [ ] Clean-clone Docker Compose setup, CI, tracing, metrics and three incident drills.
- [ ] Three ADRs, one RFC, three code reviews, delivery plan and leadership mock.
- [ ] Five **real** employer stories with no fabricated performance metrics or confidential information.
- [ ] All 60 interview sessions attempted, with actual passes, misses, mock scores and repair queue transparently recorded.
- [ ] Resume, five-minute demo, architecture narrative and first-90-days leadership plan.

## Standard daily log template

```markdown
# Day NNN | Date YYYY-MM-DD | Circuit DAY NNN | Engineering ENN
Start 08:00 | End 11:00 | Actual minutes: ___
DSA cues: G __ / Y __ / R __ | Cold problems passed: __
Circuit core: assigned __ | attempted __ | passed/partial/failed __
Engineering acceptance checks: __/__; critical failure: yes/no
Score: correctness __/40; failure __/20; tests __/20; explanation __/10; documentation __/10
Status: GREEN / YELLOW / RED / BLOCKED
Git commit: __ | Test command: __ | Evidence path: __
Biggest failure: __ | Next repair: __ | Next scheduled circuit/ticket: __
```

## Final 12-week outcome evaluation

| Readiness | Evidence-based pass condition |
|---|---|
| Interview | Timed unfamiliar DSA, Java, LLD, HLD, SQL and behavioral mocks with specific reviewed mistakes |
| Engineering | Fresh-clone reproduction, correctness under concurrency, recovery, deployment, telemetry and documented limitations |
| Technical lead | Defensible architecture decisions, code reviews, task decomposition, mentoring feedback and incident communication |
| Hiring | Truthful resume, demonstrable portfolio, applications/referrals and role-specific interview stories |

**Important:** The goal is credible evidence of growing competence, not a guarantee of an offer, a compensation figure, or immediate authority over a team.

---

# Appendix A — Original uploaded 60-Day Interview Circuit (verbatim source)

This appendix preserves the user's supplied text, including its original eight-week pacing, optional bonus tasks and references to separate PDFs. The separate **Java Core**, **HLD**, and **AlgoMaster LLD** PDFs cited in the source were not included in this attachment; page references below are preserved as-is, not independently verified. The hybrid plan's daily clock and week mapping above supersede the original pacing when they conflict.

```text
DAY / WEEK
PACE 🟢 7+ · 🟡 5–6 · 🔴 0–4	DSA FLOOR · 15m
4–5 CUES · LEETCODE + LOCAL JAVA	SESSION WORK
☐ CORE · 90m REQUIRED
◇ BONUS · +90m OPTIONAL
🏁 WEEK 1 · DAYS 001–007 · TARGET DAY 007	35/263 CUES · 7/60 SESSIONS	CORE TO DATE: DSA 3 · LLD 2 · HLD 1 · Java 1 · Mock 0

ACTUAL: __ sessions · status __

CAREER TARGET 2/18 · ACTUAL __ · CALIBRATE: ___ → ___
▶ DAY 001 · DSA PATTERN	☐ DSA 001–005 · CUES
S014 Validate Binary Search Tree · Java
S029 House Robber · Java
S003 Longest Substring Without Repeating Characters · Java
S105 Constrained Subsequence Sum · Java
S165 Edit Distance · Java	☐ 2 COLD PROBLEMS
First two Floor problems: 35m each; 20m tests, invariant, complexity, and explanation.

◇ THIRD PROBLEM + EDUCATIVE 01/22
Cold-code the third Floor problem for 45m.
Educative project 01/22 — Netflix. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 002 · LLD COMPLETE DESIGN	☐ DSA 006–010 · CUES
S008 Merge Two Sorted Lists · Java
S077 Verify Preorder Serialization Of A Binary Tree · Java
S073 Search Insert Position · Java
S002 Binary Search · Java
S010 Merge K Sorted Lists · Java	☐ LLD 01/15 · THEORY + LOCAL CASE
LLD 01/21 — What LLD is, LLD vs HLD, interview forms. AlgoMaster LLD. Exit: one-page interview flow + 90-second answer.
LLD 02/21 — Classes, objects, enums, interfaces. AlgoMaster LLD. Exit: small Java model + invariant.
L01 Parking Lot · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.

CAREER · 30m: Naukri.
DAY 003 · JAVA / BACKEND / JVM / SPRING / TRADING	☐ DSA 011–015 · CUES
S032 Valid Parentheses · Java
S046 Binary Subarrays With Sum · Java
V017 Max Stack · Java
S172 Path Sum II · Java
S225 Restore IP Addresses · Java	☐ JAVA SET 01/9 · 2 QUESTIONS
J01 What must equals and hashCode guarantee for a map key? · related Java (map usage only)
Source: Java Core — 1.1 Answer construction and senior reasoning; 2.1 Values/references, parameter passing, dispatch; 2.2 Interfaces, abstract classes, inheritance, composition. PDF p13. Exit: cold answers + one example/failure + one follow-up per topic.

J02 How do HashMap collisions and resizing affect lookup? · related Java (map usage only)
Source: Java Core — 2.3 Equality, hashing, ordering contracts; 2.4 Immutable objects and defensive copies; 2.5 Generics, erasure, PECS. PDF p18. Exit: cold answers + one example/failure + one follow-up per topic.

◇ FAILURE / RACE / PRODUCTION CHANGE
One failing example, race test, JVM observation, or changed production constraint.
DAY 004 · DSA TIMED	☐ DSA 016–020 · CUES
S085 Middle of the Linked List · Java
S011 Two Sum II - Input Array Is Sorted · Java
S006 Reverse Linked List · Java
S036 Implement Trie (Prefix Tree) · Java
V007 Find All Anagrams In A String · Java	☐ 2 TIMED PROBLEMS
First two Floor problems: 35m each; 20m walkthrough, edge cases, and rubric.

◇ THIRD PROBLEM + EDUCATIVE 02/22
Cold-code the third Floor problem for 45m.
Educative project 02/22 — Facebook. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 005 · HLD COMPLETE SYSTEM	☐ DSA 021–025 · CUES
S129 Next Greater Element I · Java
S213 Random Point in Non-overlapping Rectangles · Java
S202 Remove Duplicates from Sorted Array II · Java
S151 String To Integer Atoi · Java
S016 Number Of Islands · Java	☐ HLD 01/8 · TOPICS + LOCAL SYSTEM
HLD Part 1/12 — Framework, capacity estimation, scale. PDF p1. Exit: fresh diagram + requirements/scale/API/data/failure/trade-off + changed requirement.
HLD Part 2/12 — Networking. PDF p56. Exit: fresh diagram + requirements/scale/API/data/failure/trade-off + changed requirement.
H01 Design a short-link service · Java

◇ 10× + FAILURE RECOVERY
10× scale/SLA/consistency change; failure + recovery + signal + trade-off.

CAREER · 30m: Instahyre.
DAY 006 · DSA REPAIR	☐ DSA 026–030 · CUES
S031 Subsets · Java
V008 Balanced Binary Tree · Java
S093 Kth Largest Element In An Array · Java
S167 Add Binary · Java
S245 Design A Leaderboard · Java	☐ R/Y REPAIR + SECOND PROBLEM
Oldest unresolved R/Y replaces the first item; otherwise use the first two Floor problems. Use 45m repair + 30m second problem + 15m exact gap.

◇ THIRD PROBLEM + EDUCATIVE 03/22
Cold-code the third Floor problem for 45m.
Educative project 03/22 — Search Engine. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 007 · LLD COMPLETE DESIGN	☐ DSA 031–035 · CUES
S219 High Five · Java
S229 Palindrome Partitioning II · Java
V009 Partition Equal Subset Sum · Java
S082 First Unique Number · Java
S001 Two Sum · Java	☐ LLD 02/15 · THEORY + LOCAL CASE
LLD 03/21 — Encapsulation, abstraction, inheritance, polymorphism. AlgoMaster LLD. Exit: refactor weak model + composition trade-off.
LLD 04/21 — Association, aggregation, composition, dependency, realization. AlgoMaster LLD. Exit: class diagram + lifecycle ownership.
L02 LRU Cache · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.
🏁 WEEK 2 · DAYS 008–014 · TARGET DAY 014	70/263 CUES · 14/60 SESSIONS	CORE TO DATE: DSA 5 · LLD 4 · HLD 2 · Java 2 · Mock 1

ACTUAL: __ sessions · status __

CAREER TARGET 4/18 · ACTUAL __ · CALIBRATE: ___ → ___
DAY 008 · DSA PATTERN	☐ DSA 036–040 · CUES
S224 N-Queens · Java
S048 Ransom Note · Java
S217 Sum of Subarray Minimums · Java
V002 4Sum · Java
S069 Number Of Provinces · Java	☐ 2 COLD PROBLEMS
First two Floor problems: 35m each; 20m tests, invariant, complexity, and explanation.

◇ THIRD PROBLEM + EDUCATIVE 04/22
Cold-code the third Floor problem for 45m.
Educative project 04/22 — Google Calendar. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 009 · JAVA / BACKEND / JVM / SPRING / TRADING	☐ DSA 041–045 · CUES
S098 Find The Index Of The First Occurrence In A String · Java
S017 Course Schedule · Java
S039 Minimum Number of Arrows to Burst Balloons · Java
S057 Binary Tree Inorder Traversal · Java
S184 Distinct Subsequences II · Java	☐ JAVA SET 02/9 · 2 QUESTIONS
J03 What does ConcurrentHashMap.computeIfAbsent make atomic, and what does it not? · related Java (computeIfAbsent example)
Source: Java Core — 2.6 Strings, pool, interning, builders; 2.7 Collection selection, complexity, concurrent access; 2.8 HashMap internals and mutable keys. PDF p24. Exit: cold answers + one example/failure + one follow-up per topic.

J04 When is volatile insufficient compared with an atomic operation? · related Java (atomic update example)
Source: Java Core — 2.9 PriorityQueue, Deque, comparator safety; 3.1 Atomicity, visibility, ordering, happens-before; 3.2 synchronized, monitors, reentrancy. PDF p32. Exit: cold answers + one example/failure + one follow-up per topic.

◇ FAILURE / RACE / PRODUCTION CHANGE
One failing example, race test, JVM observation, or changed production constraint.
DAY 010 · MOCK + POSTMORTEM	☐ DSA 046–050 · CUES
S200 First Unique Character in a String · Java
S144 Parallel Courses · Java
S148 Sort Items by Groups Respecting Dependencies · Java
S150 Spiral Matrix · Java
S015 Lowest Common Ancestor Of A Binary Tree · Java	☐ MOCK 1/6 · 60m
START MOCK Compressed → uninterrupted candidate attempt → DONE

◇ GRADE + VIVA + ≤1 REPAIR
Follow-up: E01 Résumé impact: two-minute context/problem/action/result/lesson + two follow-ups; no invented metric.

CAREER · 30–45m: LinkedIn.
DAY 011 · LLD COMPLETE DESIGN	☐ DSA 051–055 · CUES
S052 Binary Tree Right Side View · Java
S233 Happy Number · Java
S143 Redundant Connection · Java
S171 Reverse Linked List II · Java
S121 Convert BST To Greater Tree · Java	☐ LLD 03/15 · THEORY + LOCAL CASE
LLD 05/21 — DRY, KISS, YAGNI, Law of Demeter, coupling/cohesion. AlgoMaster LLD. Exit: three corrections to one local project.
LLD 06/21 — SOLID. AlgoMaster LLD. Exit: one violation + corrected Java design.
L03 Splitwise · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.
DAY 012 · DSA TIMED	☐ DSA 056–060 · CUES
S030 Coin Change · Java
S019 Word Ladder · Java
S066 Best Time to Buy and Sell Stock · Java
S028 01 Matrix · Java
S166 Distinct Subsequences · Java	☐ 2 TIMED PROBLEMS
First two Floor problems: 35m each; 20m walkthrough, edge cases, and rubric.

◇ THIRD PROBLEM + EDUCATIVE 05/22
Cold-code the third Floor problem for 45m.
Educative project 05/22 — Stock Scraper. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.

CAREER · 30m: Delhi NCR official sites.
DAY 013 · HLD COMPLETE SYSTEM	☐ DSA 061–065 · CUES
S161 Map Sum Pairs · Java
S050 Reverse Nodes in k-Group · Java
S087 Sliding Window Maximum · Java
V012 Time Based Key-Value Store · Java
S221 Number of Orders in the Backlog · Java	☐ HLD 02/8 · TOPICS + LOCAL SYSTEM
HLD Part 3/12 — API communication and real-time systems. PDF p109. Exit: fresh diagram + requirements/scale/API/data/failure/trade-off + changed requirement.
HLD Part 4/12 — Databases, replication, partitioning, sharding. PDF p160. Exit: fresh diagram + requirements/scale/API/data/failure/trade-off + changed requirement.
H02 Design retrying multichannel delivery · Java

◇ 10× + FAILURE RECOVERY
10× scale/SLA/consistency change; failure + recovery + signal + trade-off.
DAY 014 · LLD COMPLETE DESIGN	☐ DSA 066–070 · CUES
V015 Maximal Rectangle · Java
S149 Can Place Flowers · Java
S070 Gas Station · Java
S038 Is Graph Bipartite? · Java
S027 Rotting Oranges · Java	☐ LLD 04/15 · THEORY + LOCAL CASE
LLD 07/21 — UML class, sequence, state-machine diagrams. AlgoMaster LLD. Exit: three diagrams for one system.
LLD 08/21 — Builder and Factory Method. AlgoMaster LLD. Exit: cold reconstruction + trigger difference.
L04 Elevator System · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.
🏁 WEEK 3 · DAYS 015–021 · TARGET DAY 021	105/263 CUES · 21/60 SESSIONS	CORE TO DATE: DSA 8 · LLD 5 · HLD 3 · Java 3 · Mock 2

ACTUAL: __ sessions · status __

CAREER TARGET 6/18 · ACTUAL __ · CALIBRATE: ___ → ___
DAY 015 · DSA REPAIR	☐ DSA 071–075 · CUES
S162 Maximum XOR With an Element From Array · Java
V006 Container With Most Water · Java
V003 Maximum Depth Of Binary Tree · Java
V010 Copy List With Random Pointer · Java
S022 Find First And Last Position Of Element In Sorted Array · Java	☐ R/Y REPAIR + SECOND PROBLEM
Oldest unresolved R/Y replaces the first item; otherwise use the first two Floor problems. Use 45m repair + 30m second problem + 15m exact gap.

◇ THIRD PROBLEM + EDUCATIVE 06/22
Cold-code the third Floor problem for 45m.
Educative project 06/22 — UBER. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.

CAREER · 30m: Bengaluru official sites.
DAY 016 · JAVA / BACKEND / JVM / SPRING / TRADING	☐ DSA 076–080 · CUES
S045 Find Median From Data Stream · Java
S072 Jump Game · Java
S147 Sequence Reconstruction · Java
S170 Shortest Palindrome · Java
V005 Maximum Subarray (Kadane) · Java	☐ JAVA SET 03/9 · 2 QUESTIONS
J05 When would you choose synchronized versus ReentrantLock? · related Java (lock comparison example)
Source: Java Core — 3.3 Races, compound actions, state ownership; 3.4 Safe publication, final fields, construction; 3.5 Atomic counters and LongAdder. PDF p41. Exit: cold answers + one example/failure + one follow-up per topic.

J06 What happens when a bounded executor queue fills? · related Java (bounded queue example)
Source: Java Core — 3.6 Deadlock, livelock, starvation, lock ordering; 3.7 Thread lifecycle, wait/notify, interruption; 3.8 Executors, capacity, queues, rejection. PDF p48. Exit: cold answers + one example/failure + one follow-up per topic.

◇ FAILURE / RACE / PRODUCTION CHANGE
One failing example, race test, JVM observation, or changed production constraint.
DAY 017 · DSA PATTERN	☐ DSA 081–085 · CUES
S164 Count Pairs With XOR in a Range · Java
S005 Minimum Window Substring · Java
S097 Evaluate Reverse Polish Notation · Java
S058 Invert Binary Tree · Java
S199 Repeated DNA Sequences · Java	☐ 2 COLD PROBLEMS
First two Floor problems: 35m each; 20m tests, invariant, complexity, and explanation.

◇ THIRD PROBLEM + EDUCATIVE 07/22
Cold-code the third Floor problem for 45m.
Educative project 07/22 — Amazon. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 018 · LLD COMPLETE DESIGN	☐ DSA 086–090 · CUES
S228 Word Break II · Java
S063 Surrounded Regions · Java
S127 Coloring A Border · Java
S024 Kth Smallest Element in a BST · Java
S226 Palindrome Partitioning · Java	☐ LLD 05/15 · THEORY + LOCAL CASE
LLD 09/21 — Adapter, Decorator, Facade. AlgoMaster LLD. Exit: one example proving structural differences.
LLD 10/21 — Strategy, Observer, State, Command. AlgoMaster LLD. Exit: trigger table + runnable implementation.
L05 Token Bucket Rate Limiter · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.
DAY 019 · DSA TIMED	☐ DSA 091–095 · CUES
S211 Longest Repeating Character Replacement · Java
S194 Number of Longest Increasing Subsequence · Java
S101 Remove Nth Node From End of List · Java
S084 Minimum Number Of Days To Make M Bouquets · Java
S215 Online Stock Span · Java	☐ 2 TIMED PROBLEMS
First two Floor problems: 35m each; 20m walkthrough, edge cases, and rubric.

◇ THIRD PROBLEM + EDUCATIVE 08/22
Cold-code the third Floor problem for 45m.
Educative project 08/22 — Zoom. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 020 · MOCK + POSTMORTEM	☐ DSA 096–100 · CUES
S040 Unique Paths · Java
S201 Random Flip Matrix · Java
S240 Design an Ordered Stream · Java
S157 Longest Word in Dictionary · Java
S023 LRU Cache · Java	☐ MOCK 2/6 · 60m
START MOCK Compressed → uninterrupted candidate attempt → DONE

◇ GRADE + VIVA + ≤1 REPAIR
Follow-up: E02 SQL: one join/window/index/isolation/slow-query problem + edge case + performance consequence.

CAREER · 30–45m: Warm referrals.
DAY 021 · HLD COMPLETE SYSTEM	☐ DSA 101–105 · CUES
S136 Sort Characters By Frequency · Java
S044 Word Search · Java
S234 Fraction to Recurring Decimal · Java
S177 Course Schedule IV · Java
S020 Koko Eating Bananas · Java	☐ HLD 03/8 · TOPICS + LOCAL SYSTEM
HLD Part 5/12 — Transactions, isolation, distributed consistency. PDF p212. Exit: fresh diagram + requirements/scale/API/data/failure/trade-off + changed requirement.
HLD Part 6/12 — Caching and thundering herd. PDF p266. Exit: fresh diagram + requirements/scale/API/data/failure/trade-off + changed requirement.
H03 Design leases and retrying workers · Java

◇ 10× + FAILURE RECOVERY
10× scale/SLA/consistency change; failure + recovery + signal + trade-off.
🏁 WEEK 4 · DAYS 022–028 · TARGET DAY 028	135/263 CUES · 28/60 SESSIONS	CORE TO DATE: DSA 10 · LLD 7 · HLD 4 · Java 5 · Mock 2

ACTUAL: __ sessions · status __

CAREER TARGET 8/18 · ACTUAL __ · CALIBRATE: ___ → ___
DAY 022 · LLD COMPLETE DESIGN	☐ DSA 106–110 · CUES
S180 Best Time to Buy and Sell Stock with Transaction Fee · Java
S120 Binary Search Tree Iterator · Java
S209 Consecutive Characters · Java
S138 Merge Intervals · Java
S207 Move Zeroes · Java	☐ LLD 06/15 · THEORY + LOCAL CASE
LLD 11/21 — Repository, DI, Producer-Consumer, Thread Pool. AlgoMaster LLD. Exit: backend flow + ownership/capacity/failure.
LLD 12/21 — Entity discovery, clean code, patterns, concurrency. AlgoMaster LLD. Exit: five-minute LLD opening script.
L06 BookMyShow · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.

CAREER · 30m: Dubai.
DAY 023 · DSA REPAIR	☐ DSA 111–115 · CUES
S033 Top K Frequent Elements · Java
S175 Lowest Common Ancestor Of A Binary Tree IV · Java
V014 Permutation In String · Java
S117 All Nodes Distance K in Binary Tree · Java
S155 Longest Happy Prefix · Java	☐ R/Y REPAIR + SECOND PROBLEM
Oldest unresolved R/Y replaces the first item; otherwise use the first two Floor problems. Use 45m repair + 30m second problem + 15m exact gap.

◇ THIRD PROBLEM + EDUCATIVE 09/22
Cold-code the third Floor problem for 45m.
Educative project 09/22 — Plagiarism Checker. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 024 · JAVA / BACKEND / JVM / SPRING / TRADING	☐ DSA 116–119 · CUES
S047 Majority Element · Java
S065 Minimum Height Trees · Java
S153 Maximum XOR of Two Numbers in an Array · Java
S034 Daily Temperatures · Java	☐ JAVA SET 04/9 · 2 QUESTIONS
J07 How do you time out and cancel a CompletableFuture chain? · related Java (timeout example; cancellation not shown)
Source: Java Core — 3.9 Callable, Future, completion stages; 3.10 CompletableFuture composition/errors/cancellation; 3.11 ThreadLocal cleanup and context propagation. PDF p58. Exit: cold answers + one example/failure + one follow-up per topic.

J08 Name a happens-before relation that makes a cross-thread read safe. · related Java (cross-thread state example)
Source: Java Core — 3.12 Risk limits, atomic reservations, partition ownership; 3.13 Testing concurrency and shutdown; 4.1 JVM bytecode and execution model. PDF p65. Exit: cold answers + one example/failure + one follow-up per topic.

◇ FAILURE / RACE / PRODUCTION CHANGE
One failing example, race test, JVM observation, or changed production constraint.
DAY 025 · LLD COMPLETE DESIGN	☐ DSA 120–123 · CUES
S012 Trapping Rain Water · Java
S232 Concatenated Words · Java
S059 Construct Binary Search Tree From Preorder Traversal · Java
S076 Design Browser History · Java	☐ LLD 07/15 · THEORY + LOCAL CASE
LLD 13/21 — Parking Lot. AlgoMaster LLD. Exit: cold API/model/allocation invariant/tests/change.
L07 Fraud Pattern Detection · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.

CAREER · 30m: London · wider Europe.
DAY 026 · JAVA / BACKEND / JVM / SPRING / TRADING	☐ DSA 124–127 · CUES
S124 Max Area Of Island · Java
S128 Sqrtx · Java
S227 String Compression II · Java
S007 Linked List Cycle · Java	☐ JAVA SET 05/9 · 2 QUESTIONS
J09 Distinguish heap, stack, and metaspace symptoms in a Java service. · related Java (load generator; diagnosis not shown)
Source: Java Core — 4.2 Runtime memory areas and stack frames; 4.3 Reachability roots, cycles, collection eligibility; 4.4 Memory leaks and retained references. PDF p73. Exit: cold answers + one example/failure + one follow-up per topic.

J10 How would you investigate a GC pause in production? · related Java (load generator; GC investigation not shown)
Source: Java Core — 4.5 Heap dumps, retained size, dominators; 4.6 GC diagnosis, allocation rate, heap sizing; 4.7 Performance regressions and bottlenecks. PDF p82. Exit: cold answers + one example/failure + one follow-up per topic.

◇ FAILURE / RACE / PRODUCTION CHANGE
One failing example, race test, JVM observation, or changed production constraint.
DAY 027 · DSA PATTERN	☐ DSA 128–131 · CUES
S222 Car Pooling · Java
S089 Design Add and Search Words Data Structure · Java
S103 Count Number Of Nice Subarrays · Java
S140 Partition Labels · Java	☐ 2 COLD PROBLEMS
First two Floor problems: 35m each; 20m tests, invariant, complexity, and explanation.

◇ THIRD PROBLEM + EDUCATIVE 10/22
Cold-code the third Floor problem for 45m.
Educative project 10/22 — Network. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 028 · HLD COMPLETE SYSTEM	☐ DSA 132–135 · CUES
S218 Ipo · Java
S193 Perfect Squares · Java
S142 Permutations · Java
S025 Diameter of Binary Tree · Java	☐ HLD 04/8 · TOPICS + LOCAL SYSTEM
HLD Part 7/12 — Async processing, queues, pub/sub, event streaming. PDF p304. Exit: fresh diagram + requirements/scale/API/data/failure/trade-off + changed requirement.
HLD Part 8/12 — Load balancers, gateways, discovery, service mesh. PDF p345. Exit: fresh diagram + requirements/scale/API/data/failure/trade-off + changed requirement.
H04 Design idempotent order processing · Java

◇ 10× + FAILURE RECOVERY
10× scale/SLA/consistency change; failure + recovery + signal + trade-off.
🏁 WEEK 5 · DAYS 029–035 · TARGET DAY 035	163/263 CUES · 35/60 SESSIONS	CORE TO DATE: DSA 13 · LLD 9 · HLD 4 · Java 6 · Mock 3

ACTUAL: __ sessions · status __

CAREER TARGET 11/18 · ACTUAL __ · CALIBRATE: ___ → ___
DAY 029 · LLD COMPLETE DESIGN	☐ DSA 136–139 · CUES
S239 Missing Number · Java
S004 Product Of Array Except Self · Java
V001 3Sum · Java
S242 Design Hit Counter · Java	☐ LLD 08/15 · THEORY + LOCAL CASE
LLD 14/21 — Elevator or Vending Machine. AlgoMaster LLD. Exit: state machine + scheduling/transitions.
L08 DesignRedis · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.
DAY 030 · MOCK + POSTMORTEM	☐ DSA 140–143 · CUES
S216 Remove K Digits · Java
S181 Best Time to Buy and Sell Stock with Cooldown · Java
S119 Recover Binary Search Tree · Java
S041 Longest Increasing Subsequence · Java	☐ MOCK 3/6 · 60m
START MOCK Compressed → uninterrupted candidate attempt → DONE

◇ GRADE + VIVA + ≤1 REPAIR
Follow-up: E03 Project architecture: entry/data path/state/concurrency/failure/observability/limitation + one change.

CAREER · 30–45m: Germany · Netherlands.
MIDPOINT: preserve the arrow; name ≤3 pass-changing repairs; never restart.
DAY 031 · DSA TIMED	☐ DSA 144–147 · CUES
S043 Longest Substring With At Most K Distinct Characters · Java
S013 Binary Tree Level Order Traversal · Java
S210 Max Consecutive Ones · Java
S116 Search In A Binary Search Tree · Java	☐ 2 TIMED PROBLEMS
First two Floor problems: 35m each; 20m walkthrough, edge cases, and rubric.

◇ THIRD PROBLEM + EDUCATIVE 11/22
Cold-code the third Floor problem for 45m.
Educative project 11/22 — Cyber Security. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 032 · JAVA / BACKEND / JVM / SPRING / TRADING	☐ DSA 148–151 · CUES
S137 Insert Interval · Java
S160 Short Encoding of Words · Java
S060 Sum Root To Leaf Numbers · Java
S064 Accounts Merge · Java	☐ JAVA SET 06/9 · 2 QUESTIONS
J11 Why can Spring @Transactional self-invocation fail to start a transaction? · related Java (@Transactional example; self-invocation not shown)
Source: Java Core — 4.8 JFR, thread dumps, diagnostic evidence; 4.9 Throughput, tail latency, allocation cost; 4.10 Backpressure, queue growth, timeouts, Little's Law. PDF p88. Exit: cold answers + one example/failure + one follow-up per topic.

J12 How do isolation and optimistic locking affect a concurrent update? · related Java (@Version example; isolation not shown)
Source: Java Core — 4.11 Optimization workflow and verification; 5.1 Functional interfaces, lambdas, identity; 5.2 Stream laziness and single use. PDF p93. Exit: cold answers + one example/failure + one follow-up per topic.

◇ FAILURE / RACE / PRODUCTION CHANGE
One failing example, race test, JVM observation, or changed production constraint.

CAREER · 30m: Australia.
DAY 033 · DSA REPAIR	☐ DSA 152–155 · CUES
S037 Flood Fill · Java
S174 Lowest Common Ancestor Of A Binary Tree III · Java
S208 Longer Contiguous Segments of Ones than Zeros · Java
S195 Russian Doll Envelopes · Java	☐ R/Y REPAIR + SECOND PROBLEM
Oldest unresolved R/Y replaces the first item; otherwise use the first two Floor problems. Use 45m repair + 30m second problem + 15m exact gap.

◇ THIRD PROBLEM + EDUCATIVE 12/22
Cold-code the third Floor problem for 45m.
Educative project 12/22 — Operating System. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 034 · LLD COMPLETE DESIGN	☐ DSA 156–159 · CUES
S068 Search In Rotated Sorted Array II · Java
S092 Task Scheduler · Java
S088 Construct Binary Tree From Inorder And Postorder Traversal · Java
S214 Random Pick with Weight · Java	☐ LLD 09/15 · THEORY + LOCAL CASE
LLD 15/21 — LRU Cache. AlgoMaster LLD. Exit: runnable Java + invariant/tests/concurrency.
L09 URL Shortener · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.
DAY 035 · DSA PATTERN	☐ DSA 160–163 · CUES
S220 Sliding Window Median · Java
V011 Largest Rectangle In Histogram · Java
S099 Basic Calculator · Java
S102 Longest Palindromic Substring · Java	☐ 2 COLD PROBLEMS
First two Floor problems: 35m each; 20m tests, invariant, complexity, and explanation.

◇ THIRD PROBLEM + EDUCATIVE 13/22
Cold-code the third Floor problem for 45m.
Educative project 13/22 — Language Compiler. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.

CAREER · 30m: Naukri.
🏁 WEEK 6 · DAYS 036–042 · TARGET DAY 042	191/263 CUES · 42/60 SESSIONS	CORE TO DATE: DSA 15 · LLD 11 · HLD 5 · Java 7 · Mock 4

ACTUAL: __ sessions · status __

CAREER TARGET 13/18 · ACTUAL __ · CALIBRATE: ___ → ___
DAY 036 · HLD COMPLETE SYSTEM	☐ DSA 164–167 · CUES
S236 Linked List Random Node · Java
S192 Min Cost Climbing Stairs · Java
S062 Pacific Atlantic Water Flow · Java
V004 Valid Palindrome · Java	☐ HLD 05/8 · TOPICS + LOCAL SYSTEM
HLD Part 9/12 — Monoliths, microservices, circuit breakers, rate limiting. PDF p380. Exit: fresh diagram + requirements/scale/API/data/failure/trade-off + changed requirement.
H05 Design market-data ingest and fan-out · Java

◇ 10× + FAILURE RECOVERY
10× scale/SLA/consistency change; failure + recovery + signal + trade-off.
DAY 037 · LLD COMPLETE DESIGN	☐ DSA 168–171 · CUES
S231 Decode Ways · Java
S212 Linked List Cycle II · Java
S118 Amount of Time for Binary Tree to Be Infected · Java
S244 Logger Rate Limiter · Java	☐ LLD 10/15 · THEORY + LOCAL CASE
LLD 16/21 — Splitwise. AlgoMaster LLD. Exit: expense model + balance invariant/settlement/tests.
L10 IPO Allotment · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.
DAY 038 · DSA TIMED	☐ DSA 172–175 · CUES
S223 Sudoku Solver · Java
S083 Capacity To Ship Packages Within D Days · Java
S135 H-Index · Java
S042 Combination Sum · Java	☐ 2 TIMED PROBLEMS
First two Floor problems: 35m each; 20m walkthrough, edge cases, and rubric.

◇ THIRD PROBLEM + EDUCATIVE 14/22
Cold-code the third Floor problem for 45m.
Educative project 14/22 — Boggle. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 039 · JAVA / BACKEND / JVM / SPRING / TRADING	☐ DSA 176–179 · CUES
S179 Encode And Decode Tinyurl · Java
S026 Path Sum III · Java
S182 Best Time to Buy and Sell Stock III · Java
S049 Intersection of Two Linked Lists · Java	☐ JAVA SET 07/9 · 2 QUESTIONS
J13 Make a retried REST write idempotent; where is the key stored? · related Java (idempotency-key example)
Source: Java Core — 5.3 Filtering, mapping, flattening; 5.4 Distinct, equality, duplicate detection; 5.5 Sorting, ranking, limiting, skipping. PDF p99. Exit: cold answers + one example/failure + one follow-up per topic.

J14 What does a Java record make immutable, and what can remain mutable? · related Java (record example; mutable component not shown)
Source: Java Core — 5.6 Finding, matching, extrema, Optional; 5.7 Reduction vs mutable collection; 5.8 Collectors and result mutability. PDF p107. Exit: cold answers + one example/failure + one follow-up per topic.

◇ FAILURE / RACE / PRODUCTION CHANGE
One failing example, race test, JVM observation, or changed production constraint.
DAY 040 · MOCK + POSTMORTEM	☐ DSA 180–183 · CUES
S104 Maximum Number of Robots Within Budget · Java
S115 Range Sum Of BST · Java
S206 Remove Duplicates from Sorted Array · Java
S122 K Highest Ranked Items Within A Price Range · Java	☐ MOCK 4/6 · 60m
START MOCK Compressed → uninterrupted candidate attempt → DONE

◇ GRADE + VIVA + ≤1 REPAIR
Follow-up: E04 Behavioral: disagreement/incident/mentoring/prioritization/failure/influence story + follow-ups.

CAREER · 30–45m: Instahyre.
DAY 041 · LLD COMPLETE DESIGN	☐ DSA 184–187 · CUES
V013 Minimum Size Subarray Sum · Java
S178 Jump Game II · Java
S154 Design a Stack With Increment Operation · Java
S106 Jump Game VI · Java	☐ LLD 11/15 · THEORY + LOCAL CASE
LLD 17/21 — Payment Gateway. AlgoMaster LLD. Exit: state machine + idempotency/retries/recovery.
L11 Order Management System · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.
DAY 042 · DSA REPAIR	☐ DSA 188–191 · CUES
S067 Serialize And Deserialize Binary Tree · Java
S235 Shuffle an Array · Java
S185 Word Break · Java
S163 Maximum Genetic Difference Query · Java	☐ R/Y REPAIR + SECOND PROBLEM
Oldest unresolved R/Y replaces the first item; otherwise use the first two Floor problems. Use 45m repair + 30m second problem + 15m exact gap.

◇ THIRD PROBLEM + EDUCATIVE 15/22
Cold-code the third Floor problem for 45m.
Educative project 15/22 — Scrabble 2.0. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.

CAREER · 30m: LinkedIn.
🏁 WEEK 7 · DAYS 043–049 · TARGET DAY 049	219/263 CUES · 49/60 SESSIONS	CORE TO DATE: DSA 18 · LLD 13 · HLD 6 · Java 8 · Mock 4

ACTUAL: __ sessions · status __

CAREER TARGET 14/18 · ACTUAL __ · CALIBRATE: ___ → ___
DAY 043 · HLD COMPLETE SYSTEM	☐ DSA 192–195 · CUES
S081 Maximum Profit In Job Scheduling · Java
S009 Valid Anagram · Java
S090 Construct Binary Tree From Preorder And Inorder Traversal · Java
S237 Missing Ranges · Java	☐ HLD 06/8 · TOPICS + LOCAL SYSTEM
HLD Part 10/12 — Batch/stream, ETL, telemetry, availability, leader election. PDF p423. Exit: fresh diagram + requirements/scale/API/data/failure/trade-off + changed requirement.
H06 Design synchronous pre-trade controls · Java

◇ 10× + FAILURE RECOVERY
10× scale/SLA/consistency change; failure + recovery + signal + trade-off.
DAY 044 · DSA PATTERN	☐ DSA 196–199 · CUES
S191 Climbing Stairs · Java
S141 Letter Combinations Of A Phone Number · Java
S056 Swap Nodes In Pairs · Java
S152 Repeated Substring Pattern · Java	☐ 2 COLD PROBLEMS
First two Floor problems: 35m each; 20m tests, invariant, complexity, and explanation.

◇ THIRD PROBLEM + EDUCATIVE 16/22
Cold-code the third Floor problem for 45m.
Educative project 16/22 — Game. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 045 · LLD COMPLETE DESIGN	☐ DSA 200–203 · CUES
S173 Lowest Common Ancestor Of A Binary Tree II · Java
S018 Course Schedule II · Java
S169 Count Unique Characters of All Substrings of a Given String · Java
S241 Design Parking System · Java	☐ LLD 12/15 · THEORY + LOCAL CASE
LLD 18/21 — Online Stock Exchange. AlgoMaster LLD. Exit: order lifecycle + price-time/partial fill/concurrency.
L12 Pre-Trade Risk Engine · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.

CAREER · 30m: Delhi NCR official sites.
DAY 046 · DSA TIMED	☐ DSA 204–207 · CUES
S146 Find Eventual Safe States · Java
S156 Longest Common Prefix · Java
S198 Random Pick with Blacklist · Java
S021 Search In Rotated Sorted Array · Java	☐ 2 TIMED PROBLEMS
First two Floor problems: 35m each; 20m walkthrough, edge cases, and rubric.

◇ THIRD PROBLEM + EDUCATIVE 17/22
Cold-code the third Floor problem for 45m.
Educative project 17/22 — Stocks. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 047 · JAVA / BACKEND / JVM / SPRING / TRADING	☐ DSA 208–211 · CUES
S035 Meeting Rooms II · Java
S096 Next Greater Element II · Java
S078 Find Peak Element · Java
S197 Longest Continuous Increasing Subsequence · Java	☐ JAVA SET 08/9 · 3 QUESTIONS
J15 Explain PECS with one API signature. · related Java (generic API; PECS wildcard not shown)
Source: Java Core — 5.9 Lookup maps, duplicate keys, merge policies; 5.10 Grouping, downstream aggregation, partitioning; 5.11 Side effects, peek, encounter order. PDF p111. Exit: cold answers + one example/failure + one follow-up per topic.

J16 When is a Stream pipeline worse than a loop for a hot path? · related Java (Stream example; benchmark not shown)
Source: Java Core — 5.12 Parallel streams and constraints; 6.1 Throwable, exceptions, errors; 6.2 Resource lifetime and deterministic cleanup. PDF p120. Exit: cold answers + one example/failure + one follow-up per topic.

J17 How do try-with-resources and suppressed exceptions behave? · related Java (try-with-resources example; suppression not shown)
Source: Java Core — 6.3 Exception policies, ordering, translation; 6.4 Numeric precision, overflow, time; 7.1 Spring container, injection, lifecycle, scopes. PDF p126. Exit: cold answers + one example/failure + one follow-up per topic.

◇ FAILURE / RACE / PRODUCTION CHANGE
One failing example, race test, JVM observation, or changed production constraint.
DAY 048 · LLD COMPLETE DESIGN	☐ DSA 212–215 · CUES
S114 Minimum Absolute Difference In BST · Java
S053 Sort Colors · Java
S230 Extra Characters in a String · Java
S075 Moving Average From Data Stream · Java	☐ LLD 13/15 · THEORY + LOCAL CASE
LLD 19/21 — URL Shortener or Rate Limiter. AlgoMaster LLD. Exit: LLD API/state + HLD boundary.
L13 DesignOrderBook · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.
DAY 049 · DSA REPAIR	☐ DSA 216–219 · CUES
S183 Best Time to Buy and Sell Stock IV · Java
S130 Min Stack · Java
S051 Lowest Common Ancestor Of A Binary Search Tree · Java
S086 Meeting Rooms · Java	☐ R/Y REPAIR + SECOND PROBLEM
Oldest unresolved R/Y replaces the first item; otherwise use the first two Floor problems. Use 45m repair + 30m second problem + 15m exact gap.

◇ THIRD PROBLEM + EDUCATIVE 18/22
Cold-code the third Floor problem for 45m.
Educative project 18/22 — Computational Biology. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
🏁 WEEK 8 · DAYS 050–056 · TARGET DAY 056	247/263 CUES · 56/60 SESSIONS	CORE TO DATE: DSA 21 · LLD 14 · HLD 7 · Java 9 · Mock 5

ACTUAL: __ sessions · status __

CAREER TARGET 17/18 · ACTUAL __ · CALIBRATE: ___ → ___
DAY 050 · MOCK + POSTMORTEM	☐ DSA 220–223 · CUES
S126 Possible Bipartition · Java
S094 Kth Largest Element In A Stream · Java
S203 Remove Element · Java
S095 Word Search II · Java	☐ MOCK 5/6 · 60m
START MOCK Compressed → uninterrupted candidate attempt → DONE

◇ GRADE + VIVA + ≤1 REPAIR
Follow-up: E05 Trading/risk: order lifecycle/pre-trade control/market data/limits/venue/recovery + invariant/trade-off.

CAREER · 30–45m: Bengaluru official sites.
DAY 051 · HLD COMPLETE SYSTEM	☐ DSA 224–227 · CUES
S186 Interleaving String · Java
S091 Binary Tree Maximum Path Sum · Java
S107 Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit · Java
S123 Number Of Closed Islands · Java	☐ HLD 07/8 · TOPICS + LOCAL SYSTEM
HLD Part 11/12 — Case studies: WhatsApp, Tinder, Gmail. PDF p456. Exit: fresh diagram + requirements/scale/API/data/failure/trade-off + changed requirement.
H07 Design ordered venue sessions and recovery · Java

◇ 10× + FAILURE RECOVERY
10× scale/SLA/consistency change; failure + recovery + signal + trade-off.
DAY 052 · DSA PATTERN	☐ DSA 228–231 · CUES
S190 Minimum ASCII Delete Sum for Two Strings · Java
S054 Odd Even Linked List · Java
S159 Search Suggestions System · Java
S080 Split Array Largest Sum · Java	☐ 2 COLD PROBLEMS
First two Floor problems: 35m each; 20m tests, invariant, complexity, and explanation.

◇ THIRD PROBLEM + EDUCATIVE 19/22
Cold-code the third Floor problem for 45m.
Educative project 19/22 — Cellular Operator (AT&T). Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.

CAREER · 30m: Warm referrals.
DAY 053 · LLD COMPLETE DESIGN	☐ DSA 232–235 · CUES
S134 Top K Frequent Words · Java
S113 Insert Into A Binary Search Tree · Java
S205 String Compression · Java
S132 Implement Stack Using Queues · Java	☐ LLD 14/15 · THEORY + LOCAL CASE
LLD 20/21 — Pub-Sub, Chat, or Task Scheduler. AlgoMaster LLD. Exit: interfaces/state/backpressure/failure/tests.
L14 Matching Engine · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.
DAY 054 · DSA TIMED	☐ DSA 236–239 · CUES
S061 Network Delay Time · Java
V016 Substring With Concatenation Of All Words · Java
S110 Path Sum · Java
S074 Best Time to Buy and Sell Stock II · Java	☐ 2 TIMED PROBLEMS
First two Floor problems: 35m each; 20m walkthrough, edge cases, and rubric.

◇ THIRD PROBLEM + EDUCATIVE 20/22
Cold-code the third Floor problem for 45m.
Educative project 20/22 — Twitter. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 055 · JAVA / BACKEND / JVM / SPRING / TRADING	☐ DSA 240–243 · CUES
S187 Longest Common Subsequence · Java
S139 Non Overlapping Intervals · Java
S196 Maximum Length of Pair Chain · Java
S109 Shortest Subarray with Sum at Least K · Java	☐ JAVA SET 09/9 · 3 QUESTIONS
J18 How would you use an execution plan to challenge a slow SQL query? · related Java (query source; SQL execution plan not shown)
Source: Java Core — 7.2 AOP proxies and self-invocation; 7.3 Transaction propagation/isolation/rollback; 7.4 Local coordination vs shared authority. PDF p134. Exit: cold answers + one example/failure + one follow-up per topic.

J19 Sketch a thread-safe cache update and its race test. · related Java (thread-safe update; cache race test not shown)
Source: Java Core — 7.5 DB atomicity, optimistic/pessimistic locking; 7.6 Idempotency, retries, effect guarantees; 8.1 Timed coding, debugging, defense. PDF p139. Exit: cold answers + one example/failure + one follow-up per topic.

J20 How do you detect and prevent a lock-order deadlock? · related Java (deadlock example)
Source: Java Applied — 9.1 Modern Java/version boundaries; 9.2 Bounded caches, FIFO/LRU, dedup; 9.3 Sorted concurrent maps/skip lists; 9.4 Primitive streams, boxing, arrays; 9.5 Streams, loops, complexity. PDF p147. Exit: cold answers + one example/failure + one follow-up per topic.

◇ FAILURE / RACE / PRODUCTION CHANGE
One failing example, race test, JVM observation, or changed production constraint.

CAREER · 30m: Dubai.
DAY 056 · DSA REPAIR	☐ DSA 244–247 · CUES
S168 Count Primes · Java
S145 Alien Dictionary · Java
S100 Longest Palindrome · Java
S238 Random Pick Index · Java	☐ R/Y REPAIR + SECOND PROBLEM
Oldest unresolved R/Y replaces the first item; otherwise use the first two Floor problems. Use 45m repair + 30m second problem + 15m exact gap.

◇ THIRD PROBLEM + EDUCATIVE 21/22
Cold-code the third Floor problem for 45m.
Educative project 21/22 — Trees. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
🏁 FINISH · DAYS 057–060 · TARGET DAY 060 BY CALENDAR DAY 60	263/263 CUES · 60/60 SESSIONS	FINAL CORE: DSA 22 · LLD 15 · HLD 8 · Java 9 · Mock 6

CIRCUIT SCORE: G/Y/R evidence complete · ≤3 next-loop repairs chosen

CAREER 18/18 · REWARD: Circuit 1 complete
DAY 057 · HLD COMPLETE SYSTEM	☐ DSA 248–251 · CUES
S112 Binary Tree Preorder Traversal · Java
S189 Longest Palindromic Subsequence · Java
S176 Permutations II · Java
S071 Clone Graph · Java	☐ HLD 08/8 · TOPICS + LOCAL SYSTEM
HLD Part 12/12 — Case studies: Chess, Calling, Live Video, Analytics, TinyURL. PDF p490. Exit: fresh diagram + requirements/scale/API/data/failure/trade-off + changed requirement.
H08 Design gateway-to-exchange lifecycle · Java

◇ 10× + FAILURE RECOVERY
10× scale/SLA/consistency change; failure + recovery + signal + trade-off.
DAY 058 · LLD COMPLETE DESIGN	☐ DSA 252–255 · CUES
S158 Replace Words · Java
S125 Graph Valid Tree · Java
S243 Stock Price Fluctuation · Java
V018 Design Circular Queue · Java	☐ LLD 15/15 · THEORY + LOCAL CASE
LLD 21/21 — Unseen machine-coding rehearsal. AlgoMaster LLD. Exit: 60-minute cold attempt + score + one repair.
L15 FIX Session Manager · Java

◇ CHANGE + TESTS
Changed requirement + tests; defend model/API/invariant/state.
DAY 059 · DSA PATTERN	☐ DSA 256–259 · CUES
S079 First Bad Version · Java
S133 K Closest Points To Origin · Java
S055 Rotate List · Java
S204 Find the Duplicate Number · Java	☐ 2 COLD PROBLEMS
First two Floor problems: 35m each; 20m tests, invariant, complexity, and explanation.

◇ THIRD PROBLEM + EDUCATIVE 22/22
Cold-code the third Floor problem for 45m.
Educative project 22/22 — Miscellaneous. Decode Coding Interview in Java. Selected feature cold + one unseen variation + tests/invariant/complexity/explanation.
DAY 060 · MOCK + POSTMORTEM	☐ DSA 260–263 · CUES
S131 Implement Queue Using Stacks · Java
S108 Max Value of Equation · Java
S111 Binary Tree Postorder Traversal · Java
S188 Delete Operation for Two Strings · Java	☐ MOCK 6/6 · 60m
START MOCK Compressed → uninterrupted candidate attempt → DONE

◇ GRADE + VIVA + ≤1 REPAIR
Follow-up: E06 Communication: explain today's LLD/HLD for ten minutes, then five minutes of interruptions.

CAREER · 30–45m: London · wider Europe.
LOOP REVIEW: record G/Y/R and ≤3 repairs. Next loop promotes unresolved R/Y, then least-recently-coded DSA; all 263 cues repeat.
Route: DAY 001 · NEXT CIRCUIT
```
