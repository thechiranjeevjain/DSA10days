# DSA — 21 Core Pattern Cards to Memorize
## The reusable mental machines behind the problem bank

> **Memorize this, not 150 final solutions.**
>
> For each pattern, retrieve:
>
> **TRIGGER → INVARIANT → SKELETON → COMPLEXITY → FAILURES → VARIATIONS → RECALL PHRASE**
>
> For each A/B/C anchor, retrieve only:
>
> **ROLE → RECOGNITION → CORE INVARIANT → TRANSFER**

---


> **Why 21 instead of forcing 20?** Dijkstra and Union-Find are different mental machines. Keeping them separate improves recognition and prevents category confusion.

---

# CARD 01 — HashMap / Frequency

## TRIGGER

Use when the problem says or implies:

- count occurrences
- duplicates / uniqueness
- matching frequencies
- complement lookup
- “have I seen this before?”
- map one value to information about another
- need expected O(1) membership / lookup

## INVARIANT

> The map contains exactly the information needed from the portion of input already processed.

For frequency problems:

> `freq[x]` equals the count of `x` seen in the relevant region.

For lookup problems:

> Before processing the current item, the map represents valid previous candidates only.

## JAVA SKELETON

```java
Map<Integer, Integer> freq = new HashMap<>();

for (int x : nums) {
    freq.put(x, freq.getOrDefault(x, 0) + 1);
}
```

Complement lookup:

```java
Map<Integer, Integer> seen = new HashMap<>();

for (int i = 0; i < nums.length; i++) {
    int need = target - nums[i];

    if (seen.containsKey(need)) {
        return new int[]{seen.get(need), i};
    }

    seen.put(nums[i], i);
}
```

**COMPLEXITY SHAPE:** Usually O(n) expected time with O(n) map space.

## ANCHOR MICRO-CARDS

### A — Valid Anagram

**ROLE:** Primitive frequency-table training.

**RECOGNITION:** Same multiset of characters.

**CORE INVARIANT:** Tracked counts represent exactly the characters still unmatched between the two strings.

**TRANSFER:** Two Sum, grouping, duplicate/frequency problems.

### B — Two Sum

**ROLE:** Canonical fast-lookup anchor.

**RECOGNITION:** Pair target + need information from earlier values.

**CORE INVARIANT:** Before index i, map contains valid earlier values/indices only.

**TRANSFER:** 3Sum primitive, complement lookup, seen-before state.

### C — Top K Frequent Elements

**ROLE:** Frequency + selection transfer.

**RECOGNITION:** Need k most frequent rather than a full ordering.

**CORE INVARIANT:** Frequency map is complete; selection keeps only best candidates needed.

**TRANSFER:** Heap/bucket top-k, frequency-ranked problems.

## COMMON FAILURES

- updating the map before checking when order matters
- confusing frequency with index
- forgetting duplicate values
- using sorting when O(n) lookup is simpler
- forgetting `getOrDefault`
- modifying a map while iterating over it incorrectly

## VARIATIONS

- frequency map
- first occurrence / last occurrence
- deduplication
- complement lookup
- grouping
- prefix-sum frequency
- cache/index table

## RECALL PHRASE

> **Need fast memory of what I have already seen → HashMap.**

---

# CARD 02 — Two Pointers

## TRIGGER

Use when:

- array/string is sorted, or can be sorted
- compare from both ends
- pair/triplet target
- palindrome
- remove/partition in place
- two sequences advance at different rates
- one pointer can be moved without reconsidering earlier positions

## INVARIANT

> Everything outside the active pointer range is already resolved.

Opposite ends:

> Given current `left` and `right`, the comparison tells which side can be safely discarded.

Same direction:

> `[0 .. slow)` is the already-correct compacted/processed region.

## JAVA SKELETON

Opposite ends:

```java
int left = 0;
int right = nums.length - 1;

while (left < right) {
    // inspect nums[left], nums[right]

    if (/* move left */) {
        left++;
    } else {
        right--;
    }
}
```

Sorted 2Sum:

```java
while (left < right) {
    int sum = nums[left] + nums[right];

    if (sum == target) {
        // found
    } else if (sum < target) {
        left++;
    } else {
        right--;
    }
}
```

**COMPLEXITY SHAPE:** Usually O(n) after any required sort; sorting variants are O(n log n).

## ANCHOR MICRO-CARDS

### A — Valid Palindrome

**ROLE:** Primitive opposite-end pointers.

**RECOGNITION:** Symmetric comparison with ignorable characters.

**CORE INVARIANT:** Everything outside [left,right] is already validated.

**TRANSFER:** Filtered scans, palindrome variants.

### B — Container With Most Water

**ROLE:** Pointer-movement proof anchor.

**RECOGNITION:** Two endpoints define score; width shrinks each step.

**CORE INVARIANT:** Only moving the shorter wall can possibly improve the limiting height.

**TRANSFER:** Trapping-water intuition, pointer elimination.

### C — 3Sum

**ROLE:** Sorted two-pointer transfer.

**RECOGNITION:** Unique triplets hitting a target.

**CORE INVARIANT:** For fixed i, left/right search sorted suffix without missing a valid pair.

**TRANSFER:** 4Sum/k-Sum, duplicate handling.

## COMMON FAILURES

- moving the wrong pointer without proving why
- missing duplicate skipping in 3Sum
- using two pointers on data where monotonic movement is not valid
- `left <= right` vs `left < right`
- forgetting to sort when the proof requires sorted order

## VARIATIONS

- opposite-direction pointers
- slow/fast compaction
- partitioning
- pair sum
- k-Sum reduction
- palindrome scan
- trapping-water style boundary pointers

## RECALL PHRASE

> **If one comparison lets me permanently discard one side, think two pointers.**

---

# CARD 03 — Sliding Window

## TRIGGER

Use when:

- contiguous subarray / substring
- longest / shortest / count
- constraint can be maintained incrementally
- window expands with `right`
- invalidity can be repaired by advancing `left`

Typical words:

```text
substring
subarray
contiguous
at most K
without repeating
minimum window
maximum length
```

## INVARIANT

> `[left .. right]` is the current active window.

Variable window:

> After shrinking, the window satisfies the required validity condition.

Fixed window:

> The window always contains exactly `k` elements before evaluating it.

## JAVA SKELETON

Variable:

```java
int left = 0;

for (int right = 0; right < nums.length; right++) {
    add(nums[right]);

    while (invalid()) {
        remove(nums[left]);
        left++;
    }

    updateAnswer(left, right);
}
```

Fixed:

```java
for (int right = 0; right < nums.length; right++) {
    add(nums[right]);

    if (right >= k) {
        remove(nums[right - k]);
    }

    if (right >= k - 1) {
        updateAnswer();
    }
}
```

**COMPLEXITY SHAPE:** Usually O(n): each pointer moves forward at most n times.

## ANCHOR MICRO-CARDS

### A — Minimum Size Subarray Sum

**ROLE:** Primitive variable-window mechanics.

**RECOGNITION:** Positive contiguous subarray + minimum length + threshold.

**CORE INVARIANT:** Shrink as far as possible while window still meets target.

**TRANSFER:** Expand-right/shrink-left mechanics.

### B — Longest Substring Without Repeating Characters

**ROLE:** Canonical validity-window anchor.

**RECOGNITION:** Longest contiguous substring under uniqueness.

