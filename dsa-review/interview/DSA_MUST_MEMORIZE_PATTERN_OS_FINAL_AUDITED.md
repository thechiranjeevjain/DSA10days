# DSA — MUST-MEMORIZE PATTERN OS — FINAL

> **Interview RAM. Nothing here is for completeness.**
>
> Goal: **an unseen problem may be hard, but it should never leave you directionless.**
>
> **Templates are Java-shaped reconstruction spines, not standalone compilable solutions; placeholder names such as `valid()`, `State`, or `hit()` are intentional.**

# 0. THE SIX-STEP RETRIEVAL

```text
TRIGGER
→ PATTERN
→ INVARIANT / FORMULA
→ TINY TEMPLATE
→ FALLBACK
→ OPTIMIZATION LAST
```

```text
ONE canonical route.
ONE fallback.
Do not memorize complete solutions.
```

# 1. NO-FREEZE PROTOCOL

```text
What must I return?
↓
What complexity can the constraints tolerate?
↓
What structure is present?
↓
Pattern clicks?
  YES → invariant → template → adapt.
  MAYBE → compare only the 2 likely patterns.
  PARTLY → keep the first pattern; ask what second structure solves the remaining bottleneck.
  NO → brute force → identify the bottleneck → pattern that removes it.
↓
Still stuck?
→ CODE THE FALLBACK.
```

Never ask only:

```text
"What is the trick?"
```

Ask:

```text
"What can I prove or build next?"
```

# 2. 10-SECOND ROUTER

```text
simple one-pass evolving state        → DIRECT SCAN
count / duplicate / complement       → HASHING
exact range / target sum; negatives often favor PREFIX over WINDOW → PREFIX
sorted pair / opposite ends          → TWO POINTERS
contiguous + monotonic boundary moves → SLIDING WINDOW
one test discards half               → BINARY SEARCH
min/max feasible numeric answer      → BS ON ANSWER
[start,end] / overlaps               → INTERVALS
provably safe local choice           → GREEDY
nested / latest unmatched            → STACK
next/previous greater/smaller        → MONOTONIC STACK
moving window max/min                → MONOTONIC DEQUE
top-k / kth / repeated extreme       → HEAP
reverse / cycle / middle list        → LINKED-LIST POINTERS
parent needs subtree result          → TREE DFS
minimum unweighted steps / levels    → BFS
BST                                  → BST ORDERING
components / connectivity / grid     → GRAPH DFS/BFS
prerequisites / dependency order     → TOPOLOGICAL SORT
nonnegative weighted shortest path   → DIJKSTRA
repeated merge / same component      → DSU
generate all possibilities           → BACKTRACKING
repeated subproblems + optimize/count/feasibility → DP
prefix dictionary                    → TRIE
substring border reuse               → KMP
XOR / masks / bit flags              → BIT MANIPULATION
operations + required complexities  → STATEFUL DATA-STRUCTURE DESIGN
```

# 3. CONFUSION KILLERS

```text
WINDOW vs PREFIX
Window → contiguous + a monotonic rule lets boundaries move only forward.
Prefix → exact range relation; for sum-based windows, negatives often break that monotonicity.

2PTR vs BINARY SEARCH
2PTR → two positions interact.
BS   → one midpoint eliminates a half.

BINARY SEARCH vs BS ON ANSWER
BS   → stored ordered positions/values.
BS-A → numeric answer space + feasible(x).

BFS vs DFS
BFS → shortest unweighted / levels.
DFS → reachability / components.

BFS vs DIJKSTRA
BFS → equal-cost edges.
Dijkstra → standard default for shortest paths with nonnegative weights when edge costs vary.

HEAP vs SORT
Heap → repeated extreme/top-k.
Sort → complete order useful.

STACK vs MONOTONIC STACK
Stack → latest unresolved context.
Mono → unresolved candidates stay ordered.

MONOTONIC STACK vs DEQUE
Stack → nearest boundary.
Deque → moving window + expiry.

GREEDY vs DP
Greedy → local choice has proof.
DP → competing states must stay alive.

BACKTRACKING vs DP
BT → enumerate possibilities.
DP → reuse repeated states to optimize/count.

DSU vs DFS/BFS
DSU → repeated merge/connectivity.
DFS/BFS → actual traversal/path/component.
```

