package org.chijai.day7.session1.heap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * K Closest Points to Origin
 *
 * Reconstruction-first interview study file.
 *
 * Primary solution:
 *      Fixed-size max heap
 *
 * Secondary solution:
 *      Randomized iterative Quickselect
 *
 * Baseline:
 *      Full sort
 */
public class KClosestPointsToOrigin {

    /*
     * ============================================================
     * 1. PROBLEM + RECOGNITION
     * ============================================================
     *
     * Given:
     *
     *      points[i] = [x, y]
     *
     * return the K points closest to the origin (0, 0).
     *
     * Euclidean distance:
     *
     *      sqrt(x^2 + y^2)
     *
     * Since sqrt is monotonic for non-negative values,
     * comparing:
     *
     *      x^2 + y^2
     *
     * gives exactly the same ranking.
     *
     * The returned K points may be in ANY order.
     *
     * Example:
     *
     *      points = [[1,3], [-2,2]]
     *      k = 1
     *
     * Squared distances:
     *
     *      [1,3]   -> 10
     *      [-2,2]  -> 8
     *
     * Answer:
     *
     *      [[-2,2]]
     *
     * Recognition words:
     *
     *      K closest
     *      K smallest
     *      Top K
     *      keep only K winners
     *
     * Official problem:
     *
     *      https://leetcode.com/problems/k-closest-points-to-origin/
     *
     * ------------------------------------------------------------
     * THE 10-SECOND INTERVIEW DECISION
     * ------------------------------------------------------------
     *
     * Need only K winners?
     *
     *      YES
     *
     * Is input a stream / should I retain only K items?
     *
     *      fixed-size heap
     *
     * Is the whole mutable array already available and
     * expected linear selection is attractive?
     *
     *      Quickselect
     *
     * Need complete sorted ordering?
     *
     *      sort
     */


    /*
     * ============================================================
     * VISUAL MAP -- THE FIVE PICTURES TO REMEMBER
     * ============================================================
     *
     * If the prose disappears after six months, retain these:
     *
     *      1. WORK REDUCTION FUNNEL
     *
     *           SORT EVERYTHING
     *                 |
     *                 v
     *           KEEP ONLY K
     *                 |
     *                 v
     *           FIND K-BOUNDARY
     *
     *
     *      2. HEAP ROOT
     *
     *            WORST ACCEPTED
     *                 /\
     *                /  \
     *          better    better
     *
     *
     *      3. OFFER -> TRIM
     *
     *           K winners
     *               +
     *           newcomer
     *
     *               |
     *               v
     *
     *          K + 1 candidates
     *
     *               |
     *        remove WORST
     *               v
     *
     *           K winners
     *
     *
     *      4. QUICKSELECT TAPE
     *
     *        winners | pivot | losers
     *                ^
     *             K-boundary
     *
     *
     *      5. RETENTION HIERARCHY
     *
     *          AUTOMATIC      -> HEAP
     *          DERIVABLE      -> SORT
     *          CONCEPTUAL     -> QUICKSELECT
     *
     * The detailed diagrams below unpack these five pictures.
     */

    /*
     * ============================================================
     * 2. PRIMARY INTERVIEW SOLUTION -- READ THIS PHOTOGRAPHICALLY
     * ============================================================
     *
     * Mental skeleton:
     *
     *      RankedPoint(point, distance)
     *
     *      max heap by distance
     *
     *      offer
     *
     *      if size > K
     *          poll
     *
     *      unwrap answer
     *
     * Time:
     *
     *      O(N log K)
     *
     * Space:
     *
     *      O(K)
     */

    private record RankedPoint(
            int[] point,
            long distance) {
    }

    static final class MaxHeapSolution {

        public int[][] kClosest(int[][] points, int k) {

            PriorityQueue<RankedPoint> maxHeap =
                    new PriorityQueue<>(
                            Comparator
                                    .comparingLong(
                                            RankedPoint::distance)
                                    .reversed());

            for (int[] point : points) {

                maxHeap.offer(
                        new RankedPoint(
                                point,
                                squaredDistance(point)));

                if (maxHeap.size() > k) {
                    maxHeap.poll();
                }
            }

            int[][] answer = new int[k][2];

            for (int i = 0; i < k; i++) {
                answer[i] =
                        maxHeap.poll().point();
            }

            return answer;
        }
    }

    private static long squaredDistance(int[] point) {

        long x = point[0];
        long y = point[1];

        return x * x + y * y;
    }

    /*
     * ============================================================
     * 3. APPROACH LADDER -- WHAT PROBLEM IS EACH SOLUTION SOLVING?
     * ============================================================
     *
     * Start with the obvious solution.
     *

     * ------------------------------------------------------------
     * VISUAL -- WORK REDUCTION FUNNEL
     * ------------------------------------------------------------
     *
     * Requirement:
     *
     *      "Return K closest points."
     *
     *
     *        +---------------------------------------+
     *        | FULL SORT                             |
     *        | Order ALL N points                    |
     *        |                                       |
     *        |  p1 <= p2 <= p3 <= ... <= pN         |
     *        +---------------------------------------+
     *                         |
     *                         | remove unnecessary
     *                         | global ordering
     *                         v
     *              +-------------------------+
     *              | FIXED-SIZE HEAP         |
     *              | Retain only K winners   |
     *              |                         |
     *              | [ best K candidates ]   |
     *              +-------------------------+
     *                         |
     *                         | remove unnecessary
     *                         | ordering among K
     *                         v
     *                  +---------------+
     *                  | QUICKSELECT   |
     *                  | Find boundary |
     *                  | only          |
     *                  +---------------+
     *
     * Each step solves LESS of the ordering problem.
     *
     * That is why the asymptotic work can decrease.
     *
     * ------------------------------------------------------------
     * A. FULL SORT
     * ------------------------------------------------------------
     *
     *      compute ranking
     *      sort every point
     *      take first K
     *
     * Solves:
     *
     *      COMPLETE ORDERING
     *
     * Time:
     *
     *      O(N log N)
     *
     * ------------------------------------------------------------
     * B. FIXED-SIZE MAX HEAP
     * ------------------------------------------------------------
     *
     * Ask:
     *
     *      "Why order all N if I only need K survivors?"
     *
     * Maintain only the best K seen so far.
     *
     * Solves:
     *
     *      CONTINUOUS TOP-K MAINTENANCE
     *
     * Time:
     *
     *      O(N log K)
     *
     * Space:
     *
     *      O(K)
     *
     * ------------------------------------------------------------
     * C. QUICKSELECT
     * ------------------------------------------------------------
     *
     * Ask again:
     *
     *      "Do even the K winners need to be heap-ordered?"
     *
     * No.
     *
     * We only need a boundary separating:
     *
     *      first K winners
     *      remaining N-K points
     *
     * Solves:
     *
     *      SELECTION / BOUNDARY FINDING
     *
     * Expected time:
     *
     *      O(N)
     *
     * Worst:
     *
     *      O(N^2)
     *
     * ------------------------------------------------------------
     * PROGRESSION
     * ------------------------------------------------------------
     *
     *      SORT EVERYTHING
     *
     *          ↓ remove unnecessary global ordering
     *
     *      KEEP ONLY K WINNERS
     *
     *          ↓ remove unnecessary heap maintenance
     *
     *      FIND ONLY THE K-BOUNDARY
     *
     * This is the conceptual progression:
     *
     *      sort -> heap -> Quickselect
     */

