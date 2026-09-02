package org.chijai.day1.Arrays.session4.Intervals;

import java.util.Arrays;
import java.util.Comparator;

/**
 * INTERVAL PATTERN 2 — GREEDY BY END
 *
 * LEARNING PROGRESSION
 * --------------------
 * 1. Activity Selection
 *      canonical problem -> derive earliest-finish greedy.
 *
 * 2. Non-overlapping Intervals
 *      same compatible-set engine -> transform output to n - kept.
 *      https://leetcode.com/problems/non-overlapping-intervals/
 *
 * 3. Minimum Arrows
 *      same earliest-end principle -> different objective and state.\
 *
 *      SORT BY END
 *
 * boundary = first useful greedy boundary
 *
 * for each interval:
 *
 *     if current interval cannot work with boundary:
 *         perform required action
 *         boundary = current.end
 *
 * STUDY-FILE RULE
 * ---------------
 * Keep the problem close to its real interview / LeetCode representation.
 * Use int[][] here because that is what the canonical LeetCode interval problems use.
 *
 * Do not introduce a custom Interval model unless it adds real domain value.
 * Otherwise it creates a translation step that does not exist in the problem.
 */
public class IntervalGreedyByEnd {

    // =========================================================================
    // 1) ACTIVITY SELECTION — CANONICAL ENGINE
    // =========================================================================

    /*
     * PROBLEM STATEMENT
     * -----------------
     * You are given activities where:
     *
     *      activities[i] = [start, end]
     *
     * An activity occupies the interval from start to end.
     *
     * Choose the maximum number of mutually compatible activities.
     *
     * In this file, touching activities are compatible:
     *
     *      [1,3] and [3,5]
     *
     * can both be selected.
     *
     * Example:
     *
     *      activities =
     *      [[1,4], [3,5], [0,6], [5,7], [8,9], [5,9]]
     *
     * One optimal selection:
     *
     *      [1,4], [5,7], [8,9]
     *
     * Output:
     *
     *      3
     *
     * QUESTION THAT MATTERS
     * ---------------------
     * Which current choice leaves the most freedom for future choices?
     */

    // =========================================================================
    // NATURAL SEARCH -> GREEDY QUESTION
    // =========================================================================

    /*
     * Natural exhaustive idea:
     *
     *      for every interval:
     *          TAKE
     *          or
     *          SKIP
     *
     * This creates O(2^n) subsets in the worst case.
     *
     * So ask:
     *
     *      Is there a locally safe choice that lets us avoid branching?
     */

    // =========================================================================
    // FAILED INSTINCTS
    // =========================================================================

    /*
     * EARLIEST START FAILS
     * --------------------
     *
     *      [1,10], [2,3], [3,4], [4,5], [5,6], [6,7]
     *
     * earliest start:
     *      [1,10] -> count 1
     *
     * earliest finish:
     *      [2,3], [3,4], [4,5], [5,6], [6,7] -> count 5
     *
     *
     * SHORTEST DURATION FAILS
     * -----------------------
     *
     *      [0,3], [2,4], [3,6]
     *
     * shortest:
     *      [2,4] -> blocks both others -> count 1
     *
     * earliest finish:
     *      [0,3], [3,6] -> count 2
     *
     * Duration measures interval SIZE.
     * End time measures when the FUTURE becomes available.
     */

    // =========================================================================
    // FIRST PRINCIPLES — DERIVE SORT BY END
    // =========================================================================

    /*
     * Once I accept an interval, no compatible future interval can begin
     * before that interval ends.
     *
     * Therefore:
     *
     *      maximize future choices
     *              ↓
     *      make timeline available sooner
     *              ↓
     *      choose earliest-finishing interval
     *              ↓
     *          SORT BY END
     *
     * This derives the candidate greedy choice.
     * The exchange argument below proves it is safe.
     */

    /*
     * VISUAL — WHY END TIME MATTERS
     * =============================
     *
     * time
     * 1   2   3   4   5   6   7   8   9   10
     * |---|---|---|---|---|---|---|---|---|
     *
     * [===================================]   [1,10]
     *
     *     [===]                               [2,3]
     *         [===]                           [3,4]
     *             [===]                       [4,5]
     *                 [===]                   [5,6]
     *                     [===]               [6,7]
     *
     * choose [1,10]:
     *
     *      ───────────────────────────────X
     *                                      future becomes free
     *
     * choose [2,3]:
     *
     *          ───X
     *              much more future remains
     *
     * Memory picture:
     *
     *      END = boundary after which future choices become possible.
     */

    // =========================================================================
    // PRIMARY SOLUTION — ACTIVITY SELECTION
    // =========================================================================

    static final class ActivitySelection {

        public int maximumActivities(int[][] activities) {
            if (activities == null || activities.length == 0) {
                return 0;
            }

            Arrays.sort(
                    activities,
                    Comparator.comparingInt(activity -> activity[1])
            );

            int selected = 0;
            int lastFinish = Integer.MIN_VALUE;

            for (int[] current : activities) {
                if (current[0] >= lastFinish) {
                    selected++;
                    lastFinish = current[1];
                }
            }

            return selected;
        }
    }

