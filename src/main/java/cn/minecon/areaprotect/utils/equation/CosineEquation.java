package cn.minecon.areaprotect.utils.equation;

import java.util.Map;

public class CosineEquation extends Equation {
    private Equation inside;

    public CosineEquation(Equation inside) {
        this.inside = inside;
    }

    @Override
    public double calculate(Map<String, Double> variables) {
        return Math.cos(inside.calculate(variables));
    }

    public String toString() {
        return "cos(" + inside.toString() + ")";
    }
}
