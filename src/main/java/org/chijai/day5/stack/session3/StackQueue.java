package org.chijai.day5.stack.session3;

/**
 * ============================================================
 * 📘 INVARIANT-FIRST ALGORITHM CHAPTER
 * ============================================================
 *
 * PRIMARY TOPIC:
 *   1) Implement Stack using Queues
 *   2) Implement Queue using Stacks
 *
 * This chapter is designed to be:
 *  • invariant-first
 *  • interview-ready
 *  • long-term recall friendly
 *  • correctness-guaranteed
 *
 * ============================================================
 */
public class StackQueue {

/*
 * ============================================================
 * 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENTS
 * ============================================================
 */

/**
 * ------------------------------------------------------------
 * PROBLEM 1: Implement Stack using Queues
 * ------------------------------------------------------------
 *
 * 🔗 Official LeetCode Link:
 * https://leetcode.com/problems/implement-stack-using-queues/
 *
 * 🧩 Difficulty: Easy
 * 🏷️ Tags: Stack, Queue, Design
 *
 * ------------------------------------------------------------
 * FULL OFFICIAL PROBLEM STATEMENT (VERBATIM)
 * ------------------------------------------------------------
 *
 * Implement a last-in-first-out (LIFO) stack using only two queues.
 * The implemented stack should support all the functions of a normal
 * stack (push, top, pop, and empty).
 *
 * Implement the MyStack class:
 *
 *  • void push(int x) Pushes element x to the top of the stack.
 *  • int pop() Removes the element on the top of the stack and returns it.
 *  • int top() Returns the element on the top of the stack.
 *  • boolean empty() Returns true if the stack is empty, false otherwise.
 *
 * Notes:
 *  • You must use only standard operations of a queue, which means that
 *    only push to back, peek/pop from front, size, and is empty operations
 *    are valid.
 *  • Depending on your language, the queue may not be supported natively.
 *    You may simulate a queue using a list or deque (double-ended queue),
 *    as long as you use only a queue's standard operations.
 *
 * ------------------------------------------------------------
 * Example 1:
 *
 * Input:
 * ["MyStack", "push", "push", "top", "pop", "empty"]
 * [[], [1], [2], [], [], []]
 *
 * Output:
 * [null, null, null, 2, 2, false]
 *
 * Explanation:
 * MyStack myStack = new MyStack();
 * myStack.push(1);
 * myStack.push(2);
 * myStack.top();    // return 2
 * myStack.pop();    // return 2
 * myStack.empty();  // return false
 *
 * ------------------------------------------------------------
 * Constraints:
 *
 *  • 1 <= x <= 9
 *  • At most 100 calls will be made to push, pop, top, and empty.
 *  • All the calls to pop and top are valid.
 *
 * ------------------------------------------------------------
 * Follow-up:
 * Can you implement the stack using only one queue?
 * ------------------------------------------------------------
 */


/**
 * ------------------------------------------------------------
 * PROBLEM 2: Implement Queue using Stacks
 * ------------------------------------------------------------
 *
 * 🔗 Official LeetCode Link:
 * https://leetcode.com/problems/implement-queue-using-stacks/
 *
 * 🧩 Difficulty: Easy
 * 🏷️ Tags: Stack, Queue, Design
 *
 * ------------------------------------------------------------
 * FULL OFFICIAL PROBLEM STATEMENT (VERBATIM)
 * ------------------------------------------------------------
 *
 * Implement a first-in-first-out (FIFO) queue using only two stacks.
 * The implemented queue should support all the functions of a normal
 * queue (push, peek, pop, and empty).
 *
 * Implement the MyQueue class:
 *
 *  • void push(int x) Pushes element x to the back of the queue.
 *  • int pop() Removes the element from the front of the queue and returns it.
 *  • int peek() Returns the element at the front of the queue.
 *  • boolean empty() Returns true if the queue is empty, false otherwise.
 *
 * Notes:
 *  • You must use only standard operations of a stack, which means only
 *    push to top, peek/pop from top, size, and is empty operations are valid.
 *
 * ------------------------------------------------------------
 * Example 1:
 *
 * Input:
 * ["MyQueue", "push", "push", "peek", "pop", "empty"]
 * [[], [1], [2], [], [], []]
 *
 * Output:
 * [null, null, null, 1, 1, false]
 *
 * Explanation:
 * MyQueue myQueue = new MyQueue();
 * myQueue.push(1);
 * myQueue.push(2);
 * myQueue.peek();   // return 1
 * myQueue.pop();    // return 1
 * myQueue.empty();  // return false
 *
 * ------------------------------------------------------------
 * Constraints:
 *
 *  • 1 <= x <= 9
 *  • At most 100 calls will be made to push, pop, peek, and empty.
 *  • All the calls to pop and peek are valid.
 * ------------------------------------------------------------
 */


/*
 * ============================================================
 * 3️⃣ 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
 * ============================================================
 */

/**
 * 🔵 Pattern Name:
 *   "Behavior Preservation via State Reversal"
 *
 * 🔵 Problem Archetype:
 *   Simulating one data structure using another with opposing order semantics
 *
 * 🟢 Core Invariant (MANDATORY, ONE SENTENCE):
 *   At all observable boundaries, the exposed end of the structure must
 *   behave exactly like the target abstraction (LIFO or FIFO), regardless
 *   of internal rearrangements.
 *
 * 🟡 Why this invariant makes the pattern work:
 *   Because users only observe push/pop/peek boundaries, we are free to
 *   reorder internally as long as the next visible element is correct.
 *
 * 🔵 When this pattern applies:
 *   • Stack using Queue
 *   • Queue using Stack
 *   • API-limited simulations
 *
 * 🧭 Pattern Recognition Signals:
 *   • "Use X to implement Y"
 *   • Restricted operations
 *   • Order mismatch (LIFO vs FIFO)
 *
 * 🔴 How this differs from similar patterns:
 *   • Not about optimization
 *   • Not about amortization only
 *   • This is about **invariant preservation under reordering**
 */


/*
 * ============================================================
 * 4️⃣ 🟢 MENTAL MODEL & INVARIANTS (CANONICAL SOURCE OF TRUTH)
 * ============================================================
 */

/**
 * 🧠 Mental Model:
 *
 *   Think in terms of:
 *   "Where must the next visible element live?"
 *
 *   NOT:
 *   "How do I simulate every operation?"
 *
 * ------------------------------------------------------------
 * 🟢 INVARIANTS — STACK USING QUEUE
 * ------------------------------------------------------------
 *
 * Invariant S1:
 *   The front of the primary queue is always the TOP of the stack.
 *
 * Invariant S2:
 *   After every push, the newest element must be rotated to the front.
 *
 * State Representation:
 *   • One active queue
 *   • Optional helper queue (or rotation)
 *
 * Allowed Moves:
 *   • enqueue
 *   • dequeue
 *
 * Forbidden Moves:
 *   • direct index access
 *   • reversing without queue ops
 *
 * Termination Logic:
 *   • pop() removes front
 *
 * Why naive alternatives fail:
 *   • Leaving newest element at back violates S1
 *
 * ------------------------------------------------------------
 * 🟢 INVARIANTS — QUEUE USING STACKS
 * ------------------------------------------------------------
 *
 * Invariant Q1:
 *   The top of the output stack is always the FRONT of the queue.
 *
 * Invariant Q2:
 *   Input stack may be arbitrarily ordered until transfer.
 *
 * State Representation:
 *   • inStack (push-only)
 *   • outStack (pop/peek-only)
 *
 * Allowed Moves:
 *   • push to inStack
 *   • transfer only when outStack empty
 *
 * Forbidden Moves:
 *   • partial transfer
 *
 * Termination Logic:
 *   • pop/peek from outStack
 *
 * Why common wrong solutions fail:
 *   • Moving elements on every pop destroys amortized O(1)
 */

// ---------- IMPLEMENTATIONS START IN NEXT PART ----------

