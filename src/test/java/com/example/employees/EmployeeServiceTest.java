package com.example.employees;

import com.example.employees.exceptions.EmployeeAlreadyAddedException;
import com.example.employees.exceptions.EmployeeIncorrectNameException;
import com.example.employees.exceptions.EmployeeNotFoundException;
import com.example.employees.exceptions.EmployeeStorageIsFullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class EmployeeServiceTest {
    private EmployeeService employeeService; //Тестируемый сервис. Не инициализируем
    //делаем перед каждым тестом employeeService = new EmployeeService() для очистки;
    private final List<Employee> testEmployeeCollection = List.of(
            //тестовый список, на котором будем делать проверки. Перебрасываем в сервис перед каждой проверкой
            new Employee("Иван", "Иванов", (byte) 2, 1000),
            new Employee("Петр", "Петров", (byte) 2, 2000)
            );

    @BeforeEach
    public void initEmployeeBook() {
        //Очистим список работников. Мы не знаем их имена от предыдущих тестов, поэтому не можем вызвать remove
        //Можно конечно сначала получить всех и поудалять по одному. Но вдруг в этих операциях кроется ошибка
        employeeService = new EmployeeService();
        //Заполним сервис работниками из тестового списка
        for (Employee emp : testEmployeeCollection) {
            employeeService.add(emp.getFirstName(), emp.getLastName(), emp.getDepartment(),emp.getSalary());
        }
    }

    @Test
    void allEmployeesTest() {
        //не нашел никаких преимуществ у assertIterableEquals,
        //assertEquals работает также хорошо
        //видимо assertIterableEquals нужен для итерируемых объектов, у которых нет equals
        //a у ArrayList equals есть
        assertEquals(testEmployeeCollection.stream().sorted().collect(Collectors.toList()),
                employeeService.AllEmployees().stream().sorted().collect(Collectors.toList()));
        //изменим кому-нибудь зарплату, проверим потом на неравенство
        //чтобы убедиться, что сравниваются все поля всех объектов 2х коллекций
        testEmployeeCollection.get(1).setSalary(testEmployeeCollection.get(1).getSalary()+1);
        assertNotEquals(testEmployeeCollection.stream().sorted().collect(Collectors.toList()),
                employeeService.AllEmployees().stream().sorted().collect(Collectors.toList()));
    }

    @Test
    void addTest() {
        int beforeAddSize = employeeService.AllEmployees().size();
        Employee exp = new Employee("Тест","Тестов", (byte)0, 1111);
        Employee act = employeeService.add(exp.getFirstName(), exp.getLastName(), exp.getDepartment(), exp.getSalary());
        //возвращен тот работник, реквизиты которого мы передали
        assertEquals(exp, act);
        //работник, реквизиты которого мы передали, действительно теперь существует в сервисе
        assertTrue(employeeService.AllEmployees().contains(exp));
        //количество работников увеличилось на 1
        assertEquals(beforeAddSize+1, employeeService.AllEmployees().size());
        //повторное добавление того же работника вызывает ошибку EmployeeAlreadyAddedException
        assertThrows(EmployeeAlreadyAddedException.class,
                ()->employeeService.add(exp.getFirstName(), exp.getLastName(), (byte)0, 0));
        //добавление четвертого работника вызывает ошибку EmployeeStorageIsFullException
        assertThrows(EmployeeStorageIsFullException.class,
                ()->employeeService.add("aaa", "bbb", (byte)0, 0));
    }

    static Stream<Arguments> paramsForNameCheck() {
        return Stream.of(
                Arguments.of("Test", "Test"),
                Arguments.of("аБВ", "Абв"),
                Arguments.of("мамин cибиряк", ""), //если имя не приемлимо, то второй параметр = ""
                Arguments.of("Test12",""),
                Arguments.of("!Test",""),
                Arguments.of("Test_",""),
                Arguments.of("","")
                );
    }
    @ParameterizedTest
    @MethodSource("paramsForNameCheck")
    void addTestNameCheck(String sourceName, String expectedName) {
        if (!expectedName.isEmpty()) { //Эти комплекты параметров не должны вызывать исключение
            Employee e = employeeService.add(sourceName, sourceName, (byte) 0, 0);
            assertEquals(expectedName, e.getFirstName());
            assertEquals(expectedName, e.getLastName());
            employeeService.remove(e.getFirstName(), e.getLastName());
        } else { //Эти комплекты параметров должны вызывать исключение
            assertThrows(EmployeeIncorrectNameException.class,
                    ()->employeeService.add(sourceName, sourceName, (byte) 0, 0));
        }
    }

    @Test
    void removeTest() {
        int beforeRemoveSize = employeeService.AllEmployees().size();
        Employee exp = testEmployeeCollection.get(1);
        Employee act = employeeService.remove(exp.getFirstName(), exp.getLastName());
        //возвращен тот работник, реквизиты которого мы передали
        assertEquals(exp, act);
        //работник, реквизиты которого мы передали, действительно теперь удален в сервисе
        assertFalse(employeeService.AllEmployees().contains(exp));
        //количество работников уменьшилось на 1
        assertEquals(beforeRemoveSize-1, employeeService.AllEmployees().size());
        //повторное удаление того же работника вызывает ошибку EmployeeAlreadyAddedException
        assertThrows(EmployeeNotFoundException.class,
                ()->employeeService.remove(exp.getFirstName(), exp.getLastName()));
     }

    @Test
    void find() {
        //возьмем какого-нибудь работника из исходого списка и поищем его в сервисе
        Employee exp=testEmployeeCollection.get(1);
        assertEquals(exp,employeeService.find(exp.getFirstName(), exp.getLastName()));
        assertThrows(EmployeeNotFoundException.class, ()->employeeService.remove("Тест", "Тестов"));
    }
}