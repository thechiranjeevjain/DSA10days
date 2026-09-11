package org.chijai.PTR;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

enum Side {
    Buy,
    Sell
}

enum Result {
    PASS,
    BREACH
}

class Account {

    final int id;
    final String name;

    Account(int id, String name) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String toString() {
        return name;
    }
}

class Order {

    final String orderId;
    final Account account;
    final String ticker;
    final String marketSegment;
    final Side side;
    final BigDecimal price;
    final long quantity;

    /*
     * Event time belongs to the order.
     * Processing time comes from the injected Clock in RiskEngine.
     */
    final Instant eventTime;

    Order(
            String orderId,
            Account account,
            String ticker,
            String marketSegment,
            Side side,
            BigDecimal price,
            long quantity,
            Instant eventTime) {

        this.orderId = Objects.requireNonNull(orderId);
        this.account = Objects.requireNonNull(account);
        this.ticker = Objects.requireNonNull(ticker);
        this.marketSegment = Objects.requireNonNull(marketSegment);
        this.side = Objects.requireNonNull(side);
        this.price = Objects.requireNonNull(price);
        this.eventTime = Objects.requireNonNull(eventTime);

        if (price.signum() <= 0) {
            throw new IllegalArgumentException("price must be positive");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }

        this.quantity = quantity;
    }
}

/*
 * The single decision object used outside a RiskCheck.
 *
 * It can be:
 * - returned synchronously to the order path
 * - published to a bus
 * - logged/audited
 *
 * Result is only the status field inside the decision.
 */
record RiskDecision(
        String orderId,
        int accountId,
        String ticker,
        Result result,
        String checkName,
        String reason,
        Instant decidedAt) {

    static RiskDecision pass(
            Order order,
            long nowMs) {

        return new RiskDecision(
                order.orderId,
                order.account.id,
                order.ticker,
                Result.PASS,
                "NONE",
                "OK",
                Instant.ofEpochMilli(nowMs));
    }

    static RiskDecision breach(
            Order order,
            long nowMs,
            String checkName,
            String reason) {

        return new RiskDecision(
                order.orderId,
                order.account.id,
                order.ticker,
                Result.BREACH,
                checkName,
                reason,
                Instant.ofEpochMilli(nowMs));
    }

    boolean accepted() {
        return result == Result.PASS;
    }
}

/*
 * Infrastructure port.
 *
 * Production can inject an asynchronous message-bus adapter.
 * Tests can inject an in-memory/no-op lambda.
 */
@FunctionalInterface
interface RiskDecisionPublisher {

    void publish(RiskDecision decision);
}

/*
 * Market-data port.
 *
 * PriceDeviationCheck does not know where reference prices come from.
 */
@FunctionalInterface
interface ReferencePriceProvider {

    BigDecimal referencePrice(Order order);
}

/*
 * Universal rule contract.
 *
 * Stateful checks override begin/rollback/onCancel.
 * Stateless checks implement only check().
 */
interface RiskCheck {

    RiskDecision check(
            Order order,
            long nowMs);

    default void begin() {
    }

    default void rollback() {
    }

    default void onCancel(Order order) {
    }
}

/*
 * Shared transaction support for checks that own mutable state.
 *
 * T should be immutable (Long, BigDecimal, immutable value object)
 * so snapshot assignment is sufficient.
 */
abstract class AbstractStatefulRiskCheck<T>
        implements RiskCheck {

    final String name;

    T consumption;

    private T lastConsumption;

    AbstractStatefulRiskCheck(
            String name,
            T initialConsumption) {

        this.name = Objects.requireNonNull(name);
        this.consumption =
                Objects.requireNonNull(initialConsumption);
    }

    @Override
    public void begin() {
        lastConsumption = consumption;
    }

    @Override
    public void rollback() {
        consumption = lastConsumption;
    }
}

/*
 * Reusable applicability predicates.
 *
 * The rule and the scope to which it applies are separate concerns.
 */
final class Scopes {

    private Scopes() {
    }

    static Predicate<Order> all() {
        return order -> true;
    }

    static Predicate<Order> ticker(String ticker) {

        return order ->
                order.ticker.equals(ticker);
    }

