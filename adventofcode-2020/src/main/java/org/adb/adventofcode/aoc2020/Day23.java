package org.adb.adventofcode.aoc2020;

import java.util.ArrayList;
import java.util.List;

import org.adb.adventofcode.Solver;
import org.adb.adventofcode.io.FileResourceReader;

class Day23 implements Solver {

    private static final String INPUT_FILENAME = "aoc_day23.txt";

    private final List<Integer> initSequence;

    public Day23() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            long number = reader.nextLong();
            initSequence = parseInitSequence(number);
        }
    }

    private List<Integer> parseInitSequence(long number) {
        List<Integer> cups = new ArrayList<>(9);
        long rem = number;
        for (int n = 8; n >= 0; n--) {
            int n10 = (int) Math.pow(10, n);
            int value = (int) (rem / n10);
            rem = rem % n10;
            cups.add(value);
        }
        return cups;
    }

    @Override
    public void solveSilver() {
        final Cups cups = toCups();
        for (int n = 1; n <= 100; n++) {
            moveCups(cups);
        }

        String result = toCupsResult(cups);
        System.out.printf("The final result of simple crab cup is %s.%n", result);
    }

    private Cups toCups() {
        return toCups(9);
    }

    private void moveCups(Cups cups) {
        moveCups(cups, 9);
    }

    private String toCupsResult(Cups cups) {
        Cup cup = cups.getCup(1);
        StringBuilder builder = new StringBuilder(8);
        for (int n = 1; n <= 8; n++) {
            builder.append(cup.value);
            cup = cup.next;
        }
        return builder.toString();
    }

    @Override
    public void solveGold() {
        final int maxValue = 1000000;
        final Cups cups = toCups(maxValue);

        for (int n = 1; n <= 10000000; n++) {
            moveCups(cups, maxValue);
        }

        Cup cup1 = cups.getCup(1);
        long a = cup1.next.value;
        long b = cup1.next.next.value;
        long result = a * b;
        System.out.printf("The final result of big crab cup is %s.%n", result);
    }

    private Cups toCups(int maxValue) {
        List<Integer> cups = new ArrayList<>(maxValue);
        cups.addAll(initSequence);
        for (int n = 10; n <= maxValue; n++) {
            cups.add(n);
        }
        return new Cups(cups);
    }

    private void moveCups(Cups cups, int maxValue) {
        Cup currentCup = cups.getFirstCup();
        Cup pickUpsHead = currentCup.next;
        Cup pickUpsTail = pickUpsHead.next.next;
        List<Integer> pickUps = List.of(pickUpsHead.value, pickUpsHead.next.value, pickUpsTail.value);

        int destination = currentCup.value;
        do {
            destination = destination > 1 ? destination - 1 : maxValue;
        } while (pickUps.contains(destination));

        Cup destinationNode = cups.getCup(destination);
        Cup nextAfterDestination = destinationNode.next;
        currentCup.next = pickUpsTail.next;
        destinationNode.next = pickUpsHead;
        pickUpsTail.next = nextAfterDestination;
        cups.rearrange();
    }

    private static class Cups {
        private final Cup[] cups;

        private Cup head;

        private Cups(List<Integer> source) {
            cups = new Cup[source.size() + 1];

            head = null;
            for (int i = 0; i < source.size(); i++) {
                final int value = source.get(i);
                Cup cup = new Cup(value);
                if (head != null) {
                    cups[source.get(i - 1)].next = cup;
                } else {
                    head = cup;
                }
                cups[value] = cup;
                cup.next = head;
            }
        }

        private Cup getCup(int value) {
            return cups[value];
        }

        private void rearrange() {
            head = head.next;
        }

        private Cup getFirstCup() {
            return head;
        }
    }

    private static class Cup {
        private final int value;
        private Cup next;

        private Cup(int value) {
            this.value = value;
        }
    }
}
