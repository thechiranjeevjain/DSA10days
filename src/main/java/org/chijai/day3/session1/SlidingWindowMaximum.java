package org.chijai.day3.session1;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 239 - Sliding Window Maximum
 *
 * Link: https://leetcode.com/problems/sliding-window-maximum/
 * Difficulty: Hard
 * Tags: Array, Queue, Sliding Window, Heap, Monotonic Queue
 *
 * Repository chapter pattern:
 * PROBLEM -> BASELINE -> RECOGNITION -> INVARIANT -> TRAPS -> OPTIMAL -> DEFEND
 *
 * ================================================================
 * 1. PRIMARY PROBLEM - OFFICIAL REQUIREMENTS + ELI5 RESTATEMENT
 * ================================================================
 *
 * Imagine looking at only k consecutive numbers through a movable window.
 * For every position of that window, report the largest visible number.
 * Then move the window one position to the right and repeat.
 *
 * Example:
 *
 *     nums = [1, 3, -1, -3, 5, 3, 6, 7]
 *     k = 3
 *
 *     Window                    Maximum
 *     [1, 3, -1] -3  5  3  6  7     3
 *      1 [3, -1, -3] 5  3  6  7     3
 *      1  3 [-1, -3, 5] 3  6  7     5
 *      1  3  -1 [-3, 5, 3] 6  7     5
 *      1  3  -1  -3 [5, 3, 6] 7     6
 *      1  3  -1  -3  5 [3, 6, 7]    7
 *
 *     answer = [3, 3, 5, 5, 6, 7]
 *
 * If nums has n values, there are n - k + 1 valid windows. That is why the
 * result array has length nums.length - k + 1.
 *
 * Official constraints:
 *
 *     1 <= nums.length <= 100,000
 *     -10,000 <= nums[i] <= 10,000
 *     1 <= k <= nums.length
 *
 * ================================================================
 * 2. CORE PATTERN OVERVIEW + BRUTE-FORCE BASELINE
 * ================================================================
 *
 * The most direct solution is:
 *
 * 1. Start a window at every valid position.
 * 2. Scan all k values inside that window.
 * 3. Save the largest value.
 *
 * For n windows of size k, this costs O(n * k) time in the worst case.
 * With n = 100,000 and a large k, repeatedly rescanning almost the same values
 * wastes too much work.
 *
 * The key question is:
 *
 *     When the window moves one step, what old information is still useful?
 *
 * ================================================================
 * 3. PATTERN RECOGNITION: WHY A MONOTONIC DEQUE
 * ================================================================
 *
 * A queue can tell us which value entered first and should expire first, but
 * it cannot tell us the maximum in O(1) time.
 *
 * A max heap can expose the maximum, but removing an expired value that is not
 * at the heap root is awkward. A heap also costs O(log k) per insertion.
 *
 * We need a structure that supports both facts:
 *
 * 1. Which candidates have expired from the left side of the window?
 * 2. Which remaining candidate is largest?
 *
 * A double-ended queue, or deque, can remove from both ends and maintain both
 * facts in O(1) amortized time.
 *
 * ================================================================
 * 4. MENTAL MODEL: WHY DOMINATED VALUES CAN BE FORGOTTEN
 * ================================================================
 *
 * Suppose the deque currently contains an older value 2, and a new value 5
 * arrives to its right:
 *
 *     older 2 ... newer 5
 *
 * The 2 can never become a future window maximum:
 *
 * 1. 5 is larger than 2.
 * 2. 5 is newer, so 5 will remain in the window longer than 2.
 *
 * Therefore, 5 dominates 2. We permanently remove 2.
 *
 * The same reasoning removes every smaller or equal value from the back before
 * adding the new value. What remains is a decreasing sequence of candidates.
 *
 * Example candidate values:
 *
 *     front [9, 7, 4, 1] back
 *
 * The largest candidate is always at the front.
 *
 * ================================================================
 * 5. CORE INVARIANT: WHY THE DEQUE STORES INDICES
 * ================================================================
 *
 * The window moves, so old elements eventually expire. A value alone does not
 * tell us where it came from. An index tells us both:
 *
 *     value      = nums[index]
 *     expiration = index <= right - k
 *
 * Example: if right = 5 and k = 3, the current window covers indices 3, 4, 5.
 * Any index <= 2 has left the window and must be removed from the front.
 *
 * ================================================================
 * 6. OPTIMAL SOLUTION RULES DERIVED FROM THE INVARIANT
 * ================================================================
 *
 * For every new index right:
 *
 * RULE A — REMOVE EXPIRED INDICES FROM THE FRONT
 *
 *     while frontIndex <= right - k:
 *         remove frontIndex
 *
 * RULE B — REMOVE DOMINATED VALUES FROM THE BACK
 *
 *     while nums[backIndex] <= nums[right]:
 *         remove backIndex
 *
 * Then add right at the back.
 *
 * After these rules:
 *
 * 1. Every stored index belongs to the current window.
 * 2. Stored values are decreasing from front to back.
 * 3. The front index points to the current maximum.
 *
 * ================================================================
 * 7. STEP-BY-STEP TRACE
 * ================================================================
 *
 * Input: nums = [1, 3, -1, -3, 5], k = 3
 * The deque below shows index:value.
 *
 * right = 0, value = 1
 *     add 0
 *     deque = [0:1]
 *     no complete window yet
 *
 * right = 1, value = 3
 *     3 dominates 1, so remove index 0 from the back
 *     add 1
 *     deque = [1:3]
 *     no complete window yet
 *
 * right = 2, value = -1
 *     -1 does not dominate 3
 *     add 2
 *     deque = [1:3, 2:-1]
 *     first complete window [1, 3, -1]
 *     front value = 3, so output 3
 *
 * right = 3, value = -3
 *     expired boundary = right - k = 0
 *     front index 1 is still valid
 *     -3 does not dominate -1
 *     add 3
 *     deque = [1:3, 2:-1, 3:-3]
 *     window [3, -1, -3], output 3
 *
 * right = 4, value = 5
 *     expired boundary = 1
 *     remove expired front index 1
 *     5 dominates -3, remove index 3
 *     5 dominates -1, remove index 2
 *     add 4
 *     deque = [4:5]
 *     window [-1, -3, 5], output 5
 *
 * ================================================================
 * 8. CORRECTNESS PROOF: LOOP INVARIANT
 * ================================================================
 *
 * After processing index right, the deque contains only useful candidate
 * indices for the current and future windows:
 *
 * 1. Indices increase from front to back.
 * 2. Their values decrease from front to back.
 * 3. No index is outside the current window.
 * 4. The front value is the current window maximum.
 *
 * This invariant is the main idea to defend in an interview.
 *
 * ================================================================
 * 9. EDGE CASES + INTERVIEW TRAPS
 * ================================================================
 *
 * Window size 1:
 *
 *     nums = [4, 2, 7], k = 1
 *     answer = [4, 2, 7]
 *
 * Window covers the entire array:
 *
 *     nums = [4, 2, 7], k = 3
 *     answer = [7]
 *
 * Duplicate maximums:
 *
 *     nums = [4, 4, 2], k = 2
 *     answer = [4, 4]
 *
 * Negative values:
 *
 *     nums = [-4, -2, -5], k = 2
 *     answer = [-2, -2]
 *
 * Implementation traps:
 *
 * 1. Store indices, not values, because indices reveal expiration.
 * 2. Remove expired indices from the front, never from the back.
 * 3. Remove dominated values from the back, never from the front.
 * 4. Emit an answer only when right >= k - 1.
 * 5. Write the output at right - k + 1.
 * 6. Do not call the deque a normal sliding-window frequency pattern: the
 *    fixed window determines membership; the monotonic deque answers max.
 *
 * ================================================================
 * 10. COMPLEXITY PROOF: WHY O(n), NOT O(n * k)
 * ================================================================
 *
 * The code contains nested while loops, but an index can enter the deque once
 * and leave it once. Once removed, that index never returns.
 *
 * Across the entire algorithm:
 *
 *     at most n additions
 *     at most n removals
 *
 * Therefore, all deque operations together cost O(n), not O(n * k).
 *
 * Time: O(n)
 * Extra space: O(k), because the deque holds at most one window of indices.
 *
 * ================================================================
 * 11. INTERVIEW ARTICULATION AND DEFENSE
 * ================================================================
 *
 * Explain the progression instead of claiming that a monotonic deque appeared
 * magically:
 *
 * 1. "The brute-force solution scans each window, costing O(n * k)."
 * 2. "Adjacent windows overlap, so I should preserve useful candidates."
 * 3. "An older value smaller than a newer value can never win again, so I can
 *    remove it permanently."
 * 4. "I keep useful indices in decreasing-value order inside a deque."
 * 5. "I remove expired indices from the front and dominated values from the
 *    back. The front is then the current maximum."
 *
 * Interview answer:
 *
 * "I use a monotonic decreasing deque of indices. Before inserting index i, I
 * remove expired indices from the front. Then I remove indices from the back
 * while their values are less than or equal to nums[i], because the new value
 * is larger and will live longer. After adding i, the front is the maximum for
 * the current window. Each index enters and leaves once, so the solution is
 * O(n) time and O(k) space."
 */
