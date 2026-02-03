package org.chijai.day5.session1;

/**
 * =====================================================================================
 * 1️⃣ TOP-LEVEL PUBLIC CLASS DECLARATION
 * =====================================================================================
 *
 * This file is a COMPLETE, invariant-first algorithm textbook chapter.
 * It is designed to be read, re-derived, debugged, and taught months later
 * without reopening LeetCode or the internet.
 *
 * ⚠️ This file is intentionally long and explicit.
 * ⚠️ No Java implicit behavior is relied upon without explanation.
 */
public class MinStackDesign {

// =================================================================================
// 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT (VERBATIM)
// =================================================================================
/*
 * Design a stack that supports push, pop, top, and retrieving the minimum element
 * in constant time.
 *
 * Implement the MinStack class:
 *
 * MinStack() initializes the stack object.
 * void push(int val) pushes the element val onto the stack.
 * void pop() removes the element on the top of the stack.
 * int top() gets the top element of the stack.
 * int getMin() retrieves the minimum element in the stack.
 *
 * You must implement a solution with O(1) time complexity for each function.
 *
 * Example 1:
 *
 * Input
 * ["MinStack","push","push","push","getMin","pop","top","getMin"]
 * [[],[-2],[0],[-3],[],[],[],[]]
 *
 * Output
 * [null,null,null,null,-3,null,0,-2]
 *
 * Explanation
 * MinStack minStack = new MinStack();
 * minStack.push(-2);
 * minStack.push(0);
 * minStack.push(-3);
 * minStack.getMin(); // return -3
 * minStack.pop();
 * minStack.top();    // return 0
 * minStack.getMin(); // return -2
 *
 * Constraints:
 *
 * -2^31 <= val <= 2^31 - 1
 * Methods pop, top and getMin operations will always be called on non-empty stacks.
 * At most 3 * 10^4 calls will be made to push, pop, top, and getMin.
 *
 * 🔗 Official Link: https://leetcode.com/problems/min-stack/
 * 🧩 Difficulty: Medium
 * 🏷️ Tags: Stack, Design
 */

// =================================================================================
// 3️⃣ 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST · FULL)
// =================================================================================
/*
 * 🔵 Pattern Name:
 * Augmented Stack with Historical State Preservation
 *
 * 🔵 Problem Archetype:
 * A LIFO data structure that must answer an aggregate query (minimum)
 * in constant time, even after destructive operations (pop).
 *
 * 🟢 Core Invariant (MANDATORY — ONE SENTENCE):
 * At every stack depth, the minimum of all elements below and including that depth
 * is explicitly known and preserved.
 *
 * 🔵 Why this invariant makes the pattern work:
 * The difficulty of the problem is not finding a minimum, but NOT LOSING IT
 * when elements are popped. By storing the minimum-at-this-depth alongside
 * each element, no pop operation can destroy historical information.
 *
 * 🔵 When this pattern applies:
 * • Stack-based problems
 * • O(1) access to min/max/aggregate required
 * • Operations are strictly LIFO
 *
 * 🔵 🧭 Pattern recognition signals:
 * • “Design a stack” + “getMin / getMax in O(1)”
 * • Query must remain valid after pop
 * • Constraints disallow rescanning
 *
 * 🔵 How this pattern differs from similar patterns:
 * • Unlike prefix arrays → supports pop
 * • Unlike heaps → supports O(1) top/pop
 * • Unlike global variables → preserves history
 */

// =================================================================================
// 4️⃣ 🟢 MENTAL MODEL & INVARIANTS (CANONICAL SECTION)
// =================================================================================
/*
 * 🟢 Mental Model (HOW TO THINK, NOT CODE):
 *
 * Imagine each element pushed onto the stack carries a “receipt” that says:
 * “If the stack were cut off here, THIS is the minimum.”
 *
 * When you pop, you simply throw away the top receipt and reveal the previous one.
 *
 * 🟢 Base State (EXPLICIT — NO IMPLICIT ASSUMPTIONS):
 *
 * • An empty stack has:
 *   - no elements
 *   - no minimum
 *   - top == null
 *
 * This base state represents the EMPTY invariant.
 * The first push establishes the invariant.
 *
 * 🟢 Invariants (COMPLETE AND EXPLICIT):
 *
 * Invariant 1:
 * Every node in the stack stores:
 * • its own value
 * • the minimum value of all nodes below it (inclusive)
 *
 * Invariant 2:
 * The minimum of the entire stack is ALWAYS available at the top node.
 *
 * 🟢 State Representation (WHAT VARIABLES MEAN):
 *
 * Node.value      → actual element pushed by the user
 * Node.minSoFar   → minimum from bottom of stack up to THIS node
 * Node.next       → next node below in the stack
 * top             → top of the stack (latest pushed element)
 *
 * 🟢 Allowed Moves (INVARIANT-PRESERVING):
 *
 * • push(val):
 *   - If stack is empty → minSoFar = val
 *   - Else → minSoFar = min(val, previous.minSoFar)
 *
 * • pop():
 *   - Discard top node
 *   - Previous node automatically restores previous minimum
 *
 * 🟢 Forbidden Moves (INVARIANT-BREAKING):
 *
 * ❌ Recomputing min by scanning
 * ❌ Using a single global min variable
 * ❌ Forgetting historical minimums
 *
 * 🟢 Termination Logic:
 *
 * Each operation:
 * • moves a constant number of pointers
 * • performs constant comparisons
 * Therefore, termination is guaranteed in O(1).
 *
 * 🟢 Why common alternatives are inferior:
 *
 * • Global min breaks when min element is popped
 * • Re-scan violates time constraints
 * • Heap violates stack semantics
 */

// =================================================================================
// 5️⃣ 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
// =================================================================================
/*
 * 🔴 Wrong Approach 1: Track a single global minimum
 *
 * Why it seems correct:
 * • getMin() becomes O(1)
 * • push updates min easily
 *
 * Why it FAILS:
 * • When the minimum element is popped, you no longer know the previous minimum
 *
 * Invariant violated:
 * ❌ “At every depth, minimum is known”
 *
 * Minimal Counterexample:
 *
 * push(5)
 * push(3)   → min = 3
 * pop()     → min SHOULD be 5, but information is lost
 *
 * 🔴 Wrong Approach 2: Scan stack on getMin()
 *
 * Why it seems correct:
 * • Always returns correct minimum
 *
 * Why it FAILS:
 * • getMin() becomes O(n)
 *
 * Invariant violated:
 * ❌ Constant-time aggregate access
 *
 * 🔴 Interviewer Trap:
 * “What happens when the minimum element is popped?”
 *
 * Candidates without invariant thinking cannot answer this confidently.
 */