# 4. PATTERN CARDS

## 00 DIRECT SCAN / SIMULATION
`TRIGGER` → one-pass evolving state; parsing; running min/max/count; no stronger structure  
`PATTERN` → Direct scan / state machine  
`INVARIANT` → processed prefix is fully summarized by a small state  
`TEMPLATE`
```java
State s = initial();
for (int i = 0; i < n; i++)
    update(s, a[i]);
return answer(s);
```
`FALLBACK` → straightforward multi-pass / auxiliary-state simulation  
`OPTIMIZATION` → none; do not force a fancy pattern


## 01 HASHING / FREQUENCY
`TRIGGER` → count / duplicate / complement / seen-before / grouping  
`PATTERN` → HashMap / HashSet / frequency array  
`INVARIANT` → map stores exactly what future elements need from processed input  
`TEMPLATE`
```java
for (int i = 0; i < n; i++) {
    int need = target - a[i];
    if (seen.containsKey(need)) return hit(need, i);
    seen.put(a[i], i);
}
```
`FALLBACK` → nested loops; sort + scan when reordering is allowed  
`OPTIMIZATION` → fixed array when key range is small


## 02 PREFIX / PREFIX+MAP
`TRIGGER` → exact range/subarray relation; target sums/counts; especially when negatives break window monotonicity  
`PATTERN` → Prefix accumulation  
`INVARIANT` → `range(l..r)=prefix[r+1]-prefix[l]`; target needs `prefix-target`  
`TEMPLATE`
```java
freq.put(0L, 1);
for (int x : a) {
    prefix += x;
    ans += freq.getOrDefault(prefix-target, 0);
    freq.put(prefix, freq.getOrDefault(prefix,0)+1);
}
```
`FALLBACK` → enumerate ranges / explicit prefix array  
`OPTIMIZATION` → rolling prefix if old values need not be queried


## 03 TWO POINTERS
`TRIGGER` → sorted/orderable pair; opposite ends; one move eliminates candidates  
`PATTERN` → Two pointers  
`INVARIANT` → every pointer move safely discards impossible candidates  
`TEMPLATE`
```java
int l = 0, r = n-1;
while (l < r) {
    if (needLarger(l,r)) l++;
    else r--;
}
```
`FALLBACK` → brute-force candidate pairs / auxiliary lookup structure  
`OPTIMIZATION` → O(1)-space pointer tricks only after movement proof


## 04 SLIDING WINDOW
`TRIGGER` → contiguous + longest/shortest/count + a monotonic rule lets `left` and `right` move only forward  
`PATTERN` → Expand right, repair with left  
`INVARIANT` → boundaries move only forward, discarded prefixes never need to return, and the answer is updated exactly when the active window has the required property  
`TEMPLATE`
```java
// longest / at-most style
for (int r = 0, l = 0; r < n; r++) {
    add(r);
    while (invalid()) remove(l++);
    update(l, r);
}

// minimum-cover / at-least style
for (int r = 0, l = 0; r < n; r++) {
    add(r);
    while (valid()) {
        update(l, r);
        remove(l++);
    }
}
```
`FALLBACK` → enumerate windows  
`OPTIMIZATION` → jump-left / match-count tricks only after base window


## 05 BINARY SEARCH
`TRIGGER` → sorted/monotonic; one test discards half  
`PATTERN` → Binary search  
`INVARIANT` → if answer exists, it stays inside active interval  
`TEMPLATE`
```java
int l = 0, r = n-1;
while (l <= r) {
    int m = l + (r-l)/2;
    if (a[m] == target) return m;
    if (a[m] < target) l = m+1;
    else r = m-1;
}
```
`FALLBACK` → linear scan  
`OPTIMIZATION` → boundary/first-true template only when needed


