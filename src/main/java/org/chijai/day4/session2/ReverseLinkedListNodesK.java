package org.chijai.day4.session2;

// =====================================================================================
// 📘 REVERSE NODES IN K-GROUP — INVARIANT-FIRST ALGORITHM CHAPTER
// FILE: ReverseNodesInKGroup_InvariantChapter.java
// PART 1 / N  (CONTINUATION-LOCKED — DO NOT EDIT / DO NOT REORDER)
// =====================================================================================

public class ReverseLinkedListNodesK {

    // =================================================================================
    // 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT (VERBATIM)
    // =================================================================================
    /*
     * 🔗 Official LeetCode Link:
     * https://leetcode.com/problems/reverse-nodes-in-k-group/
     *
     * 🧩 Difficulty:
     * Hard
     *
     * 🏷️ Tags:
     * Linked List
     * Recursion
     *
     * =========================
     * Problem Description
     * =========================
     *
     * Given the head of a linked list, reverse the nodes of the list k at a time,
     * and return the modified list.
     *
     * k is a positive integer and is less than or equal to the length of the linked list.
     * If the number of nodes is not a multiple of k then left-out nodes, in the end,
     * should remain as it is.
     *
     * You may not alter the values in the list's nodes, only nodes themselves may be changed.
     *
     * =========================
     * Examples
     * =========================
     *
     * Example 1:
     *
     * Input: head = [1,2,3,4,5], k = 2
     * Output: [2,1,4,3,5]
     *
     * Example 2:
     *
     * Input: head = [1,2,3,4,5], k = 3
     * Output: [3,2,1,4,5]
     *
     * =========================
     * Constraints
     * =========================
     *
     * The number of nodes in the list is n.
     * 1 <= k <= n <= 5000
     * 0 <= Node.val <= 1000
     *
     * =========================
     * Follow-up
     * =========================
     *
     * Can you solve the problem in O(1) extra memory space?
     */

    // =================================================================================
    // 3️⃣ 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST · FULL)
    // =================================================================================
    /*
     * 🔵 Pattern Name:
     * Fixed-Window In-Place Linked List Transformation
     *
     * 🔵 Problem Archetype:
     * Repeated local mutation under strict boundary isolation.
     *
     * 🟢 Core Invariant (ONE SENTENCE — MANDATORY):
     * At every step, nodes strictly before the current k-sized window are finalized and
     * correct, nodes strictly after the window are untouched, and only nodes inside the
     * window may be temporarily disconnected.
     *
     * 🔵 Why this invariant makes the pattern work:
     * Linked lists cannot be randomly accessed; correctness depends on preserving
     * connectivity. By isolating mutation to a closed k-node window, we prevent pointer
     * corruption outside the active region.
     *
     * 🔵 When this pattern applies:
     * • Linked list problems
     * • Fixed-size group operations
     * • Partial tail must remain unchanged
     * • In-place constraint
     *
     * 🧭 Pattern Recognition Signals:
     * • “reverse every k nodes”
     * • “leave remaining nodes unchanged”
     * • “do not use extra memory”
     * • “nodes, not values”
     *
     * 🔵 How this pattern differs from similar patterns:
     * • Unlike full reversal, boundaries repeat.
     * • Unlike sliding window, window does not overlap.
     * • Unlike recursion-only solutions, iteration enforces O(1) space.
     */