    /*
     * ============================================================
     * 5️⃣ 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
     * ============================================================
     */

    /**
     * ------------------------------------------------------------
     * STACK USING QUEUE — WRONG THINKING
     * ------------------------------------------------------------
     *
     * 🔴 Wrong Approach A:
     *   "Just enqueue elements and pop when needed"
     *
     * Why it seems correct:
     *   • Queue stores all elements
     *   • Stack top is the most recent push
     *
     * Why it fails:
     *   • Queue front ≠ Stack top
     *   • Violates Invariant S1
     *
     * Minimal Counterexample:
     *   push(1), push(2)
     *   queue = [1, 2]
     *   pop() returns 1 ❌ (should return 2)
     *
     * Interviewer Trap:
     *   They let you implement push() easily
     *   Then ask: "What does pop() return now?"
     *
     * ------------------------------------------------------------
     * QUEUE USING STACK — WRONG THINKING
     * ------------------------------------------------------------
     *
     * 🔴 Wrong Approach B:
     *   "Move elements on every pop"
     *
     * Why it seems correct:
     *   • Guarantees correct order each time
     *
     * Why it fails:
     *   • Repeated transfers
     *   • Violates Invariant Q2 (lazy transfer)
     *   • Time complexity degrades to O(n) per op
     *
     * Minimal Counterexample:
     *   push 1,2,3,4
     *   pop repeatedly → each pop costs O(n)
     *
     * Interviewer Trap:
     *   They ask amortized complexity
     *   This approach collapses under scrutiny
     */


