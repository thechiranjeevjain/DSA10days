# Matching Engine

This is a separate domain drill from pre-trade risk.

```text
risk engine:
"may the order proceed?"

matching engine:
"which resting order does it trade against?"
```

## Core structures

```text
bids -> highest price first
asks -> lowest price first
same price -> FIFO
```

## Core processing order

```text
incoming order
   ↓
match against opposite RESTING orders
   ↓
execute at resting price
   ↓
if incoming LIMIT still has quantity
   ↓
rest the remainder on its own side
```

The interview version is intentionally one file:

`interview-version/MatchingEngineInterview.java`
