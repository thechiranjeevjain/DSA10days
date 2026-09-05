package org.chijai.day3.session2.prefix.suffix;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * =====================================================================================
 * TRAPPING RAIN WATER — PREFIX / SUFFIX BOUNDARY SUMMARY
 * =====================================================================================
 * LeetCode 42 - Trapping Rain Water
 *
 * Primary recognition:
 *
 *      BEST LEFT / BEST RIGHT
 *          -> PREFIX / SUFFIX
 *
 * Approach roles:
 *
 *      Prefix / Suffix   -> RECONSTRUCTION ANCHOR
 *      Two Pointers      -> SPACE OPTIMIZATION
 *      Monotonic Deque   -> ALTERNATIVE INVARIANT
 *
 * =====================================================================================
 */
public class TrappingRainwater {

    /*
     * =================================================================================
     * 1️⃣ PROBLEM STATEMENT
     * =================================================================================
     *
     * Given non-negative bar heights, compute how much rain water is trapped.
     *
     * Example:
     *
     *      height = [4,2,0,3,2,5]
     *      answer = 9
     *
     * At any index i:
     *
     *      waterLevel[i]
     *          = min(maxLeft[i], maxRight[i])
     *
     *      waterAtI
     *          = waterLevel[i] - height[i]
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 2️⃣ HOW THE BRAIN SHOULD SEE IT
     * =================================================================================
     *
     * Ask:
     *
     *      "What information does ONE position need?"
     *
     * It needs:
     *
     *      BEST / TALLEST wall anywhere on the LEFT
     *      BEST / TALLEST wall anywhere on the RIGHT
     *
     * Not:
     *
     *      nearest wall
     *
     * That phrase:
     *
     *      BEST over an entire side
     *
     * is the Prefix / Suffix signal.
     *
     *
     * Mental image:
     *
     *      BEST LEFT WALL                 BEST RIGHT WALL
     *            █~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~█
     *            █~~~~~~~~~~~ water ~~~~~~~~~~~~~█
     *            █~~~~~~~~~~~~~~█~~~~~~~~~~~~~~~~█
     *                           ↑
     *                        current
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 3️⃣ UNSEEN-PROBLEM DECODER
     * =================================================================================
     *
     * For a random problem, ask:
     *
     *      WHAT does current need from the LEFT?
     *          nearest / best / aggregate?
     *
     *      WHAT does current need from the RIGHT?
     *          nearest / best / aggregate?
     *
     *      CAN all raw history on one side be compressed
     *      into one summary?
     *
     * Here:
     *
     *      LEFT  -> maximum
     *      RIGHT -> maximum
     *
     * Therefore:
     *
     *      BEST LEFT  -> prefix summary
     *      BEST RIGHT -> suffix summary
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 4️⃣ 3 × 3 ANTI-CONFUSION MATRIX
     * =================================================================================
     *
     * =============================================================================================================
     * ASK ↓ / TOOL →    | MONOTONIC STACK          | PREFIX / SUFFIX               | TWO POINTERS
     * =============================================================================================================
     *
     * NEAREST BLOCKER   | ✅ exact fit             | ❌ stores BEST, not nearest    | ❌ no endpoint discard proof
     *                    | Largest Rectangle        |                               |
     *
     * -------------------------------------------------------------------------------------------------------------
     *
     * BEST SIDE SUPPORT | ⚠️ possible with         | ✅ exact fit                   | ✅ space optimization
     *                    | valley-closure invariant | Trapping Rain Water           | Trapping Rain Water
     *
     * -------------------------------------------------------------------------------------------------------------
     *
     * BEST ENDPOINT PAIR| ❌ nearest irrelevant    | ❌ independent summaries       | ✅ exact fit
     *                    |                          |    insufficient                | Container
     *
     * =============================================================================================================
     *
     * Lock:
     *
     *      NEAREST -> MONOTONIC STACK
     *      BEST    -> PREFIX / SUFFIX
     *      PAIR    -> TWO POINTERS
     *
     *
     * Interior meaning:
     *
     *      Largest Rectangle -> interior = CONSTRAINT
     *      Rain              -> interior = CONTRIBUTION / VALLEY
     *      Container         -> interior = IRRELEVANT
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 5️⃣ MENTAL MODEL + CORE INVARIANT
     * =================================================================================
     *
     * maxLeft[i]
     *      = tallest bar from 0 ... i
     *
     * maxRight[i]
     *      = tallest bar from i ... n-1
     *
     * boundedHeight
     *      = min(maxLeft[i], maxRight[i])
     *
     * waterAtI
     *      = boundedHeight - height[i]
     *
     *
     * Because maxima include current:
     *
     *      maxLeft[i]  >= height[i]
     *      maxRight[i] >= height[i]
     *
     * so contribution is never negative.
     *
     * =================================================================================
     */


