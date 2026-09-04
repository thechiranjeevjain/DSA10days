package org.chijai.day1.Arrays.session2;

import java.util.Arrays;

/**
 * =====================================================================================
 * CONTAINER WITH MOST WATER — OPPOSITE-END TWO POINTERS
 * =====================================================================================
 *
 * Primary recognition:
 *
 *      BEST ENDPOINT PAIR
 *      +
 *      SAFE DISCARD PROOF
 *          -> TWO POINTERS
 *
 * Core motto:
 *
 *      "Discard an endpoint only when you can prove
 *       every future pair using it is dominated."
 *
 * =====================================================================================
 */
public class ContainerWithMostWater {

    /*
     * =================================================================================
     * 1️⃣ PROBLEM STATEMENT
     * =================================================================================
     *
     * Given height[], choose two indices:
     *
     *      left < right
     *
     * Area:
     *
     *      width
     *          = right - left
     *
     *      limitingHeight
     *          = min(height[left], height[right])
     *
     *      area
     *          = width * limitingHeight
     *
     * Return the maximum possible area.
     *
     *
     * Example:
     *
     *      [1,8,6,2,5,4,8,3,7]
     *
     * answer:
     *
     *      49
     *
     *
     * Mental image:
     *
     *      LEFT ENDPOINT                    RIGHT ENDPOINT
     *
     *            █~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~█
     *            █~~~~ interior irrelevant ~~~~~█
     *            █~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~█
     *
     * Only:
     *
     *      endpoint heights
     *      +
     *      distance between them
     *
     * determine the area.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 2️⃣ HOW THE BRAIN SHOULD SEE IT
     * =================================================================================
     *
     * Do NOT begin with:
     *
     *      "Container -> Two Pointers."
     *
     * Begin with:
     *
     *      "What exactly am I choosing?"
     *
     * Here:
     *
     *      exactly TWO endpoints.
     *
     *
     * Then ask:
     *
     *      "What determines the score of this pair?"
     *
     *      area
     *          = distance
     *          *
     *          shorter endpoint
     *
     *
     * Key observation:
     *
     *      width always DECREASES when pointers move inward.
     *
     * Therefore:
     *
     *      after moving inward,
     *      area can improve only if the limiting height improves enough.
     *
     *
     * The SHORTER endpoint is the bottleneck.
     *
     * That immediately suggests the real question:
     *
     *      "Can I prove the shorter endpoint is exhausted forever?"
     *
     * If yes:
     *
     *      discard it.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 3️⃣ UNSEEN-PROBLEM DECODER — SEE TWO POINTERS BEFORE CODING
     * =================================================================================
     *
     * For a random problem, ask:
     *
     *
     * 1. AM I choosing a PAIR of ordered endpoints?
     *
     *      yes / no
     *
     *
     * 2. DOES the score depend on both endpoints together?
     *
     *      yes / no
     *
     *
     * 3. DOES moving inward monotonically reduce one resource?
     *
     * Here:
     *
     *      width always decreases.
     *
     *
     * 4. CAN I prove one endpoint can NEVER help again?
     *
     * Here:
     *
     *      yes.
     *
     *      the shorter endpoint has already been tested
     *      with its widest possible partner.
     *
     *
     * 5. DOES the interior matter?
     *
     * Here:
     *
     *      NO.
     *
     *
     * If:
     *
     *      pair
     *      +
     *      monotonic shrink
     *      +
     *      safe endpoint discard
     *
     * then opposite-end two pointers should be strongly suspected.
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
     *                    | alternate valley model   | Trapping Rain Water           | Trapping Rain Water
     *
     * -------------------------------------------------------------------------------------------------------------
     *
     * BEST ENDPOINT PAIR| ❌ nearest irrelevant    | ❌ side summaries insufficient | ✅ exact fit
     *                    |                          |                                | Container
     *
     * =============================================================================================================
     *
     *
     * LOCK:
     *
     *      NEAREST -> MONOTONIC STACK
     *
     *      BEST    -> PREFIX / SUFFIX
     *
     *      PAIR    -> TWO POINTERS
     *
     *
     * Deepest interior discriminator:
     *
     *      Largest Rectangle:
     *          interior = CONSTRAINT
     *
     *      Trapping Rain Water:
     *          interior = CONTRIBUTION / VALLEY
     *
     *      Container:
     *          interior = IRRELEVANT
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 5️⃣ CORE INVARIANT + DISCARD PROOF
     * =================================================================================
     *
     * Suppose:
     *
     *      height[left] <= height[right]
     *
     * Current pair:
     *
     *      area
     *          = (right - left) * height[left]
     *
     *
     * Now keep the SAME left endpoint
     * and move right inward:
     *
     *      (left, right - 1)
     *      (left, right - 2)
     *      ...
     *
     *
     * Every such future pair has:
     *
     *      smaller width
     *
     * AND:
     *
     *      limitingHeight <= height[left]
     *
     *
     * Therefore:
     *
     *      futureArea
     *          <= smallerWidth * height[left]
     *
     *          < currentWidth * height[left]
     *
     *
     * So this left endpoint has ALREADY produced
     * the best possible area it can ever produce.
     *
     * It is:
     *
     *      EXHAUSTED FOREVER.
     *
     *
     * Therefore:
     *
     *      left++
     *
     * is safe.
     *
     *
     * Symmetrically:
     *
     *      if height[right] < height[left]
     *
     *      right--
     *
     *
     * CORE INVARIANT:
     *
     *      Every discarded endpoint has already been evaluated
     *      in the best-width situation it could ever have,
     *      so no discarded endpoint can belong to a better
     *      remaining solution.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 6️⃣ HOW SOMEONE COULD INVENT THIS
     * =================================================================================
     *
     * Do NOT try to invent:
     *
     *      while (left < right) { ... }
     *
     * directly.
     *
     *
     * -------------------------------------------------------------------------
     * STEP 1 — BRUTE FORCE PAIR SEARCH
     * -------------------------------------------------------------------------
     *
     * Natural first attempt:
     *
     *      try every pair (L, R)
     *
     * Number of pairs:
     *
     *      O(n^2)
     *
     *
     * -------------------------------------------------------------------------
     * STEP 2 — ASK WHAT MAKES A PAIR GOOD
     * -------------------------------------------------------------------------
     *
     *      area
     *          = width * limitingHeight
     *
     * Start with maximum possible width:
     *
     *      left = 0
     *      right = n - 1
     *
     *
     * -------------------------------------------------------------------------
     * STEP 3 — WIDTH CAN ONLY GET WORSE
     * -------------------------------------------------------------------------
     *
     * Once either pointer moves inward:
     *
     *      width decreases.
     *
     * Therefore:
     *
     *      improvement requires a better limiting height.
     *
     *
     * -------------------------------------------------------------------------
     * STEP 4 — IDENTIFY THE BOTTLENECK
     * -------------------------------------------------------------------------
     *
     *      limitingHeight
     *          = min(height[left], height[right])
     *
     * The shorter endpoint is the bottleneck.
     *
     *
     * -------------------------------------------------------------------------
     * STEP 5 — PROVE ONE WHOLE SET OF PAIRS IS DOMINATED
     * -------------------------------------------------------------------------
     *
     * If LEFT is shorter:
     *
     *      keeping LEFT
     *      +
     *      shrinking width
     *
     * can never improve the current area.
     *
     * Therefore every remaining pair using LEFT is useless.
     *
     * Discard LEFT.
     *
     *
     * -------------------------------------------------------------------------
     * STEP 6 — REPEAT
     * -------------------------------------------------------------------------
     *
     * Exactly one endpoint disappears each iteration.
     *
     *      n endpoints
     *          -> O(n) iterations
     *
     *
     * GENERAL INVENTION TEMPLATE:
     *
     *      brute-force search space
     *          ↓
     *      identify monotonic resource loss
     *          ↓
     *      identify bottleneck
     *          ↓
     *      prove one candidate class is dominated
     *          ↓
     *      discard safely
     *
     * =================================================================================
     */


