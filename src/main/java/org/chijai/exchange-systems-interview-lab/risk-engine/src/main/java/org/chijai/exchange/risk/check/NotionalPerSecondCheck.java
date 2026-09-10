package org.chijai.exchange;

import org.chijai.exchange.risk.model.Order;

public final class NotionalPerSecondCheck extends AbstractRiskCheck {

    private long bucketSecond = Long.MIN_VALUE;
    private long previousBucketSecond;

    public NotionalPerSecondCheck(long maxSubmittedNotionalPerSecond) {
        super("NotionalPerSecond", maxSubmittedNotionalPerSecond);
    }

    @Override
    public void begin() {
        super.begin();
        previousBucketSecond = bucketSecond;
    }

    @Override
    public CheckResult check(Order order, long nowMs) {
        long currentBucket = Math.floorDiv(nowMs, 1_000L);

        if (currentBucket != bucketSecond) {
            bucketSecond = currentBucket;
            consumption = 0;
        }

        long orderNotional = Math.multiplyExact(order.price(), order.quantity());
        long candidate = Math.addExact(consumption, orderNotional);

        if (candidate > limit) {
            return CheckResult.BREACH;
        }

        consumption = candidate;
        return CheckResult.PASS;
    }

    @Override
    public void rollback() {
        super.rollback();
        bucketSecond = previousBucketSecond;
    }

    @Override
    public String rejectionReason() {
        return "submitted notional-per-second limit breached";
    }
}
