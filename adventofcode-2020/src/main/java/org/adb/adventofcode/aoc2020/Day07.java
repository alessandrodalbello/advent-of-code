package org.adb.adventofcode.aoc2020;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day07 implements Day {

    private static final String INPUT_FILENAME = "input_day7.txt";
    private static final String BAG_COLOR = "shiny gold";

    private final Map<String, Map<String, Integer>> bagRules;

    public Day07() {
        bagRules = new HashMap<>();
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            List<String> rawBagRules = reader.asLines().collect(Collectors.toList());
            for (String bagRule : rawBagRules) {
                String[] bagRuleTokens = bagRule.split(" bags contain ", 2);
                String bagColor = bagRuleTokens[0];
                Map<String, Integer> containedBags = parseContainedBags(bagRuleTokens[1]);
                bagRules.put(bagColor, containedBags);
            }
        }
    }

    private Map<String, Integer> parseContainedBags(String rawContainedBag) {
        Map<String, Integer> containedBags = new HashMap<>();
        String[] rawContainedBags = rawContainedBag.split(", ");
        for (String containedBag : rawContainedBags) {
            if (!containedBag.startsWith("no other")) {
                String[] tokens = containedBag.split(" ", 2);
                int count = Integer.parseInt(tokens[0]);
                containedBags.put(tokens[1].replaceAll(" bag.*", ""), count);
            }
        }
        return containedBags;
    }

    @Override
    public void solveSilver() {
        Set<String> distinctColors = getDistinctColors(BAG_COLOR);
        System.out.printf("There are %d distinct colors that can contain %s bags.%n", distinctColors.size(), BAG_COLOR);
    }

    private Set<String> getDistinctColors(String targetColor) {
        List<String> colors = bagRules.entrySet().stream()
                .filter(entry -> entry.getValue().containsKey(targetColor))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        Set<String> allColors = new HashSet<>(colors);
        for (String color : colors) {
            allColors.addAll(getDistinctColors(color));
        }
        return allColors;
    }

    @Override
    public void solveGold() {
        long numberOfBags = countBags(BAG_COLOR, new HashMap<>()) - 1;
        System.out.printf("There are %d other bags inside a %s bag.%n", numberOfBags, BAG_COLOR);
    }

    private long countBags(String color, Map<String, Long> memory) {
        Map<String, Integer> nestedBags = bagRules.get(color);
        if (nestedBags.isEmpty()) {
            return 1;
        }

        long numberOfBags = 1;
        for (Map.Entry<String, Integer> entry : nestedBags.entrySet()) {
            String nestedColor = entry.getKey();
            Integer count = entry.getValue();

            long nestedCount;
            if (memory.containsKey(nestedColor)) {
                nestedCount = memory.get(nestedColor);
            } else {
                nestedCount = countBags(nestedColor, memory);
                memory.put(nestedColor, nestedCount);
            }
            numberOfBags += count * nestedCount;
        }
        return numberOfBags;
    }
}