    /*
     * STATE / CHOICE / UPDATE
     * -----------------------
     * STATE:
     *      lastFinish = end of the most recently accepted interval.
     *
     * CHOICE:
     *      current.start >= lastFinish -> TAKE
     *      otherwise                  -> SKIP
     *
     * UPDATE AFTER TAKE:
     *      selected++
     *      lastFinish = current.end
     *
     * Why is one number enough?
     *      Future compatibility depends only on this boundary.
     */

    /*
     * DRY RUN
     * -------
     * Sorted by end:
     *
     *      [1,4] [3,5] [0,6] [5,7] [8,9] [5,9]
     *
     * current   lastFinish before   action   lastFinish after
     * -------------------------------------------------------
     * [1,4]     -INF                TAKE     4
     * [3,5]      4                  SKIP     4
     * [0,6]      4                  SKIP     4
     * [5,7]      4                  TAKE     7
     * [8,9]      7                  TAKE     9
     * [5,9]      9                  SKIP     9
     *
     * selected = 3
     */

    /*
     * INVARIANT
     * ---------
     * After scanning a prefix of intervals sorted by end:
     *
     *      - selected intervals are mutually compatible;
     *      - lastFinish is the end of the latest selected interval.
     *
     * EXCHANGE PROOF
     * --------------
     * Let G be the earliest-finishing compatible interval.
     * Suppose an optimal solution chooses O instead.
     *
     *      G.end <= O.end
     *
     * Replace O with G.
     *
     * Any later interval that could follow O can still follow G,
     * because G frees the timeline no later.
     *
     * Therefore some optimal solution begins with G.
     * Repeat on the remaining timeline.
     *
     * Equal reward (+1 per interval) makes this maximum cardinality.
     * The exchange property—not equal reward alone—proves greedy correctness.
     */

    // =========================================================================
    // ACTIVITY SELECTION — MUTATION MICROSCOPE
    // =========================================================================

    /*
     * ORIGINAL CORE
     * -------------
     *      "maximum NUMBER of mutually compatible activities"
     *      "every selected activity contributes the same unit value"
     *      "all activities are known beforehand"
     *
     * These conditions support:
     *
     *      maximum cardinality
     *          + offline ordering
     *          + earliest-finish exchange argument
     *          -> greedy by END.
     *
     *
     * CHANGE 1 — "maximum NUMBER" -> "maximum PROFIT / VALUE"
     * --------------------------------------------------------
     *
     * Example:
     *      activity = [start, end, profit]
     *
     * What changed?
     *      One activity is no longer worth the same as another.
     *
     * What breaks?
     *      Maximum count != maximum value.
     *      Earliest finish can discard a highly valuable interval.
     *
     * Pattern shift:
     *      Weighted Interval Scheduling
     *      -> sort by end
     *      -> find previous compatible interval
     *      -> TAKE / SKIP DP
     *      -> often binary search for predecessor.
     *
     * Retrieval signal:
     *      COUNT -> WEIGHT / PROFIT
     *      usually means the objective needs more state.
     *
     *
     * CHANGE 2 — "maximum count" -> "return the selected activities"
     * ----------------------------------------------------------------
     *
     * What changed?
     *      Output only.
     *
     * What stays?
     *      Greedy choice, proof, and complexity.
     *
     * New state:
     *      store each accepted interval.
     *
     * Retrieval signal:
     *      COUNT ONLY -> RECONSTRUCT CHOICES
     *      often preserves the algorithm.
     *
     *
     * CHANGE 3 — "touching is compatible" -> "touching overlaps"
     * -----------------------------------------------------------
     *
     * Original:
     *      current.start >= lastFinish
     *
     * Changed semantics:
     *      current.start > lastFinish
     *
     * Pattern:
     *      same greedy.
     *
     * Retrieval signal:
     *      endpoint wording may change only the inequality,
     *      not the algorithm.
     *
     *
     * CHANGE 4 — "all activities are known" -> "activities arrive online"
     * --------------------------------------------------------------------
     *
     * What breaks?
     *      We cannot sort the complete input once by end.
     *
     * New requirement:
     *      maintain useful ordering dynamically.
     *
     * Pattern shift:
     *      depends on the exact query/update contract;
     *      ordered structures / heaps / dynamic scheduling may be needed.
     *
     * Retrieval signal:
     *      OFFLINE -> ONLINE
     *      re-check every solution whose first step is "sort everything."
     *
     *
     * CHANGE 5 — "choose maximum count" -> "choose exactly / at most k"
     * ------------------------------------------------------------------
     *
     * If the goal is still only count:
     *      "at most k" can be trivial: min(k, maximumCompatibleCount).
     *
     * But if another objective is added:
     *
     *      "choose at most k activities with maximum profit"
     *
     * then:
     *      remaining choices k becomes part of the state
     *      -> DP / constrained optimization.
     *
     * Retrieval signal:
     *      adding a BUDGET / LIMIT matters only when it changes the objective
     *      or future feasibility.
     *
     *
     * CHANGE 6 — "one resource" -> "k rooms / k machines"
     * ---------------------------------------------------
     *
     * Original:
     *      selected intervals may not overlap at all.
     *
     * Changed:
     *      up to k intervals may run simultaneously.
     *
     * What breaks?
     *      One lastFinish no longer describes feasibility.
     *
     * New information:
     *      multiple active ending times.
     *
     * Pattern shift:
     *      heap / sweep line / resource allocation.
     *
     * Retrieval signal:
     *      ONE active boundary -> MANY active boundaries
     *      often means heap / sweep.
     */