public class SlidingWindowMaximum {

    /*
     * CONSISTENT INTERVIEW SOLVING CONTRACT
     * -------------------------------------
     * 1. PROBLEM     - restate inputs, output, constraints, and examples.
     * 2. BASELINE    - give the simplest correct solution and its cost.
     * 3. RECOGNIZE   - fixed moving window needs a reusable maximum.
     * 4. INVARIANT   - deque holds in-window indices in decreasing value order.
     * 5. TRAPS       - expire front, remove dominated back, store indices.
     * 6. OPTIMAL     - derive every deque operation from the invariant.
     * 7. DEFEND      - each index enters/leaves once: O(n) time, O(k) space.
     */

    /**
     * LeetCode-compatible entry point.
     *
     * <p>Keeping this method on the public class makes the file easy to submit
     * and preserves every Markdown link and test already pointing here. The
     * actual interview-preferred implementation lives in {@link Optimal} so
     * the baseline-to-optimal progression remains explicit.</p>
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        return new Optimal().maxSlidingWindow(nums, k);
    }

    /**
     * Baseline solution used to derive and verify the optimization.
     *
     * <p>For every valid window, scan all {@code k} elements again.</p>
     *
     * <p>Time: O(n * k). Extra space: O(1), excluding the result.</p>
     */
    static class BruteForce {

