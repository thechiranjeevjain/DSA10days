package org.chijai.day4.LinkedList.session4;


import java.util.ArrayList;
import java.util.List;

public class MiddleOfLinkedList {

    /*
     * ============================================================
     * 2. 📘 PRIMARY PROBLEM
     * ============================================================
     *
     * Title:
     * Middle of Linked List
     *
     * Difficulty:
     * Easy
     *
     * Tags:
     * Linked List
     * Two Pointers
     * Fast & Slow Pointer
     *
     * Problem Description
     * -------------------
     * Given the head of a singly linked list, return the middle node.
     *
     * If the linked list contains two middle nodes (even length),
     * return the SECOND middle node.
     *
     * The returned value is the node itself rather than its value.
     *
     * Constraints
     * -----------
     * 1 <= Number of Nodes <= 100
     * 1 <= Node.val <= 100
     *
     * Examples
     * --------
     *
     * Example 1
     *
     * Input:
     * 1 -> 2 -> 3 -> 4 -> 5
     *
     * Output:
     * 3 -> 4 -> 5
     *
     * Example 2
     *
     * Input:
     * 1 -> 2 -> 3 -> 4 -> 5 -> 6
     *
     * Output:
     * 4 -> 5 -> 6
     *
     * Since there are two middle nodes (3 and 4),
     * return the second one.
     *
     * Official LeetCode:
     * https://leetcode.com/problems/middle-of-the-linked-list/
     */

    /*
     * ============================================================
     * Helper List Node
     * ============================================================
     */

    static class ListNode {

        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    /*
     * ============================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern
     * -------
     * Fast & Slow Pointer
     *
     * Archetype
     * ---------
     * Differential-speed traversal.
     *
     * One pointer progresses faster than another so that
     * useful positional information emerges naturally.
     *
     * Core Invariant
     * --------------
     * During every iteration:
     *
     * fast has travelled exactly twice as many edges as slow.
     *
     * Therefore,
     * when fast reaches the end,
     * slow has travelled exactly half the distance.
     *
     * That position is precisely the required middle.
     *
     * Why It Works
     * ------------
     * Instead of computing the total length first,
     * we discover the halfway point while traversing only once.
     *
     * The distance ratio (2 : 1) continuously preserves
     * the invariant.
     *
     * Recognition Signals
     * -------------------
     * Think Fast & Slow Pointer whenever you see:
     *
     * • middle node
     * • halfway point
     * • detect cycle
     * • entrance of cycle
     * • kth-from-end
     * • simultaneous traversal at different speeds
     *
     * When To Use
     * -----------
     * ✓ Need midpoint
     * ✓ Single traversal preferred
     * ✓ Linked list
     * ✓ Constant extra space
     *
     * When NOT To Use
     * ----------------
     * ✗ Random indexing available
     * ✗ Need arbitrary positions repeatedly
     * ✗ Array provides O(1) indexing naturally
     *
     * Comparison
     * ----------
     *
     * Counting Length
     *
     * Pass 1:
     * Count nodes.
     *
     * Pass 2:
     * Walk to n/2.
     *
     * Complexity:
     * O(n)
     * Two traversals.
     *
     * Fast & Slow
     *
     * One traversal.
     * Same complexity.
     * Smaller traversal cost.
     * Cleaner interview solution.
     */

