package org.adb.adventofcode.aoc2020;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.adb.adventofcode.Solver;
import org.adb.adventofcode.io.FileResourceReader;

class Day24 implements Solver {

    private static final String INPUT_FILENAME = "aoc_day24.txt";
    private static final int HEXGRID_SIZE = 131;

    private final List<String> tileSequences;
    private final byte[][] hexgrid;

    public Day24() {
        hexgrid = new byte[HEXGRID_SIZE][HEXGRID_SIZE];
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            tileSequences = reader.asLines().collect(Collectors.toList());
        }
    }

    @Override
    public void solveSilver() {
        completeFloor();
        int blackTiles = countBlackTiles();
        System.out.printf("There are %d black tiles after the floor is completed.%n", blackTiles);
    }

    private void completeFloor() {
        for (String sequence : tileSequences) {
            int c = 0;
            int i = HEXGRID_SIZE / 2;
            int j = HEXGRID_SIZE / 2;
            while (c < sequence.length()) {
                if (sequence.charAt(c) == 'e') {
                    j += 1;
                } else if (sequence.charAt(c) == 'w') {
                    j -= 1;
                } else if (sequence.charAt(c) == 's') {
                    c += 1;
                    if (i % 2 == 0 && sequence.charAt(c) == 'w') {
                        j -= 1;
                    } else if (i % 2 != 0 && sequence.charAt(c) == 'e') {
                        j += 1;
                    }
                    i += 1;
                } else if (sequence.charAt(c) == 'n') {
                    c += 1;
                    if (i % 2 == 0 && sequence.charAt(c) == 'w') {
                        j -= 1;
                    } else if (i % 2 != 0 && sequence.charAt(c) == 'e') {
                        j += 1;
                    }
                    i -= 1;
                }
                c += 1;
            }
            hexgrid[i][j] ^= 1;
        }
    }

    @Override
    public void solveGold() {
        for (int n = 1; n <= 100; n++) {
            flipTiles();
        }
        int blackTiles = countBlackTiles();
        System.out.printf("There are %d black tiles after 100 days of art exhibition.%n", blackTiles);
    }

    private void flipTiles() {
        final byte[][] newHexgrid = new byte[HEXGRID_SIZE][HEXGRID_SIZE];
        for (int i = 0; i < HEXGRID_SIZE; i++) {
            newHexgrid[i] = Arrays.copyOf(hexgrid[i], HEXGRID_SIZE);
        }

        for (int i = 1; i < HEXGRID_SIZE - 1; i++) {
            for (int j = 1; j < HEXGRID_SIZE - 1; j++) {
                int adjacentBlacks = countAdjacentBlacks(i, j, hexgrid);
                if (hexgrid[i][j] == 1 && (adjacentBlacks == 0 || adjacentBlacks > 2)) {
                    newHexgrid[i][j] = 0;
                } else if (hexgrid[i][j] == 0 && adjacentBlacks == 2) {
                    newHexgrid[i][j] = 1;
                }
            }
        }

        for (int i = 0; i < HEXGRID_SIZE; i++) {
            hexgrid[i] = Arrays.copyOf(newHexgrid[i], HEXGRID_SIZE);
        }
    }

    private int countAdjacentBlacks(int i, int j, byte[][] hexgrid) {
        int blacks = 0;
        if (hexgrid[i][j - 1] == 1) {
            blacks += 1;
        }
        if (hexgrid[i][j + 1] == 1) {
            blacks += 1;
        }
        if (hexgrid[i - 1][j] == 1) {
            blacks += 1;
        }
        if (i % 2 == 0 && hexgrid[i - 1][j - 1] == 1) {
            blacks += 1;
        } else if (i % 2 != 0 && hexgrid[i - 1][j + 1] == 1) {
            blacks += 1;
        }
        if (hexgrid[i + 1][j] == 1) {
            blacks += 1;
        }
        if (i % 2 == 0 && hexgrid[i + 1][j - 1] == 1) {
            blacks += 1;
        } else if (i % 2 != 0 && hexgrid[i + 1][j + 1] == 1) {
            blacks += 1;
        }
        return blacks;
    }

    private int countBlackTiles() {
        int count = 0;
        for (byte[] bytes : hexgrid) {
            for (int j = 0; j < hexgrid.length; j++) {
                if (bytes[j] == 1) {
                    count += 1;
                }
            }
        }
        return count;
    }
}
