package org.chijai.day4.LinkedList.session1;

/**
 * =====================================================================================
 * 📘 ALGORITHM TEXTBOOK CHAPTER (HARD-LOCKED)
 * =====================================================================================
 * <p>
 * Title:
 * Intersection of Two Linked Lists
 * <p>
 * Core Pattern:
 * OFFSET NEUTRALIZATION VIA TOTAL TRAVERSAL EQUALIZATION
 * <p>
 * ⚠️ This is a RARE pattern.
 * ⚠️ This is NOT a generic two-pointer pattern.
 * ⚠️ This chapter is intentionally narrow and deep.
 * <p>
 * =====================================================================================
 */
public class Intersection {

    // =====================================================================================
    // 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
    // =====================================================================================
    /*
     * 160. Intersection of Two Linked Lists
     *
     * Given the heads of two singly linked-lists headA and headB,
     * return the node at which the two lists intersect.
     * If the two linked lists have no intersection at all, return null.
     *
     * The test cases are generated such that there are no cycles anywhere
     * in the entire linked structure.
     *
     * Note that the linked lists must retain their original structure
     * after the function returns.
     *
     * 🔗 https://leetcode.com/problems/intersection-of-two-linked-lists/
     * 🧩 Difficulty: Easy
     * 🏷️ Tags: Linked List, Two Pointers, Hash Table
     */

    // =====================================================================================
    // 🔵 PATTERN DECLARATION (HARD-LOCKED)
    // =====================================================================================
    /*
     * 🔵 PATTERN NAME (EXACT):
     * Offset Neutralization via Total Traversal Equalization
     *
     * 🔒 HARD LOCK:
     * This pattern applies ONLY when ALL conditions below hold.
     * If even ONE is violated — ABANDON this pattern immediately.
     *
     * Required Conditions:
     * 1. Two sequences are traversed FORWARD-ONLY
     * 2. They may share a COMMON SUFFIX by IDENTITY (not value)
     * 3. Starting offsets are UNKNOWN and UNEQUAL
     * 4. Backtracking is impossible
     * 5. Extra memory is disallowed or undesirable
     *
     * Linked lists are merely the container here.
     * The pattern is CONTAINER-INDEPENDENT.
     */

    // =====================================================================================
    // 🟢 MENTAL MODEL & INVARIANT (HARD-LOCKED)
    // =====================================================================================
    /*
     * 🟢 Mental Model:
     * Two forward-only processes start at unknown offsets
     * and may converge at a shared suffix.
     *
     * 🟢 Core Invariant (NON-NEGOTIABLE):
     * Both traversals MUST cover the SAME TOTAL DISTANCE.
     *
     * This is NOT about speed difference.
     * This is NOT about cycle detection.
     * This is about OFFSET NEUTRALIZATION.
     *
     * If total traversal is equalized,
     * convergence (or lack thereof) becomes inevitable.
     */

    // =====================================================================================
    // 🔴 PATTERN BOUNDARIES — DO NOT CROSS
    // =====================================================================================
    /*
     * 🔴 DO NOT USE THIS PATTERN IF:
     *
     * ❌ Random access exists (arrays, strings)
     * ❌ Lengths are trivial to compute and store
     * ❌ Backtracking is allowed
     * ❌ Equality is by VALUE, not IDENTITY
     * ❌ Cycles exist
     * ❌ Relative speed difference is the intended invariant
     *
     * ⚠️ WARNING:
     * Fast/slow pointer problems are a DIFFERENT PATTERN.
     * Treating them as the same is a conceptual error.
     */

    // =====================================================================================
    // 🟡 WHY THIS PATTERN HAS FEW REINFORCEMENTS (EXPLICIT)
    // =====================================================================================
    /*
     * 🟡 IMPORTANT TRUTH:
     * This pattern has VERY FEW pure reinforcements.
     *
     * Reason:
     * Most problems violate at least one required constraint:
     * - They allow indexing
     * - They allow memory
     * - They allow backtracking
     * - They rely on value equality
     *
     * This rarity is STRUCTURAL, not pedagogical.
     *
     * Do NOT force unrelated problems into this chapter.
     */

