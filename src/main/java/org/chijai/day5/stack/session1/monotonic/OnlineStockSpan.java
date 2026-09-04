package org.chijai.day5.stack.session1.monotonic;

import java.util.*;

/**
 * =====================================================================================
 * ONLINE STOCK SPAN — RECONSTRUCTION-FIRST STUDY FILE
 * =====================================================================================
 *
 * LeetCode 901 — Online Stock Span
 *
 * Pattern:
 *   Monotonic decreasing stack + span compression
 *
 * Stack entry:
 *   (price, span)
 *
 * Core implementation intentionally preserved.
 * =====================================================================================
 */
public class OnlineStockSpan {

    /*
     * =================================================================================
     * 1️⃣ PROBLEM STATEMENT
     * =================================================================================
     *
     * Stock prices arrive ONE DAY AT A TIME.
     *
     * For each new price, return its span:
     *
     *   the maximum number of CONSECUTIVE days ending today
     *   for which every day's price was <= today's price.
     *
     * Think mechanically:
     *
     *   start at today
     *   move backward while previous price <= today's price
     *   stop at the first STRICTLY GREATER price
     *
     * ------------------------------------------------------------
     * Example
     * ------------------------------------------------------------
     *
     * Prices:
     *   [100, 80, 60, 70, 60, 75, 85]
     *
     * Spans:
     *   [  1,  1,  1,  2,  1,  4,  6]
     *
     * Why:
     *
     *   100 -> [100]                       => 1
     *
     *    80 -> [80]                        => 1
     *          100 > 80, stop
     *
     *    60 -> [60]                        => 1
     *           80 > 60, stop
     *
     *    70 -> [60, 70]                    => 2
     *           80 > 70, stop
     *
     *    60 -> [60]                        => 1
     *           70 > 60, stop
     *
     *    75 -> [60, 70, 60, 75]            => 4
     *           80 > 75, stop
     *
     *    85 -> [80, 60, 70, 60, 75, 85]    => 6
     *          100 > 85, stop
     *
     * ------------------------------------------------------------
     * IMPORTANT: "CONSECUTIVE"
     * ------------------------------------------------------------
     *
     * Example:
     *
     *   history = [7, 34, 1, 2]
     *   today   = 8
     *
     * Going backward:
     *
     *   2 <= 8   -> include
     *   1 <= 8   -> include
     *   34 > 8   -> STOP
     *
     * span = 3
     *
     * Even though 7 <= 8, we cannot jump across 34.
     * The greater value is a hard barrier.
     * =================================================================================
     */

    /*
     * =================================================================================
     * 2️⃣ HOW THE BRAIN SHOULD THINK
     * =================================================================================
     *
     * Natural brute-force thought:
     *
     *   For each new price, scan backward until a greater price appears.
     *
     * Correct — but increasing prices make this O(n^2):
     *
     *   10, 20, 30, 40, 50, ...
     *
     * New question:
     *
     *   If an earlier price already proved that several consecutive days
     *   behind it are <= it, can we reuse that information?
     *
     * Yes.
     *
     * Instead of storing only price, store:
     *
     *   (price, span)
     *
     * Example:
     *
     *   (75, 4)
     *
     * means:
     *
     *   75 represents a block of 4 consecutive days ending at 75,
     *   and every day in that block is <= 75.
     *
     * Therefore, if today = 85:
     *
     *   75 <= 85
     *
     * so ALL 4 represented days are also <= 85.
     *
     * We can add the whole block at once:
     *
     *   span += 4
     *
     * This is SPAN COMPRESSION.
     * =================================================================================
     */

    /*
     * =================================================================================
     * 3️⃣ CORE INVARIANT
     * =================================================================================
     *
     * Stack prices are STRICTLY DECREASING from bottom -> top.
     *
     * Why?
     *
     * Before pushing today's price P, we remove every top entry satisfying:
     *
     *   top.price <= P
     *
     * So when the loop ends:
     *
     *   stack is empty
     *   OR
     *   top.price > P
     *
     * Then we push P, preserving strict decrease.
     *
     * Equality matters:
     *
     *   equal prices are absorbed too,
     *   because the problem says previous prices <= today count.
     *
     * Example:
     *
     *   100, 100, 100
     *
     * spans:
     *
     *   1, 2, 3
     * =================================================================================
     */

    /*
     * =================================================================================
     * 4️⃣ PRIMARY IMPLEMENTATION
     * =================================================================================
     *
     * Mechanical reconstruction:
     *
     *   1. today counts itself -> span = 1
     *   2. while top.price <= today:
     *          absorb top.span
     *   3. push (today, span)
     *   4. return span
     * =================================================================================
     */
    static class StockSpanner {

        private record PriceSpan(int price, int span) {}

