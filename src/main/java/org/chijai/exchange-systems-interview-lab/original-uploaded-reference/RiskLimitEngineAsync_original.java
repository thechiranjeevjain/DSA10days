package org.chijai.exchange;

import org.chijai.PTR.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * LEVEL 2 — Message Bus / Event-Driven
 *
 * <p>Compile from this source directory:
 * {@code javac -d out RiskLimitEngine.java RiskLimitEngineAsync.java}
 *
 * <p>Run: {@code java -cp out org.chijai.PTR.RiskLimitEngineAsync}
 *
 * <p>Purpose: simulate how RiskLimitEngine connects to other exchange
 * components through a message bus: matching engine, participants, config,
 * and operator commands.
 *
 * <p>Reference notes for the real Gen3 system:
 *
 * <pre>
 * applicationContainer.addMessageHandler(RxRiskLimit.class, this::onRxRiskLimit)
 * enterOrderReceiver.setOrderEventListener(this)
 * updateOrderReceiver.setOrderEventListener(this)
 * </pre>
 *
 * <p>This file simulates:
 *
 * <pre>
 * MatchingEngine  -> OrderEvent   -> RiskEngine handles it
 * RDM/Config      -> ConfigEvent  -> RiskEngine registers limits
 * OperatorConsole -> CommandEvent -> KillSwitch changes
 * </pre>
 *
 * <p>Level 1 is a direct call:
 *
 * <pre>
 * RiskEngine.validate(order)
 * </pre>
 *
 * <p>Level 2 introduces an integration boundary:
 *
 * <pre>
 * MessageBus.publish(event) -> registered handler processes it
 * </pre>
 *
 * <p>The bus is synchronous and in-process. `CompletableFuture` represents the
 * reply carried back to the caller; it does not make dispatch asynchronous.
 */

// ── Event types ─────────────────────────────────────────────────────────────

/** Sent by the matching engine when an order requires a risk decision. */
class OrderEvent {
    final Order order;
    final long nowMs;
    final CompletableFuture<ExposureResults> reply;

    OrderEvent(Order order) {
        this.order = order;
        this.nowMs = System.currentTimeMillis();
        this.reply = new CompletableFuture<>();
    }
}

/** Sent by reference-data/config processing at startup or during an update. */
class ConfigEvent {
    final Account account;
    final RiskCheckGroup group;

    ConfigEvent(Account account, RiskCheckGroup group) {
        this.account = account;
        this.group = group;
    }
}

enum CommandType {
    KILL_SWITCH_ON,
    KILL_SWITCH_OFF,
    CANCEL
}

/** Sent by an operator or by the order-lifecycle integration boundary. */
class CommandEvent {
    final Account account;
    final CommandType type;
    final Order order;

    CommandEvent(Account account, CommandType type) {
        this.account = account;
        this.type = type;
        this.order = null;
    }

    CommandEvent(Account account, Order order) {
        this.account = account;
        this.type = CommandType.CANCEL;
        this.order = order;
    }
}

// ── Typed synchronous message bus ───────────────────────────────────────────

class MessageBus {
    private final Map<Class<?>, List<Consumer<Object>>> handlers =
            new HashMap<>();

    @SuppressWarnings("unchecked")
    <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
        handlers.computeIfAbsent(eventType, ignored -> new ArrayList<>())
                .add(event -> handler.accept((T) event));
    }

    void publish(Object event) {
        List<Consumer<Object>> subscribers = handlers.get(event.getClass());
        if (subscribers != null) {
            subscribers.forEach(handler -> handler.accept(event));
        }
    }
}

// ── Event listener that adapts messages to the Level-1 engine ───────────────

class RiskEngineListener {
    private final RiskEngine engine;
    private final Map<Integer, KillSwitchCheck> killSwitches = new HashMap<>();

    RiskEngineListener(RiskEngine engine, MessageBus bus) {
        this.engine = engine;
        bus.subscribe(OrderEvent.class, this::onOrderEvent);
        bus.subscribe(ConfigEvent.class, this::onConfigEvent);
        bus.subscribe(CommandEvent.class, this::onCommandEvent);
    }

    /** Matching engine submits an order and waits for the risk decision. */
    private void onOrderEvent(OrderEvent event) {
        ExposureResults result = engine.validate(event.order, event.nowMs);
        event.reply.complete(result);
    }

    /** Reference/config input registers an account and its checks. */
    private void onConfigEvent(ConfigEvent event) {
        KillSwitchCheck killSwitch = new KillSwitchCheck();
        killSwitches.put(event.account.id, killSwitch);
        event.group.addCheck(killSwitch);
        engine.register(event.account, event.group);
        System.out.println("  [Config] registered: " + event.account.name);
    }