    // =====================================================================================
    // 🧱 LIST NODE DEFINITION
    // =====================================================================================
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int value) {
            this.val = value;
        }
    }

    // =====================================================================================
    // 🟥 BRUTE FORCE (REFERENCE CHECK)
    // =====================================================================================
    static class BruteForceSolution {
        static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            for (ListNode a = headA; a != null; a = a.next) {
                for (ListNode b = headB; b != null; b = b.next) {
                    if (a == b) return a;
                }
            }
            return null;
        }
    }

    // =====================================================================================
    // 🟡 USER HASHSET SOLUTION (CORRECT, SPACE-HEAVY)
    // =====================================================================================
    static class UserHashSetSolution {
        static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            java.util.HashSet<ListNode> visited = new java.util.HashSet<>();
            ListNode curr = headA;
            while (curr != null) {
                visited.add(curr);
                curr = curr.next;
            }
            curr = headB;
            while (curr != null) {
                if (visited.contains(curr)) return curr;
                curr = curr.next;
            }
            return null;
        }
    }

    // =====================================================================================
    // 🟢 OPTIMAL SOLUTION — PATTERN EMBODIMENT
    // =====================================================================================

    /*
    =====================================================================================
    🟢 TERMINATION PROOF — WHY BOTH POINTERS HIT FINAL null AT THE SAME ITERATION
    =====================================================================================

    Step 2: Define the total path each pointer is allowed to walk

    Let:
    - LA = number of nodes in list A
    - LB = number of nodes in list B

    We analyze the code behavior MECHANICALLY, not conceptually.

    -------------------------------------
    Pointer A path (NO intersection case)
    -------------------------------------
    1. Walks all LA nodes of list A
    2. Hits null
    3. Redirects to headB
    4. Walks all LB nodes of list B
    5. Hits null again

    👉 Total steps until FINAL null = LA + LB


    -------------------------------------
    Pointer B path
    -------------------------------------
    1. Walks all LB nodes of list B
    2. Hits null
    3. Redirects to headA
    4. Walks all LA nodes of list A
    5. Hits null again

    👉 Total steps until FINAL null = LB + LA

    Which is the SAME number.


    =====================================================================================
    Step 3: Why they MUST hit final null at the SAME iteration
    =====================================================================================

    Now combine Step 1 (loop mechanics) and Step 2 (path length).

    Facts:
    1. In EACH loop iteration, BOTH pointers advance EXACTLY ONCE
    2. Neither pointer can advance more than (LA + LB) steps
    3. The structure is finite and acyclic

    Therefore:
    - At iteration (LA + LB):
        • pointerA has exhausted its entire allowed path
        • pointerB has exhausted its entire allowed path

    So at that EXACT iteration:
        pointerA == null
        pointerB == null

    This makes the loop condition fail:
        while (pointerA != pointerB)  →  while (null != null) → false

    No earlier iteration can exhaust BOTH paths.
    No later iteration is possible because the loop terminates immediately.

    This is a HARD termination guarantee, not intuition.
    =====================================================================================
    */

    static class OptimalOffsetNeutralizationSolution {
        static ListNode getIntersectionNode(ListNode headA, ListNode headB) {

            ListNode pointerA = headA;
            ListNode pointerB = headB;

            /*
             * 🟢 HARD-LOCKED INVARIANT ENFORCEMENT
             *
             * Redirecting a pointer at null is NOT a trick.
             * It forces both traversals to cover:
             *
             * lengthA + lengthB
             *
             * thereby neutralizing unknown offset.
             * once own list is fully traversed , traverse the other list.
             */
            while (pointerA != pointerB) {
                pointerA = (pointerA == null) ? headB : pointerA.next;
                pointerB = (pointerB == null) ? headA : pointerB.next;
            }

            return pointerA;
        }
    }

    // =====================================================================================
    // ⚫ CUSTOM REINFORCEMENT PROBLEMS (INVARIANT-PURE)
    // =====================================================================================
    /*
     * ⚫ NOTE:
     * These are NOT LeetCode problems.
     * They are REAL invariant-preserving reinforcements.
     *
     * 1. Two forward-only log streams that may share a replicated suffix
     * 2. Two distributed timelines that may converge after reconciliation
     * 3. Two append-only iterators with unknown offset and no buffering
     *
     * All share the SAME mental move:
     * → Equalize TOTAL traversal, not starting position.
     */

    /*
    =====================================================================================
    ⚫ REINFORCEMENT PROBLEM 1 — FORWARD-ONLY LOG STREAM INTERSECTION
    =====================================================================================

    📘 PROBLEM STATEMENT:
    You are given two log streams, StreamA and StreamB.

    Properties:
    - Append-only
    - Forward-only traversal
    - Logs are immutable objects
    - Streams MAY share a replicated suffix (same object references)
    - Replication point is UNKNOWN
    - No buffering of full streams is allowed

    Task:
    Return the FIRST shared log entry by REFERENCE.
    If no shared suffix exists, return null.

    🧠 PATTERN MAPPING:
    - LogEntry  → ListNode
    - next      → next pointer
    - Stream    → linked structure

    🟢 INVARIANT:
    Both traversals must consume the SAME TOTAL number of log entries.
    */

    static class LogEntry {
        final String message;
        LogEntry next;

        LogEntry(String message) {
            this.message = message;
        }
    }

    static class LogStreamIntersection {

        static LogEntry findIntersection(LogEntry headA, LogEntry headB) {

            LogEntry readerA = headA;
            LogEntry readerB = headB;

            // Offset neutralization via total traversal equalization
            while (readerA != readerB) {
                readerA = (readerA == null) ? headB : readerA.next;
                readerB = (readerB == null) ? headA : readerB.next;
            }

            return readerA; // first shared log entry or null
        }
    }

    /*
    =====================================================================================
    ⚫ REINFORCEMENT PROBLEM 2 — DISTRIBUTED TIMELINE CONVERGENCE
    =====================================================================================

    📘 PROBLEM STATEMENT:
    Two distributed systems maintain independent timelines of events.

    Properties:
    - Forward-only traversal
    - Events are immutable objects
    - Timelines MAY converge after reconciliation
    - Convergence point is UNKNOWN
    - No timestamps or global ordering
    - No full timeline buffering allowed

    Task:
    Return the FIRST shared event object by IDENTITY.
    If timelines never converge, return null.

    🧠 PATTERN MAPPING:
    - Event        → ListNode
    - next         → next pointer
    - Timeline     → linked structure

    🟢 INVARIANT:
    Both traversals must consume the SAME TOTAL number of events.
    */

    static class Event {
        final int id;
        Event next;

        Event(int id) {
            this.id = id;
        }
    }

    static class TimelineConvergence {

        static Event findFirstCommonEvent(Event headA, Event headB) {

            Event cursorA = headA;
            Event cursorB = headB;

            // Structural offset neutralization
            while (cursorA != cursorB) {
                cursorA = (cursorA == null) ? headB : cursorA.next;
                cursorB = (cursorB == null) ? headA : cursorB.next;
            }

            return cursorA;
        }
    }

    /*
    =====================================================================================
    ⚫ REINFORCEMENT PROBLEM 3 — APPEND-ONLY ITERATOR CONVERGENCE
    =====================================================================================

    📘 PROBLEM STATEMENT:
    You are given two append-only iterators.

    Properties:
    - Forward-only traversal
    - No rewind
    - No buffering of past elements
    - Iterators may start at different offsets
    - Iterators may converge to the same underlying iterator

    Task:
    Return the FIRST shared element by IDENTITY.
    If no convergence exists, return null.

    🧠 PATTERN MAPPING:
    - IteratorNode → ListNode
    - next         → next pointer

    🟢 INVARIANT:
    Equalize TOTAL traversal length, not starting position.
    */
    static class IteratorNode {
        final int value;
        IteratorNode next;

        IteratorNode(int value) {
            this.value = value;
        }
    }

    static class IteratorConvergence {

        static IteratorNode findConvergence(IteratorNode startA, IteratorNode startB) {

            IteratorNode itA = startA;
            IteratorNode itB = startB;

            while (itA != itB) {
                itA = (itA == null) ? startB : itA.next;
                itB = (itB == null) ? startA : itB.next;
            }

            return itA;
        }
    }


    // =====================================================================================
    // 🧪 SELF-VERIFYING TESTS
    // =====================================================================================
    public static void main(String[] args) {

        // Shared suffix
        ListNode common = new ListNode(8);
        common.next = new ListNode(10);

        // List A
        ListNode a = new ListNode(3);
        a.next = new ListNode(7);
        a.next.next = common;

        // List B
        ListNode b = new ListNode(99);
        b.next = new ListNode(1);
        b.next.next = common;

        assert BruteForceSolution.getIntersectionNode(a, b) == common;
        assert UserHashSetSolution.getIntersectionNode(a, b) == common;
        assert OptimalOffsetNeutralizationSolution.getIntersectionNode(a, b) == common;

        // No intersection
        ListNode x = new ListNode(1);
        ListNode y = new ListNode(2);
        assert OptimalOffsetNeutralizationSolution.getIntersectionNode(x, y) == null;

        // Same head
        assert OptimalOffsetNeutralizationSolution.getIntersectionNode(common, common) == common;

        System.out.println("All tests passed ✔");
    }

    // =====================================================================================
    // 🧘 FINAL CLOSURE — HARD-LOCKED
    // =====================================================================================
    /*
     * This pattern is rare by necessity.
     * Its constraints are strict.
     * Its invariant is absolute.
     *
     * When the constraints match,
     * the solution is inevitable.
     *
     * When they do not,
     * this pattern must be abandoned immediately.
     *
     * This chapter is complete.
     */
}
