package org.chijai.trading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ===============================================================
 * Problem: Running Totals / Position Tracking
 * ===============================================================
 *
 * You are given a stream of trades.
 *
 * Each trade contains:
 *
 *     accountId
 *     quantity
 *     price
 *
 * Example:
 *
 *     ("ACC1", 10, 100)
 *
 * means account ACC1 bought 10 units at a price of 100.
 *
 *
 * Quantity is SIGNED:
 *
 *     positive quantity -> BUY
 *     negative quantity -> SELL
 *
 * Therefore:
 *
 *     ("ACC1", 10, 100)
 *
 * means:
 *
 *     BUY 10 @ 100
 *
 * and:
 *
 *     ("ACC1", -4, 120)
 *
 * means:
 *
 *     SELL 4 @ 120
 *
 *
 * Trades may arrive in any order and the same account may appear
 * many times throughout the stream.
 *
 * Example:
 *
 *     ACC1
 *     ACC2
 *     ACC1
 *     ACC3
 *     ACC2
 *     ACC1
 *
 *
 * For every account, calculate:
 *
 *     1. Net quantity / net position
 *     2. Total signed cost
 *
 *
 * Net quantity is:
 *
 *     sum of all signed quantities
 *
 *
 * Total signed cost is:
 *
 *     sum(quantity * tradePrice)
 *
 *
 * If a current market price is also supplied, calculate:
 *
 *     mark-to-market P&L
 *
 * using:
 *
 *     P&L = netQty * currentMarketPrice - totalCost
 *
 *
 * ----------------------------------------------------------------
 * EXAMPLE 1 — SINGLE ACCOUNT
 * ----------------------------------------------------------------
 *
 * Input trades:
 *
 *     ACC1, +10, 100
 *     ACC1,  -4, 120
 *
 *
 * After the first trade:
 *
 *     netQty
 *     = 10
 *
 *     totalCost
 *     = 10 * 100
 *     = 1000
 *
 *
 * After the second trade:
 *
 *     netQty
 *     = 10 + (-4)
 *     = 6
 *
 *     totalCost
 *     = 1000 + (-4 * 120)
 *     = 1000 - 480
 *     = 520
 *
 *
 * Output:
 *
 *     ACC1 -> [6, 520]
 *
 *
 * Meaning:
 *
 *     ACC1 currently holds 6 units
 *     and its accumulated signed cost is 520.
 *
 *
 * ----------------------------------------------------------------
 * EXAMPLE 2 — MULTIPLE ACCOUNTS
 * ----------------------------------------------------------------
 *
 * Input trades:
 *
 *     ACC1, +10, 100
 *     ACC2,  +5, 200
 *     ACC1,  -4, 120
 *     ACC2,  +3, 210
 *     ACC1,  +2,  90
 *
 *
 * ACC1:
 *
 *     netQty
 *     = 10 - 4 + 2
 *     = 8
 *
 *     totalCost
 *     = 10 * 100
 *       - 4 * 120
 *       + 2 * 90
 *
 *     = 1000 - 480 + 180
 *     = 700
 *
 *
 * ACC2:
 *
 *     netQty
 *     = 5 + 3
 *     = 8
 *
 *     totalCost
 *     = 5 * 200
 *       + 3 * 210
 *
 *     = 1000 + 630
 *     = 1630
 *
 *
 * Output:
 *
 *     ACC1 -> [8, 700]
 *     ACC2 -> [8, 1630]
 *
 *
 * ----------------------------------------------------------------
 * EXAMPLE 3 — ACCOUNT BECOMES FLAT
 * ----------------------------------------------------------------
 *
 * Input trades:
 *
 *     ACC1, +10, 100
 *     ACC1, -10, 120
 *
 *
 * Net quantity:
 *
 *     10 - 10
 *     = 0
 *
 *
 * Total signed cost:
 *
 *     10 * 100
 *     + (-10 * 120)
 *
 *     = 1000 - 1200
 *     = -200
 *
 *
 * Output:
 *
 *     ACC1 -> [0, -200]
 *
 *
 * The account has no remaining position.
 *
 * If we use the mark-to-market formula:
 *
 *     P&L
 *     = 0 * currentMarketPrice - (-200)
 *     = 200
 *
 * The result no longer depends on the current market price because
 * the position is completely closed.
 *
 *
 * ----------------------------------------------------------------
 * EXAMPLE 4 — MARK-TO-MARKET P&L
 * ----------------------------------------------------------------
 *
 * Suppose after processing all trades:
 *
 *     ACC1 -> [6, 520]
 *
 * and the current market price is:
 *
 *     110
 *
 *
 * Current market value:
 *
 *     6 * 110
 *     = 660
 *
 *
 * P&L:
 *
 *     660 - 520
 *     = 140
 *
 *
 * Output:
 *
 *     ACC1 P&L = 140
 *
 *
 * ----------------------------------------------------------------
 * EXPECTED DATA STRUCTURE
 * ----------------------------------------------------------------
 *
 * Use:
 *
 *     HashMap<String, long[]>
 *
 * where:
 *
 *     key
 *         = accountId
 *
 *     value[0]
 *         = netQty
 *
 *     value[1]
 *         = totalCost
 *
 *
 * Example:
 *
 *     ACC1 -> [8, 700]
 *     ACC2 -> [8, 1630]
 *
 *
 * ----------------------------------------------------------------
 * CONSTRAINT / INTERVIEW ASSUMPTIONS
 * ----------------------------------------------------------------
 *
 * Unless stated otherwise:
 *
 *     - trades are processed in the order received
 *     - accountId is non-null
 *     - quantity may be positive or negative
 *     - price is non-negative
 *     - long is used to reduce overflow risk compared with int
 *     - HashMap operations are expected O(1)
 *
 *
 * If prices contain decimals, use an exact representation such as:
 *
 *     cents
 *     paise
 *     ticks
 *
 * instead of double when exact financial arithmetic matters.
 *
 *
 * ----------------------------------------------------------------
 * CLASSIFICATION
 * ----------------------------------------------------------------
 *
 * Primary pattern:
 *
 *     HashMap aggregation + running totals
 *
 * Also belongs to:
 *
 *     - Stream processing
 *     - Group-by aggregation
 *     - Position tracking
 *     - Stateful accumulation
 *
 *
 * ----------------------------------------------------------------
 * FIRST-PRINCIPLES INVENTION PATH
 * ----------------------------------------------------------------
 *
 * We receive trades one by one.
 *
 * The same account may appear many times:
 *
 *     A
 *     B
 *     A
 *     C
 *     B
 *     A
 *
 * For every account we only need to remember its current accumulated state.
 *
 * Therefore:
 *
 *     accountId -> [netQty, totalCost]
 *
 * A HashMap gives us the account's current state in expected O(1) time.
 *
 * For every trade:
 *
 *     netQty    += quantity
 *     totalCost += quantity * price
 *
 *
 * IMPORTANT ASSUMPTION:
 *
 * Quantity is SIGNED.
 *
 *     BUY  10 @ 100  -> quantity = +10
 *     SELL  4 @ 120  -> quantity = -4
 *
 *
 * ----------------------------------------------------------------
 * WHY long[]?
 * ----------------------------------------------------------------
 *
 * positions.get(accountId) returns:
 *
 *     long[0] -> netQty
 *     long[1] -> totalCost
 *
 * This is compact and useful when the interview specifically asks for:
 *
 *     HashMap<String, long[]>
 *
 * In production code, a named Position class is usually more readable.
 *
 *
 * ----------------------------------------------------------------
 * PRICE REPRESENTATION
 * ----------------------------------------------------------------
 *
 * This example uses long price values.
 *
 * If prices contain decimals, store them in the smallest fixed unit:
 *
 *     ₹123.45 -> 12345 paise
 *
 * or use BigDecimal when appropriate.
 *
 * Avoid double for exact financial accounting.
 */
