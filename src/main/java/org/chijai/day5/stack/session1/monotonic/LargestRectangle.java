package org.chijai.day5.stack.session1.monotonic;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * =====================================================================================
 * LARGEST RECTANGLE IN HISTOGRAM — NEAREST-SMALLER BOUNDARY
 * =====================================================================================
 *
 * Core question:
 *
 *      For each bar, how far can it extend left and right
 *      while remaining the minimum height?
 *
 * Core invariant:
 *
 *      Stack stores unresolved indices in non-decreasing height order.
 *
 * Pop meaning:
 *
 *      A smaller current bar FINALIZES a taller unresolved bar.
 *
 * =====================================================================================
 *
 * FILE NAVIGATION RULE:
 *
 *      Need to code now?
 *          -> Problem -> 10-second card -> Primary Implementation
 *
 *      Need to understand/reconstruct?
 *          -> continue into derivation, dry run, cross-product, proof, ±Δ
 *
 * =====================================================================================
 */
public class LargestRectangle {

    /*
     * =================================================================================
     * 1️⃣ PROBLEM STATEMENT
     * =================================================================================
     *
     * Given histogram bar heights, return the largest rectangle area.
     *
     * Example:
     *
     *      heights = [2,1,5,6,2,3]
     *      index      0 1 2 3 4 5
     *
     * Focus on bar index 2:
     *
     *      height = 5
     *
     * Ask:
     *
     *      "How far can height 5 survive contiguously?"
     *
     *      index:    1      2      3      4
     *      height:   1      5      6      2
     *                ↑      └──────┘      ↑
     *            LEFT BLOCKER         RIGHT BLOCKER
     *
     * Both blockers are STRICTLY SMALLER than 5.
     *
     * So height 5 can use only indices:
     *
     *      2 ... 3
     *
     * width:
     *
     *      2
     *
     * area:
     *
     *      5 * 2 = 10
     *
     *
     * Mental image:
     *
     *                 I AM BAR height H
     *                        |
     *                        v
     *
     *          ← ← ← can I survive? → → →
     *
     *      I may cross bars >= H.
     *
     *      FIRST bar < H
     *          =
     *        BLOCKER
     *        STOP
     *
     *
     * Therefore:
     *
     *      area = height * maximum valid width
     *
     * The real unknown is WIDTH.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 2️⃣ 10-SECOND RECOGNITION CARD
     * =================================================================================
     *
     * Problem truth:
     *
     *      area = height * widest contiguous span
     *
     * For one chosen bar:
     *
     *      every interior bar must be >= chosen height
     *
     * Therefore I need:
     *
     *      FIRST / NEAREST smaller blocker on LEFT
     *      FIRST / NEAREST smaller blocker on RIGHT
     *
     * That means:
     *
     *      unresolved candidates
     *      +
     *      current smaller can permanently close old taller bars
     *
     *      -> MONOTONIC STACK
     *
     *
     * Code verb:
     *
     *      CURRENT SMALLER
     *          -> POP TALLER
     *          -> FINALIZE POPPED BAR
     *
     *
     * On pop:
     *
     *      right = current index
     *      left  = new stack top, or -1
     *      width = right - left - 1
     *
     *
     * Six-month anchor:
     *
     *      NEAREST -> STACK
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 3️⃣ PRIMARY IMPLEMENTATION
     * =================================================================================
     */
    static class OptimalMonotonicStackSolution {

        public int largestRectangleArea(int[] heights) {

            int n = heights.length;
            int best = 0;

            Deque<Integer> stack = new ArrayDeque<>();

            for (int i = 0; i <= n; i++) {

                int currentHeight =
                        (i == n) ? 0 : heights[i];

                while (!stack.isEmpty()
                        && currentHeight
                        < heights[stack.peek()]) {

                    int index = stack.pop();
                    int height = heights[index];

                    int rightBoundary = i;

                    int leftBoundary =
                            stack.isEmpty()
                            ? -1
                            : stack.peek();

                    int width =
                            rightBoundary
                            - leftBoundary
                            - 1;

                    best = Math.max(
                            best,
                            height * width
                    );
                }

                if (i < n) {
                    stack.push(i);
                }
            }

            return best;
        }
    }


