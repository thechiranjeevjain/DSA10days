# PTR / Risk Engine — Mermaid Diagrams

This file reconstructs the attached design PDFs as Mermaid diagrams and keeps the diagrams aligned with the Java model.

---

## 1. High-Level End-to-End System Diagram

Use this first when the interviewer asks:

> "Walk me through the end-to-end system."

```mermaid
flowchart LR
    P["Participant<br/>Trader / Broker"]

    subgraph EX["Exchange — interview/system view"]
        ME["Matching Engine<br/>Order validation<br/>Primary checks"]
        PTR["Pre-Trade Risk<br/>RiskEngine<br/>RiskCheckGroup<br/>Secondary risk limits"]
        OB["Order Book<br/>Bids / Asks<br/>Price-Time Priority"]

        CFG["Config / Reference Data<br/>Accounts<br/>Limits<br/>Risk Groups"]
        OP["Operator API / Commands<br/>Kill Switch<br/>Block / Unblock"]
    end

    EXEC["Execution<br/>Buy + Sell Matched<br/>Price × Quantity"]
    REJ["Rejected<br/>Back to Participant"]

    P -->|"new order"| ME

    ME -->|"primary pass"| PTR
    ME -->|"primary fail"| REJ

    CFG -->|"limits at startup<br/>+ intraday updates"| PTR
    OP -->|"operator commands"| PTR

    PTR -->|"risk pass"| OB
    PTR -->|"risk fail"| REJ

    OB -->|"match found"| EXEC
    EXEC -->|"confirmation"| P
```

### 30-second narration

Participant sends an order. The matching-engine side performs its primary validation. If that passes, the order reaches pre-trade risk, where account/risk-group state is checked against limits such as position, notional-per-time and kill switch. A risk breach rejects the order immediately. If risk passes, the order proceeds to the order book, where price-time priority determines matching and execution. Configuration/reference-data updates feed the risk state, while operator commands can change controls such as a kill switch.

### Interview safety

Treat boxes such as a REST Operator API or a particular Config DB as an **interview/system design** unless your real PTR source material explicitly establishes those exact components.

---

## 2. 60–90 Second Class Diagram

Use this before coding. This is the smallest useful class-level picture.

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
    RiskEngine --> ExposureResults : returns
    RiskEngine --> RiskCheckGroup : owns/locates
    Order --> Account : belongs to
    RiskCheckGroup o-- RiskCheck : contains
