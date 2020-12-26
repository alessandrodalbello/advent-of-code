package org.adb.adventofcode.aoc2020;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day17 implements Day {

    private static final String INPUT_FILENAME = "input_day17.txt";
    private static final int CYCLES = 6;

    private final char[][] initialState;

    public Day17() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            initialState = reader.asLines()
                    .map(String::toCharArray)
                    .toArray(char[][]::new);
        }
    }

    @Override
    public String title() {
        return "Conway Cubes";
    }

    @Override
    public String solveSilver() {
        int finalSize = initialState.length + CYCLES * 2;
        char[][][] cube = empty3DCube(finalSize);

        for (int n = 1; n <= CYCLES; n++) {
            cube = simulate3D(cube);
        }
        int actives = countActives3D(cube);
        return String.format("There are %d active cubes at the end of the six-cycle boot process in 3D space.", actives);
    }

    private char[][][] empty3DCube(int size) {
        char[][][] cube = new char[size][size][size];
        for (int z = 0; z < size; z++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    cube[z][i][j] = '.';
                }
            }
        }
        int inputSize = initialState.length;
        for (int i = 0; i < inputSize; i++) {
            System.arraycopy(initialState[i], 0, cube[CYCLES][i + CYCLES], CYCLES, inputSize);
        }
        return cube;
    }

    private char[][][] simulate3D(char[][][] cube) {
        char[][][] newCube = new char[cube.length][cube.length][cube.length];
        for (int z = 0; z < cube.length; z++) {
            for (int i = 0; i < cube.length; i++) {
                System.arraycopy(cube[z][i], 0, newCube[z][i], 0, cube.length);
            }
        }
        for (int z = 0; z < cube.length; z++) {
            for (int i = 0; i < cube.length; i++) {
                for (int j = 0; j < cube.length; j++) {
                    int active = countActiveNeighbors3D(z, i, j, cube);
                    if (cube[z][i][j] == '.' && active == 3) {
                        newCube[z][i][j] = '#';
                    } else if (cube[z][i][j] == '#' && (active == 3 || active == 2)) {
                        newCube[z][i][j] = '#';
                    } else {
                        newCube[z][i][j] = '.';
                    }
                }
            }
        }
        return newCube;
    }

    private int countActiveNeighbors3D(int z, int i, int j, char[][][] cube) {
        int actives = 0;
        for (int dz = -1; dz <= 1; dz++) {
            for (int di = -1; di <= 1; di++) {
                for (int dj = -1; dj <= 1; dj++) {
                    if ((dz != 0 || di != 0 || dj != 0) && isNeighborActive3D(z + dz, i + di, j + dj, cube)) {
                        actives += 1;
                    }
                }
            }
        }
        return actives;
    }

    private boolean isNeighborActive3D(int z, int i, int j, char[][][] cube) {
        return z >= 0 && z < cube.length
                && i >= 0 && i < cube.length
                && j >= 0 && j < cube.length
                && cube[z][i][j] == '#';
    }

    private int countActives3D(char[][][] cube) {
        int actives = 0;
        for (char[][] face : cube) {
            for (int i = 0; i < cube.length; i++) {
                for (int j = 0; j < cube.length; j++) {
                    if (face[i][j] == '#') {
                        actives += 1;
                    }
                }
            }
        }
        return actives;
    }

    @Override
    public String solveGold() {
        int finalSize = initialState.length + CYCLES * 2;
        char[][][][] cube = empty4DCube(finalSize);

        for (int n = 1; n <= CYCLES; n++) {
            cube = simulate4D(cube);
        }
        int actives = countActives4D(cube);
        return String.format("There are %d active cubes at the end of the six-cycle boot process in 4D space.", actives);
    }

    private char[][][][] empty4DCube(int size) {
        char[][][][] cube = new char[size][size][size][size];
        for (int w = 0; w < size; w++) {
            for (int z = 0; z < size; z++) {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        cube[w][z][i][j] = '.';
                    }
                }
            }
        }
        int inputSize = initialState.length;
        for (int i = 0; i < inputSize; i++) {
            System.arraycopy(initialState[i], 0, cube[CYCLES][CYCLES][i + CYCLES], CYCLES, inputSize);
        }
        return cube;
    }

    private char[][][][] simulate4D(char[][][][] cube) {
        char[][][][] newCube = new char[cube.length][cube.length][cube.length][cube.length];
        for (int w = 0; w < cube.length; w++) {
            for (int z = 0; z < cube.length; z++) {
                for (int i = 0; i < cube.length; i++) {
                    System.arraycopy(cube[w][z][i], 0, newCube[w][z][i], 0, cube.length);
                }
            }
        }
        for (int w = 0; w < cube.length; w++) {
            for (int z = 0; z < cube.length; z++) {
                for (int i = 0; i < cube.length; i++) {
                    for (int j = 0; j < cube.length; j++) {
                        int active = countActiveNeighbors4D(w, z, i, j, cube);
                        if (cube[w][z][i][j] == '.' && active == 3) {
                            newCube[w][z][i][j] = '#';
                        } else if (cube[w][z][i][j] == '#' && (active == 3 || active == 2)) {
                            newCube[w][z][i][j] = '#';
                        } else {
                            newCube[w][z][i][j] = '.';
                        }
                    }
                }
            }
        }
        return newCube;
    }

    private int countActiveNeighbors4D(int w, int z, int i, int j, char[][][][] cube) {
        int actives = 0;
        for (int dw = -1; dw <= 1; dw++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        if ((dw != 0 || dz != 0 || di != 0 || dj != 0) && isNeighborActive4D(w + dw, z + dz, i + di, j + dj, cube)) {
                            actives += 1;
                        }
                    }
                }
            }
        }
        return actives;
    }

    private boolean isNeighborActive4D(int w, int z, int i, int j, char[][][][] cube) {
        return w >= 0 && w < cube.length
                && z >= 0 && z < cube.length
                && i >= 0 && i < cube.length
                && j >= 0 && j < cube.length
                && cube[w][z][i][j] == '#';
    }

    private int countActives4D(char[][][][] cube) {
        int actives = 0;
        for (char[][][] subCube : cube) {
            for (int z = 0; z < cube.length; z++) {
                for (int i = 0; i < cube.length; i++) {
                    for (int j = 0; j < cube.length; j++) {
                        if (subCube[z][i][j] == '#') {
                            actives += 1;
                        }
                    }
                }
            }
        }
        return actives;
    }
}
