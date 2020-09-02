package com.company.service;

import com.company.entity.Formula;
import com.company.exception.InvalidFormulaException;

import java.util.*;

public class TransformToService {

    public static CharacterType getCharacterType(CharacterType previousType, char ch)
            throws InvalidFormulaException{

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

    public static EnumClassOperator getOperator(CharacterType characterType, char ch)
                    throws InvalidFormulaException {
        for(EnumClassOperator operator: EnumClassOperator.values()){
            if((operator.getOperatorCharacter() == ch) && (operator.getOperationType() == characterType)){
                return operator;
            }
        }

        throw new InvalidFormulaException("Недопустимая запись формулы!!!");
    }

    public static void toRPN(Formula formula)
            throws InvalidFormulaException {
        Deque<EnumClassOperator> stack = new ArrayDeque<>();
        StringBuilder number = new StringBuilder();
        Deque<Object> rpnFormula = new LinkedList<>();
        EnumSet<CharacterType> expectedCharacterTypeSet;

        CharacterType previousType = null;
        EnumClassOperator operator;

        expectedCharacterTypeSet = ValidationFormulaService.getExpectedCharacterTypeSet(previousType);

        for (int i = 0, length = formula.getInputFormula().length(); i < length; i++) {

            char ch = formula.getInputFormula().charAt(i);

            CharacterType characterType = getCharacterType(previousType, ch);
            previousType = characterType;

            if(!ValidationFormulaService.IsNextCharacterValid(expectedCharacterTypeSet, characterType)){
                throw  new InvalidFormulaException("Недопустимая запись формулы!");
            }

            expectedCharacterTypeSet = ValidationFormulaService.getExpectedCharacterTypeSet(previousType);

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
                    while (!stack.isEmpty() && (stack.getLast().getPriority().compareTo(operator.getPriority()) > 0)) {
                        rpnFormula.addLast(stack.pollLast());
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

        formula.setRpnFormula(rpnFormula);
    }

}