    // =================================================================================
    // 4️⃣ 🟢 MENTAL MODEL & INVARIANTS (CANONICAL SECTION)
    // =================================================================================
    /*
     * 🧠 Mental Model (HOW TO THINK — NOT CODE):
     *
     * Visualize the list as sealed blocks of size k.
     * Once a block is reversed and reconnected, it is NEVER touched again.
     * The algorithm moves strictly forward.
     *
     * -------------------------
     * 🟢 Invariants (EXPLICIT)
     * -------------------------
     *
     * Invariant 1 — Boundary Safety:
     * The node immediately before the current group (groupPrev) always points
     * to the first node of the group BEFORE reversal.
     *
     * Invariant 2 — Isolation:
     * During reversal, only pointers inside the k-group may change.
     * Pointers outside the group are read-only.
     *
     * Invariant 3 — Tail Preservation:
     * If fewer than k nodes remain, they are NEVER modified.
     *
     * Invariant 4 — Progress:
     * After each group is processed, groupPrev advances exactly k nodes forward.
     *
     * -------------------------
     * 🟢 State Representation
     * -------------------------
     *
     * groupPrev:
     *   The node immediately before the k-sized window.
     *
     * kth:
     *   The last node of the current window.
     *
     * groupNext:
     *   The node immediately after the window.
     *
     * -------------------------
     * 🟢 Allowed Moves
     * -------------------------
     *
     * • Reverse next pointers inside the window.
     * • Reconnect window head and tail after reversal.
     *
     * -------------------------
     * 🔴 Forbidden Moves
     * -------------------------
     *
     * • Reversing before confirming k nodes exist.
     * • Touching nodes beyond kth.
     * • Losing reference to groupNext.
     *
     * -------------------------
     * 🟢 Termination Logic
     * -------------------------
     *
     * The algorithm terminates when kth cannot be found,
     * guaranteeing no partial group reversal.
     *
     * -------------------------
     * 🔴 Why Common Alternatives Fail
     * -------------------------
     *
     * • Streaming reversal breaks boundary isolation.
     * • Recursive without boundary check breaks tail invariant.
     */

    // =================================================================================
    // 5️⃣ 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
    // =================================================================================
    /*
     * ❌ Typical Wrong Approach #1:
     * Reverse nodes while counting up to k.
     *
     * Why it seems correct:
     * • Mirrors full list reversal logic.
     *
     * Exact invariant violated:
     * • Boundary Safety — reversal starts without knowing group end.
     *
     * Minimal Counterexample:
     * head = [1,2,3,4,5], k = 3
     * The last two nodes must remain untouched but get partially reversed.
     *
     * ❌ Typical Wrong Approach #2:
     * Reverse entire list, then reverse back leftover.
     *
     * Why it fails:
     * • Tail invariant is violated transiently.
     *
     * 🎯 Interviewer Trap:
     * They allow the first group to succeed and wait for pointer corruption
     * in the second or third group.
     */

    /*
     * ============================================================
     * 🟣 INTERVIEW ARTICULATION — INVARIANT-FIRST (SPOKEN SCRIPT)
     * ============================================================
     *
     * ✅ What strong candidates say (invariant-first):
     *
     * “The key constraint is that leftover nodes must remain unchanged,
     * so I’m only allowed to reverse when I know k nodes exist.”
     *
     * (This immediately signals boundary-aware thinking.)
     *
     * ------------------------------------------------------------
     * 🎤 The 60-second ideal interview explanation:
     *
     * Say this slowly and calmly:
     *
     * “Before touching pointers, I establish an invariant:
     * everything before my current pointer is finalized,
     * and everything after the current group is untouched.
     *
     * So for each step, I first scan ahead k nodes.
     * If I can’t find k nodes, I stop immediately — that preserves the tail.
     *
     * Once I confirm the boundary, I reverse exactly k nodes in place,
     * reconnect the group to the previous part of the list,
     * and move the boundary forward.
     *
     * Because I only reverse inside verified boundaries and never touch partial groups,
     * the algorithm is correct and runs in O(n) time with O(1) extra space.”
     *
     * Then stop talking.
     *
     * Silence is confidence.
     *
     * ------------------------------------------------------------
     * 🎯 If the interviewer pushes: “Why the lookahead?”
     *
     * Answer:
     *
     * “Because linked lists don’t allow partial rollback.
     * If I start reversing and later discover fewer than k nodes,
     * I’ve already corrupted the list.”
     *
     * (That sentence alone signals senior-level linked list reasoning.)
     *
     * ============================================================
     */


