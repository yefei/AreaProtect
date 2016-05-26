package cn.minecon.areaprotect.utils.equation;

import java.util.Map;

public class Constant extends Equation {
    private final double value;

    public Constant(double value) {
        this.value = value;;
    }

    @Override
    public double calculate(Map<String, Double> variables) {
        return value;
    }

    public String toString() {
        return "(" + value + ")";
    }
}
