package org.chijai.exchange

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * LEVEL 4 — partitioned single-owner workers.
 *
 * ALL state mutations for an account route through the same partition queue:
 *
 * Order
 * Cancel
 * Config
 * Kill switch
 *
 * That is what makes "no locks inside partition state" defensible.
 */
public class L4_RiskLimitEnginePartitionedOwner {

    sealed interface Event permits OrderEvent, ConfigEvent, CancelEvent, KillEvent, StopEvent {
        int accountId();
    }

    record OrderEvent(
            int accountId,
            long quantity,
            CompletableFuture<String> reply
    ) implements Event {}

    record ConfigEvent(
            int accountId,
            long limit
    ) implements Event {}

    record CancelEvent(
            int accountId,
            long quantity
    ) implements Event {}

    record KillEvent(
            int accountId,
            boolean active
    ) implements Event {}

    record StopEvent(int accountId) implements Event {}

    static class State {
        long position;
        long limit;
        boolean killed;

        State(long limit) {
            this.limit = limit;
        }
    }

    static class Partition implements Runnable {
        final BlockingQueue<Event> queue = new ArrayBlockingQueue<>(128);
        final Map<Integer, State> states = new HashMap<>();
        final Thread worker;

        Partition(int id) {
            worker = new Thread(this, "partition-" + id);
            worker.start();
        }

        boolean offer(Event event) {
            return queue.offer(event);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Event event = queue.take();

                    if (event instanceof StopEvent) return;

                    if (event instanceof ConfigEvent config) {
                        states.put(config.accountId(),
                                new State(config.limit()));
                        continue;
                    }

                    State state = states.get(event.accountId());

                    if (event instanceof KillEvent kill) {
                        if (state != null) state.killed = kill.active();
                        continue;
                    }

                    if (event instanceof CancelEvent cancel) {
                        if (state != null) state.position -= cancel.quantity();
                        continue;
                    }

                    OrderEvent order = (OrderEvent) event;

                    if (state == null) {
                        order.reply().complete("REJECT: unknown");
                    } else if (state.killed) {
                        order.reply().complete("REJECT: kill switch");
                    } else {
                        long candidate =
                                Math.addExact(state.position, order.quantity());

                        if (candidate > state.limit) {
                            order.reply().complete("REJECT: position");
                        } else {
                            state.position = candidate;
                            order.reply().complete("ACCEPT");
                        }
                    }

                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    static class RiskEngine implements AutoCloseable {
        final Partition[] partitions;

        RiskEngine(int count) {
            partitions = new Partition[count];
            for (int i = 0; i < count; i++) {
                partitions[i] = new Partition(i);
            }
        }

        Partition partitionFor(int accountId) {
            return partitions[Math.floorMod(accountId, partitions.length)];
        }

        void config(int accountId, long limit) {
            submit(new ConfigEvent(accountId, limit));
        }

        void kill(int accountId, boolean active) {
            submit(new KillEvent(accountId, active));
        }

        void cancel(int accountId, long quantity) {
            submit(new CancelEvent(accountId, quantity));
        }

        String validate(int accountId, long quantity) {
            CompletableFuture<String> reply = new CompletableFuture<>();
            OrderEvent event = new OrderEvent(accountId, quantity, reply);

            if (!partitionFor(accountId).offer(event)) {
                return "REJECT: overload";
            }

            return reply.join();
        }

        void submit(Event event) {
            if (!partitionFor(event.accountId()).offer(event)) {
                throw new IllegalStateException("control queue full");
            }
        }

        @Override
        public void close() throws Exception {
            for (int i = 0; i < partitions.length; i++) {
                partitions[i].queue.put(new StopEvent(i));
            }

            for (Partition partition : partitions) {
                partition.worker.join();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        try (RiskEngine engine = new RiskEngine(4)) {
            engine.config(42, 100);

            System.out.println(engine.validate(42, 50));

            engine.kill(42, true);
            System.out.println(engine.validate(42, 1));

            engine.kill(42, false);
            engine.cancel(42, 50);
            System.out.println(engine.validate(42, 100));
        }
    }
}
