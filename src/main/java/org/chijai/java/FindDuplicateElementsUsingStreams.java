package org.chijai.java;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FindDuplicateElementsUsingStreams {

    /*
     * Returns each duplicated value ONCE.
     *
     * Ordering requirement:
     * preserve the order of FIRST APPEARANCE in the input.
     *
     * Example:
     *
     * [B, A, C, B, A, B, D]
     *
     * -> [B, A]
     */
    public static <T> List<T> findDuplicates(List<T> values) {

        Map<T, Long> frequency =
                values.stream()
                        .collect(Collectors.groupingBy(
                                Function.identity(),
                                LinkedHashMap::new,
                                Collectors.counting()
                        ));

        return frequency.entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /*
     * Alternative:
     *
     * Return duplicates in the order in which they are
     * FIRST DETECTED as duplicates.
     *
     * Example:
     *
     * Input:
     * [A, B, B, A, C]
     *
     * First-appearance ordering:
     * [A, B]
     *
     * First-duplicate-detection ordering:
     * [B, A]
     */
    public static <T> List<T> findDuplicatesByDetectionOrder(
            List<T> values) {

        Set<T> seen = new HashSet<>();

        return values.stream()
                .filter(value -> !seen.add(value))
                .distinct()
                .collect(Collectors.toList());
    }

    /*
     * If ordering does NOT matter.
     */
    public static <T> Set<T> findDuplicatesUnordered(
            List<T> values) {

        Set<T> seen = new HashSet<>();

        return values.stream()
                .filter(value -> !seen.add(value))
                .collect(Collectors.toSet());
    }

    public static void main(String[] args) {

        List<String> values = List.of(
                "A",
                "B",
                "B",
                "A",
                "C",
                "D",
                "B",
                "E",
                "D"
        );

        System.out.println("Input:");
        System.out.println(values);

        System.out.println("\nDuplicates — first appearance order:");
        System.out.println(findDuplicates(values));

        System.out.println("\nDuplicates — first duplicate detection order:");
        System.out.println(findDuplicatesByDetectionOrder(values));

        System.out.println("\nDuplicates — ordering irrelevant:");
        System.out.println(findDuplicatesUnordered(values));
    }
}

/*
============================================================
DSA-142 — FIND DUPLICATE ELEMENTS USING STREAMS
============================================================

PROBLEM:

Given:

[A, B, B, A, C, D, B]

Return duplicate VALUES once.


Possible answer:

[A, B]

or:

[B, A]

depending on the required ordering.


============================================================
FIRST QUESTION TO ASK
============================================================

"What ordering should the duplicates have?"


This matters.

There are at least THREE interpretations:


1. ORDER DOES NOT MATTER

2. PRESERVE FIRST APPEARANCE ORDER

3. PRESERVE FIRST DUPLICATE-DETECTION ORDER


============================================================
EXAMPLE THAT EXPOSES THE DIFFERENCE
============================================================

Input:

[A, B, B, A, C]


A first appears before B.

But B becomes a duplicate before A.


FIRST APPEARANCE ORDER:

[A, B]


FIRST DUPLICATE-DETECTION ORDER:

[B, A]


This is why the ordering requirement matters.


============================================================
SOLUTION 1 — PRESERVE FIRST APPEARANCE ORDER
============================================================

Use:

LinkedHashMap
+
frequency counting


Map<T, Long> frequency =
    values.stream()
        .collect(groupingBy(
            identity(),
            LinkedHashMap::new,
            counting()
        ));


Then:

frequency.entrySet()
    .stream()
    .filter(e -> e.getValue() > 1)
    .map(Map.Entry::getKey)
    .collect(toList());


WHY LinkedHashMap?

Because it preserves insertion order.

The first occurrence determines when a key enters
the map.


============================================================
CORE MUG-UP
============================================================

stream()
    -> groupingBy(identity(), LinkedHashMap, counting())
    -> count > 1
    -> key


============================================================
SOLUTION 2 — DUPLICATE DETECTION ORDER
============================================================

Set<T> seen = new HashSet<>();

values.stream()
      .filter(x -> !seen.add(x))
      .distinct()
      .collect(toList());


KEY TRICK:

seen.add(x)


returns:

true
    -> x was new

false
    -> x already existed


Therefore:

!seen.add(x)

means:

"x is a duplicate."


============================================================
WHY distinct()?
============================================================

Input:

[A, A, A, A]


Without distinct():

[A, A, A]


Every occurrence after the first is detected as
a duplicate.


But usually the question asks:

"return duplicate elements"


meaning each duplicated VALUE once.


So:

.filter(x -> !seen.add(x))
.distinct()


returns:

[A]


============================================================
SOLUTION 3 — ORDER DOESN'T MATTER
============================================================

Set<T> seen = new HashSet<>();

Set<T> duplicates =
    values.stream()
          .filter(x -> !seen.add(x))
          .collect(toSet());


============================================================
COMPLEXITY
============================================================

FREQUENCY-MAP VERSION:

Time:
O(n) average

Space:
O(k)


SEEN-SET VERSION:

Time:
O(n) average

Space:
O(k)


where:

k = number of distinct values.


============================================================
IMPORTANT INTERVIEW ISSUE
============================================================

This:

Set<T> seen = new HashSet<>();

stream.filter(x -> !seen.add(x))


has SIDE EFFECTS inside the stream.


It works predictably for a:

SEQUENTIAL STREAM


But should NOT be used with:

parallelStream()


because the logic depends on shared mutable state
and encounter order.


So don't blindly do:

values.parallelStream()
      .filter(x -> !seen.add(x))


for an ordering-sensitive solution.


============================================================
SAFER DECLARATIVE STREAM APPROACH
============================================================

For interview-quality code where ordering matters:

frequency map
+
LinkedHashMap


is generally easier to reason about.


============================================================
COMMON WRONG ANSWER
============================================================

values.stream()
      .distinct()


WRONG.


distinct() removes duplicates.

It does NOT identify which values were duplicated.


Input:

[A, B, B, C]


distinct():

[A, B, C]


Wanted duplicates:

[B]


============================================================
ANOTHER WRONG IDEA
============================================================

filter(x -> Collections.frequency(values, x) > 1)


This can work logically:

values.stream()
      .filter(x -> Collections.frequency(values, x) > 1)
      .distinct()


But:

Collections.frequency()

scans the collection each time.


n elements
x
O(n) scan


TIME:

O(n²)


Avoid when a frequency map gives:

O(n).


============================================================
GENERAL PATTERN
============================================================

"Find duplicates"

Think:

OPTION A:

FREQUENCY MAP

value -> count

then:

count > 1


OPTION B:

SEEN SET

if add() == false

=> duplicate


============================================================
WHEN TO CHOOSE WHICH?
============================================================

Need counts too?

        YES
         |
         v
   FREQUENCY MAP


Need first-appearance ordering?

        YES
         |
         v
   LinkedHashMap
   + counting


Need simple duplicate detection?

        YES
         |
         v
      seen Set


Need duplicate detection order?

        YES
         |
         v
 !seen.add(x)
 + distinct()


============================================================
RELATED QUESTIONS
============================================================

FREQUENCY:

groupingBy(identity(), counting())


DUPLICATES:

count > 1


UNIQUE VALUES:

count == 1


MOST FREQUENT:

max frequency


FIRST DUPLICATE:

first value where:

!seen.add(value)


============================================================
TRADING / BACKEND ANALOGY
============================================================

Execution IDs:

E1
E2
E3
E2
E4
E1


Duplicate IDs:

E1
E2


Same conceptual problem as:

duplicate executions

duplicate requests

duplicate transaction IDs

duplicate message IDs

duplicate idempotency keys


============================================================
INTERVIEW GOLDEN ANSWER
============================================================

"I'd first clarify what ordering is required.

If duplicates should retain the order of their first
appearance, I'd build a frequency map using a
LinkedHashMap and filter entries whose count is greater
than one.

If duplicate-detection order is desired, for a
sequential stream I can maintain a seen HashSet and use
the return value of Set.add(), followed by distinct()."


============================================================
MUG-UP VERSION
============================================================

DUPLICATES
=
FREQUENCY > 1


Preserve first appearance:

groupingBy(
    identity(),
    LinkedHashMap::new,
    counting()
)


Fast detection:

Set seen

!seen.add(x)

=> DUPLICATE


============================================================
ONE-LINE MEMORY HOOK
============================================================

DUPLICATE = COUNT > 1
         OR
             seen.add(x) == false


============================================================
RETRIEVAL TRIGGER
============================================================

"Find duplicate elements"

        ↓

ASK ORDERING

        ↓

FIRST APPEARANCE
    -> LinkedHashMap + frequency > 1

DETECTION ORDER
    -> !seen.add(x) + distinct()

NO ORDER
    -> HashSet


============================================================
*/