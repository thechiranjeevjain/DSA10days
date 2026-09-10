package org.chijai.exchange;

import org.chijai.PTR.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * LEVEL 3b — Partitioned + Message Bus / Event-Driven
 *
 * Compile: javac RiskLimitEngine.java RiskLimitEngineAsync.java
 *               RiskLimitEnginePartitioned.java RiskLimitEnginePartitionedAsync.java
 * Run:     java RiskLimitEnginePartitionedAsync
 *
 * DIFFERENCE FROM RiskLimitEnginePartitioned.java (L3a):
 *
 *   L3a:  Partition validates via synchronized(group)
 *         Caller thread does the work directly
 *
 *   L3b:  SAME N partitions, SAME routing (accountId % N)
 *         SAME Partition class — but order/config/command arrive via MessageBus
 *         Each Partition subscribes to its slice of events on the bus
 *         Worker thread processes the queue — no synchronized
 *
 * THAT IS THE ONLY DIFFERENCE:
 *   synchronized(group)  →  MessageBus → partition queue → worker
 *
 * REAL SYSTEM:
 *   L3a ≈ Gen3 single-threaded ME (synchronized, direct call)
 *   L3b ≈ Gen4 multi-partition with command bus
 *         TransactionalRiskCheckGroup dispatches RiskCheckDeltaCommand per partition
 *
 * EXTENDS:
 *   AsyncPartitionOnBus extends Partition — adds queue + worker + bus subscription
 *   PartitionedAsyncRiskEngine extends PartitionedRiskEngine — uses AsyncPartitionOnBus
 */

// Reuse bus + event types from RiskLimitEngineAsync.java (same package, already compiled)
// OrderEvent, ConfigEvent, CommandEvent, MessageBus, RiskEngineListener are all available

// Extends Partition — adds queue + worker + bus subscription
// SAME logic as Partition.validate() but via queue instead of synchronized
class AsyncPartitionOnBus extends Partition implements Runnable {
    final BlockingQueue<OrderEvent> queue = new ArrayBlockingQueue<>(1024);
    final Thread                    worker;
    final MessageBus bus;

    AsyncPartitionOnBus(int id, MessageBus bus) {
        super(id);
        this.bus    = bus;
        this.worker = new Thread(this, "risk-partition-" + id);
        this.worker.setDaemon(false);
        this.worker.start();

        // Subscribe to OrderEvents for accounts owned by this partition
        // (filtered inside worker — bus delivers to all, partition checks ownership)
        bus.subscribe(OrderEvent.class, this::onOrderEvent);
    }

    // Called from bus — filter by ownership, then queue
    private void onOrderEvent(OrderEvent event) {
        int partitionIndex = Math.abs(event.order.account.id % PartitionedRiskEngine.N);
        if (partitionIndex != id) return;  // not mine

        if (!queue.offer(event)) {
            ExposureResults r = new ExposureResults();
            r.reject("OVERLOAD partition " + id);
            event.reply.complete(r);
        }
    }

    @Override public void run() {
        while (true) {
            try {
                OrderEvent event = queue.take();
                if (event.order == null) { System.out.println("[P-"+id+"] done"); return; }

                ExposureResults r = new ExposureResults();
                RiskCheckGroup g = groups.get(event.order.account.id);
                if (g == null) {
                    r.reject("no group for " + event.order.account);
                } else {
                    // NO synchronized — worker is the only thread touching groups
                    g.begin();
                    if (g.check(event.order, event.nowMs, r) == Result.BREACH)
                        g.rollback();
                }
                event.reply.complete(r);
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
        }
    }

    // Override validate — not used directly (bus handles routing)
    @Override ExposureResults validate(Order order, long nowMs) {
        OrderEvent event = new OrderEvent(order);
        bus.publish(event);
        try { return event.reply.get(); }
        catch (Exception e) {
            ExposureResults r = new ExposureResults();
            r.reject("ERROR: " + e.getMessage());
            return r;
        }
    }

    void shutdown() throws InterruptedException {
        queue.put(new OrderEvent(null) {{ /* poison */ }});
    }
    void join() throws InterruptedException { worker.join(); }
}

class PartitionedAsyncRiskEngine extends PartitionedRiskEngine {
    final MessageBus bus = new MessageBus();
    private final AsyncPartitionOnBus[] asyncPartitions;

    PartitionedAsyncRiskEngine() {
        asyncPartitions = new AsyncPartitionOnBus[N];
        for (int i = 0; i < N; i++)
            asyncPartitions[i] = new AsyncPartitionOnBus(i, bus);

        // Config events register account in the correct partition
        bus.subscribe(ConfigEvent.class, this::onConfigEvent);

        // Command events (kill switch, cancel) via bus
        bus.subscribe(CommandEvent.class, this::onCommandEvent);
    }