    /*
     * ============================================================
     * 4. HOW TO INVENT THE MAX HEAP FROM FIRST PRINCIPLES
     * ============================================================
     *
     * Do NOT memorize:
     *
     *      "Top K = heap."
     *
     * Reconstruct:
     *
     * Step 1
     * ------
     *
     * What does "better" mean?
     *
     *      smaller squared distance
     *
     * Step 2
     * ------
     *
     * How many winners do I need?
     *
     *      only K
     *
     * Step 3
     * ------
     *
     * Suppose K winners are already accepted.
     *
     * A new point arrives.
     *
     * Which accepted point matters?
     *
     *      only the WORST accepted point
     *
     * because that is the candidate the newcomer must replace.
     *
     * Step 4
     * ------
     *
     * What must therefore be easy to access/remove?
     *
     *      worst accepted point
     *
     * Step 5
     * ------
     *
     * For K closest:
     *
     *      worst accepted = farthest
     *                     = largest distance
     *
     * Therefore:
     *
     *      MAX HEAP
     *
     * ------------------------------------------------------------
     * RETENTION METAPHOR
     * ------------------------------------------------------------
     *
     * Imagine a backpack with only K slots.
     *
     * The point sitting at the opening is not the BEST trophy.
     *
     * It is the weakest trophy currently allowed to remain.
     *
     * Every new point only needs to beat that boundary.
     *
     * ROOT = DISPOSAL BOUNDARY.
     */

    /*
     * ============================================================
     * 5. PRECISE HEAP INVARIANT
     * ============================================================
     *
     * After processing p points:
     *
     *      heap.size() = min(K, p)
     *
     * and the heap contains the min(K, p) closest points
     * among those p processed points.
     *
     * Once p >= K:
     *
     *      maxHeap.peek()
     *
     * is the farthest point among the accepted K.
     *

     * ------------------------------------------------------------
     * VISUAL -- ROOT IS THE DISPOSAL BOUNDARY
     * ------------------------------------------------------------
     *
     * Suppose K = 4 and accepted squared distances are:
     *
     *      3, 7, 10, 18
     *
     * Conceptual MAX HEAP:
     *
     *                    18   <- ROOT
     *                   /  \
     *                  10   7
     *                 /
     *                3
     *
     * Read the picture as:
     *
     *      18 = WORST point I am still accepting.
     *
     * The children are not required to be globally sorted.
     *
     * If newcomer distance = 20:
     *
     *                    18
     *
     * newcomer 20 is worse than the boundary.
     *
     * After offer -> trim:
     *
     *      20 gets removed.
     *
     * Survivors remain:
     *
     *      3, 7, 10, 18
     *
     *
     * If newcomer distance = 5:
     *
     *      candidates:
     *
     *          3, 5, 7, 10, 18
     *
     *      remove worst = 18
     *
     *      survivors:
     *
     *          3, 5, 7, 10
     *
     * ------------------------------------------------------------
     * VISUAL -- OFFER -> TRIM AS A STATE TRANSITION
     * ------------------------------------------------------------
     *
     * BEFORE
     *
     *      accepted K
     *
     *      [ 3 | 7 | 10 | 18 ]
     *                       ^
     *                   worst accepted
     *
     * NEWCOMER = 5
     *
     *              +
     *              5
     *
     * AFTER OFFER
     *
     *      [ 3 | 5 | 7 | 10 | 18 ]
     *                            ^
     *                         remove
     *
     * AFTER POLL
     *
     *      [ 3 | 5 | 7 | 10 ]
     *                       ^
     *                new worst accepted
     *
     * Invariant restored immediately.
     *
     * ------------------------------------------------------------
     * WHY OFFER -> TRIM WORKS
     * ------------------------------------------------------------
     *
     * Assume heap already contains the best K processed points.
     *
     * Offer a new point.
     *
     * Now there are K+1 candidates.
     *
     * Poll the maximum distance.
     *
     * You have removed exactly the worst candidate among K+1.
     *
     * Therefore the remaining K are exactly the K best points
     * among everything processed so far.
     *
     * Invariant restored.
     */

