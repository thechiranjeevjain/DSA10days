# LeetCode Solved Index

Recursive source scan: this is the book-style table of contents for LeetCode problems found in Java source files by full LeetCode URL or explicit LC problem number.

Regenerate it with `dsa-review/scripts/build-interview-cockpit.cmd`, `dsa-review/scripts/build-interview-cockpit.sh`, or `verify-all.ps1` after adding or editing Java solution files. Add a full LeetCode URL or cataloged LC problem number when a file contains a solved problem.

Use [Zero To Hero Ranked Table](01_ZERO_TO_HERO_RANKED_TABLE.md) for interview crunch order. Use this file when you want the complete source-backed LeetCode inventory.

| Metric | Count |
|---|---:|
| Unique LeetCode problems found recursively | 214 |
| Also present in interview-ranked cockpit | 192 |
| Extra source-discovered problems | 22 |
| Problems appearing in multiple Java files | 57 |

## Table Of Contents

- [HashMap / Frequency / Set (5)](#hashmap-frequency-set)
- [Binary Search / Answer Search (13)](#binary-search-answer-search)
- [Sliding Window (12)](#sliding-window)
- [Prefix Sum / Prefix-Suffix (2)](#prefix-sum-prefixsuffix)
- [Linked List Pointers (11)](#linked-list-pointers)
- [Heap / Priority Queue (13)](#heap-priority-queue)
- [Two Pointers (4)](#two-pointers)
- [Tree BFS / Level Order (2)](#tree-bfs-level-order)
- [Tree DFS / Recursion (31)](#tree-dfs-recursion)
- [Graph DFS / Components (12)](#graph-dfs-components)
- [Topological Sort (9)](#topological-sort)
- [Graph BFS / Shortest Path (6)](#graph-bfs-shortest-path)
- [Dynamic Programming (30)](#dynamic-programming)
- [Backtracking / Combinatorial DFS (8)](#backtracking-combinatorial-dfs)
- [Stack / Monotonic Stack (15)](#stack-monotonic-stack)
- [Trie (13)](#trie)
- [Intervals / Sorting Greedy (7)](#intervals-sorting-greedy)
- [Union Find / DSU (1)](#union-find-dsu)
- [Design Data Structures (7)](#design-data-structures)
- [Math / Bit / String (7)](#math-bit-string)
- [Basics / Implementation (6)](#basics-implementation)

## HashMap / Frequency / Set

### Two pointers / hash

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 1 | 1 | Two Sum | [LC](https://leetcode.com/problems/two-sum/) | [Three3Sum2Sum.java](../../src/main/java/org/chijai/day1/Arrays/session2/Three3Sum2Sum.java) |

### Frequency count

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 9 | 9 | Valid Anagram | [LC](https://leetcode.com/problems/valid-anagram/) | [ValidAnagram.java](../../src/main/java/org/chijai/day3/session3/ValidAnagram.java) |

### Boyer-Moore / frequency

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 48 | 51 | Majority Element | [LC](https://leetcode.com/problems/majority-element/) | [MajorityElement.java](../../src/main/java/org/chijai/day1/Arrays/session2/MajorityElement.java) |

### HashMap/frequency

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 49 | 53 | Ransom Note | [LC](https://leetcode.com/problems/ransom-note/) | [RansomNote.java](../../src/main/java/org/chijai/day1/Arrays/session1/RansomNote.java) |

### Hash/frequency

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 98 | 104 | Longest Palindrome | [LC](https://leetcode.com/problems/longest-palindrome/) | [LongestPalindrome.java](../../src/main/java/org/chijai/day3/session3/LongestPalindrome.java) |


## Binary Search / Answer Search

### Binary search invariant

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 2 | 2 | Binary Search | [LC](https://leetcode.com/problems/binary-search/) | [BinarySearch.java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) |
| 71 | 76 | Search Insert Position | [LC](https://leetcode.com/problems/search-insert-position/) | [BinarySearch.java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java), [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 76 | 81 | First Bad Version | [LC](https://leetcode.com/problems/first-bad-version/) | [BinarySearch.java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) |

### Binary search on answer

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 20 | 22 | Koko Eating Bananas | [LC](https://leetcode.com/problems/koko-eating-bananas/) | [AGGRCOW.java](../../src/main/java/org/chijai/day2/session2/AGGRCOW.java), [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) |
| 77 | 82 | Split Array Largest Sum | [LC](https://leetcode.com/problems/split-array-largest-sum/) | [AGGRCOW.java](../../src/main/java/org/chijai/day2/session2/AGGRCOW.java), [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) |
| 81 | 87 | Capacity To Ship Packages Within D Days | [LC](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/) | [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) |
| 82 | 88 | Minimum Number Of Days To Make M Bouquets | [LC](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/) | [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) |

### Binary search boundary

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 21 | 23 | Search In Rotated Sorted Array | [LC](https://leetcode.com/problems/search-in-rotated-sorted-array/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 22 | 24 | Find First And Last Position Of Element In Sorted Array | [LC](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 68 | 73 | Search In Rotated Sorted Array II | [LC](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 75 | 80 | Find Peak Element | [LC](https://leetcode.com/problems/find-peak-element/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 120 | 127 | Sqrtx | [LC](https://leetcode.com/problems/sqrtx/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |

### HashMap + binary search

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 92 | 98 | Time Based Key Value Store | [LC](https://leetcode.com/problems/time-based-key-value-store/) | [TimeBasedKeyValueStore.java](../../src/main/java/org/chijai/day2/session3/TimeBasedKeyValueStore.java) |


## Sliding Window

### Sliding window / set

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 3 | 3 | Longest Substring Without Repeating Characters | [LC](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | [CountUniqueChars.java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java), [LongestSubString.java](../../src/main/java/org/chijai/day3/session1/LongestSubString.java), [LongestSubstringVariations.java](../../src/main/java/org/chijai/day3/session1/LongestSubstringVariations.java), [CountUniqueChars.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/CountUniqueChars.java) |

### Sliding window / need-have

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 5 | 5 | Minimum Window Substring | [LC](https://leetcode.com/problems/minimum-window-substring/) | [LongestSubString.java](../../src/main/java/org/chijai/day3/session1/LongestSubString.java), [MinimumWindowSubstring.java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) |

### Sliding window

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 44 | 47 | Longest Substring With At Most K Distinct Characters | [LC](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/) | [AtMostKDistinct.java](../../src/main/java/org/chijai/day3/session1/AtMostKDistinct.java), [LongestSubString.java](../../src/main/java/org/chijai/day3/session1/LongestSubString.java) |
| 193 | - | Constrained Subsequence Sum | [LC](https://leetcode.com/problems/constrained-subsequence-sum/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) |
| 194 | - | Jump Game VI | [LC](https://leetcode.com/problems/jump-game-vi/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) |
| 195 | - | Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit | [LC](https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) |
| 196 | - | Longest Repeating Character Replacement | [LC](https://leetcode.com/problems/longest-repeating-character-replacement/) | [LongestRepeatingCharacterReplacement.java](../../src/main/java/org/chijai/day3/session1/LongestRepeatingCharacterReplacement.java) |
| 197 | - | Max Value of Equation | [LC](https://leetcode.com/problems/max-value-of-equation/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) |
| 198 | - | Maximum Number of Robots Within Budget | [LC](https://leetcode.com/problems/maximum-number-of-robots-within-budget/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) |
| 199 | - | Shortest Subarray with Sum at Least K | [LC](https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) |

### Queue / stream

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 72 | 77 | Moving Average From Data Stream | [LC](https://leetcode.com/problems/moving-average-from-data-stream/) | [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java), [MovingAverage.java](../../src/main/java/org/chijai/day7/session1/heap/MovingAverage.java), [MovingAverageFromDataStream.java](../../src/main/java/org/chijai/trading/MovingAverageFromDataStream.java) |

### Prefix/window counting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 100 | 106 | Count Number Of Nice Subarrays | [LC](https://leetcode.com/problems/count-number-of-nice-subarrays/) | [NiceSubArrays.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/NiceSubArrays.java) |


## Prefix Sum / Prefix-Suffix

### Prefix/suffix

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 4 | 4 | Product Of Array Except Self | [LC](https://leetcode.com/problems/product-of-array-except-self/) | [ProductOfArrayExceptSelf.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/ProductOfArrayExceptSelf.java) |

### Prefix/window counting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 47 | 50 | Binary Subarrays With Sum | [LC](https://leetcode.com/problems/binary-subarrays-with-sum/) | [NiceSubArrays.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/NiceSubArrays.java) |


## Linked List Pointers

### Pointer reversal

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 6 | 6 | Reverse Linked List | [LC](https://leetcode.com/problems/reverse-linked-list/) | [ReverseLinkedList.java](../../src/main/java/org/chijai/day4/LinkedList/session1/ReverseLinkedList.java) |

### Fast/slow pointers

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 7 | 7 | Linked List Cycle | [LC](https://leetcode.com/problems/linked-list-cycle/) | [LinkedListCycle.java](../../src/main/java/org/chijai/day4/LinkedList/session1/LinkedListCycle.java) |

### Merge / dummy node

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 8 | 8 | Merge Two Sorted Lists | [LC](https://leetcode.com/problems/merge-two-sorted-lists/) | [Merge2SortedLists.java](../../src/main/java/org/chijai/day4/LinkedList/session4/Merge2SortedLists.java) |

### HashMap + doubly linked list

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 23 | 25 | LRU Cache | [LC](https://leetcode.com/problems/lru-cache/) | [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) |
| 73 | 78 | Design Browser History | [LC](https://leetcode.com/problems/design-browser-history/) | [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) |

### Linked list two pointers

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 50 | 54 | Intersection Of Two Linked Lists | [LC](https://leetcode.com/problems/intersection-of-two-linked-lists/) | [Intersection.java](../../src/main/java/org/chijai/day4/LinkedList/session1/Intersection.java) |

### Linked-list reversal groups

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 51 | 56 | Reverse Nodes in k-Group | [LC](https://leetcode.com/problems/reverse-nodes-in-k-group/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |
| 55 | 60 | Odd Even Linked List | [LC](https://leetcode.com/problems/odd-even-linked-list/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |
| 56 | 61 | Rotate List | [LC](https://leetcode.com/problems/rotate-list/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |
| 57 | 62 | Swap Nodes In Pairs | [LC](https://leetcode.com/problems/swap-nodes-in-pairs/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |
| 180 | 190 | Reverse Linked List II | [LC](https://leetcode.com/problems/reverse-linked-list-ii/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |


## Heap / Priority Queue

### Heap / divide and conquer

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 10 | 11 | Merge K Sorted Lists | [LC](https://leetcode.com/problems/merge-k-sorted-lists/) | [MergeKSortedLists.java](../../src/main/java/org/chijai/day4/LinkedList/session4/MergeKSortedLists.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) |

### Frequency + heap/bucket

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 33 | 36 | Top K Frequent Elements | [LC](https://leetcode.com/problems/top-k-frequent-elements/) | [HeapSort.java](../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java), [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java), [TopKFrequentTransactions.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentTransactions.java), [TopKFrequentElements.java](../../src/main/java/org/chijai/trading/TopKFrequentElements.java) |
| 128 | 136 | Top K Frequent Words | [LC](https://leetcode.com/problems/top-k-frequent-words/) | [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) |
| 129 | 137 | H-Index | [LC](https://leetcode.com/problems/h-index/) | [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) |
| 130 | 138 | Sort Characters By Frequency | [LC](https://leetcode.com/problems/sort-characters-by-frequency/) | [HeapSort.java](../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java), [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) |

### Intervals / heap

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 35 | 38 | Meeting Rooms II | [LC](https://leetcode.com/problems/meeting-rooms-ii/) | [IntervalActiveMinHeap.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalActiveMinHeap.java) |

### Two heaps

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 46 | 49 | Find Median From Data Stream | [LC](https://leetcode.com/problems/find-median-from-data-stream/) | [HeapSort.java](../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java), [Median.java](../../src/main/java/org/chijai/day7/session1/heap/Median.java) |

### Greedy / heap

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 89 | 95 | Task Scheduler | [LC](https://leetcode.com/problems/task-scheduler/) | [TaskScheduler.java](../../src/main/java/org/chijai/day7/session1/heap/TaskScheduler.java) |

### Min-heap size K

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 90 | 96 | Kth Largest Element In An Array | [LC](https://leetcode.com/problems/kth-largest-element-in-an-array/) | [HeapSort.java](../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java), [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) |
| 91 | 97 | Kth Largest Element In A Stream | [LC](https://leetcode.com/problems/kth-largest-element-in-a-stream/) | [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java), [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) |

### Heap / quickselect

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 127 | 135 | K Closest Points To Origin | [LC](https://leetcode.com/problems/k-closest-points-to-origin/) | [KClosestPointsToOrigin.java](../../src/main/java/org/chijai/day7/session1/heap/KClosestPointsToOrigin.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java), [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) |

### Heap / Priority Queue

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 202 | - | Ipo | [LC](https://leetcode.com/problems/ipo/) | [IPO.java](../../src/main/java/org/chijai/day7/session1/heap/IPO.java) |
| 203 | - | Sliding Window Median | [LC](https://leetcode.com/problems/sliding-window-median/) | [SlidingWindowMedian.java](../../src/main/java/org/chijai/day7/session1/heap/SlidingWindowMedian.java) |


## Two Pointers

### Two pointers / hash

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 11 | 12 | Two Sum II - Input Array Is Sorted | [LC](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/) | [Three3Sum2Sum.java](../../src/main/java/org/chijai/day1/Arrays/session2/Three3Sum2Sum.java) |

### Two pointers / stack

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 12 | 14 | Trapping Rain Water | [LC](https://leetcode.com/problems/trapping-rain-water/) | [TrappingRainwater.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/TrappingRainwater.java) |

### Partition / Dutch flag

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 54 | 59 | Sort Colors | [LC](https://leetcode.com/problems/sort-colors/) | [SortColors.java](../../src/main/java/org/chijai/day1/Arrays/session1/SortColors.java) |

### Expand around center

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 99 | 105 | Longest Palindromic Substring | [LC](https://leetcode.com/problems/longest-palindromic-substring/) | [LongestPalindromicSubstring.java](../../src/main/java/org/chijai/day3/session3/LongestPalindromicSubstring.java) |


## Tree BFS / Level Order

### Tree traversal

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 13 | 15 | Binary Tree Level Order Traversal | [LC](https://leetcode.com/problems/binary-tree-level-order-traversal/) | [BinaryTreeTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeTraversal.java), [BurnBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java) |

### Tree BFS / DFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 53 | 58 | Binary Tree Right Side View | [LC](https://leetcode.com/problems/binary-tree-right-side-view/) | [BinaryTreeSideView.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeSideView.java) |


## Tree DFS / Recursion

### Tree DFS / stack

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 14 | 16 | Validate Binary Search Tree | [LC](https://leetcode.com/problems/validate-binary-search-tree/) | [BinaryTreeInorderTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java), [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java), [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java), [ValidateBST.java](../../src/main/java/org/chijai/day6/trees/session3/ValidateBST.java) |
| 58 | 63 | Binary Tree Inorder Traversal | [LC](https://leetcode.com/problems/binary-tree-inorder-traversal/) | [BinaryTreeInorderTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) |
| 102 | 109 | Binary Tree Postorder Traversal | [LC](https://leetcode.com/problems/binary-tree-postorder-traversal/) | [BinaryTreeInorderTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) |
| 103 | 110 | Binary Tree Preorder Traversal | [LC](https://leetcode.com/problems/binary-tree-preorder-traversal/) | [BinaryTreeInorderTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) |

### Tree DFS return contract

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 15 | 17 | Lowest Common Ancestor Of A Binary Tree | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/) | [LCA.java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) |
| 182 | 192 | Lowest Common Ancestor Of A Binary Tree II | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-ii/) | [LCA.java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) |
| 183 | 193 | Lowest Common Ancestor Of A Binary Tree III | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/) | [LCA.java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) |
| 184 | 194 | Lowest Common Ancestor Of A Binary Tree IV | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iv/) | [LCA.java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) |

### BST inorder

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 24 | 27 | Kth Smallest Element in a BST | [LC](https://leetcode.com/problems/kth-smallest-element-in-a-bst/) | [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java), [KthSmallestElementInBST.java](../../src/main/java/org/chijai/day6/trees/session3/KthSmallestElementInBST.java) |
| 110 | 117 | Recover Binary Search Tree | [LC](https://leetcode.com/problems/recover-binary-search-tree/) | [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) |
| 111 | 118 | Binary Search Tree Iterator | [LC](https://leetcode.com/problems/binary-search-tree-iterator/) | [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) |
| 112 | 119 | Convert BST To Greater Tree | [LC](https://leetcode.com/problems/convert-bst-to-greater-tree/) | [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) |

### Core tree patterns

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 25 | 28 | Diameter of Binary Tree | [LC](https://leetcode.com/problems/diameter-of-binary-tree/) | [BinaryTree.java](../../src/main/java/org/chijai/day6/trees/session3/BinaryTree.java) |

### Tree path DFS / global answer

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 26 | 29 | Path Sum III | [LC](https://leetcode.com/problems/path-sum-iii/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |
| 61 | 66 | Sum Root To Leaf Numbers | [LC](https://leetcode.com/problems/sum-root-to-leaf-numbers/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |
| 88 | 94 | Binary Tree Maximum Path Sum | [LC](https://leetcode.com/problems/binary-tree-maximum-path-sum/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |
| 101 | 108 | Path Sum | [LC](https://leetcode.com/problems/path-sum/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |
| 181 | 191 | Path Sum II | [LC](https://leetcode.com/problems/path-sum-ii/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |

### BST property

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 52 | 57 | Lowest Common Ancestor Of A Binary Search Tree | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) |
| 104 | 111 | Insert Into A Binary Search Tree | [LC](https://leetcode.com/problems/insert-into-a-binary-search-tree/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) |
| 105 | 112 | Minimum Absolute Difference In BST | [LC](https://leetcode.com/problems/minimum-absolute-difference-in-bst/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java), [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) |
| 106 | 113 | Range Sum Of BST | [LC](https://leetcode.com/problems/range-sum-of-bst/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) |
| 107 | 114 | Search In A Binary Search Tree | [LC](https://leetcode.com/problems/search-in-a-binary-search-tree/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) |

### Tree DFS/BFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 59 | 64 | Invert Binary Tree | [LC](https://leetcode.com/problems/invert-binary-tree/) | [InvertBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session3/InvertBinaryTree.java) |

### Tree recursion / hashmap index

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 60 | 65 | Construct Binary Search Tree From Preorder Traversal | [LC](https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/) | [ConstructTree.java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) |
| 74 | 79 | Verify Preorder Serialization Of A Binary Tree | [LC](https://leetcode.com/problems/verify-preorder-serialization-of-a-binary-tree/) | [ConstructTree.java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) |
| 85 | 91 | Construct Binary Tree From Inorder And Postorder Traversal | [LC](https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/) | [ConstructTree.java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) |
| 87 | 93 | Construct Binary Tree From Preorder And Inorder Traversal | [LC](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/) | [ConstructTree.java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) |

### Tree BFS/DFS serialization

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 67 | 72 | Serialize And Deserialize Binary Tree | [LC](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/) | [SerializeAndDeserializeBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session2/SerializeAndDeserializeBinaryTree.java) |

### Tree + graph BFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 108 | 115 | All Nodes Distance K in Binary Tree | [LC](https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/) | [BurnBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java) |
| 109 | 116 | Amount of Time for Binary Tree to Be Infected | [LC](https://leetcode.com/problems/amount-of-time-for-binary-tree-to-be-infected/) | [BurnBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java) |


## Graph DFS / Components

### Matrix DFS/BFS components

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 16 | 18 | Number Of Islands | [LC](https://leetcode.com/problems/number-of-islands/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java), [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |
| 63 | 68 | Pacific Atlantic Water Flow | [LC](https://leetcode.com/problems/pacific-atlantic-water-flow/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |
| 64 | 69 | Surrounded Regions | [LC](https://leetcode.com/problems/surrounded-regions/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |
| 114 | 121 | Number Of Closed Islands | [LC](https://leetcode.com/problems/number-of-closed-islands/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |
| 115 | 122 | Max Area Of Island | [LC](https://leetcode.com/problems/max-area-of-island/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |

### Matrix DFS/BFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 37 | 40 | Flood Fill | [LC](https://leetcode.com/problems/flood-fill/) | [FloodFill.java](../../src/main/java/org/chijai/day8/graph/session1/FloodFill.java), [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java), [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |

### BFS/DFS coloring

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 38 | 41 | Is Graph Bipartite? | [LC](https://leetcode.com/problems/is-graph-bipartite/) | [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |
| 116 | 123 | Graph Valid Tree | [LC](https://leetcode.com/problems/graph-valid-tree/) | [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |
| 117 | 124 | Possible Bipartition | [LC](https://leetcode.com/problems/possible-bipartition/) | [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |
| 118 | 125 | Redundant Connection | [LC](https://leetcode.com/problems/redundant-connection/) | [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |

### Graph DFS/BFS clone

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 70 | 75 | Clone Graph | [LC](https://leetcode.com/problems/clone-graph/) | [CloneGraph.java](../../src/main/java/org/chijai/day8/graph/session2/CloneGraph.java), [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |

### Matrix DFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 119 | 126 | Coloring A Border | [LC](https://leetcode.com/problems/coloring-a-border/) | [ColoringABorder.java](../../src/main/java/org/chijai/day8/graph/session1/ColoringABorder.java) |


## Topological Sort

### Topological sort / cycle

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 17 | 19 | Course Schedule | [LC](https://leetcode.com/problems/course-schedule/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java), [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |
| 18 | 20 | Course Schedule II | [LC](https://leetcode.com/problems/course-schedule-ii/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |
| 137 | 145 | Parallel Courses | [LC](https://leetcode.com/problems/parallel-courses/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |
| 138 | 146 | Alien Dictionary | [LC](https://leetcode.com/problems/alien-dictionary/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |
| 139 | 147 | Find Eventual Safe States | [LC](https://leetcode.com/problems/find-eventual-safe-states/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |
| 140 | 148 | Sequence Reconstruction | [LC](https://leetcode.com/problems/sequence-reconstruction/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |
| 141 | 149 | Sort Items by Groups Respecting Dependencies | [LC](https://leetcode.com/problems/sort-items-by-groups-respecting-dependencies/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |
| 186 | 196 | Course Schedule IV | [LC](https://leetcode.com/problems/course-schedule-iv/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |

### Topological trimming

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 66 | 71 | Minimum Height Trees | [LC](https://leetcode.com/problems/minimum-height-trees/) | [MinHTree.java](../../src/main/java/org/chijai/day8/graph/session3/MinHTree.java) |


## Graph BFS / Shortest Path

### BFS shortest path

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 19 | 21 | Word Ladder | [LC](https://leetcode.com/problems/word-ladder/) | [WordLadder.java](../../src/main/java/org/chijai/day8/graph/session3/WordLadder.java) |

### Multi-source BFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 27 | 30 | Rotting Oranges | [LC](https://leetcode.com/problems/rotting-oranges/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java), [RottenOranges.java](../../src/main/java/org/chijai/day8/graph/session1/RottenOranges.java) |
| 28 | 31 | 01 Matrix | [LC](https://leetcode.com/problems/01-matrix/) | [Matrix01.java](../../src/main/java/org/chijai/day8/graph/session1/Matrix01.java) |

### Dijkstra / graph

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 62 | 67 | Network Delay Time | [LC](https://leetcode.com/problems/network-delay-time/) | [NetworkDelayTime.java](../../src/main/java/org/chijai/day8/graph/session2/NetworkDelayTime.java) |

### Matrix DFS/BFS components

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 69 | 74 | Number Of Provinces | [LC](https://leetcode.com/problems/number-of-provinces/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java), [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |

### BFS + sorting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 113 | 120 | K Highest Ranked Items Within A Price Range | [LC](https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/) | [KHighestRankedItemsWithinAPriceRange.java](../../src/main/java/org/chijai/day8/graph/session3/KHighestRankedItemsWithinAPriceRange.java) |


## Dynamic Programming

### 1D DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 29 | 32 | House Robber | [LC](https://leetcode.com/problems/house-robber/) | [HouseRobber.java](../../src/main/java/org/chijai/day9/dp/session1/HouseRobber.java), [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |

### Unbounded knapsack DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 30 | 33 | Coin Change | [LC](https://leetcode.com/problems/coin-change/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |
| 162 | 171 | Climbing Stairs | [LC](https://leetcode.com/problems/climbing-stairs/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |
| 163 | 172 | Min Cost Climbing Stairs | [LC](https://leetcode.com/problems/min-cost-climbing-stairs/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |
| 164 | 173 | Perfect Squares | [LC](https://leetcode.com/problems/perfect-squares/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |
| 165 | 174 | Word Break | [LC](https://leetcode.com/problems/word-break/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |

### Grid DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 40 | 43 | Unique Paths | [LC](https://leetcode.com/problems/unique-paths/) | [UniquePaths.java](../../src/main/java/org/chijai/day9/dp/session1/UniquePaths.java) |

### 0/1 knapsack DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 41 | 44 | Partition Equal Subset Sum | [LC](https://leetcode.com/problems/partition-equal-subset-sum/) | [PartitionEqualSubsetSum.java](../../src/main/java/org/chijai/day9/dp/session2/PartitionEqualSubsetSum.java) |

### DP / patience sorting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 42 | 45 | Longest Increasing Subsequence | [LC](https://leetcode.com/problems/longest-increasing-subsequence/) | [LIS.java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) |
| 172 | 181 | Longest Continuous Increasing Subsequence | [LC](https://leetcode.com/problems/longest-continuous-increasing-subsequence/) | [LIS.java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) |
| 173 | 182 | Maximum Length of Pair Chain | [LC](https://leetcode.com/problems/maximum-length-of-pair-chain/) | [LIS.java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) |
| 174 | 183 | Number of Longest Increasing Subsequence | [LC](https://leetcode.com/problems/number-of-longest-increasing-subsequence/) | [LIS.java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) |
| 175 | 184 | Russian Doll Envelopes | [LC](https://leetcode.com/problems/russian-doll-envelopes/) | [LIS.java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) |

### DP + binary search

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 78 | 83 | Maximum Profit In Job Scheduling | [LC](https://leetcode.com/problems/maximum-profit-in-job-scheduling/) | [MaximumProfitInJobScheduling.java](../../src/main/java/org/chijai/day2/session3/MaximumProfitInJobScheduling.java) |

### Greedy / DP states

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 79 | 85 | Best Time to Buy and Sell Stock | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/) | [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java), [StockSeries2.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) |
| 159 | 168 | Best Time to Buy and Sell Stock with Cooldown | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/) | [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java), [StockSeries2.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) |
| 160 | 169 | Best Time to Buy and Sell Stock with Transaction Fee | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/) | [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java), [StockSeries2.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) |
| 190 | 205 | Best Time to Buy and Sell Stock II | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/) | [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java), [StockSeries2.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) |
| 191 | 206 | Best Time to Buy and Sell Stock III | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/) | [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java), [StockSeries2.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) |
| 192 | 207 | Best Time to Buy and Sell Stock IV | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/) | [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java), [StockSeries2.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) |

### Greedy

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 157 | 166 | Gas Station | [LC](https://leetcode.com/problems/gas-station/) | [GasStation.java](../../src/main/java/org/chijai/day9/dp/session1/GasStation.java) |
| 161 | 170 | Jump Game | [LC](https://leetcode.com/problems/jump-game/) | [GasStation.java](../../src/main/java/org/chijai/day9/dp/session1/GasStation.java) |

### 2D DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 158 | 167 | Edit Distance | [LC](https://leetcode.com/problems/edit-distance/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |
| 166 | 175 | Delete Operation for Two Strings | [LC](https://leetcode.com/problems/delete-operation-for-two-strings/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |
| 167 | 176 | Distinct Subsequences | [LC](https://leetcode.com/problems/distinct-subsequences/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |
| 168 | 177 | Interleaving String | [LC](https://leetcode.com/problems/interleaving-string/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |
| 169 | 178 | Longest Common Subsequence | [LC](https://leetcode.com/problems/longest-common-subsequence/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |
| 170 | 179 | Longest Palindromic Subsequence | [LC](https://leetcode.com/problems/longest-palindromic-subsequence/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |
| 171 | 180 | Minimum ASCII Delete Sum for Two Strings | [LC](https://leetcode.com/problems/minimum-ascii-delete-sum-for-two-strings/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |

### Dynamic Programming

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 207 | - | Stock Price Fluctuation | [LC](https://leetcode.com/problems/stock-price-fluctuation/) | [StockPriceFluctuation.java](../../src/main/java/org/chijai/trading/StockPriceFluctuation.java) |


## Backtracking / Combinatorial DFS

### Backtracking subsets

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 31 | 34 | Subsets | [LC](https://leetcode.com/problems/subsets/) | [Subsets.java](../../src/main/java/org/chijai/day11/backtracking/session1/Subsets.java) |

### Backtracking reuse

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 43 | 46 | Combination Sum | [LC](https://leetcode.com/problems/combination-sum/) | [CombinationSum.java](../../src/main/java/org/chijai/day11/backtracking/session1/CombinationSum.java) |

### DFS backtracking

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 45 | 48 | Word Search | [LC](https://leetcode.com/problems/word-search/) | [WordSearch.java](../../src/main/java/org/chijai/day8/graph/session1/WordSearch.java) |

### Backtracking / mapping

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 135 | 143 | Letter Combinations Of A Phone Number | [LC](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) | [LetterCombinationsOfAPhoneNumber.java](../../src/main/java/org/chijai/day11/backtracking/session1/LetterCombinationsOfAPhoneNumber.java) |

### Backtracking permutations

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 136 | 144 | Permutations | [LC](https://leetcode.com/problems/permutations/) | [BacktrackingRecursion.java](../../src/main/java/org/chijai/day11/backtracking/BacktrackingRecursion.java), [Permutations.java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) |
| 185 | 195 | Permutations II | [LC](https://leetcode.com/problems/permutations-ii/) | [Permutations.java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) |

### Backtracking / Combinatorial DFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 205 | - | N-Queens | [LC](https://leetcode.com/problems/n-queens/) | [NQueens.java](../../src/main/java/org/chijai/day11/backtracking/session1/NQueens.java) |
| 206 | - | Sudoku Solver | [LC](https://leetcode.com/problems/sudoku-solver/) | [SudokuSolver.java](../../src/main/java/org/chijai/day11/backtracking/session1/SudokuSolver.java) |


## Stack / Monotonic Stack

### Stack

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 32 | 35 | Valid Parentheses | [LC](https://leetcode.com/problems/valid-parentheses/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java), [ValidParentheses.java](../../src/main/java/org/chijai/day5/stack/session3/ValidParentheses.java) |
| 95 | 101 | Evaluate Reverse Polish Notation | [LC](https://leetcode.com/problems/evaluate-reverse-polish-notation/) | [EvalRPN.java](../../src/main/java/org/chijai/day5/stack/session3/EvalRPN.java) |

### Monotonic stack

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 34 | 37 | Daily Temperatures | [LC](https://leetcode.com/problems/daily-temperatures/) | [DailyTemperatures.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/DailyTemperatures.java), [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) |
| 94 | 100 | Next Greater Element II | [LC](https://leetcode.com/problems/next-greater-element-ii/) | [NextGreaterElement.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/NextGreaterElement.java) |

### Stack/queue design

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 84 | 90 | Sliding Window Maximum | [LC](https://leetcode.com/problems/sliding-window-maximum/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java), [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) |
| 123 | 131 | Implement Queue Using Stacks | [LC](https://leetcode.com/problems/implement-queue-using-stacks/) | [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |
| 124 | 132 | Implement Stack Using Queues | [LC](https://leetcode.com/problems/implement-stack-using-queues/) | [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |

### Stack / expression parsing

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 97 | 103 | Basic Calculator | [LC](https://leetcode.com/problems/basic-calculator/) | [BasicCalculator.java](../../src/main/java/org/chijai/day5/stack/session3/BasicCalculator.java) |

### Stack design

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 121 | 129 | Min Stack | [LC](https://leetcode.com/problems/min-stack/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java), [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |
| 122 | 130 | Max Stack | [LC](https://leetcode.com/problems/max-stack/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) |
| 125 | 133 | Next Greater Element I | [LC](https://leetcode.com/problems/next-greater-element-i/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java), [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |
| 126 | 134 | Online Stock Span | [LC](https://leetcode.com/problems/online-stock-span/) | [OnlineStockSpan.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/OnlineStockSpan.java), [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) |
| 146 | 154 | Design A Stack With Increment Operation | [LC](https://leetcode.com/problems/design-a-stack-with-increment-operation/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) |

### Stack / Monotonic Stack

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 200 | - | Remove K Digits | [LC](https://leetcode.com/problems/remove-k-digits/) | [RemoveKDigits.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/RemoveKDigits.java) |
| 201 | - | Sum of Subarray Minimums | [LC](https://leetcode.com/problems/sum-of-subarray-minimums/) | [SumOfSubarrayMinimums.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SumOfSubarrayMinimums.java) |


## Trie

### Trie

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 36 | 39 | Implement Trie (Prefix Tree) | [LC](https://leetcode.com/problems/implement-trie-prefix-tree/) | [MaximumXOR.java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java), [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java), [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) |
| 86 | 92 | Design Add and Search Words Data Structure | [LC](https://leetcode.com/problems/design-add-and-search-words-data-structure/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java), [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) |
| 148 | 156 | Longest Common Prefix | [LC](https://leetcode.com/problems/longest-common-prefix/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) |
| 150 | 158 | Replace Words | [LC](https://leetcode.com/problems/replace-words/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java), [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) |
| 151 | 159 | Search Suggestions System | [LC](https://leetcode.com/problems/search-suggestions-system/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java), [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) |
| 152 | 160 | Short Encoding of Words | [LC](https://leetcode.com/problems/short-encoding-of-words/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) |

### Trie + backtracking

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 93 | 99 | Word Search II | [LC](https://leetcode.com/problems/word-search-ii/) | [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java), [WordSearchII.java](../../src/main/java/org/chijai/day10/session1/trie/WordSearchII.java) |

### Binary trie / bit

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 145 | 153 | Maximum XOR of Two Numbers in an Array | [LC](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/) | [MaximumXOR.java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) |
| 154 | 162 | Maximum XOR With an Element From Array | [LC](https://leetcode.com/problems/maximum-xor-with-an-element-from-array/) | [MaximumXOR.java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) |
| 155 | 163 | Maximum Genetic Difference Query | [LC](https://leetcode.com/problems/maximum-genetic-difference-query/) | [MaximumXOR.java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) |
| 156 | 164 | Count Pairs With XOR in a Range | [LC](https://leetcode.com/problems/count-pairs-with-xor-in-a-range/) | [MaximumXOR.java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) |

### Trie + DFS wildcard

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 149 | 157 | Longest Word in Dictionary | [LC](https://leetcode.com/problems/longest-word-in-dictionary/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java), [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) |
| 153 | 161 | Map Sum Pairs | [LC](https://leetcode.com/problems/map-sum-pairs/) | [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) |


## Intervals / Sorting Greedy

### Intervals / sorting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 39 | 42 | Minimum Number Of Arrows To Burst Balloons | [LC](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/) | [IntervalGreedyByEnd.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalGreedyByEnd.java) |
| 133 | 141 | Non Overlapping Intervals | [LC](https://leetcode.com/problems/non-overlapping-intervals/) | [IntervalGreedyByEnd.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalGreedyByEnd.java) |

### Intervals / merge

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 83 | 89 | Meeting Rooms | [LC](https://leetcode.com/problems/meeting-rooms/) | [IntervalSortByStart.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalSortByStart.java) |
| 131 | 139 | Insert Interval | [LC](https://leetcode.com/problems/insert-interval/) | [IntervalSortByStart.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalSortByStart.java) |
| 132 | 140 | Merge Intervals | [LC](https://leetcode.com/problems/merge-intervals/) | [IntervalSortByStart.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalSortByStart.java) |

### Greedy last-occurrence boundary

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 134 | 142 | Partition Labels | [LC](https://leetcode.com/problems/partition-labels/) | [CountUniqueChars.java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java), [CountUniqueChars.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/CountUniqueChars.java) |

### Intervals / Sorting Greedy

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 204 | - | Car Pooling | [LC](https://leetcode.com/problems/car-pooling/) | [BoundaryDelta.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/BoundaryDelta.java) |


## Union Find / DSU

### Union Find / graph

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 65 | 70 | Accounts Merge | [LC](https://leetcode.com/problems/accounts-merge/) | [AccountsMerge.java](../../src/main/java/org/chijai/day8/graph/session3/AccountsMerge.java) |


## Design Data Structures

### HashMap + doubly linked list

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 80 | 86 | First Unique Number | [LC](https://leetcode.com/problems/first-unique-number/) | [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) |

### LLD / URL shortener

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 188 | 198 | Encode And Decode Tinyurl | [LC](https://leetcode.com/problems/encode-and-decode-tinyurl/) | [DesignUrlShortner.java](../../src/main/java/org/chijai/design/lld/DesignUrlShortner.java) |

### Stack/queue design

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 189 | 199 | Design Circular Queue | [LC](https://leetcode.com/problems/design-circular-queue/) | [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |

### Design Data Structures

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 211 | - | Design A Leaderboard | [LC](https://leetcode.com/problems/design-a-leaderboard/) | [DesignALeaderboard.java](../../src/main/java/org/chijai/design/lld/DesignALeaderboard.java) |
| 212 | - | Design an Ordered Stream | [LC](https://leetcode.com/problems/design-an-ordered-stream/) | [DesignOrderedStream.java](../../src/main/java/org/chijai/design/lld/DesignOrderedStream.java) |
| 213 | - | Design Hit Counter | [LC](https://leetcode.com/problems/design-hit-counter/) | [DesignHitCounter.java](../../src/main/java/org/chijai/design/lld/DesignHitCounter.java) |
| 214 | - | Design Parking System | [LC](https://leetcode.com/problems/design-parking-system/) | [DesignParkingSystem.java](../../src/main/java/org/chijai/design/lld/DesignParkingSystem.java) |


## Math / Bit / String

### KMP string matching

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 96 | 102 | Find The Index Of The First Occurrence In A String | [LC](https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/) | [KmpPatterns.java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java), [LongestHappyPrefix.java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java), [ZFunction.java](../../src/main/java/org/chijai/day7/session2/ZFunction.java) |
| 144 | 152 | Repeated Substring Pattern | [LC](https://leetcode.com/problems/repeated-substring-pattern/) | [KmpPatterns.java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java), [LongestHappyPrefix.java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) |
| 179 | 189 | Shortest Palindrome | [LC](https://leetcode.com/problems/shortest-palindrome/) | [KmpPatterns.java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java), [LongestHappyPrefix.java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) |

### KMP / rolling hash

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 147 | 155 | Longest Happy Prefix | [LC](https://leetcode.com/problems/longest-happy-prefix/) | [KmpPatterns.java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java), [LongestHappyPrefix.java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) |

### Bit/string addition

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 176 | 185 | Add Binary | [LC](https://leetcode.com/problems/add-binary/) | [AddBinary.java](../../src/main/java/org/chijai/day10/session2/AddBinary.java) |

### Math / sieve

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 177 | 186 | Count Primes | [LC](https://leetcode.com/problems/count-primes/) | [CountPrimes.java](../../src/main/java/org/chijai/day10/session2/CountPrimes.java) |

### Contribution counting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 178 | 187 | Count Unique Characters of All Substrings of a Given String | [LC](https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/) | [CountUniqueChars.java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java), [CountUniqueChars.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/CountUniqueChars.java) |


## Basics / Implementation

### Matrix boundary traversal

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 142 | 150 | Spiral Matrix | [LC](https://leetcode.com/problems/spiral-matrix/) | [SpiralMatrix.java](../../src/main/java/org/chijai/day1/Arrays/session1/SpiralMatrix.java) |

### Parsing / edge cases

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 143 | 151 | String To Integer Atoi | [LC](https://leetcode.com/problems/string-to-integer-atoi/) | [StringToIntegerAtoi.java](../../src/main/java/org/chijai/day3/session3/StringToIntegerAtoi.java) |

### Contribution counting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 187 | 197 | Distinct Subsequences II | [LC](https://leetcode.com/problems/distinct-subsequences-ii/) | [CountUniqueChars.java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java), [CountUniqueChars.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/CountUniqueChars.java) |

### Basics / Implementation

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 208 | - | Missing Number | [LC](https://leetcode.com/problems/missing-number/) | [MissingNumber.java](../../src/main/java/org/chijai/day10/session2/MissingNumber.java) |
| 209 | - | Missing Ranges | [LC](https://leetcode.com/problems/missing-ranges/) | [MissingRanges.java](../../src/main/java/org/chijai/trading/MissingRanges.java) |
| 210 | - | Number of Orders in the Backlog | [LC](https://leetcode.com/problems/number-of-orders-in-the-backlog/) | [NumberOfOrdersInTheBacklog.java](../../src/main/java/org/chijai/trading/NumberOfOrdersInTheBacklog.java) |