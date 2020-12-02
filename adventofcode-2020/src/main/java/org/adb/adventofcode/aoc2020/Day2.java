package org.adb.adventofcode.aoc2020;

import java.util.List;
import java.util.stream.Collectors;

import org.adb.adventofcode.Solver;
import org.adb.adventofcode.io.FileResourceReader;

class Day2 implements Solver {

    private static final String INPUT_FILENAME = "aoc_day2.txt";

    private final List<PasswordPolicy> passwordPolicies;

    public Day2() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            passwordPolicies = reader.parseAsStream(fastReader -> {
                int[] policy = fastReader.nextIntSplit("-");
                char target = fastReader.nextChar();
                String password = fastReader.nextString();
                return new PasswordPolicy(policy, target, password);
            }).collect(Collectors.toList());
        }
    }

    @Override
    public void solveSilver() {
        long valid = passwordPolicies.stream()
                .filter(PasswordPolicy::isValidByFrequency)
                .count();
        System.out.printf("There are %d valid passwords by frequency of target char.%n", valid);
    }

    @Override
    public void solveGold() {
        long valid = passwordPolicies.stream()
                .filter(PasswordPolicy::isValidByIndex)
                .count();
        System.out.printf("There are %d valid passwords by index positions of target char.%n", valid);
    }

    private static class PasswordPolicy {
        private final int[] policy;
        private final char targetChar;
        private final String password;

        private PasswordPolicy(int[] policy, char targetChar, String password) {
            this.policy = policy;
            this.targetChar = targetChar;
            this.password = password;
        }

        private boolean isValidByFrequency() {
            long count = password.chars()
                    .filter(c -> c == targetChar)
                    .count();
            return count >= policy[0] && count <= policy[1];
        }

        private boolean isValidByIndex() {
            int i = policy[0] - 1;
            boolean matchMin = i < password.length() && password.charAt(i) == targetChar;
            int j = policy[1] - 1;
            boolean matchMax = j < password.length() && password.charAt(j) == targetChar;
            return matchMin && !matchMax || !matchMin && matchMax;
        }
    }
}