    static Predicate<Order> tickers(
            Set<String> tickers) {

        Set<String> copy =
                Set.copyOf(tickers);

        return order ->
                copy.contains(order.ticker);
    }

    static Predicate<Order> marketSegment(
            String marketSegment) {

        return order ->
                order.marketSegment.equals(
                        marketSegment);
    }

    static Predicate<Order> side(Side side) {

        return order ->
                order.side == side;
    }
}

/*
 * Accumulated open quantity for any injected scope.
 *
 * Examples:
 * - one ticker BUY
 * - one ticker SELL
 * - collection of tickers
 * - market segment
 * - whole group
 */
class MaxQtyCheck
        extends AbstractStatefulRiskCheck<Long> {

    private final Predicate<Order> appliesTo;

    private final long limit;

    MaxQtyCheck(
            String name,
            Predicate<Order> appliesTo,
            long limit) {

        super(name, 0L);

        this.appliesTo =
                Objects.requireNonNull(appliesTo);

        if (limit < 0) {
            throw new IllegalArgumentException(
                    "limit must be non-negative");
        }

        this.limit = limit;
    }

    @Override
    public RiskDecision check(
            Order order,
            long nowMs) {

        if (!appliesTo.test(order)) {
            return RiskDecision.pass(
                    order,
                    nowMs);
        }

        long candidate =
                Math.addExact(
                        consumption,
                        order.quantity);

        if (candidate > limit) {

            return RiskDecision.breach(
                    order,
                    nowMs,
                    name,
                    "MAX QTY BREACH candidate="
                            + candidate
                            + " limit="
                            + limit);
        }

        consumption = candidate;

        return RiskDecision.pass(
                order,
                nowMs);
    }

    @Override
    public void onCancel(Order order) {

        if (!appliesTo.test(order)) {
            return;
        }

        consumption =
                Math.max(
                        consumption - order.quantity,
                        0L);
    }
}

/*
 * Signed net monetary exposure.
 *
 * BUY  => +price * quantity
 * SELL => -price * quantity
 *
 * BigDecimal is used here for explicit decimal money semantics.
 * A production low-latency venue may instead use fixed-point long/ticks.
 */
class ExposureCheck
        extends AbstractStatefulRiskCheck<BigDecimal> {

    private final Predicate<Order> appliesTo;

    private final BigDecimal limit;

    ExposureCheck(
            String name,
            Predicate<Order> appliesTo,
            BigDecimal limit) {

        super(
                name,
                BigDecimal.ZERO);

        this.appliesTo =
                Objects.requireNonNull(appliesTo);

        this.limit =
                Objects.requireNonNull(limit);

        if (limit.signum() < 0) {
            throw new IllegalArgumentException(
                    "limit must be non-negative");
        }
    }

    @Override
    public RiskDecision check(
            Order order,
            long nowMs) {

        if (!appliesTo.test(order)) {
            return RiskDecision.pass(
                    order,
                    nowMs);
        }

        BigDecimal delta =
                signedValue(order);

        BigDecimal candidate =
                consumption.add(delta);

        if (candidate.abs()
                .compareTo(limit) > 0) {

            return RiskDecision.breach(
                    order,
                    nowMs,
                    name,
                    "EXPOSURE BREACH candidate="
                            + candidate.toPlainString()
                            + " limit="
                            + limit.toPlainString());
        }

        consumption = candidate;

        return RiskDecision.pass(
                order,
                nowMs);
    }

    @Override
    public void onCancel(Order order) {

        if (!appliesTo.test(order)) {
            return;
        }

        consumption =
                consumption.subtract(
                        signedValue(order));
    }

    private BigDecimal signedValue(
            Order order) {

        BigDecimal value =
                order.price.multiply(
                        BigDecimal.valueOf(
                                order.quantity));

        return order.side == Side.Buy
                ? value
                : value.negate();
    }
}

/*
 * Fixed processing-time window.
 *
 * Scope and window duration are constructor-injected.
 */
