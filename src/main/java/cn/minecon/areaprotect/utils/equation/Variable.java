package cn.minecon.areaprotect.utils.equation;

import java.util.Map;

public class Variable extends Equation {
    private final String token;

    public Variable(String token) {
        this.token = token;
    }

    @Override
    public double calculate(Map<String, Double> variables) {
        return variables.get(token);
    }

    public String toString() {
        return token;
    }
}
