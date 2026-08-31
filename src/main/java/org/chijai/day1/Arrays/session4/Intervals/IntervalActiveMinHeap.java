package org.chijai.day1.Arrays.session4.Intervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * INTERVAL PATTERN — ACTIVE MIN HEAP / REUSABLE RESOURCES
 *
 * MASTER ENGINE
 * -------------
 * Sort intervals by START.
 * Keep a MIN HEAP of active END times.
 *
 * Before each new interval:
 *      remove every end time that is already reusable.
 *
 * Then:
 *      add the current end.
 *
 * The heap contains active resource demand.
 * Peak heap size = minimum reusable resources required.
 *
 * HORIZONTAL MASTERY
 * ------------------
 *      Meeting Rooms II  -> room
 *      Minimum Platforms -> platform
 *      Minimum Chairs    -> chair
 *
 * SAME CODE SHAPE.
 * Different nouns and endpoint policy only.
 *
 * LEARNING POLICY
 * ---------------
 * MASTER:
 *      Active Min Heap once.
 *
 * REUSE:
 *      Rooms / Platforms / Chairs mechanically.
 *
 * KNOW ONCE:
 *      Sorted Starts + Ends as a count-only optimization when identity can disappear.
 *
 * CROSS-LINK:
 *      Car Pooling / Flight Bookings / Range Addition / Population Year
 *      -> DifferenceArraySweepLine.java
 *
 * ONE-LINE RECALL
 * ---------------
 *      SORT START -> FREE FINISHED ENDs -> ADD CURRENT END -> TRACK PEAK.
 *
 * CODE STYLE
 * ----------
 * Plain arrays / PriorityQueue / explicit loops.
 * Shared int[][] <-> List<Interval> helpers stay for Java boundary-conversion fluency.
 * No Streams or Collectors in core algorithm logic.
 */

public class IntervalActiveMinHeap {

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
    // SIMPLE BOUNDARY CONVERSION — JAVA FLUENCY
    // =========================================================================

    static List<Interval> toIntervals(int[][] raw) {
        List<Interval> result = new ArrayList<>();

        for (int[] interval : raw) {
            result.add(new Interval(interval[0], interval[1]));
        }

        return result;
    }

    static int[][] toArray(List<Interval> intervals) {
        int[][] result = new int[intervals.size()][2];

        for (int i = 0; i < intervals.size(); i++) {
            result[i][0] = intervals.get(i).start;
            result[i][1] = intervals.get(i).end;
        }

        return result;
    }

    /*
     * WHY KEEP THESE HELPERS?
     * -----------------------
     * Interview platforms often expose intervals as int[][].
     * Reasoning is often clearer with a small Interval domain object.
     *
     *      int[][] -> List<Interval>
     *          use raw.length for arrays
     *          use add(...) to build the List
     *
     *      List<Interval> -> int[][]
     *          use list.size() for Lists
     *          allocate the fixed-size output array first
     *
     * This is boundary-conversion fluency, not a new interval algorithm.
     */

    // =========================================================================
    // PATTERN POSITION / RECOGNITION
    // =========================================================================

    /*
     * USE THIS ENGINE WHEN:
     *
     *      Minimum reusable resources?
     *      Peak simultaneous interval demand?
     *      Need active end times / IDs / assignment follow-ups?
     *
     * MASTER QUESTION:
     *      "Which active resource becomes reusable earliest?"
     *
     *      -> Min Heap of END times.
     *
     * SAME-ENGINE SIBLINGS:
     *      Meeting Rooms II
     *      Minimum Platforms
     *      Minimum Chairs
     *
     * NOT THIS ENGINE:
     *
     *      Does ANY overlap exist / merge / insert?
     *          -> IntervalSortByStart.java
     *
     *      Choose compatible intervals / removals / arrows?
     *          -> IntervalGreedyByEnd.java
     *
     *      Aggregate boundary deltas / range additions / running capacity?
     *          -> DifferenceArraySweepLine.java
     *
     *      Maximize compatible profit?
     *          -> WeightedIntervalScheduling.java
     */

