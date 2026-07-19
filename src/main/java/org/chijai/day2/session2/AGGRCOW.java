package org.chijai.day2.session2;

/**
 * ================================================================
 * 🐄 AGGRCOW – Aggressive Cows
 * ================================================================
 *
 * This file is a COMPLETE algorithm chapter.
 * It is designed for:
 *  • Pattern mastery
 *  • Interview readiness
 *  • Long-term recall
 *  • Teaching others
 *  • Forensic debugging
 *  • Correctness confidence
 *
 * ================================================================
 * 1️⃣ TOP-LEVEL PUBLIC CLASS DECLARATION
 * ================================================================
 */
public class AGGRCOW {

    /*
     * ================================================================
     * 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL STATEMENT
     * ================================================================
     *
     * AGGRCOW - Aggressive cows
     *
     * Farmer John has built a new long barn, with N (2 ≤ N ≤ 100,000)
     * stalls. The stalls are located along a straight line at positions
     * x1 ... xN (0 ≤ xi ≤ 1,000,000,000).
     *
     * His C (2 ≤ C ≤ N) cows don't like this barn layout and become
     * aggressive towards each other once put into a stall.
     *
     * To prevent the cows from hurting each other, FJ wants to assign
     * the cows to the stalls, such that the minimum distance between
     * any two of them is as large as possible.
     *
     * What is the largest minimum distance?
     *
     * Input:
     * t – the number of test cases, then t test cases follows.
     *
     * For each test case:
     * Line 1: Two space-separated integers: N and C
     * Lines 2..N+1: Line i+1 contains an integer stall location, xi
     *
     * Output:
     * For each test case output one integer: the largest minimum distance.
     *
     * Example:
     * Input:
     * 1
     * 5 3
     * 1
     * 2
     * 8
     * 4
     * 9
     *
     * Output:
     * 3
     *
     * Explanation:
     * FJ can put his 3 cows in the stalls at positions 1, 4 and 8,
     * resulting in a minimum distance of 3.
     *
     * 🔗 https://www.spoj.com/problems/AGGRCOW/
     * 🧩 Difficulty: Medium–Hard
     * 🏷️ Tags: Binary Search, Greedy, Optimization
     */

    /*
     * ================================================================
     * 3️⃣ 🔵 CORE PATTERN OVERVIEW
     * ================================================================
     *
     * 🔵 Pattern Name:
     * Binary Search on Answer (Monotonic Feasibility)
     *
     * 🔵 Core Idea:
     * Instead of directly constructing the placement, binary search
     * the ANSWER (minimum distance) and check feasibility.
     *
     * 🔵 Why It Works:
     * If we can place all cows with minimum distance = D,
     * then we can place them for any distance < D.
     * This monotonic property enables binary search.
     *
     * 🔵 When To Use:
     * • Objective: maximize / minimize a value
     * • Feasibility can be checked greedily
     * • Search space is large (up to 1e9)
     *
     * 🔵 Pattern Recognition Signals:
     * • "Largest minimum"
     * • "Smallest maximum"
     * • Positions on a line
     * • Greedy placement possible
     *
     * 🔵 Difference from Similar Patterns:
     * • NOT sliding window (window size not fixed)
     * • NOT DP (no overlapping subproblems)
     * • Binary search on value, not index
     */

    /*
     * ================================================================
     * 4️⃣ 🟢 MENTAL MODEL & INVARIANTS
     * ================================================================
     *
     * 🟢 Mental Model:
     * "Guess a minimum distance and VERIFY if it is achievable."
     *
     * 🟢 Core Invariant:
     * If distance D is feasible, all distances < D are feasible.
     * If distance D is NOT feasible, all distances > D are NOT feasible.
     *
     * 🟢 Variables & Roles:
     * • lowDistance  → smallest candidate answer
     * • highDistance → largest candidate answer
     * • midDistance  → current guess
     * • lastPlacedPosition → position of last cow placed
     * • cowsPlaced → number of cows placed so far
     *
     * 🟢 Termination Logic:
     * Binary search converges on the LAST feasible distance.
     *
     * 🟢 Forbidden Actions:
     * ❌ Binary search on indices
     * ❌ Random placement
     * ❌ Pairwise distance computation (O(N^2))
     *
     * 🟢 Why Alternatives Are Inferior:
     * • Brute force distances → too slow
     * • Greedy without binary search → misses optimal
     */

