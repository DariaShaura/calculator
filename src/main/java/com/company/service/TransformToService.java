package com.company.service;

import com.company.exception.InvalidFormulaException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.LinkedList;

public class TransformToService implements TransformService {

    ValidationFormulaService validationService;

    public TransformToService(ValidationFormulaService validationService) {
        this.validationService = validationService;
    }

    public EnumClassOperator getOperator(CharacterType characterType, char ch)
                    throws InvalidFormulaException {
        for(EnumClassOperator operator: EnumClassOperator.values()){
            if((operator.getOperatorCharacter() == ch) && (operator.getOperationType() == characterType)){
                return operator;
            }
        }

        throw new InvalidFormulaException("Недопустимая запись формулы!!!");
    }

    @Override
    public Deque<Object> transformToRPNView(String formula)
            throws InvalidFormulaException {
        if (!validationService.isFormulaContainsOnlyValidCharacters(formula)) {
            throw new InvalidFormulaException("Недопустимая запись формулы!");
        }

        formula = formula.replaceAll(" ", "");

        Deque<EnumClassOperator> stack = new ArrayDeque<>();
        StringBuilder number = new StringBuilder();
        Deque<Object> rpnFormula = new LinkedList<>();
        EnumSet<CharacterType> expectedCharacterTypeSet;

        CharacterType previousType = null;
        EnumClassOperator operator;

        expectedCharacterTypeSet = validationService.getExpectedCharacterTypeSet(previousType);

        for (int i = 0, length = formula.length(); i < length; i++) {

            char ch = formula.charAt(i);

            CharacterType characterType = validationService.getCharacterType(previousType, ch);
            previousType = characterType;

            if(!validationService.isNextCharacterInExpectedCharacterTypeSet(expectedCharacterTypeSet, characterType)){
                throw  new InvalidFormulaException("Недопустимая запись формулы!");
            }

            expectedCharacterTypeSet = validationService.getExpectedCharacterTypeSet(previousType);

            switch (characterType) {
                case NUMBER:
                case DOT_NUMBER:
                    number.append(ch);
                    break;
                case PREFIX_OPERATION:
                case LEFT_BRACKET_OPERATION:
                    stack.addLast(getOperator(characterType, ch));
                    break;
                case RIGHT_BRACKET_OPERATION:
                    if(number.length() > 0) {
                        rpnFormula.addLast(Double.valueOf(number.toString()));
                        number = new StringBuilder();
                    }

                    while(!stack.isEmpty() && (stack.getLast().getOperationType() != CharacterType.LEFT_BRACKET_OPERATION)){
                        rpnFormula.addLast(stack.pollLast());
                    }

                    if(stack.isEmpty()){
                        throw new InvalidFormulaException("Недопустимая запись формулы!");
                    }

                    stack.pollLast();
                    break;
                case BIN_OPERATION:
                    if(number.length() > 0) {
                        rpnFormula.addLast(Double.valueOf(number.toString()));
                        number = new StringBuilder();
                    }

                    operator = getOperator(characterType, ch);
                    EnumClassOperator lastOperatorInStack = stack.peekLast();

                    while ((lastOperatorInStack != null) &&
                            ((lastOperatorInStack.getPriority().compareTo(operator.getPriority()) >= 0) ||
                                    (lastOperatorInStack.getOperationType() == CharacterType.PREFIX_OPERATION))) {
                        rpnFormula.addLast(stack.pollLast());
                        lastOperatorInStack = stack.peekLast();
                    }

                    stack.addLast(operator);
                    break;
            }
        }

        if(!expectedCharacterTypeSet.contains(CharacterType.EMPTY) || stack.contains(EnumClassOperator.LEFT_BRACKET)){
            throw new InvalidFormulaException("Недопустимая запись формулы!");
        }

        if(number.length() > 0) {
            rpnFormula.addLast(Double.valueOf(number.toString()));
        }

        while (!stack.isEmpty()) {
            rpnFormula.addLast(stack.pollLast());
        }

        return rpnFormula;
    }

}