    // =========================================================================
    // 1) MEETING ROOMS II — CANONICAL ACTIVE MIN HEAP
    // Goal: Minimum rooms = maximum simultaneous meetings.
    // =========================================================================

    static final class MeetingRoomsIIActiveHeap {

        int minMeetingRooms(Interval[] meetings) {
            if (meetings == null || meetings.length == 0) {
                return 0;
            }

            Arrays.sort(meetings, Comparator.comparingInt(i -> i.start));

            PriorityQueue<Integer> activeEnds = new PriorityQueue<>();
            int maximumRooms = 0;

            for (Interval meeting : meetings) {
                while (!activeEnds.isEmpty()
                        && activeEnds.peek() <= meeting.start) {
                    activeEnds.poll();
                }

                activeEnds.offer(meeting.end);
                maximumRooms = Math.max(maximumRooms, activeEnds.size());
            }

            return maximumRooms;
        }
    }

    /*
     * WHY? — MEETING ROOMS II / ACTIVE HEAP
     * -------------------------------------
     * 1. Why sort by START?
     *      We process meetings in the order new demand enters.
     *      Before adding the current meeting, we can safely discard every meeting
     *      that has already ended.
     *
     * 2. Why a MIN heap of END times?
     *      The only active meeting that matters first is the one ending earliest.
     *      If even the earliest end is after current.start, then no active room is free.
     *      If it has finished, remove it and continue checking the next earliest end.
     *
     * 3. Why WHILE, not IF?
     *      This implementation gives the heap a strong meaning:
     *
     *          activeEnds = EXACTLY the meetings active when current begins.
     *
     *      Several old meetings may have finished before the current one starts,
     *      so preserving that invariant requires removing ALL of them.
     *
     * 4. Why does heap.size() equal rooms needed RIGHT NOW?
     *      After cleanup, every heap entry is a meeting that overlaps the current time.
     *      Adding current makes heap.size() the simultaneous meeting count at this point.
     *
     * 5. Why is maximum heap size the minimum number of rooms?
     *      If k meetings coexist, fewer than k rooms are impossible.
     *      Conversely, reusing every room as soon as its meeting ends never allocates
     *      more than the peak simultaneous demand. So peak overlap is both a lower
     *      bound and achievable -> exactly the minimum resource count.
     *
     * 6. Brute-force derivation
     *      Sort by start and maintain room end times in a List.
     *      For every meeting, linearly search for a reusable room -> O(n^2).
     *      The repeated expensive question is:
     *
     *          "Which room becomes free earliest?"
     *
     *      PriorityQueue answers that in O(log n), giving O(n log n) overall.
     *
     * 7. Wrong shortcut: count pairwise overlaps per interval
     *      Pairwise overlap is NOT the same as simultaneous occupancy.
     *      One long interval can overlap many short intervals that never overlap each other.
     *      The resource answer is a GLOBAL timeline peak, not a per-interval overlap count.
     *
     * 8. Complexity
     *      Sort: O(n log n)
     *      Each end enters and leaves the heap at most once: O(n log n)
     *      Heap: O(n) space in the worst case.
     */


    // =========================================================================
    // 2) MINIMUM PLATFORMS — SAME CANONICAL ACTIVE MIN HEAP
    // Goal: Learn Meeting Rooms II once; reuse the same heap skeleton.
    // =========================================================================

    static final class MinimumPlatformsActiveHeap {