    /*
     * ============================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * Mental Model
     * ------------
     * Imagine two runners on a circular track.
     *
     * Runner A:
     * walks one step.
     *
     * Runner B:
     * walks two steps.
     *
     * When Runner B finishes,
     * Runner A has naturally completed half the journey.
     *
     * Exactly the same idea works on a linked list.
     *
     * -------------------------
     * Primary Invariant
     * -------------------------
     *
     * After k iterations:
     *
     * slow has moved k nodes.
     *
     * fast has moved 2k nodes.
     *
     * Distance(fast)
     * =
     * 2 × Distance(slow)
     *
     * -------------------------
     * State Variables
     * -------------------------
     *
     * slow
     * ----
     * Candidate middle.
     *
     * fast
     * ----
     * Progress indicator.
     *
     * fast determines
     * how much list remains.
     *
     * -------------------------
     * Allowed Transition
     * -------------------------
     *
     * slow = slow.next
     *
     * fast = fast.next.next
     *
     * Both moves happen together.
     *
     * Never move only one pointer.
     *
     * -------------------------
     * Forbidden Moves
     * -------------------------
     *
     * ❌ Move slow twice.
     *
     * ❌ Move fast once.
     *
     * ❌ Advance fast before checking next.
     *
     * ❌ Dereference fast.next.next when
     * fast.next is null.
     *
     * -------------------------
     * Loop Condition
     * -------------------------
     *
     * while (fast != null &&
     *        fast.next != null)
     *
     * Meaning:
     *
     * Fast must still be able
     * to perform its full
     * two-step transition.
     *
     * -------------------------
     * Even Length
     * -------------------------
     *
     * 1 2 3 4 5 6
     *
     * slow starts at 1
     * fast starts at 1
     *
     * Iteration 1
     *
     * slow ->2
     * fast ->3
     *
     * Iteration 2
     *
     * slow ->3
     * fast ->5
     *
     * Iteration 3
     *
     * slow ->4
     * fast ->null
     *
     * Return 4.
     *
     * Exactly the required
     * second middle.
     *
     * -------------------------
     * Odd Length
     * -------------------------
     *
     * 1 2 3 4 5
     *
     * Iteration 1
     *
     * slow ->2
     * fast ->3
     *
     * Iteration 2
     *
     * slow ->3
     * fast ->5
     *
     * Next iteration impossible.
     *
     * Return 3.
     *
     * -------------------------
     * Termination
     * -------------------------
     *
     * Loop stops because
     * fast cannot complete another
     * legal two-step transition.
     *
     * At that exact instant,
     * slow has consumed exactly half
     * the traversal.
     *
     * -------------------------
     * Correctness Intuition
     * -------------------------
     *
     * The algorithm never guesses.
     *
     * The midpoint emerges automatically
     * from the maintained 2:1 distance ratio.
     *
     * -------------------------
     * Why Naive Solutions Fail
     * -------------------------
     *
     * Counting solution:
     *
     * Needs two traversals.
     *
     * More bookkeeping.
     *
     * Easier off-by-one mistakes
     * for even-length lists.
     */

    /*
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * Mistake 1
     * ---------
     * Returning first middle.
     *
     * Example
     *
     * 1 2 3 4
     *
     * Returning 2 violates
     * problem specification.
     *
     * Violated Invariant
     * ------------------
     * Incorrect stopping condition.
     *
     * -------------------------------------------------
     * Mistake 2
     * -------------------------------------------------
     *
     * while(fast.next != null)
     *
     * This crashes when
     * fast becomes null.
     *
     * -------------------------------------------------
     * Mistake 3
     * -------------------------------------------------
     *
     * Move fast first,
     * then check null.
     *
     * NullPointerException.
     *
     * -------------------------------------------------
     * Mistake 4
     * -------------------------------------------------
     *
     * Fast moves only one node.
     *
     * Then both pointers
     * move equally.
     *
     * Ratio becomes 1:1.
     *
     * Midpoint property disappears.
     *
     * -------------------------------------------------
     * Interview Trap
     * -------------------------------------------------
     *
     * Candidate says:
     *
     * "I'll count first."
     *
     * Interviewer asks:
     *
     * Can you do it
     * in one traversal
     * and O(1) space?
     */

    /*
     * ============================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Mechanical Typing Order
     *
     * 1.
     * Create function.
     *
     * 2.
     * Handle empty list if desired.
     *
     * 3.
     * slow = head
     *
     * 4.
     * fast = head
     *
     * 5.
     * while(fast != null &&
     *       fast.next != null)
     *
     * 6.
     * slow moves once.
     *
     * 7.
     * fast moves twice.
     *
     * 8.
     * Return slow.
     *
     * Nothing more.
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * slow ← head
     * fast ← head
     *
     * while fast can move twice
     *     slow ← one step
     *     fast ← two steps
     *
     * return slow
     */

    /*
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    static class BruteForce {

        /*
         * Idea
         * ----
         * Count total nodes.
         * Traverse again to length / 2.
         *
         * Invariant
         * ---------
         * Second traversal always walks
         * toward the computed midpoint.
         *
         * Limitation
         * ----------
         * Requires two traversals.
         *
         * Complexity
         * ----------
         * Time  : O(n)
         * Space : O(1)
         *
         * Interview Usefulness
         * --------------------
         * Good baseline.
         * Usually improved immediately.
         */

        ListNode middleNode(ListNode head) {

            int length = 0;

            ListNode current = head;

            while (current != null) {
                length++;
                current = current.next;
            }

            current = head;

            for (int i = 0; i < length / 2; i++) {
                current = current.next;
            }

            return current;
        }
    }

    static class Improved {


        /*
         * Idea
         * ----
         * Store every node while traversing once.
         *
         * After traversal,
         * directly index the middle node.
         *
         * Invariant
         * ---------
         * list.get(i) always refers to the i-th node
         * encountered during traversal.
         *
         * Improvement
         * -----------
         * Single traversal instead of two,
         * but requires extra memory.
         *
         * Complexity
         * ----------
         * Time  : O(n)
         * Space : O(n)
         *
         * Interview Usefulness
         * --------------------
         * Demonstrates incremental improvement,
         * but is still inferior because the linked
         * list already provides sequential access.
         */

