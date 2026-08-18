# DSA 170 — Brain Map

> **Mug-up target:** `signal → pattern → invariant/key move`
>
> **★ Anchor** = know cold. **○ Variation** = map back to anchor.  
> Numbers in `[ ]` preserve the original crunch-time rank.

## 0. Master Retrieval Tree

```text
UNKNOWN PROBLEM
│
├─ contiguous? ─────────────→ Sliding Window / Prefix
├─ sorted / monotonic? ─────→ Binary Search / Two Pointers
├─ pair / opposite ends? ───→ Two Pointers
├─ overlap / scheduling? ───→ Intervals / Greedy / Heap
├─ linked nodes? ────────────→ Pointer Patterns
├─ next greater/smaller? ───→ Monotonic Stack
├─ top-k / streaming best? ─→ Heap
├─ hierarchy? ───────────────→ Tree / BST
├─ connectivity / paths? ───→ Graph
├─ choose possibilities? ───→ Backtracking
├─ repeated optimal state? ─→ DP
├─ prefix words/bits? ───────→ Trie
└─ string border/matching? ─→ KMP
```

# 1. Arrays / Hashing / Basic Scans

## Sum family
- ★ **[1] 2Sum / 3Sum / 4Sum** → `2Sum=hash | 3/4Sum=sort+fix+2ptr | skip duplicates`

## Prefix / product / contribution
- ★ **[4] Product Except Self** → `prefix × suffix | no division`
- ○ **[89] Kadane Max Subarray** → `bestEnd=max(x,bestEnd+x)`
- ○ **[91] Best Time Stock I** → `minSoFar | profit=price-min`
- ○ **[154] Count Unique Chars All Substrings** → `contribution=(i-prev)*(next-i)`

## Counting / frequency
- ★ **[9] Valid Anagram** → `frequency net zero`
- ○ **[55] Ransom Note** → `count supply → spend demand`
- ○ **[111] Longest Palindrome** → `use pairs + at most one odd`
- ○ **[54] Majority Element** → `Boyer-Moore cancel opposites`

## Array partition / simulation
- ★ **[61] Sort Colors** → `Dutch flag: low | unknown | high`
- ○ **[143] Spiral Matrix** → `shrink top/bottom/left/right`
- ○ **[152] Add Binary** → `right→left + carry`
- ○ **[153] Count Primes** → `sieve | mark from p²`

# 2. Two Pointers

- ★ **[10] Valid Palindrome** → `skip junk | compare normalized ends`
- ★ **[12] Container Most Water** → `area=min(hL,hR)*width | move shorter`
- ★ **[13] Trapping Rain Water** → `smaller boundary decides water | track Lmax/Rmax`

# 3. Sliding Window

## Variable window
- ★ **[3] Longest Substring No Repeat** → `unique window | duplicate→shrink`
- ★ **[5] Minimum Window Substring** → `cover need → shrink while valid`
- ○ **[50] Character Replacement** → `valid iff window-maxFreq ≤ k`
- ○ **[51] At Most K Distinct** → `freq map | distinct>k→shrink`
- ○ **[114] Minimum Size Subarray Sum** → `positive sum≥target → shrink/minimize`

## Fixed window
- ★ **[45] Find All Anagrams** → `window size=|p| | frequency match`
- ○ **[52] Permutation In String** → `fixed window | counts match`
- ○ **[62] Concatenation All Words** → `word-sized aligned windows + bounded counts`

## Exact-K via atMost
- ★ **[53] Binary Subarrays With Sum** → `exact(goal)=atMost(goal)-atMost(goal-1)`
- ○ **[113] Nice Subarrays** → `exact k odds = atMost(k)-atMost(k-1)`

# 4. Binary Search

## Classic / boundaries
- ★ **[2] Binary Search** → `sorted + mid → discard impossible half`
- ★ **[22] First/Last Position** → `boundary BS twice`
- ○ **[82] Search Insert** → `first value ≥ target`
- ○ **[87] First Bad Version** → `first TRUE`
- ○ **[129] Sqrt(x)** → `largest mid with mid²≤x`

