package org.chijai.exchange

import java.util.HashMap;
import java.util.Map;

/**
 * LEVEL 0 — simplest correct interview version.
 *
 * Assumptions:
 * - one caller/owner mutates state;
 * - position is signed: BUY +qty, SELL -qty;
 * - rate limit is submitted notional in a fixed epoch-second bucket;
 * - rejected order changes no state.
 */
public class L0_RiskLimitEngineSingleOwner {

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
        final Map<Integer, AccountState> accounts = new HashMap<>();

        void register(int accountId, long maxPosition, long maxNotionalPerSecond) {
            accounts.put(accountId,
                    new AccountState(maxPosition, maxNotionalPerSecond));
        }

        String validate(Order order, long nowMs) {
            AccountState state = accounts.get(order.accountId());
            if (state == null) return "REJECT: unknown account";

            long oldPosition = state.positions.getOrDefault(order.ticker(), 0L);
            long candidatePosition =
                    Math.addExact(oldPosition, order.signedQty());

            long second = Math.floorDiv(nowMs, 1_000L);
            long oldBucketNotional =
                    second == state.bucketSecond ? state.bucketNotional : 0L;

            long orderNotional =
                    Math.multiplyExact(order.price(), order.quantity());
            long candidateNotional =
                    Math.addExact(oldBucketNotional, orderNotional);

            // Validate ALL candidate state before mutating ANY state.
            if (candidatePosition > state.maxAbsPosition
                    || candidatePosition < -state.maxAbsPosition) {
                return "REJECT: position";
            }

            if (candidateNotional > state.maxNotionalPerSecond) {
                return "REJECT: rate";
            }

            // Commit ALL after every check passes.
            state.positions.put(order.ticker(), candidatePosition);
            state.bucketSecond = second;
            state.bucketNotional = candidateNotional;

            return "ACCEPT";
        }
    }

    public static void main(String[] args) {
        RiskEngine engine = new RiskEngine();
        engine.register(42, 100, 100_000);

        long t = 10_000;

        System.out.println(engine.validate(
                new Order(42, "AAPL", Side.BUY, 100, 50), t));
        System.out.println(engine.validate(
                new Order(42, "AAPL", Side.BUY, 100, 60), t));
        System.out.println(engine.validate(
                new Order(42, "AAPL", Side.SELL, 100, 20), t));
    }
}
