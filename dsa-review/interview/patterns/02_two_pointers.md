# Two Pointers

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Shrink the search space by moving the pointer that can still improve the answer.

## Interview Move

Brute force tries pairs; sorting/order lets pointers eliminate impossible pairs.

## Problems

| Global Rank | Must Level | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 4 | Must Must Must | 2Sum / 3Sum / 4Sum | Two pointers / hash | [Java](../../../src/main/java/org/chijai/day1/session2/Three3Sum2Sum.java) | - | For sum families: hash for 2Sum, sort/fix one value, then two-pointer the remaining sum. | Sort when indices are not required, loop fixed values, move left/right by sum comparison, skip duplicates. |
| 5 | Must Must Must | Valid Palindrome | Two pointers | [Java](../../../src/main/java/org/chijai/day3/session3/ValidPalindrome.java) | [LC](https://leetcode.com/problems/valid-palindrome/) | Skip non-alphanumeric chars and compare normalized ends while pointers move inward. | Advance left/right past invalid chars, compare lowercase chars, stop when pointers cross. |
| 6 | Must Must Must | Container With Most Water | Two pointers | [Java](../../../src/main/java/org/chijai/day5/stack/session2/ContainerWithMostWater.java) | [LC](https://leetcode.com/problems/container-with-most-water/) | Area is limited by shorter wall, so move the shorter side inward. | Compute area at left/right, update max, move pointer with smaller height. |
| 7 | Must Must Must | Trapping Rain Water | Two pointers / stack | [Java](../../../src/main/java/org/chijai/day5/stack/session2/TrappingRainwater.java) | [LC](https://leetcode.com/problems/trapping-rain-water/) | Water at a side depends on the smaller max boundary seen so far. | Move the side with lower height, update max, add max-height when bounded. |
| 101 | Must | Longest Palindrome | Hash/frequency | [Java](../../../src/main/java/org/chijai/day3/session3/LongestPalindrome.java) | [LC](https://leetcode.com/problems/longest-palindrome/) | Shrink the search space by moving the pointer that can still improve the answer. | Initialize pointers, compare current state, move the pointer whose movement is justified. |
| 157 | If Time | Shortest Palindrome | KMP string matching | [Java](../../../src/main/java/org/chijai/day7/session2/KmpPatterns.java) | [LC](https://leetcode.com/problems/shortest-palindrome/) | Shrink the search space by moving the pointer that can still improve the answer. | Initialize pointers, compare current state, move the pointer whose movement is justified. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.