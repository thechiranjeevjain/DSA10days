package org.chijai.day1.session3;

import java.util.Arrays;

/**
 * ============================================================================
 * StockSeries2
 * ============================================================================
 *
 * Covers
 *
 * LC188  Best Time to Buy and Sell Stock IV
 *
 * LC714  Best Time to Buy and Sell Stock with Transaction Fee
 *
 * LC309  Best Time to Buy and Sell Stock with Cooldown
 *
 * ============================================================================
 *
 * Theme
 *
 * Generalized Stock Dynamic Programming
 *
 * Instead of learning three unrelated problems,
 * this chapter derives all of them from one DP framework.
 *
 * Every stock problem can be viewed as
 *
 *      States
 *
 *          +
 *
 *      State Transitions
 *
 *          +
 *
 *      One additional constraint.
 *
 * ============================================================================
 */
public class StockSeries2 {

    /*
    ============================================================================
    📘 CHAPTER OVERVIEW
    ============================================================================

    StockSeries1 introduced

        LC121

            Running Minimum

        LC122

            Unlimited Transactions

        LC123

            Four-State DP

    This chapter generalizes the pattern.

    We answer three questions.

    Question 1

        What if we allow

            K

        transactions?

        (LC188)

    ---------------------------------------------

    Question 2

        What if selling costs money?

        (LC714)

    ---------------------------------------------

    Question 3

        What if buying is forbidden for one day
        after selling?

        (LC309)

    ---------------------------------------------

    Every answer changes only

        one transition

    while preserving the same DP philosophy.

    ============================================================================
    🔵 UNIFIED STOCK DP FRAMEWORK
    ============================================================================

    Every stock problem can be represented as
    a finite-state machine.

    Generic State

        BUY

        SELL

    Every transaction contributes

            BUY

                →

            SELL

    Therefore

        K transactions

    naturally require

        2K states.

    ----------------------------------------------------------------

    Transition

        BUY

            may come from

                previous SELL

        SELL

            may come from

                corresponding BUY

    ----------------------------------------------------------------

    Generic Recurrence

        buy

            =

        best wealth while holding stock

    sell

            =

        best wealth while holding cash

    ============================================================================
    🟢 CORE INVARIANT
    ============================================================================

    Regardless of the problem,

    every DP state always represents

        the optimal achievable wealth

    after processing today's price.

    Once this invariant holds,

    transitions become mechanical.

    ============================================================================
    🟢 WHY STATE COMPRESSION WORKS
    ============================================================================

    Observe carefully.

    Today's state never depends on

        two weeks ago,

        five weeks ago,

        or arbitrary history.

    It only depends on

        yesterday's compressed states.

    Therefore

    instead of storing

        DP[day][state]

    we store only

        current state values.

    Memory

        O(days × states)

    becomes

        O(states)

    ============================================================================
    ⚫ GENERAL STATE MACHINE
    ============================================================================

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

              |

              v

             ...

              |

              v

            BuyK

              |

              v

            SellK

    Every additional transaction adds

        exactly

            one BUY state

            one SELL state.

    ============================================================================
    🔄 EVOLUTION OF THE STOCK SERIES
    ============================================================================

    LC121

        Running Minimum

            ↓

    LC122

        Unlimited Transactions

            ↓

    LC123

        Two Transactions

            ↓

    LC188

        K Transactions

            ↓

    LC714

        Add Fee

            ↓

    LC309

        Add Cooldown

    ============================================================================
    ██████╗  █████╗ ██████╗ ████████╗
    ██╔══██╗██╔══██╗██╔══██╗╚══██╔══╝
    ██████╔╝███████║██████╔╝   ██║
    ██╔═══╝ ██╔══██║██╔══██╗   ██║
    ██║     ██║  ██║██║  ██║   ██║
    ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝

    LC188

    Best Time to Buy and Sell Stock IV

    Difficulty

        Hard

    Pattern

        Generalized State Compression DP

    ============================================================================
    📘 PRIMARY PROBLEM
    ============================================================================

    Problem

        You are given

            prices

        and

            k

        where

            k

        is the maximum number of transactions.

    Rules

        Buy before Sell.

        Hold at most one stock.

        Transactions cannot overlap.

    Return

        Maximum obtainable profit.

    Example

        k = 2

        prices

            [2,4,1]

        Buy

            2

        Sell

            4

        Profit

            2

    ============================================================================
    🔵 CORE PATTERN OVERVIEW
    ============================================================================

    Pattern

        Multi-State Dynamic Programming

    Search Space

        Every legal sequence

            Buy

            Sell

        repeated at most

            K

        times.

    Observation

        LC123 used

            four states.

        LC188 simply generalizes this.

    Number of States

        Buy1

        Sell1

        ...

        BuyK

        SellK

    ============================================================================
    🟢 MENTAL MODEL
    ============================================================================

    Imagine

        K workers.

    Worker i

    manages

        Transaction i.

    Every worker owns

        one Buy state

        one Sell state.

    While scanning prices,

    every worker independently updates

        Buy

        then

        Sell

    using the result produced by
    the previous worker.

    ============================================================================
    🟢 CORE INVARIANTS
    ============================================================================

    Invariant 1

        buy[i]

        stores

        the minimum effective buying cost
        for transaction i.

    -------------------------------------

    Invariant 2

        sell[i]

        stores

        the maximum completed profit
        after transaction i.

    -------------------------------------

    Invariant 3

        sell[i-1]

        always represents

        completed history

        before

        buy[i].

    -------------------------------------

    Invariant 4

        Every transaction depends only on

            previous transaction

        not

            every previous day.

    This enables state compression.

    ============================================================================
    🔴 WHY WRONG SOLUTIONS FAIL
    ============================================================================

    Wrong Idea

        Duplicate LC123 code manually
        for every value of k.

    Problem

        Impossible for arbitrary k.

    Correct Idea

        Store states inside arrays.

            buy[]

            sell[]

    Then iterate through them.

    ============================================================================
    ⚙ IMPLEMENTATION BLUEPRINT
    ============================================================================

    Step 1

        Allocate

            buy[k+1]

            sell[k+1]

    Step 2

        Initialize every buy

        with first price.

    Step 3

        Scan every stock price.

    Step 4

        Iterate transactions
        from

            k

            down to

            1.

    Step 5

        Update Sell.

    Step 6

        Update Buy.

    Step 7

        Return sell[k].

    ============================================================================
    🧾 ULTRA COMPACT PSEUDOCODE
    ============================================================================

    initialize buy[]

    initialize sell[]

    for each price

        for transaction = k..1

            update sell

            update buy

    return sell[k]

    ============================================================================
    6. SOLUTION CLASSES
    ============================================================================
    static final class LC188BruteForce {

        /*
        Idea

            Enumerate every legal sequence of

                Buy

                Sell

            up to k transactions.

        Invariant

            Every valid trading sequence is explored.

        Limitation

            Exponential.

        Complexity

            Time  : Exponential

            Space : O(k)

        Interview usefulness

            Good for deriving the DP state.
        */