    /*
     * ================================================================
     * 5️⃣ 🔴 WHY NAIVE / WRONG SOLUTIONS FAIL
     * ================================================================
     *
     * 🔴 Wrong Approach 1: Try all subsets of stalls
     * • Seems correct conceptually
     * • Completely infeasible (2^N)
     *
     * 🔴 Wrong Approach 2: Check all pairwise distances
     * • Misses global placement constraint
     * • Violates greedy feasibility invariant
     *
     * 🔴 Wrong Approach 3: Binary search but wrong bounds
     * • low = 0, high = 1e9 blindly
     * • Causes infinite loops or wrong answer
     *
     * 🔴 Interviewer Trap:
     * • Forgetting to sort stalls
     * • Incorrect greedy check (placing too early)
     * • Returning mid instead of last feasible
     */

    /*
     * ================================================================
     * 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES
     * ================================================================
     */

    /** ------------------------------------------------------------
     * 🟤 BRUTE FORCE SOLUTION
     * -------------------------------------------------------------
     * Core Idea:
     * Try all possible minimum distances from 1 to max range.
     *
     * Time: O(N * RANGE)
     * Space: O(1)
     * Interview Preference: ❌ Never
     */
    static class BruteForce {
        static int solve(int[] stalls, int cows) {
            java.util.Arrays.sort(stalls);
            int maxDistance = stalls[stalls.length - 1] - stalls[0];
            int answer = 0;

            for (int distance = 1; distance <= maxDistance; distance++) {
                if (canPlace(stalls, cows, distance)) {
                    answer = distance;
                }
            }
            return answer;
        }
    }

    /** ------------------------------------------------------------
     * 🟡 IMPROVED SOLUTION
     * -------------------------------------------------------------
     * Core Idea:
     * Same feasibility check, but prune using binary search.
     *
     * Time: O(N log RANGE)
     * Space: O(1)
     * Interview Preference: ⚠️ Acceptable
     */
    static class Improved {
        static int solve(int[] stalls, int cows) {
            java.util.Arrays.sort(stalls);

            int low = 1;
            int high = stalls[stalls.length - 1] - stalls[0];
            int best = 0;

            while (low <= high) {
                int mid = low + (high - low) / 2;

                if (canPlace(stalls, cows, mid)) {
                    best = mid;          // 🟢 feasible, try bigger
                    low = mid + 1;
                } else {
                    high = mid - 1;     // 🔴 infeasible, shrink
                }
            }
            return best;
        }
    }

    /** ------------------------------------------------------------
     * 🟢 OPTIMAL (INTERVIEW-PREFERRED)
     * -------------------------------------------------------------
     */
    static boolean canPlace(int[] stalls, int cows, int minDistance) {
        int cowsPlaced = 1; // first cow always placed
        int lastPlacedPosition = stalls[0];

        for (int i = 1; i < stalls.length; i++) {
            if (stalls[i] - lastPlacedPosition >= minDistance) {
                cowsPlaced++;
                lastPlacedPosition = stalls[i];

                if (cowsPlaced == cows) {
                    return true; // 🟢 invariant satisfied
                }
            }
        }
        return false;
    }
    static class Optimal {
        static int solve(int[] stalls, int cows) {
            java.util.Arrays.sort(stalls);

            int low = 1;
            int high = stalls[stalls.length - 1] - stalls[0];
            int best = 0;

            while (low <= high) {
                int mid = low + (high - low) / 2;

                if (canPlace(stalls, cows, mid)) {
                    best = mid;          // 🟢 feasible, try bigger
                    low = mid + 1;
                } else {
                    high = mid - 1;     // 🔴 infeasible, shrink
                }
            }
            return best;
        }
    }
    /**
     * ---------------------------------------------------------------------------
     * Key Realization
     * ---------------------------------------------------------------------------
     *
     * We are NOT trying to directly construct the best arrangement of cows.
     *
     * Instead, we repeatedly ask:
     *
     *     "If I require every pair of cows to be at least D units apart,
     *      is it possible to place all the cows?"
     *
     * This is a simple Yes/No (feasibility) question that can be answered
     * greedily by placing each cow in the earliest valid stall.
     *
     * The answers are monotonic:
     *
     *      Minimum Distance
     *
     *      1   2   3   4   5   6   7   8
     *      ✅  ✅  ✅  ❌  ❌  ❌  ❌  ❌
     *
     * Once a distance becomes impossible, every larger distance is also
     * impossible.
     *
     * Therefore, we binary search over the answer space to find the
     * largest feasible minimum distance.
     * ---------------------------------------------------------------------------
     */

