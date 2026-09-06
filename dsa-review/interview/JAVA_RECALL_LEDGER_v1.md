# JAVA RECALL LEDGER

> A compact retrieval system for Java syntax, APIs, semantics, and library usage
> that I repeatedly forget, confuse, or need disproportionate effort to recall.
>
> **Question this file answers:**  
> **“I know what I want to do. How do I express it correctly in Java?”**
>
> **Not a textbook. Not a chronological dump.**
>
> **Hard rule:** One concept → one canonical home. Every later mention must add a new purpose.

---

# 0. OPERATING SYSTEM

## 0.1 What belongs here?

Add something here when at least one is true:

- I repeatedly forget exact Java syntax.
- I confuse two similar Java APIs.
- I misunderstand Java behavior or semantics.
- I know the algorithm, but Java retrieval interrupts implementation.
- A small Java detail repeatedly causes avoidable mistakes.

Do **not** put algorithm-selection rules or problem-specific derivations here.

Route those to:

```text
DSA_RECALL_LEDGER.md
→ pattern / invariant / boundary / algorithm reasoning

Problem.java
→ full problem-specific derivation / proof / solution
```

---

## 0.2 Failure Tags

Use one tag when capturing:

```text
[SYN] exact Java syntax forgotten
[API] wrong / forgotten library method
[SEM] Java behavior misunderstood
```

---

## 0.3 CAPTURE INBOX

During study, capture cheaply and continue.

```text
- [ ] [SYN] Forgot ...
- [ ] [API] Confused X with Y ...
- [ ] [SEM] Thought Java behaved like ...
```

### Current Inbox

- [ ] _empty_

---

## 0.4 ACTIVE REVIEW INDEX

Only current weak spots belong here.

| ID | Retrieval prompt | Status |
|---|---|---|
| JAVA-CONF-01 | `length`, `length()`, or `size()`? | 🟡 |
| JAVA-DEQUE-01 | Stack vs queue verbs? | 🟡 |
| JAVA-PQ-01 | Min-heap vs max-heap syntax? | 🟡 |
| JAVA-MAP-01 | `containsKey`, `containsValue`, or `get`? | 🟡 |
| JAVA-STREAM-02 | Can I reproduce the six core Stream skeletons? | 🟡 |

```text
🔴 repeatedly failing / slow
🟡 shaky
🟢 reliable
```

When reliable, remove the row.
Do not duplicate the answer here.

---

# A. HIGH-FRICTION CONFUSION PAIRS

## JAVA-CONF-01 — Size / Length

```text
Array       → arr.length
String      → s.length()
Collection  → list.size()
```

---

## JAVA-CONF-02 — Element Access

```text
Array   → arr[i]
String  → s.charAt(i)
List    → list.get(i)
```

---

## JAVA-CONF-03 — Equality

```text
primitive char/int/etc. → ==
String content          → equals()
Character object value  → Objects.equals(a, b)
```

Do not rely on `Character == Character` for general value comparison.

---

## JAVA-CONF-04 — Stack vs Queue Verbs

```text
STACK → push / pop / peek
QUEUE → offer / poll / peek
```

---

## JAVA-CONF-05 — `map` vs `flatMap`

```text
map     → transform each element
flatMap → transform + flatten nested results
```

---

## JAVA-CONF-06 — `groupingBy` vs `toMap`

```text
groupingBy → many values may belong to one group/key
toMap      → construct key → value entries directly
```

---

# B. LANGUAGE SEMANTICS

## JAVA-CHAR-01 — `char` vs `Character` vs `String`

**TRIGGER**  
I tried calling methods on primitive `char` or mixed comparison rules.

**RECALL**

```text
char       → primitive
Character  → object wrapper
String     → object
```

```java
c == 'a';

s.equals("abc");

Objects.equals(a, b);
```

Primitive types do not have instance methods.

---

## JAVA-NULL-01 — HashMap Null Behavior

`HashMap` permits one null key and null values.

Therefore:

```java
node.random = cloneMap.get(curr.random);
```

works even when:

```java
curr.random == null
```

because:

```java
cloneMap.get(null)
```

is valid for `HashMap`.

`ConcurrentHashMap` permits neither null keys nor null values.

---

# C. STRING + ARRAY RETRIEVAL

## JAVA-STR-01 — String DSA Minimum API