## 06 BINARY SEARCH ON ANSWER
`TRIGGER` → minimum feasible / maximum feasible; speed/capacity/day/distance  
`PATTERN` → Binary search numeric answer space  
`INVARIANT` → the predicate is monotonic and the bounds contain the answer; the shown minimum-feasible template assumes a `false → true` transition and a feasible upper bound  
`TEMPLATE`
```java
long l = minPossible, r = maxPossible;
while (l < r) {
    long m = l + (r-l)/2;
    if (feasible(m)) r = m;
    else l = m+1;
}
return l;
```
`FALLBACK` → try answers linearly  
`OPTIMIZATION` → template shown is **minimum feasible** (`false → true`); the exact maximum-feasible upper-mid rule is in Section 5


## 07 INTERVALS / SWEEP
`TRIGGER` → `[start,end]`; overlap; meetings; concurrent activity  
`PATTERN` → Sort intervals/boundaries first; then use the local operation the output requires  
`INVARIANT` → sorting makes the next conflict/choice local  
`TEMPLATE`
```java
Arrays.sort(in, Comparator.comparingInt(x -> x[0]));
for (int[] cur : in) {
    if (noOverlap(cur)) add(cur);
    else merge(cur);
}
```
`FALLBACK` → pairwise overlap checks  
`OPTIMIZATION` → heap for active intervals; sweep when only counts matter; decide from the statement whether touching endpoints overlap (`<` vs `<=`)


## 08 GREEDY
`TRIGGER` → irreversible local choice seems globally safe  
`PATTERN` → Greedy + exchange/safety proof  
`INVARIANT` → chosen action preserves at least one optimal solution  
`TEMPLATE`
```java
for (Choice x : usefulOrder(items)) {
    if (safeToCommit(x)) {
        commit(x);
        updateState(x);
    }
}
```
`FALLBACK` → DP/search over choices  
`OPTIMIZATION` → greedy is already the optimization; no proof = no greedy


## 09 STACK
`TRIGGER` → nesting / brackets / expression context / latest unmatched  
`PATTERN` → LIFO stack  
`INVARIANT` → top = most recent unresolved context  
`TEMPLATE`
```java
for (char ch : s.toCharArray()) {
    if (isOpen(ch)) st.push(ch);
    else if (st.isEmpty() || !matches(st.pop(),ch))
        return false;
}
return st.isEmpty();
```
`FALLBACK` → direct simulation  
`OPTIMIZATION` → push expected closer when it simplifies matching


## 10 MONOTONIC STACK
`TRIGGER` → next/previous greater/smaller; nearest boundary; histogram/span  
`PATTERN` → Ordered stack of unresolved indices  
`INVARIANT` → stack indices are unresolved; their corresponding values satisfy the chosen monotonic order; when the pop-condition becomes true, the current boundary permanently resolves the popped index  
`TEMPLATE`
```java
for (int i = 0; i < n; i++) {
    while (!st.isEmpty() && a[i] > a[st.peek()])
        ans[st.pop()] = i;
    st.push(i);
}
```
`FALLBACK` → scan left/right from every index  
`OPTIMIZATION` → histogram/contribution only after pop meaning is clear


## 11 MONOTONIC DEQUE
`TRIGGER` → moving window + repeated max/min + expiry  
`PATTERN` → Deque of useful candidates  
`INVARIANT` → indices are in-window, values monotonic, front = best  
`TEMPLATE`
```java
for (int r = 0; r < n; r++) {
    while (!dq.isEmpty() && dq.peekFirst() <= r-k) dq.pollFirst();
    while (!dq.isEmpty() && a[dq.peekLast()] <= a[r]) dq.pollLast();
    dq.offerLast(r);
    if (r >= k-1) ans[r-k+1] = a[dq.peekFirst()];
}
```
`FALLBACK` → recompute each window / heap  
`OPTIMIZATION` → dual deques when validity uses both max and min


## 12 HEAP / TOP-K
`TRIGGER` → top-k / kth / repeated smallest-largest / streaming extreme  
`PATTERN` → PriorityQueue  
`INVARIANT` → heap root is the only extreme needed; with a size-`k` min-heap, trimming the minimum keeps the `k` largest seen so far  
`TEMPLATE`
```java
PriorityQueue<Integer> pq = new PriorityQueue<>();
for (int x : a) {
    pq.offer(x);
    if (pq.size() > k) pq.poll();
}
return pq.peek(); // kth largest
```
`FALLBACK` → sort everything  
`OPTIMIZATION` → bucket/quickselect only if constraints justify