        int minPlatforms(int[] arrivals, int[] departures) {
            if (arrivals == null || departures == null
                    || arrivals.length != departures.length
                    || arrivals.length == 0) {
                return 0;
            }

            Interval[] trains = new Interval[arrivals.length];

            // Heap processing needs each train's arrival/departure pair preserved.
            for (int i = 0; i < arrivals.length; i++) {
                trains[i] = new Interval(arrivals[i], departures[i]);
            }

            Arrays.sort(trains, Comparator.comparingInt(train -> train.start));

            PriorityQueue<Integer> activeDepartures = new PriorityQueue<>();
            int maximumPlatforms = 0;

            for (Interval train : trains) {
                // Common GFG policy used here: departure == arrival still conflicts.
                while (!activeDepartures.isEmpty()
                        && activeDepartures.peek() < train.start) {
                    activeDepartures.poll();
                }

                activeDepartures.offer(train.end);
                maximumPlatforms = Math.max(maximumPlatforms, activeDepartures.size());
            }

            return maximumPlatforms;
        }
    }

    /*
     * HORIZONTAL TRANSFER — ROOMS -> PLATFORMS
     * ----------------------------------------
     * SAME CODE SHAPE:
     *
     *      sort by START / ARRIVAL
     *      min heap of END / DEPARTURE times
     *      remove every finished interval
     *      add current end
     *      track peak heap size
     *
     * Only the nouns and endpoint policy changed.
     *
     * Meeting Rooms policy used here:
     *      end == next start -> reusable
     *      free while end <= start
     *
     * Common GFG Platforms policy:
     *      departure == arrival -> conflict
     *      free only while departure < arrival
     *
     * The algorithm did NOT change.
     * The equality semantics came from the problem statement.
     *
     * WHY ZIP ARRIVAL + DEPARTURE INTO Interval[] FOR THE HEAP?
     *      Heap processing sorts trains by arrival while each train's departure
     *      must stay attached to that train. Pair identity still matters here.
     *
     * Complexity:
     *      O(n log n) time
     *      O(n) auxiliary space
     */

    // =========================================================================
    // 3) MINIMUM CHAIRS — SAME CANONICAL ACTIVE MIN HEAP
    // Goal: Same algorithm again so the transfer becomes mechanical.
    // =========================================================================

    static final class MinimumChairsActiveHeap {

        int minChairs(Interval[] people) {
            if (people == null || people.length == 0) {
                return 0;
            }

            Arrays.sort(people, Comparator.comparingInt(person -> person.start));

            PriorityQueue<Integer> activeLeaveTimes = new PriorityQueue<>();
            int maximumChairs = 0;

            for (Interval person : people) {
                while (!activeLeaveTimes.isEmpty()
                        && activeLeaveTimes.peek() <= person.start) {
                    activeLeaveTimes.poll();
                }

                activeLeaveTimes.offer(person.end);
                maximumChairs = Math.max(maximumChairs, activeLeaveTimes.size());
            }

            return maximumChairs;
        }
    }

    /*
     * HORIZONTAL TRANSFER — ROOMS -> PLATFORMS -> CHAIRS
     * --------------------------------------------------
     *      meeting.start  -> train.arrival  -> person.arrival
     *      meeting.end    -> train.depart   -> person.leave
     *      room           -> platform       -> chair
     *
     * Strip the nouns:
     *
     *      sort intervals by start
     *      min heap of active end times
     *      remove finished
     *      add current
     *      peak heap size = minimum reusable resources
     *
     * THIS is the pattern to retain.
     * Do not memorize three separate algorithms.
     */

    // =========================================================================
    // 4) COUNT-ONLY SPECIALIZATION — SORTED STARTS + ENDS
    // Goal: Know once as an optimization; do not drill separately for every noun.
    // =========================================================================

    static final class MeetingRoomsIISortedStartsEnds {

        int minMeetingRooms(Interval[] meetings) {
            if (meetings == null || meetings.length == 0) {
                return 0;
            }

            int n = meetings.length;
            int[] starts = new int[n];
            int[] ends = new int[n];

            for (int i = 0; i < n; i++) {
                starts[i] = meetings[i].start;
                ends[i] = meetings[i].end;
            }

            Arrays.sort(starts);
            Arrays.sort(ends);

            int startIndex = 0;
            int endIndex = 0;
            int active = 0;
            int maximumRooms = 0;

            while (startIndex < n) {
                if (starts[startIndex] < ends[endIndex]) {
                    active++;
                    maximumRooms = Math.max(maximumRooms, active);
                    startIndex++;
                } else {
                    active--;
                    endIndex++;
                }
            }

            return maximumRooms;
        }
    }

