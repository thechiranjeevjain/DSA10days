package org.chijai.day1.session2;

import java.util.*;

/**
 * =============================================================================
 * MeetingRooms
 * =============================================================================
 *
 * 📘 PRIMARY PROBLEM
 * -----------------------------------------------------------------------------
 * Title:
 * Meeting Rooms I
 *
 * Difficulty:
 * Easy
 *
 * Tags:
 * Interval Scheduling
 * Sorting
 * Greedy
 *
 * -----------------------------------------------------------------------------
 * Problem Description
 *
 * Given an array of meeting time intervals where
 *
 * interval[i] = [start, end]
 *
 * determine whether a single person can attend every meeting.
 *
 * A person can attend all meetings only if no two meetings overlap.
 *
 * Meetings touching at endpoints are NOT overlapping.
 *
 * Example:
 *
 * [0,10]
 * [10,20]
 *
 * is valid because the first meeting finishes exactly when the second begins.
 *
 * -----------------------------------------------------------------------------
 * Constraints
 *
 * • 1 <= n <= 100000
 * • 0 <= start < end <= 10^9
 *
 * -----------------------------------------------------------------------------
 * Examples
 *
 * Example 1
 *
 * Input:
 * [[0,30],[5,10],[15,20]]
 *
 * Output:
 * false
 *
 * Explanation:
 * First meeting overlaps second.
 *
 * ------------------------------------------------
 *
 * Example 2
 *
 * Input:
 * [[7,10],[2,4]]
 *
 * Output:
 * true
 *
 * ------------------------------------------------
 *
 * Example 3
 *
 * Input:
 * [[1,5],[5,8],[8,10]]
 *
 * Output:
 * true
 *
 * -----------------------------------------------------------------------------
 * Official Problem
 *
 * https://leetcode.com/problems/meeting-rooms/
 *
 *
 * =============================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * =============================================================================
 *
 * Pattern
 * -------
 * Sort + Adjacent Interval Verification
 *
 * Archetype
 * ---------
 * Greedy ordering followed by local verification.
 *
 * Core Invariant
 * --------------
 * After sorting by start time, every possible overlap must appear between
 * neighboring intervals.
 *
 * Therefore only adjacent intervals need comparison.
 *
 * Why It Works
 * ------------
 * Suppose interval A overlaps interval C.
 *
 * Since intervals are sorted by start time,
 *
 * every interval beginning between A and C must also lie between them.
 *
 * Thus the earliest possible conflicting interval is always adjacent after
 * sorting.
 *
 * Recognition Signals
 * -------------------
 * • Interval input
 * • Detect overlap
 * • Need only yes/no
 * • Order can be changed
 *
 * Use When
 * --------
 * • Overlap detection
 * • Calendar validation
 * • Schedule verification
 *
 * Do NOT Use
 * ----------
 * • Dynamic insertion
 * • Online scheduling
 * • Room allocation
 * • Weighted scheduling
 *
 * Comparison
 * ----------
 * Meeting Rooms I
 *     Sort + verify.
 *
 * Meeting Rooms II
 *     Sort + maintain active meetings.
 *
 * Merge Intervals
 *     Sort + merge.
 *
 * Interval Intersection
 *     Two pointers.
 *
 *
 * =============================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * =============================================================================
 *
 * Mental Model
 * ------------
 * Imagine laying every meeting on a timeline.
 *
 * After sorting,
 *
 * every meeting only needs to look at its immediate predecessor.
 *
 * If no adjacent meetings overlap,
 * no distant meetings can overlap either.
 *
 * ------------------------------------------------
 *
 * State
 *
 * previousEnd
 *
 * End time of the previous interval in sorted order.
 *
 * ------------------------------------------------
 *
 * Search Space
 *
 * Remaining sorted meetings.
 *
 * ------------------------------------------------
 *
 * Transition
 *
 * Compare
 *
 * current.start
 *
 * against
 *
 * previous.end
 *
 * ------------------------------------------------
 *
 * Invariant 1
 *
 * All processed meetings are mutually compatible.
 *
 * ------------------------------------------------
 *
 * Invariant 2
 *
 * previousEnd belongs to the latest processed interval.
 *
 * ------------------------------------------------
 *
 * Invariant 3
 *
 * Every overlap must appear between neighbors after sorting.
 *
 * ------------------------------------------------
 *
 * Allowed Move
 *
 * Advance if
 *
 * current.start >= previousEnd
 *
 * ------------------------------------------------
 *
 * Forbidden Move
 *
 * Ignore sorting.
 *
 * Without sorting,
 * neighboring comparisons become meaningless.
 *
 * ------------------------------------------------
 *
 * Termination
 *
 * Finish entire scan without overlap.
 *
 * ------------------------------------------------
 *
 * Correctness Intuition
 *
 * Sorting transforms a global overlap problem into local comparisons.
 *
 * ------------------------------------------------
 *
 * Why Naive Solutions Fail
 *
 * Comparing every pair works,
 * but wastes O(n²).
 *
 * The sorted invariant removes unnecessary comparisons.
 *
 *
 * =============================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * =============================================================================
 *
 * Mistake 1
 *
 * Compare unsorted intervals.
 *
 * Appears reasonable because overlaps are pairwise.
 *
 * Violated invariant:
 *
 * Adjacent intervals are no longer meaningful.
 *
 * Counterexample
 *
 * [5,6]
 * [1,3]
 * [3,5]
 *
 * ------------------------------------------------
 *
 * Mistake 2
 *
 * Use
 *
 * current.start <= previous.end
 *
 * as overlap.
 *
 * Counterexample
 *
 * [1,5]
 * [5,7]
 *
 * Endpoints touching are allowed.
 *
 * ------------------------------------------------
 *
 * Mistake 3
 *
 * Sort by end.
 *
 * Adjacent intervals after end sorting are not guaranteed to reveal all
 * overlaps.
 *
 *
 * =============================================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * =============================================================================
 *
 * Typing Order
 *
 * 1.
 * Method declaration.
 *
 * 2.
 * Handle small input.
 *
 * 3.
 * Sort by start.
 *
 * 4.
 * Initialize previousEnd.
 *
 * 5.
 * Scan remaining intervals.
 *
 * 6.
 * Detect overlap.
 *
 * 7.
 * Update previousEnd.
 *
 * 8.
 * Return true.
 *
 * Loop Skeleton
 *
 * for every interval after first
 *
 * compare start with previousEnd
 *
 * update previousEnd
 *
 * Return Skeleton
 *
 * overlap -> false
 *
 * finished -> true
 *
 *
 * =============================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * =============================================================================
 *
 * sort
 *
 * prev = first.end
 *
 * loop
 *
 * if overlap
 *     return false
 *
 * prev = current.end
 *
 * return true
 *
 */
public class MeetingRoom {

    /**
     * Simple interval model reused by every problem in this chapter.
     */
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
    // Solution 1
    // Brute Force
    // =========================================================================

    static final class BruteForceMeetingRooms {

