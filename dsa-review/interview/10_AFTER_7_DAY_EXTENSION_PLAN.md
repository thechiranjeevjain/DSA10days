# After 7 Days Extension Plan

Purpose: continue after the 7-day sprint without pretending that raw coverage equals interview readiness.

This file is generated from the same ranked metadata and recursive LeetCode source scan as the cockpit.

## Cutoff Rule

- If interview is in 1-2 days: stop new coverage around rank 70 and repair misses.
- If interview is in 1 week: target ranks 1-150 plus spaced review, not all 220.
- If interview is in 2+ weeks: finish ranks 151-216 and source-only extras, but only after top 100 recall is stable.
- If ranks 1-50 contain repeated RED, do not continue this extension. Rebuild fundamentals first.

## Daily Shape

- 09:00-10:00: due reviews from `review/review.json`.
- 10:00-15:00: 15 new or weak-tail problems, three 18-minute reps per hour.
- 15:00-16:00: repair the worst three misses from today.
- 16:00-17:00: one random mock from ranks 1-150 so old fundamentals stay hot.

---

## Day 8 - finish remaining Phase 4 depth without touching weak fundamentals

| Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Next Review |
|---:|---|---|---|---|---|---|---|---|
| 151 | Sequence Reconstruction | [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/sequence-reconstruction/) | Topological Sort | Unique topological order | The target is uniquely reconstructible only when Kahn's frontier has exactly one node and that node equals nums[index] at every step. |  |  |  |
| 152 | Sort Items by Groups Respecting Dependencies | [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/sort-items-by-groups-respecting-dependencies/) | Topological Sort | Two-level topological sort | VERIFY FROM SOURCE - the local chapter records that item and group dependencies require two coordinated topological orders, but it does not provide a complete accepted implementation. |  |  |  |
| 153 | Spiral Matrix | [Java](../../src/main/java/org/chijai/day1/Arrays/session1/SpiralMatrix.java) / [LC](https://leetcode.com/problems/spiral-matrix/) | Basics / Implementation | Matrix boundary traversal | Shrink top, bottom, left, and right boundaries after traversing each side. |  |  |  |
| 154 | String To Integer Atoi | [Java](../../src/main/java/org/chijai/day3/session3/StringToIntegerAtoi.java) / [LC](https://leetcode.com/problems/string-to-integer-atoi/) | Basics / Implementation | Parsing / edge cases | Parse sign and digits once, clamping before overflow. |  |  |  |
| 155 | Repeated Substring Pattern | [Java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java) / [LC](https://leetcode.com/problems/repeated-substring-pattern/) | Math / Bit / String | KMP string matching | A repeated pattern exists when the final LPS leaves a block length that divides n. |  |  |  |
| 156 | Maximum XOR of Two Numbers in an Array | [Java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) / [LC](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/) | Trie | Binary trie / bit | Binary trie chooses the opposite bit greedily to maximize each XOR bit from high to low. |  |  |  |
| 157 | Design A Stack With Increment Operation | [Java](../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) / [LC](https://leetcode.com/problems/design-a-stack-with-increment-operation/) | Stack / Monotonic Stack | Stack design | Lazy increment stores pending additions at the boundary index instead of touching k items. |  |  |  |
| 158 | Longest Happy Prefix | [Java](../../src/main/java/org/chijai/day7/session2/LongestHappyPrefix.java) / [LC](https://leetcode.com/problems/longest-happy-prefix/) | Math / Bit / String | KMP / rolling hash | The answer is the final LPS value: longest proper prefix that is also suffix. |  |  |  |
| 159 | Longest Common Prefix | [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) / [LC](https://leetcode.com/problems/longest-common-prefix/) | Trie | Single-branch prefix walk | The common prefix continues only while the trie path has exactly one child and the current node is not terminal. |  |  |  |
| 160 | Longest Word in Dictionary | [Java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) / [LC](https://leetcode.com/problems/longest-word-in-dictionary/) | Trie | Every-prefix-terminal trie | A candidate is legal only if every trie node on its path is terminal, meaning every prefix is also a word. |  |  |  |
| 161 | Replace Words | [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) / [LC](https://leetcode.com/problems/replace-words/) | Trie | Shortest terminal prefix | While scanning a sentence word, the first terminal trie node is its shortest dictionary root. |  |  |  |
| 162 | Search Suggestions System | [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) / [LC](https://leetcode.com/problems/search-suggestions-system/) | Trie | Prefix node + lexicographic DFS top 3 | For each typed prefix, suggestions are the first at most three terminal words below that prefix in lexicographic DFS order. |  |  |  |
| 163 | Short Encoding of Words | [Java](../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) / [LC](https://leetcode.com/problems/short-encoding-of-words/) | Trie | Reversed suffix trie | Only words that are not suffixes of a longer encoded word add word.length + 1 characters. |  |  |  |
| 164 | Map Sum Pairs | [Java](../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) / [LC](https://leetcode.com/problems/map-sum-pairs/) | Trie | Prefix aggregate with overwrite delta | node.sum is the total current value of every key passing through that prefix; updating an existing key changes each prefix by delta only. |  |  |  |
| 165 | Maximum XOR With an Element From Array | [Java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) / [LC](https://leetcode.com/problems/maximum-xor-with-an-element-from-array/) | Trie | Binary trie / bit | Offline sort queries by limit; insert eligible numbers into a bitwise trie before maximizing XOR. |  |  |  |

End-of-day gate: new attempted __; GREEN __; YELLOW __; RED __; carry-forward repair __.

---

## Day 9 - pattern transfer across medium-frequency variants

| Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Next Review |
|---:|---|---|---|---|---|---|---|---|
| 166 | Maximum Genetic Difference Query | [Java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) / [LC](https://leetcode.com/problems/maximum-genetic-difference-query/) | Trie | Binary trie / bit | DFS the tree while the current root-to-node path is stored in a bitwise trie. |  |  |  |
| 167 | Count Pairs With XOR in a Range | [Java](../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) / [LC](https://leetcode.com/problems/count-pairs-with-xor-in-a-range/) | Trie | Binary trie / bit | Count pairs with XOR < bound using bitwise trie prefixes, then subtract low from high+1. |  |  |  |
| 168 | Climbing Stairs Fib | [Java](../../src/main/java/org/chijai/day9/dp/session1/ClimbingStairsFib.java) | Dynamic Programming | 1D DP | Ways to step n equals ways to n-1 plus ways to n-2. |  |  |  |
| 169 | Edit Distance | [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/edit-distance/) | Dynamic Programming | 2D DP | dp[i][j] is edits to convert first i chars of word1 to first j chars of word2. |  |  |  |
| 170 | Distinct Subsequences | [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/distinct-subsequences/) | Dynamic Programming | 2D DP | dp[i][j] counts ways first i source chars form first j target chars. |  |  |  |
| 171 | Add Binary | [Java](../../src/main/java/org/chijai/day10/session2/AddBinary.java) / [LC](https://leetcode.com/problems/add-binary/) | Math / Bit / String | Bit/string addition | Add bits from right to left with carry, exactly like decimal addition. |  |  |  |
| 172 | Count Primes | [Java](../../src/main/java/org/chijai/day10/session2/CountPrimes.java) / [LC](https://leetcode.com/problems/count-primes/) | Math / Bit / String | Math / sieve | Sieve marks multiples of each discovered prime starting at p*p. |  |  |  |
| 173 | Count Unique Characters of All Substrings of a Given String | [Java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java) / [LC](https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/) | Math / Bit / String | Contribution counting | Each character occurrence contributes by distance to the previous same char times distance to the next one. |  |  |  |
| 174 | Award Top K Hotels | [Java](../../src/main/java/org/chijai/day7/session1/heap/AwardTopKHotels.java) | Heap / Priority Queue | Heap / ranking | Score each hotel by keyword hits, then rank by score and tie-breaker. |  |  |  |
| 175 | Shortest Palindrome | [Java](../../src/main/java/org/chijai/day7/session2/KmpPatterns.java) / [LC](https://leetcode.com/problems/shortest-palindrome/) | Math / Bit / String | KMP string matching | Find the longest palindromic prefix, then prepend the reverse of the remaining suffix. |  |  |  |
| 176 | Reverse Linked List II | [Java](../../src/main/java/org/chijai/day4/LinkedList/session2/ReverseLinkedListNodesK.java) / [LC](https://leetcode.com/problems/reverse-linked-list-ii/) | Linked List Pointers | Linked-list reversal groups | Use a dummy and reverse exactly the sublist between left and right. |  |  |  |
| 177 | Path Sum II | [Java](../../src/main/java/org/chijai/day6/trees/session4/BinaryTreePathProblems.java) / [LC](https://leetcode.com/problems/path-sum-ii/) | Tree DFS / Recursion | Tree path DFS / global answer | Backtrack the current root-to-leaf path and copy it when the target is hit. |  |  |  |
| 178 | Lowest Common Ancestor Of A Binary Tree II | [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) / [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-ii/) | Tree DFS / Recursion | Tree DFS return contract | Same split-point idea, but verify both targets actually exist. |  |  |  |
| 179 | Lowest Common Ancestor Of A Binary Tree III | [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) / [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/) | Tree DFS / Recursion | Tree DFS return contract | With parent pointers, walk ancestors or switch pointers like linked-list intersection. |  |  |  |
| 180 | Lowest Common Ancestor Of A Binary Tree IV | [Java](../../src/main/java/org/chijai/day6/trees/session1/LCA.java) / [LC](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iv/) | Tree DFS / Recursion | Tree DFS return contract | For many target nodes, current node is answer when multiple target paths meet. |  |  |  |

End-of-day gate: new attempted __; GREEN __; YELLOW __; RED __; carry-forward repair __.

---

## Day 10 - secondary tree, graph, stack, and linked-list variants

| Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Next Review |
|---:|---|---|---|---|---|---|---|---|
| 181 | Permutations II | [Java](../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) / [LC](https://leetcode.com/problems/permutations-ii/) | Backtracking / Combinatorial DFS | Sorted duplicate-aware permutations | used[i] owns an index on the current path; after sorting, equal values are tried in a fixed same-depth order. |  |  |  |
| 182 | Course Schedule IV | [Java](../../src/main/java/org/chijai/day8/graph/session2/CourseSchedule.java) / [LC](https://leetcode.com/problems/course-schedule-iv/) | Topological Sort | Dependency transitive closure | reachable[a][b] means course a is a direct or indirect prerequisite of course b. |  |  |  |
| 183 | Encode And Decode Tinyurl | [Java](../../src/main/java/org/chijai/design/lld/DesignUrlShortner.java) / [LC](https://leetcode.com/problems/encode-and-decode-tinyurl/) | Design Data Structures | LLD / URL shortener | Encode creates a stable short key mapped to the original URL; decode is a map lookup. |  |  |  |
| 184 | Design Circular Queue | [Java](../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) / [LC](https://leetcode.com/problems/design-circular-queue/) | Design Data Structures | Stack/queue design | Circular queue uses head, size, and modulo arithmetic to reuse fixed array slots. |  |  |  |
| 185 | Hotel Reviews | [Java](../../src/main/java/org/chijai/day10/session1/trie/HotelReviews.java) | Trie | Trie / ranking | Use trie or keyword set to count good words per review, then rank hotels by score. |  |  |  |
| 186 | Design Fraud Pattern Detection | [Java](../../src/main/java/org/chijai/design/lld/DesignFraudPatternDetection.java) | Design Data Structures | LLD / domain modeling | Define which transaction events are retained and which rule/window makes a pattern fraudulent. |  |  |  |
| 187 | Api Integration Example | [Java](../../src/main/java/org/chijai/design/lld/ApiIntegrationExample.java) | Design Data Structures | LLD/API integration | Model request, response, retry, timeout, and idempotency boundaries explicitly. |  |  |  |
| 188 | Design Redis | [Java](../../src/main/java/org/chijai/design/lld/DesignRedis.java) | Design Data Structures | LLD / data structures | Key-value operations need storage, expiry metadata, and eviction/cleanup policy. |  |  |  |
| 189 | Design Token Bucket Rate Limiter | [Java](../../src/main/java/org/chijai/design/lld/DesignTokenBucketRateLimiter.java) | Design Data Structures | LLD / rate limiting | A bucket refills by elapsed time and each request consumes one token if available. |  |  |  |
| 190 | Best Time to Buy and Sell Stock with Transaction Fee | [Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) / [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/) | Dynamic Programming | Stock DP variants | Fee changes the sell transition; hold/cash states prevent double-counting fees. |  |  |  |
| 191 | Best Time to Buy and Sell Stock with Cooldown | [Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) / [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/) | Dynamic Programming | Stock DP variants | Cooldown creates three states: hold, sold today, and rest. |  |  |  |
| 192 | Best Time to Buy and Sell Stock III | [Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java) / [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/) | Dynamic Programming | Greedy / DP states | Four states track first buy, first sell, second buy, second sell. |  |  |  |
| 193 | Best Time to Buy and Sell Stock IV | [Java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries2.java) / [LC](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/) | Dynamic Programming | Stock DP variants | For k transactions, each transaction layer has a hold and cash state. |  |  |  |
| 194 | Distinct Subsequences II | [Java](../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java) / [LC](https://leetcode.com/problems/distinct-subsequences-ii/) | Dynamic Programming | Contribution counting | Each char doubles subsequences, then subtracts subsequences counted before its previous occurrence. |  |  |  |
| 195 | Word Break | [Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) / [LC](https://leetcode.com/problems/word-break/) | Dynamic Programming | Unbounded knapsack DP | dp[i] means prefix s[0..i) can be segmented into dictionary words. |  |  |  |

End-of-day gate: new attempted __; GREEN __; YELLOW __; RED __; carry-forward repair __.

---

## Day 11 - lower-ROI but useful breadth; keep attempts timed

| Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Next Review |
|---:|---|---|---|---|---|---|---|---|
| 196 | Interleaving String | [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/interleaving-string/) | Dynamic Programming | 2D DP | dp[i][j] says s3 prefix i+j can be formed by prefixes of s1 and s2. |  |  |  |
| 197 | Longest Common Subsequence | [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/longest-common-subsequence/) | Dynamic Programming | 2D DP | dp[i][j] is the best subsequence length between two prefixes. |  |  |  |
| 198 | Delete Operation for Two Strings | [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/delete-operation-for-two-strings/) | Dynamic Programming | 2D DP | Minimum deletions equals removing everything not in the LCS. |  |  |  |
| 199 | Longest Palindromic Subsequence | [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/longest-palindromic-subsequence/) | Dynamic Programming | 2D DP | dp[l][r] is best palindrome subsequence inside s[l..r]. |  |  |  |
| 200 | Minimum ASCII Delete Sum for Two Strings | [Java](../../src/main/java/org/chijai/day9/dp/session2/EditDistance.java) / [LC](https://leetcode.com/problems/minimum-ascii-delete-sum-for-two-strings/) | Dynamic Programming | 2D DP | dp[i][j] is minimum ASCII deletion cost to make two prefixes equal. |  |  |  |
| 201 | Climbing Stairs | [Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) / [LC](https://leetcode.com/problems/climbing-stairs/) | Dynamic Programming | Unbounded knapsack DP | Ways to reach n comes from n-1 plus n-2, with two rolling counts. |  |  |  |
| 202 | Min Cost Climbing Stairs | [Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) / [LC](https://leetcode.com/problems/min-cost-climbing-stairs/) | Dynamic Programming | Unbounded knapsack DP | Cost to stand on step i is cost[i] plus min(previous one, previous two). |  |  |  |
| 203 | Perfect Squares | [Java](../../src/main/java/org/chijai/day9/dp/session2/CoinChange.java) / [LC](https://leetcode.com/problems/perfect-squares/) | Dynamic Programming | Unbounded knapsack DP | dp[x] is the fewest square numbers summing to x; try each square as the last move. |  |  |  |
| 204 | Number of Longest Increasing Subsequence | [Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) / [LC](https://leetcode.com/problems/number-of-longest-increasing-subsequence/) | Dynamic Programming | DP / patience sorting | Track both LIS length ending at i and how many ways achieve that length. |  |  |  |
| 205 | Russian Doll Envelopes | [Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) / [LC](https://leetcode.com/problems/russian-doll-envelopes/) | Dynamic Programming | DP / patience sorting | Sort width ascending, height descending for equal width, then LIS on heights. |  |  |  |
| 206 | Maximum Length of Pair Chain | [Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) / [LC](https://leetcode.com/problems/maximum-length-of-pair-chain/) | Intervals / Sorting Greedy | DP / patience sorting | Sort pairs by end and take the next pair whose start is after the current end. |  |  |  |
| 207 | Longest Continuous Increasing Subsequence | [Java](../../src/main/java/org/chijai/day9/dp/session2/LIS.java) / [LC](https://leetcode.com/problems/longest-continuous-increasing-subsequence/) | Sliding Window | DP / patience sorting | Continuous means subarray, so reset the current streak whenever nums[i] <= nums[i-1]. |  |  |  |

End-of-day gate: new attempted __; GREEN __; YELLOW __; RED __; carry-forward repair __.

---

## Day 12 - ranked cleanup plus recursive source-only LeetCode inventory

| Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Next Review |
|---:|---|---|---|---|---|---|---|---|
| source-only | Constrained Subsequence Sum | [LC](https://leetcode.com/problems/constrained-subsequence-sum/) / [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) | Sliding Window | Sliding Window | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Jump Game VI | [LC](https://leetcode.com/problems/jump-game-vi/) / [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) | Sliding Window | Sliding Window | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit | [LC](https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/) / [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) | Sliding Window | Sliding Window | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Longest Repeating Character Replacement | [LC](https://leetcode.com/problems/longest-repeating-character-replacement/) / [LongestRepeatingCharacterReplacement.java](../../src/main/java/org/chijai/day3/session1/LongestRepeatingCharacterReplacement.java) | Sliding Window | Sliding Window | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Max Value of Equation | [LC](https://leetcode.com/problems/max-value-of-equation/) / [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) | Sliding Window | Sliding Window | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Maximum Number of Robots Within Budget | [LC](https://leetcode.com/problems/maximum-number-of-robots-within-budget/) / [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) | Sliding Window | Sliding Window | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Shortest Subarray with Sum at Least K | [LC](https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/) / [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day3/session1/SlidingWindowMaximum.java), [SlidingWindowMaximum.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SlidingWindowMaximum.java) | Sliding Window | Sliding Window | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Remove K Digits | [LC](https://leetcode.com/problems/remove-k-digits/) / [RemoveKDigits.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/RemoveKDigits.java) | Stack / Monotonic Stack | Stack / Monotonic Stack | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Sum of Subarray Minimums | [LC](https://leetcode.com/problems/sum-of-subarray-minimums/) / [SumOfSubarrayMinimums.java](../../src/main/java/org/chijai/day5/stack/session1/monotonic/SumOfSubarrayMinimums.java) | Stack / Monotonic Stack | Stack / Monotonic Stack | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Ipo | [LC](https://leetcode.com/problems/ipo/) / [IPO.java](../../src/main/java/org/chijai/day7/session1/heap/IPO.java) | Heap / Priority Queue | Heap / Priority Queue | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Sliding Window Median | [LC](https://leetcode.com/problems/sliding-window-median/) / [SlidingWindowMedian.java](../../src/main/java/org/chijai/day7/session1/heap/SlidingWindowMedian.java) | Heap / Priority Queue | Heap / Priority Queue | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Car Pooling | [LC](https://leetcode.com/problems/car-pooling/) / [BoundaryDelta.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/BoundaryDelta.java) | Intervals / Sorting Greedy | Intervals / Sorting Greedy | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | N-Queens | [LC](https://leetcode.com/problems/n-queens/) / [NQueens.java](../../src/main/java/org/chijai/day11/backtracking/session1/NQueens.java) | Backtracking / Combinatorial DFS | Backtracking / Combinatorial DFS | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Sudoku Solver | [LC](https://leetcode.com/problems/sudoku-solver/) / [SudokuSolver.java](../../src/main/java/org/chijai/day11/backtracking/session1/SudokuSolver.java) | Backtracking / Combinatorial DFS | Backtracking / Combinatorial DFS | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Stock Price Fluctuation | [LC](https://leetcode.com/problems/stock-price-fluctuation/) / [StockPriceFluctuation.java](../../src/main/java/org/chijai/trading/StockPriceFluctuation.java) | Dynamic Programming | Dynamic Programming | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Missing Number | [LC](https://leetcode.com/problems/missing-number/) / [MissingNumber.java](../../src/main/java/org/chijai/day10/session2/MissingNumber.java) | Basics / Implementation | Basics / Implementation | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Missing Ranges | [LC](https://leetcode.com/problems/missing-ranges/) / [MissingRanges.java](../../src/main/java/org/chijai/trading/MissingRanges.java) | Basics / Implementation | Basics / Implementation | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Number of Orders in the Backlog | [LC](https://leetcode.com/problems/number-of-orders-in-the-backlog/) / [NumberOfOrdersInTheBacklog.java](../../src/main/java/org/chijai/trading/NumberOfOrdersInTheBacklog.java) | Basics / Implementation | Basics / Implementation | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Design A Leaderboard | [LC](https://leetcode.com/problems/design-a-leaderboard/) / [DesignALeaderboard.java](../../src/main/java/org/chijai/design/lld/DesignALeaderboard.java) | Design Data Structures | Design Data Structures | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Design an Ordered Stream | [LC](https://leetcode.com/problems/design-an-ordered-stream/) / [DesignOrderedStream.java](../../src/main/java/org/chijai/design/lld/DesignOrderedStream.java) | Design Data Structures | Design Data Structures | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Design Hit Counter | [LC](https://leetcode.com/problems/design-hit-counter/) / [DesignHitCounter.java](../../src/main/java/org/chijai/design/lld/DesignHitCounter.java) | Design Data Structures | Design Data Structures | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |
| source-only | Design Parking System | [LC](https://leetcode.com/problems/design-parking-system/) / [DesignParkingSystem.java](../../src/main/java/org/chijai/design/lld/DesignParkingSystem.java) | Design Data Structures | Design Data Structures | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |

End-of-day gate: new attempted __; GREEN __; YELLOW __; RED __; carry-forward repair __.

## Recircling Rule

- After ranks 1-216 are touched once, stop adding lists.
- Recircle by weakest signal: repeated RED -> old YELLOW -> random rank 1-150 mock -> source-only extras.
- A problem graduates only when you can explain brute force -> bottleneck -> pattern -> invariant -> code -> dry run without opening Java.
- The target is fast retrieval and adaptation, not finishing a file.