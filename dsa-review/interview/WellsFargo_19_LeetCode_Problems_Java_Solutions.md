# Wells Fargo — 19 LeetCode Problems with Java Solutions

All 19 problems from the supplied screenshot, ordered with common interview patterns first. Each entry includes the statement, an example, the derivation, the trade-offs, a spoken answer, complexity, and checks. The Java link opens the existing implementation for that problem. The screenshot is a practice list. [Live LeetCode company page](https://leetcode.com/company/wells-fargo/?favoriteSlug=wells-fargo-all&from=discuss_company_card).

Start with the Core 10, then use the Stretch 9 for extra pattern coverage. For a cold attempt, read the problem and example before opening the Java. The [eight-hour HackerRank guide](WellsFargo_HackerRank_8_Hour_Must_Cover.md) gives the overall schedule.

The Java links open the source file. Search for the class or method named under **Java entry point**. If your preview blocks local files, open `G:/TechStudyNotes/Codes/DSA10days` in your editor and use its file picker to open the linked filename.

## How to find a solution under interview pressure

1. **Notice.** Restate the result and the constraint that shapes it. Trace a small example and watch what changes at each step.
2. **Rule out.** State a correct baseline. Find its exact cost, repeated work, or counterexample. A valid baseline is a good place to start when you are stuck.
3. **Build.** Ask what one step needs from the past and what it can safely discard. Choose a data structure or recurrence that supports those operations.
4. **Verify.** Say what stays true after each step. Dry-run one awkward input, then code and give the complexity.

If recall stalls, reveal one derivation step at a time.

### When to stop exploring and start coding

- **Classify by the work the solution must do.** “Interval problem” is too broad. Merging needs the union, meeting rooms need peak overlap, and taxi rides need the best compatible total reward.
- **Compare the serious options.** Eliminate an approach when you have a counterexample, a cost that misses the target, or an operation the structure cannot do efficiently. If two approaches fit, choose the one you can implement and verify cleanly.
- **Commit when you can state the invariant and dry-run a discriminating case.** Know what you store, how it changes, and what the answer is. If the optimal idea has stalled, state the correct baseline and its cost, then ask which work repeats or which detail the contract lets you discard.
- **Give the interviewer the decision, reason, invariant, and one test.** Keep the initial explanation short. Mention a rejected approach when the comparison shows something important. Use the sample answers below as rehearsal prompts, then explain the same decisions in your own words.

## Problem index

### Core 10

- [20. Valid Parentheses](#20-valid-parentheses) · Easy
- [121. Best Time to Buy and Sell Stock](#121-best-time-to-buy-and-sell-stock) · Easy
- [56. Merge Intervals](#56-merge-intervals) · Medium
- [703. Kth Largest in a Stream](#703-kth-largest-in-a-stream) · Easy
- [200. Number of Islands](#200-number-of-islands) · Medium
- [713. Subarray Product Less Than K](#713-subarray-product-less-than-k) · Medium
- [1768. Merge Strings Alternately](#1768-merge-strings-alternately) · Easy
- [796. Rotate String](#796-rotate-string) · Easy
- [2696. Minimum Length After Removing Substrings](#2696-minimum-length-after-removing-substrings) · Easy
- [844. Backspace String Compare](#844-backspace-string-compare) · Easy

### Stretch 9

- [160. Intersection of Two Linked Lists](#160-intersection-of-two-linked-lists) · Easy
- [253. Meeting Rooms II](#253-meeting-rooms-ii) · Medium
- [146. LRU Cache](#146-lru-cache) · Medium
- [696. Count Binary Substrings](#696-count-binary-substrings) · Easy
- [2062. Count Vowel Substrings](#2062-count-vowel-substrings) · Easy
- [2904. Shortest and Lexicographically Smallest Beautiful String](#2904-shortest-and-lexicographically-smallest-beautiful-string) · Medium
- [93. Restore IP Addresses](#93-restore-ip-addresses) · Medium
- [2008. Maximum Earnings From Taxi](#2008-maximum-earnings-from-taxi) · Medium
- [460. LFU Cache](#460-lfu-cache) · Hard

## 20. Valid Parentheses

Easy · [LeetCode 20](https://leetcode.com/problems/valid-parentheses/) · [ValidParentheses.java](../../src/main/java/org/chijai/day5/stack/session3/ValidParentheses.java)

**Problem.** Given a string made up of `()`, `[]`, and `{}`, decide whether every opener is closed by the same bracket type in the correct nesting order.

**Example.** `"{[()]}" → true`, `"([)]" → false`.

### How to derive it in an interview

1. **Notice.** Pick a nested example such as `([{}])`. The first `}` must close the most recent unmatched opener. An earlier opener cannot close while the inner one is still open.
2. **Rule out.** Try counting balance. A single balance loses bracket types, and one balance per type still accepts `([)]`. The missing information is the order of pending closers.
3. **Build.** Write down the expected closers as you read openers. The only operations you need are adding one expectation and removing the most recent one. That is a stack.
4. **Verify.** Before reading each character, the stack lists the closers still owed, with the next one at the top. A mismatched or unexpected closer fails immediately. Empty stack at the end means the whole input closed.

**Pattern.** Stack.

### What to choose and what to skip

**What this reduces to and when to commit.** Nesting with type and order. Commit to a stack as soon as `([)]` shows that counts can balance while the nesting is invalid.

**Where to save time.** Drop frequency maps and recursive parsing for this contract. If the alphabet were just `(` and `)`, a depth counter with an early negative check would suffice.

**How I’d explain it aloud.** “A closer has to match the latest unmatched opener. I’ll push the expected closer and reject a mismatch immediately. At the end the stack must be empty. I’ll test `([)]` because the counts balance even though the nesting is wrong.”

**Trade-off.** A depth counter costs `O(1)` space and is sufficient for a single bracket type. Mixed nesting requires remembering the types in order, so the stack uses `O(depth)` logical state. The linked implementation uses `Stack` and copies the string to a character array. `ArrayDeque` with `charAt` preserves the same algorithm and avoids that full input copy.

**Java entry point.** `Optimal.isValid` in the linked DSA10days topic file.

**Complexity.** `O(n)` time, `O(n)` auxiliary space.

**Checks.** Check a leading closer and a trailing opener. Counting bracket types alone misses `([)]`.

## 121. Best Time to Buy and Sell Stock

Easy · [LeetCode 121](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/) · [StockSeries1.java](../../src/main/java/org/chijai/day1/Arrays/session3/StockSeries1.java)

**Problem.** Given daily prices, return the largest profit from buying once and selling on a later day. You may skip the trade and return zero.

**Example.** `[9, 2, 7, 1, 4] → 5` (buy at 2 and sell at 7).

### How to derive it in an interview

1. **Notice.** Fix a possible sell day. The only thing that matters about earlier days is the cheapest buy price. The order constraint matters more than finding the global minimum and maximum.
2. **Rule out.** Checking every earlier buy works but repeats the same search for each sell. Taking the global max minus the global min breaks on `[9, 2, 7]`, where the maximum comes first.
3. **Build.** As the sell day moves right, the previous minimum is still useful. Compare it with the new price and keep only the smaller one. Track the best profit separately so a late low price cannot erase an earlier winning trade.
4. **Verify.** After each day, `minimum` is the smallest price seen and `answer` is the best valid profit so far, including the option to skip. Updating the minimum before evaluating that day can add a zero-profit candidate, which never improves the result.

**Pattern.** One-pass minimum.

### What to choose and what to skip

**What this reduces to and when to commit.** One buy, one later sell, and a single maximum. Commit to a running minimum once you see that each sell day has exactly one best buy price to consider.

**Where to save time.** Skip sorting because it erases chronology. Skip a DP table because two values hold all the state. If the interviewer adds repeated trades, a fee, or a cooldown, clarify the new rules before reusing this scan.

**How I’d explain it aloud.** “For any sell day, the cheapest earlier buy gives its best profit. I can carry that minimum forward and track the best profit separately. That’s one pass and constant extra space. On a falling market the answer stays zero.”

**Trade-off.** Brute force is a useful correctness baseline at `O(n²)`. A prefix-min array computes the same information with `O(n)` storage, and a running minimum compresses it to one value. Multiple trades, fees, or a cooldown change what state must be retained.

**Java entry point.** `LC121Optimal.maxProfit` in the linked DSA10days topic file.

**Complexity.** `O(n)` time, `O(1)` auxiliary space.

**Checks.** Check a descending sequence and a single price. A new minimum must never erase profit already found.

## 56. Merge Intervals

Medium · [LeetCode 56](https://leetcode.com/problems/merge-intervals/) · [IntervalSortByStart.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalSortByStart.java)

**Problem.** Given closed intervals `[start, end]`, combine every overlapping group and return the disjoint intervals that cover the same points. Touching endpoints overlap.

**Example.** `[[5,7],[1,3],[3,4]] → [[1,4],[5,7]]`.

### How to derive it in an interview

1. **Notice.** Ask what makes an interval safe to output. Without an ordering, a later input can bridge groups you thought were separate. For closed intervals, start equal to end counts as overlap.
2. **Rule out.** Repeatedly comparing and merging pairs can revisit the same groups. Sort by start so that when a new start is beyond the current merged end, every later start is beyond it too.
3. **Build.** Keep just the current group’s start and rightmost end. If a new interval overlaps, extend with `max`. Otherwise write the finished group and start a new one.
4. **Verify.** All emitted groups are final and disjoint. The open group is exactly the union of the current overlapping chain. A contained interval such as `[2,3]` inside `[1,5]` must leave the end at 5.

**Pattern.** Sort and sweep.

### What to choose and what to skip

**What this reduces to and when to commit.** Union of closed intervals. Commit after identifying a certificate that a group is complete. In start order, the next start beyond the current end makes the current group final.

**Where to save time.** Keep the sort-and-scan once it meets the constraints. A heap helps schedule rooms, and counting coverage serves a different output. If input is already start-sorted, drop the sort.

**How I’d explain it aloud.** “I’ll sort by start and maintain one open merged interval. A start at or before its end extends it with `max`. A later start lets me emit the whole group because every future start is at least as late. I’ll test a contained interval and touching endpoints.”

**Trade-off.** The linked implementation converts the input into sortable interval objects and preserves input order at `O(n)` extra space. Sorting the original array saves that conversion if mutation is allowed. Already sorted input reduces the merge pass to `O(n)` time.

**Java entry point.** `MergeIntervals.merge` in the linked DSA10days topic file.

**Complexity.** `O(n log n)` time and `O(n)` auxiliary space for the linked conversion and sort, plus the output.

**Checks.** Keep the larger end when one interval contains another and append the final interval. The linked implementation sorts its own interval list and preserves the caller’s order.

## 703. Kth Largest in a Stream

Easy · [LeetCode 703](https://leetcode.com/problems/kth-largest-element-in-a-stream/) · [KthLargestInStream.java](../../src/main/java/org/chijai/day7/session1/heap/KthLargestInStream.java)

**Problem.** Build an object initialized with `k` and a starting array. Each `add(value)` inserts into the stream and returns the kth largest value seen so far, counting duplicates separately. The judge queries the kth largest only when enough values have been seen.

**Example.** `k=2, initial=[5,1]`. `add(5) → 5`, then `add(6) → 5`.

### How to derive it in an interview

1. **Notice.** Read `add` as a repeated query on a growing stream. You need a cutoff value after every insertion. Duplicates occupy distinct ranks.
2. **Rule out.** Sorting the entire history per add repeats work. A max-heap exposes the largest item, which is usually the wrong endpoint. Keeping every value also spends more space than the repeated query needs.
3. **Build.** Once a value falls below the top `k`, the `k` values ahead of it cannot disappear. Keep only those winners. You need to eject the weakest winner quickly, so choose a min-heap.
4. **Verify.** After each insertion and optional removal, the heap holds the largest `min(k, seen)` values. With at least `k` values, its minimum is the exact kth largest.

**Pattern.** Bounded min-heap.

### What to choose and what to skip

**What this reduces to and when to commit.** An exact rank after each insert. Commit to a size-`k` min-heap when you can explain that the result is the weakest member of the winners.

**Where to save time.** Skip sorting per update and skip keeping all items in a max-heap just to pop `k-1` of them on each query. Reconsider the structure if old values can expire or `k` changes.

**How I’d explain it aloud.** “I’ll keep only the largest `k` values seen so far. If an insertion gives me `k+1`, I discard the smallest. The minimum among the retained values is the kth largest, so each update takes logarithmic time in `k`.”

**Trade-off.** A size-`k` min-heap pays logarithmic work per add and retains only `k` values. Quickselect suits a one-off rank on a fixed array. It does not reuse a compact summary across repeated updates. Expiration or an increasing `k` can make discarded values relevant again.

**Java entry point.** `Optimal.add` in the linked DSA10days topic file.

**Complexity.** `O(n log(k+1))` initialization, `O(log(k+1))` per add, `O(k)` space.

**Checks.** The root comes from a min-heap. Use a max-heap only if you want a different invariant. Test duplicates and `k=1`.

## 200. Number of Islands

Medium · [LeetCode 200](https://leetcode.com/problems/number-of-islands/) · [Islands.java](../../src/main/java/org/chijai/day8/graph/session1/Islands.java)

**Problem.** Count connected groups of land (`'1'`) in a rectangular grid of land and water (`'0'`). Cells connect only vertically or horizontally.

**Example.** `[[1,0,1],[1,0,0],[0,1,1]] → 3` (shown as digits for readability).

### How to derive it in an interview

1. **Notice.** Treat a land cell as a vertex and its four land neighbors as edges. The question asks for connected components. Two cells can be in the same island even if they are far apart.
2. **Rule out.** Counting land cells or checking only immediate neighbors cannot trace winding paths. A traversal from each land cell also repeats work unless earlier traversals leave a visited mark.
3. **Build.** Scan for the first untouched land cell, count one island, and traverse everything reachable from it. DFS or BFS works. Use a BFS queue to avoid deep recursive calls and mark cells when enqueuing.
4. **Verify.** Every enqueued cell belongs to this island, and it can be enqueued only once. When the queue is empty, this whole component is done. A diagonal contact alone never joins components.

**Pattern.** Grid BFS.

### What to choose and what to skip

**What this reduces to and when to commit.** Connected components with four-way adjacency. Commit to a full flood fill as soon as you see that one scan hit may represent a whole winding region.

**Where to save time.** Skip shortest-path reasoning because the route length never appears in the answer. Pick BFS or iterative DFS quickly. Ask about modifying the grid only if the contract leaves that open, since that determines whether you need a separate visited array.

**How I’d explain it aloud.** “I’ll scan for unvisited land. Each time I find some, I count one island and mark every cell reachable from it. Marking on enqueue prevents duplicate work. I’ll test diagonal cells because they should count as separate islands.”

**Trade-off.** The linked BFS uses a `visited` matrix and preserves the input. Marking the grid in place removes the matrix if mutation is allowed, though the queue can still grow. Recursive DFS is concise, but a large component can exhaust the call stack. A union-find structure becomes more attractive if cells are activated over time.

**Java entry point.** `BFSWithVisited.numIslands` in the linked DSA10days topic file.

**Complexity.** `O(rows × cols)` time and auxiliary space for the visited matrix and worst-case queue.

**Checks.** Diagonal land stays separate. The linked BFS marks a separate matrix and leaves the grid intact.

## 713. Subarray Product Less Than K

Medium · [LeetCode 713](https://leetcode.com/problems/subarray-product-less-than-k/) · [WellsFargoHackerRankDsaSolutions.java](../../src/main/java/org/chijai/wellsfargo/WellsFargoHackerRankDsaSolutions.java)

**Problem.** Given positive integers and a threshold `k`, count contiguous nonempty subarrays whose product is strictly below `k`.

**Example.** `[2,3,4], k=10 → 4` (`[2]`, `[3]`, `[4]`, `[2,3]`).

### How to derive it in an interview

1. **Notice.** Fix a right endpoint and ask which starts give a valid subarray. Positive integer factors are at least one, so moving the start right can only preserve or lower the product.
2. **Rule out.** An incremental product for every start still considers quadratically many windows. The monotonic property means valid starts for a fixed right endpoint form a suffix. Once a start is invalid, extending farther right cannot make it valid again.
3. **Build.** Extend the right edge while keeping a running product. While it is at least `k`, drop factors from the left. The remaining valid starts run from `left` through `right`, so add `right - left + 1` at once.
4. **Verify.** After shrinking, the retained window is the longest valid suffix ending at `right`, or empty. Each subarray is counted at exactly one right endpoint. For `[2,3,4]` with `k=10`, the last step removes 2 and 3 and counts only `[4]`.

**Pattern.** Sliding window.

### What to choose and what to skip

**What this reduces to and when to commit.** Count all qualifying subarrays with a monotone product bound. Commit once you can prove that for a fixed right endpoint, every suffix of a valid window remains valid.

**Where to save time.** Skip prefix sums and binary search once the validity boundary can move forward without retreating. Zero or negative values would require revisiting the proof and possibly the algorithm. Pin down the strict `< k` condition before coding.

**How I’d explain it aloud.** “I’ll fix the right endpoint and shrink from the left until the product drops below `k`. Every suffix of that window is valid because all values are positive integers. I’ll add the window length to the count. Each pointer moves forward at most `n` times.”

**Trade-off.** An incremental-product nested scan is straightforward but still quadratic. The moving window gains linear time because both boundaries only move forward. With zeros, division and the shortcut for `k <= 1` need separate handling. With negatives, the suffix argument itself breaks.

**Java entry point.** `subarrayProductLessThanK` in the Wells Fargo solution bank.

**Complexity.** `O(n)` time, `O(1)` auxiliary space.

**Checks.** `k <= 1` yields zero. Exclude equality, use `long` for the intermediate product, and revisit the algorithm if zeros or negatives are allowed.

## 1768. Merge Strings Alternately

Easy · [LeetCode 1768](https://leetcode.com/problems/merge-strings-alternately/) · [WellsFargoHackerRankDsaSolutions.java](../../src/main/java/org/chijai/wellsfargo/WellsFargoHackerRankDsaSolutions.java)

**Problem.** Take alternating characters from the first and second strings, starting with the first. Once one is exhausted, append the rest of the other.

**Example.** `"ab"`, `"WXYZ" → "aWbXYZ"`.

### How to derive it in an interview

1. **Notice.** Write the output order as `first[0], second[0], first[1], second[1], …`. Both inputs use the same index until one runs out. After that, only the longer one contributes.
2. **Rule out.** Two pointers and a separate remainder pass work. They add branching you do not need because the input indices advance in step whenever both have a character. Repeated `String` concatenation also rebuilds prefixes.
3. **Build.** Iterate one index to the longer length. Append from each input in the required order if it has that index. Use a `StringBuilder` so appending does not repeatedly copy the full answer.
4. **Verify.** Before index `i`, every existing character at smaller indices has been appended exactly once in the right order. Two independent bounds checks preserve this even when the lengths differ.

**Pattern.** StringBuilder.

### What to choose and what to skip

**What this reduces to and when to commit.** Straight construction with a fixed output order. Commit to one shared index and two bounds checks as soon as you can trace unequal-length inputs.

**Where to save time.** Skip creating a queue or splitting the remainder into separate cases. A builder gives a simple linear implementation. Ask about Unicode code points only if the input contract goes beyond ordinary single-unit characters.

**How I’d explain it aloud.** “At each index I append from the first word and then the second, whenever that index exists. I iterate to the longer length, so those same checks handle the leftover suffix. Each character is appended once.”

**Trade-off.** A builder with one shared index avoids separate remainder logic. A two-pointer loop plus a tail append is equally valid and can express bulk appending clearly. Both take linear time and output space. If the unit becomes a Unicode code point, switch the traversal unit before reusing either implementation.

**Java entry point.** `mergeAlternately` in the Wells Fargo solution bank.

**Complexity.** `O(n+m)` time and `O(n+m)` buffer/output space.

**Checks.** Use two independent `if` checks. An `else if` would skip the second character whenever both inputs still have one.

## 796. Rotate String

Easy · [LeetCode 796](https://leetcode.com/problems/rotate-string/) · [WellsFargoHackerRankDsaSolutions.java](../../src/main/java/org/chijai/wellsfargo/WellsFargoHackerRankDsaSolutions.java)

**Problem.** Decide whether `goal` can be formed from `s` by repeated left rotations, moving the first character to the end each time. Zero rotations are allowed.

**Example.** `s="wxyz", goal="yzwx" → true`. `goal="yz" → false`.

### How to derive it in an interview

1. **Notice.** Try rotating `wxyz` by two. The answer `yzwx` is a suffix followed by a prefix. Both pieces keep their order, and the output length cannot change.
2. **Rule out.** Generating all rotations makes up to `n` full strings. Comparing character counts would also accept reorderings that are not rotations. The only search you need is for one contiguous cyclic traversal.
3. **Build.** Lay down two copies of the input. A length-`n` window starting at any offset in the first copy now contains one entire rotation, even if it crosses the seam.
4. **Verify.** After checking equal lengths, test whether `goal` occurs in the doubled string. Every full-length match represents one legal offset. Reach for KMP only if a guaranteed linear search is required.

**Pattern.** String doubling.

### What to choose and what to skip

**What this reduces to and when to commit.** Cyclic order with the same length. Commit to checking for a full-length substring in a doubled string once you can express a rotation as suffix plus prefix.

**Where to save time.** Drop anagrams and sorting because position order matters. Stop generating each rotated copy after recognizing the doubled representation. Use KMP if guaranteed linear search is a stated requirement. Avoid claiming Java `contains` gives that bound.

**How I’d explain it aloud.** “A rotation cuts the string once and moves the prefix to the end. Every such result appears across some length-`n` window in `s + s`. First I check equal lengths, then the substring match checks every offset.”

**Trade-off.** Doubling makes the cyclic check easy to implement and costs `O(n)` extra space. The search bound depends on the substring algorithm. For a guaranteed linear bound, run KMP with a prefix table and scan the doubled input virtually using modular indexing.

**Java entry point.** `rotateString` in the Wells Fargo solution bank.

**Complexity.** `O(n)` extra space for doubling. Search time depends on the substring implementation and can be `O(n²)` with naive matching. KMP gives guaranteed `O(n)` time if required.

**Checks.** Length equality is essential. Test no rotation, repeated characters, and a goal of the wrong length.

## 2696. Minimum Length After Removing Substrings

Easy · [LeetCode 2696](https://leetcode.com/problems/minimum-string-length-after-removing-substrings/) · [WellsFargoHackerRankDsaSolutions.java](../../src/main/java/org/chijai/wellsfargo/WellsFargoHackerRankDsaSolutions.java)

**Problem.** Given an uppercase string, repeatedly delete adjacent `AB` or `CD`. Deletions can join previously separated characters. Return the smallest remaining length.

**Example.** `"AABB" → 0`. Removing the middle `AB` exposes a second `AB`.

### How to derive it in an interview

1. **Notice.** Dry-run `AABB`. Removing the middle `AB` exposes a new adjacency. The next useful check is local to the deletion boundary.
2. **Rule out.** Repeated whole-string searches and rebuilds do unnecessary work. Also check whether grabbing the first pair could make the final answer worse. Current removable pairs cannot overlap, because first letters are `A/C` and second letters are `B/D`. Disjoint deletions commute, so either order reaches the same shorter string after both deletions.
3. **Build.** Keep the reduced prefix. When you append one character, the only newly possible pair is between it and the last survivor. That operation needs access to just one end of the prefix.
4. **Verify.** The stack is the fully reduced prefix. Pop when the new character completes `AB` or `CD`. Otherwise push it. A pop leaves a prefix of an already reduced string, so there is no internal pair left to rescan.

**Pattern.** Stack reduction.

### What to choose and what to skip

**What this reduces to and when to commit.** Local reduction with cascading adjacency. Commit when you see that each new character can form a pair only with the last survivor, and that simultaneously removable pairs cannot overlap.

**Where to save time.** Skip repeated `replace` passes because each one rescans or rebuilds the string. Skip searching deletion orders after proving these rules allow disjoint deletions to commute. If the rules allowed overlapping matches, revisit whether eager removal is safe.

**How I’d explain it aloud.** “I’ll keep the fully reduced prefix on a stack. A new character either forms a removable pair with the top or joins the prefix. The rules have no overlapping matches, so choosing an available deletion is safe. `AABB` tests the chain reaction.”

**Trade-off.** Repeated string replacement is short but may rescan the string for every round. A stack or writable character buffer spends linear storage to preserve the only prefix that still matters. The character buffer can avoid boxing. Greedy removal depends on these exact deletion rules.

**Java entry point.** `minimumLengthAfterRemovingSubstrings` in the Wells Fargo solution bank.

**Complexity.** `O(n)` time, `O(n)` auxiliary space.

**Checks.** The second pair is `CD`. Test an input where one deletion exposes another pair, and an input with no deletions.

## 844. Backspace String Compare

Easy · [LeetCode 844](https://leetcode.com/problems/backspace-string-compare/) · [WellsFargoHackerRankDsaSolutions.java](../../src/main/java/org/chijai/wellsfargo/WellsFargoHackerRankDsaSolutions.java)

**Problem.** A `#` deletes the most recent surviving character, if any. Compare the two strings after all deletions.

**Example.** `"xy##a"` and `"#a" → true` because both become `"a"`.

### How to derive it in an interview

1. **Notice.** Read `#` as deleting a character to its left. From right to left, you encounter a deletion before the character it may erase. That allows you to skip instead of storing the surviving string.
2. **Rule out.** Building two strings with stacks is a valid linear-time baseline. Comparing raw positions fails because a later `#` can erase the character you just compared. Reverse traversal keeps the same time and removes the buffers.
3. **Build.** Use one pointer per string and a local count of pending deletions to find the next visible character. `#` increases the count. An ordinary character consumes one skip or is visible if none remain.
4. **Verify.** Each comparison uses the next surviving character from the right in both strings. If only one has a survivor, return false. Excess backspaces disappear when the pointer runs out of characters.

**Pattern.** Reverse two pointers.

### What to choose and what to skip

**What this reduces to and when to commit.** Comparison after edits. A forward stack is a complete linear baseline. Commit to a reverse scan when the interviewer expects constant extra space or invites an improvement.

**Where to save time.** Skip comparing raw positions or trying to align indices across the two strings. Past deletions affect where a visible character lies in its own string. The two pointers can advance independently.

**How I’d explain it aloud.** “A forward pass can build both results. I can avoid those buffers by scanning backward. A backspace tells me to skip a character I haven’t reached yet. I’ll compare the next visible characters and check whether both sides finish together.”

**Trade-off.** A forward stack is easier to write and test and takes `O(n+m)` extra space. Reverse skipping keeps the same linear time with `O(1)` extra space, at the cost of more boundary checks. Use the forward version if the task changes to producing the edited strings.

**Java entry point.** `backspaceCompare` in the Wells Fargo solution bank.

**Complexity.** `O(n+m)` time, `O(1)` auxiliary space.

**Checks.** Leading backspaces have no effect once a pointer runs off the left edge. Test one empty result against a nonempty result.

## 160. Intersection of Two Linked Lists

Easy · [LeetCode 160](https://leetcode.com/problems/intersection-of-two-linked-lists/) · [Intersection.java](../../src/main/java/org/chijai/day4/LinkedList/session1/Intersection.java)

**Problem.** Given the heads of two acyclic singly linked lists, return the shared node where the lists merge, or `null` when they do not share a node.

**Example.** If `1 → X` and `2 → 3 → X` both point to the same object `X`, return `X`. Equal values in separate objects do not count.

### How to derive it in an interview

1. **Notice.** Clarify that intersection is reference identity. Once acyclic lists share a node, the rest of their path is identical because each node has one `next` pointer.
2. **Rule out.** A set of node references solves it with extra memory. Walking both pointers from their heads can miss the start of the shared tail when their prefixes have different lengths. Counting lengths and advancing the longer list is a valid constant-space option.
3. **Build.** Cancel the length difference without counting. At `null`, send each pointer to the other head. If the exclusive prefixes have lengths `a` and `b` and the shared suffix length is `c`, the switched paths reach the merge after `a+c+1+b` and `b+c+1+a` steps respectively, unless the pointers meet sooner.
4. **Verify.** Compare references at each step. Switching equalizes the distance to the merge, or to the final `null` if there is no shared node. Equal-valued nodes in different lists never qualify.

**Pattern.** Linked-list pointer switching.

### What to choose and what to skip

**What this reduces to and when to commit.** Shared node identity in two acyclic lists. Commit to aligning the suffixes once you identify the unequal prefixes as the only obstacle.

**Where to save time.** Skip comparing values and mutating pointers in the lists. A reference set works with extra space. Counting lengths and advancing the longer list is a sound fallback if the switch-on-null proof does not come quickly.

**How I’d explain it aloud.** “After two lists share a node, their tails are identical. Switching each pointer to the other head makes them spend the same total distance on the unequal prefixes. They meet at the first shared reference or at `null`.”

**Trade-off.** A reference set skips alignment reasoning but stores up to one whole list. Both length alignment and pointer switching use constant space and linear time. Pick length alignment when it makes the proof clearer. Switch heads to avoid a separate counting pass.

**Java entry point.** `OptimalSolution.getIntersectionNode` in the linked DSA10days topic file.

**Complexity.** `O(n+m)` time, `O(1)` auxiliary space.

**Checks.** Use reference equality (`==`). The judge supplies its `ListNode` type, so keep only the method when adapting the local snippet.

## 253. Meeting Rooms II

Medium · [LeetCode 253](https://leetcode.com/problems/meeting-rooms-ii/) · [IntervalActiveMinHeap.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalActiveMinHeap.java)

**Problem.** Given meeting intervals `[start, end)`, return the minimum number of rooms needed. A room becomes available at its meeting’s end time.

**Example.** `[[0,8],[3,5],[5,9]] → 2`. The last meeting can reuse the room freed at 5.

### How to derive it in an interview

1. **Notice.** Process meetings in start order. The answer is the busiest overlap, and room identities can be ignored. At each start, count the meetings that have started and have yet to finish.
2. **Rule out.** Trying every room for each meeting can be quadratic. A chronological sweep of start and end events is another valid solution. With a heap, the earliest remaining end tells you whether more meetings can be removed.
3. **Build.** Store end times in a min-heap. Before processing each meeting, remove every end at or before its start. Insert the current end, then update the largest heap size seen.
4. **Verify.** After insertion the heap represents exactly the meetings active at the current start. Each needs one room. Expired meetings free those rooms, so the highest heap size is both necessary and sufficient.

**Pattern.** Min-heap scheduling.

### What to choose and what to skip

**What this reduces to and when to commit.** Peak concurrent demand. Commit once you can expire finished meetings using the earliest end and distinguish the current active count from its peak.

**Where to save time.** Merging ranges loses counts. Either an event sweep or an active heap is enough. If you use the active heap, remove all finished meetings and retain the maximum size, because the final size can be smaller.

**How I’d explain it aloud.** “I’ll process meetings in start order and remove all end times at or before the next start. Then I add that meeting and update the peak. The heap holds exactly the meetings running together. I’ll test a finish at 5 followed by a start at 5, and a later meeting after the peak has dropped.”

**Trade-off.** The linked heap tracks active end times, removes all finished meetings, and records the peak. Sorting start and end arrays separately also works for a count. If actual room IDs are required, keep `(end, roomId)` per allocated room and reuse one available room per meeting. Each variant has a different heap-size interpretation. [Sorted start/end variant in IntervalActiveMinHeap.java](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/IntervalActiveMinHeap.java). Search for `MeetingRoomsIISortedStartsEnds`.

**Java entry point.** `MeetingRoomsIIActiveHeap.minMeetingRooms` in the linked DSA10days topic file.

**Complexity.** `O(n log n)` time, `O(n)` auxiliary space.

**Checks.** Retire ends with `<= start` and return the recorded peak. The linked method accepts `Interval[]` and sorts that array in place. Use the file’s adapter or the judge’s `int[][]` input when recreating it.

## 146. LRU Cache

Medium · [LeetCode 146](https://leetcode.com/problems/lru-cache/) · [LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java)

**Problem.** Implement an LRU cache with fixed capacity, `get(key)` returning the value or `-1`, and `put(key,value)` inserting or updating. A hit or update makes a key most recent. When inserting into a full cache, evict the least recently used key. Aim for average constant-time operations.

**Example.** Capacity 2. `put(1,10)`, `put(2,20)`, `get(1) → 10`, `put(3,30)`, `get(2) → -1`.

### How to derive it in an interview

1. **Notice.** Extract the three operations inside the contract. Find a node by key, move a known node to most recent, and remove the least recent. They all need to be fast.
2. **Rule out.** A map alone does not track recency. An array or singly linked list can maintain order but may need a scan to find an entry or its predecessor before unlinking. Sorting by a timestamp on eviction also misses the time target.
3. **Build.** Use a map to turn a key into a node reference. Give the node both neighbor links so moving it takes a fixed number of pointer updates. Keep the least recent and most recent ends accessible.
4. **Verify.** The map and list represent exactly the same entries. A successful get or update moves a node to the most-recent end. Only inserting a new key into a full cache evicts the first real node at the least-recent end.

**Pattern.** Hash map + doubly linked list.

### What to choose and what to skip

**What this reduces to and when to commit.** Two independent requirements drive the design. Find by key and move a known entry in recency order. Commit once both checks are constant time with a hash map and a doubly linked list.

**Where to save time.** Skip timestamps plus a scan or heap if the requirement is average `O(1)`. An access-ordered `LinkedHashMap` is viable if library shortcuts are allowed. Use the explicit map-and-list version when the interview is testing the internals.

**How I’d explain it aloud.** “The map locates a node by key, and the list tells me which key is least recent. A hit or update moves that node to the recent end in constant time. Only a new insert at capacity evicts. I’ll test that updating an existing key changes recency without removing another key.”

**Trade-off.** The manual list makes recency and pointer mutation explicit. Access-ordered `LinkedHashMap` gives the same average time and space with less code if the interviewer allows it. Both have mutable recency on reads, so a concurrent map alone cannot preserve the combined lookup-and-recency invariant. [Library variant in LruCache.java](../../src/main/java/org/chijai/day4/LinkedList/session3/LruCache.java). Search for `LRUCacheJava`.

**Java entry point.** `LRUCache` in the linked DSA10days topic file.

**Complexity.** Average `O(1)` per operation, `O(capacity)` space.

**Checks.** Updating an existing key refreshes its position without allocating a second node or evicting a peer. In the linked implementation, `head.next` is most recent and `tail.prev` is least recent.

## 696. Count Binary Substrings

Easy · [LeetCode 696](https://leetcode.com/problems/count-binary-substrings/) · [WellsFargoHackerRankDsaSolutions.java](../../src/main/java/org/chijai/wellsfargo/WellsFargoHackerRankDsaSolutions.java)

**Problem.** Count contiguous substrings where zeros and ones appear in exactly two consecutive runs of equal length. Count occurrences at different positions separately.

**Example.** `"00011" → 2` (`"01"` and `"0011"`).

### How to derive it in an interview

1. **Notice.** Ask where a valid substring can cross from zeros to ones or vice versa. It has exactly one such boundary and takes the same number of characters from both adjacent runs.
2. **Rule out.** Enumerating substrings repeats counts. Counting only the number of zeros and ones wrongly accepts `0101`, which crosses three boundaries. Once the boundary is fixed, the only choice is how far to extend on each side.
3. **Build.** For adjacent runs of lengths `a` and `b`, every length from 1 through `min(a,b)` gives exactly one valid substring. Walk the runs and keep just the previous and current lengths.
4. **Verify.** Each valid occurrence belongs to one boundary. Add `min(previous,current)` once for each boundary and flush the final one after the loop.

**Pattern.** Adjacent run lengths.

### What to choose and what to skip

**What this reduces to and when to commit.** Count substrings with exactly one run boundary and equal extension on both sides. Commit to storing two run lengths once the two halves are forced by a boundary.

**Where to save time.** Skip prefix balance because `0101` also has equal counts but crosses multiple boundaries. Skip a general sliding window because valid answers are determined by the neighboring runs.

**How I’d explain it aloud.** “Each valid substring crosses exactly one transition. For adjacent runs of lengths `a` and `b`, I can extend equally on both sides by any size from 1 to `min(a,b)`. I’ll sum that once per boundary and remember the last run pair.”

**Trade-off.** Tracking just the current and previous run is enough for counting. Saving all run lengths costs linear space and helps only if later queries need those runs. If the task asks to list each matching substring, retain positions and account for the size of the requested output.

**Java entry point.** `countBinarySubstrings` in the Wells Fargo solution bank.

**Complexity.** `O(n)` time, `O(1)` auxiliary space.

**Checks.** Add the final boundary after the loop. Equal total zeros and ones alone would incorrectly accept alternating strings with more than two runs.

## 2062. Count Vowel Substrings

Easy · [LeetCode 2062](https://leetcode.com/problems/count-vowel-substrings-of-a-string/) · [WellsFargoHackerRankDsaSolutions.java](../../src/main/java/org/chijai/wellsfargo/WellsFargoHackerRankDsaSolutions.java)

**Problem.** Count contiguous substrings that contain only lowercase vowels and include all five distinct vowels at least once.

**Example.** `"baeioua" → 3` (`"aeiou"`, `"aeioua"`, `"eioua"`).

### How to derive it in an interview

1. **Notice.** Separate the two conditions. A consonant splits the input into independent runs. Inside a run, a valid answer needs exactly five distinct vowels, with repeats allowed.
2. **Rule out.** A nested scan can count them directly. A window rule that simply counts all suffixes whenever it sees all five overcounts, because a shorter suffix can lose a vowel.
3. **Build.** Count a condition that survives deleting from the left. If a window has at most `k` distinct vowels, every suffix also has at most `k`. Count all suffixes in that window. Subtract the at-most-four count from the at-most-five count to keep exactly five.
4. **Verify.** In each pass, after shrinking, the window is the longest suffix within its distinct-vowel limit. Each right endpoint contributes precisely the valid suffixes. Reset at consonants so no counted substring spans a separator.

**Pattern.** Two sliding windows.

### What to choose and what to skip

**What this reduces to and when to commit.** Count substrings satisfying both an alphabet restriction and exact distinct coverage. Commit to independent vowel runs and at-most subtraction once you see that deleting from a window can destroy exact coverage.

**Where to save time.** Skip treating all vowel-only substrings as answers or adding every suffix of a window with all five. If you cannot derive subtraction under pressure, a nested scan that stops at a consonant is a clear baseline and keeps both conditions visible.

**How I’d explain it aloud.** “Consonants split the input into independent runs. Inside a run I need exactly five distinct vowels. The at-most condition lets me count all valid suffixes, so I’ll count at most five and subtract at most four.”

**Trade-off.** The quadratic scan is a sound small-input baseline. Two at-most passes give linear time and a reusable exact-count pattern. A one-pass last-occurrence approach is also possible for these five fixed vowels, but it replaces the reusable window logic with vowel-specific bookkeeping.

**Java entry point.** `countVowelSubstrings` in the Wells Fargo solution bank.

**Complexity.** `O(n)` time with two passes, `O(1)` auxiliary space because the map has at most five keys.

**Checks.** Do not count across consonants. Keep a vowel in the map until its last occurrence leaves the window.

## 2904. Shortest and Lexicographically Smallest Beautiful String

Medium · [LeetCode 2904](https://leetcode.com/problems/shortest-and-lexicographically-smallest-beautiful-string/) · [WellsFargoHackerRankDsaSolutions.java](../../src/main/java/org/chijai/wellsfargo/WellsFargoHackerRankDsaSolutions.java)

**Problem.** Given a binary string and a positive `k`, return the shortest substring containing exactly `k` ones. Break length ties lexicographically and return `""` if there is no candidate.

**Example.** `s="11001011", k=3 → "1011"`. Among length-four substrings, only `"1011"` qualifies. For a tie, `s="1101011", k=3 → "1011"` beats `"1101"`.

### How to derive it in an interview

1. **Notice.** Translate the objective into a comparison rule. First minimize length, then compare lexicographically among equal-length candidates. A few ones close together can beat an earlier valid substring.
2. **Rule out.** Checking every substring by recounting its ones repeats work. For a fixed start, keep a running count. Once it reaches `k`, any later end would be longer for that same start, even if the added characters are all zeros.
3. **Build.** Enumerate starts and stop each scan at its kth one. Keep one best candidate and compare lengths before lexicographic order. The quadratic approach fits this problem’s small input bound. It also lets you reason about both tie rules directly.
4. **Verify.** The global winner must be among the shortest valid candidates for individual starts. Test `1101011` with `k=3`. Both `1101` and `1011` have length four, so `1011` wins on the tie-break.

**Pattern.** Substring enumeration.

### What to choose and what to skip

**What this reduces to and when to commit.** Optimization with two priorities under a small input bound. Commit to bounded enumeration once you prove that for each start only the first end reaching `k` can win.

**Where to save time.** Skip optimizing before checking the size bound. Skip sorting candidates or comparing lexicographically before length. If the input grows, tracking consecutive groups of `k` ones can reduce the candidate search. Tie comparison still needs separate analysis.

**How I’d explain it aloud.** “I’ll rank candidates by length first and lexicographic order second. For any start, the first time I reach `k` ones produces its shortest candidate. The bound is small, so I’ll enumerate those candidates and test an equal-length tie.”

**Trade-off.** Quadratic enumeration is simple and fits the given bound. Tracking the positions of ones reduces candidate generation to groups of consecutive ones when the input grows. Equal-length lexicographic comparisons can still be costly, so a faster candidate scan alone does not establish an overall linear bound.

**Java entry point.** `shortestBeautifulSubstring` in the Wells Fargo solution bank.

**Complexity.** `O(n²)` time including substring copying and tie comparisons, since there is at most one candidate per start. `O(n)` peak extra space for live candidate and best strings.

**Checks.** Check fewer than `k` ones and equal-length candidates. Stop at the kth one for each start.

## 93. Restore IP Addresses

Medium · [LeetCode 93](https://leetcode.com/problems/restore-ip-addresses/) · [WellsFargoHackerRankDsaSolutions.java](../../src/main/java/org/chijai/wellsfargo/WellsFargoHackerRankDsaSolutions.java)

**Problem.** Insert three dots into a digit string to produce every valid IPv4 address without changing digit order. Each of the four segments must be `0` through `255`. Multi-digit segments cannot start with zero.

**Example.** `"1010" → ["1.0.1.0"]`. `"0000" → ["0.0.0.0"]`.

### How to derive it in an interview

1. **Notice.** Think of placing three cuts, or equivalently choosing lengths for four segments. The digit order is fixed. At each position the only choices are one, two, or three digits.
2. **Rule out.** Making every cut combination and validating at the end works, but invalid prefixes cannot recover. A multi-digit segment beginning with zero and a segment already over 255 can be discarded immediately.
3. **Build.** Backtrack by selecting one segment, recursing on the remaining suffix, then removing that choice. If `p` segments remain, prune whenever the remaining digit count falls outside `[p, 3p]`.
4. **Verify.** The selected segments are valid and cover a contiguous prefix. Emit only when four segments consume the whole string. This is why `0000` has one answer and `00` can never be a single segment.

**Pattern.** Backtracking with pruning.

### What to choose and what to skip

**What this reduces to and when to commit.** Generate every valid placement of three dots, with bounded segment length. Commit to depth-four backtracking once your choices and the two segment rejection rules are explicit.

**Where to save time.** Skip DP that only counts whether a suffix can finish, since the output needs the actual addresses. Testing all cut triples is also valid. Pruning by remaining digits and segment validity keeps the search simple.

**How I’d explain it aloud.** “Each step picks one to three digits for the next segment. I’ll reject leading zeros in multi-digit segments and values above 255. If there are `p` segments left, I need between `p` and `3p` digits. I’ll emit only after four segments consume the whole input.”

**Trade-off.** Checking all three cut positions is easy to audit with such a small search. Backtracking prunes impossible lengths and invalid segments earlier and keeps validation local. If the output were only a count under generalized segment rules, a memoized count could avoid materializing results.

**Java entry point.** `restoreIpAddresses` in the Wells Fargo solution bank.

**Complexity.** Constant-size search bounded by `O(3⁴)` branches, `O(4)` recursion depth plus output. Segment parsing is bounded to three digits.

**Checks.** `"01"` is invalid as one segment. Stop extending a segment once it exceeds 255.

## 2008. Maximum Earnings From Taxi

Medium · [LeetCode 2008](https://leetcode.com/problems/maximum-earnings-from-taxi/) · [WellsFargoHackerRankDsaSolutions.java](../../src/main/java/org/chijai/wellsfargo/WellsFargoHackerRankDsaSolutions.java)

**Problem.** A taxi travels forward along points `1` through `n`. Each ride `[start,end,tip]` earns `end-start+tip`. Carry at most one passenger at a time, with drop-off and next pickup allowed at the same point. Return the highest total earnings.

**Example.** `n=8, rides=[[1,4,2],[4,8,1],[2,8,3]] → 10`. The first two rides together beat the overlapping ride’s 9.

### How to derive it in an interview

1. **Notice.** Convert every ride into a time interval with a reward. Non-overlap is the same constraint as meeting scheduling, but rewards vary, so you need the highest-value schedule.
2. **Rule out.** Greedy earliest end can take a cheap short ride that blocks a valuable long one. Greedy highest individual reward can lose to several compatible rides. For example, rewards 5 and 5 on touching rides beat a conflicting ride worth 9.
3. **Build.** Sort by end and define `best[i]` over the first `i` rides. If the next ride is skipped, reuse the old answer. If taken, add its reward to the best prefix of rides that end by its start. End order makes that prefix findable with binary search.
4. **Verify.** Every optimal schedule over the processed prefix either takes or skips its last candidate in end order. Taking it rules out all later-ending overlaps. The recurrence covers both cases, including drop-off and pickup at the same point.

**Pattern.** Weighted interval DP.

### What to choose and what to skip

**What this reduces to and when to commit.** Maximum total reward from compatible intervals. Commit to weighted scheduling once you can split the optimum by taking or skipping the last ride under consideration.

**Where to save time.** Drop earliest-finish and highest-reward greedy rules after a small counterexample. Skip sorting by tip or indexing DP by every road point unless the constraints favor point-indexed DP. The ride-indexed state and end-time search already cover the choices.

**How I’d explain it aloud.** “Rides have different rewards, so I can’t choose greedily by finish time. After sorting by end, I’ll compare skipping each ride with taking its reward plus the best compatible prefix. I can binary-search that prefix because its rides are sorted by end time.”

**Trade-off.** Ride-indexed weighted DP costs `O(r log r)` for `r` rides and avoids scanning unused road points. Point-indexed DP can run in `O(n+r)` with rides bucketed by endpoint, which makes sense when the road domain `n` is small or dense. Use `long` for total reward in either version. [Related weighted-job implementation](../../src/main/java/org/chijai/day1/Arrays/session4/Intervals/WeightedIntervalScheduling.java). It uses explicit job profits and an `int` total, so use the taxi-specific link for fare calculation and `long` totals. Search for `MaximumProfitJobs`.

**Java entry point.** `maximumTaxiEarnings` in the Wells Fargo solution bank.

**Complexity.** `O(r log r)` time and `O(r)` auxiliary space for `r` rides.

**Checks.** Compatibility is `end <= start`. Search only earlier rides. The destination parameter defines the input range, but this ride-indexed DP does not need to iterate over every road point.

## 460. LFU Cache

Hard · [LeetCode 460](https://leetcode.com/problems/lfu-cache/) · [WellsFargoHackerRankDsaSolutions.java](../../src/main/java/org/chijai/wellsfargo/WellsFargoHackerRankDsaSolutions.java)

**Problem.** Implement an LFU cache with `get` and `put`. A new key starts at frequency 1, and a hit or update increments frequency. On a full-cache insert, evict the least frequent key, breaking frequency ties by least recent use. Missing keys return `-1`. Aim for average constant-time operations.

**Example.** Capacity 2. `put(1,10)`, `put(2,20)`, `get(1) → 10`, `put(3,30)`, `get(2) → -1`. With no preceding `get(1)`, the same insert would evict key 1 on recency.

### How to derive it in an interview

1. **Notice.** Read eviction as a two-part ordering. Frequency comes first and recency breaks ties. Also spot that a successful get or an update moves a key from frequency `f` to exactly `f+1`.
2. **Rule out.** Scanning all keys on eviction is a simple baseline with linear cost. A heap can maintain the global ordering, but maintaining priority after hits adds logarithmic work. A single LRU list handles recency but loses frequency priority.
3. **Build.** Separate the two priorities. Use a map for values and frequencies. For each frequency keep an insertion-ordered set to find the oldest key and remove a known key quickly. On a hit, move the key to the back of the next bucket.
4. **Verify.** Keep the smallest nonempty frequency in `minimumFrequency`. If its last key is promoted, the key itself makes `f+1` nonempty. On a new insert the minimum resets to 1. Remove empty buckets to keep metadata bounded by capacity.

**Pattern.** Hash maps + frequency buckets.

### What to choose and what to skip

**What this reduces to and when to commit.** Eviction ordered by frequency, then by recency inside a tie. Commit to frequency buckets when you see that a hit raises one key’s frequency by exactly one and you need average constant-time operations.

**Where to save time.** Drop a single LRU list because recency alone chooses the wrong victim. Scanning on eviction is a baseline. An indexed heap adds update cost on hits. Leave out a balanced tree once you can maintain the smallest nonempty frequency from the permitted operations.

**How I’d explain it aloud.** “I’ll separate frequency from recency. A map tells me a key’s frequency, and each frequency bucket keeps its keys oldest first. A hit moves the key to the next bucket. If that empties the minimum bucket, the next one is now the minimum. New inserts reset it to one.”

**Trade-off.** Scanning for the victim is easier to implement and can suffice for a tiny cache, but eviction is linear in capacity. A heap buys logarithmic reprioritization. Frequency buckets meet the average constant-time target because promotion always advances by one. Empty-bucket cleanup keeps metadata tied to capacity.

**Java entry point.** `LFUCache` in the Wells Fargo solution bank.

**Complexity.** Average `O(1)` per operation, `O(capacity)` space with empty frequency buckets removed.

**Checks.** Update counts as a use. Remove empty buckets on both promotion and eviction so metadata cannot grow with the number of operations.