    /*
     * =================================================================================
     * 4️⃣ HOW THE BRAIN SHOULD SEE IT
     * =================================================================================
     *
     * Pick one bar with height H.
     *
     * It can keep extending:
     *
     *      LEFT  until the first bar < H
     *      RIGHT until the first bar < H
     *
     * Those smaller bars are blockers.
     *
     * If:
     *
     *      leftSmaller  = L
     *      rightSmaller = R
     *
     * then THIS bar owns:
     *
     *      L + 1 ... R - 1
     *
     * width:
     *
     *      R - L - 1
     *
     * area:
     *
     *      H * (R - L - 1)
     *
     *
     * Brute-force thought:
     *
     *      for every bar
     *          scan left until smaller
     *          scan right until smaller
     *
     *      O(n²)
     *
     * Repeated work:
     *
     *      many bars repeatedly search for the same
     *      nearest-smaller boundaries.
     *
     * Structural question:
     *
     *      Can a current smaller bar tell us that some previous bar
     *      can NEVER extend any farther right?
     *
     * Yes.
     *
     * That is the pop event.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 5️⃣ UNSEEN-PROBLEM DECODER — SEE THE STRUCTURE BEFORE CODING
     * =================================================================================
     *
     * For any random problem, ask these in order:
     *
     *
     * STEP 1 — WHAT DOES EACH ELEMENT NEED?
     *
     * Here:
     *
     *      each bar needs its MAXIMUM CONTIGUOUS SPAN
     *      where it remains the minimum.
     *
     *
     * STEP 2 — WHAT STOPS THAT SPAN?
     *
     * Here:
     *
     *      a STRICTLY SMALLER bar.
     *
     * So we need directional boundaries:
     *
     *      nearest smaller on left
     *      nearest smaller on right
     *
     *
     * STEP 3 — CAN CURRENT FINALIZE OLD ELEMENTS?
     *
     * Suppose:
     *
     *      old bar height = 6
     *      current height = 2
     *
     * Once 2 appears:
     *
     *      6 can NEVER extend through current.
     *
     * Its right boundary is now known permanently.
     *
     *      current smaller
     *          ↓
     *      old taller bar closes
     *
     * This permanent-resolution event is the monotonic-stack signal.
     *
     *
     * STEP 4 — WHAT MUST THE STACK MEAN?
     *
     * Not:
     *
     *      "a bunch of previous indices"
     *
     * It must have a mathematical meaning:
     *
     *      unresolved bars whose right-smaller boundary
     *      has not been found yet.
     *
     * Heights stay:
     *
     *      NON-DECREASING bottom -> top
     *
     *
     * STEP 5 — WHAT DOES POP GIVE ME?
     *
     * When index x is popped because current index i is smaller:
     *
     *      RIGHT boundary = i
     *
     * After x disappears:
     *
     *      LEFT boundary = new stack top
     *                      or -1 if empty
     *
     * Then:
     *
     *      width = right - left - 1
     *
     *
     * -------------------------------------------------------------------------
     * MENTAL IMAGE TO LOCK
     * -------------------------------------------------------------------------
     *
     * STACK = UNRESOLVED BARS
     *
     *      [small] [medium] [tall]
     *                          ↑
     *                       waiting
     *
     *                    CURRENT SMALLER
     *                           |
     *                           v
     *
     *      [small] [medium] [tall]    [current]
     *                          |
     *                          v
     *                      POP tall
     *
     *      RIGHT = current
     *
     *      LEFT  = survivor after pop
     *
     *      POP = FINALIZE AREA
     *
     *
     * -------------------------------------------------------------------------
     * 10-SECOND DECISION TREE
     * -------------------------------------------------------------------------
     *
     *               RANDOM ARRAY PROBLEM
     *                        |
     *                        v
     *             Need contiguous span/boundary?
     *                  /             \
     *                NO               YES
     *                |                 |
     *          other family            v
     *                    Element remains MIN/MAX?
     *                         /            \
     *                       NO              YES
     *                       |                |
     *                  other family         v
     *                           First smaller/greater blocks it?
     *                                /             \
     *                              NO               YES
     *                              |                 |
     *                         other family           v
     *                              Can current permanently
     *                              finalize old candidates?
     *                                /             \
     *                              NO               YES
     *                                                |
     *                                                v
     *                                        MONOTONIC STACK
     *
     *
     * -------------------------------------------------------------------------
     * RANDOM-PROBLEM WORKSHEET
     * -------------------------------------------------------------------------
     *
     *      WHAT span/boundary is needed?
     *
     *      WHAT value blocks it?
     *
     *      WHAT does the stack represent?
     *
     *      WHEN does current defeat/finalize the top?
     *
     *      WHAT does POP reveal?
     *
     *      WHAT formula uses those boundaries?
     *
     * If these are clear, code becomes mechanical.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 6️⃣ PATTERN RECOGNITION + BOUNDARY
     * =================================================================================
     *
     * PRIMARY:
     *
     *      Monotonic Stack
     *
     * SUBTYPE:
     *
     *      Nearest Smaller Boundary
     *
     * ARCHETYPE:
     *
     *      Find the maximal contiguous span where each element
     *      remains the minimum.
     *
     *
     * Deep comparison with Trapping Rain Water and
     * Container With Most Water appears in the next section.
     *
     * =================================================================================
     */



