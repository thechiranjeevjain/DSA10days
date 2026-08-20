package org.chijai.java;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PartitionEmployeesByPredicate {

    static class Employee {
        private final int id;
        private final String name;
        private final String department;
        private final double salary;

        Employee(int id, String name, String department, double salary) {
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

        public double getSalary() {
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
     * Split employees into exactly TWO groups:
     *
     * true  -> predicate satisfied
     * false -> predicate not satisfied
     *
     * Here:
     * true  -> salary >= 100,000
     * false -> salary < 100,000
     */
    public static Map<Boolean, List<Employee>> partitionEmployees(
            List<Employee> employees) {

        return employees.stream()
                .collect(Collectors.partitioningBy(
                        employee -> employee.getSalary() >= 100_000
                ));
    }

    public static void main(String[] args) {

        List<Employee> employees = List.of(
                new Employee(1, "Alice", "Engineering", 120_000),
                new Employee(2, "Bob", "Engineering", 90_000),
                new Employee(3, "Charlie", "Finance", 110_000),
                new Employee(4, "David", "Finance", 80_000),
                new Employee(5, "Eva", "HR", 105_000),
                new Employee(6, "Frank", "HR", 70_000)
        );

        Map<Boolean, List<Employee>> result =
                partitionEmployees(employees);

        System.out.println("Salary >= 100,000:");
        result.get(true).forEach(System.out::println);

        System.out.println("\nSalary < 100,000:");
        result.get(false).forEach(System.out::println);
    }
}

/*
============================================================
DSA-143 — PARTITION EMPLOYEES BY PREDICATE
============================================================

PROBLEM:

Split employees into TWO groups according to
a boolean condition.


Example condition:

salary >= 100000


INPUT:

Alice   120000
Bob      90000
Charlie 110000
David    80000


OUTPUT:

true:
[Alice, Charlie]

false:
[Bob, David]


============================================================
CORE SOLUTION
============================================================

Map<Boolean, List<Employee>> result =
        employees.stream()
                .collect(Collectors.partitioningBy(
                        e -> e.getSalary() >= 100_000
                ));


============================================================
MUG-UP LINE
============================================================

stream().collect(
    partitioningBy(predicate)
);


============================================================
MENTAL MODEL
============================================================

                 Employee
                    |
                    v
                PREDICATE
                    |
          +---------+---------+
          |                   |
        true                false
          |                   |
          v                   v
     satisfies           does not
     condition           satisfy


Result:

Map<Boolean, List<Employee>>


============================================================
WHAT IS A PREDICATE?
============================================================

A Predicate<T> is conceptually:

T -> boolean


Example:

Employee -> boolean


employee ->
    employee.getSalary() >= 100_000


For Alice:

120000 >= 100000

true


For Bob:

90000 >= 100000

false


============================================================
PARTITIONINGBY VS GROUPINGBY
============================================================

This distinction is important.


GROUPING BY:

many possible groups


Example:

Engineering
Finance
HR
Sales


Code:

groupingBy(Employee::getDepartment)


Result:

Map<String, List<Employee>>


------------------------------------------------------------


PARTITIONING BY:

exactly TWO groups


true
false


Code:

partitioningBy(
    e -> e.getSalary() >= 100_000
)


Result:

Map<Boolean, List<Employee>>


============================================================
FAST DECISION RULE
============================================================

Question says:

"Group BY category/property"

        ↓

groupingBy()


Question says:

"Split based on CONDITION"

        ↓

partitioningBy()


============================================================
EXAMPLES
============================================================

HIGH SALARY / LOW SALARY:

partitioningBy(
    e -> e.getSalary() >= 100_000
)


ACTIVE / INACTIVE:

partitioningBy(
    Employee::isActive
)


ADULT / MINOR:

partitioningBy(
    person -> person.getAge() >= 18
)


PASS / FAIL:

partitioningBy(
    student -> student.getMarks() >= 40
)


VALID / INVALID:

partitioningBy(
    transaction -> transaction.isValid()
)


============================================================
WITHOUT STREAMS
============================================================

List<Employee> highSalary = new ArrayList<>();
List<Employee> lowSalary = new ArrayList<>();

for (Employee employee : employees) {

    if (employee.getSalary() >= 100_000) {
        highSalary.add(employee);
    } else {
        lowSalary.add(employee);
    }
}


partitioningBy() essentially packages these
two collections into:

Map<Boolean, List<Employee>>


============================================================
DOWNSTREAM COLLECTOR
============================================================

partitioningBy() can also perform aggregation.


Example:

COUNT employees in each partition.


Map<Boolean, Long> counts =
        employees.stream()
                .collect(Collectors.partitioningBy(
                        e -> e.getSalary() >= 100_000,
                        Collectors.counting()
                ));


Result:

true  -> 3
false -> 3


============================================================
SUM SALARIES PER PARTITION
============================================================

Map<Boolean, Double> salaryTotals =
        employees.stream()
                .collect(Collectors.partitioningBy(
                        e -> e.getSalary() >= 100_000,
                        Collectors.summingDouble(
                                Employee::getSalary
                        )
                ));


Pattern:

PARTITION
+
AGGREGATE


============================================================
GET THE TWO GROUPS
============================================================

List<Employee> highSalary =
        result.get(true);


List<Employee> lowSalary =
        result.get(false);


Very simple mental model:

true  = passed predicate

false = failed predicate


============================================================
ORDERING
============================================================

For a normal sequential ordered stream,
employees inside each resulting list retain
their encounter order.


Input:

Alice
Bob
Charlie
David


If:

Alice   -> true
Bob     -> false
Charlie -> true
David   -> false


then:

true:

[Alice, Charlie]


false:

[Bob, David]


============================================================
COMPLEXITY
============================================================

n = number of employees


TIME:

O(n)


Every employee is evaluated once.


SPACE:

O(n)


Employee references are stored in the
resulting lists.


============================================================
COMMON INTERVIEW TRAP
============================================================

You could technically write:

employees.stream()
    .collect(Collectors.groupingBy(
        e -> e.getSalary() >= 100_000
    ));


This also produces approximately:

Map<Boolean, List<Employee>>


But if the classifier is explicitly boolean,
the semantically correct collector is:

partitioningBy()


It communicates the intent:

"TWO-WAY SPLIT"


============================================================
SUBTLE DIFFERENCE
============================================================

partitioningBy() represents the two boolean
partitions.

So conceptually you have:

true  -> [...]
false -> [...]


This makes it especially appropriate when the
problem explicitly requires both sides of a
boolean condition.


============================================================
GENERALIZED PATTERN
============================================================

"Split collection according to yes/no condition"

        ↓

Predicate<T>

        ↓

partitioningBy(predicate)

        ↓

Map<Boolean, List<T>>


============================================================
TRADING / BACKEND EXAMPLES
============================================================

ORDERS:

accepted / rejected

partitioningBy(Order::isValid)


TRADES:

profitable / losing

partitioningBy(
    trade -> trade.getPnl() >= 0
)


TRANSACTIONS:

fraud / legitimate

partitioningBy(
    transaction -> transaction.getRiskScore() >= threshold
)


REQUESTS:

within rate limit / exceeded

partitioningBy(
    request -> request.isWithinLimit()
)


EXECUTIONS:

buy / sell

partitioningBy(
    execution -> execution.getQuantity() > 0
)


============================================================
INTERVIEW RETRIEVAL TREE
============================================================

Need groups?

        |
        v

How many?


BOOLEAN / TWO GROUPS
        |
        v
 partitioningBy()


MULTIPLE KEYS
        |
        v
   groupingBy()


============================================================
RELATION TO PREVIOUS PATTERNS
============================================================

GROUP EMPLOYEES BY DEPARTMENT:

groupingBy(
    Employee::getDepartment
)


FREQUENCY MAP:

groupingBy(
    identity(),
    counting()
)


HIGHEST PER DEPARTMENT:

toMap(
    department,
    employee,
    keepHigherSalary
)


PARTITION EMPLOYEES:

partitioningBy(
    predicate
)


============================================================
MUG-UP TABLE
============================================================

GROUP:

groupingBy(key)


COUNT:

groupingBy(
    key,
    counting()
)


PARTITION:

partitioningBy(predicate)


BEST PER KEY:

toMap(
    key,
    identity(),
    keepBest
)


============================================================
INTERVIEW GOLDEN ANSWER
============================================================

"Because the condition has exactly two outcomes,
I'd use Collectors.partitioningBy(). It returns a
Map<Boolean, List<Employee>>, where true contains
employees satisfying the predicate and false contains
the rest. The operation is O(n)."


============================================================
FASTEST RECALL
============================================================

"SPLIT BY CONDITION"

        ↓

TRUE / FALSE

        ↓

partitioningBy(predicate)


============================================================
ONE-LINE MEMORY HOOK
============================================================

BOOLEAN SPLIT
=
partitioningBy(predicate)


============================================================
RETRIEVAL TRIGGER
============================================================

"Partition / split based on condition"

Think immediately:

Map<Boolean, List<T>>

        +

Collectors.partitioningBy(predicate)


============================================================
*/
