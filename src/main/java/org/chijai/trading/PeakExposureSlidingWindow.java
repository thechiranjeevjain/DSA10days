package org.chijai.trading;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ===============================================================
 * Problem: Peak Exposure in Any N-Day Window
 * ===============================================================
 *
 * CLASSIFICATION
 * ---------------------------------------------------------------
 * Primary pattern: Fixed-size Sliding Window
 * Supporting idea: Running sum / aggregation
 *
 * Recognition trigger:
 *
 *     "Find the maximum / minimum / average / total
 *      over every contiguous window of exactly N elements."
 *
 * If the input already contains one value per day, then an N-day
 * interval is exactly a fixed-size window of N array elements.
 *
 * ---------------------------------------------------------------
 * PROBLEM STATEMENT
 * ---------------------------------------------------------------
 * A financial system records one cash-flow / exposure contribution
 * for each calendar day.
 *
 * You are given:
 *
 *     long[] cashFlows
 *
 * where:
 *
 *     cashFlows[i] = cash-flow contribution on day i + 1
 *
 * and:
 *
 *     int n
 *
 * representing the exact number of consecutive days that must be
 * included in the exposure window.
 *
 * Your task is to examine EVERY contiguous window containing exactly
 * n days and return the largest total cash flow among those windows.
 *
 * In other words, for every valid starting index start, calculate:
 *
 *     cashFlows[start]
 *       + cashFlows[start + 1]
 *       + ...
 *       + cashFlows[start + n - 1]
 *
 * and return the maximum of these sums.
 *
 * ---------------------------------------------------------------
 * IMPORTANT INTERPRETATION
 * ---------------------------------------------------------------
 * "N-day window" means N CONSECUTIVE days.
 *
 * We cannot skip a day simply because its cash flow is negative.
 *
 * For example:
 *
 *     cashFlows = [10, -100, 200]
 *     n = 2
 *
 * Valid windows are only:
 *
 *     [10, -100]
 *     [-100, 200]
 *
 * We are NOT allowed to choose:
 *
 *     [10, 200]
 *
 * because those values are not contiguous.
 *
 * Also, "peak exposure" in this problem means the MAXIMUM SIGNED SUM.
 * It does not mean maximum absolute value.
 *
 * ---------------------------------------------------------------
 * RETURN VALUE
 * ---------------------------------------------------------------
 * Return the maximum sum of any contiguous subarray of exactly n
 * elements.
 *
 * ---------------------------------------------------------------
 * ASSUMPTIONS / INPUT REQUIREMENTS
 * ---------------------------------------------------------------
 * - cashFlows is not null.
 * - n > 0.
 * - cashFlows.length >= n.
 * - Each array element already represents exactly one day.
 * - Cash flows may be positive, zero, or negative.
 * - long is used for sums because accumulated financial values may
 *   exceed the range of int even when individual inputs are moderate.
 *
 * Invalid inputs are rejected with IllegalArgumentException.
 *
 * ---------------------------------------------------------------
 * EXAMPLE 1 — NORMAL MIX OF POSITIVE AND NEGATIVE CASH FLOWS
 * ---------------------------------------------------------------
 * Input:
 *
 *     cashFlows = [10, 20, -5, 40, 15]
 *     n = 3
 *
 * All possible 3-day windows:
 *
 *     Days 1-3:
 *         10 + 20 - 5 = 25
 *
 *     Days 2-4:
 *         20 - 5 + 40 = 55
 *
 *     Days 3-5:
 *         -5 + 40 + 15 = 50
 *
 * The largest total is:
 *
 *     55
 *
 * Output:
 *
 *     55
 *
 * ---------------------------------------------------------------
 * EXAMPLE 2 — NEGATIVE VALUES CANNOT BE SKIPPED
 * ---------------------------------------------------------------
 * Input:
 *
 *     cashFlows = [100, -150, 300, -20]
 *     n = 2
 *
 * Windows:
 *
 *     Days 1-2: 100 - 150 = -50
 *     Days 2-3: -150 + 300 = 150
 *     Days 3-4: 300 - 20 = 280
 *
 * Output:
 *
 *     280
 *
 * ---------------------------------------------------------------
 * EXAMPLE 3 — ALL WINDOWS HAVE NEGATIVE EXPOSURE
 * ---------------------------------------------------------------
 * Input:
 *
 *     cashFlows = [-8, -3, -6, -2]
 *     n = 2
 *
 * Windows:
 *
 *     Days 1-2: -8 + -3 = -11
 *     Days 2-3: -3 + -6 = -9
 *     Days 3-4: -6 + -2 = -8
 *
 * Even though every total is negative, we still choose the largest
 * signed value.
 *
 * Output:
 *
 *     -8
 *
 * This is why maxExposure must NOT start at 0. If it started at 0,
 * this case would incorrectly return 0 even though no such window
 * exists.
 *
 * ---------------------------------------------------------------
 * EXAMPLE 4 — N = 1
 * ---------------------------------------------------------------
 * Input:
 *
 *     cashFlows = [12, -4, 30, 7]
 *     n = 1
 *
 * Every individual day is its own window:
 *
 *     12, -4, 30, 7
 *
 * Output:
 *
 *     30
 *
 * ---------------------------------------------------------------
 * EXAMPLE 5 — N EQUALS THE ENTIRE ARRAY LENGTH
 * ---------------------------------------------------------------
 * Input:
 *
 *     cashFlows = [25, -10, 5]
 *     n = 3
 *
 * There is only one valid window:
 *
 *     25 - 10 + 5 = 20
 *
 * Output:
 *
 *     20
 *
 * ---------------------------------------------------------------
 * WHY THIS IS A SLIDING-WINDOW PROBLEM
 * ---------------------------------------------------------------
 * Every valid answer candidate has the SAME fixed size n, and
 * neighboring windows overlap in n - 1 positions.
 *
 * Example for n = 3:
 *
 *     [10, 20, -5]
 *          [20, -5, 40]
 *
 * Instead of summing all three values again, the next sum is:
 *
 *     nextWindowSum
 *         = previousWindowSum
 *         - valueThatLeaves
 *         + valueThatEnters
 *
 * That is the fixed-size sliding-window pattern.
 *
 * ---------------------------------------------------------------
 * INTERVIEW FOLLOW-UP
 * ---------------------------------------------------------------
 * If the input contains arbitrary timestamped transactions instead
 * of exactly one aggregated value per day, then "N days" is a time
 * interval rather than exactly N array elements.
 *
 * That variation requires a time-based sliding window / two pointers
 * and is shown later in this file.
 *
 * ===============================================================
 */