    static int maxProfit(int k, int[] prices) {
        return dfs(prices, 0, k, false);
    }

    private static int dfs(int[] prices,
                           int day,
                           int transactionsLeft,
                           boolean holding) {

        if (day == prices.length)
            return 0;

        if (transactionsLeft == 0 && !holding)
            return 0;

        int skip = dfs(
                prices,
                day + 1,
                transactionsLeft,
                holding);

        if (holding) {

            if (transactionsLeft == 0)
                return skip;

            int sell = prices[day]
                    + dfs(
                    prices,
                    day + 1,
                    transactionsLeft - 1,
                    false);

            return Math.max(skip, sell);

        } else {

            int buy = -prices[day]
                    + dfs(
                    prices,
                    day + 1,
                    transactionsLeft,
                    true);

            return Math.max(skip, buy);
        }
    }


    static final class LC188Optimal {

        /*
        Idea

            Compress

                2*k

            DP states into

                buy[]

                sell[]

        buy[i]

            Effective buying cost of the
            i-th transaction.

        sell[i]

            Maximum profit after completing
            the i-th transaction.

        Invariant

            After processing today's price,

            buy[i]

            and

            sell[i]

            are optimal for every i.

        Why Reverse Iteration?

            sell[i]

            depends on

                buy[i]

            buy[i]

            depends on

                sell[i-1]

            Therefore

            iterating backwards prevents
            today's updates from corrupting
            states still needed later.

        Complexity

            Time

                O(nk)

            Space

                O(k)

        Interview usefulness

            Canonical LC188 solution.
        */

