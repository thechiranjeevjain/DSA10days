package org.chijai.day4.LinkedList.session4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Merge K Sorted Lists
 * LeetCode 23
 *
 * ============================================================
 * 1. PROBLEM STATEMENT
 * ============================================================
 *
 * Given K individually sorted singly linked lists, merge them into
 * one sorted linked list.
 *
 * Example:
 *
 * L1: 1 -> 4 -> 5
 * L2: 1 -> 3 -> 4
 * L3: 2 -> 6
 *
 * Output:
 *
 * 1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6
 *
 * Let:
 *
 * N = total number of nodes across all lists
 * K = number of lists
 *
 * More precisely:
 *
 * N = n1 + n2 + ... + nK
 *
 * where ni is the number of nodes in list i.
 *
 * ============================================================
 * CORE IDEA
 * ============================================================
 *
 * Multiple independently sorted sources
 * -> only each source's first unprocessed item can matter
 * -> repeatedly choose the smallest source frontier
 * -> advance only the source that won
 *
 * Preferred:
 *
 * Min-heap of at most K frontier nodes.
 *
 * Standard interview complexity:
 *
 * Time  : O(N log K)
 * Space : O(K) auxiliary
 */
public class MergeKSortedLists {

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

    // ============================================================
    // 2. PREFERRED INTERVIEW SOLUTION — MIN HEAP OF K FRONTIERS
    // ============================================================

    static final class Solution {

        ListNode mergeKLists(ListNode[] lists) {
            if (lists == null || lists.length == 0) {
                return null;
            }

            PriorityQueue<ListNode> minHeap =
                    new PriorityQueue<>(Comparator.comparingInt(node -> node.val));

            int index = 0;

            while (index < lists.length) {
                if (lists[index] != null) {
                    minHeap.offer(lists[index]);
                }
                index++;
            }

            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;

            while (!minHeap.isEmpty()) {
                ListNode node = minHeap.poll();

                tail.next = node;
                tail = tail.next;

                if (node.next != null) {
                    minHeap.offer(node.next);
                }
            }

            return dummy.next;
        }
    }

    /*
     * ============================================================
     * 3. WHY BLOCKS — MATCH THE CODE
     * ============================================================
     */

    /*
     * WHY 1 — Why seed only list heads?
     *
     * Each sorted list contributes exactly one currently eligible node:
     * its first unprocessed node.
     */

    /*
     * WHY 2 — Why skip null heads?
     *
     * Empty lists contribute no candidate.
     *
     * Java PriorityQueue does not permit null elements.
     */

    /*
     * WHY 3 — Why is poll() globally safe?
     *
     * Heap contains each unfinished list's smallest remaining node.
     *
     * Minimum among those source minima
     * =
     * minimum across all remaining nodes.
     */

    /*
     * WHY 4 — Why push only node.next?
     *
     * Polling node changes only the frontier of node's own source.
     *
     * Since that source is sorted, node.next becomes its new smallest
     * remaining candidate.
     */

    /*
     * ============================================================
     * 4. 30-SECOND RECALL CARD
     * ============================================================
     *
     * Trigger:
     * K sorted sources -> one sorted output.
     *
     * Invariant:
     * heap contains one frontier from every unfinished source.
     *
     * Move:
     * poll min
     * append
     * push only successor
     *
     * Complexity anchor:
     *
     * N nodes
     * x
     * heap operation on at most K elements
     * =
     * O(N log K)
     *
     * Space:
     * at most K heap entries
     * =
     * O(K)
     *
     * Master sentence:
     *
     * "Expose one eligible item per sorted source;
     * when one wins, advance only that source."
     */

    // ============================================================
    // 5. FIRST PRINCIPLES — DERIVE THE ALGORITHM
    // ============================================================

    /*
     * Goal:
     *
     * repeatedly output the globally smallest remaining node.
     *
     *
     * STEP A — What does sortedness buy us?
     * ------------------------------------------------------------
     *
     * One remaining source:
     *
     * 4 -> 5 -> 9
     *
     * 4 is that source's smallest remaining node.
     *
     * Therefore nodes behind 4 cannot become globally next before 4.
     *
     * So:
     *
     * source minimum
     * =
     * first unprocessed node
     * =
     * FRONTIER.
     *
     *
     * STEP B — Reduce the global problem
     * ------------------------------------------------------------
     *
     * Suppose current source frontiers are:
     *
     * f1, f2, ..., fK
     *
     * Every remaining node belongs to one source and is >= that
     * source's frontier.
     *
     * Therefore:
     *
     * min(all remaining nodes)
     * =
     * min(f1, f2, ..., fK)
     *
     * FRONTIER THEOREM.
     *
     *
     * STEP C — Required operations
     * ------------------------------------------------------------
     *
     * Repeatedly:
     *
     * 1. remove minimum frontier
     * 2. output it
     * 3. insert one possible successor
     *
     *
     * STEP D — Choose data structure from operations
     * ------------------------------------------------------------
     *
     * Need:
     *
     * repeated remove-min
     * +
     * repeated insertion
     *
     * over at most K candidates.
     *
     * Min-heap:
     *
     * poll  -> O(log K)
     * offer -> O(log K)
     *
     * Hence the algorithm is derived,
     * not memorized.
     */

