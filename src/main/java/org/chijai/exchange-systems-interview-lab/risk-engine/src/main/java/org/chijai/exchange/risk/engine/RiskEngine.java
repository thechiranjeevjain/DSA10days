package org.chijai.exchange;

import org.chijai.exchange.risk.check.RiskCheckGroup;
import org.chijai.exchange.risk.model.Account;
import org.chijai.exchange.risk.model.Order;
import org.chijai.exchange.risk.model.RiskDecision;

public interface RiskEngine {

    void register(Account account, RiskCheckGroup group);

    RiskDecision validate(Order order, long nowMs);

    void onCancel(Order order);
}
