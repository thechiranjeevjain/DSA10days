package org.chijai.day7.session1.heap;

import java.util.TreeMap;

/**
 * ============================================================================
 * SLIDING WINDOW MEDIAN
 * ============================================================================
 *
 * Pattern:
 *      Two Ordered Halves / Balanced Multisets
 *
 * Preferred Java idea:
 *
 *      lower = lower half
 *      upper = upper half
 *
 *      lower.lastKey()  = largest lower value
 *      upper.firstKey() = smallest upper value
 *
 * Why TreeMap instead of PriorityQueue here?
 *
 *      Stream Median:
 *          only insert
 *          -> two heaps are perfect
 *
 *      Sliding Median:
 *          insert incoming
 *          remove outgoing
 *
 *      PriorityQueue cannot efficiently remove an arbitrary buried value.
 *      TreeMap can add/remove any value in O(log k).
 *
 * Invariants:
 *
 *      every lower value <= every upper value
 *
 *      lowerSize == upperSize
 *      OR
 *      lowerSize == upperSize + 1
 *
 * Complexity:
 *
 *      each slide    O(log k)
 *      total         O(n log k)
 *      space         O(k)
 *
 * LeetCode:
 *      https://leetcode.com/problems/sliding-window-median/
 */
public class SlidingWindowMedian {

    /*
     * =========================================================================
     * PRIMARY INTERVIEW SOLUTION
     * =========================================================================
     *
     * TreeMap<Integer, Integer> acts as a multiset:
     *
     *      key   = number
     *      value = frequency
     *
     * We track element counts separately because:
     *
     *      map.size()
     *
     * counts DISTINCT keys, not total elements.
     */

    private final TreeMap<Integer, Integer> lower = new TreeMap<>();
    private final TreeMap<Integer, Integer> upper = new TreeMap<>();

    private int lowerSize;
    private int upperSize;
    private int windowSize;

    public double[] medianSlidingWindow(int[] nums, int k) {

        if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
            throw new IllegalArgumentException("Invalid input.");
        }

        reset(k);

        double[] answer = new double[nums.length - k + 1];

        int right = 0;

        while (right < nums.length) {

            add(nums[right]);

            if (right >= k) {
                remove(nums[right - k]);
            }

            if (right >= k - 1) {
                answer[right - k + 1] = median();
            }

            right++;
        }

