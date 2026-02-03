package org.chijai.day2.session2;

/**
 * ===============================================================
 * 📘 ALGORITHM TEXTBOOK CHAPTER
 * ===============================================================
 * <p>
 * Pattern: Binary Search on Answer Space (Monotonic Predicate)
 * Primary Problem: Koko Eating Bananas
 * <p>
 * This file is a COMPLETE algorithm chapter.
 * It is intentionally verbose, structured, and interview-grade.
 * <p>
 * ===============================================================
 */
public class KokoBananas {

    // ===============================================================
    // 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
    // ===============================================================

    /*
     * Koko Eating Bananas
     * https://leetcode.com/problems/koko-eating-bananas/
     *
     * Difficulty: Medium
     * Tags: Array, Binary Search
     *
     * ---------------------------------------------------------------
     * Description:
     *
     * Koko loves to eat bananas. There are n piles of bananas,
     * the i-th pile has piles[i] bananas.
     *
     * The guards have gone and will come back in h hours.
     *
     * Koko can decide her bananas-per-hour eating speed of k.
     * Each hour, she chooses some pile of bananas and eats k bananas
     * from that pile. If the pile has less than k bananas, she eats
     * all of them instead and will not eat any more bananas during
     * this hour.
     *
     * Koko likes to eat slowly but still wants to finish eating all
     * the bananas before the guards return.
     *
     * Return the minimum integer k such that she can eat all the
     * bananas within h hours.
     *
     * ---------------------------------------------------------------
     * Example 1:
     * Input: piles = [3,6,7,11], h = 8
     * Output: 4
     *
     * ---------------------------------------------------------------
     * Example 2:
     * Input: piles = [30,11,23,4,20], h = 5
     * Output: 30
     *
     * ---------------------------------------------------------------
     * Example 3:
     * Input: piles = [30,11,23,4,20], h = 6
     * Output: 23
     *
     * ---------------------------------------------------------------
     * Constraints:
     * 1 <= piles.length <= 10^4
     * piles.length <= h <= 10^9
     * 1 <= piles[i] <= 10^9
     */

    // ===============================================================
    // 3️⃣ 🔵 CORE PATTERN OVERVIEW
    // ===============================================================

    /*
     * 🔵 Pattern Name:
     * Binary Search on Answer Space (Monotonic Predicate)
     *
     * 🔵 Core Idea:
     * We are not searching indices.
     * We are searching the smallest VALUE that satisfies a condition.
     *
     * 🔵 Why It Works:
     * As eating speed increases, required hours monotonically decrease.
     *
     * 🔵 When to Use:
     * • “minimum X such that …”
     * • X lies in a numeric range
     * • feasibility can be checked
     *
     * 🔵 Pattern Recognition Signals:
     * • constraints up to 1e9
     * • brute force too slow
     * • clear true/false feasibility check
     *
     * 🔵 Difference from Classic Binary Search:
     * • classic → search sorted array
     * • this → search numeric answer space
     *
     *  ===============================================================
    // 🔵 WHAT DOES “MONOTONICITY” ACTUALLY MEAN?
    // ===============================================================

    /*
     * 🔵 Monotonicity = moving in one direction only.
     *
     * Something is monotonic if, as the input increases:
     *
     * • it never goes back and forth
     * • it never oscillates
     * • it never improves and then worsens
     *
     * It can be:
     * • always increasing
     * • always decreasing
     * • false → true (only once)
     *
     * ---------------------------------------------------------------
     * 🟣 The Interview Definition (this is the one that matters):
     *
     * A condition is monotonic if, once it becomes true,
     * it stays true forever as the input increases.
     *
     * That’s it.
     *
     * ---------------------------------------------------------------
     * 🔵 Why Binary Search Works ONLY Because of Monotonicity:
     *
     * Binary search assumes this structure:
     *
     * false false false false | true true true true
     *
     * There is exactly ONE boundary.
     *
     * If the condition looked like this:
     *
     * false true false true
     *
     * Binary search would be INVALID.
     *
     * ---------------------------------------------------------------
     * 🟢 Example — Koko Eating Bananas:
     *
     * Speed k:
     * k = 1  → ❌
     * k = 2  → ❌
     * k = 3  → ❌
     * k = 4  → ✅
     * k = 5  → ✅
     * k = 6  → ✅
     *
     * Once it becomes possible, it stays possible.
     * That’s monotonicity.
     *
     * ---------------------------------------------------------------
     * 🟢 Example — Bouquet Problem:
     *
     * Day:
     * Day 1 → ❌
     * Day 2 → ❌
     * Day 3 → ✅
     * Day 4 → ✅
     * Day 5 → ✅
     *
     * Again: one flip, no going back.
     *
     * ---------------------------------------------------------------
     * 🔴 What Monotonicity is NOT (very important):
     *
     * ❌ “Sometimes true, sometimes false”
     * ❌ “Gets better then worse”
     * ❌ “Depends on position randomly”
     *
     * Example that is NOT monotonic:
     *
     * “Is today a holiday?”
     *
     * false true false false true
     *
     * Binary search CANNOT be used here.
     *
     * ---------------------------------------------------------------
     * 🟣 The One-Question Test (use this in interviews):
     *
     * Whenever you think “binary search”, ask yourself:
     *
     * ❓ “If this works for X, will it definitely work for all X + 1?”
     *
     * If YES → monotonic → binary search allowed
     * If NO  → stop immediately
     */