**CORE INVARIANT:** Current [L..R] has no duplicate; left never moves backward.

**TRANSFER:** At-most-K distinct, replacement windows.

### C — Minimum Window Substring

**ROLE:** Hard minimum-valid-window transfer.

**RECOGNITION:** Smallest substring satisfying required frequencies.

**CORE INVARIANT:** Window covers all required counts; shrink while still valid.

**TRANSFER:** Need/have bookkeeping, shortest satisfying window.

## COMMON FAILURES

- shrinking under the wrong condition
- moving `left` backward
- stale frequency counts
- not deleting zero-frequency keys when distinct-count matters
- confusing “at most K” with “exactly K”
- updating answer before/after shrink at the wrong time

## VARIATIONS

- fixed-size window
- longest valid window
- shortest valid window
- at-most K
- exactly K via `atMost(K) - atMost(K - 1)`
- frequency matching
- distinct-count windows

## RECALL PHRASE

> **Contiguous + maintainable constraint → expand right, repair with left.**

---

# CARD 04 — Prefix Sum

## TRIGGER

Use when:

- repeated range sum
- subarray sum
- count subarrays with a target
- cumulative balance / difference
- convert a range into subtraction of two historical states

## INVARIANT

> `prefix[i]` represents the aggregate of everything before or through position `i`, consistently.

Core identity:

```text
sum(l..r) = prefix[r + 1] - prefix[l]
```

Target subarray:

```text
prefix[j] - prefix[i] = target
⇒ prefix[i] = prefix[j] - target
```

## JAVA SKELETON

Range prefix:

```java
int[] prefix = new int[nums.length + 1];

for (int i = 0; i < nums.length; i++) {
    prefix[i + 1] = prefix[i] + nums[i];
}
```

Count target subarrays:

```java
Map<Integer, Integer> freq = new HashMap<>();
freq.put(0, 1);

int prefix = 0;
int count = 0;

for (int x : nums) {
    prefix += x;

    count += freq.getOrDefault(prefix - target, 0);

    freq.put(prefix, freq.getOrDefault(prefix, 0) + 1);
}
```

**COMPLEXITY SHAPE:** Build O(n); range query O(1). Prefix-frequency counting is O(n) expected time, O(n) space.

## ANCHOR MICRO-CARDS

### A — Range Sum Query — Immutable

**ROLE:** Concrete prefix-sum primitive.

**RECOGNITION:** Many immutable range-sum queries on one array.

**CORE INVARIANT:** prefix[i] has one fixed meaning; range(l,r) = prefix[r+1] - prefix[l].

**TRANSFER:** 2D prefix, prefix XOR, cumulative arrays.

### B — Binary Subarrays With Sum

**ROLE:** Canonical prefix-frequency counting.

**RECOGNITION:** Count contiguous subarrays with exact target sum.

**CORE INVARIANT:** Before current prefix is inserted, map contains counts of all earlier prefix sums.

**TRANSFER:** Subarray Sum Equals K, exact-count transforms.

### C — Path Sum III

**ROLE:** Tree transfer of prefix sums.

**RECOGNITION:** Count downward paths ending at current node.

**CORE INVARIANT:** Map contains prefix sums on the current root-to-node path only.

**TRANSFER:** Tree path counting + backtracking state.

## COMMON FAILURES

- forgetting initial prefix `0`
- off-by-one prefix definition
- confusing prefix index with array index
- using sliding window when negative numbers destroy monotonicity
- using `int` when cumulative sums may require `long`

## VARIATIONS

- 1D prefix
- 2D prefix
- prefix XOR
- prefix frequency map
- running balance
- tree path prefix sum

## RECALL PHRASE

> **Range/subarray result = difference between two cumulative states.**

---

# CARD 05 — Binary Search

## TRIGGER

Use when:

- sorted data
- ordered search space
- find exact target
- first / last occurrence
- insertion point
- first true / last false boundary
- each comparison can eliminate half

## INVARIANT

Closed interval version:

> If the target exists, it remains inside `[left .. right]`.

Boundary search:

> Maintain a region that definitely cannot contain a better answer and a region that still can.

## JAVA SKELETON

Exact search:

```java
int left = 0;
int right = nums.length - 1;

while (left <= right) {
    int mid = left + (right - left) / 2;

    if (nums[mid] == target) {
        return mid;
    }

    if (nums[mid] < target) {
        left = mid + 1;
    } else {
        right = mid - 1;
    }
}

return -1;
```

First true / lower bound:

```java
int left = 0;
int right = n;   // answer may be n

while (left < right) {
    int mid = left + (right - left) / 2;

    if (predicate(mid)) {
        right = mid;
    } else {
        left = mid + 1;
    }
}

return left;
```

**COMPLEXITY SHAPE:** O(log n) time, O(1) auxiliary space for iterative array search.

## ANCHOR MICRO-CARDS

### A — Binary Search

**ROLE:** Primitive search invariant.

**RECOGNITION:** Sorted data + exact target.

**CORE INVARIANT:** If target exists, it stays inside current search interval.

**TRANSFER:** Lower/upper bound, insertion point.

### B — Find First and Last Position of Element in Sorted Array

**ROLE:** Boundary-search anchor.

**RECOGNITION:** Need extreme occurrence, not any match.

**CORE INVARIANT:** Search keeps possibility of a better boundary even after a hit.

**TRANSFER:** Lower/upper bound with duplicates.

### C — Search in Rotated Sorted Array

**ROLE:** Modified-search transfer.

**RECOGNITION:** Sorted array rotated around pivot.

**CORE INVARIANT:** At least one half is sorted; discard only half that cannot contain target.

**TRANSFER:** Rotated II, modified binary search.

## COMMON FAILURES

- mixing binary-search templates
- wrong interval meaning
- forgetting `+1` / `-1`
- infinite loop
- integer overflow in midpoint
- not preserving a possible answer during boundary search

## VARIATIONS

- exact search
- lower bound
- upper bound
- first true
- last true
- rotated array
- peak search

## RECALL PHRASE

> **If one test discards half the remaining search space, binary search.**

---

# CARD 06 — Binary Search on Answer

## TRIGGER

Use when the problem asks:

```text
minimum possible maximum
maximum possible minimum
minimum speed
minimum capacity
earliest day
smallest feasible value
largest feasible value
```

and feasibility is monotonic:

```text
false false false true true true
```

or:

```text
true true true false false
```

## INVARIANT

> The answer lies on the boundary between feasible and infeasible values.

For minimum feasible:

> Everything below the boundary is impossible; everything at/above it is feasible.

## JAVA SKELETON

```java
long left = minimumPossible;
long right = maximumPossible;

while (left < right) {
    long mid = left + (right - left) / 2;

    if (feasible(mid)) {
        right = mid;
    } else {
        left = mid + 1;
    }
}

return left;
```

**COMPLEXITY SHAPE:** O(cost(feasibility-check) × log(answer-range)).

## ANCHOR MICRO-CARDS

### A — Koko Eating Bananas

**ROLE:** Canonical first-feasible answer search.

**RECOGNITION:** Minimum rate satisfying monotonic feasibility.

**CORE INVARIANT:** Below answer infeasible; answer and above feasible.

**TRANSFER:** Speed/rate/capacity search.

### B — Capacity To Ship Packages Within D Days

**ROLE:** Feasibility-function strengthening.

**RECOGNITION:** Minimum capacity meeting a deadline.