## 13 LINKED-LIST POINTERS
`TRIGGER` → reverse / reconnect / cycle / middle / intersection  
`PATTERN` → Pointer preservation + rewiring  
`INVARIANT` → never lose the unexplored remainder before changing a link  
`TEMPLATE`
```java
ListNode prev = null, cur = head;
while (cur != null) {
    ListNode next = cur.next;
    cur.next = prev;
    prev = cur;
    cur = next;
}
return prev;
```
`FALLBACK` → array/list/set storage  
`OPTIMIZATION` → dummy for head changes; fast/slow for cycle/middle


## 14 TREE DFS
`TRIGGER` → parent needs facts from subtrees; height/balance/diameter/LCA/path  
`PATTERN` → Recursive return contract  
`INVARIANT` → define exactly `dfs(node) returns ___`; return-to-parent may differ from global answer  
`TEMPLATE`
```java
int dfs(TreeNode n) {
    if (n == null) return BASE;
    int L = dfs(n.left), R = dfs(n.right);
    updateGlobal(L,R,n);
    return parentValue(L,R,n);
}
```
`FALLBACK` → separate traversals / explicit stack  
`OPTIMIZATION` → combine subtree summary + global update in one postorder


## 15 BFS / SHORTEST UNWEIGHTED
`TRIGGER` → minimum steps/edges; nearest; spreading; levels  
`PATTERN` → Breadth-first search  
`INVARIANT` → first claim = shortest unweighted distance; mark visited when enqueueing  
`TEMPLATE`
```java
q.offer(start); visited[start] = true;
while (!q.isEmpty()) {
    State cur = q.poll();
    for (State nx : neighbors(cur))
        if (!visited[nx]) {
            visited[nx] = true; q.offer(nx);
        }
}
```
`FALLBACK` → DFS for reachability only  
`OPTIMIZATION` → multi-source = enqueue all sources initially


## 16 BST
`TRIGGER` → BST search/validate/kth/range/predecessor  
`PATTERN` → Exploit BST ordering / inorder  
`INVARIANT` → for a strict BST: all left values `< node <` all right values; inorder is sorted; validation bounds come from all ancestors  
`TEMPLATE`
```java
boolean valid(TreeNode n, long lo, long hi) {
    if (n == null) return true;
    if (n.val <= lo || n.val >= hi) return false;
    return valid(n.left,lo,n.val)
        && valid(n.right,n.val,hi);
}
```
`FALLBACK` → generic full-tree traversal  
`OPTIMIZATION` → prune impossible subtree immediately


## 17 GRAPH DFS / COMPONENTS
`TRIGGER` → graph/grid reachability; flood fill; undirected component count/size  
`PATTERN` → DFS/BFS traversal  
`INVARIANT` → each claimed state is processed once; for undirected/grid connectivity, each traversal from an unseen node discovers exactly one connected component  
`TEMPLATE`
```java
void dfs(int u) {
    visited[u] = true;
    for (int v : graph.get(u))
        if (!visited[v]) dfs(v);
}
```
`FALLBACK` → BFS instead of DFS  
`OPTIMIZATION` → mark in-place when mutation is allowed


## 18 TOPOLOGICAL SORT
`TRIGGER` → prerequisites / dependencies / build order / directed cycle  
`PATTERN` → Kahn's algorithm  
`INVARIANT` → `indegree` = unmet prerequisites; queue = zero-indegree nodes  
`TEMPLATE`
```java
for (int i=0;i<n;i++) if (indegree[i]==0) q.offer(i);
while (!q.isEmpty()) {
    int u=q.poll(); processed++;
    for (int v:graph.get(u))
        if (--indegree[v]==0) q.offer(v);
}
```
`FALLBACK` → DFS 3-state cycle detection  
`OPTIMIZATION` → none; O(V+E) is canonical


