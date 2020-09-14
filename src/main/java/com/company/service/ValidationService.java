package com.company.service;

import com.company.exception.InvalidFormulaException;

import java.util.EnumSet;

public interface ValidationService {
    CharacterType getCharacterType(CharacterType previousType, char ch)
            throws InvalidFormulaException;

    EnumSet<CharacterType> getExpectedCharacterTypeSet(CharacterType characterType);

    boolean IsNextCharacterInExpectedCharacterTypeSet(EnumSet<CharacterType> expectedCharacterTypeSet, CharacterType characterType);

    boolean IsFormulaContainsOnlyValidCharacters(String formula);
}
