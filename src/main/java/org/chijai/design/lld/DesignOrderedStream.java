package org.chijai.design.lld;

import java.util.*;

/**
 * LeetCode 1656 - Design an Ordered Stream
 *
 * -------------------------------------------------------------------------
 * CORE IDEA
 * -------------------------------------------------------------------------
 *
 * IDs are guaranteed to be:
 *
 *      1, 2, 3, ... n
 *
 * and every id appears exactly once.
 *
 * So we do NOT need:
 *
 *      TreeMap
 *      PriorityQueue
 *      sorting
 *
 * We can directly store:
 *
 *      values[id] = value
 *
 * and maintain one pointer:
 *
 *      ptr = first ID not yet emitted
 *
 * -------------------------------------------------------------------------
 * EXAMPLE
 * -------------------------------------------------------------------------
 *
 * n = 5
 *
 * insert(3, "ccccc")
 *
 * values:
 *
 *      1 -> null
 *      2 -> null
 *      3 -> ccccc
 *      4 -> null
 *      5 -> null
 *
 * ptr = 1
 *
 * Since values[1] is missing:
 *
 *      return []
 *
 *
 * insert(1, "aaaaa")
 *
 * values[1] exists.
 *
 * Start from ptr = 1:
 *
 *      1 exists -> emit
 *      2 missing -> stop
 *
 * return:
 *
 *      ["aaaaa"]
 *
 * ptr becomes 2.
 *
 *
 * insert(2, "bbbbb")
 *
 * Start from ptr = 2:
 *
 *      2 exists -> emit
 *      3 already exists -> emit
 *      4 missing -> stop
 *
 * return:
 *
 *      ["bbbbb", "ccccc"]
 *
 * ptr becomes 4.
 *
 * -------------------------------------------------------------------------
 * INVARIANT
 * -------------------------------------------------------------------------
 *
 * ptr always points to:
 *
 *      the smallest ID that has NOT yet been returned
 *
 * Therefore:
 *
 * after every insertion,
 * emit the largest consecutive block starting exactly at ptr.
 *
 * -------------------------------------------------------------------------
 * WHY THIS IS AMORTIZED O(1)
 * -------------------------------------------------------------------------
 *
 * A single insert() may return many values.
 *
 * Example:
 *
 *      insert(2)
 *      insert(3)
 *      insert(4)
 *      insert(1)
 *
 * The final insert may emit 4 values.
 *
 * But every element is emitted exactly once over the lifetime
 * of the OrderedStream.
 *
 * Across all n calls:
 *
 *      total pointer movement = n
 *
 * Therefore:
 *
 *      Total time across n inserts = O(n)
 *      Amortized insert()          = O(1)
 *
 * More precisely:
 *
 *      insert() = O(chunk size)
 *
 * -------------------------------------------------------------------------
 * SPACE
 * -------------------------------------------------------------------------
 *
 * values array:
 *
 *      O(n)
 */
public class DesignOrderedStream {

    static class OrderedStream {

        /*
         * Use n + 1 so IDs can be used directly as indexes.
         *
         * Index 0 is intentionally unused.
         */
        private final String[] values;

        /*
         * Smallest ID not yet emitted.
         */
        private int ptr = 1;

        public OrderedStream(int n) {
            values = new String[n + 1];
        }

        /**
         * Insert (idKey, value), then return the largest consecutive
         * chunk beginning at ptr.
         */
        public String[] insert(int idKey, String value) {

            // Direct O(1) placement by ID.
            values[idKey] = value;

            /*
             * If this insertion does NOT fill the current gap,
             * nothing can be emitted yet.
             *
             * Example:
             *
             * ptr = 1
             * insert(3, "ccccc")
             *
             * values[1] is still missing -> return []
             */
            if (values[ptr] == null) {
                return new String[0];
            }

            List<String> chunk = new ArrayList<>();

            /*
             * Emit the largest consecutive block starting at ptr.
             */
            while (ptr < values.length && values[ptr] != null) {
                chunk.add(values[ptr]);
                ptr++;
            }

            return chunk.toArray(new String[0]);
        }
    }

    // ---------------------------------------------------------------------
    // TEST HARNESS
    // ---------------------------------------------------------------------

    private static void assertArrayEquals(
            String[] expected,
            String[] actual,
            String testName
    ) {

        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(
                    testName
                            + " FAILED"
                            + "\nexpected = " + Arrays.toString(expected)
                            + "\nactual   = " + Arrays.toString(actual)
            );
        }

