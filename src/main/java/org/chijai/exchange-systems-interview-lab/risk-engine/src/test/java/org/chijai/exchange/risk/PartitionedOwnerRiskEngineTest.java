package org.chijai.exchange;

import org.chijai.exchange.risk.check.PositionLimitCheck;
import org.chijai.exchange.risk.check.RiskCheckGroup;
import org.chijai.exchange.risk.model.Account;
import org.chijai.exchange.risk.model.Order;
import org.chijai.exchange.risk.model.Side;
import org.chijai.exchange.risk.partition.PartitionedOwnerRiskEngine;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class PartitionedOwnerRiskEngineTest {

    @Test
    void configKillSwitchCancelAndOrderShareOneOwnerQueue() {
        Account account = new Account(42, "Desk");

        try (PartitionedOwnerRiskEngine engine =
                     new PartitionedOwnerRiskEngine(
                             4, 128, Duration.ofSeconds(1))) {

            engine.register(
                    account,
                    new RiskCheckGroup()
                            .addCheck(new PositionLimitCheck("AAPL", 100)));

            Order buy50 =
                    new Order(account, "AAPL", Side.BUY, 100, 50);

            assertTrue(engine.validate(buy50, 1_000).accepted());

            engine.setKillSwitch(account, true);
            assertFalse(engine.validate(
                    new Order(account, "AAPL", Side.BUY, 100, 1),
                    1_001).accepted());

            engine.setKillSwitch(account, false);
            engine.onCancel(buy50);

            assertTrue(engine.validate(
                    new Order(account, "AAPL", Side.BUY, 100, 100),
                    1_002).accepted());
        }
    }
}
