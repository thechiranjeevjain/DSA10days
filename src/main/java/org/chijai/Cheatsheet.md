# DSA Interview Cheatsheet

> Reformatted from personal notes for last-minute interview revision.

------------------------------------------------------------------------

# Arrays & Hashing

## Two Sum

-   **Pattern:** Hash Map
-   use hash map to instantly check for difference value
-   map stores index of last occurrence
-   don't use same element twice

## Best Time to Buy and Sell Stock

-   **Pattern:** Sliding Window
-   find local minimum
-   search for local maximum

## Contains Duplicate

-   **Pattern:** Hash Set
-   hashset to get unique values
-   compare set size with array size

## Product of Array Except Self

-   **Pattern:** Prefix + Suffix
-   first pass builds left products
-   second pass builds right products

## Maximum Subarray

-   **Pattern:** Kadane / DP
-   previous subarray can't be negative

## Maximum Product Subarray

-   **Pattern:** DP
-   maintain current maximum and minimum product

## Find Minimum in Rotated Sorted Array

-   **Pattern:** Binary Search
-   determine sorted half
-   pivot is minimum

## Search in Rotated Sorted Array

-   **Pattern:** Binary Search
-   one half always sorted

## 3Sum

-   **Pattern:** Sort + Two Pointers
-   skip duplicates
-   search remaining pair

## Container With Most Water

-   **Pattern:** Two Pointers
-   move shorter wall

------------------------------------------------------------------------

# Dynamic Programming

-   Climbing Stairs -> f(n)=f(n-1)+f(n-2)
-   Coin Change -> Memoization / Bottom-up
-   LIS -> DP ending at each index
-   LCS -> 2D DP
-   Word Break -> Prefix + Memoization
-   Combination Sum -> Backtracking
-   House Robber -> Take / Skip
-   House Robber II -> Exclude first / Exclude last
-   Decode Ways -> One-digit vs Two-digit transitions
-   Unique Paths -> Grid DP
-   Jump Game -> Greedy (move goal backwards)

------------------------------------------------------------------------

# Graphs

-   Clone Graph -> DFS + HashMap
-   Course Schedule -> DFS Cycle Detection
-   Pacific Atlantic -> Reverse DFS
-   Number of Islands -> Flood Fill
-   Longest Consecutive -> HashSet
-   Alien Dictionary -> Topological Sort
-   Graph Valid Tree -> Union Find / DFS
-   Connected Components -> DFS

------------------------------------------------------------------------

# Linked List

-   Reverse List -> Reverse pointers
-   Cycle Detection -> Fast & Slow
-   Merge Two Lists -> Dummy Node
-   Merge K Lists -> Heap / Divide & Conquer
-   Remove Nth -> Two Pointers
-   Reorder List -> Middle + Reverse + Merge

------------------------------------------------------------------------

# Matrix

-   Set Matrix Zeroes -> First row/column markers
-   Spiral Matrix -> Boundary traversal
-   Rotate Image -> Layer rotation
-   Word Search -> DFS + Backtracking

------------------------------------------------------------------------

# Strings

-   Longest Substring -> Sliding Window
-   Character Replacement -> Sliding Window
-   Minimum Window -> Need / Have
-   Valid Anagram -> Count
-   Group Anagrams -> Frequency key
-   Valid Parentheses -> Stack
-   Valid Palindrome -> Two Pointers
-   Longest Palindrome -> Expand Around Center
-   Palindromic Substrings -> Expand Around Center
-   Encode / Decode Strings -> Length + Delimiter

------------------------------------------------------------------------

# Trees

-   Max Depth -> DFS / BFS
-   Same Tree -> DFS
-   Invert Tree -> DFS
-   Max Path Sum -> Tree DP
-   Level Order -> BFS
-   Serialize Tree -> BFS
-   Subtree -> DFS Compare
-   Build Tree -> Preorder + Inorder
-   Validate BST -> Min / Max Range
-   Kth Smallest -> Inorder
-   LCA BST -> BST Property

------------------------------------------------------------------------

# Trie

-   Implement Trie
-   Add & Search Word
-   Word Search II -> Trie + DFS

------------------------------------------------------------------------

# Heap

-   Top K Frequent -> Heap
-   Median Stream -> Two Heaps

------------------------------------------------------------------------

# Toolbox

HashMap / HashSet / Stack / Queue / Deque / PriorityQueue / DFS / BFS /
Union Find / Trie / Binary Search / Sliding Window / Prefix Sum / DP /
Greedy / Backtracking / Bit Manipulation

------------------------------------------------------------------------

# Interview Checklist

-   Read constraints
-   Find brute force
-   Identify pattern
-   Consider edge cases
-   Dry run
-   Code
-   Dry run again
-   Explain complexity
