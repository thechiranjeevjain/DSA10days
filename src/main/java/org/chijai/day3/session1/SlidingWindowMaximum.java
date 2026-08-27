package org.chijai.day3.session1;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.PriorityQueue;

/**
 * LeetCode 239 - Sliding Window Maximum
 * Version: V2 FINAL AUDITED - 2026-08-27
 *
 * Link: https://leetcode.com/problems/sliding-window-maximum/
 * Difficulty: Hard
 * Tags: Array, Queue, Sliding Window, Heap, Monotonic Queue
 *
 * Repository chapter pattern:
 * PROBLEM -> BASELINE -> RECOGNITION -> INVARIANT -> TRAPS -> FALLBACK -> OPTIMAL -> DEFEND
 *
 * ================================================================
 * 0. PATTERN TAXONOMY / WHERE THIS PROBLEM BELONGS
 * ================================================================
 *
 * PRIMARY HOME:
 *
 *     Sliding Window
 *       -> Fixed-Size Sliding Window
 *          -> Window Extremum / Repeated Max-Min Query
 *             -> Monotonic Deque
 *                -> LeetCode 239 Sliding Window Maximum
 *
 * Why this is the primary home:
 *
 *     fixed moving window
 *            +
 *     repeated maximum/minimum
 *            ↓
 *     MONOTONIC DEQUE
 *
 * SECONDARY / FALLBACK HOME:
 *
 *     Heap
 *       -> Dynamic Best Candidate
 *          -> Max Heap
 *             -> Lazy Deletion / Stale Candidates
 *
 * Important distinction:
 *
 * MONOTONIC STACK
 *     Usually: next greater/smaller, previous greater/smaller, span, contribution.
 *     Usually one-ended.
 *
 * MONOTONIC DEQUE
 *     Usually: repeated max/min over a moving bounded range.
 *     Both ends matter:
 *
 *         FRONT -> expire / read best
 *         BACK  -> remove dominated / add newest
 *
 * Do not primarily classify Sliding Window Maximum as a monotonic-stack problem.
 * The trigger is the moving window; the deque is the optimal data structure.
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
 * There are exactly n - k + 1 windows. Scanning k values per window costs
 * O((n - k + 1) * k), which is O(n * k) in the worst case.
 * With n = 100,000 and a large k, repeatedly rescanning almost the same values
 * wastes too much work.
 *
 * The key question is:
 *
 *     When the window moves one step, what old information is still useful?
 *
 * ================================================================
 * 2A. HIGH-ROI FALLBACK RECOGNITION: MAX HEAP
 * ================================================================
 *
 * Before reaching for the specialized O(n) solution, there is a very reusable
 * first-principles fallback:
 *
 *     repeatedly need the largest dynamic candidate
 *                         ↓
 *                     MAX HEAP
 *
 * For this problem the heap stores:
 *
 *     Entry(value, index)
 *
 * Why both?
 *
 *     value -> priority / maximum
 *     index -> tells us whether the entry is stale
 *
 * Core heap engine:
 *
 *     ADD current Entry(value, index)
 *     REMOVE stale heap roots
 *     PEEK root for current maximum
 *
 * The simple lazy-deletion heap solution is:
 *
 *     Time:  O(n log n)
 *     Space: O(n) worst case
 *
 * It is interview-acceptable as a reliable fallback, but the monotonic deque
 * is the canonical optimal O(n) solution for this problem.
 *
 * Heap is worth knowing because the same operating logic transfers broadly to
 * Top K, Kth largest, meeting rooms, minimum platforms, merge-k, scheduling,
 * dynamic priority, and other stale-candidate problems.
 *
 * ================================================================
 * 3. PATTERN RECOGNITION: WHY A MONOTONIC DEQUE
 * ================================================================
 *
 * A queue can tell us which value entered first and should expire first, but
 * it cannot tell us the maximum in O(1) time.
 *
 * A max heap can expose the maximum, but removing an expired value that is not
 * at the heap root is awkward. Heap operations cost O(log H), where H is the
 * current heap size. In the simple lazy-deletion fallback below, H can grow to
 * O(n), so heap operations are O(log n) in the worst case.
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
 * 6A. THE THREE EQUALITY SIGNS — DERIVE THEM, DO NOT MEMORIZE THEM
 * ================================================================
 *
 * These three conditions look similar syntactically but mean three different
 * things. Keep their meanings separate.
 *
 * ----------------------------------------------------------------
 * A. EXPIRATION
 * ----------------------------------------------------------------
 *
 *     decreasingIndices.peekFirst() <= right - k
 *
 * Equality is REQUIRED.
 *
 * Current valid window is:
 *
 *     [right - k + 1 ... right]
 *
 * Therefore right - k itself is already outside the window.
 *
 * Example: right = 3, k = 3
 *
 *     valid indices = [1, 2, 3]
 *     right - k    = 0
 *
 * Index 0 is stale, so <= is correct.
 *
 * Permanent meaning:
 *
 *     EXPIRED -> <= right - k
 *
 * ----------------------------------------------------------------
 * B. DOMINATION / REDUNDANT EQUAL VALUES
 * ----------------------------------------------------------------
 *
 *     nums[decreasingIndices.peekLast()] <= nums[right]
 *
 * Equality is OPTIONAL FOR CORRECTNESS, but <= is the cleaner canonical choice.
 *
 * If old value == new value, the newer equal value is at least as good because
 * it survives longer. So the older equal value is redundant and may be removed.
 *
 *     <   -> keep equal candidates
 *     <=  -> remove older equal candidates
 *
 * Both can produce correct window maxima. We standardize on <=.
 *
 * Permanent meaning:
 *
 *     WEAKER OR REDUNDANT -> <= current value
 *
 * ----------------------------------------------------------------
 * C. FIRST COMPLETE WINDOW
 * ----------------------------------------------------------------
 *
 *     if (right >= k - 1)
 *
 * Equality is REQUIRED.
 *
 * With k = 3, the first complete window uses indices [0, 1, 2].
 * So the first answer must be emitted exactly when right == 2 == k - 1.
 *
 * Permanent meaning:
 *
 *     WINDOW READY -> >= k - 1
 *
 * ----------------------------------------------------------------
 * ONE-LINE SIGN CHEAT SHEET
 * ----------------------------------------------------------------
 *
 *     EXPIRED?      index <= right - k
 *     DOMINATED?    oldValue <= newValue    // equality is a cleanup choice
 *     WINDOW READY? right >= k - 1
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
 * is at least as large and, being newer, expires later. After adding i, the
 * front is the maximum for
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
     * 6. FALLBACK    - max heap + Entry(value, index) + lazy stale removal.
     * 7. OPTIMAL     - derive every deque operation from the invariant.
     * 8. DEFEND      - each index enters/leaves once: O(n) time, O(k) space.
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
     * High-reuse fallback: max heap + lazy deletion.
     *
     * <p>The heap stores both value and index. Value determines priority; index
     * tells us whether the candidate has expired from the current window.</p>
     *
     * <p>Expired entries buried inside the heap are left there until they reach
     * the root. This is lazy deletion.</p>
     *
     * <p>Time: O(n log n). Extra space: O(n) worst case, excluding result.</p>
     */
    static class HeapFallback {

        record Entry(int value, int index) {
        }

        public int[] maxSlidingWindow(int[] nums, int k) {
            if (hasInvalidInput(nums, k)) {
                return new int[0];
            }

            int[] result = new int[nums.length - k + 1];

            PriorityQueue<Entry> maxHeap =
                    new PriorityQueue<>(
                            Comparator.comparingInt(Entry::value).reversed());

            for (int i = 0; i < nums.length; i++) {
                maxHeap.offer(new Entry(nums[i], i));

                // Heap root must belong to the current window.
                while (!maxHeap.isEmpty() && maxHeap.peek().index() <= i - k) {
                    maxHeap.poll();
                }

                if (i >= k - 1) {
                    result[i - k + 1] = maxHeap.peek().value();
                }
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
                // <= is REQUIRED: right - k itself is already outside the window.
                while (!decreasingIndices.isEmpty()
                        && decreasingIndices.peekFirst() <= right - k) {
                    decreasingIndices.pollFirst();
                }

                // RULE 2: remove older candidates dominated by the new value.
                // Use <= instead of < to remove equal values as well, keeping
                // the most recent equal maximum in the deque.
                // Use peekLast(), not peekFirst(), because domination cleanup
                // happens from the back.
                // Compare VALUES on both sides: nums[decreasingIndices.peekLast()]
                // versus nums[right], not the raw stored index.
                // Equality is OPTIONAL for correctness: < keeps equal values;
                // <= removes older equal values.
                // Example [3,1,1,3]: when the newer 3 arrives, the older equal
                // 3 is redundant because the newer one survives longer.
                while (!decreasingIndices.isEmpty()
                        && nums[decreasingIndices.peekLast()] <= nums[right]) {
                    decreasingIndices.pollLast();
                }

                // RULE 3: add the new index as the newest useful candidate.
                decreasingIndices.addLast(right);

                // RULE 4: emit only after the first complete window exists.
                // >= is REQUIRED: right == k - 1 is exactly the first complete window.
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
     *
     * Same / very similar bounded-best-candidate family:
     * - Sliding Window Minimum: exact mirror; reverse the back comparison.
     * - LeetCode 1696 Jump Game VI: heap/deque over dp[i] instead of nums[i].
     * - LeetCode 1425 Constrained Subsequence Sum: bounded maximum over DP.
     * - LeetCode 1499 Max Value of Equation: bounded best transformed score.
     * - LeetCode 1438 Longest Continuous Subarray With Absolute Diff <= Limit:
     *   maintain both maximum and minimum, using two heaps or two deques.
     * - LeetCode 2398 Maximum Number of Robots Within Budget: sliding window +
     *   repeated maximum charge time plus running sum.
     * - LeetCode 862 Shortest Subarray With Sum at Least K: advanced monotonic
     *   deque variation over prefix sums; not the exact same skeleton.
     *
     * Reusable heap-entry lens:
     *
     *     239  -> Entry(nums[i], index)
     *     1696 -> Entry(dp[i], index)
     *     1425 -> Entry(dp[i], index)
     *     1499 -> Entry(y - x, x/index)
     *
     * Safe-start recognition:
     *
     *     bounded/relevant candidates + repeatedly need best
     *                            ↓
     *                  Heap<Entry(score, metadata)>
     *                            ↓
     *     if weaker candidates can be permanently discarded
     *                            ↓
     *                     Monotonic Deque
     */

    /*
     * ========================================================================
     * 13. MAIN METHOD + SELF-VERIFYING EXAMPLES
     * ========================================================================
     */
    public static void main(String[] args) {
        BruteForce baseline = new BruteForce();
        HeapFallback heapFallback = new HeapFallback();
        Optimal solver = new Optimal();

        assertArrayEquals(
                new int[]{3, 3, 5, 5, 6, 7},
                solver.maxSlidingWindow(
                        new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3),
                "official example");
        assertArrayEquals(
                new int[]{3, 3, 5, 5, 6, 7},
                heapFallback.maxSlidingWindow(
                        new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3),
                "official example - heap fallback");
        assertArrayEquals(
                new int[]{1, -1},
                solver.maxSlidingWindow(new int[]{1, -1}, 1),
                "k = 1 regression");
        assertArrayEquals(
                new int[]{1, -1},
                heapFallback.maxSlidingWindow(new int[]{1, -1}, 1),
                "k = 1 regression - heap fallback");
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
     * Heap fallback:
     * Max heap Entry(value, index) + lazy stale-root removal.
     * O(n log n) time and O(n) worst-case auxiliary space.
     *
     * Sign reconstruction:
     * EXPIRED      -> index <= right - k          [equality required]
     * DOMINATED    -> oldValue <= newValue        [equality optional cleanup]
     * WINDOW READY -> right >= k - 1              [equality required]
     *
     * Final reconstruction phrase:
     * EXPIRE FRONT -> DOMINATE BACK -> ADD RIGHT -> EMIT FRONT.
     */
}
