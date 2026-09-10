package org.chijai.exchange;

import org.chijai.exchange.risk.check.KillSwitchCheck;
import org.chijai.exchange.risk.check.RiskCheckGroup;
import org.chijai.exchange.risk.model.RiskDecision;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * One queue + one worker owns all mutable state for this partition.
 *
 * Orders, cancels, config, and kill-switch mutations all pass through the
 * same worker. That is what makes the "no locks inside the partition" claim
 * valid.
 */
final class RiskPartition implements Runnable {

    private final int id;
    private final BlockingQueue<PartitionEvent> queue;
    private final Thread worker;

    private final Map<Integer, RiskCheckGroup> groups = new HashMap<>();
    private final Map<Integer, KillSwitchCheck> killSwitches = new HashMap<>();

    RiskPartition(int id, int queueCapacity) {
        this.id = id;
        this.queue = new ArrayBlockingQueue<>(queueCapacity);
        this.worker = new Thread(this, "risk-partition-" + id);
        this.worker.start();
    }

    boolean offer(PartitionEvent event) {
        return queue.offer(event);
    }

    void put(PartitionEvent event) throws InterruptedException {
        queue.put(event);
    }

    void join() throws InterruptedException {
        worker.join();
    }

    @Override
    public void run() {
        while (true) {
            try {
                PartitionEvent event = queue.take();

                if (event instanceof PartitionEvent.StopEvent) {
                    return;
                }

                handle(event);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                return;
            } catch (RuntimeException runtimeException) {
                // Keep one bad event from silently killing the owner thread.
                // Order events are completed exceptionally inside handle().
                System.err.println(
                        "Partition " + id + " event failure: "
                                + runtimeException.getMessage());
            }
        }
    }

    private void handle(PartitionEvent event) {
        if (event instanceof PartitionEvent.ConfigEvent config) {
            KillSwitchCheck killSwitch = new KillSwitchCheck();
            config.group().addCheck(killSwitch);
            killSwitches.put(config.accountId(), killSwitch);
            groups.put(config.accountId(), config.group());
            return;
        }

        if (event instanceof PartitionEvent.KillSwitchEvent command) {
            KillSwitchCheck killSwitch = killSwitches.get(command.accountId());
            if (killSwitch != null) {
                if (command.active()) {
                    killSwitch.activate();
                } else {
                    killSwitch.deactivate();
                }
            }
            return;
        }

        if (event instanceof PartitionEvent.CancelEvent cancel) {
            RiskCheckGroup group = groups.get(cancel.accountId());
            if (group != null) {
                group.onCancel(cancel.order());
            }
            return;
        }

        PartitionEvent.OrderEvent orderEvent =
                (PartitionEvent.OrderEvent) event;

        try {
            RiskCheckGroup group = groups.get(orderEvent.accountId());

            RiskDecision decision = group == null
                    ? RiskDecision.reject(
                            "no risk group for account " + orderEvent.accountId())
                    : group.validate(orderEvent.order(), orderEvent.nowMs());

            orderEvent.reply().complete(decision);
        } catch (RuntimeException exception) {
            orderEvent.reply().completeExceptionally(exception);
        }
    }
}
