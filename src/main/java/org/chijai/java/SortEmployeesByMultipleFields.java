package org.chijai.java;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortEmployeesByMultipleFields {

    static class Employee {
        private final int id;
        private final String name;
        private final String department;
        private final long salary;

        Employee(int id, String name, String department, long salary) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.salary = salary;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDepartment() {
            return department;
        }

        public long getSalary() {
            return salary;
        }

        @Override
        public String toString() {
            return "Employee{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", department='" + department + '\'' +
                    ", salary=" + salary +
                    '}';
        }
    }

    /*
     * Priority:
     *
     * 1. Salary DESCENDING
     * 2. Name ASCENDING
     * 3. ID ASCENDING
     *
     * ID provides a deterministic final tie-breaker.
     */
    public static List<Employee> sortEmployees(List<Employee> employees) {

        Comparator<Employee> comparator =
                Comparator.comparingLong(Employee::getSalary)
                        .reversed()
                        .thenComparing(
                                Employee::getName,
                                Comparator.nullsLast(
                                        String.CASE_INSENSITIVE_ORDER
                                )
                        )
                        .thenComparingInt(Employee::getId);

        return employees.stream()
                .sorted(comparator)
                .toList();
    }

    public static void main(String[] args) {

        List<Employee> employees = List.of(
                new Employee(5, "Charlie", "Engineering", 120_000),
                new Employee(2, "Bob", "Finance", 150_000),
                new Employee(4, "Alice", "Engineering", 120_000),
                new Employee(1, "David", "HR", 150_000),
                new Employee(3, "Alice", "Finance", 120_000),
                new Employee(6, null, "HR", 120_000)
        );

        System.out.println("Before sorting:");

        employees.forEach(System.out::println);

        List<Employee> sorted = sortEmployees(employees);

        System.out.println("\nAfter sorting:");
        System.out.println(
                "Priority = salary DESC, name ASC, id ASC\n"
        );

        sorted.forEach(System.out::println);

        /*
         * If interviewer asks for IN-PLACE sorting:
         */
        List<Employee> mutableEmployees =
                new ArrayList<>(employees);

        mutableEmployees.sort(
                Comparator.comparingLong(Employee::getSalary)
                        .reversed()
                        .thenComparing(
                                Employee::getName,
                                Comparator.nullsLast(
                                        String.CASE_INSENSITIVE_ORDER
                                )
                        )
                        .thenComparingInt(Employee::getId)
        );

        System.out.println("\nIn-place sorted copy:");

        mutableEmployees.forEach(System.out::println);
    }
}