    /*
     * ============================================================
     * 6. HEAP DRY RUN
     * ============================================================
     *
     * points:
     *
     *      [[3,3], [5,-1], [-2,4], [1,1]]
     *
     * K = 2
     *
     * Distances:
     *
     *      [3,3]   -> 18
     *      [5,-1]  -> 26
     *      [-2,4]  -> 20
     *      [1,1]   -> 2
     *
     * Conceptual ROOT-FIRST heap state:
     *
     * +----------+------+--------------------+------------------+
     * | Incoming | Dist | After offer        | After trim       |
     * +----------+------+--------------------+------------------+
     * | [3,3]    | 18   | [18]               | [18]             |
     * | [5,-1]   | 26   | [26,18]            | [26,18]          |
     * | [-2,4]   | 20   | [26,18,20]         | [20,18]          |
     * | [1,1]    | 2    | [20,18,2]          | [18,2]           |
     * +----------+------+--------------------+------------------+
     *
     * Final survivor distances:
     *
     *      18, 2
     *
     * Important:
     *
     * PriorityQueue itself is NOT globally sorted.
     *
     * Only the root ordering is guaranteed.
     */

    /*
     * ============================================================
     * 7. HEAP DIRECTION -- THE GENERIC RULE
     * ============================================================
     *

     * ------------------------------------------------------------
     * VISUAL -- KEEP SIDE VS THROW-AWAY SIDE
     * ------------------------------------------------------------
     *
     *                    ALL CANDIDATES
     *
     *      smaller  <---------------------------->  larger
     *
     *
     * K SMALLEST / K CLOSEST
     *
     *      KEEP THIS SIDE
     *      <============|
     *                   ^
     *                   |
     *              largest accepted
     *              = throw-away boundary
     *              = MAX-HEAP ROOT
     *
     *
     * K LARGEST / K FARTHEST
     *
     *                   |============>
     *                   KEEP THIS SIDE
     *                   ^
     *                   |
     *              smallest accepted
     *              = throw-away boundary
     *              = MIN-HEAP ROOT
     *
     * The heap direction comes from the BOUNDARY,
     * not from the words "Top K."
     *
     * Never memorize:
     *
     *      "Top K -> max heap"
     *
     * Instead ask:
     *
     *      "Which accepted item should be easiest to throw away?"
     *
     * +-------------------+--------------------------+-----------+
     * | Goal              | Worst accepted candidate | Root      |
     * +-------------------+--------------------------+-----------+
     * | K smallest        | largest                  | max heap  |
     * | K closest         | farthest                 | max heap  |
     * | K largest         | smallest                 | min heap  |
     * | K farthest        | closest                  | min heap  |
     * +-------------------+--------------------------+-----------+
     *
     * ROOT = WORST ACCEPTED.
     */

    /*
     * ============================================================
     * 8. MIN HEAP NUANCE -- WRONG VERSION VS VALID VERSION
     * ============================================================
     *
     * Statement:
     *
     *      "A min heap cannot solve K closest."
     *
     * is TOO BROAD.
     *
     * ------------------------------------------------------------
     * FIXED-SIZE MIN HEAP OF K
     * ------------------------------------------------------------
     *
     * Wrong with the generic:
     *
     *      offer
     *      if size > K:
     *          poll
     *
     * because poll() removes the CLOSEST candidate.
     *
     * You end up preserving farther points.
     *
     * ------------------------------------------------------------
     * MIN HEAP CONTAINING ALL N POINTS
     * ------------------------------------------------------------
     *
     * Correct:
     *
     *      insert every point
     *      poll K times
     *
     * But:
     *
     *      space becomes O(N)
     *
     * and repeated PriorityQueue insertion costs:
     *
     *      O(N log N)
     *
     * followed by:
     *
     *      O(K log N)
     *
     * removals.
     *
     * So it solves the problem, but not with the desirable
     * fixed-size Top-K invariant.
     */

    /*
     * ============================================================
     * 9. PRIMARY HEAP VS GUARDED REPLACEMENT
     * ============================================================
     *
     * PRIMARY / RETAINABLE:
     *
     *      for item:
     *          offer(item)
     *          if size > K:
     *              poll()
     *
     * Advantages:
     *
     *      one mechanical rule
     *      no special first-K branch
     *      easy invariant
     *      easy to reconstruct months later
     *
     * ------------------------------------------------------------
     * GUARDED VERSION:
     * ------------------------------------------------------------
     *
     *      if size < K:
     *          offer(item)
     *      else if item better than root:
     *          poll()
     *          offer(item)
     *
     * Advantage:
     *
     *      avoids unnecessary mutation for clearly bad candidates
     *
     * Disadvantage:
     *
     *      extra branching and comparison logic
     *      slightly higher reconstruction burden
     *
     * Same asymptotic complexity:
     *
     *      O(N log K)
     *
     * INTERVIEW DEFAULT:
     *
     *      retain offer -> trim.
     *
     * This is a good example of:
     *
     *      small micro-optimization
     *      vs
     *      lower cognitive error rate.
     */

    /*
     * ============================================================
     * 10. MAX HEAP COMPLEXITY -- DERIVE IT
     * ============================================================
     *
     * N points.
     *
     * Heap contains at most:
     *
     *      K + 1 temporarily
     *
     * Heap height:
     *
     *      O(log K)
     *
     * For each point:
     *
     *      offer = O(log K)
     *
     * possibly:
     *
     *      poll = O(log K)
     *
     * N iterations:
     *
     *      O(N log K)
     *
     * Extract K answers:
     *
     *      O(K log K)
     *
     * Since K <= N, standard overall bound remains:
     *
     *      O(N log K)
     *
     * Auxiliary space:
     *
     *      O(K)
     *
     * Output array itself is normally not counted as
     * auxiliary working space.
     */

    /*
     * ============================================================
     * 11. MAX HEAP CORRECTNESS PROOF
     * ============================================================
     *
     * Claim:
     *
     * After processing p points, the heap contains the
     * min(K, p) closest processed points.
     *
     * Base:
     *
     * p = 0.
     *
     * Heap is empty.
     *
     * Claim holds.
     *
     * Inductive step:
     *
     * Assume claim holds before new point X.
     *
     * Add X.
     *
     * If heap size <= K:
     *
     *      all processed candidates fit.
     *
     * If heap size = K+1:
     *
     *      maxHeap.poll()
     *
     * removes the farthest among those K+1 candidates.
     *
     * Therefore exactly the K closest candidates survive.
     *
     * Claim remains true.
     *
     * After N points:
     *
     *      heap contains exactly the K closest points.
     */