    // =========================================================================
    // 2) NON-OVERLAPPING INTERVALS — SAME ENGINE, OUTPUT TRANSFORMATION
    // =========================================================================

    /*
     * PROBLEM STATEMENT
     * -----------------
     * You are given:
     *
     *      intervals[i] = [start, end]
     *
     * Return the minimum number of intervals that must be removed
     * so the remaining intervals do not overlap.
     *
     * Touching endpoints are allowed:
     *
     *      [1,2] and [2,3]
     *
     * are compatible.
     *
     * Example:
     *
     *      intervals =
     *      [[1,2], [2,3], [3,4], [1,3]]
     *
     * Keep:
     *      [1,2], [2,3], [3,4]
     *
     * Remove:
     *      [1,3]
     *
     * Output:
     *      1
     *
     * WORDING TRANSFORMATION
     * ----------------------
     * The problem says:
     *
     *      MINIMUM REMOVE
     *
     * but:
     *
     *      removed = total - kept
     *
     * total is fixed, therefore:
     *
     *      minimize removed
     *          ==
     *      maximize compatible kept
     *
     * That is exactly Activity Selection.
     */

    static final class NonOverlappingIntervals {

        public int eraseOverlapIntervals(int[][] intervals) {
            if (intervals == null || intervals.length <= 1) {
                return 0;
            }

            Arrays.sort(
                    intervals,
                    Comparator.comparingInt(interval -> interval[1])
            );

            int kept = 0;
            int lastFinish = Integer.MIN_VALUE;

            for (int[] current : intervals) {
                if (current[0] >= lastFinish) {
                    kept++;
                    lastFinish = current[1];
                }
            }

            return intervals.length - kept;
        }
    }

    /*
     * TRANSFER DRY RUN
     * ----------------
     *
     *      total = 4
     *      maximum compatible kept = 3
     *
     *      removed = 4 - 3 = 1
     *
     * No new greedy algorithm was invented.
     * The output wording was transformed into the canonical problem.
     */

    // =========================================================================
    // NON-OVERLAPPING INTERVALS — MUTATION MICROSCOPE
    // =========================================================================

    /*
     * ORIGINAL CORE
     * -------------
     *      "minimum NUMBER of intervals to REMOVE"
     *      "remaining intervals must be pairwise non-overlapping"
     *
     * Key transformation:
     *
     *      removed = total - kept
     *
     * Since every removed interval costs the same unit 1:
     *
     *      minimize removed
     *          ==
     *      maximize compatible kept
     *
     *      -> Activity Selection
     *      -> greedy by END.
     *
     *
     * CHANGE 1 — "minimum NUMBER removed" -> "minimum COST removed"
     * ----------------------------------------------------------------
     *
     * Suppose each interval has a removal cost.
     *
     * What changed?
     *      Removing one interval is no longer equivalent to removing another.
     *
     * Transform:
     *
     *      minimize removed cost
     *          ==
     *      maximize value/cost of intervals kept
     *
     * What breaks?
     *      Maximum cardinality greedy no longer solves maximum kept value.
     *
     * Pattern shift:
     *      Weighted Interval Scheduling / DP.
     *
     * Retrieval signal:
     *      NUMBER -> COST
     *      unit objective became weighted.
     *
     *
     * CHANGE 2 — "return minimum count" -> "return intervals to remove"
     * -----------------------------------------------------------------
     *
     * What changed?
     *      Output only.
     *
     * Pattern:
     *      same greedy.
     *
     * New state:
     *      record kept or removed intervals.
     *
     *
     * CHANGE 3 — "no overlaps allowed" -> "at most k may overlap"
     * ----------------------------------------------------------------
     *
     * Original:
     *      compatibility is binary:
     *      next.start >= lastFinish.
     *
     * Changed:
     *      several intervals may coexist.
     *
     * What breaks?
     *      lastFinish is insufficient.
     *
     * New state:
     *      active intervals / their end times.
     *
     * Pattern shift:
     *      sweep line / min-heap,
     *      possibly optimization on top if removals are still minimized.
     *
     * Retrieval signal:
     *      PAIRWISE NON-OVERLAP -> BOUNDED OVERLAP
     *      changes the state from one boundary to an active set.
     *
     *
     * CHANGE 4 — "minimum removals" -> "minimum intervals to edit / move"
     * -------------------------------------------------------------------
     *
     * What changed?
     *      We are no longer only selecting a subset.
     *      An interval itself may be modified.
     *
     * What breaks?
     *      n - maximum kept no longer captures the action space.
     *
     * Pattern shift:
     *      depends on allowed edit:
     *          DP / greedy / optimization.
     *
     * Retrieval signal:
     *      REMOVE -> MODIFY
     *      changes the legal decisions, so re-derive from scratch.
     *
     *
     * CHANGE 5 — intervals already sorted by END
     * -------------------------------------------
     *
     * What changed?
     *      preprocessing guarantee.
     *
     * Pattern:
     *      same greedy.
     *
     * Complexity:
     *      O(n) scan instead of O(n log n).
     *
     * Retrieval signal:
     *      an ordering guarantee may remove a data-organization step
     *      without changing the core algorithm.
     *
     *
     * CHANGE 6 — online insert/delete + query minimum removals
     * --------------------------------------------------------
     *
     * What breaks?
     *      static sorted scan cannot be recomputed cheaply after every update.
     *
     * Pattern shift:
     *      dynamic ordered structure / interval data structure,
     *      depending on required update/query complexity.
     *
     * Retrieval signal:
     *      STATIC -> DYNAMIC
     *      turns a one-pass invariant into a maintained invariant.
     */

