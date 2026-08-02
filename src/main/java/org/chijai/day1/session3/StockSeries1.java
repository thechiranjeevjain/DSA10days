package org.chijai.day1.session3;

import java.util.Arrays;

/**
 * ============================================================================
 * StockSeries1
 * ============================================================================
 *
 * Covers:
 *
 * LC 121 - Best Time to Buy and Sell Stock
 * LC 122 - Best Time to Buy and Sell Stock II
 * LC 123 - Best Time to Buy and Sell Stock III
 *
 * Theme:
 *
 *      One family.
 *      One evolving invariant.
 *      One evolving state machine.
 *
 * Instead of memorizing three independent problems, this chapter derives each
 * from the previous one.
 *
 * ============================================================================
 */
public class StockSeries1 {

    /*
    ============================================================================
    ██████╗  █████╗ ██████╗ ████████╗     ██╗
    ██╔══██╗██╔══██╗██╔══██╗╚══██╔══╝    ███║
    ██████╔╝███████║██████╔╝   ██║        ╚██║
    ██╔═══╝ ██╔══██║██╔══██╗   ██║         ██║
    ██║     ██║  ██║██║  ██║   ██║         ██║
    ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝         ╚═╝
    ============================================================================

    LC 121

    Best Time to Buy and Sell Stock
    Difficulty : Easy

    Pattern:
        Running Minimum
        Online Dynamic Programming
        One Pass Optimization

    Tags:
        Array
        DP
        Greedy
        Kadane Relation
    */

    /*
    ============================================================================
    📘 PRIMARY PROBLEM
    ============================================================================

    Problem

    You are given an integer array prices.

    prices[i] represents the stock price on day i.

    You may perform exactly one transaction:

        Buy once
        Sell once later

    Buying and selling on the same day is allowed only if profit is zero.

    Selling before buying is forbidden.

    Return the maximum obtainable profit.

    If no profitable transaction exists,
    return 0.

    Constraints

        1 <= prices.length <= 100000

        0 <= prices[i] <= 10000

    Examples

    Example 1

    prices = [7,1,5,3,6,4]

    Buy  at 1

    Sell at 6

    Profit = 5

    Answer = 5


    Example 2

    prices = [7,6,4,3,1]

    Prices only decrease.

    Best action:

        Never trade.

    Answer = 0


    LeetCode

    https://leetcode.com/problems/best-time-to-buy-and-sell-stock/

    ============================================================================
    🔵 CORE PATTERN OVERVIEW
    ============================================================================

    Pattern

        Running Minimum

    Archetype

        Online Optimization

    Search Space

        Every possible buying day before current day.

    State

        Current minimum buying price seen so far.

    Transition

        Current profit

            =
            todayPrice
            -
            minimumBuySeen

    Core Invariant

        Before processing day i,

        minBuy

        equals

        the minimum stock price among all previous days
        including current day after update.

    Therefore

        every legal transaction ending today

        is evaluated exactly once.

    Why It Works

        Every optimal transaction has exactly

            one buying day
            one selling day

        While scanning left to right,

        buying days already belong to history.

        Therefore history can be compressed into

            one number

                minimum price.

    Recognition Signals

        Exactly one transaction.

        Buy must happen before sell.

        Need maximum difference.

        Left element must precede right element.

    When To Use

        Running minimum

        Running maximum

        Prefix optimum

        Online processing

    Do NOT Use

        Multiple transactions

        Cooldown

        Transaction fee

        Limited k transactions

    Pattern Comparison

    ----------------------------------------------------------------

    Pattern

        Prefix Minimum

    Stores

        Best buy so far

    Suitable

        Single transaction

    ----------------------------------------------------------------

    Pattern

        Kadane

    Stores

        Best subarray ending here

    Suitable

        Difference array

    ----------------------------------------------------------------

    Pattern

        DP States

    Stores

        Buy/Sell states

    Suitable

        Multiple transactions

    ----------------------------------------------------------------

    ============================================================================
    🟢 MENTAL MODEL
    ============================================================================

    Imagine walking through time.

    You cannot return.

    Every day asks one question.

        "If today were the selling day,
         what was the cheapest legal buying day?"

    You never need all previous prices.

    You only need

        the cheapest one.

    Everything else is dominated forever.

    This is why the algorithm uses O(1) memory.

    ============================================================================
    🟢 CORE INVARIANTS
    ============================================================================

    Invariant 1

        minBuy

        always equals

        minimum price seen so far.

    ------------------------------

    Invariant 2

        maxProfit

        always equals

        maximum legal profit found so far.

    ------------------------------

    Invariant 3

        Selling always happens today.

        Buying always belongs to history.

    ------------------------------

    Invariant 4

        Illegal transactions

            sell before buy

        are impossible because history is processed first.

    ------------------------------

    Variable Meaning

        minBuy

            Cheapest buying opportunity.

        maxProfit

            Best completed transaction.

        price

            Current selling candidate.

    ------------------------------

    Allowed Transition

        Sell today.

        Update answer.

        Update running minimum.

    ------------------------------

    Forbidden Transition

        Update minimum after pretending we bought
        in the future.

        That would violate chronological order.

    ------------------------------

    Termination

        Every day becomes selling day exactly once.

        Therefore every legal transaction has been checked.

    ------------------------------

    Correctness Intuition

        Every optimal answer finishes on some day.

        When we process that day,

        the optimal buying day has already appeared.

        Therefore

            optimal profit

        is computed on that iteration.

    ------------------------------

    Why Naive Solutions Fail

        O(n²)

        compares every pair.

        The minimum buying price is recomputed repeatedly.

        Thousands of duplicate comparisons happen.

        Running minimum compresses all previous buying days
        into one value.

    ============================================================================
    🔴 WHY WRONG SOLUTIONS FAIL
    ============================================================================

    Mistake 1

        Update minimum after computing future profit.

    Violated Invariant

        Buying must belong to history.

    ------------------------------

    Mistake 2

        Return largest difference between any two values.

    Counterexample

        [5,2,7]

        Difference

            7-2

        valid.

        But

            5-7

        is illegal.

    Order matters.

    ------------------------------

    Mistake 3

        Initialize answer with Integer.MIN_VALUE.

    Profit cannot be negative because

        no trade

        is always legal.

    Start from zero.

    ------------------------------

    Interview Trap

        Candidate says

            "Maximum difference."

        Interviewer asks

            "Difference respecting order?"

        Correct answer

            Left element must appear before right element.

    ============================================================================
    ⚙ IMPLEMENTATION BLUEPRINT
    ============================================================================

    Type in exactly this order.

    Step 1

        Handle null / size <=1.

    Step 2

        minBuy = first price

    Step 3

        maxProfit = 0

    Step 4

        Iterate from index 1.

    Step 5

        Candidate

            today - minBuy

    Step 6

        Update answer.

    Step 7

        Update running minimum.

    Step 8

        Return answer.

    Mechanical reconstruction:

        initialize

        ↓

        compute candidate

        ↓

        update answer

        ↓

        update minimum

        ↓

        repeat

    ============================================================================
    🧾 ULTRA COMPACT PSEUDOCODE
    ============================================================================

    min = first

    ans = 0

    for every price

        ans = max(ans, price-min)

        min = min(min, price)

    return ans

    ============================================================================
    6. SOLUTION CLASSES
    ============================================================================
    */