    // =================================================================================
    // 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES (DERIVED FROM INVARIANT)
    // =================================================================================

    // ---------------------------------------------------------------------------------
    // 🔴 BRUTE FORCE SOLUTION
    // ---------------------------------------------------------------------------------
    /*
     * 🔴 Core Idea:
     * Store all elements normally. Whenever getMin() is called,
     * scan the entire stack to find the minimum.
     *
     * 🟢 Invariant enforced:
     * NONE. The minimum is recomputed every time.
     *
     * 🔴 Why this is insufficient:
     * Violates the O(1) requirement for getMin().
     *
     * ⏱ Time Complexity:
     * • push → O(1)
     * • pop  → O(1)
     * • top  → O(1)
     * • getMin → O(n)
     *
     * 📦 Space Complexity:
     * • O(n)
     *
     * 🟣 Interview Preference:
     * ❌ Rejected after clarification of constraints.
     */
    static class MinStackBruteForce {

        private final java.util.Stack<Integer> valueStack;

        public MinStackBruteForce() {
            // 🟢 Base invariant:
            // Empty stack → no elements → no minimum
            this.valueStack = new java.util.Stack<>();
        }

        public void push(int val) {
            valueStack.push(val);
        }

        public void pop() {
            valueStack.pop();
        }

        public int top() {
            return valueStack.peek();
        }

        public int getMin() {
            int currentMinimum = Integer.MAX_VALUE;

            // 🔴 Full scan violates O(1)
            for (int value : valueStack) {
                currentMinimum = Math.min(currentMinimum, value);
            }

            return currentMinimum;
        }
    }

