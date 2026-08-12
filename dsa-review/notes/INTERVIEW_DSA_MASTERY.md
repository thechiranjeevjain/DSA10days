# Interview DSA Mastery System

Use this as the operating system for turning the existing Java solution chapters into interview performance.

The goal is not to reread 150 solutions. The goal is to build recall, derivation, implementation speed, and confidence on unseen variants.

## Success Definition

You are interview-ready for a pattern when you can do all of this without opening the solution:

1. Recognize the pattern from wording and constraints.
2. State the invariant or state meaning in one sentence.
3. Explain brute force and why it fails.
4. Implement the optimal solution in Java in 15-25 minutes.
5. Dry-run edge cases before the interviewer asks.
6. Adapt to one nearby variant.

If you can only remember the exact old solution, it is not mastered yet.

## ROI Ranking

Priority A means master first. These prevent the most common red flags and cover the highest interview frequency.

| Rank | Pattern | ROI | Difficulty | Why it matters | Repo anchors |
|---:|---|---|---|---|---|
| 1 | HashMap / HashSet / frequency | Very high | Low-medium | Fast wins, common in arrays/strings | `day1`, `day3`, `day7` |
| 2 | Two pointers | Very high | Medium | Shows clean reasoning, avoids brute force | `day1`, `day3`, `day5`, `day4` |
| 3 | Sliding window | Very high | Medium | Many string/subarray questions | `day3` |
| 4 | Tree DFS / BFS | Very high | Medium | Senior candidates are expected to be stable here | `day6` |
| 5 | Graph BFS / DFS | Very high | Medium | Islands, shortest path, visited-state discipline | `day8` |
| 6 | Linked list pointer control | High | Low-medium | Easy to blunder in interviews | `day4` |
| 7 | Binary search and binary search on answer | High | Medium | Tests invariant thinking | `day2` |
| 8 | Stack / monotonic stack | High | Medium | Daily temperatures, histogram, parentheses | `day5` |
| 9 | Heap / top K / two heaps | Medium-high | Medium | Useful in streaming/top-k scheduling problems | `day7` |
| 10 | Intervals / sorting / greedy | Medium-high | Medium | Very common in practical interviews | `day1`, `day3` |
| 11 | Backtracking | Medium | Medium | Combination/permutation/search problems | `day11` |
| 12 | Trie | Medium | Medium | Prefix/search dictionary variants | `day10` |
| 13 | Dynamic programming | Medium | High | Important, but lower ROI until core patterns are stable | `day9`, `day1` |
| 14 | Union Find / topo / Dijkstra | Medium | Medium-high | Needed for selected graph variants | `day8` |
| 15 | KMP / Z / bit / math | Low-medium | Medium-high | Learn after interview core is cached | `day7`, `day10` |
| 16 | LLD design | Role dependent | Medium | Good for 8-year experience interviews, but separate from coding rounds | `design/lld` |

## Mastery Loop

Use this loop for every problem. Do not start by reading the optimal solution.

1. Recall: Write the problem name, pattern, and invariant from memory.
2. Derive: Write brute force, bottleneck, and the optimization idea.
3. Code: Implement from a blank editor.
4. Dry-run: Use one normal case, one edge case, one failure case.
5. Variant: Change one constraint and solve again.
6. Review: Open your Java chapter only after the attempt and mark what failed.

One good active attempt beats three passive rereads.

## Daily Schedule

Use the 90-minute plan when preparing seriously.

| Block | Time | Work |
|---|---:|---|
| RAM warm-up | 10 min | Read `PRE_ZOOM_INTERVIEW_RAM_CACHE.md` pattern triggers |
| Active recall | 20 min | Pick 5 old problems, write pattern + invariant only |
| Blank implementation | 35 min | Code 1 Priority A problem without looking |
| Variant drill | 15 min | Solve a nearby unseen variant or change constraints |
| Error log | 10 min | Record only blunders, not generic notes |

If you only have 30 minutes:

1. Do 5 pattern flash recalls.
2. Code 1 known problem from blank.
3. Dry-run and write the exact failure, if any.

## Weekly Schedule

| Day | Focus |
|---|---|
| Day 1 | HashMap, two pointers, sliding window |
| Day 2 | Binary search, intervals, stack |
| Day 3 | Linked list, tree DFS/BFS |
| Day 4 | Graph BFS/DFS, matrix BFS/DFS |
| Day 5 | Heap, backtracking, trie |
| Day 6 | DP fundamentals and weak problems |
| Day 7 | Mock interview: 2 random problems, no notes |

Repeat the week. Weak problems stay in rotation until they pass blank implementation twice.

## Pattern RAM Cards

### HashMap / HashSet / Frequency

Recognition:
- Need existence, duplicates, counts, first/last index, grouping, complement.
- Input is unsorted and brute force checks pairs/substrings repeatedly.

