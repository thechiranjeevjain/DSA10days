# Trie

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Each node is a prefix; branch only when wildcard or board search requires it.

## Interview Move

Repeated string scans waste prefix work; trie shares prefixes across words.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 139 | Phase 4 - Secondary | Design Add And Search Words Data Structure | Trie + DFS wildcard | [Java](../../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) | [LC](https://leetcode.com/problems/design-add-and-search-words-data-structure/) | Each node is a prefix; branch only when wildcard or board search requires it. | Insert words by characters; search follows children and DFS branches on wildcard/board. |
| 161 | Phase 5 - If Time | Hotel Reviews | Trie / ranking | [Java](../../../src/main/java/org/chijai/day10/session1/trie/HotelReviews.java) | - | Each node is a prefix; branch only when wildcard or board search requires it. | Insert words by characters; search follows children and DFS branches on wildcard/board. |
| 162 | Phase 5 - If Time | Maximum XOR Of Two Numbers In An Array | Binary trie / bit | [Java](../../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) | [LC](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/) | Each node is a prefix; branch only when wildcard or board search requires it. | Insert words by characters; search follows children and DFS branches on wildcard/board. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.