    private final Map<Integer, KillSwitchCheck> killSwitches = new HashMap<>();

    private void onConfigEvent(ConfigEvent event) {
        KillSwitchCheck ks = new KillSwitchCheck();
        killSwitches.put(event.account.id, ks);
        event.group.addCheck(ks);
        // store group in the owning partition
        Partition p = partitionFor(event.account);
        p.register(event.account, event.group);
        System.out.println("  [Config] registered: " + event.account.name
                + " → Partition " + p.id);
    }

    private void onCommandEvent(CommandEvent event) {
        switch (event.type) {
            case KILL_SWITCH_ON  -> {
                KillSwitchCheck ks = killSwitches.get(event.account.id);
                if (ks != null) { ks.activate();   System.out.println("  [Cmd] KS ON:  " + event.account); }
            }
            case KILL_SWITCH_OFF -> {
                KillSwitchCheck ks = killSwitches.get(event.account.id);
                if (ks != null) { ks.deactivate(); System.out.println("  [Cmd] KS OFF: " + event.account); }
            }
            case CANCEL -> {
                if (event.order != null) onCancel(event.order);
            }
        }
    }

    @Override Partition partitionFor(Account account) {
        return asyncPartitions[Math.abs(account.id % N)];
    }

    // Submit via bus — same as RiskLimitEngineAsync
    ExposureResults submitOrder(Order order) {
        OrderEvent event = new OrderEvent(order);
        bus.publish(event);
        try { return event.reply.get(); }
        catch (Exception e) {
            ExposureResults r = new ExposureResults(); r.reject("ERROR"); return r;
        }
    }

    void sendConfig(Account account, RiskCheckGroup group) {
        bus.publish(new ConfigEvent(account, group));
    }

    void sendCommand(CommandEvent cmd) { bus.publish(cmd); }

    void shutdown() throws InterruptedException {
        for (AsyncPartitionOnBus p : asyncPartitions) p.shutdown();
        for (AsyncPartitionOnBus p : asyncPartitions) p.join();
    }
}

public class RiskLimitEnginePartitionedAsync {
    public static void main(String[] args) throws InterruptedException {
        PartitionedAsyncRiskEngine engine = new PartitionedAsyncRiskEngine();

        // ── Config from RDM ─────────────────────────────────────
        System.out.println("=== Config from RDM → partitioned by accountId % 4 ===");
        Account[] accounts = new Account[4];
        for (int i = 0; i < 4; i++) {
            accounts[i] = new Account(i, "Desk-" + i);
            RiskCheckGroup g = new RiskCheckGroup();
            g.addCheck(new TotalTradedPerTimeCheck(500_000));
            g.addCheck(new MaxQtyCheck("AAPL", 1000));
            engine.sendConfig(accounts[i], g);
        }

        // ── Orders from Matching Engine ─────────────────────────
        System.out.println("\n=== Orders from Matching Engine ===");
        long t = System.currentTimeMillis();
        Account a0 = accounts[0];
        show("buy 100@100 ", engine.submitOrder(new Order(a0,"AAPL",Side.Buy,100,100)));
        show("buy 950@100 ", engine.submitOrder(new Order(a0,"AAPL",Side.Buy,100,950)));
        show("buy 600@1000", engine.submitOrder(new Order(a0,"AAPL",Side.Buy,1000,600)));
        show("unknown     ", engine.submitOrder(new Order(new Account(99,"X"),"AAPL",Side.Buy,100,1)));

        // ── Operator Kill Switch via bus ────────────────────────
        System.out.println("\n=== Operator Kill Switch via bus ===");
        engine.sendCommand(new CommandEvent(a0, CommandType.KILL_SWITCH_ON));
        show("blocked     ", engine.submitOrder(new Order(a0,"AAPL",Side.Buy,100,10)));
        engine.sendCommand(new CommandEvent(a0, CommandType.KILL_SWITCH_OFF));
        show("restored    ", engine.submitOrder(new Order(a0,"AAPL",Side.Buy,100,10)));

        // ── All accounts concurrently ───────────────────────────
        System.out.println("\n=== 4 accounts concurrently — 4 async partitions ===");
        Thread[] callers = new Thread[4];
        for (int i = 0; i < 4; i++) {
            final Account acct = accounts[i];
            callers[i] = new Thread(() -> {
                for (int j = 0; j < 2; j++)
                    show(acct.name+"-"+j, engine.submitOrder(
                            new Order(acct,"AAPL",Side.Buy,100,50)));
            });
        }
        for (Thread c : callers) c.start();
        for (Thread c : callers) try { c.join(); } catch (InterruptedException e) {}

        engine.shutdown();
        System.out.println("4 partitions, message bus, zero locks — done.");
    }

    static void show(String l, ExposureResults r) {
        System.out.printf("  %-14s → %s%n", l, r);
    }
}