    // ---------------------------------------------------------------------------------
    // 🟡 IMPROVED SOLUTION — TWO STACKS
    // ---------------------------------------------------------------------------------
    /*
     * 🟡 Core Idea:
     * Maintain a secondary stack that tracks minimum values as they evolve.
     *
     * 🟢 Invariant (PARTIALLY ENFORCED):
     * The top of minStack always stores the current minimum.
     *
     * 🟡 Limitation fixed:
     * No rescanning required for getMin().
     *
     * ⏱ Time Complexity:
     * • All operations → O(1)
     *
     * 📦 Space Complexity:
     * • O(n) extra space for minStack
     *
     * 🟣 Interview Preference:
     * ✅ Acceptable, but not minimal
     */

    /*
     * 🧪 DRY RUN — ALTERNATING PEAKS & VALLEYS (INVARIANT PROOF)
     *
     * Operations:
     * push(5), push(1), push(6), push(0), push(7), push(2)
     *
     * After pushes:
     * valueStack = [5, 1, 6, 0, 7, 2]
     * minStack   = [5, 1, 0]        // only when new minimum appears
     *
     * Pops:
     * pop 2 → min unchanged
     * pop 7 → min unchanged
     * pop 0 → min pops → new min = 1
     * pop 6 → min unchanged
     * pop 1 → min pops → new min = 5
     * pop 5 → min pops → stack empty
     *
     * Invariant check (always true):
     * minStack.peek() == min(valueStack)
     *
     * Key insight:
     * minStack stores MIN-TRANSITIONS, not all values.
     * LIFO guarantees minimums are removed in reverse order.
     */


    static class MinStackTwoStacks {

        private final java.util.Stack<Integer> valueStack;
        private final java.util.Stack<Integer> minStack;

        public MinStackTwoStacks() {
            // 🟢 Base invariant:
            // Both stacks empty → no minimum defined
            this.valueStack = new java.util.Stack<>();
            this.minStack = new java.util.Stack<>();
        }

        public void push(int val) {
            valueStack.push(val);

            // 🟢 Preserve invariant:
            // Push into minStack ONLY when new minimum appears
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            }
        }

        public void pop() {
            int removedValue = valueStack.pop();

            // 🟡 Synchronization rule:
            // Only pop from minStack if the removed value was the minimum
            if (removedValue == minStack.peek()) {
                minStack.pop();
            }
        }

        public int top() {
            return valueStack.peek();
        }

        public int getMin() {
            return minStack.peek();
        }
    }

    // ---------------------------------------------------------------------------------
    // 🟢 OPTIMAL SOLUTION — INVARIANT-EMBEDDED STACK (INTERVIEW-PREFERRED)
    // ---------------------------------------------------------------------------------
    /*
     * 🟢 Core Idea:
     * Embed the invariant directly into each node.
     *
     * Every node stores:
     * • its own value
     * • the minimum of all values beneath it (inclusive)
     *
     * 🟢 Fully Enforced Invariant:
     * At every depth, the minimum-so-far is explicitly preserved.
     *
     * ⏱ Time Complexity:
     * • push → O(1)
     * • pop  → O(1)
     * • top  → O(1)
     * • getMin → O(1)
     *
     * 📦 Space Complexity:
     * • O(n), no auxiliary structures
     *
     * 🟣 Interview Preference:
     * ⭐ Strongly preferred — simplest invariant, strongest guarantees
     */
    static class MinStack {

        // 🟢 Represents the top of the stack
        private Node top;

        // 🟢 Explicit constructor — NO implicit assumptions
        public MinStack() {
            /*
             * Base invariant:
             * An empty stack has no nodes and no minimum.
             * top == null represents this state explicitly.
             */
            this.top = null;
        }

        // ---------------------------------------------------------------------------------
        // 🟢 NODE DEFINITION — INVARIANT CARRIER
        // ---------------------------------------------------------------------------------
        private static class Node {
            int value;       // actual value pushed
            int minSoFar;    // minimum from bottom → this node
            Node next;       // next node below

            Node(int value, int minSoFar, Node next) {
                this.value = value;
                this.minSoFar = minSoFar;
                this.next = next;
            }
        }

        public void push(int val) {

            if (top == null) {
                // 🟢 First push establishes invariant
                top = new Node(val, val, null);
            } else {
                // 🟢 Preserve invariant by comparing with previous min
                int updatedMinimum = Math.min(val, top.minSoFar);
                top = new Node(val, updatedMinimum, top);
            }
        }

        public void pop() {
            /*
             * 🟢 Invariant-safe removal:
             * Discarding top node reveals previous node,
             * which already knows the correct minimum.
             */
            top = top.next;
        }

        public int top() {
            return top.value;
        }

        public int getMin() {
            return top.minSoFar;
        }
    }

