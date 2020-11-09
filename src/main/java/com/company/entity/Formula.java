package com.company.entity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Formula {

    private String inputFormula;
    private Deque<Object> rpnFormula;

    public Formula(String formula) {
        this.inputFormula = formula;
    }

    public String getInputFormula() {
        return inputFormula;
    }

    public Deque<Object> getRpnFormula() {
        return rpnFormula;
    }

    public void setRpnFormula(Deque<Object> rpnFormula) {
        this.rpnFormula = rpnFormula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Formula)) return false;
        Formula formula = (Formula) o;
        return Objects.equals(getInputFormula(), formula.getInputFormula());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInputFormula());
    }
}
