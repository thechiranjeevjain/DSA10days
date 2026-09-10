package org.chijai.trading;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * ===============================================================
 * Problem: Cash Allocation / Margin Breach Detection
 * Pattern: HashMap Lookup + Sort + Iterate
 * ===============================================================
 *
 * DETAILED PROBLEM STATEMENT
 * --------------------------
 *
 * A trading or brokerage platform maintains cash balances for a set
 * of customer accounts.
 *
 * Each account may also have a margin requirement.
 *
 * The margin requirement represents the minimum amount of cash that
 * the account must currently hold.
 *
 * An account is considered to be BREACHING its margin requirement
 * when:
 *
 *      availableCash < requiredMargin
 *
 *
 * For every account with a margin requirement:
 *
 *      1. Look up its available cash balance.
 *      2. Compare the cash balance with the required margin.
 *      3. If the account does not have enough cash,
 *         calculate its shortfall.
 *
 *
 * The shortfall is:
 *
 *      shortfall = requiredMargin - availableCash
 *
 *
 * Return all accounts whose shortfall is positive.
 *
 *
 * ---------------------------------------------------------------
 * INPUT
 * ---------------------------------------------------------------
 *
 * You are given:
 *
 * 1. A mapping:
 *
 *      accountId -> availableCash
 *
 *    Example:
 *
 *      A -> 1000
 *      B -> 500
 *
 *
 * 2. A list of margin requirements.
 *
 *    Each margin requirement contains:
 *
 *      accountId
 *      requiredMargin
 *
 *    Example:
 *
 *      ("A", 800)
 *      ("B", 700)
 *
 *
 * If an account appears in the margin requirements but does not
 * appear in the cash map, assume that its available cash is 0.
 *
 *
 * ---------------------------------------------------------------
 * OUTPUT
 * ---------------------------------------------------------------
 *
 * Return all breaching accounts.
 *
 * For each breaching account, return:
 *
 *      accountId
 *      availableCash
 *      requiredMargin
 *      shortfall
 *
 *
 * ---------------------------------------------------------------
 * BREACH CONDITION
 * ---------------------------------------------------------------
 *
 *      requiredMargin - availableCash > 0
 *
 * means:
 *
 *      BREACH
 *
 *
 *      requiredMargin - availableCash <= 0
 *
 * means:
 *
 *      SAFE
 *
 *
 * ===============================================================
 * EXAMPLE 1
 * ===============================================================
 *
 * Input:
 *
 * cashByAccount:
 *
 *      A -> 1000
 *      B -> 500
 *      C -> 1200
 *      D -> 300
 *
 * marginRequirements:
 *
 *      A -> 800
 *      B -> 700
 *      C -> 1200
 *      D -> 450
 *
 *
 * Account A:
 *
 *      cash = 1000
 *      requiredMargin = 800
 *
 *      shortfall = 800 - 1000
 *                = -200
 *
 *      SAFE
 *
 *
 * Account B:
 *
 *      cash = 500
 *      requiredMargin = 700
 *
 *      shortfall = 700 - 500
 *                = 200
 *
 *      BREACH
 *
 *
 * Account C:
 *
 *      cash = 1200
 *      requiredMargin = 1200
 *
 *      shortfall = 1200 - 1200
 *                = 0
 *
 *      SAFE
 *
 *
 * Account D:
 *
 *      cash = 300
 *      requiredMargin = 450
 *
 *      shortfall = 450 - 300
 *                = 150
 *
 *      BREACH
 *
 *
 * Output:
 *
 *      B -> cash=500, requiredMargin=700, shortfall=200
 *      D -> cash=300, requiredMargin=450, shortfall=150
 *
 *
 * ===============================================================
 * EXAMPLE 2 — ALL ACCOUNTS ARE SAFE
 * ===============================================================
 *
 * Input:
 *
 * cashByAccount:
 *
 *      ACC1 -> 1000
 *      ACC2 -> 2000
 *      ACC3 -> 500
 *
 * marginRequirements:
 *
 *      ACC1 -> 500
 *      ACC2 -> 1500
 *      ACC3 -> 500
 *
 *
 * Calculations:
 *
 *      ACC1:
 *
 *          500 - 1000 = -500
 *
 *          SAFE
 *
 *
 *      ACC2:
 *
 *          1500 - 2000 = -500
 *
 *          SAFE
 *
 *
 *      ACC3:
 *
 *          500 - 500 = 0
 *
 *          SAFE
 *
 *
 * Output:
 *
 *      []
 *
 *
 * Notice:
 *
 * Having cash exactly equal to the margin requirement is NOT a
 * breach.
 *
 *
 * ===============================================================
 * EXAMPLE 3 — ACCOUNT HAS NO CASH ENTRY
 * ===============================================================
 *
 * Input:
 *
 * cashByAccount:
 *
 *      A -> 1000
 *
 * marginRequirements:
 *
 *      A -> 900
 *      B -> 400
 *
 *
 * Account A:
 *
 *      cash = 1000
 *      requiredMargin = 900
 *
 *      shortfall = 900 - 1000
 *                = -100
 *
 *      SAFE
 *
 *
 * Account B:
 *
 *      B does not exist in cashByAccount.
 *
 *      Therefore:
 *
 *          cash = 0
 *
 *      shortfall = 400 - 0
 *                = 400
 *
 *      BREACH
 *
 *
 * Output:
 *
 *      B -> cash=0, requiredMargin=400, shortfall=400
 *
 *
 * ===============================================================
 * EXAMPLE 4 — PROCESSING PRIORITY
 * ===============================================================
 *
 * Suppose accounts must be reviewed in descending order of margin
 * requirement.
 *
 * Input:
 *
 * cashByAccount:
 *
 *      A -> 300
 *      B -> 1000
 *      C -> 100
 *
 * marginRequirements:
 *
 *      A -> 500
 *      B -> 1500
 *      C -> 200
 *
 *
 * Before sorting:
 *
 *      A -> 500
 *      B -> 1500
 *      C -> 200
 *
 *
 * After sorting by requiredMargin descending:
 *
 *      B -> 1500
 *      A -> 500
 *      C -> 200
 *
 *
 * Process B:
 *
 *      1500 - 1000 = 500
 *
 *      BREACH
 *
 *
 * Process A:
 *
 *      500 - 300 = 200
 *
 *      BREACH
 *
 *
 * Process C:
 *
 *      200 - 100 = 100
 *
 *      BREACH
 *
 *
 * Output in processing order:
 *
 *      B -> shortfall=500
 *      A -> shortfall=200
 *      C -> shortfall=100
 *
 *
 * ===============================================================
 * CONSTRAINTS / ASSUMPTIONS
 * ===============================================================
 *
 * Assume:
 *
 *      1 <= number of accounts
 *
 *      accountId is non-null and uniquely identifies an account.
 *
 *      cash >= 0
 *
 *      requiredMargin >= 0
 *
 *      cash and requiredMargin may be large, therefore long is used.
 *
 *
 * ===============================================================
 * WHAT MAKES THIS "SORT + ITERATE + HASHMAP LOOKUP"?
 * ===============================================================
 *
 * We have two separate needs.
 *
 *
 * Need 1:
 *
 *      Given an accountId, quickly find its cash balance.
 *
 * Best fit:
 *
 *      HashMap
 *
 *
 * Need 2:
 *
 *      Process accounts according to some priority.
 *
 * Best fit:
 *
 *      Sort
 *
 *
 * Then:
 *
 *      iterate through the sorted requirements
 *      lookup cash in HashMap
 *      compute shortfall
 *      record breaches
 *
 *
 * ===============================================================
 * IMPORTANT CLASSIFICATION DISTINCTION
 * ===============================================================
 *
 * If the ONLY question is:
 *
 *      "Which accounts are breaching?"
 *
 * then sorting is unnecessary.
 *
 * We can simply scan every requirement once.
 *
 * That solution is:
 *
 *      O(n)
 *
 *
 * Sorting becomes relevant only when the problem says something like:
 *
 *      "Process accounts by highest margin requirement."
 *
 *      "Prioritize the riskiest accounts."
 *
 *      "Allocate limited cash according to priority."
 *
 *      "Return accounts in a particular priority order."
 *
 *
 * ===============================================================
 * CLASSIFICATION
 * ===============================================================
 *
 * Primary pattern:
 *
 *      HashMap Lookup
 *      +
 *      Sort
 *      +
 *      Iterate
 *
 *
 * Related patterns:
 *
 *      Aggregation
 *      Greedy allocation
 *      Priority processing
 *      Financial risk / margin monitoring
 *
 *
 * ===============================================================
 * FIRST-PRINCIPLES INVENTION PATH
 * ===============================================================
 *
 * Step 1:
 *
 * For one account, ask:
 *
 *      "Does it have enough cash?"
 *
 *
 * Step 2:
 *
 * Convert that English condition into arithmetic:
 *
 *      shortfall = requiredMargin - cash
 *
 *
 * Step 3:
 *
 * Interpret the value:
 *
 *      shortfall > 0
 *          -> BREACH
 *
 *      shortfall <= 0
 *          -> SAFE
 *
 *
 * Step 4:
 *
 * We repeatedly need:
 *
 *      accountId -> cash
 *
 * So use a HashMap.
 *
 *
 * Step 5:
 *
 * If the accounts must be processed in a priority order,
 * sort the margin requirements first.
 *
 *
 * Step 6:
 *
 * Iterate:
 *
 *      lookup cash
 *      calculate shortfall
 *      add account if shortfall > 0
 *
 *
 * Retrieval anchor:
 *
 *      Need account state quickly?
 *          -> HashMap
 *
 *      Need priority/order?
 *          -> Sort
 *
 *      Need to process each account?
 *          -> Iterate
 *
 *      Breach?
 *          -> requirement - cash > 0
 *
 *
 * ===============================================================
 * PRIMARY SOLUTION
 * HashMap Lookup + Sort + Iterate
 * ===============================================================
 *
 * In this version, accounts are processed by descending margin
 * requirement.
 *
 * The exact comparator can easily be replaced if an interviewer
 * gives a different allocation priority.
 */