    /*
     * ================================================================
     * 7️⃣ 🟣 INTERVIEW ARTICULATION
     * ================================================================
     *
     * • We binary search the minimum distance.
     * • Feasibility is monotonic.
     * • Greedy placement is optimal for checking.
     * • Changing greedy order breaks correctness.
     * • Streaming possible after sorting.
     * • Not usable if positions are dynamic.
     */

    /*
     * ================================================================
     * 8️⃣ 🔄 VARIATIONS & TWEAKS
     * ================================================================
     *
     * 🟢 Invariant-Preserving:
     * • Different distance metric
     * • Floating point with epsilon
     *
     * 🔴 Pattern Break:
     * • 2D placement
     * • Non-monotonic feasibility
     */

    /*
     * ================================================================
     * 9️⃣ ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
     * ================================================================
     */

    /*
     * ================================================================
     * ⚫ REINFORCEMENT SUB-CHAPTER 1
     * ================================================================
     * 📘 LeetCode 410 — Split Array Largest Sum
     *
     * FULL OFFICIAL PROBLEM STATEMENT
     *
     * Given an integer array nums and an integer m, split nums into m
     * non-empty continuous subarrays.
     *
     * The largest sum among these subarrays should be minimized.
     *
     * Return the minimized largest sum.
     *
     * Constraints:
     * 1 <= nums.length <= 1000
     * 0 <= nums[i] <= 10^6
     * 1 <= m <= nums.length
     *
     * Example:
     * Input: nums = [7,2,5,10,8], m = 2
     * Output: 18
     * Explanation:
     * Split into [7,2,5] and [10,8].
     * Largest sum = max(14,18) = 18.
     *
     * 🔗 https://leetcode.com/problems/split-array-largest-sum/
     * 🧩 Difficulty: Hard
     * 🏷️ Tags: Binary Search, Greedy, DP
     *
     * ------------------------------------------------
     * 🧠 PATTERN MAPPING
     * ------------------------------------------------
     * ⚫ Same Binary Search on Answer pattern.
     *
     * 🟢 Invariant:
     * If we can split the array with max subarray sum ≤ X,
     * then we can split it for any X' > X.
     */

    static class SplitArrayLargestSum {

        static int splitArray(int[] nums, int m) {
            int low = 0;
            int high = 0;

            // 🟢 Lower bound must be max element (cannot split a number)
            // 🟢 Upper bound is sum of all elements (single subarray)
            for (int num : nums) {
                low = Math.max(low, num);
                high += num;
            }

            int bestAnswer = high;

            while (low <= high) {
                int candidateMaxSum = low + (high - low) / 2;

                if (canSplit(nums, m, candidateMaxSum)) {
                    bestAnswer = candidateMaxSum;   // 🟢 feasible
                    high = candidateMaxSum - 1;     // try smaller
                } else {
                    low = candidateMaxSum + 1;      // 🔴 infeasible
                }
            }
            return bestAnswer;
        }

        static boolean canSplit(int[] nums, int m, int maxAllowedSum) {
            int subarraysUsed = 1;
            int currentSum = 0;

            for (int num : nums) {
                if (currentSum + num > maxAllowedSum) {
                    subarraysUsed++;
                    currentSum = num;

                    // 🔴 violated invariant: too many subarrays
                    if (subarraysUsed > m) return false;
                } else {
                    currentSum += num;
                }
            }
            return true;
        }
    }

    /*
     * ------------------------------------------------
     * 🧪 EDGE CASES & INTERVIEW TRAPS
     * ------------------------------------------------
     * 🔴 Trap: low = 0 breaks invariant
     * 🔴 Trap: DP-first approach (worse signal)
     * 🟢 Edge: m == nums.length → answer = max(nums)
     *
     * ------------------------------------------------
     * 🟣 INTERVIEW ARTICULATION
     * ------------------------------------------------
     * Binary search the maximum allowed subarray sum.
     * Greedily split when sum exceeds the candidate.
     * Monotonic feasibility guarantees correctness.
     */


