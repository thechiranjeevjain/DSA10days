package org.chijai.day3.session2;

/**
 * =================================================================================================
 *  📘 MINIMUM PLATFORMS REQUIRED — COMPLETE ALGORITHM CHAPTER
 * =================================================================================================
 *
 *  This file is a FULLY SELF-CONTAINED, IntelliJ-ready Java chapter.
 *  It is NOT just a solution — it is a reusable algorithm textbook.
 *
 *  Pattern: Interval Overlap Counting (Two-Pointer Sweep Line)
 *
 * =================================================================================================
 */

/**
 * ================================================================================================
 * 1️⃣ TOP-LEVEL PUBLIC CLASS (MANDATORY)
 * ================================================================================================
 */
public class MinimumPlatforms {

    /*
     * ============================================================================================
     * 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL STATEMENT (AS COMMENTS)
     * ============================================================================================
     *
     * Minimum Platforms Required
     * Last Updated : 8 Sep, 2025
     *
     * Given two arrays arr[] and dep[], that represent the arrival and departure time of i-th train
     * respectively. Find the minimum number of platforms required so that no train has to wait.
     *
     * If the departure time of one train is the same as the arrival time of another train,
     * both trains cannot use the same platform at that time.
     *
     * Note:
     * Time intervals are in the 24-hour format (HHMM), where:
     * - The first two characters represent hour (00 to 23)
     * - The last two characters represent minutes (00 to 59)
     * - Leading zeros for hours less than 10 are optional (e.g., 0900 == 900)
     *
     * Examples:
     *
     * Input:
     * arr[] = [1000, 935, 1100]
     * dep[] = [1200, 1240, 1130]
     * Output: 3
     * Explanation:
     * All three trains overlap in time, so 3 platforms are required.
     *
     * Input:
     * arr[] = [900, 1235, 1100]
     * dep[] = [1000, 1240, 1200]
     * Output: 1
     * Explanation:
     * Each train departs before the next arrives.
     *
     * 🔗 Link: https://practice.geeksforgeeks.org/problems/minimum-platforms-1587115620/1
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Arrays, Sorting, Two Pointers, Greedy
     */

    /*
     * ============================================================================================
     * 3️⃣ 🔵 CORE PATTERN OVERVIEW
     * ============================================================================================
     *
     * Pattern Name:
     * Interval Overlap Counting (Sweep Line using Two Pointers)
     *
     * Core Idea:
     * Convert interval overlap into an event-counting problem.
     *
     * Why It Works:
     * At any time, number of platforms required equals the number of trains currently present.
     *
     * When to Use:
     * - Minimum rooms / platforms
     * - Maximum overlap of intervals
     * - Scheduling conflicts
     *
     * 🧭 Pattern Recognition Signals:
     * - "Minimum number of resources"
     * - "Overlapping intervals"
     * - Arrival & departure / start & end times
     *
     * Difference from Similar Patterns:
     * - NOT interval merging
     * - NOT binary search
     * - Focuses on simultaneous presence, not grouping
     */

    /*
     * ============================================================================================
     * 4️⃣ 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================================================
     *
     * Mental Model:
     * Imagine a station timeline.
     * Arrivals increase platform demand.
     * Departures decrease platform demand.
     * Track the maximum demand at any moment.
     *
     * Invariants:
     * - currentPlatforms = arrivals processed − departures processed
     * - maxPlatforms = max(currentPlatforms seen so far)
     *
     * Variable Roles:
     * - arrivalIndex → next arrival event
     * - departureIndex → next departure event
     * - currentPlatforms → live overlap count
     * - maxPlatforms → answer
     *
     * Termination Logic:
     * Loop ends when all arrivals are processed.
     *
     * Forbidden Actions:
     * - Pairing arrival with its own departure
     * - Using greedy matching instead of counting
     *
     * Why Common Alternatives Are Inferior:
     * - Naive comparison is O(n²)
     * - Event sorting without pointers obscures invariants
     */