    /*
     * ============================================================
     * 6. REUSABLE K-WAY MERGE TEMPLATE
     * ============================================================
     *
     * seed one frontier from every non-empty sorted source
     *
     * while candidates remain:
     *
     *     smallest = remove minimum frontier
     *     output smallest
     *
     *     if smallest's source has another item:
     *         add exactly that next frontier
     *
     * Pattern fingerprint:
     *
     * multiple sorted sources
     * +
     * one eligible item per source
     * +
     * repeated global minimum
     * +
     * advance only winning source
     */

    // ============================================================
    // 7. APPROACH LADDER
    // ============================================================

    /*
     * 1. Collect all values + sort
     *    O(N log N), O(N)
     *
     *        ↓ repeated minimum extraction
     *
     * 2. Put all N nodes in heap
     *    O(N log N), O(N)
     *
     *        ↓ sortedness means most nodes are not yet eligible
     *
     * 3. Keep only K frontiers, scan for minimum
     *    O(NK), O(1)
     *
     *        ↓ repeated minimum selection is expensive
     *
     * 4. Min-heap of K frontiers
     *    O(N log K), O(K)
     *
     *
     * Independent route:
     *
     * Merge Two
     * -> sequential Merge K
     * -> balance merge tree
     * -> O(N log K)
     */

    // ============================================================
    // APPROACH 1 — COLLECT + SORT
    // ============================================================

    static final class CollectAndSortSolution {

        ListNode mergeKLists(ListNode[] lists) {
            if (lists == null || lists.length == 0) {
                return null;
            }

            List<Integer> values = new ArrayList<>();

            int listIndex = 0;

            while (listIndex < lists.length) {
                ListNode current = lists[listIndex];

                while (current != null) {
                    values.add(current.val);
                    current = current.next;
                }

                listIndex++;
            }

            Collections.sort(values);

            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;

            int index = 0;

            while (index < values.size()) {
                tail.next = new ListNode(values.get(index));
                tail = tail.next;
                index++;
            }

            return dummy.next;
        }
    }

    // ============================================================
    // APPROACH 2 — ALL N NODES IN HEAP
    // ============================================================

    static final class AllNodesHeapSolution {

        ListNode mergeKLists(ListNode[] lists) {
            if (lists == null || lists.length == 0) {
                return null;
            }

            PriorityQueue<ListNode> minHeap =
                    new PriorityQueue<>(Comparator.comparingInt(node -> node.val));

            int listIndex = 0;

            while (listIndex < lists.length) {
                ListNode current = lists[listIndex];

                while (current != null) {
                    minHeap.offer(current);
                    current = current.next;
                }

                listIndex++;
            }

            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;

            while (!minHeap.isEmpty()) {
                ListNode node = minHeap.poll();

                tail.next = new ListNode(node.val);
                tail = tail.next;
            }

            return dummy.next;
        }
    }

    /*
     * Valid solution.
     *
     * But heap can grow to N.
     *
     * Therefore:
     *
     * N offers x O(log N)
     * +
     * N polls  x O(log N)
     *
     * =
     * O(N log N)
     *
     * Extra space:
     * O(N)
     *
     * Key missed opportunity:
     *
     * if 4 is still unprocessed in:
     *
     * 1 -> 4 -> 5
     *
     * then 5 cannot yet beat 4.
     *
     * So 5 does not need to be in the competition.
     */

    // ============================================================
    // APPROACH 3 — SCAN K FRONTIERS
    // ============================================================

    static final class ScanKFrontiersSolution {

        ListNode mergeKLists(ListNode[] lists) {
            if (lists == null || lists.length == 0) {
                return null;
            }

            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;

            while (true) {
                int minimumIndex = -1;
                int index = 0;

                while (index < lists.length) {
                    if (lists[index] != null
                            && (minimumIndex == -1
                            || lists[index].val < lists[minimumIndex].val)) {

                        minimumIndex = index;
                    }

                    index++;
                }

                if (minimumIndex == -1) {
                    break;
                }

                tail.next = lists[minimumIndex];
                tail = tail.next;

                lists[minimumIndex] = lists[minimumIndex].next;
            }

            return dummy.next;
        }
    }

    /*
     * Candidate set is now at most K.
     *
     * But each output requires scanning K source frontiers.
     *
     * N outputs
     * x
     * O(K)
     *
     * =
     * O(NK)
     *
     * Extra space:
     * O(1)
     */