        private final Deque<PriceSpan> stack = new ArrayDeque<>();

        public int next(int price) {
            int span = 1; // today always counts

            while (!stack.isEmpty() && stack.peek().price() <= price) {
                span += stack.pop().span();
            }

            stack.push(new PriceSpan(price, span));
            return span;
        }
    }


    /*
     * =================================================================================
     * WHY span += stack.pop().span() DOES NOT DOUBLE-COUNT
     * =================================================================================
     *
     * Key invariant:
     *
     *   EACH (price, span) OWNS ONE DISJOINT CONSECUTIVE BLOCK OF DAYS.
     *
     * The stack is a PARTITION of history, not overlapping summaries.
     *
     * Example after processing:
     *
     *   100, 80, 60, 70, 60
     *
     * Stack:
     *
     *   (100,1)   (80,1)   (70,2)   (60,1)
     *
     * Visual ownership:
     *
     *   100   |   80   |   60 70   |   60
     *    ^         ^          ^           ^
     *    |         |          |           |
     * (100,1)   (80,1)     (70,2)      (60,1)
     *
     * Notice:
     *
     *   (70,2) owns [60,70].
     *   The old (60,1) that helped create it was POPPED and no longer exists.
     *
     * ------------------------------------------------------------
     * NOW 75 ARRIVES
     * ------------------------------------------------------------
     *
     * Start:
     *
     *   span = 1
     *
     *   [75]
     *
     * Current stack blocks:
     *
     *   [100]   [80]   [60 70]   [60]
     *
     * Loop 1:
     *
     *   pop (60,1)
     *
     *   span = 1 + 1 = 2
     *
     *   merged block:
     *
     *   [60 75]
     *
     * Loop 2:
     *
     *   pop (70,2)
     *
     *   span = 2 + 2 = 4
     *
     *   merged block:
     *
     *   [60 70] + [60] + [75]
     *
     *   = [60 70 60 75]
     *
     * Then 80 > 75, so stop.
     *
     * Push:
     *
     *   (75,4)
     *
     * New partition:
     *
     *   [100]   [80]   [60 70 60 75]
     *     ^       ^            ^
     *     |       |            |
     * (100,1)  (80,1)       (75,4)
     *
     * No day appears in two live blocks.
     *
     * ------------------------------------------------------------
     * WHY pop() MATTERS
     * ------------------------------------------------------------
     *
     * This line:
     *
     *   span += stack.pop().span();
     *                 ^^^^^
     *
     * means:
     *
     *   1. take ownership of the whole old block
     *   2. add its size
     *   3. DELETE the old owner
     *
     * So ownership TRANSFERS; it does not duplicate.
     *
     * Think of boxes:
     *
     *   Box A = 1 day
     *   Box B = 2 different days
     *   Today = 1 day
     *
     * Merge:
     *
     *   1 + 2 + 1 = 4
     *
     * Then destroy A and B.
     *
     * Only the merged box survives.
     *
     * RECALL:
     *
     *   "ADD THE WHOLE BOX, THEN DELETE THE OLD BOX."
     *
     * =================================================================================
     */

    /*
     * =================================================================================
     * 5️⃣ FULL STATE-EVOLUTION DRY RUN
     * =================================================================================
     *
     * Input:
     *   [100, 80, 60, 70, 60, 75, 85]
     *
     * Stack shown bottom -> top.
     * Entry = (price, span)
     *
     * ---------------------------------------------------------------------------------
     * Day | Price | Work                               | Stack after              | Ans
     * ---------------------------------------------------------------------------------
     *  1  | 100   | nothing to pop                     | (100,1)                  | 1
     *  2  |  80   | 100 > 80 -> barrier                | (100,1)(80,1)            | 1
     *  3  |  60   | 80 > 60 -> barrier                 | (100,1)(80,1)(60,1)      | 1
     *  4  |  70   | pop (60,1), span=2                 | (100,1)(80,1)(70,2)      | 2
     *  5  |  60   | 70 > 60 -> barrier                 | ... (70,2)(60,1)         | 1
     *  6  |  75   | pop (60,1) -> span=2               |                           |
     *     |       | pop (70,2) -> span=4               | (100,1)(80,1)(75,4)      | 4
     *  7  |  85   | pop (75,4) -> span=5               |                           |
     *     |       | pop (80,1) -> span=6               | (100,1)(85,6)            | 6
     *     |       | 100 > 85 -> barrier                |                           |
     * ---------------------------------------------------------------------------------
     *
     * Final spans:
     *   [1, 1, 1, 2, 1, 4, 6]
     * =================================================================================
     */