        return answer;
    }

    private void add(int num) {

        if (lowerSize == 0 || num <= lower.lastKey()) {
            addOne(lower, num);
            lowerSize++;
        } else {
            addOne(upper, num);
            upperSize++;
        }

        rebalance();
    }

    private void remove(int num) {

        /*
         * If the same value exists in both halves,
         * removing either identical copy is equivalent.
         *
         * Prefer lower when present.
         */
        if (lower.containsKey(num)) {
            removeOne(lower, num);
            lowerSize--;
        } else {
            removeOne(upper, num);
            upperSize--;
        }

        rebalance();
    }

    private void rebalance() {

        if (lowerSize > upperSize + 1) {

            int boundary = lower.lastKey();

            removeOne(lower, boundary);
            lowerSize--;

            addOne(upper, boundary);
            upperSize++;

        } else if (upperSize > lowerSize) {

            int boundary = upper.firstKey();

            removeOne(upper, boundary);
            upperSize--;

            addOne(lower, boundary);
            lowerSize++;
        }
    }

    private double median() {

        if (windowSize % 2 == 1) {
            return lower.lastKey();
        }

        return (
                (double) lower.lastKey()
                        + upper.firstKey()
        ) / 2.0;
    }

    private void addOne(
            TreeMap<Integer, Integer> multiset,
            int value) {

        multiset.put(
                value,
                multiset.getOrDefault(value, 0) + 1
        );
    }

    private void removeOne(
            TreeMap<Integer, Integer> multiset,
            int value) {

        int count = multiset.get(value);

        if (count == 1) {
            multiset.remove(value);
        } else {
            multiset.put(value, count - 1);
        }
    }

    private void reset(int k) {

        lower.clear();
        upper.clear();

        lowerSize = 0;
        upperSize = 0;

        windowSize = k;
    }

    /*
     * =========================================================================
     * WHY 1 — WHAT CHANGED FROM STREAM MEDIAN?
     * =========================================================================
     *
     * Find Median from Data Stream:
     *
     *      add
     *      add
     *      add
     *
     * Sliding Window Median:
     *
     *      add incoming
     *      remove outgoing
     *
     * That ONE new operation changes the data-structure choice.
     *
     * PriorityQueue:
     *
     *      remove root       O(log k)
     *      remove arbitrary  O(k)
     *
     * TreeMap:
     *
     *      add arbitrary     O(log k)
     *      remove arbitrary  O(log k)
     *      min/max boundary  O(log k)
     *
     * So the original two-half MODEL survives;
     * only the representation changes.
     */

    /*
     * =========================================================================
     * WHY 2 — WHY A FREQUENCY MAP, NOT TreeSet?
     * =========================================================================
     *
     * Duplicates matter.
     *
     * Window:
     *
     *      [2, 2, 2]
     *
     * TreeSet would store only:
     *
     *      {2}
     *
     * TreeMap stores:
     *
     *      2 -> 3
     *
     * Therefore:
     *
     *      TreeMap<Integer, Integer>
     *
     * behaves like Java's missing ordered multiset.
     */

    /*
     * =========================================================================
     * WHY 3 — WHY TRACK lowerSize / upperSize?
     * =========================================================================
     *
     * TreeMap.size() means:
     *
     *      number of DISTINCT values
     *
     * Not:
     *
     *      number of elements
     *
     * Example:
     *
     *      lower contains [2,2,2]
     *
     *      lower.size()    = 1
     *      lowerSize       = 3
     *
     * Median balancing needs total element counts,
     * so lowerSize / upperSize are required.
     */

    /*
     * =========================================================================
     * WHY 4 — WHY THESE BOUNDARIES?
     * =========================================================================
     *
     * lower.lastKey()
     *      = largest value in lower half
     *
     * upper.firstKey()
     *      = smallest value in upper half
     *
     * These are exactly the two values touching the median boundary.
     *
     * Rebalance:
     *
     *      lower too big
     *          -> move lower.lastKey()
     *
     *      upper too big
     *          -> move upper.firstKey()
     *
     * Same invariant as ordinary stream median.
     */

    /*
     * =========================================================================
     * WHY 5 — WHY CAN remove() CHECK lower.containsKey(num)?
     * =========================================================================
     *
     * We only care about the multiset of values in each half,
     * not the identity of a particular duplicate occurrence.
     *
     * If value 5 exists in both halves and one 5 leaves the window,
     * removing either copy of 5 produces an equivalent multiset.
     *
     * Rebalancing then restores the required half sizes.
     */

    /*
     * =========================================================================
     * DRY RUN
     * =========================================================================
     *
     * nums = [1, 3, -1, -3]
     * k = 3
     *
     * Build first window:
     *
     *      [1, 3, -1]
     *
     * balanced halves:
     *
     *      lower = [-1, 1]
     *      upper = [3]
     *
     * median = lower maximum = 1
     *
     * Slide:
     *
     *      incoming = -3
     *      outgoing = 1
     *
     * after add/remove/rebalance:
     *
     *      lower = [-3, -1]
     *      upper = [3]
     *
     * median = -1
     *
     * Repeat:
     *
     *      ADD incoming
     *      REMOVE outgoing
     *      REBALANCE
     *      READ boundary
     */

    /*
     * =========================================================================
     * 30-SECOND RECALL CARD
     * =========================================================================
     *
     * START FROM STREAM MEDIAN:
     *
     *      lower half
     *      upper half
     *      boundary gives median
     *
     * SLIDING WINDOW ADDS:
     *
     *      arbitrary deletion
     *
     * JAVA-FRIENDLY FIX:
     *
     *      two TreeMap multisets
     *
     * BOUNDARIES:
     *
     *      lower.lastKey()
     *      upper.firstKey()
     *
     * SIZE:
     *
     *      lowerSize == upperSize
     *      OR
     *      lowerSize == upperSize + 1
     *
     * LOOP:
     *
     *      add incoming
     *      remove outgoing
     *      rebalance
     *      median
     *
     * ONE-LINER:
     *
     *      Keep the median partition,
     *      upgrade heaps to deletable ordered multisets.
     */

    /*
     * =========================================================================
     * REUSABLE MASTER TEMPLATE
     * =========================================================================
     *
     * When a sliding window needs an order statistic:
     *
     *      1. Maintain ordered lower / upper partitions.
     *
     *      2. Use a structure supporting:
     *
     *          insert
     *          delete arbitrary value
     *          boundary lookup
     *
     *      3. Balance partition sizes.
     *
     *      4. Read the answer from the boundary.
     *
     * Java options:
     *
     *      TreeMap multisets
     *          simpler deletion logic
     *
     *      Two heaps + lazy deletion
     *          canonical heap solution
     *          more bookkeeping
     */

    /*
     * =========================================================================
     * ALTERNATIVE — TWO HEAPS + LAZY DELETION
     * =========================================================================
     *
     * Know the pattern; do not mix its machinery into this preferred solution.
     *
     * Why needed?
     *
     *      PriorityQueue cannot cheaply remove a buried outgoing value.
     *
     * Idea:
     *
     *      delayed[value]++
     *
     * means:
     *
     *      logically deleted now,
     *      physically remove later when value reaches heap.peek().
     *
     * Extra state:
     *
     *      delayed map
     *      logical lower size
     *      logical upper size
     *      prune(heap)
     *
     * Same complexity:
     *
     *      O(n log k)
     *
     * Interview choice:
     *
     *      If TreeMap / ordered multiset is accepted:
     *          prefer the simpler TreeMap solution in Java.
     *
     *      If interviewer specifically asks for heaps:
     *          use lazy deletion.
     */

    /*
     * =========================================================================
     * INTERVIEW ARTICULATION
     * =========================================================================
     *
     * "This starts from the same two-half invariant as stream median, but a
     * sliding window adds arbitrary deletion. Java PriorityQueue cannot delete
     * a buried value efficiently, so I represent each half as a TreeMap
     * multiset. The lower half exposes lastKey(), the upper half exposes
     * firstKey(), and I track total element counts separately because
     * TreeMap.size() counts only distinct keys. Every insert, delete, and
     * rebalance is O(log k), so the full scan is O(n log k)."
     */

    /*
     * =========================================================================
     * INTERVIEW TRAPS
     * =========================================================================
     *
     * TreeSet
     *      WRONG when duplicates exist.
     *
     * TreeMap.size()
     *      counts distinct values, not elements.
     *
     * PriorityQueue.remove(value)
     *      O(k), not O(log k).
     *
     * Forgetting to rebalance after remove
     *      breaks the median boundary.
     *
     * Integer addition before averaging
     *      may overflow.
     */

    /*
     * =========================================================================
     * RELATED / REINFORCEMENT
     * =========================================================================
     *
     * Find Median from Data Stream
     *      same partition
     *      insert only
     *
     * Kth order statistic in a moving window
     *      same need for ordered insert + delete
     *
     * Ordered multiset problems
     *      TreeMap<value, frequency> is a reusable Java substitute.
     */

    public static void main(String[] args) {

        SlidingWindowMedian solution = new SlidingWindowMedian();

        assertArrayEquals(
                new double[]{1, -1, -1, 3, 5, 6},
                solution.medianSlidingWindow(
                        new int[]{1, 3, -1, -3, 5, 3, 6, 7},
                        3
                )
        );

        assertArrayEquals(
                new double[]{2.5},
                solution.medianSlidingWindow(
                        new int[]{1, 4, 2, 3},
                        4
                )
        );

        assertArrayEquals(
                new double[]{2, 2, 2},
                solution.medianSlidingWindow(
                        new int[]{2, 2, 2, 2, 2},
                        3
                )
        );

        assertArrayEquals(
                new double[]{Integer.MAX_VALUE},
                solution.medianSlidingWindow(
                        new int[]{Integer.MAX_VALUE},
                        1
                )
        );

        System.out.println("SlidingWindowMedian: all assertions passed.");
    }

    private static void assertArrayEquals(
            double[] expected,
            double[] actual) {

        assert expected.length == actual.length;

        int index = 0;

        while (index < expected.length) {

            assert Math.abs(expected[index] - actual[index]) < 1e-9
                    : "Mismatch at index " + index
                    + ": expected=" + expected[index]
                    + ", actual=" + actual[index];

            index++;
        }
    }
}