## Modified search
- ★ **[21] Rotated Sorted Array** → `one half sorted | target in it?`
- ○ **[78] Rotated II** → `L=M=R ambiguous → shrink ends`
- ○ **[86] Find Peak** → `mid<mid+1 → peak exists right`

## Binary search on answer
- ★ **[20] Koko Bananas** → `minimum feasible speed | works(k) monotonic`
- ○ **[88] Split Array Largest Sum** → `min feasible maxSum`
- ○ **[92] Ship Packages D Days** → `min feasible capacity`
- ○ **[93] Bouquets** → `min feasible day | consecutive blooms`

## Binary search inside structure
- ○ **[104] Time Based KV** → `per-key sorted timestamps | latest ≤ query`
- ○ **[90] Job Scheduling** → `sort end + DP + BS previous compatible`
- ○ **[49] LIS** → `tails[len]=smallest tail | lower_bound`

# 5. Intervals / Sweep / Greedy Scheduling

- ★ **[95] Meeting Rooms** → `sort start | overlap previous end?`
- ★ **[31] Meeting Rooms II** → `sort start | min-heap active end times`
- ★ **[37] Min Arrows Balloons** → `sort end | shoot at earliest end`
- ○ **[138] Intervals** → `sort → overlap becomes local`
- ○ **[139] Car Pooling** → `pickup +passengers | drop -passengers | prefix load`

# 6. Linked Lists

## Reverse / local rewiring
- ★ **[6] Reverse Linked List** → `save next → reverse edge → advance`
- ○ **[158] Reverse Linked List II** → `dummy + reverse bounded sublist`
- ○ **[58] Reverse K Group** → `confirm k first → reverse group`
- ○ **[65] Swap Pairs** → `dummy + rewire adjacent pair`

## Fast / slow
- ★ **[7] Linked List Cycle** → `slow1 fast2 → meet iff cycle`
- ○ **[57] Cycle II** → `after meet: head+meet move1 → entry`
- ○ **[94] Middle Linked List** → `slow1 fast2 → slow=middle`

## Alignment / merge
- ★ **[8] Merge Two Sorted Lists** → `dummy tail takes smaller node`
- ○ **[56] Intersection Lists** → `switch heads at null → distances align`
- ○ **[11] Merge K Lists** → `min-heap current heads`

## Structural manipulation
- ○ **[63] Odd Even List** → `build odd/even chains → join`
- ○ **[64] Rotate List** → `circle → break at n-k%n`
- ○ **[24] Copy Random Pointer** → `old→clone map / interleave clones`

## Linked structure design
- ★ **[23] LRU Cache** → `HashMap + DLL | lookup + recency O(1)`
- ○ **[66] First Unique Number** → `counts + ordered candidates`
- ○ **[83] Browser History** → `visit drops forward | back/forward move state`
- ○ **[84] Moving Average** → `queue + running sum`

# 7. Stack / Monotonic Stack / Deque

## Matching / evaluation
- ★ **[33] Valid Parentheses** → `closing matches latest unmatched opening`
- ○ **[108] Evaluate RPN** → `operator pops b,a → push a op b`
- ○ **[110] Basic Calculator** → `running result/sign | stack parenthesis context`

## Monotonic stack
- ★ **[32] Daily Temperatures** → `decreasing indices | warmer resolves stack`
- ★ **[44] Largest Rectangle Histogram** → `shorter bar pops | popped bar gets max width`
- ○ **[106] Next Greater II** → `decreasing stack | traverse 2n circularly`
- ○ **[107] Sum Subarray Minimums** → `each x owns leftChoices×rightChoices`
- ○ **[135] Next Greater I** → `precompute NGE with decreasing stack`
- ○ **[136] Stock Span** → `pop ≤ current | merge spans`
- ○ **[130] Maximal Rectangle** → `each row→histogram → largest rectangle`

## Monotonic deque
- ★ **[96] Sliding Window Maximum** → `decreasing deque indices | front=max`

## Stack/queue design
- ○ **[131] Min Stack** → `store min with each push / second min stack`
- ○ **[132] Max Stack** → `stack order + efficient max locate/remove`
- ○ **[133] Queue Using Stacks** → `in/out stacks | transfer only if out empty`
- ○ **[134] Stack Using Queues** → `push then rotate queue`
- ○ **[147] Stack Increment** → `lazy increment at boundary`
- ○ **[148] Circular Queue** → `head + size + modulo`

