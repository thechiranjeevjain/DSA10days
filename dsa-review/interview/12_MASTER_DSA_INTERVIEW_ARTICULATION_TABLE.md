# Master DSA Interview Articulation Table

Purpose: one retrieval sheet for speaking the exact correctness contract before coding. This is not a solution summary.

Organization: Pattern -> Sub-pattern. Similar rows stay together, while the rank inside each problem preserves interview ROI from `01_ZERO_TO_HERO_RANKED_TABLE.md`.

Use each row as: problem wording -> exact state meaning -> invariant -> transition -> trap. If a row does not constrain the code strongly enough, improve the generator instead of hand-editing this file.

## Precision Rules

- Say the contract before coding; then code only what the contract permits.
- Treat words like `exceeds`, `reaches`, `unused`, `seen`, `current`, `total`, `first`, and `any` as operator-level words.
- For exact operators, preserve the literal token when it matters: `>`, `>=`, `<`, `<=`, `i + 1`, `right - k`, `right >= k - 1`, `dp[0] = true`.
- Rows are generated from local ranked metadata, curated problem hooks, and checked source chapters. Category fallback rows intentionally avoid unverified operator claims.

## HashMap / Frequency / Set

### Two pointers / hash

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **1. [Two Sum](https://leetcode.com/problems/two-sum/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session2/Three3Sum2Sum.java) / [LC](https://leetcode.com/problems/two-sum/) | The map contains only indices already passed, so a complement hit never reuses the current element. For current value x, ask whether target - x has been seen before inserting x. If found, return the stored old index and the current index. | check complement before insert; no self reuse |

### Frequency count

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **9. [Valid Anagram](https://leetcode.com/problems/valid-anagram/)**<br>[Java](../../src/main/java/org/chijai/day3/session3/ValidAnagram.java) / [LC](https://leetcode.com/problems/valid-anagram/) | The count array/map is the net balance between the two strings. Increment for one string and decrement for the other, then every count must be zero. If lengths differ, fail before counting. | check insert/consume order |

### Boyer-Moore / frequency

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **51. [Majority Element](https://leetcode.com/problems/majority-element/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session2/MajorityElement.java) / [LC](https://leetcode.com/problems/majority-element/) | candidate survives pair cancellation between different values. When count becomes zero, the current value becomes the new candidate; equal increments and different decrements. This is valid because a value appearing more than n/2 cannot be fully cancelled. | reset candidate only when count == 0 |

### HashMap/frequency

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **53. [Ransom Note](https://leetcode.com/problems/ransom-note/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session1/RansomNote.java) / [LC](https://leetcode.com/problems/ransom-note/) | Define exactly what the map/set contains: processed values, counts, or remaining supply. Check before insert when the current item cannot pair with itself; update after consumption when supply is being spent. The state must change exactly once per current item. | check insert/consume order |

### Hash/frequency

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **107. [Longest Palindrome](https://leetcode.com/problems/longest-palindrome/)**<br>[Java](../../src/main/java/org/chijai/day3/session3/LongestPalindrome.java) / [LC](https://leetcode.com/problems/longest-palindrome/) | Define exactly what the map/set contains: processed values, counts, or remaining supply. Check before insert when the current item cannot pair with itself; update after consumption when supply is being spent. The state must change exactly once per current item. | check insert/consume order |

## Binary Search / Answer Search

### Binary search invariant

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **2. [Binary Search](https://leetcode.com/problems/binary-search/)**<br>[Java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) / [LC](https://leetcode.com/problems/binary-search/) | The target can only be inside the current inclusive range [left, right]. Compare nums[mid] with target, then discard the half that sorted order proves impossible. Stop when left > right, because no candidate index remains. | left <= right; move by mid +/- 1 |
| **79. [Search Insert Position](https://leetcode.com/problems/search-insert-position/)**<br>[Java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) / [LC](https://leetcode.com/problems/search-insert-position/) | Name the candidate space and the monotonic predicate before coding. A true candidate must let you discard one side without losing the first/last feasible answer. Preserve inclusivity of left/right and move with mid + 1 or mid - 1 only after saving a feasible answer when required. | wrong equality boundary |
| **85. [First Bad Version](https://leetcode.com/problems/first-bad-version/)**<br>[Java](../../src/main/java/org/chijai/day2/session1/BinarySearch.java) / [LC](https://leetcode.com/problems/first-bad-version/) | Name the candidate space and the monotonic predicate before coding. A true candidate must let you discard one side without losing the first/last feasible answer. Preserve inclusivity of left/right and move with mid + 1 or mid - 1 only after saving a feasible answer when required. | wrong equality boundary |

### Binary search on answer

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **22. [Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/)**<br>[Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) / [LC](https://leetcode.com/problems/koko-eating-bananas/) | speed is the candidate bananas per hour and must start at 1, never 0. For each pile, required hours are ceil(pile / speed), and feasibility is totalHours <= h. If a speed works, every higher speed works, so save it and search smaller. | speed starts at 1; ceil division |
| **86. [Split Array Largest Sum](https://leetcode.com/problems/split-array-largest-sum/)**<br>[Java](../../src/main/java/org/chijai/day2/session2/AGGRCOW.java) / [LC](https://leetcode.com/problems/split-array-largest-sum/) | currentSum is the sum of the current contiguous partition and must never exceed maxAllowedSum. If currentSum + x > maxAllowedSum, close the current partition and let x start the next one. Feasibility is pieces <= m because extra allowed partitions can be split later when needed. | > not >=; x starts new partition |
| **90. [Capacity To Ship Packages Within D Days](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/)**<br>[Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) / [LC](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/) | currentLoad is the load already assigned to the current day and must never exceed capacity. Before assigning w, if currentLoad + w > capacity, open a new day and let w start that day; equality is allowed in the current day. Feasibility is requiredDays <= days. | > not >=; w starts new day |
| **91. [Minimum Number Of Days To Make M Bouquets](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/)**<br>[Java](../../src/main/java/org/chijai/day2/session2/KokoBananas.java) / [LC](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/) | flowers counts consecutive bloomed flowers not yet consumed into a bouquet. A flower with bloomDay <= day extends the streak; an unbloomed flower breaks adjacency and resets flowers to 0. When flowers == k, one bouquet consumes those k flowers, so increment bouquets and reset flowers to 0. | two resets: gap and flowers == k |

### Binary search boundary

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **23. [Search In Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/)**<br>[Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) / [LC](https://leetcode.com/problems/search-in-rotated-sorted-array/) | At least one half around mid is sorted. Decide which half is sorted, then keep it only if target lies within that half's inclusive bounds. Otherwise discard it and search the other half. | wrong equality boundary |
| **24. [Find First And Last Position Of Element In Sorted Array](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/)**<br>[Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) / [LC](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) | Use two boundary searches, not one hit plus expansion. The left boundary is the first index with value >= target; the right boundary is the last index with value <= target. Verify the boundaries actually equal target before returning them. | two boundary searches; verify equality |
| **74. [Search In Rotated Sorted Array II](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/)**<br>[Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) / [LC](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/) | Name the candidate space and the monotonic predicate before coding. A true candidate must let you discard one side without losing the first/last feasible answer. Preserve inclusivity of left/right and move with mid + 1 or mid - 1 only after saving a feasible answer when required. | wrong equality boundary |
| **84. [Find Peak Element](https://leetcode.com/problems/find-peak-element/)**<br>[Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) / [LC](https://leetcode.com/problems/find-peak-element/) | Name the candidate space and the monotonic predicate before coding. A true candidate must let you discard one side without losing the first/last feasible answer. Preserve inclusivity of left/right and move with mid + 1 or mid - 1 only after saving a feasible answer when required. | wrong equality boundary |
| **130. [Sqrtx](https://leetcode.com/problems/sqrtx/)**<br>[Java](../../src/main/java/org/chijai/day2/session1/SearchRange.java) / [LC](https://leetcode.com/problems/sqrtx/) | Name the candidate space and the monotonic predicate before coding. A true candidate must let you discard one side without losing the first/last feasible answer. Preserve inclusivity of left/right and move with mid + 1 or mid - 1 only after saving a feasible answer when required. | wrong equality boundary |

### HashMap + binary search

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **101. [Time Based Key Value Store](https://leetcode.com/problems/time-based-key-value-store/)**<br>[Java](../../src/main/java/org/chijai/day2/session3/TimeBasedKeyValueStore.java) / [LC](https://leetcode.com/problems/time-based-key-value-store/) | Name the candidate space and the monotonic predicate before coding. A true candidate must let you discard one side without losing the first/last feasible answer. Preserve inclusivity of left/right and move with mid + 1 or mid - 1 only after saving a feasible answer when required. | wrong equality boundary |

## Sliding Window

### Sliding window / set

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **3. [Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/)**<br>[Java](../../src/main/java/org/chijai/day3/session1/LongestSubString.java) / [LC](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | The active window contains no duplicate characters. When the current character was last seen inside the window, move left to lastSeen[ch] + 1, not one step blindly. Record the answer after the window is valid. | left = max(left, lastSeen + 1) |

### Sliding window / need-have

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **5. [Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/)**<br>[Java](../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) / [LC](https://leetcode.com/problems/minimum-window-substring/) | window counts only the current [left, right] characters and formed/have counts how many required character quotas are satisfied. Expand until every required quota is covered, then shrink from left while still valid and save the best before breaking validity. Extra copies do not increase formed once the quota is already met. | save answer before left removal breaks validity |

### Sliding window

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **47. [Longest Substring With At Most K Distinct Characters](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/)**<br>[Java](../../src/main/java/org/chijai/day3/session1/AtMostKDistinct.java) / [LC](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/) | The window state must describe exactly the current contiguous range. Include the right item, repair validity by moving left only while the invariant is broken or answer can improve, then update the answer at the correct valid moment. Removed items must also be removed from counts/sum/state. | remove left state when left moves |

### Sliding window frequency

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **52. Find All Anagrams In A String**<br>[Java](../../src/main/java/org/chijai/day3/session1/FindAllAnagramsInAString.java) | The window length must stay exactly p.length. Add the right char, remove the char that falls out once the window is too large, then compare frequency state. Record the left index only for complete matching windows. | remove left state when left moves |

### Queue / stream

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **81. [Moving Average From Data Stream](https://leetcode.com/problems/moving-average-from-data-stream/)**<br>[Java](../../src/main/java/org/chijai/day7/session1/heap/MovingAverage.java) / [LC](https://leetcode.com/problems/moving-average-from-data-stream/) | The window state must describe exactly the current contiguous range. Include the right item, repair validity by moving left only while the invariant is broken or answer can improve, then update the answer at the correct valid moment. Removed items must also be removed from counts/sum/state. | remove left state when left moves |

### Prefix/window counting

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **109. [Count Number Of Nice Subarrays](https://leetcode.com/problems/count-number-of-nice-subarrays/)**<br>[Java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/NiceSubArrays.java) / [LC](https://leetcode.com/problems/count-number-of-nice-subarrays/) | The window state must describe exactly the current contiguous range. Include the right item, repair validity by moving left only while the invariant is broken or answer can improve, then update the answer at the correct valid moment. Removed items must also be removed from counts/sum/state. | remove left state when left moves |

### DP / patience sorting

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **207. [Longest Continuous Increasing Subsequence](https://leetcode.com/problems/longest-continuous-increasing-subsequence/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) / [LC](https://leetcode.com/problems/longest-continuous-increasing-subsequence/) | The window state must describe exactly the current contiguous range. Include the right item, repair validity by moving left only while the invariant is broken or answer can improve, then update the answer at the correct valid moment. Removed items must also be removed from counts/sum/state. | remove left state when left moves |

## Prefix Sum / Prefix-Suffix

### Prefix/suffix

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **4. [Product Of Array Except Self](https://leetcode.com/problems/product-of-array-except-self/)**<br>[Java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/ProductOfArrayExceptSelf.java) / [LC](https://leetcode.com/problems/product-of-array-except-self/) | answer[i] first receives the product of all values strictly left of i, then gets multiplied by the product strictly right of i. The current element is never included in its own answer. This avoids division and handles zeros naturally. | strict vs inclusive prefix boundary |

### Prefix/window counting

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **50. [Binary Subarrays With Sum](https://leetcode.com/problems/binary-subarrays-with-sum/)**<br>[Java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/NiceSubArrays.java) / [LC](https://leetcode.com/problems/binary-subarrays-with-sum/) | For binary nonnegative arrays, exact goal equals atMost(goal) - atMost(goal - 1). atMost keeps a valid window with sum <= goal and adds right - left + 1 subarrays ending at right. Guard goal < 0 as zero. | atMost(goal) - atMost(goal - 1) |

## Linked List Pointers

### Pointer reversal

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **6. [Reverse Linked List](https://leetcode.com/problems/reverse-linked-list/)**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session1/ReverseLinkedList.java) / [LC](https://leetcode.com/problems/reverse-linked-list/) | Save next before changing current.next. After rewiring current.next to prev, move prev to current and current to saved next. When current becomes null, prev is the new head. | save next before current.next rewrite |

### Fast/slow pointers

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **7. [Linked List Cycle](https://leetcode.com/problems/linked-list-cycle/)**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session1/LinkedListCycle.java) / [LC](https://leetcode.com/problems/linked-list-cycle/) | slow advances one node and fast advances two nodes. Without a cycle, fast or fast.next reaches null; inside a cycle, the speed difference forces a meeting. Never dereference fast.next before checking it. | guard fast and fast.next |
| **110. Middle Of Linked List**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session4/MiddleOfLinkedList.java) | Every pointer has an ownership role: previous fixed node, current node being moved, and saved next node. Save next before rewiring, reconnect all boundary nodes, and return the real head after possible head changes. Node identity matters more than node value. | lost next pointer or wrong returned head |

### Merge / dummy node

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **8. [Merge Two Sorted Lists](https://leetcode.com/problems/merge-two-sorted-lists/)**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session4/Merge2SortedLists.java) / [LC](https://leetcode.com/problems/merge-two-sorted-lists/) | dummy.next is the real head and tail is the last node already attached to the merged list. At each step attach the smaller current node and advance only that source list. After one list ends, attach the remaining suffix directly. | lost next pointer or wrong returned head |

### HashMap + doubly linked list

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **25. [LRU Cache](https://leetcode.com/problems/lru-cache/)**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) / [LC](https://leetcode.com/problems/lru-cache/) | The map owns key to node lookup, and the doubly linked list owns recency order from most-recent to least-recent. Every get or updated put moves the node to the front. When capacity is exceeded, remove the tail node from both list and map. | remove evicted node from map and list |
| **82. [Design Browser History](https://leetcode.com/problems/design-browser-history/)**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) / [LC](https://leetcode.com/problems/design-browser-history/) | Every pointer has an ownership role: previous fixed node, current node being moved, and saved next node. Save next before rewiring, reconnect all boundary nodes, and return the real head after possible head changes. Node identity matters more than node value. | lost next pointer or wrong returned head |

### HashMap / interleaving copy

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **26. Copy List With Random Pointer**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session2/CopyListWithRandomPointer.java) | Each original node must map to exactly one cloned node, preserving identity rather than value. After clones exist, set each clone's next and random by looking up the original node's targets. Null random remains null. | lost next pointer or wrong returned head |

### Linked list two pointers

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **54. [Intersection Of Two Linked Lists](https://leetcode.com/problems/intersection-of-two-linked-lists/)**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session1/Intersection.java) / [LC](https://leetcode.com/problems/intersection-of-two-linked-lists/) | Each pointer walks its list then switches to the other head at null. After both switches, the remaining path lengths are equalized. They meet at the shared node identity or both reach null. | compare node identity, not value |

### Floyd cycle entry

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **55. Linked List Cycle II**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session4/LinkedListCycleII.java) | After slow and fast meet inside the cycle, reset one pointer to head. Move both one step at a time; their meeting point is the cycle entry. The first meeting itself is not necessarily the entry. | meeting point is not always entry |

### Linked-list reversal groups

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **56. [Reverse Nodes in k-Group](https://leetcode.com/problems/reverse-nodes-in-k-group/)**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) / [LC](https://leetcode.com/problems/reverse-nodes-in-k-group/) | Before reversing, verify that the next k nodes exist. Reverse exactly that closed group, reconnect previous group tail to the new head, and connect the reversed tail to the next group. Leave a final short group unchanged. | confirm k nodes before reversing |
| **60. [Odd Even Linked List](https://leetcode.com/problems/odd-even-linked-list/)**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) / [LC](https://leetcode.com/problems/odd-even-linked-list/) | Every pointer has an ownership role: previous fixed node, current node being moved, and saved next node. Save next before rewiring, reconnect all boundary nodes, and return the real head after possible head changes. Node identity matters more than node value. | lost next pointer or wrong returned head |
| **61. [Rotate List](https://leetcode.com/problems/rotate-list/)**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) / [LC](https://leetcode.com/problems/rotate-list/) | Every pointer has an ownership role: previous fixed node, current node being moved, and saved next node. Save next before rewiring, reconnect all boundary nodes, and return the real head after possible head changes. Node identity matters more than node value. | lost next pointer or wrong returned head |
| **62. [Swap Nodes In Pairs](https://leetcode.com/problems/swap-nodes-in-pairs/)**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) / [LC](https://leetcode.com/problems/swap-nodes-in-pairs/) | Every pointer has an ownership role: previous fixed node, current node being moved, and saved next node. Save next before rewiring, reconnect all boundary nodes, and return the real head after possible head changes. Node identity matters more than node value. | lost next pointer or wrong returned head |
| **176. [Reverse Linked List II](https://leetcode.com/problems/reverse-linked-list-ii/)**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) / [LC](https://leetcode.com/problems/reverse-linked-list-ii/) | Every pointer has an ownership role: previous fixed node, current node being moved, and saved next node. Save next before rewiring, reconnect all boundary nodes, and return the real head after possible head changes. Node identity matters more than node value. | lost next pointer or wrong returned head |

## Two Pointers

### Two pointers

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **10. Valid Palindrome**<br>[Java](../../src/main/java/org/chijai/day3/session3/ValidPalindrome.java) | left and right move inward over the original string, skipping only non-alphanumeric characters. Compare normalized characters after both skips. A mismatch returns false; crossing pointers means every required pair matched. | move only the provably discardable side |
| **13. Container With Most Water**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session2/ContainerWithMostWater.java) | Area is width times the shorter wall, so the shorter side is the limiting boundary. Moving the taller side cannot improve the old limiting height with smaller width. Move the shorter side and keep the best area seen. | move only the provably discardable side |

### Two pointers / hash

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **12. [Two Sum II - Input Array Is Sorted](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session2/Three3Sum2Sum.java) / [LC](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/) | left and right bound the remaining sorted search space. If sum is too small, only moving left rightward can increase it; if sum is too large, only moving right leftward can decrease it. Return the required one-based indices when the sum equals target. | move only the provably discardable side |

### Two pointers / stack

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **14. [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/)**<br>[Java](../../src/main/java/org/chijai/day3/session2/prefix/suffix/TrappingRainwater.java) / [LC](https://leetcode.com/problems/trapping-rain-water/) | leftMax and rightMax are the best walls already seen from each side. The side with smaller max determines trapped water there because the opposite side is already high enough. Move that side inward and add max - height only after updating the side max. | update side max before adding trapped water |

### Partition / Dutch flag

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **59. [Sort Colors](https://leetcode.com/problems/sort-colors/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session1/SortColors.java) / [LC](https://leetcode.com/problems/sort-colors/) | Maintain three regions: [0, low) are 0s, [low, mid) are 1s, and (high, end] are 2s. A 0 swaps to low and advances both low and mid; a 2 swaps to high and only high moves because the incoming value is unclassified. A 1 advances mid. | after swapping 2, do not advance mid |

### Expand around center

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **108. [Longest Palindromic Substring](https://leetcode.com/problems/longest-palindromic-substring/)**<br>[Java](../../src/main/java/org/chijai/day3/session3/LongestPalindromicSubstring.java) / [LC](https://leetcode.com/problems/longest-palindromic-substring/) | The two pointers bound the remaining candidate space. Each move must be justified by order, symmetry, or a one-sided bottleneck so that discarded candidates cannot become answers. Save the answer before moving a pointer when the current state is valid. | move only the provably discardable side |

## Heap / Priority Queue

### Heap / divide and conquer

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **11. [Merge K Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/)**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session4/MergeKSortedLists.java) / [LC](https://leetcode.com/problems/merge-k-sorted-lists/) | The heap stores one current head per non-empty list, so the heap root is the globally smallest available node. Poll that node, append it to tail, then push its next node because that next node just became the list's candidate. Do not scan all k heads on every append. | push polled.next, not every node upfront |

### Frequency + heap/bucket

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **36. [Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/)**<br>[Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) / [LC](https://leetcode.com/problems/top-k-frequent-elements/) | First count exact frequencies, then select by frequency rather than by value. A size-k min-heap keeps the k strongest candidates by evicting the current weakest. If using buckets, bucket index is frequency. | stale root or comparator reversed |
| **139. [Top K Frequent Words](https://leetcode.com/problems/top-k-frequent-words/)**<br>[Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) / [LC](https://leetcode.com/problems/top-k-frequent-words/) | The heap contains only candidates still eligible for the current priority question, or it uses lazy deletion until stale candidates reach the root. Comparator order must match the requested best item. Poll only when size, expiry, or frontier rules say the root is no longer allowed. | stale root or comparator reversed |
| **140. [H-Index](https://leetcode.com/problems/h-index/)**<br>[Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) / [LC](https://leetcode.com/problems/h-index/) | The heap contains only candidates still eligible for the current priority question, or it uses lazy deletion until stale candidates reach the root. Comparator order must match the requested best item. Poll only when size, expiry, or frontier rules say the root is no longer allowed. | stale root or comparator reversed |
| **141. [Sort Characters By Frequency](https://leetcode.com/problems/sort-characters-by-frequency/)**<br>[Java](../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) / [LC](https://leetcode.com/problems/sort-characters-by-frequency/) | The heap contains only candidates still eligible for the current priority question, or it uses lazy deletion until stale candidates reach the root. Comparator order must match the requested best item. Poll only when size, expiry, or frontier rules say the root is no longer allowed. | stale root or comparator reversed |

### Intervals / heap

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **38. [Meeting Rooms II](https://leetcode.com/problems/meeting-rooms-ii/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalActiveMinHeap.java) / [LC](https://leetcode.com/problems/meeting-rooms-ii/) | Sort meetings by start time and keep active meeting end times in a min-heap. If the earliest end is <= current start, that room is reusable before adding the current meeting. Heap size after adding is active rooms; maximum size is rooms needed. | reuse when earliestEnd <= start |

### Two heaps

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **49. [Find Median From Data Stream](https://leetcode.com/problems/find-median-from-data-stream/)**<br>[Java](../../src/main/java/org/chijai/day7/session1/heap/Median.java) / [LC](https://leetcode.com/problems/find-median-from-data-stream/) | The max-heap owns the lower half and the min-heap owns the upper half. Keep sizes balanced so they differ by at most one and every lower value is <= every upper value. Median is one heap top or the average of both tops. | rebalance heaps after each insert |

### Greedy / heap

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **98. [Task Scheduler](https://leetcode.com/problems/task-scheduler/)**<br>[Java](../../src/main/java/org/chijai/day7/session1/heap/TaskScheduler.java) / [LC](https://leetcode.com/problems/task-scheduler/) | The heap contains only candidates still eligible for the current priority question, or it uses lazy deletion until stale candidates reach the root. Comparator order must match the requested best item. Poll only when size, expiry, or frontier rules say the root is no longer allowed. | stale root or comparator reversed |

### Min-heap size K

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **99. [Kth Largest Element In An Array](https://leetcode.com/problems/kth-largest-element-in-an-array/)**<br>[Java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) / [LC](https://leetcode.com/problems/kth-largest-element-in-an-array/) | The heap contains only candidates still eligible for the current priority question, or it uses lazy deletion until stale candidates reach the root. Comparator order must match the requested best item. Poll only when size, expiry, or frontier rules say the root is no longer allowed. | stale root or comparator reversed |
| **100. [Kth Largest Element In A Stream](https://leetcode.com/problems/kth-largest-element-in-a-stream/)**<br>[Java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) / [LC](https://leetcode.com/problems/kth-largest-element-in-a-stream/) | The heap contains only candidates still eligible for the current priority question, or it uses lazy deletion until stale candidates reach the root. Comparator order must match the requested best item. Poll only when size, expiry, or frontier rules say the root is no longer allowed. | stale root or comparator reversed |

### Heap / quickselect

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **138. [K Closest Points To Origin](https://leetcode.com/problems/k-closest-points-to-origin/)**<br>[Java](../../src/main/java/org/chijai/day7/session1/heap/KClosestPointsToOrigin.java) / [LC](https://leetcode.com/problems/k-closest-points-to-origin/) | The heap contains only candidates still eligible for the current priority question, or it uses lazy deletion until stale candidates reach the root. Comparator order must match the requested best item. Poll only when size, expiry, or frontier rules say the root is no longer allowed. | stale root or comparator reversed |

### Heap / ranking

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **174. Award Top K Hotels**<br>[Java](../../src/main/java/org/chijai/day7/session1/heap/AwardTopKHotels.java) | The heap contains only candidates still eligible for the current priority question, or it uses lazy deletion until stale candidates reach the root. Comparator order must match the requested best item. Poll only when size, expiry, or frontier rules say the root is no longer allowed. | stale root or comparator reversed |

## Tree BFS / Level Order

### Tree traversal

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **15. [Binary Tree Level Order Traversal](https://leetcode.com/problems/binary-tree-level-order-traversal/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeTraversal.java) / [LC](https://leetcode.com/problems/binary-tree-level-order-traversal/) | At the start of each outer loop, queue.size() is exactly the number of nodes in the current level. Process exactly that many nodes, appending their children for the next level. Never let newly enqueued children leak into the current level. | capture queue size before pushing children |

### Tree BFS / DFS

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **58. [Binary Tree Right Side View](https://leetcode.com/problems/binary-tree-right-side-view/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeSideView.java) / [LC](https://leetcode.com/problems/binary-tree-right-side-view/) | The queue frontier defines the current level. Capture the level size before adding children so newly discovered nodes belong to the next level. Any per-level answer must be finalized after exactly that size is processed. | children leak into current level |

## Tree DFS / Recursion

### Tree DFS / stack

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **16. [Validate Binary Search Tree](https://leetcode.com/problems/validate-binary-search-tree/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) / [LC](https://leetcode.com/problems/validate-binary-search-tree/) | Each node must be strictly greater than its inherited lower bound and strictly less than its inherited upper bound. Left children tighten the upper bound; right children tighten the lower bound. Parent-child checks alone miss ancestor violations. | strict bounds; ancestor violations matter |
| **63. [Binary Tree Inorder Traversal](https://leetcode.com/problems/binary-tree-inorder-traversal/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) / [LC](https://leetcode.com/problems/binary-tree-inorder-traversal/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **112. [Binary Tree Postorder Traversal](https://leetcode.com/problems/binary-tree-postorder-traversal/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) / [LC](https://leetcode.com/problems/binary-tree-postorder-traversal/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **113. [Binary Tree Preorder Traversal](https://leetcode.com/problems/binary-tree-preorder-traversal/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/BinaryTreeInorderTraversal.java) / [LC](https://leetcode.com/problems/binary-tree-preorder-traversal/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |

### Tree DFS return contract

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **17. [Lowest Common Ancestor Of A Binary Tree](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) / [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/) | The helper returns a found target or an LCA from that subtree. If both left and right return non-null, current is the split point. If only one side returns non-null, pass that result upward. | mixing helper return with global answer |
| **178. [Lowest Common Ancestor Of A Binary Tree II](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-ii/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) / [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-ii/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **179. [Lowest Common Ancestor Of A Binary Tree III](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) / [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **180. [Lowest Common Ancestor Of A Binary Tree IV](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iv/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) / [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iv/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |

### BST inorder

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **27. [Kth Smallest Element in a BST](https://leetcode.com/problems/kth-smallest-element-in-a-bst/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session3/KthSmallestElementInBST.java) / [LC](https://leetcode.com/problems/kth-smallest-element-in-a-bst/) | BST inorder traversal visits values in ascending order. Decrement k exactly when visiting the node itself, after left subtree and before right subtree. The kth visit is the answer. | mixing helper return with global answer |
| **120. [Recover Binary Search Tree](https://leetcode.com/problems/recover-binary-search-tree/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) / [LC](https://leetcode.com/problems/recover-binary-search-tree/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **121. [Binary Search Tree Iterator](https://leetcode.com/problems/binary-search-tree-iterator/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) / [LC](https://leetcode.com/problems/binary-search-tree-iterator/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **122. [Convert BST To Greater Tree](https://leetcode.com/problems/convert-bst-to-greater-tree/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) / [LC](https://leetcode.com/problems/convert-bst-to-greater-tree/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |

### Core tree patterns

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **28. [Diameter of Binary Tree](https://leetcode.com/problems/diameter-of-binary-tree/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session3/BinaryTree.java) / [LC](https://leetcode.com/problems/diameter-of-binary-tree/) | The helper returns height upward, but the global answer is the best leftHeight + rightHeight seen at any node. Update diameter before returning 1 + max(leftHeight, rightHeight). Do not return diameter as height. | return height, update diameter separately |

### Tree path DFS / global answer

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **29. [Path Sum III](https://leetcode.com/problems/path-sum-iii/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) / [LC](https://leetcode.com/problems/path-sum-iii/) | prefixSum counts belong only to the current root-to-node path. For current sum s, paths ending here with targetSum equal the number of earlier prefixes s - targetSum. Add current prefix before going down, then decrement it when backtracking. | decrement prefix count on backtrack |
| **66. [Sum Root To Leaf Numbers](https://leetcode.com/problems/sum-root-to-leaf-numbers/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) / [LC](https://leetcode.com/problems/sum-root-to-leaf-numbers/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **97. [Binary Tree Maximum Path Sum](https://leetcode.com/problems/binary-tree-maximum-path-sum/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) / [LC](https://leetcode.com/problems/binary-tree-maximum-path-sum/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **111. [Path Sum](https://leetcode.com/problems/path-sum/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) / [LC](https://leetcode.com/problems/path-sum/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **177. [Path Sum II](https://leetcode.com/problems/path-sum-ii/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) / [LC](https://leetcode.com/problems/path-sum-ii/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |

### BST property

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **57. [Lowest Common Ancestor Of A Binary Search Tree](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) / [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/) | Use BST order to walk toward the split. If both targets are smaller than current, go left; if both are larger, go right. Otherwise current is where the two search paths diverge or one target equals current. | mixing helper return with global answer |
| **114. [Insert Into A Binary Search Tree](https://leetcode.com/problems/insert-into-a-binary-search-tree/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) / [LC](https://leetcode.com/problems/insert-into-a-binary-search-tree/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **115. [Minimum Absolute Difference In BST](https://leetcode.com/problems/minimum-absolute-difference-in-bst/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) / [LC](https://leetcode.com/problems/minimum-absolute-difference-in-bst/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **116. [Range Sum Of BST](https://leetcode.com/problems/range-sum-of-bst/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) / [LC](https://leetcode.com/problems/range-sum-of-bst/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **117. [Search In A Binary Search Tree](https://leetcode.com/problems/search-in-a-binary-search-tree/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) / [LC](https://leetcode.com/problems/search-in-a-binary-search-tree/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |

### Tree DFS/BFS

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **64. [Invert Binary Tree](https://leetcode.com/problems/invert-binary-tree/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session3/InvertBinaryTree.java) / [LC](https://leetcode.com/problems/invert-binary-tree/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |

### Tree recursion / hashmap index

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **65. [Construct Binary Search Tree From Preorder Traversal](https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) / [LC](https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **83. [Verify Preorder Serialization Of A Binary Tree](https://leetcode.com/problems/verify-preorder-serialization-of-a-binary-tree/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) / [LC](https://leetcode.com/problems/verify-preorder-serialization-of-a-binary-tree/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **94. [Construct Binary Tree From Inorder And Postorder Traversal](https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) / [LC](https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **96. [Construct Binary Tree From Preorder And Inorder Traversal](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session2/ConstructTree.java) / [LC](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |

### Tree BFS/DFS serialization

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **73. [Serialize And Deserialize Binary Tree](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session2/SerializeAndDeserializeBinaryTree.java) / [LC](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |

### Tree + graph BFS

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **118. [All Nodes Distance K in Binary Tree](https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java) / [LC](https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |
| **119. [Amount of Time for Binary Tree to Be Infected](https://leetcode.com/problems/amount-of-time-for-binary-tree-to-be-infected/)**<br>[Java](../../src/main/java/org/chijai/day6/trees/session2/BurnBinaryTree.java) / [LC](https://leetcode.com/problems/amount-of-time-for-binary-tree-to-be-infected/) | Define what the helper returns to its parent and what global answer it may update separately. Null/leaf base cases must match that return contract. Combine left and right results once per node without recomputing subtrees. | mixing helper return with global answer |

## Graph DFS / Components

### Matrix DFS/BFS components

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **18. [Number Of Islands](https://leetcode.com/problems/number-of-islands/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) / [LC](https://leetcode.com/problems/number-of-islands/) | A new island is counted only when an unvisited land cell is first found. DFS/BFS then owns and marks that entire 4-directional land component. Water and already visited land contribute nothing. | mark visited before exploring neighbors |
| **68. [Pacific Atlantic Water Flow](https://leetcode.com/problems/pacific-atlantic-water-flow/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) / [LC](https://leetcode.com/problems/pacific-atlantic-water-flow/) | Reverse the problem: start from each ocean border and move to neighbors with height >= current height. A cell can reach an ocean if the reversed search can reach the cell from that ocean. The answer is cells marked by both ocean searches. | reverse flow: move to >= height |
| **69. [Surrounded Regions](https://leetcode.com/problems/surrounded-regions/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) / [LC](https://leetcode.com/problems/surrounded-regions/) | Only O cells connected to the border are safe. Mark all border-connected O cells first, then flip every remaining O to X and restore safe marks to O. Do not flip before proving border reachability. | mark border-safe O before flipping |
| **124. [Number Of Closed Islands](https://leetcode.com/problems/number-of-closed-islands/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) / [LC](https://leetcode.com/problems/number-of-closed-islands/) | Visited state means this node/cell is already owned by the current traversal or component. Mark before exploring neighbors, and restore only when the problem is path backtracking rather than component ownership. Direction, parent, and boundary checks decide cycle behavior. | mark/restore semantics confused |
| **125. [Max Area Of Island](https://leetcode.com/problems/max-area-of-island/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) / [LC](https://leetcode.com/problems/max-area-of-island/) | Visited state means this node/cell is already owned by the current traversal or component. Mark before exploring neighbors, and restore only when the problem is path backtracking rather than component ownership. Direction, parent, and boundary checks decide cycle behavior. | mark/restore semantics confused |

### Matrix DFS/BFS

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **40. [Flood Fill](https://leetcode.com/problems/flood-fill/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session1/FloodFill.java) / [LC](https://leetcode.com/problems/flood-fill/) | Only cells connected to the start and equal to the original color can change. If newColor equals original color, return early to avoid revisiting forever. Mark/recolor before recursing to prevent cycles. | return early when newColor == original |

### BFS/DFS coloring

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **41. [Is Graph Bipartite?](https://leetcode.com/problems/is-graph-bipartite/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) / [LC](https://leetcode.com/problems/is-graph-bipartite/) | color[node] records which side of the partition owns the node. Each edge must connect opposite colors; an uncolored neighbor receives the opposite color. A same-color edge is an immediate contradiction. | visited is not enough; check colors |
| **126. [Graph Valid Tree](https://leetcode.com/problems/graph-valid-tree/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) / [LC](https://leetcode.com/problems/graph-valid-tree/) | Visited state means this node/cell is already owned by the current traversal or component. Mark before exploring neighbors, and restore only when the problem is path backtracking rather than component ownership. Direction, parent, and boundary checks decide cycle behavior. | mark/restore semantics confused |
| **127. [Possible Bipartition](https://leetcode.com/problems/possible-bipartition/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) / [LC](https://leetcode.com/problems/possible-bipartition/) | Visited state means this node/cell is already owned by the current traversal or component. Mark before exploring neighbors, and restore only when the problem is path backtracking rather than component ownership. Direction, parent, and boundary checks decide cycle behavior. | mark/restore semantics confused |
| **128. [Redundant Connection](https://leetcode.com/problems/redundant-connection/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/GraphBipartite.java) / [LC](https://leetcode.com/problems/redundant-connection/) | Visited state means this node/cell is already owned by the current traversal or component. Mark before exploring neighbors, and restore only when the problem is path backtracking rather than component ownership. Direction, parent, and boundary checks decide cycle behavior. | mark/restore semantics confused |

### Graph DFS/BFS clone

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **77. [Clone Graph](https://leetcode.com/problems/clone-graph/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/CloneGraph.java) / [LC](https://leetcode.com/problems/clone-graph/) | Visited state means this node/cell is already owned by the current traversal or component. Mark before exploring neighbors, and restore only when the problem is path backtracking rather than component ownership. Direction, parent, and boundary checks decide cycle behavior. | mark/restore semantics confused |

### Matrix DFS

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **129. [Coloring A Border](https://leetcode.com/problems/coloring-a-border/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session1/ColoringABorder.java) / [LC](https://leetcode.com/problems/coloring-a-border/) | Visited state means this node/cell is already owned by the current traversal or component. Mark before exploring neighbors, and restore only when the problem is path backtracking rather than component ownership. Direction, parent, and boundary checks decide cycle behavior. | mark/restore semantics confused |

## Topological Sort

### Topological sort / cycle

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **19. [Course Schedule](https://leetcode.com/problems/course-schedule/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/course-schedule/) | indegree is the number of prerequisites still unmet for a course. Only zero-indegree courses can enter the queue, and processing one course decrements its dependents. If processed count is less than n, a cycle kept some courses locked. | processed count detects cycle |
| **20. [Course Schedule II](https://leetcode.com/problems/course-schedule-ii/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/course-schedule-ii/) | A course is appended to the answer only when its indegree has dropped to zero. Processing that course consumes it as a prerequisite and unlocks dependents by decrementing indegree. If the final order length is not n, return empty because a cycle remains. | return empty if order length < n |
| **148. [Parallel Courses](https://leetcode.com/problems/parallel-courses/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/parallel-courses/) | indegree is the count of prerequisites still blocking a node. Only zero-indegree nodes can be processed, and processing a node consumes its outgoing prerequisite relation by decrementing neighbors. A leftover node means a dependency cycle. | decrement wrong indegree edge |
| **149. [Alien Dictionary](https://leetcode.com/problems/alien-dictionary/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/alien-dictionary/) | indegree is the count of prerequisites still blocking a node. Only zero-indegree nodes can be processed, and processing a node consumes its outgoing prerequisite relation by decrementing neighbors. A leftover node means a dependency cycle. | decrement wrong indegree edge |
| **150. [Find Eventual Safe States](https://leetcode.com/problems/find-eventual-safe-states/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/find-eventual-safe-states/) | indegree is the count of prerequisites still blocking a node. Only zero-indegree nodes can be processed, and processing a node consumes its outgoing prerequisite relation by decrementing neighbors. A leftover node means a dependency cycle. | decrement wrong indegree edge |
| **151. [Sequence Reconstruction](https://leetcode.com/problems/sequence-reconstruction/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/sequence-reconstruction/) | indegree is the count of prerequisites still blocking a node. Only zero-indegree nodes can be processed, and processing a node consumes its outgoing prerequisite relation by decrementing neighbors. A leftover node means a dependency cycle. | decrement wrong indegree edge |
| **152. [Sort Items by Groups Respecting Dependencies](https://leetcode.com/problems/sort-items-by-groups-respecting-dependencies/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/sort-items-by-groups-respecting-dependencies/) | indegree is the count of prerequisites still blocking a node. Only zero-indegree nodes can be processed, and processing a node consumes its outgoing prerequisite relation by decrementing neighbors. A leftover node means a dependency cycle. | decrement wrong indegree edge |
| **182. [Course Schedule IV](https://leetcode.com/problems/course-schedule-iv/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/course-schedule-iv/) | indegree is the count of prerequisites still blocking a node. Only zero-indegree nodes can be processed, and processing a node consumes its outgoing prerequisite relation by decrementing neighbors. A leftover node means a dependency cycle. | decrement wrong indegree edge |

### Topological trimming

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **71. [Minimum Height Trees](https://leetcode.com/problems/minimum-height-trees/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session3/MinHTree.java) / [LC](https://leetcode.com/problems/minimum-height-trees/) | indegree is the count of prerequisites still blocking a node. Only zero-indegree nodes can be processed, and processing a node consumes its outgoing prerequisite relation by decrementing neighbors. A leftover node means a dependency cycle. | decrement wrong indegree edge |

## Graph BFS / Shortest Path

### BFS shortest path

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **21. [Word Ladder](https://leetcode.com/problems/word-ladder/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session3/WordLadder.java) / [LC](https://leetcode.com/problems/word-ladder/) | Each queued word has a distance from beginWord, and all one-letter transformations cost one step. Mark a word visited when enqueueing so it cannot be reached again at the same or greater distance. The first time endWord is generated or dequeued is the shortest length. | mark visited when enqueueing |

### Multi-source BFS

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **30. [Rotting Oranges](https://leetcode.com/problems/rotting-oranges/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session1/RottenOranges.java) / [LC](https://leetcode.com/problems/rotting-oranges/) | All initially rotten oranges are minute 0 BFS sources. One BFS layer is one minute, so minutes increments after processing the whole layer, not per orange. Mark a fresh orange rotten when enqueueing it and return -1 if fresh remains unreachable. | minutes per layer, not per cell |
| **31. [01 Matrix](https://leetcode.com/problems/01-matrix/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session1/Matrix01.java) / [LC](https://leetcode.com/problems/01-matrix/) | All zero cells start in the queue with distance 0. A one cell gets its nearest-zero distance the first time BFS reaches it. Mark distance when enqueueing to avoid duplicate visits. | multi-source from zeros, not BFS from each one |

### Dijkstra / graph

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **67. [Network Delay Time](https://leetcode.com/problems/network-delay-time/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session2/NetworkDelayTime.java) / [LC](https://leetcode.com/problems/network-delay-time/) | dist[node] is the best known time from the source. The min-heap always expands the currently smallest candidate distance; ignore stale heap entries larger than dist[node]. Weighted edges require Dijkstra, not plain BFS. | ignore stale heap distances |

### Matrix DFS/BFS components

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **75. [Number Of Provinces](https://leetcode.com/problems/number-of-provinces/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java) / [LC](https://leetcode.com/problems/number-of-provinces/) | Queue entries are states reached in nondecreasing number of steps. Mark visited when enqueueing so duplicate paths do not re-enter the frontier. The first time a state is reached is optimal only when each edge has equal cost. | visited too late; duplicate enqueue |

### BFS + sorting

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **123. [K Highest Ranked Items Within A Price Range](https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session3/KHighestRankedItemsWithinAPriceRange.java) / [LC](https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/) | Queue entries are states reached in nondecreasing number of steps. Mark visited when enqueueing so duplicate paths do not re-enter the frontier. The first time a state is reached is optimal only when each edge has equal cost. | visited too late; duplicate enqueue |

## Dynamic Programming

### 1D DP

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **32. [House Robber](https://leetcode.com/problems/house-robber/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session1/HouseRobber.java) / [LC](https://leetcode.com/problems/house-robber/) | At each house, the only relevant history is best if I skip this house versus rob it after the best before previous. The transition is max(previous best, bestBeforePrevious + current). Adjacent houses cannot both be chosen. | state meaning or iteration order wrong |
| **168. Climbing Stairs Fib**<br>[Java](../../src/main/java/org/chijai/day9/dp/session1/ClimbingStairsFib.java) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |

### Unbounded knapsack DP

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **33. [Coin Change](https://leetcode.com/problems/coin-change/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) / [LC](https://leetcode.com/problems/coin-change/) | dp[a] is the fewest coins needed to form amount a, with dp[0] = 0. For each amount, try every coin that can precede it and relax dp[a] from dp[a - coin] + 1. Unreachable states must stay as INF, not accidentally overflow into valid answers. | keep INF unreachable states safe |
| **195. [Word Break](https://leetcode.com/problems/word-break/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) / [LC](https://leetcode.com/problems/word-break/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |
| **201. [Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) / [LC](https://leetcode.com/problems/climbing-stairs/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |
| **202. [Min Cost Climbing Stairs](https://leetcode.com/problems/min-cost-climbing-stairs/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) / [LC](https://leetcode.com/problems/min-cost-climbing-stairs/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |
| **203. [Perfect Squares](https://leetcode.com/problems/perfect-squares/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) / [LC](https://leetcode.com/problems/perfect-squares/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |

### Grid DP

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **43. [Unique Paths](https://leetcode.com/problems/unique-paths/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session1/UniquePaths.java) / [LC](https://leetcode.com/problems/unique-paths/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |

### 0/1 knapsack DP

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **44. Partition Equal Subset Sum**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/PartitionEqualSubsetSum.java) | Equal partition means one subset must make total / 2, so odd total is impossible. dp[s] means processed numbers can form sum s, seeded by dp[0] = true. Iterate sums right to left so the current number cannot reuse a state it just created. | right-to-left DP; dp[0] seed |

### DP / patience sorting

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **45. [Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) / [LC](https://leetcode.com/problems/longest-increasing-subsequence/) | tails[len] is the smallest possible tail value for an increasing subsequence of length len + 1. Binary search the first tail >= x and replace it with x; this improves future options without changing known length incorrectly. Equal values replace, not extend, for strictly increasing subsequences. | first tail >= x; equal replaces |
| **204. [Number of Longest Increasing Subsequence](https://leetcode.com/problems/number-of-longest-increasing-subsequence/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) / [LC](https://leetcode.com/problems/number-of-longest-increasing-subsequence/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |
| **205. [Russian Doll Envelopes](https://leetcode.com/problems/russian-doll-envelopes/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) / [LC](https://leetcode.com/problems/russian-doll-envelopes/) | Sort width ascending, but equal width descending by height so equal-width envelopes cannot chain through LIS. Then run strict LIS on heights. Without descending tie-break, equal widths can be illegally nested. | equal width sorted by height desc |

### DP + binary search

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **87. [Maximum Profit In Job Scheduling](https://leetcode.com/problems/maximum-profit-in-job-scheduling/)**<br>[Java](../../src/main/java/org/chijai/day2/session3/MaximumProfitInJobScheduling.java) / [LC](https://leetcode.com/problems/maximum-profit-in-job-scheduling/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |

### Kadane / DP

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **88. Kadane Max Sub Array**<br>[Java](../../src/main/java/org/chijai/day9/dp/session1/KadaneMaxSubArray.java) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |

### 2D DP

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **169. [Edit Distance](https://leetcode.com/problems/edit-distance/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/edit-distance/) | dp[i][j] is the minimum edits to convert the first i chars of word1 to the first j chars of word2. Equal last chars inherit dp[i - 1][j - 1]; otherwise choose one plus insert, delete, or replace. Empty-prefix rows and columns are the base cases. | base rows/cols are prefix lengths |
| **170. [Distinct Subsequences](https://leetcode.com/problems/distinct-subsequences/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/distinct-subsequences/) | dp[i][j] counts ways the first i chars of s form the first j chars of t. Skipping s[i - 1] always contributes dp[i - 1][j]; if chars match, taking it also contributes dp[i - 1][j - 1]. dp[*][0] is 1 because empty t can always be formed by taking nothing. | dp[*][0] = 1 |
| **196. [Interleaving String](https://leetcode.com/problems/interleaving-string/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/interleaving-string/) | dp[i][j] means s3 prefix length i + j can be formed from s1 first i chars and s2 first j chars. The next char must come from s1[i - 1] or s2[j - 1] and match s3[i + j - 1]. Greedy fails when both strings offer the same character. | s3 index is i + j - 1 |
| **197. [Longest Common Subsequence](https://leetcode.com/problems/longest-common-subsequence/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/longest-common-subsequence/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |
| **198. [Delete Operation for Two Strings](https://leetcode.com/problems/delete-operation-for-two-strings/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/delete-operation-for-two-strings/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |
| **199. [Longest Palindromic Subsequence](https://leetcode.com/problems/longest-palindromic-subsequence/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/longest-palindromic-subsequence/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |
| **200. [Minimum ASCII Delete Sum for Two Strings](https://leetcode.com/problems/minimum-ascii-delete-sum-for-two-strings/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/minimum-ascii-delete-sum-for-two-strings/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |

### Stock DP variants

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **190. [Best Time to Buy and Sell Stock with Transaction Fee](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) / [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |
| **191. [Best Time to Buy and Sell Stock with Cooldown](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) / [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |
| **193. [Best Time to Buy and Sell Stock IV](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) / [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |

### Greedy / DP states

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **192. [Best Time to Buy and Sell Stock III](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java) / [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |

### Contribution counting

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **194. [Distinct Subsequences II](https://leetcode.com/problems/distinct-subsequences-ii/)**<br>[Java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java) / [LC](https://leetcode.com/problems/distinct-subsequences-ii/) | State the exact meaning of dp before the recurrence. Each transition must read only states that are already valid under the chosen iteration order. Base cases are not initialization trivia; they are the smallest true meanings that allow every later state to derive correctly. | state meaning or iteration order wrong |

## Backtracking / Combinatorial DFS

### Backtracking subsets

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **34. [Subsets](https://leetcode.com/problems/subsets/)**<br>[Java](../../src/main/java/org/chijai/day11/backtracking/session1/Subsets.java) / [LC](https://leetcode.com/problems/subsets/) | The path is the current partial decision and visited/used state says what cannot be reused on this path. Choose one candidate, mutate state, recurse, then restore state before trying the next candidate. Duplicate skipping must depend on sorted order and same-depth ownership. | forgot undo or duplicate skip condition |

### Backtracking reuse

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **46. [Combination Sum](https://leetcode.com/problems/combination-sum/)**<br>[Java](../../src/main/java/org/chijai/day11/backtracking/session1/CombinationSum.java) / [LC](https://leetcode.com/problems/combination-sum/) | The path is the current partial decision and visited/used state says what cannot be reused on this path. Choose one candidate, mutate state, recurse, then restore state before trying the next candidate. Duplicate skipping must depend on sorted order and same-depth ownership. | forgot undo or duplicate skip condition |

### DFS backtracking

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **48. [Word Search](https://leetcode.com/problems/word-search/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session1/WordSearch.java) / [LC](https://leetcode.com/problems/word-search/) | The path owns board cells temporarily while matching one word index. Mark the current cell before exploring neighbors, then restore it after recursion returns. A cell cannot be reused in the same path. | restore visited cell after recursion |

### Backtracking / mapping

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **146. [Letter Combinations Of A Phone Number](https://leetcode.com/problems/letter-combinations-of-a-phone-number/)**<br>[Java](../../src/main/java/org/chijai/day11/backtracking/session1/LetterCombinationsOfAPhoneNumber.java) / [LC](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) | The path is the current partial decision and visited/used state says what cannot be reused on this path. Choose one candidate, mutate state, recurse, then restore state before trying the next candidate. Duplicate skipping must depend on sorted order and same-depth ownership. | forgot undo or duplicate skip condition |

### Backtracking permutations

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **147. [Permutations](https://leetcode.com/problems/permutations/)**<br>[Java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) / [LC](https://leetcode.com/problems/permutations/) | The path is the current partial decision and visited/used state says what cannot be reused on this path. Choose one candidate, mutate state, recurse, then restore state before trying the next candidate. Duplicate skipping must depend on sorted order and same-depth ownership. | forgot undo or duplicate skip condition |
| **181. [Permutations II](https://leetcode.com/problems/permutations-ii/)**<br>[Java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) / [LC](https://leetcode.com/problems/permutations-ii/) | The path is the current partial decision and visited/used state says what cannot be reused on this path. Choose one candidate, mutate state, recurse, then restore state before trying the next candidate. Duplicate skipping must depend on sorted order and same-depth ownership. | forgot undo or duplicate skip condition |

## Stack / Monotonic Stack

### Stack

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **35. [Valid Parentheses](https://leetcode.com/problems/valid-parentheses/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session3/ValidParentheses.java) / [LC](https://leetcode.com/problems/valid-parentheses/) | The stack contains unmatched opening brackets in nesting order. A closing bracket must match and consume the most recent opening bracket. At the end the stack must be empty. | closing must match most recent opening |
| **104. [Evaluate Reverse Polish Notation](https://leetcode.com/problems/evaluate-reverse-polish-notation/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session3/EvalRPN.java) / [LC](https://leetcode.com/problems/evaluate-reverse-polish-notation/) | The stack stores unresolved items whose answer depends on a future closer/warmer/smaller/larger/current token. While the current item resolves the stack top, pop and finalize that old item. Push current only if it remains unresolved. | pop condition or unresolved ownership wrong |

### Monotonic stack

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **37. [Daily Temperatures](https://leetcode.com/problems/daily-temperatures/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/DailyTemperatures.java) / [LC](https://leetcode.com/problems/daily-temperatures/) | The stack stores indices whose next warmer day is unresolved, with temperatures decreasing from bottom to top. Current temperature resolves all colder stack-top days, and answer[old] is currentIndex - old. Push current only after resolving. | store indices; answer is i - oldIndex |
| **103. [Next Greater Element II](https://leetcode.com/problems/next-greater-element-ii/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/NextGreaterElement.java) / [LC](https://leetcode.com/problems/next-greater-element-ii/) | The stack stores unresolved items whose answer depends on a future closer/warmer/smaller/larger/current token. While the current item resolves the stack top, pop and finalize that old item. Push current only if it remains unresolved. | pop condition or unresolved ownership wrong |
| **131. Largest Rectangle**<br>[Java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java) | The stack stores unresolved items whose answer depends on a future closer/warmer/smaller/larger/current token. While the current item resolves the stack top, pop and finalize that old item. Push current only if it remains unresolved. | pop condition or unresolved ownership wrong |

### Stack/queue design

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **93. [Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) / [LC](https://leetcode.com/problems/sliding-window-maximum/) | The deque stores indices inside the current window, and their values decrease from front to back. Expire front indices with index <= right - k, remove dominated back indices while nums[back] <= nums[right], then add right. Emit only when right >= k - 1, writing to right - k + 1. | expire front <= right-k; emit at right >= k-1 |
| **134. [Implement Queue Using Stacks](https://leetcode.com/problems/implement-queue-using-stacks/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) / [LC](https://leetcode.com/problems/implement-queue-using-stacks/) | The stack stores unresolved items whose answer depends on a future closer/warmer/smaller/larger/current token. While the current item resolves the stack top, pop and finalize that old item. Push current only if it remains unresolved. | pop condition or unresolved ownership wrong |
| **135. [Implement Stack Using Queues](https://leetcode.com/problems/implement-stack-using-queues/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) / [LC](https://leetcode.com/problems/implement-stack-using-queues/) | The stack stores unresolved items whose answer depends on a future closer/warmer/smaller/larger/current token. While the current item resolves the stack top, pop and finalize that old item. Push current only if it remains unresolved. | pop condition or unresolved ownership wrong |

### Stack / expression parsing

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **106. [Basic Calculator](https://leetcode.com/problems/basic-calculator/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session3/BasicCalculator.java) / [LC](https://leetcode.com/problems/basic-calculator/) | The stack stores unresolved items whose answer depends on a future closer/warmer/smaller/larger/current token. While the current item resolves the stack top, pop and finalize that old item. Push current only if it remains unresolved. | pop condition or unresolved ownership wrong |

### Stack design

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **132. [Min Stack](https://leetcode.com/problems/min-stack/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) / [LC](https://leetcode.com/problems/min-stack/) | The stack stores unresolved items whose answer depends on a future closer/warmer/smaller/larger/current token. While the current item resolves the stack top, pop and finalize that old item. Push current only if it remains unresolved. | pop condition or unresolved ownership wrong |
| **133. [Max Stack](https://leetcode.com/problems/max-stack/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) / [LC](https://leetcode.com/problems/max-stack/) | The stack stores unresolved items whose answer depends on a future closer/warmer/smaller/larger/current token. While the current item resolves the stack top, pop and finalize that old item. Push current only if it remains unresolved. | pop condition or unresolved ownership wrong |
| **136. [Next Greater Element I](https://leetcode.com/problems/next-greater-element-i/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) / [LC](https://leetcode.com/problems/next-greater-element-i/) | The stack stores unresolved items whose answer depends on a future closer/warmer/smaller/larger/current token. While the current item resolves the stack top, pop and finalize that old item. Push current only if it remains unresolved. | pop condition or unresolved ownership wrong |
| **137. [Online Stock Span](https://leetcode.com/problems/online-stock-span/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) / [LC](https://leetcode.com/problems/online-stock-span/) | The stack stores unresolved items whose answer depends on a future closer/warmer/smaller/larger/current token. While the current item resolves the stack top, pop and finalize that old item. Push current only if it remains unresolved. | pop condition or unresolved ownership wrong |
| **157. [Design A Stack With Increment Operation](https://leetcode.com/problems/design-a-stack-with-increment-operation/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) / [LC](https://leetcode.com/problems/design-a-stack-with-increment-operation/) | The stack stores unresolved items whose answer depends on a future closer/warmer/smaller/larger/current token. While the current item resolves the stack top, pop and finalize that old item. Push current only if it remains unresolved. | pop condition or unresolved ownership wrong |

## Trie

### Trie

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **39. [Implement Trie (Prefix Tree)](https://leetcode.com/problems/implement-trie-prefix-tree/)**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) / [LC](https://leetcode.com/problems/implement-trie-prefix-tree/) | Each node represents the prefix formed by the path from root. insert creates missing child nodes and marks only the final node as a full word. search requires terminal true; startsWith does not. | prefix exists but terminal missing |
| **95. [Design Add and Search Words Data Structure](https://leetcode.com/problems/design-add-and-search-words-data-structure/)**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) / [LC](https://leetcode.com/problems/design-add-and-search-words-data-structure/) | Each edge consumes one character and each node represents the prefix consumed so far. Terminal state is separate from prefix existence. Search follows only valid child edges unless wildcard/board branching explicitly allows multiple children. | prefix exists but terminal missing |
| **159. [Longest Common Prefix](https://leetcode.com/problems/longest-common-prefix/)**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) / [LC](https://leetcode.com/problems/longest-common-prefix/) | Each edge consumes one character and each node represents the prefix consumed so far. Terminal state is separate from prefix existence. Search follows only valid child edges unless wildcard/board branching explicitly allows multiple children. | prefix exists but terminal missing |
| **161. [Replace Words](https://leetcode.com/problems/replace-words/)**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) / [LC](https://leetcode.com/problems/replace-words/) | Each edge consumes one character and each node represents the prefix consumed so far. Terminal state is separate from prefix existence. Search follows only valid child edges unless wildcard/board branching explicitly allows multiple children. | prefix exists but terminal missing |
| **162. [Search Suggestions System](https://leetcode.com/problems/search-suggestions-system/)**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) / [LC](https://leetcode.com/problems/search-suggestions-system/) | Each edge consumes one character and each node represents the prefix consumed so far. Terminal state is separate from prefix existence. Search follows only valid child edges unless wildcard/board branching explicitly allows multiple children. | prefix exists but terminal missing |
| **163. [Short Encoding of Words](https://leetcode.com/problems/short-encoding-of-words/)**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) / [LC](https://leetcode.com/problems/short-encoding-of-words/) | Each edge consumes one character and each node represents the prefix consumed so far. Terminal state is separate from prefix existence. Search follows only valid child edges unless wildcard/board branching explicitly allows multiple children. | prefix exists but terminal missing |

### Trie + backtracking

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **102. [Word Search II](https://leetcode.com/problems/word-search-ii/)**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/WordSearchII.java) / [LC](https://leetcode.com/problems/word-search-ii/) | Each edge consumes one character and each node represents the prefix consumed so far. Terminal state is separate from prefix existence. Search follows only valid child edges unless wildcard/board branching explicitly allows multiple children. | prefix exists but terminal missing |

### Binary trie / bit

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **156. [Maximum XOR of Two Numbers in an Array](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/)**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) / [LC](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/) | Each edge consumes one character and each node represents the prefix consumed so far. Terminal state is separate from prefix existence. Search follows only valid child edges unless wildcard/board branching explicitly allows multiple children. | prefix exists but terminal missing |
| **165. [Maximum XOR With an Element From Array](https://leetcode.com/problems/maximum-xor-with-an-element-from-array/)**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) / [LC](https://leetcode.com/problems/maximum-xor-with-an-element-from-array/) | Each edge consumes one character and each node represents the prefix consumed so far. Terminal state is separate from prefix existence. Search follows only valid child edges unless wildcard/board branching explicitly allows multiple children. | prefix exists but terminal missing |
| **166. [Maximum Genetic Difference Query](https://leetcode.com/problems/maximum-genetic-difference-query/)**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) / [LC](https://leetcode.com/problems/maximum-genetic-difference-query/) | Each edge consumes one character and each node represents the prefix consumed so far. Terminal state is separate from prefix existence. Search follows only valid child edges unless wildcard/board branching explicitly allows multiple children. | prefix exists but terminal missing |
| **167. [Count Pairs With XOR in a Range](https://leetcode.com/problems/count-pairs-with-xor-in-a-range/)**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) / [LC](https://leetcode.com/problems/count-pairs-with-xor-in-a-range/) | Each edge consumes one character and each node represents the prefix consumed so far. Terminal state is separate from prefix existence. Search follows only valid child edges unless wildcard/board branching explicitly allows multiple children. | prefix exists but terminal missing |

### Trie + DFS wildcard

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **160. [Longest Word in Dictionary](https://leetcode.com/problems/longest-word-in-dictionary/)**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) / [LC](https://leetcode.com/problems/longest-word-in-dictionary/) | Each edge consumes one character and each node represents the prefix consumed so far. Terminal state is separate from prefix existence. Search follows only valid child edges unless wildcard/board branching explicitly allows multiple children. | prefix exists but terminal missing |
| **164. [Map Sum Pairs](https://leetcode.com/problems/map-sum-pairs/)**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) / [LC](https://leetcode.com/problems/map-sum-pairs/) | Each edge consumes one character and each node represents the prefix consumed so far. Terminal state is separate from prefix existence. Search follows only valid child edges unless wildcard/board branching explicitly allows multiple children. | prefix exists but terminal missing |

### Trie / ranking

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **185. Hotel Reviews**<br>[Java](../../src/main/java/org/chijai/day10/session1/trie/HotelReviews.java) | Each edge consumes one character and each node represents the prefix consumed so far. Terminal state is separate from prefix existence. Search follows only valid child edges unless wildcard/board branching explicitly allows multiple children. | prefix exists but terminal missing |

## Intervals / Sorting Greedy

### Intervals / sorting

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **42. [Minimum Number Of Arrows To Burst Balloons](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalGreedyByEnd.java) / [LC](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/) | Sort balloons by end and shoot the current arrow at the earliest ending balloon's end. Any balloon starting <= arrowEnd is already covered. A new arrow is needed only when start > arrowEnd. | new arrow only when start > arrowEnd |
| **144. [Non Overlapping Intervals](https://leetcode.com/problems/non-overlapping-intervals/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalGreedyByEnd.java) / [LC](https://leetcode.com/problems/non-overlapping-intervals/) | Sort to make the next conflict or safe choice local. Track the boundary that represents the current merged interval, selected endpoint, or active resource. Equality decides whether intervals touch, overlap, or can reuse a resource. | overlap equality boundary |

### Intervals / merge

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **92. [Meeting Rooms](https://leetcode.com/problems/meeting-rooms/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalSortByStart.java) / [LC](https://leetcode.com/problems/meeting-rooms/) | Sort to make the next conflict or safe choice local. Track the boundary that represents the current merged interval, selected endpoint, or active resource. Equality decides whether intervals touch, overlap, or can reuse a resource. | overlap equality boundary |
| **142. [Insert Interval](https://leetcode.com/problems/insert-interval/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalSortByStart.java) / [LC](https://leetcode.com/problems/insert-interval/) | Sort to make the next conflict or safe choice local. Track the boundary that represents the current merged interval, selected endpoint, or active resource. Equality decides whether intervals touch, overlap, or can reuse a resource. | overlap equality boundary |
| **143. [Merge Intervals](https://leetcode.com/problems/merge-intervals/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalSortByStart.java) / [LC](https://leetcode.com/problems/merge-intervals/) | Sort to make the next conflict or safe choice local. Track the boundary that represents the current merged interval, selected endpoint, or active resource. Equality decides whether intervals touch, overlap, or can reuse a resource. | overlap equality boundary |

### Greedy last-occurrence boundary

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **145. [Partition Labels](https://leetcode.com/problems/partition-labels/)**<br>[Java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java) / [LC](https://leetcode.com/problems/partition-labels/) | Sort to make the next conflict or safe choice local. Track the boundary that represents the current merged interval, selected endpoint, or active resource. Equality decides whether intervals touch, overlap, or can reuse a resource. | overlap equality boundary |

### DP / patience sorting

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **206. [Maximum Length of Pair Chain](https://leetcode.com/problems/maximum-length-of-pair-chain/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) / [LC](https://leetcode.com/problems/maximum-length-of-pair-chain/) | Sort to make the next conflict or safe choice local. Track the boundary that represents the current merged interval, selected endpoint, or active resource. Equality decides whether intervals touch, overlap, or can reuse a resource. | overlap equality boundary |

## Union Find / DSU

### Union Find / graph

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **70. [Accounts Merge](https://leetcode.com/problems/accounts-merge/)**<br>[Java](../../src/main/java/org/chijai/day8/graph/session3/AccountsMerge.java) / [LC](https://leetcode.com/problems/accounts-merge/) | parent[x] identifies the component representative after find compression. union merges two components only when representatives differ; equal representatives mean the connection was already present. Use DSU only when component identity is enough and path details are irrelevant. | union raw nodes instead of roots |

## Greedy

### Greedy / DP states

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **72. [Best Time to Buy and Sell Stock](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java) / [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/) | Name the local choice and the exchange/dominance reason that makes it safe. After taking the choice, update the boundary/state that represents everything committed so far. If a future choice can invalidate the local choice, this is not greedy yet. | local choice lacks proof |
| **80. [Best Time to Buy and Sell Stock II](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java) / [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/) | Name the local choice and the exchange/dominance reason that makes it safe. After taking the choice, update the boundary/state that represents everything committed so far. If a future choice can invalidate the local choice, this is not greedy yet. | local choice lacks proof |

### Greedy

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **76. [Gas Station](https://leetcode.com/problems/gas-station/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session1/GasStation.java) / [LC](https://leetcode.com/problems/gas-station/) | tank is the net gas from the current candidate start through the current station. If tank becomes negative, no station inside that failed segment can be a valid start, so the next index becomes the candidate and tank resets to 0. total gas minus cost decides whether any solution exists. | reset start after tank < 0 |
| **78. [Jump Game](https://leetcode.com/problems/jump-game/)**<br>[Java](../../src/main/java/org/chijai/day9/dp/session1/GasStation.java) / [LC](https://leetcode.com/problems/jump-game/) | farthest is the farthest index reachable using positions processed so far. If the current index is ever greater than farthest, it is unreachable and the answer is false. Otherwise update farthest with i + nums[i] and succeed once farthest reaches the last index. | fail when i > farthest |

## Design Data Structures

### HashMap + doubly linked list

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **89. [First Unique Number](https://leetcode.com/problems/first-unique-number/)**<br>[Java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java) / [LC](https://leetcode.com/problems/first-unique-number/) | Start by naming each operation's contract and the stored state that makes the contract cheap. Every mutation must preserve lookup, ordering, capacity, expiry, or consistency invariants. State the failure/edge behavior before coding the happy path. | state mutation violates operation invariant |

### LLD / URL shortener

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **183. [Encode And Decode Tinyurl](https://leetcode.com/problems/encode-and-decode-tinyurl/)**<br>[Java](../../src/main/java/org/chijai/design/lld/DesignUrlShortner.java) / [LC](https://leetcode.com/problems/encode-and-decode-tinyurl/) | Start by naming each operation's contract and the stored state that makes the contract cheap. Every mutation must preserve lookup, ordering, capacity, expiry, or consistency invariants. State the failure/edge behavior before coding the happy path. | state mutation violates operation invariant |

### Stack/queue design

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **184. [Design Circular Queue](https://leetcode.com/problems/design-circular-queue/)**<br>[Java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) / [LC](https://leetcode.com/problems/design-circular-queue/) | Start by naming each operation's contract and the stored state that makes the contract cheap. Every mutation must preserve lookup, ordering, capacity, expiry, or consistency invariants. State the failure/edge behavior before coding the happy path. | state mutation violates operation invariant |

### LLD / domain modeling

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **186. Design Fraud Pattern Detection**<br>[Java](../../src/main/java/org/chijai/design/lld/DesignFraudPatternDetection.java) | Start by naming each operation's contract and the stored state that makes the contract cheap. Every mutation must preserve lookup, ordering, capacity, expiry, or consistency invariants. State the failure/edge behavior before coding the happy path. | state mutation violates operation invariant |

### LLD/API integration

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **187. Api Integration Example**<br>[Java](../../src/main/java/org/chijai/design/lld/ApiIntegrationExample.java) | Start by naming each operation's contract and the stored state that makes the contract cheap. Every mutation must preserve lookup, ordering, capacity, expiry, or consistency invariants. State the failure/edge behavior before coding the happy path. | state mutation violates operation invariant |

### LLD / data structures

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **188. Design Redis**<br>[Java](../../src/main/java/org/chijai/design/lld/DesignRedis.java) | Start by naming each operation's contract and the stored state that makes the contract cheap. Every mutation must preserve lookup, ordering, capacity, expiry, or consistency invariants. State the failure/edge behavior before coding the happy path. | state mutation violates operation invariant |

### LLD / rate limiting

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **189. Design Token Bucket Rate Limiter**<br>[Java](../../src/main/java/org/chijai/design/lld/DesignTokenBucketRateLimiter.java) | Start by naming each operation's contract and the stored state that makes the contract cheap. Every mutation must preserve lookup, ordering, capacity, expiry, or consistency invariants. State the failure/edge behavior before coding the happy path. | state mutation violates operation invariant |

## Math / Bit / String

### KMP string matching

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **105. [Find The Index Of The First Occurrence In A String](https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/)**<br>[Java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java) / [LC](https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/) | Expose the algebra, carry, bit, border, or contribution meaning before simulating. Each update must preserve that exact numeric/string invariant. Avoid operator claims unless the linked source confirms the equality boundary. | operator-sensitive boundary guessed |
| **155. [Repeated Substring Pattern](https://leetcode.com/problems/repeated-substring-pattern/)**<br>[Java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java) / [LC](https://leetcode.com/problems/repeated-substring-pattern/) | Expose the algebra, carry, bit, border, or contribution meaning before simulating. Each update must preserve that exact numeric/string invariant. Avoid operator claims unless the linked source confirms the equality boundary. | operator-sensitive boundary guessed |
| **175. [Shortest Palindrome](https://leetcode.com/problems/shortest-palindrome/)**<br>[Java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java) / [LC](https://leetcode.com/problems/shortest-palindrome/) | Expose the algebra, carry, bit, border, or contribution meaning before simulating. Each update must preserve that exact numeric/string invariant. Avoid operator claims unless the linked source confirms the equality boundary. | operator-sensitive boundary guessed |

### KMP / rolling hash

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **158. [Longest Happy Prefix](https://leetcode.com/problems/longest-happy-prefix/)**<br>[Java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) / [LC](https://leetcode.com/problems/longest-happy-prefix/) | Expose the algebra, carry, bit, border, or contribution meaning before simulating. Each update must preserve that exact numeric/string invariant. Avoid operator claims unless the linked source confirms the equality boundary. | operator-sensitive boundary guessed |

### Bit/string addition

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **171. [Add Binary](https://leetcode.com/problems/add-binary/)**<br>[Java](../../src/main/java/org/chijai/day10/session2/AddBinary.java) / [LC](https://leetcode.com/problems/add-binary/) | Expose the algebra, carry, bit, border, or contribution meaning before simulating. Each update must preserve that exact numeric/string invariant. Avoid operator claims unless the linked source confirms the equality boundary. | operator-sensitive boundary guessed |

### Math / sieve

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **172. [Count Primes](https://leetcode.com/problems/count-primes/)**<br>[Java](../../src/main/java/org/chijai/day10/session2/CountPrimes.java) / [LC](https://leetcode.com/problems/count-primes/) | Expose the algebra, carry, bit, border, or contribution meaning before simulating. Each update must preserve that exact numeric/string invariant. Avoid operator claims unless the linked source confirms the equality boundary. | operator-sensitive boundary guessed |

### Contribution counting

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **173. [Count Unique Characters of All Substrings of a Given String](https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/)**<br>[Java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java) / [LC](https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/) | Expose the algebra, carry, bit, border, or contribution meaning before simulating. Each update must preserve that exact numeric/string invariant. Avoid operator claims unless the linked source confirms the equality boundary. | operator-sensitive boundary guessed |

## Basics / Implementation

### Matrix boundary traversal

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **153. [Spiral Matrix](https://leetcode.com/problems/spiral-matrix/)**<br>[Java](../../src/main/java/org/chijai/day1/Arrays/session1/SpiralMatrix.java) / [LC](https://leetcode.com/problems/spiral-matrix/) | Shrink top, bottom, left, and right boundaries after traversing each side. Traverse top row, right col, bottom row if valid, left col if valid; move boundaries inward. | verify exact boundary in linked Java |

### Parsing / edge cases

| Problem | Say Before Coding - Correctness Contract | Trap |
|---|---|---|
| **154. [String To Integer Atoi](https://leetcode.com/problems/string-to-integer-atoi/)**<br>[Java](../../src/main/java/org/chijai/day3/session3/StringToIntegerAtoi.java) / [LC](https://leetcode.com/problems/string-to-integer-atoi/) | Parse sign and digits once, clamping before overflow. Skip spaces, read optional sign, accumulate digit while checking against INT_MAX limits. | verify exact boundary in linked Java |