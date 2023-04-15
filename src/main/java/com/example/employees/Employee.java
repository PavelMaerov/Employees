package com.example.employees;

import java.util.Objects;

public class Employee {
    private String firstName;
    private String lastName;
    private byte department;
    private int salary;

    public Employee(String firstName, String lastName, byte department, int salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.salary = salary;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public byte getDepartment() {
        return department;
    }
    public int getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Работник " + firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return firstName.equals(employee.firstName) && lastName.equals(employee.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

}
