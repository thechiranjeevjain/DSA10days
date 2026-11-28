# Spring Boot — Senior Engineer Active-Recall Cheatsheet

**Baseline:** Java 17+, Spring Boot 3.x (Spring Framework 6, Jakarta APIs), Spring MVC, JPA, PostgreSQL. Examples are focused fragments from a hotel booking application, not a standalone compilable project. Imports and obvious supporting types are omitted. Boot 3.2+ is required for RestClient; Boot 3.4+ supports `@MockitoBean`.

**P0:** reproduce from memory. **P1:** implement confidently with documentation. **P2:** recognize and look up. Practice: hide the snippet, reproduce it, run tests, explain one failure mode. Do not memorize imports or dependency versions.

## 01. Bootstrap and dependencies [P0]

```java
@SpringBootApplication
@ConfigurationPropertiesScan
public class HotelApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelApplication.class, args);
    }
}
```

Use Boot's BOM or parent for compatible versions. Typical starters: `spring-boot-starter-web`, `data-jpa`, `validation`, `security`, `oauth2-resource-server`, `actuator`, `cache`, `test` (all with the `spring-boot-starter-` prefix). Add PostgreSQL driver, Flyway (including PostgreSQL support where required), and Testcontainers as appropriate.

Recall: Explain component scanning, auto-configuration and conditional beans.

## 02. Dependency injection and configuration [P0]

```java
@ConfigurationProperties(prefix = "hotel")
@Validated
public record HotelProperties(@NotNull Duration timeout) {}

@Configuration
class HotelConfig {
    @Bean
    Clock clock() { return Clock.systemUTC(); }
}
```

```yaml
hotel:
  timeout: 2s
spring:
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: validate
```

Constructor injection is preferred; a single constructor needs no `@Autowired`. Use `@Qualifier` for ambiguous beans. Keep secrets outside source control and use migrations for schema changes.

Recall: Inject a clock and typed configuration into a service.

## 03. REST controller [P0]