    /*
     * ============================================================
     * TWO SEPARATE OPTIMIZATIONS
     * ============================================================
     *
     * Optimization 1:
     *
     * all N nodes
     * ->
     * K eligible frontiers
     *
     * Reduces maintained candidate set.
     *
     *
     * Optimization 2:
     *
     * scan K candidates
     * ->
     * heap K candidates
     *
     * Reduces repeated minimum selection:
     *
     * O(K)
     * ->
     * O(log K)
     */

    // ============================================================
    // 8. FULL HEAP DRY RUN
    // ============================================================

    /*
     * Input:
     *
     * L1: 1 -> 4 -> 5
     * L2: 1 -> 3 -> 4
     * L3: 2 -> 6
     *
     * +------+-----------------+----------+----------+-------------------------+
     * | Step | Heap before     | Poll     | Push     | Output                  |
     * +------+-----------------+----------+----------+-------------------------+
     * | Init | 1L1,1L2,2L3     | -        | -        | []                      |
     * | 1    | 1L1,1L2,2L3     | 1L1      | 4L1      | 1                       |
     * | 2    | 1L2,2L3,4L1     | 1L2      | 3L2      | 1,1                     |
     * | 3    | 2L3,3L2,4L1     | 2L3      | 6L3      | 1,1,2                   |
     * | 4    | 3L2,4L1,6L3     | 3L2      | 4L2      | 1,1,2,3                 |
     * | 5    | 4L1,4L2,6L3     | 4L1      | 5L1      | 1,1,2,3,4               |
     * | 6    | 4L2,5L1,6L3     | 4L2      | -        | 1,1,2,3,4,4             |
     * | 7    | 5L1,6L3         | 5L1      | -        | 1,1,2,3,4,4,5           |
     * | 8    | 6L3             | 6L3      | -        | 1,1,2,3,4,4,5,6         |
     * +------+-----------------+----------+----------+-------------------------+
     *
     * Heap size never exceeds K = 3.
     */

    /*
     * ============================================================
     * 9. INVARIANT + CORRECTNESS PROOF
     * ============================================================
     *
     * Invariant:
     *
     * Before every poll, the heap contains the smallest unprocessed
     * node from every unfinished list.
     *
     * Initialization:
     *
     * Each non-empty list head is that list's smallest remaining node.
     *
     * Maintenance:
     *
     * The minimum among all source minima is the global minimum.
     * We append it.
     *
     * Only that source changes.
     * Its successor becomes its new minimum remaining node.
     *
     * Therefore pushing node.next restores the invariant.
     *
     * Termination:
     *
     * Heap empty means no source has an unprocessed node.
     *
     * Every node has therefore been emitted exactly once,
     * and always in nondecreasing order.
     */

    // ============================================================
    // 10. COMPLEXITY FROM FIRST PRINCIPLES — PREFERRED SOLUTION
    // ============================================================

    /*
     * This section is for interview defense.
     *
     * Do NOT start by saying:
     *
     * "I remember it is O(N log K)."
     *
     * Start from the code and derive it.
     */

    /*
     * ============================================================
     * STEP 1 — DEFINE THE INPUT VARIABLES
     * ============================================================
     *
     * K = number of input lists.
     *
     * N = total number of nodes across all lists.
     *
     * If list lengths are:
     *
     * n1, n2, ..., nK
     *
     * then:
     *
     * N = n1 + n2 + ... + nK
     *
     * Important:
     *
     * N is NOT "length of each list".
     *
     * Lists may have different sizes.
     */

    /*
     * ============================================================
     * STEP 2 — INITIAL HEAP BUILD
     * ============================================================
     *
     * Code:
     *
     * while (index < lists.length) {
     *     if (lists[index] != null) {
     *         minHeap.offer(lists[index]);
     *     }
     *     index++;
     * }
     *
     * We inspect K array positions:
     *
     * O(K)
     *
     * At most K non-null heads are offered.
     *
     * Each PriorityQueue.offer() costs:
     *
     * O(log K)
     *
     * Therefore this exact repeated-offer construction is bounded by:
     *
     * O(K log K)
     *
     * Interview nuance:
     *
     * A heap can be bulk-heapified in O(K),
     * but THIS implementation repeatedly calls offer(),
     * so O(K log K) is the clean bound for initialization.
     */

    /*
     * ============================================================
     * STEP 3 — HOW MANY TIMES DOES THE MAIN LOOP RUN?
     * ============================================================
     *
     * Code:
     *
     * while (!minHeap.isEmpty()) {
     *     ListNode node = minHeap.poll();
     *     ...
     * }
     *
     * Do not merely say:
     *
     * "This loop is O(N)."
     *
     * Prove it.
     *
     * Every iteration:
     *
     * permanently consumes exactly ONE previously unprocessed input node.
     *
     * There are N total input nodes.
     *
     * No node is appended twice.
     *
     * Therefore:
     *
     * main-loop iterations = exactly N.
     */

