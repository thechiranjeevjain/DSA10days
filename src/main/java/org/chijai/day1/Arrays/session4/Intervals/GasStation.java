package org.chijai.day1.Arrays.session4.Intervals;

public class GasStation {

    // ===============================================================
    // 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
    // ===============================================================
    /*
     * LeetCode 134. Gas Station
     *
     * 🔗 https://leetcode.com/problems/gas-station/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Greedy, Array
     *
     * There are n gas stations along a circular route, where the amount
     * of gas at the ith station is gas[i].
     *
     * You have a car with an unlimited gas tank and it costs cost[i] of
     * gas to travel from the ith station to its next (i + 1)th station.
     * You begin the journey with an empty tank at one of the gas stations.
     *
     * Given two integer arrays gas and cost, return the starting gas
     * station's index if you can travel around the circuit once in the
     * clockwise direction, otherwise return -1.
     *
     * If there exists a solution, it is guaranteed to be unique.
     *
     * Constraints:
     * n == gas.length == cost.length
     * 1 <= n <= 10^5
     * 0 <= gas[i], cost[i] <= 10^4
     */

    // ===============================================================
    // 🔵 CORE PATTERN OVERVIEW
    // ===============================================================
    /*
     * Pattern: Greedy Prefix Elimination
     *
     * Core Idea:
     * This is NOT a traversal problem.
     * This is an elimination problem.
     *
     * We are not asking:
     * "Can I complete the circuit from here?"
     *
     * We are asking:
     * "Which starting positions are impossible forever?"
     *
     * Once a start fails, a whole prefix becomes invalid.
     */

    // ===============================================================
    // 🟢 MENTAL MODEL & INVARIANTS
    // ===============================================================
    /*
     * Mental Model:
     * Think in net balance, not movement.
     *
     * Let net[i] = gas[i] - cost[i]
     *
     * Invariants:
     *
     * 1️⃣ Global feasibility:
     *     sum(net[i]) >= 0 must hold, or no solution exists.
     *
     * 2️⃣ Prefix failure invariant:
     *     If starting at index L fails at index R,
     *     then all indices in [L, R] are invalid starts.
     *
     * 3️⃣ Performance invariant:
     *     Once a prefix is invalidated, it must never be revisited.
     */

    // ===============================================================
    // 🔴 WHY NAIVE / SIMULATION FAILS
    // ===============================================================
    /*
     * Simulation tries every start.
     *
     * Even if logically correct, it:
     * - Re-simulates already failed prefixes
     * - Recounts the same net differences
     *
     * Worst-case time: O(n²)
     *
     * Interview trap:
     * Passes almost all tests, TLEs on last.
     */

    // ===============================================================
    // 6️⃣ SOLUTION TIERS
    // ===============================================================

    // ---------------------------------------------------------------
    // 🔹 Brute Force
    // ---------------------------------------------------------------
    static class BruteForce {
        static int canCompleteCircuit(int[] gas, int[] cost) {
            int n = gas.length;

            for (int start = 0; start < n; start++) {
                int tank = 0;
                boolean failed = false;

                for (int step = 0; step < n; step++) {
                    int idx = (start + step) % n;
                    tank += gas[idx] - cost[idx];
                    if (tank < 0) {
                        failed = true;
                        break;
                    }
                }
                if (!failed) return start;
            }
            return -1;
        }
        // Time: O(n²), Space: O(1)
    }

    // ---------------------------------------------------------------
    // 🔹 Improved (Prefix Skip)
    // ---------------------------------------------------------------
    static class Improved {
        static int canCompleteCircuit(int[] gas, int[] cost) {
            int n = gas.length;
            int start = 0;

            while (start < n) {
                int tank = 0;
                int steps = 0;

                while (steps < n) {
                    int idx = (start + steps) % n;
                    tank += gas[idx] - cost[idx];
                    if (tank < 0) break;
                    steps++;
                }

                if (steps == n) return start;
                start += steps + 1; // skip invalid prefix
            }
            return -1;
        }
        // Time: O(n), Space: O(1)
    }

