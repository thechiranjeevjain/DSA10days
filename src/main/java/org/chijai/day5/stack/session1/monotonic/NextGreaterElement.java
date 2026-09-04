package org.chijai.day5.stack.session1.monotonic;

import java.util.Arrays;
import java.util.Stack;

/**
 * =====================================================================================
 * NEXT GREATER / SMALLER ELEMENT FAMILY
 * =====================================================================================
 *
 * Goal:
 * Learn one monotonic-boundary family deeply enough to reconstruct:
 *
 *   Next Greater Right
 *   Previous Greater
 *   Next Smaller Right
 *   Previous Smaller
 *   Next Greater Element I
 *   Next Greater Element II (circular)
 *
 * Core rule:
 *
 *   NEXT GREATER  -> decreasing candidates
 *   NEXT SMALLER  -> increasing candidates
 *
 * The important NEXT vs PREVIOUS difference:
 *
 *   NEXT     -> popped element gets answered.
 *   PREVIOUS -> surviving top answers current.
 *
 * =====================================================================================
 */
public class NextGreaterElement {

    /*
     * =================================================================================
     * 1️⃣ FAMILY MAP — WHAT CHANGES?
     * =================================================================================
     *
     * ===================================================================================================
     * VARIANT                | DIRECTION | TARGET  | WHILE LOOP GOAL          | ANSWER COMES FROM
     * ===================================================================================================
     * Next Greater Right     | right     | greater | resolve waiting elements | current value
     * Previous Greater       | left      | greater | remove useless candidates| surviving top
     * Next Smaller Right     | right     | smaller | resolve waiting elements | current value
     * Previous Smaller       | left      | smaller | remove useless candidates| surviving top
     * NGE I                  | right     | greater | same as Next Greater     | value->answer map
     * NGE II                 | circular  | greater | same as Next Greater     | 2n traversal
     * ===================================================================================================
     *
     * Same family:
     *   Daily Temperatures -> Next Greater + distance
     *   Stock Span         -> Previous Greater + compressed count
     *
     * Name traps:
     *   NGE III -> next permutation, NOT monotonic stack
     *   NGE IV  -> second greater; multi-stage resolution
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 2️⃣ PRIMARY PROBLEM — NEXT GREATER ELEMENT II
     * =================================================================================
     *
     * LeetCode 503
     *
     * Given a circular array nums, return the first STRICTLY greater value encountered
     * after each element while traversing forward. If none exists, return -1.
     *
     * Circular means:
     *
     *   after nums[n - 1], traversal continues at nums[0].
     *
     * Example:
     *
     *   nums   = [1, 2, 1]
     *   answer = [2,-1, 2]
     *
     * Visual:
     *
     *   index:    0   1   2
     *   nums:    [1,  2,  1]
     *              \      |
     *               > 2   | wrap
     *                      v
     *                     2
     *
     * For index 2:
     *   nothing greater exists to its physical right,
     *   so circular traversal wraps and finds 2 at index 1.
     *
     * Brute force:
     *   For every index, scan up to n future positions -> O(n^2).
     *
     * We want to avoid repeatedly rescanning elements that are still waiting
     * for their first greater value.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 3️⃣ CORE MENTAL MODEL + INVARIANT
     * =================================================================================
     *
     * Stack = WAITING ROOM of unresolved indices.
     *
     * Each waiting index is saying:
     *
     *   "I have not yet seen my first greater value."
     *
     * When current arrives:
     *
     *   while current > waitingTop:
     *       current is the answer for waitingTop
     *       pop and resolve it
     *
     *   push current
     *
     * Invariant:
     *
     *   Waiting values are monotonically decreasing from bottom -> top.
     *
     * Why?
     *
     *   Any smaller waiting value is popped as soon as a greater current value arrives.
     *
     * Why the first greater is guaranteed:
     *
     *   We scan in traversal order.
     *   The first current value that can pop an unresolved index is therefore
     *   the first strictly greater value encountered after that index.
     *
     * Strictly greater matters:
     *
     *   use >
     *   not >=
     *
     * Equal values do NOT resolve each other.
     *
     * =================================================================================
     */



