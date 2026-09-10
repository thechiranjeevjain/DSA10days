# Risk Engine

This module has two deliberately different representations.

## `interview-versions/`

Self-contained files designed to be rewritten on a blank CoderPad.

Do not judge them by production packaging. Their job is **reconstruction under time pressure**.

```text
L0 = simplest correct single-owner model
L1 = ConcurrentHashMap + per-account lock
L2 = synchronous event integration
L3 = explicit partition organization
L4 = partition queue + one state owner
```

## `src/main/java/`

Production-style split classes with clear domain ownership.

The default implementation is `SingleOwnerRiskEngine`, because that is the easiest model for understanding the business invariant.

`ConcurrentRiskEngine` exists to demonstrate the important rule:

> `ConcurrentHashMap` protects map operations; it does not make a compound risk decision atomic.

## Rate-window semantics

This project implements a **fixed epoch-second bucket**, not an exact rolling one-second window.

```text
bucket = nowMs / 1000
```

That is intentionally easy to explain in an interview and permits boundary bursts. An exact rolling window would require timestamped usage such as a deque/ring buffer.