```java
@RestController
@RequestMapping("/hotels")
class HotelController {
    private final HotelService service;
    HotelController(HotelService service) { this.service = service; }

    @GetMapping("/{id}")
    HotelDto get(@PathVariable long id) {
        return service.get(id);
    }

    @PostMapping
    ResponseEntity<HotelDto> create(@Valid @RequestBody CreateHotelRequest request) {
        HotelDto created = service.create(request);
        return ResponseEntity.created(URI.create("/hotels/" + created.id()))
                .body(created);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

HTTP: GET 200/404; POST 201 + Location; DELETE 204; malformed/invalid input 400; conflict 409; unauthenticated 401; forbidden 403. PUT replaces a resource; PATCH applies partial changes. GET must not mutate state.

Recall: Write GET, POST and DELETE without looking.

## 04. DTOs and validation [P0]

```java
public record CreateHotelRequest(
    @NotBlank @Size(max = 100) String name,
    @NotNull @Positive Long cityId
) {}
public record HotelDto(Long id, String name, Long cityId) {}
```

`@Valid` cascades validation. `@Validated` enables validation groups and method validation where configured. Do not expose entities directly. Business invariants still belong in the domain/service and database.

Recall: Reject missing name and nonpositive city ID.

## 05. Entities and relationships [P0]

```java
@Entity
@Table(name = "hotels")
class Hotel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false)
    private Long cityId;
    @Version
    private Long version;
    @Column(nullable = false)
    private boolean deleted;

    protected Hotel() {}
    Hotel(String name, Long cityId) {
        this.name = name;
        this.cityId = cityId;
    }
    Long getId() { return id; }
    String getName() { return name; }
    Long getCityId() { return cityId; }
    void markDeleted() { deleted = true; }
}
```

```java
// Inside Booking entity (other fields omitted)
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "hotel_id", nullable = false)
private Hotel hotel;
```

Use `mappedBy` for inverse collections; avoid blanket `CascadeType.ALL` and eager fetching. Use database foreign keys and indexes. Avoid Lombok `@Data` on entities. Dirty checking persists managed entity changes at flush.

Recall: Map a booking to its hotel and explain relationship ownership.

## 06. Repositories and JPQL [P0]

```java
interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByIdAndDeletedFalse(Long id);
    Page<Hotel> findByCityIdAndDeletedFalse(Long cityId, Pageable pageable);

    @Query("select h from Hotel h where h.cityId = :city and h.deleted = false")
    List<Hotel> findActive(@Param("city") Long city);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Hotel h set h.deleted = true, h.version = h.version + 1 " +
           "where h.id = :id and h.version = :version and h.deleted = false")
    int softDelete(@Param("id") Long id, @Param("version") Long version);
}
```

Bulk updates require a transaction, bypass dirty checking and lifecycle callbacks, and do not automatically honor optimistic locking. Check affected row count. JPQL references entity properties, not SQL column names. Prefer projections or fetch joins/entity graphs for N+1; avoid collection fetch joins with offset pagination.

Recall: Write derived, JPQL and modifying queries; explain N+1.

## 07. Service and transactions [P0]

```java
@Service
class HotelService {
    private final HotelRepository repository;
    HotelService(HotelRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public HotelDto get(long id) {
        return toDto(find(id));
    }

    @Transactional
    public HotelDto create(CreateHotelRequest request) {
        return toDto(repository.save(new Hotel(request.name(), request.cityId())));
    }

    @Transactional
    public void delete(long id) {
        find(id).markDeleted();
    }

    private Hotel find(long id) {
        return repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
    }
    private HotelDto toDto(Hotel h) {
        return new HotelDto(h.getId(), h.getName(), h.getCityId());
    }
}
```

```java
@Transactional(propagation = Propagation.REQUIRES_NEW,
               isolation = Isolation.READ_COMMITTED,
               rollbackFor = IOException.class)
public void independentWork() throws IOException { /* work */ }
```

REQUIRED joins existing transaction (default); REQUIRES_NEW suspends it and starts another; NESTED uses savepoints when supported. Isolation applies to newly started transactions. Runtime exceptions and Errors roll back by default, checked exceptions do not. Proxy self-invocation bypasses advice; public methods on a separate injected bean are a safe default. `readOnly` is a hint, not authorization. Flush is not commit. Do not hold a transaction across slow network calls.

Recall: Explain rollback, propagation, isolation, and proxy boundaries.

## 08. Concurrency and locking [P0]

Optimistic: `@Version` on entity; concurrent stale update fails, typically on flush/commit. Retry the whole transaction only if safe, from outside its boundary. Map genuine conflicts to 409.

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("select h from Hotel h where h.id = :id and h.deleted = false")
Optional<Hotel> findByIdForUpdate(@Param("id") Long id);
```

Use in a transaction. For booking inventory, lock the relevant room/date inventory row, not every hotel unnecessarily. An atomic conditional UPDATE (available > 0) with affected-row check is often simpler. Database unique constraints are the final guard against duplicate reservations; overlapping date ranges need a suitable inventory model or exclusion constraint. Locks can deadlock; keep transactions short and lock in stable order.

Recall: Design two concurrent requests competing for the last room.

## 09. Pagination and filtering [P0]

```java
Pageable pageable = PageRequest.of(page, Math.min(size, 100),
    Sort.by("name").ascending().and(Sort.by("id").ascending()));
Page<HotelDto> result = repository
    .findByCityIdAndDeletedFalse(cityId, pageable).map(this::toDto);
```

Validate page >= 0 and size >= 1; whitelist sort fields. Page includes total count; Slice avoids the total count query; keyset pagination helps deep pages. Return a stable API page DTO rather than relying on raw Page serialization.

Recall: Explain stable ordering and Page vs Slice.

## 10. Global exception handling [P0]

```java
@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(HotelNotFoundException.class)
    ResponseEntity<ProblemDetail> notFound(HotelNotFoundException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, ex.getMessage());
        p.setProperty("code", "HOTEL_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(p);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ProblemDetail> invalid(MethodArgumentNotValidException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "Invalid request");
        p.setProperty("fields", ex.getBindingResult().getFieldErrors().stream()
            .map(e -> Map.of("field", e.getField(), "message",
                Objects.toString(e.getDefaultMessage(), "Invalid"))).toList());
        return ResponseEntity.badRequest().body(p);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    ResponseEntity<ProblemDetail> conflict(OptimisticLockingFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Concurrent update"));
    }
}
```

Handle method-parameter validation and malformed JSON where needed. Map known uniqueness violations to domain conflicts, not every database failure. Never expose stack traces or secrets. Security filter exceptions require security-specific handlers. Unexpected errors should be logged and return generic 500 responses.

Recall: Return structured 400, 404 and 409 errors.

## 11. Security and JWT [P0]

```java
@Configuration
@EnableMethodSecurity
class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/hotels/**").hasAuthority("SCOPE_hotels.write")
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
            .build();
    }
}
```

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${JWT_ISSUER_URI}
```

Configure expected audience validation as well as issuer/signature/expiry. Disable CSRF only for bearer-token APIs not using ambient cookie credentials. CORS is not authentication. Authorize resource ownership, not just roles. Use `@PreAuthorize` at service boundaries as appropriate. Never log tokens.

Recall: Explain authentication vs authorization, 401 vs 403, and JWT verification.

## 12. External API calls [P1]

```java
@Bean
RestClient paymentClient(RestClient.Builder builder) {
    var factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(1000);
    factory.setReadTimeout(2000);
    return builder.baseUrl("https://payments.example.com")
        .requestFactory(factory).build();
}
// In injected client/service:
PaymentResponse response = paymentClient.post().uri("/payments")
    .header("Idempotency-Key", key)
    .body(request).retrieve().body(PaymentResponse.class);
```

RestClient is synchronous; WebClient supports reactive/nonblocking pipelines. Configure connection, read and overall deadlines; pool connections for production throughput. Handle status codes and ambiguous payment outcomes explicitly.

Recall: Call a payment API with bounded timeouts.

## 13. Retry, circuit breaker and timeout [P1]

With compatible Resilience4j Spring Boot integration and AOP:

```java
@Retry(name = "inventory")
@CircuitBreaker(name = "inventory")
public InventoryResponse lookup(long hotelId) {
    return inventoryClient.get(hotelId);
}
```

```yaml
resilience4j:
  retry:
    instances:
      inventory:
        max-attempts: 3
        wait-duration: 200ms
  circuitbreaker:
    instances:
      inventory:
        sliding-window-size: 20
        minimum-number-of-calls: 10
        failure-rate-threshold: 50
        wait-duration-in-open-state: 10s
```

Configure retryable exception classes explicitly, exponential backoff and jitter. Do not retry validation failures or non-idempotent writes blindly. Check advice ordering, total latency budget and transaction boundaries. Circuit breaker is not a timeout; bulkhead limits concurrency.

Recall: Explain what happens when a dependency is slow vs unavailable.

## 14. Caching [P1]

```java
@EnableCaching
@Configuration
class CacheConfig {}

// On a separate service bean:
@Cacheable(cacheNames = "hotels", key = "#id")
public HotelDto get(long id) { return hotelService.get(id); }

@CacheEvict(cacheNames = "hotels", key = "#id")
public void invalidate(long id) {}
```

Configure provider, bounded size and TTL. Invalidate after successful commit (e.g. after-commit event or transaction-aware cache configuration); concurrent cache fills can still produce stale data. Include tenant/security context in keys where relevant. Do not cache live booking availability without a defined consistency policy.

Recall: Design cache invalidation for hotel updates.

## 15. Async and scheduling [P2]

```java
@EnableAsync
@EnableScheduling
@Configuration
class WorkerConfig {
    @Bean("hotelExecutor")
    ThreadPoolTaskExecutor hotelExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        return executor;
    }
}

