# Binary Search

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Maintain a monotonic search space and discard the half that cannot contain the answer.

## Interview Move

Brute force scans candidates; monotonicity lets each check discard half the search space.

## Problems

| Global Rank | Must Level | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 75 | Must | Binary Search | Binary search invariant | [Java](../../../src/main/java/org/chijai/day2/session1/BinarySearch.java) | [LC](https://leetcode.com/problems/binary-search/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 76 | Must | First Bad Version | Binary search invariant | [Java](../../../src/main/java/org/chijai/day2/session1/BinarySearch.java) | [LC](https://leetcode.com/problems/first-bad-version/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 77 | Must | Search Insert Position | Binary search invariant | [Java](../../../src/main/java/org/chijai/day2/session1/BinarySearch.java) | [LC](https://leetcode.com/problems/search-insert-position/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 78 | Must | Find First And Last Position Of Element In Sorted Array | Binary search boundary | [Java](../../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LC](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 79 | Must | Find Peak Element | Binary search boundary | [Java](../../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LC](https://leetcode.com/problems/find-peak-element/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 80 | Must | Search In Rotated Sorted Array | Binary search boundary | [Java](../../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LC](https://leetcode.com/problems/search-in-rotated-sorted-array/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 81 | Must | Search In Rotated Sorted Array Ii | Binary search boundary | [Java](../../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LC](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 82 | Must | Sqrtx | Binary search boundary | [Java](../../../src/main/java/org/chijai/day2/session1/SearchRange.java) | [LC](https://leetcode.com/problems/sqrtx/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 83 | Must | Split Array Largest Sum | Binary search on answer | [Java](../../../src/main/java/org/chijai/day2/session2/AGGRCOW.java) | [LC](https://leetcode.com/problems/split-array-largest-sum/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 84 | Must | Capacity To Ship Packages Within D Days | Binary search on answer | [Java](../../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | [LC](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 85 | Must | Koko Eating Bananas | Binary search on answer | [Java](../../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | [LC](https://leetcode.com/problems/koko-eating-bananas/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 86 | Must | Minimum Number Of Days To Make M Bouquets | Binary search on answer | [Java](../../../src/main/java/org/chijai/day2/session2/KokoBananas.java) | [LC](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 87 | Must | Insert Into A Binary Search Tree | BST property | [Java](../../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | [LC](https://leetcode.com/problems/insert-into-a-binary-search-tree/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 88 | Must | Lowest Common Ancestor Of A Binary Search Tree | BST property | [Java](../../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 89 | Must | Search In A Binary Search Tree | BST property | [Java](../../../src/main/java/org/chijai/day6/trees/session1/LCA_BST.java) | [LC](https://leetcode.com/problems/search-in-a-binary-search-tree/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 110 | Must | Maximum Profit In Job Scheduling | DP + binary search | [Java](../../../src/main/java/org/chijai/day2/session3/MaximumProfitInJobScheduling.java) | [LC](https://leetcode.com/problems/maximum-profit-in-job-scheduling/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 111 | Should | Time Based Key Value Store | HashMap + binary search | [Java](../../../src/main/java/org/chijai/day2/session3/TimeBasedKeyValueStore.java) | [LC](https://leetcode.com/problems/time-based-key-value-store/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 112 | Should | Binary Search Tree Iterator | BST inorder | [Java](../../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) | [LC](https://leetcode.com/problems/binary-search-tree-iterator/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |
| 113 | Should | Recover Binary Search Tree | BST inorder | [Java](../../../src/main/java/org/chijai/day6/trees/session2/RecoverBST.java) | [LC](https://leetcode.com/problems/recover-binary-search-tree/) | Maintain a monotonic search space and discard the half that cannot contain the answer. | Define left/right and predicate; update the boundary without losing the answer. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.