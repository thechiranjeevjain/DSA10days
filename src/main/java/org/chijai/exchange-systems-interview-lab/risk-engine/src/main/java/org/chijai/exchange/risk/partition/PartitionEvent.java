package org.chijai.exchange;

import org.chijai.exchange.risk.check.RiskCheckGroup;
import org.chijai.exchange.risk.model.Account;
import org.chijai.exchange.risk.model.Order;
import org.chijai.exchange.risk.model.RiskDecision;

import java.util.concurrent.CompletableFuture;

public sealed interface PartitionEvent
        permits PartitionEvent.OrderEvent,
                PartitionEvent.CancelEvent,
                PartitionEvent.ConfigEvent,
                PartitionEvent.KillSwitchEvent,
                PartitionEvent.StopEvent {

    int accountId();

    record OrderEvent(
            Order order,
            long nowMs,
            CompletableFuture<RiskDecision> reply
    ) implements PartitionEvent {
        @Override
        public int accountId() {
            return order.account().id();
        }
    }

    record CancelEvent(Order order) implements PartitionEvent {
        @Override
        public int accountId() {
            return order.account().id();
        }
    }

    record ConfigEvent(
            Account account,
            RiskCheckGroup group
    ) implements PartitionEvent {
        @Override
        public int accountId() {
            return account.id();
        }
    }

    record KillSwitchEvent(
            int accountId,
            boolean active
    ) implements PartitionEvent {
    }

    record StopEvent(int accountId) implements PartitionEvent {
    }
}
