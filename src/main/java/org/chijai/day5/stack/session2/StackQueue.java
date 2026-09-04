package org.chijai.day5.stack.session2;

/**
 * ============================================================
 * STACK <-> QUEUE DESIGN — V3
 * ============================================================
 *
 * PURPOSE
 * -------
 * Keep ONLY the two interview-canonical implementations:
 *
 *   1) Stack using ONE Queue
 *   2) Queue using TWO Stacks
 *
 * IMPORTANT
 * ---------
 * The implementations below are intentionally preserved in the
 * same form as the already-working versions:
 *
 *   • Queue<Integer> + LinkedList for MyStackOptimal
 *   • Stack<Integer> for MyQueueOptimal
 *   • same variable names
 *   • same control flow
 *   • same poll / peek / offer / push / pop calls
 *
 * The learning value of this file is in understanding WHY the
 * structures have enough power to simulate the target behavior.
 */
public class StackQueue {

    /*
     * ============================================================
     * 1) FIRST PRINCIPLES — WHY THE ASYMMETRY?
     * ============================================================
     *
     * QUESTION
     * --------
     * Why can ONE queue simulate a stack,
     * while a queue normally needs TWO stacks?
     *
     * ------------------------------------------------------------
     * QUEUE = TWO DOORS
     * ------------------------------------------------------------
     *
     *   EXIT / FRONT                 ENTRY / BACK
     *        ↓                            ↓
     *      [ A  B  C ]
     *
     * A queue removes from one end and inserts at the opposite end.
     *
     * Therefore it has a circulation path:
     *
     *   take from FRONT -> put back at BACK
     *
     * So a queue can rearrange / rotate ITSELF without needing a
     * second storage structure.
     *
     * RECALL:
     *   TWO DOORS -> SELF-ROTATION IS POSSIBLE.
     *
     * ------------------------------------------------------------
     * STACK = ONE DOOR
     * ------------------------------------------------------------
     *
     *              push / pop
     *                  ↓
     *                [ 3 ]  <- blocker
     *                [ 2 ]  <- blocker
     *                [ 1 ]  <- queue wants this first
     *
     * A stack inserts and removes from the SAME end: the top.
     *
     * To reach 1, we must first remove 3 and 2.
     * But the removed blockers must live SOMEWHERE temporarily.
     *
     * That temporary parking place is the SECOND stack.
     *
     * RECALL:
     *   ONE DOOR -> BLOCKERS NEED TEMPORARY PARKING.
     *
     * ------------------------------------------------------------
     * MASTER RECALL
     * ------------------------------------------------------------
     *
     *   Queue has circulation.
     *   Stack has obstruction.
     *
     *   Queue = 2 doors -> can rotate itself.
     *   Stack = 1 door  -> needs temporary parking to reach deeper items.
     */


    /*
     * ============================================================
     * 2) IMPLEMENT STACK USING ONE QUEUE
     * ============================================================
     *
     * TARGET BEHAVIOR
     * ---------------
     * Stack = LIFO
     * The newest pushed element must be returned first.
     *
     * INVARIANT
     * ---------
     *   q.front == stack.top
     *
     * Therefore every push must leave the newly inserted value at
     * the FRONT of the queue.
     *
     * ------------------------------------------------------------
     * CONCRETE VISUAL — push(3)
     * ------------------------------------------------------------
     *
     * Assume the simulated stack currently contains:
     *
     *   top -> 2, 1
     *
     * Since queue.front represents stack.top:
     *
     * before:
     *   front -> 2 1 -> back
     *
     * add 3:
     *   front -> 2 1 3 -> back
     *
     * Now rotate the OLD elements.
     *
     * move 2 to back:
     *   front -> 1 3 2 -> back
     *
     * move 1 to back:
     *   front -> 3 2 1 -> back
     *
     * Now:
     *   q.poll() returns 3
     *
     * which is exactly stack.pop().
     *
     * ------------------------------------------------------------
     * WHY q.size() - 1?
     * ------------------------------------------------------------
     * After q.offer(x), exactly ONE element is the new value.
     * Every other element is old and must move behind x.
     *
     *   number of old elements = q.size() - 1
     *
     * The queue size stays constant during each rotation step:
     *
     *   poll()  -> size - 1
     *   offer() -> size + 1
     *   net     -> unchanged
     *
     * Therefore using q.size() in the for-condition is safe here.
     *
     * COMPLEXITY
     * ----------
     * push()  O(n)
     * pop()   O(1)
     * top()   O(1)
     * empty() O(1)
     * space   O(n)
     */

    static class MyStackOptimal {
        java.util.Queue<Integer> q = new java.util.LinkedList<>();