## 19 DIJKSTRA
`TRIGGER` → shortest/minimum cost path with nonnegative edge weights (especially when weights are unequal)  
`PATTERN` → dist[] + min-heap + relaxation  
`INVARIANT` → `dist[v]` = best known; pop smallest current candidate; ignore stale entries  
`TEMPLATE`
```java
PriorityQueue<long[]> pq = new PriorityQueue<>(
    Comparator.comparingLong(x -> x[0]));
Arrays.fill(dist, INF);
dist[start] = 0;
pq.offer(new long[]{0, start});

while (!pq.isEmpty()) {
    long[] cur = pq.poll();
    long d = cur[0]; int u = (int) cur[1];
    if (d != dist[u]) continue;

    for (Edge e : graph.get(u)) {
        long nd = d + e.weight;
        if (nd < dist[e.to]) {
            dist[e.to] = nd;
            pq.offer(new long[]{nd, e.to});
        }
    }
}
```
`FALLBACK` → BFS if equal weights; Bellman-Ford if negatives  
`OPTIMIZATION` → early exit when target is popped with current best


## 20 UNION-FIND / DSU
`TRIGGER` → repeated merges; same-component?; incremental undirected cycle/connectivity  
`PATTERN` → Disjoint Set Union  
`INVARIANT` → `find(x)` = component representative  
`TEMPLATE`
```java
void init(int n) {
    parent = new int[n];
    for (int i=0; i<n; i++) parent[i] = i;
}
int find(int x) {
    if (parent[x] != x) parent[x] = find(parent[x]);
    return parent[x];
}
void union(int a, int b) {
    int ra=find(a), rb=find(b);
    if (ra != rb) parent[rb]=ra;
}
```
`FALLBACK` → graph + DFS/BFS connectivity  
`OPTIMIZATION` → path compression + union by rank/size


## 21 BACKTRACKING
`TRIGGER` → all subsets/combinations/permutations/paths; constraint search  
`PATTERN` → Choose → recurse → undo  
`INVARIANT` → `path` exactly equals current branch; sibling starts from restored state  
`TEMPLATE`
```java
recordIfNeeded(path);
for (Choice c : choices(state)) {
    if (!valid(c, state)) continue;
    choose(c, state, path);
    backtrack(nextState(c, state), path);
    undo(c, state, path);
}
```
The concrete `state` may be `start`, `used[]`, board marks, remaining target, etc.
`FALLBACK` → generate then filter  
`OPTIMIZATION` → prune only with proof; sort before duplicate skipping


## 22 DYNAMIC PROGRAMMING
`TRIGGER` → min/max/count/feasibility + choices + repeated subproblems  
`PATTERN` → STATE → TRANSITION → BASE → ORDER  
`INVARIANT` → each state has one precise meaning; its recurrence depends only on well-defined subproblems, and a solved state is reused instead of recomputed  
`TEMPLATE`
```java
int dfs(State s) {
    if (base(s)) return BASE;
    if (memo.containsKey(s)) return memo.get(s);
    int ans = combine(dfs(next1(s)), dfs(next2(s)));
    memo.put(s, ans);
    return ans;
}
```
`FALLBACK` → plain recursion first  
`OPTIMIZATION` → memo → tabulation → rolling space; never jump ahead


## 23 TRIE
`TRIGGER` → prefix lookup; shared dictionary prefixes; wildcard/prefix pruning  
`PATTERN` → Trie  
`INVARIANT` → each node = exactly one processed prefix; a terminal marker distinguishes a complete word from a mere prefix  
`TEMPLATE`
```java
TrieNode node = root; // template assumes lowercase a-z
for (char ch : word.toCharArray()) {
    int i = ch-'a';
    if (node.child[i] == null) node.child[i] = new TrieNode();
    node = node.child[i];
}
node.end = true;
```
`FALLBACK` → HashSet / scan words  
`OPTIMIZATION` → combine with DFS to prune impossible prefixes


