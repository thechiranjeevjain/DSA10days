package org.chijai.java;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class EmployeeMaxSalary {

    static class Employee {
        private final String name;
        private final double salary;

        Employee(String name, double salary) {
            this.name = name;
            this.salary = salary;
        }

        public String getName() {
            return name;
        }

        public double getSalary() {
            return salary;
        }

        @Override
        public String toString() {
            return "Employee{name='" + name + "', salary=" + salary + "}";
        }
    }

    public static Optional<Employee> findHighestPaidEmployee(
            List<Employee> employees) {

        return employees.stream()
                .max(Comparator.comparingDouble(Employee::getSalary));
    }

    public static void main(String[] args) {

        List<Employee> employees = List.of(
                new Employee("Alice", 80_000),
                new Employee("Bob", 120_000),
                new Employee("Charlie", 100_000),
                new Employee("David", 95_000)
        );

        Optional<Employee> highestPaid =
                findHighestPaidEmployee(employees);

        highestPaid.ifPresent(employee ->
                System.out.println("Highest paid employee: " + employee)
        );
    }
}

/*
OUTPUT:
Highest paid employee: Employee{name='Bob', salary=120000.0}


==================== INTERVIEW RECALL ====================

PROBLEM:
Given List<Employee>, find the highest-paid employee using Streams.

PATTERN:
Collection
    -> stream()
    -> max()
    -> Comparator

CORE LINE:

employees.stream()
         .max(Comparator.comparingDouble(Employee::getSalary));


WHY OPTIONAL?
The list could be empty, so max() returns Optional<Employee>.


COMPLEXITY:
Time  : O(n)
Space : O(1)


RETRIEVAL TRIGGER:

"Find max/min object based on a field"

        ↓

stream()
    .max/min(
        Comparator.comparingX(Object::getField)
    )


VARIANTS:

// Lowest salary
employees.stream()
        .min(Comparator.comparingDouble(Employee::getSalary));

// Highest salary value only
employees.stream()
        .mapToDouble(Employee::getSalary)
        .max();

// Highest paid, guaranteed non-empty
Employee employee = employees.stream()
        .max(Comparator.comparingDouble(Employee::getSalary))
        .orElseThrow();

==========================================================
*/