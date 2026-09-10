package org.chijai.exchange

import java.util.HashMap;
import java.util.Map;

/**
 * LEVEL 3 — explicit partition organization, still caller-thread execution.
 *
 * This file introduces:
 *
 *     accountId -> partition -> account state
 *
 * It does NOT magically create more concurrency.
 * There are no worker threads and no queues here.
 */
public class L3_RiskLimitEnginePartitioned {

    record Order(int accountId, long quantity) {}

    static class AccountState {
        long position;
        final long limit;

        AccountState(long limit) {
            this.limit = limit;
        }
    }

    static class Partition {
        final Map<Integer, AccountState> states = new HashMap<>();

        void register(int accountId, long limit) {
            states.put(accountId, new AccountState(limit));
        }

        String validate(Order order) {
            AccountState state = states.get(order.accountId());
            if (state == null) return "REJECT: unknown";

            synchronized (state) {
                long candidate =
                        Math.addExact(state.position, order.quantity());

                if (candidate > state.limit) {
                    return "REJECT: position";
                }

                state.position = candidate;
                return "ACCEPT";
            }
        }
    }

    static class RiskEngine {
        final Partition[] partitions;

        RiskEngine(int count) {
            partitions = new Partition[count];
            for (int i = 0; i < count; i++) {
                partitions[i] = new Partition();
            }
        }

        Partition partitionFor(int accountId) {
            return partitions[Math.floorMod(accountId, partitions.length)];
        }

        void register(int accountId, long limit) {
            partitionFor(accountId).register(accountId, limit);
        }

        String validate(Order order) {
            return partitionFor(order.accountId()).validate(order);
        }
    }

    public static void main(String[] args) {
        RiskEngine engine = new RiskEngine(4);
        engine.register(42, 100);

        System.out.println(engine.validate(new Order(42, 50)));
        System.out.println(engine.validate(new Order(42, 60)));
    }
}
