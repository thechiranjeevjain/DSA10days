package org.chijai.day1.Arrays.session4.Intervals;

import java.util.*;

/*
 * FILE VERSION: V3
 * Canonical interval-patterns revision file.
 * Includes Minimum Platforms Rank 1 Active Min Heap and Rank 2 sorted arrivals/departures.
 */

/**
 * Interview interval chapter:
 * - Meeting Rooms I
 * - Meeting Rooms II
 * - Minimum Platforms
 * - Merge / Insert Intervals
 * - Unweighted Interval Scheduling / Minimum Removals
 * - Interval Covering / Minimum Arrows
 * - Weighted Sweep Line / Car Pooling
 * - Weighted Interval Scheduling DP
 *
 * Code stays intentionally clean.
 * Deeper reasoning is grouped into compact note sections.
 */
public class MeetingRoom {

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
    // MASTER DECISION MAP
    // =========================================================================

    /*
     * TOPIC
     * -----
     * Sorting / Greedy / Intervals
     *
     * CATEGORY
     * --------
     * Interval Problems
     *
     * SUB-PATTERNS
     * ------------
     * 1. Any overlap/conflict?
     *      -> Sort + adjacent verification
     *      -> Meeting Rooms I
     *
     * 2. Peak overlap / minimum reusable resources?
     *      -> Min Heap OR Sorted Starts + Ends OR Sweep Line
     *      -> Meeting Rooms II, Minimum Platforms, Minimum Chairs
     *
     * 3. Merge / union ranges?
     *      -> Sort by start + merge
     *      -> Merge Intervals, Insert Interval
     *
     * 4. Maximize number of non-overlapping intervals?
     *      -> Greedy by earliest finish
     *      -> Activity Selection / Transaction Scheduling
     *
     * 5. Minimum removals to eliminate overlap?
     *      -> Greedy by earliest finish
     *      -> Non-overlapping Intervals
     *
     * 6. Minimum points to hit/cover intervals?
     *      -> Greedy by earliest end
     *      -> Minimum Arrows to Burst Balloons
     *
     * 7. Weighted occupancy/capacity over time or position?
     *      -> Sweep Line / Difference Array
     *      -> Car Pooling, Corporate Flight Bookings
     *
     * 8. Maximum profit from compatible intervals?
     *      -> Weighted Interval Scheduling DP
     *
     * NOT THIS FAMILY
     * ---------------
     * Gas Station
     *      -> Greedy / Running Balance / Restart Candidate
     *
     * MASTER QUESTION
     * ---------------
     * Do not recognize only "intervals".
     * Recognize what the question asks you to COMPUTE.
     */


    // =========================================================================
    // REUSABILITY / ROI LEARNING POLICY
    // =========================================================================

    /*
     * PRIMARY GOAL
     * ------------
     * Learn a small number of invariants that transfer to unseen variants.
     * Do not memorize problem-name -> algorithm-name mappings.
     *
     * For Peak Overlap / Minimum Resources:
     *
     *      start consumes one resource
     *      end releases one resource
     *      maximum active resources = minimum resources required
     *
     * CANONICAL GENERAL TOOL
     * ----------------------
     * Active Min Heap.
     *
     *      sort intervals by start
     *      remove all finished intervals
     *      add current end
     *      heap = currently active intervals
     *      peak heap size = answer
     *
     * Why master this first:
     *      It preserves active-state information and extends naturally to
     *      room/platform IDs, interval IDs, metadata, assignments and other
     *      variants where identity matters.
     *
     * COUNT-ONLY SPECIALIZATION
     * -------------------------
     * Sorted Starts + Sorted Ends.
     *
     *      next start before earliest end -> consume another resource
     *      earliest end first           -> reuse a resource
     *
     * Why keep this fluent:
     *      It is often the simplest implementation when the output is only
     *      maximum concurrency / minimum resource count.
     *
     * INTERVIEW CHOICE
     * ----------------
     * Both are O(n log n), so asymptotic constraints usually do not separate them.
     *
     *      Need only aggregate count?
     *          -> Starts + Ends is excellent and very low-risk.
     *
     *      Need active objects, assignments, IDs or metadata?
     *          -> Active Min Heap is more extensible.
     *
     * Learning priority:
     *      Deeply master Active Min Heap.
     *      Be fluent with Starts + Ends.
     *      Know the allocated-room heap variant, but do not over-drill it.
     *
     * DECISION PRINCIPLE
     * ------------------
     * Ask what information must survive.
     *
     *      Can interval identity be discarded safely?
     *          -> aggregate sweep / starts + ends
     *
     *      Must active interval/resource identity survive?
     *          -> heap / active-set structure
     */

    // =========================================================================
    // MEETING ROOMS I
    // Goal: Can one person attend every meeting?
    // =========================================================================

