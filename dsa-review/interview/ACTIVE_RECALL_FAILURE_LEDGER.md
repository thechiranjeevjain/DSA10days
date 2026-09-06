# Active Recall Failure Ledger

> One source of truth for concepts I repeatedly forget, implement incorrectly, confuse, or can explain only with unusual effort.
>
> Desk rule: **Try before looking. Recall before rereading. Fix gaps, not everything. AI after attempt. If I cannot reconstruct it cold, I do not own it yet.**

This is not another syllabus and not a notebook for complete tutorials. It is a small, living repair queue. A topic enters only when there is evidence of a useful gap. Every entry must end in a retrieval prompt and observable proof.

## How to use this file

### During study or problem solving: capture in 60 seconds

Add one line to the inbox. Do not stop the current problem to write an essay.

Format:

```text
- [ ] YYYY-MM-DD | area | what I forgot/did wrong | where it happened
```

Example:

```text
- [ ] 2026-09-06 | Java/HashMap | was unsure whether get(null) is legal | Copy Random List
```

### At the end of the study block: process in 5 minutes

1. If it was a one-off typo, fix it in the source and do not create a card.
2. If it is recurring, correctness-critical, or interview-visible, assign the next `GAP-nnn` ID.
3. Write one cold prompt that exposes the gap without leaking the answer.
4. Store only the smallest correction, invariant, or code skeleton needed.
5. Set its priority and next retrieval.
6. After two clean reconstructions on different days, mark it `STABLE`. Keep the evidence; do not delete history.

### During review: never read top to bottom

1. Open the Active Queue.
2. Pick the highest-priority due card.
3. Read only its **Cold prompt**.
4. Close or look away from this file; speak, draw, or code from a blank page.
5. Test at least one edge case and one meaningful variation.
6. Open the answer, compare, and record the result.

## Quick Capture Inbox

Add new lines immediately below this marker. Raw wording is acceptable here.

<!-- ADD NEW QUICK-CAPTURE LINES BELOW THIS COMMENT -->

- [ ] YYYY-MM-DD | area | what I forgot/did wrong | problem, mock, or discussion

<!-- ADD NEW QUICK-CAPTURE LINES ABOVE THIS COMMENT -->

## Classification

### Priority

| Priority | Meaning | Action |
|---|---|---|
| `A` | Can cause a wrong answer, broken code, or a visible senior-level red flag | Retrieve before the next interview practice block |
| `B` | Usually correct, but slow, hazy, or dependent on hints | Retrieve in the next 1-3 days |
| `C` | Useful refinement, uncommon API, or optional optimization | Retrieve after the interview-critical queue is clear |

### Failure type

| Code | Gap |
|---|---|
| `TRIGGER` | Did not recognize when the technique applies |
| `INVARIANT` | Recognized the technique but could not justify correctness |
| `TEMPLATE` | Understood it but could not reconstruct clean code |
| `JAVA` | Java syntax, API, type, equality, comparator, or library mistake |
| `EDGE` | Missed null, empty, overflow, duplicates, impossible case, or boundary |
| `TRADEOFF` | Chose a plausible structure but could not defend what it loses |
| `COMMUNICATION` | Could solve, but could not explain the decision clearly |

### Result and spacing

| Result | Evidence | Next retrieval |
|---|---|---|
| `AGAIN` | Looked before answering, wrong invariant, or code did not work | Retry after 15-30 minutes, then tomorrow |
| `HARD` | Correct only with hesitation, a hint, or a repair | Tomorrow, then +3 and +7 days |
| `GOOD` | Correct cold, clean code/explanation, edge cases tested | +3, +7, and +14 days |
| `EASY` | Correct cold and adapted to a changed constraint | +7 and +21 days; then mark `STABLE` |

Use actual dates in the queue after a review. The `1/3/7` rhythm is a fallback; if Review OS gives a due date, its adaptive date wins.

## Active Queue