    /**
     * =================================================================================
     * 6️⃣ REUSABLE PREFIX / SUFFIX SKELETON
     * =================================================================================
     *
     * leftBest[0] = nums[0];
     *
     * for (int i = 1; i < n; i++) {
     *     leftBest[i] =
     *         combine(leftBest[i - 1], nums[i]);
     * }
     *
     *
     * rightBest[n - 1] = nums[n - 1];
     *
     * for (int i = n - 2; i >= 0; i--) {
     *     rightBest[i] =
     *         combine(rightBest[i + 1], nums[i]);
     * }
     *
     *
     * for (int i = 0; i < n; i++) {
     *     answer += contribution(
     *         leftBest[i],
     *         rightBest[i],
     *         nums[i]
     *     );
     * }
     *
     *
     * THIS PROBLEM:
     *
     *      left summary  -> maximum
     *      right summary -> maximum
     *      combine       -> minimum
     *      contribution  -> boundedHeight - height[i]
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 7️⃣ PREFIX / SUFFIX — RECONSTRUCTION ANCHOR
     * =================================================================================
     *
     * Time  = O(n)
     * Space = O(n)
     *
     * This is the safest version to re-derive from first principles.
     * =================================================================================
     */
    static class PrefixSuffixSolution {

        public int trap(int[] height) {

            int n = height.length;

            if (n == 0) {
                return 0;
            }

            int[] maxLeft = new int[n];
            int[] maxRight = new int[n];

            maxLeft[0] = height[0];

            for (int i = 1; i < n; i++) {
                maxLeft[i] =
                        Math.max(maxLeft[i - 1], height[i]);
            }

            maxRight[n - 1] = height[n - 1];

            for (int i = n - 2; i >= 0; i--) {
                maxRight[i] =
                        Math.max(maxRight[i + 1], height[i]);
            }

            int totalWater = 0;

            for (int i = 0; i < n; i++) {

                int boundedHeight =
                        Math.min(maxLeft[i], maxRight[i]);

                totalWater += boundedHeight - height[i];
            }

            return totalWater;
        }
    }


