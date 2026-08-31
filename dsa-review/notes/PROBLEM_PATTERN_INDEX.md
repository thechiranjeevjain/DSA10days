# Problem Pattern Index

Use this file as the active tracker for the Java chapters in `src/main/java/org/chijai`.

Status columns:

- `Recall`: Can I name the pattern and invariant from memory?
- `Code`: Can I implement from a blank editor?
- `Variant`: Can I solve a nearby new problem?
- Use grades 0-5 from `INTERVIEW_DSA_MASTERY.md`.

## Priority A - Master First

These are the highest ROI patterns for avoiding interview red flags.

| File | Main pattern | ROI | Recall | Code | Variant | Notes |
|---|---|---|---:|---:|---:|---|
| `day1/Arrays/session1/RansomNote.java` | HashMap/frequency | A |  |  |  | |
| `day1/Arrays/session2/MajorityElement.java` | Boyer-Moore / frequency | A |  |  |  | |
| `day1/Arrays/session2/Three3Sum2Sum.java` | Two pointers / hash | A |  |  |  | |
| `day1/Arrays/session4/Intervals/IntervalSortByStart.java` | Intervals / merge | A |  |  |  | |
| `day1/Arrays/session4/Intervals/IntervalActiveMinHeap.java` | Intervals / heap | A |  |  |  | |
| `day2/session1/BinarySearch.java` | Binary search invariant | A |  |  |  | |
| `day2/session1/SearchRange.java` | Binary search boundary | A |  |  |  | |
| `day2/session2/AGGRCOW.java` | Binary search on answer | A |  |  |  | |
| `day2/session2/KokoBananas.java` | Binary search on answer | A |  |  |  | |
| `day3/session1/AtMostKDistinct.java` | Sliding window | A |  |  |  | |
| `day3/session1/LongestSubString.java` | Sliding window / set | A |  |  |  | |
| `day3/session1/LongestSubstringVariations.java` | Sliding window variants | A |  |  |  | |
| `day3/session1/MinimumWindowSubstring.java` | Sliding window / need-have | A |  |  |  | |
| `day3/session2/prefix/suffix/NiceSubArrays.java` | Prefix/window counting | A |  |  |  | |
| `day3/session2/prefix/suffix/ProductOfArrayExceptSelf.java` | Prefix/suffix | A |  |  |  | |
| `day3/session1/FindAllAnagramsInAString.java` | Sliding window frequency | A |  |  |  | |
| `day3/session3/ValidAnagram.java` | Frequency count | A |  |  |  | |
| `day3/session3/ValidPalindrome.java` | Two pointers | A |  |  |  | |
| `day4/LinkedList/session1/Intersection.java` | Linked list two pointers | A |  |  |  | |
| `day4/LinkedList/session1/LinkedListCycle.java` | Fast/slow pointers | A |  |  |  | |
| `day4/LinkedList/session1/ReverseLinkedList.java` | Pointer reversal | A |  |  |  | |
| `day4/LinkedList/session2/CopyListWithRandomPointer.java` | HashMap / interleaving copy | A |  |  |  | |
| `day4/LinkedList/session2/ReverseLinkedListNodesK.java` | Linked-list reversal groups | A |  |  |  | |
| `day4/LinkedList/session3/LruCache.java` | HashMap + doubly linked list | A |  |  |  | |
| `day4/LinkedList/session4/LinkedListCycleII.java` | Floyd cycle entry | A |  |  |  | |
| `day4/LinkedList/session4/Merge2SortedLists.java` | Merge / dummy node | A |  |  |  | |
| `day4/LinkedList/session4/MergeKSortedLists.java` | Heap / divide and conquer | A |  |  |  | |
| `day4/LinkedList/session4/MiddleOfLinkedList.java` | Fast/slow pointers | A |  |  |  | |
| `day5/stack/session1/monotonic/DailyTemperatures.java` | Monotonic stack | A |  |  |  | |
| `day5/stack/session1/monotonic/NextGreaterElement.java` | Monotonic stack | A |  |  |  | |
| `day1/Arrays/session2/ContainerWithMostWater.java` | Two pointers | A |  |  |  | |
| `day5/stack/session1/monotonic/LargestRectangle.java` | Monotonic stack | A |  |  |  | |
| `day3/session2/prefix/suffix/TrappingRainwater.java` | Two pointers / stack | A |  |  |  | |
| `day5/stack/session3/ValidParentheses.java` | Stack | A |  |  |  | |
| `day6/trees/session1/BinaryTreeInorderTraversal.java` | Tree DFS / stack | A |  |  |  | |
| `day6/trees/session1/BinaryTreeSideView.java` | Tree BFS / DFS | A |  |  |  | |
| `day6/trees/session1/BinaryTreeTraversal.java` | Tree traversal | A |  |  |  | |
| `day6/trees/session1/LCA.java` | Tree DFS return contract | A |  |  |  | |
| `day6/trees/session1/LCA_BST.java` | BST property | A |  |  |  | |
| `day6/trees/session2/ConstructTree.java` | Tree recursion / hashmap index | A |  |  |  | |
| `day6/trees/session2/SerializeAndDeserializeBinaryTree.java` | Tree BFS/DFS serialization | A |  |  |  | |
| `day6/trees/session3/BinaryTree.java` | Core tree patterns | A |  |  |  | |
| `day6/trees/session3/InvertBinaryTree.java` | Tree DFS/BFS | A |  |  |  | |
| `day6/trees/session3/KthSmallestElementInBST.java` | BST inorder | A |  |  |  | |
| `day6/trees/session3/ValidateBST.java` | DFS bounds / inorder | A |  |  |  | |
| `day6/trees/session4/BinaryTreePathProblems.java` | Tree path DFS / global answer | A |  |  |  | |
| `day8/graph/session1/FloodFill.java` | Matrix DFS/BFS | A |  |  |  | |
| `day8/graph/session1/Islands.java` | Matrix DFS/BFS components | A |  |  |  | |
| `day8/graph/session1/Matrix01.java` | Multi-source BFS | A |  |  |  | |
| `day8/graph/session1/RottenOranges.java` | Multi-source BFS | A |  |  |  | |
| `day8/graph/session1/WordSearch.java` | DFS backtracking | A |  |  |  | |
| `day8/graph/session2/CloneGraph.java` | Graph DFS/BFS clone | A |  |  |  | |
| `day8/graph/session2/CourseSchedule.java` | Topological sort / cycle | A |  |  |  | |
| `day8/graph/session2/GraphBipartite.java` | BFS/DFS coloring | A |  |  |  | |
| `day8/graph/session3/WordLadder.java` | BFS shortest path | A |  |  |  | |