    /*
     * ============================================================
     * STEP 4 — WHAT IS THE MAXIMUM HEAP SIZE?
     * ============================================================
     *
     * This determines whether heap operations cost:
     *
     * O(log K)
     *
     * or:
     *
     * O(log N).
     *
     * Invariant:
     *
     * at most one frontier node per unfinished list.
     *
     * There are K lists.
     *
     * Therefore:
     *
     * heap size <= K.
     *
     *
     * During an iteration:
     *
     * poll one node
     * ->
     * heap temporarily shrinks by one
     *
     * then possibly:
     *
     * offer exactly one successor
     *
     * So heap never exceeds K.
     */

    /*
     * ============================================================
     * STEP 5 — COST OF ONE MAIN-LOOP ITERATION
     * ============================================================
     *
     * ListNode node = minHeap.poll();
     *
     * O(log K)
     *
     *
     * tail.next = node;
     * tail = tail.next;
     *
     * O(1)
     *
     *
     * if (node.next != null) {
     *     minHeap.offer(node.next);
     * }
     *
     * at most:
     *
     * O(log K)
     *
     *
     * Therefore:
     *
     * one iteration
     * =
     * O(log K) + O(1) + O(log K)
     *
     * =
     * O(log K)
     */

    /*
     * ============================================================
     * STEP 6 — MULTIPLY ITERATIONS BY WORK PER ITERATION
     * ============================================================
     *
     * N iterations
     *
     * x
     *
     * O(log K) work per iteration
     *
     * =
     *
     * O(N log K)
     */

    /*
     * ============================================================
     * STEP 7 — INCLUDE INITIALIZATION
     * ============================================================
     *
     * Full bound for this exact implementation:
     *
     * initialization:
     * O(K log K)
     *
     * main merge:
     * O(N log K)
     *
     * total:
     *
     * O(K log K + N log K)
     *
     * =
     *
     * O((N + K) log K)
     *
     *
     * Why do interviews usually say:
     *
     * O(N log K)?
     *
     * Because the merge phase dominates under the usual interpretation
     * where K is the number of non-empty/active lists and N >= K.
     *
     * Also, many standard analyses focus on processing the N nodes.
     *
     * If the input array contains many empty lists and K >> N,
     * mentioning initialization separately is the more precise defense.
     */

    /*
     * ============================================================
     * STEP 8 — WHY log K, NOT log N?
     * ============================================================
     *
     * Heap-operation complexity depends on:
     *
     * CURRENT HEAP SIZE
     *
     * not:
     *
     * TOTAL NUMBER OF ELEMENTS EVER PROCESSED.
     *
     * Preferred frontier heap:
     *
     * heap size <= K
     *
     * therefore:
     *
     * poll / offer = O(log K)
     *
     *
     * All-nodes heap:
     *
     * heap can hold N nodes
     *
     * therefore:
     *
     * poll / offer = O(log N)
     *
     *
     * This distinction is the algorithmic payoff of exploiting sortedness.
     */

    /*
     * ============================================================
     * STEP 9 — PROVE EACH NODE ENTERS THE HEAP AT MOST ONCE
     * ============================================================
     *
     * Take any node x.
     *
     * Case 1:
     *
     * x is a list head.
     *
     * Then x is offered during initialization exactly once.
     *
     *
     * Case 2:
     *
     * x is not a list head.
     *
     * Then x has exactly one predecessor p in its singly linked list.
     *
     * x is offered only when:
     *
     * p is polled
     *
     * and:
     *
     * minHeap.offer(p.next)
     *
     * Since p is polled once,
     * x can be offered at most once.
     *
     * Therefore:
     *
     * each node:
     *
     * offered <= 1 time
     * polled  = 1 time
     *
     * This gives another accounting proof of O(N log K).
     */

    // ============================================================
    // 11. SPACE COMPLEXITY FROM FIRST PRINCIPLES
    // ============================================================

    /*
     * Ask:
     *
     * "What extra memory can exist simultaneously?"
     *
     *
     * HEAP
     * ------------------------------------------------------------
     *
     * At most one node reference per unfinished list.
     *
     * Maximum:
     *
     * K entries.
     *
     * Therefore:
     *
     * O(K)
     *
     *
     * OTHER VARIABLES
     * ------------------------------------------------------------
     *
     * dummy
     * tail
     * node
     * index
     *
     * constant number of references / primitives.
     *
     * Therefore:
     *
     * O(1)
     *
     *
     * Total auxiliary space:
     *
     * O(K) + O(1)
     *
     * =
     *
     * O(K)
     */

    /*
     * ============================================================
     * OUTPUT-SPACE NUANCE
     * ============================================================
     *
     * The final merged list contains N nodes.
     *
     * Does that mean this implementation uses O(N) extra space?
     *
     * No.
     *
     * The preferred solution REUSES original ListNode objects:
     *
     * tail.next = node;
     *
     * It does not allocate N new result nodes.
     *
     * Therefore:
     *
     * auxiliary algorithmic space
     * =
     * O(K)
     *
     *
     * If an interviewer says:
     *
     * "But the result itself has N nodes."
     *
     * Strong answer:
     *
     * "Yes, the output contains N nodes, but those nodes already existed.
     * The algorithm's additional data structure is the heap of at most K
     * references, so auxiliary space is O(K)."
     */

