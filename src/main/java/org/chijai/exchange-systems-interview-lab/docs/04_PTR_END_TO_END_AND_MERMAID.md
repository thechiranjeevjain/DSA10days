# PTR End-to-End + Mermaid Diagrams

The attached design PDFs are useful because they create a clear zoom sequence:

```text
SYSTEM FLOW
   ↓
RISK COMPONENT
   ↓
CLASS DESIGN
   ↓
CODE
   ↓
CONCURRENCY EVOLUTION
```

Use only the first two diagrams by default in a live interview.

---

# 1. Source-safe high-level PTR flow

```mermaid
flowchart LR
    IN["Incoming order"] --> ME["Matching-engine environment"]
    ME --> PTR["PTR risk plugin<br/>in same JVM"]
    PTR --> STATE["In-memory risk state<br/>configured limits + consumption"]
    STATE --> PTR

    PTR -->|"PASS"| BOOK["Order may continue<br/>toward order book"]
    PTR -->|"BREACH"| REJ["Reject"]

    CTRL["Control plane<br/>configuration / persistence / recovery<br/>security / observability / operations"]
    --> STATE
```

## 60-second narration

> The order reaches the matching-engine environment and PTR must produce a synchronous decision before the order can continue toward the order book. The risk plugin executes inside the matching-engine JVM, and the hot decision uses in-memory state to avoid a database or remote-service dependency on every order.
>
> The state is not static: orders, cancellations, trades and configuration changes affect what the next decision sees. Separately, the control plane is responsible for configuration, persistence, recovery, observability, security and operational concerns.
>
> The important architectural boundary is that the hot path consumes already-ready state. The negative-exposure incident happened when startup declared readiness before some deferred DEFAULT PTLG state was actually installed.

---

# 2. Interview/system-design high-level diagram

The attached high-level PDF contains a richer conceptual design. Use this when discussing the exercise, not automatically as a factual production diagram.

```mermaid
flowchart LR
    P["Participant<br/>Trader / Broker"]

    subgraph EX["Exchange — interview/system view"]
        ME["Matching Engine<br/>Primary validation"]
        PTR["Pre-Trade Risk<br/>RiskEngine<br/>RiskCheckGroup"]
        OB["Order Book<br/>Bids / Asks<br/>Price-Time Priority"]

        CFG["Config / Reference Data<br/>Accounts / Limits / Groups"]
        OP["Operator Commands<br/>Kill switch / block / unblock"]
    end

    EXEC["Execution"]
    REJ["Rejected"]

    P -->|"order"| ME
    ME -->|"pass"| PTR
    ME -->|"fail"| REJ

    CFG --> PTR
    OP --> PTR

    PTR -->|"pass"| OB
    PTR -->|"breach"| REJ

    OB -->|"match"| EXEC
    EXEC --> P
```

Do not claim a specific REST API, UI, DB or protocol from this diagram unless independently confirmed.

---

# 3. 60–90 second class diagram

This is the best default class-level drawing from the attached PDFs.

```mermaid
classDiagram
    class RiskEngine {
        -ConcurrentHashMap~Integer, RiskCheckGroup~ groups
        +register(Account, RiskCheckGroup)
        +validate(Order, long) ExposureResults
        +onCancel(Order)
    }

    class Order {
        +Account account
        +String ticker
        +Side side
        +long price
        +long quantity
    }

    class Account {
        +int id
        +String name
    }

    class ExposureResults {
        +boolean accepted
        +String reason
        +reject(String)
    }

    class RiskCheckGroup {
        -List~RiskCheck~ checks
        +addCheck(RiskCheck)
        +begin()
        +rollback()
        +check(Order, long, ExposureResults) Result
        +onCancel(Order)
    }

    class RiskCheck {
        <<interface>>
        +check(Order, long, ExposureResults) Result
        +begin()
        +rollback()
    }

    RiskEngine --> Order : validates
    Order --> Account
    RiskEngine --> ExposureResults : returns
    RiskEngine --> RiskCheckGroup : account/group lookup
    RiskCheckGroup o-- RiskCheck : contains
```

## Spoken explanation

> RiskEngine is the orchestration boundary. It finds the applicable risk group for the account and asks that group to evaluate the order. The group owns the set of checks that have to behave as one logical decision. Individual `RiskCheck` implementations own rule-specific logic. That keeps orchestration separate from the rules without forcing the CoderPad implementation into a large framework.

---

# 4. Detailed class hierarchy

Only draw this if the interviewer asks about extension or rollback.

