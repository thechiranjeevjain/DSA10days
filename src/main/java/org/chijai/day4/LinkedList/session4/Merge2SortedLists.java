package org.chijai.day4.LinkedList.session4;

//public class Merge2SortedLists {
//}

import java.util.*;

/**
 * ============================================================================
 * Merge Two Sorted Lists
 * ============================================================================
 *
 * LeetCode:
 * https://leetcode.com/problems/merge-two-sorted-lists/
 *
 * Difficulty:
 * Easy
 *
 * Tags:
 * Linked List
 * Two Pointers
 * Recursion
 * Iterative Construction
 *
 * ============================================================================
 * PROBLEM
 * ============================================================================
 *
 * You are given the heads of two sorted linked lists.
 *
 * Merge them into one sorted linked list by reusing the existing nodes
 * (do NOT create new nodes except an optional dummy node).
 *
 * Return the head of the merged list.
 *
 * Examples
 * --------
 *
 * list1 = [1,2,4]
 * list2 = [1,3,4]
 *
 * Output:
 * [1,1,2,3,4,4]
 *
 * ------------------------
 *
 * list1 = []
 * list2 = []
 *
 * Output:
 * []
 *
 * ------------------------
 *
 * list1 = []
 * list2 = [0]
 *
 * Output:
 * [0]
 *
 * Constraints
 * -----------
 *
 * Number of nodes:
 * 0 ... 50
 *
 * Node values:
 * -100 ... 100
 *
 * Both lists are already sorted in non-decreasing order.
 *
 * ============================================================================
 * PRIMARY TAKEAWAY
 * ============================================================================
 *
 * This is NOT a sorting problem.
 *
 * The lists are already sorted.
 *
 * The only job is:
 *
 * "Always connect the smaller current node."
 *
 * Think of it exactly like the merge phase of Merge Sort.
 *
 * ============================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern Name
 * ------------
 * Two Sorted Stream Merge
 *
 * Problem Archetype
 * -----------------
 * Two already-sorted sequences
 * +
 * One output sequence
 *
 * Core Invariant
 * --------------
 * The merged list built so far is always completely sorted.
 *
 * Why It Works
 * ------------
 * Since each list is individually sorted,
 * whichever current node is smaller must be the next globally smallest node.
 *
 * Therefore choosing anything else can never lead to a valid sorted merge.
 *
 * When To Use
 * -----------
 * ✔ Merge two sorted linked lists
 * ✔ Merge two sorted arrays
 * ✔ Merge step of Merge Sort
 * ✔ Merge K Lists (building block)
 *
 * Recognition Signals
 * -------------------
 * • Two sorted inputs
 * • Need one sorted output
 * • No reordering inside individual lists
 * • Sequential processing
 *
 * Similar Patterns
 * ----------------
 *
 * Merge vs Two Sum
 *
 * Two Sum
 * --------
 * Goal:
 * Find relationship.
 *
 * Merge
 * -----
 * Goal:
 * Produce ordered output.
 *
 * -----------------------------
 *
 * Merge vs Fast-Slow Pointer
 *
 * Fast-Slow:
 * One list
 *
 * Merge:
 * Two lists
 *
 * -----------------------------
 *
 * Merge vs Heap Merge
 *
 * Two Lists
 * ----------
 * Two pointers.
 *
 * K Lists
 * --------
 * Min Heap.
 *
 * ============================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * Imagine two conveyor belts.
 *
 * Belt A:
 *
 * 1 -> 4 -> 8
 *
 * Belt B:
 *
 * 2 -> 3 -> 7
 *
 * You only see the front package of each belt.
 *
 * Since each belt is sorted,
 * the smaller visible package MUST be globally smallest.
 *
 * Remove it.
 *
 * Repeat.
 *
 * Eventually one belt finishes.
 *
 * Append the remaining belt.
 *
 * Nothing else is required.
 *
 * --------------------------------------------------------------------------
 * Variables
 * --------------------------------------------------------------------------
 *
 * l1
 * ----
 * Current head of first remaining list.
 *
 * l2
 * ----
 * Current head of second remaining list.
 *
 * dummy
 * -----
 * Permanent fake node before answer.
 *
 * Never moves.
 *
 * tail
 * ----
 * Last node already merged.
 *
 * Always moves forward.
 *
 * --------------------------------------------------------------------------
 * Invariants
 * --------------------------------------------------------------------------
 *
 * Invariant 1
 * -----------
 * dummy.next always points to beginning of merged list.
 *
 * Invariant 2
 * -----------
 * tail always points to LAST merged node.
 *
 * Invariant 3
 * -----------
 * Everything before tail is already final.
 *
 * Never revisit.
 *
 * Invariant 4
 * -----------
 * l1 and l2 always point to first unmerged nodes.
 *
 * Invariant 5
 * -----------
 * The merged prefix is fully sorted.
 *
 * Invariant 6
 * -----------
 * Every original node appears exactly once.
 *
 * No duplication.
 * No loss.
 *
 * --------------------------------------------------------------------------
 * Allowed Moves
 * --------------------------------------------------------------------------
 *
 * ✔ Compare l1.val and l2.val
 *
 * ✔ Connect smaller node
 *
 * ✔ Advance pointer
 *
 * ✔ Move tail
 *
 * --------------------------------------------------------------------------
 * Forbidden Moves
 * --------------------------------------------------------------------------
 *
 * ✘ Skip smaller node
 *
 * ✘ Break next pointers incorrectly
 *
 * ✘ Lose reference to remaining nodes
 *
 * ✘ Move tail before attaching
 *
 * --------------------------------------------------------------------------
 * Termination Logic
 * --------------------------------------------------------------------------
 *
 * Stop when either list becomes empty.
 *
 * Since the other list is already sorted,
 * attach it entirely.
 *
 * --------------------------------------------------------------------------
 * Why Naive Thinking Fails
 * --------------------------------------------------------------------------
 *
 * A beginner often thinks:
 *
 * "Maybe compare several nodes first."
 *
 * Unnecessary.
 *
 * Because sorted order already guarantees
 * the front smaller node is globally correct.
 *
 * ============================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================================
 *
 * Wrong Idea 1
 * ------------
 * Copy all values into an array.
 *
 * Why it seems okay
 * -----------------
 * Easy to sort.
 *
 * Why it is inferior
 * ------------------
 * Wastes extra memory.
 * Ignores linked list structure.
 *
 * --------------------------------------------------------------------------
 *
 * Wrong Idea 2
 * ------------
 * Always attach from one list until value becomes larger.
 *
 * Counterexample
 *
 * l1:
 * 1 100
 *
 * l2:
 * 2 3 4
 *
 * Output becomes
 *
 * 1 100 2 3 4
 *
 * Sorted invariant broken.
 *
 * --------------------------------------------------------------------------
 *
 * Wrong Idea 3
 * ------------
 * Forget to move tail.
 *
 * Result
 * ------
 * Repeated overwriting.
 *
 * List corruption.
 *
 * --------------------------------------------------------------------------
 *
 * Wrong Idea 4
 * ------------
 * Forget final append.
 *
 * Example
 *
 * 1 2
 *
 * and
 *
 * 100 200
 *
 * Output
 *
 * 1 2
 *
 * Remaining nodes disappear.
 *
 * Invariant Violated
 * ------------------
 * Every node must appear exactly once.
 *
 * ============================================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================================
 *
 * Mechanical typing order
 *
 * Step 1
 * ------
 * Create function.
 *
 * Step 2
 * ------
 * Handle trivial recursive base cases
 * (recursive solution only).
 *
 * Step 3
 * ------
 * Create dummy node.
 *
 * Step 4
 * ------
 * tail = dummy
 *
 * Step 5
 * ------
 * while both lists exist
 *
 * Step 6
 * ------
 * Compare current values.
 *
 * Step 7
 * ------
 * Connect smaller node.
 *
 * Step 8
 * ------
 * Advance chosen pointer.
 *
 * Step 9
 * ------
 * Move tail.
 *
 * Step 10
 * -------
 * Attach remaining list.
 *
 * Step 11
 * -------
 * Return dummy.next.
 *
 * ============================================================================
 * ULTRA-COMPACT PSEUDOCODE
 * ============================================================================
 *
 * dummy
 * tail
 *
 * while both exist
 *
 *      choose smaller
 *      attach
 *      advance chosen
 *      move tail
 *
 * attach remaining
 *
 * return dummy.next
 *
 * ============================================================================
 * SOLUTION 1
 * Brute Force
 * ============================================================================
 *
 * Core Idea
 * ---------
 * Copy values into array.
 *
 * Sort array.
 *
 * Build new linked list.
 *
 * Invariant
 * ---------
 * Array stays sortable.
 *
 * Limitation
 * ----------
 * Doesn't reuse existing nodes.
 *
 * Extra memory.
 *
 * Time
 * ----
 * O((m+n) log(m+n))
 *
 * Space
 * -----
 * O(m+n)
 *
 * Interview Preference
 * --------------------
 * Rarely preferred.
 */
