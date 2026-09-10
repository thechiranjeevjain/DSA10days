package org.chijai.exchange;

import org.chijai.exchange.risk.check.RiskCheckGroup;
import org.chijai.exchange.risk.model.Account;
import org.chijai.exchange.risk.model.Order;
import org.chijai.exchange.risk.model.RiskDecision;

import java.util.HashMap;
import java.util.Map;

/**
 * Simplest model: one caller/owner mutates the risk state.
 *
 * No concurrent collection and no locks are needed because the ownership
 * contract guarantees that only one execution thread invokes mutations.
 */
public final class SingleOwnerRiskEngine implements RiskEngine {

    private final Map<Integer, RiskCheckGroup> groups = new HashMap<>();

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

        return group.validate(order, nowMs);
    }

    @Override
    public void onCancel(Order order) {
        RiskCheckGroup group = groups.get(order.account().id());

        if (group != null) {
            group.onCancel(order);
        }
    }
}
