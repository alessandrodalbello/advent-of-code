package org.adb.adventofcode.io;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class StringReader extends FastReader {

    public StringReader(String string) {
        super(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
    }

    public int[] nextIntSplit(String regex) {
        String[] tokens = split(regex);
        int[] array = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            array[i] = Integer.parseInt(tokens[i]);
        }
        return array;
    }

    public long[] nextLongSplit(String regex) {
        String[] tokens = split(regex);
        long[] array = new long[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            array[i] = Long.parseLong(tokens[i]);
        }
        return array;
    }

    public double[] nextDoubleSplit(String regex) {
        String[] tokens = split(regex);
        double[] array = new double[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            array[i] = Double.parseDouble(tokens[i]);
        }
        return array;
    }

    public char[] nextCharSplit(String regex) {
        String[] tokens = split(regex);
        char[] array = new char[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            array[i] = tokens[i].charAt(0);
        }
        return array;
    }

    public String[] nextSplit(String regex) {
        return split(regex);
    }

    private String[] split(String regex) {
        return next().split(regex);
    }
}
