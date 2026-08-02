package org.chijai;
import java.util.*;

/**
 * =============================================================
 *                    DSA INTERVIEW CHEATSHEET
 * =============================================================
 *
 * Purpose
 * -------
 * Personal last-minute interview revision.
 *
 * Source
 * ------
 * Reformatted from personal handwritten notes.
 *
 * =============================================================
 */
public class CheatSheet {

/*
 * =============================================================
 * ARRAYS & HASHING
 * =============================================================
 */

/*
 * -------------------------------------------------------------
 * Two Sum
 * -------------------------------------------------------------
 *
 * Pattern
 * Hash Map
 *
 * Notes
 * • use hash map to instantly check for difference value
 * • map stores index of last occurrence
 * • don't use same element twice
 *
 * Complexity
 * Time : O(n)
 * Space: O(n)
 */

/*
 * -------------------------------------------------------------
 * Best Time to Buy and Sell Stock
 * -------------------------------------------------------------
 *
 * Pattern
 * Sliding Window / Two Pointers
 *
 * Notes
 * • find local minimum
 * • search for local maximum
 */

/*
 * -------------------------------------------------------------
 * Contains Duplicate
 * -------------------------------------------------------------
 *
 * Pattern
 * Hash Set
 *
 * Notes
 * • hashset to get unique values
 * • compare set size with array size
 */

/*
 * -------------------------------------------------------------
 * Product of Array Except Self
 * -------------------------------------------------------------
 *
 * Pattern
 * Prefix Product
 * Suffix Product
 *
 * Notes
 * • make two passes
 * • first pass builds left products
 * • second pass builds right products
 */

/*
 * -------------------------------------------------------------
 * Maximum Subarray
 * -------------------------------------------------------------
 *
 * Pattern
 * Dynamic Programming
 * Kadane's Algorithm
 *
 * Notes
 * • previous subarray can't be negative
 * • compute max sum for every prefix
 */

/*
 * -------------------------------------------------------------
 * Maximum Product Subarray
 * -------------------------------------------------------------
 *
 * Pattern
 * Dynamic Programming
 *
 * Notes
 * • maintain current maximum product
 * • maintain current minimum product
 * • negative values can swap max/min
 */

/*
 * -------------------------------------------------------------
 * Find Minimum in Rotated Sorted Array
 * -------------------------------------------------------------
 *
 * Pattern
 * Binary Search
 *
 * Notes
 * • determine which half is sorted
 * • array contains at most two sorted segments
 * • pivot contains minimum
 */

/*
 * -------------------------------------------------------------
 * Search in Rotated Sorted Array
 * -------------------------------------------------------------
 *
 * Pattern
 * Binary Search
 *
 * Notes
 * • one half is always sorted
 * • if target lies inside sorted half, search there
 * • otherwise search the opposite half
 */

/*
 * -------------------------------------------------------------
 * 3Sum
 * -------------------------------------------------------------
 *
 * Pattern
 * Sorting + Two Pointers
 *
 * Notes
 * • sort input
 * • iterate first element
 * • find remaining pair using left/right pointers
 * • skip duplicate first values
 * • skip duplicate second values
 */

/*
 * -------------------------------------------------------------
 * Container With Most Water
 * -------------------------------------------------------------
 *
 * Pattern
 * Two Pointers
 *
 * Notes
 * • start from both ends
 * • move pointer with smaller height
 * • shrinking window
 */

/*
 * -------------------------------------------------------------
 * Sum of Two Integers
 * -------------------------------------------------------------
 *
 * Pattern
 * Bit Manipulation
 *
 * Notes
 * • add bit by bit
 * • remember carry
 * • continue until carry becomes zero
 */

/*
 * -------------------------------------------------------------
 * Number of 1 Bits
 * -------------------------------------------------------------
 *
 * Pattern
 * Bit Manipulation
 *
 * Notes
 * • use bitwise AND
 * • use right shift
 * • avoid modulo/division where possible
 */

/*
 * -------------------------------------------------------------
 * Counting Bits
 * -------------------------------------------------------------
 *
 * Pattern
 * Dynamic Programming
 *
 * Notes
 * • offset = largest power of two
 * • dp[i] = dp[i - offset]
 */

/*
 * -------------------------------------------------------------
 * Missing Number
 * -------------------------------------------------------------
 *
 * Pattern
 * XOR
 * Mathematics
 *
 * Notes
 * • expected sum − actual sum
 * • XOR index with value
 */

/*
 * -------------------------------------------------------------
 * Reverse Bits
 * -------------------------------------------------------------
 *
 * Pattern
 * Bit Manipulation
 *
 * Notes
 * • reverse all 32 bits
 */

/*
 * =============================================================
 * DYNAMIC PROGRAMMING
 * =============================================================
 */

/*
 * -------------------------------------------------------------
 * Climbing Stairs
 * -------------------------------------------------------------
 *
 * Pattern
 * Dynamic Programming
 *
 * Notes
 * • answer = f(n-1) + f(n-2)
 */

/*
 * -------------------------------------------------------------
 * Coin Change
 * -------------------------------------------------------------
 *
 * Pattern
 * Dynamic Programming
 *
 * Notes
 * • recursive DFS
 * • memoization
 * • bottom-up DP
 * • compute amount from 1 to target
 */

/*
 * -------------------------------------------------------------
 * Longest Increasing Subsequence
 * -------------------------------------------------------------
 *
 * Pattern
 * Dynamic Programming
 *
 * Notes
 * • recursive include/exclude
 * • memoization
 * • dp ending at each index
 */

/*
 * -------------------------------------------------------------
 * Longest Common Subsequence
 * -------------------------------------------------------------
 *
 * Pattern
 * Dynamic Programming
 *
 * Notes
 * • recursive comparison
 * • if chars match, move both pointers
 * • otherwise take max of skipping either string
 * • memoization
 * • iterative 2D DP
 */

/*
 * -------------------------------------------------------------
 * Word Break
 * -------------------------------------------------------------
 *
 * Pattern
 * Dynamic Programming
 *
 * Notes
 * • every prefix is a possible decision
 * • if prefix exists in dictionary
 * • recursively solve remaining suffix
 * • cache results
 */

/*
 * -------------------------------------------------------------
 * Combination Sum
 * -------------------------------------------------------------
 *
 * Pattern
 * Backtracking
 *
 * Notes
 * • visualize decision tree
 * • stop when sum == target
 * • stop when sum > target
 * • recurse on current candidate
 * • recurse on candidates to the right
 */

/*
 * -------------------------------------------------------------
 * House Robber
 * -------------------------------------------------------------
 *
 * Pattern
 * Dynamic Programming
 *
 * Notes
 * • rob current
 * • skip current
 * • store previous best values
 */

/*
 * -------------------------------------------------------------
 * House Robber II
 * -------------------------------------------------------------
 *
 * Pattern
 * Dynamic Programming
 *
 * Notes
 * • solve excluding first house
 * • solve excluding last house
 * • take maximum
 */

/*
 * -------------------------------------------------------------
 * Decode Ways
 * -------------------------------------------------------------
 *
 * Pattern
 * Dynamic Programming
 *
 * Notes
 * • one-digit decision
 * • two-digit decision
 * • memoization
 * • bottom-up DP
 * • remember edge cases
 *   10
 *   20
 *   26
 *   29
 *   31
 *   52
 */

/*
 * -------------------------------------------------------------
 * Unique Paths
 * -------------------------------------------------------------
 *
 * Pattern
 * Grid DP
 *
 * Notes
 * • work backwards
 * • paths = down + right
 * • optimize to previous row only
 */

/*
 * -------------------------------------------------------------
 * Jump Game
 * -------------------------------------------------------------
 *
 * Pattern
 * Greedy
 *
 * Notes
 * • visualize recursion tree
 * • iterative O(1) solution
 * • move goal backwards
 * • if current reaches goal
 *     current becomes new goal
 */

/*
 * =============================================================
 * GRAPHS
 * =============================================================
 */

/*
 * -------------------------------------------------------------
 * Clone Graph
 * -------------------------------------------------------------
 *
 * Pattern
 * DFS
 *
 * Notes
 * • recursive DFS
 * • hashmap old -> new node
 */

/*
 * -------------------------------------------------------------
 * Course Schedule
 * -------------------------------------------------------------
 *
 * Pattern
 * Graph
 * Cycle Detection
 *
 * Notes
 * • adjacency list
 * • DFS
 * • three states
 *     - unvisited
 *     - visiting
 *     - visited
 * • visiting node twice means cycle
 */

/*
 * -------------------------------------------------------------
 * Pacific Atlantic Water Flow
 * -------------------------------------------------------------
 *
 * Pattern
 * DFS
 *
 * Notes
 * • DFS from Pacific
 * • DFS from Atlantic
 * • answer is intersection
 */

/*
 * -------------------------------------------------------------
 * Number of Islands
 * -------------------------------------------------------------
 *
 * Pattern
 * Flood Fill
 *
 * Notes
 * • iterate entire grid
 * • DFS every unvisited land
 * • mark visited
 * • increment island count
 */

/*
 * -------------------------------------------------------------
 * Longest Consecutive Sequence
 * -------------------------------------------------------------
 *
 * Pattern
 * Hash Set
 *
 * Notes
 * • add every number into set
 * • only begin sequence if num-1 absent
 * • count forward
 * • union-find also possible
 */

/*
 * -------------------------------------------------------------
 * Alien Dictionary
 * -------------------------------------------------------------
 *
 * Pattern
 * Topological Sort
 *
 * Notes
 * • compare adjacent words
 * • first differing character creates edge
 * • build graph
 * • topological sort
 * • detect cycles
 */

/*
 * -------------------------------------------------------------
 * Graph Valid Tree
 * -------------------------------------------------------------
 *
 * Pattern
 * Union Find
 * DFS
 *
 * Notes
 * • union returning false means cycle
 * • final component count must equal one
 * • DFS solution removes parent edge
 */

/*
 * -------------------------------------------------------------
 * Number of Connected Components
 * -------------------------------------------------------------
 *
 * Pattern
 * DFS
 * BFS
 * Union Find
 *
 * Notes
 * • DFS every unvisited node
 * • increment component count
 */

/*
 * =============================================================
 * INTERVALS
 * =============================================================
 */

/*
 * -------------------------------------------------------------
 * Insert Interval
 * -------------------------------------------------------------
 *
 * Pattern
 * Merge Intervals
 *
 * Notes
 * • insert in order
 * • merge overlaps
 * • append remaining intervals
 */

/*
 * -------------------------------------------------------------
 * Merge Intervals
 * -------------------------------------------------------------
 *
 * Pattern
 * Sorting
 *
 * Notes
 * • sort intervals
 * • overlapping intervals become adjacent
 * • merge while traversing
 */

/*
 * -------------------------------------------------------------
 * Non-overlapping Intervals
 * -------------------------------------------------------------
 *
 * Pattern
 * Greedy
 * DP
 *
 * Notes
 * • maximize intervals kept
 * • equivalent to minimizing removals
 */

/*
 * -------------------------------------------------------------
 * Meeting Rooms
 * -------------------------------------------------------------
 *
 * Pattern
 * Sorting
 *
 * Notes
 * • sort by start time
 * • compare consecutive meetings
 */

/*
 * -------------------------------------------------------------
 * Meeting Rooms II
 * -------------------------------------------------------------
 *
 * Pattern
 * Min Heap
 *
 * Notes
 * • separate start/end times
 * • count active meetings
 * • alternatively use min heap
 */

/*
 * =============================================================
 * LINKED LIST
 * =============================================================
 */

/*
 * -------------------------------------------------------------
 * Reverse Linked List
 * -------------------------------------------------------------
 *
 * Pattern
 * Pointer Manipulation
 *
 * Notes
 * • iterate maintaining prev and current
 * • recursively reverse also possible
 * • return new head
 */

/*
 * -------------------------------------------------------------
 * Detect Cycle in a Linked List
 * -------------------------------------------------------------
 *
 * Pattern
 * Fast & Slow Pointer
 *
 * Notes
 * • hashset solution
 * • Floyd Cycle Detection
 * • if slow meets fast, cycle exists
 */

/*
 * -------------------------------------------------------------
 * Merge Two Sorted Lists
 * -------------------------------------------------------------
 *
 * Pattern
 * Merge
 *
 * Notes
 * • merge in sorted order
 * • dummy node simplifies implementation
 */

/*
 * -------------------------------------------------------------
 * Merge K Sorted Lists
 * -------------------------------------------------------------
 *
 * Pattern
 * Divide & Conquer
 * Min Heap
 *
 * Notes
 * • repeatedly merge pairs
 * • O(N log K)
 * • heap solution always extracts minimum frontier
 */

/*
 * -------------------------------------------------------------
 * Remove Nth Node From End Of List
 * -------------------------------------------------------------
 *
 * Pattern
 * Two Pointers
 *
 * Notes
 * • dummy node
 * • fast pointer starts n ahead
 * • move together
 */

/*
 * -------------------------------------------------------------
 * Reorder List
 * -------------------------------------------------------------
 *
 * Pattern
 * Fast/Slow + Reverse
 *
 * Notes
 * • find middle
 * • reverse second half
 * • merge alternating nodes
 */

/*
 * =============================================================
 * MATRIX
 * =============================================================
 */

/*
 * -------------------------------------------------------------
 * Set Matrix Zeroes
 * -------------------------------------------------------------
 *
 * Pattern
 * Matrix
 *
 * Notes
 * • hashset solution
 * • optimal uses first row/column as markers
 */

/*
 * -------------------------------------------------------------
 * Spiral Matrix
 * -------------------------------------------------------------
 *
 * Pattern
 * Boundary Traversal
 *
 * Notes
 * • top
 * • bottom
 * • left
 * • right
 * • shrink layer by layer
 */

/*
 * -------------------------------------------------------------
 * Rotate Image
 * -------------------------------------------------------------
 *
 * Pattern
 * Matrix
 *
 * Notes
 * • rotate layer by layer
 * • swap four positions
 * • use temporary variable
 */

/*
 * -------------------------------------------------------------
 * Word Search
 * -------------------------------------------------------------
 *
 * Pattern
 * DFS + Backtracking
 *
 * Notes
 * • DFS from every cell
 * • visited set
 * • remove from visited while backtracking
 */

/*
 * =============================================================
 * STRINGS
 * =============================================================
 */

/*
 * -------------------------------------------------------------
 * Longest Substring Without Repeating Characters
 * -------------------------------------------------------------
 *
 * Pattern
 * Sliding Window
 *
 * Notes
 * • expand right
 * • duplicate found
 * • shrink left until unique
 */

/*
 * -------------------------------------------------------------
 * Longest Repeating Character Replacement
 * -------------------------------------------------------------
 *
 * Pattern
 * Sliding Window
 *
 * Notes
 * • characters limited to A-Z
 * • maintain maximum frequency
 * • expand while valid
 * • shrink when invalid
 */

/*
 * -------------------------------------------------------------
 * Minimum Window Substring
 * -------------------------------------------------------------
 *
 * Pattern
 * Sliding Window
 *
 * Notes
 * • Need
 * • Have
 * • expand until valid
 * • shrink while valid
 */

/*
 * -------------------------------------------------------------
 * Valid Anagram
 * -------------------------------------------------------------
 *
 * Pattern
 * Hash Map
 *
 * Notes
 * • count characters
 * • decrement counts
 * • verify all zero
 */

/*
 * -------------------------------------------------------------
 * Group Anagrams
 * -------------------------------------------------------------
 *
 * Pattern
 * Hash Map
 *
 * Notes
 * • 26-character frequency array
 * • frequency tuple becomes key
 */

/*
 * -------------------------------------------------------------
 * Valid Parentheses
 * -------------------------------------------------------------
 *
 * Pattern
 * Stack
 *
 * Notes
 * • push opening brackets
 * • matching closing pops stack
 * • stack empty at end
 */

/*
 * -------------------------------------------------------------
 * Valid Palindrome
 * -------------------------------------------------------------
 *
 * Pattern
 * Two Pointers
 *
 * Notes
 * • ignore non-alphanumeric
 * • compare after normalization
 */

/*
 * -------------------------------------------------------------
 * Longest Palindromic Substring
 * -------------------------------------------------------------
 *
 * Pattern
 * Expand Around Center
 *
 * Notes
 * • odd centers
 * • even centers
 */

/*
 * -------------------------------------------------------------
 * Palindromic Substrings
 * -------------------------------------------------------------
 *
 * Pattern
 * Expand Around Center
 *
 * Notes
 * • identical expansion
 * • count every palindrome
 * • Manacher's algorithm exists
 */

/*
 * -------------------------------------------------------------
 * Encode and Decode Strings
 * -------------------------------------------------------------
 *
 * Pattern
 * String Encoding
 *
 * Notes
 * • store length
 * • delimiter '#'
 * • parse sequentially
 */