    /*
     * =================================================================================
     * 6️⃣ FOCUSED TRACE — WHY span += popped.span
     * =================================================================================
     *
     * Focus on today = 75.
     *
     * Before 75:
     *
     *   bottom -> top
     *   (100,1), (80,1), (70,2), (60,1)
     *
     * Start:
     *   span = 1
     *
     * Step 1:
     *   top = (60,1)
     *   60 <= 75
     *   span = 1 + 1 = 2
     *   pop (60,1)
     *
     * Step 2:
     *   top = (70,2)
     *   70 <= 75
     *
     *   (70,2) already represents two valid consecutive days:
     *   [60, 70]
     *
     *   Since 70 <= 75, both are automatically <= 75.
     *
     *   span = 2 + 2 = 4
     *   pop (70,2)
     *
     * Step 3:
     *   top = (80,1)
     *   80 > 75
     *   STOP
     *
     * Push:
     *   (75,4)
     *
     * Answer:
     *   4
     * =================================================================================
     */

    /*
     * =================================================================================
     * 7️⃣ EQUALITY DRY RUN — [7, 2, 1, 2]
     * =================================================================================
     *
     * Day 1: 7
     *   stack = (7,1)
     *   span = 1
     *
     * Day 2: 2
     *   7 > 2 -> stop
     *   stack = (7,1)(2,1)
     *   span = 1
     *
     * Day 3: 1
     *   2 > 1 -> stop
     *   stack = (7,1)(2,1)(1,1)
     *   span = 1
     *
     * Day 4: 2
     *
     *   start span = 1
     *
     *   pop (1,1):
     *      1 <= 2
     *      span = 2
     *
     *   pop (2,1):
     *      2 <= 2    <-- EQUALITY COUNTS
     *      span = 3
     *
     *   next top = 7
     *      7 > 2 -> stop
     *
     *   push (2,3)
     *
     * Final stack:
     *   (7,1)(2,3)
     *
     * Final answer for last 2:
     *   3
     * =================================================================================
     */

    /*
     * =================================================================================
     * 8️⃣ WHY POPPED ENTRIES NEVER NEED TO RETURN
     * =================================================================================
     *
     * Suppose today = 75 and we pop (70,2).
     *
     * 75 is:
     *   newer than 70
     *   >= 70
     *   and now represents everything 70 represented
     *
     * So 70 is dominated.
     *
     * Any future price that can cross 75 can certainly cross 70 too.
     * Therefore 70 never needs to be a separate candidate again.
     * =================================================================================
     */

    /*
     * =================================================================================
     * 9️⃣ COMPLEXITY — HOW TO DEFEND AMORTIZED O(1)
     * =================================================================================
     *
     * One next() call can pop many entries.
     * So one call can be O(n).
     *
     * Example:
     *   100, 90, 80, 70, 60, 200
     *
     * When 200 arrives, it may pop almost everything.
     *
     * But across ALL n calls:
     *
     *   each entry is pushed exactly once
     *   each entry is popped at most once
     *
     * Therefore:
     *
     *   total pushes <= n
     *   total pops   <= n
     *   total work   = O(n)
     *
     * Hence:
     *
     *   worst-case one call = O(n)
     *   amortized next()    = O(1)
     *   total n calls       = O(n)
     *   space               = O(n)
     *
     * Interview proof:
     *
     *   "Although one call may pop many entries, each entry is pushed once and
     *    popped at most once over its lifetime, so total work across n calls is O(n)."
     * =================================================================================
     */

    /*
     * =================================================================================
     * 🔟 HIGH-ROI TRAPS
     * =================================================================================
     *
     * TRAP 1: using < instead of <=
     *
     *   Equal prices count in the span.
     *
     * TRAP 2: span++ instead of span += popped.span
     *
     *   A popped entry represents an entire compressed block, not one day.
     *
     * TRAP 3: forgetting "consecutive"
     *
     *   First strictly greater price is a hard barrier.
     *
     * TRAP 4: claiming every call is worst-case O(1)
     *
     *   Correct statement is amortized O(1).
     * =================================================================================
     */

    /*
     * =================================================================================
     * 1️⃣1️⃣ 30-SECOND RECALL CARD
     * =================================================================================
     *
     * ONLINE STOCK SPAN
     *
     * Ask:
     *   "How many consecutive days backward are <= today?"
     *
     * Store:
     *   (price, compressedSpan)
     *
     * Invariant:
     *   prices strictly decreasing bottom -> top
     *
     * For today:
     *
     *   span = 1
     *
     *   while top.price <= today:
     *       span += top.span
     *       pop
     *
     *   push(today, span)
     *
     * Memory hook:
     *   "Today's bigger/equal price EATS smaller/equal previous blocks."
     * =================================================================================
     */