        System.out.println(
                "PASS: " + testName + " -> " + Arrays.toString(actual)
        );
    }

    /**
     * Exact LeetCode example.
     */
    private static void testLeetCodeExample() {

        System.out.println("\n=== Test 1: LeetCode Example ===");

        OrderedStream os = new OrderedStream(5);

        assertArrayEquals(
                new String[]{},
                os.insert(3, "ccccc"),
                "insert(3)"
        );

        assertArrayEquals(
                new String[]{"aaaaa"},
                os.insert(1, "aaaaa"),
                "insert(1)"
        );

        assertArrayEquals(
                new String[]{"bbbbb", "ccccc"},
                os.insert(2, "bbbbb"),
                "insert(2)"
        );

        assertArrayEquals(
                new String[]{},
                os.insert(5, "eeeee"),
                "insert(5)"
        );

        assertArrayEquals(
                new String[]{"ddddd", "eeeee"},
                os.insert(4, "ddddd"),
                "insert(4)"
        );
    }

    /**
     * Already sorted arrival.
     *
     * Every insert immediately emits one element.
     */
    private static void testAlreadySortedArrival() {

        System.out.println("\n=== Test 2: Already Sorted Arrival ===");

        OrderedStream os = new OrderedStream(3);

        assertArrayEquals(
                new String[]{"aaaaa"},
                os.insert(1, "aaaaa"),
                "Sorted insert 1"
        );

        assertArrayEquals(
                new String[]{"bbbbb"},
                os.insert(2, "bbbbb"),
                "Sorted insert 2"
        );

        assertArrayEquals(
                new String[]{"ccccc"},
                os.insert(3, "ccccc"),
                "Sorted insert 3"
        );
    }

    /**
     * Reverse order arrival.
     *
     * Nothing can be emitted until id 1 arrives.
     * Then all values become one large chunk.
     */
    private static void testReverseArrival() {

        System.out.println("\n=== Test 3: Reverse Arrival ===");

        OrderedStream os = new OrderedStream(4);

        assertArrayEquals(
                new String[]{},
                os.insert(4, "dddd"),
                "Reverse insert 4"
        );

        assertArrayEquals(
                new String[]{},
                os.insert(3, "cccc"),
                "Reverse insert 3"
        );

        assertArrayEquals(
                new String[]{},
                os.insert(2, "bbbb"),
                "Reverse insert 2"
        );

        assertArrayEquals(
                new String[]{"aaaa", "bbbb", "cccc", "dddd"},
                os.insert(1, "aaaa"),
                "Reverse insert 1 releases all"
        );
    }

    /**
     * Single element stream.
     */
    private static void testSingleElement() {

        System.out.println("\n=== Test 4: Single Element ===");

        OrderedStream os = new OrderedStream(1);

        assertArrayEquals(
                new String[]{"hello"},
                os.insert(1, "hello"),
                "Single element"
        );
    }

    /**
     * Multiple blocked chunks.
     */
    private static void testMultipleGaps() {

        System.out.println("\n=== Test 5: Multiple Gaps ===");

        OrderedStream os = new OrderedStream(6);

        assertArrayEquals(
                new String[]{},
                os.insert(2, "bbbbb"),
                "Insert 2 before 1"
        );

        assertArrayEquals(
                new String[]{"aaaaa", "bbbbb"},
                os.insert(1, "aaaaa"),
                "Insert 1 releases 1-2"
        );

        assertArrayEquals(
                new String[]{},
                os.insert(5, "eeeee"),
                "Insert 5 while ptr at 3"
        );

        assertArrayEquals(
                new String[]{"ccccc"},
                os.insert(3, "ccccc"),
                "Insert 3 releases only 3"
        );

        assertArrayEquals(
                new String[]{"ddddd", "eeeee"},
                os.insert(4, "ddddd"),
                "Insert 4 releases 4-5"
        );

        assertArrayEquals(
                new String[]{"fffff"},
                os.insert(6, "fffff"),
                "Insert 6"
        );
    }

    /**
     * Proves that previously inserted future values can be released
     * by one later insertion that fills the missing prefix.
     */
    private static void testLargeChunkRelease() {

        System.out.println("\n=== Test 6: Large Chunk Release ===");

        OrderedStream os = new OrderedStream(5);

        assertArrayEquals(
                new String[]{},
                os.insert(2, "twooo"),
                "Store 2"
        );

        assertArrayEquals(
                new String[]{},
                os.insert(3, "three"),
                "Store 3"
        );

        assertArrayEquals(
                new String[]{},
                os.insert(4, "fourr"),
                "Store 4"
        );

        assertArrayEquals(
                new String[]{"oneee", "twooo", "three", "fourr"},
                os.insert(1, "oneee"),
                "Filling missing prefix releases large chunk"
        );

        assertArrayEquals(
                new String[]{"fivee"},
                os.insert(5, "fivee"),
                "Final value"
        );
    }

    public static void main(String[] args) {

        testLeetCodeExample();
        testAlreadySortedArrival();
        testReverseArrival();
        testSingleElement();
        testMultipleGaps();
        testLargeChunkRelease();

        System.out.println("\nALL TESTS PASSED");
    }
}