        /*
         * Idea
         * ----
         * Compare every pair.
         *
         * Invariant
         * ---------
         * Any overlap must eventually be examined.
         *
         * Limitation
         * ----------
         * Quadratic comparisons.
         *
         * Complexity
         * ----------
         * Time : O(n²)
         * Space: O(1)
         *
         * Interview Usefulness
         * --------------------
         * Good starting point before optimization.
         */

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

            return Math.max(a.start, b.start)
                    < Math.min(a.end, b.end);
        }
    }

    // =========================================================================
    // Solution 2
    // Improved
    // =========================================================================

    static final class ImprovedMeetingRooms {

        /*
         * Idea
         * ----
         * Sort by start then compare neighbors.
         *
         * Invariant
         * ---------
         * Every remaining overlap appears between adjacent intervals.
         *
         * Improvement
         * -----------
         * O(n²) -> O(n log n)
         *
         * Complexity
         * ----------
         * Time : O(n log n)
         * Space: O(log n) sort stack
         */

        boolean canAttendMeetings(Interval[] meetings) {

            if (meetings == null || meetings.length <= 1) {
                return true;
            }

            Arrays.sort(meetings, Comparator.comparingInt(i -> i.start));

            for (int i = 1; i < meetings.length; i++) {

                // 🟢 Invariant:
                // Earlier meetings are already conflict free.

                if (meetings[i].start < meetings[i - 1].end) {
                    return false;
                }
            }

            return true;
        }
    }

    // =========================================================================
    // Solution 3
    // Optimal (Interview Preferred)
    // =========================================================================

    static final class OptimalMeetingRooms {

        /*
         * Idea
         * ----
         * Same asymptotic complexity with explicit invariant visibility.
         *
         * Correctness
         * -----------
         * previousEnd always belongs to the latest compatible meeting.
         *
         * Complexity
         * ----------
         * Time : O(n log n)
         * Space: O(log n)
         */

        boolean canAttendMeetings(Interval[] meetings) {

            // Empty schedule is always feasible.
            if (meetings == null || meetings.length <= 1) {
                return true;
            }

            Arrays.sort(meetings, Comparator.comparingInt(i -> i.start));

            int previousEnd = meetings[0].end;

            for (int i = 1; i < meetings.length; i++) {

                Interval current = meetings[i];

                // 🟢 Invariant:
                // Answer still depends only on remaining intervals.

                if (current.start < previousEnd) {
                    return false;
                }

                // Maintain invariant for next comparison.
                previousEnd = current.end;
            }

            return true;
        }
    }


// =========================================================================
// 🟣 INTERVIEW ARTICULATION
// =========================================================================

/*
 * Explain Like a Senior Engineer
 * ------------------------------
 *
 * Pattern
 * -------
 * Sorting converts a global interval relationship into a local verification
 * problem.
 *
 * ------------------------------------------------
 *
 * Invariant
 * ---------
 * After sorting by start time,
 * every possible overlap must involve adjacent intervals.
 *
 * ------------------------------------------------
 *
 * Why Adjacent Comparison Is Enough
 * --------------------------------
 *
 * Suppose interval A overlaps interval C.
 *
 * Since starts are sorted,
 *
 * every interval beginning between A and C is positioned between them.
 *
 * Therefore C cannot become the first conflicting interval without one of
 * the intervals before it already conflicting.
 *
 * Hence checking neighbors is sufficient.
 *
 * ------------------------------------------------
 *
 * Correctness
 * -----------
 *
 * Every overlap is detected.
 *
 * Every non-overlap passes.
 *
 * Therefore the algorithm is both sound and complete.
 *
 * ------------------------------------------------
 *
 * Termination
 * -----------
 *
 * Single linear scan after sorting.
 *
 * ------------------------------------------------
 *
 * In-place Feasibility
 * --------------------
 *
 * Yes.
 *
 * Sorting modifies the interval array.
 *
 * ------------------------------------------------
 *
 * Streaming Feasibility
 * ---------------------
 *
 * No.
 *
 * Future intervals may begin earlier than already processed intervals.
 *
 * Full ordering is required.
 *
 * ------------------------------------------------
 *
 * When NOT To Use
 * ---------------
 *
 * • Need minimum rooms
 * • Dynamic insertion
 * • Online calendar
 * • Resource allocation
 *
 */


// =========================================================================
// 🎯 INTERVIEW RECALL SHEET
// =========================================================================

/*
 * Trigger
 * -------
 * Can one person attend every meeting?
 *
 * Pattern
 * -------
 * Sort + Adjacent Verification
 *
 * Invariant
 * ---------
 * Adjacent intervals expose every overlap.
 *
 * Search Target
 * -------------
 * First conflicting neighbor.
 *
 * Discard Rule
 * ------------
 * Once neighbors do not overlap,
 * earlier intervals never need revisiting.
 *
 * Common Trap
 * -----------
 * Using <= instead of <
 *
 * Edge Cases
 * ----------
 * Empty
 * One interval
 * Equal endpoints
 *
 * One-liner
 * ---------
 * Sort by start.
 * Compare neighbors.
 *
 * Re-derivation Cue
 * -----------------
 * Global overlap becomes local after ordering.
 */


// =========================================================================
// 🔄 VARIATIONS & TWEAKS
// =========================================================================

/*
 * Variation
 * ---------
 * Return conflicting intervals.
 *
 * Invariant
 * ---------
 * Same.
 *
 * Only returned information changes.
 *
 * ------------------------------------------------
 *
 * Variation
 * ---------
 * Count overlaps.
 *
 * Still compare adjacent intervals.
 *
 * ------------------------------------------------
 *
 * Variation
 * ---------
 * Remove minimum intervals.
 *
 * Pattern changes.
 *
 * Greedy by end time.
 *
 * ------------------------------------------------
 *
 * Variation
 * ---------
 * Dynamic insertion.
 *
 * Pattern breaks.
 *
 * Balanced tree required.
 *
 */


// =========================================================================
// 🧠 MASTERY CHECKLIST
// =========================================================================

/*
 * □ I know the invariant.
 *
 * □ I know why sorting works.
 *
 * □ I know why neighbors are sufficient.
 *
 * □ I know the discard rule.
 *
 * □ I know why <= is wrong.
 *
 * □ I know the termination argument.
 *
 * □ I can derive correctness.
 *
 * □ I can debug endpoint mistakes.
 *
 * □ I know where this pattern stops working.
 */


// =========================================================================
// =========================================================================
//                       MEETING ROOMS II
// =========================================================================
// =========================================================================