    // ===============================================================
    // 4️⃣ 🟢 MENTAL MODEL & INVARIANTS
    // ===============================================================

    /*
     * 🟢 Mental Model:
     *
     * Imagine speeds laid out on a number line.
     *
     * Too slow → Koko fails ❌
     * Fast enough → Koko succeeds ✅
     *
     * There exists a sharp boundary:
     *   ❌ ❌ ❌ ❌ | ✅ ✅ ✅
     *
     * We must find the FIRST ✅.
     *
     * ---------------------------------------------------------------
     * 🟢 Invariants:
     *
     * • Speed is ALWAYS >= 1
     * • left = smallest candidate speed
     * • right = largest possible speed
     * • If a speed works, all higher speeds work
     *
     * ---------------------------------------------------------------
     * 🟢 Role of Variables:
     *
     * left  → lowest feasible candidate
     * right → upper bound of search space
     * mid   → candidate speed being tested
     *
     * ---------------------------------------------------------------
     * 🟢 Termination:
     *
     * When left > right, search space is exhausted.
     *
     * ---------------------------------------------------------------
     * 🟢 Forbidden Actions:
     *
     * ❌ speed = 0 (division undefined)
     * ❌ non-monotonic predicate
     *
     * ---------------------------------------------------------------
     * 🟢 Why Alternatives Are Inferior:
     *
     * • linear scan → TLE
     * • guessing speed → unverifiable
     */

    // ===============================================================
    // 5️⃣ 🔴 WHY NAIVE / WRONG SOLUTIONS FAIL (FORENSIC)
    // ===============================================================

    /*
     * 🔴 Wrong Approach #1:
     * Start binary search with left = 0.
     *
     * 🔴 Why It Seems Correct:
     * “0 is smallest integer”
     *
     * 🔴 Why It Fails:
     * Division by zero OR invalid feasibility.
     *
     * ---------------------------------------------------------------
     * 🔴 Wrong Approach #2:
     * Using floating-point without guarding invariants.
     *
     * 🔴 Failure Mode:
     * Precision issues + broken monotonicity.
     *
     * ---------------------------------------------------------------
     * 🔴 Interviewer Trap:
     * They want to see if you define a VALID search space,
     * not just if you know binary search syntax.
     */

    // ===============================================================
    // 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES
    // ===============================================================

    /**
     * ---------------------------------------------------------------
     * Brute Force Solution
     * ---------------------------------------------------------------
     */
    static class BruteForce {

        /*
         * 🟡 Core Idea:
         * Try every possible eating speed.
         *
         * 🟡 Limitation:
         * maxPile can be up to 1e9 → impossible.
         *
         * Time: O(n * maxPile)
         * Space: O(1)
         * Interview: ❌
         */
        static int minEatingSpeed(int[] piles, int h) {

            int maxPile = 0;
            for (int pile : piles) {
                maxPile = Math.max(maxPile, pile);
            }

            for (int speed = 1; speed <= maxPile; speed++) {
                if (canFinish(piles, h, speed)) {
                    return speed;
                }
            }
            return -1;
        }
    }

    /**
     * ---------------------------------------------------------------
     * Optimal Solution (Interview-Preferred)
     * ---------------------------------------------------------------
     */
    static class Optimal {

