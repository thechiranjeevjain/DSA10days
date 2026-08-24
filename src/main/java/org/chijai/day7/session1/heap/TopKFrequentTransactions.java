package org.chijai.day7.session1.heap;

import java.util.*;

/**
 * ================================================================
 *                     TopKFrequentTransactions
 * ================================================================
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * HashMap
 * Heap (Priority Queue)
 * Bucket Sort
 * Frequency Counting
 * Top-K
 *
 * ----------------------------------------------------------------
 * Problem Description
 * ----------------------------------------------------------------
 *
 * Given a list of transaction identifiers (strings), return the
 * k most frequently occurring transactions.
 *
 * If multiple transactions have the same frequency, any order among
 * those transactions is acceptable unless a custom ordering is
 * explicitly required.
 *
 * The goal is to avoid sorting the entire collection whenever
 * possible.
 *
 * ----------------------------------------------------------------
 * Constraints
 * ----------------------------------------------------------------
 *
 * 1 <= n <= 100000
 * 1 <= k <= number of unique transactions
 *
 * Transaction IDs may repeat many times.
 *
 * ----------------------------------------------------------------
 * Representative Example 1
 * ----------------------------------------------------------------
 *
 * transactions =
 * ["A","B","A","C","B","A"]
 *
 * k = 2
 *
 * Output:
 *
 * ["A","B"]
 *
 * Explanation:
 *
 * A -> 3
 * B -> 2
 * C -> 1
 *
 * ----------------------------------------------------------------
 * Representative Example 2
 * ----------------------------------------------------------------
 *
 * transactions =
 * ["TX1","TX2","TX2","TX3","TX3","TX3"]
 *
 * k = 1
 *
 * Output:
 *
 * ["TX3"]
 *
 * ----------------------------------------------------------------
 * Representative Example 3
 * ----------------------------------------------------------------
 *
 * transactions =
 * ["A"]
 *
 * k = 1
 *
 * Output:
 *
 * ["A"]
 *
 * ----------------------------------------------------------------
 * Similar Problems
 * ----------------------------------------------------------------
 *
 * LeetCode 347
 * Top K Frequent Elements
 *
 * https://leetcode.com/problems/top-k-frequent-elements/
 *
 * This chapter adapts exactly the same invariant to transaction
 * identifiers represented as Strings.
 *
 * ================================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ================================================================
 *
 * Pattern
 * -------
 *
 * Frequency Counting + Min Heap
 *
 * Pattern Archetype
 * -----------------
 *
 * Maintain only the best K candidates while scanning all unique
 * states.
 *
 * Core Invariant
 * --------------
 *
 * The heap always contains the current K most frequent transactions
 * seen so far.
 *
 * The smallest frequency inside the heap represents the weakest
 * accepted candidate.
 *
 * Every future candidate only has to beat this weakest candidate.
 *
 * Why It Works
 * ------------
 *
 * Every transaction contributes exactly one frequency after the
 * counting phase.
 *
 * While iterating through unique transactions:
 *
 * - if heap size < K
 *      accept immediately
 *
 * - otherwise
 *      compare against weakest accepted frequency
 *
 * If stronger:
 *      remove weakest
 *      insert stronger
 *
 * Otherwise:
 *      discard immediately.
 *
 * Therefore no unnecessary candidates survive.
 *
 * Recognition Signals
 * -------------------
 *
 * Use this pattern when the question contains phrases like:
 *
 * - top K
 * - most frequent
 * - highest occurring
 * - K largest
 * - K best
 * - maintain best candidates
 *
 * Especially when:
 *
 * n is large
 *
 * and
 *
 * K << n.
 *
 * When NOT To Use
 * ---------------
 *
 * Do not use a heap if:
 *
 * - full ordering is required
 * - K equals number of unique elements
 * - frequencies must remain dynamically updated after every query
 *
 * Comparison
 * ----------
 *
 * HashMap + Sorting
 *
 * Time:
 *
 * O(U log U)
 *
 * where U is number of unique transactions.
 *
 * HashMap + Min Heap
 *
 * Time:
 *
 * O(U log K)
 *
 * Better whenever
 *
 * K << U.
 *
 * Bucket Sort
 *
 * Time:
 *
 * O(n)
 *
 * Faster asymptotically,
 * but requires bucket allocation proportional to input size.
 *
 * Heap solution is usually the preferred interview solution because
 * it generalizes naturally to many Top-K problems.
 *
 * ================================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ================================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine interviewing candidates.
 *
 * The interview room has only K chairs.
 *
 * Every new candidate arrives with a score
 * (= frequency).
 *
 * If chairs remain:
 *
 * let them enter.
 *
 * Otherwise compare against the weakest candidate currently seated.
 *
 * If stronger:
 *
 * weakest leaves
 * stronger enters.
 *
 * Otherwise ignore the candidate forever.
 *
 * Eventually only the strongest K remain.
 *
 * -------------------------
 * Invariant 1
 * -------------------------
 *
 * Heap size never exceeds K.
 *
 * -------------------------
 * Invariant 2
 * -------------------------
 *
 * Heap root is always the weakest accepted transaction.
 *
 * Therefore replacement is O(log K).
 *
 * -------------------------
 * Invariant 3
 * -------------------------
 *
 * Every transaction outside the heap has frequency
 * <= heap minimum
 * at the moment it was rejected.
 *
 * Therefore it can never belong to the final answer.
 *
 * -------------------------
 * Variable Meaning
 * -------------------------
 *
 * frequency
 *
 * Maps transaction
 * ->
 * occurrence count.
 *
 * heap
 *
 * Current best K transactions.
 *
 * heap.peek()
 *
 * Weakest accepted transaction.
 *
 * -------------------------
 * Allowed Transition
 * -------------------------
 *
 * Candidate arrives
 *
 * Compare with weakest accepted.
 *
 * Replace only if stronger.
 *
 * -------------------------
 * Forbidden Transition
 * -------------------------
 *
 * Never remove a stronger transaction before removing the weakest.
 *
 * Otherwise the invariant collapses.
 *
 * -------------------------
 * Termination
 * -------------------------
 *
 * After every unique transaction has been processed,
 * no unseen candidate remains.
 *
 * Therefore heap contains exactly the desired answer.
 *
 * -------------------------
 * Why Naive Sorting Fails
 * -------------------------
 *
 * A common implementation:
 *
 * Count frequencies.
 *
 * Sort every unique transaction.
 *
 * Complexity:
 *
 * O(U log U)
 *
 * Even if
 *
 * K = 3
 *
 * and
 *
 * U = 1,000,000.
 *
 * Nearly all sorting work is unnecessary.
 *
 * ================================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ================================================================
 *
 * Mistake 1
 * ---------
 *
 * Using a max heap.
 *
 * Why it seems correct:
 *
 * Highest frequencies naturally appear first.
 *
 * Problem:
 *
 * Removing one element after every insertion ejects the strongest
 * transaction instead of the weakest.
 *
 * Violated invariant:
 *
 * Heap root must be weakest accepted.
 *
 * ------------------------------------------------
 *
 * Mistake 2
 * ---------
 *
 * Heap stores every transaction.
 *
 * Works correctly.
 *
 * But complexity becomes
 *
 * O(U log U).
 *
 * Top-K advantage disappears.
 *
 * ------------------------------------------------
 *
 * Mistake 3
 * ---------
 *
 * Comparing transaction names instead of frequencies.
 *
 * Counterexample:
 *
 * A -> 100
 *
 * Z -> 2
 *
 * Lexicographic comparison produces the wrong answer.
 *
 * ------------------------------------------------
 *
 * Mistake 4
 * ---------
 *
 * Forgetting to count frequencies before heap processing.
 *
 * The heap invariant depends on final frequencies,
 * not streaming appearances.
 *
 * ================================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ================================================================
 *
 * Typing Order
 * ------------
 *
 * 1.
 * Create frequency map.
 *
 * 2.
 * Create min heap ordered by frequency.
 *
 * 3.
 * Iterate over map entries.
 *
 * 4.
 * If heap not full
 *      push.
 *
 * 5.
 * Else compare against heap root.
 *
 * 6.
 * Replace weakest if current is stronger.
 *
 * 7.
 * Extract heap into list.
 *
 * 8.
 * Reverse result because extraction happens from weakest to strongest.
 *
 * ================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ================================================================
 *
 * count frequencies
 *
 * create min heap
 *
 * for every unique transaction
 *
 *     if heap not full
 *         insert
 *
 *     else if stronger
 *         remove weakest
 *         insert current
 *
 * extract answer
 *
 * reverse
 *
 * return
 *
 * ================================================================
 * 6. SOLUTION CLASSES
 * ================================================================
 */