    // =========================================================================
    // 3) MINIMUM NUMBER OF ARROWS TO BURST BALLOONS — LEETCODE 452
    // =========================================================================

    /*
     * PROBLEM STATEMENT — CLOSE TO THE ORIGINAL
     * -----------------------------------------
     * Some spherical balloons are taped to a flat wall representing the XY-plane.
     *
     *      points[i] = [xStart, xEnd]
     *
     * describes the horizontal diameter of one balloon.
     * Its exact y-coordinate is unknown.
     *
     * An arrow is fired vertically upward from some x-coordinate.
     *
     * A balloon is burst when:
     *
     *      xStart <= x <= xEnd
     *
     * An arrow keeps travelling upward and can therefore burst every balloon
     * whose horizontal interval contains that x-coordinate.
     *
     * There is no limit on how many arrows may be fired.
     *
     * Return the MINIMUM number of arrows required to burst all balloons.
     *
     * Important constraints / semantics:
     *
     *      1 <= points.length
     *      points[i].length == 2
     *      xStart < xEnd
     *      coordinates may span the full int range
     *      intervals are CLOSED because equality bursts the balloon.
     *
     * Example:
     *
     *      points =
     *      [[10,16], [2,8], [1,6], [7,12]]
     *
     * One arrow at x = 6 bursts:
     *      [1,6], [2,8]
     *
     * One arrow at x = 12 bursts:
     *      [7,12], [10,16]
     *
     * Output:
     *      2
     *
     * LEETCODE INTERFACE
     * ------------------
     * class Solution {
     *     public int findMinArrowShots(int[][] points) {
     *         ...
     *     }
     * }
     *
     * The method below intentionally uses the same parameter type and method name,
     * so moving it into LeetCode requires no data-model conversion.
     */

    static final class MinimumArrows {

        public int findMinArrowShots(int[][] points) {
            if (points == null || points.length == 0) {
                return 0;
            }

            Arrays.sort(
                    points,
                    Comparator.comparingInt(point -> point[1])
            );

            int arrows = 1;
            int arrowPosition = points[0][1];

            for (int i = 1; i < points.length; i++) {
                if (arrowPosition < points[i][0]) {
                    arrows++;
                    arrowPosition = points[i][1];
                }
            }

            return arrows;
        }
    }

    /*
     * WHY:
     *
     *      int arrows = 1;
     *      int arrowPosition = points[0][1];
     *
     * ?
     * -------------------------------------------------------------------------
     *
     * We already handled the empty case.
     *
     * Therefore at least one balloon exists.
     * Any non-empty set of balloons needs at least one arrow.
     *
     * After sorting by end, points[0] is the earliest-ending balloon.
     *
     * Greedy says:
     *
     *      place the first arrow at that balloon's right endpoint.
     *
     * Therefore the initial state is naturally:
     *
     *      arrows = 1
     *      arrowPosition = points[0][1]
     *
     * The loop begins at i = 1 because points[0] has already created
     * and been covered by the first arrow.
     *
     * This is not an arbitrary special case.
     * It directly represents the first greedy decision.
     */

    /*
     * WHY PLACE AN ARROW AT THE EARLIEST END?
     * ---------------------------------------
     * Consider the earliest-ending uncovered balloon.
     *
     * Every valid solution must shoot somewhere inside it.
     *
     * Moving that shot rightward to its end:
     *
     *      - still bursts that balloon;
     *      - goes as far right as possible while remaining inside it;
     *      - therefore maximizes the chance of also covering later balloons.
     *
     * So an optimal solution exists with an arrow at that end.
     */

    /*
     * STATE / CHOICE / UPDATE
     * -----------------------
     * STATE:
     *      arrowPosition = x-coordinate of the current arrow.
     *
     * CHOICE:
     *      points[i][0] <= arrowPosition
     *          -> current balloon contains the arrow -> already covered.
     *
     *      arrowPosition < points[i][0]
     *          -> current arrow lies before the balloon -> need a new arrow.
     *
     * UPDATE:
     *      arrows++
     *      arrowPosition = points[i][1]
     */