    /**
     * ============================================================================
     * DEFERRED RESOLUTION PATTERN — MONOTONIC STACK
     * ============================================================================
     *
     * Mental Model
     * ------------
     *
     * Stack = Waiting Room of unresolved elements.
     *
     * Every incoming element follows the same lifecycle:
     *
     *                      Incoming Element
     *                             │
     *                             ▼
     *                 Can I resolve someone?
     *                       /         \
     *                     YES          NO
     *                      │            │
     *             Resolve & Remove      │
     *                      │            │
     *                Keep checking      │
     *                      │            │
     *                 No one left? ◄────┘
     *                      │
     *                      ▼
     *             Join the Waiting Room
     *
     *
     * Generic Template
     * ----------------
     *
     * Stack<Element> waiting = new Stack<>();
     *
     * for (each incomingElement) {
     *
     *     while (!waiting.isEmpty()
     *             && canResolve(incomingElement, waiting.peek())) {
     *
     *         resolve(incomingElement, waiting.pop());
     *     }
     *
     *     waiting.push(incomingElement);
     * }
     *
     *
     * Customize Only
     * --------------
     *
     * 1. Who enters the waiting room?
     * 2. What are they waiting for?
     * 3. When can current resolve them?
     * 4. How is the answer recorded?
     *
     *
     * ============================================================================
     * THIS PROBLEM — NEXT GREATER
     * ============================================================================
     *
     * Waiting Room  : indices whose Next Greater Element is unknown
     *
     * Waiting For   : first strictly greater value to their right
     *
     * Resolve When  :
     *     nums[current] > nums[waiting.peek()]
     *
     * Record Answer :
     *     answer[unresolved] = nums[current]
     *
     * ============================================================================
     *
     * Boundary:
     *
     * This template fits NEXT problems directly:
     *
     *   current arrives
     *   -> resolves OLD waiting elements
     *   -> popped element gets answered
     *
     * PREVIOUS problems are slightly different:
     *
     *   current arrives
     *   -> filters useless old candidates
     *   -> surviving top answers CURRENT
     *
     * ============================================================================
     */