        static int maxProfit(int k, int[] prices) {

            if (prices == null
                    || prices.length <= 1
                    || k == 0)
                return 0;

            /*
             * Optimization
             *
             * If k is large enough,
             * the problem degenerates
             * into LC122.
             */

            if (k >= prices.length / 2) {

                int answer = 0;

                for (int i = 1; i < prices.length; i++) {

                    answer += Math.max(
                            0,
                            prices[i] - prices[i - 1]);
                }

                return answer;
            }

            int[] buy = new int[k + 1];

            int[] sell = new int[k + 1];

            Arrays.fill(buy, prices[0]);

            for (int price : prices) {

                for (int transaction = k;
                     transaction >= 1;
                     transaction--) {

                    // 🟢 Invariant:
                    // Finish transaction today.

                    sell[transaction] = Math.max(
                            sell[transaction],
                            price - buy[transaction]);

                    // 🟢 Effective buying cost
                    // after previous transaction.

                    buy[transaction] = Math.min(
                            buy[transaction],
                            price - sell[transaction - 1]);
                }
            }

            return sell[k];
        }
    }

    /*
    ============================================================================
    🟣 INTERVIEW ARTICULATION
    ============================================================================

    Pattern

        Generalized State Compression DP

    State

        buy[i]

        sell[i]

    Search Space

        Every legal sequence containing
        at most

            k

        transactions.

    Invariant

        buy[i]

        stores the minimum effective cost
        of entering transaction i.

        sell[i]

        stores the maximum completed
        profit after transaction i.

    Discard Rule

        Any state worse than the
        current optimum is permanently
        dominated.

    Correctness

        Every legal transaction sequence
        passes through exactly one chain

            Buy1

            Sell1

            ...

            BuyK

            SellK

    Therefore

        the entire trading history
        can be represented by

            2*k

        compressed states.

    Termination

        After the final day

            sell[k]

        contains the answer.

    In-place Feasibility

        Yes.

    Streaming Feasibility

        Yes.

        Prices may arrive online.

        Only compressed states are needed.

    ============================================================================
    🎯 INTERVIEW RECALL SHEET
    ============================================================================

    Trigger

        At most

            k

        transactions.

    Pattern

        Generalized LC123.

    State

        buy[]

        sell[]

    Invariant

        One Buy and one Sell state
        for every transaction.

    Search Target

        sell[k]

    Common Trap

        Forgetting reverse iteration.

    Re-derivation Cue

        Replace

            buy1

            sell1

            buy2

            sell2

        with

            buy[i]

            sell[i]

        inside a loop.

    ============================================================================
    🔄 TRANSITION TO LC714
    ============================================================================

    LC188 changes

        the number of states.

    LC714 keeps

        only two states

    but modifies

        one transition.

    Selling now costs

        a fixed transaction fee.

    Therefore

        only the

            Sell

        transition changes.

    The DP framework itself
    remains identical.    /*
    ============================================================================
    ██████╗  █████╗ ██████╗ ████████╗
    ██╔══██╗██╔══██╗██╔══██╗╚══██╔══╝
    ██████╔╝███████║██████╔╝   ██║
    ██╔═══╝ ██╔══██║██╔══██╗   ██║
    ██║     ██║  ██║██║  ██║   ██║
    ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝

    LC714

    Best Time to Buy and Sell Stock with Transaction Fee

    Difficulty

        Medium

    Pattern

        Two-State DP

        State Compression

    ============================================================================
    📘 PRIMARY PROBLEM
    ============================================================================

    Problem

        You may perform unlimited transactions.

        Every completed sale pays

            fee.

        Hold at most one stock.

        Return the maximum obtainable profit.

    Example

        prices

            [1,3,2,8,4,9]

        fee

            2

        Answer

            8

    ============================================================================
    🔵 CORE PATTERN OVERVIEW
    ============================================================================

    Observation

        LC122 already solved

            unlimited transactions.

        The only change is

            selling

        now loses

            fee.

    Therefore

        only one DP transition changes.

    Old Transition

        sell

            =

        max(
            sell,
            price - buy
        )

    New Transition

        sell

            =

        max(
            sell,
            price - buy - fee
        )

    Nothing else changes.

    ============================================================================
    🟢 MENTAL MODEL
    ============================================================================

    Imagine that the exchange charges
    commission only when selling.

    Buying is unchanged.

    Selling simply receives

        price - fee

    instead of

        price.

    Therefore

        every profitable transaction
        becomes slightly less valuable.

    ============================================================================
    🟢 CORE INVARIANTS
    ============================================================================

    buy

        Minimum effective buying cost.

    --------------------------------

    sell

        Maximum completed profit.

    --------------------------------

    Selling transition

        always subtracts

            fee.

    --------------------------------

    Buying transition

        still uses

            completed profit

        exactly like LC122.

    ============================================================================
    🔴 WHY WRONG SOLUTIONS FAIL
    ============================================================================

    Mistake

        Deduct fee while buying.

    Why it appears reasonable

        Every transaction has one buy
        and one sell.

    Why it is wrong

        The problem explicitly charges
        the fee only when selling.

    Another valid formulation deducts
    the fee while buying,

    but then every recurrence changes.

    Mixing formulations breaks the invariant.

    ============================================================================
    ⚙ IMPLEMENTATION BLUEPRINT
    ============================================================================

    Initialize

        buy

            first price

        sell

            zero

    For every price

        Update sell.

        Update effective buy.

    Return sell.

    ============================================================================
    🧾 ULTRA COMPACT PSEUDOCODE
    ============================================================================

    buy = first

    sell = 0

    for every price

        sell = max(sell, price-buy-fee)

        buy = min(buy, price-sell)

    return sell

    ============================================================================
    6. SOLUTION CLASSES
    ============================================================================
    */