    /*
     * DRY RUN
     * -------
     * Sorted:
     *
     *      [1,6] [2,8] [7,12] [10,16]
     *
     * Initialization:
     *
     *      arrows = 1
     *      arrowPosition = 6
     *
     * current    arrow before   arrow < start?   action       arrow after
     * -------------------------------------------------------------------
     * [2,8]      6              no               COVERED      6
     * [7,12]     6              yes              NEW ARROW    12
     * [10,16]    12             no               COVERED      12
     *
     * answer = 2
     */

    /*
     * ENDPOINT SEMANTICS
     * ------------------
     * The statement says:
     *
     *      xStart <= x <= xEnd
     *
     * Therefore if:
     *
     *      points[i][0] == arrowPosition
     *
     * the balloon IS still burst.
     *
     * New arrow only when:
     *
     *      arrowPosition < points[i][0]
     *
     * The inequality comes from the problem statement.
     * Do not memorize > independently.
     */

    // =========================================================================
    // SAME EQUALITY CASE — OPPOSITE ACTION
    // =========================================================================

    /*
     * Do NOT memorize >= versus > independently.
     * Derive the action from the statement semantics.
     *
     * ACTIVITY SELECTION / NON-OVERLAPPING
     * ------------------------------------
     *
     * Question:
     *      Can the next interval START exactly when the previous one ENDS?
     *
     * In these problems:
     *      YES.
     *
     * Therefore:
     *
     *      current.start == lastFinish
     *          -> compatible
     *          -> TAKE it.
     *
     * Code:
     *
     *      current.start >= lastFinish
     *
     *
     * MINIMUM ARROWS
     * --------------
     *
     * Question:
     *      Does an arrow exactly at xStart still burst the balloon?
     *
     * Statement:
     *
     *      xStart <= x <= xEnd
     *
     * Therefore:
     *
     *      point.start == arrowPosition
     *          -> already covered
     *          -> DO NOT fire another arrow.
     *
     * A new arrow is needed only when:
     *
     *      arrowPosition < point.start
     *
     *
     * SAME equality:
     *
     *      start == boundary
     *
     * but DIFFERENT action:
     *
     *      Activity -> TAKE
     *      Arrow    -> KEEP SAME ARROW
     *
     * Why?
     *      The boundary represents different things:
     *
     *      lastFinish    -> when a selected interval stops occupying time.
     *      arrowPosition -> a point that already hits every interval containing it.
     */

    // =========================================================================
    // MINIMUM ARROWS — MUTATION MICROSCOPE
    // =========================================================================