    static final class MinimumPlatformsSortedStartsEnds {

        int minPlatforms(int[] arrivals, int[] departures) {
            if (arrivals == null || departures == null
                    || arrivals.length != departures.length
                    || arrivals.length == 0) {
                return 0;
            }

            Arrays.sort(arrivals);
            Arrays.sort(departures);

            int arrivalIndex = 0;
            int departureIndex = 0;
            int currentPlatforms = 0;
            int maximumPlatforms = 0;

            while (arrivalIndex < arrivals.length) {
                // This class follows the common GFG rule:
                // arrival == departure still requires another platform.
                if (arrivals[arrivalIndex] <= departures[departureIndex]) {
                    currentPlatforms++;
                    maximumPlatforms = Math.max(maximumPlatforms, currentPlatforms);
                    arrivalIndex++;
                } else {
                    currentPlatforms--;
                    departureIndex++;
                }
            }

            return maximumPlatforms;
        }
    }

    /*
     * WHY KEEP STARTS + ENDS AT ALL?
     * ------------------------------
     * Not because Rooms / Platforms need another primary solution.
     *
     * It teaches one reusable representation decision:
     *
     *      Need active identities / assignments / metadata?
     *          -> Active Min Heap.
     *
     *      Need only aggregate peak count?
     *          -> identity may be discarded.
     *          -> sort starts and ends independently.
     *
     * Same asymptotic time:
     *      O(n log n)
     *
     * Space:
     *      Active heap -> O(n).
     *
     *      Meeting Rooms int[][] input:
     *          starts + ends usually requires O(n) extra arrays anyway.
     *
     *      Minimum Platforms already gives separate arrival/departure arrays:
     *          sorting them in-place can avoid the O(n) heap state
     *          (ignoring sorting implementation stack/details).
     *
     * So learn the heap as the DEFAULT.
     * Keep Starts + Ends as a count-only optimization knob.
     *
     * IDENTITY-FREE INVARIANT
     * -----------------------
     *      active = starts processed - ends processed
     *
     * No start/end pairing is required once the output is only peak occupancy.
     *
     * Endpoint policy still comes from the statement:
     *      reusable on equality  -> END before START
     *      conflict on equality  -> START before END
     */

    // =========================================================================
    // 30-SECOND RECALL CARD
    // =========================================================================

    /*
     * ACTIVE MIN HEAP
     * ---------------
     * Trigger:
     *      minimum reusable resources / peak simultaneous demand.
     *
     * Master invariant:
     *      heap contains active END times.
     *
     * Template:
     *      sort by START
     *      while earliest END is reusable -> poll
     *      offer current END
     *      peak = max(peak, heap.size())
     *
     * SAME TEMPLATE:
     *      Meeting Rooms II
     *      Minimum Platforms
     *      Minimum Chairs
     *
     * Count-only optimization:
     *      identity irrelevant -> sorted STARTS + ENDS.
     *
     * Memory line:
     *      EARLIEST END CONTROLS RESOURCE REUSE.
     */

    // =========================================================================
    // REUSABLE MASTER TEMPLATES
    // =========================================================================

    /*
     * TEMPLATE A — ACTIVE MIN HEAP
     * ----------------------------
     * sort intervals by start
     * heap = min heap of active end times
     * peak = 0
     *
     * for each interval:
     *      while heap not empty AND earliest end is reusable:
     *          poll
     *
     *      offer current end
     *      peak = max(peak, heap.size)
     *
     * return peak
     *
     *
     * TEMPLATE B — COUNT-ONLY STARTS + ENDS
     * -------------------------------------
     * sort starts
     * sort ends
     *
     * active = 0
     * peak = 0
     * i = 0
     * j = 0
     *
     * while starts remain:
     *      if next start happens before next reusable end:
     *          active++
     *          peak = max(peak, active)
     *          i++
     *      else:
     *          active--
     *          j++
     *
     * Learn Template A deeply.
     * Keep Template B as an optimization knob, not another primary solution to drill.
     */