    /*
     * ============================================================
     * 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES (DERIVED FROM INVARIANT)
     * ============================================================
     */

    /**
     * ============================================================
     * 🔹 SOLUTION 1 — STACK USING QUEUES
     * ============================================================
     */

    /**
     * ------------------------------------------------------------
     * 🟥 Brute Force
     * ------------------------------------------------------------
     * Core Idea:
     *   Use two queues, move all elements on pop.
     *
     * Enforced Invariant:
     *   Stack top must be identified at pop time.
     *
     * Limitation:
     *   Pop is O(n)
     *
     * Interview Preference:
     *   ❌ Not preferred
     */
    static class MyStackBrute {
        java.util.Queue<Integer> q1 = new java.util.LinkedList<>();
        java.util.Queue<Integer> q2 = new java.util.LinkedList<>();

        public void push(int x) {
            q1.offer(x);
        }

        public int pop() {
            while (q1.size() > 1) {
                q2.offer(q1.poll());
            }
            int res = q1.poll();
            java.util.Queue<Integer> tmp = q1;
            q1 = q2;
            q2 = tmp;
            return res;
        }

        public int top() {
            while (q1.size() > 1) {
                q2.offer(q1.poll());
            }
            int res = q1.peek();
            q2.offer(q1.poll());
            java.util.Queue<Integer> tmp = q1;
            q1 = q2;
            q2 = tmp;
            return res;
        }

        public boolean empty() {
            return q1.isEmpty();
        }
    }

    /**
     * ------------------------------------------------------------
     * 🟨 Improved
     * ------------------------------------------------------------
     * Core Idea:
     *   Costly push, cheap pop.
     *
     * Enforced Invariant:
     *   Queue front is always stack top (S1).
     *
     * Limitation Fixed:
     *   Eliminates O(n) pop.
     *
     * Interview Preference:
     *   ✅ Acceptable
     */
    static class MyStackImproved {
        java.util.Queue<Integer> q = new java.util.LinkedList<>();

        public void push(int x) {
            q.offer(x);
            int size = q.size();
            while (size-- > 1) {
                q.offer(q.poll());
            }
        }

        public int pop() {
            return q.poll();
        }

        public int top() {
            return q.peek();
        }

        public boolean empty() {
            return q.isEmpty();
        }
    }

    /**
     * ------------------------------------------------------------
     * 🟩 Optimal (Interview-Preferred)
     * ------------------------------------------------------------
     * Core Idea:
     *   Single queue rotation.
     *
     * Invariant:
     *   Front of queue is stack top at all times.
     *
     * Time:
     *   push → O(n), pop/top → O(1)
     *
     * Interview Preference:
     *   ⭐ Strongly preferred
     */
    static class MyStackOptimal {
        java.util.Queue<Integer> q = new java.util.LinkedList<>();

        public void push(int x) {
            q.offer(x);
            for (int i = 0; i < q.size() - 1; i++) {
                q.offer(q.poll());
            }
        }