public class CashAllocationMarginBreach {

    static class MarginRequirement {
        String accountId;
        long requiredMargin;

        MarginRequirement(String accountId, long requiredMargin) {
            this.accountId = accountId;
            this.requiredMargin = requiredMargin;
        }
    }

    static class Breach {
        String accountId;
        long cash;
        long requiredMargin;
        long shortfall;

        Breach(
                String accountId,
                long cash,
                long requiredMargin,
                long shortfall) {

            this.accountId = accountId;
            this.cash = cash;
            this.requiredMargin = requiredMargin;
            this.shortfall = shortfall;
        }

        @Override
        public String toString() {
            return accountId
                    + " -> cash=" + cash
                    + ", requiredMargin=" + requiredMargin
                    + ", shortfall=" + shortfall;
        }
    }

    /*
     * ===========================================================
     * PRIMARY SOLUTION
     * ===========================================================
     *
     * Example processing priority:
     *
     *      highest margin requirement first
     *
     * If the interviewer gives another greedy priority,
     * only the comparator needs to change.
     */
    static List<Breach> findBreaches(
            Map<String, Long> cashByAccount,
            List<MarginRequirement> requirements) {

        requirements.sort(
                Comparator.comparingLong(
                        requirement -> requirement.requiredMargin)
                        .reversed());

        List<Breach> breaches = new ArrayList<>();

        for (MarginRequirement requirement : requirements) {

            /*
             * If an account is missing from the cash map,
             * treat its available cash as 0.
             */
            long cash =
                    cashByAccount.getOrDefault(
                            requirement.accountId,
                            0L);

            /*
             * Positive shortfall means:
             *
             *      requiredMargin > cash
             *
             * therefore the account is breaching.
             */
            long shortfall =
                    requirement.requiredMargin - cash;

            if (shortfall > 0) {

                breaches.add(
                        new Breach(
                                requirement.accountId,
                                cash,
                                requirement.requiredMargin,
                                shortfall));
            }
        }

        return breaches;
    }


