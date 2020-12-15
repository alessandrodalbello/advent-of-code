package org.adb.adventofcode.aoc2020;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.adb.adventofcode.Solver;
import org.adb.adventofcode.io.FileResourceReader;

class Day15 implements Solver {

    private static final String INPUT_FILENAME = "aoc_day15.txt";

    private final int[] startingNumbers;

    public Day15() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            startingNumbers = reader.nextIntSplit(",");
        }
    }

    @Override
    public void solveSilver() {
        int numb = getNth(2020);
        System.out.printf("The 2020th number spoken is %d.%n", numb);
    }

    @Override
    public void solveGold() {
        int numb = getNth(30000000);
        System.out.printf("The 30000000th number spoken is %d.%n", numb);
    }

    private int getNth(int n) {
        final LinkedList<Integer> numbers = new LinkedList<>();
        final Map<Integer, int[]> lastSpoken = new HashMap<>();
        for (int i = 0; i < startingNumbers.length; i++) {
            numbers.add(startingNumbers[i]);
            lastSpoken.put(startingNumbers[i], new int[]{0, i + 1});
        }
        for (int i = startingNumbers.length; i < n; i++) {
            int target = numbers.get(i - 1);
            int[] targetIndex = lastSpoken.get(target);
            int spoken = targetIndex != null && targetIndex[0] != 0 ? targetIndex[1] - targetIndex[0] : 0;
            numbers.add(spoken);

            if (lastSpoken.containsKey(spoken)) {
                int[] spokenIndex = lastSpoken.get(spoken);
                spokenIndex[0] = spokenIndex[1];
                spokenIndex[1] = i + 1;
            } else {
                lastSpoken.put(spoken, new int[]{0, i + 1});
            }
        }
        return numbers.getLast();
    }
}