    // =========================================================================
    // HORIZONTAL MASTERY — SAME ENGINE, DIFFERENT NOUNS
    // =========================================================================

    /*
     * MEETING ROOMS II
     *      meeting.start / meeting.end / room
     *
     *          ↓ rename nouns
     *
     * MINIMUM PLATFORMS
     *      arrival / departure / platform
     *
     *          ↓ rename nouns
     *
     * MINIMUM CHAIRS
     *      arrival / leave / chair
     *
     * STRIP THE STORY:
     *
     *      interval enters
     *      interval leaves
     *      reusable resource
     *
     * SAME ACTIVE-MIN-HEAP TEMPLATE.
     *
     * The only important policy variation is endpoint equality:
     *      can an end at t be reused by a start at t?
     */

    // =========================================================================
    // QUESTION MUTATIONS / CROSS-FILE CONNECTIONS
    // =========================================================================

    /*
     * MUTATION 1 — EXISTENCE -> MAGNITUDE
     * -----------------------------------
     * Meeting Rooms I:
     *      "Does ANY overlap exist?"
     *      -> IntervalSortByStart.java
     *
     * Change to:
     *      "HOW MANY are active at the peak?"
     *
     *      -> Active Min Heap.
     *
     *
     * MUTATION 2 — COUNT ONLY -> ASSIGNMENT / IDENTITY
     * ------------------------------------------------
     * Sorted Starts + Ends can discard identity.
     *
     * Ask:
     *      "Which room/platform should I assign?"
     *
     *      -> heap entries can retain endTime + resourceId.
     *
     *
     * MUTATION 3 — ACTIVE END TIMES -> AGGREGATE BOUNDARY DELTAS
     * ---------------------------------------------------------
     * If the problem does NOT need which interval ends next and only asks for
     * aggregate weighted changes:
     *
     *      pickup -> +passengers
     *      drop   -> -passengers
     *      [L,R]  -> +value / -value
     *
     *      -> DifferenceArraySweepLine.java
     *
     * This is a PATTERN CHANGE, not another heap variation.
     *
     *
     * MUTATION 4 — RESOURCE COUNT -> INTERVAL SELECTION
     * ------------------------------------------------
     * Choose maximum compatible intervals / minimum removals / arrows:
     *      -> IntervalGreedyByEnd.java
     *
     *
     * MUTATION 5 — UNIT VALUE -> WEIGHTED COMPATIBLE PROFIT
     * -----------------------------------------------------
     *      -> WeightedIntervalScheduling.java
     */

    // =========================================================================
    // HIGH-ROI APPROACH POLICY
    // =========================================================================

    /*
     * MASTER DEEPLY
     * -------------
     * Active Min Heap:
     *
     *      sort by start
     *      remove all reusable earliest ends
     *      add current end
     *      track peak heap size
     *
     * Reuse the SAME template for:
     *      Meeting Rooms II
     *      Minimum Platforms
     *      Minimum Chairs
     *
     * KNOW ONCE — DO NOT DRILL PER PROBLEM
     * ------------------------------------
     * Sorted Starts + Ends:
     *      useful when only aggregate count matters and identity can disappear.
     *
     * Same asymptotic time:
     *      O(n log n)
     *
     * Space:
     *      heap -> O(n)
     *
     * For Minimum Platforms, arrivals/departures already arrive as separate arrays,
     * so sorting those arrays in place can reduce explicit auxiliary state.
     *
     * UNDERSTAND ONCE — DO NOT DRILL
     * ------------------------------
     * Allocated-slot heap:
     *      IF + final heap.size()
     *
     * Active heap:
     *      WHILE + maximum heap.size()
     *
     * Same answer, different invariant.
     *
     * MOVE OUT OF THIS FILE
     * ---------------------
     * Car Pooling / Flight Bookings / Range Addition / Population Year
     *      -> DifferenceArraySweepLine.java
     *
     * They teach boundary-delta accumulation, a genuinely different reusable engine.
     */