    /*
     * =================================================================================
     * ⭐ HOW SOMEONE COULD INVENT THE TWO-POINTER SOLUTION
     * =================================================================================
     *
     * Do NOT try to invent this loop directly:
     *
     *      while (left <= right) { ... }
     *
     * Invent it by COMPRESSING the obvious solution.
     *
     *
     * -------------------------------------------------------------------------
     * STEP 1 — START FROM THE UNDENIABLE PHYSICS
     * -------------------------------------------------------------------------
     *
     * For one index i:
     *
     *      waterAtI
     *          = min(bestLeft, bestRight) - height[i]
     *
     * This immediately gives the easy O(n) solution:
     *
     *      maxLeft[]
     *      maxRight[]
     *
     *
     * -------------------------------------------------------------------------
     * STEP 2 — ASK THE OPTIMIZATION QUESTION
     * -------------------------------------------------------------------------
     *
     * Prefix/Suffix stores:
     *
     *      maxLeft for EVERY index
     *      maxRight for EVERY index
     *
     * But while processing one side,
     * do we really need every stored value?
     *
     * Maybe not.
     *
     * Ask:
     *
     *      "Can I process positions in an order where
     *       one side is already safe to finalize?"
     *
     *
     * -------------------------------------------------------------------------
     * STEP 3 — PUT ONE POINTER AT EACH END
     * -------------------------------------------------------------------------
     *
     *      L                               R
     *      ↓                               ↓
     *
     *      [ ... heights ... ]
     *
     * The endpoints give two CERTIFIED walls immediately.
     *
     *
     * -------------------------------------------------------------------------
     * STEP 4 — FIND THE IRREVERSIBLE SIDE
     * -------------------------------------------------------------------------
     *
     * Suppose:
     *
     *      height[left] <= height[right]
     *
     * Then there already exists a wall on the right
     * at least as tall as height[left].
     *
     * So LEFT is not waiting for proof that
     * enough right-side support exists.
     *
     * The only remaining information LEFT needs is:
     *
     *      tallest wall seen so far on the LEFT
     *
     *      maxLeft
     *
     * Therefore LEFT is safe to finalize.
     *
     *
     * Symmetrically:
     *
     *      height[left] > height[right]
     *
     * means RIGHT is safe to finalize using maxRight.
     *
     *
     * -------------------------------------------------------------------------
     * STEP 5 — COMPRESS THE ARRAYS
     * -------------------------------------------------------------------------
     *
     * Prefix/Suffix:
     *
     *      maxLeft[0 ... n-1]
     *      maxRight[0 ... n-1]
     *
     * Two Pointers:
     *
     *      maxLeft
     *      maxRight
     *
     * We replaced:
     *
     *      "know everything first"
     *
     * with:
     *
     *      "know enough to finalize one position permanently."
     *
     *
     * -------------------------------------------------------------------------
     * THE GENERAL INVENTION TEMPLATE
     * -------------------------------------------------------------------------
     *
     * When an obvious solution stores global information:
     *
     *      1. What information am I storing?
     *
     *      2. Am I storing more than the current answer needs?
     *
     *      3. Can some index/candidate be finalized
     *         before all information is known?
     *
     *      4. What inequality proves future information
     *         cannot change that answer?
     *
     *
     * For Rain Water:
     *
     *      stored information
     *          = bestLeft[] + bestRight[]
     *
     *      over-storage
     *          = yes
     *
     *      safe side
     *          = smaller current boundary
     *
     *      proof
     *          = opposite side already provides sufficient support
     *
     *
     * RECALL:
     *
     *      OBVIOUS GLOBAL STATE
     *          ↓
     *      FIND WHAT CAN BE FINALIZED EARLY
     *          ↓
     *      KEEP ONLY RUNNING STATE
     *          ↓
     *      TWO POINTERS
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 8️⃣ TWO POINTERS — SAME INVARIANT, LESS SPACE
     * =================================================================================
     *
     * START FROM PREFIX / SUFFIX:
     *
     *      waterAtI
     *          = min(bestLeft, bestRight) - height[i]
     *
     * Prefix/Suffix stores BOTH best values for every index.
     *
     * Two Pointers asks:
     *
     *      "Can I finalize one side before knowing every future value?"
     *
     *
     * -------------------------------------------------------------------------
     * KEY PROOF — WHY THE SMALLER SIDE IS SAFE
     * -------------------------------------------------------------------------
     *
     * Suppose:
     *
     *      height[left] <= height[right]
     *
     * Then there already exists a wall on the RIGHT
     * at least as tall as height[left].
     *
     * So the current LEFT position does not need to wait
     * for more right-side information.
     *
     * Its limiting information is now:
     *
     *      the tallest wall seen so far on the LEFT
     *
     *      maxLeft
     *
     *
     * Therefore:
     *
     *      update maxLeft
     *      water at left = maxLeft - height[left]
     *      left++
     *
     *
     * Symmetrically:
     *
     *      if height[left] > height[right]
     *
     * then RIGHT can be finalized using maxRight.
     *
     *
     * RECALL:
     *
     *      SMALLER SIDE
     *          -> enough opposite support already exists
     *          -> finalize it
     *          -> move inward
     *
     *
     * -------------------------------------------------------------------------
     * WHY UPDATE maxLeft / maxRight FIRST?
     * -------------------------------------------------------------------------
     *
     * Example:
     *
     *      maxLeft = 3
     *      current left height = 5
     *
     * Current is a NEW boundary.
     *
     *      maxLeft = max(3,5) = 5
     *
     * then:
     *
     *      water = 5 - 5 = 0
     *
     * Correct.
     *
     *
     * If:
     *
     *      maxLeft = 5
     *      current left height = 2
     *
     * then:
     *
     *      maxLeft = 5
     *      water = 5 - 2 = 3
     *
     * So the same two lines handle both:
     *
     *      new boundary
     *      valley
     *
     *
     * -------------------------------------------------------------------------
     * MENTAL CODE SHAPE
     * -------------------------------------------------------------------------
     *
     *      while left <= right
     *
     *          if LEFT is smaller/equal
     *
     *              update maxLeft
     *              answer current LEFT
     *              left++
     *
     *          else
     *
     *              update maxRight
     *              answer current RIGHT
     *              right--
     *
     *
     * Mental verb:
     *
     *      FINALIZE SAFE SIDE -> MOVE
     *
     *
     * Time  = O(n)
     * Space = O(1)
     *
     * =================================================================================
     */
    static class TwoPointerSolution {

