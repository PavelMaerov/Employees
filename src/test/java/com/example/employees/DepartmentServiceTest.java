package com.example.employees;

import com.example.employees.exceptions.EmployeeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {
    public DepartmentServiceTest() {
        //в конструкторе теста определить заглушаемый метод c помощью Mockito.when не удастся.
        //Будет NullPointerException, т.к. employeeService еще не создан.
        //Mockito.when(employeeService.AllEmployees()).thenReturn(testEmployeeCollection);
    }
    @Mock  //объект-заглушка
    EmployeeService employeeService; //Mockito сам создаст этот объект, правда с пустыми результатами методов
    //Объект будет готов только в вызове BeforeEach, не раньше. Если обратиться раньше, то значение этого поля = null

    @InjectMocks
    DepartmentService departmentService; //Тестируемый сервис. Правильное имя - out.
    //Mockito сам создаст этот объект и внедрит в его поля заглушки
    //но только, если Mockito найдет конструктор DepartmentService по умолчанию, куда можно подставить заглушку.
    //Например, если будет два конструктора с заглушкой или в конструкторе будут неизвестные для Mockito параметры, то возникнет такая ошибка:
    //org.mockito.exceptions.base.MockitoException:
    //Cannot instantiate @InjectMocks field named 'departmentService'! Cause: the type 'DepartmentService' has no default constructor
    //You haven't provided the instance at field declaration so I tried to construct the instance.
    //Examples of correct usage of @InjectMocks:
    //   @InjectMocks Service service = new Service();
    //   @InjectMocks Service service;
    //   //and... don't forget about some @Mocks for injection :)
    //Если все-таки надо нам самим конструировать сервис, то его конструктор надо просто вызывать из BeforeEach,
    //когда мок уже будет готов. И самим передавать мок в специфический конструктор сервиса. @InjectMocks тогда не пригодится

    //результат для заглушки
    private static final List<Employee> testEmployeeCollection = List.of(
            //тестовый список, на котором будем делать проверки.
            //Мокаем им метод employeeService.AllEmployees перед каждой проверкой
            //Интересно, что реальный сервис у меня ограничен 3мя работниками. Но в мок можно положить что угодно.
            new Employee("Иван2", "Иванов2", (byte) 2, 21000),
            new Employee("Петр2", "Петров2", (byte) 2, 22000),
            new Employee("Иван3", "Иванов3", (byte) 3, 31000),
            new Employee("Петр3", "Петров3", (byte) 3, 32000),
            new Employee("Сидор3", "Сидоров3", (byte) 3, 33000)
    );

    @BeforeEach   //Хотя у нас мок всегда одинаковый, его метод придется определять перед каждым тестом
                  //Нет аннотации - перед всеми. Есть @BeforeAll, но она требует статический метод.
    void BeforeEach() {
        //departmentService=new DepartmentService(employeeService,1); //Если сервис надо создать специфическим образом
        Mockito.when(employeeService.AllEmployees()).thenReturn(testEmployeeCollection);
    }

    //----------------эксперимент
    private static Stream<Arguments> paramsForSalaryTest(){
        //Однотипных методов сервиса целых 5, да еще в каждом тестировать разные отделы и на исключение.
        //Писать 10 тестов, из которых 5 параметрических - поленился.
        //Решил все тесты все собрать в один поток.
        //К сожалению Mockito требует статический метод для возврата параметров и сюда нельзя передать
        //в качестве параметра Arguments.of тестируемый метод сервиса, который не статический.
        //Пришлось здесь использовать строку переключатель, а уже в тесте определять вызываемый метод
        return Stream.of( //проверяемый метод - отдел - результат
                Arguments.of("employeeMin", (byte)2, testEmployeeCollection.get(0)),
                Arguments.of("employeeMin", (byte)3, testEmployeeCollection.get(2)),
                Arguments.of("employeeMin", (byte)1, null),
                Arguments.of("min", (byte)2, 21000),
                Arguments.of("min", (byte)3, 31000),
                Arguments.of("min", (byte)1, null),
                Arguments.of("employeeMax", (byte)2, testEmployeeCollection.get(1)),
                Arguments.of("employeeMax", (byte)3, testEmployeeCollection.get(4)),
                Arguments.of("employeeMax", (byte)1, null),
                Arguments.of("max", (byte)2, 22000),
                Arguments.of("max", (byte)3, 33000),
                Arguments.of("max", (byte)1, null),
                Arguments.of("sum", (byte)2, 43000),
                Arguments.of("sum", (byte)3, 96000),
                Arguments.of("sum", (byte)1, 0)
                );
    }

    @ParameterizedTest
    @MethodSource("paramsForSalaryTest")
    void salaryTest(String action, byte department, Object expected) {
        Function<Byte, Object> employeeMethod = switch(action){  //только начиная с 14ой Java
            case "employeeMin"->departmentService::employeeMinSalaryForDepartment;
            case "employeeMax"->departmentService::employeeMaxSalaryForDepartment;
            case "min"->departmentService::minSalaryForDepartment;
            case "max"->departmentService::maxSalaryForDepartment;
            case "sum"->departmentService::sumSalaryForDepartment;
            default->null;  //без default не компилируется
        };
        if (expected!=null) {
            assertEquals(expected, employeeMethod.apply(department));
        } else{
            assertThrows(EmployeeNotFoundException.class, ()->employeeMethod.apply(department));
        }
    }
    private static Stream<Arguments> paramsForAllEmployeesOfDepartmentTest(){
        return Stream.of(
                Arguments.of((byte)2,List.of(
                        testEmployeeCollection.get(0),
                        testEmployeeCollection.get(1)
                )),
                Arguments.of((byte)3,List.of(
                        testEmployeeCollection.get(4),  //специально порядок обратный, чтобы проверить, что сортировка работает
                        testEmployeeCollection.get(3),
                        testEmployeeCollection.get(2)
                )),
                Arguments.of((byte)1,List.of())
        );
    }
    @ParameterizedTest
    @MethodSource("paramsForAllEmployeesOfDepartmentTest")
    void allEmployeesOfDepartmentTest(byte department, List<Employee> expected) {
        assertEquals( //чтобы сравнить списки assertEqualsом, их надо сначала отсортировать
                expected.stream().sorted().collect(Collectors.toList()),
                departmentService.AllEmployeesOfDepartment(department)
                        .stream().sorted().collect(Collectors.toList())
        );
    }

    @Test
    void allEmployeesByDepartmentTest() {
        //не нашел в org.junit.jupiter.api.Assertions как сравнивать мапы со списками.
        //из-за разного порядка элементов в списке тест не проходит.
        //также не справилась библиотека org.assertj.core.api.Assertions
        //видимо Values из мапы сравнивается с помощью eguals в обоих библиотеках.
        //А т.к. Values - это списки, то никто их преварительно не сортирует
        //Значит придется сортировать мне.
        Map<Byte, List<Employee>> actual = departmentService.AllEmployeesByDepartment();
        //хотя я возвращаю из мок-сервиса unmodifiableCollection, образованную List.of,
        //после сортировки по отделам значения мапы (списки работников отдела) - изменяемы;
        actual.forEach((k,v)->Collections.sort(v));

        //Map.of создает ImmutableCollections
        //и List.of тоже создает ImmutableCollections
        //и пересоздание с помощью new HashMap<>(Map.of()) все равно оставляет вложенные списки - Immutable
        //можно конечно отсортировать expected на глаз, но можно ведь и ошибиться
        //чтобы была возможность сортировать, пришлось набирать expected по шагам
        Map<Byte, List<Employee>> expected = new HashMap<>();
        List<Employee> list2 = new ArrayList<>();
        list2.add(testEmployeeCollection.get(0));
        list2.add(testEmployeeCollection.get(1));
        expected.put((byte)2, list2);

        List<Employee> list3 = new ArrayList<>();
        list3.add(testEmployeeCollection.get(4));  //специально порядок обратный, чтобы проверить, что сортировка работает
        list3.add(testEmployeeCollection.get(3));
        list3.add(testEmployeeCollection.get(2));
        expected.put((byte)3, list3);

        expected.forEach((k,v)->Collections.sort(v));

        assertEquals(expected, actual);
    }
}