        ListNode middleNode(ListNode head) {

            List<ListNode> nodes = new ArrayList<>();

            ListNode current = head;

            while (current != null) {
                nodes.add(current);
                current = current.next;
            }

            return nodes.get(nodes.size() / 2);
        }
    }

    static class Optimal {

        /*
         * Idea
         * ----
         * Maintain a permanent 2:1 speed ratio.
         *
         * When fast exhausts the remaining search space,
         * slow has consumed exactly half the traversal.
         *
         * Invariant
         * ---------
         * After every completed iteration:
         *
         * distance(fast)
         * =
         * 2 × distance(slow)
         *
         * Correctness
         * -----------
         * The invariant guarantees that slow reaches
         * the unique midpoint for odd length and the
         * required second midpoint for even length.
         *
         * Complexity
         * ----------
         * Time  : O(n)
         * Space : O(1)
         *
         * Interview Usefulness
         * --------------------
         * Preferred solution.
         * Single traversal.
         * Constant extra memory.
         */

        ListNode middleNode(ListNode head) {

            // Invariant: both pointers begin at the same state.
            ListNode slow = head;
            ListNode fast = head;

            // Invariant:
            // Every iteration preserves the 2:1 distance ratio.
            while (fast != null && fast.next != null) {

                // Candidate middle advances one step.
                slow = slow.next;

                // Progress indicator advances two steps.
                fast = fast.next.next;
            }

            // Invariant:
            // Fast can no longer complete two legal moves,
            // therefore slow is exactly at the required middle.
            return slow;
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Pattern
 * -------
 * Fast & Slow Pointer.
 *
 * Invariant
 * ---------
 * Fast always travels exactly twice as far as slow.
 *
 * Search Space
 * ------------
 * The unexplored suffix ahead of fast.
 *
 * Discard Rule
 * ------------
 * There is no explicit discard.
 *
 * Instead,
 * the search space naturally shrinks because
 * fast consumes it twice as quickly.
 *
 * Correctness
 * -----------
 * When fast cannot legally move two steps,
 * exactly half of the list has been traversed
 * by slow.
 *
 * Thus slow is positioned at the required
 * middle node.
 *
 * Termination
 * -----------
 * The loop ends once fast reaches the end
 * or cannot perform another two-step move.
 *
 * In-place Feasibility
 * --------------------
 * Yes.
 *
 * Only two pointers are maintained.
 *
 * Streaming Feasibility
 * ---------------------
 * Yes.
 *
 * Nodes are processed in arrival order.
 * No backward traversal is required.
 *
 * When NOT To Use
 * ----------------
 * If random indexing already exists
 * (arrays),
 * computing the midpoint by index is simpler.
 */

/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 * Halfway through a linked list.
 *
 * Pattern
 * -------
 * Fast & Slow Pointer.
 *
 * Invariant
 * ---------
 * Fast moves two.
 * Slow moves one.
 *
 * Search Target
 * -------------
 * Middle node.
 *
 * Discard Rule
 * ------------
 * None.
 *
 * Speed ratio reveals the answer.
 *
 * Common Trap
 * -----------
 * Incorrect loop condition.
 *
 * Edge Cases
 * ----------
 * Single node.
 * Two nodes.
 * Even length.
 * Odd length.
 *
 * One-liner
 * ---------
 * Fast finishes.
 * Slow reaches the middle.
 *
 * Re-derivation Cue
 * -----------------
 * Ask:
 *
 * "Which pointer should consume the list
 * twice as quickly?"
 */

/*
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variation 1
 * -----------
 * Return first middle instead.
 *
 * Reasoning Change
 * ----------------
 * Start fast one node ahead,
 * or alter the stopping condition.
 *
 * Pattern Preserved
 * -----------------
 * Yes.
 *
 * The speed invariant remains unchanged.
 *
 * ------------------------------------------------
 * Variation 2
 * ------------------------------------------------
 * Detect a cycle.
 *
 * Reasoning Change
 * ----------------
 * Instead of waiting for fast to finish,
 * detect whether fast eventually equals slow.
 *
 * Pattern Preserved
 * -----------------
 * Yes.
 *
 * Same invariant.
 * Different stopping condition.
 *
 * ------------------------------------------------
 * Variation 3
 * ------------------------------------------------
 * Find cycle entry.
 *
 * Reasoning Change
 * ----------------
 * Reset one pointer after collision.
 *
 * Pattern Preserved
 * -----------------
 * Yes.
 *
 * Additional mathematical property.
 *
 * ------------------------------------------------
 * Variation 4
 * ------------------------------------------------
 * Remove N-th node from end.
 *
 * Reasoning Change
 * ----------------
 * Maintain a fixed gap instead of a speed ratio.
 *
 * Pattern Break
 * -------------
 * Speed invariant disappears.
 *
 * Gap invariant replaces it.
 *
 * ------------------------------------------------
 * Variation 5
 * ------------------------------------------------
 * Palindrome Linked List.
 *
 * Reasoning Change
 * ----------------
 * First locate the middle.
 * Reverse the second half.
 * Compare both halves.
 *
 * Pattern Preserved
 * -----------------
 * Midpoint discovery is identical.
 */

/*
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * □ I know the invariant.
 *
 * Fast always advances twice as far as slow.
 *
 * □ I know the search target.
 *
 * Reach the middle in one traversal.
 *
 * □ I know why the answer emerges.
 *
 * The maintained distance ratio forces slow
 * to stop halfway when fast exhausts the list.
 *
 * □ I know the termination condition.
 *
 * fast == null
 * OR
 * fast.next == null
 *
 * □ I know why counting is weaker.
 *
 * Extra traversal.
 *
 * □ I know the edge cases.
 *
 * One node.
 * Two nodes.
 * Even length.
 * Odd length.
 *
 * □ I can debug null pointer failures.
 *
 * Always verify both
 * fast
 * and
 * fast.next
 * before advancing twice.
 *
 * □ I recognize nearby variants.
 *
 * Middle.
 * Cycle.
 * Cycle entry.
 * Remove N-th from end.
 * Palindrome.
 *
 * □ I know the pattern boundary.
 *
 * Differential-speed traversal,
 * not binary search,
 * not sliding window,
 * not two-sum.
 */

    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        /*
         * Happy Path
         *
         * Odd length.
         * Middle should be 3.
         */
        ListNode odd = build(1, 2, 3, 4, 5);

        assert solver.middleNode(odd).val == 3
                : "Odd-length list should return the unique middle node.";

        /*
         * Happy Path
         *
         * Even length.
         * Must return SECOND middle.
         */
        ListNode even = build(1, 2, 3, 4, 5, 6);

        assert solver.middleNode(even).val == 4
                : "Even-length list should return the second middle node.";

        /*
         * Boundary
         *
         * Single node.
         */
        ListNode single = build(42);

        assert solver.middleNode(single).val == 42
                : "Single-node list should return itself.";

        /*
         * Boundary
         *
         * Two nodes.
         * Second node is the required middle.
         */
        ListNode two = build(7, 9);

        assert solver.middleNode(two).val == 9
                : "Two-node list should return the second node.";

        /*
         * Representative
         *
         * Seven nodes.
         */
        ListNode seven = build(10, 20, 30, 40, 50, 60, 70);

        assert solver.middleNode(seven).val == 40
                : "Middle of seven nodes should be node 40.";

        /*
         * Representative
         *
         * Eight nodes.
         * Second middle is position five.
         */
        ListNode eight = build(1, 2, 3, 4, 5, 6, 7, 8);

        assert solver.middleNode(eight).val == 5
                : "Second middle should be returned for even length.";

        /*
         * Edge Case
         *
         * Duplicate values.
         * Position matters, not uniqueness.
         */
        ListNode duplicates = build(5, 5, 5, 5, 5);

        assert solver.middleNode(duplicates).val == 5
                : "Algorithm depends on position, not value uniqueness.";

        /*
         * Edge Case
         *
         * Verify returned node identity,
         * not merely its value.
         */
        ListNode identity = build(1, 2, 3, 4, 5);

        ListNode expected = identity.next.next;

        assert solver.middleNode(identity) == expected
                : "Returned object should be the actual middle node.";

        /*
         * Larger representative case.
         */
        ListNode larger = build(
                1, 2, 3, 4, 5,
                6, 7, 8, 9, 10,
                11
        );

        assert solver.middleNode(larger).val == 6
                : "Middle of eleven nodes should be six.";

        System.out.println("All assertions passed.");
    }

    /*
     * ============================================================
     * Utility Methods
     * ============================================================
     */

    static ListNode build(int... values) {

        if (values == null || values.length == 0) {
            return null;
        }

        ListNode head = new ListNode(values[0]);
        ListNode tail = head;

        for (int i = 1; i < values.length; i++) {
            tail.next = new ListNode(values[i]);
            tail = tail.next;
        }

        return head;
    }

    static List<Integer> toList(ListNode head) {

        List<Integer> result = new ArrayList<>();

        while (head != null) {
            result.add(head.val);
            head = head.next;
        }

        return result;
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/