        /*
         * 🟢 Core Idea:
         * Binary search on eating speed.
         *
         * Time: O(n log maxPile)
         * Space: O(1)
         * Interview: ✅ Preferred
         */
        static int minEatingSpeed(int[] piles, int h) {

            int left = 1;     // 🟢 invariant: speed >= 1
            int right = 0;
            int answer = -1;

            for (int pile : piles) {
                right = Math.max(right, pile);
            }

            while (left <= right) {

                int midSpeed = left + (right - left) / 2;
                long requiredHours = 0;

                for (int pile : piles) {
                    /*
                     * 🟢 Ceiling division using Math.ceil.
                     *
                     * Safe because:
                     * • midSpeed >= 1 by invariant
                     * • explicit double conversion
                     * • cast to long avoids overflow
                     */
                    requiredHours += (long) Math.ceil((double) pile / midSpeed);
                }

                if (requiredHours > h) {
                    // 🔴 speed too slow → move right
                    left = midSpeed + 1;
                } else {
                    // 🟢 speed works → try smaller
                    answer = midSpeed;
                    right = midSpeed - 1;
                }
            }
            return answer;
        }
    }

// ===============================================================
// 7️⃣ 🟣 INTERVIEW ARTICULATION
// ===============================================================

    /*
     * 🟣 How to explain:
     *
     * “I binary search the minimum eating speed.
     *  For a given speed, I check if total hours needed
     *  is within h. This predicate is monotonic.”
     *
     * ---------------------------------------------------------------
     * 🟣 Correctness Invariant:
     *
     * If speed k works, any speed > k also works.
     *
     * ---------------------------------------------------------------
     * 🟣 What breaks if changed:
     *
     * Starting from speed 0 → invariant violation.
     *
     * ---------------------------------------------------------------
     * 🟣 When NOT to use this pattern:
     *
     * If feasibility is non-monotonic.
     */

// ===============================================================
// 8️⃣ 🔄 VARIATIONS & TWEAKS
// ===============================================================

    /*
     * 🟢 Invariant-Preserving Changes:
     * • Replace Math.ceil with integer ceiling
     * • Tighten right bound if known
     *
     * 🟡 Reasoning-Only Changes:
     * • Different feasibility check
     *
     * 🔴 Pattern-Break Signals:
     * • Required hours fluctuate with speed
     * • No clear true/false boundary
     */

// ===============================================================
// ⛔ STOP HERE — Reinforcement Problems start in PART 2
// ===============================================================

    // ===============================================================
    // 9️⃣ ⚫ REINFORCEMENT PROBLEMS
    // ===============================================================

    // ===============================================================
    // Reinforcement Problem 1
    // ===============================================================

    /*
     * ---------------------------------------------------------------
     * 📘 Capacity To Ship Packages Within D Days
     * https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/
     *
     * Difficulty: Medium
     * Tags: Array, Binary Search
     *
     * ---------------------------------------------------------------
     * Description:
     *
     * A conveyor belt has packages that must be shipped from one port
     * to another within D days.
     *
     * The i-th package has a weight of weights[i].
     * Each day, we load the ship with packages in the given order.
     * The ship has a maximum weight capacity.
     *
     * Return the least weight capacity of the ship that will result
     * in all the packages being shipped within D days.
     *
     * ---------------------------------------------------------------
     * Example:
     * Input: weights = [1,2,3,4,5,6,7,8,9,10], D = 5
     * Output: 15
     *
     * ---------------------------------------------------------------
     * Constraints:
     * 1 <= weights.length <= 5 * 10^4
     * 1 <= weights[i] <= 500
     * weights.length <= D <= 10^9
     */

    /*
     * ⚫ PATTERN MAPPING
     *
     * • Answer space = ship capacity
     * • Predicate = can we ship within D days?
     * • Capacity ↑ ⇒ required days ↓ (monotonic)
     *
     * Invariant carried:
     * If capacity works, any larger capacity works.
     */

    static class ShipPackages {

        static int shipWithinDays(int[] weights, int days) {

            int left = 0;   // minimum capacity must handle max weight
            int right = 0;  // maximum capacity = sum of all weights

            for (int w : weights) {
                left = Math.max(left, w);
                right += w;
            }

            int answer = -1;

            while (left <= right) {
                int midCapacity = left + (right - left) / 2;

                if (canShip(weights, days, midCapacity)) {
                    answer = midCapacity;
                    right = midCapacity - 1;
                } else {
                    left = midCapacity + 1;
                }
            }
            return answer;
        }

