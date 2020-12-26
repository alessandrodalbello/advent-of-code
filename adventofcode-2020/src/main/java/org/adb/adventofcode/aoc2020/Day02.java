package org.adb.adventofcode.aoc2020;

import java.util.List;
import java.util.stream.Collectors;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;
import org.adb.adventofcode.io.StringReader;

class Day02 implements Day {

    private static final String INPUT_FILENAME = "input_day2.txt";

    private final List<PasswordPolicy> passwordPolicies;

    public Day02() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            passwordPolicies = reader.parseLines(this::parsePolicy).collect(Collectors.toList());
        }
    }

    private PasswordPolicy parsePolicy(String rawPolicy) {
        try (StringReader policyReader = new StringReader(rawPolicy)) {
            int[] policy = policyReader.nextIntSplit("-");
            char target = policyReader.nextChar();
            String password = policyReader.nextString();
            return new PasswordPolicy(policy, target, password);
        }
    }

    @Override
    public String title() {
        return "Password Philosophy";
    }

    @Override
    public String solveSilver() {
        long valid = passwordPolicies.stream()
                .filter(this::isPolicyValidByFrequency)
                .count();
        return String.format("There are %d valid passwords by frequency of target char.", valid);
    }

    private boolean isPolicyValidByFrequency(PasswordPolicy passwordPolicy) {
        long count = passwordPolicy.password.chars()
                .filter(c -> c == passwordPolicy.targetChar)
                .count();
        return count >= passwordPolicy.policy[0] && count <= passwordPolicy.policy[1];
    }

    @Override
    public String solveGold() {
        long valid = passwordPolicies.stream()
                .filter(this::isPolicyValidByIndex)
                .count();
        return String.format("There are %d valid passwords by index positions of target char.", valid);
    }

    private boolean isPolicyValidByIndex(PasswordPolicy passwordPolicy) {
        String password = passwordPolicy.password;
        int i = passwordPolicy.policy[0] - 1;
        boolean matchMin = i < password.length() && password.charAt(i) == passwordPolicy.targetChar;
        int j = passwordPolicy.policy[1] - 1;
        boolean matchMax = j < password.length() && password.charAt(j) == passwordPolicy.targetChar;
        return matchMin && !matchMax || !matchMin && matchMax;
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
    }
}