    /*
     * ================================================================
     * ⚫ REINFORCEMENT SUB-CHAPTER 2
     * ================================================================
     * 📘 LeetCode 875 — Koko Eating Bananas
     *
     * FULL OFFICIAL PROBLEM STATEMENT
     *
     * Koko loves to eat bananas. There are n piles of bananas.
     * Guards will return in h hours.
     *
     * Koko eats at speed k bananas per hour.
     * Each hour she chooses exactly one pile.
     *
     * Return the minimum integer k such that she can eat all
     * bananas within h hours.
     *
     * Constraints:
     * 1 <= piles.length <= 10^4
     * piles[i] <= 10^9
     * 1 <= h <= 10^9
     *
     * Example:
     * Input: piles = [3,6,7,11], h = 8
     * Output: 4
     *
     * 🔗 https://leetcode.com/problems/koko-eating-bananas/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Binary Search
     *
     * ------------------------------------------------
     * 🧠 PATTERN MAPPING
     * ------------------------------------------------
     * ⚫ Same Binary Search on Answer.
     *
     * 🟢 Invariant:
     * If speed k works, any k' > k also works.
     */

    static class KokoEatingBananas {

        static int minEatingSpeed(int[] piles, int h) {
            int lowSpeed = 1;
            int highSpeed = 0;

            // 🟢 Max pile is absolute upper bound
            for (int pile : piles) {
                highSpeed = Math.max(highSpeed, pile);
            }

            int minimumFeasibleSpeed = highSpeed;

            while (lowSpeed <= highSpeed) {
                int candidateSpeed = lowSpeed + (highSpeed - lowSpeed) / 2;

                if (canFinish(piles, h, candidateSpeed)) {
                    minimumFeasibleSpeed = candidateSpeed; // 🟢 feasible
                    highSpeed = candidateSpeed - 1;
                } else {
                    lowSpeed = candidateSpeed + 1;         // 🔴 infeasible
                }
            }
            return minimumFeasibleSpeed;
        }

        static boolean canFinish(int[] piles, int h, int speed) {
            long totalHours = 0;

            for (int pile : piles) {
                // 🟢 Ceiling division without floating point
                totalHours += (pile + speed - 1) / speed;

                if (totalHours > h) return false;
            }
            return true;
        }
    }

    /*
     * ------------------------------------------------
     * 🧪 EDGE CASES & INTERVIEW TRAPS
     * ------------------------------------------------
     * 🔴 Trap: using Math.ceil(double)
     * 🔴 Trap: binary search on hours instead of speed
     * 🟢 Edge: h == piles.length → speed = max pile
     *
     * ------------------------------------------------
     * 🟣 INTERVIEW ARTICULATION
     * ------------------------------------------------
     * Binary search the eating speed.
     * Compute hours greedily using ceiling division.
     * Predicate is monotonic.
     */


    /*
     * ================================================================
     * ⚫ REINFORCEMENT SUB-CHAPTER 3
     * ================================================================
     * 📘 Painter’s Partition Problem
     *
     * FULL OFFICIAL PROBLEM STATEMENT
     *
     * Given n boards of different lengths and k painters.
     * Each painter paints contiguous boards.
     * Each unit length takes 1 unit of time.
     *
     * Find the minimum time required to paint all boards.
     *
     * ------------------------------------------------
     * 🧠 PATTERN MAPPING
     * ------------------------------------------------
     * ⚫ Identical to Split Array Largest Sum.
     *
     * 🟢 Invariant:
     * If painters can finish within time T,
     * they can finish in any time T' > T.
     */