    /*
     * =============================================================
     * TREES
     * =============================================================
     */

    /*
     * -------------------------------------------------------------
     * Maximum Depth of Binary Tree
     * -------------------------------------------------------------
     *
     * Pattern
     * DFS / BFS
     *
     * Notes
     * • recursive DFS
     * • iterative BFS counts levels
     */

    /*
     * -------------------------------------------------------------
     * Same Tree
     * -------------------------------------------------------------
     *
     * Pattern
     * DFS
     *
     * Notes
     * • traverse both trees simultaneously
     * • recursive or iterative BFS
     */

    /*
     * -------------------------------------------------------------
     * Invert Binary Tree
     * -------------------------------------------------------------
     *
     * Pattern
     * DFS
     *
     * Notes
     * • swap left and right
     * • recursive
     * • iterative DFS
     * • BFS also works
     */

    /*
     * -------------------------------------------------------------
     * Binary Tree Maximum Path Sum
     * -------------------------------------------------------------
     *
     * Pattern
     * Tree DP
     *
     * Notes
     * • helper returns max path without split
     * • update global answer using split path
     */

    /*
     * -------------------------------------------------------------
     * Binary Tree Level Order Traversal
     * -------------------------------------------------------------
     *
     * Pattern
     * BFS
     *
     * Notes
     * • queue
     * • process level by level
     */