/*
 * =============================================================================
 * 📘 PRIMARY PROBLEM
 * =============================================================================
 *
 * Title
 * -----
 * Meeting Rooms II
 *
 * Difficulty
 * ----------
 * Medium
 *
 * Tags
 * ----
 * Heap
 * Greedy
 * Sweep Line
 * Sorting
 *
 * -----------------------------------------------------------------------------
 *
 * Problem
 * -------
 *
 * Given meeting intervals,
 *
 * determine the minimum number of conference rooms required so that every
 * meeting can be held.
 *
 * -----------------------------------------------------------------------------
 *
 * Example
 *
 * [[0,30],[5,10],[15,20]]
 *
 * Answer
 *
 * 2
 *
 * -----------------------------------------------------------------------------
 *
 * Example
 *
 * [[7,10],[2,4]]
 *
 * Answer
 *
 * 1
 *
 * -----------------------------------------------------------------------------
 *
 * Official
 *
 * https://leetcode.com/problems/meeting-rooms-ii/
 *
 */


// =========================================================================
// 🔵 CORE PATTERN OVERVIEW
// =========================================================================

/*
 * Pattern
 * -------
 * Greedy Resource Allocation
 *
 * Archetype
 * ---------
 * Maintain all currently active intervals.
 *
 * Core Invariant
 * --------------
 * Heap always stores exactly the meetings that are still occupying rooms.
 *
 * Heap minimum
 * ------------
 * Earliest room becoming available.
 *
 * Why It Works
 * ------------
 *
 * Whenever the earliest finishing meeting has ended,
 * its room may immediately be reused.
 *
 * Recognition Signals
 * -------------------
 *
 * • Minimum resources
 * • Concurrent intervals
 * • Active windows
 * • Room allocation
 *
 * Use When
 * --------
 *
 * Active intervals matter.
 *
 * Do NOT Use
 * ----------
 *
 * Simple overlap detection.
 *
 * Meeting Rooms I is sufficient there.
 */


// =========================================================================
// 🟢 MENTAL MODEL & INVARIANTS
// =========================================================================

/*
 * Mental Model
 * ------------
 *
 * Imagine each room has a clock showing when it becomes free.
 *
 * The heap stores these clocks.
 *
 * Smallest clock
 * =
 * first available room.
 *
 * ------------------------------------------------
 *
 * State
 *
 * Min Heap of ending times.
 *
 * ------------------------------------------------
 *
 * Search Space
 *
 * Remaining meetings in chronological order.
 *
 * ------------------------------------------------
 *
 * Transition
 *
 * Compare
 *
 * current.start
 *
 * with
 *
 * earliestEnding.
 *
 * ------------------------------------------------
 *
 * Invariant 1
 *
 * Heap contains every currently occupied room.
 *
 * ------------------------------------------------
 *
 * Invariant 2
 *
 * Heap minimum is the first room that can become reusable.
 *
 * ------------------------------------------------
 *
 * Invariant 3
 *
 * Heap size equals number of simultaneously active meetings.
 *
 * ------------------------------------------------
 *
 * Goal
 *
 * Maximum heap size observed.
 *
 * That maximum equals minimum rooms required.
 *
 * ------------------------------------------------
 *
 * Allowed Move
 *
 * Remove every meeting already finished.
 *
 * ------------------------------------------------
 *
 * Forbidden Move
 *
 * Remove only one finished meeting.
 *
 * Multiple rooms may become free before the current meeting begins.
 *
 * ------------------------------------------------
 *
 * Termination
 *
 * Every meeting processed exactly once.
 *
 * ------------------------------------------------
 *
 * Why Naive Fails
 *
 * Pairwise overlap counting double-counts resources because room reuse is
 * ignored.
 */


// =========================================================================
// 🔴 WHY WRONG SOLUTIONS FAIL
// =========================================================================

/*
 * Mistake 1
 *
 * Pop only one finished meeting.
 *
 * Counterexample
 *
 * [1,2]
 * [2,3]
 * [3,4]
 * [10,20]
 *
 * Three rooms would incorrectly remain occupied.
 *
 * ------------------------------------------------
 *
 * Mistake 2
 *
 * Keep maximum ending time instead of minimum.
 *
 * Earliest reusable room becomes invisible.
 *
 * ------------------------------------------------
 *
 * Mistake 3
 *
 * Forget sorting.
 *
 * Heap loses chronological meaning.
 *
 */


// =========================================================================
// ⚙ IMPLEMENTATION BLUEPRINT
// =========================================================================

