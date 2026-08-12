# Greedy

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Take the local choice only after proving it cannot hurt the future optimum.

## Interview Move

DP/search may be possible, but a proven safe local choice collapses the state space.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 154 | Phase 4 - Secondary | Gas Station | Greedy | [Java](../../../src/main/java/org/chijai/day3/session2/GasStation.java) | [LC](https://leetcode.com/problems/gas-station/) | Take the local choice only after proving it cannot hurt the future optimum. | Sort or scan to make the safe local choice repeatedly. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.