```mermaid
classDiagram
    class RiskCheck {
        <<interface>>
        +check(Order, long, ExposureResults) Result
        +begin()
        +rollback()
    }

    class AbstractRiskCheck {
        +String name
        +long limit
        +long consumption
        -long lastConsumption
        +begin()
        +rollback()
    }

    class MaxQtyCheck {
        +String ticker
        +check(Order, long, ExposureResults) Result
        +onCancel(Order)
    }

    class TotalTradedPerTimeCheck {
        -long windowStartMs
        -long lastWindowMs
        +begin()
        +rollback()
        +check(Order, long, ExposureResults) Result
    }

    class KillSwitchCheck {
        -boolean active
        +activate()
        +deactivate()
        +check(Order, long, ExposureResults) Result
    }

    RiskCheck <|.. AbstractRiskCheck
    AbstractRiskCheck <|-- MaxQtyCheck
    AbstractRiskCheck <|-- TotalTradedPerTimeCheck
    AbstractRiskCheck <|-- KillSwitchCheck
```

## Why this design can be useful

```text
RiskEngine        = orchestration
RiskCheckGroup    = decision boundary / grouped invariant
RiskCheck         = individual rule
AbstractRiskCheck = shared consumption snapshot/rollback behavior
```

But for only two checks in a 30-minute exercise, direct candidate-state calculation may be easier to write.

---

# 5. Group transaction / rollback

The attached Java foundation uses `begin()` and `rollback()` because individual checks may mutate their own consumption during evaluation.

```mermaid
flowchart TD
    O["Order"] --> G["RiskCheckGroup"]
    G --> B["begin() all checks<br/>snapshot state"]
    B --> C1["RiskCheck 1"]
    C1 -->|"BREACH"| RB["rollback all checks"]
    C1 -->|"PASS"| C2["RiskCheck 2"]
    C2 -->|"BREACH"| RB
    C2 -->|"PASS"| C3["RiskCheck 3"]
    C3 -->|"BREACH"| RB
    C3 -->|"PASS"| OK["ACCEPT<br/>keep mutations"]
    RB --> NO["REJECT"]
```

## Simpler CoderPad equivalent

```text
read current state
→ compute candidate position
→ compute candidate notional
→ validate both
→ commit only if both pass
```

That is easier to reconstruct and avoids rollback machinery.

---

# 6. Source-grounded startup/control-plane boundary

```mermaid
flowchart TD
    R["Normal reference/risk data"] --> D["Deferred DEFAULT PTLG work"]
    D --> C["RiskCheckContainers ready"]
    C --> READY["cacheLoadComplete / readiness"]
    READY --> REPLAY["GTC replay / normal processing"]
```

The key rule:

> **Readiness must mean all required downstream risk state is usable, including deferred work.**

---

# 7. Failure ordering from the negative-exposure incident

```mermaid
flowchart TD
    A["Normal active data processed"] --> B["cacheLoadComplete set too early"]
    B --> C["GTC replay can progress"]
    C --> D["RiskCheckContainer still missing"]
    D --> E["Initial exposure increment skipped"]
    E --> F["DEFAULT PTLG limit later installed"]
    F --> G["Cancel arrives"]
    G --> H["Exposure decremented"]
    H --> I["Negative exposure"]
```

---

# 8. Attached Java architecture progression

## Level 1 — direct concurrent group access

```mermaid
flowchart LR
    A["Caller A"] --> MAP["ConcurrentHashMap<br/>account -> RiskCheckGroup"]
    B["Caller B"] --> MAP

    MAP --> G1["Group A<br/>synchronized(group)"]
    MAP --> G2["Group B<br/>synchronized(group)"]

    G1 --> R1["Risk checks"]
    G2 --> R2["Risk checks"]
```

Different groups can be processed independently; same group is serialized.

---

# 9. Level 2 — in-process synchronous message bus

```mermaid
flowchart LR
    ME["Matching Engine"] -->|"OrderEvent"| BUS["MessageBus"]
    CFG["Config"] -->|"ConfigEvent"| BUS
    CMD["Command"] -->|"CommandEvent"| BUS

    BUS --> L["RiskEngineListener"]
    L --> E["RiskEngine"]
    E --> RES["ExposureResults"]
    RES --> ME
```

Important: in the attached implementation, `publish()` invokes handlers directly. This is event-driven integration, not asynchronous worker execution.

---

# 10. Level 3a — partitioned organization, caller-thread execution

```mermaid
flowchart TD
    O["Order"] --> H["accountId -> partition"]
    H --> P0["Partition 0"]
    H --> P1["Partition 1"]
    H --> P2["Partition 2"]
    H --> P3["Partition 3"]

    P0 --> G0["RiskCheckGroup(s)<br/>caller thread + group lock"]
    P1 --> G1["RiskCheckGroup(s)<br/>caller thread + group lock"]
    P2 --> G2["RiskCheckGroup(s)<br/>caller thread + group lock"]
    P3 --> G3["RiskCheckGroup(s)<br/>caller thread + group lock"]
```

Partitioning here creates an explicit sharding/ownership structure, but it does **not** itself replace the group locks.

---

# 11. Level 3b — desired true partition-owner model