    /*
     * -------------------------------------------------------------
     * Serialize and Deserialize Binary Tree
     * -------------------------------------------------------------
     *
     * Pattern
     * BFS
     *
     * Notes
     * • include null nodes
     * • deserialize using queue
     */

    /*
     * -------------------------------------------------------------
     * Subtree of Another Tree
     * -------------------------------------------------------------
     *
     * Pattern
     * DFS
     *
     * Notes
     * • compare every subtree
     * • helper checks tree equality
     * • Merkle hashing possible
     */

    /*
     * -------------------------------------------------------------
     * Construct Binary Tree from Preorder and Inorder
     * -------------------------------------------------------------
     *
     * Pattern
     * Divide & Conquer
     *
     * Notes
     * • preorder first value = root
     * • inorder splits left/right subtree
     * • recurse
     */

    /*
     * -------------------------------------------------------------
     * Validate Binary Search Tree
     * -------------------------------------------------------------
     *
     * Pattern
     * DFS
     *
     * Notes
     * • maintain min/max bounds
     * • inorder traversal must be increasing
     */

    /*
     * -------------------------------------------------------------
     * Kth Smallest Element in BST
     * -------------------------------------------------------------
     *
     * Pattern
     * Inorder Traversal
     *
     * Notes
     * • inorder gives sorted order
     * • iterative stack solution
     */