    // =========================================================================
    // ENDPOINT SEMANTICS / JAVA DETAILS
    // =========================================================================

    /*
     * ENDPOINT POLICY — ASK BEFORE CODING
     * -----------------------------------
     * If one interval ends at t and another starts at t, can the resource be reused?
     *
     * YES:
     *      heap frees end <= start
     *      starts+ends treats END as reusable before START at equality
     *
     * NO:
     *      heap frees only end < start
     *      starts+ends treats START as conflict before END at equality
     *
     * Equality is a problem policy, not a separate algorithm.
     *
     * JAVA NOTES
     * ----------
     * PriorityQueue<Integer>
     *      -> min heap by default; peek() is earliest end.
     *
     * Arrays.sort(Object[], Comparator)
     *      -> mutates the Interval[] and may use O(n) auxiliary memory in Java.
     *         Heap space is already O(n), so total auxiliary remains O(n).
     *
     * Arrays.sort(int[])
     *      -> mutates primitive arrays. If callers need original arrays unchanged,
     *         clone before sorting.
     *
     * Starts + Ends intentionally uses two int[] arrays:
     *      the loss of start/end pairing is not a bug; it is the optimization insight.
     *
     * Shared List<Interval> conversion helpers remain useful at platform boundaries.

     * No Streams / Collectors are needed. The goal is easy interview reconstruction,
     * not maximum Java syntax compression.
     */

    // =========================================================================
    // INTERVIEW ARTICULATION
    // =========================================================================

    /*
     * ACTIVE MIN HEAP — SAY IT LIKE THIS
     * ----------------------------------
     * "The minimum reusable resources equal the maximum number of intervals active
     * at the same time. I sort by start time and keep a min heap of active end times.
     * Before processing each interval, I remove every end that is reusable by the
     * current start, then add the current end. The heap therefore represents active
     * demand, so the maximum heap size is the answer. This is O(n log n) time and
     * O(n) space."
     *
     * Correctness invariant:
     *      after cleanup and insertion, heap contains exactly the active end times.
     *
     * Why min heap:
     *      the earliest end is the first resource that can become reusable.
     *
     * Why WHILE:
     *      several intervals may have finished before the next start.
     *
     * Termination:
     *      each interval is inserted once and removed at most once.
     *
     *
     * STARTS + ENDS — COUNT-ONLY FOLLOW-UP
     * ------------------------------------
     * "If I only need the count, interval identity can be discarded. I can sort
     * starts and ends separately and process chronological boundaries. This keeps
     * the same O(n log n) time and can reduce retained state in some input forms."
     */

    // =========================================================================
    // REINFORCEMENT / DISCRIMINATION
    // =========================================================================

    /*
     * SAME ENGINE
     * -----------
     * Meeting Rooms II
     * Minimum Platforms
     * Minimum Chairs
     * machine / gate / server allocation counts
     *
     * COUNT-ONLY SPECIALIZATION
     * -------------------------
     * Sorted Starts + Ends
     *
     * PATTERN BOUNDARIES
     * ------------------
     * Meeting Rooms I / Merge / Insert
     *      -> IntervalSortByStart.java
     *
     * Maximum compatible count / removals / arrows
     *      -> IntervalGreedyByEnd.java
     *
     * Car Pooling / Flight Bookings / Range Addition / Population Year
     *      -> DifferenceArraySweepLine.java
     *
     * Weighted compatible profit
     *      -> WeightedIntervalScheduling.java
     */

    // =========================================================================
    // MASTERY EXIT CHECK
    // =========================================================================

