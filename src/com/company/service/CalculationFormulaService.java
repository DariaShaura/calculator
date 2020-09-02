package com.company.service;

import com.company.dto.InputOutputFormula;
import com.company.entity.Formula;
import com.company.exception.DivisionByZeroException;
import com.company.exception.InvalidFormulaException;

import java.util.LinkedList;
import java.util.ListIterator;

public class CalculationFormulaService {
    private Formula formula;

    public CalculationFormulaService() {
    }

    public void calculator(){
        try {
            while (true) {
                String inputFormula = InputOutputFormula.inputFormula();

                if (inputFormula.equals("q")) {
                    System.out.println("Завершение работы калькулятора");
                    return;
                }

                 try {
                     if (ValidationFormulaService.IsCharactersValid(inputFormula)) {
                         formula = new Formula(inputFormula.replaceAll(" ", ""));

                         TransformToService.toRPN(formula);

                         System.out.println("Result = " + calculateFromRPN());
                     }
                     else{
                         throw  new InvalidFormulaException("Недопустимая запись формулы!");
                     }
                 }
                 catch (InvalidFormulaException | IllegalArgumentException | DivisionByZeroException e){
                 System.out.println(e.getMessage());
                 }

            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            InputOutputFormula.terminateInput();
        }
    }

    private double calculateFromRPN()
            throws DivisionByZeroException {

        LinkedList<Object> rpnFormula = new LinkedList<>(formula.getRpnFormula());

        if(rpnFormula.size() == 1){
            return (Double) rpnFormula.get(0);
        }

        double calculationResult = 0;

        while(rpnFormula.size() > 1){
            int i=0;
            while(rpnFormula.get(i) instanceof Double){
                i++;
            }

            EnumClassOperator operator = (EnumClassOperator)rpnFormula.get(i);
            switch (operator.getOperationType()){
                case BIN_OPERATION:
                    calculationResult = operator.calculate((Double) rpnFormula.get(i-2), (Double) rpnFormula.get(i-1));
                    rpnFormula.set(i-2, calculationResult);
                    rpnFormula.remove(i-1);
                    rpnFormula.remove(i-1);
                    break;
                case PREFIX_OPERATION:
                    calculationResult = operator.calculate((Double) rpnFormula.get(i-1));
                    rpnFormula.set(i-1, calculationResult);
                    rpnFormula.remove(i);
            }
        }

        return calculationResult;
    }
}