    /*
     * GOAL
     * ----
     * Five months later, do not remember:
     *
     *      "Minimum Arrows = sort by end."
     *
     * Reconstruct from the statement.
     *
     * Ask:
     *
     *      What exactly am I minimizing?
     *      What does one arrow affect?
     *      Are intervals static or changing?
     *      Are coordinates bounded?
     *      Do arrows have restrictions / costs?
     *      Do I need only the count or the actual positions?
     *
     *
     * ORIGINAL STATEMENT CORE
     * -----------------------
     *
     *      "minimum number of arrows"
     *      "arrow at x bursts EVERY balloon with xStart <= x <= xEnd"
     *      "arrows can be shot at ANY x"
     *      "all balloons are known beforehand"
     *      "every arrow has the SAME cost"
     *
     * These words create the greedy structure:
     *
     *      one point can cover many intervals
     *              +
     *      every point costs the same
     *              +
     *      all intervals known offline
     *              ↓
     *      place a point at earliest uncovered END.
     *
     *
     * -------------------------------------------------------------------------
     * TINY CHANGE 1
     * -------------------------------------------------------------------------
     *
     * ORIGINAL:
     *      "minimum number of arrows"
     *
     * CHANGED:
     *      "each arrow position x has a different COST;
     *       minimize total cost"
     *
     * WHAT BROKE?
     *      One arrow is no longer equivalent to another.
     *      "use as few arrows as possible" became "choose cheapest coverage."
     *
     * NEW INFORMATION:
     *      cost of the chosen point matters.
     *
     * PATTERN SHIFT:
     *      greedy-by-end is not sufficient in general.
     *      Depending on how costs are defined:
     *          -> DP
     *          -> weighted covering
     *          -> shortest-path / optimization formulation.
     *
     * RETRIEVAL SIGNAL:
     *      COUNT became WEIGHT / COST -> suspect DP / weighted optimization.
     *
     *
     * -------------------------------------------------------------------------
     * TINY CHANGE 2
     * -------------------------------------------------------------------------
     *
     * ORIGINAL:
     *      "an arrow at x bursts every balloon containing x"
     *
     * CHANGED:
     *      "an arrow can burst at most ONE balloon"
     *
     * WHAT BROKE?
     *      Shared coverage disappeared.
     *
     * CONSEQUENCE:
     *      There is no interval-grouping problem anymore.
     *
     * ANSWER:
     *      number of balloons.
     *
     * RETRIEVAL SIGNAL:
     *      Remove the sharing effect -> the greedy structure may disappear entirely.
     *
     *
     * -------------------------------------------------------------------------
     * TINY CHANGE 3
     * -------------------------------------------------------------------------
     *
     * ORIGINAL:
     *      "an arrow keeps traveling upward infinitely"
     *
     * CHANGED:
     *      "an arrow can burst at most k balloons"
     *
     * WHAT BROKE?
     *      One chosen x no longer covers its entire overlap group.
     *
     * NEW STATE:
     *      how many balloons this arrow has already consumed.
     *
     * PATTERN SHIFT:
     *      no longer the plain interval-stabbing greedy.
     *      Depending on the exact rules:
     *          -> greedy with capacity bookkeeping,
     *          -> heap,
     *          -> flow / matching,
     *          -> DP.
     *
     * RETRIEVAL SIGNAL:
     *      Add CAPACITY to a previously unlimited action -> state usually expands.
     *
     *
     * -------------------------------------------------------------------------
     * TINY CHANGE 4
     * -------------------------------------------------------------------------
     *
     * ORIGINAL:
     *      "arrows can be shot from ANY x-coordinate"
     *
     * CHANGED:
     *      "arrows may be shot only from a given set of x-coordinates"
     *
     * WHAT BROKE?
     *      The safe greedy point:
     *
     *          earliest interval's end
     *
     * may not be a legal shot position.
     *
     * NEW PROBLEM:
     *      choose legal points that cover all intervals.
     *
     * PATTERN SHIFT:
     *      depending on structure:
     *          -> greedy over allowed points,
     *          -> set cover style problem,
     *          -> DP / search.
     *
     * RETRIEVAL SIGNAL:
     *      Add ALLOWED-CHOICE RESTRICTIONS -> re-prove greedy; never reuse blindly.
     *
     *
     * -------------------------------------------------------------------------
     * TINY CHANGE 5
     * -------------------------------------------------------------------------
     *
     * ORIGINAL:
     *      "return the minimum NUMBER of arrows"
     *
     * CHANGED:
     *      "return the actual x-positions of one minimum-arrow solution"
     *
     * WHAT CHANGES?
     *      Core algorithm does NOT change.
     *
     * NEW OUTPUT STATE:
     *      store every chosen arrowPosition.
     *
     * PATTERN:
     *      same greedy.
     *
     * RETRIEVAL SIGNAL:
     *      Output changed, objective did not -> often same algorithm + reconstruction.
     *
     *
     * -------------------------------------------------------------------------
     * TINY CHANGE 6
     * -------------------------------------------------------------------------
     *
     * ORIGINAL:
     *      "all balloons are given in points"
     *
     * CHANGED:
     *      "balloons arrive one by one; after every insertion,
     *       report the current minimum arrows"
     *
     * WHAT BROKE?
     *      Offline sorting is no longer freely available.
     *
     * NEW REQUIREMENT:
     *      dynamically maintain interval relationships.
     *
     * PATTERN SHIFT:
     *      ordered map / balanced tree / dynamic interval structure,
     *      depending on update/query guarantees.
     *
     * RETRIEVAL SIGNAL:
     *      OFFLINE -> ONLINE often kills "sort once, scan once."
     *
     *
     * -------------------------------------------------------------------------
     * TINY CHANGE 7
     * -------------------------------------------------------------------------
     *
     * ORIGINAL:
     *      "coordinates may span the integer range"
     *
     * CHANGED:
     *      "0 <= xStart < xEnd <= 1000"
     *
     * WHAT CHANGED?
     *      Coordinate-indexed memory is now cheap.
     *
     * POSSIBLE PATTERN SHIFT:
     *      difference array / prefix sweep / counting representation
     *      may become viable for overlap-oriented variants.
     *
     * IMPORTANT:
     *      The original Minimum Arrows greedy is still excellent.
     *      A new feasible technique does not automatically become a better one.
     *
     * RETRIEVAL SIGNAL:
     *      HUGE DOMAIN -> sort/events.
     *      TINY DOMAIN -> coordinate arrays become candidates.
     *
     *
     * -------------------------------------------------------------------------
     * TINY CHANGE 8
     * -------------------------------------------------------------------------
     *
     * ORIGINAL:
     *      "minimum arrows to burst ALL balloons"
     *
     * CHANGED:
     *      "with at most k arrows, maximize the number of balloons burst"
     *
     * WHAT BROKE?
     *      Feasibility became an optimization-under-budget problem.
     *
     * NEW STATE:
     *      arrows remaining / used
     *      + what balloons can still be covered.
     *
     * PATTERN SHIFT:
     *      depending on constraints:
     *          -> DP
     *          -> greedy + heap
     *          -> interval optimization.
     *
     * RETRIEVAL SIGNAL:
     *      "MINIMUM resources for ALL"
     *          changed to
     *      "MAXIMUM reward with BUDGET k"
     *          -> expect a different optimization pattern.
     *
     *
     * -------------------------------------------------------------------------
     * TINY CHANGE 9
     * -------------------------------------------------------------------------
     *
     * ORIGINAL:
     *      balloons are intervals on ONE x-axis.
     *
     * CHANGED:
     *      "a shot at (x, y) must hit 2D rectangles / disks"
     *
     * WHAT BROKE?
     *      One-dimensional interval ordering disappears.
     *
     * PATTERN SHIFT:
     *      computational geometry / sweep line / spatial structures,
     *      depending on the exact geometry.
     *
     * RETRIEVAL SIGNAL:
     *      1D ordering is often the hidden reason interval greedy works.
     *
     *
     * -------------------------------------------------------------------------
     * TINY CHANGE 10
     * -------------------------------------------------------------------------
     *
     * ORIGINAL:
     *      xStart <= x <= xEnd
     *
     * CHANGED:
     *      endpoints are OPEN:
     *
     *          xStart < x < xEnd
     *
     * WHAT CHANGES?
     *      Endpoint equality no longer counts.
     *
     * PATTERN:
     *      greedy idea may remain,
     *      but boundary handling / legal point choice changes.
     *
     * RETRIEVAL SIGNAL:
     *      CLOSED / OPEN / TOUCHING language -> derive inequality from semantics.
     *
     *
     * THE HIGH-VALUE LESSON
     * ---------------------
     *
     * Do not attach a pattern to the noun "balloon."
     *
     * Attach it to the OPERATIONS guaranteed by the statement:
     *
     *      static 1D intervals
     *      + arbitrary point choice
     *      + one point covers every containing interval
     *      + equal cost per point
     *      + minimize point count
     *
     *              ↓
     *
     *      interval stabbing
     *              ↓
     *
     *      GREEDY BY END
     */

