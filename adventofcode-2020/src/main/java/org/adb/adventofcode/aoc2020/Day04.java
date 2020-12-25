package org.adb.adventofcode.aoc2020;

import java.util.List;
import java.util.stream.Collectors;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;
import org.adb.adventofcode.io.StringReader;

class Day04 implements Day {

    private static final String INPUT_FILENAME = "input_day4.txt";

    private final List<Passport> passports;

    public Day04() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            passports = reader.parseMultilines(this::parsePassport).collect(Collectors.toList());
        }
    }

    private Passport parsePassport(String rawPassport) {
        try (StringReader passportReader = new StringReader(rawPassport)) {
            final Passport passport = new Passport();
            boolean hasNext = true;
            while (hasNext) {
                try {
                    String[] keyValue = passportReader.nextSplit(":");
                    assignPassportKey(passport, keyValue);
                } catch (RuntimeException e) {
                    hasNext = false;
                }
            }
            return passport;
        }
    }

    private void assignPassportKey(Passport passport, String[] keyValue) {
        String tokenKey = keyValue[0];
        String tokenValue = keyValue[1];
        if (tokenKey.equals("byr")) {
            passport.birthYear = parseNumber(tokenValue);
        } else if (tokenKey.equals("iyr")) {
            passport.issueYear = parseNumber(tokenValue);
        } else if (tokenKey.equals("eyr")) {
            passport.expirationYear = parseNumber(tokenValue);
        } else if (tokenKey.equals("pid")) {
            passport.passportId = tokenValue;
        } else if (tokenKey.equals("ecl")) {
            passport.eyeColor = tokenValue;
        } else if (tokenKey.equals("hcl")) {
            passport.hairColor = tokenValue;
        } else if (tokenKey.equals("hgt")) {
            passport.height = parseNumber(tokenValue.substring(0, tokenValue.length() - 2));
            passport.heightUnit = tokenValue.substring(tokenValue.length() - 2);
        }
    }

    private int parseNumber(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void solveSilver() {
        long valid = passports.stream()
                .filter(this::isValidPassportByNumberOfFields)
                .count();
        System.out.printf("There are %d passports with the correct number of fields.%n", valid);
    }

    private boolean isValidPassportByNumberOfFields(Passport passport) {
        return (passport.birthYear > 0 || passport.birthYear == -1)
                && (passport.issueYear > 0 || passport.issueYear == -1)
                && (passport.expirationYear > 0 || passport.expirationYear == -1)
                && (passport.height > 0 || passport.height == -1)
                && passport.passportId != null && passport.hairColor != null && passport.eyeColor != null;
    }

    @Override
    public void solveGold() {
        long valid = passports.stream()
                .filter(this::isValidPassportByFieldConstraints)
                .count();
        System.out.printf("There are %d passports with the correct number of valid fields.%n", valid);
    }

    private boolean isValidPassportByFieldConstraints(Passport passport) {
        return isValidRange(passport.birthYear, 1920, 2002)
                && isValidRange(passport.issueYear, 2010, 2020)
                && isValidRange(passport.expirationYear, 2020, 2030)
                && isValidPatter(passport.passportId, "\\d{9}")
                && isValidPatter(passport.hairColor, "#[\\da-f]{6}")
                && isValidPatter(passport.eyeColor, "amb|blu|brn|gry|grn|hzl|oth")
                && ("cm".equals(passport.heightUnit) && isValidRange(passport.height, 150, 193)
                    || "in".equals(passport.heightUnit) && isValidRange(passport.height, 59, 76));
    }

    private boolean isValidRange(int value, int low, int high) {
        return value >= low && value <= high;
    }

    private boolean isValidPatter(String value, String pattern) {
        return value != null && value.matches(pattern);
    }

    private static class Passport {
        private int birthYear;
        private int issueYear;
        private int expirationYear;
        private int height;
        private String passportId;
        private String hairColor;
        private String eyeColor;
        private String heightUnit;
    }
}
