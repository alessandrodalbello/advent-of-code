package org.adb.adventofcode.aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.adb.adventofcode.Day;
import org.adb.adventofcode.io.FileResourceReader;

class Day20 implements Day {

    private static final String INPUT_FILENAME = "input_day20.txt";

    private static final int MONSTER_LENGTH = 20;
    private static final int MONSTER_HEIGHT = 3;
    private static final String[] MONSTER = new String[]{
            "                  # ",
            "#    ##    ##    ###",
            " #  #  #  #  #  #   "
    };

    private final List<Tile> tiles;
    private final Tile[][] completeImage;

    public Day20() {
        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            tiles = reader.parseMultilines(this::parseTile).collect(Collectors.toList());
        }
        int imageSize = (int) Math.sqrt(tiles.size());
        completeImage = new Tile[imageSize][imageSize];
    }

    private Tile parseTile(String rawTile) {
        String[] tileParts = rawTile.split(":\\n");
        long id = parseId(tileParts[0]);
        char[][] image = parseImage(tileParts[1]);
        return new Tile(id, image);
    }

    private long parseId(String rawId) {
        return Long.parseLong(rawId.replaceAll("\\D", ""));
    }

    private char[][] parseImage(String rawImage) {
        List<String> imageLines = rawImage.lines().collect(Collectors.toList());
        char[][] image = new char[imageLines.size()][imageLines.size()];
        for (int i = 0; i < image.length; i++) {
            String imageLine = imageLines.get(i);
            image[i] = imageLine.toCharArray();
        }
        return image;
    }

    @Override
    public void solveSilver() {
        Tile firstCorner = tiles.stream()
                .filter(this::isTileCorner)
                .findFirst().orElseThrow();
        firstCorner.image = rotateToTopLeft(firstCorner);
        completeImage[0][0] = firstCorner;

        int imageSize = completeImage.length;
        for (int j = 0; j < imageSize - 1; j++) {
            Tile adjacentTile = findAdjacent(completeImage[0][j], this::borderE, this::borderW);
            completeImage[0][j + 1] = adjacentTile;
        }
        for (int i = 0; i < imageSize - 1; i++) {
            for (int j = 0; j < imageSize; j++) {
                Tile adjacentTile = findAdjacent(completeImage[i][j], this::borderS, this::borderN);
                completeImage[i + 1][j] = adjacentTile;
            }
        }

        long cornersProduct = completeImage[0][0].id
                * completeImage[0][imageSize - 1].id
                * completeImage[imageSize - 1][0].id
                * completeImage[imageSize - 1][imageSize - 1].id;
        System.out.printf("The product of corners is %d.%n", cornersProduct);
    }

    private boolean isTileCorner(Tile tile) {
        char[] borderN = borderN(tile.image);
        char[] borderS = borderS(tile.image);
        char[] borderW = borderW(tile.image);
        char[] borderE = borderE(tile.image);
        return Stream.of(borderN, borderS, borderW, borderE)
                .filter(border -> isBorderMatchingAnotherTile(tile.id, border))
                .count() == 2L;
    }

    private boolean isBorderMatchingAnotherTile(long tileId, char[] border) {
        return tiles.stream()
                .filter(tile -> tile.id != tileId)
                .anyMatch(tile -> isBorderMatchingTile(border, tile));
    }

    private boolean isBorderMatchingTile(char[] border, Tile otherTile) {
        char[] borderN = borderN(otherTile.image);
        char[] borderS = borderS(otherTile.image);
        char[] borderW = borderW(otherTile.image);
        char[] borderE = borderE(otherTile.image);
        return Arrays.equals(border, borderN) || Arrays.equals(border, flipBorder(borderN))
                || Arrays.equals(border, borderS) || Arrays.equals(border, flipBorder(borderS))
                || Arrays.equals(border, borderW) || Arrays.equals(border, flipBorder(borderW))
                || Arrays.equals(border, borderE) || Arrays.equals(border, flipBorder(borderE));
    }

    private char[] flipBorder(char[] border) {
        char[] flippedBorder = new char[border.length];
        for (int i = 0; i < border.length; i++) {
            flippedBorder[i] = border[border.length - 1 - i];
        }
        return flippedBorder;
    }

    private char[][] rotateToTopLeft(Tile tile) {
        char[][] image = tile.image;
        boolean isOriented = false;
        while (!isOriented) {
            char[] borderE = borderE(image);
            char[] borderS = borderS(image);
            isOriented = isBorderMatchingAnotherTile(tile.id, borderE) && isBorderMatchingAnotherTile(tile.id, borderS);
            if (!isOriented) {
                image = rotateImage(image);
            }
        }
        return image;
    }

    private Tile findAdjacent(Tile tile,
                              Function<char[][], char[]> borderExtractor,
                              Function<char[][], char[]> matchingBorderExtractor) {
        char[] border = borderExtractor.apply(tile.image);
        return findAdjacent(tile.id, border, matchingBorderExtractor);
    }

    private Tile findAdjacent(long tileId, char[] border, Function<char[][], char[]> matchingBorderExtractor) {
        int i = 0;
        Tile matchingTile = null;
        while (matchingTile == null && i < tiles.size()) {
            Tile otherTile = tiles.get(i);
            if (otherTile.id != tileId) {
                char[][] otherImage = otherTile.image;
                char[][] matchingImage = searchMatching(border, otherImage, matchingBorderExtractor);
                if (matchingImage == null) {
                    matchingImage = searchMatching(border, flipImage(otherImage), matchingBorderExtractor);
                }
                if (matchingImage != null) {
                    matchingTile = otherTile;
                    matchingTile.image = matchingImage;
                }
            }
            i++;
        }
        return matchingTile;
    }

    private char[][] searchMatching(char[] border, char[][] otherImage,
                                    Function<char[][], char[]> matchingBorderExtractor) {
        boolean isMatching = false;
        int count = 1;
        while (!isMatching && count <= 4) {
            isMatching = Arrays.equals(border, matchingBorderExtractor.apply(otherImage));
            if (!isMatching) {
                otherImage = rotateImage(otherImage);
            }
            count++;
        }
        return isMatching ? otherImage : null;
    }

    private char[] borderN(char[][] image) {
        return image[0];
    }

    private char[] borderS(char[][] image) {
        return image[image.length - 1];
    }

    private char[] borderW(char[][] image) {
        char[] border = new char[image.length];
        for (int i = 0; i < image.length; i++) {
            border[i] = image[i][0];
        }
        return border;
    }

    private char[] borderE(char[][] image) {
        char[] border = new char[image.length];
        for (int i = 0; i < image.length; i++) {
            border[i] = image[i][image.length - 1];
        }
        return border;
    }

    private char[][] flipImage(char[][] image) {
        char[][] flippedImage = new char[image.length][image.length];
        for (int i = 0; i < flippedImage.length; i++) {
            flippedImage[i] = image[image.length - 1 - i];
        }
        return flippedImage;
    }

    private char[][] rotateImage(char[][] image) {
        char[][] rotatedImage = new char[image.length][image.length];
        for (int i = 0; i < rotatedImage.length; i++) {
            for (int j = 0; j < rotatedImage.length; j++) {
                rotatedImage[i][j] = image[j][image.length - 1 - i];
            }
        }
        return rotatedImage;
    }

    @Override
    public void solveGold() {
        for (Tile tile : tiles) {
            tile.image = cropTileImage(tile.image);
        }
        char[][] finalImage = mergeTiles();
        int roughWaters = countRoughWaters(finalImage);
        System.out.printf("There are %d rough waters in the final image.%n", roughWaters);
    }

    private char[][] cropTileImage(char[][] image) {
        char[][] croppedImage = new char[image.length - 2][image.length - 2];
        for (int i = 0; i < croppedImage.length; i++) {
            System.arraycopy(image[i + 1], 1, croppedImage[i], 0, croppedImage.length);
        }
        return croppedImage;
    }

    private char[][] mergeTiles() {
        int tileSize = tiles.get(0).image.length;
        int mergedImageSize = tileSize * completeImage.length;
        char[][] mergedImage = new char[mergedImageSize][mergedImageSize];
        for (int i = 0; i < completeImage.length; i++) {
            for (int j = 0; j < completeImage.length; j++) {
                Tile tile = completeImage[i][j];
                for (int z = 0; z < tileSize; z++) {
                    System.arraycopy(tile.image[z], 0, mergedImage[i * tileSize + z], j * tileSize, tileSize);
                }
            }
        }
        return mergedImage;
    }

    private int countRoughWaters(char[][] image) {
        List<int[]> monsterOffsets = monsterOffsets();

        int monstersNumb = countMonsters(image, monsterOffsets);
        if (monstersNumb == 0) {
            image = flipImage(image);
            monstersNumb = countMonsters(image, monsterOffsets);
        }

        int roughWaters = 0;
        for (char[] chars : image) {
            for (int j = 0; j < image.length; j++) {
                if (chars[j] == '#') {
                    roughWaters += 1;
                }
            }
        }
        return roughWaters - monstersNumb * monsterOffsets.size();
    }

    private List<int[]> monsterOffsets() {
        List<int[]> offsets = new ArrayList<>();
        for (int i = 0; i < MONSTER_HEIGHT; i++) {
            String monsterLine = MONSTER[i];
            for (int j = 0; j < monsterLine.length(); j++) {
                if (monsterLine.charAt(j) == '#') {
                    offsets.add(new int[]{i, j});
                }
            }
        }
        return offsets;
    }

    private int countMonsters(char[][] image, List<int[]> monsterOffsets) {
        int monstersNumb = 0;
        int rotations = 1;
        while (monstersNumb == 0 && rotations <= 4) {
            monstersNumb = searchMonstersInImage(image, monsterOffsets);
            if (monstersNumb == 0) {
                image = rotateImage(image);
            }
            rotations++;
        }
        return monstersNumb;
    }

    private int searchMonstersInImage(char[][] image, List<int[]> monsterOffsets) {
        int monstersNumb = 0;
        for (int i = 0; i < image.length - MONSTER_HEIGHT; i++) {
            for (int j = 0; j < image.length - MONSTER_LENGTH; j++) {
                boolean isMonster = true;
                int n = 0;
                while (isMonster && n < monsterOffsets.size()) {
                    int[] offset = monsterOffsets.get(n);
                    isMonster = image[i + offset[0]][j + offset[1]] == '#';
                    n++;
                }
                if (isMonster) {
                    monstersNumb += 1;
                }
            }
        }
        return monstersNumb;
    }

    private static class Tile {
        private final long id;
        private char[][] image;

        private Tile(long id, char[][] image) {
            this.id = id;
            this.image = image;
        }
    }
}