        public int trap(int[] height) {

            int left = 0;
            int right = height.length - 1;

            int maxLeft = 0;
            int maxRight = 0;

            int totalWater = 0;

            while (left <= right) {

                if (height[left] <= height[right]) {

                    maxLeft = Math.max(maxLeft, height[left]);

                    totalWater +=
                            maxLeft - height[left];

                    left++;

                } else {

                    maxRight = Math.max(maxRight, height[right]);

                    totalWater +=
                            maxRight - height[right];

                    right--;
                }
            }

            return totalWater;
        }
    }


    /*
     * =================================================================================
     * 8.1️⃣ TWO-POINTER DRY RUN — [4,2,0,3,2,5]
     * =================================================================================
     *
     * Start:
     *
     *      left = 0
     *      right = 5
     *      maxLeft = 0
     *      maxRight = 0
     *      water = 0
     *
     *
     * =================================================================================================
     * L  | R | h[L] | h[R] | SAFE SIDE | RUNNING MAX | WATER ADDED | TOTAL
     * =================================================================================================
     * 0  | 5 |  4   |  5   | LEFT      | maxLeft=4  | 4-4 = 0     | 0
     * 1  | 5 |  2   |  5   | LEFT      | maxLeft=4  | 4-2 = 2     | 2
     * 2  | 5 |  0   |  5   | LEFT      | maxLeft=4  | 4-0 = 4     | 6
     * 3  | 5 |  3   |  5   | LEFT      | maxLeft=4  | 4-3 = 1     | 7
     * 4  | 5 |  2   |  5   | LEFT      | maxLeft=4  | 4-2 = 2     | 9
     * 5  | 5 |  5   |  5   | LEFT      | maxLeft=5  | 5-5 = 0     | 9
     * =================================================================================================
     *
     * Final answer:
     *
     *      9
     *
     *
     * Important:
     *
     * This example happens to keep finalizing LEFT.
     *
     * In a different shape, RIGHT may be the safe side.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 9️⃣ MONOTONIC DEQUE — ALTERNATIVE VALLEY-CLOSURE INVARIANT
     * =================================================================================
     *
     * This is NOT the primary BEST-left / BEST-right derivation.
     *
     * Different mental image:
     *
     *      LEFT WALL       valley       CURRENT WALL
     *
     *          █~~~~~~~~~~~~~~~~~~~~~~~~~~~█
     *          █~~~~~~~~ water ~~~~~~~~~~~~█
     *                   █
     *                   ↑
     *                valley bottom
     *
     *
     * Current taller bar can close valleys behind it.
     *
     * Mental verb:
     *
     *      CURRENT TALLER
     *          -> POP VALLEY
     *          -> FILL BOUNDED LAYER
     *
     *
     * Deque is used as a stack:
     *
     *      push()
     *      pop()
     *      peek()
     *
     * Time  = O(n) amortized
     * Space = O(n)
     *
     * =================================================================================
     */
    static class MonotonicDequeSolution {

