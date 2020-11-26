package org.adb.adventofcode.practice.day1;

import org.adb.adventofcode.io.FileResourceReader;

public class AocPracticeDay1 {

    private static final String INPUT_FILENAME = "aoc_in1.txt";

    public static void main(String[] args) {
        AocPracticeDay1.solvePart1();
        AocPracticeDay1.solvePart2();
    }

    private static void solvePart1() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            int totalFuel = reader.asIntStream()
                    .map(mass -> Math.floorDiv(mass, 3) - 2)
                    .sum();
            System.out.printf("Total fuel to use considering only the mass of modules is %d.%n", totalFuel);
        }
    }

    private static void solvePart2() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            int totalFuel = reader.asIntStream()
                    .map(AocPracticeDay1::fuelForMass)
                    .sum();
            System.out.printf("Total fuel to use considering the mass of modules and the fuel itself is %d.%n", totalFuel);
        }
    }

    private static int fuelForMass(int mass) {
        int fuel = Math.floorDiv(mass, 3) - 2;
        if (fuel <= 0) {
            return 0;
        } else {
            return fuel + AocPracticeDay1.fuelForMass(fuel);
        }
    }
}
