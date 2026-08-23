package org.chijai.design.lld;

import java.util.*;

/**
 * LeetCode 362 - Design Hit Counter
 *
 * We need to count how many hits happened in the past 5 minutes.
 *
 * 5 minutes = 300 seconds.
 *
 * For getHits(timestamp), valid hits are from:
 *
 *      timestamp - 299  ...  timestamp
 *
 * Equivalently, a stored hit is valid when:
 *
 *      timestamp - storedTimestamp < 300
 *
 * -------------------------------------------------------------------------
 * BEST INTERVIEW DESIGN: FIXED-SIZE CIRCULAR ARRAY
 * -------------------------------------------------------------------------
 *
 * Since the window is ALWAYS exactly 300 seconds, we only need 300 buckets.
 *
 * For every second:
 *
 *      index = timestamp % 300
 *
 * At each index we store:
 *
 *      times[index] = exact timestamp currently occupying the bucket
 *      hits[index]  = number of hits at that exact timestamp
 *
 * Why store the exact timestamp too?
 *
 * Because timestamps 1, 301, 601, ... all map to the same array index.
 *
 * When a newer timestamp reuses a slot, the old bucket is stale and must
 * be reset.
 *
 * Example:
 *
 *      timestamp = 1
 *      index = 1
 *
 *      timestamp = 301
 *      index = 1 again
 *
 * So when hit(301) arrives:
 *
 *      times[1] != 301
 *
 * which tells us to overwrite/reset that bucket.
 *
 * -------------------------------------------------------------------------
 * CORE INVARIANT
 * -------------------------------------------------------------------------
 *
 * For every index i:
 *
 *      hits[i] belongs ONLY to times[i].
 *
 * We never mix hits from different timestamps that happen to collide
 * through timestamp % 300.
 *
 * -------------------------------------------------------------------------
 * COMPLEXITY
 * -------------------------------------------------------------------------
 *
 * hit(timestamp)
 *      O(1)
 *
 * getHits(timestamp)
 *      scans exactly 300 buckets
 *      O(300) = O(1), because 300 is a fixed constant
 *
 * Space
 *      two arrays of size 300
 *      O(300) = O(1)
 *
 * -------------------------------------------------------------------------
 * IMPORTANT BOUNDARY
 * -------------------------------------------------------------------------
 *
 * A hit exactly 300 seconds old is EXPIRED.
 *
 * Example:
 *
 *      hit at 1
 *      query at 300 -> age = 299 -> VALID
 *      query at 301 -> age = 300 -> EXPIRED
 *
 * Therefore:
 *
 *      timestamp - times[i] < 300
 *
 * NOT:
 *
 *      <= 300
 */
public class DesignHitCounter {

    static class HitCounter {

        private static final int WINDOW_SECONDS = 300;

        // Exact timestamp currently stored in each circular slot.
        private final int[] times = new int[WINDOW_SECONDS];

        // Number of hits at that exact timestamp.
        private final int[] hits = new int[WINDOW_SECONDS];

        /**
         * Record one hit at timestamp.
         *
         * LeetCode guarantees calls are made in chronological order.
         */
        public void hit(int timestamp) {

            int index = timestamp % WINDOW_SECONDS;

            /*
             * Same circular slot can represent timestamps:
             *
             *      1, 301, 601, ...
             *
             * If the slot belongs to an older timestamp,
             * reset it before storing the new hit.
             */
            if (times[index] != timestamp) {
                times[index] = timestamp;
                hits[index] = 1;
            } else {
                // Multiple hits in the same second.
                hits[index]++;
            }
        }

        /**
         * Return number of hits in the previous 300 seconds,
         * including hits at the current timestamp.
         */
        public int getHits(int timestamp) {

            int total = 0;

            for (int i = 0; i < WINDOW_SECONDS; i++) {

                /*
                 * Valid window:
                 *
                 *      timestamp - storedTimestamp < 300
                 *
                 * We also require times[i] != 0 because arrays start
                 * initialized with zero and timestamps are positive.
                 */
                if (times[i] != 0 &&
                        timestamp - times[i] < WINDOW_SECONDS) {

                    total += hits[i];
                }
            }

            return total;
        }
    }

    // ---------------------------------------------------------------------
    // TEST HARNESS
    // ---------------------------------------------------------------------

    private static void assertEquals(
            int expected,
            int actual,
            String testName
    ) {

        if (expected != actual) {
            throw new AssertionError(
                    testName
                            + " FAILED: expected=" + expected
                            + ", actual=" + actual
            );
        }

        System.out.println(
                "PASS: " + testName + " -> " + actual
        );
    }