```mermaid
flowchart TD
    E["Order / Cancel / Config / KillSwitch"] --> R["Route by ownership key"]

    R --> Q0["Bounded Queue 0"]
    R --> Q1["Bounded Queue 1"]
    R --> Q2["Bounded Queue 2"]
    R --> Q3["Bounded Queue 3"]

    Q0 --> W0["Single Worker 0"]
    Q1 --> W1["Single Worker 1"]
    Q2 --> W2["Single Worker 2"]
    Q3 --> W3["Single Worker 3"]

    W0 --> S0["Owned State 0"]
    W1 --> S1["Owned State 1"]
    W2 --> S2["Owned State 2"]
    W3 --> S3["Owned State 3"]
```

For the "zero locks inside partition" claim to be true, **all mutations** must go through the partition owner.

---

# 12. Matching-engine practice diagram

The attached `MatchingEngine.java` is a separate trading-domain drill.

```mermaid
classDiagram
    class OrderBook {
        +TreeMap bids
        +TreeMap asks
        +List trades
        +enterOrder(Order)
        -match()
        -add(Order)
        -remove(...)
    }

    class Order {
        +int id
        +String trader
        +Side side
        +int price
        +int qty
    }

    class Trade {
        +int price
        +int qty
        +int buyId
        +int sellId
    }

    OrderBook o-- Order
    OrderBook o-- Trade
```

Data-structure anchor:

```text
bids -> prices descending
asks -> prices ascending
same price -> FIFO queue
```

---

# 13. What to draw live

Default:

```text
FIRST:  high-level order -> risk -> decision flow      ~60 sec
SECOND: RiskEngine -> RiskCheckGroup -> RiskCheck     ~60 sec
THIRD:  start coding
```

Only draw detailed inheritance, startup sequencing or partitioning when the interviewer drills into that concern.

---

# 14. Synchronous Hot-Path Sequence — MUST KNOW

This is the clearest diagram for:

> "How does PTR interact with the matching engine?"

```mermaid
sequenceDiagram
    participant Client as Participant
    participant ME as Matching Engine
    participant PTR as PTR / Risk Engine
    participant State as In-Memory Risk State
    participant Book as Order Book

    Client->>ME: Order
    ME->>ME: Primary validation
    ME->>PTR: validate(order)
    PTR->>State: Read limits + consumption
    State-->>PTR: Current state
    PTR->>PTR: Evaluate risk checks

    alt BREACH
        PTR-->>ME: REJECT
        ME-->>Client: Rejection
    else PASS
        PTR-->>ME: ACCEPT
        ME->>Book: Continue toward order book
    end
```

Core point:

```text
ME calls PTR
→ PTR evaluates synchronously
→ PTR returns ACCEPT / REJECT
→ ME continues
```

---

# 15. Startup Lifecycle State Machine — MUST KNOW

This is the best visual for the negative-exposure readiness bug.

```mermaid
stateDiagram-v2
    [*] --> LoadingReferenceData

    LoadingReferenceData --> ProcessingDeferredDefaultPTLG:
        normal reference data loaded

    ProcessingDeferredDefaultPTLG --> RiskStateReady:
        DEFAULT PTLG limits installed
        RiskCheckContainers ready

    RiskStateReady --> ReplayGTC:
        readiness invariant satisfied

    ReplayGTC --> Live:
        recovery complete

    Live --> Live:
        orders / cancels / trades / config
```

Correct lifecycle:

```text
reference data
→ deferred DEFAULT PTLG work
→ RiskCheckContainers ready
→ readiness
→ GTC replay
→ live processing
```

The bug was effectively:

```text
reference data
→ readiness declared too early
→ GTC replay starts

while DEFAULT PTLG risk state is still incomplete
```

Memory line:

> **Readiness is a lifecycle invariant, not just a boolean flag.**

---

# 16. 10× / Multi-JVM Ownership — FOLLOW-UP ONLY

Use only when asked how you would scale beyond one process.

**INTERVIEW DESIGN — not a claim about actual PTR topology.**

```mermaid
flowchart TD
    E["Incoming Event"] --> R["Ownership Router"]

    R --> J1["JVM 1"]
    R --> J2["JVM 2"]
    R --> J3["JVM 3"]

    J1 --> P10["Partition 0<br/>Single Owner"]
    J1 --> P11["Partition 1<br/>Single Owner"]

    J2 --> P20["Partition 2<br/>Single Owner"]
    J2 --> P21["Partition 3<br/>Single Owner"]

    J3 --> P30["Partition 4<br/>Single Owner"]
    J3 --> P31["Partition 5<br/>Single Owner"]
```

Invariant:

```text
SAME OWNERSHIP KEY
→ SAME OWNER
→ ORDER PRESERVED

DIFFERENT OWNERSHIP KEYS
→ DIFFERENT OWNERS
→ PARALLELISM
```

The hard problems at this level are ownership transfer, failover, recovery, skew and preventing two owners from becoming authoritative simultaneously.