Invariant:
- The map/set represents exactly the processed prefix or current window.

Template:
1. Decide what key means.
2. Decide what value means.
3. Check answer before or after insertion based on same-element rule.
4. Update count/index.

Common blunders:
- Using same element twice in Two Sum.
- Forgetting to decrement/remove zero counts in window problems.
- Using a mutable array/list as a hash key directly.

### Two Pointers

Recognition:
- Sorted array, pair/triplet, palindrome, container, remove/partition, linked-list slow/fast.

Invariant:
- The answer, if not found yet, remains inside the unprocessed pointer range.

Template:
1. Sort if allowed and useful.
2. Put pointers at both ends or both at start.
3. Move the pointer whose movement can improve the answer.
4. Skip duplicates when output needs unique combinations.

Common blunders:
- Moving both pointers without proof.
- Missing duplicate skip after finding a valid tuple.
- Forgetting that sorting changes original indices.

### Sliding Window

Recognition:
- Contiguous substring/subarray.
- Longest/shortest with a count, sum, distinct, frequency, replacement, or validity condition.

Invariant:
- After the shrink loop, the window is valid.

Template:
1. Expand right and update state.
2. While invalid, remove left and move left.
3. Update answer at the correct time.

Common blunders:
- Updating answer while the window is invalid.
- Using `if` when the window may need multiple shrinks.
- Confusing "at most K" with "exactly K". Often exactly K = atMost(K) - atMost(K - 1).

### Binary Search

Recognition:
- Sorted input, monotonic predicate, first/last occurrence, minimum feasible value, capacity/speed/time.

Invariant:
- The target or answer boundary remains inside the active search space.

Template:
1. Define search space.
2. Define monotonic predicate.
3. Choose closed interval or lower-bound style.
4. Prove which side is discarded.

Common blunders:
- `mid = (left + right) / 2` overflow in general code.
- Infinite loop from not shrinking.
- Binary searching when there is no monotonic property.

### Linked List

Recognition:
- Reverse, cycle, middle, nth from end, merge, reorder, random pointer.

Invariant:
- Every pointer has a named role: previous, current, next, slow, fast, dummy, tail.

Template:
1. Use dummy node when head may change.
2. Save `next` before rewiring.
3. Move pointers in a deliberate order.
4. Return the correct new head.

Common blunders:
- Losing the rest of the list.
- Returning old head after reversal.
- Null pointer in `fast.next.next`.

### Stack / Monotonic Stack

Recognition:
- Parentheses, previous/next greater/smaller, histogram, temperatures, expression evaluation.

Invariant:
- The stack stores unresolved candidates in a useful order.

Template:
1. Decide whether stack stores values or indices.
2. While current resolves stack top, pop and compute.
3. Push current as unresolved.

Common blunders:
- Storing value when index is needed for distance/width.
- Wrong comparison for duplicates.
- Forgetting final cleanup pass.

### Tree DFS

Recognition:
- Path, depth, LCA, validate, subtree, construct, max path, recursion on left/right.

Invariant:
- Define exactly what the recursive function returns upward.

Template:
1. Base case for null.
2. Ask left and right for information.
3. Compute local answer.
4. Return the contract to parent.

Common blunders:
- Mixing global answer with return value.
- Forgetting negative contribution handling in max path.
- Using BST logic on a normal binary tree.

### Tree BFS

Recognition:
- Level order, shortest distance in tree, right side view, burn tree by time, serialize by levels.

Invariant:
- Queue contains nodes at the current or next level.

Template:
1. Offer root.
2. For each level, capture `size`.
3. Process exactly `size` nodes.
4. Add children.

Common blunders:
- Not capturing level size before the loop.
- Adding nulls accidentally unless serialization needs them.

### Graph BFS

Recognition:
- Shortest path in unweighted graph, minimum moves, word ladder, rotten oranges, 01 matrix.

Invariant:
- The first time a state is dequeued/discovered, it has minimum distance.

Template:
1. Build or generate neighbors.
2. Mark visited when enqueuing, not after dequeuing.
3. Process level/distance.
4. Stop when target is found.

Common blunders:
- DFS for shortest path with equal weights.
- Marking visited too late and duplicating states.
- Missing multi-source BFS initialization.

### Graph DFS

Recognition:
- Components, islands, clone graph, cycle detection, topological dependency.

Invariant:
- Visited means this state will never be processed as new again.

Template:
1. Build adjacency or neighbor generator.
2. For each unvisited node/cell, start DFS.
3. Mark before recursing.
4. Carry parent/state if cycle detection needs it.

Common blunders:
- No visited set.
- Incorrect directed-cycle states.
- Mutating grid without understanding whether input can be changed.

### Heap

Recognition:
- Top K, kth largest, merge K, streaming median, scheduling next available.

Invariant:
- Heap top is the next best candidate under the chosen ordering.

