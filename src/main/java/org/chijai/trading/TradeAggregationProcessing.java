package org.chijai.trading;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
 * ================================================================
 * DATA PROCESSING PATTERN
 * Parse trades -> aggregate by key -> return sorted results
 * ================================================================
 *
 * CLASSIFICATION
 * ------------------------------------------------
 * Primary:
 * - Data Processing
 * - HashMap / Aggregation
 * - Sorting
 *
 * Secondary:
 * - Parsing
 * - TreeMap
 * - SQL-style GROUP BY + ORDER BY thinking
 *
 *
 * REUSABLE INTERVIEW PATTERN
 * ------------------------------------------------
 *
 *      raw trades
 *          |
 *          v
 *        parse
 *          |
 *          v
 *   HashMap<Key, Aggregate>
 *          |
 *          v
 *         sort
 *          |
 *          v
 *        result
 *
 *
 * SQL MENTAL MODEL
 * ------------------------------------------------
 *
 * SELECT account_id, SUM(quantity)
 * FROM trades
 * GROUP BY account_id
 * ORDER BY account_id;
 *
 *
 * CORE DECISION
 * ------------------------------------------------
 *
 * GROUP BY something
 *      -> HashMap<Key, AggregateObject>
 *
 * ORDER BY key
 *      -> TreeMap
 *         OR sort the final aggregate objects by key
 *
 * ORDER BY computed value
 *      -> sort quantityByAccount.values()
 *
 *
 * ================================================================
 * PROBLEM STATEMENT
 * ================================================================
 *
 * You are given trade records.
 *
 * Each trade contains:
 *
 *      accountId
 *      symbol
 *      quantity
 *      price
 *
 * Aggregate total traded quantity by account.
 *
 * Return the aggregated results sorted by accountId.
 *
 *
 * Example:
 *
 * Input:
 *
 *      A1,AAPL,10,100
 *      A2,MSFT,5,200
 *      A1,AAPL,20,110
 *      A3,GOOG,7,150
 *      A2,TSLA,8,250
 *
 *
 * Aggregation:
 *
 *      A1 -> 30
 *      A2 -> 13
 *      A3 -> 7
 *
 *
 * Sorted result:
 *
 *      A1 -> 30
 *      A2 -> 13
 *      A3 -> 7
 *
 *
 * ================================================================
 * FIRST-PRINCIPLES INVENTION PATH
 * ================================================================
 *
 * Ask:
 *
 * 1. For every incoming trade, what information must survive?
 *
 *      The running total quantity for that account.
 *
 *
 * 2. What gives fast lookup by accountId?
 *
 *      HashMap<String, Long>
 *
 *
 * 3. What happens when another trade for the same account arrives?
 *
 *      Add its quantity to the existing total.
 *
 *
 * 4. When do we need sorting?
 *
 *      Only after all aggregation is complete.
 *
 *
 * Therefore:
 *
 *      one pass for aggregation
 *      +
 *      one final sorting step
 *
 *
 * This is usually preferable to maintaining sorted order throughout
 * processing because sorting is only required for the final output.
 *
 *
 * ================================================================
 * PRIMARY SOLUTION 1
 * HASHMAP AGGREGATION + SORT ONCE AT THE END
 * ================================================================
 *
 * Best default interview solution when:
 *
 * - we only need sorted output at the end
 * - we want O(1) average aggregation lookup
 * - we may later need to sort by something other than the key
 *
 */
public class TradeAggregationProcessing {

    static class Trade {
        String accountId;
        String symbol;
        long quantity;
        double price;

        Trade(String accountId, String symbol, long quantity, double price) {
            this.accountId = accountId;
            this.symbol = symbol;
            this.quantity = quantity;
            this.price = price;
        }

        @Override
        public String toString() {
            return accountId + "," + symbol + "," + quantity + "," + price;
        }
    }

    static class AccountAggregate {
        String accountId;
        long totalQuantity;

        AccountAggregate(String accountId) {
            this.accountId = accountId;
        }

        void addQuantity(long quantity) {
            totalQuantity += quantity;
        }

        long totalQuantity() {
            return totalQuantity;
        }

        @Override
        public String toString() {
            return accountId + " -> " + totalQuantity;
        }
    }


    /*
     * ================================================================
     * PRIMARY SOLUTION 1
     * HashMap -> List -> sort by key
     * ================================================================
     *
     * Why this is the easiest reusable default:
     *
     * During processing:
     *
     *      accountId -> running total quantity
     *
     * HashMap gives average O(1) lookup/update.
     *
     * Only after processing all trades do we pay for sorting.
     *
     */
    static List<AccountAggregate> aggregateAndSortByAccount(List<Trade> trades) {

        Map<String, AccountAggregate> quantityByAccount =
                new HashMap<>();

        for (Trade trade : trades) {

            AccountAggregate aggregate =
                    quantityByAccount.get(trade.accountId);

            if (aggregate == null) {
                aggregate = new AccountAggregate(trade.accountId);
                quantityByAccount.put(
                        trade.accountId,
                        aggregate);
            }

            aggregate.addQuantity(trade.quantity);
        }

        List<AccountAggregate> result =
                new ArrayList<>(quantityByAccount.values());

        result.sort(
                Comparator.comparing(
                        aggregate -> aggregate.accountId));

        return result;
    }


