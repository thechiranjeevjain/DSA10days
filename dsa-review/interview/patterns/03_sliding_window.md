# Sliding Window

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Expand right, shrink left to restore validity, then update the answer at the right time.

## Interview Move

Brute force checks all substrings/subarrays; a window reuses counts while boundaries move once.

## Problems

| Global Rank | Must Level | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 8 | Must Must Must | Longest Substring With At Most K Distinct Characters | Sliding window | [Java](../../../src/main/java/org/chijai/day3/session1/AtMostKDistinct.java) | [LC](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/) | Keep a frequency map with at most k distinct chars; shrink until valid. | Expand right count, while distinct > k decrement/remove left, update max length. |
| 9 | Must Must Must | Longest Substring Without Repeating Characters | Sliding window / set | [Java](../../../src/main/java/org/chijai/day3/session1/LongestSubString.java) | [LC](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | Window must contain unique chars; move left past duplicates. | Expand right, while duplicate exists remove left, then update max. |
| 10 | Must Must Must | Longest Repeating Character Replacement | Sliding window / need-have | [Java](../../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LC](https://leetcode.com/problems/longest-repeating-character-replacement/) | Window is valid when size - maxFreq <= k replacements. | Track counts and maxFreq, shrink when windowLen - maxFreq > k, update best. |
| 11 | Must Must Must | Minimum Size Subarray Sum | Sliding window / need-have | [Java](../../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LC](https://leetcode.com/problems/minimum-size-subarray-sum/) | For positive numbers, expand until sum >= target, then shrink to minimize length. | Add right to sum, while sum >= target update min and subtract left. |
| 12 | Must Must Must | Minimum Window Substring | Sliding window / need-have | [Java](../../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LC](https://leetcode.com/problems/minimum-window-substring/) | Expand until all needed chars are covered, then shrink while still valid. | Build need map, update have on right, while have == needCount update best and remove left. |
| 13 | Must Must Must | Permutation In String | Sliding window / need-have | [Java](../../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LC](https://leetcode.com/problems/permutation-in-string/) | A fixed-size window is a permutation when its frequency counts match the target. | Track counts/matches for window length s1, slide one char in and one char out. |
| 14 | Must Must Must | Substring With Concatenation Of All Words | Sliding window / need-have | [Java](../../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LC](https://leetcode.com/problems/substring-with-concatenation-of-all-words/) | Scan word-sized windows by offset and keep word counts bounded by need. | For each offset, move in wordLen steps, count words, shrink when a word is overused. |
| 15 | Must Must Must | Count Number Of Nice Subarrays | Prefix/window counting | [Java](../../../src/main/java/org/chijai/day3/session2/NiceSubArrays.java) | [LC](https://leetcode.com/problems/count-number-of-nice-subarrays/) | Exactly k odds equals atMost(k) minus atMost(k-1), or prefix count of odd count. | Count subarrays with at most k odd numbers using a sliding left pointer, subtract atMost(k-1). |
| 16 | Must Must Must | Find All Anagrams In A String | Sliding window frequency | [Java](../../../src/main/java/org/chijai/day3/session3/FindAllAnagramsInAString.java) | [LC](https://leetcode.com/problems/find-all-anagrams-in-a-string/) | Slide a fixed-size frequency window and record starts where counts match p. | Maintain difference counts or match count across a window of length p. |
| 102 | Must | Sliding Window Maximum | Stack/queue design | [Java](../../../src/main/java/org/chijai/day5/stack/session3/StackQueue.java) | [LC](https://leetcode.com/problems/sliding-window-maximum/) | Expand right, shrink left to restore validity, then update the answer at the right time. | Move right to include, move left while invalid or while answer can improve. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.