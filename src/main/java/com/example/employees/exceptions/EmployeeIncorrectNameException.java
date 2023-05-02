package com.example.employees.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EmployeeIncorrectNameException extends RuntimeException {
    //public EmployeeIncorrectNameException(String message) {
    //    super(message);
    //}
}
