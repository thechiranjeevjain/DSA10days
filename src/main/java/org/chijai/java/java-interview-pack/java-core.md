# Core Java — active recall

**Baseline:** Java 17+ (Java 21 features marked), Spring Boot 3.x, PostgreSQL where applicable. P0 = closed-book recall; P1 = explain and implement with documentation. Snippets are focused fragments, not standalone projects.

## P0 — Collections and contracts
| Type | Use | Cost / caveat |
|---|---|---|
| ArrayList | indexed sequence | get O(1), append amortized O(1), middle removal O(n) |
| HashMap / HashSet | key lookup / membership | expected O(1), no iteration order |
| LinkedHashMap | insertion/access order | expected O(1), extra links |
| TreeMap / TreeSet | sorted keys | O(log n), comparator defines key equivalence |
| ArrayDeque | stack / queue | amortized O(1) at ends; no null |
| PriorityQueue | min heap | peek O(1), offer/poll O(log n), iteration not sorted |

```java
Map<String, Long> counts = new HashMap<>();
counts.merge("hotel", 1L, Long::sum);
Map<Long, List<Booking>> byHotel = new HashMap<>();
byHotel.computeIfAbsent(id, k -> new ArrayList<>()).add(booking);
Deque<Integer> stack = new ArrayDeque<>();
stack.push(1); int top = stack.pop();
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
```
HashMap uses hash then equality; collisions do not imply equality. Equal objects must have equal hash codes. Never mutate fields participating in a key's equality while stored. HashMap is not thread safe. Modern implementations can treeify heavily collided bins under certain conditions.

## P0 — Equality, records and immutability
```java
record HotelKey(long hotelId, LocalDate date) {}
record BookingRequest(List<Long> roomIds) {
    BookingRequest { roomIds = List.copyOf(roomIds); }
}
```
Records provide component-based equality and final references, not deep immutability. `==` compares reference identity for objects; `equals` compares logical equality when overridden. `List.copyOf` rejects nulls; an unmodifiable view can reflect mutations of its backing list.

## P0 — Generics and variance
```java
static <T> void copy(List<? extends T> src, List<? super T> dst) {
    for (T value : src) dst.add(value);
}
```
PECS: producer extends, consumer super. Generic types are invariant; type erasure prevents `new T()` and `instanceof List<String>`. Arrays are covariant and checked at runtime.

## P0 — Streams and Optional
```java
Map<Long, Long> countByHotel = bookings.stream()
    .filter(Booking::confirmed)
    .collect(Collectors.groupingBy(Booking::hotelId, Collectors.counting()));
List<Booking> sorted = bookings.stream()
    .sorted(Comparator.comparing(Booking::createdAt)
        .thenComparingLong(Booking::id)).toList();
Hotel hotel = repository.findById(id).orElseThrow(() -> new NoSuchElementException());
```
Intermediate operations are lazy; terminal operations consume streams. Avoid side effects. `orElse` evaluates eagerly; `orElseGet` is lazy. `toList()` is unmodifiable. `map` transforms; `flatMap` flattens. Parallel streams are not automatically faster; avoid blocking work in the common pool.

## P0 — Exceptions and resources
```java
try (var reader = Files.newBufferedReader(path)) {
    return reader.readLine();
} catch (IOException e) {
    throw new UncheckedIOException(e);
}
```
Checked exceptions must be caught or declared; unchecked exceptions need not be. Try-with-resources closes in reverse order and preserves suppressed exceptions. Never swallow failures or return from finally.

## P0 — Language and numeric traps
- Overloading is compile-time selection; overriding is runtime dispatch. Static methods are hidden, not overridden.
- Java passes everything by value, including reference values. `final` references do not make objects immutable.
- String is immutable; use StringBuilder for repeated concatenation in loops. Interning and boxed integer caches make identity comparisons unreliable.
- Use `BigDecimal` constructed from string for decimal money; specify scale and rounding; `compareTo` ignores scale while `equals` does not. Use integer minor units where appropriate.
- Use `Instant` for timestamps; `LocalDate` for business dates; specify zone for conversions.
- Java 17 sealed classes restrict permitted subclasses; Java 21 pattern matching switch can express exhaustive variant handling.

## Closed-book drills
- [ ] Group bookings by hotel, count statuses, sort by date and ID.
- [ ] Implement immutable money value object and explain equality.
- [ ] Explain HashMap collisions, resizing, and mutable-key failure.
- [ ] Implement generic copy and explain variance.
- [ ] Compare composition and inheritance using payment providers.

Reference: https://dev.java/learn/