    /*
     * =================================================================================
     * ⭐ UNSEEN PROBLEM DECODER — SEE THE STRUCTURE BEFORE CODING
     * =================================================================================
     *
     * Do NOT begin with:
     *
     *   "Which LeetCode problem does this resemble?"
     *
     * Begin with:
     *
     *   "Who needs an answer, where can that answer come from,
     *    and what happens to candidates that can no longer matter?"
     *
     *
     * -------------------------------------------------------------------------
     * STEP 1 — WHO NEEDS THE ANSWER?
     * -------------------------------------------------------------------------
     *
     * Does CURRENT need an answer from the past?
     *
     *   -> PREVIOUS
     *
     * Are OLD elements waiting for an answer from the future?
     *
     *   -> NEXT
     *
     *
     * -------------------------------------------------------------------------
     * THE MENTAL IMAGE TO LOCK
     * -------------------------------------------------------------------------
     *
     * NEXT
     * ================================================================
     *
     *           FUTURE
     *             ↓
     *
     *  [old] [old] [old]       CURRENT
     *    ?     ?     ?            |
     *    |     |     |            |
     *    +-----+-----+<-----------+
     *           current answers them
     *
     *  POP = ANSWER
     *
     *  answer[popped] = current
     *
     *
     * PREVIOUS
     * ================================================================
     *
     *           PAST
     *            ↓
     *
     *  [candidate] [candidate] [candidate] <- CURRENT
     *                             |
     *                    remove useless ones
     *                             |
     *                             v
     *                     SURVIVING TOP
     *                             |
     *                             v
     *                      answers current
     *
     *  POP = FILTER
     *
     *  answer[current] = survivor
     *
     *
     * RECALL:
     *
     *   NEXT     -> old waits; current resolves; POPPED gets answer.
     *
     *   PREVIOUS -> current asks; old candidates are filtered;
     *               SURVIVOR gives answer.
     *
     *
     * -------------------------------------------------------------------------
     * STEP 2 — WHAT RELATION?
     * -------------------------------------------------------------------------
     *
     * GREATER?
     *
     *   -> keep decreasing candidates
     *
     * SMALLER?
     *
     *   -> keep increasing candidates
     *
     *
     * -------------------------------------------------------------------------
     * STEP 3 — CAN A CANDIDATE BE DISCARDED FOREVER?
     * -------------------------------------------------------------------------
     *
     * Ask:
     *
     *   "Once current defeats this candidate,
     *    can that candidate ever matter again?"
     *
     * If NO:
     *
     *   permanent domination exists
     *   -> monotonic structure is likely
     *
     * If candidates can expire because a window moves:
     *
     *   -> monotonic DEQUE, not plain stack
     *
     * If elements can be skipped arbitrarily:
     *
     *   -> likely subsequence / DP territory
     *
     *
     * -------------------------------------------------------------------------
     * STEP 4 — WHAT MUST THE ANSWER STORE?
     * -------------------------------------------------------------------------
     *
     * The pattern may stay identical while only the payload changes:
     *
     *   value    -> answer[old] = nums[current]
     *
     *   index    -> answer[old] = current
     *
     *   distance -> answer[old] = current - old
     *
     *   span     -> compress count with the stack entry
     *
     *
     * -------------------------------------------------------------------------
     * STEP 5 — IS THERE ONLY A TRAVERSAL MODIFIER?
     * -------------------------------------------------------------------------
     *
     * Normal:
     *
     *   n visits
     *
     * Circular:
     *
     *   2n visits
     *   current = visit % n
     *
     * Direction reversed:
     *
     *   traversal changes
     *
     * The core invariant may remain unchanged.
     *
     *
     * -------------------------------------------------------------------------
     * 10-SECOND DECISION TREE
     * -------------------------------------------------------------------------
     *
     *                     RANDOM ARRAY PROBLEM
     *                              |
     *                              v
     *                Directional greater/smaller?
     *                       /              \
     *                     NO                YES
     *                     |                  |
     *                other family           v
     *                                NEXT or PREVIOUS?
     *                                 /            \
     *                              NEXT            PREVIOUS
     *                               |                 |
     *                          old waits         current asks
     *                               |                 |
     *                         pop = answer       pop = filter
     *                               |                 |
     *                        GREATER / SMALLER
     *                          /          \
     *                    decreasing     increasing
     *                              |
     *                              v
     *                   What is the payload?
     *                 value/index/distance/span
     *                              |
     *                              v
     *                    Traversal modifier?
     *                    normal/circular/etc.
     *
     *
     * -------------------------------------------------------------------------
     * RANDOM-PROBLEM WORKSHEET
     * -------------------------------------------------------------------------
     *
     * Before writing code, fill only this:
     *
     *   WHO needs the answer?
     *       current / old unresolved elements
     *
     *   WHERE is the answer?
     *       past / future
     *
     *   RELATION?
     *       greater / smaller
     *
     *   WHAT DOES POP MEAN?
     *       resolve / filter
     *
     *   WHO GETS ANSWERED?
     *       popped / current
     *
     *   PAYLOAD?
     *       value / index / distance / span
     *
     *   MODIFIER?
     *       circular / reversed direction / window expiry / none
     *
     *
     * If these seven lines are clear, code should feel mechanical.
     *
     * =================================================================================
     */

    /*
     * =================================================================================
     * 5️⃣ PRIMARY IMPLEMENTATION — NGE II
     * =================================================================================
     */
    static class Optimal {

        static int[] nextGreaterElements(int[] nums) {

            int n = nums.length;

            int[] answer = new int[n];
            Arrays.fill(answer, -1);

            // Waiting Room of unresolved indices.
            Stack<Integer> waiting = new Stack<>();

            // Traverse twice to simulate a circular array.
            for (int visit = 0; visit < 2 * n; visit++) {

                int current = visit % n;

                // NEXT: current resolves popped waiting indices.
                while (!waiting.isEmpty()
                        && nums[current] > nums[waiting.peek()]) {

                    int unresolved = waiting.pop();
                    answer[unresolved] = nums[current];
                }

                waiting.push(current);
            }

            return answer;
        }
    }


