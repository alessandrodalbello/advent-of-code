package org.adb.adventofcode.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

class FileResourceReaderTest {

    private static final String NUMBERS_TEST_FILENAME = "numbers_test.txt";
    private static final String STRINGS_TEST_FILENAME = "strings_test.txt";
    private static final String SPLIT_NUMBERS_TEST_FILENAME = "split_numbers_test.txt";
    private static final String SPLIT_STRINGS_TEST_FILENAME = "split_strings_test.txt";

    @Test
    void nextInt() {
        try (FileResourceReader unit = new FileResourceReader(NUMBERS_TEST_FILENAME)) {
            assertEquals(42, unit.nextInt());
            assertEquals(73, unit.nextInt());
            assertEquals(1337, unit.nextInt());
        }
    }

    @Test
    void nextLong() {
        try (FileResourceReader unit = new FileResourceReader(NUMBERS_TEST_FILENAME)) {
            assertEquals(42L, unit.nextLong());
            assertEquals(73L, unit.nextLong());
            assertEquals(1337L, unit.nextLong());
        }
    }

    @Test
    void nextDouble() {
        try (FileResourceReader unit = new FileResourceReader(NUMBERS_TEST_FILENAME)) {
            assertEquals(42d, unit.nextDouble());
            assertEquals(73d, unit.nextDouble());
            assertEquals(1337d, unit.nextDouble());
            assertEquals(16.08d, unit.nextDouble());
            assertEquals(19.7d, unit.nextDouble());
            assertEquals(248d, unit.nextDouble());
        }
    }

    @Test
    void nextChar() {
        try (FileResourceReader unit = new FileResourceReader(STRINGS_TEST_FILENAME)) {
            assertEquals('T', unit.nextChar());
            assertEquals('i', unit.nextChar());
            assertEquals('a', unit.nextChar());
            assertEquals('t', unit.nextChar());
            assertEquals('a', unit.nextChar());
            assertEquals('b', unit.nextChar());
            assertEquals('c', unit.nextChar());
        }
    }

    @Test
    void nextString() {
        try (FileResourceReader unit = new FileResourceReader(STRINGS_TEST_FILENAME)) {
            assertEquals("This", unit.nextString());
            assertEquals("is", unit.nextString());
            assertEquals("a", unit.nextString());
            assertEquals("test!", unit.nextString());
            assertEquals("a", unit.nextString());
            assertEquals("b", unit.nextString());
            assertEquals("c", unit.nextString());
        }
    }

    @Test
    void nextIntSplit() {
        try (FileResourceReader unit = new FileResourceReader(SPLIT_NUMBERS_TEST_FILENAME)) {
            assertArrayEquals(new int[]{42, 73, 1337}, unit.nextIntSplit("-"));
        }
    }

    @Test
    void nextLongSplit() {
        try (FileResourceReader unit = new FileResourceReader(SPLIT_NUMBERS_TEST_FILENAME)) {
            assertArrayEquals(new long[]{42L, 73L, 1337L}, unit.nextLongSplit("-"));
        }
    }

    @Test
    void nextDoubleSplit() {
        try (FileResourceReader unit = new FileResourceReader(SPLIT_NUMBERS_TEST_FILENAME)) {
            assertArrayEquals(new double[]{42d, 73d, 1337d}, unit.nextDoubleSplit("-"));
        }
    }

    @Test
    void nextCharSplit() {
        try (FileResourceReader unit = new FileResourceReader(SPLIT_STRINGS_TEST_FILENAME)) {
            assertArrayEquals(new char[]{'h', 'w'}, unit.nextCharSplit(":"));
        }
    }

    @Test
    void nextSplit() {
        try (FileResourceReader unit = new FileResourceReader(SPLIT_STRINGS_TEST_FILENAME)) {
            assertArrayEquals(new String[]{"hello", "world"}, unit.nextSplit(":"));
        }
    }

    @Test
    void nextIntArray() {
        try (FileResourceReader unit = new FileResourceReader(NUMBERS_TEST_FILENAME)) {
            assertArrayEquals(new int[]{42, 73, 1337}, unit.nextIntArray(3));
        }
    }

    @Test
    void nextLongArray() {
        try (FileResourceReader unit = new FileResourceReader(NUMBERS_TEST_FILENAME)) {
            assertArrayEquals(new long[]{42L, 73L, 1337L}, unit.nextLongArray(3));
        }
    }

    @Test
    void nextDoubleArray() {
        try (FileResourceReader unit = new FileResourceReader(NUMBERS_TEST_FILENAME)) {
            assertArrayEquals(new double[]{42d, 73d, 1337d}, unit.nextDoubleArray(3));
            assertArrayEquals(new double[]{16.08d, 19.7d, 248d}, unit.nextDoubleArray(3));
        }
    }

    @Test
    void nextCharArray() {
        try (FileResourceReader unit = new FileResourceReader(STRINGS_TEST_FILENAME)) {
            assertArrayEquals(new char[]{'T', 'i', 'a', 't'}, unit.nextCharArray(4));
            assertArrayEquals(new char[]{'a', 'b', 'c'}, unit.nextCharArray(3));
        }
    }

    @Test
    void asLines() {
        try (FileResourceReader unit = new FileResourceReader(STRINGS_TEST_FILENAME)) {
            assertArrayEquals(new String[]{"This is a test!", "a b c"},
                    unit.asLines().toArray(String[]::new));
        }
    }

    @Test
    void asIntStream() {
        try (FileResourceReader unit = new FileResourceReader(NUMBERS_TEST_FILENAME)) {
            assertArrayEquals(new int[]{42, 73, 1337}, unit.asIntStream().toArray());
        }
    }

    @Test
    void asLongStream() {
        try (FileResourceReader unit = new FileResourceReader(NUMBERS_TEST_FILENAME)) {
            assertArrayEquals(new long[]{42L, 73L, 1337L}, unit.asLongStream().toArray());
        }
    }

    @Test
    void asDoubleStream() {
        try (FileResourceReader unit = new FileResourceReader(NUMBERS_TEST_FILENAME)) {
            assertArrayEquals(new double[]{42d, 73d, 1337d, 16.08d, 19.7d, 248d}, unit.asDoubleStream().toArray());
        }
    }

    @Test
    void asStream() {
        try (FileResourceReader unit = new FileResourceReader(STRINGS_TEST_FILENAME)) {
            assertArrayEquals(new Object[]{"This", "is", "a", "test!", "a", "b", "c"},
                    unit.asStream(Function.identity()).toArray(Object[]::new));
        }
    }

    @Test
    void parseAsStream() {
        try (FileResourceReader unit = new FileResourceReader(NUMBERS_TEST_FILENAME)) {
            assertArrayEquals(new Object[]{new double[]{42d, 73d, 1337d}, new double[]{16.08d, 19.7d, 248d}},
                    unit.parseAsStream(reader -> reader.nextDoubleArray(3)).toArray(Object[]::new));
        }
    }

    @Test
    void nextLine() {
        try (FileResourceReader unit = new FileResourceReader(STRINGS_TEST_FILENAME)) {
            assertEquals("This is a test!", unit.nextLine());
            assertEquals("a b c", unit.nextLine());
        }
    }
}