    /*
     * ================================================================
     * PRIMARY SOLUTION 2
     * TreeMap directly
     * ================================================================
     *
     * Use when:
     *
     * - output must be sorted by key
     * - sorted-key behavior is central to the problem
     * - you want the map itself to remain sorted while processing
     *
     *
     * Trade-off:
     *
     * HashMap update:
     *
     *      average O(1)
     *
     * TreeMap update:
     *
     *      O(log k)
     *
     * where k = number of distinct accounts.
     *
     */
    static Map<String, AccountAggregate> aggregateUsingTreeMap(List<Trade> trades) {

        Map<String, AccountAggregate> quantityByAccount =
                new TreeMap<>();

        for (Trade trade : trades) {

            AccountAggregate aggregate =
                    quantityByAccount.get(trade.accountId);

            if (aggregate == null) {
                aggregate = new AccountAggregate(trade.accountId);
                quantityByAccount.put(
                        trade.accountId,
                        aggregate);
            }

            aggregate.addQuantity(trade.quantity);
        }

        return quantityByAccount;
    }


    /*
     * ================================================================
     * COMMON FOLLOW-UP
     * Sort by aggregate VALUE instead of key
     * ================================================================
     *
     * Example:
     *
     * Return accounts from largest total quantity to smallest.
     *
     *
     * IMPORTANT:
     *
     * TreeMap sorts by KEY.
     *
     * It does NOT naturally solve:
     *
     *      ORDER BY totalQuantity DESC
     *
     *
     * Therefore:
     *
     *      HashMap aggregation
     *          +
     *      sort the resulting entries / objects
     *
     */
    static List<AccountAggregate> aggregateAndSortByQuantityDescending(
            List<Trade> trades) {

        Map<String, AccountAggregate> quantityByAccount =
                new HashMap<>();

        for (Trade trade : trades) {

            AccountAggregate aggregate =
                    quantityByAccount.get(trade.accountId);

            if (aggregate == null) {
                aggregate = new AccountAggregate(trade.accountId);
                quantityByAccount.put(
                        trade.accountId,
                        aggregate);
            }

            aggregate.addQuantity(trade.quantity);
        }

        /*
         * The map value is already our domain object.
         *
         * So instead of working with:
         *
         *      Map.Entry<String, Long>
         *
         * we can directly take:
         *
         *      quantityByAccount.values()
         *
         * and sort AccountAggregate objects.
         */
        List<AccountAggregate> result =
                new ArrayList<>(quantityByAccount.values());

        result.sort(
                Comparator.comparingLong(
                        AccountAggregate::totalQuantity)
                        .reversed());

        return result;
    }


    /*
     * ================================================================
     * CSV PARSING EXAMPLE
     * ================================================================
     *
     * Input row:
     *
     *      A1,AAPL,10,100
     *
     *
     * parts:
     *
     *      [0] -> accountId
     *      [1] -> symbol
     *      [2] -> quantity
     *      [3] -> price
     *
     *
     * For real production CSV:
     *
     * Do NOT rely on String.split(",") if fields may contain:
     *
     * - quoted commas
     * - escaped quotes
     * - embedded newlines
     *
     * Use a proper CSV library.
     *
     * For interview-style simple CSV questions,
     * split(",") is often sufficient unless the problem says otherwise.
     *
     */
    static Trade parseSimpleCsvTrade(String line) {

        String[] parts = line.split(",");

        String accountId = parts[0].trim();
        String symbol = parts[1].trim();
        long quantity = Long.parseLong(parts[2].trim());
        double price = Double.parseDouble(parts[3].trim());

        return new Trade(
                accountId,
                symbol,
                quantity,
                price);
    }


