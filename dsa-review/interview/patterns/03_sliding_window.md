# Sliding Window

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

Expand right, shrink left to restore validity, then update the answer at the right time.

## Interview Move

Brute force checks all substrings/subarrays; a window reuses counts while boundaries move once.

## Pattern Taxonomy Map

```mermaid
flowchart TD
  Topic["TOPIC<br/>Sliding Window"]
  Recognition["RECOGNITION<br/>Expand right, shrink left to restore validity, then update the answer at the right time."]
  Invariant["INVARIANT<br/>Brute force checks all substrings/subarrays; a window reuses counts while boundaries move once."]
  Topic --> Recognition --> Invariant
  Invariant --> Sub01["SUB-PATTERN<br/>Prefix/window counting<br/>1 problem(s)"]
  Sub01 --> Sub01A01["ANCHOR<br/>rank 113: Count Number Of Nice Subarrays"]
  Invariant --> Sub02["SUB-PATTERN<br/>Sliding window<br/>1 problem(s)"]
  Sub02 --> Sub02A01["ANCHOR<br/>rank 49: Longest Substring With At Most K Distinct Characters"]
  Invariant --> Sub03["SUB-PATTERN<br/>Sliding window / need-have<br/>5 problem(s)"]
  Sub03 --> Sub03A01["ANCHOR<br/>rank 5: Minimum Window Substring"]
  Sub03 --> Sub03A02["ANCHOR<br/>rank 47: Longest Repeating Character Replacement"]
  Sub03 --> Sub03A03["ANCHOR<br/>rank 51: Permutation In String"]
  Invariant --> Sub04["SUB-PATTERN<br/>Sliding window / set<br/>1 problem(s)"]
  Sub04 --> Sub04A01["ANCHOR<br/>rank 3: Longest Substring Without Repeating Characters"]
  Invariant --> Sub05["SUB-PATTERN<br/>Sliding window frequency<br/>1 problem(s)"]
  Sub05 --> Sub05A01["ANCHOR<br/>rank 56: Find All Anagrams In A String"]
```

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 3 | Phase 1 - No Red Flags | Longest Substring Without Repeating Characters | Sliding window / set | [Java](../../../src/main/java/org/chijai/day3/session1/LongestSubString.java) | [LC](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | Window must contain unique chars; move left past duplicates. | Expand right, while duplicate exists remove left, then update max. |
| 5 | Phase 1 - No Red Flags | Minimum Window Substring | Sliding window / need-have | [Java](../../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LC](https://leetcode.com/problems/minimum-window-substring/) | Expand until all needed chars are covered, then shrink while still valid. | Build need map, update have on right, while have == needCount update best and remove left. |
| 47 | Phase 2 - Strong Core | Longest Repeating Character Replacement | Sliding window / need-have | [Java](../../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LC](https://leetcode.com/problems/longest-repeating-character-replacement/) | Window is valid when size - maxFreq <= k replacements. | Track counts and maxFreq, shrink when windowLen - maxFreq > k, update best. |
| 49 | Phase 2 - Strong Core | Longest Substring With At Most K Distinct Characters | Sliding window | [Java](../../../src/main/java/org/chijai/day3/session1/AtMostKDistinct.java) | [LC](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/) | Keep a frequency map with at most k distinct chars; shrink until valid. | Expand right count, while distinct > k decrement/remove left, update max length. |
| 51 | Phase 2 - Strong Core | Permutation In String | Sliding window / need-have | [Java](../../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LC](https://leetcode.com/problems/permutation-in-string/) | A fixed-size window is a permutation when its frequency counts match the target. | Track counts/matches for window length s1, slide one char in and one char out. |
| 56 | Phase 2 - Strong Core | Find All Anagrams In A String | Sliding window frequency | [Java](../../../src/main/java/org/chijai/day3/session3/FindAllAnagramsInAString.java) | [LC](https://leetcode.com/problems/find-all-anagrams-in-a-string/) | Slide a fixed-size frequency window and record starts where counts match p. | Maintain difference counts or match count across a window of length p. |
| 64 | Phase 2 - Strong Core | Substring With Concatenation Of All Words | Sliding window / need-have | [Java](../../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LC](https://leetcode.com/problems/substring-with-concatenation-of-all-words/) | Scan word-sized windows by offset and keep word counts bounded by need. | For each offset, move in wordLen steps, count words, shrink when a word is overused. |
| 113 | Phase 4 - Secondary | Count Number Of Nice Subarrays | Prefix/window counting | [Java](../../../src/main/java/org/chijai/day3/session2/prefix/suffix/NiceSubArrays.java) | [LC](https://leetcode.com/problems/count-number-of-nice-subarrays/) | Exactly k odds equals atMost(k) minus atMost(k-1), or prefix count of odd count. | Count subarrays with at most k odd numbers using a sliding left pointer, subtract atMost(k-1). |
| 114 | Phase 4 - Secondary | Minimum Size Subarray Sum | Sliding window / need-have | [Java](../../../src/main/java/org/chijai/day3/session1/MinimumWindowSubstring.java) | [LC](https://leetcode.com/problems/minimum-size-subarray-sum/) | For positive numbers, expand until sum >= target, then shrink to minimize length. | Add right to sum, while sum >= target update min and subtract left. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.