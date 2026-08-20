package org.chijai.java;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FlattenNestedCollections {

    /*
     * Flatten:
     *
     * List<List<T>>
     *
     * into:
     *
     * List<T>
     *
     * while:
     * 1. ignoring null inner lists
     * 2. ignoring null elements
     */
    public static <T> List<T> flatten(List<List<T>> nested) {

        if (nested == null) {
            return List.of();
        }

        return nested.stream()
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /*
     * Example:
     *
     * Flatten integers
     * -> keep only even numbers
     * -> multiply each by 10
     */
    public static List<Integer> flattenFilterAndMap(
            List<List<Integer>> nested) {

        if (nested == null) {
            return List.of();
        }

        return nested.stream()
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .filter(number -> number % 2 == 0)
                .map(number -> number * 10)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {

        List<List<Integer>> nested = List.of(
                List.of(1, 2, 3),
                List.of(4, 5),
                List.of(),
                List.of(6, 7, 8),
                List.of(9, 10)
        );

        System.out.println("Nested:");
        System.out.println(nested);

        List<Integer> flattened = flatten(nested);

        System.out.println("\nFlattened:");
        System.out.println(flattened);

        List<Integer> transformed =
                flattenFilterAndMap(nested);

        System.out.println(
                "\nFlattened + even numbers + multiply by 10:"
        );
        System.out.println(transformed);
    }
}

/*
============================================================
DSA-145 — FLATTEN NESTED COLLECTIONS
============================================================

PROBLEM:

Transform:

List<List<T>>

into:

List<T>


Example:

[
    [1, 2, 3],
    [4, 5],
    [6, 7]
]

        ↓

[1, 2, 3, 4, 5, 6, 7]


============================================================
CORE SOLUTION
============================================================

nested.stream()
      .flatMap(List::stream)
      .collect(Collectors.toList());


============================================================
MUG-UP LINE
============================================================

listOfLists.stream()
           .flatMap(List::stream)
           .collect(toList());


============================================================
MENTAL MODEL
============================================================

Suppose:

List<List<Integer>>


Outer stream contains:

Stream<List<Integer>>


Conceptually:

[1,2,3]   [4,5]   [6,7]

     |
     | map(List::stream)
     v

Stream<Integer>
Stream<Integer>
Stream<Integer>


But we don't want:

Stream<Stream<Integer>>


We want ONE stream:

1 2 3 4 5 6 7


Therefore:

flatMap()


============================================================
MAP VS FLATMAP
============================================================

This is the key concept.


MAP:

ONE input
    ->
ONE output


flatMap:

ONE input
    ->
STREAM OF outputs

then FLATTEN all those streams.


============================================================
WITH map()
============================================================

nested.stream()
      .map(List::stream)


Conceptually produces:

Stream<Stream<T>>


Structure remains nested.


============================================================
WITH flatMap()
============================================================

nested.stream()
      .flatMap(List::stream)


Produces:

Stream<T>


The nesting disappears.


============================================================
VISUAL MEMORY
============================================================

MAP:

[[1,2], [3,4]]

        ↓

[Stream(1,2), Stream(3,4)]


FLATMAP:

[[1,2], [3,4]]

        ↓

[1,2,3,4]


============================================================
FILTER AFTER FLATTENING
============================================================

Suppose:

[[1,2,3], [4,5], [6]]


Need only EVEN values.


nested.stream()
      .flatMap(List::stream)
      .filter(x -> x % 2 == 0)
      .collect(toList());


Result:

[2,4,6]


Mental pipeline:

NESTED
    ->
FLATTEN
    ->
FILTER
    ->
COLLECT


============================================================
MAP AFTER FLATTENING
============================================================

Suppose:

multiply every value by 10.


nested.stream()
      .flatMap(List::stream)
      .map(x -> x * 10)
      .collect(toList());


Result:

[10,20,30,...]


============================================================
FILTER + MAP
============================================================

nested.stream()
      .flatMap(List::stream)
      .filter(x -> x % 2 == 0)
      .map(x -> x * 10)
      .collect(toList());


Pipeline:

FLATTEN
    ->
FILTER
    ->
TRANSFORM
    ->
COLLECT


============================================================
NULL SAFETY
============================================================

There are TWO possible null problems.


1. NULL INNER COLLECTION

Example conceptually:

[
    [1,2],
    null,
    [3,4]
]


Then:

.flatMap(List::stream)

would fail when it reaches null.


Protect using:

.filter(Objects::nonNull)
.flatMap(List::stream)


------------------------------------------------------------

2. NULL ELEMENT INSIDE INNER LIST

Example:

[
    [1, null, 2],
    [3, 4]
]


After flattening:

.filter(Objects::nonNull)


Therefore robust pipeline:

nested.stream()
      .filter(Objects::nonNull)
      .flatMap(List::stream)
      .filter(Objects::nonNull)


============================================================
IMPORTANT ORDER
============================================================

For null INNER LISTS:

.filter(Objects::nonNull)

must happen BEFORE:

.flatMap(List::stream)


Otherwise List::stream would be called on null.


============================================================
JAVA 8 VS MODERN JAVA
============================================================

Java 8:

.collect(Collectors.toList())


Java 16+:

.toList()


Example:

List<Integer> result =
        nested.stream()
              .flatMap(List::stream)
              .toList();


Remember:

Stream.toList() generally returns an
unmodifiable result.

Collectors.toList() makes fewer mutability guarantees
by specification, though commonly produces an ArrayList.


============================================================
COMPLEXITY
============================================================

Let:

N = total number of elements across ALL inner lists.


Example:

[
  3 elements,
  5 elements,
  10 elements
]

N = 18


TIME:

O(N)


Every flattened element is visited once.


SPACE:

O(N)

if collecting into a new list.


If you simply process the stream without collecting:

additional storage can be much smaller.


============================================================
COMMON INTERVIEW VARIANT
============================================================

EMPLOYEE DEPARTMENTS:

List<Department>

Each department contains:

List<Employee>


Need all employees:


departments.stream()
           .flatMap(
               department ->
                   department.getEmployees().stream()
           )
           .collect(toList());


============================================================
OBJECT EXAMPLE
============================================================

class Department {

    List<Employee> employees;

}


Then:

List<Employee> allEmployees =
    departments.stream()
        .flatMap(
            department ->
                department.getEmployees().stream()
        )
        .collect(Collectors.toList());


============================================================
FLATTEN + FILTER OBJECTS
============================================================

Get all employees earning > 100k:


departments.stream()
    .flatMap(
        department ->
            department.getEmployees().stream()
    )
    .filter(
        employee ->
            employee.getSalary() > 100_000
    )
    .collect(toList());


============================================================
FLATTEN + MAP OBJECT PROPERTY
============================================================

Get all employee names:


departments.stream()
    .flatMap(
        department ->
            department.getEmployees().stream()
    )
    .map(Employee::getName)
    .collect(toList());


============================================================
FLATTEN + FILTER + MAP
============================================================

Get names of high-paid employees:


departments.stream()
    .flatMap(
        department ->
            department.getEmployees().stream()
    )
    .filter(
        employee ->
            employee.getSalary() > 100_000
    )
    .map(Employee::getName)
    .collect(toList());


This pipeline appears frequently in
Java Stream interviews.


============================================================
STRING EXAMPLE
============================================================

Suppose:

List<String> sentences =
    List.of(
        "java streams",
        "low latency trading"
    );


Need individual words:


sentences.stream()
    .flatMap(
        sentence ->
            List.of(sentence.split(" ")).stream()
    )
    .collect(toList());


Better:

sentences.stream()
    .flatMap(
        sentence ->
            java.util.Arrays.stream(
                sentence.split(" ")
            )
    )
    .collect(toList());


Result:

[
 java,
 streams,
 low,
 latency,
 trading
]


============================================================
OPTIONAL CONNECTION
============================================================

flatMap also appears with Optional.


Conceptual problem:

Optional<Optional<T>>


flatMap removes one level of nesting:

Optional<T>


Same mental idea:

NESTED CONTAINER

        ↓

flatMap

        ↓

FLAT CONTAINER


============================================================
GENERALIZED PATTERN
============================================================

Whenever you see:

List<List<T>>

Stream<Collection<T>>

List<Department<List<Employee>>>

List<Order<List<Execution>>>

List<Account<List<Transaction>>>


and need:

ONE stream of inner elements


think:

flatMap()


============================================================
TRADING / BACKEND EXAMPLES
============================================================

ORDERS -> EXECUTIONS

List<Order>

Each Order:

List<Execution>


Need all executions:

orders.stream()
      .flatMap(
          order ->
              order.getExecutions().stream()
      )


------------------------------------------------------------

ACCOUNTS -> POSITIONS

accounts.stream()
        .flatMap(
            account ->
                account.getPositions().stream()
        )


------------------------------------------------------------

BATCHES -> MESSAGES

batches.stream()
       .flatMap(
           batch ->
               batch.getMessages().stream()
       )


============================================================
STREAM PIPELINE MENTAL MODEL
============================================================

SOURCE

nested.stream()

        |
        v

FLATTEN

flatMap(...)

        |
        v

FILTER

filter(...)

        |
        v

TRANSFORM

map(...)

        |
        v

TERMINAL OPERATION

collect(...)


============================================================
COMMON WRONG ANSWER
============================================================

nested.stream()
      .map(List::stream)


Why wrong?


Because result type becomes:

Stream<Stream<T>>


The data remains nested.


If interviewer says:

"flatten"

your brain should immediately switch:

map

        ↓

flatMap


============================================================
INTERVIEW GOLDEN ANSWER
============================================================

"I'd use flatMap because each element of the outer
stream is itself a collection. List::stream converts
each inner list into a stream, and flatMap merges those
streams into one Stream<T>. I can then apply ordinary
filter and map operations before collecting the result."


============================================================
FASTEST RECALL
============================================================

List<List<T>>

        ↓

stream()

        ↓

flatMap(List::stream)

        ↓

Stream<T>


============================================================
MUG-UP TEMPLATE
============================================================

nested.stream()
      .flatMap(Collection::stream)
      .filter(...)
      .map(...)
      .collect(toList());


============================================================
ONE-LINE MEMORY HOOK
============================================================

NESTED COLLECTION
=
flatMap(Collection::stream)


============================================================
RETRIEVAL TRIGGER
============================================================

"Flatten nested lists / collections"

Think immediately:

MAP creates nesting.

FLATMAP removes nesting.


List<List<T>>

        ↓

flatMap(List::stream)

        ↓

Stream<T>


============================================================
*/