    // Rank 2 — Brute force derivation only.
    static final class BruteForceMeetingRoomsI {

        boolean canAttendMeetings(Interval[] meetings) {
            if (meetings == null || meetings.length <= 1) {
                return true;
            }

            for (int i = 0; i < meetings.length; i++) {
                for (int j = i + 1; j < meetings.length; j++) {
                    if (overlap(meetings[i], meetings[j])) {
                        return false;
                    }
                }
            }

            return true;
        }

        private boolean overlap(Interval a, Interval b) {
            return Math.max(a.start, b.start) < Math.min(a.end, b.end);
        }
    }

    // Rank 1 — Interview preferred.
    static final class MeetingRoomsISortAndScan {

        boolean canAttendMeetings(Interval[] meetings) {
            if (meetings == null || meetings.length <= 1) {
                return true;
            }

            Arrays.sort(meetings, Comparator.comparingInt(i -> i.start));

            for (int i = 1; i < meetings.length; i++) {
                if (meetings[i].start < meetings[i - 1].end) {
                    return false;
                }
            }

            return true;
        }
    }

    /*
     * MEETING ROOMS I — NOTES
     * -----------------------
     * Trigger:
     *      Need only YES/NO conflict detection.
     *
     * Pattern:
     *      Sort + adjacent verification.
     *
     * Invariant:
     *      After sorting by start, any conflict is exposed by some adjacent pair.
     *
     * Endpoint rule used here:
     *      [1,5] and [5,8] do NOT overlap.
     *      Therefore conflict check is start < previousEnd, not <=.
     *
     * Complexity:
     *      Brute : O(n^2) time, O(1) extra space.
     *      Sort  : O(n log n) time.
     *
     * Java note:
     *      Arrays.sort(Object[], Comparator) may use O(n) auxiliary memory.
     *      Do not blindly claim O(log n) auxiliary space for this Java code.
     *
     * Important correction:
     *      Sorting by end can also be made correct for existence detection.
     *      Sorting by start is kept as the canonical interview formulation because
     *      the invariant is simpler and transfers cleanly to the interval family.
     *
     * Pattern boundary:
     *      Need peak overlap / room count? -> Meeting Rooms II.
     *      Need minimum removals?          -> Greedy by end.
     */

    // =========================================================================
    // MEETING ROOMS II
    // Goal: Minimum rooms = maximum simultaneous meetings.
    // =========================================================================

    // Rank 5 — Brute force derivation only.
    static final class BruteForceMeetingRoomsII {

        int minMeetingRooms(Interval[] meetings) {
            if (meetings == null || meetings.length == 0) {
                return 0;
            }

            Arrays.sort(meetings, Comparator.comparingInt(i -> i.start));

            List<Integer> roomEndTimes = new ArrayList<>();

            for (Interval meeting : meetings) {
                boolean assigned = false;

                for (int room = 0; room < roomEndTimes.size(); room++) {
                    if (roomEndTimes.get(room) <= meeting.start) {
                        roomEndTimes.set(room, meeting.end);
                        assigned = true;
                        break;
                    }
                }

                if (!assigned) {
                    roomEndTimes.add(meeting.end);
                }
            }

            return roomEndTimes.size();
        }
    }

    // Rank 1 — Interview preferred: clearest active-meeting invariant.
    static final class MeetingRoomsIIActiveHeap {

        int minMeetingRooms(Interval[] meetings) {
            if (meetings == null || meetings.length == 0) {
                return 0;
            }

            Arrays.sort(meetings, Comparator.comparingInt(i -> i.start));

            PriorityQueue<Integer> activeEnds = new PriorityQueue<>();
            int maximumRooms = 0;

            for (Interval meeting : meetings) {
                // Remove every meeting that finished by the time current starts.
                while (!activeEnds.isEmpty()
                        && activeEnds.peek() <= meeting.start) {
                    activeEnds.poll();
                }

                activeEnds.offer(meeting.end);

                // Heap size = simultaneous meetings right now.
                maximumRooms = Math.max(maximumRooms, activeEnds.size());
            }

            return maximumRooms;
        }
    }

    // Rank 3 — Excellent count-only specialization: no heap required.
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

            int rooms = 0;
            int endIndex = 0;

            for (int start : starts) {
                if (start < ends[endIndex]) {
                    rooms++;          // No room free -> allocate one.
                } else {
                    endIndex++;       // Earliest finished room -> reuse it.
                }
            }

