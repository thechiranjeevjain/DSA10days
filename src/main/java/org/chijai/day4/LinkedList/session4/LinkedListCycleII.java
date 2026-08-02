package org.chijai.day4.LinkedList.session4;


import java.util.Objects;

public class LinkedListCycleII {

/*
 * ============================================================
 * 📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Title:
 * Linked List Cycle II
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Linked List
 * Two Pointers
 * Floyd's Cycle Detection
 * Fast & Slow Pointer
 * Mathematics
 *
 * Problem:
 *
 * Given the head of a singly linked list, determine whether
 * the linked list contains a cycle.
 *
 * If a cycle exists, return the node where the cycle begins.
 *
 * If no cycle exists, return null.
 *
 * You must solve it without modifying the linked list.
 *
 * Extra Space:
 * O(1)
 *
 * ------------------------------------------------------------
 * Constraints
 * ------------------------------------------------------------
 *
 * Number of nodes:
 * 0 <= n <= 10^4
 *
 * Node values:
 * -10^5 <= val <= 10^5
 *
 * pos is used only for building the test and is NOT passed.
 *
 * ------------------------------------------------------------
 * Representative Example 1
 * ------------------------------------------------------------
 *
 * 3 -> 2 -> 0 -> -4
 *      ^         |
 *      |_________|
 *
 * Output:
 * node with value 2
 *
 * ------------------------------------------------------------
 * Example 2
 * ------------------------------------------------------------
 *
 * 1 -> 2
 * ^    |
 * |____|
 *
 * Output:
 * node with value 1
 *
 * ------------------------------------------------------------
 * Example 3
 * ------------------------------------------------------------
 *
 * 1 -> null
 *
 * Output:
 * null
 *
 * ------------------------------------------------------------
 * LeetCode
 * ------------------------------------------------------------
 *
 * https://leetcode.com/problems/linked-list-cycle-ii/
 *
 */

/*
 * ============================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern
 * --------
 * Floyd's Tortoise and Hare
 *
 * Archetype
 * ---------
 * Two pointers moving with different speeds.
 *
 * Core Invariant
 * --------------
 * If a cycle exists,
 * eventually the faster pointer laps the slower pointer
 * and both occupy exactly the same node.
 *
 * That meeting node is NOT necessarily the cycle entry.
 *
 * However,
 * after the meeting,
 * one pointer restarted from head and the other left at the
 * meeting point will meet exactly at the cycle entry.
 *
 * Why It Works
 * ------------
 * Relative speed inside a cycle guarantees collision.
 *
 * Collision reveals enough distance information to recover
 * the beginning of the loop.
 *
 * Recognition Signals
 * -------------------
 * ✓ singly linked list
 * ✓ no random access
 * ✓ O(1) extra memory
 * ✓ detect loop
 * ✓ locate loop start
 *
 * When To Use
 * -----------
 * - cycle detection
 * - duplicate number
 * - functional graph
 * - repeated state transitions
 *
 * When NOT To Use
 * ---------------
 * - arbitrary graph cycles
 * - weighted graph cycles
 * - tree traversal
 * - multiple outgoing edges
 *
 * Comparison
 * ----------
 *
 * HashSet
 * --------
 * Time : O(n)
 * Space: O(n)
 * Easier.
 *
 * Floyd
 * ------
 * Time : O(n)
 * Space: O(1)
 * Interview preferred.
 */

/*
 * ============================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Imagine a circular running track.
 *
 * One runner moves one step.
 *
 * Another moves two steps.
 *
 * Eventually,
 * the faster runner catches the slower runner.
 *
 * The collision only tells us:
 *
 * "A cycle definitely exists."
 *
 * Surprisingly,
 * it also encodes exactly where the loop begins.
 *
 * ------------------------------------------------------------
 * Mental Model
 * ------------------------------------------------------------
 *
 *                 x1
 *
 * head --------------------> entry
 *                             |
 *                             |
 *                           x2|
 *                             |
 *                        meeting
 *                             |
 *                           x3|
 *                             |
 *                             V
 *                           entry
 *
 * Loop Length = x2 + x3
 *
 * ------------------------------------------------------------
 * State Variables
 * ------------------------------------------------------------
 *
 * slow
 * ----
 * moves one edge
 *
 * fast
 * ----
 * moves two edges
 *
 * entry
 * -----
 * restarted from head after collision
 *
 * intersection
 * ------------
 * collision point inside cycle
 *
 * ------------------------------------------------------------
 * Allowed State Transitions
 * ------------------------------------------------------------
 *
 * Detection Phase
 *
 * slow = slow.next
 *
 * fast = fast.next.next
 *
 * Recovery Phase
 *
 * entry = entry.next
 *
 * intersection = intersection.next
 *
 * ------------------------------------------------------------
 * Forbidden Moves
 * ------------------------------------------------------------
 *
 * ✗ restart both pointers
 *
 * ✗ restart fast instead of one pointer
 *
 * ✗ change pointer speeds
 *
 * ✗ modify list
 *
 * ✗ count nodes first
 *
 * ------------------------------------------------------------
 * Detection Invariant
 * ------------------------------------------------------------
 *
 * Before collision:
 *
 * fast gains exactly
 * one node
 * over slow
 * every iteration
 * inside the cycle.
 *
 * Therefore,
 * the distance between them modulo cycle length
 * decreases deterministically.
 *
 * Hence collision is inevitable.
 *
 * ------------------------------------------------------------
 * Recovery Invariant
 * ------------------------------------------------------------
 *
 * After collision:
 *
 * One pointer starts from head.
 *
 * One pointer starts from meeting point.
 *
 * Both now move at identical speed.
 *
 * Their first meeting MUST be
 * the cycle entry.
 *
 * This is the invariant that drives the second phase.
 *
 * ------------------------------------------------------------
 * Mathematical Proof
 * ------------------------------------------------------------
 *
 * Let
 *
 * x1 = head to cycle entry
 *
 * x2 = entry to collision
 *
 * x3 = collision back to entry
 *
 * Loop Length
 *
 * L = x2 + x3
 *
 * Slow travels
 *
 * x1 + x2
 *
 * Fast travels
 *
 * x1 + x2 + nL
 *
 * where
 *
 * n >= 1
 *
 * because fast may complete multiple full laps before
 * meeting slow.
 *
 * Relative speed:
 *
 * Fast Distance
 * =
 * 2 × Slow Distance
 *
 * Therefore
 *
 * x1 + x2 + nL
 * =
 * 2(x1 + x2)
 *
 * Expand L
 *
 * x1 + x2 + n(x2 + x3)
 * =
 * 2x1 + 2x2
 *
 * Rearranging
 *
 * x1
 * =
 * (n-1)x2
 * +
 * nx3
 *
 * Notice
 *
 * x1 differs from x3 only by whole multiples
 * of the cycle length.
 *
 * Since travelling one complete cycle returns to the
 * same node,
 * moving x1 from head reaches exactly the same node as
 * moving x3 (plus whole cycles) from collision.
 *
 * Therefore
 *
 * head pointer
 * and
 * collision pointer
 *
 * meet precisely at the cycle entry.
 *
 * This proof correctly handles
 * every possible number of extra laps.
 *
 * ------------------------------------------------------------
 * Why Naive Thinking Fails
 * ------------------------------------------------------------
 *
 * Many people incorrectly assume
 * collision itself equals cycle entry.
 *
 * False.
 *
 * Collision location depends on
 * loop size
 * and
 * tail length.
 *
 * Only the SECOND phase restores the invariant
 * needed to discover the entry.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Phase 1
 *
 * Either
 *
 * fast reaches null
 *
 * OR
 *
 * collision occurs.
 *
 * Phase 2
 *
 * Distance between
 * entry pointer
 * and
 * intersection pointer
 *
 * decreases together while preserving equality modulo
 * loop length.
 *
 * They meet exactly once
 * at the entry.
 */

/*
 * ============================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================
 *
 * Mistake 1
 * ---------
 * Return collision node.
 *
 * Why It Looks Correct
 * --------------------
 * Collision proves cycle.
 *
 * Violated Invariant
 * ------------------
 * Collision != Entry.
 *
 * Counterexample
 * --------------
 *
 * 3 -> 2 -> 0 -> -4
 *      ^         |
 *      |_________|
 *
 * Collision is usually -4,
 * not 2.
 *
 * ------------------------------------------------------------
 * Mistake 2
 * ---------
 * Restart fast instead of one pointer.
 *
 * Violated Invariant
 * ------------------
 * Recovery requires equal speeds.
 *
 * ------------------------------------------------------------
 * Mistake 3
 * ---------
 * Move restarted pointer by two.
 *
 * Violated Invariant
 * ------------------
 * Equal-speed convergence disappears.
 *
 * ------------------------------------------------------------
 * Mistake 4
 * ---------
 * Forget fast.next null check.
 *
 * Interview Trap
 * --------------
 * NullPointerException.
 *
 * ------------------------------------------------------------
 * Mistake 5
 * ---------
 * Assume proof only works when fast makes one lap.
 *
 * Reality
 * -------
 * Fast may complete any number of additional laps.
 *
 * The generalized proof
 *
 * x1=(n-1)x2+nx3
 *
 * still guarantees correctness.
 */


