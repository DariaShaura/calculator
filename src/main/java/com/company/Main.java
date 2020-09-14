package com.company;

import com.company.dto.InputOutputFormula;
import com.company.exception.DivisionByZeroException;
import com.company.exception.InvalidFormulaException;
import com.company.service.CalculationFormulaService;

public class Main {

    public static void main(String[] args) {

        CalculationFormulaService calculationFormulaService = new CalculationFormulaService();
        double result;

        try {
            while (true) {
                String inputFormula = InputOutputFormula.inputFormula();

                if (inputFormula.equals("q")) {
                    System.out.println("Завершение работы калькулятора");
                    return;
                }
                try {
                    result = calculationFormulaService.calculate(inputFormula);
                    InputOutputFormula.printFormulaResult(result);
                }
                catch (InvalidFormulaException | DivisionByZeroException e){
                    System.out.println(e.getMessage());
                }

            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            InputOutputFormula.terminateInput();
        }
    }
}
