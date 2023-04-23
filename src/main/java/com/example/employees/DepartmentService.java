package com.example.employees;

import com.example.employees.exceptions.EmployeeNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    private final EmployeeService employeeService;

    public DepartmentService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public Employee employeeMinSalaryForDepartment(byte department) {
        return employeeService.AllEmployees().stream()
                .filter(e -> e.getDepartment() == department)
                .min(Comparator.comparingInt(e -> e.getSalary()))
                .orElseThrow(() -> new EmployeeNotFoundException());
    }

    public Employee employeeMaxSalaryForDepartment(byte department) {
        return employeeService.AllEmployees().stream()
                .filter(e -> e.getDepartment() == department)
                .max(Comparator.comparingInt(e -> e.getSalary()))
                .orElseThrow(() -> new EmployeeNotFoundException());
    }

    public Collection<Employee> AllEmployeesOfDepartment(byte department) {
        return employeeService.AllEmployees().stream()
                .filter(e -> e.getDepartment() == department)
                .collect(Collectors.toList());
    }

    public Map<Byte, Collection<Employee>> AllEmployeesByDepartment() {
        return employeeService.AllEmployees().stream()
                .map(e -> e.getDepartment())
                .distinct()
                .collect(Collectors.toMap(e -> e, e -> AllEmployeesOfDepartment(e)));
    }
}
