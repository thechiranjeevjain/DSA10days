# PTR Interview Pack — Structured Index

This pack reorganizes the interview material into a small number of files with one job each.

## Source hierarchy

When two statements conflict, use this priority:

1. **SOURCE-GROUNDED ACTUAL PTR** — attached PTR/VP documents, resume, and concrete code/source evidence.
2. **USER-PROVIDED EXPERIENCE DETAIL** — details supplied directly in the interview notes.
3. **INTERVIEW DESIGN** — how you would design or evolve the system if asked.
4. **PUBLIC / INDUSTRY TALKING POINT** — useful analogy or number, but not a claim about actual PTR unless separately verified.

The safest interview rule is:

> **Actual system facts and hypothetical redesigns must never blur together.**

## Files

### `01_PTR_INTERVIEW_MASTER_SCRIPT.md`
The default spoken answers. Start here. It is intentionally concise and source-safe.

### `02_PTR_CONCURRENCY_AND_SCALE.md`
The full answer to the "single-threaded?" pushback, trade-offs, concurrency evolution, and what you can/cannot safely claim about actual PTR.

### `03_NEGATIVE_EXPOSURE_TECHNICAL_CHALLENGE.md`
Your strongest technical story: DEFAULT PTLG startup ordering, missing `RiskCheckContainer`, GTC replay, negative exposure, deterministic reproduction, and the readiness-boundary fix.

### `04_PTR_END_TO_END_AND_MERMAID.md`
High-level system flow, control plane vs hot data plane, class diagrams from the attached PDFs, transaction/rollback flow, and concurrency evolution diagrams.

### `05_JAVA_CODE_REVIEW_AND_ALIGNMENT.md`
Review of the five attached Java files and how they map to the diagrams. Includes the fixes required before memorizing them.

### `06_CODERPAD_30_40_MIN_PLAYBOOK.md`
How to avoid freezing: working MVP first, then correctness, then concurrency, then scale.

### `07_MINIMUM_TO_MEMORIZE.md`
The one-page-ish revision sheet. Read this before interviews.

### `08_PUBLIC_CLAIMS_VERIFY_BEFORE_USE.md`
Quarantines scale numbers and strong industry claims that appeared in the notes but are not established by the attached PTR source material.

---

# Recommended practice order

Do not study every file equally.

```text
1. 07_MINIMUM_TO_MEMORIZE
2. 01_PTR_INTERVIEW_MASTER_SCRIPT
3. 03_NEGATIVE_EXPOSURE_TECHNICAL_CHALLENGE
4. 04_PTR_END_TO_END_AND_MERMAID
5. 06_CODERPAD_30_40_MIN_PLAYBOOK
6. RiskLimitEngine.java — blank-screen rewrite
7. 02_PTR_CONCURRENCY_AND_SCALE
8. 05_JAVA_CODE_REVIEW_AND_ALIGNMENT
9. 08_PUBLIC_CLAIMS_VERIFY_BEFORE_USE
```

## The core interview loop

For any project question:

```text
BUSINESS CONTEXT
      ↓
STATE / INVARIANT
      ↓
TECHNICAL CONSTRAINT
      ↓
DECISION + WHY
      ↓
TRADE-OFF
      ↓
FAILURE MODE
      ↓
VALIDATION
      ↓
OUTCOME
```

For any coding question:

```text
REQUIREMENTS
    ↓
DATA + STATE
    ↓
INVARIANT
    ↓
WORKING MVP
    ↓
TEST
    ↓
CONCURRENCY
    ↓
SCALE / FAILURE
```

The goal is not maximum information. The goal is being able to reconstruct the important ideas under pressure.