    /** Operator or order-lifecycle command enters through the same bus. */
    private void onCommandEvent(CommandEvent event) {
        switch (event.type) {
            case KILL_SWITCH_ON -> {
                KillSwitchCheck killSwitch =
                        killSwitches.get(event.account.id);
                if (killSwitch != null) {
                    killSwitch.activate();
                    System.out.println(
                            "  [Command] KILL SWITCH ON: " + event.account);
                }
            }
            case KILL_SWITCH_OFF -> {
                KillSwitchCheck killSwitch =
                        killSwitches.get(event.account.id);
                if (killSwitch != null) {
                    killSwitch.deactivate();
                    System.out.println(
                            "  [Command] KILL SWITCH OFF: " + event.account);
                }
            }
            case CANCEL -> {
                if (event.order != null) {
                    engine.onCancel(event.order);
                }
            }
        }
    }
}

// ── Message-bus-aware façade ────────────────────────────────────────────────

class RiskEngineOnBus {
    final MessageBus bus = new MessageBus();
    final RiskEngine engine = new RiskEngine();
    final RiskEngineListener listener;

    RiskEngineOnBus() {
        listener = new RiskEngineListener(engine, bus);
    }

    /** Submit via the bus and synchronously wait for the completed decision. */
    ExposureResults submitOrder(Order order) {
        OrderEvent event = new OrderEvent(order);
        bus.publish(event);
        try {
            return event.reply.get();
        } catch (Exception exception) {
            ExposureResults result = new ExposureResults();
            result.reject("ERROR: " + exception.getMessage());
            return result;
        }
    }

    void sendConfig(Account account, RiskCheckGroup group) {
        bus.publish(new ConfigEvent(account, group));
    }

    void sendCommand(CommandEvent command) {
        bus.publish(command);
    }
}

// ── Entry point — always the final top-level type ───────────────────────────

public class RiskLimitEngineAsync {

    public static void main(String[] args) {
        RiskEngineOnBus system = new RiskEngineOnBus();

        // 1. Reference/config input at startup.
        System.out.println("=== Config from RDM (startup) ===");
        Account accountA = new Account(42, "TradingDeskA");
        RiskCheckGroup groupA = new RiskCheckGroup();
        groupA.addCheck(new TotalTradedPerTimeCheck(500_000));
        groupA.addCheck(new MaxQtyCheck("AAPL", 1_000));
        system.sendConfig(accountA, groupA);

        Account accountB = new Account(99, "TradingDeskB");
        RiskCheckGroup groupB = new RiskCheckGroup();
        groupB.addCheck(new TotalTradedPerTimeCheck(300_000));
        groupB.addCheck(new MaxQtyCheck("MSFT", 500));
        system.sendConfig(accountB, groupB);

        // 2. Matching engine submits orders.
        System.out.println("\n=== Orders from Matching Engine ===");
        show("A buy 100@100", system.submitOrder(
                new Order(accountA, "AAPL", Side.Buy, 100, 100)));
        show("A buy 950@100", system.submitOrder(
                new Order(accountA, "AAPL", Side.Buy, 100, 950)));
        show("B buy 200@100", system.submitOrder(
                new Order(accountB, "MSFT", Side.Buy, 100, 200)));
        show("unknown       ", system.submitOrder(
                new Order(new Account(7, "X"), "AAPL", Side.Buy, 100, 1)));

        // 3. Operator activates and deactivates the kill switch.
        System.out.println("\n=== Operator command: Kill Switch ===");
        system.sendCommand(
                new CommandEvent(accountA, CommandType.KILL_SWITCH_ON));
        show("A buy (blocked)", system.submitOrder(
                new Order(accountA, "AAPL", Side.Buy, 100, 10)));
        system.sendCommand(
                new CommandEvent(accountA, CommandType.KILL_SWITCH_OFF));
        show("A buy (restored)", system.submitOrder(
                new Order(accountA, "AAPL", Side.Buy, 100, 10)));

        // 4. Matching engine sends a cancellation.
        System.out.println("\n=== Cancel from Matching Engine ===");
        Order orderToCancel =
                new Order(accountA, "AAPL", Side.Buy, 100, 100);
        system.sendCommand(new CommandEvent(accountA, orderToCancel));
        System.out.println("  Cancel sent for: " + orderToCancel);
    }

    static void show(String label, ExposureResults results) {
        System.out.printf("  %-18s → %s%n", label, results);
    }
}
