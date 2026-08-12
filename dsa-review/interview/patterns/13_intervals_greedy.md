# Intervals/Greedy

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints.

## Interview Move

Unsorted comparisons are noisy; sorting makes overlap or greedy choice local.

## Problems

| Global Rank | Must Level | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 98 | Must | Intervals | Intervals / merge | [Java](../../../src/main/java/org/chijai/day1/session2/Intervals.java) | - | Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints. | Sort by start/end, then merge/count/select with one pass or heap. |
| 131 | Should | Car Pooling | Intervals / sorting | [Java](../../../src/main/java/org/chijai/day3/session2/MinimumPlatforms.java) | [LC](https://leetcode.com/problems/car-pooling/) | Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints. | Sort by start/end, then merge/count/select with one pass or heap. |
| 132 | Should | Minimum Number Of Arrows To Burst Balloons | Intervals / sorting | [Java](../../../src/main/java/org/chijai/day3/session2/MinimumPlatforms.java) | [LC](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/) | Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints. | Sort by start/end, then merge/count/select with one pass or heap. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.