@Async("hotelExecutor")
public CompletableFuture<Void> sendConfirmation(long bookingId) {
    notificationService.send(bookingId);
    return CompletableFuture.completedFuture(null);
}

@Scheduled(fixedDelayString = "${outbox.poll-ms:1000}")
public void poll() { outboxRelay.runOnce(); }
```

Async work needs its own transaction where required; caller transaction and thread-local context do not automatically propagate. Handle executor rejection and failures. Scheduled methods run in each application replica: coordinate work across replicas. In-memory tasks are not durable.

Recall: Explain why an async task can see uncommitted/missing data.

## 16. Events and transactional outbox [P1]

```java
// Within transactional service:
events.publishEvent(new HotelDeletedEvent(id));

@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void afterCommit(HotelDeletedEvent event) {
    cacheInvalidator.invalidate(event.id());
}
```

AFTER_COMMIT is suitable for best-effort work, not durable delivery. If writing database state after commit, use a new transaction through a separate proxied bean.

```java
@Transactional
public BookingDto book(CreateBookingRequest request) {
    Booking booking = bookingRepository.save(reserveInventory(request));
    outboxRepository.save(OutboxEvent.pending(UUID.randomUUID(),
        "BookingCreated", serialize(booking)));
    return toDto(booking);
}
```

Persist business state and outbox row atomically. Relay claims pending rows (lease or SKIP LOCKED), publishes and marks sent after broker acknowledgement. Crash after publish before marking sent causes duplicates: consumers deduplicate using event ID in the same transaction as their effect. Add retries, monitoring, dead-letter handling and ordering rules. Do not claim exactly-once end-to-end.

Recall: Draw the failure windows of direct publishing vs outbox.

## 17. Idempotency [P1]

```sql
CREATE TABLE booking_request (
    tenant_id bigint NOT NULL,
    request_key text NOT NULL,
    request_hash text NOT NULL,
    booking_id bigint,
    PRIMARY KEY (tenant_id, request_key)
);
```

```sql
INSERT INTO booking_request (tenant_id, request_key, request_hash)
VALUES (:tenant, :key, :hash)
ON CONFLICT (tenant_id, request_key) DO NOTHING;
```

Within one transaction: claim key using atomic insert; winner reserves inventory, creates booking and records result; duplicate reads existing record and returns same result if request hash matches, otherwise 409. With PostgreSQL READ COMMITTED, use a separate SELECT after conflicting INSERT; handle concurrent/rollback cases. Scope key to authorized caller and operation, set retention policy. External side effects require provider idempotency or reconciliation/outbox. Never use check-then-insert alone.

Recall: Handle client timeout after a successful booking commit.

## 18. Unit testing and Mockito [P0]

```java
@ExtendWith(MockitoExtension.class)
class HotelServiceTest {
    @Mock HotelRepository repository;
    @InjectMocks HotelService service;

