# Stack

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

Stack stores unresolved candidates; current item resolves or validates the top.

## Interview Move

Brute force searches previous/next matches; stack keeps unresolved candidates in useful order.

## Problems

| Global Rank | Must Level | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 90 | Must | Daily Temperatures | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session1/DailyTemperatures.java) | [LC](https://leetcode.com/problems/daily-temperatures/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 91 | Must | Next Greater Element Ii | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session1/NextGreaterElement.java) | [LC](https://leetcode.com/problems/next-greater-element-ii/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 92 | Must | Largest Rectangle In Histogram | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session2/LargestRectangle.java) | [LC](https://leetcode.com/problems/largest-rectangle-in-histogram/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 93 | Must | Maximal Rectangle | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session2/LargestRectangle.java) | [LC](https://leetcode.com/problems/maximal-rectangle/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 94 | Must | Sum Of Subarray Minimums | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session2/LargestRectangle.java) | [LC](https://leetcode.com/problems/sum-of-subarray-minimums/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 95 | Must | Valid Parentheses | Stack | [Java](../../../src/main/java/org/chijai/day5/stack/session3/ValidParentheses.java) | [LC](https://leetcode.com/problems/valid-parentheses/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 114 | Should | Design A Stack With Increment Operation | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LC](https://leetcode.com/problems/design-a-stack-with-increment-operation/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 115 | Should | Max Stack | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LC](https://leetcode.com/problems/max-stack/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 116 | Should | Min Stack | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LC](https://leetcode.com/problems/min-stack/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 117 | Should | Next Greater Element I | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LC](https://leetcode.com/problems/next-greater-element-i/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 118 | Should | Basic Calculator | Stack / expression parsing | [Java](../../../src/main/java/org/chijai/day5/stack/session3/BasicCalculator.java) | [LC](https://leetcode.com/problems/basic-calculator/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 119 | Should | Evaluate Reverse Polish Notation | Stack | [Java](../../../src/main/java/org/chijai/day5/stack/session3/EvalRPN.java) | [LC](https://leetcode.com/problems/evaluate-reverse-polish-notation/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 120 | Should | Design Circular Queue | Stack/queue design | [Java](../../../src/main/java/org/chijai/day5/stack/session3/StackQueue.java) | [LC](https://leetcode.com/problems/design-circular-queue/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 121 | Should | Implement Queue Using Stacks | Stack/queue design | [Java](../../../src/main/java/org/chijai/day5/stack/session3/StackQueue.java) | [LC](https://leetcode.com/problems/implement-queue-using-stacks/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 122 | Should | Implement Stack Using Queues | Stack/queue design | [Java](../../../src/main/java/org/chijai/day5/stack/session3/StackQueue.java) | [LC](https://leetcode.com/problems/implement-stack-using-queues/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.