/*
 * Typing Order
 *
 * Method
 *
 * Sort
 *
 * Create min heap
 *
 * Iterate meetings
 *
 * Remove every finished meeting
 *
 * Add current ending time
 *
 * Update answer
 *
 * Return answer
 */

    // =========================================================================
    // 🧾 ULTRA-COMPACT PSEUDOCODE
    // =========================================================================

    /*
     * sort
     *
     * heap = empty
     *
     * answer = 0
     *
     * for meeting
     *
     *     while earliestEnd <= start
     *         pop
     *
     *     push(end)
     *
     *     answer = max(answer, heapSize)
     *
     * return answer
     */


    // =========================================================================
    // Solution 1
    // Brute Force
    // =========================================================================

    static final class BruteForceMeetingRoomsII {

        /*
         * Idea
         * ----
         * Simulate room assignment by scanning every existing room.
         *
         * Invariant
         * ---------
         * rooms[i] stores the ending time of the latest meeting assigned to
         * that room.
         *
         * Limitation
         * ----------
         * Every meeting may inspect every room.
         *
         * Complexity
         * ----------
         * Time : O(n²)
         * Space: O(n)
         *
         * Interview Usefulness
         * --------------------
         * Good derivation before introducing a heap.
         */

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


    // =========================================================================
    // Solution 2
    // Improved
    // =========================================================================

    static final class ImprovedMeetingRoomsII {

        /*
         * Idea
         * ----
         * Always reuse the room that becomes free first.
         *
         * Invariant
         * ---------
         * Heap minimum is the earliest reusable room.
         *
         * Improvement
         * -----------
         * Linear room search becomes logarithmic.
         *
         * Complexity
         * ----------
         * Time : O(n log n)
         * Space: O(n)
         */

        int minMeetingRooms(Interval[] meetings) {

            if (meetings == null || meetings.length == 0) {
                return 0;
            }

            Arrays.sort(meetings, Comparator.comparingInt(i -> i.start));

            PriorityQueue<Integer> minHeap = new PriorityQueue<>();

            for (Interval meeting : meetings) {

                while (!minHeap.isEmpty()
                        && minHeap.peek() <= meeting.start) {

                    minHeap.poll();
                }

                minHeap.offer(meeting.end);
            }

            return minHeap.size();
        }
    }


    // =========================================================================
    // Solution 3
    // Optimal (Interview Preferred)
    // =========================================================================

    static final class OptimalMeetingRoomsII {

        /*
         * Idea
         * ----
         * Heap tracks every active meeting.
         *
         * Maximum active meetings
         * =
         * Minimum required rooms.
         *
         * Correctness
         * -----------
         * Active meetings occupy rooms.
         *
         * Finished meetings immediately release rooms.
         *
         * Complexity
         * ----------
         * Time : O(n log n)
         * Space: O(n)
         */

        int minMeetingRooms(Interval[] meetings) {

            if (meetings == null || meetings.length == 0) {
                return 0;
            }

            Arrays.sort(meetings, Comparator.comparingInt(i -> i.start));

            PriorityQueue<Integer> activeMeetings = new PriorityQueue<>();

            int maximumRooms = 0;

            for (Interval current : meetings) {

                // 🟢 Invariant:
                // Heap contains only currently active meetings.

                while (!activeMeetings.isEmpty()
                        && activeMeetings.peek() <= current.start) {

                    // Free every reusable room.
                    activeMeetings.poll();
                }

                // Current meeting occupies one room.
                activeMeetings.offer(current.end);

                // Peak simultaneous occupancy.
                maximumRooms = Math.max(maximumRooms,
                        activeMeetings.size());
            }

            return maximumRooms;
        }
    }


// =========================================================================
// 🟣 INTERVIEW ARTICULATION
// =========================================================================

/*
 * Pattern
 * -------
 * Greedy Resource Allocation.
 *
 * ------------------------------------------------
 *
 * Invariant
 * ---------
 * Heap stores exactly the meetings currently occupying rooms.
 *
 * ------------------------------------------------
 *
 * Discard Rule
 * ------------
 * Every meeting whose end <= current.start
 * can never affect future room allocation.
 *
 * Remove it permanently.
 *
 * ------------------------------------------------
 *
 * Correctness
 * -----------
 * Heap size equals concurrent meetings.
 *
 * Maximum concurrency equals minimum rooms.
 *
 * ------------------------------------------------
 *
 * Why Minimum End?
 * ----------------
 * Only the earliest finishing room can possibly be reused first.
 *
 * Any later-ending room cannot help before it.
 *
 * ------------------------------------------------
 *
 * Termination
 * -----------
 * Each meeting enters and leaves the heap once.
 *
 * ------------------------------------------------
 *
 * Streaming
 * ---------
 * Possible if meetings already arrive sorted by start time.
 *
 * Otherwise ordering is required.
 */


// =========================================================================
// 🎯 INTERVIEW RECALL SHEET
// =========================================================================

/*
 * Trigger
 * -------
 * Minimum rooms.
 *
 * Pattern
 * -------
 * Min Heap.
 *
 * Invariant
 * ---------
 * Heap == active meetings.
 *
 * Search Target
 * -------------
 * Earliest ending room.
 *
 * Discard Rule
 * ------------
 * Remove every finished meeting.
 *
 * Common Trap
 * -----------
 * Poll only once.
 *
 * Edge Cases
 * ----------
 * Empty input
 * Equal endpoints
 * Fully overlapping meetings
 *
 * One-liner
 * ---------
 * Heap size equals current room usage.
 *
 * Re-derivation Cue
 * -----------------
 * Rooms become reusable in end-time order.
 */


// =========================================================================
// 🔄 VARIATIONS & TWEAKS
// =========================================================================

/*
 * Variant
 * -------
 * Return room assignment.
 *
 * Store
 *
 * (endTime, roomId)
 *
 * in heap.
 *
 * ------------------------------------------------
 *
 * Variant
 * -------
 * Maximum overlap.
 *
 * Same invariant.
 *
 * Heap size already computes it.
 *
 * ------------------------------------------------
 *
 * Variant
 * -------
 * CPU scheduling.
 *
 * Replace room with processor.
 *
 * Same resource invariant.
 *
 * ------------------------------------------------
 *
 * Variant
 * -------
 * Hotel booking.
 *
 * Replace meeting with reservation.
 *
 * Same algorithm.
 */


// =========================================================================
// 🧠 MASTERY CHECKLIST
// =========================================================================

/*
 * □ Heap contains active meetings.
 *
 * □ Heap minimum is earliest reusable room.
 *
 * □ Maximum heap size is answer.
 *
 * □ Remove every finished meeting.
 *
 * □ Sorting is mandatory.
 *
 * □ I can explain correctness.
 *
 * □ I can derive complexity.
 *
 * □ I know why a max heap fails.
 */


// =========================================================================
// =========================================================================
//                     TRANSACTION SCHEDULING
// =========================================================================
// =========================================================================

/*
 * =============================================================================
 * 📘 PRIMARY PROBLEM
 * =============================================================================
 *
 * Title
 * -----
 * Transaction Scheduling
 *
 * Difficulty
 * ----------
 * Medium
 *
 * Pattern
 * -------
 * Interval Scheduling
 *
 * Problem
 * -------
 *
 * Given transactions represented as execution windows
 *
 * [start, finish]
 *
 * schedule the maximum number of mutually compatible transactions.
 *
 * Two transactions are compatible when
 *
 * previous.finish <= next.start.
 *
 * Goal
 * ----
 * Maximize completed transactions.
 *
 * This is the classical interval scheduling problem.
 */

    // =========================================================================
    // 🔵 CORE PATTERN OVERVIEW
    // =========================================================================

    /*
     * Pattern
     * -------
     * Greedy by Earliest Finish Time
     *
     * Archetype
     * ---------
     * Earliest finishing activity leaves maximum opportunity for future
     * activities.
     *
     * Core Invariant
     * --------------
     * After choosing the earliest finishing compatible transaction,
     * the remaining scheduling problem is structurally identical.
     *
     * Why It Works
     * ------------
     * Every later-finishing compatible transaction blocks at least as much
     * future search space.
     *
     * Therefore the earliest finishing compatible transaction is always a
     * safe greedy choice.
     *
     * Recognition Signals
     * -------------------
     * • Maximize completed intervals
     * • Choose compatible jobs
     * • One machine
     * • No weights
     *
     * Use When
     * --------
     * Unweighted interval scheduling.
     *
     * Do NOT Use
     * ----------
     * Weighted profits.
     * Multiple machines.
     * Preemption.
     */


    // =========================================================================
    // 🟢 MENTAL MODEL & INVARIANTS
    // =========================================================================

    /*
     * Mental Model
     * ------------
     * Imagine every accepted transaction permanently occupies the timeline.
     *
     * The earlier it finishes,
     * the larger the remaining free timeline.
     *
     * ------------------------------------------------
     *
     * State
     * -----
     * lastFinish
     *
     * End time of the latest accepted transaction.
     *
     * ------------------------------------------------
     *
     * Search Space
     * ------------
     * Remaining transactions ordered by finish time.
     *
     * ------------------------------------------------
     *
     * Transition
     * ----------
     * Accept only if
     *
     * current.start >= lastFinish.
     *
     * ------------------------------------------------
     *
     * Invariant 1
     * -----------
     * Accepted transactions never overlap.
     *
     * ------------------------------------------------
     *
     * Invariant 2
     * -----------
     * lastFinish belongs to the last accepted transaction.
     *
     * ------------------------------------------------
     *
     * Invariant 3
     * -----------
     * Every accepted transaction finishes as early as possible among all
     * remaining compatible choices.
     *
     * ------------------------------------------------
     *
     * Allowed Move
     * ------------
     * Accept compatible transaction.
     *
     * ------------------------------------------------
     *
     * Forbidden Move
     * --------------
     * Accept incompatible transaction.
     *
     * ------------------------------------------------
     *
     * Termination
     * -----------
     * Every transaction inspected exactly once.
     *
     * ------------------------------------------------
     *
     * Why Naive Solutions Fail
     * ------------------------
     * Picking earliest starting transaction may block many later
     * compatible transactions.
     */


    // =========================================================================
    // 🔴 WHY WRONG SOLUTIONS FAIL
    // =========================================================================

    /*
     * Mistake 1
     * ---------
     * Sort by start time.
     *
     * Counterexample
     *
     * [1,10]
     * [2,3]
     * [3,4]
     * [4,5]
     *
     * Earliest start chooses only one transaction.
     *
     * Greedy by finish chooses three.
     *
     * ------------------------------------------------
     *
     * Mistake 2
     * ---------
     * Choose shortest duration.
     *
     * Duration does not imply earliest release.
     *
     * ------------------------------------------------
     *
     * Mistake 3
     * ---------
     * Choose earliest compatible arbitrarily.
     *
     * Greedy proof disappears.
     */


    // =========================================================================
    // ⚙ IMPLEMENTATION BLUEPRINT
    // =========================================================================

    /*
     * Typing Order
     *
     * Method
     *
     * Sort by finish
     *
     * lastFinish = -INF
     *
     * answer = 0
     *
     * Iterate
     *
     * If compatible
     *     accept
     *     update lastFinish
     *     increment answer
     *
     * Return answer
     */


    // =========================================================================
    // 🧾 ULTRA-COMPACT PSEUDOCODE
    // =========================================================================

    /*
     * sort by finish
     *
     * lastFinish = -INF
     *
     * answer = 0
     *
     * loop
     *
     *     if compatible
     *         accept
     *         update finish
     *
     * return answer
     */


    // =========================================================================
    // Solution 1
    // Brute Force
    // =========================================================================

    static final class BruteForceTransactionScheduling {

        /*
         * Idea
         * ----
         * Explore every subset.
         *
         * Invariant
         * ---------
         * Every feasible schedule is eventually explored.
         *
         * Limitation
         * ----------
         * Exponential search.
         *
         * Complexity
         * ----------
         * Time : O(2^n)
         * Space: O(n)
         */

        int maximumTransactions(Interval[] transactions) {

            Interval[] copy = Arrays.copyOf(transactions, transactions.length);

            Arrays.sort(copy, Comparator.comparingInt(i -> i.start));

            return dfs(copy, 0, Integer.MIN_VALUE);
        }

        private int dfs(Interval[] intervals,
                        int index,
                        int lastFinish) {

            if (index == intervals.length) {
                return 0;
            }

            int skip = dfs(intervals, index + 1, lastFinish);

            int take = 0;

            if (intervals[index].start >= lastFinish) {

                take = 1 + dfs(intervals,
                        index + 1,
                        intervals[index].end);
            }

            return Math.max(skip, take);
        }
    }


    // =========================================================================
    // Solution 2
    // Improved
    // =========================================================================

    static final class ImprovedTransactionScheduling {

        /*
         * Idea
         * ----
         * Earliest finishing compatible transaction.
         *
         * Complexity
         * ----------
         * Time : O(n log n)
         * Space: O(log n)
         */

        int maximumTransactions(Interval[] transactions) {

            if (transactions == null || transactions.length == 0) {
                return 0;
            }

            Arrays.sort(transactions,
                    Comparator.comparingInt(i -> i.end));

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


    // =========================================================================
    // Solution 3
    // Optimal (Interview Preferred)
    // =========================================================================

    static final class OptimalTransactionScheduling {

        /*
         * Idea
         * ----
         * Always preserve the maximum future search space.
         *
         * Correctness
         * -----------
         * Earliest finish dominates every other compatible greedy choice.
         *
         * Complexity
         * ----------
         * Time : O(n log n)
         * Space: O(log n)
         */

        int maximumTransactions(Interval[] transactions) {

            if (transactions == null || transactions.length == 0) {
                return 0;
            }

            Arrays.sort(transactions,
                    Comparator.comparingInt(i -> i.end));

            int accepted = 0;

            int lastFinish = Integer.MIN_VALUE;

            for (Interval current : transactions) {

                // 🟢 Invariant:
                // Accepted transactions never overlap.

                if (current.start < lastFinish) {

                    // Reject incompatible transaction.
                    continue;
                }

                // Earliest finishing compatible choice accepted.
                accepted++;

                lastFinish = current.end;
            }

            return accepted;
        }
    }

// =========================================================================
// 🟣 INTERVIEW ARTICULATION
// =========================================================================

/*
 * Pattern
 * -------
 * Greedy Interval Scheduling.
 *
 * ------------------------------------------------
 *
 * Invariant
 * ---------
 * Every accepted transaction is compatible with every previously accepted
 * transaction.
 *
 * lastFinish always equals the end time of the latest accepted
 * transaction.
 *
 * ------------------------------------------------
 *
 * Greedy Choice
 * -------------
 * Among all compatible transactions,
 * choose the one finishing earliest.
 *
 * That leaves the largest remaining search space.
 *
 * ------------------------------------------------
 *
 * Why Earliest Finish?
 * --------------------
 * A later finishing transaction cannot increase future choices.
 *
 * It can only eliminate opportunities.
 *
 * ------------------------------------------------
 *
 * Correctness
 * -----------
 * Exchange Argument.
 *
 * If an optimal schedule begins with another compatible transaction that
 * finishes later,
 * replacing it with the earliest finishing transaction cannot reduce the
 * number of future compatible transactions.
 *
 * Therefore the greedy choice is always safe.
 *
 * ------------------------------------------------
 *
 * Termination
 * -----------
 * Every interval is processed once after sorting.
 *
 * ------------------------------------------------
 *
 * Streaming Feasibility
 * ---------------------
 * Possible only if transactions already arrive ordered by finish time.
 *
 * Otherwise sorting is required.
 *
 * ------------------------------------------------
 *
 * When NOT To Use
 * ---------------
 * Weighted interval scheduling.
 *
 * Profit optimization.
 *
 * Multiple parallel resources.
 */


// =========================================================================
// 🎯 INTERVIEW RECALL SHEET
// =========================================================================

/*
 * Trigger
 * -------
 * Maximize compatible intervals.
 *
 * Pattern
 * -------
 * Greedy by Finish Time.
 *
 * Invariant
 * ---------
 * lastFinish belongs to the latest accepted interval.
 *
 * Search Target
 * -------------
 * Earliest finishing compatible interval.
 *
 * Discard Rule
 * ------------
 * Reject every overlapping interval.
 *
 * Common Trap
 * -----------
 * Sorting by start time.
 *
 * Edge Cases
 * ----------
 * Empty input.
 * Single interval.
 * Equal endpoints.
 *
 * One-liner
 * ---------
 * Earliest finish preserves maximum future opportunity.
 *
 * Re-derivation Cue
 * -----------------
 * Finish early to leave the timeline as free as possible.
 */


// =========================================================================
// 🔄 VARIATIONS & TWEAKS
// =========================================================================

/*
 * Variant
 * -------
 * Return selected transactions.
 *
 * Store accepted intervals while scanning.
 *
 * ------------------------------------------------
 *
 * Variant
 * -------
 * Weighted interval scheduling.
 *
 * Greedy fails.
 *
 * Dynamic Programming required.
 *
 * ------------------------------------------------
 *
 * Variant
 * -------
 * Multiple identical machines.
 *
 * Heap required.
 *
 * ------------------------------------------------
 *
 * Variant
 * -------
 * Online scheduling.
 *
 * Greedy proof no longer applies because future intervals are unknown.
 */


// =========================================================================
// 🧠 MASTERY CHECKLIST
// =========================================================================

/*
 * □ I know why earliest finish is optimal.
 *
 * □ I know why earliest start fails.
 *
 * □ I know the exchange argument.
 *
 * □ I know the invariant.
 *
 * □ I know the discard rule.
 *
 * □ I know weighted scheduling breaks greedy.
 *
 * □ I can derive the implementation.
 */


// =========================================================================
// ⚫ PATTERN MAPPING
// =========================================================================

/*
 * Problem                              Pattern
 * ---------------------------------------------------------------
 * Meeting Rooms I                      Sort + Adjacent Verification
 *
 * Meeting Rooms II                     Heap of Active Meetings
 *
 * Transaction Scheduling               Greedy by Earliest Finish
 *
 * Merge Intervals                      Sort + Merge
 *
 * Insert Interval                      Sort + Merge
 *
 * Employee Free Time                   Sweep Line / Heap
 *
 * Non-overlapping Intervals            Greedy by Finish
 *
 * Maximum Events                       Heap + Sweep
 *
 * Weighted Scheduling                  Dynamic Programming
 *
 * Skyline                              Sweep Line
 */


// =========================================================================
// ⚫ COMMON INTERVAL INVARIANTS
// =========================================================================

/*
 * Interval problems generally reduce to one of four invariants.
 *
 * ---------------------------------------------------------------
 *
 * 1.
 * Sorted neighbors reveal all conflicts.
 *
 * Used by
 * Meeting Rooms I.
 *
 * ---------------------------------------------------------------
 *
 * 2.
 * Active intervals determine resource usage.
 *
 * Used by
 * Meeting Rooms II.
 *
 * ---------------------------------------------------------------
 *
 * 3.
 * Earliest finish preserves future choices.
 *
 * Used by
 * Transaction Scheduling.
 *
 * ---------------------------------------------------------------
 *
 * 4.
 * Merge whenever overlap exists.
 *
 * Used by
 * Merge Intervals.
 */


// =========================================================================
// ⚫ DEBUGGING PLAYBOOK
// =========================================================================

/*
 * If Meeting Rooms I fails:
 *
 * Check sorting.
 *
 * Check '<' vs '<='.
 *
 * Check endpoint semantics.
 *
 * ---------------------------------------------------------------
 *
 * If Meeting Rooms II fails:
 *
 * Ensure while() not if().
 *
 * Ensure min heap.
 *
 * Ensure sorting by start.
 *
 * Track maximum heap size.
 *
 * ---------------------------------------------------------------
 *
 * If Transaction Scheduling fails:
 *
 * Ensure sorting by finish.
 *
 * Ensure compatibility uses
 *
 * current.start >= lastFinish.
 *
 * Never sort by duration.
 */


// =========================================================================
// ⚫ COMPLEXITY SUMMARY
// =========================================================================

/*
 * ---------------------------------------------------------------
 * Problem                  Time          Space
 * ---------------------------------------------------------------
 * Meeting Rooms I
 * Brute                    O(n²)         O(1)
 * Optimal                  O(n log n)    O(log n)
 *
 * ---------------------------------------------------------------
 *
 * Meeting Rooms II
 * Brute                    O(n²)         O(n)
 * Optimal                  O(n log n)    O(n)
 *
 * ---------------------------------------------------------------
 *
 * Transaction Scheduling
 * Brute                    O(2^n)        O(n)
 * Optimal                  O(n log n)    O(log n)
 */


// =========================================================================
// ⚫ IMPLEMENTATION RECONSTRUCTION
// =========================================================================

/*
 * Meeting Rooms I
 * ---------------
 *
 * Sort by start.
 *
 * Compare neighbors.
 *
 * First overlap -> false.
 *
 * End -> true.
 *
 * ---------------------------------------------------------------
 *
 * Meeting Rooms II
 * ----------------
 *
 * Sort by start.
 *
 * Heap of end times.
 *
 * Pop finished.
 *
 * Push current.
 *
 * Max heap size.
 *
 * ---------------------------------------------------------------
 *
 * Transaction Scheduling
 * ----------------------
 *
 * Sort by finish.
 *
 * lastFinish.
 *
 * Accept compatible.
 *
 * Update finish.
 *
 * Count answer.
 */

// =========================================================================
// ⚫ INTERVIEW DECISION TREE
// =========================================================================

/*
 * Q1.
 * Only need to know whether overlap exists?
 *
 * YES
 *  ->
 * Meeting Rooms I
 * Sort + Adjacent Verification
 *
 * ---------------------------------------------------------------
 *
 * Q2.
 * Need minimum simultaneous resources?
 *
 * YES
 *  ->
 * Meeting Rooms II
 * Heap
 *
 * ---------------------------------------------------------------
 *
 * Q3.
 * Need maximum compatible intervals?
 *
 * YES
 *  ->
 * Earliest Finish Greedy
 *
 * ---------------------------------------------------------------
 *
 * Q4.
 * Need maximum profit instead of maximum count?
 *
 * YES
 *  ->
 * Weighted Interval Scheduling
 * Dynamic Programming
 *
 * ---------------------------------------------------------------
 *
 * Q5.
 * Need merged intervals?
 *
 * YES
 *  ->
 * Merge Intervals
 */


// =========================================================================
// ⚫ DERIVATION UNDER PRESSURE
// =========================================================================

/*
 * Meeting Rooms I
 * ---------------
 *
 * Goal:
 * Detect overlap.
 *
 * Ask:
 * How can I make overlaps local?
 *
 * Answer:
 * Sort.
 *
 * Once sorted,
 * neighbors are sufficient.
 *
 * ---------------------------------------------------------------
 *
 * Meeting Rooms II
 * ----------------
 *
 * Goal:
 * Count simultaneous meetings.
 *
 * Ask:
 * Which room becomes free first?
 *
 * Answer:
 * Earliest ending meeting.
 *
 * Data Structure:
 * Min Heap.
 *
 * ---------------------------------------------------------------
 *
 * Transaction Scheduling
 * ----------------------
 *
 * Goal:
 * Preserve maximum future opportunity.
 *
 * Ask:
 * Which compatible transaction blocks the least future?
 *
 * Answer:
 * Earliest finishing transaction.
 */


// =========================================================================
// ⚫ EDGE CASE CATALOG
// =========================================================================

/*
 * Empty array
 *
 * []
 *
 * ---------------------------------------------------------------
 *
 * Single interval
 *
 * [[5,9]]
 *
 * ---------------------------------------------------------------
 *
 * Touching endpoints
 *
 * [1,5]
 * [5,9]
 *
 * Compatible.
 *
 * ---------------------------------------------------------------
 *
 * Complete overlap
 *
 * [1,10]
 * [2,3]
 *
 * ---------------------------------------------------------------
 *
 * Nested intervals
 *
 * [1,20]
 * [5,10]
 * [11,15]
 *
 * ---------------------------------------------------------------
 *
 * Identical intervals
 *
 * [3,6]
 * [3,6]
 *
 * ---------------------------------------------------------------
 *
 * Large coordinates
 *
 * [0,1_000_000_000]
 *
 * ---------------------------------------------------------------
 *
 * Already sorted.
 *
 * ---------------------------------------------------------------
 *
 * Reverse sorted.
 */


// =========================================================================
// ⚫ FORENSIC DEBUGGING GUIDE
// =========================================================================

/*
 * Symptom
 * -------
 * Meeting Rooms I returns false
 * for touching endpoints.
 *
 * Cause
 * -----
 * Used <= instead of <.
 *
 * ---------------------------------------------------------------
 *
 * Symptom
 * -------
 * Meeting Rooms II returns too many rooms.
 *
 * Cause
 * -----
 * Only one poll().
 *
 * Should remove every completed meeting.
 *
 * ---------------------------------------------------------------
 *
 * Symptom
 * -------
 * Meeting Rooms II returns too few rooms.
 *
 * Cause
 * -----
 * Using max heap.
 *
 * ---------------------------------------------------------------
 *
 * Symptom
 * -------
 * Transaction Scheduling selects too few jobs.
 *
 * Cause
 * -----
 * Sorted by start.
 *
 * ---------------------------------------------------------------
 *
 * Symptom
 * -------
 * Random failures.
 *
 * Cause
 * -----
 * Forgot sorting before greedy scan.
 */


// =========================================================================
// ⚫ FORMAL CORRECTNESS SKETCH
// =========================================================================

/*
 * Meeting Rooms I
 * ---------------
 *
 * Sorting establishes total order.
 *
 * Every overlap must appear between neighboring intervals.
 *
 * Therefore adjacent comparison is sufficient.
 *
 * ---------------------------------------------------------------
 *
 * Meeting Rooms II
 * ----------------
 *
 * Heap always contains active meetings.
 *
 * Heap size equals active room usage.
 *
 * Maximum active usage equals minimum required rooms.
 *
 * ---------------------------------------------------------------
 *
 * Transaction Scheduling
 * ----------------------
 *
 * Earliest finishing compatible interval is always exchangeable
 * with any later finishing compatible interval.
 *
 * Thus greedy remains optimal.
 */


// =========================================================================
// ⚫ TRANSFER LEARNING
// =========================================================================

/*
 * Similar Problems
 * ----------------
 *
 * Airline gate allocation
 *
 * CPU scheduling
 *
 * Hospital operating rooms
 *
 * Parking slot allocation
 *
 * Hotel reservation assignment
 *
 * Classroom scheduling
 *
 * Taxi dispatch windows
 *
 * Manufacturing machine allocation
 *
 * Bandwidth reservation
 *
 * Calendar booking
 *
 * The invariant remains identical.
 */


// =========================================================================
// ⚫ QUICK IMPLEMENTATION TEMPLATES
// =========================================================================

/*
 * Meeting Rooms I
 *
 * sort(start)
 *
 * for neighbors
 *     overlap?
 *
 * ---------------------------------------------------------------
 *
 * Meeting Rooms II
 *
 * sort(start)
 *
 * while finished
 *     poll
 *
 * push(end)
 *
 * answer=max(answer,size)
 *
 * ---------------------------------------------------------------
 *
 * Transaction Scheduling
 *
 * sort(end)
 *
 * if compatible
 *     accept
 *     updateFinish
 */

    // =========================================================================
    // ⚫ INVARIANT RE-DERIVATION EXERCISES
    // =========================================================================

    /*
     * Exercise 1
     * ----------
     * Forget the algorithm.
     *
     * Ask only:
     *
     * "What information must always remain true?"
     *
     * Meeting Rooms I
     * ----------------
     * Every processed neighboring pair is non-overlapping.
     *
     * ---------------------------------------------------------------
     *
     * Meeting Rooms II
     * ----------------
     * Heap contains every currently active meeting and nothing else.
     *
     * ---------------------------------------------------------------
     *
     * Transaction Scheduling
     * ----------------------
     * Accepted intervals are mutually compatible and finish as early as
     * possible.
     */


    // =========================================================================
    // ⚫ PATTERN BOUNDARIES
    // =========================================================================

    /*
     * Meeting Rooms I breaks when:
     * ----------------------------
     * • Meetings are inserted online.
     * • Order changes dynamically.
     * • Need room allocation.
     *
     * ---------------------------------------------------------------
     *
     * Meeting Rooms II breaks when:
     * -----------------------------
     * • Meetings have priorities.
     * • Rooms have capacities.
     * • Scheduling becomes weighted.
     *
     * ---------------------------------------------------------------
     *
     * Transaction Scheduling breaks when:
     * -----------------------------------
     * • Intervals have profits.
     * • Partial execution is allowed.
     * • Dependencies exist.
     */


    // =========================================================================
    // ⚫ INTERVIEW PITFALLS
    // =========================================================================

    /*
     * Pitfall
     * -------
     * Forgetting endpoint convention.
     *
     * Clarify:
     * Is [1,5] compatible with [5,8]?
     *
     * ---------------------------------------------------------------
     *
     * Pitfall
     * -------
     * Sorting by wrong key.
     *
     * Meeting Rooms I
     * -> start
     *
     * Meeting Rooms II
     * -> start
     *
     * Transaction Scheduling
     * -> end
     *
     * ---------------------------------------------------------------
     *
     * Pitfall
     * -------
     * Returning heap size at the end instead of maximum heap size.
     *
     * The last meeting is not necessarily the peak concurrency.
     *
     * ---------------------------------------------------------------
     *
     * Pitfall
     * -------
     * Assuming shortest interval is always optimal.
     *
     * Duration is irrelevant.
     *
     * Finish time is the invariant.
     */


    // =========================================================================
    // ⚫ CHEAT SHEET
    // =========================================================================

    /*
     * ------------------------------------------------------------
     * Problem
     *      Meeting Rooms I
     *
     * Goal
     *      Detect overlap
     *
     * Sort By
     *      Start
     *
     * Data Structure
     *      None
     *
     * Invariant
     *      Neighbors expose overlap
     *
     * ------------------------------------------------------------
     *
     * Problem
     *      Meeting Rooms II
     *
     * Goal
     *      Minimum rooms
     *
     * Sort By
     *      Start
     *
     * Data Structure
     *      Min Heap
     *
     * Invariant
     *      Heap = active meetings
     *
     * ------------------------------------------------------------
     *
     * Problem
     *      Transaction Scheduling
     *
     * Goal
     *      Maximum compatible intervals
     *
     * Sort By
     *      End
     *
     * Data Structure
     *      None
     *
     * Invariant
     *      Earliest finish preserves future
     */


    // =========================================================================
    // ⚫ MEMORY ANCHORS
    // =========================================================================

    /*
     * Detect?
     * -------
     * Sort.
     * Compare neighbors.
     *
     * ---------------------------------------------------------------
     *
     * Allocate?
     * ---------
     * Heap.
     * Active meetings.
     *
     * ---------------------------------------------------------------
     *
     * Maximize?
     * ---------
     * Finish earliest.
     */


    // =========================================================================
    // ⚫ IMPLEMENTATION MUSCLE MEMORY
    // =========================================================================

    /*
     * Meeting Rooms I
     *
     * Arrays.sort(start)
     *
     * prevEnd
     *
     * loop
     *
     * if(start < prevEnd)
     *     false
     *
     * prevEnd = end
     *
     * ---------------------------------------------------------------
     *
     * Meeting Rooms II
     *
     * Arrays.sort(start)
     *
     * PriorityQueue<Integer>
     *
     * while(peek <= start)
     *     poll
     *
     * offer(end)
     *
     * max=max(max,size)
     *
     * ---------------------------------------------------------------
     *
     * Transaction Scheduling
     *
     * Arrays.sort(end)
     *
     * lastFinish
     *
     * if(start>=lastFinish)
     *      accept
     */


    // =========================================================================
    // 🧪 MAIN + SELF-VERIFYING TESTS
    // =========================================================================

    public static void main(String[] args) {

        OptimalMeetingRooms meetingRooms =
                new OptimalMeetingRooms();

        OptimalMeetingRoomsII meetingRoomsII =
                new OptimalMeetingRoomsII();

        OptimalTransactionScheduling scheduler =
                new OptimalTransactionScheduling();

        // ------------------------------------------------------------
        // Meeting Rooms I
        // ------------------------------------------------------------

        Interval[] overlap = {
                new Interval(0, 30),
                new Interval(5, 10),
                new Interval(15, 20)
        };

        assert !meetingRooms.canAttendMeetings(overlap)
                : "Overlapping meetings cannot be attended.";

        Interval[] touching = {
                new Interval(1, 5),
                new Interval(5, 10),
                new Interval(10, 15)
        };

        assert meetingRooms.canAttendMeetings(touching)
                : "Touching endpoints are compatible.";

        Interval[] single = {
                new Interval(2, 3)
        };

        assert meetingRooms.canAttendMeetings(single)
                : "Single meeting is always feasible.";

        Interval[] empty = {};

        assert meetingRooms.canAttendMeetings(empty)
                : "Empty schedule is feasible.";

        // ------------------------------------------------------------
        // Meeting Rooms II
        // ------------------------------------------------------------

        Interval[] roomsExample = {
                new Interval(0, 30),
                new Interval(5, 10),
                new Interval(15, 20)
        };

        assert meetingRoomsII.minMeetingRooms(roomsExample) == 2
                : "Two rooms required.";

        Interval[] oneRoom = {
                new Interval(7, 10),
                new Interval(2, 4)
        };

        assert meetingRoomsII.minMeetingRooms(oneRoom) == 1
                : "Single room is sufficient.";

        Interval[] allOverlap = {
                new Interval(1, 10),
                new Interval(2, 11),
                new Interval(3, 12),
                new Interval(4, 13)
        };

        assert meetingRoomsII.minMeetingRooms(allOverlap) == 4
                : "All meetings overlap simultaneously.";

        Interval[] reuseRooms = {
                new Interval(1, 2),
                new Interval(2, 3),
                new Interval(3, 4),
                new Interval(4, 5)
        };

        assert meetingRoomsII.minMeetingRooms(reuseRooms) == 1
                : "Every room is immediately reusable.";

        // ------------------------------------------------------------
        // Transaction Scheduling
        // ------------------------------------------------------------

        Interval[] transactions = {
                new Interval(1, 4),
                new Interval(3, 5),
                new Interval(0, 6),
                new Interval(5, 7),
                new Interval(8, 9),
                new Interval(5, 9)
        };

        assert scheduler.maximumTransactions(transactions) == 3
                : "Classical interval scheduling example.";

        Interval[] chain = {
                new Interval(1, 2),
                new Interval(2, 3),
                new Interval(3, 4),
                new Interval(4, 5)
        };

        assert scheduler.maximumTransactions(chain) == 4
                : "Every transaction is compatible.";

        Interval[] nested = {
                new Interval(1, 10),
                new Interval(2, 3),
                new Interval(3, 4),
                new Interval(4, 5),
                new Interval(5, 6)
        };

        assert scheduler.maximumTransactions(nested) == 4
                : "Earliest finishing intervals maximize throughput.";

        Interval[] identical = {
                new Interval(1, 5),
                new Interval(1, 5),
                new Interval(1, 5)
        };

        assert scheduler.maximumTransactions(identical) == 1
                : "Only one identical overlapping interval can be scheduled.";

        Interval[] endpointCompatibility = {
                new Interval(1, 5),
                new Interval(5, 6),
                new Interval(6, 7),
                new Interval(7, 8)
        };

        assert scheduler.maximumTransactions(endpointCompatibility) == 4
                : "Endpoint-touching intervals remain compatible.";

        Interval[] emptyTransactions = {};

        assert scheduler.maximumTransactions(emptyTransactions) == 0
                : "Empty input schedules zero transactions.";

        // ------------------------------------------------------------
        // Regression Tests
        // ------------------------------------------------------------

        Interval[] reverseOrder = {
                new Interval(9, 10),
                new Interval(7, 8),
                new Interval(5, 6),
                new Interval(3, 4),
                new Interval(1, 2)
        };

        assert meetingRooms.canAttendMeetings(reverseOrder)
                : "Sorting should handle reverse-ordered input.";

        Interval[] mixed = {
                new Interval(10, 15),
                new Interval(0, 5),
                new Interval(5, 10),
                new Interval(15, 20),
                new Interval(2, 4)
        };

        assert !meetingRooms.canAttendMeetings(mixed)
                : "Nested overlap must be detected.";

        Interval[] peakConcurrency = {
                new Interval(1, 8),
                new Interval(2, 7),
                new Interval(3, 6),
                new Interval(8, 9),
                new Interval(9, 10)
        };

        assert meetingRoomsII.minMeetingRooms(peakConcurrency) == 3
                : "Peak concurrency determines required rooms.";

        Interval[] greedyCounterExample = {
                new Interval(1, 10),
                new Interval(2, 3),
                new Interval(3, 4),
                new Interval(4, 5),
                new Interval(5, 6),
                new Interval(6, 7)
        };

        assert scheduler.maximumTransactions(greedyCounterExample) == 5
                : "Earliest-finish greedy outperforms earliest-start.";

        System.out.println("All assertions passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/