package com.example.employees;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/department")
class DepartmentController {
    private final DepartmentService departmentService = new DepartmentService();

    @GetMapping("/min-salary")
    public Employee employeeMinSalaryForDepartment(@RequestParam byte department) {
        return departmentService.employeeMinSalaryForDepartment(department);
    }

    @GetMapping("/max-salary")
    public Employee employeeMaxSalaryForDepartment(@RequestParam byte department) {
        return departmentService.employeeMaxSalaryForDepartment(department);
    }

    @GetMapping(value = "/all", params = {"department"})
    public Collection<Employee> AllEmployeesOfDepartment(@RequestParam byte department) {
        return departmentService.AllEmployeesOfDepartment(department);
    }

    @GetMapping("/all")
    public Collection<Employee> AllEmployeesByDepartment() {
        return departmentService.AllEmployeesByDepartment();
    }
}
