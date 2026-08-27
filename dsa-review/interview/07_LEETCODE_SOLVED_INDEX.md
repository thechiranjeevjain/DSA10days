# LeetCode Solved Index

Recursive source scan: this is the book-style table of contents for LeetCode problems found in Java source files.

Regenerate it with `dsa-review\scripts\build-interview-cockpit.cmd` or `verify-all.cmd` after adding or editing Java solution files.

Use [Zero To Hero Ranked Table](01_ZERO_TO_HERO_RANKED_TABLE.md) for interview crunch order. Use this file when you want the complete source-backed LeetCode inventory.

| Metric | Count |
|---|---:|
| Unique LeetCode problems found recursively | 155 |
| Also present in interview-ranked cockpit | 154 |
| Extra source-discovered problems | 1 |
| Problems appearing in multiple Java files | 29 |

## Table Of Contents

- [Binary Search / Answer Search (13)](#binary-search-answer-search)
- [Sliding Window (9)](#sliding-window)
- [Prefix Sum / Prefix-Suffix (2)](#prefix-sum-prefixsuffix)
- [Linked List Pointers (17)](#linked-list-pointers)
- [HashMap / Frequency / Set (3)](#hashmap-frequency-set)
- [Two Pointers (4)](#two-pointers)
- [Tree BFS / Level Order (2)](#tree-bfs-level-order)
- [Tree DFS / Recursion (31)](#tree-dfs-recursion)
- [Graph DFS / Components (9)](#graph-dfs-components)
- [Graph BFS / Shortest Path (6)](#graph-bfs-shortest-path)
- [Intervals / Sorting Greedy (4)](#intervals-sorting-greedy)
- [Stack / Monotonic Stack (17)](#stack-monotonic-stack)
- [Heap / Priority Queue (7)](#heap-priority-queue)
- [Dynamic Programming (8)](#dynamic-programming)
- [Backtracking / Combinatorial DFS (7)](#backtracking-combinatorial-dfs)
- [Trie (4)](#trie)
- [Union Find / DSU (1)](#union-find-dsu)
- [Topological Sort (1)](#topological-sort)
- [Math / Bit / String (7)](#math-bit-string)
- [Basics / Implementation (2)](#basics-implementation)
- [Design Data Structures (1)](#design-data-structures)

## Binary Search / Answer Search

### Binary search invariant

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 1 | 2 | Binary Search | [LC](https://leetcode.com/problems/binary-search/) | [BinarySearch.java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) |
| 77 | 83 | Search Insert Position | [LC](https://leetcode.com/problems/search-insert-position/) | [BinarySearch.java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java), [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 82 | 88 | First Bad Version | [LC](https://leetcode.com/problems/first-bad-version/) | [BinarySearch.java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) |

### Binary search on answer

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 18 | 20 | Koko Eating Bananas | [LC](https://leetcode.com/problems/koko-eating-bananas/) | [AGGRCOW.java](../../src/main/java/org/chijai/day2/session2/AGGRCOW.java), [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) |
| 83 | 89 | Split Array Largest Sum | [LC](https://leetcode.com/problems/split-array-largest-sum/) | [AGGRCOW.java](../../src/main/java/org/chijai/day2/session2/AGGRCOW.java), [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) |
| 86 | 93 | Capacity To Ship Packages Within D Days | [LC](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/) | [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) |
| 87 | 94 | Minimum Number Of Days To Make M Bouquets | [LC](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/) | [KokoBananas.java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) |

### Binary search boundary

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 19 | 21 | Search In Rotated Sorted Array | [LC](https://leetcode.com/problems/search-in-rotated-sorted-array/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 20 | 22 | Find First And Last Position Of Element In Sorted Array | [LC](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 73 | 79 | Search In Rotated Sorted Array Ii | [LC](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 81 | 87 | Find Peak Element | [LC](https://leetcode.com/problems/find-peak-element/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |
| 122 | 129 | Sqrtx | [LC](https://leetcode.com/problems/sqrtx/) | [SearchRange.java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) |

### HashMap + binary search

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 97 | 104 | Time Based Key Value Store | [LC](https://leetcode.com/problems/time-based-key-value-store/) | [TimeBasedKeyValueStore.java](../../src/main/java/org/chijai/day2/session3/TimeBasedKeyValueStore.java) |


## Sliding Window

### Sliding window / set

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 2 | 3 | Longest Substring Without Repeating Characters | [LC](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | [LongestSubString.java](../../src/main/java/org/chijai/day3/session1/LongestSubString.java), [LongestSubstringVariations.java](../../src/main/java/org/chijai/day3/session1/LongestSubstringVariations.java), [MinimumWindowSubstring.java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) |

### Sliding window / need-have

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 4 | 5 | Minimum Window Substring | [LC](https://leetcode.com/problems/minimum-window-substring/) | [LongestSubString.java](../../src/main/java/org/chijai/day3/session1/LongestSubString.java), [MinimumWindowSubstring.java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) |
| 48 | 51 | Longest Repeating Character Replacement | [LC](https://leetcode.com/problems/longest-repeating-character-replacement/) | [MinimumWindowSubstring.java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) |
| 50 | 53 | Permutation In String | [LC](https://leetcode.com/problems/permutation-in-string/) | [MinimumWindowSubstring.java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) |
| 58 | 63 | Substring With Concatenation Of All Words | [LC](https://leetcode.com/problems/substring-with-concatenation-of-all-words/) | [MinimumWindowSubstring.java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) |
| 107 | 114 | Minimum Size Subarray Sum | [LC](https://leetcode.com/problems/minimum-size-subarray-sum/) | [MinimumWindowSubstring.java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) |

### Sliding window frequency

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 43 | 46 | Find All Anagrams In A String | [LC](https://leetcode.com/problems/find-all-anagrams-in-a-string/) | [FindAllAnagramsInAString.java](../../src/main/java/org/chijai/day3/session3/FindAllAnagramsInAString.java) |

### Sliding window

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 49 | 52 | Longest Substring With At Most K Distinct Characters | [LC](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/) | [AtMostKDistinct.java](../../src/main/java/org/chijai/day3/session1/AtMostKDistinct.java), [LongestSubString.java](../../src/main/java/org/chijai/day3/session1/LongestSubString.java) |

### Prefix/window counting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 106 | 113 | Count Number Of Nice Subarrays | [LC](https://leetcode.com/problems/count-number-of-nice-subarrays/) | [NiceSubArrays.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/NiceSubArrays.java) |


## Prefix Sum / Prefix-Suffix

### Prefix/suffix

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 3 | 4 | Product Of Array Except Self | [LC](https://leetcode.com/problems/product-of-array-except-self/) | [ProductOfArrayExceptSelf.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/ProductOfArrayExceptSelf.java) |

### Prefix/window counting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 51 | 54 | Binary Subarrays With Sum | [LC](https://leetcode.com/problems/binary-subarrays-with-sum/) | [NiceSubArrays.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/NiceSubArrays.java) |


## Linked List Pointers

### Pointer reversal

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 5 | 6 | Reverse Linked List | [LC](https://leetcode.com/problems/reverse-linked-list/) | [ReverseLinkedList.java](../../src/main/java/org/chijai/day4/LinkedList/session1/ReverseLinkedList.java) |

### Fast/slow pointers

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 6 | 7 | Linked List Cycle | [LC](https://leetcode.com/problems/linked-list-cycle/) | [LinkedListCycle.java](../../src/main/java/org/chijai/day4/LinkedList/session1/LinkedListCycle.java) |
| 88 | 95 | Middle Of The Linked List | [LC](https://leetcode.com/problems/middle-of-the-linked-list/) | [MiddleOfLinkedList.java](../../src/main/java/org/chijai/day4/LinkedList/session4/MiddleOfLinkedList.java) |

### Merge / dummy node

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 7 | 8 | Merge Two Sorted Lists | [LC](https://leetcode.com/problems/merge-two-sorted-lists/) | [Merge2SortedLists.java](../../src/main/java/org/chijai/day4/LinkedList/session4/Merge2SortedLists.java) |

### Heap / divide and conquer

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 10 | 11 | Merge K Sorted Lists | [LC](https://leetcode.com/problems/merge-k-sorted-lists/) | [MergeKSortedLists.java](../../src/main/java/org/chijai/day4/LinkedList/session4/MergeKSortedLists.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) |

### HashMap + doubly linked list

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 21 | 23 | LRU Cache | [LC](https://leetcode.com/problems/lru-cache/) | [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) |
| 62 | 67 | First Unique Number | [LC](https://leetcode.com/problems/first-unique-number/) | [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) |
| 78 | 84 | Design Browser History | [LC](https://leetcode.com/problems/design-browser-history/) | [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) |
| 79 | 85 | Moving Average From Data Stream | [LC](https://leetcode.com/problems/moving-average-from-data-stream/) | [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java), [MovingAverage.java](../../src/main/java/org/chijai/day7/session1/heap/MovingAverage.java) |

### HashMap / interleaving copy

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 22 | 24 | Copy List With Random Pointer | [LC](https://leetcode.com/problems/copy-list-with-random-pointer/) | [CopyListWithRandomPointer.java](../../src/main/java/org/chijai/day4/LinkedList/session2/CopyListWithRandomPointer.java) |

### Linked list two pointers

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 53 | 57 | Intersection Of Two Linked Lists | [LC](https://leetcode.com/problems/intersection-of-two-linked-lists/) | [Intersection.java](../../src/main/java/org/chijai/day4/LinkedList/session1/Intersection.java) |

### Floyd cycle entry

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 54 | 58 | Linked List Cycle Ii | [LC](https://leetcode.com/problems/linked-list-cycle-ii/) | [LinkedListCycleII.java](../../src/main/java/org/chijai/day4/LinkedList/session4/LinkedListCycleII.java) |

### Linked-list reversal groups

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 55 | 59 | Reverse Nodes In K Group | [LC](https://leetcode.com/problems/reverse-nodes-in-k-group/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |
| 59 | 64 | Odd Even Linked List | [LC](https://leetcode.com/problems/odd-even-linked-list/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |
| 60 | 65 | Rotate List | [LC](https://leetcode.com/problems/rotate-list/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |
| 61 | 66 | Swap Nodes In Pairs | [LC](https://leetcode.com/problems/swap-nodes-in-pairs/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |
| 148 | 159 | Reverse Linked List Ii | [LC](https://leetcode.com/problems/reverse-linked-list-ii/) | [ReverseLinkedListNodesK.java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) |


## HashMap / Frequency / Set

### Frequency count

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 8 | 9 | Valid Anagram | [LC](https://leetcode.com/problems/valid-anagram/) | [ValidAnagram.java](../../src/main/java/org/chijai/day3/session3/ValidAnagram.java) |

### HashMap/frequency

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 52 | 56 | Ransom Note | [LC](https://leetcode.com/problems/ransom-note/) | [RansomNote.java](../../src/main/java/org/chijai/day1/Arrays/session1/RansomNote.java) |

### Hash/frequency

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 104 | 111 | Longest Palindrome | [LC](https://leetcode.com/problems/longest-palindrome/) | [LongestPalindrome.java](../../src/main/java/org/chijai/day3/session3/LongestPalindrome.java) |


## Two Pointers

### Two pointers

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 9 | 10 | Valid Palindrome | [LC](https://leetcode.com/problems/valid-palindrome/) | [ValidPalindrome.java](../../src/main/java/org/chijai/day3/session3/ValidPalindrome.java) |
| 11 | 12 | Container With Most Water | [LC](https://leetcode.com/problems/container-with-most-water/) | [ContainerWithMostWater.java](../../src/main/java/org/chijai/day5/stack/session2/ContainerWithMostWater.java) |

### Two pointers / stack

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 12 | 13 | Trapping Rain Water | [LC](https://leetcode.com/problems/trapping-rain-water/) | [TrappingRainwater.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/TrappingRainwater.java) |

### Expand around center

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 105 | 112 | Longest Palindromic Substring | [LC](https://leetcode.com/problems/longest-palindromic-substring/) | [LongestPalindromicSubstring.java](../../src/main/java/org/chijai/day3/session3/LongestPalindromicSubstring.java) |


## Tree BFS / Level Order

### Tree traversal

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 13 | 14 | Binary Tree Level Order Traversal | [LC](https://leetcode.com/problems/binary-tree-level-order-traversal/) | [BinaryTreeTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeTraversal.java) |

### Tree BFS / DFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 57 | 61 | Binary Tree Right Side View | [LC](https://leetcode.com/problems/binary-tree-right-side-view/) | [BinaryTreeSideView.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeSideView.java) |


## Tree DFS / Recursion

### Tree DFS / stack

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 14 | 15 | Validate Binary Search Tree | [LC](https://leetcode.com/problems/validate-binary-search-tree/) | [BinaryTreeInorderTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java), [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java), [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java), [ValidateBST.java](../../src/main/java/org/chijai/day6/trees/session3/ValidateBST.java) |
| 63 | 68 | Binary Tree Inorder Traversal | [LC](https://leetcode.com/problems/binary-tree-inorder-traversal/) | [BinaryTreeInorderTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) |
| 109 | 116 | Binary Tree Postorder Traversal | [LC](https://leetcode.com/problems/binary-tree-postorder-traversal/) | [BinaryTreeInorderTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) |
| 110 | 117 | Binary Tree Preorder Traversal | [LC](https://leetcode.com/problems/binary-tree-preorder-traversal/) | [BinaryTreeInorderTraversal.java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) |

### Tree DFS return contract

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 15 | 16 | Lowest Common Ancestor Of A Binary Tree | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/) | [LCA.java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) |
| 150 | 161 | Lowest Common Ancestor Of A Binary Tree Ii | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-ii/) | [LCA.java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) |
| 151 | 162 | Lowest Common Ancestor Of A Binary Tree Iii | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/) | [LCA.java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) |
| 152 | 163 | Lowest Common Ancestor Of A Binary Tree Iv | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iv/) | [LCA.java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) |

### BST inorder

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 23 | 25 | Kth Smallest Element In A BST | [LC](https://leetcode.com/problems/kth-smallest-element-in-a-bst/) | [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java), [KthSmallestElementInBST.java](../../src/main/java/org/chijai/day6/trees/session3/KthSmallestElementInBST.java) |
| 115 | 122 | Recover Binary Search Tree | [LC](https://leetcode.com/problems/recover-binary-search-tree/) | [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) |
| 116 | 123 | Binary Search Tree Iterator | [LC](https://leetcode.com/problems/binary-search-tree-iterator/) | [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) |
| 117 | 124 | Convert BST To Greater Tree | [LC](https://leetcode.com/problems/convert-bst-to-greater-tree/) | [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) |

### Core tree patterns

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 24 | 26 | Balanced Binary Tree | [LC](https://leetcode.com/problems/balanced-binary-tree/) | [BinaryTree.java](../../src/main/java/org/chijai/day6/trees/session3/BinaryTree.java) |
| 25 | 27 | Diameter Of Binary Tree | [LC](https://leetcode.com/problems/diameter-of-binary-tree/) | [BinaryTree.java](../../src/main/java/org/chijai/day6/trees/session3/BinaryTree.java) |
| 74 | 80 | Maximum Depth Of Binary Tree | [LC](https://leetcode.com/problems/maximum-depth-of-binary-tree/) | [BinaryTree.java](../../src/main/java/org/chijai/day6/trees/session3/BinaryTree.java) |

### Tree path DFS / global answer

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 26 | 28 | Path Sum Iii | [LC](https://leetcode.com/problems/path-sum-iii/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |
| 66 | 71 | Sum Root To Leaf Numbers | [LC](https://leetcode.com/problems/sum-root-to-leaf-numbers/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |
| 93 | 100 | Binary Tree Maximum Path Sum | [LC](https://leetcode.com/problems/binary-tree-maximum-path-sum/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |
| 108 | 115 | Path Sum | [LC](https://leetcode.com/problems/path-sum/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |
| 149 | 160 | Path Sum Ii | [LC](https://leetcode.com/problems/path-sum-ii/) | [BinaryTreePathProblems.java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) |

### BST property

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 56 | 60 | Lowest Common Ancestor Of A Binary Search Tree | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) |
| 111 | 118 | Insert Into A Binary Search Tree | [LC](https://leetcode.com/problems/insert-into-a-binary-search-tree/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) |
| 112 | 119 | Minimum Absolute Difference In BST | [LC](https://leetcode.com/problems/minimum-absolute-difference-in-bst/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java), [RecoverBST.java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) |
| 113 | 120 | Range Sum Of BST | [LC](https://leetcode.com/problems/range-sum-of-bst/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) |
| 114 | 121 | Search In A Binary Search Tree | [LC](https://leetcode.com/problems/search-in-a-binary-search-tree/) | [LCA_BST.java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) |

### Tree DFS/BFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 64 | 69 | Invert Binary Tree | [LC](https://leetcode.com/problems/invert-binary-tree/) | [InvertBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session3/InvertBinaryTree.java) |

### Tree recursion / hashmap index

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 65 | 70 | Construct Binary Search Tree From Preorder Traversal | [LC](https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/) | [ConstructTree.java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) |
| 80 | 86 | Verify Preorder Serialization Of A Binary Tree | [LC](https://leetcode.com/problems/verify-preorder-serialization-of-a-binary-tree/) | [ConstructTree.java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) |
| 90 | 97 | Construct Binary Tree From Inorder And Postorder Traversal | [LC](https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/) | [ConstructTree.java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) |
| 92 | 99 | Construct Binary Tree From Preorder And Inorder Traversal | [LC](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/) | [ConstructTree.java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) |

### Tree BFS/DFS serialization

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 72 | 78 | Serialize And Deserialize Binary Tree | [LC](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/) | [SerializeAndDeserializeBinaryTree.java](../../src/main/java/org/chijai/day6/trees/session2/SerializeAndDeserializeBinaryTree.java) |


## Graph DFS / Components

### Matrix DFS/BFS components

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 16 | 17 | Number Of Islands | [LC](https://leetcode.com/problems/number-of-islands/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |
| 68 | 74 | Pacific Atlantic Water Flow | [LC](https://leetcode.com/problems/pacific-atlantic-water-flow/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |
| 69 | 75 | Surrounded Regions | [LC](https://leetcode.com/problems/surrounded-regions/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |
| 119 | 126 | Number Of Closed Islands | [LC](https://leetcode.com/problems/number-of-closed-islands/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |
| 120 | 127 | Max Area Of Island | [LC](https://leetcode.com/problems/max-area-of-island/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |

### Matrix DFS/BFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 32 | 34 | Flood Fill | [LC](https://leetcode.com/problems/flood-fill/) | [FloodFill.java](../../src/main/java/org/chijai/day8/graph/session1/FloodFill.java), [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |

### BFS/DFS coloring

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 33 | 35 | Is Graph Bipartite | [LC](https://leetcode.com/problems/is-graph-bipartite/) | [GraphBipartite.java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) |

### Graph DFS/BFS clone

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 76 | 82 | Clone Graph | [LC](https://leetcode.com/problems/clone-graph/) | [CopyListWithRandomPointer.java](../../src/main/java/org/chijai/day4/LinkedList/session2/CopyListWithRandomPointer.java), [CloneGraph.java](../../src/main/java/org/chijai/day8/graph/session2/CloneGraph.java) |

### Matrix DFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 121 | 128 | Coloring A Border | [LC](https://leetcode.com/problems/coloring-a-border/) | [ColoringABorder.java](../../src/main/java/org/chijai/day8/graph/session1/ColoringABorder.java) |


## Graph BFS / Shortest Path

### BFS shortest path

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 17 | 19 | Word Ladder | [LC](https://leetcode.com/problems/word-ladder/) | [WordLadder.java](../../src/main/java/org/chijai/day8/graph/session3/WordLadder.java) |

### Multi-source BFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 27 | 29 | Rotting Oranges | [LC](https://leetcode.com/problems/rotting-oranges/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java), [RottenOranges.java](../../src/main/java/org/chijai/day8/graph/session1/RottenOranges.java) |
| 28 | 30 | 01 Matrix | [LC](https://leetcode.com/problems/01-matrix/) | [Matrix01.java](../../src/main/java/org/chijai/day8/graph/session1/Matrix01.java) |

### Dijkstra / graph

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 67 | 73 | Network Delay Time | [LC](https://leetcode.com/problems/network-delay-time/) | [NetworkDelayTime.java](../../src/main/java/org/chijai/day8/graph/session2/NetworkDelayTime.java) |

### Matrix DFS/BFS components

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 75 | 81 | Number Of Provinces | [LC](https://leetcode.com/problems/number-of-provinces/) | [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) |

### BFS + sorting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 118 | 125 | K Highest Ranked Items Within A Price Range | [LC](https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/) | [KHighestRankedItemsWithinAPriceRange.java](../../src/main/java/org/chijai/day8/graph/session3/KHighestRankedItemsWithinAPriceRange.java) |


## Intervals / Sorting Greedy

### Intervals / sorting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 29 | 31 | Meeting Rooms Ii | [LC](https://leetcode.com/problems/meeting-rooms-ii/) | [MinimumPlatforms.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/MinimumPlatforms.java) |
| 35 | 38 | Minimum Number Of Arrows To Burst Balloons | [LC](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/) | [MinimumPlatforms.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/MinimumPlatforms.java) |
| 132 | 141 | Car Pooling | [LC](https://leetcode.com/problems/car-pooling/) | [MinimumPlatforms.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/MinimumPlatforms.java) |

### Greedy

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 131 | 140 | Gas Station | [LC](https://leetcode.com/problems/gas-station/) | [GasStation.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/GasStation.java) |


## Stack / Monotonic Stack

### Monotonic stack

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 30 | 32 | Daily Temperatures | [LC](https://leetcode.com/problems/daily-temperatures/) | [DailyTemperatures.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/DailyTemperatures.java), [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) |
| 42 | 45 | Largest Rectangle In Histogram | [LC](https://leetcode.com/problems/largest-rectangle-in-histogram/) | [LargestRectangle.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java) |
| 99 | 106 | Next Greater Element Ii | [LC](https://leetcode.com/problems/next-greater-element-ii/) | [NextGreaterElement.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/NextGreaterElement.java) |
| 100 | 107 | Sum Of Subarray Minimums | [LC](https://leetcode.com/problems/sum-of-subarray-minimums/) | [LargestRectangle.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java) |
| 123 | 130 | Maximal Rectangle | [LC](https://leetcode.com/problems/maximal-rectangle/) | [LargestRectangle.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java) |

### Stack

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 31 | 33 | Valid Parentheses | [LC](https://leetcode.com/problems/valid-parentheses/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java), [ValidParentheses.java](../../src/main/java/org/chijai/day5/stack/session3/ValidParentheses.java) |
| 101 | 108 | Evaluate Reverse Polish Notation | [LC](https://leetcode.com/problems/evaluate-reverse-polish-notation/) | [EvalRPN.java](../../src/main/java/org/chijai/day5/stack/session3/EvalRPN.java) |

### Stack/queue design

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 89 | 96 | Sliding Window Maximum | [LC](https://leetcode.com/problems/sliding-window-maximum/) | [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java), [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) |
| 126 | 133 | Implement Queue Using Stacks | [LC](https://leetcode.com/problems/implement-queue-using-stacks/) | [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |
| 127 | 134 | Implement Stack Using Queues | [LC](https://leetcode.com/problems/implement-stack-using-queues/) | [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |
| 140 | 149 | Design Circular Queue | [LC](https://leetcode.com/problems/design-circular-queue/) | [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |

### Stack / expression parsing

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 103 | 110 | Basic Calculator | [LC](https://leetcode.com/problems/basic-calculator/) | [BasicCalculator.java](../../src/main/java/org/chijai/day5/stack/session3/BasicCalculator.java) |

### Stack design

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 124 | 131 | Min Stack | [LC](https://leetcode.com/problems/min-stack/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java), [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |
| 125 | 132 | Max Stack | [LC](https://leetcode.com/problems/max-stack/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) |
| 128 | 135 | Next Greater Element I | [LC](https://leetcode.com/problems/next-greater-element-i/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java), [StackQueue.java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) |
| 129 | 136 | Online Stock Span | [LC](https://leetcode.com/problems/online-stock-span/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) |
| 139 | 148 | Design A Stack With Increment Operation | [LC](https://leetcode.com/problems/design-a-stack-with-increment-operation/) | [MinStackDesign.java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) |


## Heap / Priority Queue

### Heap fundamentals

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 34 | 37 | Top K Frequent Elements | [LC](https://leetcode.com/problems/top-k-frequent-elements/) | [HeapSort.java](../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java), [TopKFrequentTransactions.java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentTransactions.java) |
| 146 | 157 | Sort Characters By Frequency | [LC](https://leetcode.com/problems/sort-characters-by-frequency/) | [HeapSort.java](../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java) |

### Two heaps

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 41 | 44 | Find Median From Data Stream | [LC](https://leetcode.com/problems/find-median-from-data-stream/) | [HeapSort.java](../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java), [Median.java](../../src/main/java/org/chijai/day7/session1/heap/Median.java) |

### Greedy / heap

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 94 | 101 | Task Scheduler | [LC](https://leetcode.com/problems/task-scheduler/) | [TaskScheduler.java](../../src/main/java/org/chijai/day7/session1/heap/TaskScheduler.java) |

### Min-heap size K

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 95 | 102 | Kth Largest Element In An Array | [LC](https://leetcode.com/problems/kth-largest-element-in-an-array/) | [HeapSort.java](../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) |
| 96 | 103 | Kth Largest Element In A Stream | [LC](https://leetcode.com/problems/kth-largest-element-in-a-stream/) | [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) |

### Heap / quickselect

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 130 | 138 | K Closest Points To Origin | [LC](https://leetcode.com/problems/k-closest-points-to-origin/) | [KClosestPointsToOrigin.java](../../src/main/java/org/chijai/day7/session1/heap/KClosestPointsToOrigin.java), [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) |


## Dynamic Programming

### 1D DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 36 | 39 | House Robber | [LC](https://leetcode.com/problems/house-robber/) | [HouseRobber.java](../../src/main/java/org/chijai/day9/dp/session1/HouseRobber.java) |

### Unbounded knapsack DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 37 | 40 | Coin Change | [LC](https://leetcode.com/problems/coin-change/) | [CoinChange.java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) |

### Grid DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 45 | 48 | Unique Paths | [LC](https://leetcode.com/problems/unique-paths/) | [UniquePaths.java](../../src/main/java/org/chijai/day9/dp/session1/UniquePaths.java) |

### 0/1 knapsack DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 46 | 49 | Partition Equal Subset Sum | [LC](https://leetcode.com/problems/partition-equal-subset-sum/) | [PartitionEqualSubsetSum.java](../../src/main/java/org/chijai/day9/dp/session2/PartitionEqualSubsetSum.java) |

### DP / patience sorting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 47 | 50 | Longest Increasing Subsequence | [LC](https://leetcode.com/problems/longest-increasing-subsequence/) | [LIS.java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) |

### DP + binary search

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 84 | 91 | Maximum Profit In Job Scheduling | [LC](https://leetcode.com/problems/maximum-profit-in-job-scheduling/) | [MaximumProfitInJobScheduling.java](../../src/main/java/org/chijai/day2/session3/MaximumProfitInJobScheduling.java) |

### Greedy / DP states

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 85 | 92 | Best Time To Buy And Sell Stock | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/) | [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java) |

### 2D DP

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 142 | 152 | Edit Distance | [LC](https://leetcode.com/problems/edit-distance/) | [EditDistance.java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) |


## Backtracking / Combinatorial DFS

### Backtracking subsets

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 38 | 41 | Subsets | [LC](https://leetcode.com/problems/subsets/) | [Subsets.java](../../src/main/java/org/chijai/day11/backtracking/session1/Subsets.java) |

### Backtracking reuse

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 39 | 42 | Combination Sum | [LC](https://leetcode.com/problems/combination-sum/) | [CombinationSum.java](../../src/main/java/org/chijai/day11/backtracking/session1/CombinationSum.java) |

### DFS backtracking

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 40 | 43 | Word Search | [LC](https://leetcode.com/problems/word-search/) | [WordSearch.java](../../src/main/java/org/chijai/day8/graph/session1/WordSearch.java) |

### Backtracking / mapping

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 133 | 142 | Letter Combinations Of A Phone Number | [LC](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) | [LetterCombinationsOfAPhoneNumber.java](../../src/main/java/org/chijai/day11/backtracking/session1/LetterCombinationsOfAPhoneNumber.java) |

### Backtracking permutations

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 134 | 143 | Permutations | [LC](https://leetcode.com/problems/permutations/) | [BacktrackingRecursion.java](../../src/main/java/org/chijai/day11/backtracking/BacktrackingRecursion.java), [Permutations.java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) |
| 153 | 164 | Permutations Ii | [LC](https://leetcode.com/problems/permutations-ii/) | [Permutations.java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) |

### Backtracking / Combinatorial DFS

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 155 | - | Permutation | [LC](https://leetcode.com/problems/permutation/) | [BacktrackingRecursion.java](../../src/main/java/org/chijai/day11/backtracking/BacktrackingRecursion.java) |


## Trie

### Trie

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 44 | 47 | Implement Trie Prefix Tree | [LC](https://leetcode.com/problems/implement-trie-prefix-tree/) | [TriePrefix.java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) |

### Trie + DFS wildcard

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 91 | 98 | Design Add And Search Words Data Structure | [LC](https://leetcode.com/problems/design-add-and-search-words-data-structure/) | [TrieWordDictionary.java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) |

### Trie + backtracking

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 98 | 105 | Word Search Ii | [LC](https://leetcode.com/problems/word-search-ii/) | [WordSearchII.java](../../src/main/java/org/chijai/day10/session1/trie/WordSearchII.java) |

### Binary trie / bit

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 138 | 147 | Maximum XOR Of Two Numbers In An Array | [LC](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/) | [MaximumXOR.java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) |


## Union Find / DSU

### Union Find / graph

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 70 | 76 | Accounts Merge | [LC](https://leetcode.com/problems/accounts-merge/) | [AccountsMerge.java](../../src/main/java/org/chijai/day8/graph/session3/AccountsMerge.java) |


## Topological Sort

### Topological trimming

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 71 | 77 | Minimum Height Trees | [LC](https://leetcode.com/problems/minimum-height-trees/) | [MinHTree.java](../../src/main/java/org/chijai/day8/graph/session3/MinHTree.java) |


## Math / Bit / String

### KMP string matching

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 102 | 109 | Find The Index Of The First Occurrence In A String | [LC](https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/) | [KmpPatterns.java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java), [LongestHappyPrefix.java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java), [ZFunction.java](../../src/main/java/org/chijai/day7/session2/ZFunction.java) |
| 137 | 146 | Repeated Substring Pattern | [LC](https://leetcode.com/problems/repeated-substring-pattern/) | [KmpPatterns.java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java), [LongestHappyPrefix.java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) |
| 147 | 158 | Shortest Palindrome | [LC](https://leetcode.com/problems/shortest-palindrome/) | [KmpPatterns.java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java), [LongestHappyPrefix.java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) |

### KMP / rolling hash

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 141 | 150 | Longest Happy Prefix | [LC](https://leetcode.com/problems/longest-happy-prefix/) | [KmpPatterns.java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java), [LongestHappyPrefix.java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) |

### Bit/string addition

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 143 | 153 | Add Binary | [LC](https://leetcode.com/problems/add-binary/) | [AddBinary.java](../../src/main/java/org/chijai/day10/session2/AddBinary.java) |

### Math / sieve

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 144 | 154 | Count Primes | [LC](https://leetcode.com/problems/count-primes/) | [CountPrimes.java](../../src/main/java/org/chijai/day10/session2/CountPrimes.java) |

### Contribution counting

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 145 | 155 | Count Unique Characters Of All Substrings Of A Given String | [LC](https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/) | [CountUniqueChars.java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java), [CountUniqueChars.java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/CountUniqueChars.java) |


## Basics / Implementation

### Matrix boundary traversal

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 135 | 144 | Spiral Matrix | [LC](https://leetcode.com/problems/spiral-matrix/) | [SpiralMatrix.java](../../src/main/java/org/chijai/day1/Arrays/session1/SpiralMatrix.java) |

### Parsing / edge cases

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 136 | 145 | String To Integer Atoi | [LC](https://leetcode.com/problems/string-to-integer-atoi/) | [StringToIntegerAtoi.java](../../src/main/java/org/chijai/day3/session3/StringToIntegerAtoi.java) |


## Design Data Structures

### LLD / URL shortener

| # | Interview Rank | Problem | LeetCode | Local solution file(s) |
|---:|---:|---|---|---|
| 154 | 165 | Encode And Decode Tinyurl | [LC](https://leetcode.com/problems/encode-and-decode-tinyurl/) | [DesignUrlShortner.java](../../src/main/java/org/chijai/design/lld/DesignUrlShortner.java) |