    /**
     * =================================================================================
     * 7️⃣ REUSABLE OPPOSITE-END TWO-POINTER SKELETON
     * =================================================================================
     *
     * int left = 0;
     * int right = n - 1;
     *
     * while (left < right) {
     *
     *     evaluate(left, right);
     *
     *     if (left endpoint is provably exhausted) {
     *         left++;
     *     } else {
     *         right--;
     *     }
     * }
     *
     *
     * Customize:
     *
     *      1. pair score
     *
     *      2. bottleneck / ordering relation
     *
     *      3. proof for discarding one endpoint
     *
     *
     * Never use this skeleton unless:
     *
     *      you can state WHY the discarded endpoint
     *      cannot participate in a better remaining answer.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 8️⃣ PRIMARY IMPLEMENTATION
     * =================================================================================
     */
    static final class Optimal {

        static int maxArea(int[] height) {

            if (height == null || height.length < 2) {
                return 0;
            }

            int left = 0;
            int right = height.length - 1;

            int best = 0;

            while (left < right) {

                int width =
                        right - left;

                int limitingHeight =
                        Math.min(
                                height[left],
                                height[right]
                        );

                int area =
                        width * limitingHeight;

                best =
                        Math.max(best, area);

                if (height[left] <= height[right]) {
                    left++;
                } else {
                    right--;
                }
            }

            return best;
        }
    }