    /*
     * ============================================================
     * REUSABLE COMPLEXITY-DERIVATION CHECKLIST
     * ============================================================
     *
     * For any algorithm, ask:
     *
     * 1. What variables describe the input size?
     *
     * 2. How many times does the main operation execute?
     *
     * 3. Why exactly that many times?
     *
     * 4. What work happens inside one operation?
     *
     * 5. What is the size of the data structure when that operation runs?
     *
     * 6. Sum or multiply the costs.
     *
     * 7. What memory can exist simultaneously?
     *
     * 8. Am I counting auxiliary space or output space?
     *
     *
     * For this problem:
     *
     * N consumed nodes
     * x
     * heap operation on <= K candidates
     *
     * =
     *
     * O(N log K)
     */

    // ============================================================
    // 12. INTERVIEW COMPLEXITY DEFENSE — COMMON PUSHBACK
    // ============================================================

    /*
     * Q:
     * Why is the while loop exactly N iterations?
     *
     * A:
     * "Each iteration polls and permanently appends exactly one previously
     * unprocessed input node. Since there are N total nodes and no node is
     * consumed twice, there are exactly N iterations."
     *
     *
     * Q:
     * Why is heap size K?
     *
     * A:
     * "I maintain only one frontier node per unfinished list. Polling removes
     * one candidate and I add at most one successor from that same list, so
     * the heap can never exceed K elements."
     *
     *
     * Q:
     * Why log K rather than log N?
     *
     * A:
     * "PriorityQueue operations depend on the heap's current size. This heap
     * contains at most K frontier nodes, even though N total nodes pass through
     * it over time. Therefore each offer/poll is O(log K)."
     *
     *
     * Q:
     * Does every node really enter once?
     *
     * A:
     * "A head enters during initialization. Every other node can enter only
     * when its unique predecessor is polled. Since each predecessor is polled
     * once, each node is offered at most once."
     *
     *
     * Q:
     * Isn't initialization O(K), not O(K log K)?
     *
     * A:
     * "Scanning the array is O(K), but this implementation calls offer() up
     * to K times, so the clean repeated-insertion bound is O(K log K).
     * A bulk heapify implementation could build a heap in O(K)."
     *
     *
     * Q:
     * Isn't space O(N) because the output has N nodes?
     *
     * A:
     * "The preferred implementation reuses the input nodes, so it doesn't
     * allocate N new output nodes. Auxiliary space is the heap, O(K)."
     */

    // ============================================================
    // 13. FAILURE MODES
    // ============================================================

    /*
     * FAILURE 1 — Add null to PriorityQueue
     *
     * PriorityQueue does not accept null.
     */

    /*
     * FAILURE 2 — Reinsert same polled node
     *
     * Wrong:
     *
     * ListNode curr = minHeap.poll();
     *
     * while (curr.next != null) {
     *     minHeap.offer(curr);
     *     curr = curr.next;
     * }
     *
     * Poll 1
     * -> offer same 1 again
     * -> poll 1 again
     * -> ...
     *
     * Progress can collapse.
     */

    /*
     * FAILURE 3 — Re-add entire suffix after every poll
     *
     * Adding every node once during initialization:
     *
     * valid all-nodes heap solution.
     *
     * Adding the remaining suffix after EVERY poll:
     *
     * duplicate work / duplicate references.
     */

    /*
     * FAILURE 4 — Claim O(N log K) while heap stores all N nodes
     *
     * Complexity follows actual heap size.
     *
     * If heap can grow to N:
     *
     * operations are O(log N).
     */

    /*
     * FAILURE 5 — Forget sortedness
     *
     * If sources are unsorted:
     *
     * first unprocessed node
     * != guaranteed source minimum.
     *
     * Frontier theorem fails.
     */

    // ============================================================
    // 14. SECOND DERIVATION — MERGE TWO -> SEQUENTIAL MERGE
    // ============================================================

    static final class SequentialMergeSolution {

        ListNode mergeKLists(ListNode[] lists) {
            if (lists == null || lists.length == 0) {
                return null;
            }

            ListNode merged = null;
            int index = 0;

            while (index < lists.length) {
                merged = mergeTwoLists(merged, lists[index]);
                index++;
            }

            return merged;
        }

        private ListNode mergeTwoLists(ListNode first, ListNode second) {
            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;

            while (first != null && second != null) {
                if (first.val <= second.val) {
                    tail.next = first;
                    first = first.next;
                } else {
                    tail.next = second;
                    second = second.next;
                }

                tail = tail.next;
            }

            tail.next = first != null ? first : second;

            return dummy.next;
        }
    }

