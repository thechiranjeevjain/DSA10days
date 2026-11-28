package org.chijai.PTR;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RiskLimitEngineTest {

    private final Account account = new Account(42, "DeskA");

    @Test
    void positionCheckIsScopedToItsTicker() {
        MaxQtyCheck aaplPosition = new MaxQtyCheck("AAPL", 100);
        ExposureResults result = new ExposureResults();

        assertEquals(Result.PASS, aaplPosition.check(
                new Order(account, "MSFT", Side.Buy, 10, 1_000), 0, result));
        assertTrue(result.accepted);
        assertEquals(0, aaplPosition.consumption);
    }

    @Test
    void signedPositionAllowsLongAndShortUpToAbsoluteLimit() {
        MaxQtyCheck position = new MaxQtyCheck("AAPL", 100);

        assertPass(position, order(Side.Buy, 80));
        assertEquals(80, position.consumption);

        assertPass(position, order(Side.Sell, 150));
        assertEquals(-70, position.consumption);

        ExposureResults breach = new ExposureResults();
        assertEquals(Result.BREACH, position.check(order(Side.Sell, 31), 0, breach));
        assertFalse(breach.accepted);
        assertEquals(-70, position.consumption, "a breach must not commit the candidate position");
    }

    @Test
    void cancellationReversesBuyAndSellContributionsWithoutClamping() {
        MaxQtyCheck position = new MaxQtyCheck("AAPL", 100);

        Order buy = order(Side.Buy, 80);
        Order sell = order(Side.Sell, 30);
        assertPass(position, buy);
        assertPass(position, sell);
        assertEquals(50, position.consumption);

        position.onCancel(sell);
        assertEquals(80, position.consumption);
        position.onCancel(buy);
        assertEquals(0, position.consumption);
    }

    @Test
    void notionalMultiplicationOverflowIsRejected() {
        TotalTradedPerTimeCheck notional = new TotalTradedPerTimeCheck(Long.MAX_VALUE);
        ExposureResults result = new ExposureResults();

        assertEquals(Result.BREACH, notional.check(
                new Order(account, "AAPL", Side.Buy, Long.MAX_VALUE, 2), 1, result));
        assertFalse(result.accepted);
        assertTrue(result.reason.contains("OVERFLOW"));
        assertEquals(0, notional.consumption);
    }

    @Test
    void positionAdditionOverflowIsRejectedWithoutCommitting() {
        MaxQtyCheck position = new MaxQtyCheck("AAPL", Long.MAX_VALUE);
        assertPass(position, new Order(account, "AAPL", Side.Buy, 1, Long.MAX_VALUE));

        ExposureResults result = new ExposureResults();
        assertEquals(Result.BREACH, position.check(order(Side.Buy, 1), 0, result));
        assertFalse(result.accepted);
        assertTrue(result.reason.contains("OVERFLOW"));
        assertEquals(Long.MAX_VALUE, position.consumption);
    }

    @Test
    void groupRollbackRestoresEarlierMutationWhenLaterCheckBreaches() {
        TotalTradedPerTimeCheck notional = new TotalTradedPerTimeCheck(1_000_000);
        MaxQtyCheck position = new MaxQtyCheck("AAPL", 100);
        RiskCheckGroup group = new RiskCheckGroup();
        group.addCheck(notional);
        group.addCheck(position);

        RiskEngine engine = new RiskEngine();
        engine.register(account, group);

        ExposureResults accepted = engine.validate(order(Side.Buy, 80), 1);
        assertTrue(accepted.accepted);
        assertEquals(800, notional.consumption);
        assertEquals(80, position.consumption);

        ExposureResults rejected = engine.validate(order(Side.Buy, 30), 2);
        assertFalse(rejected.accepted);
        assertEquals(800, notional.consumption, "notional mutation must roll back");
        assertEquals(80, position.consumption, "breaching position must not commit");
    }

    @Test
    void orderRejectsInvalidMoneyAndQuantityInputs() {
        assertThrows(IllegalArgumentException.class,
                () -> new Order(account, "AAPL", Side.Buy, 0, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new Order(account, "AAPL", Side.Buy, 1, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Order(account, " ", Side.Buy, 1, 1));
    }

    private Order order(Side side, long quantity) {
        return new Order(account, "AAPL", side, 10, quantity);
    }

    private static void assertPass(MaxQtyCheck check, Order order) {
        ExposureResults result = new ExposureResults();
        assertEquals(Result.PASS, check.check(order, 0, result));
        assertTrue(result.accepted);
    }
}
