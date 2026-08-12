# Intervals / Sorting Greedy

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints.

## Interview Move

Unsorted comparisons are noisy; sorting makes overlap or greedy choice local.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 98 | Phase 3 - Important | Intervals | Intervals / merge | [Java](../../../src/main/java/org/chijai/day1/session2/Intervals.java) | - | Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints. | Sort by start/end, then merge/count/select with one pass or heap. |
| 131 | Phase 4 - Secondary | Car Pooling | Intervals / sorting | [Java](../../../src/main/java/org/chijai/day3/session2/MinimumPlatforms.java) | [LC](https://leetcode.com/problems/car-pooling/) | Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints. | Sort by start/end, then merge/count/select with one pass or heap. |
| 132 | Phase 4 - Secondary | Minimum Number Of Arrows To Burst Balloons | Intervals / sorting | [Java](../../../src/main/java/org/chijai/day3/session2/MinimumPlatforms.java) | [LC](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/) | Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints. | Sort by start/end, then merge/count/select with one pass or heap. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.