        private static boolean canShip(int[] weights, int days, int capacity) {

            int requiredDays = 1;
            int currentLoad = 0;

            for (int w : weights) {
                if (currentLoad + w > capacity) {
                    requiredDays++;
                    currentLoad = 0;
                }
                currentLoad += w;
            }
            return requiredDays <= days;
        }
    }

    /*
     * 🧪 EDGE CASE & TRAP
     *
     * Trap:
     * Setting left = 0 (capacity cannot be 0).
     *
     * Interviewer checks:
     * Do you derive lower bound correctly?
     */

    /*
     * 🟣 INTERVIEW ARTICULATION
     *
     * “This is identical to Koko: binary search the minimum capacity
     * such that days required ≤ D.”
     */

    // ===============================================================
    // Reinforcement Problem 2
    // ===============================================================

    /*
     * ---------------------------------------------------------------
     * 📘 Split Array Largest Sum
     * https://leetcode.com/problems/split-array-largest-sum/
     *
     * Difficulty: Hard
     * Tags: Array, Binary Search, DP
     *
     * ---------------------------------------------------------------
     * Description:
     *
     * Given an integer array nums and an integer m,
     * split nums into m non-empty continuous subarrays.
     *
     * Minimize the largest sum among these subarrays.
     *
     * ---------------------------------------------------------------
     * Example:
     * Input: nums = [7,2,5,10,8], m = 2
     * Output: 18
     *
     * ---------------------------------------------------------------
     * Constraints:
     * 1 <= nums.length <= 1000
     * 0 <= nums[i] <= 10^6
     * 1 <= m <= min(50, nums.length)
     */

    /*
     * ⚫ PATTERN MAPPING
     *
     * • Answer space = maximum allowed subarray sum
     * • Predicate = can we split into ≤ m parts?
     * • MaxSum ↑ ⇒ fewer splits required
     */

    static class SplitArray {

        static int splitArray(int[] nums, int m) {

            int left = 0;
            int right = 0;

            for (int n : nums) {
                left = Math.max(left, n);
                right += n;
            }

            int answer = -1;

            while (left <= right) {

                int maxAllowedSum = left + (right - left) / 2;

                if (canSplit(nums, m, maxAllowedSum)) {
                    answer = maxAllowedSum;
                    right = maxAllowedSum - 1;
                } else {
                    left = maxAllowedSum + 1;
                }
            }
            return answer;
        }

        private static boolean canSplit(int[] nums, int m, int maxAllowedSum) {

            int pieces = 1;
            int currentSum = 0;

            for (int n : nums) {
                if (currentSum + n > maxAllowedSum) {
                    pieces++;
                    currentSum = 0;
                }
                currentSum += n;
            }
            return pieces <= m;
        }
    }

    /*
     * 🧪 EDGE CASE
     *
     * nums = [1,1,1,1], m = 4
     * Answer must be 1 (not 0).
     *
     * Interview trap:
     * Confusing exact m vs ≤ m splits.
     */

    // ===============================================================
    // Reinforcement Problem 3
    // ===============================================================

    /*
     * ---------------------------------------------------------------
     * 📘 Minimum Number of Days to Make m Bouquets
     * https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/
     *
     * Difficulty: Medium
     * Tags: Array, Binary Search
     *
     * ---------------------------------------------------------------
     * Description:
     *
     * You are given an integer array bloomDay, an integer m
     * and an integer k.
     *
     * You want to make m bouquets.
     * To make a bouquet, you need k adjacent flowers that
     * have bloomed.
     *
     * Return the minimum number of days needed to make m bouquets.
     * If it is impossible, return -1.
     *
     * ---------------------------------------------------------------
     * Example:
     * Input: bloomDay = [1,10,3,10,2], m = 3, k = 1
     * Output: 3
     *
     * ---------------------------------------------------------------
     * Constraints:
     * 1 <= bloomDay.length <= 10^5
     * 1 <= bloomDay[i] <= 10^9
     * 1 <= m <= 10^6
     * 1 <= k <= 10^6
     */

    /*
     * ⚫ PATTERN MAPPING
     *
     * • Answer space = days
     * • Predicate = can we make m bouquets by day D?
     * • Days ↑ ⇒ more flowers bloom
     */

    // ---------------------------------------------------------------
    // 🧠 INTUITIVE WALKTHROUGH — WHY ADJACENCY MATTERS (CRITICAL)
    // ---------------------------------------------------------------

