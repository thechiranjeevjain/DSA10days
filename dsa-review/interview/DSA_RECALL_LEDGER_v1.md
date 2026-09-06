# DSA RECALL LEDGER

> A compact reconstruction system for DSA patterns, invariants, boundaries,
> and algorithmic decisions that I repeatedly forget, confuse, or fail to invent.
>
> **Question this file answers:**  
> **“What mechanism, invariant, or reasoning rule do I need?”**
>
> **Not a textbook. Not a chronological dump.**
>
> **Hard rule:** One concept → one canonical home. Every later mention must add a new purpose.

---

# 0. OPERATING SYSTEM

## 0.1 What belongs here?

Add something here when at least one is true:

- I failed to recognize the right algorithm / data structure.
- I forgot an invariant required for correctness.
- I confused boundary / index logic.
- I memorized a solution but could not reconstruct it.
- The learning transfers across multiple problems.

Do **not** put Java syntax/API retrieval here.

Route that to:

```text
JAVA_RECALL_LEDGER.md
→ Java syntax / APIs / semantics

Problem.java
→ full problem-specific derivation / proof / implementation
```

---

## 0.2 Failure Tags

```text
[PAT] pattern / data-structure choice not recognized
[INV] correctness invariant forgotten
[IDX] boundary / index reasoning confused
```

---

## 0.3 CAPTURE INBOX

During study:

```text
- [ ] [PAT] Did not recognize ...
- [ ] [INV] Forgot why ...
- [ ] [IDX] Confused left/right boundary ...
```

Capture first. Organize later.

### Current Inbox

- [ ] _empty_

---

## 0.4 ACTIVE REVIEW INDEX

| ID | Retrieval prompt | Status |
|---|---|---|
| DSA-BSANS-01 | Can I derive answer-space bounds? | 🔴 |
| DSA-BT-01 | Order matters? Reuse? Duplicates? | 🔴 |
| DSA-DS-01 | Dominated forever or still relevant? | 🟡 |
| DSA-SWM-01 | Which deque end expires, dominates, inserts, answers? | 🟡 |
| DSA-IDX-01 | Why can `-1` be a valid conceptual boundary? | 🟡 |

```text
🔴 repeatedly failing / slow
🟡 shaky
🟢 reliable
```

Remove the row when reliable.
Keep the canonical entry below.

---

# A. PATTERN SELECTION

## DSA-BS-01 — Binary Search Requirement

Binary search does **not** fundamentally require a sorted input array.

It requires:

> a monotonic search space / decision boundary where information at `mid`
> lets us safely discard one half.

A sorted array is one common instance.

---

## DSA-BSANS-01 — Binary Search on Answer

**TRIGGER**  
I forget answer-space bounds or memorize them problem by problem.

**RECALL**

```text
candidate answer
    ↓
smallest meaningful candidate
largest useful candidate
    ↓
monotonic feasible(candidate)
    ↓
binary search the boundary
```

> **Lower bound = smallest meaningful candidate.**  
> **Upper bound = largest useful candidate.**

| Problem | Candidate | Lower | Why? | Upper | Why? |
|---|---|---:|---|---:|---|
| Koko | speed | `1` | minimum legal speed | `maxPile` | faster cannot reduce a pile below one hour |
| Ship | capacity | `maxWeight` | every package must fit | `sumWeight` | all packages can fit in one day |
| Split Array | max sum | `maxElement` | every element belongs somewhere | `totalSum` | whole array can be one piece |
| Bouquet | day | `minBloom` | before it nothing blooms | `maxBloom` | by then all flowers bloom |

Feasibility initialization:

```text
Koko    → hours = 0
Ship    → days = 1
Split   → pieces = 1
Bouquet → bouquets = 0
```

---

## DSA-BT-01 — Backtracking Family

**TRIGGER**  
I confuse `start`, `i + 1`, `i`, `used[]`, and duplicate skipping.

**RECALL**

```text
Does order matter?
│
├─ NO → subset / combination
│       move forward using start
│
│       recurse(i + 1) → no index reuse
│       recurse(i)     → reuse allowed
│
└─ YES → permutation
        loop 0..n-1 at every depth
        used[] prevents reusing same INDEX
```

Duplicate values are a separate concern:

> Skip equivalent choices at the **same recursion depth**.

---

## DSA-DS-01 — Monotonic Structure vs Heap

**RECALL**

> **DOMINATED forever → Monotonic.**  
> **STILL relevant, need current best → Heap.**

Monotonic:

```text
candidate can be proved permanently useless
→ remove forever
```

Heap:

```text
candidate may still matter
→ retain it
→ repeatedly retrieve current min/max
```

Examples:

