package org.adb.adventofcode.aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day10 implements Day {

    private static final String INPUT_FILENAME = "input_day10.txt";

    private final List<Integer> adapters;

    public Day10() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            adapters = reader.asIntStream().sorted().boxed().collect(Collectors.toList());
        }
    }

    @Override
    public String title() {
        return "Adapter Array";
    }

    @Override
    public String solveSilver() {
        int joltsDiff1 = 0;
        int joltsDiff3 = 1;
        int previous = 0;
        for (Integer current : adapters) {
            if (current - previous == 3) {
                joltsDiff3++;
            } else if (current - previous == 1) {
                joltsDiff1++;
            }
            previous = current;
        }
        return String.format("The product of jolt differences is %d.", joltsDiff1 * joltsDiff3);
    }

    @Override
    public String solveGold() {
        List<Integer> adaptersCopy = new ArrayList<>(List.of(0));
        adaptersCopy.addAll(adapters);

        long[] cache = new long[adaptersCopy.size()];
        Arrays.fill(cache, 0L);
        cache[0] = 1L;
        long adapterCombos = countPaths(adaptersCopy, adaptersCopy.size() - 1, cache);
        return String.format("The number of possible adapters combinations is %d.", adapterCombos);
    }

    private long countPaths(List<Integer> adapters, int position, long[] cache) {
        if (cache[position] > 0) {
            return cache[position];
        }

        long paths = countPathsFromPosition(adapters, position, position - 1, cache) +
                countPathsFromPosition(adapters, position, position - 2, cache) +
                countPathsFromPosition(adapters, position, position - 3, cache);
        cache[position] = paths;
        return paths;
    }

    private long countPathsFromPosition(List<Integer> adapters, int position, int previous, long[] cache) {
        if (previous >= 0 && adapters.get(position) - adapters.get(previous) <= 3) {
            return countPaths(adapters, previous, cache);
        }
        return 0L;
    }
}