/*
============================================================
DSA-144 — SORT EMPLOYEES BY MULTIPLE FIELDS
============================================================

PROBLEM:

Sort employees using multiple levels of priority.

Example requirement:

1. Salary DESC
2. Name ASC
3. ID ASC


============================================================
CORE SOLUTION
============================================================

employees.stream()
    .sorted(
        Comparator.comparingLong(Employee::getSalary)
            .reversed()
            .thenComparing(Employee::getName)
            .thenComparingInt(Employee::getId)
    )
    .toList();


============================================================
MUG-UP PATTERN
============================================================

Comparator
    .comparingX(PRIMARY)
    .thenComparing(SECONDARY)
    .thenComparingX(TIE_BREAKER)


============================================================
MENTAL MODEL
============================================================

SORT BY:

salary DESC

        |
        | tie
        v

name ASC

        |
        | tie
        v

id ASC


This is LEXICOGRAPHIC ordering.


============================================================
EXAMPLE
============================================================

Employees:

ID   NAME      SALARY

5    Charlie   120
2    Bob       150
4    Alice     120
1    David     150
3    Alice     120


Rule:

salary DESC
name ASC
id ASC


Result:

Bob       150   ID 2
David     150   ID 1
Alice     120   ID 3
Alice     120   ID 4
Charlie   120   ID 5


Why?

First:

150 > 120


Within salary 150:

Bob < David alphabetically


Within salary 120:

Alice < Charlie


Two Alices:

ID 3 < ID 4


============================================================
THE IMPORTANT COMPARATOR METHODS
============================================================

OBJECT / COMPARABLE FIELD:

Comparator.comparing(...)


int:

Comparator.comparingInt(...)


long:

Comparator.comparingLong(...)


double:

Comparator.comparingDouble(...)


NEXT FIELD:

.thenComparing(...)


REVERSE ORDER:

.reversed()


============================================================
VERY IMPORTANT reversed() TRAP
============================================================

Suppose requirement:

salary DESC
name ASC


CORRECT:

Comparator
    .comparingLong(Employee::getSalary)
    .reversed()
    .thenComparing(Employee::getName);


Be careful with:

Comparator
    .comparingLong(Employee::getSalary)
    .thenComparing(Employee::getName)
    .reversed();


The final reversed() reverses the ENTIRE comparator.

That produces:

salary DESC
name DESC


not:

salary DESC
name ASC.


============================================================
MEMORY RULE
============================================================

REVERSE THE FIELD WHERE YOU NEED DESCENDING ORDER.

Do not casually put:

.reversed()

at the end of a multi-field comparator.


============================================================
DESCENDING SECONDARY FIELD
============================================================

Suppose:

salary DESC
name DESC


Use:

Comparator
    .comparingLong(Employee::getSalary)
    .reversed()
    .thenComparing(
        Employee::getName,
        Comparator.reverseOrder()
    );


============================================================
NULL SAFETY
============================================================

Suppose:

employee.name == null


Plain:

.thenComparing(Employee::getName)

can cause problems during comparison.


Use:

.thenComparing(
    Employee::getName,
    Comparator.nullsLast(
        Comparator.naturalOrder()
    )
)


Meaning:

non-null names first
null names last.


============================================================
CASE-INSENSITIVE NAME SORT
============================================================

Use:

.thenComparing(
    Employee::getName,
    String.CASE_INSENSITIVE_ORDER
)


For null safety:

.thenComparing(
    Employee::getName,
    Comparator.nullsLast(
        String.CASE_INSENSITIVE_ORDER
    )
)


============================================================
WHY ID AS FINAL TIE-BREAKER?
============================================================

Suppose:

Employee 100:
Alice, salary 120000

Employee 200:
Alice, salary 120000


Without ID:

comparator.compare(a, b)

returns:

0


even though these are two different employees.


Adding:

.thenComparingInt(Employee::getId)


creates deterministic ordering.


This is especially useful with:

TreeSet
TreeMap
PriorityQueue
distributed deterministic processing
repeatable tests


============================================================
IMPORTANT TREESET CATCH
============================================================

TreeSet uses comparator equality to decide whether
elements are duplicates from the set's perspective.


Suppose comparator only uses:

salary
name


Two different employees have:

same salary
same name


Comparator returns:

0


TreeSet may retain only ONE.


Therefore a unique final tie-breaker such as:

employee ID

can be essential.


============================================================
STREAM SORT VS LIST SORT
============================================================

STREAM:

List<Employee> sorted =
    employees.stream()
        .sorted(comparator)
        .toList();


Original list:

UNCHANGED


------------------------------------------------------------

IN PLACE:

employees.sort(comparator);


Original list:

MODIFIED


============================================================
JAVA VERSION NOTE
============================================================

Stream.toList()

is available from Java 16.


If interviewer requires Java 8:

employees.stream()
    .sorted(comparator)
    .collect(Collectors.toList());


============================================================
COMPLEXITY
============================================================

n = number of employees


SORTING:

O(n log n)


Comparator evaluation:

O(1)

assuming ordinary field comparisons.


STREAM RESULT SPACE:

O(n)


because a new result list is produced.


============================================================
COMMON BAD COMPARATOR
============================================================

DO NOT write:

(a, b) -> (int) (b.getSalary() - a.getSalary())


Why?


1. numeric overflow

2. narrowing conversion

3. precision problems

4. comparator contract violations


Similarly avoid:

(a, b) -> a.getId() - b.getId()


because integer subtraction can overflow.


============================================================
SAFE COMPARISON
============================================================

Use:

Integer.compare(a.getId(), b.getId())

Long.compare(a.getSalary(), b.getSalary())

Double.compare(a.getSalary(), b.getSalary())


Or preferably:

Comparator.comparingInt(...)
Comparator.comparingLong(...)
Comparator.comparingDouble(...)


============================================================
WHY "SAFELY" MATTERS
============================================================

A comparator must obey a consistent ordering contract.

Think:

compare(a, b) < 0

a comes before b


compare(a, b) == 0

equal according to comparator


compare(a, b) > 0

a comes after b


Broken comparators can cause incorrect behavior in:

sorting
TreeSet
TreeMap
PriorityQueue


============================================================
GENERALIZED PATTERN
============================================================

Whenever question says:

"Sort by X, then Y, then Z"

translate immediately to:

Comparator
    .comparing(X)
    .thenComparing(Y)
    .thenComparing(Z)


============================================================
TRADING EXAMPLE
============================================================

ORDER priority:

BUY:

price DESC
arrivalSequence ASC
orderId ASC


Comparator:

Comparator
    .comparingLong(Order::getPrice)
    .reversed()
    .thenComparingLong(Order::getArrivalSequence)
    .thenComparingLong(Order::getOrderId);


This is exactly the same pattern.


============================================================
PRICE-TIME PRIORITY CONNECTION
============================================================

Price-time priority is simply:

MULTI-FIELD SORTING.


BUY:

price DESC
time ASC


SELL:

price ASC
time ASC


So the comparator knowledge from this problem
directly maps to an order book.


============================================================
INTERVIEW GOLDEN ANSWER
============================================================

"I'd compose the comparator lexicographically using
Comparator.comparing and thenComparing.

For descending salary I'd reverse only the salary
comparator, then compare names ascending, and finally
use employee ID as a deterministic tie-breaker.

I'd also clarify null handling and avoid subtraction-
based comparators because of overflow and comparator
contract issues."


============================================================
FASTEST RECALL
============================================================

SORT BY:

A
then B
then C

        ↓

comparing(A)
.thenComparing(B)
.thenComparing(C)


DESCENDING PRIMARY:

comparing(A)
.reversed()
.thenComparing(B)


============================================================
ONE-LINE MEMORY HOOK
============================================================

MULTI-FIELD SORT
=
comparing(PRIMARY)
.thenComparing(SECONDARY)
.thenComparing(TIE_BREAKER)


============================================================
RETRIEVAL TRIGGER
============================================================

"Sort by X, then Y/id"

Think immediately:

COMPARATOR CHAIN

        ↓

comparingX()
    -> reversed() if needed
    -> thenComparing()
    -> unique tie-breaker


============================================================
*/