Template:
1. Choose min-heap or max-heap.
2. Keep heap size bounded if only K matters.
3. Define comparator carefully.
4. Poll/push according to frontier logic.

Common blunders:
- Reversed comparator.
- Keeping all elements when size K is enough.
- Integer subtraction comparator overflow. Prefer `Integer.compare`.

### Intervals / Greedy

Recognition:
- Meetings, merge, insert, overlap, minimum rooms, arrows, platforms.

Invariant:
- After sorting, all possible conflicts become local or heap-frontier checks.

Template:
1. Sort by start or end based on objective.
2. Merge overlaps or count active intervals.
3. For minimization, prove greedy choice.

Common blunders:
- Wrong sort key.
- Treating touching intervals as overlapping when problem says otherwise.
- Forgetting to append the final merged interval.

### Backtracking

Recognition:
- All combinations/permutations/subsets, choose/explore/undo, board search.

Invariant:
- The current path represents exactly the decisions made so far.

Template:
1. Base case.
2. Loop over candidates.
3. Choose.
4. Recurse.
5. Undo.

Common blunders:
- Missing undo.
- Duplicate output from not sorting/skipping.
- Reusing candidate when only once is allowed.

### Dynamic Programming

Recognition:
- Optimal count/min/max/ways, overlapping subproblems, choices at each index/amount/cell.

Invariant:
- Each `dp[...]` has one fixed meaning for the whole solution.

Template:
1. Define state meaning in English.
2. Define transition from smaller states.
3. Define base cases.
4. Choose top-down or bottom-up.
5. Check iteration order.

Common blunders:
- Starting to code before state meaning is fixed.
- Wrong base case.
- 0/1 vs unbounded knapsack loop direction confusion.

### Trie

Recognition:
- Prefix search, dictionary lookup, wildcard search, many words over characters.

Invariant:
- Each trie node represents a prefix.

Template:
1. Node has children and end flag.
2. Insert follows characters, creating nodes.
3. Search follows characters; wildcard triggers DFS over children.
4. For board search, prune with trie prefixes.

Common blunders:
- Missing end-of-word flag.
- Returning true for prefix when full word is required.
- Duplicate results in Word Search II.

## Interview Solve Protocol

Use this live during the interview.

0-2 minutes:
- Restate problem.
- Ask constraints and input guarantees.
- Confirm examples.

2-5 minutes:
- Give brute force.
- State time complexity.
- Identify why it is too slow.

5-8 minutes:
- Map to a pattern.
- State invariant or DP state.
- Explain optimal idea before coding.

8-22 minutes:
- Code the cleanest version.
- Narrate pointer/state meaning.
- Keep variable names boring and clear.

22-27 minutes:
- Dry-run sample.
- Dry-run edge cases.
- State time and space.

If stuck:
- Say the brute force clearly.
- Identify repeated work.
- Ask whether constraints hint at sorting, hash map, BFS, DP, or binary search.
- Keep talking in terms of invariants, not memory.

## No-Blunder Checklist For Senior Candidates

Avoid these red flags:

- Do not jump straight to code without problem restatement.
- Do not say "I have seen this" as the reason.
- Do not use a pattern without saying why constraints fit it.
- Do not code binary search without stating the search space.
- Do not code DP without saying what `dp[i]` means.
- Do not use DFS for unweighted shortest path.
- Do not ignore null, empty, single-element, duplicate, negative, and overflow cases.
- Do not hide when confused. Convert confusion into a smaller brute force or invariant question.
- Do not over-optimize before correct logic exists.
- Do not stop after code. Dry-run.

## How To Train Unseen Variants

For every mastered problem, create one variant:

- Change return type: length -> actual subarray, boolean -> count.
- Change constraint: at most K -> exactly K, sorted -> unsorted, distinct -> duplicates.
- Change data shape: array -> matrix, tree -> graph, single source -> multi-source.
- Change objective: existence -> minimum, minimum -> number of ways.
- Change update mode: static input -> stream.

If the variant breaks your solution, write the broken assumption. That is the actual learning.

## Scoreboard

Use this grading for each problem in `PROBLEM_PATTERN_INDEX.md`.

| Grade | Meaning |
|---|---|
| 0 | I only recognize the file name |
| 1 | I understand after reading |
| 2 | I can explain the invariant from memory |
| 3 | I can implement from blank |
| 4 | I can solve a nearby variant |
| 5 | I can teach it under pressure |

Target before interviews:

- Priority A: grade 4+
- Priority B: grade 3+
- Priority C: grade 2-3, unless role specifically needs it

## Error Log Format

Do not write long notes. Write failures like this:

```text
Date:
Problem:
Pattern:
Failed because:
Correct invariant:
One-line fix:
Retest date:
```

The goal is to remove repeat mistakes, not to create another large notebook.
