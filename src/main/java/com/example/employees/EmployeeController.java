package com.example.employees;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public EmployeeController() {
        employeeService = new EmployeeService();
        departmentService = new DepartmentService(employeeService);
    }

    @GetMapping
    public Collection<Employee> AllEmployees() {
        return employeeService.AllEmployees();
    }
    @GetMapping("/add")
    public Employee add(@RequestParam String firstName,
                        @RequestParam String lastName,
                        @RequestParam byte department,
                        @RequestParam int salary) {
        return employeeService.add(firstName,lastName,department,salary);
    }
    @GetMapping("/remove")
    public Employee remove(@RequestParam String firstName,
                           @RequestParam String lastName) {
        return employeeService.remove(firstName,lastName);
    }
    @GetMapping("/find")
    public Employee find(@RequestParam String firstName,
                         @RequestParam String lastName) {
        return employeeService.find(firstName,lastName);
    }

    //Два контроллера в разных классах сделать можно, но поскольку я сам их не создаю,
    //то и не могу раздать им общую ссылку на EmployeeService, где лежат данные.
    //Т.е. поле employeeService может существовать либо в "старом" контроллере для работников (пополнение, удаление и пр)
    //либо (по заданию) будет "заинжекчено" в новый сервис нового контроллера (где работа с отделами)

    //Можно сделать один контроллер с двумя сервисами,
    //но тогда придется отказаться от удобства под названием @RequestMapping

    //Предлагается самодеятельное решение - вложить второй контроллер в первый.
    //Оба зайца убиваются - вложенный класс видит ссылку на сервис из внешнего класса
    //@RequestMapping - свой для каждого класса
    //Не нашел в интернете - можно ли так делать, но на тестах ничего плохого не заметил
    @RestController
    @RequestMapping("/department")
    class DepartmentController {
        @GetMapping("/min-salary")
        public Employee employeeMinSalaryForDepartment(@RequestParam byte department){
            return departmentService.employeeMinSalaryForDepartment(department);
        }
        @GetMapping("/max-salary")
        public Employee employeeMaxSalaryForDepartment(@RequestParam byte department){
            return departmentService.employeeMaxSalaryForDepartment(department);
        }
        @GetMapping(value="/all", params={"department"})
        public Collection<Employee> AllEmployeesOfDepartment(@RequestParam byte department) {
            return departmentService.AllEmployeesOfDepartment(department);
        }
        @GetMapping("/all")
        public Collection<Employee> AllEmployeesByDepartment() {
            return departmentService.AllEmployeesByDepartment();
        }
    }
}