public class RunningTotalsPositionTracking {

    /**
     * =============================================================
     * PRIMARY SOLUTION
     * =============================================================
     *
     * Map:
     *
     *     accountId -> [netQty, totalCost]
     *
     *
     * Time:
     *
     *     O(n)
     *
     * because each of n trades is processed once and each HashMap lookup/update
     * is expected O(1).
     *
     *
     * Space:
     *
     *     O(k)
     *
     * where k is the number of distinct accounts.
     */
    static Map<String, long[]> calculatePositions(List<Trade> trades) {

        Map<String, long[]> positions = new HashMap<>();

        for (Trade trade : trades) {

            /*
             * If this is the first trade for the account,
             * create:
             *
             *     [0, 0]
             *
             * meaning:
             *
             *     netQty    = 0
             *     totalCost = 0
             */
            long[] position = positions.computeIfAbsent(
                    trade.accountId,
                    accountId -> new long[2]
            );

            /*
             * Running net position.
             *
             * BUY quantities are positive.
             * SELL quantities are negative.
             */
            position[0] += trade.quantity;

            /*
             * Running signed cash cost.
             *
             * BUY:
             *
             *     +quantity * price
             *
             * increases totalCost.
             *
             * SELL:
             *
             *     -quantity * price
             *
             * decreases totalCost.
             */
            position[1] += trade.quantity * trade.price;
        }

        return positions;
    }