    /*
     * =================================================================================
     * 1️⃣2️⃣ INTERVIEW ARTICULATION
     * =================================================================================
     *
     * "The brute-force approach scans backward from every new day until it finds
     *  a greater price, which can become O(n^2).
     *
     *  I compress already-resolved consecutive blocks using a monotonic decreasing
     *  stack of (price, span).
     *
     *  Today's span starts at 1. While the stack top price is <= today's price,
     *  I pop it and add its entire compressed span, because every day represented
     *  by that entry is also <= today's price.
     *
     *  When the loop stops, the top is the first greater barrier. Then I push
     *  today's (price, span).
     *
     *  Each entry is pushed once and popped at most once, so total work across
     *  n calls is O(n), giving amortized O(1) per next() call."
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣3️⃣ WHY THIS IS NOT LIS — THE MOST IMPORTANT CLASSIFICATION TRAP
     * =================================================================================
     *
     * It is completely natural to see:
     *
     *   "previous prices <= today's price"
     *
     * and think:
     *
     *   "This sounds like Longest Increasing Subsequence."
     *
     * But the decisive word in Stock Span is:
     *
     *   CONSECUTIVE
     *
     * ------------------------------------------------------------
     * LIS
     * ------------------------------------------------------------
     *
     * You may SKIP elements.
     *
     * Example:
     *
     *   [3, 100, 4, 5, 6]
     *
     * LIS can choose:
     *
     *   3, 4, 5, 6
     *
     * It jumps over 100.
     *
     * So 100 is NOT a physical barrier.
     *
     * Typical question:
     *
     *   "What is the best subsequence ending here / overall?"
     *
     * Typical tools:
     *
     *   DP
     *   or
     *   patience sorting + binary search
     *
     * ------------------------------------------------------------
     * STOCK SPAN
     * ------------------------------------------------------------
     *
     * You may NOT skip.
     *
     * Example:
     *
     *   [3, 100, 4, 5, 6]
     *                 ^
     *               today
     *
     * Going backward from 6:
     *
     *   5 <= 6  -> include
     *   4 <= 6  -> include
     *   100 > 6 -> STOP
     *
     * You CANNOT jump over 100 and include 3.
     *
     * 100 is a HARD BARRIER.
     *
     * ------------------------------------------------------------
     * MASTER DISCRIMINATOR
     * ------------------------------------------------------------
     *
     * Ask:
     *
     *   "Can I skip elements?"
     *
     * YES
     *   -> subsequence territory
     *   -> DP / greedy / binary search may apply
     *
     * NO
     *   -> contiguous / consecutive / boundary territory
     *   -> ask what stops the range
     *
     * For Stock Span:
     *
     *   first STRICTLY GREATER value stops the range
     *
     * That is the monotonic-stack clue.
     *
     * ------------------------------------------------------------
     * WHY A STACK EMERGES NATURALLY
     * ------------------------------------------------------------
     *
     * Start from brute force:
     *
     *   for today:
     *       walk left
     *       consume smaller/equal prices
     *       stop at first greater
     *
     * Now ask:
     *
     *   "If today defeats a smaller/equal previous price,
     *    can that previous price ever independently block today?"
     *
     * No.
     *
     * It is dominated.
     *
     * So:
     *
     *   pop dominated smaller/equal candidates
     *   keep only greater barriers that may still matter
     *
     * That is exactly what a monotonic stack does:
     *
     *   keep only unresolved / undominated boundary candidates.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣4️⃣ PATTERN-DERIVATION DECISION TREE
     * =================================================================================
     *
     * Do NOT begin with:
     *
     *   "Which pattern name does this remind me of?"
     *
     * Begin with the relationship the problem asks for.
     *
     *
     *                    Compare current with other elements
     *                               |
     *                               v
     *                      Can I SKIP elements?
     *                         /           \
     *                       YES            NO
     *                        |              |
     *                        v              v
     *                  SUBSEQUENCE      CONTIGUOUS /
     *                   territory       BOUNDARY
     *                        |              |
     *                DP / greedy /          v
     *                binary search     What determines
     *                                  where I stop?
     *                                      |
     *                    +-----------------+------------------+
     *                    |                                    |
     *                    v                                    v
     *          first greater/smaller                    validity of whole
     *             value is barrier                       moving range
     *                    |                                    |
     *                    v                                    v
     *             MONOTONIC STACK                     SLIDING WINDOW /
     *                                                 MONOTONIC DEQUE
     *
     *
     * Another useful branch:
     *
     *   "Am I accumulating the best answer ending at i,
     *    rather than searching for a boundary?"
     *
     * YES
     *   -> DP / Kadane-style state may be natural.
     *
     * ------------------------------------------------------------
     * STOCK SPAN PATH
     * ------------------------------------------------------------
     *
     * Can skip?
     *   NO
     *
     * What stops me?
     *   first previous STRICTLY GREATER price
     *
     * Do smaller/equal previous values become dominated?
     *   YES
     *
     * Therefore:
     *
     *   MONOTONIC DECREASING STACK
     *
     * Need count/distance across already-resolved blocks?
     *   YES
     *
     * Therefore store:
     *
     *   (price, compressedSpan)
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣5️⃣ HORIZONTAL MASTERY — CONFUSION & NEIGHBOR TABLE
     * =================================================================================
     *
     * Read this table horizontally.
     *
     * Goal:
     *
     *   not just "recognize Stock Span"
     *
     * but:
     *
     *   "know why neighboring-looking problems diverge into different patterns."
     *
     * ================================================================================================================
     * PROBLEM / WORDING              | WHY IT LOOKS SIMILAR       | DECISIVE QUESTION              | ACTUAL PATTERN
     * ================================================================================================================
     *
     * ONLINE STOCK SPAN              | compare today with past    | consecutive? first greater     | monotonic dec stack
     *                                | smaller/equal prices        | barrier? YES                   | + span compression
     *
     * LIS                            | earlier smaller values      | can skip elements? YES         | DP O(n^2) or
     *                                | contribute to longer answer |                               | binary search O(nlogn)
     *
     * LONGEST INCREASING             | "increasing" like LIS       | must remain adjacent? YES      | simple linear scan
     * CONTIGUOUS RUN                 |                             |                               |
     *
     * PREVIOUS GREATER ELEMENT       | same left-side barrier      | only need nearest barrier,     | monotonic dec stack
     *                                |                             | not compressed span            |
     *
     * DAILY TEMPERATURES             | compare prices/temps        | first FUTURE greater?          | monotonic dec stack
     *                                | across days                 | resolve older indices          | of unresolved indices
     *
     * NEXT GREATER ELEMENT           | same greater-boundary idea  | first greater to RIGHT?        | monotonic dec stack
     *
     * SLIDING WINDOW MAXIMUM         | maintain useful candidates  | fixed moving window causes     | monotonic DEQUE
     *                                |                             | expiry from left? YES          |
     *
     * LARGEST RECTANGLE              | nearest smaller barriers    | smaller bar finalizes range    | monotonic inc stack
     * IN HISTOGRAM                   |                             | of taller bar                  |
     *
     * SUM OF SUBARRAY MINIMUMS       | nearest smaller boundaries  | count ranges where each value  | monotonic stack
     *                                |                             | owns the minimum               | + contribution math
     *
     * REMOVE K DIGITS                | pop previous larger values  | objective is lexicographically | greedy +
     *                                | when smaller arrives        | smallest number with k pops    | monotonic stack
     *
     * MAXIMUM SUBARRAY               | asks about a contiguous     | first greater/smaller barrier? | Kadane DP / greedy
     *                                | region                      | NO; continue vs restart sum    |
     *
     * LONGEST VALID SUBARRAY /       | contiguous backward/forward | can left pointer move forward  | sliding window
     * WINDOW CONSTRAINT              | region                      | to restore validity?           |
     *
     * LONGEST CONSECUTIVE SEQUENCE   | word "consecutive"          | consecutive VALUES or adjacent | HashSet
     *                                | can mislead                 | POSITIONS? values              |
     *
     * MIN STACK                      | contains "stack" + min      | array relationship problem? NO | augmented stack
     *                                |                             | data-structure design          |
     *
     * ================================================================================================================
     *
     * ------------------------------------------------------------
     * SAME-LOOKING AND ACTUALLY SAME FAMILY
     * ------------------------------------------------------------
     *
     * 1. Stock Span
     * 2. Previous Greater Element
     * 3. Daily Temperatures
     * 4. Next Greater Element
     *
     * Shared deep idea:
     *
     *   keep only candidates that are still capable of being a useful
     *   greater boundary.
     *
     * Difference:
     *
     *   Stock Span
     *      -> look LEFT for previous greater
     *      -> answer is count/distance
     *      -> compress spans
     *
     *   Daily Temperatures / NGE
     *      -> future element resolves older unresolved elements
     *      -> often store indices
     *
     * ------------------------------------------------------------
     * SAME "MONOTONIC" WORD, DIFFERENT DIRECTION
     * ------------------------------------------------------------
     *
     * Need GREATER boundary:
     *
     *   maintain decreasing stack
     *
     * Need SMALLER boundary:
     *
     *   maintain increasing stack
     *
     * Memory anchor:
     *
     *   NEXT GREATER -> DECREASING candidates
     *   NEXT SMALLER -> INCREASING candidates
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣6️⃣ HORIZONTAL RECALL — FIVE QUESTIONS BEFORE CHOOSING A PATTERN
     * =================================================================================
     *
     * When a new problem looks vaguely like Stock Span / LIS / Window:
     *
     * 1. Can I SKIP elements?
     *
     * 2. Must the answer be CONTIGUOUS / CONSECUTIVE?
     *
     * 3. Is there a FIRST GREATER / SMALLER value that acts as a hard boundary?
     *
     * 4. When current defeats a previous candidate, can that candidate be
     *    permanently discarded as DOMINATED?
     *
     * 5. Do I need:
     *
     *      best subsequence state     -> DP
     *      nearest boundary           -> monotonic stack
     *      valid moving range         -> sliding window
     *      expiring max/min           -> monotonic deque
     *
     * For Stock Span:
     *
     *   skip?                 NO
     *   consecutive?          YES
     *   greater barrier?      YES
     *   dominated candidates? YES
     *
     * => MONOTONIC STACK.
     *
     * =================================================================================
     */