**CORE INVARIANT:** Capacity feasibility is monotonic; simulation returns required days correctly.

**TRANSFER:** Workload partitioning.

### C — Split Array Largest Sum

**ROLE:** Hard minimax partition transfer.

**RECOGNITION:** Minimize largest partition sum.

**CORE INVARIANT:** Candidate maxSum is feasible iff greedy partition count stays within limit.

**TRANSFER:** Painter/book-allocation family.

## COMMON FAILURES

- binary searching before proving monotonicity
- wrong lower/upper bound; for the shown minimum-feasible template, `right` must be a feasible upper bound
- wrong feasibility function
- overflow inside feasibility calculation
- returning last tested `mid` instead of the boundary
- using exact-search binary search instead of first-feasible search

## VARIATIONS

- minimum feasible
- maximum feasible
- rate / speed
- capacity
- days
- partition threshold
- distance threshold

## RECALL PHRASE

> **I can test a candidate answer, and feasibility changes only once → binary search the answer.**

---

# CARD 07 — Intervals / Sweep Line

## TRIGGER

Use when:

- start/end ranges
- overlap
- merge
- meeting rooms
- concurrent events
- coverage
- capacity over time
- “how many active at once?”

## INVARIANT

Merge:

> Process intervals in sorted order; the current merged interval summarizes all overlapping intervals seen so far.

Sweep:

> Running count/state equals the number or total effect of currently active events at the current coordinate/time.

## JAVA SKELETON

Merge:

```java
Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

List<int[]> result = new ArrayList<>();

for (int[] cur : intervals) {
    if (result.isEmpty() ||
        result.get(result.size() - 1)[1] < cur[0]) {

        result.add(new int[]{cur[0], cur[1]});
    } else {
        int[] last = result.get(result.size() - 1);
        last[1] = Math.max(last[1], cur[1]);
    }
}
```

Sweep events:

```java
List<int[]> events = new ArrayList<>();

for (int[] interval : intervals) {
    events.add(new int[]{interval[0], +1});
    events.add(new int[]{interval[1], -1});
}

events.sort(/* coordinate, then tie rule */);

int active = 0;

for (int[] e : events) {
    active += e[1];
}
```

**COMPLEXITY SHAPE:** Usually O(n log n) because boundaries/intervals must be sorted.

## ANCHOR MICRO-CARDS

### A — Merge Intervals

**ROLE:** Primitive overlap anchor.

**RECOGNITION:** Ranges overlap and must be consolidated.

**CORE INVARIANT:** Current merged interval summarizes all overlapping ranges processed so far.

**TRANSFER:** Insert interval, union/coverage.

### B — Meeting Rooms II

**ROLE:** Concurrent-activity anchor.

**RECOGNITION:** Need maximum simultaneous intervals.

**CORE INVARIANT:** Heap/sweep state equals currently active meetings.

**TRANSFER:** Rooms/platforms/resource allocation.

### C — Car Pooling

**ROLE:** Capacity sweep transfer.

**RECOGNITION:** Capacity changes at ordered pickup/drop points.

**CORE INVARIANT:** Running occupancy equals net event effect up to current position.

**TRANSFER:** Difference arrays, capacity timelines.

## COMMON FAILURES

- wrong endpoint tie rule
- forgetting to sort
- merging touching intervals when spec says not to, or vice versa
- modifying original arrays unexpectedly
- not distinguishing “merge ranges” from “count overlaps”

## VARIATIONS

- merge
- insert interval
- max overlap
- min rooms
- event sweep
- difference array
- capacity timeline

## RECALL PHRASE

> **Ranges become easy after ordering their boundaries.**

---

# CARD 08 — Greedy

## TRIGGER

Use when:

- need optimal result from local choices
- sorting exposes a safe choice
- once a prefix becomes impossible, earlier choices can be discarded
- choose earliest finish / cheapest / largest gain
- global result can be proven by exchange argument or invariant

## INVARIANT

> Every local choice preserves the existence of an optimal solution for the remaining problem.

You must be able to answer:

> **Why can this decision never make the future worse?**

## JAVA SKELETON

Greedy is problem-specific, but the shape is:

```java
sortByUsefulCriterion(items);

State state = initialState();

for (Item item : items) {
    if (canTake(item, state)) {
        take(item);
        update(state, item);
    }
}
```

Reset-style:

```java
int start = 0;
int balance = 0;

for (int i = 0; i < n; i++) {
    balance += gain(i);

    if (balance < 0) {
        start = i + 1;
        balance = 0;
    }
}
```

**COMPLEXITY SHAPE:** Usually O(n), or O(n log n) when sorting enables the greedy proof.

## ANCHOR MICRO-CARDS

### A — Minimum Number of Arrows to Burst Balloons

**ROLE:** Primitive sort-by-end greedy proof.

**RECOGNITION:** Choose minimum points to cover overlapping intervals.

**CORE INVARIANT:** After sorting by end, firing at the earliest possible end leaves maximum room for future balloons.

**TRANSFER:** Interval scheduling, earliest-finish greedy.

### B — Gas Station

**ROLE:** Canonical failed-prefix greedy proof.

**RECOGNITION:** Choose a circular start so cumulative resource never fails.

**CORE INVARIANT:** If balance becomes negative at i, no start inside that failed prefix can succeed past i.

**TRANSFER:** Reset-prefix greedy, circular feasibility.

### C — Task Scheduler

**ROLE:** Frequency-driven greedy transfer.

**RECOGNITION:** Repeated tasks plus cooldown create idle pressure.

**CORE INVARIANT:** The highest frequencies determine the tightest schedule frame; other tasks can only fill its gaps.

**TRANSFER:** Cooldown scheduling, rearrangement by frequency.

## COMMON FAILURES

- calling something greedy without proof
- choosing a plausible local rule that has a counterexample
- confusing greedy with DP
- wrong sort criterion
- failure to state the exchange/invariant argument

## VARIATIONS

- earliest finish
- running minimum/maximum
- reset failed prefix
- interval scheduling
- frequency-based scheduling
- choose locally dominant candidate

## RECALL PHRASE

> **If I can prove a local choice never hurts the best possible future, greedy.**

---

# CARD 09 — Monotonic Stack

## TRIGGER

Use for:

- next greater / next smaller
- previous greater / previous smaller
- first boundary where monotonic condition breaks
- nearest dominating element
- histogram rectangle
- contribution range of each element

## INVARIANT

> Stack elements remain monotonic, and each stored index is still unresolved/useful.

When popping:

> The current element is the first element that resolves the popped element's boundary/question.

## JAVA SKELETON

Next greater:

```java
Deque<Integer> stack = new ArrayDeque<>();

for (int i = 0; i < nums.length; i++) {
    while (!stack.isEmpty() &&
           nums[i] > nums[stack.peek()]) {

        int j = stack.pop();
        answer[j] = i; // or nums[i], depending on the requested output
    }

    stack.push(i);
}
```

**COMPLEXITY SHAPE:** O(n) amortized time; each index is pushed and popped at most once.

## ANCHOR MICRO-CARDS

### A — Next Greater Element I

**ROLE:** Primitive unresolved-index stack.

**RECOGNITION:** Need first greater value to right.

**CORE INVARIANT:** Stack contains unresolved elements in monotonic order.

**TRANSFER:** Next/previous greater/smaller.

### B — Daily Temperatures

**ROLE:** Index-distance anchor.

