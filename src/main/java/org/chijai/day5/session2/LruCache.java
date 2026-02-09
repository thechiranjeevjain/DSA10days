package org.chijai.day5.session2;

/**
 * ===============================================================
 * 📘 INVARIANT-FIRST ALGORITHM CHAPTER
 * Problem: LRU Cache
 * ===============================================================
 * <p>
 * This file is designed to be a:
 * - reusable algorithm textbook chapter
 * - long-term reference
 * - interview-ready derivation guide
 * - correctness-proof document
 * <p>
 * Follow top-to-bottom. Nothing is accidental.
 */

import java.util.HashMap;
import java.util.Map;

public class LruCache {

    /* =============================================================
     * 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
     * =============================================================
     *
     * 🔗 https://leetcode.com/problems/lru-cache/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Hash Table, Linked List, Design
     *
     * -------------------------------------------------------------
     * Design a data structure that follows the constraints of a
     * Least Recently Used (LRU) cache.
     *
     * Implement the LRUCache class:
     *
     * LRUCache(int capacity)
     *     Initialize the LRU cache with positive size capacity.
     *
     * int get(int key)
     *     Return the value of the key if the key exists, otherwise
     *     return -1.
     *
     * void put(int key, int value)
     *     Update the value of the key if the key exists. Otherwise,
     *     add the key-value pair to the cache. If the number of keys
     *     exceeds the capacity from this operation, evict the least
     *     recently used key.
     *
     * The functions get and put must each run in O(1) average time
     * complexity.
     *
     * -------------------------------------------------------------
     * Example 1:
     *
     * Input
     * ["LRUCache","put","put","get","put","get","put","get","get","get"]
     * [[2],[1,1],[2,2],[1],[3,3],[2],[4,4],[1],[3],[4]]
     *
     * Output
     * [null,null,null,1,null,-1,null,-1,3,4]
     *
     * Explanation:
     * LRUCache lRUCache = new LRUCache(2);
     * lRUCache.put(1, 1); // cache is {1=1}
     * lRUCache.put(2, 2); // cache is {1=1, 2=2}
     * lRUCache.get(1);    // return 1
     * lRUCache.put(3, 3); // evicts key 2
     * lRUCache.get(2);    // return -1
     * lRUCache.put(4, 4); // evicts key 1
     * lRUCache.get(1);    // return -1
     * lRUCache.get(3);    // return 3
     * lRUCache.get(4);    // return 4
     *
     * -------------------------------------------------------------
     * Constraints:
     * 1 <= capacity <= 3000
     * 0 <= key <= 10^4
     * 0 <= value <= 10^5
     * At most 2 * 10^5 calls to get and put.
     */

    /* =============================================================
     * 3️⃣ 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
     * =============================================================
     *
     * 🔵 Pattern Name:
     * Hash Map + Doubly Linked List (Recency Ordering Pattern)
     *
     * 🔵 Problem Archetype:
     * O(1) constrained eviction with temporal ordering
     *
     * 🟢 Core Invariant (MANDATORY — ONE SENTENCE):
     * The doubly linked list always represents keys in strict
     * descending order of recency, with the head as most-recently
     * used and the tail as least-recently used.
     *
     * 🟡 Why This Invariant Makes the Pattern Work:
     * - HashMap gives O(1) access to nodes
     * - Linked list gives O(1) removal and insertion
     * - Recency is encoded structurally, not computed
     *
     * 🧭 When This Pattern Applies:
     * - Need O(1) get + put
     * - Need eviction by usage history
     * - Capacity is bounded
     *
     * 🧭 Pattern Recognition Signals:
     * - "Least Recently Used"
     * - "O(1) average time"
     * - "Evict oldest / stale / inactive"
     *
     * 🔵 How This Differs from Similar Patterns:
     * - NOT sliding window (no fixed indices)
     * - NOT priority queue (ordering must mutate on access)
     * - NOT TreeMap (O(log n) unacceptable)
     */