    /*
     * ============================================================
     * DERIVE SEQUENTIAL MERGE COMPLEXITY
     * ============================================================
     *
     * Suppose for intuition the K lists have roughly equal sizes:
     *
     * each list ~= N / K nodes.
     *
     * Merge first two:
     *
     * ~ 2N/K work
     *
     * Merge accumulated result with third:
     *
     * ~ 3N/K work
     *
     * Then:
     *
     * ~ 4N/K
     *
     * ...
     *
     * Final:
     *
     * ~ KN/K
     *
     * Total:
     *
     * (N/K) * (2 + 3 + ... + K)
     *
     * Sum:
     *
     * 2 + 3 + ... + K
     * =
     * O(K^2)
     *
     * Therefore:
     *
     * (N/K) * O(K^2)
     *
     * =
     *
     * O(NK)
     *
     *
     * Intuition:
     *
     * early nodes in the accumulated result are rescanned again and again.
     */

    // ============================================================
    // 15. BALANCED PAIRWISE MERGE
    // ============================================================

    static final class DivideAndConquerSolution {

        ListNode mergeKLists(ListNode[] lists) {
            if (lists == null || lists.length == 0) {
                return null;
            }

            int interval = 1;

            while (interval < lists.length) {
                int index = 0;

                while (index + interval < lists.length) {
                    lists[index] =
                            mergeTwoLists(lists[index], lists[index + interval]);

                    index += interval * 2;
                }

                interval *= 2;
            }

            return lists[0];
        }

        private ListNode mergeTwoLists(ListNode first, ListNode second) {
            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;

            while (first != null && second != null) {
                if (first.val <= second.val) {
                    tail.next = first;
                    first = first.next;
                } else {
                    tail.next = second;
                    second = second.next;
                }

                tail = tail.next;
            }

            tail.next = first != null ? first : second;

            return dummy.next;
        }
    }

    /*
     * ============================================================
     * DERIVE BALANCED MERGE COMPLEXITY
     * ============================================================
     *
     * Example:
     *
     * K = 8
     *
     * Round 1:
     * 8 lists -> 4 merged lists
     *
     * Round 2:
     * 4 -> 2
     *
     * Round 3:
     * 2 -> 1
     *
     * Number of rounds:
     *
     * log2(K)
     *
     *
     * At ONE round:
     *
     * every node participates in at most one Merge Two operation.
     *
     * Therefore total work across an entire round:
     *
     * O(N)
     *
     *
     * Number of rounds:
     *
     * O(log K)
     *
     * Therefore:
     *
     * O(N)
     * x
     * O(log K)
     *
     * =
     *
     * O(N log K)
     *
     *
     * Auxiliary space:
     *
     * O(1)
     *
     * for this iterative implementation,
     * ignoring the input array.
     *
     * Important:
     *
     * Same final Big-O as heap,
     * but completely different proof.
     */

    // ============================================================
    // 16. COMPLEXITY COMPARISON — DERIVED, NOT MEMORIZED
    // ============================================================

    /*
     * +-------------------------+--------------------------------------+-------------+-------------+
     * | Approach                | Core reason                          | Time        | Extra space |
     * +-------------------------+--------------------------------------+-------------+-------------+
     * | Collect + sort          | sort N collected values              | O(N log N)  | O(N)        |
     * | All-nodes heap          | N ops on heap size up to N           | O(N log N)  | O(N)        |
     * | Scan K frontiers        | N outputs x K-way scan               | O(NK)       | O(1)        |
     * | Sequential merge-two    | growing result repeatedly rescanned  | O(NK) worst | O(1)        |
     * | Heap K frontiers        | N ops on heap size <= K              | O(N log K)  | O(K)        |
     * | Balanced pairwise merge | O(N) per level x O(log K) levels     | O(N log K)  | O(1)        |
     * +-------------------------+--------------------------------------+-------------+-------------+
     */

    /*
     * ============================================================
     * THE KEY COMPLEXITY CONTRAST
     * ============================================================
     *
     * ALL-NODES HEAP:
     *
     * heap size = N
     *
     * N heap operations
     *
     * ->
     *
     * O(N log N)
     *
     *
     * FRONTIER HEAP:
     *
     * heap size <= K
     *
     * N heap operations
     *
     * ->
     *
     * O(N log K)
     *
     *
     * Same PriorityQueue.
     *
     * Different maintained state.
     *
     * Therefore different complexity.
     */

    // ============================================================
    // 17. HEAP VS BALANCED MERGE
    // ============================================================

    /*
     * HEAP:
     *
     * best generic K sorted streams abstraction
     * lazy consumption
     * easy to stop after first X outputs
     * natural for iterators/files
     *
     *
     * BALANCED MERGE:
     *
     * no heap
     * iterative O(1) auxiliary structure
     * excellent linked-list follow-up
     *
     *
     * Both:
     *
     * O(N log K)
     *
     * but via different mechanisms.
     */

    // ============================================================
    // 18. HORIZONTAL MASTERY
    // ============================================================

