package com.company.service;

import com.company.exception.DivisionByZeroException;
import com.company.exception.InvalidFormulaException;

public interface CalculationService{
    double calculate(String formula)
            throws InvalidFormulaException, DivisionByZeroException;
}