    static final class LC714Optimal {

        /*
        Idea

            Same DP as LC122.

            Selling pays a fee.

        Invariant

            buy

                minimum effective buying cost.

            sell

                maximum completed profit.

        Complexity

            Time  : O(n)

            Space : O(1)

        Interview usefulness

            Canonical LC714 solution.
        */

        static int maxProfit(int[] prices,
                             int fee) {

            if (prices == null
                    || prices.length <= 1)
                return 0;

            int buy = prices[0];

            int sell = 0;

            for (int price : prices) {

                // 🟢 Complete today's sale.

                sell = Math.max(
                        sell,
                        price - buy - fee);

                // 🟢 Effective buying cost.

                buy = Math.min(
                        buy,
                        price - sell);
            }

            return sell;
        }
    }

    /*
    ============================================================================
    ALTERNATIVE CASH / HOLD FORMULATION
    ============================================================================

    Some interviewers prefer explicit states.

    cash

        Maximum wealth while
        holding no stock.

    hold

        Maximum wealth while
        holding one stock.

    Transition

        cash

            =

        max(
            cash,
            hold + price - fee
        )

        hold

            =

        max(
            hold,
            cash - price
        )

    Both formulations are mathematically
    equivalent.

    Effective-buy formulation is easier
    to derive from LC122.

    ============================================================================
    🟣 INTERVIEW ARTICULATION
    ============================================================================

    Pattern

        Two-State DP

    Difference from LC122

        Only one transition changes.

    Invariant

        Fee affects completed sales only.

    Search Space

        Unlimited legal transactions.

    Discard Rule

        Higher effective buying cost
        is permanently dominated.

    Correctness

        Every transaction pays the fee
        exactly once.

    Termination

        Final

            sell

        equals maximum obtainable profit.

    ============================================================================
    🎯 INTERVIEW RECALL SHEET
    ============================================================================

    Trigger

        Unlimited transactions
        plus fee.

    State

        buy

        sell

    Modification

        Subtract fee while selling.

    One-liner

        LC122 + fee on Sell transition.

    Re-derivation Cue

        Change exactly one equation.    /*
    ============================================================================
    🔄 TRANSITION TO LC309
    ============================================================================

    LC714 modified

        the Sell transition.

    LC309 modifies

        the Buy transition.

    New Constraint

        After selling,

        you cannot buy on the next day.

    Therefore

        Buy

        can no longer depend on

            yesterday's Sell.

    Instead

        Buy must depend on

            Sell from two days ago.

    This introduces one additional compressed state.

    ============================================================================
    ██████╗  █████╗ ██████╗ ████████╗
    ██╔══██╗██╔══██╗██╔══██╗╚══██╔══╝
    ██████╔╝███████║██████╔╝   ██║
    ██╔═══╝ ██╔══██║██╔══██╗   ██║
    ██║     ██║  ██║██║  ██║   ██║
    ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝

    LC309

    Best Time to Buy and Sell Stock with Cooldown

    Difficulty

        Medium

    Pattern

        Two-State DP

        State Compression

        Cooldown State

    ============================================================================
    📘 PRIMARY PROBLEM
    ============================================================================

    Problem

        You may perform unlimited transactions.

    Constraint

        After selling,

        you must wait exactly one day
        before buying again.

    Goal

        Maximize total profit.

    Example

        prices

            [1,2,3,0,2]

        Transactions

            Buy

            Sell

            Cooldown

            Buy

            Sell

        Answer

            3

    ============================================================================
    🔵 CORE PATTERN OVERVIEW
    ============================================================================

    Compared with LC122

        nothing changes except

            Buy.

    Why?

    Because buying immediately after selling
    violates the cooldown rule.

    Therefore

        Buy

        depends on

            Sell

        from

            two days earlier.

    ============================================================================
    🟢 MENTAL MODEL
    ============================================================================

    Imagine every Sell transaction
    creates a one-day lock.

    During that day

        buying is illegal.

    Therefore

        buying cannot consume
        yesterday's Sell state.

    It must consume

        the previous completed Sell
        before cooldown.

    ============================================================================
    🟢 CORE INVARIANTS
    ============================================================================

    State

        buy

            Effective buying cost.

    --------------------------------

    State

        sell

            Best completed profit.

    --------------------------------

    State

        previousSell

            Yesterday's completed Sell
            before updating today.

    --------------------------------

    Buying Transition

        Uses

            previousSell

        instead of

            current sell.

    ============================================================================
    🔴 WHY WRONG SOLUTIONS FAIL
    ============================================================================

    Mistake

        Reuse LC122.

    Counterexample

        prices

            [1,2,3,0,2]

    LC122

        allows buying immediately
        after selling.

    LC309

        explicitly forbids it.

    Therefore

        LC122 overestimates profit.

    ============================================================================
    ⚙ IMPLEMENTATION BLUEPRINT
    ============================================================================

    Initialize

        buy

        sell

        previousSell

    For every price

        Cache today's sell.

        Update sell.

        Update buy
            using previousSell.

        Shift previousSell.

    Return sell.

    ============================================================================
    🧾 ULTRA COMPACT PSEUDOCODE
    ============================================================================

    buy = first

    sell = 0

    prev = 0

    for price

        cache = sell

        sell = max(sell,
                   price-buy)

        buy = min(buy,
                  price-prev)

        prev = cache

    return sell

    ============================================================================
    6. SOLUTION CLASSES
    ============================================================================
    */