    /*
     * MOVE ON WHEN YOU CAN:
     *      [ ] Translate minimum resources <-> peak simultaneous occupancy.
     *      [ ] Code Active Min Heap from the invariant.
     *      [ ] Reuse the same code shape for Rooms / Platforms / Chairs.
     *      [ ] Explain why cleanup uses WHILE.
     *      [ ] Change equality semantics without guessing.
     *      [ ] Explain why min heap stores END times.
     *      [ ] Explain when identity may be discarded for Starts + Ends.
     *      [ ] Treat Starts + Ends as an optimization knob, not another pattern to drill.
     *      [ ] Recognize Car Pooling / range updates as boundary-delta problems instead.
     */

    // =========================================================================
    // TESTS
    // Run with assertions enabled:
    // java -ea org.chijai.day1.Arrays.session4.Intervals.IntervalActiveMinHeap
    // =========================================================================

    public static void main(String[] args) {

        MeetingRoomsIIActiveHeap meetingRooms = new MeetingRoomsIIActiveHeap();
        MinimumPlatformsActiveHeap platformsHeap = new MinimumPlatformsActiveHeap();
        MinimumPlatformsSortedStartsEnds platformsStartsEnds =
                new MinimumPlatformsSortedStartsEnds();
        MinimumChairsActiveHeap chairs = new MinimumChairsActiveHeap();
        MeetingRoomsIISortedStartsEnds meetingRoomsStartsEnds =
                new MeetingRoomsIISortedStartsEnds();

        Interval[] overlap = {
                new Interval(0, 30),
                new Interval(5, 10),
                new Interval(15, 20)
        };

        assert meetingRooms.minMeetingRooms(copy(overlap)) == 2;
        assert meetingRoomsStartsEnds.minMeetingRooms(copy(overlap)) == 2;
        assert chairs.minChairs(copy(overlap)) == 2;

        Interval[] touching = {
                new Interval(1, 5),
                new Interval(5, 10),
                new Interval(10, 15)
        };

        // Meeting Rooms / Chairs policy here: equality allows reuse.
        assert meetingRooms.minMeetingRooms(copy(touching)) == 1;
        assert meetingRoomsStartsEnds.minMeetingRooms(copy(touching)) == 1;
        assert chairs.minChairs(copy(touching)) == 1;

        Interval[] peakThenDrop = {
                new Interval(0, 100),
                new Interval(1, 90),
                new Interval(2, 80),
                new Interval(200, 201)
        };

        assert meetingRooms.minMeetingRooms(copy(peakThenDrop)) == 3;
        assert meetingRoomsStartsEnds.minMeetingRooms(copy(peakThenDrop)) == 3;

        int[] arrivals = {900, 940, 950, 1100, 1500, 1800};
        int[] departures = {910, 1200, 1120, 1130, 1900, 2000};

        assert platformsHeap.minPlatforms(arrivals.clone(), departures.clone()) == 3;
        assert platformsStartsEnds.minPlatforms(arrivals.clone(), departures.clone()) == 3;

        // Common GFG Minimum Platforms policy: equality conflicts.
        int[] touchingArrivals = {100, 200};
        int[] touchingDepartures = {200, 300};

        assert platformsHeap.minPlatforms(
                touchingArrivals.clone(),
                touchingDepartures.clone()
        ) == 2;

        assert platformsStartsEnds.minPlatforms(
                touchingArrivals.clone(),
                touchingDepartures.clone()
        ) == 2;

        assert meetingRooms.minMeetingRooms(new Interval[]{}) == 0;
        assert meetingRoomsStartsEnds.minMeetingRooms(new Interval[]{}) == 0;
        assert platformsHeap.minPlatforms(new int[]{}, new int[]{}) == 0;
        assert platformsStartsEnds.minPlatforms(new int[]{}, new int[]{}) == 0;
        assert chairs.minChairs(new Interval[]{}) == 0;

        System.out.println("All IntervalActiveMinHeap assertions passed.");
    }

    private static Interval[] copy(Interval[] intervals) {
        return Arrays.copyOf(intervals, intervals.length);
    }
}
