package org.adb.adventofcode.aoc2020;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.adb.adventofcode.Solver;
import org.adb.adventofcode.io.FileResourceReader;
import org.adb.adventofcode.io.StringReader;

class Day8 implements Solver {

    private static final String INPUT_FILENAME = "aoc_day8.txt";

    private final List<Operation> code;
    private final CodeExecutionResult firstExecutionResult;

    public Day8() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            code = reader.parseLines(this::parseOperation).collect(Collectors.toList());
        }
        firstExecutionResult = executeCode(code);
    }

    private Operation parseOperation(String rawOperation) {
        try (StringReader operationReader = new StringReader(rawOperation)) {
            String opCode = operationReader.nextString();
            int value = operationReader.nextInt();
            return new Operation(opCode, value);
        }
    }

    @Override
    public void solveSilver() {
        System.out.printf("The value of the accumulator before the loop is %d.%n", firstExecutionResult.result);
    }

    @Override
    public void solveGold() {
        CodeExecutionResult executionResult = firstExecutionResult;
        List<Integer> executedOps = executionResult.executedOps;
        int i = executedOps.size() - 1;
        while (executionResult.looped && i >= 0) {
            int opIndex = executedOps.get(i);
            Operation originalOperation = code.get(opIndex);
            if (originalOperation.opCode != Operation.OpCode.ACC) {
                originalOperation.switchOpCode();
                executionResult = executeCode(code);
                if (executionResult.looped) {
                    originalOperation.switchOpCode();
                }
            }
            i--;
        }
        System.out.printf("The value of the accumulator after code has been fixed is %d.%n", executionResult.result);
    }

    private CodeExecutionResult executeCode(List<Operation> code) {
        int result = 0;
        boolean loopFound = false;
        List<Integer> executedOps = new LinkedList<>();
        for (int i = 0; i < code.size(); i++) {
            if (executedOps.contains(i)) {
                loopFound = true;
                break;
            }

            executedOps.add(i);
            Operation operation = code.get(i);
            switch (operation.opCode) {
                case ACC:
                    result += operation.value;
                    break;
                case JMP:
                    i += operation.value - 1;
                    break;
            }
        }
        return new CodeExecutionResult(result, loopFound, executedOps);
    }

    private static class Operation {
        private enum OpCode {
            ACC, JMP, NOP
        }

        private OpCode opCode;
        private final int value;

        private Operation(String opCode, int value) {
            this.opCode = OpCode.valueOf(opCode.toUpperCase());
            this.value = value;
        }

        private void switchOpCode() {
            if (opCode == OpCode.JMP) {
                opCode = OpCode.NOP;
            } else if (opCode == OpCode.NOP) {
                opCode = OpCode.JMP;
            }
        }
    }

    private static class CodeExecutionResult {
        private final int result;
        private final boolean looped;
        private final List<Integer> executedOps;

        private CodeExecutionResult(int result, boolean looped, List<Integer> executedOps) {
            this.result = result;
            this.looped = looped;
            this.executedOps = executedOps;
        }
    }
}