        public void push(int x) {

            q.offer(x);
            int oldElements = q.size() - 1;
            while (oldElements-- > 0) {
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


    /*
     * ============================================================
     * 3) IMPLEMENT QUEUE USING TWO STACKS
     * ============================================================
     *
     * TARGET BEHAVIOR
     * ---------------
     * Queue = FIFO
     * The OLDEST inserted element must leave first.
     *
     * A single stack naturally exposes the NEWEST value first.
     * So we need one reversal.
     *
     * ------------------------------------------------------------
     * TWO ROLES
     * ------------------------------------------------------------
     *
     *   in  = convenient orientation for accepting NEW elements
     *   out = convenient orientation for serving OLD elements
     *
     * push 1, 2, 3 into in:
     *
     *   in
     *   TOP
     *   [3]
     *   [2]
     *   [1]
     *
     * Transfer all elements to out:
     *
     *   out
     *   TOP
     *   [1] <- queue front
     *   [2]
     *   [3]
     *
     * Moving stack -> stack reverses the order.
     * The oldest value is now exposed at out.top().
     *
     * ------------------------------------------------------------
     * WHY TRANSFER ONLY WHEN out IS EMPTY?
     * ------------------------------------------------------------
     *
     * Example:
     *
     * push 1,2,3
     * first transfer makes:
     *
     *   out top -> 1,2,3
     *
     * pop 1
     *
     * out still contains:
     *   top -> 2,3
     *
     * now push 4,5:
     *
     *   in top  -> 5,4
     *   out top -> 2,3
     *
     * Queue order must still be:
     *   2,3,4,5
     *
     * Therefore we MUST finish older values already sitting in out
     * before transferring the newer values from in.
     *
     * INVARIANT
     * ---------
     * If out is non-empty:
     *   out.peek() is the queue front.
     *
     * If out is empty:
     *   moving ALL of in -> out makes the oldest in element become
     *   out.peek(), restoring the invariant.
     *
     * ------------------------------------------------------------
     * WHY AMORTIZED O(1)?
     * ------------------------------------------------------------
     * Each element is:
     *
     *   1. pushed into in once
     *   2. transferred in -> out at most once
     *   3. popped from out once
     *
     * No element repeatedly travels back and forth.
     * So over N operations the total movement is O(N), giving
     * amortized O(1) per queue operation.
     *
     * COMPLEXITY
     * ----------
     * push()  O(1)
     * pop()   amortized O(1), worst-case O(n) on a transfer
     * peek()  amortized O(1), worst-case O(n) on a transfer
     * empty() O(1)
     * space   O(n)
     */

    /*
     * WHY NO REARRANGEMENT ON push()?
     * --------------------------------
     *
     * In a queue, a newly pushed element belongs at the BACK.
     *
     * Example:
     *   FRONT -> 1 2 3 <- BACK
     *                  ↑
     *                 new
     *
     * So the new element does NOT need to become accessible yet.
     * We can simply push it into `in` and let it wait.
     *
     * Reversal is needed only when we need the OLDEST element
     * and `out` has no older elements ready.
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
            // lazy transfer: only move in -> out when out is empty
            // out non-empty -> its top is already the queue front, so do nothing.
            // out empty     -> reverse ALL waiting elements from in -> out once.
            if (out.isEmpty()) {
                while (!in.isEmpty()) {
                    out.push(in.pop());
                }
            }
        }
    }


    /*
     * ============================================================
     * 4) 30-SECOND RECALL CARD
     * ============================================================
     *
     * STACK USING 1 QUEUE
     * -------------------
     * Invariant:
     *   queue front = stack top
     *
     * push(x):
     *   offer x
     *   rotate q.size()-1 old elements
     *
     * Why one queue is enough:
     *   queue has 2 doors -> front-to-back circulation.
     *
     * ------------------------------------------------------------
     * QUEUE USING 2 STACKS
     * --------------------
     * in  = receive new
     * out = serve old
     *
     * If out empty:
     *   move everything in -> out
     *
     * Why two stacks:
     *   one stack has one door.
     *   To reach the oldest/bottom item, blockers need temporary parking.
     *
     * ------------------------------------------------------------
     * ONE-LINE INTERVIEW RECALL
     * ------------------------------------------------------------
     * One queue can self-rearrange by circulation;
     * one stack cannot expose its bottom without storing blockers elsewhere.
     */


    public static void main(String[] args) {
        testStackUsingQueue();
        testQueueUsingStacks();
        System.out.println("StackQueue_v3: all tests passed.");
    }

    private static void testStackUsingQueue() {
        MyStackOptimal stack = new MyStackOptimal();

        stack.push(1);
        stack.push(2);
        stack.push(3);

        assertEquals(3, stack.top(), "stack top");
        assertEquals(3, stack.pop(), "stack pop 3");
        assertEquals(2, stack.pop(), "stack pop 2");

        stack.push(4);
        assertEquals(4, stack.pop(), "stack pop 4");
        assertEquals(1, stack.pop(), "stack pop 1");
        assertTrue(stack.empty(), "stack should be empty");
    }

    private static void testQueueUsingStacks() {
        MyQueueOptimal queue = new MyQueueOptimal();

        queue.push(1);
        queue.push(2);
        queue.push(3);

        assertEquals(1, queue.peek(), "queue front");
        assertEquals(1, queue.pop(), "queue pop 1");

        // Critical lazy-transfer case:
        // old values remain in out while newer values enter in.
        queue.push(4);
        queue.push(5);

        assertEquals(2, queue.pop(), "queue pop 2");
        assertEquals(3, queue.pop(), "queue pop 3");
        assertEquals(4, queue.pop(), "queue pop 4");
        assertEquals(5, queue.pop(), "queue pop 5");
        assertTrue(queue.empty(), "queue should be empty");
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(
                    message + " | expected=" + expected + ", actual=" + actual
            );
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
