package org.chijai.day9.dp.session1;

public class GasStation {

    /*
     * ===============================================================
     * 📘 PRIMARY PROBLEM — LeetCode 134. Gas Station
     * ===============================================================
     *
     * 🔗 https://leetcode.com/problems/gas-station/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Greedy, Array
     *
     * There are n gas stations along a circular route.
     *
     * gas[i]  = gas available at station i
     * cost[i] = gas needed to travel from station i to i + 1
     *
     * You start with an empty tank at one station.
     *
     * Return the starting station index from which you can complete
     * one clockwise circuit.
     *
     * If no such station exists, return -1.
     *
     * If a solution exists, it is guaranteed to be unique.
     *
     * Constraints:
     * n == gas.length == cost.length
     * 1 <= n <= 10^5
     * 0 <= gas[i], cost[i] <= 10^4
     *
     * Example 1:
     *
     * gas  = [1, 2, 3, 4, 5]
     * cost = [3, 4, 5, 1, 2]
     *
     * Output: 3
     *
     * Starting at station 3:
     *
     * station 3: tank = 0 + 4 - 1 = 3
     * station 4: tank = 3 + 5 - 2 = 6
     * station 0: tank = 6 + 1 - 3 = 4
     * station 1: tank = 4 + 2 - 4 = 2
     * station 2: tank = 2 + 3 - 5 = 0
     *
     * Full circuit completed.
     *
     * Example 2:
     *
     * gas  = [2, 3, 4]
     * cost = [3, 4, 3]
     *
     * Output: -1
     */

    /*
     * ===============================================================
     * 🧠 FIRST-PRINCIPLES INVENTION PATH
     * ===============================================================
     *
     * Start with the obvious idea:
     *
     *     Try every station.
     *     Simulate a complete circuit.
     *
     * That is O(n²).
     *
     * To reach O(n), ask:
     *
     *     "When one starting point fails, what work can I permanently
     *      avoid repeating?"
     *
     * Suppose we started at L and our tank first becomes negative
     * after processing station R:
     *
     *     L ---------------------- R
     *
     * Then L cannot be the answer.
     *
     * More importantly, no station between L and R can be the answer.
     *
     * Why?
     *
     * While travelling from L to every station before R, the running
     * tank never became negative.
     *
     * Therefore, starting later inside that range only removes some
     * non-negative fuel contribution that L had already accumulated.
     *
     * So if L still cannot cross R, a later start inside [L, R]
     * cannot cross R either.
     *
     * Therefore:
     *
     *     next candidate = R + 1
     *
     * This is the greedy elimination.
     */

    /*
     * ===============================================================
     * ✅ PRIMARY SOLUTION — EASIEST O(n) GREEDY
     * ===============================================================
     *
     * Keep the two logical questions separate:
     *
     * 1. Is completing the whole circle possible at all?
     * 2. If yes, where should we start?
     *
     * Two passes are still O(n):
     *
     *     O(n) + O(n) = O(n)
     *
     * This version is slightly less compact than the one-pass version,
     * but easier to reconstruct from first principles.
     */

    static class Primary {

        static int canCompleteCircuit(int[] gas, int[] cost) {

            int n = gas.length;

            // -------------------------------------------------------
            // STEP 1: Global feasibility.
            //
            // If all stations together do not provide enough gas to
            // pay the total travel cost, no starting point can work.
            // -------------------------------------------------------

            int totalGas = 0;
            int totalCost = 0;

            for (int i = 0; i < n; i++) {
                totalGas += gas[i];
                totalCost += cost[i];
            }

            if (totalGas < totalCost) {
                return -1;
            }

            // -------------------------------------------------------
            // STEP 2: Find the only surviving starting candidate.
            // -------------------------------------------------------

            int start = 0;
            int tank = 0;

            for (int i = 0; i < n; i++) {

                tank += gas[i];
                tank -= cost[i];

                /*
                 * Starting from 'start', we cannot get past station i.
                 *
                 * Therefore:
                 *
                 *     start, start + 1, ... , i
                 *
                 * are all impossible starting points.
                 *
                 * The next possible candidate is i + 1.
                 */
                if (tank < 0) {
                    start = i + 1;
                    tank = 0;
                }
            }

            /*
             * Why can we directly return start?
             *
             * STEP 1 already proved:
             *
             *     totalGas >= totalCost
             *
             * Therefore, enough gas exists globally to complete
             * one full circle from some starting station.
             *
             * STEP 2 eliminated every starting station that
             * cannot work:
             *
             * whenever tank became negative at station i,
             * every candidate from the current start through i
             * was proven impossible.
             *
             * So after the scan finishes, 'start' is the only
             * surviving candidate.
             *
             * Since STEP 1 proved that a solution must exist,
             * this surviving candidate must be the valid answer.
             *
             * We do NOT need another full-circle simulation.
             */
            return start;
        }

        // Time: O(n) + O(n) = O(n)
        // Space: O(1)
    }