```

### One-line explanation

**RiskEngine orchestrates, RiskCheckGroup protects the group-level invariant, and individual RiskCheck implementations own individual risk rules.**

---

## 3. Detailed Class Diagram

Use this only when the interviewer asks how the checks are extended or how rollback works.

```mermaid
classDiagram
    class RiskEngine {
        #ConcurrentHashMap~Integer, RiskCheckGroup~ groups
        +register(Account, RiskCheckGroup)
        +validate(Order, long) ExposureResults
        +onCancel(Order)
    }

    class Account {
        +int id
        +String name
    }

    class Order {
        +Account account
        +String ticker
        +Side side
        +long price
        +long quantity
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

    RiskEngine --> RiskCheckGroup : accountId -> group
    RiskEngine --> Order : validates
    RiskEngine --> ExposureResults : produces
    Order --> Account

    RiskCheckGroup o-- RiskCheck : checks

    RiskCheck <|.. AbstractRiskCheck
    AbstractRiskCheck <|-- MaxQtyCheck
    AbstractRiskCheck <|-- TotalTradedPerTimeCheck
    AbstractRiskCheck <|-- KillSwitchCheck
```

---

## 4. Risk Check Transaction / Rollback Flow

This diagram explains the `begin() -> check() -> rollback()` design.

```mermaid
flowchart TD
    START["Order arrives"] --> GROUP["Locate RiskCheckGroup"]
    GROUP --> SNAP["begin()<br/>snapshot mutable consumption"]
    SNAP --> C1["Check 1"]
    C1 -->|"BREACH"| RB["rollback() all checks"]
    C1 -->|"PASS"| C2["Check 2"]
    C2 -->|"BREACH"| RB
    C2 -->|"PASS"| C3["Check 3"]
    C3 -->|"BREACH"| RB
    C3 -->|"PASS"| OK["Keep mutations<br/>ACCEPT"]
    RB --> NO["REJECT"]
```

### Invariant

A risk decision behaves like one logical transaction:

```text
snapshot
→ evaluate checks
→ if every check passes, keep state
→ if any check breaches, restore prior state
```

An even simpler interview MVP is:

```text
calculate candidate values
→ validate all candidates
→ commit only after every validation passes
```

The second version is easier to write; the first version maps more closely to the attached class design.

---

## 5. Level 1 — Direct Concurrent Risk Engine

Maps to `RiskLimitEngine.java`.

```mermaid
flowchart LR
    C1["Caller Thread A"] --> MAP["ConcurrentHashMap<br/>accountId -> RiskCheckGroup"]
    C2["Caller Thread B"] --> MAP

    MAP --> G1["Account / Group A<br/>synchronized(group)"]
    MAP --> G2["Account / Group B<br/>synchronized(group)"]

    G1 --> CHECK1["Risk Checks"]
    G2 --> CHECK2["Risk Checks"]
```

### Concurrency statement

Different account groups may be processed concurrently. Mutations inside the same `RiskCheckGroup` are serialized.

---

## 6. Level 2 — Synchronous Message-Bus Integration

Maps to `RiskLimitEngineAsync.java`.

```mermaid
flowchart LR
    ME["Matching Engine"] -->|"OrderEvent"| BUS["In-Process MessageBus"]
    CFG["Config / Reference Data"] -->|"ConfigEvent"| BUS
    OP["Operator / Lifecycle"] -->|"CommandEvent"| BUS

    BUS --> LISTENER["RiskEngineListener"]
    LISTENER --> ENGINE["RiskEngine"]
    ENGINE --> RESULT["ExposureResults"]

    RESULT -->|"CompletableFuture reply"| ME
```

### Important

The attached implementation's bus dispatch is **synchronous**. `CompletableFuture` carries the response but does not itself create asynchronous execution.

---

## 7. Level 3a — Partitioned Direct Calls

Maps to `RiskLimitEnginePartitioned.java`.

```mermaid
flowchart TD
    O["Order"] --> HASH["accountId -> partition"]
    HASH --> P0["Partition 0"]
    HASH --> P1["Partition 1"]
    HASH --> P2["Partition 2"]
    HASH --> P3["Partition 3"]

    P0 --> G0["Account RiskCheckGroup(s)"]
    P1 --> G1["Account RiskCheckGroup(s)"]
    P2 --> G2["Account RiskCheckGroup(s)"]
    P3 --> G3["Account RiskCheckGroup(s)"]

    G0 -->|"synchronized(group)"| C0["Checks"]
    G1 -->|"synchronized(group)"| C1["Checks"]
    G2 -->|"synchronized(group)"| C2["Checks"]
    G3 -->|"synchronized(group)"| C3["Checks"]
```

### Important

This partitions **state lookup/ownership organization**, but the current implementation still executes on caller threads and still locks each `RiskCheckGroup`.

---

## 8. Level 3b — Partitioned Event-Loop Design

This is the intended model behind `RiskLimitEnginePartitionedAsync.java`.

```mermaid
flowchart TD
    O["Order / Command / Config Event"] --> ROUTE["Route by ownership key"]

    ROUTE --> Q0["Partition 0<br/>Bounded Queue"]
    ROUTE --> Q1["Partition 1<br/>Bounded Queue"]
    ROUTE --> Q2["Partition 2<br/>Bounded Queue"]
    ROUTE --> Q3["Partition 3<br/>Bounded Queue"]

    Q0 --> W0["Single Worker 0"]
    Q1 --> W1["Single Worker 1"]
    Q2 --> W2["Single Worker 2"]
    Q3 --> W3["Single Worker 3"]

    W0 --> S0["Partition-owned state"]
    W1 --> S1["Partition-owned state"]
    W2 --> S2["Partition-owned state"]
    W3 --> S3["Partition-owned state"]
```

### Desired invariant

**Every mutation of state owned by a partition must enter through that same partition queue.**

That includes, when applicable:

- orders
- cancels
- limit/config updates
- kill-switch changes

Only then is the statement "one worker owns mutable partition state" fully true.

---

## 9. Concurrency Evolution — Interview Whiteboard

```mermaid
flowchart LR
    V1["V1<br/>Single Thread<br/>Correct Invariant"]
    --> V2["V2<br/>ConcurrentHashMap<br/>+ per-group lock"]
    --> V3["V3<br/>Partition by ownership key"]
    --> V4["V4<br/>Single-owner worker<br/>per partition"]
    --> V5["V5<br/>Bounded queues<br/>Backpressure / Recovery"]
```

Say:

> "I would first make the business invariant correct. Then I would choose the concurrency model based on measured contention and the ownership boundary."

---

## 10. Matching Engine — Class / Data Structure Diagram

Maps to `MatchingEngine.java`.

```mermaid
classDiagram
    class MatchingEngine

    class OrderBook {
        +TreeMap bids
        +TreeMap asks
        +List trades
        +enterOrder(Order) String
        -match()
        -add(Order)
        -remove(TreeMap, Order)
    }

    class Order {
        +int id
        +String trader
        +Side side
        +int price
        +int qty
        +isMarket() boolean
    }

    class Trade {
        +int price
        +int qty
        +int buyId
        +int sellId
    }

    MatchingEngine --> OrderBook
    OrderBook o-- Order : bids / asks
    OrderBook o-- Trade : executions
```

### Price-time structure

```mermaid
flowchart LR
    BID["Bids<br/>TreeMap descending price"]
    --> BQ["Queue per price<br/>FIFO"]

    ASK["Asks<br/>TreeMap ascending price"]
    --> AQ["Queue per price<br/>FIFO"]
```

---

# What to Draw Live

Do not draw every diagram.

Use this sequence:

```text
1. End-to-end flow                     60–90 sec
2. Compact RiskEngine class diagram    60–90 sec
3. Start coding
4. Detailed inheritance/partitioning only if interviewer drills
```

The first two diagrams are the default interview pair.
