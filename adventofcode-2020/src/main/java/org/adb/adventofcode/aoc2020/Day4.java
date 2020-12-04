package org.adb.adventofcode.aoc2020;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.adb.adventofcode.Solver;
import org.adb.adventofcode.io.FileResourceReader;

class Day4 implements Solver {

    private static final String INPUT_FILENAME = "aoc_day4.txt";

    private final List<Passport> passports;

    public Day4() {
        passports = new LinkedList<>();

        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            List<String> lines = reader.asLines().collect(Collectors.toList());
            StringBuilder rawPassport = new StringBuilder();
            for (String line : lines) {
                if (line.isEmpty()) {
                    Passport passport = Passport.parse(rawPassport.substring(1));
                    passports.add(passport);
                    rawPassport.setLength(0);
                } else {
                    rawPassport.append(" ").append(line);
                }
            }
        }
    }

    @Override
    public void solveSilver() {
        long valid = passports.stream()
                .filter(Passport::isValidByNumberOfFields)
                .count();
        System.out.printf("There are %d passports with the correct number of fields.%n", valid);
    }

    @Override
    public void solveGold() {
        long valid = passports.stream()
                .filter(Passport::isValidByFieldConstraints)
                .count();
        System.out.printf("There are %d passports with the correct number of valid fields.%n", valid);
    }

    private static class Passport {

        private static final Set<String> EYE_COLORS = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
        private static final String HEIGHT_UNIT_CM = "cm";
        private static final String HEIGHT_UNIT_IN = "in";

        private int birthYear;
        private int issueYear;
        private int expirationYear;
        private int height;
        private String passportId;
        private String hairColor;
        private String eyeColor;
        private String heightUnit;

        private static Passport parse(String rawPassport) {
            String[] tokens = rawPassport.split("\\s");
            final Passport passport = new Passport();
            for (String token : tokens) {
                String tokenValue = token.substring(4);
                if (token.startsWith("byr:")) {
                    passport.birthYear = Passport.parseNumber(tokenValue);
                } else if (token.startsWith("iyr:")) {
                    passport.issueYear = Passport.parseNumber(tokenValue);
                } else if (token.startsWith("eyr:")) {
                    passport.expirationYear = Passport.parseNumber(tokenValue);
                } else if (token.startsWith("pid:")) {
                    passport.passportId = tokenValue;
                } else if (token.startsWith("ecl:")) {
                    passport.eyeColor = tokenValue;
                } else if (token.startsWith("hcl:")) {
                    passport.hairColor = tokenValue;
                } else if (token.startsWith("hgt:")) {
                    passport.height = Passport.parseNumber(tokenValue.substring(0, tokenValue.length() - 2));
                    passport.heightUnit = tokenValue.substring(tokenValue.length() - 2);
                }
            }
            return passport;
        }

        private static int parseNumber(String number) {
            try {
                return Integer.parseInt(number);
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        private boolean isValidByNumberOfFields() {
            return (birthYear > 0 || birthYear == -1)
                    && (issueYear > 0 || issueYear == -1)
                    && (expirationYear > 0 || expirationYear == -1)
                    && (height > 0 || height == -1)
                    && passportId != null && hairColor != null && eyeColor != null;
        }

        private boolean isValidByFieldConstraints() {
            if (birthYear < 1920 || birthYear > 2002) {
                return false;
            }
            if (issueYear < 2010 || issueYear > 2020) {
                return false;
            }
            if (expirationYear < 2020 || expirationYear > 2030) {
                return false;
            }
            if (passportId == null || !passportId.matches("\\d{9}")) {
                return false;
            }
            if (hairColor == null || !hairColor.matches("#[\\da-f]{6}")) {
                return false;
            }
            if (eyeColor == null || !EYE_COLORS.contains(eyeColor)) {
                return false;
            }
            if (HEIGHT_UNIT_CM.equals(heightUnit)) {
                return height >= 150 && height <= 193;
            } else if (HEIGHT_UNIT_IN.equals(heightUnit)) {
                return height >= 59 && height <= 76;
            } else {
                return false;
            }
        }
    }

}
