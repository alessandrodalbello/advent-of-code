package org.adb.adventofcode.aoc2020;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.adb.adventofcode.Solver;
import org.adb.adventofcode.io.FileResourceReader;

class Day5 implements Solver {

    private static final String INPUT_FILENAME = "aoc_day5.txt";

    private final List<Seat> seats;

    public Day5() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            seats = reader.asStream(this::parseSeat)
                    .sorted(Comparator.comparingInt(s -> s.id))
                    .collect(Collectors.toList());
        }
    }

    private Seat parseSeat(String boardingPass) {
        char[] rowChars = Arrays.copyOfRange(boardingPass.toCharArray(), 0, 7);
        int row = binarySearchBoardingPass(rowChars, 'F');
        char[] colChars = Arrays.copyOfRange(boardingPass.toCharArray(), 7, 10);
        int col = binarySearchBoardingPass(colChars, 'L');
        int id = row * 8 + col;
        return new Seat(id);
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
        Seat lastSeat = seats.get(seats.size() - 1);
        System.out.printf("The highest seat ID in the plane is %d.%n", lastSeat.id);
    }

    @Override
    public void solveGold() {
        Seat seatAfterMissing = null;
        int i = 1;
        while (seatAfterMissing == null) {
            if (seats.get(i).id - seats.get(i - 1).id == 2) {
                seatAfterMissing = seats.get(i);
            }
            i += 1;
        }
        System.out.printf("The missing seat ID is %d.%n", seatAfterMissing.id - 1);
    }

    private static class Seat {
        private final int id;

        private Seat(int id) {
            this.id = id;
        }
    }
}
