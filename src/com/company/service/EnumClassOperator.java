package com.company.service;

import com.company.exception.DivisionByZeroException;

public enum EnumClassOperator{

    ADD('+', OperatorPriority.LOW, CharacterType.BIN_OPERATION){
        public double calculate(double... operands) {
            return operands[0] + operands[1];
        }
    },
    SUBTRACT('-', OperatorPriority.LOW, CharacterType.BIN_OPERATION){
        public double calculate(double... operands) {
            return operands[0] - operands[1];
        }
    },
    MULTIPLY('*', OperatorPriority.MIDDLE, CharacterType.BIN_OPERATION){
        public double calculate(double... operands) {
            return operands[0] * operands[1];
        }
    },
    DIVISION('/', OperatorPriority.MIDDLE, CharacterType.BIN_OPERATION){
        public double calculate(double... operands)
                throws DivisionByZeroException {
            if(operands[1] == 0) {
                throw new DivisionByZeroException("Деление на ноль");
            }
            return operands[0] / operands[1];
        }
    },
    POWER('^', OperatorPriority.HIGH, CharacterType.BIN_OPERATION){
        public double calculate(double... operands) {
            return Math.pow(operands[0], operands[1]);
        }
    },
    NEGATION('-', OperatorPriority.LOW, CharacterType.PREFIX_OPERATION){
        public double calculate(double... operands) {
            return -operands[0];
        }
    },
    LEFT_BRACKET('(', OperatorPriority.LOWEST, CharacterType.LEFT_BRACKET_OPERATION){
        public double calculate(double... operands) {
            throw new IllegalStateException("Недопустимое вычисление!!!");
        }
    }
    ;

    private char operatorCharacter;
    private OperatorPriority priority;
    private CharacterType operationType;

    EnumClassOperator(char operatorCharacter, OperatorPriority priority, CharacterType operationType) {
        this.operatorCharacter = operatorCharacter;
        this.priority = priority;
        this.operationType = operationType;
    }

    public abstract double calculate(double... operands) throws DivisionByZeroException;

    public char getOperatorCharacter() {
        return operatorCharacter;
    }

    public OperatorPriority getPriority() {
        return priority;
    }

    public CharacterType getOperationType() {
        return operationType;
    }
}
