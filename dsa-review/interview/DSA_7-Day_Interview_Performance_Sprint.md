# DSA 7-Day Interview Performance Sprint

Goal: eliminate senior-candidate red flags by training closed-book retrieval, reconstruction, debugging, and explanation.

Source of truth: generated from `01_ZERO_TO_HERO_RANKED_TABLE.md` data. The sprint keeps a cognitive training order, while `Source Rank` preserves the canonical ranking.

## North Star

Random problem -> recognize family -> state invariant -> code working Java from a blank editor -> test -> explain complexity/trade-offs.

## Non-Negotiable Rules

- First attempt is closed-book: blank editor, no old Java, no notes, no solution.
- Each listed slot is a 20-minute diagnostic time-box, not a guarantee of completion.
- At 20:00, score the attempt and move on. A failure found here is interview data.
- Do not sacrifice Rank 1-50 repair just to touch Rank 150.
- Reviews are active retrieval: blank editor + timer + reconstruction, not passive rereading.

## 20-Minute Protocol

| Minute | Stage | Required output | Pass condition |
|---:|---|---|---|
| 00-02 | Recognize | Family, pattern, candidate data structure | Plausible approach without notes |
| 02-05 | Derive | Brute force, bottleneck, invariant, complexity | Can explain why it works |
| 05-15 | Implement | Java solution from blank editor | Compiles or clearly represents intended algorithm |
| 15-18 | Test | Normal, boundary, tricky case | Correct or independently debugged |
| 18-20 | Explain + score | Complexity, trade-off, edge case, result | Clear interview explanation |

## Score And Failure Codes

- GREEN: independent recognition, derivation, implementation, testing, and complexity within the time-box.
- YELLOW: right family/idea, but hint, implementation trouble, missed edge case, debugging gap, or explanation weakness.
- RED: no viable derivation, major wrong approach, incomplete implementation, or solution lookup required.

Failure codes: P pattern recognition, I invariant/reasoning, D data structure, J Java implementation, E edge case, C complexity, B debugging, M memorized/not understood.

## Spaced-Repetition Policy

| Result | Default reviews |
|---|---|
| RED | D+1 -> D+3 -> D+7 -> D+14 -> D+30 |
| YELLOW | D+2 -> D+7 -> D+14 -> D+30 |
| GREEN | D+7 -> D+30, then random mocks |

On every review, record Score, Failure, Attempts, Last Review, and Next Review. Repeated RED matters more than a first RED.

## Daily Operating Window

`09:00-12:00` 9 problems -> `12:00-13:00` lunch/walk -> `13:00-17:00` 12 problems. Hard stop at 17:00.

## ROI Tiers

- Sprint ranks 1-50: no-red-flag fundamentals; must become overwhelmingly GREEN.
- Sprint ranks 51-90: strong senior core; should recognize rapidly and usually implement.
- Sprint ranks 91-125: interview breadth and transfer.
- Sprint ranks 126-150: diminishing returns; useful, but never above repair of fundamentals.

---

## Day 1 - Monday