**RECOGNITION:** Need distance to next warmer day.

**CORE INVARIANT:** Current day resolving a popped index is its first warmer future day.

**TRANSFER:** Next-event distances, stock span.

### C — Largest Rectangle in Histogram

**ROLE:** Boundary-width transfer.

**RECOGNITION:** Each bar needs widest range where it is limiting height.

**CORE INVARIANT:** When popped, current index fixes right smaller boundary; stack reveals left boundary.

**TRANSFER:** Maximal Rectangle, contribution ranges.

## COMMON FAILURES

- storing values when indices are required
- wrong monotonic direction
- `<` vs `<=` with duplicates
- forgetting unresolved items
- not understanding what a pop means
- wrong left/right boundary formula

## VARIATIONS

- next greater
- next smaller
- previous greater/smaller
- circular scan
- histogram
- contribution counting
- stock span

## RECALL PHRASE

> **Need nearest boundary where order breaks → monotonic stack.**

---

# CARD 10 — Monotonic Deque

## TRIGGER

Use when:

- max/min for every moving window
- need best candidate repeatedly while old candidates expire
- newer candidate can permanently dominate older candidate

## INVARIANT

For sliding maximum:

> Deque stores indices inside the window, with values in decreasing order.

Therefore:

> Front is always the maximum.

Dominance rule:

> If a newer value is >= an older value, the older one can never be useful again while the newer one remains.

## JAVA SKELETON

```java
Deque<Integer> dq = new ArrayDeque<>();

for (int right = 0; right < nums.length; right++) {

    while (!dq.isEmpty() &&
           dq.peekFirst() <= right - k) {
        dq.pollFirst();
    }

    while (!dq.isEmpty() &&
           nums[dq.peekLast()] <= nums[right]) {
        dq.pollLast();
    }

    dq.offerLast(right);

    if (right >= k - 1) {
        answer[right - k + 1] = nums[dq.peekFirst()];
    }
}
```

**COMPLEXITY SHAPE:** O(n) amortized time, O(k) deque space for a window of size k.

## ANCHOR MICRO-CARDS

### A — Sliding Window Maximum

**ROLE:** Canonical monotonic-deque anchor.

**RECOGNITION:** Maximum for every fixed-size moving window.

**CORE INVARIANT:** Deque contains in-window indices with decreasing values; front is the maximum.

**TRANSFER:** Sliding minimum, repeated best under expiry.

### B — Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit

**ROLE:** Dual-deque transfer.

**RECOGNITION:** Longest window whose max-min stays within a limit.

**CORE INVARIANT:** One deque tracks max, one tracks min; shrink until max-min <= limit.

**TRANSFER:** Simultaneous window extrema, validity by range.

### C — Constrained Subsequence Sum

**ROLE:** DP + monotonic-queue transfer.

**RECOGNITION:** Current DP state needs the best previous state from only the last k positions.

**CORE INVARIANT:** Deque stores eligible DP indices in decreasing dp-value order; front is best transition candidate.

**TRANSFER:** Monotonic-queue DP optimization.

## COMMON FAILURES

- storing values instead of indices
- removing expired indices after reading the answer
- wrong monotonic direction
- misunderstanding why dominated elements can be removed permanently
- confusing deque with heap

## VARIATIONS

- sliding maximum
- sliding minimum
- bounded-range optimization
- monotonic queue DP optimization

## RECALL PHRASE

> **Moving window + repeated best + expiring candidates → monotonic deque.**

---

# CARD 11 — Heap / Top-K

## TRIGGER

Use when:

- repeatedly need smallest/largest
- top K
- kth largest / kth smallest
- merge sorted streams
- scheduling by priority
- dynamic median
- do not need the entire collection sorted

## INVARIANT

Top K:

> Heap contains exactly the best K candidates seen so far.

K-way merge:

> Heap contains the next available candidate from each source.

Two heaps:

> Lower half and upper half remain ordered and size-balanced.

## JAVA SKELETON

Top K:

```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

for (int x : nums) {
    minHeap.offer(x);

    if (minHeap.size() > k) {
        minHeap.poll();
    }
}
```

Custom comparator:

```java
PriorityQueue<Node> pq =
    new PriorityQueue<>(
        Comparator.comparingInt(node -> node.value)
    );
```

**COMPLEXITY SHAPE:** Top-k is typically O(n log k), O(k) space; streaming median insertion is O(log n).

## ANCHOR MICRO-CARDS

### A — Kth Largest Element in an Array

**ROLE:** Primitive bounded-heap selection.

**RECOGNITION:** Need kth statistic, not full ordering.

**CORE INVARIANT:** Min-heap of size k contains k largest seen so far.

**TRANSFER:** Streaming kth, bounded selection.

### B — Top K Frequent Elements

**ROLE:** Frequency + top-k anchor.

**RECOGNITION:** Need top-k by derived score.

**CORE INVARIANT:** Selection structure retains exactly the best k frequency candidates.

**TRANSFER:** Custom-score top-k.

### C — Find Median from Data Stream

**ROLE:** Two-heap balancing transfer.

**RECOGNITION:** Need median after every insertion.

**CORE INVARIANT:** Lower max-heap <= upper min-heap and sizes differ by at most one.

**TRANSFER:** Online percentile/order statistics.

## COMMON FAILURES

- min-heap vs max-heap confusion
- comparator reversed incorrectly
- keeping all N elements when only K are needed
- integer subtraction comparator overflow
- forgetting deterministic tie rules when required

## VARIATIONS

- top K
- kth statistic
- k-way merge
- scheduler
- two heaps
- streaming order statistic

## RECALL PHRASE

> **Need the next best item repeatedly, not a full sort → heap.**

---

# CARD 12 — Linked-List Pointers

## TRIGGER

Use when:

- reverse/reorder list
- cycle
- middle
- intersection
- kth node
- reconnect nodes
- O(1) auxiliary-space pointer manipulation

## INVARIANT

Before rewiring:

> Never lose the pointer to the unexplored remainder.

Dummy-head problems:

> `dummy.next` always points to the current valid output head.

Slow/fast:

> Pointer speed difference encodes distance information.

## JAVA SKELETON

Reverse:

```java
ListNode prev = null;
ListNode curr = head;

while (curr != null) {
    ListNode next = curr.next;
    curr.next = prev;
    prev = curr;
    curr = next;
}

return prev;
```

Slow/fast:

```java
ListNode slow = head;
ListNode fast = head;

while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
}
```

**COMPLEXITY SHAPE:** Usually O(n) time and O(1) auxiliary space for pointer-only variants.

## ANCHOR MICRO-CARDS

### A — Reverse Linked List

**ROLE:** Primitive pointer rewiring.

**RECOGNITION:** In-place reversal with O(1) extra space.

**CORE INVARIANT:** next preserves unexplored suffix; prev is fully reversed prefix.

**TRANSFER:** Pair/group reversal, rotation.

### B — Linked List Cycle

**ROLE:** Canonical slow/fast anchor.

**RECOGNITION:** Detect cycle without extra memory.

**CORE INVARIANT:** Fast moves twice as quickly; inside a cycle it must meet slow.

**TRANSFER:** Middle, cycle entry.

### C — Reverse Nodes in K Group

**ROLE:** Hard boundary-rewiring transfer.

**RECOGNITION:** Reverse fixed-size blocks while preserving remainder.

