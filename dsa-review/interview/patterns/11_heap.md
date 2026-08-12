# Heap / Priority Queue

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

Heap top is the next best candidate; keep only the frontier or top K when possible.

## Interview Move

Sorting everything is wasteful; a heap keeps only the next best or top K frontier.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 30 | Phase 1 - No Red Flags | Meeting Rooms Ii | Intervals / heap | [Java](../../../src/main/java/org/chijai/day1/session2/MeetingRoom.java) | [LC](https://leetcode.com/problems/meeting-rooms-ii/) | Sort meetings by start; a min-heap of end times counts active rooms. | Sort intervals, pop heap while end <= start, push current end, track max heap size. |
| 104 | Phase 3 - Important | Task Scheduler | Greedy / heap | [Java](../../../src/main/java/org/chijai/day7/session1/heap/TaskScheduler.java) | [LC](https://leetcode.com/problems/task-scheduler/) | Heap top is the next best candidate; keep only the frontier or top K when possible. | Push candidates with comparator; poll when size or frontier rules require it. |
| 108 | Phase 3 - Important | Find Median From Data Stream | Two heaps | [Java](../../../src/main/java/org/chijai/day7/session1/heap/Median.java) | [LC](https://leetcode.com/problems/find-median-from-data-stream/) | Heap top is the next best candidate; keep only the frontier or top K when possible. | Push candidates with comparator; poll when size or frontier rules require it. |
| 121 | Phase 4 - Secondary | Top K Frequent Elements | Frequency + heap/bucket | [Java](../../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentTransactions.java) | [LC](https://leetcode.com/problems/top-k-frequent-elements/) | Heap top is the next best candidate; keep only the frontier or top K when possible. | Push candidates with comparator; poll when size or frontier rules require it. |
| 139 | Phase 4 - Secondary | K Closest Points To Origin | Heap / quickselect | [Java](../../../src/main/java/org/chijai/day7/session1/heap/KClosestPointsToOrigin.java) | [LC](https://leetcode.com/problems/k-closest-points-to-origin/) | Heap top is the next best candidate; keep only the frontier or top K when possible. | Push candidates with comparator; poll when size or frontier rules require it. |
| 140 | Phase 4 - Secondary | Kth Largest Element In An Array | Min-heap size K | [Java](../../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) | [LC](https://leetcode.com/problems/kth-largest-element-in-an-array/) | Heap top is the next best candidate; keep only the frontier or top K when possible. | Push candidates with comparator; poll when size or frontier rules require it. |
| 150 | Phase 4 - Secondary | Kth Largest Element In A Stream | Min-heap size K | [Java](../../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) | [LC](https://leetcode.com/problems/kth-largest-element-in-a-stream/) | Heap top is the next best candidate; keep only the frontier or top K when possible. | Push candidates with comparator; poll when size or frontier rules require it. |
| 153 | Phase 4 - Secondary | Award Top K Hotels | Heap / ranking | [Java](../../../src/main/java/org/chijai/day7/session1/heap/AwardTopKHotels.java) | - | Heap top is the next best candidate; keep only the frontier or top K when possible. | Push candidates with comparator; poll when size or frontier rules require it. |
| 154 | Phase 4 - Secondary | Sort Characters By Frequency | Heap fundamentals | [Java](../../../src/main/java/org/chijai/day7/session1/heap/HeapSort.java) | [LC](https://leetcode.com/problems/sort-characters-by-frequency/) | Heap top is the next best candidate; keep only the frontier or top K when possible. | Push candidates with comparator; poll when size or frontier rules require it. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.