    /*
     * ============================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Goal
     * ----
     * Mechanically reconstruct the solution under interview pressure.
     *
     * Think in exactly two phases:
     *
     * Phase 1:
     * Detect whether a cycle exists.
     *
     * Phase 2:
     * Recover the entry of the cycle.
     *
     * Never mix these phases.
     *
     * ------------------------------------------------------------
     * Typing Order
     * ------------------------------------------------------------
     *
     * 1.
     * Function signature.
     *
     * 2.
     * Initialize
     *
     * slow = head
     * fast = head
     *
     * 3.
     * Detection loop
     *
     * while (fast != null && fast.next != null)
     *
     * 4.
     * Advance pointers
     *
     * slow += 1
     * fast += 2
     *
     * 5.
     * Collision?
     *
     * if (slow == fast)
     *
     * 6.
     * Restart entry pointer
     *
     * entry = head
     *
     * 7.
     * Recovery loop
     *
     * while (entry != slow)
     *
     * both move one step
     *
     * 8.
     * Return entry.
     *
     * 9.
     * If loop finishes,
     * return null.
     *
     * ------------------------------------------------------------
     * Mechanical Skeleton
     * ------------------------------------------------------------
     *
     * initialize pointers
     *
     * while (fast can move)
     *
     *      move slow
     *
     *      move fast
     *
     *      if collision
     *
     *           restart entry
     *
     *           while entry != collision
     *
     *                move both
     *
     *           return entry
     *
     * return null
     *
     * ------------------------------------------------------------
     * Debugging Checklist
     * ------------------------------------------------------------
     *
     * If collision never happens:
     *
     * verify
     *
     * fast moves twice.
     *
     * If NullPointerException:
     *
     * verify
     *
     * fast.next checked first.
     *
     * If wrong entry:
     *
     * verify
     *
     * recovery moves both exactly one step.
     *
     * If infinite loop:
     *
     * verify
     *
     * recovery compares references,
     * not values.
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * slow=head
     * fast=head
     *
     * while fast valid
     *
     *      advance
     *
     *      if meet
     *
     *           entry=head
     *
     *           while entry!=meet
     *
     *                both++
     *
     *           return entry
     *
     * return null
     */