    /*
     * Example:
     * bloomDay = [1, 10, 3, 10, 2]
     * day = 3
     *
     * Question:
     * Which flowers are bloomed by day 3?
     *
     * Rule:
     * • Day <= 3 → bloomed (✓)
     * • Day > 3  → not bloomed (✗)
     *
     * Resulting state:
     * [✓, ✗, ✓, ✗, ✓]
     *
     * -----------------------------------------------------------
     * Now look at ADJACENCY (this is the core difficulty):
     *
     * Traverse left to right:
     *
     * First ✓  → flowers = 1
     * ✗        → reset flowers = 0
     * ✓        → flowers = 1
     * ✗        → reset flowers = 0
     * ✓        → flowers = 1
     *
     * If k = 1:
     * • each ✓ forms a bouquet
     * • total bouquets = 3
     *
     * If k = 2:
     * • no two ✓ are adjacent
     * • impossible to form even one bouquet
     *
     * This is WHY adjacency logic exists.
     *
     * -----------------------------------------------------------
     * 🔴 Necessary feasibility condition:
     *
     * You need m × k flowers total.
     *
     * If bloomDay.length < m × k:
     * • impossible regardless of days
     * • must return -1 immediately
     *
     * -----------------------------------------------------------
     * 🟢 Why binary search boundaries make sense:
     *
     * • Before the minimum bloom day → no flowers bloomed
     * • After the maximum bloom day  → all flowers bloomed
     *
     * The answer MUST lie in this range.
     */

    // ---------------------------------------------------------------
    // 🧪 FULL BINARY SEARCH WALKTHROUGH (CONCRETE EXAMPLE)
    // ---------------------------------------------------------------

    /*
     * bloomDay = [1, 10, 3, 10, 2]
     * m = 3
     * k = 1
     *
     * Goal:
     * Find the MINIMUM day such that we can form 3 bouquets.
     *
     * -----------------------------------------------------------
     * Binary search tries:
     *
     * Day = 2
     * Blooms: [✓, ✗, ✗, ✗, ✓]
     * Bouquets formed = 2  → ❌ insufficient
     *
     * Day = 3
     * Blooms: [✓, ✗, ✓, ✗, ✓]
     * Bouquets formed = 3  → ✅ sufficient
     *
     * Since feasibility flips from ❌ to ✅ at day 3,
     * the answer is:
     *
     * ✅ 3
     *
     * -----------------------------------------------------------
     * Note:
     * Once a day works, all later days will also work.
     * This is the monotonicity that enables binary search.
     */


    static class Bouquets {

        static int minDays(int[] bloomDay, int m, int k) {

            if ((long) m * k > bloomDay.length) return -1;

            int left = Integer.MAX_VALUE;
            int right = Integer.MIN_VALUE;

            for (int d : bloomDay) {
                left = Math.min(left, d);
                right = Math.max(right, d);
            }

            int answer = -1;

            while (left <= right) {

                int midDay = left + (right - left) / 2;

                if (canMake(bloomDay, m, k, midDay)) {
                    answer = midDay;
                    right = midDay - 1;
                } else {
                    left = midDay + 1;
                }
            }
            return answer;
        }

        private static boolean canMake(int[] bloomDay, int m, int k, int day) {

            int bouquets = 0;
            int flowers = 0;

            for (int d : bloomDay) {
                if (d <= day) {
                    flowers++;
                    if (flowers == k) {
                        bouquets++;
                        flowers = 0;
                    }
                } else {
                    flowers = 0;
                }
            }
            return bouquets >= m;
        }
    }

    // -----------------------------------------------------------
    // 🧠 CORE CHECK — WHAT DOES (d <= day) ACTUALLY MEAN?
    // -----------------------------------------------------------

    /*
     * Understanding the comparison:
     *
     * for (int d : bloomDay)
     *
     * • d   = the day on which THIS specific flower blooms
     * • day = the hypothetical day we are currently testing
     *
     * Binary search is repeatedly asking:
     * “If today were day = D, could we make m bouquets by now?”
     *
     * -----------------------------------------------------------
     * The check:
     *
     * if (d <= day)
     *
     * literally means:
     *
     * “Has this flower bloomed by day = D?”
     *
     * -----------------------------------------------------------
     * Two possible cases:
     *
     * ✅ d <= day
     *    → flower has already bloomed
     *    → usable for bouquet
     *
     * ❌ d > day
     *    → flower has NOT bloomed yet
     *    → unusable for bouquet
     *
     * -----------------------------------------------------------
     * This comparison converts the original problem into
     * a binary usable / unusable view:
     *
     * usable flower    → ✓
     * unusable flower  → ✗
     *
     * Example:
     * bloomDay = [1, 10, 3, 10, 2]
     * day = 3
     *
     * Result:
     * [✓, ✗, ✓, ✗, ✓]
     *
     * -----------------------------------------------------------
     * Why this is critical:
     *
     * As day increases:
     * • more flowers satisfy (d <= day)
     * • ✓ never turns back into ✗
     *
     * This guarantees MONOTONICITY and
     * makes binary search valid.
     */


