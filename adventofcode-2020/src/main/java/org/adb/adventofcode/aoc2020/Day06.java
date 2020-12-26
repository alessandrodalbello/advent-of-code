package org.adb.adventofcode.aoc2020;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day06 implements Day {

    private static final String INPUT_FILENAME = "input_day6.txt";

    private final List<GroupAnswers> answers;

    public Day06() {
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
    public String title() {
        return "Custom Customs";
    }

    @Override
    public String solveSilver() {
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
        return String.format("The sum of the answers in each group is %d.", totalAnswers);
    }

    @Override
    public String solveGold() {
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
        return String.format("The sum of the shared answers in each group is %d.", totalAnswers);
    }

    private static class GroupAnswers {
        private final List<String> answers;

        private GroupAnswers(List<String> answers) {
            this.answers = answers;
        }
    }
}