    /*
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    /**
     * Basic singly-linked-list node.
     */
    static final class ListNode {

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
     * Brute Force
     * ============================================================
     *
     * Idea
     * ----
     * Remember every visited node inside a HashSet.
     *
     * First repeated node
     * is the cycle entry.
     *
     * Pattern
     * -------
     * Visited State Recording.
     *
     * Invariant
     * ---------
     * Every node appears at most once before entering
     * the cycle.
     *
     * Limitation
     * ----------
     * O(n) memory.
     *
     * Complexity
     * ----------
     * Time  : O(n)
     * Space : O(n)
     *
     * Interview Usefulness
     * --------------------
     * Good starting point.
     * Rarely accepted as optimal follow-up.
     */

    static final class BruteForce {

        ListNode detectCycle(ListNode head) {

            java.util.HashSet<ListNode> visited = new java.util.HashSet<>();

            ListNode current = head;

            while (current != null) {

                if (!visited.add(current)) {
                    return current;
                }

                current = current.next;
            }

            return null;
        }
    }

    /*
     * ============================================================
     * Improved
     * ============================================================
     *
     * Observation
     * -----------
     * The HashSet only stores information already encoded
     * geometrically by pointer speeds.
     *
     * Floyd eliminates the extra memory.
     *
     * Improvement
     * -----------
     * O(n) space
     * →
     * O(1) space.
     *
     * Invariant
     * ---------
     * Relative speed guarantees eventual collision
     * whenever a cycle exists.
     *
     * Complexity
     * ----------
     * Time  : O(n)
     * Space : O(1)
     *
     * Interview Usefulness
     * --------------------
     * This is the mathematical bridge toward the optimal
     * implementation.
     */