class TotalTradedPerTimeCheck
        extends AbstractStatefulRiskCheck<BigDecimal> {

    private final Predicate<Order> appliesTo;

    private final BigDecimal limit;

    private final long windowMs;

    private long windowStartMs = -1L;

    private long lastWindowStartMs;

    TotalTradedPerTimeCheck(
            String name,
            Predicate<Order> appliesTo,
            BigDecimal limit,
            long windowMs) {

        super(
                name,
                BigDecimal.ZERO);

        this.appliesTo =
                Objects.requireNonNull(appliesTo);

        this.limit =
                Objects.requireNonNull(limit);

        if (limit.signum() < 0) {
            throw new IllegalArgumentException(
                    "limit must be non-negative");
        }

        if (windowMs <= 0) {
            throw new IllegalArgumentException(
                    "windowMs must be positive");
        }

        this.windowMs = windowMs;
    }

    @Override
    public void begin() {
        super.begin();
        lastWindowStartMs =
                windowStartMs;
    }

    @Override
    public void rollback() {
        super.rollback();
        windowStartMs =
                lastWindowStartMs;
    }

    @Override
    public RiskDecision check(
            Order order,
            long nowMs) {

        if (!appliesTo.test(order)) {
            return RiskDecision.pass(
                    order,
                    nowMs);
        }

        if (windowStartMs < 0
                || nowMs - windowStartMs
                >= windowMs) {

            windowStartMs = nowMs;
            consumption =
                    BigDecimal.ZERO;
        }

        BigDecimal value =
                order.price.multiply(
                        BigDecimal.valueOf(
                                order.quantity));

        BigDecimal candidate =
                consumption.add(value);

        if (candidate.compareTo(limit) > 0) {

            return RiskDecision.breach(
                    order,
                    nowMs,
                    name,
                    "TOTAL PER TIME BREACH candidate="
                            + candidate.toPlainString()
                            + " limit="
                            + limit.toPlainString());
        }

        consumption = candidate;

        return RiskDecision.pass(
                order,
                nowMs);
    }
}

/*
 * Stateless rule.
 *
 * ReferencePriceProvider is injected so this check is independent
 * of market-data storage/transport.
 */
class PriceDeviationCheck
        implements RiskCheck {

    private static final
    BigDecimal BPS =
            BigDecimal.valueOf(10_000L);

    private final String name;

    private final Predicate<Order> appliesTo;

    private final
    ReferencePriceProvider referencePriceProvider;

    private final long maxDeviationBps;

    PriceDeviationCheck(
            String name,
            Predicate<Order> appliesTo,
            ReferencePriceProvider referencePriceProvider,
            long maxDeviationBps) {

        this.name =
                Objects.requireNonNull(name);

        this.appliesTo =
                Objects.requireNonNull(appliesTo);

        this.referencePriceProvider =
                Objects.requireNonNull(
                        referencePriceProvider);

        if (maxDeviationBps < 0) {
            throw new IllegalArgumentException(
                    "maxDeviationBps must be non-negative");
        }

        this.maxDeviationBps =
                maxDeviationBps;
    }

    @Override
    public RiskDecision check(
            Order order,
            long nowMs) {

        if (!appliesTo.test(order)) {
            return RiskDecision.pass(
                    order,
                    nowMs);
        }

        BigDecimal referencePrice =
                referencePriceProvider
                        .referencePrice(order);

        if (referencePrice == null
                || referencePrice.signum() <= 0) {

            return RiskDecision.breach(
                    order,
                    nowMs,
                    name,
                    "REFERENCE PRICE UNAVAILABLE");
        }

        BigDecimal deviation =
                order.price
                        .subtract(referencePrice)
                        .abs();

        /*
         * deviation / referencePrice * 10_000 > maxDeviationBps
         *
         * Cross-multiplication avoids division/rounding:
         *
         * deviation * 10_000
         * >
         * referencePrice * maxDeviationBps
         */
        BigDecimal actual =
                deviation.multiply(BPS);

        BigDecimal allowed =
                referencePrice.multiply(
                        BigDecimal.valueOf(
                                maxDeviationBps));

        if (actual.compareTo(allowed) > 0) {

            return RiskDecision.breach(
                    order,
                    nowMs,
                    name,
                    "PRICE DEVIATION BREACH");
        }

        return RiskDecision.pass(
                order,
                nowMs);
    }
}

/*
 * Kill-switch state is supplied from outside.
 *
 * AtomicBoolean::get is one possible injected BooleanSupplier.
 */
