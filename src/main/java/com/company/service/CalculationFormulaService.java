package com.company.service;

import com.company.entity.Formula;
import com.company.exception.DivisionByZeroException;
import com.company.exception.InvalidFormulaException;

import java.util.LinkedList;

public class CalculationFormulaService implements CalculationService {
    private Formula formula;
    private ValidationFormulaService validationService;
    private TransformToService transformToService;

    public CalculationFormulaService() {
    }

    @Override
    public double calculate(String strFormula)
            throws InvalidFormulaException, DivisionByZeroException{
        validationService = new ValidationFormulaService();
        transformToService = new TransformToService(validationService);

        formula.setRpnFormula(transformToService.transformToRPNView(formula.getInputFormula()));

        return calculateFromRPN();

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