    /*
     * =================================================================================
     * 5️⃣ VISUAL DRY RUN — NGE II [1,2,1]
     * =================================================================================
     *
     * answer starts:
     *
     *   [-1,-1,-1]
     *
     * waiting is shown bottom -> top.
     *
     * -------------------------------------------------------------------------
     * visit 0 -> current index 0 -> value 1
     *
     * waiting empty
     * push 0
     *
     * waiting = [0]
     *
     * -------------------------------------------------------------------------
     * visit 1 -> current index 1 -> value 2
     *
     * 2 > nums[0]=1
     *
     * pop 0
     * answer[0] = 2
     *
     * push 1
     *
     * waiting = [1]
     * answer  = [2,-1,-1]
     *
     * -------------------------------------------------------------------------
     * visit 2 -> current index 2 -> value 1
     *
     * 1 > nums[1]=2 ? NO
     *
     * push 2
     *
     * waiting = [1,2]
     *
     * -------------------------------------------------------------------------
     * SECOND TRAVERSAL = circular replay
     *
     * visit 3 -> current 0 -> value 1
     *
     * 1 > nums[2]=1 ? NO
     * push 0
     *
     * waiting = [1,2,0]
     *
     * -------------------------------------------------------------------------
     * visit 4 -> current 1 -> value 2
     *
     * 2 > nums[0]=1
     *   pop 0
     *   answer[0] = 2
     *
     * 2 > nums[2]=1
     *   pop 2
     *   answer[2] = 2
     *
     * 2 > nums[1]=2 ? NO
     *
     * final answer already:
     *
     *   [2,-1,2]
     *
     * -------------------------------------------------------------------------
     * Why pushing on both passes is still O(n):
     *
     *   visits           = 2n
     *   pushed entries   <= 2n
     *   each pushed occurrence pops at most once
     *
     *   => total stack work O(n)
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 6️⃣ NEXT GREATER ELEMENT I — SAME ENGINE, MAP WRAPPER
     * =================================================================================
     *
     * LeetCode 496
     *
     * nums1 is a subset of nums2 and nums2 values are unique.
     * For each nums1 value, return its next greater value in nums2.
     *
     * Example:
     *
     *   nums1 = [4,1,2]
     *   nums2 = [1,3,4,2]
     *
     * Build next-greater relationships from nums2:
     *
     *   1 -> 3
     *   3 -> 4
     *   4 -> -1
     *   2 -> -1
     *
     * Result:
     *
     *   [-1,3,-1]
     *
     * Difference from generic NGE:
     *
     *   values are unique, so value -> nextGreater can be stored in a Map.
     *
     * =================================================================================
     */
    static class NextGreaterElementI {

        static int[] nextGreaterElement(int[] nums1, int[] nums2) {
            java.util.Map<Integer, Integer> nextMap = new java.util.HashMap<>();
            java.util.Deque<Integer> stack = new java.util.ArrayDeque<>();

            for (int value : nums2) {
                while (!stack.isEmpty() && value > stack.peek()) {
                    nextMap.put(stack.pop(), value);
                }
                stack.push(value);
            }

            int[] answer = new int[nums1.length];
            for (int i = 0; i < nums1.length; i++) {
                answer[i] = nextMap.getOrDefault(nums1[i], -1);
            }

            return answer;
        }
    }


    /*
     * =================================================================================
     * 7️⃣ GENERIC NEXT GREATER RIGHT
     * =================================================================================
     *
     * nums = [2,1,2,4,3]
     *
     * answer = [4,2,4,-1,-1]
     *
     * NEXT semantics:
     *
     *   while loop RESOLVES old waiting indices.
     *   Every pop produces an answer.
     *
     * =================================================================================
     */
    static class NextGreaterRight {

        static int[] solve(int[] nums) {
            int[] answer = new int[nums.length];
            Arrays.fill(answer, -1);

            Stack<Integer> waiting = new Stack<>();

            for (int current = 0; current < nums.length; current++) {
                while (!waiting.isEmpty()
                        && nums[current] > nums[waiting.peek()]) {

                    int unresolved = waiting.pop();
                    answer[unresolved] = nums[current];
                }

                waiting.push(current);
            }

            return answer;
        }
    }


