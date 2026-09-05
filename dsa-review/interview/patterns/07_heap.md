# Heap / Priority Queue

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

Keep only the frontier, top K, or two balanced halves instead of fully sorting each step.

## Interview Move

Sorting everything is wasteful; a heap keeps only the next best or top K frontier.

## Pattern Taxonomy Map

```mermaid
flowchart TD
  Topic["TOPIC<br/>Heap / Priority Queue"]
  Recognition["RECOGNITION<br/>Keep only the frontier, top K, or two balanced halves instead of fully sorting each step."]
  Invariant["INVARIANT<br/>Sorting everything is wasteful; a heap keeps only the next best or top K frontier."]
  Topic --> Recognition --> Invariant
  Invariant --> Sub01["SUB-PATTERN<br/>Bounded frequency buckets<br/>1 problem(s)"]
  Sub01 --> Sub01A01["ANCHOR<br/>rank 139: H-Index"]
  Invariant --> Sub02["SUB-PATTERN<br/>Bounded heap with tie ordering<br/>1 problem(s)"]
  Sub02 --> Sub02A01["ANCHOR<br/>rank 138: Top K Frequent Words"]
  Invariant --> Sub03["SUB-PATTERN<br/>Frequency + heap/bucket<br/>2 problem(s)"]
  Sub03 --> Sub03A01["ANCHOR<br/>rank 36: Top K Frequent Elements"]
  Sub03 --> Sub03A02["ANCHOR<br/>rank 140: Sort Characters By Frequency"]
  Invariant --> Sub04["SUB-PATTERN<br/>Greedy / heap<br/>1 problem(s)"]
  Sub04 --> Sub04A01["ANCHOR<br/>rank 99: Task Scheduler"]
  Invariant --> Sub05["SUB-PATTERN<br/>Heap / divide and conquer<br/>1 problem(s)"]
  Sub05 --> Sub05A01["ANCHOR<br/>rank 11: Merge K Sorted Lists"]
  Invariant --> Sub06["SUB-PATTERN<br/>Heap / quickselect<br/>1 problem(s)"]
  Sub06 --> Sub06A01["ANCHOR<br/>rank 137: K Closest Points To Origin"]
  Invariant --> Sub07["SUB-PATTERN<br/>Heap / ranking<br/>1 problem(s)"]
  Sub07 --> Sub07A01["ANCHOR<br/>rank 174: Award Top K Hotels"]
  Invariant --> Sub08["SUB-PATTERN<br/>Intervals / heap<br/>1 problem(s)"]
  Sub08 --> Sub08A01["ANCHOR<br/>rank 38: Meeting Rooms II"]
  Invariant --> Sub09["SUB-PATTERN<br/>Min-heap size K<br/>2 problem(s)"]
  Sub09 --> Sub09A01["ANCHOR<br/>rank 100: Kth Largest Element In An Array"]
  Sub09 --> Sub09A02["ANCHOR<br/>rank 101: Kth Largest Element In A Stream"]
  Invariant --> Sub10["SUB-PATTERN<br/>Two heaps<br/>1 problem(s)"]
  Sub10 --> Sub10A01["ANCHOR<br/>rank 49: Find Median From Data Stream"]
```

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 11 | Phase 1 - No Red Flags | Merge K Sorted Lists | Heap / divide and conquer | [Java](../../../src/main/java/org/chijai/day4/LinkedList/session4/MergeKSortedLists.java) | [LC](https://leetcode.com/problems/merge-k-sorted-lists/) | A min-heap stores the current smallest head among k lists. | Push non-null heads, poll min, append it, push its next. |
| 36 | Phase 2 - Strong Core | Top K Frequent Elements | Frequency + heap/bucket | [Java](../../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) | [LC](https://leetcode.com/problems/top-k-frequent-elements/) | Count frequencies, then keep only the k highest-frequency entries. | Build frequency map, then use bucket lists by frequency or a min-heap of size k. |
| 38 | Phase 2 - Strong Core | Meeting Rooms II | Intervals / heap | [Java](../../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalActiveMinHeap.java) | [LC](https://leetcode.com/problems/meeting-rooms-ii/) | Sort meetings by start; a min-heap of end times counts active rooms. | Sort intervals, pop heap while end <= start, push current end, track max heap size. |
| 49 | Phase 2 - Strong Core | Find Median From Data Stream | Two heaps | [Java](../../../src/main/java/org/chijai/day7/session1/heap/Median.java) | [LC](https://leetcode.com/problems/find-median-from-data-stream/) | Two heaps split lower and upper halves; median comes from heap tops. | Push into maxHeap/minHeap, rebalance sizes, median is top or average of tops. |
| 99 | Phase 3 - Important | Task Scheduler | Greedy / heap | [Java](../../../src/main/java/org/chijai/day7/session1/heap/TaskScheduler.java) | [LC](https://leetcode.com/problems/task-scheduler/) | CPU idles only when the most frequent tasks cannot be spaced by cooldown gaps. | Use maxFreq and countMax: max(tasks.length, (maxFreq-1)*(n+1)+countMax). |
| 100 | Phase 3 - Important | Kth Largest Element In An Array | Min-heap size K | [Java](../../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) | [LC](https://leetcode.com/problems/kth-largest-element-in-an-array/) | A size-k min-heap keeps the k largest seen so far; top is kth largest. | Push each number, pop when heap size > k, return heap top. |
| 101 | Phase 3 - Important | Kth Largest Element In A Stream | Min-heap size K | [Java](../../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java) | [LC](https://leetcode.com/problems/kth-largest-element-in-a-stream/) | Maintain a size-k min-heap after every add; top is the kth largest in the stream. | On add, push value, trim heap to k, return heap.peek(). |
| 137 | Phase 4 - Secondary | K Closest Points To Origin | Heap / quickselect | [Java](../../../src/main/java/org/chijai/day7/session1/heap/KClosestPointsToOrigin.java) | [LC](https://leetcode.com/problems/k-closest-points-to-origin/) | Keep the k smallest squared distances; compare without taking square roots. | Use max-heap of size k by distance, or quickselect by squared distance. |
| 138 | Phase 4 - Secondary | Top K Frequent Words | Bounded heap with tie ordering | [Java](../../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) | [LC](https://leetcode.com/problems/top-k-frequent-words/) | A size-k min-heap stores the k strongest words, with the weakest winner at the root: lower frequency, or lexicographically larger on a tie. | Count words, offer each distinct word, evict when size > k, then remove from weakest to strongest and prepend to produce final order. |
| 139 | Phase 4 - Secondary | H-Index | Bounded frequency buckets | [Java](../../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) | [LC](https://leetcode.com/problems/h-index/) | buckets[h] counts papers with exactly h citations, except every citation >= n is capped into bucket n. | Accumulate paper counts from h = n downward; the first h with papers >= h is the maximum valid H-index. |
| 140 | Phase 4 - Secondary | Sort Characters By Frequency | Frequency + heap/bucket | [Java](../../../src/main/java/org/chijai/day7/session1/heap/TopKFrequentElements.java) | [LC](https://leetcode.com/problems/sort-characters-by-frequency/) | Frequency map plus bucket/heap outputs characters from highest count to lowest. | Count chars, bucket by frequency or heap entries, append char repeated count times. |
| 174 | Phase 5 - If Time | Award Top K Hotels | Heap / ranking | [Java](../../../src/main/java/org/chijai/day7/session1/heap/AwardTopKHotels.java) | - | Score each hotel by keyword hits, then rank by score and tie-breaker. | Build keyword set, count matches per hotel review, then sort or heap by score/id. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.