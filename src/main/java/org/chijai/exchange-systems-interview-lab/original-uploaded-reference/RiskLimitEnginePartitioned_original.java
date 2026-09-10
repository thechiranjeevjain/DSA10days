package org.chijai.exchange;


import org.chijai.PTR.*;

import java.util.*;

/**
 * LEVEL 3a — Partitioned (no event bus, extends Level 1)
 *
 * Compile: javac RiskLimitEngine.java RiskLimitEnginePartitioned.java
 * Run:     java RiskLimitEnginePartitioned
 *
 * WHAT L1 does:    one global map, synchronized(group) per account
 * WHAT L3a adds:   accountId % N → Partition[i]
 *                  each partition owns its own groups map
 *                  synchronized still used but N independent lock domains
 *                  N=4 → contention reduced 4x, zero new threads
 *
 * EXTENDS: PartitionedRiskEngine extends RiskEngine (Level 1)
 * NEW:     Partition class (owns slice of accounts)
 *
 * NO queues. NO worker threads. Caller thread does the work.
 * Gen3 analogy: one ME process per market — same accounts, same lock pattern
 */

class Partition {
    final int                          id;
    final Map<Integer, RiskCheckGroup> groups = new HashMap<>();

    Partition(int id) { this.id = id; }

    void register(Account account, RiskCheckGroup group) {
        groups.put(account.id, group);
    }

    ExposureResults validate(Order order, long nowMs) {
        ExposureResults results = new ExposureResults();
        RiskCheckGroup group = groups.get(order.account.id);
        if (group == null) { results.reject("no group for " + order.account); return results; }
        synchronized (group) {
            group.begin();
            if (group.check(order, nowMs, results) == Result.BREACH) group.rollback();
        }
        return results;
    }

    void onCancel(Order order) {
        RiskCheckGroup g = groups.get(order.account.id);
        if (g != null) synchronized (g) { g.onCancel(order); }
    }
}

class PartitionedRiskEngine extends RiskEngine {
    static final int N = 4;
    final Partition[] partitions = new Partition[N];

    PartitionedRiskEngine() {
        for (int i = 0; i < N; i++) partitions[i] = new Partition(i);
    }

    Partition partitionFor(Account account) {
        return partitions[Math.abs(account.id % N)];
    }

    @Override void register(Account account, RiskCheckGroup group) {
        partitionFor(account).register(account, group);
    }

    @Override ExposureResults validate(Order order, long nowMs) {
        return partitionFor(order.account).validate(order, nowMs);
    }

    @Override void onCancel(Order order) {
        partitionFor(order.account).onCancel(order);
    }
}

public class RiskLimitEnginePartitioned {
    public static void main(String[] args) {
        PartitionedRiskEngine engine = new PartitionedRiskEngine();

        Account[] accounts = new Account[6];
        for (int i = 0; i < 6; i++) {
            accounts[i] = new Account(i, "Desk-" + i);
            RiskCheckGroup g = new RiskCheckGroup();
            g.addCheck(new TotalTradedPerTimeCheck(500_000));
            g.addCheck(new MaxQtyCheck("AAPL", 1000));
            engine.register(accounts[i], g);
            System.out.printf("  %-10s → Partition %d%n",
                    accounts[i].name, Math.abs(i % PartitionedRiskEngine.N));
        }

        long t = System.currentTimeMillis();
        Account a0 = accounts[0];
        System.out.println("\n=== L3a — same correctness, 4 lock domains ===");
        show("buy 100@100 ", engine.validate(new Order(a0,"AAPL",Side.Buy,100,100), t));
        show("buy 950@100 ", engine.validate(new Order(a0,"AAPL",Side.Buy,100,950), t));
        show("buy 600@1000", engine.validate(new Order(a0,"AAPL",Side.Buy,1000,600), t));
        show("new second  ", engine.validate(new Order(a0,"AAPL",Side.Buy,100,50), t+1001));
        show("unknown     ", engine.validate(new Order(new Account(99,"X"),"AAPL",Side.Buy,100,1), t));

        System.out.println("\n=== 6 accounts concurrent — 4 lock domains (not 1) ===");
        Thread[] threads = new Thread[6];
        for (int i = 0; i < 6; i++) {
            final Account acct = accounts[i];
            threads[i] = new Thread(() -> {
                long now = System.currentTimeMillis();
                for (int j = 0; j < 2; j++)
                    show(acct.name+"-"+j,
                            engine.validate(new Order(acct,"AAPL",Side.Buy,100,50), now));
            });
        }
        for (Thread th : threads) th.start();
        for (Thread th : threads) try { th.join(); } catch (InterruptedException e) {}
        System.out.println("  4 partitions, no queues, no extra threads.");
    }

    static void show(String l, ExposureResults r) {
        System.out.printf("  %-14s → %s%n", l, r);
    }
}
