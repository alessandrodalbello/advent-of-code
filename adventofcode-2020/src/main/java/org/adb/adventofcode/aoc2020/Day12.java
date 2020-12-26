package org.adb.adventofcode.aoc2020;

import java.util.List;
import java.util.stream.Collectors;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day12 implements Day {

    private static final String INPUT_FILENAME = "input_day12.txt";

    private final List<ShipCommand> shipCommands;

    public Day12() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            shipCommands = reader.parseLines(this::parseShipCommand).collect(Collectors.toList());
        }
    }

    private ShipCommand parseShipCommand(String rawCommand) {
        char command = rawCommand.charAt(0);
        int move = Integer.parseInt(rawCommand.substring(1));
        return new ShipCommand(command, move);
    }

    @Override
    public String title() {
        return "Rain Risk";
    }

    @Override
    public String solveSilver() {
        char direction = 'E';
        int northOffset = 0;
        int eastOffset = 0;
        for (ShipCommand shipCommand : shipCommands) {
            if (shipCommand.command == 'R') {
                if (shipCommand.move == 90) {
                    if (direction == 'E') {
                        direction = 'S';
                    } else if (direction == 'S') {
                        direction = 'W';
                    } else if (direction == 'W') {
                        direction = 'N';
                    } else {
                        direction = 'E';
                    }
                } else if (shipCommand.move == 180) {
                    if (direction == 'E') {
                        direction = 'W';
                    } else if (direction == 'S') {
                        direction = 'N';
                    } else if (direction == 'W') {
                        direction = 'E';
                    } else {
                        direction = 'S';
                    }
                } else if (shipCommand.move == 270) {
                    if (direction == 'E') {
                        direction = 'N';
                    } else if (direction == 'S') {
                        direction = 'E';
                    } else if (direction == 'W') {
                        direction = 'S';
                    } else {
                        direction = 'W';
                    }
                }
            } else if (shipCommand.command == 'L') {
                if (shipCommand.move == 90) {
                    if (direction == 'E') {
                        direction = 'N';
                    } else if (direction == 'S') {
                        direction = 'E';
                    } else if (direction == 'W') {
                        direction = 'S';
                    } else {
                        direction = 'W';
                    }
                } else if (shipCommand.move == 180) {
                    if (direction == 'E') {
                        direction = 'W';
                    } else if (direction == 'S') {
                        direction = 'N';
                    } else if (direction == 'W') {
                        direction = 'E';
                    } else {
                        direction = 'S';
                    }
                } else if (shipCommand.move == 270) {
                    if (direction == 'E') {
                        direction = 'S';
                    } else if (direction == 'S') {
                        direction = 'W';
                    } else if (direction == 'W') {
                        direction = 'N';
                    } else {
                        direction = 'E';
                    }
                }
            } else if (shipCommand.command == 'F') {
                if (direction == 'E') {
                    eastOffset += shipCommand.move;
                } else if (direction == 'W') {
                    eastOffset -= shipCommand.move;
                } else if (direction == 'N') {
                    northOffset += shipCommand.move;
                } else {
                    northOffset -= shipCommand.move;
                }
            } else if (shipCommand.command == 'E') {
                eastOffset += shipCommand.move;
            } else if (shipCommand.command == 'W') {
                eastOffset -= shipCommand.move;
            } else if (shipCommand.command == 'N') {
                northOffset += shipCommand.move;
            } else {
                northOffset -= shipCommand.move;
            }
        }
        int manhattanDistance = manhattanDistance(northOffset, eastOffset);
        return String.format("The Manhattan distance using the silver command system is %d.", manhattanDistance);
    }

    @Override
    public String solveGold() {
        int wpNorth = 1;
        int wpEast = 10;
        int northOffset = 0;
        int eastOffset = 0;
        for (ShipCommand shipCommand : shipCommands) {
            if (shipCommand.command == 'R') {
                if (shipCommand.move == 90) {
                    int wp = wpEast;
                    wpEast = wpNorth;
                    wpNorth = -wp;
                } else if (shipCommand.move == 180) {
                    wpNorth = -wpNorth;
                    wpEast = -wpEast;
                } else if (shipCommand.move == 270) {
                    int wp = wpEast;
                    wpEast = -wpNorth;
                    wpNorth = wp;
                }
            } else if (shipCommand.command == 'L') {
                if (shipCommand.move == 90) {
                    int wp = wpEast;
                    wpEast = -wpNorth;
                    wpNorth = wp;
                } else if (shipCommand.move == 180) {
                    wpEast = -wpEast;
                    wpNorth = -wpNorth;
                } else if (shipCommand.move == 270) {
                    int wp = wpEast;
                    wpEast = wpNorth;
                    wpNorth = -wp;
                }
            } else if (shipCommand.command == 'F') {
                northOffset += wpNorth * shipCommand.move;
                eastOffset += wpEast * shipCommand.move;
            } else if (shipCommand.command == 'E') {
                wpEast += shipCommand.move;
            } else if (shipCommand.command == 'W') {
                wpEast -= shipCommand.move;
            } else if (shipCommand.command == 'N') {
                wpNorth += shipCommand.move;
            } else {
                wpNorth -= shipCommand.move;
            }
        }
        int manhattanDistance = manhattanDistance(northOffset, eastOffset);
        return String.format("The Manhattan distance using the gold command system is %d.", manhattanDistance);
    }

    private int manhattanDistance(int northOffset, int eastOffset) {
        return Math.abs(northOffset) + Math.abs(eastOffset);
    }

    private static class ShipCommand {
        private final char command;
        private final int move;

        private ShipCommand(char command, int move) {
            this.command = command;
            this.move = move;
        }
    }
}
