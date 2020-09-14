package com.company.service;

import com.company.exception.InvalidFormulaException;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationFormulaService implements ValidationService {

    private static final Set<Character> operations;

    static {
        operations = Stream.of('+', '-', '*', '/', '^', '(', ')')
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public CharacterType getCharacterType(CharacterType previousType, char ch)
            throws InvalidFormulaException {

        if (Character.isDigit(ch)) {
            if(previousType ==  CharacterType.DOT_NUMBER) {
                return previousType;
            }
            else {
                return CharacterType.NUMBER;
            }
        } else if (ch == '.') {
            if(previousType != CharacterType.DOT_NUMBER) {
                return CharacterType.DOT_NUMBER;
            }
            else{
                throw new InvalidFormulaException("Недопустимая запись формулы!");
            }
        } else if(ch == '('){
            return CharacterType.LEFT_BRACKET_OPERATION;
        }
        else if (ch == ')'){
            return CharacterType.RIGHT_BRACKET_OPERATION;
        }
        else if((ch == '-') && ((previousType == null) || (previousType == CharacterType.LEFT_BRACKET_OPERATION))){
            return CharacterType.PREFIX_OPERATION;
        }
        else{
            return CharacterType.BIN_OPERATION;
        }
    }

    @Override
    public EnumSet<CharacterType>  getExpectedCharacterTypeSet(CharacterType characterType){

        if(characterType == null) {
            return EnumSet.of(CharacterType.NUMBER, CharacterType.PREFIX_OPERATION, CharacterType.LEFT_BRACKET_OPERATION);
        }
        else {
            switch (characterType) {
                case NUMBER:
                    return EnumSet.of(CharacterType.NUMBER, CharacterType.DOT_NUMBER, CharacterType.RIGHT_BRACKET_OPERATION, CharacterType.BIN_OPERATION, CharacterType.EMPTY);
                case DOT_NUMBER:
                    return EnumSet.of(CharacterType.DOT_NUMBER, CharacterType.BIN_OPERATION, CharacterType.RIGHT_BRACKET_OPERATION, CharacterType.EMPTY);
                case BIN_OPERATION:
                case PREFIX_OPERATION:
                    return EnumSet.of(CharacterType.NUMBER, CharacterType.LEFT_BRACKET_OPERATION);
                case LEFT_BRACKET_OPERATION:
                    return EnumSet.of(CharacterType.NUMBER, CharacterType.LEFT_BRACKET_OPERATION, CharacterType.PREFIX_OPERATION);
                case RIGHT_BRACKET_OPERATION:
                    return EnumSet.of(CharacterType.BIN_OPERATION, CharacterType.RIGHT_BRACKET_OPERATION, CharacterType.EMPTY);
            }
        }

        return null;
    }

    @Override
    public boolean isNextCharacterInExpectedCharacterTypeSet(EnumSet<CharacterType> expectedCharacterTypeSet, CharacterType characterType){
        return expectedCharacterTypeSet.contains(characterType);
    }

    @Override
    public boolean isFormulaContainsOnlyValidCharacters(String formula)
    {
        if(formula.isEmpty()){
            return false;
        }
        formula = formula.replaceAll("[ \\d\\.]", "");
        for(Character operation: operations){
            formula = formula.replace(operation.toString(), "");
        }

        return formula.isEmpty();
    }
}
