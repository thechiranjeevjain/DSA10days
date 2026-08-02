package org.chijai.day4.LinkedList.session1;

/**
 * =================================================================================================
 * 🧠 ALGORITHM TEXTBOOK CHAPTER — LINKED LIST CYCLE DETECTION (FLOYD’S TORTOISE & HARE)
 * =================================================================================================
 *
 * This is a SINGLE, IntelliJ-ready, self-contained Java file.
 * It is a COMPLETE algorithm chapter — not notes, not snippets.
 *
 * Pattern Mastered Here:
 * 🔵 Two Pointers with Relative Speed (Floyd’s Cycle Detection)
 *
 * =================================================================================================
 * 1️⃣ TOP-LEVEL PUBLIC CLASS DECLARATION
 * =================================================================================================
 */
public class LinkedListCycle {

    /* =============================================================================================
     * 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
     * =============================================================================================
     *
     * 🔗 Link: https://leetcode.com/problems/linked-list-cycle/
     * 🧩 Difficulty: Easy
     * 🏷️ Tags: Linked List, Two Pointers
     *
     * ---------------------------------------------------------------------------------------------
     * Given head, the head of a linked list, determine if the linked list has a cycle in it.
     *
     * There is a cycle in a linked list if there is some node in the list that can be reached again
     * by continuously following the next pointer. Internally, pos is used to denote the index of
     * the node that tail's next pointer is connected to. Note that pos is not passed as a parameter.
     *
     * Return true if there is a cycle in the linked list. Otherwise, return false.
     *
     * Example 1:
     * Input: head = [3,2,0,-4], pos = 1
     * Output: true
     * Explanation: There is a cycle in the linked list, where the tail connects to the 1st node (0-indexed).
     *
     * Example 2:
     * Input: head = [1,2], pos = 0
     * Output: true
     * Explanation: There is a cycle in the linked list, where the tail connects to the 0th node.
     *
     * Example 3:
     * Input: head = [1], pos = -1
     * Output: false
     * Explanation: There is no cycle in the linked list.
     *
     * Constraints:
     * • The number of the nodes in the list is in the range [0, 10^4].
     * • -10^5 <= Node.val <= 10^5
     * • pos is -1 or a valid index in the linked-list.
     */

    /* =============================================================================================
     * 3️⃣ 🔵 CORE PATTERN OVERVIEW
     * =============================================================================================
     *
     * Pattern Name:
     * 🔵 Floyd’s Cycle Detection Algorithm (Two Pointers with Relative Speed)
     *
     * Core Idea:
     * • Move two pointers through the structure at different speeds.
     * • If a cycle exists, the faster pointer MUST eventually lap the slower one.
     *
     * Why It Works:
     * • In cyclic space, relative speed guarantees collision.
     * • In acyclic space, fast pointer exits (null) first.
     *
     * When To Use:
     * • Linked list cycle detection
     * • Detecting loops without extra memory
     * • When modification of structure is forbidden
     *
     * 🧭 Pattern Recognition Signals:
     * • “Detect cycle”
     * • “Repeated visitation”
     * • “Infinite traversal risk”
     * • “Do not modify list”
     *
     * How This Differs From Similar Patterns:
     * • Unlike HashSet tracking, this uses O(1) space.
     * • Unlike visited marking, it preserves structure.
     */

    /* =============================================================================================
     * 4️⃣ 🟢 MENTAL MODEL & INVARIANTS
     * =============================================================================================
     *
     * 🟢 Mental Model:
     * Imagine runners on a circular track.
     * • One runs at 1x speed (slow)
     * • One runs at 2x speed (fast)
     * If the track is circular, the faster runner WILL catch the slower one.
     *
     * 🟢 Invariants:
     * • slow advances exactly 1 node per iteration
     * • fast advances exactly 2 nodes per iteration
     * • If fast reaches null → no cycle exists
     * • If slow == fast → cycle exists
     *
     * Variable Roles:
     * • slow → proof-of-progress pointer
     * • fast → escape-or-collision pointer
     *
     * Termination Logic:
     * • Loop ends when:
     *   1) fast == null
     *   2) fast.next == null
     *   3) slow == fast
     *
     * ❌ Forbidden Actions:
     * • Modifying next pointers
     * • Using extra memory unless explicitly allowed
     *
     * Why Common Alternatives Are Inferior:
     * • HashSet uses O(n) memory
     * • Node marking mutates input
     */

    /* =============================================================================================
     * 5️⃣ 🔴 WHY NAIVE / WRONG SOLUTIONS FAIL
     * =============================================================================================
     *
     * ❌ Wrong Approach 1: Traverse indefinitely
     * • Seems logical but never terminates on cycle
     *
     * ❌ Wrong Approach 2: Compare values instead of nodes
     * • Duplicate values ≠ same node
     *
     * ❌ Wrong Approach 3: HashSet of visited nodes
     * • Violates space constraint
     *
     * Interviewer Trap:
     * • “What if values repeat?”
     * • “Can you do this without memory?”
     */

    /* =============================================================================================
     * 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES
     * =============================================================================================
     */

