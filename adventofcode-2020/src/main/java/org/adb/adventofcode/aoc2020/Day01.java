package org.adb.adventofcode.aoc2020;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day01 implements Day {

    private static final String INPUT_FILENAME = "input_day1.txt";
    private static final int TARGET = 2020;

    private final int[] expenses;

    public Day01() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            expenses = reader.asIntStream().toArray();
        }
    }

    @Override
    public void solveSilver() {
        for (int i = 0, length = expenses.length; i < length - 1; i++) {
            final int a = expenses[i];
            if (a > TARGET) continue;
            for (int j = i + 1; j < length; j++) {
                final int b = expenses[j];
                if (a + b == TARGET) {
                    int prod = a * b;
                    System.out.printf("%d * %d -> %d%n", a, b, prod);
                    return;
                }
            }
        }
    }

    @Override
    public void solveGold() {
        for (int i = 0, length = expenses.length; i < length - 2; i++) {
            final int a = expenses[i];
            if (a > TARGET) continue;
            for (int j = i + 1; j < length - 1; j++) {
                final int b = expenses[j];
                if (a + b > TARGET) continue;
                for (int k = j + 1; k < length; k++) {
                    final int c = expenses[k];
                    if (a + b + c == TARGET) {
                        int prod = a * b * c;
                        System.out.printf("%d * %d * %d -> %d%n", a, b, c, prod);
                        return;
                    }
                }
            }
        }
    }
}