    // ---------------------------------------------------------------
    // 🔹 Optimal (Interview Preferred)
    // ---------------------------------------------------------------
    static class Optimal {
        static int canCompleteCircuit(int[] gas, int[] cost) {

            int totalNet = 0;
            int runningTank = 0;
            int startIndex = 0;

            for (int i = 0; i < gas.length; i++) {
                int net = gas[i] - cost[i];
                totalNet += net;
                runningTank += net;

                if (runningTank < 0) {
                    startIndex = i + 1;
                    runningTank = 0;
                }
            }
            return totalNet >= 0 ? startIndex : -1;
        }
        // Time: O(n), Space: O(1)
    }

    /**
     * ============================================================================
     * Gas Station (Greedy)
     * ============================================================================
     *
     * Mental Model
     * ------------
     * We maintain ONE candidate starting station.
     *
     * As we move forward, we simulate the journey from that candidate.
     *
     * If the tank ever becomes negative, the current candidate cannot complete
     * the journey, and neither can any station between the candidate and the
     * current station.
     *
     * Therefore, we discard that entire range and choose the next station as
     * the new candidate.
     *
     * ---------------------------------------------------------------------------
     * Key Observation
     * ---------------------------------------------------------------------------
     *
     * net = gas[i] - cost[i]
     *
     * Positive net  -> Gain fuel
     * Negative net  -> Lose fuel
     *
     * Instead of separately adding gas and subtracting cost, we only track the
     * net change in the fuel tank.
     *
     * ---------------------------------------------------------------------------
     * State
     * ---------------------------------------------------------------------------
     *
     * totalNet
     *      Total net fuel across the entire circle.
     *
     * runningTank
     *      Fuel remaining while simulating from the current candidate.
     *
     * startIndex
     *      Current candidate starting station.
     *
     * ---------------------------------------------------------------------------
     * Invariant
     * ---------------------------------------------------------------------------
     *
     * Before every iteration:
     *
     * 1. startIndex is the only remaining candidate.
     *
     * 2. runningTank is the fuel remaining if we started from startIndex.
     *
     * 3. runningTank is never negative.
     *
     * ---------------------------------------------------------------------------
     * Greedy Insight
     * ---------------------------------------------------------------------------
     *
     * If runningTank becomes negative at station i:
     *
     *      startIndex -------------> i
     *          ❌  ❌  ❌  ❌  ❌
     *
     * Every station in this interval is impossible.
     *
     * Therefore:
     *
     *      startIndex = i + 1
     *      runningTank = 0
     *
     * ---------------------------------------------------------------------------
     * Why totalNet?
     * ---------------------------------------------------------------------------
     *
     * runningTank answers:
     *
     *      "Can my current candidate survive?"
     *
     * totalNet answers:
     *
     *      "Does any solution exist?"
     *
     * If totalNet < 0
     *
     *      Total Gas < Total Cost
     *
     *      ⇒ Impossible to complete the circuit.
     *
     * Otherwise, the greedy proof guarantees that the final startIndex is the
     * unique valid starting station.
     *
     * ---------------------------------------------------------------------------
     * Complexity
     * ---------------------------------------------------------------------------
     *
     * Time  : O(n)
     * Space : O(1)
     *
     * ============================================================================
     */

    // ===============================================================
    // 🟣 INTERVIEW ARTICULATION
    // ===============================================================
    /*
     * If total gas is insufficient, return -1.
     * Otherwise, traverse once.
     * Whenever cumulative gas becomes negative,
     * discard all previous start candidates.
     */


    // ===============================================================
    // 🧠 CHAPTER COMPLETION CHECKLIST (ANSWERED)
    // ===============================================================
    /*
     * Invariant clarity → cumulative net gas must never go negative
     * Search target → first index after last failed prefix
     * Discard logic → failure at i invalidates all ≤ i
     * Termination → single forward scan
     * Failure awareness → simulation re-visits dead prefixes
     * Edge cases → global sum check handles impossibility
     * Pattern boundary → breaks if backward travel allowed
     */

    // ===============================================================
    // 🧘 FINAL CLOSURE
    // ===============================================================
    /*
     * The invariant is prefix feasibility.
     * The answer is the first viable start after elimination.
     * I can re-derive this under pressure.
     * This chapter is complete.
     */

    // ===============================================================
    // 🔵 PATTERN DOCTRINE
    // ===============================================================
    /*
     * Pattern: Greedy Prefix Elimination
     *
     * Trigger:
     * Failure at position i invalidates a continuous range of candidates.
     *
     * Rule:
     * Never re-evaluate an invalidated prefix.
     */

