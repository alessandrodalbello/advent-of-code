package org.adb.adventofcode.aoc2020;

import java.util.Arrays;
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

    private final List<GroupAnswers> answers;

    public Day6() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            answers = reader.parseMultilines(this::parseGroupAnswers).collect(Collectors.toList());
        }
    }

    private GroupAnswers parseGroupAnswers(String groupAnswers) {
        List<String> answers = Arrays.stream(groupAnswers.split("\\n"))
                .collect(Collectors.toList());
        return new GroupAnswers(answers);
    }

    @Override
    public void solveSilver() {
        int totalAnswers = 0;
        for (GroupAnswers groupAnswers : answers) {
            Set<Integer> set = new HashSet<>();
            groupAnswers.answers.forEach(answer -> {
                for (int i = 0; i < answer.length(); i++) {
                    set.add((int) answer.charAt(i));
                }
            });
            totalAnswers += set.size();
        }
        System.out.printf("The sum of the answers in each group is %d.%n", totalAnswers);
    }

    @Override
    public void solveGold() {
        int totalAnswers = 0;
        for (GroupAnswers groupAnswers : answers) {
            Map<Integer, Integer> charFrequency = new HashMap<>();
            for (String personAnswer : groupAnswers.answers) {
                for (int i = 0; i < personAnswer.length(); i++) {
                    int c = personAnswer.charAt(i);
                    charFrequency.merge(c, 1, Integer::sum);
                }
            }
            totalAnswers += charFrequency.entrySet().stream()
                    .filter(entry -> entry.getValue() == groupAnswers.answers.size())
                    .count();
        }
        System.out.printf("The sum of the shared answers in each group is %d.%n", totalAnswers);
    }

    private static class GroupAnswers {
        private final List<String> answers;

        private GroupAnswers(List<String> answers) {
            this.answers = answers;
        }
    }
}
