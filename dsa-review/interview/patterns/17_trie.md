# Trie

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

Share prefix nodes so lookup/search consumes one character at a time instead of rescanning words.

## Interview Move

Repeated string scans waste prefix work; trie shares prefixes across words.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 46 | Phase 2 - Strong Core | Implement Trie Prefix Tree | Trie | [Java](../../../src/main/java/org/chijai/day10/session1/trie/TriePrefix.java) | [LC](https://leetcode.com/problems/implement-trie-prefix-tree/) | Each trie node represents one prefix; terminal marks distinguish full words from prefixes. | For insert/search/startsWith, walk chars through children; create on insert, fail on missing child. |
| 98 | Phase 3 - Important | Design Add And Search Words Data Structure | Trie + DFS wildcard | [Java](../../../src/main/java/org/chijai/day10/session1/trie/TrieWordDictionary.java) | [LC](https://leetcode.com/problems/design-add-and-search-words-data-structure/) | Trie search branches only on '.', otherwise it follows exactly one child. | DFS over trie and word index; on '.', try every child, otherwise follow the matching child. |
| 105 | Phase 3 - Important | Word Search Ii | Trie + backtracking | [Java](../../../src/main/java/org/chijai/day10/session1/trie/WordSearchII.java) | [LC](https://leetcode.com/problems/word-search-ii/) | Trie prunes dictionary prefixes while board DFS chooses, marks, explores, and unmarks cells. | Build trie, DFS board paths, stop when prefix missing, collect terminal words, mark cells in-place. |
| 146 | Phase 4 - Secondary | Maximum XOR Of Two Numbers In An Array | Binary trie / bit | [Java](../../../src/main/java/org/chijai/day10/session1/trie/MaximumXOR.java) | [LC](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/) | Binary trie chooses the opposite bit greedily to maximize each XOR bit from high to low. | Insert numbers by bits, then for each number walk preferred opposite bits and update max. |
| 165 | Phase 5 - If Time | Hotel Reviews | Trie / ranking | [Java](../../../src/main/java/org/chijai/day10/session1/trie/HotelReviews.java) | - | Use trie or keyword set to count good words per review, then rank hotels by score. | Normalize review words, count keyword hits, aggregate per hotel, sort by score and id. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.