    static final class LC121BruteForce {

        /*
        Idea

            Try every buy day.

            Try every sell day after it.

        Invariant

            Every legal pair is examined.

        Limitation

            Quadratic.

        Complexity

            Time  : O(n²)

            Space : O(1)

        Interview usefulness

            Good baseline.
        */

        static int maxProfit(int[] prices) {

            if (prices == null || prices.length <= 1)
                return 0;

            int answer = 0;

            for (int buy = 0; buy < prices.length; buy++) {

                for (int sell = buy + 1; sell < prices.length; sell++) {

                    answer = Math.max(answer,
                            prices[sell] - prices[buy]);
                }
            }

            return answer;
        }
    }    static final class LC121Improved {

        /*
        Idea

            Build the running minimum while scanning once.

            Every day is treated as the selling day.

        Invariant

            Before evaluating today's profit,

            minBuy stores the cheapest buying opportunity
            from all previous days.

        Improvement

            Compresses all historical buying days into one value.

        Complexity

            Time  : O(n)

            Space : O(1)

        Interview usefulness

            This is the expected interview solution.
        */

        static int maxProfit(int[] prices) {

            if (prices == null || prices.length <= 1)
                return 0;

            int minBuy = prices[0];
            int maxProfit = 0;

            for (int i = 1; i < prices.length; i++) {

                maxProfit = Math.max(maxProfit,
                        prices[i] - minBuy);

                minBuy = Math.min(minBuy,
                        prices[i]);
            }

            return maxProfit;
        }
    }

    static final class LC121Optimal {

        /*
        Idea

            Same asymptotic complexity as Improved.

            The emphasis here is on making the invariant
            visible while live coding.

        Invariant

            minBuy

                cheapest buying opportunity seen so far.

            maxProfit

                best completed legal transaction.

        Correctness

            Every legal transaction ends on exactly one day.

            That day is evaluated once.

        Complexity

            Time  : O(n)

            Space : O(1)

        Interview usefulness

            Preferred implementation.
        */

        static int maxProfit(int[] prices) {

            if (prices == null || prices.length <= 1)
                return 0;

            int minBuy = prices[0];
            int maxProfit = 0;

            for (int price : prices) {

                // 🟢 Invariant:
                // minBuy always belongs to history.

                maxProfit = Math.max(
                        maxProfit,
                        price - minBuy);

                // 🟢 Update historical minimum for future sells.
                minBuy = Math.min(
                        minBuy,
                        price);
            }

            return maxProfit;
        }