## 24 KMP / LPS
`TRIGGER` → substring matching + prefix=suffix reuse  
`PATTERN` → KMP + LPS prefix function  
`INVARIANT` → mismatch falls back to longest still-valid border  
`TEMPLATE`
```java
if (p.isEmpty()) return 0;
int[] lps = new int[p.length()];

// build lps[] for pattern p
for (int i=1,j=0; i<p.length(); i++) {
    while (j>0 && p.charAt(i)!=p.charAt(j)) j=lps[j-1];
    if (p.charAt(i)==p.charAt(j)) j++;
    lps[i]=j;
}

// match text t against p
for (int i=0,j=0; i<t.length(); i++) {
    while (j>0 && t.charAt(i)!=p.charAt(j)) j=lps[j-1];
    if (t.charAt(i)==p.charAt(j)) {
        j++;
        if (j==p.length()) return i-j+1;
    }
}
return -1;
```
`FALLBACK` → naive matching  
`OPTIMIZATION` → use only when border reuse materially matters


## 25 BIT MANIPULATION
`TRIGGER` → XOR; powers of two; masks; compact Boolean subset/state  
`PATTERN` → Bit operations / bitmask  
`INVARIANT` → each bit stores one Boolean fact; XOR cancels equal pairs  
`TEMPLATE`
```java
boolean on = (mask & (1 << b)) != 0;
mask |=  (1 << b);   // set
mask &= ~(1 << b);   // clear
mask ^=  (1 << b);   // toggle
```
`FALLBACK` → boolean array / HashSet  
`OPTIMIZATION` → bitmask DP only for small state spaces; use `1L << b` when a `long` mask is required


## 26 STATEFUL DATA-STRUCTURE DESIGN
`TRIGGER` → design a class supporting `get/put/add/remove/peek`; stream of operations; explicit O(1)/O(log n) requirements  
`PATTERN` → persistent state + operation invariant + data structures chosen from required complexities  
`INVARIANT` → after every public operation, the stored state exactly represents the abstract data structure and all required ordering/count/expiry relationships remain valid  
`TEMPLATE`
```java
class DS {
    // persistent structures

    Result operation(Input x) {
        updateState(x);
        restoreInvariant();
        return result();
    }
}
```
`FALLBACK` → implement the simplest correct structure first, even if an operation is slower  
`OPTIMIZATION` → identify the slow required operation, then add the structure that makes exactly that operation cheap


# 5. NON-OBVIOUS RULES WORTH MEMORIZING

```text
Two Sum
→ check complement BEFORE inserting current.

Prefix target count
→ freq.put(0,1)
→ use prefix-target
→ then insert current prefix.

Exact K (only when a monotonic atMost-count exists)
→ exactly(K) = atMost(K) - atMost(K-1)

Binary-search boundary (first true, half-open [l,r))
→ initialize l = 0, r = n (n is the no-answer sentinel)
→ if predicate(mid) is true: r = mid
→ else: l = mid + 1
→ return l

Binary-search-on-answer: maximum feasible
(for a `true → false` predicate with a feasible lower bound and bounds containing the answer)
→ use upper mid: mid = l + (r-l+1)/2
→ feasible(mid) ? l = mid : r = mid-1
→ return l


3Sum
→ sort → fix one → 2 pointers
→ skip repeated fixed values and duplicate left/right values at the same choice depth.

Product Except Self
→ answer[i] = product(left of i) * product(right of i)
→ store prefix products in answer, then multiply by one running suffix product.

Fast / slow pointers
→ while (fast != null && fast.next != null):
  slow = slow.next; fast = fast.next.next;
→ middle: slow when fast finishes.
→ cycle: after moving, `slow == fast` means a cycle meeting was found.

Rotated sorted-array search
→ first check `a[m] == target`.
→ without duplicate ambiguity, at every step at least one half is sorted.
→ keep that half only if target lies inside its sorted bounds; otherwise discard it.
→ with duplicates, if a[l] == a[m] == a[r], shrink l++ and r-- before reusing the rule.

LRU with O(1) get/put
→ HashMap: key → node
→ doubly linked list: recency order
→ access/update moves node to MRU end; eviction removes LRU end.

Streaming median
→ max-heap = lower half; min-heap = upper half
→ all lower <= all upper; heap sizes differ by at most 1
→ median comes from one top or the two tops.

Path Sum III
→ Tree DFS + prefix-frequency map on the CURRENT root-to-node path
→ seed `freq[0] = 1`
→ count prefix-target before inserting current prefix for children
→ decrement current prefix count when backtracking.

Trapping Rain Water
→ water[i] = min(maxLeft,maxRight) - height[i]
→ O(1): process the side with the smaller established boundary/max;
  the opposite side is guaranteed not to be the limiting side.

Floyd cycle entry
→ if a cycle meeting is found: one pointer=head; both move 1;
  next meeting=cycle entry.

Tree LCA (when both targets are guaranteed present)
→ dfs returns target/LCA
→ both child returns non-null = current split/LCA.

Diameter / Max Path
→ helper returns ONE extendable branch
→ global answer may combine BOTH.

BFS
→ mark visited when ENQUEUEING.
→ multi-source = enqueue ALL sources initially.
→ for levels/minutes: capture `size = queue.size()` before the inner loop;
  processing exactly that many nodes = one BFS layer.

Topological
→ processed < V after Kahn = directed cycle.

Dijkstra
→ if (d != dist[u]) continue;   // stale

Histogram pop
→ after pop: left boundary = new stack top, or -1 if stack is empty
→ width = currentIndex - leftBoundary - 1

Backtracking duplicates
→ sort first.
→ combination/subset-style loop:
  if (i > start && a[i] == a[i-1]) continue;
  same value + same depth = skip.
→ permutation-style with used[]:
  if (i > 0 && a[i]==a[i-1] && !used[i-1]) continue;

1D knapsack compression (items in the outer loop):
0/1 item used once → capacity BACKWARD
unbounded reuse    → capacity FORWARD

Power of two
→ x > 0 && (x & (x-1)) == 0

XOR
→ x ^ x = 0
→ x ^ 0 = x
```

