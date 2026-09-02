package org.chijai.day1.Arrays.session4.Intervals;

import java.util.Arrays;
import java.util.TreeMap;

/**
 * PATTERN — BOUNDARY DELTA
 *
 * Difference Array and Sweep Line are two representations of this SAME engine.
 *
 * MASTER ENGINE
 * -------------
 * Many ranges add or remove some quantity.
 *
 * Do NOT update every point inside every range.
 *
 * FIRST THINK IN "CARRYING" LANGUAGE:
 *
 *      at the left boundary:
 *          START carrying the effect.
 *
 *      while inside the range:
 *          KEEP carrying it.
 *
 *      when the range ends:
 *          STOP carrying it.
 *
 * Then compress that story by recording only where the carried effect CHANGES:
 *
 *      effect starts -> +delta
 *      effect stops  -> -delta
 *
 * Then walk boundaries left -> right:
 *
 *      running += delta
 *
 * `running` means:
 *      "What total effect am I carrying RIGHT NOW?"
 *
 * The formal name for this running accumulation is a PREFIX SUM.
 *
 * HIGH-ROI PROGRESSION
 * --------------------
 * Every problem below follows:
 *
 *      problem statement
 *      -> varied examples
 *      -> visual/common-sense dry run
 *      -> solution
 *      -> WHY / correctness / complexity
 *
 * Understand the concrete story BEFORE reading the code.
 *
 *      Range Addition
 *          -> pure inclusive range-update template
 *
 *      Corporate Flight Bookings
 *          -> same inclusive range-update template with story nouns
 *
 *      Car Pooling
 *          -> half-open weighted capacity over bounded positions
 *
 *      Maximum Population Year
 *          -> half-open unit-weight events
 *
 *      Sparse Weighted Sweep
 *          -> same boundary-delta engine when coordinates are too sparse for an array
 *
 * REPRESENTATION CHOICE
 * ---------------------
 *      BOUNDARY DELTA
 *          |
 *          +-- small / bounded integer coordinates
 *          |       -> Difference Array + Prefix Sum
 *          |
 *          +-- huge / sparse / arbitrary coordinates
 *                  -> ordered events / TreeMap Sweep Line
 *
 * ONE-LINE RECALL
 * ---------------
 *      DON'T UPDATE THE RANGE.
 *      UPDATE WHERE THE RANGE CHANGES.
 */
public class BoundaryDelta {

    // =========================================================================
    // PATTERN POSITION / RECOGNITION
    // =========================================================================

    /*
     * TRIGGERS
     * --------
     *      "Add x to every index from L to R."
     *      "Many range updates, then return final values."
     *      "Capacity changes between positions."
     *      "How much weighted state is active at each point?"
     *      "Birth/death, pickup/dropoff, booking start/end."
     *
     * MASTER QUESTION
     * ---------------
     *      Do I need individual interval identity,
     *      or only the aggregate quantity active at each coordinate?
     *
     * If only aggregate state matters:
     *
     *      boundary -> delta
     *      ordered prefix -> state
     *
     * This is NOT an Active Min Heap problem.
     *
     * Active Min Heap asks:
     *      "Which active object ends first?"
     *
     * Boundary Delta asks:
     *      "How much does aggregate state change here?"
     */

    // =========================================================================
    // 1) RANGE ADDITION — GENERIC MASTER PROBLEM
    // =========================================================================

    /*
     * PROBLEM STATEMENT — RANGE ADDITION
     * ----------------------------------
     * You are given an integer array of length `length`.
     *
     * Initially every value is 0.
     *
     * Each update has the form:
     *
     *      [startIndex, endIndex, increment]
     *
     * For that update, add `increment` to EVERY array element whose index lies
     * in the inclusive range:
     *
     *      startIndex <= i <= endIndex
     *
     * Apply all updates and return the final array.
     *
     * EXAMPLES
     * --------
     * Example 1 — overlapping positive and negative updates:
     *
     *      length = 5
     *
     *      updates:
     *          [1,3,+2]
     *          [2,4,+3]
     *          [0,2,-2]
     *
     *      result:
     *          [-2, 0, 3, 5, 3]
     *
     *
     * Example 2 — one full-array update:
     *
     *      length = 4
     *      updates = [[0,3,+5]]
     *
     *      result = [5,5,5,5]
     *
     *
     * Example 3 — single-point update:
     *
     *      length = 5
     *      updates = [[2,2,+7]]
     *
     *      result = [0,0,7,0,0]
     *
     * Observe:
     *      even a single index is still an inclusive range [2,2].
     *
     * NAIVE IDEA
     * ----------
     * For every update, loop through every index in [startIndex, endIndex].
     *
     * Worst case:
     *      O(numberOfUpdates * length)
     *
     * TARGET IDEA
     * -----------
     * Each range update should modify only TWO boundaries.
     */

