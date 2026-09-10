package org.chijai.exchange;

import org.chijai.exchange.risk.check.RiskCheckGroup;
import org.chijai.exchange.risk.model.Account;
import org.chijai.exchange.risk.model.Order;
import org.chijai.exchange.risk.model.RiskDecision;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Interview-design evolution:
 *
 * ownership key -> partition -> bounded queue -> one worker -> owned state
 *
 * This class does not claim to represent deployed PTR topology.
 */
public final class PartitionedOwnerRiskEngine implements AutoCloseable {

    private final RiskPartition[] partitions;
    private final Duration decisionTimeout;

    public PartitionedOwnerRiskEngine(
            int partitionCount,
            int queueCapacity,
            Duration decisionTimeout
    ) {
        if (partitionCount <= 0) {
            throw new IllegalArgumentException("partitionCount must be > 0");
        }

        this.partitions = new RiskPartition[partitionCount];
        this.decisionTimeout = decisionTimeout;

        for (int i = 0; i < partitionCount; i++) {
            partitions[i] = new RiskPartition(i, queueCapacity);
        }
    }

    public void register(Account account, RiskCheckGroup group) {
        submitControl(new PartitionEvent.ConfigEvent(account, group));
    }

    public void setKillSwitch(Account account, boolean active) {
        submitControl(new PartitionEvent.KillSwitchEvent(account.id(), active));
    }

    public void onCancel(Order order) {
        submitControl(new PartitionEvent.CancelEvent(order));
    }

    public RiskDecision validate(Order order, long nowMs) {
        CompletableFuture<RiskDecision> reply = new CompletableFuture<>();

        PartitionEvent.OrderEvent event =
                new PartitionEvent.OrderEvent(order, nowMs, reply);

        RiskPartition partition = partitionFor(order.account().id());

        if (!partition.offer(event)) {
            return RiskDecision.reject("OVERLOAD");
        }

        try {
            return reply.get(
                    decisionTimeout.toMillis(),
                    TimeUnit.MILLISECONDS);
        } catch (TimeoutException timeout) {
            return RiskDecision.reject("TIMEOUT");
        } catch (Exception exception) {
            return RiskDecision.reject("ERROR: " + exception.getMessage());
        }
    }

    private void submitControl(PartitionEvent event) {
        RiskPartition partition = partitionFor(event.accountId());

        if (!partition.offer(event)) {
            throw new IllegalStateException(
                    "partition queue full for control event");
        }
    }

    private RiskPartition partitionFor(int accountId) {
        return partitions[Math.floorMod(accountId, partitions.length)];
    }

    @Override
    public void close() {
        try {
            for (int i = 0; i < partitions.length; i++) {
                partitions[i].put(new PartitionEvent.StopEvent(i));
            }

            for (RiskPartition partition : partitions) {
                partition.join();
            }
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
        }
    }
}