    /**
     * Definition for singly-linked list.
     */
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }

    /* ---------------------------------------------------------------------------------------------
     * 🔴 BRUTE FORCE SOLUTION — HashSet
     * ---------------------------------------------------------------------------------------------
     */
    static class BruteForceSolution {
        /**
         * Core Idea:
         * • Track visited nodes using HashSet
         *
         * Fixes:
         * • Prevents infinite traversal
         *
         * Time: O(n)
         * Space: O(n)
         * Interview Preference: ❌ Only if memory allowed
         */
        public boolean hasCycle(ListNode head) {
            java.util.HashSet<ListNode> visited = new java.util.HashSet<>();

            while (head != null) {
                if (visited.contains(head)) {
                    return true; // 🔴 revisiting same node
                }
                visited.add(head);
                head = head.next;
            }
            return false;
        }
    }

    /* ---------------------------------------------------------------------------------------------
     * 🟡 IMPROVED SOLUTION — Node Mutation (Not Recommended)
     * ---------------------------------------------------------------------------------------------
     */
    static class ImprovedSolution {
        /**
         * Core Idea:
         * • Mark visited nodes by altering value
         *
         * Why It’s Inferior:
         * • Modifies input
         * • Unsafe if values matter
         *
         * Time: O(n)
         * Space: O(1)
         * Interview Preference: ❌ Avoid
         */
        public boolean hasCycle(ListNode head) {
            while (head != null) {
                if (head.val == Integer.MIN_VALUE) {
                    return true;
                }
                head.val = Integer.MIN_VALUE;
                head = head.next;
            }
            return false;
        }
    }

    /* ---------------------------------------------------------------------------------------------
     * 🟢 OPTIMAL SOLUTION — Floyd’s Cycle Detection
     * ---------------------------------------------------------------------------------------------
     */
    static class OptimalSolution {
        /**
         * Core Idea:
         * • Two pointers moving at different speeds
         *
         * Time: O(n)
         * Space: O(1)
         * Interview Preference: ✅ GOLD STANDARD
         */
        public boolean hasCycle(ListNode head) {
            if (head == null || head.next == null) {
                return false; // 🟢 empty or single node cannot cycle
            }

            ListNode slow = head;
            ListNode fast = head;

            while (fast != null && fast.next != null) {
                slow = slow.next;           // 🟢 move 1 step
                fast = fast.next.next;     // 🟢 move 2 steps

                if (slow == fast) {
                    return true;            // 🔵 collision proves cycle
                }
            }
            return false; // 🟢 fast escaped → no cycle
        }
    }

    /* =============================================================================================
     * 7️⃣ 🟣 INTERVIEW ARTICULATION
     * =============================================================================================
     *
     * • Why it works:
     *   Relative speed guarantees collision in cycle.
     *
     * • Correctness invariant:
     *   Distance between fast and slow shrinks mod cycle length.
     *
     * • What breaks if changed:
     *   If both move at same speed → no collision.
     *
     * • In-place feasibility:
     *   Yes.
     *
     * • Streaming feasibility:
     *   Yes — no memory needed.
     *
     * • When NOT to use:
     *   When you need cycle entry index (needs extension).
     */

    /* =============================================================================================
     * 8️⃣ 🔄 VARIATIONS & TWEAKS
     * =============================================================================================
     *
     * 🟢 Invariant-Preserving:
     * • Change speeds (1x, 3x still works)
     *
     * 🟡 Reasoning-Only:
     * • Detect cycle length after collision
     *
     * 🔴 Pattern-Break:
     * • DAG or tree structures
     */

    /* =============================================================================================
     * 9️⃣ ⚫ REINFORCEMENT PROBLEMS (SUMMARY)
     * =============================================================================================
     *
     * • Linked List Cycle II
     * • Find Duplicate Number
     * • Happy Number
     *
     * (Same pattern: cycle in state transitions)
     */

    /* =============================================================================================
     * 11️⃣ 🟢 LEARNING VERIFICATION
     * =============================================================================================
     *
     * • Can you explain collision inevitability?
     * • Can you detect cycle without memory?
     * • Can you derive entry point?
     */

    /* =============================================================================================
     * 12️⃣ 🧪 main() METHOD + SELF-VERIFYING TESTS
     * =============================================================================================
     */
    public static void main(String[] args) {
        OptimalSolution solution = new OptimalSolution();

        // Happy path: cycle exists
        ListNode a = new ListNode(1);
        ListNode b = new ListNode(2);
        ListNode c = new ListNode(3);
        a.next = b;
        b.next = c;
        c.next = b; // cycle

        assertTrue(solution.hasCycle(a), "Cycle should be detected");

        // Boundary: no cycle
        ListNode x = new ListNode(1);
        ListNode y = new ListNode(2);
        x.next = y;

        assertFalse(solution.hasCycle(x), "No cycle should be detected");

        // Single node
        ListNode single = new ListNode(1);
        assertFalse(solution.hasCycle(single), "Single node cannot have cycle");

        System.out.println("✅ All tests passed.");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }

    /* =============================================================================================
     * 13️⃣ 🧠 CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
     * =============================================================================================
     *
     * • Invariant clarity
     * → Answer: fast moves 2x, slow moves 1x; collision ⇔ cycle
     *
     * • Search target clarity
     * → Answer: Detect node revisitation via pointer collision
     *
     * • Discard logic
     * → Answer: fast reaching null eliminates cycle
     *
     * • Termination guarantee
     * → Answer: fast exits or collides
     *
     * • Failure awareness
     * → Answer: naive traversal loops forever
     *
     * • Edge-case confidence
     * → Answer: null and single-node handled upfront
     *
     * • Variant readiness
     * → Answer: extend after collision to find entry
     *
     * • Pattern boundary
     * → Answer: not for acyclic graphs with branches
     */

    /* =============================================================================================
     * 🧘 FINAL CLOSURE STATEMENT
     * =============================================================================================
     *
     * For this problem, the invariant is relative pointer speed.
     * The answer represents proof of revisitation.
     * The search terminates because fast either escapes or collides.
     * I can re-derive this solution under pressure.
     * This chapter is complete.
     *
     * 📌 RULE TO PREVENT OVER-STUDY:
     * If I can explain it, I don’t need to reread it.
     */
}