    /*
     * =================================================================================
     * 9️⃣ FULL STATE-EVOLUTION DRY RUN
     * =================================================================================
     *
     * height:
     *
     *      [1,8,6,2,5,4,8,3,7]
     *
     * =========================================================================================
     * L | R | h[L] | h[R] | width | limit | area | best | discard
     * =========================================================================================
     * 0 | 8 |  1   |  7   |   8   |   1   |   8  |   8  | LEFT
     * 1 | 8 |  8   |  7   |   7   |   7   |  49  |  49  | RIGHT
     * 1 | 7 |  8   |  3   |   6   |   3   |  18  |  49  | RIGHT
     * 1 | 6 |  8   |  8   |   5   |   8   |  40  |  49  | LEFT*
     * 2 | 6 |  6   |  8   |   4   |   6   |  24  |  49  | LEFT
     * 3 | 6 |  2   |  8   |   3   |   2   |   6  |  49  | LEFT
     * 4 | 6 |  5   |  8   |   2   |   5   |  10  |  49  | LEFT
     * 5 | 6 |  4   |  8   |   1   |   4   |   4  |  49  | LEFT
     * =========================================================================================
     *
     * * Equal heights:
     *
     *      either endpoint may be discarded.
     *
     * Final:
     *
     *      49
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 🔟 FOCUSED HARD PART — WHY MOVE THE SHORTER WALL?
     * =================================================================================
     *
     * Suppose:
     *
     *      L height = 4
     *      R height = 9
     *
     * Current:
     *
     *      area = width * 4
     *
     *
     * If you move R inward but keep L:
     *
     *      width becomes smaller
     *
     * while:
     *
     *      limiting height can NEVER exceed 4
     *
     * because L is still only 4.
     *
     *
     * So:
     *
     *      smaller width
     *      *
     *      no better limiting height
     *
     * can never beat the current pair using L.
     *
     *
     * Therefore:
     *
     *      do NOT move the taller wall.
     *
     *      discard the shorter wall.
     *
     *
     * RECALL:
     *
     *      WIDTH WILL ONLY SHRINK.
     *
     *      SO CHANGE THE BOTTLENECK.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣1️⃣ CONTAINER vs RAIN vs LARGEST RECTANGLE
     * =================================================================================
     *
     * CONTAINER:
     *
     *      "Which TWO walls should I choose?"
     *
     *      interior = IRRELEVANT
     *
     *      PAIR + discard proof
     *          -> TWO POINTERS
     *
     *
     * TRAPPING RAIN WATER:
     *
     *      "How much can sit above EVERY position?"
     *
     *      interior = CONTRIBUTION / VALLEY
     *
     *      BEST support on both sides
     *          -> PREFIX / SUFFIX
     *
     *      two pointers are later space optimization.
     *
     *
     * LARGEST RECTANGLE:
     *
     *      "How far can THIS chosen height survive?"
     *
     *      interior = CONSTRAINT
     *
     *      NEAREST smaller blocker
     *          -> MONOTONIC STACK
     *
     *
     * Ultra-short:
     *
     *      RECTANGLE -> NEAREST
     *
     *      RAIN      -> BEST
     *
     *      CONTAINER -> PAIR
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣2️⃣ WHY THIS IS NOT BINARY SEARCH / SLIDING WINDOW
     * =================================================================================
     *
     * BINARY SEARCH:
     *
     *      discard a half using sorted order
     *      or a monotonic predicate.
     *
     * CONTAINER:
     *
     *      discard ONE endpoint using dominance
     *      from the objective formula.
     *
     *
     * SLIDING WINDOW:
     *
     *      usually maintains a valid contiguous window
     *      while expanding/shrinking around a window condition.
     *
     * CONTAINER:
     *
     *      does NOT maintain all elements between pointers as a window state.
     *
     *      interior elements are irrelevant.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣3️⃣ ±Δ — HORIZONTAL MASTERY
     * =================================================================================
     *
     * +Δ SAME FAMILY:
     *
     *      pair score changes
     *
     * but still:
     *
     *      opposite ends
     *      +
     *      one endpoint can be safely discarded
     *
     *      -> two pointers may survive.
     *
     *
     * -------------------------------------------------------------------------
     * -Δ: INTERIOR BECOMES A CONSTRAINT
     * -------------------------------------------------------------------------
     *
     * Change:
     *
     *      interior irrelevant
     *
     * to:
     *
     *      every interior value must satisfy threshold H
     *
     * Result:
     *
     *      endpoint-only discard proof may die.
     *
     *      range / nearest-boundary reasoning becomes relevant.
     *
     *
     * -------------------------------------------------------------------------
     * -Δ: ONE BEST PAIR -> EVERY POSITION CONTRIBUTES
     * -------------------------------------------------------------------------
     *
     * Change:
     *
     *      choose one pair
     *
     * to:
     *
     *      compute bounded contribution at every index
     *
     * Result:
     *
     *      becomes Rain-like.
     *
     *
     * -------------------------------------------------------------------------
     * -Δ: OPTIMUM ONLY -> ENUMERATE ALL VALID PAIRS
     * -------------------------------------------------------------------------
     *
     * The discard proof preserves:
     *
     *      the optimum.
     *
     * It does NOT preserve:
     *
     *      every possible pair.
     *
     * Therefore:
     *
     *      counting/listing all pairs
     *      may require a different method.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣4️⃣ COMPLEXITY — DERIVE, DON'T MEMORIZE
     * =================================================================================
     *
     * Time:
     *
     *      left only moves right.
     *
     *      right only moves left.
     *
     *      exactly one pointer moves each iteration.
     *
     * Therefore:
     *
     *      at most O(n) pointer movements.
     *
     *      Time = O(n)
     *
     *
     * Space:
     *
     *      only:
     *
     *          left
     *          right
     *          width
     *          limitingHeight
     *          area
     *          best
     *
     *      Space = O(1)
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣5️⃣ 30-SECOND RECONSTRUCTION + INTERVIEW ARTICULATION
     * =================================================================================
     *
     * Reconstruction:
     *
     *      choose TWO endpoints
     *
     *      area
     *          = width * min(leftHeight, rightHeight)
     *
     *      start with maximum width
     *
     *      width only decreases
     *
     *      so improvement needs a better limiting height
     *
     *      shorter endpoint is limiting
     *
     *      prove every future pair keeping it is worse
     *
     *      discard shorter endpoint
     *
     *      repeat
     *
     *
     * Code shape:
     *
     *      EVALUATE PAIR
     *          -> DISCARD BOTTLENECK END
     *
     *
     * Interview proof:
     *
     *      "If the left wall is shorter, every future pair that keeps
     *       that same left wall has smaller width while its limiting
     *       height cannot exceed the left wall. So none can beat the
     *       current pair using that left endpoint, which makes left
     *       safe to discard. The right side is symmetric."
     *
     * =================================================================================
     */


