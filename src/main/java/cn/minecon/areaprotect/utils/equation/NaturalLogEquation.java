package cn.minecon.areaprotect.utils.equation;

import java.util.Map;

public class NaturalLogEquation extends Equation {
    private final Equation inside;

    public NaturalLogEquation(Equation pollLast) {
        this.inside = pollLast;
    }

    @Override
    public double calculate(Map<String, Double> variables) {
        return Math.log(inside.calculate(variables));
    }

    public String toString() {
        return "ln(" + inside + ")";
    }
}