    /*
     * -------------------------------------------------------------
     * Lowest Common Ancestor of BST
     * -------------------------------------------------------------
     *
     * Pattern
     * BST
     *
     * Notes
     * • both left -> go left
     * • both right -> go right
     * • split point is answer
     */

    /*
     * =============================================================
     * TRIE
     * =============================================================
     */

    /*
     * -------------------------------------------------------------
     * Implement Trie
     * -------------------------------------------------------------
     *
     * Pattern
     * Trie
     *
     * Notes
     * • node stores children
     * • end-of-word flag
     * • root has no character
     */

    /*
     * -------------------------------------------------------------
     * Add and Search Word
     * -------------------------------------------------------------
     *
     * Pattern
     * Trie + DFS
     *
     * Notes
     * • '.' matches every child
     * • recursively explore all branches
     */

    /*
     * -------------------------------------------------------------
     * Word Search II
     * -------------------------------------------------------------
     *
     * Pattern
     * Trie + DFS
     *
     * Notes
     * • build trie from dictionary
     * • DFS from every board cell
     * • avoid duplicate answers
     * • backtracking
     */

    /*
     * =============================================================
     * HEAPS
     * =============================================================
     */

    /*
     * -------------------------------------------------------------
     * Top K Frequent Elements
     * -------------------------------------------------------------
     *
     * Pattern
     * Heap
     *
     * Notes
     * • frequency map
     * • min heap of size k
     */

