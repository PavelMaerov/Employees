package com.example.employees;

import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/department")
class DepartmentController2 {
    public DepartmentController2(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    private final DepartmentService departmentService;

    @GetMapping("/{id}/salary/min")
    public int minSalaryForDepartment(@PathVariable("id") byte department) {
        return departmentService.minSalaryForDepartment(department);
    }

    @GetMapping("/{id}/salary/max")
    public int maxSalaryForDepartment(@PathVariable("id") byte department) {
        return departmentService.maxSalaryForDepartment(department);
    }

    @GetMapping("/{id}/salary/sum")
    public int sumSalaryForDepartment(@PathVariable("id") byte department) {
        return departmentService.sumSalaryForDepartment(department);
    }

    @GetMapping("/{id}/employees")
    public Collection<Employee> AllEmployeesOfDepartment(@PathVariable("id") byte department) {
        return departmentService.AllEmployeesOfDepartment(department);
    }

    @GetMapping("/employees")
    public Map<Byte, List<Employee>> AllEmployeesByDepartment() {
        return departmentService.AllEmployeesByDepartment();
    }
 }