        public int[] maxSlidingWindow(int[] nums, int k) {
            if (hasInvalidInput(nums, k)) {
                return new int[0];
            }

            int[] result = new int[nums.length - k + 1];

            for (int left = 0; left + k <= nums.length; left++) {
                int maximum = nums[left];

                for (int index = left + 1; index < left + k; index++) {
                    maximum = Math.max(maximum, nums[index]);
                }

                result[left] = maximum;
            }

            return result;
        }
    }

    /**
     * Interview-preferred monotonic-deque solution.
     *
     * <p>Core invariant after processing index {@code right}:</p>
     *
     * <ol>
     *   <li>Every stored index is inside the active window.</li>
     *   <li>Indices increase from front to back.</li>
     *   <li>Corresponding values decrease from front to back.</li>
     *   <li>The front therefore identifies the active window's maximum.</li>
     * </ol>
     *
     * <p>Time: O(n), because each index enters once and leaves at most once.
     * Extra space: O(k), excluding the result.</p>
     */
    static class Optimal {

        public int[] maxSlidingWindow(int[] nums, int k) {
            // Defensive handling outside LeetCode's guaranteed valid constraints.
            if (hasInvalidInput(nums, k)) {
                return new int[0];
            }

            // An array of length n contains exactly n - k + 1 windows of size k.
            int[] result = new int[nums.length - k + 1];

            // Stores useful indices. Their values decrease from front to back.
            Deque<Integer> decreasingIndices = new ArrayDeque<>();

            for (int right = 0; right < nums.length; right++) {
                // RULE 1: expire indices that are left of the active window.
                while (!decreasingIndices.isEmpty()
                        && decreasingIndices.peekFirst() <= right - k) {
                    decreasingIndices.pollFirst();
                }

                // RULE 2: remove older candidates dominated by the new value.
                //make sure to use <= instead of < to remove equal values as well, so that we always keep the most recent maximum in the deque
                //don't forget to use peekLast() instead of peekFirst() to check the last element in the deque
                //we are comparing nums here both side not indices, so we need to use nums[decreasingIndices.peekLast()] instead of decreasingIndices.peekLast()
                while (!decreasingIndices.isEmpty()
                        && nums[decreasingIndices.peekLast()] <= nums[right]) {
                    decreasingIndices.pollLast();
                }

                // RULE 3: add the new index as the newest useful candidate.
                decreasingIndices.addLast(right);

                // RULE 4: emit only after the first complete window exists.
                if (right >= k - 1) {
                    result[right - k + 1] = nums[decreasingIndices.peekFirst()];
                }
            }

            return result;
        }
    }