// =================================================================================
// 🔜 NEXT PART:
// 7️⃣ 🟣 INTERVIEW ARTICULATION
// 8️⃣ 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
// =================================================================================

// =================================================================================
// 7️⃣ 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · FULL)
// =================================================================================
/*
 * 🟣 How to explain the solution WITHOUT code:
 *
 * Step 1 — State the invariant:
 * “At every stack depth, I explicitly store the minimum of all elements
 * below and including that depth.”
 *
 * Step 2 — Explain push:
 * • If the stack is empty, the pushed value is trivially the minimum.
 * • Otherwise, compare the new value with the previous minimum and store
 *   the smaller one alongside the value.
 *
 * Step 3 — Explain pop:
 * • Pop simply discards the top node.
 * • The next node already knows the correct historical minimum.
 *
 * Step 4 — Why correctness is guaranteed:
 * • No operation ever destroys minimum history.
 * • getMin() is a constant-time read from the top node.
 *
 * Step 5 — What breaks if logic changes:
 * • Removing minSoFar from the node breaks pop().
 * • Using a single global min breaks when min is popped.
 *
 * Step 6 — In-place feasibility:
 * • Yes. Single linked structure.
 *
 * Step 7 — Streaming feasibility:
 * • Yes. Operations are independent and online.
 *
 * Step 8 — When NOT to use this pattern:
 * • When random access is needed
 * • When operations are not LIFO
 */