    static final class Improved {

        boolean hasCycle(ListNode head) {

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

    /*
     * ============================================================
     * Optimal (Interview Preferred)
     * ============================================================
     *
     * Idea
     * ----
     * Phase 1:
     * Detect collision.
     *
     * Phase 2:
     * Recover entry.
     *
     * Core Invariant
     * --------------
     * See the mathematical proof in the invariant section.
     *
     * Correctness
     * -----------
     * Collision proves a cycle.
     *
     * Equal-speed recovery proves the entry.
     *
     * Complexity
     * ----------
     * Time  : O(n)
     * Space : O(1)
     *
     * Interview Usefulness
     * --------------------
     * Canonical Floyd interview problem.
     */

    static final class Optimal {

        ListNode detectCycle(ListNode head) {

            if (head == null) {
                return null;
            }

            ListNode slow = head;
            ListNode fast = head;

            while (fast != null && fast.next != null) {

                // 🟢 Invariant:
                // slow moves one edge per iteration.
                slow = slow.next;

                // 🟢 Invariant:
                // fast gains exactly one node each iteration
                // inside the cycle.
                fast = fast.next.next;

                if (slow == fast) {

                    // 🔵 Reset only one pointer.
                    // Equal-speed movement now reveals
                    // the cycle entry.
                    ListNode entry = head;

                    while (entry != slow) {

                        // 🟢 Invariant:
                        // Remaining distance to entry
                        // stays identical for both pointers.
                        entry = entry.next;

                        slow = slow.next;
                    }

                    // 🟡 First equal node after restart
                    // must be the cycle entry.
                    return entry;
                }
            }

            // 🔴 Fast escaped the list.
            // Therefore no cycle exists.
            return null;
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * How I would explain this solution verbally:
 *
 * "I use Floyd's two-pointer algorithm.
 *
 * The fast pointer moves twice as quickly as the slow pointer.
 *
 * If there is a cycle,
 * they must eventually collide because the relative speed
 * inside the finite loop is one node per iteration.
 *
 * The collision itself is not the answer.
 *
 * Using the distance equations,
 * the distance from the head to the cycle entry equals
 * the distance from the collision point to the entry
 * modulo complete loop traversals.
 *
 * Therefore,
 * restarting one pointer from the head while leaving the
 * other at the collision point,
 * then moving both one step at a time,
 * guarantees they meet exactly at the cycle entry.
 *
 * This achieves O(n) time and O(1) extra space."
 *
 * In-place Feasibility
 * --------------------
 * Yes.
 *
 * Streaming Feasibility
 * ---------------------
 * No.
 *
 * Future nodes may determine whether
 * the current path eventually cycles.
 *
 * When NOT To Use
 * ---------------
 * General graph cycle detection.
 * Floyd assumes exactly one outgoing edge per state.
 */

/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * 30-Second Recall
 * ----------------
 *
 * Trigger
 * -------
 * Singly linked list
 * +
 * O(1) extra space
 * +
 * Detect cycle start.
 *
 * Pattern
 * -------
 * Floyd's Tortoise and Hare.
 *
 * Search Space
 * ------------
 * Nodes reachable from head.
 *
 * Invariant
 * ---------
 * Fast gains exactly one node on slow per iteration
 * inside the cycle.
 *
 * Collision is inevitable.
 *
 * Recovery Invariant
 * ------------------
 * Head pointer and collision pointer move together.
 *
 * Their first meeting is the cycle entry.
 *
 * Search Target
 * -------------
 * Cycle entry node.
 *
 * Discard Rule
 * ------------
 * If fast reaches null,
 * the remaining search space cannot contain a cycle.
 *
 * Common Trap
 * -----------
 * Returning the collision node.
 *
 * Edge Cases
 * ----------
 * ✓ Empty list
 * ✓ One node without cycle
 * ✓ One node pointing to itself
 * ✓ Two-node cycle
 * ✓ Entire list is one cycle
 * ✓ Long tail before cycle
 *
 * Complexity
 * ----------
 * Time  : O(n)
 * Space : O(1)
 *
 * One-Liner
 * ---------
 * Detect together.
 * Restart one.
 * Walk together.
 * Meet at entry.
 *
 * Re-derivation Cue
 * -----------------
 * Relative speed proves collision.
 *
 * Distance equation proves restart.
 */

/*
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * ------------------------------------------------------------
 * Variation 1
 * ------------------------------------------------------------
 * Detect cycle only.
 *
 * Change
 * ------
 * Stop immediately after collision.
 *
 * Invariant
 * ---------
 * Detection invariant only.
 *
 * Recovery phase unnecessary.
 *
 * ------------------------------------------------------------
 * Variation 2
 * ------------------------------------------------------------
 * Compute cycle length.
 *
 * Change
 * ------
 * After collision,
 * keep one pointer fixed.
 *
 * Walk the other pointer until it returns.
 *
 * Count steps.
 *
 * Invariant
 * ---------
 * One complete traversal equals loop length.
 *
 * ------------------------------------------------------------
 * Variation 3
 * ------------------------------------------------------------
 * Find middle node.
 *
 * Pattern
 * -------
 * Same fast/slow movement.
 *
 * Difference
 * ----------
 * No cycle assumption.
 *
 * Termination occurs when fast reaches null.
 *
 * ------------------------------------------------------------
 * Variation 4
 * ------------------------------------------------------------
 * Happy Number
 *
 * Pattern
 * -------
 * Floyd on state transitions.
 *
 * State
 * -----
 * Integer.
 *
 * Transition
 * ----------
 * Sum of squared digits.
 *
 * Invariant
 * ---------
 * Functional graph.
 *
 * ------------------------------------------------------------
 * Variation 5
 * ------------------------------------------------------------
 * Find Duplicate Number
 *
 * Pattern
 * -------
 * Floyd on implicit graph.
 *
 * State
 * -----
 * Array index.
 *
 * Transition
 * ----------
 * nums[index]
 *
 * Same mathematics.
 *
 * ------------------------------------------------------------
 * Variation 6
 * ------------------------------------------------------------
 * General graph cycle detection.
 *
 * Pattern Break
 * -------------
 * Floyd no longer applies.
 *
 * Why
 * ---
 * Multiple outgoing edges violate the
 * functional-graph invariant.
 *
 * Required Pattern
 * ----------------
 * DFS
 * Topological Sort
 * Union-Find
 * depending on graph type.
 */

/*
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * Can you answer these without notes?
 *
 * □ What is the Pattern?
 *
 * Floyd's Tortoise and Hare.
 *
 * □ What is the Search Space?
 *
 * Reachable nodes.
 *
 * □ What is the Detection Invariant?
 *
 * Fast gains one node every iteration
 * inside the cycle.
 *
 * □ Why must collision happen?
 *
 * Finite modulo arithmetic.
 *
 * □ What mathematical equation is used?
 *
 * Fast Distance
 * =
 * 2 × Slow Distance.
 *
 * □ Why is
 *
 * x1=(n-1)x2+nx3
 *
 * stronger than
 *
 * x1=x3 ?
 *
 * Because it correctly handles
 * multiple complete laps.
 *
 * □ Why restart exactly one pointer?
 *
 * Restores equal-speed convergence.
 *
 * □ Why move both one step?
 *
 * Recovery invariant requires equal speeds.
 *
 * □ Why compare references instead of values?
 *
 * Node values may repeat.
 *
 * Identity matters.
 *
 * □ When does the algorithm terminate?
 *
 * Fast reaches null
 * OR
 * recovery pointers meet.
 *
 * □ Why does the brute-force solution work?
 *
 * First repeated node is the cycle entry.
 *
 * □ Why is Floyd preferable?
 *
 * Same asymptotic time.
 *
 * Constant extra memory.
 *
 * □ Can you derive the implementation
 * without memorizing code?
 *
 * Detection
 * →
 * Collision
 * →
 * Restart
 * →
 * Equal-speed walk
 * →
 * Entry.
 *
 * □ Pattern Boundary
 *
 * Exactly one outgoing edge per state.
 *
 * Otherwise Floyd is invalid.
 */

/*
 * ============================================================
 * ⚫ PATTERN MAPPING
 * ============================================================
 *
 * Pattern Family
 * --------------
 * Two Pointers
 *
 * Specialized Pattern
 * -------------------
 * Fast & Slow Pointer
 *
 * Mathematical Tool
 * -----------------
 * Modular Arithmetic
 *
 * Search Space
 * ------------
 * Functional Graph
 *
 * Typical Problems
 * ----------------
 * ✓ Linked List Cycle
 * ✓ Linked List Cycle II
 * ✓ Happy Number
 * ✓ Find the Duplicate Number
 * ✓ Middle of Linked List (speed differential only)
 *
 * Distinguishing Feature
 * ----------------------
 * Every state has exactly one deterministic successor.
 */

/*
 * ============================================================
 * ⚫ DEBUGGING PLAYBOOK
 * ============================================================
 *
 * Symptom
 * -------
 * NullPointerException
 *
 * Verify
 * ------
 * fast != null
 * &&
 * fast.next != null
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Infinite recovery loop
 *
 * Verify
 * ------
 * Both pointers move exactly one step.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Wrong returned node
 *
 * Verify
 * ------
 * Restart pointer begins from head,
 * not head.next.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * False positive
 *
 * Verify
 * ------
 * Comparison uses
 *
 * ==
 *
 * rather than node value.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 * Collision never occurs
 *
 * Verify
 * ------
 * Fast advances two edges every iteration.
 */

/*
 * ============================================================
 * ⚫ IMPLEMENTATION RECONSTRUCTION
 * ============================================================
 *
 * Under interview pressure,
 * remember only these six physical actions:
 *
 * 1.
 * slow=head
 * fast=head
 *
 * 2.
 * While fast can move
 *
 * 3.
 * slow +=1
 * fast +=2
 *
 * 4.
 * Collision?
 *
 * 5.
 * entry=head
 *
 * 6.
 * Walk together until equal.
 *
 * Everything else follows naturally.
 */


    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    private static ListNode connectCycle(ListNode[] nodes, int entryIndex) {

        Objects.requireNonNull(nodes);

        if (nodes.length == 0) {
            return null;
        }

        for (int i = 0; i < nodes.length - 1; i++) {
            nodes[i].next = nodes[i + 1];
        }

        if (entryIndex >= 0) {
            nodes[nodes.length - 1].next = nodes[entryIndex];
        }

        return nodes[0];
    }

    private static ListNode[] createNodes(int... values) {

        ListNode[] nodes = new ListNode[values.length];

        for (int i = 0; i < values.length; i++) {
            nodes[i] = new ListNode(values[i]);
        }

        return nodes;
    }

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        /*
         * Happy Path
         *
         * 3 -> 2 -> 0 -> -4
         *      ^         |
         *      |_________|
         */
        {
            ListNode[] nodes = createNodes(3, 2, 0, -4);
            ListNode head = connectCycle(nodes, 1);

            assert solver.detectCycle(head) == nodes[1]
                    : "Cycle entry should be node with value 2.";
        }

        /*
         * Entire list is a cycle.
         */
        {
            ListNode[] nodes = createNodes(1, 2, 3, 4);
            ListNode head = connectCycle(nodes, 0);

            assert solver.detectCycle(head) == nodes[0]
                    : "Entry should be head.";
        }

        /*
         * Two-node cycle.
         */
        {
            ListNode[] nodes = createNodes(10, 20);
            ListNode head = connectCycle(nodes, 0);

            assert solver.detectCycle(head) == nodes[0]
                    : "Head is cycle entry.";
        }

        /*
         * Self-cycle.
         */
        {
            ListNode node = new ListNode(42);
            node.next = node;

            assert solver.detectCycle(node) == node
                    : "Single node pointing to itself.";
        }

        /*
         * Long tail before cycle.
         */
        {
            ListNode[] nodes = createNodes(1, 2, 3, 4, 5, 6, 7, 8, 9);
            ListNode head = connectCycle(nodes, 5);

            assert solver.detectCycle(head) == nodes[5]
                    : "Must correctly recover distant entry.";
        }

        /*
         * No cycle.
         */
        {
            ListNode[] nodes = createNodes(1, 2, 3, 4);
            ListNode head = connectCycle(nodes, -1);

            assert solver.detectCycle(head) == null
                    : "Acyclic list should return null.";
        }

        /*
         * Empty list.
         */
        {
            assert solver.detectCycle(null) == null
                    : "Null head should return null.";
        }

        /*
         * One node without cycle.
         */
        {
            ListNode node = new ListNode(100);

            assert solver.detectCycle(node) == null
                    : "Single isolated node has no cycle.";
        }

        /*
         * Duplicate values.
         *
         * Ensures reference equality is used instead
         * of value equality.
         */
        {
            ListNode[] nodes = createNodes(5, 5, 5, 5, 5);
            ListNode head = connectCycle(nodes, 2);

            assert solver.detectCycle(head) == nodes[2]
                    : "Reference identity must determine entry.";
        }

        /*
         * Long acyclic list.
         */
        {
            ListNode[] nodes = new ListNode[1000];

            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = new ListNode(i);
            }

            ListNode head = connectCycle(nodes, -1);

            assert solver.detectCycle(head) == null
                    : "Large acyclic input.";
        }

        /*
         * Cycle beginning near the end.
         */
        {
            ListNode[] nodes = createNodes(1, 2, 3, 4, 5, 6, 7, 8);
            ListNode head = connectCycle(nodes, 6);

            assert solver.detectCycle(head) == nodes[6]
                    : "Late-entry cycle.";
        }

        /*
         * Brute-force correctness cross-check.
         */
        {
            BruteForce brute = new BruteForce();

            ListNode[] nodes = createNodes(9, 8, 7, 6, 5);
            ListNode head = connectCycle(nodes, 3);

            assert brute.detectCycle(head) == solver.detectCycle(head)
                    : "Brute-force and optimal should agree.";
        }

        /*
         * Improved detection sanity check.
         */
        {
            Improved improved = new Improved();

            ListNode[] cyclic = createNodes(1, 2, 3);
            connectCycle(cyclic, 1);

            assert improved.hasCycle(cyclic[0]);

            ListNode[] acyclic = createNodes(4, 5, 6);
            connectCycle(acyclic, -1);

            assert !improved.hasCycle(acyclic[0]);
        }

        System.out.println("All assertions passed.");
    }

    /*
     * ============================================================
     * FINAL RECAP
     * ============================================================
     *
     * Pattern
     * -------
     * Floyd's Tortoise and Hare.
     *
     * Detection
     * ---------
     * Different speeds guarantee collision
     * inside a finite cycle.
     *
     * Recovery
     * --------
     * Restart one pointer at head.
     *
     * Move both pointers one step.
     *
     * Their first meeting is the cycle entry.
     *
     * Mathematical Foundation
     * -----------------------
     *
     * Let
     *
     * x1 = head to entry
     * x2 = entry to collision
     * x3 = collision to entry
     * L  = loop length
     *
     * Fast:
     *
     * x1 + x2 + nL
     *
     * Slow:
     *
     * x1 + x2
     *
     * Since
     *
     * Fast = 2 × Slow
     *
     * we derive
     *
     * x1 = (n - 1)x2 + nx3
     *
     * which proves that the distance from
     * the collision point to the entry
     * differs from the distance from head
     * to the entry only by whole loop traversals.
     *
     * Therefore,
     * equal-speed walking converges exactly
     * at the cycle entry.
     *
     * Complexity
     * ----------
     * Time  : O(n)
     * Space : O(1)
     */

}