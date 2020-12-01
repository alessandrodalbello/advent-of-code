package org.adb.adventofcode.aoc2020;

import java.util.ArrayList;
import java.util.List;

import org.adb.adventofcode.io.FileResourceReader;

public class AocDay1 {

    private static final String INPUT_FILENAME = "aoc_day1.txt";

    public static void main(String[] args) {
        AocDay1.solveSilver();
        AocDay1.solveGold();
    }

    private static void solveSilver() {
        System.out.println("*** Silver star ***");
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            List<Integer> expenses = reader.asIntStream()
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            for (int i = 0; i < expenses.size() - 1; i++) {
                int a = expenses.get(i);
                for (int j = i + 1; j < expenses.size(); j++) {
                    int b = expenses.get(j);
                    if (a + b == 2020) {
                        int prod = a * b;
                        System.out.printf("%d * %d -> %d.%n", a, b, prod);
                    }
                }
            }
        }
    }

    private static void solveGold() {
        System.out.println("*** Gold star ***");
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            List<Integer> expenses = reader.asIntStream()
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            for (int i = 0; i < expenses.size() - 2; i++) {
                int a = expenses.get(i);
                for (int j = i + 1; j < expenses.size() - 1; j++) {
                    int b = expenses.get(j);
                    for (int k = j + 1; k < expenses.size(); k++) {
                        int c = expenses.get(k);
                        if (a + b + c == 2020) {
                            int prod = a * b * c;
                            System.out.printf("%d * %d * %d -> %d.%n", a, b, c, prod);
                        }
                    }
                }
            }
        }
    }
}
