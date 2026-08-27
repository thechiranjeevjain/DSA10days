# Stack / Monotonic Stack

Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.

## Recognition Signal

Keep pending openings, operands, or monotonic candidates until the current item resolves them.

## Interview Move

Brute force searches previous/next matches; stack keeps unresolved candidates in useful order.

## Pattern Taxonomy Map

```mermaid
flowchart TD
  Topic["TOPIC<br/>Stack / Monotonic Stack"]
  Recognition["RECOGNITION<br/>Keep pending openings, operands, or monotonic candidates until the current item resolves them."]
  Invariant["INVARIANT<br/>Brute force searches previous/next matches; stack keeps unresolved candidates in useful order."]
  Topic --> Recognition --> Invariant
  Invariant --> Sub01["SUB-PATTERN<br/>Monotonic stack<br/>5 problem(s)"]
  Sub01 --> Sub01A01["ANCHOR<br/>rank 32: Daily Temperatures"]
  Sub01 --> Sub01A02["ANCHOR<br/>rank 45: Largest Rectangle In Histogram"]
  Sub01 --> Sub01A03["ANCHOR<br/>rank 106: Next Greater Element Ii"]
  Invariant --> Sub02["SUB-PATTERN<br/>Stack<br/>2 problem(s)"]
  Sub02 --> Sub02A01["ANCHOR<br/>rank 33: Valid Parentheses"]
  Sub02 --> Sub02A02["ANCHOR<br/>rank 108: Evaluate Reverse Polish Notation"]
  Invariant --> Sub03["SUB-PATTERN<br/>Stack / expression parsing<br/>1 problem(s)"]
  Sub03 --> Sub03A01["ANCHOR<br/>rank 110: Basic Calculator"]
  Invariant --> Sub04["SUB-PATTERN<br/>Stack design<br/>5 problem(s)"]
  Sub04 --> Sub04A01["ANCHOR<br/>rank 131: Min Stack"]
  Sub04 --> Sub04A02["ANCHOR<br/>rank 132: Max Stack"]
  Sub04 --> Sub04A03["ANCHOR<br/>rank 135: Next Greater Element I"]
  Invariant --> Sub05["SUB-PATTERN<br/>Stack/queue design<br/>4 problem(s)"]
  Sub05 --> Sub05A01["ANCHOR<br/>rank 96: Sliding Window Maximum"]
  Sub05 --> Sub05A02["ANCHOR<br/>rank 133: Implement Queue Using Stacks"]
  Sub05 --> Sub05A03["ANCHOR<br/>rank 134: Implement Stack Using Queues"]
```

## Problems

| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 32 | Phase 2 - Strong Core | Daily Temperatures | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session1/monotonic/DailyTemperatures.java) | [LC](https://leetcode.com/problems/daily-temperatures/) | Keep indices of days waiting for a warmer temperature; current day resolves colder previous days. | While current temp is warmer than stack top, pop index and set answer to current - popped. |
| 33 | Phase 2 - Strong Core | Valid Parentheses | Stack | [Java](../../../src/main/java/org/chijai/day5/stack/session3/ValidParentheses.java) | [LC](https://leetcode.com/problems/valid-parentheses/) | Every closing bracket must match the most recent unmatched opening bracket. | Push opening brackets; on closing, fail if stack empty or top is not its matching opener. |
| 45 | Phase 2 - Strong Core | Largest Rectangle In Histogram | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java) | [LC](https://leetcode.com/problems/largest-rectangle-in-histogram/) | When a shorter bar arrives, popped bars know their maximal rectangle width. | Append sentinel zero, keep increasing indices, pop and compute height * width when current is smaller. |
| 96 | Phase 3 - Important | Sliding Window Maximum | Stack/queue design | [Java](../../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) | [LC](https://leetcode.com/problems/sliding-window-maximum/) | A decreasing deque stores candidate indices; front is always the current window maximum. | Drop out-of-window front, pop smaller/equal from back, push index, read front after first window. |
| 106 | Phase 3 - Important | Next Greater Element Ii | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session1/monotonic/NextGreaterElement.java) | [LC](https://leetcode.com/problems/next-greater-element-ii/) | Loop twice over the circular array while a decreasing stack waits for next greater values. | For i in 0..2n-1, resolve stack with nums[i % n], push i only during first pass. |
| 107 | Phase 3 - Important | Sum Of Subarray Minimums | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java) | [LC](https://leetcode.com/problems/sum-of-subarray-minimums/) | Each element contributes as minimum for leftChoices times rightChoices subarrays. | Find previous less and next less-or-equal distances, sum arr[i] * left * right modulo M. |
| 108 | Phase 3 - Important | Evaluate Reverse Polish Notation | Stack | [Java](../../../src/main/java/org/chijai/day5/stack/session3/EvalRPN.java) | [LC](https://leetcode.com/problems/evaluate-reverse-polish-notation/) | Postfix expression evaluates when each operator consumes the latest two operands from a stack. | Push numbers; on operator pop b then a, compute a op b, push result. |
| 110 | Phase 3 - Important | Basic Calculator | Stack / expression parsing | [Java](../../../src/main/java/org/chijai/day5/stack/session3/BasicCalculator.java) | [LC](https://leetcode.com/problems/basic-calculator/) | Use sign and stack to preserve the expression value before each parenthesis. | Track result, sign, number; on '(' push result/sign and reset; on ')' fold into previous context. |
| 130 | Phase 4 - Secondary | Maximal Rectangle | Monotonic stack | [Java](../../../src/main/java/org/chijai/day5/stack/session1/monotonic/LargestRectangle.java) | [LC](https://leetcode.com/problems/maximal-rectangle/) | Treat every matrix row as histogram heights and run largest-rectangle on each row. | Update heights per row, then compute largest histogram area with monotonic stack. |
| 131 | Phase 4 - Secondary | Min Stack | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) | [LC](https://leetcode.com/problems/min-stack/) | Store the current minimum with each push, or keep a second stack of minimums. | Push value and min(value,currentMin); pop both together; getMin reads min top. |
| 132 | Phase 4 - Secondary | Max Stack | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) | [LC](https://leetcode.com/problems/max-stack/) | Maintain stack order plus a way to locate/remove the current maximum. | Use stack plus max tracking, or doubly linked list plus TreeMap for O(log n) popMax. |
| 133 | Phase 4 - Secondary | Implement Queue Using Stacks | Stack/queue design | [Java](../../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) | [LC](https://leetcode.com/problems/implement-queue-using-stacks/) | Use input stack for pushes and output stack for pops; transfer only when output is empty. | push -> in.push; pop/peek -> if out empty move all in to out, then read out. |
| 134 | Phase 4 - Secondary | Implement Stack Using Queues | Stack/queue design | [Java](../../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) | [LC](https://leetcode.com/problems/implement-stack-using-queues/) | After each push, rotate the queue so the newest element is at the front. | Offer x, then rotate size-1 older elements behind it; pop removes queue front. |
| 135 | Phase 4 - Secondary | Next Greater Element I | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) | [LC](https://leetcode.com/problems/next-greater-element-i/) | Precompute next greater for nums2 with a decreasing stack, then answer nums1 by map lookup. | Scan nums2, pop smaller values and map them to current, then lookup each nums1 value. |
| 136 | Phase 4 - Secondary | Online Stock Span | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) | [LC](https://leetcode.com/problems/online-stock-span/) | A decreasing stack of price/span pairs merges all previous prices <= current price. | Start span=1, while stack top price <= current add its span and pop, then push current/span. |
| 148 | Phase 4 - Secondary | Design A Stack With Increment Operation | Stack design | [Java](../../../src/main/java/org/chijai/day5/stack/session2/MinStackDesign.java) | [LC](https://leetcode.com/problems/design-a-stack-with-increment-operation/) | Lazy increment stores pending additions at the boundary index instead of touching k items. | Keep stack plus inc array; on pop carry inc[i] to inc[i-1] and return value + inc[i]. |
| 149 | Phase 4 - Secondary | Design Circular Queue | Stack/queue design | [Java](../../../src/main/java/org/chijai/day5/stack/session2/StackQueue.java) | [LC](https://leetcode.com/problems/design-circular-queue/) | Circular queue uses head, size, and modulo arithmetic to reuse fixed array slots. | enQueue writes at (head + size) % capacity; deQueue advances head and decrements size. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.