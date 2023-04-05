package com.example.employees;

import com.example.employees.exceptions.EmployeeAlreadyAddedException;
import com.example.employees.exceptions.EmployeeNotFoundException;
import com.example.employees.exceptions.EmployeeStorageIsFullException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

@Service
public class EmployeeService {
    private final Map<Employee, Object> employeeBook = new HashMap<>();
    //В типе Object пока хранить нечего. Буду писать туда просто 0. По-хорошему нужен просто Set. Но в задании - Map
    //Писать в Object - null можно, но тогда возвращаемое value становится неотличимым от неуспешных вставок и удаления
    private final int limit = 3;

    public Map<Employee, Object> AllEmployees() {
        return Collections.unmodifiableMap(employeeBook);
    }

    //1. Добавить сотрудника.
    public Employee add(String firstName, String lastName) {
        Employee e = new Employee(firstName, lastName);
        if (employeeBook.size() == limit) {
            throw new EmployeeStorageIsFullException();
        }
        //Если данные у работника появятся и их нельзя будет перезаписывать, то так делать нельзя.
        //Тогда надо будет проверить с помощью containsKey отсутствие работника, а затем только делать put
        if (employeeBook.put(e, 0) != null) {  //если существовало старое value, put его вернет, иначе вернет null
            throw new EmployeeAlreadyAddedException();
        }
        return e;
    }

    //2. Удалить сотрудника.
    public Employee remove(String firstName, String lastName) {
        Employee e = new Employee(firstName, lastName);
        if (employeeBook.remove(e) == null) {  //при листе - remove возвращает true|false, а при map - удаленное value или null
            throw new EmployeeNotFoundException();
        }
        return e;
    }

    //3. Найти сотрудника.
    public Employee find(String firstName, String lastName) {
        Employee e = new Employee(firstName, lastName);
        if (!employeeBook.containsKey(e)) {
            throw new EmployeeNotFoundException();
        }
        return e;
    }
}