    // =================================================================================
    // 1️⃣6️⃣ SELF-VERIFYING TESTS
    // =================================================================================

    private static void assertEquals(
            int expected,
            int actual,
            String reason) {

        if (expected != actual) {
            throw new AssertionError(
                    reason
                            + "\nExpected: " + expected
                            + "\nActual:   " + actual
            );
        }
    }


    /*
     * Test oracle only.
     *
     * This is intentionally NOT presented as a second study implementation.
     * It exists to cross-check the optimal solution on regression cases.
     */
    private static int bruteMaxArea(int[] height) {

        int best = 0;

        for (int left = 0;
             left < height.length - 1;
             left++) {

            for (int right = left + 1;
                 right < height.length;
                 right++) {

                int area =
                        (right - left)
                        * Math.min(
                                height[left],
                                height[right]
                        );

                best =
                        Math.max(best, area);
            }
        }

        return best;
    }


    private static void assertMatchesBruteForce(
            int[] height) {

        int brute =
                bruteMaxArea(height);

        int optimal =
                Optimal.maxArea(height);

        if (brute != optimal) {
            throw new AssertionError(
                    "Mismatch for "
                            + Arrays.toString(height)
                            + "\nBrute:   " + brute
                            + "\nOptimal: " + optimal
            );
        }
    }


    public static void main(String[] args) {

        assertEquals(
                49,
                Optimal.maxArea(
                        new int[]{
                                1,8,6,2,5,4,8,3,7
                        }
                ),
                "classic example"
        );

        assertEquals(
                1,
                Optimal.maxArea(
                        new int[]{1,1}
                ),
                "minimum input"
        );

        assertEquals(
                6,
                Optimal.maxArea(
                        new int[]{1,2,3,4,5}
                ),
                "increasing heights"
        );

        assertEquals(
                6,
                Optimal.maxArea(
                        new int[]{5,4,3,2,1}
                ),
                "decreasing heights"
        );

        assertEquals(
                400,
                Optimal.maxArea(
                        new int[]{100,1,1,1,100}
                ),
                "tall endpoints"
        );

        assertEquals(
                16,
                Optimal.maxArea(
                        new int[]{4,4,4,4,4}
                ),
                "equal heights"
        );

        int[][] regression = {
                {1,8,6,2,5,4,8,3,7},
                {1,1},
                {5,5},
                {1,2,3,4,5},
                {5,4,3,2,1},
                {2,3,10,5,7,8,9},
                {100,1,1,1,100},
                {4,4,4,4,4},
                {0,2,0,4,0},
                {9,1,2,3,9},
                {2,4,2,4,2},
                {6,9,3,4,5,8}
        };

        for (int[] test : regression) {
            assertMatchesBruteForce(test);
        }

        System.out.println(
                "ALL TESTS PASSED"
        );
    }
}
