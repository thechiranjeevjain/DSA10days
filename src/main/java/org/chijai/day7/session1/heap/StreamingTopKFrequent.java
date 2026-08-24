package org.chijai.day7.session1.heap;

import java.util.*;

/**
 * Streaming Top-K Frequent Elements — Exact Online Version
 *
 * Problem:
 *   Numbers arrive continuously. After every update (or whenever queried),
 *   return the current top K most frequent values.
 *
 * Why not just use PriorityQueue with a comparator that reads from a mutable frequency map?
 *   Because Java PriorityQueue does NOT automatically re-heapify when the external
 *   frequency values used by the comparator change.
 *
 * Design:
 *   1) HashMap<Integer, Integer> frequencies
 *        value -> current frequency
 *
 *   2) TreeSet<Entry> ranking
 *        ordered by:
 *          - higher frequency first
 *          - smaller value first as deterministic tie-breaker
 *
 * Update:
 *   - remove the OLD (value, frequency) entry from TreeSet
 *   - increment frequency in the map
 *   - insert the NEW (value, frequency) entry
 *
 * Complexity:
 *   Let u = number of unique values seen so far.
 *
 *   add(value) : O(log u)
 *   topK(k)    : O(k)
 *   space      : O(u)
 *
 * Important limitation:
 *   For an infinite stream with infinitely many distinct values, exact counting requires
 *   memory that can grow without bound. In such systems, use a time window or an
 *   approximate heavy-hitter algorithm such as Space-Saving / Count-Min Sketch.
 */
public class StreamingTopKFrequent {

    /**
     * Immutable ranking entry.
     *
     * TreeSet equality is determined by the comparator, so the comparator MUST include
     * both frequency and value. Otherwise two different values with the same frequency
     * could be treated as duplicates.
     */
    record Entry(int value, int frequency) {}

    static class StreamingTopK {

        private final Map<Integer, Integer> frequencies = new HashMap<>();

        /**
         * Highest frequency first.
         *
         * Example ordering:
         *   (10, freq=5)
         *   (3,  freq=4)
         *   (7,  freq=4)
         *   (2,  freq=1)
         */
        private final TreeSet<Entry> ranking = new TreeSet<>(
                Comparator.comparingInt(Entry::frequency)
                        .reversed()
                        .thenComparingInt(Entry::value)
        );

        /**
         * Process one number from the stream.
         */
        public void add(int value) {
            int oldFrequency = frequencies.getOrDefault(value, 0);

            // IMPORTANT:
            // Remove the old ranking entry BEFORE changing its frequency.
            if (oldFrequency > 0) {
                ranking.remove(new Entry(value, oldFrequency));
            }

            int newFrequency = oldFrequency + 1;
            frequencies.put(value, newFrequency);

            ranking.add(new Entry(value, newFrequency));
        }

        /**
         * Return up to k values with the highest frequencies.
         *
         * Because the TreeSet is already sorted descending by frequency,
         * we simply read its first k entries.
         */
        public List<Integer> topK(int k) {
            if (k <= 0) {
                return List.of();
            }

            List<Integer> result = new ArrayList<>(Math.min(k, ranking.size()));

            int count = 0;
            for (Entry entry : ranking) {
                if (count == k) {
                    break;
                }

                result.add(entry.value());
                count++;
            }

            return result;
        }

        /**
         * Read the current frequency of one value.
         */
        public int frequencyOf(int value) {
            return frequencies.getOrDefault(value, 0);
        }

        /**
         * Number of distinct values seen so far.
         */
        public int uniqueCount() {
            return frequencies.size();
        }

        /**
         * Debug helper: current full ranking.
         */
        public List<Entry> fullRanking() {
            return new ArrayList<>(ranking);
        }
    }

    public static void main(String[] args) {

        testBasicStreamingTopK();
        testFrequencyUpdatesReorderRanking();
        testTieBreaking();
        testKGreaterThanUniqueCount();
        testZeroK();
        testNegativeAndZeroValues();

        System.out.println("All tests passed.");
    }

    private static void testBasicStreamingTopK() {
        StreamingTopK stream = new StreamingTopK();

        // Stream: 1, 2, 1, 3, 1, 2
        stream.add(1);
        stream.add(2);
        stream.add(1);
        stream.add(3);
        stream.add(1);
        stream.add(2);

        // Frequencies:
        // 1 -> 3
        // 2 -> 2
        // 3 -> 1
        assertEquals(List.of(1, 2), stream.topK(2), "basic topK");
        assertEquals(3, stream.frequencyOf(1), "frequency of 1");
        assertEquals(2, stream.frequencyOf(2), "frequency of 2");
        assertEquals(1, stream.frequencyOf(3), "frequency of 3");
    }

    private static void testFrequencyUpdatesReorderRanking() {
        StreamingTopK stream = new StreamingTopK();

        // Initially: 1 -> 2, 2 -> 1
        stream.add(1);
        stream.add(1);
        stream.add(2);

        assertEquals(List.of(1), stream.topK(1), "1 should initially lead");

        // Now 2 becomes more frequent:
        // 1 -> 2
        // 2 -> 3
        stream.add(2);
        stream.add(2);

        assertEquals(List.of(2), stream.topK(1), "2 should move to the top after updates");
    }

    private static void testTieBreaking() {
        StreamingTopK stream = new StreamingTopK();

        // All frequencies are equal.
        stream.add(5);
        stream.add(3);
        stream.add(7);

        // Deterministic tie-breaker: smaller value first.
        assertEquals(List.of(3, 5, 7), stream.topK(3), "tie breaker");
    }

    private static void testKGreaterThanUniqueCount() {
        StreamingTopK stream = new StreamingTopK();

        stream.add(4);
        stream.add(4);
        stream.add(9);

        assertEquals(List.of(4, 9), stream.topK(10), "k > unique count");
    }

    private static void testZeroK() {
        StreamingTopK stream = new StreamingTopK();

        stream.add(1);
        stream.add(2);

        assertEquals(List.of(), stream.topK(0), "k = 0");
    }

    private static void testNegativeAndZeroValues() {
        StreamingTopK stream = new StreamingTopK();

        stream.add(-1);
        stream.add(0);
        stream.add(-1);
        stream.add(0);
        stream.add(0);

        // 0 -> 3
        // -1 -> 2
        assertEquals(List.of(0, -1), stream.topK(2), "negative and zero values");
    }

    private static <T> void assertEquals(T expected, T actual, String testName) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(
                    testName + " failed. Expected: " + expected + ", actual: " + actual
            );
        }
    }
}