        /*
        Kadane Interpretation

        Build the difference array.

            diff[i] = prices[i]-prices[i-1]

        Example

            7 1 5 3 6 4

        Difference

            -6 +4 -2 +3 -2

        Buying then selling corresponds to selecting
        one contiguous positive-sum segment.

        Therefore

            LC121

        can also be solved as

            Maximum Subarray.

        The running minimum solution is usually preferred
        because

            • fewer variables

            • simpler invariant

            • easier interview explanation.
        */

        static int maxProfitKadane(int[] prices) {

            if (prices == null || prices.length <= 1)
                return 0;

            int current = 0;
            int best = 0;

            for (int i = 1; i < prices.length; i++) {

                current = Math.max(
                        0,
                        current + prices[i] - prices[i - 1]);

                best = Math.max(best, current);
            }

            return best;
        }
    }

    /*
    ============================================================================
    🟣 INTERVIEW ARTICULATION
    ============================================================================

    Pattern

        Running Minimum

    Invariant

        minBuy stores the cheapest historical price.

    Search Space

        Every day is treated as the selling day.

    Discard Rule

        Any buying price larger than the running minimum
        can never produce a better future transaction.

    Correctness

        The optimal transaction ends on some day.

        When that day is processed,
        its buying day already belongs to history.

    Termination

        Every selling day is processed once.

    In-place Feasibility

        Yes.

    Streaming Feasibility

        Yes.

        Prices may arrive one by one.

    When NOT to Use

        More than one transaction.

        Cooldown.

        Transaction fee.

        Transaction limit.

    ============================================================================
    🎯 INTERVIEW RECALL SHEET
    ============================================================================

    Trigger

        One buy.
        One sell.

    Invariant

        Cheapest buy so far.

    Search Target

        Maximum future profit.

    Discard Rule

        Larger historical prices are dominated forever.

    Common Trap

        Updating variables in the wrong order.

    Edge Cases

        Empty array.

        One element.

        Strictly decreasing prices.

        Duplicate prices.

    One-liner

        Sell today using the cheapest historical buy.

    Re-derivation Cue

        Compress the entire past into one minimum.

    ============================================================================
    🔄 TRANSITION TO LC122
    ============================================================================

    LC121 answers

        "What if I can trade only once?"

    New question

        "What changes if I may trade forever?"

    Old State

        minBuy

    is no longer enough.

    Why?

        After selling,

        we may buy again.

    Therefore

        previous profit now influences future buying cost.

    This introduces the idea of

        Effective Buy Cost

    which becomes the central invariant for every
    remaining Stock DP problem.    /*
    ============================================================================
    ██████╗  █████╗ ██████╗ ████████╗    ██████╗ ██████╗
    ██╔══██╗██╔══██╗██╔══██╗╚══██╔══╝    ╚════██╗╚════██╗
    ██████╔╝███████║██████╔╝   ██║        █████╔╝ █████╔╝
    ██╔═══╝ ██╔══██║██╔══██╗   ██║       ██╔═══╝  ╚═══██╗
    ██║     ██║  ██║██║  ██║   ██║       ███████╗██████╔╝
    ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝       ╚══════╝╚═════╝
    ============================================================================

    LC 122

    Best Time to Buy and Sell Stock II

    Difficulty : Medium

    Pattern

        Greedy

        State Compression DP

        Unlimited Transactions

    ============================================================================
    📘 PRIMARY PROBLEM
    ============================================================================

    Problem

    You are given an array prices.

    You may perform as many transactions as you like.

    Constraints

        • Hold at most one stock.

        • Sell before buying again.

        • Buying and selling on the same day is allowed.

    Goal

        Maximize total profit.

    Example

        prices

            [7,1,5,3,6,4]

        Buy 1

        Sell 5

        Profit = 4

        Buy 3

        Sell 6

        Profit = 3

        Total = 7

    ============================================================================
    🔵 CORE PATTERN OVERVIEW
    ============================================================================

    Pattern

        Greedy
            OR
        Two-State Dynamic Programming

    Archetype

        State Machine

    States

        HOLD

            We currently own one stock.

        CASH

            We currently own no stock.

    Core Invariant

        At the end of every day

            HOLD

        stores the maximum achievable wealth while
        holding one stock.

        CASH

        stores the maximum achievable wealth while
        holding no stock.

    Why LC121 No Longer Works

        Previously

            history

        could be summarized by

            minimum price.

        Now

            previous profit changes future buying power.

        Buying after earning profit is effectively cheaper.

    Therefore

        we need

            Effective Buy Cost

    rather than

            Raw Buy Price.

    ============================================================================
    🟢 MENTAL MODEL
    ============================================================================

    Imagine two wallets.

        Wallet A

            Holding one stock.

        Wallet B

            Holding cash only.

    Every day

        exactly one transition may happen.

            HOLD -> CASH

                Sell.

            CASH -> HOLD

                Buy.

            HOLD -> HOLD

                Ignore.

            CASH -> CASH

                Ignore.

    Every transition preserves the best possible wealth.

    ============================================================================
    🟢 CORE INVARIANTS
    ============================================================================

    Invariant 1

        maxProfit

        equals

        best completed profit.

    -----------------------------

    Invariant 2

        minBuy

        no longer means

            cheapest price.

        Instead

            effective buying cost.

    Formula

        effectiveCost

            =

        currentPrice

            -

        completedProfit

    This is the key insight behind all remaining stock DP
    variants.

    -----------------------------

    Invariant 3

        Every completed transaction immediately becomes
        available for reinvestment.

    -----------------------------

    Allowed Transition

        Sell

            if profitable.

        Then

            reduce effective future buying cost.

    -----------------------------

    Correctness

        Previous profits reduce future purchase cost.

        Therefore

        maintaining

            effective buy cost

        is sufficient.

    ============================================================================
    🔴 WHY WRONG SOLUTIONS FAIL
    ============================================================================

    Mistake

        Reusing LC121 unchanged.

    Counterexample

        prices

            [1,5,3,8]

    LC121

        returns

            7

    Correct answer

        9

            (1→5)

            +

            (3→8)

    LC121 assumes

        exactly one transaction.

    Unlimited transactions require state updates.

    ============================================================================
    ⚙ IMPLEMENTATION BLUEPRINT
    ============================================================================

    Initialize

        effectiveBuy

            first price

        maxProfit

            zero

    For every price

        Update completed profit.

        Update effective buying cost.

    Return completed profit.

    ============================================================================
    🧾 ULTRA COMPACT PSEUDOCODE
    ============================================================================

    profit = 0

    buy = first

    for every price

        profit = max(profit, price-buy)

        buy = min(buy, price-profit)

    return profit

    ============================================================================
    6. SOLUTION CLASSES
    ============================================================================
    */