    // =================================================================================
    // 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES (DERIVED FROM INVARIANT)
    // =================================================================================

    // -----------------------------------------------------------------------------
    // 📘 ListNode Definition (Shared)
    // -----------------------------------------------------------------------------
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int value) {
            this.val = value;
        }
    }

    // -----------------------------------------------------------------------------
    // 🟡 SOLUTION 1: BRUTE FORCE
    // -----------------------------------------------------------------------------
    static class BruteForce {

        /*
         * 🟡 Core Idea:
         * Copy k nodes into a temporary list, reverse pointers, reconnect.
         *
         * 🟢 Invariant Enforced:
         * Tail preservation only.
         *
         * ❌ Limitation:
         * Uses extra memory.
         *
         * ⏱ Time: O(n)
         * 🧠 Space: O(k)
         * 🎤 Interview Preference: Low
         */

        static ListNode reverseKGroup(ListNode head, int k) {

            if (head == null || k == 1) return head;

            ListNode dummy = new ListNode(0);
            dummy.next = head;
            ListNode groupPrev = dummy;

            while (true) {
                ListNode current = groupPrev.next;

                // Count k nodes
                int count = 0;
                while (count < k && current != null) {
                    current = current.next;
                    count++;
                }
                if (count < k) break;

                // Collect nodes
                ListNode[] buffer = new ListNode[k];
                current = groupPrev.next;
                for (int i = 0; i < k; i++) {
                    buffer[i] = current;
                    current = current.next;
                }

                // Reverse pointers inside buffer
                for (int i = k - 1; i > 0; i--) {
                    buffer[i].next = buffer[i - 1];
                }

                buffer[0].next = current;
                groupPrev.next = buffer[k - 1];
                groupPrev = buffer[0];
            }

            return dummy.next;
        }
    }

    // -----------------------------------------------------------------------------
    // 🟡 SOLUTION 2: IMPROVED (RECURSIVE)
    // -----------------------------------------------------------------------------
    static class Recursive {

        /*
         * 🟡 Core Idea:
         * Recursively reverse k nodes, then process remainder.
         *
         * 🟢 Invariant:
         * Each recursion seals one k-group.
         *
         * ⚠ Limitation:
         * Stack space grows with n/k.
         *
         * ⏱ Time: O(n)
         * 🧠 Space: O(n/k)
         * 🎤 Interview Preference: Medium
         */

        static ListNode reverseKGroup(ListNode head, int k) {

            ListNode current = head;
            int count = 0;

            // Check availability of k nodes
            while (count < k && current != null) {
                current = current.next;
                count++;
            }

            if (count < k) return head;

            // Reverse first k nodes
            ListNode prev = null;
            ListNode curr = head;
            for (int i = 0; i < k; i++) {
                ListNode nextTemp = curr.next;
                curr.next = prev;
                prev = curr;
                curr = nextTemp;
            }

            // Recurse on remaining list
            head.next = reverseKGroup(curr, k);

            return prev;
        }
    }

    // -----------------------------------------------------------------------------
    // 🟢 SOLUTION 3: OPTIMAL (INTERVIEW-PREFERRED)
    // -----------------------------------------------------------------------------
    static class Optimal {

        /*
         * 🟢 Core Idea:
         * Iteratively isolate each k-group, reverse in place, reconnect safely.
         *
         * 🟢 Fully enforces ALL invariants.
         *
         * ⏱ Time: O(n)
         * 🧠 Space: O(1)
         * 🎤 Interview Preference: Highest
         */

        static ListNode reverseKGroup(ListNode head, int k) {

            ListNode dummy = new ListNode(0);
            dummy.next = head;
            ListNode groupPrev = dummy;

            while (true) {

                // 🔵 Locate kth node
                ListNode kth = groupPrev;
                for (int i = 0; i < k && kth != null; i++) {
                    kth = kth.next;
                }

                if (kth == null) {
                    // 🟢 Invariant: fewer than k nodes remain — do not modify
                    break;
                }

                ListNode groupNext = kth.next;

                // 🔵 Reverse group
                ListNode prev = groupNext;
                ListNode curr = groupPrev.next;

                while (curr != groupNext) {
                    ListNode nextTemp = curr.next;
                    curr.next = prev;
                    prev = curr;
                    curr = nextTemp;
                }

                // 🔵 Reconnect
                ListNode oldGroupHead = groupPrev.next;
                groupPrev.next = kth;
                groupPrev = oldGroupHead;
            }

            return dummy.next;
        }
    }


