# Dynamic Programming

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

Name the state, base case, transition, and iteration order before writing loops.

## Interview Move

Naive recursion repeats states; DP caches each state and reuses transitions.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 39 | Phase 2 - Strong Core | House Robber | 1D DP | [Java](../../../src/main/java/org/chijai/day9/dp/session1/HouseRobber.java) | [LC](https://leetcode.com/problems/house-robber/) | At each house choose max(skip current, rob current plus best before previous). | For each money, next = max(prev1, prev2 + money); shift prev2=prev1, prev1=next. |
| 40 | Phase 2 - Strong Core | Coin Change | Unbounded knapsack DP | [Java](../../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) | [LC](https://leetcode.com/problems/coin-change/) | dp[amount] is the fewest coins needed; each coin relaxes reachable amounts. | Initialize dp[0]=0 and others INF; for amount 1..target, try every coin. |
| 48 | Phase 2 - Strong Core | Unique Paths | Grid DP | [Java](../../../src/main/java/org/chijai/day9/dp/session1/UniquePaths.java) | [LC](https://leetcode.com/problems/unique-paths/) | Ways to a cell equal ways from top plus ways from left. | Initialize first row/column to 1, fill dp[r][c] = dp[r-1][c] + dp[r][c-1]. |
| 49 | Phase 2 - Strong Core | Partition Equal Subset Sum | 0/1 knapsack DP | [Java](../../../src/main/java/org/chijai/day9/dp/session2/PartitionEqualSubsetSum.java) | [LC](https://leetcode.com/problems/partition-equal-subset-sum/) | Partition is possible only if some subset reaches total/2. | If total odd return false; update boolean dp from target down to num for each num. |
| 50 | Phase 2 - Strong Core | Longest Increasing Subsequence | DP / patience sorting | [Java](../../../src/main/java/org/chijai/day9/dp/session2/LIS.java) | [LC](https://leetcode.com/problems/longest-increasing-subsequence/) | tails[len] stores the smallest possible tail for an increasing subsequence of that length. | For each x, lower_bound in tails and replace; answer is tails size. |
| 90 | Phase 3 - Important | Kadane Max Sub Array | Kadane / DP | [Java](../../../src/main/java/org/chijai/day1/Arrays/session1/KadaneMaxSubArray.java) | - | Best subarray ending here is either current alone or previous best ending here plus current. | cur = max(x, cur + x); best = max(best, cur) for every element. |
| 91 | Phase 3 - Important | Maximum Profit In Job Scheduling | DP + binary search | [Java](../../../src/main/java/org/chijai/day2/session3/MaximumProfitInJobScheduling.java) | [LC](https://leetcode.com/problems/maximum-profit-in-job-scheduling/) | Sort jobs by end time; dp[i] is best profit up to i, with binary search for compatible previous job. | Sort by end, for each job compute max(skip, profit + dp[lastNonOverlapping]). |
| 92 | Phase 3 - Important | Best Time To Buy And Sell Stock | Greedy / DP states | [Java](../../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java) | [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/) | Track the lowest price so far; today's profit is price minus that minimum. | For each price, update minPrice, then best = max(best, price - minPrice). |
| 151 | Phase 5 - If Time | Climbing Stairs Fib | 1D DP | [Java](../../../src/main/java/org/chijai/day9/dp/session1/ClimbingStairsFib.java) | - | Ways to step n equals ways to n-1 plus ways to n-2. | Iterate two rolling values for ways to previous one and two steps. |
| 152 | Phase 5 - If Time | Edit Distance | 2D DP | [Java](../../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) | [LC](https://leetcode.com/problems/edit-distance/) | dp[i][j] is edits to convert first i chars of word1 to first j chars of word2. | Initialize empty-string row/column; if chars equal copy diagonal else 1 + min(insert, delete, replace). |
| 167 | Phase 5 - If Time | Stock Series2 | Stock DP variants | [Java](../../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) | - | For unlimited transactions, add every positive day-to-day price difference. | Scan prices and add max(0, prices[i] - prices[i-1]). |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.