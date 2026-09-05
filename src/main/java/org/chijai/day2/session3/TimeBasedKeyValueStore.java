package org.chijai.day2.session3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TimeBasedKeyValueStore {

    /*
     =====================================================================================
     1. 📘 PROBLEM STATEMENT + FEW EXAMPLES
     =====================================================================================

     Design a time-based key-value store.

     set(key, value, timestamp)

         Store value for key at timestamp.

         For every individual key,
         timestamps passed to set() are STRICTLY INCREASING.

     get(key, timestamp)

         Return the value belonging to the LARGEST stored timestamp such that

             storedTimestamp <= timestamp

         If no such timestamp exists, return "".

     Constraints

         • 1 <= key.length <= 100
         • 1 <= value.length <= 100
         • 1 <= timestamp <= 10^7
         • timestamps for each key are strictly increasing
         • at most 2 * 10^5 total operations

     -------------------------------------------------------------------------------------
     EXAMPLE 1

         set("foo", "bar", 1)
         set("foo", "bar2", 4)
         set("foo", "bar3", 9)

         foo

             (1,"bar") ---- (4,"bar2") ---- (9,"bar3")

         get("foo", 7)

             largest timestamp <= 7
                        = 4

             answer = "bar2"

     -------------------------------------------------------------------------------------
     EXAMPLE 2 — BEFORE FIRST TIMESTAMP

         history

             (5,"A")

         query = 3

             no timestamp <= 3

         answer = ""

     -------------------------------------------------------------------------------------
     THE GUARANTEE THAT CHANGES THE SOLUTION

         timestamps arrive increasing
             -> append preserves sorted order
             -> no sorting / ordered insertion is needed
             -> get() can binary-search the already-sorted history
     */


    /*
     =====================================================================================
     2. 🧠 15-SECOND RETRIEVAL ANCHOR
     =====================================================================================

         KEY -> SORTED HISTORY

         SET
             GET / CREATE HISTORY
             APPEND

         GET
             FIRST timestamp > query
             STEP BACK ONE

         Binary-search boundary at termination

             [ valid valid valid | invalid invalid ]
                                 ^
                                left

         valid
             timestamp <= query

         invalid
             timestamp > query

         Therefore

             left     = first timestamp > query
             left - 1 = largest timestamp <= query

         Memory sentence

             SEARCH FOR FIRST TOO LARGE.
             ANSWER IS ONE BEFORE.

         Trap

             VALID DOES NOT MEAN FINAL.

         Constraint flip

             timestamps not increasing -> TreeMap / ordered structure
     */


    /*
     =====================================================================================
     3. 🟢 PRIMARY PHOTOGRAPHIC MEMORY SOLUTION
     =====================================================================================
     */
    static class OptimalTimeMap {

        private final Map<String, List<Entry>> store = new HashMap<>();

        private record Entry(
                int timestamp,
                String value) {
        }

        public void set(String key, String value, int timestamp) {

            List<Entry> history =
                    store.computeIfAbsent(key, ignored -> new ArrayList<>());

            history.add(new Entry(timestamp, value));
        }

        public String get(String key, int timestamp) {

            List<Entry> history = store.get(key);

            if (history == null) {
                return "";
            }

            int left = 0;
            int right = history.size();

            while (left < right) {

                int mid = left + (right - left) / 2;

                Entry current = history.get(mid);

                if (current.timestamp() <= timestamp) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            if (left == 0) {
                return "";
            }

            return history.get(left - 1).value();
        }
    }


    /*
     =====================================================================================
     4. 🧭 FIRST-PRINCIPLES INVENTION PATH
     =====================================================================================

     Start from the obstacle, not from the data structure name.

     1. One key can have many historical values.

            key -> history

        So an outer HashMap naturally answers

            WHICH KEY?

     2. get() does not ask for an exact timestamp.

        It asks for

            largest timestamp <= query

        This is a PREDECESSOR query.

     3. For each key, timestamps arrive strictly increasing.

        Therefore

            append keeps history sorted forever.

        We do NOT need

            sorting after insertion
            ordered insertion
            TreeMap maintenance

     4. A sorted random-access history suggests ArrayList + binary search.

     5. Search for a boundary that is easy to prove:

            first timestamp > query

        Then the predecessor is automatically

            left - 1

     Final structure

         HashMap<String, List<Entry>>

         set()
             get/create history
             append

         get()
             binary-search first > query
             return predecessor

     Implementation devices solve different problems

         HashMap
             -> locate the key

         ArrayList
             -> preserve append-only sorted history with O(1) random access

         Binary search
             -> locate predecessor in O(log H)
     */


    /*
     =====================================================================================
     5. 🎞 ONE CANONICAL VISUAL DRY RUN
     =====================================================================================

         history

             index        0       1       2        3
             timestamp    2       5       8       20
             value       "A"     "B"     "C"      "D"

         query = 7

         Desired answer

             timestamp 5
             value "B"

         We search for FIRST timestamp > 7.

         +------+-------+-----+----------------+----------------------+-------------+
         | left | right | mid | timestamp[mid] | meaning              | move        |
         +------+-------+-----+----------------+----------------------+-------------+
         | 0    | 4     | 2   | 8              | too large            | right = 2   |
         | 0    | 2     | 1   | 5              | valid; seek newer    | left = 2    |
         +------+-------+-----+----------------+----------------------+-------------+

         stop

             left == right == 2

         boundary picture

             [ 2   5 | 8   20 ]
                     ^
                    left

         first > 7
             index 2 -> timestamp 8

         predecessor
             index 1 -> timestamp 5

         answer
             "B"

     -------------------------------------------------------------------------------------
     WHY left = mid + 1 WHEN current <= query?

         current is VALID,
         but it may not be the NEWEST valid timestamp.

         So we deliberately move past it and search right.

         Eventually left lands one position AFTER the best valid answer.

         Hence

             answer = left - 1

     -------------------------------------------------------------------------------------
     EDGE CASES FALL OUT OF THE SAME BOUNDARY

         query before everything

             [ | 5 8 20 ]
               ^
              left = 0

             no predecessor -> ""

         query after everything

             [ 5 8 20 | ]
                       ^
                      left = size

             predecessor = size - 1 -> latest value

         exact match

             still search first > query;
             exact match becomes the predecessor of that boundary.
     */


    /*
     =====================================================================================
     6. ✅ CORRECTNESS + COMPLEXITY
     =====================================================================================

     Correctness contract

         The search returns the first index whose timestamp is > query.

         Therefore

             every index before left has timestamp <= query
             every index from left onward has timestamp > query

         So left - 1, when it exists, is exactly the largest timestamp <= query.

     Why each move is safe

         timestamp[mid] <= query
             mid is valid; only a later valid timestamp can beat it
             -> search right

         timestamp[mid] > query
             mid and everything after it are too large
             -> cut right

     Termination

         [left, right) strictly shrinks every iteration.

     Complexity

         Let H = history size for the queried key.
         Let S = total number of set operations.

         set()
             HashMap lookup + ArrayList append
             O(1) average / amortized

         get()
             HashMap lookup + binary search
             O(log H)

         total space
             O(S)
     */


    /*
     =====================================================================================
     7. 🧭 APPROACH / DATA-STRUCTURE TRADE-OFF MATRIX
     =====================================================================================

     +--------------------------------------+-------------------------------+-------------+-------------+----------------------+----------------------------------------------+
     | Approach                             | Per-key history               | set()       | get()       | Custom BS?           | Best use                                     |
     +--------------------------------------+-------------------------------+-------------+-------------+----------------------+----------------------------------------------+
     | Reverse scan baseline                | ArrayList<Entry>              | O(1) amort. | O(H)        | No                   | First correct baseline                       |
     | TreeMap predecessor                  | TreeMap<timestamp,value>      | O(log H)    | O(log H)    | No                   | Arbitrary timestamp insertion                |
     | ArrayList + manual upper bound       | ArrayList<Entry>              | O(1) amort. | O(log H)    | Yes                  | LEETCODE PRIMARY / monotonic writes          |
     | ArrayList + Collections.binarySearch | ArrayList<Entry>              | O(1) amort. | O(log H)    | Library call          | Avoid handwritten loop; API decoding cost    |
     | Encapsulated Timeline                | Timeline -> ArrayList<Entry>  | O(1) amort. | O(log H)    | Inside Timeline       | OOP / LLD responsibility separation          |
     | Concurrent append-only Timeline      | locked ArrayList<Version>     | O(1) amort. | O(log H)    | Inside Timeline       | Concurrent monotonic in-memory production    |
     | ConcurrentSkipListMap Timeline       | ordered concurrent map        | O(log H)    | O(log H)    | No                   | Concurrent out-of-order writes               |
     +--------------------------------------+-------------------------------+-------------+-------------+----------------------+----------------------------------------------+

     Primary interview distinction

         TreeMap solves the REQUIREMENT.
         ArrayList + binary search exploits the CONSTRAINT.

     Why not the other tempting structures?

         HashMap<timestamp,value>
             exact lookup is easy; predecessor relationship is absent.

         LinkedList<Entry>
             append is easy; random access makes binary search a bad fit.

         Deque<Entry>
             good for latest-only access; arbitrary historical predecessor is O(H).

         PriorityQueue<Entry>
             gives one global min/max, not predecessor around arbitrary query X.

         TreeMap<String,...> as outer map
             orders keys even though we only need exact key lookup.
     */


    /*
     =====================================================================================
     8. 🔄 RUNNABLE ALGORITHM ALTERNATIVES
     =====================================================================================
     */

    /*
     Brute baseline

         set() O(1)
         get() O(H)

         Reverse scan is correct because newest timestamps are visited first.
     */
    static class BruteForceTimeMap {

        private record Entry(
                int timestamp,
                String value) {
        }

        private final Map<String, List<Entry>> store = new HashMap<>();

        public void set(String key, String value, int timestamp) {
            store.computeIfAbsent(key, ignored -> new ArrayList<>())
                    .add(new Entry(timestamp, value));
        }

        public String get(String key, int timestamp) {

            List<Entry> history = store.get(key);

            if (history == null) {
                return "";
            }

            for (int i = history.size() - 1; i >= 0; i--) {

                Entry current = history.get(i);

                if (current.timestamp() <= timestamp) {
                    return current.value();
                }
            }

            return "";
        }
    }


    /*
     TreeMap fallback / constraint-flip solution

         floorKey(timestamp)
             -> largest stored timestamp <= query

         set() O(log H)
         get() O(log H)

         Prefer when timestamps may arrive out of order.
     */
    static class ImprovedTreeMapTimeMap {

        private final Map<String, TreeMap<Integer, String>> store = new HashMap<>();

        public void set(String key, String value, int timestamp) {

            store.computeIfAbsent(key, ignored -> new TreeMap<>())
                    .put(timestamp, value);
        }

        public String get(String key, int timestamp) {

            TreeMap<Integer, String> timeline = store.get(key);

            if (timeline == null) {
                return "";
            }

            Integer predecessor = timeline.floorKey(timestamp);

            if (predecessor == null) {
                return "";
            }

            return timeline.get(predecessor);
        }
    }


    /*
     Library binary-search alternative

         Same ArrayList representation and asymptotic complexity as primary.

         Trade-off

             less handwritten binary-search code
             but must remember Java's negative insertion-point contract:

                 result = -(insertionPoint) - 1

         That API decoding is why this is not the photographic-memory primary.
     */
    static class LibraryBinarySearchTimeMap {

        private record Entry(
                int timestamp,
                String value) {
        }

        private static final Comparator<Entry> BY_TIMESTAMP =
                Comparator.comparingInt(Entry::timestamp);

        private final Map<String, List<Entry>> store = new HashMap<>();

        public void set(String key, String value, int timestamp) {

            List<Entry> history =
                    store.computeIfAbsent(key, ignored -> new ArrayList<>());

            history.add(new Entry(timestamp, value));
        }

        public String get(String key, int timestamp) {

            List<Entry> history = store.get(key);

            if (history == null) {
                return "";
            }

            int index = Collections.binarySearch(
                    history,
                    new Entry(timestamp, ""),
                    BY_TIMESTAMP);

            if (index >= 0) {
                return history.get(index).value();
            }

            int insertionPoint = -index - 1;
            int predecessor = insertionPoint - 1;

            if (predecessor < 0) {
                return "";
            }

            return history.get(predecessor).value();
        }
    }


    /*
     =====================================================================================
     9. 🟦 OOP / LOW-LEVEL DESIGN VERSION
     =====================================================================================

     New purpose

         Separate responsibilities without changing the algorithm.

         TimeMap
             -> WHICH key / Timeline?

         Timeline
             -> append history
             -> predecessor lookup

     Benefit

         Timeline internals can later change from ArrayList to another ordered
         representation without changing the outer store API.

     Cost

         more classes / indirection than the DSA solution.

     Use this to discuss LLD, not as the simplest coding-interview implementation.
     */
    static class EncapsulatedTimeMap {

        private final Map<String, Timeline> store = new HashMap<>();

        public void set(String key, String value, int timestamp) {

            Timeline timeline =
                    store.computeIfAbsent(key, ignored -> new Timeline());

            timeline.append(timestamp, value);
        }

        public String get(String key, int timestamp) {

            Timeline timeline = store.get(key);

            if (timeline == null) {
                return "";
            }

            return timeline.valueAt(timestamp);
        }

        private static final class Timeline {

            private final List<Entry> history = new ArrayList<>();

            void append(int timestamp, String value) {
                history.add(new Entry(timestamp, value));
            }

            String valueAt(int timestamp) {

                int left = 0;
                int right = history.size();

                while (left < right) {

                    int mid = left + (right - left) / 2;

                    if (history.get(mid).timestamp() <= timestamp) {
                        left = mid + 1;
                    } else {
                        right = mid;
                    }
                }

                if (left == 0) {
                    return "";
                }

                return history.get(left - 1).value();
            }
        }

        private record Entry(
                int timestamp,
                String value) {
        }
    }


    /*
     =====================================================================================
     10. 🏭 PRODUCTION LLD — CONCURRENT APPEND-ONLY IN-MEMORY STORE
     =====================================================================================

     New purpose

         Add production concerns absent from LeetCode:

             explicit API contract
             validation
             long timestamps
             Optional instead of magic empty-string absence
             concurrent access
             per-timeline synchronization
             monotonic timestamp enforcement

     Concurrency model

         ConcurrentHashMap
             protects key -> Timeline lookup / creation.

         ReentrantReadWriteLock inside each Timeline
             protects that key's ArrayList history.

         Therefore unrelated keys do not share one global lock.

     Important

         ConcurrentHashMap<String, List<Entry>> alone is NOT sufficient.
         The map would be concurrent; ArrayList would not.
     */
    interface ProductionTimeBasedStore {

        void put(String key, String value, long timestamp);

        Optional<String> get(String key, long timestamp);
    }


    static class ConcurrentAppendOnlyTimeBasedStore
            implements ProductionTimeBasedStore {

        private final ConcurrentHashMap<String, ConcurrentTimeline> store =
                new ConcurrentHashMap<>();

        @Override
        public void put(String key, String value, long timestamp) {

            validate(key, value, timestamp);

            ConcurrentTimeline timeline =
                    store.computeIfAbsent(
                            key,
                            ignored -> new ConcurrentTimeline());

            timeline.append(timestamp, value);
        }

        @Override
        public Optional<String> get(String key, long timestamp) {

            Objects.requireNonNull(key, "key");

            ConcurrentTimeline timeline = store.get(key);

            if (timeline == null) {
                return Optional.empty();
            }

            return timeline.valueAt(timestamp);
        }

        private static void validate(
                String key,
                String value,
                long timestamp) {

            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(value, "value");

            if (key.isBlank()) {
                throw new IllegalArgumentException("key cannot be blank");
            }

            if (timestamp < 0) {
                throw new IllegalArgumentException(
                        "timestamp cannot be negative");
            }
        }

        private static final class ConcurrentTimeline {

            private final List<Version> history = new ArrayList<>();

            private final ReentrantReadWriteLock lock =
                    new ReentrantReadWriteLock();

            void append(long timestamp, String value) {

                lock.writeLock().lock();

                try {

                    if (!history.isEmpty()) {

                        long latestTimestamp =
                                history.get(history.size() - 1).timestamp();

                        if (timestamp <= latestTimestamp) {
                            throw new IllegalArgumentException(
                                    "timestamps must be strictly increasing per key");
                        }
                    }

                    history.add(new Version(timestamp, value));

                } finally {
                    lock.writeLock().unlock();
                }
            }

            Optional<String> valueAt(long timestamp) {

                lock.readLock().lock();

                try {

                    int left = 0;
                    int right = history.size();

                    while (left < right) {

                        int mid = left + (right - left) / 2;

                        if (history.get(mid).timestamp() <= timestamp) {
                            left = mid + 1;
                        } else {
                            right = mid;
                        }
                    }

                    if (left == 0) {
                        return Optional.empty();
                    }

                    return Optional.of(
                            history.get(left - 1).value());

                } finally {
                    lock.readLock().unlock();
                }
            }
        }

        private record Version(
                long timestamp,
                String value) {
        }
    }


    /*
     =====================================================================================
     11. 🏭 PRODUCTION CONSTRAINT FLIP — OUT-OF-ORDER CONCURRENT WRITES
     =====================================================================================

     Changed requirement

         timestamps are no longer guaranteed to arrive increasing.

     Consequence

         append-only ArrayList no longer preserves sorted order.

     Replacement

         ConcurrentSkipListMap<Long, String>

         put(timestamp, value)
             O(log H)

         floorEntry(timestamp)
             O(log H)

     Duplicate policy in this runnable example

         later put overwrites the existing value at the same timestamp.

         A real product must explicitly choose overwrite / reject / idempotent semantics.
     */
    static class ConcurrentOrderedTimeBasedStore
            implements ProductionTimeBasedStore {

        private final ConcurrentHashMap<String, ConcurrentSkipListMap<Long, String>> store =
                new ConcurrentHashMap<>();

        @Override
        public void put(String key, String value, long timestamp) {

            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(value, "value");

            if (key.isBlank()) {
                throw new IllegalArgumentException("key cannot be blank");
            }

            if (timestamp < 0) {
                throw new IllegalArgumentException(
                        "timestamp cannot be negative");
            }

            ConcurrentSkipListMap<Long, String> timeline =
                    store.computeIfAbsent(
                            key,
                            ignored -> new ConcurrentSkipListMap<>());

            timeline.put(timestamp, value);
        }

        @Override
        public Optional<String> get(String key, long timestamp) {

            Objects.requireNonNull(key, "key");

            ConcurrentSkipListMap<Long, String> timeline = store.get(key);

            if (timeline == null) {
                return Optional.empty();
            }

            Map.Entry<Long, String> entry =
                    timeline.floorEntry(timestamp);

            if (entry == null) {
                return Optional.empty();
            }

            return Optional.of(entry.getValue());
        }
    }


    /*
     =====================================================================================
     12. ↔ CONSTRAINT CROSS-PRODUCT + PRODUCTION EVOLUTION
     =====================================================================================

     +--------------------------------------+----------------------------------------------+
     | Changed requirement                  | Best direction                               |
     +--------------------------------------+----------------------------------------------+
     | Base problem: monotonic timestamps   | ArrayList + binary search                    |
     | Timestamps arrive out of order       | TreeMap / ordered map                        |
     | Exact timestamp only                 | HashMap timestamp -> value                   |
     | Latest value only                    | Keep only latest version                     |
     | Need timestamp range queries         | NavigableMap / persistent ordered index      |
     | Concurrent + monotonic writes        | ConcurrentHashMap + locked append Timeline   |
     | Concurrent + arbitrary-order writes  | ConcurrentSkipListMap                        |
     | Durable / very large history         | database index on (key, timestamp) + cache   |
     +--------------------------------------+----------------------------------------------+

     Production evolution

         DSA

             Map<String, List<Entry>>

         OOP / LLD

             Map<String, Timeline>

         Concurrent in-memory

             ConcurrentHashMap<String, ConcurrentTimeline>

         Durable service

             API
              |
              v
             Service
              |
              +---- cache / in-memory timeline
              |
              +---- repository
                       |
                       v
                     database

     Persistent physical model

         key       timestamp       value
         foo       1               bar
         foo       4               bar2

     Important index

         (key, timestamp)

     Logical predecessor query

         WHERE key = ?
           AND timestamp <= ?
         ORDER BY timestamp DESC
         LIMIT 1

     Production questions LeetCode intentionally leaves unspecified

         - client timestamp or server timestamp?
         - duplicate timestamp semantics?
         - out-of-order writes?
         - retention / TTL / compaction?
         - can history grow forever?
         - durability and restart behavior?
         - read-after-write consistency?
         - key/value/history size limits?
         - observability and failure metrics?

     Transfer principle

         key lookup + predecessor search

         appears as

             HashMap + binary search
             HashMap + TreeMap.floorEntry()
             database composite index seek
     */


    /*
     =====================================================================================
     13. ⚔ HIGH-ROI TRAPS / CONFUSION KILLERS
     =====================================================================================

     1. VALID DOES NOT MEAN FINAL

         timestamp[mid] <= query

         means mid is a candidate,
         not necessarily the newest candidate.

         Therefore continue right.

     2. DO NOT MIX INTERVAL CONVENTIONS

         This implementation uses

             [left, right)

         so

             right = history.size()
             while (left < right)
             right = mid

         Do not randomly switch to right = mid - 1.

     3. BEFORE FIRST TIMESTAMP

         left == 0
             -> no predecessor
             -> return ""

     4. DO NOT SORT AFTER EVERY INSERT

         Monotonic timestamps already preserve order.

     5. DO NOT USE ONE GLOBAL HISTORY

         Each key owns an independent timeline.

     6. TreeMap vs ArrayList + binary search

         TreeMap
             maintains ordering dynamically.

         ArrayList
             receives ordering for free from the problem guarantee.
     */


    /*
     =====================================================================================
     14. 🎯 INTERVIEW ARTICULATION + BLANK-BRAIN RECONSTRUCTION
     =====================================================================================

     Concise explanation

         I keep one history list per key.

         Because timestamps for each key arrive strictly increasing, appending keeps
         every history sorted in O(1) amortized time.

         get() is a predecessor query: I binary-search for the first timestamp greater
         than the query. The element immediately before that boundary is therefore the
         largest timestamp less than or equal to the query.

         set() is O(1) average/amortized, get() is O(log H), and total space is O(S).

     -------------------------------------------------------------------------------------
     BLANK-BRAIN RECONSTRUCTION

         What am I storing?
             multiple historical values per key

         Structure?
             key -> history

         Why can history be a List?
             timestamps arrive increasing

         set()?
             get/create history + append

         get() asks for?
             largest timestamp <= query

         Convert to boundary?
             first timestamp > query

         Answer?
             left - 1

         No predecessor?
             left == 0 -> ""

     One-line recall

         PER-KEY SORTED HISTORY + UPPER BOUND + PREDECESSOR.
     */


    /*
     =====================================================================================
     15. 🧪 MAIN + SELF-VERIFYING TESTS
     =====================================================================================
     */
    public static void main(String[] args) {

        testPrimary();
        testBruteForce();
        testTreeMap();
        testLibraryBinarySearch();
        testOopVersion();
        testConcurrentAppendOnlyVersion();
        testConcurrentOrderedVersion();

        System.out.println("All assertions passed.");
    }

    private static void testPrimary() {

        OptimalTimeMap timeMap = new OptimalTimeMap();

        timeMap.set("foo", "bar", 1);

        assert "bar".equals(timeMap.get("foo", 1));
        assert "bar".equals(timeMap.get("foo", 3));

        timeMap.set("foo", "bar2", 4);

        assert "bar2".equals(timeMap.get("foo", 4));
        assert "bar2".equals(timeMap.get("foo", 5));
        assert "".equals(timeMap.get("foo", 0));
        assert "".equals(timeMap.get("missing", 100));

        OptimalTimeMap history = new OptimalTimeMap();

        history.set("k", "v1", 2);
        history.set("k", "v2", 5);
        history.set("k", "v3", 8);
        history.set("k", "v4", 20);

        assert "v1".equals(history.get("k", 4));
        assert "v2".equals(history.get("k", 7));
        assert "v3".equals(history.get("k", 19));
        assert "v4".equals(history.get("k", 1000));
    }

    private static void testBruteForce() {

        BruteForceTimeMap timeMap = new BruteForceTimeMap();

        timeMap.set("z", "first", 1);
        timeMap.set("z", "second", 9);

        assert "first".equals(timeMap.get("z", 4));
        assert "second".equals(timeMap.get("z", 100));
    }

    private static void testTreeMap() {

        ImprovedTreeMapTimeMap timeMap = new ImprovedTreeMapTimeMap();

        timeMap.set("x", "one", 10);
        timeMap.set("x", "zero", 5);

        assert "zero".equals(timeMap.get("x", 8));
        assert "one".equals(timeMap.get("x", 10));
    }

    private static void testLibraryBinarySearch() {

        LibraryBinarySearchTimeMap timeMap = new LibraryBinarySearchTimeMap();

        timeMap.set("k", "v1", 2);
        timeMap.set("k", "v2", 5);
        timeMap.set("k", "v3", 8);

        assert "".equals(timeMap.get("k", 1));
        assert "v2".equals(timeMap.get("k", 7));
        assert "v3".equals(timeMap.get("k", 8));
    }

    private static void testOopVersion() {

        EncapsulatedTimeMap timeMap = new EncapsulatedTimeMap();

        timeMap.set("foo", "A", 2);
        timeMap.set("foo", "B", 5);

        assert "A".equals(timeMap.get("foo", 4));
        assert "B".equals(timeMap.get("foo", 5));
    }

    private static void testConcurrentAppendOnlyVersion() {

        ProductionTimeBasedStore timeMap =
                new ConcurrentAppendOnlyTimeBasedStore();

        timeMap.put("foo", "A", 2);
        timeMap.put("foo", "B", 5);

        assert Optional.of("A").equals(timeMap.get("foo", 4));
        assert Optional.of("B").equals(timeMap.get("foo", 100));
        assert Optional.empty().equals(timeMap.get("foo", 1));
    }

    private static void testConcurrentOrderedVersion() {

        ProductionTimeBasedStore timeMap =
                new ConcurrentOrderedTimeBasedStore();

        timeMap.put("foo", "C", 20);
        timeMap.put("foo", "A", 2);
        timeMap.put("foo", "B", 5);

        assert Optional.of("A").equals(timeMap.get("foo", 4));
        assert Optional.of("B").equals(timeMap.get("foo", 7));
        assert Optional.of("C").equals(timeMap.get("foo", 100));
    }
}