    /*
     * ============================================================
     * 12. BASELINE SOLUTION -- FULL SORT
     * ============================================================
     *
     * Why keep this solution in the file?
     *
     * Because it is the natural first answer and gives the
     * conceptual starting point from which Heap and Quickselect
     * are optimized.
     *
     * It is also useful when:
     *
     *      complete ordering is actually required
     *      many future rank queries reuse sorted order
     *      simplicity matters more than asymptotic optimization
     *
     * Time:
     *
     *      O(N log N)
     *
     * Mutates:
     *
     *      points
     */

    static final class FullSortSolution {

        public int[][] kClosest(int[][] points, int k) {

            Arrays.sort(
                    points,
                    Comparator.comparingLong(
                            KClosestPointsToOrigin::squaredDistance));

            /*
             * Modern Java/API simplification worth remembering:
             *
             *      Arrays.copyOf(points, k)
             *
             * is clearer here than:
             *
             *      Arrays.copyOfRange(points, 0, k)
             */
            return Arrays.copyOf(points, k);
        }
    }

    /*
     * ============================================================
     * 13. QUICKSELECT -- WHY IT EXISTS
     * ============================================================
     *
     * Full sorting establishes:
     *
     *      point 1 <= point 2 <= point 3 <= ...
     *
     * But the actual requirement only needs:
     *
     *      K winning points
     *      ---------------- boundary
     *      remaining points
     *
     * So the problem is selection, not complete ordering.
     *
     * Quickselect repeatedly partitions around a pivot.
     *
     * Target index:
     *
     *      K - 1
     *
     * After partition:
     *
     *      [left, pivotIndex)
     *          distance <= pivot
     *
     *      pivotIndex
     *          pivot
     *
     *      (pivotIndex, right]
     *          distance > pivot
     *
     * If:
     *
     *      pivotIndex == K - 1
     *
     * the boundary is found.
     *
     * If pivotIndex is too small:
     *
     *      search right
     *
     * If pivotIndex is too large:
     *
     *      search left
     *
     * ONLY ONE SIDE remains relevant.
     *
     * That is the critical difference from Quicksort.

     * ------------------------------------------------------------
     * VISUAL -- QUICKSELECT IS A TAPE WITH ONE TARGET INDEX
     * ------------------------------------------------------------
     *
     * K = 3
     *
     * target = K - 1 = 2
     *
     * Index:
     *
     *        0      1      2      3      4
     *      +------+------+------+------+------+
     *      |  18  |  26  |  20  |   2  |  13  |
     *      +------+------+------+------+------+
     *                    ^
     *                 target
     *
     * After some partition:
     *
     *        0      1      2      3      4
     *      +------+------+------+------+------+
     *      |   2  |  13  |  18  |  26  |  20  |
     *      +------+------+------+------+------+
     *                    ^
     *                 pivotIndex
     *                 == target
     *
     * STOP.
     *
     * We only need:
     *
     *      +------------------+----------------+
     *      | FIRST K WINNERS  | REMAINDER      |
     *      +------------------+----------------+
     *      |  2 | 13 | 18     | 26 | 20        |
     *      +------------------+----------------+
     *
     * We DO NOT need:
     *
     *      2 < 13 < 18
     *
     * to be fully sorted.
     *
     * ------------------------------------------------------------
     * VISUAL -- WHICH SIDE SURVIVES?
     * ------------------------------------------------------------
     *
     * target = 4
     *
     * pivot lands at 2:
     *
     *      [ <= pivot ] P [ > pivot ................. ]
     *                    ^
     *                 index 2
     *
     *      target is RIGHT
     *
     *      discard left + pivot
     *
     *
     * pivot lands at 7:
     *
     *      [ ................. <= pivot ] P [ > pivot ]
     *                                ^
     *                             index 7
     *
     *      target is LEFT
     *
     *      discard right + pivot
     *
     *
     * pivot lands at 4:
     *
     *      [ winners ........ ] P [ remainder ........ ]
     *                         ^
     *                       target
     *
     *      DONE
     *
     * Only ONE side remains relevant after each partition.
     *
     */

    static final class QuickSelectSolution {

        public int[][] kClosest(int[][] points, int k) {

            if (k == points.length) {
                return Arrays.copyOf(points, k);
            }

            int target = k - 1;

            int left = 0;
            int right = points.length - 1;

            while (left <= right) {

                int pivotIndex =
                        partition(points, left, right);

                if (pivotIndex == target) {
                    break;
                }

                if (pivotIndex < target) {
                    left = pivotIndex + 1;
                } else {
                    right = pivotIndex - 1;
                }
            }

            return Arrays.copyOf(points, k);
        }

        private int partition(
                int[][] points,
                int left,
                int right) {

            /*
             * Random pivot:
             *
             * useful modern Java utility and better defensive
             * default than always choosing right-most pivot.
             */
            int pivotIndex =
                    ThreadLocalRandom.current()
                            .nextInt(left, right + 1);

            swap(points, pivotIndex, right);

            long pivotDistance =
                    squaredDistance(points[right]);

            int nextSmaller = left;

            /*
             * Loop invariant:
             *
             * [left, nextSmaller)
             *      <= pivot
             *
             * [nextSmaller, i)
             *      > pivot
             *
             * [i, right)
             *      unknown
             */
            for (int i = left; i < right; i++) {

                if (squaredDistance(points[i])
                        <= pivotDistance) {

                    swap(points, nextSmaller, i);
                    nextSmaller++;
                }
            }

            swap(points, nextSmaller, right);

            return nextSmaller;
        }
    }

