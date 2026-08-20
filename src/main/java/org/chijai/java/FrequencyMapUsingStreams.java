package org.chijai.java;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FrequencyMapUsingStreams {

    public static <T> Map<T, Long> frequencyMap(List<T> values) {

        return values.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
    }

    public static void main(String[] args) {

        List<String> values = List.of(
                "A",
                "B",
                "A",
                "C",
                "B",
                "A",
                "D",
                "C",
                "A"
        );

        Map<String, Long> frequencies = frequencyMap(values);

        System.out.println("Frequency Map:");

        frequencies.forEach((value, count) ->
                System.out.println(value + " -> " + count)
        );
    }
}

/*
============================================================
DSA-141 — FREQUENCY MAP USING STREAMS
============================================================

PROBLEM:

Convert a sequence into:

value -> frequency


INPUT:

[A, B, A, C, B, A]


OUTPUT:

A -> 3
B -> 2
C -> 1


============================================================
CORE SOLUTION
============================================================

Map<String, Long> frequencies =
        values.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));


============================================================
MUG-UP LINE
============================================================

stream().collect(
    groupingBy(
        Function.identity(),
        counting()
    )
);


============================================================
MENTAL MODEL
============================================================

Values:

A B A C B A

        |
        v

GROUP BY VALUE

        |
        v

A -> [A, A, A]
B -> [B, B]
C -> [C]

        |
        v

COUNT EACH GROUP

        |
        v

A -> 3
B -> 2
C -> 1


============================================================
PATTERN
============================================================

FREQUENCY MAP

=

GROUP BY SELF
+
COUNT


============================================================
FUNCTION.IDENTITY()
============================================================

Function.identity()

means:

x -> x


Therefore:

Collectors.groupingBy(
        Function.identity(),
        Collectors.counting()
)


is equivalent to:

Collectors.groupingBy(
        x -> x,
        Collectors.counting()
)


============================================================
WITHOUT STREAMS
============================================================

Map<String, Integer> frequency = new HashMap<>();

for (String value : values) {

    frequency.put(
            value,
            frequency.getOrDefault(value, 0) + 1
    );
}


============================================================
BETTER NON-STREAM COLLECTION IDIOM
============================================================

Map<String, Integer> frequency = new HashMap<>();

for (String value : values) {
    frequency.merge(value, 1, Integer::sum);
}


Mental model:

merge(
    key,
    initialValue,
    howToCombine
);


============================================================
IMPORTANT TYPE DETAIL
============================================================

Collectors.counting()

returns:

Long


Therefore:

Map<T, Long>


NOT:

Map<T, Integer>


This is a common small interview mistake.


============================================================
ALTERNATIVE STREAM SOLUTION — toMap()
============================================================

Map<String, Integer> frequency =
        values.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        value -> 1,
                        Integer::sum
                ));


Mental model:

value becomes key

first occurrence -> 1

duplicate key -> add counts


============================================================
WHICH STREAM VERSION TO REMEMBER?
============================================================

Most readable:

groupingBy + counting


frequency =

GROUP SAME VALUES
+
COUNT THEM


Use:

Collectors.groupingBy(
    Function.identity(),
    Collectors.counting()
)


============================================================
COMPLEXITY
============================================================

n = number of elements
k = number of distinct elements


TIME:

O(n) average


SPACE:

O(k)


============================================================
COMMON INTERVIEW VARIANTS
============================================================

1. CHARACTER FREQUENCY
------------------------------------------------------------

String text = "banana";

Map<Character, Long> frequency =
        text.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));


Result:

b -> 1
a -> 3
n -> 2


------------------------------------------------------------
2. WORD FREQUENCY
------------------------------------------------------------

List<String> words =
        List.of("java", "sql", "java", "spring", "java");

Map<String, Long> frequency =
        words.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));


------------------------------------------------------------
3. EMPLOYEES PER DEPARTMENT
------------------------------------------------------------

Map<String, Long> count =
        employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.counting()
                ));


Notice:

Frequency of VALUES:

Function.identity()


Frequency of PROPERTY:

Object::getProperty


============================================================
INTERVIEW EXTENSION — MOST FREQUENT VALUE
============================================================

Map<String, Long> frequency =
        frequencyMap(values);

Map.Entry<String, Long> mostFrequent =
        frequency.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();


Pattern:

frequency map
    ->
entrySet
    ->
max by value


============================================================
GENERALIZED PATTERN
============================================================

Question contains:

"How many times does each X occur?"

Think:

Map<X, Long>

        ↓

groupingBy(
    X,
    counting()
)


Examples:

symbol -> execution count

account -> transaction count

IP -> request count

error code -> occurrence count

department -> employee count

word -> frequency

character -> frequency


============================================================
TRADING EXAMPLE
============================================================

Executions:

AAPL
MSFT
AAPL
GOOG
AAPL
MSFT


Frequency:

AAPL -> 3
MSFT -> 2
GOOG -> 1


Code:

executions.stream()
        .collect(Collectors.groupingBy(
                Execution::getSymbol,
                Collectors.counting()
        ));


============================================================
RETRIEVAL TRIGGER
============================================================

"FREQUENCY / COUNT EACH DISTINCT VALUE"

        ↓

GROUP BY SELF
+
COUNT


============================================================
ONE-LINE MEMORY HOOK
============================================================

FREQUENCY MAP
=
groupingBy(identity(), counting())


============================================================
FASTEST RECALL
============================================================

[A, B, A, C, A]

        ↓

Map<Value, Count>

        ↓

stream()
    .collect(
        groupingBy(
            identity(),
            counting()
        )
    );


============================================================
*/