**CORE INVARIANT:** Reverse only after confirming k nodes; reconnect previous block, reversed block, and suffix.

**TRANSFER:** Pair swap, block manipulation.

## COMMON FAILURES

- losing `next`
- null dereference
- returning wrong head
- not using dummy node for head-changing operations
- confusing value equality with node identity
- off-by-one around kth/group boundaries

## VARIATIONS

- reverse
- slow/fast
- cycle entry
- merge
- pair swap
- k-group reversal
- odd/even partition
- rotate

## RECALL PHRASE

> **Draw the pointers; preserve the remainder before changing links.**

---

# CARD 13 — Tree DFS

## TRIGGER

Use when:

- subtree answer contributes to parent
- depth / height
- path
- validate recursively
- combine left and right answers
- “for every node”
- answer naturally defined in terms of child answers

## INVARIANT

Before coding, complete this sentence:

> **`dfs(node)` returns __________.**

Examples:

```text
height of subtree
whether subtree is valid
best downward path
whether target exists
subtree sum
```

Global-answer pattern:

> Return what the parent needs; separately update what the whole problem needs.

## JAVA SKELETON

```java
int dfs(TreeNode node) {
    if (node == null) {
        return BASE;
    }

    int left = dfs(node.left);
    int right = dfs(node.right);

    // combine
    return RESULT_FOR_PARENT;
}
```

**COMPLEXITY SHAPE:** Usually O(n) time, O(h) recursion stack where h is tree height.

## ANCHOR MICRO-CARDS

### A — Maximum Depth of Binary Tree

**ROLE:** Primitive recursive-return training.

**RECOGNITION:** Parent answer depends on child subtree answers.

**CORE INVARIANT:** dfs(node) returns subtree height.

**TRANSFER:** Balanced tree, diameter.

### B — Lowest Common Ancestor of a Binary Tree

**ROLE:** Child-result combination anchor.

**RECOGNITION:** Need first node whose subtrees collectively contain both targets.

**CORE INVARIANT:** dfs(node) returns discovered target/LCA; two non-null child results make current node LCA.

**TRANSFER:** Ancestor queries, postorder flow.

### C — Binary Tree Maximum Path Sum

**ROLE:** Return-vs-global transfer.

**RECOGNITION:** Global path may use both children; parent path may use only one.

**CORE INVARIANT:** dfs(node) returns best downward gain; global answer may combine left + node + right.

**TRANSFER:** Tree DP, diameter-style global aggregation.

## COMMON FAILURES

- undefined recursive return meaning
- mixing global answer with returned value
- wrong null base case
- forgetting leaf-only requirement
- recomputing subtree work and creating O(n²)

## VARIATIONS

- preorder
- inorder
- postorder
- return boolean
- return height
- return path contribution
- global accumulator
- path state

## RECALL PHRASE

> **Tree recursion becomes easy once I define exactly what `dfs(node)` returns.**

---

# CARD 14 — Tree BFS

## TRIGGER

Use when:

- level order
- nodes by depth
- nearest/shortest in unweighted tree
- right/left view
- minimum depth
- process one layer at a time

## INVARIANT

> At the start of each outer iteration, the queue contains exactly the nodes of the current frontier.

Level processing:

> Capture `levelSize = queue.size()` before consuming the level.

## JAVA SKELETON

```java
Queue<TreeNode> queue = new ArrayDeque<>();

if (root != null) {
    queue.offer(root);
}

while (!queue.isEmpty()) {
    int levelSize = queue.size();

    for (int i = 0; i < levelSize; i++) {
        TreeNode node = queue.poll();

        if (node.left != null) {
            queue.offer(node.left);
        }

        if (node.right != null) {
            queue.offer(node.right);
        }
    }
}
```

**COMPLEXITY SHAPE:** O(n) time, O(w) queue space where w is maximum tree width.

## ANCHOR MICRO-CARDS

### A — Binary Tree Level Order Traversal

**ROLE:** Primitive frontier anchor.

**RECOGNITION:** Need nodes grouped by depth.

**CORE INVARIANT:** At level start, queue contains exactly that level; fixed levelSize prevents mixing.

**TRANSFER:** Zigzag, per-level aggregates.

### B — Binary Tree Right Side View

**ROLE:** One-result-per-level transfer.

**RECOGNITION:** Need rightmost visible node at each depth.

**CORE INVARIANT:** Process one frontier and select its final/rightmost representative.

**TRANSFER:** Left view, per-level selection.

### C — Amount of Time for Binary Tree to Be Infected

**ROLE:** Tree-to-graph BFS transfer.

**RECOGNITION:** Spread can move to parent or child.

**CORE INVARIANT:** After parent edges are added, each BFS frontier is one elapsed-time layer.

**TRANSFER:** Burn Tree, nodes at distance K.

## COMMON FAILURES

- using changing `queue.size()` inside the loop
- adding null nodes to `ArrayDeque`
- mixing current and next level
- using DFS when shortest number of edges is required

## VARIATIONS

- level order
- right/left view
- zigzag
- min depth
- tree infection/spread
- nodes at distance K

## RECALL PHRASE

> **Depth/frontier/nearest in an unweighted tree → BFS by level.**

---

# CARD 15 — BST

## TRIGGER

Use when:

- binary **search** tree
- ordered property matters
- kth smallest
- range query
- predecessor/successor
- validate ordering
- search/insert
- inorder gives sorted order

## INVARIANT

Core BST property:

> Every key in left subtree < node < every key in right subtree, subject to the problem's duplicate policy.

Validation:

> Bounds come from **all ancestors**, not only the parent.

Inorder:

> Values appear in sorted order.

## JAVA SKELETON

Search:

```java
TreeNode curr = root;

while (curr != null) {
    if (curr.val == target) {
        return curr;
    }

    curr = target < curr.val
        ? curr.left
        : curr.right;
}

return null;
```

Validation:

```java
boolean valid(TreeNode node, long low, long high) {
    if (node == null) {
        return true;
    }

    if (node.val <= low || node.val >= high) {
        return false;
    }

    return valid(node.left, low, node.val)
        && valid(node.right, node.val, high);
}
```

**COMPLEXITY SHAPE:** Search/insert O(h); full validation/inorder O(n).

## ANCHOR MICRO-CARDS

### A — Search in a Binary Search Tree

**ROLE:** Primitive BST pruning.

**RECOGNITION:** Target compared against ordered node values.

**CORE INVARIANT:** Ordering lets each comparison eliminate one entire subtree.

**TRANSFER:** Insert, predecessor/successor.

### B — Validate Binary Search Tree

**ROLE:** Global-ordering anchor.

**RECOGNITION:** Every node must respect all ancestor constraints.

**CORE INVARIANT:** node.val stays inside inherited (low,high) bounds; parent-only checks are insufficient.

**TRANSFER:** Bounds recursion, inorder validation.

### C — Kth Smallest Element in a BST

**ROLE:** Inorder-order transfer.

**RECOGNITION:** Need order statistic from BST.

**CORE INVARIANT:** Inorder emits BST values in sorted order; kth emitted value is answer.

**TRANSFER:** BST iterator, rank/select.

## COMMON FAILURES

- checking only parent-child relation
- integer boundary overflow
- forgetting inorder sorted property
- using generic DFS when BST ordering can prune
- unclear duplicate policy

## VARIATIONS

