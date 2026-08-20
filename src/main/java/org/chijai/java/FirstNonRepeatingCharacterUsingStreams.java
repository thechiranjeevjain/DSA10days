package org.chijai.java;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FirstNonRepeatingCharacterUsingStreams {

    public static Optional<Character> firstNonRepeatingCharacter(String input) {

        if (input == null || input.isEmpty()) {
            return Optional.empty();
        }

        Map<Character, Long> frequency =
                input.chars()
                        .mapToObj(c -> (char) c)
                        .collect(Collectors.groupingBy(
                                Function.identity(),
                                LinkedHashMap::new,
                                Collectors.counting()
                        ));

        return frequency.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public static void main(String[] args) {

        String input = "swiss";

        Optional<Character> result =
                firstNonRepeatingCharacter(input);

        System.out.println("Input: " + input);

        System.out.println(
                "First non-repeating character: "
                        + result.map(String::valueOf)
                        .orElse("NONE")
        );
    }
}

/*
============================================================
DSA-146 — FIRST NON-REPEATING CHARACTER USING STREAMS
============================================================

PROBLEM:

Given a string, return the FIRST character
whose frequency is exactly 1.


Example:

"swiss"

Characters:

s -> 3
w -> 1
i -> 1

First non-repeating:

w


============================================================
CORE IDEA
============================================================

Need TWO things:

1. FREQUENCY
2. ORIGINAL ORDER


Therefore use:

LinkedHashMap<Character, Long>


Why LinkedHashMap?

Because it preserves insertion order.


============================================================
CORE SOLUTION
============================================================

Map<Character, Long> frequency =
    input.chars()
         .mapToObj(c -> (char) c)
         .collect(Collectors.groupingBy(
             Function.identity(),
             LinkedHashMap::new,
             Collectors.counting()
         ));


Then:

frequency.entrySet()
         .stream()
         .filter(e -> e.getValue() == 1)
         .map(Map.Entry::getKey)
         .findFirst();


============================================================
MUG-UP VERSION
============================================================

chars
    -> groupingBy(identity, LinkedHashMap, counting)
    -> count == 1
    -> findFirst


============================================================
MENTAL MODEL
============================================================

String:

s w i s s

        |
        v

COUNT while preserving first appearance

        |
        v

s -> 3
w -> 1
i -> 1

        |
        v

first count == 1

        |
        v

w


============================================================
WHY NOT Collections.frequency()?
============================================================

A tempting solution:

input.chars()
     .mapToObj(c -> (char) c)
     .filter(c ->
         Collections.frequency(chars, c) == 1
     )
     .findFirst();


This is unnecessarily expensive.


Why?

For every character:

scan whole collection again.


n characters

x

O(n) frequency scan


TIME:

O(n²)


Avoid.


============================================================
GOOD COMPLEXITY
============================================================

First pass:

build frequency map

O(n)


Second pass:

scan map entries

O(k)


where:

k <= n


Overall:

O(n)


Space:

O(k)


============================================================
WHY LinkedHashMap MATTERS
============================================================

Suppose:

"swiss"


A normal HashMap could contain entries in
arbitrary iteration order.


Then:

findFirst()

might not correspond to the first character
in the original string.


LinkedHashMap preserves:

first insertion order


So:

s
w
i


remains in that order.


============================================================
ALTERNATIVE TWO-PASS SOLUTION
============================================================

Another clean approach:

1. build normal HashMap frequency
2. scan original string again


Example:

Map<Character, Long> frequency =
    input.chars()
         .mapToObj(c -> (char) c)
         .collect(Collectors.groupingBy(
             Function.identity(),
             Collectors.counting()
         ));


return input.chars()
        .mapToObj(c -> (char) c)
        .filter(c -> frequency.get(c) == 1)
        .findFirst();


This is also:

O(n)


And it does NOT require LinkedHashMap because
the second pass uses the original string order.


============================================================
WHICH VERSION TO PREFER?
============================================================

Both are good.


VERSION A:

LinkedHashMap
+
frequency
+
entrySet().findFirst()


VERSION B:

HashMap frequency
+
rescan original string


For interview simplicity:

LinkedHashMap version is concise and expressive.


============================================================
IMPORTANT CHARACTER DETAIL
============================================================

input.chars()

returns:

IntStream


not:

Stream<Character>


Therefore convert:

.mapToObj(c -> (char) c)


============================================================
UNICODE CATCH
============================================================

Java char represents a UTF-16 code unit.

For ordinary ASCII / common interview strings:

(char) c

is fine.


For full Unicode correctness with supplementary
characters, use:

input.codePoints()


Then work with:

Integer code points


because some Unicode characters require
surrogate pairs.


============================================================
UNICODE-SAFE IDEA
============================================================

Map<Integer, Long> frequency =
    input.codePoints()
         .boxed()
         .collect(Collectors.groupingBy(
             Function.identity(),
             LinkedHashMap::new,
             Collectors.counting()
         ));


Then find the first code point whose count == 1.


For standard DSA interviews:

char solution is usually expected.


============================================================
EDGE CASES
============================================================

INPUT:

""


Result:

Optional.empty()


------------------------------------------------------------

INPUT:

"a"


Result:

a


------------------------------------------------------------

INPUT:

"aabbcc"


Result:

Optional.empty()


------------------------------------------------------------

INPUT:

"leetcode"


Counts:

l -> 1
e -> 3
t -> 1
c -> 1
o -> 1
d -> 1


Result:

l


============================================================
COMMON WRONG SOLUTION
============================================================

input.chars()
     .distinct()
     .findFirst();


WRONG.


distinct() only removes repeated occurrences.

It does NOT tell whether the value appeared once.


Example:

"aabc"


distinct():

a b c


First distinct:

a


But:

a appeared TWICE.


Correct answer:

b


============================================================
GENERAL PATTERN
============================================================

FIRST ELEMENT SATISFYING FREQUENCY CONDITION

        ↓

BUILD FREQUENCY MAP

        ↓

PRESERVE / REVISIT ORIGINAL ORDER

        ↓

findFirst()


============================================================
RELATED PROBLEMS
============================================================

FIRST REPEATING CHARACTER:

count > 1
+
original order


FIRST UNIQUE WORD:

word frequency
+
find first count == 1


FIRST UNIQUE EVENT TYPE:

eventType frequency
+
find first frequency == 1


============================================================
INTERVIEW GOLDEN ANSWER
============================================================

"I'd avoid repeatedly scanning the string for each
character because that becomes O(n²).

Instead I'd build a frequency map in O(n), preserve
encounter order with a LinkedHashMap, then return the
first entry whose count is one. Overall complexity is
O(n) time and O(k) space."


============================================================
FASTEST RECALL
============================================================

FIRST NON-REPEATING

        ↓

COUNT

        +

PRESERVE ORDER

        ↓

LinkedHashMap

        ↓

count == 1

        ↓

findFirst()


============================================================
ONE-LINE MEMORY HOOK
============================================================

FIRST UNIQUE
=
ORDERED FREQUENCY MAP
+
COUNT == 1


============================================================
RETRIEVAL TRIGGER
============================================================

"First non-repeating character"

Think immediately:

frequency map
+
original order
+
first count == 1


============================================================
*/