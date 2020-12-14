package org.adb.adventofcode.aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.adb.adventofcode.Solver;
import org.adb.adventofcode.io.FileResourceReader;
import org.adb.adventofcode.io.StringReader;

class Day14 implements Solver {

    private static final String INPUT_FILENAME = "aoc_day14.txt";
    private static final Pattern MEMORY_OPERATION_PATTERN = Pattern.compile("mem\\[(\\d+)\\]");

    private final List<Operation> code;

    public Day14() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            code = reader.parseLines(this::parseOperation).collect(Collectors.toList());
        }
    }

    private Operation parseOperation(String rawOperation) {
        try (StringReader reader = new StringReader(rawOperation)) {
            String command = reader.nextString();
            reader.nextString();
            if ("mask".equals(command)) {
                return new MaskOperation(reader.nextString());
            } else {
                Matcher matcher = MEMORY_OPERATION_PATTERN.matcher(command);
                if (matcher.find()) {
                    long address = Long.parseLong(matcher.group(1));
                    long value = reader.nextLong();
                    return new MemoryOperation(address, value);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    @Override
    public void solveSilver() {
        Map<Long, Long> memory = new HashMap<>();
        String mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        for (Operation operation : code) {
            if (operation instanceof MaskOperation) {
                mask = ((MaskOperation) operation).mask;
            } else {
                MemoryOperation memoryOperation = (MemoryOperation) operation;
                long value = memoryOperation.value;
                for (int i = 0; i < mask.length(); i++) {
                    if (mask.charAt(i) != 'X') {
                        int shiftPos = mask.length() - 1 - i;
                        if (mask.charAt(i) == '1') {
                            value |= 1L << shiftPos;
                        } else if (mask.charAt(i) == '0') {
                            value &= ~(1L << shiftPos);
                        }
                    }
                }
                memory.put(memoryOperation.address, value);
            }
        }

        long memoryTotalValue = sumOfMemory(memory);
        System.out.printf("The sum of non-zero memory addresses is %d.%n", memoryTotalValue);
    }

    @Override
    public void solveGold() {
        Map<Long, Long> memory = new HashMap<>();
        String mask = "000000000000000000000000000000000000";
        for (Operation operation : code) {
            if (operation instanceof MaskOperation) {
                mask = ((MaskOperation) operation).mask;
            } else {
                MemoryOperation memoryOperation = (MemoryOperation) operation;

                List<char[]> binaryAddresses = new ArrayList<>();
                String binaryAddress = String.format("%36s", Long.toBinaryString(memoryOperation.address))
                        .replaceAll(" ", "0");
                binaryAddresses.add(binaryAddress.toCharArray());

                for (int i = 0; i < mask.length(); i++) {
                    if (mask.charAt(i) != '0') {
                        for (char[] address : binaryAddresses) {
                            address[i] = '1';
                        }
                        if (mask.charAt(i) == 'X') {
                            final int j = i;
                            List<char[]> newAddresses = binaryAddresses.stream()
                                    .map(address -> Arrays.copyOf(address, address.length))
                                    .peek(address -> address[j] = '0')
                                    .collect(Collectors.toList());
                            binaryAddresses.addAll(newAddresses);
                        }
                    }
                }

                binaryAddresses.stream()
                        .mapToLong(address -> Long.parseLong(new String(address), 2))
                        .forEach(address -> memory.put(address, memoryOperation.value));
            }
        }

        long memoryTotalValue = sumOfMemory(memory);
        System.out.printf("The sum of non-zero memory addresses is %d.%n", memoryTotalValue);
    }

    private long sumOfMemory(Map<Long, Long> memory) {
        return memory.values().stream().reduce(0L, Long::sum);
    }

    private interface Operation {
    }

    private static class MaskOperation implements Operation {
        private final String mask;

        private MaskOperation(String mask) {
            this.mask = mask;
        }
    }

    private static class MemoryOperation implements Operation {
        private final long address;
        private final long value;

        private MemoryOperation(long address, long value) {
            this.address = address;
            this.value = value;
        }
    }
}
