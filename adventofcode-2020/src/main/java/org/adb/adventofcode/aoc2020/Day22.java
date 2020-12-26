package org.adb.adventofcode.aoc2020;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day22 implements Day {

    private static final String INPUT_FILENAME = "input_day22.txt";

    private final List<Integer> player1Deck;
    private final List<Integer> player2Deck;

    public Day22() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            String[] input = reader.asMultilines().toArray(String[]::new);
            player1Deck = parseDeck(input[0]);
            player2Deck = parseDeck(input[1]);
        }
    }

    private List<Integer> parseDeck(String rawDeck) {
        return rawDeck.lines()
                .skip(1)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public String title() {
        return "Crab Combat";
    }

    @Override
    public String solveSilver() {
        CrabCombatResult combatResult = playCombat(new LinkedList<>(player1Deck), new LinkedList<>(player2Deck));
        int score = calculateScore(combatResult.winningDeck);
        return String.format("The score of the winning player of Combat is %d.", score);
    }

    private CrabCombatResult playCombat(LinkedList<Integer> player1Deck, LinkedList<Integer> player2Deck) {
        while (!player1Deck.isEmpty() && !player2Deck.isEmpty()) {
            Integer player1Card = player1Deck.removeFirst();
            Integer player2Card = player2Deck.removeFirst();
            if (player1Card > player2Card) {
                player1Deck.addLast(player1Card);
                player1Deck.addLast(player2Card);
            } else {
                player2Deck.addLast(player2Card);
                player2Deck.addLast(player1Card);
            }
        }

        boolean isPlayer1Winner = player2Deck.isEmpty();
        List<Integer> winningDeck = isPlayer1Winner ? player1Deck : player2Deck;
        return new CrabCombatResult(isPlayer1Winner, winningDeck);
    }

    @Override
    public String solveGold() {
        CrabCombatResult combatResult = playRecursiveCombat(new LinkedList<>(player1Deck), new LinkedList<>(player2Deck));
        int score = calculateScore(combatResult.winningDeck);
        return String.format("The score of the winning player of Recursive Combat is %d.", score);
    }

    private CrabCombatResult playRecursiveCombat(LinkedList<Integer> player1Deck, LinkedList<Integer> player2Deck) {
        List<Integer[][]> memory = new LinkedList<>();
        while (!player1Deck.isEmpty() && !player2Deck.isEmpty()) {
            Integer[] cards1 = player1Deck.toArray(Integer[]::new);
            Integer[] cards2 = player2Deck.toArray(Integer[]::new);
            boolean ifRoundAlreadyPlayed = memory.stream()
                    .anyMatch(round -> Arrays.equals(round[0], cards1) && Arrays.equals(round[1], cards2));
            if (ifRoundAlreadyPlayed) {
                return new CrabCombatResult(true);
            }

            Integer player1Card = player1Deck.removeFirst();
            Integer player2Card = player2Deck.removeFirst();
            if (player1Deck.size() < player1Card || player2Deck.size() < player2Card) {
                if (player1Card > player2Card) {
                    player1Deck.addLast(player1Card);
                    player1Deck.addLast(player2Card);
                } else {
                    player2Deck.addLast(player2Card);
                    player2Deck.addLast(player1Card);
                }
            } else {
                LinkedList<Integer> newPlayer1Deck = extractSubDeck(player1Deck, player1Card);
                LinkedList<Integer> newPlayer2Deck = extractSubDeck(player2Deck, player2Card);
                Integer maxPlayer1 = maxCard(newPlayer1Deck);
                Integer maxPlayer2 = maxCard(newPlayer2Deck);
                if (maxPlayer1 > maxPlayer2) {
                    player1Deck.addLast(player1Card);
                    player1Deck.addLast(player2Card);
                } else {
                    CrabCombatResult combatResult = playRecursiveCombat(newPlayer1Deck, newPlayer2Deck);
                    if (combatResult.isPlayer1Winner) {
                        player1Deck.addLast(player1Card);
                        player1Deck.addLast(player2Card);
                    } else {
                        player2Deck.addLast(player2Card);
                        player2Deck.addLast(player1Card);
                    }
                }
            }
            memory.add(new Integer[][]{cards1, cards2});
        }

        boolean isPlayer1Winner = player2Deck.isEmpty();
        List<Integer> winningDeck = isPlayer1Winner ? player1Deck : player2Deck;
        return new CrabCombatResult(isPlayer1Winner, winningDeck);
    }

    private LinkedList<Integer> extractSubDeck(List<Integer> deck, Integer drewCard) {
        return deck.stream().limit(drewCard).collect(Collectors.toCollection(LinkedList::new));
    }

    private Integer maxCard(LinkedList<Integer> deck) {
        return deck.stream().max(Integer::compareTo).orElseThrow();
    }

    private int calculateScore(List<Integer> winningDeck) {
        int score = 0;
        for (int i = 0; i < winningDeck.size(); i++) {
            score += winningDeck.get(i) * (winningDeck.size() - i);
        }
        return score;
    }

    private static class CrabCombatResult {
        private final boolean isPlayer1Winner;
        private final List<Integer> winningDeck;

        private CrabCombatResult(boolean isPlayer1Winner) {
            this(isPlayer1Winner, null);
        }

        private CrabCombatResult(boolean isPlayer1Winner, List<Integer> winningDeck) {
            this.isPlayer1Winner = isPlayer1Winner;
            this.winningDeck = winningDeck;
        }
    }
}
