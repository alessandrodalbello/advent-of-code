package org.adb.adventofcode.aoc2020;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day18 implements Day {

    private static final String INPUT_FILENAME = "input_day18.txt";
    private static final Pattern PATTER_SIMPLIFY_EXPRESSION = Pattern.compile("\\(([^\\(\\)]+)\\)");
    private static final Pattern PATTER_SOLVE_SILVER = Pattern.compile("(\\b\\d+)([\\+\\*])(\\d+\\b)");
    private static final Pattern PATTER_SOLVE_GOLD = Pattern.compile("(\\b\\d+)\\+(\\d+\\b)");

    private final List<String> expressions;

    public Day18() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            expressions = reader.parseLines(s -> s.replaceAll("\\s", "")).collect(Collectors.toList());
        }
    }

    @Override
    public String title() {
        return "Operation Order";
    }

    @Override
    public String solveSilver() {
        long total = 0;
        for (String expression : expressions) {
            long value = evaluateExpression(expression, this::solveSilverExpression);
            total += value;
        }
        return String.format("The sum of homeworks with equal precedence is %d.", total);
    }

    private String solveSilverExpression(String expression) {
        Matcher matcher = PATTER_SOLVE_SILVER.matcher(expression);
        if (matcher.find()) {
            long operand1 = Long.parseLong(matcher.group(1));
            String operator = matcher.group(2);
            long operand2 = Long.parseLong(matcher.group(3));
            long result = "+".equals(operator) ? operand1 + operand2 : operand1 * operand2;
            String simplifiedExpression = matcher.replaceFirst(String.valueOf(result));
            return solveSilverExpression(simplifiedExpression);
        }
        return expression;
    }

    @Override
    public String solveGold() {
        long total = 0;
        for (String expression : expressions) {
            total += evaluateExpression(expression, this::solveGoldExpression);
        }
        return String.format("The sum of homeworks with different precedence is %d.", total);
    }

    private String solveGoldExpression(String expression) {
        Matcher matcher = PATTER_SOLVE_GOLD.matcher(expression);
        if (matcher.find()) {
            long operand1 = Long.parseLong(matcher.group(1));
            long operand2 = Long.parseLong(matcher.group(2));
            long result = operand1 + operand2;
            String simplifiedExpression = matcher.replaceFirst(String.valueOf(result));
            return solveGoldExpression(simplifiedExpression);
        } else {
            String[] operands = expression.split("\\*");
            long result = Arrays.stream(operands).mapToLong(Long::parseLong).reduce(1L, (a, b) -> a * b);
            return String.valueOf(result);
        }
    }

    private long evaluateExpression(String expression, Function<String, String> solver) {
        if (expression.matches("\\d+")) {
            return Long.parseLong(expression);
        } else {
            String simplifiedExpression = simplifyExpression(expression, solver);
            return evaluateExpression(simplifiedExpression, solver);
        }
    }

    private String simplifyExpression(String expression, Function<String, String> solver) {
        Matcher matcher = PATTER_SIMPLIFY_EXPRESSION.matcher(expression);
        if (matcher.find()) {
            String matchedExpression = matcher.group(0);
            String subExpression = matcher.group(1);
            String subExpressionResult = solver.apply(subExpression);
            return expression.replace(matchedExpression, subExpressionResult);
        }
        return solver.apply(expression);
    }
}