    /*
     * =================================================================================
     * 1️⃣7️⃣ ± DELTA HORIZONTAL MASTERY — HOW FAR CAN THE INVARIANT BEND?
     * =================================================================================
     *
     * ANCHOR — ONLINE STOCK SPAN
     *
     * Structural properties:
     *
     *   • online input
     *   • look backward
     *   • answer is consecutive
     *   • cannot skip
     *   • first strictly greater value is a hard barrier
     *   • smaller/equal candidates become dominated
     *   • need count/span, not merely the barrier
     *
     * Therefore:
     *
     *   monotonic decreasing stack
     *   + span compression
     *
     *
     * ---------------------------------------------------------------------------------
     * +Δ — SMALL CHANGE, SAME CORE PATTERN SURVIVES
     * ---------------------------------------------------------------------------------
     *
     * CHANGE:
     *   "Return the previous greater element."
     *
     * SAME:
     *   • look left
     *   • first greater is the barrier
     *   • smaller/equal values are dominated
     *
     * REMOVE:
     *   • span compression
     *
     * RESULT:
     *   still a monotonic decreasing stack
     *
     *
     * CHANGE:
     *   "How many days until a future warmer temperature?"
     *
     * CHANGED:
     *   • previous -> future
     *   • answer-now -> resolve older elements when future arrives
     *
     * SAME:
     *   • greater value defeats smaller unresolved values
     *
     * RESULT:
     *   monotonic decreasing stack of INDICES
     *   -> Daily Temperatures
     *
     *
     * CHANGE:
     *   "Return the next greater element."
     *
     * CHANGED:
     *   • left boundary -> right boundary
     *   • span/count -> first greater value
     *
     * SAME:
     *   • unresolved smaller candidates are popped when greater arrives
     *
     * RESULT:
     *   monotonic decreasing stack
     *
     *
     * CHANGE:
     *   "Need first smaller instead of first greater."
     *
     * CHANGED:
     *   • comparison polarity flips
     *
     * RESULT:
     *   monotonic decreasing -> monotonic increasing
     *
     *
     * CHANGE:
     *   "Need width/range between smaller boundaries."
     *
     * SAME FAMILY:
     *   • nearest-smaller boundary machinery
     *
     * EXTRA:
     *   • width / contribution math
     *
     * RESULT:
     *   Largest Rectangle / Sum of Subarray Minimums family
     *
     *
     * ---------------------------------------------------------------------------------
     * -Δ — SMALL CHANGE, ORIGINAL PATTERN BREAKS
     * ---------------------------------------------------------------------------------
     *
     * CHANGE:
     *   "Longest increasing SUBSEQUENCE."
     *
     * Critical delta:
     *
     *   Stock Span -> cannot skip
     *   LIS        -> CAN skip
     *
     * A greater value is no longer a hard barrier.
     *
     * Example:
     *
     *   [3, 100, 4, 5, 6]
     *
     * LIS can skip 100:
     *
     *   3, 4, 5, 6
     *
     * Stock Span cannot jump over 100.
     *
     * RESULT:
     *   monotonic-stack boundary reasoning dies
     *   -> DP / binary-search LIS
     *
     *
     * CHANGE:
     *   "Maximum in every fixed k-day window."
     *
     * Critical delta:
     *
     *   candidates can become invalid because they AGE OUT.
     *
     * A stack can remove only from one end.
     * A sliding window needs:
     *
     *   • remove dominated candidates from back
     *   • remove expired candidates from front
     *
     * RESULT:
     *   monotonic STACK is insufficient
     *   -> monotonic DEQUE
     *
     *
     * CHANGE:
     *   "Maximum-sum contiguous subarray."
     *
     * Critical delta:
     *
     *   no first-greater/smaller barrier determines the answer.
     *
     * Decision becomes:
     *
     *   continue previous sum
     *   OR
     *   restart at current element
     *
     * RESULT:
     *   -> Kadane / DP
     *
     *
     * CHANGE:
     *   "Longest consecutive sequence."
     *
     * Critical delta:
     *
     *   "consecutive" means consecutive VALUES,
     *   not consecutive POSITIONS in the original array.
     *
     * RESULT:
     *   -> HashSet
     *
     *
     * CHANGE:
     *   "Design a stack with getMin() in O(1)."
     *
     * Critical delta:
     *
     *   this is not a directional boundary problem over an array.
     *
     * RESULT:
     *   -> augmented stack / historical state preservation
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣8️⃣ HORIZONTAL MASTERY TABLE — SAME-LOOKING VS SAME-FAMILY
     * =================================================================================
     *
     * Read HORIZONTALLY.
     *
     * ================================================================================================================
     * PROBLEM / DELTA              | WHAT CHANGED                    | INVARIANT SURVIVES?      | RESULT
     * ================================================================================================================
     *
     * Stock Span                   | anchor                          | YES                      | dec stack + span
     *
     * Previous Greater             | output only                     | YES                      | dec stack
     *
     * Next Greater                 | left -> right                   | YES                      | dec stack
     *
     * Daily Temperatures           | future resolution + distance    | YES                      | dec stack + indices
     *
     * Equal prices                 | equality must count              | YES                      | use <=
     *
     * Next Smaller                 | greater -> smaller               | FAMILY survives          | inc stack
     *
     * Largest Rectangle            | boundary + width math            | FAMILY survives          | inc stack + width
     *
     * Sum Subarray Minimums        | boundary + contribution math     | FAMILY survives          | inc stack + math
     *
     * ---------------------------------------------------------------------------------------------------------------
     *
     * LIS                          | can SKIP elements                | NO                       | DP / binary search
     *
     * Increasing contiguous run    | only adjacent comparison         | NO                       | linear scan
     *
     * Sliding Window Maximum       | candidates EXPIRE by age         | NO for stack             | monotonic deque
     *
     * Maximum Subarray             | sum/restart decision             | NO                       | Kadane
     *
     * Longest Consecutive Sequence | consecutive VALUES               | NO                       | HashSet
     *
     * Min Stack                    | DS history, not array boundary    | NO                       | augmented stack
     *
     * ================================================================================================================
     *
     * KEY:
     *
     *   "Invariant survives"
     *      means the same core monotonic-stack skeleton still applies.
     *
     *   "Family survives"
     *      means monotonic-boundary thinking remains,
     *      but direction/comparison/output changes.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣9️⃣ RECONSTRUCTION DRILL — DO NOT MATCH BY KEYWORD
     * =================================================================================
     *
     * When an unseen problem resembles Stock Span:
     *
     * DO NOT ask:
     *
     *   "Which LeetCode problem does this remind me of?"
     *
     * Ask:
     *
     *   1. What changed from the anchor problem?
     *
     *      • direction?
     *      • skip permission?
     *      • contiguity?
     *      • greater/smaller boundary?
     *      • expiry?
     *      • output required?
     *      • online/offline?
     *
     *   2. Does the original invariant still survive?
     *
     *   3. If YES:
     *
     *      what changes in:
     *
     *      • stack payload?
     *      • comparison?
     *      • answer calculation?
     *
     *   4. If NO:
     *
     *      which exact property killed the pattern?
     *
     *
     * ---------------------------------------------------------------------------------
     * DRILL A — Stock Span -> Daily Temperatures
     * ---------------------------------------------------------------------------------
     *
     * Delta:
     *
     *   previous -> future
     *
     * Survives:
     *
     *   smaller unresolved values are defeated by a greater current value
     *
     * Changes:
     *
     *   store indices instead of compressed spans
     *
     * Conclusion:
     *
     *   SAME FAMILY
     *
     *
     * ---------------------------------------------------------------------------------
     * DRILL B — Stock Span -> LIS
     * ---------------------------------------------------------------------------------
     *
     * Delta:
     *
     *   consecutive -> subsequence
     *   cannot skip -> can skip
     *
     * Dies:
     *
     *   first-greater hard-barrier invariant
     *
     * Conclusion:
     *
     *   DIFFERENT PATTERN
     *
     *
     * ---------------------------------------------------------------------------------
     * DRILL C — Stock Span -> Sliding Window Maximum
     * ---------------------------------------------------------------------------------
     *
     * Delta:
     *
     *   fixed window introduces AGE EXPIRY
     *
     * Original stack cannot remove stale candidates from the opposite end.
     *
     * Conclusion:
     *
     *   STACK -> DEQUE
     *
     *
     * ---------------------------------------------------------------------------------
     * DRILL D — Stock Span -> Previous Greater Element
     * ---------------------------------------------------------------------------------
     *
     * Delta:
     *
     *   only answer form changes
     *
     * Invariant survives completely.
     *
     * Remove:
     *
     *   compressed span
     *
     * Conclusion:
     *
     *   SAME PATTERN, SIMPLER PAYLOAD
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 2️⃣0️⃣ FIVE-QUESTION PATTERN CLASSIFIER
     * =================================================================================
     *
     * Before choosing DP / stack / window / deque, ask:
     *
     * 1. Can I SKIP elements?
     *
     *    YES -> subsequence territory may be DP / greedy / binary search
     *
     *
     * 2. Must the answer remain CONTIGUOUS / CONSECUTIVE?
     *
     *    YES -> boundary/window reasoning becomes relevant
     *
     *
     * 3. Is there a FIRST GREATER / SMALLER value that acts as a hard boundary?
     *
     *    YES -> monotonic stack is a strong candidate
     *
     *
     * 4. When current defeats a previous candidate, can that candidate
     *    be permanently discarded as DOMINATED?
     *
     *    YES -> monotonic stack/deque compression is likely
     *
     *
     * 5. Can a candidate also become invalid merely because it gets TOO OLD?
     *
     *    YES -> likely need DEQUE, not just STACK
     *
     *
     * STOCK SPAN:
     *
     *   skip?                 NO
     *   consecutive?          YES
     *   greater barrier?      YES
     *   dominated candidates? YES
     *   age expiry?           NO
     *
     * Therefore:
     *
     *   MONOTONIC DECREASING STACK + SPAN COMPRESSION
     *
     * =================================================================================
     */


