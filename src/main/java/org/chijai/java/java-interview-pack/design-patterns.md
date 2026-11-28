# Design patterns and low-level design

**Baseline:** Java 17+ (Java 21 features marked), Spring Boot 3.x, PostgreSQL where applicable. P0 = closed-book recall; P1 = explain and implement with documentation. Snippets are focused fragments, not standalone projects.

## P0 — SOLID with trade-offs
S: cohesive reasons to change. O: extend stable behavior without editing central logic unnecessarily. L: subtype preserves behavioral contract. I: small client-specific interfaces. D: high-level policy depends on abstraction, not infrastructure. Avoid speculative interfaces and excessive layers.

## P0 — Strategy and factory
```java
interface PricingStrategy { BigDecimal price(Booking booking); }
final class PricingService {
    private final Map<String,PricingStrategy> strategies;
    PricingService(Map<String,PricingStrategy> strategies){this.strategies=Map.copyOf(strategies);}
    BigDecimal quote(String type, Booking booking){
        var strategy=strategies.get(type);
        if(strategy==null) throw new IllegalArgumentException("Unknown pricing type");
        return strategy.price(booking);
    }
}
```
Strategy swaps algorithms; factory centralizes creation; abstract factory creates related families. Spring DI can replace manual factories for known implementations, but runtime selection still needs policy.

## P0 — Adapter, decorator and proxy
```java
interface PaymentGateway { Receipt charge(PaymentRequest request); }
final class ProviderAdapter implements PaymentGateway {
    private final VendorClient client;
    ProviderAdapter(VendorClient client){this.client=client;}
    public Receipt charge(PaymentRequest request){return translate(client.pay(toVendor(request)));}
}
```
Adapter changes interface; decorator adds behavior while preserving interface; proxy controls access (Spring AOP, remote/lazy). Decorate with metrics or authorization carefully; avoid logging payment secrets. Facade simplifies subsystem interface.

## P1 — Other patterns
Builder: many optional construction parameters, enforce invariants at build. Observer: one-to-many notifications; define sync/async and failure semantics. Template Method: fixed algorithm with subclass hooks; Strategy often favors composition. State: allowed behavior depends on state; model booking transitions explicitly. Chain of Responsibility: ordered validation/risk checks; define short circuit. Repository: persistence abstraction. Singleton: one instance per relevant scope, not a distributed singleton; Spring singleton is per container/bean definition by default.

## P0 — Low-level design interview
Start with use cases, invariants, entities/value objects, interfaces, responsibilities, state transitions, concurrency, errors, tests. Design booking, parking lot, risk rules engine, notification service. For risk checks: Rule interface, ordered rule evaluation, immutable input/result, rule version, fail-open/closed policy decided by risk class, deterministic audit trail. Prefer pure domain functions and infrastructure adapters.

## Recall drills
- [ ] Implement payment provider adapter and pricing strategy.
- [ ] Design booking state machine with illegal transition rejection.
- [ ] Explain where composition beats inheritance.
- [ ] Refactor giant conditional into rule chain only when complexity warrants it.
