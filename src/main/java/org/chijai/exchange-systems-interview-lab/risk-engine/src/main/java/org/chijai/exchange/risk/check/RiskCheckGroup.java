package org.chijai.exchange;

import org.chijai.exchange.risk.model.Order;
import org.chijai.exchange.risk.model.RiskDecision;

import java.util.ArrayList;
import java.util.List;

public final class RiskCheckGroup {

    private final List<RiskCheck> checks = new ArrayList<>();

    public RiskCheckGroup addCheck(RiskCheck check) {
        checks.add(check);
        return this;
    }

    public RiskDecision validate(Order order, long nowMs) {
        checks.forEach(RiskCheck::begin);

        for (RiskCheck check : checks) {
            if (check.check(order, nowMs) == CheckResult.BREACH) {
                checks.forEach(RiskCheck::rollback);
                return RiskDecision.reject(check.rejectionReason());
            }
        }

        return RiskDecision.accept();
    }

    public void onCancel(Order order) {
        checks.forEach(check -> check.onCancel(order));
    }
}