    /*
     * ================================================================
     * DRY RUN
     * ================================================================
     *
     * Input:
     *
     *      A1,AAPL,10,100
     *      A2,MSFT,5,200
     *      A1,AAPL,20,110
     *
     *
     * Start:
     *
     *      quantityByAccount = {}
     *
     *
     * Trade 1:
     *
     *      A1, quantity = 10
     *
     * Map:
     *
     *      A1 -> 10
     *
     *
     * Trade 2:
     *
     *      A2, quantity = 5
     *
     * Map:
     *
     *      A1 -> 10
     *      A2 -> 5
     *
     *
     * Trade 3:
     *
     *      A1, quantity = 20
     *
     * A1 already exists.
     *
     *      10 + 20 = 30
     *
     * Map:
     *
     *      A1 -> 30
     *      A2 -> 5
     *
     *
     * Convert entries to result objects.
     *
     * Sort by accountId.
     *
     * Final:
     *
     *      A1 -> 30
     *      A2 -> 5
     *
     *
     * ================================================================
     * WHY HASHMAP FIRST INSTEAD OF TREEMAP?
     * ================================================================
     *
     * Suppose:
     *
     *      n = number of trades
     *      k = number of distinct accounts
     *
     *
     * HashMap aggregation:
     *
     *      n updates
     *      each average O(1)
     *
     *      => O(n)
     *
     *
     * Final sort:
     *
     *      k aggregated accounts
     *
     *      => O(k log k)
     *
     *
     * Total:
     *
     *      O(n + k log k)
     *
     *
     * TreeMap throughout:
     *
     *      each trade update = O(log k)
     *
     *      n trades
     *
     *      => O(n log k)
     *
     *
     * If sorting is only required once at the end,
     * HashMap + final sort is usually the cleaner default.
     *
     *
     * ================================================================
     * SPACE COMPLEXITY
     * ================================================================
     *
     * HashMap contains one entry per distinct account:
     *
     *      O(k)
     *
     * Result list also contains k aggregated records:
     *
     *      O(k)
     *
     * Total auxiliary space:
     *
     *      O(k)
     *
     *
     * ================================================================
     * HOW TO RECOGNIZE THIS PATTERN IN A NEW PROBLEM
     * ================================================================
     *
     * Look for wording like:
     *
     * - aggregate by account
     * - group by symbol
     * - total quantity per trader
     * - sum exposure per desk
     * - count events per customer
     * - calculate volume per instrument
     * - return grouped results sorted by ...
     *
     *
     * Translate immediately:
     *
     *      "per X"
     *
     * usually means:
     *
     *      Map<X, Aggregate>
     *
     *
     * Then ask:
     *
     *      What is ORDER BY?
     *
     * If ORDER BY key:
     *
     *      TreeMap
     *      OR sort final entries
     *
     * If ORDER BY aggregate/value:
     *
     *      sort the final list / entrySet
     *
     *
     * ================================================================
     * FOLLOW-UPS
     * ================================================================
     *
     * 1. Aggregate not just quantity but quantity + notional.
     *
     *      notional += quantity * price
     *
     *
     * 2. Aggregate by composite key.
     *
     *      accountId + symbol
     *
     * Better production representation:
     *
     *      record AccountSymbol(String accountId, String symbol)
     *
     *
     * 3. Sort by largest exposure.
     *
     *      HashMap
     *          +
     *      sort entrySet / aggregate objects by value
     *
     *
     * 4. Return only top K accounts.
     *
     *      HashMap aggregation
     *          +
     *      min-heap of size K
     *
     *
     * 5. Process a very large file that does not fit in memory.
     *
     *      stream rows instead of loading all trades first
     *
     * The HashMap still stores only the aggregate state,
     * not every raw trade.
     *
     *
     * 6. Results must remain sorted after every update.
     *
     *      TreeMap becomes more attractive.
     *
     *
     * ================================================================
     * RELATED INTERVIEW PATTERNS
     * ================================================================
     *
     * - Top K Frequent Elements
     *      HashMap frequency + heap / bucket sort
     *
     * - High Five
     *      HashMap + bounded heap per student
     *
     * - Group Anagrams
     *      HashMap<Signature, List<String>>
     *
     * - Frequency Sort
     *      HashMap + sorting by frequency
     *
     * - Merge / summarize transaction streams
     *      HashMap aggregation
     *
     *
     * ================================================================
     * MAIN
     * ================================================================
     */
    public static void main(String[] args) {

        List<String> csvLines = List.of(
                "A1,AAPL,10,100",
                "A2,MSFT,5,200",
                "A1,AAPL,20,110",
                "A3,GOOG,7,150",
                "A2,TSLA,8,250"
        );

        List<Trade> trades = new ArrayList<>();

        for (String line : csvLines) {
            trades.add(parseSimpleCsvTrade(line));
        }


        System.out.println("Sorted by accountId:");

        List<AccountAggregate> sortedByAccount =
                aggregateAndSortByAccount(trades);

        for (AccountAggregate aggregate : sortedByAccount) {
            System.out.println(aggregate);
        }


        System.out.println();
        System.out.println("Using TreeMap:");

        Map<String, AccountAggregate> treeMapResult =
                aggregateUsingTreeMap(trades);

        for (AccountAggregate aggregate : treeMapResult.values()) {
            System.out.println(aggregate);
        }


        System.out.println();
        System.out.println("Sorted by total quantity descending:");

        List<AccountAggregate> sortedByQuantity =
                aggregateAndSortByQuantityDescending(trades);

        for (AccountAggregate aggregate : sortedByQuantity) {
            System.out.println(aggregate);
        }
    }
}
