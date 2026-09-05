package org.chijai.day5.stack.session2;

/**
 * =====================================================================================
 * AUGMENTED STACK DESIGN — V2
 * =====================================================================================
 * LeetCode 155 - Min Stack
 * LeetCode 1381 - Design a Stack With Increment Operation
 *
 * MASTER QUESTION
 * ---------------
 * What if a stack must remember MORE than just the pushed value?
 *
 * Keep the normal LIFO behavior, but attach/preserve extra state that lets us answer
 * additional operations efficiently.
 *
 * This file intentionally contains one coherent family:
 *
 *   1) MinStack — TWO useful O(1) representations
 *      A. valueStack + minStack
 *      B. Node(value, minSoFar, next)
 *
 *   2) MaxStack / getMax — direct min -> max mutation
 *
 *   3) CustomStack Increment — adjacent augmentation idea using DEFERRED state
 *
 * IMPORTANT
 * ---------
 * The already-working implementations are preserved rather than rewritten into
 * alternative Java collection types or stylistic variants.
 */
public class MinStackDesign {

    /*
     * =================================================================================
     * PART 1 — MIN STACK
     * =================================================================================
     *
     * REQUIREMENT
     * -----------
     * Support:
     *
     *   push
     *   pop
     *   top
     *   getMin
     *
     * all in O(1).
     *
     * ------------------------------------------------------------
     * FIRST-PRINCIPLES FAILURE
     * ------------------------------------------------------------
     *
     * Suppose we keep only one global variable:
     *
     *   push(5) -> min = 5
     *   push(3) -> min = 3
     *   pop()   -> removes 3
     *
     * Now:
     *
     *   previous minimum = ???
     *
     * The real problem is NOT:
     *   "How do I know the current minimum?"
     *
     * It is:
     *   "How do I restore the PREVIOUS minimum after destructive pop()?"
     *
     * Therefore:
     *
     *   CURRENT MINIMUM IS NOT ENOUGH.
     *   WE NEED MINIMUM HISTORY.
     *
     * Core invariant:
     *   At every observable stack depth, the correct minimum for that depth
     *   is preserved and can be restored in O(1).
     */


    /*
     * =================================================================================
     * SOLUTION 1 — TWO STACKS
     * =================================================================================
     *
     * MENTAL MODEL
     * ------------
     *
     *   valueStack = actual stack values
     *   minStack   = history of minimum transitions
     *
     * Example:
     *
     * push(5)
     *   valueStack = [5]
     *   minStack   = [5]
     *
     * push(1)
     *   valueStack = [5,1]
     *   minStack   = [5,1]
     *
     * push(6)
     *   valueStack = [5,1,6]
     *   minStack   = [5,1]
     *
     * push(0)
     *   valueStack = [5,1,6,0]
     *   minStack   = [5,1,0]
     *
     * pop 0
     *   0 is also minStack.peek(), so remove it there too.
     *
     * Immediately:
     *   minStack.peek() == 1
     *
     * Previous minimum restored without scanning.
     *
     * ------------------------------------------------------------
     * DUPLICATE-MINIMUM TRAP
     * ------------------------------------------------------------
     *
     * push 2
     * push 2
     *
     * minStack must remember BOTH minimum occurrences:
     *
     *   [2,2]
     *
     * Therefore push uses:
     *
     *   val <= minStack.peek()
     *
     * not merely <.
     *
     * Otherwise popping one 2 could lose the fact that another 2 remains.
     *
     * COMPLEXITY
     * ----------
     * push()   O(1)
     * pop()    O(1)
     * top()    O(1)
     * getMin() O(1)
     * space    O(n)
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


    /*
     * =================================================================================
     * SOLUTION 2 — STORE minSoFar AT EVERY STACK DEPTH
     * =================================================================================
     *
     * MENTAL MODEL
     * ------------
     * Every pushed node carries a receipt:
     *
     *   "If the stack ended HERE, the minimum would be minSoFar."
     *
     * Example:
     *
     * push(5), push(3), push(7), push(2)
     *
     *                 value | minSoFar
     *   TOP             2   |    2
     *                   7   |    3
     *                   3   |    3
     *                   5   |    5
     *
     * pop() removes:
     *
     *                   2   |    2
     *
     * and reveals:
     *
     *   TOP             7   |    3
     *
     * The previous minimum was never recomputed.
     * It was already stored at the previous stack depth.
     *
     * ------------------------------------------------------------
     * CORE INVARIANT
     * ------------------------------------------------------------
     *
     *   Node.minSoFar = minimum of every value from the bottom
     *                    through this node.
     *
     * Therefore the top node always contains the minimum for the
     * entire current stack.
     *
     * ------------------------------------------------------------
     * IMPORTANT COMPARISON
     * ------------------------------------------------------------
     *
     * TWO STACKS                 NODE + minSoFar
     * ----------                 ---------------
     * O(1) all operations        O(1) all operations
     * O(n) space                 O(n) space
     *
     * Neither is asymptotically more optimal.
     * They are two representations of the SAME historical-state invariant.
     *
     * Interview derivation often comes naturally from the two-stack version.
     * The Node version makes the invariant extremely explicit.
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


    /*
     * =================================================================================
     * WHICH MINSTACK REPRESENTATION SHOULD I REMEMBER?
     * =================================================================================
     *
     * FIRST DERIVATION:
     *   "I need previous minimums after pop."
     *       ↓
     *   keep a minStack history.
     *
     * DEEPER INVARIANT:
     *   "Every stack depth can carry the answer for that depth."
     *       ↓
     *   Node(value, minSoFar, next).
     *
     * 30-SECOND LINE:
     *   MinStack is not mainly about FINDING the minimum.
     *   It is about NOT LOSING the previous minimum after pop().
     */