    /*
     * VISUAL DRY RUN — RANGE ADDITION
     * --------------------------------
     *
     * FIRST SEE THE IDEA WITHOUT `diff[]`
     * -----------------------------------
     *
     * Suppose there is only ONE update:
     *
     *      [1,3] += 2
     *
     * Common-sense meaning:
     *
     *      index 0:
     *          carry 0
     *
     *      index 1:
     *          START carrying +2
     *          current carried effect = +2
     *
     *      index 2:
     *          KEEP carrying +2
     *          current carried effect = +2
     *
     *      index 3:
     *          KEEP carrying +2
     *          current carried effect = +2
     *
     *      index 4:
     *          STOP carrying +2
     *          current carried effect = 0
     *
     * Actual result of this one update:
     *
     *      index:   0   1   2   3   4
     *      value:  [0, +2, +2, +2, 0]
     *
     *
     * NOW COMPRESS THAT STORY
     * -----------------------
     *
     * We do NOT need to write +2 separately at indexes 1, 2 and 3.
     *
     * Only TWO moments matter:
     *
     *      At 1:
     *          START carrying +2
     *
     *      At 4:
     *          STOP carrying +2
     *
     * Encode only those changes:
     *
     *      diff[1] += 2
     *      diff[4] -= 2
     *
     *      index:  0   1   2   3   4
     *      diff : [0, +2,  0,  0, -2]
     *
     *
     * WHAT DOES diff[i] MEAN?
     * -----------------------
     *
     * It does NOT mean:
     *      "the final value at index i."
     *
     * It means:
     *      "when I reach index i,
     *       CHANGE what I am currently carrying by this amount."
     *
     *
     * WHY A RUNNING / PREFIX SUM?
     * ---------------------------
     *
     * Walk left -> right and keep asking:
     *
     *      "What am I carrying right now?"
     *
     *      running += diff[i]
     *
     * For this one update:
     *
     *      i=0: running = 0
     *
     *      i=1: running = 0 + 2 = 2
     *           START carrying +2
     *
     *      i=2: running = 2 + 0 = 2
     *           KEEP carrying +2
     *
     *      i=3: running = 2 + 0 = 2
     *           KEEP carrying +2
     *
     *      i=4: running = 2 - 2 = 0
     *           STOP carrying +2
     *
     * So:
     *
     *      DELTA   = what changes HERE.
     *      RUNNING = what is active / carried HERE.
     *
     * "Prefix sum" is only the formal name for this running carry.
     *
     *
     * NOW USE THE FULL EXAMPLE
     * ------------------------
     *
     *      length = 5
     *
     *      updates:
     *          [1,3,+2]
     *          [2,4,+3]
     *          [0,2,-2]
     *
     *
     * UPDATE 1: [1,3] += 2
     *
     *      At 1: START carrying +2
     *      At 4: STOP  carrying +2
     *
     *      diff[1] += 2
     *      diff[4] -= 2
     *
     *      index:  0   1   2   3   4   5
     *      diff : [0, +2,  0,  0, -2,  0]
     *
     *
     * UPDATE 2: [2,4] += 3
     *
     *      At 2: START carrying +3
     *      At 5: STOP  carrying +3
     *
     *      index:  0   1   2   3   4   5
     *      diff : [0, +2, +3,  0, -2, -3]
     *
     *
     * UPDATE 3: [0,2] += -2
     *
     *      At 0: START carrying -2
     *      At 3: STOP  carrying -2
     *
     * Stopping -2 means adding +2:
     *
     *      diff[0] += -2
     *      diff[3] -= -2
     *
     *      FINAL DIFF
     *
     *      index:  0   1   2   3   4   5
     *      diff : [-2, +2, +3, +2, -2, -3]
     *
     *
     * NOW WALK LEFT -> RIGHT
     * ----------------------
     *
     * `running` = total effect currently being carried.
     *
     *      i=0:
     *          change  = -2
     *          running = -2
     *          answer[0] = -2
     *
     *      i=1:
     *          change  = +2
     *          running = -2 + 2 = 0
     *          answer[1] = 0
     *
     *      i=2:
     *          change  = +3
     *          running = 0 + 3 = 3
     *          answer[2] = 3
     *
     *      i=3:
     *          change  = +2
     *          running = 3 + 2 = 5
     *          answer[3] = 5
     *
     *      i=4:
     *          change  = -2
     *          running = 5 - 2 = 3
     *          answer[4] = 3
     *
     *      answer = [-2, 0, 3, 5, 3]
     *
     *
     * COMMON-SENSE PICTURE
     * --------------------
     *
     *      diff[]:
     *          START / STOP / CHANGE instructions.
     *
     *      running:
     *          what I am carrying RIGHT NOW.
     *
     *      answer[]:
     *          the actual state at each index.
     *
     * MEMORY LINE:
     *
     *      START carrying at L.
     *      KEEP carrying through the range.
     *      STOP carrying after R.
     */

    static final class RangeAddition {

        int[] getModifiedArray(int length, int[][] updates) {
            if (length <= 0) {
                return new int[0];
            }

            int[] difference = new int[length + 1];

            if (updates != null) {
                for (int[] update : updates) {
                    int start = update[0];
                    int end = update[1];
                    int increment = update[2];

                    difference[start] += increment;
                    difference[end + 1] -= increment;
                }
            }

            int[] answer = new int[length];
            int running = 0;

            for (int i = 0; i < length; i++) {
                running += difference[i];
                answer[i] = running;
            }

            return answer;
        }
    }