    @Test
    void missingHotel() {
        when(repository.findByIdAndDeletedFalse(7L)).thenReturn(Optional.empty());
        assertThrows(HotelNotFoundException.class, () -> service.get(7L));
        verify(repository).findByIdAndDeletedFalse(7L);
    }
}
```

Use arrange-act-assert; test success, failure and boundary conditions. Mockito does not test real transactions, SQL or Spring proxies.

Recall: Test creation and not-found behavior without starting Spring.

## 19. MVC and integration testing [P0]

```java
@WebMvcTest(HotelController.class)
@Import(SecurityConfig.class)
class HotelControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean HotelService service;
    @MockitoBean JwtDecoder jwtDecoder;

    @Test
    void getHotel() throws Exception {
        when(service.get(7L)).thenReturn(new HotelDto(7L, "Hotel", 1L));
        mvc.perform(get("/hotels/{id}", 7).with(jwt()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(7));
    }
    @Test
    void invalidRequest() throws Exception {
        mvc.perform(post("/hotels").with(jwt())
            .contentType(MediaType.APPLICATION_JSON).content("{}"))
            .andExpect(status().isBadRequest());
    }
    @Test
    void unauthenticated() throws Exception {
        mvc.perform(get("/hotels/7")).andExpect(status().isUnauthorized());
    }
}
```

Add `spring-security-test`; `jwt()` is a static SecurityMockMvcRequestPostProcessors helper. Boot 3.0–3.3 uses `@MockBean` instead of `@MockitoBean`. Test 403, 404, 409 and successful creation. Mock JWT tests do not prove real signature verification.

Recall: Write happy-path, validation and authorization tests.

## 20. Testcontainers [P1]

```java
@SpringBootTest
@Testcontainers
class BookingIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16");

    @Autowired BookingService bookingService;
    // Seed inventory; run competing bookings in independent transactions;
    // assert one succeeds, the other conflicts, and inventory stays nonnegative.
}
```

Requires Boot 3.1+ testcontainers support, JUnit Jupiter and PostgreSQL Testcontainers modules, Docker and the database driver. Run real migrations. Use separate transactions/threads for concurrency tests; test commit-time failures outside automatically rolled-back test transactions.

Recall: Explain why H2 and Mockito cannot prove PostgreSQL locking behavior.

## 21. Logging, metrics and Actuator [P2]

```java
private static final Logger log = LoggerFactory.getLogger(HotelService.class);
// Inside a service method:
log.info("Hotel created: hotelId={}", hotelId);
registry.counter("booking.conflicts").increment();
```

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: never
```