    /*
     * =================================================================================
     * 8️⃣ GENERIC PREVIOUS GREATER
     * =================================================================================
     *
     * nums = [10,4,2,20,40,12,30]
     *
     * answer = [-1,10,4,-1,-1,40,40]
     *
     * PREVIOUS semantics:
     *
     *   while loop only FILTERS useless candidates.
     *   Pops do NOT produce answers.
     *
     *   After filtering:
     *   surviving top = nearest previous greater.
     *
     * =================================================================================
     */
    static class PreviousGreater {

        static int[] solve(int[] nums) {
            int[] answer = new int[nums.length];
            Arrays.fill(answer, -1);

            Stack<Integer> stack = new Stack<>();

            for (int i = 0; i < nums.length; i++) {

                // PREVIOUS: remove candidates that cannot answer current.
                while (!stack.isEmpty()
                        && nums[stack.peek()] <= nums[i]) {
                    stack.pop();
                }

                // Survivor answers CURRENT.
                if (!stack.isEmpty()) {
                    answer[i] = nums[stack.peek()];
                }

                stack.push(i);
            }

            return answer;
        }
    }


    /*
     * =================================================================================
     * 9️⃣ NEXT vs PREVIOUS — THE KEY SEMANTIC DIFFERENCE
     * =================================================================================
     *
     * NEXT:
     *
     *   while (...) {
     *       int unresolved = waiting.pop();
     *       answer[unresolved] = nums[current];
     *   }
     *
     *   while = RESOLUTION
     *   popped element gets answered
     *
     * PREVIOUS:
     *
     *   while (...) {
     *       stack.pop();
     *   }
     *
     *   answer[current] = stack.peek()
     *
     *   while = FILTERING
     *   survivor answers current
     *
     * RECALL:
     *
     *   NEXT     -> POPPED gets answer.
     *   PREVIOUS -> SURVIVOR gives answer.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 🔟 NEXT SMALLER RIGHT
     * =================================================================================
     *
     * Same NEXT engine.
     * Flip greater -> smaller.
     *
     * nums = [4,8,5,2,25]
     *
     * answer = [2,5,2,-1,-1]
     *
     * NEXT GREATER -> decreasing candidates
     * NEXT SMALLER -> increasing candidates
     *
     * =================================================================================
     */
    static class NextSmallerRight {

        static int[] solve(int[] nums) {
            int[] answer = new int[nums.length];
            Arrays.fill(answer, -1);

            Stack<Integer> waiting = new Stack<>();

            for (int current = 0; current < nums.length; current++) {
                while (!waiting.isEmpty()
                        && nums[current] < nums[waiting.peek()]) {

                    int unresolved = waiting.pop();
                    answer[unresolved] = nums[current];
                }

                waiting.push(current);
            }

            return answer;
        }
    }


    /*
     * =================================================================================
     * 1️⃣1️⃣ PREVIOUS SMALLER
     * =================================================================================
     *
     * Same PREVIOUS engine.
     * Flip greater -> smaller.
     *
     * nums = [4,5,2,10,8]
     *
     * answer = [-1,4,-1,2,2]
     *
     * =================================================================================
     */
    static class PreviousSmaller {

        static int[] solve(int[] nums) {
            int[] answer = new int[nums.length];
            Arrays.fill(answer, -1);

            Stack<Integer> stack = new Stack<>();

            for (int i = 0; i < nums.length; i++) {

                while (!stack.isEmpty()
                        && nums[stack.peek()] >= nums[i]) {
                    stack.pop();
                }

                if (!stack.isEmpty()) {
                    answer[i] = nums[stack.peek()];
                }

                stack.push(i);
            }

            return answer;
        }
    }


