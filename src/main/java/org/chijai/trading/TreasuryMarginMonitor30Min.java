package org.chijai.trading;

import java.util.*;

/*
 * TREASURY 30-MINUTE INTERVIEW DEFAULT
 *
 * Problem:
 *   Cash movements arrive for accounts.
 *   Each account has a required margin.
 *   Return accounts whose cash is below margin,
 *   largest shortfall first.
 *
 * Pattern:
 *   aggregate -> lookup/join -> filter -> sort
 *
 * Money is stored as long in the smallest unit.
 */
public class TreasuryMarginMonitor30Min {

    record CashEvent(String account, long amount) {}
    record Margin(String account, long required) {}
    record Breach(String account, long cash, long required, long shortfall) {}

    static List<Breach> findBreaches(
            List<CashEvent> events,
            List<Margin> margins) {

        Map<String, Long> cashByAccount = new HashMap<>();

        for (CashEvent event : events) {
            cashByAccount.merge(
                    event.account(),
                    event.amount(),
                    Long::sum);
        }

        List<Breach> breaches = new ArrayList<>();

        for (Margin margin : margins) {

            long cash =
                    cashByAccount.getOrDefault(
                            margin.account(), 0L);

            long shortfall =
                    margin.required() - cash;

            if (shortfall > 0) {
                breaches.add(
                        new Breach(
                                margin.account(),
                                cash,
                                margin.required(),
                                shortfall));
            }
        }

        breaches.sort(
                Comparator.comparingLong(Breach::shortfall)
                        .reversed());

        return breaches;
    }

    public static void main(String[] args) {

        List<CashEvent> events = List.of(
                new CashEvent("A", 1_000),
                new CashEvent("B",   500),
                new CashEvent("A",  -200),
                new CashEvent("C",   300));

        List<Margin> margins = List.of(
                new Margin("A", 900),
                new Margin("B", 800),
                new Margin("C", 300),
                new Margin("D", 200));

        findBreaches(events, margins)
                .forEach(System.out::println);
    }
}

/*
 * RECONSTRUCTION:
 *
 * events
 *   -> HashMap<account, cash>
 *   -> compare cash with margin
 *   -> shortfall = required - cash
 *   -> keep shortfall > 0
 *   -> sort largest shortfall first
 *
 * Complexity:
 *   T cash events + M margins:
 *   O(T + M log M) worst case because up to M breaches are sorted.
 *   Space O(A + M).
 *
 * Follow-ups:
 *   - No output ordering needed? Remove sort -> O(T + M).
 *   - Repeatedly need worst breach? PriorityQueue.
 *   - Broker vs internal books? Same map/join pattern for reconciliation.
 *   - Peak N-day exposure? Fixed-size sliding window.
 *   - Concurrent updates? Define account ownership / locking before mutating.
 */