    static final class LC122Greedy {

        /*
        Idea

            Every upward slope contributes to the answer.

        Observation

            Splitting one increasing segment into
            multiple profitable trades does not change
            the total profit.

        Example

            1 3 7

            (7-1)

                =

            (3-1)

                +

            (7-3)

        Complexity

            Time  : O(n)

            Space : O(1)

        Interview usefulness

            Fastest explanation.
        */

        static int maxProfit(int[] prices) {

            if (prices == null || prices.length <= 1)
                return 0;

            int answer = 0;

            for (int i = 1; i < prices.length; i++) {

                int gain = prices[i] - prices[i - 1];

                if (gain > 0)
                    answer += gain;
            }

            return answer;
        }
    }

    static final class LC122StateCompression {

        /*
        Idea

            Maintain

                effective buying cost

            instead of explicit DP arrays.

        Invariant

            effectiveBuy

            equals

            minimum

                currentPrice
                -
                completedProfit

            seen so far.

        Complexity

            Time  : O(n)

            Space : O(1)

        Interview usefulness

            This implementation naturally extends to

                LC123
                LC188
                LC714
                LC309

            with only small state changes.
        */

        static int maxProfit(int[] prices) {

            if (prices == null || prices.length <= 1)
                return 0;

            int effectiveBuy = prices[0];

            int maxProfit = 0;

            for (int i = 1; i < prices.length; i++) {

                // 🟢 Complete today's selling transaction.

                maxProfit = Math.max(
                        maxProfit,
                        prices[i] - effectiveBuy);

                // 🟢 Effective buying cost after reinvesting
                // previously earned profit.

                effectiveBuy = Math.min(
                        effectiveBuy,
                        prices[i] - maxProfit);
            }

            return maxProfit;
        }
    }    /*
    ============================================================================
    🟣 INTERVIEW ARTICULATION
    ============================================================================

    Pattern

        Unlimited Transactions

    Core State

        Effective Buy Cost

    Invariant

        effectiveBuy

            equals

            minimum value of

                currentPrice - completedProfit

        observed so far.

    Interpretation

        Every completed profit immediately reduces
        the effective cost of buying again.

    Discard Rule

        Any buying opportunity with a higher effective cost
        can never dominate a lower effective cost.

    Correctness

        Every completed transaction contributes to future
        purchasing power exactly once.

    Therefore the entire trading history can still be
    compressed into constant memory.

    Termination

        Every day has been considered once as a potential
        selling day.

    In-place Feasibility

        Yes.

    Streaming Feasibility

        Yes.

        Prices may arrive online.

    When NOT To Use

        Fixed cooldown.

        Transaction fee.

        Limited transaction count.

        Those require additional states.

    ============================================================================
    🎯 INTERVIEW RECALL SHEET
    ============================================================================

    Trigger

        Unlimited transactions.

    Core Invariant

        Maintain effective buying cost.

    State

        Completed profit.

    Search Target

        Best profit after today's sale.

    Discard Rule

        Larger effective buy cost is permanently dominated.

    Common Trap

        Forgetting that previous profit affects future buys.

    Edge Cases

        Empty input.

        One price.

        Strictly increasing.

        Strictly decreasing.

        Flat prices.

    One-liner

        Profit earned yesterday discounts today's buy.

    Re-derivation Cue

        Replace raw buying price with

            price - completedProfit.

    ============================================================================
    🔄 TRANSITION TO LC123
    ============================================================================

    LC122 introduces one powerful idea.

        Previous profit changes future buying cost.

    LC123 simply duplicates this idea.

    Instead of maintaining

        one buy
        one sell

    we maintain

        first buy
        first sell
        second buy
        second sell

    The algorithm remains

        left to right

        O(n)

        O(1)

    Only the number of states increases.

    ============================================================================
    ██████╗  █████╗ ██████╗ ████████╗    ██████╗ ██████╗
    ██╔══██╗██╔══██╗██╔══██╗╚══██╔══╝    ╚════██╗╚════██╗
    ██████╔╝███████║██████╔╝   ██║        █████╔╝ █████╔╝
    ██╔═══╝ ██╔══██║██╔══██╗   ██║       ██╔═══╝ ██╔═══╝
    ██║     ██║  ██║██║  ██║   ██║       ███████╗███████╗
    ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝       ╚══════╝╚══════╝
    ============================================================================

    LC 123

    Best Time to Buy and Sell Stock III

    Difficulty : Hard

    Pattern

        Four-State Dynamic Programming

        State Compression

    ============================================================================
    📘 PRIMARY PROBLEM
    ============================================================================

    Problem

        You may perform at most

            two

        complete transactions.

    Rules

        Buy before sell.

        Hold at most one stock.

        Complete first transaction before
        starting the second.

    Example

        prices

            [3,3,5,0,0,3,1,4]

        Buy 0

        Sell 3

        Profit = 3

        Buy 1

        Sell 4

        Profit = 3

        Answer = 6

    ============================================================================
    🔵 CORE PATTERN OVERVIEW
    ============================================================================

    Pattern

        Multi-State DP

    Search Space

        Every legal sequence of

            Buy

            Sell

            Buy

            Sell

    Observation

        The second purchase depends on
        profit from the first sale.

    Therefore

        LC122's

            effective buying cost

        naturally appears again.

    ============================================================================
    🟢 MENTAL MODEL
    ============================================================================

    Imagine four checkpoints.

        Buy #1

            ↓

        Sell #1

            ↓

        Buy #2

            ↓

        Sell #2

    Every day

        each checkpoint may improve.

    Earlier checkpoints influence
    later checkpoints.

    History never needs to be revisited.

    ============================================================================
    🟢 FOUR CORE STATES
    ============================================================================

    State 1

        minBuyOne

        Cheapest first purchase.

    --------------------------------

    State 2

        maxProfitSaleOne

        Best completed first transaction.

    --------------------------------

    State 3

        minBuyTwo

        Effective cost of second purchase.

        Formula

            currentPrice

                -

            firstTransactionProfit

    --------------------------------

    State 4

        maxProfitSaleTwo

        Best completed second transaction.

    These four variables completely
    summarize every legal history.

    No DP table is required.

    ============================================================================
    🔴 WHY THIS WORKS
    ============================================================================

    Buying the second stock
    is not paid entirely from cash.

    It is partially funded by

        profit earned previously.

    Therefore

        effective second buying cost

        equals

            currentPrice
                -
            firstTransactionProfit

    This is exactly the same invariant
    introduced in LC122.

    LC123 simply applies it twice.    /*
    ============================================================================
    ⚙ IMPLEMENTATION BLUEPRINT
    ============================================================================

    Type in exactly this order.

    Step 1

        Handle trivial inputs.

    Step 2

        Initialize

            minBuyOne

            maxProfitSaleOne

            minBuyTwo

            maxProfitSaleTwo

    Step 3

        Scan prices exactly once.

    Step 4

        Update second sale.

    Step 5

        Update second buy.

    Step 6

        Update first sale.

    Step 7

        Update first buy.

    Step 8

        Return second sale.

    ----------------------------------------------------------------

    Why this update order?

    maxProfitSaleTwo

        depends on

            minBuyTwo

    minBuyTwo

        depends on

            maxProfitSaleOne

    maxProfitSaleOne

        depends on

            minBuyOne

    Therefore the dependency chain is

        Buy1

            →

        Sell1

            →

        Buy2

            →

        Sell2

    During one iteration we update them in reverse dependency
    order so every state consumes values representing history
    from previous iterations rather than partially updated future
    information.

    This ordering is easy to remember.

        Sell2

        Buy2

        Sell1

        Buy1

    ============================================================================
    🧾 ULTRA COMPACT PSEUDOCODE
    ============================================================================

    buy1 = firstPrice

    sell1 = 0

    buy2 = firstPrice

    sell2 = 0

    for every remaining price

        sell2 = max(sell2, price - buy2)

        buy2 = min(buy2, price - sell1)

        sell1 = max(sell1, price - buy1)

        buy1 = min(buy1, price)

    return sell2

    ============================================================================
    6. SOLUTION CLASSES
    ============================================================================
    */

