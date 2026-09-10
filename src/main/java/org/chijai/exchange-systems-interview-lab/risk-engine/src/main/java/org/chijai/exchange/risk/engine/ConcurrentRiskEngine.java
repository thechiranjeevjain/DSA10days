package org.chijai.exchange;

import org.chijai.exchange.risk.check.RiskCheckGroup;
import org.chijai.exchange.risk.model.Account;
import org.chijai.exchange.risk.model.Order;
import org.chijai.exchange.risk.model.RiskDecision;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Shared-state concurrency exercise.
 *
 * ConcurrentHashMap protects lookup/registration operations.
 * synchronized(group) protects the compound business invariant:
 *
 *     snapshot -> evaluate all checks -> commit all or rollback all
 */
public final class ConcurrentRiskEngine implements RiskEngine {

    private final ConcurrentHashMap<Integer, RiskCheckGroup> groups =
            new ConcurrentHashMap<>();

    @Override
    public void register(Account account, RiskCheckGroup group) {
        groups.put(account.id(), group);
    }

    @Override
    public RiskDecision validate(Order order, long nowMs) {
        RiskCheckGroup group = groups.get(order.account().id());

        if (group == null) {
            return RiskDecision.reject("no risk group for account " + order.account().id());
        }

        synchronized (group) {
            return group.validate(order, nowMs);
        }
    }

    @Override
    public void onCancel(Order order) {
        RiskCheckGroup group = groups.get(order.account().id());

        if (group != null) {
            synchronized (group) {
                group.onCancel(order);
            }
        }
    }
}