    /*
     * ===============================================================
     * 🔵 CORE PATTERN — GREEDY PREFIX ELIMINATION
     * ===============================================================
     *
     * Trigger:
     *
     *     Failure of one candidate proves that a whole continuous
     *     range of candidates can never work.
     *
     * Rule:
     *
     *     Never retry a candidate that has already been disproved.
     *
     * Here:
     *
     *     tank < 0 at i
     *
     * means:
     *
     *     every start from current start through i is invalid.
     */

    /*
     * ===============================================================
     * 🟢 TWO INVARIANTS
     * ===============================================================
     *
     * 1️⃣ Global feasibility
     *
     *     totalGas >= totalCost
     *
     * must be true for any solution to exist.
     *
     *
     * 2️⃣ Candidate feasibility
     *
     *     tank
     *
     * represents the fuel remaining when travelling from the current
     * candidate 'start' through the current station.
     *
     * If tank becomes negative at i:
     *
     *     start = i + 1
     *     tank = 0
     *
     * because all starts in the failed range are eliminated.
     */

    /*
     * ===============================================================
     * 🔍 WHY CAN WE SKIP EVERY START BETWEEN start AND i?
     * ===============================================================
     *
     * Suppose:
     *
     *     start = L
     *
     * and the first failure happens at R.
     *
     * Before R, the running balance from L never became negative.
     *
     * Example:
     *
     *     net = [+2, -1, -4]
     *
     * Starting at 0:
     *
     *     after 0 ->  2
     *     after 1 ->  1
     *     after 2 -> -3   FAIL
     *
     * Could station 1 work instead?
     *
     *     -1 + -4 = -5   FAIL
     *
     * Starting later removes the positive fuel accumulated before it.
     *
     * Therefore every station in the failed prefix can be discarded.
     */

    /*
     * ===============================================================
     * 🔴 BRUTE FORCE — DIRECT SIMULATION
     * ===============================================================
     *
     * Useful as the first-principles baseline, but too slow for
     * n <= 100000.
     */

    static class BruteForce {

        static int canCompleteCircuit(int[] gas, int[] cost) {

            int n = gas.length;

            for (int start = 0; start < n; start++) {

                int tank = 0;
                boolean failed = false;

                for (int step = 0; step < n; step++) {

                    int current =
                            (start + step) % n;

                    tank += gas[current];
                    tank -= cost[current];

                    if (tank < 0) {
                        failed = true;
                        break;
                    }
                }

                if (!failed) {
                    return start;
                }
            }

            return -1;
        }

        // Time: O(n²)
        // Space: O(1)
    }

    /*
     * ===============================================================
     * 🟡 ALTERNATIVE O(n) — EXPLICIT PREFIX SKIP
     * ===============================================================
     *
     * Same elimination idea, but expressed as repeated simulations.
     *
     * Correct and O(n), but the Primary solution is simpler because
     * it needs only ordinary forward loops.
     */

    static class PrefixSkip {

        static int canCompleteCircuit(int[] gas, int[] cost) {

            int n = gas.length;
            int start = 0;

            while (start < n) {

                int tank = 0;
                int steps = 0;

                while (steps < n) {

                    int current =
                            (start + steps) % n;

                    tank += gas[current];
                    tank -= cost[current];

                    if (tank < 0) {
                        break;
                    }

                    steps++;
                }

                if (steps == n) {
                    return start;
                }

                // All starts through the failed station are invalid.
                start += steps + 1;
            }

            return -1;
        }

        // Time: O(n)
        // Space: O(1)
    }

    /*
     * ===============================================================
     * 🎤 INTERVIEW ARTICULATION
     * ===============================================================
     *
     * "First I check whether total gas is at least total cost.
     *  Otherwise no solution exists.
     *
     *  Then I scan from left to right while maintaining the fuel
     *  balance from my current candidate start.
     *
     *  If that balance becomes negative at station i, none of the
     *  stations from my current start through i can work, so I move
     *  the candidate to i + 1 and reset the tank.
     *
     *  Since each station is processed a constant number of times,
     *  the algorithm is O(n) with O(1) extra space."
     */

    /*
     * ===============================================================
     * 🧭 PATTERN BOUNDARY
     * ===============================================================
     *
     * This greedy works because:
     *
     * - travel direction is fixed
     * - every edge must eventually be traversed
     * - failure invalidates one continuous prefix of candidates
     *
     * Do NOT blindly apply this when:
     *
     * - backward movement is allowed
     * - choices can revisit earlier states
     * - failure does not eliminate a whole candidate range
     */

    /*
     * ===============================================================
     * 🔗 RELATED PROBLEMS
     * ===============================================================
     *
     * These are related by greedy elimination / irreversible progress,
     * but they are NOT the exact same invariant.
     */

    // ---------------------------------------------------------------
    // LeetCode 55. Jump Game
    //
    // Invariant:
    // maxReach = farthest index reachable so far.
    // ---------------------------------------------------------------

    static class JumpGame {

