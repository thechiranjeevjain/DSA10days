# Prefix/Suffix

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Precompute cumulative left/right state so each range or exclusion is answered cheaply.

## Interview Move

Brute force recomputes ranges; prefix/suffix stores reusable aggregate state.

## Problems

| Global Rank | Must Level | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 17 | Must Must Must | Binary Subarrays With Sum | Prefix/window counting | [Java](../../../src/main/java/org/chijai/day3/session2/NiceSubArrays.java) | [LC](https://leetcode.com/problems/binary-subarrays-with-sum/) | For binary arrays, exact goal count can be atMost(goal) - atMost(goal-1). | Implement atMost(sum): expand right, shrink while sum > goal, add window length. |
| 18 | Must Must Must | Product Of Array Except Self | Prefix/suffix | [Java](../../../src/main/java/org/chijai/day3/session2/ProductOfArrayExceptSelf.java) | [LC](https://leetcode.com/problems/product-of-array-except-self/) | Answer is product of everything left times everything right, no division needed. | Fill answer with left products, then multiply by running right product from the end. |
| 103 | Must | Implement Trie Prefix Tree | Trie | [Java](../../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) | [LC](https://leetcode.com/problems/implement-trie-prefix-tree/) | Precompute cumulative left/right state so each range or exclusion is answered cheaply. | Build prefix/suffix arrays or running aggregates, then combine in O(1) per query/index. |
| 158 | If Time | Find The Index Of The First Occurrence In A String | KMP / rolling hash | [Java](../../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) | [LC](https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/) | Precompute cumulative left/right state so each range or exclusion is answered cheaply. | Build prefix/suffix arrays or running aggregates, then combine in O(1) per query/index. |
| 159 | If Time | Longest Happy Prefix | KMP / rolling hash | [Java](../../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) | [LC](https://leetcode.com/problems/longest-happy-prefix/) | Precompute cumulative left/right state so each range or exclusion is answered cheaply. | Build prefix/suffix arrays or running aggregates, then combine in O(1) per query/index. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.