    /*
     * -------------------------------------------------------------
     * Find Median From Data Stream
     * -------------------------------------------------------------
     *
     * Pattern
     * Two Heaps
     *
     * Notes
     * • max heap for smaller half
     * • min heap for larger half
     * • rebalance after insertion
     */

    /*
     * =============================================================
     * GENERAL PATTERN REMINDERS
     * =============================================================
     *
     * Arrays
     * • Try left/right pointers
     * • Try sliding window
     * • Try sorting
     *
     * Strings
     * • Sliding Window
     * • Frequency Counter
     * • Two Pointers
     *
     * Trees
     * • DFS
     * • BFS
     * • Divide & Conquer
     *
     * Graphs
     * • DFS
     * • BFS
     * • Union Find
     * • Topological Sort
     *
     * Dynamic Programming
     * • Top Down (Memoization)
     * • Bottom Up (Tabulation)
     *
     * Common Tools
     * • HashMap
     * • HashSet
     * • Stack
     * • Queue
     * • Deque
     * • PriorityQueue
     * • Arrays.sort()
     * • Collections.sort()
     */

}

/*
 * =============================================================
 * PROBLEM CATEGORIES
 * =============================================================
 *
 * Arrays & Hashing
 * • Arrays
 * • Two Pointers
 * • Sliding Window
 * • Prefix/Suffix
 *
 * Strings
 * • HashMap
 * • Sliding Window
 * • Expand Around Center
 *
 * Linked List
 * • Fast & Slow
 * • Reverse
 * • Merge
 *
 * Trees
 * • DFS
 * • BFS
 * • Divide & Conquer
 *
 * Graphs
 * • DFS
 * • BFS
 * • Union Find
 * • Topological Sort
 *
 * Dynamic Programming
 * • Memoization
 * • Tabulation
 *
 * Backtracking
 * • Decision Tree
 *
 * Intervals
 * • Sort + Merge
 *
 * Heap
 * • Min Heap
 * • Max Heap
 *
 * Bit Manipulation
 * • XOR
 * • Shift
 * • Masking
 */