    static final class LC123Optimal {

        /*
        Idea

            Compress four DP states into four variables.

        State Mapping

            buy1

                cheapest first purchase

            sell1

                best first completed transaction

            buy2

                effective second purchase

            sell2

                best overall answer

        Invariant

            Every variable stores the optimum value
            of its corresponding state after processing
            the current day.

        Correctness

            Every legal sequence

                Buy

                Sell

                Buy

                Sell

            is represented by exactly one path through
            these four states.

        Complexity

            Time  : O(n)

            Space : O(1)

        Interview usefulness

            Canonical solution expected for LC123.
        */

        static int maxProfit(int[] prices) {

            if (prices == null || prices.length <= 1)
                return 0;

            int minBuyOne = prices[0];

            int maxProfitSaleOne = 0;

            int minBuyTwo = prices[0];

            int maxProfitSaleTwo = 0;

            for (int price : prices) {

                // 🟢 Invariant:
                // Second transaction finishes today.

                maxProfitSaleTwo = Math.max(
                        maxProfitSaleTwo,
                        price - minBuyTwo);

                // 🟢 Effective second buying cost.

                minBuyTwo = Math.min(
                        minBuyTwo,
                        price - maxProfitSaleOne);

                // 🟢 Finish first transaction today.

                maxProfitSaleOne = Math.max(
                        maxProfitSaleOne,
                        price - minBuyOne);

                // 🟢 Cheapest first purchase.

                minBuyOne = Math.min(
                        minBuyOne,
                        price);
            }

            return maxProfitSaleTwo;
        }
    }