class KillSwitchCheck
        implements RiskCheck {

    private final String name;

    private final Predicate<Order> appliesTo;

    private final BooleanSupplier active;

    KillSwitchCheck(
            String name,
            Predicate<Order> appliesTo,
            BooleanSupplier active) {

        this.name =
                Objects.requireNonNull(name);

        this.appliesTo =
                Objects.requireNonNull(appliesTo);

        this.active =
                Objects.requireNonNull(active);
    }

    @Override
    public RiskDecision check(
            Order order,
            long nowMs) {

        if (appliesTo.test(order)
                && active.getAsBoolean()) {

            return RiskDecision.breach(
                    order,
                    nowMs,
                    name,
                    "KILL SWITCH ACTIVE");
        }

        return RiskDecision.pass(
                order,
                nowMs);
    }
}

/*
 * Owns:
 * - heterogeneous rule composition
 * - transaction snapshot/rollback
 * - synchronization for all mutable check state in this group
 *
 * consumption does NOT need AtomicLong because every state mutation
 * is performed while holding this group monitor.
 */
class RiskCheckGroup {

    final String groupId;

    private final List<RiskCheck> checks =
            new ArrayList<>();

    RiskCheckGroup(String groupId) {
        this.groupId =
                Objects.requireNonNull(groupId);
    }

    RiskCheckGroup addCheck(
            RiskCheck check) {

        checks.add(
                Objects.requireNonNull(check));

        return this;
    }

    synchronized RiskDecision evaluate(
            Order order,
            long nowMs) {

        begin();

        try {

            for (RiskCheck check : checks) {

                RiskDecision decision =
                        check.check(
                                order,
                                nowMs);

                if (!decision.accepted()) {
                    rollback();
                    return decision;
                }
            }

            return RiskDecision.pass(
                    order,
                    nowMs);

        } catch (RuntimeException e) {

            rollback();
            throw e;
        }
    }

    synchronized void onCancel(
            Order order) {

        begin();

        try {

            checks.forEach(
                    check ->
                            check.onCancel(order));

        } catch (RuntimeException e) {

            rollback();
            throw e;
        }
    }

    private void begin() {
        checks.forEach(
                RiskCheck::begin);
    }

    private void rollback() {
        checks.forEach(
                RiskCheck::rollback);
    }
}

/*
 * Composition root dependencies are constructor-injected:
 *
 * Clock                  -> processing time
 * RiskDecisionPublisher  -> bus/audit output
 *
 * RiskCheckGroup owns rule-level concurrency.
 */
class RiskEngine {

    private final
    ConcurrentHashMap<Integer, RiskCheckGroup> groups =
            new ConcurrentHashMap<>();

    private final Clock clock;

    private final
    RiskDecisionPublisher publisher;

    RiskEngine(
            Clock clock,
            RiskDecisionPublisher publisher) {

        this.clock =
                Objects.requireNonNull(clock);

        this.publisher =
                Objects.requireNonNull(publisher);
    }

    void register(
            Account account,
            RiskCheckGroup group) {

        groups.put(
                account.id,
                Objects.requireNonNull(group));
    }

    RiskDecision validate(
            Order order) {

        long nowMs =
                clock.millis();

        RiskCheckGroup group =
                groups.get(
                        order.account.id);

        RiskDecision decision;

        if (group == null) {

            decision =
                    RiskDecision.breach(
                            order,
                            nowMs,
                            "RiskEngine",
                            "NO RISK GROUP");

        } else {

            /*
             * The map retains the same group object across validate() calls.
             * The group retains the same RiskCheck objects.
             * Their instance consumption therefore survives between calls
             * until the process is restarted/rebuilt.
             */
            decision =
                    group.evaluate(
                            order,
                            nowMs);
        }

        /*
         * Keep broker-specific code outside RiskEngine.
         * Inject an asynchronous publisher in production if bus latency
         * must stay off the risk hot path.
         */
        publisher.publish(decision);

        return decision;
    }

    void onCancel(
            Order order) {

        RiskCheckGroup group =
                groups.get(
                        order.account.id);

        if (group != null) {
            group.onCancel(order);
        }
    }
}