    /*
     * ================================================================
     * 🧠 PAINTER’S PARTITION — DEEP INTUITION + VISUAL MENTAL MODEL
     * ================================================================
     *
     * This block exists so that the solution can be RE-INVENTED
     * from first principles during interviews or future revision.
     *
     * ------------------------------------------------
     * 🔵 PROBLEM REFRAMING (THE INVENTION STEP)
     * ------------------------------------------------
     *
     * Original ask:
     *   “Find the minimum time required to paint all boards.”
     *
     * Correct mental reframing:
     *   “If each painter is allowed to work for at most T time,
     *    can all boards be painted using ≤ k painters?”
     *
     * This converts:
     *   Optimization → YES / NO feasibility
     *   Unknown answer → decision problem
     *
     * This single reframing CREATES the solution.
     *
     * ------------------------------------------------
     * 🟢 MONOTONICITY INVARIANT (WHY BINARY SEARCH IS FORCED)
     * ------------------------------------------------
     *
     * If painters can finish within time T,
     * then they can also finish within any time T' > T.
     *
     * If painters cannot finish within time T,
     * then they cannot finish within any time T' < T.
     *
     * Feasibility shape over T:
     *
     *   ❌ ❌ ❌ ❌ | ✅ ✅ ✅
     *             ↑
     *        single boundary
     *
     * This one-directional flip is the ONLY requirement
     * for Binary Search on Answer.
     *
     * ------------------------------------------------
     * 🟡 VISUAL MODEL — “BUCKET FILLING” (GREEDY FEASIBILITY)
     * ------------------------------------------------
     *
     * Imagine:
     *   • Each painter is a bucket
     *   • Bucket capacity = T (guessed max time)
     *   • Boards are poured left → right (must stay contiguous)
     *
     * Greedy rule:
     *   Fill the current bucket until the next board would overflow.
     *   On overflow → start a new bucket (new painter).
     *
     * Example:
     *   Boards = [5, 10, 30, 20, 15], Painters = 3
     *
     *   Guess T = 35
     *
     *   Painter 1: 5 + 10 = 15, +30 ❌ → [5 | 10]
     *   Painter 2: 30 = 30, +20 ❌     → [30]
     *   Painter 3: 20 + 15 = 35        → [20 | 15]
     *
     *   Painters used = 3 (≤ allowed)
     *   ⇒ T = 35 is FEASIBLE
     *
     * Guess T = 25:
     *   Board 30 alone exceeds T
     *   ⇒ Immediately INFEASIBLE
     *
     * This proves:
     *   T < max(board length) is impossible
     *
     * ------------------------------------------------
     * 🟢 WHY THIS GREEDY IS CORRECT
     * ------------------------------------------------
     *
     * Placing as much work as possible on the current painter
     * maximizes remaining capacity for future painters.
     *
     * Any solution that delays a split can be shifted left
     * without increasing the maximum workload.
     *
     * Therefore:
     *   “Split only when forced” is optimal.
     *
     * ------------------------------------------------
     * 🔴 COMMON MISTAKES (INTENTIONALLY CALLED OUT)
     * ------------------------------------------------
     *
     * ❌ Trying to balance painters evenly
     *    → Invalid because board order is fixed.
     *
     * ❌ Allowing painters to split boards
     *    → Violates indivisible board constraint.
     *
     * ❌ low = 0
     *    → Introduces impossible values, breaks invariant.
     *
     * ❌ Returning low blindly
     *    → Returns first feasible, not last verified answer.
     *
     * Correct bounds:
     *   low  = max(board length)
     *   high = sum(board lengths)
     *
     * ------------------------------------------------
     * ⚫ PATTERN FAMILY CONNECTION (LOCK-IN)
     * ------------------------------------------------
     *
     * Painter’s Partition has the SAME SHAPE as:
     *
     *   • Split Array Largest Sum
     *     (boards ↔ array, painters ↔ subarrays)
     *
     *   • Minimum Days to Make m Bouquets
     *     (time threshold ↔ day threshold)
     *
     *   • Aggressive Cows
     *     (time threshold ↔ distance threshold)
     *
     * Unified mental sentence:
     *   “Binary search the threshold; verify feasibility
     *    greedily in one left-to-right pass.”
     *
     * ------------------------------------------------
     * 🟣 INTERVIEW-READY ARTICULATION (MEMORIZE)
     * ------------------------------------------------
     *
     * “I’m minimizing the maximum workload.
     *  I binary search on the allowed time T.
     *  For a fixed T, I greedily assign contiguous boards
     *  and count how many painters are required.
     *  The feasibility predicate is monotonic,
     *  so binary search finds the minimum feasible T.”
     *
     * ------------------------------------------------
     * 🧠 MEMORY LOCK (ONE-LINE)
     * ------------------------------------------------
     *
     * Minimize the maximum
     * → Guess the maximum
     * → Greedily check feasibility
     * → Binary search the answer
     */

