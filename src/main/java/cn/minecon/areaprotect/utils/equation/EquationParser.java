package cn.minecon.areaprotect.utils.equation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class EquationParser {
    public static Equation parse(String string) {
        LinkedList<String> stack = new LinkedList<String>();
        LinkedList<Equation> output = new LinkedList<Equation>();
        StringBuilder tokenBuilder = new StringBuilder();
        for (char c : string.toCharArray()) {
            if (c == ' ') {
                if (tokenBuilder.length() > 0) {
                    handleToken(stack, output, tokenBuilder.toString());
                    tokenBuilder = new StringBuilder();
                }
                continue;
            }
            if (isOperation(c)) {
                if (tokenBuilder.length() > 0) {
                    handleToken(stack, output, tokenBuilder.toString());
                    tokenBuilder = new StringBuilder();
                }
                int prec = getPrecidence(c);
                if (stack.size() > 0) {
                    while (true) {
                        int precStack = getPrecidence(stack.peekLast().charAt(0));
                        if (prec <= precStack) {
                            Equation b = output.pollLast();
                            Equation a = output.pollLast();
                            switch (stack.pollLast().charAt(0)) {
                                case '*':
                                    output.add(new MultiplicationEquation(a, b));
                                    break;
                                case '/':
                                    output.add(new DivisionEquation(a, b));
                                    break;
                                case '+':
                                    output.add(new AdditionEquation(a, b));
                                    break;
                                case '-':
                                    output.add(new SubtractionEquation(a, b));
                                    break;
                                case '^':
                                    output.add(new PowerEquation(a, b));
                                    break;
                            }
                            break;
                        } else {
                            break;
                        }
                    }
                }
                stack.add(String.valueOf(c));
            } else if (c == '(') {
                if (isSpecial(tokenBuilder.toString())) {
                    stack.add(tokenBuilder.toString());
                    tokenBuilder = new StringBuilder();
                } else {
                    if (tokenBuilder.length() > 0) {
                        throw new IllegalStateException("Invalid token: " + tokenBuilder.toString());
                    }
                    stack.add(String.valueOf(c));
                }
            } else if (c == ')') {
                if (tokenBuilder.length() > 0) {
                    handleToken(stack, output, tokenBuilder.toString());
                    tokenBuilder = new StringBuilder();
                }
                String last = stack.peekLast();
                while (!isSpecial(last) && !last.equalsIgnoreCase("(")) {
                    Equation b = output.pollLast();
                    Equation a = output.pollLast();
                    switch (stack.pollLast().charAt(0)) {
                        case '*':
                            output.add(new MultiplicationEquation(a, b));
                            break;
                        case '/':
                            output.add(new DivisionEquation(a, b));
                            break;
                        case '+':
                            output.add(new AdditionEquation(a, b));
                            break;
                        case '-':
                            output.add(new SubtractionEquation(a, b));
                            break;
                        case '^':
                            output.add(new PowerEquation(a, b));
                            break;
                    }
                    last = stack.peekLast();
                }
                String b = stack.pollLast();
                if (b.charAt(0) == '(') {
                    continue;
                } else {
                    switch (b.charAt(0)) {
                        case 's':
                            output.add(new SineEquation(output.pollLast()));
                            break;
                        case 'c':
                            output.add(new CosineEquation(output.pollLast()));
                            break;
                        case 't':
                            output.add(new TangentEquation(output.pollLast()));
                            break;
                        case 'l':
                            if (b.toLowerCase().charAt(1) == 'o') {
                                output.add(new LogEquation(output.pollLast()));
                            } else {
                                output.add(new NaturalLogEquation(output.pollLast()));
                            }
                            break;
                    }
                }
            } else {
                tokenBuilder.append(c);
            }
        }
        if (tokenBuilder.length() > 0) {
            handleToken(stack, output, tokenBuilder.toString());
        }
        while (stack.peekLast() != null) {
            Equation b = output.pollLast();
            Equation a = output.pollLast();
            switch (stack.pollLast().charAt(0)) {
                case '*':
                    output.add(new MultiplicationEquation(a, b));
                    break;
                case '/':
                    output.add(new DivisionEquation(a, b));
                    break;
                case '+':
                    output.add(new AdditionEquation(a, b));
                    break;
                case '-':
                    output.add(new SubtractionEquation(a, b));
                    break;
                case '^':
                    output.add(new PowerEquation(a, b));
                    break;
            }
        }
        return output.pollLast();
    }

    private static void handleToken(LinkedList<String> stack, LinkedList<Equation> output, String token) {
        if (isSpecial(token)) {
            switch (token.charAt(0)) {
                case 's':
                    output.add(new SineEquation(output.pollLast()));
                    return;
                case 'c':
                    output.add(new CosineEquation(output.pollLast()));
                    return;
                case 't':
                    output.add(new TangentEquation(output.pollLast()));
                    return;
                case 'l':
                    if (token.toLowerCase().charAt(1) == 'o') {
                        output.add(new LogEquation(output.pollLast()));
                    } else {
                        output.add(new NaturalLogEquation(output.pollLast()));
                    }
                    return;
            }
        } else if (token.equalsIgnoreCase("pi")) {
            output.add(new Constant(Math.PI));
        } else if (isNumber(token)) {
            output.add(new Constant(Double.valueOf(token)));
        } else {
            output.add(new Variable(token));
        }
    }

    private static boolean isOperation(char c) {
        return c == '*' || c == '/' || c == '+' || c == '-' || c == '^';
    }

    private static boolean isNumber(String token) {
        return token.matches("^*[0-9\\.]+$");
    }

    private static boolean isSpecial(String token) {
        return token.equalsIgnoreCase("sin") || token.equalsIgnoreCase("cos") || token.equalsIgnoreCase("tan") || token.equalsIgnoreCase("ln") || token.equalsIgnoreCase("log");
    }

    private static int getPrecidence(char c) {
        if (c == '*' || c == '/') {
            return 3;
        }
        if (c == '+' || c == '-') {
            return 2;
        }
        if (c == '^') {
            return 4;
        }
        return 0;
    }

    public static void main(String[] args) {
        String test1 = "123 + 2 + (4/2)";
        Equation test = parse(test1);
        System.out.println(test.toString());
        System.out.println(test.calculate(null));
        String test2 = "1 + sin(2) + (4/2)";
        test = parse(test2);
        System.out.println(test.toString());
        System.out.println(test.calculate(null));
        Map<String, Double> vars = new HashMap<String, Double>();
        vars.put("X", 12D);
        String test3 = "1 + sin(X) + ln(4/2)";
        test = parse(test3);
        System.out.println(test.toString());
        System.out.println(test.calculate(vars));
    }
}