// =====================================================================================
// 📘 REVERSE NODES IN K-GROUP — INVARIANT-FIRST ALGORITHM CHAPTER
// FILE: ReverseNodesInKGroup_InvariantChapter.java
// PART 2 / N  (CONTINUATION-LOCKED — DO NOT EDIT / DO NOT REORDER)
// =====================================================================================

    // =================================================================================
    // 7️⃣ 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · FULL)
    // =================================================================================
    /*
     * 🟣 How to explain WITHOUT code:
     *
     * 1️⃣ State the invariant first:
     * “Before every step, everything before my pointer is finalized and correct.
     * Everything after the current group is untouched.”
     *
     * 2️⃣ Explain boundary detection:
     * I scan ahead to find the kth node. If it doesn’t exist, I stop immediately.
     * This preserves the tail invariant.
     *
     * 3️⃣ Explain the transition:
     * I reverse pointers only inside the isolated k-sized window.
     * I never touch nodes outside this window.
     *
     * 4️⃣ Explain correctness:
     * Because I never break connectivity outside the window and each window
     * is sealed forever after processing, the list remains valid globally.
     *
     * 5️⃣ What breaks if logic changes:
     * • Reversing before confirming k nodes → corrupts tail
     * • Losing groupNext → loses list remainder
     *
     * 6️⃣ In-place feasibility:
     * Yes. Only constant pointers are used.
     *
     * 7️⃣ Streaming feasibility:
     * No. You must know k nodes ahead to preserve invariants.
     *
     * 8️⃣ When NOT to use this pattern:
     * • Variable group sizes
     * • Random-access structures
     */

    // =================================================================================
    // 8️⃣ 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
    // =================================================================================
    /*
     * 🟢 Invariant-Preserving Changes:
     * • k = 1 → no-op
     * • k = list length → full reversal
     *
     * 🟡 Reasoning-Only Changes:
     * • Recursive implementation (same invariant, different mechanics)
     *
     * 🔴 Pattern-Break Signals:
     * • “Reverse until condition”
     * • “Process as many as possible”
     *
     * Explanation:
     * These remove fixed boundaries and collapse isolation.
     */

    // =================================================================================
    // 9️⃣ ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS · INVARIANT-REUSED)
    // =================================================================================


    // =================================================================================
    // ⚫ REINFORCEMENT PROBLEM 2
    // =================================================================================
    /*
     * 📘 Problem: Swap Nodes in Pairs
     * 🔗 https://leetcode.com/problems/swap-nodes-in-pairs/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Linked List
     *
     * Given a linked list, swap every two adjacent nodes and return its head.
     *
     * Example:
     * Input: head = [1,2,3,4]
     * Output: [2,1,4,3]
     */

    /*
     * 🧠 Invariant Mapping:
     * Same invariant, k = 2.
     */

    static class Reinforcement_SwapPairs {

        static ListNode swapPairs(ListNode head) {
            return Optimal.reverseKGroup(head, 2);
        }
    }

    /*
     * 🧪 Edge Case:
     * Single node list.
     *
     * 🎤 Interview Trap:
     * Hardcoding two-node logic instead of general invariant.
     */

    // =================================================================================
    // ⚫ REINFORCEMENT PROBLEM 3
    // =================================================================================
    /*
     * 📘 Problem: Reverse Linked List II
     * 🔗 https://leetcode.com/problems/reverse-linked-list-ii/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Linked List
     *
     * Reverse a linked list from position left to position right.
     *
     * Example:
     * Input: head = [1,2,3,4,5], left = 2, right = 4
     * Output: [1,4,3,2,5]
     */

    /*
     * 🧠 Invariant Mapping:
     * Single window with dynamic boundaries.
     */

    static class Reinforcement_ReverseBetween {

        static ListNode reverseBetween(ListNode head, int left, int right) {

            if (left == right) return head;

            ListNode dummy = new ListNode(0);
            dummy.next = head;

            ListNode prev = dummy;
            for (int i = 1; i < left; i++) {
                prev = prev.next;
            }

            ListNode curr = prev.next;
            ListNode next = null;

            for (int i = 0; i < right - left; i++) {
                next = curr.next;
                curr.next = next.next;
                next.next = prev.next;
                prev.next = next;
            }

            return dummy.next;
        }
    }

