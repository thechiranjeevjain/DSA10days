package org.chijai.exchange;

import org.chijai.exchange.risk.model.Order;

public final class KillSwitchCheck implements RiskCheck {

    private boolean active;

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public boolean active() {
        return active;
    }

    @Override
    public void begin() {
        // No per-order consumption to snapshot.
    }

    @Override
    public CheckResult check(Order order, long nowMs) {
        return active ? CheckResult.BREACH : CheckResult.PASS;
    }

    @Override
    public void rollback() {
        // Order evaluation does not mutate the kill-switch state.
    }

    @Override
    public String rejectionReason() {
        return "kill switch active";
    }
}