    /*
     * WHY? — RANGE ADDITION
     * ---------------------
     * Inclusive update:
     *
     *      [L, R] += value
     *
     * means:
     *
     *      effect STARTS at L
     *      effect STOPS after R
     *
     * therefore:
     *
     *      diff[L]     += value
     *      diff[R + 1] -= value
     *
     * Prefix sum carries the contribution through every index L..R.
     *
     * Complexity:
     *      u = number of updates
     *      n = array length
     *
     *      Time  = O(u + n)
     *      Space = O(n)
     */


    // =========================================================================
    // 2) CORPORATE FLIGHT BOOKINGS — SAME INCLUSIVE RANGE UPDATE
    // =========================================================================

    /*
     * PROBLEM STATEMENT — CORPORATE FLIGHT BOOKINGS
     * ---------------------------------------------
     * There are `n` flights numbered:
     *
     *      1, 2, 3, ... n
     *
     * Each booking is:
     *
     *      [firstFlight, lastFlight, seats]
     *
     * It means `seats` seats are booked on EVERY flight from `firstFlight`
     * through `lastFlight`, both endpoints INCLUDED.
     *
     * Return an array `answer` of length n where:
     *
     *      answer[i]
     *
     * is the total number of seats booked on flight i + 1 after processing
     * all bookings.
     *
     * EXAMPLES
     * --------
     * Example 1 — overlapping bookings:
     *
     *      bookings =
     *          [1,2,10]
     *          [2,3,20]
     *          [2,5,25]
     *
     *      n = 5
     *
     *      answer =
     *          [10,55,45,25,25]
     *
     *
     * Example 2 — booking covers every flight:
     *
     *      bookings = [[1,4,12]]
     *      n = 4
     *
     *      answer = [12,12,12,12]
     *
     *
     * Example 3 — bookings touch different single flights:
     *
     *      bookings =
     *          [1,1,5]
     *          [3,3,7]
     *
     *      n = 4
     *
     *      answer = [5,0,7,0]
     *
     * KEY RECOGNITION
     * ---------------
     * Ignore the airplane story.
     *
     * This is exactly:
     *
     *      add seats to inclusive index range [L, R].
     */

    /*
     * VISUAL DRY RUN — CORPORATE FLIGHT BOOKINGS
     * ------------------------------------------
     * Input:
     *
     *      n = 5
     *
     *      bookings:
     *          [1,2,10]
     *          [2,3,20]
     *          [2,5,25]
     *
     * Meaning:
     *
     *      booking [1,2,10]
     *          flight 1 gets +10
     *          flight 2 gets +10
     *
     * Instead of touching both flights,
     * mark where +10 STARTS and where it STOPS.
     *
     *
     * Use 0-based Java positions:
     *
     *      flights:       1    2    3    4    5
     *      array index:   0    1    2    3    4
     *
     *
     * BOOKING 1: [1,2,10]
     *
     *      start index = 0
     *      after last  = 2
     *
     *      diff[0] += 10
     *      diff[2] -= 10
     *
     *      diff = [10, 0, -10, 0, 0, 0]
     *
     *
     * BOOKING 2: [2,3,20]
     *
     *      diff[1] += 20
     *      diff[3] -= 20
     *
     *      diff = [10, 20, -10, -20, 0, 0]
     *
     *
     * BOOKING 3: [2,5,25]
     *
     *      diff[1] += 25
     *      diff[5] -= 25
     *
     *      FINAL DIFF:
     *
     *      index:  0    1    2     3    4     5
     *      diff : [10, 45, -10, -20,  0,  -25]
     *
     *
     * PREFIX = ACTUAL SEATS ON EACH FLIGHT
     *
     *      flight 1: 10
     *      flight 2: 10 + 45 = 55
     *      flight 3: 55 - 10 = 45
     *      flight 4: 45 - 20 = 25
     *      flight 5: 25 + 0  = 25
     *
     *      answer = [10,55,45,25,25]
     *
     *
     * COMMON-SENSE TRANSLATION
     * ------------------------
     * "Book 25 seats from flight 2 through flight 5"
     *
     * becomes:
     *
     *      "Starting at flight 2, carry +25 forward."
     *      "Immediately after flight 5, cancel that +25."
     *
     * Same Range Addition engine. Airplanes are only nouns.
     */

    static final class CorporateFlightBookings {

