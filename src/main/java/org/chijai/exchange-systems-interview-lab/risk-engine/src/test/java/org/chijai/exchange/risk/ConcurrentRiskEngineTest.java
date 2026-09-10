package org.chijai.exchange;

import org.chijai.exchange.risk.check.PositionLimitCheck;
import org.chijai.exchange.risk.check.RiskCheckGroup;
import org.chijai.exchange.risk.engine.ConcurrentRiskEngine;
import org.chijai.exchange.risk.model.Account;
import org.chijai.exchange.risk.model.Order;
import org.chijai.exchange.risk.model.Side;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConcurrentRiskEngineTest {

    @Test
    void sameAccountCompoundInvariantIsSerialized() throws Exception {
        Account account = new Account(1, "Desk");
        ConcurrentRiskEngine engine = new ConcurrentRiskEngine();
        engine.register(
                account,
                new RiskCheckGroup().addCheck(
                        new PositionLimitCheck("AAPL", 100)));

        int callers = 20;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(callers);
        AtomicInteger accepted = new AtomicInteger();

        for (int i = 0; i < callers; i++) {
            Thread thread = new Thread(() -> {
                try {
                    start.await();

                    if (engine.validate(
                            new Order(account, "AAPL", Side.BUY, 100, 10),
                            1_000).accepted()) {
                        accepted.incrementAndGet();
                    }
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });

            thread.start();
        }

        start.countDown();
        done.await();

        assertEquals(10, accepted.get());
    }
}