        public int trap(int[] height) {

            int totalWater = 0;

            Deque<Integer> stack = new ArrayDeque<>();

            for (int current = 0;
                 current < height.length;
                 current++) {

                while (!stack.isEmpty()
                        && height[current] > height[stack.peek()]) {

                    int valley = stack.pop();

                    if (stack.isEmpty()) {
                        break;
                    }

                    int left = stack.peek();

                    int width =
                            current - left - 1;

                    int boundedHeight =
                            Math.min(
                                    height[left],
                                    height[current]
                            )
                            - height[valley];

                    totalWater +=
                            width * boundedHeight;
                }

                stack.push(current);
            }

            return totalWater;
        }
    }


    /*
     * =================================================================================
     * 🔟 FULL PREFIX / SUFFIX DRY RUN
     * =================================================================================
     *
     * height:
     *
     *      [4,2,0,3,2,5]
     *
     * index:       0  1  2  3  4  5
     * height:      4  2  0  3  2  5
     * maxLeft:     4  4  4  4  4  5
     * maxRight:    5  5  5  5  5  5
     *
     *
     * ========================================================================
     * i | height | min(maxLeft,maxRight) | water
     * ========================================================================
     * 0 |   4    |          4            |   0
     * 1 |   2    |          4            |   2
     * 2 |   0    |          4            |   4
     * 3 |   3    |          4            |   1
     * 4 |   2    |          4            |   2
     * 5 |   5    |          5            |   0
     * ========================================================================
     *
     * total = 9
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣1️⃣ NEAREST vs BEST — MAIN CONFUSION TRAP
     * =================================================================================
     *
     * Rain asks:
     *
     *      TALLEST support anywhere on each side.
     *
     * Not:
     *
     *      first qualifying boundary.
     *
     *
     *      NEAREST
     *          -> monotonic-boundary thinking
     *
     *      BEST
     *          -> prefix/suffix aggregate thinking
     *
     * One word can change the primary pattern:
     *
     *      NEAREST -> BEST
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣2️⃣ THREE APPROACHES — COMPLEXITY + RETRIEVAL ROLE
     * =================================================================================
     *
     * ==================================================================================================
     * APPROACH          | STATE RETAINED                  | TIME   | SPACE | ROLE
     * ==================================================================================================
     *
     * Prefix / Suffix   | BEST wall for every index      | O(n)   | O(n)  | reconstruction anchor
     *
     * Two Pointers      | running BEST left/right        | O(n)   | O(1)  | space optimization
     *
     * Monotonic Deque   | unresolved valley boundaries   | O(n)*  | O(n)  | alternate invariant
     *
     * ==================================================================================================
     *
     * * amortized:
     *   each index is pushed once and popped at most once.
     *
     *
     * AFTER 500 MIXED PROBLEMS:
     *
     * Do NOT ask:
     *
     *      "Which of three codes do I remember?"
     *
     * Also do NOT try to recall the optimized loop first.
     *
     * Reconstruct:
     *
     *      mathematical truth
     *          -> obvious state
     *          -> identify over-stored information
     *          -> find irreversible/safe candidate
     *          -> compress state
     *
     * Ask:
     *
     *      "What does water at ONE index require?"
     *
     *          BEST LEFT
     *          BEST RIGHT
     *
     *      -> Prefix / Suffix recovered.
     *
     *
     * Then:
     *
     *      "Can I avoid storing both arrays?"
     *
     *      -> Two Pointers.
     *
     *
     * Only if needed:
     *
     *      "Can I view current as closing old valleys?"
     *
     *      -> Monotonic Deque.
     *
     *
     * Safety ladder:
     *
     *      FORGET EVERYTHING
     *          ↓
     *      min(bestLeft, bestRight) - height[i]
     *          ↓
     *      PREFIX / SUFFIX
     *          ↓
     *      optimize space
     *          ↓
     *      TWO POINTERS
     *
     *      smaller side is safe
     *          ↓
     *      update running max
     *          ↓
     *      add runningMax - currentHeight
     *          ↓
     *      move that side
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣3️⃣ STACK SEMANTICS — RAIN vs LARGEST RECTANGLE
     * =================================================================================
     *
     * Both can use a monotonic structure,
     * but POP means something different.
     *
     *
     * LARGEST RECTANGLE:
     *
     *      CURRENT SMALLER
     *          -> pop taller bar
     *          -> finalize popped bar's span
     *
     *
     * TRAPPING RAIN WATER:
     *
     *      CURRENT TALLER
     *          -> pop valley bottom
     *          -> fill bounded water layer
     *
     *
     * Recall:
     *
     *      RECTANGLE
     *          POP = FINALIZE BAR
     *
     *      RAIN
     *          POP = FILL VALLEY
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣4️⃣ ±Δ — HORIZONTAL MASTERY
     * =================================================================================
     *
     * +Δ SAME CORE:
     *
     *      BEST on each side remains required
     *      but aggregate changes:
     *
     *      max / min / sum / boolean summary
     *
     *      -> prefix/suffix may survive.
     *
     *
     * -Δ BEST -> NEAREST:
     *
     *      "tallest anywhere"
     *          becomes
     *      "first qualifying boundary"
     *
     *      -> monotonic-boundary thinking.
     *
     *
     * -Δ EVERY POSITION -> ONE PAIR:
     *
     *      "sum contribution at every position"
     *          becomes
     *      "choose two endpoints maximizing score"
     *
     *      -> pair / two-pointer reasoning.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣5️⃣ 30-SECOND RECONSTRUCTION + INTERVIEW ARTICULATION
     * =================================================================================
     *
     * Primary reconstruction:
     *
     *      water at i
     *          needs BEST wall left + BEST wall right
     *
     *      -> maxLeft[]
     *      -> maxRight[]
     *
     *      boundedHeight
     *          = min(maxLeft[i], maxRight[i])
     *
     *      water
     *          += boundedHeight - height[i]
     *
     *
     * Optimization:
     *
     *      store all BEST values
     *          -> Prefix / Suffix
     *
     *      ask:
     *          "Which side can be finalized
     *           before all future information is known?"
     *
     *      smaller side is already sufficiently supported
     *
     *      maintain BEST dynamically
     *          -> Two Pointers
     *
     *      smaller side
     *          -> update its running max
     *          -> add max - current
     *          -> move inward
     *
     *
     * Alternative:
     *
     *      current taller closes old valleys
     *          -> Monotonic Deque
     *
     *
     * Interview sentence:
     *
     *      "The reconstruction anchor is that water at each index is bounded
     *       by the shorter of the tallest wall on its left and right.
     *       Prefix/suffix stores those maxima explicitly in O(n) space.
     *       Two pointers preserves the same invariant with O(1) extra space.
     *       A monotonic deque is an alternate valley-closure interpretation."
     *
     * =================================================================================
     */


