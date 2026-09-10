package org.chijai.exchange

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * LEVEL 2 — synchronous in-process event integration.
 *
 * Important:
 * MessageBus.publish() directly calls handlers.
 * CompletableFuture carries the reply; it does NOT make dispatch asynchronous.
 */
public class L2_RiskLimitEngineOnBus {

    enum Side { BUY, SELL }

    record Order(int accountId, String ticker, Side side, long price, long quantity) {}

    record OrderEvent(
            Order order,
            CompletableFuture<String> reply
    ) {}

    record ConfigEvent(
            int accountId,
            long maxPosition
    ) {}

    static class MessageBus {
        private final Map<Class<?>, List<Consumer<Object>>> handlers =
                new HashMap<>();

        <T> void subscribe(Class<T> type, Consumer<T> handler) {
            handlers.computeIfAbsent(type, ignored -> new ArrayList<>())
                    .add(event -> handler.accept(type.cast(event)));
        }

        void publish(Object event) {
            for (Consumer<Object> handler :
                    handlers.getOrDefault(event.getClass(), List.of())) {
                handler.accept(event);
            }
        }
    }

    static class RiskEngine {
        final Map<Integer, Long> position = new HashMap<>();
        final Map<Integer, Long> limit = new HashMap<>();

        void register(int accountId, long maxPosition) {
            limit.put(accountId, maxPosition);
        }

        String validate(Order order) {
            Long max = limit.get(order.accountId());
            if (max == null) return "REJECT: unknown";

            long old = position.getOrDefault(order.accountId(), 0L);
            long signed = order.side() == Side.BUY
                    ? order.quantity()
                    : -order.quantity();
            long candidate = Math.addExact(old, signed);

            if (candidate > max || candidate < -max) {
                return "REJECT: position";
            }

            position.put(order.accountId(), candidate);
            return "ACCEPT";
        }
    }

    public static void main(String[] args) {
        MessageBus bus = new MessageBus();
        RiskEngine engine = new RiskEngine();

        bus.subscribe(ConfigEvent.class,
                e -> engine.register(e.accountId(), e.maxPosition()));

        bus.subscribe(OrderEvent.class,
                e -> e.reply().complete(engine.validate(e.order())));

        bus.publish(new ConfigEvent(42, 100));

        CompletableFuture<String> reply = new CompletableFuture<>();
        bus.publish(new OrderEvent(
                new Order(42, "AAPL", Side.BUY, 100, 50),
                reply));

        System.out.println(reply.join());
    }
}
