package org.chijai.day4.LinkedList.session1;

import java.util.HashSet;
import java.util.Set;

/**
 * Intersection of Two Linked Lists
 *
 * Primary pattern:
 * OFFSET NEUTRALIZATION VIA TOTAL TRAVERSAL EQUALIZATION
 *
 * Reconstruction goal:
 * identify node IDENTITY, understand the offset problem,
 * derive length alignment, then compress it into head switching.
 */
public class Intersection {

    // =====================================================================================
    // 1. PRIMARY PROBLEM
    // =====================================================================================
    /*
     * LeetCode 160 — Intersection of Two Linked Lists
     *
     * Return the first node shared by BOTH acyclic singly linked lists.
     * Intersection means SAME NODE OBJECT, not same value.
     * If they never intersect, return null.
     * Do not modify either list.
     */

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    // =====================================================================================
    // 2. PRIMARY INTERVIEW SOLUTION — SEE THIS FIRST
    // =====================================================================================
    /*
     * RECOGNIZE:
     * same node identity + shared suffix + unequal prefix lengths
     *
     * REMEMBER:
     * "Walk mine, then yours. You walk yours, then mine."
     * Both pointers pay for both prefixes, so the offset disappears.
     *
     * TYPE FROM MEMORY:
     * p = headA, q = headB
     * while (p != q)
     *     p = p == null ? headB : p.next
     *     q = q == null ? headA : q.next
     * return p
     *
     * INTERVIEW COMPLEXITY:
     * Time  O(m + n)
     * Space O(1)
     */
    static class OptimalSolution {
        static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            ListNode pointerA = headA;
            ListNode pointerB = headB;

            while (pointerA != pointerB) {
                pointerA = (pointerA == null) ? headB : pointerA.next;
                pointerB = (pointerB == null) ? headA : pointerB.next;
            }

