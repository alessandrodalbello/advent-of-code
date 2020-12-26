package org.adb.adventofcode;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.adb.adventofcode.io.FastReader;
import org.adb.adventofcode.io.FileResourceReader;
import org.adb.adventofcode.io.StandardInputReader;
import picocli.CommandLine;

@CommandLine.Command(
        name = "adventofcode",
        aliases = "aoc",
        description = "Solve an Advent of Code problem.",
        version = "1.0"
)
public class AdventOfCode implements Callable<Integer> {

    private static final String BANNER_FILENAME = "banner.txt";
    private static final String DAYS_CLASS_NAME_PATTERN = "org.adb.adventofcode.aoc%d.Day%02d";

    @CommandLine.Option(
            names = {"-d", "--day"},
            description = "Day to solve, in the range [1-25]."
    )
    private int dayNumber = -1;

    @CommandLine.Option(
            names = {"-t", "--time"},
            description = "Whether to track time execution or not."
    )
    private boolean trackTimes = false;

    private final int year;

    public AdventOfCode(int year) {
        this.year = year;
    }

    @Override
    public Integer call() {
        loadBanner();
        try {
            if (dayNumber == -1) {
                solve();
            } else {
                solveDay(dayNumber);
            }
            return 0;
        } catch (IllegalArgumentException e) {
            System.err.println("*** INVALID DAY *** ");
            System.err.println("Select a valid day. A valid day is included between 1 and 25.");
            return 1;
        } catch (ReflectiveOperationException e) {
            System.err.println("*** UNABLE TO LOAD DAY *** ");
            System.err.printf("An error occurred loading day %d.%n", dayNumber);
            return 2;
        }
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

    public void solve() throws IllegalArgumentException, ReflectiveOperationException {
        System.out.printf("%nSelect an Advent of Code 2020 day [1-25]: ");
        try (FastReader inputReader = new StandardInputReader()) {
            int dayNumber = inputReader.nextInt();
            solveDay(dayNumber);
        }
    }

    public void solveDay(int dayNumber) throws IllegalArgumentException, ReflectiveOperationException {
        if (dayNumber < 1 || dayNumber > 25) {
            throw new IllegalArgumentException("Invalid day number: out of range.");
        }
        Day day = loadDay(dayNumber);
        System.out.printf("Solving day %d (%s)...%n", dayNumber, day.title());
        solveDay(day);
    }

    private Day loadDay(int dayNumber) throws ReflectiveOperationException {
        String dayClassName = String.format(DAYS_CLASS_NAME_PATTERN, year, dayNumber);
        Constructor<?> constructor = Class.forName(dayClassName).getConstructor();
        constructor.setAccessible(true);
        return (Day) constructor.newInstance();
    }

    private void solveDay(Day day) {
        long tStart = System.nanoTime();
        System.out.print("\t* SILVER: ");
        String silverResult = day.solveSilver();
        long tSilver = System.nanoTime();
        printResult(silverResult, tSilver - tStart);

        System.out.print("\t**  GOLD: ");
        String goldResult = day.solveGold();
        long tGold = System.nanoTime();
        printResult(goldResult, tGold - tSilver);
    }

    private void printResult(String result, long nanos) {
        System.out.print(result);
        if (trackTimes) {
            System.out.printf(" (%d ms)", TimeUnit.NANOSECONDS.toMillis(nanos));
        }
        System.out.printf("%n");
    }
}
