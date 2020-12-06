package org.adb.adventofcode.aoc2020;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.adb.adventofcode.Solver;
import org.adb.adventofcode.io.FileResourceReader;

class Day6 implements Solver {

    private static final String INPUT_FILENAME = "aoc_day6.txt";

    private final List<String> answers;

    public Day6() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            answers = reader.asMultilines().collect(Collectors.toList());
        }
    }

    @Override
    public void solveSilver() {
        int totalAnswers = 0;
        for (String groupAnswers : answers) {
            Set<Integer> set = new HashSet<>();
            for (int i = 0; i < groupAnswers.length(); i++) {
                int c = groupAnswers.charAt(i);
                if (c >= 'a' && c <= 'z') {
                    set.add(c);
                }
            }
            totalAnswers += set.size();
        }
        System.out.printf("The sum of the answers in each group is %d.%n", totalAnswers);
    }

    @Override
    public void solveGold() {
        int totalAnswers = 0;
        for (String groupAnswers : answers) {
            Map<Integer, Integer> map = new HashMap<>();
            String[] personAnswers = groupAnswers.split("\\s");
            for (String personAnswer : personAnswers) {
                for (int i = 0; i < personAnswer.length(); i++) {
                    int c = personAnswer.charAt(i);
                    map.merge(c, 1, Integer::sum);
                }
            }
            totalAnswers += map.entrySet().stream()
                    .filter(entry -> entry.getValue() == personAnswers.length)
                    .count();
        }
        System.out.printf("The sum of the shared answers in each group is %d.%n", totalAnswers);
    }
}
