package org.chijai.exchange;

import org.chijai.exchange.risk.model.Order;

public final class PositionLimitCheck extends AbstractRiskCheck {

    private final String ticker;

    public PositionLimitCheck(String ticker, long absolutePositionLimit) {
        super("Position[" + ticker + "]", absolutePositionLimit);
        this.ticker = ticker;
    }

    @Override
    public CheckResult check(Order order, long nowMs) {
        if (!ticker.equals(order.ticker())) {
            return CheckResult.PASS;
        }

        long candidate = Math.addExact(consumption, order.signedQuantity());

        if (candidate > limit || candidate < -limit) {
            return CheckResult.BREACH;
        }

        consumption = candidate;
        return CheckResult.PASS;
    }

    @Override
    public void onCancel(Order order) {
        if (!ticker.equals(order.ticker())) {
            return;
        }

        consumption = Math.subtractExact(consumption, order.signedQuantity());
    }

    @Override
    public String rejectionReason() {
        return "position limit breached for " + ticker;
    }
}
