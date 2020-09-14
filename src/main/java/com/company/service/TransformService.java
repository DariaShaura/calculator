package com.company.service;

import com.company.entity.Formula;
import com.company.exception.InvalidFormulaException;

public interface TransformService {
    void transformToRPNView(Formula formula)
            throws InvalidFormulaException;


}
