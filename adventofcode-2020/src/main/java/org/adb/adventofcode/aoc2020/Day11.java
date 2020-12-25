package org.adb.adventofcode.aoc2020;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day11 implements Day {

    private static final String INPUT_FILENAME = "input_day11.txt";

    private final char[][] seats;

    public Day11() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            seats = reader.parseLines(String::toCharArray).toArray(char[][]::new);
        }
    }

    @Override
    public void solveSilver() {
        char[][] stableSeats = doOnboardAirplaneSilver(seats);
        int occupied = countOccupied(stableSeats);
        System.out.printf("After all people have sat down based on silver rules, %d are occupied.%n", occupied);
    }

    private char[][] doOnboardAirplaneSilver(char[][] seats) {
        char[][] seatsCopy = new char[seats.length][];
        for (int i = 0; i < seats.length; i++) {
            seatsCopy[i] = new char[seats[i].length];
            System.arraycopy(seats[i], 0, seatsCopy[i], 0, seats[i].length);
        }
        boolean changed = false;
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                char s = seats[i][j];
                if (s == 'L' || s == '#') {
                    int occupied = countOccupiedAdjacentSilver(seats, i, j);
                    if (s == 'L' && occupied == 0) {
                        seatsCopy[i][j] = '#';
                        changed = true;
                    } else if (s == '#' && occupied >= 4) {
                        seatsCopy[i][j] = 'L';
                        changed = true;
                    }
                }
            }
        }
        return changed ? doOnboardAirplaneSilver(seatsCopy) : seats;
    }

    private int countOccupiedAdjacentSilver(char[][] seats, int posI, int posJ) {
        int occupied = 0;
        for (int i = posI - 1; i <= posI + 1; i++) {
            for (int j = posJ - 1; j <= posJ + 1; j++) {
                if (i >= 0 && j >= 0 && i < seats.length && j < seats[i].length
                        && (i != posI || j != posJ)
                        && seats[i][j] == '#') {
                    occupied += 1;
                }
            }
        }
        return occupied;
    }

    @Override
    public void solveGold() {
        char[][] stableSeats = doOnboardAirplaneGold(seats);
        int occupied = countOccupied(stableSeats);
        System.out.printf("After all people have sat down based on gold rules, %d are occupied.%n", occupied);
    }

    private char[][] doOnboardAirplaneGold(char[][] seats) {
        char[][] seatsCopy = new char[seats.length][];
        for (int i = 0; i < seats.length; i++) {
            seatsCopy[i] = new char[seats[i].length];
            System.arraycopy(seats[i], 0, seatsCopy[i], 0, seats[i].length);
        }
        boolean changed = false;
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                char s = seats[i][j];
                if (s == 'L' || s == '#') {
                    int occupied = countOccupiedAdjacentGold(seats, i, j);
                    if (s == 'L' && occupied == 0) {
                        seatsCopy[i][j] = '#';
                        changed = true;
                    } else if (s == '#' && occupied >= 5) {
                        seatsCopy[i][j] = 'L';
                        changed = true;
                    }
                }
            }
        }
        return changed ? doOnboardAirplaneGold(seatsCopy) : seats;
    }

    private int countOccupiedAdjacentGold(char[][] seats, int posI, int posJ) {
        int occupied = 0;
        occupied += isAdjacentOccupied(seats, posI, -1, posJ, 0) ? 1 : 0;
        occupied += isAdjacentOccupied(seats, posI, 1, posJ, 0) ? 1 : 0;
        occupied += isAdjacentOccupied(seats, posI, 0, posJ, -1) ? 1 : 0;
        occupied += isAdjacentOccupied(seats, posI, 0, posJ, 1) ? 1 : 0;
        occupied += isAdjacentOccupied(seats, posI, -1, posJ, -1) ? 1 : 0;
        occupied += isAdjacentOccupied(seats, posI, 1, posJ, -1) ? 1 : 0;
        occupied += isAdjacentOccupied(seats, posI, -1, posJ, 1) ? 1 : 0;
        occupied += isAdjacentOccupied(seats, posI, 1, posJ, 1) ? 1 : 0;
        return occupied;
    }

    private boolean isAdjacentOccupied(char[][] seats, int posI, int deltaI, int posJ, int deltaJ) {
        int i = posI + deltaI;
        int j = posJ + deltaJ;
        while (i >= 0 && j >= 0 && i < seats.length && j < seats[i].length) {
            if (seats[i][j] == '#') {
                return true;
            } else if (seats[i][j] == 'L') {
                return false;
            }
            i += deltaI;
            j += deltaJ;
        }
        return false;
    }

    private int countOccupied(char[][] seats) {
        int occupied = 0;
        for (char[] seatLine : seats) {
            for (char seat : seatLine) {
                if (seat == '#') {
                    occupied += 1;
                }
            }
        }
        return occupied;
    }
}