/*
 * =============================================================
 * GENERAL INTERVIEW REMINDERS
 * =============================================================
 *
 * Arrays
 * • Start from both ends
 * • Consider sorting
 * • Consider prefix/suffix
 *
 * Strings
 * • Frequency Counter
 * • Sliding Window
 * • Two Pointers
 *
 * Trees
 * • Think recursively first
 * • Base case before recursion
 * • Return information upward
 *
 * Graphs
 * • Connected?
 * • Directed?
 * • Need cycle detection?
 * • Need topological order?
 *
 * Dynamic Programming
 * • Define state
 * • Define transition
 * • Base cases
 * • Memoize
 *
 * Binary Search
 * • Search space
 * • Mid
 * • Which half can be discarded?
 *
 * Backtracking
 * • Choose
 * • Explore
 * • Undo
 */


/*
 * =============================================================
 * TOOLBOX
 * =============================================================
 *
 * HashMap
 * HashSet
 * Counter
 * Array
 * List
 * Stack
 * Queue
 * Deque
 * PriorityQueue
 * TreeMap
 * TreeSet
 * Binary Search
 * Sorting
 * DFS
 * BFS
 * Union Find
 * Trie
 * Prefix Sum
 * Monotonic Stack
 * Sliding Window
 * Bit Manipulation
 * Greedy
 * Dynamic Programming
 */


/*
 * =============================================================
 * PERSONAL INTERVIEW CHECKLIST
 * =============================================================
 *
 * □ Read constraints
 * □ Find brute force
 * □ Identify pattern
 * □ State time complexity
 * □ State space complexity
 * □ Think about edge cases
 * □ Empty input
 * □ Single element
 * □ Duplicates
 * □ Negative values
 * □ Overflow
 * □ Dry run
 * □ Code
 * □ Dry run again
 * □ Explain complexity
 *
 * Interview > Perfect Code
 */