public class PeakExposureSlidingWindow {

    /* =============================================================
     * PRIMARY SOLUTION — FIXED-SIZE SLIDING WINDOW
     * =============================================================
     *
     * FIRST-PRINCIPLES INVENTION PATH
     * -------------------------------------------------------------
     * Brute force would calculate every n-day window from scratch.
     *
     * But two neighboring windows overlap almost completely.
     *
     * Example, n = 3:
     *
     *     old window = [10, 20, -5]
     *     new window =     [20, -5, 40]
     *
     * Only two things changed:
     *
     *     10 left the window
     *     40 entered the window
     *
     * Therefore:
     *
     *     newWindowSum
     *         = oldWindowSum
     *         - valueLeaving
     *         + valueEntering
     *
     * That observation removes the inner loop.
     * =============================================================
     */
    static long peakExposure(long[] cashFlows, int n) {

        if (cashFlows == null || n <= 0 || cashFlows.length < n) {
            throw new IllegalArgumentException(
                    "cashFlows must contain at least n values and n must be positive");
        }

        long windowSum = 0;

        /*
         * Build the first complete n-day window.
         */
        for (int i = 0; i < n; i++) {
            windowSum += cashFlows[i];
        }

        long maxExposure = windowSum;

        /*
         * right is the new element entering the window.
         *
         * If right = n, then index 0 leaves.
         * If right = n + 1, then index 1 leaves.
         *
         * In general:
         *
         *     leaving index = right - n
         */
        for (int right = n; right < cashFlows.length; right++) {

            windowSum += cashFlows[right];
            windowSum -= cashFlows[right - n];

            maxExposure = Math.max(maxExposure, windowSum);
        }

        return maxExposure;
    }