```text
READ
length()
charAt(i)
toCharArray()

CUT
substring(l, r)     // r excluded

FIND
indexOf(x)
contains(x)

COMPARE
equals(t)           // content equality
compareTo(t)        // lexicographic ordering

BUILD
StringBuilder sb = new StringBuilder();
sb.append(x);
sb.reverse();
sb.toString();

CHARACTER CHECKS
Character.isLetter(c)
Character.isDigit(c)
Character.isLetterOrDigit(c)

CONVERT
Integer.parseInt(s)
String.valueOf(x)
```

---

## JAVA-ARR-01 — Common Array Retrieval

Maximum:

```java
Arrays.stream(nums)
        .max()
        .getAsInt();
```

Fixed-size list view:

```java
Arrays.asList(a, b, c);
```

Empty primitive array:

```java
new int[]{}
```

---

# D. MAP + SET

## JAVA-MAP-01 — Membership / Lookup

```java
map.get(key);
map.containsKey(key);
map.containsValue(value);
```

There is no generic:

```java
map.contains(...)
```

---

## JAVA-MAP-02 — Merge / Accumulate

```java
map.merge(key, amount, Long::sum);
```

Meaning:

```text
missing key → insert amount
existing key → old + amount
```

---

## JAVA-SET-01 — Seen-Set Duplicate Detection

```java
Set<T> seen = new HashSet<>();

if (!seen.add(value)) {
    // duplicate
}
```

```text
true  → newly inserted
false → already existed
```

---

# E. LIST / DEQUE / QUEUE / STACK

## JAVA-DEQUE-01 — ArrayDeque as Stack

```java
Deque<Integer> stack = new ArrayDeque<>();

stack.push(x);
stack.pop();
stack.peek();
```

```text
push → FIRST
pop  → FIRST
peek → FIRST
```

Prefer `ArrayDeque` over legacy `Stack` for normal interview code.

---

## JAVA-DEQUE-02 — ArrayDeque as Queue

```java
Queue<Integer> queue = new ArrayDeque<>();

queue.offer(x);
queue.poll();
queue.peek();
```

```text
offer → LAST
poll  → FIRST
peek  → FIRST
```

---

## JAVA-DEQUE-03 — Explicit Both-Ends API

```java
deque.peekFirst();
deque.pollFirst();

deque.peekLast();
deque.pollLast();

deque.addLast(x);
```

Use these when the algorithm genuinely needs both ends.

---

# F. PRIORITYQUEUE / TREEMAP / COMPARATOR

## JAVA-PQ-01 — PriorityQueue Direction

Java `PriorityQueue` is a **min-heap by default**.

Min-heap:

```java
PriorityQueue<Integer> minHeap =
        new PriorityQueue<>();
```

Max-heap:

```java
PriorityQueue<Integer> maxHeap =
        new PriorityQueue<>(Comparator.reverseOrder());
```

```text
offer → insert
peek  → inspect best
poll  → remove best
```

---

## JAVA-TMAP-01 — TreeMap Ordered Boundaries

```text
firstEntry()      → smallest key
lastEntry()       → largest key

pollFirstEntry()  → remove smallest
pollLastEntry()   → remove largest
```

---

## JAVA-COMP-01 — Comparator Chain

Ascending:

```java
Comparator.comparingLong(X::value)
```

Descending:

```java
Comparator.comparingLong(X::value)
        .reversed()
```

Tie-break:

```java
Comparator.comparingLong(X::value)
        .thenComparingLong(X::sequence)
```

Multiple levels:

```java
Comparator.comparingLong(X::primary)
        .reversed()
        .thenComparing(X::secondary)
        .thenComparingInt(X::id)
```

---

# G. STREAMS

## JAVA-STREAM-01 — Pipeline Mental Model

```text
SOURCE
stream()
Arrays.stream()
Stream.of()
IntStream.range()

    ↓

FILTER
filter()

    ↓

TRANSFORM
map()
mapToInt()
mapToLong()
flatMap()

    ↓

STRUCTURE / ORDER
distinct()
sorted()
limit()
skip()

    ↓

TERMINAL
findFirst()
anyMatch()
count()
min()
max()
sum()
reduce()
collect()
```

Compact categories:

```text
FILTER    → filter
TRANSFORM → map / flatMap
ORDER     → sorted / distinct / limit / skip
SEARCH    → findFirst / anyMatch / max / min
AGGREGATE → count / sum / reduce
COLLECT   → toList / toSet / toMap
GROUP     → groupingBy / partitioningBy
```

---

## JAVA-STREAM-02 — Six Core Skeletons

### 1. Filter + transform

```java
list.stream()
        .filter(x -> condition)
        .map(x -> transformation)
        .toList();
```

### 2. Sort

```java
list.stream()
        .sorted(Comparator.comparing(X::field))
        .toList();
```

