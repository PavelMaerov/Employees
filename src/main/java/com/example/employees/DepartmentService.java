package com.example.employees;

import com.example.employees.exceptions.EmployeeNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
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
                .min(Comparator.comparingInt(Employee::getSalary))
                .orElseThrow(() -> new EmployeeNotFoundException());
    }
    public int minSalaryForDepartment(byte department) {
        return employeeMinSalaryForDepartment(department).getSalary();
    }

    public Employee employeeMaxSalaryForDepartment(byte department) {
        return employeeService.AllEmployees().stream()
                .filter(e -> e.getDepartment() == department)
                .max(Comparator.comparingInt(Employee::getSalary))
                .orElseThrow(() -> new EmployeeNotFoundException());
    }
    public int maxSalaryForDepartment(byte department) {
        return employeeMaxSalaryForDepartment(department).getSalary();
    }

    public List<Employee> AllEmployeesOfDepartment(byte department) {
        return employeeService.AllEmployees().stream()
                .filter(e -> e.getDepartment() == department)
                .collect(Collectors.toList());  //List - чтобы сортировать в тестах
    }
    public int sumSalaryForDepartment(byte department) {
        return AllEmployeesOfDepartment(department).stream().mapToInt(Employee::getSalary).sum();
    }

    public Map<Byte, List<Employee>> AllEmployeesByDepartment() {
        return employeeService.AllEmployees().stream()
                .map(e -> e.getDepartment())
                .distinct()
                .collect(Collectors.toMap(e -> e, e -> AllEmployeesOfDepartment(e)));
    }
}