    private static void assertEquals(int expected, int actual, String name) {
        if (expected != actual) {
            throw new AssertionError(
                    name + " FAILED: expected=" + expected + ", actual=" + actual
            );
        }
        System.out.println("PASS: " + name + " -> " + actual);
    }

    private static void testLeetCodeExample() {
        System.out.println("\n=== Test 1: LeetCode Example ===");
        StockSpanner s = new StockSpanner();

        assertEquals(1, s.next(100), "100");
        assertEquals(1, s.next(80), "80");
        assertEquals(1, s.next(60), "60");
        assertEquals(2, s.next(70), "70");
        assertEquals(1, s.next(60), "60 again");
        assertEquals(4, s.next(75), "75");
        assertEquals(6, s.next(85), "85");
    }

    private static void testIncreasing() {
        System.out.println("\n=== Test 2: Increasing Prices ===");
        StockSpanner s = new StockSpanner();

        assertEquals(1, s.next(10), "10");
        assertEquals(2, s.next(20), "20");
        assertEquals(3, s.next(30), "30");
        assertEquals(4, s.next(40), "40");
        assertEquals(5, s.next(50), "50");
    }

    private static void testDecreasing() {
        System.out.println("\n=== Test 3: Decreasing Prices ===");
        StockSpanner s = new StockSpanner();

        assertEquals(1, s.next(50), "50");
        assertEquals(1, s.next(40), "40");
        assertEquals(1, s.next(30), "30");
        assertEquals(1, s.next(20), "20");
    }