    /*
     * =================================================================================
     * ⭐ DEEP CONFUSION TRIANGLE — SAME HEIGHT ARRAY, THREE DIFFERENT ASKS
     * =================================================================================
     *
     * Do NOT memorize:
     *
     *      Container -> Two Pointers
     *      Rain      -> Prefix/Suffix
     *      Rectangle -> Stack
     *
     * Memorize the INFORMATION each problem asks for:
     *
     *      NEAREST  -> Monotonic Stack
     *
     *      BEST     -> Prefix / Suffix
     *
     *      PAIR     -> Two Pointers
     *
     *
     * -------------------------------------------------------------------------
     * SAME HEIGHT ARRAY — THREE DIFFERENT MENTAL PICTURES
     * -------------------------------------------------------------------------
     *
     *      heights = [8,1,6,2,7]
     *
     *
     * LARGEST RECTANGLE
     *
     *      "Where does MY chosen height stop?"
     *
     *              chosen H
     *                 ↓
     *
     *          ← ← ←  H  → → →
     *
     *      every interior bar must be >= H
     *
     *      first bar < H
     *          =
     *        BLOCKER
     *
     *      Need:
     *          NEAREST smaller boundary
     *
     *
     * TRAPPING RAIN WATER
     *
     *      "How strong are MY surrounding walls?"
     *
     *      BEST LEFT WALL                 BEST RIGHT WALL
     *            █~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~█
     *            █~~~~~~~~~~~ water ~~~~~~~~~~~~~█
     *                           ↑
     *                        current
     *
     *      Need:
     *          BEST / TALLEST wall anywhere on each side
     *
     *
     * CONTAINER WITH MOST WATER
     *
     *      "Which TWO walls should I choose?"
     *
     *      LEFT ENDPOINT                 RIGHT ENDPOINT
     *            █~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~█
     *            █~~~~ interior irrelevant ~~~~~█
     *
     *      Need:
     *          BEST endpoint PAIR
     *
     *
     * -------------------------------------------------------------------------
     * DEEPEST DISCRIMINATOR — WHAT DO INTERIOR BARS MEAN?
     * -------------------------------------------------------------------------
     *
     *      RECTANGLE
     *          interior = CONSTRAINT
     *
     *      RAIN
     *          interior = CONTRIBUTION / VALLEY
     *
     *      CONTAINER
     *          interior = IRRELEVANT
     *
     *
     * Brutal separator:
     *
     *      [8,1,8]
     *
     * Container:
     *
     *      the two 8s form a valid large container.
     *      middle 1 does NOT matter.
     *
     * Largest Rectangle:
     *
     *      an 8-high rectangle CANNOT cross the middle 1.
     *
     *
     * -------------------------------------------------------------------------
     * LAWYER-LEVEL ONE-LINE DIFFERENCES
     * -------------------------------------------------------------------------
     *
     * Largest Rectangle:
     *
     *      CONTIGUOUS-THRESHOLD problem:
     *      chosen height extends only until the nearest violation.
     *
     * Trapping Rain Water:
     *
     *      BILATERAL-CONTAINMENT problem:
     *      each position depends on the strongest wall on both sides.
     *
     * Container With Most Water:
     *
     *      GLOBAL PAIR-SELECTION problem:
     *      only chosen endpoints determine area; interior heights do not.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * ⭐ 3 × 3 CROSS-PRODUCT ANTI-CONFUSION MATRIX
     * =================================================================================
     *
     * Rows:
     *
     *      What INFORMATION does the problem require?
     *
     * Columns:
     *
     *      What MACHINERY am I considering?
     *
     *
     * ================================================================================================================
     * ASK ↓ / TOOL →     | MONOTONIC STACK              | PREFIX / SUFFIX              | TWO POINTERS
     * ================================================================================================================
     *
     * NEAREST BLOCKER    | ✅ EXACT FIT                 | ❌ WRONG INFORMATION          | ❌ NO ENDPOINT DISCARD
     *                     |                             |                               |
     * "Where do I stop?" | keeps unresolved candidates | summarizes BEST over a side  | interior blockers matter
     *                     | current resolves old        | loses nearest-boundary event | so endpoint-only logic fails
     *                     |                             |                               |
     * Example:            | LARGEST RECTANGLE           |                               |
     *
     * ---------------------------------------------------------------------------------------------------------------
     *
     * BEST SIDE SUPPORT   | ⚠️ possible only with a     | ✅ EXACT FIT                  | ✅ space-optimized version
     *                     | different basin invariant   |                               | after derivation
     * "Strongest wall     |                             | leftBest / rightBest          |
     *  on each side?"     | do NOT confuse with         | store exactly what each       | chosen side can be finalized
     *                     | histogram nearest-boundary  | position asks for             | without arrays
     *                     | stack                       |                               |
     * Example:            |                             | TRAPPING RAIN WATER           | TRAPPING RAIN WATER
     *
     * ---------------------------------------------------------------------------------------------------------------
     *
     * BEST ENDPOINT PAIR  | ❌ NEAREST BOUNDARY         | ❌ INDEPENDENT SIDE SUMMARY   | ✅ EXACT FIT
     *                     |    IRRELEVANT               |    INSUFFICIENT               |
     * "Which two ends?"   |                             |                               | evaluate pair
     *                     | middle values do not close  | answer depends on coupled     | discard limiting endpoint
     *                     | an endpoint candidate       | pair + distance               |
     * Example:            |                             |                               | CONTAINER
     *
     * ================================================================================================================
     *
     *
     * DIAGONAL TO LOCK:
     *
     *      NEAREST
     *          -> MONOTONIC STACK
     *
     *      BEST
     *          -> PREFIX / SUFFIX
     *
     *      PAIR
     *          -> TWO POINTERS
     *
     *
     * OFF-DIAGONAL TEST:
     *
     * Before choosing a tool, ask:
     *
     *      "What information would this tool preserve,
     *       and is that EXACTLY the information the problem asks for?"
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * ⭐ CODE-SHAPE CROSS PRODUCT — HOW THE REUSABLE TEMPLATE CHANGES
     * =================================================================================
     *
     * -------------------------------------------------------------------------
     * NEAREST -> MONOTONIC STACK
     * -------------------------------------------------------------------------
     *
     * State:
     *
     *      unresolved candidates
     *
     * Generic shape:
     *
     *      for (current) {
     *
     *          while (current defeats stack top) {
     *
     *              old = pop
     *
     *              // current gives old its boundary
     *              finalize(old)
     *          }
     *
     *          push(current)
     *      }
     *
     * Mental verb:
     *
     *      WAIT -> POP -> FINALIZE OLD
     *
     *
     * -------------------------------------------------------------------------
     * BEST -> PREFIX / SUFFIX
     * -------------------------------------------------------------------------
     *
     * State:
     *
     *      best summary of everything seen on each side
     *
     * Generic shape:
     *
     *      leftBest[i] =
     *          combine(leftBest[i - 1], nums[i]);
     *
     *      rightBest[i] =
     *          combine(rightBest[i + 1], nums[i]);
     *
     *      answer += contribution(
     *          leftBest[i],
     *          rightBest[i],
     *          nums[i]
     *      );
     *
     * Mental verb:
     *
     *      SUMMARIZE SIDE -> ANSWER CURRENT
     *
     *
     * -------------------------------------------------------------------------
     * PAIR -> TWO POINTERS
     * -------------------------------------------------------------------------
     *
     * State:
     *
     *      current candidate endpoint pair
     *
     * Generic shape:
     *
     *      left = 0
     *      right = n - 1
     *
     *      while (left < right) {
     *
     *          evaluate(left, right)
     *
     *          discard the endpoint
     *          proven unable to help further
     *      }
     *
     * Mental verb:
     *
     *      EVALUATE PAIR -> DISCARD END
     *
     *
     * -------------------------------------------------------------------------
     * WHAT CAN I FORGET FOREVER?
     * -------------------------------------------------------------------------
     *
     * MONOTONIC STACK:
     *
     *      forget candidate once current permanently resolves/dominates it.
     *
     * PREFIX / SUFFIX:
     *
     *      forget raw history once its required summary is stored.
     *
     * TWO POINTERS:
     *
     *      forget endpoint once a proof shows it cannot belong
     *      to a better remaining solution.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * ⭐ HORIZONTAL MASTERY — +Δ / -Δ MUTATIONS
     * =================================================================================
     *
     * Do not memorize problem -> pattern.
     *
     * Mutate one clause and ask:
     *
     *      "Which mathematical guarantee survives?"
     *
     *
     * -------------------------------------------------------------------------
     * NEAREST -> NEAREST
     * -------------------------------------------------------------------------
     *
     * Change:
     *
     *      nearest smaller
     *
     * to:
     *
     *      nearest greater
     *
     * Result:
     *
     *      +Δ
     *      same monotonic-boundary family
     *      comparator flips
     *
     *
     * -------------------------------------------------------------------------
     * NEAREST -> BEST
     * -------------------------------------------------------------------------
     *
     * Change:
     *
     *      "first blocker on each side"
     *
     * to:
     *
     *      "strongest value anywhere on each side"
     *
     * Result:
     *
     *      -Δ
     *      nearest-boundary invariant dies
     *      prefix/suffix aggregate becomes natural
     *
     *
     * -------------------------------------------------------------------------
     * BEST -> PAIR
     * -------------------------------------------------------------------------
     *
     * Change:
     *
     *      "compute contribution for every position"
     *
     * to:
     *
     *      "choose one globally best pair"
     *
     * Result:
     *
     *      -Δ
     *      per-position summary disappears
     *      pair optimization appears
     *
     *
     * -------------------------------------------------------------------------
     * PAIR -> NEAREST / CONTIGUOUS THRESHOLD
     * -------------------------------------------------------------------------
     *
     * Change:
     *
     *      "interior values do not matter"
     *
     * to:
     *
     *      "every interior value must satisfy a threshold"
     *
     * Result:
     *
     *      -Δ
     *      endpoint-only discard proof may die
     *      contiguous-boundary reasoning becomes relevant
     *
     *
     * RECALL:
     *
     *      NEAREST -> BEST
     *          stack state -> aggregate state
     *
     *      BEST -> PAIR
     *          per-position answer -> coupled pair answer
     *
     *      PAIR -> INTERIOR-CONSTRAINED
     *          endpoint-only geometry -> boundary/range geometry
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * ⭐ HYBRID-PROBLEM ANTI-FREEZE WORKSHEET
     * =================================================================================
     *
     * For a random hybrid, do NOT ask:
     *
     *      "Which known problem is this?"
     *
     * Fill this:
     *
     *      WHAT is being chosen?
     *          one anchor / every position / two endpoints
     *
     *      WHAT information is required?
     *          nearest / best / pair
     *
     *      WHAT do interior elements mean?
     *          constraint / contribution / irrelevant
     *
     *      WHEN does something become final?
     *          on pop / at current / after pair evaluation
     *
     *      WHAT can be forgotten forever?
     *          resolved candidate / raw history / useless endpoint
     *
     *      WHAT does answer update look like?
     *          finalize(old)
     *          sum += contribution(current)
     *          best = max(best, pair)
     *
     *
     * If two independent requirements survive,
     * the solution may need TWO mechanisms.
     *
     * Example:
     *
     *      "Choose two endpoints,
     *       but every interior element must be at least K."
     *
     * This contains:
     *
     *      pair selection
     *      +
     *      interior range constraint
     *
     * Do not force pure Container logic.
     *
     *
     * FINAL ANTI-FREEZE QUESTIONS:
     *
     *      1. Am I asking for NEAREST, BEST, or PAIR?
     *
     *      2. What exact proof lets me forget a candidate forever?
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 7️⃣ MENTAL MODEL + CORE INVARIANT
     * =================================================================================
     *
     * Stack = unresolved bar indices.
     *
     * Every index still inside is waiting for:
     *
     *      its first smaller bar on the RIGHT.
     *
     * Heights are non-decreasing from bottom -> top.
     *
     *
     * POP EVENT:
     *
     *      currentHeight < heights[stack.peek()]
     *
     * means:
     *
     *      current is the first smaller bar on the right
     *      for the popped bar.
     *
     * After pop:
     *
     *      new stack top = nearest surviving smaller bar on the left
     *
     * Therefore one pop reveals BOTH boundaries needed for area.
     *
     *
     * RECALL:
     *
     *      CURRENT SMALLER
     *          -> POP TALLER
     *          -> FINALIZE ITS AREA
     *
     * =================================================================================
     */