            return pointerA;
        }
    }

    // =====================================================================================
    // 3. APPROACH TRADE-OFF — KNOW WHY EACH VERSION EXISTS
    // =====================================================================================
    /*
     * Approach          Time       Extra Space   Trade-off
     * -------------------------------------------------------------------------------
     * Brute force       O(mn)      O(1)          No memory, but repeats comparisons
     * HashSet           O(m+n)     O(m)          Linear time by remembering A
     * Length alignment  O(m+n)     O(1)          Optimal; explicit and easy to derive
     * Head switching    O(m+n)     O(1)          Optimal; least bookkeeping <- PRIMARY
     *
     * PROGRESSION QUESTION:
     * Why move from one approach to the next?
     *
     * Brute force -> HashSet
     *     Spend memory to remove repeated searches.
     *
     * HashSet -> Length alignment
     *     Keep linear time, remove O(m) extra memory by using list lengths.
     *
     * Length alignment -> Head switching
     *     Keep the SAME optimal O(m+n) time and O(1) space,
     *     but remove explicit length calculation, difference calculation, and skipping.
     *
     * IMPORTANT:
     * Head switching does NOT improve Big-O over length alignment.
     * Its advantage is smaller state, less bookkeeping, and compact interview code.
     */

    // =====================================================================================
    // 4. FULL APPROACH PROGRESSION — KEEP ALL RUNNABLE VERSIONS TOGETHER
    // =====================================================================================
    /*
     * 1. Brute force
     *    Compare every node in A with every node in B.
     *    Time O(mn), space O(1).
     *
     * 2. HashSet
     *    Store all node references from A; scan B for first contained node.
     *    Time O(m+n), space O(m).
     *
     * 3. Length alignment
     *    Compute lengths, advance the longer list by |m-n|, then walk together.
     *    Time O(m+n), space O(1).
     *
     * 4. Head switching — PRIMARY (shown up top)
     *    Make each pointer traverse A+B / B+A.
     *    Time O(m+n), space O(1).
     *    Same asymptotic cost as length alignment, but less bookkeeping.
     *
     * RETENTION LADDER:
     * brute force
     *     -> spend memory to remember visited nodes
     *     -> remove memory by explicitly aligning lengths
     *     -> remove length bookkeeping by switching heads.
     *
     * INTERVIEW CHOICE:
     * - If asked for simplest correct idea: HashSet is easy to explain.
     * - If asked for O(1) space: length alignment is the most explicit derivation.
     * - For final production/interview answer: head switching is compact and optimal.
     */

    static class BruteForceSolution {
        static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            for (ListNode a = headA; a != null; a = a.next) {
                for (ListNode b = headB; b != null; b = b.next) {
                    if (a == b) {
                        return a;
                    }
                }
            }
            return null;
        }
    }

    static class HashSetSolution {
        static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            Set<ListNode> visited = new HashSet<>();

            ListNode current = headA;
            while (current != null) {
                visited.add(current);
                current = current.next;
            }

            current = headB;
            while (current != null) {
                if (visited.contains(current)) {
                    return current;
                }
                current = current.next;
            }

            return null;
        }
    }

    static class LengthAlignmentSolution {
        static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            int lengthA = length(headA);
            int lengthB = length(headB);

            ListNode pointerA = headA;
            ListNode pointerB = headB;

            if (lengthA > lengthB) {
                pointerA = advance(pointerA, lengthA - lengthB);
            } else {
                pointerB = advance(pointerB, lengthB - lengthA);
            }

            while (pointerA != pointerB) {
                pointerA = pointerA.next;
                pointerB = pointerB.next;
            }

            return pointerA;
        }

        private static int length(ListNode head) {
            int length = 0;

            for (ListNode current = head; current != null; current = current.next) {
                length++;
            }

            return length;
        }

        private static ListNode advance(ListNode node, int steps) {
            while (steps-- > 0) {
                node = node.next;
            }
            return node;
        }
    }


    // =====================================================================================
    // 5. HOW THE BRAIN SHOULD THINK
    // =====================================================================================
    /*
     * Shape:
     *
     * A: a1 -> a2 --------\
     *                       c1 -> c2 -> null
     * B: b1 -> b2 -> b3 --/
     *
     * Once two singly linked lists share a node,
     * every node after it is shared too: they have a COMMON SUFFIX.
     *
     * The only difficulty is OFFSET:
     * one pointer may have more private nodes before the common suffix.
     *
     * First natural fix:
     *     measure lengths -> skip the difference -> walk together.
     *
     * Better compression:
     *     let each pointer walk BOTH lists.
     *     A + B and B + A have equal total length,
     *     so the original offset cancels automatically.
     */

    // =====================================================================================
    // 6. PATTERN + BOUNDARY
    // =====================================================================================
    /*
     * RECOGNITION TRIGGER:
     * - two forward-only acyclic chains
     * - possible shared suffix by IDENTITY
     * - unequal unknown prefix lengths
     * - want O(1) extra space
     *
     * CORE INVARIANT:
     * Both pointers eventually traverse the same total distance.
     *
     * NOT THIS PATTERN:
     * - middle/cycle       -> relative-speed fast/slow
     * - nth from end       -> fixed-gap pointers
     * - equal values       -> value comparison problem
     * - cyclic lists       -> different case analysis
     */

    // =====================================================================================
    // 7. RECONSTRUCTION SKELETON — MECHANICAL RECALL
    // =====================================================================================
    /*
     * pointerA = headA
     * pointerB = headB
     *
     * while pointerA != pointerB:
     *     pointerA = next node, or headB after null
     *     pointerB = next node, or headA after null
     *
     * return pointerA
     *
     * MEMORY LINE:
     * "Walk my list, then yours. You walk yours, then mine."
     */

    // =====================================================================================
    // 8. WHY THE PRIMARY WORKS — DERIVATION, NOT TRICK
    // =====================================================================================
    /*
     * Let:
     * a = private prefix length of A
     * b = private prefix length of B
     * c = shared suffix length
     *
     * Pointer A route:
     *     a + c + b
     *
     * Pointer B route:
     *     b + c + a
     *
     * These are equal.
     *
     * So after each pointer pays for BOTH private prefixes,
     * the original offset disappears and they enter the common suffix aligned.
     *
     * If there is NO intersection:
     * A walks m+n nodes and B walks n+m nodes.
     * Both finally become null together.
     */

    // =====================================================================================
    // 9. DRY RUN — STATE EVOLUTION
    // =====================================================================================
    /*
     * A: A1 -> A2 ------\
     *                     C1 -> C2 -> null
     * B: B1 -> B2 -> B3 /
     *
     * iteration     pointerA     pointerB
     * -----------------------------------
     * 0             A1           B1
     * 1             A2           B2
     * 2             C1           B3
     * 3             C2           C1
     * 4             null         C2
     * 5             B1           null
     * 6             B2           A1
     * 7             B3           A2
     * 8             C1           C1   <- meet
     *
     * Switching does not make either pointer faster.
     * It only makes both pay the same TOTAL route length.
     */

    // =====================================================================================
    // 10. HIGH-ROI NUANCES / TRAPS
    // =====================================================================================
    /*
     * 1. Compare references:
     *        pointerA == pointerB
     *    NOT values:
     *        pointerA.val == pointerB.val
     *
     * 2. No special null pre-check is required.
     *    The loop naturally handles an empty list.
     *
     * 3. Redirect only when the pointer itself is null:
     *        p == null ? otherHead : p.next
     *
     * 4. The lists must be acyclic for this proof.
     *
     * 5. We never mutate next pointers.
     *
     * 6. Head switching is not asymptotically faster than length alignment.
     *    Its advantage is less state and less bookkeeping.
     */

    // =====================================================================================
    // 11. CORRECTNESS + COMPLEXITY
    // =====================================================================================
    /*
     * CORRECTNESS:
     * - If an intersection exists, both routes contain the same private-prefix
     *   distances in opposite order, so the offset is neutralized and both pointers
     *   reach the first common node together.
     * - If no intersection exists, both traverse exactly m+n nodes and become null
     *   together, so null is returned.
     *
     * COMPLEXITY + PRACTICAL TRADE-OFF:
     *
     * Brute force
     *     Time  O(mn)
     *     Space O(1)
     *     Cost: repeatedly scans B for every node of A.
     *
     * HashSet
     *     Time  O(m+n) expected
     *     Space O(m) when storing A
     *     Gain: removes repeated comparisons.
     *     Cost: extra memory and hashing.
     *
     * Length alignment
     *     Time  O(m+n)
     *     Space O(1)
     *     Gain: optimal bounds with a very explicit correctness story.
     *     Cost: two length counts + difference bookkeeping before aligned scan.
     *
     * Head switching
     *     Time  O(m+n)
     *     Space O(1)
     *     Gain: same optimal bounds with fewer variables/bookkeeping.
     *     Cost: less obvious until you understand traversal equalization.
     *
     * FINAL TRADE-OFF:
     * Head switching wins on implementation simplicity, NOT asymptotic complexity.
     */

    // =====================================================================================
    // 12. ±Δ — SMALL WORDING CHANGE, DIFFERENT PATTERN
    // =====================================================================================
    /*
     * "same node object / shared suffix"     -> HEAD SWITCHING
     * "same value appears in both lists"     -> set / search by VALUE
     * "find middle"                          -> FAST/SLOW
     * "detect a cycle"                       -> FAST/SLOW
     * "remove nth node from end"             -> FIXED GAP
     * "lists may contain cycles"             -> CYCLE CASE ANALYSIS
     * "lengths are already known"            -> LENGTH ALIGNMENT is very natural
     *
     * This problem is tagged Two Pointers,
     * but "two pointers" is the mechanism, not the invariant.
     */

    // =====================================================================================
    // 13. CUSTOM REINFORCEMENTS — SAME INVARIANT, DIFFERENT DOMAIN
    // =====================================================================================
    /*
     * WHY KEEP THESE:
     * These are deliberately NOT new algorithms.
     * They are transfer exercises for the SAME invariant:
     *
     *     unequal starting offsets
     *     + forward-only traversal
     *     + possible shared suffix by IDENTITY
     *     + no buffering
     *     -> equalize TOTAL traversal
     *
     * Their value is abstraction:
     * can you recognize the structure when the word "linked list" disappears?
     *
     * These are NOT LeetCode problems.
     */

    /*
     * -----------------------------------------------------------------------------
     * REINFORCEMENT 1 — FORWARD-ONLY LOG STREAM INTERSECTION
     * -----------------------------------------------------------------------------
     *
     * Two append-only log streams may share a replicated suffix.
     * Entries are immutable objects and traversal is forward-only.
     * The replication point is unknown and full buffering is disallowed.
     *
     * Return the first shared LogEntry by REFERENCE, or null.
     *
     * MAPPING:
     * LogEntry -> ListNode
     * next     -> next
     * stream   -> linked chain
     *
     * SAME INVARIANT:
     * both readers consume the same total route.
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

            while (readerA != readerB) {
                readerA = (readerA == null) ? headB : readerA.next;
                readerB = (readerB == null) ? headA : readerB.next;
            }

            return readerA;
        }
    }

    /*
     * -----------------------------------------------------------------------------
     * REINFORCEMENT 2 — DISTRIBUTED TIMELINE CONVERGENCE
     * -----------------------------------------------------------------------------
     *
     * Two systems maintain forward-only immutable event timelines.
     * They may eventually converge onto the same Event objects after reconciliation.
     * There is no timestamp/global ordering and no full buffering.
     *
     * Return the first shared Event object by IDENTITY, or null.
     *
     * SAME INVARIANT:
     * equalize total traversal, not starting position.
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

            while (cursorA != cursorB) {
                cursorA = (cursorA == null) ? headB : cursorA.next;
                cursorB = (cursorB == null) ? headA : cursorB.next;
            }

            return cursorA;
        }
    }

    /*
     * -----------------------------------------------------------------------------
     * REINFORCEMENT 3 — APPEND-ONLY ITERATOR CONVERGENCE
     * -----------------------------------------------------------------------------
     *
     * Two forward-only append-only iterator chains may begin at unequal offsets
     * and later converge onto the same underlying IteratorNode objects.
     * Rewind and buffering are unavailable.
     *
     * Return the first shared IteratorNode by IDENTITY, or null.
     *
     * SAME INVARIANT:
     * each iterator traverses both routes, so the unknown offset cancels.
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
            IteratorNode iteratorA = startA;
            IteratorNode iteratorB = startB;

            while (iteratorA != iteratorB) {
                iteratorA = (iteratorA == null) ? startB : iteratorA.next;
                iteratorB = (iteratorB == null) ? startA : iteratorB.next;
            }

            return iteratorA;
        }
    }

    // =====================================================================================
    // 14. RELATED REINFORCEMENT — SAME TWO-POINTER MECHANISM, DIFFERENT INVARIANT
    // =====================================================================================
    /*
     * PURE same-pattern LeetCode reinforcement is rare: LC160 is essentially the canonical one.
     * So reinforce the FAMILY by contrasting the invariant.
     *
     * A. LC876 Middle of the Linked List
     *    Invariant: fast moves 2x, so slow reaches the middle.
     *
     * B. LC141 Linked List Cycle
     *    Invariant: relative speed forces collision inside a cycle.
     *
     * C. LC19 Remove Nth Node From End
     *    Invariant: preserve an n-node gap between fast and slow.
     *
     * D. LC142 Linked List Cycle II
     *    Invariant: after collision, distance geometry identifies cycle entry.
     */

    static class MiddleOfLinkedListReinforcement {
        static ListNode middleNode(ListNode head) {
            ListNode slow = head;
            ListNode fast = head;

            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }

            return slow;
        }
    }

    static class LinkedListCycleReinforcement {
        static boolean hasCycle(ListNode head) {
            ListNode slow = head;
            ListNode fast = head;

            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;

                if (slow == fast) {
                    return true;
                }
            }

            return false;
        }
    }

    static class RemoveNthFromEndReinforcement {
        static ListNode removeNthFromEnd(ListNode head, int n) {
            ListNode dummy = new ListNode(0);
            dummy.next = head;

            ListNode slow = dummy;
            ListNode fast = dummy;

            for (int i = 0; i <= n; i++) {
                fast = fast.next;
            }

            while (fast != null) {
                slow = slow.next;
                fast = fast.next;
            }

            slow.next = slow.next.next;
            return dummy.next;
        }
    }

    static class LinkedListCycleIIReinforcement {
        static ListNode detectCycle(ListNode head) {
            ListNode slow = head;
            ListNode fast = head;

            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;

                if (slow == fast) {
                    ListNode entry = head;

                    while (entry != slow) {
                        entry = entry.next;
                        slow = slow.next;
                    }

                    return entry;
                }
            }

            return null;
        }
    }

    // =====================================================================================
    // 15. INTERVIEW ARTICULATION
    // =====================================================================================
    /*
     * BEFORE CODING:
     * "Intersection is by node identity. Because the lists are singly linked and acyclic,
     *  any intersection forms a shared suffix. The problem is unequal prefix lengths.
     *  I can align them explicitly by length, or avoid storing lengths by letting each
     *  pointer traverse both lists. Then both cover m+n distance, which cancels the offset."
     *
     * AFTER CODING:
     * "Each pointer visits at most both lists once, so time is O(m+n), space is O(1),
     *  and the input structure is unchanged. If there is no intersection, both end at null."
     */

    // =====================================================================================
    // 16. 30-SECOND RECALL CARD
    // =====================================================================================
    /*
     * TRIGGER:
     * two acyclic lists + shared node identity + unequal prefixes
     *
     * DERIVE:
     * align lengths -> then compress alignment by switching heads
     *
     * TYPE:
     * p = headA, q = headB
     * while (p != q)
     *     p = p == null ? headB : p.next
     *     q = q == null ? headA : q.next
     * return p
     *
     * WHY:
     * A+B == B+A in total traversal length
     *
     * TRAP:
     * identity, not value
     */

    // =====================================================================================
    // 17. SELF-VERIFYING TESTS — LAST
    // =====================================================================================
    public static void main(String[] args) {
        testIntersectionApproaches();
        testEdgeCases();
        testCustomReinforcements();
        testReinforcements();

        System.out.println("All tests passed ✔");
    }

    private static void testIntersectionApproaches() {
        ListNode common = chain(8, 10);

        ListNode headA = chain(3, 7);
        tail(headA).next = common;

        ListNode headB = chain(99, 1, 5);
        tail(headB).next = common;

        check(BruteForceSolution.getIntersectionNode(headA, headB) == common);
        check(HashSetSolution.getIntersectionNode(headA, headB) == common);
        check(LengthAlignmentSolution.getIntersectionNode(headA, headB) == common);
        check(OptimalSolution.getIntersectionNode(headA, headB) == common);
    }

    private static void testEdgeCases() {
        ListNode common = chain(8, 10);
        check(OptimalSolution.getIntersectionNode(common, common) == common);

        ListNode a = chain(1, 2, 3);
        ListNode b = chain(4, 5);
        check(OptimalSolution.getIntersectionNode(a, b) == null);

        check(OptimalSolution.getIntersectionNode(null, b) == null);
        check(OptimalSolution.getIntersectionNode(null, null) == null);
    }

    private static void testCustomReinforcements() {
        LogEntry commonLog = new LogEntry("shared-1");
        commonLog.next = new LogEntry("shared-2");

        LogEntry logA = new LogEntry("a-1");
        logA.next = commonLog;

        LogEntry logB = new LogEntry("b-1");
        logB.next = new LogEntry("b-2");
        logB.next.next = commonLog;

        check(LogStreamIntersection.findIntersection(logA, logB) == commonLog);

        Event commonEvent = new Event(100);
        commonEvent.next = new Event(101);

        Event eventA = new Event(1);
        eventA.next = commonEvent;

        Event eventB = new Event(2);
        eventB.next = new Event(3);
        eventB.next.next = commonEvent;

        check(TimelineConvergence.findFirstCommonEvent(eventA, eventB) == commonEvent);

        IteratorNode commonIteratorNode = new IteratorNode(50);
        commonIteratorNode.next = new IteratorNode(60);

        IteratorNode iteratorA = new IteratorNode(10);
        iteratorA.next = commonIteratorNode;

        IteratorNode iteratorB = new IteratorNode(20);
        iteratorB.next = new IteratorNode(30);
        iteratorB.next.next = commonIteratorNode;

        check(IteratorConvergence.findConvergence(iteratorA, iteratorB) == commonIteratorNode);
    }

    private static void testReinforcements() {
        ListNode middleList = chain(1, 2, 3, 4, 5);
        check(MiddleOfLinkedListReinforcement.middleNode(middleList).val == 3);

        ListNode cycle = chain(1, 2, 3, 4);
        ListNode cycleEntry = cycle.next;
        tail(cycle).next = cycleEntry;
        check(LinkedListCycleReinforcement.hasCycle(cycle));
        check(LinkedListCycleIIReinforcement.detectCycle(cycle) == cycleEntry);

        ListNode removable = chain(1, 2, 3, 4, 5);
        ListNode result = RemoveNthFromEndReinforcement.removeNthFromEnd(removable, 2);
        check(toString(result).equals("1->2->3->5"));
    }

    private static ListNode chain(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        for (int value : values) {
            current.next = new ListNode(value);
            current = current.next;
        }

        return dummy.next;
    }

    private static ListNode tail(ListNode head) {
        ListNode current = head;
        while (current.next != null) {
            current = current.next;
        }
        return current;
    }

    private static String toString(ListNode head) {
        StringBuilder result = new StringBuilder();

        for (ListNode current = head; current != null; current = current.next) {
            if (!result.isEmpty()) {
                result.append("->");
            }
            result.append(current.val);
        }

        return result.toString();
    }

    private static void check(boolean condition) {
        if (!condition) {
            throw new AssertionError("Test failed");
        }
    }
}