public class TopKFrequentTransactions {

    /**
     * Shared record used by heap-based solutions.
     */
    static class Node {
        final String transaction;
        final int frequency;

        Node(String transaction, int frequency) {
            this.transaction = transaction;
            this.frequency = frequency;
        }
    }

    /**
     * ============================================================
     * Brute Force
     * ============================================================
     *
     * Idea
     * ----
     *
     * Count frequencies.
     *
     * Sort every unique transaction by descending frequency.
     *
     * Return first K.
     *
     * Invariant
     * ---------
     *
     * Entire unique set remains globally ordered.
     *
     * Limitation
     * ----------
     *
     * Performs unnecessary ordering.
     *
     * Complexity
     * ----------
     *
     * Time:
     * O(U log U)
     *
     * Space:
     * O(U)
     *
     * Interview Usefulness
     * --------------------
     *
     * Good baseline.
     * Easy to derive.
     */
    static class BruteForce {

        List<String> topKFrequent(List<String> transactions, int k) {

            Map<String, Integer> frequency = new HashMap<>();

            for (String tx : transactions) {
                frequency.merge(tx, 1, Integer::sum);
            }

            List<Map.Entry<String, Integer>> entries =
                    new ArrayList<>(frequency.entrySet());

            entries.sort((a, b) ->
                    Integer.compare(b.getValue(), a.getValue()));

            List<String> answer = new ArrayList<>();

            for (int i = 0; i < k; i++) {
                answer.add(entries.get(i).getKey());
            }

            return answer;
        }
    }

