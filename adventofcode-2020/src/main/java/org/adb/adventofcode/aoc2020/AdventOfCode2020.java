package org.adb.adventofcode.aoc2020;

import org.adb.adventofcode.AdventOfCode;
import picocli.CommandLine;

public class AdventOfCode2020 {

    public static void main(String[] args) {
        AdventOfCode adventOfCode = new AdventOfCode(2020);
        int exitCode = new CommandLine(adventOfCode).execute(args);
        System.exit(exitCode);
    }
}