public class Merge2SortedLists {

    static class ListNode {

        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static class BruteForceSolution {

        /**
         * Copy values.
         * Sort values.
         * Build entirely new list.
         */
        public ListNode mergeTwoLists(ListNode l1, ListNode l2) {

            List<Integer> values = new ArrayList<>();

            while (l1 != null) {
                values.add(l1.val);
                l1 = l1.next;
            }

            while (l2 != null) {
                values.add(l2.val);
                l2 = l2.next;
            }

            Collections.sort(values);

            ListNode dummy = new ListNode();
            ListNode tail = dummy;

            for (int value : values) {
                tail.next = new ListNode(value);
                tail = tail.next;
            }

            return dummy.next;
        }
    }

    /**
     * =========================================================================
     * SOLUTION 2
     * Recursive
     * =========================================================================
     *
     * Core Idea
     * ---------
     * Let recursion merge the remaining suffix.
     *
     * Current frame only decides
     * which node becomes the next answer.
     *
     * Invariant
     * ---------
     * merge(a,b)
     * always returns correctly merged list
     * starting from a and b.
     *
     * Limitation Fixed
     * ----------------
     * No extra sorting.
     *
     * Remaining Limitation
     * --------------------
     * Deep recursion may overflow stack.
     *
     * Think of extremely deep file systems
     * or production linked lists.
     *
     * Time
     * ----
     * O(m+n)
     *
     * Space
     * -----
     * O(m+n)
     * recursion stack
     *
     * Interview Preference
     * --------------------
     * Elegant.
     *
     * Mention stack overflow concern.
     */
    static class RecursiveSolution {