# 8. Heap / Top-K / Streaming

- ★ **[36] Top K Frequent** → `frequency map → size-k heap / buckets`
- ★ **[43] Median Stream** → `maxHeap lower | minHeap upper | balance`
- ○ **[102] Kth Largest Array** → `size-k minHeap | top=kth`
- ○ **[103] Kth Largest Stream** → `maintain size-k minHeap per add`
- ○ **[137] K Closest Points** → `keep k smallest squared distances`
- ○ **[101] Task Scheduler** → `max frequency defines cooldown frame`
- ○ **[155] Award Top K Hotels** → `score map → rank/top-k`
- ○ **[156] Sort Chars Frequency** → `count → bucket/heap descending`

# 9. Trees — Generic

## Traversal
- ★ **[67] Inorder** → `left → node → right`
- ○ **[117] Preorder** → `node → left → right`
- ○ **[116] Postorder** → `left → right → node`
- ★ **[14] Level Order** → `BFS | snapshot queue size per level`
- ○ **[60] Right Side View** → `last node each BFS level`

## Bottom-up DFS
- ★ **[26] Balanced Tree** → `postorder height | -1 propagates imbalance`
- ★ **[27] Diameter** → `global=max(Lheight+Rheight) | return height`
- ○ **[79] Maximum Depth** → `1+max(left,right)`
- ★ **[100] Maximum Path Sum** → `return one-side gain | global may split`

## Path state
- ★ **[115] Path Sum** → `remaining -= node | leaf checks zero`
- ○ **[159] Path Sum II** → `path choose→DFS→undo | copy at valid leaf`
- ★ **[28] Path Sum III** → `root-path prefix | count prefix-currentTarget`
- ○ **[70] Sum Root-to-Leaf Numbers** → `value=value*10+node | add at leaf`

## LCA
- ★ **[16] LCA Binary Tree** → `left+right both find target → current split`
- ○ **[160] LCA II** → `same + verify both exist`
- ○ **[161] LCA III** → `parent pointers → ancestor/switch alignment`
- ○ **[162] LCA IV** → `target set | merge target paths`

## Construction / representation
- ★ **[99] Preorder + Inorder Build** → `pre root | inorder splits subtrees`
- ○ **[97] Inorder + Postorder Build** → `post last=root | inorder splits`
- ○ **[77] Serialize/Deserialize** → `traversal + null markers preserve shape`
- ○ **[85] Verify Preorder Serialization** → `slots: start1 | node consumes1 | nonnull +2`
- ○ **[68] Invert Tree** → `swap children at every node`
- ○ **[71] Burn Tree** → `parent links → tree becomes graph → BFS from target`

# 10. BST

- ★ **[15] Validate BST** → `strict ancestor bounds (min,node,max)`
- ★ **[25] Kth Smallest BST** → `inorder sorted → kth visit`
- ★ **[59] LCA BST** → `both smaller→L | both larger→R | else split`
- ○ **[69] BST From Preorder** → `consume preorder under value bounds`
- ○ **[118] Insert BST** → `ordering → walk one branch to null`
- ○ **[119] Min Absolute Difference** → `inorder sorted → adjacent diff`
- ○ **[120] Range Sum BST** → `prune branches outside [low,high]`
- ○ **[121] Search BST** → `compare → only possible branch`
- ○ **[122] Recover BST** → `inorder inversions reveal swapped nodes`
- ○ **[123] BST Iterator** → `stack current left spine | lazy inorder`
- ○ **[124] Greater Tree** → `reverse inorder + running sum`

# 11. Graph / Grid

## Components / flood fill
- ★ **[17] Number of Islands** → `unvisited land → DFS/BFS whole component → count++`
- ○ **[34] Flood Fill** → `traverse only start-color component`
- ○ **[80] Number of Provinces** → `unvisited node starts one component`
- ○ **[127] Max Area Island** → `DFS component → return size`
- ○ **[126] Closed Islands** → `component valid iff never touches border`
- ○ **[128] Coloring Border** → `traverse component | recolor boundary only`