## Priority B - Stabilize Next

These often appear, but they should come after Priority A is reliable.

| File | Main pattern | ROI | Recall | Code | Variant | Notes |
|---|---|---|---:|---:|---:|---|
| `day9/dp/session1/KadaneMaxSubArray.java` | Kadane / DP | B |  |  |  | |
| `day1/Arrays/session1/SortColors.java` | Partition / Dutch flag | B |  |  |  | |
| `day1/Arrays/session1/SpiralMatrix.java` | Matrix boundary traversal | B |  |  |  | |
| `day1/Arrays/session3/StockSeries1.java` | Greedy / DP states | B |  |  |  | |
| `day1/Arrays/session3/StockSeries2.java` | Stock DP variants | B |  |  |  | |
| `day2/session3/MaximumProfitInJobScheduling.java` | DP + binary search | B |  |  |  | |
| `day2/session3/TimeBasedKeyValueStore.java` | HashMap + binary search | B |  |  |  | |
| `day9/dp/session1/GasStation.java` | Greedy | B |  |  |  | |
| `day1/Arrays/session4/Intervals/IntervalGreedyByEnd.java` | Intervals / sorting | B |  |  |  | |
| `day3/session3/LongestPalindrome.java` | Hash/frequency | B |  |  |  | |
| `day3/session3/LongestPalindromicSubstring.java` | Expand around center | B |  |  |  | |
| `day3/session3/StringToIntegerAtoi.java` | Parsing / edge cases | B |  |  |  | |
| `day5/stack/session2/MinStackDesign.java` | Stack design | B |  |  |  | |
| `day5/stack/session3/BasicCalculator.java` | Stack / expression parsing | B |  |  |  | |
| `day5/stack/session3/EvalRPN.java` | Stack | B |  |  |  | |
| `day5/stack/session2/StackQueue.java` | Stack/queue design | B |  |  |  | |
| `day6/trees/session2/BurnBinaryTree.java` | Tree + graph BFS | B |  |  |  | |
| `day6/trees/session2/RecoverBST.java` | BST inorder | B |  |  |  | |
| `day7/session1/heap/AwardTopKHotels.java` | Heap / ranking | B |  |  |  | |
| `day7/session1/heap/HeapSort.java` | Heap fundamentals | B |  |  |  | |
| `day7/session1/heap/KClosestPointsToOrigin.java` | Heap / quickselect | B |  |  |  | |
| `day7/session1/heap/KthLargestInStream.java` | Min-heap size K | B |  |  |  | |
| `day7/session1/heap/Median.java` | Two heaps | B |  |  |  | |
| `day7/session1/heap/MovingAverage.java` | Queue / stream | B |  |  |  | |
| `day7/session1/heap/TaskScheduler.java` | Greedy / heap | B |  |  |  | |
| `day7/session1/heap/TopKFrequentElements.java` | Frequency + heap/bucket | B |  |  |  | |
| `day8/graph/session1/ColoringABorder.java` | Matrix DFS | B |  |  |  | |
| `day8/graph/session2/NetworkDelayTime.java` | Dijkstra / graph | B |  |  |  | |
| `day8/graph/session3/AccountsMerge.java` | Union Find / graph | B |  |  |  | |
| `day8/graph/session3/KHighestRankedItemsWithinAPriceRange.java` | BFS + sorting | B |  |  |  | |
| `day8/graph/session3/MinHTree.java` | Topological trimming | B |  |  |  | |
| `day9/dp/session1/ClimbingStairsFib.java` | 1D DP | B |  |  |  | |
| `day9/dp/session1/HouseRobber.java` | 1D DP | B |  |  |  | |
| `day9/dp/session1/UniquePaths.java` | Grid DP | B |  |  |  | |
| `day9/dp/session2/CoinChange.java` | Unbounded knapsack DP | B |  |  |  | |
| `day9/dp/session2/EditDistance.java` | 2D DP | B |  |  |  | |
| `day9/dp/session2/LIS.java` | DP / patience sorting | B |  |  |  | |
| `day9/dp/session2/PartitionEqualSubsetSum.java` | 0/1 knapsack DP | B |  |  |  | |
| `day10/session1/trie/TriePrefix.java` | Trie | B |  |  |  | |
| `day10/session1/trie/TrieWordDictionary.java` | Trie + DFS wildcard | B |  |  |  | |
| `day10/session1/trie/WordSearchII.java` | Trie + backtracking | B |  |  |  | |
| `day11/backtracking/BacktrackingRecursion.java` | Backtracking model | B |  |  |  | |
| `day11/backtracking/session1/CombinationSum.java` | Backtracking reuse | B |  |  |  | |
| `day11/backtracking/session1/LetterCombinationsOfAPhoneNumber.java` | Backtracking / mapping | B |  |  |  | |
| `day11/backtracking/session1/Permutations.java` | Backtracking permutations | B |  |  |  | |
| `day11/backtracking/session1/Subsets.java` | Backtracking subsets | B |  |  |  | |