        public ListNode mergeTwoLists(ListNode l1, ListNode l2) {

            // Empty first list.
            if (l1 == null) {
                return l2;
            }

            // Empty second list.
            if (l2 == null) {
                return l1;
            }

            // Smaller node must appear next.
            if (l1.val <= l2.val) {

                // Remaining merge starts after current node.
                l1.next = mergeTwoLists(l1.next, l2);

                return l1;
            }

            // Otherwise second node is smaller.
            l2.next = mergeTwoLists(l1, l2.next);

            return l2;
        }
    }

    /**
     * =========================================================================
     * SOLUTION 3
     * Optimal (Interview Preferred)
     * =========================================================================
     *
     * Core Idea
     * ---------
     *
     * Build answer from left to right.
     *
     * Never create unnecessary nodes.
     *
     * Reuse every existing node.
     *
     * Core Invariant
     * --------------
     *
     * Before every iteration:
     *
     * 1.
     * Everything before tail is completely merged.
     *
     * 2.
     * l1 and l2 are the only remaining candidates.
     *
     * 3.
     * The answer remains sorted.
     *
     * Limitation Fixed
     * ----------------
     *
     * Eliminates recursion stack.
     *
     * Safer in production systems.
     *
     * Time
     * ----
     * O(m+n)
     *
     * Space
     * -----
     * O(1)
     *
     * Interview Preference
     * --------------------
     *
     * Best solution.
     */
    static class OptimalSolution {

        public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
            // Dummy node avoids handling a special "first node" case.
            ListNode dummy = new ListNode();

            // Invariant: tail always points to the last node of the merged list.
            ListNode tail = dummy;

            // Continue while both lists still have candidates.
            while (l1 != null && l2 != null) {

                // Invariant: the smaller front node must be globally smallest.
                if (l1.val <= l2.val) {

                    // Attach current node from first list.
                    tail.next = l1;

                    // Advance first list.
                    l1 = l1.next;

                } else {

                    // Attach current node from second list.
                    tail.next = l2;

                    // Advance second list.
                    l2 = l2.next;
                }

                // The merged suffix now grows by one node.
                tail = tail.next;
            }

            // One list is exhausted.
            // The remaining list is already sorted,
            // so it can be attached as-is.
            if (l1 != null) {
                tail.next = l1;
            }

            if (l2 != null) {
                tail.next = l2;
            }

            // Skip dummy node.
            return dummy.next;
        }
    }