    // =========================================================================
    // HORIZONTAL MASTERY — COMPRESS THE THREE MICROSCOPES
    // =========================================================================

    /*
     * Statement delta                 What changed?              Likely consequence
     * -------------------------------------------------------------------------------
     * COUNT -> PROFIT / COST          objective is weighted      DP / weighted problem
     *
     * COUNT -> ACTUAL CHOICES         output only                same core + reconstruction
     *
     * TOUCHING semantics change       boundary definition        inequality changes
     *
     * ONE active interval -> MANY     required state grows       heap / sweep
     *
     * OFFLINE -> ONLINE               cannot sort once           dynamic structure
     *
     * STATIC -> DYNAMIC updates       invariant must persist      ordered/dynamic structure
     *
     * UNLIMITED -> CAPACITY k         future depends on budget    extra state / DP / heap
     *
     * ANY choice -> restricted choice greedy move may be illegal re-prove / new pattern
     *
     * 1D -> 2D                        total order disappears      geometry / sweep / spatial DS
     *
     * Memory chain:
     *
     *      EXACT WORD / CONSTRAINT DELTA
     *                  ↓
     *      WHICH OLD ASSUMPTION DID IT REMOVE?
     *                  ↓
     *      DOES OLD STATE / INVARIANT STILL SUFFICE?
     *                  ↓
     *          YES -> adapt same pattern
     *          NO  -> derive new pattern
     */

    // =========================================================================
    // FIVE-MONTH / 5000-PROBLEM RETRIEVAL TEST
    // =========================================================================

    /*
     * If you remember only these questions, reconstruct the rest:
     *
     * 1. WHAT is being optimized?
     *      count / removals / profit / active overlap / coverage?
     *
     * 2. WHAT does one decision block or expose?
     *      For Activity Selection: the END defines when future becomes free.
     *
     * 3. WHAT can one action affect?
     *      For Arrows: one x can cover every interval containing x.
     *
     * 4. WHAT information must survive to the next step?
     *      lastFinish / arrowPosition / active ends / DP state?
     *
     * 5. WHICH word or constraint changed from the problem I know?
     *      cost? capacity? online? weighted? endpoint semantics? bounded domain?
     *
     * 6. DID that change only the output,
     *    or did it change feasibility / state / objective?
     *
     * Reconstruct:
     *
     *      statement
     *          -> operation
     *          -> state
     *          -> invariant
     *          -> data structure / algorithm
     *          -> proof
     *          -> complexity.
     */

    // =========================================================================
    // COMPLEXITY — DERIVE, DON'T RECITE
    // =========================================================================

    /*
     * Let n = number of intervals.
     *
     * Sorting:
     *      O(n log n)
     *
     * Scan:
     *      each interval is examined once -> O(n)
     *
     * Total:
     *      O(n log n)
     *
     * If input is already sorted by end:
     *      O(n)
     *
     * Java detail:
     * Arrays.sort(Object[], Comparator) mutates the supplied int[][] outer array.
     *
     * Prefer:
     *      Comparator.comparingInt(interval -> interval[1])
     *
     * over:
     *      (a, b) -> a[1] - b[1]
     *
     * because subtraction can overflow for extreme coordinates.
     */

    // =========================================================================
    // REUSABLE GREEDY EXCHANGE-PROOF TEMPLATE
    // =========================================================================

    /*
     * 1. Let G be the greedy choice.
     * 2. Take an optimal solution using O.
     * 3. Show G leaves the remaining problem no worse than O.
     * 4. Exchange O -> G without reducing feasibility or objective value.
     * 5. Therefore some optimal solution contains G.
     * 6. Repeat on the remaining subproblem.
     *
     * Memory:
     *
     *      greedy choice
     *          -> exchange into optimum
     *          -> optimum preserved.
     */

    // =========================================================================
    // HIGH-ROI APPROACH POLICY
    // =========================================================================

