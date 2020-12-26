package org.adb.adventofcode.aoc2020;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day05 implements Day {

    private static final String INPUT_FILENAME = "input_day5.txt";

    private final List<Integer> seatIds;

    public Day05() {
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
    public String title() {
        return "Binary Boarding";
    }

    @Override
    public String solveSilver() {
        int lastSeatId = seatIds.get(seatIds.size() - 1);
        return String.format("The highest seat ID in the plane is %d.", lastSeatId);
    }

    @Override
    public String solveGold() {
        int missingSeatId = -1;
        int i = 1;
        while (missingSeatId < 0) {
            if (seatIds.get(i) - seatIds.get(i - 1) == 2) {
                missingSeatId = seatIds.get(i) - 1;
            }
            i += 1;
        }
        return String.format("The missing seat ID is %d.", missingSeatId);
    }
}