    /**
     * =================================================================================
     * 8️⃣ REUSABLE POP-TIME BOUNDARY SKELETON
     * =================================================================================
     *
     * Deque<Integer> stack = new ArrayDeque<>();
     *
     * for (each current index) {
     *
     *     while (!stack.isEmpty()
     *             && current breaks invariant with stack top) {
     *
     *         int index = stack.pop();
     *
     *         int right = currentIndex;
     *         int left  = stack.isEmpty()
     *                 ? -1
     *                 : stack.peek();
     *
     *         // finalize popped element
     *     }
     *
     *     stack.push(currentIndex);
     * }
     *
     *
     * Customize:
     *
     *      1. Increasing or decreasing stack?
     *      2. What comparison triggers pop?
     *      3. What does popped index represent?
     *      4. What formula uses left/right boundaries?
     *      5. Is a final flush/sentinel needed?
     *
     *
     * THIS PROBLEM:
     *
     *      stack order  -> non-decreasing heights
     *
     *      resolve when -> currentHeight < topHeight
     *
     *      right        -> current index
     *
     *      left         -> new top after pop, or -1
     *
     *      width        -> right - left - 1
     *
     *      contribution -> height * width
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 9️⃣ FULL STATE-EVOLUTION DRY RUN
     * =================================================================================
     *
     * heights = [2,1,5,6,2,3]
     *
     * stack shows indices; values are shown beside them.
     *
     * -------------------------------------------------------------------------
     * i=0, current=2
     *
     * push 0
     *
     * stack:
     *
     *      [0]
     *       2
     *
     * best = 0
     *
     * -------------------------------------------------------------------------
     * i=1, current=1
     *
     * 1 < 2
     *
     * pop index 0, height=2
     *
     *      right = 1
     *      left  = -1
     *      width = 1 - (-1) - 1 = 1
     *      area  = 2 * 1 = 2
     *
     * push 1
     *
     * stack:
     *
     *      [1]
     *       1
     *
     * best = 2
     *
     * -------------------------------------------------------------------------
     * i=2, current=5
     *
     * 5 < 1 ? NO
     *
     * push 2
     *
     * stack values:
     *
     *      [1,5]
     *
     * -------------------------------------------------------------------------
     * i=3, current=6
     *
     * 6 < 5 ? NO
     *
     * push 3
     *
     * stack values:
     *
     *      [1,5,6]
     *
     * -------------------------------------------------------------------------
     * i=4, current=2
     *
     * 2 < 6
     *
     * pop height 6:
     *
     *      right = 4
     *      left  = 2
     *      width = 4 - 2 - 1 = 1
     *      area  = 6
     *
     * stack values:
     *
     *      [1,5]
     *
     * 2 < 5
     *
     * pop height 5:
     *
     *      right = 4
     *      left  = 1
     *      width = 4 - 1 - 1 = 2
     *      area  = 10
     *
     * stack values:
     *
     *      [1]
     *
     * 2 < 1 ? NO
     *
     * push 4
     *
     * stack values:
     *
     *      [1,2]
     *
     * best = 10
     *
     * -------------------------------------------------------------------------
     * i=5, current=3
     *
     * push 5
     *
     * stack values:
     *
     *      [1,2,3]
     *
     * -------------------------------------------------------------------------
     * i=6, virtual current=0
     *
     * final smaller bar forces all remaining bars to close.
     *
     * Pop 3, then 2, then 1.
     *
     * No area beats 10.
     *
     * answer = 10
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 🔟 FOCUSED HARD PART — WHY WIDTH = RIGHT - LEFT - 1
     * =================================================================================
     *
     * Suppose popped bar owns:
     *
     *      leftBoundary  = 1
     *      rightBoundary = 4
     *
     * Both boundaries are SMALLER, so neither can be included.
     *
     * Valid indices:
     *
     *      2, 3
     *
     * Visual:
     *
     *      L | valid valid | R
     *      1 |   2     3   | 4
     *
     * Count:
     *
     *      4 - 1 - 1 = 2
     *
     * General:
     *
     *      width = right - left - 1
     *
     *
     * POP semantics:
     *
     *      right = current index that caused pop
     *
     *      left  = new stack top AFTER pop
     *
     * The order matters:
     *
     *      POP first
     *      THEN read left boundary.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣1️⃣ HIGH-ROI NUANCES
     * =================================================================================
     *
     * 1. WHY ONE EXTRA ITERATION?
     *
     * Bars surviving until the end never meet a smaller bar.
     *
     * So:
     *
     *      i == n -> currentHeight = 0
     *
     * acts as a virtual final bar and flushes taller bars.
     *
     * We do NOT push virtual index n.
     *
     *
     * 2. DUPLICATES
     *
     * This one-pass version pops only when:
     *
     *      currentHeight < topHeight
     *
     * Equal heights may coexist.
     *
     * Therefore stack heights are:
     *
     *      non-decreasing
     *
     * not strictly increasing.
     *
     *
     * 3. WHY INDICES, NOT JUST HEIGHTS?
     *
     * Width needs positions:
     *
     *      right - left - 1
     *
     * So the stack stores indices.
     *
     *
     * 4. THE DEBUGGING QUESTION
     *
     * Never trust stack.peek() merely because "this is a stack problem."
     *
     * Ask:
     *
     *      "What exactly does stack.peek() mathematically guarantee?"
     *
     * Here:
     *
     *      after pop, it is the nearest surviving smaller boundary on the left.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣2️⃣ CORRECTNESS + COMPLEXITY
     * =================================================================================
     *
     * Correctness:
     *
     * While an index remains in the stack, no smaller bar has appeared
     * to its right yet.
     *
     * When currentHeight becomes smaller than the top bar:
     *
     *      current index is that bar's first smaller boundary on the right.
     *
     * Because the stack is monotonic, after the bar is popped:
     *
     *      the new top is its nearest surviving smaller boundary on the left,
     *      or -1 if none exists.
     *
     * Therefore:
     *
     *      right - left - 1
     *
     * is exactly the maximum width where the popped height remains valid.
     *
     * We evaluate that maximal rectangle for every bar when it is finalized,
     * so the maximum computed area is the global answer.
     *
     *
     * Complexity:
     *
     *      each real index is pushed once
     *      each real index is popped at most once
     *
     *      Time  = O(n)
     *      Space = O(n)
     *
     * The nested while loop does NOT make the algorithm O(n²).
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣3️⃣ SAME-FAMILY VARIANTS
     * =================================================================================
     *
     * Maximal Rectangle:
     *
     *      each matrix row becomes histogram heights
     *      then reuse Largest Rectangle.
     *
     *
     * Sum of Subarray Minimums:
     *
     *      same previous/next boundary engine
     *
     *      but instead of:
     *
     *          height * width
     *
     *      use:
     *
     *          value
     *          * choicesOnLeft
     *          * choicesOnRight
     *
     *
     * Next Smaller Element:
     *
     *      same unresolved-boundary idea
     *      simpler output: just return the boundary value/index.
     *
     *
     * Daily Temperatures / Next Greater:
     *
     *      comparison direction flips
     *      current resolves previous unresolved elements.
     *
     * Keep their full implementations in their own mastery files.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣4️⃣ ±Δ — SAME PATTERN VS PATTERN BREAK
     * =================================================================================
     *
     * +Δ SAME CORE
     *
     * Need nearest GREATER boundaries instead:
     *
     *      flip monotonic direction/comparison.
     *
     *
     * Need contribution count instead of rectangle area:
     *
     *      boundaries survive
     *      payload/formula changes.
     *
     *
     * Histogram comes from each row of a binary matrix:
     *
     *      preprocess row into heights
     *      core histogram solver survives.
     *
     *
     * -------------------------------------------------------------------------
     * -Δ PATTERN BREAKS
     * -------------------------------------------------------------------------
     *
     * Container With Most Water:
     *
     *      rectangle uses two chosen endpoints
     *      not one bar's maximal nearest-smaller span
     *      -> two pointers.
     *
     *
     * Trapping Rain Water:
     *
     *      need bounded water from left/right heights
     *      not "where does this bar remain the minimum?"
     *
     *
     * Sliding Window Maximum:
     *
     *      candidates expire because of window age
     *      -> monotonic deque.
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
     *      area = height * widest span
     *
     *      widest span for bar
     *          -> first smaller left/right
     *
     *      current smaller closes previous taller bars
     *          -> monotonic stack
     *
     *      on pop:
     *
     *          right = current
     *          left  = new top or -1
     *          width = right - left - 1
     *          best  = max(best, height * width)
     *
     *      final virtual 0 flushes survivors
     *
     *
     * Interview sentence:
     *
     *      "I keep indices in non-decreasing height order.
     *       A smaller current bar means taller bars on top cannot extend
     *       any farther right, so I pop them. Current gives the right
     *       boundary, and the new stack top gives the left boundary.
     *       That lets me finalize each popped bar's maximal rectangle.
     *       Every index is pushed and popped at most once, so it is O(n)."
     *
     * =================================================================================
     */


