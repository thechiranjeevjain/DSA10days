package org.chijai.java;

import java.util.*;
import java.util.stream.Collectors;

public class GroupEmployeesByDepartment {

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
                    ", salary=" + salary +
                    '}';
        }
    }

    public static Map<String, List<Employee>> groupByDepartment(
            List<Employee> employees) {

        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }

    public static void main(String[] args) {

        List<Employee> employees = List.of(
                new Employee(1, "Alice", "Engineering", 120_000),
                new Employee(2, "Bob", "Engineering", 150_000),
                new Employee(3, "Charlie", "Finance", 110_000),
                new Employee(4, "David", "Finance", 135_000),
                new Employee(5, "Eva", "HR", 95_000),
                new Employee(6, "Frank", "Engineering", 140_000)
        );

        Map<String, List<Employee>> grouped =
                groupByDepartment(employees);

        grouped.forEach((department, departmentEmployees) -> {
            System.out.println("\n" + department + ":");

            departmentEmployees.forEach(employee ->
                    System.out.println("  " + employee)
            );
        });
    }
}

/*
============================================================
DSA-140 — GROUP EMPLOYEES BY DEPARTMENT
============================================================

PROBLEM:

Given:

List<Employee>

Group employees according to department.

INPUT:

Alice   -> Engineering
Bob     -> Engineering
Charlie -> Finance
David   -> Finance
Eva     -> HR


OUTPUT:

Engineering -> [Alice, Bob]
Finance     -> [Charlie, David]
HR          -> [Eva]


============================================================
CORE ANSWER
============================================================

Map<String, List<Employee>> result =
        employees.stream()
                .collect(
                    Collectors.groupingBy(
                        Employee::getDepartment
                    )
                );


============================================================
MUG-UP LINE
============================================================

employees.stream()
         .collect(Collectors.groupingBy(Employee::getDepartment));


============================================================
MENTAL MODEL
============================================================

Employee

        |
        v

extract department

        |
        v

department becomes KEY

        |
        v

all employees having same key

        |
        v

stored inside List


Result:

Map<Department, List<Employee>>


============================================================
PATTERN
============================================================

GROUP OBJECTS BY PROPERTY

        ↓

Collectors.groupingBy(
        Object::getProperty
)


============================================================
WHAT groupingBy() PRODUCES
============================================================

Conceptually:

for (Employee employee : employees) {

    map.computeIfAbsent(
        employee.getDepartment(),
        key -> new ArrayList<>()
    ).add(employee);
}


Streams simply express this operation declaratively.


============================================================
WITHOUT STREAMS
============================================================

Map<String, List<Employee>> result = new HashMap<>();

for (Employee employee : employees) {

    result.computeIfAbsent(
            employee.getDepartment(),
            key -> new ArrayList<>()
    ).add(employee);
}


============================================================
IMPORTANT COLLECTION PATTERN
============================================================

Map<K, List<V>>

usually means:

ONE KEY
TO
MANY VALUES


Examples:

department -> employees

city -> customers

symbol -> trades

account -> transactions

category -> products


============================================================
COMPLEXITY
============================================================

n = number of employees


TIME:

O(n)


Every employee is processed once.


SPACE:

O(n)


All employee references are stored inside
the grouped lists.


============================================================
COMMON INTERVIEW VARIANTS
============================================================


1. COUNT EMPLOYEES PER DEPARTMENT
------------------------------------------------------------

Map<String, Long> counts =
        employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.counting()
                ));


Pattern:

GROUP
+
COUNT


------------------------------------------------------------
2. EMPLOYEE NAMES PER DEPARTMENT
------------------------------------------------------------

Map<String, List<String>> names =
        employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.mapping(
                                Employee::getName,
                                Collectors.toList()
                        )
                ));


Pattern:

GROUP
+
MAP


------------------------------------------------------------
3. AVERAGE SALARY PER DEPARTMENT
------------------------------------------------------------

Map<String, Double> averages =
        employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.averagingDouble(
                                Employee::getSalary
                        )
                ));


Pattern:

GROUP
+
AGGREGATE


------------------------------------------------------------
4. TOTAL SALARY PER DEPARTMENT
------------------------------------------------------------

Map<String, Double> totals =
        employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.summingDouble(
                                Employee::getSalary
                        )
                ));


Pattern:

GROUP
+
SUM


------------------------------------------------------------
5. HIGHEST PAID PER DEPARTMENT
------------------------------------------------------------

Map<String, Optional<Employee>> highest =
        employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.maxBy(
                                Comparator.comparingDouble(
                                        Employee::getSalary
                                )
                        )
                ));


Pattern:

GROUP
+
MAX


============================================================
GROUPINGBY MENTAL TREE
============================================================

                     groupingBy
                         |
             +-----------+-----------+
             |                       |
         GROUP ONLY              GROUP + ACTION
             |                       |
             v                       v
     Map<K, List<V>>           downstream collector
                                     |
                    +----------------+---------------+
                    |                |               |
                  count             sum             max
                    |                |               |
                 counting()    summingDouble()    maxBy()


============================================================
TRADING / BACKEND EXAMPLES
============================================================

Same pattern:

trade -> symbol

Map<String, List<Trade>>


execution -> account

Map<String, List<Execution>>


order -> instrument

Map<String, List<Order>>


transaction -> customer

Map<String, List<Transaction>>


risk event -> risk type

Map<RiskType, List<RiskEvent>>


============================================================
INTERVIEW DISTINCTION
============================================================

Question:

"Group employees by department"

        ↓

groupingBy()


Question:

"Partition employees into salary > 100k
and salary <= 100k"

        ↓

partitioningBy()


groupingBy:

many possible keys


partitioningBy:

exactly two groups:

true
false


============================================================
RETRIEVAL TRIGGER
============================================================

"GROUP objects BY some property"

Think immediately:

stream
    -> collect
    -> groupingBy


============================================================
ONE-LINE MEMORY HOOK
============================================================

GROUP BY PROPERTY

=

stream().collect(
    groupingBy(Object::getProperty)
)


============================================================
FASTEST RECALL
============================================================

GROUP EMPLOYEES BY DEPARTMENT

        ↓

Map<String, List<Employee>>

        ↓

employees.stream()
         .collect(
             Collectors.groupingBy(
                 Employee::getDepartment
             )
         );


============================================================
*/