    /**
     * ============================================================
     * Improved
     * ============================================================
     *
     * Idea
     * ----
     *
     * Bucket Sort.
     *
     * Frequency becomes the bucket index.
     *
     * Invariant
     * ---------
     *
     * Bucket i stores every transaction occurring exactly i times.
     *
     * Improvement
     * -----------
     *
     * Eliminates sorting.
     *
     * Complexity
     * ----------
     *
     * Time:
     * O(n)
     *
     * Space:
     * O(n)
     *
     * Interview Usefulness
     * --------------------
     *
     * Excellent when maximum possible frequency equals input size.
     */
    static class ImprovedBucketSort {

        List<String> topKFrequent(List<String> transactions, int k) {

            Map<String, Integer> frequency = new HashMap<>();

            for (String tx : transactions) {
                frequency.merge(tx, 1, Integer::sum);
            }

            List<List<String>> buckets =
                    new ArrayList<>(transactions.size() + 1);

            for (int i = 0; i <= transactions.size(); i++) {
                buckets.add(new ArrayList<>());
            }

            for (Map.Entry<String, Integer> entry : frequency.entrySet()) {
                buckets.get(entry.getValue()).add(entry.getKey());
            }

            List<String> answer = new ArrayList<>();

            for (int freq = buckets.size() - 1;
                 freq >= 0 && answer.size() < k;
                 freq--) {

                for (String tx : buckets.get(freq)) {

                    answer.add(tx);

                    if (answer.size() == k) {
                        return answer;
                    }
                }
            }

            return answer;
        }
    }

    /**
     * ============================================================
     * Optimal (Interview Preferred)
     * ============================================================
     *
     * Idea
     * ----
     *
     * Count frequencies once.
     *
     * Maintain only the current best K transactions inside a
     * min heap.
     *
     * The heap never grows beyond K.
     *
     * 🟢 Invariant
     * ------------
     *
     * The heap always contains the K strongest transactions
     * processed so far.
     *
     * The root is always the weakest among the accepted K.
     *
     * Every replacement strengthens the heap.
     *
     * Correctness
     * -----------
     *
     * Suppose a transaction is rejected.
     *
     * At rejection time,
     * its frequency is not larger than the weakest accepted one.
     *
     * Therefore replacing any accepted transaction with it would
     * never improve the final answer.
     *
     * Thus every rejection is permanently safe.
     *
     * Complexity
     * ----------
     *
     * Counting:
     *
     * O(n)
     *
     * Heap processing:
     *
     * O(U log K)
     *
     * Extraction:
     *
     * O(K log K)
     *
     * Total:
     *
     * O(n + U log K)
     *
     * Space:
     *
     * O(U + K)
     *
     * Interview Usefulness
     * --------------------
     *
     * This is the canonical Top-K interview solution because the
     * invariant transfers directly to:
     *
     * • top K words
     * • top K customers
     * • top K products
     * • top K scores
     * • top K logs
     * • streaming top K (with modified counting)
     */
    static class Optimal {