    // =================================================================================
    // 1️⃣6️⃣ SELF-VERIFYING TESTS
    // =================================================================================

    public static void main(String[] args) {

        PrefixSuffixSolution prefixSuffix =
                new PrefixSuffixSolution();

        TwoPointerSolution twoPointer =
                new TwoPointerSolution();

        MonotonicDequeSolution deque =
                new MonotonicDequeSolution();

        int[][] tests = {
                {0,1,0,2,1,0,1,3,2,1,2,1},
                {4,2,0,3,2,5},
                {5,0,1,0,2},
                {1,2,3,4,5},
                {5,4,3,2,1},
                {3,3,3,3},
                {5}
        };

        int[] expected = {
                6,
                9,
                5,
                0,
                0,
                0,
                0
        };

        for (int i = 0; i < tests.length; i++) {

            assertEquals(
                    expected[i],
                    prefixSuffix.trap(tests[i]),
                    "Prefix/Suffix test " + i
            );

            assertEquals(
                    expected[i],
                    twoPointer.trap(tests[i]),
                    "Two Pointer test " + i
            );

            assertEquals(
                    expected[i],
                    deque.trap(tests[i]),
                    "Monotonic Deque test " + i
            );
        }

        System.out.println("ALL TESTS PASSED");
    }


    private static void assertEquals(
            int expected,
            int actual,
            String name) {

        if (expected != actual) {
            throw new AssertionError(
                    name
                            + " expected=" + expected
                            + " actual=" + actual
            );
        }
    }
}