// =================================================================================
// 8️⃣ 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
// =================================================================================
/*
 * 🟢 Invariant-Preserving Changes:
 *
 * • MaxStack:
 *   Replace minSoFar with maxSoFar.
 *
 * • Pair-based implementation:
 *   Store (value, minSoFar) as a pair instead of a Node.
 *
 * 🟡 Reasoning-Only Changes:
 *
 * • Using <= instead of < when comparing values
 *   (important for duplicates).
 *
 * • Returning OptionalInt instead of int
 *   (does not affect invariant, only API).
 *
 * 🔴 Pattern-Break Signals:
 *
 * • Need to delete arbitrary elements
 * • Need to access minimum excluding top
 * • Need to support undo beyond LIFO
 *
 * In these cases, the invariant collapses and a different data structure
 * (heap, tree, deque) is required.
 */


    // =================================================================================
    // 9️⃣ ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS · INVARIANT-REUSED)
    // =================================================================================

    // ================================================================================
    // ⚫ REINFORCEMENT 1: MAX STACK
    // ================================================================================
    /*
     * 📘 FULL OFFICIAL LEETCODE PROBLEM STATEMENT
     *
     * Design a max stack data structure that supports the stack operations and supports
     * finding the stack's maximum element.
     *
     * Implement the MaxStack class:
     *
     * MaxStack() initializes the stack object.
     * void push(int x) pushes element x onto the stack.
     * int pop() removes the element on top of the stack and returns it.
     * int top() gets the element on the top of the stack.
     * int peekMax() retrieves the maximum element in the stack.
     * int popMax() removes and returns the maximum element in the stack.
     *
     * 🔗 https://leetcode.com/problems/max-stack/
     * 🧩 Difficulty: Hard
     * 🏷️ Tags: Stack, Design
     *
     * ⚠️ NOTE:
     * For invariant reuse, we implement the O(1) peekMax variant
     * (not the ordered popMax variant).
     */

    /*
     * ⚫ INVARIANT MAPPING
     *
     * Same invariant as MinStack, inverted:
     * • At every stack depth, the maximum of all elements below (inclusive) is known.
     *
     * What remains unchanged:
     * • LIFO structure
     * • Historical aggregate preservation
     *
     * What changes:
     * • minSoFar → maxSoFar
     */

    static class MaxStack {

        private MaxNode top;

        public MaxStack() {
            // 🟢 Base invariant: empty stack → no maximum
            this.top = null;
        }

        private static class MaxNode {
            int value;
            int maxSoFar;
            MaxNode next;

            MaxNode(int value, int maxSoFar, MaxNode next) {
                this.value = value;
                this.maxSoFar = maxSoFar;
                this.next = next;
            }
        }

        public void push(int val) {
            if (top == null) {
                top = new MaxNode(val, val, null);
            } else {
                int updatedMax = Math.max(val, top.maxSoFar);
                top = new MaxNode(val, updatedMax, top);
            }
        }

        public int pop() {
            int removed = top.value;
            top = top.next;
            return removed;
        }

        public int top() {
            return top.value;
        }

        public int peekMax() {
            return top.maxSoFar;
        }
    }

    /*
     * 🧪 EDGE CASE & TRAP
     *
     * Trap:
     * Forgetting to propagate maxSoFar breaks pop().
     *
     * Interview follow-up:
     * “How would you support popMax()?”
     * → Requires doubly linked list + TreeMap.
     */

    // ================================================================================
    // ⚫ REINFORCEMENT 2: STACK WITH INCREMENT OPERATION
    // ================================================================================
    /*
     * 📘 FULL OFFICIAL LEETCODE PROBLEM STATEMENT
     *
     * Design a stack that supports increment operations on its bottom elements.
     *
     * Implement the CustomStack class:
     *
     * CustomStack(int maxSize) initializes the object with maxSize.
     * void push(int x) pushes x onto the stack if not full.
     * int pop() pops and returns top element or -1 if empty.
     * void increment(int k, int val) increments bottom k elements by val.
     *
     * 🔗 https://leetcode.com/problems/design-a-stack-with-increment-operation/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Stack, Design
     */

    /*
     * ⚫ INVARIANT MAPPING
     *
     * Core invariant reused:
     * • Deferred state preservation per depth.
     *
     * Modified invariant:
     * • Each index stores pending increment to apply.
     */

    static class CustomStack {

        private final int[] values;
        private final int[] increment;
        private int size;

        public CustomStack(int maxSize) {
            this.values = new int[maxSize];
            this.increment = new int[maxSize];
            this.size = 0;
        }

        public void push(int x) {
            if (size == values.length) return;
            values[size] = x;
            increment[size] = 0;
            size++;
        }

        public int pop() {
            if (size == 0) return -1;

            int index = size - 1;
            int result = values[index] + increment[index];

            if (index > 0) {
                increment[index - 1] += increment[index];
            }

            increment[index] = 0;
            size--;
            return result;
        }

        public void increment(int k, int val) {
            if (size == 0) return;
            int index = Math.min(k, size) - 1;
            increment[index] += val;
        }
    }

    /*
     * 🧪 EDGE CASE & TRAP
     *
     * Trap:
     * Incrementing bottom k eagerly → O(n).
     *
     * Interview follow-up:
     * “Why does lazy propagation work here?”
     */

    // ================================================================================
    // ⚫ REINFORCEMENT 3: ONLINE STOCK SPAN
    // ================================================================================
    /*
     * 📘 FULL OFFICIAL LEETCODE PROBLEM STATEMENT
     *
     * Write a class StockSpanner that collects daily price quotes and returns
     * the span of the stock’s price for the current day.
     *
     * The span is defined as the maximum number of consecutive days starting from
     * the current day and going backward for which the price was less than or equal
     * to the current price.
     *
     * 🔗 https://leetcode.com/problems/online-stock-span/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Stack, Monotonic Stack
     */

    /*
     * ⚫ INVARIANT MAPPING
     *
     * Same invariant idea:
     * • Stack stores compressed historical information.
     *
     * Modified invariant:
     * • Stack is monotonically decreasing.
     */

    static class StockSpanner {

        private static class PriceSpan {
            int price;
            int span;

            PriceSpan(int price, int span) {
                this.price = price;
                this.span = span;
            }
        }

        private final java.util.Stack<PriceSpan> stack;

        public StockSpanner() {
            this.stack = new java.util.Stack<>();
        }

        public int next(int price) {
            int span = 1;

            while (!stack.isEmpty() && stack.peek().price <= price) {
                span += stack.pop().span;
            }

            stack.push(new PriceSpan(price, span));
            return span;
        }
    }

    /*
     * 🧪 EDGE CASE & TRAP
     *
     * Trap:
     * Forgetting to accumulate spans during pop.
     *
     * Interview follow-up:
     * “Why is this amortized O(1)?”
     */

    // ================================================================================
    // ⚫ REINFORCEMENT 4: DAILY TEMPERATURES
    // ================================================================================
    /*
     * 📘 FULL OFFICIAL LEETCODE PROBLEM STATEMENT
     *
     * Given an array of integers temperatures represents the daily temperatures,
     * return an array answer such that answer[i] is the number of days you have to
     * wait after the ith day to get a warmer temperature.
     *
     * 🔗 https://leetcode.com/problems/daily-temperatures/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Stack, Monotonic Stack
     */

    /*
     * ⚫ INVARIANT COMPARISON
     *
     * Different invariant:
     * • Stack maintains indices of decreasing temperatures.
     *
     * Relationship:
     * • Same historical compression idea
     * • Different aggregate (next greater element)
     */

    static class DailyTemperatures {

        public int[] dailyTemperatures(int[] temperatures) {
            int n = temperatures.length;
            int[] result = new int[n];
            java.util.Stack<Integer> stack = new java.util.Stack<>();

            for (int i = 0; i < n; i++) {
                while (!stack.isEmpty()
                        && temperatures[i] > temperatures[stack.peek()]) {

                    int prevIndex = stack.pop();
                    result[prevIndex] = i - prevIndex;
                }
                stack.push(i);
            }
            return result;
        }
    }