    /* =============================================================
     * 4️⃣ 🟢 MENTAL MODEL & INVARIANTS (SOURCE OF TRUTH)
     * =============================================================
     *
     * 🟢 Mental Model (Think, Not Code):
     * Imagine a row of cards.
     * - Touch a card → move it to the front
     * - Shelf full? → remove the card at the back
     *
     * The list IS the truth of recency.
     *
     * -------------------------------------------------------------
     * 🟢 State Representation:
     * - Map<Integer, Node> index:
     *     Maps key → exact node in list
     *
     * - Doubly Linked List:
     *     Head = most recently used
     *     Tail = least recently used
     *
     * -------------------------------------------------------------
     * 🟢 Invariants (EXPLICIT):
     *
     * I1. Every key in the cache appears exactly once in the list.
     * I2. Head.next is always the most recently accessed key.
     * I3. Tail.prev is always the least recently accessed key.
     * I4. Any get/put of an existing key moves its node to head.
     * I5. Size never exceeds capacity.
     *
     * -------------------------------------------------------------
     * 🟡 Allowed Moves (Invariant-Preserving):
     * - Remove a node from anywhere in O(1)
     * - Insert a node right after head in O(1)
     *
     * 🔴 Forbidden Moves (Invariant-Breaking):
     * - Traversing list to find LRU (O(n))
     * - Recomputing recency on demand
     * - Leaving accessed node in-place
     *
     * -------------------------------------------------------------
     * 🟡 Termination Logic:
     * Each operation performs a constant number of pointer updates.
     * No loops. No recursion. Always terminates.
     *
     * -------------------------------------------------------------
     * 🔴 Why Common Alternatives Are Inferior:
     * - ArrayList: O(n) deletion
     * - PriorityQueue: cannot update priority in O(1)
     * - HashMap alone: no ordering
     */

    /* =============================================================
     * 5️⃣ 🔴 WHY NAIVE / WRONG SOLUTIONS FAIL (FORENSIC)
     * =============================================================
     *
     * ❌ Wrong Approach #1: HashMap + Timestamp
     *
     * Why it seems correct:
     * - Track last-used time
     *
     * Why it fails:
     * - Eviction requires scanning all entries → O(n)
     *
     * Invariant Violated:
     * - Recency must be structural, not computed
     *
     * Minimal Counterexample:
     * capacity = 3000
     * 3000 inserts → eviction scan every time
     *
     * Interviewer Trap:
     * "How do you evict in O(1)?"
     *
     * -------------------------------------------------------------
     * ❌ Wrong Approach #2: Queue of Keys
     *
     * Why it seems correct:
     * - FIFO resembles LRU
     *
     * Why it fails:
     * - Accessed key must move forward
     * - Removing from middle is O(n)
     *
     * Invariant Violated:
     * - List must support arbitrary removal
     *
     * -------------------------------------------------------------
     * ❌ Wrong Approach #3: LinkedList + contains()
     *
     * Why it fails:
     * - contains() is O(n)
     *
     * Interviewer Signal:
     * If you say "LinkedList" without HashMap → red flag
     */

    /*
     * ⚠️ Pattern Boundary — Stack-Based LRU Is Impossible
     *
     * Any correct LRU implementation requires:
     * - O(1) arbitrary deletion
     * - O(1) arbitrary reinsertion
     *
     * A stack only supports top-based operations.
     * Therefore, stack-based LRU implementations
     * either:
     * - degrade to O(n), or
     * - violate the recency invariant.
     *
     * Conclusion:
     * If a solution uses a stack for recency,
     * it is either incorrect or non-O(1).
     */


    /* =============================================================
     * 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES
     * (DERIVED STRICTLY FROM THE INVARIANT)
     * =============================================================
     */

    /* =============================================================
     * 🔴 BRUTE FORCE SOLUTION
     * =============================================================
     *
     * 🔴 Core Idea:
     * Store key-value pairs and track recency manually.
     *
     * 🔴 What It Enforces:
     * - Correctness of values
     *
     * 🔴 What It Fails To Enforce:
     * - O(1) eviction
     * - Structural recency invariant
     *
     * 🔴 Time Complexity:
     * - get: O(n)
     * - put: O(n)
     *
     * 🔴 Space Complexity:
     * - O(n)
     *
     * 🔴 Interview Preference:
     * ❌ Never acceptable beyond warm-up discussion
     */
    static class LRUCacheBruteForce {

        private final int capacity;

        // Stores keys in access order (most recent at end)
        private final java.util.List<Integer> recencyList = new java.util.ArrayList<>();

        private final Map<Integer, Integer> keyToValue = new HashMap<>();

        LRUCacheBruteForce(int capacity) {
            this.capacity = capacity;
        }

        int get(int key) {
            if (!keyToValue.containsKey(key)) {
                return -1;
            }

            // ❌ O(n): remove from middle
            recencyList.remove((Integer) key);
            recencyList.add(key); // move to most recent

            return keyToValue.get(key);
        }