| Time | Sprint Rank | Source Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Attempts | Last Review | Next Review |
|---|---:|---:|---|---|---|---|---|---|---|---:|---|---|
| 09:00 | 1 | 1 | Two Sum | [Java](../../src/main/java/org/chijai/day1/Arrays/session2/Three3Sum2Sum.java) / [LC](https://leetcode.com/problems/two-sum/) | HashMap / Frequency / Set | Two pointers / hash | Use a HashMap from value to index; each number asks whether its complement was seen. |  |  | 0 |  |  |
| 09:20 | 2 | 2 | Binary Search | [Java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) / [LC](https://leetcode.com/problems/binary-search/) | Binary Search / Answer Search | Binary search invariant | Sorted order plus mid comparison proves which half cannot contain the target. |  |  | 0 |  |  |
| 09:40 | 3 | 9 | Valid Anagram | [Java](../../src/main/java/org/chijai/day3/session3/ValidAnagram.java) / [LC](https://leetcode.com/problems/valid-anagram/) | HashMap / Frequency / Set | Frequency count | Two strings are anagrams when every character count nets to zero. |  |  | 0 |  |  |
| 10:00 | 4 | 10 | Valid Palindrome | [Java](../../src/main/java/org/chijai/day3/session3/ValidPalindrome.java) | Two Pointers | Two pointers | Skip non-alphanumeric chars and compare normalized ends while pointers move inward. |  |  | 0 |  |  |
| 10:20 | 5 | 54 | Intersection Of Two Linked Lists | [Java](../../src/main/java/org/chijai/day4/LinkedList/session1/Intersection.java) / [LC](https://leetcode.com/problems/intersection-of-two-linked-lists/) | Linked List Pointers | Linked list two pointers | Switch heads at null; equal path lengths make pointers meet at intersection or null. |  |  | 0 |  |  |
| 10:40 | 6 | 61 | Rotate List | [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) / [LC](https://leetcode.com/problems/rotate-list/) | Linked List Pointers | Linked-list reversal groups | Make the list circular, then break at length - k % length. |  |  | 0 |  |  |
| 11:00 | 7 | 89 | Sliding Window Maximum | [Java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) / [LC](https://leetcode.com/problems/sliding-window-maximum/) | Stack / Monotonic Stack | Stack/queue design | A decreasing deque stores candidate indices; front is always the current window maximum. |  |  | 0 |  |  |
| 11:20 | 8 | 91 | Design Add and Search Words Data Structure | [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) / [LC](https://leetcode.com/problems/design-add-and-search-words-data-structure/) | Trie | Trie | Trie search branches only on '.', otherwise it follows exactly one child. |  |  | 0 |  |  |
| 11:40 | 9 | 4 | Product Of Array Except Self | [Java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/ProductOfArrayExceptSelf.java) / [LC](https://leetcode.com/problems/product-of-array-except-self/) | Prefix Sum / Prefix-Suffix | Prefix/suffix | Answer is product of everything left times everything right, no division needed. |  |  | 0 |  |  |
| 13:00 | 10 | 12 | Two Sum II - Input Array Is Sorted | [Java](../../src/main/java/org/chijai/day1/Arrays/session2/Three3Sum2Sum.java) / [LC](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/) | Two Pointers | Two pointers / hash | Sorted input lets left/right shrink toward the target sum. |  |  | 0 |  |  |
| 13:20 | 11 | 138 | H-Index | [Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) / [LC](https://leetcode.com/problems/h-index/) | Heap / Priority Queue | Frequency + heap/bucket | Keep only the frontier, top K, or two balanced halves instead of fully sorting each step. |  |  | 0 |  |  |
| 13:40 | 12 | 95 | Kth Largest Element In An Array | [Java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) / [LC](https://leetcode.com/problems/kth-largest-element-in-an-array/) | Heap / Priority Queue | Min-heap size K | A size-k min-heap keeps the k largest seen so far; top is kth largest. |  |  | 0 |  |  |
| 14:00 | 13 | 31 | 01 Matrix | [Java](../../src/main/java/org/chijai/day8/graph/session1/Matrix01.java) / [LC](https://leetcode.com/problems/01-matrix/) | Graph BFS / Shortest Path | Multi-source BFS | Start BFS from all zero cells; first visit gives nearest-zero distance. |  |  | 0 |  |  |
| 14:20 | 14 | 3 | Longest Substring Without Repeating Characters | [Java](../../src/main/java/org/chijai/day3/session1/LongestSubString.java) / [LC](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | Sliding Window | Sliding window / set | Window must contain unique chars; move left past duplicates. |  |  | 0 |  |  |
| 14:40 | 15 | 5 | Minimum Window Substring | [Java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) / [LC](https://leetcode.com/problems/minimum-window-substring/) | Sliding Window | Sliding window / need-have | Expand until all needed chars are covered, then shrink while still valid. |  |  | 0 |  |  |
| 15:00 | 16 | 50 | Binary Subarrays With Sum | [Java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/NiceSubArrays.java) / [LC](https://leetcode.com/problems/binary-subarrays-with-sum/) | Prefix Sum / Prefix-Suffix | Prefix/window counting | For binary arrays, exact goal count can be atMost(goal) - atMost(goal-1). |  |  | 0 |  |  |
| 15:20 | 17 | 45 | Longest Increasing Subsequence | [Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) / [LC](https://leetcode.com/problems/longest-increasing-subsequence/) | Dynamic Programming | DP / patience sorting | tails[len] stores the smallest possible tail for an increasing subsequence of that length. |  |  | 0 |  |  |
| 15:40 | 18 | 114 | Search In A Binary Search Tree | [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) / [LC](https://leetcode.com/problems/search-in-a-binary-search-tree/) | Tree DFS / Recursion | BST property | Compare target with node value and move only to the branch that can still contain it. |  |  | 0 |  |  |
| 16:00 | 19 | 53 | Ransom Note | [Java](../../src/main/java/org/chijai/day1/Arrays/session1/RansomNote.java) / [LC](https://leetcode.com/problems/ransom-note/) | HashMap / Frequency / Set | HashMap/frequency | Count magazine chars, then spend counts for ransom; fail when a needed char is missing. |  |  | 0 |  |  |
| 16:20 | 20 | 113 | Range Sum Of BST | [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) / [LC](https://leetcode.com/problems/range-sum-of-bst/) | Tree DFS / Recursion | BST property | BST ordering lets you prune subtrees outside [low, high]. |  |  | 0 |  |  |
| 16:40 | 21 | 20 | Course Schedule II | [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/course-schedule-ii/) | Topological Sort | Topological sort / cycle | A course enters the order only when its indegree drops to zero. |  |  | 0 |  |  |

Daily scoreboard: Attempted __/21; GREEN __; YELLOW __; RED __; repeated RED __; fundamental RED __.

Top 3 failure lessons: 1. ___  2. ___  3. ___

Tomorrow repair queue: 1. ___  2. ___  3. ___

---

## Day 2 - Tuesday

| Time | Sprint Rank | Source Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Attempts | Last Review | Next Review |
|---|---:|---:|---|---|---|---|---|---|---|---:|---|---|
| 09:00 | 22 | 21 | Word Ladder | [Java](../../src/main/java/org/chijai/day8/graph/session3/WordLadder.java) / [LC](https://leetcode.com/problems/word-ladder/) | Graph BFS / Shortest Path | BFS shortest path | BFS words level by level; first time reaching endWord is the shortest transformation length. |  |  | 0 |  |  |
| 09:20 | 23 | 22 | Koko Eating Bananas | [Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) / [LC](https://leetcode.com/problems/koko-eating-bananas/) | Binary Search / Answer Search | Binary search on answer | Binary search the minimum speed; if speed k works, every higher speed also works. |  |  | 0 |  |  |
| 09:40 | 24 | 82 | First Bad Version | [Java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) / [LC](https://leetcode.com/problems/first-bad-version/) | Binary Search / Answer Search | Binary search invariant | Find the first true in a false...false,true...true version predicate. |  |  | 0 |  |  |
| 10:00 | 25 | 86 | Best Time to Buy and Sell Stock | [Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java) / [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/) | Dynamic Programming | Greedy / DP states | Track the lowest price so far; today's profit is price minus that minimum. |  |  | 0 |  |  |
| 10:20 | 26 | 87 | Capacity To Ship Packages Within D Days | [Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) / [LC](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/) | Binary Search / Answer Search | Binary search on answer | Binary search minimum capacity; capacity works if one pass ships within D days. |  |  | 0 |  |  |
| 10:40 | 27 | 92 | Construct Binary Tree From Preorder And Inorder Traversal | [Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) / [LC](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/) | Tree DFS / Recursion | Tree recursion / hashmap index | Preorder first is root; inorder index splits left and right subtrees. |  |  | 0 |  |  |
| 11:00 | 28 | 93 | Binary Tree Maximum Path Sum | [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) / [LC](https://leetcode.com/problems/binary-tree-maximum-path-sum/) | Tree DFS / Recursion | Tree path DFS / global answer | Helper returns best non-splitting gain; global answer may split through node. |  |  | 0 |  |  |
| 11:20 | 29 | 6 | Reverse Linked List | [Java](../../src/main/java/org/chijai/day4/LinkedList/session1/ReverseLinkedList.java) / [LC](https://leetcode.com/problems/reverse-linked-list/) | Linked List Pointers | Pointer reversal | Reverse one edge at a time after saving next. |  |  | 0 |  |  |
| 11:40 | 30 | 7 | Linked List Cycle | [Java](../../src/main/java/org/chijai/day4/LinkedList/session1/LinkedListCycle.java) / [LC](https://leetcode.com/problems/linked-list-cycle/) | Linked List Pointers | Fast/slow pointers | Slow and fast meet only if a cycle exists. |  |  | 0 |  |  |
| 13:00 | 31 | 8 | Merge Two Sorted Lists | [Java](../../src/main/java/org/chijai/day4/LinkedList/session4/Merge2SortedLists.java) / [LC](https://leetcode.com/problems/merge-two-sorted-lists/) | Linked List Pointers | Merge / dummy node | Dummy tail repeatedly takes the smaller current node. |  |  | 0 |  |  |
| 13:20 | 32 | 94 | Task Scheduler | [Java](../../src/main/java/org/chijai/day7/session1/heap/TaskScheduler.java) / [LC](https://leetcode.com/problems/task-scheduler/) | Heap / Priority Queue | Greedy / heap | CPU idles only when the most frequent tasks cannot be spaced by cooldown gaps. |  |  | 0 |  |  |
| 13:40 | 33 | 56 | Reverse Nodes in k-Group | [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) / [LC](https://leetcode.com/problems/reverse-nodes-in-k-group/) | Linked List Pointers | Linked-list reversal groups | Only reverse a group after confirming k nodes exist. |  |  | 0 |  |  |
| 14:00 | 34 | 57 | Lowest Common Ancestor Of A Binary Search Tree | [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) / [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/) | Tree DFS / Recursion | BST property | If both targets are smaller go left, if both are larger go right, else current node is the split. |  |  | 0 |  |  |
| 14:20 | 35 | 23 | Search In Rotated Sorted Array | [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) / [LC](https://leetcode.com/problems/search-in-rotated-sorted-array/) | Binary Search / Answer Search | Binary search boundary | At every step one half is sorted; keep it only if target lies inside its bounds. |  |  | 0 |  |  |
| 14:40 | 36 | 33 | Coin Change | [Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) / [LC](https://leetcode.com/problems/coin-change/) | Dynamic Programming | Unbounded knapsack DP | dp[amount] is the fewest coins needed; each coin relaxes reachable amounts. |  |  | 0 |  |  |
| 15:00 | 37 | 131 | Implement Queue Using Stacks | [Java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) / [LC](https://leetcode.com/problems/implement-queue-using-stacks/) | Stack / Monotonic Stack | Stack/queue design | Use input stack for pushes and output stack for pops; transfer only when output is empty. |  |  | 0 |  |  |
| 15:20 | 38 | 133 | Next Greater Element I | [Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) / [LC](https://leetcode.com/problems/next-greater-element-i/) | Stack / Monotonic Stack | Stack design | Precompute next greater for nums2 with a decreasing stack, then answer nums1 by map lookup. |  |  | 0 |  |  |
| 15:40 | 39 | 32 | House Robber | [Java](../../src/main/java/org/chijai/day9/dp/session1/HouseRobber.java) / [LC](https://leetcode.com/problems/house-robber/) | Dynamic Programming | 1D DP | At each house choose max(skip current, rob current plus best before previous). |  |  | 0 |  |  |
| 16:00 | 40 | 96 | Kth Largest Element In A Stream | [Java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) / [LC](https://leetcode.com/problems/kth-largest-element-in-a-stream/) | Heap / Priority Queue | Min-heap size K | Maintain a size-k min-heap after every add; top is the kth largest in the stream. |  |  | 0 |  |  |
| 16:20 | 41 | 36 | Top K Frequent Elements | [Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) / [LC](https://leetcode.com/problems/top-k-frequent-elements/) | Heap / Priority Queue | Frequency + heap/bucket | Count frequencies, then keep only the k highest-frequency entries. |  |  | 0 |  |  |
| 16:40 | 42 | 102 | Basic Calculator | [Java](../../src/main/java/org/chijai/day5/stack/session3/BasicCalculator.java) / [LC](https://leetcode.com/problems/basic-calculator/) | Stack / Monotonic Stack | Stack / expression parsing | Use sign and stack to preserve the expression value before each parenthesis. |  |  | 0 |  |  |

Daily scoreboard: Attempted __/21; GREEN __; YELLOW __; RED __; repeated RED __; fundamental RED __.

Top 3 failure lessons: 1. ___  2. ___  3. ___

Tomorrow repair queue: 1. ___  2. ___  3. ___

---

## Day 3 - Wednesday

| Time | Sprint Rank | Source Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Attempts | Last Review | Next Review |
|---|---:|---:|---|---|---|---|---|---|---|---:|---|---|
| 09:00 | 43 | 103 | Longest Palindrome | [Java](../../src/main/java/org/chijai/day3/session3/LongestPalindrome.java) / [LC](https://leetcode.com/problems/longest-palindrome/) | HashMap / Frequency / Set | Hash/frequency | At most one character may have an odd count; pairs from all counts build the longest palindrome. |  |  | 0 |  |  |
| 09:20 | 44 | 14 | Trapping Rain Water | [Java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/TrappingRainwater.java) / [LC](https://leetcode.com/problems/trapping-rain-water/) | Two Pointers | Two pointers / stack | Water at a side depends on the smaller max boundary seen so far. |  |  | 0 |  |  |
| 09:40 | 45 | 79 | Moving Average From Data Stream | [Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) / [LC](https://leetcode.com/problems/moving-average-from-data-stream/) | Linked List Pointers | HashMap + doubly linked list | Queue last size values and running sum; average is sum divided by queue size. |  |  | 0 |  |  |
| 10:00 | 46 | 67 | Sum Root To Leaf Numbers | [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) / [LC](https://leetcode.com/problems/sum-root-to-leaf-numbers/) | Tree DFS / Recursion | Tree path DFS / global answer | Carry the number formed so far; at a leaf, add it to the total. |  |  | 0 |  |  |
| 10:20 | 47 | 117 | Recover Binary Search Tree | [Java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) / [LC](https://leetcode.com/problems/recover-binary-search-tree/) | Tree DFS / Recursion | BST inorder | Inorder traversal should be sorted; the two broken nodes appear at one or two inversions. |  |  | 0 |  |  |
| 10:40 | 48 | 116 | Amount of Time for Binary Tree to Be Infected | [Java](../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java) / [LC](https://leetcode.com/problems/amount-of-time-for-binary-tree-to-be-infected/) | Tree DFS / Recursion | Tree + graph BFS | Define exactly what the helper returns, combine left/right, and update global answer separately if needed. |  |  | 0 |  |  |
| 11:00 | 49 | 15 | Binary Tree Level Order Traversal | [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeTraversal.java) / [LC](https://leetcode.com/problems/binary-tree-level-order-traversal/) | Tree BFS / Level Order | Tree traversal | Capture queue size to process exactly one tree level at a time. |  |  | 0 |  |  |
| 11:20 | 50 | 25 | LRU Cache | [Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) / [LC](https://leetcode.com/problems/lru-cache/) | Linked List Pointers | HashMap + doubly linked list | HashMap gives O(1) lookup; doubly linked list keeps recency order. |  |  | 0 |  |  |
| 11:40 | 51 | 26 | Copy List With Random Pointer | [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/CopyListWithRandomPointer.java) | Linked List Pointers | HashMap / interleaving copy | Clone nodes then connect next/random using old-to-new mapping or interleaving. |  |  | 0 |  |  |
| 13:00 | 52 | 27 | Kth Smallest Element In A BST | [Java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) / [LC](https://leetcode.com/problems/kth-smallest-element-in-a-bst/) | Tree DFS / Recursion | BST inorder | BST inorder gives ascending values; kth visited is the answer. |  |  | 0 |  |  |
| 13:20 | 53 | 16 | Validate Binary Search Tree | [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) / [LC](https://leetcode.com/problems/validate-binary-search-tree/) | Tree DFS / Recursion | Tree DFS / stack | Every node must stay inside strict min/max bounds inherited from ancestors. |  |  | 0 |  |  |
| 13:40 | 54 | 59 | Sort Colors | [Java](../../src/main/java/org/chijai/day1/Arrays/session1/SortColors.java) / [LC](https://leetcode.com/problems/sort-colors/) | Two Pointers | Partition / Dutch flag | Dutch flag keeps < pivot, unknown, and > pivot regions with three pointers. |  |  | 0 |  |  |
| 14:00 | 55 | 68 | Network Delay Time | [Java](../../src/main/java/org/chijai/day8/graph/session2/NetworkDelayTime.java) / [LC](https://leetcode.com/problems/network-delay-time/) | Graph BFS / Shortest Path | Dijkstra / graph | Dijkstra keeps the next shortest unsettled node in a min-heap. |  |  | 0 |  |  |
| 14:20 | 56 | 17 | Lowest Common Ancestor Of A Binary Tree | [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) / [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/) | Tree DFS / Recursion | Tree DFS return contract | If left and right both return a target, current node is the split point. |  |  | 0 |  |  |
| 14:40 | 57 | 29 | Path Sum Iii | [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) / [LC](https://leetcode.com/problems/path-sum-iii/) | Tree DFS / Recursion | Tree path DFS / global answer | Use prefix sums on the root-to-current path to count paths ending at this node. |  |  | 0 |  |  |
| 15:00 | 58 | 30 | Rotting Oranges | [Java](../../src/main/java/org/chijai/day8/graph/session1/RottenOranges.java) / [LC](https://leetcode.com/problems/rotting-oranges/) | Graph BFS / Shortest Path | Multi-source BFS | All initially rotten oranges start a multi-source BFS; each level is one minute. |  |  | 0 |  |  |
| 15:20 | 59 | 34 | Subsets | [Java](../../src/main/java/org/chijai/day11/backtracking/session1/Subsets.java) / [LC](https://leetcode.com/problems/subsets/) | Backtracking / Combinatorial DFS | Backtracking subsets | Choose, recurse, undo; the path is exactly the current decision state. |  |  | 0 |  |  |
| 15:40 | 60 | 80 | Verify Preorder Serialization Of A Binary Tree | [Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) / [LC](https://leetcode.com/problems/verify-preorder-serialization-of-a-binary-tree/) | Tree DFS / Recursion | Tree recursion / hashmap index | Slots start at one; every node consumes a slot, non-null nodes create two. |  |  | 0 |  |  |
| 16:00 | 61 | 35 | Valid Parentheses | [Java](../../src/main/java/org/chijai/day5/stack/session3/ValidParentheses.java) / [LC](https://leetcode.com/problems/valid-parentheses/) | Stack / Monotonic Stack | Stack | Every closing bracket must match the most recent unmatched opening bracket. |  |  | 0 |  |  |
| 16:20 | 62 | 18 | Number Of Islands | [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) / [LC](https://leetcode.com/problems/number-of-islands/) | Graph DFS / Components | Matrix DFS/BFS components | Every time you find unvisited land, sink its whole connected component and count one island. |  |  | 0 |  |  |
| 16:40 | 63 | 72 | Minimum Height Trees | [Java](../../src/main/java/org/chijai/day8/graph/session3/MinHTree.java) / [LC](https://leetcode.com/problems/minimum-height-trees/) | Topological Sort | Topological trimming | Peel all current leaves together until one or two centroid roots remain. |  |  | 0 |  |  |

Daily scoreboard: Attempted __/21; GREEN __; YELLOW __; RED __; repeated RED __; fundamental RED __.

Top 3 failure lessons: 1. ___  2. ___  3. ___

Tomorrow repair queue: 1. ___  2. ___  3. ___

---

## Day 4 - Thursday

| Time | Sprint Rank | Source Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Attempts | Last Review | Next Review |
|---|---:|---:|---|---|---|---|---|---|---|---:|---|---|
| 09:00 | 64 | 75 | Number Of Provinces | [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) / [LC](https://leetcode.com/problems/number-of-provinces/) | Graph BFS / Shortest Path | Matrix DFS/BFS components | Each DFS/BFS from an unvisited city marks one connected province. |  |  | 0 |  |  |
| 09:20 | 65 | 38 | Meeting Rooms Ii | [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/MinimumPlatforms.java) / [LC](https://leetcode.com/problems/meeting-rooms-ii/) | Intervals / Sorting Greedy | Intervals / sorting | Sort meetings by start; a min-heap of end times counts active rooms. |  |  | 0 |  |  |
| 09:40 | 66 | 150 | Sequence Reconstruction | [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/sequence-reconstruction/) | Topological Sort | Topological sort / cycle | Use indegree or DFS states to process dependencies before dependents. |  |  | 0 |  |  |
| 10:00 | 67 | 39 | Implement Trie (Prefix Tree) | [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) / [LC](https://leetcode.com/problems/implement-trie-prefix-tree/) | Trie | Trie | Each trie node represents one prefix; terminal marks distinguish full words from prefixes. |  |  | 0 |  |  |
| 10:20 | 68 | 47 | Longest Substring With At Most K Distinct Characters | [Java](../../src/main/java/org/chijai/day3/session1/AtMostKDistinct.java) / [LC](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/) | Sliding Window | Sliding window | Keep a frequency map with at most k distinct chars; shrink until valid. |  |  | 0 |  |  |
| 10:40 | 69 | 48 | Word Search | [Java](../../src/main/java/org/chijai/day8/graph/session1/WordSearch.java) / [LC](https://leetcode.com/problems/word-search/) | Backtracking / Combinatorial DFS | DFS backtracking | Choose, recurse, undo; the path is exactly the current decision state. |  |  | 0 |  |  |
| 11:00 | 70 | 49 | Find Median From Data Stream | [Java](../../src/main/java/org/chijai/day7/session1/heap/Median.java) / [LC](https://leetcode.com/problems/find-median-from-data-stream/) | Heap / Priority Queue | Two heaps | Two heaps split lower and upper halves; median comes from heap tops. |  |  | 0 |  |  |
| 11:20 | 71 | 40 | Flood Fill | [Java](../../src/main/java/org/chijai/day8/graph/session1/FloodFill.java) / [LC](https://leetcode.com/problems/flood-fill/) | Graph DFS / Components | Matrix DFS/BFS | Recolor only the connected component matching the starting color. |  |  | 0 |  |  |
| 11:40 | 72 | 41 | Is Graph Bipartite? | [Java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) / [LC](https://leetcode.com/problems/is-graph-bipartite/) | Graph DFS / Components | BFS/DFS coloring | A graph is bipartite if every edge connects opposite colors. |  |  | 0 |  |  |
| 13:00 | 73 | 42 | Minimum Number Of Arrows To Burst Balloons | [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/MinimumPlatforms.java) / [LC](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/) | Intervals / Sorting Greedy | Intervals / sorting | Sort balloons by end; shoot at current end and start a new arrow only after it is missed. |  |  | 0 |  |  |
| 13:20 | 74 | 140 | Intervals | [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/Intervals.java) | Intervals / Sorting Greedy | Intervals / merge | Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints. |  |  | 0 |  |  |
| 13:40 | 75 | 141 | Gas Station | [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/GasStation.java) / [LC](https://leetcode.com/problems/gas-station/) | Intervals / Sorting Greedy | Greedy | Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints. |  |  | 0 |  |  |
| 14:00 | 76 | 43 | Unique Paths | [Java](../../src/main/java/org/chijai/day9/dp/session1/UniquePaths.java) / [LC](https://leetcode.com/problems/unique-paths/) | Dynamic Programming | Grid DP | Ways to a cell equal ways from top plus ways from left. |  |  | 0 |  |  |
| 14:20 | 77 | 11 | Merge K Sorted Lists | [Java](../../src/main/java/org/chijai/day4/LinkedList/session4/MergeKSortedLists.java) / [LC](https://leetcode.com/problems/merge-k-sorted-lists/) | Linked List Pointers | Heap / divide and conquer | A min-heap stores the current smallest head among k lists. |  |  | 0 |  |  |
| 14:40 | 78 | 37 | Daily Temperatures | [Java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/DailyTemperatures.java) / [LC](https://leetcode.com/problems/daily-temperatures/) | Stack / Monotonic Stack | Monotonic stack | Keep indices of days waiting for a warmer temperature; current day resolves colder previous days. |  |  | 0 |  |  |
| 15:00 | 79 | 142 | Jump Game | [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/GasStation.java) / [LC](https://leetcode.com/problems/jump-game/) | Intervals / Sorting Greedy | Greedy | Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints. |  |  | 0 |  |  |
| 15:20 | 80 | 108 | Path Sum | [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) / [LC](https://leetcode.com/problems/path-sum/) | Tree DFS / Recursion | Tree path DFS / global answer | Subtract node values along root-to-leaf paths and check target at leaf. |  |  | 0 |  |  |
| 15:40 | 81 | 137 | Top K Frequent Words | [Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) / [LC](https://leetcode.com/problems/top-k-frequent-words/) | Heap / Priority Queue | Frequency + heap/bucket | Keep only the frontier, top K, or two balanced halves instead of fully sorting each step. |  |  | 0 |  |  |
| 16:00 | 82 | 101 | Find The Index Of The First Occurrence In A String | [Java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java) / [LC](https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/) | Math / Bit / String | KMP string matching | KMP reuses the longest proper prefix that is also a suffix after a mismatch. |  |  | 0 |  |  |
| 16:20 | 83 | 104 | Longest Palindromic Substring | [Java](../../src/main/java/org/chijai/day3/session3/LongestPalindromicSubstring.java) / [LC](https://leetcode.com/problems/longest-palindromic-substring/) | Two Pointers | Expand around center | Expand around every odd and even center and keep the longest span. |  |  | 0 |  |  |
| 16:40 | 84 | 46 | Combination Sum | [Java](../../src/main/java/org/chijai/day11/backtracking/session1/CombinationSum.java) / [LC](https://leetcode.com/problems/combination-sum/) | Backtracking / Combinatorial DFS | Backtracking reuse | Choose, recurse, undo; the path is exactly the current decision state. |  |  | 0 |  |  |

Daily scoreboard: Attempted __/21; GREEN __; YELLOW __; RED __; repeated RED __; fundamental RED __.

Top 3 failure lessons: 1. ___  2. ___  3. ___

Tomorrow repair queue: 1. ___  2. ___  3. ___

---

## Day 5 - Friday

| Time | Sprint Rank | Source Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Attempts | Last Review | Next Review |
|---|---:|---:|---|---|---|---|---|---|---|---:|---|---|
| 09:00 | 85 | 98 | Word Search Ii | [Java](../../src/main/java/org/chijai/day10/session1/trie/WordSearchII.java) / [LC](https://leetcode.com/problems/word-search-ii/) | Trie | Trie + backtracking | Trie prunes dictionary prefixes while board DFS chooses, marks, explores, and unmarks cells. |  |  | 0 |  |  |
| 09:20 | 86 | 24 | Find First And Last Position Of Element In Sorted Array | [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) / [LC](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) | Binary Search / Answer Search | Binary search boundary | Run boundary binary search twice: first index >= target, and last index <= target. |  |  | 0 |  |  |
| 09:40 | 87 | 58 | Binary Tree Right Side View | [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeSideView.java) / [LC](https://leetcode.com/problems/binary-tree-right-side-view/) | Tree BFS / Level Order | Tree BFS / DFS | The last node seen at each BFS level is visible from the right. |  |  | 0 |  |  |
| 10:00 | 88 | 60 | Odd Even Linked List | [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) / [LC](https://leetcode.com/problems/odd-even-linked-list/) | Linked List Pointers | Linked-list reversal groups | Keep odd and even chains separately, then attach even head after odd tail. |  |  | 0 |  |  |
| 10:20 | 89 | 69 | Pacific Atlantic Water Flow | [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) / [LC](https://leetcode.com/problems/pacific-atlantic-water-flow/) | Graph DFS / Components | Matrix DFS/BFS components | Reverse the flow: start from both oceans and move to equal-or-higher neighboring cells. |  |  | 0 |  |  |
| 10:40 | 90 | 70 | Surrounded Regions | [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) / [LC](https://leetcode.com/problems/surrounded-regions/) | Graph DFS / Components | Matrix DFS/BFS components | Only O-regions connected to the border survive; all other O cells are captured. |  |  | 0 |  |  |
| 11:00 | 91 | 73 | Serialize And Deserialize Binary Tree | [Java](../../src/main/java/org/chijai/day6/trees/session2/SerializeAndDeserializeBinaryTree.java) / [LC](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/) | Tree DFS / Recursion | Tree BFS/DFS serialization | Include null markers so structure can be reconstructed unambiguously. |  |  | 0 |  |  |
| 11:20 | 92 | 74 | Search In Rotated Sorted Array Ii | [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) / [LC](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/) | Binary Search / Answer Search | Binary search boundary | With duplicates, shrink both ends only when left, mid, and right are equal and ordering is ambiguous. |  |  | 0 |  |  |
| 11:40 | 93 | 76 | Clone Graph | [Java](../../src/main/java/org/chijai/day8/graph/session2/CloneGraph.java) / [LC](https://leetcode.com/problems/clone-graph/) | Graph DFS / Components | Graph DFS/BFS clone | Map original node to cloned node before cloning neighbors to handle cycles. |  |  | 0 |  |  |
| 13:00 | 94 | 77 | Search Insert Position | [Java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) / [LC](https://leetcode.com/problems/search-insert-position/) | Binary Search / Answer Search | Binary search invariant | Find the first index whose value is >= target; if none, insert at n. |  |  | 0 |  |  |
| 13:20 | 95 | 81 | Find Peak Element | [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) / [LC](https://leetcode.com/problems/find-peak-element/) | Binary Search / Answer Search | Binary search boundary | Compare mid with mid+1; the rising side must contain a peak. |  |  | 0 |  |  |
| 13:40 | 96 | 88 | Minimum Number Of Days To Make M Bouquets | [Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) / [LC](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/) | Binary Search / Answer Search | Binary search on answer | Binary search days; by a given day, consecutive bloomed flowers form bouquets greedily. |  |  | 0 |  |  |
| 14:00 | 97 | 90 | Construct Binary Tree From Inorder And Postorder Traversal | [Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) / [LC](https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/) | Tree DFS / Recursion | Tree recursion / hashmap index | Postorder last is root; inorder index splits left and right subtrees. |  |  | 0 |  |  |
| 14:20 | 98 | 97 | Time Based Key Value Store | [Java](../../src/main/java/org/chijai/day2/session3/TimeBasedKeyValueStore.java) / [LC](https://leetcode.com/problems/time-based-key-value-store/) | Binary Search / Answer Search | HashMap + binary search | Map each key to timestamped values in order; binary search finds latest timestamp <= query. |  |  | 0 |  |  |
| 14:40 | 99 | 99 | Next Greater Element Ii | [Java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/NextGreaterElement.java) / [LC](https://leetcode.com/problems/next-greater-element-ii/) | Stack / Monotonic Stack | Monotonic stack | Loop twice over the circular array while a decreasing stack waits for next greater values. |  |  | 0 |  |  |
| 15:00 | 100 | 100 | Evaluate Reverse Polish Notation | [Java](../../src/main/java/org/chijai/day5/stack/session3/EvalRPN.java) / [LC](https://leetcode.com/problems/evaluate-reverse-polish-notation/) | Stack / Monotonic Stack | Stack | Postfix expression evaluates when each operator consumes the latest two operands from a stack. |  |  | 0 |  |  |
| 15:20 | 101 | 105 | Count Number Of Nice Subarrays | [Java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/NiceSubArrays.java) / [LC](https://leetcode.com/problems/count-number-of-nice-subarrays/) | Sliding Window | Prefix/window counting | Exactly k odds equals atMost(k) minus atMost(k-1), or prefix count of odd count. |  |  | 0 |  |  |
| 15:40 | 102 | 106 | Middle Of Linked List | [Java](../../src/main/java/org/chijai/day4/LinkedList/session4/MiddleOfLinkedList.java) | Linked List Pointers | Fast/slow pointers | Name every pointer, save next before rewiring, and return the real new head. |  |  | 0 |  |  |
| 16:00 | 103 | 107 | Kth Smallest Element In BST | [Java](../../src/main/java/org/chijai/day6/trees/session3/KthSmallestElementInBST.java) | Tree DFS / Recursion | BST inorder | Define exactly what the helper returns, combine left/right, and update global answer separately if needed. |  |  | 0 |  |  |
| 16:20 | 104 | 109 | Binary Tree Postorder Traversal | [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) / [LC](https://leetcode.com/problems/binary-tree-postorder-traversal/) | Tree DFS / Recursion | Tree DFS / stack | Postorder visits children before the node, useful when parent depends on subtree results. |  |  | 0 |  |  |
| 16:40 | 105 | 110 | Binary Tree Preorder Traversal | [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) / [LC](https://leetcode.com/problems/binary-tree-preorder-traversal/) | Tree DFS / Recursion | Tree DFS / stack | Preorder visits node before children, useful for serialization and copying structure. |  |  | 0 |  |  |

Daily scoreboard: Attempted __/21; GREEN __; YELLOW __; RED __; repeated RED __; fundamental RED __.

Top 3 failure lessons: 1. ___  2. ___  3. ___

Tomorrow repair queue: 1. ___  2. ___  3. ___

---

## Day 6 - Saturday

| Time | Sprint Rank | Source Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Attempts | Last Review | Next Review |
|---|---:|---:|---|---|---|---|---|---|---|---:|---|---|
| 09:00 | 106 | 111 | Insert Into A Binary Search Tree | [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) / [LC](https://leetcode.com/problems/insert-into-a-binary-search-tree/) | Tree DFS / Recursion | BST property | Use BST ordering to walk one branch until a null child is found, then insert there. |  |  | 0 |  |  |
| 09:20 | 107 | 112 | Minimum Absolute Difference In BST | [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) / [LC](https://leetcode.com/problems/minimum-absolute-difference-in-bst/) | Tree DFS / Recursion | BST property | BST inorder is sorted, so minimum difference is between adjacent inorder values. |  |  | 0 |  |  |
| 09:40 | 108 | 115 | All Nodes Distance K in Binary Tree | [Java](../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java) / [LC](https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/) | Tree DFS / Recursion | Tree + graph BFS | Define exactly what the helper returns, combine left/right, and update global answer separately if needed. |  |  | 0 |  |  |
| 10:00 | 109 | 118 | Binary Search Tree Iterator | [Java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) / [LC](https://leetcode.com/problems/binary-search-tree-iterator/) | Tree DFS / Recursion | BST inorder | Maintain a stack of the current left spine so next() returns the next inorder value lazily. |  |  | 0 |  |  |
| 10:20 | 110 | 119 | Convert BST To Greater Tree | [Java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) / [LC](https://leetcode.com/problems/convert-bst-to-greater-tree/) | Tree DFS / Recursion | BST inorder | Reverse inorder visits larger values first, so a running sum can rewrite each node. |  |  | 0 |  |  |
| 10:40 | 111 | 120 | K Highest Ranked Items Within A Price Range | [Java](../../src/main/java/org/chijai/day8/graph/session3/KHighestRankedItemsWithinAPriceRange.java) / [LC](https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/) | Graph BFS / Shortest Path | BFS + sorting | BFS by distance, collecting valid items and sorting tie-breaks by price,row,col. |  |  | 0 |  |  |
| 11:00 | 112 | 121 | Number Of Closed Islands | [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) / [LC](https://leetcode.com/problems/number-of-closed-islands/) | Graph DFS / Components | Matrix DFS/BFS components | A closed island is a land component that never touches the grid boundary. |  |  | 0 |  |  |
| 11:20 | 113 | 122 | Max Area Of Island | [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) / [LC](https://leetcode.com/problems/max-area-of-island/) | Graph DFS / Components | Matrix DFS/BFS components | DFS each land component and return its cell count; keep the maximum. |  |  | 0 |  |  |
| 11:40 | 114 | 123 | Graph Valid Tree | [Java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) / [LC](https://leetcode.com/problems/graph-valid-tree/) | Graph DFS / Components | BFS/DFS coloring | Own each component or path with visited state so one traversal fully accounts for it. |  |  | 0 |  |  |
| 13:00 | 115 | 124 | Possible Bipartition | [Java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) / [LC](https://leetcode.com/problems/possible-bipartition/) | Graph DFS / Components | BFS/DFS coloring | Own each component or path with visited state so one traversal fully accounts for it. |  |  | 0 |  |  |
| 13:20 | 116 | 125 | Redundant Connection | [Java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) / [LC](https://leetcode.com/problems/redundant-connection/) | Graph DFS / Components | BFS/DFS coloring | Own each component or path with visited state so one traversal fully accounts for it. |  |  | 0 |  |  |
| 13:40 | 117 | 126 | Coloring A Border | [Java](../../src/main/java/org/chijai/day8/graph/session1/ColoringABorder.java) / [LC](https://leetcode.com/problems/coloring-a-border/) | Graph DFS / Components | Matrix DFS | Only cells on the component boundary get recolored; interior cells keep original color. |  |  | 0 |  |  |
| 14:00 | 118 | 127 | Sqrtx | [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) / [LC](https://leetcode.com/problems/sqrtx/) | Binary Search / Answer Search | Binary search boundary | Find the largest integer mid whose square is <= x. |  |  | 0 |  |  |
| 14:20 | 119 | 128 | Largest Rectangle | [Java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java) | Stack / Monotonic Stack | Monotonic stack | Keep pending openings, operands, or monotonic candidates until the current item resolves them. |  |  | 0 |  |  |
| 14:40 | 120 | 129 | Min Stack | [Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) / [LC](https://leetcode.com/problems/min-stack/) | Stack / Monotonic Stack | Stack design | Store the current minimum with each push, or keep a second stack of minimums. |  |  | 0 |  |  |
| 15:00 | 121 | 130 | Max Stack | [Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) / [LC](https://leetcode.com/problems/max-stack/) | Stack / Monotonic Stack | Stack design | Maintain stack order plus a way to locate/remove the current maximum. |  |  | 0 |  |  |
| 15:20 | 122 | 132 | Implement Stack Using Queues | [Java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) / [LC](https://leetcode.com/problems/implement-stack-using-queues/) | Stack / Monotonic Stack | Stack/queue design | After each push, rotate the queue so the newest element is at the front. |  |  | 0 |  |  |
| 15:40 | 123 | 134 | Online Stock Span | [Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) / [LC](https://leetcode.com/problems/online-stock-span/) | Stack / Monotonic Stack | Stack design | A decreasing stack of price/span pairs merges all previous prices <= current price. |  |  | 0 |  |  |
| 16:00 | 124 | 135 | Meeting Room | [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/MeetingRoom.java) | Heap / Priority Queue | Intervals / heap | Keep only the frontier, top K, or two balanced halves instead of fully sorting each step. |  |  | 0 |  |  |
| 16:20 | 125 | 136 | K Closest Points To Origin | [Java](../../src/main/java/org/chijai/day7/session1/heap/KClosestPointsToOrigin.java) / [LC](https://leetcode.com/problems/k-closest-points-to-origin/) | Heap / Priority Queue | Heap / quickselect | Keep the k smallest squared distances; compare without taking square roots. |  |  | 0 |  |  |
| 16:40 | 126 | 139 | Sort Characters By Frequency | [Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) / [LC](https://leetcode.com/problems/sort-characters-by-frequency/) | Heap / Priority Queue | Frequency + heap/bucket | Frequency map plus bucket/heap outputs characters from highest count to lowest. |  |  | 0 |  |  |

Daily scoreboard: Attempted __/21; GREEN __; YELLOW __; RED __; repeated RED __; fundamental RED __.

Top 3 failure lessons: 1. ___  2. ___  3. ___

Tomorrow repair queue: 1. ___  2. ___  3. ___

---

## Day 7 - Sunday

| Time | Sprint Rank | Source Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Attempts | Last Review | Next Review |
|---|---:|---:|---|---|---|---|---|---|---|---:|---|---|
| 09:00 | 127 | 143 | Car Pooling | [Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/MinimumPlatforms.java) / [LC](https://leetcode.com/problems/car-pooling/) | Intervals / Sorting Greedy | Intervals / sorting | Treat each pickup/dropoff as passenger-count delta and ensure capacity is never exceeded. |  |  | 0 |  |  |
| 09:20 | 128 | 144 | Partition Labels | [Java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java) / [LC](https://leetcode.com/problems/partition-labels/) | Intervals / Sorting Greedy | Greedy last-occurrence boundary | Close a partition only when the current index reaches the farthest last occurrence of all chars seen so far. |  |  | 0 |  |  |
| 09:40 | 129 | 145 | Letter Combinations Of A Phone Number | [Java](../../src/main/java/org/chijai/day11/backtracking/session1/LetterCombinationsOfAPhoneNumber.java) / [LC](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) | Backtracking / Combinatorial DFS | Backtracking / mapping | Choose, recurse, undo; the path is exactly the current decision state. |  |  | 0 |  |  |
| 10:00 | 130 | 146 | Permutations | [Java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) / [LC](https://leetcode.com/problems/permutations/) | Backtracking / Combinatorial DFS | Backtracking permutations | Choose, recurse, undo; the path is exactly the current decision state. |  |  | 0 |  |  |
| 10:20 | 131 | 147 | Parallel Courses | [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/parallel-courses/) | Topological Sort | Topological sort / cycle | Use indegree or DFS states to process dependencies before dependents. |  |  | 0 |  |  |
| 10:40 | 132 | 148 | Alien Dictionary | [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/alien-dictionary/) | Topological Sort | Topological sort / cycle | Use indegree or DFS states to process dependencies before dependents. |  |  | 0 |  |  |
| 11:00 | 133 | 149 | Find Eventual Safe States | [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/find-eventual-safe-states/) | Topological Sort | Topological sort / cycle | Use indegree or DFS states to process dependencies before dependents. |  |  | 0 |  |  |
| 11:20 | 134 | 19 | Course Schedule | [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/course-schedule/) | Topological Sort | Topological sort / cycle | A course is unlocked only when its indegree becomes zero. |  |  | 0 |  |  |
| 11:40 | 135 | 28 | Diameter of Binary Tree | [Java](../../src/main/java/org/chijai/day6/trees/session3/BinaryTree.java) / [LC](https://leetcode.com/problems/diameter-of-binary-tree/) | Tree DFS / Recursion | Core tree patterns | Diameter through a node is left height plus right height; return height upward. |  |  | 0 |  |  |
| 13:00 | 136 | 44 | Partition Equal Subset Sum | [Java](../../src/main/java/org/chijai/day9/dp/session2/PartitionEqualSubsetSum.java) / [LC](https://leetcode.com/problems/partition-equal-subset-sum/) | Dynamic Programming | 0/1 knapsack DP | Partition is possible only if some subset reaches total/2. |  |  | 0 |  |  |
| 13:20 | 137 | 51 | Majority Element | [Java](../../src/main/java/org/chijai/day1/Arrays/session2/MajorityElement.java) / [LC](https://leetcode.com/problems/majority-element/) | HashMap / Frequency / Set | Boyer-Moore / frequency | Boyer-Moore cancels different values; surviving candidate is majority after optional verification. |  |  | 0 |  |  |
| 13:40 | 138 | 52 | Find All Anagrams In A String | [Java](../../src/main/java/org/chijai/day3/session1/FindAllAnagramsInAString.java) | Sliding Window | Sliding window frequency | Slide a fixed-size frequency window and record starts where counts match p. |  |  | 0 |  |  |
| 14:00 | 139 | 55 | Linked List Cycle Ii | [Java](../../src/main/java/org/chijai/day4/LinkedList/session4/LinkedListCycleII.java) | Linked List Pointers | Floyd cycle entry | After slow/fast meet, move one pointer from head and both one step to find entry. |  |  | 0 |  |  |
| 14:20 | 140 | 62 | Swap Nodes In Pairs | [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) / [LC](https://leetcode.com/problems/swap-nodes-in-pairs/) | Linked List Pointers | Linked-list reversal groups | Dummy node lets you swap each adjacent pair without special-casing head. |  |  | 0 |  |  |
| 14:40 | 141 | 63 | First Unique Number | [Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) / [LC](https://leetcode.com/problems/first-unique-number/) | Linked List Pointers | HashMap + doubly linked list | Queue/list stores arrival order; counts decide whether the front is still unique. |  |  | 0 |  |  |
| 15:00 | 142 | 64 | Binary Tree Inorder Traversal | [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) / [LC](https://leetcode.com/problems/binary-tree-inorder-traversal/) | Tree DFS / Recursion | Tree DFS / stack | Inorder is left, node, right; for BST it yields sorted order. |  |  | 0 |  |  |
| 15:20 | 143 | 65 | Invert Binary Tree | [Java](../../src/main/java/org/chijai/day6/trees/session3/InvertBinaryTree.java) / [LC](https://leetcode.com/problems/invert-binary-tree/) | Tree DFS / Recursion | Tree DFS/BFS | Swap left and right at every node. |  |  | 0 |  |  |
| 15:40 | 144 | 66 | Construct Binary Search Tree From Preorder Traversal | [Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) / [LC](https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/) | Tree DFS / Recursion | Tree recursion / hashmap index | Preorder root plus BST bounds tells where each next value belongs. |  |  | 0 |  |  |
| 16:00 | 145 | 71 | Accounts Merge | [Java](../../src/main/java/org/chijai/day8/graph/session3/AccountsMerge.java) / [LC](https://leetcode.com/problems/accounts-merge/) | Union Find / DSU | Union Find / graph | Represent components with parent links; union merges and failed union detects cycles. |  |  | 0 |  |  |
| 16:20 | 146 | 78 | Design Browser History | [Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) / [LC](https://leetcode.com/problems/design-browser-history/) | Linked List Pointers | HashMap + doubly linked list | Back/forward are pointer moves over a history chain; visit drops forward history. |  |  | 0 |  |  |
| 16:40 | 147 | 83 | Split Array Largest Sum | [Java](../../src/main/java/org/chijai/day2/session2/AGGRCOW.java) / [LC](https://leetcode.com/problems/split-array-largest-sum/) | Binary Search / Answer Search | Binary search on answer | Binary search the smallest allowed subarray sum that can split into at most m pieces. |  |  | 0 |  |  |

Daily scoreboard: Attempted __/21; GREEN __; YELLOW __; RED __; repeated RED __; fundamental RED __.

Top 3 failure lessons: 1. ___  2. ___  3. ___

Tomorrow repair queue: 1. ___  2. ___  3. ___

---

## Overflow / Completion - Sprint Ranks 148-150

These three are deliberately outside the 147 fixed slots. Complete only after higher-priority repair; there is no interview benefit in forcing superficial completion.

| Time | Sprint Rank | Source Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Attempts | Last Review | Next Review |
|---|---:|---:|---|---|---|---|---|---|---|---:|---|---|
| - | 148 | 84 | Maximum Profit In Job Scheduling | [Java](../../src/main/java/org/chijai/day2/session3/MaximumProfitInJobScheduling.java) / [LC](https://leetcode.com/problems/maximum-profit-in-job-scheduling/) | Dynamic Programming | DP + binary search | Sort jobs by end time; dp[i] is best profit up to i, with binary search for compatible previous job. |  |  | 0 |  |  |
| - | 149 | 85 | Kadane Max Sub Array | [Java](../../src/main/java/org/chijai/day9/dp/session1/KadaneMaxSubArray.java) | Dynamic Programming | Kadane / DP | Best subarray ending here is either current alone or previous best ending here plus current. |  |  | 0 |  |  |
| - | 150 | 13 | Container With Most Water | [Java](../../src/main/java/org/chijai/day1/Arrays/session2/ContainerWithMostWater.java) | Two Pointers | Two pointers | Area is limited by shorter wall, so move the shorter side inward. |  |  | 0 |  |  |

## Interview-Ready Gate

- [ ] Sprint ranks 1-50 are overwhelmingly GREEN with no recurring fundamental RED.
- [ ] Random/rephrased problem family is recognized quickly without category hints.
- [ ] Blank-editor Java implementation is reliable.
- [ ] Brute force -> optimized transition and invariant can be explained.
- [ ] Time/space complexity is correct.
- [ ] Edge cases are generated independently.
- [ ] Ordinary bugs are diagnosed calmly.
- [ ] Requirement mutations can be discussed.
- [ ] Random timed mocks are consistently passing.

## After The Sprint

Stop accumulating sheets. Shift to performance mode: due spaced reviews -> random unseen/rephrased DSA -> timed coding -> requirement mutation/debugging -> LLD mock -> HLD mock.

Execution mantra: `MASTER FUNDAMENTALS -> RETRIEVE -> FAIL FAST -> RECORD -> SPACE -> REPAIR -> RANDOMIZE -> MOCK -> INTERVIEW`