    /*
     * The abstraction is:
     *
     * K sorted sources
     * +
     * one current candidate per source.
     *
     *
     * LINKED LIST
     *
     * heap entry:
     * ListNode
     *
     * successor:
     * node.next
     *
     *
     * SORTED ARRAY
     *
     * heap entry:
     * value, arrayIndex, elementIndex
     *
     * successor:
     * same array, elementIndex + 1
     *
     *
     * SORTED MATRIX ROW
     *
     * heap entry:
     * value, row, col
     *
     * successor:
     * same row, col + 1
     *
     *
     * SORTED ITERATOR
     *
     * heap entry:
     * current value + source identity
     *
     * successor:
     * iterator.next()
     *
     *
     * SORTED FILE
     *
     * heap entry:
     * current record + reader
     *
     * successor:
     * reader.readNext()
     */

    /*
     * ============================================================
     * TRANSFER TABLE
     * ============================================================
     *
     * +----------------------+----------------------+----------------------------+
     * | Problem              | Heap entry           | Successor after poll       |
     * +----------------------+----------------------+----------------------------+
     * | Merge K Lists        | ListNode             | node.next                  |
     * | Merge K Arrays       | value,array,index    | same array, index + 1      |
     * | Kth Smallest Matrix  | value,row,col        | same row, col + 1          |
     * | Smallest Range       | value,list,index     | same list, index + 1       |
     * | External Merge       | record,reader        | reader.readNext()          |
     * | Sorted Event Streams | event,source         | source.nextEvent()         |
     * +----------------------+----------------------+----------------------------+
     */

    // ============================================================
    // 19. REINFORCEMENT PROBLEMS
    // ============================================================

    /*
     * 1. Merge Two Sorted Lists
     *
     * Same frontier principle with K = 2.
     *
     *
     * 2. Merge K Sorted Arrays
     *
     * Same exact engine.
     * Explicitly store continuation state.
     *
     *
     * 3. Kth Smallest Element in a Sorted Matrix
     *
     * Same K-way merge.
     * Stop after kth poll instead of full merge.
     *
     *
     * 4. Smallest Range Covering Elements from K Lists
     *
     * Same frontier heap
     * +
     * maintain current maximum.
     *
     *
     * 5. External Merge Sort
     *
     * Same algorithm at file/system scale.
     *
     *
     * 6. Merge Sorted Event Streams
     *
     * Same source-frontier invariant on timestamps.
     */

    // ============================================================
    // 20. PATTERN BOUNDARIES
    // ============================================================

    /*
     * HEAP does NOT imply K-way merge.
     *
     *
     * Top K Frequent:
     *
     * heap maintains best K scores.
     *
     * No sorted-source advancement.
     *
     *
     * Median from Data Stream:
     *
     * heaps maintain lower/upper partitions.
     *
     *
     * Dijkstra:
     *
     * heap maintains smallest tentative graph distance.
     *
     * New states appear via relaxation.
     *
     *
     * K-way merge fingerprint:
     *
     * 1. multiple independently sorted sources
     * 2. one currently eligible item per source
     * 3. repeatedly choose smallest eligible item
     * 4. advance only source that produced it
     */

    // ============================================================
    // 21. INTERVIEW ARTICULATION — COMPLETE ANSWER
    // ============================================================

    /*
     * "Each list is individually sorted, so only its first unprocessed node
     * can possibly be the next global minimum. Therefore I reduce the search
     * space from all remaining nodes to at most one frontier node per list.
     *
     * I keep those frontiers in a min-heap. The heap minimum is the minimum
     * among every source's smallest remaining node, so it is globally safe
     * to append.
     *
     * After consuming that node, only its own source exposes a new candidate,
     * so I insert its successor.
     *
     * For complexity, let N be the total number of nodes and K be the number
     * of lists. The main loop runs exactly N times because each iteration
     * permanently consumes one node. The heap contains at most K entries,
     * so each poll and optional offer costs O(log K). Therefore the merge
     * phase is O(N log K). The heap stores at most K references, so auxiliary
     * space is O(K).
     *
     * This implementation seeds the heap using up to K repeated offers, so
     * initialization is O(K log K); under the usual active-list assumption
     * this is absorbed into the standard O(N log K) bound."
     */

    /*
     * ============================================================
     * 22. FINAL RECONSTRUCTION MAP
     * ============================================================
     *
     * Need global minimum
     *
     *      ↓
     *
     * sources are individually sorted
     *
     *      ↓
     *
     * each source minimum
     * =
     * first unprocessed item
     *
     *      ↓
     *
     * global minimum
     * =
     * minimum of K frontiers
     *
     *      ↓
     *
     * maintain at most K candidates
     *
     *      ↓
     *
     * need repeated removeMin + insert successor
     *
     *      ↓
     *
     * min-heap
     *
     *      ↓
     *
     * N nodes consumed
     * x
     * O(log K) heap work
     *
     *      ↓
     *
     * O(N log K)
     *
     * Space:
     *
     * <= K simultaneous heap entries
     *
     *      ↓
     *
     * O(K)
     */

