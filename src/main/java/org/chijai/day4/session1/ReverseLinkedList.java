package org.chijai.day4.session1;


/**
 * =====================================================================================
 * 📘 ALGORITHM TEXTBOOK CHAPTER
 * =====================================================================================
 * Title: Reverse Linked List
 * Pattern: Iterative Pointer Reversal (In-Place Linked List Transformation)
 *
 * This file is a COMPLETE, standalone, IntelliJ-ready Java chapter.
 * It is designed for mastery, interviews, debugging, and long-term recall.
 *
 * =====================================================================================
 */
public class ReverseLinkedList {

    // =====================================================================================
    // 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
    // =====================================================================================
    /*
     * 🔗 https://leetcode.com/problems/reverse-linked-list/
     *
     * 🧩 Difficulty: Easy
     * 🏷️ Tags: Linked List, Recursion
     *
     * -------------------------------------------------------------------------------------
     * Given the head of a singly linked list, reverse the list, and return the reversed list.
     *
     * Example 1:
     * Input: head = [1,2,3,4,5]
     * Output: [5,4,3,2,1]
     *
     * Example 2:
     * Input: head = [1,2]
     * Output: [2,1]
     *
     * Example 3:
     * Input: head = []
     * Output: []
     *
     * Constraints:
     * The number of nodes in the list is the range [0, 5000].
     * -5000 <= Node.val <= 5000
     *
     * Follow up: A linked list can be reversed either iteratively or recursively.
     * Could you implement both?
     *
     * -------------------------------------------------------------------------------------
     * Definition for singly-linked list:
     *
     * public class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode() {}
     *     ListNode(int val) { this.val = val; }
     *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    // =====================================================================================

    // =====================================================================================
    // 🔵 CORE PATTERN OVERVIEW
    // =====================================================================================
    /*
     * Pattern Name:
     * 👉 In-Place Pointer Reversal (Iterative Linked List Transformation)
     *
     * Core Idea:
     * Reverse the direction of `next` pointers one node at a time while traversing the list.
     *
     * Why It Works:
     * Each node only needs to know:
     * 1) Where it came from (previous)
     * 2) Where it was going (next)
     *
     * When to Use:
     * • Reversing a linked list
     * • Partial reversals (k-group)
     * • Palindrome checks
     * • Reordering lists
     *
     * 🧭 Pattern Recognition Signals:
     * • Singly linked list
     * • Directional change required
     * • O(1) space expectation
     *
     * How It Differs from Similar Patterns:
     * • NOT two pointers moving inward
     * • NOT recursion-based tree reversal
     * • This pattern mutates pointers, not values
     */
    // =====================================================================================

    // =====================================================================================
    // 🟢 MENTAL MODEL & INVARIANTS
    // =====================================================================================
    /*
     * Mental Model:
     * Think of walking forward while flipping arrows behind you.
     *
     * Invariant:
     * • `prev` always points to the fully reversed prefix.
     * • `curr` points to the first unreversed node.
     * • No node is ever lost because `next` is saved before mutation.
     *
     * Variable Roles:
     * • prev → Head of reversed portion
     * • curr → Current node being processed
     * • next → Temporary storage to preserve forward traversal
     *
     * Termination Logic:
     * Loop ends when curr == null (entire list processed).
     *
     * Forbidden Actions:
     * ❌ Mutating curr.next before saving it
     * ❌ Losing reference to remaining list
     *
     * Why Common Alternatives Are Inferior:
     * • Using stacks → O(n) extra space
     * • Value swapping → Violates node identity
     */
    // =====================================================================================

    // =====================================================================================
    // 🔴 WHY NAIVE / WRONG SOLUTIONS FAIL
    // =====================================================================================
    /*
     * Typical Wrong Approaches:
     * 1) Reassigning curr.next before saving next
     * 2) Using recursion without understanding stack depth
     *
     * Why They Seem Correct:
     * • Small test cases pass
     * • Visual symmetry illusion
     *
     * Violated Invariant:
     * ❌ Forward traversal pointer is lost
     *
     * Counterexample:
     * Input: 1 -> 2 -> 3
     * If next is not saved, node 3 becomes unreachable.
     *
     * Interviewer Trap:
     * "What happens to the rest of the list after you reverse this pointer?"
     */
    // =====================================================================================

    // =====================================================================================
    // PRIMARY PROBLEM — SOLUTION CLASSES
    // =====================================================================================

    // -------------------------------------------------------------------------------------
    // 🔹 Brute Force Solution
    // -------------------------------------------------------------------------------------
    static class BruteForceSolution {
        /*
         * Core Idea:
         * Copy values into array, reverse array, rebuild list.
         *
         * Fixes:
         * • Simplicity
         *
         * Time: O(n)
         * Space: O(n)
         * Interview Preference: ❌ Poor
         */
        static ListNode reverseList(ListNode head) {
            java.util.List<Integer> values = new java.util.ArrayList<>();
            ListNode current = head;
            while (current != null) {
                values.add(current.val);
                current = current.next;
            }
            current = head;
            for (int i = values.size() - 1; i >= 0; i--) {
                current.val = values.get(i);
                current = current.next;
            }
            return head;
        }
    }

    // -------------------------------------------------------------------------------------
    // 🔹 Improved Solution (Recursive)
    // -------------------------------------------------------------------------------------
    static class RecursiveSolution {
        /*
         * Core Idea:
         * Reverse from second node onward, then fix head.
         *
         * Time: O(n)
         * Space: O(n) recursion stack
         * Interview Preference: ⚠️ Acceptable with explanation
         */
        static ListNode reverseList(ListNode head) {
            if (head == null || head.next == null) return head;

            ListNode reversedHead = reverseList(head.next);

            // Fix pointers
            head.next.next = head;
            head.next = null;

            return reversedHead;
        }
    }

