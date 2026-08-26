# Intervals / Sorting Greedy

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints.

## Interview Move

Unsorted comparisons are noisy; sorting makes overlap or greedy choice local.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 37 | Phase 2 - Strong Core | Minimum Number Of Arrows To Burst Balloons | Intervals / sorting | [Java](../../../src/main/java/org/chijai/day3/session2/prefix/suffix/prefix/suffix/MinimumPlatforms.java) | [LC](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/) | Sort balloons by end; shoot at current end and start a new arrow only after it is missed. | Sort by end, keep currentArrowEnd, increment arrows when next.start > currentArrowEnd. |
| 95 | Phase 3 - Important | Meeting Rooms | Intervals / heap | [Java](../../../src/main/java/org/chijai/day1/Arrays/Arrays/session2/MeetingRoom.java) | [LC](https://leetcode.com/problems/meeting-rooms/) | After sorting intervals by start, any overlap with the previous end means a conflict. | Sort by start, scan adjacent intervals, return false if current.start < previous.end. |
| 138 | Phase 4 - Secondary | Intervals | Intervals / merge | [Java](../../../src/main/java/org/chijai/day1/Arrays/Arrays/session2/Intervals.java) | - | Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints. | Sort by start/end, then merge/count/select with one pass or heap. |
| 139 | Phase 4 - Secondary | Car Pooling | Intervals / sorting | [Java](../../../src/main/java/org/chijai/day3/session2/prefix/suffix/prefix/suffix/MinimumPlatforms.java) | [LC](https://leetcode.com/problems/car-pooling/) | Treat each pickup/dropoff as passenger-count delta and ensure capacity is never exceeded. | Use difference array or sorted events: add passengers at start, subtract at end, track running load. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.