        static boolean canJump(int[] nums) {

            int maxReach = 0;

            for (int i = 0; i < nums.length; i++) {

                if (i > maxReach) {
                    return false;
                }

                maxReach =
                        Math.max(maxReach, i + nums[i]);
            }

            return true;
        }
    }

    // ---------------------------------------------------------------
    // LeetCode 45. Jump Game II
    //
    // Greedy level/range expansion.
    // ---------------------------------------------------------------

    static class JumpGameII {

        static int jump(int[] nums) {

            int jumps = 0;
            int currentRangeEnd = 0;
            int farthest = 0;

            for (int i = 0; i < nums.length - 1; i++) {

                farthest =
                        Math.max(farthest, i + nums[i]);

                if (i == currentRangeEnd) {
                    jumps++;
                    currentRangeEnd = farthest;
                }
            }

            return jumps;
        }
    }

    // ---------------------------------------------------------------
    // LeetCode 605. Can Place Flowers
    //
    // Local greedy choice:
    // plant whenever the current position is safely available.
    // ---------------------------------------------------------------

    static class CanPlaceFlowers {

        static boolean canPlaceFlowers(int[] flowerbed, int n) {

            for (int i = 0;
                 i < flowerbed.length && n > 0;
                 i++) {

                boolean currentEmpty =
                        flowerbed[i] == 0;

                boolean leftEmpty =
                        i == 0
                                || flowerbed[i - 1] == 0;

                boolean rightEmpty =
                        i == flowerbed.length - 1
                                || flowerbed[i + 1] == 0;

                if (currentEmpty
                        && leftEmpty
                        && rightEmpty) {

                    flowerbed[i] = 1;
                    n--;
                }
            }

            return n == 0;
        }
    }

    /*
     * ===============================================================
     * 🧠 LEARNING TRANSFER CHECKLIST
     * ===============================================================
     *
     * When you see a greedy-looking problem, ask:
     *
     * 1. If one candidate fails, does that prove several candidates
     *    are impossible?
     *
     * 2. Can those candidates be permanently discarded?
     *
     * 3. Is there a separate global condition that tells me whether
     *    any solution can exist?
     *
     * If yes, greedy elimination may be available.
     */

    /*
     * ===============================================================
     * 🧪 SANITY TESTS
     * ===============================================================
     */

    public static void main(String[] args) {

        assertEq(
                3,
                Primary.canCompleteCircuit(
                        new int[]{1, 2, 3, 4, 5},
                        new int[]{3, 4, 5, 1, 2}),
                "Example 1");

        assertEq(
                -1,
                Primary.canCompleteCircuit(
                        new int[]{2, 3, 4},
                        new int[]{3, 4, 3}),
                "Example 2");

        assertEq(
                -1,
                Primary.canCompleteCircuit(
                        new int[]{
                                2, 6, 3, 6, 8, 1, 4,
                                6, 7, 1, 4, 3, 1
                        },
                        new int[]{
                                4, 8, 4, 11, 5, 2, 8,
                                16, 3, 4, 6, 7, 6
                        }),
                "Impossible larger case");

        assertEq(
                0,
                Primary.canCompleteCircuit(
                        new int[]{5},
                        new int[]{4}),
                "Single station");

        assertEq(
                0,
                Primary.canCompleteCircuit(
                        new int[]{1},
                        new int[]{1}),
                "Exact fuel");

        /*
         * Cross-check Primary against brute force on representative
         * examples.
         */
        crossCheck(
                new int[]{1, 2, 3, 4, 5},
                new int[]{3, 4, 5, 1, 2});

        crossCheck(
                new int[]{2, 3, 4},
                new int[]{3, 4, 3});

        crossCheck(
                new int[]{5, 1, 2, 3, 4},
                new int[]{4, 4, 1, 5, 1});

        System.out.println("✅ Gas Station tests passed");

        if (!JumpGame.canJump(
                new int[]{2, 3, 1, 1, 4})) {
            throw new AssertionError(
                    "Jump Game failed");
        }

        if (JumpGameII.jump(
                new int[]{2, 3, 1, 1, 4}) != 2) {
            throw new AssertionError(
                    "Jump Game II failed");
        }

        if (!CanPlaceFlowers.canPlaceFlowers(
                new int[]{1, 0, 0, 0, 1},
                1)) {
            throw new AssertionError(
                    "Can Place Flowers failed");
        }

        System.out.println(
                "✅ Related problem tests passed");
    }

    private static void crossCheck(
            int[] gas,
            int[] cost) {

        int expected =
                BruteForce.canCompleteCircuit(
                        gas.clone(),
                        cost.clone());

        int actual =
                Primary.canCompleteCircuit(
                        gas.clone(),
                        cost.clone());

        if (expected != actual) {
            throw new AssertionError(
                    "Primary and brute force disagree");
        }
    }

    private static void assertEq(
            int expected,
            int actual,
            String name) {

        if (expected != actual) {
            throw new AssertionError(
                    name
                            + " failed. Expected: "
                            + expected
                            + ", Actual: "
                            + actual);
        }
    }
}