/*
 * CHANGE ISOLATION
 * ---------------------------------------------------------------
 *
 * limit change
 *     -> constructor/config value
 *
 * ticker / side / segment / ticker-set change
 *     -> injected Predicate<Order>
 *
 * reference-price source change
 *     -> ReferencePriceProvider
 *
 * bus change
 *     -> RiskDecisionPublisher
 *
 * existing rule semantics change
 *     -> that RiskCheck implementation only
 *
 * new rule
 *     -> new RiskCheck class + one addCheck(...) wiring line
 *
 * RiskEngine and RiskCheckGroup stay unchanged.
 *
 * RDM / REST parsing belongs in the composition/configuration layer,
 * not inside the risk-rule classes.
 */

/*
 * Composition root / wiring example.
 *
 * This is where dependencies and behavior are injected.
 */
public class RiskEngineExtensible {

    public static void main(String[] args) {

        Account account =
                new Account(
                        101,
                        "CLIENT-101");

        Set<String> techTickers =
                Set.of(
                        "AAPL",
                        "MSFT",
                        "GOOG");

        Predicate<Order> aaplBuys =
                Scopes.ticker("AAPL")
                        .and(
                                Scopes.side(
                                        Side.Buy));

        Predicate<Order> techSells =
                Scopes.tickers(techTickers)
                        .and(
                                Scopes.side(
                                        Side.Sell));

        Predicate<Order> cashEquities =
                Scopes.marketSegment(
                        "CASH");

        Predicate<Order> allOrders =
                Scopes.all();

        Map<String, BigDecimal> referencePrices =
                Map.of(
                        "AAPL",
                        new BigDecimal("200.00"),
                        "MSFT",
                        new BigDecimal("500.00"),
                        "GOOG",
                        new BigDecimal("180.00"));

        ReferencePriceProvider
                referencePriceProvider =
                order ->
                        referencePrices.get(
                                order.ticker);

        AtomicBoolean killSwitch =
                new AtomicBoolean(false);

        /*
         * In production:
         * decision -> messageBus.publish(decision)
         *
         * The adapter should normally hand off asynchronously.
         */
        RiskDecisionPublisher publisher =
                decision ->
                        System.out.println(
                                "BUS -> " + decision);

        RiskCheckGroup group =
                new RiskCheckGroup(
                        "ACCOUNT-101")

                        /*
                         * Same MaxQtyCheck class.
                         * Different injected scopes.
                         */
                        .addCheck(
                                new MaxQtyCheck(
                                        "AAPL-BUY-OPEN-QTY",
                                        aaplBuys,
                                        1_000))

                        .addCheck(
                                new MaxQtyCheck(
                                        "TECH-SELL-OPEN-QTY",
                                        techSells,
                                        5_000))

                        /*
                         * Whole-account/group exposure.
                         */
                        .addCheck(
                                new ExposureCheck(
                                        "NET-EXPOSURE",
                                        allOrders,
                                        new BigDecimal(
                                                "10000000.00")))

                        /*
                         * Only orders in the CASH market segment.
                         */
                        .addCheck(
                                new TotalTradedPerTimeCheck(
                                        "CASH-VALUE-PER-SECOND",
                                        cashEquities,
                                        new BigDecimal(
                                                "50000000.00"),
                                        1_000L))

                        .addCheck(
                                new PriceDeviationCheck(
                                        "PRICE-DEVIATION",
                                        cashEquities,
                                        referencePriceProvider,
                                        100L))

                        .addCheck(
                                new KillSwitchCheck(
                                        "KILL-SWITCH",
                                        allOrders,
                                        killSwitch::get));

        RiskEngine engine =
                new RiskEngine(
                        Clock.systemUTC(),
                        publisher);

        engine.register(
                account,
                group);

        Order order =
                new Order(
                        "ORD-1",
                        account,
                        "AAPL",
                        "CASH",
                        Side.Buy,
                        new BigDecimal(
                                "201.00"),
                        100,
                        Instant.now());

        RiskDecision decision =
                engine.validate(order);

        System.out.println(
                "CALLER -> " + decision);

        /*
         * Feature changes require composition changes, not engine changes:
         *
         * new scope          -> inject another Predicate<Order>
         * new risk rule      -> implement RiskCheck and addCheck(...)
         * new market source  -> inject another ReferencePriceProvider
         * new bus            -> inject another RiskDecisionPublisher
         * deterministic test -> inject Clock.fixed(...)
         * kill switch source -> inject another BooleanSupplier
         */
    }
}
