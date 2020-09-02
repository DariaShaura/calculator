package com.company.service;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationFormulaService {

    public static final Set<Character> operations;

    static {
        operations = Stream.of('+', '-', '*', '/', '^', '(', ')')
                .collect(Collectors.toCollection(HashSet::new));
    }

    public static EnumSet<CharacterType>  getExpectedCharacterTypeSet(CharacterType characterType){

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

    public static boolean IsNextCharacterValid(EnumSet<CharacterType> expectedCharacterTypeSet, CharacterType characterType){
        return expectedCharacterTypeSet.contains(characterType);
    }

    public static boolean IsCharactersValid(String formula)
    {
        formula = formula.replaceAll("[ \\d\\.]", "");
        for(Character operation: operations){
            formula = formula.replace(operation.toString(), "");
        }

        return formula.isEmpty();
    }
}
