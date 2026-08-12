# Backtracking / Combinatorial DFS

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Choose, recurse, undo; the path is exactly the current decision state.

## Interview Move

Brute force generates blindly; backtracking prunes invalid decision paths early.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 99 | Phase 3 - Important | Word Search | DFS backtracking | [Java](../../../src/main/java/org/chijai/day8/graph/session1/WordSearch.java) | [LC](https://leetcode.com/problems/word-search/) | Choose, recurse, undo; the path is exactly the current decision state. | Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths. |
| 133 | Phase 4 - Secondary | Word Search Ii | Trie + backtracking | [Java](../../../src/main/java/org/chijai/day10/session1/trie/WordSearchII.java) | [LC](https://leetcode.com/problems/word-search-ii/) | Choose, recurse, undo; the path is exactly the current decision state. | Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths. |
| 134 | Phase 4 - Secondary | Combination Sum | Backtracking reuse | [Java](../../../src/main/java/org/chijai/day11/backtracking/session1/CombinationSum.java) | [LC](https://leetcode.com/problems/combination-sum/) | Choose, recurse, undo; the path is exactly the current decision state. | Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths. |
| 135 | Phase 4 - Secondary | Letter Combinations Of A Phone Number | Backtracking / mapping | [Java](../../../src/main/java/org/chijai/day11/backtracking/session1/LetterCombinationsOfAPhoneNumber.java) | [LC](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) | Choose, recurse, undo; the path is exactly the current decision state. | Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths. |
| 136 | Phase 4 - Secondary | Permutations | Backtracking permutations | [Java](../../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) | [LC](https://leetcode.com/problems/permutations/) | Choose, recurse, undo; the path is exactly the current decision state. | Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths. |
| 137 | Phase 4 - Secondary | Permutations Ii | Backtracking permutations | [Java](../../../src/main/java/org/chijai/day11/backtracking/session1/Permutations.java) | [LC](https://leetcode.com/problems/permutations-ii/) | Choose, recurse, undo; the path is exactly the current decision state. | Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths. |
| 138 | Phase 4 - Secondary | Subsets | Backtracking subsets | [Java](../../../src/main/java/org/chijai/day11/backtracking/session1/Subsets.java) | [LC](https://leetcode.com/problems/subsets/) | Choose, recurse, undo; the path is exactly the current decision state. | Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.