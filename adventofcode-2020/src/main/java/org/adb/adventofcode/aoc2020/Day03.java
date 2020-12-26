package org.adb.adventofcode.aoc2020;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day03 implements Day {

    private static final String INPUT_FILENAME = "input_day3.txt";

    private final String[] lines;
    private final int lineLength;

    public Day03() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            lines = reader.asLines().toArray(String[]::new);
        }
        lineLength = lines[0].length();
    }

    @Override
    public String title() {
        return "Toboggan Trajectory";
    }

    @Override
    public String solveSilver() {
        long trees = countTrees(3, 1);
        return String.format("Trees counted using slope (r3, d1): %d", trees);
    }

    @Override
    public String solveGold() {
        long trees11 = countTrees(1, 1);
        long trees13 = countTrees(3, 1);
        long trees15 = countTrees(5, 1);
        long trees17 = countTrees(7, 1);
        long trees21 = countTrees(1, 2);
        long prod = trees11 * trees13 * trees15 * trees17 * trees21;
        return String.format("The product of the slopes is %d", prod);
    }

    private long countTrees(int right, int down) {
        long trees = 0;
        for (int i = down, j = right; i < lines.length; i += down, j += right) {
            if (lines[i].charAt(j % lineLength) == '#') {
                trees += 1;
            }
        }
        return trees;
    }
}