- search
- insert
- validate
- kth smallest
- iterator
- range sum
- predecessor/successor
- recover swapped nodes

## RECALL PHRASE

> **BST = tree structure + sorted-order leverage.**

---

# CARD 16 — Graph DFS / BFS

## TRIGGER

Use when:

- nodes + connections
- grid as graph
- connected components
- reachability
- flood fill
- shortest path in an **unweighted** graph
- state transitions
- “can I get from A to B?”

## INVARIANT

DFS/BFS:

> Every visited state is processed at most once.

Component counting:

> Each traversal started from an unvisited node discovers exactly one component.

BFS shortest path:

> First time a state is reached is via the minimum number of edges, provided all edges have equal cost.

## JAVA SKELETON

Graph BFS:

```java
Queue<Integer> queue = new ArrayDeque<>();
boolean[] visited = new boolean[n];

queue.offer(start);
visited[start] = true;

while (!queue.isEmpty()) {
    int node = queue.poll();

    for (int next : graph.get(node)) {
        if (!visited[next]) {
            visited[next] = true;
            queue.offer(next);
        }
    }
}
```

Grid DFS:

```java
void dfs(int r, int c) {
    if (r < 0 || r >= rows ||
        c < 0 || c >= cols ||
        visited[r][c] ||
        !isValidCell(r, c)) {
        return;
    }

    visited[r][c] = true;

    for (int[] d : dirs) {
        dfs(r + d[0], c + d[1]);
    }
}
```

**COMPLEXITY SHAPE:** O(V + E) for graph traversal; grid traversal is O(rows × cols).

## ANCHOR MICRO-CARDS

### A — Flood Fill

**ROLE:** Primitive graph/grid traversal.

**RECOGNITION:** Modify one connected region.

**CORE INVARIANT:** Every reachable same-state cell is visited once; unrelated cells untouched.

**TRANSFER:** Grid DFS/BFS, visited discipline.

### B — Number of Islands

**ROLE:** Canonical component-count anchor.

**RECOGNITION:** Count disconnected land components.

**CORE INVARIANT:** Each traversal from unseen land discovers exactly one complete island.

**TRANSFER:** Provinces, max-area/closed islands.

### C — Word Ladder

**ROLE:** Implicit-graph shortest-path transfer.

**RECOGNITION:** One-letter transformations are unweighted edges.

**CORE INVARIANT:** BFS first reaches each word at minimum transformation depth.

**TRANSFER:** State-space BFS, mutation chains.

## COMMON FAILURES

- marking visited too late
- revisiting states
- wrong graph direction
- forgetting disconnected components
- BFS queue stores insufficient state
- DFS recursion depth concerns
- treating weighted shortest path as ordinary BFS

## VARIATIONS

- components
- reachability
- grid DFS
- multi-source BFS
- implicit graph
- bipartite coloring
- clone graph
- reverse reachability

## RECALL PHRASE

> **Define node, edge, visited rule, start, target — then DFS/BFS.**

---

# CARD 17 — Topological Sort

## TRIGGER

Use when:

- prerequisites
- dependencies
- build/order tasks
- course scheduling
- directed acyclic graph
- detect dependency cycle
- “what order can these be completed?”

## INVARIANT

Kahn's algorithm:

> Queue contains exactly nodes whose remaining prerequisite count is zero.

After processing a node:

> Removing its outgoing edges may unlock new zero-indegree nodes.

Cycle rule:

> If processed count < number of nodes, a directed cycle exists.

## JAVA SKELETON

```java
List<List<Integer>> graph = new ArrayList<>();
int[] indegree = new int[n];

// build graph and indegree

Queue<Integer> queue = new ArrayDeque<>();

for (int i = 0; i < n; i++) {
    if (indegree[i] == 0) {
        queue.offer(i);
    }
}

int processed = 0;

while (!queue.isEmpty()) {
    int node = queue.poll();
    processed++;

    for (int next : graph.get(node)) {
        if (--indegree[next] == 0) {
            queue.offer(next);
        }
    }
}

boolean hasCycle = processed != n;
```

**COMPLEXITY SHAPE:** O(V + E) time and O(V + E) graph/indegree storage.

## ANCHOR MICRO-CARDS

### A — Course Schedule

**ROLE:** Primitive dependency-cycle anchor.

**RECOGNITION:** Can every prerequisite eventually be satisfied?

**CORE INVARIANT:** Queue contains exactly remaining indegree-zero nodes; processed < V means a directed cycle.

**TRANSFER:** DAG validation, dependency cycles.

### B — Course Schedule II

**ROLE:** Canonical ordering anchor.

**RECOGNITION:** Need an actual valid prerequisite order.

**CORE INVARIANT:** Removing a processed node's outgoing edges may unlock new indegree-zero nodes.

**TRANSFER:** Build ordering, workflow scheduling.

### C — Alien Dictionary

**ROLE:** Graph-construction + topo transfer.

**RECOGNITION:** Relative order of characters must be inferred from sorted words.

**CORE INVARIANT:** Only the first differing character between adjacent words creates an ordering edge; topo order must include all characters.

**TRANSFER:** Custom dependency extraction + topological ordering.

## COMMON FAILURES

- reversing edge direction
- incrementing wrong indegree
- forgetting disconnected nodes
- returning partial ordering despite cycle
- confusing undirected cycle detection with topo sort
- Alien Dictionary: missing the invalid-prefix case, e.g. `"abc"` before `"ab"`

## VARIATIONS

- can finish?
- return ordering
- alien dictionary
- build dependencies
- prerequisite queries
- DAG scheduling

## RECALL PHRASE

> **Directed prerequisites + ordering → indegree zero first.**

---

# CARD 18 — Dijkstra / Weighted Shortest Path

## TRIGGER

Use when:

- minimum total distance / cost / time
- graph edges have different nonnegative weights
- ordinary BFS is not enough
- need shortest distance from one source to many states

## INVARIANT

> `dist[v]` is the best distance discovered so far.

> When a node is popped with its current best distance, relaxing its outgoing edges is safe; stale heap entries can be skipped.

## JAVA SKELETON

```java
long[] dist = new long[n];
Arrays.fill(dist, Long.MAX_VALUE);

PriorityQueue<long[]> pq =
    new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));

dist[start] = 0;
pq.offer(new long[]{start, 0});

while (!pq.isEmpty()) {
    long[] cur = pq.poll();
    int node = (int) cur[0];
    long d = cur[1];

    if (d != dist[node]) {
        continue; // stale
    }

    for (Edge e : graph.get(node)) {
        long nd = d + e.weight;

        if (nd < dist[e.to]) {
            dist[e.to] = nd;
            pq.offer(new long[]{e.to, nd});
        }
    }
}
```

**COMPLEXITY SHAPE:** O((V + E) log V) with a binary heap; O(V + E) graph storage.

## ANCHOR MICRO-CARDS

### A — Network Delay Time

**ROLE:** Canonical single-source Dijkstra anchor.

**RECOGNITION:** Weighted directed graph + time to reach all nodes.

**CORE INVARIANT:** Each relaxation only improves `dist[next]`; stale heap entries do no work.

**TRANSFER:** Routing, latency/cost propagation.

### B — Path With Minimum Effort

**ROLE:** Minimax-cost Dijkstra transfer.

**RECOGNITION:** Path cost is the maximum edge effort, not the sum.

**CORE INVARIANT:** `dist[cell]` is the minimum possible maximum edge effort needed to reach that cell.