    /* =============================================================
     * DRY RUN
     * =============================================================
     *
     * cashFlows = [10, 20, -5, 40, 15]
     * n = 3
     *
     * First window:
     *
     *     10 + 20 - 5 = 25
     *
     *     windowSum   = 25
     *     maxExposure = 25
     *
     * -------------------------------------------------------------
     * right = 3, entering = 40
     * leaving index = 3 - 3 = 0, leaving = 10
     *
     *     windowSum = 25 + 40 - 10
     *               = 55
     *
     *     maxExposure = 55
     *
     * Window is now [20, -5, 40].
     *
     * -------------------------------------------------------------
     * right = 4, entering = 15
     * leaving index = 4 - 3 = 1, leaving = 20
     *
     *     windowSum = 55 + 15 - 20
     *               = 50
     *
     *     maxExposure remains 55
     *
     * Final answer = 55
     * =============================================================
     */

    /* =============================================================
     * COMPLEXITY
     * =============================================================
     *
     * Time: O(D)
     *
     * Why:
     * - First loop processes n elements once.
     * - Second loop processes the remaining D - n elements once.
     *
     * Total work:
     *
     *     n + (D - n) = D
     *
     * Therefore O(D).
     *
     * Space: O(1)
     *
     * We only keep a few numeric variables regardless of input size.
     * =============================================================
     */

    /* =============================================================
     * BRUTE FORCE REFERENCE
     * =============================================================
     *
     * Useful only to see what the sliding window optimizes away.
     *
     * For every possible starting day, this recomputes all n values.
     *
     * Time: O((D - n + 1) * n) -> O(D * n)
     * Space: O(1)
     * =============================================================
     */
    static long peakExposureBruteForce(long[] cashFlows, int n) {

        if (cashFlows == null || n <= 0 || cashFlows.length < n) {
            throw new IllegalArgumentException(
                    "cashFlows must contain at least n values and n must be positive");
        }

        long maxExposure = Long.MIN_VALUE;

        for (int start = 0; start + n <= cashFlows.length; start++) {

            long sum = 0;

            for (int i = start; i < start + n; i++) {
                sum += cashFlows[i];
            }

            maxExposure = Math.max(maxExposure, sum);
        }

        return maxExposure;
    }

    /* =============================================================
     * FOLLOW-UP — RAW TIMESTAMPED CASH FLOWS
     * =============================================================
     *
     * Important distinction:
     *
     * If the input is not "one array element per calendar day" and
     * instead contains arbitrary timestamped events, then an N-day
     * window is NOT necessarily N array elements.
     *
     * Example events might be:
     *
     *     Day 1 -> +100
     *     Day 1 -> -20
     *     Day 4 -> +50
     *     Day 9 -> +30
     *
     * In that case, use a time-based sliding window / two pointers.
     * Keep events whose timestamps satisfy the required N-day range,
     * remove events that become too old, and maintain the running sum.
     *
     * The method below assumes:
     * - events arrive sorted by day
     * - day is represented as an integer
     * - a window ending on day d contains events from
     *   day d - n + 1 through day d, inclusive
     * =============================================================
     */
    static long peakExposureTimestamped(Event[] events, int n) {

        if (events == null || n <= 0) {
            throw new IllegalArgumentException(
                    "events must not be null and n must be positive");
        }

        if (events.length == 0) {
            return 0;
        }

        Deque<Event> window = new ArrayDeque<>();
        long windowSum = 0;
        long maxExposure = Long.MIN_VALUE;

        for (Event event : events) {

            window.addLast(event);
            windowSum += event.amount;

            int earliestAllowedDay = event.day - n + 1;

            while (!window.isEmpty()
                    && window.peekFirst().day < earliestAllowedDay) {

                windowSum -= window.removeFirst().amount;
            }

            maxExposure = Math.max(maxExposure, windowSum);
        }

        return maxExposure;
    }

    static class Event {
        int day;
        long amount;

        Event(int day, long amount) {
            this.day = day;
            this.amount = amount;
        }
    }

    /* =============================================================
     * INTERVIEW RECALL
     * =============================================================
     *
     * EXACTLY N contiguous values
     *          -> fixed-size sliding window
     *
     * AT MOST / WITHIN N days using timestamps
     *          -> time-based sliding window / two pointers
     *
     * Core fixed-window transition:
     *
     *     windowSum += entering;
     *     windowSum -= leaving;
     *
     * Do not recompute the overlapping portion.
     * =============================================================
     */

    public static void main(String[] args) {

        long[] cashFlows = {10, 20, -5, 40, 15};
        int n = 3;

        System.out.println(peakExposure(cashFlows, n));
        // 55
    }
}