## Reverse / boundary reachability
- ★ **[74] Surrounded Regions** → `border-connected O survive | flip rest`
- ○ **[73] Pacific Atlantic** → `reverse from oceans → move uphill`

## BFS shortest / multi-source
- ★ **[19] Word Ladder** → `unweighted transformations → BFS | first hit shortest`
- ★ **[29] Rotting Oranges** → `all rotten sources → BFS levels=minutes`
- ○ **[30] 01 Matrix** → `all zeros sources → BFS nearest distance`
- ○ **[125] K Highest Ranked Items** → `BFS distance first | tie sort`
- ○ **[76] Minimum Height Trees** → `peel leaves layerwise → 1/2 centroids`

## Coloring
- ★ **[35] Bipartite** → `neighbors must opposite colors | conflict→false`

## Topological
- ★ **[18] Course Schedule II** → `indegree0 queue → remove edges → order`

## Weighted shortest path
- ★ **[72] Network Delay** → `Dijkstra | minHeap next shortest unsettled`

## Clone / identity
- ○ **[81] Clone Graph** → `old→clone map BEFORE neighbors`

## Union Find
- ★ **[75] Accounts Merge** → `shared identity → union components → group by root`

# 12. Backtracking

## Include / exclude
- ★ **[40] Subsets** → `choose/include → recurse → undo / exclude`
- ★ **[41] Combination Sum** → `choose candidate → recurse remaining → undo | reuse allowed`

## Permutations
- ★ **[141] Permutations** → `choose unused → recurse → undo`
- ○ **[163] Permutations II** → `sort + skip duplicate choice at same depth`

## Path search
- ★ **[42] Word Search** → `choose cell → mark → DFS → unmark`
- ○ **[140] Phone Letters** → `one choice per digit → recurse → undo`

# 13. Dynamic Programming

## 1D recurrence
- ★ **[38] House Robber** → `dp[i]=max(skip, rob+dp[i-2])`
- ○ **[150] Climbing Stairs** → `dp[n]=dp[n-1]+dp[n-2]`

## Grid DP
- ★ **[47] Unique Paths** → `dp[r][c]=top+left`

## Knapsack / amount
- ★ **[39] Coin Change** → `dp[a]=min(dp[a],1+dp[a-coin])`
- ★ **[48] Partition Equal Subset** → `target=sum/2 | 0/1 reachable sums`

## Sequence / string DP
- ★ **[151] Edit Distance** → `dp[i][j] prefixes | match diagonal else 1+min(ins,del,replace)`

## Weighted interval DP
- ★ **[90] Job Scheduling** → `sort end | take=profit+dp[compatible] | skip=dp[i-1]`

# 14. Greedy

- ★ **[142] Gas Station** → `total≥0 required | tank<0 → next index candidate`
- ★ **[166] Stock Series II** → `take every positive adjacent rise`
- ○ **[37] Balloons** → `earliest end is safe irreversible choice`
- ○ **[101] Task Scheduler** → `most frequent task constrains frame`

# 15. Trie

- ★ **[46] Implement Trie** → `node=prefix | terminal marks full word`
- ○ **[98] Add/Search Words** → `normal char one edge | '.' branches`
- ★ **[105] Word Search II** → `board DFS + trie prefix pruning`
- ○ **[146] Maximum XOR** → `bit trie | greedily choose opposite bit`
- ○ **[165] Hotel Reviews** → `keyword lookup/trie → score reviews`

# 16. KMP / String Structure

- ★ **[109] First Occurrence / KMP** → `mismatch → fallback using LPS, don't restart`
- ○ **[149] Longest Happy Prefix** → `answer=LPS[n-1]`
- ○ **[145] Repeated Substring Pattern** → `period=n-LPSlast | n%period==0`
- ○ **[157] Shortest Palindrome** → `longest palindromic prefix via s#reverse(s)`
- ○ **[112] Longest Palindromic Substring** → `expand around odd/even centers`
- ○ **[144] Atoi** → `skip spaces → sign → digits → clamp overflow`

# 17. Design / Stateful Structures

