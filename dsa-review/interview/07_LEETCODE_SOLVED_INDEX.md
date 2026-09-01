# LeetCode Solved Index

Recursive source scan: this is the book-style table of contents for LeetCode problems found in Java source files by full LeetCode URL or explicit LC problem number.

Regenerate it with `dsa-review/scripts/build-interview-cockpit.cmd`, `dsa-review/scripts/build-interview-cockpit.sh`, or `verify-all.ps1` after adding or editing Java solution files. Add a full LeetCode URL or cataloged LC problem number when a file contains a solved problem.

Use [Zero To Hero Ranked Table](01_ZERO_TO_HERO_RANKED_TABLE.md) for interview crunch order. Use this file when you want the complete source-backed LeetCode inventory.

| Metric | Count |
|---|---:|
| Unique LeetCode problems found recursively | 213 |
| Also present in interview-ranked cockpit | 191 |
| Extra source-discovered problems | 22 |
| Problems appearing in multiple Java files | 57 |

## Table Of Contents

- [HashMap / Frequency / Set (5)](#hashmap-frequency-set)
- [Binary Search / Answer Search (13)](#binary-search-answer-search)
- [Sliding Window (13)](#sliding-window)
- [Prefix Sum / Prefix-Suffix (2)](#prefix-sum-prefixsuffix)
- [Linked List Pointers (11)](#linked-list-pointers)
- [Heap / Priority Queue (13)](#heap-priority-queue)
- [Two Pointers (4)](#two-pointers)
- [Tree BFS / Level Order (2)](#tree-bfs-level-order)
- [Tree DFS / Recursion (31)](#tree-dfs-recursion)
- [Graph DFS / Components (12)](#graph-dfs-components)
- [Topological Sort (9)](#topological-sort)
- [Graph BFS / Shortest Path (6)](#graph-bfs-shortest-path)
- [Dynamic Programming (24)](#dynamic-programming)
- [Backtracking / Combinatorial DFS (8)](#backtracking-combinatorial-dfs)
- [Stack / Monotonic Stack (15)](#stack-monotonic-stack)
- [Trie (13)](#trie)
- [Intervals / Sorting Greedy (8)](#intervals-sorting-greedy)
- [Union Find / DSU (1)](#union-find-dsu)
- [Greedy (4)](#greedy)
- [Design Data Structures (7)](#design-data-structures)
- [Math / Bit / String (7)](#math-bit-string)
- [Basics / Implementation (5)](#basics-implementation)

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
| 47 | 51 | Majority Element | [LC](https://leetcode.com/problems/majority-element/) | [MajorityElement.java](../../src/main/java/org/chijai/day1/Arrays/session2/MajorityElement.java) |

### HashMap/frequency

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 48 | 53 | Ransom Note | [LC](https://leetcode.com/problems/ransom-note/) | [RansomNote.java](../../src/main/java/org/chijai/day1/Arrays/session1/RansomNote.java) |

### Hash/frequency

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 100 | 107 | Longest Palindrome | [LC](https://leetcode.com/problems/longest-palindrome/) | [LongestPalindrome.java](../../src/main/java/org/chijai/day3/session3/LongestPalindrome.java) |


## Binary Search / Answer Search

### Binary search invariant

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 2 | 2 | Binary Search | [LC](https://leetcode.com/problems/binary-search/) | [BinarySearch.java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) |
| 73 | 79 | Search Insert Position | [LC](https://leetcode.com/problems/search-insert-position/) | [BinarySearch.java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java), [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 79 | 85 | First Bad Version | [LC](https://leetcode.com/problems/first-bad-version/) | [BinarySearch.java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) |

### Binary search on answer

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 20 | 22 | Koko Eating Bananas | [LC](https://leetcode.com/problems/koko-eating-bananas/) | [AGGRCOW.java](../../src/main/java/org/chijai/day2/session2/AGGRCOW.java), [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) |
| 80 | 86 | Split Array Largest Sum | [LC](https://leetcode.com/problems/split-array-largest-sum/) | [AGGRCOW.java](../../src/main/java/org/chijai/day2/session2/AGGRCOW.java), [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) |
| 83 | 90 | Capacity To Ship Packages Within D Days | [LC](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/) | [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) |
| 84 | 91 | Minimum Number Of Days To Make M Bouquets | [LC](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/) | [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) |

### Binary search boundary

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 21 | 23 | Search In Rotated Sorted Array | [LC](https://leetcode.com/problems/search-in-rotated-sorted-array/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 22 | 24 | Find First And Last Position Of Element In Sorted Array | [LC](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 68 | 74 | Search In Rotated Sorted Array II | [LC](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 78 | 84 | Find Peak Element | [LC](https://leetcode.com/problems/find-peak-element/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 122 | 130 | Sqrtx | [LC](https://leetcode.com/problems/sqrtx/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |

### HashMap + binary search

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 94 | 101 | Time Based Key Value Store | [LC](https://leetcode.com/problems/time-based-key-value-store/) | [TimeBasedKeyValueStore.java](../../src/main/java/org/chijai/day2/session3/TimeBasedKeyValueStore.java) |


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
| 43 | 47 | Longest Substring With At Most K Distinct Characters | [LC](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/) | [AtMostKDistinct.java](../../src/main/java/org/chijai/day3/session1/AtMostKDistinct.java), [LongestSubString.java](../../src/main/java/org/chijai/day3/session1/LongestSubString.java) |
| 192 | - | Constrained Subsequence Sum | [LC](https://leetcode.com/problems/constrained-subsequence-sum/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) |
| 193 | - | Jump Game VI | [LC](https://leetcode.com/problems/jump-game-vi/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) |
| 194 | - | Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit | [LC](https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) |
| 195 | - | Longest Repeating Character Replacement | [LC](https://leetcode.com/problems/longest-repeating-character-replacement/) | [LongestRepeatingCharacterReplacement.java](../../src/main/java/org/chijai/day3/session1/LongestRepeatingCharacterReplacement.java) |
| 196 | - | Max Value of Equation | [LC](https://leetcode.com/problems/max-value-of-equation/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) |
| 197 | - | Maximum Number of Robots Within Budget | [LC](https://leetcode.com/problems/maximum-number-of-robots-within-budget/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) |
| 198 | - | Shortest Subarray with Sum at Least K | [LC](https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) |

### Queue / stream

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 75 | 81 | Moving Average From Data Stream | [LC](https://leetcode.com/problems/moving-average-from-data-stream/) | [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java), [MovingAverage.java](../../src/main/java/org/chijai/day7/session1/heap/MovingAverage.java), [MovingAverageFromDataStream.java](../../src/main/java/org/chijai/trading/MovingAverageFromDataStream.java) |

### Prefix/window counting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 102 | 109 | Count Number Of Nice Subarrays | [LC](https://leetcode.com/problems/count-number-of-nice-subarrays/) | [NiceSubArrays.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/NiceSubArrays.java) |

### DP / patience sorting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 191 | 207 | Longest Continuous Increasing Subsequence | [LC](https://leetcode.com/problems/longest-continuous-increasing-subsequence/) | [LIS.java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) |


## Prefix Sum / Prefix-Suffix

### Prefix/suffix

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 4 | 4 | Product Of Array Except Self | [LC](https://leetcode.com/problems/product-of-array-except-self/) | [ProductOfArrayExceptSelf.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/ProductOfArrayExceptSelf.java) |

### Prefix/window counting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 46 | 50 | Binary Subarrays With Sum | [LC](https://leetcode.com/problems/binary-subarrays-with-sum/) | [NiceSubArrays.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/NiceSubArrays.java) |


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
| 76 | 82 | Design Browser History | [LC](https://leetcode.com/problems/design-browser-history/) | [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) |

### Linked list two pointers

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 49 | 54 | Intersection Of Two Linked Lists | [LC](https://leetcode.com/problems/intersection-of-two-linked-lists/) | [Intersection.java](../../src/main/java/org/chijai/day4/LinkedList/session1/Intersection.java) |

### Linked-list reversal groups

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 50 | 56 | Reverse Nodes in k-Group | [LC](https://leetcode.com/problems/reverse-nodes-in-k-group/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |
| 54 | 60 | Odd Even Linked List | [LC](https://leetcode.com/problems/odd-even-linked-list/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |
| 55 | 61 | Rotate List | [LC](https://leetcode.com/problems/rotate-list/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |
| 56 | 62 | Swap Nodes In Pairs | [LC](https://leetcode.com/problems/swap-nodes-in-pairs/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |
| 165 | 176 | Reverse Linked List II | [LC](https://leetcode.com/problems/reverse-linked-list-ii/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |


## Heap / Priority Queue

### Heap / divide and conquer

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 10 | 11 | Merge K Sorted Lists | [LC](https://leetcode.com/problems/merge-k-sorted-lists/) | [MergeKSortedLists.java](../../src/main/java/org/chijai/day4/LinkedList/session4/MergeKSortedLists.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) |

### Frequency + heap/bucket

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 33 | 36 | Top K Frequent Elements | [LC](https://leetcode.com/problems/top-k-frequent-elements/) | [HeapSort.java](../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java), [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java), [TopKFrequentTransactions.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentTransactions.java), [TopKFrequentElements.java](../../src/main/java/org/chijai/trading/TopKFrequentElements.java) |
| 130 | 139 | Top K Frequent Words | [LC](https://leetcode.com/problems/top-k-frequent-words/) | [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) |
| 131 | 140 | H-Index | [LC](https://leetcode.com/problems/h-index/) | [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) |
| 132 | 141 | Sort Characters By Frequency | [LC](https://leetcode.com/problems/sort-characters-by-frequency/) | [HeapSort.java](../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java), [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) |

### Intervals / heap

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 35 | 38 | Meeting Rooms II | [LC](https://leetcode.com/problems/meeting-rooms-ii/) | [IntervalActiveMinHeap.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalActiveMinHeap.java) |

### Two heaps

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 45 | 49 | Find Median From Data Stream | [LC](https://leetcode.com/problems/find-median-from-data-stream/) | [HeapSort.java](../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java), [Median.java](../../src/main/java/org/chijai/day7/session1/heap/Median.java) |

### Greedy / heap

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 91 | 98 | Task Scheduler | [LC](https://leetcode.com/problems/task-scheduler/) | [TaskScheduler.java](../../src/main/java/org/chijai/day7/session1/heap/TaskScheduler.java) |

### Min-heap size K

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 92 | 99 | Kth Largest Element In An Array | [LC](https://leetcode.com/problems/kth-largest-element-in-an-array/) | [HeapSort.java](../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java), [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) |
| 93 | 100 | Kth Largest Element In A Stream | [LC](https://leetcode.com/problems/kth-largest-element-in-a-stream/) | [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java), [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) |

### Heap / quickselect

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 129 | 138 | K Closest Points To Origin | [LC](https://leetcode.com/problems/k-closest-points-to-origin/) | [KClosestPointsToOrigin.java](../../src/main/java/org/chijai/day7/session1/heap/KClosestPointsToOrigin.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java), [TopKFrequentElements.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) |

### Heap / Priority Queue

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 201 | - | Ipo | [LC](https://leetcode.com/problems/ipo/) | [IPO.java](../../src/main/java/org/chijai/day7/session1/heap/IPO.java) |
| 202 | - | Sliding Window Median | [LC](https://leetcode.com/problems/sliding-window-median/) | [SlidingWindowMedian.java](../../src/main/java/org/chijai/day7/session1/heap/SlidingWindowMedian.java) |


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
| 53 | 59 | Sort Colors | [LC](https://leetcode.com/problems/sort-colors/) | [SortColors.java](../../src/main/java/org/chijai/day1/Arrays/session1/SortColors.java) |

### Expand around center

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 101 | 108 | Longest Palindromic Substring | [LC](https://leetcode.com/problems/longest-palindromic-substring/) | [LongestPalindromicSubstring.java](../../src/main/java/org/chijai/day3/session3/LongestPalindromicSubstring.java) |


## Tree BFS / Level Order

### Tree traversal

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 13 | 15 | Binary Tree Level Order Traversal | [LC](https://leetcode.com/problems/binary-tree-level-order-traversal/) | [BinaryTreeTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeTraversal.java), [BurnBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java) |

### Tree BFS / DFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 52 | 58 | Binary Tree Right Side View | [LC](https://leetcode.com/problems/binary-tree-right-side-view/) | [BinaryTreeSideView.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeSideView.java) |


## Tree DFS / Recursion

### Tree DFS / stack

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 14 | 16 | Validate Binary Search Tree | [LC](https://leetcode.com/problems/validate-binary-search-tree/) | [BinaryTreeInorderTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java), [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java), [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java), [ValidateBST.java](../../src/main/java/org/chijai/day6/trees/session3/ValidateBST.java) |
| 57 | 63 | Binary Tree Inorder Traversal | [LC](https://leetcode.com/problems/binary-tree-inorder-traversal/) | [BinaryTreeInorderTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) |
| 104 | 112 | Binary Tree Postorder Traversal | [LC](https://leetcode.com/problems/binary-tree-postorder-traversal/) | [BinaryTreeInorderTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) |
| 105 | 113 | Binary Tree Preorder Traversal | [LC](https://leetcode.com/problems/binary-tree-preorder-traversal/) | [BinaryTreeInorderTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) |

### Tree DFS return contract

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 15 | 17 | Lowest Common Ancestor Of A Binary Tree | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/) | [LCA.java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) |
| 167 | 178 | Lowest Common Ancestor Of A Binary Tree II | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-ii/) | [LCA.java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) |
| 168 | 179 | Lowest Common Ancestor Of A Binary Tree III | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/) | [LCA.java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) |
| 169 | 180 | Lowest Common Ancestor Of A Binary Tree IV | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iv/) | [LCA.java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) |

### BST inorder

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 24 | 27 | Kth Smallest Element in a BST | [LC](https://leetcode.com/problems/kth-smallest-element-in-a-bst/) | [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java), [KthSmallestElementInBST.java](../../src/main/java/org/chijai/day6/trees/session3/KthSmallestElementInBST.java) |
| 112 | 120 | Recover Binary Search Tree | [LC](https://leetcode.com/problems/recover-binary-search-tree/) | [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) |
| 113 | 121 | Binary Search Tree Iterator | [LC](https://leetcode.com/problems/binary-search-tree-iterator/) | [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) |
| 114 | 122 | Convert BST To Greater Tree | [LC](https://leetcode.com/problems/convert-bst-to-greater-tree/) | [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) |

### Core tree patterns

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 25 | 28 | Diameter of Binary Tree | [LC](https://leetcode.com/problems/diameter-of-binary-tree/) | [BinaryTree.java](../../src/main/java/org/chijai/day6/trees/session3/BinaryTree.java) |

### Tree path DFS / global answer

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 26 | 29 | Path Sum III | [LC](https://leetcode.com/problems/path-sum-iii/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |
| 60 | 66 | Sum Root To Leaf Numbers | [LC](https://leetcode.com/problems/sum-root-to-leaf-numbers/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |
| 90 | 97 | Binary Tree Maximum Path Sum | [LC](https://leetcode.com/problems/binary-tree-maximum-path-sum/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |
| 103 | 111 | Path Sum | [LC](https://leetcode.com/problems/path-sum/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |
| 166 | 177 | Path Sum II | [LC](https://leetcode.com/problems/path-sum-ii/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |

### BST property

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 51 | 57 | Lowest Common Ancestor Of A Binary Search Tree | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) |
| 106 | 114 | Insert Into A Binary Search Tree | [LC](https://leetcode.com/problems/insert-into-a-binary-search-tree/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) |
| 107 | 115 | Minimum Absolute Difference In BST | [LC](https://leetcode.com/problems/minimum-absolute-difference-in-bst/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java), [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) |
| 108 | 116 | Range Sum Of BST | [LC](https://leetcode.com/problems/range-sum-of-bst/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) |
| 109 | 117 | Search In A Binary Search Tree | [LC](https://leetcode.com/problems/search-in-a-binary-search-tree/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) |

### Tree DFS/BFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 58 | 64 | Invert Binary Tree | [LC](https://leetcode.com/problems/invert-binary-tree/) | [InvertBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session3/InvertBinaryTree.java) |

### Tree recursion / hashmap index

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 59 | 65 | Construct Binary Search Tree From Preorder Traversal | [LC](https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/) | [ConstructTree.java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) |
| 77 | 83 | Verify Preorder Serialization Of A Binary Tree | [LC](https://leetcode.com/problems/verify-preorder-serialization-of-a-binary-tree/) | [ConstructTree.java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) |
| 87 | 94 | Construct Binary Tree From Inorder And Postorder Traversal | [LC](https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/) | [ConstructTree.java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) |
| 89 | 96 | Construct Binary Tree From Preorder And Inorder Traversal | [LC](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/) | [ConstructTree.java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) |

### Tree BFS/DFS serialization

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 67 | 73 | Serialize And Deserialize Binary Tree | [LC](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/) | [SerializeAndDeserializeBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session2/SerializeAndDeserializeBinaryTree.java) |

### Tree + graph BFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 110 | 118 | All Nodes Distance K in Binary Tree | [LC](https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/) | [BurnBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java) |
| 111 | 119 | Amount of Time for Binary Tree to Be Infected | [LC](https://leetcode.com/problems/amount-of-time-for-binary-tree-to-be-infected/) | [BurnBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java) |


## Graph DFS / Components

### Matrix DFS/BFS components

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 16 | 18 | Number Of Islands | [LC](https://leetcode.com/problems/number-of-islands/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java), [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |
| 62 | 68 | Pacific Atlantic Water Flow | [LC](https://leetcode.com/problems/pacific-atlantic-water-flow/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |
| 63 | 69 | Surrounded Regions | [LC](https://leetcode.com/problems/surrounded-regions/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |
| 116 | 124 | Number Of Closed Islands | [LC](https://leetcode.com/problems/number-of-closed-islands/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |
| 117 | 125 | Max Area Of Island | [LC](https://leetcode.com/problems/max-area-of-island/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |

### Matrix DFS/BFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 37 | 40 | Flood Fill | [LC](https://leetcode.com/problems/flood-fill/) | [FloodFill.java](../../src/main/java/org/chijai/day8/graph/session1/FloodFill.java), [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java), [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |

### BFS/DFS coloring

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 38 | 41 | Is Graph Bipartite? | [LC](https://leetcode.com/problems/is-graph-bipartite/) | [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |
| 118 | 126 | Graph Valid Tree | [LC](https://leetcode.com/problems/graph-valid-tree/) | [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |
| 119 | 127 | Possible Bipartition | [LC](https://leetcode.com/problems/possible-bipartition/) | [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |
| 120 | 128 | Redundant Connection | [LC](https://leetcode.com/problems/redundant-connection/) | [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |

### Graph DFS/BFS clone

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 71 | 77 | Clone Graph | [LC](https://leetcode.com/problems/clone-graph/) | [CloneGraph.java](../../src/main/java/org/chijai/day8/graph/session2/CloneGraph.java), [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |

### Matrix DFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 121 | 129 | Coloring A Border | [LC](https://leetcode.com/problems/coloring-a-border/) | [ColoringABorder.java](../../src/main/java/org/chijai/day8/graph/session1/ColoringABorder.java) |


## Topological Sort

### Topological sort / cycle

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 17 | 19 | Course Schedule | [LC](https://leetcode.com/problems/course-schedule/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java), [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |
| 18 | 20 | Course Schedule II | [LC](https://leetcode.com/problems/course-schedule-ii/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |
| 139 | 148 | Parallel Courses | [LC](https://leetcode.com/problems/parallel-courses/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |
| 140 | 149 | Alien Dictionary | [LC](https://leetcode.com/problems/alien-dictionary/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |
| 141 | 150 | Find Eventual Safe States | [LC](https://leetcode.com/problems/find-eventual-safe-states/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |
| 142 | 151 | Sequence Reconstruction | [LC](https://leetcode.com/problems/sequence-reconstruction/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |
| 143 | 152 | Sort Items by Groups Respecting Dependencies | [LC](https://leetcode.com/problems/sort-items-by-groups-respecting-dependencies/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |
| 171 | 182 | Course Schedule IV | [LC](https://leetcode.com/problems/course-schedule-iv/) | [CourseSchedule.java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) |

### Topological trimming

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 65 | 71 | Minimum Height Trees | [LC](https://leetcode.com/problems/minimum-height-trees/) | [MinHTree.java](../../src/main/java/org/chijai/day8/graph/session3/MinHTree.java) |


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
| 61 | 67 | Network Delay Time | [LC](https://leetcode.com/problems/network-delay-time/) | [NetworkDelayTime.java](../../src/main/java/org/chijai/day8/graph/session2/NetworkDelayTime.java) |

### Matrix DFS/BFS components

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 69 | 75 | Number Of Provinces | [LC](https://leetcode.com/problems/number-of-provinces/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java), [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |

### BFS + sorting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 115 | 123 | K Highest Ranked Items Within A Price Range | [LC](https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/) | [KHighestRankedItemsWithinAPriceRange.java](../../src/main/java/org/chijai/day8/graph/session3/KHighestRankedItemsWithinAPriceRange.java) |


## Dynamic Programming

### 1D DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 29 | 32 | House Robber | [LC](https://leetcode.com/problems/house-robber/) | [HouseRobber.java](../../src/main/java/org/chijai/day9/dp/session1/HouseRobber.java), [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |

### Unbounded knapsack DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 30 | 33 | Coin Change | [LC](https://leetcode.com/problems/coin-change/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |
| 179 | 195 | Word Break | [LC](https://leetcode.com/problems/word-break/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |
| 185 | 201 | Climbing Stairs | [LC](https://leetcode.com/problems/climbing-stairs/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |
| 186 | 202 | Min Cost Climbing Stairs | [LC](https://leetcode.com/problems/min-cost-climbing-stairs/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |
| 187 | 203 | Perfect Squares | [LC](https://leetcode.com/problems/perfect-squares/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |

### Grid DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 40 | 43 | Unique Paths | [LC](https://leetcode.com/problems/unique-paths/) | [UniquePaths.java](../../src/main/java/org/chijai/day9/dp/session1/UniquePaths.java) |

### DP / patience sorting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 41 | 45 | Longest Increasing Subsequence | [LC](https://leetcode.com/problems/longest-increasing-subsequence/) | [LIS.java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) |
| 188 | 204 | Number of Longest Increasing Subsequence | [LC](https://leetcode.com/problems/number-of-longest-increasing-subsequence/) | [LIS.java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) |
| 189 | 205 | Russian Doll Envelopes | [LC](https://leetcode.com/problems/russian-doll-envelopes/) | [LIS.java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) |

### DP + binary search

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 81 | 87 | Maximum Profit In Job Scheduling | [LC](https://leetcode.com/problems/maximum-profit-in-job-scheduling/) | [MaximumProfitInJobScheduling.java](../../src/main/java/org/chijai/day2/session3/MaximumProfitInJobScheduling.java) |

### 2D DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 159 | 169 | Edit Distance | [LC](https://leetcode.com/problems/edit-distance/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |
| 160 | 170 | Distinct Subsequences | [LC](https://leetcode.com/problems/distinct-subsequences/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |
| 180 | 196 | Interleaving String | [LC](https://leetcode.com/problems/interleaving-string/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |
| 181 | 197 | Longest Common Subsequence | [LC](https://leetcode.com/problems/longest-common-subsequence/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |
| 182 | 198 | Delete Operation for Two Strings | [LC](https://leetcode.com/problems/delete-operation-for-two-strings/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |
| 183 | 199 | Longest Palindromic Subsequence | [LC](https://leetcode.com/problems/longest-palindromic-subsequence/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |
| 184 | 200 | Minimum ASCII Delete Sum for Two Strings | [LC](https://leetcode.com/problems/minimum-ascii-delete-sum-for-two-strings/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |

### Stock DP variants

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 174 | 190 | Best Time to Buy and Sell Stock with Transaction Fee | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/) | [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java), [StockSeries2.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) |
| 175 | 191 | Best Time to Buy and Sell Stock with Cooldown | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/) | [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java), [StockSeries2.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) |
| 177 | 193 | Best Time to Buy and Sell Stock IV | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/) | [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java), [StockSeries2.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) |

### Greedy / DP states

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 176 | 192 | Best Time to Buy and Sell Stock III | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/) | [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java), [StockSeries2.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) |

### Contribution counting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 178 | 194 | Distinct Subsequences II | [LC](https://leetcode.com/problems/distinct-subsequences-ii/) | [CountUniqueChars.java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java), [CountUniqueChars.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/CountUniqueChars.java) |

### Dynamic Programming

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 206 | - | Stock Price Fluctuation | [LC](https://leetcode.com/problems/stock-price-fluctuation/) | [StockPriceFluctuation.java](../../src/main/java/org/chijai/trading/StockPriceFluctuation.java) |


## Backtracking / Combinatorial DFS

### Backtracking subsets

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 31 | 34 | Subsets | [LC](https://leetcode.com/problems/subsets/) | [Subsets.java](../../src/main/java/org/chijai/day11/backtracking/session1/Subsets.java) |

### Backtracking reuse

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 42 | 46 | Combination Sum | [LC](https://leetcode.com/problems/combination-sum/) | [CombinationSum.java](../../src/main/java/org/chijai/day11/backtracking/session1/CombinationSum.java) |

### DFS backtracking

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 44 | 48 | Word Search | [LC](https://leetcode.com/problems/word-search/) | [WordSearch.java](../../src/main/java/org/chijai/day8/graph/session1/WordSearch.java) |

### Backtracking / mapping

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 137 | 146 | Letter Combinations Of A Phone Number | [LC](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) | [LetterCombinationsOfAPhoneNumber.java](../../src/main/java/org/chijai/day11/backtracking/session1/LetterCombinationsOfAPhoneNumber.java) |

### Backtracking permutations

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 138 | 147 | Permutations | [LC](https://leetcode.com/problems/permutations/) | [BacktrackingRecursion.java](../../src/main/java/org/chijai/day11/backtracking/BacktrackingRecursion.java), [Permutations.java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) |
| 170 | 181 | Permutations II | [LC](https://leetcode.com/problems/permutations-ii/) | [Permutations.java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) |

### Backtracking / Combinatorial DFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 204 | - | N-Queens | [LC](https://leetcode.com/problems/n-queens/) | [NQueens.java](../../src/main/java/org/chijai/day11/backtracking/session1/NQueens.java) |
| 205 | - | Sudoku Solver | [LC](https://leetcode.com/problems/sudoku-solver/) | [SudokuSolver.java](../../src/main/java/org/chijai/day11/backtracking/session1/SudokuSolver.java) |


## Stack / Monotonic Stack

### Stack

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 32 | 35 | Valid Parentheses | [LC](https://leetcode.com/problems/valid-parentheses/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java), [ValidParentheses.java](../../src/main/java/org/chijai/day5/stack/session3/ValidParentheses.java) |
| 97 | 104 | Evaluate Reverse Polish Notation | [LC](https://leetcode.com/problems/evaluate-reverse-polish-notation/) | [EvalRPN.java](../../src/main/java/org/chijai/day5/stack/session3/EvalRPN.java) |

### Monotonic stack

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 34 | 37 | Daily Temperatures | [LC](https://leetcode.com/problems/daily-temperatures/) | [DailyTemperatures.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/DailyTemperatures.java), [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) |
| 96 | 103 | Next Greater Element II | [LC](https://leetcode.com/problems/next-greater-element-ii/) | [NextGreaterElement.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/NextGreaterElement.java) |

### Stack/queue design

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 86 | 93 | Sliding Window Maximum | [LC](https://leetcode.com/problems/sliding-window-maximum/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java), [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) |
| 125 | 134 | Implement Queue Using Stacks | [LC](https://leetcode.com/problems/implement-queue-using-stacks/) | [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |
| 126 | 135 | Implement Stack Using Queues | [LC](https://leetcode.com/problems/implement-stack-using-queues/) | [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |

### Stack / expression parsing

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 99 | 106 | Basic Calculator | [LC](https://leetcode.com/problems/basic-calculator/) | [BasicCalculator.java](../../src/main/java/org/chijai/day5/stack/session3/BasicCalculator.java) |

### Stack design

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 123 | 132 | Min Stack | [LC](https://leetcode.com/problems/min-stack/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java), [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |
| 124 | 133 | Max Stack | [LC](https://leetcode.com/problems/max-stack/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) |
| 127 | 136 | Next Greater Element I | [LC](https://leetcode.com/problems/next-greater-element-i/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java), [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |
| 128 | 137 | Online Stock Span | [LC](https://leetcode.com/problems/online-stock-span/) | [OnlineStockSpan.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/OnlineStockSpan.java), [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) |
| 148 | 157 | Design A Stack With Increment Operation | [LC](https://leetcode.com/problems/design-a-stack-with-increment-operation/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) |

### Stack / Monotonic Stack

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 199 | - | Remove K Digits | [LC](https://leetcode.com/problems/remove-k-digits/) | [RemoveKDigits.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/RemoveKDigits.java) |
| 200 | - | Sum of Subarray Minimums | [LC](https://leetcode.com/problems/sum-of-subarray-minimums/) | [SumOfSubarrayMinimums.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SumOfSubarrayMinimums.java) |


## Trie

### Trie

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 36 | 39 | Implement Trie (Prefix Tree) | [LC](https://leetcode.com/problems/implement-trie-prefix-tree/) | [MaximumXOR.java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java), [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java), [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) |
| 88 | 95 | Design Add and Search Words Data Structure | [LC](https://leetcode.com/problems/design-add-and-search-words-data-structure/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java), [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) |
| 150 | 159 | Longest Common Prefix | [LC](https://leetcode.com/problems/longest-common-prefix/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) |
| 152 | 161 | Replace Words | [LC](https://leetcode.com/problems/replace-words/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java), [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) |
| 153 | 162 | Search Suggestions System | [LC](https://leetcode.com/problems/search-suggestions-system/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java), [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) |
| 154 | 163 | Short Encoding of Words | [LC](https://leetcode.com/problems/short-encoding-of-words/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) |

### Trie + backtracking

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 95 | 102 | Word Search II | [LC](https://leetcode.com/problems/word-search-ii/) | [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java), [WordSearchII.java](../../src/main/java/org/chijai/day10/session1/trie/WordSearchII.java) |

### Binary trie / bit

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 147 | 156 | Maximum XOR of Two Numbers in an Array | [LC](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/) | [MaximumXOR.java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) |
| 156 | 165 | Maximum XOR With an Element From Array | [LC](https://leetcode.com/problems/maximum-xor-with-an-element-from-array/) | [MaximumXOR.java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) |
| 157 | 166 | Maximum Genetic Difference Query | [LC](https://leetcode.com/problems/maximum-genetic-difference-query/) | [MaximumXOR.java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) |
| 158 | 167 | Count Pairs With XOR in a Range | [LC](https://leetcode.com/problems/count-pairs-with-xor-in-a-range/) | [MaximumXOR.java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) |

### Trie + DFS wildcard

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 151 | 160 | Longest Word in Dictionary | [LC](https://leetcode.com/problems/longest-word-in-dictionary/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java), [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) |
| 155 | 164 | Map Sum Pairs | [LC](https://leetcode.com/problems/map-sum-pairs/) | [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) |


## Intervals / Sorting Greedy

### Intervals / sorting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 39 | 42 | Minimum Number Of Arrows To Burst Balloons | [LC](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/) | [IntervalGreedyByEnd.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalGreedyByEnd.java) |
| 135 | 144 | Non Overlapping Intervals | [LC](https://leetcode.com/problems/non-overlapping-intervals/) | [IntervalGreedyByEnd.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalGreedyByEnd.java) |

### Intervals / merge

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 85 | 92 | Meeting Rooms | [LC](https://leetcode.com/problems/meeting-rooms/) | [IntervalSortByStart.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalSortByStart.java) |
| 133 | 142 | Insert Interval | [LC](https://leetcode.com/problems/insert-interval/) | [IntervalSortByStart.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalSortByStart.java) |
| 134 | 143 | Merge Intervals | [LC](https://leetcode.com/problems/merge-intervals/) | [IntervalSortByStart.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalSortByStart.java) |

### Greedy last-occurrence boundary

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 136 | 145 | Partition Labels | [LC](https://leetcode.com/problems/partition-labels/) | [CountUniqueChars.java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java), [CountUniqueChars.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/CountUniqueChars.java) |

### DP / patience sorting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 190 | 206 | Maximum Length of Pair Chain | [LC](https://leetcode.com/problems/maximum-length-of-pair-chain/) | [LIS.java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) |

### Intervals / Sorting Greedy

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 203 | - | Car Pooling | [LC](https://leetcode.com/problems/car-pooling/) | [BoundaryDelta.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/BoundaryDelta.java) |


## Union Find / DSU

### Union Find / graph

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 64 | 70 | Accounts Merge | [LC](https://leetcode.com/problems/accounts-merge/) | [AccountsMerge.java](../../src/main/java/org/chijai/day8/graph/session3/AccountsMerge.java) |


## Greedy

### Greedy / DP states

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 66 | 72 | Best Time to Buy and Sell Stock | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/) | [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java), [StockSeries2.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) |
| 74 | 80 | Best Time to Buy and Sell Stock II | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/) | [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java), [StockSeries2.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) |

### Greedy

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 70 | 76 | Gas Station | [LC](https://leetcode.com/problems/gas-station/) | [GasStation.java](../../src/main/java/org/chijai/day9/dp/session1/GasStation.java) |
| 72 | 78 | Jump Game | [LC](https://leetcode.com/problems/jump-game/) | [GasStation.java](../../src/main/java/org/chijai/day9/dp/session1/GasStation.java) |


## Design Data Structures

### HashMap + doubly linked list

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 82 | 89 | First Unique Number | [LC](https://leetcode.com/problems/first-unique-number/) | [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) |

### LLD / URL shortener

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 172 | 183 | Encode And Decode Tinyurl | [LC](https://leetcode.com/problems/encode-and-decode-tinyurl/) | [DesignUrlShortner.java](../../src/main/java/org/chijai/design/lld/DesignUrlShortner.java) |

### Stack/queue design

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 173 | 184 | Design Circular Queue | [LC](https://leetcode.com/problems/design-circular-queue/) | [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |

### Design Data Structures

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 210 | - | Design A Leaderboard | [LC](https://leetcode.com/problems/design-a-leaderboard/) | [DesignALeaderboard.java](../../src/main/java/org/chijai/design/lld/DesignALeaderboard.java) |
| 211 | - | Design an Ordered Stream | [LC](https://leetcode.com/problems/design-an-ordered-stream/) | [DesignOrderedStream.java](../../src/main/java/org/chijai/design/lld/DesignOrderedStream.java) |
| 212 | - | Design Hit Counter | [LC](https://leetcode.com/problems/design-hit-counter/) | [DesignHitCounter.java](../../src/main/java/org/chijai/design/lld/DesignHitCounter.java) |
| 213 | - | Design Parking System | [LC](https://leetcode.com/problems/design-parking-system/) | [DesignParkingSystem.java](../../src/main/java/org/chijai/design/lld/DesignParkingSystem.java) |


## Math / Bit / String

### KMP string matching

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 98 | 105 | Find The Index Of The First Occurrence In A String | [LC](https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/) | [KmpPatterns.java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java), [LongestHappyPrefix.java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java), [ZFunction.java](../../src/main/java/org/chijai/day7/session2/ZFunction.java) |
| 146 | 155 | Repeated Substring Pattern | [LC](https://leetcode.com/problems/repeated-substring-pattern/) | [KmpPatterns.java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java), [LongestHappyPrefix.java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) |
| 164 | 175 | Shortest Palindrome | [LC](https://leetcode.com/problems/shortest-palindrome/) | [KmpPatterns.java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java), [LongestHappyPrefix.java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) |

### KMP / rolling hash

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 149 | 158 | Longest Happy Prefix | [LC](https://leetcode.com/problems/longest-happy-prefix/) | [KmpPatterns.java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java), [LongestHappyPrefix.java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) |

### Bit/string addition

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 161 | 171 | Add Binary | [LC](https://leetcode.com/problems/add-binary/) | [AddBinary.java](../../src/main/java/org/chijai/day10/session2/AddBinary.java) |

### Math / sieve

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 162 | 172 | Count Primes | [LC](https://leetcode.com/problems/count-primes/) | [CountPrimes.java](../../src/main/java/org/chijai/day10/session2/CountPrimes.java) |

### Contribution counting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 163 | 173 | Count Unique Characters of All Substrings of a Given String | [LC](https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/) | [CountUniqueChars.java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java), [CountUniqueChars.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/CountUniqueChars.java) |


## Basics / Implementation

### Matrix boundary traversal

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 144 | 153 | Spiral Matrix | [LC](https://leetcode.com/problems/spiral-matrix/) | [SpiralMatrix.java](../../src/main/java/org/chijai/day1/Arrays/session1/SpiralMatrix.java) |

### Parsing / edge cases

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 145 | 154 | String To Integer Atoi | [LC](https://leetcode.com/problems/string-to-integer-atoi/) | [StringToIntegerAtoi.java](../../src/main/java/org/chijai/day3/session3/StringToIntegerAtoi.java) |

### Basics / Implementation

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 207 | - | Missing Number | [LC](https://leetcode.com/problems/missing-number/) | [MissingNumber.java](../../src/main/java/org/chijai/day10/session2/MissingNumber.java) |
| 208 | - | Missing Ranges | [LC](https://leetcode.com/problems/missing-ranges/) | [MissingRanges.java](../../src/main/java/org/chijai/trading/MissingRanges.java) |
| 209 | - | Number of Orders in the Backlog | [LC](https://leetcode.com/problems/number-of-orders-in-the-backlog/) | [NumberOfOrdersInTheBacklog.java](../../src/main/java/org/chijai/trading/NumberOfOrdersInTheBacklog.java) |