    /**
     * =============================================================
     * MARK-TO-MARKET P&L
     * =============================================================
     *
     * Given:
     *
     *     netQty
     *     totalCost
     *     currentMarketPrice
     *
     * then:
     *
     *     currentValue = netQty * currentMarketPrice
     *
     * and:
     *
     *     P&L = currentValue - totalCost
     *
     *
     * Example:
     *
     *     BUY   10 @ 100
     *     SELL   4 @ 120
     *
     *     netQty
     *     = 10 - 4
     *     = 6
     *
     *     totalCost
     *     = 10 * 100 + (-4 * 120)
     *     = 1000 - 480
     *     = 520
     *
     * If market price = 110:
     *
     *     currentValue
     *     = 6 * 110
     *     = 660
     *
     *     P&L
     *     = 660 - 520
     *     = 140
     */
    static long calculatePnl(long[] position, long currentMarketPrice) {

        long netQty = position[0];
        long totalCost = position[1];

        return netQty * currentMarketPrice - totalCost;
    }


    /**
     * =============================================================
     * DRY RUN
     * =============================================================
     *
     * Trades:
     *
     *     A, +10, 100
     *     B,  +5, 200
     *     A,  -4, 120
     *     A,  +2,  90
     *
     *
     * Start:
     *
     *     {}
     *
     *
     * Trade 1:
     *
     *     A, +10, 100
     *
     *     A -> [
     *              10,
     *              1000
     *          ]
     *
     *
     * Trade 2:
     *
     *     B, +5, 200
     *
     *     A -> [10, 1000]
     *     B -> [ 5, 1000]
     *
     *
     * Trade 3:
     *
     *     A, -4, 120
     *
     *     netQty:
     *
     *         10 + (-4) = 6
     *
     *     totalCost:
     *
     *         1000 + (-4 * 120)
     *         = 520
     *
     *     A -> [6, 520]
     *
     *
     * Trade 4:
     *
     *     A, +2, 90
     *
     *     netQty:
     *
     *         6 + 2 = 8
     *
     *     totalCost:
     *
     *         520 + 180 = 700
     *
     *     A -> [8, 700]
     *
     *
     * Final:
     *
     *     A -> [8, 700]
     *     B -> [5, 1000]
     */
    public static void main(String[] args) {

        List<Trade> trades = List.of(
                new Trade("A", 10, 100),
                new Trade("B", 5, 200),
                new Trade("A", -4, 120),
                new Trade("A", 2, 90)
        );

        Map<String, long[]> positions = calculatePositions(trades);

        for (Map.Entry<String, long[]> entry : positions.entrySet()) {

            String accountId = entry.getKey();
            long[] position = entry.getValue();

            System.out.println(
                    accountId
                            + " -> netQty=" + position[0]
                            + ", totalCost=" + position[1]
            );
        }

        long marketPriceForA = 110;

        long pnlA = calculatePnl(
                positions.get("A"),
                marketPriceForA
        );

        System.out.println(
                "A -> P&L at market price "
                        + marketPriceForA
                        + " = "
                        + pnlA
        );
    }


    /**
     * Simple immutable trade representation.
     *
     * quantity:
     *
     *     positive -> BUY
     *     negative -> SELL
     *
     * price:
     *
     *     assumed to already be stored in an exact integer unit
     *     such as cents, paise, or ticks when required.
     */
    static class Trade {

        String accountId;
        long quantity;
        long price;

        Trade(String accountId, long quantity, long price) {
            this.accountId = accountId;
            this.quantity = quantity;
            this.price = price;
        }
    }


    /*
     * =============================================================
     * IMPORTANT INTERVIEW CAVEAT
     * =============================================================
     *
     * [netQty, totalCost] is enough for:
     *
     *     mark-to-market P&L
     *
     * using:
     *
     *     netQty * currentMarketPrice - totalCost
     *
     *
     * But it does NOT by itself maintain separate:
     *
     *     realized P&L
     *     unrealized P&L
     *
     * under accounting methods such as:
     *
     *     FIFO
     *     LIFO
     *     average cost
     *
     *
     * If the interviewer asks:
     *
     *     "How much P&L has already been realized by closed trades?"
     *
     * then more state is required.
     *
     * That becomes a richer position-accounting problem rather than
     * only a two-running-total aggregation problem.
     *
     *
     * =============================================================
     * CORE RECALL ANCHOR
     * =============================================================
     *
     * Repeated key in a stream
     *
     *         ↓
     *
     * HashMap
     *
     *         ↓
     *
     * Store the minimum running state needed for that key
     *
     *         ↓
     *
     * Update it once per event
     *
     *
     * Here:
     *
     *     accountId -> [netQty, totalCost]
     *
     * This same pattern appears in:
     *
     *     customer -> totalSpend
     *     symbol   -> netPosition
     *     product  -> unitsSold
     *     user     -> runningScore
     *     account  -> cashBalance
     */
}