        record Node(String transaction, int frequency) {}

        List<String> topKFrequent(List<String> transactions, int k) {

            if (k <= 0) {
                return List.of();
            }

            // 1. Count final frequencies.
            Map<String, Integer> frequency = new HashMap<>();

            for (String tx : transactions) {
                frequency.merge(tx, 1, Integer::sum);
            }

            // 2. Min-heap:
            // root = weakest member of current Top K.
            PriorityQueue<Node> heap =
                    new PriorityQueue<>(
                            Comparator.comparingInt(Node::frequency)
                    );

            // 3. Keep only the best K candidates.
            for (Map.Entry<String, Integer> entry : frequency.entrySet()) {

                heap.offer(new Node(entry.getKey(), entry.getValue()));

                // If we temporarily have K + 1 candidates,
                // remove the weakest one.
                if (heap.size() > k) {
                    heap.poll();
                }
            }

            // 4. Heap gives weakest -> strongest.
            List<String> answer = new ArrayList<>();

            while (!heap.isEmpty()) {
                answer.add(heap.poll().transaction());
            }

            // 5. Return strongest -> weakest.
            Collections.reverse(answer);

            return answer;
        }
    }

/**
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * If asked:
 *
 * "Why does the heap work?"
 *
 * Answer:
 *
 * We never need the entire ordering.
 *
 * We only care about the strongest K transactions.
 *
 * Therefore I keep exactly K candidates.
 *
 * The heap root is intentionally the weakest accepted
 * transaction because every future comparison needs only one
 * question:
 *
 * "Is this new candidate stronger than the weakest accepted?"
 *
 * If yes,
 * replace.
 *
 * Otherwise discard forever.
 *
 * ------------------------------------------------------------
 *
 * Invariant
 * ---------
 *
 * Heap always stores the current best K transactions.
 *
 * ------------------------------------------------------------
 *
 * Search Space
 * ------------
 *
 * Every unique transaction.
 *
 * ------------------------------------------------------------
 *
 * Discard Rule
 * ------------
 *
 * Any transaction whose frequency is less than or equal to the
 * heap minimum cannot improve the answer.
 *
 * ------------------------------------------------------------
 *
 * Correctness
 * -----------
 *
 * Every replacement strictly improves the weakest accepted
 * frequency.
 *
 * Therefore the heap monotonically becomes stronger.
 *
 * ------------------------------------------------------------
 *
 * Termination
 * -----------
 *
 * Once every unique transaction has been evaluated,
 * no unseen candidate exists.
 *
 * Therefore the heap is final.
 *
 * ------------------------------------------------------------
 *
 * In-place Feasibility
 * --------------------
 *
 * Impossible.
 *
 * Frequency counting fundamentally requires additional storage.
 *
 * ------------------------------------------------------------
 *
 * Streaming Feasibility
 * ---------------------
 *
 * Exact streaming is difficult because frequencies change after
 * each arrival.
 *
 * Approximate streaming requires algorithms such as:
 *
 * • Count-Min Sketch
 * • Misra-Gries
 * • Space Saving
 *
 * ------------------------------------------------------------
 *
 * When NOT To Use
 * ---------------
 *
 * Avoid this pattern if:
 *
 * • every element must be globally sorted
 * • K equals every unique element
 * • order depends on multiple changing dimensions
 *
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 *
 * "Top K"
 *
 * "Most Frequent"
 *
 * "Largest K"
 *
 * ------------------------------------------------------------
 *
 * Pattern
 * -------
 *
 * Frequency Map
 * +
 * Min Heap
 *
 * ------------------------------------------------------------
 *
 * Invariant
 * ---------
 *
 * Heap stores only the strongest K candidates.
 *
 * ------------------------------------------------------------
 *
 * Search Target
 * -------------
 *
 * Every unique transaction exactly once.
 *
 * ------------------------------------------------------------
 *
 * Discard Rule
 * ------------
 *
 * Reject every transaction that cannot beat the heap root.
 *
 * ------------------------------------------------------------
 *
 * Common Trap
 * -----------
 *
 * Using a max heap.
 *
 * ------------------------------------------------------------
 *
 * Edge Cases
 * ----------
 *
 * k = 1
 *
 * k = number of unique transactions
 *
 * only one transaction
 *
 * all frequencies equal
 *
 * one dominant transaction
 *
 * ------------------------------------------------------------
 *
 * One-Liner
 * ---------
 *
 * "Protect only the best K by keeping the weakest accepted
 * transaction on top."
 *
 * ------------------------------------------------------------
 *
 * Re-derivation Cue
 * -----------------
 *
 * Ask:
 *
 * "Which accepted transaction should be easiest to evict?"
 *
 * Answer:
 *
 * The weakest accepted one.
 */

/**
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * ------------------------------------------------------------
 * Variation 1
 * ------------------------------------------------------------
 *
 * Top K Least Frequent Transactions
 *
 * Pattern
 * -------
 *
 * Frequency Map
 * +
 * Max Heap
 *
 * Reasoning Change
 * ----------------
 *
 * Now we wish to preserve the smallest frequencies.
 *
 * Therefore the heap root should become the largest accepted
 * frequency.
 *
 * New Invariant
 * -------------
 *
 * Heap always contains the K least frequent transactions.
 *
 * ------------------------------------------------------------
 * Variation 2
 * ------------------------------------------------------------
 *
 * Lexicographic Tie Breaking
 *
 * Example
 * -------
 *
 * Same frequency:
 *
 * TX01
 * TX02
 *
 * Smaller identifier should appear first.
 *
 * Comparator
 * ----------
 *
 * Frequency ascending.
 *
 * If equal:
 *
 * Identifier descending inside the heap.
 *
 * Why?
 *
 * Because the heap removes the weakest candidate.
 *
 * Under equal frequency,
 * lexicographically larger should become weaker so the smaller
 * identifier survives.
 *
 * Comparator:
 *
 * (a, b) -> {
 *
 * if (a.frequency != b.frequency)
 *     return a.frequency - b.frequency;
 *
 * return b.transaction.compareTo(a.transaction);
 *
 * }
 *
 * ------------------------------------------------------------
 * Variation 3
 * ------------------------------------------------------------
 *
 * Bucket Sort
 *
 * Pattern
 * -------
 *
 * Frequency becomes an array index.
 *
 * Why It Works
 * ------------
 *
 * Maximum possible frequency is n.
 *
 * Therefore every frequency has a natural bucket.
 *
 * Complexity
 * ----------
 *
 * O(n)
 *
 * Trade-off
 * ---------
 *
 * Additional memory proportional to input size.
 *
 * ------------------------------------------------------------
 * Variation 4
 * ------------------------------------------------------------
 *
 * Streaming Transactions
 *
 * Incoming transactions never stop.
 *
 * Exact frequency ordering continuously changes.
 *
 * Heap invariant alone becomes insufficient because accepted
 * candidates may later increase.
 *
 * Typical Alternatives
 * --------------------
 *
 * • Count-Min Sketch
 * • Misra-Gries
 * • Heavy Hitters
 *
 * ------------------------------------------------------------
 * Variation 5
 * ------------------------------------------------------------
 *
 * Top K By Revenue
 *
 * Replace:
 *
 * frequency
 *
 * with
 *
 * accumulated revenue.
 *
 * Invariant remains identical.
 *
 * Only the scoring function changes.
 *
 * ------------------------------------------------------------
 * Variation 6
 * ------------------------------------------------------------
 *
 * Top K Customers
 *
 * Aggregate:
 *
 * Customer
 * ->
 * Purchase Count
 *
 * Same invariant.
 *
 * Same heap.
 *
 * Same correctness proof.
 *
 * ------------------------------------------------------------
 * Pattern Boundary
 * ------------------------------------------------------------
 *
 * The pattern breaks when:
 *
 * • ranking depends on future information
 * • comparison is not transitive
 * • score cannot be finalized before insertion
 * • ordering depends on pairwise interactions
 *
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * □ Can I define the invariant?
 *
 * Heap always stores the strongest K transactions.
 *
 * ------------------------------------------------------------
 *
 * □ Can I identify the search space?
 *
 * Every unique transaction.
 *
 * ------------------------------------------------------------
 *
 * □ Can I explain why the root is weakest?
 *
 * Because every new candidate needs only one comparison.
 *
 * ------------------------------------------------------------
 *
 * □ Can I derive the discard rule?
 *
 * Reject every transaction that cannot beat heap minimum.
 *
 * ------------------------------------------------------------
 *
 * □ Can I explain termination?
 *
 * Every unique transaction has been evaluated exactly once.
 *
 * ------------------------------------------------------------
 *
 * □ Can I explain naive failure?
 *
 * Sorting everything performs unnecessary work.
 *
 * ------------------------------------------------------------
 *
 * □ Can I identify edge cases?
 *
 * Empty input
 *
 * One transaction
 *
 * k == 1
 *
 * k == unique count
 *
 * Equal frequencies
 *
 * ------------------------------------------------------------
 *
 * □ Can I debug confidently?
 *
 * Verify:
 *
 * Heap size never exceeds K.
 *
 * Root always has smallest accepted frequency.
 *
 * Frequency map built before heap processing.
 *
 * Reverse answer after extraction.
 *
 * ------------------------------------------------------------
 *
 * □ Can I adapt this pattern?
 *
 * Yes.
 *
 * Replace frequency with any comparable score.
 *
 * ------------------------------------------------------------
 *
 * □ Do I know the pattern boundary?
 *
 * Yes.
 *
 * Heap assumes finalized comparable scores.
 *
 * ============================================================
 * ⚫ PATTERN MAPPING
 * ============================================================
 *
 * Frequency Counting
 *        │
 *        ▼
 * Aggregate Score
 *        │
 *        ▼
 * Visit Each Unique State Once
 *        │
 *        ▼
 * Keep Only Best K
 *        │
 *        ▼
 * Heap Root Represents Weakest Accepted State
 *        │
 *        ▼
 * Replace Only When Stronger Candidate Arrives
 *        │
 *        ▼
 * Final Heap Contains Desired Top K
 *
 * ============================================================
 * 🔍 DEBUGGING GUIDE
 * ============================================================
 *
 * Symptom
 * -------
 *
 * Returned more than K transactions.
 *
 * Check
 * -----
 *
 * Heap size condition.
 *
 * It should never exceed K.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 *
 * Small frequencies appearing in answer.
 *
 * Check
 * -----
 *
 * Comparator direction.
 *
 * Heap must be a min heap.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 *
 * Strongest transaction missing.
 *
 * Check
 * -----
 *
 * Replacement condition.
 *
 * It should be:
 *
 * current.frequency > heap.peek().frequency
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 *
 * Answer appears reversed.
 *
 * Check
 * -----
 *
 * Heap extraction is ascending.
 *
 * Reverse before returning.
 *
 * ------------------------------------------------------------
 *
 * Symptom
 * -------
 *
 * Heap behaves unpredictably.
 *
 * Check
 * -----
 *
 * Frequency map must be completed before heap iteration.
 *
 * ============================================================
 * ⚡ IMPLEMENTATION RECONSTRUCTION
 * ============================================================
 *
 * Step 1
 * ------
 *
 * Build frequency map.
 *
 * Step 2
 * ------
 *
 * Create min heap ordered by frequency.
 *
 * Step 3
 * ------
 *
 * Iterate through unique transactions.
 *
 * Step 4
 * ------
 *
 * Fill heap until size K.
 *
 * Step 5
 * ------
 *
 * Compare against weakest accepted transaction.
 *
 * Step 6
 * ------
 *
 * Replace only if stronger.
 *
 * Step 7
 * ------
 *
 * Extract.
 *
 * Step 8
 * ------
 *
 * Reverse.
 *
 * Return.
 */

