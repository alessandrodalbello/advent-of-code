package org.adb.adventofcode.aoc2020;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;
import org.adb.adventofcode.io.StringReader;

class Day13 implements Day {

    private static final String INPUT_FILENAME = "input_day13.txt";

    private final long target;
    private final Map<Long, Integer> busses;

    public Day13() {
        busses = new HashMap<>();
        try (FileResourceReader fileReader = new FileResourceReader(INPUT_FILENAME)) {
            target = fileReader.nextLong();
            String rawBusses = fileReader.nextString();
            try (StringReader stringReader = new StringReader(rawBusses)) {
                String[] busses = stringReader.nextSplit(",");
                for (int i = 0; i < busses.length; i++) {
                    if (!"x".equals(busses[i])) {
                        long bus = Long.parseLong(busses[i]);
                        this.busses.put(bus, i);
                    }
                }
            }
        }
    }

    @Override
    public String title() {
        return "Shuttle Search";
    }

    @Override
    public String solveSilver() {
        long closestBus = -1;
        long minMinutes = Long.MAX_VALUE;
        for (Long bus : busses.keySet()) {
            long minutes = bus - (target % bus);
            if (minutes < minMinutes) {
                minMinutes = minutes;
                closestBus = bus;
            }
        }
        long answer = closestBus * minMinutes;
        return String.format("The ID of the closest bus multiplied to the number of minutes to wait is %d.", answer);
    }

    @Override
    public String solveGold() {
        long timestamp = busses.entrySet().stream()
                .filter(entry -> entry.getValue() == 0)
                .map(Map.Entry::getKey)
                .findFirst().orElse(0L);
        boolean found = false;
        while (!found) {
            final long finalTs = timestamp;
            List<Long> divisorBusses = busses.keySet().stream()
                    .filter(bus -> (finalTs + busses.get(bus)) % bus == 0)
                    .collect(Collectors.toList());
            found = divisorBusses.size() == busses.size();
            if (!found) {
                long increment = divisorBusses.stream().reduce(1L, (a, b) -> a * b);
                timestamp += increment;
            }
        }
        return String.format("The first timestamp at which busses departure at the same time based on their offset is %d.", timestamp);
    }
}