    /*
     * MASTER DEEPLY
     *      earliest-finish greedy + exchange argument.
     *
     * KEEP FLUENT
     *      maximum kept <-> minimum removed.
     *      earliest-end point placement for Minimum Arrows.
     *
     * DERIVATION ONLY
     *      TAKE / SKIP brute force.
     *
     * Do not add a second implementation merely because it is correct.
     *
     * An alternative earns code only if it adds:
     *      reusable pattern knowledge,
     *      an important tradeoff,
     *      a complexity improvement,
     *      a common follow-up,
     *      or a useful boundary.
     */

    // =========================================================================
    // 30-SECOND RECALL CARD
    // =========================================================================

    /*
     * GREEDY BY END
     * -------------
     * Why END?
     *      It tells me when the future becomes available.
     *
     * SELECT:
     *      sort END -> if start >= lastFinish, take.
     *
     * REMOVE:
     *      maximize kept -> n - kept.
     *
     * COVER:
     *      first arrow at earliest end.
     *      closed intervals -> new arrow only when arrowPosition < start.
     *
     * Proof:
     *      earliest end can replace a later-ending competing choice
     *      without reducing future options.
     *
     * Mutation test:
     *      changed WORD / CONSTRAINT
     *          -> changed required STATE / OPERATION
     *          -> maybe changed PATTERN.
     */

    // =========================================================================
    // INTERVIEW ARTICULATION
    // =========================================================================

    /*
     * "The key decision is how much future timeline my current choice blocks.
     * For maximum-cardinality compatible intervals, the earliest finish leaves
     * at least as much future room as any competing choice, so I sort by end.
     *
     * The exchange argument proves that choice is safe.
     *
     * Non-overlapping Intervals is the same engine after transforming
     * minimum removals into maximum kept.
     *
     * Minimum Arrows still uses earliest end, but now the state is a chosen
     * point rather than the end of a selected interval.
     *
     * Sorting dominates, so these offline solutions are O(n log n)."
     */

    // =========================================================================
    // MASTERY EXIT CHECK
    // =========================================================================

    /*
     * [ ] Derive END from "when does the future become available?"
     * [ ] Explain why earliest start fails.
     * [ ] Explain why shortest duration fails.
     * [ ] Code Activity Selection using int[][].
     * [ ] Give the exchange proof.
     * [ ] Derive minimum removals as n - maximum kept.
     * [ ] Explain why Minimum Arrows starts with arrows = 1.
     * [ ] Explain why arrowPosition starts at points[0][1].
     * [ ] Derive >= versus > from endpoint semantics.
     * [ ] Explain why unequal profit moves greedy -> DP.
     * [ ] Explain why peak overlap moves greedy -> sweep / heap.
     * [ ] Explain when bounded coordinates can suggest a difference array.
     * [ ] Route a changed statement by identifying the exact changed word/constraint.
     */

    // =========================================================================
    // TESTS
    // Run with assertions enabled.
    // =========================================================================

    public static void main(String[] args) {

        ActivitySelection activitySelection = new ActivitySelection();
        NonOverlappingIntervals nonOverlap = new NonOverlappingIntervals();
        MinimumArrows arrows = new MinimumArrows();

        int[][] activities = {
                {1, 4},
                {3, 5},
                {0, 6},
                {5, 7},
                {8, 9},
                {5, 9}
        };
        assert activitySelection.maximumActivities(copy(activities)) == 3;

        int[][] startSortTrap = {
                {1, 10},
                {2, 3},
                {3, 4},
                {4, 5},
                {5, 6},
                {6, 7}
        };
        assert activitySelection.maximumActivities(copy(startSortTrap)) == 5;

        int[][] shortestDurationTrap = {
                {0, 3},
                {2, 4},
                {3, 6}
        };
        assert activitySelection.maximumActivities(copy(shortestDurationTrap)) == 2;

        int[][] touching = {
                {1, 2},
                {2, 3},
                {3, 4}
        };
        assert activitySelection.maximumActivities(copy(touching)) == 3;

        assert nonOverlap.eraseOverlapIntervals(new int[][]{
                {1, 2},
                {2, 3},
                {3, 4},
                {1, 3}
        }) == 1;

        assert nonOverlap.eraseOverlapIntervals(new int[][]{
                {1, 2},
                {1, 2},
                {1, 2}
        }) == 2;

        assert arrows.findMinArrowShots(new int[][]{
                {10, 16},
                {2, 8},
                {1, 6},
                {7, 12}
        }) == 2;

        assert arrows.findMinArrowShots(new int[][]{
                {1, 2},
                {2, 3},
                {3, 4},
                {4, 5}
        }) == 2;

        assert activitySelection.maximumActivities(new int[][]{}) == 0;
        assert nonOverlap.eraseOverlapIntervals(new int[][]{}) == 0;
        assert arrows.findMinArrowShots(new int[][]{}) == 0;

        System.out.println("All assertions passed.");
    }

    private static int[][] copy(int[][] intervals) {
        int[][] copy = new int[intervals.length][];

        for (int i = 0; i < intervals.length; i++) {
            copy[i] = Arrays.copyOf(intervals[i], intervals[i].length);
        }

        return copy;
    }
}
