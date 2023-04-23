package com.example.employees.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FOUND)
public class EmployeeAlreadyAddedException extends RuntimeException {
}
