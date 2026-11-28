package org.chijai.PTR;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LEVEL 1 — Foundation + Concurrent-Safe RiskEngine
 *
 * <p>All shared foundation classes are package-accessible (no private
 * modifier) so RiskLimitEngineAsync.java and RiskLimitEnginePartitioned.java
 * can extend them.
 *
 * <p>Foundation inherited by all levels:
 *
 * <ul>
 *   <li>Account, Order, ExposureResults</li>
 *   <li>RiskCheck interface</li>
 *   <li>AbstractRiskCheck — consumption, limit, begin, rollback</li>
 *   <li>MaxQtyCheck, TotalTradedPerTimeCheck, KillSwitchCheck</li>
 *   <li>RiskCheckGroup</li>
 * </ul>
 *
 * <p>Level 1 — RiskEngine:
 *
 * <ul>
 *   <li>ConcurrentHashMap + synchronized(group)</li>
 *   <li>Different accounts: parallel. Same account: serialized.</li>
 * </ul>
 */
// ── Shared model ────────────────────────────────────────────────────────────

enum Side {
    Buy,
    Sell
}

enum Result {
    PASS,
    BREACH,
    UNBREACH
}

class Account {
    final int id;
    final String name;

    Account(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

class Order {
    final Account account;
    final String ticker;
    final Side side;
    final long price;
    final long quantity;

    Order(Account account, String ticker, Side side, long price,
          long quantity) {
        this.account = Objects.requireNonNull(account, "account");
        this.ticker = Objects.requireNonNull(ticker, "ticker");
        this.side = Objects.requireNonNull(side, "side");
        if (ticker.isBlank()) {
            throw new IllegalArgumentException("ticker must not be blank");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("price must be positive");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return account + " " + side + " " + quantity + " " + ticker
                + " @" + price;
    }
}

class ExposureResults {
    boolean accepted = true;
    String reason = "OK";

    void reject(String rejectionReason) {
        accepted = false;
        reason = rejectionReason;
    }

    @Override
    public String toString() {
        return accepted ? "ACCEPTED" : "REJECTED — " + reason;
    }
}

// ── Foundation 1 — RiskCheck interface ─────────────────────────────────────

interface RiskCheck {
    Result check(Order order, long nowMs, ExposureResults results);

    void begin();

    void rollback();
}

// ── Foundation 2 — AbstractRiskCheck ────────────────────────────────────────

abstract class AbstractRiskCheck implements RiskCheck {
    final String name;
    final long limit;
    long consumption;
    private long lastConsumption;

    AbstractRiskCheck(String name, long limit) {
        this.name = Objects.requireNonNull(name, "name");
        if (limit < 0) {
            throw new IllegalArgumentException("limit must not be negative");
        }
        this.limit = limit;
    }

    @Override
    public void begin() {
        lastConsumption = consumption;
    }

    @Override
    public void rollback() {
        consumption = lastConsumption;
    }

    @Override
    public String toString() {
        return name + "[" + consumption + "/" + limit + "]";
    }
}

// ── Concrete checks — each is one class, one override ──────────────────────

/**
 * Historical interview name retained for compatibility with the evolution
 * files. Semantically this is a per-ticker signed position-limit check:
 * BUY adds quantity, SELL subtracts quantity, and absolute position is bounded.
 */
class MaxQtyCheck extends AbstractRiskCheck {
    final String ticker;

    MaxQtyCheck(String ticker, long limit) {
        super("MaxQty[" + ticker + "]", limit);
        this.ticker = ticker;
    }

    @Override
    public Result check(Order order, long nowMs, ExposureResults results) {
        if (!order.ticker.equals(ticker)) {
            return Result.PASS;
        }

        long candidatePosition;
        try {
            candidatePosition = Math.addExact(consumption, signedQuantity(order));
        } catch (ArithmeticException overflow) {
            results.reject("POSITION OVERFLOW " + this);
            return Result.BREACH;
        }

        // Avoid Math.abs(Long.MIN_VALUE), which itself overflows.
        if (candidatePosition > limit || candidatePosition < -limit) {
            results.reject("POSITION BREACH " + this);
            return Result.BREACH;
        }
        consumption = candidatePosition;
        return Result.PASS;
    }

    void onCancel(Order order) {
        if (!order.ticker.equals(ticker)) {
            return;
        }
        try {
            // Cancellation reverses the signed contribution of the accepted order.
            consumption = Math.subtractExact(consumption, signedQuantity(order));
        } catch (ArithmeticException overflow) {
            throw new IllegalStateException("position overflow while cancelling " + order, overflow);
        }
    }

    private long signedQuantity(Order order) {
        return order.side == Side.Buy
                ? order.quantity
                : Math.negateExact(order.quantity);
    }
}

class TotalTradedPerTimeCheck extends AbstractRiskCheck {
    private long windowStartMs;
    private long lastWindowMs;

    TotalTradedPerTimeCheck(long limit) {
        super("NotionalPerSec", limit);
    }

    @Override
    public void begin() {
        super.begin();
        lastWindowMs = windowStartMs;
    }

    @Override
    public void rollback() {
        super.rollback();
        windowStartMs = lastWindowMs;
    }

    @Override
    public Result check(Order order, long nowMs, ExposureResults results) {
        if (nowMs - windowStartMs >= 1_000L) {
            windowStartMs = nowMs;
            consumption = 0;
        }
        long notional;
        long candidateConsumption;
        try {
            notional = Math.multiplyExact(order.price, order.quantity);
            candidateConsumption = Math.addExact(consumption, notional);
        } catch (ArithmeticException overflow) {
            results.reject("NOTIONAL OVERFLOW " + this);
            return Result.BREACH;
        }
        if (candidateConsumption > limit) {
            results.reject("NOTIONAL BREACH " + this);
            return Result.BREACH;
        }
        consumption = candidateConsumption;
        return Result.PASS;
    }
}

class KillSwitchCheck extends AbstractRiskCheck {
    private boolean active;

    KillSwitchCheck() {
        super("KillSwitch", Long.MAX_VALUE);
    }

    void activate() {
        active = true;
    }

    void deactivate() {
        active = false;
    }

    @Override
    public Result check(Order order, long nowMs, ExposureResults results) {
        if (active) {
            results.reject("KILL SWITCH active");
            return Result.BREACH;
        }
        return Result.PASS;
    }
}

// ── Foundation 3 — RiskCheckGroup ──────────────────────────────────────────

class RiskCheckGroup {
    final List<RiskCheck> checks = new ArrayList<>();

    void addCheck(RiskCheck check) {
        checks.add(check);
    }

    void begin() {
        checks.forEach(RiskCheck::begin);
    }

    void rollback() {
        checks.forEach(RiskCheck::rollback);
    }

    Result check(Order order, long nowMs, ExposureResults results) {
        for (RiskCheck check : checks) {
            if (check.check(order, nowMs, results) == Result.BREACH) {
                return Result.BREACH;
            }
        }
        return Result.PASS;
    }

    void onCancel(Order order) {
        for (RiskCheck check : checks) {
            if (check instanceof MaxQtyCheck maxQtyCheck) {
                maxQtyCheck.onCancel(order);
            }
        }
    }
}

// ── Level 1 — RiskEngine ───────────────────────────────────────────────────

class RiskEngine {
    protected final ConcurrentHashMap<Integer, RiskCheckGroup> groups =
            new ConcurrentHashMap<>();

    void register(Account account, RiskCheckGroup group) {
        groups.put(account.id, group);
    }

    ExposureResults validate(Order order, long nowMs) {
        ExposureResults results = new ExposureResults();
        RiskCheckGroup group = groups.get(order.account.id);
        if (group == null) {
            results.reject("no group for " + order.account);
            return results;
        }

        synchronized (group) {
            group.begin();
            if (group.check(order, nowMs, results) == Result.BREACH) {
                group.rollback();
            }
        }
        return results;
    }

    void onCancel(Order order) {
        RiskCheckGroup group = groups.get(order.account.id);
        if (group != null) {
            synchronized (group) {
                group.onCancel(order);
            }
        }
    }
}

// ── Entry point for Level 1 demo ───────────────────────────────────────────

public class RiskLimitEngine {

    public static void main(String[] args) {
        RiskEngine engine = new RiskEngine();

        Account account = new Account(42, "TradingDeskA");
        RiskCheckGroup group = new RiskCheckGroup();
        group.addCheck(new KillSwitchCheck());
        group.addCheck(new TotalTradedPerTimeCheck(500_000));
        group.addCheck(new MaxQtyCheck("AAPL", 1_000));
        engine.register(account, group);

        long time = System.currentTimeMillis();
        show("buy 100@100 ", engine.validate(
                new Order(account, "AAPL", Side.Buy, 100, 100), time));
        show("buy 950@100 ", engine.validate(
                new Order(account, "AAPL", Side.Buy, 100, 950), time));
        show("buy 600@1000", engine.validate(
                new Order(account, "AAPL", Side.Buy, 1_000, 600), time));
        show("new second  ", engine.validate(
                new Order(account, "AAPL", Side.Buy, 100, 50),
                time + 1_001));
        engine.onCancel(new Order(
                account, "AAPL", Side.Buy, 100, 50));
        show("unknown     ", engine.validate(
                new Order(new Account(99, "X"), "AAPL", Side.Buy, 100, 1),
                time));
    }

    static void show(String label, ExposureResults results) {
        System.out.printf("%-14s → %s%n", label, results);
    }
}
