package org.chijai.exchange;

import org.chijai.exchange.risk.check.NotionalPerSecondCheck;
import org.chijai.exchange.risk.check.PositionLimitCheck;
import org.chijai.exchange.risk.check.RiskCheckGroup;
import org.chijai.exchange.risk.engine.SingleOwnerRiskEngine;
import org.chijai.exchange.risk.model.Account;
import org.chijai.exchange.risk.model.Order;
import org.chijai.exchange.risk.model.Side;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SingleOwnerRiskEngineTest {

    @Test
    void rejectsPositionBreachWithoutKeepingEarlierNotionalMutation() {
        Account account = new Account(1, "Desk");
        NotionalPerSecondCheck notional = new NotionalPerSecondCheck(1_000_000);
        PositionLimitCheck position = new PositionLimitCheck("AAPL", 100);

        RiskCheckGroup group = new RiskCheckGroup()
                .addCheck(notional)
                .addCheck(position);

        SingleOwnerRiskEngine engine = new SingleOwnerRiskEngine();
        engine.register(account, group);

        long t = 10_000;

        assertTrue(engine.validate(
                new Order(account, "AAPL", Side.BUY, 100, 50), t).accepted());

        long notionalAfterAcceptedOrder = notional.consumption();

        assertFalse(engine.validate(
                new Order(account, "AAPL", Side.BUY, 100, 60), t).accepted());

        assertEquals(
                notionalAfterAcceptedOrder,
                notional.consumption(),
                "Rejected order must roll back mutations from earlier checks");
        assertEquals(50, position.consumption());
    }

    @Test
    void positionCheckIsTickerSpecificAndSigned() {
        Account account = new Account(1, "Desk");
        PositionLimitCheck aapl = new PositionLimitCheck("AAPL", 100);

        RiskCheckGroup group = new RiskCheckGroup().addCheck(aapl);
        SingleOwnerRiskEngine engine = new SingleOwnerRiskEngine();
        engine.register(account, group);

        long t = 10_000;

        assertTrue(engine.validate(
                new Order(account, "MSFT", Side.BUY, 100, 1_000), t).accepted());
        assertEquals(0, aapl.consumption());

        assertTrue(engine.validate(
                new Order(account, "AAPL", Side.BUY, 100, 80), t).accepted());
        assertEquals(80, aapl.consumption());

        assertTrue(engine.validate(
                new Order(account, "AAPL", Side.SELL, 100, 30), t).accepted());
        assertEquals(50, aapl.consumption());
    }

    @Test
    void fixedNotionalWindowResetsAtNextEpochSecond() {
        Account account = new Account(1, "Desk");
        NotionalPerSecondCheck rate = new NotionalPerSecondCheck(10_000);

        RiskCheckGroup group = new RiskCheckGroup().addCheck(rate);
        SingleOwnerRiskEngine engine = new SingleOwnerRiskEngine();
        engine.register(account, group);

        assertTrue(engine.validate(
                new Order(account, "AAPL", Side.BUY, 100, 100), 10_100).accepted());

        assertFalse(engine.validate(
                new Order(account, "AAPL", Side.BUY, 1, 1), 10_900).accepted());

        assertTrue(engine.validate(
                new Order(account, "AAPL", Side.BUY, 1, 1), 11_000).accepted());
    }
}