    /*
     * ============================================================
     * 23. MASTERY CHECK — ALGORITHM + COMPLEXITY
     * ============================================================
     *
     * You should be able to answer without code:
     *
     * 1. Why does each source expose only one candidate?
     *
     * 2. Why is minimum frontier globally safe?
     *
     * 3. Why does only one new candidate appear after a poll?
     *
     * 4. Why does the main loop execute exactly N times?
     *
     * 5. Why is heap size at most K?
     *
     * 6. Why is each heap operation O(log K), not O(log N)?
     *
     * 7. Why does each node enter heap at most once?
     *
     * 8. Why is auxiliary space O(K)?
     *
     * 9. Why does output size N not force O(N) auxiliary space here?
     *
     * 10. Why is all-nodes heap O(N log N)?
     *
     * 11. Why is scanning K frontiers O(NK)?
     *
     * 12. Derive sequential merge's O(NK).
     *
     * 13. Derive balanced merge's O(N log K).
     *
     * 14. Why can two algorithms both be O(N log K) for different reasons?
     *
     * 15. How does this pattern transfer to arrays, matrices, iterators,
     *     files, and event streams?
     */

    // ============================================================
    // TEST HELPERS
    // ============================================================

    private static ListNode build(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        int index = 0;

        while (index < values.length) {
            tail.next = new ListNode(values[index]);
            tail = tail.next;
            index++;
        }

        return dummy.next;
    }

    private static int[] toArray(ListNode head) {
        int length = 0;
        ListNode current = head;

        while (current != null) {
            length++;
            current = current.next;
        }

        int[] result = new int[length];

        current = head;
        int index = 0;

        while (current != null) {
            result[index] = current.val;
            current = current.next;
            index++;
        }

        return result;
    }

    private static void assertArrayEquals(int[] expected, int[] actual) {
        if (expected.length != actual.length) {
            throw new AssertionError(
                    "Length mismatch: expected "
                            + expected.length
                            + ", actual "
                            + actual.length
            );
        }

        int index = 0;

        while (index < expected.length) {
            if (expected[index] != actual[index]) {
                throw new AssertionError(
                        "Mismatch at index "
                                + index
                                + ": expected "
                                + expected[index]
                                + ", actual "
                                + actual[index]
                );
            }

            index++;
        }
    }

    private static ListNode[] exampleInput() {
        return new ListNode[]{
                build(1, 4, 5),
                build(1, 3, 4),
                build(2, 6)
        };
    }

    // ============================================================
    // SELF-VERIFYING TESTS
    // ============================================================

    public static void main(String[] args) {
        int[] expected =
                new int[]{1, 1, 2, 3, 4, 4, 5, 6};

        assertArrayEquals(
                expected,
                toArray(new Solution().mergeKLists(exampleInput()))
        );

        assertArrayEquals(
                expected,
                toArray(new CollectAndSortSolution().mergeKLists(exampleInput()))
        );

        assertArrayEquals(
                expected,
                toArray(new AllNodesHeapSolution().mergeKLists(exampleInput()))
        );

        assertArrayEquals(
                expected,
                toArray(new ScanKFrontiersSolution().mergeKLists(exampleInput()))
        );

        assertArrayEquals(
                expected,
                toArray(new SequentialMergeSolution().mergeKLists(exampleInput()))
        );

        assertArrayEquals(
                expected,
                toArray(new DivideAndConquerSolution().mergeKLists(exampleInput()))
        );

        ListNode[] empty = {};
        assert new Solution().mergeKLists(empty) == null;

        assert new Solution().mergeKLists(null) == null;

        ListNode[] allNull = {
                null,
                null,
                null
        };

        assert new Solution().mergeKLists(allNull) == null;

        ListNode[] withNulls = {
                null,
                build(2),
                null,
                build(1, 5)
        };

        assertArrayEquals(
                new int[]{1, 2, 5},
                toArray(new Solution().mergeKLists(withNulls))
        );

        ListNode[] negatives = {
                build(-10, -2, 8),
                build(-9, -1),
                build(0, 3)
        };

        assertArrayEquals(
                new int[]{-10, -9, -2, -1, 0, 3, 8},
                toArray(new Solution().mergeKLists(negatives))
        );

        ListNode[] duplicates = {
                build(1, 1, 1),
                build(1, 1),
                build(1)
        };

        assertArrayEquals(
                new int[]{1, 1, 1, 1, 1, 1},
                toArray(new Solution().mergeKLists(duplicates))
        );

        ListNode[] singleList = {
                build(1, 2, 3)
        };

        assertArrayEquals(
                new int[]{1, 2, 3},
                toArray(new Solution().mergeKLists(singleList))
        );

        System.out.println("MergeKSortedListsV4: all assertions passed.");
    }
}