    /*
     * ============================================================
     * 14. QUICKSELECT DRY RUN -- BOUNDARY THINKING
     * ============================================================
     *
     * Suppose distances are:
     *
     *      [18, 26, 20, 2, 13]
     *
     * K = 3
     *
     * Target index:
     *
     *      K - 1 = 2
     *
     * Imagine a partition produces:
     *
     *      [2, 13, 18, 26, 20]
     *              ^
     *          pivotIndex = 2
     *
     * We do NOT care whether:
     *
     *      2 < 13 < 18
     *
     * is fully sorted.
     *
     * We only care that three acceptable points occupy:
     *
     *      indices 0, 1, 2
     *
     * Therefore return first K.
     *

     * ------------------------------------------------------------
     * VISUAL -- PARTITION POINTERS
     * ------------------------------------------------------------
     *
     * Pivot distance = 18
     *
     * Start:
     *
     *      nextSmaller
     *          |
     *          v
     *
     *        [26, 13, 20, 2, 18]
     *          ^
     *          i
     *
     * Regions while scanning:
     *
     *      +----------------+----------------+----------------+
     *      | <= pivot       | > pivot        | unknown        |
     *      +----------------+----------------+----------------+
     *      left          nextSmaller          i          right
     *
     * When points[i] <= pivot:
     *
     *      swap(points[nextSmaller], points[i])
     *      nextSmaller++
     *
     * When points[i] > pivot:
     *
     *      i moves
     *      nextSmaller stays
     *
     * At the end:
     *
     *      [ <= 18 ][ > 18 ][ pivot 18 ]
     *
     * Swap pivot with nextSmaller:
     *
     *      [ <= 18 ][ 18 ][ > 18 ]
     *                    ^
     *                 final pivot
     *
     * This picture is the partition invariant.
     *
     * ------------------------------------------------------------
     * IMPORTANT CORRECTION
     * ------------------------------------------------------------
     *
     * Quickselect does NOT guarantee:
     *
     *      "discard half after every partition."
     *
     * A bad partition can look like:
     *
     *      1 element | N-1 elements
     *
     * Worst-case recurrence:
     *
     *      T(N) = T(N-1) + O(N)
     *
     * therefore:
     *
     *      O(N^2)
     *
     * Random pivots make expected behavior:
     *
     *      O(N)
     *
     * not guaranteed O(N).
     */

    /*
     * ============================================================
     * 15. CROSS-PRODUCT -- SLIGHT WORDING CHANGE, DIFFERENT TOOL
     * ============================================================
     *
     * +--------------------------------------+----------------------------+
     * | Requirement                          | Best default               |
     * +--------------------------------------+----------------------------+
     * | K closest, any order                 | max heap / Quickselect     |
     * | Streaming input                      | fixed-size max heap        |
     * | N unknown / unbounded                | fixed-size max heap        |
     * | Cannot modify input                  | fixed-size max heap        |
     * | Mutable full array exists            | Quickselect attractive     |
     * | Expected fastest static selection    | randomized Quickselect     |
     * | Strong worst-case asymptotic bound   | fixed-size max heap        |
     * | Complete sorted output               | full sort                  |
     * | K closest AND sorted                 | select K + sort those K    |
     * | Many different future K queries      | sort once may amortize     |
     * | K is tiny relative to N              | heap especially attractive |
     * | Need only K-th closest boundary      | Quickselect / size-K heap  |
     * +--------------------------------------+----------------------------+
     *
     * ------------------------------------------------------------
     * WHY HEAP DOES NOT REPLACE QUICKSELECT
     * ------------------------------------------------------------
     *
     * Heap:
     *
     *      O(N log K)
     *      O(K) memory
     *
     * Quickselect:
     *
     *      expected O(N)
     *      O(1) auxiliary with iterative implementation
     *
     * For a mutable offline array, Quickselect may do less work.
     *
     * ------------------------------------------------------------
     * WHY QUICKSELECT DOES NOT REPLACE HEAP
     * ------------------------------------------------------------
     *
     * Quickselect needs a complete random-access array.
     *
     * It is not naturally online.
     *
     * It mutates the input.
     *
     * It has O(N^2) theoretical worst case.
     *
     * ------------------------------------------------------------
     * WHY SORT DOES NOT REPLACE BOTH
     * ------------------------------------------------------------
     *
     * Sort is correct.
     *
     * But it establishes complete ordering even when the
     * output only asks for a K-element subset.
     *
     * ------------------------------------------------------------
     * IF OUTPUT MUST BE SORTED
     * ------------------------------------------------------------
     *
     * Heap alone does NOT return globally sorted survivors.
     *
     * One option:
     *
     *      select K using heap
     *      sort the K survivors
     *
     * Complexity:
     *
     *      O(N log K + K log K)
     *
     * ------------------------------------------------------------
     * WORDING THAT FLIPS HEAP DIRECTION
     * ------------------------------------------------------------
     *
     *      K closest  -> keep smallest -> MAX heap
     *      K farthest -> keep largest  -> MIN heap
     *      K largest  -> keep largest  -> MIN heap
     *      K smallest -> keep smallest -> MAX heap
     */

