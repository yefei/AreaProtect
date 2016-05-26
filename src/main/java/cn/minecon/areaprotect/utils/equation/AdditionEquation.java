package cn.minecon.areaprotect.utils.equation;

import java.util.Map;

public class AdditionEquation extends Equation {
    private final Equation left;
    private final Equation right;

    public AdditionEquation(Equation left, Equation right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public double calculate(Map<String, Double> variables) {
        return left.calculate(variables) + right.calculate(variables);
    }

    public String toString() {
        return "(" + left.toString() + " + " + right.toString() + ")";
    }
}
