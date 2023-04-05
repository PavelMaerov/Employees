package com.example.employees;

import com.example.employees.exceptions.EmployeeAlreadyAddedException;
import com.example.employees.exceptions.EmployeeNotFoundException;
import com.example.employees.exceptions.EmployeeStorageIsFullException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeService {
    private final Map<String, Employee> employeeBook = new HashMap<>();
    private final int limit = 3;
    private String key(String firstName, String lastName) {   //конструируем ключ для Map
        return firstName + " " + lastName;
    }

    public Collection<Employee> AllEmployees() {
        return Collections.unmodifiableCollection(employeeBook.values());
    }

    //1. Добавить сотрудника.
    public Employee add(String firstName, String lastName) {
        Employee e = new Employee(firstName, lastName);
        String key = key(firstName, lastName);
        if (employeeBook.containsKey(key)) {
            throw new EmployeeAlreadyAddedException();
        }
        if (employeeBook.size() == limit) {
            throw new EmployeeStorageIsFullException();
        }
        employeeBook.put(key, e);
        return e;
    }

    //2. Удалить сотрудника.
    public Employee remove(String firstName, String lastName) {
        Employee e = employeeBook.remove(key(firstName, lastName));
        if (e == null) {
            throw new EmployeeNotFoundException();
        }
        return e;
    }

    //3. Найти сотрудника.
    public Employee find(String firstName, String lastName) {
        Employee e = employeeBook.get(key(firstName, lastName));
        if (e == null) {
            throw new EmployeeNotFoundException();
        }
        return e;
    }
}