    /*
     * =================================================================================
     * 1️⃣2️⃣ ± DELTA — RECOGNITION BOUNDARIES
     * =================================================================================
     *
     * +Δ SAME FAMILY
     *
     * Daily Temperatures
     *   Next Greater + store indices + answer distance.
     *
     * Stock Span
     *   Previous Greater + compressed count/span.
     *
     * Next Smaller
     *   Flip comparison; decreasing -> increasing candidates.
     *
     * -------------------------------------------------------------------------
     * -Δ PATTERN BREAKS
     *
     * NGE III
     *   "next greater number using same digits"
     *   -> Next Permutation.
     *
     * LIS
     *   can skip elements; first greater is not a hard boundary
     *   -> DP / binary search.
     *
     * Sliding Window Maximum
     *   candidates expire by age
     *   -> Monotonic Deque.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣3️⃣ COMPLEXITY + INTERVIEW RECALL
     * =================================================================================
     *
     * Linear variants:
     *
     *   each index pushes once
     *   each index pops at most once
     *   -> O(n) time, O(n) space
     *
     * Circular NGE II:
     *
     *   2n visits
     *   <= 2n pushed occurrences
     *   each occurrence pops at most once
     *   -> O(n) time, O(n) space
     *
     * 30-second reconstruction:
     *
     *   NEXT GREATER:
     *       decreasing waiting stack
     *       while current > top:
     *           answer[pop] = current
     *
     *   NEXT SMALLER:
     *       flip > to <
     *
     *   PREVIOUS:
     *       pop impossible candidates
     *       survivor = answer for current
     *
     *   CIRCULAR:
     *       traverse 2n with visit % n
     *
     * Interview sentence:
     *
     *   "The stack stores unresolved monotonic candidates. For NEXT problems,
     *    current resolves popped indices. For PREVIOUS problems, current removes
     *    invalid candidates and the surviving top answers current. Each pushed
     *    entry can be popped at most once, so the total work is linear."
     *
     * =================================================================================
     */


    // =================================================================================
    // 1️⃣4️⃣ SELF-VERIFYING TESTS
    // =================================================================================

    public static void main(String[] args) {

        assertArrayEquals(
                new int[]{2, -1, 2},
                Optimal.nextGreaterElements(new int[]{1, 2, 1}),
                "NGE II [1,2,1]"
        );

        assertArrayEquals(
                new int[]{2, 3, 4, -1, 4},
                Optimal.nextGreaterElements(new int[]{1, 2, 3, 4, 3}),
                "NGE II [1,2,3,4,3]"
        );

        assertArrayEquals(
                new int[]{-1, 5, 5, 5, 5},
                Optimal.nextGreaterElements(new int[]{5, 4, 3, 2, 1}),
                "NGE II decreasing"
        );

        assertArrayEquals(
                new int[]{-1, -1, -1},
                Optimal.nextGreaterElements(new int[]{2, 2, 2}),
                "NGE II equal"
        );

        assertArrayEquals(
                new int[]{-1, 3, -1},
                NextGreaterElementI.nextGreaterElement(
                        new int[]{4, 1, 2},
                        new int[]{1, 3, 4, 2}
                ),
                "NGE I"
        );

        assertArrayEquals(
                new int[]{4, 2, 4, -1, -1},
                NextGreaterRight.solve(new int[]{2, 1, 2, 4, 3}),
                "Next Greater Right"
        );

        assertArrayEquals(
                new int[]{-1, 10, 4, -1, -1, 40, 40},
                PreviousGreater.solve(new int[]{10, 4, 2, 20, 40, 12, 30}),
                "Previous Greater"
        );

        assertArrayEquals(
                new int[]{2, 5, 2, -1, -1},
                NextSmallerRight.solve(new int[]{4, 8, 5, 2, 25}),
                "Next Smaller Right"
        );

        assertArrayEquals(
                new int[]{-1, 4, -1, 2, 2},
                PreviousSmaller.solve(new int[]{4, 5, 2, 10, 8}),
                "Previous Smaller"
        );

        System.out.println("ALL TESTS PASSED");
    }

    private static void assertArrayEquals(
            int[] expected,
            int[] actual,
            String name) {

        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(
                    name
                            + " expected=" + Arrays.toString(expected)
                            + " actual=" + Arrays.toString(actual)
            );
        }
    }
}