    /*
     * ============================================================================================
     * 5️⃣ 🔴 WHY NAIVE / WRONG SOLUTIONS FAIL
     * ============================================================================================
     *
     * Typical Wrong Approach:
     * - For each train, count overlaps with others
     *
     * Why It Seems Correct:
     * - Directly checks conflicts
     *
     * Invariant Violated:
     * - Overlaps are global, not per-train
     *
     * Counterexample:
     * - Three trains overlapping partially → max overlap missed
     *
     * Interviewer Trap:
     * - Equal arrival & departure times MUST count as conflict
     */

    /*
     * ============================================================================================
     * 🔵 REAL-WORLD TIMELINE SIMULATIONS (IDENTITY-FREE PROOF)
     * ============================================================================================
     *
     * These examples exist ONLY to permanently kill the doubt:
     * “How can we solve this without pairing arrival & departure per train?”
     *
     * --------------------------------------------------------------------------------------------
     * EXAMPLE 1 — MAX PLATFORMS = 3 (OVERLAP)
     * --------------------------------------------------------------------------------------------
     *
     * Arrivals:   9:00, 9:10, 9:15
     * Departures: 9:30, 9:20, 9:25
     *
     * Timeline (what the station manager experiences):
     *
     * 9:00  → arrival → platforms = 1
     * 9:10  → arrival → platforms = 2
     * 9:15  → arrival → platforms = 3
     * 9:20  → departure → platforms = 2
     * 9:25  → departure → platforms = 1
     * 9:30  → departure → platforms = 0
     *
     * Answer = 3
     *
     * Key insight:
     * - No departure happened before the next arrival.
     * - Three trains existed simultaneously.
     *
     * --------------------------------------------------------------------------------------------
     * EXAMPLE 2 — MAX PLATFORMS = 1 (NO OVERLAP, CLEAN)
     * --------------------------------------------------------------------------------------------
     *
     * Arrivals:   9:00, 9:20, 9:40
     * Departures: 9:10, 9:30, 9:50
     *
     * Timeline:
     *
     * 9:00  → arrival → platforms = 1
     * 9:10  → departure → platforms = 0
     *
     * 9:20  → arrival → platforms = 1
     * 9:30  → departure → platforms = 0
     *
     * 9:40  → arrival → platforms = 1
     * 9:50  → departure → platforms = 0
     *
     * Answer = 1
     *
     * Key insight:
     * - Each train leaves before the next arrives.
     * - Never more than one train at any time.
     *
     * --------------------------------------------------------------------------------------------
     * EXAMPLE 3 — MAX PLATFORMS = 1 (UNSORTED INPUT, STILL 1)
     * --------------------------------------------------------------------------------------------
     *
     * Arrivals:   11:00, 9:00, 10:00
     * Departures: 11:30, 9:30, 10:30
     *
     * Timeline (real-world order, not input order):
     *
     * 9:00   → arrival → platforms = 1
     * 9:30   → departure → platforms = 0
     *
     * 10:00  → arrival → platforms = 1
     * 10:30  → departure → platforms = 0
     *
     * 11:00  → arrival → platforms = 1
     * 11:30  → departure → platforms = 0
     *
     * Answer = 1
     *
     * Key insight:
     * - Input order is irrelevant.
     * - Time order is everything.
     *
     * --------------------------------------------------------------------------------------------
     * EXAMPLE 4 — MAX PLATFORMS = 1 (CLOSE TIMES, STILL SAFE)
     * --------------------------------------------------------------------------------------------
     *
     * Arrivals:   9:00, 9:10, 9:20
     * Departures: 9:05, 9:15, 9:25
     *
     * Timeline:
     *
     * 9:00  → arrival → platforms = 1
     * 9:05  → departure → platforms = 0
     *
     * 9:10  → arrival → platforms = 1
     * 9:15  → departure → platforms = 0
     *
     * 9:20  → arrival → platforms = 1
     * 9:25  → departure → platforms = 0
     *
     * Answer = 1
     *
     * --------------------------------------------------------------------------------------------
     * 🔒 LOCKED CONCLUSION
     * --------------------------------------------------------------------------------------------
     *
     * At NO point in ANY example did we:
     * ❌ match arrival to its own departure
     * ❌ track train identity
     *
     * We ONLY counted:
     * ✔ arrivals (+1)
     * ✔ departures (−1)
     * ✔ maximum simultaneous presence
     *
     * This is why sorting arrivals and departures independently is valid.
     */