    /*
     * ============================================================
     * 16. TIME / SPACE / RETAINABILITY TRADE-OFF TABLE
     * ============================================================
     *
     * +--------------+----------------+------------+------------+------------+
     * | Approach     | Time           | Aux Space  | 6-mo Recall| Bug Risk   |
     * +--------------+----------------+------------+------------+------------+
     * | Full Sort    | O(N log N)     | sort-dep.  | VERY HIGH  | VERY LOW   |
     * | Max Heap     | O(N log K)     | O(K)       | VERY HIGH  | LOW        |
     * | Quickselect  | avg O(N)       | O(1)*      | MEDIUM     | MED-HIGH   |
     * +--------------+----------------+------------+------------+------------+
     *

     * ------------------------------------------------------------
     * VISUAL -- MACHINE EFFICIENCY VS HUMAN RETRIEVABILITY
     * ------------------------------------------------------------
     *
     * Human reconstruction reliability
     *
     * HIGH
     *  ^
     *  |
     *  |   FULL SORT              MAX HEAP
     *  |      *                      *
     *  |
     *  |
     *  |                         QUICKSELECT
     *  |                              *
     *  |
     *  +-------------------------------------------->
     *       simpler / more work       less expected work
     *                     Machine efficiency
     *
     * Interpretation:
     *
     * FULL SORT
     *      easiest to retrieve,
     *      but may do unnecessary ordering.
     *
     * MAX HEAP
     *      near the top-right practical sweet spot:
     *      strong efficiency + strong reconstruction.
     *
     * QUICKSELECT
     *      strongest expected asymptotics here,
     *      but more state/pointer details to reconstruct.
     *
     * ------------------------------------------------------------
     * VISUAL -- WHAT SHOULD LIVE IN MEMORY?
     * ------------------------------------------------------------
     *
     *             +------------------------------+
     *             | LEVEL 1 -- AUTOMATIC         |
     *             |                              |
     *             | FIXED-SIZE HEAP              |
     *             | ROOT = WORST ACCEPTED        |
     *             | OFFER -> SIZE>K -> POLL      |
     *             +------------------------------+
     *                          |
     *                          v
     *             +------------------------------+
     *             | LEVEL 2 -- INSTANTLY DERIVE  |
     *             |                              |
     *             | FULL SORT                    |
     *             | SORT BY METRIC -> FIRST K    |
     *             +------------------------------+
     *                          |
     *                          v
     *             +------------------------------+
     *             | LEVEL 3 -- CONCEPT MASTERED  |
     *             |                              |
     *             | QUICKSELECT                  |
     *             | BOUNDARY, NOT ORDER          |
     *             +------------------------------+
     *
     * * O(1) auxiliary here because THIS implementation is
     *   iterative. Recursive Quickselect consumes stack space.
     *
     * ------------------------------------------------------------
     * FULL SORT -- RETENTION PROFILE
     * ------------------------------------------------------------
     *
     * Retrieval:
     *
     *      almost immediate
     *
     * Reconstruction:
     *
     *      metric -> comparator -> sort -> first K
     *
     * Failure modes:
     *
     *      low
     *
     * Cost:
     *
     *      asymptotically does extra work
     *
     * ------------------------------------------------------------
     * MAX HEAP -- RETENTION PROFILE
     * ------------------------------------------------------------
     *
     * Retrieval anchor:
     *
     *      KEEP K WINNERS
     *      ROOT = WORST ACCEPTED
     *
     * Reconstruction:
     *
     *      comparator
     *      offer
     *      if size > K -> poll
     *
     * Failure modes:
     *
     *      mainly heap direction / comparator direction
     *
     * This is the best BALANCE of:
     *
     *      performance
     *      generality
     *      interview articulation
     *      six-month reconstruction
     *
     * ------------------------------------------------------------
     * QUICKSELECT -- RETENTION PROFILE
     * ------------------------------------------------------------
     *
     * Retrieval anchor:
     *
     *      FIND BOUNDARY, NOT ORDER
     *
     * Reconstruction requires remembering:
     *
     *      target = K - 1
     *      pivot
     *      partition invariant
     *      swap pivot back
     *      move only one boundary
     *
     * Failure modes:
     *
     *      off-by-one
     *      broken partition invariant
     *      wrong target
     *      searching both sides
     *      forgetting input mutation
     *
     * Therefore Quickselect has excellent algorithmic efficiency
     * but lower reconstruction reliability months later.
     *
     * ------------------------------------------------------------
     * INTERVIEW ROI
     * ------------------------------------------------------------
     *
     * If only ONE implementation must become automatic:
     *
     *      MAX HEAP
     *
     * If interviewer asks:
     *
     *      "Can you improve expected runtime?"
     *
     * then derive:
     *
     *      QUICKSELECT
     *
     * Full sort remains the baseline you can always produce.
     */

    /*
     * ============================================================
     * 17. MODERN JAVA -- WHY RankedPoint EARNS ITS PLACE
     * ============================================================
     *
     * Preferred readable form:
     *
     *      private record RankedPoint(
     *              int[] point,
     *              long distance) {
     *      }
     *
     *      PriorityQueue<RankedPoint> maxHeap =
     *              new PriorityQueue<>(
     *                      Comparator
     *                              .comparingLong(
     *                                      RankedPoint::distance)
     *                              .reversed());
     *
     * Why this is nicer:
     *
     *      1. The heap stores the logical candidate:
     *
     *             point + ranking key
     *
     *      2. The comparator reads almost like English:
     *
     *             compare RankedPoint by distance
     *             reverse
     *
     *      3. Distance is calculated once when the candidate
     *         enters the algorithm.
     *
     *      4. No long class-qualified method reference appears
     *         inside the core algorithm.
     *
     *      5. The record is immutable and requires no getters,
     *         constructor boilerplate, equals/hashCode code, etc.
     *
     * ------------------------------------------------------------
     * IMPORTANT DISTINCTION
     * ------------------------------------------------------------
     *
     * This is better than wrapping merely:
     *
     *      record Point(int x, int y)
     *
     * because RankedPoint is not just cosmetic.
     *
     * It represents actual algorithmic state:
     *
     *      candidate + cached ranking metric
     *
     * ------------------------------------------------------------
     * COST
     * ------------------------------------------------------------
     *
     * Each processed point creates a RankedPoint object.
     *
     * Trade-off:
     *
     *      + clearer semantics
     *      + cached distance
     *      + cleaner comparator
     *
     *      - extra wrapper allocation per processed point
     *
     * Asymptotically both remain:
     *
     *      O(N log K) time
     *      O(K) live heap state
     *
     * but the record version creates O(N) short-lived wrapper
     * objects over the full run.
     *
     * ------------------------------------------------------------
     * INTERVIEW CHOICE
     * ------------------------------------------------------------
     *
     * If interviewer values:
     *
     *      clean Java
     *      expressive types
     *      readability
     *
     * RankedPoint is excellent.
     *
     * If an interviewer insists on absolute minimum typing,
     * the same heap invariant can be written directly with int[].
     *
     * But this file intentionally keeps ONE primary heap form
     * for photographic retention rather than maintaining two
     * competing implementations.
     *
     * ------------------------------------------------------------
     * RETENTION RULE
     * ------------------------------------------------------------
     *
     * Primary mental model stays identical:
     *
     *      KEEP K WINNERS
     *      ROOT = WORST ACCEPTED
     *      OFFER
     *      SIZE > K -> POLL
     *
     * The record changes representation,
     * NOT the algorithm.
     *
     * IMPORTANT FILE-LAYOUT RULE
     * --------------------------
     *
     * The primary implementation above is intentionally kept
     * uninterrupted.
     *
     * No long WHY blocks are inserted inside the code.
     *
     * Explanation comes AFTER the photographic code block.
     */

