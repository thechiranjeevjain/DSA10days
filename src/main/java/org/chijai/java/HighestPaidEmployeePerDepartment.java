package org.chijai.java;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HighestPaidEmployeePerDepartment {

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

    public static Map<String, Employee> highestPaidPerDepartment(
            List<Employee> employees) {

        return employees.stream()
                .collect(Collectors.toMap(
                        Employee::getDepartment,
                        Function.identity(),
                        (existing, candidate) ->
                                candidate.getSalary() > existing.getSalary()
                                        ? candidate
                                        : existing
                ));
    }

    public static void main(String[] args) {

        List<Employee> employees = List.of(
                new Employee(1, "Alice", "Engineering", 120_000),
                new Employee(2, "Bob", "Engineering", 150_000),
                new Employee(3, "Charlie", "Engineering", 140_000),

                new Employee(4, "David", "Finance", 110_000),
                new Employee(5, "Eva", "Finance", 135_000),

                new Employee(6, "Frank", "HR", 90_000),
                new Employee(7, "Grace", "HR", 95_000)
        );

        Map<String, Employee> result =
                highestPaidPerDepartment(employees);

        System.out.println("Highest paid employee per department:");

        result.forEach((department, employee) ->
                System.out.println(department + " -> " + employee)
        );
    }
}

/*
============================================================
DSA-139 — HIGHEST PAID EMPLOYEE PER DEPARTMENT
============================================================

PROBLEM:

Given:

List<Employee>

Return:

maximum salary employee for every department.


Example:

Engineering:
Alice   120
Bob     150
Charlie 140

Finance:
David   110
Eva     135


Result:

Engineering -> Bob
Finance     -> Eva


============================================================
CORE PATTERN
============================================================

GROUP KEY
+
KEEP BEST VALUE


For each employee:

key   = department
value = employee


If department is seen again:

compare existing employee
vs
candidate employee


Keep:

higher salary employee


============================================================
CORE STREAM SOLUTION
============================================================

employees.stream()
        .collect(Collectors.toMap(
                Employee::getDepartment,
                Function.identity(),
                (a, b) ->
                        a.getSalary() >= b.getSalary()
                                ? a
                                : b
        ));


============================================================
MUG-UP LINE
============================================================

toMap(
    Employee::getDepartment,
    Function.identity(),
    (a, b) -> a.salary >= b.salary ? a : b
)


============================================================
MENTAL MODEL
============================================================

Employee

        |
        v

department becomes KEY

        |
        v

employee becomes VALUE

        |
        v

duplicate department?

        |
        v

merge employees

        |
        v

KEEP HIGHER SALARY


============================================================
WHY toMap() WORKS WELL
============================================================

Collectors.toMap() accepts:

1. key mapper
2. value mapper
3. merge function


Here:

KEY:

Employee::getDepartment


VALUE:

Function.identity()

means:

the employee object itself.


MERGE:

(existing, candidate) ->
        higherSalaryEmployee


============================================================
FUNCTION.IDENTITY()
============================================================

Function.identity()

means:

x -> x


Therefore:

Function.identity()

and

employee -> employee

are equivalent.


============================================================
ALTERNATIVE — GROUPINGBY + MAXBY
============================================================

Another common interview solution:

Map<String, Optional<Employee>> result =
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

groupingBy
+
maxBy


============================================================
WHICH VERSION IS BETTER?
============================================================

For this exact problem:

toMap + merge

is compact and directly returns:

Map<String, Employee>


groupingBy + maxBy

returns:

Map<String, Optional<Employee>>

which is slightly noisier.


So for interview coding:

toMap + merge

is often the cleanest.


============================================================
COMPLEXITY
============================================================

Let:

n = number of employees


Each employee processed once.

HashMap insert/update:

O(1) average


TIME:

O(n)


SPACE:

O(d)

where:

d = number of departments


============================================================
TIE CASE
============================================================

Suppose:

Alice = 150000
Bob   = 150000


Current code:

candidate.getSalary() > existing.getSalary()

keeps existing employee.


If interviewer wants deterministic tie-breaking:

higher salary
then smaller employee ID


Use:

(existing, candidate) -> {

    if (candidate.getSalary() > existing.getSalary()) {
        return candidate;
    }

    if (candidate.getSalary() < existing.getSalary()) {
        return existing;
    }

    return candidate.getId() < existing.getId()
            ? candidate
            : existing;
}


============================================================
GENERALIZABLE PATTERN
============================================================

This problem is NOT really about employees.

It is:

"Find BEST OBJECT PER KEY."


Examples:

highest salary per department

latest trade per symbol

largest transaction per account

latest event per user

cheapest product per category

highest score per student

maximum exposure per desk


Mental template:

stream()
    .collect(
        toMap(
            KEY,
            OBJECT,
            KEEP_BEST
        )
    );


============================================================
INTERVIEW RETRIEVAL TRIGGER
============================================================

"Maximum object PER something"

Think:

KEY BY GROUP
+
MERGE COLLISIONS
+
KEEP MAX


============================================================
ONE-LINE MEMORY HOOK
============================================================

BEST OBJECT PER GROUP
=
toMap(groupKey, identity, keepBest)


============================================================
*/