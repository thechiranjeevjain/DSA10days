package org.chijai.day1.Arrays.session4.Intervals;

import java.util.Arrays;
import java.util.Comparator;

/**
 * INTERVAL PATTERN 2 — GREEDY BY END
 *
 * Horizontal mastery:
 *      Activity Selection         -> maximize compatible count
 *      Non-overlapping Intervals -> minimize removals
 *      Minimum Arrows            -> minimum points covering intervals
 *
 * MASTER ENGINE
 * -------------
 * When choosing among intervals, prefer the interval that finishes earliest.
 * An earlier finish leaves at least as much timeline available for future choices.
 *
 * ONE-LINE RECALL
 * ---------------
 *      SELECT -> sort by end -> take compatible interval -> move lastFinish
 *      REMOVE -> maximize kept -> answer = n - kept
 *      COVER  -> sort by end -> place point at earliest uncovered end
 *
 * CODE STYLE
 * ----------
 * Keep reconstruction familiar: arrays / Comparator / plain loops.
 * No Streams or Collectors in core algorithm logic.
 */
public class IntervalGreedyByEnd {

    // =========================================================================
    // DOMAIN MODEL
    // =========================================================================

    static final class Interval {
        final int start;
        final int end;

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "[" + start + "," + end + "]";
        }
    }

    // =========================================================================
    // PATTERN POSITION / RECOGNITION
    // =========================================================================

    /*
     * USE THIS FILE WHEN THE QUESTION ASKS YOU TO CHOOSE / KEEP / COVER RANGES:
     *
     *      Maximize number of mutually compatible intervals?
     *          -> Activity Selection
     *
     *      Minimum intervals to remove so the rest are compatible?
     *          -> Non-overlapping Intervals
     *
     *      Minimum points / arrows needed to hit every interval?
     *          -> Minimum Arrows
     *
     * NOT THIS ENGINE:
     *
     *      Detect / merge / insert ordered ranges?
     *          -> IntervalSortByStart.java
     *
     *      Peak simultaneous occupancy / minimum resources?
     *          -> IntervalActiveOverTime.java
     *
     *      Maximize compatible PROFIT / VALUE?
     *          -> WeightedIntervalScheduling.java
     *
     * MASTER QUESTION:
     *      Am I choosing a compatible set / representative points,
     *      or am I computing overlap magnitude / union / weighted value?
     */

    // =========================================================================
    // 1) ACTIVITY SELECTION — MAXIMIZE COMPATIBLE COUNT
    // =========================================================================

    static final class ActivitySelection {

        int maximumActivities(Interval[] activities) {
            if (activities == null || activities.length == 0) {
                return 0;
            }

            Arrays.sort(activities, Comparator.comparingInt(i -> i.end));

            int selected = 0;
            int lastFinish = Integer.MIN_VALUE;

            for (Interval current : activities) {
                if (current.start >= lastFinish) {
                    selected++;
                    lastFinish = current.end;
                }
            }

            return selected;
        }
    }

    /*
     * WHY? — ACTIVITY SELECTION
     * -------------------------
     * 1. Why sort by END?
     *      We want the accepted interval to occupy as little future timeline
     *      as possible. Among compatible candidates, the earliest finish leaves
     *      at least as much room for every future interval.
     *
     * 2. What is the invariant?
     *      lastFinish is the end of the latest interval in the greedy set.
     *      Every selected interval is mutually compatible with that set.
     *
     * 3. Why accept current.start >= lastFinish?
     *      Under the compatibility policy used here, touching intervals are allowed:
     *          [1,3] and [3,5] can both be selected.
     *
     * 4. Why is the greedy choice safe? — shortest defensible exchange proof
     *      Let G be the earliest-finishing compatible interval chosen now.
     *      If an optimal solution chooses another compatible interval O first,
     *      then G.end <= O.end.
     *      Replacing O with G cannot invalidate any later interval that followed O,
     *      because G frees the timeline no later than O.
     *      Therefore some optimal solution begins with G.
     *      Repeat the argument for the remaining timeline.
     *
     * 5. Complexity
     *      O(n log n) time from sorting; O(n) is the scan.
     *
     * BRUTE-FORCE DERIVATION
     *      Each interval can be taken or skipped -> O(2^n) possibilities.
     *      Greedy works because all accepted intervals contribute the SAME value: 1.
     *
     * IMPORTANT COUNTEREXAMPLE TO "SORT BY START"
     *      [1,10], [2,3], [3,4], [4,5], [5,6], [6,7]
     *
     *      Taking the earliest START can trap us in [1,10] -> count 1.
     *      Earliest FINISH keeps [2,3],[3,4],[4,5],[5,6],[6,7] -> count 5.
     */

    // =========================================================================
    // 2) NON-OVERLAPPING INTERVALS — MINIMUM REMOVALS
    // =========================================================================

    static final class NonOverlappingIntervals {

        int eraseOverlapIntervals(Interval[] intervals) {
            if (intervals == null || intervals.length <= 1) {
                return 0;
            }

            Arrays.sort(intervals, Comparator.comparingInt(i -> i.end));

            int kept = 0;
            int lastFinish = Integer.MIN_VALUE;

            for (Interval current : intervals) {
                if (current.start >= lastFinish) {
                    kept++;
                    lastFinish = current.end;
                }
            }

            return intervals.length - kept;
        }
    }

    /*
     * WHY? — NON-OVERLAPPING INTERVALS
     * --------------------------------
     * 1. Why does minimum removals become maximum kept?
     *
     *          removed = total - kept
     *
     *      total is fixed, so minimizing removed is exactly the same objective
     *      as maximizing the number of compatible intervals kept.
     *
     * 2. Why reuse Activity Selection unchanged?
     *      The optimal kept set is the maximum-cardinality compatible set.
     *      Earliest-finish greedy already computes exactly that.
     *
     * 3. Horizontal transfer
     *      Activity Selection asks directly for kept.
     *      Non-overlapping Intervals asks for its complement.
     *
     *      SAME ENGINE. DIFFERENT OUTPUT TRANSFORMATION.
     *
     * 4. Complexity
     *      O(n log n) time from sorting.
     */

    // =========================================================================
    // 3) MINIMUM ARROWS — MINIMUM POINTS TO HIT CLOSED INTERVALS
    // =========================================================================

    static final class MinimumArrows {

        int findMinArrowShots(Interval[] balloons) {
            if (balloons == null || balloons.length == 0) {
                return 0;
            }

            Arrays.sort(balloons, Comparator.comparingInt(i -> i.end));

            int arrows = 1;
            int arrowPosition = balloons[0].end;

            for (int i = 1; i < balloons.length; i++) {
                if (balloons[i].start > arrowPosition) {
                    arrows++;
                    arrowPosition = balloons[i].end;
                }
            }

            return arrows;
        }
    }

    /*
     * WHY? — MINIMUM ARROWS
     * ---------------------
     * 1. What changed from Activity Selection?
     *      We are NOT selecting a maximum compatible subset.
     *      We are selecting the minimum number of POINTS that hit all intervals.
     *
     * 2. Why sort by END again?
     *      Consider the earliest-ending uncovered interval.
     *      Any valid solution must place some arrow inside it.
     *      Placing that arrow at the interval's END reaches as far right as possible
     *      while still guaranteeing that this earliest-ending interval is hit.
     *
     * 3. What is the invariant?
     *      arrowPosition is the chosen point for the current covered group.
     *      Every interval processed since that arrow was created contains it.
     *
     * 4. Why current.start > arrowPosition means a new arrow?
     *      Balloons are treated as CLOSED intervals.
     *      If current.start == arrowPosition, the current arrow still touches it.
     *      Only start > arrowPosition proves the current arrow cannot hit it.
     *
     * 5. Shortest greedy proof
     *      The earliest-ending uncovered interval must be hit somewhere at or before
     *      its end. Moving that chosen point rightward to exactly its end cannot lose
     *      that interval and can only help reach later-starting intervals.
     *      Therefore an optimal solution exists with an arrow at that earliest end.
     *
     * 6. Complexity
     *      O(n log n) time from sorting.
     */

    // =========================================================================
    // 30-SECOND RECALL CARD
    // =========================================================================

    /*
     * GREEDY BY END
     * -------------
     * Trigger:
     *      choose / keep / cover intervals.
     *
     * Master reason:
     *      earliest finish preserves maximum future freedom.
     *
     * Activity Selection:
     *      sort END -> accept if start >= lastFinish.
     *
     * Non-overlapping Intervals:
     *      same kept set -> removals = n - kept.
     *
     * Minimum Arrows:
     *      sort END -> arrow at earliest uncovered end -> new arrow only when
     *      current.start > arrowPosition for closed intervals.
     *
     * Memory line:
     *      SELECT / REMOVE / COVER -> look hard at END time.
     */

    // =========================================================================
    // REUSABLE MASTER TEMPLATES
    // =========================================================================

    /*
     * TEMPLATE A — MAXIMUM COMPATIBLE COUNT
     * -------------------------------------
     * sort intervals by end
     *
     * count = 0
     * lastFinish = -infinity
     *
     * for each interval:
     *      if interval.start >= lastFinish:
     *          count++
     *          lastFinish = interval.end
     *
     * return count
     *
     *
     * TEMPLATE B — MINIMUM REMOVALS
     * -----------------------------
     * kept = maximumCompatibleCount(intervals)
     * return n - kept
     *
     *
     * TEMPLATE C — MINIMUM POINTS / STABBING
     * --------------------------------------
     * sort intervals by end
     *
     * points = 1
     * point = first.end
     *
     * for each remaining interval:
     *      if interval does NOT contain point:
     *          points++
     *          point = interval.end
     */

    // =========================================================================
    // HORIZONTAL MASTERY — SAME ENGINE / TRANSFER
    // =========================================================================

    /*
     * SAME SORT KEY, DIFFERENT OUTPUT SEMANTICS
     * -----------------------------------------
     * Activity Selection
     *      choose maximum number of compatible intervals.
     *
     * Non-overlapping Intervals
     *      choose the SAME kind of maximum compatible set,
     *      then return its complement.
     *
     * Minimum Arrows
     *      do not choose intervals; choose representative points.
     *      Earliest end remains the safe greedy frontier.
     *
     * DO NOT MEMORIZE:
     *      Activity Selection -> algorithm A
     *      Non-overlap       -> algorithm B
     *      Arrows            -> algorithm C
     *
     * REMEMBER:
     *      Earliest end is valuable when the current decision should leave
     *      maximum freedom to everything that comes later.
     */

    // =========================================================================
    // QUESTION MUTATIONS / CROSS-BRANCH CONNECTIONS
    // =========================================================================

    /*
     * MUTATION 1 — COUNT -> REMOVALS
     * ------------------------------
     * Activity Selection:
     *      maximize compatible COUNT.
     *
     * Change only the requested output:
     *      minimum intervals REMOVED.
     *
     *      -> same greedy kept set
     *      -> answer = n - kept
     *
     *
     * MUTATION 2 — UNIT VALUE -> UNEQUAL VALUE
     * ----------------------------------------
     * Activity Selection:
     *      every accepted interval is effectively worth 1.
     *
     * Add unequal PROFIT / VALUE:
     *      earliest finish can reject a much more valuable combination.
     *
     *      -> greedy is no longer sufficient in general
     *      -> WeightedIntervalScheduling.java
     *      -> sort by end + take/skip DP + predecessor search
     *
     *
     * MUTATION 3 — SELECT -> PEAK OCCUPANCY
     * -------------------------------------
     * Here:
     *      choose which intervals / points survive.
     *
     * Change question to:
     *      how many intervals are active simultaneously?
     *
     *      -> IntervalActiveOverTime.java
     *      -> heap / starts+ends / sweep
     *
     *
     * MUTATION 4 — CHOOSE -> COMBINE
     * ------------------------------
     * Here:
     *      select / cover intervals.
     *
     * Change question to:
     *      return the UNION of overlapping ranges.
     *
     *      -> IntervalSortByStart.java
     *      -> sort by start + merge
     */

    // =========================================================================
    // HIGH-ROI APPROACH POLICY
    // =========================================================================

    /*
     * MASTER DEEPLY
     *      Earliest-finish greedy + exchange argument.
     *
     * KEEP FLUENT
     *      maximum kept <-> minimum removed transformation.
     *      earliest-end interval stabbing / Minimum Arrows.
     *
     * DERIVATION ONLY
     *      recursive take/skip brute force for unweighted scheduling.
     *      It explains the search space but is not worth drilling as a second
     *      implementation once the greedy proof is understood.
     *
     * DO NOT ADD CODE JUST BECAUSE ANOTHER CORRECT IMPLEMENTATION EXISTS.
     * An alternative earns code only if it adds a reusable pattern, important
     * tradeoff, complexity improvement, common follow-up, or useful boundary.
     */

    // =========================================================================
    // ENDPOINT SEMANTICS / JAVA DETAILS
    // =========================================================================

    /*
     * ENDPOINT POLICY — ASK BEFORE CODING
     * -----------------------------------
     * If A ends at t and B starts at t, are they compatible?
     *
     * This file uses:
     *      Activity Selection / Non-overlap:
     *          touching IS compatible -> start >= lastFinish.
     *
     *      Minimum Arrows:
     *          balloons are CLOSED -> start == arrowPosition is still hit,
     *          so a new arrow requires start > arrowPosition.
     *
     * If a problem gives different endpoint semantics, adjust the inequality.
     * Do not memorize < / <= independently of the statement.
     *
     * JAVA NOTES
     * ----------
     * Arrays.sort(Object[], Comparator) mutates the supplied array and may use
     * O(n) auxiliary memory in Java. Do not blindly claim O(1) extra space.
     *
     * Comparator.comparingInt(i -> i.end) is preferred over subtraction such as
     * (a, b) -> a.end - b.end, which can overflow for extreme int values.
     */

    // =========================================================================
    // INTERVIEW ARTICULATION
    // =========================================================================

    /*
     * ACTIVITY SELECTION — SAY IT LIKE THIS
     * -------------------------------------
     * "This is unweighted interval scheduling. I sort by finish time and greedily
     * take each interval that starts after the last selected one ends. The key
     * invariant is that my current greedy schedule ends no later than an equally
     * sized alternative schedule. Choosing the earliest finish is safe by an
     * exchange argument: replacing another first choice with the earlier-ending
     * one cannot reduce the room available for later intervals. Sorting dominates
     * the complexity, so the solution is O(n log n)."
     *
     * NON-OVERLAPPING INTERVALS — SAY IT LIKE THIS
     * --------------------------------------------
     * "Minimum removals is the complement of maximum intervals kept. I compute
     * the maximum compatible set with earliest-finish greedy and return n - kept."
     *
     * MINIMUM ARROWS — SAY IT LIKE THIS
     * ---------------------------------
     * "I sort balloons by end and place an arrow at the earliest end not already
     * covered. Any solution must hit that earliest-ending balloon, and moving the
     * arrow to its right endpoint preserves that hit while maximizing its chance
     * of hitting later balloons. For closed intervals, equality is still covered."
     */

    // =========================================================================
    // REINFORCEMENT / DISCRIMINATION
    // =========================================================================

    /*
     * SAME ENGINE
     * -----------
     * Activity Selection / Transaction Scheduling
     *      -> maximum compatible unweighted count.
     *
     * Non-overlapping Intervals
     *      -> same compatible-set engine; complement output.
     *
     * RELATED EARLIEST-END COVERING
     * ----------------------------
     * Minimum Arrows / interval stabbing
     *      -> choose minimum representative points rather than intervals.
     *
     * PATTERN BOUNDARIES
     * ------------------
     * Meeting Rooms I
     *      asks only whether overlap exists
     *      -> IntervalSortByStart.java
     *
     * Meeting Rooms II / Minimum Platforms
     *      asks peak simultaneous overlap
     *      -> IntervalActiveOverTime.java
     *
     * Weighted Job Scheduling
     *      asks maximum compatible PROFIT
     *      -> WeightedIntervalScheduling.java
     */

    // =========================================================================
    // MASTERY EXIT CHECK
    // =========================================================================

    /*
     * MOVE ON WHEN YOU CAN:
     *      [ ] Recognize "maximize compatible count" as earliest-finish greedy.
     *      [ ] Give the exchange proof without memorizing a paragraph.
     *      [ ] Derive minimum removals as n - maximum kept.
     *      [ ] Explain why Minimum Arrows uses the same sort key but a different state.
     *      [ ] Handle touching endpoints from the problem statement.
     *      [ ] Explain why sort-by-start is wrong for maximizing compatible count.
     *      [ ] Explain why unequal profit moves you from greedy to DP.
     *      [ ] Code all three canonical scans without looking them up.
     */

    // =========================================================================
    // TESTS
    // Run with assertions enabled:
    // java -ea org.chijai.day1.Arrays.session4.Intervals.IntervalGreedyByEnd
    // =========================================================================

    public static void main(String[] args) {

        ActivitySelection activitySelection = new ActivitySelection();
        NonOverlappingIntervals nonOverlap = new NonOverlappingIntervals();
        MinimumArrows arrows = new MinimumArrows();

        Interval[] activities = {
                new Interval(1, 4),
                new Interval(3, 5),
                new Interval(0, 6),
                new Interval(5, 7),
                new Interval(8, 9),
                new Interval(5, 9)
        };

        assert activitySelection.maximumActivities(copy(activities)) == 3;

        Interval[] startSortTrap = {
                new Interval(1, 10),
                new Interval(2, 3),
                new Interval(3, 4),
                new Interval(4, 5),
                new Interval(5, 6),
                new Interval(6, 7)
        };

        assert activitySelection.maximumActivities(copy(startSortTrap)) == 5;

        Interval[] touching = {
                new Interval(1, 2),
                new Interval(2, 3),
                new Interval(3, 4)
        };

        assert activitySelection.maximumActivities(copy(touching)) == 3;

        assert nonOverlap.eraseOverlapIntervals(new Interval[]{
                new Interval(1, 2),
                new Interval(2, 3),
                new Interval(3, 4),
                new Interval(1, 3)
        }) == 1;

        assert nonOverlap.eraseOverlapIntervals(new Interval[]{
                new Interval(1, 2),
                new Interval(1, 2),
                new Interval(1, 2)
        }) == 2;

        assert arrows.findMinArrowShots(new Interval[]{
                new Interval(10, 16),
                new Interval(2, 8),
                new Interval(1, 6),
                new Interval(7, 12)
        }) == 2;

        assert arrows.findMinArrowShots(new Interval[]{
                new Interval(1, 2),
                new Interval(2, 3),
                new Interval(3, 4),
                new Interval(4, 5)
        }) == 2;

        assert activitySelection.maximumActivities(new Interval[]{}) == 0;
        assert nonOverlap.eraseOverlapIntervals(new Interval[]{}) == 0;
        assert arrows.findMinArrowShots(new Interval[]{}) == 0;

        System.out.println("All assertions passed.");
    }

    private static Interval[] copy(Interval[] intervals) {
        return Arrays.copyOf(intervals, intervals.length);
    }
}