The seed cards below were created from the supplied notes because they were explicitly described as forgotten, mistaken, or effortful. Do not try to review all of them in one sitting.

| Order | ID | Priority | Area | Recurring risk | Next | Clean spaced reps | Status |
|---:|---|:---:|---|---|---|:---:|---|
| 1 | [GAP-004](#gap-004-java-api-equality-and-type-traps) | A | Java correctness | Small API/type errors can break otherwise-correct CoderPad code | NOW | 0 | ACTIVE |
| 2 | [GAP-002](#gap-002-binary-search-and-binary-search-on-the-answer) | A | Binary search | Bounds, feasibility direction, and return contract get mixed | NOW | 0 | ACTIVE |
| 3 | [GAP-003](#gap-003-backtracking-choice-spaces) | A | Backtracking | Subset and permutation recursion are being conflated | NOW | 0 | ACTIVE |
| 4 | [GAP-005](#gap-005-monotonic-stack-deque-or-heap) | A | Data-structure choice | Similar-looking problems trigger the wrong tool | NOW | 0 | ACTIVE |
| 5 | [GAP-006](#gap-006-arraydeque-as-stack-queue-and-deque) | A | Java collections | End operations and empty behavior are not automatic yet | D+1 | 0 | ACTIVE |
| 6 | [GAP-008](#gap-008-treemap-versus-priorityqueue-for-orders) | A | Trading/DSA | Price aggregation is confused with true price-time priority | D+1 | 0 | ACTIVE |
| 7 | [GAP-009](#gap-009-price-time-priority-and-matching-contract) | A | Trading/LLD | Comparator or crossing condition can be reversed | D+1 | 0 | ACTIVE |
| 8 | [GAP-001](#gap-001-random-pointer-clone-and-null-map-lookup) | B | Linked list/Java | Null-map behavior is remembered without its precondition | D+1 | 0 | ACTIVE |
| 9 | [GAP-010](#gap-010-stream-pipeline-and-collector-selection) | B | Java Streams | Collector selection and return shapes require lookup | D+1 | 0 | ACTIVE |
| 10 | [GAP-011](#gap-011-stream-interview-drills) | B | Java Streams | Familiar examples are not yet cold-reconstructable | D+3 | 0 | ACTIVE |
| 11 | [GAP-007](#gap-007-string-and-character-toolkit) | B | Strings/Java | Basic operations and char assumptions are effortful | D+3 | 0 | ACTIVE |
| 12 | [GAP-012](#gap-012-small-java-return-and-overflow-patterns) | B | Java templates | Tiny completion/overflow patterns are easy to omit | D+3 | 0 | ACTIVE |

## Immediate Goldman use

Until the CoderPad round, use this as a repair queue, not as a reason to expand the syllabus.

| Session | Maximum time | Cards | Passing evidence |
|---|---:|---|---|
| First pass | 45 minutes | GAP-004, GAP-002, GAP-003, GAP-005 | Speak the discriminator, reconstruct one skeleton, test one edge case |
| Second pass | 35 minutes | GAP-006, GAP-008, GAP-009 | Use the correct deque/comparator/map without notes and defend the trade-off |
| Third pass | 30 minutes | Only cards rated `AGAIN` or `HARD` | Repair the exact missing piece; do not reread every card |
| Interview-day activation | 15-20 minutes | At most three stubborn cards | Verbal recall only; no new topics and no marathon coding |

## Durable Failure Cards

### GAP-001 Random-pointer clone and null map lookup

**Priority:** B
**Types:** `JAVA`, `EDGE`, `INVARIANT`
**Anchor:** [LeetCode 138 - Copy List with Random Pointer](https://leetcode.com/problems/copy-list-with-random-pointer/) | [Local Java](../../src/main/java/org/chijai/day4/LinkedList/session2/CopyListWithRandomPointer.java)

**Cold prompt**

1. Why can `cloneMap.get(curr.random)` correctly assign a `null` random pointer?
2. What precondition makes the same line correct for every non-null random pointer?
3. When is `get(key) == null` ambiguous?
4. Would the same null behavior work with `ConcurrentHashMap`?

<details>
<summary>Open correction only after answering</summary>

`HashMap` permits a null key and null values. If `curr.random == null`, then `cloneMap.get(null)` returns null when there is no mapping for the null key, so the cloned pointer remains null.

The deeper correctness requirement is a completed first pass: every non-null original node must already map to its clone before the second pass wires `next` and `random`.

```java
clone.next = cloneMap.get(curr.next);
clone.random = cloneMap.get(curr.random);
```

`HashMap.get` also returns null for a missing non-null key, so use `containsKey` if the program must distinguish "absent" from "present and mapped to null." `ConcurrentHashMap` does not permit null keys or null values.

Minimum tests: `random == null`, self-random, two nodes pointing to each other, and multiple nodes pointing to the same target.

</details>

**Transfer question:** How would the O(1)-extra-space interleaving solution avoid the map, and which restoration invariant must hold?

**Evidence log:**

| Date | Result | What was still missing | Next |
|---|---|---|---|
| | | | |

### GAP-002 Binary search and binary search on the answer

**Priority:** A
**Types:** `TRIGGER`, `INVARIANT`, `TEMPLATE`, `EDGE`
**Anchors:** [Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/) | [Local Koko Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | [Capacity to Ship Packages](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/) | [Split Array Largest Sum](https://leetcode.com/problems/split-array-largest-sum/) | [Minimum Days to Make Bouquets](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/)

**Cold prompt**

1. State the search space, monotone predicate, and return contract before coding.
2. Derive the low and high bounds for Koko, Ship, Split Array, and Bouquet.
3. Explain why a feasibility function is monotone.
4. Where must `long` be used?

<details>
<summary>Open correction only after answering</summary>

Binary search is valid because each comparison or feasibility result eliminates a contiguous half of an ordered candidate space. For answer search, first decide whether the contract is **smallest feasible** or **largest feasible**.

Smallest-feasible template, where feasibility changes `false -> true`:

```java
long lo = lowerBound;
long hi = upperBound;

while (lo < hi) {
    long mid = lo + (hi - lo) / 2;
    if (feasible(mid)) {
        hi = mid;
    } else {
        lo = mid + 1;
    }
}
return lo;
```

| Problem | Candidate answer | Lower bound | Upper bound | Feasibility work | Contract |
|---|---|---:|---:|---|---|
| Koko | eating speed | `1` | `max(piles)` | `sum(ceil(pile / speed)) <= h` | smallest feasible |
| Ship | ship capacity | `max(weights)` | `sum(weights)` | greedy days required `<= days` | smallest feasible |
| Split Array | maximum subarray sum | `max(nums)` | `sum(nums)` | greedy pieces required `<= k` | smallest feasible |
| Bouquet | day | `min(bloomDay)` | `max(bloomDay)` | adjacent bouquets formed `>= m` | smallest feasible |

Use `long` for sums, products such as `m * k`, counters that may accumulate large values, and safe midpoint arithmetic. For Bouquet, fail immediately if `(long) m * k > bloomDay.length`.

Communication contract:

> "I am not searching an array index. I am searching the smallest candidate answer for which the feasibility predicate becomes true. I will prove the bounds, show monotonicity, then return the first feasible value."

</details>

**Transfer question:** If the task changes from the minimum valid capacity to the maximum valid minimum distance, which half is retained after a feasible midpoint?

**Evidence log:**

| Date | Result | Problem reconstructed cold | Missing piece | Next |
|---|---|---|---|---|
| | | | | |

### GAP-003 Backtracking choice spaces

**Priority:** A
**Types:** `TRIGGER`, `INVARIANT`, `TEMPLATE`, `EDGE`
**Anchors:** [Subsets](https://leetcode.com/problems/subsets/) | [Local Subsets Java](../../src/main/java/org/chijai/day11/backtracking/session1/Subsets.java) | [Permutations](https://leetcode.com/problems/permutations/) | [Local Permutations Java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) | [Letter Combinations](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) | [Local Java](../../src/main/java/org/chijai/day11/backtracking/session1/LetterCombinationsOfAPhoneNumber.java)

**Cold prompt**

1. What does one recursion level mean for subsets/combinations versus permutations?
2. When do I pass `start`, and when do I need `used[]` or swapping?
3. Why must the result store a copy of the path?
4. State the duplicate-skip rule for sorted permutation input.

<details>
<summary>Open correction only after answering</summary>

The reusable loop is:

`choose -> recurse -> unchoose`

The important difference is the choice space.

| Family | Meaning of one depth | Next choices | State |
|---|---|---|---|
| Subset/combination | choose the next item after a boundary | indices `start..n-1` | pass `i + 1` |
| Permutation | fill the next output position | any not-yet-used item | `used[]` or in-place swap |
| Fixed alphabet mapping | expand the current input symbol | choices mapped from this position | input index |

Combination-style skeleton:

```java
void dfs(int start, int[] nums, List<Integer> path, List<List<Integer>> out) {
    out.add(new ArrayList<>(path));
    for (int i = start; i < nums.length; i++) {
        path.add(nums[i]);
        dfs(i + 1, nums, path, out);
        path.remove(path.size() - 1);
    }
}
```

Permutation-style skeleton:

```java
void dfs(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> out) {
    if (path.size() == nums.length) {
        out.add(new ArrayList<>(path));
        return;
    }
    for (int i = 0; i < nums.length; i++) {
        if (used[i]) continue;
        used[i] = true;
        path.add(nums[i]);
        dfs(nums, used, path, out);
        path.remove(path.size() - 1);
        used[i] = false;
    }
}
```

With sorted duplicate values, skip the same value at the same tree level. For unique permutations:

```java
if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) continue;
```

`new ArrayList<>(path)` is required because `path` is mutated during later backtracking.

</details>

**Transfer question:** Change Subsets to Combination Sum where an item can be reused. Which index is passed recursively, and why?

**Evidence log:**

| Date | Result | Family discriminator spoken first? | Next |
|---|---|---|---|
| | | | |

### GAP-004 Java API, equality, and type traps

**Priority:** A
**Types:** `JAVA`, `EDGE`

**Cold prompt**

Correct each statement without opening the answer:

1. "A `Map` has a `contains(x)` method."
2. "Use `==` for two `Character` objects."
3. "`Arrays.stream(nums).max()` returns an `int`."
4. "`Stream.toList()` and `Collectors.toList()` guarantee the same mutability."
5. "If `map.get(key)` is null, the key is definitely absent."

<details>
<summary>Open correction only after answering</summary>

| Need | Correct Java | Trap |
|---|---|---|
| key exists | `map.containsKey(key)` | no general `Map.contains(...)` |
| value exists | `map.containsValue(value)` | usually O(n) |
| primitive chars equal | `a == b` | both are `char` values |
| boxed characters equal by value | `Objects.equals(a, b)` or `a.equals(b)` after null check | `Character == Character` may compare object identity |
| strings equal by content | `Objects.equals(a, b)` or `a.equals(b)` | `==` compares references |
| maximum of `int[]` | `Arrays.stream(nums).max().orElseThrow()` | returns `OptionalInt` before unwrapping |
| empty primitive array | `new int[0]` | do not return null unless the contract requires it |
| collect a stream | `.collect(Collectors.toList())` or `.toList()` | do not assume the two APIs have the same mutability contract |
| distinguish absent from mapped-to-null | `map.containsKey(key)` | `get` alone is ambiguous for maps permitting null values |

Other high-frequency safe forms:

```java
int max = Arrays.stream(nums).max().orElseThrow();
long maxLong = values.stream().mapToLong(Long::longValue).max().orElseThrow();
Map.Entry<Long, Long> best = map.firstEntry();
```

For interviews, prefer explicit empty-input handling when the problem contract is unclear rather than hiding it inside an arbitrary `orElse(0)`.

</details>

**Transfer question:** What happens when one operand is primitive `char` and the other is a non-null `Character`? What extra failure becomes possible?

**Evidence log:**

| Date | Result | Exact trap missed | Next |
|---|---|---|---|
| | | | |

### GAP-005 Monotonic stack, deque, or heap

**Priority:** A
**Types:** `TRIGGER`, `INVARIANT`, `TRADEOFF`
**Anchors:** [Next Greater Element I](https://leetcode.com/problems/next-greater-element-i/) | [Local Java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/NextGreaterElement.java) | [Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) | [Local Java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java) | [Largest Rectangle](https://leetcode.com/problems/largest-rectangle-in-histogram/) | [Local Java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java)

**Cold prompt**

Choose the structure and state its invariant:

1. nearest next greater element;
2. maximum in every contiguous window of size `k`;
3. globally largest `k` elements;
4. repeatedly process the best current candidate while arbitrary insertions arrive.

<details>
<summary>Open correction only after answering</summary>

| Signal in the problem | Structure | Core invariant |
|---|---|---|
| nearest greater/smaller, boundary, span | monotonic stack | stack preserves monotone candidates; each element is pushed/popped at most once |
| max/min over a sliding contiguous window | monotonic deque | indices are both in-window and monotone by value |
| global top-k, kth item, repeated best candidate | heap | root is the current best/worst retained candidate |
| best price level plus ordered key navigation | `TreeMap` | keys stay sorted and price-level lookup/removal is logarithmic |

Mnemonic: **nearest boundary -> monotonic stack; ordered window extrema -> monotonic deque; global priority -> heap.**

A heap can solve sliding-window maximum with lazy stale removal, but it is typically O(n log n); the monotonic deque is O(n). The deque works because dominated smaller values behind a new larger value can never become the maximum before that larger value expires.

</details>

**Transfer question:** Why is a monotonic deque generally not the right answer for "top k frequent elements"?

**Evidence log:**

| Date | Result | Wrong trigger, invariant, or complexity? | Next |
|---|---|---|---|
| | | | |

### GAP-006 ArrayDeque as stack, queue, and deque

**Priority:** A
**Types:** `JAVA`, `TEMPLATE`, `EDGE`

**Cold prompt**

From memory, write the safe operations for stack, queue, and both deque ends. Then state the null and empty-container behavior that matters in an interview.

<details>
<summary>Open correction only after answering</summary>

```java
Deque<Integer> dq = new ArrayDeque<>();

// Stack: LIFO at the front
dq.push(x);       // addFirst
int a = dq.pop(); // removeFirst; throws if empty
int b = dq.peek();// peekFirst; null if empty

// Queue: FIFO from back to front
dq.offer(x);      // offerLast
int c = dq.poll();// pollFirst; null if empty
int d = dq.peek();// peekFirst; null if empty

// Explicit ends
dq.offerFirst(x);
dq.offerLast(x);
dq.pollFirst();
dq.pollLast();
dq.peekFirst();
dq.peekLast();
```

`ArrayDeque` rejects null elements. That makes null a useful empty result for `poll`/`peek`. Prefer `poll`/`peek` when empty is an expected state; `remove`/`pop`/`element` throw on empty. It has no indexed random access.

</details>

**Transfer question:** In a monotonic deque, why store indices rather than only values?

**Evidence log:**

| Date | Result | End or empty behavior missed | Next |
|---|---|---|---|
| | | | |

### GAP-007 String and character toolkit

**Priority:** B
**Types:** `JAVA`, `TEMPLATE`, `EDGE`

**Cold prompt**

Without looking, write the Java calls for: length, char access, substring, search, prefix/suffix, content equality, lexicographic comparison, char-array conversion, efficient construction, and frequency counting. State when `int[26]` is unsafe.

<details>
<summary>Open correction only after answering</summary>

```java
int n = s.length();
char ch = s.charAt(i);
String part = s.substring(from, to); // to is exclusive
int pos = s.indexOf(target);
boolean same = s.equals(other);
boolean prefix = s.startsWith("pre");
boolean suffix = s.endsWith("ing");
int order = s.compareTo(other);
char[] chars = s.toCharArray();

StringBuilder sb = new StringBuilder();
sb.append(ch).append(word);
sb.deleteCharAt(sb.length() - 1);
String result = sb.toString();
```

Frequency options:

```java
int[] lowerEnglish = new int[26];
lowerEnglish[ch - 'a']++;

Map<Character, Integer> freq = new HashMap<>();
freq.merge(ch, 1, Integer::sum);
```

Use `int[26]` only when the constraint guarantees lowercase English letters. A 128/256-sized array assumes an ASCII-like bounded character set; it is not a general Unicode solution. Java `char` is a UTF-16 code unit, not necessarily a complete Unicode code point.

</details>

**Transfer question:** What changes if the interviewer says the input can contain emoji or arbitrary Unicode code points?

**Evidence log:**

| Date | Result | API or assumption missed | Next |
|---|---|---|---|
| | | | |

### GAP-008 TreeMap versus PriorityQueue for orders

**Priority:** A
**Types:** `TRIGGER`, `TRADEOFF`, `INVARIANT`
**Anchor:** [LeetCode 1801 - Number of Orders in the Backlog](https://leetcode.com/problems/number-of-orders-in-the-backlog/) | [Local Java](../../src/main/java/org/chijai/trading/NumberOfOrdersInTheBacklog.java)

**Cold prompt**

1. Why can an aggregated `TreeMap<price, quantity>` solve LeetCode 1801?
2. Why is the same representation insufficient for a real order book with price-time priority and cancellation by order ID?
3. When is a `PriorityQueue` awkward despite giving the best price quickly?

<details>
<summary>Open correction only after answering</summary>

For a problem that only needs total quantity at each price and repeatedly consumes the best crossing price, price-level aggregation is enough:

```java
TreeMap<Integer, Long> buys = new TreeMap<>(Comparator.reverseOrder());
TreeMap<Integer, Long> sells = new TreeMap<>();

Integer bestBuy = buys.firstKey();
Integer bestSell = sells.firstKey();
```

Update with `merge`, subtract matched quantity, and remove a level when its quantity reaches zero. Sum remaining quantities in `long` and apply the modulus at the end.

But aggregation destroys order identity and FIFO order within a price. A price-time book needs something closer to:

```text
sorted price levels
  -> FIFO queue of orders at each price
  -> orderId index for cancellation/amendment
```

A `PriorityQueue` gives fast best-price access, but arbitrary cancellation/update is awkward because removal is not generally O(log n) by identity. Lazy deletion can help, but adds stale-entry bookkeeping. It also does not by itself model FIFO queues at a price level.

</details>

**Transfer question:** Design the minimum structures needed for O(log P) price-level access and near-O(1) removal when given an order ID.

**Evidence log:**

| Date | Result | Trade-off defended aloud? | Next |
|---|---|---|---|
| | | | |

### GAP-009 Price-time priority and matching contract

**Priority:** A
**Types:** `JAVA`, `INVARIANT`, `EDGE`, `COMMUNICATION`

**Cold prompt**

1. Write buy-side and sell-side price-time comparators.
2. State the crossing condition.
3. State exactly what changes after a partial fill and a complete fill.
4. Name two production concerns intentionally omitted from an interview-sized matcher.

<details>
<summary>Open correction only after answering</summary>

For an order with integer `price` and monotonic `sequence`:

```java
Comparator<Order> buyPriority = Comparator
        .comparingInt(Order::price).reversed()
        .thenComparingLong(Order::sequence);

Comparator<Order> sellPriority = Comparator
        .comparingInt(Order::price)
        .thenComparingLong(Order::sequence);
```

Best buy is highest price, then earliest sequence. Best sell is lowest price, then earliest sequence. Orders cross when:

```java
bestBuy.price() >= bestSell.price()
```

Trade quantity is `min(buy.remaining(), sell.remaining())`. Decrement both remaining quantities. Remove only the fully filled order; a partially filled order keeps its original time priority unless the stated amend rules say otherwise.

Interview invariant:

> "At every matching step, I compare the highest-priority live buy with the highest-priority live sell. If they cross, I execute the maximum possible quantity without making either remaining quantity negative."

Production concerns that can remain discussion-only unless asked: cancel/replace semantics, order-ID index, persistence/recovery, deterministic sequencing, concurrency model, market/limit orders, self-trade prevention, and audit events.

</details>

**Transfer question:** What should happen to time priority if an amend increases quantity? State the policy explicitly rather than assuming.

**Evidence log:**

| Date | Result | Comparator/crossing/state error | Next |
|---|---|---|---|
| | | | |

### GAP-010 Stream pipeline and collector selection

**Priority:** B
**Types:** `TRIGGER`, `TEMPLATE`, `JAVA`, `TRADEOFF`

**Cold prompt**

1. State the three parts of a stream pipeline.
2. Choose the terminal collector for list, set, map, grouping, partitioning, counting, joining, and numeric summary.
3. Explain duplicate-key handling in `toMap`.
4. Explain why a consumed stream cannot be reused.

<details>
<summary>Open correction only after answering</summary>

Model:

```text
source -> zero or more intermediate operations -> one terminal operation
```

Intermediate operations such as `filter`, `map`, `flatMap`, `distinct`, `sorted`, `limit`, and `skip` are lazy. Work begins when a terminal operation such as `collect`, `reduce`, `count`, `findFirst`, `anyMatch`, or `forEach` executes. A stream represents a one-use traversal and cannot be reused after its terminal operation.

Collector map:

```java
list.stream().filter(this::valid).toList();
list.stream().map(this::key).collect(Collectors.toSet());

list.stream().collect(Collectors.toMap(
        Item::id,
        Function.identity(),
        (left, right) -> left));

list.stream().collect(Collectors.groupingBy(Item::category));
list.stream().collect(Collectors.groupingBy(Item::category, Collectors.counting()));
list.stream().collect(Collectors.partitioningBy(Item::active));
list.stream().map(Item::name).collect(Collectors.joining(", "));
list.stream().collect(Collectors.summarizingInt(Item::score));
```

`toMap` must receive a merge function when duplicate keys are possible. The merge function is a business decision: keep first, keep last, combine values, or choose by a comparator. Say which one and why.

Avoid streams when an imperative loop is materially clearer, early exit is central, checked exceptions dominate, or the pipeline would hide stateful/mutation-heavy logic.

</details>

**Transfer question:** Convert a nested `List<List<String>>` to one distinct sorted list, then explain why `flatMap` is necessary.

**Evidence log:**

| Date | Result | Collector or explanation missed | Next |
|---|---|---|---|
| | | | |

### GAP-011 Stream interview drills

**Priority:** B
**Types:** `TEMPLATE`, `JAVA`, `COMMUNICATION`

**Cold prompt**

Reconstruct any two without notes, then speak the return type before coding:

1. group employees by department;
2. highest-paid employee per department;
3. values that occur more than once;
4. flatten all employee names from departments;
5. first non-repeating character while preserving encounter order.

<details>
<summary>Open correction only after answering</summary>

```java
Map<String, List<Employee>> byDepartment = employees.stream()
        .collect(Collectors.groupingBy(Employee::department));

Map<String, Optional<Employee>> highestByDepartment = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::department,
                Collectors.maxBy(Comparator.comparingInt(Employee::salary))));

Map<String, Employee> highestWithoutOptional = employees.stream()
        .collect(Collectors.toMap(
                Employee::department,
                Function.identity(),
                BinaryOperator.maxBy(Comparator.comparingInt(Employee::salary))));

Set<Integer> duplicates = nums.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet().stream()
        .filter(e -> e.getValue() > 1)
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());

List<String> names = departments.stream()
        .flatMap(d -> d.employees().stream())
        .map(Employee::name)
        .toList();

Optional<Character> firstNonRepeating = input.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(
                Function.identity(),
                LinkedHashMap::new,
                Collectors.counting()))
        .entrySet().stream()
        .filter(e -> e.getValue() == 1)
        .map(Map.Entry::getKey)
        .findFirst();
```

The first-non-repeating version above operates on UTF-16 `char` values, not arbitrary Unicode code points. Mention the input constraint if Unicode correctness matters.

</details>

**Transfer question:** Return department to the employee names tied for maximum salary, not just one employee. Which downstream collector or second pass would you use?

**Evidence log:**

| Date | Result | Drill reconstructed | Next |
|---|---|---|---|
| | | | |

### GAP-012 Small Java return and overflow patterns

**Priority:** B
**Types:** `JAVA`, `EDGE`, `TEMPLATE`

**Cold prompt**

1. Return a topological order only if all courses were processed.
2. Sum large quantities under a modulus without overflowing first.
3. Explain when `new int[0]` is better than `null`.
4. Explain why casting after an overflowing `int` multiplication is too late.

<details>
<summary>Open correction only after answering</summary>

```java
return index == numCourses ? result : new int[0];
```

The completion counter proves whether the traversal processed every vertex; fewer processed vertices implies a cycle for the standard Kahn topological-sort contract.

```java
long total = 0L;
for (long quantity : quantities) {
    total = (total + quantity) % MOD;
}
return (int) total;
```

Use an empty array/collection when "valid result with zero elements" is part of the normal return type. Reserve null for contracts where absence is semantically different and explicitly documented.

Promote before arithmetic:

```java
long required = (long) m * k; // correct
// long required = (long) (m * k); // int may overflow before the cast
```

</details>

**Transfer question:** Where else in binary search, comparator arithmetic, and quantity matching can overflow silently change correctness?

**Evidence log:**

| Date | Result | Overflow/completion contract missed | Next |
|---|---|---|---|
| | | | |

## New Card Template

Copy this block when an inbox line proves recurring or high-value. Keep it small enough to retrieve in under ten minutes.

```markdown
### GAP-nnn Short name

**Priority:** A/B/C
**Types:** `TRIGGER` / `INVARIANT` / `TEMPLATE` / `JAVA` / `EDGE` / `TRADEOFF` / `COMMUNICATION`
**Anchor:** problem/source/local file link

**Cold prompt**

1. Question that exposes the exact miss.
2. Ask for the invariant or decision rule.
3. Ask for one edge case or variation.

<details>
<summary>Open correction only after answering</summary>

Smallest correct mental model, invariant, or skeleton. No full tutorial.

</details>

**Transfer question:** Change one meaningful constraint.

**Evidence log:**

| Date | Result | What was still missing | Next |
|---|---|---|---|
| | | | |
```

## Stable/Archived Index

Do not remove a durable card when it becomes stable. Change its queue status and add one row here. A stable card returns to `ACTIVE` if it fails again.

| ID | Stabilized on | Evidence | Reactivation trigger |
|---|---|---|---|
| | | | |

## Weekly Hygiene: 10 minutes maximum

1. Process remaining inbox lines.
2. Keep only recurring and actionable misses as durable cards.
3. Merge duplicate cards instead of creating near-identical notes.
4. Move cards with two spaced clean reconstructions to `STABLE`.
5. Reactivate a card immediately after a real miss.
6. Count evidence: `cards reconstructed cold / cards attempted`.
7. Do not copy full solutions, editorials, or every fact learned.

The purpose of this file is to reduce anxiety by making the next action mechanical: **capture one line, retrieve one card, record one result, repair only the missing piece.**
