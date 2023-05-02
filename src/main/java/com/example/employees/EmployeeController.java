package com.example.employees;

import org.springframework.web.bind.annotation.*;
import java.util.Collection;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
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

    /*
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmployeeIncorrectNameException.class)
    public Exception incorrectNameExceptionHandler(EmployeeIncorrectNameException e) {
        //return e.getMessage(); Так возвращается слишком мало, только сообщение
        //return e; Так возвращается слишком много - stackTrace
        //как сохранить структуру возврата по умолчанию, но со своим сообщением - не разобрался
    }
    */
 }