### 3. Max / min

```java
list.stream()
        .max(Comparator.comparing(X::field));
```

### 4. To map

```java
list.stream()
        .collect(Collectors.toMap(
                X::key,
                Function.identity()
        ));
```

### 5. Group

```java
list.stream()
        .collect(Collectors.groupingBy(X::field));
```

### 6. Group + aggregate

```java
list.stream()
        .collect(Collectors.groupingBy(
                X::field,
                Collectors.counting()
        ));
```

---

## JAVA-STREAM-03 — Terminal Operations

```text
forEach   → run action
findFirst → first matching result as Optional
anyMatch  → does any match?
allMatch  → do all match?
noneMatch → do none match?
count     → how many?
max/min   → extreme element
reduce    → fold into one value
sum       → primitive stream total
collect   → build result container
```

---

# H. COLLECTORS

## JAVA-COLLECT-01 — Collector Decision Tree

```text
MAKE COLLECTION
├─ toList
├─ toSet
└─ toCollection

MAKE MAP
└─ toMap

GROUP
├─ groupingBy
└─ partitioningBy

AGGREGATE INSIDE GROUP
├─ counting
├─ summingInt
├─ averagingInt
├─ maxBy
└─ minBy

TRANSFORM INSIDE GROUP
├─ mapping
├─ filtering
└─ collectingAndThen

STRING
└─ joining
```

---

## JAVA-COLLECT-02 — Frequency Map

```java
Map<String, Long> frequency =
        values.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
```

---

## JAVA-COLLECT-03 — Group by Field

```java
employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment
        ));
```

---

## JAVA-COLLECT-04 — Highest Value Per Key

```java
employees.stream()
        .collect(Collectors.toMap(
                Employee::getDepartment,
                Function.identity(),
                (a, b) ->
                        a.getSalary() >= b.getSalary()
                                ? a
                                : b
        ));
```

---

## JAVA-COLLECT-05 — Partition Into Two Groups

```java
employees.stream()
        .collect(Collectors.partitioningBy(
                employee ->
                        employee.getSalary() >= 100_000
        ));
```

---

## JAVA-COLLECT-06 — Flatten Nested Lists

```java
nested.stream()
        .flatMap(List::stream)
        .toList();
```

---

## JAVA-COLLECT-07 — Duplicates by Detection Order

```java
Set<T> seen = new HashSet<>();

values.stream()
        .filter(value -> !seen.add(value))
        .distinct()
        .toList();
```

---

# I. HOW TO UPDATE THIS FILE

## During study

Add only a raw inbox line:

```text
- [ ] [API] Forgot TreeMap floorKey syntax.
```

Then continue studying.

## Later

```text
new inbox item
      ↓
search existing Java ledger
      ↓
same concept exists?
├─ YES → strengthen canonical entry
└─ NO  → create one new entry
```

For Java, prefer:

```text
CONFUSION
What did I mix up?

CORRECT
Exact syntax / semantic distinction.
```

Use explanation only when it prevents another likely misunderstanding.

---

# J. HARD RULES

1. Raw inbox may be chronological. Permanent knowledge may not.
2. Search before adding.
3. One concept → one canonical home.
4. Prefer contrasts for similar APIs.
5. Prefer exact syntax over prose when syntax is the failure.
6. Do not store algorithm theory here.
7. Do not paste full solutions.
8. Repeated mistakes strengthen one entry; they do not create duplicates.
9. Active Review contains only questions, never copied answers.
10. Delete low-value entries once they are obvious and have no transfer value.
11. Every code snippet must compile conceptually and be free of formatting artifacts.
12. The file is allowed to shrink.

---

# K. MASTER UPDATE PROMPT

```text
Update my JAVA RECALL LEDGER.

Process only the CAPTURE INBOX.

For every inbox item:

1. Classify it as [SYN], [API], or [SEM].
2. Search the existing ledger for its canonical concept.
3. If it already exists, merge the new learning into that entry.
4. If genuinely new, place it in the correct semantic section and create one stable ID.
5. Prefer concise contrast tables or exact syntax.
6. Add it to ACTIVE REVIEW only if it is a current recurring weakness.
7. Remove every processed inbox line.
8. Remove redundancy introduced by the merge.
9. Do not change unrelated sections.
10. Do not move DSA reasoning into this file.

Preserve:
One concept → one canonical home.
Every later mention must add a new purpose.

Return the complete updated file.
```

---

# FINAL RECALL

```text
JAVA_RECALL_LEDGER.md
= HOW do I write it?
```

> **Capture cheaply. Retrieve exactly.**
