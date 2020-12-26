package org.adb.adventofcode.aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;
import org.adb.adventofcode.io.StringReader;

class Day16 implements Day {

    private static final String INPUT_FILENAME = "input_day16.txt";

    private final Map<String, FieldRule> fieldRules;
    private final List<Ticket> tickets;
    private final List<Ticket> validTickets;

    public Day16() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            String[] inputParts = reader.asMultilines().toArray(String[]::new);
            fieldRules = parseFieldRules(inputParts[0]);
            tickets = parseTickets(inputParts[1], inputParts[2]);
        }
        validTickets = new LinkedList<>();
    }

    private Map<String, FieldRule> parseFieldRules(String rawFieldRules) {
        Map<String, FieldRule> fieldRules = new HashMap<>();
        String[] rawRules = rawFieldRules.split("\\n");
        for (String rawRule : rawRules) {
            String[] ruleParts = rawRule.split(": ");
            String fieldName = ruleParts[0];
            String[] ranges = ruleParts[1].split(" or ");
            int[] range1 = parseRange(ranges[0]);
            int[] range2 = parseRange(ranges[1]);
            fieldRules.put(fieldName, new FieldRule(range1, range2));
        }
        return fieldRules;
    }

    private int[] parseRange(String rawRange) {
        return Arrays.stream(rawRange.split("-")).mapToInt(Integer::parseInt).toArray();
    }

    private List<Ticket> parseTickets(String rawOwnTicket, String rawNearbyTickets) {
        String[] rawTickets = rawNearbyTickets.split("\\n");
        List<Ticket> tickets = new ArrayList<>(rawTickets.length);
        Ticket ownTicket = parseOwnTicket(rawOwnTicket);
        tickets.add(ownTicket);
        for (int i = 1; i < rawTickets.length; i++) {
            Ticket ticket = parseTicket(rawTickets[i]);
            tickets.add(ticket);
        }
        return tickets;
    }

    private Ticket parseOwnTicket(String rawOwnTicket) {
        String ticket = rawOwnTicket.split("\\n")[1];
        return parseTicket(ticket);
    }

    private Ticket parseTicket(String rawTicket) {
        try (StringReader reader = new StringReader(rawTicket)) {
            int[] fields = reader.nextIntSplit(",");
            return new Ticket(fields);
        }
    }

    @Override
    public String title() {
        return "Ticket Translation";
    }

    @Override
    public String solveSilver() {
        int minValue = fieldRules.values().stream()
                .mapToInt(fieldRule -> fieldRule.range1[0])
                .min().orElseThrow();
        int maxValue = fieldRules.values().stream()
                .mapToInt(fieldRule -> fieldRule.range2[1])
                .min().orElseThrow();
        int errorRate = identifyInvalidTickets(minValue, maxValue);
        return String.format("The ticket scanning error rate is %d.", errorRate);
    }

    private int identifyInvalidTickets(int minValue, int maxValue) {
        int errorRate = 0;
        for (Ticket ticket : tickets) {
            boolean isValidTicket = true;
            for (int field : ticket.fields) {
                boolean isInvalidField = field < minValue || field > maxValue;
                if (isInvalidField) {
                    errorRate += field;
                    isValidTicket = false;
                }
            }
            if (isValidTicket) {
                validTickets.add(ticket);
            }
        }
        return errorRate;
    }

    @Override
    public String solveGold() {
        Map<String, Set<Integer>> fieldNameOptions = calculateOptions();
        Map<String, Integer> fieldNameMatches = identifyMatches(fieldNameOptions);

        Ticket ticket = validTickets.get(0);
        long result = fieldNameMatches.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("departure"))
                .mapToInt(Map.Entry::getValue)
                .mapToLong(i -> ticket.fields[i])
                .reduce(1L, (a, b) -> a * b);
        return String.format("The product of departures is %d.", result);
    }

    private Map<String, Set<Integer>> calculateOptions() {
        Map<String, Set<Integer>> fieldNameOptions = new HashMap<>();
        int numberOfFields = validTickets.get(0).fields.length;
        for (String fieldName : fieldRules.keySet()) {
            FieldRule fieldRule = fieldRules.get(fieldName);
            for (int i = 0; i < numberOfFields; i++) {
                boolean canBeField = canBeField(fieldRule, i);
                if (canBeField) {
                    fieldNameOptions.putIfAbsent(fieldName, new HashSet<>());
                    fieldNameOptions.get(fieldName).add(i);
                }
            }
        }
        return fieldNameOptions;
    }

    private boolean canBeField(FieldRule fieldRule, int fieldIndex) {
        return validTickets.stream()
                .map(ticket -> ticket.fields[fieldIndex])
                .allMatch(field -> field >= fieldRule.range1[0] && field <= fieldRule.range1[1]
                        || field >= fieldRule.range2[0] && field <= fieldRule.range2[1]);
    }

    private Map<String, Integer> identifyMatches(Map<String, Set<Integer>> fieldNameOptions) {
        Map<String, Integer> fieldNameMatches = new HashMap<>();
        boolean unidentifiedExist;
        do {
            Optional<Map.Entry<String, Set<Integer>>> possibleOption = fieldNameOptions.entrySet().stream()
                    .filter(entry -> entry.getValue().size() == 1)
                    .findFirst();
            unidentifiedExist = possibleOption.isPresent();

            if (possibleOption.isPresent()) {
                Map.Entry<String, Set<Integer>> fieldNameOption = possibleOption.get();
                String fieldName = fieldNameOption.getKey();
                Integer ticketIndex = fieldNameOption.getValue().iterator().next();
                fieldNameMatches.put(fieldName, ticketIndex);
                fieldNameOptions.remove(fieldName);
                fieldNameOptions.values().forEach(indexes -> indexes.remove(ticketIndex));
            }
        } while (unidentifiedExist);
        return fieldNameMatches;
    }

    private static class FieldRule {
        private final int[] range1;
        private final int[] range2;

        private FieldRule(int[] range1, int[] range2) {
            this.range1 = range1;
            this.range2 = range2;
        }
    }

    private static class Ticket {
        private final int[] fields;

        private Ticket(int[] fields) {
            this.fields = fields;
        }
    }
}