        void put(int key, int value) {
            if (keyToValue.containsKey(key)) {
                // Update value and recency
                recencyList.remove((Integer) key);
            } else if (keyToValue.size() == capacity) {
                // ❌ O(n): eviction by scanning order list
                int lruKey = recencyList.remove(0);
                keyToValue.remove(lruKey);
            }

            recencyList.add(key);
            keyToValue.put(key, value);
        }
    }

    /* =============================================================
     * 🟡 IMPROVED SOLUTION
     * =============================================================
     *
     * 🟡 Core Idea:
     * Use HashMap + custom doubly linked list
     * but without sentinel nodes.
     *
     * 🟡 What It Enforces:
     * - O(1) access
     * - O(1) updates
     *
     * 🟡 What It Risks:
     * - Edge-case bugs (null head/tail handling)
     *
     * 🟡 Time Complexity:
     * - get: O(1)
     * - put: O(1)
     *
     * 🟡 Space Complexity:
     * - O(n)
     *
     * 🟡 Interview Preference:
     * ⚠️ Acceptable but fragile
     */
    static class LRUCacheImproved {

        private static class Node {
            int key;
            int value;
            Node prev;
            Node next;

            Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }

        private final int capacity;
        private final Map<Integer, Node> index = new HashMap<>();
        private Node head;
        private Node tail;

        LRUCacheImproved(int capacity) {
            this.capacity = capacity;
        }

        int get(int key) {
            Node node = index.get(key);
            if (node == null) {
                return -1;
            }

            moveToHead(node);
            return node.value;
        }

        void put(int key, int value) {
            Node node = index.get(key);

            if (node != null) {
                node.value = value;
                moveToHead(node);
                return;
            }

            Node newNode = new Node(key, value);
            index.put(key, newNode);
            addToHead(newNode);

            if (index.size() > capacity) {
                // ❌ Edge-case prone eviction
                index.remove(tail.key);
                removeNode(tail);
            }
        }

        // ===== Linked List Operations =====

        private void moveToHead(Node node) {
            removeNode(node);
            addToHead(node);
        }

        private void addToHead(Node node) {
            node.prev = null;
            node.next = head;

            if (head != null) {
                head.prev = node;
            }

            head = node;

            if (tail == null) {
                tail = node;
            }
        }