    // =================================================================================
    // 1️⃣6️⃣ SELF-VERIFYING TESTS
    // =================================================================================

    public static void main(String[] args) {

        OptimalMonotonicStackSolution solver =
                new OptimalMonotonicStackSolution();

        assertEquals(
                10,
                solver.largestRectangleArea(
                        new int[]{2, 1, 5, 6, 2, 3}
                ),
                "classic histogram"
        );

        assertEquals(
                4,
                solver.largestRectangleArea(
                        new int[]{1, 2, 3}
                ),
                "increasing"
        );

        assertEquals(
                4,
                solver.largestRectangleArea(
                        new int[]{3, 2, 1}
                ),
                "decreasing"
        );

        assertEquals(
                8,
                solver.largestRectangleArea(
                        new int[]{2, 2, 2, 2}
                ),
                "duplicates"
        );

        assertEquals(
                3,
                solver.largestRectangleArea(
                        new int[]{2, 1, 2}
                ),
                "valley"
        );

        assertEquals(
                12,
                solver.largestRectangleArea(
                        new int[]{6, 2, 5, 4, 5, 1, 6}
                ),
                "mixed"
        );

        assertEquals(
                0,
                solver.largestRectangleArea(
                        new int[]{0, 0, 0}
                ),
                "zero heights"
        );

        assertEquals(
                5,
                solver.largestRectangleArea(
                        new int[]{5}
                ),
                "single bar"
        );

        System.out.println("ALL TESTS PASSED");
    }

    private static void assertEquals(
            int expected,
            int actual,
            String name) {

        if (expected != actual) {
            throw new AssertionError(
                    name
                            + " expected=" + expected
                            + " actual=" + actual
            );
        }
    }
}
