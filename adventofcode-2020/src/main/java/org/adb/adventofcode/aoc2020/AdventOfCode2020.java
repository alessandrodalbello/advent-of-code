package org.adb.adventofcode.aoc2020;

import java.util.concurrent.TimeUnit;

import org.adb.adventofcode.Day;

public class AdventOfCode2020 {

    public static void main(String[] args) {
        System.out.println("--- AoC 2020 ---");
        long tStart = System.nanoTime();
        Day day = new Day25();
        long tInput = System.nanoTime();
        System.out.printf("Solver loaded in %d ms.%n", nanosToMillis(tInput - tStart));

        System.out.printf("%n* SILVER *%n");
        day.solveSilver();
        long tSilver = System.nanoTime();
        System.out.printf("Silver star solved in %d ms.%n", nanosToMillis(tSilver - tInput));

        System.out.printf("%n** GOLD **%n");
        day.solveGold();
        long tGold = System.nanoTime();
        System.out.printf("Gold star solved in %d ms.%n", nanosToMillis(tGold - tSilver));
    }

    private static long nanosToMillis(long nanos) {
        return TimeUnit.NANOSECONDS.toMillis(nanos);
    }
}
