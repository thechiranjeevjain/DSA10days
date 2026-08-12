# Stack / Monotonic Stack

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

Stack stores unresolved candidates; current item resolves or validates the top.

## Interview Move

Brute force searches previous/next matches; stack keeps unresolved candidates in useful order.

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 32 | Phase 2 - Strong Core | Daily Temperatures | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session1/DailyTemperatures.java) | [LC](https://leetcode.com/problems/daily-temperatures/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 33 | Phase 2 - Strong Core | Valid Parentheses | Stack | [Java](../../../src/main/java/org/chijai/day5/stack/session3/ValidParentheses.java) | [LC](https://leetcode.com/problems/valid-parentheses/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 44 | Phase 2 - Strong Core | Largest Rectangle In Histogram | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session2/LargestRectangle.java) | [LC](https://leetcode.com/problems/largest-rectangle-in-histogram/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 96 | Phase 3 - Important | Sliding Window Maximum | Stack/queue design | [Java](../../../src/main/java/org/chijai/day5/stack/session3/StackQueue.java) | [LC](https://leetcode.com/problems/sliding-window-maximum/) | A decreasing deque stores candidate indices; front is always the current window maximum. | Drop out-of-window front, pop smaller/equal from back, push index, read front after first window. |
| 106 | Phase 3 - Important | Next Greater Element Ii | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session1/NextGreaterElement.java) | [LC](https://leetcode.com/problems/next-greater-element-ii/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 107 | Phase 3 - Important | Sum Of Subarray Minimums | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session2/LargestRectangle.java) | [LC](https://leetcode.com/problems/sum-of-subarray-minimums/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 108 | Phase 3 - Important | Evaluate Reverse Polish Notation | Stack | [Java](../../../src/main/java/org/chijai/day5/stack/session3/EvalRPN.java) | [LC](https://leetcode.com/problems/evaluate-reverse-polish-notation/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 110 | Phase 3 - Important | Basic Calculator | Stack / expression parsing | [Java](../../../src/main/java/org/chijai/day5/stack/session3/BasicCalculator.java) | [LC](https://leetcode.com/problems/basic-calculator/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 130 | Phase 4 - Secondary | Maximal Rectangle | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session2/LargestRectangle.java) | [LC](https://leetcode.com/problems/maximal-rectangle/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 131 | Phase 4 - Secondary | Min Stack | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LC](https://leetcode.com/problems/min-stack/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 132 | Phase 4 - Secondary | Max Stack | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LC](https://leetcode.com/problems/max-stack/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 133 | Phase 4 - Secondary | Implement Queue Using Stacks | Stack/queue design | [Java](../../../src/main/java/org/chijai/day5/stack/session3/StackQueue.java) | [LC](https://leetcode.com/problems/implement-queue-using-stacks/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 134 | Phase 4 - Secondary | Implement Stack Using Queues | Stack/queue design | [Java](../../../src/main/java/org/chijai/day5/stack/session3/StackQueue.java) | [LC](https://leetcode.com/problems/implement-stack-using-queues/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 135 | Phase 4 - Secondary | Next Greater Element I | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LC](https://leetcode.com/problems/next-greater-element-i/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 136 | Phase 4 - Secondary | Online Stock Span | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LC](https://leetcode.com/problems/online-stock-span/) | A decreasing stack of price/span pairs merges all previous prices <= current price. | Start span=1, while stack top price <= current add its span and pop, then push current/span. |
| 147 | Phase 4 - Secondary | Design A Stack With Increment Operation | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session1/MinStackDesign.java) | [LC](https://leetcode.com/problems/design-a-stack-with-increment-operation/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |
| 148 | Phase 4 - Secondary | Design Circular Queue | Stack/queue design | [Java](../../../src/main/java/org/chijai/day5/stack/session3/StackQueue.java) | [LC](https://leetcode.com/problems/design-circular-queue/) | Stack stores unresolved candidates; current item resolves or validates the top. | While top is resolved by current value, pop and compute; then push current. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.