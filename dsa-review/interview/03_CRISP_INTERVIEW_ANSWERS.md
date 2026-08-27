# Crisp Interview Answers

Practice speaking these in the interview rhythm.

~~~text
brute force -> bottleneck -> pattern -> invariant -> code -> dry run
~~~

## Phase 1 - No Red Flags

Ranks 1-30. Remove common interview red flags first.

### 1. Two Sum

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session2/Three3Sum2Sum.java) | [LeetCode](https://leetcode.com/problems/two-sum/)
- Brute force: Scan repeatedly or compare every candidate pair/count directly.
- Bottleneck: Brute force tries all pairs; complement lookup makes the second value O(1).
- Pattern: HashMap / Frequency / Set, using Two pointers / hash.
- Invariant/state: Use a HashMap from value to index; each number asks whether its complement was seen.
- Code idea: Scan left to right, if target - nums[i] exists return indices, otherwise store nums[i] -> i.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 2. Binary Search

- Links: [Java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) | [LeetCode](https://leetcode.com/problems/binary-search/)
- Brute force: Linearly scan the sorted array for the target.
- Bottleneck: Linear scan is O(n); sorted order gives monotonic elimination in O(log n).
- Pattern: Binary Search / Answer Search, using Binary search invariant.
- Invariant/state: Sorted order plus mid comparison proves which half cannot contain the target.
- Code idea: While left <= right, compare nums[mid] to target; move left/right, return index or -1.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 3. Longest Substring Without Repeating Characters

- Links: [Java](../../src/main/java/org/chijai/day3/session1/LongestSubString.java) | [LeetCode](https://leetcode.com/problems/longest-substring-without-repeating-characters/)
- Brute force: Enumerate every substring/subarray and recompute validity from scratch.
- Bottleneck: Restarting at every duplicate loses useful overlap; a set/map keeps current window valid.
- Pattern: Sliding Window, using Sliding window / set.
- Invariant/state: Window must contain unique chars; move left past duplicates.
- Code idea: Expand right, while duplicate exists remove left, then update max.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 4. Product Of Array Except Self

- Links: [Java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/ProductOfArrayExceptSelf.java) | [LeetCode](https://leetcode.com/problems/product-of-array-except-self/)
- Brute force: For every index or query, recompute the needed range/product/sum directly.
- Bottleneck: For each index recomputing products is O(n^2); prefix/suffix accumulates in two passes.
- Pattern: Prefix Sum / Prefix-Suffix, using Prefix/suffix.
- Invariant/state: Answer is product of everything left times everything right, no division needed.
- Code idea: Fill answer with left products, then multiply by running right product from the end.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 5. Minimum Window Substring

- Links: [Java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LeetCode](https://leetcode.com/problems/minimum-window-substring/)
- Brute force: Enumerate every substring/subarray and recompute validity from scratch.
- Bottleneck: Checking every substring repeats frequency validation; need/have counts update incrementally.
- Pattern: Sliding Window, using Sliding window / need-have.
- Invariant/state: Expand until all needed chars are covered, then shrink while still valid.
- Code idea: Build need map, update have on right, while have == needCount update best and remove left.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 6. Reverse Linked List

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session1/ReverseLinkedList.java) | [LeetCode](https://leetcode.com/problems/reverse-linked-list/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Stack/list copy is extra memory; three pointers reverse in place.
- Pattern: Linked List Pointers, using Pointer reversal.
- Invariant/state: Reverse one edge at a time after saving next.
- Code idea: Keep prev, curr, next; curr.next = prev; advance; return prev.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 7. Linked List Cycle

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session1/LinkedListCycle.java) | [LeetCode](https://leetcode.com/problems/linked-list-cycle/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: HashSet detects repeats with memory; Floyd uses speed difference in O(1) space.
- Pattern: Linked List Pointers, using Fast/slow pointers.
- Invariant/state: Slow and fast meet only if a cycle exists.
- Code idea: Move slow one, fast two while fast and fast.next exist; meeting means cycle.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 8. Merge Two Sorted Lists

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session4/Merge2SortedLists.java) | [LeetCode](https://leetcode.com/problems/merge-two-sorted-lists/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Creating an array loses list structure; merge pointers preserve nodes in one pass.
- Pattern: Linked List Pointers, using Merge / dummy node.
- Invariant/state: Dummy tail repeatedly takes the smaller current node.
- Code idea: Compare l1/l2, append smaller to tail, advance, then attach remainder.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 9. Valid Anagram

- Links: [Java](../../src/main/java/org/chijai/day3/session3/ValidAnagram.java) | [LeetCode](https://leetcode.com/problems/valid-anagram/)
- Brute force: Scan repeatedly or compare every candidate pair/count directly.
- Bottleneck: Sorting works but costs O(n log n); frequency counts compare in linear time.
- Pattern: HashMap / Frequency / Set, using Frequency count.
- Invariant/state: Two strings are anagrams when every character count nets to zero.
- Code idea: Reject different lengths, increment for s and decrement for t, then verify all counts zero.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 10. Valid Palindrome

- Links: [Java](../../src/main/java/org/chijai/day3/session3/ValidPalindrome.java) | [LeetCode](https://leetcode.com/problems/valid-palindrome/)
- Brute force: Try all pairs, all boundaries, or build an auxiliary cleaned structure.
- Bottleneck: Building a cleaned string is extra space; two pointers validate in place.
- Pattern: Two Pointers, using Two pointers.
- Invariant/state: Skip non-alphanumeric chars and compare normalized ends while pointers move inward.
- Code idea: Advance left/right past invalid chars, compare lowercase chars, stop when pointers cross.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 11. Merge K Sorted Lists

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session4/MergeKSortedLists.java) | [LeetCode](https://leetcode.com/problems/merge-k-sorted-lists/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Repeatedly scanning k heads costs O(kN); heap reduces selection to O(log k).
- Pattern: Linked List Pointers, using Heap / divide and conquer.
- Invariant/state: A min-heap stores the current smallest head among k lists.
- Code idea: Push non-null heads, poll min, append it, push its next.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 12. Two Sum II - Input Array Is Sorted

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session2/Three3Sum2Sum.java) | [LeetCode](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/)
- Brute force: Try all pairs, all boundaries, or build an auxiliary cleaned structure.
- Bottleneck: HashMap works, but sorted order gives O(1) space by eliminating impossible pairs.
- Pattern: Two Pointers, using Two pointers / hash.
- Invariant/state: Sorted input lets left/right shrink toward the target sum.
- Code idea: Compare nums[left] + nums[right] with target; move left if small, right if large.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 13. Container With Most Water

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/ContainerWithMostWater.java) | [LeetCode](https://leetcode.com/problems/container-with-most-water/)
- Brute force: Try all pairs, all boundaries, or build an auxiliary cleaned structure.
- Bottleneck: Brute force checks all pairs; moving taller side cannot improve the limiting height.
- Pattern: Two Pointers, using Two pointers.
- Invariant/state: Area is limited by shorter wall, so move the shorter side inward.
- Code idea: Compute area at left/right, update max, move pointer with smaller height.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 14. Trapping Rain Water

- Links: [Java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/TrappingRainwater.java) | [LeetCode](https://leetcode.com/problems/trapping-rain-water/)
- Brute force: Try all pairs, all boundaries, or build an auxiliary cleaned structure.
- Bottleneck: Brute force rescans left/right max for each index; two pointers maintain both maxima.
- Pattern: Two Pointers, using Two pointers / stack.
- Invariant/state: Water at a side depends on the smaller max boundary seen so far.
- Code idea: Move the side with lower height, update max, add max-height when bounded.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 15. Binary Tree Level Order Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeTraversal.java) | [LeetCode](https://leetcode.com/problems/binary-tree-level-order-traversal/)
- Brute force: Traverse without preserving levels, then reconstruct level/view information afterward.
- Bottleneck: Naive queue loop loses level boundaries; size snapshot preserves grouping.
- Pattern: Tree BFS / Level Order, using Tree traversal.
- Invariant/state: Capture queue size to process exactly one tree level at a time.
- Code idea: For each level, poll size nodes, collect values, enqueue children.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 16. Validate Binary Search Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) | [LeetCode](https://leetcode.com/problems/validate-binary-search-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Checking only parent-child misses ancestor violations.
- Pattern: Tree DFS / Recursion, using Tree DFS / stack.
- Invariant/state: Every node must stay inside strict min/max bounds inherited from ancestors.
- Code idea: DFS with low/high bounds, reject value <= low or >= high, recurse tightened bounds.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 17. Lowest Common Ancestor Of A Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) | [LeetCode](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Paths can be found separately, but DFS return contract finds LCA in one pass.
- Pattern: Tree DFS / Recursion, using Tree DFS return contract.
- Invariant/state: If left and right both return a target, current node is the split point.
- Code idea: Return node if null/p/q; ask left/right; if both non-null return root else non-null side.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 18. Number Of Islands

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LeetCode](https://leetcode.com/problems/number-of-islands/)
- Brute force: Start a fresh traversal for every cell/node without reusable visited/component state.
- Bottleneck: Without visited marking, the same land cells get counted repeatedly.
- Pattern: Graph DFS / Components, using Matrix DFS/BFS components.
- Invariant/state: Every time you find unvisited land, sink its whole connected component and count one island.
- Code idea: Scan grid; on '1', increment count and DFS/BFS four directions marking visited/water.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 19. Course Schedule

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) | [LeetCode](https://leetcode.com/problems/course-schedule/)
- Brute force: Repeatedly scan all courses to find one whose prerequisites are already completed.
- Bottleneck: Plain traversal can process a course before prerequisites; indegree is the remaining-lock count.
- Pattern: Topological Sort, using Topological sort / cycle.
- Invariant/state: A course is unlocked only when its indegree becomes zero.
- Code idea: Build prerequisite->course graph, queue indegree-zero courses, decrement neighbors, compare processed count.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 20. Course Schedule II

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) | [LeetCode](https://leetcode.com/problems/course-schedule-ii/)
- Brute force: Repeatedly scan all courses to append one whose prerequisites are already completed.
- Bottleneck: Plain traversal can violate prerequisites; indegree tracks the remaining unmet prerequisites.
- Pattern: Topological Sort, using Topological sort / cycle.
- Invariant/state: A course enters the order only when its indegree drops to zero.
- Code idea: Build prerequisite->course graph, queue indegree-zero courses, append order, fail if processed < n.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 21. Word Ladder

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session3/WordLadder.java) | [LeetCode](https://leetcode.com/problems/word-ladder/)
- Brute force: Run a separate search from each source or use DFS and then compare path lengths.
- Bottleneck: DFS may find a longer path first; all transformations cost one step.
- Pattern: Graph BFS / Shortest Path, using BFS shortest path.
- Invariant/state: BFS words level by level; first time reaching endWord is the shortest transformation length.
- Code idea: Queue begin word, generate one-letter mutations, visit dictionary words once per level.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 22. Koko Eating Bananas

- Links: [Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | [LeetCode](https://leetcode.com/problems/koko-eating-bananas/)
- Brute force: Try every speed from 1 to max pile and simulate total eating hours.
- Bottleneck: Trying every speed up to maxPile is too slow; feasibility is monotonic.
- Pattern: Binary Search / Answer Search, using Binary search on answer.
- Invariant/state: Binary search the minimum speed; if speed k works, every higher speed also works.
- Code idea: Search speed 1..maxPile, compute total ceil(pile/speed) hours, keep smaller working speed.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 23. Search In Rotated Sorted Array

- Links: [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LeetCode](https://leetcode.com/problems/search-in-rotated-sorted-array/)
- Brute force: Scan every index because the pivot breaks global sorted order.
- Bottleneck: A normal sorted-array binary search fails because the pivot breaks global ordering.
- Pattern: Binary Search / Answer Search, using Binary search boundary.
- Invariant/state: At every step one half is sorted; keep it only if target lies inside its bounds.
- Code idea: Compare nums[left] and nums[mid] to identify sorted half, then discard the half that cannot contain target.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 24. Find First And Last Position Of Element In Sorted Array

- Links: [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LeetCode](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/)
- Brute force: Scan the array once and record the first and last target positions.
- Bottleneck: Finding one target then expanding can become O(n) when all elements equal target.
- Pattern: Binary Search / Answer Search, using Binary search boundary.
- Invariant/state: Run boundary binary search twice: first index >= target, and last index <= target.
- Code idea: findFirst moves left on nums[mid] >= target; findLast moves right on nums[mid] <= target.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 25. LRU Cache

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) | [LeetCode](https://leetcode.com/problems/lru-cache/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: A plain map cannot evict least-recently-used; a list gives O(1) move/remove.
- Pattern: Linked List Pointers, using HashMap + doubly linked list.
- Invariant/state: HashMap gives O(1) lookup; doubly linked list keeps recency order.
- Code idea: On get/put move node to front; if over capacity remove tail and map entry.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 26. Copy List With Random Pointer

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/CopyListWithRandomPointer.java) | [LeetCode](https://leetcode.com/problems/copy-list-with-random-pointer/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Random pointers prevent simple one-pass copy; a map preserves identity mapping.
- Pattern: Linked List Pointers, using HashMap / interleaving copy.
- Invariant/state: Clone nodes then connect next/random using old-to-new mapping or interleaving.
- Code idea: First create clones in map, second assign clone.next and clone.random from mapped nodes.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 27. Kth Smallest Element In A BST

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session3/KthSmallestElementInBST.java) | [LeetCode](https://leetcode.com/problems/kth-smallest-element-in-a-bst/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Heap/sort is unnecessary because BST already encodes order.
- Pattern: Tree DFS / Recursion, using BST inorder.
- Invariant/state: BST inorder gives ascending values; kth visited is the answer.
- Code idea: Iterative inorder with stack, decrement k on visit, return when k hits zero.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 28. Balanced Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session3/BinaryTree.java) | [LeetCode](https://leetcode.com/problems/balanced-binary-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Computing height repeatedly causes O(n^2); postorder height does it once.
- Pattern: Tree DFS / Recursion, using Core tree patterns.
- Invariant/state: Return height, but use -1 or flag to propagate unbalanced subtrees early.
- Code idea: DFS left/right heights, if either -1 or diff > 1 return -1 else max+1.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 29. Diameter Of Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session3/BinaryTree.java) | [LeetCode](https://leetcode.com/problems/diameter-of-binary-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Global answer differs from helper return value.
- Pattern: Tree DFS / Recursion, using Core tree patterns.
- Invariant/state: Diameter through a node is left height plus right height; return height upward.
- Code idea: Postorder compute heights, update max diameter with left+right, return max height+1.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 30. Path Sum Iii

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) | [LeetCode](https://leetcode.com/problems/path-sum-iii/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Brute force restarts DFS at every node; prefix sums reuse ancestor sums.
- Pattern: Tree DFS / Recursion, using Tree path DFS / global answer.
- Invariant/state: Use prefix sums on the root-to-current path to count paths ending at this node.
- Code idea: DFS with running sum, add count[sum-target], increment before children, decrement on backtrack.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## Phase 2 - Strong Core

Ranks 31-70. High-frequency core patterns after the first pass is stable.

### 31. Rotting Oranges

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/RottenOranges.java) | [LeetCode](https://leetcode.com/problems/rotting-oranges/)
- Brute force: Run a separate search from each source or use DFS and then compare path lengths.
- Bottleneck: Starting BFS separately repeats infection work and gives wrong simultaneous timing.
- Pattern: Graph BFS / Shortest Path, using Multi-source BFS.
- Invariant/state: All initially rotten oranges start a multi-source BFS; each level is one minute.
- Code idea: Queue all rotten cells, count fresh, process BFS levels, decrement fresh on infection.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 32. 01 Matrix

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Matrix01.java) | [LeetCode](https://leetcode.com/problems/01-matrix/)
- Brute force: Run a separate search from each source or use DFS and then compare path lengths.
- Bottleneck: Running BFS from every one repeats work; multi-source BFS expands all shortest distances together.
- Pattern: Graph BFS / Shortest Path, using Multi-source BFS.
- Invariant/state: Start BFS from all zero cells; first visit gives nearest-zero distance.
- Code idea: Queue every zero with distance 0, then relax unvisited neighbors to dist+1.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 33. House Robber

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session1/HouseRobber.java) | [LeetCode](https://leetcode.com/problems/house-robber/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats suffix decisions; two rolling states capture all history needed.
- Pattern: Dynamic Programming, using 1D DP.
- Invariant/state: At each house choose max(skip current, rob current plus best before previous).
- Code idea: For each money, next = max(prev1, prev2 + money); shift prev2=prev1, prev1=next.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 34. Coin Change

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) | [LeetCode](https://leetcode.com/problems/coin-change/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Recursive choice tree repeats the same remaining amounts.
- Pattern: Dynamic Programming, using Unbounded knapsack DP.
- Invariant/state: dp[amount] is the fewest coins needed; each coin relaxes reachable amounts.
- Code idea: Initialize dp[0]=0 and others INF; for amount 1..target, try every coin.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 35. Subsets

- Links: [Java](../../src/main/java/org/chijai/day11/backtracking/session1/Subsets.java) | [LeetCode](https://leetcode.com/problems/subsets/)
- Brute force: Generate all possible candidates first, then filter invalid answers at the end.
- Bottleneck: Brute force generates blindly; backtracking prunes invalid decision paths early.
- Pattern: Backtracking / Combinatorial DFS, using Backtracking subsets.
- Invariant/state: Choose, recurse, undo; the path is exactly the current decision state.
- Code idea: Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 36. Valid Parentheses

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session3/ValidParentheses.java) | [LeetCode](https://leetcode.com/problems/valid-parentheses/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Counting brackets is not enough because nesting order matters.
- Pattern: Stack / Monotonic Stack, using Stack.
- Invariant/state: Every closing bracket must match the most recent unmatched opening bracket.
- Code idea: Push opening brackets; on closing, fail if stack empty or top is not its matching opener.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 37. Top K Frequent Elements

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) | [LeetCode](https://leetcode.com/problems/top-k-frequent-elements/)
- Brute force: Sort all candidates every time a top, kth, median, or next-best item is needed.
- Bottleneck: Sorting all unique values works but costs more than keeping a size-k heap or buckets.
- Pattern: Heap / Priority Queue, using Frequency + heap/bucket.
- Invariant/state: Count frequencies, then keep only the k highest-frequency entries.
- Code idea: Build frequency map, then use bucket lists by frequency or a min-heap of size k.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 38. Daily Temperatures

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/DailyTemperatures.java) | [LeetCode](https://leetcode.com/problems/daily-temperatures/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Scanning forward from every day is O(n^2); a decreasing stack resolves each day once.
- Pattern: Stack / Monotonic Stack, using Monotonic stack.
- Invariant/state: Keep indices of days waiting for a warmer temperature; current day resolves colder previous days.
- Code idea: While current temp is warmer than stack top, pop index and set answer to current - popped.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 39. Meeting Rooms Ii

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/MinimumPlatforms.java) | [LeetCode](https://leetcode.com/problems/meeting-rooms-ii/)
- Brute force: Compare every interval with every other interval before deciding conflicts/order.
- Bottleneck: Need the earliest finishing active meeting to decide whether a room can be reused.
- Pattern: Intervals / Sorting Greedy, using Intervals / sorting.
- Invariant/state: Sort meetings by start; a min-heap of end times counts active rooms.
- Code idea: Sort intervals, pop heap while end <= start, push current end, track max heap size.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 40. Implement Trie (Prefix Tree)

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) | [LeetCode](https://leetcode.com/problems/implement-trie-prefix-tree/)
- Brute force: Compare each word/prefix character-by-character against every dictionary entry.
- Bottleneck: HashSet handles exact lookup, but prefix queries need shared character paths.
- Pattern: Trie, using Trie.
- Invariant/state: Each trie node represents one prefix; terminal marks distinguish full words from prefixes.
- Code idea: For insert/search/startsWith, walk chars through children; create on insert, fail on missing child.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 41. Flood Fill

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/FloodFill.java) | [LeetCode](https://leetcode.com/problems/flood-fill/)
- Brute force: Start a fresh traversal for every cell/node without reusable visited/component state.
- Bottleneck: Blind DFS can recolor wrong regions or loop when new color equals old color.
- Pattern: Graph DFS / Components, using Matrix DFS/BFS.
- Invariant/state: Recolor only the connected component matching the starting color.
- Code idea: If oldColor == newColor return; DFS/BFS neighbors with oldColor and recolor them.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 42. Is Graph Bipartite?

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) | [LeetCode](https://leetcode.com/problems/is-graph-bipartite/)
- Brute force: Start a fresh traversal for every cell/node without reusable visited/component state.
- Bottleneck: Visited alone is insufficient; conflicts appear when an edge sees same-color endpoints.
- Pattern: Graph DFS / Components, using BFS/DFS coloring.
- Invariant/state: A graph is bipartite if every edge connects opposite colors.
- Code idea: For each uncolored node, BFS/DFS assign colors and fail on same-color neighbor.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 43. Minimum Number Of Arrows To Burst Balloons

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/MinimumPlatforms.java) | [LeetCode](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/)
- Brute force: Try arrow positions or compare balloon overlaps pair by pair.
- Bottleneck: This is greedy endpoint selection, not overlap counting like meeting rooms.
- Pattern: Intervals / Sorting Greedy, using Intervals / sorting.
- Invariant/state: Sort balloons by end; shoot at current end and start a new arrow only after it is missed.
- Code idea: Sort by end, keep currentArrowEnd, increment arrows when next.start > currentArrowEnd.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 44. Unique Paths

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session1/UniquePaths.java) | [LeetCode](https://leetcode.com/problems/unique-paths/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion recomputes the same grid cells exponentially.
- Pattern: Dynamic Programming, using Grid DP.
- Invariant/state: Ways to a cell equal ways from top plus ways from left.
- Code idea: Initialize first row/column to 1, fill dp[r][c] = dp[r-1][c] + dp[r][c-1].
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 45. Partition Equal Subset Sum

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/PartitionEqualSubsetSum.java) | [LeetCode](https://leetcode.com/problems/partition-equal-subset-sum/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Trying all subsets repeats sums; 0/1 knapsack tracks reachable sums once.
- Pattern: Dynamic Programming, using 0/1 knapsack DP.
- Invariant/state: Partition is possible only if some subset reaches total/2.
- Code idea: If total odd return false; update boolean dp from target down to num for each num.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 46. Longest Increasing Subsequence

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) | [LeetCode](https://leetcode.com/problems/longest-increasing-subsequence/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: O(n^2) DP works, but binary-search tails gives faster length tracking.
- Pattern: Dynamic Programming, using DP / patience sorting.
- Invariant/state: tails[len] stores the smallest possible tail for an increasing subsequence of that length.
- Code idea: For each x, lower_bound in tails and replace; answer is tails size.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 47. Longest Repeating Character Replacement

- Links: [Java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LeetCode](https://leetcode.com/problems/longest-repeating-character-replacement/)
- Brute force: Enumerate every substring/subarray and recompute validity from scratch.
- Bottleneck: Trying every target char wastes work; max frequency tells replacement cost.
- Pattern: Sliding Window, using Sliding window / need-have.
- Invariant/state: Window is valid when size - maxFreq <= k replacements.
- Code idea: Track counts and maxFreq, shrink when windowLen - maxFreq > k, update best.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 48. Combination Sum

- Links: [Java](../../src/main/java/org/chijai/day11/backtracking/session1/CombinationSum.java) | [LeetCode](https://leetcode.com/problems/combination-sum/)
- Brute force: Generate all possible candidates first, then filter invalid answers at the end.
- Bottleneck: Brute force generates blindly; backtracking prunes invalid decision paths early.
- Pattern: Backtracking / Combinatorial DFS, using Backtracking reuse.
- Invariant/state: Choose, recurse, undo; the path is exactly the current decision state.
- Code idea: Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 49. Longest Substring With At Most K Distinct Characters

- Links: [Java](../../src/main/java/org/chijai/day3/session1/AtMostKDistinct.java) | [LeetCode](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/)
- Brute force: Enumerate every substring/subarray and recompute validity from scratch.
- Bottleneck: All substrings repeat counting; sliding window updates counts as boundaries move once.
- Pattern: Sliding Window, using Sliding window.
- Invariant/state: Keep a frequency map with at most k distinct chars; shrink until valid.
- Code idea: Expand right count, while distinct > k decrement/remove left, update max length.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 50. Word Search

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/WordSearch.java) | [LeetCode](https://leetcode.com/problems/word-search/)
- Brute force: Generate all possible candidates first, then filter invalid answers at the end.
- Bottleneck: Brute force generates blindly; backtracking prunes invalid decision paths early.
- Pattern: Backtracking / Combinatorial DFS, using DFS backtracking.
- Invariant/state: Choose, recurse, undo; the path is exactly the current decision state.
- Code idea: Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 51. Permutation In String

- Links: [Java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LeetCode](https://leetcode.com/problems/permutation-in-string/)
- Brute force: Enumerate every substring/subarray and recompute validity from scratch.
- Bottleneck: Sorting every window is too slow; maintain counts as window slides.
- Pattern: Sliding Window, using Sliding window / need-have.
- Invariant/state: A fixed-size window is a permutation when its frequency counts match the target.
- Code idea: Track counts/matches for window length s1, slide one char in and one char out.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 52. Find Median From Data Stream

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/Median.java) | [LeetCode](https://leetcode.com/problems/find-median-from-data-stream/)
- Brute force: Sort all candidates every time a top, kth, median, or next-best item is needed.
- Bottleneck: Sorting the stream after every insert is too slow.
- Pattern: Heap / Priority Queue, using Two heaps.
- Invariant/state: Two heaps split lower and upper halves; median comes from heap tops.
- Code idea: Push into maxHeap/minHeap, rebalance sizes, median is top or average of tops.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 53. Binary Subarrays With Sum

- Links: [Java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/NiceSubArrays.java) | [LeetCode](https://leetcode.com/problems/binary-subarrays-with-sum/)
- Brute force: For every index or query, recompute the needed range/product/sum directly.
- Bottleneck: Brute force sums all ranges; binary nonnegative values let the window count at-most sums.
- Pattern: Prefix Sum / Prefix-Suffix, using Prefix/window counting.
- Invariant/state: For binary arrays, exact goal count can be atMost(goal) - atMost(goal-1).
- Code idea: Implement atMost(sum): expand right, shrink while sum > goal, add window length.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 54. Largest Rectangle in Histogram

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java) | [LeetCode](https://leetcode.com/problems/largest-rectangle-in-histogram/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Trying every left/right boundary is O(n^2); monotonic stack finds nearest smaller bars.
- Pattern: Stack / Monotonic Stack, using Monotonic stack.
- Invariant/state: When a shorter bar arrives, popped bars know their maximal rectangle width.
- Code idea: Append sentinel zero, keep increasing indices, pop and compute height * width when current is smaller.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 55. Majority Element

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session2/MajorityElement.java)
- Brute force: Scan repeatedly or compare every candidate pair/count directly.
- Bottleneck: Counting uses O(n) space; majority > n/2 lets pair cancellation preserve the answer.
- Pattern: HashMap / Frequency / Set, using Boyer-Moore / frequency.
- Invariant/state: Boyer-Moore cancels different values; surviving candidate is majority after optional verification.
- Code idea: Track candidate and count; reset at zero, increment on match, decrement otherwise.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 56. Find All Anagrams In A String

- Links: [Java](../../src/main/java/org/chijai/day3/session3/FindAllAnagramsInAString.java) | [LeetCode](https://leetcode.com/problems/find-all-anagrams-in-a-string/)
- Brute force: Enumerate every substring/subarray and recompute validity from scratch.
- Bottleneck: Sorting each candidate window is expensive; update char counts by one in/out.
- Pattern: Sliding Window, using Sliding window frequency.
- Invariant/state: Slide a fixed-size frequency window and record starts where counts match p.
- Code idea: Maintain difference counts or match count across a window of length p.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 57. Ransom Note

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session1/RansomNote.java) | [LeetCode](https://leetcode.com/problems/ransom-note/)
- Brute force: Scan repeatedly or compare every candidate pair/count directly.
- Bottleneck: Brute force repeatedly searches magazine; counting turns every char check into O(1).
- Pattern: HashMap / Frequency / Set, using HashMap/frequency.
- Invariant/state: Count magazine chars, then spend counts for ransom; fail when a needed char is missing.
- Code idea: Build int[26] or map from magazine, decrement while scanning ransomNote, return false below zero.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 58. Intersection Of Two Linked Lists

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session1/Intersection.java) | [LeetCode](https://leetcode.com/problems/intersection-of-two-linked-lists/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: HashSet works but costs space; pointer switching aligns the remaining distances.
- Pattern: Linked List Pointers, using Linked list two pointers.
- Invariant/state: Switch heads at null; equal path lengths make pointers meet at intersection or null.
- Code idea: Move a and b one step; when null redirect to other head; return when a == b.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 59. Linked List Cycle Ii

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session4/LinkedListCycleII.java) | [LeetCode](https://leetcode.com/problems/linked-list-cycle-ii/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Cycle existence is not enough; Floyd distance math locates the entry in O(1) space.
- Pattern: Linked List Pointers, using Floyd cycle entry.
- Invariant/state: After slow/fast meet, move one pointer from head and both one step to find entry.
- Code idea: Detect meeting, reset one pointer to head, move both until equal.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 60. Reverse Nodes in k-Group

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) | [LeetCode](https://leetcode.com/problems/reverse-nodes-in-k-group/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Blind reversal corrupts final short group; group boundary check preserves list.
- Pattern: Linked List Pointers, using Linked-list reversal groups.
- Invariant/state: Only reverse a group after confirming k nodes exist.
- Code idea: Use dummy/groupPrev, locate kth, reverse group, reconnect, advance groupPrev.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 61. Lowest Common Ancestor Of A Binary Search Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | [LeetCode](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: BST ordering turns LCA into one directed walk instead of full DFS.
- Pattern: Tree DFS / Recursion, using BST property.
- Invariant/state: If both targets are smaller go left, if both are larger go right, else current node is the split.
- Code idea: Loop from root; compare p and q to node.val and move left/right until they diverge.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 62. Binary Tree Right Side View

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeSideView.java) | [LeetCode](https://leetcode.com/problems/binary-tree-right-side-view/)
- Brute force: Traverse without preserving levels, then reconstruct level/view information afterward.
- Bottleneck: DFS can work, but level BFS directly exposes the rightmost node per depth.
- Pattern: Tree BFS / Level Order, using Tree BFS / DFS.
- Invariant/state: The last node seen at each BFS level is visible from the right.
- Code idea: For each level size, process nodes and record value when i == size - 1.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 63. Sort Colors

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session1/SortColors.java) | [LeetCode](https://leetcode.com/problems/sort-colors/)
- Brute force: Try all pairs, all boundaries, or build an auxiliary cleaned structure.
- Bottleneck: Sorting is overkill for three values; partitioning maintains regions in one pass.
- Pattern: Two Pointers, using Partition / Dutch flag.
- Invariant/state: Dutch flag keeps < pivot, unknown, and > pivot regions with three pointers.
- Code idea: Use low, mid, high; swap 0 to low, 2 to high, advance mid on 1.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 64. Substring With Concatenation Of All Words

- Links: [Java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LeetCode](https://leetcode.com/problems/substring-with-concatenation-of-all-words/)
- Brute force: Enumerate every substring/subarray and recompute validity from scratch.
- Bottleneck: Trying every substring repeats tokenization; fixed word length gives aligned sliding windows.
- Pattern: Sliding Window, using Sliding window / need-have.
- Invariant/state: Scan word-sized windows by offset and keep word counts bounded by need.
- Code idea: For each offset, move in wordLen steps, count words, shrink when a word is overused.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 65. Odd Even Linked List

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) | [LeetCode](https://leetcode.com/problems/odd-even-linked-list/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Array grouping is extra space; pointers can preserve relative order in place.
- Pattern: Linked List Pointers, using Linked-list reversal groups.
- Invariant/state: Keep odd and even chains separately, then attach even head after odd tail.
- Code idea: Move odd to even.next and even to odd.next until even chain ends, then connect.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 66. Rotate List

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) | [LeetCode](https://leetcode.com/problems/rotate-list/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Repeated single rotations are too slow; length gives the final split directly.
- Pattern: Linked List Pointers, using Linked-list reversal groups.
- Invariant/state: Make the list circular, then break at length - k % length.
- Code idea: Count length and tail, connect tail to head, move to new tail, break circle.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 67. Swap Nodes In Pairs

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) | [LeetCode](https://leetcode.com/problems/swap-nodes-in-pairs/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Value swap is not always allowed; pointer swap preserves nodes.
- Pattern: Linked List Pointers, using Linked-list reversal groups.
- Invariant/state: Dummy node lets you swap each adjacent pair without special-casing head.
- Code idea: For each pair, rewire prev->second, first->second.next, second->first.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 68. First Unique Number

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) | [LeetCode](https://leetcode.com/problems/first-unique-number/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Scanning every query is slow; counts plus ordered candidates make showFirstUnique cheap.
- Pattern: Linked List Pointers, using HashMap + doubly linked list.
- Invariant/state: Queue/list stores arrival order; counts decide whether the front is still unique.
- Code idea: On add update count and queue/list, while front count > 1 pop it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 69. Binary Tree Inorder Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) | [LeetCode](https://leetcode.com/problems/binary-tree-inorder-traversal/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Recursive or stack both follow the same left-spine invariant.
- Pattern: Tree DFS / Recursion, using Tree DFS / stack.
- Invariant/state: Inorder is left, node, right; for BST it yields sorted order.
- Code idea: Push left chain, pop node, visit, then go right.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 70. Invert Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session3/InvertBinaryTree.java) | [LeetCode](https://leetcode.com/problems/invert-binary-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: The operation is local and identical for all subtrees.
- Pattern: Tree DFS / Recursion, using Tree DFS/BFS.
- Invariant/state: Swap left and right at every node.
- Code idea: DFS or BFS each node, swap children, continue.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## Phase 3 - Important

Ranks 71-110. Important breadth once the core signal is reliable.

### 71. Construct Binary Search Tree From Preorder Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) | [LeetCode](https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Searching split points repeatedly is slower; bounds consume preorder once.
- Pattern: Tree DFS / Recursion, using Tree recursion / hashmap index.
- Invariant/state: Preorder root plus BST bounds tells where each next value belongs.
- Code idea: Use index over preorder and recursive upper/lower bounds to build nodes.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 72. Sum Root To Leaf Numbers

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) | [LeetCode](https://leetcode.com/problems/sum-root-to-leaf-numbers/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: The state is the path value, not the full path list.
- Pattern: Tree DFS / Recursion, using Tree path DFS / global answer.
- Invariant/state: Carry the number formed so far; at a leaf, add it to the total.
- Code idea: DFS with value = value * 10 + node.val; return value at leaves, sum children otherwise.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 73. Network Delay Time

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/NetworkDelayTime.java) | [LeetCode](https://leetcode.com/problems/network-delay-time/)
- Brute force: Run a separate search from each source or use DFS and then compare path lengths.
- Bottleneck: Unweighted BFS is not valid with weighted edges; heap order settles shortest distances.
- Pattern: Graph BFS / Shortest Path, using Dijkstra / graph.
- Invariant/state: Dijkstra keeps the next shortest unsettled node in a min-heap.
- Code idea: Build adjacency, push source distance 0, relax neighbors when a smaller distance is found.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 74. Pacific Atlantic Water Flow

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LeetCode](https://leetcode.com/problems/pacific-atlantic-water-flow/)
- Brute force: Start a fresh traversal for every cell/node without reusable visited/component state.
- Bottleneck: DFS from every cell to both oceans repeats huge overlap.
- Pattern: Graph DFS / Components, using Matrix DFS/BFS components.
- Invariant/state: Reverse the flow: start from both oceans and move to equal-or-higher neighboring cells.
- Code idea: Mark cells reachable from Pacific border and Atlantic border; answer intersection.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 75. Surrounded Regions

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LeetCode](https://leetcode.com/problems/surrounded-regions/)
- Brute force: Start a fresh traversal for every cell/node without reusable visited/component state.
- Bottleneck: Flipping every O before knowing border reachability captures safe regions incorrectly.
- Pattern: Graph DFS / Components, using Matrix DFS/BFS components.
- Invariant/state: Only O-regions connected to the border survive; all other O cells are captured.
- Code idea: DFS/BFS border O cells as safe, flip remaining O to X, restore safe marks.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 76. Accounts Merge

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session3/AccountsMerge.java) | [LeetCode](https://leetcode.com/problems/accounts-merge/)
- Brute force: Run DFS/BFS connectivity checks after every merge/query.
- Bottleneck: Repeated graph searches are expensive; union-find maintains components incrementally.
- Pattern: Union Find / DSU, using Union Find / graph.
- Invariant/state: Represent components with parent links; union merges and failed union detects cycles.
- Code idea: Initialize parent/rank, find with compression, union by rank/size.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 77. Minimum Height Trees

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session3/MinHTree.java) | [LeetCode](https://leetcode.com/problems/minimum-height-trees/)
- Brute force: Root the tree at every node and compute its height.
- Bottleneck: Trying every root is O(n^2); leaves can never be optimal centers after each layer.
- Pattern: Topological Sort, using Topological trimming.
- Invariant/state: Peel all current leaves together until one or two centroid roots remain.
- Code idea: Build graph/degrees, queue degree-1 leaves, remove layers while remainingNodes > 2.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 78. Serialize And Deserialize Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/SerializeAndDeserializeBinaryTree.java) | [LeetCode](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Values alone lose missing-child positions; null markers preserve shape.
- Pattern: Tree DFS / Recursion, using Tree BFS/DFS serialization.
- Invariant/state: Include null markers so structure can be reconstructed unambiguously.
- Code idea: Preorder/BFS serialize with # for null; deserialize by consuming tokens in same order.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 79. Search In Rotated Sorted Array Ii

- Links: [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LeetCode](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/)
- Brute force: Scan every index, especially when duplicates hide sorted-half information.
- Bottleneck: Duplicates can destroy the sorted-half signal, so worst-case time can degrade to O(n).
- Pattern: Binary Search / Answer Search, using Binary search boundary.
- Invariant/state: With duplicates, shrink both ends only when left, mid, and right are equal and ordering is ambiguous.
- Code idea: If nums[left]==nums[mid]==nums[right], left++ and right--; otherwise reuse sorted-half logic.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 80. Maximum Depth Of Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session3/BinaryTree.java) | [LeetCode](https://leetcode.com/problems/maximum-depth-of-binary-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Each subtree depth is independent and computed once.
- Pattern: Tree DFS / Recursion, using Core tree patterns.
- Invariant/state: Depth is one plus the deeper child depth.
- Code idea: Return 0 for null, else 1 + max(depth(left), depth(right)).
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 81. Number Of Provinces

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LeetCode](https://leetcode.com/problems/number-of-provinces/)
- Brute force: Run a separate search from each source or use DFS and then compare path lengths.
- Bottleneck: Checking pairs repeatedly is unnecessary once a city's component is visited.
- Pattern: Graph BFS / Shortest Path, using Matrix DFS/BFS components.
- Invariant/state: Each DFS/BFS from an unvisited city marks one connected province.
- Code idea: Scan cities; when unvisited, count province and traverse connected cities from adjacency matrix.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 82. Clone Graph

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/CloneGraph.java) | [LeetCode](https://leetcode.com/problems/clone-graph/)
- Brute force: Start a fresh traversal for every cell/node without reusable visited/component state.
- Bottleneck: Naive recursive copy loops on cycles and duplicates shared nodes.
- Pattern: Graph DFS / Components, using Graph DFS/BFS clone.
- Invariant/state: Map original node to cloned node before cloning neighbors to handle cycles.
- Code idea: DFS/BFS: create clone if absent, then connect cloned neighbors from the map.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 83. Search Insert Position

- Links: [Java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) | [LeetCode](https://leetcode.com/problems/search-insert-position/)
- Brute force: Scan from left until finding the first value greater than or equal to target.
- Bottleneck: Equality is a boundary candidate, not a reason to abandon the left side.
- Pattern: Binary Search / Answer Search, using Binary search invariant.
- Invariant/state: Find the first index whose value is >= target; if none, insert at n.
- Code idea: Binary search with answer initialized to n; when nums[mid] >= target save mid and move right left.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 84. Design Browser History

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) | [LeetCode](https://leetcode.com/problems/design-browser-history/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Arrays are simple but pointer/list model makes state transitions explicit.
- Pattern: Linked List Pointers, using HashMap + doubly linked list.
- Invariant/state: Back/forward are pointer moves over a history chain; visit drops forward history.
- Code idea: Maintain current node; visit creates current.next and clears forward branch.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 85. Moving Average From Data Stream

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) | [LeetCode](https://leetcode.com/problems/moving-average-from-data-stream/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Recomputing average scans the window; running sum updates in O(1).
- Pattern: Linked List Pointers, using HashMap + doubly linked list.
- Invariant/state: Queue last size values and running sum; average is sum divided by queue size.
- Code idea: Offer val, add to sum, if queue too large poll and subtract, return sum/count.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 86. Verify Preorder Serialization Of A Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) | [LeetCode](https://leetcode.com/problems/verify-preorder-serialization-of-a-binary-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Building the tree is unnecessary; valid serialization preserves slot balance.
- Pattern: Tree DFS / Recursion, using Tree recursion / hashmap index.
- Invariant/state: Slots start at one; every node consumes a slot, non-null nodes create two.
- Code idea: For each token decrement slots, fail below zero, add two slots if token is not #.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 87. Find Peak Element

- Links: [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LeetCode](https://leetcode.com/problems/find-peak-element/)
- Brute force: Check every index and compare it with neighbors to find a peak.
- Bottleneck: Binary search does not require sorted data, only a safe half-discard rule.
- Pattern: Binary Search / Answer Search, using Binary search boundary.
- Invariant/state: Compare mid with mid+1; the rising side must contain a peak.
- Code idea: If nums[mid] > nums[mid+1], move right to mid; else move left to mid+1 until left == right.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 88. First Bad Version

- Links: [Java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) | [LeetCode](https://leetcode.com/problems/first-bad-version/)
- Brute force: Call isBadVersion from version 1 upward until the first bad version appears.
- Bottleneck: Checking versions one by one wastes the monotonic bad suffix.
- Pattern: Binary Search / Answer Search, using Binary search invariant.
- Invariant/state: Find the first true in a false...false,true...true version predicate.
- Code idea: If isBadVersion(mid), save mid and search left; otherwise search right.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 89. Split Array Largest Sum

- Links: [Java](../../src/main/java/org/chijai/day2/session2/AGGRCOW.java) | [LeetCode](https://leetcode.com/problems/split-array-largest-sum/)
- Brute force: Try possible max sums or enumerate contiguous partitions directly.
- Bottleneck: The feasibility check is monotonic: larger max sum never needs more pieces.
- Pattern: Binary Search / Answer Search, using Binary search on answer.
- Invariant/state: Binary search the smallest allowed subarray sum that can split into at most m pieces.
- Code idea: Search max(nums)..sum(nums), greedily count pieces when current sum would exceed mid.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 90. Kadane Max Sub Array

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session1/KadaneMaxSubArray.java)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Checking all subarrays is O(n^2); local ending-best captures the only needed history.
- Pattern: Dynamic Programming, using Kadane / DP.
- Invariant/state: Best subarray ending here is either current alone or previous best ending here plus current.
- Code idea: cur = max(x, cur + x); best = max(best, cur) for every element.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 91. Maximum Profit In Job Scheduling

- Links: [Java](../../src/main/java/org/chijai/day2/session3/MaximumProfitInJobScheduling.java) | [LeetCode](https://leetcode.com/problems/maximum-profit-in-job-scheduling/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Trying all subsets repeats compatibility checks; DP plus sorted end times reuses optimal prefixes.
- Pattern: Dynamic Programming, using DP + binary search.
- Invariant/state: Sort jobs by end time; dp[i] is best profit up to i, with binary search for compatible previous job.
- Code idea: Sort by end, for each job compute max(skip, profit + dp[lastNonOverlapping]).
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 92. Best Time to Buy and Sell Stock

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java) | [LeetCode](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Trying all buy/sell pairs repeats the same prefix minimum search.
- Pattern: Dynamic Programming, using Greedy / DP states.
- Invariant/state: Track the lowest price so far; today's profit is price minus that minimum.
- Code idea: For each price, update minPrice, then best = max(best, price - minPrice).
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 93. Capacity To Ship Packages Within D Days

- Links: [Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | [LeetCode](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/)
- Brute force: Try every capacity from max weight to total weight and simulate shipping days.
- Bottleneck: Capacity must be at least max weight, and larger capacity never requires more days.
- Pattern: Binary Search / Answer Search, using Binary search on answer.
- Invariant/state: Binary search minimum capacity; capacity works if one pass ships within D days.
- Code idea: Search maxWeight..sumWeight, count days by accumulating load until capacity would overflow.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 94. Minimum Number Of Days To Make M Bouquets

- Links: [Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | [LeetCode](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/)
- Brute force: Try days linearly and count how many adjacent bouquets can be made.
- Bottleneck: Day feasibility is monotonic, but adjacency resets the current flower streak.
- Pattern: Binary Search / Answer Search, using Binary search on answer.
- Invariant/state: Binary search days; by a given day, consecutive bloomed flowers form bouquets greedily.
- Code idea: Reject if m*k > n; for each day mid, count adjacent bloomed streaks of length k.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 95. Middle Of The Linked List

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session4/MiddleOfLinkedList.java) | [LeetCode](https://leetcode.com/problems/middle-of-the-linked-list/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Counting length needs two passes; fast/slow finds middle in one pass.
- Pattern: Linked List Pointers, using Fast/slow pointers.
- Invariant/state: Fast moves twice as fast; slow lands at the middle when fast finishes.
- Code idea: While fast and fast.next exist, move slow one and fast two, return slow.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 96. Sliding Window Maximum

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) | [LeetCode](https://leetcode.com/problems/sliding-window-maximum/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Recomputing max for each window is O(nk); the deque removes dominated elements once.
- Pattern: Stack / Monotonic Stack, using Stack/queue design.
- Invariant/state: A decreasing deque stores candidate indices; front is always the current window maximum.
- Code idea: Drop out-of-window front, pop smaller/equal from back, push index, read front after first window.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 97. Construct Binary Tree From Inorder And Postorder Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) | [LeetCode](https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Linear search for root each time is slow; map inorder value to index.
- Pattern: Tree DFS / Recursion, using Tree recursion / hashmap index.
- Invariant/state: Postorder last is root; inorder index splits left and right subtrees.
- Code idea: Pop root from postorder end, build right then left using inorder bounds.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 98. Design Add and Search Words Data Structure

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) | [LeetCode](https://leetcode.com/problems/design-add-and-search-words-data-structure/)
- Brute force: Compare each word/prefix character-by-character against every dictionary entry.
- Bottleneck: Wildcard lookup cannot be solved by one HashSet lookup; branching is limited by trie prefixes.
- Pattern: Trie, using Trie.
- Invariant/state: Trie search branches only on '.', otherwise it follows exactly one child.
- Code idea: DFS over trie and word index; on '.', try every child, otherwise follow the matching child.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 99. Construct Binary Tree From Preorder And Inorder Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) | [LeetCode](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: The two traversals define structure when values are unique.
- Pattern: Tree DFS / Recursion, using Tree recursion / hashmap index.
- Invariant/state: Preorder first is root; inorder index splits left and right subtrees.
- Code idea: Read preorder index, split by inorder map, recursively build left and right ranges.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 100. Binary Tree Maximum Path Sum

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) | [LeetCode](https://leetcode.com/problems/binary-tree-maximum-path-sum/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Return value and global maximum are different concepts.
- Pattern: Tree DFS / Recursion, using Tree path DFS / global answer.
- Invariant/state: Helper returns best non-splitting gain; global answer may split through node.
- Code idea: Clamp child gains at zero, update global with node+left+right, return node+max(left,right).
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 101. Task Scheduler

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/TaskScheduler.java) | [LeetCode](https://leetcode.com/problems/task-scheduler/)
- Brute force: Sort all candidates every time a top, kth, median, or next-best item is needed.
- Bottleneck: Simulating every schedule is unnecessary; max frequency defines the minimum frame.
- Pattern: Heap / Priority Queue, using Greedy / heap.
- Invariant/state: CPU idles only when the most frequent tasks cannot be spaced by cooldown gaps.
- Code idea: Use maxFreq and countMax: max(tasks.length, (maxFreq-1)*(n+1)+countMax).
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 102. Kth Largest Element In An Array

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) | [LeetCode](https://leetcode.com/problems/kth-largest-element-in-an-array/)
- Brute force: Sort all candidates every time a top, kth, median, or next-best item is needed.
- Bottleneck: Full sorting is O(n log n) when only one order statistic is needed.
- Pattern: Heap / Priority Queue, using Min-heap size K.
- Invariant/state: A size-k min-heap keeps the k largest seen so far; top is kth largest.
- Code idea: Push each number, pop when heap size > k, return heap top.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 103. Kth Largest Element In A Stream

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) | [LeetCode](https://leetcode.com/problems/kth-largest-element-in-a-stream/)
- Brute force: Sort all candidates every time a top, kth, median, or next-best item is needed.
- Bottleneck: Resorting all stream values after every add is too slow.
- Pattern: Heap / Priority Queue, using Min-heap size K.
- Invariant/state: Maintain a size-k min-heap after every add; top is the kth largest in the stream.
- Code idea: On add, push value, trim heap to k, return heap.peek().
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 104. Time Based Key Value Store

- Links: [Java](../../src/main/java/org/chijai/day2/session3/TimeBasedKeyValueStore.java) | [LeetCode](https://leetcode.com/problems/time-based-key-value-store/)
- Brute force: Linearly test candidates or scan the full sorted/search range.
- Bottleneck: Scanning history on every get is slow; timestamps are monotonic per key.
- Pattern: Binary Search / Answer Search, using HashMap + binary search.
- Invariant/state: Map each key to timestamped values in order; binary search finds latest timestamp <= query.
- Code idea: Append on set; on get binary search the key's list for rightmost timestamp <= target.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 105. Word Search Ii

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/WordSearchII.java) | [LeetCode](https://leetcode.com/problems/word-search-ii/)
- Brute force: Compare each word/prefix character-by-character against every dictionary entry.
- Bottleneck: Running Word Search for every word repeats prefix work; trie shares the dictionary search.
- Pattern: Trie, using Trie + backtracking.
- Invariant/state: Trie prunes dictionary prefixes while board DFS chooses, marks, explores, and unmarks cells.
- Code idea: Build trie, DFS board paths, stop when prefix missing, collect terminal words, mark cells in-place.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 106. Next Greater Element Ii

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/NextGreaterElement.java) | [LeetCode](https://leetcode.com/problems/next-greater-element-ii/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Naive circular scans repeat work; stack resolves each index when the next greater appears.
- Pattern: Stack / Monotonic Stack, using Monotonic stack.
- Invariant/state: Loop twice over the circular array while a decreasing stack waits for next greater values.
- Code idea: For i in 0..2n-1, resolve stack with nums[i % n], push i only during first pass.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 107. Sum of Subarray Minimums

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java) | [LeetCode](https://leetcode.com/problems/sum-of-subarray-minimums/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Enumerating subarrays is O(n^2); monotonic stacks count ownership ranges in O(n).
- Pattern: Stack / Monotonic Stack, using Monotonic stack.
- Invariant/state: Each element contributes as minimum for leftChoices times rightChoices subarrays.
- Code idea: Find previous less and next less-or-equal distances, sum arr[i] * left * right modulo M.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 108. Evaluate Reverse Polish Notation

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session3/EvalRPN.java) | [LeetCode](https://leetcode.com/problems/evaluate-reverse-polish-notation/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Parentheses/precedence disappear in RPN; the only state needed is operand stack.
- Pattern: Stack / Monotonic Stack, using Stack.
- Invariant/state: Postfix expression evaluates when each operator consumes the latest two operands from a stack.
- Code idea: Push numbers; on operator pop b then a, compute a op b, push result.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 109. Find The Index Of The First Occurrence In A String

- Links: [Java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java) | [LeetCode](https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/)
- Brute force: Simulate the process directly or compare every possible candidate/string.
- Bottleneck: Naive matching restarts too far; LPS tells how much matched work remains valid.
- Pattern: Math / Bit / String, using KMP string matching.
- Invariant/state: KMP reuses the longest proper prefix that is also a suffix after a mismatch.
- Code idea: Build LPS for needle, scan haystack with i/j, and fallback j = lps[j - 1] on mismatch.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 110. Basic Calculator

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session3/BasicCalculator.java) | [LeetCode](https://leetcode.com/problems/basic-calculator/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Direct left-to-right evaluation breaks when parentheses change the active sign context.
- Pattern: Stack / Monotonic Stack, using Stack / expression parsing.
- Invariant/state: Use sign and stack to preserve the expression value before each parenthesis.
- Code idea: Track result, sign, number; on '(' push result/sign and reset; on ')' fold into previous context.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## Phase 4 - Secondary

Ranks 111-150. Good coverage after the main interview patterns are under control.

### 111. Longest Palindrome

- Links: [Java](../../src/main/java/org/chijai/day3/session3/LongestPalindrome.java) | [LeetCode](https://leetcode.com/problems/longest-palindrome/)
- Brute force: Scan repeatedly or compare every candidate pair/count directly.
- Bottleneck: Order does not matter here; frequency parity decides how many chars can be used.
- Pattern: HashMap / Frequency / Set, using Hash/frequency.
- Invariant/state: At most one character may have an odd count; pairs from all counts build the longest palindrome.
- Code idea: Count chars, add count / 2 * 2, and allow one odd center if any count is odd.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 112. Longest Palindromic Substring

- Links: [Java](../../src/main/java/org/chijai/day3/session3/LongestPalindromicSubstring.java) | [LeetCode](https://leetcode.com/problems/longest-palindromic-substring/)
- Brute force: Try all pairs, all boundaries, or build an auxiliary cleaned structure.
- Bottleneck: Every palindrome is defined by its center, which is cheaper than checking all substrings.
- Pattern: Two Pointers, using Expand around center.
- Invariant/state: Expand around every odd and even center and keep the longest span.
- Code idea: For each index, expand(i,i) and expand(i,i+1), update best start/length.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 113. Count Number Of Nice Subarrays

- Links: [Java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/NiceSubArrays.java) | [LeetCode](https://leetcode.com/problems/count-number-of-nice-subarrays/)
- Brute force: Enumerate every substring/subarray and recompute validity from scratch.
- Bottleneck: Enumerating subarrays repeats odd counts; prefix/window reuses odd-count state.
- Pattern: Sliding Window, using Prefix/window counting.
- Invariant/state: Exactly k odds equals atMost(k) minus atMost(k-1), or prefix count of odd count.
- Code idea: Count subarrays with at most k odd numbers using a sliding left pointer, subtract atMost(k-1).
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 114. Minimum Size Subarray Sum

- Links: [Java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LeetCode](https://leetcode.com/problems/minimum-size-subarray-sum/)
- Brute force: Enumerate every substring/subarray and recompute validity from scratch.
- Bottleneck: Brute force recomputes sums; positivity makes sum monotonic under window movement.
- Pattern: Sliding Window, using Sliding window / need-have.
- Invariant/state: For positive numbers, expand until sum >= target, then shrink to minimize length.
- Code idea: Add right to sum, while sum >= target update min and subtract left.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 115. Longest Common Prefix

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) | [LeetCode](https://leetcode.com/problems/longest-common-prefix/)
- Brute force: For every index or query, recompute the needed range/product/sum directly.
- Bottleneck: Brute force recomputes ranges; prefix/suffix stores reusable aggregate state.
- Pattern: Prefix Sum / Prefix-Suffix, using Trie.
- Invariant/state: Precompute cumulative left/right state so each range or exclusion is answered cheaply.
- Code idea: Build prefix/suffix arrays or running aggregates, then combine in O(1) per query/index.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 116. Longest Word in Dictionary

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) | [LeetCode](https://leetcode.com/problems/longest-word-in-dictionary/)
- Brute force: For every index or query, recompute the needed range/product/sum directly.
- Bottleneck: Brute force recomputes ranges; prefix/suffix stores reusable aggregate state.
- Pattern: Prefix Sum / Prefix-Suffix, using Trie.
- Invariant/state: Precompute cumulative left/right state so each range or exclusion is answered cheaply.
- Code idea: Build prefix/suffix arrays or running aggregates, then combine in O(1) per query/index.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 117. Replace Words

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) | [LeetCode](https://leetcode.com/problems/replace-words/)
- Brute force: For every index or query, recompute the needed range/product/sum directly.
- Bottleneck: Brute force recomputes ranges; prefix/suffix stores reusable aggregate state.
- Pattern: Prefix Sum / Prefix-Suffix, using Trie.
- Invariant/state: Precompute cumulative left/right state so each range or exclusion is answered cheaply.
- Code idea: Build prefix/suffix arrays or running aggregates, then combine in O(1) per query/index.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 118. Search Suggestions System

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) | [LeetCode](https://leetcode.com/problems/search-suggestions-system/)
- Brute force: For every index or query, recompute the needed range/product/sum directly.
- Bottleneck: Brute force recomputes ranges; prefix/suffix stores reusable aggregate state.
- Pattern: Prefix Sum / Prefix-Suffix, using Trie.
- Invariant/state: Precompute cumulative left/right state so each range or exclusion is answered cheaply.
- Code idea: Build prefix/suffix arrays or running aggregates, then combine in O(1) per query/index.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 119. Short Encoding of Words

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) | [LeetCode](https://leetcode.com/problems/short-encoding-of-words/)
- Brute force: For every index or query, recompute the needed range/product/sum directly.
- Bottleneck: Brute force recomputes ranges; prefix/suffix stores reusable aggregate state.
- Pattern: Prefix Sum / Prefix-Suffix, using Trie.
- Invariant/state: Precompute cumulative left/right state so each range or exclusion is answered cheaply.
- Code idea: Build prefix/suffix arrays or running aggregates, then combine in O(1) per query/index.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 120. Parallel Courses

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) | [LeetCode](https://leetcode.com/problems/parallel-courses/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Brute force may use extra storage; pointer invariants let us solve in one pass or O(1) space.
- Pattern: Linked List Pointers, using Topological sort / cycle.
- Invariant/state: Name every pointer, save next before rewiring, and return the real new head.
- Code idea: Use dummy when head can change; update prev/current/next in a fixed order.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 121. Alien Dictionary

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) | [LeetCode](https://leetcode.com/problems/alien-dictionary/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Brute force may use extra storage; pointer invariants let us solve in one pass or O(1) space.
- Pattern: Linked List Pointers, using Topological sort / cycle.
- Invariant/state: Name every pointer, save next before rewiring, and return the real new head.
- Code idea: Use dummy when head can change; update prev/current/next in a fixed order.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 122. Find Eventual Safe States

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) | [LeetCode](https://leetcode.com/problems/find-eventual-safe-states/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Brute force may use extra storage; pointer invariants let us solve in one pass or O(1) space.
- Pattern: Linked List Pointers, using Topological sort / cycle.
- Invariant/state: Name every pointer, save next before rewiring, and return the real new head.
- Code idea: Use dummy when head can change; update prev/current/next in a fixed order.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 123. Sequence Reconstruction

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) | [LeetCode](https://leetcode.com/problems/sequence-reconstruction/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Brute force may use extra storage; pointer invariants let us solve in one pass or O(1) space.
- Pattern: Linked List Pointers, using Topological sort / cycle.
- Invariant/state: Name every pointer, save next before rewiring, and return the real new head.
- Code idea: Use dummy when head can change; update prev/current/next in a fixed order.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 124. Sort Items by Groups Respecting Dependencies

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) | [LeetCode](https://leetcode.com/problems/sort-items-by-groups-respecting-dependencies/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Brute force may use extra storage; pointer invariants let us solve in one pass or O(1) space.
- Pattern: Linked List Pointers, using Topological sort / cycle.
- Invariant/state: Name every pointer, save next before rewiring, and return the real new head.
- Code idea: Use dummy when head can change; update prev/current/next in a fixed order.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 125. Graph Valid Tree

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) | [LeetCode](https://leetcode.com/problems/graph-valid-tree/)
- Brute force: Traverse without preserving levels, then reconstruct level/view information afterward.
- Bottleneck: DFS can mix levels; BFS preserves level order for views, distances, and serialization.
- Pattern: Tree BFS / Level Order, using BFS/DFS coloring.
- Invariant/state: Use a queue by levels; capture level size before pushing children.
- Code idea: Queue root, loop by level size, push children, collect per-level result.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 126. Path Sum

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) | [LeetCode](https://leetcode.com/problems/path-sum/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Only root-to-leaf complete paths count.
- Pattern: Tree DFS / Recursion, using Tree path DFS / global answer.
- Invariant/state: Subtract node values along root-to-leaf paths and check target at leaf.
- Code idea: DFS with remaining sum; at leaf return remaining == node.val.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 127. Binary Tree Postorder Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) | [LeetCode](https://leetcode.com/problems/binary-tree-postorder-traversal/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: A parent cannot be finalized before children when return data flows upward.
- Pattern: Tree DFS / Recursion, using Tree DFS / stack.
- Invariant/state: Postorder visits children before the node, useful when parent depends on subtree results.
- Code idea: Use recursion or stack with last-visited tracking; visit after left and right.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 128. Binary Tree Preorder Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) | [LeetCode](https://leetcode.com/problems/binary-tree-preorder-traversal/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Root-first order captures decisions before descending.
- Pattern: Tree DFS / Recursion, using Tree DFS / stack.
- Invariant/state: Preorder visits node before children, useful for serialization and copying structure.
- Code idea: Visit node, then left, then right; iterative stack pushes right before left.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 129. Insert Into A Binary Search Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | [LeetCode](https://leetcode.com/problems/insert-into-a-binary-search-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: BST property removes the need to search both sides.
- Pattern: Tree DFS / Recursion, using BST property.
- Invariant/state: Use BST ordering to walk one branch until a null child is found, then insert there.
- Code idea: Iterate or recurse: if val < node.val go left, else go right; attach new node at null.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 130. Minimum Absolute Difference In BST

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | [LeetCode](https://leetcode.com/problems/minimum-absolute-difference-in-bst/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Comparing all pairs is unnecessary once sorted order is available.
- Pattern: Tree DFS / Recursion, using BST property.
- Invariant/state: BST inorder is sorted, so minimum difference is between adjacent inorder values.
- Code idea: Inorder traverse, track previous value and best difference.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 131. Range Sum Of BST

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | [LeetCode](https://leetcode.com/problems/range-sum-of-bst/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Full traversal works but wastes branches that cannot contribute.
- Pattern: Tree DFS / Recursion, using BST property.
- Invariant/state: BST ordering lets you prune subtrees outside [low, high].
- Code idea: If node < low go right, if node > high go left, else add node and both sides.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 132. Search In A Binary Search Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | [LeetCode](https://leetcode.com/problems/search-in-a-binary-search-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: BST ordering prunes half the tree at every step.
- Pattern: Tree DFS / Recursion, using BST property.
- Invariant/state: Compare target with node value and move only to the branch that can still contain it.
- Code idea: While node != null and node.val != val, move left if val < node.val else right.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 133. All Nodes Distance K in Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java) | [LeetCode](https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Brute force revisits subtrees; helper return contracts summarize each subtree once.
- Pattern: Tree DFS / Recursion, using Tree + graph BFS.
- Invariant/state: Define exactly what the helper returns, combine left/right, and update global answer separately if needed.
- Code idea: Base case null, recurse left/right, compute local result, return contract.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 134. Amount of Time for Binary Tree to Be Infected

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java) | [LeetCode](https://leetcode.com/problems/amount-of-time-for-binary-tree-to-be-infected/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Brute force revisits subtrees; helper return contracts summarize each subtree once.
- Pattern: Tree DFS / Recursion, using Tree + graph BFS.
- Invariant/state: Define exactly what the helper returns, combine left/right, and update global answer separately if needed.
- Code idea: Base case null, recurse left/right, compute local result, return contract.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 135. Recover Binary Search Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) | [LeetCode](https://leetcode.com/problems/recover-binary-search-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: BST validity is an inorder ordering invariant, not a local parent-child check.
- Pattern: Tree DFS / Recursion, using BST inorder.
- Invariant/state: Inorder traversal should be sorted; the two broken nodes appear at one or two inversions.
- Code idea: Track prev, first, second during inorder; after traversal swap first.val and second.val.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 136. Binary Search Tree Iterator

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) | [LeetCode](https://leetcode.com/problems/binary-search-tree-iterator/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Need sorted iteration without flattening the whole tree up front.
- Pattern: Tree DFS / Recursion, using BST inorder.
- Invariant/state: Maintain a stack of the current left spine so next() returns the next inorder value lazily.
- Code idea: pushLeft(root); next() pops, then pushLeft(node.right); hasNext() checks stack.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 137. Convert BST To Greater Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) | [LeetCode](https://leetcode.com/problems/convert-bst-to-greater-tree/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: BST sorted order makes right-node-left the natural accumulation order.
- Pattern: Tree DFS / Recursion, using BST inorder.
- Invariant/state: Reverse inorder visits larger values first, so a running sum can rewrite each node.
- Code idea: Traverse right, add node.val into running sum, rewrite node.val, then traverse left.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 138. Possible Bipartition

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) | [LeetCode](https://leetcode.com/problems/possible-bipartition/)
- Brute force: Run a separate search from each source or use DFS and then compare path lengths.
- Bottleneck: DFS finds a path, but BFS gives shortest path when every edge has equal cost.
- Pattern: Graph BFS / Shortest Path, using BFS/DFS coloring.
- Invariant/state: Use queue layers when first discovery is the shortest or minimum-step answer.
- Code idea: Queue start states, mark visited immediately, expand valid neighbors by level.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 139. Redundant Connection

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) | [LeetCode](https://leetcode.com/problems/redundant-connection/)
- Brute force: Run a separate search from each source or use DFS and then compare path lengths.
- Bottleneck: DFS finds a path, but BFS gives shortest path when every edge has equal cost.
- Pattern: Graph BFS / Shortest Path, using BFS/DFS coloring.
- Invariant/state: Use queue layers when first discovery is the shortest or minimum-step answer.
- Code idea: Queue start states, mark visited immediately, expand valid neighbors by level.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 140. K Highest Ranked Items Within A Price Range

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session3/KHighestRankedItemsWithinAPriceRange.java) | [LeetCode](https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/)
- Brute force: Run a separate search from each source or use DFS and then compare path lengths.
- Bottleneck: DFS does not preserve shortest distance order in the grid.
- Pattern: Graph BFS / Shortest Path, using BFS + sorting.
- Invariant/state: BFS by distance, collecting valid items and sorting tie-breaks by price,row,col.
- Code idea: BFS from start through passable cells; collect price-in-range items with distance and sort ranking.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 141. Number Of Closed Islands

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LeetCode](https://leetcode.com/problems/number-of-closed-islands/)
- Brute force: Start a fresh traversal for every cell/node without reusable visited/component state.
- Bottleneck: Counting components alone overcounts islands connected to the border.
- Pattern: Graph DFS / Components, using Matrix DFS/BFS components.
- Invariant/state: A closed island is a land component that never touches the grid boundary.
- Code idea: DFS each land component, return false if any cell touches border, mark visited.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 142. Max Area Of Island

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LeetCode](https://leetcode.com/problems/max-area-of-island/)
- Brute force: Start a fresh traversal for every cell/node without reusable visited/component state.
- Bottleneck: Counting land globally ignores component boundaries.
- Pattern: Graph DFS / Components, using Matrix DFS/BFS components.
- Invariant/state: DFS each land component and return its cell count; keep the maximum.
- Code idea: On each unvisited land cell, DFS four directions accumulating area.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 143. Coloring A Border

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/ColoringABorder.java) | [LeetCode](https://leetcode.com/problems/coloring-a-border/)
- Brute force: Start a fresh traversal for every cell/node without reusable visited/component state.
- Bottleneck: Flood filling the whole component changes interior cells incorrectly.
- Pattern: Graph DFS / Components, using Matrix DFS.
- Invariant/state: Only cells on the component boundary get recolored; interior cells keep original color.
- Code idea: DFS component, mark a cell as border if it touches outside grid or different color.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 144. Sqrtx

- Links: [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LeetCode](https://leetcode.com/problems/sqrtx/)
- Brute force: Try integers one by one until square exceeds x.
- Bottleneck: Linear testing is slow and mid*mid can overflow without long arithmetic.
- Pattern: Binary Search / Answer Search, using Binary search boundary.
- Invariant/state: Find the largest integer mid whose square is <= x.
- Code idea: Binary search 0..x, cast mid*mid to long, save mid when square <= x.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 145. Maximal Rectangle

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java) | [LeetCode](https://leetcode.com/problems/maximal-rectangle/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Checking every rectangle is too slow; row heights reuse vertical continuity.
- Pattern: Stack / Monotonic Stack, using Monotonic stack.
- Invariant/state: Treat every matrix row as histogram heights and run largest-rectangle on each row.
- Code idea: Update heights per row, then compute largest histogram area with monotonic stack.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 146. Min Stack

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) | [LeetCode](https://leetcode.com/problems/min-stack/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Scanning stack on getMin makes the required O(1) operation impossible.
- Pattern: Stack / Monotonic Stack, using Stack design.
- Invariant/state: Store the current minimum with each push, or keep a second stack of minimums.
- Code idea: Push value and min(value,currentMin); pop both together; getMin reads min top.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 147. Max Stack

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) | [LeetCode](https://leetcode.com/problems/max-stack/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: A plain stack gives pop order but cannot remove max efficiently.
- Pattern: Stack / Monotonic Stack, using Stack design.
- Invariant/state: Maintain stack order plus a way to locate/remove the current maximum.
- Code idea: Use stack plus max tracking, or doubly linked list plus TreeMap for O(log n) popMax.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 148. Implement Queue Using Stacks

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) | [LeetCode](https://leetcode.com/problems/implement-queue-using-stacks/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Moving elements on every operation repeats work; lazy transfer amortizes the reversal.
- Pattern: Stack / Monotonic Stack, using Stack/queue design.
- Invariant/state: Use input stack for pushes and output stack for pops; transfer only when output is empty.
- Code idea: push -> in.push; pop/peek -> if out empty move all in to out, then read out.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 149. Implement Stack Using Queues

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) | [LeetCode](https://leetcode.com/problems/implement-stack-using-queues/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Queue order is FIFO; rotation restores LIFO behavior.
- Pattern: Stack / Monotonic Stack, using Stack/queue design.
- Invariant/state: After each push, rotate the queue so the newest element is at the front.
- Code idea: Offer x, then rotate size-1 older elements behind it; pop removes queue front.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 150. Next Greater Element I

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) | [LeetCode](https://leetcode.com/problems/next-greater-element-i/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Searching nums2 for every nums1 value repeats the same next-greater work.
- Pattern: Stack / Monotonic Stack, using Stack design.
- Invariant/state: Precompute next greater for nums2 with a decreasing stack, then answer nums1 by map lookup.
- Code idea: Scan nums2, pop smaller values and map them to current, then lookup each nums1 value.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## Phase 5 - If Time

Ranks 151+. Cover only if time remains or a target interviewer leans this way.

### 151. Online Stock Span

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) | [LeetCode](https://leetcode.com/problems/online-stock-span/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Scanning backward repeats work; collapsed spans let each price enter and leave once.
- Pattern: Stack / Monotonic Stack, using Stack design.
- Invariant/state: A decreasing stack of price/span pairs merges all previous prices <= current price.
- Code idea: Start span=1, while stack top price <= current add its span and pop, then push current/span.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 152. Meeting Room

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/MeetingRoom.java)
- Brute force: Sort all candidates every time a top, kth, median, or next-best item is needed.
- Bottleneck: Sorting everything is wasteful; a heap keeps only the next best or top K frontier.
- Pattern: Heap / Priority Queue, using Intervals / heap.
- Invariant/state: Keep only the frontier, top K, or two balanced halves instead of fully sorting each step.
- Code idea: Push candidates with comparator; poll when size or frontier rules require it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 153. K Closest Points To Origin

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/KClosestPointsToOrigin.java) | [LeetCode](https://leetcode.com/problems/k-closest-points-to-origin/)
- Brute force: Sort all candidates every time a top, kth, median, or next-best item is needed.
- Bottleneck: Sorting all points is unnecessary when only k closest are needed.
- Pattern: Heap / Priority Queue, using Heap / quickselect.
- Invariant/state: Keep the k smallest squared distances; compare without taking square roots.
- Code idea: Use max-heap of size k by distance, or quickselect by squared distance.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 154. Top K Frequent Words

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) | [LeetCode](https://leetcode.com/problems/top-k-frequent-words/)
- Brute force: Sort all candidates every time a top, kth, median, or next-best item is needed.
- Bottleneck: Sorting everything is wasteful; a heap keeps only the next best or top K frontier.
- Pattern: Heap / Priority Queue, using Frequency + heap/bucket.
- Invariant/state: Keep only the frontier, top K, or two balanced halves instead of fully sorting each step.
- Code idea: Push candidates with comparator; poll when size or frontier rules require it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 155. H-Index

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) | [LeetCode](https://leetcode.com/problems/h-index/)
- Brute force: Sort all candidates every time a top, kth, median, or next-best item is needed.
- Bottleneck: Sorting everything is wasteful; a heap keeps only the next best or top K frontier.
- Pattern: Heap / Priority Queue, using Frequency + heap/bucket.
- Invariant/state: Keep only the frontier, top K, or two balanced halves instead of fully sorting each step.
- Code idea: Push candidates with comparator; poll when size or frontier rules require it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 156. Sort Characters By Frequency

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) | [LeetCode](https://leetcode.com/problems/sort-characters-by-frequency/)
- Brute force: Sort all candidates every time a top, kth, median, or next-best item is needed.
- Bottleneck: Comparator sorting every character occurrence is wasteful; sort unique chars by counts.
- Pattern: Heap / Priority Queue, using Frequency + heap/bucket.
- Invariant/state: Frequency map plus bucket/heap outputs characters from highest count to lowest.
- Code idea: Count chars, bucket by frequency or heap entries, append char repeated count times.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 157. Intervals

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/Intervals.java)
- Brute force: Compare every interval with every other interval before deciding conflicts/order.
- Bottleneck: Unsorted comparisons are noisy; sorting makes overlap or greedy choice local.
- Pattern: Intervals / Sorting Greedy, using Intervals / merge.
- Invariant/state: Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints.
- Code idea: Sort by start/end, then merge/count/select with one pass or heap.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 158. Gas Station

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/GasStation.java) | [LeetCode](https://leetcode.com/problems/gas-station/)
- Brute force: Compare every interval with every other interval before deciding conflicts/order.
- Bottleneck: Unsorted comparisons are noisy; sorting makes overlap or greedy choice local.
- Pattern: Intervals / Sorting Greedy, using Greedy.
- Invariant/state: Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints.
- Code idea: Sort by start/end, then merge/count/select with one pass or heap.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 159. Jump Game

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/GasStation.java) | [LeetCode](https://leetcode.com/problems/jump-game/)
- Brute force: Compare every interval with every other interval before deciding conflicts/order.
- Bottleneck: Unsorted comparisons are noisy; sorting makes overlap or greedy choice local.
- Pattern: Intervals / Sorting Greedy, using Greedy.
- Invariant/state: Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints.
- Code idea: Sort by start/end, then merge/count/select with one pass or heap.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 160. Car Pooling

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/MinimumPlatforms.java) | [LeetCode](https://leetcode.com/problems/car-pooling/)
- Brute force: For every route point, recompute passenger load by checking all trips.
- Bottleneck: Checking every trip pair misses the global passenger load over the route.
- Pattern: Intervals / Sorting Greedy, using Intervals / sorting.
- Invariant/state: Treat each pickup/dropoff as passenger-count delta and ensure capacity is never exceeded.
- Code idea: Use difference array or sorted events: add passengers at start, subtract at end, track running load.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 161. Letter Combinations Of A Phone Number

- Links: [Java](../../src/main/java/org/chijai/day11/backtracking/session1/LetterCombinationsOfAPhoneNumber.java) | [LeetCode](https://leetcode.com/problems/letter-combinations-of-a-phone-number/)
- Brute force: Generate all possible candidates first, then filter invalid answers at the end.
- Bottleneck: Brute force generates blindly; backtracking prunes invalid decision paths early.
- Pattern: Backtracking / Combinatorial DFS, using Backtracking / mapping.
- Invariant/state: Choose, recurse, undo; the path is exactly the current decision state.
- Code idea: Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 162. Permutations

- Links: [Java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) | [LeetCode](https://leetcode.com/problems/permutations/)
- Brute force: Generate all possible candidates first, then filter invalid answers at the end.
- Bottleneck: Brute force generates blindly; backtracking prunes invalid decision paths early.
- Pattern: Backtracking / Combinatorial DFS, using Backtracking permutations.
- Invariant/state: Choose, recurse, undo; the path is exactly the current decision state.
- Code idea: Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 163. Spiral Matrix

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session1/SpiralMatrix.java) | [LeetCode](https://leetcode.com/problems/spiral-matrix/)
- Brute force: Try the direct simulation or enumeration first.
- Bottleneck: Visited simulation is more state than needed; boundaries define the remaining ring.
- Pattern: Basics / Implementation, using Matrix boundary traversal.
- Invariant/state: Shrink top, bottom, left, and right boundaries after traversing each side.
- Code idea: Traverse top row, right col, bottom row if valid, left col if valid; move boundaries inward.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 164. String To Integer Atoi

- Links: [Java](../../src/main/java/org/chijai/day3/session3/StringToIntegerAtoi.java) | [LeetCode](https://leetcode.com/problems/string-to-integer-atoi/)
- Brute force: Try the direct simulation or enumeration first.
- Bottleneck: Using built-in parse or wider assumptions misses whitespace, sign, and overflow rules.
- Pattern: Basics / Implementation, using Parsing / edge cases.
- Invariant/state: Parse sign and digits once, clamping before overflow.
- Code idea: Skip spaces, read optional sign, accumulate digit while checking against INT_MAX limits.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 165. Repeated Substring Pattern

- Links: [Java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java) | [LeetCode](https://leetcode.com/problems/repeated-substring-pattern/)
- Brute force: Simulate the process directly or compare every possible candidate/string.
- Bottleneck: Testing every divisor naively repeats string comparisons; KMP exposes the repeated border.
- Pattern: Math / Bit / String, using KMP string matching.
- Invariant/state: A repeated pattern exists when the final LPS leaves a block length that divides n.
- Code idea: Let len = lps[n - 1]; return len > 0 and n % (n - len) == 0.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 166. Maximum XOR of Two Numbers in an Array

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) | [LeetCode](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/)
- Brute force: Compare each word/prefix character-by-character against every dictionary entry.
- Bottleneck: Checking all pairs is O(n^2); bitwise trie preserves candidate prefixes cheaply.
- Pattern: Trie, using Binary trie / bit.
- Invariant/state: Binary trie chooses the opposite bit greedily to maximize each XOR bit from high to low.
- Code idea: Insert numbers by bits, then for each number walk preferred opposite bits and update max.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 167. Design A Stack With Increment Operation

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) | [LeetCode](https://leetcode.com/problems/design-a-stack-with-increment-operation/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Incrementing bottom k elements directly makes increment O(k).
- Pattern: Stack / Monotonic Stack, using Stack design.
- Invariant/state: Lazy increment stores pending additions at the boundary index instead of touching k items.
- Code idea: Keep stack plus inc array; on pop carry inc[i] to inc[i-1] and return value + inc[i].
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 168. Design Circular Queue

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) | [LeetCode](https://leetcode.com/problems/design-circular-queue/)
- Brute force: For each element, scan left/right or simulate operations without remembering unresolved state.
- Bottleneck: Shifting array elements on enqueue/dequeue is unnecessary and slow.
- Pattern: Stack / Monotonic Stack, using Stack/queue design.
- Invariant/state: Circular queue uses head, size, and modulo arithmetic to reuse fixed array slots.
- Code idea: enQueue writes at (head + size) % capacity; deQueue advances head and decrements size.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 169. Longest Happy Prefix

- Links: [Java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) | [LeetCode](https://leetcode.com/problems/longest-happy-prefix/)
- Brute force: Simulate the process directly or compare every possible candidate/string.
- Bottleneck: Trying every prefix repeats comparisons; KMP prefix table stores reusable border lengths.
- Pattern: Math / Bit / String, using KMP / rolling hash.
- Invariant/state: The answer is the final LPS value: longest proper prefix that is also suffix.
- Code idea: Build LPS over the string and return substring(0, lps[n - 1]).
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 170. Map Sum Pairs

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) | [LeetCode](https://leetcode.com/problems/map-sum-pairs/)
- Brute force: Compare each word/prefix character-by-character against every dictionary entry.
- Bottleneck: Repeated string scans waste prefix work; trie shares prefixes across words.
- Pattern: Trie, using Trie + DFS wildcard.
- Invariant/state: Share prefix nodes so lookup/search consumes one character at a time instead of rescanning words.
- Code idea: Insert words by characters; search follows children and DFS branches on wildcard/board.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 171. Maximum XOR With an Element From Array

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) | [LeetCode](https://leetcode.com/problems/maximum-xor-with-an-element-from-array/)
- Brute force: Compare each word/prefix character-by-character against every dictionary entry.
- Bottleneck: Repeated string scans waste prefix work; trie shares prefixes across words.
- Pattern: Trie, using Binary trie / bit.
- Invariant/state: Share prefix nodes so lookup/search consumes one character at a time instead of rescanning words.
- Code idea: Insert words by characters; search follows children and DFS branches on wildcard/board.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 172. Maximum Genetic Difference Query

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) | [LeetCode](https://leetcode.com/problems/maximum-genetic-difference-query/)
- Brute force: Compare each word/prefix character-by-character against every dictionary entry.
- Bottleneck: Repeated string scans waste prefix work; trie shares prefixes across words.
- Pattern: Trie, using Binary trie / bit.
- Invariant/state: Share prefix nodes so lookup/search consumes one character at a time instead of rescanning words.
- Code idea: Insert words by characters; search follows children and DFS branches on wildcard/board.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 173. Count Pairs With XOR in a Range

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) | [LeetCode](https://leetcode.com/problems/count-pairs-with-xor-in-a-range/)
- Brute force: Compare each word/prefix character-by-character against every dictionary entry.
- Bottleneck: Repeated string scans waste prefix work; trie shares prefixes across words.
- Pattern: Trie, using Binary trie / bit.
- Invariant/state: Share prefix nodes so lookup/search consumes one character at a time instead of rescanning words.
- Code idea: Insert words by characters; search follows children and DFS branches on wildcard/board.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 174. Climbing Stairs Fib

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session1/ClimbingStairsFib.java)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Recursive Fibonacci repeats the same smaller step counts.
- Pattern: Dynamic Programming, using 1D DP.
- Invariant/state: Ways to step n equals ways to n-1 plus ways to n-2.
- Code idea: Iterate two rolling values for ways to previous one and two steps.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 175. Edit Distance

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) | [LeetCode](https://leetcode.com/problems/edit-distance/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion branches into insert/delete/replace repeatedly for same prefixes.
- Pattern: Dynamic Programming, using 2D DP.
- Invariant/state: dp[i][j] is edits to convert first i chars of word1 to first j chars of word2.
- Code idea: Initialize empty-string row/column; if chars equal copy diagonal else 1 + min(insert, delete, replace).
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 176. Sort Array By Parity

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session1/SortColors.java) | [LeetCode](https://leetcode.com/problems/sort-array-by-parity/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using Partition / Dutch flag.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 177. Best Time to Buy and Sell Stock with Cooldown

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java) | [LeetCode](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using Greedy / DP states.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 178. Best Time to Buy and Sell Stock with Transaction Fee

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java) | [LeetCode](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using Greedy / DP states.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 179. Move Zeroes

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session1/SortColors.java) | [LeetCode](https://leetcode.com/problems/move-zeroes/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using Partition / Dutch flag.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 180. Climbing Stairs

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) | [LeetCode](https://leetcode.com/problems/climbing-stairs/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using Unbounded knapsack DP.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 181. Min Cost Climbing Stairs

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) | [LeetCode](https://leetcode.com/problems/min-cost-climbing-stairs/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using Unbounded knapsack DP.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 182. Perfect Squares

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) | [LeetCode](https://leetcode.com/problems/perfect-squares/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using Unbounded knapsack DP.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 183. Word Break

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) | [LeetCode](https://leetcode.com/problems/word-break/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using Unbounded knapsack DP.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 184. Delete Operation for Two Strings

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) | [LeetCode](https://leetcode.com/problems/delete-operation-for-two-strings/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using 2D DP.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 185. Distinct Subsequences

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) | [LeetCode](https://leetcode.com/problems/distinct-subsequences/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using 2D DP.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 186. Interleaving String

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) | [LeetCode](https://leetcode.com/problems/interleaving-string/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using 2D DP.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 187. Longest Common Subsequence

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) | [LeetCode](https://leetcode.com/problems/longest-common-subsequence/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using 2D DP.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 188. Longest Palindromic Subsequence

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) | [LeetCode](https://leetcode.com/problems/longest-palindromic-subsequence/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using 2D DP.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 189. Minimum ASCII Delete Sum for Two Strings

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) | [LeetCode](https://leetcode.com/problems/minimum-ascii-delete-sum-for-two-strings/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using 2D DP.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 190. Longest Continuous Increasing Subsequence

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) | [LeetCode](https://leetcode.com/problems/longest-continuous-increasing-subsequence/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using DP / patience sorting.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 191. Maximum Length of Pair Chain

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) | [LeetCode](https://leetcode.com/problems/maximum-length-of-pair-chain/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using DP / patience sorting.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 192. Number of Longest Increasing Subsequence

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) | [LeetCode](https://leetcode.com/problems/number-of-longest-increasing-subsequence/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using DP / patience sorting.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 193. Russian Doll Envelopes

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) | [LeetCode](https://leetcode.com/problems/russian-doll-envelopes/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using DP / patience sorting.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 194. Partition Labels

- Links: [Java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java) | [LeetCode](https://leetcode.com/problems/partition-labels/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using Contribution counting.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 195. Add Binary

- Links: [Java](../../src/main/java/org/chijai/day10/session2/AddBinary.java) | [LeetCode](https://leetcode.com/problems/add-binary/)
- Brute force: Simulate the process directly or compare every possible candidate/string.
- Bottleneck: Converting to integer can overflow and hides the carry invariant.
- Pattern: Math / Bit / String, using Bit/string addition.
- Invariant/state: Add bits from right to left with carry, exactly like decimal addition.
- Code idea: Use i,j,carry; append (sum % 2), update carry=sum/2, reverse result.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 196. Count Primes

- Links: [Java](../../src/main/java/org/chijai/day10/session2/CountPrimes.java) | [LeetCode](https://leetcode.com/problems/count-primes/)
- Brute force: Simulate the process directly or compare every possible candidate/string.
- Bottleneck: Testing every number by trial division repeats divisibility work.
- Pattern: Math / Bit / String, using Math / sieve.
- Invariant/state: Sieve marks multiples of each discovered prime starting at p*p.
- Code idea: Boolean isComposite; for p*p < n, mark multiples p*p, p*p+p, ...; count unmarked.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 197. Count Unique Characters of All Substrings of a Given String

- Links: [Java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java) | [LeetCode](https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/)
- Brute force: Simulate the process directly or compare every possible candidate/string.
- Bottleneck: Contribution counting avoids enumerating all substrings.
- Pattern: Math / Bit / String, using Contribution counting.
- Invariant/state: Each character occurrence contributes by distance to the previous same char times distance to the next one.
- Code idea: Record previous and next positions for each occurrence, sum leftGap * rightGap contributions.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 198. Award Top K Hotels

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/AwardTopKHotels.java)
- Brute force: Sort all candidates every time a top, kth, median, or next-best item is needed.
- Bottleneck: Repeated text scans and full sorting can be avoided with maps and top-k selection.
- Pattern: Heap / Priority Queue, using Heap / ranking.
- Invariant/state: Score each hotel by keyword hits, then rank by score and tie-breaker.
- Code idea: Build keyword set, count matches per hotel review, then sort or heap by score/id.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 199. Shortest Palindrome

- Links: [Java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java) | [LeetCode](https://leetcode.com/problems/shortest-palindrome/)
- Brute force: Simulate the process directly or compare every possible candidate/string.
- Bottleneck: Expanding every prefix is expensive; KMP on s + # + reverse(s) finds the prefix length.
- Pattern: Math / Bit / String, using KMP string matching.
- Invariant/state: Find the longest palindromic prefix, then prepend the reverse of the remaining suffix.
- Code idea: Compute LPS on combined string, reverse suffix from lps length, prepend it to s.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 200. Reverse Linked List Ii

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) | [LeetCode](https://leetcode.com/problems/reverse-linked-list-ii/)
- Brute force: Copy nodes into an array/set, or make extra passes to recover positions.
- Bottleneck: Head can change; dummy plus sublist predecessor prevents edge-case bugs.
- Pattern: Linked List Pointers, using Linked-list reversal groups.
- Invariant/state: Use a dummy and reverse exactly the sublist between left and right.
- Code idea: Find node before left, then head-insert nodes from the sublist for right-left steps.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 201. Path Sum Ii

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) | [LeetCode](https://leetcode.com/problems/path-sum-ii/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Path list is mutable, so choose/explore/undo is required.
- Pattern: Tree DFS / Recursion, using Tree path DFS / global answer.
- Invariant/state: Backtrack the current root-to-leaf path and copy it when the target is hit.
- Code idea: Add node, recurse children with remaining sum, copy on valid leaf, remove node.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 202. Lowest Common Ancestor Of A Binary Tree Ii

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) | [LeetCode](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-ii/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Returning one found node is wrong when the other target is absent.
- Pattern: Tree DFS / Recursion, using Tree DFS return contract.
- Invariant/state: Same split-point idea, but verify both targets actually exist.
- Code idea: DFS returns found node/count flags; only accept LCA when both p and q are found.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 203. Lowest Common Ancestor Of A Binary Tree Iii

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) | [LeetCode](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: No root traversal is needed when each node can move upward.
- Pattern: Tree DFS / Recursion, using Tree DFS return contract.
- Invariant/state: With parent pointers, walk ancestors or switch pointers like linked-list intersection.
- Code idea: Move a and b upward; when null redirect to the other node; meeting is LCA.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 204. Lowest Common Ancestor Of A Binary Tree Iv

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) | [LeetCode](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iv/)
- Brute force: Restart traversal from many nodes or compute subtree facts repeatedly.
- Bottleneck: Pairwise LCA repeats work; a target set lets DFS aggregate matches.
- Pattern: Tree DFS / Recursion, using Tree DFS return contract.
- Invariant/state: For many target nodes, current node is answer when multiple target paths meet.
- Code idea: Return root if in target set; combine child returns and current membership.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 205. Permutations Ii

- Links: [Java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) | [LeetCode](https://leetcode.com/problems/permutations-ii/)
- Brute force: Generate all possible candidates first, then filter invalid answers at the end.
- Bottleneck: Brute force generates blindly; backtracking prunes invalid decision paths early.
- Pattern: Backtracking / Combinatorial DFS, using Backtracking permutations.
- Invariant/state: Choose, recurse, undo; the path is exactly the current decision state.
- Code idea: Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 206. Course Schedule IV

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) | [LeetCode](https://leetcode.com/problems/course-schedule-iv/)
- Brute force: Repeatedly scan all dependencies to find what can be processed next.
- Bottleneck: Brute force dependency checks loop; topo processes nodes only when prerequisites are done.
- Pattern: Topological Sort, using Topological sort / cycle.
- Invariant/state: Use indegree or DFS states to process dependencies before dependents.
- Code idea: Build graph and indegrees, queue zero-indegree nodes, process order.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 207. Distinct Subsequences II

- Links: [Java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java) | [LeetCode](https://leetcode.com/problems/distinct-subsequences-ii/)
- Brute force: Try the direct simulation or enumeration first.
- Bottleneck: Use brute force to expose repeated work, then choose the invariant and data structure.
- Pattern: Basics / Implementation, using Contribution counting.
- Invariant/state: Derive the direct approach, name the wasted work, then choose the invariant that removes it.
- Code idea: Code the invariant directly, then dry-run edge cases.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 208. Encode And Decode Tinyurl

- Links: [Java](../../src/main/java/org/chijai/design/lld/DesignUrlShortner.java) | [LeetCode](https://leetcode.com/problems/encode-and-decode-tinyurl/)
- Brute force: Implement only the happy-path operation with one map and no invariant for edge cases.
- Bottleneck: The core invariant is key uniqueness and persistence, not string shortening alone.
- Pattern: Design Data Structures, using LLD / URL shortener.
- Invariant/state: Encode creates a stable short key mapped to the original URL; decode is a map lookup.
- Code idea: Generate/increment key, store key->longUrl, return domain/key; decode extracts key and reads map.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 209. Hotel Reviews

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/HotelReviews.java)
- Brute force: Compare each word/prefix character-by-character against every dictionary entry.
- Bottleneck: Repeated string matching for every keyword wastes prefix/lookup work.
- Pattern: Trie, using Trie / ranking.
- Invariant/state: Use trie or keyword set to count good words per review, then rank hotels by score.
- Code idea: Normalize review words, count keyword hits, aggregate per hotel, sort by score and id.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 210. Design Fraud Pattern Detection

- Links: [Java](../../src/main/java/org/chijai/design/lld/DesignFraudPatternDetection.java)
- Brute force: Implement only the happy-path operation with one map and no invariant for edge cases.
- Bottleneck: Without explicit time-window and identity keys, the detector becomes vague and untestable.
- Pattern: Design Data Structures, using LLD / domain modeling.
- Invariant/state: Define which transaction events are retained and which rule/window makes a pattern fraudulent.
- Code idea: Index recent events by account/card/merchant, evict expired entries, evaluate rules on insert.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 211. Api Integration Example

- Links: [Java](../../src/main/java/org/chijai/design/lld/ApiIntegrationExample.java)
- Brute force: Implement only the happy-path operation with one map and no invariant for edge cases.
- Bottleneck: Integration code fails interviews when error handling and contracts are implicit.
- Pattern: Design Data Structures, using LLD/API integration.
- Invariant/state: Model request, response, retry, timeout, and idempotency boundaries explicitly.
- Code idea: Wrap client call with typed DTOs, timeout/retry policy, status handling, and clear failure result.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 212. Design Redis

- Links: [Java](../../src/main/java/org/chijai/design/lld/DesignRedis.java)
- Brute force: Implement only the happy-path operation with one map and no invariant for edge cases.
- Bottleneck: A map alone misses TTL semantics and memory-pressure behavior.
- Pattern: Design Data Structures, using LLD / data structures.
- Invariant/state: Key-value operations need storage, expiry metadata, and eviction/cleanup policy.
- Code idea: Store value plus expireAt, check expiry on get/set, and maintain cleanup or eviction structure.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 213. Design Token Bucket Rate Limiter

- Links: [Java](../../src/main/java/org/chijai/design/lld/DesignTokenBucketRateLimiter.java)
- Brute force: Implement only the happy-path operation with one map and no invariant for edge cases.
- Bottleneck: Fixed counters burst badly at window boundaries; token bucket smooths rate with bounded burst.
- Pattern: Design Data Structures, using LLD / rate limiting.
- Invariant/state: A bucket refills by elapsed time and each request consumes one token if available.
- Code idea: Per key, compute tokens = min(capacity, tokens + elapsed*rate), allow if tokens >= cost.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 214. Best Time to Buy and Sell Stock II

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java) | [LeetCode](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using Greedy / DP states.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 215. Best Time to Buy and Sell Stock III

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java) | [LeetCode](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using Greedy / DP states.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

### 216. Best Time to Buy and Sell Stock IV

- Links: [Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java) | [LeetCode](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/)
- Brute force: Use plain recursion or enumerate choices without caching repeated states.
- Bottleneck: Naive recursion repeats states; DP caches each state and reuses transitions.
- Pattern: Dynamic Programming, using Greedy / DP states.
- Invariant/state: Name the state, base case, transition, and iteration order before writing loops.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.