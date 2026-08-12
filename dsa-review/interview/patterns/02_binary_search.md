# Binary Search / Answer Search

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

Maintain a monotonic search space and discard the half that cannot contain the answer.

## Interview Move

Brute force scans candidates; monotonicity lets each check discard half the search space.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 2 | Phase 1 - No Red Flags | Binary Search | Binary search invariant | [Java](../../../src/main/java/org/chijai/day2/session1/BinarySearch.java) | [LC](https://leetcode.com/problems/binary-search/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 20 | Phase 1 - No Red Flags | Koko Eating Bananas | Binary search on answer | [Java](../../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | [LC](https://leetcode.com/problems/koko-eating-bananas/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 21 | Phase 1 - No Red Flags | Search In Rotated Sorted Array | Binary search boundary | [Java](../../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LC](https://leetcode.com/problems/search-in-rotated-sorted-array/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 22 | Phase 1 - No Red Flags | Find First And Last Position Of Element In Sorted Array | Binary search boundary | [Java](../../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LC](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 78 | Phase 3 - Important | Search In Rotated Sorted Array Ii | Binary search boundary | [Java](../../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LC](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 82 | Phase 3 - Important | Search Insert Position | Binary search invariant | [Java](../../../src/main/java/org/chijai/day2/session1/BinarySearch.java) | [LC](https://leetcode.com/problems/search-insert-position/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 86 | Phase 3 - Important | Find Peak Element | Binary search boundary | [Java](../../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LC](https://leetcode.com/problems/find-peak-element/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 87 | Phase 3 - Important | First Bad Version | Binary search invariant | [Java](../../../src/main/java/org/chijai/day2/session1/BinarySearch.java) | [LC](https://leetcode.com/problems/first-bad-version/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 88 | Phase 3 - Important | Split Array Largest Sum | Binary search on answer | [Java](../../../src/main/java/org/chijai/day2/session2/AGGRCOW.java) | [LC](https://leetcode.com/problems/split-array-largest-sum/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 92 | Phase 3 - Important | Capacity To Ship Packages Within D Days | Binary search on answer | [Java](../../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | [LC](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 93 | Phase 3 - Important | Minimum Number Of Days To Make M Bouquets | Binary search on answer | [Java](../../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | [LC](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 104 | Phase 3 - Important | Time Based Key Value Store | HashMap + binary search | [Java](../../../src/main/java/org/chijai/day2/session3/TimeBasedKeyValueStore.java) | [LC](https://leetcode.com/problems/time-based-key-value-store/) | Map each key to timestamped values in order; binary search finds latest timestamp <= query. | Append on set; on get binary search the key's list for rightmost timestamp <= target. |
| 129 | Phase 4 - Secondary | Sqrtx | Binary search boundary | [Java](../../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LC](https://leetcode.com/problems/sqrtx/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.