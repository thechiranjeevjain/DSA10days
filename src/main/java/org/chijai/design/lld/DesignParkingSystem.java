package org.chijai.design.lld;

/**
 * LeetCode 1603 - Design Parking System
 *
 * -------------------------------------------------------------------------
 * CORE IDEA
 * -------------------------------------------------------------------------
 *
 * There are only 3 fixed parking-space types:
 *
 *      1 = BIG
 *      2 = MEDIUM
 *      3 = SMALL
 *
 * A car can ONLY park in the exact matching slot type.
 *
 * So the entire state is just:
 *
 *      remainingBig
 *      remainingMedium
 *      remainingSmall
 *
 * No HashMap.
 * No Queue.
 * No TreeMap.
 * No object per slot.
 *
 * -------------------------------------------------------------------------
 * INVARIANT
 * -------------------------------------------------------------------------
 *
 * Each counter stores:
 *
 *      number of currently AVAILABLE slots of that type
 *
 * If counter > 0:
 *
 *      decrement it
 *      return true
 *
 * Otherwise:
 *
 *      return false
 *
 * -------------------------------------------------------------------------
 * COMPLEXITY
 * -------------------------------------------------------------------------
 *
 * Constructor:
 *      O(1)
 *
 * addCar():
 *      O(1)
 *
 * Space:
 *      O(1)
 *
 * -------------------------------------------------------------------------
 * INTERVIEW NOTE
 * -------------------------------------------------------------------------
 *
 * Do not over-engineer this problem.
 *
 * The constraints and requirements do NOT require:
 *
 * - tracking slot IDs
 * - removing cars
 * - reservations
 * - nearest-slot lookup
 * - concurrency
 * - multiple floors
 *
 * If those requirements appeared, the design would change.
 *
 * For THIS problem:
 *
 *      3 counters = complete solution.
 */
public class DesignParkingSystem {

    static class ParkingSystem {

        private int remainingBig;
        private int remainingMedium;
        private int remainingSmall;

        /**
         * Initialize available capacity for each slot type.
         */
        public ParkingSystem(int big, int medium, int small) {
            this.remainingBig = big;
            this.remainingMedium = medium;
            this.remainingSmall = small;
        }

        /**
         * Try to park one car.
         *
         * carType:
         *      1 -> big
         *      2 -> medium
         *      3 -> small
         *
         * Return true only if a matching slot was available.
         */
        public boolean addCar(int carType) {

            switch (carType) {

                case 1:
                    if (remainingBig == 0) {
                        return false;
                    }

                    remainingBig--;
                    return true;

                case 2:
                    if (remainingMedium == 0) {
                        return false;
                    }

                    remainingMedium--;
                    return true;

                case 3:
                    if (remainingSmall == 0) {
                        return false;
                    }

                    remainingSmall--;
                    return true;

                default:
                    /*
                     * LeetCode guarantees carType is 1, 2, or 3.
                     * Keeping this branch makes the implementation defensive.
                     */
                    return false;
            }
        }
    }

    // ---------------------------------------------------------------------
    // TEST HARNESS
    // ---------------------------------------------------------------------

    private static void assertEquals(
            boolean expected,
            boolean actual,
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
     * Exact LeetCode example.
     */
    private static void testLeetCodeExample() {

        System.out.println("\n=== Test 1: LeetCode Example ===");

        ParkingSystem parkingSystem = new ParkingSystem(1, 1, 0);

        assertEquals(
                true,
                parkingSystem.addCar(1),
                "Big car gets only big slot"
        );

        assertEquals(
                true,
                parkingSystem.addCar(2),
                "Medium car gets only medium slot"
        );

        assertEquals(
                false,
                parkingSystem.addCar(3),
                "No small slot available"
        );

        assertEquals(
                false,
                parkingSystem.addCar(1),
                "Big slot already occupied"
        );
    }

    /**
     * Zero capacity for every type.
     */
    private static void testAllZeroCapacity() {

        System.out.println("\n=== Test 2: All Zero Capacity ===");

        ParkingSystem parkingSystem = new ParkingSystem(0, 0, 0);

        assertEquals(
                false,
                parkingSystem.addCar(1),
                "No big capacity"
        );

        assertEquals(
                false,
                parkingSystem.addCar(2),
                "No medium capacity"
        );

        assertEquals(
                false,
                parkingSystem.addCar(3),
                "No small capacity"
        );
    }

    /**
     * Multiple slots of the same type.
     */
    private static void testMultipleBigSlots() {

        System.out.println("\n=== Test 3: Multiple Big Slots ===");

        ParkingSystem parkingSystem = new ParkingSystem(2, 0, 0);

        assertEquals(
                true,
                parkingSystem.addCar(1),
                "First big car"
        );

        assertEquals(
                true,
                parkingSystem.addCar(1),
                "Second big car"
        );

        assertEquals(
                false,
                parkingSystem.addCar(1),
                "Third big car exceeds capacity"
        );
    }

    /**
     * Proves there is NO fallback between types.
     *
     * A small car cannot consume a medium or big slot.
     * A medium car cannot consume a big slot.
     */
    private static void testNoCrossTypeParking() {

        System.out.println("\n=== Test 4: No Cross-Type Parking ===");

        ParkingSystem parkingSystem = new ParkingSystem(1, 1, 0);

        assertEquals(
                false,
                parkingSystem.addCar(3),
                "Small car cannot use larger slot"
        );

        assertEquals(
                true,
                parkingSystem.addCar(1),
                "Big slot remains available for big car"
        );

        assertEquals(
                true,
                parkingSystem.addCar(2),
                "Medium slot remains available for medium car"
        );
    }

    /**
     * Independent counters should not affect each other.
     */
    private static void testIndependentCapacities() {

        System.out.println("\n=== Test 5: Independent Capacities ===");

        ParkingSystem parkingSystem = new ParkingSystem(1, 2, 1);

        assertEquals(
                true,
                parkingSystem.addCar(2),
                "First medium"
        );

        assertEquals(
                true,
                parkingSystem.addCar(3),
                "Small remains independent"
        );

        assertEquals(
                true,
                parkingSystem.addCar(2),
                "Second medium"
        );

        assertEquals(
                false,
                parkingSystem.addCar(2),
                "Medium exhausted"
        );

        assertEquals(
                true,
                parkingSystem.addCar(1),
                "Big still available"
        );
    }

    /**
     * Defensive invalid-input behavior.
     *
     * LeetCode itself guarantees only 1, 2, or 3.
     */
    private static void testInvalidTypeDefensively() {

        System.out.println("\n=== Test 6: Defensive Invalid Type ===");

        ParkingSystem parkingSystem = new ParkingSystem(1, 1, 1);

        assertEquals(
                false,
                parkingSystem.addCar(99),
                "Invalid car type rejected"
        );

        // Capacities must remain unchanged.
        assertEquals(
                true,
                parkingSystem.addCar(1),
                "Big capacity unaffected"
        );

        assertEquals(
                true,
                parkingSystem.addCar(2),
                "Medium capacity unaffected"
        );

        assertEquals(
                true,
                parkingSystem.addCar(3),
                "Small capacity unaffected"
        );
    }

    public static void main(String[] args) {

        testLeetCodeExample();
        testAllZeroCapacity();
        testMultipleBigSlots();
        testNoCrossTypeParking();
        testIndependentCapacities();
        testInvalidTypeDefensively();

        System.out.println("\nALL TESTS PASSED");
    }
}