        public int pop() {
            return q.poll();
        }

        public int top() {
            return q.peek();
        }

        public boolean empty() {
            return q.isEmpty();
        }
    }

    /**
     * ============================================================
     * 🔹 SOLUTION 2 — QUEUE USING STACKS
     * ============================================================
     */

    /**
     * ------------------------------------------------------------
     * 🟥 Brute Force
     * ------------------------------------------------------------
     * Core Idea:
     *   Reverse stack on every pop.
     *
     * Limitation:
     *   O(n) per pop.
     *
     * Interview Preference:
     *   ❌ Weak
     */
    static class MyQueueBrute {
        java.util.Stack<Integer> s1 = new java.util.Stack<>();
        java.util.Stack<Integer> s2 = new java.util.Stack<>();

        public void push(int x) {
            s1.push(x);
        }

        public int pop() {
            while (!s1.isEmpty()) s2.push(s1.pop());
            int res = s2.pop();
            while (!s2.isEmpty()) s1.push(s2.pop());
            return res;
        }

        public int peek() {
            while (!s1.isEmpty()) s2.push(s1.pop());
            int res = s2.peek();
            while (!s2.isEmpty()) s1.push(s2.pop());
            return res;
        }

        public boolean empty() {
            return s1.isEmpty();
        }
    }

    /**
     * ------------------------------------------------------------
     * 🟨 Improved
     * ------------------------------------------------------------
     * Core Idea:
     *   Lazy transfer.
     *
     * Invariant:
     *   outStack top is queue front (Q1).
     *
     * Interview Preference:
     *   ✅ Acceptable
     */
    static class MyQueueImproved {
        java.util.Stack<Integer> in = new java.util.Stack<>();
        java.util.Stack<Integer> out = new java.util.Stack<>();

        public void push(int x) {
            in.push(x);
        }

        public int pop() {
            moveIfNeeded();
            return out.pop();
        }

        public int peek() {
            moveIfNeeded();
            return out.peek();
        }

        private void moveIfNeeded() {
            if (out.isEmpty()) {
                while (!in.isEmpty()) {
                    out.push(in.pop());
                }
            }
        }

        public boolean empty() {
            return in.isEmpty() && out.isEmpty();
        }
    }

    /**
     * ------------------------------------------------------------
     * 🟩 Optimal (Interview-Preferred)
     * ------------------------------------------------------------
     * Core Idea:
     *   Lazy transfer with amortized O(1) operations.
     *
     * 🟢 Invariant:
     *   • outStack.top() is always the queue front
     *   • Each element moves from inStack → outStack exactly once
     *
     * 🟡 Why optimal:
     *   Total stack operations ≤ 2 per element over lifetime.
     *
     * Interview Preference:
     *   ⭐ Gold standard
     */
    static class MyQueueOptimal {
        private java.util.Stack<Integer> in = new java.util.Stack<>();
        private java.util.Stack<Integer> out = new java.util.Stack<>();

        public void push(int x) {
            in.push(x);
        }

        public int pop() {
            shiftIfNeeded();
            return out.pop();
        }

        public int peek() {
            shiftIfNeeded();
            return out.peek();
        }

        public boolean empty() {
            return in.isEmpty() && out.isEmpty();
        }

        // 🟢 Enforces invariant Q1 + Q2
        private void shiftIfNeeded() {
            if (out.isEmpty()) {
                while (!in.isEmpty()) {
                    out.push(in.pop());
                }
            }
        }
    }

// ---------- INTERVIEW ARTICULATION & BEYOND IN NEXT PART ----------

    /*
     * ============================================================
     * 7️⃣ 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
     * ============================================================
     */

    /**
     * 🟣 How to explain STACK USING QUEUE
     *
     * • Invariant:
     *   The front of the queue is always the stack top.
     *
     * • Discard logic:
     *   On push, rotate older elements behind the new one.
     *
     * • Correctness guarantee:
     *   Because pop/top always read the front, which by invariant is
     *   the most recently pushed element.
     *
     * • What breaks if changed:
     *   If rotation is skipped, FIFO leaks through → stack violated.
     *
     * • In-place / streaming:
     *   Yes, single queue.
     *
     * • When NOT to use:
     *   When push must be O(1) strictly.
     */