    /*
     * 🧪 EDGE CASE
     *
     * Impossible case: m * k > n
     *
     * Interviewer wants to see:
     * early rejection before binary search.
     */


    // ===============================================================
    // 11️⃣ 🟢 LEARNING VERIFICATION
    // ===============================================================

    /*
     * 🟢 How to Confirm Mastery (Without Code):
     *
     * You should be able to answer, verbally:
     *
     * 1️⃣ What is the answer space?
     *     → Eating speed (1 … maxPile)
     *
     * 2️⃣ What is the predicate?
     *     → Can Koko finish within h hours?
     *
     * 3️⃣ Why is it monotonic?
     *     → Higher speed never increases hours.
     *
     * 4️⃣ What is the FIRST valid value?
     *     → Minimum speed where predicate becomes true.
     *
     * ---------------------------------------------------------------
     * 🟢 Invariants to Recall Instantly:
     *
     * • Search space contains ONLY valid candidates
     * • left always points to smallest possible candidate
     * • right always points to largest possible candidate
     * • If mid works → search left
     * • If mid fails → search right
     *
     * ---------------------------------------------------------------
     * 🟢 Bugs You Should Be Able to Debug Intentionally:
     *
     * ❌ left = 0  → division by zero / invalid invariant
     * ❌ wrong feasibility logic → broken monotonicity
     * ❌ using exact m instead of ≤ m (split problems)
     *
     * ---------------------------------------------------------------
     * 🟢 Pattern Detection in Unseen Problems:
     *
     * Look for:
     * • “minimum / maximum such that…”
     * • huge numeric bounds
     * • feasibility check
     *
     * If all three exist → Binary Search on Answer Space.
     */

    // ===============================================================
    // 12️⃣ 🧪 MAIN METHOD + SELF-VERIFYING TESTS
    // ===============================================================

    public static void main(String[] args) {

        // -----------------------------------------------------------
        // PRIMARY PROBLEM TESTS — Koko Eating Bananas
        // -----------------------------------------------------------

        // Happy path
        assertEquals(
                4,
                Optimal.minEatingSpeed(new int[]{3, 6, 7, 11}, 8),
                "Koko basic example"
        );

        // Boundary: h very large → speed = 1
        assertEquals(
                1,
                Optimal.minEatingSpeed(new int[]{1, 1, 1}, 100),
                "Koko minimum speed"
        );

        // Interview trap: single pile, one hour
        assertEquals(
                11,
                Optimal.minEatingSpeed(new int[]{11}, 1),
                "Koko single pile"
        );

        // -----------------------------------------------------------
        // REINFORCEMENT PROBLEM TESTS
        // -----------------------------------------------------------

        // Capacity to Ship Packages
        assertEquals(
                15,
                ShipPackages.shipWithinDays(
                        new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5),
                "Ship packages example"
        );

        // Split Array Largest Sum
        assertEquals(
                18,
                SplitArray.splitArray(new int[]{7, 2, 5, 10, 8}, 2),
                "Split array example"
        );

        // Minimum Days to Make Bouquets
        assertEquals(
                3,
                Bouquets.minDays(new int[]{1, 10, 3, 10, 2}, 3, 1),
                "Bouquets example"
        );

        System.out.println("✅ ALL TESTS PASSED — FILE IS CORRECT");
    }

    // ===============================================================
    // 🧪 SIMPLE ASSERTION HELPERS
    // ===============================================================

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(
                    message + " | expected=" + expected + ", actual=" + actual
            );
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Assertion failed: " + message);
        }
    }

    // ===============================================================
    // 🔧 SHARED FEASIBILITY HELPERS
    // ===============================================================

    private static boolean canFinish(int[] piles, int h, int speed) {
        long hours = 0;
        for (int pile : piles) {
            hours += (long) Math.ceil((double) pile / speed);
        }
        return hours <= h;
    }
}

