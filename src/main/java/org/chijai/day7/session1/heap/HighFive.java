package org.chijai.day7.session1.heap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * LeetCode 1086 - High Five
 *
 * https://leetcode.com/problems/high-five/
 *
 * =====================================================================================
 * 1. 📘 PROBLEM + PATTERN CLASSIFICATION
 * =====================================================================================
 *
 * Given items where
 *
 *     item[0] = studentId
 *     item[1] = score
 *
 * return, for every student:
 *
 *     [studentId, integer average of that student's TOP 5 scores]
 *
 * Output must be ordered by increasing studentId.
 *
 * Every student has at least 5 scores.
 *
 * -------------------------------------------------------------------------------------
 * CATEGORY MAP
 * -------------------------------------------------------------------------------------
 *
 * Broad Category
 *     Heap / PriorityQueue
 *
 * Algorithmic Family
 *     Top K / Streaming Selection
 *
 * Primary Pattern
 *     GROUP BY KEY + BOUNDED TOP K
 *
 * Exact Sub-pattern
 *     For every key, keep K LARGEST values
 *     using a MIN-HEAP of size K.
 *
 * Secondary Concern
 *     Sort output keys once at the end.
 *
 * Data-structure responsibilities
 *
 *     HashMap
 *         -> WHICH student?
 *
 *     Min-heap
 *         -> WHICH 5 scores survive for that student?
 *
 *     Sorted id list
 *         -> required output order
 *
 * -------------------------------------------------------------------------------------
 * RECOGNITION SIGNALS
 * -------------------------------------------------------------------------------------
 *
 * Think:
 *
 *     GROUP BY KEY + TOP K
 *
 * when the problem says:
 *
 *     • many records belong to the same id / user / category
 *     • only best / largest / smallest K matter inside each group
 *     • K is much smaller than total records
 *     • values may arrive in arbitrary order
 *     • sum / average / answer uses only those K survivors
 *
 * -------------------------------------------------------------------------------------
 * FAMILY RULE
 * -------------------------------------------------------------------------------------
 *
 *     want K LARGEST
 *         -> MIN-HEAP size K
 *
 *     want K SMALLEST
 *         -> MAX-HEAP size K
 *
 * Why?
 *
 * The heap root should be the WEAKEST CURRENT WINNER,
 * because that is exactly the element we may need to evict.
 *
 * =====================================================================================
 * 2. 🧠 15-SECOND RETRIEVAL ANCHOR
 * =====================================================================================
 *
 *     STUDENT -> MIN-HEAP(5)
 *
 *     for each [id, score]
 *
 *         get heap
 *         if missing -> create + put
 *         offer score
 *
 *         if size > 5
 *             poll smallest
 *
 *     sort student ids
 *
 *     for each id
 *         sum heap
 *         average = sum / 5
 *
 * Memory sentence
 *
 *     KEEP THE FIVE WINNERS;
 *     THE HEAP ROOT IS THE WEAKEST WINNER.
 *
 * Core invariant
 *
 *     After processing any prefix of input,
 *     each student's heap contains exactly the
 *     min(5, scoresSeenForStudent) LARGEST scores seen so far.
 *
 * Trap
 *
 *     TOP 5 LARGEST -> MIN-HEAP,
 *     not max-heap.
 *
 * =====================================================================================
 * 3. 🟢 PRIMARY PHOTOGRAPHIC-MEMORY SOLUTION
 * =====================================================================================
 */
public class HighFive {

    static class Primary {

        private static final int TOP_K = 5;

        public int[][] highFive(int[][] items) {

            Map<Integer, PriorityQueue<Integer>> topScores =
                    new HashMap<>();

            for (int[] item : items) {

                int studentId = item[0];
                int score = item[1];

                PriorityQueue<Integer> minHeap =
                        topScores.get(studentId);

                if (minHeap == null) {
                    minHeap = new PriorityQueue<>();
                    topScores.put(studentId, minHeap);
                }

                minHeap.offer(score);

                if (minHeap.size() > TOP_K) {
                    minHeap.poll();
                }
            }

            List<Integer> studentIds =
                    new ArrayList<>(topScores.keySet());

            Collections.sort(studentIds);

            int[][] answer =
                    new int[studentIds.size()][2];

            for (int i = 0; i < studentIds.size(); i++) {

                int studentId = studentIds.get(i);
                int sum = 0;

                for (int score : topScores.get(studentId)) {
                    sum += score;
                }

                answer[i][0] = studentId;
                answer[i][1] = sum / TOP_K;
            }

            return answer;
        }
    }


    /*
     =====================================================================================
     4. ☕ JAVA API BREAKDOWN — computeIfAbsent
     =====================================================================================

     Optional compact Java shortcut

         PriorityQueue<Integer> minHeap =
                 topScores.computeIfAbsent(
                         studentId,
                         ignored -> new PriorityQueue<>());

     The PRIMARY solution intentionally expands this for recall:

         PriorityQueue<Integer> minHeap =
                 topScores.get(studentId);

         if (minHeap == null) {
             minHeap = new PriorityQueue<>();
             topScores.put(studentId, minHeap);
         }

     Read computeIfAbsent as

         "Get this student's heap.
          If no heap exists yet, create one, store it, and return it."

     Equivalent expanded Java

         PriorityQueue<Integer> minHeap = topScores.get(studentId);

         if (minHeap == null) {
             minHeap = new PriorityQueue<>();
             topScores.put(studentId, minHeap);
         }

     computeIfAbsent compresses exactly that get/create/store pattern.

     Piece by piece

         topScores
             Map<Integer, PriorityQueue<Integer>>

         studentId
             key we are looking up

         computeIfAbsent(studentId, ...)
             return existing value for studentId;
             otherwise compute and insert a value

         ignored -> new PriorityQueue<>()
             function used ONLY when studentId is absent

             ignored is the missing key supplied to the lambda.
             We do not need the key to construct an empty heap,
             so the parameter is intentionally named ignored.

         returned value
             either the existing heap
             OR the newly created and stored heap

     Example

         map initially:

             {}

         studentId = 1

             absent
             -> create heap A
             -> map becomes {1 -> heap A}
             -> minHeap references heap A

         studentId = 1 again

             present
             -> no new heap
             -> minHeap references the SAME heap A

     Important aliasing point

         minHeap.offer(score);

     mutates the same PriorityQueue object already stored inside the map.

     Therefore we do NOT need:

         topScores.put(studentId, minHeap);

     after every offer.

     Memory translation

         computeIfAbsent
             = GET OR CREATE + STORE

     =====================================================================================
     5. 🧭 FIRST-PRINCIPLES INVENTION PATH
     =====================================================================================

     Start from the obstacle, not from "this looks like a heap problem."

     1. Scores from different students must never compete.

            score belongs to exactly one student

        Therefore first separate the problem by key:

            studentId -> that student's state

        HashMap naturally solves:

            WHICH STUDENT?

     2. For one student, the final answer ignores every score
        except the largest 5.

        So storing every historical score is unnecessary.

     3. Freeze one student after several scores.

        What information from the past can still affect the answer?

            only the best 5 seen so far

        Any score below those 5 can be permanently forgotten.

     4. Suppose we already retained 5 candidates
        and one new score arrives.

        Temporarily there are 6 candidates.

        Exactly one must leave:

            the SMALLEST of the 6.

     5. Therefore we repeatedly need:

            add one score
            remove current minimum

        That is exactly what a min-heap provides.

     6. Keep heap size bounded at 5.

        After every insertion:

            if size > 5
                remove minimum

     7. The problem separately requires student ids in ascending order.

        We do not need ordered lookup while processing every score.

        Therefore:

            HashMap during grouping
            sort unique ids once at the end

        rather than paying TreeMap ordering cost on every input item.

     Final structure

         HashMap<studentId, minHeap>

         process
             get/create heap
             offer score
             if size > 5 -> poll

         finish
             sort ids
             sum each retained heap
             divide by 5

     Implementation devices solve different problems

         HashMap
             -> group records by student

         min-heap
             -> maintain only top 5 scores for that student

         heap-size bound
             -> discard information proven irrelevant

         final id sort
             -> satisfy output-order contract
     */


    /*
     =====================================================================================
     6. 🎞 ONE CANONICAL VISUAL DRY RUN
     =====================================================================================

     One student receives:

         91, 92, 60, 65, 87, 100

     First five:

         retained = {60, 65, 87, 91, 92}

         heap root = 60
                     ^
                     weakest current winner

     New score:

         100

     Temporarily six candidates:

         {60, 65, 87, 91, 92, 100}

     size > 5

         poll() -> 60

     Retained top five:

         {65, 87, 91, 92, 100}

     Notice:

         We do NOT care about heap iteration order.

         The heap is not being used to return scores sorted.
         It is being used only to make the weakest retained score
         cheap to evict.

     -------------------------------------------------------------------------------------
     WHY CAN A DISCARDED SCORE NEVER MATTER AGAIN?

     Suppose x is removed.

     At the moment x is removed,
     at least 5 scores are already >= x.

     Future scores can only add more competition.

     Therefore x can never become one of the final top 5.

     This is the entire safety proof behind the discard.
     */


    /*
     =====================================================================================
     7. ✅ CORRECTNESS + COMPLEXITY
     =====================================================================================

     Invariant

         For each student,
         after processing any prefix of input:

             heap contains exactly the
             min(5, numberSeen) largest scores seen so far.

     Initialization

         Empty heap contains the top 0 values.

     Maintenance

         Add the new score.

         If heap size <= 5:
             every seen score still belongs in the retained set.

         If heap size == 6:
             remove the smallest.

             The remaining 5 are exactly the largest 5
             among everything retained plus the new score.

         Scores discarded earlier were already proven unable
         to enter the final top 5.

     Termination

         After all records,
         every heap contains exactly that student's top 5 scores.

         Summing those 5 and dividing by 5 therefore gives
         the required integer average.

     -------------------------------------------------------------------------------------
     COMPLEXITY

     Let

         N = number of input records
         S = number of distinct students
         K = 5

     Processing scores

         Each offer / poll touches a heap of size at most K.

         O(N log K)

         Since K = 5:

             effectively O(N)

     Sorting student ids

         O(S log S)

     Final summation

         O(S * K)

     Total

         O(N log K + S log S)

     Space

         At most K retained scores per student:

             O(S * K)

         plus O(S) ids.

     With K = 5:

             O(S)

     Important distinction

         This solution does NOT store all N scores.
     */


    /*
     =====================================================================================
     8. 🧭 APPROACH / DATA-STRUCTURE TRADE-OFF MATRIX
     =====================================================================================

     +-------------------------------+------------------------+----------------------+------------------------+---------------------------------------------+
     | Approach                      | Scores stored/student  | Processing           | Output ordering        | Judgment                                    |
     +-------------------------------+------------------------+----------------------+------------------------+---------------------------------------------+
     | Store all + sort each group   | all                    | O(N log N) worst     | sort ids               | Simple baseline; stores irrelevant scores   |
     | HashMap + bounded min-heaps   | at most 5              | O(N log 5)           | sort ids once          | PRIMARY / best general interview choice     |
     | TreeMap + bounded min-heaps   | at most 5              | O(N(log S+log 5))    | already ordered        | Cleaner finish; pays ordering on every item |
     | Score-frequency buckets       | counts for score range | O(N + S*scoreRange)  | sort ids / ordered map | Useful only because score range is bounded  |
     +-------------------------------+------------------------+----------------------+------------------------+---------------------------------------------+

     Primary interview distinction

         HashMap solves GROUPING.
         Heap solves TOP K.
         Sorting ids solves OUTPUT ORDER.

     Do not make one data structure solve responsibilities
     that only arise later unless the trade-off is worth it.
     */


    /*
     =====================================================================================
     9. 🔄 RUNNABLE ALTERNATIVES
     =====================================================================================
     */

    /*
     Baseline: group every score, sort descending, take first 5.

     Easier to discover,
     but retains and sorts information the final answer does not need.
     */
    static class SortAllScores {

        private static final int TOP_K = 5;

        public int[][] highFive(int[][] items) {

            Map<Integer, List<Integer>> scoresByStudent =
                    new HashMap<>();

            for (int[] item : items) {

                scoresByStudent
                        .computeIfAbsent(
                                item[0],
                                ignored -> new ArrayList<>())
                        .add(item[1]);
            }

            List<Integer> studentIds =
                    new ArrayList<>(scoresByStudent.keySet());

            Collections.sort(studentIds);

            int[][] answer =
                    new int[studentIds.size()][2];

            for (int i = 0; i < studentIds.size(); i++) {

                int studentId = studentIds.get(i);

                List<Integer> scores =
                        scoresByStudent.get(studentId);

                scores.sort(Comparator.reverseOrder());

                int sum = 0;

                for (int j = 0; j < TOP_K; j++) {
                    sum += scores.get(j);
                }

                answer[i][0] = studentId;
                answer[i][1] = sum / TOP_K;
            }

            return answer;
        }
    }


    /*
     TreeMap alternative

         Same bounded Top-K invariant.

         New purpose:
             keep ids ordered during processing.

         Trade-off:
             avoids final id sort,
             but every input record pays TreeMap O(log S).

         Not primary because output ordering is needed only once.
     */
    static class OrderedMapAlternative {

        private static final int TOP_K = 5;

        public int[][] highFive(int[][] items) {

            Map<Integer, PriorityQueue<Integer>> topScores =
                    new TreeMap<>();

            for (int[] item : items) {

                PriorityQueue<Integer> minHeap =
                        topScores.computeIfAbsent(
                                item[0],
                                ignored -> new PriorityQueue<>());

                minHeap.offer(item[1]);

                if (minHeap.size() > TOP_K) {
                    minHeap.poll();
                }
            }

            int[][] answer =
                    new int[topScores.size()][2];

            int index = 0;

            for (Map.Entry<Integer, PriorityQueue<Integer>> entry
                    : topScores.entrySet()) {

                int sum = 0;

                for (int score : entry.getValue()) {
                    sum += score;
                }

                answer[index][0] = entry.getKey();
                answer[index][1] = sum / TOP_K;

                index++;
            }

            return answer;
        }
    }


    /*
     =====================================================================================
     10. 🟣 INTERVIEW ARTICULATION
     =====================================================================================

     Strong concise explanation

         "I group scores by student id.

          For each student I maintain a min-heap capped at five elements.

          My invariant is that after every processed record,
          that heap contains the five largest scores seen so far
          for that student, or all scores if fewer than five have appeared.

          When a sixth candidate arrives,
          the smallest of those six cannot belong to the top five,
          so I remove the min-heap root.

          At the end I sort the student ids once,
          sum each student's five retained scores,
          and use integer division by five."

     -------------------------------------------------------------------------------------
     WHY MIN-HEAP FOR LARGEST K?

         I need fast access to the smallest CURRENTLY RETAINED score,
         because that is the first score to evict.

     -------------------------------------------------------------------------------------
     WHAT BREAKS WITH AN UNBOUNDED HEAP?

         Correctness still works,
         but the main optimization disappears.

         We would keep irrelevant scores and use unnecessary space.

     -------------------------------------------------------------------------------------
     WHAT BREAKS IF WE POLL BEFORE OFFERING?

         If we blindly poll whenever size == 5
         before seeing how the new score compares,
         we may evict a valid winner for a worse incoming score.

         Safe mechanical rule:

             offer
             if size > K
                 poll

     -------------------------------------------------------------------------------------
     DO WE NEED TO SORT THE HEAP?

         No.

         We only need the sum of the surviving five values.
         Heap iteration order is irrelevant.

     -------------------------------------------------------------------------------------
     STREAMING?

         Yes.

         The top-five state for each student can be maintained online
         without retaining the full score history.

     =====================================================================================
     11. 🎯 INTERVIEW RECALL SHEET
     =====================================================================================

     Trigger
         per-key / per-user TOP K

     Pattern
         GROUP BY KEY + BOUNDED HEAP

     Core invariant
         heap = largest min(K, seen) values for that key

     For K largest
         min-heap

     Heap root means
         weakest current winner

     Update
         offer
         if size > K -> poll

     Finish
         aggregate retained values

     Output-order concern
         sort keys once if required

     Common trap
         using max-heap for K largest

     Another trap
         storing every score even though only 5 can matter

     Re-derivation cue

         "If I have K winners and one challenger,
          who must leave?"

         Answer:
             weakest winner / minimum

         Therefore:
             min-heap

     =====================================================================================
     12. 🔄 VARIATIONS + PATTERN BOUNDARIES
     =====================================================================================

     Variation: average top K instead of top 5

         Replace constant 5 with K.
         Same invariant.

     Variation: keep K smallest

         Reverse the eviction frontier:

             max-heap size K

     Variation: return retained scores sorted

         The bounded heap still selects the winners,
         but sort only the K survivors before returning.

     Variation: all scores are needed later

         Bounded heap is no longer sufficient.
         Discarding history would destroy required information.

     Variation: frequent arbitrary deletions / score updates

         Simple heap becomes awkward because deleting arbitrary stale values
         is not directly supported.

         You may need:
             indexed heap,
             ordered multiset / TreeMap counts,
             or lazy deletion depending on the exact contract.

     Pattern boundary

         Heap is ideal when the repeated question is:

             "Which K values survive?"

         It is not a general replacement for:
             complete sorted order,
             arbitrary rank queries,
             arbitrary predecessor queries,
             or full history.

     =====================================================================================
     13. ⚫ RELATED PROBLEMS + TRANSFER MAP
     =====================================================================================

     SAME CORE HEAP MECHANISM

     +---------+--------------------------------------+-----------------------------+--------------------------------------------------+
     | LC      | Problem                              | Pattern                     | What transfers                                   |
     +---------+--------------------------------------+-----------------------------+--------------------------------------------------+
     | 703     | Kth Largest Element in a Stream      | bounded Top-K largest       | min-heap size K; root = kth largest              |
     | 215     | Kth Largest Element in an Array      | Top-K / selection           | same min-heap orientation for K largest          |
     | 347     | Top K Frequent Elements              | aggregate -> Top K          | count first, then min-heap size K                |
     | 692     | Top K Frequent Words                 | Top K + tie-breaking        | bounded heap plus custom comparator              |
     | 973     | K Closest Points to Origin           | bounded Top-K smallest      | reverse orientation: max-heap size K             |
     | 658     | Find K Closest Elements              | K-selection family          | retain/select K best under a comparison rule     |
     +---------+--------------------------------------+-----------------------------+--------------------------------------------------+

     CLOSEST EXACT SHAPE: GROUP -> KEEP BEST K PER KEY

         High Five is a grouped version of bounded Top-K:

             key -> independent Top-K state

         The reusable skeleton is:

             Map<Key, PriorityQueue<Value>> groups

             for each record
                 heap = groups.computeIfAbsent(
                         key,
                         ignored -> new PriorityQueue<>())

                 heap.offer(value)

                 if heap.size() > K
                     heap.poll()

             aggregate each bounded heap

     PATTERN CONTRASTS WORTH PRACTICING

         LC 703 — Kth Largest Element in a Stream

             No grouping.
             One global min-heap of size K.

             High Five adds:

                 key -> heap

         LC 347 — Top K Frequent Elements

             First aggregate:

                 value -> frequency

             Then perform Top K on frequencies.

             High Five performs Top K independently DURING grouping.

         LC 973 — K Closest Points to Origin

             Want K SMALLEST distances.

             Therefore:

                 max-heap size K

             This is the cleanest contrast for remembering
             heap orientation.

         LC 692 — Top K Frequent Words

             Same Top-K selection,
             but now "weakest winner" depends on:

                 frequency
                 + lexicographic tie-breaking

             This trains comparator design.

     RETRIEVAL LADDER

         Learn in this order:

             703  -> one bounded min-heap
             973  -> reverse heap orientation
             347  -> aggregate then Top K
             692  -> Top K with comparator tie rules
             1086 -> one bounded heap PER KEY

     High Five's distinctive upgrade is therefore:

         TOP K
             ->
         TOP K PER GROUP

     =====================================================================================
     14. 🧪 SELF-VERIFYING TESTS
     =====================================================================================
     */

    public static void main(String[] args) {

        Primary solution = new Primary();

        /*
         Happy path:
         multiple students + more than five scores.
         */
        assertMatrixEquals(
                new int[][]{
                        {1, 87},
                        {2, 89}
                },
                solution.highFive(
                        new int[][]{
                                {1, 91},
                                {1, 92},
                                {2, 93},
                                {2, 97},
                                {1, 60},
                                {2, 77},
                                {1, 65},
                                {1, 87},
                                {1, 100},
                                {2, 100},
                                {2, 76},
                                {2, 80}
                        }));

        /*
         Exactly five:
         no eviction should occur.
         */
        assertMatrixEquals(
                new int[][]{
                        {7, 30}
                },
                solution.highFive(
                        new int[][]{
                                {7, 10},
                                {7, 20},
                                {7, 30},
                                {7, 40},
                                {7, 50}
                        }));

        /*
         Trap:
         a late low score must not displace a top-five winner.
         */
        assertMatrixEquals(
                new int[][]{
                        {3, 80}
                },
                solution.highFive(
                        new int[][]{
                                {3, 100},
                                {3, 90},
                                {3, 80},
                                {3, 70},
                                {3, 60},
                                {3, 1}
                        }));

        /*
         Output ordering:
         input student ids are deliberately scrambled.
         */
        assertMatrixEquals(
                new int[][]{
                        {1, 10},
                        {9, 90}
                },
                solution.highFive(
                        new int[][]{
                                {9, 90},
                                {1, 10},
                                {9, 90},
                                {1, 10},
                                {9, 90},
                                {1, 10},
                                {9, 90},
                                {1, 10},
                                {9, 90},
                                {1, 10}
                        }));

        /*
         Integer division:
         top-five sum 404 -> 80, not 80.8.
         */
        assertMatrixEquals(
                new int[][]{
                        {4, 80}
                },
                solution.highFive(
                        new int[][]{
                                {4, 100},
                                {4, 90},
                                {4, 80},
                                {4, 70},
                                {4, 64}
                        }));

        System.out.println("All HighFiveV3 tests passed.");
    }


    private static void assertMatrixEquals(
            int[][] expected,
            int[][] actual) {

        if (!Arrays.deepEquals(expected, actual)) {
            throw new AssertionError(
                    "Expected: "
                            + Arrays.deepToString(expected)
                            + "\nActual:   "
                            + Arrays.deepToString(actual));
        }
    }
}