# 6. JAVA SYNTAX — AUTOMATIC

```java
map.getOrDefault(key, 0);
map.containsKey(key);

Deque<Integer> stack = new ArrayDeque<>();
stack.push(x); stack.pop(); stack.peek();

Queue<Integer> queue = new ArrayDeque<>();
queue.offer(x); queue.poll(); queue.peek();

PriorityQueue<Integer> minHeap = new PriorityQueue<>();
PriorityQueue<Integer> maxHeap =
        new PriorityQueue<>(Comparator.reverseOrder());

Arrays.sort(nums);
Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

int mid = left + (right - left) / 2;
```

```text
STACK → PUSH / POP / PEEK
QUEUE → OFFER / POLL / PEEK
```

Use `long` for large cumulative sums, products, and distances; cast before multiplication when needed, e.g. `1L * mid * mid`.

# 7. BLANK-BRAIN RECONSTRUCTION

```text
1. Simplest correct solution?
2. What exactly makes it slow or hard?
3. Which pattern removes that bottleneck?
4. State the invariant.
5. Write the pattern spine.
6. Replace only problem-specific:
   valid / feasible / neighbors / transition / comparator / base.
7. Still stuck? CODE THE FALLBACK.
```

```text
repeated lookup            → HASHING
exact/repeated range work   → PREFIX
contiguous monotonic range  → SLIDING WINDOW
ordered elimination        → 2PTR / BINARY SEARCH
nearest ordered boundary   → MONOTONIC STACK
repeated window extreme    → MONOTONIC DEQUE
repeated best/extreme      → HEAP
subtree-derived facts      → TREE DFS
repeated decision states    → DP
repeated reachability      → DFS/BFS
repeated component merges  → DSU
required operation costs    → STATEFUL DESIGN
```

# 8. FINAL 10-SECOND CHECK

```text
TRIGGER?
PATTERN?
INVARIANT?
TEMPLATE?
TWIST?
FALLBACK?
```

Then:

```text
CODE
→ dry-run one normal case + one dangerous edge/boundary
→ state TIME + SPACE
```

# 9. DO NOT MEMORIZE

```text
complete solutions
170 brute-force explanations
170 dry runs
every edge case
multiple equivalent implementations
every optimization
full Java files
long proofs
```

Memorize only:

```text
TRIGGERS
PATTERNS
INVARIANTS / FORMULAS
TINY CODE SPINES
ONE FALLBACK
A FEW NON-OBVIOUS RULES
```
