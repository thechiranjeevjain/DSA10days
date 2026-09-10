package org.chijai.exchange;

import org.chijai.exchange.risk.model.Order;

public interface RiskCheck {

    void begin();

    CheckResult check(Order order, long nowMs);

    void rollback();

    default void onCancel(Order order) {
        // Most checks have nothing to release on cancellation.
    }

    String rejectionReason();
}
