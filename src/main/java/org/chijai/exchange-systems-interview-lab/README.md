# Exchange Systems Interview Lab

One repository, two domain modules, three learning views.

```text
docs / Mermaid
"I can explain it"
        ↓
single-file interview versions
"I can reconstruct it"
        ↓
multi-file Maven modules + tests
"I understand how to design it properly"
```

## Modules

### `risk-engine/`
Pre-trade risk decision engine.

Core invariant:

```text
READ STATE
→ CALCULATE CANDIDATES
→ VALIDATE ALL
→ COMMIT ALL
```

A rejected order must cause **zero partial state mutation**.

Interview progression:

```text
L0 Single Owner
    ↓
L1 Concurrent Shared State
    ↓
L2 Synchronous Event Bus
    ↓
L3 Partitioned Organization
    ↓
L4 Partition-Owned Workers
```

### `matching-engine/`
Simple price-time-priority order book.

Core invariant:

```text
best price first
→ FIFO within a price level
→ incoming order matches resting liquidity
→ execution uses resting order price
```

## Build

```bash
mvn test
```

Requires Java 17+ and Maven.

## Study order

```text
docs/07_MINIMUM_TO_MEMORIZE.md
        ↓
docs/01_PTR_INTERVIEW_MASTER_SCRIPT.md
        ↓
docs/03_NEGATIVE_EXPOSURE_TECHNICAL_CHALLENGE.md
        ↓
risk-engine/interview-versions/L0_RiskLimitEngineSingleOwner.java
        ↓
risk-engine/interview-versions/L1_RiskLimitEngineConcurrent.java
        ↓
matching-engine/interview-version/MatchingEngineInterview.java
        ↓
deep concurrency versions only after the basics are automatic
```

## Important source discipline

The repo separates:

- **actual/source-grounded PTR facts**;
- **user-provided source-code mapping**;
- **interview-design exercises**;
- **public analogies**.

Do not turn an interview-design partition model or a public Redis/LMAX analogy into a claim about the deployed PTR topology.


## No-Maven verification

If Maven is unavailable, compile the production sources and all self-contained interview versions with:

```bash
./verify-with-javac.sh
```

The script uses only `javac` and `java`.
