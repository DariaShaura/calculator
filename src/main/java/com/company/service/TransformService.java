package com.company.service;

import com.company.exception.InvalidFormulaException;

import java.util.Deque;

public interface TransformService {
    Deque<Object> transformToRPNView(String formula)
            throws InvalidFormulaException;


}
