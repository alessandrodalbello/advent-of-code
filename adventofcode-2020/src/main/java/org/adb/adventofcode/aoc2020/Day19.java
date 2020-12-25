package org.adb.adventofcode.aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day19 implements Day {

    private static final String INPUT_FILENAME = "input_day19.txt";

    private final Rule rootRule;
    private final List<String> messages;

    private Rule rule42;
    private Rule rule31;

    public Day19() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            String[] input = reader.asMultilines().toArray(String[]::new);
            List<String> rawRules = input[0].lines().collect(Collectors.toList());
            rootRule = parseRules(rawRules);
            messages = input[1].lines().collect(Collectors.toList());
        }
    }

    private Rule parseRules(List<String> rawRules) {
        String[] rawRuleContents = new String[rawRules.size()];
        for (String rawRule : rawRules) {
            String[] tokens = rawRule.split(": ");
            int ruleKey = Integer.parseInt(tokens[0]);
            rawRuleContents[ruleKey] = tokens[1];
        }
        Rule[] rulesCache = new Rule[rawRules.size()];
        return parseRules(0, rawRuleContents, rulesCache);
    }

    private Rule parseRules(int ruleKey, String[] rawRuleContents, Rule[] rulesCache) {
        if (rulesCache[ruleKey] != null) {
            return rulesCache[ruleKey];
        }

        String rawRule = rawRuleContents[ruleKey];
        Rule rule = rawRule.startsWith("\"") ?
                parseCharRule(rawRule) : parseChainRule(rawRule, rawRuleContents, rulesCache);
        if (ruleKey == 42) {
            rule42 = rule;
        } else if (ruleKey == 31) {
            rule31 = rule;
        }
        rulesCache[ruleKey] = rule;
        return rule;
    }

    private CharRule parseCharRule(String rawRule) {
        return new CharRule(rawRule.charAt(1));
    }

    private ChainRule parseChainRule(String rawRule, String[] rawRuleContents, Rule[] rulesCache) {
        String[] chains = rawRule.split(" \\| ");
        List<Rule> chain1 = parseChain(chains[0], rawRuleContents, rulesCache);
        List<Rule> chain2 = null;
        if (chains.length == 2) {
            chain2 = parseChain(chains[1], rawRuleContents, rulesCache);
        }
        return new ChainRule(chain1, chain2);
    }

    private List<Rule> parseChain(String chain, String[] rawRuleContents, Rule[] rulesCache) {
        return Arrays.stream(chain.split(" "))
                .map(rawChildKey -> {
                    int childKey = Integer.parseInt(rawChildKey);
                    return parseRules(childKey, rawRuleContents, rulesCache);
                }).collect(Collectors.toList());
    }

    @Override
    public void solveSilver() {
        long validMessages = messages.stream()
                .filter(this::isValidMessage)
                .count();
        System.out.printf("There are %d valid silver messages.%n", validMessages);
    }

    private boolean isValidMessage(String message) {
        Optional<String> matchResult = matchChainRules(message, ((ChainRule) rootRule).chain1);
        return matchResult.filter(""::equals).isPresent();
    }

    private Optional<String> matchChainRules(String message, List<Rule> chain) {
        LinkedList<String> subMessages = new LinkedList<>();
        subMessages.add(message);

        for (Rule rule : chain) {
            if (subMessages.isEmpty()) {
                return Optional.empty();
            }
            String remainingMessage = subMessages.removeLast();
            if (remainingMessage.isEmpty()) {
                return Optional.empty();
            }

            if (rule instanceof CharRule) {
                Optional<String> matchResult = matchCharRule(remainingMessage, (CharRule) rule);
                matchResult.ifPresent(subMessages::add);
            } else {
                ChainRule chainRule = (ChainRule) rule;
                Optional<String> matchChain1 = matchChainRules(remainingMessage, chainRule.chain1);
                matchChain1.ifPresent(subMessages::add);
                if (chainRule.chain2 != null) {
                    Optional<String> matchChain2 = matchChainRules(remainingMessage, chainRule.chain2);
                    matchChain2.ifPresent(subMessages::add);
                }
            }
        }
        return subMessages.stream().findFirst();
    }

    private Optional<String> matchCharRule(String message, CharRule rule) {
        return rule.c == message.charAt(0) ? Optional.of(message.substring(1)) : Optional.empty();
    }

    @Override
    public void solveGold() {
        final List<String> rule42SubMessages = rule42SubMessages();
        final List<String> rule31SubMessages = rule31SubMessages();

        long validMessages = messages.stream()
                .filter(message -> isValidMessage(message, rule42SubMessages, rule31SubMessages))
                .count();
        System.out.printf("There are %d valid gold messages.%n", validMessages);
    }

    private List<String> rule42SubMessages() {
        return ruleSubMessages(rule42, List.of(new StringBuilder())).stream()
                .map(StringBuilder::toString)
                .collect(Collectors.toList());
    }

    private List<String> rule31SubMessages() {
        return ruleSubMessages(rule31, List.of(new StringBuilder())).stream()
                .map(StringBuilder::toString)
                .collect(Collectors.toList());
    }

    private List<StringBuilder> ruleSubMessages(Rule rule, List<StringBuilder> prefixes) {
        if (rule instanceof CharRule) {
            char c = ((CharRule) rule).c;
            return prefixes.stream()
                    .map(builder -> builder.append(c))
                    .collect(Collectors.toList());
        } else {
            ChainRule chainRule = (ChainRule) rule;
            List<StringBuilder> subMessages = new ArrayList<>(chainSubMessages(chainRule.chain1, prefixes));
            if (chainRule.chain2 != null) {
                subMessages.addAll(chainSubMessages(chainRule.chain2, prefixes));
            }
            return subMessages;
        }
    }

    private List<StringBuilder> chainSubMessages(List<Rule> rules, List<StringBuilder> prefixes) {
        List<StringBuilder> subMessages = new ArrayList<>(prefixes);
        for (Rule rule : rules) {
            List<StringBuilder> ruleSubMessages = ruleSubMessages(rule, List.of(new StringBuilder()));
            List<StringBuilder> newSubMessages = new ArrayList<>(prefixes.size() * 2);
            for (StringBuilder subMessage : subMessages) {
                for (StringBuilder ruleSubMessage : ruleSubMessages) {
                    StringBuilder concatResult = new StringBuilder(subMessage).append(ruleSubMessage);
                    newSubMessages.add(concatResult);
                }
            }
            subMessages = newSubMessages;
        }
        return subMessages;
    }

    private boolean isValidMessage(String message, List<String> subMessages42, List<String> subMessages31) {
        int count31Matches = countMessages(message, subMessages31);
        if (count31Matches > 0) {
            message = message.substring(0, message.length() - count31Matches * 8);
            int count42Matches = countMessages(message, subMessages42);
            message = message.substring(0, message.length() - count42Matches * 8);
            return message.isEmpty() && count42Matches > count31Matches;
        }
        return false;
    }

    private int countMessages(String message, List<String> subMessages) {
        int counter = 0;
        boolean isMatching = true;
        while (isMatching && message.length() >= 8) {
            String messageTail = message.substring(message.length() - 8);
            isMatching = subMessages.contains(messageTail);
            if (isMatching) {
                counter += 1;
                message = message.substring(0, message.length() - 8);
            }
        }
        return counter;
    }

    private interface Rule {
    }

    private static class ChainRule implements Rule {
        private final List<Rule> chain1;
        private final List<Rule> chain2;

        private ChainRule(List<Rule> chain1, List<Rule> chain2) {
            this.chain1 = chain1;
            this.chain2 = chain2;
        }
    }

    private static class CharRule implements Rule {
        private final char c;

        private CharRule(char c) {
            this.c = c;
        }
    }
}