    static final class LC309Optimal {

        /*
        Idea

            Add one compressed state
            representing the Sell state
            before cooldown.

        Invariant

            buy

                effective buying cost.

            sell

                maximum completed profit.

            previousSell

                Sell state available for
                legal buying today.

        Complexity

            Time  : O(n)

            Space : O(1)

        Interview usefulness

            Canonical LC309 solution.
        */

        static int maxProfit(int[] prices) {

            if (prices == null
                    || prices.length <= 1)
                return 0;

            int buy = prices[0];

            int sell = 0;

            int previousSell = 0;

            for (int price : prices) {

                int cachedSell = sell;

                // 🟢 Finish today's transaction.

                sell = Math.max(
                        sell,
                        price - buy);

                // 🟢 Buy only after respecting cooldown.

                buy = Math.min(
                        buy,
                        price - previousSell);

                // 🟢 Advance cooldown state.

                previousSell = cachedSell;
            }

            return sell;
        }
    }

    /*
    ============================================================================
    🟣 INTERVIEW ARTICULATION
    ============================================================================

    Pattern

        State Compression DP

    Difference from LC122

        Buy transition changes.

    New State

        previousSell

    Invariant

        Buying must never use
        yesterday's completed sale.

    Correctness

        Cooldown is enforced by
        delaying the Sell state
        available to Buy.

    Search Space

        All legal transaction sequences
        respecting cooldown.

    Termination

        Final

            sell

        stores the optimal answer.

    In-place Feasibility

        Yes.

    Streaming Feasibility

        Yes.

        Only three compressed states
        are maintained.    /*
    ============================================================================
    🎯 INTERVIEW RECALL SHEET
    ============================================================================

    Trigger

        Unlimited transactions
        plus one-day cooldown.

    Pattern

        State Compression DP.

    States

        buy

        sell

        previousSell

    Search Target

        Final

            sell

    Common Trap

        Using today's Sell state
        immediately while buying.

    One-liner

        LC122 + delayed Buy transition.

    Re-derivation Cue

        Cooldown delays exactly one dependency.

    ============================================================================
    🔄 UNIFIED STOCK DP FRAMEWORK
    ============================================================================

    Every stock problem in LeetCode is generated
    by modifying one of three dimensions.

    ---------------------------------------------------------------------

    Dimension 1

        Number of Transactions

    Examples

        LC121

            One

        LC123

            Two

        LC188

            K

    ---------------------------------------------------------------------

    Dimension 2

        Sell Constraint

    Example

        LC714

            Fee

    ---------------------------------------------------------------------

    Dimension 3

        Buy Constraint

    Example

        LC309

            Cooldown

    ---------------------------------------------------------------------

    Nothing else fundamentally changes.

    ============================================================================
    ⚫ MASTER STATE DIAGRAM
    ============================================================================

                      BUY1
                        |
                        v
                      SELL1
                        |
                        v
                      BUY2
                        |
                        v
                      SELL2
                        |
                       ...
                        |
                        v
                      BUYK
                        |
                        v
                      SELLK

    Constraints simply modify transitions.

    ---------------------------------------------------------------------

    Fee

        SELL

            loses fee.

    ---------------------------------------------------------------------

    Cooldown

        BUY

            waits one day.

    ============================================================================
    STATE TRANSITION TABLE
    ============================================================================

    -------------------------------------------------------------------------
    Problem

        LC121

    Buy

        Minimum price.

    Sell

        price-buy

    -------------------------------------------------------------------------

    Problem

        LC122

    Buy

        price-profit

    Sell

        price-buy

    -------------------------------------------------------------------------

    Problem

        LC123

    Buy

        price-sell1

    Sell

        price-buy2

    -------------------------------------------------------------------------

    Problem

        LC188

    Buy

        price-sell[i-1]

    Sell

        price-buy[i]

    -------------------------------------------------------------------------

    Problem

        LC714

    Buy

        price-profit

    Sell

        price-buy-fee

    -------------------------------------------------------------------------

    Problem

        LC309

    Buy

        price-previousSell

    Sell

        price-buy

    -------------------------------------------------------------------------

    Observe

        Every recurrence differs by

            one dependency

        or

            one arithmetic term.

    ============================================================================
    PATTERN BOUNDARIES
    ============================================================================

    Use this family whenever

        • chronological ordering matters

        • buy before sell

        • one stock at a time

        • maximize profit

    -------------------------------------------------------------------------

    Do NOT use this framework for

        • unlimited inventory

        • multiple simultaneous holdings

        • short selling

        • arbitrary graph optimization

        • interval scheduling

    ============================================================================
    DEBUGGING CHECKLIST
    ============================================================================

    If the answer is incorrect,
    verify the following.

    □ Empty input handled.

    □ Single element handled.

    □ Buy initialized correctly.

    □ Sell initialized to zero.

    □ Reverse iteration used for LC188.

    □ Fee deducted exactly once.

    □ Cooldown state delayed exactly one day.

    □ Returning Sell state,
      not Buy state.

    □ Correct transaction ordering.

    ============================================================================
    IMPLEMENTATION RECONSTRUCTION
    ============================================================================

    LC188

        sell[k]

            ↓

        buy[k]

            ↓

        ...

            ↓

        sell[1]

            ↓

        buy[1]

    -------------------------------------------------------------------------

    LC714

        sell

            ↓

        buy

    -------------------------------------------------------------------------

    LC309

        cache

            ↓

        sell

            ↓

        buy

            ↓

        previousSell

    -------------------------------------------------------------------------

    Memorize

        State

            →

        Transition

            →

        Invariant

    Never memorize code.

    ============================================================================
    COMPARISON TABLE
    ============================================================================

    -------------------------------------------------------------------------
    Problem      States     Time       Space
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

        2k

        O(nk)

        O(k)

    -------------------------------------------------------------------------

    LC714

        2

        O(n)

        O(1)

    -------------------------------------------------------------------------

    LC309

        3

        O(n)

        O(1)

    ============================================================================

         /*
    ============================================================================
    RE-DERIVATION ALGORITHM
    ============================================================================

    During an interview, never recall code first.

    Instead answer these five questions.

    -------------------------------------------------------------------------

    Question 1

        What are my DP states?

    -------------------------------------------------------------------------

    Question 2

        What does each state represent?

        (Always define the invariant first.)

    -------------------------------------------------------------------------

    Question 3

        Which previous state can legally transition
        into the current state?

    -------------------------------------------------------------------------

    Question 4

        Is there any additional constraint?

            • fee

            • cooldown

            • transaction limit

    -------------------------------------------------------------------------

    Question 5

        Can historical information be compressed?

    If yes,

        compress states.

    Otherwise,

        keep the DP table.

    ============================================================================
    GENERIC STOCK DP TEMPLATE
    ============================================================================

    Generic Buy Transition

        buy

            =

        minimum effective buying cost.

    ---------------------------------------------------------------

    Generic Sell Transition

        sell

            =

        maximum completed profit.

    ---------------------------------------------------------------

    General Form

        sell

            depends on

                corresponding buy

        buy

            depends on

                previous sell

    Every stock problem merely modifies

        one dependency

    or

        one arithmetic term.

    ============================================================================
    CHEAT SHEET
    ============================================================================

    LC121

        Buy

            raw minimum price

    --------------------------------------------

    LC122

        Buy

            price - profit

    --------------------------------------------

    LC123

        Duplicate LC122 once.

    --------------------------------------------

    LC188

        Replace variables

            with arrays.

    --------------------------------------------

    LC714

        Charge fee while selling.

    --------------------------------------------

    LC309

        Delay Buy by one Sell state.

    ============================================================================
    INTERVIEW QUESTIONS
    ============================================================================

    Q

        Why is LC188 O(k) memory?

    A

        Because each transaction only depends
        on the immediately previous transaction.

    -------------------------------------------------------------------------

    Q

        Why reverse iterate transactions?

    A

        Prevent overwriting states that are still
        required during today's computation.

    -------------------------------------------------------------------------

    Q

        Why subtract fee while selling?

    A

        Because the problem charges fee
        on completed sales.

    -------------------------------------------------------------------------

    Q

        Why does cooldown require
        another state?

    A

        Buying cannot legally consume
        yesterday's Sell state.

    -------------------------------------------------------------------------

    Q

        Which problem is easiest?

    A

        LC121

        because there are only two states.

    -------------------------------------------------------------------------

    Q

        Which problem generalizes all others?

    A

        LC188.

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

        Duplicate States.

    -------------------------------------------------------------------------

    LC188

        Arrays of States.

    -------------------------------------------------------------------------

    LC714

        Sell Pays Fee.

    -------------------------------------------------------------------------

    LC309

        Buy Waits One Day.

    ============================================================================
    MASTERY CHECKLIST
    ============================================================================

    □ I know every DP state.

    □ I know every invariant.

    □ I know every transition.

    □ I know why state compression works.

    □ I know why LC188 iterates backwards.

    □ I know why LC714 modifies Sell.

    □ I know why LC309 modifies Buy.

    □ I can derive every recurrence.

    □ I can reconstruct every implementation.

    □ I understand the pattern boundaries.

    ============================================================================
    FINAL PATTERN SUMMARY
    ============================================================================

    There is only one Stock DP pattern.

    Every LeetCode stock problem modifies

        Number of States

            OR

        Sell Transition

            OR

        Buy Transition.

    Once the invariant is understood,

    every implementation becomes
    a mechanical translation of the state machine.

    ============================================================================
    MAIN + SELF-VERIFYING TESTS
    ============================================================================
    */

