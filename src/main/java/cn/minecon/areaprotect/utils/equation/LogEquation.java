package cn.minecon.areaprotect.utils.equation;

import java.util.Map;

public class LogEquation extends Equation {
    private final Equation inside;

    public LogEquation(Equation pollLast) {
        this.inside = pollLast;
    }

    @Override
    public double calculate(Map<String, Double> variables) {
        return Math.log10(inside.calculate(variables));
    }

    public String toString() {
        return "log(" + inside + ")";
    }
}