            return rooms;
        }
    }

    // Rank 2 — General event sweep; best bridge to weighted capacity problems.
    static final class MeetingRoomsIISweepLine {

        private static final class Event {
            final int time;
            final int delta;

            Event(int time, int delta) {
                this.time = time;
                this.delta = delta;
            }
        }

        int minMeetingRooms(Interval[] meetings) {
            if (meetings == null || meetings.length == 0) {
                return 0;
            }

            List<Event> events = new ArrayList<>(meetings.length * 2);

            for (Interval meeting : meetings) {
                events.add(new Event(meeting.start, +1));
                events.add(new Event(meeting.end, -1));
            }

            // End before start at the same time: touching meetings can reuse a room.
            events.sort(Comparator
                    .comparingInt((Event e) -> e.time)
                    .thenComparingInt(e -> e.delta));

            int active = 0;
            int maximumRooms = 0;

            for (Event event : events) {
                active += event.delta;
                maximumRooms = Math.max(maximumRooms, active);
            }

            return maximumRooms;
        }
    }

    // Rank 4 — Correct heap variant; slightly subtler invariant.
    static final class MeetingRoomsIIAllocatedHeap {

        int minMeetingRooms(Interval[] meetings) {
            if (meetings == null || meetings.length == 0) {
                return 0;
            }

            Arrays.sort(meetings, Comparator.comparingInt(i -> i.start));

            PriorityQueue<Integer> rooms = new PriorityQueue<>();

            for (Interval meeting : meetings) {
                // Current meeting can reuse at most one room.
                if (!rooms.isEmpty() && rooms.peek() <= meeting.start) {
                    rooms.poll();
                }

                rooms.offer(meeting.end);
            }

            return rooms.size();
        }
    }

    /*
     * MEETING ROOMS II — MASTER NOTES
     * -------------------------------
     * Translation:
     *      minimum reusable resources = maximum simultaneous intervals.
     *
     * Recognition:
     *      start consumes a resource; end releases a resource.
     *
     * Rank 1 — Active-meetings min heap
     *      while finished -> poll all
     *      offer current end
     *      track maximum heap size
     *
     *      Heap meaning:
     *          currently active meetings only.
     *
     * Rank 2 — General sweep line
     *      convert each interval into start +1 and end -1 events.
     *      sort chronologically and track peak running occupancy.
     *      This generalizes naturally to weighted occupancy / capacity.
     *
     * Rank 3 — Sorted starts + sorted ends
     *      compare each start against earliest unconsumed end.
     *
     *      start < earliestEnd
     *          -> no room free -> allocate
     *
     *      start >= earliestEnd
     *          -> one room is reusable
     *
     *      This is the same resource decision without maintaining a heap.
     *
     * Rank 4 — Allocated-room heap
     *      if earliest room is reusable -> poll ONE
     *      offer current end
     *      return final heap size
     *
     *      Heap meaning:
     *          one entry per allocated room slot.
     *
     *      Size never decreases:
     *          allocate      -> k -> k + 1
     *          reuse + offer -> k -> k
     *
     *      Therefore final size = peak allocated rooms.
     *
     * THE IF vs WHILE CONFUSION
     * -------------------------
     * if + final heap.size()
     *      -> allocated room slots
     *      -> remove at most one because current meeting needs one room
     *
     * while + maximumRooms
     *      -> active meetings only
     *      -> remove all finished meetings to preserve active-only invariant
     *
     * Both are correct because they maintain DIFFERENT invariants.
     *
     * Rank 5 — Brute force
     *      useful only to derive the optimization target.
     *
     * Complexity:
     *      Brute            : O(n^2) time, O(n) space
     *      Heap variants    : O(n log n) time, O(n) space
     *      Sweep Line       : O(n log n) time, O(n) events
     *      Starts + Ends    : O(n log n) time, O(n) extra arrays
     *
     * Why heap is the best default in an interview:
     *      "Which room becomes free first?" -> min heap of end times.
     *      The derivation is direct and generalizes to actual room assignment.
     */

    // =========================================================================
    // MINIMUM PLATFORMS — SAME FAMILY AS MEETING ROOMS II
    // =========================================================================


    // Rank 1 — Canonical reusable solution: Active Min Heap.
    static final class MinimumPlatformsActiveHeap {

        int minPlatforms(int[] arrivals, int[] departures) {
            if (arrivals == null || departures == null
                    || arrivals.length != departures.length
                    || arrivals.length == 0) {
                return 0;
            }

            Interval[] trains = new Interval[arrivals.length];

            // Preserve each train's arrival-departure pair before sorting.
            for (int i = 0; i < arrivals.length; i++) {
                trains[i] = new Interval(arrivals[i], departures[i]);
            }

            return minPlatforms(trains);
        }

        int minPlatforms(Interval[] trains) {
            if (trains == null || trains.length == 0) {
                return 0;
            }

            Arrays.sort(trains, Comparator.comparingInt(t -> t.start));

            PriorityQueue<Integer> activeTrains = new PriorityQueue<>();
            int maximumPlatforms = 0;

            for (Interval train : trains) {

                // Free every platform whose train has already departed.
                while (!activeTrains.isEmpty()
                        && activeTrains.peek() <= train.start) {
                    activeTrains.poll();
                }

                activeTrains.offer(train.end);
                maximumPlatforms = Math.max(maximumPlatforms, activeTrains.size());
            }

            return maximumPlatforms;
        }
    }

    /*
     * MINIMUM PLATFORMS — APPROACH RANKING
     * ------------------------------------
     * Rank 1 — Active Min Heap
     *      Same canonical model as Meeting Rooms II.
     *      Best transfer to variants needing train/platform identity or metadata.
     *
     * Rank 2 — Sorted Arrivals + Departures
     *      The existing MinimumPlatforms class below.
     *      Best count-only specialization: simple, compact and O(n log n).
     *
     * SAME FAMILY, SAME CORE MODEL
     * ----------------------------
     * Meeting start  == train arrival
     * Meeting end    == train departure
     * Room           == platform
     *
     * Do not memorize:
     *      Meeting Rooms II -> heap
     *      Minimum Platforms -> two pointers
     *
     * Remember:
     *      Peak overlap / minimum resources
     *          -> canonical Active Min Heap
     *          -> count-only Starts + Ends specialization
     */

    // Rank 2 — Count-only specialization: Sorted Arrivals + Departures.
    static final class MinimumPlatforms {

        int minPlatforms(int[] arrivals, int[] departures) {
            if (arrivals == null || departures == null
                    || arrivals.length != departures.length
                    || arrivals.length == 0) {
                return 0;
            }

            Arrays.sort(arrivals);
            Arrays.sort(departures);

            int current = 0;
            int maximum = 0;
            int arrivalIndex = 0;
            int departureIndex = 0;

            while (arrivalIndex < arrivals.length) {
                if (arrivals[arrivalIndex] < departures[departureIndex]) {
                    current++;
                    maximum = Math.max(maximum, current);
                    arrivalIndex++;
                } else {
                    current--;
                    departureIndex++;
                }
            }

            return maximum;
        }
    }

    /*
     * MINIMUM PLATFORMS — TRANSFER NOTE
     * ---------------------------------
     * Meeting start  == train arrival
     * Meeting end    == train departure
     * Room           == platform
     *
     * Same core question:
     *      maximum simultaneous occupancy.
     *
     * IMPORTANT TIE RULE:
     *      This implementation allows reuse when arrival == departure,
     *      matching Meeting Rooms endpoint semantics.
     *
     * Some platform problem statements treat equal arrival/departure as a
     * conflict. In that case:
     *      Sorted arrays -> change arrival < departure to arrival <= departure.
     *      Active heap   -> free only when departure < arrival (change <= to <).
     *
     * Always clarify endpoint semantics before coding.
     */

    // Same peak-overlap family; intentionally delegates to the canonical heap model.
    static final class MinimumChairs {

        int minChairs(Interval[] people) {
            return new MeetingRoomsIIActiveHeap().minMeetingRooms(people);
        }
    }

    // =========================================================================
    // UNWEIGHTED INTERVAL SCHEDULING / TRANSACTION SCHEDULING
    // Goal: Maximum number of mutually compatible intervals.
    // =========================================================================

    // Rank 2 — Brute force derivation.
    static final class BruteForceTransactionScheduling {

        int maximumTransactions(Interval[] transactions) {
            if (transactions == null || transactions.length == 0) {
                return 0;
            }

            Interval[] copy = Arrays.copyOf(transactions, transactions.length);
            Arrays.sort(copy, Comparator.comparingInt(i -> i.start));

            return dfs(copy, 0, Integer.MIN_VALUE);
        }

        private int dfs(Interval[] intervals, int index, int lastFinish) {
            if (index == intervals.length) {
                return 0;
            }

            int skip = dfs(intervals, index + 1, lastFinish);
            int take = 0;

            if (intervals[index].start >= lastFinish) {
                take = 1 + dfs(intervals, index + 1, intervals[index].end);
            }

            return Math.max(take, skip);
        }
    }

    // Rank 1 — Interview preferred.
    static final class TransactionSchedulingGreedy {

        int maximumTransactions(Interval[] transactions) {
            if (transactions == null || transactions.length == 0) {
                return 0;
            }

            Arrays.sort(transactions, Comparator.comparingInt(i -> i.end));

            int accepted = 0;
            int lastFinish = Integer.MIN_VALUE;

            for (Interval current : transactions) {
                if (current.start >= lastFinish) {
                    accepted++;
                    lastFinish = current.end;
                }
            }

            return accepted;
        }
    }

    /*
     * INTERVAL SCHEDULING — NOTES
     * ---------------------------
     * Trigger:
     *      Maximize COUNT of mutually compatible, unweighted intervals.
     *
     * Greedy choice:
     *      Always accept the compatible interval that finishes earliest.
     *
     * Why:
     *      Earlier finish leaves at least as much timeline for future choices.
     *
     * Sort by:
     *      END time, not START time.
     *
     * Complexity:
     *      Brute  : O(2^n) time, O(n) recursion
     *      Greedy : O(n log n) time
     *
     * Pattern boundary:
     *      Maximize total PROFIT instead of count?
     *      -> Greedy no longer works in general.
     *      -> Weighted Interval Scheduling DP.
     */

    // =========================================================================
    // MERGE INTERVALS
    // Goal: Return the union of overlapping ranges.
    // =========================================================================

    // Rank 1 — Canonical: sort by start + merge into the last result interval.
    static final class MergeIntervals {

        Interval[] merge(Interval[] intervals) {
            if (intervals == null || intervals.length == 0) {
                return new Interval[0];
            }

            Arrays.sort(intervals, Comparator.comparingInt(i -> i.start));
            List<Interval> merged = new ArrayList<>();

            int start = intervals[0].start;
            int end = intervals[0].end;

            for (int i = 1; i < intervals.length; i++) {
                Interval current = intervals[i];

                // Closed-range merge convention: touching endpoints are merged.
                if (current.start <= end) {
                    end = Math.max(end, current.end);
                } else {
                    merged.add(new Interval(start, end));
                    start = current.start;
                    end = current.end;
                }
            }

            merged.add(new Interval(start, end));
            return merged.toArray(new Interval[0]);
        }
    }

    // =========================================================================
    // INSERT INTERVAL
    // Goal: Insert into sorted non-overlapping intervals and merge if required.
    // =========================================================================

    // Rank 1 — Canonical linear scan; no re-sort needed when input invariant holds.
    static final class InsertInterval {

        Interval[] insert(Interval[] intervals, Interval newInterval) {
            if (newInterval == null) {
                return intervals == null ? new Interval[0] : Arrays.copyOf(intervals, intervals.length);
            }

            if (intervals == null || intervals.length == 0) {
                return new Interval[]{newInterval};
            }

            List<Interval> result = new ArrayList<>();
            int i = 0;

            while (i < intervals.length && intervals[i].end < newInterval.start) {
                result.add(intervals[i++]);
            }

            int start = newInterval.start;
            int end = newInterval.end;

            while (i < intervals.length && intervals[i].start <= end) {
                start = Math.min(start, intervals[i].start);
                end = Math.max(end, intervals[i].end);
                i++;
            }

            result.add(new Interval(start, end));

            while (i < intervals.length) {
                result.add(intervals[i++]);
            }

            return result.toArray(new Interval[0]);
        }
    }

    /*
     * MERGE / INSERT — RECALL
     * -----------------------
     * Asked to COMBINE ranges, not count overlap.
     *
     * Merge Intervals:
     *      sort by start -> extend current merged end while overlap continues.
     *
     * Insert Interval:
     *      input is already sorted + non-overlapping -> left / merge / right.
     *
     * Endpoint semantics are problem-specific.
     * These implementations use closed-range merge semantics: touching merges.
     */

    // =========================================================================
    // NON-OVERLAPPING INTERVALS
    // Goal: Minimum intervals to remove so the rest are mutually compatible.
    // =========================================================================

    // Rank 1 — Canonical: keep earliest-finishing compatible intervals.
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

    // =========================================================================
    // MINIMUM ARROWS TO BURST BALLOONS
    // Goal: Minimum points required to hit all closed intervals.
    // =========================================================================

    // Rank 1 — Canonical interval stabbing greedy: shoot at earliest end.
    static final class MinimumArrows {

        int findMinArrowShots(Interval[] balloons) {
            if (balloons == null || balloons.length == 0) {
                return 0;
            }

            Arrays.sort(balloons, Comparator.comparingInt(i -> i.end));

            int arrows = 1;
            int arrowPosition = balloons[0].end;

            for (int i = 1; i < balloons.length; i++) {
                // Closed intervals: start == arrowPosition is still hit.
                if (balloons[i].start > arrowPosition) {
                    arrows++;
                    arrowPosition = balloons[i].end;
                }
            }

            return arrows;
        }
    }

    /*
     * GREEDY-BY-END DISCRIMINATION
     * ----------------------------
     * Maximum compatible COUNT?
     *      -> keep earliest-finishing compatible interval.
     *
     * Minimum removals?
     *      -> same kept-set invariant; answer = n - kept.
     *
     * Minimum points/arrows to cover closed intervals?
     *      -> place each new point at earliest uncovered end.
     *
     * Same sort key, DIFFERENT output semantics.
     */

    // =========================================================================
    // CAR POOLING — WEIGHTED SWEEP LINE
    // Goal: Verify capacity under weighted pickup/drop events.
    // =========================================================================

    // Rank 1 — General sparse-coordinate sweep using ordered events.
    static final class CarPooling {

        boolean carPooling(int[][] trips, int capacity) {
            if (trips == null || trips.length == 0) {
                return true;
            }

            TreeMap<Integer, Integer> events = new TreeMap<>();

            for (int[] trip : trips) {
                int passengers = trip[0];
                int from = trip[1];
                int to = trip[2];

                events.merge(from, passengers, Integer::sum);
                events.merge(to, -passengers, Integer::sum);
            }

            int load = 0;

            for (int delta : events.values()) {
                load += delta;

                if (load > capacity) {
                    return false;
                }
            }

            return true;
        }
    }

    /*
     * SWEEP-LINE GENERALIZATION
     * -------------------------
     * Meeting Rooms II:
     *      start -> +1
     *      end   -> -1
     *
     * Car Pooling:
     *      pickup -> +passengers
     *      drop   -> -passengers
     *
     * This is the weighted generalization of peak-overlap counting.
     * Use a difference array instead when coordinates are small and bounded.
     */

    // =========================================================================
    // CORPORATE FLIGHT BOOKINGS — DIFFERENCE ARRAY
    // Goal: Aggregate weighted range additions over bounded discrete indices.
    // =========================================================================

    // Rank 1 — Difference array + prefix sum.
    static final class CorporateFlightBookings {

        int[] corpFlightBookings(int[][] bookings, int n) {
            if (n <= 0) {
                return new int[0];
            }

            int[] difference = new int[n + 1];

            if (bookings != null) {
                for (int[] booking : bookings) {
                    int first = booking[0] - 1;
                    int lastExclusive = booking[1];
                    int seats = booking[2];

                    difference[first] += seats;

                    if (lastExclusive < n) {
                        difference[lastExclusive] -= seats;
                    }
                }
            }

            int[] answer = new int[n];
            int running = 0;

            for (int i = 0; i < n; i++) {
                running += difference[i];
                answer[i] = running;
            }

            return answer;
        }
    }

    /*
     * SWEEP LINE vs DIFFERENCE ARRAY
     * ------------------------------
     * Sparse / arbitrary coordinates:
     *      -> ordered event map / sweep line.
     *
     * Small bounded integer coordinates:
     *      -> difference array + prefix sum.
     *
     * Same idea:
     *      range starts add weight; range end removes weight.
     */

    // =========================================================================
    // WEIGHTED INTERVAL SCHEDULING
    // Goal: Maximum profit from mutually compatible jobs.
    // =========================================================================

    static final class WeightedJob {
        final int start;
        final int end;
        final int profit;

        WeightedJob(int start, int end, int profit) {
            this.start = start;
            this.end = end;
            this.profit = profit;
        }
    }

    // Rank 1 — DP + binary search for the latest compatible predecessor.
    static final class WeightedIntervalScheduling {

        int maxProfit(WeightedJob[] jobs) {
            if (jobs == null || jobs.length == 0) {
                return 0;
            }

            Arrays.sort(jobs, Comparator.comparingInt(j -> j.end));

            int n = jobs.length;
            int[] ends = new int[n];
            int[] dp = new int[n + 1];

            for (int i = 0; i < n; i++) {
                ends[i] = jobs[i].end;
            }

            for (int i = 1; i <= n; i++) {
                WeightedJob current = jobs[i - 1];

                int compatibleCount = upperBound(
                        ends,
                        i - 1,
                        current.start
                );

                int take = current.profit + dp[compatibleCount];
                int skip = dp[i - 1];

                dp[i] = Math.max(take, skip);
            }

            return dp[n];
        }

        // Number of previous jobs with end <= target.
        private int upperBound(int[] sortedEnds, int exclusiveRight, int target) {
            int left = 0;
            int right = exclusiveRight;

            while (left < right) {
                int mid = left + (right - left) / 2;

                if (sortedEnds[mid] <= target) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            return left;
        }
    }

    /*
     * WEIGHTED SCHEDULING — PATTERN BOUNDARY
     * --------------------------------------
     * Maximize number of compatible intervals:
     *      -> Greedy by earliest finish.
     *
     * Maximize total PROFIT:
     *      -> Greedy fails in general.
     *      -> Sort by end + DP + predecessor search.
     */

    // =========================================================================
    // INTERVIEW DISCRIMINATION TABLE
    // =========================================================================

    /*
     * QUESTION ASKED                          PRIMARY PATTERN
     * -------------------------------------------------------------------------
     * Can all meetings be attended?          Sort + adjacent overlap detection
     * Minimum rooms/platforms/chairs?        Peak overlap / reusable resources
     * Merge overlapping ranges?              Sort by start + merge
     * Maximum compatible intervals?          Greedy by earliest finish
     * Minimum intervals to remove?           Greedy by earliest finish
     * Minimum arrows/points to cover?         Greedy by earliest end
     * Capacity changes by location/time?      Sweep Line / Difference Array
     * Maximum weighted compatible profit?     DP + predecessor search
     * Circular gain/cost feasibility?         Running balance greedy (Gas Station)
     *
     * FINITE-TIME INTERVIEW DECISION LOOP
     * -----------------------------------
     * 1. What exactly is the output: boolean, count, merge, selection, capacity?
     * 2. State the brute force.
     * 3. Identify what operation makes it expensive.
     * 4. Choose the pattern that removes that bottleneck.
     * 5. State one invariant before coding.
     * 6. Prefer the lowest-risk correct implementation you can explain.
     */

    // =========================================================================
    // RECALL SHEET
    // =========================================================================

    /*
     * Meeting Rooms I
     *      Detect conflict
     *      -> sort by START
     *      -> compare neighbors
     *
     * Meeting Rooms II
     *      Minimum resources = peak overlap
     *      -> default: active min heap of END times
     *      -> generalization: event sweep line
     *      -> count-only specialization: sorted starts + sorted ends
     *
     * Minimum Platforms
     *      Same as Meeting Rooms II
     *      -> arrival = start, departure = end
     *      -> verify tie semantics
     *
     * Interval Scheduling
     *      Maximize compatible COUNT
     *      -> sort by END
     *      -> accept if start >= lastFinish
     *
     * ONE-LINE MEMORY
     *      Detect?   -> sort + compare
     *      Allocate? -> active heap / sweep
     *      Merge?    -> sort + merge
     *      Select?   -> earliest finish
     *      Cover?    -> earliest-end stabbing
     *      Capacity? -> weighted sweep / difference array
     *      Profit?   -> weighted interval DP
     */

    // =========================================================================
    // TESTS
    // Run with assertions enabled:
    // java -ea -cp <classes> org.chijai.day1.Arrays.session4.Intervals.IntervalPatterns_V3
    // =========================================================================

    public static void main(String[] args) {

        MeetingRoomsISortAndScan meetingRoomsI = new MeetingRoomsISortAndScan();
        MeetingRoomsIIActiveHeap activeHeap = new MeetingRoomsIIActiveHeap();
        MeetingRoomsIISortedStartsEnds twoArrays = new MeetingRoomsIISortedStartsEnds();
        MeetingRoomsIIAllocatedHeap allocatedHeap = new MeetingRoomsIIAllocatedHeap();
        MeetingRoomsIISweepLine sweepLine = new MeetingRoomsIISweepLine();
        TransactionSchedulingGreedy scheduler = new TransactionSchedulingGreedy();
        MinimumPlatforms platforms = new MinimumPlatforms();
        MinimumPlatformsActiveHeap platformsHeap = new MinimumPlatformsActiveHeap();
        MinimumChairs chairs = new MinimumChairs();

        Interval[] overlap = {
                new Interval(0, 30),
                new Interval(5, 10),
                new Interval(15, 20)
        };

        assert !meetingRoomsI.canAttendMeetings(copy(overlap));
        assert activeHeap.minMeetingRooms(copy(overlap)) == 2;
        assert twoArrays.minMeetingRooms(copy(overlap)) == 2;
        assert allocatedHeap.minMeetingRooms(copy(overlap)) == 2;
        assert sweepLine.minMeetingRooms(copy(overlap)) == 2;

        Interval[] touching = {
                new Interval(1, 5),
                new Interval(5, 10),
                new Interval(10, 15)
        };

        assert meetingRoomsI.canAttendMeetings(copy(touching));
        assert activeHeap.minMeetingRooms(copy(touching)) == 1;
        assert twoArrays.minMeetingRooms(copy(touching)) == 1;
        assert allocatedHeap.minMeetingRooms(copy(touching)) == 1;
        assert sweepLine.minMeetingRooms(copy(touching)) == 1;

        Interval[] peakThenDrop = {
                new Interval(0, 100),
                new Interval(1, 90),
                new Interval(2, 80),
                new Interval(200, 201)
        };

        assert activeHeap.minMeetingRooms(copy(peakThenDrop)) == 3;
        assert twoArrays.minMeetingRooms(copy(peakThenDrop)) == 3;
        assert allocatedHeap.minMeetingRooms(copy(peakThenDrop)) == 3;
        assert sweepLine.minMeetingRooms(copy(peakThenDrop)) == 3;

        Interval[] transactions = {
                new Interval(1, 4),
                new Interval(3, 5),
                new Interval(0, 6),
                new Interval(5, 7),
                new Interval(8, 9),
                new Interval(5, 9)
        };

        assert scheduler.maximumTransactions(copy(transactions)) == 3;

        Interval[] greedyCounterExample = {
                new Interval(1, 10),
                new Interval(2, 3),
                new Interval(3, 4),
                new Interval(4, 5),
                new Interval(5, 6),
                new Interval(6, 7)
        };

        assert scheduler.maximumTransactions(copy(greedyCounterExample)) == 5;

        int[] arrivals = {900, 940, 950, 1100, 1500, 1800};
        int[] departures = {910, 1200, 1120, 1130, 1900, 2000};
        assert platformsHeap.minPlatforms(arrivals.clone(), departures.clone()) == 3;
        assert platforms.minPlatforms(arrivals.clone(), departures.clone()) == 3;


        Interval[] trainIntervals = {
                new Interval(900, 910),
                new Interval(940, 1200),
                new Interval(950, 1120),
                new Interval(1100, 1130),
                new Interval(1500, 1900),
                new Interval(1800, 2000)
        };
        assert platformsHeap.minPlatforms(copy(trainIntervals)) == 3;

        int[] touchingArrivals = {100, 200};
        int[] touchingDepartures = {200, 300};
        assert platformsHeap.minPlatforms(touchingArrivals.clone(), touchingDepartures.clone()) == 1;
        assert platforms.minPlatforms(touchingArrivals.clone(), touchingDepartures.clone()) == 1;


        Interval[] touchingTrains = {
                new Interval(100, 200),
                new Interval(200, 300)
        };
        assert platformsHeap.minPlatforms(copy(touchingTrains)) == 1;
        assert chairs.minChairs(copy(overlap)) == 2;

        // ------------------------------------------------------------
        // Merge / Insert Intervals
        // ------------------------------------------------------------

        MergeIntervals mergeIntervals = new MergeIntervals();
        Interval[] merged = mergeIntervals.merge(new Interval[]{
                new Interval(1, 3),
                new Interval(2, 6),
                new Interval(8, 10),
                new Interval(15, 18)
        });
        assert sameIntervals(merged, new int[][]{{1, 6}, {8, 10}, {15, 18}});

        InsertInterval insertInterval = new InsertInterval();
        Interval[] inserted = insertInterval.insert(new Interval[]{
                new Interval(1, 3),
                new Interval(6, 9)
        }, new Interval(2, 5));
        assert sameIntervals(inserted, new int[][]{{1, 5}, {6, 9}});

        // ------------------------------------------------------------
        // Greedy by End
        // ------------------------------------------------------------

        NonOverlappingIntervals nonOverlap = new NonOverlappingIntervals();
        assert nonOverlap.eraseOverlapIntervals(new Interval[]{
                new Interval(1, 2),
                new Interval(2, 3),
                new Interval(3, 4),
                new Interval(1, 3)
        }) == 1;

        MinimumArrows arrows = new MinimumArrows();
        assert arrows.findMinArrowShots(new Interval[]{
                new Interval(10, 16),
                new Interval(2, 8),
                new Interval(1, 6),
                new Interval(7, 12)
        }) == 2;

        // ------------------------------------------------------------
        // Weighted Sweep / Weighted Scheduling
        // ------------------------------------------------------------

        CarPooling carPooling = new CarPooling();
        assert !carPooling.carPooling(new int[][]{
                {2, 1, 5},
                {3, 3, 7}
        }, 4);
        assert carPooling.carPooling(new int[][]{
                {2, 1, 5},
                {3, 3, 7}
        }, 5);

        CorporateFlightBookings flightBookings = new CorporateFlightBookings();
        assert Arrays.equals(
                flightBookings.corpFlightBookings(new int[][]{
                        {1, 2, 10},
                        {2, 3, 20},
                        {2, 5, 25}
                }, 5),
                new int[]{10, 55, 45, 25, 25}
        );

        WeightedIntervalScheduling weighted = new WeightedIntervalScheduling();
        assert weighted.maxProfit(new WeightedJob[]{
                new WeightedJob(1, 3, 50),
                new WeightedJob(2, 4, 10),
                new WeightedJob(3, 5, 40),
                new WeightedJob(3, 6, 70)
        }) == 120;

        assert meetingRoomsI.canAttendMeetings(new Interval[]{});
        assert activeHeap.minMeetingRooms(new Interval[]{}) == 0;
        assert scheduler.maximumTransactions(new Interval[]{}) == 0;

        System.out.println("All assertions passed.");
    }

    private static boolean sameIntervals(Interval[] actual, int[][] expected) {
        if (actual.length != expected.length) {
            return false;
        }

        for (int i = 0; i < actual.length; i++) {
            if (actual[i].start != expected[i][0] || actual[i].end != expected[i][1]) {
                return false;
            }
        }

        return true;
    }

    private static Interval[] copy(Interval[] intervals) {
        return Arrays.copyOf(intervals, intervals.length);
    }
}