Use structured logs, correlation/trace IDs, Micrometer metrics and distributed traces. Track request rate, error rate, p95/p99 latency, pool saturation and outbox lag. Avoid high-cardinality metric labels (user IDs, request IDs). Restrict management endpoints; never log passwords, JWTs or payment secrets. Filters handle servlet-level concerns; MVC interceptors handle handler-level concerns; security belongs in the security filter chain.

## 22. Production traps — interview flashcards [P0]

| Trap | Correct mental model |
|---|---|
| Transaction self-invocation | Standard proxy advice is bypassed |
| Checked exceptions | No default rollback |
| readOnly | Hint, not guaranteed write prohibition |
| save / flush / commit | Distinct operations; flush can fail, commit can still fail |
| N+1 / lazy loading | Fetch deliberately within a persistence boundary |
| Bulk JPQL | Bypasses managed state and automatic version checks |
| REQUIRES_NEW | Independent commit and extra connection demand |
| Async | No automatic caller transaction propagation |
| Cache | Define invalidation, TTL and stale-data tolerance |
| AFTER_COMMIT | Not durable messaging |
| Retry POST | Duplicate effects unless idempotent |
| Check then insert | Race; use database constraints/atomic operations |
| Remote calls in transactions | Long lock duration and pool exhaustion |
| Concurrent update | Version, conditional update or appropriate lock |
| Deep offset pagination | Expensive scans; consider keyset |
| JWT | Stateless does not mean authenticated or authorized |
| Connection pool | Bound concurrency; more connections are not always better |
| Schema migrations | Apply compatible expand/contract changes for rolling deployments |

## 23. Active recall — definition of mastery

- [ ] Bootstrap service with typed configuration and constructor injection.
- [ ] Build CRUD REST API with DTO validation, soft deletion and HTTP semantics.
- [ ] Map JPA entities, write JPQL, paginate and eliminate N+1.
- [ ] Demonstrate rollback and explain propagation, isolation and proxy limitations.
- [ ] Prevent concurrent double booking using a database-backed invariant.
- [ ] Secure endpoints and enforce ownership with JWT authentication.
- [ ] Implement idempotent booking creation with database uniqueness.
- [ ] Bound external call latency and handle transient/ambiguous failures.
- [ ] Implement outbox and idempotent consumer; explain crash windows.
- [ ] Write Mockito, MockMvc and PostgreSQL integration tests.
- [ ] Explain observability, cache invalidation and connection-pool trade-offs.
- [ ] Reproduce the P0 snippets from a blank editor without autocomplete or notes.