    // -------------------------------------------------------------------------------------
    // 🔹 Optimal Solution (Interview-Preferred)
    // -------------------------------------------------------------------------------------
    static class OptimalSolution {
        /*
         * Core Idea:
         * Iteratively reverse pointers in one pass.
         *
         * Time: O(n)
         * Space: O(1)
         * Interview Preference: ✅ Gold Standard
         */
        static ListNode reverseList(ListNode head) {

            // 🟢 prev is the head of reversed list so far
            ListNode previous = null;

            // 🟢 current is the node being processed
            ListNode current = head;

            while (current != null) {

                // 🟡 Preserve forward link before mutation
                ListNode nextNode = current.next;

                // 🔵 Reverse pointer direction
                current.next = previous;

                // 🟢 Advance invariant holders
                previous = current;
                current = nextNode;
            }

            // 🟢 previous is new head
            return previous;
        }
    }

    // =====================================================================================
    // 🟣 INTERVIEW ARTICULATION
    // =====================================================================================
    /*
     * Why Optimal Works:
     * • Maintains strict invariant: no node lost
     *
     * What Breaks If Changed:
     * • If next is not saved → list truncation
     *
     * In-Place Feasibility:
     * ✅ Yes (pointer reassignment only)
     *
     * Streaming Feasibility:
     * ❌ No (needs backward links)
     *
     * When NOT to Use:
     * • Immutable structures
     * • Doubly-linked list with constraints
     *
     * Whiteboard Explanation:
     * "I walk through the list once, reversing arrows behind me."
     */
    // =====================================================================================

    // =====================================================================================
    // 🔄 VARIATIONS & TWEAKS
    // =====================================================================================
    /*
     * 🟢 Invariant-Preserving:
     * • Reverse sublist
     * • Reverse in k-groups
     *
     * 🟡 Reasoning-Only:
     * • Recursive form
     *
     * 🔴 Pattern-Break:
     * • Random access required
     * • Immutable nodes
     */
    // =====================================================================================

    // =====================================================================================
    // ⚫ REINFORCEMENT PROBLEMS
    // =====================================================================================
    /*
     * 1) Reverse Linked List II
     * 2) Reverse Nodes in k-Group
     * 3) Palindrome Linked List
     *
     * (Omitted here for brevity in this chapter version,
     * but pattern remains identical: pointer reversal invariant.)
     */
    // =====================================================================================

    // =====================================================================================
    // 🟢 LEARNING VERIFICATION
    // =====================================================================================
    /*
     * Mastery Check:
     * • Can you explain invariant without code? YES
     * • Can you reverse partial list? YES
     * • Can you debug lost-node bug? YES
     */
    // =====================================================================================

    // =====================================================================================
    // 🧪 main() METHOD + SELF-VERIFYING TESTS
    // =====================================================================================
    public static void main(String[] args) {

        // Happy Path
        ListNode list = ListNode.fromArray(new int[]{1, 2, 3, 4, 5});
        ListNode reversed = OptimalSolution.reverseList(list);
        assertEquals(new int[]{5, 4, 3, 2, 1}, reversed, "Happy path");

        // Single element
        list = ListNode.fromArray(new int[]{1});
        reversed = OptimalSolution.reverseList(list);
        assertEquals(new int[]{1}, reversed, "Single element");

        // Empty list
        list = null;
        reversed = OptimalSolution.reverseList(list);
        assert reversed == null : "Empty list failed";

        // Interview trap: two nodes
        list = ListNode.fromArray(new int[]{1, 2});
        reversed = OptimalSolution.reverseList(list);
        assertEquals(new int[]{2, 1}, reversed, "Two node reversal");

        System.out.println("✅ All tests passed.");
    }

    // =====================================================================================
    // 🧠 CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
    // =====================================================================================
    /*
     * Invariant clarity
     * → prev is always head of reversed prefix
     *
     * Search target clarity
     * → Entire list traversal
     *
     * Discard logic
     * → Forward pointer preserved before reversal
     *
     * Termination guarantee
     * → current moves forward, becomes null
     *
     * Failure awareness
     * → Losing next pointer breaks list
     *
     * Edge-case confidence
     * → null and single node handled explicitly
     *
     * Variant readiness
     * → Partial reversal modifies boundaries
     *
     * Pattern boundary
     * → Not usable for immutable nodes
     */
    // =====================================================================================

    // =====================================================================================
    // 🧘 FINAL CLOSURE STATEMENT
    // =====================================================================================
    /*
     * For this problem, the invariant is that the prefix before `current`
     * is always fully reversed.
     *
     * The answer represents the new head of the list.
     *
     * The search terminates because `current` strictly moves forward.
     *
     * I can re-derive this solution under pressure.
     *
     * This chapter is complete.
     */
    // =====================================================================================

    // =====================================================================================
    // 🔧 SUPPORTING DATA STRUCTURES
    // =====================================================================================
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }

        static ListNode fromArray(int[] values) {
            if (values == null || values.length == 0) return null;
            ListNode head = new ListNode(values[0]);
            ListNode current = head;
            for (int i = 1; i < values.length; i++) {
                current.next = new ListNode(values[i]);
                current = current.next;
            }
            return head;
        }
    }

    static void assertEquals(int[] expected, ListNode actual, String testName) {
        for (int value : expected) {
            assert actual != null && actual.val == value :
                    "❌ " + testName + " failed";
            actual = actual.next;
        }
        assert actual == null : "❌ " + testName + " extra nodes";
    }
}