    /**
     * Canonical LeetCode-style example:
     *
     * hit(1)
     * hit(2)
     * hit(3)
     *
     * getHits(4)   = 3
     *
     * hit(300)
     *
     * getHits(300) = 4
     *
     * At timestamp 301:
     * hit at timestamp 1 is now exactly 300 seconds old,
     * so it expires.
     *
     * getHits(301) = 3
     */
    private static void testBasicExample() {

        System.out.println("\n=== Test 1: Basic Example ===");

        HitCounter counter = new HitCounter();

        counter.hit(1);
        counter.hit(2);
        counter.hit(3);

        assertEquals(
                3,
                counter.getHits(4),
                "Hits at timestamp 4"
        );

        counter.hit(300);

        assertEquals(
                4,
                counter.getHits(300),
                "Hits at timestamp 300"
        );

        assertEquals(
                3,
                counter.getHits(301),
                "Timestamp 1 expires at timestamp 301"
        );
    }

    /**
     * Multiple hits can occur in exactly the same second.
     */
    private static void testMultipleHitsSameSecond() {

        System.out.println("\n=== Test 2: Multiple Hits Same Second ===");

        HitCounter counter = new HitCounter();

        counter.hit(10);
        counter.hit(10);
        counter.hit(10);
        counter.hit(10);

        assertEquals(
                4,
                counter.getHits(10),
                "Four hits in same second"
        );

        assertEquals(
                4,
                counter.getHits(309),
                "Same hits still valid at age 299"
        );

        assertEquals(
                0,
                counter.getHits(310),
                "Same hits expire at age 300"
        );
    }

    /**
     * Critical off-by-one boundary:
     *
     * hit at 1:
     *
     * query 300 => age 299 => included
     * query 301 => age 300 => excluded
     */
    private static void testExactWindowBoundary() {

        System.out.println("\n=== Test 3: Exact 300-Second Boundary ===");

        HitCounter counter = new HitCounter();

        counter.hit(1);

        assertEquals(
                1,
                counter.getHits(300),
                "Age 299 is still inside window"
        );

        assertEquals(
                0,
                counter.getHits(301),
                "Age 300 is outside window"
        );
    }

    /**
     * Tests circular-array collision.
     *
     * timestamp 1 and timestamp 301 both map to:
     *
     *      index = 1
     *
     * Old bucket must be overwritten, not accumulated.
     */
    private static void testCircularSlotReuse() {

        System.out.println("\n=== Test 4: Circular Slot Reuse ===");

        HitCounter counter = new HitCounter();

        counter.hit(1);
        counter.hit(1);

        assertEquals(
                2,
                counter.getHits(1),
                "Two old hits"
        );

        counter.hit(301);

        assertEquals(
                1,
                counter.getHits(301),
                "Old circular bucket correctly replaced"
        );
    }

    /**
     * Tests hits spread across the window.
     */
    private static void testDistributedHits() {

        System.out.println("\n=== Test 5: Distributed Hits ===");

        HitCounter counter = new HitCounter();

        counter.hit(100);
        counter.hit(150);
        counter.hit(200);
        counter.hit(250);
        counter.hit(300);

        assertEquals(
                5,
                counter.getHits(300),
                "All distributed hits initially valid"
        );

        // At 450:
        // timestamp 100 -> age 350 -> expired
        // timestamp 150 -> age 300 -> expired
        // timestamps 200,250,300 -> valid
        assertEquals(
                3,
                counter.getHits(450),
                "Only hits younger than 300 seconds remain"
        );
    }

    /**
     * Tests a large gap between hits.
     */
    private static void testLargeTimeGap() {

        System.out.println("\n=== Test 6: Large Time Gap ===");

        HitCounter counter = new HitCounter();

        counter.hit(1);
        counter.hit(2);
        counter.hit(3);

        assertEquals(
                0,
                counter.getHits(1000),
                "All old hits expire after large gap"
        );

        counter.hit(1000);
        counter.hit(1000);

        assertEquals(
                2,
                counter.getHits(1000),
                "New hits after large gap"
        );
    }

    /**
     * Tests exactly 300 consecutive timestamps.
     */
    private static void testFullWindow() {

        System.out.println("\n=== Test 7: Full 300-Second Window ===");

        HitCounter counter = new HitCounter();

        for (int timestamp = 1; timestamp <= 300; timestamp++) {
            counter.hit(timestamp);
        }

        assertEquals(
                300,
                counter.getHits(300),
                "Exactly 300 seconds of hits"
        );

        // At timestamp 301, timestamp 1 expires.
        assertEquals(
                299,
                counter.getHits(301),
                "Oldest second expires"
        );

        counter.hit(301);

        assertEquals(
                300,
                counter.getHits(301),
                "Window returns to 300 after new hit"
        );
    }

    public static void main(String[] args) {

        testBasicExample();
        testMultipleHitsSameSecond();
        testExactWindowBoundary();
        testCircularSlotReuse();
        testDistributedHits();
        testLargeTimeGap();
        testFullWindow();

        System.out.println("\nALL TESTS PASSED");
    }
}
