# Crisp Interview Answers

Practice speaking these in the interview rhythm.

~~~text
brute force -> bottleneck -> pattern -> invariant -> code -> dry run
~~~

## 1. Ransom Note

- Links: [Java](../../src/main/java/org/chijai/day1/session1/RansomNote.java) | [LeetCode](https://leetcode.com/problems/ransom-note/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: HashMap/HashSet, using HashMap/frequency.
- Invariant/state: Count magazine chars, then spend counts for ransom; fail when a needed char is missing.
- Code idea: Build int[26] or map from magazine, decrement while scanning ransomNote, return false below zero.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 2. Majority Element

- Links: [Java](../../src/main/java/org/chijai/day1/session2/MajorityElement.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: HashMap/HashSet, using Boyer-Moore / frequency.
- Invariant/state: Boyer-Moore cancels different values; surviving candidate is majority after optional verification.
- Code idea: Track candidate and count; reset at zero, increment on match, decrement otherwise.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 3. Valid Anagram

- Links: [Java](../../src/main/java/org/chijai/day3/session3/ValidAnagram.java) | [LeetCode](https://leetcode.com/problems/valid-anagram/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: HashMap/HashSet, using Frequency count.
- Invariant/state: Two strings are anagrams when every character count nets to zero.
- Code idea: Reject different lengths, increment for s and decrement for t, then verify all counts zero.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 4. 2Sum / 3Sum / 4Sum

- Links: [Java](../../src/main/java/org/chijai/day1/session2/Three3Sum2Sum.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Two Pointers, using Two pointers / hash.
- Invariant/state: For sum families: hash for 2Sum, sort/fix one value, then two-pointer the remaining sum.
- Code idea: Sort when indices are not required, loop fixed values, move left/right by sum comparison, skip duplicates.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 5. Valid Palindrome

- Links: [Java](../../src/main/java/org/chijai/day3/session3/ValidPalindrome.java) | [LeetCode](https://leetcode.com/problems/valid-palindrome/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Two Pointers, using Two pointers.
- Invariant/state: Skip non-alphanumeric chars and compare normalized ends while pointers move inward.
- Code idea: Advance left/right past invalid chars, compare lowercase chars, stop when pointers cross.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 6. Container With Most Water

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/ContainerWithMostWater.java) | [LeetCode](https://leetcode.com/problems/container-with-most-water/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Two Pointers, using Two pointers.
- Invariant/state: Area is limited by shorter wall, so move the shorter side inward.
- Code idea: Compute area at left/right, update max, move pointer with smaller height.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 7. Trapping Rain Water

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/TrappingRainwater.java) | [LeetCode](https://leetcode.com/problems/trapping-rain-water/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Two Pointers, using Two pointers / stack.
- Invariant/state: Water at a side depends on the smaller max boundary seen so far.
- Code idea: Move the side with lower height, update max, add max-height when bounded.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 8. Longest Substring With At Most K Distinct Characters

- Links: [Java](../../src/main/java/org/chijai/day3/session1/AtMostKDistinct.java) | [LeetCode](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Sliding Window, using Sliding window.
- Invariant/state: Keep a frequency map with at most k distinct chars; shrink until valid.
- Code idea: Expand right count, while distinct > k decrement/remove left, update max length.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 9. Longest Substring Without Repeating Characters

- Links: [Java](../../src/main/java/org/chijai/day3/session1/LongestSubString.java) | [LeetCode](https://leetcode.com/problems/longest-substring-without-repeating-characters/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Sliding Window, using Sliding window / set.
- Invariant/state: Window must contain unique chars; move left past duplicates.
- Code idea: Expand right, while duplicate exists remove left, then update max.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 10. Longest Repeating Character Replacement

- Links: [Java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LeetCode](https://leetcode.com/problems/longest-repeating-character-replacement/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Sliding Window, using Sliding window / need-have.
- Invariant/state: Window is valid when size - maxFreq <= k replacements.
- Code idea: Track counts and maxFreq, shrink when windowLen - maxFreq > k, update best.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 11. Minimum Size Subarray Sum

- Links: [Java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LeetCode](https://leetcode.com/problems/minimum-size-subarray-sum/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Sliding Window, using Sliding window / need-have.
- Invariant/state: For positive numbers, expand until sum >= target, then shrink to minimize length.
- Code idea: Add right to sum, while sum >= target update min and subtract left.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 12. Minimum Window Substring

- Links: [Java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LeetCode](https://leetcode.com/problems/minimum-window-substring/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Sliding Window, using Sliding window / need-have.
- Invariant/state: Expand until all needed chars are covered, then shrink while still valid.
- Code idea: Build need map, update have on right, while have == needCount update best and remove left.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 13. Permutation In String

- Links: [Java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LeetCode](https://leetcode.com/problems/permutation-in-string/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Sliding Window, using Sliding window / need-have.
- Invariant/state: A fixed-size window is a permutation when its frequency counts match the target.
- Code idea: Track counts/matches for window length s1, slide one char in and one char out.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 14. Substring With Concatenation Of All Words

- Links: [Java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LeetCode](https://leetcode.com/problems/substring-with-concatenation-of-all-words/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Sliding Window, using Sliding window / need-have.
- Invariant/state: Scan word-sized windows by offset and keep word counts bounded by need.
- Code idea: For each offset, move in wordLen steps, count words, shrink when a word is overused.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 15. Count Number Of Nice Subarrays

- Links: [Java](../../src/main/java/org/chijai/day3/session2/NiceSubArrays.java) | [LeetCode](https://leetcode.com/problems/count-number-of-nice-subarrays/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Sliding Window, using Prefix/window counting.
- Invariant/state: Exactly k odds equals atMost(k) minus atMost(k-1), or prefix count of odd count.
- Code idea: Count subarrays with at most k odd numbers using a sliding left pointer, subtract atMost(k-1).
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 16. Find All Anagrams In A String

- Links: [Java](../../src/main/java/org/chijai/day3/session3/FindAllAnagramsInAString.java) | [LeetCode](https://leetcode.com/problems/find-all-anagrams-in-a-string/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Sliding Window, using Sliding window frequency.
- Invariant/state: Slide a fixed-size frequency window and record starts where counts match p.
- Code idea: Maintain difference counts or match count across a window of length p.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 17. Binary Subarrays With Sum

- Links: [Java](../../src/main/java/org/chijai/day3/session2/NiceSubArrays.java) | [LeetCode](https://leetcode.com/problems/binary-subarrays-with-sum/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Prefix/Suffix, using Prefix/window counting.
- Invariant/state: For binary arrays, exact goal count can be atMost(goal) - atMost(goal-1).
- Code idea: Implement atMost(sum): expand right, shrink while sum > goal, add window length.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 18. Product Of Array Except Self

- Links: [Java](../../src/main/java/org/chijai/day3/session2/ProductOfArrayExceptSelf.java) | [LeetCode](https://leetcode.com/problems/product-of-array-except-self/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Prefix/Suffix, using Prefix/suffix.
- Invariant/state: Answer is product of everything left times everything right, no division needed.
- Code idea: Fill answer with left products, then multiply by running right product from the end.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 19. Intersection Of Two Linked Lists

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session1/Intersection.java) | [LeetCode](https://leetcode.com/problems/intersection-of-two-linked-lists/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using Linked list two pointers.
- Invariant/state: Switch heads at null; equal path lengths make pointers meet at intersection or null.
- Code idea: Move a and b one step; when null redirect to other head; return when a == b.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 20. Linked List Cycle

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session1/LinkedListCycle.java) | [LeetCode](https://leetcode.com/problems/linked-list-cycle/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using Fast/slow pointers.
- Invariant/state: Slow and fast meet only if a cycle exists.
- Code idea: Move slow one, fast two while fast and fast.next exist; meeting means cycle.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 21. Reverse Linked List

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session1/ReverseLinkedList.java) | [LeetCode](https://leetcode.com/problems/reverse-linked-list/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using Pointer reversal.
- Invariant/state: Reverse one edge at a time after saving next.
- Code idea: Keep prev, curr, next; curr.next = prev; advance; return prev.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 22. Copy List With Random Pointer

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/CopyListWithRandomPointer.java) | [LeetCode](https://leetcode.com/problems/copy-list-with-random-pointer/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using HashMap / interleaving copy.
- Invariant/state: Clone nodes then connect next/random using old-to-new mapping or interleaving.
- Code idea: First create clones in map, second assign clone.next and clone.random from mapped nodes.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 23. Odd Even Linked List

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) | [LeetCode](https://leetcode.com/problems/odd-even-linked-list/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using Linked-list reversal groups.
- Invariant/state: Keep odd and even chains separately, then attach even head after odd tail.
- Code idea: Move odd to even.next and even to odd.next until even chain ends, then connect.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 24. Reverse Linked List Ii

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) | [LeetCode](https://leetcode.com/problems/reverse-linked-list-ii/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using Linked-list reversal groups.
- Invariant/state: Use a dummy and reverse exactly the sublist between left and right.
- Code idea: Find node before left, then head-insert nodes from the sublist for right-left steps.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 25. Reverse Nodes In K Group

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) | [LeetCode](https://leetcode.com/problems/reverse-nodes-in-k-group/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using Linked-list reversal groups.
- Invariant/state: Only reverse a group after confirming k nodes exist.
- Code idea: Use dummy/groupPrev, locate kth, reverse group, reconnect, advance groupPrev.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 26. Rotate List

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) | [LeetCode](https://leetcode.com/problems/rotate-list/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using Linked-list reversal groups.
- Invariant/state: Make the list circular, then break at length - k % length.
- Code idea: Count length and tail, connect tail to head, move to new tail, break circle.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 27. Swap Nodes In Pairs

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) | [LeetCode](https://leetcode.com/problems/swap-nodes-in-pairs/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using Linked-list reversal groups.
- Invariant/state: Dummy node lets you swap each adjacent pair without special-casing head.
- Code idea: For each pair, rewire prev->second, first->second.next, second->first.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 28. Design Browser History

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) | [LeetCode](https://leetcode.com/problems/design-browser-history/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using HashMap + doubly linked list.
- Invariant/state: Back/forward are pointer moves over a history chain; visit drops forward history.
- Code idea: Maintain current node; visit creates current.next and clears forward branch.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 29. First Unique Number

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) | [LeetCode](https://leetcode.com/problems/first-unique-number/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using HashMap + doubly linked list.
- Invariant/state: Queue/list stores arrival order; counts decide whether the front is still unique.
- Code idea: On add update count and queue/list, while front count > 1 pop it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 30. LRU Cache

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) | [LeetCode](https://leetcode.com/problems/lru-cache/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using HashMap + doubly linked list.
- Invariant/state: HashMap gives O(1) lookup; doubly linked list keeps recency order.
- Code idea: On get/put move node to front; if over capacity remove tail and map entry.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 31. Moving Average From Data Stream

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) | [LeetCode](https://leetcode.com/problems/moving-average-from-data-stream/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using HashMap + doubly linked list.
- Invariant/state: Queue last size values and running sum; average is sum divided by queue size.
- Code idea: Offer val, add to sum, if queue too large poll and subtract, return sum/count.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 32. Linked List Cycle Ii

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session4/LinkedListCycleII.java) | [LeetCode](https://leetcode.com/problems/linked-list-cycle-ii/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using Floyd cycle entry.
- Invariant/state: After slow/fast meet, move one pointer from head and both one step to find entry.
- Code idea: Detect meeting, reset one pointer to head, move both until equal.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 33. Merge Two Sorted Lists

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session4/Merge2SortedLists.java) | [LeetCode](https://leetcode.com/problems/merge-two-sorted-lists/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using Merge / dummy node.
- Invariant/state: Dummy tail repeatedly takes the smaller current node.
- Code idea: Compare l1/l2, append smaller to tail, advance, then attach remainder.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 34. Merge K Sorted Lists

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session4/MergeKSortedLists.java) | [LeetCode](https://leetcode.com/problems/merge-k-sorted-lists/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using Heap / divide and conquer.
- Invariant/state: A min-heap stores the current smallest head among k lists.
- Code idea: Push non-null heads, poll min, append it, push its next.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 35. Middle Of The Linked List

- Links: [Java](../../src/main/java/org/chijai/day4/LinkedList/session4/MiddleOfLinkedList.java) | [LeetCode](https://leetcode.com/problems/middle-of-the-linked-list/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Linked List, using Fast/slow pointers.
- Invariant/state: Fast moves twice as fast; slow lands at the middle when fast finishes.
- Code idea: While fast and fast.next exist, move slow one and fast two, return slow.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 36. Binary Tree Right Side View

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeSideView.java) | [LeetCode](https://leetcode.com/problems/binary-tree-right-side-view/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree BFS, using Tree BFS / DFS.
- Invariant/state: The last node seen at each BFS level is visible from the right.
- Code idea: For each level size, process nodes and record value when i == size - 1.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 37. Binary Tree Level Order Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeTraversal.java) | [LeetCode](https://leetcode.com/problems/binary-tree-level-order-traversal/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree BFS, using Tree traversal.
- Invariant/state: Capture queue size to process exactly one tree level at a time.
- Code idea: For each level, poll size nodes, collect values, enqueue children.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 38. Binary Tree Inorder Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) | [LeetCode](https://leetcode.com/problems/binary-tree-inorder-traversal/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree DFS / stack.
- Invariant/state: Inorder is left, node, right; for BST it yields sorted order.
- Code idea: Push left chain, pop node, visit, then go right.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 39. Binary Tree Postorder Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) | [LeetCode](https://leetcode.com/problems/binary-tree-postorder-traversal/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree DFS / stack.
- Invariant/state: Postorder visits children before the node, useful when parent depends on subtree results.
- Code idea: Use recursion or stack with last-visited tracking; visit after left and right.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 40. Binary Tree Preorder Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) | [LeetCode](https://leetcode.com/problems/binary-tree-preorder-traversal/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree DFS / stack.
- Invariant/state: Preorder visits node before children, useful for serialization and copying structure.
- Code idea: Visit node, then left, then right; iterative stack pushes right before left.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 41. Validate Binary Search Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) | [LeetCode](https://leetcode.com/problems/validate-binary-search-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree DFS / stack.
- Invariant/state: Every node must stay inside strict min/max bounds inherited from ancestors.
- Code idea: DFS with low/high bounds, reject value <= low or >= high, recurse tightened bounds.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 42. Lowest Common Ancestor Of A Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) | [LeetCode](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree DFS return contract.
- Invariant/state: If left and right both return a target, current node is the split point.
- Code idea: Return node if null/p/q; ask left/right; if both non-null return root else non-null side.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 43. Lowest Common Ancestor Of A Binary Tree Ii

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) | [LeetCode](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-ii/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree DFS return contract.
- Invariant/state: Same split-point idea, but verify both targets actually exist.
- Code idea: DFS returns found node/count flags; only accept LCA when both p and q are found.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 44. Lowest Common Ancestor Of A Binary Tree Iii

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) | [LeetCode](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree DFS return contract.
- Invariant/state: With parent pointers, walk ancestors or switch pointers like linked-list intersection.
- Code idea: Move a and b upward; when null redirect to the other node; meeting is LCA.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 45. Lowest Common Ancestor Of A Binary Tree Iv

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) | [LeetCode](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iv/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree DFS return contract.
- Invariant/state: For many target nodes, current node is answer when multiple target paths meet.
- Code idea: Return root if in target set; combine child returns and current membership.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 46. Minimum Absolute Difference In BST

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | [LeetCode](https://leetcode.com/problems/minimum-absolute-difference-in-bst/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using BST property.
- Invariant/state: BST inorder is sorted, so minimum difference is between adjacent inorder values.
- Code idea: Inorder traverse, track previous value and best difference.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 47. Range Sum Of BST

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | [LeetCode](https://leetcode.com/problems/range-sum-of-bst/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using BST property.
- Invariant/state: BST ordering lets you prune subtrees outside [low, high].
- Code idea: If node < low go right, if node > high go left, else add node and both sides.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 48. Construct Binary Search Tree From Preorder Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) | [LeetCode](https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree recursion / hashmap index.
- Invariant/state: Preorder root plus BST bounds tells where each next value belongs.
- Code idea: Use index over preorder and recursive upper/lower bounds to build nodes.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 49. Construct Binary Tree From Inorder And Postorder Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) | [LeetCode](https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree recursion / hashmap index.
- Invariant/state: Postorder last is root; inorder index splits left and right subtrees.
- Code idea: Pop root from postorder end, build right then left using inorder bounds.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 50. Construct Binary Tree From Preorder And Inorder Traversal

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) | [LeetCode](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree recursion / hashmap index.
- Invariant/state: Preorder first is root; inorder index splits left and right subtrees.
- Code idea: Read preorder index, split by inorder map, recursively build left and right ranges.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 51. Verify Preorder Serialization Of A Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) | [LeetCode](https://leetcode.com/problems/verify-preorder-serialization-of-a-binary-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree recursion / hashmap index.
- Invariant/state: Slots start at one; every node consumes a slot, non-null nodes create two.
- Code idea: For each token decrement slots, fail below zero, add two slots if token is not #.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 52. Serialize And Deserialize Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/SerializeAndDeserializeBinaryTree.java) | [LeetCode](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree BFS/DFS serialization.
- Invariant/state: Include null markers so structure can be reconstructed unambiguously.
- Code idea: Preorder/BFS serialize with # for null; deserialize by consuming tokens in same order.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 53. Balanced Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session3/BinaryTree.java) | [LeetCode](https://leetcode.com/problems/balanced-binary-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Core tree patterns.
- Invariant/state: Return height, but use -1 or flag to propagate unbalanced subtrees early.
- Code idea: DFS left/right heights, if either -1 or diff > 1 return -1 else max+1.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 54. Diameter Of Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session3/BinaryTree.java) | [LeetCode](https://leetcode.com/problems/diameter-of-binary-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Core tree patterns.
- Invariant/state: Diameter through a node is left height plus right height; return height upward.
- Code idea: Postorder compute heights, update max diameter with left+right, return max height+1.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 55. Maximum Depth Of Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session3/BinaryTree.java) | [LeetCode](https://leetcode.com/problems/maximum-depth-of-binary-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Core tree patterns.
- Invariant/state: Depth is one plus the deeper child depth.
- Code idea: Return 0 for null, else 1 + max(depth(left), depth(right)).
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 56. Invert Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session3/InvertBinaryTree.java) | [LeetCode](https://leetcode.com/problems/invert-binary-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree DFS/BFS.
- Invariant/state: Swap left and right at every node.
- Code idea: DFS or BFS each node, swap children, continue.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 57. Kth Smallest Element In A BST

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session3/KthSmallestElementInBST.java) | [LeetCode](https://leetcode.com/problems/kth-smallest-element-in-a-bst/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using BST inorder.
- Invariant/state: BST inorder gives ascending values; kth visited is the answer.
- Code idea: Iterative inorder with stack, decrement k on visit, return when k hits zero.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 58. Binary Tree Maximum Path Sum

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) | [LeetCode](https://leetcode.com/problems/binary-tree-maximum-path-sum/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree path DFS / global answer.
- Invariant/state: Helper returns best non-splitting gain; global answer may split through node.
- Code idea: Clamp child gains at zero, update global with node+left+right, return node+max(left,right).
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 59. Path Sum

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) | [LeetCode](https://leetcode.com/problems/path-sum/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree path DFS / global answer.
- Invariant/state: Subtract node values along root-to-leaf paths and check target at leaf.
- Code idea: DFS with remaining sum; at leaf return remaining == node.val.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 60. Path Sum Ii

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) | [LeetCode](https://leetcode.com/problems/path-sum-ii/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree path DFS / global answer.
- Invariant/state: Backtrack the current root-to-leaf path and copy it when the target is hit.
- Code idea: Add node, recurse children with remaining sum, copy on valid leaf, remove node.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 61. Path Sum Iii

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) | [LeetCode](https://leetcode.com/problems/path-sum-iii/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree path DFS / global answer.
- Invariant/state: Use prefix sums on the root-to-current path to count paths ending at this node.
- Code idea: DFS with running sum, add count[sum-target], increment before children, decrement on backtrack.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 62. Sum Root To Leaf Numbers

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) | [LeetCode](https://leetcode.com/problems/sum-root-to-leaf-numbers/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree path DFS / global answer.
- Invariant/state: Carry the number formed so far; at a leaf, add it to the total.
- Code idea: DFS with value = value * 10 + node.val; return value at leaves, sum children otherwise.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 63. Number Of Provinces

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LeetCode](https://leetcode.com/problems/number-of-provinces/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph BFS, using Matrix DFS/BFS components.
- Invariant/state: For unweighted minimum steps, mark when enqueuing because first discovery is shortest.
- Code idea: Queue start states, mark visited immediately, expand valid neighbors by level.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 64. 01 Matrix

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Matrix01.java) | [LeetCode](https://leetcode.com/problems/01-matrix/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph BFS, using Multi-source BFS.
- Invariant/state: For unweighted minimum steps, mark when enqueuing because first discovery is shortest.
- Code idea: Queue start states, mark visited immediately, expand valid neighbors by level.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 65. Rotting Oranges

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/RottenOranges.java) | [LeetCode](https://leetcode.com/problems/rotting-oranges/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph BFS, using Multi-source BFS.
- Invariant/state: For unweighted minimum steps, mark when enqueuing because first discovery is shortest.
- Code idea: Queue start states, mark visited immediately, expand valid neighbors by level.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 66. Word Ladder

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session3/WordLadder.java) | [LeetCode](https://leetcode.com/problems/word-ladder/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph BFS, using BFS shortest path.
- Invariant/state: For unweighted minimum steps, mark when enqueuing because first discovery is shortest.
- Code idea: Queue start states, mark visited immediately, expand valid neighbors by level.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 67. Flood Fill

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/FloodFill.java) | [LeetCode](https://leetcode.com/problems/flood-fill/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph DFS, using Matrix DFS/BFS.
- Invariant/state: Mark visited before recursion and explore one component/path completely.
- Code idea: Mark visited, recursively explore neighbors, carry parent/state when cycles matter.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 68. Max Area Of Island

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LeetCode](https://leetcode.com/problems/max-area-of-island/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph DFS, using Matrix DFS/BFS components.
- Invariant/state: Mark visited before recursion and explore one component/path completely.
- Code idea: Mark visited, recursively explore neighbors, carry parent/state when cycles matter.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 69. Number Of Closed Islands

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LeetCode](https://leetcode.com/problems/number-of-closed-islands/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph DFS, using Matrix DFS/BFS components.
- Invariant/state: Mark visited before recursion and explore one component/path completely.
- Code idea: Mark visited, recursively explore neighbors, carry parent/state when cycles matter.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 70. Number Of Islands

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LeetCode](https://leetcode.com/problems/number-of-islands/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph DFS, using Matrix DFS/BFS components.
- Invariant/state: Mark visited before recursion and explore one component/path completely.
- Code idea: Mark visited, recursively explore neighbors, carry parent/state when cycles matter.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 71. Pacific Atlantic Water Flow

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LeetCode](https://leetcode.com/problems/pacific-atlantic-water-flow/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph DFS, using Matrix DFS/BFS components.
- Invariant/state: Mark visited before recursion and explore one component/path completely.
- Code idea: Mark visited, recursively explore neighbors, carry parent/state when cycles matter.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 72. Surrounded Regions

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) | [LeetCode](https://leetcode.com/problems/surrounded-regions/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph DFS, using Matrix DFS/BFS components.
- Invariant/state: Mark visited before recursion and explore one component/path completely.
- Code idea: Mark visited, recursively explore neighbors, carry parent/state when cycles matter.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 73. Clone Graph

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/CloneGraph.java) | [LeetCode](https://leetcode.com/problems/clone-graph/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph DFS, using Graph DFS/BFS clone.
- Invariant/state: Mark visited before recursion and explore one component/path completely.
- Code idea: Mark visited, recursively explore neighbors, carry parent/state when cycles matter.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 74. Is Graph Bipartite

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) | [LeetCode](https://leetcode.com/problems/is-graph-bipartite/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph DFS, using BFS/DFS coloring.
- Invariant/state: Mark visited before recursion and explore one component/path completely.
- Code idea: Mark visited, recursively explore neighbors, carry parent/state when cycles matter.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 75. Binary Search

- Links: [Java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) | [LeetCode](https://leetcode.com/problems/binary-search/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using Binary search invariant.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 76. First Bad Version

- Links: [Java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) | [LeetCode](https://leetcode.com/problems/first-bad-version/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using Binary search invariant.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 77. Search Insert Position

- Links: [Java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) | [LeetCode](https://leetcode.com/problems/search-insert-position/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using Binary search invariant.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 78. Find First And Last Position Of Element In Sorted Array

- Links: [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LeetCode](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using Binary search boundary.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 79. Find Peak Element

- Links: [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LeetCode](https://leetcode.com/problems/find-peak-element/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using Binary search boundary.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 80. Search In Rotated Sorted Array

- Links: [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LeetCode](https://leetcode.com/problems/search-in-rotated-sorted-array/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using Binary search boundary.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 81. Search In Rotated Sorted Array Ii

- Links: [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LeetCode](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using Binary search boundary.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 82. Sqrtx

- Links: [Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LeetCode](https://leetcode.com/problems/sqrtx/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using Binary search boundary.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 83. Split Array Largest Sum

- Links: [Java](../../src/main/java/org/chijai/day2/session2/AGGRCOW.java) | [LeetCode](https://leetcode.com/problems/split-array-largest-sum/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using Binary search on answer.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 84. Capacity To Ship Packages Within D Days

- Links: [Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | [LeetCode](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using Binary search on answer.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 85. Koko Eating Bananas

- Links: [Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | [LeetCode](https://leetcode.com/problems/koko-eating-bananas/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using Binary search on answer.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 86. Minimum Number Of Days To Make M Bouquets

- Links: [Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | [LeetCode](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using Binary search on answer.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 87. Insert Into A Binary Search Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | [LeetCode](https://leetcode.com/problems/insert-into-a-binary-search-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using BST property.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 88. Lowest Common Ancestor Of A Binary Search Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | [LeetCode](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using BST property.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 89. Search In A Binary Search Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | [LeetCode](https://leetcode.com/problems/search-in-a-binary-search-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using BST property.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 90. Daily Temperatures

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session1/DailyTemperatures.java) | [LeetCode](https://leetcode.com/problems/daily-temperatures/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Monotonic stack.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 91. Next Greater Element Ii

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session1/NextGreaterElement.java) | [LeetCode](https://leetcode.com/problems/next-greater-element-ii/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Monotonic stack.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 92. Largest Rectangle In Histogram

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/LargestRectangle.java) | [LeetCode](https://leetcode.com/problems/largest-rectangle-in-histogram/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Monotonic stack.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 93. Maximal Rectangle

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/LargestRectangle.java) | [LeetCode](https://leetcode.com/problems/maximal-rectangle/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Monotonic stack.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 94. Sum Of Subarray Minimums

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session2/LargestRectangle.java) | [LeetCode](https://leetcode.com/problems/sum-of-subarray-minimums/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Monotonic stack.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 95. Valid Parentheses

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session3/ValidParentheses.java) | [LeetCode](https://leetcode.com/problems/valid-parentheses/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Stack.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 96. Meeting Rooms

- Links: [Java](../../src/main/java/org/chijai/day1/session2/MeetingRoom.java) | [LeetCode](https://leetcode.com/problems/meeting-rooms/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Heap, using Intervals / heap.
- Invariant/state: Heap top is the next best candidate; keep only the frontier or top K when possible.
- Code idea: Push candidates with comparator; poll when size or frontier rules require it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 97. Meeting Rooms Ii

- Links: [Java](../../src/main/java/org/chijai/day1/session2/MeetingRoom.java) | [LeetCode](https://leetcode.com/problems/meeting-rooms-ii/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Heap, using Intervals / heap.
- Invariant/state: Heap top is the next best candidate; keep only the frontier or top K when possible.
- Code idea: Push candidates with comparator; poll when size or frontier rules require it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 98. Intervals

- Links: [Java](../../src/main/java/org/chijai/day1/session2/Intervals.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Intervals/Greedy, using Intervals / merge.
- Invariant/state: Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints.
- Code idea: Sort by start/end, then merge/count/select with one pass or heap.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 99. Word Search

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/WordSearch.java) | [LeetCode](https://leetcode.com/problems/word-search/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Backtracking, using DFS backtracking.
- Invariant/state: Choose, recurse, undo; the path is exactly the current decision state.
- Code idea: Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 100. Course Schedule Ii

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) | [LeetCode](https://leetcode.com/problems/course-schedule-ii/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Topological Sort, using Topological sort / cycle.
- Invariant/state: Use indegree or DFS states to process dependencies before dependents.
- Code idea: Build graph and indegrees, queue zero-indegree nodes, process order.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 101. Longest Palindrome

- Links: [Java](../../src/main/java/org/chijai/day3/session3/LongestPalindrome.java) | [LeetCode](https://leetcode.com/problems/longest-palindrome/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Two Pointers, using Hash/frequency.
- Invariant/state: Shrink the search space by moving the pointer that can still improve the answer.
- Code idea: Initialize pointers, compare current state, move the pointer whose movement is justified.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 102. Sliding Window Maximum

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session3/StackQueue.java) | [LeetCode](https://leetcode.com/problems/sliding-window-maximum/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Sliding Window, using Stack/queue design.
- Invariant/state: Expand right, shrink left to restore validity, then update the answer at the right time.
- Code idea: Move right to include, move left while invalid or while answer can improve.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 103. Implement Trie Prefix Tree

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) | [LeetCode](https://leetcode.com/problems/implement-trie-prefix-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Prefix/Suffix, using Trie.
- Invariant/state: Precompute cumulative left/right state so each range or exclusion is answered cheaply.
- Code idea: Build prefix/suffix arrays or running aggregates, then combine in O(1) per query/index.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 104. Longest Palindromic Substring

- Links: [Java](../../src/main/java/org/chijai/day3/session3/LongestPalindromicSubstring.java) | [LeetCode](https://leetcode.com/problems/longest-palindromic-substring/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Expand around center.
- Invariant/state: Define exactly what the helper returns, combine left/right, and update global answer separately if needed.
- Code idea: Base case null, recurse left/right, compute local result, return contract.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 105. Burn Binary Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Tree + graph BFS.
- Invariant/state: Define exactly what the helper returns, combine left/right, and update global answer separately if needed.
- Code idea: Base case null, recurse left/right, compute local result, return contract.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 106. Convert BST To Greater Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) | [LeetCode](https://leetcode.com/problems/convert-bst-to-greater-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using BST inorder.
- Invariant/state: Define exactly what the helper returns, combine left/right, and update global answer separately if needed.
- Code idea: Base case null, recurse left/right, compute local result, return contract.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 107. K Highest Ranked Items Within A Price Range

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session3/KHighestRankedItemsWithinAPriceRange.java) | [LeetCode](https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph BFS, using BFS + sorting.
- Invariant/state: For unweighted minimum steps, mark when enqueuing because first discovery is shortest.
- Code idea: Queue start states, mark visited immediately, expand valid neighbors by level.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 108. Coloring A Border

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session1/ColoringABorder.java) | [LeetCode](https://leetcode.com/problems/coloring-a-border/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph DFS, using Matrix DFS.
- Invariant/state: Mark visited before recursion and explore one component/path completely.
- Code idea: Mark visited, recursively explore neighbors, carry parent/state when cycles matter.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 109. Network Delay Time

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session2/NetworkDelayTime.java) | [LeetCode](https://leetcode.com/problems/network-delay-time/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Graph DFS, using Dijkstra / graph.
- Invariant/state: Mark visited before recursion and explore one component/path completely.
- Code idea: Mark visited, recursively explore neighbors, carry parent/state when cycles matter.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 110. Maximum Profit In Job Scheduling

- Links: [Java](../../src/main/java/org/chijai/day2/session3/MaximumProfitInJobScheduling.java) | [LeetCode](https://leetcode.com/problems/maximum-profit-in-job-scheduling/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using DP + binary search.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 111. Time Based Key Value Store

- Links: [Java](../../src/main/java/org/chijai/day2/session3/TimeBasedKeyValueStore.java) | [LeetCode](https://leetcode.com/problems/time-based-key-value-store/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using HashMap + binary search.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 112. Binary Search Tree Iterator

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) | [LeetCode](https://leetcode.com/problems/binary-search-tree-iterator/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using BST inorder.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 113. Recover Binary Search Tree

- Links: [Java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) | [LeetCode](https://leetcode.com/problems/recover-binary-search-tree/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Binary Search, using BST inorder.
- Invariant/state: Maintain a monotonic search space and discard the half that cannot contain the answer.
- Code idea: Define left/right and predicate; update the boundary without losing the answer.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 114. Design A Stack With Increment Operation

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LeetCode](https://leetcode.com/problems/design-a-stack-with-increment-operation/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Stack design.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 115. Max Stack

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LeetCode](https://leetcode.com/problems/max-stack/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Stack design.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 116. Min Stack

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LeetCode](https://leetcode.com/problems/min-stack/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Stack design.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 117. Next Greater Element I

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LeetCode](https://leetcode.com/problems/next-greater-element-i/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Stack design.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 118. Basic Calculator

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session3/BasicCalculator.java) | [LeetCode](https://leetcode.com/problems/basic-calculator/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Stack / expression parsing.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 119. Evaluate Reverse Polish Notation

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session3/EvalRPN.java) | [LeetCode](https://leetcode.com/problems/evaluate-reverse-polish-notation/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Stack.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 120. Design Circular Queue

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session3/StackQueue.java) | [LeetCode](https://leetcode.com/problems/design-circular-queue/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Stack/queue design.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 121. Implement Queue Using Stacks

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session3/StackQueue.java) | [LeetCode](https://leetcode.com/problems/implement-queue-using-stacks/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Stack/queue design.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 122. Implement Stack Using Queues

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session3/StackQueue.java) | [LeetCode](https://leetcode.com/problems/implement-stack-using-queues/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Stack, using Stack/queue design.
- Invariant/state: Stack stores unresolved candidates; current item resolves or validates the top.
- Code idea: While top is resolved by current value, pop and compute; then push current.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 123. Award Top K Hotels

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/AwardTopKHotels.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Heap, using Heap / ranking.
- Invariant/state: Heap top is the next best candidate; keep only the frontier or top K when possible.
- Code idea: Push candidates with comparator; poll when size or frontier rules require it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 124. Sort Characters By Frequency

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java) | [LeetCode](https://leetcode.com/problems/sort-characters-by-frequency/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Heap, using Heap fundamentals.
- Invariant/state: Heap top is the next best candidate; keep only the frontier or top K when possible.
- Code idea: Push candidates with comparator; poll when size or frontier rules require it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 125. K Closest Points To Origin

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/KClosestPointsToOrigin.java) | [LeetCode](https://leetcode.com/problems/k-closest-points-to-origin/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Heap, using Heap / quickselect.
- Invariant/state: Heap top is the next best candidate; keep only the frontier or top K when possible.
- Code idea: Push candidates with comparator; poll when size or frontier rules require it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 126. Kth Largest Element In A Stream

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) | [LeetCode](https://leetcode.com/problems/kth-largest-element-in-a-stream/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Heap, using Min-heap size K.
- Invariant/state: Heap top is the next best candidate; keep only the frontier or top K when possible.
- Code idea: Push candidates with comparator; poll when size or frontier rules require it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 127. Kth Largest Element In An Array

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) | [LeetCode](https://leetcode.com/problems/kth-largest-element-in-an-array/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Heap, using Min-heap size K.
- Invariant/state: Heap top is the next best candidate; keep only the frontier or top K when possible.
- Code idea: Push candidates with comparator; poll when size or frontier rules require it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 128. Find Median From Data Stream

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/Median.java) | [LeetCode](https://leetcode.com/problems/find-median-from-data-stream/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Heap, using Two heaps.
- Invariant/state: Heap top is the next best candidate; keep only the frontier or top K when possible.
- Code idea: Push candidates with comparator; poll when size or frontier rules require it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 129. Task Scheduler

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/TaskScheduler.java) | [LeetCode](https://leetcode.com/problems/task-scheduler/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Heap, using Greedy / heap.
- Invariant/state: Heap top is the next best candidate; keep only the frontier or top K when possible.
- Code idea: Push candidates with comparator; poll when size or frontier rules require it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 130. Top K Frequent Elements

- Links: [Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentTransactions.java) | [LeetCode](https://leetcode.com/problems/top-k-frequent-elements/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Heap, using Frequency + heap/bucket.
- Invariant/state: Heap top is the next best candidate; keep only the frontier or top K when possible.
- Code idea: Push candidates with comparator; poll when size or frontier rules require it.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 131. Car Pooling

- Links: [Java](../../src/main/java/org/chijai/day3/session2/MinimumPlatforms.java) | [LeetCode](https://leetcode.com/problems/car-pooling/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Intervals/Greedy, using Intervals / sorting.
- Invariant/state: Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints.
- Code idea: Sort by start/end, then merge/count/select with one pass or heap.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 132. Minimum Number Of Arrows To Burst Balloons

- Links: [Java](../../src/main/java/org/chijai/day3/session2/MinimumPlatforms.java) | [LeetCode](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Intervals/Greedy, using Intervals / sorting.
- Invariant/state: Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints.
- Code idea: Sort by start/end, then merge/count/select with one pass or heap.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 133. Word Search Ii

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/WordSearchII.java) | [LeetCode](https://leetcode.com/problems/word-search-ii/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Backtracking, using Trie + backtracking.
- Invariant/state: Choose, recurse, undo; the path is exactly the current decision state.
- Code idea: Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 134. Combination Sum

- Links: [Java](../../src/main/java/org/chijai/day11/backtracking/session1/CombinationSum.java) | [LeetCode](https://leetcode.com/problems/combination-sum/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Backtracking, using Backtracking reuse.
- Invariant/state: Choose, recurse, undo; the path is exactly the current decision state.
- Code idea: Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 135. Letter Combinations Of A Phone Number

- Links: [Java](../../src/main/java/org/chijai/day11/backtracking/session1/LetterCombinationsOfAPhoneNumber.java) | [LeetCode](https://leetcode.com/problems/letter-combinations-of-a-phone-number/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Backtracking, using Backtracking / mapping.
- Invariant/state: Choose, recurse, undo; the path is exactly the current decision state.
- Code idea: Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 136. Permutations

- Links: [Java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) | [LeetCode](https://leetcode.com/problems/permutations/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Backtracking, using Backtracking permutations.
- Invariant/state: Choose, recurse, undo; the path is exactly the current decision state.
- Code idea: Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 137. Permutations Ii

- Links: [Java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) | [LeetCode](https://leetcode.com/problems/permutations-ii/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Backtracking, using Backtracking permutations.
- Invariant/state: Choose, recurse, undo; the path is exactly the current decision state.
- Code idea: Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 138. Subsets

- Links: [Java](../../src/main/java/org/chijai/day11/backtracking/session1/Subsets.java) | [LeetCode](https://leetcode.com/problems/subsets/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Backtracking, using Backtracking subsets.
- Invariant/state: Choose, recurse, undo; the path is exactly the current decision state.
- Code idea: Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 139. Design Add And Search Words Data Structure

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) | [LeetCode](https://leetcode.com/problems/design-add-and-search-words-data-structure/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Trie, using Trie + DFS wildcard.
- Invariant/state: Each node is a prefix; branch only when wildcard or board search requires it.
- Code idea: Insert words by characters; search follows children and DFS branches on wildcard/board.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 140. Kadane Max Sub Array

- Links: [Java](../../src/main/java/org/chijai/day1/session1/KadaneMaxSubArray.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Dynamic Programming, using Kadane / DP.
- Invariant/state: Fix dp state meaning, base cases, transition, and iteration order before coding.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 141. Sort Colors

- Links: [Java](../../src/main/java/org/chijai/day1/session1/SortColors.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Dynamic Programming, using Partition / Dutch flag.
- Invariant/state: Fix dp state meaning, base cases, transition, and iteration order before coding.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 142. Best Time To Buy And Sell Stock

- Links: [Java](../../src/main/java/org/chijai/day1/session3/StockSeries1.java) | [LeetCode](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Dynamic Programming, using Greedy / DP states.
- Invariant/state: Fix dp state meaning, base cases, transition, and iteration order before coding.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 143. Stock Series2

- Links: [Java](../../src/main/java/org/chijai/day1/session3/StockSeries2.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Dynamic Programming, using Stock DP variants.
- Invariant/state: Fix dp state meaning, base cases, transition, and iteration order before coding.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 144. Online Stock Span

- Links: [Java](../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LeetCode](https://leetcode.com/problems/online-stock-span/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Dynamic Programming, using Stack design.
- Invariant/state: Fix dp state meaning, base cases, transition, and iteration order before coding.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 145. Climbing Stairs Fib

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session1/ClimbingStairsFib.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Dynamic Programming, using 1D DP.
- Invariant/state: Fix dp state meaning, base cases, transition, and iteration order before coding.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 146. House Robber

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session1/HouseRobber.java) | [LeetCode](https://leetcode.com/problems/house-robber/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Dynamic Programming, using 1D DP.
- Invariant/state: Fix dp state meaning, base cases, transition, and iteration order before coding.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 147. Unique Paths

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session1/UniquePaths.java) | [LeetCode](https://leetcode.com/problems/unique-paths/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Dynamic Programming, using Grid DP.
- Invariant/state: Fix dp state meaning, base cases, transition, and iteration order before coding.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 148. Coin Change

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) | [LeetCode](https://leetcode.com/problems/coin-change/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Dynamic Programming, using Unbounded knapsack DP.
- Invariant/state: Fix dp state meaning, base cases, transition, and iteration order before coding.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 149. Edit Distance

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) | [LeetCode](https://leetcode.com/problems/edit-distance/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Dynamic Programming, using 2D DP.
- Invariant/state: Fix dp state meaning, base cases, transition, and iteration order before coding.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 150. Longest Increasing Subsequence

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) | [LeetCode](https://leetcode.com/problems/longest-increasing-subsequence/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Dynamic Programming, using DP / patience sorting.
- Invariant/state: Fix dp state meaning, base cases, transition, and iteration order before coding.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 151. Partition Equal Subset Sum

- Links: [Java](../../src/main/java/org/chijai/day9/dp/session2/PartitionEqualSubsetSum.java) | [LeetCode](https://leetcode.com/problems/partition-equal-subset-sum/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Dynamic Programming, using 0/1 knapsack DP.
- Invariant/state: Fix dp state meaning, base cases, transition, and iteration order before coding.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 152. Accounts Merge

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session3/AccountsMerge.java) | [LeetCode](https://leetcode.com/problems/accounts-merge/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Union Find, using Union Find / graph.
- Invariant/state: Represent components with parent links; union merges and failed union detects cycles.
- Code idea: Initialize parent/rank, find with compression, union by rank/size.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 153. Minimum Height Trees

- Links: [Java](../../src/main/java/org/chijai/day8/graph/session3/MinHTree.java) | [LeetCode](https://leetcode.com/problems/minimum-height-trees/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Topological Sort, using Topological trimming.
- Invariant/state: Use indegree or DFS states to process dependencies before dependents.
- Code idea: Build graph and indegrees, queue zero-indegree nodes, process order.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 154. Gas Station

- Links: [Java](../../src/main/java/org/chijai/day3/session2/GasStation.java) | [LeetCode](https://leetcode.com/problems/gas-station/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Greedy, using Greedy.
- Invariant/state: Take the local choice only after proving it cannot hurt the future optimum.
- Code idea: Sort or scan to make the safe local choice repeatedly.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 155. Spiral Matrix

- Links: [Java](../../src/main/java/org/chijai/day1/session1/SpiralMatrix.java) | [LeetCode](https://leetcode.com/problems/spiral-matrix/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Core Basics, using Matrix boundary traversal.
- Invariant/state: Start from brute force, find repeated work, and state the invariant before coding.
- Code idea: Code the invariant directly, then dry-run edge cases.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 156. String To Integer Atoi

- Links: [Java](../../src/main/java/org/chijai/day3/session3/StringToIntegerAtoi.java) | [LeetCode](https://leetcode.com/problems/string-to-integer-atoi/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Core Basics, using Parsing / edge cases.
- Invariant/state: Start from brute force, find repeated work, and state the invariant before coding.
- Code idea: Code the invariant directly, then dry-run edge cases.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 157. Shortest Palindrome

- Links: [Java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java) | [LeetCode](https://leetcode.com/problems/shortest-palindrome/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Two Pointers, using KMP string matching.
- Invariant/state: Shrink the search space by moving the pointer that can still improve the answer.
- Code idea: Initialize pointers, compare current state, move the pointer whose movement is justified.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 158. Find The Index Of The First Occurrence In A String

- Links: [Java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) | [LeetCode](https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Prefix/Suffix, using KMP / rolling hash.
- Invariant/state: Precompute cumulative left/right state so each range or exclusion is answered cheaply.
- Code idea: Build prefix/suffix arrays or running aggregates, then combine in O(1) per query/index.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 159. Longest Happy Prefix

- Links: [Java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) | [LeetCode](https://leetcode.com/problems/longest-happy-prefix/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Prefix/Suffix, using KMP / rolling hash.
- Invariant/state: Precompute cumulative left/right state so each range or exclusion is answered cheaply.
- Code idea: Build prefix/suffix arrays or running aggregates, then combine in O(1) per query/index.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 160. Count Unique Characters Of All Substrings Of A Given String

- Links: [Java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java) | [LeetCode](https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using Contribution counting.
- Invariant/state: Define exactly what the helper returns, combine left/right, and update global answer separately if needed.
- Code idea: Base case null, recurse left/right, compute local result, return contract.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 161. Repeated Substring Pattern

- Links: [Java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java) | [LeetCode](https://leetcode.com/problems/repeated-substring-pattern/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Tree DFS, using KMP string matching.
- Invariant/state: Define exactly what the helper returns, combine left/right, and update global answer separately if needed.
- Code idea: Base case null, recurse left/right, compute local result, return contract.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 162. Hotel Reviews

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/HotelReviews.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Trie, using Trie / ranking.
- Invariant/state: Each node is a prefix; branch only when wildcard or board search requires it.
- Code idea: Insert words by characters; search follows children and DFS branches on wildcard/board.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 163. Maximum XOR Of Two Numbers In An Array

- Links: [Java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) | [LeetCode](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Trie, using Binary trie / bit.
- Invariant/state: Each node is a prefix; branch only when wildcard or board search requires it.
- Code idea: Insert words by characters; search follows children and DFS branches on wildcard/board.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 164. Design Fraud Pattern Detection

- Links: [Java](../../src/main/java/org/chijai/design/lld/DesignFraudPatternDetection.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Dynamic Programming, using LLD / domain modeling.
- Invariant/state: Fix dp state meaning, base cases, transition, and iteration order before coding.
- Code idea: Initialize base states, fill states in dependency order, return target state.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 165. Add Binary

- Links: [Java](../../src/main/java/org/chijai/day10/session2/AddBinary.java) | [LeetCode](https://leetcode.com/problems/add-binary/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Math/Bit/String, using Bit/string addition.
- Invariant/state: Use the algebra, bit, or string invariant instead of simulating blindly.
- Code idea: Track the exact numeric/string invariant and update it in constant or linear time.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 166. Count Primes

- Links: [Java](../../src/main/java/org/chijai/day10/session2/CountPrimes.java) | [LeetCode](https://leetcode.com/problems/count-primes/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Math/Bit/String, using Math / sieve.
- Invariant/state: Use the algebra, bit, or string invariant instead of simulating blindly.
- Code idea: Track the exact numeric/string invariant and update it in constant or linear time.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 167. Api Integration Example

- Links: [Java](../../src/main/java/org/chijai/design/lld/ApiIntegrationExample.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Design/LLD, using LLD/API integration.
- Invariant/state: State API contract, data structures, invariants, and complexity per operation.
- Code idea: Implement operations around maps, lists, queues, heaps, or tries with clear invariants.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 168. Design Redis

- Links: [Java](../../src/main/java/org/chijai/design/lld/DesignRedis.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Design/LLD, using LLD / data structures.
- Invariant/state: State API contract, data structures, invariants, and complexity per operation.
- Code idea: Implement operations around maps, lists, queues, heaps, or tries with clear invariants.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 169. Design Token Bucket Rate Limiter

- Links: [Java](../../src/main/java/org/chijai/design/lld/DesignTokenBucketRateLimiter.java)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Design/LLD, using LLD / rate limiting.
- Invariant/state: State API contract, data structures, invariants, and complexity per operation.
- Code idea: Implement operations around maps, lists, queues, heaps, or tries with clear invariants.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 170. Encode And Decode Tinyurl

- Links: [Java](../../src/main/java/org/chijai/design/lld/DesignUrlShortner.java) | [LeetCode](https://leetcode.com/problems/encode-and-decode-tinyurl/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Design/LLD, using LLD / URL shortener.
- Invariant/state: State API contract, data structures, invariants, and complexity per operation.
- Code idea: Implement operations around maps, lists, queues, heaps, or tries with clear invariants.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.

## 171. Two Sum

- Links: [Java](../../src/main/java/org/chijai/design/lld/DesignUrlShortner.java) | [LeetCode](https://leetcode.com/problems/two-sum/)
- Brute force: Try all candidate states or combinations directly.
- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.
- Pattern: Design/LLD, using LLD / URL shortener.
- Invariant/state: State API contract, data structures, invariants, and complexity per operation.
- Code idea: Implement operations around maps, lists, queues, heaps, or tries with clear invariants.
- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.