## Priority C - Review After Core Is Stable

These are useful, but do not let them delay mastery of Priority A.

| File | Main pattern | ROI | Recall | Code | Variant | Notes |
|---|---|---|---:|---:|---:|---|
| `CheatSheet.java` | Broad notes | C |  |  |  | Use for last-mile reminders only |
| `Main.java` | Project entry | C |  |  |  | Not an interview target |
| `day7/session2/KmpPatterns.java` | KMP string matching | C |  |  |  | |
| `day7/session2/LongestHappyPrefix.java` | KMP / rolling hash | C |  |  |  | |
| `day7/session2/ZFunction.java` | Z algorithm | C |  |  |  | |
| `day10/session1/trie/HotelReviews.java` | Trie / ranking | C |  |  |  | |
| `day10/session1/trie/MaximumXOR.java` | Binary trie / bit | C |  |  |  | |
| `day10/session2/AddBinary.java` | Bit/string addition | C |  |  |  | |
| `day10/session2/CountPrimes.java` | Math / sieve | C |  |  |  | |
| `day10/session2/CountUniqueChars.java` | Contribution counting | C |  |  |  | |
| `design/lld/ApiIntegrationExample.java` | LLD/API integration | C |  |  |  | Role dependent |
| `design/lld/DesignFraudPatternDetection.java` | LLD / domain modeling | C |  |  |  | Role dependent |
| `design/lld/DesignRedis.java` | LLD / data structures | C |  |  |  | Role dependent |
| `design/lld/DesignTokenBucketRateLimiter.java` | LLD / rate limiting | C |  |  |  | Role dependent |
| `design/lld/DesignUrlShortner.java` | LLD / URL shortener | C |  |  |  | Role dependent |

## Random Drill Queue

Use this when you do not know what to practice.

1. Pick one Priority A file randomly.
2. Do not open the solution.
3. Write pattern, invariant, edge cases.
4. Code from blank.
5. Open the Java chapter and compare.
6. Add a grade in the table.
7. If grade is below 3, repeat after 2 days.

## Minimum Mastery Targets

Before a real interview:

- At least 40 Priority A rows at grade 4+.
- All linked-list rows at grade 4+.
- All sliding-window rows at grade 4+.
- All binary-search rows at grade 4+.
- At least 8 tree rows at grade 4+.
- At least 8 graph rows at grade 4+.
- At least 5 DP rows at grade 3+.

## Stuck Pattern Conversion

When a new problem feels random, convert it:

| If you see | Ask |
|---|---|
| repeated pair checks | Can a map remember complement/count/index? |
| contiguous range | Can a window expand/shrink? |
| sorted or feasible threshold | Can binary search eliminate half/range? |
| minimum moves | Is it unweighted BFS? |
| all paths/combos | Is it backtracking? |
| optimal min/max/count | What is the DP state? |
| dependencies | Is it topo sort? |
| grouping/merging identities | Is it union find? |
| next greater/smaller | Is it monotonic stack? |
| top/best K | Is it heap or quickselect? |

## Review Cadence

After solving a row:

- Review after 1 day.
- Review after 3 days.
- Review after 7 days.
- Review after 14 days.

Do not mark a problem stable until it survives the 7-day review from blank code.
