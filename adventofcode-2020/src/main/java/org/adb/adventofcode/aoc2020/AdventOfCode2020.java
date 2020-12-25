package org.adb.adventofcode.aoc2020;

import java.util.concurrent.TimeUnit;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FastReader;
import org.adb.adventofcode.io.FileResourceReader;
import org.adb.adventofcode.io.StandardInputReader;

public class AdventOfCode2020 {

    private static final String BANNER_FILENAME = "banner.txt";

    public static void main(String[] args) {
        loadBanner();
        System.out.print("Select an Advent of Code 2020 day [1-25]: ");
        try (FastReader inputReader = new StandardInputReader()) {
            int dayNumber = inputReader.nextInt();
            if (dayNumber < 1 || dayNumber > 25) {
                throw new IllegalArgumentException("Day number out of range");
            }

            try {
                Day day = loadDay(dayNumber);
                solveDay(day);
            } catch (ReflectiveOperationException e) {
                System.err.println("*** UNABLE TO LOAD DAY *** ");
                System.err.printf("An error occurred loading day %d.%n", dayNumber);
            }
        } catch (RuntimeException e) {
            System.err.println("*** INVALID DAY *** ");
            System.err.println("Select a valid day. A valid day is included between 1 and 25.");
        }
    }

    private static void loadBanner() {
        try (FileResourceReader reader = new FileResourceReader(BANNER_FILENAME)) {
            reader.asLines().takeWhile(line -> !line.isBlank()).forEach(System.out::println);
        }
    }

    private static Day loadDay(int dayNumber) throws ReflectiveOperationException {
        String dayClassName = AdventOfCode2020.class.getPackageName() + String.format(".Day%02d", dayNumber);
        return (Day) Class.forName(dayClassName).getConstructor().newInstance();
    }

    private static void solveDay(Day day) {
        long tStart = System.nanoTime();
        System.out.printf("%n* SILVER *%n");
        day.solveSilver();
        long tSilver = System.nanoTime();
        System.out.printf("Silver star solved in %d ms.%n", nanosToMillis(tSilver - tStart));

        System.out.printf("%n** GOLD **%n");
        day.solveGold();
        long tGold = System.nanoTime();
        System.out.printf("Gold star solved in %d ms.%n", nanosToMillis(tGold - tSilver));
    }

    private static long nanosToMillis(long nanos) {
        return TimeUnit.NANOSECONDS.toMillis(nanos);
    }
}