    /**
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     *
     * Run with assertions enabled:
     *
     * java -ea TopKFrequentTransactions
     */
    public static void main(String[] args) {

        Optimal solver = new Optimal();

        // --------------------------------------------------------
        // Representative example.
        // --------------------------------------------------------
        {
            List<String> input =
                    Arrays.asList("A", "B", "A", "C", "B", "A");

            List<String> answer =
                    solver.topKFrequent(input, 2);

            assert answer.size() == 2
                    : "Exactly two transactions expected.";

            assert answer.get(0).equals("A")
                    : "Most frequent transaction should be A.";

            assert answer.get(1).equals("B")
                    : "Second most frequent transaction should be B.";
        }

        // --------------------------------------------------------
        // Single transaction.
        // --------------------------------------------------------
        {
            List<String> input =
                    Collections.singletonList("TX1");

            List<String> answer =
                    solver.topKFrequent(input, 1);

            assert answer.equals(Collections.singletonList("TX1"))
                    : "Single transaction should be returned.";
        }

        // --------------------------------------------------------
        // k equals number of unique transactions.
        // --------------------------------------------------------
        {
            List<String> input =
                    Arrays.asList(
                            "A", "B", "C",
                            "A", "B",
                            "A"
                    );

            List<String> answer =
                    solver.topKFrequent(input, 3);

            assert answer.size() == 3
                    : "Every unique transaction should appear.";

            Set<String> expected =
                    new HashSet<>(Arrays.asList("A", "B", "C"));

            assert expected.equals(new HashSet<>(answer))
                    : "Returned transactions should match unique set.";
        }

        // --------------------------------------------------------
        // Dominant transaction.
        // --------------------------------------------------------
        {
            List<String> input =
                    Arrays.asList(
                            "X", "X", "X", "X", "X",
                            "Y",
                            "Z"
                    );

            List<String> answer =
                    solver.topKFrequent(input, 1);

            assert answer.size() == 1;

            assert answer.get(0).equals("X")
                    : "Dominant transaction should survive.";
        }

        // --------------------------------------------------------
        // Equal frequencies.
        // Order is intentionally not asserted.
        // --------------------------------------------------------
        {
            List<String> input =
                    Arrays.asList(
                            "A",
                            "B",
                            "C",
                            "D"
                    );

            List<String> answer =
                    solver.topKFrequent(input, 2);

            assert answer.size() == 2
                    : "Should return exactly K transactions.";

            Set<String> all =
                    new HashSet<>(Arrays.asList("A", "B", "C", "D"));

            assert all.contains(answer.get(0));

            assert all.contains(answer.get(1));

            assert !answer.get(0).equals(answer.get(1))
                    : "Transactions must be distinct.";
        }

        // --------------------------------------------------------
        // Larger frequency distribution.
        // --------------------------------------------------------
        {
            List<String> input = Arrays.asList(
                    "TX1", "TX1", "TX1", "TX1",
                    "TX2", "TX2", "TX2",
                    "TX3", "TX3",
                    "TX4",
                    "TX5", "TX5", "TX5", "TX5", "TX5"
            );

            List<String> answer =
                    solver.topKFrequent(input, 3);

            assert answer.size() == 3;

            Set<String> expected =
                    new HashSet<>(
                            Arrays.asList(
                                    "TX5",
                                    "TX1",
                                    "TX2"
                            )
                    );

            assert expected.equals(new HashSet<>(answer))
                    : "Top three frequent transactions incorrect.";
        }

        // --------------------------------------------------------
        // Stress sanity check.
        // --------------------------------------------------------
        {
            List<String> input = new ArrayList<>();

            for (int i = 0; i < 1000; i++) {
                input.add("HOT");
            }

            for (int i = 0; i < 500; i++) {
                input.add("WARM");
            }

            for (int i = 0; i < 100; i++) {
                input.add("COLD");
            }

            List<String> answer =
                    solver.topKFrequent(input, 2);

            assert answer.size() == 2;

            assert answer.get(0).equals("HOT")
                    : "Highest frequency must appear first.";

            assert answer.get(1).equals("WARM")
                    : "Second highest frequency must appear second.";
        }

        System.out.println("All assertions passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/