/*
 * 🧪 Edge Case:
 * left = 1.
 *
 * 🎤 Interview Trap:
 * Losing reference to sublist head.
 */


// =====================================================================================
// 📘 REVERSE NODES IN K-GROUP — INVARIANT-FIRST ALGORITHM CHAPTER
// FILE: ReverseNodesInKGroup_InvariantChapter.java
// PART 3 / N  (CONTINUATION-LOCKED — DO NOT EDIT / DO NOT REORDER)
// =====================================================================================

    // =================================================================================
    // 10️⃣ 🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
    // =================================================================================

    // =================================================================================
    // 🧩 RELATED PROBLEM 1
    // =================================================================================
    /*
     * 📘 Problem: Rotate List
     * 🔗 https://leetcode.com/problems/rotate-list/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Linked List, Two Pointers
     *
     * Given the head of a linked list, rotate the list to the right by k places.
     *
     * Example 1:
     * Input: head = [1,2,3,4,5], k = 2
     * Output: [4,5,1,2,3]
     *
     * Example 2:
     * Input: head = [0,1,2], k = 4
     * Output: [2,0,1]
     *
     * Constraints:
     * The number of nodes in the list is in the range [0, 500].
     * -100 <= Node.val <= 100
     * 0 <= k <= 2 * 10^9
     */

    /*
     * 🧠 Relationship to Primary Invariant:
     * Modified invariant.
     *
     * The list is treated as a single circular window.
     * No fixed-size local windows exist.
     *
     * Why pattern changes:
     * Rotation requires cycle formation, not window isolation.
     */

    static class Related_RotateList {

        static ListNode rotateRight(ListNode head, int k) {

            if (head == null || head.next == null || k == 0) return head;

            ListNode tail = head;
            int length = 1;

            while (tail.next != null) {
                tail = tail.next;
                length++;
            }

            k %= length;
            if (k == 0) return head;

            tail.next = head; // form cycle

            int stepsToNewHead = length - k;
            ListNode newTail = tail;
            while (stepsToNewHead-- > 0) {
                newTail = newTail.next;
            }

            ListNode newHead = newTail.next;
            newTail.next = null;

            return newHead;
        }
    }

    /*
     * 🧪 Edge Case:
     * k multiple of list length.
     *
     * 🎤 Interview Note:
     * Tests understanding of cyclic invariants.
     */

    // =================================================================================
    // 🧩 RELATED PROBLEM 2
    // =================================================================================
    /*
     * 📘 Problem: Odd Even Linked List
     * 🔗 https://leetcode.com/problems/odd-even-linked-list/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Linked List
     *
     * Given the head of a singly linked list, group all the nodes with odd indices together
     * followed by the nodes with even indices.
     *
     * Example:
     * Input: head = [1,2,3,4,5]
     * Output: [1,3,5,2,4]
     *
     * Constraints:
     * The number of nodes in the linked list is in the range [0, 10^4].
     * -10^6 <= Node.val <= 10^6
     */

    /*
     * 🧠 Relationship to Primary Invariant:
     * Invariant impossible.
     *
     * This problem reorders based on position parity, not fixed windows.
     * Window isolation does not apply.
     */

    static class Related_OddEvenList {

        static ListNode oddEvenList(ListNode head) {

            if (head == null) return null;

            ListNode odd = head;
            ListNode even = head.next;
            ListNode evenHead = even;

            while (even != null && even.next != null) {
                odd.next = even.next;
                odd = odd.next;
                even.next = odd.next;
                even = even.next;
            }

            odd.next = evenHead;
            return head;
        }
    }

    /*
     * 🧪 Edge Case:
     * Two-node list.
     *
     * 🎤 Interview Note:
     * Tests ability to detect pattern mismatch.
     */

    // =================================================================================
    // 11️⃣ 🟢 LEARNING VERIFICATION (INVARIANT-FIRST)
    // =================================================================================
    /*
     * You must recall WITHOUT code:
     *
     * • The invariant: isolate, reverse, seal.
     * • Why partial tails must never be touched.
     * • How kth detection gates reversal.
     * • Why streaming reversal fails.
     *
     * Bugs you should debug intentionally:
     * • Losing groupNext
     * • Advancing groupPrev incorrectly
     *
     * Detecting this invariant in unseen problems:
     * Look for fixed-size local mutation with tail preservation.
     */

    // =================================================================================
    // 12️⃣ 🧪 main() METHOD + SELF-VERIFYING TESTS (MUST BE LAST)
    // =================================================================================
    public static void main(String[] args) {

        // 🧪 Happy Path
        ListNode test1 = buildList(1, 2, 3, 4, 5);
        ListNode result1 = Optimal.reverseKGroup(test1, 2);
        assertListEquals(result1, new int[]{2, 1, 4, 3, 5});

        // 🧪 Boundary Case: k equals list length
        ListNode test2 = buildList(1, 2, 3);
        ListNode result2 = Optimal.reverseKGroup(test2, 3);
        assertListEquals(result2, new int[]{3, 2, 1});

        // 🧪 Interview Trap: tail smaller than k
        ListNode test3 = buildList(1, 2, 3, 4, 5);
        ListNode result3 = Optimal.reverseKGroup(test3, 3);
        assertListEquals(result3, new int[]{3, 2, 1, 4, 5});

        System.out.println("All invariant-based tests passed.");
    }

    static ListNode buildList(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        for (int v : values) {
            current.next = new ListNode(v);
            current = current.next;
        }
        return dummy.next;
    }

    static void assertListEquals(ListNode head, int[] expected) {
        for (int value : expected) {
            if (head == null || head.val != value) {
                throw new AssertionError("Invariant violated: expected " + value);
            }
            head = head.next;
        }
        if (head != null) {
            throw new AssertionError("Invariant violated: extra nodes present");
        }
    }

    // =================================================================================
    // 13️⃣ ✅ CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
    // =================================================================================
    /*
     * Invariant clarity → Fixed k-sized window isolation
     * Search target clarity → kth node ahead
     * Discard logic → insufficient nodes remaining
     * Termination guarantee → forward-only pointer movement
     * Failure awareness → premature reversal, pointer loss
     * Edge-case confidence → tail < k, k = 1
     * Variant readiness → swap pairs, reverse range
     * Pattern boundary → variable windows break invariant
     */

    // =================================================================================
    // 🧘 FINAL CLOSURE STATEMENT (PROBLEM-SPECIFIC)
    // =================================================================================
    /*
     * For this problem, the invariant is strict isolation of k-sized reversal windows.
     * The answer represents locally reversed but globally consistent list structure.
     * The search terminates when a full window cannot be formed.
     * I can re-derive this solution under pressure.
     * This chapter is complete.
     */

}