    /*
     * ===========================================================
     * SIMPLER VERSION
     * Direct O(n) scan when ordering is NOT required
     * ===========================================================
     *
     * If the problem only asks:
     *
     *      "Find the accounts that are breaching."
     *
     * there is no reason to sort.
     *
     * This is the simplest and optimal solution for that version.
     */
    static List<Breach> findBreachesWithoutSorting(
            Map<String, Long> cashByAccount,
            List<MarginRequirement> requirements) {

        List<Breach> breaches = new ArrayList<>();

        for (MarginRequirement requirement : requirements) {

            long cash =
                    cashByAccount.getOrDefault(
                            requirement.accountId,
                            0L);

            long shortfall =
                    requirement.requiredMargin - cash;

            if (shortfall > 0) {

                breaches.add(
                        new Breach(
                                requirement.accountId,
                                cash,
                                requirement.requiredMargin,
                                shortfall));
            }
        }

        return breaches;
    }


    /*
     * ===========================================================
     * DRY RUN
     * ===========================================================
     *
     * cashByAccount:
     *
     *      A -> 1000
     *      B -> 500
     *      C -> 1200
     *      D -> 300
     *
     * requirements:
     *
     *      A -> 800
     *      B -> 700
     *      C -> 1200
     *      D -> 450
     *
     *
     * After sorting by requiredMargin descending:
     *
     *      C -> 1200
     *      A -> 800
     *      B -> 700
     *      D -> 450
     *
     *
     * C:
     *
     *      cash = 1200
     *      required = 1200
     *
     *      shortfall = 1200 - 1200 = 0
     *
     *      SAFE
     *
     *
     * A:
     *
     *      cash = 1000
     *      required = 800
     *
     *      shortfall = 800 - 1000 = -200
     *
     *      SAFE
     *
     *
     * B:
     *
     *      cash = 500
     *      required = 700
     *
     *      shortfall = 700 - 500 = 200
     *
     *      BREACH
     *
     *
     * D:
     *
     *      cash = 300
     *      required = 450
     *
     *      shortfall = 450 - 300 = 150
     *
     *      BREACH
     *
     *
     * Result:
     *
     *      B -> shortfall 200
     *      D -> shortfall 150
     *
     *
     * ===========================================================
     * TIME COMPLEXITY DERIVATION
     * ===========================================================
     *
     * Let n = number of account requirements.
     *
     * HashMap lookup:
     *
     *      O(1) average per account
     *
     * Sorting:
     *
     *      O(n log n)
     *
     * Iteration:
     *
     *      O(n)
     *
     * Therefore:
     *
     *      O(n log n) + O(n)
     *          =
     *      O(n log n)
     *
     *
     * SPACE COMPLEXITY
     * ----------------
     *
     * Output may contain up to n breaches:
     *
     *      O(n)
     *
     * The cash HashMap also requires O(n) if we build it
     * from raw input.
     *
     *
     * ===========================================================
     * FOLLOW-UPS / VARIATIONS
     * ===========================================================
     *
     * 1. "Return only the account IDs that breach."
     *
     *      Store String accountId instead of Breach objects.
     *
     *
     * 2. "Return accounts ordered by largest shortfall."
     *
     *      First compute the breaches.
     *
     *      Then sort by:
     *
     *          shortfall descending
     *
     *
     * 3. "We have limited extra cash. Fix as many accounts
     *    as possible."
     *
     *      Now this becomes a genuine greedy allocation problem.
     *
     *      A natural strategy may be:
     *
     *          smallest shortfall first
     *
     *      because spending less cash per account can maximize
     *      the number of accounts cured.
     *
     *      Whether that greedy rule is correct depends on the
     *      exact optimization objective.
     *
     *
     * 4. "Fix the highest-risk accounts first."
     *
     *      Sort by risk priority, then allocate while cash remains.
     *
     *
     * 5. "Cash balances arrive as transactions."
     *
     *      First aggregate them:
     *
     *          accountId -> totalCash
     *
     *      using HashMap.merge(...).
     *
     *
     * 6. "Margin requirements are updated continuously."
     *
     *      Maintain current values in a HashMap.
     *
     *      If we repeatedly need the highest-risk breach,
     *      consider a PriorityQueue.
     *
     *
     * ===========================================================
     * RELATED PATTERNS
     * ===========================================================
     *
     * This problem is closely related to:
     *
     *      Account aggregation
     *          -> HashMap
     *
     *      Scheduling / allocation by priority
     *          -> Sort + Greedy
     *
     *      Repeatedly retrieve highest/lowest priority
     *          -> PriorityQueue
     *
     *      Running balances from transactions
     *          -> HashMap + running totals
     *
     *
     * ===========================================================
     * INTERVIEW MEMORY ANCHOR
     * ===========================================================
     *
     *      accountId -> value
     *          HashMap
     *
     *      priority matters
     *          Sort
     *
     *      process once
     *          Iterate
     *
     *      breach
     *          requirement - cash > 0
     */
    public static void main(String[] args) {

        Map<String, Long> cashByAccount = new HashMap<>();

        cashByAccount.put("A", 1000L);
        cashByAccount.put("B", 500L);
        cashByAccount.put("C", 1200L);
        cashByAccount.put("D", 300L);

        List<MarginRequirement> requirements =
                new ArrayList<>();

        requirements.add(new MarginRequirement("A", 800L));
        requirements.add(new MarginRequirement("B", 700L));
        requirements.add(new MarginRequirement("C", 1200L));
        requirements.add(new MarginRequirement("D", 450L));

        List<Breach> breaches =
                findBreaches(
                        cashByAccount,
                        requirements);

        for (Breach breach : breaches) {
            System.out.println(breach);
        }
    }
}