    /**
     * 🟣 How to explain QUEUE USING STACKS
     *
     * • Invariant:
     *   outStack.top() is the queue front.
     *
     * • Discard logic:
     *   Only transfer when outStack is empty.
     *
     * • Correctness guarantee:
     *   Each element reverses exactly once.
     *
     * • What breaks if changed:
     *   Partial or repeated transfer ruins amortized O(1).
     *
     * • In-place / streaming:
     *   Yes, incremental.
     *
     * • When NOT to use:
     *   When stack memory is constrained.
     */


    /*
     * ============================================================
     * 8️⃣ 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
     * ============================================================
     */

    /**
     * 🔄 Invariant-Preserving Changes:
     *   • Stack using 2 queues vs 1 queue
     *   • Queue using Deque instead of Stack
     *
     * 🟡 Reasoning-Only Changes:
     *   • Swap costly push vs costly pop
     *
     * 🔴 Pattern-Break Signals:
     *   • Accessing middle elements
     *   • Random access requirement
     */


    /*
     * ============================================================
     * 9️⃣ ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
     * ============================================================
     */

    /**
     * ============================================================
     * ⚫ Reinforcement 1: Min Stack
     * ============================================================
     *
     * 🔗 https://leetcode.com/problems/min-stack/
     *
     * FULL OFFICIAL STATEMENT:
     *
     * Design a stack that supports push, pop, top, and retrieving the
     * minimum element in constant time.
     *
     * Implement the MinStack class:
     *
     * • MinStack() initializes the stack object.
     * • void push(int val) pushes the element val onto the stack.
     * • void pop() removes the element on the top of the stack.
     * • int top() gets the top element of the stack.
     * • int getMin() retrieves the minimum element in the stack.
     *
     * Constraints:
     * • -2^31 <= val <= 2^31 - 1
     * • pop, top, and getMin are always called on non-empty stacks.
     *
     * 🟢 Invariant:
     *   Min stack top always equals minimum of all elements below it.
     */
    static class MinStack {
        java.util.Stack<Integer> stack = new java.util.Stack<>();
        java.util.Stack<Integer> min = new java.util.Stack<>();

        public void push(int val) {
            stack.push(val);
            if (min.isEmpty() || val <= min.peek()) {
                min.push(val);
            }
        }

        public void pop() {
            if (stack.pop().equals(min.peek())) {
                min.pop();
            }
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return min.peek();
        }
    }

    /**
     * ============================================================
     * ⚫ Reinforcement 2: Implement Stack using Deque
     * ============================================================
     *
     * Problem:
     * Use only Deque operations to simulate a stack.
     *
     * 🟢 Invariant:
     *   Front of deque is stack top.
     */
    static class StackUsingDeque {
        java.util.Deque<Integer> dq = new java.util.ArrayDeque<>();

        public void push(int x) {
            dq.addFirst(x);
        }

        public int pop() {
            return dq.removeFirst();
        }

        public int top() {
            return dq.peekFirst();
        }

        public boolean empty() {
            return dq.isEmpty();
        }
    }

    /**
     * ============================================================
     * ⚫ Reinforcement 3: Implement Queue using Deque
     * ============================================================
     *
     * Problem:
     * Use deque to simulate FIFO queue.
     *
     * 🟢 Invariant:
     *   Front is dequeue point, back is enqueue point.
     */
    static class QueueUsingDeque {
        java.util.Deque<Integer> dq = new java.util.ArrayDeque<>();

        public void push(int x) {
            dq.addLast(x);
        }

        public int pop() {
            return dq.removeFirst();
        }

        public int peek() {
            return dq.peekFirst();
        }

        public boolean empty() {
            return dq.isEmpty();
        }
    }

// ---------- RELATED PROBLEMS, TESTS & CLOSURE IN FINAL PART ----------


    /*
     * ============================================================
     * 🔟 🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
     * ============================================================
     */