    /*
     * =================================================================================
     * PART 2 — DIRECT VARIATION: MAX STACK / getMax()
     * =================================================================================
     *
     * This SHOULD stay in the same file.
     *
     * Why?
     * The invariant changes by exactly one word:
     *
     *   minimum history -> maximum history
     *
     * Mechanical mutation:
     *
     *   minSoFar -> maxSoFar
     *   Math.min -> Math.max
     *
     * NOTE:
     * This implementation supports stack operations + peekMax()/getMax-style behavior.
     * The full LeetCode Max Stack follow-up with popMax() is a different boundary,
     * because popMax() may remove a NON-TOP element.
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
     * =================================================================================
     * FOLLOW-UP — WHAT IF BOTH getMin() AND getMax() ARE REQUIRED?
     * =================================================================================
     *
     * Same idea; each depth can preserve both aggregates:
     *
     *   [ value | minSoFar | maxSoFar ]
     *
     * On push(value):
     *
     *   newMin = min(value, previous.minSoFar)
     *   newMax = max(value, previous.maxSoFar)
     *
     * On pop():
     *   reveal the previous node, which already contains both answers.
     *
     * No new pattern is required, so a separate mastery file would add little value.
     */


    /*
     * =================================================================================
     * PART 3 — CUSTOM STACK WITH INCREMENT
     * =================================================================================
     *
     * WHY KEEP IT HERE?
     * -----------------
     * It is not the SAME invariant as MinStack, but it belongs to the same broader
     * "stack + extra state" design family and is not large enough to deserve a
     * separate mastery file.
     *
     * MinStack:
     *   preserve HISTORICAL aggregate state.
     *
     * CustomStack:
     *   preserve DEFERRED work.
     *
     * ------------------------------------------------------------
     * PROBLEM
     * ------------------------------------------------------------
     * increment(k, val) adds val to the bottom k current stack elements.
     *
     * Naive:
     *   physically update k values -> O(k).
     *
     * Better:
     *   record the increment only at the HIGHEST affected stack index.
     *
     * increment[i] means:
     *   "this pending amount belongs to element i and must eventually
     *    flow to every affected element below it."
     *
     * When element i is popped:
     *
     *   increment[i - 1] += increment[i]
     *
     * This passes the deferred work downward exactly when needed.
     *
     * ------------------------------------------------------------
     * DRY RUN
     * ------------------------------------------------------------
     *
     * push 1,2,3
     * increment(2, 5)
     *
     * values:     [1,2,3]
     * increment:  [0,5,0]
     *                 ^
     *                 highest of bottom 2 affected elements
     *
     * pop 3 -> 3
     *
     * pop 2:
     *   result = 2 + 5 = 7
     *   pass 5 downward:
     *   increment[0] += 5
     *
     * pop 1:
     *   result = 1 + 5 = 6
     *
     * COMPLEXITY
     * ----------
     * push()      O(1)
     * pop()       O(1)
     * increment() O(1)
     * space       O(maxSize)
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
     * =================================================================================
     * PATTERN BOUNDARY — WHAT DOES NOT BELONG HERE?
     * =================================================================================
     *
     * KEEP HERE
     * ---------
     * MinStack
     * getMax variation
     * getMin + getMax follow-up
     * CustomStack increment
     *
     * because each is a stack-design problem where extra state is attached/preserved.
     *
     * MOVE / KEEP ELSEWHERE
     * ---------------------
     * Next Greater Element
     * Daily Temperatures
     * Online Stock Span
     * Largest Rectangle
     *   -> MONOTONIC STACK
     *
     * Valid Parentheses
     * Eval RPN
     * Basic Calculator
     *   -> LIFO MATCHING / EXPRESSION EVALUATION
     *
     * Full MaxStack.popMax()
     *   -> arbitrary non-top removal; different design requirement
     */


