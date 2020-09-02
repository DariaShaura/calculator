package com.company;

import com.company.entity.Formula;
import com.company.service.CalculationFormulaService;

import java.util.Deque;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

        CalculationFormulaService calculationFormulaService = new CalculationFormulaService();

        calculationFormulaService.calculator();
    }
}