    /**
     * ------------------------------------------------------------
     * 🧩 Related 1: Design Circular Queue
     * ------------------------------------------------------------
     *
     * 🔗 https://leetcode.com/problems/design-circular-queue/
     *
     * Invariant:
     *   Head points to front, tail points to next insertion slot.
     *
     * Why related:
     *   Same boundary-observable behavior preservation.
     *
     * Interview articulation:
     *   "Modulo arithmetic preserves FIFO order without shifting."
     */

    /**
     * ------------------------------------------------------------
     * 🧩 Related 2: Sliding Window Maximum
     * ------------------------------------------------------------
     *
     * 🔗 https://leetcode.com/problems/sliding-window-maximum/
     *
     * Invariant:
     *   Deque maintains decreasing order of candidates.
     *
     * Why related:
     *   Structure exists purely to preserve a visible boundary value.
     */

    /**
     * ------------------------------------------------------------
     * 🧩 Related 3: Next Greater Element
     * ------------------------------------------------------------
     *
     * 🔗 https://leetcode.com/problems/next-greater-element-i/
     *
     * Invariant:
     *   Stack holds decreasing unresolved elements.
     *
     * Why related:
     *   Lazy resolution + invariant preservation.
     */


    /*
     * ============================================================
     * 1️⃣1️⃣ 🟢 LEARNING VERIFICATION
     * ============================================================
     */

    /**
     * 🟢 Invariant Recall (no code):
     *   • Stack using Queue → queue front is stack top.
     *   • Queue using Stack → outStack top is queue front.
     *
     * 🟢 Naive Failure Explanation:
     *   • FIFO leaks into LIFO when rotation missing.
     *   • Repeated transfer kills amortized guarantees.
     *
     * 🟢 Debugging Readiness:
     *   • If output order wrong → invariant broken.
     *
     * 🟢 Pattern Recognition Signals:
     *   • "Design X using Y"
     *   • Restricted operations
     */


    /*
     * ============================================================
     * 1️⃣2️⃣ 🧪 main() METHOD + SELF-VERIFYING TESTS
     * ============================================================
     */

    public static void main(String[] args) {

        // -------- STACK USING QUEUE TESTS --------
        MyStackOptimal s = new MyStackOptimal();

        // Happy path
        s.push(1);
        s.push(2);
        assert s.top() == 2 : "Top should be 2";
        assert s.pop() == 2 : "Pop should return 2";
        assert !s.empty() : "Stack should not be empty";

        // Boundary
        s.pop();
        assert s.empty() : "Stack should be empty";

        // Interview trap
        s.push(10);
        s.push(20);
        s.push(30);
        assert s.pop() == 30 : "LIFO violation";

        // -------- QUEUE USING STACK TESTS --------
        MyQueueOptimal q = new MyQueueOptimal();

        // Happy path
        q.push(1);
        q.push(2);
        assert q.peek() == 1 : "Peek should be 1";
        assert q.pop() == 1 : "Pop should be 1";
        assert !q.empty() : "Queue should not be empty";

        // Boundary
        q.pop();
        assert q.empty() : "Queue should be empty";

        // Interview trap
        q.push(5);
        q.push(6);
        q.push(7);
        assert q.pop() == 5 : "FIFO violation";

        // -------- MIN STACK TEST --------
        MinStack ms = new MinStack();
        ms.push(3);
        ms.push(5);
        ms.push(2);
        assert ms.getMin() == 2 : "Min should be 2";
        ms.pop();
        assert ms.getMin() == 3 : "Min should revert to 3";

        System.out.println("ALL TESTS PASSED — INVARIANTS HOLD");
    }


    /*
     * ============================================================
     * 1️⃣3️⃣ ✅ COMPLETION CHECKLIST (ANSWERED)
     * ============================================================
     *
     * • Invariant → Boundary element always correct
     * • Search target → Next visible element
     * • Discard rule → Reorder internally, never externally
     * • Termination → pop/peek boundary
     * • Naive failure → Invariant violation
     * • Edge cases → Empty, single element
     * • Variant readiness → Yes
     * • Pattern boundary → No random access
     */


    /*
     * ============================================================
     * 🧘 FINAL CLOSURE STATEMENT
     * ============================================================
     *
     * I understand the invariant.
     * I can re-derive the solution.
     * This chapter is complete.
     * ============================================================
     */
}