    // ===============================================================
    // 🔄 VARIATIONS & TWEAKS
    // ===============================================================
    /*
     * 🟢 Invariant-preserving:
     * - Forward-only traversal
     * - Prefix sum tracking
     *
     * 🟡 Reasoning-only:
     * - Different variable names
     * - Different failure semantics
     *
     * 🔴 Pattern breaks when:
     * - Backtracking is allowed
     * - Partial traversal is acceptable
     */

    // ===============================================================
    // ⚫ REINFORCEMENT PROBLEM 1 — Jump Game
    // ===============================================================
    /*
     * LeetCode 55. Jump Game
     *
     * Same pattern:
     * If index i is unreachable, all indices > i are unreachable.
     */

    static class JumpGame {
        static boolean canJump(int[] nums) {
            int maxReach = 0;
            for (int i = 0; i < nums.length; i++) {
                if (i > maxReach) return false;
                maxReach = Math.max(maxReach, i + nums[i]);
            }
            return true;
        }
    }

    // ===============================================================
    // ⚫ REINFORCEMENT PROBLEM 2 — Jump Game II
    // ===============================================================
    static class JumpGameII {
        static int jump(int[] nums) {
            int jumps = 0, end = 0, farthest = 0;

            for (int i = 0; i < nums.length - 1; i++) {
                farthest = Math.max(farthest, i + nums[i]);
                if (i == end) {
                    jumps++;
                    end = farthest;
                }
            }
            return jumps;
        }
    }

    // ===============================================================
    // ⚫ REINFORCEMENT PROBLEM 3 — Can Place Flowers
    // ===============================================================
    static class CanPlaceFlowers {
        static boolean canPlaceFlowers(int[] bed, int n) {
            for (int i = 0; i < bed.length && n > 0; i++) {
                if (bed[i] == 0 &&
                        (i == 0 || bed[i - 1] == 0) &&
                        (i == bed.length - 1 || bed[i + 1] == 0)) {
                    bed[i] = 1;
                    n--;
                }
            }
            return n == 0;
        }
    }

    // ===============================================================
    // 🧩 RELATED PROBLEMS (PATTERN BOUNDARIES)
    // ===============================================================
    /*
     * ❌ Circular Array Loop
     * Prefix elimination fails due to direction changes.
     *
     * ❌ Split Array Largest Sum
     * Requires binary search + greedy check.
     */

    // ===============================================================
    // 🧠 LEARNING TRANSFER CHECKLIST
    // ===============================================================
    /*
     * Ask:
     * 1. Does failure kill a range?
     * 2. Is re-visiting wasteful?
     * 3. Can I prove impossibility globally?
     *
     * If yes → Greedy Prefix Elimination.
     */

    // ===============================================================
    // 🧪 SANITY TESTS
    // ===============================================================
    public static void main(String[] args) {


        assertEq(3, Optimal.canCompleteCircuit(
                new int[]{1, 2, 3, 4, 5},
                new int[]{3, 4, 5, 1, 2}), "Example 1");

        assertEq(-1, Optimal.canCompleteCircuit(
                new int[]{2, 3, 4},
                new int[]{3, 4, 3}), "Example 2");

        assertEq(-1, Optimal.canCompleteCircuit(
                new int[]{2, 6, 3, 6, 8, 1, 4, 6, 7, 1, 4, 3, 1},
                new int[]{4, 8, 4, 11, 5, 2, 8, 16, 3, 4, 6, 7, 6}), "TLE Trap");

        System.out.println("✅ Core Chapter tests passed");

        if (!JumpGame.canJump(new int[]{2, 3, 1, 1, 4}))
            throw new AssertionError("Jump Game failed");

        if (JumpGameII.jump(new int[]{2, 3, 1, 1, 4}) != 2)
            throw new AssertionError("Jump Game II failed");

        if (!CanPlaceFlowers.canPlaceFlowers(new int[]{1, 0, 0, 0, 1}, 1))
            throw new AssertionError("Can Place Flowers failed");


        System.out.println("✅ Pattern Companion tests passed");
    }

    private static void assertEq(int e, int a, String name) {
        if (e != a) throw new AssertionError(name + " failed");
    }
}
