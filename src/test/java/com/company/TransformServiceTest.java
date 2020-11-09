package com.company;

import com.company.exception.InvalidFormulaException;
import com.company.service.TransformToService;
import com.company.service.ValidationFormulaService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Deque;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("TransformToRPNView")
public class TransformServiceTest {

    private static TransformToService transformService;
    private static ValidationFormulaService validationService;

    @BeforeAll
    static void setup(){
        validationService = new ValidationFormulaService();
        transformService = new TransformToService(validationService);
    }

    public static class DequeConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
                return Arrays.stream(String.valueOf(source).split(";"))
                        .map(p -> {
                            try {
                                return !Character.isDigit(p.charAt(0)) ?
                                        transformService.getOperator(validationService.getCharacterType(null, p.charAt(0)), p.charAt(0))
                                        : Double.valueOf(p.toString());
                            } catch (InvalidFormulaException e) {
                                e.printStackTrace();
                            }
                            return null;
                        })
                        .toArray();//collect(Collectors.toCollection(ArrayDeque::new));
        }
    }

    @ParameterizedTest(name="Тест метода transformToRPNView({0})")
    @CsvSource({"(1+2), 1;2;+",
            "-(4+3)^5, 4;3;+;-;5;^",
                "4*(1+2)/3, 4;1;2;+;*;3;/",
                "-3.1+7^5, 3.1;-;7;5;^;+"})
    void testTransformToRPNView(String formula, @ConvertWith(DequeConverter.class) Object[] expectedRPN)
            throws InvalidFormulaException {
        Deque<Object> testDeque = transformService.transformToRPNView(formula);
        assertThat(expectedRPN, arrayContaining(testDeque.toArray()));
    }

    @ParameterizedTest(name="Тест метода transformToRPNView({0}) с исключением InvalidFormulaException")
    @CsvSource({"((1+2)",
            "1(4+3)^5",
            "4*(1..+2)/3",
            "-3.1()+7^5"})
    void testTransformToRPNView_shouldBeException(String formula){
        assertThrows(InvalidFormulaException.class, () -> transformService.transformToRPNView(formula));
    }
}