    /*
     * ============================================================
     * 18. WHAT WAS RESTORED FROM THE EARLIER FILE
     * ============================================================
     *
     * V2 intentionally compressed repeated material, but a few
     * pieces were worth restoring because they aid retrieval:
     *
     *      representative example
     *
     *      official problem link
     *
     *      recognition signals
     *
     *      backpack / disposal-boundary mental image
     *
     *      explicit pattern variations
     *
     *      explicit mastery / retrieval questions
     *
     * What remains compressed instead of duplicated:
     *
     *      repeated statements of the same invariant
     *
     *      multiple separate "use heap when / avoid heap when"
     *      blocks
     *
     *      repeated complexity summaries
     *
     *      repeated interview questions already answered by
     *      the cross-product and recall sections
     *
     * The goal is:
     *
     *      preserve unique learning value
     *      remove repetition that increases scroll cost.
     */

    /*
     * ============================================================
     * 19. WRONG SOLUTIONS / NEAR-MISSES
     * ============================================================
     *
     * 1. Fixed-size MIN heap for K closest
     *
     *    Removes the best candidate.
     *
     * ------------------------------------------------------------
     *
     * 2. Let heap grow to N
     *
     *    Correctness may survive, but:
     *
     *        space -> O(N)
     *        Top-K invariant disappears.
     *
     * ------------------------------------------------------------
     *
     * 3. Compute sqrt()
     *
     *    Correct but unnecessary.
     *
     * ------------------------------------------------------------
     *
     * 4. Comparator subtraction
     *
     *    Can overflow under larger constraints.
     *
     * ------------------------------------------------------------
     *
     * 5. Quickselect searches BOTH sides
     *
     *    That drifts toward Quicksort-style work.
     *
     * ------------------------------------------------------------
     *
     * 6. Say "Quickselect removes half every time"
     *
     *    False.
     *
     * ------------------------------------------------------------
     *
     * 7. Say recursive Quickselect is O(1) auxiliary space
     *
     *    False.
     *
     *    Recursive version consumes call-stack space.
     */

    /*
     * ============================================================
     * 20. REUSABLE TOP-K TEMPLATE
     * ============================================================
     *
     * define ranking metric
     *
     * identify what "better" means
     *
     * identify WORST accepted candidate
     *
     * make WORST accepted candidate the root
     *
     * for each item:
     *
     *      offer(item)
     *
     *      if size > K:
     *          poll()
     *
     * return survivors
     *
     * ------------------------------------------------------------
     * TRANSFER EXAMPLES
     * ------------------------------------------------------------
     *
     * K closest points:
     *
     *      metric = distance
     *      keep smallest
     *      root = largest
     *      max heap
     *
     * K largest values:
     *
     *      metric = value
     *      keep largest
     *      root = smallest
     *      min heap
     *
     * Top K frequent:
     *
     *      metric = frequency
     *      keep largest frequencies
     *      root = smallest accepted frequency
     *      min heap
     *
     * K closest numbers to X:
     *
     *      metric = abs(value - X)
     *      keep smallest metric
     *      root = largest distance
     *      max heap
     */

    /*
     * ============================================================
     * 21. INTERVIEW ARTICULATION -- 30 SECOND ANSWER
     * ============================================================
     *
     * "We only need K closest points, not complete ordering.
     *
     * I'll maintain the K closest points seen so far in a
     * fixed-size max heap.
     *
     * The root is intentionally the farthest accepted point,
     * because that is exactly the candidate I want to evict
     * whenever a better point arrives.
     *
     * I offer every point and poll when size exceeds K.
     *
     * Since the heap stays size K, each update is O(log K),
     * giving O(N log K) time and O(K) auxiliary space.
     *
     * If the complete mutable array is available and expected
     * linear selection is desired, randomized Quickselect is an
     * alternative with expected O(N) time."
     */

    /*
     * ============================================================
     * 22. SIX-MONTH RECALL CARD
     * ============================================================
     *
     * TRIGGER
     * -------
     *
     *      KEEP ONLY K WINNERS
     *
     * FIRST QUESTION
     * --------------
     *
     *      Which accepted item should be easiest to throw away?
     *
     * RULE
     * ----
     *
     *      ROOT = WORST ACCEPTED
     *
     * K CLOSEST
     * ---------
     *
     *      keep smallest distances
     *      worst accepted = largest
     *      => MAX HEAP
     *
     * PRIMARY CODE
     * ------------
     *
     *      maxHeap
     *
     *      for point:
     *          offer(point)
     *
     *          if size > K:
     *              poll()
     *
     *      return survivors
     *
     * QUICKSELECT CUE
     * ---------------
     *
     *      FIND BOUNDARY, NOT ORDER
     *
     * TARGET
     * ------
     *
     *      K - 1
     *
     * COMPLEXITY
     * ----------
     *
     *      sort:
     *          O(N log N)
     *
     *      heap:
     *          O(N log K), O(K)
     *
     *      randomized iterative Quickselect:
     *          expected O(N)
     *          worst O(N^2)
     *          O(1) auxiliary
     *
     * JAVA RECALL
     * -----------
     *
     *      BY_DISTANCE
     *          = closest -> farthest
     *
     *      BY_DISTANCE.reversed()
     *          = farthest -> closest
     *          = max heap for K closest
     *
     *      Point record?
     *          Good domain model,
     *          optional for LeetCode int[][].
     *
     * BIGGEST TRAPS
     * -------------
     *
     *      heap direction
     *      heap growing to N
     *      assuming PriorityQueue is globally sorted
     *      comparator overflow
     *      Quickselect target/off-by-one
     *      Quickselect searching both sides
     *      claiming every partition halves the range
     */