**TRANSFER:** Non-additive but monotonic path costs.

### C — Swim in Rising Water

**ROLE:** Threshold/minimax shortest-path transfer.

**RECOGNITION:** Need the path minimizing the maximum elevation encountered.

**CORE INVARIANT:** Heap expands the state with the smallest currently achievable path threshold.

**TRANSFER:** Minimum-bottleneck paths, threshold routing.

## COMMON FAILURES

- using ordinary BFS on unequal weights
- using Dijkstra with negative edge weights
- forgetting stale heap entries
- overflow in distance arithmetic
- not defining what the path cost means in minimax variants

## VARIATIONS

- standard additive shortest path
- minimax / bottleneck path
- weighted grid
- state-space Dijkstra
- multi-source Dijkstra

## RECALL PHRASE

> **Nonnegative weighted shortest path → best distance + min-heap + relaxation.**

---

# CARD 19 — Union-Find / DSU

## TRIGGER

Use when:

- repeatedly merge components
- ask whether two items are connected
- transitive grouping
- undirected connectivity changes over time
- edges are added and component identity matters

## INVARIANT

> Every element belongs to exactly one representative root.

> `find(x)` returns that representative; `union(a,b)` merges two different components without changing connectivity semantics.

## JAVA SKELETON

```java
class DSU {
    int[] parent;
    int[] rank;

    DSU(int n) {
        parent = new int[n];
        rank = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    boolean union(int a, int b) {
        int ra = find(a);
        int rb = find(b);

        if (ra == rb) {
            return false;
        }

        if (rank[ra] < rank[rb]) {
            int tmp = ra;
            ra = rb;
            rb = tmp;
        }

        parent[rb] = ra;

        if (rank[ra] == rank[rb]) {
            rank[ra]++;
        }

        return true;
    }
}
```

**COMPLEXITY SHAPE:** Nearly O(1) amortized per operation: O(α(n)); O(n) storage.

## ANCHOR MICRO-CARDS

### A — Number of Provinces

**ROLE:** Primitive component-union anchor.

**RECOGNITION:** Undirected connectivity matrix; need number of groups.

**CORE INVARIANT:** Nodes with the same representative belong to the same connected component.

**TRANSFER:** Connected components, cluster counting.

### B — Accounts Merge

**ROLE:** External-key mapping transfer.

**RECOGNITION:** Shared emails imply transitive membership in one account group.

**CORE INVARIANT:** Every email/account index ultimately maps to one representative component.

**TRANSFER:** Entity resolution, transitive grouping.

### C — Redundant Connection

**ROLE:** Cycle-by-union transfer.

**RECOGNITION:** Adding an undirected edge creates a cycle exactly when its endpoints are already connected.

**CORE INVARIANT:** `find(u) == find(v)` before union means the new edge is redundant.

**TRANSFER:** Dynamic cycle detection, incremental connectivity.

## COMMON FAILURES

- using DSU for directed reachability/order
- forgetting path compression
- forgetting union by rank/size
- mapping external keys incorrectly
- counting nodes instead of representative roots

## VARIATIONS

- component counting
- connectivity queries
- cycle detection
- account/entity merging
- Kruskal MST

## RECALL PHRASE

> **Repeated merging/connectivity in an undirected world → DSU.**

---

# CARD 20 — Backtracking

## TRIGGER

Use when:

- generate all possibilities
- combinations / permutations / subsets
- constraint satisfaction
- choose candidates recursively
- path must be undone after exploring
- output size itself may be exponential

## INVARIANT

> `path` represents exactly the choices made along the current recursion branch.

After returning:

> Restore state to exactly what it was before making the choice.

Core:

```text
choose
→ recurse
→ unchoose
```

## JAVA SKELETON

```java
void backtrack(int start, List<Integer> path) {
    if (shouldRecord(path)) {
        result.add(new ArrayList<>(path));
    }

    if (shouldStop(path)) {
        return;
    }

    for (int i = start; i < candidates.length; i++) {
        if (!valid(i, path)) {
            continue;
        }

        path.add(candidates[i]);
        backtrack(nextStart(i), path);
        path.remove(path.size() - 1);
    }
}
```

> `shouldRecord` and `shouldStop` are problem-specific. For Subsets, record every state but do not stop immediately.

**COMPLEXITY SHAPE:** Output-dependent exponential time; auxiliary space is recursion depth/state.

## ANCHOR MICRO-CARDS

### A — Subsets

**ROLE:** Primitive choose/skip recursion.

**RECOGNITION:** Generate every subset.

**CORE INVARIANT:** path contains exactly current branch choices and is restored after recursion.

**TRANSFER:** Power set, include/exclude.

### B — Combination Sum

**ROLE:** Canonical reusable-choice anchor.

**RECOGNITION:** Choose candidates to reach target; reuse allowed.

**CORE INVARIANT:** start index prevents permutation duplicates while state tracks remaining target.

**TRANSFER:** Combination variants, reuse vs single-use.

### C — Word Search

**ROLE:** Grid/path backtracking transfer.

**RECOGNITION:** Search one path without reusing a cell.

**CORE INVARIANT:** Visited belongs only to current recursion path and is restored on backtrack.

**TRANSFER:** Constraint/path search.

## COMMON FAILURES

- forgetting undo
- adding the same mutable `path` object to result
- wrong `start` index
- duplicate solutions
- wrong base case
- failure to distinguish reusable vs single-use choices

## VARIATIONS

- subsets
- permutations
- combinations
- reusable candidates
- grid path
- N-Queens
- word generation

## RECALL PHRASE

> **Explore choice tree: choose → recurse → undo.**

---

# CARD 21 — Dynamic Programming

## TRIGGER

Use when:

- optimal value / number of ways / feasibility
- same subproblem repeats
- decision at current state affects future
- brute-force recursion branches repeatedly
- “min / max / count / can”
- overlapping subproblems + optimal substructure

## INVARIANT

Before coding, define:

```text
STATE:
What exactly does dp[...] mean?

CHOICE:
What decisions are available?

RECURRENCE:
How does current state depend on smaller states?

BASE:
What smallest states are already known?
```

If you cannot complete these sentences, do not code yet.

## JAVA SKELETON

1D:

```java
int[] dp = new int[n + 1];

dp[0] = BASE;

for (int i = 1; i <= n; i++) {
    dp[i] = /* transition from earlier states */;
}
```

Take/skip:

```java
for (int i = 0; i < n; i++) {
    int skip = ...;
    int take = ...;

    dp[i] = Math.max(skip, take);
}
```

0/1 subset:

```java
boolean[] dp = new boolean[target + 1];
dp[0] = true;

for (int x : nums) {
    for (int s = target; s >= x; s--) {
        dp[s] = dp[s] || dp[s - x];
    }
}
```

**COMPLEXITY SHAPE:** O(number of states × transitions per state); space is the number of stored states.

## ANCHOR MICRO-CARDS

### A — Climbing Stairs

**ROLE:** Primitive DP state training.

**RECOGNITION:** Ways to reach i depend on smaller solved states.

**CORE INVARIANT:** dp[i] has explicit meaning; recurrence and bases match it.

**TRANSFER:** Fibonacci DP, compression.

### B — House Robber

**ROLE:** Canonical take/skip anchor.

**RECOGNITION:** Choosing current conflicts with adjacent previous.