```text
Daily Temperatures
resolved / dominated candidates disappear
→ stack

Sliding Window Maximum
smaller older candidate behind larger newer candidate
can never become maximum
→ deque

Meeting Rooms
every active meeting still occupies a room
need earliest ending active meeting
→ min-heap
```

---

# B. INVARIANTS + MECHANICS

## DSA-SWM-01 — Sliding Window Maximum Deque Ends

**TRIGGER**  
I confuse which end performs which job.

**RECALL**

```text
FIRST → expired?
LAST  → dominated?
LAST  → add new
FIRST → answer
```

The deque stores only candidates that are still both:

```text
inside the window
AND
not dominated by a better newer candidate
```

---

## DSA-BOUQUET-01 — Local vs Global Constraint

```text
k = LOCAL
adjacent bloomed flowers needed for ONE bouquet

m = GLOBAL
total bouquets needed
```

Unbloomed flower:

```java
consecutiveFlowers = 0;
```

because adjacency cannot cross the gap.

---

# C. BOUNDARY / INDEX REASONING

## DSA-IDX-01 — Sentinel Boundaries

**TRIGGER**  
I see `-1` and think calculations should fail because it is not a real array index.

**RECALL**

A sentinel such as `-1` can represent a **conceptual boundary outside the array**.

It is not necessarily used for array access.

Example shape:

```text
left boundary
right boundary

width = right - left - 1
```

If nothing smaller exists on the left:

```text
left = -1
```

then:

```text
width = right - (-1) - 1
      = right
```

which correctly means the span reaches index `0`.

Keep the full Largest Rectangle derivation in its canonical problem `.java` file.

---

# D. DOMAIN / MODELING DECISIONS

## DSA-BOOK-01 — Order Book Structure Choice

```text
PriorityQueue<Order>
→ best individual resting order / batch

TreeMap<Price, Quantity>
→ best aggregated price level

TreeMap<Price, Deque<Order>>
→ price priority + FIFO time priority
```

Direction:

```text
BUY  → highest price best
SELL → lowest price best
```

Crossing:

```text
incoming BUY  matches when bestAsk <= buyPrice
incoming SELL matches when bestBid >= sellPrice
```

TreeMap aggregation can use less state when many orders share one price.

The Java syntax for `PriorityQueue`, `TreeMap`, and comparators belongs in
`JAVA_RECALL_LEDGER.md`.

---

# E. HOW TO UPDATE THIS FILE

## During solving

Capture the exact reasoning failure:

```text
- [ ] [PAT] Again chose heap when dominated values could be deleted forever.
```

or:

```text
- [ ] [IDX] Forgot why previous-smaller boundary is stack.peek() after pop.
```

Then keep solving.

## Later

```text
new inbox item
      ↓
search DSA ledger
      ↓
same concept exists?
├─ YES → strengthen canonical entry
└─ NO  → create one new entry
```

For DSA, prefer:

```text
TRIGGER
What failed?

RECALL
Shortest transferable rule.

WHY / INVARIANT
What lets me reconstruct it?

EXAMPLE
Only if needed to discriminate the rule.
```

Do not preserve a full problem solution here.

---

# F. HARD RULES

1. Raw inbox may be chronological. Permanent knowledge may not.
2. Search before adding.
3. One concept → one canonical home.
4. Prefer reconstruction over memorization.
5. Prefer general rules over lists of problem-specific facts.
6. Examples justify the rule; they do not replace it.
7. Java syntax belongs in the Java ledger.
8. Full problem derivations belong in `Problem.java`.
9. Repeated mistakes strengthen one entry; they do not create duplicates.
10. Active Review contains only questions, never copied explanations.
11. Remove low-value entries once they become obvious and have no transfer value.
12. Every line must earn its place.
13. The file is allowed to shrink.

---

# G. MASTER UPDATE PROMPT

```text
Update my DSA RECALL LEDGER.

Process only the CAPTURE INBOX.

For every inbox item:

1. Classify it as [PAT], [INV], or [IDX].
2. Search the existing ledger for its canonical concept.
3. If it already exists, strengthen that entry instead of adding a duplicate.
4. If genuinely new, place it in the correct semantic section and create one stable ID.
5. Prefer a transferable invariant or reconstruction rule over problem-specific memorization.
6. Keep Java syntax/API details in JAVA_RECALL_LEDGER.md.
7. Keep full problem-specific derivations in the canonical Problem.java file.
8. Add it to ACTIVE REVIEW only if it is a current recurring weakness.
9. Remove every processed inbox line.
10. Remove redundancy introduced by the merge.
11. Do not change unrelated sections.

Preserve:
One concept → one canonical home.
Every later mention must add a new purpose.

Return the complete updated file.
```

---

# FINAL RECALL

```text
DSA_RECALL_LEDGER.md
= WHAT mechanism / invariant do I need?
```

> **Capture cheaply. Reconstruct reliably.**
