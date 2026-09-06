package org.chijai.day2.session3;

import java.util.ArrayList;
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
             GET HISTORY
             ADD ENTRY
             PUT HISTORY

         GET
             BINARY SEARCH FOR LARGEST timestamp <= query

             VALID   -> SAVE ans -> GO RIGHT
             INVALID -> GO LEFT

         Canonical boundary picture

             [ valid valid valid | invalid invalid ]
                           ^      ^
                          ans    left

         valid
             timestamp <= query

         invalid
             timestamp > query

         At termination

             ans = index of the newest valid timestamp

         If no valid timestamp was ever seen

             ans == -1 -> return ""

         Memory sentence

             VALID -> SAVE -> RIGHT.
             INVALID -> LEFT.

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

            List<Entry> history = store.getOrDefault(key, new ArrayList<>());
            history.add(new Entry(timestamp, value));
            store.put(key, history);
        }

        public String get(String key, int timestamp) {

            List<Entry> history = store.get(key);

            if (history == null) {
                return "";
            }

            int left = 0;
            int right = history.size() - 1;
            int ans = -1;

            while (left <= right) {

                int mid = left + (right - left) / 2;

                Entry current = history.get(mid);

                if (current.timestamp() <= timestamp) {
                    ans = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            return ans == -1
                    ? ""
                    : history.get(ans).value();
        }
    }


    /*
     RANK 2 — TreeMap fallback

         floorKey(timestamp)
             -> largest stored timestamp <= query

         set() O(log H)
         get() O(log H)

         Prefer when timestamps may arrive out of order,
         or when you want the cleanest predecessor API.
     */
    static class ImprovedTreeMapTimeMap {

        private final Map<String, TreeMap<Integer, String>> store = new HashMap<>();

        public void set(String key, String value, int timestamp) {

            store.computeIfAbsent(key, missingKey -> new TreeMap<>())
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

     5. During binary search, explicitly remember the best valid candidate.

            timestamp[mid] <= query
                -> mid is valid
                -> save ans = mid
                -> search right for something newer

            timestamp[mid] > query
                -> mid is invalid
                -> search left

     Final structure

         HashMap<String, List<Entry>>

         set()
             get history or default empty history
             append
             put history back

         get()
             binary-search largest timestamp <= query
             return saved ans

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

         Search interval is inclusive:

             [left, right]

         ans stores the newest valid index seen so far.

         +------+-------+-----+-----+----------------+----------------------+------------------+
         | left | right | mid | ans | timestamp[mid] | meaning              | move             |
         +------+-------+-----+-----+----------------+----------------------+------------------+
         | 0    | 3     | 1   | -1  | 5              | valid                | ans=1, left=2    |
         | 2    | 3     | 2   | 1   | 8              | too large            | right=1          |
         +------+-------+-----+-----+----------------+----------------------+------------------+

         stop

             left = 2
             right = 1
             ans = 1

         Canonical picture

             [ 2   5 | 8   20 ]
                   ^   ^
                  ans left

             [ valid valid | invalid invalid ]
                     ^       ^
                    ans     left

         answer

             history[ans]
             history[1]
             "B"

     -------------------------------------------------------------------------------------
     WHY SAVE ans AND STILL MOVE RIGHT?

         current is VALID,
         but it may not be the NEWEST valid timestamp.

         So

             ans = mid

         remembers the current best answer, while

             left = mid + 1

         searches for a newer valid timestamp.

         Memory rule

             VALID -> SAVE -> RIGHT.

     -------------------------------------------------------------------------------------
     WHY right = mid - 1 WHEN current > query?

         With inclusive [left, right], mid itself is invalid.

         Therefore mid cannot be the answer and can be discarded.

             INVALID -> LEFT.

     -------------------------------------------------------------------------------------
     EDGE CASES

         query before everything

             ans stays -1
             -> return ""

         query after everything

             every visited valid candidate can update ans
             -> ans finishes at the latest index

         exact match

             exact timestamp is valid
             -> save it
             -> continue right only in case a newer valid timestamp exists
     */


    /*
     =====================================================================================
     6. ✅ CORRECTNESS + COMPLEXITY
     =====================================================================================

     Correctness contract

         ans always stores the latest valid index found so far:

             history[ans].timestamp <= query

         If timestamp[mid] <= query

             mid is valid,
             so saving ans = mid is safe.

             Only something farther right can be newer,
             so search right.

         If timestamp[mid] > query

             mid is invalid.

             Because history is sorted,
             everything to the right is also invalid,
             so search left.

         When the loop ends

             ans == -1
                 -> no timestamp <= query exists

             otherwise
                 -> ans is the largest index whose timestamp <= query

     Termination

         Inclusive interval [left, right] strictly shrinks every iteration:

             valid   -> left = mid + 1
             invalid -> right = mid - 1

     Complexity

         Let H = history size for the queried key.
         Let S = total number of set operations.

         set()
             HashMap get/put + ArrayList append
             O(1) average / amortized

         get()
             HashMap lookup + binary search
             O(log H)

         total space
             O(S)
     */


    /*
     =====================================================================================
     7. 🏆 INTERVIEW APPROACH RANKING — MAX 3
     =====================================================================================

     Rank only genuinely different algorithmic approaches.

     +------+-----------------------------------+-------------+-------------+-----------------------------------------------+
     | Rank | Approach                          | set()       | get()       | Interview suitability                         |
     +------+-----------------------------------+-------------+-------------+-----------------------------------------------+
     | 1    | ArrayList + manual binary search | O(1) amort. | O(log H)    | PRIMARY — best use of stated constraints      |
     | 2    | TreeMap + predecessor lookup      | O(log H)    | O(log H)    | STRONG FALLBACK — simpler, more general        |
     | 3    | ArrayList + reverse scan          | O(1) amort. | O(H)        | BASELINE — derive first, then optimize          |
     +------+-----------------------------------+-------------+-------------+-----------------------------------------------+

     Why this ranking?

         1. ArrayList + binary search

             Timestamps already arrive increasing.

             Therefore we receive ordering for free:

                 append
                     O(1) amortized

                 predecessor search
                     O(log H)

             This is the interview-preferred solution.

         2. TreeMap

             floorKey() / floorEntry() directly answers

                 largest timestamp <= query

             It is clean and survives out-of-order timestamps,
             but pays O(log H) on every insertion to maintain ordering
             that the base problem already guarantees.

         3. Reverse scan

             Correct and easy to derive.

             But get() can inspect the entire history:

                 O(H)

             Useful as the baseline, not the final answer.

     Core distinction

         TreeMap solves the REQUIREMENT.

         ArrayList + binary search exploits the CONSTRAINT.

     Not separate ranked approaches

         Collections.binarySearch()

             Same algorithmic family as Rank 1.
             It only replaces the handwritten binary-search loop with a library call.

         OOP Timeline / production concurrent versions

             Same core lookup algorithm under a different software-design goal.
             They belong in the LLD sections, not in the interview-algorithm ranking.
     */


    /*
     =====================================================================================
     8. 🔄 RUNNABLE INTERVIEW ALTERNATIVES
     =====================================================================================

     Rank 1 is the PRIMARY PHOTOGRAPHIC MEMORY SOLUTION above.
     Rank 2 TreeMap fallback is placed immediately after it for fast retrieval.

     Rank 3 remains here as the derivation baseline.
     */

    /*
     RANK 3 — Reverse-scan baseline

         set() O(1)
         get() O(H)

         Correct because newest timestamps are visited first.
     */
    static class BruteForceTimeMap {

        private record Entry(
                int timestamp,
                String value) {
        }

        private final Map<String, List<Entry>> store = new HashMap<>();

        public void set(String key, String value, int timestamp) {
            store.computeIfAbsent(key, missingKey -> new ArrayList<>())
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
                    store.computeIfAbsent(key, missingKey -> new Timeline());

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
                int right = history.size() - 1;
                int ans = -1;

                while (left <= right) {

                    int mid = left + (right - left) / 2;

                    if (history.get(mid).timestamp() <= timestamp) {
                        ans = mid;
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }

                return ans == -1
                        ? ""
                        : history.get(ans).value();
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
                            missingKey -> new ConcurrentTimeline());

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
                    int right = history.size() - 1;
                    int ans = -1;

                    while (left <= right) {

                        int mid = left + (right - left) / 2;

                        if (history.get(mid).timestamp() <= timestamp) {
                            ans = mid;
                            left = mid + 1;
                        } else {
                            right = mid - 1;
                        }
                    }

                    return ans == -1
                            ? Optional.empty()
                            : Optional.of(history.get(ans).value());

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
                            missingKey -> new ConcurrentSkipListMap<>());

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

         Therefore

             ans = mid
             left = mid + 1

     2. KEEP ONE BINARY-SEARCH CONVENTION

         This file uses the inclusive interval

             [left, right]

         so

             right = history.size() - 1
             while (left <= right)
             valid   -> left = mid + 1
             invalid -> right = mid - 1

         Do not mix this with the half-open [left, right) template.

     3. BEFORE FIRST TIMESTAMP

         ans stays -1
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

         get() is a predecessor query. I binary-search the sorted history while keeping
         ans as the latest valid index seen so far. When timestamp[mid] <= query,
         I save mid and continue right for something newer. Otherwise I search left.

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
             GET -> ADD -> PUT

         get() asks for?
             largest timestamp <= query

         Binary-search rule?
             VALID -> SAVE ans -> RIGHT
             INVALID -> LEFT

         No valid timestamp?
             ans == -1 -> ""

     One-line recall

         PER-KEY SORTED HISTORY + SAVE VALID + SEARCH RIGHT.
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
