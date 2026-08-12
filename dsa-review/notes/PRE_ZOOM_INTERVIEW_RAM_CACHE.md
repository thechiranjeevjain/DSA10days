# Pre-Zoom Interview RAM Cache

Read this 10-15 minutes before a coding interview. Do not deep study here. This is for recall and composure.

## Opening Script

Use this rhythm:

1. "Let me restate the problem."
2. "What are the constraints and edge cases?"
3. "A brute-force way is..."
4. "The bottleneck is..."
5. "This looks like [pattern] because..."
6. "The invariant/state is..."
7. "I'll code that, then dry-run."

This prevents the biggest senior-candidate red flag: silently guessing and coding.

## First 60 Seconds

Ask:

- Is input sorted?
- Are duplicates allowed?
- Can input be empty?
- Need index, value, count, boolean, path, or actual structure?
- Is it contiguous?
- Is it shortest/minimum moves?
- Are edge weights equal?
- Is there a monotonic condition?
- Can I mutate input?
- What are size constraints?

## Pattern Trigger Table

| Wording / constraint | First pattern to try |
|---|---|
| pair, triplet, complement, duplicates, frequency | HashMap / HashSet |
| sorted array, palindrome, pair sum, container | Two pointers |
| contiguous substring/subarray, longest/shortest, at most K | Sliding window |
| sorted, first/last, minimum feasible capacity/speed/time | Binary search |
| next greater/smaller, histogram, temperatures | Monotonic stack |
| parentheses, calculator, RPN | Stack |
| top K, kth, stream, merge K | Heap |
| level order, minimum moves in unweighted graph | BFS |
| components, islands, clone, path existence | DFS |
| prerequisites, ordering, dependencies | Topological sort |
| connected components with merge operations | Union Find |
| prefix dictionary, wildcard word search | Trie |
| all combinations/permutations/subsets | Backtracking |
| min/max/count ways with repeated subproblems | DP |
| meetings, overlap, merge, rooms | Intervals / greedy |

## Invariant Prompts

Say one of these before coding:

- HashMap: "The map stores exactly the processed prefix/current window."
- Sliding window: "After shrinking, the window is valid."
- Two pointers: "The answer still lies inside the remaining pointer range."
- Binary search: "The answer boundary remains inside [left, right]."
- BFS: "First time discovered means minimum distance."
- DFS: "Visited nodes will never be processed as new again."
- Tree DFS: "The helper returns [exact meaning] to its parent."
- DP: "`dp[i]` means [exact meaning], and never changes meaning."
- Backtracking: "Path contains exactly the decisions made so far."
- Heap: "Heap top is always the next best candidate."

## Java Blunder Guard

Before running/submitting, check:

- `left <= right` vs `left < right` in binary search.
- `mid = left + (right - left) / 2`.
- Mark graph/matrix visited when enqueuing.
- Capture BFS `size` before level loop.
- Save linked-list `next` before rewiring.
- Remove zero counts from maps in sliding window.
- Use `long` for sums/products if constraints can overflow int.
- Use `Integer.compare(a, b)` style comparator, not risky subtraction.
- Return new linked-list head, often `dummy.next` or `prev`.
- Handle null root, empty array/string, single element, duplicates.

## If You Freeze

Do this in order:

1. Write brute force.
2. Name the repeated work.
3. Look for one of: hash lookup, sorting, window, monotonicity, BFS level, DP state.
4. State a smaller invariant.
5. Code a correct simpler version if optimal is not clear.

Useful sentence:

"I can solve this brute force first. The repeated work is X, so I am looking for a way to cache/eliminate it."

## Final 2-Minute Dry Run

Always test:

- Empty or null if allowed.
- One element.
- Duplicate values.
- All same values.
- No answer.
- Answer at boundary.
- Negative values.
- Overflow risk.
- Disconnected graph or unreachable target.
- Tree with one side missing.

## Complexity Language

Say it cleanly:

- "Each element enters and leaves the window once, so time is O(n)."
- "Each node is visited once, so time is O(V + E)."
- "The heap size is bounded by k, so time is O(n log k)."
- "Binary search takes O(log range), and each feasibility check is O(n)."
- "DP has [states] states and [transition cost] work per state."

## Last Reminder

Your job is not to remember the exact old solution. Your job is to expose a correct thought process:

brute force -> bottleneck -> pattern -> invariant -> code -> dry run.