    /*
     * ============================================================================================
     * 🟡 DRY RUN — STEP-BY-STEP CODE EXECUTION (NO PAIRING, NO MAGIC)
     * ============================================================================================
     *
     * Example Used:
     *
     * Arrivals   = [900, 910, 915]
     * Departures = [930, 920, 925]
     *
     * Step 0 — Sorting (this only orders events in time)
     *
     * arrivals   → [900, 910, 915]
     * departures → [920, 925, 930]
     *
     * Initialization:
     *
     * arrivalIndex   = 0
     * departureIndex = 0
     * currentPlatforms = 0
     * maxPlatforms     = 0
     *
     * --------------------------------------------------------------------------------------------
     * Iteration 1
     * --------------------------------------------------------------------------------------------
     *
     * arrivals[arrivalIndex]   = 900
     * departures[departureIndex] = 920
     *
     * Condition:
     * 900 <= 920  → TRUE
     *
     * Action:
     * currentPlatforms++   → 1
     * maxPlatforms = max(0, 1) → 1
     * arrivalIndex++ → 1
     *
     * Interpretation:
     * One train arrived before any train left.
     *
     * --------------------------------------------------------------------------------------------
     * Iteration 2
     * --------------------------------------------------------------------------------------------
     *
     * arrivals[arrivalIndex]   = 910
     * departures[departureIndex] = 920
     *
     * Condition:
     * 910 <= 920  → TRUE
     *
     * Action:
     * currentPlatforms++   → 2
     * maxPlatforms = max(1, 2) → 2
     * arrivalIndex++ → 2
     *
     * Interpretation:
     * Second train arrived, still no departure yet.
     *
     * --------------------------------------------------------------------------------------------
     * Iteration 3
     * --------------------------------------------------------------------------------------------
     *
     * arrivals[arrivalIndex]   = 915
     * departures[departureIndex] = 920
     *
     * Condition:
     * 915 <= 920  → TRUE
     *
     * Action:
     * currentPlatforms++   → 3
     * maxPlatforms = max(2, 3) → 3
     * arrivalIndex++ → 3
     *
     * Interpretation:
     * Third train arrived before any train left.
     * → This is the PEAK overlap.
     *
     * --------------------------------------------------------------------------------------------
     * Loop Ends
     * --------------------------------------------------------------------------------------------
     *
     * arrivalIndex == arrivals.length → stop loop
     *
     * Final Answer:
     * maxPlatforms = 3
     *
     * --------------------------------------------------------------------------------------------
     * 🔒 CRITICAL OBSERVATION
     * --------------------------------------------------------------------------------------------
     *
     * At NO step did we:
     * ❌ match arrival with its own departure
     * ❌ ask which train is which
     *
     * We ONLY asked ONE question repeatedly:
     *
     * “Does the next arrival happen before the earliest departure?”
     *
     * If YES → platform needed
     * If NO  → platform freed
     *
     * This is why identity is irrelevant.
     */


    /*
     * ============================================================================================
     * 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES
     * ============================================================================================
     */

    /**
     * 🔴 BRUTE FORCE SOLUTION
     */
    static class BruteForce {
        /*
         * Core Idea:
         * Check every time point overlap.
         *
         * Time: O(n²)
         * Space: O(1)
         * Interview Preference: ❌
         */
        static int minPlatforms(int[] arrivals, int[] departures) {
            int maxPlatforms = 0;

            for (int i = 0; i < arrivals.length; i++) {
                int platformsNeeded = 1;

                for (int j = 0; j < arrivals.length; j++) {
                    if (i != j) {
                        if (arrivals[i] <= departures[j] && arrivals[j] <= departures[i]) {
                            platformsNeeded++;
                        }
                    }
                }
                maxPlatforms = Math.max(maxPlatforms, platformsNeeded);
            }
            return maxPlatforms;
        }
    }

