package com.company;

import com.company.exception.InvalidFormulaException;
import com.company.service.CharacterType;
import com.company.service.ValidationFormulaService;
import com.company.service.ValidationService;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.DefaultArgumentConverter;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.EnumSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Validation")
public class ValidationServiceTest {
    public static class NullableConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            if ("null".equals(source)) {
                return null;
            }
            return DefaultArgumentConverter.INSTANCE.convert(source, targetType);
        }
    }

    private static ValidationService validationService;

    @BeforeAll
    static void setup(){
        validationService = new ValidationFormulaService();
    }

    @ParameterizedTest(name="Тест метода ValidationService.getCharacterType({0}, {1})")
    @CsvSource({"null, 2, NUMBER",
            "DOT_NUMBER, 5, DOT_NUMBER",
            "NUMBER, (, LEFT_BRACKET_OPERATION"})
    void testGetCharacterType(@ConvertWith(NullableConverter.class) CharacterType previousCharacterType,
                              char ch,
                              CharacterType expected) throws InvalidFormulaException {
        assertThat(expected, IsEqual.equalTo(validationService.getCharacterType(previousCharacterType ,ch)));
    }

    @ParameterizedTest(name = "Тест метода ValidationService.getCharacterType({0}, {1}) с исключением InvalidFormulaException")
    @CsvSource({"DOT_NUMBER, ."})
    void testGetCharacterType_shouldBeException(CharacterType previousCharacterType, char ch){
        assertThrows(InvalidFormulaException.class, ()->{validationService.getCharacterType(previousCharacterType, ch);});
    }

    @Test
    @DisplayName("Тест метода ValidationService.getExpectedCharacterTypeSet(CharacterType characterType)")
    void testGetExpectedCharacterTypeSet(){
        CharacterType type = CharacterType.RIGHT_BRACKET_OPERATION;
        EnumSet<CharacterType> expectedSet = EnumSet.of(CharacterType.BIN_OPERATION, CharacterType.RIGHT_BRACKET_OPERATION, CharacterType.EMPTY);
        assertThat(expectedSet, Matchers.is(validationService.getExpectedCharacterTypeSet(type)));
    }

    @ParameterizedTest(name = "Тест метода isFormulaContainsOnlyValidCharacters({0}) должен возвращать true")
    @ValueSource(strings = {"(1223)+-","(1+2)^5","-6/3","2*3+6-(4+1)"})
    void testIsFormulaContainsOnlyValidCharacters_returnsTrue(String formula){
        assertTrue(validationService.isFormulaContainsOnlyValidCharacters(formula));
    }


    @ParameterizedTest(name = "Тест метода isFormulaContainsOnlyValidCharacters({0}) должен возвращать false")
    @ValueSource(strings = {"!123","1()asd","_+&","{qwe}/{122}"})
    void testIsFormulaContainsOnlyValidCharacters_returnsFalse(String formula){
        assertFalse(validationService.isFormulaContainsOnlyValidCharacters(formula));
    }
}
