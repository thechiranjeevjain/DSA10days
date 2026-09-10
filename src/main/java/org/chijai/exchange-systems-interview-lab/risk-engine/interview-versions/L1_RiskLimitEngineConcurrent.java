package org.chijai.exchange

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LEVEL 1 — shared-state concurrent alternative.
 *
 * ConcurrentHashMap makes account lookup safe.
 * synchronized(state) makes the compound read-check-commit invariant atomic.
 *
 * Different accounts can progress independently.
 * Same-account mutations are serialized.
 */
public class L1_RiskLimitEngineConcurrent {

    enum Side { BUY, SELL }

    record Order(int accountId, String ticker, Side side, long price, long quantity) {
        long signedQty() {
            return side == Side.BUY ? quantity : -quantity;
        }
    }

    static class AccountState {
        final Map<String, Long> positions = new HashMap<>();
        final long maxAbsPosition;
        final long maxNotionalPerSecond;

        long bucketSecond = Long.MIN_VALUE;
        long bucketNotional;

        AccountState(long maxAbsPosition, long maxNotionalPerSecond) {
            this.maxAbsPosition = maxAbsPosition;
            this.maxNotionalPerSecond = maxNotionalPerSecond;
        }
    }

    static class RiskEngine {
        final ConcurrentHashMap<Integer, AccountState> accounts =
                new ConcurrentHashMap<>();

        void register(int accountId, long maxPosition, long maxNotionalPerSecond) {
            accounts.put(accountId,
                    new AccountState(maxPosition, maxNotionalPerSecond));
        }

        String validate(Order order, long nowMs) {
            AccountState state = accounts.get(order.accountId());
            if (state == null) return "REJECT: unknown account";

            synchronized (state) {
                long oldPosition =
                        state.positions.getOrDefault(order.ticker(), 0L);

                long candidatePosition =
                        Math.addExact(oldPosition, order.signedQty());

                long second = Math.floorDiv(nowMs, 1_000L);

                long oldBucketNotional =
                        second == state.bucketSecond
                                ? state.bucketNotional
                                : 0L;

                long orderNotional =
                        Math.multiplyExact(order.price(), order.quantity());

                long candidateNotional =
                        Math.addExact(oldBucketNotional, orderNotional);

                if (candidatePosition > state.maxAbsPosition
                        || candidatePosition < -state.maxAbsPosition) {
                    return "REJECT: position";
                }

                if (candidateNotional > state.maxNotionalPerSecond) {
                    return "REJECT: rate";
                }

                state.positions.put(order.ticker(), candidatePosition);
                state.bucketSecond = second;
                state.bucketNotional = candidateNotional;

                return "ACCEPT";
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        RiskEngine engine = new RiskEngine();
        engine.register(42, 100, 1_000_000);

        Thread a = new Thread(() ->
                System.out.println(engine.validate(
                        new Order(42, "AAPL", Side.BUY, 100, 60), 10_000)));

        Thread b = new Thread(() ->
                System.out.println(engine.validate(
                        new Order(42, "AAPL", Side.BUY, 100, 60), 10_000)));

        a.start();
        b.start();
        a.join();
        b.join();
    }
}