    /**
     * 🟡 IMPROVED SOLUTION
     */
    static class Improved {
        /*
         * Core Idea:
         * Sort intervals and count overlaps.
         *
         * Time: O(n log n)
         * Space: O(n)
         * Interview Preference: ⚠️
         */
        static int minPlatforms(int[] arrivals, int[] departures) {
            java.util.Arrays.sort(arrivals);
            java.util.Arrays.sort(departures);

            int currentPlatforms = 0;
            int maxPlatforms = 0;
            int arrivalIndex = 0;
            int departureIndex = 0;

            while (arrivalIndex < arrivals.length) {
                if (arrivals[arrivalIndex] <= departures[departureIndex]) {
                    currentPlatforms++;
                    maxPlatforms = Math.max(maxPlatforms, currentPlatforms);
                    arrivalIndex++;
                } else {
                    currentPlatforms--;
                    departureIndex++;
                }
            }
            return maxPlatforms;
        }
    }

    /**
     * 🟢 OPTIMAL SOLUTION (INTERVIEW-PREFERRED)
     */
    static class Optimal {
        /*
         * Core Idea:
         * Sweep line with two sorted arrays.
         *
         * Fixes:
         * - Removes per-train reasoning
         *
         * Time: O(n log n)
         * Space: O(1) extra
         * Interview Preference: ✅
         */
        static int minPlatforms(int[] arrivals, int[] departures) {
            java.util.Arrays.sort(arrivals);
            java.util.Arrays.sort(departures);

            int arrivalIndex = 0;
            int departureIndex = 0;
            int currentPlatforms = 0;
            int maxPlatforms = 0;

            while (arrivalIndex < arrivals.length) {

                // 🟢 Arrival before or at departure → platform needed
                if (arrivals[arrivalIndex] <= departures[departureIndex]) {
                    currentPlatforms++;
                    maxPlatforms = Math.max(maxPlatforms, currentPlatforms);
                    arrivalIndex++;
                }
                // 🟢 Departure frees platform
                else {
                    currentPlatforms--;
                    departureIndex++;
                }
            }
            return maxPlatforms;
        }
    }

    /*
     * ============================================================================================
     * 7️⃣ 🟣 INTERVIEW ARTICULATION
     * ============================================================================================
     *
     * Why It Works:
     * - Counts simultaneous trains
     *
     * Correctness Invariant:
     * - currentPlatforms always equals active trains
     *
     * What Breaks If Changed:
     * - Changing <= to < violates equal-time constraint
     *
     * In-Place Feasible:
     * - Yes (after sorting)
     *
     * Streaming Feasible:
     * - No (needs sorted events)
     *
     * When NOT to Use:
     * - When intervals are dynamic or mutable
     */

    /*
     * ============================================================================================
     * 8️⃣ 🔄 VARIATIONS & TWEAKS
     * ============================================================================================
     *
     * 🟢 Invariant-Preserving:
     * - Use event array (+1 / -1)
     *
     * 🟡 Reasoning-Only:
     * - Track timestamps instead of pointers
     *
     * 🔴 Pattern-Break:
     * - If reuse of platforms allowed at same time
     */

    /*
     * ============================================================================================
     * 9️⃣ ⚫ REINFORCEMENT PROBLEMS — SAME CORE PATTERN
     * ============================================================================================
     *
     * These problems use the EXACT SAME invariant:
     *
     * “Answer = maximum number of simultaneous active intervals.”
     *
     * If you understand Minimum Platforms, these become mechanical.
     */

    /*
     * ============================================================================================
     * 🔁 REINFORCEMENT PROBLEM 1 — MEETING ROOMS II
     * ============================================================================================
     *
     * 📘 OFFICIAL LEETCODE STATEMENT
     *
     * Given an array of meeting time intervals intervals where intervals[i] = [starti, endi],
     * return the minimum number of conference rooms required.
     *
     * A meeting ending at time t cannot share a room with a meeting starting at time t.
     *
     * Example 1:
     * Input: intervals = [[0,30],[5,10],[15,20]]
     * Output: 2
     *
     * Example 2:
     * Input: intervals = [[7,10],[2,4]]
     * Output: 1
     *
     * Constraints:
     * 1 <= intervals.length <= 10^4
     * 0 <= starti < endi <= 10^6
     *
     * 🔗 https://leetcode.com/problems/meeting-rooms-ii/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Heap, Sorting, Sweep Line
     */

