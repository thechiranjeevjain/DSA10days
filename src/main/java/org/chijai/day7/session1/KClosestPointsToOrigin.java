package org.chijai.day7.session1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class KClosestPointsToOrigin {

    /*
     * ============================================================
     * 2. 📘 PRIMARY PROBLEM
     * ============================================================
     *
     * Title:
     * K Closest Points to Origin
     *
     * Difficulty:
     * Medium
     *
     * Tags:
     * Heap
     * Priority Queue
     * Quick Select
     * Partition
     * Divide and Conquer
     *
     * Problem
     * -------
     * Given an array of points where:
     *
     * points[i] = [xi, yi]
     *
     * Return the K points whose Euclidean distance from
     * the origin (0,0) is the smallest.
     *
     * Distance:
     *
     * sqrt(x²+y²)
     *
     * Since sqrt is monotonic, compare squared distance:
     *
     * x²+y²
     *
     * The answer may be returned in any order.
     *
     * Constraints
     * -----------
     * 1 <= K <= points.length <= 10^4
     * -10^4 <= xi, yi <= 10^4
     * Unique answer (ignoring ordering).
     *
     * Example 1
     * ---------
     * points = [[1,3],[-2,2]]
     * K = 1
     *
     * Output:
     * [[-2,2]]
     *
     * Example 2
     * ---------
     * points = [[3,3],[5,-1],[-2,4]]
     * K = 2
     *
     * Output:
     * [[3,3],[-2,4]]
     *
     * Accepted ordering:
     * [[-2,4],[3,3]]
     *
     * Official Link
     * -------------
     * https://leetcode.com/problems/k-closest-points-to-origin/
     */

    /*
     * ============================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern
     * -------
     * Top-K Elements
     *
     * Archetype
     * ---------
     * Maintain only the K best candidates seen so far.
     *
     * Two classical implementations:
     *
     * 1.
     * Fixed-size Max Heap
     *
     * 2.
     * Quick Select
     *
     * ------------------------------------------------------------
     * Core Invariant (Heap)
     * ------------------------------------------------------------
     *
     * The heap always stores exactly the K closest points
     * among every point processed so far.
     *
     * The root is intentionally the WORST point
     * among those K points.
     *
     * Therefore:
     *
     * if newPoint is better than root
     *     remove root
     *     insert newPoint
     *
     * Otherwise ignore it.
     *
     * ------------------------------------------------------------
     * Why Max Heap?
     * ------------------------------------------------------------
     *
     * We need to discard the farthest element whenever
     * size exceeds K.
     *
     * Therefore removal must be O(logK).
     *
     * Root must always be the largest distance.
     *
     * ------------------------------------------------------------
     * Core Invariant (Quick Select)
     * ------------------------------------------------------------
     *
     * Partition rearranges points such that:
     *
     * left side <= pivot
     * right side > pivot
     *
     * We never need complete sorting.
     *
     * We only recurse into the side still containing
     * the K-th boundary.
     *
     * ------------------------------------------------------------
     * Recognition Signals
     * ------------------------------------------------------------
     *
     * Look for:
     *
     * • K closest
     * • K smallest
     * • Top K
     * • K largest
     * • Streaming input
     * • Ranking without complete sorting
     *
     * ------------------------------------------------------------
     * Use Heap When
     * ------------------------------------------------------------
     *
     * • data arrives continuously
     * • streaming
     * • cannot store everything
     * • K << N
     *
     * ------------------------------------------------------------
     * Use Quick Select When
     * ------------------------------------------------------------
     *
     * • complete array already exists
     * • fastest average runtime desired
     * • modifying array allowed
     *
     * ------------------------------------------------------------
     * Avoid Heap When
     * ------------------------------------------------------------
     *
     * You require fully sorted output.
     *
     * ------------------------------------------------------------
     * Avoid Quick Select When
     * ------------------------------------------------------------
     *
     * Online stream.
     *
     * Frequent insertions.
     *
     * Immutable collection.
     *
     * Worst-case guarantee required.
     *
     * ------------------------------------------------------------
     * Comparison
     * ------------------------------------------------------------
     *
     * Full Sort
     * ----------
     * Time:
     * O(NlogN)
     *
     * Produces:
     * Completely sorted array.
     *
     * Heap
     * ----
     * Time:
     * O(NlogK)
     *
     * Space:
     * O(K)
     *
     * Online capable:
     * YES
     *
     * Quick Select
     * ------------
     * Average:
     * O(N)
     *
     * Worst:
     * O(N²)
     *
     * Space:
     * O(1)
     *
     * Online:
     * NO
     */

    /*
     * ============================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * -----------------------------
     * Heap Mental Model
     * -----------------------------
     *
     * Imagine carrying a backpack that can contain
     * exactly K trophies.
     *
     * Every incoming point competes against the
     * weakest trophy currently inside.
     *
     * If it is better,
     * replace the weakest.
     *
     * Otherwise ignore it forever.
     *
     * At the end,
     * the backpack contains exactly the K winners.
     *
     * ------------------------------------------------------------
     * Heap Invariant #1
     * ------------------------------------------------------------
     *
     * Heap size never exceeds K.
     *
     * ------------------------------------------------------------
     * Heap Invariant #2
     * ------------------------------------------------------------
     *
     * Root always stores the largest distance
     * among all points inside the heap.
     *
     * ------------------------------------------------------------
     * Heap Invariant #3
     * ------------------------------------------------------------
     *
     * Every point outside the heap that has already
     * been processed is farther than at least one
     * point inside the heap.
     *
     * Therefore it can never belong to the final answer.
     *
     * ------------------------------------------------------------
     * Heap Variables
     * ------------------------------------------------------------
     *
     * heap
     * ----
     * Current best K candidates.
     *
     * root
     * ----
     * Worst candidate currently accepted.
     *
     * distance
     * --------
     * Ranking metric.
     *
     * ------------------------------------------------------------
     * Allowed Transition
     * ------------------------------------------------------------
     *
     * Insert point.
     *
     * If size>K
     * remove root.
     *
     * Invariant restored immediately.
     *
     * ------------------------------------------------------------
     * Forbidden Transition
     * ------------------------------------------------------------
     *
     * Removing arbitrary node.
     *
     * Removing closest node.
     *
     * Keeping size>K.
     *
     * ------------------------------------------------------------
     * Heap Termination
     * ------------------------------------------------------------
     *
     * Every point processed exactly once.
     *
     * Heap already represents answer.
     *
     * ------------------------------------------------------------
     * Quick Select Mental Model
     * ------------------------------------------------------------
     *
     * We are not searching for order.
     *
     * We are searching only for the boundary
     * separating:
     *
     * first K points
     *
     * remaining points.
     *
     * Partition places one pivot exactly where
     * it belongs relative to every other point.
     *
     * Entire halves become impossible answers.
     *
     * ------------------------------------------------------------
     * Quick Select Invariant
     * ------------------------------------------------------------
     *
     * After partition:
     *
     * left <= pivot
     * right > pivot
     *
     * Therefore only one side can still contain
     * the desired K-th boundary.
     *
     * ------------------------------------------------------------
     * Why Naive Sorting Fails
     * ------------------------------------------------------------
     *
     * Sorting solves a larger problem than required.
     *
     * We only need:
     *
     * first K points.
     *
     * Not complete ordering.
     *
     * Hence O(NlogN) work is unnecessary.
     */

    /*
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * Mistake 1
     * ---------
     * Using Min Heap of size K.
     *
     * Why it looks correct:
     * Smallest distance stays on top.
     *
     * Actual problem:
     * We need fast removal of the WORST point.
     *
     * Min heap removes the BEST point.
     *
     * Violated invariant:
     * Root must represent discard candidate.
     *
     * ------------------------------------------------------------
     * Mistake 2
     * ---------
     * Computing sqrt().
     *
     * Looks mathematically cleaner.
     *
     * Unnecessary.
     *
     * x²+y² preserves ordering.
     *
     * ------------------------------------------------------------
     * Mistake 3
     * ---------
     * Heap grows to N elements.
     *
     * Complexity silently becomes:
     *
     * O(NlogN)
     *
     * instead of
     *
     * O(NlogK)
     *
     * ------------------------------------------------------------
     * Mistake 4
     * ---------
     * Wrong comparator direction.
     *
     * Comparator accidentally creates
     * a min heap.
     *
     * Root becomes smallest distance.
     *
     * Removal becomes incorrect.
     *
     * ------------------------------------------------------------
     * Mistake 5
     * ---------
     * Quick Select recurses into both halves.
     *
     * That becomes Quick Sort.
     *
     * Entire benefit disappears.
     *
     * ------------------------------------------------------------
     * Interview Trap
     * --------------
     *
     * Question:
     *
     * Why max heap instead of min heap?
     *
     * Correct answer:
     *
     * Because the removable candidate must
     * always be accessible in O(logK).
     *
     * The removable candidate is the farthest
     * among the accepted K points.
     */

    /*
     * ============================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Heap Typing Order
     * -----------------
     *
     * 1. Method signature
     *
     * 2. Create max heap
     *
     * 3. Iterate all points
     *
     * 4. Offer point
     *
     * 5. If size>K
     *      poll()
     *
     * 6. Build answer array
     *
     * 7. Return
     *
     * ------------------------------------------------------------
     * Quick Select Typing Order
     * ------------------------------------------------------------
     *
     * 1. quickSelect(...)
     *
     * 2. choose pivot
     *
     * 3. partition
     *
     * 4. compute left count
     *
     * 5. recurse one side only
     *
     * 6. copy first K
     */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * Heap
     * ----
     *
     * create maxHeap
     *
     * for point
     *     insert
     *     if size>K
     *         remove root
     *
     * collect answer
     *
     * return
     *
     * -------------------------
     *
     * Quick Select
     *
     * partition
     *
     * if leftSize==K
     *      stop
     *
     * recurse one side
     */

    /*
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    static final class BruteForce {

        /*
         * Idea
         * ----
         * Compute every distance.
         * Sort entire array.
         * Return first K.
         *
         * Invariant
         * ---------
         * Entire array remains globally sorted.
         *
         * Limitation
         * ----------
         * Solves more than required.
         *
         * Complexity
         * ----------
         * Time:
         * O(NlogN)
         *
         * Space:
         * Depends on sorting implementation.
         *
         * Interview Usefulness
         * --------------------
         * Good baseline only.
         */

        int[][] kClosest(int[][] points, int k) {
            Arrays.sort(points, Comparator.comparingInt(BruteForce::squareDistance));
            return Arrays.copyOfRange(points, 0, k);
        }

        private static int squareDistance(int[] point) {
            return point[0] * point[0] + point[1] * point[1];
        }
    }

    static final class MaxHeapSolution {

        /*
         * Idea
         * ----
         * Maintain only the best K points.
         *
         * Invariant
         * ---------
         * Root is always the farthest point
         * among accepted candidates.
         *
         * Improvement
         * -----------
         * Never stores unnecessary points.
         *
         * Complexity
         * ----------
         * Time:
         * O(NlogK)
         *
         * Space:
         * O(K)
         *
         * Interview Usefulness
         * --------------------
         * Preferred whenever
         * streaming data is possible.
         */

        public int[][] kClosest(int[][] points, int k) {

            PriorityQueue<int[]> maxHeap =
                    new PriorityQueue<>(
                            (a, b) -> Integer.compare(
                                    squareDistance(b),
                                    squareDistance(a)));

            for (int[] point : points) {

                // 🟢 Invariant:
                // heap stores candidate answers only.
                maxHeap.offer(point);

                // 🟢 Restore invariant immediately.
                if (maxHeap.size() > k) {
                    // Discard current worst candidate.
                    maxHeap.poll();
                }
            }

            int[][] answer = new int[k][2];

            while (k > 0) {
                // Heap already contains final candidates.
                answer[--k] = maxHeap.poll();
            }

            return answer;
        }

        private int squareDistance(int[] point) {
            return point[0] * point[0]
                    + point[1] * point[1];
        }
    }

    static final class QuickSelectSolution {

        /*
         * Idea
         * ----
         * Partition the array until the first K positions
         * contain exactly the K closest points.
         *
         * We never fully sort the array.
         *
         * --------------------------------------------------------
         * 🟢 Invariant
         * --------------------------------------------------------
         *
         * After every partition:
         *
         * left partition  -> distance <= pivot
         * pivot           -> final partition position
         * right partition -> distance > pivot
         *
         * Therefore only ONE partition can still contain the
         * K-th boundary.
         *
         * --------------------------------------------------------
         * Improvement
         * --------------------------------------------------------
         *
         * Average O(N)
         *
         * Avoids unnecessary sorting work.
         *
         * --------------------------------------------------------
         * Complexity
         * --------------------------------------------------------
         *
         * Average:
         * O(N)
         *
         * Worst:
         * O(N²)
         *
         * Space:
         * O(1) extra
         *
         * --------------------------------------------------------
         * Interview Usefulness
         * --------------------------------------------------------
         *
         * Best offline solution when modification of
         * the array is allowed.
         */

        public int[][] kClosest(int[][] points, int k) {

            quickSelect(points, 0, points.length - 1, k);

            return Arrays.copyOfRange(points, 0, k);
        }

        private void quickSelect(
                int[][] points,
                int left,
                int right,
                int k) {

            if (left >= right) {
                return;
            }

            int pivotIndex = partition(points, left, right);

            /*
             * Number of elements belonging to the
             * left partition INCLUDING pivot.
             */
            int leftCount = pivotIndex - left + 1;

            if (leftCount == k) {

                // 🟢 Exactly K elements finalized.
                return;
            }

            if (leftCount > k) {

                // Search Space shrinks to left half.
                quickSelect(
                        points,
                        left,
                        pivotIndex - 1,
                        k);

            } else {

                // Left side already accepted.
                // Continue searching remaining K.
                quickSelect(
                        points,
                        pivotIndex + 1,
                        right,
                        k - leftCount);
            }
        }

        private int partition(
                int[][] points,
                int left,
                int right) {

            int[] pivot = points[right];

            int nextSmaller = left;

            for (int i = left; i < right; i++) {

                /*
                 * 🟢 Invariant:
                 *
                 * [left, nextSmaller)
                 * contains only elements
                 * <= pivot.
                 */
                if (squareDistance(points[i])
                        <= squareDistance(pivot)) {

                    swap(points, nextSmaller, i);

                    nextSmaller++;
                }
            }

            /*
             * Pivot reaches its final partition
             * position.
             */
            swap(points, nextSmaller, right);

            return nextSmaller;
        }

        private void swap(
                int[][] points,
                int i,
                int j) {

            int[] temp = points[i];
            points[i] = points[j];
            points[j] = temp;
        }

        private int squareDistance(int[] point) {

            return point[0] * point[0]
                    + point[1] * point[1];
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * -------------------------
 * Heap Version
 * -------------------------
 *
 * "The Pattern is Top-K using a fixed-size max heap.
 *
 * The invariant is that the heap always stores the
 * K closest points seen so far.
 *
 * The root intentionally represents the farthest
 * among those accepted points.
 *
 * Every new point competes only against the root.
 *
 * If it is closer, the root is discarded.
 *
 * Otherwise the new point can never appear in the
 * final answer because an even closer candidate
 * already exists."
 *
 * ------------------------------------------------------------
 * Discard Rule
 * ------------------------------------------------------------
 *
 * Heap size exceeds K
 *
 * ->
 *
 * Remove maximum distance.
 *
 * ------------------------------------------------------------
 * Correctness
 * ------------------------------------------------------------
 *
 * Every discarded point was farther than every
 * surviving candidate at the time of removal.
 *
 * Since only K answers are required,
 * it can never re-enter the solution.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Every point processed once.
 *
 * Heap already stores exactly K answers.
 *
 * ------------------------------------------------------------
 * In-place?
 * ------------------------------------------------------------
 *
 * Heap:
 * No.
 *
 * Needs O(K) memory.
 *
 * Quick Select:
 * Yes.
 *
 * Rearranges input directly.
 *
 * ------------------------------------------------------------
 * Streaming?
 * ------------------------------------------------------------
 *
 * Heap:
 * Excellent.
 *
 * Quick Select:
 * Impossible.
 *
 * Requires complete array beforehand.
 *
 * ------------------------------------------------------------
 * When NOT to Use Heap
 * ------------------------------------------------------------
 *
 * Need globally sorted output.
 *
 * Need strict average O(N).
 *
 * ------------------------------------------------------------
 * When NOT to Use Quick Select
 * ------------------------------------------------------------
 *
 * Online stream.
 *
 * Immutable input.
 *
 * Worst-case guarantees required.
 */

/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 *
 * Top K
 *
 * K smallest
 *
 * K closest
 *
 * ------------------------------------------------------------
 * Pattern
 * ------------------------------------------------------------
 *
 * Fixed-size Max Heap
 *
 * OR
 *
 * Quick Select
 *
 * ------------------------------------------------------------
 * Invariant
 * ------------------------------------------------------------
 *
 * Heap always contains exactly the
 * K best candidates processed so far.
 *
 * ------------------------------------------------------------
 * Search Target
 * ------------------------------------------------------------
 *
 * Boundary between
 *
 * accepted
 *
 * rejected
 *
 * ------------------------------------------------------------
 * Discard Rule
 * ------------------------------------------------------------
 *
 * Heap:
 *
 * remove largest distance.
 *
 * Quick Select:
 *
 * discard entire partition.
 *
 * ------------------------------------------------------------
 * Common Trap
 * ------------------------------------------------------------
 *
 * Accidentally using min heap.
 *
 * ------------------------------------------------------------
 * Edge Cases
 * ------------------------------------------------------------
 *
 * K == 1
 *
 * K == N
 *
 * duplicate distances
 *
 * negative coordinates
 *
 * origin itself
 *
 * ------------------------------------------------------------
 * One-Liner
 * ------------------------------------------------------------
 *
 * Keep only the best K candidates.
 *
 * ------------------------------------------------------------
 * Re-Derivation Cue
 * ------------------------------------------------------------
 *
 * Ask:
 *
 * "Which point should be easiest
 * to remove?"
 *
 * Answer:
 *
 * The farthest accepted point.
 */

/*
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variation
 * ---------
 * K Largest Elements
 *
 * Change
 * ------
 *
 * Use fixed-size MIN heap.
 *
 * Why?
 *
 * Root becomes discard candidate.
 *
 * ------------------------------------------------------------
 * Variation
 * ------------------------------------------------------------
 *
 * K Closest Numbers
 *
 * Distance changes.
 *
 * Heap invariant unchanged.
 *
 * ------------------------------------------------------------
 * Variation
 * ------------------------------------------------------------
 *
 * K Nearest Restaurants
 *
 * Distance metric changes.
 *
 * Pattern unchanged.
 *
 * ------------------------------------------------------------
 * Variation
 * ------------------------------------------------------------
 *
 * K Weakest Rows
 *
 * Ranking function changes.
 *
 * Heap invariant preserved.
 *
 * ------------------------------------------------------------
 * Variation
 * ------------------------------------------------------------
 *
 * Top Frequent Elements
 *
 * Frequency replaces distance.
 *
 * Same Top-K invariant.
 *
 * ------------------------------------------------------------
 * Pattern Break
 * ------------------------------------------------------------
 *
 * Need complete ordering.
 *
 * Heap alone insufficient.
 *
 * ------------------------------------------------------------
 * Pattern Break
 * ------------------------------------------------------------
 *
 * Dynamic deletions.
 *
 * PriorityQueue cannot efficiently
 * remove arbitrary elements.
 *
 * Different data structure required.
 */

/*
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * □ What is the Pattern?
 *
 *   Top-K Elements
 *
 * ------------------------------------------------------------
 *
 * □ What is the Heap Invariant?
 *
 *   The heap always contains the K closest points processed
 *   so far.
 *
 *   The root is intentionally the farthest among them.
 *
 * ------------------------------------------------------------
 *
 * □ What is the Quick Select Invariant?
 *
 *   After partition:
 *
 *      left  <= pivot
 *      right > pivot
 *
 *   Only one side can still contain the K-boundary.
 *
 * ------------------------------------------------------------
 *
 * □ Search Space?
 *
 *   Heap:
 *      Processed points.
 *
 *   Quick Select:
 *      Current partition.
 *
 * ------------------------------------------------------------
 *
 * □ State?
 *
 *   Heap:
 *      Current best K candidates.
 *
 *   Quick Select:
 *      Current active partition.
 *
 * ------------------------------------------------------------
 *
 * □ Transition?
 *
 *   Heap:
 *      Insert
 *      ->
 *      Remove root if size>K
 *
 *   Quick Select:
 *      Partition
 *      ->
 *      Discard one side
 *
 * ------------------------------------------------------------
 *
 * □ Discard Rule?
 *
 *   Heap:
 *      Remove largest accepted distance.
 *
 *   Quick Select:
 *      Remove partition that cannot contain K.
 *
 * ------------------------------------------------------------
 *
 * □ Termination?
 *
 *   Heap:
 *      Every point processed.
 *
 *   Quick Select:
 *      Partition boundary equals K.
 *
 * ------------------------------------------------------------
 *
 * □ Why does sorting waste work?
 *
 *   Sorting establishes ordering between every pair
 *   of elements.
 *
 *   We only require the first K.
 *
 * ------------------------------------------------------------
 *
 * □ Why compare squared distance?
 *
 *   sqrt(x)
 *   preserves ordering.
 *
 *   Therefore:
 *
 *      x
 *
 *   is sufficient.
 *
 * ------------------------------------------------------------
 *
 * □ Heap Debug Checklist
 *
 *   □ Comparator reversed?
 *
 *   □ Heap size ever exceeds K?
 *
 *   □ Poll after offer?
 *
 *   □ Root really represents farthest point?
 *
 *   □ Distance overflow?
 *
 * ------------------------------------------------------------
 *
 * □ Quick Select Debug Checklist
 *
 *   □ Pivot chosen correctly?
 *
 *   □ Partition invariant preserved?
 *
 *   □ Pivot swapped back?
 *
 *   □ Recurse only ONE side?
 *
 *   □ K adjusted after right recursion?
 *
 * ------------------------------------------------------------
 *
 * □ Pattern Boundary
 *
 *   Heap excels when:
 *
 *      online
 *      streaming
 *      unknown N
 *
 *   Quick Select excels when:
 *
 *      offline
 *      mutable array
 *      average linear time
 *
 * ------------------------------------------------------------
 *
 * □ Interview Summary
 *
 *   Heap:
 *
 *      Keep only K winners.
 *
 *   Quick Select:
 *
 *      Find only the boundary,
 *      not the complete order.
 */

/*
 * ============================================================
 * ⚫ PATTERN MAPPING
 * ============================================================
 *
 * Top-K Pattern Family
 *
 * ---------------------------------------
 * Problem
 * ---------------------------------------
 *
 * K Closest Points
 *
 * Metric:
 * Squared distance
 *
 * Heap:
 * Max Heap
 *
 * ---------------------------------------
 *
 * K Largest Element
 *
 * Metric:
 * Value
 *
 * Heap:
 * Min Heap
 *
 * ---------------------------------------
 *
 * Top K Frequent
 *
 * Metric:
 * Frequency
 *
 * Heap:
 * Min Heap
 *
 * ---------------------------------------
 *
 * K Weakest Rows
 *
 * Metric:
 * Soldier count
 *
 * Heap:
 * Max Heap
 *
 * ---------------------------------------
 *
 * K Closest Numbers
 *
 * Metric:
 * Absolute difference
 *
 * Heap:
 * Max Heap
 *
 * ---------------------------------------
 *
 * Kth Largest
 *
 * Metric:
 * Value
 *
 * Heap:
 * Min Heap
 *
 * ---------------------------------------
 *
 * Kth Smallest
 *
 * Metric:
 * Value
 *
 * Heap:
 * Max Heap
 *
 * ============================================================
 * IMPLEMENTATION RECONSTRUCTION
 * ============================================================
 *
 * Heap Reconstruction
 *
 * Step 1
 * -------
 * Create max heap using distance comparator.
 *
 * Step 2
 * -------
 * Traverse every point.
 *
 * Step 3
 * -------
 * Offer point.
 *
 * Step 4
 * -------
 * If heap size>K
 * remove root.
 *
 * Step 5
 * -------
 * Pop everything into answer array.
 *
 * ------------------------------------------------------------
 *
 * Quick Select Reconstruction
 *
 * Step 1
 * -------
 * Choose pivot.
 *
 * Step 2
 * -------
 * Partition.
 *
 * Step 3
 * -------
 * Count left partition.
 *
 * Step 4
 * -------
 * Compare with K.
 *
 * Step 5
 * -------
 * Recurse exactly one side.
 *
 * Step 6
 * -------
 * Copy first K elements.
 *
 * ============================================================
 * COMPLEXITY TABLE
 * ============================================================
 *
 * Brute Force
 * -----------
 * Time:
 * O(N log N)
 *
 * Space:
 * O(log N) (sorting recursion) or implementation dependent
 *
 * Stable:
 * Depends on sort
 *
 * Online:
 * No
 *
 * ------------------------------------------------------------
 *
 * Max Heap
 * --------
 * Time:
 * O(N log K)
 *
 * Space:
 * O(K)
 *
 * Stable:
 * No
 *
 * Online:
 * Yes
 *
 * ------------------------------------------------------------
 *
 * Quick Select
 * ------------
 * Average:
 * O(N)
 *
 * Worst:
 * O(N²)
 *
 * Space:
 * O(1)
 *
 * Online:
 * No
 *
 * ============================================================
 * COMMON INTERVIEW QUESTIONS
 * ============================================================
 *
 * Q:
 * Why compare squared distances?
 *
 * A:
 * Square root is monotonic, so ordering is unchanged.
 *
 * ------------------------------------------------------------
 *
 * Q:
 * Why use a max heap?
 *
 * A:
 * Because the removable candidate must always be the
 * farthest accepted point.
 *
 * ------------------------------------------------------------
 *
 * Q:
 * Why is heap size fixed?
 *
 * A:
 * Otherwise complexity becomes O(N log N).
 *
 * ------------------------------------------------------------
 *
 * Q:
 * Why is Quick Select faster?
 *
 * A:
 * It discards half of the remaining search space after
 * every partition instead of ordering everything.
 *
 * ------------------------------------------------------------
 *
 * Q:
 * Can Quick Select process streams?
 *
 * A:
 * No.
 *
 * The entire array must already exist.
 *
 * ------------------------------------------------------------
 *
 * Q:
 * Which approach would you choose in production?
 *
 * A:
 * Streaming data:
 * Max Heap.
 *
 * Static dataset:
 * Quick Select.
 */

    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    private static int squaredDistance(int[] point) {
        return point[0] * point[0] + point[1] * point[1];
    }

    private static void assertContainsPoint(int[][] points, int x, int y) {
        for (int[] point : points) {
            if (point[0] == x && point[1] == y) {
                return;
            }
        }
        throw new AssertionError(
                "Expected point [" + x + "," + y + "] not found.");
    }

    private static void assertEveryPointWithinDistance(
            int[][] result,
            int maxSquaredDistance) {

        for (int[] point : result) {
            assert squaredDistance(point) <= maxSquaredDistance
                    : "Point farther than expected.";
        }
    }

    public static void main(String[] args) {

        MaxHeapSolution heap = new MaxHeapSolution();
        QuickSelectSolution quick = new QuickSelectSolution();

        /*
         * ---------------------------------------------------------
         * Happy Path
         * ---------------------------------------------------------
         */

        int[][] points1 = {
                {1, 3},
                {-2, 2}
        };

        int[][] heapAnswer1 =
                heap.kClosest(
                        Arrays.stream(points1)
                                .map(int[]::clone)
                                .toArray(int[][]::new),
                        1);

        int[][] quickAnswer1 =
                quick.kClosest(
                        Arrays.stream(points1)
                                .map(int[]::clone)
                                .toArray(int[][]::new),
                        1);

        // Only closest point should remain.
        assertContainsPoint(heapAnswer1, -2, 2);
        assertContainsPoint(quickAnswer1, -2, 2);

        /*
         * ---------------------------------------------------------
         * Representative Example
         * ---------------------------------------------------------
         */

        int[][] points2 = {
                {3, 3},
                {5, -1},
                {-2, 4}
        };

        int[][] heapAnswer2 =
                heap.kClosest(
                        Arrays.stream(points2)
                                .map(int[]::clone)
                                .toArray(int[][]::new),
                        2);

        int[][] quickAnswer2 =
                quick.kClosest(
                        Arrays.stream(points2)
                                .map(int[]::clone)
                                .toArray(int[][]::new),
                        2);

        // Returned points must all be within squared distance 20.
        assertEveryPointWithinDistance(heapAnswer2, 20);
        assertEveryPointWithinDistance(quickAnswer2, 20);

        /*
         * ---------------------------------------------------------
         * Edge Case
         * K == N
         * ---------------------------------------------------------
         */

        int[][] points3 = {
                {2, 2},
                {3, 3},
                {-1, -1}
        };

        assert heap.kClosest(
                Arrays.stream(points3)
                        .map(int[]::clone)
                        .toArray(int[][]::new),
                3).length == 3;

        assert quick.kClosest(
                Arrays.stream(points3)
                        .map(int[]::clone)
                        .toArray(int[][]::new),
                3).length == 3;

        /*
         * ---------------------------------------------------------
         * Boundary
         * Origin included.
         * ---------------------------------------------------------
         */

        int[][] points4 = {
                {0, 0},
                {10, 10},
                {-5, -5}
        };

        int[][] heapAnswer4 =
                heap.kClosest(
                        Arrays.stream(points4)
                                .map(int[]::clone)
                                .toArray(int[][]::new),
                        1);

        int[][] quickAnswer4 =
                quick.kClosest(
                        Arrays.stream(points4)
                                .map(int[]::clone)
                                .toArray(int[][]::new),
                        1);

        assertContainsPoint(heapAnswer4, 0, 0);
        assertContainsPoint(quickAnswer4, 0, 0);

        /*
         * ---------------------------------------------------------
         * Duplicate distances.
         * Order is irrelevant.
         * ---------------------------------------------------------
         */

        int[][] points5 = {
                {1, 1},
                {-1, -1},
                {2, 2},
                {-2, -2}
        };

        assert heap.kClosest(
                Arrays.stream(points5)
                        .map(int[]::clone)
                        .toArray(int[][]::new),
                2).length == 2;

        assert quick.kClosest(
                Arrays.stream(points5)
                        .map(int[]::clone)
                        .toArray(int[][]::new),
                2).length == 2;

        /*
         * ---------------------------------------------------------
         * Interview Trap
         * Negative coordinates.
         * ---------------------------------------------------------
         */

        int[][] points6 = {
                {-8, -8},
                {-1, 2},
                {-3, -4},
                {9, 9}
        };

        int[][] heapAnswer6 =
                heap.kClosest(
                        Arrays.stream(points6)
                                .map(int[]::clone)
                                .toArray(int[][]::new),
                        2);

        int[][] quickAnswer6 =
                quick.kClosest(
                        Arrays.stream(points6)
                                .map(int[]::clone)
                                .toArray(int[][]::new),
                        2);

        assert heapAnswer6.length == 2;
        assert quickAnswer6.length == 2;

        /*
         * ---------------------------------------------------------
         * Single element input.
         * ---------------------------------------------------------
         */

        int[][] single = {
                {7, -3}
        };

        assert heap.kClosest(
                Arrays.stream(single)
                        .map(int[]::clone)
                        .toArray(int[][]::new),
                1).length == 1;

        assert quick.kClosest(
                Arrays.stream(single)
                        .map(int[]::clone)
                        .toArray(int[][]::new),
                1).length == 1;

        System.out.println("All assertions passed.");
    }

}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/