    /*
     * =================================================================================
     * 30-SECOND RECALL CARD
     * =================================================================================
     *
     * MIN STACK
     * ---------
     * Current min is easy.
     * Previous min after pop is the problem.
     *
     * Preserve history as either:
     *
     *   1) valueStack + minStack
     *   2) [value | minSoFar] at every depth
     *
     * MAX
     * ---
     * Same invariant:
     *   min -> max
     *
     * CUSTOM INCREMENT
     * ----------------
     * Don't eagerly touch bottom k.
     * Store pending work at the highest affected depth.
     * Pass it downward on pop.
     *
     * MASTER IDEA
     * -----------
     * A stack can carry VALUE + EXTRA STATE.
     */


    public static void main(String[] args) {
        testMinStackTwoStacks();
        testMinStackNode();
        testMaxStack();
        testCustomStack();

        System.out.println("AugmentedStackDesign_v2: all tests passed.");
    }

    private static void testMinStackTwoStacks() {
        MinStackTwoStacks stack = new MinStackTwoStacks();

        stack.push(5);
        stack.push(2);
        stack.push(2);
        stack.push(7);

        assertEquals(2, stack.getMin(), "two-stack current min");
        stack.pop(); // 7
        assertEquals(2, stack.getMin(), "non-min pop keeps min");
        stack.pop(); // first 2
        assertEquals(2, stack.getMin(), "duplicate min survives");
        stack.pop(); // second 2
        assertEquals(5, stack.getMin(), "previous min restored");
    }

    private static void testMinStackNode() {
        MinStack stack = new MinStack();

        stack.push(5);
        stack.push(3);
        stack.push(7);
        stack.push(2);

        assertEquals(2, stack.getMin(), "node current min");
        stack.pop();
        assertEquals(3, stack.getMin(), "node previous min restored");
        assertEquals(7, stack.top(), "node top");
    }

    private static void testMaxStack() {
        MaxStack stack = new MaxStack();

        stack.push(4);
        stack.push(9);
        stack.push(3);

        assertEquals(9, stack.peekMax(), "current max");
        stack.pop();
        assertEquals(9, stack.peekMax(), "smaller pop keeps max");
        stack.pop();
        assertEquals(4, stack.peekMax(), "previous max restored");
    }

    private static void testCustomStack() {
        CustomStack stack = new CustomStack(3);

        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.increment(2, 5);

        assertEquals(3, stack.pop(), "unaffected top");
        assertEquals(7, stack.pop(), "incremented second");
        assertEquals(6, stack.pop(), "increment propagated to bottom");
        assertEquals(-1, stack.pop(), "empty stack");
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(
                    message + " | expected=" + expected + ", actual=" + actual
            );
        }
    }
}