    /*
    ============================================================================
    🟣 INTERVIEW ARTICULATION
    ============================================================================

    Pattern

        Four-State Dynamic Programming.

    Core Invariant

        Four variables summarize every legal history.

    Search Space

        All valid

            Buy

            Sell

            Buy

            Sell

        sequences.

    State Meaning

        buy1

            Cheapest first purchase.

        sell1

            Best first completed trade.

        buy2

            Cheapest second purchase after accounting
            for profit already earned.

        sell2

            Final answer.

    Discard Rule

        Any state worse than the current optimum
        can never become optimal later because all
        future transitions are monotonic improvements.

    Correctness

        Every legal transaction sequence passes
        through these four states exactly once.

        No history outside these four values
        is required.

    Termination

        After the last day

            sell2

        represents the maximum achievable profit
        using at most two transactions.

    In-place Feasibility

        Yes.

    Streaming Feasibility

        Yes.

        Prices may be processed online.

    When NOT To Use

        Arbitrary k transactions.

        That requires maintaining
        multiple buy/sell state pairs.

    ============================================================================
    🎯 INTERVIEW RECALL SHEET
    ============================================================================

    Trigger

        At most two transactions.

    Pattern

        Four compressed DP states.

    Invariant

        One variable per DP state.

    Search Target

        Maximize second completed sale.

    Discard Rule

        Dominated state values are never needed again.

    Common Trap

        Forgetting that

            buy2

        is an effective buying cost rather than
        the raw stock price.

    Edge Cases

        Empty input.

        One element.

        Always increasing.

        Always decreasing.

        Duplicate prices.

    One-liner

        Duplicate the LC122 state machine once.

    Re-derivation Cue

        Every additional transaction adds

            one Buy state

            one Sell state.    /*
    ============================================================================
    🔄 PATTERN EVOLUTION
    ============================================================================

    Understanding the evolution is more valuable than
    memorizing individual problems.

    ----------------------------------------------------------------

    LC121

        One Buy

            →

        One Sell

    States

        buy1

        sell1

    ----------------------------------------------------------------

    LC122

        Unlimited Transactions

    Observation

        Profit from previous transactions reduces
        future buying cost.

    New Invariant

        effectiveBuy

            =

        price - completedProfit

    ----------------------------------------------------------------

    LC123

        Duplicate the previous state machine.

        buy1

            →

        sell1

            →

        buy2

            →

        sell2

    ----------------------------------------------------------------

    Future Generalization

        Every additional transaction contributes

            one Buy state

            one Sell state

    Therefore

        k transactions

        require

            2k compressed states.

    ============================================================================
    ⚫ PATTERN MAPPING
    ============================================================================

    ----------------------------------------------------------------
    Problem                     Pattern
    ----------------------------------------------------------------

    LC121

        Running Minimum

    ----------------------------------------------------------------

    LC122

        Two-State DP

    ----------------------------------------------------------------

    LC123

        Four-State DP

    ----------------------------------------------------------------

    LC188

        2k-State DP

    ----------------------------------------------------------------

    LC714

        Two-State DP
        +
        Transaction Fee

    ----------------------------------------------------------------

    LC309

        Two-State DP
        +
        Cooldown State

    ----------------------------------------------------------------

    Observe

        Every later problem modifies

            the state

        not

            the scanning order.

    ============================================================================
    🔄 VARIATIONS & TWEAKS
    ============================================================================

    Variant

        Prices contain duplicates.

    Effect

        Nothing changes.

    Invariant remains identical.

    ----------------------------------------------------------------

    Variant

        Prices strictly increase.

    Effect

        LC121

            first day buy

            last day sell

        LC122

            capture every increase

        LC123

            behaves exactly like LC122
            because only one profitable trend exists.

    ----------------------------------------------------------------

    Variant

        Prices strictly decrease.

    Effect

        Every Sell state remains zero.

    Buying still updates.

    Selling never improves.

    ----------------------------------------------------------------

    Variant

        Large plateaus.

    Effect

        Duplicate prices never violate
        any invariant.

    ----------------------------------------------------------------

    Variant

        Streaming prices.

    Effect

        All three algorithms remain valid.

        Historical prices never need to
        be revisited.

    ----------------------------------------------------------------

    Pattern Break

        Transaction Fee

    Reason

        Selling transition changes.

    ----------------------------------------------------------------

    Pattern Break

        Cooldown

    Reason

        Buying transition depends on an
        older Sell state.

    ----------------------------------------------------------------

    Pattern Break

        k Transactions

    Reason

        Number of states increases.

    ============================================================================
    🧠 MASTERY CHECKLIST
    ============================================================================

    LC121

        □ I know why running minimum works.

        □ I know why selling always happens today.

        □ I know why answer starts at zero.

        □ I can derive Kadane interpretation.

    ----------------------------------------------------------------

    LC122

        □ I know why previous profit reduces
          buying cost.

        □ I understand effective buying cost.

        □ I can explain greedy and DP versions.

    ----------------------------------------------------------------

    LC123

        □ I know every state.

        □ I know update ordering.

        □ I know why four variables are enough.

        □ I can derive the recurrence
          without memorization.

    ----------------------------------------------------------------

    Global

        □ I know the invariant.

        □ I know the search space.

        □ I know the discard rule.

        □ I know the termination argument.

        □ I know why naive solutions fail.

        □ I can debug wrong state updates.

        □ I know where the pattern breaks.

        □ I can transition naturally to LC188.

    ============================================================================
    COMMON DEBUGGING CHECKLIST
    ============================================================================

    If the answer is wrong,

    verify in this order.

    1.

        Empty input handled?

    -----------------------------

    2.

        Profit initialized to zero?

    -----------------------------

    3.

        Buying before selling?

    -----------------------------

    4.

        Effective buying cost used where required?

    -----------------------------

    5.

        State update ordering correct?

    -----------------------------

    6.

        Using current iteration values
        only where legal?

    -----------------------------

    7.

        Returning final Sell state
        instead of Buy state?

    ============================================================================
    IMPLEMENTATION RECONSTRUCTION GUIDE
    ============================================================================

    LC121

        minBuy

            ↓

        sell

            ↓

        update minimum

    ----------------------------------------------------------------

    LC122

        profit

            ↓

        effective buy

    ----------------------------------------------------------------

    LC123

        sell2

            ↓

        buy2

            ↓

        sell1

            ↓

        buy1

    ----------------------------------------------------------------

    Never memorize code.

    Memorize

        State

            →

        Transition

            →

        Invariant.    /*
    ============================================================================
    STOCK DP FAMILY CHEAT SHEET
    ============================================================================

    The entire LeetCode Stock series can be viewed as one evolving
    finite-state machine.

    -------------------------------------------------------------------------
    LC121

        One Transaction

            Buy1
              |
              v
            Sell1

    States

        buy1

        sell1

    -------------------------------------------------------------------------
    LC122

        Unlimited Transactions

            Buy
              |
              v
            Sell
              |
              +------+
                     |
                     v
                    Buy

    Effective Buy

        buy = min(buy, price - sell)

    -------------------------------------------------------------------------
    LC123

        Two Transactions

        Buy1
          |
          v
        Sell1
          |
          v
        Buy2
          |
          v
        Sell2

    -------------------------------------------------------------------------
    LC188

        Generalization

        Buy1

        Sell1

        Buy2

        Sell2

        ...

        BuyK

        SellK

    Number of states

        2 * K

    -------------------------------------------------------------------------
    LC714

        Add transaction fee.

    Selling transition becomes

        sell = max(sell,
                   price - buy - fee)

    Buying transition remains

        buy = min(buy,
                  price - sell)

    -------------------------------------------------------------------------
    LC309

        Cooldown

    Buying transition cannot immediately
    use yesterday's sell.

    Instead

        previousSellBeforeYesterday

    participates in the transition.

    ============================================================================
    STATE EVOLUTION TABLE
    ============================================================================

    -------------------------------------------------------------------------
    Problem        Buy State                     Sell State
    -------------------------------------------------------------------------

    LC121

        min(price)

        max(price-buy)

    -------------------------------------------------------------------------

    LC122

        min(price-profit)

        max(price-buy)

    -------------------------------------------------------------------------

    LC123

        buy1

        sell1

        buy2

        sell2

    -------------------------------------------------------------------------

    LC188

        buy[i]

        sell[i]

    -------------------------------------------------------------------------

    LC714

        buy

        sell - fee

    -------------------------------------------------------------------------

    LC309

        buy(previousSell)

        sell

    -------------------------------------------------------------------------

    Every problem only changes

        one transition

    or

        the number of states.

    The invariant philosophy never changes.

    ============================================================================
    INTERVIEW COMPARISON TABLE
    ============================================================================

    -------------------------------------------------------------------------
    Problem        States        Time      Space
    -------------------------------------------------------------------------

    LC121

        2

        O(n)

        O(1)

    -------------------------------------------------------------------------

    LC122

        2

        O(n)

        O(1)

    -------------------------------------------------------------------------

    LC123

        4

        O(n)

        O(1)

    -------------------------------------------------------------------------

    LC188

        2K

        O(nk)

        O(k)

    -------------------------------------------------------------------------

    LC714

        2

        O(n)

        O(1)

    -------------------------------------------------------------------------

    LC309

        3 Effective

        O(n)

        O(1)

    -------------------------------------------------------------------------

    ============================================================================
    RE-DERIVATION ALGORITHM
    ============================================================================

    Never memorize code.

    During an interview ask yourself only four questions.

    Question 1

        What are the legal states?

    -----------------------------

    Question 2

        What transition changes one state
        into another?

    -----------------------------

    Question 3

        What historical information is required?

    -----------------------------

    Question 4

        Can that history be compressed into
        O(1) variables?

    If all four questions are answered,

    the implementation becomes mechanical.

    ============================================================================
    COMMON INTERVIEW QUESTIONS
    ============================================================================

    Q.

        Why is O(1) memory sufficient?

    A.

        Because each DP state depends only on
        the previous day's compressed state.

    -------------------------------------------------------------------------

    Q.

        Why does LC122 subtract previous profit
        while buying?

    A.

        Previous profit effectively discounts
        future purchases.

    -------------------------------------------------------------------------

    Q.

        Why does LC123 require four variables?

    A.

        Each transaction contributes exactly

            one Buy state

            one Sell state.

    -------------------------------------------------------------------------

    Q.

        Why does LC188 iterate transaction states
        backwards?

    A.

        To avoid overwriting states that are still
        required during the same day's transitions.

    -------------------------------------------------------------------------

    Q.

        Which problems are actually greedy?

    A.

        LC122 admits a greedy proof.

        The state-compressed DP is equivalent.

    ============================================================================
    MEMORY HOOKS
    ============================================================================

    LC121

        Cheapest Buy.

    -------------------------------------------------------------------------

    LC122

        Cheapest Effective Buy.

    -------------------------------------------------------------------------

    LC123

        Duplicate LC122 once.

    -------------------------------------------------------------------------

    LC188

        Duplicate LC123 K times.

    -------------------------------------------------------------------------

    LC714

        Charge fee while selling.

    -------------------------------------------------------------------------

    LC309

        Delay buying by one day.

    ============================================================================


    */