    private static boolean hasInvalidInput(int[] nums, int k) {
        return nums == null || nums.length == 0 || k <= 0 || k > nums.length;
    }

    /*
     * ========================================================================
     * 12. LEARNING VERIFICATION + TRANSFER
     * ========================================================================
     *
     * Recall without looking at code:
     * - Window membership: valid indices are right - k + 1 through right.
     * - Deque order: indices increase; corresponding values decrease.
     * - Front meaning: maximum of the current complete window.
     * - Front removal reason: expired.
     * - Back removal reason: dominated by a newer, greater-or-equal value.
     *
     * Mutation readiness:
     * - Sliding Window Minimum: reverse the value comparison at the back.
     * - Keep oldest equal maximum: remove only strictly smaller values.
     * - Return maximum indices: emit peekFirst() instead of nums[peekFirst()].
     *
     * Pattern boundary:
     * - A frequency/contribution window tracks all active items.
     * - This monotonic deque intentionally forgets active items that can never
     *   become the best candidate.
     */

    /*
     * ========================================================================
     * 13. MAIN METHOD + SELF-VERIFYING EXAMPLES
     * ========================================================================
     */
    public static void main(String[] args) {
        BruteForce baseline = new BruteForce();
        Optimal solver = new Optimal();

        assertArrayEquals(
                new int[]{3, 3, 5, 5, 6, 7},
                solver.maxSlidingWindow(
                        new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3),
                "official example");
        assertArrayEquals(
                new int[]{4, 4},
                solver.maxSlidingWindow(new int[]{4, 4, 2}, 2),
                "duplicate maximums");
        assertArrayEquals(
                baseline.maxSlidingWindow(new int[]{-4, -2, -5}, 2),
                solver.maxSlidingWindow(new int[]{-4, -2, -5}, 2),
                "baseline and optimal agree for negatives");

        System.out.println("ALL SLIDING WINDOW MAXIMUM CHECKS PASSED");
    }

    private static void assertArrayEquals(
            int[] expected,
            int[] actual,
            String reason) {

        if (!java.util.Arrays.equals(expected, actual)) {
            throw new IllegalStateException(
                    "Failed: " + reason
                            + " expected=" + java.util.Arrays.toString(expected)
                            + " actual=" + java.util.Arrays.toString(actual));
        }
    }

    /*
     * ========================================================================
     * 14. CHAPTER COMPLETION CHECKLIST
     * ========================================================================
     *
     * Recognition:
     * Fixed moving window + repeated max/min query -> monotonic deque.
     *
     * Invariant:
     * In-window indices increase while their values decrease; front is max.
     *
     * Discard proof:
     * An older smaller/equal value loses to the newer value now and expires
     * sooner, so it can never become a future maximum.
     *
     * Termination:
     * right increases once per input value; removed indices never return.
     *
     * Complexity:
     * O(n) time and O(k) auxiliary space.
     *
     * Final reconstruction phrase:
     * EXPIRE FRONT -> DOMINATE BACK -> ADD RIGHT -> EMIT FRONT.
     */
}