**CORE INVARIANT:** dp[i] is best through i = max(skip, take + non-conflicting state).

**TRANSFER:** Non-adjacent selection.

### C — Coin Change

**ROLE:** Unbounded minimum-choice transfer.

**RECOGNITION:** Minimum reusable coins to reach amount.

**CORE INVARIANT:** dp[a] is minimum coins for amount a; unreachable states stay sentinel/infinity.

**TRANSFER:** Unbounded knapsack, min-cost construction.

## COMMON FAILURES

- coding before defining state
- wrong iteration direction
- incorrect base case
- mixing 0/1 with unbounded choice
- returning wrong DP state
- using DP where greedy is enough
- memorizing recurrence without understanding meaning

## VARIATIONS

- 1D DP
- take/skip
- unbounded knapsack
- 0/1 knapsack
- grid DP
- sequence DP
- interval DP
- tree DP
- DP + binary search

## RECALL PHRASE

> **STATE → CHOICE → RECURRENCE → BASE.**

---

# CUSTOMER STANDARD — WHAT TO MEMORIZE

For each pattern card, memorize the conceptual spine:

`Trigger → Invariant → Skeleton → Complexity → Failures → Recall Phrase`

For each anchor micro-card, memorize:

`Recognition → Core Invariant → Transfer`

`Role` is orientation only; you do not need to recite it in an interview.

---

# HOW TO MEMORIZE THE A / B / C ANCHORS

Do **not** memorize final code for each anchor.

For every anchor, retrieve in under 20 seconds:

```text
ROLE        Why is this an anchor?
RECOGNITION What clue fires the pattern?
INVARIANT   What must stay true?
TRANSFER    What harder/adjacent problem does it unlock?
```

Then prove ownership by coding from a blank editor:

```text
A = primitive mechanics
B = canonical interview anchor
C = harder transfer / mutation
```

Recognition without blank-editor reconstruction is not ownership.

---

# HIGH-VALUE CONFUSION GUARDS

Memorize these distinctions; they prevent wrong-pattern starts.

| If you are choosing between... | Use this discriminator |
|---|---|
| Sliding Window vs Prefix Sum | Window needs monotonic repair by moving left; arbitrary negatives often break that. Prefix Sum handles exact historical differences. |
| Binary Search vs Binary Search on Answer | Search data directly vs search a candidate answer using a monotonic feasibility predicate. |
| BFS vs Dijkstra | Equal edge cost / fewest edges vs unequal nonnegative edge weights. |
| DFS/BFS vs DSU | Traverse/reach states now vs repeatedly merge/query undirected components. |
| Heap vs Monotonic Deque | General repeated best candidate vs moving-window best where candidates expire by index and domination is permanent. |
| Greedy vs DP | Greedy needs a proof that the local choice cannot hurt the optimal future; otherwise keep competing future states with DP. |
| Backtracking vs DP | Enumerate actual solutions/paths vs cache overlapping subproblems for min/max/count/can. |
| Tree DFS vs Tree BFS | Parent answer from child answers vs level/frontier/nearest-by-edges. |
| Interval Merge vs Sweep Line | Produce merged ranges vs track active count/capacity across ordered boundaries. |
| Topological Sort vs Generic Graph Traversal | Directed prerequisites/order vs ordinary reachability/components. |

---

# OPTIONAL SPECIALIZED CARDS

These are lower-frequency specialized tools. Learn them after the 21 core cards unless your target problems require them earlier.

---

# SPECIAL CARD — Trie

## TRIGGER

- prefix lookup
- dictionary
- autocomplete
- many strings share prefixes
- wildcard word search

## INVARIANT

> The path from root to a node represents exactly one prefix.

## SKELETON

```java
class TrieNode {
    TrieNode[] child = new TrieNode[26]; // lowercase English letters only
    boolean end;
}
```

> Use a map or a larger alphabet structure when the character set is not fixed to `a..z`.

**COMPLEXITY SHAPE:** O(L) per insert/search for word length L; space is proportional to stored characters.

## ANCHORS

```text
A  Implement Trie
B  Add and Search Word
C  Word Search II
```

## RECALL PHRASE

> **Many strings + shared prefixes → Trie.**

---

# SPECIAL CARD — KMP / Prefix Function

## TRIGGER

- substring search
- repeated prefix/suffix
- periodic string
- avoid restarting pattern matching after mismatch

## INVARIANT

> `lps[i]` = length of longest proper prefix of pattern[0..i] that is also a suffix.

**COMPLEXITY SHAPE:** O(n + m) time and O(m) LPS space.

## ANCHORS

```text
A  strStr / Find First Occurrence
B  Repeated Substring Pattern
C  Longest Happy Prefix
```

## RECALL PHRASE

> **Mismatch should reuse already-matched prefix information → KMP.**

---

# SPECIAL CARD — Bit Trie

## TRIGGER

- maximize XOR
- binary-prefix decisions
- choose opposite bit greedily

## INVARIANT

> At each bit, prefer the opposite bit if that prefix exists.

**COMPLEXITY SHAPE:** O(n × B) time and O(n × B) worst-case trie space for B bits.

## ANCHOR

```text
Maximum XOR of Two Numbers in an Array
```

## RECALL PHRASE

> **Max XOR → greedily seek opposite bits in a binary trie.**

---

# ONE-PAGE RECALL INDEX

```text
01 HashMap / Frequency
   Fast memory of what I have already seen.

02 Two Pointers
   One comparison lets me discard one side.

03 Sliding Window
   Contiguous + maintainable constraint.

04 Prefix Sum
   Range = difference of cumulative states.

05 Binary Search
   One test discards half.

06 Binary Search on Answer
   Candidate answer has monotonic feasibility.

07 Intervals / Sweep
   Order boundaries, then reason locally.

08 Greedy
   Prove local choice cannot hurt optimal future.

09 Monotonic Stack
   Nearest boundary where order breaks.

10 Monotonic Deque
   Moving window + repeated best + expiry.

11 Heap / Top-K
   Need next best repeatedly, not a full sort.

12 Linked-List Pointers
   Preserve the remainder before rewiring.

13 Tree DFS
   Define dfs(node) returns ____.

14 Tree BFS
   Frontier / level / nearest.

15 BST
   Tree + sorted-order leverage.

16 Graph DFS/BFS
   Node / edge / visited / start / target.

17 Topological Sort
   Directed prerequisites → indegree zero first.

18 Dijkstra
   Nonnegative weighted shortest path → relax through min-heap.

19 Union-Find / DSU
   Repeated merge/connectivity → representative roots.

20 Backtracking
   Choose → recurse → undo.

21 Dynamic Programming
   STATE → CHOICE → RECURRENCE → BASE.
```

---

# MASTERY TEST FOR EACH CARD

Do not mark a card mastered until you can answer **YES**:

```text
SEE IT?       Can I recognize the trigger?
DERIVE IT?    Can I derive the approach?
STATE IT?     Can I say the invariant?
TYPE IT?      Can I write the skeleton from blank?
TEST IT?      Can I generate edge cases?
DEBUG IT?     Can I recover from a bug?
EXPLAIN IT?   Can I communicate it clearly?
CHANGE IT?    Can I handle a mutation?
RECALL IT?    Can I retrieve it after 7+ days?
DO IT TIMED?  Can I perform under interview time?
```

> **You own a pattern after 3 independent successes on different problems/variants — not after rereading one solution 5 times.**