    /*
     * ------------------------------------------------
     * 🧠 CORE INTUITION — BOUNDS + GREEDY FEASIBILITY
     * ------------------------------------------------
     *
     * “No painter can paint a board faster than its length.”
     * → Boards are indivisible.
     *
     * Therefore:
     *   Lower bound (low) = length of the largest board
     *
     * “In the worst case, one painter does all the work.”
     * → Always feasible.
     *
     * Therefore:
     *   Upper bound (high) = sum of all board lengths
     *
     * In short:
     *   Lower bound = largest indivisible unit
     *   Upper bound = everything done by one worker
     *
     * ------------------------------------------------
     * 🟡 FEASIBILITY QUESTION (WHAT canPaint REALLY ASKS)
     * ------------------------------------------------
     *
     * “Given a maximum allowed time maxTime,
     *  how many painters would I need if I assign boards
     *  strictly left to right?”
     *
     * If painters needed ≤ painters allowed → FEASIBLE
     * Else → INFEASIBLE
     *
     * ------------------------------------------------
     * 🟢 GREEDY DECISION — READ THIS CONDITION AS ENGLISH
     * ------------------------------------------------
     *
     * if (currentTime + board > maxTime)
     *
     * Read as:
     * “If I give this board to the current painter,
     *  will they exceed the allowed time?”
     *
     * If YES → I am forced to start a new painter.
     * If NO  → Keep assigning to the current painter.
     *
     * ------------------------------------------------
     * 🖼️ STEP-BY-STEP DRY RUN (MENTAL SIMULATION)
     * ------------------------------------------------
     *
     * Boards = [5, 10, 30, 20, 15]
     * Painters = 3
     * maxTime = 35
     *
     * Painter 1:
     *   currentTime = 0
     *   +5  → 5
     *   +10 → 15
     *   +30 → 45 ❌ overflow → new painter
     *
     * Painter 2:
     *   currentTime = 30
     *   +20 → 50 ❌ overflow → new painter
     *
     * Painter 3:
     *   currentTime = 20
     *   +15 → 35 ✅ fits
     *
     * paintersUsed = 3 (≤ allowed)
     * → maxTime is FEASIBLE
     *
     * ------------------------------------------------
     * 🧠 KEY TAKEAWAY
     * ------------------------------------------------
     *
     * This loop does NOT find the optimal assignment.
     * It only checks feasibility for a guessed limit.
     *
     * Greedy rule:
     *   “Split only when forced.”
     *
     * This is what makes Binary Search on Answer correct.
     */


    static class PaintersPartition {

        static int minTime(int[] boards, int painters) {
            int low = 0;
            int high = 0;

            // 🟢 Bounds derived from constraints
            for (int board : boards) {
                low = Math.max(low, board);
                high += board;
            }

            int minimumTime = high;

            while (low <= high) {
                int candidateTime = low + (high - low) / 2;

                if (canPaint(boards, painters, candidateTime)) {
                    minimumTime = candidateTime;   // 🟢 feasible
                    high = candidateTime - 1;
                } else {
                    low = candidateTime + 1;       // 🔴 infeasible
                }
            }
            return minimumTime;
        }

        static boolean canPaint(int[] boards, int painters, int maxTime) {
            int paintersUsed = 1;
            int currentTime = 0;

            for (int board : boards) {
                if (currentTime + board > maxTime) {
                    paintersUsed++;
                    currentTime = board;

                    // 🔴 More painters than allowed
                    if (paintersUsed > painters) return false;
                } else {
                    currentTime += board;
                }
            }
            return true;
        }
    }

    /*
     * ------------------------------------------------
     * 🧪 EDGE CASES & INTERVIEW NOTES
     * ------------------------------------------------
     * 🔴 Trap: allowing painters to split boards
     * 🟢 Edge: painters >= boards.length → max board
     *
     * ------------------------------------------------
     * 🟣 INTERVIEW ARTICULATION
     * ------------------------------------------------
     * Binary search the maximum allowed time.
     * Greedily assign contiguous boards.
     * Monotonic feasibility guarantees correctness.
     */

    /*
     * ================================================================
     * 11️⃣ 🟢 LEARNING VERIFICATION
     * ================================================================
     *
     * • Can you explain monotonicity without code?
     * • Can you flip maximize ↔ minimize?
     * • Can you detect greedy feasibility?
     */

    /*
     * ================================================================
     * 12️⃣ 🧪 MAIN METHOD + SELF-VERIFYING TESTS
     * ================================================================
     */
    public static void main(String[] args) {
        testExample();
        testEdgeCase();
        testLargeGap();
        System.out.println("✅ All tests passed.");
    }

    static void assertEquals(int expected, int actual, String reason) {
        if (expected != actual) {
            throw new AssertionError(
                    "Expected " + expected + " but got " + actual +
                            " | Reason: " + reason
            );
        }
    }

    static void testExample() {
        int[] stalls = {1, 2, 8, 4, 9};
        int cows = 3;
        assertEquals(3, Optimal.solve(stalls, cows),
                "Classic sample case");
    }

    static void testEdgeCase() {
        int[] stalls = {1, 2};
        int cows = 2;
        assertEquals(1, Optimal.solve(stalls, cows),
                "Minimum possible input");
    }

    static void testLargeGap() {
        int[] stalls = {0, 1000000000};
        int cows = 2;
        assertEquals(1000000000, Optimal.solve(stalls, cows),
                "Max coordinate gap");
    }
}
