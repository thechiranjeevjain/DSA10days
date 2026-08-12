# Dynamic Programming

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

Fix dp state meaning, base cases, transition, and iteration order before coding.

## Interview Move

Naive recursion repeats states; DP caches each state and reuses transitions.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 110 | Phase 3 - Important | House Robber | 1D DP | [Java](../../../src/main/java/org/chijai/day9/dp/session1/HouseRobber.java) | [LC](https://leetcode.com/problems/house-robber/) | Fix dp state meaning, base cases, transition, and iteration order before coding. | Initialize base states, fill states in dependency order, return target state. |
| 111 | Phase 4 - Secondary | Unique Paths | Grid DP | [Java](../../../src/main/java/org/chijai/day9/dp/session1/UniquePaths.java) | [LC](https://leetcode.com/problems/unique-paths/) | Fix dp state meaning, base cases, transition, and iteration order before coding. | Initialize base states, fill states in dependency order, return target state. |
| 112 | Phase 4 - Secondary | Coin Change | Unbounded knapsack DP | [Java](../../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) | [LC](https://leetcode.com/problems/coin-change/) | Fix dp state meaning, base cases, transition, and iteration order before coding. | Initialize base states, fill states in dependency order, return target state. |
| 113 | Phase 4 - Secondary | Partition Equal Subset Sum | 0/1 knapsack DP | [Java](../../../src/main/java/org/chijai/day9/dp/session2/PartitionEqualSubsetSum.java) | [LC](https://leetcode.com/problems/partition-equal-subset-sum/) | Fix dp state meaning, base cases, transition, and iteration order before coding. | Initialize base states, fill states in dependency order, return target state. |
| 114 | Phase 4 - Secondary | Longest Increasing Subsequence | DP / patience sorting | [Java](../../../src/main/java/org/chijai/day9/dp/session2/LIS.java) | [LC](https://leetcode.com/problems/longest-increasing-subsequence/) | Fix dp state meaning, base cases, transition, and iteration order before coding. | Initialize base states, fill states in dependency order, return target state. |
| 122 | Phase 4 - Secondary | Kadane Max Sub Array | Kadane / DP | [Java](../../../src/main/java/org/chijai/day1/session1/KadaneMaxSubArray.java) | - | Fix dp state meaning, base cases, transition, and iteration order before coding. | Initialize base states, fill states in dependency order, return target state. |
| 123 | Phase 4 - Secondary | Maximum Profit In Job Scheduling | DP + binary search | [Java](../../../src/main/java/org/chijai/day2/session3/MaximumProfitInJobScheduling.java) | [LC](https://leetcode.com/problems/maximum-profit-in-job-scheduling/) | Sort jobs by end time; dp[i] is best profit up to i, with binary search for compatible previous job. | Sort by end, for each job compute max(skip, profit + dp[lastNonOverlapping]). |
| 124 | Phase 4 - Secondary | Best Time To Buy And Sell Stock | Greedy / DP states | [Java](../../../src/main/java/org/chijai/day1/session3/StockSeries1.java) | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/) | Fix dp state meaning, base cases, transition, and iteration order before coding. | Initialize base states, fill states in dependency order, return target state. |
| 151 | Phase 4 - Secondary | Climbing Stairs Fib | 1D DP | [Java](../../../src/main/java/org/chijai/day9/dp/session1/ClimbingStairsFib.java) | - | Fix dp state meaning, base cases, transition, and iteration order before coding. | Initialize base states, fill states in dependency order, return target state. |
| 152 | Phase 4 - Secondary | Edit Distance | 2D DP | [Java](../../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) | [LC](https://leetcode.com/problems/edit-distance/) | Fix dp state meaning, base cases, transition, and iteration order before coding. | Initialize base states, fill states in dependency order, return target state. |
| 156 | Phase 4 - Secondary | Stock Series2 | Stock DP variants | [Java](../../../src/main/java/org/chijai/day1/session3/StockSeries2.java) | - | Fix dp state meaning, base cases, transition, and iteration order before coding. | Initialize base states, fill states in dependency order, return target state. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.