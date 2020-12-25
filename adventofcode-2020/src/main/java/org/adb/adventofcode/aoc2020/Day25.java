package org.adb.adventofcode.aoc2020;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day25 implements Day {

    private static final String INPUT_FILENAME = "input_day25.txt";
    private static final long GEN_NUMBER = 7;
    private static final long REM_NUMBER = 20201227;

    private final long cardPubKey;
    private final long doorPubKey;

    public Day25() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            long[] input = reader.asLongStream().toArray();
            cardPubKey = input[0];
            doorPubKey = input[1];
        }
    }

    @Override
    public void solveSilver() {
        int cardLoopSize = crackLoopSize(cardPubKey);
        long encryptionKey = calculateEncryptionKey(doorPubKey, cardLoopSize);
        System.out.printf("The encryption key is %d.%n", encryptionKey);
    }

    private int crackLoopSize(long pubKey) {
        int loopSize = 1;
        long value = GEN_NUMBER;
        while (value != pubKey) {
            value = (value * GEN_NUMBER) % REM_NUMBER;
            loopSize +=1;
        }
        return loopSize;
    }

    private long calculateEncryptionKey(long subject, int loopSize) {
        long value = subject;
        do {
            value = (value * subject) % REM_NUMBER;
            loopSize--;
        } while (loopSize > 1);
        return value;
    }

    @Override
    public void solveGold() {
        System.out.printf("Merry Christmas!!%n");
    }
}