        private void removeNode(Node node) {
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                head = node.next;
            }

            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                tail = node.prev;
            }
        }
    }

    /* =============================================================
     * 🟢 OPTIMAL SOLUTION (INTERVIEW-PREFERRED)
     * =============================================================
     *
     * 🟢 Core Idea:
     * HashMap + Doubly Linked List with SENTINEL NODES
     *
     * 🟢 Fully Enforces Invariant:
     * "List order == recency order at all times"
     *
     * 🟢 Why Sentinels Matter:
     * - No null checks
     * - No head/tail edge cases
     * - Invariant preserved mechanically
     *
     * 🟢 Time Complexity:
     * - get: O(1)
     * - put: O(1)
     *
     * 🟢 Space Complexity:
     * - O(capacity)
     *
     * 🟢 Interview Preference:
     * ✅ Gold standard
     */
    static class LRUCache {

        /* ---------------------------------------------------------
         * 🟢 Node definition
         * ---------------------------------------------------------
         */
        private static class Node {
            int key;
            int value;
            Node prev;
            Node next;

            Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }

        private final int capacity;

        // 🔵 O(1) index to exact node
        private final Map<Integer, Node> index = new HashMap<>();

        // 🟢 Sentinel nodes (never store real data)
        private final Node head; // Most recent is head.next
        private final Node tail; // Least recent is tail.prev

        LRUCache(int capacity) {
            this.capacity = capacity;

            // Initialize sentinels
            head = new Node(-1, -1);
            tail = new Node(-1, -1);

            head.next = tail;
            tail.prev = head;
        }

        int get(int key) {
            Node node = index.get(key);
            if (node == null) {
                return -1;
            }

            // 🟢 Access = refresh recency
            removeNode(node);
            addAfterHead(node);
            return node.value;
        }

        void put(int key, int value) {
            Node existingNode = index.get(key);

            if (existingNode != null) {
                // Update value and refresh recency
                existingNode.value = value;
                removeNode(existingNode);
                addAfterHead(existingNode);
                return; // Early return exit break
            }

            // Insert new node as most recent
            Node newNode = new Node(key, value);
            index.put(key, newNode);
            addAfterHead(newNode);

            // Enforce capacity invariant
            if (index.size() > capacity) {
                Node lruNode = tail.prev;
                removeNode(lruNode);
                index.remove(lruNode.key);
            }
        }

        /* ---------------------------------------------------------
         * 🟢 Doubly Linked List Operations
         * (Invariant-preserving primitives)
         * ---------------------------------------------------------
         */

        // Remove node from its current position
        private void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        // Insert node right after head (most recent)
        private void addAfterHead(Node node) {
            node.prev = head;
            node.next = head.next;

            head.next.prev = node;
            head.next = node;
        }
    }

    /* =============================================================
     * 7️⃣ 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
     * =============================================================
     *
     * 🟣 State the Invariant:
     * The doubly linked list always reflects strict recency order:
     * head.next is most recently used, tail.prev is least recently used.
     *
     * 🟣 Discard / Transition Logic:
     * - Every get / put touches a key → move its node to the front.
     * - When capacity is exceeded → evict tail.prev.
     *
     * 🟣 Why Correctness Is Guaranteed:
     * - HashMap guarantees O(1) access to the exact node.
     * - Linked list guarantees O(1) structural updates.
     * - No operation violates the recency ordering invariant.
     *
     * 🟣 What Breaks If Logic Changes:
     * - Not moving accessed node → stale LRU eviction.
     * - Evicting before insertion → wrong key removed.
     *
     * 🟣 In-place Feasibility:
     * - Yes, mutation is essential.
     *
     * 🟣 Streaming Feasibility:
     * - Yes, operations are local and bounded.
     *
     * 🟣 When NOT to Use This Pattern:
     * - When eviction is not usage-based.
     * - When O(log n) is acceptable and ordering is complex.
     */

    /* =============================================================
     * 8️⃣ 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
     * =============================================================
     *
     * 🟢 Invariant-Preserving Changes:
     * - Return eviction key
     * - Track hit/miss counts
     * - Add peekLeastRecent()
     *
     * 🟡 Reasoning-Only Changes:
     * - Thread-safety via synchronized blocks
     * - Statistics counters
     *
     * 🔴 Pattern-Break Signals:
     * - "Evict based on frequency" → LFU needed
     * - "Evict based on time window" → sliding window
     *
     * 🟡 Why Invariant Holds or Collapses:
     * - As long as recency == list order → holds
     * - If ordering depends on non-local data → collapses
     */

    /* =============================================================
     * 9️⃣ ⚫ REINFORCEMENT PROBLEMS — FULL SUB-CHAPTERS
     * (SAME OR INTENTIONALLY BROKEN INVARIANT)
     * =============================================================
     */

    /* =============================================================
     * 9️⃣ ⚫ REINFORCEMENT PROBLEM — LFU CACHE (FULL CHAPTER)
     * =============================================================
     *
     * 📘 Official Problem (LeetCode: LFU Cache)
     *
     * Design and implement a data structure for a Least Frequently
     * Used (LFU) cache.
     *
     * get(key): return value if exists, else -1
     * put(key, value): insert or update
     *
     * Eviction Rule:
     * - Evict the key with the LOWEST frequency
     * - If tie, evict the LEAST RECENTLY USED among them
     *
     * get and put must run in O(1) average time.
     *
     * -------------------------------------------------------------
     * 🔴 Why LRU Invariant Fails:
     * Recency alone cannot distinguish frequently-used keys.
     *
     * -------------------------------------------------------------
     * 🟢 LFU CORE INVARIANT:
     * Keys are partitioned by frequency.
     * Eviction always occurs from the minimum-frequency bucket.
     * Within a bucket, eviction follows LRU order.
     *
     * -------------------------------------------------------------
     * 🟢 State:
     * key → Node (value + frequency)
     * frequency → Doubly Linked List of nodes (LRU order)
     * minFrequency → current eviction target
     */

    /* =============================================================
     * 10️⃣ 🧩 FORMAL INVARIANT COMPARISON — LRU vs LFU
     * =============================================================
     *
     * LRU Invariant:
     * - Single list ordered by recency
     * - Eviction from tail
     *
     * LFU Invariant:
     * - Keys grouped by frequency
     * - Eviction from lowest-frequency group
     * - LRU ordering within group
     *
     * Core Difference:
     * LRU edits ORDER
     * LFU edits CATEGORY
     */

    /* =============================================================
     * 11️⃣ 🧠 REUSABLE CACHE INVARIANT FRAMEWORK
     * =============================================================
     *
     * Cache design reduces to:
     *
     * 1) What is the eviction metric?
     *    - Time      → LRU
     *    - Frequency → LFU
     *    - Arrival   → FIFO
     *
     * 2) Is the metric mutable?
     *    - No        → Queue
     *    - Yes       → Doubly Linked Structure
     *
     * 3) Are ties ordered?
     *    - Yes       → DLL inside bucket
     *
     * Structural Rule:
     * If eviction logic requires O(1) reordering,
     * recomputation-based structures (heap, stack)
     * are invalid by invariant violation.
     */


    /* =============================================================
     * Reinforcement Problem 1: First Unique Number
     * =============================================================
     *
     * 🔗 https://leetcode.com/problems/first-unique-number/
     *
     * Problem Statement (Official):
     * You have a queue of integers, you need to retrieve the first
     * unique integer in the queue.
     *
     * Implement the FirstUnique class:
     * - FirstUnique(int[] nums)
     * - int showFirstUnique()
     * - void add(int value)
     *
     * -------------------------------------------------------------
     * ⚫ Invariant Mapping:
     * The doubly linked list contains ONLY numbers that are unique,
     * in their arrival order. Head.next is the first unique number.
     *
     * -------------------------------------------------------------
     * 🔴 Why Naive Fails:
     * - Scanning counts on every query → O(n)
     *
     * -------------------------------------------------------------
     * 🟢 Java Solution (Invariant-Derived)
     */
    static class FirstUnique {

        private static class Node {
            int value;
            Node prev, next;

            Node(int value) {
                this.value = value;
            }
        }

        private final Map<Integer, Integer> frequency = new HashMap<>();
        private final Map<Integer, Node> nodeIndex = new HashMap<>();
        private final Node head = new Node(-1);
        private final Node tail = new Node(-1);

        FirstUnique(int[] nums) {
            head.next = tail;
            tail.prev = head;
            for (int num : nums) add(num);
        }

        int showFirstUnique() {
            return head.next == tail ? -1 : head.next.value;
        }

        void add(int value) {
            int count = frequency.getOrDefault(value, 0) + 1;
            frequency.put(value, count);

            if (count == 1) {
                Node node = new Node(value);
                nodeIndex.put(value, node);
                insertAtEnd(node);
            } else if (count == 2) {
                Node node = nodeIndex.remove(value);
                if (node != null) remove(node);
            }
        }

        private void insertAtEnd(Node node) {
            node.prev = tail.prev;
            node.next = tail;
            tail.prev.next = node;
            tail.prev = node;
        }

        private void remove(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }


    /* =============================================================
     * 10️⃣ 🧩 RELATED PROBLEMS — MINI INVARIANT CHAPTERS
     * =============================================================
     */

    /* =============================================================
     * Related Problem 1: Design Browser History
     * =============================================================
     *
     * 🔗 https://leetcode.com/problems/design-browser-history/
     *
     * Invariant:
     * Cursor always points to the current page in a doubly linked list.
     */
    static class BrowserHistory {

        private static class Node {
            String url;
            Node prev, next;

            Node(String url) {
                this.url = url;
            }
        }

        private Node current;

        BrowserHistory(String homepage) {
            current = new Node(homepage);
        }

        void visit(String url) {
            Node node = new Node(url);
            current.next = node;
            node.prev = current;
            current = node;
        }

        String back(int steps) {
            while (steps-- > 0 && current.prev != null) {
                current = current.prev;
            }
            return current.url;
        }

        String forward(int steps) {
            while (steps-- > 0 && current.next != null) {
                current = current.next;
            }
            return current.url;
        }
    }

    /* =============================================================
     * Related Problem 2: Moving Average from Data Stream
     * =============================================================
     *
     * 🔗 https://leetcode.com/problems/moving-average-from-data-stream/
     *
     * Invariant:
     * Queue always contains the last N elements.
     */
    static class MovingAverage {

        private final int size;
        private final java.util.Queue<Integer> window = new java.util.ArrayDeque<>();
        private double sum = 0;

        MovingAverage(int size) {
            this.size = size;
        }

        double next(int val) {
            window.add(val);
            sum += val;

            if (window.size() > size) {
                sum -= window.remove();
            }

            return sum / window.size();
        }
    }


    /* =============================================================
     * 11️⃣ 🟢 LEARNING VERIFICATION (INVARIANT-FIRST)
     * =============================================================
     *
     * 🟢 Invariant to Recall (Without Code):
     * The doubly linked list ALWAYS represents strict recency order.
     *
     * 🔴 Why Naive Approaches Fail:
     * - They recompute recency instead of preserving it structurally.
     *
     * 🟡 Bugs to Debug Intentionally:
     * - Forgetting to move node on get()
     * - Evicting before insertion
     * - Removing wrong end of list
     *
     * 🧭 Detecting This Invariant in New Problems:
     * Ask:
     * - Do I need O(1) access?
     * - Do I need O(1) eviction based on order?
     * If yes → this pattern.
     */

    /* =============================================================
     * 12️⃣ 🧪 main() METHOD + SELF-VERIFYING TESTS
     * =============================================================
     */

    public static void main(String[] args) {

        /* ---------------------------------------------------------
         * Test 1: Happy Path (Given Example)
         * ---------------------------------------------------------
         * Why this test exists:
         * - Verifies core invariant under normal operations
         */
        LRUCache cache = new LRUCache(2);

        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1, cache.get(1), "Test1-get(1)");

        cache.put(3, 3); // evicts 2
        assertEquals(-1, cache.get(2), "Test1-get(2)");

        cache.put(4, 4); // evicts 1
        assertEquals(-1, cache.get(1), "Test1-get(1)-evicted");
        assertEquals(3, cache.get(3), "Test1-get(3)");
        assertEquals(4, cache.get(4), "Test1-get(4)");

        /* ---------------------------------------------------------
         * Test 2: Capacity = 1 (Boundary Case)
         * ---------------------------------------------------------
         * Why this test exists:
         * - Forces eviction on every put
         */
        LRUCache single = new LRUCache(1);
        single.put(10, 10);
        single.put(20, 20);

        assertEquals(-1, single.get(10), "Test2-eviction");
        assertEquals(20, single.get(20), "Test2-survivor");

        /* ---------------------------------------------------------
         * Test 3: Update Existing Key
         * ---------------------------------------------------------
         * Why this test exists:
         * - Ensures update refreshes recency
         */
        LRUCache update = new LRUCache(2);
        update.put(1, 1);
        update.put(2, 2);
        update.put(1, 100); // refresh 1

        update.put(3, 3); // evicts 2
        assertEquals(-1, update.get(2), "Test3-evict-correct");
        assertEquals(100, update.get(1), "Test3-updated-value");

        /* ---------------------------------------------------------
         * Test 4: Interleaved Access Pattern (Interviewer Trap)
         * ---------------------------------------------------------
         * Why this test exists:
         * - Verifies invariant under repeated get()
         */
        LRUCache interleave = new LRUCache(2);
        interleave.put(1, 1);
        interleave.put(2, 2);
        interleave.get(1);
        interleave.get(1);
        interleave.put(3, 3); // must evict 2

        assertEquals(-1, interleave.get(2), "Test4-evict-after-gets");
        assertEquals(1, interleave.get(1), "Test4-most-recent");

        System.out.println("✅ All invariant-based tests passed.");
    }

    /* -------------------------------------------------------------
     * Simple assertion helper (no external libs)
     * -------------------------------------------------------------
     */
    private static void assertEquals(int expected, int actual, String testName) {
        if (expected != actual) {
            throw new AssertionError(
                    testName + " FAILED | expected=" + expected + ", actual=" + actual
            );
        }
    }

    /* =============================================================
     * 13️⃣ 🧠 CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
     * =============================================================
     *
     * Invariant → List order equals recency order
     * Search target → Exact node via HashMap
     * Discard rule → Evict tail.prev
     * Termination guarantee → Constant pointer operations
     * Naive failure → Recomputed recency is O(n)
     * Edge cases → capacity=1, update existing key
     * Variant readiness → LFU, history, browser nav
     * Pattern boundary → Breaks if eviction ≠ recency
     */

    /* =============================================================
     * 🧘 FINAL CLOSURE STATEMENT
     * =============================================================
     *
     * For this problem, the invariant is that the doubly linked list
     * always reflects strict recency order.
     *
     * The answer represents the value stored at the node indexed
     * by the key.
     *
     * The search terminates because each operation performs a
     * constant number of invariant-preserving pointer updates.
     *
     * I can re-derive this solution under pressure.
     *
     * This chapter is complete.
     */
}