        int[] corpFlightBookings(int[][] bookings, int n) {
            if (n <= 0) {
                return new int[0];
            }

            // One extra cancellation slot for "after the last included flight".
            int[] difference = new int[n + 1];

            if (bookings != null) {
                for (int[] booking : bookings) {
                    int first = booking[0] - 1;
                    int afterLast = booking[1];
                    int seats = booking[2];

                    difference[first] += seats;
                    difference[afterLast] -= seats;
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
     * WHY? — CORPORATE FLIGHT BOOKINGS
     * ---------------------------------
     * Flights are 1-indexed in the statement.
     * Arrays are 0-indexed in Java.
     *
     * Booking:
     *
     *      [firstFlight, lastFlight, seats]
     *
     * converts to:
     *
     *      start index = firstFlight - 1
     *      stop index  = lastFlight
     *
     * because `lastFlight` is exactly the 0-based position AFTER the final
     * included flight.
     *
     * Same engine as Range Addition.
     *
     * Complexity:
     *      m = bookings
     *
     *      Time  = O(m + n)
     *      Space = O(n)
     */


    // =========================================================================
    // 3) CAR POOLING — HALF-OPEN WEIGHTED CAPACITY
    // =========================================================================

    /*
     * PROBLEM STATEMENT — CAR POOLING
     * -------------------------------
     * A car has a fixed passenger `capacity`.
     *
     * You are given trips where:
     *
     *      trip = [numPassengers, from, to]
     *
     * At location `from`:
     *      `numPassengers` enter the car.
     *
     * At location `to`:
     *      those passengers leave the car.
     *
     * The car only moves forward along the route.
     *
     * Return:
     *
     *      true  -> every trip can be completed without ever exceeding capacity
     *      false -> passenger load exceeds capacity at some location
     *
     * IMPORTANT ENDPOINT SEMANTICS
     * ----------------------------
     * A trip occupies:
     *
     *      [from, to)
     *
     * Passengers are present starting at `from`,
     * but they are already OUT of the car at `to`.
     *
     * EXAMPLES
     * --------
     * Example 1 — capacity exceeded:
     *
     *      trips =
     *          [2,1,5]
     *          [3,3,7]
     *
     *      capacity = 4
     *
     * Between locations 3 and 5:
     *      load = 2 + 3 = 5
     *
     * answer:
     *      false
     *
     *
     * Example 2 — same trips, enough capacity:
     *
     *      capacity = 5
     *
     * answer:
     *      true
     *
     *
     * Example 3 — dropoff and pickup at same location:
     *
     *      trips =
     *          [3,5,7]
     *          [3,7,9]
     *
     *      capacity = 3
     *
     * At location 7:
     *      first 3 passengers leave,
     *      next 3 passengers enter.
     *
     * answer:
     *      true
     *
     * This example exposes why the trip is [from,to), not [from,to].
     *
     * KEY RECOGNITION
     * ---------------
     * We do not need trip identity.
     * We only need current passenger LOAD.
     */

    /*
     * VISUAL DRY RUN — CAR POOLING
     * ----------------------------
     * Input:
     *
     *      trips:
     *          [2,1,5]
     *          [3,3,7]
     *
     *      capacity = 4
     *
     *
     * TRIP 1: 2 passengers from 1 to 5
     *
     *      location 1 -> +2 get in
     *      location 5 -> -2 get out
     *
     *
     * TRIP 2: 3 passengers from 3 to 7
     *
     *      location 3 -> +3 get in
     *      location 7 -> -3 get out
     *
     *
     * BOUNDARY DELTAS:
     *
     *      location:  0   1   2   3   4   5   6   7
     *      delta:     0  +2   0  +3   0  -2   0  -3
     *
     *
     * DRIVE FORWARD / PREFIX THE DELTAS:
     *
     *      location 0: load = 0
     *      location 1: load = 2
     *      location 2: load = 2
     *      location 3: load = 5   <- capacity 4 exceeded
     *
     *      return false
     *
     *
     * WHY SUBTRACT AT `to`, NOT `to + 1`?
     * ------------------------------------
     * Trip is:
     *
     *      [from, to)
     *
     * At location 5 the first two passengers GET OUT.
     * They should not occupy capacity from 5 onward.
     *
     *
     * SAME-LOCATION HANDOFF
     * ---------------------
     *
     *      [3,5,7]
     *      [3,7,9]
     *      capacity = 3
     *
     * At location 7:
     *
     *      first trip  -> -3
     *      second trip -> +3
     *
     *      net delta = 0
     *
     * Load stays 3.
     *
     * COMMON-SENSE PICTURE:
     *      The difference array is a passenger ENTER/EXIT board.
     *      The prefix sum is how many people are actually sitting in the car.
     */

    static final class CarPooling {

        boolean carPooling(int[][] trips, int capacity) {
            if (trips == null || trips.length == 0) {
                return true;
            }

            // LeetCode 1094 bounds locations to 0..1000.
            int[] difference = new int[1001];

            for (int[] trip : trips) {
                int passengers = trip[0];
                int from = trip[1];
                int to = trip[2];

                difference[from] += passengers;
                difference[to] -= passengers;
            }

            int currentLoad = 0;

            // at diff 0 also could fail
            //
            for (int delta : difference) {
                currentLoad += delta;

                if (currentLoad > capacity) {
                    return false;
                }
            }

            return true;
        }
    }

    /*
     * WHY? — CAR POOLING
     * ------------------
     * Half-open contribution:
     *
     *      [from, to)
     *
     * therefore:
     *
     *      diff[from] += passengers
     *      diff[to]   -= passengers
     *
     * Notice:
     *
     *      NOT diff[to + 1]
     *
     * because `to` itself is already outside the occupied range.
     *
     * Same-location handoff works automatically:
     *
     *      one trip drops at x  -> negative delta
     *      another picks at x   -> positive delta
     *
     * Both changes combine at the same boundary.
     *
     * Complexity:
     *      n = trips
     *      U = bounded coordinate universe
     *
     *      Time  = O(n + U)
     *      Space = O(U)
     */


    // =========================================================================
    // 4) MAXIMUM POPULATION YEAR — HALF-OPEN UNIT-WEIGHT EVENTS
    // =========================================================================

    /*
     * PROBLEM STATEMENT — MAXIMUM POPULATION YEAR
     * -------------------------------------------
     * Each person is represented by:
     *
     *      [birthYear, deathYear]
     *
     * A person is considered alive during:
     *
     *      birthYear <= year < deathYear
     *
     * So:
     *
     *      birth year IS included
     *      death year is NOT included
     *
     * Return the EARLIEST year having the maximum population.
     *
     * EXAMPLES
     * --------
     * Example 1 — separated lifetimes:
     *
     *      logs =
     *          [1993,1999]
     *          [2000,2010]
     *
     * Maximum population is 1 in many years.
     * Earliest such year:
     *
     *      1993
     *
     *
     * Example 2 — overlapping lifetimes:
     *
     *      logs =
     *          [1950,1961]
     *          [1960,1971]
     *          [1970,1981]
     *
     * Population reaches 2 in 1960 and again in 1970.
     *
     * Earliest maximum year:
     *
     *      1960
     *
     *
     * Example 3 — death year does NOT count:
     *
     *      logs =
     *          [1950,1960]
     *          [1960,1970]
     *
     * At 1960:
     *      first person is no longer alive,
     *      second person becomes alive.
     *
     * Population is 1, not 2.
     *
     * KEY RECOGNITION
     * ---------------
     * Each person contributes weight 1 over a half-open interval:
     *
     *      [birth, death)
     */

    /*
     * VISUAL DRY RUN — MAXIMUM POPULATION YEAR
     * ----------------------------------------
     *
     * FIRST UNDERSTAND ONE PERSON
     * ---------------------------
     *
     * Person:
     *
     *      [1950,1961]
     *
     * The problem says a person is alive on:
     *
     *      [birth, death)
     *
     * So this person counts in:
     *
     *      1950, 1951, ..., 1960
     *
     * but NOT in:
     *
     *      1961
     *
     * Same carrying language:
     *
     *      At 1950:
     *          START carrying +1 alive person
     *
     *      1951 ... 1960:
     *          KEEP carrying that +1
     *
     *      At 1961:
     *          STOP carrying that +1
     *
     * Therefore:
     *
     *      difference[1950] += 1
     *      difference[1961] -= 1
     *
     * IMPORTANT:
     *
     *      We subtract at death itself,
     *      NOT death + 1,
     *
     * because death is already EXCLUDED.
     *
     *      [birth, death)
     *
     *
     * NOW USE MULTIPLE PEOPLE
     * -----------------------
     *
     *      logs:
     *          [1950,1961]
     *          [1960,1971]
     *          [1970,1981]
     *
     * Turn each person into START / STOP instructions:
     *
     *      person 1:
     *          1950 -> START +1
     *          1961 -> STOP  +1  => -1
     *
     *      person 2:
     *          1960 -> START +1
     *          1971 -> STOP  +1  => -1
     *
     *      person 3:
     *          1970 -> START +1
     *          1981 -> STOP  +1  => -1
     *
     *
     * WALK THROUGH IMPORTANT YEARS:
     *
     *      1950:
     *          change = +1
     *          carrying / population = 1
     *
     *          maximum = 1
     *          answer = 1950
     *
     *      1960:
     *          another person STARTS
     *          population = 2
     *
     *          maximum = 2
     *          answer = 1960
     *
     *      1961:
     *          first person STOPS
     *          population = 1
     *
     *      1970:
     *          another person STARTS
     *          population = 2
     *
     *          This only TIES the maximum.
     *          Keep answer = 1960.
     *
     *      1971:
     *          second person STOPS
     *          population = 1
     *
     *      1981:
     *          third person STOPS
     *          population = 0
     *
     *
     * FINAL:
     *
     *      maximum population = 2
     *      earliest year      = 1960
     *
     *
     * WHY `>` AND NOT `>=`?
     * ---------------------
     *
     * We scan years from small -> large.
     *
     * Update answer only when we find a STRICTLY larger population.
     * On a tie, leave the earlier answer untouched.
     */

    /*
     * CODE READING MAP — BEFORE THE SOLUTION
     * --------------------------------------
     *
     *      difference[birthYear]++
     *          = START carrying this person.
     *
     *      difference[deathYear]--
     *          = STOP carrying this person.
     *
     *      currentPopulation += difference[year]
     *          = after applying this year's START / STOP instructions,
     *            how many alive people am I carrying RIGHT NOW?
     *
     *      if (currentPopulation > maximumPopulation)
     *          = did this year create a NEW highest population?
     */

    static final class MaximumPopulationYear {

        int maximumPopulation(int[][] logs) {
            if (logs == null || logs.length == 0) {
                return 0;
            }

            final int firstYear = 1950;
            final int lastYear = 2050;

            /*
             * Intentionally use the ACTUAL YEAR as the array index.
             *
             *      difference[1960]
             *
             * literally means:
             *
             *      "population changes at year 1960."
             *
             * This wastes a tiny unused prefix 0..1949,
             * but removes offset arithmetic from the mental model.
             */
            int[] difference = new int[lastYear + 1];

            for (int[] log : logs) {
                int birthYear = log[0];
                int deathYear = log[1];

                // START carrying +1 alive person.
                difference[birthYear]++;

                // STOP carrying that person at death.
                // Alive interval is [birthYear, deathYear).
                difference[deathYear]--;
            }

            int currentPopulation = 0;
            int maximumPopulation = 0;
            int answerYear = firstYear;

            for (int year = firstYear; year <= lastYear; year++) {

                // Apply all START / STOP changes happening this year.
                currentPopulation += difference[year];

                // Strictly greater preserves the earliest year on ties.
                if (currentPopulation > maximumPopulation) {
                    maximumPopulation = currentPopulation;
                    answerYear = year;
                }
            }

            return answerYear;
        }
    }

    /*
     * WHY? — MAXIMUM POPULATION YEAR
     * ------------------------------
     *
     * Person:
     *
     *      [birth, death)
     *
     * Common-sense carrying:
     *
     *      birth -> START carrying +1
     *      death -> STOP  carrying +1
     *
     * therefore:
     *
     *      difference[birthYear]++
     *      difference[deathYear]--
     *
     *
     * WHY CAN `currentPopulation += difference[year]` WORK?
     * -----------------------------------------------------
     *
     * difference[year] contains only:
     *
     *      how much the population CHANGES this year.
     *
     * `currentPopulation` carries forward everyone who started earlier
     * and has not stopped yet.
     *
     * So:
     *
     *      currentPopulation
     *
     * literally means:
     *
     *      "How many +1 life effects am I still carrying?"
     *
     *
     * WHY USE THE YEAR DIRECTLY AS THE INDEX?
     * ---------------------------------------
     *
     * We could compress:
     *
     *      1950 -> index 0
     *      1951 -> index 1
     *
     * using `year - 1950`.
     *
     * But the year range is tiny.
     *
     * Using:
     *
     *      difference[1960]
     *
     * directly is easier to read and remember:
     *
     *      "something changes in 1960."
     *
     *
     * WHY STRICT `>`?
     * ---------------
     *
     * The question wants the EARLIEST year with maximum population.
     *
     * We scan:
     *
     *      1950 -> 1951 -> 1952 -> ...
     *
     * Once maximum population is reached,
     * a later year with the SAME population must NOT replace it.
     *
     * Therefore:
     *
     *      if (currentPopulation > maximumPopulation)
     *
     * not:
     *
     *      >=
     *
     *
     * Complexity:
     *      L = number of logs
     *      U = fixed year universe
     *
     *      Time  = O(L + U)
     *      Space = O(U)
     */

    // =========================================================================
    // 5) SPARSE WEIGHTED SWEEP — SAME ENGINE, SPARSE REPRESENTATION
    // =========================================================================

    /*
     * GENERIC PROBLEM STATEMENT — SPARSE WEIGHTED OVERLAP
     * ---------------------------------------------------
     * You are given weighted half-open ranges:
     *
     *      [weight, start, end)
     *
     * Each range contributes `weight` to every coordinate x satisfying:
     *
     *      start <= x < end
     *
     * Return the maximum total active weight at any coordinate.
     *
     * EXAMPLES
     * --------
     * Example 1 — overlapping weighted ranges:
     *
     *      [2,1,5)
     *      [3,3,7)
     *
     * Between 3 and 5:
     *
     *      active weight = 2 + 3 = 5
     *
     * answer:
     *      5
     *
     *
     * Example 2 — ranges touch but do not overlap:
     *
     *      [4,10,20)
     *      [6,20,30)
     *
     * At 20 the first weight stops exactly when the second begins.
     *
     * Peak:
     *      max(4,6) = 6
     *
     * not:
     *      10
     *
     *
     * Example 3 — huge sparse coordinates:
     *
     *      [2,10,1_000_000_000)
     *      [5,500_000_000,700_000_000)
     *
     * An int[] covering every coordinate would be wasteful.
     * Store only the actual change points in a TreeMap.
     *
     * WHY THIS VERSION EXISTS
     * -----------------------
     * Difference Array works beautifully when coordinates are small and dense.
     *
     * But suppose coordinates are:
     *
     *      10
     *      1_000_000_000
     *      -500_000_000
     *
     * Allocating an array for every possible coordinate is wasteful or impossible.
     *
     * So store ONLY boundaries that actually occur.
     */

    /*
     * VISUAL DRY RUN — SPARSE WEIGHTED SWEEP
     * --------------------------------------
     * Input:
     *
     *      ranges:
     *          [2,1,5)
     *          [3,3,7)
     *
     * Meaning:
     *
     *      weight 2 is active from 1 until before 5
     *      weight 3 is active from 3 until before 7
     *
     *
     * BOUNDARY EVENTS:
     *
     *      1 -> +2
     *      5 -> -2
     *
     *      3 -> +3
     *      7 -> -3
     *
     *
     * TreeMap combines and sorts them:
     *
     *      {
     *          1 : +2,
     *          3 : +3,
     *          5 : -2,
     *          7 : -3
     *      }
     *
     *
     * SWEEP LEFT -> RIGHT:
     *
     *      at 1:
     *          running = 0 + 2 = 2
     *          maximum = 2
     *
     *      at 3:
     *          running = 2 + 3 = 5
     *          maximum = 5
     *
     *      at 5:
     *          running = 5 - 2 = 3
     *
     *      at 7:
     *          running = 3 - 3 = 0
     *
     *      answer = 5
     *
     *
     * WHY NOT int[]?
     * --------------
     * Imagine boundaries:
     *
     *      10
     *      500_000_000
     *      1_000_000_000
     *
     * We do not want hundreds of millions of empty array cells.
     *
     * TreeMap stores ONLY:
     *
     *      places where something changes.
     *
     *
     * COMMON-SENSE CONNECTION
     * -----------------------
     *
     * Dense Difference Array:
     *
     *      every possible coordinate has an array slot.
     *
     * Sparse Sweep:
     *
     *      store only non-empty change points.
     *
     * SAME MATH.
     * DIFFERENT STORAGE.
     */

    static final class SparseWeightedSweep {

        int maximumActiveWeight(int[][] ranges) {
            if (ranges == null || ranges.length == 0) {
                return 0;
            }

            TreeMap<Integer, Integer> events = new TreeMap<>();

            for (int[] range : ranges) {
                int weight = range[0];
                int start = range[1];
                int end = range[2];

                events.merge(start, weight, Integer::sum);
                events.merge(end, -weight, Integer::sum);
            }

            int running = 0;
            int maximum = 0;

            for (int delta : events.values()) {
                running += delta;
                maximum = Math.max(maximum, running);
            }

            return maximum;
        }
    }

    /*
     * WHY? — SPARSE SWEEP
     * -------------------
     * SAME mathematical representation:
     *
     *      start -> +weight
     *      end   -> -weight
     *
     * DIFFERENT storage:
     *
     *      dense bounded domain -> int[] difference
     *      sparse huge domain   -> TreeMap boundary events
     *
     * TreeMap gives sorted coordinates automatically.
     *
     * Why `events.values()` is enough here:
     *
     *      We only need each delta.
     *      We do NOT use the coordinate itself in this solution.
     *
     * TreeMap's collection views follow the map's sorted key order,
     * so iterating:
     *
     *      for (int delta : events.values())
     *
     * still processes deltas from the smallest coordinate to the largest.
     *
     * JAVA MAP RECALL:
     *
     *      keySet()   -> keys only
     *      values()   -> values only
     *      entrySet() -> key + value pair
     *
     * Use entrySet() only when the coordinate itself is also required,
     * for example when returning the position where a maximum occurs.
     *
     * Complexity:
     *      n ranges -> at most 2n distinct boundaries
     *
     *      Time  = O(n log n)
     *      Space = O(n)
     */


    // =========================================================================
    // 30-SECOND RECALL CARD
    // =========================================================================

    /*
     * BOUNDARY DELTA
     * --------------
     * Trigger:
     *      many ranges change an aggregate value.
     *
     * Inclusive [L, R]:
     *
     *      diff[L]     += value
     *      diff[R + 1] -= value
     *
     * Half-open [L, R):
     *
     *      diff[L] += value
     *      diff[R] -= value
     *
     * Then:
     *
     *      running += diff[i]
     *
     * Dense / bounded:
     *      Difference Array.
     *
     * Sparse / huge:
     *      TreeMap / sorted events.
     *
     * MEMORY LINE:
     *
     *      START carrying the effect.
     *      KEEP carrying it through the range.
     *      STOP carrying it when the range ends.
     *
     *      diff[]  = where carrying CHANGES.
     *      running = what I am carrying NOW.
     *
     * "Prefix sum" is simply the formal name for maintaining that running carry.
     */

    // =========================================================================
    // HORIZONTAL MASTERY — ONE ENGINE, DIFFERENT STORIES
    // =========================================================================

    /*
     * RANGE ADDITION
     *      [L, R] += increment
     *
     *          ↓ rename value
     *
     * CORPORATE FLIGHT BOOKINGS
     *      [firstFlight, lastFlight] += seats
     *
     *          ↓ change endpoint semantics to half-open
     *
     * CAR POOLING
     *      [pickup, dropoff) += passengers
     *
     *          ↓ weight becomes 1
     *
     * MAXIMUM POPULATION YEAR
     *      [birth, death) += 1
     *
     * SAME ENGINE:
     *
     *      boundary -> delta
     *      prefix deltas -> actual state
     *
     * DO NOT MEMORIZE FOUR ALGORITHMS.
     */

    // =========================================================================
    // QUESTION MUTATIONS / REPRESENTATION CHOICES
    // =========================================================================

    /*
     * MUTATION 1 — INCLUSIVE -> HALF-OPEN
     * -----------------------------------
     * Inclusive [L,R]:
     *      cancel at R + 1.
     *
     * Half-open [L,R):
     *      cancel at R.
     *
     *
     * MUTATION 2 — DENSE -> SPARSE
     * ----------------------------
     * Small bounded integer coordinates:
     *      int[] difference.
     *
     * Huge / sparse coordinates:
     *      TreeMap / sorted events.
     *
     *
     * MUTATION 3 — UNIT -> WEIGHTED
     * -----------------------------
     * Population:
     *      +1 / -1.
     *
     * Car Pooling:
     *      +passengers / -passengers.
     *
     * Same engine.
     *
     *
     * MUTATION 4 — FULL OUTPUT -> PEAK / FEASIBILITY
     * ----------------------------------------------
     * Range Addition / Flight Bookings:
     *      materialize every prefix value.
     *
     * Population:
     *      track maximum prefix value.
     *
     * Car Pooling:
     *      fail when prefix value exceeds capacity.
     */

    // =========================================================================
    // HIGH-ROI APPROACH POLICY
    // =========================================================================

    /*
     * MASTER DEEPLY
     * -------------
     *      Inclusive Difference Array:
     *
     *          diff[L]     += x
     *          diff[R + 1] -= x
     *          prefix
     *
     * ALSO MASTER
     * -----------
     *      Half-open boundary semantics:
     *
     *          diff[L] += x
     *          diff[R] -= x
     *
     * UNDERSTAND AS SAME ENGINE
     * -------------------------
     *      Sparse Sweep / TreeMap.
     *
     * Do NOT treat TreeMap Sweep as an unrelated trick.
     *
     * ROI RULE
     * --------
     *      Store the smallest state that still answers the question.
     *
     * If aggregate load is enough, do not preserve individual interval identity.
     */

    // =========================================================================
    // INTERVIEW ARTICULATION
    // =========================================================================

    /*
     * DIFFERENCE ARRAY — SAY IT LIKE THIS
     * -----------------------------------
     * "Instead of applying each update to every position inside its range, I record
     * only where that contribution starts and where it stops. A prefix sum of those
     * boundary changes reconstructs the actual value at each position. So each range
     * update becomes O(1), followed by one linear scan."
     *
     *
     * CAR POOLING — SAY IT LIKE THIS
     * ------------------------------
     * "Each pickup adds passengers at its start location and each dropoff removes
     * them at its end location. Because the locations are bounded, I can store those
     * deltas in an array. The prefix sum is the current passenger load, and if it
     * ever exceeds capacity the schedule is impossible."
     *
     *
     * SPARSE SWEEP — SAY IT LIKE THIS
     * -------------------------------
     * "The invariant is still boundary delta plus ordered prefix accumulation.
     * If coordinates are too large or sparse for an array, I store only the actual
     * boundaries in a TreeMap. Since this solution needs only the deltas, I iterate
     * values(); if I also needed each coordinate, I would iterate entrySet()."
     */

    // =========================================================================
    // MASTERY EXIT CHECK
    // =========================================================================

    /*
     * MOVE ON WHEN YOU CAN:
     *      [ ] Explain a range first as START / KEEP / STOP carrying.
     *      [ ] Derive diff[] only AFTER that carrying picture is clear.
     *      [ ] Explain running prefix as "what am I carrying right now?"
     *      [ ] Dry-run boundary markers -> running prefix without looking at code.
     *      [ ] See [L,R] += x and derive the two difference boundaries.
     *      [ ] Explain R + 1 for inclusive ranges.
     *      [ ] Explain R for half-open ranges.
     *      [ ] Code Range Addition from memory.
     *      [ ] Recognize Flight Bookings as the same inclusive template.
     *      [ ] Recognize Car Pooling as weighted half-open deltas.
     *      [ ] Recognize Population Year as unit-weight half-open deltas.
     *      [ ] Switch int[] -> TreeMap when coordinates become sparse.
     *      [ ] Explain why prefix accumulation reconstructs actual state.
     */

    // =========================================================================
    // TESTS
    // Run with assertions enabled:
    // java -ea org.chijai.day1.Arrays.session4.RangeUpdates.BoundaryDelta
    // =========================================================================

    public static void main(String[] args) {

        RangeAddition rangeAddition = new RangeAddition();

        assert Arrays.equals(
                rangeAddition.getModifiedArray(
                        5,
                        new int[][]{
                                {1, 3, 2},
                                {2, 4, 3},
                                {0, 2, -2}
                        }
                ),
                new int[]{-2, 0, 3, 5, 3}
        );

        CorporateFlightBookings flightBookings = new CorporateFlightBookings();

        assert Arrays.equals(
                flightBookings.corpFlightBookings(new int[][]{
                        {1, 2, 10},
                        {2, 3, 20},
                        {2, 5, 25}
                }, 5),
                new int[]{10, 55, 45, 25, 25}
        );

        CarPooling carPooling = new CarPooling();

        assert !carPooling.carPooling(new int[][]{
                {2, 1, 5},
                {3, 3, 7}
        }, 4);

        assert carPooling.carPooling(new int[][]{
                {2, 1, 5},
                {3, 3, 7}
        }, 5);

        // Dropoff and pickup at the same location reuse capacity.
        assert carPooling.carPooling(new int[][]{
                {3, 5, 7},
                {3, 7, 9}
        }, 3);

        MaximumPopulationYear population = new MaximumPopulationYear();

        assert population.maximumPopulation(new int[][]{
                {1993, 1999},
                {2000, 2010}
        }) == 1993;

        assert population.maximumPopulation(new int[][]{
                {1950, 1961},
                {1960, 1971},
                {1970, 1981}
        }) == 1960;

        SparseWeightedSweep sparseSweep = new SparseWeightedSweep();

        assert sparseSweep.maximumActiveWeight(new int[][]{
                {2, 1, 5},
                {3, 3, 7}
        }) == 5;

        assert Arrays.equals(
                rangeAddition.getModifiedArray(0, new int[][]{}),
                new int[]{}
        );

        assert Arrays.equals(
                flightBookings.corpFlightBookings(new int[][]{}, 0),
                new int[]{}
        );

        assert carPooling.carPooling(new int[][]{}, 1);
        assert population.maximumPopulation(new int[][]{}) == 0;
        assert sparseSweep.maximumActiveWeight(new int[][]{}) == 0;

        System.out.println("All BoundaryDelta assertions passed.");
    }
}
