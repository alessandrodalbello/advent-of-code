package org.adb.adventofcode.aoc2020;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.adb.adventofcode.Solver;
import org.adb.adventofcode.io.FileResourceReader;

class Day5 implements Solver {

    private static final String INPUT_FILENAME = "aoc_day5.txt";

    private final List<Integer> seatIds;

    public Day5() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            seatIds = reader.asStream(this::parseSeat)
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    private int parseSeat(String boardingPass) {
        char[] rowChars = Arrays.copyOfRange(boardingPass.toCharArray(), 0, 7);
        int row = binarySearchBoardingPass(rowChars, 'F');
        char[] colChars = Arrays.copyOfRange(boardingPass.toCharArray(), 7, 10);
        int col = binarySearchBoardingPass(colChars, 'L');
        return row * 8 + col;
    }

    private int binarySearchBoardingPass(char[] chars, char lowerChar) {
        int l = 0, h = (int) Math.pow(2, chars.length) - 1;
        for (char c : chars) {
            int half = (h - l + 1) / 2;
            if (c == lowerChar) {
                h -= half;
            } else {
                l += half;
            }
        }
        return l;
    }

    @Override
    public void solveSilver() {
        int lastSeatId = seatIds.get(seatIds.size() - 1);
        System.out.printf("The highest seat ID in the plane is %d.%n", lastSeatId);
    }

    @Override
    public void solveGold() {
        int missingSeatId = -1;
        int i = 1;
        while (missingSeatId < 0) {
            if (seatIds.get(i) - seatIds.get(i - 1) == 2) {
                missingSeatId = seatIds.get(i) - 1;
            }
            i += 1;
        }
        System.out.printf("The missing seat ID is %d.%n", missingSeatId);
    }
}