    private static void testEqualPrices() {
        System.out.println("\n=== Test 4: Equal Prices ===");
        StockSpanner s = new StockSpanner();

        assertEquals(1, s.next(100), "first 100");
        assertEquals(2, s.next(100), "second 100");
        assertEquals(3, s.next(100), "third 100");
    }

    private static void testPromptExample() {
        System.out.println("\n=== Test 5: Prompt Example [7,2,1,2] ===");
        StockSpanner s = new StockSpanner();

        assertEquals(1, s.next(7), "7");
        assertEquals(1, s.next(2), "2");
        assertEquals(1, s.next(1), "1");
        assertEquals(3, s.next(2), "final 2");
    }

    private static void testGreaterBarrier() {
        System.out.println("\n=== Test 6: Greater Barrier ===");
        StockSpanner s = new StockSpanner();

        s.next(7);
        s.next(34);
        s.next(1);
        s.next(2);

        assertEquals(3, s.next(8), "8 stopped by 34");
    }

    private static void testNewMaximum() {
        System.out.println("\n=== Test 7: New Global Maximum ===");
        StockSpanner s = new StockSpanner();

        s.next(100);
        s.next(80);
        s.next(120);

        assertEquals(4, s.next(150), "150 spans entire history");
    }

    public static void main(String[] args) {
        testLeetCodeExample();
        testIncreasing();
        testDecreasing();
        testEqualPrices();
        testPromptExample();
        testGreaterBarrier();
        testNewMaximum();

        System.out.println("\nALL TESTS PASSED");
    }
}
