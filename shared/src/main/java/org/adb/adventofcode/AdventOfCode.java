package org.adb.adventofcode;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.adb.adventofcode.io.FastReader;
import org.adb.adventofcode.io.FileResourceReader;
import org.adb.adventofcode.io.StandardInputReader;

public class AdventOfCode {

    private static final String BANNER_FILENAME = "banner.txt";
    private static final String DAYS_CLASS_NAME_PATTERN = "org.adb.adventofcode.aoc%d.Day%02d";

    private final int year;

    public AdventOfCode(int year) {
        this.year = year;
        loadBanner();
    }

    private void loadBanner() {
        try (FileResourceReader reader = new FileResourceReader(BANNER_FILENAME)) {
            List<String> lines = reader.asLines()
                    .takeWhile(line -> !line.isBlank())
                    .collect(Collectors.toList());
            lines.set(lines.size() - 1, replaceYear(lines.get(lines.size() - 1)));
            lines.forEach(System.out::println);
        }
    }

    private String replaceYear(String bannerLine) {
        char[] yearChars = String.valueOf(year).toCharArray();
        for (int i = 0; i < 4; i++) {
            bannerLine = bannerLine.replaceFirst(String.valueOf((char) ('a' + i)), String.valueOf(yearChars[i]));
        }
        return bannerLine;
    }

    public void solve() {
        System.out.printf("%nSelect an Advent of Code 2020 day [1-25]: ");
        try (FastReader inputReader = new StandardInputReader()) {
            int dayNumber = inputReader.nextInt();
            solveDay(dayNumber);
        } catch (RuntimeException e) {
            System.err.println("*** INVALID DAY *** ");
            System.err.println("Select a valid day. A valid day is included between 1 and 25.");
        }
    }

    public void solveDay(int dayNumber) {
        if (dayNumber >= 1 && dayNumber <= 25) {
            try {
                Day day = loadDay(dayNumber);
                solveDay(day);
            } catch (ReflectiveOperationException e) {
                System.err.println("*** UNABLE TO LOAD DAY *** ");
                System.err.printf("An error occurred loading day %d.%n", dayNumber);
            }
        } else {
            System.err.println("*** INVALID DAY *** ");
            System.err.println("Select a valid day. A valid day is included between 1 and 25.");
        }
    }

    private Day loadDay(int dayNumber) throws ReflectiveOperationException {
        String dayClassName = String.format(DAYS_CLASS_NAME_PATTERN, year, dayNumber);
        Constructor<?> constructor = Class.forName(dayClassName).getConstructor();
        constructor.setAccessible(true);
        return (Day) constructor.newInstance();
    }

    private void solveDay(Day day) {
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
