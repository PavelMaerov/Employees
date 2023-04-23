package com.example.employees;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/department")
class DepartmentController {
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    private final DepartmentService departmentService;

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
    public Map<Byte, Collection<Employee>> AllEmployeesByDepartment() {
        return departmentService.AllEmployeesByDepartment();
    }
}