    /**
     * =========================================================================
     * 🟣 INTERVIEW ARTICULATION (NO CODE)
     * =========================================================================
     *
     * If the interviewer asks:
     *
     * "Why is this correct?"
     *
     * A strong answer:
     *
     * ---------------------------------------------------------
     *
     * Both lists are already individually sorted.
     *
     * Therefore the smaller front node between the two lists
     * must be the globally smallest remaining node.
     *
     * Once I append that node,
     * it never needs reconsideration.
     *
     * I repeat this until one list finishes,
     * then append the remaining list because it is already sorted.
     *
     * ---------------------------------------------------------
     *
     * Why can we safely discard one node?
     *
     * Because after selecting the smaller front node,
     * no unseen node can legally appear before it.
     *
     * ---------------------------------------------------------
     *
     * Correctness Guarantee
     *
     * Every iteration:
     *
     * • exactly one node is consumed
     * • merged prefix stays sorted
     * • no node is lost
     * • no node is duplicated
     *
     * Therefore induction proves correctness.
     *
     * ---------------------------------------------------------
     *
     * What breaks if we attach the larger node?
     *
     * Example
     *
     * l1
     * 1 -> 100
     *
     * l2
     * 2 -> 3
     *
     * Choosing 2 first produces
     *
     * 2...
     *
     * But 1 should have appeared first.
     *
     * Sorted invariant immediately fails.
     *
     * ---------------------------------------------------------
     *
     * In-place?
     *
     * Yes.
     *
     * Existing nodes are reused.
     *
     * Only one dummy node is optional.
     *
     * ---------------------------------------------------------
     *
     * Streaming?
     *
     * Yes.
     *
     * As long as both streams remain sorted,
     * this algorithm naturally extends.
     *
     * ---------------------------------------------------------
     *
     * When NOT to use this pattern?
     *
     * If either input is not sorted.
     *
     * Then local comparisons no longer imply global correctness.
     *
     * =========================================================================
     * 🎯 INTERVIEW RECALL SHEET (30 Seconds)
     * =========================================================================
     *
     * Pattern Trigger
     * ---------------
     * Two sorted lists.
     *
     * Core Invariant
     * --------------
     * Prefix already merged is final.
     *
     * Search Target
     * -------------
     * Next globally smallest node.
     *
     * Selection Rule
     * --------------
     * Pick smaller front node.
     *
     * Pointer Rule
     * ------------
     * Move only the pointer that supplied the node.
     *
     * Tail Rule
     * ---------
     * Move tail after attaching.
     *
     * Finish Rule
     * -----------
     * Append remaining list.
     *
     * Common Trap
     * -----------
     * Forget final append.
     *
     * Edge Cases
     * ----------
     * Empty list
     * Both empty
     * Equal values
     * Negative values
     * One element
     *
     * Interview One-Liner
     * -------------------
     * Since both inputs are sorted,
     * the smaller visible node is always globally smallest.
     *
     * Re-Derivation Cue
     * -----------------
     * Merge Sort's merge phase.
     *
     * =========================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =========================================================================
     *
     * Variation 1
     * -----------
     * Descending Lists
     *
     * Change comparison:
     *
     * >
     *
     * instead of
     *
     * <
     *
     * Invariant preserved.
     *
     * ---------------------------------------------------------
     *
     * Variation 2
     * -----------
     * Merge K Sorted Lists
     *
     * Two pointers no longer sufficient.
     *
     * Replace with Min Heap.
     *
     * Pattern evolves.
     *
     * ---------------------------------------------------------
     *
     * Variation 3
     * -----------
     * Merge Arrays
     *
     * Identical invariant.
     *
     * Different storage.
     *
     * ---------------------------------------------------------
     *
     * Variation 4
     * -----------
     * Stable Merge
     *
     * Keep
     *
     * <=
     *
     * instead of
     *
     * <
     *
     * Equal values preserve original ordering.
     *
     * ---------------------------------------------------------
     *
     * Variation 5
     * -----------
     * Build Completely New List
     *
     * Allocate new nodes.
     *
     * Easier conceptually.
     *
     * Uses O(n) extra memory.
     *
     * ---------------------------------------------------------
     *
     * Pattern Break Signals
     * ---------------------
     *
     * Input not sorted
     *
     * Random insertions
     *
     * Arbitrary node deletions
     *
     * Need global optimization
     *
     * Local comparison no longer sufficient.
     *
     * =========================================================================
     * 🧠 MASTERY CHECKLIST
     * =========================================================================
     *
     * □ I know the invariant.
     *
     * □ I know why the smaller node must be chosen.
     *
     * □ I know why choosing the larger node fails.
     *
     * □ I know why tail never moves backward.
     *
     * □ I know why dummy simplifies implementation.
     *
     * □ I know why the remaining list can be appended.
     *
     * □ I can derive recursion from the same invariant.
     *
     * □ I know why iteration is preferred in production.
     *
     * □ I can debug pointer mistakes.
     *
     * □ I can extend this to Merge K Lists.
     *
     * =========================================================================
     * Utility Helpers
     * =========================================================================
     */

