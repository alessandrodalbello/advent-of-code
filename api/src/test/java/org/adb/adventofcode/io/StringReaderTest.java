package org.adb.adventofcode.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StringReaderTest {

    @Test
    void nextInt() {
        try (StringReader unit = new StringReader("42 73 1337")) {
            assertEquals(42, unit.nextInt());
            assertEquals(73, unit.nextInt());
            assertEquals(1337, unit.nextInt());
        }
    }

    @Test
    void nextLong() {
        try (StringReader unit = new StringReader("42 73 1337")) {
            assertEquals(42L, unit.nextLong());
            assertEquals(73L, unit.nextLong());
            assertEquals(1337L, unit.nextLong());
        }
    }

    @Test
    void nextDouble() {
        try (StringReader unit = new StringReader("42 73 1337")) {
            assertEquals(42d, unit.nextDouble());
            assertEquals(73d, unit.nextDouble());
            assertEquals(1337d, unit.nextDouble());
        }
    }

    @Test
    void nextChar() {
        try (StringReader unit = new StringReader("42 73 1337")) {
            assertEquals('4', unit.nextChar());
            assertEquals('7', unit.nextChar());
            assertEquals('1', unit.nextChar());
        }
    }

    @Test
    void nextString() {
        try (StringReader unit = new StringReader("This is a test!")) {
            assertEquals("This", unit.nextString());
            assertEquals("is", unit.nextString());
            assertEquals("a", unit.nextString());
            assertEquals("test!", unit.nextString());
        }
    }

    @Test
    void nextIntSplit() {
        try (StringReader unit = new StringReader("42-73-1337")) {
            assertArrayEquals(new int[]{42, 73, 1337}, unit.nextIntSplit("-"));
        }
    }

    @Test
    void nextLongSplit() {
        try (StringReader unit = new StringReader("42-73-1337")) {
            assertArrayEquals(new long[]{42L, 73L, 1337L}, unit.nextLongSplit("-"));
        }
    }

    @Test
    void nextDoubleSplit() {
        try (StringReader unit = new StringReader("42-73-1337")) {
            assertArrayEquals(new double[]{42d, 73d, 1337d}, unit.nextDoubleSplit("-"));
        }
    }

    @Test
    void nextCharSplit() {
        try (StringReader unit = new StringReader("hello:world")) {
            assertArrayEquals(new char[]{'h', 'w'}, unit.nextCharSplit(":"));
        }
    }

    @Test
    void nextSplit() {
        try (StringReader unit = new StringReader("hello:world")) {
            assertArrayEquals(new String[]{"hello", "world"}, unit.nextSplit(":"));
        }
    }

    @Test
    void nextIntArray() {
        try (StringReader unit = new StringReader("42 73 1337")) {
            assertArrayEquals(new int[]{42, 73, 1337}, unit.nextIntArray(3));
        }
    }

    @Test
    void nextLongArray() {
        try (StringReader unit = new StringReader("42 73 1337")) {
            assertArrayEquals(new long[]{42L, 73L, 1337L}, unit.nextLongArray(3));
        }
    }

    @Test
    void nextDoubleArray() {
        try (StringReader unit = new StringReader("42 73 1337")) {
            assertArrayEquals(new double[]{42d, 73d, 1337d}, unit.nextDoubleArray(3));
        }
    }

    @Test
    void nextCharArray() {
        try (StringReader unit = new StringReader("This is a test!")) {
            assertArrayEquals(new char[]{'T', 'i', 'a', 't'}, unit.nextCharArray(4));
        }
    }

    @Test
    void nextLine() {
        try (StringReader unit = new StringReader("This is a test!\nAnother line")) {
            assertEquals("This is a test!", unit.nextLine());
            assertEquals("Another line", unit.nextLine());
        }
    }
}