    public static void main(String[] args) {

        /*
         * ============================================================
         * LC188
         * ============================================================
         */

        // Official example.
        assert LC188Optimal.maxProfit(
                2,
                new int[]{2, 4, 1}) == 2;

        // Official example 2.
        assert LC188Optimal.maxProfit(
                2,
                new int[]{3, 2, 6, 5, 0, 3}) == 7;

        // No transactions allowed.
        assert LC188Optimal.maxProfit(
                0,
                new int[]{1, 5, 2}) == 0;

        // Empty prices.
        assert LC188Optimal.maxProfit(
                2,
                new int[]{}) == 0;

        // Increasing prices.
        assert LC188Optimal.maxProfit(
                2,
                new int[]{1, 2, 3, 4, 5}) == 4;

        // Decreasing prices.
        assert LC188Optimal.maxProfit(
                3,
                new int[]{5, 4, 3, 2, 1}) == 0;

        /*
         * ============================================================
         * LC714
         * ============================================================
         */

        // Official example.
        assert LC714Optimal.maxProfit(
                new int[]{1, 3, 2, 8, 4, 9},
                2) == 8;

        // Fee = 0 behaves like LC122.
        assert LC714Optimal.maxProfit(
                new int[]{1, 2, 3, 4, 5},
                0) == 4;

        // Fee too large.
        assert LC714Optimal.maxProfit(
                new int[]{1, 3, 5},
                10) == 0;

        // Decreasing prices.
        assert LC714Optimal.maxProfit(
                new int[]{5, 4, 3, 2},
                2) == 0;

        /*
         * ============================================================
         * LC309
         * ============================================================
         */

        // Official example.
        assert LC309Optimal.maxProfit(
                new int[]{1, 2, 3, 0, 2}) == 3;

        // Increasing prices.
        assert LC309Optimal.maxProfit(
                new int[]{1, 2, 3, 4, 5}) == 4;

        // Decreasing prices.
        assert LC309Optimal.maxProfit(
                new int[]{5, 4, 3, 2, 1}) == 0;

        // Single element.
        assert LC309Optimal.maxProfit(
                new int[]{5}) == 0;

        // Empty array.
        assert LC309Optimal.maxProfit(
                new int[]{}) == 0;

        System.out.println("All StockSeries2 assertions passed.");
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