package org.adb.adventofcode.aoc2020;

import java.util.Arrays;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day09 implements Day {

    private static final String INPUT_FILENAME = "input_day9.txt";

    private final long[] numbers;

    public Day09() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            numbers = reader.asLongStream().toArray();
        }
    }

    @Override
    public String title() {
        return "Encoding Error";
    }

    @Override
    public String solveSilver() {
        long target = findTarget();
        return String.format("The first number which can't be obtained from the previous 25 is %d.", target);
    }

    private long findTarget() {
        long target = -1;
        int i = 25;
        while (target < 0 && i < numbers.length) {
            long[] subNumbers = Arrays.copyOfRange(numbers, i - 25, i);
            if (!isSumOfPreviousNumbers(numbers[i], subNumbers)) {
                target = numbers[i];
            }
            i++;
        }
        return target;
    }

    private boolean isSumOfPreviousNumbers(long target, long[] subNumbers) {
        Arrays.sort(subNumbers);
        boolean isSum = false;
        int i = 0;
        while (!isSum && i < subNumbers.length) {
            long toFind = Math.abs(target - subNumbers[i]);
            isSum = Arrays.binarySearch(subNumbers, toFind) >= 0;
            i++;
        }
        return isSum;
    }

    @Override
    public String solveGold() {
        long target = findTarget();
        long[] targetRange = findTargetRange(target);
        Arrays.sort(targetRange);
        long min = targetRange[0];
        long max = targetRange[targetRange.length - 1];
        return String.format("The sum of minimum and maximum values in the target range is %d.", min + max);
    }

    private long[] findTargetRange(long target) {
        int l = -1, h = -1;
        int i = 0;
        while (l < 0 && i < numbers.length) {
            long sum = 0;
            int j = i;
            while (sum < target && j < numbers.length) {
                sum += numbers[j];
                j++;
            }
            if (sum == target && j > i + 1) {
                l = i;
                h = j;
            }
            i++;
        }
        return Arrays.copyOfRange(numbers, l, h);
    }
}