// =================================================================================
// 🔜 NEXT PART:
// 10️⃣ 🧩 RELATED PROBLEMS
// 11️⃣ 🟢 LEARNING VERIFICATION
// 12️⃣ 🧪 main() + SELF-VERIFYING TESTS
// 13️⃣ ✅ COMPLETION CHECKLIST + FINAL CLOSURE
// =================================================================================



    // =================================================================================
    // 10️⃣ 🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
    // =================================================================================

    // -----------------------------------------------------------------------------
    // 🧩 RELATED PROBLEM 1: NEXT GREATER ELEMENT I
    // -----------------------------------------------------------------------------
    /*
     * 📘 FULL OFFICIAL LEETCODE PROBLEM STATEMENT
     *
     * The next greater element of some element x in an array is the first greater
     * element that is to the right of x in the same array.
     *
     * You are given two distinct 0-indexed integer arrays nums1 and nums2, where
     * nums1 is a subset of nums2.
     *
     * For each 0 <= i < nums1.length, find the index j such that nums1[i] == nums2[j]
     * and determine the next greater element of nums2[j] in nums2. If there is no
     * next greater element, then the answer for this query is -1.
     *
     * 🔗 https://leetcode.com/problems/next-greater-element-i/
     * 🧩 Difficulty: Easy
     * 🏷️ Tags: Stack, Monotonic Stack, HashMap
     */

    /*
     * 🧠 RELATIONSHIP TO PRIMARY INVARIANT
     *
     * • Modified invariant
     * • Stack preserves decreasing order to enable future resolution
     * • Unlike MinStack, the aggregate is directional (future-facing)
     */

    static class NextGreaterElementI {

        public int[] nextGreaterElement(int[] nums1, int[] nums2) {
            java.util.Map<Integer, Integer> nextGreater = new java.util.HashMap<>();
            java.util.Stack<Integer> stack = new java.util.Stack<>();

            for (int value : nums2) {
                while (!stack.isEmpty() && value > stack.peek()) {
                    nextGreater.put(stack.pop(), value);
                }
                stack.push(value);
            }

            int[] result = new int[nums1.length];
            for (int i = 0; i < nums1.length; i++) {
                result[i] = nextGreater.getOrDefault(nums1[i], -1);
            }
            return result;
        }
    }

    /*
     * 🧪 EDGE CASE + INTERVIEW NOTE
     *
     * Edge Case:
     * nums1 contains elements whose next greater does not exist.
     *
     * Interviewer asks this next to test:
     * • Understanding of monotonic stack vs aggregate-preserving stack
     */

    // -----------------------------------------------------------------------------
    // 🧩 RELATED PROBLEM 2: VALID PARENTHESES
    // -----------------------------------------------------------------------------
    /*
     * 📘 FULL OFFICIAL LEETCODE PROBLEM STATEMENT
     *
     * Given a string s containing just the characters '(', ')', '{', '}', '[' and ']',
     * determine if the input string is valid.
     *
     * 🔗 https://leetcode.com/problems/valid-parentheses/
     * 🧩 Difficulty: Easy
     * 🏷️ Tags: Stack, String
     */

    /*
     * 🧠 RELATIONSHIP TO PRIMARY INVARIANT
     *
     * • Invariant impossible to reuse
     * • Stack tracks structural correctness, not aggregate history
     * • Demonstrates pattern boundary
     */

    static class ValidParentheses {

        public boolean isValid(String s) {
            java.util.Stack<Character> stack = new java.util.Stack<>();

            for (char c : s.toCharArray()) {
                if (c == '(' || c == '{' || c == '[') {
                    stack.push(c);
                } else {
                    if (stack.isEmpty()) return false;
                    char open = stack.pop();
                    if (!matches(open, c)) return false;
                }
            }
            return stack.isEmpty();
        }

        private boolean matches(char open, char close) {
            return (open == '(' && close == ')')
                    || (open == '{' && close == '}')
                    || (open == '[' && close == ']');
        }
    }

    // =================================================================================
    // 11️⃣ 🟢 LEARNING VERIFICATION (INVARIANT-FIRST)
    // =================================================================================
    /*
     * 🟢 Invariant you must recall without code:
     * At every stack depth, the historical aggregate (minimum) is preserved.
     *
     * 🟢 Why naive approaches fail:
     * They destroy historical information on pop().
     *
     * 🟢 Bugs you should be able to debug intentionally:
     * • Forgetting to propagate minSoFar
     * • Using < instead of <=
     *
     * 🟢 How to detect this invariant in unseen problems:
     * • Stack + O(1) aggregate + destructive operations
     */

    // =================================================================================
    // 12️⃣ 🧪 main() METHOD + SELF-VERIFYING TESTS (MUST BE LAST)
    // =================================================================================
    public static void main(String[] args) {

        // -----------------------------
        // MinStack invariant tests
        // -----------------------------
        MinStack stack = new MinStack();

        stack.push(-2);
        stack.push(0);
        stack.push(-3);
        assertEquals(-3, stack.getMin(), "Min after pushes");

        stack.pop();
        assertEquals(0, stack.top(), "Top after pop");
        assertEquals(-2, stack.getMin(), "Min restored after pop");

        stack.push(-5);
        assertEquals(-5, stack.getMin(), "New min detected");

        // -----------------------------
        // MaxStack invariant test
        // -----------------------------
        MaxStack maxStack = new MaxStack();
        maxStack.push(1);
        maxStack.push(3);
        maxStack.push(2);
        assertEquals(3, maxStack.peekMax(), "Max invariant preserved");

        // -----------------------------
        // CustomStack test
        // -----------------------------
        CustomStack custom = new CustomStack(3);
        custom.push(1);
        custom.push(2);
        custom.increment(2, 5);
        assertEquals(7, custom.pop(), "Increment applied correctly");

        // -----------------------------
        // StockSpanner test
        // -----------------------------
        StockSpanner spanner = new StockSpanner();
        assertEquals(1, spanner.next(100), "Span day 1");
        assertEquals(1, spanner.next(80), "Span day 2");
        assertEquals(2, spanner.next(80), "Span accumulation");

        System.out.println("✅ All invariant tests passed.");
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(
                    message + " | expected=" + expected + ", actual=" + actual
            );
        }
    }

    // =================================================================================
    // 13️⃣ ✅ CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
    // =================================================================================
    /*
     * • Invariant clarity → Explicitly stated and enforced at every depth
     * • Search target clarity → Stack top
     * • Discard logic → Pop reveals historical state
     * • Termination guarantee → O(1) pointer transitions
     * • Failure awareness → Global min and rescanning fail
     * • Edge-case confidence → First push defines invariant
     * • Variant readiness → MaxStack, lazy increment, monotonic stacks
     * • Pattern boundary → Non-LIFO problems
     */

    // =================================================================================
    // 🧘 FINAL CLOSURE STATEMENT (PROBLEM-SPECIFIC)
    // =================================================================================
    /*
     * For this problem, the invariant is that every stack node stores the minimum
     * of all elements below it.
     * The answer represents the minimum at the current stack depth.
     * The search terminates because each operation performs constant-time transitions.
     * I can re-derive this solution under pressure.
     * This chapter is complete.
     */

}