    /*
     * 🧠 PATTERN MAPPING
     *
     * - Meetings = trains
     * - Start time = arrival
     * - End time = departure
     * - Rooms = platforms
     *
     * Invariant carried over:
     * activeMeetings = startsProcessed − endsProcessed
     */

    static class MeetingRoomsII {

        static int minMeetingRooms(int[][] intervals) {

            int n = intervals.length;
            int[] starts = new int[n];
            int[] ends = new int[n];

            for (int i = 0; i < n; i++) {
                starts[i] = intervals[i][0];
                ends[i] = intervals[i][1];
            }

            java.util.Arrays.sort(starts);
            java.util.Arrays.sort(ends);

            int startIndex = 0;
            int endIndex = 0;

            int activeRooms = 0;
            int maxRooms = 0;

            while (startIndex < n) {

                if (starts[startIndex] <= ends[endIndex]) {
                    activeRooms++;
                    maxRooms = Math.max(maxRooms, activeRooms);
                    startIndex++;
                } else {
                    activeRooms--;
                    endIndex++;
                }
            }
            return maxRooms;
        }
    }

    /*
     * 🧪 EDGE CASE & TRAP
     *
     * Trap:
     * - Using < instead of <= breaks the rule that same-time meetings conflict.
     *
     * Interview Note:
     * - This is literally Minimum Platforms with different nouns.
     */

    /*
     * ============================================================================================
     * 🔁 REINFORCEMENT PROBLEM 2 — CAR POOLING
     * ============================================================================================
     *
     * 📘 FULL OFFICIAL LEETCODE STATEMENT
     *
     * There is a car with capacity empty seats.
     * You are given an array trips where trips[i] = [numPassengers, from, to]
     * indicates that the i-th trip has numPassengers passengers and the locations
     * to pick them up and drop them off are from and to respectively.
     *
     * Return true if it is possible to pick up and drop off all passengers
     * without exceeding the car's capacity.
     *
     * Example 1:
     * Input: trips = [[2,1,5],[3,3,7]], capacity = 4
     * Output: false
     *
     * Example 2:
     * Input: trips = [[2,1,5],[3,3,7]], capacity = 5
     * Output: true
     *
     * Constraints:
     * 1 <= trips.length <= 1000
     * 1 <= numPassengers <= 100
     * 0 <= from < to <= 1000
     *
     * 🔗 https://leetcode.com/problems/car-pooling/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Prefix Sum, Sweep Line
     */

    /*
     * 🧠 PATTERN MAPPING
     *
     * - Passenger pickup  → arrival event (+passengers)
     * - Passenger dropoff → departure event (−passengers)
     * - Capacity          → platform limit
     *
     * Same invariant as Minimum Platforms:
     *
     * currentLoad = totalPickupsSoFar − totalDropoffsSoFar
     *
     * This invariant must NEVER exceed capacity.
     */

    static class CarPooling {

        static boolean carPooling(int[][] trips, int capacity) {

            int[] timeline = new int[1001]; // bounded by problem constraints

            for (int[] trip : trips) {
                timeline[trip[1]] += trip[0]; // pickup event
                timeline[trip[2]] -= trip[0]; // dropoff event
            }

            int currentLoad = 0;

            for (int delta : timeline) {
                currentLoad += delta;

                // 🟢 Invariant check: load must never exceed capacity
                if (currentLoad > capacity) {
                    return false;
                }
            }
            return true;
        }
    }

    /*
     * 🧪 EDGE CASES & INTERVIEW TRAPS
     *
     * Trap 1:
     * - Treating this as greedy assignment instead of sweep-line accumulation.
     *
     * Trap 2:
     * - Forgetting that dropoff at location X happens BEFORE pickup at X+ε.
     *
     * Edge Case:
     * trips = [[3,5,7],[3,7,9]], capacity = 3 → true
     * (handoff happens cleanly at the same point)
     */

    /*
     * 🟣 INTERVIEW ARTICULATION
     *
     * This is Minimum Platforms with weighted arrivals.
     * I convert pickups and dropoffs into + and − events on a timeline.
     * I scan the timeline and track current passenger load.
     * If the load ever exceeds capacity, the answer is false.
     * Otherwise, it is true.
     */


