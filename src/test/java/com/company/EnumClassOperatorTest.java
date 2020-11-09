package com.company;

import com.company.exception.DivisionByZeroException;
import com.company.service.EnumClassOperator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EnumClassOperatorTest {
    @ParameterizedTest(name = "Тест метода EnumClassOperator.ADD.calculate({0}, {1})={2}")
    @CsvSource({"1, 2, 3",
               "5.2, 7, 12.2"})
    void Add(double a, double b, double sum)
            throws DivisionByZeroException{
        assertThat(sum, closeTo(EnumClassOperator.ADD.calculate(a, b), 0.000001));
    }

    @ParameterizedTest(name = "Тест метода EnumClassOperator.SUBTRACT.calculate({0}, {1})={2}")
    @CsvSource({"1, 2, -1",
            "5.2, 7, -1.8"})
    void SUBTRACT(double a, double b, double diff)
            throws DivisionByZeroException{
        assertThat(diff, closeTo(EnumClassOperator.SUBTRACT.calculate(a, b), 0.000001));
    }

    @ParameterizedTest(name="Тест метода EnumClassOperator.MULTIPLY.calculate({0},{1})={2}")
    @CsvSource({"1, 2, 2",
            "5, 7, 35"})
    void MULTIPLY(double a, double b, double composition)
            throws DivisionByZeroException{
        assertThat(composition, closeTo(EnumClassOperator.MULTIPLY.calculate(a, b), 0.000001));
    }

    @ParameterizedTest(name="Тест метода EnumClassOperator.DIVISION.calculate({0},{1}) = {2}")
    @CsvSource({"1, 2, 0.5",
            "4, 2, 2"})
    void DIVISION(double a, double b, double div)
            throws DivisionByZeroException{
        assertThat(div, closeTo(EnumClassOperator.DIVISION.calculate(a, b), 0.000001));
    }

    @ParameterizedTest(name="Тест метода EnumClassOperator.DIVISION.calculate({0}, {1}) с исключением DivisionZeroException")
    @CsvSource({"1, 0"})
    void DIVISION_shouldBeException(double a, double b) {
        assertThrows(DivisionByZeroException.class, ()->EnumClassOperator.DIVISION.calculate(a, b));
    }

    @ParameterizedTest(name="Тест метода EnumClassOperator.NEGATION.calculate({0})={1}")
    @CsvSource({"1, -1",
            "4, -4"})
    void NEGATION(double a, double neg)
            throws DivisionByZeroException{
        assertThat(neg, closeTo(EnumClassOperator.NEGATION.calculate(a), 0.000001));
    }

    @ParameterizedTest(name="Тест метода EnumClassOperator.POWER.calculate({0},{1})={2}")
    @CsvSource({"1, 2, 1",
            "2, 3, 8"})
    void POWER(double a, double b, double power)
            throws DivisionByZeroException{
        assertThat(power, closeTo(EnumClassOperator.POWER.calculate(a, b), 0.000001));
    }
}
