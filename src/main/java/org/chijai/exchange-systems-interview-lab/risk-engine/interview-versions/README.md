# Risk Engine — Interview Versions

Each file is intentionally self-contained.

Compile any one independently:

```bash
javac L0_RiskLimitEngineSingleOwner.java
java L0_RiskLimitEngineSingleOwner
```

Progression:

```text
L0  HashMap + single owner
L1  ConcurrentHashMap + per-account lock
L2  synchronous event bus
L3  explicit partition organization
L4  bounded queue + one owner per partition
```

The production-style implementation under `src/main/java` is cleaner. These files optimize for **blank-screen recall**.
