package com.company;

import com.company.exception.DivisionByZeroException;
import com.company.exception.InvalidFormulaException;
import com.company.service.CalculationFormulaService;
import com.company.service.CalculationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("Calculation")
public class CalculationServiceTest {
    private static CalculationService calculationService;

    @BeforeAll
    static void setup(){
        calculationService = new CalculationFormulaService();
    }

    @ParameterizedTest(name="Тест метода CalculationService.calculate({0})={1}")
    @CsvSource({"5 + 3, 8",
                "4 - 8, -4",
                "3.2 * 4, 12.8",
                "10.5 / 2, 5.25",
                "(1 + 1)^0, 1",
                "(3*(1 + 1))^2, 36",
                "(-5)^1, -5"})
    void testCalculate(String formula, double expectedAnswer)
        throws InvalidFormulaException, DivisionByZeroException {
        double testAnswer = calculationService.calculate(formula);
        assertThat(expectedAnswer, closeTo(testAnswer, 0.0000001));
    }

    @ParameterizedTest(name="Тест метода CalculationService.calculate({0}) с исключением InvalidFormulaException")
    @ValueSource(strings = {"5!",
            "3+",
            "s_",
            "((()))",
            "((1+1)^3 - 4"})
    void testCalculate_shouldBeInvalidFormulaException(String formula){
        assertThrows(InvalidFormulaException.class, ()->calculationService.calculate(formula));
    }

    @ParameterizedTest(name="Тест метода CalculationService.calculate({0}) c исключением DivisionByZeroException")
    @ValueSource(strings = {"5/0",
                            "5/(5-5)"})
    void testCalculate_shouldBeDivisionByZeroException(String formula){
        assertThrows(DivisionByZeroException.class, ()->calculationService.calculate(formula));
    }
}