**Practice loop:** 20 minutes closed-book coding → 10 minutes compiling/testing → 5 minutes explaining trade-offs. Repeat missed patterns on days 1, 3, 7 and 14. Keep one runnable reference application; snippets are a recall aid, not proof of correctness.

## Official references for deeper verification

- Spring Boot: https://docs.spring.io/spring-boot/reference/
- Spring Framework: https://docs.spring.io/spring-framework/reference/
- Spring Data JPA: https://docs.spring.io/spring-data/jpa/reference/
- Spring Security: https://docs.spring.io/spring-security/reference/
- Testcontainers: https://java.testcontainers.org/
- Resilience4j: https://resilience4j.readme.io/

Use the documentation version matching your project dependencies.

## 24. Final interview internals supplement [P0/P1]

- Bean lifecycle: instantiate → inject dependencies → BeanPostProcessor before initialization → `@PostConstruct` / initialization callbacks → post-process (often proxy creation) → ready; `@PreDestroy` on managed singleton shutdown. Prototype destruction is not automatically managed. Constructor injection exposes dependency cycles. Singleton beans must handle concurrent access safely.
- `@Primary` resolves a preferred candidate; `@Qualifier` narrows selection. `@Profile` controls registration by environment. `@ConditionalOnMissingBean` supports overridable auto-configuration; Boot 3 auto-configuration candidates are registered via `AutoConfiguration.imports`.
- MVC flow: servlet filters → DispatcherServlet → HandlerMapping → HandlerAdapter → argument resolution/validation → controller → return value handling/message conversion → response; interceptors surround handler execution; exception resolvers map exceptions. AOP wraps method invocations, not arbitrary HTTP traffic.
- Spring AOP normally uses interface or subclass proxies; self-invocation bypasses proxy advice. Final methods cannot be overridden by subclass proxies. Advice ordering matters (especially retry versus transaction). AspectJ weaving has different semantics.
- Hibernate entity states: transient, managed, detached, removed. Managed entities are dirty-checked; flush synchronizes changes, commit establishes transaction outcome. Merge returns managed copy; input remains detached. Owning side controls relationship updates; synchronize both sides in memory. Cascade propagates operations, orphanRemoval removes disassociated owned children; avoid indiscriminate REMOVE on many-to-many.
- Security: FilterChainProxy selects matching SecurityFilterChain; authentication establishes identity, authorization evaluates access. Enable method security and check ownership, not only roles. JWT signature, expiry, issuer and appropriate audience checks matter. 401 is unauthenticated; 403 is forbidden for typical authenticated access failures.
- Flyway: versioned immutable migration scripts, review locking/backfill costs, expand-contract for rolling deployments. Do not rely on automatic schema updates in production.
- Graceful shutdown: stop accepting traffic, drain in-flight work within configured deadline, account for readiness and orchestration termination grace period.

```java
@Configuration
@EnableMethodSecurity
class MethodSecurityConfig {}

@PreAuthorize("hasAuthority('SCOPE_booking.write')")
public void cancel(long id) { /* also enforce ownership */ }

@Bean
ThreadPoolTaskExecutor bookingExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(8);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("booking-");
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    return executor; // Spring manages lifecycle
}
```

Kafka syntax and delivery semantics: see [kafka-messaging.md](kafka-messaging.md). Executor guarantees: see [java-concurrency.md](java-concurrency.md).

Recall: draw bean lifecycle and MVC request flow; explain proxy limitations, owning side, security filters, migration rollout and bounded executor behavior. Freeze this file after implementing and testing the patterns.