    public static void main(String[] args) {

        /*
        ============================================================
        LC121
        ============================================================
        */

    // Representative example.
        assert LC121Optimal.maxProfit(
                new int[]{7, 1, 5, 3, 6, 4}) == 5;

    // Decreasing prices.
        assert LC121Optimal.maxProfit(
                new int[]{7, 6, 4, 3, 1}) == 0;

    // Single element.
        assert LC121Optimal.maxProfit(
                new int[]{5}) == 0;

    // Flat prices.
        assert LC121Optimal.maxProfit(
                new int[]{3, 3, 3, 3}) == 0;

    // Kadane equivalence.
        assert LC121Optimal.maxProfit(
                new int[]{7, 1, 5, 3, 6, 4})
            ==
            LC121Optimal.maxProfitKadane(
            new int[]{7, 1, 5, 3, 6, 4});

        /*
        ============================================================
        LC122
        ============================================================
        */

    // Representative example.
        assert LC122Greedy.maxProfit(
                new int[]{7, 1, 5, 3, 6, 4}) == 7;

    // DP compression should equal greedy.
        assert LC122Greedy.maxProfit(
                new int[]{7, 1, 5, 3, 6, 4})
            ==
            LC122StateCompression.maxProfit(
            new int[]{7, 1, 5, 3, 6, 4});

    // Strictly increasing.
        assert LC122Greedy.maxProfit(
                new int[]{1, 2, 3, 4, 5}) == 4;

    // Strictly decreasing.
        assert LC122Greedy.maxProfit(
                new int[]{5, 4, 3, 2, 1}) == 0;

    // Oscillating prices.
        assert LC122Greedy.maxProfit(
                new int[]{2, 1, 2, 0, 1}) == 2;

        /*
        ============================================================
        LC123
        ============================================================
        */

    // Official example.
        assert LC123Optimal.maxProfit(
                new int[]{3, 3, 5, 0, 0, 3, 1, 4}) == 6;

    // Decreasing prices.
        assert LC123Optimal.maxProfit(
                new int[]{7, 6, 5, 4, 3}) == 0;

    // Increasing prices.
        assert LC123Optimal.maxProfit(
                new int[]{1, 2, 3, 4, 5}) == 4;

    // Two profitable segments.
        assert LC123Optimal.maxProfit(
                new int[]{1, 5, 2, 8}) == 10;

    // Duplicate prices.
        assert LC123Optimal.maxProfit(
                new int[]{2, 2, 2, 2}) == 0;

        System.out.println("All StockSeries1 assertions passed.");
}

}

/*
============================================================================
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
============================================================================
*/