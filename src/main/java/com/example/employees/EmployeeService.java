package com.example.employees;

import com.example.employees.exceptions.EmployeeAlreadyAddedException;
import com.example.employees.exceptions.EmployeeNotFoundException;
import com.example.employees.exceptions.EmployeeStorageIsFullException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


@Service
public class EmployeeService {
    private final ArrayList<Employee> list = new ArrayList();
    private final int limit = 3;

    public List<Employee> AllEmployees() {
        return Collections.unmodifiableList(list);
    }

    //1. Добавить сотрудника.
    public Employee add(String firstName, String lastName) {
        Employee e = new Employee(firstName, lastName);
        if (list.contains(e)) {
            throw new EmployeeAlreadyAddedException();
        }
        if (list.size() == limit) {
            throw new EmployeeStorageIsFullException();
        }
        list.add(e);
        return e;
    }

    //2. Удалить сотрудника.
    public Employee remove(String firstName, String lastName) {
        Employee e = new Employee(firstName, lastName);
        if (!list.remove(e)) {
            throw new EmployeeNotFoundException();
        }
        return e;
    }
    //3. Найти сотрудника.
    public Employee find(String firstName, String lastName) {
        Employee e = new Employee(firstName, lastName);
        if (!list.contains(e)) {
            throw new EmployeeNotFoundException();
        }
        return e;
    }
}