    /*
     * ============================================================
     * ONE-SCREEN VISUAL RECALL CARD
     * ============================================================
     *
     *                  K CLOSEST POINTS
     *                        |
     *                        v
     *              keep SMALL distances
     *                        |
     *                        v
     *              worst accepted = LARGE
     *                        |
     *                        v
     *                    MAX HEAP
     *
     *
     *                  +-----------+
     * incoming ------> |   offer   |
     *                  +-----------+
     *                        |
     *                   size > K ?
     *                    /       \
     *                  no         yes
     *                  |           |
     *                  |           v
     *                  |         poll
     *                  |           |
     *                  +-----+-----+
     *                        |
     *                        v
     *               K best seen so far
     *
     *
     *      NEED STREAMING? -----------------> HEAP
     *
     *      NEED COMPLETE ORDER? ------------> SORT
     *
     *      STATIC + MUTABLE + EXPECTED O(N)?
     *                                     -> QUICKSELECT
     *
     *
     *      HEAP:
     *          ROOT = WORST ACCEPTED
     *
     *      QUICKSELECT:
     *          TARGET = K - 1
     *          FIND BOUNDARY, NOT ORDER
     */

    /*
     * ============================================================
     * 23. MASTERY SELF-CHECK
     * ============================================================
     *
     * Without looking above, can I answer:
     *
     * [ ] Why is K closest a max heap rather than min heap?
     *
     * [ ] What exactly does heap.peek() represent?
     *
     * [ ] What is the invariant after p processed points?
     *
     * [ ] Why does offer -> trim preserve correctness?
     *
     * [ ] Why is the runtime O(N log K), not O(N log N)?
     *
     * [ ] Can a min heap solve the problem at all?
     *
     * [ ] Why is full sort solving more than required?
     *
     * [ ] What does Quickselect remove from the work?
     *
     * [ ] Why does Quickselect search only one side?
     *
     * [ ] Why is Quickselect expected O(N), not guaranteed O(N)?
     *
     * [ ] Which approach survives streaming input?
     *
     * [ ] Which approach mutates the input?
     *
     * [ ] Which solution can I reconstruct most reliably after
     *     six months?
     *
     * Desired answer to the last question:
     *
     *      fixed-size max heap
     */

    /*
     * ============================================================
     * 24. HELPERS + SELF-VERIFYING TESTS
     * ============================================================
     */


    private static void swap(int[][] points, int i, int j) {

        int[] temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }

    private static int[][] deepCopy(int[][] points) {

        /*
         * Intentionally use a plain loop rather than a Stream.
         *
         * This helper is test plumbing, and the loop is easier
         * to mentally parse in a DSA file.
         */
        int[][] copy = new int[points.length][];

        for (int i = 0; i < points.length; i++) {
            copy[i] = points[i].clone();
        }

        return copy;
    }

    private static void check(boolean condition, String message) {

        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void verifyKClosest(
            int[][] original,
            int[][] result,
            int k) {

        check(
                result.length == k,
                "Expected " + k
                        + " points but got "
                        + result.length);

        long[] distances =
                new long[original.length];

        for (int i = 0; i < original.length; i++) {
            distances[i] =
                    squaredDistance(original[i]);
        }

        Arrays.sort(distances);

        long cutoff =
                distances[k - 1];

        for (int[] point : result) {

            check(
                    squaredDistance(point) <= cutoff,
                    "Point beyond K-th distance cutoff: "
                            + Arrays.toString(point));
        }
    }

    private static void runCase(
            int[][] points,
            int k) {

        MaxHeapSolution heap =
                new MaxHeapSolution();

        FullSortSolution sort =
                new FullSortSolution();

        QuickSelectSolution quick =
                new QuickSelectSolution();

        int[][] heapResult =
                heap.kClosest(
                        deepCopy(points),
                        k);

        int[][] sortResult =
                sort.kClosest(
                        deepCopy(points),
                        k);

        int[][] quickResult =
                quick.kClosest(
                        deepCopy(points),
                        k);

        verifyKClosest(
                points,
                heapResult,
                k);

        verifyKClosest(
                points,
                sortResult,
                k);

        verifyKClosest(
                points,
                quickResult,
                k);
    }

    public static void main(String[] args) {

        runCase(
                new int[][]{
                        {1, 3},
                        {-2, 2}
                },
                1);

        runCase(
                new int[][]{
                        {3, 3},
                        {5, -1},
                        {-2, 4}
                },
                2);

        runCase(
                new int[][]{
                        {2, 2},
                        {3, 3},
                        {-1, -1}
                },
                3);

        runCase(
                new int[][]{
                        {0, 0},
                        {10, 10},
                        {-5, -5}
                },
                1);

        runCase(
                new int[][]{
                        {1, 1},
                        {-1, -1},
                        {2, 2},
                        {-2, -2}
                },
                2);

        runCase(
                new int[][]{
                        {-8, -8},
                        {-1, 2},
                        {-3, -4},
                        {9, 9}
                },
                2);

        runCase(
                new int[][]{
                        {7, -3}
                },
                1);

        runCase(
                new int[][]{
                        {10000, 10000},
                        {-10000, -10000},
                        {1, 0},
                        {0, 1}
                },
                2);

        System.out.println(
                "All K Closest implementations passed.");
    }
}

/*
 * ================================================================
 * FINAL RECONSTRUCTION TEST
 * ================================================================
 *
 * Can I derive this without remembering the exact code?
 *
 *      only K winners
 *
 *          ->
 *
 *      need one eviction boundary
 *
 *          ->
 *
 *      worst accepted should be root
 *
 *          ->
 *
 *      K closest = keep smallest
 *
 *          ->
 *
 *      worst accepted = largest distance
 *
 *          ->
 *
 *      MAX HEAP
 *
 *          ->
 *
 *      OFFER
 *
 *          ->
 *
 *      IF SIZE > K
 *          POLL
 *
 * If yes, I know the pattern rather than merely remembering
 * this LeetCode solution.
 */