- ★ **[164] TinyURL** → `short unique key ↔ long URL mapping`
- ★ **[167] Fraud Pattern Detection** → `identity key + retained events + time-window rule`
- ○ **[168] API Integration** → `contract + timeout + retry + idempotency`
- ★ **[169] Redis** → `KV + TTL metadata + expiry/eviction`
- ★ **[170] Token Bucket** → `refill by elapsed time | request consumes token`
- ○ **[148] Circular Queue** → `fixed array + head/size/modulo`

# 18. Cross-Pattern Problems — Remember These Links

These are valuable because an unseen OA may hide one pattern inside another.

```text
Path Sum III             → Tree DFS + Prefix Sum
Word Search              → Grid DFS + Backtracking
Word Search II           → Trie + Grid Backtracking
Maximal Rectangle        → Matrix + Histogram + Monotonic Stack
Job Scheduling           → Interval Sorting + DP + Binary Search
Merge K Lists            → Linked List + Heap
Meeting Rooms II         → Intervals + Heap
Sliding Window Maximum   → Sliding Window + Monotonic Deque
Burn Binary Tree         → Tree → Graph + BFS
Pacific Atlantic         → Grid Graph + Reverse Reachability
Accounts Merge           → Identity Graph + Union Find
Maximum XOR              → Greedy Bits + Trie
```

# 19. The 20 Invariants to Mug Up

```text
1. Hashing          → store what future lookup needs.
2. Two pointers     → each move safely eliminates candidates.
3. Sliding window   → expand; when invalid, shrink until valid.
4. Prefix           → reuse cumulative state instead of recomputing ranges.
5. Binary search    → predicate/order proves one half impossible.
6. BS on answer     → feasible(x) is monotonic.
7. Intervals        → sort first; overlap/choice becomes local.
8. Linked reversal  → save next before destroying current edge.
9. Fast/slow        → relative speed reveals cycle/middle.
10. Monotonic stack → unresolved candidates remain ordered.
11. Heap            → retain only currently relevant extremes.
12. Tree DFS        → define exactly what recursive call returns.
13. Tree BFS        → queue level = equal distance/depth.
14. BST             → ordering lets you prune one side.
15. Graph DFS/BFS   → mark state when claimed, not repeatedly.
16. Topological     → indegree = unmet dependencies.
17. Dijkstra        → settle smallest known distance next.
18. Backtracking    → choose → explore → undo.
19. DP              → state → transition → base → computation order.
20. Trie/KMP        → reuse shared prefix information.
```

# 20. Anchor Set — Highest Memory ROI

If you want the **smallest set to know cold**, start here:

```text
01  2Sum / 3Sum
02  Binary Search
03  Longest Substring No Repeat
04  Minimum Window Substring
05  Container Most Water
06  Reverse Linked List
07  Linked List Cycle
08  Merge Two Lists
09  LRU Cache
10  Largest Rectangle Histogram
11  Daily Temperatures
12  Meeting Rooms II
13  Koko Bananas
14  Validate BST
15  LCA Binary Tree
16  Diameter Binary Tree
17  Path Sum III
18  Number of Islands
19  Course Schedule II
20  Word Ladder
21  Rotting Oranges
22  Network Delay / Dijkstra
23  Top K Frequent
24  Median Stream
25  Subsets
26  Combination Sum
27  Word Search
28  House Robber
29  Coin Change
30  Partition Equal Subset
31  Unique Paths
32  Trie
33  Word Search II
34  KMP
35  Gas Station
```

Everything else should increasingly feel like a **variation or composition of these mental structures**.

# 21. 10-Second Recall Protocol

For every problem, retrieve only:

```text
SIGNAL?
→ PATTERN?

INVARIANT?
→ what must always remain true?

MOVE?
→ what changes each iteration/recursion?

DONE.
```

Example:

```text
Minimum Window
→ minimum contiguous cover
→ sliding window
→ window contains all required counts
→ expand until valid; shrink while valid
```

Do **not** mug up paragraphs.

Do **not** mug up full code.

Do **not** try to visualize 170 disconnected solutions.

Mug up the **tree + anchors + invariants**, then use the individual problem lines as retrieval hooks.