    /*
     * ============================================================================================
     * 🔟 RELATED PROBLEM — PATTERN BOUNDARY (NOT A REINFORCEMENT)
     * ============================================================================================
     *
     * MINIMUM NUMBER OF ARROWS TO BURST BALLOONS
     *
     * 📘 FULL OFFICIAL LEETCODE STATEMENT (SUMMARY)
     *
     * You are given an array of balloons where balloons[i] = [start, end].
     * One arrow can burst all balloons whose intervals overlap at a point.
     * Return the minimum number of arrows required.
     *
     * 🔗 https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Greedy, Sorting
     */

    /*
     * 🧠 RELATIONSHIP TO PRIMARY PATTERN
     *
     * ❌ NOT an overlap counting problem
     * ❌ NOT sweep-line accumulation
     *
     * This is a PATTERN BREAK:
     * - Goal is to SELECT minimum arrows
     * - Not to COUNT maximum overlap
     *
     * Requires greedy strategy:
     * - Sort by end time
     * - Shoot arrow at earliest possible end
     *
     * This problem exists here to teach:
     * WHEN the Minimum Platforms pattern MUST be abandoned.
     */

    static class MinArrows {

        static int findMinArrowShots(int[][] points) {

            java.util.Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));

            int arrows = 1;
            long currentArrowEnd = points[0][1];

            for (int i = 1; i < points.length; i++) {
                if (points[i][0] > currentArrowEnd) {
                    arrows++;
                    currentArrowEnd = points[i][1];
                }
            }
            return arrows;
        }
    }

    /*
     * 🧪 EDGE CASE & INTERVIEW NOTE
     *
     * Edge Case:
     * - Single balloon → 1 arrow
     *
     * Interviewer Intent:
     * - To see if you wrongly apply sweep-line counting
     * - Or correctly recognize this as a greedy selection problem
     */


    /*
     * ============================================================================================
     * 11️⃣ 🟢 LEARNING VERIFICATION
     * ============================================================================================
     *
     * Mastery Check:
     * - Can you explain without code?
     * - Can you modify equality condition?
     * - Can you reason about worst overlap?
     */

    /*
     * ============================================================================================
     * 12️⃣ 🧪 MAIN METHOD + SELF-VERIFYING TESTS
     * ============================================================================================
     */
    public static void main(String[] args) {

        assertEquals(3,
                Optimal.minPlatforms(
                        new int[]{1000, 935, 1100},
                        new int[]{1200, 1240, 1130}),
                "All trains overlap");

        assertEquals(1,
                Optimal.minPlatforms(
                        new int[]{900, 1235, 1100},
                        new int[]{1000, 1240, 1200}),
                "No overlap");

        assertEquals(2,
                Optimal.minPlatforms(
                        new int[]{900, 900},
                        new int[]{910, 920}),
                "Same arrival time");

        System.out.println("✅ All tests passed.");
    }

    private static void assertEquals(int expected, int actual, String reason) {
        if (expected != actual) {
            throw new AssertionError(
                    "Test failed: " + reason +
                            " | Expected: " + expected +
                            ", Actual: " + actual);
        }
    }

    /*
     * ============================================================================================
     * 13️⃣ 🧠 CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
     * ============================================================================================
     *
     * Invariant clarity
     * → Answer: currentPlatforms equals active trains
     *
     * Search target clarity
     * → Answer: maximum simultaneous trains
     *
     * Discard logic
     * → Answer: earliest departure frees platform
     *
     * Termination guarantee
     * → Answer: arrivalIndex strictly increases
     *
     * Failure awareness
     * → Answer: naive per-train overlap fails globally
     *
     * Edge-case confidence
     * → Answer: equal times handled via <=
     *
     * Variant readiness
     * → Answer: adjust comparator for reuse rules
     *
     * Pattern boundary
     * → Answer: fails when events are dynamic
     */

    /*
     * 🧘 FINAL CLOSURE STATEMENT
     *
     * For this problem, the invariant is the count of active trains.
     * The answer represents the maximum overlap.
     * The search terminates because arrivals are finite.
     * I can re-derive this solution under pressure.
     * This chapter is complete.
     */
}
