# Math / Bit / String

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Use the algebra, bit, or string invariant instead of simulating blindly.

## Interview Move

Simulation is often slow or bug-prone; use the invariant encoded in arithmetic or bits.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 164 | Phase 5 - If Time | Add Binary | Bit/string addition | [Java](../../../src/main/java/org/chijai/day10/session2/AddBinary.java) | [LC](https://leetcode.com/problems/add-binary/) | Use the algebra, bit, or string invariant instead of simulating blindly. | Track the exact numeric/string invariant and update it in constant or linear time. |
| 165 | Phase 5 - If Time | Count Primes | Math / sieve | [Java](../../../src/main/java/org/chijai/day10/session2/CountPrimes.java) | [LC](https://leetcode.com/problems/count-primes/) | Use the algebra, bit, or string invariant instead of simulating blindly. | Track the exact numeric/string invariant and update it in constant or linear time. |
| 166 | Phase 5 - If Time | Count Unique Characters Of All Substrings Of A Given String | Contribution counting | [Java](../../../src/main/java/org/chijai/day10/session2/CountUniqueChars.java) | [LC](https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/) | Each character occurrence contributes by distance to the previous same char times distance to the next one. | Record previous and next positions for each occurrence, sum leftGap * rightGap contributions. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.