    static ListNode build(int... values) {

        ListNode dummy = new ListNode();
        ListNode tail = dummy;

        for (int value : values) {
            tail.next = new ListNode(value);
            tail = tail.next;
        }

        return dummy.next;
    }

    static int[] toArray(ListNode head) {

        List<Integer> answer = new ArrayList<>();

        while (head != null) {
            answer.add(head.val);
            head = head.next;
        }

        int[] result = new int[answer.size()];

        for (int i = 0; i < answer.size(); i++) {
            result[i] = answer.get(i);
        }

        return result;
    }

    static void assertListEquals(String name,
                                 int[] expected,
                                 ListNode actual) {

        int[] actualArray = toArray(actual);

        if (!Arrays.equals(expected, actualArray)) {
            throw new AssertionError(
                    name +
                            "\nExpected : " + Arrays.toString(expected) +
                            "\nActual   : " + Arrays.toString(actualArray));
        }
    }

    static void runTest(String name,
                        int[] expected,
                        ListNode l1,
                        ListNode l2) {

        OptimalSolution solution = new OptimalSolution();

        ListNode merged = solution.mergeTwoLists(l1, l2);

        assertListEquals(name, expected, merged);

        System.out.println("PASS : " + name);
    }

    public static void main(String[] args) {

        // Happy path.
        runTest(
                "Typical Merge",
                new int[]{1, 1, 2, 3, 4, 4},
                build(1, 2, 4),
                build(1, 3, 4)
        );

        // Both lists empty.
        runTest(
                "Both Empty",
                new int[]{},
                build(),
                build()
        );

        // First list empty.
        runTest(
                "First Empty",
                new int[]{0},
                build(),
                build(0)
        );

        // Second list empty.
        runTest(
                "Second Empty",
                new int[]{1, 2, 3},
                build(1, 2, 3),
                build()
        );

        // Equal values.
        // Verifies stable handling of duplicates using <=.
        runTest(
                "Duplicate Values",
                new int[]{1, 1, 1, 1},
                build(1, 1),
                build(1, 1)
        );

        // Negative numbers.
        // Ensures ordering is based on value, not sign.
        runTest(
                "Negative Values",
                new int[]{-5, -4, -2, 0, 3},
                build(-5, -2, 3),
                build(-4, 0)
        );

        // Interleaving merge.
        // Exercises repeated switching between lists.
        runTest(
                "Alternating Merge",
                new int[]{1, 2, 3, 4, 5, 6},
                build(1, 3, 5),
                build(2, 4, 6)
        );

        // One list entirely smaller.
        // Final append should attach the entire second list.
        runTest(
                "Append Remaining Right",
                new int[]{1, 2, 3, 10, 11},
                build(1, 2, 3),
                build(10, 11)
        );

        // One list entirely larger.
        // Final append should attach the remainder of the first list.
        runTest(
                "Append Remaining Left",
                new int[]{1, 2, 10, 11},
                build(10, 11),
                build(1, 2)
        );

        // Single-element lists.
        runTest(
                "Single Elements",
                new int[]{1, 2},
                build(1),
                build(2)
        );

        // Boundary values from constraints.
        runTest(
                "Constraint Boundaries",
                new int[]{-100, -100, 100, 100},
                build(-100, 100),
                build(-100, 100)
        );

        // Recursive solution sanity check.
        RecursiveSolution recursive = new RecursiveSolution();

        assertListEquals(
                "Recursive Solution",
                new int[]{1, 1, 2, 3, 4, 4},
                recursive.mergeTwoLists(
                        build(1, 2, 4),
                        build(1, 3, 4)
                )
        );

        // Brute force solution sanity check.
        BruteForceSolution brute = new BruteForceSolution();

        assertListEquals(
                "Brute Force Solution",
                new int[]{1, 1, 2, 3, 4, 4},
                brute.mergeTwoLists(
                        build(1, 2, 4),
                        build(1, 3, 4)
                )
        );

        System.out.println();
        System.out.println("=================================================");
        System.out.println("All self-verifying tests passed.");
        System.out.println("=================================================");
        System.out.println();

        System.out.println("FINAL CLOSURE");
        System.out.println("----------------------------");
        System.out.println("I understand the invariant.");
        System.out.println("I can re-derive the solution.");
        System.out.println("I can physically reconstruct the implementation under pressure.");
        System.out.println("This chapter is complete.");
    }
}